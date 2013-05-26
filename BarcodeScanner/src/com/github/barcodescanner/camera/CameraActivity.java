package com.github.barcodescanner.camera;

import java.util.ArrayList;
import java.util.List;

import com.github.barcodescanner.R;
import com.github.barcodescanner.activities.AddNewActivity;
import com.github.barcodescanner.activities.BarcodeViewActivity;
import com.github.barcodescanner.activities.NoProductActivity;
import com.github.barcodescanner.activities.ProductActivity;
import com.github.barcodescanner.core.BCGenerator;
import com.github.barcodescanner.core.BCLocator;
import com.github.barcodescanner.core.Product;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class CameraActivity extends Activity {
	private static final String TAG = "CameraActivity";

	private Preview mPreview;
	private boolean mPreviewRunning;
	private Camera mCamera;
	private boolean isOwner;

	private Handler autoFocusHandler;

	private DatabaseHelper database;

	private BCLocator bcLocator;
	private BCGenerator bcGenerator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);
		isOwner = getIntent().getExtras().getBoolean("isOwner");

		// Setup the database
		setupDatabase();

		// prepare the camera for being able to scan barcodes
		bcLocator = new BCLocator();
		bcGenerator = new BCGenerator();
	}

	private void setupDatabase() {
		DatabaseHelperFactory.init(this);
		database = DatabaseHelperFactory.getInstance();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		mCamera = getCameraInstance();
		System.err.println("mCamera is: " + mCamera);
		mPreview = new Preview(this, mCamera);
		mPreviewRunning = true;
		autoFocusHandler = new Handler();
		mPreview.setAutoFocusCallback(autoFocusCallback);
		// gets the FrameLayout camera_preview in activity_camera.xml
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
		// adds the mPreview view to that FrameLayout
		frameLayout.addView(mPreview);
	}

	private static Camera getCameraInstance() {
		Camera camera = null;

		try {
			camera = Camera.open();
		} catch (Exception e) {
			Log.e(TAG, "Exception when opening the camera", e);
		}
		return camera;
	}

	@Override
	protected void onPause() {
		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		releaseCamera();

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
		// adds the mPreview view to that FrameLayout
		frameLayout.removeView(mPreview);

		super.onPause();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mPreviewRunning = false;
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			// mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private PictureCallback pictureCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			String tempBarcode = null;
			List<Integer> tempList = new ArrayList<Integer>();

			bcLocator.setData(data);
			boolean foundBarcode = bcLocator.foundBarcode();

			if (foundBarcode) {
				tempList = bcGenerator.generate(bcLocator.getSegment());
				tempBarcode = bcGenerator.normalize(tempList);

				if (tempBarcode.length() == 12) {

					System.err.println("bcGenerator: " + tempBarcode);

					List<Product> products = database.getProducts();
					Product foundProduct = null;
					if (products != null) {
						for (Product p : products) {
							boolean isSame = (bcGenerator.compare(p.getBarcode(), tempBarcode) <= 1);
							if (isSame) {
								foundProduct = p;
								break;
							}
						}
					}
					checkBarcode(foundProduct, tempBarcode);
				}
			}
		}
	};

	public void startBarcodeViewActivity() {
		Intent intent = new Intent(this, BarcodeViewActivity.class);
		intent.putExtra("barcodeBitmap", bcLocator.getSegmentedBitmap());
		startActivity(intent);
	}

	public void takePicture(View view) {
		mCamera.takePicture(null, null, pictureCallback);
	}

	private void checkBarcode(Product matchingProduct, String barcode) {
		// Initialize the bundle that we will send to the productActivity
		Bundle productBundle = new Bundle();
		Intent productIntent;

		// Check if the database contained a matching product
		if (matchingProduct != null) {
			// Get product name
			String productName = matchingProduct.getName();
			
			// Get product price
			int productPrice = matchingProduct.getPrice();

			// Put product name in bundle
			productBundle.putString("productName", productName);
			
			// Put product price in bundle
			productBundle.putInt("productPrice", productPrice);

			// Set ProductActivity as the intent
			productIntent = new Intent(this, ProductActivity.class);
		} else if (isOwner) {
			// Put new product ID in bundle
			productBundle.putString("productID", barcode);

			// Set AddNewActivity as the intent
			productIntent = new Intent(this, AddNewActivity.class);
		} else {
			// Put new product ID in bundle
			productBundle.putString("productID", barcode);

			// Set NoProductActivity as the intent
			productIntent = new Intent(this, NoProductActivity.class);
		}

		// Add the bundle to the intent, and start the requested activity
		productIntent.putExtras(productBundle);
		startActivity(productIntent);
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (mPreviewRunning)
				mCamera.autoFocus(autoFocusCallback);
		}
	};

	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 5000);
		}
	};
	
}
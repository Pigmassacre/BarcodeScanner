package com.github.barcodescanner.camera;

import java.util.ArrayList;
import java.util.List;

import com.github.barcodescanner.R;
import com.github.barcodescanner.barcode.BCGenerator;
import com.github.barcodescanner.barcode.BCLocator;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;
import com.github.barcodescanner.debug.BarcodeViewActivity;
import com.github.barcodescanner.product.AddNewProductActivity;
import com.github.barcodescanner.product.Product;
import com.github.barcodescanner.product.ProductActivity;

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

	// TODO Fix aspect ratio
	// TODO If possible, fix so it scans like 10 times per second, instead of
	// pressing a button to scan

	private static final String TAG = "CameraActivity";

	private Preview mPreview;
	private boolean mPreviewRunning;
	private Camera mCamera;

	private Handler autoFocusHandler;

	private DatabaseHelper database;

	private BCLocator bcLocator;
	private BCGenerator bcGenerator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_camera);

		setupDatabase();
		prepareScanner();
	}

	/**
	 * Gives the class access to the database.
	 */
	private void setupDatabase() {
		DatabaseHelperFactory.init(this);
		database = DatabaseHelperFactory.getInstance();
	}

	/**
	 * Instantiates the necessary classes needed to scan barcodes.
	 */
	private void prepareScanner() {
		bcLocator = new BCLocator();
		bcGenerator = new BCGenerator();
	}

	@Override
	protected void onResume() {
		super.onResume();

		mCamera = getCameraInstance();

		mPreview = new Preview(this, mCamera);
		mPreviewRunning = true;

		autoFocusHandler = new Handler();
		mPreview.setAutoFocusCallback(autoFocusCallback);

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
		frameLayout.addView(mPreview);
	}

	private static Camera getCameraInstance() {
		Camera camera = null;

		try {
			camera = Camera.open();
		} catch (Exception e) {
			Log.e(TAG, "Exception when opening camera: ", e);
		}
		return camera;
	}

	@Override
	protected void onPause() {
		releaseCamera();

		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
		frameLayout.removeView(mPreview);

		super.onPause();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mPreviewRunning = false;
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
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
					List<Product> products = database.getProducts();
					Product foundProduct = null;
					if (products != null) {
						for (Product p : products) {
							System.out.println("onPictureTaken: p.getBarcode() gives: " + p.getBarcode());
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

		/*
		 * If the database contains a matching product, package that products
		 * info in a bundle and then send that info to the requested activity.
		 * Else, let the owner create a product with the found id.
		 */
		productBundle.putString("productId", barcode);
		if (matchingProduct != null) {
			String productName = matchingProduct.getName();
			int productPrice = matchingProduct.getPrice();

			productBundle.putString("productName", productName);
			productBundle.putInt("productPrice", productPrice);

			productIntent = new Intent(this, ProductActivity.class);
		} else {
			productIntent = new Intent(this, AddNewProductActivity.class);
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
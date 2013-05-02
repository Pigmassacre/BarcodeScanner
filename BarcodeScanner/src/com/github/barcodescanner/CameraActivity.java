package com.github.barcodescanner;

import com.github.barcodescanner.core.DatabaseHelper;
import com.github.barcodescanner.core.DatabaseHelperFactory;
import com.github.barcodescanner.core.Product;

import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.app.Activity;
import android.content.Intent;

public class CameraActivity extends Activity {

	private static final String TAG = "CameraActivity";
	private Camera mCamera;
	private CameraPreview mPreview;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private String barcodeInfo;
	private ImageScanner scanner;
	private boolean previewing = true;
	private Handler autoFocusHandler;
	private DatabaseHelper database;

	static {
		System.loadLibrary("iconv");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_camera);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Create an instance of Camera
		mCamera = getCameraInstance();

		autoFocusHandler = new Handler();

		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);

		// Get instance of DatabaseHelper class
		database = DatabaseHelperFactory.getInstance();

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera, previewCallback,
				autoFocusCallback);

		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	public static Camera getCameraInstance() {
		Camera camera = null;

		try {
			camera = Camera.open();
		} catch (Exception e) {
			Log.d(TAG, "Exception when opening the camera", e);
		}
		return camera;
	}

	PreviewCallback previewCallback = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				previewing = false;
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();

				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					barcodeInfo = sym.getData();
					viewProduct();
				}
			}
		}
	};

	private void viewProduct() {
		// Initialize the bundle that we will send to the productActivity
		Bundle productBundle = new Bundle();
		Intent productIntent;
		
		// Just for now we make barcode shorter before we change int to long in
		// database
		barcodeInfo = barcodeInfo.substring(0, 6);
		
		// Cast scanned barcode to an int
		int barcode = (int) Integer.parseInt(barcodeInfo);
		
		// Get product from database, returns null if nonexisting
		Product product = database.getProduct(barcode);
		
		// Check if database returned a product (it exists in database)
		if (product != null) {
			// Get product name
			String productName = product.getName();
			
			// Get product price
			int productPrice = product.getPrice();

			// Put product name in bundle
			productBundle.putString("productName", productName);
			
			// Put product price in bundle
			productBundle.putInt("productPrice", productPrice);
			
			// Set ProductActivity as the intent
			productIntent = new Intent(this, ProductActivity.class);
		} else {
			// Put new product ID in bundle
			productBundle.putString("productID", barcodeInfo);
			
			// Set AddNewActivity as the intent
			productIntent = new Intent(this, AddNewActivity.class);
		}

		// Add the bundle to the intent, and start the requested activity
		productIntent.putExtras(productBundle);
		startActivity(productIntent);
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (previewing)
				mCamera.autoFocus(autoFocusCallback);
		}
	};

	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	/**
	 * Method to release the camera
	 * */
	private void releaseCamera() {
		if (mCamera != null) {
			previewing = false;
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}
}

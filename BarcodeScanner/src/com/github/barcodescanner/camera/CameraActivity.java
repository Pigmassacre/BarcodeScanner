package com.github.barcodescanner.camera;

import com.github.barcodescanner.AddNewActivity;
import com.github.barcodescanner.ProductActivity;
import com.github.barcodescanner.core.DatabaseHelper;
import com.github.barcodescanner.core.DatabaseHelperFactory;
import com.github.barcodescanner.core.Product;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

// ----------------------------------------------------------------------

public class CameraActivity extends Activity {
	private static final String TAG = "CameraActivity";

	private Preview mPreview;
	private boolean mPreviewRunning;
	private Camera mCamera;
	private int numberOfCameras;
	private int cameraCurrentlyLocked;

	// The first rear facing camera
	private int defaultCameraId;

	private Handler autoFocusHandler;

	private String barcodeData;
	private ImageScanner scanner;

	private DatabaseHelper database;

	// Load zbar library
	static {
		System.loadLibrary("iconv");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hide the window title.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create a RelativeLayout container that will hold a SurfaceView,
		// and set it as the content of our activity.
		setupPreview();

		// Find the ID of the default camera
		findDefaultCameraId();

		// Configure the ZBar scanner
		setupScanner();

		// Setup autofocus handler
		setupAutoFocus();

		// Setup the database
		setupDatabase();
	}

	private void setupPreview() {
		mPreview = new Preview(this);
		setContentView(mPreview);
		mPreviewRunning = true;
		mPreview.setPreviewCallback(previewCallback);
	}

	private void findDefaultCameraId() {
		numberOfCameras = Camera.getNumberOfCameras();

		CameraInfo cameraInfo = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, cameraInfo);
			if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
				defaultCameraId = i;
			}
		}
	}

	private void setupScanner() {
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);
	}

	private void setupAutoFocus() {
		autoFocusHandler = new Handler();
		mPreview.setAutoFocusCallback(autoFocusCallback);
	}

	private void setupDatabase() {
		database = DatabaseHelperFactory.getInstance();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Open the default i.e. the first rear facing camera.
		mCamera = getCameraInstance();
		cameraCurrentlyLocked = defaultCameraId;
		mPreview.setCamera(mCamera);
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
		super.onPause();

		// Because the Camera object is a shared resource, it's very
		// important to release it when the activity is paused.
		releaseCamera();
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mPreviewRunning = false;
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mPreview.setCamera(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private PreviewCallback previewCallback = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			Camera.Parameters parameters = camera.getParameters();
			Size size = parameters.getPreviewSize();

			Image barcode = new Image(size.width, size.height, "Y800");
			barcode.setData(data);

			int result = scanner.scanImage(barcode);

			if (result != 0) {
				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					barcodeData = sym.getData();
					checkBarcode();
				}
			}
		}
	};

	private void checkBarcode() {
		// Initialize the bundle that we will send to the productActivity
		Bundle productBundle = new Bundle();
		Intent productIntent;

		// Just for now we make barcode shorter before we change int to long in
		// database
		barcodeData = barcodeData.substring(0, 6);

		// Cast scanned barcode to an int
		int barcode = (int) Integer.parseInt(barcodeData);

		// Try to match the barcode with a product from the database
		Product product = database.getProduct(barcode);

		// Check if the database contained a matching product
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
			productBundle.putString("productID", barcodeData);

			// Set AddNewActivity as the intent
			productIntent = new Intent(this, AddNewActivity.class);
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
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};
}
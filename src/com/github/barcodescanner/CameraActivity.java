package com.github.barcodescanner;

import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.app.Activity;

public class CameraActivity extends Activity {

	private Camera mCamera;
	private CameraPreview mPreview;
	//DrawView object to draw lines on the camera preview
	DrawView drawLines;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// Create an instance of Camera
		mCamera = getCameraInstance();
		
		//Create an instance of DrawView
		drawLines = new DrawView(this);

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		preview.addView(drawLines);
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}
}

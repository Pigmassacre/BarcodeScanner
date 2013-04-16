package com.github.barcodescanner;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.app.Activity;

public class CameraActivity extends Activity {

	private BCanalyzer bcAnalyzer;
	private Camera mCamera;
	private CameraPreview mPreview;
	// DrawView object to draw lines on the camera preview
	DrawView drawLines;
	Button scanButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// Create an instance of Camera
		mCamera = getCameraInstance();

		// Create an instance of DrawView
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

	private PictureCallback picture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			bcAnalyzer = new BCanalyzer(data);
			System.out.println(bcAnalyzer.getWidth());
		}

	};

	// instead of adding a listener we can add "android:onClick="methodName" to the activity_camera.xml file
	// then we can just add the mCamera.takePicture(bla..) to that method. Saves us some code and I think that is
	// the standard way of doing it.
	public void addListener() {
		scanButton = (Button) findViewById(R.id.button_capture);
		scanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mCamera.takePicture(null, null, picture);
			}
		});
	}
}

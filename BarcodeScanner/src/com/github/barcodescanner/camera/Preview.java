package com.github.barcodescanner.camera;

import java.io.IOException;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * A simple wrapper around a Camera and a SurfaceView that renders a centered
 * preview of the Camera to the surface.
 */
public class Preview extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "Preview";
	
	private SurfaceHolder mHolder;
	private Camera mCamera;
	private AutoFocusCallback mAutoFocusCallback;

	@SuppressWarnings("deprecation")
	public Preview(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceCreated(mHolder);
	}

	/** Automatically called whenever the preview surface is created */
	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the
		// preview.
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
			mCamera.autoFocus(mAutoFocusCallback);
		} catch (IOException e) {
			Log.d(TAG, "Error setting camera preview: " + e.getMessage());
		}
	}

	/** Automatically called whenever the preview surface is destroyed.*/
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	/** Automatically called whenever the preview surface changes */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (mHolder.getSurface() == null) {
			return;
		}

		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			Log.e(TAG, "Exception when stopping camera: " + e);
		}

		mCamera.setDisplayOrientation(90);

		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			mCamera.autoFocus(this.mAutoFocusCallback);
		} catch (Exception e) {
			Log.e(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	public void setAutoFocusCallback(AutoFocusCallback autoFocusCallback) {
		mAutoFocusCallback = autoFocusCallback;
	}

}

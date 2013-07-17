package com.github.barcodescanner.camera;

import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
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
@SuppressLint("ViewConstructor")
public class Preview extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "Preview";

	private SurfaceHolder mHolder;
	private Camera mCamera;
	private AutoFocusCallback mAutoFocusCallback;

	public Preview(Context context, Camera camera) {
		super(context);
		mCamera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
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

	/** Automatically called whenever the preview surface is destroyed. */
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	/** Automatically called whenever the preview surface changes */
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (mHolder.getSurface() == null) {
			return;
		}

		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			Log.e(TAG, "Exception when stopping camera: " + e);
		}

		Camera.Parameters parameters = mCamera.getParameters();
		Camera.Size bestSize = getBestPreviewSize(width, height, parameters);
		
		mCamera.setDisplayOrientation(90);

		try {
			if (bestSize != null) {
				parameters.setPreviewSize(bestSize.width, bestSize.height);
				mCamera.setParameters(parameters);
			}
			mCamera.setPreviewDisplay(mHolder);
			mCamera.startPreview();
			mCamera.autoFocus(this.mAutoFocusCallback);
		} catch (Exception e) {
			Log.e(TAG, "Error starting camera preview: " + e.getMessage());
		}
	}

	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters) {
		Camera.Size bestSize = null;
		List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

		bestSize = sizeList.get(0);

		for (int i = 1; i < sizeList.size(); i++) {
			if ((sizeList.get(i).width * sizeList.get(i).height) > (bestSize.width * bestSize.height)) {
				bestSize = sizeList.get(i);
			}
		}

		return bestSize;
	}

	public void setAutoFocusCallback(AutoFocusCallback autoFocusCallback) {
		mAutoFocusCallback = autoFocusCallback;
	}

}

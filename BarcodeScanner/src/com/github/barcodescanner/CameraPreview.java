package com.github.barcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
@SuppressLint("ViewConstructor")
public class CameraPreview extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = "CameraPreview";

	private Camera mCamera;
	private SurfaceHolder mHolder;
	private PreviewCallback mPreviewCallback;
	private AutoFocusCallback mAutoFocusCallback;

	public CameraPreview(Context context, Camera camera,
			PreviewCallback previewCallback, AutoFocusCallback autoFocusCallback) {
		super(context);

		mCamera = camera;
		mPreviewCallback = previewCallback;
		mAutoFocusCallback = autoFocusCallback;

		mCamera.setDisplayOrientation(90);

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
		
		// deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		surfaceCreated(mHolder);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		startPreview();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		if (mHolder.getSurface() == null) {
			return;
		}

		stopPreview();

		// TODO Do stuff with surface here

		startPreview();
	}

	private void startPreview() {
		try {
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(mPreviewCallback);
			mCamera.startPreview();
			mCamera.autoFocus(mAutoFocusCallback);
		} catch (Exception e) {
			Log.e(TAG, "Exception when starting camera preview", e);
		}
	}

	private void stopPreview() {
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
			Log.e(TAG, "Exception when stopping camera preview", e);
		}
	}

}

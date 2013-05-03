package com.github.barcodescanner.old;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
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
	private Size mPreviewSize;
	private List<Size> mSupportedPreviewSizes;

	public CameraPreview(Context context, Camera camera,
			PreviewCallback previewCallback, AutoFocusCallback autoFocusCallback) {
		super(context);

		mCamera = camera;
		mPreviewCallback = previewCallback;
		mAutoFocusCallback = autoFocusCallback;

		mCamera.setDisplayOrientation(90);
		mSupportedPreviewSizes = mCamera.getParameters()
				.getSupportedPreviewSizes();

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

		if (mSupportedPreviewSizes != null) {
			mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, w, h);
		}

		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
		mCamera.setParameters(parameters);

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

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) h / w;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetWidth = w;

		// Try to find an size match aspect ratio and size
		for (Size size : sizes) {
			double ratio = (double) size.height / size.width;
			if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
				continue;
			if (Math.abs(size.width - targetWidth) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.width - targetWidth);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if (optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for (Size size : sizes) {
				if (Math.abs(size.width - targetWidth) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.width - targetWidth);
				}
			}
		}
		return optimalSize;
	}

}

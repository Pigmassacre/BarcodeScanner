package com.github.barcodescanner;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.app.Activity;
import android.content.Intent;

public class CameraActivity extends Activity {

	private BCanalyzer bcAnalyzer;
	private Camera mCamera;
	private CameraPreview mPreview;
	// DrawView object to draw lines on the camera preview
	DrawView drawLines;
	public static final int MEDIA_TYPE_IMAGE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println(mCamera);
		super.onCreate(savedInstanceState);
		System.out.println("!!!!!!!!!!!!!");
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from the camera view
		System.out.println("??????????????");
		setContentView(R.layout.activity_camera);
		System.out.println("hsdiogfhdsoghdsjogdajogajog");
		// Create an instance of Camera
		mCamera = getCameraInstance();
		System.out.println("1234567890");


		// Create an instance of DrawView
		drawLines = new DrawView(this);
		System.out.println("1234567890");
		System.out.println(mCamera);
		
		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		System.out.println("1234567890");
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);
		preview.addView(drawLines);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		releaseCamera();
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
			System.out.println(c);
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	private PictureCallback picture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			System.out.println("BILD SPARAD!!!");
			
			/*bcAnalyzer = new BCanalyzer(data);
			System.out.println(bcAnalyzer.getWidth());*/
			
		      File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
		        if (pictureFile == null){
		            Log.d("TEST", "Error creating media file, check storage permissions: ");
		            return;
		        }

		        try {
		            FileOutputStream fos = new FileOutputStream(pictureFile);
		            fos.write(data);
		            fos.close();
		        } catch (FileNotFoundException e) {
		            Log.d("TEST", "File not found: " + e.getMessage());
		        } catch (IOException e) {
 		        }
		        
		        Log.d("TEST", "Reached the end of the filemethod.");
		    }
	};
	
	/** Create a File for saving an image or video */
	private  File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");


	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	

	// instead of adding a listener we can add "android:onClick="methodName" to the activity_camera.xml file
	// then we can just add the mCamera.takePicture(bla..) to that method. Saves us some code and I think that is
	// the standard way of doing it.
	public void scanPicture(View view) {
		System.out.println("Bild Tagen");
		mCamera.takePicture(null, null, picture);	
		Intent intent = new Intent(this, ProductActivity.class);
		startActivity(intent);	
	}
	
	/**
	 * Method to release the camera 
	 * */
	private void releaseCamera(){
		if(mCamera != null){
			//Release the camera 
			mCamera.release();
			mCamera = null;
		}
	}
}

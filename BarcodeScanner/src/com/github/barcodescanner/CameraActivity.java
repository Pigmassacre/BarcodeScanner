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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from the camera view
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
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}

	private PictureCallback picture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			//bcAnalyzer = new BCanalyzer(data);
			//System.out.println(bcAnalyzer.getWidth());
			
			System.out.println("BILD SPARAD!!!");
		       File pictureFile = getOutputMediaFile();
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
		            Log.d("TEST", "Error accessing file: " + e.getMessage());
		        }
		        
		        Log.d("TEST", "Reached the end of the filemethod.");
		    }
	};
	
	/** Create a File for saving the image */
    private static File getOutputMediaFile(){
 
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");
 
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
 
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
 
        return mediaFile;
    }

	// instead of adding a listener we can add "android:onClick="methodName" to the activity_camera.xml file
	// then we can just add the mCamera.takePicture(bla..) to that method. Saves us some code and I think that is
	// the standard way of doing it.
	public void scanPicture(View view) {
		System.out.println("Bild Tagen");
		Intent intent = new Intent(this, ProductActivity.class);
		startActivity(intent);
		mCamera.takePicture(null, null, picture);		
	}
	
	private void releaseCamera(){
		if(mCamera != null){
			//Release the camera 
			mCamera.release();
			mCamera = null;
		}
	}
}

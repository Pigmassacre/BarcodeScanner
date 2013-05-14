package com.example.bcanalyzer;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

public class BCAnalyzer extends Activity {
	public Analyze analyze;
	private File image = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		// Create text view
		ImageView imgView = new ImageView(this);
		File path = Environment.getExternalStorageDirectory();
		
		image = new File(path.toString() + "/" + message);
		
		//Bitmap bitmap = BitmapFactory.decodeFile(path.toString() + "/" + "test5" + ".jpg");
		//ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		//byte[] byte_image = baos.toByteArray();
		
		if (image.exists()){
			analyze = new Analyze(path.toString() + "/" + "test7" + ".jpg");
			//analyze = new Analyze(byte_image);
			imgView.setImageBitmap(analyze.getBitmap());
		}else
			Log.d("Output","File does not exist");
		
		setContentView(imgView);
		
		// Show the Up button in the action bar.
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bcanalyzer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

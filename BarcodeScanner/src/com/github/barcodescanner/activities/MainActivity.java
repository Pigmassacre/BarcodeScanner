package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.camera.CameraActivity;
import com.github.barcodescanner.database.DatabaseActivity;
import com.github.barcodescanner.product.AddManuallyActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
public class MainActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.main_menu_scan:
			intent = new Intent(this, CameraActivity.class);
			startActivity(intent);
			return true;
		case R.id.main_menu_help:
			intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void enterCamera(View view) {
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}

	public void enterHelp(View view) {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	public void enterDatabase(View view) {
		Intent intent = new Intent(this, DatabaseActivity.class);
		startActivity(intent);
	}

}
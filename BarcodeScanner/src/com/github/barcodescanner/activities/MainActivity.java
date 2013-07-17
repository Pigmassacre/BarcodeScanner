package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.camera.CameraActivity;
import com.github.barcodescanner.database.DatabaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
public class MainActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		TextView titleView = (TextView) findViewById(R.id.main_news_title);
		titleView.setText(getString(R.string.main_news_title, getString(R.string.version_number)));
		
		// Start the introactivity which is displayed on top of this activity.
		Intent intent = new Intent(this, MainIntroActivity.class);
		startActivity(intent);
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
		case R.id.main_menu_access_database:
			intent = new Intent(this, DatabaseActivity.class);
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

}
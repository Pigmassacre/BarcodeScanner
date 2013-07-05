package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.camera.CameraActivity;
import com.github.barcodescanner.database.DatabaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	private boolean adminMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		adminMode = getIntent().getExtras().getBoolean("adminMode");

		if (!adminMode) {
			((TextView) findViewById(R.id.user_message)).setText(R.string.welcome_scan_mode);
		} else {
			((TextView) findViewById(R.id.user_message)).setText(R.string.welcome_admin_mode);
		}
	}

	public void enterCamera(View view) {
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra("adminMode", adminMode);
		startActivity(intent);
	}

	public void enterHelp(View view) {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

	public void enterDatabase(View view) {
		Intent intent = new Intent(this, DatabaseActivity.class);
		intent.putExtra("adminMode", adminMode);
		startActivity(intent);
	}

}
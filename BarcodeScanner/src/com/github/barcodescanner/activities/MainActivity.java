package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.camera.CameraActivity;
import com.github.barcodescanner.database.DatabaseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
public class MainActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
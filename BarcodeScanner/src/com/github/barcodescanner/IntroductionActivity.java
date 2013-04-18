package com.github.barcodescanner;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class IntroductionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from the camera view
		setContentView(R.layout.activity_introduction);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.introduction, menu);
		return true;
	}

	public void enterAdmin(View view) {
		// For now this will start the main activity, but it's supposed to launch into an admin specific activity
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	public void enterCustomer(View view) {
		// For now this will start the main activity, but it's supposed to launch into a customer specific activity
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
}
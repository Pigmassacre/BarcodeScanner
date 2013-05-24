package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.R.layout;
import com.github.barcodescanner.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

}

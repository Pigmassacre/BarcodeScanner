package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	public static final String SETTINGS = "settingsFile";
	private boolean showMainIntroOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView titleView = (TextView) findViewById(R.id.main_news_title);
		titleView.setText(getString(R.string.main_news_title, getString(R.string.version_number)));
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		SharedPreferences settings = getSharedPreferences(SETTINGS, 0);
		showMainIntroOverlay = settings.getBoolean("showMainIntroOverlay", true);

		// Start the MainIntroActivity which is displayed on top of this activity.
		if (showMainIntroOverlay) {
			// Save settings to not show MainIntroActivity overlay next time MainActivity is started.
			SharedPreferences.Editor editor = settings.edit();
			editor.putBoolean("showMainIntroOverlay", false);
			editor.commit();
			Intent intent = new Intent(this, MainIntroActivity.class);
			startActivity(intent);
		}
	}

}
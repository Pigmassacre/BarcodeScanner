		package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MainIntroActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_intro);

		RelativeLayout intro = (RelativeLayout) findViewById(R.id.main_intro_layout);
		intro.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				finish();
				return false;
			}
		});
	}

}

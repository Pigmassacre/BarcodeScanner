package com.github.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ProductActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from the camera view
		setContentView(R.layout.activity_product);
	}
	
	public void addToCart(View view) {
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}
}

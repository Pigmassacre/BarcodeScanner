package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.R.id;
import com.github.barcodescanner.R.layout;
import com.github.barcodescanner.camera.CameraActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "ProductActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from
														// the camera view
		setContentView(R.layout.activity_product);

		// Product name to display
		String productName = getIntent().getExtras().getString("productName");
		
		String productInfo = getIntent().getExtras().getString("productInfo");
		// Price to be displayed
		String productPrice = Integer.toString(getIntent().getExtras().getInt(
				"productPrice"));
		// TextView instance to display product name
		TextView name = (TextView) findViewById(R.id.productName);
		// TextView instance to display product info
		TextView info = (TextView) findViewById(R.id.productInfo);
		// TextView instance to display product price		
		TextView price = (TextView) findViewById(R.id.productPrice);
		// Set name to be displayed in TextView
		name.setText(productName);
		// Set info to be displayed in TextView
		info.setText(productInfo);
		// Set price to be displayed in TextView
		price.setText(productPrice);
	}
}

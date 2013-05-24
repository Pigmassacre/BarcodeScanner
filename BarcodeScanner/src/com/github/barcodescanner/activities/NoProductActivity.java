package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class NoProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "ProductActivity";
	private String productID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_no_product);

		// Get the product id to display
		productID = getIntent().getExtras().getString("productID");
		
		// TextView instance to display product ID for new product
    	TextView view = (TextView) findViewById(R.id.no_product_id);
		// Set price to be displayed in TextView
		view.setText(productID);
	}
}

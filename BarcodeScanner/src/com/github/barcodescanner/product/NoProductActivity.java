package com.github.barcodescanner.product;

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

		productID = getIntent().getExtras().getString("productID");

		TextView view = (TextView) findViewById(R.id.no_product_id);

		view.setText(productID);
	}
}

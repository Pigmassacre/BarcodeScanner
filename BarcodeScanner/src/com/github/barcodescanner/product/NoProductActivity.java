package com.github.barcodescanner.product;

import com.github.barcodescanner.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class NoProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "ProductActivity";

	private String productId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_no_product);

		productId = getIntent().getExtras().getString("productId");

		TextView view = (TextView) findViewById(R.id.no_product_id);

		view.setText(productId);
	}
}

package com.github.barcodescanner.product;

import com.github.barcodescanner.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class ProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "ProductActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_product);

		String productName = getIntent().getExtras().getString("productName");
		String productPrice = Integer.toString(getIntent().getExtras().getInt("productPrice"));

		TextView name = (TextView) findViewById(R.id.productName);
		TextView price = (TextView) findViewById(R.id.productPrice);

		name.setText(productName);
		price.setText(productPrice);
	}
}

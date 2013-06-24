package com.github.barcodescanner.product;

import com.github.barcodescanner.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "ProductActivity";

	private Bundle productBundle;

	private String productName;
	private String productPrice;

	private boolean adminMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_product);

		adminMode = getIntent().getExtras().getBoolean("adminMode");

		productBundle = getIntent().getExtras();

		productName = productBundle.getString("productName");
		productPrice = Integer.toString(productBundle.getInt("productPrice"));

		TextView name = (TextView) findViewById(R.id.product_name);
		TextView price = (TextView) findViewById(R.id.product_price);

		name.setText(productName);
		price.setText(productPrice);
		
		// only the owner can see and use the edit button
		if (!adminMode) {
			findViewById(R.id.product_edit_button).setVisibility(View.INVISIBLE);
		}
	}

	public void editProduct(View v) {
		if (adminMode) {
			Intent editProductIntent = new Intent(this, EditProductActivity.class);
			editProductIntent.putExtras(productBundle);
			startActivity(editProductIntent);
		}
	}
}

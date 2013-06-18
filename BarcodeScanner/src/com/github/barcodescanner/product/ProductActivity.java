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
	
	Bundle productBundle;
	
	String productName;
	String productPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_product);

		productBundle = getIntent().getExtras();
		
		productName = productBundle.getString("productName");
		productPrice = Integer.toString(productBundle.getInt("productPrice"));

		TextView name = (TextView) findViewById(R.id.product_name);
		TextView price = (TextView) findViewById(R.id.product_price);

		name.setText(productName);
		price.setText(productPrice);
	}
	
	public void editProduct(View v) {
		Intent editProductIntent = new Intent(this, EditProductActivity.class);
		editProductIntent.putExtras(productBundle);
		startActivity(editProductIntent);
	}
}

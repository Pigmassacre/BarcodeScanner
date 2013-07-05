package com.github.barcodescanner.product;

import com.github.barcodescanner.R;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "ProductActivity";

	private DatabaseHelper database;
	
	private Bundle productBundle;

	private String productName;
	private String productPrice;
	private String productId;

	private boolean adminMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);

		adminMode = getIntent().getExtras().getBoolean("adminMode");

		productBundle = getIntent().getExtras();

		productName = productBundle.getString("productName");
		productPrice = Integer.toString(productBundle.getInt("productPrice"));
		productId = Integer.toString(productBundle.getInt("productId"));

		TextView name = (TextView) findViewById(R.id.product_name);
		TextView price = (TextView) findViewById(R.id.product_price);
		TextView id = (TextView) findViewById(R.id.product_id);

		name.setText(productName);
		price.setText(productPrice);
		id.setText(productId);
		
		DatabaseHelperFactory.init(this);
		database = DatabaseHelperFactory.getInstance();
		
		// only the owner can see and use the edit and delete buttons
		if (!adminMode) {
			findViewById(R.id.product_edit_button).setVisibility(View.INVISIBLE);
			findViewById(R.id.product_delete_button).setVisibility(View.INVISIBLE);
		}
	}

	public void editProduct(View v) {
		if (adminMode) {
			Intent editProductIntent = new Intent(this, EditProductActivity.class);
			editProductIntent.putExtras(productBundle);
			startActivity(editProductIntent);
		}
	}
	
	public void deleteProduct(View v) {
		if (adminMode) {
			database.removeProduct(productId);
			
			Context context = getApplicationContext();
			CharSequence text = getString(R.string.database_toast_deleted, productName);
			
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			
			finish();
		}
	}
}

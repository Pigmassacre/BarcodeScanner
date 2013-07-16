package com.github.barcodescanner.product;

import com.github.barcodescanner.R;
import com.github.barcodescanner.activities.MainActivity;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		adminMode = getIntent().getExtras().getBoolean("adminMode");

		productBundle = getIntent().getExtras();

		productName = productBundle.getString("productName");
		productPrice = Integer.toString(productBundle.getInt("productPrice"));
		productId = productBundle.getString("productId");

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
			findViewById(R.id.product_menu_edit).setVisibility(View.INVISIBLE);
			findViewById(R.id.product_menu_delete).setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_product, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.product_menu_edit:
			editProduct();
			return true;
		case R.id.product_menu_delete:
			deleteProduct();
			return true;
		case android.R.id.home:
			intent = new Intent(this, MainActivity.class);
			intent.putExtra("adminMode", adminMode);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void editProduct() {
		if (adminMode) {
			Intent editProductIntent = new Intent(this, EditProductActivity.class);
			editProductIntent.putExtras(productBundle);
			startActivity(editProductIntent);
		}
	}
	
	public void deleteProduct() {
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

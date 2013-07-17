package com.github.barcodescanner.product;

import java.util.Scanner;

import com.github.barcodescanner.R;
import com.github.barcodescanner.activities.HelpActivity;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("Recycle")
public class EditProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "EditProductActivity";

	private DatabaseHelper database;

	private Bundle productBundle;

	private EditText editId;
	private EditText editName;
	private EditText editPrice;

	private String oldId;
	private String productId;
	private String productName;
	private Integer productPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_product);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		setupDatabase();

		productBundle = getIntent().getExtras();

		// Get all the info needed from the bundle we received
		productName = productBundle.getString("productName");
		productPrice = productBundle.getInt("productPrice");
		productId = productBundle.getString("productId");
		oldId = productId;

		// Use the bundle data to fill in the EditText fields in the view.
		editId = (EditText) findViewById(R.id.edit_product_id);
		editId.setText(productId);
		editName = (EditText) findViewById(R.id.edit_product_name);
		editName.setText(productName);
		editPrice = (EditText) findViewById(R.id.edit_product_price);
		editPrice.setText(Integer.toString(productPrice));

		// This "hack" is to make sure that the keyboard shows up when the
		// TextView gains focus.
		(new Handler()).postDelayed(new Runnable() {

			public void run() {
				editName.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						MotionEvent.ACTION_DOWN, 0, 0, 0));
				editName.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						MotionEvent.ACTION_UP, 0, 0, 0));
			}
		}, 50);
	}

	/**
	 * Gives the class access to the database.
	 */
	private void setupDatabase() {
		DatabaseHelperFactory.init(this);
		database = DatabaseHelperFactory.getInstance();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_product_edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.edit_product_menu_accept:
			editProduct();
			return true;
		case R.id.edit_product_menu_help:
			intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			intent = new Intent(this, ProductActivity.class);
			intent.putExtras(productBundle);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void editProduct() {
		String productId = editId.getText().toString();
		String productName = editName.getText().toString();
		Integer productPrice;

		Scanner scanner = new Scanner(editPrice.getText().toString());
		if (scanner.hasNextInt()) {
			// If the price is an integer, we accept it.
			productPrice = scanner.nextInt();

			Product updatedProduct = new Product(productName, productPrice, productId);
			database.updateProduct(oldId, updatedProduct);

			Bundle productBundle = new Bundle();

			productBundle.putString("productName", productName);
			productBundle.putInt("productPrice", productPrice);
			productBundle.putString("productId", productId);
			
			Intent intent = new Intent();
			intent.putExtras(productBundle);
			setResult(5, intent);
			finish();
		} else {
			// otherwise, we show a toast.
			Context context = getApplicationContext();
			CharSequence text = getString(R.string.edit_product_toast_failed);

			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

}

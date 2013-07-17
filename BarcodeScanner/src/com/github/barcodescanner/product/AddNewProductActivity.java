package com.github.barcodescanner.product;

import java.util.Scanner;

import com.github.barcodescanner.R;
import com.github.barcodescanner.activities.HelpActivity;
import com.github.barcodescanner.camera.CameraActivity;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "AddNewActivity";

	private DatabaseHelper database;
	private EditText editPrice;
	private EditText editName;
	private String productId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		TextView view = (TextView) findViewById(R.id.new_product_id);
		
		setupDatabase();
		
		if (getIntent().hasExtra("productId")) {
			productId = getIntent().getExtras().getString("productId");
			view.setText(productId);
		}

		editName = (EditText) findViewById(R.id.new_product_name);
		editPrice = (EditText) findViewById(R.id.new_product_price);
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
		inflater.inflate(R.menu.menu_product_new, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.new_product_menu_accept:
			addProduct();
			return true;
		case R.id.new_product_menu_help:
			intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			intent = new Intent(this, CameraActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public void addProduct() {
		String productName = editName.getText().toString();

		int productPrice;

		Scanner scanner = new Scanner(editPrice.getText().toString());
		if (scanner.hasNextInt()) {
			// If the price is an integer, we accept it.
			productPrice = scanner.nextInt();

			Product newProduct = new Product(productName, productPrice, productId);
			database.addProduct(newProduct);

			Context context = getApplicationContext();
			CharSequence text = getString(R.string.new_product_toast_done, productName);
			
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			
			finish();
		} else {
			// otherwise, we show a toast
			Context context = getApplicationContext();
			CharSequence text = getString(R.string.new_product_toast_failed);
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

}

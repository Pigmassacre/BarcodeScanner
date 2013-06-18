package com.github.barcodescanner.product;

import java.util.Scanner;

import com.github.barcodescanner.R;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;
import com.github.barcodescanner.database.Product;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProductActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "EditProductActivity";

	private DatabaseHelper database;
	private EditText editPrice;
	private EditText editName;
	private String productName;
	private String productPrice;
	private String productID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_product);

		database = DatabaseHelperFactory.getInstance();

		// Get all the info needed from the bundle we received
		productName = getIntent().getExtras().getString("productName");
		productPrice = getIntent().getExtras().getString("productPrice");
		productID = getIntent().getExtras().getString("productID");

		TextView view = (TextView) findViewById(R.id.edit_product_id);
		view.setText(productID);

		// Use the bundle data to fill in the EditText fields in the view.
		editName = (EditText) findViewById(R.id.edit_product_name_field);
		editName.setText(productName, TextView.BufferType.EDITABLE);
		editPrice = (EditText) findViewById(R.id.edit_product_price_field);
		editPrice.setText(productPrice, TextView.BufferType.EDITABLE);
	}

	public void editProduct(View view) {
		String productID = this.productID;
		String productName = editName.getText().toString();

		int productPrice;

		Scanner scanner = new Scanner(editPrice.getText().toString());
		if (scanner.hasNextInt()) {
			// If the price is an integer, we accept it.
			productPrice = scanner.nextInt();

			Product updatedProduct = new Product(productName, productPrice, productID);
			database.updateProduct(updatedProduct);

			Context context = getApplicationContext();
			CharSequence text = getString(R.string.edit_product_toast_done, productName);
			
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			
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

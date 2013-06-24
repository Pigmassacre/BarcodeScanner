package com.github.barcodescanner.product;

import java.util.Scanner;

import com.github.barcodescanner.R;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_add_new);

		database = DatabaseHelperFactory.getInstance();

		productId = getIntent().getExtras().getString("productId");

		TextView view = (TextView) findViewById(R.id.new_product_id);
		view.setText(productId);

		editName = (EditText) findViewById(R.id.new_product_name_field);
		editPrice = (EditText) findViewById(R.id.new_product_price_field);
	}

	public void addProduct(View view) {
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

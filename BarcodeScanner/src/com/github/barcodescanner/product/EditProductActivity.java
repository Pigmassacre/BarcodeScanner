package com.github.barcodescanner.product;

import java.util.Scanner;

import com.github.barcodescanner.R;
import com.github.barcodescanner.database.DatabaseHelper;
import com.github.barcodescanner.database.DatabaseHelperFactory;
import com.github.barcodescanner.database.Product;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class EditProductActivity extends Activity{
	
	@SuppressWarnings("unused")
	private static final String TAG = "AddNewActivity";
	
	private DatabaseHelper database;
	private EditText editPrice;
	private EditText editName;
	private String productID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_product);
		
		database = DatabaseHelperFactory.getInstance();
		
		productID = getIntent().getExtras().getString("productID");
		
    	TextView view = (TextView) findViewById(R.id.new_product_id);
    	view.setText(productID);

    	editName = (EditText) findViewById(R.id.new_product_name_field);
    	editPrice = (EditText) findViewById(R.id.new_product_price);
	}
	
	public void editProduct(View view) {
		String productID = this.productID;
		String productName = editName.getText().toString();

		Scanner scanner = new Scanner(editPrice.getText().toString());
		int productPrice;
		if (scanner.hasNextInt()) {
			// If the price is an integer, we accept it.
			productPrice = scanner.nextInt();
		} else {
			// If the price isn't an integer, we set the price to the max integer value
			productPrice = Integer.MAX_VALUE;
		}

		Product updatedProduct = new Product(productName, productPrice, productID);

		database.updateProduct(updatedProduct);

		finish();
	}

}

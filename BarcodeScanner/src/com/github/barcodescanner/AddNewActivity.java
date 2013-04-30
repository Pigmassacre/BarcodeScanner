package com.github.barcodescanner;

import com.github.barcodescanner.core.DatabaseHelper;
import com.github.barcodescanner.core.DatabaseHelperFactory;
import com.github.barcodescanner.core.Product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class AddNewActivity extends Activity{
	
	private DatabaseHelper database;
	private EditText editPrice;
	private EditText editName;
	private String productID;
	//private int barCode;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from the camera view
		setContentView(R.layout.activity_addnew);
		
		//Get instance of DatabaseHelper class
		database = DatabaseHelperFactory.getInstance();
		//Get product ID to be displayed for new product
		productID = getIntent().getExtras().getString("productID");
		//TextView instance to display product ID for new product
    	TextView view = (TextView) findViewById(R.id.new_product_id);
    	//Display product ID for the new product
    	view.setText(this.productID);
    	//EditText instance to get new Name for product
    	editName = (EditText)findViewById(R.id.new_product_name_field);
    	//EditText instance to get new Price for product
    	editPrice = (EditText)findViewById(R.id.new_product_price);
	}
	
	public void addProduct(View view) {
		//
		//String newText = getIntent().getExtras().getString("productID");
		//Cast the productID to an int
		int barCode = Integer.parseInt(this.productID);
		//Get new Product name from EditText
		String productName = editName.getText().toString();
		//Get new Product price from EditText and cast to an int
		int productPrice = Integer.parseInt(editPrice.getText().toString());
		//Create instance of Product class for the new Product
		Product newProduct = new Product(productName,productPrice,barCode);
		//Add the new Product to the database
		database.addProduct(newProduct);
		//Change to CameraActivity to be able to scan more products
		Intent intent = new Intent(this,CameraActivity.class);
		startActivity(intent);
		System.out.println("addProduct(): Button pressed.");
	}

}

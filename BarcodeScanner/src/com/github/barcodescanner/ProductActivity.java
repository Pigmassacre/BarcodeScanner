package com.github.barcodescanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class ProductActivity extends Activity{
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from the camera view
		setContentView(R.layout.activity_product);
		
		//Product name to display
		String productName = getIntent().getExtras().getString("productName");
		//Price to be displayed
		String productPrice = Integer.toString(getIntent().getExtras().getInt("productPrice"));
		//TextView instance to display product name
    	TextView name = (TextView) findViewById(R.id.productName);
    	//TextView instance to display product price
    	TextView price = (TextView) findViewById(R.id.productPrice);
    	//Set name to be displayed in TextView
    	name.setText(productName);
    	//Set price to be displayed in TextView
    	price.setText(productPrice);
	}
	
	public void addToCart(View view) {
		// TODO
		//Starts CameraActivity to be able to scan more items
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}
}

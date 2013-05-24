package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.ProductActivity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class ProductActivityTest extends ActivityInstrumentationTestCase2<ProductActivity> {

	private TextView pName;
	private TextView pPrice;
	private ProductActivity pActivity;
	
	@SuppressWarnings("deprecation")
	public ProductActivityTest(){
		super("com.github.barcodescanner.activities", ProductActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
		Intent addIntent = new Intent();
		addIntent.putExtra("productName", "Milk");
		addIntent.putExtra("productPrice", 10);
		
		setActivityIntent(addIntent);
		
		pActivity = getActivity();
		pName = (TextView) pActivity.findViewById(com.github.barcodescanner.R.id.productName);
		pPrice = (TextView) pActivity.findViewById(com.github.barcodescanner.R.id.productPrice);
	}
	
	public void testProductName(){
		assertEquals("Milk", pName.getText());
	}
	
	public void testProductPrice(){
		assertEquals("10", pPrice.getText());
	}
}

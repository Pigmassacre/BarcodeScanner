package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.ProductActivity;

import android.test.ActivityInstrumentationTestCase2;

public class ProductActivityTest extends ActivityInstrumentationTestCase2<ProductActivity> {

	
	@SuppressWarnings("deprecation")
	public ProductActivityTest(){
		super("com.github.barcodescanner.activities", ProductActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
	}
}

package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.BarcodeViewActivity;

import android.test.ActivityInstrumentationTestCase2;

public class BarcodeViewActivityTest extends
		ActivityInstrumentationTestCase2<BarcodeViewActivity> {
	
	@SuppressWarnings("deprecation")
	public BarcodeViewActivityTest(){
		super("com.github.barcodescanner.activities", BarcodeViewActivity.class);
	}
	
	
	protected void setUp() throws Exception{
		super.setUp();
		
	}

}

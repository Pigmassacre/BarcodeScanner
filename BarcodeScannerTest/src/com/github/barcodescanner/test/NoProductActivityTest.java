package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.NoProductActivity;

import android.test.ActivityInstrumentationTestCase2;

public class NoProductActivityTest extends ActivityInstrumentationTestCase2<NoProductActivity> {
	
	@SuppressWarnings("deprecation")
	public NoProductActivityTest(){
		super("com.github.barcodescanner.activities", NoProductActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
	}

}

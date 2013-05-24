package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.DatabaseActivity;

import android.test.ActivityInstrumentationTestCase2;

public class DatabaseActivityTest extends ActivityInstrumentationTestCase2<DatabaseActivity> {
	
	@SuppressWarnings("deprecation")
	public DatabaseActivityTest(){
		super("com.github.barcodescanner.activities", DatabaseActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
	}

}

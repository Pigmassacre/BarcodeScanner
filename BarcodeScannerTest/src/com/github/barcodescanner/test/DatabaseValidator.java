package com.github.barcodescanner.test;

import com.github.barcodescanner.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class DatabaseValidator extends ActivityInstrumentationTestCase2<MainActivity> {
	
	//private TextView result;

	public DatabaseValidator(String name) {
		super("com.github.barcodescanner", MainActivity.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		MainActivity mainActivity = getActivity();
		//result = (TextView) mainActivity.findViewById(R.id.result);
	}
	
	public void testAddValues() {  
		   //sendKeys(NUMBER_24);  
		   // now on value2 entry  
		   //sendKeys(NUMBER_74);  
		   // now on Add button  
		   //sendKeys("ENTER");  
		   // get result  
		   //String mathResult = result.getText().toString();  
		   //assertTrue("Add result should be 98", mathResult.equals(ADD_RESULT));  
	}  

}

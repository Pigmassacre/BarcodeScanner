package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.IntroductionActivity;
import com.github.barcodescanner.activities.MainActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class IntroductionActivityTest extends
		ActivityInstrumentationTestCase2<IntroductionActivity> {
	
	private IntroductionActivity mainActivity;
	private Button adminButton;
	private Button customerButton;
	
	@SuppressWarnings("deprecation")
	public IntroductionActivityTest(){
		super("com.github.barcodescanner.activities", IntroductionActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
		mainActivity = getActivity();
		
		adminButton = (Button) mainActivity.findViewById(com.github.barcodescanner.R.id.admin_button);
		customerButton  = (Button) mainActivity.findViewById(com.github.barcodescanner.R.id.customer_button);
	}
	
	
	public void testAdminButton(){
		assertEquals("Shop Owner", adminButton.getText());
	}
	
	public void testCustomerButton(){
		assertEquals("Customer", customerButton.getText());
	}
	

}

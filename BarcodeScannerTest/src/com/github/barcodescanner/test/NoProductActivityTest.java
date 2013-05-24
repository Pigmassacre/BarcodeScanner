package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.NoProductActivity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

public class NoProductActivityTest extends ActivityInstrumentationTestCase2<NoProductActivity> {
	
	private TextView noProductTitle;
	private TextView noPMessage;
	private NoProductActivity noPActivity;
	
	@SuppressWarnings("deprecation")
	public NoProductActivityTest(){
		super("com.github.barcodescanner.activities", NoProductActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
		Intent addIntent = new Intent();
		addIntent.putExtra("productID", "123456");
		setActivityIntent(addIntent);
		
		noPActivity = getActivity();
		
		noProductTitle = (TextView) noPActivity.findViewById(com.github.barcodescanner.R.id.no_product_id_title);
		noPMessage     = (TextView) noPActivity.findViewById(com.github.barcodescanner.R.id.no_product_message_title);	
	}
	
	public void testNoProductTitle(){
		assertEquals("Product ID -", noProductTitle.getText());
	}
	
	public void testNoProductMessage(){
		assertEquals("No product found", noPMessage.getText());
	}

}

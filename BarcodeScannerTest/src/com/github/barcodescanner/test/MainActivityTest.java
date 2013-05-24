package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.MainActivity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
	
	private MainActivity mainActivity;
	private Button startButton;
	private Button helpButton;
	
	@SuppressWarnings("deprecation")
	public MainActivityTest(){
		super("com.github.barcodescanner", MainActivity.class);
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		
		Intent addEvent = new Intent();
	    addEvent.putExtra("isOwner", true);
	    setActivityIntent(addEvent);
		
		mainActivity = getActivity();
		
		startButton = (Button) mainActivity.findViewById(com.github.barcodescanner.R.id.start_button);
		helpButton  = (Button) mainActivity.findViewById(com.github.barcodescanner.R.id.help_button);
	}
	
	public void testStartButton(){
		assertEquals("Scan Barcode",startButton.getText());
	}
	
	public void testHelpButton(){
		assertEquals("Help", helpButton.getText());
	}

}

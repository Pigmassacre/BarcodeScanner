package com.github.barcodescanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class AddNewActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // hides the title from the camera view
		setContentView(R.layout.activity_addnew);
		String newText = getIntent().getExtras().getString("product");
    	TextView view = (TextView) findViewById(R.id.new_product_id);
    	view.setText(newText);
	}
	
	public void addProduct() {
		// TODO Stuff
	}

}

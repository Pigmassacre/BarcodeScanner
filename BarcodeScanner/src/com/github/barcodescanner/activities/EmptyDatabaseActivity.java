package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.camera.CameraActivity;
import com.github.barcodescanner.product.AddManuallyActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class EmptyDatabaseActivity extends Activity{
	
	private boolean adminMode;
	private Button addManually;
	private Button scanNew;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_database_empty);
		adminMode = getIntent().getExtras().getBoolean("adminMode");
		addManually = (Button)findViewById(R.id.manually_add);
		scanNew = (Button)findViewById(R.id.scan_new);
		if(adminMode){
			addManually.setVisibility(View.VISIBLE);
			scanNew.setVisibility(View.VISIBLE);
		}	
	}
	
	public void addProductManually(View view){
		if(adminMode){
			Intent intent = new Intent(this, AddManuallyActivity.class);
			intent.putExtra("adminMode", adminMode);
		    startActivity(intent);
		    finish();
		}
	}
	
	public void scanNewBarcode(View view){
		if(adminMode){
			Intent intent = new Intent(this, CameraActivity.class);
			intent.putExtra("adminMode", adminMode);
			startActivity(intent);
			finish();
		}
	}
	
	public void helpView(View view){
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}
	
	public void backToMain(View view){
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("adminMode", adminMode);
		startActivity(intent);
		finish();
	}
}

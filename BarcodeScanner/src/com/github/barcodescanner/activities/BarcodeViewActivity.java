package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;
import com.github.barcodescanner.R.layout;
import com.github.barcodescanner.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.widget.ImageView;

public class BarcodeViewActivity extends Activity {

	private static final String TAG = "BarcodeViewActivity";
	private Bitmap barcodeBitmap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_view);
		
		barcodeBitmap = (Bitmap) getIntent().getParcelableExtra("barcodeBitmap");
		ImageView img = (ImageView) findViewById(R.id.barcode_bitmap_imageview);
		
		img.setImageBitmap(barcodeBitmap);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barcode_view, menu);
		return true;
	}

}

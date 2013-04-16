package com.github.barcodescanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class BCanalyzer {
	private Bitmap bitmap;
	@SuppressWarnings("unused") //yah whatever
	private Canvas canvas;

	//constructor for BCanalyser with a path as paramter
	public BCanalyzer(String path) {
		this.bitmap = BitmapFactory.decodeFile(path);
		this.setCanvas(new Canvas(bitmap)); 
	}
	
	//constructor for BCanalyser with a byrearray as paramter
	public BCanalyzer(byte[] bitmapdata) {
		this.bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
		this.setCanvas(new Canvas(bitmap)); 
	}
	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	public Bitmap getBitmap(){
		return this.bitmap;
	}
}

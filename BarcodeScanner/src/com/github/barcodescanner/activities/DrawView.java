package com.github.barcodescanner.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

public class DrawView extends SurfaceView {

	@SuppressWarnings("unused")
	private static final String TAG = "DrawView";
	private Paint linePaint;
	private Integer[] line;

	public DrawView(Context context) {
		super(context);

		// initiates linePaint
		linePaint = new Paint();
		// Sets color of linePaint to black
		linePaint.setColor(Color.RED);
		// Sets width line
		linePaint.setStrokeWidth(5);
		// Sets the text style
		// Sets that this View has something to draw
		setWillNotDraw(false);
	}
	
	public void setLineArray(Integer[] line){
		this.line = line;
	}
	
	

	@Override
	protected void onDraw(Canvas canvas) {
		// Draws a straight horizontal line from x values 50 to 700 on y value
		// 300
		canvas.drawLine(line[0], line[1], line[2], line[3], linePaint);
		// Draws a straight vertical line from x value 700 and y values 320 to
		// 500
		//canvas.drawLine(700, 320, 700, 500, linePaint);
	}
}

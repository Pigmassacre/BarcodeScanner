package com.github.barcodescanner.core;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/*
 * 30 black lines
 * 29 white lines
 * 
 * either 1,2,3 or 4 in width
 * 
 */

public class Analyze {
	private Bitmap bitmap;
	private Canvas canvas = new Canvas();
	private Integer[] mostPlausibleBarcode = new Integer[4];

	/**
	 * raw imagedata as parameter
	 * 
	 * @param bitmapdata
	 */
	public Analyze() {}

	/**
	 * path to image as parameter
	 * 
	 * @param path
	 */
	public Analyze(String path) {
		Log.d("Output", path);
		Bitmap temp = BitmapFactory.decodeFile(path);
		bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), null, temp.hasAlpha());		
		canvas.setBitmap(bitmap);
		scanLines(30);
		if (mostPlausibleBarcode[0]!=null){
			drawRectToBitmap(mostPlausibleBarcode[0],mostPlausibleBarcode[1],mostPlausibleBarcode[2],mostPlausibleBarcode[3]);
			System.out.println(mostPlausibleBarcode[0] + " : " + mostPlausibleBarcode[2]);
		}else System.out.println("No barcode exist");
	}
	
	public void SetData(byte[] bitmapdata){
		Bitmap temp = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata .length);
		bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), null, temp.hasAlpha());		
		canvas.setBitmap(bitmap);
		scanLines(10);	
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
	
	/**
	 * This method is the first in the chain of methods looking for barcode.
	 * 
	 * @param iteration how many lines that should be scanned over the height
	 * @return if any scanline had over 30 "swaps" 
	 */
	private boolean scanLines(int iteration){
		boolean found = false;
		List<Integer> horizontalline = new ArrayList<Integer>();
		for (int i = 1; i < iteration; i++){ 
			Integer scanline = (getHeight() / iteration) * i;

			horizontalline = scanHorizontal(scanline, 0.5f);
			List<Integer> switchPoints = plausibleBarcode(horizontalline);

			if (switchPoints.size()>=30){ 
				found = true;
				Log.d("Value", "true");
				extendHeightSearch(scanline,switchPoints);			
			}else Log.d("Value", "false");

		}
		return found;
	}
	
	/**
	 * Draws a red rect around target position, used to debug result of the algorithm
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	private void drawRectToBitmap(int x1, int y1, int x2, int y2){
		Rect r = new Rect(x1,y1,x2,y2);
		Paint p = new Paint();
		p.setColor(Color.rgb(255, 0, 0));
		p.setStrokeWidth(5);
		p.setStyle(Paint.Style.STROKE);     
		canvas.drawRect(r, p);
		Log.d("Output", "Rect drawn");
	}
	
	/**
	 * replaces the value of each pixel with the brightness of this pixel, 
	 * 1s represent dark pixel, 0 represent bright pixel, -1 for undefined
	 * 
	 * @param heightLine the horizontal line to be scanned
	 * @param threshold the threshold for accepting bright and dark
	 * @return array of line data
	 *  
	 */
	private List<Integer> scanHorizontal(int heightLine, float threshold){
		List<Integer> lineData = new ArrayList<Integer>();
		
		int red,green,blue;
		float[] hsv = new float[3];
				
		for (int x = 0; x < getWidth(); x++){	
			red = (bitmap.getPixel(x, heightLine))&0xFF;
			green = (bitmap.getPixel(x, heightLine)>>8)&0xFF;
			blue = (bitmap.getPixel(x, heightLine)>>16)&0xFF;
			
			Color.RGBToHSV(red, green, blue, hsv);

			if (hsv[2]>1.0-threshold) lineData.add(0);
			else if (hsv[2]<0+threshold) lineData.add(1);
			else lineData.add(-1);
		}
		return lineData;
	}
	
	/**
	 * Looks for places where the value changes from light to dark and puts position into array 
	 * 
	 * @param horizontalline line to be scanned for barcode
	 * @return switches array of indexes where the pixels shift from light to dark
	 * 
	 */
	private List<Integer> plausibleBarcode(List<Integer> horizontalline){
		List<Integer> switches = new ArrayList<Integer>();
		
		for (int x = 1; x < horizontalline.size(); x++){
			int lastValue = horizontalline.get(x-1);
			
			if (horizontalline.get(x) == 1 && lastValue == 0){			
				switches.add(x);
			}
		}		
		return switches;
	}
	
	/**
	 * 
	 * 
	 * @param scanline
	 * @param switchPointsMain
	 */
	private void extendHeightSearch(Integer scanline, List<Integer> switchPointsMain){
		List<Integer> switchPoints = getSmallestSubset(switchPointsMain);

		int count = lookUpAndDown(scanline, switchPoints);
		
		if (count > 0){
			int mostLeft = switchPoints.get(0);
			int mostRight = switchPoints.get(switchPoints.size()-1);
			
			if (mostPlausibleBarcode[0] != null){
				if ((mostPlausibleBarcode[2] - mostPlausibleBarcode[0]) >= (mostRight - mostLeft) && switchPoints.size()==30){
					mostPlausibleBarcode[0] = mostLeft;
					mostPlausibleBarcode[1] = scanline;
					mostPlausibleBarcode[2] = mostRight;
					mostPlausibleBarcode[3] = scanline;
					drawRectToBitmap(mostLeft,scanline,mostRight,scanline);
				}
			}else {
				mostPlausibleBarcode[0] = mostLeft;
				mostPlausibleBarcode[1] = scanline;
				mostPlausibleBarcode[2] = mostRight;
				mostPlausibleBarcode[3] = scanline;
				drawRectToBitmap(mostLeft,scanline,mostRight,scanline);
			}
		}
	}
	
	private int lookUpAndDown(Integer scanline,List<Integer> switchPointsMain){
		int delta = getHeight() / 20;
		int count = 0;
		List <Integer> switchPoints;
		List <Integer> horizontalline;
		
		//looks delta distance above scanline for similar pattern
		horizontalline = scanHorizontal(scanline - delta, 0.5f);
		switchPoints = getSmallestSubset(plausibleBarcode(horizontalline));
		if (switchPoints.size() == switchPointsMain.size())
			count++;

		//looks delta distance below scanline for similar pattern
			horizontalline = scanHorizontal(scanline + delta, 0.5f);
			switchPoints = getSmallestSubset(plausibleBarcode(horizontalline));
			if (switchPoints.size() == switchPointsMain.size())
				count++;
			
		System.out.println("Both or sideways ? : " + count);
		return count;
	}
	
	private List<Integer> getSmallestSubset(List<Integer> switchPoints){
		System.out.println("The size before was : " + switchPoints.size());
		boolean cut;
		int greatestDistance;
		int location;
		int tempDistance;		
		List<Integer> tmpSwitchPoints;
		do {
			tmpSwitchPoints = switchPoints;
			cut = false;
			greatestDistance = 0;
			location = 0;
			tempDistance = 0;
			
			for (int i = 1; i < switchPoints.size(); i++){
				tempDistance = switchPoints.get(i) - switchPoints.get(i-1);
				if (tempDistance >= greatestDistance){
					greatestDistance = tempDistance;
					location = i;
				}
			}
			
			int subArrayRight = switchPoints.size() - location - 1;
			int subArrayLeft = location;
			
			if (subArrayRight >= subArrayLeft && subArrayRight >= 30){
				switchPoints = switchPoints.subList(location, switchPoints.size());
				cut = true;
			}
			else if (subArrayRight <= subArrayLeft && subArrayLeft>=30){
				switchPoints = switchPoints.subList(0, location);
				cut = true;
			}
			
		if (switchPoints.size() < 30)
			switchPoints = tmpSwitchPoints;
		
		}while (switchPoints.size() > 30 && cut);
		
		System.out.println("The size after is : " + switchPoints.size());

		return switchPoints;
	}
}
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

public class BCLocator {
	// the bitmap containing the raw data from camera
	private Bitmap bitmap;
	
	// the canvas in case the bitmap needs to be drawn on
	private Canvas canvas = new Canvas();
	
	// array of x1,y2,x2,y2 representing the resulting barcode if its found
	private Integer[] mostPlausibleBarcode = new Integer[4];
	
	// the number to divide the height on when confirming a line of a possible barcode
	int delta = 40;

	public BCLocator() {}

	/**
	 * Path to image as parameter
	 * This constructor is used for testing purposes
	 * 
	 * @param path
	 */
	public BCLocator(String path) {
		Bitmap temp = BitmapFactory.decodeFile(path);
		bitmap = Bitmap.createBitmap(temp, 0, 0, temp.getWidth(), temp.getHeight(), null, temp.hasAlpha());		
		canvas.setBitmap(bitmap);
		scanLines(30);
	}
	
	/**
	 * Sets the data in the class, this initiates a line of functions to locate the barcode
	 * 
	 * @param bitmapdata
	 */
	public void setData(byte[] bitmapdata){
		mostPlausibleBarcode = new Integer[4];
		Bitmap temp = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata.length);
		bitmap = temp.copy(temp.getConfig(), true);
		canvas.setBitmap(bitmap);
		scanLines(30);	
	}

	/**
	 * Used to check if mostPlausibleBarcode is set
	 * 
	 * @return true if it is set, false if its not (eg. if barcode is found)
	 */
	public boolean foundBarcode(){
		return mostPlausibleBarcode[0] != null;
	}

	/**
	 * Get width of bitmap
	 * 
	 * @return width
	 */
	public int getWidth() {
		return bitmap.getWidth();
	}

	/**
	 * Get height of bitmap
	 * 
	 * @return height
	 */
	public int getHeight() {
		return bitmap.getHeight();
	}

	/**
	 * Get the bitmap
	 * 
	 * @return bitmap
	 */
	public Bitmap getBitmap(){
		return this.bitmap;
	}

	/**
	 * Gets the segment-matrix of bitmap data converted to 0s and 1s within the mostPlausibleBarcode 
	 * 
	 * @return segment
	 */
	public List<List<Integer>> getSegment(){
		List<List<Integer>> segmentMap = new ArrayList<List<Integer>>();
		List<Integer> segment = null;
		int start = mostPlausibleBarcode[1] -getHeight()/delta;
		int stop = mostPlausibleBarcode[3] + getHeight()/delta;
		
		if (mostPlausibleBarcode[0]!=null){
			for (int line = start; line < stop; line++){
				List<Integer> horizontalline = scanHorizontal(line, 0.5f);	
				segment = horizontalline.subList(mostPlausibleBarcode[0], mostPlausibleBarcode[2]);
				segmentMap.add(segment);
			}
		}

		return segmentMap;
	}
	
	/**
	 * Used for debug purpose
	 * 
	 * @return a segmented bitmap according to mostPlausibleBarcode indexes
	 */
	public Bitmap getSegmentedBitmap(){
		Bitmap segmentedBitmap = Bitmap.createBitmap(bitmap, mostPlausibleBarcode[0], mostPlausibleBarcode[1] - getHeight()/delta, 
				mostPlausibleBarcode[2]-mostPlausibleBarcode[0], mostPlausibleBarcode[1]-mostPlausibleBarcode[3] + getHeight()/delta);
		return segmentedBitmap;

	}

	/**
	 * This method is the first in the chain of methods when locating a barcode.
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
			} else {
				Log.d("Value", "false");
			}

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
	public void drawRectToBitmap(int x1, int y1, int x2, int y2){
		Rect r = new Rect(x1,y1,x2,y2);
		Paint p = new Paint();
		p.setColor(Color.rgb(255, 0, 0));
		p.setStrokeWidth(5);
		p.setStyle(Paint.Style.STROKE);     
		canvas.drawRect(r, p);
		Log.d("Output", "Rect drawn");
	}

	/**
	 * Replaces the value of each pixel with the brightness of this pixel, 
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
	 * Called from scanLines() when it returns true on a line
	 * 
	 * @param scanline the line containing over or equal to 30 switches
	 * @param switchPointsMain the switchpoints between bright and dark
	 */
	private void extendHeightSearch(Integer scanline, List<Integer> switchPointsMain){
		List<Integer> switchPoints = getSmallestSubset(switchPointsMain);

		int count = getSimilarPatterns(scanline, switchPoints);

		if (count > 1){
			int mostLeft = switchPoints.get(0);
			int mostRight = switchPoints.get(switchPoints.size()-1);

			mostPlausibleBarcode[0] = mostLeft;
			mostPlausibleBarcode[1] = scanline;
			mostPlausibleBarcode[2] = mostRight;
			mostPlausibleBarcode[3] = scanline;
		}
	}

	/**
	 * This method tries to cut away parts of the line that has switchpoints not within the barcode,
	 * eg. if a line contains text and a barcode the text should be removed
	 * 
	 * @param switchPoints
	 * @return a new switchpoints only containing the barcode or the parameter if the algorithm failed
	 */
	private List<Integer> getSmallestSubset(List<Integer> switchPoints){
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

			if (subArrayRight > subArrayLeft && subArrayRight >= 30){
				switchPoints = switchPoints.subList(location, switchPoints.size());
				cut = true;
			}
			else if (subArrayRight < subArrayLeft && subArrayLeft>=30){
				switchPoints = switchPoints.subList(0, location);
				cut = true;
			}

		if (switchPoints.size() < 30){
			switchPoints = tmpSwitchPoints;
			cut = false;
		}

		}while (switchPoints.size() > 30 && cut);

		return switchPoints;
	}	
	
	/**
	 * Checks if the line on deltaheight above and under the current scanline has similar patterns
	 * 
	 * @param scanline
	 * @param switchPointsMain
	 * @return integer representing if above and below did or did not contain somilar patterns
	 */
	private int getSimilarPatterns(Integer scanline,List<Integer> switchPointsMain){
		int distDelta = getHeight() / delta;
		int count = 0;
		List <Integer> switchPoints;
		List <Integer> horizontalline;

		//looks delta distance above scanline for similar pattern
		horizontalline = scanHorizontal(scanline - distDelta, 0.5f);
		switchPoints = getSmallestSubset(plausibleBarcode(horizontalline));
		if (switchPoints.size() == switchPointsMain.size())
			count++;

		//looks delta distance below scanline for similar pattern
			horizontalline = scanHorizontal(scanline + distDelta, 0.5f);
			switchPoints = getSmallestSubset(plausibleBarcode(horizontalline));
			if (switchPoints.size() == switchPointsMain.size())
				count++;

		return count;
	}
}
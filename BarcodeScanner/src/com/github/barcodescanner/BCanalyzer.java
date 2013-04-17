package imgMatrix;

import java.util.ArrayList;
import java.util.List;

public class ScanNodes {
	private PixleNode nodes[][];
	private int w,h;

	public void scan(PixleNode nodes[][], int w ,int h) {
		this.nodes = nodes;
		this.w = w;
		this.h = h;
		for (PixleNode[] rows : nodes) {
			for (PixleNode n : rows) {
				if ((n.getX() + 1) % (w / 2) == 0 && 
					(n.getY() + 1) % (h / 2) == 0) {
					System.out.println(n.getX() + "," + n.getY());
					getLines(n);
				}
			}
		}
	}

	private void getLines(PixleNode n) {
		PixleNode lineX[] = new PixleNode[w];
		PixleNode lineY[] = new PixleNode[h];

		for (int x = 0; x < w; x++) {
			lineX[x] = nodes[x][n.getY()];
		}

		for (int y = 0; y < h; y++) {
			lineY[y] = nodes[n.getX()][y];
		}

		scanLine(lineX);
		scanLine(lineY);
	}

	private boolean scanLine(PixleNode[] line){		
		boolean foundBarcode = false;
		List<PixleNode> dark = new ArrayList<PixleNode>(); 
		List<PixleNode> light = new ArrayList<PixleNode>();
		
		for (PixleNode temp : line) {
			float bright = temp.getBrightness();
			
			if (bright>0.95) 
				System.out.println(temp.getX() + "," + temp.getY());
//					light.add(temp);
			
			if (bright<0.05)
				System.out.println(temp.getX() + "," + temp.getY());
//					dark.add(temp);
		}
		
		
		
		return foundBarcode;
	}
}

package com.github.barcodescanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

public class BCanalyzer {
	private Bitmap bitmap;
	@SuppressWarnings("unused") //yah whatever
	private Canvas canvas;

	//constructor for BCanalyser with a path as paramter
	public BCanalyzer(String path) {
		this.bitmap = BitmapFactory.decodeFile(path);
		this.setCanvas(new Canvas(bitmap)); 
	}
	
	//constructor for BCanalyser with a byte array as paramter
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
	
	/**
	 * 
	 * @param heightLine the horisontal line to be scanned
	 * @param threshold the threshold for accepting bright and dark
	 * @return array of line data, 1s represent dark pixel, 0 represent bright pixel, -1 for undefined
	 *  
	 */
	@SuppressWarnings("null")
	private int[] scanHorisontal(int heightLine, float threshold){
		int[] lineData = new int[getWidth()]; 
		int red,green,blue;

		//fills the lineData with desired color data.
		bitmap.getPixels(lineData, 0, 0, 0, heightLine, getWidth(), 1);

		for (int x = 0; x < getWidth(); x++){
			float[] hsv = null;
			
			red = (lineData[x])&0xFF;
			green = (lineData[x]>>8)&0xFF;
			blue = (lineData[x]>>16)&0xFF;
			
			Color.RGBToHSV(red, green, blue, hsv);
			
			if (hsv[2]>1-threshold) lineData[x] = 0;
			else if (hsv[2]<0+threshold) lineData[x] = 1;
			else lineData[x] = -1;
		}
		
		return lineData;
	}
	
	@SuppressWarnings("null")
	private int[] scanVertical(int widthLine, float threshold){
		int[] lineData = new int[getHeight()]; 
		int red,green,blue;

		//fills the lineData with desired color data.
		bitmap.getPixels(lineData, 0, 0, 0, widthLine, getHeight(), 1);

		for (int x = 0; x < getHeight(); x++){
			float[] hsv = null;
			
			red = (lineData[x])&0xFF;
			green = (lineData[x]>>8)&0xFF;
			blue = (lineData[x]>>16)&0xFF;
			
			Color.RGBToHSV(red, green, blue, hsv);
			
			//hsv[2]
			if (hsv[2]>1-threshold) lineData[x] = 0;
			else if (hsv[2]<0+threshold) lineData[x] = 1;
			else lineData[x] = -1;
		}
		
		return lineData;
	}
}

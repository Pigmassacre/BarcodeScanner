package com.github.barcodescanner.test;

import java.util.List;

import junit.framework.TestCase;
import android.graphics.Bitmap;

import com.github.barcodescanner.core.*;


public class BCLocatorTest extends TestCase {
	
	private BCLocator firstLocator;
	private BCLocator secondLocator;
	private String imagePath;
	private boolean foundBC;
	private int bcWidth;
	private int bcHeight;
	private Bitmap bcBitmap;
	private List<List<Integer>> bcSegment;
	
	
	
	
	
	public BCLocatorTest(){
		imagePath = "res/drawable-dhpi/barcode.jpg";
		firstLocator = new BCLocator(imagePath);
		secondLocator = new BCLocator(imagePath);
	}
	
	public void testFoundBarcode(){
		assertEquals(firstLocator.foundBarcode(), secondLocator.foundBarcode());
	}
	
	public void testBarcodeWidth(){
		assertEquals(firstLocator.getWidth(), secondLocator.getWidth());
	}

	public void testBarcodeHeigth(){
		assertEquals(firstLocator.getHeight(), secondLocator.getHeight());
	}
	
	public void testBarcodeBitmap(){
		assertEquals(firstLocator.getBitmap(),secondLocator.getBitmap()); // ?? Bitmap assert ??
	}
	
	public void testBarcodeSegment(){
		List<List<Integer>> firstList = firstLocator.getSegment();
		List<List<Integer>> secondList = secondLocator.getSegment();
		for(int i = 0; i < firstList.size() - 1; i++){
			List<Integer> firstInnerList = firstList.get(i);
			for(int j = 0; j < firstInnerList.size() - 1; j++){
				assertEquals(firstInnerList.get(j), secondList.get(i).get(j));
			}
		}
	}
}

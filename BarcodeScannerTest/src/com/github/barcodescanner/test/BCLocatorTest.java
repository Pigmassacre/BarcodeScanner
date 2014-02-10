package com.github.barcodescanner.test;

import java.util.List;

import junit.framework.TestCase;

import com.github.barcodescanner.barcode.*;


public class BCLocatorTest extends TestCase {
	
	private BCLocator firstLocator;
	private BCLocator secondLocator;
	private String imagePath;

	
	
	
	
	
	public BCLocatorTest(){
		imagePath = "/barcode.jpg";
		firstLocator = new BCLocator(imagePath, false);
		secondLocator = new BCLocator(imagePath, false);
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
	
	public void testMedianFilter(){
		System.out.println("STARTING THE MEDIAN TEST");
		imagePath = "/ean13_barcode_sample.jpg";
		BCLocator bcLocator = new BCLocator(imagePath, true);
		BCGenerator bcGenerator = new BCGenerator();
		List<Integer> tempList = bcGenerator .generate(bcLocator.getSegment());
		String tempBarcode = bcGenerator.normalize(tempList);
		System.out.println("THE BARCODE: " + tempBarcode);
	}
}

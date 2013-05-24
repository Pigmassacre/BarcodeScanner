package com.github.barcodescanner.test;

import java.util.List;

import com.github.barcodescanner.core.BCGenerator;
import com.github.barcodescanner.core.BCLocator;

import junit.framework.TestCase;

public class BCGeneratorTest extends TestCase {

	private BCLocator bcLocator;
	private BCGenerator firstGenerator;
	private BCGenerator secondGenerator;
	
	
	public void BCGeneratorTest(){
		String imagePath = "/barcode.jpg";
		bcLocator = new BCLocator(imagePath);
		firstGenerator = new BCGenerator();
		secondGenerator = new BCGenerator();
	}
	
	protected void setUp() throws Exception{
		super.setUp();
		String imagePath = "/barcode.jpg";
		bcLocator = new BCLocator(imagePath);
		firstGenerator = new BCGenerator();
		secondGenerator = new BCGenerator();
		
		
	}
	
	public void testGenerate(){
		List<Integer> firstList = firstGenerator.generate(bcLocator.getSegment());
		List<Integer> secondList = secondGenerator.generate(bcLocator.getSegment());
		for(int i = 0; i < firstList.size(); i++){
			assertEquals(firstList.get(i), secondList.get(i));
		}
	}
	
	public void testNormalize(){
		List<Integer> generatedList = firstGenerator.generate(bcLocator.getSegment());
		assertEquals(firstGenerator.normalize(generatedList), secondGenerator.normalize(generatedList));
	}
}

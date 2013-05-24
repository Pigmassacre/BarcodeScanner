package com.github.barcodescanner.test;

import com.github.barcodescanner.activities.AddNewActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

public class AddNewActivityTest extends ActivityInstrumentationTestCase2<AddNewActivity> {
	
	private AddNewActivity addNew;
	private TextView titleText;
	private TextView productTitle;
	private TextView nameTitle;
	private TextView descriptionTitle;
	private TextView priceTitle;
	private Button addButton;
	
	
	
	@SuppressWarnings("deprecation")
	public AddNewActivityTest(){
		super("com.github.barcodescanner.activities", AddNewActivity.class);
	}
	
	
	protected void setUp() throws Exception{
		super.setUp();
		
		addNew = getActivity();
		titleText = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_title);
		productTitle = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_id_title);
		nameTitle = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_name_title);
		descriptionTitle = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_description_title);
		priceTitle = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_description_price);
		addButton = (Button) addNew.findViewById(com.github.barcodescanner.R.id.new_product_add_button);
	}
	
	public void testTitleText(){
		assertEquals("Add new product", titleText.getText());
	}
	
	public void testProductTitle(){
		assertEquals("Product ID -", productTitle.getText());
	}
	
	public void testNameTitle(){
		assertEquals("Name:", nameTitle.getText());
	}
	
	public void testDesctriptionTitle(){
		assertEquals("Description:", descriptionTitle.getText());
	}
	
	public void testPriceTitle(){
		assertEquals("Price:", priceTitle.getText());
	}
	
	public void testAddButton(){
		assertEquals("Add product", addButton.getText());
	}

}

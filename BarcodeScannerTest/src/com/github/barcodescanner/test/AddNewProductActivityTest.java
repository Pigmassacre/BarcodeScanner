package com.github.barcodescanner.test;

import com.github.barcodescanner.product.AddNewProductActivity;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

public class AddNewProductActivityTest extends ActivityInstrumentationTestCase2<AddNewProductActivity> {
	
	private AddNewProductActivity addNew;
	private TextView titleText;
	private TextView productTitle;
	private TextView nameTitle;
	private TextView descriptionTitle;
	private TextView priceTitle;
	private Button addButton;
	
	
	
	@SuppressWarnings("deprecation")
	public AddNewProductActivityTest(){
		super("com.github.barcodescanner.activities", AddNewProductActivity.class);
	}
	
	
	protected void setUp() throws Exception{
		super.setUp();
		
		Intent addEvent = new Intent();
		addEvent.putExtra("productID", "123456789");
		setActivityIntent(addEvent);
		
		addNew = getActivity();
		titleText = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_name);
		productTitle = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_id);
		nameTitle = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_name);
		priceTitle = (TextView) addNew.findViewById(com.github.barcodescanner.R.id.new_product_price);
		addButton = (Button) addNew.findViewById(com.github.barcodescanner.R.id.new_product_menu_accept);
	}
	
	public void testTitleText(){
		assertEquals("Add new product:", titleText.getText());
	}
	
	public void testProductTitle(){
		assertEquals("Product ID -", productTitle.getText());
	}
	
	public void testNameTitle(){
		assertEquals("Name:", nameTitle.getText());
	}
	
	
	public void testPriceTitle(){
		assertEquals("Price:", priceTitle.getText());
	}
	
	public void testAddButton(){
		assertEquals("Add product", addButton.getText());
	}

}

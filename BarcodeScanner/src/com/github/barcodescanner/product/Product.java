package com.github.barcodescanner.product;

public class Product {

	/*
	 * This is the object that represents the objects that each barcode belongs
	 * to. The objects will therefore contain the values that are to be saved in
	 * the database.
	 */

	private String name;
	private int price;
	private String barcode;

	public Product() {

	}

	public Product(String name, int price, String barcode) {
		this.name = name;
		this.price = price;
		this.barcode = barcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}

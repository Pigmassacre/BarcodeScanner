package com.github.barcodescanner.core;
//TODO
public class Product {
	
	/*
	 * This is the object that represents the objects that each barcode belongs to. 
	 * The objects will therefor contain the values that are to be saved in the database.
	 * */
	
	private int ID;
	private String name;
	private int price;
	private int barcode;
	
	
	public Product(){
		
	}
	
	public Product(int ID, String name, int price, int barcode){
		this.ID = ID;
		this.name = name;
		this.price = price;
		this.barcode = barcode;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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

	public int getBarcode() {
		return barcode;
	}

	public void setBarcode(int barcode) {
		this.barcode = barcode;
	}

}

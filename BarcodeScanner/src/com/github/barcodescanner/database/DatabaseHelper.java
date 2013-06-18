package com.github.barcodescanner.database;

import java.util.ArrayList;
import java.util.List;

import com.github.barcodescanner.product.Product;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// TODO Possibly change price from INTEGER to FLOAT
	
	// The name of the database and its tables, views and columns
	static final String DB_NAME = "BarcodeDB";
	static final String TABLE_PRODUCTS = "Products";
	static final String KEY_BCODE = "ProductBarcode";
	static final String KEY_NAME = "ProductName";
	static final String KEY_PRICE = "Price";
	static final String VIEW_PRODUCTS = "ProductsView";

	/*
	 * change the number in the superconstructor to invoke the onUpgrade method;
	 * I/E you change the version number
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" + KEY_BCODE
				+ " TEXT PRIMARY KEY, " + KEY_NAME + " TEXT, "
				+ KEY_PRICE + " INTEGER)");

		db.execSQL("CREATE VIEW " + VIEW_PRODUCTS + " AS SELECT "
				+ TABLE_PRODUCTS + "." + KEY_BCODE + " AS _id," + " "
				+ TABLE_PRODUCTS + "." + KEY_NAME + "," + " " + TABLE_PRODUCTS
				+ "." + KEY_PRICE  + " FROM " + TABLE_PRODUCTS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		onCreate(db);
	}

	/**
	 * Given an id in the form of a string, returns a matching Product from
	 * the database, if there is any. Otherwise returns a null Product.
	 * 
	 * @param ID the id (barcode data) of the item
	 * @return the matching product if there is any, otherwise null
	 */
	public Product getProduct(String ID) {
		SQLiteDatabase db = this.getReadableDatabase();

		// Create and place the cursor
		Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { KEY_BCODE,
				KEY_NAME, KEY_PRICE}, KEY_BCODE + "=?",
				new String[] { String.valueOf(ID) }, null, null, null, null);
		
		Product product = null;
		if(cursor.getCount() != 0){
			cursor.moveToFirst();
		}else{
			return product;
		}
		
		// read from the cursor to parse a possible Product from the given ID
		product = new Product(cursor.getString(1),
				Integer.parseInt(cursor.getString(2)),
				(cursor.getString(0)));

		return product;
	}

	/**
	 * Given a product, adds that product to the database.
	 * 
	 * @param p the product to be added to the database.
	 */
	public void addProduct(Product p) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, p.getName());
		values.put(KEY_PRICE, p.getPrice());
		values.put(KEY_BCODE, p.getBarcode());

		db.insert(TABLE_PRODUCTS, null, values);
		db.close();
	}

	/**
	 * Returns all the products that are in the database in the form
	 * of a list.
	 * 
	 * @return a list containing all the products in the database.
	 */
	public List<Product> getProducts() {
		List<Product> productList = new ArrayList<Product>();

		String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

		SQLiteDatabase db = this.getWritableDatabase();

		// Create the cursor (works like an Iterator)
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Product product = new Product();
				
				product.setBarcode((cursor.getString(0)));
				product.setName(cursor.getString(1));
				product.setPrice(Integer.parseInt(cursor.getString(2)));

				productList.add(product);
			} while (cursor.moveToNext());
		}

		return productList;
	}

	/**
	 * Given a barcode id in the form of a string, removes any product that matches
	 * this barcode id from the database.
	 * 
	 * @param p the barcode id that matches the product to be removed
	 */
	public void removeProduct(String p) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCTS, KEY_BCODE + " = ?", new String[] { String.valueOf(p) });
		db.close();
	}

	/**
	 * Returns the number of products in the database.
	 * 
	 * @return an int representing the number of products in the database
	 */
	public int getProductsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	/**
	 * Given a product, this function updates the item in the database that has a matching
	 * barcode id to the id in the given product. 
	 * 
	 * @param p the product to be updated
	 * @return the row in the database that was updated
	 */
	public int updateProduct(Product p) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, p.getName());
		values.put(KEY_PRICE, p.getPrice());
		values.put(KEY_BCODE, p.getBarcode());

		// updates the corresponding row in the database
		return db.update(TABLE_PRODUCTS, values, KEY_BCODE + " = ?", new String[] { String.valueOf(p.getBarcode()) });
	}

}

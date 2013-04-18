package com.github.barcodescanner.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// The name of the database and its tables, views and columns
	static final String DB_NAME = "BarcodeDB";
	static final String TABLE_PRODUCTS = "Products";
	static final String KEY_ID = "ProductID";
	static final String KEY_BCODE = "ProductBarcode";
	static final String KEY_NAME = "ProductName";
	static final String KEY_PRICE = "Price";
	static final String VIEW_PRODUCTS = "Products";

	/*
	 * change the number in the superconstructor to invoke the onUpgrade method;
	 * I/E you change the version number
	 */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE " + TABLE_PRODUCTS + " (" + KEY_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME + " TEXT, "
				+ KEY_PRICE + " Integer, " + KEY_BCODE + " INTEGER NOT NULL");

		db.execSQL("CREATE VIEW " + VIEW_PRODUCTS + " AS SELECT "
				+ TABLE_PRODUCTS + "." + KEY_ID + " AS _id," + " "
				+ TABLE_PRODUCTS + "." + KEY_NAME + "," + " " + TABLE_PRODUCTS
				+ "." + KEY_PRICE + "," + " " + TABLE_PRODUCTS + "."
				+ KEY_BCODE + "" + " FROM " + TABLE_PRODUCTS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		onCreate(db);
	}

	// Method for getting a single product from the db
	public Product getProduct(int ID) {
		SQLiteDatabase db = this.getReadableDatabase();

		// Create and place the cursor
		Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { KEY_ID,
				KEY_NAME, KEY_PRICE, KEY_BCODE }, KEY_ID + "=?",
				new String[] { String.valueOf(ID) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		// read from the cursor to parse a possible Product from teh given ID
		Product product = new Product(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), Integer.parseInt(cursor.getString(2)),
				Integer.parseInt(cursor.getString(3)));
		// return product
		return product;
	}

	// Method for adding a single product to teh db
	public void addProduct(Product p) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, p.getName()); // Product name
		values.put(KEY_ID, p.getID()); // Product id
		values.put(KEY_PRICE, p.getPrice()); // Product price
		values.put(KEY_BCODE, p.getBarcode()); // Product barcode

		// Inserting Row
		db.insert(TABLE_PRODUCTS, null, values);
		db.close(); // Closing database connection
	}

	// Method for getting all the products in the db
	public List<Product> getProducts() {
		List<Product> productList = new ArrayList<Product>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

		// Fetch the db
		SQLiteDatabase db = this.getWritableDatabase();

		// Create the cursor (works like an Iterator)
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Product product = new Product();
				product.setID(Integer.parseInt(cursor.getString(0)));
				product.setName(cursor.getString(1));
				product.setPrice(Integer.parseInt(cursor.getString(2)));
				product.setBarcode(Integer.parseInt(cursor.getString(3)));
				// Adding product to list
				productList.add(product);
			} while (cursor.moveToNext());
		}

		// return product list
		return productList;
	}

	// Method for removing a product
	public void removeProduct(Product p) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCTS, KEY_ID + " = ?",
				new String[] { String.valueOf(p.getID()) });
		db.close();
	}

	// method for getting the products count
	public int getProductsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Method for updating single product
	public int updateProduct(Product p) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, p.getName()); // Product name
		values.put(KEY_ID, p.getID()); // Product id
		values.put(KEY_PRICE, p.getPrice()); // Product price
		values.put(KEY_BCODE, p.getBarcode()); // Product barcode

		// updating row
		return db.update(TABLE_PRODUCTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(p.getID()) });
	}

}

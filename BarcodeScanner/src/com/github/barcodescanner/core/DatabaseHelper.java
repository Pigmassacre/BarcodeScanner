package com.github.barcodescanner.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	static final String dbName = "BarcodeDB";
	static final String productTable = "Products";
	static final String colID = "ProductID";
	static final String colBCode = "ProductBarcode";
	static final String colName = "ProductName";
	static final String colPrice = "Price";
	static final String viewProducts="ViewProducts";

	/*
	 * change the number in the superconstructor to invoke the onUpgrade method;
	 * I/E you change the version number
	 */
	public DatabaseHelper(Context context) {
		super(context, dbName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		  db.execSQL("CREATE TABLE "+productTable+" ("+colID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
				  	colName+" TEXT, "+colPrice+" Integer, "+colBCode+" INTEGER NOT NULL");


		  db.execSQL("CREATE VIEW "+viewProducts+
				    " AS SELECT "+productTable+"."+colID+" AS _id,"+
				    " "+productTable+"."+colName+","+
				    " "+productTable+"."+colPrice+","+
				    " "+productTable+"."+colBCode+""+
				    " FROM "+productTable
				    );

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS "+productTable);
		onCreate(db);
	}

}

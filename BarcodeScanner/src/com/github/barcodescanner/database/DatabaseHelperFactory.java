package com.github.barcodescanner.database;

import android.content.Context;

public class DatabaseHelperFactory {

	private static DatabaseHelper databaseHelper;

	/**
	 * Initiates an instance of the DatabaseHelper if there isn't any already.
	 * If there is, nothing happens.
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		if (databaseHelper == null) {
			databaseHelper = new DatabaseHelper(context);
		}
	}

	/**
	 * Returns the instance of DatabaseHelper (or null, if it isn't instantiated)
	 * 
	 * @return databaseHelper The instance of DatabaseHelper
	 */
	public static DatabaseHelper getInstance() {
		return databaseHelper;
	}
}

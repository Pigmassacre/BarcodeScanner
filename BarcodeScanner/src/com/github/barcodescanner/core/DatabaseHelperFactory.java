package com.github.barcodescanner.core;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperFactory{
	

    private static DatabaseHelper databaseHelper;

    public static void init(Context context ){
       databaseHelper = new DatabaseHelper(context);
    }
    
     public static DatabaseHelper getInstance(){
    	return databaseHelper;
    }
}

package com.github.barcodescanner.core;

import android.content.Context;

public class DatabaseHelperFactory{
	

    private static DatabaseHelper databaseHelper;

    public static void init(Context context ){
       databaseHelper = new DatabaseHelper(context);
    }
    
     public static DatabaseHelper getInstance(){
    	return databaseHelper;
    }
}

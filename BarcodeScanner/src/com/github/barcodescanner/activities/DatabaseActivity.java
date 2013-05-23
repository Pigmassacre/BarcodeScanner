package com.github.barcodescanner.activities;

import java.util.List;

import com.github.barcodescanner.R;
import com.github.barcodescanner.R.drawable;
import com.github.barcodescanner.R.layout;
import com.github.barcodescanner.core.DatabaseHelper;
import com.github.barcodescanner.core.Product;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DatabaseActivity extends Activity{
	
	@SuppressWarnings("unused")
	private static final String TAG = "DatabaseActivity";
	DatabaseHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_database);
	}
	
	private void addRow(List<String> values){
		TableLayout table = (TableLayout)findViewById(R.layout.activity_database);    
		TableRow row = new TableRow(this);
		row.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		TextView textView;
		for(int i = 0; i<values.size()-1; i++){
			textView = generateCell(values.get(i).toString());
			row.addView(textView);
		}
		
		table.addView(row, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	private void removeRow(){
		//TODO
	}
	
	private TextView generateCell(String text){
		TextView nameView = new TextView(this);
		nameView.setText(text);
		nameView.setBackgroundResource(R.drawable.cell_normal);
		nameView.setGravity(Gravity.CENTER_HORIZONTAL);
		nameView.setTypeface(null, Typeface.BOLD);
		return nameView;
	}
}


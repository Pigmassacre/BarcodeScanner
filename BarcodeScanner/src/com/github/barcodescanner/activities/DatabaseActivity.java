package com.github.barcodescanner.activities;

import java.util.ArrayList;
import java.util.List;

import com.github.barcodescanner.R;
import com.github.barcodescanner.core.DatabaseHelper;
import com.github.barcodescanner.core.DatabaseHelperFactory;
import com.github.barcodescanner.core.Product;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DatabaseActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "DatabaseActivity";
	DatabaseHelper db;
	List<String[]> items = new ArrayList();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_database);
		ListView list = (ListView) findViewById(R.id.list);
		 
		String[] item = {"item1Name", "item1Price","item1Id"};
		String[] item2 = {"item2Name", "item2Price","item2Id"};
		items.add(item);
		items.add(item2);
		SpecialAdapter adapter = new SpecialAdapter(this, items);
		list.setAdapter(adapter);

		DatabaseHelperFactory.init(this);
		db = DatabaseHelperFactory.getInstance();
		
		List<Product> products = db.getProducts();
		List<String> tempList = new ArrayList<String>();
		if (products.size() > 0) {
			for (int i = 0; i < products.size(); i++) {
				tempList.clear();

				tempList.add(products.get(i).getBarcode());
				tempList.add(products.get(i).getName());
				tempList.add(Integer.toString(products.get(i).getPrice()));

				addRow(tempList);
			}
		}

	}
	
	static class ViewHolder {
	    TextView name, id, price;
	}
	
	public void deleteClick(View v) {
	    ImageButton button = (ImageButton) v;
	    TableRow row = (TableRow)button.getParent();
	    TextView textChild = (TextView) row.getChildAt(0);
	    String text = textChild.getText().toString();
	    text.split("id");
	}

	private void addRow(List<String> values) {
		TableLayout table = (TableLayout) findViewById(R.id.list);
		TableRow row = new TableRow(this);
		row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		TextView textView;
		for (int i = 0; i < values.size(); i++) {
			textView = generateCell(values.get(i).toString());
			row.addView(textView);
			row.setGravity(Gravity.CENTER);
		}
		table.addView(row, new TableLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/*
	 * 
	 * 
	private void removeRow() {
		// TODO
	}*/
	
	
	
	private TextView generateCell(String text) {
		// TODO Fix this, it looks ugly on the device
		TextView nameView = new TextView(this);
		nameView.setText(text);
		nameView.setGravity(Gravity.CENTER_HORIZONTAL);
		nameView.setTypeface(null, Typeface.BOLD);
		//nameView.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
		return nameView;
	}
	
	private class SpecialAdapter extends BaseAdapter {
		//Defining the background color of rows. The row will alternate between green light and green dark.
		private int[] colors = new int[] { 0xAA999999, 0xAA7d7d7d };
		private LayoutInflater mInflater;
		 
		//The variable that will hold our text data to be tied to list.
		private List<String[]> data;
		 
		public SpecialAdapter(Context context, List<String[]> items) {
		    mInflater = LayoutInflater.from(context);
		    this.data = items;
		}
		 
		@Override
		public int getCount() {
		    return data.size();
		}
		 
		@Override
		public Object getItem(int position) {
		    return position;
		}
		 
		@Override
		public long getItemId(int position) {
		    return position;
		}
		 
		//A view to hold each row in the list
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		 
		// A ViewHolder keeps references to children views to avoid unneccessary calls
		// to findViewById() on each row.
		ViewHolder holder;
		 
		if (convertView == null) {
		    convertView = mInflater.inflate(R.layout.row, null);
		 
		    holder = new ViewHolder();
		    holder.name = (TextView) convertView.findViewById(R.id.name);
		    holder.price = (TextView) convertView.findViewById(R.id.price);
		    holder.id = (TextView) convertView.findViewById(R.id.id);
		    convertView.setTag(holder);
		} else {
		    holder = (ViewHolder) convertView.getTag();
		}
		    // Bind the data efficiently with the holder.
		    holder.name.setText(data.get(position)[0]);
		    holder.price.setText(data.get(position)[1]);
		    holder.id.setText(data.get(position)[2]);
		 
		    //Set the background color depending of  odd/even colorPos result
		    int colorPos = position % colors.length;
		    convertView.setBackgroundColor(colors[colorPos]);
		 
		   return convertView;
		}
		}
}



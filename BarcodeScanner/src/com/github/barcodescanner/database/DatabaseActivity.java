package com.github.barcodescanner.database;

import java.util.ArrayList;
import java.util.List;

import com.github.barcodescanner.R;
import com.github.barcodescanner.libraries.selv.ActionSlideExpandableListView;
import com.github.barcodescanner.libraries.selv.SlideExpandableListAdapter;
import com.github.barcodescanner.product.EditProductActivity;
import com.github.barcodescanner.product.Product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DatabaseActivity extends Activity {

	@SuppressWarnings("unused")
	private static final String TAG = "DatabaseActivity";

	private DatabaseHelper db;
	private List<Product> items = new ArrayList<Product>();
	private ActionSlideExpandableListView list;
	private boolean adminMode;
	private EditText searchBar;
	private String searchQuery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_database);

		adminMode = getIntent().getExtras().getBoolean("adminMode");

		DatabaseHelperFactory.init(this);
		db = DatabaseHelperFactory.getInstance();

		list = (ActionSlideExpandableListView) findViewById(R.id.list);
		searchBar = (EditText)findViewById(R.id.search_product);
		searchQuery = "";
		updateSpecialAdapter();
		
		
        /**
         * Enabling Search Filter
         * */
        searchBar.addTextChangedListener(new TextWatcher() {
             
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                searchQuery = cs.toString();
                updateSpecialAdapter();
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                          
            }
        });
	}

	@Override
	protected void onResume() {
		super.onResume();
		searchQuery = "";
		updateSpecialAdapter();
	}

	/**
	 * Updates the SpecialAdapter, which in turn updates the view of the list of
	 * all the items in the database. Uses the SlideExpandableListView library, to
	 * allow for a view to slide out from under a view.
	 */
	private void updateSpecialAdapter() {
		items = filterList(db.getProducts(), searchQuery);
		SpecialAdapter adapter = new SpecialAdapter(this, items);
		list.setAdapter(new SlideExpandableListAdapter(adapter, R.id.expandable_toggle_button, R.id.expandable));
		list.setItemActionListener(new ActionSlideExpandableListView.OnActionClickListener() {
			
			@Override
			public void onClick(View listView, View buttonView, int position) {
				System.out.println("Something clicked!");
				if (buttonView.getId() == R.id.edit_button) {
					editItem((ViewGroup) listView);
				} else if (buttonView.getId() == R.id.delete_button) {
					deleteItem((ViewGroup) listView);
				}
			}

		}, R.id.edit_button, R.id.delete_button);
	}

	/**
	 * A static class that helps the SpecialAdapter generate the database view.
	 */
	static class ViewHolder {
		TextView name, id, price;
	}

	/**
	 * Given a listView containing product information and the edit and delete buttons,
	 * this function takes the product information and asks the database to remove the
	 * corresponding product, and then updates the Special Adapter that handles the database
	 * view.
	 * 
	 * @param listView the view containing the information of the product to be removed
	 */
	private void deleteItem(ViewGroup listView) {
		if (adminMode) {
			ViewGroup currentRow = (ViewGroup) listView.getChildAt(0);
			
			TextView nameView = (TextView) currentRow.getChildAt(0);
			TextView idView = (TextView) currentRow.getChildAt(2);
			
			String name = nameView.getText().toString();
			String id = idView.getText().toString();
			db.removeProduct(id);
			
			Context context = getApplicationContext();
			CharSequence text = getString(R.string.database_toast_deleted, name);
			
			Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			
			// TODO Add confirmation dialog?

			updateSpecialAdapter();
		}
	}

	
	/**
	 * Given a listView containing product information and the edit and delete buttons,
	 * this function takes the product information and passes it along to the EditProductActivity,
	 * where the user can edit the products information.
	 * 
	 * @param listView the view containing the information of the product to be edited
	 */
	private void editItem(ViewGroup listView) {
		if (adminMode) {
			ViewGroup row = (ViewGroup) listView.getChildAt(0);
			
			// Get the name, price and id views and then store them in strings
			TextView nameView = (TextView) row.getChildAt(0);
			TextView priceView = (TextView) row.getChildAt(1);
			TextView idView = (TextView) row.getChildAt(2);

			String productName = nameView.getText().toString();
			String productPrice = priceView.getText().toString();
			String productId = idView.getText().toString();

			// Store the name, price and id in a bundle and send that bundle to
			// EditProductActivity
			Bundle editBundle = new Bundle();
			editBundle.putString("productName", productName);
			editBundle.putString("productPrice", productPrice);
			editBundle.putString("productId", productId);

			Intent editIntent = new Intent(this, EditProductActivity.class);

			editIntent.putExtras(editBundle);
			startActivity(editIntent);
		}
	}

	/**
	 * A special adapter that generates the view that shows the items in the
	 * database.
	 */
	private class SpecialAdapter extends BaseAdapter {
		// Defining the background color of rows. The row will alternate between
		// grey light and grey dark.
		private int[] colors = new int[] { 0xAA999999, 0xAA7d7d7d };
		private LayoutInflater mInflater;

		// The variable that will hold our text data to be tied to list.
		private List<Product> data;

		public SpecialAdapter(Context context, List<Product> items) {
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

		/**
		 * Given a view, this function returns a view that shows each item in
		 * the database in a top-down fashion. Every other item has a darker
		 * gray background, in order to more easily differentiate between each
		 * item.
		 * 
		 * @param int position
		 * @param View
		 *            convertView The view to add all the items to.
		 * @param ViewGroup
		 *            parent Not used, but this function overrides another
		 *            function so it stays
		 * 
		 * @return The finished view that shows all the items in the database.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// A ViewHolder keeps references to children views to avoid
			// unnecessary calls to findViewById() on each row.
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
			holder.name.setText(data.get(position).getName());
			holder.price.setText("" + data.get(position).getPrice());
			holder.id.setText(data.get(position).getBarcode());

			// Set the background color depending of odd/even colorPos result
			int colorPos = position % colors.length;
			convertView.setBackgroundColor(colors[colorPos]);

			// if a customer is viewing the database, hide the delete button
			if (!adminMode) {
				convertView.findViewById(R.id.edit_button).setVisibility(View.INVISIBLE);
				convertView.findViewById(R.id.delete_button).setVisibility(View.INVISIBLE);
			}

			return convertView;
		}
	}
	
	
	private List<Product> filterList(List<Product> list, String s){
		if(s.equals("")){
			return list;
		}
		List<Product> newList = new ArrayList<Product>();
		for(Product p : list){
			if(p.getName().toLowerCase().contains(s.toLowerCase())){
				newList.add(p);
			}
		}
		return newList;
	}
}

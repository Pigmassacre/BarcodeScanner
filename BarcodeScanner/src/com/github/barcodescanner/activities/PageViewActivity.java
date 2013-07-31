package com.github.barcodescanner.activities;

import java.util.ArrayList;
import java.util.List;

import com.github.barcodescanner.R;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PageViewActivity extends FragmentActivity{
	private BSPageAdapter pageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_page_view);
		List<Fragment> fragments = getFragments();
		pageAdapter = new BSPageAdapter(getSupportFragmentManager(),fragments);
		ViewPager viewPager = (ViewPager)findViewById(R.id.viewpager);
		viewPager.setAdapter(pageAdapter);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_page_view, menu);
		return true;
	}
	
	private List<Fragment> getFragments(){
		List<Fragment> fragmentList = new ArrayList<Fragment>();
		
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_scan_new));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_scan));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_database));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_search_button));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_search));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_add_manually_button));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_create_product));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_help));
		fragmentList.add(BSFragment.newInstance(R.drawable.slide_developer));
		
		return fragmentList;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.page_help_menu_help:
			intent = new Intent(this, HelpActivity.class);
			startActivity(intent);
			return true;
		case android.R.id.home:
			intent = new Intent(this, HelpActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}

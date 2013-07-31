package com.github.barcodescanner.activities;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class BSPageAdapter extends FragmentPagerAdapter {
	
	private List<Fragment> fragments;
	
	public BSPageAdapter(FragmentManager fragmentManager, List<Fragment> fragments){
		super(fragmentManager);
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int positionInFragments){
		return this.fragments.get(positionInFragments);
	}
	
	@Override
	public int getCount(){
		return this.fragments.size();
	}

}

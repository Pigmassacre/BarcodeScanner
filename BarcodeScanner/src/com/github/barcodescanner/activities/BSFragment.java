package com.github.barcodescanner.activities;

import com.github.barcodescanner.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class BSFragment extends Fragment {
	public static final String Extra_Message = "Extra_Message";
	
	public static final BSFragment newInstance(int imageName){
		BSFragment bsFragment = new BSFragment();
		Bundle bundle = new Bundle(1);
		bundle.putInt(Extra_Message, imageName);
		bsFragment.setArguments(bundle);
		
		return bsFragment;
	}
	
	@Override 
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		int message = getArguments().getInt(Extra_Message);
		View view = inflater.inflate(R.layout.activity_bsfragment, container, false);
		
		ImageView imageView = (ImageView) view.findViewById(R.id.fragmentImageView);
		imageView.setImageResource(message);
		
		return view;
	}

}

package com.github.barcodescanner.core;

import java.util.ArrayList;
import java.util.List;

public class BCGenerator{
	
	public BCGenerator(){}

	public List<Integer> generate(List<Integer> line){
		List<Integer> unNormalized = new ArrayList<Integer>();
		int count = 1;
		
		for (int i = 0; i < line.size() - 1 ; i++){ 
			if(line.get(i)==line.get(i+1)){
				count++;
			}
			else{
				unNormalized.add(count);
				count = 1;
			}
		}
		unNormalized.add(count);
		return unNormalized;
	}
	
	private void normalize(List<Integer> unNormalized){
		Integer[] firstandlasttwobars = {unNormalized.get(0),unNormalized.get(1),unNormalized.get(28),unNormalized.get(29)};
		System.out.println(firstandlasttwobars[0]);
		
	}

}

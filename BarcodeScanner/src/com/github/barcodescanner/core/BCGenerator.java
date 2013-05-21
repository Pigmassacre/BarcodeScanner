package com.github.barcodescanner.core;

import java.util.ArrayList;
import java.util.List;

public class BCGenerator{
	
	private List<Integer> unNormalized = new ArrayList<Integer>();
	private List<Integer> normalized = new ArrayList<Integer>();
	
	public BCGenerator(){}
	//11000
	//23
	public void generate(List<Integer> line){
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
	}
	
	private void normalize(){
		
		
	}

}

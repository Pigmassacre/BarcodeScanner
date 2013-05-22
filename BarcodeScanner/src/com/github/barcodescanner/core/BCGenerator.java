package com.github.barcodescanner.core;

import java.util.ArrayList;
import java.util.List;

public class BCGenerator{
	private List<Integer> unNormalized = null;
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
		this.unNormalized = unNormalized;
		return unNormalized;
	}
	
	public String normalize(){
		StringBuffer s = new StringBuffer();
		if (unNormalized==null){
			return "No unnormalized data is set";
		}
		else {			
			int lengths = unNormalized.get(0) + unNormalized.get(1) + unNormalized.get(unNormalized.size()-1) + unNormalized.get(unNormalized.size()-2) + unNormalized.get(unNormalized.size()/2);
			float division = lengths / 5;
			
			for (int i = 0; i < unNormalized.size(); i++){
				int value = Math.round(unNormalized.get(i)/division);
				s.append(value);
			}
			return s.toString();
		}
	}
}

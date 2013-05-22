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
		StringBuffer s = null;
		if (unNormalized==null){
			return "No unnormalized data is set";
		}
		else {
			List<Integer> normalized;
			int lengths = unNormalized.get(0) + unNormalized.get(1) + unNormalized.get(unNormalized.size()-1) + unNormalized.get(unNormalized.size()-2);
			float division = lengths / 4;
			
			for (int i = 0; i < unNormalized.size(); i++){
				int value = Math.round(unNormalized.get(i)/division);
				s.append(value);
			}
			return s.toString();
		}
	}
	
	public boolean compare(String one, String two, int threshhold){
		if (one.length() == two.length()){
		int distance = 0;
			int current;
			for (int i = 0; i < one.length();i++){
				current = Integer.parseInt(one.charAt(i)+"") - Integer.parseInt(two.charAt(i)+"");
				distance += Math.abs(current);
			}
			return distance <= threshhold;
		}else {
			return false;
		}
	}
	
}

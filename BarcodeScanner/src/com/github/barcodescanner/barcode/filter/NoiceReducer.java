package com.github.barcodescanner.barcode.filter;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Sampler;

public class NoiceReducer {
	private Bitmap bitmap;
	private List<float[]> BoxSData = new ArrayList<float[]>();
	private int startH;
	private int endH;
	private int startW ;
	private int endW;
	private int previousColor;
	
	public NoiceReducer(Bitmap bitmap){
		this.bitmap = bitmap;
	}

	public void ApplyMedianFilter(){
		previousColor = 0;
		for(int y = 0; y < bitmap.getHeight()-1; y++){
			for (int x = 0; x < bitmap.getWidth()-1; x++){ //O(N^2)
				int color = scanMedianForPixel(y, x);
				bitmap.setPixel(x, y, color);
			}
		}		
	}
	
	private Integer scanMedianForPixel(int heightLine, int widthLine){
		//Heuristics to skip the sort for most cases
		if(hasSameColorAsPrevious(heightLine, widthLine)){
			return previousColor;
		}
		BoxSData.clear();
		correctBoxHeight(heightLine);
		correctBoxWidth(widthLine);
		int red,green,blue;
		float[] hsv = new float[3];
		int currentPixel;
		for(int w = startW; w < endW; w++){
			for(int h = startH; h < endH; h++){
				currentPixel = bitmap.getPixel(w, h);
				red = Color.red(currentPixel);
				green = Color.green(currentPixel);
				blue = Color.blue(currentPixel);
				Color.RGBToHSV(red, green, blue, hsv);
				BoxSData.add(hsv);
			}
		}
		if(BoxSData.size() != 0){
			insertionSortByHsv();
			hsv = BoxSData.get(((BoxSData.size()-1)/2));
		}
		int color = Color.HSVToColor(hsv);
		return color;
	}
	
	private boolean hasSameColorAsPrevious(int heightLine, int widthLine){
		int currentPixel = bitmap.getPixel(heightLine, widthLine);
		int red = Color.red(currentPixel);
		int green = Color.green(currentPixel);
		int blue = Color.blue(currentPixel);
		int color = Color.rgb(red, green, blue);
		return color == this.previousColor;
	}
	
	private void insertionSortByHsv(){
		int i,j;
		float[] newValue;
		for(i = 1; i < BoxSData.size()-1; i++){
			newValue = BoxSData.get(i);
			j = i;
			while(j > 0 && BoxSData.get(j-1)[2] > BoxSData.get(j)[2]){
				BoxSData.set(j, BoxSData.get(j-1));
				j--;
			}
			BoxSData.set(j, newValue);
		}
	}
	
	private void quickSortByHSV(int low, int high){
		int i = low;
		int j = high;
		float[] pivot = BoxSData.get(low + (high-low)/2);
		while(i <= j){
			while(BoxSData.get(i)[2] < pivot[2]){
				i++;
			}
			while(BoxSData.get(j)[2] > pivot[2]){
				j--;
			}
			if(i <= j){
				exchange(i, j);
				i++;
				j--;
			}
		}
		if(low < j){
			quickSortByHSV(low, j);
		}
		if(i < high){
			quickSortByHSV(i, high);
		}
	}

	private void exchange(int i, int j){
		float[] temp = BoxSData.get(i);
		BoxSData.set(i, BoxSData.get(j));
		BoxSData.set(j, temp);
	}
	
	private void correctBoxHeight(int heightLine){
		startH = heightLine - 1;
		endH = heightLine + 1;
		if(startH < 0){
			startH = 0;
		}
		if(endH > bitmap.getHeight()-1){
			endH = bitmap.getHeight()-1;
		}
	}
	
	private void correctBoxWidth(int widthLine){
		startW = widthLine - 1;
		endW = widthLine + 1;
		if(startW < 0){
			startW = 0;
		}
		if(endW > bitmap.getWidth()-1){
			endW = bitmap.getWidth()-1;
		}
	}
}

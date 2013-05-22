package com.github.barcodescanner.core;

import java.util.ArrayList;
import java.util.List;

public class BCGenerator {
	// private List<Integer> unNormalized = null;
	private String[] barcodeNumbers;

	public BCGenerator() {
		barcodeNumbers = new String[10];
		barcodeNumbers[0] = "3211";
		barcodeNumbers[1] = "2221";
		barcodeNumbers[2] = "2122";
		barcodeNumbers[3] = "1411";
		barcodeNumbers[4] = "1132";
		barcodeNumbers[5] = "1231";
		barcodeNumbers[6] = "1114";
		barcodeNumbers[7] = "1312";
		barcodeNumbers[8] = "1213";
		barcodeNumbers[9] = "3112";
	}

	/**
	 * Generates a new array repesenting the length of each part in the barcode
	 * 
	 * @param line
	 *            segment of 0s and 1s containing a barcode found in BCLocate
	 * @return the new array
	 */
	public List<Integer> generate(List<Integer> line) {
		List<Integer> unNormalized = new ArrayList<Integer>();
		int count = 1;

		for (int i = 0; i < line.size() - 1; i++) {
			if (line.get(i) == line.get(i + 1)) {
				count++;
			} else {
				unNormalized.add(count);
				count = 1;
			}
		}
		unNormalized.add(count);
		// this.unNormalized = unNormalized;
		return unNormalized;
	}

	/**
	 * Normalizes the array given in the generate method
	 * 
	 * @param the
	 *            list of integers (the barcode data) to be normalized
	 * @return a string containing the normalized data, this string is used as
	 *         key in the database
	 */
	public String normalize(List<Integer> unNormalized) {
		StringBuffer tempStringBuffer = new StringBuffer();
		StringBuffer finalStringBuffer = new StringBuffer();
		String tempString = "";
		int sumEven = 0, sumOdd = 0, checksumValue = 0, checksumDigit = 0;

		if (unNormalized == null) {
			return "No unnormalized data is set";
		} else {
			// List<Integer> normalized;
			int lengths = unNormalized.get(0) + unNormalized.get(1)
					+ unNormalized.get(unNormalized.size() - 1)
					+ unNormalized.get(unNormalized.size() - 2);
			float division = lengths / 4;

			for (int i = 0; i < unNormalized.size(); i++) {
				int value = Math.round(unNormalized.get(i) / division);
				tempStringBuffer.append(value);
			}

			System.out.println("normalize: tempStringBuffer: "
					+ tempStringBuffer);
			System.out.println("normalize: length of tempStringBuffer: "
					+ tempStringBuffer.length());

			// we delete the five middle bars, they are not used for generating
			// the numbers.
			tempStringBuffer.delete(28, 33);

			System.out.println("normalize: tempStringBuffer: "
					+ tempStringBuffer);
			System.out.println("normalize: length of tempStringBuffer: "
					+ tempStringBuffer.length());

			for (int i = 3; i < tempStringBuffer.length() - 2; i += 4) {
				tempString = tempStringBuffer.substring(i, i + 4); // each
																	// barcode
																	// number is
																	// composed
																	// by four
																	// bars
				System.out.println("normalize: i: " + i);
				System.out.println("normalize: tempString: " + tempString);
				for (int j = 0; j < barcodeNumbers.length; j++) {
					if (tempString.equals(barcodeNumbers[j])) { // does this
																// segment match
																// any barcode
																// number?
						finalStringBuffer.append(j); // then append it to final
														// string
						System.out.println("normalize: " + j + " found!");
						// adds the matching barcode number to the final
						// string
					}
				}
			}

			for (int i = 0; i < finalStringBuffer.length(); i++) {
				if ((i % 2) == 0) { // even numbers
					sumEven = sumEven + Character.getNumericValue(finalStringBuffer.charAt(i));
				} else {
					sumOdd = sumOdd + Character.getNumericValue(finalStringBuffer.charAt(i));
				}
			}
			
			sumEven = 3 * sumEven;
			
			checksumValue = sumEven + sumOdd;
			System.out.println("normalize: checksumValue: " + checksumValue);
			checksumDigit = (checksumValue % 10);
			
			if (checksumDigit == 10) {
				checksumDigit = 0;
			}
			
			System.out.println("normalize: checksumDigit is: " + checksumDigit);

			System.out.println("normalize: finalStringBuffer: "
					+ finalStringBuffer);
			return finalStringBuffer.toString();
		}
	}

	/**
	 * compares two strings and decides if this is the same product
	 * 
	 * 
	 * @param one
	 * @param two
	 * @param threshhold
	 * @return
	 */
	public boolean compare(String one, String two, int threshhold) {
		if (one.length() == two.length()) {
			int distance = 0;
			int current;
			for (int i = 0; i < one.length(); i++) {
				current = Integer.parseInt(one.charAt(i) + "")
						- Integer.parseInt(two.charAt(i) + "");
				distance += Math.abs(current);
			}
			return distance <= threshhold;
		} else {
			return false;
		}
	}

}

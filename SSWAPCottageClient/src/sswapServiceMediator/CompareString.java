package sswapServiceMediator;

import java.util.ArrayList;
import java.util.Arrays;
/**
 * @author Bipika
 * @version 23.4.2019
 *
 */
public class CompareString {
	static ArrayList<String> strlist1 = new ArrayList<String>();

//	  generating substring of n length list from given word
	public ArrayList<String> generateNGrams(String string, int n) {
		String str = string.toLowerCase().replace("_", "");
		ArrayList<String> nGrams = new ArrayList<String>();
		int ind = str.length() - n;
		for (int i = 0; i <= ind; i++) {
			nGrams.add(str.substring(i, i + n));
		}
		return nGrams;

	}

//	  find the intersecting words 
	public int intersection(ArrayList<String> chunk1, ArrayList<String> chunk2) {
		strlist1 = new ArrayList<String>();
		strlist1.addAll(chunk1);
		chunk1.retainAll(chunk2);
		int i = chunk1.size();
		return i;
	}

//	  calculate ngrams 
	public float ngrams(ArrayList<String> str1, ArrayList<String> str2, int inter) {
		float ngram = (float) ((2.0 * inter) / (str1.size() + str2.size()));
		return ngram;
	}

	public static void main(String[] args) {
//		initiation of class
		CompareString ng = new CompareString();
//		ngram size
		int n = 3;
////		string list to compare
		ArrayList<String> string1 = new ArrayList<String>(Arrays.asList("cottage_image", "booker_name"));
		ArrayList<String> string2 = new ArrayList<String>(Arrays.asList("bookerName", "cottageImageURL"));
		for (int i = 0; i < string1.size(); i++) {
			ArrayList<Float> ngramList = new ArrayList<Float>();
			ArrayList<String> str1 = ng.generateNGrams(string1.get(i), n);
			int ind1 = i;
			for (int j = 0; j < string2.size(); j++) {
				ArrayList<String> str2 = ng.generateNGrams(string2.get(j), n);

//				finding the common chunk of sting among two strings
				int inter = ng.intersection(str1.isEmpty() ? strlist1 : str1, str2);
//				ngram calculation
				float ngram = ng.ngrams(str1.isEmpty() ? strlist1 : str1, str2, inter);
				ngramList.add(ngram);

			}
			System.out.println(ngramList);
			float max = ngramList.get(0);
			int ind2 = 0;
			for (int x = 0; x < ngramList.size(); x++) {
				if (max < ngramList.get(x)) {
					max = ngramList.get(x);
					ind2 = x;
				}
			}
//			display matched strings
			if (max > 0.5) {
				System.out.println(string1.get(ind1) + " -> " + string2.get(ind2));
			}
		}
	}

}
package sswapServiceMediator;


import java.util.ArrayList;

/**
 * @author Bipika
 * @version 23.4.2019
 *
 */
public class CompareString {
//	  generating substring of n length list from given word
		public ArrayList<String> generateNGrams(String string, int n) {
		  String str= string.toLowerCase().replace("_", "");
		  ArrayList<String> nGrams = new ArrayList<String>();
		  int ind=str.length()-n;
		  for (int i=0; i<=ind;i++) {
			  nGrams.add(str.substring(i, i + n));
		  }
		  return nGrams;
	    
	  }
//	  find the intersecting words 
	  public int intersection(ArrayList<String> str1,ArrayList<String> str2) {
		  str1.retainAll( str2 );
		  return str1.size();
	  }
//	  calculate ngrams 
	  public float ngrams(ArrayList<String> str1,ArrayList<String> str2,int inter) {
		  float ngram= (float) ((2.0*inter)/(str1.size()+str2.size()));
		  return ngram;
	  }

	  public static void main(String[] args) {
//		initiation of class
		CompareString ng = new CompareString();
//		ngram size
		int n=3;
//		strings to compare
		String string1 ="cottage_image";
		String string2 ="cottageImageURL";
//		generate list with n word length
		ArrayList<String> str1=ng.generateNGrams(string1, n);
		ArrayList<String> str2=ng.generateNGrams(string2, n);
//		finding the common chunk of sting among two strings
		int inter = ng.intersection(str1, str2);
//		ngram calculation
		float ngram= ng.ngrams(str1, str2, inter);
		System.out.println(ngram);
//		display matched strings
		if (ngram>0.5)
		{
			System.out.println(string1+"->"+string2);
		}
	  }

	}
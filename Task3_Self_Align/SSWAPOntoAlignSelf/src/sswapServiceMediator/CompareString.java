package sswapServiceMediator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import sswapServiceMediator.RDGParser;

import org.apache.commons.io.FileUtils;
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

	public static ArrayList<String> ngramCalculate(String uri1,String uri2) throws IOException {
		ArrayList<String> resultList = new ArrayList<String>();
//		initiation of class
		CompareString ng = new CompareString();
//		ngram size
		int n = 3;
//		URL rdgUrl1 = new URL("http://users.jyu.fi/~bamatya/TIES4371/onto/mySSWAPServiceRDG");
		URL rdgUrl1 = new URL(uri1);
		File rdgFile1 = new File("rdg1");
		FileUtils.copyURLToFile(rdgUrl1, rdgFile1);
		

//		URL rdgUrl2 = new URL("http://users.jyu.fi/~jahasall/TIES4371/rdg");
		URL rdgUrl2 = new URL(uri2);
		File rdgFile2 = new File("rdg2");
		FileUtils.copyURLToFile(rdgUrl2, rdgFile2);
		
//		string list to compare; lists of properties from RDGParser.java here
		ArrayList<String> string1 = (ArrayList<String>) RDGParser.parseRDG(rdgFile1);
		ArrayList<String> string2 = (ArrayList<String>) RDGParser.parseRDG(rdgFile2);
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
//			System.out.println(ngramList);
			float max = ngramList.get(0);
			int ind2 = 0;
			for (int x = 0; x < ngramList.size(); x++) {
				if (max < ngramList.get(x)) {
					max = ngramList.get(x);
					ind2 = x;
				}
			}
//			display matched strings
			if (max > 0.45) {
				resultList.add(string1.get(ind1) + " -> " + string2.get(ind2));
			}
			else if (max<0.5 && max!=0)
			{
				resultList.add("Confidence: "+max+" for '"+string1.get(ind1)+"' and '"+string2.get(ind2)+"'");
			}
			
		}
		return resultList;
	}

}
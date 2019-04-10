/**
 * 
 */
package sswapServiceMediator;

import org.apache.commons.lang3.RandomStringUtils;
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;

import fr.inrialpes.exmo.align.impl.method.StringDistAlignment;
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @author Janita
 * @version 9.4.2019
 *
 */
public class OntologyAlignment {
	
	private static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	/**
	 * Test main to make sure class runs properly
	 * @param args not used
	 */
	public static void main( String[] args ) {
		try {
			URI uri1 = new URI("http://users.jyu.fi/~jahasall/TIES4371/bookingCottage.owl");
			URI uri2 = new URI("http://users.jyu.fi/~jahasall/TIES4371/ontology_cottage.owl");
			alignWithSmoaDistance(uri1, uri2);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("Something wrong with URIs");
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * Uses smoaDistance method to align ontologies
	 * @param uri1 owl uri
	 * @param uri2 second owl uri
	 * @return created alignment file
	 */
	public static File alignWithSmoaDistance(URI uri1, URI uri2) {
		try {
			System.out.println("Starting alignment...");
			Properties params = new Properties();
			params.setProperty("stringFunction", "smoaDistance");
			AlignmentProcess process = new StringDistAlignment();
			process.init(uri1, uri2);
			process.align((Alignment) null, params);
			
			// save to new file
			String fileName = RandomStringUtils.random(7, characters) +".owl";
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			AlignmentVisitor renderer = new OWLAxiomsRendererVisitor(writer);
			process.render(renderer);
			writer.flush();
			writer.close();
			File createdFile = new File(fileName);
			System.out.println("Alignment saved to:" + createdFile.getAbsolutePath());
			return createdFile;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlignmentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * Uses ngram distance method to align ontologies.
	 * @param uri1 owl uri
	 * @param uri2 second owl uri
	 * @return created alignment file
	 */
	public static File alignWithNgramDistance(URI uri1, URI uri2) {
		try {
			System.out.println("Starting alignment...");
			Properties params = new Properties();
			params.setProperty("stringFunction", "ngramDistance");
			AlignmentProcess process = new StringDistAlignment();
			process.init(uri1, uri2);
			process.align((Alignment) null, params);
			
			// save to new file
			String fileName = RandomStringUtils.random(7, characters) +".owl";
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			AlignmentVisitor renderer = new OWLAxiomsRendererVisitor(writer);
			process.render(renderer);
			writer.flush();
			writer.close();
			File createdFile = new File(fileName);
			System.out.println("Alignment saved to:" + createdFile.getAbsolutePath());
			return createdFile;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AlignmentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}

/**
 * 
 */
package sswapServiceMediator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Janita
 * @version 22.4.2019
 *
 */
public class RDGParser {

	/**
	 * @param args not used
	 */
	public static void main(String[] args) {
	try {
//		URL rdgUrl = new URL("http://users.jyu.fi/~jahasall/TIES4371/rdg");
		URL rdgUrl = new URL("http://users.jyu.fi/~bamatya/TIES4371/onto/mySSWAPServiceRDG");
		File rdgFile = new File("rdg");
		FileUtils.copyURLToFile(rdgUrl, rdgFile);
		parseRDG(rdgFile);
	} catch (IOException e) {
		System.out.println("Could not open file");
		e.printStackTrace();
	}
	
	}
	
	/**
	 * Parses RDG file in XML format
	 * @param file RDG file
	 * @return list of properties in RDG file, including properties in mapsTo element
	 */
	public static List<String> parseRDG(File file) {
		List<String> properties = new ArrayList<>();
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
			        .newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			StringBuilder nameSpace = new StringBuilder(
                    document.getDocumentElement().getPrefix() != null ? document.getDocumentElement().getPrefix() + ":" : "");
			NodeList list; // = document.getElementsByTagName(nameSpace + "type");
			NodeList nodes = document.getDocumentElement().getElementsByTagNameNS("*", "Subject");
			if (nodes.item(0) != null) {
				list = nodes.item(0).getChildNodes();
			} else {
				list = document.getDocumentElement().getElementsByTagNameNS("*", "subject").item(0).getChildNodes();
			}
			
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
				Element item = (Element) list.item(i);
				String tagName = item.getTagName().substring(item.getTagName().indexOf(":") + 1, item.getTagName().length()); 
				if (tagName.equalsIgnoreCase("type")) continue;
				if (item.hasChildNodes()) {
					NodeList wrappedChild = item.getChildNodes();
					if (wrappedChild.getLength() > 0) {
						for (int k = 0; k < wrappedChild.getLength(); k++) {
							properties.addAll(getChildrenFromObject(wrappedChild.item(k).getChildNodes()));
						}
					}
				}
				if (tagName.equalsIgnoreCase("mapsTo")) {
					NodeList resultElements = item.getFirstChild().getChildNodes();
					for (int m = 0; m < resultElements.getLength(); m++) {
						if (resultElements.item(m).getNodeType() != Node.ELEMENT_NODE) continue;
						Element resultItem = (Element) resultElements.item(m);
						String resultTagName = resultItem.getTagName().substring(resultItem.getTagName().indexOf(":") + 1, resultItem.getTagName().length());
						if (resultTagName.equalsIgnoreCase("type")) continue;
						properties.add(resultTagName);
						System.out.println(resultTagName);
					}
					continue;
				}
				if (!tagName.contains("has")) properties.add(tagName);
				//System.out.println(tagName);
			}
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println("Something wrong when parsing RDG file");
			e.printStackTrace();
		}
		
		return properties;
	}

	private static List<String> getChildrenFromObject(NodeList childNodes) {
		List<String> props = new ArrayList<>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
			Element child = (Element) childNodes.item(i);
			String tagName = child.getTagName().substring(child.getTagName().indexOf(":") + 1, child.getTagName().length());
			if (tagName.equalsIgnoreCase("type")) continue;
			if (child.getChildNodes().getLength() > 0) {
				props.addAll(getChildrenFromObject(child.getChildNodes()));
			} else {
				if (!tagName.contains("has")) props.add(tagName);
			}
		}
		return props;
	}

}
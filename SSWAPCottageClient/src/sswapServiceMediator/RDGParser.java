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
		URL rdgUrl = new URL("http://users.jyu.fi/~jahasall/TIES4371/rdg");
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
			NodeList list = document.getElementsByTagName(nameSpace + "type");
			for (int j = 0; j < list.getLength(); j++) {
				Node temp = (Node) list.item(j);
				Element parent = (Element) temp.getParentNode();
				NodeList children = parent.getChildNodes();
				boolean isRightParent = false;
				for (int k = 0; k < children.getLength(); k++) {
					if (children.item(k).getNodeType() != Node.ELEMENT_NODE) continue;
					Element child = (Element) children.item(k);
					if (child.getTagName() == null) continue;
					String childName = child.getTagName().substring(child.getTagName().indexOf(":") + 1, child.getTagName().length());
					if (childName.equalsIgnoreCase("mapsTo")) {
						// correct parent, if mapsTo included in element
						isRightParent = true;
						break;
					}
				}
				if (isRightParent) {
					list = parent.getChildNodes();
					break;
				}
			}
			
			for (int i = 0; i < list.getLength(); i++) {
				if (list.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
				Element item = (Element) list.item(i);
				String tagName = item.getTagName().substring(item.getTagName().indexOf(":") + 1, item.getTagName().length()); 
				if (tagName.equalsIgnoreCase("type")) continue;
				if (tagName.equalsIgnoreCase("mapsTo")) {
					NodeList resultElements = item.getFirstChild().getChildNodes();
					for (int m = 0; m < resultElements.getLength(); m++) {
						if (resultElements.item(m).getNodeType() != Node.ELEMENT_NODE) continue;
						Element resultItem = (Element) resultElements.item(m);
						String resultTagName = resultItem.getTagName().substring(resultItem.getTagName().indexOf(":") + 1, resultItem.getTagName().length());
						if (resultTagName.equalsIgnoreCase("type")) continue;
						properties.add(resultTagName);
					}
					continue;
				}
				properties.add(tagName);
			}
			
		} catch (SAXException | IOException | ParserConfigurationException e) {
			System.out.println("Something wrong when parsing RDG file");
			e.printStackTrace();
		}
		
		return properties;
	}

}

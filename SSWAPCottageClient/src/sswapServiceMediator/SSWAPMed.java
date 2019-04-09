package sswapServiceMediator;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import info.sswap.api.model.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import info.sswap.api.http.HTTPProvider;

public class SSWAPMed {

	private String serviceUrl = "";
	RDG rdg = null;
	private String queryResult;
	private List<String> lookUpNames;
	private HashMap<String,String> form;
	private String results = "";

	public SSWAPMed(){

System.out.println("-------------");	
System.out.println("I am Mediator...");	
System.out.println("-------------");	
	}

	public void sendRequest(Map<String,String[]> valueMap) {
		serviceUrl = valueMap.get("serviceURL")[0];
		System.out.println("Service URL: " + serviceUrl);
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(serviceUrl);
		CloseableHttpResponse response = null;
		try {
			response = client.execute(httpGet);
		} catch (Exception e) {
			System.out.println("Error executing httpGet: " + e);
		}

		try {
			URI uri = new URI(serviceUrl);
			rdg = SSWAP.getResourceGraph(response.getEntity().getContent(), RDG.class, uri);
		} catch (DataAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalStateException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sendRIG(rdg,valueMap);
	}

	public SSWAPSubject getSubject(RDG rdg) {
		SSWAPResource resource = rdg.getResource();
		SSWAPGraph graph = resource.getGraph();
		SSWAPSubject subject = graph.getSubject();
		return subject;
	}
	
	
	public void readRDG(final RDG rdg) {
		System.out.println("---");
		System.out.println("Read RDG...");
		System.out.println("---");
		SSWAPSubject subject = getSubject(rdg);

		Iterator<SSWAPProperty> iterator = subject.getProperties().iterator();
		System.out.println("Request properties:");
		lookUpNames = new ArrayList<>();
		while (iterator.hasNext()) { // this is a string property
			SSWAPProperty property = iterator.next();
			if (property.getValue().isLiteral()) {
				String lookupName = getStrName(property.getURI());
				lookUpNames.add(lookupName);
				System.out.println(""+lookupName);
			} else if (property.getValue().isIndividual()) { // this is an object property
				SSWAPIndividual individual = property.getValue().asIndividual();
//				Iterator<SSWAPType> types = individual.getDeclaredTypes().iterator();
				Iterator<SSWAPProperty> indIterator = individual.getProperties().iterator();
				while (indIterator.hasNext()) {
					SSWAPProperty indProperty = indIterator.next();
					if(indProperty.getValue().isLiteral()){
						String lookupName = getStrName(indProperty.getURI());
						lookUpNames.add(lookupName);
						System.out.println(""+lookupName);
					}else if(indProperty.getValue().isIndividual()){
						// we suppose there are no individuals in nested property
						System.out.println("Nested property value is Individual:");
 					}else{System.out.println("Nested property value is ???");}
				}
			}
		}
   }

	/**
	 * Sends the RIG to the service to get the RRG.
	 * @param rdg the RDG where the RIG is taken from.
	 * @return the service result.
	 */
	public void sendRIG(RDG rdg,Map<String, String[]> valueMap) {
		System.out.println("---");
		System.out.println("Send RIG...");
		System.out.println("---");
		boolean errors = false;
		SSWAPSubject subject = getSubject(rdg);
		SSWAPResource resource = rdg.getResource();

		Iterator<SSWAPProperty> iterator = subject.getProperties().iterator();

		String lookupName = "";
		String lookupValue = "";

		while (iterator.hasNext()) { // this is a string property
			SSWAPProperty property = iterator.next();
			if (property.getValue().isLiteral()) {
				SSWAPPredicate predicate = rdg.getPredicate(property.getURI());
				String value  = valueMap.get(getStrName(property.getURI()))[0];
				if (value != null)
					subject.setProperty(predicate, value);
			} else if (property.getValue().isIndividual()) { // this is an object property
				SSWAPIndividual individual = property.getValue().asIndividual();
				Iterator<SSWAPProperty> indIterator = individual.getProperties().iterator();
				while (indIterator.hasNext()) {
					SSWAPProperty indProperty = indIterator.next();
					if(indProperty.getValue().isLiteral()){
						SSWAPPredicate predicate = rdg.getPredicate(indProperty.getURI());
						String name11 = getStrName(indProperty.getURI());
						String value  = valueMap.get(getStrName(indProperty.getURI()))[0];
						if (value != null)
							individual.setProperty(predicate, value);
					}else if(indProperty.getValue().isIndividual()){
						// we suppose there are no individuals in nested property
						System.out.println("Nested property value is Individual:");
					}else{System.out.println("Nested property value is ???");}
				}
			}
		}
		
		iterator = subject.getProperties().iterator();
		while (iterator.hasNext()) {
			SSWAPProperty property = iterator.next();
			if (property.getValue().isLiteral()) {
				SSWAPPredicate predicate = rdg.getPredicate(property.getURI());
				lookupName = getStrName(property.getURI());
				lookupValue = getStrValue(subject,predicate);
				System.out.println(""+lookupName+" : "+lookupValue);
			} else if (property.getValue().isIndividual()) { // this is an object property
				SSWAPIndividual individual = property.getValue().asIndividual();
				Iterator<SSWAPProperty> indIterator = individual.getProperties().iterator();
				while (indIterator.hasNext()) {
					SSWAPProperty indProperty = indIterator.next();
					if(indProperty.getValue().isLiteral()){
						SSWAPPredicate predicate = rdg.getPredicate(indProperty.getURI());
						lookupName = getStrName(indProperty.getURI());
						lookupValue = getStrValue(individual,predicate);
						System.out.println(""+lookupName+" : "+lookupValue);
					}else if(indProperty.getValue().isIndividual()){
						// we suppose there are no individuals in nested property
						System.out.println("Nested property value is Individual:");
					}else{System.out.println("Nested property value is ???");}
				}
			}
		}

		if (errors) return;

		SSWAPGraph graph = resource.getGraph();
		graph.setSubject(subject);
		resource.setGraph(graph);

		RIG rig = resource.getRDG().getRIG();
		HTTPProvider.RRGResponse response = rig.invoke(10 * 60 * 1000);//increase timeout to 5 mins
		RRG rrg = response.getRRG();

		showResults(rrg);
	}


	/**
	 * Shows the results from the RRG graph 
	 * @param rrg the RRG graph where the results are taken from
	 */
	public void showResults(RRG rrg) {		
		System.out.println("---");
		System.out.println("Get RRG...");
		System.out.println("---");
		SSWAPResource resource = rrg.getResource();
		SSWAPGraph graph = resource.getGraph();
		SSWAPSubject subject = graph.getSubject();
		Iterator<SSWAPObject> iteratorObjects =  subject.getObjects().iterator();
		int i = 1;

		lookUpNames = new ArrayList<>();
		while (iteratorObjects.hasNext()) {
			SSWAPObject object = iteratorObjects.next();
			System.out.println("Result: "+i+" -------------");
			String values = new String();
			Iterator<SSWAPProperty> iteratorProperties = object.getProperties().iterator();
			while (iteratorProperties.hasNext()) {
				SSWAPProperty property = iteratorProperties.next();
				if (property.getValue().isLiteral()) {
					SSWAPPredicate predicate = rrg.getPredicate(property.getURI());
					String lookupName = getStrName(property.getURI());
					String lookupValue = getStrValue(object,predicate);
					if (lookupValue!=null && lookupValue.indexOf("\"")>-1){
						lookupValue = lookupValue.substring(1);
						int sub = lookupValue.indexOf("\"");
						lookupValue = lookupValue.substring(0,sub);
					}
					System.out.println(""+lookupName+" : "+lookupValue);
					values = values+lookupName+"\\"+lookupValue+'|';
				} else if (property.getValue().isIndividual()) {
					SSWAPIndividual individual = property.getValue().asIndividual();
					Iterator<SSWAPProperty> indIterator = individual.getProperties().iterator();
					while (indIterator.hasNext()) {
						SSWAPProperty indProperty = indIterator.next();
						if(indProperty.getValue().isLiteral()){
							SSWAPPredicate predicate = rrg.getPredicate(indProperty.getURI());
							String lookupName = getStrName(indProperty.getURI());
							String lookupValue = getStrValue(individual,predicate);
							if (lookupValue!=null && lookupValue.indexOf("\"")>-1){
								lookupValue = lookupValue.substring(1);
								int sub = lookupValue.indexOf("\"");
								lookupValue = lookupValue.substring(0,sub);
							}
							System.out.println(""+lookupName+" : "+lookupValue);
							values = values+lookupName+"\\"+lookupValue+'|';
						} else if(indProperty.getValue().isIndividual()){
							// we suppose there are no individuals in nested property
							System.out.println("Nested property value is Individual:");
						}else{System.out.println("Nested property value is ???");}
					}
				}
			}
			i++;
			values.substring(0,values.length()-1);
			results = results + values;
		}
		
	}


	/**
	 * Gets the value of a property from an individual
	 * @param sswapIndividual the individual whose property it is
	 * @param sswapPredicate the predicate of the property
	 * @return the value of the property as String
	 */
	private String getStrValue(SSWAPIndividual sswapIndividual, SSWAPPredicate sswapPredicate) {
		String value = null;
		SSWAPProperty sswapProperty = sswapIndividual.getProperty(sswapPredicate);
		if ( sswapProperty != null ) {
			value = sswapProperty.getValue().asString();
			if ( value.isEmpty() ) {
				value = null;
			}
		}
		return value;
	}

	/**
	 * Gets the name of the property
	 * @param uri uri of the property
	 * @return name of the property
	 */
	private String getStrName(URI uri) {
		String[] parts = uri.toString().split("#");
		return parts[1];
	}
	
	public String getServiceUrl(){
//		queryResult = "Done!";
		return serviceUrl;
	}

	public List<String> getRDG() {
		return lookUpNames;
	}

	public String getRRG(){
		return results;
	}
	
	
}

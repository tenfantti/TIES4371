package fi.jyu.Service;


import java.net.URI;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import info.sswap.api.model.RIG;
import info.sswap.api.model.SSWAPIndividual;
import info.sswap.api.model.SSWAPObject;
import info.sswap.api.model.SSWAPPredicate;
import info.sswap.api.model.SSWAPProperty;
import info.sswap.api.model.SSWAPSubject;
import info.sswap.api.servlet.MapsTo;
import org.eclipse.rdf4j.RDF4JException;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import static javax.xml.bind.DatatypeConverter.parseDateTime;

public class SSWAPService extends MapsTo {

	private RIG rigGraph;
	private HashMap<String, String> subjectHashMap = new HashMap<String, String>();
	private SSWAPObject object;
	private SSWAPSubject subject;
	private int objectCount = 0;

	String filter = "";
	Integer duration = 0;
	Integer shift = null;
	List<BindingSet> selectList = null;
	List<String> bindingNames = null;

	/**
	 * Types and predicates created in the Resource Invocation Graph (RIG) document.
	 *
	 * @param rig document within which to get/create the types and predicates
	 */
	@Override
	protected void initializeRequest(RIG rig) {
		// if we need to check service parameters we could start here
		System.out.println("--- in service...");
		rigGraph = rig;
	}


	@Override
	protected void mapsTo(SSWAPSubject translatedSubject) throws Exception {		

		object = translatedSubject.getObject();
		subject = translatedSubject;

		Iterator<SSWAPProperty> iterator = translatedSubject.getProperties().iterator();

		while (iterator.hasNext()) {
			SSWAPProperty property = iterator.next();
			if (property.getValue().isLiteral()) {
				SSWAPPredicate predicate = rigGraph.getPredicate(property.getURI());
				String lookupValue = getStrValue(translatedSubject,predicate);
				if (lookupValue != null && !lookupValue.equals("")) addFilter(getStrName(property.getURI()),lookupValue);
			} else if (property.getValue().isIndividual()) {
				SSWAPIndividual individual = property.getValue().asIndividual();
				Iterator<SSWAPProperty> indIterator = individual.getProperties().iterator();
				while (indIterator.hasNext()) {
					SSWAPProperty indProperty = indIterator.next();
					if(indProperty.getValue().isLiteral()){
						SSWAPPredicate predicate = rigGraph.getPredicate(indProperty.getURI());
						String lookupValue = getStrValue(individual,predicate);
						if (lookupValue != null && !lookupValue.equals("")) addFilter(getStrName(indProperty.getURI()),lookupValue);
					}else if(indProperty.getValue().isIndividual()){
						// we suppose there are no individuals in nested property
						System.out.println("Nested property value is Individual:");
					}else{System.out.println("Nested property value is ???");}
				}
			}
		}
		if (!filter.equals("")) {
			filter = "FILTER("+filter + ")";
		}
		doSparql(filter);
		doServiceLogic();
	}

	/**
	 * Imitates logic of the service. Converts values of request properties to the values of result properties. The case of 2 results (objects).
	 */
	public void doServiceLogic() {
		objectCount = 0;
		for (BindingSet cottage:selectList) {
			String end = cottage.getValue("cottage_available_endDate").toString().substring(1,20);
			String start = cottage.getValue("cottage_available_startDate").toString().substring(1,20);
			Calendar endDate = parseDateTime(end);
			Calendar startDate = parseDateTime(start);
			Long days = TimeUnit.MILLISECONDS.toDays(Math.abs(endDate.getTimeInMillis() - startDate.getTimeInMillis()));
			if (days < duration) {
				continue;
			} else {
				objectCount = objectCount + 1;

				Iterator<SSWAPProperty> iterator = object.getProperties().iterator();

				/**
				 * Creating new empty object result for next object
				 */
				SSWAPObject sswapObject = null;
				sswapObject = assignObject(subject);

				while (iterator.hasNext()) {
					SSWAPProperty property = iterator.next();
					if (property.getValue().isLiteral()){
						SSWAPPredicate predicate = rigGraph.getPredicate(property.getURI());
						String lookupname = getStrName(property.getURI());
						String lookupvalue = cottage.getValue(lookupname).toString();
						if (lookupvalue!=null && !lookupvalue.equals("")){
							object.setProperty(predicate, lookupvalue);
						}

						sswapObject.addProperty(predicate, "");

					} else if (property.getValue().isIndividual()){
						SSWAPIndividual individual = property.getValue().asIndividual();
						Iterator<SSWAPProperty> indIterator = individual.getProperties().iterator();

						SSWAPIndividual indEmpty = rigGraph.createIndividual();
						indEmpty.addType(individual.getType());

						while (indIterator.hasNext()) {
							SSWAPProperty indProperty = indIterator.next();
							if (indProperty.getValue().isLiteral()){
								SSWAPPredicate predicate = rigGraph.getPredicate(indProperty.getURI());
								String lookupname = getStrName(indProperty.getURI());
								String lookupvalue = cottage.getValue(lookupname).toString();
								if (lookupvalue!=null && !lookupvalue.equals("")){
									individual.setProperty(predicate, lookupvalue);
								}

								indEmpty.addProperty(predicate,"");

							} else if(indProperty.getValue().isIndividual()){
								// we suppose there are no individuals in nested property
								System.out.println("Nested property value is Individual:");
							}else{System.out.println("Nested property value is ???");}
						}

						SSWAPPredicate indEmptyPredicate = rigGraph.getPredicate(individual.getURI());
						sswapObject.addProperty(indEmptyPredicate,indEmpty);
					}
				}

				if (objectCount>=3)
					break;
				object = sswapObject;
				subject.addObject(object);
			}
		}
	}

	public void doSparql(String filter){
		String sparqlString = "PREFIX bookingServiceOntology:<http://localhost:9090/SSWAPCottageService/resources/bookingCottage.owl#>\n" +
				"SELECT ?booker_name ?booking_number ?cottage_address ?cottage_image ?cottage_places ?cottage_bedrooms ?cottage_lake_distance ?cottage_nearest_city ?cottage_city_distance ?cottage_available_startDate ?cottage_available_endDate\n" +
				"WHERE { ?booking bookingServiceOntology:booker_name ?booker_name. ?booking bookingServiceOntology:booking_number ?booking_number.\n" +
				"?booking bookingServiceOntology:hasBooked ?cottage. ?cottage bookingServiceOntology:cottage_address ?cottage_address. \n" +
				"?cottage bookingServiceOntology:cottage_bedrooms ?cottage_bedrooms. ?cottage bookingServiceOntology:cottage_nearest_city ?cottage_nearest_city. \n" +
				"?cottage bookingServiceOntology:cottage_city_distance ?cottage_city_distance. ?cottage bookingServiceOntology:cottage_lake_distance ?cottage_lake_distance. \n" +
				"?cottage bookingServiceOntology:cottage_places ?cottage_places. ?cottage bookingServiceOntology:cottage_image ?cottage_image.\n" +
				"?cottage bookingServiceOntology:cottage_available_startDate ?cottage_available_startDate. ?cottage bookingServiceOntology:cottage_available_endDate ?cottage_available_endDate. \n" +
				filter+ "} ORDER BY(?cottage_lake_distance)";

		String rdf4jServer = "http://localhost:8080/rdf4j-server/";
		String repositoryID = "ties4371";
		Repository repo = new HTTPRepository(rdf4jServer, repositoryID);
		repo.initialize();

		try (RepositoryConnection conn = repo.getConnection()) {
			try {
				TupleQuery tupleQuery = conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlString);
				try (TupleQueryResult result = tupleQuery.evaluate()){
					selectList = QueryResults.asList(result);
					bindingNames = result.getBindingNames();
				} finally {
					// TODO: handle finally clause
				}
			} finally {
				conn.close();
			}
		} catch (RDF4JException e) {
			// TODO: handle exception
		}
	}

	/*add sparql filter according to name and value*/
	public void addFilter(String name, String value) {
		if (name.equals("booker_name")){
			filter = filter + "regex (?booker_name,\""+value+"\") && ";
		} else if (name.equals("cottage_places")) {
			filter = filter + "?cottage_places >= "+value+" && ";
		} else if (name.equals("cottage_bedrooms")) {
			filter = filter + "?cottage_bedrooms >= "+value+" && ";
		} else if (name.equals("cottage_lake_distance")) {
			filter = filter + "?cottage_lake_distance <= "+value+" && ";
		} else if (name.equals("cottage_nearest_city")) {
			filter = filter + "regex (?cottage_nearest_city,\""+value+"\") && ";
		} else if (name.equals("cottage_city_distance")) {
			filter = filter + "?cottage_city_distance <= "+value+" && ";
		} else if (name.equals("booking_startDate")) {
			if (shift == null)
				filter = filter + "xsd:dateTime(?cottage_available_startDate) == xsd:dateTime(\""+value+"\")";
			else{
				Calendar start1 = parseDateTime(value);
				start1.add(Calendar.DATE,-shift);
				Calendar start2 = parseDateTime(value);
				start2.add(Calendar.DATE,shift);
				filter = filter + "xsd:dateTime(?cottage_available_startDate) >= xsd:dateTime(\""+start1.toString()+"\") && " +
						"xsd:dateTime(?cottage_available_startDate) <= xsd:dateTime(\""+start2.toString()+"\") && ";
			}
		} else if (name.equals("duration")) {
			duration = Integer.valueOf(value); // rdf4j does not support datetime +/- duration
		} else if (name.equals("shift_days")) {
			shift = Integer.valueOf(value);
		}
	}

	/**
	 * Returns the string value for a property instance on an individual.
	 * If more than one property instance exists, only one is (arbitrarily) chosen.
	 * 
	 * @param sswapIndividual the individual with the property
	 * @param sswapPredicate the URI identifying the property (predicate)
	 * @return the value as a string; null on any failure
	 */
	private String getStrValue(SSWAPIndividual sswapIndividual, SSWAPPredicate sswapPredicate) {

		String value = null;
		SSWAPProperty sswapProperty = sswapIndividual.getProperty(sswapPredicate);

		if ( sswapProperty != null ) {
			value = sswapProperty.getValue().asString();

			if ( value == null) {
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

	
	
	
	
	
	
	
	
}


package fi.jyu.Server;


import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;

import info.sswap.api.model.RIG;
import info.sswap.api.model.SSWAPIndividual;
import info.sswap.api.model.SSWAPObject;
import info.sswap.api.model.SSWAPPredicate;
import info.sswap.api.model.SSWAPProperty;
import info.sswap.api.model.SSWAPSubject;
import info.sswap.api.servlet.MapsTo;

public class SSWAPService extends MapsTo {

	private RIG rigGraph;
	private HashMap<String, String> subjectHashMap = new HashMap<String, String>();
	private SSWAPObject object;
	private SSWAPSubject subject;
	private int objectCount = 0;

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

			SSWAPPredicate predicate = rigGraph.getPredicate(property.getURI());
			String lookupValue = getStrValue(translatedSubject,predicate);

			if (lookupValue == null) lookupValue = "";
			subjectHashMap.put(getStrName(property.getURI()), lookupValue);
		}

		doServiceLogic();
	}


	/**
	 * Sets an object of a property
	 * @param var the name of the value
	 * @param node the value
	 */
	public void setObjectProperty(String objProperty, String objValue) {	
		Iterator<SSWAPProperty> iterator = object.getProperties().iterator();
		
		while (iterator.hasNext()) {
			SSWAPProperty property = iterator.next();
			SSWAPPredicate predicate = rigGraph.getPredicate(property.getURI());
			if (getStrName(property.getURI()).equals(objProperty)) {
				object.setProperty(predicate, objValue);
				break;
			}
		}
	}


	/**
	 * Imitates logic of the service. Converts values of request properties to the values of result properties. The case of 2 results (objects).
	 */
	public void doServiceLogic() {
		
		
		objectCount = 1;
		
//		for (int i=0; i<3; i++) {
//			String resultProperty = "resultProperty_"+(i+1);
//			String resultValue = subjectHashMap.get("requestProperty_"+(i+1))+" - converted to result (object-"+objectCount+")";
//			setObjectProperty(resultProperty, resultValue);
//		}
		
		String booking_startDate = "booking_startDate";
		String result_booking_startDate = subjectHashMap.get("booking_startDate")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(booking_startDate, result_booking_startDate);
		
		String booking_endDate = "booking_endDate";
		String result_booking_endDate = subjectHashMap.get("booking_endDate")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(booking_endDate, result_booking_endDate);
		
		String booker_name = "booker_name";
		String result_booker_name = subjectHashMap.get("booker_name")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(booker_name, result_booker_name);
		
		String booking_number = "booking_number";
		String result_booking_number = subjectHashMap.get("booking_number")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(booking_number, result_booking_number);
		
		String cottage_address = "cottage_address";
		String result_cottage_address = subjectHashMap.get("cottage_address")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_address, result_cottage_address);
		
		String cottage_bedrooms = "cottage_bedrooms";
		String result_cottage_bedrooms = subjectHashMap.get("cottage_bedrooms")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_bedrooms, result_cottage_bedrooms);
		
		String cottage_nearest_city = "cottage_nearest_city";
		String result_cottage_nearest_city = subjectHashMap.get("cottage_nearest_city")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_nearest_city, result_cottage_nearest_city);
		
		String cottage_city_distance = "cottage_city_distance";
		String result_cottage_city_distance = subjectHashMap.get("cottage_city_distance")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_city_distance, result_cottage_city_distance);
		
		String cottage_image = "cottage_image";
		String result_cottage_image = subjectHashMap.get("cottage_image")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_image, result_cottage_image);
		
		String cottage_lake_distance = "cottage_lake_distance";
		String result_cottage_lake_distance = subjectHashMap.get("cottage_lake_distance")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_lake_distance, result_cottage_lake_distance);
		
		
		objectCount = 2;
	
		System.out.println("---set first Object...");
	/**
	 * Creating new empty object result...
	 */    
		SSWAPObject sswapObject = null;
        sswapObject = assignObject(subject);
        Iterator<SSWAPProperty> iterator = object.getProperties().iterator();
        while (iterator.hasNext()) {
			SSWAPProperty property = iterator.next();
			SSWAPPredicate predicate = rigGraph.getPredicate(property.getURI());
			sswapObject.addProperty(predicate, "");
		}
        object = sswapObject;
		subject.addObject(object);
        
		System.out.println("---added new Object...");
		
		
//		for (int i=0; i<3; i++) {
//			String resultProperty = "resultProperty_"+(i+1);
//			String resultValue = subjectHashMap.get("requestProperty_"+(i+1))+" - converted to result (object-"+objectCount+")";
//			setObjectProperty(resultProperty, resultValue);
//		}
		
		String booking_startDate2 = "booking_startDate";
		String result_booking_startDate2 = subjectHashMap.get("booking_startDate")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(booking_startDate2, result_booking_startDate2);
		
		String booking_endDate2 = "booking_endDate";
		String result_booking_endDate2 = subjectHashMap.get("booking_endDate")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(booking_endDate2, result_booking_endDate2);
		
		String booker_name2 = "booker_name";
		String result_booker_name2 = subjectHashMap.get("booker_name")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(booker_name2, result_booker_name2);
		
		String booking_number2 = "booking_number";
		String result_booking_number2 = subjectHashMap.get("booking_number")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(booking_number2, result_booking_number2);
		
		String cottage_address2 = "cottage_address";
		String result_cottage_address2 = subjectHashMap.get("cottage_address")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_address2, result_cottage_address2);
		
		String cottage_bedrooms2 = "cottage_bedrooms";
		String result_cottage_bedrooms2 = subjectHashMap.get("cottage_bedrooms")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_bedrooms2, result_cottage_bedrooms2);
		
		String cottage_nearest_city2 = "cottage_nearest_city";
		String result_cottage_nearest_city2 = subjectHashMap.get("cottage_nearest_city")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_nearest_city2, result_cottage_nearest_city2);
		
		String cottage_city_distance2 = "cottage_city_distance";
		String result_cottage_city_distance2 = subjectHashMap.get("cottage_city_distance")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_city_distance2, result_cottage_city_distance2);
		
		String cottage_image2 = "cottage_image";
		String result_cottage_image2 = subjectHashMap.get("cottage_image")+ " - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_image2, result_cottage_image2);
		
		String cottage_lake_distance2 = "cottage_lake_distance";
		String result_cottage_lake_distance2 = subjectHashMap.get("cottage_lake_distance")+" - converted to result (object-"+objectCount+")";
		setObjectProperty(cottage_lake_distance2, result_cottage_lake_distance2);
		
		
		System.out.println("---set second Object...");
	}
	
	

	/**
	 * Returns the string value for a property instance on an individual.
	 * If more than one property instance exists, only one is (arbitrarily) chosen.
	 * 
	 * @param sswapIndividual the individual with the property
	 * @param propertyURI the URI identifying the property (predicate)
	 * @return the value as a string; null on any failure
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

	
	
	
	
	
	
	
	
}


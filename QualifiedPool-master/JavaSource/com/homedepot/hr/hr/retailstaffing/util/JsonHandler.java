package com.homedepot.hr.hr.retailstaffing.util;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: XmlHandler.java
 * Application: RetailStaffing
 */
import com.homedepot.hr.hr.retailstaffing.dto.request.QualifiedPoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ErrorDetails;
import com.homedepot.hr.hr.retailstaffing.dto.response.QualifiedPoolResponse;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * This class uses the XStream API to marshal objects to JSON and unmarshal
 * JSON into transfer objects.
 * 
 * @author mts1876
 */
public class JsonHandler
{	
	// XStream handler
	private static final XStream mToHandler;
	private static final XStream mFromHandler;
	
	// static block that initializes the handler and registers classes that will need to be managed by the handler
	static
	{
		// initialize the handler
		mToHandler = new XStream(new JsonHierarchicalStreamDriver());
		mFromHandler = new XStream(new JettisonMappedXmlDriver());
		
		// indicate which classes should be marshalled/unmashalled using annotations
		mToHandler.processAnnotations(ErrorDetails.class);
		mToHandler.processAnnotations(QualifiedPoolRequest.class);
		mToHandler.processAnnotations(QualifiedPoolResponse.class);
		
		mFromHandler.processAnnotations(QualifiedPoolRequest.class);
	} // end static block
	
	/**
	 * This method marshals the object provided into an JSON string
	 * 
	 * @param object				The object to marshal
	 * 
	 * @return						String representation of the object provided or an empty string
	 * 								if the object provided is null
	 */
	public static String toJson(Object object)
	{
		return ((object == null ? "" : mToHandler.toXML(object)));
	} // end function toJson()
	
	/**
	 * This method unmarshals an JSON string into an Object
	 * 
	 * @param json					String to unmarshal
	 * 
	 * @return						Object generated from the JSON string provided, or null
	 * 								if the JSON string provided was null or empty
	 */
	public static Object fromJson(String json)
	{
		return ((json == null || json.trim().length() == 0 ? null : mFromHandler.fromXML(json)));
	} // end function fromJson
} // end class JsonHandler
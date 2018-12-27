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
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

/**
 * This class uses the XStream API to marshal objects to XML and unmarshal
 * XML into transfer objects.
 * 
 * @author rlp05
 */
public class XmlHandler
{
	private static final String ENCODING = "UTF-8";
	
	// XStream handler
	private static final XStream mHandler;
	
	// static block that initializes the handler and registers classes that will need to be managed by the handler
	static
	{
		// initialize the handler
		mHandler = new XStream(new DomDriver(ENCODING));
		
		// indicate which classes should be marshalled/unmashalled using annotations
		mHandler.processAnnotations(ErrorDetails.class);
		mHandler.processAnnotations(QualifiedPoolRequest.class);
		mHandler.processAnnotations(QualifiedPoolResponse.class);
	} // end static block
	
	/**
	 * This method marshals the object provided into an XML string
	 * 
	 * @param object				The object to marshal
	 * 
	 * @return						String representation of the object provided or an empty string
	 * 								if the object provided is null
	 */
	public static String toXml(Object object)
	{
		return ((object == null ? "" : mHandler.toXML(object)));
	} // end function toXml()
	
	/**
	 * This method unmarshals an XML string into an Object
	 * 
	 * @param xml					String to unmarshal
	 * 
	 * @return						Object generated from the XML string provided, or null
	 * 								if the XML string provided was null or empty
	 */
	public static Object fromXml(String xml)
	{
		return ((xml == null || xml.trim().length() == 0 ? null : mHandler.fromXML(xml)));
	} // end function fromXml
} // end class XMLHandler
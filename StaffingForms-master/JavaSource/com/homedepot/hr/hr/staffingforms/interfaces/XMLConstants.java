package com.homedepot.hr.hr.staffingforms.interfaces;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: XMLConstants.java
 * Application: RetailStaffing
 */

/**
 * This interface contains SHARED constants used by different XML components of the application 
 */
public interface XMLConstants
{	
	public static final String CDATA_START_TAG = "<![CDATA[" + System.getProperty("line.seperator");
	public static final String CDATA_END_TAG = "]]>" + System.getProperty("line.seperator");
	
	/*
	 * Some of the service methods in this application have clients that are
	 * using JAXB to un-marshal the XML into a objects. In order for these
	 * clients to function correctly, namespaces are required in the response
	 * XML. In order to "trick" the XStream API to set the namespaces, certain
	 * classes have had the following variables defined (with values). When
	 * these classes are loaded into the XStream handler, the handler will be
	 * instructed to treat these variables as attributes of the class instead of
	 * elements (and in doing so will generate the XML with namespaces).
	 */
	public static final String DFLT_NAMESPACE_VAR = "mDefaultNamespace";
	public static final String XML_NAMESPACE_VAR = "mXmlSchemaNamespace";
	public static final String SCHEMA_LOCATION_VAR = "mSchemaLocation";	
} // end interface XMLConstants
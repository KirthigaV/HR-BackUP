package com.homedepot.hr.hr.retailstaffing.dto.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: VDNDetailsResponse.java
 * Application: RetailStaffing
 */
import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to transport the response from looking up vector destination number. This
 * response is used by clients of the inbound phone screen service methods being 
 * developed for RSA release 4.0
 */
@XStreamAlias("Response")
public class VDNDetailsResponse implements Serializable
{
    private static final long serialVersionUID = -5549209861942426274L;

	// XML default namespace (needed because some of the clients are using JAXB to un-marshal the XML into objects)
	@XStreamAlias("xmlns")
	private String mDefaultNamespace = "http://webapps.homedepot.com/RetailStaffing";
	// XML schema instance namespace (needed because some of the clients are using JAXB to un-marshal the XML into objects)
	@XStreamAlias("xmlns:xsi")
	private String mXmlSchemaNamespace = "http://www.w3.org/2001/XMLSchema-instance";
	// XML schema location namespace (needed because some of the clients are using JAXB to un-marshal the XML into objects)
	@XStreamAlias("xsi:schemaLocation")
	private String mSchemaLocation = "http://webapps.homedepot.com/RetailStaffing/RetailStaffingServices.xsd";
		
	// Error details object
	@XStreamAlias("error")
	private ErrorTO mError;
	
	// Vector destination number. This is defined as a String instead of as an int because i want to be able to not transmit it in the response
	@XStreamAlias("vdnId")
	private String mVdn;

	/**
	 * Get the default namespace
	 * 
	 * @return				The default namespace
	 */
    public String getDefaultNamespace()
    {
    	return mDefaultNamespace;
    } // end function getDefaultNamespace()
    
    /**
     * Set the default namespace
     * 
     * @param namespace		The default namespace
     */
    public void setDefaultNamespace(String namespace)
    {
    	mDefaultNamespace = namespace;
    } // end function setDefaultNamespace()
    
    /**
     * Get the XML schema instance namespace
     * 
     * @return				The XML schema instance namespace
     */
    public String getXmlSchemaNamespace()
    {
    	return mXmlSchemaNamespace;
    } // end function getXmlSchemaNamespace()
    
    /**
     * Set the XML schema instance namespace
     * 
     * @param namespace		The XML schema instance namespace
     */
    public void setXmlSchemaNamespace(String namespace)
    {
    	mXmlSchemaNamespace = namespace;
    } // end function setXmlSchemaNamespace()   
    
    /**
     * Get the schema location
     * 
     * @return				The schema location
     */
    public String getSchemaLocation()
    {
    	return mSchemaLocation;
    } // end function getSchemaLocation()
    
    /**
     * Set the schema location 
     * 
     * @param schemaLoc		The schema location
     */
    public void setSchemaLocation(String schemaLoc)
    {
    	mSchemaLocation = schemaLoc;
    } // end function setSchemaLocation()
	
    /**
     * Get the error details object
     * 
     * @param error			The error details object
     */
	public void setError(ErrorTO error)
	{
		mError = error;		
	} // end function setError()
	
	/**
	 * Set the error details object
	 * 
	 * @return				The error details object
	 */
	public ErrorTO getError()
	{
		return mError;
	} // end function getError()
	
	/**
	 * Get the vector destination number
	 * 
	 * @return				The vector destination number
	 */
	public String getVdn()
	{
		return mVdn;
	} // end function getVdn()
	
	/**
	 * Set the vector destination number
	 * 
	 * @param vdn			The vector destination number
	 */
	public void setVdn(String vdn)
	{
		mVdn = vdn;
	} // end function setVdn()
} // end class VDNDetailsResponse()
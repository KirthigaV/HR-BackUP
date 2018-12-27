package com.homedepot.hr.hr.retailstaffing.dto.response;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: UpdateResponse.java
 * Application: RetailStaffing
 */

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Used to transport the response from updating one of the RSA object statuses. This 
 * response is used by clients of the inbound phone screen service methods being developed
 * for RSA release 4.0
 */
@XStreamAlias("updateResponse")
public class UpdateResponse implements Serializable
{
    private static final long serialVersionUID = -2658161527571098365L;
    
	private static final int VERSION0 = 0;    
	private static final String FALSE = "false";

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
	
	// if the record was updated or not
	@XStreamAlias("updated")
	private String mUpdated;
	
    // indicates the version of the XML to generate
    @XStreamOmitField
    private int mVersion;	
	
    public UpdateResponse(int version)
    {
    	mVersion = version;
    	
    	// if this is a version 0 response object, null out the namespace variables
    	if(mVersion == VERSION0)
    	{
    		mDefaultNamespace = null;
    		mXmlSchemaNamespace = null;
    		mSchemaLocation = null;
    	} // end if(mVersion == VERSION0)
    } // end constructor
    
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
		
		// if this is a version 0 object, also set hte updated value to false
		if(mVersion == VERSION0)
		{
			setUpdated(FALSE);
		} // end if(mVersion == VERSION0)
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
	 * Get the results of the update 
	 * 
	 * @return				The results of the update
	 */
	public String getUpdated()
	{
		return mUpdated;
	} // end function setUpdated()
	
	/**
	 * Set the results of the update
	 * 
	 * @param updated		The results of the update
	 */
	public void setUpdated(String updated)
	{
		mUpdated = updated;
	} // end function setUpdated()

    /**
     * Get the version of the XML that will be produced by this object
     * 
     * @return				The version of the XML that will be produced by this object
     */
    public int getVersion()
    {
    	return mVersion;
    } // end function getVersion()
} // end class UpdateResponse
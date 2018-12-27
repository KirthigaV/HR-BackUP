package com.homedepot.hr.hr.retailstaffing.dto.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: IntrvwTimePrefResponse.java
 * Application: RetailStaffing
 *
 */
import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to transport response details to clients of the inbound 
 * scheduling service being developed for RSA release 5.0
 */
@XStreamAlias("interviewTimePreferenceResponse")
public class IntrvwTimePrefResponse implements Serializable
{
    private static final long serialVersionUID = -7598721753164864465L;

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
	
	// interview date
	@XStreamAlias("interviewDate")
	private String mIntrvwDate;
	
	// interview available time (AM/PM/BOTH)
	@XStreamAlias("interviewAvailableTime")
	private String mIntrvwAvailTime;
    
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
	 * Get the interview date
	 * 
	 * @return				The interview date
	 */
	public String getIntrvwDate()
	{
		return mIntrvwDate;
	} // end function getIntrvwDate()
	
	/**
	 * Set the interview date
	 * 
	 * @param date			The interview date
	 */
	public void setIntrvwDate(String date)
	{
		mIntrvwDate = date;
	} // end function setIntrvwDate()
	
	/**
	 * Get the interview available time
	 * 
	 * @return				The interview available time
	 */
	public String getIntrvwAvailTime()
	{
		return mIntrvwAvailTime;
	} // end function getIntrvwAvailTime()
	
	/**
	 * Set the interview available time
	 * 
	 * @param availTime		The interview available time
	 */
	public void setIntrvwAvailTime(String availTime)
	{
		mIntrvwAvailTime = availTime;
	} // end function setIntrvwAvailTime()
} // end classIntrvwTimePrefResponse
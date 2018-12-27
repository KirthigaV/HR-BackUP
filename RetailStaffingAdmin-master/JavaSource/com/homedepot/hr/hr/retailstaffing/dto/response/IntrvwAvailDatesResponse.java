package com.homedepot.hr.hr.retailstaffing.dto.response;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: IntrvwAvailDatesResponse.java
 * Application: RetailStaffing
 *
 */
import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDateList;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to transport response details to clients of the inbound 
 * scheduling service being developed for RSA release 5.0
 */
@XStreamAlias("interviewAvailableDatesResponse")
public class IntrvwAvailDatesResponse implements Serializable
{
    private static final long serialVersionUID = -411232248180063061L;

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
	
    // phone screen id
    @XStreamAlias("confirmationNo")
    private String mPhnScrnId;
    
    // list of dates available for this phone screen.
    @XStreamAlias("interviewAvailDateList")
    private IntrvwAvailDateList mAvailDates;
    
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
     * Get the phone screen id
     * 
     * @return				The phone screen id
     */
    public String getPhnScrnId()
    {
    	return mPhnScrnId;
    } // end function getPhnScrnId()
    
    /**
     * Set the phone screen id
     * 
     * @param phnScrnId		The phone screen id
     */
    public void setPhnScrnId(String phnScrnId)
    {
    	mPhnScrnId = phnScrnId;
    } // end function setPhnScrnId()	
    
    /**
     * Get the available dates for this phone screen
     * 
     * @return				List of available dates for the phone screen provided or null if no 
     * 						dates are available.
     */
    public List<String> getAvailDates()
    {
    	List<String> dates = null;
    	
    	if(mAvailDates != null)
    	{
    		dates = mAvailDates.getDates();
    	} // end if(mAvailDates != null)
    	
    	return dates;
    } // end function getAvailDates()
    
    /**
     * Set the available dates for this phone screen
     * 
     * @param dates			List of available dates for the phone screen provided
     */
    public void setAvailDates(List<String> dates)
    {
    	if(mAvailDates == null)
    	{
    		mAvailDates = new IntrvwAvailDateList();
    	} // end if(mAvailDates == null)
    	
    	mAvailDates.setDates(dates);
    } // end function setOfferedDates()    
    
    /**
     * Add the available date
     * 
     * @param date			Date to add
     */
    public void addAvailDate(String date)
    {
    	// if it hasn't already been initialized, do so
    	if(mAvailDates == null)
    	{
    		mAvailDates = new IntrvwAvailDateList();
    	} // end if(mAvailDates == null)
    	
    	mAvailDates.addDate(date);
    } // end function 
} // end class IntrvwAvailDatesResponse
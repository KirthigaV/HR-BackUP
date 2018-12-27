/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: InterviewAvailResponse.java
 */

package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.InterviewAvaliableSlotsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDate;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("interviewAvailResponse")
public class InterviewAvailResponse implements Serializable
{
	/*
	 * TODO : Ideally, every web service method that uses this request object
	 * would expect the same XML, but that is not the case here. So some of the following
	 * variables are used by one web service method and the others are used by a
	 * different web service method. This is not being split out into a different
	 * class because we're using XStream to marshall/un-marshall the XML and both
	 * of the methods expects the same root element "interviewAvailResponse"
	 */	
	private static final long serialVersionUID = 362498820763181265L;
	private static final int VERSION0 = 0;
	
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

	@XStreamAlias("phoneScreenId")
	private String mPhoneScreenId;

	@XStreamAlias("interviewDate")
	private String mInterviewDate;

	@XStreamAlias("beginTime")
	private String mBeginTime;

	@XStreamAlias("endTime")
	private String mEndTime;

	@XStreamAlias("interviewAvailList")
	private List<InterviewAvaliableSlotsTO> interviewAvailDetailsList;
	
	@XStreamAlias("preferredTime")
	private String mPrefTime;
	
	@XStreamAlias("preferredDate")
	private String mPrefDate;
	
	@XStreamAlias("interviewAvailDateList")
	private List<IntrvwAvailDate> mAvailDates;
	
	public InterviewAvailResponse()
	{
		this(VERSION0);
	} // end constructor()
	
	public InterviewAvailResponse(int version)
	{
		if(version == VERSION0)
		{
			mDefaultNamespace = null;
			mXmlSchemaNamespace = null;
			mSchemaLocation = null;
		} // end if(version == VERSION0)
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

	public String getPhoneScreenId()
	{
		return mPhoneScreenId;
	}

	public void setPhoneScreenId(String phoneScreenId)
	{
		mPhoneScreenId = phoneScreenId;
	}

	public String getInterviewDate()
	{
		return mInterviewDate;
	}

	public void setBeginTime(String beginTime)
	{
		mBeginTime = beginTime;
	}

	public String getBeginTime()
	{
		return mBeginTime;
	}

	public void setEndTime(String endTime)
	{
		mEndTime = endTime;
	}

	public String getEndTime()
	{
		return mEndTime;
	}

	public void setInterviewDate(String interviewDate)
	{
		mInterviewDate = interviewDate;
	}

	public List<InterviewAvaliableSlotsTO> getInterviewAvailDetailsList()
	{
		return interviewAvailDetailsList;
	}

	public void setInterviewAvailDetailsList(List<InterviewAvaliableSlotsTO> interviewAvailDetailsList)
	{
		this.interviewAvailDetailsList = interviewAvailDetailsList;
	}

	public String getPrefTime()
	{
		return mPrefTime;
	} // end function getPrefTime()
	
	public void setPrefTime(String prefTime)
	{
		mPrefTime = prefTime;
	} // end function setPrefTime()
	
	public String getPrefDate()
	{
		return mPrefDate;
	} // end function getPrefDate()
	
	public void setPrefDate(String prefDate)
	{
		mPrefDate = prefDate;
	} // end function setPrefDate()
	
	public List<IntrvwAvailDate> getAvailDates()
	{
		return mAvailDates;
	} // end function getAvailDates()
	
	public void setAvailDates(List<IntrvwAvailDate> dates)
	{
		mAvailDates = dates;
	} // end function setAvailDates()
	
	public void addAvailDate(IntrvwAvailDate date)
	{
		if(mAvailDates == null)
		{
			mAvailDates = new ArrayList<IntrvwAvailDate>();
		} // end if(mAvailDates == null)
		
		mAvailDates.add(date);
	} // end function addAvailDate()
} // end class InterviewAvailResponse()

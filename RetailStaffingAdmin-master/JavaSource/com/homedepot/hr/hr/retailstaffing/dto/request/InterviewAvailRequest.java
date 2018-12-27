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
 * File Name: InterviewAvailRequest.java
 */

package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.homedepot.hr.hr.retailstaffing.dto.InterviewAvaliableSlotsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDate;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("interviewAvailRequest")
public class InterviewAvailRequest implements Serializable
{
    private static final long serialVersionUID = 3969657418511936079L;

	// the phone screen id to get interview availability for
	@XStreamAlias("phoneScreenId")
	private String mPhoneScreenId;
	
	// the time preference (AM/PM) 
	@XStreamAlias("preferredTime")
	private String mTimePref;
	// the preferred interview date
	@XStreamAlias("preferredDate")
	private String mPrefDate;	

	/*
	 * TODO : Ideally, every web service method that uses this request object
	 * would expect the same XML, but that is not the case here. So one of the following
	 * variables is used by one web service method and the other is used by a
	 * different web service method. This is not being split out into a different
	 * class because we're using XStream to marshall/un-marshall the XML and both
	 * of the methods expects the same root element "interviewAvailRequest"
	 */
	
	// used by the outbound service method
	@XStreamAlias("interviewAvailList")
	private List<InterviewAvaliableSlotsTO> mIntrvwAvailDetailsTOList;

	// list of available interview slots details
	@XStreamAlias("interviewAvailDateList")
	// used by the inbound service method
	private List<IntrvwAvailDate> mIntrvwAvailDates;
	
	/**
	 * Get the phone screen id
	 * 
	 * @return					The phone screen id
	 */
	public String getPhoneScreenId()
	{
		return mPhoneScreenId;
	} // end function getPhoneScreenId()
	
	/**
	 * Set the phone screen id
	 * 
	 * @param phoneScreenId		The phone screen id
	 */
	public void setPhoneScreenId(String phoneScreenId)
	{
		mPhoneScreenId = phoneScreenId;
	} // end function setPhoneScreenId()

	/**
	 * Get the time preference (AM/PM) 
	 * 
	 * @return					The time preference (AM/PM) 
	 */
	public String getTimePref()
	{
		return mTimePref;
	} // end function getTimePref()
	
	/**
	 * Set the time preference (AM/PM) 
	 * 
	 * @param timePref			The time preference (AM/PM) 
	 */
	public void setTimePref(String timePref)
	{
		mTimePref = timePref;
	} // end function setTimePref()
	
	/**
	 * Get the preferred interview date
	 * 
	 * @return					The preferred interview date
	 */
	public String getPrefDate()
	{
		return mPrefDate;
	} // end function getPrefDate()
	
	/**
	 * Set the preferred interview date
	 * 
	 * @param prefDate			The preferred interview date
	 */
	public void setPrefDate(String prefDate)
	{
		mPrefDate = prefDate;
	} // end function setPrefDate()	
	
	public List<InterviewAvaliableSlotsTO> getInterviewAvailDetailsTOList()
	{
		return mIntrvwAvailDetailsTOList;
	}

	public void setInterviewAvailDetailsTOList(List<InterviewAvaliableSlotsTO> interviewAvailDetailsTOList)
	{
		this.mIntrvwAvailDetailsTOList = interviewAvailDetailsTOList;
	}
	
	/**
	 * Get the list of interview available dates
	 * 
	 * @return					The list of available interview dates
	 */
	public List<IntrvwAvailDate> getIntrvwAvailDates()
	{
		return mIntrvwAvailDates;
	} // end function getIntrvwAvailDates()
	
	/**
	 * Set the list of interview available dates
	 * 
	 * @param dates				The list of interview available dates
	 */
	public void setIntrvwAvailDates(List<IntrvwAvailDate> dates)
	{
		mIntrvwAvailDates = dates;
	} // end function setIntrvwAvailDates()
	
	/**
	 * Add an available interview slot to the list
	 *  
	 * @param date				The available interview slot
	 */
	public void addIntrvwAvailDate(IntrvwAvailDate date)
	{
		if(mIntrvwAvailDates == null)
		{
			mIntrvwAvailDates = new ArrayList<IntrvwAvailDate>();
		} // end if(mIntrvwAvailDates == null)
		
		mIntrvwAvailDates.add(date);
	} // end function addIntrvwAvailDate()
} // end class InterviewAvailRequest

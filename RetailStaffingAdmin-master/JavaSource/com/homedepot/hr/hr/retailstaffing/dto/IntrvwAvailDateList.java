package com.homedepot.hr.hr.retailstaffing.dto;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: IntrvwAvailDateList.java
 * Application: RetailStaffing
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Used to transport request/response interview available dates details to/from clients of 
 * the inbound scheduling service being developed for RSA release 5.0
 */
@XStreamAlias("interviewAvailDateList")
public class IntrvwAvailDateList implements Serializable
{
    private static final long serialVersionUID = 1928800180020768650L;

    /*
     * List of interview dates 
     */
    @XStreamImplicit(itemFieldName="interviewDate")
    private List<String> mDates;
	
    /**
     * Get the list of dates 
     * 
     * @return			List of dates previously offered to the IVR
     */
	public List<String> getDates()
	{
		return mDates;
	} // end function getDates()

	/**
	 * Set the list of dates
	 * 
	 * @param dates		List of dates previously offered to the IVR
	 */
	public void setDates(List<String> dates)
	{
		mDates = dates;
	} // end function setDates()
	
	/**
	 * Add the date provided to the list of dates
	 * 
	 * @param date		Date to add
	 */
	public void addDate(String date)
	{
		if(mDates == null)
		{
			mDates = new ArrayList<String>();
		} // end if(mDates == null)
		
		mDates.add(date);
	} // end function addDate()
} // end class IntrvwAvailDateList
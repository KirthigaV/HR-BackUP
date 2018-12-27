package com.homedepot.hr.hr.retailstaffing.dto.request;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: IntrvwSchedRequest.java
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDate;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transfer object that will be used to store request data provided to the inbound 
 * scheduling service being developed for RSA release 5.0
 */
@XStreamAlias("interviewSchedRequest")
public class IntrvwSchedRequest implements Serializable
{
    private static final long serialVersionUID = -5314430309120974651L;
    
    /**
     * Phone screen id
     */
    @XStreamAlias("phoneScreenId")
    private int mPhnScrnId;
    
    /**
     * Interview schedule slot details that should be scheduled
     */
    @XStreamAlias("schedule")
    private List<IntrvwAvailDate> mScheduleDate;
    
    /**
     * List of interview slot details for interview slots that should
     * be released
     */
    @XStreamAlias("release")
    private AvailDateList mReleaseDates;
    
    /**
     * Get the phone screen id
     * 
     * @return					The phone screen id
     */
    public int getPhnScrnId()
    {
    	return mPhnScrnId;
    } // end function getPhnScrnId()
    
    /**
     * Set the phone screen id
     * 
     * @param phnScrnId			The phone screen id
     */
    public void setPhnScrnId(int phnScrnId)
    {
    	mPhnScrnId = phnScrnId;
    } // end function setPhnScrnId()
    
    /**
     * Get the interview schedule slot details that should be scheduled
     * 
     * @return					The interview slot details that should be scheduled
     */
    public IntrvwAvailDate getScheduleDate()
    {
    	IntrvwAvailDate date = null;
    	
    	if(mScheduleDate != null && !mScheduleDate.isEmpty())
    	{
    		date = mScheduleDate.get(0);
    	} // end if(mScheduleDate != null && !mScheduleDate.isEmpty())
    	
    	return date;
    } // end function getScheduleDate()

    /**
     * Set the interview schedule slot details that should be scheduled
     * 
     * @param date				The interview schedule slot details that should be scheduled
     */
    public void setScheduleDate(IntrvwAvailDate date)
    {
    	if(mScheduleDate == null)
    	{
    		mScheduleDate = new ArrayList<IntrvwAvailDate>();
    	} // end if(mScheduleDate == null)
    	
    	if(mScheduleDate.isEmpty())
    	{
    		mScheduleDate.add(date);
    	} // end if(mScheduleDate.isEmpty())
    	else
    	{
    		mScheduleDate.set(0, date);
    	} // end else
    } // end function setScheduleDate()
    
    /**
     * Get the list of interview schedule slot details that should be released
     * 
     * @return					The list of interview schedule slot details that should be released
     */
    public List<IntrvwAvailDate> getReleaseDates()
    {
    	List<IntrvwAvailDate> dates = null;
    	
    	if(mReleaseDates != null)
    	{
    		dates = mReleaseDates.getDates();
    	} // end if(mReleaseDates != null)
    		
    	return dates;
    } // end function getReleaseDates()
    
    /**
     * Set the list of interview schedule slot details that should be released
     * 
     * @param dates				The list of interview schedule slot details that should be released
     */
    public void setReleaseDates(List<IntrvwAvailDate> dates)
    {
    	if(mReleaseDates == null)
    	{
    		mReleaseDates = new AvailDateList();
    	} // end if(mReleaseDates == null)
    	
    	mReleaseDates.setDates(dates);
    } // end function setReleaseDates()
    
    /**
     * Inner class representing a list of interview schedule slot release dates. This is kind of a waste, but
     * it's required to get the XML generate by the XStream API to produce the format previously communicated
     * to the vendor 
     */
    @XStreamAlias("interviewAvailDateList")
    private static class AvailDateList implements Serializable
    {
        private static final long serialVersionUID = -7494763370697827201L;

        /**
         * List of interview available dates
         */
        @XStreamAlias("interviewAvailDateList")
        private List<IntrvwAvailDate> mDates;
    	
        /**
         * Get the list of interview available dates
         * 
         * @return					The list of interview available dates
         */
    	public List<IntrvwAvailDate> getDates()
    	{
    		return mDates;
    	} // end function getDates()
    	
    	/**
    	 * Set the list of interview available dates
    	 * 
    	 * @param dates				The list of interview available dates
    	 */
    	public void setDates(List<IntrvwAvailDate> dates)
    	{
    		mDates = dates;
    	} // end function setDates()
    } // end class AvailDateList
} // end class IntrvwSchedRequest
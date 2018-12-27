package com.homedepot.hr.hr.retailstaffing.dto.request;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: IntrvwAvailDatesRequest.java
 * Application: RetailStaffing
 *
 */
import java.io.Serializable;

import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDateList;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transfer object that will be used to store request data provided to the inbound 
 * scheduling service being developed for RSA release 5.0
 */
@XStreamAlias("interviewAvailableDatesRequest")
public class IntrvwAvailDatesRequest implements Serializable
{
    private static final long serialVersionUID = -766199414871399546L;

    // phone screen id
    @XStreamAlias("confirmationNo")
    private int mPhnScrnId;
    
    /*
     * A list of dates previously offered to the IVR for this phone screen. If 
     * this is the first call for this phone screen from the IVR then this will
     * be null
     */
    @XStreamAlias("interviewAvailDateList")
    private IntrvwAvailDateList mOfferedDates;
    
    /**
     * Get the phone screen id
     * 
     * @return				The phone screen id
     */
    public int getPhnScrnId()
    {
    	return mPhnScrnId;
    } // end function getPhnScrnId()
    
    /**
     * Set the phone screen id
     * 
     * @param phnScrnId		The phone screen id
     */
    public void setPhnScrnId(int phnScrnId)
    {
    	mPhnScrnId = phnScrnId;
    } // end function setPhnScrnId()
    
    /**
     * Get the dates already offered to the IVR for this phone screen
     * 
     * @return				List of dates previously offered to the IVR or
     * 						null if this is the first request from the IVR
     * 						for this phone screen
     */
    public List<String> getOfferedDates()
    {
    	List<String> dates = null;
    	
    	if(mOfferedDates != null)
    	{
    		dates = mOfferedDates.getDates();
    	} // end if(mOfferedDates != null)
    	
    	return dates;
    } // end function getOfferedDates()
    
    /**
     * Set the dates previously offered to the IVR for this phone screen
     * 
     * @param offeredDates	List of dates previously offered to the IVR for
     * 						this phone screen
     */
    public void setOfferedDates(List<String> offeredDates)
    {
    	if(mOfferedDates == null)
    	{
    		mOfferedDates = new IntrvwAvailDateList();
    	} // end if(mOfferedDates == null)
    	
    	mOfferedDates.setDates(offeredDates);
    } // end function setOfferedDates()
} // end class IntrvwAvailDatesRequest
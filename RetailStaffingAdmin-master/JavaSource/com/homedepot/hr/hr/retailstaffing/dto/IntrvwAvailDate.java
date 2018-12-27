package com.homedepot.hr.hr.retailstaffing.dto;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: IntrvwAvailDate.java
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Transfer object that will be used to transport interview schedule date data
 * used by the inbound interview scheduling services being developed for RSA 
 * release 5.0
 */
@XStreamAlias("interviewAvailDate")
public class IntrvwAvailDate implements Serializable
{
    private static final long serialVersionUID = 5956619629081991068L;

    /**
     * The interview date
     */
    @XStreamAlias("interviewDate")
    private String mIntrvwDt;
    
    /**
     * The interview begin time
     */
    @XStreamAlias("beginTime")
    private String mBgnTm;
    
    /**
     * The interview end time
     */
    @XStreamAlias("endTime")
    private String mEndTm;
    
    /**
     * List of interview slots that make up this interview date
     */
    @XStreamAlias("interviewDetailsList")
    private IntrvwDetailsList mDetailsList;
    
    /**
     * Get the interview date
     * 
     * @return					The interview date
     */
    public String getIntrvwDt()
    {
    	return mIntrvwDt;
    } // end function getIntrvwDt()
    
    /**
     * Set the interview date
     * 
     * @param date				The interview date
     */
    public void setIntrvwDt(String date)
    {
    	mIntrvwDt = date;
    } // end function setIntrvwDt()
    
    /**
     * Get the interview begin time
     * 
     * @return					The interview begin time
     */
    public String getBgnTm()
    {
    	return mBgnTm;
    } // end function getBgnTm()
    
    /**
     * Set the interview begin time
     * 
     * @param bgnTm				The interview begin time
     */
    public void setBgnTm(String bgnTm)
    {
    	mBgnTm = bgnTm;
    } // end function setBgnTm()
    
    /**
     * Get the interview end time
     * 
     * @return					The interview end time
     */
    public String getEndTm()
    {
    	return mEndTm;
    } // end function getEndTm()
    
    /**
     * Set the interview end time
     * 
     * @param endTm				The interview end time
     */
    public void setEndTm(String endTm)
    {
    	mEndTm = endTm;
    } // end function setEndTm()
    
    /**
     * Get the list of interview slots that make up this interview date
     * 
     * @return					The list of interview slots that make up this interview date
     */
    public List<InterviewAvaliableSlotsTO> getIntrvwAvailSlots()
    {
    	List<InterviewAvaliableSlotsTO> slots = null;
    	
    	if(mDetailsList != null)
    	{
    		slots = mDetailsList.getIntrvwSlots();
    	} // end if(mDetailsList != null)
    	
    	return slots;
    } // end function getIntrvwAvailSlots()
    
    /**
     * Set the list of interview slots that make up this interview date
     * 
     * @param slots				The list of interview slots that make up this interview date
     */
    public void setIntrvwAvailSlots(List<InterviewAvaliableSlotsTO> slots)
    {
    	if(mDetailsList == null)
    	{
    		mDetailsList = new IntrvwDetailsList();
    	} // end if(mDetailsList == null)
    	
    	mDetailsList.setIntrvwSlots(slots);
    } // end function setIntrvwAvailSlots()
    
    /**
     * Convenience method to add interview slot details to the list of interview slots that make up this interview date 
     * 
     * @param slot				The slot to add
     */
    public void addIntrvwAvailSlot(InterviewAvaliableSlotsTO slot)
    {
    	if(mDetailsList == null)
    	{
    		mDetailsList = new IntrvwDetailsList();
    	} // end if(mDetailsList == null)
    	
    	mDetailsList.addIntrvwSlot(slot);
    } // end function addIntrvwAvailSlot()
    
    /**
     * Inner class representing a list of interview schedule slots. This is kind of a waste, but
     * it's required to get the XML generate by the XStream API to produce the format previously 
     * communicated to the vendor 
     */
    @XStreamAlias("interviewDetailsList")
    private static class IntrvwDetailsList implements Serializable
    {
        private static final long serialVersionUID = 4478355507747533235L;
    	
        /**
         * The list of interview slots that make up this interview date
         */
        @XStreamImplicit(itemFieldName = "interviewDetails")
        private List<InterviewAvaliableSlotsTO> mIntrvwSlots;
        
        /**
         * Get the list of interview slots that make up this interview date
         * 
         * @return				The list of interview slots that make up this interview date
         */
        public List<InterviewAvaliableSlotsTO> getIntrvwSlots()
        {
        	return mIntrvwSlots;
        } // end function getIntrvwSlots();
        
        /**
         * Set the list of interview slots that make up this interview date
         * 
         * @param slots			The list of interview slots that make up this interview date
         */
        public void setIntrvwSlots(List<InterviewAvaliableSlotsTO> slots)
        {
        	mIntrvwSlots = slots;
        } // end function setIntrvwSlots()
        
        /**
         * Convenience method to add an interview slot to the list of interview slots that make up this interview date
         * 
         * @param slot			The slot to add
         */
        public void addIntrvwSlot(InterviewAvaliableSlotsTO slot)
        {
        	if(mIntrvwSlots == null)
        	{
        		mIntrvwSlots = new ArrayList<InterviewAvaliableSlotsTO>();
        	} // end if(mIntrvwSlots == null)
        	
        	mIntrvwSlots.add(slot);
        } // end function addIntrvwSlot()
    } // end class IntrvwDetailsList
} // end class IntrvwAvailDate

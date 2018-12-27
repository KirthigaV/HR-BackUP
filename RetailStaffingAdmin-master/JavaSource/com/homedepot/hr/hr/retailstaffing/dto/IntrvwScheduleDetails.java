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

/**
 * Used to transport key information used to get interview scheduling details, added as part 
 * of the inbound scheduling service being developed for RSA release 5.0
 */
public class IntrvwScheduleDetails implements Serializable
{
    private static final long serialVersionUID = -4202258990922339720L;
    
    // the phone screen id this details data is for
    private int mPhnScrnId;
    // the store number for the phone screen's requisition
    private String mStoreNbr;
    // candidate id for the phone screen
    private String mEmpltPosnCandId;
    // calendar id for the phone screen's requisition
    private int mReqCalId;
    // interview status code of the phone screen
    private short mIntrvwStatCd;
    // requisition number for the phone screen
    private int mEmpltReqNbr;
    // indicates if the requisition is part of a hiring event
    private String mHireEvntFlg;
    // indicates if the RSC is scheduling interviews for this requisition
    private String mRscSchFlg;
    // required interview duration (in minutes) for this requisition
    private short mIntrvwMins;
    // indicates if the requisition is active or not
    private String mReqActvFlg;
    // indicates if the candidate is active, attached, etc.
    private String mCandActvFlg;
    // indicates hiring event type
    private String mEventType;
    /**
     * Get the phone screen id this details data is for
     * 
     * @return					The phone screen id this details data is for
     */
	public int getPhnScrnId()
    {
    	return mPhnScrnId;
    } // end function getPhnScrnId()
	
	/**
	 * Set the phone screen id this details data is for
	 * 
	 * @param phnScrnId			The phone screen id this details data is for
	 */
	public void setPhnScrnId(int phnScrnId)
    {
    	mPhnScrnId = phnScrnId;
    } // end function setPhnScrnId()
	
	/**
	 * Get the store number for the phone screen's requisition
	 * 
	 * @return					The store number for the phone screen's requisition
	 */
	public String getStoreNbr()
    {
    	return mStoreNbr;
    } // end function getStoreNbr()
	
	/**
	 * Set the store number for the phone screen's requisition
	 * 
	 * @param storeNbr			The store number for the phone screen's requisition
	 */
	public void setStoreNbr(String storeNbr)
    {
    	mStoreNbr = storeNbr;
    } // end function setStoreNbr()
	
	/**
	 * Get the candidate id for the phone screen
	 * 
	 * @return					The candidate id for the phone screen
	 */
	public String getEmpltPosnCandId()
    {
    	return mEmpltPosnCandId;
    } // end function getEmpltPosnCandId()
	
	/**
	 * Set the candidate id for the phone screen
	 * 
	 * @param empltPosnCandId	The candidate id for the phone screen
	 */
	public void setEmpltPosnCandId(String empltPosnCandId)
    {
    	mEmpltPosnCandId = empltPosnCandId;
    } // end function setEmpltPosnCandId()
	
	/**
	 * Get the calendar id for the phone screen's requisition
	 * 
	 * @return					The calendar id for the phone screen's requisition
	 */
	public int getReqCalId()
    {
    	return mReqCalId;
    } // end function getReqCalId()
	
	/**
	 * Set the calendar id for the phone screen's requisition
	 * 
	 * @param reqCalId			The calendar id for the phone screen's requisition
	 */
	public void setReqCalId(int reqCalId)
    {
    	mReqCalId = reqCalId;
    } // end function setReqCalId()
	
	/**
	 * Get the interview status code of the phone screen
	 * 
	 * @return					The interview status code of the phone screen
	 */
	public short getIntrvwStatCd()
    {
    	return mIntrvwStatCd;
    } // end function getIntrvwStatCd()
	
	/**
	 * Set the interview status code of the phone screen
	 * 
	 * @param intrvwStatCd		The interview status code of the phone screen
	 */
	public void setIntrvwStatCd(short intrvwStatCd)
    {
    	mIntrvwStatCd = intrvwStatCd;
    } // end function setIntrvwStatCd()
	
	/**
	 * Get the requisition number for the phone screen
	 * 
	 * @return					The requisition number for the phone screen
	 */
	public int getEmpltReqNbr()
    {
    	return mEmpltReqNbr;
    } // end function getEmpltReqNbr()
	
	/**
	 * Set the requisition number for the phone screen
	 * 
	 * @param empltReqNbr		The requisition number for the phone screen
	 */
	public void setEmpltReqNbr(int empltReqNbr)
    {
    	mEmpltReqNbr = empltReqNbr;
    } // end function setEmpltReqNbr()
	
	/**
	 * Get the hiring event flag for this requisition
	 * 
	 * @return					The hiring event flag for this requisition
	 */
	public String getHireEvntFlg()
    {
    	return mHireEvntFlg;
    } // end function getHireEvntFlg()
	
	/**
	 * Set the hiring event flag for this requisition
	 * 
	 * @param hireEvntFlg		The hiring event flag for this requisition
	 */
	public void setHireEvntFlg(String hireEvntFlg)
    {
    	mHireEvntFlg = hireEvntFlg;
    } // end function setHireEvntFlg()
	
	/**
	 * Get the RSC schedule flag for this requistion
	 * 
	 * @return					The RSC schedule flag for this requistion
	 */
	public String getRscSchFlg()
    {
    	return mRscSchFlg;
    } // end function getRscSchFlg()
	
	/**
	 * Set the RSC schedule flag for this requistion
	 * 
	 * @param rscSchFlg			The RSC schedule flag for this requistion
	 */
	public void setRscSchFlg(String rscSchFlg)
    {
    	mRscSchFlg = rscSchFlg;
    } // end function setRscSchFlg()
	
	/**
	 * Get the required interview duration (in minutes) for this requisition
	 * 
	 * @return					The required interview duration (in minutes) for this requisition
	 */
	public short getIntrvwMins()
    {
    	return mIntrvwMins;
    } // end function getIntrvwMins()
	
	/**
	 * Set the required interview duration (in minutes) for this requisition
	 * 
	 * @param intrvwMins		The required interview duration (in minutes) for this requisition
	 */
	public void setIntrvwMins(short intrvwMins)
    {
    	mIntrvwMins = intrvwMins;
    } // end setIntrvwMins()
	
	/**
	 * Get the requisition active flag
	 * 
	 * @return					The requisition active flag
	 */
	public String getReqActvFlg()
    {
    	return mReqActvFlg;
    } // end function getReqActvFlg()
	
	/**
	 * Set the requisition active flag
	 * 
	 * @param reqActvFlg		The requisition active flag
	 */
	public void setReqActvFlg(String reqActvFlg)
    {
    	mReqActvFlg = reqActvFlg;
    } // end function setReqActvFlg()
	
	/**
	 * Get the candidate active flag
	 * 
	 * @return					The candidate active flag
	 */
	public String getCandActvFlg()
    {
    	return mCandActvFlg;
    } // end function getCandActvFlg()
	
	/**
	 * Set the candidate active flag
	 * 
	 * @param candActvFlg		The candidate active flag
	 */
	public void setCandActvFlg(String candActvFlg)
    {
    	mCandActvFlg = candActvFlg;
    } // end function setCandActvFlg()    
	
	/**
	 * Get the hiring event type
	 * 
	 * @return	The hiring event type
	 */
	public String getEventType()
    {
    	return mEventType;
    } // end function getEventType()
	
	/**
	 * Set the hiring event type
	 * 
	 * @param event type
	 */
	public void setEventType(String eventType)
    {
    	mEventType = eventType;
    } // end function setEventType()	
} // end class InterviewScheduleDetails
package com.homedepot.hr.hr.retailstaffing.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: OrgParamDTO
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * This class is used to transport HR Organization Parameter data 
 */
public class OrgParamTO implements Serializable
{
    private static final long serialVersionUID = -1075315428505459529L;

    // ID of the user that created the parameter
    private String mCrtSysUserId;
    // Time the parameter was created
    private Timestamp mCrtTs;
    // ID of the user that last updated the parameter
    private String mLastUpdSysUserId;
    // Time the parameter was last updated
    private Timestamp mLastUpdTs;
    // parameter description
    private String mDesc;
    // parameter int value
    private int mIntVal;
    // parameter decimal value
    private BigDecimal mDecimalVal;
    // parameter character value
    private String mCharVal;
    // parameter date value
    private Date mDateVal;
    // parameter time value
    private Time mTimeVal;
    // parameter timestamp value
    private Timestamp mTsVal;
    // data type indicator
    private String mDataTypeInd;
    
    /**
     * Get the ID of the user that created the parameter
     * 
     * @return			The ID of the user that created the parameter
     */
    public String getCrtSysUserId()
    {
    	return mCrtSysUserId;
    } // end function getCrtSysUserId()
    
    /**
     * Set the ID of the user that created the parameter
     * 
     * @param userId	The ID of the user that created the parameter
     */
    public void setCrtSysUserId(String userId)
    {
    	mCrtSysUserId = userId;
    } // end function setCrtSysUserId()
    
    /**
     * Get the time the parameter was created
     * 
     * @return			The time the parameter was created
     */
    public Timestamp getCrtTs()
    {
    	return mCrtTs;
    } // end function getCrtTs()
    
    /**
     * Set the time the parameter was created
     * 
     * @param ts		The time the parameter was created
     */
    public void setCrtTs(Timestamp ts)
    {
    	mCrtTs = ts;
    } // end function setCrtTs()
    
    /**
     * Get the ID of the user that last updated the parameter
     * 
     * @return			The ID of the user that last updated the parameter
     */
    public String getLastUpdSysUserId()
    {
    	return mLastUpdSysUserId;
    } // end function getLastUpdSysUserId()
    
    /**
     * Set the ID of the user that last updated the parameter
     * 
     * @param userId	The ID of the user that last updated the parameter
     */
    public void setLastUpdSysUserId(String userId)
    {
    	mLastUpdSysUserId = userId;
    } // end function setLastUpdSysUserId()
    
    /**
     * Get the time the parameter was last updated
     * 
     * @return			The time the parameter was last updated
     */
    public Timestamp getLastUpdTs()
    {
    	return mLastUpdTs;
    } // end function getLastUpdTs()
    
    /**
     * Set the time the parameter was last updated
     * 
     * @param ts		The time the parameter was last updated
     */
    public void setLastUpdTs(Timestamp ts)
    {
    	mLastUpdTs = ts;
    } // end function setLastUpdTs()
    
    /**
     * Get the parameter description
     * 
     * @return			The parameter description
     */
    public String getDesc()
    {
    	return mDesc;
    } // end function getDesc()
    
    /**
     * Set the parameter description
     * 
     * @param desc		The parameter description
     */
    public void setDesc(String desc)
    {
    	mDesc = desc;
    } // end function setDesc()
    
    /**
     * Get the parameter int value
     * 
     * @return			The parameter int value
     */
    public int getIntVal()
    {
    	return mIntVal;
    } // end function getIntVal()
    
    /**
     * Set the parameter int value
     * 
     * @param value		The parameter int value
     */
    public void setIntVal(int value)
    {
    	mIntVal = value;
    } // end function setIntVal()
    
    /**
     * Get the parameter decimal value
     * 
     * @return			The parameter decimal value
     */
    public BigDecimal getDecimalVal()
    {
    	return mDecimalVal;
    } // end function getDecimalVal()
    
    /**
     * Set the parameter decimal value
     * 
     * @param value		The parameter decimal value
     */
    public void setDecimalVal(BigDecimal value)
    {
    	mDecimalVal = value;
    } // end function setDecimalVal()
    
    /**
     * Get the parameter character value
     * 
     * @return			The parameter character value
     */
    public String getCharVal()
    {
    	return mCharVal;
    } // end function getCharVal()
    
    /**
     * Set the parameter character value
     * 
     * @param value		The parameter character value
     */
    public void setCharVal(String value)
    {
    	mCharVal = value;
    } // end function setCharVal()
    
    /**
     * Get the parameter date value
     * 
     * @return			The parameter date value
     */
    public Date getDateVal()
    {
    	return mDateVal;
    } // end function getDateVal()
    
    /**
     * Set the parameter date value
     * 
     * @param value		The parameter date value
     */
    public void setDateVal(Date value)
    {
    	mDateVal = value;
    } // end function setDateVal()
    
    /**
     * Get the parameter time value
     * 
     * @return			The parameter time value
     */
    public Time getTimeVal()
    {
    	return mTimeVal;
    } // end function getTimeVal()
    
    /**
     * Set the parameter time value
     * 
     * @param value		The parameter time value
     */
    public void setTimeVal(Time value)
    {
    	mTimeVal = value;
    } // end function setTimeVal()
    
    /**
     * Get the parameter timestamp value
     * 
     * @return			The parameter timestamp value
     */
    public Timestamp getTsVal()
    {
    	return mTsVal;
    } // end function getTsVal()
    
    /**
     * Set the parameter timestamp value
     * 
     * @param value		The parameter timestamp value
     */
    public void setTsVal(Timestamp value)
    {
    	mTsVal = value;
    } // end function setTsVal()
    
    /**
     * Get the parameter data type indicator
     * 
     * @return			The parameter data type indicator
     */
    public String getDataTypeInd()
    {
    	return mDataTypeInd;
    } // end function getDataTypInd()
    
    /**
     * Set the parameter data type indicator
     * 
     * @param dataTypeInd	The parameter data type indicator
     */
    public void setDataTypeInd(String dataTypeInd)
    {
    	mDataTypeInd = dataTypeInd;
    } // end function setDataTypeInd()
} // end class OrgParamTO
package com.homedepot.hr.hr.retailstaffing.dto;
/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application: RetailStaffing
 *
 * File Name: Requisition.java
 */
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Transfer object used to store employment requisition details. 
 * 
 * @author rlp05
 */

public class Requisition
{
	private int mNumber;
	private String mStoreNbr;
	private Timestamp mCrtTs;
	private String mCrtUserId;
	private String mDeptNbr;
	private String mJobTtlCd;
	private Date mReqdPosnFillDt;
	private short mAuthPosnCount;
	private short mOpenPosnCount;
	private boolean mFtReqdFlg;
	private boolean mPtReqdFlg;
	private boolean mPmReqdFlg;
	private boolean mWkndReqdFlg;
	private String mActvFlg;
	private String mLastUpdUserId;
	private Timestamp mLastUpdTs;

	/**
	 * @return unique identifier for this requisition
	 */
	public int getNumber()
	{
		return mNumber;
	} // end function getNumber()

	/**
	 * @param number
	 *            unique identifier for this requisition
	 */
	public void setNumber(int number)
	{
		mNumber = number;
	} // end function setNumber()

	/**
	 * @return the store number this requisition is for
	 */
	public String getStoreNbr()
	{
		return mStoreNbr;
	} // end function getStoreNbr()

	/**
	 * @param storeNbr
	 *            the new store number value
	 */
	public void setStoreNbr(String storeNbr)
	{
		mStoreNbr = storeNbr;
	} // end function setStoreNbr()

	/**
	 * @return date/time this requisition was created
	 */
	public Timestamp getCrtTs()
	{
		return mCrtTs;
	} // end function getCrtTs()

	/**
	 * @param crtTs
	 *            date/time this requisition was created
	 */
	public void setCrtTs(Timestamp crtTs)
	{
		mCrtTs = crtTs;
	} // end function crtTs()

	/**
	 * @return id of the user that created this requisition
	 */
	public String getCrtUserId()
	{
		return mCrtUserId;
	} // end function getCrtUserId()

	/**
	 * @param crtUserId
	 *            id of the user that created this requisition
	 */
	public void setCrtUserId(String crtUserId)
	{
		mCrtUserId = crtUserId;
	} // end function setCrtUserId()

	/**
	 * @return department this requisition is for
	 */
	public String getDeptNbr()
	{
		return mDeptNbr;
	} // end getDeptNbr()

	/**
	 * @param deptNbr
	 *            department this requisition is for
	 */
	public void setDeptNbr(String deptNbr)
	{
		mDeptNbr = deptNbr;
	} // end function setDeptNbr()

	/**
	 * @return job title this requisition is for
	 */
	public String getJobTtlCd()
	{
		return mJobTtlCd;
	} // end function getJobTtlCd()

	/**
	 * @param jobTtlCd
	 *            job title this requisition is for
	 */
	public void setJobTtlCd(String jobTtlCd)
	{
		mJobTtlCd = jobTtlCd;
	} // end function setJobTtlCd()

	/**
	 * @return date this requisition needs to be filled
	 */
	public Date getReqdPosnFillDt()
	{
		return mReqdPosnFillDt;
	} // end function getReqdPosnFillDt()

	/**
	 * @param fillDt
	 *            date this requisition needs to be filled
	 */
	public void setReqdPosnFillDt(Date fillDt)
	{
		mReqdPosnFillDt = fillDt;
	} // end function setReqdPosnFillDt()

	/**
	 * @return authorized position count for this requisition
	 */
	public short getAuthPosnCount()
	{
		return mAuthPosnCount;
	} // end function getAuthPosnCount()

	/**
	 * @param count
	 *            authorized position count for this requisition
	 */
	public void setAuthPosnCount(short count)
	{
		mAuthPosnCount = count;
	} // end function setAuthPosnCount()

	/**
	 * @return open position count for this requisition
	 */
	public short getOpenPosnCount()
	{
		return mOpenPosnCount;
	} // end functoin getOpenPosnCount()

	/**
	 * @param count
	 *            open position count for this requisition
	 */
	public void setOpenPosnCount(short count)
	{
		mOpenPosnCount = count;
	} // end function setOpenPosnCount()

	/**
	 * @return full-time required flag
	 */
	public boolean getFtReqdFlg()
	{
		return mFtReqdFlg;
	} // end function getFtReqdFlg()

	/**
	 * @param flag
	 *            full-time required flag
	 */
	public void setFtReqdFlg(boolean flag)
	{
		mFtReqdFlg = flag;
	} // end function setFtReqdFlg()

	/**
	 * @return part-time required flag
	 */
	public boolean getPtReqdFlg()
	{
		return mPtReqdFlg;
	} // end function getPtReqdFlg()

	/**
	 * @param flag
	 *            part-time required flag
	 */
	public void setPtReqdFlg(boolean flag)
	{
		mPtReqdFlg = flag;
	} // end function setPtReqdFlg()

	/**
	 * @return evening required flag
	 */
	public boolean getPmReqdFlg()
	{
		return mPmReqdFlg;
	} // end function getPmReqdFlg()

	/**
	 * @param flag
	 *            evening required flag
	 */
	public void setPmReqdFlg(boolean flag)
	{
		mPmReqdFlg = flag;
	} // end function setPmReqdFlg()

	/**
	 * @return weekend required flag
	 */
	public boolean getWkndReqdFlg()
	{
		return mWkndReqdFlg;
	} // end function getWkndReqdFlg()

	/**
	 * 
	 * @param wkndReqdFlg
	 *            weekend required flag
	 */
	public void setWkndReqdFlg(boolean wkndReqdFlg)
	{
		mWkndReqdFlg = wkndReqdFlg;
	} // end function setWkndReqdFlg()

	/**
	 * @return requisition active flag
	 */
	public String getActvFlg()
	{
		return mActvFlg;
	} // end function getActvFlg()

	/**
	 * @param flag
	 *            requisition active flag
	 */
	public void setActvFlg(String flag)
	{
		mActvFlg = flag;
	} // end function setActvFlg()

	/**
	 * @return id of the last user to update this requisition
	 */
	public String getLastUpdUserId()
	{
		return mLastUpdUserId;
	} // end function getLastUpdUserId()

	/**
	 * @param userId
	 *            id of the last user to update this requisition
	 */
	public void setLastUpdUserId(String userId)
	{
		mLastUpdUserId = userId;
	} // end function setLastUpdUserId()

	/**
	 * @return last time this requisition was updated
	 */
	public Timestamp getLastUpdTs()
	{
		return mLastUpdTs;
	} // end function getLastUpdTs()

	/**
	 * @param lastUpdTs
	 *            last time this requisition was updated
	 */
	public void setLastUpdTs(Timestamp lastUpdTs)
	{
		mLastUpdTs = lastUpdTs;
	} // end function setLastUpdTs()
} // end class Requisition
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
 * File Name: JobCategoryDetails.java
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Transfer object used to store job category details for a specific
 * store, department, and job category code
 * 
 * @author rlp05
 */
public class JobCategoryDetails
{
	// consent decree job category code
	private String mConsentDecreeJobCtgryCd;
	// consent decree job category description
	private String mConsentDecreeJobCtgryDesc;
	// primary position category code
	private int mPrimPosnCtgryCd;
	// list of job category codes (including the consent decree job category code)
	private List<String> mJobCtgryCds;
	// position code (used to store the position preference code needed to get applicants for a requisition)
	private short mPosnCd;
	// used for Tiering, EMPTST_ID, this is the Tiering Category Code
	private short mTieringCtgryCd;
	
	/**
	 * Constructor()
	 */
	public JobCategoryDetails()
	{
		mJobCtgryCds = new ArrayList<String>();
	} // end constructor()
	
	/**
	 * @return			The consent decree job category code
	 */
	public String getConsentDecreeJobCtgryCd()
	{
		return mConsentDecreeJobCtgryCd;
	} // end function getConsentDecreeJobCtgryCd()
	
	/**
	 * @param code		The consent decree job category code
	 */
	public void setConsentDecreeJobCtgryCd(String code)
	{
		mConsentDecreeJobCtgryCd = code;
		
		// if this code isn't already in the job category code map, add it
		if(!mJobCtgryCds.contains(code))
		{
			mJobCtgryCds.add(code);
		} // end if(!mJobCtgryCds.contains(code))
	} // end function setConsentDecreeJobCtgryCd()
	
	/**
	 * @return			The consent decree job category description
	 */
	public String getConsentDecreeJobCtgryDesc()
	{
		return mConsentDecreeJobCtgryDesc;
	} // end function getConsentDecreeJobCtgryDesc()
	
	/**
	 * @param desc		The consent decree job category description
	 */
	public void setConsentDecreeJobCtgryDesc(String desc)
	{
		mConsentDecreeJobCtgryDesc = desc;
	} // end function setConsentDecreeJobCtgryDesc()
	
	/** 
	 * @return			The primary position category code
	 */
	public int getPrimPosnCtgryCd()
	{
		return mPrimPosnCtgryCd;
	} // end function getPrimPosnCtgryCd()
	
	/**
	 * @param code		The primary position category code
	 */
	public void setPrimPosnCtgryCd(int code)
	{
		mPrimPosnCtgryCd = code;
	} // end function setPrimPosnCtgryCd()
	
	/**
	 * @return			List of job category codes (including the concent decree job category code)
	 */
	public List<String> getJobCtrgyCds()
	{
		return mJobCtgryCds;
	} // end function getJobCtgryCds()
	
	/**
	 * @param code		Job category code to add to the category codes list
	 */
	public void addJobCtgryCd(String code)
	{
		// add the code if it's not already in the list
		if(!mJobCtgryCds.contains(code))
		{
			mJobCtgryCds.add(code);
		} // end if(!mJobCtgryCds.contains(code))
	} // end function addJobCtgryCd()
	
	/**
	 * @return			Position code (used to store the position preference code needed to get applicants for a req)
	 */
	public short getPosnCd()
	{
		return mPosnCd;
	} // end function getPosnCd()
	
	/**
	 * @param posnCd	Position code (used to store the position preference code needed to get applicants for a req)
	 */
	public void setPosnCd(short posnCd)
	{
		mPosnCd = posnCd;
	} // end function setPosnCd()
	
	/**
	 * @return			Used for Tiering, EMPTST_ID, this is the Tiering Category Code
	 */
	public short getTieringCtgryCd()
	{
		return mTieringCtgryCd;
	} // end function getTieringCtgryCd()
	
	/**
	 * @param tieringCtgryCd	Used for Tiering, EMPTST_ID, this is the Tiering Category Code
	 */
	public void setTieringCtgryCd(short tieringCtgryCd)
	{
		mTieringCtgryCd = tieringCtgryCd;
	} // end function setTieringCtgryCd()	
	
} // end class JobCategoryDetails
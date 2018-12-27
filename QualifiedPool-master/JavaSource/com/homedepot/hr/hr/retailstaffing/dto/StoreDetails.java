package com.homedepot.hr.hr.retailstaffing.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StoreDetails.java
 * Application: RetailStaffing
 */
import java.io.Serializable;

/**
 * Transfer object containing HR store data
 * 
 * @author rlp05
 */
public class StoreDetails implements Serializable
{
	private static final long serialVersionUID = -6938500534554662019L;
	
	// the store number
	private String mStoreNbr;
	// HR store group type code
	private String mStoreGroupCd;
	// HR location type code
	private String mLocTypCd;
	// HR store type code
	private String mStoreTypCd;
	// HR country code
	private String mCntryCd;
	
	/**
	 * Default constructor
	 */
	public StoreDetails()
	{
		
	} // end constructor
	
	/**
	 * @return				The store number
	 */
	public String getStoreNbr()
	{
		return mStoreNbr;
	} // end function getStoreNbr()
	
	/**
	 * @param storeNbr		The store number
	 */
	public void setStoreNbr(String storeNbr)
	{
		mStoreNbr = storeNbr;
	} // end function setStoreNbr()
	
	/**
	 * @return				HR store group type code
	 */
	public String getStoreGroupCd()
	{
		return mStoreGroupCd;
	} // end function getStoreGroupCd()
	
	/**
	 * @param storeGroupCd	HR store group type code
	 */
	public void setStoreGroupCd(String storeGroupCd)
	{
		mStoreGroupCd = storeGroupCd;
	} // end function setStoreGroupCd()
	
	/**
	 * @return				HR location type code
	 */
	public String getLocTypCd()
	{
		return mLocTypCd;
	} // end function getLocTypCd()
	
	/**
	 * @param locTypCd		HR location type code
	 */
	public void setLocTypCd(String locTypCd)
	{
		mLocTypCd = locTypCd;
	} // end function setLocTypCd()
	
	/**
	 * @return				HR store type code
	 */
	public String getStoreTypCd()
	{
		return mStoreTypCd;
	} // end function getStoreTypCd()
	
	/**
	 * @param storeTypCd	HR store type code
	 */
	public void setStoreTypCd(String storeTypCd)
	{
		mStoreTypCd = storeTypCd;
	} // end function setStoreTypCd()
	
	/**
	 * @return				HR country code
	 */
	public String getCntryCd()
	{
		return mCntryCd;
	} // end function getCntryCd()
	
	/**
	 * @param cntryCd		HR country code
	 */
	public void setCntryCd(String cntryCd)
	{
		mCntryCd = cntryCd;
	} // end function setCntryCd()
} // end class StoreDetails()
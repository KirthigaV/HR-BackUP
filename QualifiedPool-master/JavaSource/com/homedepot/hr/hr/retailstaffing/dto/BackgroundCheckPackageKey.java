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
 * File Name: BackgroundCheckPackageKey.java
 */
import java.io.Serializable;

/**
 * This class is the key to the background package cache
 * 
 * @author rlp05
 */
public class BackgroundCheckPackageKey implements Serializable
{
    private static final long serialVersionUID = -9026923584895368441L;

    private String mStoreTypCd;
    private String mCntryCd;
    private String mDeptNbr;
    private String mJobTtlCd;

    /**
     * Constructor
     * 
     * @param storeTypCd			Store type code
     * @param cntryCd				Country code
     * @param deptNbr				Department number
     * @param jobTtlCd				Job Title Code
     */
    public BackgroundCheckPackageKey(String storeTypCd, String cntryCd, String deptNbr, String jobTtlCd)
    {
    	mStoreTypCd = storeTypCd;
    	mCntryCd = cntryCd;
    	mDeptNbr = deptNbr;
    	mJobTtlCd = jobTtlCd;
    } // end constructor()
    
    /**
     * @return			The store type code
     */
    public String getStoreTypCd()
    {
    	return mStoreTypCd;
    } // end function getStoreTypCd()
    
    /**
     * @return			The country code
     */
    public String getCntryCd()
    {
    	return mCntryCd;
    } // end function getCntryCd()
    
    /**
     * @return			The department number
     */
    public String getDeptNbr()
    {
    	return mDeptNbr;
    } // end function getDeptNbr()
    
    /**
     * @return			The job title code
     */
    public String getJobTtlCd()
    {
    	return mJobTtlCd;
    } // end function getJobTtlCd()

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((mCntryCd == null) ? 0 : mCntryCd.hashCode());
	    result = prime * result + ((mDeptNbr == null) ? 0 : mDeptNbr.hashCode());
	    result = prime * result + ((mJobTtlCd == null) ? 0 : mJobTtlCd.hashCode());
	    result = prime * result + ((mStoreTypCd == null) ? 0 : mStoreTypCd.hashCode());
	    return result;
    } // end function hashCode()

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj)
    {
		// if this is the same object as obj 
		if(this == obj)
		{
			return true;
		} // end if(this == obj)
		
		// if obj is null then they can't be equal
		if(obj == null)
		{
			return false;
		} // end if(obj == null)
		
		// if the class of this object is not the same as the class of the object passed in
		if(getClass() != obj.getClass())
		{
			return false;
		} // end if(getClass() != obj.getClass())

		// cast to the correct object type
	    BackgroundCheckPackageKey other = (BackgroundCheckPackageKey)obj;
	    
	    // if the country code of this object is null
	    if(mCntryCd == null)
	    {
	    	// if the country code of the other object is not null
	    	if(other.getCntryCd() != null)
	    	{
	    		return false;
	    	} // end if(other.getCntryCd() != null)
	    } // end if(mCntryCd == null)
	    else if(!mCntryCd.equals(other.getCntryCd()))
	    {
	    	// if the country code of this object is not null, and it is not equal to the country code of the other object
	    	return false;
	    } // end else if(!mCntryCd.equals(other.getCntryCd()))
	    
	    // if the department number of this object is null
	    if(mDeptNbr == null)
	    {
	    	// if the department number of the other object is not null
	    	if(other.getDeptNbr() != null)
	    	{
	    		return false;
	    	} // end if(other.getDeptNbr() != null)
	    } // end if(mDeptNbr == null)
	    else if(!mDeptNbr.equals(other.getDeptNbr()))
	    {
	    	// if the department number of this object is not null, and it is not equal to the department number of the other object
	    	return false;
	    } // end else if(!mDeptNbr.equals(other.getDeptNbr()))

	    // if the job title code of this object is null
	    if(mJobTtlCd == null)
	    {
	    	// if the job title code of the other object is not null
	    	if(other.getJobTtlCd() != null)
	    	{
	    		return false;
	    	} // end if(other.getJobTtlCd() != null)
	    } // end if(mJobTtlCd == null)
	    else if(!mJobTtlCd.equals(other.getJobTtlCd()))
	    {
	    	// if the job title code of this object is not null, and it's not equal to the job title code of the other object
	    	return false;
	    } // end else if(!mJobTtlCd.equals(other.getJobTtlCd()))

	    // if the store type code of this object is null
	    if(mStoreTypCd == null)
	    {
	    	// if the store type code of the other object is not null
	    	if(other.getStoreTypCd() != null)
	    	{
	    		return false;
	    	} // end if(other.getStoreTypCd() != null)
	    } // end if(mStoreTypCd == null)
	    else if(!mStoreTypCd.equals(other.getStoreTypCd()))
	    {
	    	// if the store type code of this object is not null, and it's not equal to the store type code of the other object
	    	return false;
	    } // end else if(!mStoreTypCd.equals(other.getStoreTypCd()))

	    // we got here so we must be equal
	    return true;
    } // end function equals
} // end class BackgroundCheckPackageKey
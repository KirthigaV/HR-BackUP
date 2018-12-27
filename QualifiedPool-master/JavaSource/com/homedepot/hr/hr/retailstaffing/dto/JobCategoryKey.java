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
 * File Name: JobCategoryKey.java
 */
import java.io.Serializable;
/**
 * This class is the key to the job category cache
 * 
 * @author rlp05
 */
public class JobCategoryKey implements Serializable
{
    private static final long serialVersionUID = 8265590769432682848L;

    private String mStrNbr;
    private String mDeptNbr;
    private String mJobTtlCd;

    /**
     * Constructor
     * 
     * @param strNbr		The store number
     * @param deptNbr		The department number
     * @param jobTtlCd		The job title code
     */
    public JobCategoryKey(String strNbr, String deptNbr, String jobTtlCd)
    {
    	mStrNbr = strNbr;
    	mDeptNbr = deptNbr;
    	mJobTtlCd = jobTtlCd;
    } // end constructor()
    
    /**
     * @return				The store number
     */
    public String getStrNbr()
    {
    	return mStrNbr;
    } // end function getStrNbr()
    
    /**
     * @return				The department number
     */
    public String getDeptNbr()
    {
    	return mDeptNbr;
    } // end function getDeptNbr()
    
    /**
     * @return				The job title code
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
	    result = prime * result + ((mDeptNbr == null) ? 0 : mDeptNbr.hashCode());
	    result = prime * result + ((mJobTtlCd == null) ? 0 : mJobTtlCd.hashCode());
	    result = prime * result + ((mStrNbr == null) ? 0 : mStrNbr.hashCode());
	    return result;
    } // end function hashCode()

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj)
    {
		// if this is the same object as the object passed in
		if(this == obj)
		{
			return true;
		} // end if(this == obj)
		
		// if the object passed in is null
		if(obj == null)
		{
			return false;
		} // end if(obj == null)
		
		// if the class of this object is not the same as the class of the object passed in
		if(getClass() != obj.getClass())
		{
			return false;
		} // end if(getClass() != obj.getClass())
		
		// cast to the correct type
		JobCategoryKey other = (JobCategoryKey)obj;
		
		// if the department number of this object is null
		if(mDeptNbr == null)
		{
			// and the department number of the other object is not null
			if(other.getDeptNbr() != null)
			{
				return false;
			} // end if(other.getDeptNbr() != null)
		} // end if(mDeptNbr == null)
		else
		{
			// if the department number of this object is not equal to the department number of the other object
			if(!mDeptNbr.equals(other.getDeptNbr()))
			{
				return false;
			} // end if(!mDeptNbr.equals(other.getDeptNbr()))
		} // end else

		// if the job title code of this object is null
		if(mJobTtlCd == null)
		{
			// if if the job title code of the other object is not null
			if(other.getJobTtlCd() != null)
			{
				return false;
			} // end if(other.getJobTtlCd() != null)
		} // end if(mJobTtlCd == null)
		else
		{
			// if the job title code of this object is not null and not equal to the job title code of the other object
			if(!mJobTtlCd.equals(other.getJobTtlCd()))
			{
				return false;
			} // end if(!mJobTtlCd.equals(other.getJobTtlCd()))
		} // end else

		// if the store number of this object is null
		if(mStrNbr == null)
		{
			// if the store number of the other object is not null
			if(other.getStrNbr() != null)
			{
				return false;
			} // end if(other.getStrNbr() != null)
		} // end if(mStrNbr == null)
		else
		{
			// if the store number of this object is not null and not equal to the store number of the other object
			if(!mStrNbr.equals(other.getStrNbr()))
			{
				return false;
			} // end if(!mStrNbr.equals(other.getStrNbr()))
		} // end else

		// if we go here then the objects are equal
	    return true;
    } // end function equals()
} // end class JobCategoryKey
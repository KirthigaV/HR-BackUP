package com.homedepot.hr.hr.retailstaffing.dto;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StatusCacheKey.java
 * Application: RetailStaffing
 */

import java.io.Serializable;

/**
 * Key class for the status caches managed by the StatusManager class
 */
public class StatusCacheKey implements Serializable
{
    private static final long serialVersionUID = -4148643186213073275L;
    
	/** status code */
	private short mCode;
	/** description language identifier */
	private String mLangCd;
	
	public StatusCacheKey(short code, String langCd)
	{
		setCode(code);
		setLangCd(langCd);
	} // end constructor()
	
	/**
	 * @return status code
	 */
	public short getCode()
	{
		return mCode;
	} // end function getCode()
	
	/**
	 * @param code status code
	 */
	public void setCode(short code)
	{
		mCode = code;
	} // end function setCode()
	
	/**
	 * @return description language identifier 
	 */
	public String getLangCd()
	{
		return mLangCd;
	} // end function getLangCd()
	
	/**
	 * @param langCd description language identifier 
	 */
	public void setLangCd(String langCd)
	{
		mLangCd = langCd;
	} // end function setLangCd()
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + mCode;
	    result = prime * result + ((mLangCd == null) ? 0 : mLangCd.hashCode());
	    return result;
    } // end function hashCode()

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj)
    {
		if(this == obj)
		{
			return true;
		} // end if(this == obj)
		
		if(obj == null)
		{
			return false;
		} // end if(obj == null)
		
		if(getClass() != obj.getClass())
		{
			return false;
		} // end if(getClass() != obj.getClass())
		
		StatusCacheKey other = (StatusCacheKey)obj;
		
		if(mCode != other.getCode())
		{
			return false;
		} // end if(mCode != other.getCode())

		if(mLangCd == null)
		{
			if(other.getLangCd() != null)
			{
				return false;
			} // end if(other.getLangCd() != null)
		} // end if(mLangCd == null)
		else if(!mLangCd.equals(other.getLangCd()))
		{
			return false;
		} // end else if(!mLangCd.equals(other.getLangCd()))

		return true;
    } // end function equals()		
} // end class StatusCacheKey
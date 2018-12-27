package com.homedepot.hr.hr.retailstaffing.dto;
/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: CallTypVdnKeyTO.java
 */
import java.io.Serializable;

/**
 * Key for a Vector Destination Number in the VDN cache  
 */
public class CallTypVdnKeyTO implements Serializable
{
    private static final long serialVersionUID = -8408118446019868744L;

    /**
     * Language code, i.e. en_US
     */
    private String mLangCd;
    /**
     * Structured Interview Guide ID, i.e. DEFAULT
     */
    private String mStrIntvGuideId;
    /**
     * Type Description, i.e. INTSCHED_EN 
     */
    private String mTypDesc;
    
    /**
     * Default no-argument constructor
     */
    public CallTypVdnKeyTO()
    {
    	
    } // end default constructor()
    
    /**
     * Get the language code
     * 
     * @return				The language code
     */
    public String getLangCd()
    {
    	return mLangCd;
    } // end function getLangCd()
    
    /**
     * Set the language code
     * 
     * @param langCd		The language code
     */
    public void setLangCd(String langCd)
    {
    	mLangCd = langCd;
    } // end function setLangCd()
    
    /**
     * Get the structured interview guide id
     * 
     * @return				The guide id
     */
    public String getStrIntvGuideId()
    {
    	return mStrIntvGuideId;
    } // end function getStrIntvGuideId()
    
    /**
     * Set the structured interview guide id
     * 
     * @param id			The guide id
     */
    public void setStrIntvGuideId(String id)
    {
    	mStrIntvGuideId = id;
    } // end function setStrIntvGuideId()
    
    /**
     * Get the type description
     * 
     * @return				The type description
     */
    public String getTypDesc()
    {
    	return mTypDesc;
    } // end function getTypDesc()
    
    /**
     * Set the type description
     * 
     * @param desc			The type description
     */
    public void setTypDesc(String desc)
    {
    	mTypDesc = desc;
    } // end function setTypDesc()

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((mLangCd == null) ? 0 : mLangCd.hashCode());
	    result = prime * result + ((mStrIntvGuideId == null) ? 0 : mStrIntvGuideId.hashCode());
	    result = prime * result + ((mTypDesc == null) ? 0 : mTypDesc.hashCode());
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

	    CallTypVdnKeyTO other = (CallTypVdnKeyTO)obj;

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

	    if(mStrIntvGuideId == null)
	    {
	    	if(other.getStrIntvGuideId() != null)
	    	{
	    		return false;
	    	} // end if(other.getStrIntvGuideId() != null)
	    } // end if(mStrIntvGuideId == null)
	    else if(!mStrIntvGuideId.equals(other.getStrIntvGuideId()))
	    {
	    	return false;
	    } // end else if(!mStrIntvGuideId.equals(other.getStrIntvGuideId()))
	    
	    if(mTypDesc == null)
	    {
	    	if(other.getTypDesc() != null)
	    	{
	    		return false;
	    	} // end if(other.getTypDesc() != null)
	    } // end if(mTypDesc == null)
	    else if(!mTypDesc.equals(other.getTypDesc()))
	    {
	    	return false;
	    } // end else if(!mTypDesc.equals(other.getTypDesc()))

	    return true;
    } // end equals()    
} // end class CallTypVdnKeyTO
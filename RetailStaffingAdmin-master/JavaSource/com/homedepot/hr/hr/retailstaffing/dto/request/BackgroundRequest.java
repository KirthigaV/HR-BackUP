package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("backgroundRequest")
public class BackgroundRequest implements Serializable
{
    private static final long serialVersionUID = -3793220399205328672L;

    @XStreamAlias("applicantId")
    private String mApplicantId;
    @XStreamAlias("firstName")
    private String mFirstName;
    @XStreamAlias("lastName")
    private String mLastName;
    @XStreamAlias("storeNumber")
    private String mStoreNumber;
    @XStreamAlias("languageCode")
    private String mLangCd;
    
    public String getApplicantId()
    {
    	return mApplicantId;
    } // end function getCandidateId()
    
    public void setApplicantId(String applicantId)
    {
    	mApplicantId = applicantId;
    } // end function setApplicantId()
    
    public String getFirstName()
    {
    	return mFirstName;
    } // end function getFirstName()
    
    public void setFirstName(String firstName)
    {
    	mFirstName = firstName;
    } // end function setFirstName()
    
    public String getLastName()
    {
    	return mLastName;
    } // end function getLastName()
    
    public void setLastName(String lastName)
    {
    	mLastName = lastName;
    } // end function setLastName()
    
    public String getStoreNumber()
    {
    	return mStoreNumber;
    } // end function getStoreNumber()
    
    public void setStoreNumber(String storeNumber)
    {
    	mStoreNumber = storeNumber;
    } // end function setStoreNumber()
    
    public String getLangCd()
    {
    	return mLangCd;
    } // end function getLangCd()
    
    public void setLangCd(String langCd)
    {
    	mLangCd = langCd;
    } // end function setLangCd()
} // end class BackgroundRequest
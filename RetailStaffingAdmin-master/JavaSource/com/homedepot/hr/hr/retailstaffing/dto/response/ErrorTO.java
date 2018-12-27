package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Used to transport error details to clients of the inbound phone
 * screen service methods being developed for RSA release 4.0
 */
@XStreamAlias("error")
public class ErrorTO implements Serializable
{
	private static final int VERSION0 = 0;
	
    private static final long serialVersionUID = 2374468464606332286L;
    // error message text (this field will be used when version != 0)
    @XStreamAlias("errorMessage")
    private String mErrMsg;
    // error message text (this field will be used when version == 0)
    @XStreamAlias("errorMsg")
    private String mErrorMsg;
    
    // indicates the version of the XML to generate
    @XStreamOmitField
    private int mVersion;

    /**
     * Default no-argument constructor
     */
    public ErrorTO(int version)
    {
    	mVersion = version;
    } // end constructor()
    
    /**
     * Constructor
     * 
     * @param errMsg		Error message text
     */
    public ErrorTO(String errMsg, int version)
    {
    	mVersion = version;
    	setErrMsg(errMsg);
    } // end constructor()
    
    /**
     * Get the error message text
     * 
     * @return				The error message text
     */
    public String getErrMsg()
    {
    	return (mVersion == VERSION0 ? mErrorMsg : mErrMsg);
    } // end function getErrMsg()
    
    /**
     * Set the error message text
     * 
     * @param errMsg		The error message text
     */
    public void setErrMsg(String errMsg)
    {
    	if(mVersion == VERSION0)
    	{
    		mErrorMsg = errMsg;
    	} // end if(mVersion == VERSION0)
    	else
    	{
    		mErrMsg = errMsg;
    	} // end else
    } // end function setErrMsg()
    
    /**
     * Get the version of the XML that will be produced by this object
     * 
     * @return				The version of the XML that will be produced by this object
     */
    public int getVersion()
    {
    	return mVersion;
    } // end function getVersion()
} // end class ErrorTO
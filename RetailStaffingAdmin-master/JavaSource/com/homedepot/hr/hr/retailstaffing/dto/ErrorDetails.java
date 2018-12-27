package com.homedepot.hr.hr.retailstaffing.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ErrorDetails.java
 * Application: RetailStaffing
 */
import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transfer object used to store voltage web service exception response data. This transfer
 * object is populated by methods in the VoltageWebService. The XStream API is used
 * to marshal this transfer object into the following XML:<br><br>
 * 
 * <code>
 * 	 &lt;errorDetails&gt;<br>
 *   &nbsp;&nbsp;&lt;errorCode&gt;100&lt;/errorCode&gt;<br>
 *   &nbsp;&nbsp;&lt;errorMessage&gt;Detailed exception message text here!&lt;/errorMessage&gt;<br>
 *   &lt;/errorDetails&gt;<br>
 * </code>
 * 
 * @author rlp05
 */
@XStreamAlias("errorDetails")
public class ErrorDetails implements Serializable
{
    
	private static final long serialVersionUID = 1905760564469423132L;

	// error code that can be used to identify the type of error that occurred during processing
	@XStreamAlias("errorCode")
	private int mErrorCode;
	
	// text description of the error that occurred (should not be used by a UI, will not be NLS'd)
	@XStreamAlias("errorMessage")
	private String mErrorMessage;
	
	/**
	 * @return					Error code that can be used to identify the type of error that 
	 * 							occurred during processing. 
	 */
	public int getErrorCode()
	{
		return mErrorCode;
	} // end function getErrorCode()
	
	/**
	 * @param errorCode			Error code that can be used to identify the type of error that 
	 * 							occurred during processing.
	 */
	public void setErrorCode(int errorCode)
	{
		mErrorCode = errorCode;
	} // end function setErrorCode()
	
	/**
	 * @return					Text description of the error that occurred during processing.
	 */
	public String getErrorMessage()
	{
		return mErrorMessage;
	} // end function getErrorMessage()
	
	/**
	 * @param message			Text description of the error that occurred during processing.
	 */
	public void setErrorMessage(String message)
	{
		mErrorMessage = message;
	} // end function setErrorMessage()	
} // end class ErrorDetails
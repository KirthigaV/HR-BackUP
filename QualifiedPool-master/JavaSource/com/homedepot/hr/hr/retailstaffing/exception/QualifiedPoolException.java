package com.homedepot.hr.hr.retailstaffing.exception;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: QualifiedPoolException.java
 * Application: RetailStaffing
 */
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * Application specific exception implementation that handles an additional error
 * code value.
 * 
 * @author rlp05
 */
public class QualifiedPoolException extends QueryException
{
	private static final long serialVersionUID = -2798576220157115868L;

	// error code that identifies the type of exception that occurred
	private int mErrorCode;
	
	/**
	 * Constructor
	 * 
	 * @param errorCode		Error code that identifies the type of exception that occurred		
	 * @param message		Exception message text
	 */
	public QualifiedPoolException(int errorCode, String message)
	{
		super(message);
		mErrorCode = errorCode;
	} // end constructor()
	
	/**
	 * Constructor
	 * 
	 * @param errorCode		Error code that identifies the type of exception that occurred
	 * @param exception		Root cause exception
	 */
	public QualifiedPoolException(int errorCode, Throwable exception)
	{
		super(exception.getMessage(), exception);
		mErrorCode = errorCode;
	} // end constructor()
	
	/**
	 * @return				Error code that identifies the type of exception that occurred
	 */
	public int getErrorCode()
	{
		return mErrorCode;
	} // end function getErrorCode()
} // end class QualifiedPoolException
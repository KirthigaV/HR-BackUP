package com.homedepot.hr.hr.retailstaffing.enumerations;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ErrorType.java
 * Application: RetailStaffing
 */

/** Enumeration used to represent a type of application error */
public enum ErrorType
{
	INPUT_VALIDATION ("ERR-0001"),
	NO_ROWS_FOUND ("ERR-0002"),
	QUERY_DATABASE ("ERR-0003");
	
	/** error type indicator */
	private String mType;
	
	/*
	 * Constructor
	 *  
	 * @param type error type indicator
	 */
	private ErrorType(String type)
	{
		mType = type;
	} // end constructor
	
	/**
	 * @return the error type indicator
	 */
	public String getType()
	{
		return mType;
	} // end function getTypeCode()
} // end enumeration
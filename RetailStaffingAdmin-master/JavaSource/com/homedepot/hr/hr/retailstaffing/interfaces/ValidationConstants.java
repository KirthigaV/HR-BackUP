package com.homedepot.hr.hr.retailstaffing.interfaces;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ValidationConstants.java
 * Application: RetailStaffing
 */

/**
 * This interface contains SHARED constants used by different components of the application
 * to validate input fields 
 */
public interface ValidationConstants
{
	/** Version 0 indicator */
	public static final int VER0 = 0;
	/** Version 1 indicator */
	public static final int VER1 = 1;
	/** Version 2 indicator */
	public static final int VER2 = 2;
			
	// ------------------------------------------------------------------------
	// validation regular expressions 
	// ------------------------------------------------------------------------

	// last 6 of candidate id
	public static final String VALID_REGEX_LAST6_CANDID = "\\d{6}";	
	// us postal code
	public static final String VALID_REGEX_US_ZIP = "\\d{5}";
	// language code validation regular expression pattern
	public static final String VALID_REGEX_LANG_CD = "[a-zA-Z]{2}_[a-zA-Z]{2}";
	// regular expression used to validate time (HH:mm where mm has to be 00 or 30)
	public static final String VALID_REGEX_TIME_FORMAT = "((0[0-9]|1[0-9]|2[0-3]):(0[0]|3[0]))";	
	// regular expression used to validate date strings provided are in MM-dd-yyyy format
	public static final String VALID_REGEX_DATE_FORMAT = "(((0[13578]|1[02])-(0[1-9]|1[0-9]|2[0-9]|3[01])-([2-9]([0-9]{3})))|((0[469]|1[1])-(0[1-9]|1[0-9]|2[0-9]|3[0])-([2-9]([0-9]{3})))|((0[2])-(0[1-9]|1[0-9]|2[0-9])-([2-9]([0-9]{3}))))";
	// regular expression used to validate an applicant id
	public static final String VALID_REGEX_APPLICANT_ID = "\\d{9}";
	
	/** minimum sequence number (exclusive) */
	public static final short MIN_SEQUENCE_NUMBER = 0;
	/** minimum number of records to return */
	public static final int MIN_MAX_RECORD_COUNT = 0;
	/** minimum hiring event id value */
	public static final int MIN_HIRE_EVENT_ID = 0;
} // end interface ValidationConstants
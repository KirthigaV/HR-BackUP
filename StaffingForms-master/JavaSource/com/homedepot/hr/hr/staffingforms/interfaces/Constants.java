package com.homedepot.hr.hr.staffingforms.interfaces;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: Constants.java
 * Application: RetailStaffing
 */

/**
 * This interface contains miscellaneous constants used by different components throughout the application
 */
public interface Constants
{
	/** Empty string */
	public static final String EMPTY_STRING = "";

	public static final String SUCCESS = "SUCCESS";
	
	/** total number of nanoseconds in a second */
	public static final double NANOS_IN_SECOND = 1000000000.0d;	
	/** total number of milliseconds in 1 day */
	public static final long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
	/** total number of milliseconds in 1 minute */	
	public static final long MILLISECONDS_PER_MINUTE = 1000*60;
	/** minimum number of concurrent interviewers (inclusive) */
	public static final short MIN_INTERVIEWERS = 1;
	/** maximum number of concurrent interviewers (inclusive) */
	public static final short MAX_INTERVIEWERS = 20;
	/** minimum number of recurring weeks (inclusive) */
	public static final short MIN_RECURRING_WEEKS = 1;
	/** maximum number of recurring weeks (inclusive) */
	public static final short MAX_RECURRING_WEEKS = 10;
	/** minimum sequence number (exclusive) */
	public static final short MIN_SEQUENCE_NUMBER = 0;
	
	/** interview slot status : Available */
	public static final short INTRW_SLOT_AVAILABLE = 1;
	/** interview slot status : Reserved */
	public static final short INTRW_SLOT_RESERVED = 2;
	/** interview slot status : Booked */
	public static final short INTRW_SLOT_BOOKED = 3;
	
	/** the interview duration, used during adds to break blocks of time into individual slots */
	public static final int SLOT_DURATION_IN_MINS = 30;	
	/** the number of milliseconds in an interview slot */
	public static final long SLOT_DURATION_IN_MS = SLOT_DURATION_IN_MINS * 60000L;	
	/** maximum number of interview slots allowed during one add availability call */
	public static final int MAX_SLOT_THRESHOLD = 680;	
	/** maximum number of interview slots for a time slot */
	public static final int MAX_SINGLE_SLOT_THRESHOLD = 20;	
	
	public static final String SQL_DATE_FORMAT = "yyyy-MM-dd";
	
	//Exception Severity codes
	static final int SEVERITY_HIGH = 1;
	static final int SEVERITY_MEDIUM = 2;
	static final int SEVERITY_LOW = 3;
	//Exception Status Codes
	short HTTP_STATUS_ERROR=405;
	short HTTP_STATUS_OK=200;
	//Error Description for Exception Handling
	
	//Start - Added as part of Flex to HTML Conversion - 12 May 2015
	public static final String HIRING_EVENT_JAXB_MARSHALL_ERROR_DESC = "Exception occured in marshall method in HiringEventJAXBTransformer";
	public static final String HIRING_EVENT_JAXB_UNMARSHALL_ERROR_DESC = "Exception occured in unMarshall method in HiringEventJAXBTransformer";
	public static final String HIRING_EVENT_XML_VALIDATION_ERROR_DESC = "Exception occured in validateXMLWellformedness method in PrintApplicationManager";
	public static final String CONTENT_TYPE_HEADER_INVALID = "Value for the Content-Type header is invalid";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String VERSION = "version";
	public static final String FAILURE_STR="FAILURE";
	
	public static final String MINUTES_ZERO = "00";
	public static final String MINUTES_THIRTY = "30";
	public static final String COLON = ":";
	public static final String SEMI_COLON = ";";
	public static final String ZERO = "0";
	public static final int TIMESTAMP_LENGTH = 16;
	public static final int TIMESTAMP_TIME_POSITION = 11;
	public static final String DISPLAY_DATE_FORMAT = "MM/dd/yyyy";
    //End - Added as part of Flex to HTML Conversion - 12 May 2015
    
} // end interface Constants
package com.homedepot.hr.hr.staffingforms.enumerations;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: InputField.java
 * Application: RetailStaffing
 */

/** input field enumerated type */
public enum InputField
{
	VERSION_NUMBER("INP-0001", "version number"),
	STORE_NUMBER("INP-0002", "store number"),
	CALENDAR_ID("INP-0003", "calendar id"),
	BEGIN_DATE("INP-0004", "begin date"),
	END_DATE("INP-0005", "end date"),
	DATE("INP-0006", "date"),
	BEGIN_TIME("INP-0007", "begin time"),
	END_TIME("INP-0008", "end time"),
	NUMBER_OF_INTERVIEWERS("INP-0009", "number of interviewers"),
	NUMBER_OF_RECURRING_WEEKS("INP-0010", "number of recurring weeks"),
	INTERVIEW_SLOT("INP-0011", "interview slot"),
	INPUT_XML("INP-0112", "input xml"),
	SEQUENCE_NUMBER("INP-0013", "sequence number"),
	REQUISITION_SCHEDULE_STATUS_CODE("INP-0014", "requisition schedule status code"),
	AVAILABILITY_BLOCK("INP-0015", "availability block"),
	LANGUAGE_CODE("INP-0016", "language code"),
	CALENDAR_NAME("INP-0016", "calendar name"),
	LAST_UPDATE_TIMESTAMP("INP-0017", "last update timestamp"),
	PHONE_SCREEN_NUM("INP-0018", "phone screen number"),
	INTERNAL_EXTRENAL("INP-0019", "Internal External Flag"),
	HIRE_EVENT_ID("INP-0119", "Hire Event ID"),
	HIRE_EVENT_NAME("INP-0120", "Hire Event Name"),
	DUPLICATE_HIRE_EVENT_NAME("INP-0121", "Duplicate Hire Event Name");
	
	
	/** field identifier */
	private String mFieldIdentifier;
	/** field label */
	private String mFieldLabel;
	
	/*
	 * @param fieldIdentifier field identifier
	 * @param fieldLabel field label
	 */
	InputField(String fieldIdentifier, String fieldLabel)
	{
		mFieldIdentifier = fieldIdentifier;
		mFieldLabel = fieldLabel;
	} // end constructor()
	
	/**
	 * @return field identifier
	 */
	public String getFieldIdentifier()
	{
		return mFieldIdentifier;
	} // end function getFieldIdentifier()
	
	/**
	 * @return field label
	 */
	public String getFieldLabel()
	{
		return mFieldLabel;
	} // end function getFieldLabel()
} // end enumeration
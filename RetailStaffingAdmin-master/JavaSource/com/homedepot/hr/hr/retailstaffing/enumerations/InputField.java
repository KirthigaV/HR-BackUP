package com.homedepot.hr.hr.retailstaffing.enumerations;
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
	STATUS_TYPE("INP-0001", "status type"),
	STATUS_CODE("INP-0002", "status code"),
	LANGUAGE_CODE("INP-0003", "language code"),
	VERSION_NUMBER("INP-0004", "version number"),
	ORGANIZATION_UNIT_TYPE("INP-0005", "organization unit type"),
	PHONE_SCREEN_ID("INP-0006", "phone screen id"),
	REQUISITION_NUMBER("INP-0007", "requisition number"),
	REQUISITION_CALENDAR_ID("INP-0008", "requisition calendar id"),
	STORE_NUMBER("INP-0009", "store number"),
	JOB_TITLE_CD("INP-0010", "job title code"),
	DEPARTMENT_NUMBER("INP-0011", "department number"),
	REQUISITION_CALENDAR_NAME("INP-0012", "requisition calendar name"),
	REQUISITION_CALENDAR_TYPE("INP-0013", "requisition calendar type"),
	INTERVIEW_STATUS("INP-0014", "interview status"),
	INPUT_XML("INP-0015", "input XML"),
	INTERVIEW_TIME_PREFERENCE("INP-0016", "interview time preference"),
	INTERVIEW_DATE("INP-0017", "interview date"),
	INTERVIEW_BEGIN_TIME("INP-0018", "interview begin time"),
	INTERVIEW_END_TIME("INP-0019", "interview end time"),
	INTERVIEW_SLOT("INP-0020", "interview slot"),
	INTERVIEW_SLOT_BEGIN_TIME("INP-0021", "interview slot begin time"),
	RESERVATION_DATETIME("INP-0022", "slot reservation date time"),
	INTERVIEW_SLOT_SEQUENCE("INP-0023", "interview slot sequence number"),
	INTERVIEW_DATE_DETAILS("INP-0024", "interview date details"),
	RELEASE_DATE("INP-0025", "release date"),
	RELEASE_DATE_BEGIN_TIME("INP-0026", "release date begin time"),
	RELEASE_DATE_END_TIME("INP-0027", "release date end time"),
	DATE("INP-0028", "date"),
	ITEMS_TO_ENCRYPT("INP-0029", "items to encrypt"),
	ITEM_TO_ENCRYPT("INP-0030", "item to encrypt"),
	ITEMS_TO_DECRYPT("INP-0031", "items to decrypt"),
	ITEM_TO_DECRYPT("INP-0032", "item to decrypt"),
	BEGIN_DATE("INP-0033", "begin date"),
	END_DATE("INP-0034", "end date"),
	BEGIN_TIMESTAMP("INP-0035", "begin timestamp"),
	SCHEDULE_DATE_DETAILS("INP-0036", "schedule date details"),
	PREFERRED_DATE("INP-0037", "preferred date"),
	OFFERED_DATE("INP-0038", "offered date"),
	MAX_RECORD_COUNT("INP-0039", "max record count"),
	APPLICANT_ID("INP-0040", "applicant id"),
	FIRST_NAME("INP-0041", "first name"),
	LAST_NAME("INP-0042", "last name"),
	OUTPUT_STREAM("INP-0043", "output stream"),
	APPLICANT("INP-0044", "applicant"),
	EVENT_MGR("INP-0045", "event Mgr"),
	HIRE_EVNT_ID("INP-0046","hire event id");
	
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
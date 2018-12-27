package com.homedepot.hr.hr.retailstaffing.enumerations;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplicationObject.java
 * Application: RetailStaffing
 */

/** Enumerated type representing an application object */
public enum ApplicationObject
{
	NLS_STATUS ("OBJ-0001"),
	PHONE_SCREEN_STATUS ("OBJ-0002"),
	INTERVIEW_STATUS ("OBJ-0003"),
	INTERVIEW_MATERIALS_STATUS ("OBJ-0004"),
	REQUISITION_STATUS ("OBJ-0005"),
	PHONE_SCREEN_DISPOSITION ("OBJ-0021"),
	ORGANIZATION_UNIT ("OBJ-0006"),
	DISTRICT ("OBJ-0007"),
	REGION ("OBJ-0008"),
	DIVISION ("OBJ-0009"),
	PHONE_SCREEN ("OBJ-0010"),
	REQUISITION ("OBJ-0011"),
	REQUISITION_CALENDAR ("OBJ-0012"),
	HIRING_EVENT ("OBJ-0013"),
	STORE ("OBJ-0014"),
	ENCRYPT_CONFIG ("OBJ-0015"),
	ENCRYPT_KEY_CLASS ("OBJ-0016"),
	REQUISITION_HIRING_EVENT ("OBJ-0012"),
	CANDIDATE ("OBJ-0100"),
	BGC_CNSNT_DT ("OBJ-0200");
	
	
	/** Object identifier */
	private String mObjectIdentifier;
	
	/*
	 * Constructor
	 *  
	 * @param identifier the object identifier
	 */
	private ApplicationObject(String identifier)
	{
		mObjectIdentifier = identifier;
	} // end constructor
	
	/**
	 * @return the object identifier
	 */
	public String getObjectIdentifier()
	{
		return mObjectIdentifier; 
	} // end function getObjectIdentifier()
} // end enumeration
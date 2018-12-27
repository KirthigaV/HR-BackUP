package com.homedepot.hr.hr.staffingforms.enumerations;
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
	STORE ("OBJ-0001"),
	CALENDAR("OBJ-0002"),
	REQUISITION("OBJ-0003"),
	HIRING_EVENT("OBJ-0004");
	
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
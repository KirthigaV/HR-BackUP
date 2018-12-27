package com.homedepot.hr.hr.retailstaffing.enumerations;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StatusType.java
 * Application: RetailStaffing
 */

/** enumeration representing a type of NLS status (N_PHN_SCRN_STAT, N_REQN_STAT, etc.) */
public enum StatusType
{
	PHONE_SCREEN_STATUS, // N_PHN_SCRN_STAT
	INTERVIEW_STATUS, // N_INTVW_RSPN_STAT
	MATERIALS_STATUS, // N_INTVW_MAT_STAT
	REQUISITION_STATUS; // N_REQN_STAT 
} // end enumeration
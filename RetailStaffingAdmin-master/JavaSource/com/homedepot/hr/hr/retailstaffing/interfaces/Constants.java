package com.homedepot.hr.hr.retailstaffing.interfaces;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingConstants.java
 * Application: RetailStaffing
 */

/**
 * Interface containing COMMON constants used throughout the 
 * application
 */
public interface Constants
{	
	/**
	 * Amount of time (in milliseconds) that data cached by this application
	 * will be cached for. 
	 */
	public static final long DEFAULT_CACHE_DURATION_IN_MILLIS = 14400000;
	
	/** Empty Space String */
	public static final String EMPTY_SPACE = " ";
	/** Delimiter string used to concatenate external candidate names into a "full name" */
	public static final String NAME_DELIMITER = ", ";	
	/** total number of nanoseconds in a second */
	public static final double NANOS_IN_SECOND = 1000000000.0d;		
	
	/** default calendar type code */
	public static final short REQN_CAL_TYP_DEFAULT = 1;
	/** MET calendar type code */
	public static final short REQN_CAL_TYP_MET = 2;
	/** Special calendar type code */
	public static final short REQN_CAL_TYP_SPECIAL = 10;
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	public static String ARIZONA_ZONE_CD = "13005";
	public static String EASTERN_ZONE_CD = "13411";
	public static String MOUNTAIN_ZONE_CD = "13406";
	public static String PACIFIC_ZONE_CD = "13404";
	public static String CENTRAL_ZONE_CD = "13407";
	public static String CENTRALL_ZONE_CD = "13007";
	public static String ALASKA_ZONE_CD = "13403";
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
	
	public static String API_KEY = "Api_key";
	public static String BGC_QA_API_TOKEN = "63b1d361-d7d2-4724-91a9-6197aa6b3416";
	public static String BGC_PR_API_TOKEN = "ccc87673-50f2-4610-b1a1-6343eb802c4d";
} // end interface Constants
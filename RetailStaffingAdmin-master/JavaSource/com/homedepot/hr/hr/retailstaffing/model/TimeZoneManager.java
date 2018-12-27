package com.homedepot.hr.hr.retailstaffing.model;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: TimeZoneManager.java
 * 
 * Application: RetailStaffing
 *
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.OrgParamTO;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.ta.aa.dao.basic.TimeLimitedMap;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains methods related to retrieving time zone information 
 */
public class TimeZoneManager implements RetailStaffingConstants
{
	// amount of time time zone information will be cached before the cache is cleared (4 hours)
	private static final long CACHE_INTERVAL = 14400000;
	
	// hr_org_parm.sub_sys_cd value for time zone entries
	private static final String HR_SUBSYS_CD = "HR";
	// hr_org_parm.bu_id value for time zone entries
	private static final String BU = "1";
	// hr_org_parm.prcss_typ_ind for time zone entries
	private static final String SVC_TYP_IND = "S";
	// hr_org_parm.parm_id format key for time zone entries
	private static final String TZ_KEY_FORMAT_STRING = "time.zone.%1$s";
	
	/*
	 * This is a timed collection that will be used to cache time zone data for CACHE_INTERVAL
	 */
	private static Map<String, String> mTimeZoneCache = 
		new TimeLimitedMap<String, String>(Collections.synchronizedMap(new HashMap<String, String>()), CACHE_INTERVAL);
	
	/*
	 * Logger instance
	 */
	private static final Logger mLogger = Logger.getLogger(TimeZoneManager.class);
	
	/**
	 * This method is used to look up time zone "descriptions" from the hr_org_parm table for a time zone key
	 * retrieved from one of the human resources system tables 
	 * 
	 * @param timezoneKey					time zone key
	 * 
	 * @return								description from the hr_org_table for the time zone key or null
	 * 										if the time zone key provided does not exist
	 * 
	 * @throws IllegalArgumentException		Thrown if a null or empty time zone key was provided
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database
	 */
	public static String getTimeZone(final String timezoneKey) throws IllegalArgumentException, QueryException
	{
		String timeZoneDesc = null;
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getTimeZone(), timezoneKey: %1$s", timezoneKey));
		} // end if
		
		try
		{
			// validate the time zone key passed in is not null or empty
			if(timezoneKey == null || timezoneKey.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s time zone key provided", (timezoneKey == null ? "Null" : "Empty")));
			} // end if(timezoneKey == null || timezoneKey.trim().length() == 0)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Checking the cache for time zone key %1$s", timezoneKey));
			} // end if
			
			// check the cache to see if it already contains the time zone information for this key
			timeZoneDesc = mTimeZoneCache.get(timezoneKey.trim());
			
			// if the time zone description is null (or empty) then assume the time zone has not been added to the cache and query for it
			if(timeZoneDesc == null || timeZoneDesc.trim().length() == 0)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Time zone value for key %1$s not in the cache, checking database for %2$s", timezoneKey.trim(),
						String.format(TZ_KEY_FORMAT_STRING, timezoneKey.trim())));
				} // end if

				// invoke business logic that will query the database for the timezone key (not going to cache the parameter because we'll cache the value here)
				OrgParamTO tzParam = OrgParamManager.getOrgParam(HR_SUBSYS_CD, BU, String.format(TZ_KEY_FORMAT_STRING, timezoneKey.trim()), SVC_TYP_IND, false);
				
				// if the time zone parameter was found
				if(tzParam != null)
				{
					// if the character value for the time zone is not null or empty
					if(tzParam.getCharVal() != null && tzParam.getCharVal().trim().length() > 0)
					{
						// add it to the cache
						mTimeZoneCache.put(timezoneKey.trim(), tzParam.getCharVal().trim());
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("%1$s added to the time zone cache with a value of %2$s", timezoneKey.trim(), tzParam.getCharVal().trim()));
						} // end if
					} // end if(tzParam.getCharVal() != null && tzParam.getCharVal().trim().length() > 0)
					else
					{
						// log it so we know it's happening
						mLogger.error(String.format("Time zone value for key %1$s is %2$s", timezoneKey.trim(), (tzParam.getCharVal() == null ? "null" : "empty")));
					} // end else
				} // end if(tzParam != null)
				else
				{
					// log it so we know it's happening
					mLogger.error(String.format("No time zone data found for key %1$s", timezoneKey));
				} // end else
								
				// now that it's been added, get it out and return it (it could still be null)
				timeZoneDesc = mTimeZoneCache.get(timezoneKey.trim());
			} // end if(timeZoneDesc == null || timeZoneDesc.trim().length() == 0)
		} // end try
		catch(IllegalArgumentException iae)
		{
			// make sure the error gets logged
			mLogger.error(String.format("Illegal Argument Provided: %1$s", iae.getMessage()), iae);
			// throw the error
			throw iae;
		} // end catch
		catch(QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred retrieving time zone data for key %1$s", timezoneKey.trim()), qe);
			// throw the error
			throw qe;
		} // end catch
				
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getTimeZone(), timezoneKey: %1$s", timezoneKey));
		} // end if
		
		return timeZoneDesc;
	} // end function getTimeZone()
} // end class TimeZoneManager
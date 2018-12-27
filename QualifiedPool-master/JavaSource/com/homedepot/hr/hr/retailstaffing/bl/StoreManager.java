package com.homedepot.hr.hr.retailstaffing.bl;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StoreManager.java
 * Application: RetailStaffing
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;





import com.homedepot.hr.hr.retailstaffing.dao.StoreDAO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetails;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.QualifiedPoolMessage;
import com.homedepot.ta.aa.dao.basic.TimeLimitedMap;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to store data
 * 
 * @author rlp05
 */
public class StoreManager implements Constants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(StoreManager.class);
	
	// amount of time job category information will be cached before it is considered invalid (4 hours)
	private static final long CACHE_INTERVAL = 14400000;

	// initialize a map that will cache store information for 4 hours
	private static Map<String, StoreDetails> mStoreCache = new TimeLimitedMap<String, StoreDetails>(
		Collections.synchronizedMap(new HashMap<String, StoreDetails>()), CACHE_INTERVAL);
	
	// initialize a map that will cache store group codes for 4 hours
	private static Map<String, List<String>> mHrStoreGroupCodesCache = new TimeLimitedMap<String, List<String>>(
		Collections.synchronizedMap(new HashMap<String, List<String>>()), CACHE_INTERVAL);	
	
	/**
	 * Get store details for the store number provided
	 * 
	 * @param strNbr					The store number
	 * 
	 * @return							Store details for the store number provided
	 * 
	 * @throws QualifiedPoolException	Thrown if any of the following conditions are true:
	 * 									<ul>
	 * 										<li>The store number provided is null or empty</li>
	 * 										<li>An exception occurs querying the database for store details</li>
	 * 									<ul>
	 */
	public static StoreDetails getStoreDetails(String strNbr) throws QualifiedPoolException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getStoreDetails(), strNbr: %1$s", strNbr));
		} // end if
		StoreDetails details = null;

		try
		{
			// validate the store number provided is not null or empty
			if(strNbr == null || strNbr.trim().length() == 0)
			{
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_STRNBR, String.format("%1$s store number provided", (strNbr == null ? "Null" : "Empty")));
			} // end if(strNbr == null || strNbr.trim().length() == 0)
	
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Checking for store %1$s in the store cache", strNbr.trim()));
			} // end if
			// first try to get the store details from the cache
			details = mStoreCache.get(strNbr.trim());
			
			// if the store details were not in the cache
			if(details == null)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Store %1$s not found in the cache, querying the database for store details", strNbr.trim()));
				} // end if
				// invoke the DAO method to get the details for the store
				details = StoreDAO.getStoreDetails(strNbr);
				// add the details to the cache (no need to check for null here, if the store was not found an exception would be thrown
				mStoreCache.put(strNbr.trim(), details);
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Store %1$s added to the store cache", strNbr.trim()));
				} // end if
			} // end if(details == null)
		} // end try
		catch(QualifiedPoolException qpe)
		{
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(qpe.getMessage()));
			// make sure the error shows up in the log
			mLogger.error("An exception occurred validating input data", qpe);
			// throw the exception
			throw qpe;
		} // end catch
		catch(QueryException qe)
		{
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for store details for strNbr: %1$s", strNbr)));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for store details for strNbr: %1$s", strNbr), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getStoreDetails(), strNbr: %1$s", strNbr));
		} // end if
		return details;
	} // end function getStoreDetails()
	
	
	public static List<String> getHrStoreGroupCodes(String strNbr, String locTypCd, String strTypCd, String cntryCd) throws QualifiedPoolException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getHrStoreGroupCodes(), strNbr: %1$s, locTypCd: %2$s, strTypCd: %3$s, cntryCd: %4$s", strNbr, locTypCd, strTypCd, cntryCd));
		} // end if
		List<String> details = null;
	//	details.add("0001");
	//	details.add("0009");

		try
		{
			// validate the inputs provided are not null or empty
			if(strNbr == null || strNbr.trim().length() == 0 || locTypCd == null || locTypCd.trim().length() == 0 || 
					strTypCd == null || strTypCd.trim().length() == 0 || cntryCd == null || cntryCd.trim().length() == 0)
			{
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_STRNBR, String.format("%1$s store number provided, %2$s loc type code provided, "
						+ "%3$s store type code provided, %4$s country code provided", (strNbr == null ? "Null" : "Empty"), (locTypCd == null ? "Null" : "Empty")
						, (strTypCd == null ? "Null" : "Empty"), (cntryCd == null ? "Null" : "Empty")));
			} // end if
	
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Checking for store %1$s in the store group code cache", strNbr.trim()));
			} // end if
			// first try to get the store group codes from the cache
			details = mHrStoreGroupCodesCache.get(strNbr.trim());
			
			// if the store details were not in the cache
			if(details == null)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Store %1$s group codes not found in the cache, querying the database for details", strNbr.trim()));
				} // end if
				// invoke the DAO method to get the details for the store 
				details = StoreDAO.getHrStoreGroupCodesDAO20(locTypCd, strTypCd, cntryCd);
				// add the details to the cache (no need to check for null here, if the store was not found an exception would be thrown
				mHrStoreGroupCodesCache.put(strNbr.trim(), details);
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Store %1$s added to the store group cache", strNbr.trim()));
				} // end if
			} // end if(details == null) 
		} 
		catch(QualifiedPoolException qpe)
		{
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(qpe.getMessage()));
			// make sure the error shows up in the log
			mLogger.error("An exception occurred validating input data", qpe);
			// throw the exception
			throw qpe;
		} // end catch
		catch(QueryException qe)
		{
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for store details for strNbr: %1$s", strNbr)));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for store details for strNbr: %1$s", strNbr), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getStoreDetails(), strNbr: %1$s", strNbr));
		} // end if
		return details;
	} // end function getStoreDetails()	
} // end class StoreManager
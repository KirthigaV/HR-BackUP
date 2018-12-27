package com.homedepot.hr.hr.retailstaffing.model;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StatusManager.java
 * Application: RetailStaffing
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.readers.StatusReader;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.StatusCacheKey;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.basic.TimeLimitedMap;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * Business logic module containing methods related to application status objects
 */
public class StatusManager implements Constants, DAOConstants, RetailStaffingConstants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(StatusManager.class);
	
	/** timed map used to cache status objects for a specified duration (4 hours by default) */
	private static Map<StatusType, Map<StatusCacheKey, Status>> mStatusCache = new TimeLimitedMap<StatusType, Map<StatusCacheKey,Status>>(
		Collections.synchronizedMap(new HashMap<StatusType, Map<StatusCacheKey, Status>>()),14400000);
	
	/**
	 * This method gets all statuses for the language code provided
	 * 
	 * @param langCd the language code (i.e. EN_US)
	 * 
	 * @return collection of status objects that match the criteria provided
	 * 
	 * @throws NoRowsFoundException thrown if status objects for any type could not be found for the language code provided
	 * @throws QueryException thrown if an exception occurs querying for the status objects
	 * @throws ValidationException thrown if the language code provided is null, empty, or does not match the pattern EN_US
	 */
	public static Map<StatusType, List<Status>> getAllStatusObjects(String langCd) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getAllStatusObjects(), langCd: %1$s", langCd));
		} // end if
		
		Map<StatusType, List<Status>> results = new HashMap<StatusType, List<Status>>();
		
		try
		{
			// validate the language code provided is valid
			ValidationUtils.validateLanguageCode(langCd);
			
			// iterate through the possible status types
			for(StatusType type : StatusType.values())
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Getting %1$s objects for language code %2$s", type, langCd));
				} // end if
				/*
				 * add all the statuses for the type and language to a list. There is no null check here. If no
				 * statuses were found for the type and language code provided a NoRowsFoundException would be thrown
				 * by the getStatusObject() method.
				 */
				List<Status> statuses = new ArrayList<Status>();
				statuses.addAll(getStatusObjects(type, langCd).values());
				// add the type/list to the results map
				results.put(type, statuses);
				
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Added %1$d %2$s objects with language code %3$s to the results collection", results.get(type).size(), type, langCd));
				} // end if
			} // end for(StatusType type : StatusType.values())
		} // end try
		catch(QueryException qe) // NoRowsFoundException and ValidationException extend QueryException
		{
			// make sure the exception gets logged
			mLogger.error(String.format("An exception occurred getting all status objects for language code %1$s", langCd), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			if(startTime == 0)
			{
				startTime = endTime;
			} // end if(startTime == 0)
			
			mLogger.debug(String.format("Exiting getAllStatusObjects(), langCd: %1$s. Returning %2$d types of status objects in %3$.9f seconds.",
				langCd, results.size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return results;
	} // end function getAllStatusObjects()
	
	/**
	 * This method gets the status object matching the criteria provided
	 * 
	 * @param type the type of status object to load, each type corresponds to a specific NLS table
	 * @param statusCode code identifier that identifies the status
	 * @param langCd the language code (i.e. EN_US)
	 * 
	 * @return Status object matching the criteria provided
	 * 
	 * @throws NoRowsFoundException thrown if a status object could not be found using the criteria provided
	 * @throws QueryException thrown if an exception occurred querying for the status object
	 * @throws ValidationException thrown if the type provided is null, the status code provided is &lt; 0 or the language code provided is null,
	 * 							   empty, or does not match the pattern EN_US
	 */
	public static Status getStatusObject(StatusType type, short statusCode, String langCd) throws QueryException
	{
		long startTime = 0;
		Status status = null;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getStatusObject(), type: %1$s, statusCode: %2$d, langCd: %3$s", type, statusCode, langCd));
		} // end if
		
		try
		{
			// validate the type provided is not null
			ValidationUtils.validateNotNull(InputField.STATUS_TYPE, type);
			// validate the status code provided is valid
			ValidationUtils.validateStatusCode(statusCode);
			// validate the language code provided is valid
			ValidationUtils.validateLanguageCode(langCd);
			
			// get the collection of status objects for the type and language provided
			Map<StatusCacheKey, Status> stats = getStatusObjects(type, langCd);
			
			// create a cache key using the criteria provided
			StatusCacheKey cacheKey = new StatusCacheKey(statusCode, langCd);
		
			/*
			 * use the key to get the status from the collection. No null check here, if no status
			 * objects were existed, the getStatusObjects() method would throw a NoRowsFoundException
			 */
			status = stats.get(cacheKey);

			// if the status is null (not found), throw an exception
			if(status == null)
			{
				switch(type)
				{
					case PHONE_SCREEN_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.PHONE_SCREEN_STATUS, String.format("No phone screen status found with code %1$d and language code %2$s", 
							statusCode, langCd));
					} // end case PHONE_SCREEN_STATUS
					case INTERVIEW_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.INTERVIEW_STATUS, String.format("No interview status found with code %1$d and language code %2$s", 
							statusCode, langCd));
					} // end case 
					case MATERIALS_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.INTERVIEW_MATERIALS_STATUS, String.format("No interview materials status found with code %1$d and language code %2$s", 
							statusCode, langCd));
					} // end case MATERIAL_STATUS
					case REQUISITION_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.REQUISITION_STATUS, String.format("No requisition status found with code %1$d and language code %2$s", 
							statusCode, langCd));
					} // end case REQUISITION_STATUS
				} // end switch(type)
			} // end if(status == null)
		} // end try
		catch(QueryException qe) // NoRowsFoundException and ValidationException extend QueryException
		{
			// make sure the exception gets logged
			mLogger.error(String.format("An exception occurred getting %1$s object with code %2$s for language code %3$s", type, statusCode, langCd), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			if(startTime == 0)
			{
				startTime = endTime;
			} // end if(startTime == 0)
			
			mLogger.debug(String.format("Exiting getStatusObject(), type: %1$s, statusCode: %2$d, langCd: %3$s. Returning status object in %4$.9f seconds",
				type, statusCode, langCd, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return status;
	} // end function getStatusObject()
	
	/**
	 * This method gets all status objects for the type and language provided
	 * 
	 * @param type the type of status object to load, each type corresponds to a specific NLS table
	 * @param langCd the language code (i.e. EN_US)
	 * 
	 * @return collection of status objects that match the criteria provided
	 * 
	 * @throws NoRowsFoundException thrown if no status objects are found for the criteria provided
	 * @throws QueryException thrown if an exception occurs querying the database for status objects
	 * @throws ValidationException thrown if the type provides is null or the language code provided is 
	 * 							   null, empty or does not match the pattern EN_US
	 */
	public static Map<StatusCacheKey, Status> getStatusObjects(StatusType type, String langCd) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getStatusObjects(), type: %1$s, langCd: %2$s", type, langCd));
		} // end if
		// return collection
		Map<StatusCacheKey, Status> results = new HashMap<StatusCacheKey, Status>();

		try
		{
			// validate the type provided is not null
			ValidationUtils.validateNotNull(InputField.STATUS_TYPE, type);
			// validate the language code provided is valid
			ValidationUtils.validateLanguageCode(langCd);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Checking cache for %1$s objects", type));				
			} // end if			
			
			// attempt to get status objects for the type provided from the cache
			Map<StatusCacheKey, Status> cachedStats = mStatusCache.get(type);
			
			// if status objects were not found in the cache
			if(cachedStats == null || cachedStats.isEmpty())
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("%1$s objects not found in cache, querying database", type));
				} // end if
				
				/*
				 * this could be for one of three reasons, either the cache has not been loaded, the cache expired, or the type
				 * provided has no records. To address the first two potential causes, we will call to load the status objects. 
				 */
				loadStatusObjects(type);
				
				/*
				 * Now try to get the objects from the cache again. Since the loadStatusObjects() method would've thrown a 
				 * NoRowsFoundException if no statuses were found for the type (the 3rd cause that could put us in this branch
				 * of logic), the collection should now contain at least one record at this point
				 */
				cachedStats = mStatusCache.get(type);				
			} // end if(cachedStats == null || cachedStats.isEmpty())
			
			// now iterate cached statuses, if we're here at least one status was found for the type provided
			for(Entry<StatusCacheKey, Status> entry : cachedStats.entrySet())
			{
				// if the entry, entry key, and entry key language code is not null (should never be)
				if(entry != null && entry.getKey() != null && entry.getKey().getLangCd() != null)
				{
					// if the entry key's language matches the language provided
					if(entry.getKey().getLangCd().equals(langCd))
					{
						// add it to the results list
						results.put(entry.getKey(), entry.getValue());
					} // end if(entry.getKey().getLangCd().equals(langCd))
				} // end if(entry != null && entry.getKey() != null && entry.getKey().getLangCd() != null)
			} // end for(Entry<StatusCacheKey, Status> entry : cachedStats.entrySet())
						
			// check the status map, if no entries matched the language code provided, throw an exception
			if(results.isEmpty())
			{
				switch(type)
				{
					case PHONE_SCREEN_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.PHONE_SCREEN_STATUS, String.format("No phone screen status objects found for language code %1$s", langCd));
					} // end case PHONE_SCREEN_STATUS
					case INTERVIEW_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.INTERVIEW_STATUS, String.format("No interview status objects found for language code %1$s", langCd));
					} // end case
					case MATERIALS_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.INTERVIEW_MATERIALS_STATUS, String.format("No interview materials status objects found for language code %1$s", langCd));
					} // end case MATERIAL_STATUS
					case REQUISITION_STATUS:
					{
						throw new NoRowsFoundException(ApplicationObject.REQUISITION_STATUS, String.format("No requisition status objects found for language code %1$s", langCd));
					} // end case REQUISITION_STATUS
				} // end switch(type)
			} // end if(results.isEmpty())
		} // end try
		catch(QueryException qe) // NoRowsFoundException and ValidationException extend QueryException
		{
			// make sure the exception gets logged
			mLogger.error(String.format("An exception occurred getting %1$s objects for language code %2$s", type, langCd), qe);
			// throw the error
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			if(startTime == 0)
			{
				startTime = endTime;
			} // end if(startTime == 0)
			
			mLogger.debug(String.format("Exiting getStatusObjects(), type: %1$s, langCd: %2$s. Returning %3$d status objects in %4$.9f seconds",
				type, langCd, results.size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return results;
	} // end function getStatusObjects()
	
	/**
	 * This method caches all NLS status objects for the type provided
	 * 
	 * @param type the type of status object to load, each type corresponds to a specific NLS table
	 * 
	 * @throws NoRowsFoundException thrown if no status objects could be loaded for the type provided
	 * @throws QueryException thrown if an exception occurs querying for status objects
	 * @throws ValidationException thrown if the type provided is null
	 */
	private static void loadStatusObjects(StatusType type) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering loadStatusObjects(), type: %1$s", type));
		} // end if
		
		try
		{
			// validate the type provided is not null
			ValidationUtils.validateNotNull(InputField.STATUS_TYPE, type);
			
			// create a ResultsReader object
			StatusReader reader = new StatusReader();
			
			MapStream inputs = new MapStream();
			ApplicationObject appObject = null;
			
			// use the type to determine which query to execute
			switch(type)
			{
				case PHONE_SCREEN_STATUS:
				{
					inputs.setSelectorName(READ_NLS_PHONE_SCREEN_STATUS);
					appObject = ApplicationObject.PHONE_SCREEN_STATUS;
					mLogger.debug("Executing query to get all phone screen statuses");
					break;
				} // end case PHONE_SCREEN_STATUS
				case INTERVIEW_STATUS:
				{
					inputs.setSelectorName(READ_NLS_INTERVIEW_STATUS);
					appObject = ApplicationObject.INTERVIEW_STATUS;
					mLogger.debug("Executing query to get all interview statuses");
					break;
				} // end case INTERVIEW_STATUS
				case MATERIALS_STATUS:
				{
					inputs.setSelectorName(READ_NLS_MATERIAL_STATUS);
					appObject = ApplicationObject.INTERVIEW_MATERIALS_STATUS;
					mLogger.debug("Executing query to get all interview material statuses");
					break;
				} // end case MATERIALS_STATUS
				case REQUISITION_STATUS:
				{					
					inputs.setSelectorName(READ_NLS_REQUISITION_STATUS);
					appObject = ApplicationObject.REQUISITION_STATUS;
					mLogger.debug("Executing query to get all requisition statuses");
					break;
				} // end case REQUISISTION_STATUS
			} // end switch(type)
			
			// execute the query
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, reader);
			
			// if no records were found, throw an exception
			if(reader.getStatuses() == null || reader.getStatuses().isEmpty())
			{
				throw new NoRowsFoundException(appObject, String.format("No %1$s objects found", type));
			} // end if(reader.getStatuses() == null || reader.getStatuses().isEmpty())
			
			// put the status objects we just read into the cache
			mStatusCache.put(type, reader.getStatuses());
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("%1$d %2$s objects added to the cache", reader.getStatuses().size(), type));
			} // end if
		} // end try
		catch(QueryException qe) // NoRowsFoundException and ValidationException extend QueryException
		{
			// make sure the exception gets logged
			mLogger.error(String.format("An exception occurred loading %1$s objects", type), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			if(startTime == 0)
			{
				startTime = endTime;
			} // end if(startTime == 0)
			
			mLogger.debug(String.format("Exiting loadStatusObjects(), type: %1$s. Loaded %2$d objects in %3$.9f seconds", 
				type, mStatusCache.get(type).size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end function loadStatuses()
} // end class StatusManager

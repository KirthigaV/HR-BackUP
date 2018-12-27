package com.homedepot.hr.hr.retailstaffing.bl;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: BackgroundCheckManager.java
 * Application: RetailStaffing
 */
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.BackgroundCheckDAO;
import com.homedepot.hr.hr.retailstaffing.dto.BackgroundCheckPackageKey;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.QualifiedPoolMessage;
import com.homedepot.ta.aa.dao.basic.TimeLimitedMap;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to background checks
 * 
 * @author rlp05
 */
public class BackgroundCheckManager implements Constants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(BackgroundCheckManager.class);
	
	// amount of time job category information will be cached before the cache is cleared (4 hours)
	private static final long CACHE_INTERVAL = 14400000;
	
	// initialize a map that will cache background check packages for 4 hours
	private static Map<BackgroundCheckPackageKey, Integer> mBGPackageCache = 
		new TimeLimitedMap<BackgroundCheckPackageKey, Integer>(Collections.synchronizedMap(new HashMap<BackgroundCheckPackageKey, Integer>()), CACHE_INTERVAL);

	/**
	 * Get the background package identifier for the job information provided. This method will first check it's cache
	 * for background package data. If the background package is not found in the cache, it will query for the background
	 * package information and store it in the cache. Background packages are cached for 24 hours.
	 * 
	 * @param strTypCd					Store type code for the job
	 * @param cntryCd					HR country code for the job
	 * @param deptNbr					HR department number for the job
	 * @param jobTtlCd					Title code for the job
	 * 
	 * @return							Background check package id for the job
	 * 
	 * @throws QualifiedPoolException	Thrown if any of the following conditions are true:
	 * 									<ul>
	 * 										<li>The store type code provided is null or empty</li>
	 * 										<li>The country code provided is null or empty</li>
	 * 										<li>The department number provided is null or empty</li>
	 * 										<li>The job title code provided is null or empty</li>
	 * 										<li>An exception occurs querying the database for the background package</li>
	 * 									<ul>
	 */
	public static int getBGCheckPkgForJob(String strTypCd, String cntryCd, String deptNbr, String jobTtlCd) throws QualifiedPoolException
	{		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getBGCheckPkgForJob(), strTypCd: %1$s, cntryCd: %2$s, deptNbr: %3$s, jobTtlCd: %4$s",
				strTypCd, cntryCd, deptNbr, jobTtlCd));
		} // end if
		int backgroundPkgId = 0;
		
		try
		{
			// first validate the store type code is not null or empty
			if(strTypCd == null || strTypCd.trim().length() == 0)
			{
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_REQNBR, String.format("%1$s store type code provided", (strTypCd == null ? "Null" : "Empty")));
			} // end if(strTypCd == null || strTypCd.trim().length() == 0)
			
			// next validate the country code is not null or empty
			if(cntryCd == null || cntryCd.trim().length() == 0)
			{
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_CNTRY_CD, String.format("%1$s country code provided", (cntryCd == null ? "Null" : "Empty")));			
			} // end if(cntryCd == null || cntryCd.trim().length() == 0)
			
			// next validate the department number is not null or empty
			if(deptNbr == null || deptNbr.trim().length() == 0)
			{
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_DEPT_NBR, String.format("%1$s department number provided", (deptNbr == null ? "Null" : "Empty")));			
			} // end if(deptNbr == null || deptNbr.trim().length() == 0)
			
			// next validate the job title code provided is not null or empty
			if(jobTtlCd == null || jobTtlCd.trim().length() == 0)
			{
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_JOB_TTL_CD, String.format("%1$s job title code provided", (jobTtlCd == null ? "Null" : "Empty")));						
			} // end if(jobTtlCd == null || jobTtlCd.trim().length() == 0)
			
			// now check for the background check package in the cache
			BackgroundCheckPackageKey key = new BackgroundCheckPackageKey(strTypCd, cntryCd, deptNbr, jobTtlCd);
			Integer pkgId = mBGPackageCache.get(key);
			
			// if the background package was in the cache
			if(pkgId != null)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Background package found in cache for strTypCd: %1$s, cntryCd: %2$s, deptNbr: %3$s, jobTtlCd: %4$s",
						strTypCd, cntryCd, deptNbr, jobTtlCd));
				} // end if
				// set the return value to the cached value
				backgroundPkgId = pkgId.intValue();
			} // end if(pkgId != null)
			else
			{
				// the background package is not in the cache
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Background package NOT in cache for strTypCd: %1$s, cntryCd: %2$s, deptNbr: %3$s, jobTtlCd: %4$s",
						strTypCd, cntryCd, deptNbr, jobTtlCd));
				} // end if
			
				// invoke the DAO method to get the background package id
				backgroundPkgId = BackgroundCheckDAO.getPackageForJob(strTypCd, cntryCd, deptNbr, jobTtlCd);
				// add the background package to the cache
				mBGPackageCache.put(key, backgroundPkgId);

				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Background package %1$d added to the cache cache for strTypCd: %2$s, cntryCd: %3$s, deptNbr: %4$s, jobTtlCd: %5$s",
						backgroundPkgId, strTypCd, cntryCd, deptNbr, jobTtlCd));
				} // end if
			} // end else
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
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for background package data for strTypCd: %1$s, cntryCd: %2$s, deptNbr: %3$s, jobTtlCd: %4$s",
				strTypCd, cntryCd, deptNbr, jobTtlCd)));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for background package data for strTypCd: %1$s, cntryCd: %2$s, deptNbr: %3$s, jobTtlCd: %4$s",
				strTypCd, cntryCd, deptNbr, jobTtlCd), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch						
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getBGCheckPkgForJob(), strTypCd: %1$s, cntryCd: %2$s, deptNbr: %3$s, jobTtlCd: %4$s",
				strTypCd, cntryCd, deptNbr, jobTtlCd));
		} // end if
		
		return backgroundPkgId;
	} // end function getBGCheckPkgForJob()
} // end class BackgroundCheckManager
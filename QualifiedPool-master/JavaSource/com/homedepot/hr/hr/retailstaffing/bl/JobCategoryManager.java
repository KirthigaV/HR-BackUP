package com.homedepot.hr.hr.retailstaffing.bl;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: JobCategoryManager.java
 * Application: RetailStaffing
 */
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.JobCategoryDAO;
import com.homedepot.hr.hr.retailstaffing.dto.JobCategoryDetails;
import com.homedepot.hr.hr.retailstaffing.dto.JobCategoryKey;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.QualifiedPoolMessage;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.TimeLimitedMap;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to job category data
 * 
 * @author rlp05
 */
public class JobCategoryManager implements Constants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(JobCategoryManager.class);
	
	// amount of time job category information will be cached before the cache is cleared (4 hours)
	private static final long CACHE_INTERVAL = 14400000;
	
	// list of position category codes, used to get the position preference code
	private static final List<Integer> POSN_CTGRY_CDS = new ArrayList<Integer>()
	{
        private static final long serialVersionUID = 7552685592837899405L;

		{
			add(1);
			add(2);
			add(3);
			add(101);
			add(4);
			add(8);
			add(9);
			add(900);
			add(901);
		} // end constructor block
	}; // end collection POSN_CTGRY_CDS
	
	// language code the description should be in (this is bad to have hard-coded)
	private static final String LANG_CD_ENUS = "EN_US";
	
	// initialize a map that will cache job category details for 4 hours
	private static Map<JobCategoryKey, JobCategoryDetails> mJobCategoryDetailsCache = 
		new TimeLimitedMap<JobCategoryKey, JobCategoryDetails>(Collections.synchronizedMap(new HashMap<JobCategoryKey, JobCategoryDetails>()), CACHE_INTERVAL);
	
	public static JobCategoryDetails getJobCategoryDetails(final String strNbr, final String deptNbr, final String jobTtlCd, final int tieringFlg) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getJobCategoryDetails(), strNbr: %1$s, deptNbr: %2$s, jobTtlCd: %3$s", strNbr, deptNbr, jobTtlCd));
		} // end if
		// response object
		JobCategoryDetails details = null;
		
		try
		{
			// first validate the store number is not null or empty
			if(strNbr == null || strNbr.trim().length() == 0)
			{
				throw new QualifiedPoolException(ERRCD_INVALID_STRNBR, String.format("%1$s store number provided", (strNbr == null ? "Null" : "Empty")));
			} // end if(strNbr == null || strNbr.trim().length() == 0)
			
			// next validate the department number is not null or empty
			if(deptNbr == null || deptNbr.trim().length() == 0)
			{
				throw new QualifiedPoolException(ERRCD_INVALID_DEPT_NBR, String.format("%1$s department number provided", (deptNbr == null ? "Null" : "Empty")));
			} // end if(deptNbr == null || deptNbr.trim().length() == 0)
			
			// next validate the job title code is not null or empty
			if(jobTtlCd == null || jobTtlCd.trim().length() == 0)
			{
				throw new QualifiedPoolException(ERRCD_INVALID_JOB_TTL_CD, String.format("%1$s job title code provided", (jobTtlCd == null ? "Null" : "Empty")));
			} // end if(jobTtlCd == null || jobTtlCd.trim().length() == 0)
			
			// now check for job category details in the cache
			JobCategoryKey key = new JobCategoryKey(strNbr, deptNbr, jobTtlCd);
			details = mJobCategoryDetailsCache.get(key);
			
			if(details == null)
			{
				// the job category details are not in the cache, so query for them and add them to the cache
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Job category details NOT in cache for strNbr: %1$s, deptNbr: %2$s, and jobTtlCd: %3$s",
						strNbr, deptNbr, jobTtlCd));
				} // end if
				
				// get an instance of the QueryManager
				QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
				// get the DAO connection from the QueryManager
				DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
				// have to have a final object to add the results of the inner class to
				final List<JobCategoryDetails> categoryList = new ArrayList<JobCategoryDetails>();
				
				// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
				UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, null, connection)
				{
					/*
					 * (non-Javadoc)
					 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
					 */
					@Override
                    public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
                    {
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug("Entering getJobCategoryDetails handleQuery() method");
							mLogger.debug(String.format("Getting primary job category information for store %1$s, department %2$s, and job title %3$s",
								strNbr, deptNbr, jobTtlCd));
						} // end if
						
						// TODO : THIS IS BAD, THIS SHOULD BE PROVIDED BY THE CALLING CLIENT NOT HARD-CODED! IF RSA EVER GOES INTERNATIONAL THIS HAS TO BE MODIFIED!
						// first get the primary job category information
						JobCategoryDetails details = JobCategoryDAO.getJobCategoryDetails(strNbr, deptNbr, jobTtlCd, LANG_CD_ENUS);
				
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Getting additonal job category information for store %1$s, department %2$s, and job title %3$s",
								strNbr, deptNbr, jobTtlCd));
						} // end if
						// next get any additional job codes (excluding the job category read in the last call)
						List<String> addlJobCodes = JobCategoryDAO.getAdditionalJobCodes(strNbr, details.getConsentDecreeJobCtgryCd(), jobTtlCd);
						
						// if additional job codes were returned
						if(addlJobCodes != null && addlJobCodes.size() > 0)
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Additional job category information found for store %1$s, department %2$s, and job title %3$s",
									strNbr, deptNbr, jobTtlCd));
							} // end if
							
							// iterate and add them to the job category object
							for(String addlJobCode : addlJobCodes)
							{
								details.addJobCtgryCd(addlJobCode);
							} // end for(String addlJobCode : addlJobCodes)
						} // end if(addlJobCodes != null && addlJobCodes.size() > 0)		

						// grab the current date
						Date current = new Date(Calendar.getInstance().getTimeInMillis());
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Getting the position preference code for store %1$s, department %2$s, and job title %3$s",
								strNbr, deptNbr, jobTtlCd));							
						} // end if

						// lastly get the position preference code for the store/dept/job title
						details.setPosnCd(JobCategoryDAO.getPositionPreferenceCode(strNbr, deptNbr, jobTtlCd, POSN_CTGRY_CDS, current, current));

						// add the details to the list so they can be accessed outside this class
						categoryList.add(details);
                    } // end function handleQuery()					
				}; // end UniversalConnectionHandler implementation
				
				// execute the connection handler
				connectionHandler.execute();	
				// get the details we just read
				details = categoryList.get(0);
				// add to the cache
				mJobCategoryDetailsCache.put(key, details);	
				
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Job category details for strNbr: %1$s, deptNbr: %2$s,and jobTtlCd: %3$s added to cache", strNbr, deptNbr, jobTtlCd));
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
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for job category information for strNbr: %1$s, deptNbr: %2$s, and jobTtlCd: %3$s", 
				strNbr, deptNbr, jobTtlCd)));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for store details for strNbr: %1$s", strNbr), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getJobCategoryDetails(), strNbr: %1$s, deptNbr: %2$s, jobTtlCd: %3$s", strNbr, deptNbr, jobTtlCd));
		} // end if		
		
		return details;
	} // end function getJobCategoryDetails() 
} // end class JobCategoryManager
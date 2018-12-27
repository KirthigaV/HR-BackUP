package com.homedepot.hr.hr.retailstaffing.dao;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: BackgroundCheckDAO.java
 * Application: RetailStaffing
 */
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains data access methods related to background checks
 * 
 * @author rlp05
 */
public class BackgroundCheckDAO implements Constants
{
	// selector names
	private static final String SELECTOR_READ_BGCHECK_PKG = "readBackgroundCheckJobRequirement";
	// column names
	private static final String COL_STORE_TYP_CD = "humanResourcesStoreTypeCode";
	private static final String COL_HR_CNTRY_CD = "humanResourcesSystemCountryCode";
	private static final String COL_HR_DEPT_NBR = "humanResourcesSystemDepartmentNumber";
	private static final String COL_JOB_TTL_CD = "jobTitleCode";	
	private static final String COL_BGCHECK_PKG_ID = "backgroundCheckSystemPackageId";	
	
	/**
	 * Get the background package identifier for the job information provided. 
	 * 
	 * @param strTypCd				Store type code for the job
	 * @param cntryCd				HR country code for the job
	 * @param deptNbr				HR department number for the job
	 * @param jobTtlCd				Title code for the job
	 * 
	 * @return						Background check package id for the job
	 * 
	 * @throws QueryException		Thrown if an exception occurs querying the database
	 */	
	public static int getPackageForJob(String strTypCd, String cntryCd, String deptNbr, String jobTtlCd) throws QueryException
	{
		final int[] packageIds = new int[1];
		
		// create the MapStream used to pass the input values to the DAO selector
		MapStream inputs = new MapStream(SELECTOR_READ_BGCHECK_PKG);
		// add the input parameters
		inputs.put(COL_STORE_TYP_CD, strTypCd);
		inputs.put(COL_HR_CNTRY_CD, cntryCd);
		inputs.put(COL_HR_DEPT_NBR, deptNbr);
		inputs.put(COL_JOB_TTL_CD, jobTtlCd);

		// invoke the query using the DAO
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			/*
			 * (non-Javadoc)
			 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				// if a result was returned
				if(results.next())
				{
					// add the background package to the map
					packageIds[0] = results.getInt(COL_BGCHECK_PKG_ID);
				} // end if(results.next())
				else
				{
					// no background package found for inputs, throw a query exception
					throw new QueryException(String.format("No background package found for store type %1$s, country code: %2$s, department %3$s and job title %4$s",
						inputs.getNVStream().getString(COL_STORE_TYP_CD), inputs.getNVStream().getString(COL_HR_CNTRY_CD), inputs.getNVStream().getString(COL_HR_DEPT_NBR),
						inputs.getNVStream().getString(COL_JOB_TTL_CD)));
				} // end else
			} // end function readResults()
		}); // end ResultsReader implementation

		return packageIds[0];
	} // end function getPackageForJob()
} // end class BackgroundCheckDAO
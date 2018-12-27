package com.homedepot.hr.hr.retailstaffing.dao;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: JobCategoryDAO.java
 * Application: RetailStaffing
 */

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.JobCategoryDetails;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains data access methods related to job category data
 * 
 * @author rlp05
 */
public class JobCategoryDAO implements Constants
{
	// selector constants
	private static final String SELECTOR_READ_PRIM_JOB_CAT = "readPrimaryCategoryCodeByStoreNumberDepartmentAndJobTitle";
	private static final String SELECTOR_READ_ADDL_JOB_CAT = "readAssociateJobCodesByStoreNumberDepartmentAndJobTitle";
	private static final String SELECTOR_READ_APLCNT_POSN_PREF = "readApplicantPositionPreferenceCode";
	// column constants
	private static final String COL_HR_DEPT_NBR = "humanResourcesSystemDepartmentNumber";
	private static final String COL_JOB_TTL_CD = "jobTitleCode";
	private static final String COL_HR_STR_NBR = "humanResourcesSystemStoreNumber";
	private static final String COL_LANG_CD = "languageCode";
	private static final String COL_CDECRE_JCTGRY_CD = "consentDecreeJobCategoryCode";
	private static final String COL_POSN_CTGRY_CD = "positionCategoryCode";
	private static final String COL_POSN_CTGRY_CD_LIST = "positionCategoryCodeList";
	private static final String COL_EFF_BGN_DT = "effectiveBeginDate";
	private static final String COL_EFF_END_DT = "effectiveEndDate";
	private static final String COL_TBLNO = "tblno";
	private static final String COL_EXCL_JOB_CODE = "excludedJobCode";
	private static final String COL_GIVEN_JOB_CODE = "givenJobCode";
	private static final String COL_JOB_CODE = "jobCode";
	private static final String COL_POSN_CD = "positionCode";
	private static final String COL_CDECRE_JCTGRY_DESC = "consentDecreeJobCategoryDescription";
	private static final String COL_EMPTST_ID = "employmentTestId";
	
	// additional job category table "name"
	private static final int ADDITIONAL_JOB_TABLE_NBR = 10197;
	
	/**
	 * Get primary job category details for the store, department and job title provided
	 * 
	 * @param strNbr			The store number
	 * @param deptNbr			The department number
	 * @param jobTtlCd			Job title code
	 * @param langCd			The language code the description should be in
	 * 
	 * @return					Job category details for the store, department and job title provided
	 * 
	 * @throws QueryException	Throw if an exception occurs querying the database for job category data or
	 * 							if no job category information was found
	 */
	public static JobCategoryDetails getJobCategoryDetails(final String strNbr, final String deptNbr, final String jobTtlCd, final String langCd) throws QueryException
	{
		final JobCategoryDetails details = new JobCategoryDetails();
		
		// create the MapStream used to pass the input values to the DAO selector		
		MapStream inputs = new MapStream(SELECTOR_READ_PRIM_JOB_CAT);
		// add the input parameters		
		inputs.put(COL_HR_STR_NBR, strNbr);
		inputs.put(COL_HR_DEPT_NBR, deptNbr);
		inputs.put(COL_JOB_TTL_CD, jobTtlCd);
		inputs.put(COL_LANG_CD, langCd);
		
		// invoke the query using the DAO
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			/*
			 *(non-Javadoc)
			 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
            {
				// if a record was returned, populate the job category details object
				if(results.next())
				{
					details.setConsentDecreeJobCtgryCd(results.getString(COL_CDECRE_JCTGRY_CD));
					details.setPrimPosnCtgryCd(results.getInt(COL_POSN_CTGRY_CD));
					details.setConsentDecreeJobCtgryDesc(results.getString(COL_CDECRE_JCTGRY_DESC));
					details.setTieringCtgryCd(results.getShort(COL_EMPTST_ID));
				} // end if(results.next())
				else
				{
					throw new QueryException(String.format("Job category information not found for store %1$s, department %2$s, and job title %3$s",
						strNbr, deptNbr, jobTtlCd));
				} // end else
            } // end function readResults()	
		}); // end ResultsReader implementation
		
		return details;
	} // end function getJobCategoryDetails()
	
	/**
	 * Get additional job codes for the store and job title provided
	 * 
	 * @param strNbr			The store number
	 * @param cdecreCtrgyCd		Consent/Decree job category code to be excluded from the list of job codes returned
	 * @param jobTtlCd			Job title code
	 * 
	 * @return					A list of additional job codes for the store and job title provided. If there are not any
	 * 							additional job codes, an empty list will be returned
	 * 
	 * @throws QueryException	Thrown if an exception occurs querying the database for additional job codes
	 */
	public static List<String> getAdditionalJobCodes(String strNbr, String cdecreCtrgyCd, String jobTtlCd) throws QueryException
	{
		final List<String> additionalCodes = new ArrayList<String>();
		
		// create the MapStream used to pass the input values to the DAO selector
		MapStream inputs = new MapStream(SELECTOR_READ_ADDL_JOB_CAT);
		// add the input parameters
		inputs.put(COL_TBLNO, ADDITIONAL_JOB_TABLE_NBR);
		inputs.put(COL_HR_STR_NBR, strNbr);
		inputs.put(COL_EXCL_JOB_CODE, cdecreCtrgyCd);
		inputs.put(COL_GIVEN_JOB_CODE, jobTtlCd);
		
		// invoke the query using the DAO
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			/*
			 * (non-Javadoc)
			 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				// iterate the results returned and add them to the list going back
				while(results.next())
				{
					additionalCodes.add(results.getString(COL_JOB_CODE));
				} // end while(results.next())
			} // end function readResults()
		}); // end ResultsReader implementation
		
		return additionalCodes;
	} // end function getAdditionalJobCodes()
	
	/**
	 * Get position preference code for a store/department/job title
	 * 
	 * @param strNbr				The store number
	 * @param deptNbr				The department number
	 * @param jobTtlCd				The job title code
	 * @param posnCtgryCds			Collection of position category codes
	 * @param effBgnDt				Effective begin date
	 * @param effEndDt				Effective end date
	 * 
	 * @return						The position preference code for a store/department/job title
	 * 
	 * @throws QueryException		Thrown if an exception occurs querying for the position preference code
	 * 								or if no position preference code was found
	 */
	public static short getPositionPreferenceCode(final String strNbr, final String deptNbr, final String jobTtlCd, List<Integer> posnCtgryCds, 
		Date effBgnDt, Date effEndDt) throws QueryException
	{
		// this will only ever return a single record, but to simplify reading with the DAO we'll use a list here
		final List<Short> positionCodes = new ArrayList<Short>();
		
		// create the MapStream used to pass the input values to the DAO selector
		MapStream inputs = new MapStream(SELECTOR_READ_APLCNT_POSN_PREF);
		// add the input parameters
		inputs.put(COL_HR_DEPT_NBR, deptNbr);
		inputs.put(COL_JOB_TTL_CD, jobTtlCd);
		inputs.put(COL_POSN_CTGRY_CD_LIST, posnCtgryCds);
		inputs.put(COL_EFF_BGN_DT, effBgnDt);
		inputs.put(COL_EFF_END_DT, effEndDt);
		inputs.put(COL_HR_STR_NBR, strNbr);
		
		// invoke the query using the DAO
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{			
			/*
			 * (non-Javadoc)
			 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				// if a record was returned, populate the position code
				if(results.next())
				{
					positionCodes.add(results.getShort(COL_POSN_CD));
				} // end if(results.next())
				else
				{
					throw new QueryException(String.format("Position preference code not found for store %1$s, department %2$s, and job title %3$s",
						strNbr, deptNbr, jobTtlCd));
				} // end else			
			} // end function readResults()
		});
		
		// if we get here then a row was found, so return it
		return positionCodes.get(0);
	} // end function getPositionPreferenceCode()
} // end class JobCategoryDAO
package com.homedepot.hr.hr.retailstaffing.dao;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionManager.java
 * Application: RetailStaffing
 */
import com.homedepot.hr.hr.retailstaffing.dto.Requisition;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains data access methods related to employment requisitions
 * 
 * @author rlp05
 */
public class RequisitionDAO implements Constants
{
	// selector constants
	private static final String SELECTOR_READ_REQUISITION = "readHumanResourcesStoreEmploymentRequisition";
	// column constants
	private static final String COL_REQ_NBR = "employmentRequisitionNumber";
	private static final String COL_HR_STORE_NBR = "humanResourcesSystemStoreNumber";
	private static final String COL_CRT_TS = "createTimestamp";
	private static final String COL_CRT_USERID = "createUserId";
	private static final String COL_HR_DEPT_NBR = "humanResourcesSystemDepartmentNumber";
	private static final String COL_JOB_TTL_CD = "jobTitleCode";
	private static final String COL_REQD_POSN_FILL_DT = "requiredPositionFillDate";
	private static final String COL_AUTH_POSN_COUNT = "authorizationPositionCount";
	private static final String COL_OPEN_POSN_COUNT = "openPositionCount";
	private static final String COL_FT_REQD_FLG = "fullTimeRequiredFlag";
	private static final String COL_PT_REQD_FLG = "partorpeakTimeRequiredFlag";
	private static final String COL_PM_REQD_FLG = "pmRequiredFlag";
	private static final String COL_WKND_REQD_FLG = "weekendRequiredFlag";
	private static final String COL_ACTV_FLG = "activeFlag";
	private static final String COL_LAST_UPD_USERID = "lastUpdateUserId";
	private static final String COL_LAST_UPD_TIME = "lastUpdateTimestamp";	
	
	/**
	 * Get the employment requisition for the identifier provided
	 * 
	 * @param requisitionNbr				Unique identifier for the employment requisition
	 * 
	 * @return								Employment requisition for the identifier provided
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database for the requisition
	 */	
	public static Requisition getRequisition(final int requisitionNbr) throws QueryException
	{
		// transfer object that will house requisition details
		final Requisition requisition = new Requisition();

		// create the MapStream used to pass the input values to the DAO selector
		MapStream inputs = new MapStream(SELECTOR_READ_REQUISITION);
		// add the requisition input parameter
		inputs.put(COL_REQ_NBR, requisitionNbr);
		
		// invoke the query using the DAO
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			/*
			 * (non-Javadoc)
			 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{		
				// if a record was returned, populate a requisition object
				if(results != null && results.next())
				{
					requisition.setNumber(inputs.getNVStream().getInt(COL_REQ_NBR));
					requisition.setStoreNbr((results.getString(COL_HR_STORE_NBR) == null ? null : results.getString(COL_HR_STORE_NBR).trim()));				
					requisition.setCrtTs(results.getTimestamp(COL_CRT_TS));
					requisition.setCrtUserId(results.getString(COL_CRT_USERID));
					requisition.setDeptNbr((results.getString(COL_HR_DEPT_NBR) == null ? null : results.getString(COL_HR_DEPT_NBR).trim()));
					requisition.setJobTtlCd((results.getString(COL_JOB_TTL_CD) == null ? null : results.getString(COL_JOB_TTL_CD).trim()));
					requisition.setReqdPosnFillDt(results.getDate(COL_REQD_POSN_FILL_DT));
					requisition.setAuthPosnCount(results.getShort(COL_AUTH_POSN_COUNT));
					requisition.setOpenPosnCount(results.getShort(COL_OPEN_POSN_COUNT));
					requisition.setFtReqdFlg(Boolean.valueOf(results.getString(COL_FT_REQD_FLG)));
					requisition.setPtReqdFlg(Boolean.valueOf(results.getString(COL_PT_REQD_FLG)));
					requisition.setPmReqdFlg(Boolean.valueOf(results.getString(COL_PM_REQD_FLG)));
					requisition.setWkndReqdFlg(Boolean.valueOf(results.getString(COL_WKND_REQD_FLG)));
					requisition.setActvFlg(results.getString(COL_ACTV_FLG));
					requisition.setLastUpdUserId(results.getString(COL_LAST_UPD_USERID));
					requisition.setLastUpdTs(results.getTimestamp(COL_LAST_UPD_TIME));			
				} // end if(results != null && results.next())
				else
				{
					// if no records were returned, throw an exception
					throw new QueryException(String.format("Requisition %1$d not found", inputs.getNVStream().getInt(COL_REQ_NBR)));
				} // end else
			} // end function readResults()
		}); // end ResultsReader implementation
		
		return requisition;
	} // end function getRequisition()
} // end class RequisitionDAO
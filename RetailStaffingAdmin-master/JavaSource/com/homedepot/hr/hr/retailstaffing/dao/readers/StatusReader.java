package com.homedepot.hr.hr.retailstaffing.dao.readers;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StatusReader.java
 * Application: RetailStaffing
 */

import java.util.HashMap;
import java.util.Map;

import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.StatusCacheKey;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.annotations.AnnotatedResultsReader;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;

import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * DAO Reader that processes all NLS status queries
 */
public class StatusReader extends AnnotatedResultsReader implements DAOConstants
{
	/** Collection of status objects */
	private Map<StatusCacheKey, Status> mStatuses = null;
	
	/**
	 * Process the results of the "readNlsPhoneScreenStatus" DAO selector
	 * 
	 * @param results DAO Infrastructure results object
	 * @param query DAO Infrastructure query object
	 * @param inputs DAO Infrastructure results object
	 * 
	 * @throws QueryException thrown if an exception occurs in the DAO Infrastructure
	 */
	@QuerySelector(name=READ_NLS_PHONE_SCREEN_STATUS, operation=QueryMethod.getResult)
	public void readPhoneScreenStatuses(Results results, Query query, Inputs inputs) throws QueryException
	{
		readStatusObjects(PHN_SCRN_STAT_CD, results);
	} // end function readPhoneScreenStatuses()
	
	/**
	 * Process the results of the "readNlsInterviewRespondStatusList" DAO selector
	 * 
	 * @param results DAO Infrastructure results object
	 * @param query DAO Infrastructure query object
	 * @param inputs DAO Infrastructure results object
	 * 
	 * @throws QueryException thrown if an exception occurs in the DAO Infrastructure
	 */
	@QuerySelector(name=READ_NLS_INTERVIEW_STATUS, operation=QueryMethod.getResult)
	public void readInterviewStatuses(Results results, Query query, Inputs inputs) throws QueryException
	{
		readStatusObjects(INTVW_RSPN_STAT_CD, results);
	} // end function readInterviewStatuses()
	
	/**
	 * Process the results of the "readNlsInterviewMaterialStatus" DAO selector
	 * 
	 * @param results DAO Infrastructure results object
	 * @param query DAO Infrastructure query object
	 * @param inputs DAO Infrastructure results object
	 * 
	 * @throws QueryException thrown if an exception occurs in the DAO Infrastructure
	 */
	@QuerySelector(name=READ_NLS_MATERIAL_STATUS, operation=QueryMethod.getResult)
	public void readMaterialsStatuses(Results results, Query query, Inputs inputs) throws QueryException
	{
		readStatusObjects(INTVW_MAT_STAT_CD, results);
	} // end function readMaterialsStatus()
	
	/**
	 * Process the results of the "readRequisitionStatus" DAO selector
	 * 
	 * @param results DAO Infrastructure results object
	 * @param query DAO Infrastructure query object
	 * @param inputs DAO Infrastructure results object
	 * 
	 * @throws QueryException thrown if an exception occurs in the DAO Infrastructure
	 */
	@QuerySelector(name=READ_NLS_REQUISITION_STATUS, operation=QueryMethod.getResult)
	public void readRequisitionStatuses(Results results, Query query, Inputs inputs) throws QueryException
	{
		readStatusObjects(REQN_STAT_CD, results);
	} // end function readRequisitionStatuses()
	
	/*
	 * Convenience method used by all of the "readStatus" methods that processes the query results
	 *  
	 * @param codeField name of the status code field (changes based on the NLS table being queried)
	 * @param results DAO ResultSet object
	 * 
	 * @throws QueryException thrown if an exception occurs in the DAO infrastructure
	 */
	private void readStatusObjects(String codeField, Results results) throws QueryException
	{
		// initialize the status map
		mStatuses = new HashMap<StatusCacheKey, Status>();
		
		StatusCacheKey cacheKey = null;
		Status status = null;
		
		// iterate while there are records
		while(results.next())
		{
			// initialize the status object
			status = new Status();
			status.setLastUpdSysUsrId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
			status.setLastUpdTs(results.getDate(LAST_UPD_TS));
			status.setDispStatCd(StringUtils.trim(results.getString(D_STAT_CD)));
			status.setShortStatDesc(StringUtils.trim(results.getString(S_STAT_DESC)));
			status.setStatDesc(StringUtils.trim(results.getString(STAT_DESC)));
			status.setCode(results.getShort(codeField));
			status.setLangCd(StringUtils.trim(results.getString(LANG_CD)));
			// initialize the cache key
			cacheKey = new StatusCacheKey(status.getCode(), status.getLangCd());
			// add it to the collection
			mStatuses.put(cacheKey, status);
		} // end while(results.next())
	} // end function readStatusObjects()
	
	/**
	 * @return collection of status objects
	 */
	public Map<StatusCacheKey, Status> getStatuses()
	{
		return mStatuses;
	} // end function getStatuses()
} // end class StatusReader
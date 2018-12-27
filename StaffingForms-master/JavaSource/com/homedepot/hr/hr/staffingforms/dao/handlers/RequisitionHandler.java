package com.homedepot.hr.hr.staffingforms.dao.handlers;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionHandler.java
 * Application: RetailStaffing
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.homedepot.hr.hr.staffingforms.dto.Requisition;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.util.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.annotations.AnnotatedQueryHandler;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * Annotated query handler used to process queries in complicated or transacted flows 
 */
public class RequisitionHandler extends AnnotatedQueryHandler implements DAOConstants
{
	/** collection of requisitions */
	private List<Requisition> mRequisitions;
	
	/** 
	 * collection of requisition calendar objects, this collection is used to 
	 * populate calendar details on requisition object(s) 
	 */
	private Map<Integer, RequisitionCalendar> mCalendars;
	
	/**
	 * This method processes the results of the "readHumanResourcesStoreAndThdStoreEmploymentRequisitionByInputList" selector
	 * 
	 * @param results DAO results object
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */	
	@QuerySelector(name = READ_ACTV_REQNS, operation = QueryMethod.getResult)
	public void readActiveRequisitionsForStore(Results results, Query query, Inputs inputs) throws QueryException
	{
		// initialize the collection
		mRequisitions = new ArrayList<Requisition>();
		Requisition requisition = null;
		
		// while there are records
		while(results.next())
		{
			// initialize the requisition object
			requisition = new Requisition();
			// populate the object
			requisition.setReqnNbr(results.getInt(EMPLT_REQN_NBR));
			requisition.setCrtTs(results.getTimestamp(CRT_TS));
			requisition.setCrtUserId(StringUtils.trim(results.getString(CRT_USER_ID)));
			requisition.setDeptNbr(StringUtils.trim(results.getString(HR_SYS_DEPT_NBR)));
			requisition.setJobTtlCd(StringUtils.trim(results.getString(JOB_TTL_CD)));
			requisition.setJobTtlDesc(StringUtils.trim(results.getString(JOB_TTL_DESC)));
			requisition.setReqdPosnFillDt(results.getDate(REQD_POSN_FILL_DT));
			requisition.setAuthPosnCnt(results.getShort(AUTH_POSN_CNT));
			requisition.setOpenPosnCnt(results.getShort(OPEN_POSN_CNT));
			requisition.setFtmReqd(results.getBoolean(FTM_REQD_FLG));
			requisition.setPtmReqd(results.getBoolean(PTM_REQD_FLG));
			requisition.setPmReqd(results.getBoolean(PM_REQD_FLG));
			requisition.setWkndReqd(results.getBoolean(WKND_REQD_FLG));
			requisition.setLastUpdUserId(StringUtils.trim(results.getString(LAST_UPD_USER_ID)));
			requisition.setLastUpdTs(results.getTimestamp(LAST_UPD_TS));
			requisition.setHireMgrNm(StringUtils.trim(results.getString(HIRE_MGR_NM)));
			requisition.setHireMgrTtl(StringUtils.trim(results.getString(HIRE_MGR_TTL)));
			requisition.setHireMgrPhnNbr(StringUtils.trim(results.getString(HIRE_MGR_PHN_NBR)));
			requisition.setRqstNbr(StringUtils.trim(results.getString(RQST_NBR)));
			requisition.setWkBgnDt(results.getDate(WEEK_BGN_DT));
			requisition.setHireMgrAvailTxt(StringUtils.trim(results.getString(HIRE_MGR_AVAIL_TXT)));
			
			if(!results.wasNull(TRGT_EXPR_LVL_CD))
			{
				requisition.setTrgtExprLvlCd(results.getShort(TRGT_EXPR_LVL_CD));
			} // end if(!results.wasNull(TRGT_EXPR_LVL_CD))
			
			if(!results.wasNull(TRGT_PAY_AMT))
			{
				requisition.setTrgtPayAmt(results.getBigDecimal(TRGT_PAY_AMT));
			} // end if(!results.wasNull(TRGT_PAY_AMT))
			
			if(!results.wasNull(REQN_STAT_CD))
			{
				requisition.setReqnStatCd(results.getShort(REQN_STAT_CD));
			} // end if(!results.wasNull(REQN_STAT_CD))
			
			if(!results.wasNull(INTVW_CAND_CNT))
			{
				requisition.setIntvwCandCnt(results.getShort(INTVW_CAND_CNT));
			} // end if(!results.wasNull(INTVW_CAND_CNT))
			
			if(!results.wasNull(INTVW_MINS))
			{
				requisition.setIntvwMins(results.getShort(INTVW_MINS));
			} // end if(!results.wasNull(INTVW_MINS))
			
			if(!results.wasNull(REQN_CAL_ID))
			{
				requisition.setReqnCalId(results.getInt(REQN_CAL_ID));
				
				// if the calendars collection contains the calendar specified, set it on the requisition object.
				if(mCalendars != null && mCalendars.containsKey(requisition.getReqnCalId()))
				{
					requisition.setReqnCal(mCalendars.get(requisition.getReqnCalId()));
				} // end if(mCalendars != null && mCalendars.containsKey(requisition.getReqnCalId()))
			} // end if(!results.wasNull(REQN_CAL_ID))
			
			if(!results.wasNull(INTVW_SCHED_CNT))
			{
				requisition.setIntvwSchedCnt(results.getInt(INTVW_SCHED_CNT));
			} // end if(!results.wasNull(INTVW_SCHED_CNT))
			
			if(!results.wasNull(INTVW_CMPLT_CNT))
			{
				requisition.setIntvwCmpltCnt(results.getInt(INTVW_CMPLT_CNT));
			} // end if(!results.wasNull(INTVW_CMPLT_CNT))

			if(!results.wasNull(HIRE_EVNT_FLG))
			{
				requisition.setHiringEvnt(results.getBoolean(HIRE_EVNT_FLG));
			} // end if(!results.wasNull(HIRE_EVNT_FLG))

			if(!results.wasNull(RSC_SCHED_FLG))
			{
				requisition.setRscSched(results.getBoolean(RSC_SCHED_FLG));
			} // end if(!results.wasNull(RSC_SCHED_FLG))

			if(!results.wasNull(APLCNT_TMP_FLG))
			{
				requisition.setAplcntTmp(results.getBoolean(APLCNT_TMP_FLG));
			} // end if(!results.wasNull(APLCNT_TMP_FLG))
			
			// add it to the collection
			mRequisitions.add(requisition);
		} // end while(results.next())
	} // end function readActiveRequisitionsForStore()	
	
	/**
	 * @return collection of requisitions
	 */
	public List<Requisition> getRequisitions()
	{
		return mRequisitions;
	} // end function getRequisitions()
	
	/**
	 * @param requisitions collection of requisitions
	 */
	public void setRequisitions(List<Requisition> requisitions)
	{
		mRequisitions = requisitions;
	} // end function setRequisitions()
	
	/**
	 * @return collection of requisition calendar objects
	 */
	public Map<Integer, RequisitionCalendar> getCalendars()
	{
		return mCalendars;
	} // end function getCalendars()
	
	/**
	 * @param calendars collection of requisition calendar objects
	 */
	public void setCalendars(Map<Integer, RequisitionCalendar> calendars)
	{
		mCalendars = calendars;
	} // end function setCalendars()
} // end class RequisitionHandler
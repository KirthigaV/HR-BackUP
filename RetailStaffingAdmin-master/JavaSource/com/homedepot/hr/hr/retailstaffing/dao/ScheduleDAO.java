package com.homedepot.hr.hr.retailstaffing.dao;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ScheduleDAO.java
 * Application: ScheduleDAO
 *
 */

import java.sql.Timestamp; 
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.InterviewAvaliableSlotsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class ScheduleDAO implements DAOConstants, RetailStaffingConstants
{
	private static final Logger mLogger = Logger.getLogger(ScheduleDAO.class);

	// Added Data model by SS
	public static List<InterviewAvaliableSlotsTO> readHumanResourcesRequisitionScheduleAvailableSlotsByInputList(int reqCalId, Timestamp beginTime,
	    int additionalMinutes, short statusCode, int recordsRequested, boolean slotAvailable) throws QueryException
	{

		mLogger.debug("Enter readHumanResourcesRequisitionScheduleAvailableSlotsByInputList ");
		final List<InterviewAvaliableSlotsTO> interviewAvaliableSlotsList = new ArrayList<InterviewAvaliableSlotsTO>();

		MapStream inputs = new MapStream("readHumanResourcesRequisitionScheduleAvailableSlotsByInputList");

		inputs.put("requisitionCalendarId", reqCalId);
		inputs.put("beginTimestampGreaterThan", beginTime);
		inputs.put("additionalMinutes", additionalMinutes);
		inputs.putAllowNull("requisitionScheduleStatusCode", statusCode);
		inputs.addQualifier("recordsRequested", recordsRequested);
		inputs.addQualifier("slotAvailable", slotAvailable);// optional
		inputs.put("beginTimestampMinutes", (int)30); // optional

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				InterviewAvaliableSlotsTO interviewAvaliableSlotsTO = null;
				while(results.next())
				{
					interviewAvaliableSlotsTO = new InterviewAvaliableSlotsTO();
					interviewAvaliableSlotsTO.setBeginTimestamp(results.getTimestamp("beginTimestamp"));
					interviewAvaliableSlotsTO.setSequenceNumber(results.getShort("sequenceNumber"));
					interviewAvaliableSlotsTO.setStoreNumber(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
					interviewAvaliableSlotsTO.setReservedDateTime(results.getTimestamp("requisitionScheduleReserveTimestamp"));
					interviewAvaliableSlotsList.add(interviewAvaliableSlotsTO);
				}
			}
		});
		mLogger.debug("interviewAvaliableSlotsList count -->" + interviewAvaliableSlotsList.size());
		mLogger.debug("Exit readHumanResourcesRequisitionScheduleAvailableSlotsByInputList ");
		return interviewAvaliableSlotsList;
	}

	// Added for Deferred ticket: 3373 - Check for the query that returns the
	// number of job openings that are left for a given requisition, where no
	// candidate has been extended an offer
	// (excluding candidates who have been extended an offer but declined that
	// offer).
	public static List<RequisitionDetailTO> readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList(int empRequNbr) throws QueryException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering InterviewStrEmpReqCnt(), empRequNbr: %1$s ", empRequNbr));
		} // end if

		final List<RequisitionDetailTO> interviewStrEmpReqCntList = new ArrayList<RequisitionDetailTO>();

		MapStream inputs = new MapStream("readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList");
		
		inputs.put("employmentRequisitionNumber", empRequNbr);
		inputs.put("candidateOfferMadeFlag", true);
		inputs.put("candidateOfferStatusIndicator", DECLINED_STATUS);
		List<String> activeFlgList = new ArrayList<String>();
		activeFlgList.add("Y");
		activeFlgList.add("H");
		inputs.put("activeFlagList", activeFlgList);

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				RequisitionDetailTO interviewStrEmpReqCnt = null;

				while(results.next())
				{
					interviewStrEmpReqCnt = new RequisitionDetailTO();

					interviewStrEmpReqCnt.setAuthPosCount(String.valueOf(results.getShort("authorizationPositionCount")));

					interviewStrEmpReqCnt.setOfferExtCandCnt(results.getInt("count"));
					mLogger.debug(String.format("Entering looping InterviewStrEmpReqCnt(), ResultCount 01 : %1$d  AuthPosCount: %2$d ",
					    results.getInt("count"), results.getShort("authorizationPositionCount")));
					interviewStrEmpReqCntList.add(interviewStrEmpReqCnt);
				}
			}
		});

		return interviewStrEmpReqCntList;
	}
}

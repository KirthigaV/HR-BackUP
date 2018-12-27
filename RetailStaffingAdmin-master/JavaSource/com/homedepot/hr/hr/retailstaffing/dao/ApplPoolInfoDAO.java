/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenDAO.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ApplPoolInfoTO;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to have DAO logic for Phone Screen Functionality This
 * Class will have the following functionalities:- readByPhoneScreenNumber -
 * Reading the phone screen details by phone number. readInterviewDetails -
 * Reading the interview detail for a particular phone screen.
 * readRequisitionStoreLocation - Reading the requisition store location details
 * for the candidate. readStoreDetails - Reading the store details for the store
 * number. readRequisitionDetails - Reading the requisition details for the
 * given requisition number. readResponses - Reading the responses for a given
 * phone number. readPhoneScreenEmploymentRequesitionNote - Reading the employee
 * requisition note for a given phone number. readRequisitionStaffingDetails -
 * Reading the staffing details for a given requisition number.
 * readNlsInterviewRespondStatus - Reading the interview response status codes
 * for the given phone number. readHumanResourcesPhoneScreen - Reading the phone
 * screen details by phone screen number. readNlsInterviewRespondStatusList -
 * Reading the nls response status list. getCandidateDetails - Reading the
 * candidate details by phone screen number and requisition number.
 * 
 * @author TCS
 * 
 */
public class ApplPoolInfoDAO implements DAOConstants, RetailStaffingConstants
{
	/**
	 * This method is used for getting candidate count info.
	 * 
	 * @param reqNum
	 *            - the requisition number.
	 * 
	 * @return list of candidate count
	 * @throws QueryException
	 */

	// candidate count
	public List<ApplPoolInfoTO> getCandidateCount(String reqNumber) throws RetailStaffingException
	{
		final List<ApplPoolInfoTO> applInfoTOList = new ArrayList<ApplPoolInfoTO>();
		int reqNum = Integer.parseInt(reqNumber);
		System.out.println("inside DAO getCandidateCount::");

		MapStream inputs = new MapStream("readHumanResourcesStoreRequisitionCandidateCountByInputList");
		try
		{

			inputs.put("employmentRequisitionNumber", reqNum);
			List<Object> inputActiveFlagList = new ArrayList<Object>();
			inputActiveFlagList.add(true);
			inputs.put("activeFlagList", inputActiveFlagList); // optional

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					ApplPoolInfoTO applInfoTO = null;
					while(results.next())
					{
						applInfoTO = new ApplPoolInfoTO();
						if(!results.wasNull("humanResourcesStoreRequisitionCandidateCount"))
						{
							applInfoTO.setCandidateCount(Integer.toString(results.getInt("humanResourcesStoreRequisitionCandidateCount")));
							applInfoTOList.add(applInfoTO);
						}
					}
				}
			});

		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applInfoTOList;
	}
	// candidate count

}

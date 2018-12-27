/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingUpdateApplManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingUpdateApplDAO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplToReqDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplUnavailInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.ApplToReqRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplUnavailInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplUnavailResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.dao.ConnectionHandler;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This Class is used to have the business logic for Search based on ITI Number,
 * ITI Status
 * 
 * Methods:
 * 
 */

public class RetailStaffingUpdateApplManager implements RetailStaffingConstants, DAOConstants
{
	private static final Logger logger = Logger.getLogger(RetailStaffingITIManager.class);

	/**
	 * The method will be used for updating the interview screen details in the
	 * DB.
	 * 
	 * @param itiUpdate
	 *            - The object containing the phone screen details to be updated
	 *            or inserted.
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public ApplUnavailInfoResponse attachApplToRequisition(ApplToReqRequest applToReqRequest, String mediaType) throws RetailStaffingException
	{
		logger.info(this + "Enters attachApplToRequisition method in Manager with input as" + applToReqRequest);

		// SIR 4444
		final ApplUnavailInfoResponse applUnavailInfoRes = new ApplUnavailInfoResponse();
		ApplUnavailResponse applUnavailRes = new ApplUnavailResponse();
		List<ApplUnavailInfoTO> applUnavailInfoTOList = new ArrayList<ApplUnavailInfoTO>();
		// SIR 4444


		if(applToReqRequest != null)
		{
			String applUnavailName = "";
			for(int i = 0; i < applToReqRequest.getApplToReqDetailsTOList().size(); i++)
			{
				List<ApplToReqDetailsTO> applToReqTO = applToReqRequest.getApplToReqDetailsTOList();
				final String storeNumber = (applToReqTO.get(i)).getStoreNumber();
				final int reqNumber = Integer.parseInt((applToReqTO.get(i)).getReqNumber());
				final String applId = (applToReqTO.get(i)).getApplId();
				final String applName = (applToReqTO.get(i)).getApplName();

				ApplUnavailInfoTO applUnavailInfoTO = new ApplUnavailInfoTO();
				// readHumanResourcesStoreRequisitionCandidate see if the applicant has been attached to this req
				try
				{
					QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
					QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
					
					DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
					DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);

					ConnectionHandler connectionHandler = new UniversalConnectionHandler(true, null,  workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
					{
						@Override
						public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
						{
							DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
							Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

							DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
							Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
							
							RetailStaffingUpdateApplDAO retailStaffingUpdateApplDAO = new RetailStaffingUpdateApplDAO();
							// Fixing defect 4444 06-24-2011
							// update candidate with Status 'C', if succeeds, do
							// create, if fails, do update
							String candidateAvailIndicator = "C";

							// first try to update applicant's status to "C"
							boolean updateCandidateStatus = retailStaffingUpdateApplDAO.updateReleasedEmploymentPositionCandidate(applId,
							    candidateAvailIndicator);

							List applInfoList = retailStaffingUpdateApplDAO.readHumanResourcesStoreRequisitionCandidate(storeNumber, reqNumber, applId);

							// if query came up empty, updateCandidateStatus is
							// true, attach appl to req
							if((applInfoList == null || applInfoList.size() == 0) && updateCandidateStatus)
							{
								// Insert
								boolean createHRStoreReqCandidateStatus = retailStaffingUpdateApplDAO.createHumanResourcesStoreRequisitionCandidate(
								    storeNumber, reqNumber, applId);
							} else if (applInfoList != null && applInfoList.size() > 0 && updateCandidateStatus) {
								// Update
								boolean updateHRStoreReqCandidateStatus = retailStaffingUpdateApplDAO.updateHRStoreReqCandidateStatus(storeNumber, reqNumber, applId);
							}
						}
					};

					connectionHandler.execute();
					applUnavailInfoRes.setStatus(SUCCESS_APP_STATUS);
				}
				catch (Exception e)
				{
					applUnavailName = applName + "; ";
				}

				logger.info(this + "Leaves attachApplToRequisition method in Manager with output");

				if(applUnavailName != null && applUnavailName.length() > 0)
				{
					applUnavailInfoTO.setApplUnavailName(applUnavailName);
					applUnavailInfoTOList.add(applUnavailInfoTO);
					applUnavailName = "";
				}

			} // end of for loop

			if(applUnavailInfoTOList != null && applUnavailInfoTOList.size() > 0)
			{
				applUnavailRes.setApplUnavailInfoTOList(applUnavailInfoTOList);
				applUnavailInfoRes.setApplUnavailRes(applUnavailRes);
			}

		}

		return applUnavailInfoRes;
	}
}

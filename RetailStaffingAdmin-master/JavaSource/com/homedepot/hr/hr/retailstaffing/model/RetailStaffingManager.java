/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingDAO;
import com.homedepot.hr.hr.retailstaffing.dto.OrgUnitDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ProcessStatusTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.OrgUnitResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ProcessStatusesResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.services.response.StatusResponse;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;

/**
 * This Class is used to have the business across the application like on load
 * need to get the interview status details.
 * Method:
 * getIntrwStatusDetails - Get Interview Statuses List from DB
 * getSummaryResults - Get Result based on process and organisation unit
 * getLocaleDetails - Get list of available division,region,district
 * getProcessStatuses - Fetch Process status code and description
 * 
 * @author TCS
 * 
 */
public class RetailStaffingManager implements RetailStaffingConstants
{
	private static final Logger logger = Logger
			.getLogger(RetailStaffingManager.class);

	public Response getAllInterviewStatusesDetails(String phoneScrnNbr) throws RetailStaffingException
	{
		logger.info(this + "Enters getAllInterviewStatuseDetails method in Manager ");
		Response res = null;
		
		StatusResponse statRes = new StatusResponse();
		res = new Response();
		
		try
		{
			// invoke the business logic method to get all status objects
			// TODO : pass this in
			Map<StatusType, List<Status>> stats = StatusManager.getAllStatusObjects("EN_US");
			
			/*
			 * no null check here because the response map returned will not be null and if no statuses 
			 * were found for any of the status types a NoRowsFoundException will be thrown by the 
			 * business logic method.
			 */
			statRes.setPhoneScreenStats(stats.get(StatusType.PHONE_SCREEN_STATUS));
			statRes.setInterviewStats(stats.get(StatusType.INTERVIEW_STATUS));
			statRes.setMaterialsStats(stats.get(StatusType.MATERIALS_STATUS));
								
			res.setStatus(SUCCESS_APP_STATUS);
			res.setStatusListRes(statRes);
			
			if(res == null)
			{
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger
				.info(this
						+ "Leaves getAllInterviewStatuseDetails method in Manager with output as"
						+ res);
		return res;

	}
	
	/**
	 * This method is used to get the summary response based on Store,Region,District and division
	 * @param SummaryRequest
	 * @return 
	 * @throws RetailStaffingException
	 */
	public Response getSummaryResults(SummaryRequest summReq)
			throws RetailStaffingException
	{
		logger
				.info(this
						+ "Enters getSummaryResults method in Manager with input as orgID:"
						+ summReq.getOrgID() + "Orgsearch code: "
						+ summReq.getOrgSearchCode() + " processID:"
						+ summReq.getProcessSearchCode());
		summReq.getPaginationInfoTO();
		Response res = null;
		RetailStaffingITIManager dlnITIMgr = null;
		RetailStaffingRequisitionManager dlnReqMgr = null;
		try
		{
			res = new Response();
			// get basic phone screen summary
			if(summReq.getProcessSearchCode() != null
					&& summReq.getProcessSearchCode().equalsIgnoreCase(
							CONDUCT_BASIC_PHONE_SCREENS))
			{
				dlnITIMgr = new RetailStaffingITIManager();
				if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_STORE))
				{
					res = dlnITIMgr
							.getBasicPhoneScreenIntrwDtlsByStore(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DISTRICT))
				{
					res = dlnITIMgr.getBasicPhoneScreenIntrwDtlsByDist(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_REGION))
				{
					res = dlnITIMgr.getBasicPhoneScreenIntrwDtlsByReg(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DIVISION))
				{
					res = dlnITIMgr.getBasicPhoneScreenIntrwDtlsByDiv(summReq);
				}

				else
				{
					logger.error(this + "Input is either null or empty string");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}
			// get detailed phone screen summary
			else if(summReq.getProcessSearchCode() != null
					&& summReq.getProcessSearchCode().equalsIgnoreCase(
							CONDUCT_DETAILED_PHONE_SCREENS))
			{
				dlnITIMgr = new RetailStaffingITIManager();
				if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_STORE))
				{
					res = dlnITIMgr
							.getDetailedPhoneScreenIntrwDtlsByStore(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DISTRICT))
				{
					res = dlnITIMgr
							.getDetailedPhoneScreenIntrwDtlsByDist(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_REGION))
				{
					res = dlnITIMgr
							.getDetailedPhoneScreenIntrwDtlsByReg(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DIVISION))
				{
					res = dlnITIMgr
							.getDetailedPhoneScreenIntrwDtlsByDiv(summReq);
				}

				else
				{
					logger.error(this + "Input is either null or empty string");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}
			// get schedule interview phone screen summary
			else if(summReq.getProcessSearchCode() != null
					&& summReq.getProcessSearchCode().equalsIgnoreCase(
							CONDUCT_SCHEDULEINTERVIEW_PHONE_SCREENS))
			{
				dlnITIMgr = new RetailStaffingITIManager();
				if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_STORE))
				{
					res = dlnITIMgr
							.getScheduleInterviewPhoneScreenIntrwDtlsByStore(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DISTRICT))
				{
					res = dlnITIMgr
							.getScheduleInterviewPhoneScreenIntrwDtlsByDist(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_REGION))
				{
					res = dlnITIMgr
							.getScheduleInterviewPhoneScreenIntrwDtlsByReg(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DIVISION))
				{
					res = dlnITIMgr
							.getScheduleInterviewPhoneScreenIntrwDtlsByDiv(summReq);
				}
				else
				{
					logger.error(this + "Input is either null or empty string");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}

			// get review completed phone screen summary
			else if(summReq.getProcessSearchCode() != null
					&& summReq.getProcessSearchCode().equalsIgnoreCase(
							CONDUCT_REVIEWCOMPLETED_PHONE_SCREENS))
			{

				dlnITIMgr = new RetailStaffingITIManager();
				if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_STORE))
				{
					res = dlnITIMgr
							.getReviewCompletedPhoneScreenIntrwDtlsByStore(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DISTRICT))
				{
					res = dlnITIMgr
							.getReviewCompletedPhoneScreenIntrwDtlsByDist(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_REGION))
				{
					res = dlnITIMgr
							.getReviewCompletedPhoneScreenIntrwDtlsByReg(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DIVISION))
				{
					res = dlnITIMgr
							.getReviewCompletedPhoneScreenIntrwDtlsByDiv(summReq);
				}

				else
				{
					logger.error(this + "Input is either null or empty string");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}
			
			// get Send Interview Materials summary, MTS1876 07/02/2010
			else if(summReq.getProcessSearchCode() != null
					&& summReq.getProcessSearchCode().equalsIgnoreCase(
							VIEW_SEND_INTERVIEW_MATERIALS))
			{
				dlnITIMgr = new RetailStaffingITIManager();
				if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_STORE))
				{
					res = dlnITIMgr.getSendIntrwMaterialsDtlsByStore(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DISTRICT))
				{
					res = dlnITIMgr.getSendIntrwMaterialsDtlsByDist(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_REGION))
				{
					res = dlnITIMgr.getSendIntrwMaterialsDtlsByReg(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DIVISION))
				{
					res = dlnITIMgr.getSendIntrwMaterialsDtlsByDiv(summReq);
				}

				else
				{
					logger.error(this + "Input is either null or empty string");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}
			
			
			// get requisitions summary
			else if(summReq.getProcessSearchCode() != null
					&& summReq.getProcessSearchCode().equalsIgnoreCase(
							VIEW_REQUISITIONS))
			{
				dlnReqMgr = new RetailStaffingRequisitionManager();
				if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_STORE))
				{
					res = dlnReqMgr.getRequisitionsByStore(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DISTRICT))
				{
					res = dlnReqMgr.getRequisitionsByDist(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_REGION))
				{
					res = dlnReqMgr.getRequisitionsByReg(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DIVISION))
				{
					res = dlnReqMgr.getRequisitionsByDiv(summReq);
				}

				else
				{
					logger.error(this + "Input is either null or empty string");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}
			
			// get inactive requisitions summary
			else if(summReq.getProcessSearchCode() != null
					&& summReq.getProcessSearchCode().equalsIgnoreCase(
							VIEW_INACTIVE_REQUISITIONS))
			{
				dlnReqMgr = new RetailStaffingRequisitionManager();
				if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_STORE))
				{
					res = dlnReqMgr.getInactiveRequisitionsByStore(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DISTRICT))
				{
					res = dlnReqMgr.getInactiveRequisitionsByDist(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_REGION))
				{
					res = dlnReqMgr.getInactiveRequisitionsByReg(summReq);
				}
				else if(summReq.getOrgSearchCode() != null
						&& summReq.getOrgSearchCode().equalsIgnoreCase(
								ORGID_DIVISION))
				{
					res = dlnReqMgr.getInactiveRequisitionsByDiv(summReq);
				}

				else
				{
					logger.error(this + "Input is either null or empty string");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}
			else
			{
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger
				.info(this
						+ "Leaves updateInsertITIDetails method in Manager with output as"
						+ res);
		return res;

	}

	/**
	 * The manager method will return the list of all available
	 * divisions,districts and regions.
	 * @param 
	 * @return 
	 * @throws RetailStaffingException
	 */
	public Response getLocaleDetails() throws RetailStaffingException
	{
		Response res = null;
		List<OrgUnitDetailsTO> orgList = null;
		ProcessStatusesResponse process=null;
		OrgUnitResponse orgUnitResponse = null;
		try
		{
			RetailStaffingDAO RetailStaffingDAO = new RetailStaffingDAO();
			// Get the available districts.
			orgList = RetailStaffingDAO.getAllAvailableDistsDAO20();
			res = new Response();
			orgUnitResponse = new OrgUnitResponse();
			orgUnitResponse.setOrgUnitDetailsList(orgList);
			res.setDistList(orgUnitResponse);
			// Get the available divisions
			orgList = RetailStaffingDAO.getAllAvailableDivsDAO20();
			orgUnitResponse = new OrgUnitResponse();
			orgUnitResponse.setOrgUnitDetailsList(orgList);
			res.setDivList(orgUnitResponse);
			// Get the available regions
			orgList = RetailStaffingDAO.getAllAvailableRegionsDAO20();
			orgUnitResponse = new OrgUnitResponse();
			orgUnitResponse.setOrgUnitDetailsList(orgList);
			res.setRegionList(orgUnitResponse);
			process=getProcessStatuses();
			res.setProcessStsRes(process);

		}

		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return res;
	}
	/**
	 * This method is used to get the Statuses of the processes
	 * @param 
	 * @return 
	 * @throws RetailStaffingException
	 * 
	 */
	public ProcessStatusesResponse getProcessStatuses() throws RetailStaffingException
	{
		ProcessStatusesResponse processStsRes = null;
		List<ProcessStatusTO> stsDtlList = null;
		try
		{
			String statuses = Util.getProcessStatuses();
			logger
					.info("The statuses obtained from properties is: "
							+ statuses);
			if(statuses != null && statuses.length() > 0)
			{
				String[] statusesToks = statuses.split(",");
				if(statusesToks != null && statusesToks.length > 0)
				{
					processStsRes = new ProcessStatusesResponse();
					stsDtlList = new ArrayList<ProcessStatusTO>();
					ProcessStatusTO stsDtl = null;
					for (int i = 0; i < statusesToks.length; i++)
					{
						String[] statusTokDet = statusesToks[i].split(":");
						if(statusTokDet != null && statusTokDet.length >= 2)
						{
							stsDtl = new ProcessStatusTO();
							stsDtl.setStatusCode(statusTokDet[0]);
							stsDtl.setStatusDesc(statusTokDet[1]);
							stsDtlList.add(stsDtl);

						}
					}
					processStsRes.setStsDtlList(stsDtlList);
				}
			}
		}

		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return processStsRes;
	}
	

}

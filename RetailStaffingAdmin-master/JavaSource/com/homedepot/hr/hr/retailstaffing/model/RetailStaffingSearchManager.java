/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingSearchManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.PhoneScreenDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingCandtDtlDAO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.request.SearchRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;

/**
 * This Class is used to have the buisness logic for Search based on Requistion
 * Number, ssn number,iti number,store number.
 * 
 * @author TCS
 * 
 */
public class RetailStaffingSearchManager implements RetailStaffingConstants
{
	private static final Logger logger = Logger
			.getLogger(RetailStaffingSearchManager.class);

	/**
	 * This method is used for performing the search functionality based on the
	 * given requisition ID,candidate ID or phone screen ID.
	 * 
	 * @param req
	 *            - The object containing the details for which the search has
	 *            to be made.
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getSearchDetails(SearchRequest req)
			throws RetailStaffingException
	{
		logger.info(this
				+ "Enters getSearchDetails method in Manager with input as"
				+ req);
		Response res = null;
		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();
		List<RequisitionDetailTO> reqList = null;
		CandidateDetailResponse canRes = new CandidateDetailResponse();
		List<CandidateDetailsTO> canList = null;
		ITIDetailResponse itiRes = new ITIDetailResponse();
		List<PhoneScreenIntrwDetailsTO> itiList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		PhoneScreenDAO phnScreenDAO = null;
		RetailStaffingCandtDtlDAO RetailStaffingCandtDtlDAO = null;
		String phoneScreenStatusCode = null;
		String interviewStatusCode = null;
		Status interviewStatusObj = null;
		Status phoneScrnStatusObj = null;
		CandidateDetailsTO candidateDetailsTO = null;
		
		try
		{
			res = new Response();
			if(req.getInputNbr() != null && req.getFormName() != null)
			{
				// requisition search
				if(req.getFormName().equalsIgnoreCase(
						RetailStaffingConstants.REQ_FORM))
				{
					logger.info(this + "search is based on requisition"
							+ req.getInputNbr());

					reqList = PhoneScreenDAO.readRequisitionDetails(Integer
							.parseInt(req.getInputNbr()));
					if(reqList != null && reqList.size() > 0)
					{
						reqRes.setReqDtlList(reqList);
						res.setReqDtlList(reqRes);
					}
					else
					{
						logger.error(this + "No Results for input");
						throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE,NO_RECORDS_FOUND_ERROR_MSG+"requisitionnumber: "+req.getInputNbr());
					}

				}

				if(req.getFormName().equalsIgnoreCase(
						RetailStaffingConstants.CAN_FORM))
				{
					//Can have two kinds of Candidate Search.  One for Candidate Reference Number, the other for Last 4 SSN and Partial/Full Last Name.
					//If the input contains a delimiter of | then it is a Last 4 SSN and Partial/Full Last Name Search else it is a Candidate Reference Number Search
					RetailStaffingCandtDtlDAO = new RetailStaffingCandtDtlDAO();
					if (!req.getInputNbr().contains("|")) {
						//Candidate Reference Number Search
						//Need to left Pad Candidate Id with zeros so that it is a 10 digit number
						String paddedCandRefId = "";
						if (req.getInputNbr().length() < 10) {
							paddedCandRefId = String.format("%010d", Integer.parseInt(req.getInputNbr()));
						} else {
							paddedCandRefId = req.getInputNbr();
						}
						logger.info("Search is on Candidate Reference Number:" + paddedCandRefId);
						// Calling DAO to fetch candidate details.
						canList = RetailStaffingCandtDtlDAO.getCandidateDetail(paddedCandRefId);
					} else {
						//Last 4 SSN and Partial/Full Last Name Search
						String last4Ssn = req.getInputNbr().substring(0, 4);
						String lastName = req.getInputNbr().substring(5).toUpperCase();
						logger.info(String.format("Search is on Last 4 SSN:%1$s and Partial/Full Last Name:%2$s", last4Ssn, lastName));
						// Calling DAO to fetch candidate details.
						canList = RetailStaffingCandtDtlDAO.getCandidateDetail(last4Ssn, lastName);
					}
					
					if(canList != null && canList.size() > 0)
					{
						candidateDetailsTO = (CandidateDetailsTO) canList
								.get(0);
						canList.remove(0);
						canList.add(candidateDetailsTO);
						canRes.setCndDtlList(canList);
						res.setCanDtRes(canRes);
					}
					else
					{
						logger.error(this + "No Results for input");
						throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE,NO_RECORDS_FOUND_ERROR_MSG+"candidate id: "+req.getInputNbr());
					}
				}
				if(req.getFormName().equalsIgnoreCase(
						RetailStaffingConstants.ITI_NBR_FORM))
				{
					logger.info(this + "search is based on iti number"
							+ req.getInputNbr());
					itiList = PhoneScreenDAO
							.readHumanResourcesPhoneScreen(Integer.parseInt(req
									.getInputNbr()));
					if(itiList != null && itiList.size() > 0)
					{		
						phoneScreenStatusCode = itiList.get(0).getPhoneScreenStatusCode();
						interviewStatusCode = itiList.get(0).getInterviewStatusCode();
						
						if(phoneScreenStatusCode != null
								&& !phoneScreenStatusCode.trim().equals(
										EMPTY_STRING))
						{
							phoneScrnStatusObj = StatusManager.getStatusObject(StatusType.PHONE_SCREEN_STATUS, Short.parseShort(phoneScreenStatusCode), "EN_US"); 
						}
						
						if(interviewStatusCode != null && !interviewStatusCode.trim().equals(EMPTY_STRING) && !interviewStatusCode.trim().equals("0"))
						{
							interviewStatusObj = StatusManager.getStatusObject(StatusType.INTERVIEW_STATUS, Short.parseShort(interviewStatusCode), "EN_US"); 
						}
						
						// to get the job
						reqList = PhoneScreenDAO.readRequisitionDetails(Integer
								.parseInt(itiList.get(0).getReqNbr()));
						// calling the dao to get the screentype
						itiList = null;
						phnScreenDAO = new PhoneScreenDAO();
						itiList = phnScreenDAO.readByPhoneScreenNumber(Integer
								.parseInt(req.getInputNbr()));
						if(itiList != null && !itiList.isEmpty()
								&& itiList.size() > 0)
						{
							if(reqList != null && !reqList.isEmpty()
									&& reqList.size() > 0)
							{
								itiList.get(0).setJob(reqList.get(0).getJob());
							}
							
							if (phoneScrnStatusObj != null)
							{
								itiList.get(0).setPhoneScreenStatusDesc(phoneScrnStatusObj.getStatDesc());
							}
							
							if(interviewStatusObj != null)
							{
								itiList.get(0).setInterviewStatusDesc(
										interviewStatusObj.getStatDesc());
							}
							
							itiRes.setITIDtlList(itiList);
							res.setItiDtRes(itiRes);
						}
						else
						{
							logger.error(this + "No Results for input");
							throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE,NO_RECORDS_FOUND_ERROR_MSG+"phonescreen number: "+req.getInputNbr());
						}
						
					}
					else
					{
						logger.error(this + "No Results for input");
						throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE,NO_RECORDS_FOUND_ERROR_MSG+"phonescreen number: "+req.getInputNbr());
					}
				}
			}
			else
			{
				logger.info(this + "Input is either null or empty string");
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
		logger.info(this
				+ "Leaves getSearchDetails method in Manager with output as"
				+ res);
		return res;

	}
}

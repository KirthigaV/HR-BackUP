/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingApplPoolManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.ApplPoolInfoDAO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplPoolInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplPoolInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateCountResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
/*
/**
 * This Class is used to appl profile based on applicant number
 * Methods:
 * getApplicantProfile
 * @author
 * 
 */
public class RetailStaffingApplPoolInfoManager implements RetailStaffingConstants
{
	private static final Logger logger = Logger
			.getLogger(RetailStaffingApplPoolInfoManager.class);
	public int eventId = 0;

	/**
	 * This method is used to get the applicant profile based on the applicant number.
	 * 
	 * @param applNbr
	 * @return
	 * @throws RetailStaffingException
	 */
	public ApplPoolInfoResponse getCandidateCount(String requisitionNbr)
			throws RetailStaffingException
	{
		logger.info(this
				+ "Enters RetailStaffingApplProfileManager method in Manager with input"
				+ requisitionNbr);
		
		//Response res = null;
		
		ApplPoolInfoDAO applPoolInfoDAO = new ApplPoolInfoDAO();
		ApplPoolInfoResponse applPoolInfoRes = new ApplPoolInfoResponse();
		Response applProfileRes = new Response();
		
		
		// ApplPoolInfoTO list
		List<ApplPoolInfoTO> applPoolInfoTOList = null;

		// applicant response
		//ApplPersonalInfoResponse applPersonalRes = new ApplPersonalInfoResponse();
		CandidateCountResponse candidateCountRes = new CandidateCountResponse();
		
		try
		{
			if(requisitionNbr != null && !requisitionNbr.trim().equals(""))
			{
				// candidate count	
				applPoolInfoTOList = applPoolInfoDAO.getCandidateCount(requisitionNbr);
				
				if (applPoolInfoTOList != null && applPoolInfoTOList.size() > 0) {
					candidateCountRes.setApplPoolInfoTOList(applPoolInfoTOList);
					applPoolInfoRes.setCandidateCountRes(candidateCountRes);
				}
			}
			else
			{				
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this
				+ "Leaves getRequisitionDetails method in Manager with output"
				+ applProfileRes);
		
		
		return applPoolInfoRes;
	}
}

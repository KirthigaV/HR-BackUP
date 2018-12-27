/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingCandtDtlManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.Collections;
import java.util.Comparator;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingCandtDtlDAO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplPhnScreenInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;

/**
 * This Class is used to have the business logic for Search based on Candidate
 * Number.
 * Methods: getCandidateDetails - To fetch the Candidate details
 * Due to CDP Project had to add candRefId so that the Candidate data can be looked up, the ssnNbr is now the applicantId
 * the ssnNbr/applicantId is used to get their Requisition and Phone Screen History 
 * 
 * @author TCS
 * 
 */
public class RetailStaffingCandtDtlManager implements RetailStaffingConstants
{
	private static final Logger logger = Logger
			.getLogger(RetailStaffingCandtDtlManager.class);

	/**
	 * The method will be used for the given ssn number.
	 * Due to CDP Project had to add candRefId so that the Candidate data can be looked up, the ssnNbr is now the applicantId
	 * the ssnNbr/applicantId is used to get their Requisition and Phone Screen History
	 * @param ssnNbr - The given ssn number.  Is now the applicantId
	 * @param candRefId - Kenexa Candidate Reference Number
	 * @return
	 * @throws RetailStaffingException
	 */
	public Response getCandidateDetails(String ssnNbr, String candRefId)
			throws RetailStaffingException
	{
		logger.info(this
				+ "Enters getCandidateDetails method in Manager with input as"
				+ ssnNbr);
		Response res = null;
		RetailStaffingCandtDtlDAO RetailStaffingCandtDtlDAO = null;
		try
		{
			res = null;
			if(ssnNbr != null && !ssnNbr.trim().equals(""))
			{
				RetailStaffingCandtDtlDAO = new RetailStaffingCandtDtlDAO();
				res = RetailStaffingCandtDtlDAO.getCandidateDetails(ssnNbr, candRefId);
				//Business requested that the result be sorted by Phone Screen in Desc Order MTS1876 06/20/2016
				Collections.sort(res.getItiDtRes().getITIDtlList(), new Comparator<PhoneScreenIntrwDetailsTO>() {
			        @Override
			        public int compare(final PhoneScreenIntrwDetailsTO object1, final PhoneScreenIntrwDetailsTO object2) {
			        	Integer i = new Integer(object2.getItiNbr()); 
			        	int result = i.compareTo(new Integer(object1.getItiNbr()));
			            return result;
			        }
			    });	
			}
			else
			{
				logger.error(this + "Input is either null or empty string.");
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
				+ "Leaves getCandidateDetails method in Manager with output as"
				+ res);
		return res;

	}
}

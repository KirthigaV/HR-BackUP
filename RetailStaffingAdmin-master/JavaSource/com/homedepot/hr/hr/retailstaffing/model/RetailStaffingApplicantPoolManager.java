/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingRequisitionManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.ApplPoolDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingDAO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplicantTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.ApplicantPoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplicantPoolResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplicantResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
/*
/**
 * This Class is used to have the buisness logic for Search based on Requistion
 * Number.
 * Methods:
 * getApplicantPool
 * @author
 * 
 */
public class RetailStaffingApplicantPoolManager implements
		RetailStaffingConstants
{
	private static final Logger logger = Logger
			.getLogger(RetailStaffingRequisitionManager.class);
	public int eventId = 0;

	/**
	 * This method is used to get the applicant pool based on store number.
	 * 
	 * @param storeNo
	 * @return
	 * @throws RetailStaffingException
	 */
	public ApplicantPoolResponse getApplicantPool(ApplicantPoolRequest applicantPoolReq)
			throws RetailStaffingException
	{
		logger.info(this + "Enters getApplicantPool method in Manager ");
		
		ApplPoolDAO applApplicantPoolDAO = new ApplPoolDAO();
		ApplicantPoolResponse applApplicantPoolRes = new ApplicantPoolResponse();
		// applicant response
		ApplicantResponse applApplicantRes = new ApplicantResponse();
		// applicant TO list
		List<ApplicantTO> applApplicantTOList = null;
		Map<String, Object> applicantPoolMap = null;
		
		try
		{
			RetailStaffingDAO retailStaffingDAO = new RetailStaffingDAO();
			/*
			if(!retailStaffingDAO.checkStoreForExistence(applicantPoolReq.getOrgID()))
			{
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);	
			}
			*/
			// applicant pool
			applicantPoolMap = applApplicantPoolDAO.getApplicantInfoList(applicantPoolReq);
			
			applApplicantTOList = (List<ApplicantTO>) applicantPoolMap.get(APPLICANT_POOL_LIST);
			
			if(applApplicantTOList == null || applApplicantTOList.size() <= 0)
			{}
			
			
			if (applApplicantTOList != null && applApplicantTOList.size() > 0) {
				applApplicantRes.setApplApplicantTOList(applApplicantTOList);
				applApplicantPoolRes.setApplApplicantRes(applApplicantRes);
			}	
			
			Map<String, Object> paginationToken = (Map<String, Object>) applicantPoolMap.get(PAGINATION_TOKEN);
			
			if(paginationToken != null)
			{
				/*
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if(paginationToken.get("FIRST_RECORD") != null)
				{
					
					recordDet = (Map<String, Object>) paginationToken
							.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedDate(Util
							.converDatetoDateTO((java.sql.Date) recordDet
									.get("UPDATED_TS")));
					recordInfo
							.setId(((Integer) recordDet.get("APPLICANT_ID"))
									.toString());
					applApplicantPoolRes.setFirstRecordInfo(recordInfo);
					
				}
				*/
			}
			
		}
		
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this
				+ "Leaves getRequisitionDetails method in Manager with output"
				+ applApplicantPoolRes);
		
		return applApplicantPoolRes;
	}
}

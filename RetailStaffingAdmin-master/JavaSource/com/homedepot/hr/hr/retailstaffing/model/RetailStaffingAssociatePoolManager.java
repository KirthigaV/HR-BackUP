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
import com.homedepot.hr.hr.retailstaffing.dto.AssociateTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.AssociatePoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociatePoolResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociateResponse;
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
public class RetailStaffingAssociatePoolManager implements
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
	public AssociatePoolResponse getAssociatePool(AssociatePoolRequest associatePoolReq)
			throws RetailStaffingException
	{
		logger.info(this + "Enters getApplicantPool method in Manager ");
		
		ApplPoolDAO applAssociatePoolDAO = new ApplPoolDAO();
		AssociatePoolResponse applAssociatePoolRes = new AssociatePoolResponse();
		// applicant response
		AssociateResponse applAssociateRes = new AssociateResponse();
		// applicant TO list
		List<AssociateTO> applAssociateTOList = null;
		Map<String, Object> associatePoolMap = null;
		/*
		try
		{
			RetailStaffingDAO retailStaffingDAO = new RetailStaffingDAO();
			/*
			if(!retailStaffingDAO.checkStoreForExistence(applicantPoolReq.getOrgID()))
			{
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);	
			}
			
			// applicant pool
			associatePoolMap = applAssociatePoolDAO.getAssociateInfoList(associatePoolReq);
			
			applAssociateTOList = (List<AssociateTO>) associatePoolMap.get(ASSOCIATE_POOL_LIST);
			
			if(applAssociateTOList == null || applAssociateTOList.size() <= 0)
			{}
			
			
			if (applAssociateTOList != null && applAssociateTOList.size() > 0) {
				applAssociateRes.setApplAssociateTOList(applAssociateTOList);
				applAssociatePoolRes.setApplAssociateRes(applAssociateRes);
			}	
			
			Map<String, Object> paginationToken = (Map<String, Object>) associatePoolMap.get(PAGINATION_TOKEN);

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
			
			}
			
		}
		
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this
				+ "Leaves getRequisitionDetails method in Manager with output"
				+ applAssociatePoolRes);
		*/
		return applAssociatePoolRes;
	}
}

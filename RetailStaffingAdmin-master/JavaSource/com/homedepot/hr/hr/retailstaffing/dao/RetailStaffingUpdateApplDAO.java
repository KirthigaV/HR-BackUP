/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingUpdateApplDAO.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.InsertNotifier;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.exceptions.UpdateException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class RetailStaffingUpdateApplDAO implements DAOConstants, RetailStaffingConstants {
	
	public List readHumanResourcesStoreRequisitionCandidate(String storeNum, int requisitionNum, String applID) throws QueryException 
	{
		final List readHumanResourcesStoreRequisitionCandidateList = new ArrayList();
		

		MapStream inputs = new MapStream("readHumanResourcesStoreRequisitionCandidate");

		inputs.put("humanResourcesSystemStoreNumber", storeNum);
		inputs.put("employmentRequisitionNumber", requisitionNum);
		inputs.put("employmentPositionCandidateId", applID);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {

						while (results.next()) {
							//readHumanResourcesStoreRequisitionCandidateList.add("APPLICANT_ALREADY_ATTACHED");							
							String createUserId = "";
							//String active = "";
							if (!results.wasNull("createUserId")) {
								createUserId = StringUtils.trim(results.getString("createUserId"));
								readHumanResourcesStoreRequisitionCandidateList.add(createUserId);
							}	
						}
					}
				});

		return readHumanResourcesStoreRequisitionCandidateList;
	}
	
	// createHumanResourcesStoreRequisitionCandidate
	public boolean createHumanResourcesStoreRequisitionCandidate(String storeNum, int reqNum, String applId) throws QueryException {
		final GenericResults result = new GenericResults();
		
		MapStream inputs = new MapStream("createHumanResourcesStoreRequisitionCandidate");
		
		inputs.put("humanResourcesSystemStoreNumber", storeNum);
		inputs.put("employmentRequisitionNumber", reqNum);
		inputs.put("employmentPositionCandidateId", applId);
		//inputs.put("createUserId", "");
		inputs.put("interviewStatusIndicator", "");
		inputs.put("referenceCheckResultIndicator", "");
		inputs.put("drugTestResultCode", "");
		inputs.put("candidateOfferMadeFlag", "");
		inputs.put("candidateOfferStatusIndicator", "");
		inputs.put("inactiveDate", new Date(System.currentTimeMillis()));
		inputs.put("activeFlag", true);
		inputs.put("humanResourcesActionSequenceNumber", (short) 0);
		inputs.put("employeeStatusActionCode", "");
		inputs.put("activeDate", new Date(System.currentTimeMillis()));
		inputs.put("drugTestId", "");
		inputs.putAllowNull("candidateOfferMadeDate", null); // can be null
		inputs.put("offerDeclineReasonCode", 0);
		inputs.putAllowNull("letterOfIntentDate", null); // can be null
		inputs.putAllowNull("adverseActionDate", null); // can be null
		
		BasicDAO.insertObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new InsertNotifier() 
		{
					public void notifyInsert(Object target, boolean success,
							int count, Query query, Inputs inputs)
							throws QueryException {
						//TODO: This code returns the boolean success by default, you can change 
						//this to suit your needs 
						result.setTarget(target);
						result.setSuccess(success);
						result.setCount(count);
					}
				});
		
		return result.isSuccess();
	}
	
	//updateHumanResourcesStoreRequisitionCandidate
/*	public boolean updateHumanResourcesStoreRequisitionCandidate(String storeNum, int reqNum, String applId, String createUserId) throws UpdateException, QueryException {
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("updateHumanResourcesStoreRequisitionCandidate");

		inputs.put("humanResourcesSystemStoreNumber", storeNum);
		inputs.put("employmentRequisitionNumber", reqNum);
		inputs.put("employmentPositionCandidateId", applId);
		inputs.put("createUserId", createUserId);
		inputs.put("interviewStatusIndicator", "");
		inputs.put("referenceCheckResultIndicator", "");
		inputs.put("drugTestResultCode", "");
		inputs.put("candidateOfferMadeFlag", "");
		inputs.put("candidateOfferStatusIndicator", "");
		inputs.put("inactiveDate", new Date(System.currentTimeMillis()));
		inputs.put("activeFlag", true);
		inputs.put("humanResourcesActionSequenceNumber", (short) 0);
		inputs.put("employeeStatusActionCode", "");
		inputs.put("activeDate", new Date(System.currentTimeMillis()));
		inputs.put("drugTestId", "");
		inputs.putAllowNull("candidateOfferMadeDate", null); // can be null
		inputs.put("offerDeclineReasonCode", 0);
		inputs.putAllowNull("letterOfIntentDate", null); // can be null
		inputs.putAllowNull("adverseActionDate", null); // can be null
		
		BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier() {
					public void notifyUpdate(Object target, boolean success,
							int count, Query query, Inputs inputs)
							throws QueryException {
						result.setTarget(target);
						result.setSuccess(success);
						result.setCount(count);
					}
				});
		
			return result.isSuccess();
	}*/
	
	// updateEmploymentPositionCandidate
	public boolean updateEmploymentPositionCandidate(String applId, String applType) throws QueryException {
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("updateEmploymentPositionCandidate");
		
		inputs.put("employmentPositionCandidateId", applId);
		inputs.put("candidateTypeIndicator", applType);
		inputs.put("candidateAvailabilityIndicator", "C");

		BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier() {
					public void notifyUpdate(Object target, boolean success,
							int count, Query query, Inputs inputs)
							throws QueryException {

						result.setTarget(target);
						result.setSuccess(success);
						result.setCount(count);
					}
				});

		return result.isSuccess();
	}
	
	
	// Fixing defect 4444 06-20-2011
	// updateReleasedEmploymentPositionCandidate
	public boolean updateReleasedEmploymentPositionCandidate(String applId, String availIndicator) throws QueryException {
		
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("updateReleasedEmploymentPositionCandidate");
		
		inputs.put("employmentPositionCandidateId", applId);
		inputs.put("candidateAvailabilityIndicator", availIndicator);

		BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier() {
					public void notifyUpdate(Object target, boolean success,
							int count, Query query, Inputs inputs)
							throws QueryException {

						result.setTarget(target);
						result.setSuccess(success);
						result.setCount(count);
					}
				});
		return result.isSuccess();
	}

	public boolean updateHRStoreReqCandidateStatus(String storeNumber, int reqNumber, String applId) throws QueryException {
		
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("updateHumanResourcesStoreRequisitionCandidateDetails");
		
		inputs.put("humanResourcesSystemStoreNumber", storeNumber);
		inputs.put("employmentRequisitionNumber", reqNumber); 
		inputs.put("employmentPositionCandidateId", applId); 
		inputs.put("humanResourcesActiveFlag", true); 
		inputs.put("activeFlag", false);
		inputs.put("inactiveDate", new Date(System.currentTimeMillis())); 
		inputs.put("activeDate", new Date(System.currentTimeMillis())); 		

		BasicDAO.updateObject(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new UpdateNotifier() {
					public void notifyUpdate(Object target, boolean success,
							int count, Query query, Inputs inputs)
							throws QueryException {

						result.setTarget(target);
						result.setSuccess(success);
						result.setCount(count);
					}
				});
		return result.isSuccess();
	}	
	
	//This inner class was generated for convenience.  You may move this code
	//to an external class if desired.
	public static class GenericResults {
		protected Object target;
		protected boolean success;
		protected int count;

		/**
		 * @return the success
		 */
		public boolean isSuccess() {
			return success;
		}

		/**
		 * @param success the success to set
		 */
		public void setSuccess(boolean success) {
			this.success = success;
		}

		/**
		 * @return the count
		 */
		public int getCount() {
			return count;
		}

		/**
		 * @param count the count to set
		 */
		public void setCount(int count) {
			this.count = count;
		}

		/**
		 * @return the target
		 */
		public Object getTarget() {
			return target;
		}

		/**
		 * @param target the target to set
		 */
		public void setTarget(Object target) {
			this.target = target;
		}
	}

	

}

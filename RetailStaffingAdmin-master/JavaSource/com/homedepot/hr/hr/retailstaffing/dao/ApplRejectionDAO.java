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

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.InsertNotifier;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used for Applicant Rejection 
 * inserts and reads
 * 
 * @author GXN5764
 * 
 */
public class ApplRejectionDAO implements DAOConstants, RetailStaffingConstants {
	private static final Logger logger = Logger
			.getLogger(ApplRejectionDAO.class);

	//TODO: change to match your business use id
	private static final int BUSINESS_USE_ID = 55;
	//Contract name for WorkforceEmploymentQualifications
	private static final String WORKFORCEEMPLOYMENTQUALIFICATIONS_NAME = "WorkforceEmploymentQualifications";
	//Version number for WorkforceEmploymentQualifications
	private static final int WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION = 1;

	public List<ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO> readNlsEmploymentPositionCandidateRejectedReasonByInputList()
			throws QueryException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("start readNlsEmploymentPositionCandidateRejectedReasonByInputList");
		}
		final List<ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO> readNlsEmploymentPositionCandidateRejectedReasonByInputListList = new ArrayList<ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO>();

		MapStream inputs = new MapStream(
				"readNlsEmploymentPositionCandidateRejectedReasonByInputList");
		//TODO: Replace values below
		inputs.put("languageCode", "en_US");

		if (logger.isDebugEnabled()) {
			logger.debug("executing the query");
		}

		//TODO: Verify contract name, business use id below
		BasicDAO.getResult(WORKFORCEEMPLOYMENTQUALIFICATIONS_NAME,
				BUSINESS_USE_ID, WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION,
				inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO = null;
						while (results.next()) {
							readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO = new ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO();
							readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO
									.setEmploymentPositionCandidateRejectReasonCode(results
											.getShort("employmentPositionCandidateRejectReasonCode"));
							readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO
									.setLastUpdateSystemUserId(results
											.getString("lastUpdateSystemUserId"));
							readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO
									.setLastUpdateTimestamp(results
											.getTimestamp("lastUpdateTimestamp"));
							readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO
									.setDisplayReasonCode(results
											.getString("displayReasonCode"));
							readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO
									.setShortReasonDescription(results
											.getString("shortReasonDescription"));
							readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO
									.setReasonDescription(results
											.getString("reasonDescription"));
							readNlsEmploymentPositionCandidateRejectedReasonByInputListList
									.add(readNlsEmploymentPositionCandidateRejectedReasonByInputListDTO);
						}
					}
				});

		if (logger.isDebugEnabled()) {
			logger
					.debug("finish readNlsEmploymentPositionCandidateRejectedReasonByInputList");
			logger
					.debug("returning "
							+ readNlsEmploymentPositionCandidateRejectedReasonByInputListList
									.size() + " item(s)");
		}
		return readNlsEmploymentPositionCandidateRejectedReasonByInputListList;
	}



	public boolean createEmploymentPositionCandidateRejected(String candID, int reqnNbr, int rjtCd)
			throws QueryException, RetailStaffingException {
		if (logger.isDebugEnabled()) {
			logger.debug("start createEmploymentPositionCandidateRejected");
		}
		final GenericResults result = new GenericResults();

		//get seqNbr
		int seqNbr = readMaximumSequenceNumberFromEmploymentPositionCandidateRejected(candID, reqnNbr) + 1 ;
		
		try
		{
			MapStream inputs = new MapStream(
					"createEmploymentPositionCandidateRejected");
			//TODO: Replace values below
			inputs.put("employmentPositionCandidateId", candID);
			inputs.put("employmentRequisitionNumber", reqnNbr);
			inputs.put("employmentPositionCandidateRejectReasonCode", (short) rjtCd);
			inputs.put("sequenceNumber", (short) seqNbr);
	
			if (logger.isDebugEnabled()) {
				logger.debug("executing the query");
			}
	
			//TODO: Verify contract name, business use id below
			BasicDAO.insertObject(WORKFORCEEMPLOYMENTQUALIFICATIONS_NAME,
					BUSINESS_USE_ID, WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION,
					inputs, new InsertNotifier() {
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
	
			if (logger.isDebugEnabled()) {
				logger.debug("finish createEmploymentPositionCandidateRejected");
				logger.debug("returning " + result.isSuccess());
			}
			return result.isSuccess();
		}catch(QueryException e)
		{
			throw new RetailStaffingException(INSERTING_REJECTION_DETAILS_ERROR_CODE, INSERTING_REJECTION_DETAILS_ERROR_MSG + "candidateid: "
					+ candID + " requisitionnumber: " + reqnNbr, e);
		}
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

	
	//returns max sequence number 
	//for a candidateID & requisition number
	//in EPOSN_CAND_RJTD
	public int readEmploymentPositionCandidateRejected(
			String candID, int reqNbr) throws QueryException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("start readEmploymentPositionCandidateRejected");
		}

		//get seqNbr
		int seqNbr = readMaximumSequenceNumberFromEmploymentPositionCandidateRejected(candID, reqNbr);
		
		//get rejection reason code
		final List<ReadEmploymentPositionCandidateRejectedDTO> readEmploymentPositionCandidateRejectedList = new ArrayList<ReadEmploymentPositionCandidateRejectedDTO>();
			
		MapStream inputs = new MapStream(
				"readEmploymentPositionCandidateRejected");
		
		inputs.put("employmentPositionCandidateId", candID);
		inputs.put("employmentRequisitionNumber", reqNbr);
		inputs.put("sequenceNumber", (short)seqNbr);

		if (logger.isDebugEnabled()) {
			logger.debug("executing the query");
		}

		//TODO: Verify contract name, business use id below
		BasicDAO.getResult(WORKFORCEEMPLOYMENTQUALIFICATIONS_NAME,
				BUSINESS_USE_ID, WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION,
				inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						ReadEmploymentPositionCandidateRejectedDTO readEmploymentPositionCandidateRejectedDTO = null;
						while (results.next()) {
							readEmploymentPositionCandidateRejectedDTO = new ReadEmploymentPositionCandidateRejectedDTO();
							readEmploymentPositionCandidateRejectedDTO.setEmploymentPositionCandidateRejectReasonCode(results.getShort("employmentPositionCandidateRejectReasonCode"));
							if (results.wasNull("employmentPositionCandidateRejectReasonCode")) {
								readEmploymentPositionCandidateRejectedDTO.setEmploymentPositionCandidateRejectReasonCode(0);
							}

							readEmploymentPositionCandidateRejectedList
									.add(readEmploymentPositionCandidateRejectedDTO);
						}
						
					}
				});

		if (logger.isDebugEnabled()) {
			logger
					.debug("finish readEmploymentPositionCandidateRejected");
			logger
					.debug("returning "
							+ readEmploymentPositionCandidateRejectedList
									.size() + " item(s)");
		}
		if(readEmploymentPositionCandidateRejectedList.size() == 0){
			return 0;
		} else {
			return readEmploymentPositionCandidateRejectedList.get(0).getEmploymentPositionCandidateRejectReasonCode();
		}
	}

	//returns max sequence number 
	//for a candidateID & requisition number
	//in EPOSN_CAND_RJTD
	public int readMaximumSequenceNumberFromEmploymentPositionCandidateRejected(
			String candID, int reqnNbr) throws QueryException {
		if (logger.isDebugEnabled()) {
			logger
					.debug("start readMaximumSequenceNumberFromEmploymentPositionCandidateRejected");
		}
		final List<ReadMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO> readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedList = new ArrayList<ReadMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO>();
	
		MapStream inputs = new MapStream(
				"readMaximumSequenceNumberFromEmploymentPositionCandidateRejected");
		//TODO: Replace values below
		inputs.put("employmentPositionCandidateId", candID);
		inputs.put("employmentRequisitionNumber", reqnNbr);
	
		if (logger.isDebugEnabled()) {
			logger.debug("executing the query");
		}
	
		//TODO: Verify contract name, business use id below
		BasicDAO.getResult(WORKFORCEEMPLOYMENTQUALIFICATIONS_NAME,
				BUSINESS_USE_ID, WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION,
				inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						ReadMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO = null;
						while (results.next()) {
							readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO = new ReadMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO();
							readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO
									.setMaximumSequenceNumber(results
											.getShort("maximumSequenceNumber"));
							if (results.wasNull("maximumSequenceNumber")) {
								readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO
										.setMaximumSequenceNumber(null);
							}
	
							readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedList
									.add(readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO);
						}
					}
				});
	
		if (logger.isDebugEnabled()) {
			logger
					.debug("finish readMaximumSequenceNumberFromEmploymentPositionCandidateRejected");
			logger
					.debug("returning "
							+ readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedList
									.size() + " item(s)");
		}
		if (readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedList.get(0).getMaximumSequenceNumber() == null)
		return 0 ;
		else
		return readMaximumSequenceNumberFromEmploymentPositionCandidateRejectedList.get(0).getMaximumSequenceNumber();
	}

	public static class ReadMaximumSequenceNumberFromEmploymentPositionCandidateRejectedDTO {

		//Instance variable for maximumSequenceNumber
		private Short maximumSequenceNumber;
		
		//getter method for maximumSequenceNumber
		public Short getMaximumSequenceNumber() {
			return maximumSequenceNumber;
		}

		//setter method for maximumSequenceNumber
		public void setMaximumSequenceNumber(Short aValue) {
			maximumSequenceNumber = aValue;
		}
	}
	
	public static class ReadEmploymentPositionCandidateRejectedDTO {

		//Instance variable for employmentPositionCandidateRejectReasonCode
		private int employmentPositionCandidateRejectReasonCode;
		
		//Instance variable for lastUpdateSystemUserId
		private String lastUpdateSystemUserId;
		
		//Instance variable for lastUpdateTimestamp
		private String lastUpdateTimestamp;

		//getter method for employmentPositionCandidateRejectReasonCode
		public int getEmploymentPositionCandidateRejectReasonCode() {
			return employmentPositionCandidateRejectReasonCode;
		}

		//setter method for employmentPositionCandidateRejectReasonCode
		public void setEmploymentPositionCandidateRejectReasonCode(int aValue) {
			employmentPositionCandidateRejectReasonCode = aValue;
		}

		public String getLastUpdateSystemUserId() {
			return lastUpdateSystemUserId;
		}

		public void setLastUpdateSystemUserId(String lastUpdateSystemUserId) {
			this.lastUpdateSystemUserId = lastUpdateSystemUserId;
		}

		public String getLastUpdateTimestamp() {
			return lastUpdateTimestamp;
		}

		public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
			this.lastUpdateTimestamp = lastUpdateTimestamp;
		}
	}
}

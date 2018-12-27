/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingITIManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO.ReadActiveRequisitionsByStoreTO;
import com.homedepot.hr.hr.retailstaffing.dto.PagingRecordInfo;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.ITIUpdateRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.model.PhoneScreenManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.ObjectReader;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.JavaBeanStream;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to have the buisness logic for Search based on ITI Number,
 * ITI Status. The Class will have the following functionalitie:
 * updateInsertITIDetails - Updating the phone screen details.
 * getPhoneScreenIntrwDtlsByDiv - Getting phone screen details by division(Basic
 * and Detailed). getPhoneScreenIntrwDtlsByReg - Getting phone screen details by
 * region(Basic and Detailed). getPhoneScreenIntrwDtlsByDist - Getting phone
 * screen details by district(Basic and Detailed).
 * getPhoneScreenIntrwDtlsByStore - Getting phone screen details by store(Basic
 * and Detailed). getReviewCompletedPhoneScreenIntrwDtlsByDiv - Getting
 * completed phone screen details by division.
 * getReviewCompletedPhoneScreenIntrwDtlsByReg - Getting completed phone screen
 * details by region. getReviewCompletedPhoneScreenIntrwDtlsByDist - Getting
 * completed phone screen details by district.
 * getReviewCompletedPhoneScreenIntrwDtlsByStore - Getting completed phone
 * screen details by store.
 * 
 * @author TCS
 * 
 */
public class RetailStaffingITIDAO implements DAOConstants, RetailStaffingConstants {
	private static final Logger logger = Logger.getLogger(RetailStaffingITIDAO.class);

	/**
	 * This method will be used for updating the phone screen details in DB.
	 * 
	 * @param itiUpdate
	 *            - the object containing the phone screen details to be
	 *            updated.
	 * 
	 * @throws RetailStaffingException
	 */
	public void updateITIDetails(final ITIUpdateRequest itiUpdate) throws RetailStaffingException {

		logger.info(this + "Enters updateITIDetails method in DAO with input as" + itiUpdate);

		try {
			// Added for RSA3.0
			if (itiUpdate.getPhnScrnDtlTo() != null) {
				PhoneScreenManager.updateITIDetails(itiUpdate.getPhnScrnDtlTo());
				if (itiUpdate.getPhnScrnDtlTo().getAid() != null && !itiUpdate.getPhnScrnDtlTo().getAid().trim().equals(EMPTY_STRING)) {
					// Set internal if AID is present
					itiUpdate.getPhnScrnDtlTo().setInternalExternal(INTERNAL_FLAG);
				} else {
					itiUpdate.getPhnScrnDtlTo().setInternalExternal(EXTERNAL_FLAG);
				}
			}


		} catch (Exception e) {
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, UPDATING_PHONE_DETAILS_ERROR_MSG + "phone screen number: "
					+ itiUpdate.getPhnScrnDtlTo().getItiNbr(), e);
		}
	}

	/**
	 * This method will be used for updating all statuses in HR_PHN_SCRN.
	 * 
	 * @param itiUpdate
	 *            - the object containing the phone screen details to be
	 *            updated.
	 * 
	 * @throws RetailStaffingException
	 */
	public void updateAllStatuses(final ITIUpdateRequest itiUpdate) throws RetailStaffingException {

		logger.info(this + "Enters updateAllStatuses method in DAO with input as" + itiUpdate);

		try {
			// Added for RSA3.0
			if (itiUpdate.getPhnScrnDtlTo() != null) {
				PhoneScreenManager.updateAllStatuses(itiUpdate.getPhnScrnDtlTo());
			}

		} catch (Exception e) {
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, UPDATING_PHONE_DETAILS_ERROR_MSG + "phone screen number: "
					+ itiUpdate.getPhnScrnDtlTo().getItiNbr(), e);
		}
	}

	/**
	 * This method will be used for updating the Interview screen details in DB.
	 * 
	 * @param itiUpdate
	 *            - the object containing the phone screen details to be
	 *            updated.
	 * 
	 * @throws RetailStaffingException
	 */

	public void updateInterviewScrnDetails(final ITIUpdateRequest itiUpdate,String[] checkBeginTimeValue) throws RetailStaffingException {

		logger.info(this + "Enters updateInterviewScrnDetails method in DAO with input as" + itiUpdate);

		try {
			
			if( itiUpdate.getPhnScrnDtlTo() != null)
			{
				if (supportAimsRequisition(itiUpdate)) {		
					//updating interview Screen details, interview Screen Status and Interview material status code.
					PhoneScreenManager.updateInterviewSchedulePhoneScrnDetails(itiUpdate.getPhnScrnDtlTo(),checkBeginTimeValue);					
					
				}	
				else
				{
					//updating interview material status and status for AIMS DATA only.
					PhoneScreenManager.updateInterviewStatusAndMaterialStatusCode(itiUpdate.getPhnScrnDtlTo() );
				}
				
			}	

		} catch (Exception e) {
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, UPDATING_PHONE_DETAILS_ERROR_MSG + "phone screen number: "
					+ itiUpdate.getPhnScrnDtlTo().getItiNbr(), e);
		}
	}

	public Boolean supportAimsRequisition(final ITIUpdateRequest itiUpdate) {

		int calendarId = 1;

		try {
			List<RequisitionDetailTO> reqList = new ArrayList<RequisitionDetailTO>();
			RequisitionDetailTO reqDtl = null;
			PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();

			logger.info(this + "Before supportAimsReqestion Insert" + itiUpdate.getPhnScrnDtlTo().getReqNbr().toString());
			// getting the requisition details
			reqList = phnScrnDAO.readRequisitionDetails(Integer.parseInt(itiUpdate.getPhnScrnDtlTo().getReqNbr().toString()));

			if (reqList != null && !reqList.isEmpty() && reqList.size() > 0) {
				reqDtl = reqList.get(0);
			}

			calendarId = reqDtl.getReqCalId();

			if (calendarId <= 0) {
				logger.info(String.format("Check for AIMS or Webform  Requisition Invalid calendar id %1$d provided", calendarId));
				return false;
			} // end if(calendarId <= 0)

			if (Short.parseShort(reqDtl.getInterviewDurtn()) != SCHEDULING_INTERVAL_30
					&& Short.parseShort(reqDtl.getInterviewDurtn()) != SCHEDULING_INTERVAL_60) {
				logger.info(String.format("Check for AIMS Requisition Invalid duration provided"));
				return false;
			}

		} catch (Exception e) {
			logger.info(String.format("Exception handled in Invalid calendar id %1$d provided", calendarId));
		}
		return true;
	}

	/**
	 * This method is used for inserting the phone screen details in DB.
	 * 
	 * @param itiUpdate
	 *            - the object containing the phone screen details to be
	 *            inserted.
	 * 
	 * @throws RetailStaffingException
	 */
	public void insertITIDetails(final ITIUpdateRequest itiUpdate) throws RetailStaffingException {

		logger.info(this + "Enters insertITIDetails method in DAO with input as" + itiUpdate);

		try {
			// Added for RSA3.0
			if (itiUpdate.getPhnScrnDtlTo() != null) {
				PhoneScreenManager.insertITIDetails(itiUpdate.getPhnScrnDtlTo());
			}

		} catch (Exception e) {
			throw new RetailStaffingException(INSERTING_PHONE_DETAILS_ERROR_CODE, INSERTING_PHONE_DETAILS_ERROR_MSG + "requisitionnumber: "
					+ itiUpdate.getPhnScrnDtlTo().getReqNbr() + "cadidate id: " + itiUpdate.getPhnScrnDtlTo().getCndtNbr(), e);
		}
	}

	/**
	 * This method will be used to check the existence of the given requisition
	 * number and candidate id combination in phone screen detail table.
	 * 
	 * @param itiUpdate
	 *            - the object containing the requisition number and candidate
	 *            ssn number.
	 * @return - true - if the record exists. - false - if the record does not
	 *         exist.
	 * @throws RetailStaffingException
	 */
	public boolean checkRequisitionCandidateForExistence(final ITIUpdateRequest itiUpdate) throws RetailStaffingException {
		boolean exists = false;
		final StringBuffer status = new StringBuffer();
		MapStream inputs = new MapStream("readHumanResourcesPhoneScreenCandidateIdForExistence");

		try {
			inputs.put("employmentPositionCandidateId", itiUpdate.getPhnScrnDtlTo().getCndtNbr());
			inputs.put("employmentRequisitionNumber", Integer.parseInt(itiUpdate.getPhnScrnDtlTo().getReqNbr()));

			BasicDAO.getObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ObjectReader() {
				public void readObject(Object target, Query query, Inputs inputs) throws QueryException {

					if (target != null && (Boolean) target) {
						status.append("true");
					} else {
						status.append("false");
					}
				}
			});

			if (status.length() > 0) {
				if ("true".equalsIgnoreCase(status.toString())) {
					exists = true;
				}
			}
		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "candidateid: "
					+ itiUpdate.getPhnScrnDtlTo().getCndtNbr() + "requisitionnumber: " + itiUpdate.getPhnScrnDtlTo().getReqNbr(), e);
		}
		logger.info("The requisition candidate combination  existence status is: " + exists);
		return exists;
	}

	/**
	 * This method is used to return the detiled phone screens by org
	 * 
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getPhoneScreenIntrwDtlsByStore(short status, final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> statusList = new ArrayList<Short>();
		List<Short> interviewStatusList = new ArrayList<Short>();
		List<Short> reqStatusList = new ArrayList<Short>();
		logger.info(this + "Enters getPhoneScreenIntrwDtlsByStore method in DAO ");
		try {
			ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
			inputsList.setHumanResourcesSystemStoreNumber(summReq.getOrgID());

			// PhoneScreenStatusCodeList
			if (status != SCHEDULEINTERVIEW_ITI_STATUS) {
				statusList.add(status);
				inputsList.setPhoneScreenStatusCodeList(statusList);
			} else {
				inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
			}

			// InterviewStatusCodeList
			// Schedule Interview Status
			if (status == SCHEDULEINTERVIEW_ITI_STATUS) {
				interviewStatusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
				inputsList.setInterviewStatusCodeList(interviewStatusList);
			} else {
				inputsList.setInterviewStatusCodeList(ALL_POSSIBLE_STATUSES);
			}

			// InterviewMaterialStatusCodeList
			inputsList.setInterviewMaterialStatusCodeList(ALL_POSSIBLE_STATUSES);

			// RequisitionStatusCodeList
			reqStatusList.add(REQ_STATUS_CD_IN_PROCESS);
			inputsList.setRequisitionStatusCodeList(reqStatusList);

			JavaBeanStream inputList = new JavaBeanStream(inputsList);
			inputList.setSelectorName("readPhoneDetailsByStoreAndStatus");

			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));	
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));
					}
					inputList.addQualifier("paginationToken", paginationToken);

				}
				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {
				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.

						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// reqDetMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}
						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						if (!results.wasNull("candidatePersonId")) {
							itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						}
						itiDet.setName(StringUtils.trim(results.getString("candidateName")));
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setCndDeptNbr(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));
						
						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						itiDtlList.add(itiDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});

			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}

				}
			}
			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);
		} catch (QueryException e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "storeid: " + summReq.getOrgID(),
					e);
		}
		logger.info(this + "Leaves getPhoneScreenIntrwDtlsByStore method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}

	/**
	 * This method is used to return the detailed phone screens by region
	 * 
	 * @return map stream which contains phone screen interview details
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getPhoneScreenIntrwDtlsByReg(short status, final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> statusList = new ArrayList<Short>();
		List<Short> reqStatusList = new ArrayList<Short>();
		List<Short> interviewStatusList = new ArrayList<Short>();
		ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
		inputsList.setHumanResourcesSystemOperationsGroupCode(summReq.getOrgID());

		inputsList.setHumanResourcesSystemDivisionCode(summReq.getOrgID());
		// inputsList.setOverallRespondStatusCode(status);

		// PhoneScreenStatusCodeList
		if (status != SCHEDULEINTERVIEW_ITI_STATUS) {
			statusList.add(status);
			inputsList.setPhoneScreenStatusCodeList(statusList);
		} else {
			inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
		}

		// InterviewStatusCodeList
		// Schedule Interview Status
		if (status == SCHEDULEINTERVIEW_ITI_STATUS) {
			interviewStatusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
			inputsList.setInterviewStatusCodeList(interviewStatusList);
		} else {
			inputsList.setInterviewStatusCodeList(ALL_POSSIBLE_STATUSES);
		}

		// InterviewMaterialStatusCodeList
		inputsList.setInterviewMaterialStatusCodeList(ALL_POSSIBLE_STATUSES);

		// RequisitionStatusCodeList
		reqStatusList.add(REQ_STATUS_CD_IN_PROCESS);
		inputsList.setRequisitionStatusCodeList(reqStatusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readPhoneDetailsByRegionAndStatus");
		logger.info(this + "Enters getPhoneScreenIntrwDtlsByReg method in DAO ");

		try {
			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);

				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// reqDetMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}

						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						itiDet.setName(StringUtils.trim(results.getString("candidateName")));
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setCndDeptNbr(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));
												
						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						itiDtlList.add(itiDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}

			});

			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}
				}
			}

			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);

		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE,
					FETCHING_PHONE_DETAILS_ERROR_MSG + "regionid: " + summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getPhoneScreenIntrwDtlsByReg method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}

	/**
	 * This method is used to return the detailed phone screens by district
	 * 
	 * @return map stream which contains phone screen interview details by
	 *         district
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getPhoneScreenIntrwDtlsByDist(short status, final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> statusList = new ArrayList<Short>();
		List<Short> reqStatusList = new ArrayList<Short>();
		List<Short> interviewStatusList = new ArrayList<Short>();
		ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
		inputsList.setHumanResourcesSystemRegionCode(summReq.getOrgID());

		// PhoneScreenStatusCodeList
		if (status != SCHEDULEINTERVIEW_ITI_STATUS) {
			statusList.add(status);
			inputsList.setPhoneScreenStatusCodeList(statusList);
		} else {
			inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
		}

		// InterviewStatusCodeList
		// Schedule Interview Status
		if (status == SCHEDULEINTERVIEW_ITI_STATUS) {
			interviewStatusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
			inputsList.setInterviewStatusCodeList(interviewStatusList);
		} else {
			inputsList.setInterviewStatusCodeList(ALL_POSSIBLE_STATUSES);
		}

		// InterviewMaterialStatusCodeList
		inputsList.setInterviewMaterialStatusCodeList(ALL_POSSIBLE_STATUSES);
		// RequisitionStatusCodeList
		reqStatusList.add(REQ_STATUS_CD_IN_PROCESS);
		inputsList.setRequisitionStatusCodeList(reqStatusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readPhoneDetailsByDistrictAndStatus");
		logger.info(this + "Enters getPhoneScreenIntrwDtlsByDist method in DAO ");

		try {
			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);

				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// itiDtlMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}

						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						itiDet.setName(StringUtils.trim(results.getString("candidateName")));
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setCndDeptNbr(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));

						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						itiDtlList.add(itiDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}

			});

			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}
				}
			}

			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);

		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "districtid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getPhoneScreenIntrwDtlsByDist method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}

	/**
	 * This method is used to return the detailed phone screens by division
	 * 
	 * @return map stream which contains phone screen interview details by
	 *         division
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getPhoneScreenIntrwDtlsByDiv(short status, final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> statusList = new ArrayList<Short>();
		List<Short> reqStatusList = new ArrayList<Short>();
		List<Short> interviewStatusList = new ArrayList<Short>();
		ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
		inputsList.setHumanResourcesSystemDivisionCode(summReq.getOrgID());

		// PhoneScreenStatusCodeList
		if (status != SCHEDULEINTERVIEW_ITI_STATUS) {
			statusList.add(status);
			inputsList.setPhoneScreenStatusCodeList(statusList);
		} else {
			inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
		}

		// InterviewStatusCodeList
		// Schedule Interview Status
		if (status == SCHEDULEINTERVIEW_ITI_STATUS) {
			interviewStatusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
			inputsList.setInterviewStatusCodeList(interviewStatusList);
		} else {
			inputsList.setInterviewStatusCodeList(ALL_POSSIBLE_STATUSES);
		}

		// InterviewMaterialStatusCodeList
		inputsList.setInterviewMaterialStatusCodeList(ALL_POSSIBLE_STATUSES);
		// RequisitionStatusCodeList
		reqStatusList.add(REQ_STATUS_CD_IN_PROCESS);
		inputsList.setRequisitionStatusCodeList(reqStatusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readPhoneDetailsByDivisionAndStatus");
		logger.info(this + "Enters getPhoneScreenIntrwDtlsByDiv method in DAO ");

		try {
			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);

				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// itiDtlMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}

						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						if (!results.wasNull("candidatePersonId")) {
							itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						}
						itiDet.setName(StringUtils.trim(results.getString("candidateName")));
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setCndDeptNbr(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));
												
						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						itiDtlList.add(itiDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});
			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}
				}
			}
			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);

		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "divid: " + summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getPhoneScreenIntrwDtlsByDiv method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}

	/**
	 * This method is used to return the review completed phone screens by store
	 * 
	 * @return map stream which contains phone screen interview details by store
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getReviewCompletedPhoneScreenIntrwDtlsByStore(final SummaryRequest summReq) throws RetailStaffingException {

		// final List<PhoneScreenIntrwDetailsTO> itiDtlList = new
		// ArrayList<PhoneScreenIntrwDetailsTO>();
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemStoreNumber(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		inputsList.setAuthorizationPositionCount(AuthorizationPositionCount);
		inputsList.setRequisitionStatusCode(REQ_STATUS_CD);
		List<Short> statusList = new ArrayList<Short>();
		// statusList.add(PHONE_SCREEN_COMPLETED_STAT);
		// statusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
		// statusList.add(INTERVIEW_SCHEDULED);
		// inputsList.setOverallRespondStatusCodeList(statusList);
		statusList.add(BASIC_ITI_STATUS);
		statusList.add(DETAILED_ITI_STATUS);
		inputsList.setPhoneScreenStatusCodeList(statusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readRequisitionsAndCompletePhoneDetailsByStore");
		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByStore method in DAO ");
		try {
			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {
						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// reqDetMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}
						reqDet = new RequisitionDetailTO();
						reqDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						if (results.getTimestamp("createTimestamp") != null) {
							reqDet.setDateCreate(Util.converTimeStampTOtoDateTO(results.getTimestamp("createTimestamp")).toString());
						}
						reqDet.setCreator(StringUtils.trim(results.getString("createUserId")));
						reqDet.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						reqDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						reqDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						if (results.getString("oe31") != null) {
							reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
						}
						if (!results.wasNull("requiredPositionFillDate")) {
							reqDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}
						reqDet.setOpenings(Short.toString(results.getShort("openPositionCount")));
						if (results.getBoolean("fullTimeRequiredFlag") == true) {
							reqDet.setFt(TRUE);
						} else {
							reqDet.setFt(FALSE);
						}
						if (results.getBoolean("partTimeRequiredFlag") == true) {
							reqDet.setPt(TRUE);
						} else {
							reqDet.setPt(FALSE);
						}
						reqDet.setHumanResourcesSystemRegionCode(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						reqDet.setHumanResourcesSystemOperationsGroupCode(StringUtils.trim(results
								.getString("humanResourcesSystemOperationsGroupCode")));
						reqDet.setStore(summReq.getOrgID());
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));						
						reqDet.setRscToManageFlg(results.getString("retailStaffingCenterRequisitionManagedIndicator"));
						
						reqDetList.add(reqDet);
					}

					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					reqDetMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});
			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (reqDetList != null && reqDetList.size() > 0) {
						Collections.reverse(reqDetList);
					}
				}
			}
			reqDetMap.put(REQ_DTL_LIST, reqDetList);
		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_REQUISITIONNOTE_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "divisionid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByStore method in DAO with output as" + reqDetList);
		return reqDetMap;
	}

	/**
	 * This method is used to return the review completed phone screens by
	 * region
	 * 
	 * @return map stream which contains phone screen interview details by
	 *         region
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getReviewCompletedPhoneScreenIntrwDtlsByReg(final SummaryRequest summReq) throws RetailStaffingException {

		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemOperationsGroupCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		inputsList.setAuthorizationPositionCount(AuthorizationPositionCount);
		inputsList.setRequisitionStatusCode(REQ_STATUS_CD);
		List<Short> statusList = new ArrayList<Short>();
		// statusList.add(PHONE_SCREEN_COMPLETED_STAT);
		// statusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
		// statusList.add(INTERVIEW_SCHEDULED);
		// inputsList.setOverallRespondStatusCodeList(statusList);
		statusList.add(BASIC_ITI_STATUS);
		statusList.add(DETAILED_ITI_STATUS);
		inputsList.setPhoneScreenStatusCodeList(statusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readRequisitionsAndCompletePhoneDetailsByRegion");
		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByReg method in DAO ");
		try {
			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {

						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// reqDetMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}
						reqDet = new RequisitionDetailTO();
						reqDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						if (results.getTimestamp("createTimestamp") != null) {
							reqDet.setDateCreate(Util.converTimeStampTOtoDateTO(results.getTimestamp("createTimestamp")).toString());
						}
						reqDet.setCreator(StringUtils.trim(results.getString("createUserId")));
						reqDet.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						reqDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						reqDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						if (results.getString("oe31") != null) {
							reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
						}
						if (!results.wasNull("requiredPositionFillDate")) {
							reqDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}
						reqDet.setOpenings(Short.toString(results.getShort("openPositionCount")));
						if (results.getBoolean("fullTimeRequiredFlag") == true) {
							reqDet.setFt(TRUE);
						} else {
							reqDet.setFt(FALSE);
						}
						if (results.getBoolean("partTimeRequiredFlag") == true) {
							reqDet.setPt(TRUE);
						} else {
							reqDet.setPt(FALSE);
						}
						reqDet.setHumanResourcesSystemRegionCode(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
						reqDet.setHumanResourcesSystemOperationsGroupCode(summReq.getOrgID());
						reqDet.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						reqDet.setRscToManageFlg(results.getString("retailStaffingCenterRequisitionManagedIndicator"));
						
						reqDetList.add(reqDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					reqDetMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});
			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (reqDetList != null && reqDetList.size() > 0) {
						Collections.reverse(reqDetList);
					}
				}
			}
			reqDetMap.put(REQ_DTL_LIST, reqDetList);
		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_REQUISITIONNOTE_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "regionid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByReg method in DAO with output as" + reqDetList);
		return reqDetMap;
	}

	/**
	 * This method is used to return the review completed screens by district
	 * 
	 * @return map stream which returns review completed phone screen by
	 *         district
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getReviewCompletedPhoneScreenIntrwDtlsByDist(final SummaryRequest summReq) throws RetailStaffingException {

		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemRegionCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		inputsList.setAuthorizationPositionCount(AuthorizationPositionCount);
		inputsList.setRequisitionStatusCode(REQ_STATUS_CD);
		List<Short> statusList = new ArrayList<Short>();
		// statusList.add(PHONE_SCREEN_COMPLETED_STAT);
		// statusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
		// statusList.add(INTERVIEW_SCHEDULED);
		// inputsList.setOverallRespondStatusCodeList(statusList);
		statusList.add(BASIC_ITI_STATUS);
		statusList.add(DETAILED_ITI_STATUS);
		inputsList.setPhoneScreenStatusCodeList(statusList);
		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readRequisitionsAndCompletePhoneDetailsByDistrict");
		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByDist method in DAO ");
		try {
			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {

						//paginationToken.put("CRT_TS", Util.convertDateTO(pagingRecordInfo.getUpdatedDate()));
						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// reqDetMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}
						reqDet = new RequisitionDetailTO();
						reqDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						if (results.getTimestamp("createTimestamp") != null) {
							reqDet.setDateCreate(Util.converTimeStampTOtoDateTO(results.getTimestamp("createTimestamp")).toString());
						}
						reqDet.setCreator(StringUtils.trim(results.getString("createUserId")));
						reqDet.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						reqDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						reqDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						if (results.getString("oe31") != null) {
							reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
						}
						if (!results.wasNull("requiredPositionFillDate")) {
							reqDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}
						reqDet.setOpenings(Short.toString(results.getShort("openPositionCount")));
						if (results.getBoolean("fullTimeRequiredFlag") == true) {
							reqDet.setFt(TRUE);
						} else {
							reqDet.setFt(FALSE);
						}
						if (results.getBoolean("partTimeRequiredFlag") == true) {
							reqDet.setPt(TRUE);
						} else {
							reqDet.setPt(FALSE);
						}
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
						reqDet.setHumanResourcesSystemOperationsGroupCode(StringUtils.trim(results
								.getString("humanResourcesSystemOperationsGroupCode")));
						reqDet.setHumanResourcesSystemRegionCode(summReq.getOrgID());
						reqDet.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						reqDet.setRscToManageFlg(results.getString("retailStaffingCenterRequisitionManagedIndicator"));
						
						reqDetList.add(reqDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					reqDetMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});
			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (reqDetList != null && reqDetList.size() > 0) {
						Collections.reverse(reqDetList);
					}
				}
			}
			reqDetMap.put(REQ_DTL_LIST, reqDetList);
		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_REQUISITIONNOTE_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "distid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByDist method in DAO with output as" + reqDetList);
		return reqDetMap;
	}

	/**
	 * This method is used to return the review completed screens by division
	 * 
	 * @return map stream which returns review completed phone screen by
	 *         division
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getReviewCompletedPhoneScreenIntrwDtlsByDiv(final SummaryRequest summReq) throws RetailStaffingException {

		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemDivisionCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		inputsList.setAuthorizationPositionCount(AuthorizationPositionCount);
		inputsList.setRequisitionStatusCode(REQ_STATUS_CD);
		List<Short> statusList = new ArrayList<Short>();
		// statusList.add(PHONE_SCREEN_COMPLETED_STAT);
		// statusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
		// statusList.add(INTERVIEW_SCHEDULED);
		// inputsList.setOverallRespondStatusCodeList(statusList);
		statusList.add(BASIC_ITI_STATUS);
		statusList.add(DETAILED_ITI_STATUS);
		inputsList.setPhoneScreenStatusCodeList(statusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readRequisitionsAndCompletePhoneDetailsByDivision");
		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByDiv method in DAO ");
		try {
			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					// pagingRecordInfo = summReq.getFirstRecordInfo();
					if (pagingRecordInfo != null) {

						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// reqDetMap
							// .put(
							// PAGINATION_TOKEN,
							// (Map<String, Object>) qualifiers
							// .getObject("paginationToken"));

							paginationTokenList.add(paginationToken);
						}
						reqDet = new RequisitionDetailTO();
						reqDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						if (results.getTimestamp("createTimestamp") != null) {
							reqDet.setDateCreate(Util.converTimeStampTOtoDateTO(results.getTimestamp("createTimestamp")).toString());
						}
						reqDet.setCreator(StringUtils.trim(results.getString("createUserId")));
						reqDet.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
						reqDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						reqDet.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
						if (results.getString("oe31") != null) {
							reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
						}
						if (!results.wasNull("requiredPositionFillDate")) {
							reqDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}
						reqDet.setOpenings(Short.toString(results.getShort("openPositionCount")));
						if (results.getBoolean("fullTimeRequiredFlag") == true) {
							reqDet.setFt(TRUE);
						} else {
							reqDet.setFt(FALSE);
						}
						if (results.getBoolean("partTimeRequiredFlag") == true) {
							reqDet.setPt(TRUE);
						} else {
							reqDet.setPt(FALSE);
						}
						reqDet.setHumanResourcesSystemRegionCode(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						reqDet.setHumanResourcesSystemOperationsGroupCode(StringUtils.trim(results
								.getString("humanResourcesSystemOperationsGroupCode")));
						reqDet.setHumanResourcesSystemDivisionCode(summReq.getOrgID());
						reqDet.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						reqDet.setRscToManageFlg(results.getString("retailStaffingCenterRequisitionManagedIndicator"));
						
						reqDetList.add(reqDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					reqDetMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});
			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (reqDetList != null && reqDetList.size() > 0) {
						Collections.reverse(reqDetList);
					}
				}
			}
			reqDetMap.put(REQ_DTL_LIST, reqDetList);
		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_REQUISITIONNOTE_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "divisionid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByDiv method in DAO with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * This method is used to return the Send Interview Materials detail screens
	 * by store
	 * 
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getSendIntrwMaterialDtlsByStore(final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> interviewStatusCodeList = new ArrayList<Short>();
		List<Short> interviewMaterialStatusCodeList = new ArrayList<Short>();
		List<Short> requisitionStatusCodeList = new ArrayList<Short>();
		logger.info(this + "Enters getSendIntrwMaterialDtlsByStore method in DAO ");
		try {
			ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
			inputsList.setHumanResourcesSystemStoreNumber(summReq.getOrgID());

			// PhoneScreenStatusCodeList, Not used so allow for all statuses
			inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
			// InterviewStatusCodeList
			interviewStatusCodeList.add(INTERVIEW_SCHEDULED);
			interviewStatusCodeList.add(STORE_SCHEDULING);
			inputsList.setInterviewStatusCodeList(interviewStatusCodeList);
			// InterviewMaterialStatusCodeList
			interviewMaterialStatusCodeList.add(SEND_CALENDAR_PACKET);
			interviewMaterialStatusCodeList.add(SEND_EMAIL_PACKET);
			inputsList.setInterviewMaterialStatusCodeList(interviewMaterialStatusCodeList);
			// RequisitionStatusCodeList, In Process
			requisitionStatusCodeList.add(REQ_STATUS_CD_IN_PROCESS);
			inputsList.setRequisitionStatusCodeList(requisitionStatusCodeList);

			JavaBeanStream inputList = new JavaBeanStream(inputsList);
			inputList.setSelectorName("readPhoneDetailsByStoreAndStatus");
			inputList.addQualifier("orderByRequiredPositionFillDate", Boolean.TRUE);

			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));
					}
					inputList.addQualifier("paginationToken", paginationToken);
				}
				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							Object paginationToken = results.getObject("paginationToken");
							paginationTokenList.add(paginationToken);
						}

						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndStrNbr(summReq.getOrgID());
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						if (!results.wasNull("candidatePersonId")) {
							itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						}
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));
						if (!results.wasNull("requiredPositionFillDate")) {
							itiDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}

						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						itiDtlList.add(itiDet);
						
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});

			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}

				}
			}
			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);
		} catch (QueryException e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "storeid: " + summReq.getOrgID(),
					e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialDtlsByStore method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}

	/**
	 * This method is used to return the Send Interview Materials detail screens
	 * by district
	 * 
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getSendIntrwMaterialDtlsByDist(final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> interviewStatusCodeList = new ArrayList<Short>();
		List<Short> interviewMaterialStatusCodeList = new ArrayList<Short>();
		List<Short> requisitionStatusCodeList = new ArrayList<Short>();

		logger.info(this + "Enters getSendIntrwMaterialDtlsByDist method in DAO ");
		try {
			ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
			inputsList.setHumanResourcesSystemRegionCode(summReq.getOrgID());

			// PhoneScreenStatusCodeList, Not used so allow for all statuses
			inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
			// InterviewStatusCodeList
			interviewStatusCodeList.add(INTERVIEW_SCHEDULED);
			interviewStatusCodeList.add(STORE_SCHEDULING);
			inputsList.setInterviewStatusCodeList(interviewStatusCodeList);
			// InterviewMaterialStatusCodeList
			interviewMaterialStatusCodeList.add(SEND_CALENDAR_PACKET);
			interviewMaterialStatusCodeList.add(SEND_EMAIL_PACKET);
			inputsList.setInterviewMaterialStatusCodeList(interviewMaterialStatusCodeList);
			// RequisitionStatusCodeList, In Process
			requisitionStatusCodeList.add(REQ_STATUS_CD_IN_PROCESS);
			inputsList.setRequisitionStatusCodeList(requisitionStatusCodeList);

			JavaBeanStream inputList = new JavaBeanStream(inputsList);
			inputList.setSelectorName("readPhoneDetailsByDistrictAndStatus");
			inputList.addQualifier("orderByRequiredPositionFillDate", Boolean.TRUE);

			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));
					}
					inputList.addQualifier("paginationToken", paginationToken);
				}
				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							Object paginationToken = results.getObject("paginationToken");
							paginationTokenList.add(paginationToken);
						}

						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndStrNbr(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						if (!results.wasNull("candidatePersonId")) {
							itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						}
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));
						if (!results.wasNull("requiredPositionFillDate")) {
							itiDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}
						
						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						itiDtlList.add(itiDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});

			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}

				}
			}
			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);
		} catch (QueryException e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "DistId: " + summReq.getOrgID(),
					e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialDtlsByDist method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}

	/**
	 * This method is used to return the Send Interview Materials detail screens
	 * by Region
	 * 
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getSendIntrwMaterialDtlsByReg(final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> interviewStatusCodeList = new ArrayList<Short>();
		List<Short> interviewMaterialStatusCodeList = new ArrayList<Short>();
		List<Short> requisitionStatusCodeList = new ArrayList<Short>();

		logger.info(this + "Enters getSendIntrwMaterialDtlsByReg method in DAO ");
		try {
			ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
			inputsList.setHumanResourcesSystemOperationsGroupCode(summReq.getOrgID());

			// PhoneScreenStatusCodeList, Not used so allow for all statuses
			inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
			// InterviewStatusCodeList
			interviewStatusCodeList.add(INTERVIEW_SCHEDULED);
			interviewStatusCodeList.add(STORE_SCHEDULING);
			inputsList.setInterviewStatusCodeList(interviewStatusCodeList);
			// InterviewMaterialStatusCodeList
			interviewMaterialStatusCodeList.add(SEND_CALENDAR_PACKET);
			interviewMaterialStatusCodeList.add(SEND_EMAIL_PACKET);
			inputsList.setInterviewMaterialStatusCodeList(interviewMaterialStatusCodeList);
			// RequisitionStatusCodeList, In Process
			requisitionStatusCodeList.add(REQ_STATUS_CD_IN_PROCESS);
			inputsList.setRequisitionStatusCodeList(requisitionStatusCodeList);

			JavaBeanStream inputList = new JavaBeanStream(inputsList);
			inputList.setSelectorName("readPhoneDetailsByRegionAndStatus");
			inputList.addQualifier("orderByRequiredPositionFillDate", Boolean.TRUE);

			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}
				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							Object paginationToken = results.getObject("paginationToken");
							paginationTokenList.add(paginationToken);
						}

						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndStrNbr(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						if (!results.wasNull("candidatePersonId")) {
							itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						}
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));
						if (!results.wasNull("requiredPositionFillDate")) {
							itiDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}
						
						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						itiDtlList.add(itiDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});

			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}

				}
			}
			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);
		} catch (QueryException e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "DistId: " + summReq.getOrgID(),
					e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialDtlsByDist method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}

	/**
	 * This method is used to return the Send Interview Materials detail screens
	 * by Division
	 * 
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getSendIntrwMaterialDtlsByDiv(final SummaryRequest summReq) throws RetailStaffingException {

		final List<PhoneScreenIntrwDetailsTO> itiDtlList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		final Map<String, Object> itiDtlMap = new HashMap<String, Object>();
		List<Short> interviewStatusCodeList = new ArrayList<Short>();
		List<Short> interviewMaterialStatusCodeList = new ArrayList<Short>();
		List<Short> requisitionStatusCodeList = new ArrayList<Short>();

		logger.info(this + "Enters getSendIntrwMaterialDtlsByDiv method in DAO ");
		try {
			ReadPhoneDetailsByDistrictAndStatusTO inputsList = new ReadPhoneDetailsByDistrictAndStatusTO();
			inputsList.setHumanResourcesSystemDivisionCode(summReq.getOrgID());

			// PhoneScreenStatusCodeList, Not used so allow for all statuses
			inputsList.setPhoneScreenStatusCodeList(ALL_POSSIBLE_STATUSES);
			// InterviewStatusCodeList
			interviewStatusCodeList.add(INTERVIEW_SCHEDULED);
			interviewStatusCodeList.add(STORE_SCHEDULING);
			inputsList.setInterviewStatusCodeList(interviewStatusCodeList);
			// InterviewMaterialStatusCodeList
			interviewMaterialStatusCodeList.add(SEND_CALENDAR_PACKET);
			interviewMaterialStatusCodeList.add(SEND_EMAIL_PACKET);
			inputsList.setInterviewMaterialStatusCodeList(interviewMaterialStatusCodeList);
			// RequisitionStatusCodeList, In Process
			requisitionStatusCodeList.add(REQ_STATUS_CD_IN_PROCESS);
			inputsList.setRequisitionStatusCodeList(requisitionStatusCodeList);

			JavaBeanStream inputList = new JavaBeanStream(inputsList);
			inputList.setSelectorName("readPhoneDetailsByDivisionAndStatus");
			inputList.addQualifier("orderByRequiredPositionFillDate", Boolean.TRUE);

			// Case when pagination is there.
			if (summReq.getPaginationInfoTO() != null) {
				// Case when the hit to the service is not the first hit.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					PagingRecordInfo pagingRecordInfo = null;
					Map<String, Object> paginationToken = new HashMap<String, Object>();
					if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
						pagingRecordInfo = summReq.getSecondRecordInfo();
					} else {
						pagingRecordInfo = summReq.getFirstRecordInfo();
					}
					if (pagingRecordInfo != null) {
						paginationToken.put("LAST_UPD_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("HR_PHN_SCRN_ID", Integer.parseInt(pagingRecordInfo.getId()));
					}
					inputList.addQualifier("paginationToken", paginationToken);
				}
				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				PhoneScreenIntrwDetailsTO itiDet = null;

				@SuppressWarnings("unchecked")
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();
					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							Object paginationToken = results.getObject("paginationToken");
							paginationTokenList.add(paginationToken);
						}

						itiDet = new PhoneScreenIntrwDetailsTO();
						itiDet.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
						itiDet.setCndStrNbr(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						itiDet.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						if (!results.wasNull("candidatePersonId")) {
							itiDet.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						}
						itiDet.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
						itiDet.setJob(StringUtils.trim(results.getString("jobTitleCode")));
						itiDet.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("lastUpdateTimestamp")));
						if (!results.wasNull("requiredPositionFillDate")) {
							itiDet.setFillDt(Util.converDatetoDateTO(results.getDate("requiredPositionFillDate")).toString());
						}
						
						//Added for CDP, remove leading zeros for display purposes.
						itiDet.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));						
						
						itiDtlList.add(itiDet);
					}
					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if (paginationTokenList != null && paginationTokenList.size() > 0) {
						if (Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
						} else {
							pagingMap.put("FIRST_RECORD", (Map<String, Object>) paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>) paginationTokenList.get(0));
						}
					}
					itiDtlMap.put(PAGINATION_TOKEN, pagingMap);
				}
			});

			if (summReq.getPaginationInfoTO() != null) {
				// In case the previous link was clicked
				// then reverse the array.
				if (!Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward())) {
					if (itiDtlList != null && itiDtlList.size() > 0) {
						Collections.reverse(itiDtlList);
					}

				}
			}
			itiDtlMap.put(ITI_DTL_LIST, itiDtlList);
		} catch (QueryException e) {
			throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "DistId: " + summReq.getOrgID(),
					e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialDtlsByDist method in DAO with output as" + itiDtlList);
		return itiDtlMap;

	}
	
	// Updating Interview & material Status and Interview screen details 
	public  void updateInterviewScrnDetailsWithOutTimeSlot(final ITIUpdateRequest itiUpdate) throws RetailStaffingException 
	{

		logger.debug(this + "Enters updateInterviewScrnDetailsWithOutTimeSlots method in DAO with input as" + itiUpdate);

		try {	
			
			if( itiUpdate.getPhnScrnDtlTo() != null)
			{
					if (supportAimsRequisition(itiUpdate)) {		
						//updating interview Screen details, interview Screen Status and Interview material status code.
						PhoneScreenManager.updateInterviewStatusforPhoneScrn(itiUpdate.getPhnScrnDtlTo());						
					}	
					else
					{
						//updating interview material status and status for AIMS DATA only.
						PhoneScreenManager.updateInterviewStatusAndMaterialStatusCode(itiUpdate.getPhnScrnDtlTo() );
					}				
			}			

		} catch (Exception e) {
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, UPDATING_PHONE_DETAILS_ERROR_MSG + "phone screen number: "
					+ itiUpdate.getPhnScrnDtlTo().getItiNbr(), e);
		}
	}

	/**
	 * This class represents the value object which will be populated for
	 * passing the parameters to the DAO layer.
	 * 
	 * @author TCS
	 * 
	 */
	public static class ReadPhoneDetailsByDistrictAndStatusTO {
		private String humanResourcesSystemRegionCode;
		private String humanResourcesSystemOperationsGroupCode;
		private String humanResourcesSystemDivisionCode;
		private String humanResourcesSystemStoreNumber;

		private Short overallRespondStatusCode;
		private List<Short> overallRespondStatusCodeList;
		private List<Short> phoneScreenStatusCodeList;
		private List<Short> interviewStatusCodeList;
		private List<Short> interviewMaterialStatusCodeList;
		private List<Short> requisitionStatusCodeList;

		public String getHumanResourcesSystemRegionCode() {
			return humanResourcesSystemRegionCode;
		}

		public void setHumanResourcesSystemRegionCode(String humanResourcesSystemRegionCode) {
			this.humanResourcesSystemRegionCode = humanResourcesSystemRegionCode;
		}

		public Short getOverallRespondStatusCode() {
			return overallRespondStatusCode;
		}

		public void setOverallRespondStatusCode(Short overallRespondStatusCode) {
			this.overallRespondStatusCode = overallRespondStatusCode;
		}

		public String getHumanResourcesSystemOperationsGroupCode() {
			return humanResourcesSystemOperationsGroupCode;
		}

		public void setHumanResourcesSystemOperationsGroupCode(String humanResourcesSystemOperationsGroupCode) {
			this.humanResourcesSystemOperationsGroupCode = humanResourcesSystemOperationsGroupCode;
		}

		public String getHumanResourcesSystemDivisionCode() {
			return humanResourcesSystemDivisionCode;
		}

		public void setHumanResourcesSystemDivisionCode(String humanResourcesSystemDivisionCode) {
			this.humanResourcesSystemDivisionCode = humanResourcesSystemDivisionCode;
		}

		public String getHumanResourcesSystemStoreNumber() {
			return humanResourcesSystemStoreNumber;
		}

		public void setHumanResourcesSystemStoreNumber(String humanResourcesSystemStoreNumber) {
			this.humanResourcesSystemStoreNumber = humanResourcesSystemStoreNumber;
		}

		public List<Short> getOverallRespondStatusCodeList() {
			return overallRespondStatusCodeList;
		}

		public void setOverallRespondStatusCodeList(List<Short> overallRespondStatusCodeList) {
			this.overallRespondStatusCodeList = overallRespondStatusCodeList;
		}

		public List<Short> getPhoneScreenStatusCodeList() {
			return phoneScreenStatusCodeList;
		}

		public void setPhoneScreenStatusCodeList(List<Short> phoneScreenStatusCodeList) {
			this.phoneScreenStatusCodeList = phoneScreenStatusCodeList;
		}

		public List<Short> getInterviewStatusCodeList() {
			return interviewStatusCodeList;
		}

		public void setInterviewStatusCodeList(List<Short> interviewStatusCodeList) {
			this.interviewStatusCodeList = interviewStatusCodeList;
		}

		public List<Short> getInterviewMaterialStatusCodeList() {
			return interviewMaterialStatusCodeList;
		}

		public void setInterviewMaterialStatusCodeList(List<Short> interviewMaterialStatusCodeList) {
			this.interviewMaterialStatusCodeList = interviewMaterialStatusCodeList;
		}

		public List<Short> getRequisitionStatusCodeList() {
			return requisitionStatusCodeList;
		}

		public void setRequisitionStatusCodeList(List<Short> requisitionStatusCodeList) {
			this.requisitionStatusCodeList = requisitionStatusCodeList;
		}
	}

	public boolean updateApplicantEmailAddress(String cndtNbr, String emailTxt) throws QueryException {
		logger.info(this + "Enters updateApplicantEmailAddress method in PhoneScreenDAO for candidate Number" + cndtNbr);
		final UpdateApplicantEmailAddressResult result = new UpdateApplicantEmailAddressResult();

		MapStream inputs = new MapStream("updateApplicantEmailAddress");

		inputs.put("employmentApplicantId", cndtNbr);
		inputs.put("activeFlag", true);
		inputs.putAllowNull("electronicMailAddressText", emailTxt); // can be
		// null
		// MTS1876 This is where I need to change the email stuff,
		// Internal and External
		BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier() {
			public void notifyUpdate(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException {
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		logger.info(this + "Leaves updateApplicantEmailAddress method in PhoneScreenDAO");

		return result.isSuccess();
	}

	public static class UpdateApplicantEmailAddressResult {
		// This inner class was generated for convenience. You may move this
		// code
		// to an external class if desired.
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
		 * @param success
		 *            the success to set
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
		 * @param count
		 *            the count to set
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
		 * @param target
		 *            the target to set
		 */
		public void setTarget(Object target) {
			this.target = target;
		}
		
		
		
		
	}

	
	
	

}
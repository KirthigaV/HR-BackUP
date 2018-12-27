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
package com.homedepot.hr.hr.retailstaffing.dao;


import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.AutoAttachJobTitlesTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ExperienceLevelTO;
import com.homedepot.hr.hr.retailstaffing.dto.GenericResults;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.LanguageSkillsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PagingRecordInfo;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenCallHistoryTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenDispositionsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadThdStoreEmploymentRequisitionDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReqScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateStaffingRequest;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.model.StatusManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.InsertNotifier;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.basic.BasicInsertNotifier;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.JavaBeanStream;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to have the buisness logic for Search based on Requistion
 * Number. The class will have the following functionalities:-
 * getRequisitionDetails - reads the requisition details for a requisition.
 * updateInsertStaffingDetails - updates the staffing details for the
 * requisition. updateHireEventInfo - updates the hiring event info for the
 * requisition. updateEmploymentRequisitionNote - updates the employee
 * requisition note for the requisition. readEmploymentRequisitionNote - reads
 * the employment requisition note for the given requisition.
 * createEmploymentRequisitionNote - creates the employment requisition note for
 * the given requisition. createHumanResourcesHireEventRequisition - creates the
 * hiring event for the requisition. readRequisitionPhoneScreen - reads the
 * phone details for the requisition. readRequisitionCandidate - reads the
 * candidate details for the requisition. readStateList - reads the available
 * state list. getRequisitionsByStore - getting all available requisitions for
 * the given store. getRequisitionsByReg - getting all available requisitions
 * for the given region. getRequisitionsByDist - getting all available
 * requisitions for the given district. getRequisitionsByDiv - getting all the
 * available requisitions for the given division. readNlsExperienceLevelList -
 * read the available experiance level list.
 * 
 * @author TCS
 * 
 */
public class RetailStaffingRequisitionDAO implements DAOConstants, RetailStaffingConstants 
{
	private static final Logger logger = Logger.getLogger(RetailStaffingRequisitionDAO.class);

	/**
	 * This method is used to get the requisition details based on the
	 * requisition number.
	 * 
	 * @param reqNbr
	 * @return list of store details
	 * @throws RetailStaffingException
	 */
	public List<StoreDetailsTO> getRequisitionDetails(String reqNbr) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionDetails method in Manager with input " + reqNbr);
		final List<StoreDetailsTO> readRequisitionStrDetList = new ArrayList<StoreDetailsTO>();
		List<Object> inputLocationTypeCodeList = new ArrayList<Object>();
		MapStream inputs = new MapStream(READ_STORE_DETAILS);
		try {
			inputs.put("humanResourcesSystemStoreNumber", reqNbr);
			inputLocationTypeCodeList.add("STR");
			inputLocationTypeCodeList.add("DC");
			inputs.put("locationTypeCodeList", inputLocationTypeCodeList);

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					StoreDetailsTO strDetTO = null;
					while (results.next()) {
						strDetTO = new StoreDetailsTO();
						strDetTO.setAdd(String.valueOf(results.getDate("addressLineOneText")));
						strDetTO.setCity(String.valueOf(results.getTimestamp("cityName")));
						strDetTO.setZip(String.valueOf(results.getShort("longZipCodeCode")));
						strDetTO.setState(StringUtils.trim(results.getString("stateCode")));
						strDetTO.setCountryCode(StringUtils.trim(results.getString("countryCode")));
						if (!(results.wasNull("phoneNumber"))) {
							strDetTO.setPhone(StringUtils.trim(results.getString("phoneNumber")));
						}
						strDetTO.setReg(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						/*
						 * strDetTO.setFillDt(results.getString(
						 * "humanResourcesSystemOperationsGroupName"));
						 */
						strDetTO.setDiv(StringUtils.trim(results.getString("humanResourcesSystemDivisionName")));
						/*
						 * readRequisitionsDetailsDTO.setInterviewer(results
						 * .getString("Oe31"));
						 */
						readRequisitionStrDetList.add(strDetTO);
					}
				}
			});
		} catch (Exception e) {
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
		}
		return readRequisitionStrDetList;
	}

	/**
	 * This method is used to get the next Requisition Number
	 * 
	 * @param inputs
	 * @param updReq
	 * @return RequisitionDetailTO
	 * @throws QueryException
	 */
	public RequisitionDetailTO getNextRequisitionNbr(DAOConnection daoConn, Query query, MapStream inputs, UpdateStaffingRequest updReq)
			throws QueryException {

		final RequisitionDetailTO returnedMaxRequisitionNbr = new RequisitionDetailTO();

		logger.info(this + "Enters getnextRequisitionNbr method in Manager");

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {

			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					returnedMaxRequisitionNbr.setReqNbr(Integer.toString(results.getInt("maximumEmploymentRequisitionNumber") + 1));
				}
			}

		});
		return returnedMaxRequisitionNbr;
	}

	/**
	 * This method is used to update the THD_STR_EMPLT_REQN table with setting
	 * values in Mapstream.
	 * 
	 * @param staffingUpdate
	 * @return update success/failure details
	 * @throws RetailStaffingException
	 */
	public MapStream updateInsertStaffingDetails(MapStream inputs, final UpdateStaffingRequest staffingUpdate) throws QueryException {
		// null check for staffingUpdate
		if (staffingUpdate != null && staffingUpdate.getStaffingDtlTo() != null) {
			logger.info("The humanResourcesSystemReqNumber is " + staffingUpdate.getReqNbr());

			// Updating the ThdStoreEmploymentRequisition table
			inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));
			// null check for HRG Manager Name
			if (staffingUpdate.getStaffingDtlTo().getHrgMgrName() != null
					&& !staffingUpdate.getStaffingDtlTo().getHrgMgrName().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireManagerName", staffingUpdate.getStaffingDtlTo().getHrgMgrName().trim());// can
				// be
				// null
			} else {
				inputs.putAllowNull("hireManagerName", null);
			}
			// null check for jobTitleDescription
			if (staffingUpdate.getStaffingDtlTo().getJobTitle() != null
					&& !staffingUpdate.getStaffingDtlTo().getHrgMgrTtl().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("jobTitleDescription", staffingUpdate.getStaffingDtlTo().getHrgMgrTtl().trim()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("jobTitleDescription", null); // can be
				// null
			}
			// null check for hireManagerPhoneNumber
			if (staffingUpdate.getStaffingDtlTo().getHrgMgrPhn() != null && !staffingUpdate.getStaffingDtlTo().getHrgMgrPhn().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireManagerPhoneNumber", staffingUpdate.getStaffingDtlTo().getHrgMgrPhn().trim());// can
				// be
				// null
			} else {
				inputs.putAllowNull("hireManagerPhoneNumber", null); // can
				// be
				// null
			}
			// null check for requestNumber
			if (staffingUpdate.getStaffingDtlTo().getRequestNbr() != null && !staffingUpdate.getStaffingDtlTo().getRequestNbr().equals(EMPTY_STRING)) {
				inputs.putAllowNull("requestNumber", staffingUpdate.getStaffingDtlTo().getRequestNbr().trim()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("requestNumber", null); // can be null
			}
			// null check for targetExperienceLevelCode
			if (staffingUpdate.getStaffingDtlTo().getDesiredExp() != null && !staffingUpdate.getStaffingDtlTo().getDesiredExp().equals(EMPTY_STRING)) {
				inputs.putAllowNull("targetExperienceLevelCode", (short) Short.parseShort(staffingUpdate.getStaffingDtlTo().getDesiredExp().trim())); // can
				// be
				// null
			} else {
				inputs.putAllowNull("targetExperienceLevelCode", null); // can
				// be
				// null
			}
			// null check for targetPayAmount
			if (staffingUpdate.getStaffingDtlTo().getTargetPay() != null && !staffingUpdate.getStaffingDtlTo().getTargetPay().equals(EMPTY_STRING)) {
				inputs.putAllowNull("targetPayAmount", new BigDecimal(staffingUpdate.getStaffingDtlTo().getTargetPay().trim())); // can
				// be
				// null
			} else {
				inputs.putAllowNull("targetPayAmount", null);
			}
			// null check for hireEventFlag
			if (staffingUpdate.getStaffingDtlTo().getHrgEvntFlg() != null && !staffingUpdate.getStaffingDtlTo().getHrgEvntFlg().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventFlag", Util.convertToBoolean(staffingUpdate.getStaffingDtlTo().getHrgEvntFlg().trim())); // can
				// be
				// null
			} else {
				inputs.putAllowNull("hireEventFlag", null); // can be null
			}
			// null check for weekBeginDate
			if (staffingUpdate.getStaffingDtlTo().getWeekBeginDt() != null) {
				inputs.putAllowNull("weekBeginDate", Util.convertDateTO(staffingUpdate.getStaffingDtlTo().getWeekBeginDt())); // can
				// be
				// null
			} else {
				inputs.putAllowNull("weekBeginDate", null);
			}
			// null check for hireManagerAvailabilityText
			// Changed by MTS1876 10/12/2010 for NamesOfInterviewers coming from
			// Req request Form
			// if (staffingUpdate.getStaffingDtlTo().getWeekMgrAvble() != null
			// &&
			// !staffingUpdate.getStaffingDtlTo().getWeekMgrAvble().trim().equals(EMPTY_STRING))
			// {
			// inputs.putAllowNull("hireManagerAvailabilityText",
			// staffingUpdate.getStaffingDtlTo().getWeekMgrAvble().trim());
			if (staffingUpdate.getStaffingDtlTo().getNamesOfInterviewers() != null
					&& !staffingUpdate.getStaffingDtlTo().getNamesOfInterviewers().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireManagerAvailabilityText", staffingUpdate.getStaffingDtlTo().getNamesOfInterviewers().trim());
			} else {
				inputs.putAllowNull("hireManagerAvailabilityText", null);
			}

			// null check for requisitionStatusCode
			if (staffingUpdate.getStaffingDtlTo().getReqStatus() != null
					&& !staffingUpdate.getStaffingDtlTo().getReqStatus().trim().equals(EMPTY_STRING)) {

				inputs.putAllowNull("requisitionStatusCode", Short.parseShort(staffingUpdate.getStaffingDtlTo().getReqStatus().trim()));
			} else {
				inputs.putAllowNull("requisitionStatusCode", null);
			}

			if (staffingUpdate.getStaffingDtlTo().getRscSchdFlg() != null && !staffingUpdate.getStaffingDtlTo().getRscSchdFlg().equals(EMPTY_STRING)) {
				inputs.putAllowNull("rscScheduleFlag", Util.convertToBoolean(staffingUpdate.getStaffingDtlTo().getRscSchdFlg().trim())); // can
				// be
				// null
			} else {
				inputs.putAllowNull("rscScheduleFlag", null); // can be null
			}
			
			if (staffingUpdate.getStaffingDtlTo().getRscManageFlg() != null && !staffingUpdate.getStaffingDtlTo().getRscManageFlg().equals(EMPTY_STRING)) {
				inputs.putAllowNull("retailStaffingCenterRequisitionManagedIndicator", staffingUpdate.getStaffingDtlTo().getRscManageFlg().trim()); // can be null
			} else {
				inputs.putAllowNull("retailStaffingCenterRequisitionManagedIndicator", null); // can be null
			}			

			if (staffingUpdate.getStaffingDtlTo().getSealTempJob() != null
					&& !staffingUpdate.getStaffingDtlTo().getSealTempJob().equals(EMPTY_STRING)) {
				inputs.putAllowNull("applicantTemporaryFlag", Util.convertToBoolean(staffingUpdate.getStaffingDtlTo().getSealTempJob().trim())); // can
				// be
				// null
			} else {
				inputs.putAllowNull("applicantTemporaryFlag", null);// can be
				// null
			}

			// Number of requested interviews
			if (staffingUpdate.getStaffingDtlTo().getRequestedNbrIntvws() != null
					&& !staffingUpdate.getStaffingDtlTo().getRequestedNbrIntvws().equals(EMPTY_STRING)) {
				inputs.putAllowNull("interviewCandidateCount", (short) Short.parseShort(staffingUpdate.getStaffingDtlTo().getRequestedNbrIntvws()));
			} else {
				inputs.putAllowNull("interviewCandidateCount", (short) 0);
			}

			// Interview Duration MTS1876 09/26/10
			if (staffingUpdate.getStaffingDtlTo().getInterviewDurtn() != null
					&& !staffingUpdate.getStaffingDtlTo().getInterviewDurtn().equals(EMPTY_STRING)
					&& !staffingUpdate.getStaffingDtlTo().getInterviewDurtn().equalsIgnoreCase("NULL")) {
				inputs.putAllowNull("interviewMinutes", (short) Short.parseShort(staffingUpdate.getStaffingDtlTo().getInterviewDurtn()));
			} else {
				inputs.putAllowNull("interviewMinutes", null);
			}

			// Calendar ID MTS1876 09/26/10
			inputs.putAllowNull("requisitionCalendarId", Integer.parseInt(String.valueOf(staffingUpdate.getReqCalId())));

		} else {
			logger.info("The humanResourcesSystem no output");
			inputs.putAllowNull("hireManagerName", null);
			inputs.putAllowNull("jobTitleDescription", null); // can be null
			inputs.putAllowNull("hireManagerPhoneNumber", null); // can be
			// null
			inputs.putAllowNull("requestNumber", null); // can be null
			inputs.putAllowNull("targetExperienceLevelCode", null); // can
			// be
			// null
			inputs.putAllowNull("targetPayAmount", null);
			inputs.putAllowNull("hireEventFlag", null); // can be null
			inputs.putAllowNull("weekBeginDate", null);
			inputs.putAllowNull("hireManagerAvailabilityText", null);
			inputs.putAllowNull("rscScheduleFlag", null);
			inputs.putAllowNull("applicantTemporaryFlag", null);
			inputs.putAllowNull("retailStaffingCenterRequisitionManagedIndicator", null);
			inputs.putAllowNull("interviewMinutes", null);
			inputs.putAllowNull("requisitionCalendarId", null);
		}

		return inputs;
	}

	/**
	 * This method is used to update the HR_STR_EMPLT_REQN table with setting
	 * values in Mapstream.
	 * 
	 * @param staffingUpdate
	 * @return update success/failure details
	 * @throws RetailStaffingException
	 */
	public MapStream insertStaffingDetails(MapStream inputs, final UpdateStaffingRequest staffingUpdate) throws QueryException {
		// null check for staffingUpdate
		if (staffingUpdate != null && staffingUpdate.getStaffingDtlTo() != null) {
			logger.info("The humanResourcesSystemReqNumber is " + staffingUpdate.getReqNbr());

			// Updating the HR_STR_EMPLT_REQN table
			// Requisition Number
			inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));

			// Store Number
			inputs.put("humanResourcesSystemStoreNumber", StringUtils.trim(staffingUpdate.getStaffingDtlTo().getStrNo()));

			// Requestor ID
			if (staffingUpdate.getStaffingDtlTo().getRequestorId() != null
					&& !StringUtils.trim(staffingUpdate.getStaffingDtlTo().getRequestorId()).equals(EMPTY_STRING)) {
				inputs.put("createUserId", StringUtils.trim(staffingUpdate.getStaffingDtlTo().getRequestorId()));
			} else {
				inputs.put("createUserId", "UNKNOWN");
			}

			// Department Number
			inputs.put("humanResourcesSystemDepartmentNumber", StringUtils.trim(staffingUpdate.getStaffingDtlTo().getDeptNo()));

			// Job Title Code
			inputs.put("jobTitleCode", StringUtils.trim(staffingUpdate.getStaffingDtlTo().getJobTtlCd()));

			// Fill Date
			inputs.put("requiredPositionFillDate", Util.convertDateTO(staffingUpdate.getStaffingDtlTo().getFillDt()));

			// Number of positions
			inputs.put("authorizationPositionCount", Short.parseShort(staffingUpdate.getStaffingDtlTo().getNumPositions()));
			inputs.put("openPositionCount", Short.parseShort(staffingUpdate.getStaffingDtlTo().getNumPositions()));

			// Full Time Position
			inputs.put("fullTimeRequiredFlag", Boolean.parseBoolean(staffingUpdate.getStaffingDtlTo().getJobStatusFt()));
			// Part Time Position
			inputs.put("peakTimeRequiredFlag", Boolean.parseBoolean(staffingUpdate.getStaffingDtlTo().getJobStatusPt()));

			// PM Required, set to False
			inputs.put("pmRequiredFlag", false);

			// Weekend required, set to false
			inputs.put("weekendRequiredFlag", false);

			// Active Flag, set to True
			inputs.put("activeFlag", true);
			
			//Candidate Type: Internal = 1, External = 2, Both = 3
			String candidateTypeString = staffingUpdate.getStaffingDtlTo().getCandidateType();
			if (candidateTypeString.contains("Internal")) {
				if (candidateTypeString.contains("External")) {
					//Must be Both
					logger.debug("Candidate Type is Internal and External");
					inputs.put("candidateRequisitionTypeCode", (short) 3);
				} else {
					//Must be Internal Only	
					logger.debug("Candidate Type is Internal");					
					inputs.put("candidateRequisitionTypeCode", (short) 1);
				}
			} else {
				//Must be External Only
				logger.debug("Candidate Type is External");
				inputs.put("candidateRequisitionTypeCode", (short) 2);
			}
			
			//Auto-Attach Requisition
			inputs.put("autoAttachRequisitionFlag", staffingUpdate.getStaffingDtlTo().isAutoAttachReqn());
		}
		return inputs;
	}

	
	/**
	 * This method is used to update the HR_EMPLT_REQN_LANG table with setting
	 * values in Mapstream.
	 * 
	 * @param staffingUpdate
	 * @return update success/failure details
	 * @throws RetailStaffingException
	 */
	public MapStream insertLangauageDetails(MapStream inputs, int requisitionNbr, short languageCd) throws QueryException {

		inputs.put("employmentRequisitionNumber", requisitionNbr);
		inputs.put("jobSkillCode", languageCd);			

		return inputs;
	}
	
	/**
	 * This method is used to update the hiring event information.
	 * 
	 * @param staffingUpdate
	 * @return update success/failure details
	 * @throws QueryException
	 */
	public MapStream updateHireEventInfo(MapStream inputs, final UpdateStaffingRequest staffingUpdate, final String insertUpdate)
			throws QueryException {
		logger.info(this + "Enters updateStaffingDetails method in Manager with input as" + staffingUpdate);
		// null check for staffingUpdate
		if (staffingUpdate != null && staffingUpdate.getStaffingDtlTo() != null) {
			// setting the primary key in query
			if (staffingUpdate.getStaffingDtlTo().getHiringEventID() != null && insertUpdate.equals(UPDATE)) {
				inputs.put("hireEventId", Integer.parseInt(staffingUpdate.getStaffingDtlTo().getHiringEventID().trim()));
			} else if (insertUpdate.equals(UPDATE)) {
				inputs.put("hireEventId", 0);
			}
			// hiringevent begin date
			if (staffingUpdate.getStaffingDtlTo().getStfHrgEvntStartDt() != null) {
				inputs.putAllowNull("hireEventBeginDate", Util.convertDateTO(staffingUpdate.getStaffingDtlTo().getStfHrgEvntStartDt()));
			} else {
				inputs.putAllowNull("hireEventBeginDate", null);
			}
			// null check for hiring event end date
			if (staffingUpdate.getStaffingDtlTo().getStfHrgEvntEndDt() != null) {
				inputs.putAllowNull("hireEventEndDate", Util.convertDateTO(staffingUpdate.getStaffingDtlTo().getStfHrgEvntEndDt()));
			} else {
				inputs.putAllowNull("hireEventEndDate", null);
			}
			// null check for hireEventLocationDescription
			if (staffingUpdate.getStaffingDtlTo().getStfhrgEvntLoc() != null
					&& !staffingUpdate.getStaffingDtlTo().getStfhrgEvntLoc().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventLocationDescription", staffingUpdate.getStaffingDtlTo().getStfhrgEvntLoc().trim()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("hireEventLocationDescription", null); // can
				// be
				// null
			}

			// null check for phone number
			if (staffingUpdate.getStaffingDtlTo().getStfhrgEvntLocPhn() != null
					&& !staffingUpdate.getStaffingDtlTo().getStfhrgEvntLocPhn().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventPhoneNumber", staffingUpdate.getStaffingDtlTo().getStfhrgEvntLocPhn().trim());
				// can be null
			} else {
				inputs.putAllowNull("hireEventPhoneNumber", null); // can be
				// null
			}
			// null check for address
			if (staffingUpdate.getStaffingDtlTo().getAdd() != null && !staffingUpdate.getStaffingDtlTo().getAdd().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventAddressText", staffingUpdate.getStaffingDtlTo().getAdd().trim()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("hireEventAddressText", null); // can be
				// null
			}
			// city name
			if (staffingUpdate.getStaffingDtlTo().getCity() != null && !staffingUpdate.getStaffingDtlTo().getCity().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventCityName", staffingUpdate.getStaffingDtlTo().getCity().trim()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("hireEventCityName", null); // can be
				// null
			}
			// zip code
			if (staffingUpdate.getStaffingDtlTo().getZip() != null && !staffingUpdate.getStaffingDtlTo().getZip().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventZipCodeCode", staffingUpdate.getStaffingDtlTo().getZip()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("hireEventZipCodeCode", null); // can be
				// null
			}
			// state code
			if (staffingUpdate.getStaffingDtlTo().getState() != null && !staffingUpdate.getStaffingDtlTo().getState().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventStateCode", staffingUpdate.getStaffingDtlTo().getState().trim()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("hireEventStateCode", null); // can be
				// null
			}
			// hireEventBeginTime time
			if (staffingUpdate.getStaffingDtlTo().getStartTime().getHour() != null
					&& !staffingUpdate.getStaffingDtlTo().getStartTime().getHour().toString().trim().equals(EMPTY_STRING)) {

				inputs.putAllowNull("hireEventBeginTime", Util.convertTimestampTOtoTime(staffingUpdate.getStaffingDtlTo().getStartTime()));
			} else {
				inputs.putAllowNull("hireEventBeginTime", null);
			}
			// hire event end time
			if (staffingUpdate.getStaffingDtlTo().getEndTime().getHour() != null
					&& !staffingUpdate.getStaffingDtlTo().getEndTime().getHour().toString().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventEndTime", Util.convertTimestampTOtoTime(staffingUpdate.getStaffingDtlTo().getEndTime()));
				// can be null
			} else {
				inputs.putAllowNull("hireEventEndTime", null);
			}
			// null check for break
			if (staffingUpdate.getStaffingDtlTo().getBreaks() != null && !staffingUpdate.getStaffingDtlTo().getBreaks().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("hireEventBreakText", staffingUpdate.getStaffingDtlTo().getBreaks().trim());
				// can be null
			} else {
				inputs.putAllowNull("hireEventBreakText", null);
			}
			// null check for lunch begin time
			if (staffingUpdate.getStaffingDtlTo().getLunch().getHour() != null
					&& !staffingUpdate.getStaffingDtlTo().getLunch().getHour().toString().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("lunchBeginTime", Util.convertTimestampTOtoTime(staffingUpdate.getStaffingDtlTo().getLunch()));
				// can be null
			} else {
				inputs.putAllowNull("lunchBeginTime", null); // can be null
			}
			// null check for interview duration
			if (staffingUpdate.getStaffingDtlTo().getInterviewDurtn() != null
					&& !staffingUpdate.getStaffingDtlTo().getInterviewDurtn().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("interviewDurValue", Short.parseShort(staffingUpdate.getStaffingDtlTo().getInterviewDurtn()));
				// can be null
			} else {
				inputs.putAllowNull("interviewDurValue", null);
			}
			// null check for time slot
			if (staffingUpdate.getStaffingDtlTo().getInterviewTmSlt() != null
					&& !staffingUpdate.getStaffingDtlTo().getInterviewTmSlt().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("interviewTimeSlotValue", Short.parseShort(staffingUpdate.getStaffingDtlTo().getInterviewTmSlt()));
				// can be null
			} else {
				inputs.putAllowNull("interviewTimeSlotValue", null); // can
				// be
				// null
			}
			// null check for break
			if (staffingUpdate.getStaffingDtlTo().getLastIntrTm().getHour() != null
					&& !staffingUpdate.getStaffingDtlTo().getLastIntrTm().getHour().toString().trim().equals(EMPTY_STRING)) {
				inputs.putAllowNull("lastInterviewTime", Util.convertTimestampTOtoTime(staffingUpdate.getStaffingDtlTo().getLastIntrTm())); // can
				// be
				// null
			} else {
				inputs.putAllowNull("lastInterviewTime", null);
			}
		} else {
			throw new QueryException("Application Fatal Error ");
		}

		return inputs;
	}

	/**
	 * This method is used to update the hiring event requisition information.
	 * 
	 * @param staffingUpdate
	 * @return update success/failure details
	 * @throws QueryException
	 */
	public MapStream updateHireEventReqnInfo(MapStream inputs, final UpdateStaffingRequest staffingUpdate, final String insertUpdate)
			throws QueryException {
		logger.info(this + "Enters updateStaffingDetails method in Manager with input as" + staffingUpdate);
		// null check for staffingUpdate
		if (staffingUpdate != null && staffingUpdate.getStaffingDtlTo() != null) {
			inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));
			inputs.put("setHireEventId", Integer.parseInt(staffingUpdate.getStaffingDtlTo().getHiringEventID()));
		} else {
			throw new QueryException("Application Fatal Error ");
		}

		return inputs;
	}
	
	/**
	 * This method is used to insert the hiring event requisition information.
	 * 
	 * @param staffingUpdate
	 * @return update success/failure details
	 * @throws QueryException
	 */
	public MapStream insertHireEventReqnInfo(MapStream inputs, final UpdateStaffingRequest staffingUpdate, final String insertUpdate)
			throws QueryException {
		logger.info(this + "Enters updateStaffingDetails method in Manager with input as" + staffingUpdate);
		// null check for staffingUpdate
		if (staffingUpdate != null && staffingUpdate.getStaffingDtlTo() != null) {
			inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));
			inputs.put("setHireEventId", Integer.parseInt(staffingUpdate.getStaffingDtlTo().getHiringEventID()));
		} else {
			throw new QueryException("Application Fatal Error ");
		}

		return inputs;
	}
	
	/**
	 * This method is used to delete the hiring event requisition information.
	 * 
	 * @param staffingUpdate
	 * @return update success/failure details
	 * @throws QueryException
	 */
	public MapStream deleteHireEventReqnInfo(MapStream inputs, final UpdateStaffingRequest staffingUpdate, final String insertUpdate)
			throws QueryException {
		logger.info("Enters updateStaffingDetails method in Manager with input as reqNbr:" + staffingUpdate.getReqNbr());
		// null check for staffingUpdate
		if (staffingUpdate != null && staffingUpdate.getStaffingDtlTo() != null) {
			inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));
		} else {
			throw new QueryException("Application Fatal Error ");
		}

		return inputs;
	}
	
	/**
	 * This method is used to update the employment requisition note.
	 * 
	 * @param staffingUpdate
	 * @param noteTypCode
	 * @return update success/failure details
	 * @throws QueryException
	 */
	public MapStream updateEmploymentRequisitionNote(MapStream inputs, final UpdateStaffingRequest staffingUpdate, final short noteTypCode)
			throws QueryException {
		logger.info(this + "Enters updateStaffingDetails method in Manager with input as" + staffingUpdate);
		// final short note= (short)noteTypCode;
		try {
			inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));
			inputs.put("humanResourcesNoteTypeCode", noteTypCode);
			if (noteTypCode == QUALIFIED_POOL_NOTE_TYP_CD) {
				if (staffingUpdate.getStaffingDtlTo().getQualPoolNts() != null) {
					inputs.putAllowNull("requisitionNoteText", staffingUpdate.getStaffingDtlTo().getQualPoolNts().trim()); // can
					// be
					// null
				} else {
					inputs.putAllowNull("requisitionNoteText", "");
				}

			} else if (noteTypCode == REFERRALS_NOTE_TYP_CD) {
				if (staffingUpdate.getStaffingDtlTo().getReferals() != null) {
					inputs.putAllowNull("requisitionNoteText", staffingUpdate.getStaffingDtlTo().getReferals().trim()); // can
					// be
					// null
				} else {
					inputs.putAllowNull("requisitionNoteText", "");
				}

			}
		}

		catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
		return inputs;
	}

	/**
	 * This method is used to read the note type texts.
	 * 
	 * @param reqNbr
	 * @param noteTypeCd
	 * @return staffing details
	 * @throws QueryException
	 */
	public StaffingDetailsTO readEmploymentRequisitionNote(int reqNbr, final short noteTypeCd) throws QueryException {
		final StaffingDetailsTO readEmploymentRequisitionNote = new StaffingDetailsTO();

		MapStream inputs = new MapStream("readEmploymentRequisitionNote");
		inputs.put("employmentRequisitionNumber", reqNbr);
		inputs.put("humanResourcesNoteTypeCode", noteTypeCd);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					if (results.getString("requisitionNoteText") != null) {
						if (noteTypeCd == QUALIFIED_POOL_NOTE_TYP_CD) {
							readEmploymentRequisitionNote.setQualPoolNts(StringUtils.trim(results.getString("requisitionNoteText")));

						} else if (noteTypeCd == REFERRALS_NOTE_TYP_CD) {
							readEmploymentRequisitionNote.setReferals(StringUtils.trim(results.getString("requisitionNoteText")));

						}

					}

				}
			}
		});
		return readEmploymentRequisitionNote;
	}

	/**
	 * This method is used to insert the employment requisition note
	 * 
	 * @param staffingUpdate
	 * @param noteTypCode
	 * @return insertion success/failure details
	 * @throws QueryException
	 */
	public MapStream createEmploymentRequisitionNote(MapStream inputs, final UpdateStaffingRequest staffingUpdate, final short noteTypCode)
			throws QueryException {
		logger.info(this + "Enters updateStaffingDetails method in Manager with input as" + staffingUpdate);

		inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));
		inputs.put("humanResourcesNoteTypeCode", noteTypCode);
		if (noteTypCode == QUALIFIED_POOL_NOTE_TYP_CD) {
			if (staffingUpdate.getStaffingDtlTo().getQualPoolNts() != null) {
				inputs.putAllowNull("requisitionNoteText", staffingUpdate.getStaffingDtlTo().getQualPoolNts().trim());
			} else {
				inputs.putAllowNull("requisitionNoteText", "");
			}

		} else if (noteTypCode == REFERRALS_NOTE_TYP_CD) {
			if (staffingUpdate.getStaffingDtlTo().getReferals() != null) {
				inputs.putAllowNull("requisitionNoteText", staffingUpdate.getStaffingDtlTo().getReferals().trim()); // can
				// be
				// null
			} else {
				inputs.putAllowNull("requisitionNoteText", "");
			}
		}
		return inputs;
	}

	/**
	 * This method is used to set the map sttream value for
	 * 
	 * @param inputs
	 * @param updReq
	 * @param eventId
	 * @return insertion success/failure details
	 * @throws QueryException
	 */
	public MapStream createHumanResourcesHireEventRequisition(MapStream inputs, UpdateStaffingRequest updReq, int eventId) throws QueryException {
		logger.info(this + "Enters createHumanResourcesHireEventRequisition method in Manager with " + "input as" + eventId);
		try {
			inputs.put("hireEventId", eventId);
			inputs.put("employmentRequisitionNumber", Integer.parseInt(updReq.getReqNbr()));
		} catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
		return inputs;
	}

	/**
	 * This method is used to insert a new Hire Event and return the generated
	 * eventId
	 * 
	 * @param inputs
	 * @param updReq
	 * @return eventId
	 * @throws QueryException
	 */
	public int createHumanResourcesHireEvent(DAOConnection daoConn, Query query, MapStream inputs, UpdateStaffingRequest updReq)
			throws QueryException {

		final StringBuffer genratedHireEventIdString = new StringBuffer();
		logger.info(this + "Enters createHumanResourcesHireEvent method in Manager");
		try {
			inputs.putAllowNull("hireEventBeginDate", null);
			inputs.putAllowNull("hireEventEndDate", null);
			inputs.putAllowNull("hireEventLocationDescription", null);
			inputs.putAllowNull("hireEventPhoneNumber", null);
			inputs.putAllowNull("hireEventAddressText", null);
			inputs.putAllowNull("hireEventCityName", null);
			inputs.putAllowNull("hireEventZipCodeCode", null);
			inputs.putAllowNull("hireEventStateCode", null);
			inputs.putAllowNull("hireEventBeginTime", null);
			inputs.putAllowNull("hireEventEndTime", null);
			inputs.putAllowNull("hireEventBreakText", null);
			inputs.putAllowNull("lunchBeginTime", null);
			inputs.putAllowNull("interviewDurValue", null);
			inputs.putAllowNull("interviewTimeSlotValue", null);
			inputs.putAllowNull("lastInterviewTime", null);
			inputs.putAllowNull("hireEventTypeIndicator", null); 		
			inputs.putAllowNull("emgrHumanResourcesAssociateId", null);
			inputs.addQualifier("returnGeneratedKey", true);

			query.insertObject(daoConn, new BasicInsertNotifier(new InsertNotifier() {
				MapStream primaryKeysStream;

				public void notifyInsert(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException {
					if (successFlag && count > 0) {
						primaryKeysStream = (MapStream) arg0;
						genratedHireEventIdString.append(primaryKeysStream.getMap("primaryKeys").get("hireEventId"));
					}
				}
			}), inputs);
			logger.info("After insert");
			logger.info("The generated id is" + genratedHireEventIdString.toString());

		} catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
		return Integer.parseInt(genratedHireEventIdString.toString());
	}

	/**
	 * This method is used to get the phone screen details for a Requisition
	 * 
	 * @param reqNbr
	 * @return list of phone screen interview details
	 * @throws QueryException
	 */
	public List<PhoneScreenIntrwDetailsTO> readRequisitionPhoneScreen(int reqNbr) throws QueryException {
		final List<PhoneScreenIntrwDetailsTO> readRequisitionPhoneScreenList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		try {
			MapStream inputs = new MapStream("readRequisitionPhoneScreen");
			inputs.put("employmentRequisitionNumber", reqNbr);
			// need to change from boolean to particular flag
			// -active,inactive,hold
			List<Object> inputActiveFlagList1 = new ArrayList<Object>();
			inputActiveFlagList1.add(PHN_ACTV);
			inputs.put("activeFlagList", inputActiveFlagList1);
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					PhoneScreenIntrwDetailsTO readRequisitionPhoneScreenDTO = null;
					IntrwLocationDetailsTO intrDtls = null;
					Status phoneScrnStatusObj = null;
					Status interviewStatusObj = null;
					
					while (results.next()) {
						readRequisitionPhoneScreenDTO = new PhoneScreenIntrwDetailsTO();
						intrDtls = new IntrwLocationDetailsTO();
						readRequisitionPhoneScreenDTO.setItiNbr(String.valueOf(results.getInt("humanResourcesPhoneScreenId")));
						// readRequisitionPhoneScreenDTO.setLastUpdateTimestamp(results
						// .getDate("lastUpdateTimestamp"));
						readRequisitionPhoneScreenDTO.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						readRequisitionPhoneScreenDTO.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						readRequisitionPhoneScreenDTO.setName(StringUtils.trim(results.getString("candidateName")));
						readRequisitionPhoneScreenDTO.setPhoneScreenStatusCode(results.getString("phoneScreenStatusCode"));
						readRequisitionPhoneScreenDTO.setInterviewStatusCode(results.getString("interviewStatusCode"));
						readRequisitionPhoneScreenDTO.setInterviewMaterialStatusCode(results.getString("interviewMaterialStatusCode"));

						if (results.wasNull("minimumRequirementStatusCode")) {
							readRequisitionPhoneScreenDTO.setYnstatus(null);
						} else {
							readRequisitionPhoneScreenDTO.setYnstatus(String.valueOf(results.getShort("minimumRequirementStatusCode")));
						}
						
						//Not sure why this was needed, YnStatus has nothing to do with Interview Status Desc.
						//Removed on 5/18/2011 by MTS1876 when implementing the Status Code Cache code
						/*if (readRequisitionPhoneScreenDTO.getYnstatus() != null) {
							// calling DAO to get the status desc
							statusTO = new InterviewStatusTO();
							phnScreenDAO = new PhoneScreenDAO();
							statusTO = phnScreenDAO.readNlsInterviewRespondStatus(Short.parseShort(readRequisitionPhoneScreenDTO.getYnstatus()));
							if (statusTO != null && statusTO.getStatusDesc() != null) {
								readRequisitionPhoneScreenDTO.setYnStatusDesc(statusTO.getStatusDesc().trim());
							}
						}*/

						// Gets Phone Screen Status Description
						if (readRequisitionPhoneScreenDTO.getPhoneScreenStatusCode() != null && !readRequisitionPhoneScreenDTO.getPhoneScreenStatusCode().trim().equals(EMPTY_STRING)) {
							phoneScrnStatusObj = StatusManager.getStatusObject(StatusType.PHONE_SCREEN_STATUS, Short.parseShort(readRequisitionPhoneScreenDTO
									.getPhoneScreenStatusCode()), "EN_US");
							readRequisitionPhoneScreenDTO.setPhoneScreenStatusDesc(phoneScrnStatusObj.getStatDesc());
						}

						// Gets Interview Status Description
						if (readRequisitionPhoneScreenDTO.getInterviewStatusCode() != null && !readRequisitionPhoneScreenDTO.getInterviewStatusCode().trim().equals(EMPTY_STRING) 
								&& !readRequisitionPhoneScreenDTO.getInterviewStatusCode().trim().equals("0")) {
							interviewStatusObj = StatusManager.getStatusObject(StatusType.INTERVIEW_STATUS, Short.parseShort(readRequisitionPhoneScreenDTO.getInterviewStatusCode()), "EN_US"); 
							readRequisitionPhoneScreenDTO.setInterviewStatusDesc(interviewStatusObj.getStatDesc().trim());
						}							

						//Added for CDP, remove leading zeros for display purposes.
						readRequisitionPhoneScreenDTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));						
						
						readRequisitionPhoneScreenDTO.setIntrLocDtls(intrDtls);
						readRequisitionPhoneScreenDTO.getIntrLocDtls().setInterviewDate(Util.converDatetoDateTO(results.getDate("interviewDate")));
						readRequisitionPhoneScreenList.add(readRequisitionPhoneScreenDTO);
					}
				}
			});
		}

		catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
		return readRequisitionPhoneScreenList;
	}

	/**
	 * This method is used to get the phone screen details for a Requisition
	 * 
	 * @param reqNbr
	 * @return list of phone screen interview details
	 * @throws QueryException
	 */
	public List<PhoneScreenIntrwDetailsTO> readRequisitionPhoneScreenInactive(int reqNbr) throws QueryException {
		final List<PhoneScreenIntrwDetailsTO> readRequisitionPhoneScreenList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		try {
			MapStream inputs = new MapStream("readRequisitionPhoneScreen");
			inputs.put("employmentRequisitionNumber", reqNbr);
			// need to change from boolean to particular flag
			// -active,inactive,hold
			List<Object> inputActiveFlagList1 = new ArrayList<Object>();
			inputActiveFlagList1.add(PHN_INACTV);
			inputActiveFlagList1.add(PHN_HOLD);
			inputs.put("activeFlagList", inputActiveFlagList1);
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					PhoneScreenIntrwDetailsTO readRequisitionPhoneScreenDTO = null;
					IntrwLocationDetailsTO intrDtls = null;
					Status phoneScrnStatusObj = null;
					Status interviewStatusObj = null;

					while (results.next()) {
						readRequisitionPhoneScreenDTO = new PhoneScreenIntrwDetailsTO();
						intrDtls = new IntrwLocationDetailsTO();
						readRequisitionPhoneScreenDTO.setItiNbr(String.valueOf(results.getInt("humanResourcesPhoneScreenId")));
						// readRequisitionPhoneScreenDTO.setLastUpdateTimestamp(results
						// .getDate("lastUpdateTimestamp"));
						readRequisitionPhoneScreenDTO.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						readRequisitionPhoneScreenDTO.setAid(StringUtils.trim(results.getString("candidatePersonId")));
						readRequisitionPhoneScreenDTO.setName(StringUtils.trim(results.getString("candidateName")));
						readRequisitionPhoneScreenDTO.setPhoneScreenStatusCode(String.valueOf(results.getInt("phoneScreenStatusCode")));
						readRequisitionPhoneScreenDTO.setInterviewStatusCode(String.valueOf(results.getInt("interviewStatusCode")));
						readRequisitionPhoneScreenDTO.setInterviewMaterialStatusCode(String.valueOf(results.getInt("interviewMaterialStatusCode")));

						if (results.wasNull("minimumRequirementStatusCode")) {
							readRequisitionPhoneScreenDTO.setYnstatus(null);
						} else {
							readRequisitionPhoneScreenDTO.setYnstatus(String.valueOf(results.getShort("minimumRequirementStatusCode")));
						}
						
						//Not sure why this was needed, YnStatus has nothing to do with Interview Status Desc.
						//Removed on 5/18/2011 by MTS1876 when implementing the Status Code Cache code
						/*if (readRequisitionPhoneScreenDTO.getYnstatus() != null) {
							// calling DAO to get the status desc
							statusTO = new InterviewStatusTO();
							phnScreenDAO = new PhoneScreenDAO();
							statusTO = phnScreenDAO.readNlsInterviewRespondStatus(Short.parseShort(readRequisitionPhoneScreenDTO.getYnstatus()));
							if (statusTO != null && statusTO.getStatusDesc() != null) {
								readRequisitionPhoneScreenDTO.setYnStatusDesc(statusTO.getStatusDesc().trim());
							}
						}*/

						// Gets Phone Screen Status Description
						if (readRequisitionPhoneScreenDTO.getPhoneScreenStatusCode() != null && !readRequisitionPhoneScreenDTO.getPhoneScreenStatusCode().trim().equals(EMPTY_STRING)) {
							phoneScrnStatusObj = StatusManager.getStatusObject(StatusType.PHONE_SCREEN_STATUS, Short.parseShort(readRequisitionPhoneScreenDTO
									.getPhoneScreenStatusCode()), "EN_US");
							readRequisitionPhoneScreenDTO.setPhoneScreenStatusDesc(phoneScrnStatusObj.getStatDesc());
						}

						// Gets Interview Status Description
						if (readRequisitionPhoneScreenDTO.getInterviewStatusCode() != null && !readRequisitionPhoneScreenDTO.getInterviewStatusCode().trim().equals(EMPTY_STRING) 
								&& !readRequisitionPhoneScreenDTO.getInterviewStatusCode().trim().equals("0")) {
							interviewStatusObj = StatusManager.getStatusObject(StatusType.INTERVIEW_STATUS, Short.parseShort(readRequisitionPhoneScreenDTO.getInterviewStatusCode()), "EN_US"); 
							readRequisitionPhoneScreenDTO.setInterviewStatusDesc(interviewStatusObj.getStatDesc().trim());
						}					
						
						readRequisitionPhoneScreenDTO.setIntrLocDtls(intrDtls);
						readRequisitionPhoneScreenDTO.getIntrLocDtls().setInterviewDate(Util.converDatetoDateTO(results.getDate("interviewDate")));
						
						//Added for CDP, remove leading zeros for display purposes.
						readRequisitionPhoneScreenDTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));

						readRequisitionPhoneScreenList.add(readRequisitionPhoneScreenDTO);
					}
				}
			});
		}

		catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
		return readRequisitionPhoneScreenList;
	}

	/**
	 * This method is used to get the candidate details for a Requisition.
	 * 
	 * @param reqNbr
	 * @param strNbr
	 * @return list of candidate details
	 * @throws QueryException
	 */
	public List<CandidateDetailsTO> readRequisitionCandidate(int reqNbr, String strNbr) throws QueryException {
		final List<CandidateDetailsTO> readRequisitionCandidateList = new ArrayList<CandidateDetailsTO>();
		try {
			MapStream inputs = new MapStream("readRequisitionCandidate");
			inputs.put("humanResourcesSystemStoreNumber", strNbr);
			inputs.put("employmentRequisitionNumber", reqNbr);
			inputs.put(ACTV_FLG, true);
			inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put("suffixActiveFlag", true);
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThan", true);
			inputs.put("humanResourcesSystemCountryCode", "USA");
			inputs.put("effectiveBeginDateOne", new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put("effectiveEndDateOne", new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.addQualifier("effectiveBeginDateOneLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateOneGreaterThanEqualTo", true);			
			
			
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					CandidateDetailsTO readRequisitionCandidateDTO = null;
					// StringBuffer name = new StringBuffer();
					while (results.next()) {
						readRequisitionCandidateDTO = new CandidateDetailsTO();
						readRequisitionCandidateDTO.setSsnNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
						readRequisitionCandidateDTO.setAid(StringUtils.trim(results.getString("zEmplid")));
						if (readRequisitionCandidateDTO.getAid() != null && !readRequisitionCandidateDTO.getAid().trim().equals(EMPTY_STRING)) {
							readRequisitionCandidateDTO.setInternalExternal(INTERNAL_FLAG);
						} else {
							readRequisitionCandidateDTO.setInternalExternal(EXTERNAL_FLAG);
						}
						readRequisitionCandidateDTO.setName(Util.getCandidateName(results.getString("lastName"), results.getString("firstName"),
								results.getString("middleInitialName"), results.getString("suffixName")));
						readRequisitionCandidateDTO.setIntStatus(StringUtils.trim(results.getString("interviewStatusIndicator")));
						readRequisitionCandidateDTO.setDrugTest(StringUtils.trim(results.getString("drugTestResultCode")));
						readRequisitionCandidateDTO.setOfferDate(Util.converDatetoDateTO(results.getDate("candidateOfferMadeDate")));
						readRequisitionCandidateDTO.setOfferMade(StringUtils.trim(results.getString("candidateOfferMadeFlag")));
						readRequisitionCandidateDTO.setOfferStat(StringUtils.trim(results.getString("candidateOfferStatusIndicator")));
						readRequisitionCandidateDTO.setOrganization(StringUtils.trim(results.getString("organization1")));
						readRequisitionCandidateDTO.setApplicantType(StringUtils.trim(results.getString("applicantType")));
						readRequisitionCandidateDTO.setPhnScrnId(StringUtils.trim(results.getString("humanResourcesPhoneScreenId")));
						if (results.getBoolean("activeFlag")) {
							readRequisitionCandidateDTO.setCanStatus(TRUE);
						} else {
							readRequisitionCandidateDTO.setCanStatus(FALSE);
						}
						int declineCode = results.getInt("offerDeclineReasonCode");
						switch (declineCode) {
						case 0:
							readRequisitionCandidateDTO.setDecCD(" ");
							break;
						case 1:
							readRequisitionCandidateDTO.setDecCD("PAY");
							break;
						case 2:
							readRequisitionCandidateDTO.setDecCD("BENEFITS");
							break;
						case 3:
							readRequisitionCandidateDTO.setDecCD("HOURS");
							break;
						case 4:
							readRequisitionCandidateDTO.setDecCD("ACCEPTED ANOTHER OFFER");
							break;
						case 5:
							readRequisitionCandidateDTO.setDecCD("OTHER");
							break;
						case 6:
							readRequisitionCandidateDTO.setDecCD("LOCATION");
							break;
						case 7:
							readRequisitionCandidateDTO.setDecCD("REFUSED DRUG TEST");
							break;
						}

						//Added for CDP, remove leading zeros for display purposes.
						readRequisitionCandidateDTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						//Added for FMS 7894 January 2016 CR's
						if (readRequisitionCandidateDTO.getApplicantType().equals("EXT")) {
							if (!results.wasNull("effectiveBeginDate")) {
								logger.debug("*******" + results.getDate("effectiveBeginDate"));
								readRequisitionCandidateDTO.setDoNotConsiderFor60Days("Y");
							} else {
								readRequisitionCandidateDTO.setDoNotConsiderFor60Days("N");
							}
						} else {
							readRequisitionCandidateDTO.setDoNotConsiderFor60Days("N");
						}
						
						readRequisitionCandidateDTO.setSubmitInterviewResultsDT(Util.converDatetoDateTO(new Date(Calendar.getInstance().getTimeInMillis())));

						readRequisitionCandidateList.add(readRequisitionCandidateDTO);

					}
				}
			});
		} catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
		return readRequisitionCandidateList;
	}

	/**
	 * This method is used to get get the states List
	 * 
	 * @return list of staffing details
	 * @throws QueryException
	 */
	public List<StateDetailsTO> readStateList() throws QueryException {
		final List<StateDetailsTO> readStateListList = new ArrayList<StateDetailsTO>();
		try {
			MapStream inputs = new MapStream("readStateList");
			BasicDAO.getResult("ApplicationArchitecture", 1, inputs, new ResultsReader() {
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					StateDetailsTO readStateListDTO = null;
					while (results.next()) {
						readStateListDTO = new StateDetailsTO();
						readStateListDTO.setStateCode(StringUtils.trim(results.getString("stateCode")));
						readStateListDTO.setStateName(StringUtils.trim(results.getString("stateName")));
						readStateListDTO.setStateNbr(String.valueOf(results.getShort("stateNumber")));
						readStateListList.add(readStateListDTO);
					}
				}
			});
		} catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
		return readStateListList;
	}

	/**
	 * This method is used to get the List of Job Titles that are Auto-Attach
	 * 
	 * @return list of job titles that are Auto-Attach
	 * @throws QueryException
	 */
	public List<AutoAttachJobTitlesTO> readAutoAttachJobTitles() throws QueryException {
		final List<AutoAttachJobTitlesTO> aaJobTitleList = new ArrayList<AutoAttachJobTitlesTO>();
		
		MapStream inputs = new MapStream("readTrprx000ByInputList");
		inputs.put("tabNumber", "10069");
		inputs.put("OE42", "Y");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME,WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				AutoAttachJobTitlesTO aaJobTitle = null;
				while (results.next()) {
					aaJobTitle = new AutoAttachJobTitlesTO();
					aaJobTitle.setAaJobTitle(StringUtils.trim(results.getString("tblKeys").substring(0, 6)));
					aaJobTitle.setAaDeptCd(StringUtils.trim(results.getString("tblKeys").substring(6, 10)));
					aaJobTitleList.add(aaJobTitle);
				}
			}
		});
		
		return aaJobTitleList;
	}
	
	/**
	 * This method is used to return the requisitions by store
	 * 
	 * @return requisition details based on store
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getRequisitionsByStore(final SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getRequisitionsByStore method in DAO ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemStoreNumber(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readActiveRequisitionsByStore");
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

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();

					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
						if (!results.wasNull("requiredPositionFillDate")) {
							logger.info("In Require Date Area....");
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
						reqDet.setStore(summReq.getOrgID());
						reqDet.setHumanResourcesSystemRegionCode(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						reqDet.setHumanResourcesSystemOperationsGroupCode(StringUtils.trim(results
								.getString("humanResourcesSystemOperationsGroupCode")));
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
						if (!results.wasNull("retailStaffingCenterRequisitionManagedIndicator")) {
							reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						} else {
							//Old AIMS Requisition
							reqDet.setRscToManageFlg("N");
						}
						
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
			throw new RetailStaffingException(FETCHING_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "storeid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getRequisitionsByStore method in Manager with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * This method is used to return the requisitions by region
	 * 
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getRequisitionsByReg(final SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByReg method in DAO ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemOperationsGroupCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readActiveRequisitionsByRegion");
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

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();

					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
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
						reqDet.setHumanResourcesSystemOperationsGroupCode(summReq.getOrgID());
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
						reqDet.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						
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
			throw new RetailStaffingException(FETCHING_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "regionid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getRequisitionsByReg method in Manager with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * This method is used to return the requisitions by district
	 * 
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getRequisitionsByDist(final SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByDist method in DAO ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemRegionCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readActiveRequisitionsByDistrict");
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

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					List<Object> paginationTokenList = new ArrayList<Object>();

					while (results.next()) {
						// Update the pagination token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						if (!results.wasNull("oe31")) {
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
						reqDet.setHumanResourcesSystemRegionCode(summReq.getOrgID());
						reqDet.setHumanResourcesSystemOperationsGroupCode(StringUtils.trim(results
								.getString("humanResourcesSystemOperationsGroupCode")));
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
						reqDet.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
						reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						
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
			throw new RetailStaffingException(FETCHING_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "districtid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getRequisitionsByDist method in Manager with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * This method is used to return the requisitions by division
	 * 
	 * @return requisition details based on division
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getRequisitionsByDiv(final SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByDiv method in DAO ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemDivisionCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);
		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readActiveRequisitionsByDivision");
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
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						
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
			throw new RetailStaffingException(FETCHING_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "divisionid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getRequisitionsByDiv method in Manager with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * This class represents the value object which will be populated for
	 * passing the parameters to the DAO layer.
	 * 
	 * @author TCS
	 * 
	 */
	public static class ReadActiveRequisitionsByStoreTO {
		private String humanResourcesSystemStoreNumber;
		private String humanResourcesSystemRegionCode;
		private String humanResourcesSystemDivisionCode;
		private String humanResourcesSystemOperationsGroupCode;
		private short requisitionStatusCode;
		private int authorizationPositionCount;
		private List<Short> phoneScreenStatusCodeList;
		private List<Short> interviewStatusCodeList;
		private List<Short> interviewMaterialStatusCodeList;

		public int getAuthorizationPositionCount() {
			return authorizationPositionCount;
		}

		public void setAuthorizationPositionCount(int authorizationPositionCount) {
			this.authorizationPositionCount = authorizationPositionCount;
		}

		/**
		 * @return the requisitionStatusCode
		 */
		public short getRequisitionStatusCode() {
			return requisitionStatusCode;
		}

		/**
		 * @param requisitionStatusCode
		 *            the requisitionStatusCode to set
		 */
		public void setRequisitionStatusCode(short requisitionStatusCode) {
			this.requisitionStatusCode = requisitionStatusCode;
		}

		private List<Short> overallRespondStatusCodeList;

		/**
		 * @return the overallRespondStatusCodeList
		 */
		public List<Short> getOverallRespondStatusCodeList() {
			return overallRespondStatusCodeList;
		}

		private String tabno;

		public String getHumanResourcesSystemStoreNumber() {
			return humanResourcesSystemStoreNumber;
		}

		public void setHumanResourcesSystemStoreNumber(String humanResourcesSystemStoreNumber) {
			this.humanResourcesSystemStoreNumber = humanResourcesSystemStoreNumber;
		}

		public String getTabno() {
			return tabno;
		}

		public void setTabno(String tabno) {
			this.tabno = tabno;
		}

		public String getHumanResourcesSystemRegionCode() {
			return humanResourcesSystemRegionCode;
		}

		public void setHumanResourcesSystemRegionCode(String humanResourcesSystemRegionCode) {
			this.humanResourcesSystemRegionCode = humanResourcesSystemRegionCode;
		}

		public String getHumanResourcesSystemDivisionCode() {
			return humanResourcesSystemDivisionCode;
		}

		public void setHumanResourcesSystemDivisionCode(String humanResourcesSystemDivisionCode) {
			this.humanResourcesSystemDivisionCode = humanResourcesSystemDivisionCode;
		}

		public String getHumanResourcesSystemOperationsGroupCode() {
			return humanResourcesSystemOperationsGroupCode;
		}

		public void setHumanResourcesSystemOperationsGroupCode(String humanResourcesSystemOperationsGroupCode) {
			this.humanResourcesSystemOperationsGroupCode = humanResourcesSystemOperationsGroupCode;
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

	}

	/**
	 * The method will be used to get the available experience level list from
	 * DB.
	 * 
	 * @return list of requisition details based on division
	 * @throws QueryException
	 */
	public List<ExperienceLevelTO> readNlsExperienceLevelList() throws QueryException {
		final List<ExperienceLevelTO> readNlsExperienceLevelListList = new ArrayList<ExperienceLevelTO>();

		MapStream inputs = new MapStream("readNlsExperienceLevelList");

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				ExperienceLevelTO readNlsExperienceLevelListDTO = null;
				while (results.next()) {
					readNlsExperienceLevelListDTO = new ExperienceLevelTO();
					readNlsExperienceLevelListDTO.setExpLevelCode(String.valueOf(results.getShort("experienceLevelCode")));
					readNlsExperienceLevelListDTO.setDsplyDesc(StringUtils.trim(results.getString("displayExperienceLevelCode")));
					readNlsExperienceLevelListDTO.setShortDesc(StringUtils.trim(results.getString("shortExperienceLevelDescription")));
					// readNlsExperienceLevelListDTO
					// .setExperienceLevelDescription(results
					// .getString("experienceLevelDescription"));
					readNlsExperienceLevelListList.add(readNlsExperienceLevelListDTO);
				}
			}
		});

		return readNlsExperienceLevelListList;
	}

	/**
	 * The method will be used to get the Requisition Schedule Preference list from
	 * DB.
	 * @param requisition number
	 * @return list of Requisition Schedule Preference 
	 * @throws QueryException
	 */
	public List<SchedulePreferenceTO> getReqnSchedulePref(int requisitionNbr) throws QueryException {
		final List<SchedulePreferenceTO> schPrefList = new ArrayList<SchedulePreferenceTO>();

		MapStream inputs = new MapStream("readHumanResourcesRequisitionDailyAvailableByInputList");
		inputs.put("employmentRequisitionNumber", requisitionNbr);
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				SchedulePreferenceTO schedulePreferenceTO = null;
				while (results.next()) {
					schedulePreferenceTO = new SchedulePreferenceTO();
					schedulePreferenceTO.setDaySegCd(String.valueOf(results.getShort("daySegmentCode")));
					schedulePreferenceTO.setWkDayNbr(String.valueOf(results.getShort("weekDayNumber")));
					schPrefList.add(schedulePreferenceTO);
				}
			}
		});

		return schPrefList;
	}	

	/**
	 * The method will be used to get the Requisition Language Preference list from
	 * DB.
	 * 
	 * @return list of Requisition Language Preference 
	 * @throws QueryException
	 */
	public List<LanguageSkillsTO> getReqnLanguagePref(int requisitionNbr) throws QueryException {
		final List<LanguageSkillsTO> reqnLangSklsList = new ArrayList<LanguageSkillsTO>();

		MapStream inputs = new MapStream("readHumanResourcesEmploymentRequisitionLanguageByInputList");
		inputs.put("employmentRequisitionNumber", requisitionNbr);

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					LanguageSkillsTO tempLangSkls = new LanguageSkillsTO();
					tempLangSkls.setLangCode(String.valueOf(results.getShort("jobSkillCode")));
					reqnLangSklsList.add(tempLangSkls);
				}
			}
		});

		return reqnLangSklsList;
	}
	
	/**
	 * The method will be used to get the available language skills list from
	 * DB.
	 * 
	 * @return list of language skills
	 * @throws QueryException
	 */
	public List<LanguageSkillsTO> readLanguageSkillsList() throws QueryException {
		final List<LanguageSkillsTO> readLanguageSkillsListList = new ArrayList<LanguageSkillsTO>();

		MapStream inputs = new MapStream("readHumanResourcesLanguageSkills");
		inputs.put("languageCode", "EN_US");
		inputs.put("jobSkillCategoryCode", Short.parseShort("3"));

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				LanguageSkillsTO readLanguageSkillsListDTO = null;

				while (results.next()) {
					// Don't include English
					if (results.getShort("jobSkillCode") != 1) {
						readLanguageSkillsListDTO = new LanguageSkillsTO();
						readLanguageSkillsListDTO.setLangCode(String.valueOf(results.getShort("jobSkillCode")));
						readLanguageSkillsListDTO.setDsplyDesc(StringUtils.trim(results.getString("displayJobSkillCode")));
						readLanguageSkillsListDTO.setShortDesc(StringUtils.trim(results.getString("shortJobSkillDescription")));
						readLanguageSkillsListList.add(readLanguageSkillsListDTO);
					}
				}
			}
		});

		return readLanguageSkillsListList;
	}

	
	/**
	 * The method will be used to get the Phone Screen Disposition Codes list from
	 * DB.
	 * 
	 * @return list of phone screen disposition codes
	 * @throws QueryException
	 */
	public List<PhoneScreenDispositionsTO> readPhoneScreenDispositionCodeList() throws QueryException {
		final List<PhoneScreenDispositionsTO> phoneScreenDispositionCodeList = new ArrayList<PhoneScreenDispositionsTO>();

		MapStream inputs = new MapStream("readNlsPhoneScreenDispositionByInputList");
		inputs.put("languageCode", "EN_US");

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

				while (results.next()) {
					PhoneScreenDispositionsTO phoneScreenDispositionsTO = new PhoneScreenDispositionsTO();
					phoneScreenDispositionsTO.setDispositionCode(results.getShort("phoneScreenDispositionCode"));
					phoneScreenDispositionsTO.setDispositionDesc(StringUtils.trim(results.getString("phoneScreenDispositionDescription")));
					phoneScreenDispositionCodeList.add(phoneScreenDispositionsTO);
				}
			}
		});

		return phoneScreenDispositionCodeList;
	}
	
	
	/**
	 * The method will be used to get the Phone/Interview Status call history list from
	 * DB.
	 * 
	 * @return list of Call History
	 * @throws QueryException
	 */
	public List<PhoneScreenCallHistoryTO> readCallHistoryList(int phoneScreenId) throws QueryException {
		final List<PhoneScreenCallHistoryTO> phoneScreenHistoryList = new ArrayList<PhoneScreenCallHistoryTO>();

		MapStream inputs = new MapStream("readCandidateContactStatusDetails");
		inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
		inputs.put(CRT_SYSUSR_ID, "CTISER01");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

				while (results.next()) {
					PhoneScreenCallHistoryTO phoneScreenCallHistoryTO = new PhoneScreenCallHistoryTO();
					phoneScreenCallHistoryTO.setCallType(results.getString("typeDescription"));
					if (results.getInt("ccntctStatusTypeCode") == 1) {
						phoneScreenCallHistoryTO.setCallDisposition(results.getString("phoneScreenDescription"));
					} else if (results.getInt("ccntctStatusTypeCode") == 2) {
						phoneScreenCallHistoryTO.setCallDisposition(results.getString("interviewDescription"));
					} 
					phoneScreenCallHistoryTO.setCallTs(Util.convertTimestampFormat(results.getTimestamp("createTimestamp")));	
					phoneScreenHistoryList.add(phoneScreenCallHistoryTO);
				}
			}
		});

		return phoneScreenHistoryList;
	}
	
/*	public static boolean isApplicantHaveTierResults(String applicantId, int categoryCode) throws QueryException {

		if (logger.isDebugEnabled()) {
			logger.debug("start isApplicantHaveTierResults");
		}
		
		final String datasource = "java:comp/env/jdbc/DB2Z.PR1.005";
		
		final StringBuilder sql = new StringBuilder(300);
		
		sql.append("SELECT TT.EMPTST_ID ");
		sql.append("FROM APLCNT_EMPTST_TIER TT ");
		sql.append("WHERE TT.EMPLT_APLCNT_ID = ? ");
		sql.append("  AND TT.EMPTST_ID = ? ");
		sql.append("  AND TT.EMPTST_MOD_TKN_DT = ");
		sql.append("      (SELECT Max(XX.EMPTST_MOD_TKN_DT) ");
		sql.append("       FROM APLCNT_EMPTST_TIER XX ");
		sql.append("       WHERE XX.EMPLT_APLCNT_ID = TT.EMPLT_APLCNT_ID ");
		sql.append("         AND XX.EMPTST_ID = TT.EMPTST_ID ");
		sql.append("         AND XX.ACTV_FLG = ?) ");
		sql.append("WITH UR");
		
		PhoneScreenCallHistoryTO categoryDetailsTO = (PhoneScreenCallHistoryTO) DAO.useJNDI(datasource)
				.setSQL(sql.toString())
				.input(1, applicantId)
				.input(2, categoryCode)
				.input(3, "Y")
				.get( PhoneScreenCallHistoryTO.class );
		
		logger.debug("Candidate Has needed Tier Code: " + categoryDetailsTO.getEMPTST_ID());
		if (categoryDetailsTO.getEMPTST_ID() != 0) {
			return true;
		} else {
			return false;
		}
	}*/	
	
	/**
	 * The method will be used to update the requisition statuses from DB.
	 * 
	 * @return boolean to indicate success/failure
	 * @throws QueryException
	 */
	public boolean updateRequisitionStatus(String reqNO, short reqStatus) throws RetailStaffingException {
		final UpdateRequisitionStatusResult result = new UpdateRequisitionStatusResult();

		MapStream inputs = new MapStream("updateRequisitionStatus");
		logger.info(this + "Enters updateRequisitionStatus method in RetailStaffingRequisitionDAO ");
		try {
			inputs.putAllowNull("requisitionStatusCode", (short) reqStatus); // can
			// be
			// null
			inputs.put("employmentRequisitionNumber", Integer.parseInt(reqNO));

			BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier() {
				public void notifyUpdate(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException {

					result.setTarget(target);
					result.setSuccess(success);
					result.setCount(count);
				}
			});
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.UPDATING_REVIEW_PHONE_SCREEN_ERROR_CODE,
					RetailStaffingConstants.UPDATING_REVIEW_PHONE_SCREEN_DETAILS_ERROR_MSG + "requisition number: " + reqNO, e);

		}
		logger.info(this + "Leaves updateRequisitionStatus method in RetailStaffingRequisitionDAO");
		return result.isSuccess();
	}

	public static class UpdateRequisitionStatusResult {
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

	/**
	 * The method will be used to update the phone screen statuses to DB.
	 * 
	 * @return boolean to indicate success/failure
	 * @throws QueryException
	 */
	public boolean updatePhoneScreenStatus(String phnScrnNo, short status) throws RetailStaffingException {
		final UpdatePhoneScreenStatusResult result = new UpdatePhoneScreenStatusResult();

		MapStream inputs = new MapStream("updatePhoneScreenStatus");
		logger.info(this + "Enters updatePhoneScreenStatus method in RetailStaffingRequisitionDAO for Phone Screen Number" + phnScrnNo);
		try {
			inputs.putAllowNull("overallRespondStatusCode", (short) status); // can
			// be
			// null
			inputs.put("humanResourcesPhoneScreenId", Integer.parseInt(phnScrnNo));

			BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier() {
				public void notifyUpdate(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException {
					result.setTarget(target);
					result.setSuccess(success);
					result.setCount(count);
				}
			});
		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.UPDATING_REVIEW_PHONE_SCREEN_ERROR_CODE,
					RetailStaffingConstants.UPDATING_REVIEW_PHONE_SCREEN_DETAILS_ERROR_MSG + "Phone Screen number: " + phnScrnNo, e);

		}
		logger.info(this + "Leaves updatePhoneScreenStatus method in RetailStaffingRequisitionDAO");
		return result.isSuccess();
	}

	public static class UpdatePhoneScreenStatusResult {
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

	/**
	 * The method will be used to get the details of inactive requisition based
	 * on region from DB.
	 * 
	 * @return map stream of requisition details
	 * @throws QueryException
	 */
	public Map<String, Object> readInactiveRequisitionsByRegion(final SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters readInactiveRequisitionsByStore method in dao ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemOperationsGroupCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);

		List<Short> phnStatusList = new ArrayList<Short>();
		phnStatusList.add(BASIC_ITI_STATUS);
		phnStatusList.add(DETAILED_ITI_STATUS);
		phnStatusList.add(PHONE_SCREEN_COMPLETED);
		phnStatusList.add(PHONE_SCREEN_FAVORABLE);
		inputsList.setPhoneScreenStatusCodeList(phnStatusList);

		List<Short> intvwMatStatusList = new ArrayList<Short>();
		intvwMatStatusList.add(INTERVIEW_MATERIAL_SENT);
		intvwMatStatusList.add(SEND_CALENDAR_PACKET);
		intvwMatStatusList.add(SEND_EMAIL_PACKET);
		intvwMatStatusList.add(INTERVIEW_MAT_STATUS_CODE_ZERO);
		inputsList.setInterviewMaterialStatusCodeList(intvwMatStatusList);

		List<Short> intvwStatusList = new ArrayList<Short>();
		intvwStatusList.add(SCHEDULE_INTERVIEW);
		intvwStatusList.add(INTERVIEW_STATUS_CODE_ZERO);
		inputsList.setInterviewStatusCodeList(intvwStatusList);
		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readInactiveRequisitionsByRegion");

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
					if (pagingRecordInfo != null) {
						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					List<Object> paginationTokenList = new ArrayList<Object>();

					while (results.next()) {// Update the pagination
						// token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
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
						reqDet.setQuewebNbr(results.getString("requestNumber"));
						reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						
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
			throw new RetailStaffingException(FETCHING_INACTIVE_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "storeid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getInactiveRequisitionsByStore method in DAO with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * The method will be used to get the details of inactive requisition based
	 * on district from DB.
	 * 
	 * @return map stream of requisition details
	 * @throws QueryException
	 */
	public Map<String, Object> readInactiveRequisitionsByDistrict(final SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters readInactiveRequisitionsByStore method in dao ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemRegionCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);

		List<Short> phnStatusList = new ArrayList<Short>();
		phnStatusList.add(BASIC_ITI_STATUS);
		phnStatusList.add(DETAILED_ITI_STATUS);
		phnStatusList.add(PHONE_SCREEN_COMPLETED);
		phnStatusList.add(PHONE_SCREEN_FAVORABLE);
		inputsList.setPhoneScreenStatusCodeList(phnStatusList);

		List<Short> intvwMatStatusList = new ArrayList<Short>();
		intvwMatStatusList.add(INTERVIEW_MATERIAL_SENT);
		intvwMatStatusList.add(SEND_CALENDAR_PACKET);
		intvwMatStatusList.add(SEND_EMAIL_PACKET);
		intvwMatStatusList.add(INTERVIEW_MAT_STATUS_CODE_ZERO);
		inputsList.setInterviewMaterialStatusCodeList(intvwMatStatusList);

		List<Short> intvwStatusList = new ArrayList<Short>();
		intvwStatusList.add(SCHEDULE_INTERVIEW);
		intvwStatusList.add(INTERVIEW_STATUS_CODE_ZERO);
		inputsList.setInterviewStatusCodeList(intvwStatusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readInactiveRequisitionsByDistrict");
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
					if (pagingRecordInfo != null) {
						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					List<Object> paginationTokenList = new ArrayList<Object>();

					while (results.next()) {// Update the pagination
						// token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
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
						reqDet.setQuewebNbr(results.getString("requestNumber"));
						reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						
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
			throw new RetailStaffingException(FETCHING_INACTIVE_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "storeid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getInactiveRequisitionsByStore method in DAO with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * The method will be used to get the details of inactive requisition based
	 * on division from DB.
	 * 
	 * @return map stream of requisition details
	 * @throws QueryException
	 */
	public Map<String, Object> readInactiveRequisitionsByDiv(final SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters readInactiveRequisitionsByDiv method in dao ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemDivisionCode(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);

		List<Short> phnStatusList = new ArrayList<Short>();
		phnStatusList.add(BASIC_ITI_STATUS);
		phnStatusList.add(DETAILED_ITI_STATUS);
		phnStatusList.add(PHONE_SCREEN_COMPLETED);
		phnStatusList.add(PHONE_SCREEN_FAVORABLE);
		inputsList.setPhoneScreenStatusCodeList(phnStatusList);

		List<Short> intvwMatStatusList = new ArrayList<Short>();
		intvwMatStatusList.add(INTERVIEW_MATERIAL_SENT);
		intvwMatStatusList.add(SEND_CALENDAR_PACKET);
		intvwMatStatusList.add(SEND_EMAIL_PACKET);
		intvwMatStatusList.add(INTERVIEW_MAT_STATUS_CODE_ZERO);
		inputsList.setInterviewMaterialStatusCodeList(intvwMatStatusList);

		List<Short> intvwStatusList = new ArrayList<Short>();
		intvwStatusList.add(SCHEDULE_INTERVIEW);
		intvwStatusList.add(INTERVIEW_STATUS_CODE_ZERO);
		inputsList.setInterviewStatusCodeList(intvwStatusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readInactiveRequisitionsByDivision");
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
					if (pagingRecordInfo != null) {
						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					List<Object> paginationTokenList = new ArrayList<Object>();

					while (results.next()) {// Update the pagination
						// token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
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
						reqDet.setQuewebNbr(results.getString("requestNumber"));
						reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						
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
			throw new RetailStaffingException(FETCHING_INACTIVE_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "storeid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves readInactiveRequisitionsByDiv method in DAO with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * The method will be used to get the details of inactive requisition based
	 * on store from DB.
	 * 
	 * @return map stream of requisition details
	 * @throws QueryException
	 */
	public Map<String, Object> readInactiveRequisitionsByStore(final SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters readInactiveRequisitionsByStore method in dao ");
		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemStoreNumber(summReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);

		List<Short> phnStatusList = new ArrayList<Short>();
		phnStatusList.add(BASIC_ITI_STATUS);
		phnStatusList.add(DETAILED_ITI_STATUS);
		phnStatusList.add(PHONE_SCREEN_COMPLETED);
		phnStatusList.add(PHONE_SCREEN_FAVORABLE);
		inputsList.setPhoneScreenStatusCodeList(phnStatusList);

		List<Short> intvwMatStatusList = new ArrayList<Short>();
		intvwMatStatusList.add(INTERVIEW_MATERIAL_SENT);
		intvwMatStatusList.add(SEND_CALENDAR_PACKET);
		intvwMatStatusList.add(SEND_EMAIL_PACKET);
		intvwMatStatusList.add(INTERVIEW_MAT_STATUS_CODE_ZERO);
		inputsList.setInterviewMaterialStatusCodeList(intvwMatStatusList);

		List<Short> intvwStatusList = new ArrayList<Short>();
		intvwStatusList.add(SCHEDULE_INTERVIEW);
		intvwStatusList.add(INTERVIEW_STATUS_CODE_ZERO);
		inputsList.setInterviewStatusCodeList(intvwStatusList);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readInactiveRequisitionsByStore");
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
					if (pagingRecordInfo != null) {
						paginationToken.put("CRT_TS", Util.convertTimestampTOtoTimestamp(pagingRecordInfo.getUpdatedTimestamp()));
						paginationToken.put("EMPLT_REQN_NBR", Integer.parseInt(pagingRecordInfo.getId()));

					}
					inputList.addQualifier("paginationToken", paginationToken);
				}

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);
			}
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					List<Object> paginationTokenList = new ArrayList<Object>();

					while (results.next()) {// Update the pagination
						// token.
						if (results.getObject("paginationToken") != null) {
							// NVStream qualifiers =
							// results.getSelector()
							// .getQualifiers();
							Object paginationToken = results.getObject("paginationToken");
							// Map<String, Map<String, Object>>
							// pagingMap = new HashMap<String,
							// Map<String, Object>>();
							/*
							 * reqDetMap .put(PAGINATION_TOKEN, pagingMap);
							 */

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
						reqDet.setPhnScrTyp(StringUtils.trim(results.getString("oe31")));
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
						reqDet.setStore(summReq.getOrgID());
						reqDet.setHumanResourcesSystemRegionCode(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						reqDet.setHumanResourcesSystemOperationsGroupCode(StringUtils.trim(results
								.getString("humanResourcesSystemOperationsGroupCode")));
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
						reqDet.setQuewebNbr(results.getString("requestNumber"));
						reqDet.setRscToManageFlg(StringUtils.trim(results.getString("retailStaffingCenterRequisitionManagedIndicator")));
						
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
			throw new RetailStaffingException(FETCHING_INACTIVE_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "storeid: "
					+ summReq.getOrgID(), e);
		}
		logger.info(this + "Leaves getInactiveRequisitionsByStore method in DAO with output as" + reqDetList);
		return reqDetMap;

	}

	/**
	 * This method is used to get the phone screen details based on the overall
	 * status
	 * 
	 * @param reqNbr
	 * @return requisition details
	 * @throws RetailStaffingException
	 */

	public RequisitionDetailTO readPhoneScreenByEmploymentRequisitionNumber(String reqNo) throws RetailStaffingException {
		logger.info(this + "Enters readPhoneScreenByEmploymentRequisitionNumber method ");
		final RequisitionDetailTO reqDet = new RequisitionDetailTO();
		try {
			MapStream inputs = new MapStream("readPhoneScreenByEmploymentRequisitionNumber");

			inputs.put("employmentRequisitionNumber", Integer.parseInt(reqNo));
			List<Short> statusList = new ArrayList<Short>();
			statusList.add(INTERVIEW_SCHEDULED);
			statusList.add(SCHEDULEINTERVIEW_ITI_STATUS);
			inputs.put("overallRespondStatusCodeList", statusList);
			inputs.put("authorizationPositionCount", (short) 1);

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					while (results.next()) {
						reqDet.setPhnScrnCnt(results.getInt("phoneScreenCount"));
					}
				}
			});

		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_INACTIVE_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG
					+ " Requisition Number : ", e);
		}
		logger.info(this + "Leaves readPhoneScreenByEmploymentRequisitionNumber method  with output as" + reqDet);

		return reqDet;
	}

	/**
	 * The method will be used to get the details of phone screen id by
	 * employment requisition based on region from DB.
	 * 
	 * @return map stream of phone screen details
	 * @throws QueryException
	 */
	public List<PhoneScreenIntrwDetailsTO> readPhoneScreenIdByEmploymentRequisitionNumber(String reqNo) throws RetailStaffingException {
		logger.info(this + "Enters readPhoneScreenIdByEmploymentRequisitionNumber method ");
		final List<PhoneScreenIntrwDetailsTO> phnScrnidDet = new ArrayList<PhoneScreenIntrwDetailsTO>();

		try {
			MapStream inputs = new MapStream("readPhoneScreenIdByEmploymentRequisitionNumber");
			inputs.put("employmentRequisitionNumber", Integer.parseInt(reqNo));
			List<Short> statusList = new ArrayList<Short>();
			statusList.add(BASIC_ITI_STATUS);
			statusList.add(DETAILED_ITI_STATUS);
			inputs.put("overallRespondStatusCodeList", statusList);

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
				PhoneScreenIntrwDetailsTO phnScrnDetTo = null;

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
					while (results.next()) {
						phnScrnDetTo = new PhoneScreenIntrwDetailsTO();
						phnScrnDetTo.setItiNbr(String.valueOf(results.getInt("humanResourcesPhoneScreenId")));
						phnScrnidDet.add(phnScrnDetTo);
					}
				}
			});

		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_INACTIVE_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG
					+ " Requisition Number : ", e);
		}
		logger.info(this + "Leaves readPhoneScreenByEmploymentRequisitionNumber method  with output as");

		return phnScrnidDet;
	}

	/**
	 * The method will be used to get the complete phone screen details from DB
	 * based on requisition number
	 * 
	 * @return map stream of phone screen details
	 * @throws QueryException
	 */
	public List<PhoneScreenIntrwDetailsTO> readCompletePhoneScreenDetails(int reqNbr) throws QueryException {
		final List<PhoneScreenIntrwDetailsTO> readRequisitionPhoneScreenList = new ArrayList<PhoneScreenIntrwDetailsTO>();

		MapStream inputs = new MapStream("readCompletePhoneScreenDetails");

		inputs.put("employmentRequisitionNumber", reqNbr);

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				PhoneScreenIntrwDetailsTO readRequisitionPhoneScreenDTO = null;
				IntrwLocationDetailsTO intrDtls = null;
				while (results.next()) {
					intrDtls = new IntrwLocationDetailsTO();
					readRequisitionPhoneScreenDTO = new PhoneScreenIntrwDetailsTO();
					readRequisitionPhoneScreenDTO.setItiNbr(String.valueOf(results.getInt("humanResourcesPhoneScreenId")));
					readRequisitionPhoneScreenDTO.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
					readRequisitionPhoneScreenDTO.setAid(StringUtils.trim(results.getString("candidatePersonId")));
					readRequisitionPhoneScreenDTO.setName(StringUtils.trim(results.getString("candidateName")));
					if (results.wasNull("minimumRequirementStatusCode")) {
						readRequisitionPhoneScreenDTO.setYnstatus(String.valueOf(15));
					} else {
						readRequisitionPhoneScreenDTO.setYnstatus(String.valueOf(results.getShort("minimumRequirementStatusCode")));
					}

					readRequisitionPhoneScreenDTO.setPhoneScreenStatusCode(results.getString("phoneScreenStatusCode"));
					readRequisitionPhoneScreenDTO.setInterviewStatusCode(results.getString("interviewStatusCode"));
					readRequisitionPhoneScreenDTO.setInterviewMaterialStatusCode(results.getString("interviewMaterialStatusCode"));

					readRequisitionPhoneScreenDTO.setIntrLocDtls(intrDtls);

					readRequisitionPhoneScreenDTO.getIntrLocDtls().setInterviewDate(Util.converDatetoDateTO(results.getDate("interviewDate")));
					
					//Added for CDP, remove leading zeros for display purposes.
					readRequisitionPhoneScreenDTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
			
					if (!results.wasNull("phoneScreenDispositionCode")) {
						readRequisitionPhoneScreenDTO.setPhoneScreenDispositionCode(results.getShort("phoneScreenDispositionCode"));
					}
					
					readRequisitionPhoneScreenList.add(readRequisitionPhoneScreenDTO);
				}
			}
		});

		return readRequisitionPhoneScreenList;
	}

	/**
	 * The method will be used to get the inactive requisitions from DB based on
	 * requisition number
	 * 
	 * @return map stream of phone screen details
	 * @throws QueryException
	 */
	public List<ReadInactiveRequisitionsByStoreDTO> readInactiveRequisitionsByStore() throws QueryException {
		final List<ReadInactiveRequisitionsByStoreDTO> readInactiveRequisitionsByStoreList = new ArrayList<ReadInactiveRequisitionsByStoreDTO>();

		MapStream inputs = new MapStream("readInactiveRequisitionsByStore");

		inputs.put("humanResourcesSystemStoreNumber", "value");
		List<Object> inputOverallRespondStatusCodeList1 = new ArrayList<Object>();
		inputOverallRespondStatusCodeList1.add((short) 0);
		inputs.put("overallRespondStatusCodeList", inputOverallRespondStatusCodeList1);
		inputs.put("tabno", "value");
		inputs.addQualifier("recordsRequested", 0); // optional
		inputs.addQualifier("pagingDirectionForward", false); // optional
		inputs.addQualifier("paginationToken", null); // optional

		BasicDAO.getResult("HrHrStaffing", 1, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				ReadInactiveRequisitionsByStoreDTO readInactiveRequisitionsByStoreDTO = null;
				while (results.next()) {
					readInactiveRequisitionsByStoreDTO = new ReadInactiveRequisitionsByStoreDTO();
					readInactiveRequisitionsByStoreDTO.setEmploymentRequisitionNumber(results.getInt("employmentRequisitionNumber"));
					readInactiveRequisitionsByStoreDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
					readInactiveRequisitionsByStoreDTO.setCreateUserId(results.getString("createUserId"));
					readInactiveRequisitionsByStoreDTO.setHumanResourcesSystemDepartmentNumber(results
							.getString("humanResourcesSystemDepartmentNumber"));
					readInactiveRequisitionsByStoreDTO.setJobTitleCode(results.getString("jobTitleCode"));
					readInactiveRequisitionsByStoreDTO.setJobTitleDescription(results.getString("jobTitleDescription"));
					readInactiveRequisitionsByStoreDTO.setOe31(results.getString("oe31"));
					readInactiveRequisitionsByStoreDTO.setRequiredPositionFillDate(results.getDate("requiredPositionFillDate"));
					readInactiveRequisitionsByStoreDTO.setOpenPositionCount(results.getShort("openPositionCount"));
					readInactiveRequisitionsByStoreDTO.setFullTimeRequiredFlag(results.getBoolean("fullTimeRequiredFlag"));
					readInactiveRequisitionsByStoreDTO.setPartTimeRequiredFlag(results.getBoolean("partTimeRequiredFlag"));
					readInactiveRequisitionsByStoreDTO.setHumanResourcesSystemRegionCode(results.getString("humanResourcesSystemRegionCode"));
					readInactiveRequisitionsByStoreDTO.setHumanResourcesSystemOperationsGroupCode(results
							.getString("humanResourcesSystemOperationsGroupCode"));
					readInactiveRequisitionsByStoreDTO.setHumanResourcesSystemDivisionCode(results.getString("humanResourcesSystemDivisionCode"));
					readInactiveRequisitionsByStoreDTO.setRequestNumber(results.getString("requestNumber"));
					readInactiveRequisitionsByStoreDTO.setPaginationToken(results.getObject("paginationToken"));
					readInactiveRequisitionsByStoreList.add(readInactiveRequisitionsByStoreDTO);
				}
			}
		});

		return readInactiveRequisitionsByStoreList;
	}

	// This inner class was generated for convenience. You may move this code
	// to an external class if desired.

	protected static class ReadInactiveRequisitionsByStoreDTO {

		// Instance variable for employmentRequisitionNumber
		private int employmentRequisitionNumber;

		// Instance variable for createTimestamp
		private Timestamp createTimestamp;

		// Instance variable for createUserId
		private String createUserId;

		// Instance variable for humanResourcesSystemDepartmentNumber
		private String humanResourcesSystemDepartmentNumber;

		// Instance variable for jobTitleCode
		private String jobTitleCode;

		// Instance variable for jobTitleDescription
		private String jobTitleDescription;

		// Instance variable for oe31
		private String oe31;

		// Instance variable for requiredPositionFillDate
		private Date requiredPositionFillDate;

		// Instance variable for openPositionCount
		private short openPositionCount;

		// Instance variable for fullTimeRequiredFlag
		private boolean fullTimeRequiredFlag;

		// Instance variable for partTimeRequiredFlag
		private boolean partTimeRequiredFlag;

		// Instance variable for humanResourcesSystemRegionCode
		private String humanResourcesSystemRegionCode;

		// Instance variable for humanResourcesSystemOperationsGroupCode
		private String humanResourcesSystemOperationsGroupCode;

		// Instance variable for humanResourcesSystemDivisionCode
		private String humanResourcesSystemDivisionCode;

		// Instance variable for requestNumber
		private String requestNumber;

		// Instance variable for paginationToken
		private Object paginationToken;

		// getter method for employmentRequisitionNumber
		public int getEmploymentRequisitionNumber() {
			return employmentRequisitionNumber;
		}

		// setter method for employmentRequisitionNumber
		public void setEmploymentRequisitionNumber(int aValue) {
			employmentRequisitionNumber = aValue;
		}

		// getter method for createTimestamp
		public Timestamp getCreateTimestamp() {
			return createTimestamp;
		}

		// setter method for createTimestamp
		public void setCreateTimestamp(Timestamp aValue) {
			createTimestamp = aValue;
		}

		// getter method for createUserId
		public String getCreateUserId() {
			return createUserId;
		}

		// setter method for createUserId
		public void setCreateUserId(String aValue) {
			createUserId = aValue;
		}

		// getter method for humanResourcesSystemDepartmentNumber
		public String getHumanResourcesSystemDepartmentNumber() {
			return humanResourcesSystemDepartmentNumber;
		}

		// setter method for humanResourcesSystemDepartmentNumber
		public void setHumanResourcesSystemDepartmentNumber(String aValue) {
			humanResourcesSystemDepartmentNumber = aValue;
		}

		// getter method for jobTitleCode
		public String getJobTitleCode() {
			return jobTitleCode;
		}

		// setter method for jobTitleCode
		public void setJobTitleCode(String aValue) {
			jobTitleCode = aValue;
		}

		// getter method for jobTitleDescription
		public String getJobTitleDescription() {
			return jobTitleDescription;
		}

		// setter method for jobTitleDescription
		public void setJobTitleDescription(String aValue) {
			jobTitleDescription = aValue;
		}

		// getter method for oe31
		public String getOe31() {
			return oe31;
		}

		// setter method for oe31
		public void setOe31(String aValue) {
			oe31 = aValue;
		}

		// getter method for requiredPositionFillDate
		public Date getRequiredPositionFillDate() {
			return requiredPositionFillDate;
		}

		// setter method for requiredPositionFillDate
		public void setRequiredPositionFillDate(Date aValue) {
			requiredPositionFillDate = aValue;
		}

		// getter method for openPositionCount
		public short getOpenPositionCount() {
			return openPositionCount;
		}

		// setter method for openPositionCount
		public void setOpenPositionCount(short aValue) {
			openPositionCount = aValue;
		}

		// getter method for fullTimeRequiredFlag
		public boolean getFullTimeRequiredFlag() {
			return fullTimeRequiredFlag;
		}

		// setter method for fullTimeRequiredFlag
		public void setFullTimeRequiredFlag(boolean aValue) {
			fullTimeRequiredFlag = aValue;
		}

		// getter method for partTimeRequiredFlag
		public boolean getPartTimeRequiredFlag() {
			return partTimeRequiredFlag;
		}

		// setter method for partTimeRequiredFlag
		public void setPartTimeRequiredFlag(boolean aValue) {
			partTimeRequiredFlag = aValue;
		}

		// getter method for humanResourcesSystemRegionCode
		public String getHumanResourcesSystemRegionCode() {
			return humanResourcesSystemRegionCode;
		}

		// setter method for humanResourcesSystemRegionCode
		public void setHumanResourcesSystemRegionCode(String aValue) {
			humanResourcesSystemRegionCode = aValue;
		}

		// getter method for humanResourcesSystemOperationsGroupCode
		public String getHumanResourcesSystemOperationsGroupCode() {
			return humanResourcesSystemOperationsGroupCode;
		}

		// setter method for humanResourcesSystemOperationsGroupCode
		public void setHumanResourcesSystemOperationsGroupCode(String aValue) {
			humanResourcesSystemOperationsGroupCode = aValue;
		}

		// getter method for humanResourcesSystemDivisionCode
		public String getHumanResourcesSystemDivisionCode() {
			return humanResourcesSystemDivisionCode;
		}

		// setter method for humanResourcesSystemDivisionCode
		public void setHumanResourcesSystemDivisionCode(String aValue) {
			humanResourcesSystemDivisionCode = aValue;
		}

		// getter method for requestNumber
		public String getRequestNumber() {
			return requestNumber;
		}

		// setter method for requestNumber
		public void setRequestNumber(String aValue) {
			requestNumber = aValue;
		}

		// getter method for paginationToken
		public Object getPaginationToken() {
			return paginationToken;
		}

		// setter method for paginationToken
		public void setPaginationToken(Object aValue) {
			paginationToken = aValue;
		}
	}

	/**
	 * The method will be used to insert scheduled preference data from the
	 * Requisition Request Page to DB.
	 * 
	 * @return boolean to indicate success/failure
	 * @throws QueryException
	 */
	public void insertSchedulePref(DAOConnection daoConn, Query query, String reqNbr, String weekDayNumber, String daySegmentCode)
			throws QueryException {

		MapStream inputs = new MapStream("createHumanResourcesRequisitionDailyAvailability");

		logger.info(this + "Enters createHumanResourcesRequisitionDailyAvailability method in Manager");
		try {
			inputs.put("employmentRequisitionNumber", Integer.parseInt(reqNbr));
			inputs.put("weekDayNumber", Short.parseShort(weekDayNumber));
			inputs.put("daySegmentCode", Short.parseShort(daySegmentCode));

			query.insertObject(daoConn, new BasicInsertNotifier(new InsertNotifier() {
				public void notifyInsert(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException {
				}
			}), inputs);

		} catch (Exception e) {
			throw new QueryException(e.getMessage());
		}
	}


	
	public List<ReadThdStoreEmploymentRequisitionDTO> readThdStoreEmploymentRequisition(String reqNbr) throws QueryException {

		final List<ReadThdStoreEmploymentRequisitionDTO> readThdStoreEmploymentRequisitionList = new ArrayList<ReadThdStoreEmploymentRequisitionDTO>();

		logger.info(this + "Enters readThdStoreEmploymentRequisition method in Manager with input as" + reqNbr);
		MapStream inputs = new MapStream("readThdStoreEmploymentRequisition");
		inputs.put("employmentRequisitionNumber", Integer.parseInt(reqNbr));
		logger.info("employmentRequisitionNumber -->" + Integer.parseInt(reqNbr));

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				ReadThdStoreEmploymentRequisitionDTO readThdStoreEmploymentRequisitionDTO = null;
				while (results.next()) {
					logger.info("Enter into result");
					readThdStoreEmploymentRequisitionDTO = new ReadThdStoreEmploymentRequisitionDTO();
					readThdStoreEmploymentRequisitionDTO.setEmploymentRequisitionNumber(results.getInt("employmentRequisitionNumber"));
					logger.info("value -->" + results.getInt("employmentRequisitionNumber"));
					readThdStoreEmploymentRequisitionDTO.setLastUpdateSystemUserId(results.getString("lastUpdateSystemUserId"));
					readThdStoreEmploymentRequisitionDTO.setLastUpdateTimestamp(results.getTimestamp("lastUpdateTimestamp"));
					readThdStoreEmploymentRequisitionDTO.setHireManagerName(results.getString("hireManagerName"));
					readThdStoreEmploymentRequisitionDTO.setJobTitleDescription(results.getString("jobTitleDescription"));
					readThdStoreEmploymentRequisitionDTO.setHireManagerPhoneNumber(results.getString("hireManagerPhoneNumber"));
					readThdStoreEmploymentRequisitionDTO.setRequestNumber(results.getString("requestNumber"));
					readThdStoreEmploymentRequisitionDTO.setTargetExperienceLevelCode(results.getShort("targetExperienceLevelCode"));
					if (results.wasNull("targetExperienceLevelCode")) {
						readThdStoreEmploymentRequisitionDTO.setTargetExperienceLevelCode(null);
					}
					readThdStoreEmploymentRequisitionDTO.setTargetPayAmount(results.getBigDecimal("targetPayAmount"));
					readThdStoreEmploymentRequisitionDTO.setHireEventFlag(results.getBoolean("hireEventFlag"));
					if (results.wasNull("hireEventFlag")) {
						readThdStoreEmploymentRequisitionDTO.setHireEventFlag(null);
					}
					readThdStoreEmploymentRequisitionDTO.setWeekBeginDate(results.getDate("weekBeginDate"));
					readThdStoreEmploymentRequisitionDTO.setHireManagerAvailabilityText(results.getString("hireManagerAvailabilityText"));
					readThdStoreEmploymentRequisitionDTO.setRequisitionStatusCode(results.getShort("requisitionStatusCode"));
					if (results.wasNull("requisitionStatusCode")) {
						readThdStoreEmploymentRequisitionDTO.setRequisitionStatusCode(null);
					}
					readThdStoreEmploymentRequisitionDTO.setRscScheduleFlag(results.getBoolean("rscScheduleFlag"));
					if (results.wasNull("rscScheduleFlag")) {
						readThdStoreEmploymentRequisitionDTO.setRscScheduleFlag(null);
					}
					readThdStoreEmploymentRequisitionDTO.setApplicantTemporaryFlag(results.getBoolean("applicantTemporaryFlag"));
					if (results.wasNull("applicantTemporaryFlag")) {
						readThdStoreEmploymentRequisitionDTO.setApplicantTemporaryFlag(null);
					}
					readThdStoreEmploymentRequisitionDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
					readThdStoreEmploymentRequisitionDTO.setCreateSystemUserId(results.getString("createSystemUserId"));
					readThdStoreEmploymentRequisitionDTO.setInterviewCandidateCount(results.getShort("interviewCandidateCount"));
					if (results.wasNull("interviewCandidateCount")) {
						readThdStoreEmploymentRequisitionDTO.setInterviewCandidateCount(null);
					}
					readThdStoreEmploymentRequisitionDTO.setInterviewMinutes(results.getShort("interviewMinutes"));
					if (results.wasNull("interviewMinutes")) {
						readThdStoreEmploymentRequisitionDTO.setInterviewMinutes(null);
					}
					readThdStoreEmploymentRequisitionDTO.setRequisitionCalendarId(results.getInt("requisitionCalendarId"));
					if (results.wasNull("requisitionCalendarId")) {
						readThdStoreEmploymentRequisitionDTO.setRequisitionCalendarId(null);
					}

					readThdStoreEmploymentRequisitionList.add(readThdStoreEmploymentRequisitionDTO);
				}
			}
		});
		logger.info(this + "Leaves readThdStoreEmploymentRequisition method in Manager with input as" + reqNbr);

		return readThdStoreEmploymentRequisitionList;
	}

	public boolean updateThdStoreEmploymentRequisition(ReadThdStoreEmploymentRequisitionDTO empReqDetails) throws QueryException {
		final GenericResults result = new GenericResults();
		logger.info(this + "Enters updateThdStoreEmploymentRequisition method in Manager with input as");
		MapStream inputs = new MapStream("updateThdStoreEmploymentRequisition");
		inputs.put("employmentRequisitionNumber", empReqDetails.getEmploymentRequisitionNumber());
		inputs.putAllowNull("hireManagerName", empReqDetails.getHireManagerName());
		inputs.putAllowNull("jobTitleDescription", empReqDetails.getJobTitleDescription());
		inputs.putAllowNull("hireManagerPhoneNumber", empReqDetails.getHireManagerPhoneNumber());
		inputs.putAllowNull("requestNumber", empReqDetails.getRequestNumber());
		inputs.putAllowNull("targetExperienceLevelCode", (short) empReqDetails.getTargetExperienceLevelCode());
		inputs.putAllowNull("targetPayAmount", empReqDetails.getTargetPayAmount());
		inputs.putAllowNull("hireEventFlag", empReqDetails.getHireEventFlag());
		inputs.putAllowNull("weekBeginDate", empReqDetails.getWeekBeginDate());
		inputs.putAllowNull("hireManagerAvailabilityText", empReqDetails.getHireManagerAvailabilityText());
		inputs.putAllowNull("requisitionStatusCode", (short) empReqDetails.getRequisitionStatusCode());
		inputs.putAllowNull("rscScheduleFlag", empReqDetails.getRscScheduleFlag());
		inputs.putAllowNull("applicantTemporaryFlag", empReqDetails.getApplicantTemporaryFlag());
		inputs.putAllowNull("interviewCandidateCount", empReqDetails.getInterviewCandidateCount());
		inputs.putAllowNull("interviewMinutes", (short) empReqDetails.getInterviewMinutes());
		inputs.putAllowNull("requisitionCalendarId", empReqDetails.getRequisitionCalendarId());
		BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier() {
			public void notifyUpdate(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException {
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});
		logger.info(this + "Leaves updateThdStoreEmploymentRequisition method in Manager with input as");

		return result.isSuccess();
	}

	/**
	 * The method will be used to get Mail Host Server from Parm
	 * tables in DB.
	 * 
	 * @return server name
	 * @throws QueryException
	 */
	public static String readHrOrgParmMailHost(String LCP) throws QueryException {
		final StringBuffer mailHost = new StringBuffer(15);

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		if (!LCP.equals("PR")) {
			inputs.put("parameterId", "rsa.mail.host.NONPR");
		} else {
			inputs.put("parameterId", "rsa.mail.host.PR");
		}

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					mailHost.append(StringUtils.trim(results.getString("parameterCharacterValue")));
				}
			}
		});

		return mailHost.toString();
	}
	
	/**
	 * The method will be used to get the status of the Qualified Pool from Parm
	 * tables in DB.
	 * 
	 * @return Off, Production, Pilot
	 * @throws QueryException
	 */
	public String readHrOrgParmQpStatus() throws QueryException {
		final StringBuffer qpStatus = new StringBuffer(15);

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "qualified.pool.status");

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					qpStatus.append(StringUtils.trim(results.getString("parameterCharacterValue")));
				}
			}
		});

		return qpStatus.toString();
	}

	/**
	 * The method will be used to get the status of the Qualified Pool from Parm
	 * tables in DB.
	 * 
	 * @return Off, Production, Pilot
	 * @throws QueryException
	 */
	public List<String> readHrOrgParmQpStatusIsPilot() throws QueryException {
		final List<String> pilotUserList = new ArrayList<String>();

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "qualified.pool.user.");
		inputs.addQualifier("parameterIdSearch", true);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					if (results.getString("parameterCharacterValue") != null && !results.getString("parameterCharacterValue").equals("")) {
						String[] userList = results.getString("parameterCharacterValue").split(",");
						for (int i = 0; i < userList.length; i++) {
							pilotUserList.add(StringUtils.trim(userList[i]));
						}
					}
				}
			}
		});

		return pilotUserList;
	}
	
	public List<ReqScheduleTO> readHumanResourcesRequisitionSchedule(int reqCalId, Timestamp beginTime ) throws RetailStaffingException
	{
			if( logger.isDebugEnabled() )
			{
				logger.info("Enter into readHumanResourcesRequisitionSchedule");
			}
			final List<ReqScheduleTO> readHumanResourcesRequisitionScheduleList = new ArrayList<ReqScheduleTO>();
			try {			
				MapStream inputs = new MapStream("readHumanResourcesRequisitionSchedule");
				inputs.put("requisitionCalendarId", reqCalId);
				inputs.put("beginTimestamp", beginTime); // optional
				//inputs.put("beginTimestampDate", ""); // optional
				//Removed because if a sequenceNumber 1 does not exist then the store number is not found causing fatal error to occur
				//inputs.put("sequenceNumber", (short)1); // optional

				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

						ReqScheduleTO readHumanResourcesRequisitionScheduleDTO = null;
						while (results.next()) {
							readHumanResourcesRequisitionScheduleDTO = new ReqScheduleTO();
							readHumanResourcesRequisitionScheduleDTO.setCrtTs(results.getTimestamp("createTimestamp"));
							readHumanResourcesRequisitionScheduleDTO.setCrtSysUsrId(results.getString("createSystemUserId"));
							readHumanResourcesRequisitionScheduleDTO.setLastUpdSysUsrId(results.getString("lastUpdateSystemUserId"));
							readHumanResourcesRequisitionScheduleDTO.setLastUpdTs(results.getTimestamp("lastUpdateTimestamp"));
							readHumanResourcesRequisitionScheduleDTO.setHrSysStrNbr(results.getString("humanResourcesSystemStoreNumber"));
							
							if(!results.wasNull("requisitionScheduleStatusCode"))
							{
								readHumanResourcesRequisitionScheduleDTO.setReqnSchStatCd(results.getShort("requisitionScheduleStatusCode"));
							}
							readHumanResourcesRequisitionScheduleList.add(readHumanResourcesRequisitionScheduleDTO);
						}
					}
				});
			} catch (Exception e) {
				throw new RetailStaffingException(FETCHING_INTERVIEW_RESPONSES_DETAILS_ERROR_CODE, e);
			}
			if( logger.isDebugEnabled() )
			{
				logger.info("readHumanResourcesReqCount" + readHumanResourcesRequisitionScheduleList.size());
				logger.info("Exit from readHumanResourcesRequisitionSchedule");			
			}		
			return readHumanResourcesRequisitionScheduleList;
		}
	
	/**
	 * This method is used to get the candidate details for a Requisition.  This uses DAO 2.0
	 * 
	 * @param reqNbr
	 * @param strNbr
	 * @return list of candidate details
	 * @throws QueryException
	 */
	public List<CandidateDetailsTO> readRequisitionCandidate_DAO20(int reqNbr, String strNbr) throws QueryException {
			
			Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
			
			List<CandidateDetailsTO> readRequisitionCandidateList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
					.setSQL(SQL_GET_REQUISITION_CANDIDATE_DATA)
					.displayAs("Read Requisition and Candidate Data for use on Interview Summary Form")
					.input(1, currentDate)
					.input(2, currentDate)
					.input(3, strNbr) 
					.input(4, reqNbr)
					.input(5, "Y")
					.input(6, "Y")
					.input(7, strNbr) 
					.input(8, reqNbr)
					.input(9, "Y")
					.input(10, "Y")
					.input(11, currentDate)
					.input(12, currentDate)
					.input(13, "USA")
					.debug(logger)
					.formatCycles(1)
					.list(CandidateDetailsTO.class);
		
		return readRequisitionCandidateList;
	}	

	/**
	 * The method will be used to get the Drug Test Type of a Location for the ISF form, from TRPRX000
	 * table in DB.
	 * 
	 * @return Drug Test Type Conventional Condition Yes, Other, No
	 * @throws QueryException
	 */
	public static String readLocDTStatusIsConv(final String LocNbr) throws QueryException
	{	
		String drugTestTypeConvCond;
		String drugTestTypeConv;
		
		if(logger.isDebugEnabled())
		{
			logger.debug(String.format("Entering readLocDTStatusIsConv(), LocNbr: %1$s", LocNbr));
		} // end if
		
		try
		{
			drugTestTypeConvCond = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
						   	.setSQL(SQL_GET_LOCATION_DRUGTESTTYPE)
						   	.input(1, LocNbr)
							.debug(logger)
							.get(String.class);
			if (drugTestTypeConvCond.equals("Y") ){
				drugTestTypeConv = "CONV";
			} else {
				drugTestTypeConv = "ORAL";
			}
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			logger.error(String.format("An exception occurred getting location Drug Test Type"), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		return drugTestTypeConv;
	}
	
}

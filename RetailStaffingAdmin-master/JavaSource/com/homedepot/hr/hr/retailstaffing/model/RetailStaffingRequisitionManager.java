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

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.PhoneScreenDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingCandtDtlDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingITIDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.dto.AutoAttachJobTitlesTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.DateTO;
import com.homedepot.hr.hr.retailstaffing.dto.DeptDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ExperienceLevelTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventMgrTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.JobTtlDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.LanguageSkillsTO;
import com.homedepot.hr.hr.retailstaffing.dto.LocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PagingRecordInfo;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadHumanResourcesStoreEmploymentRequisitionDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadThdStoreEmploymentRequisitionDTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.UserDataTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.ITIUpdateRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.JobTitleRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateReviewPhnScrnRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateStaffingRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.AutoAttachJobTitleResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CompleteITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.DeptResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ExpLvlResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetaiInactivelResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.JobTtlResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.LangSklsResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.ScheduleDescResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDetailResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.services.response.StatusResponse;
import com.homedepot.hr.hr.retailstaffing.util.EmailSender;
import com.homedepot.hr.hr.retailstaffing.util.PropertyReader;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.catalina.realm.Profile;
import com.homedepot.ta.aa.dao.ConnectionHandler;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.NVStream;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.annotations.AnnotatedQueryHandler;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;
import com.homedepot.ta.aa.dao.basic.TransactionConnectionHandler;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.exceptions.UpdateException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to have the business logic for Search based on Requisition
 * Number. Methods: getRequisitionDetails updateStaffingDetails
 * getRequisitionsByStore getRequisitionsByReg getRequisitionsByDist
 * getRequisitionsByDiv
 * 
 * @author TCS
 * 
 */
public class RetailStaffingRequisitionManager implements RetailStaffingConstants, DAOConstants {
	private static final Logger logger = Logger.getLogger(RetailStaffingRequisitionManager.class);
	public int eventId = 0;

	/**
	 * This method is used to get the requisition details based on the
	 * requisition number.
	 * 
	 * @param reqNbr
	 * @return
	 * @throws RetailStaffingException
	 */
	public Response getRequisitionDetails(String ReqNo) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionDetails method in Manager with input" + ReqNo);
		Response res = null;

		StoreDetailResponse StrRes = new StoreDetailResponse();
		List<StateDetailsTO> states = null;
		StateDetailResponse stDetRes = new StateDetailResponse();
		ITIDetailResponse itiDetRes = new ITIDetailResponse();
		List<RequisitionDetailTO> reqList = null;
		List<PhoneScreenIntrwDetailsTO> phnScrIntrTo = null;
		List<CandidateDetailsTO> candDetTo = null;
		CandidateDetailResponse canDetRes = new CandidateDetailResponse();
		RequisitionDetailTO reqDtl = null;
		PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		List<StaffingDetailsTO> stfList = null;
		StaffingDetailsTO stfDtls = null;
		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();
		ExpLvlResponse expLvlres = new ExpLvlResponse();
		List<ExperienceLevelTO> oExpLvlRes = null;
		ScheduleDescResponse calendarListRes = new ScheduleDescResponse(); 
		List<RequisitionCalendarTO> calendarList = null;
		StatusResponse statRes = new StatusResponse();
		RequisitionDetailTO reqDet = null;
		StoreDetailsTO store = null;
		HiringEventsManager myMgr = new HiringEventsManager();
		HiringEventMgrTO hiringEventMgrTO = null;
		List<SchedulePreferenceTO> schPrefList = null;
		List<LanguageSkillsTO> oLangSklsRes = null;
		LangSklsResponse langSklsres = new LangSklsResponse();
		List<LanguageSkillsTO> reqnLangSklsList = null;
		
		
		try {
			res = new Response();
			if (ReqNo != null && !ReqNo.trim().equals("")) {
				// get Store Number from DAO
				reqList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(ReqNo));
				stfList = phnScrnDAO.readRequisitionStaffingDetails(Integer.parseInt(ReqNo));
				if (stfList != null && !stfList.isEmpty() && stfList.size() > 0) {
					stfDtls = stfList.get(0);
					res.setStfDtls(stfDtls);
				}
				if (reqList != null && !reqList.isEmpty() && reqList.size() > 0) {
					reqDtl = reqList.get(0);
					// getting store data from DAO
					store = LocationManager.getStoreDetailsDAO20(reqDtl.getStore());

					if(stfList != null && stfList.size() > 0)
					{
						reqList.get(0).setInsertUpdate(UPDATE);
					} 
					else 
					{
						reqList.get(0).setInsertUpdate(INSERT);
					}

					// to fetch basic requisition details
					reqRes.setReqDtlList(reqList);
					res.setReqDtlList(reqRes);
					// to fetch candidate details for requisition screen
					candDetTo = delReqMgr.readRequisitionCandidate(Integer.parseInt(ReqNo), reqDtl.getStore());
					if (candDetTo != null && candDetTo.size() > 0) {
						canDetRes.setCndDtlList(candDetTo);
						res.setCanDtRes(canDetRes);
					}
					
					//Added for Auto-Attach - Get Requisition Schedule Preference
					schPrefList = delReqMgr.getReqnSchedulePref(Integer.parseInt(ReqNo));
					if (schPrefList != null && !schPrefList.isEmpty() && schPrefList.size() > 0) {
						reqRes.getReqDtlList().get(0).setReqnSchdPref(schPrefList);
					}
					
					//Added for Auto-Attach - Get requested Requisition Languages
					//Get Requisition Language Preference
					reqnLangSklsList = delReqMgr.getReqnLanguagePref(Integer.parseInt(ReqNo));
					if (reqnLangSklsList != null && !reqnLangSklsList.isEmpty() && reqnLangSklsList.size() > 0) {
						reqRes.getReqDtlList().get(0).setReqnLangPref(reqnLangSklsList);
					}					
					
				}
				// to fetch desired experience level
				oExpLvlRes = delReqMgr.readNlsExperienceLevelList();
				if (oExpLvlRes != null && oExpLvlRes.size() > 0) {
					expLvlres.setExpLvlList(oExpLvlRes);
					res.setExpLvlRes(expLvlres);
				}

				//ATS Hiring Events
				//This is used to get the correct Calendar/Hiring Event drop down values.  
				//When it is not a Hiring Event or Hiring Event Type is 'SDE', which is the old Hiring Event.
				//An AIMS Created Requisition will not have Hiring Event Data, so strDtls will be null.  By default get Calendars 
				//Get the calendar drop down				
				if (stfDtls == null || stfDtls.getHrgEvntFlg().equals("N") || stfDtls.getHireEvntType().equals("SDE")) {					
					//To fetch Calendar Detail List by Store
					calendarList = ScheduleManager.getRequisitionCalendarsForStore(reqDtl.getStore(), false);
					calendarListRes.setSchDescList(calendarList);
					res.setScheduleRes(calendarListRes);
				} else if (stfDtls.getHrgEvntFlg().equals("Y") && !stfDtls.getHireEvntType().equals("SDE")) {
					//Get Hiring Event calendars
					calendarList = ScheduleManager.getRequisitionHiringEventsForStore(reqDtl.getStore());
					calendarListRes.setSchDescList(calendarList);
					res.setScheduleRes(calendarListRes);					
				}
				
				//ATS Hiring Events
				//When it is an ATS Hiring Event need to get the Event Manager Data
				//An AIMS Created Requisition will not have Hiring Event Data, so strDtls will be null.				
				if (stfDtls != null) {
					if (stfDtls.getHrgEvntFlg().equals("Y") && !stfDtls.getHireEvntType().equals("SDE")) {
						if (stfDtls.getHireEvntMgrAssociateId() != null) {
							//Get Hiring Event Mgr Data
							hiringEventMgrTO = myMgr.getHiringEventMgrData(null, stfDtls.getHireEvntMgrAssociateId());
							res.setHiringEventMgrData(hiringEventMgrTO);
						}
					}
				}
				
				
				// fetch requisition statuses
				statRes.setRequisitionStats(new ArrayList<Status>(StatusManager.getStatusObjects(StatusType.REQUISITION_STATUS, "EN_US").values()));
				res.setStatusListRes(statRes);
				
				// to fetch states
				states = delReqMgr.readStateList();
				if (states != null && states.size() > 0) {
					stDetRes.setStrDtlList(states);
					res.setStateDtRes(stDetRes);
				}
				// to fetch phone screen details
				phnScrIntrTo = delReqMgr.readRequisitionPhoneScreen(Integer.parseInt(ReqNo));
				if (phnScrIntrTo != null && phnScrIntrTo.size() > 0) {

					itiDetRes.setITIDtlList(phnScrIntrTo);
					res.setItiDtRes(itiDetRes);
				}

				StrRes.addStore(store);
				res.setStrDtRes(StrRes);

				// To Check if for phone screens autoHold condition is satisfied
				reqDet = delReqMgr.readPhoneScreenByEmploymentRequisitionNumber(ReqNo);
				if (reqDet != null && reqDet.getPhnScrnCnt() > 0) {
					res.setAutoHold(AUTO_HOLD_FLAG_TRUE);
				} else {
					res.setAutoHold(AUTO_HOLD_FLAG_FALSE);
				}
				
				// to fetch language skills
				oLangSklsRes = delReqMgr.readLanguageSkillsList();
				if (oLangSklsRes != null && oLangSklsRes.size() > 0) {
					langSklsres.setLangSklsList(oLangSklsRes);
					res.setLangSklsRes(langSklsres);
				}				
				
			} else {
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRequisitionDetails method in Manager with output" + res);
		return res;
	}

	/**
	 * This method is used to get the requisition details based on the
	 * requisition number.
	 * 
	 * @param reqNbr
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getInactiveRequisitionDetails(String ReqNo) throws RetailStaffingException {
		logger.info(this + "Enters getInactiveRequisitionDetails method in Manager with input" + ReqNo);
		Response res = null;

		StoreDetailResponse StrRes = new StoreDetailResponse();
		StoreDetailsTO store = null;

		List<StateDetailsTO> states = null;
		StateDetailResponse stDetRes = new StateDetailResponse();
		ITIDetailResponse itiDetRes = new ITIDetailResponse();
		List<RequisitionDetailTO> reqList = null;
		List<PhoneScreenIntrwDetailsTO> phnScrIntrTo = null;
		RequisitionDetailTO reqDtl = null;
		PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		List<StaffingDetailsTO> stfList = null;
		StaffingDetailsTO stfDtls = null;
		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();
		ExpLvlResponse expLvlres = new ExpLvlResponse();
		List<ExperienceLevelTO> oExpLvlRes = null;
		StatusResponse statRes = new StatusResponse();
		
		try 
		{
			res = new Response();
			if(ReqNo != null && ReqNo.trim().length() > 0)
			{
				// get Store Number from DAO
				reqList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(ReqNo));
				stfList = phnScrnDAO.readRequisitionStaffingDetails(Integer.parseInt(ReqNo));
				if (stfList != null && !stfList.isEmpty() && stfList.size() > 0) {
					stfDtls = stfList.get(0);
					res.setStfDtls(stfDtls);
				}
				if (reqList != null && !reqList.isEmpty() && reqList.size() > 0) {
					reqDtl = reqList.get(0);
					// getting store data from DAO
					store = LocationManager.getStoreDetailsDAO20(reqDtl.getStore());
					
					if(stfList != null && stfList.size() > 0)
					{
						reqList.get(0).setInsertUpdate(UPDATE);
					} 
					else 
					{
						reqList.get(0).setInsertUpdate(INSERT);
					}

					// to fetch basic requisition details
					reqRes.setReqDtlList(reqList);
					res.setReqDtlList(reqRes);
				}
				// to fetch desired experience level
				oExpLvlRes = delReqMgr.readNlsExperienceLevelList();
				if (oExpLvlRes != null && oExpLvlRes.size() > 0) {
					expLvlres.setExpLvlList(oExpLvlRes);
					res.setExpLvlRes(expLvlres);
				}

				// fetch requisition statuses
				statRes.setRequisitionStats(new ArrayList<Status>(StatusManager.getStatusObjects(StatusType.REQUISITION_STATUS, "EN_US").values()));
				res.setStatusListRes(statRes);

				// to fetch states
				states = delReqMgr.readStateList();
				if (states != null && states.size() > 0) {
					stDetRes.setStrDtlList(states);
					res.setStateDtRes(stDetRes);
				}
				// to fetch phone screen details
				phnScrIntrTo = delReqMgr.readCompletePhoneScreenDetails(Integer.parseInt(ReqNo));
				if (phnScrIntrTo != null && phnScrIntrTo.size() > 0) {

					itiDetRes.setITIDtlList(phnScrIntrTo);
					res.setItiDtRes(itiDetRes);
				}

				StrRes.addStore(store);
				res.setStrDtRes(StrRes);

			} else {
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRequisitionDetails method in Manager with output" + res);
		return res;
	}

	/**
	 * This method is used to get the requisition details based on the of review
	 * completed phone screens
	 * 
	 * @param reqNo
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getRevCmplPhnScrnDetails(String reqNo) throws RetailStaffingException {

		logger.info(this + "Enters getRevCmplPhnScrnDetails method in Manager with input as" + reqNo);
		Response res = null;
		StaffingDetailsTO ReqstaffingDetTO = null;
		PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
		List<RequisitionDetailTO> reqList = null;
		List<RequisitionDetailTO> reqCandList = null;
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		List<PhoneScreenIntrwDetailsTO> phnScrIntrToList = null;
		List<PhoneScreenIntrwDetailsTO> phnScrIntrToListInAct = null;
		PhoneScreenIntrwDetailsTO phnScrnTo = null;
		List<PhoneScreenIntrwDetailsTO> phnScrnlist = null;
		List<PhoneScreenIntrwDetailsTO> totalPhnScrnlist = new ArrayList<PhoneScreenIntrwDetailsTO>();
		List<PhoneScreenIntrwDetailsTO> totalPhnScrnlistInactive = new ArrayList<PhoneScreenIntrwDetailsTO>();
		PhoneScreenIntrwDetailsTO phnScrnIntrwDtls = null;
		List<PhoneScreenIntrwDetailsTO> phcrnResOverReslist = null;
		List<PhoneScreenIntrwDetailsTO> phnScrnResTxtlist = null;
		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();
		ITIDetailResponse itiRes = new ITIDetailResponse();
		ITIDetaiInactivelResponse itiInactRes = new ITIDetaiInactivelResponse();
		RetailStaffingCandtDtlDAO candDao = new RetailStaffingCandtDtlDAO();
		List<PhoneScreenIntrwDetailsTO> phnScrIntrTo = null;
		CompleteITIDetailResponse cmplITIRes = new CompleteITIDetailResponse();
		List<StaffingDetailsTO> stfList = null;
		StaffingDetailsTO stfDtls = null;
		List<ReadHumanResourcesStoreEmploymentRequisitionDTO> readStoreReq = new ArrayList<ReadHumanResourcesStoreEmploymentRequisitionDTO>();

		StatusResponse statRes = new StatusResponse();
		
		try {
			res = new Response();
			if (reqNo != null && !reqNo.trim().equals("")) {
				// ReqstaffingDetTO = new StaffingDetailsTO();
				// get Store Number from DAO
				reqList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(reqNo));
				ReqstaffingDetTO = delReqMgr.readEmploymentRequisitionNote(Integer.parseInt(reqNo), REFERRALS_NOTE_TYP_CD);
				RequisitionDetailTO reqTo = (RequisitionDetailTO) reqList.get(0);
				reqTo.setReferrals(ReqstaffingDetTO.getReferals());
				reqList.remove(0);
				reqList.add(reqTo);

				// To fetch Complete phone screen details(Both Active and
				// Inactive)
				phnScrIntrTo = delReqMgr.readCompletePhoneScreenDetails(Integer.parseInt(reqNo));
				if (phnScrIntrTo != null && phnScrIntrTo.size() > 0) {

					cmplITIRes.setITIDtlList(phnScrIntrTo);
					res.setCmplITIDet(cmplITIRes);
				}

				// To set Authorisation position count for requisition

				readStoreReq = phnScrnDAO.readHumanResourcesStoreEmploymentRequisition(Integer.parseInt(reqNo));
				if (readStoreReq != null && readStoreReq.size() > 0) {
					res.setAuthCnt(String.valueOf(readStoreReq.get(0).getAuthorizationPositionCount()));
				}

				stfList = phnScrnDAO.readRequisitionStaffingDetails(Integer.parseInt(reqNo));
				if (stfList != null && !stfList.isEmpty() && stfList.size() > 0) {
					stfDtls = stfList.get(0);
					res.setStfDtls(stfDtls);
				}

			}
			
			// invoke the business logic method to get all status objects
			Map<StatusType, List<Status>> stats = StatusManager.getAllStatusObjects("EN_US");
			
			/*
			 * no null check here because the response map returned will not be null and if no statuses 
			 * were found for any of the status types a NoRowsFoundException will be thrown by the 
			 * business logic method.
			 */
			statRes.setPhoneScreenStats(stats.get(StatusType.PHONE_SCREEN_STATUS));
			statRes.setInterviewStats(stats.get(StatusType.INTERVIEW_STATUS));
			statRes.setMaterialsStats(stats.get(StatusType.MATERIALS_STATUS));
			statRes.setRequisitionStats(stats.get(StatusType.REQUISITION_STATUS));
											
			res.setStatusListRes(statRes);
			

			// to fetch phone screen active details
			phnScrIntrToList = delReqMgr.readRequisitionPhoneScreen(Integer.parseInt(reqNo));
			// To fetch phone screens inactive

			phnScrIntrToListInAct = delReqMgr.readRequisitionPhoneScreenInactive(Integer.parseInt(reqNo));

			if (phnScrIntrToList != null && phnScrIntrToList.size() > 0) {
				int phnScrIntrToListSize = phnScrIntrToList.size();
				for (int i = 0; i < phnScrIntrToListSize; i++) {
					phnScrnTo = (PhoneScreenIntrwDetailsTO) phnScrIntrToList.get(i);
					phnScrnlist = phnScrnDAO.readByPhoneScreenNumber(Integer.parseInt(phnScrnTo.getItiNbr()));
					if (phnScrnlist != null && !phnScrnlist.isEmpty()) {
						phnScrnIntrwDtls = phnScrnlist.get(0);

						if (phnScrnIntrwDtls.getAid() != null && !phnScrnIntrwDtls.getAid().trim().equals(EMPTY_STRING)) {
							// Set internal if AID is present
							phnScrnIntrwDtls.setInternalExternal(INTERNAL_FLAG);
						} else {
							// Set external if no AID is present
							phnScrnIntrwDtls.setInternalExternal(EXTERNAL_FLAG);
							phnScrnIntrwDtls.setCndStrNbr(EMPTY_STRING);
							phnScrnIntrwDtls.setCndDeptNbr(EMPTY_STRING);
							phnScrnIntrwDtls.setTitle(EMPTY_STRING);
						}

						phnScrnIntrwDtls.setPhoneScreenStatusCode(phnScrnTo.getPhoneScreenStatusCode());
						phnScrnIntrwDtls.setInterviewStatusCode(phnScrnTo.getInterviewStatusCode());
						phnScrnIntrwDtls.setInterviewMaterialStatusCode(phnScrnTo.getInterviewMaterialStatusCode());

						phcrnResOverReslist = PhoneScreenDAO.readHumanResourcesPhoneScreen(Integer.parseInt(phnScrnTo.getItiNbr()));
						if (phcrnResOverReslist != null && !phcrnResOverReslist.isEmpty() && phcrnResOverReslist.size() > 0) {
							phnScrnIntrwDtls.setOverAllStatus(phcrnResOverReslist.get(0).getOverAllStatus());
							phnScrnIntrwDtls.setPhnScreener(phcrnResOverReslist.get(0).getPhnScreener());
							phnScrnIntrwDtls.setYnstatus(phcrnResOverReslist.get(0).getYnstatus());
							phnScrnIntrwDtls.setPhnScrnDate(phcrnResOverReslist.get(0).getPhnScrnDate());
							phnScrnIntrwDtls.setPhnScrnTime(phcrnResOverReslist.get(0).getPhnScrnTime());
							// call for the minimum status desc
							//Not sure why this is in the code.  ynStatus has nothing to do with Interview Status.  Removed by MTS1876
							//05/18/2011 while implementing Status Code Caching							
							/*if (phnScrnIntrwDtls.getYnstatus() != null && !phnScrnIntrwDtls.getYnstatus().trim().equals(EMPTY_STRING)) {
								statusTo = phnScrnDAO.readNlsInterviewRespondStatus(Short.parseShort(phnScrnIntrwDtls.getYnstatus()));
								if (statusTo != null) {
									if (statusTo.getStatusDesc() != null) {
										phnScrnIntrwDtls.setYnStatusDesc(statusTo.getStatusDesc().trim());
									} else {
										phnScrnIntrwDtls.setYnStatusDesc(EMPTY_STRING);
									}
								}
							}*/
						}
						// to get the note text for Response - notetypecode is 2
						// -Detail response Text History
						phnScrnResTxtlist = null;
						phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(Integer.parseInt(phnScrnTo.getItiNbr()),
								PHN_SCRN_RESPONSE_NOTE_TYP_CD);
						if (phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0) {
							phnScrnIntrwDtls.setDetailTxt(phnScrnResTxtlist.get(0).getDetailTxt());
						}
						// to fetch Candidate offer status
						reqCandList = candDao.getRequisitionDetailsForCandidate(phnScrnIntrwDtls.getCndtNbr());
						if (reqCandList != null && reqCandList.size() > 0) {
							RequisitionDetailTO CandDetails = (RequisitionDetailTO) reqCandList.get(0);
							phnScrnIntrwDtls.setOffered(CandDetails.getOfferMade());

							PropertyReader propertyReader = PropertyReader.getInstance();

							if (CandDetails.getOfferStat().equals(ACCEPTED_STATUS)) {
								phnScrnIntrwDtls.setOfferStatus(propertyReader.getProperty(ACCEPTED_STATUS));
							}

							else if (CandDetails.getOfferStat().equals(DECLINED_STATUS)) {
								phnScrnIntrwDtls.setOfferStatus(propertyReader.getProperty(DECLINED_STATUS));
							}

							else
								phnScrnIntrwDtls.setOfferStatus(CandDetails.getOfferStat());
						}

						totalPhnScrnlist.add(phnScrnIntrwDtls);

					}
					/*
					 * itiDetRes.setITIDtlList(phnScrIntrTo);
					 * res.setItiDtRes(itiDetRes);
					 */
				}

				if (phnScrIntrToListInAct != null && phnScrIntrToListInAct.size() > 0) {
					int phnScrIntrToListInActSize = phnScrIntrToListInAct.size();
					for (int i = 0; i < phnScrIntrToListInActSize; i++) {
						phnScrnTo = (PhoneScreenIntrwDetailsTO) phnScrIntrToListInAct.get(i);
						phnScrnlist = phnScrnDAO.readByPhoneScreenNumber(Integer.parseInt(phnScrnTo.getItiNbr()));

						if (phnScrnlist != null && !phnScrnlist.isEmpty()) {
							phnScrnIntrwDtls = phnScrnlist.get(0);

							if (phnScrnIntrwDtls.getAid() != null && !phnScrnIntrwDtls.getAid().trim().equals(EMPTY_STRING)) {
								// Set internal if AID is present
								phnScrnIntrwDtls.setInternalExternal(INTERNAL_FLAG);
							} else {
								// Set external if no AID is present
								phnScrnIntrwDtls.setInternalExternal(EXTERNAL_FLAG);
								phnScrnIntrwDtls.setCndStrNbr(EMPTY_STRING);
								phnScrnIntrwDtls.setCndDeptNbr(EMPTY_STRING);
								phnScrnIntrwDtls.setTitle(EMPTY_STRING);
							}

							phnScrnIntrwDtls.setPhoneScreenStatusCode(phnScrnTo.getPhoneScreenStatusCode());
							phnScrnIntrwDtls.setInterviewStatusCode(phnScrnTo.getInterviewStatusCode());
							phnScrnIntrwDtls.setInterviewMaterialStatusCode(phnScrnTo.getInterviewMaterialStatusCode());

							phcrnResOverReslist = phnScrnDAO.readHumanResourcesPhoneScreen(Integer.parseInt(phnScrnTo.getItiNbr()));
							if (phcrnResOverReslist != null && !phcrnResOverReslist.isEmpty() && phcrnResOverReslist.size() > 0) {
								phnScrnIntrwDtls.setOverAllStatus(phcrnResOverReslist.get(0).getOverAllStatus());
								phnScrnIntrwDtls.setPhnScreener(phcrnResOverReslist.get(0).getPhnScreener());
								phnScrnIntrwDtls.setYnstatus(phcrnResOverReslist.get(0).getYnstatus());
								phnScrnIntrwDtls.setPhnScrnDate(phcrnResOverReslist.get(0).getPhnScrnDate());
								phnScrnIntrwDtls.setPhnScrnTime(phcrnResOverReslist.get(0).getPhnScrnTime());

								// call for the minimum status desc
								//Not sure why this is in the code.  ynStatus has nothing to do with Interview Status.  Removed by MTS1876
								//05/18/2011 while implementing Status Code Caching
								/*if (phnScrnIntrwDtls.getYnstatus() != null && !phnScrnIntrwDtls.getYnstatus().trim().equals(EMPTY_STRING)) {
									statusTo = phnScrnDAO.readNlsInterviewRespondStatus(Short.parseShort(phnScrnIntrwDtls.getYnstatus()));
									if (statusTo != null) {
										if (statusTo.getStatusDesc() != null) {
											phnScrnIntrwDtls.setYnStatusDesc(statusTo.getStatusDesc().trim());
										} else {
											phnScrnIntrwDtls.setYnStatusDesc(EMPTY_STRING);
										}
									}
								}*/
							}
							// to get the note text for Response - notetypecode
							// is 2
							// -Detail response Text History
							phnScrnResTxtlist = null;
							phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(Integer.parseInt(phnScrnTo.getItiNbr()),
									PHN_SCRN_RESPONSE_NOTE_TYP_CD);
							if (phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0) {
								phnScrnIntrwDtls.setDetailTxt(phnScrnResTxtlist.get(0).getDetailTxt());
							}
							// to fetch Candidate offer status
							reqCandList = candDao.getRequisitionDetailsForCandidate(phnScrnIntrwDtls.getCndtNbr());
							if (reqCandList != null && reqCandList.size() > 0) {
								RequisitionDetailTO CandDetails = (RequisitionDetailTO) reqCandList.get(0);
								phnScrnIntrwDtls.setOffered(CandDetails.getOfferMade());

								PropertyReader propertyReader = PropertyReader.getInstance();

								if (CandDetails.getOfferStat().equals(ACCEPTED_STATUS)) {
									phnScrnIntrwDtls.setOfferStatus(propertyReader.getProperty(ACCEPTED_STATUS));
								}

								else if (CandDetails.getOfferStat().equals(DECLINED_STATUS)) {
									phnScrnIntrwDtls.setOfferStatus(propertyReader.getProperty(DECLINED_STATUS));
								}

								else
									phnScrnIntrwDtls.setOfferStatus(CandDetails.getOfferStat());
							}

							totalPhnScrnlistInactive.add(phnScrnIntrwDtls);
						}
						/*
						 * itiDetRes.setITIDtlList(phnScrIntrTo);
						 * res.setItiDtRes(itiDetRes);
						 */
					}
				}

				itiInactRes.setITIDtlList(totalPhnScrnlistInactive);
				itiRes.setITIDtlList(totalPhnScrnlist);
				reqRes.setReqDtlList(reqList);
				res.setStatus(SUCCESS_APP_STATUS);
				res.setItiDtResInact(itiInactRes);
				res.setItiDtRes(itiRes);
				res.setReqDtlList(reqRes);

			}
		}

		catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRevCmplPhnScrnDetails method in Manager with output as" + res);
		return res;
	}

	/**
	 * This method is used to update the requisition details based on the of
	 * review completed phone screens
	 * 
	 * @param UpdateReviewPhnScrnRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response updateRevCmplPhnScrnDetails(final UpdateReviewPhnScrnRequest updateReviewPhnScrn) throws RetailStaffingException {
		logger.info(this + "Enters updateRevCmplPhnScrnDetails method in Manager with input as" + updateReviewPhnScrn);
		Response res = null;
		RetailStaffingRequisitionDAO reqDao = new RetailStaffingRequisitionDAO();
		RetailStaffingITIDAO retStafDao = new RetailStaffingITIDAO();

		try 
		{
			if (updateReviewPhnScrn != null) 
			{
				reqDao.updateRequisitionStatus(updateReviewPhnScrn.getReqDetailsTO().getReqNbr(), Short.parseShort(updateReviewPhnScrn
						.getReqDetailsTO().getRequisitionStatusCode()));
				final int updateReviewPhnScrnSize = updateReviewPhnScrn.getPhnScrnIntrwDetTOs().size();

				// New MTS1876 10/20/10
				for (int i = 0; i < updateReviewPhnScrnSize; i++) 
				{
					ITIUpdateRequest itiUpdate = new ITIUpdateRequest();
					itiUpdate.setPhnScrnDtlTo((PhoneScreenIntrwDetailsTO) updateReviewPhnScrn.getPhnScrnIntrwDetTOs().get(i));
					// Calling the Update All Statuses Code
					retStafDao.updateAllStatuses(itiUpdate);
				}
			}

			if (updateReviewPhnScrn != null) 
			{
				res = getRevCmplPhnScrnDetails(updateReviewPhnScrn.getReqDetailsTO().getReqNbr());
				res.setSuccessMsg(REVIEW_COMPLETED_PHONE_SCREENS);
			}

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.UPDATING_REQUISITION_STAFFING_DETAILS_ERROR_CODE,
					RetailStaffingConstants.UPDATING_REVIEW_PHONE_SCREEN_DETAILS_ERROR_MSG + "requisition number: "
							+ updateReviewPhnScrn.getReqDetailsTO().getReqNbr(), e);
		}
		logger.info(this + "Leaves updateRevCmplPhnScrnDetails method in Manager with output" + res);
		return res;
	}

	/**
	 * This method is used to update the staffing details of a requisition.
	 * 
	 * @param updStfReq
	 *            - Contains details of staffing details to be updated
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response updateStaffingDetails(final UpdateStaffingRequest updStfReq) throws RetailStaffingException {
		logger.info(this + "Enters updateStaffingDetails method in Manager with input" + updStfReq);
		Response res = null;
		try {
			res = new Response();
			if (updStfReq != null) 
			{
				QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
				DAOConnection daoConn = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
				QueryHandler queryHandler = new RequisitionServiceHandler();
				ConnectionHandler connHandler = new TransactionConnectionHandler(queryHandler, daoConn) 
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException {
						logger.info(this + "before getting connection");
						// getting DAO Connection
						DAOConnection daoConn = daoConnList.get(HRSTAFFING_DAO_CONTRACT);
						Query query = daoConn.getQuery();
						RetailStaffingRequisitionDAO dlrnReqDAO = new RetailStaffingRequisitionDAO();
						MapStream updateInsertStaffingDtls = null;
						MapStream updateEventInfo = null;
						MapStream insertEventReqnInfo = null;
						MapStream updateEventReqnInfo = null;
						MapStream deleteEventReqnInfo = null;
						MapStream qualifiedPool = null;
						MapStream referals = null;
						MapStream insertEventReq = null;
						MapStream updatePhoneScreenCalIdByReqNumber = null;
						StaffingDetailsTO stfDtlsTO = null;
						// Update scenario
						// calling the DAO to update THD Requisition Table
						if (updStfReq.getInsertUpdate().equals(UPDATE)) {
							logger.info(this + "Enters into Update Scenario");
							logger.info(this + "Before updateThdStoreEmploymentRequisition");
							updateInsertStaffingDtls = new MapStream("updateThdStoreEmploymentRequisition");
							updateInsertStaffingDtls = dlrnReqDAO.updateInsertStaffingDetails(updateInsertStaffingDtls, updStfReq);
							query.updateObject(daoConn, queryHandler, updateInsertStaffingDtls);

							// calling the hiring event table update
							if (updStfReq.getStaffingDtlTo().getHrgEvntFlg().equals("Y")) {
								if (updStfReq.getStaffingDtlTo().getHireEvntType().equals("SDE")) {
									//For the ATS Hiring Event this will only be called if it is a Before-Go-Live Requisition
									if (logger.isDebugEnabled()) {
										logger.debug(String.format("Updating Hiring Event Table for hireEventId:%1$s",updStfReq.getStaffingDtlTo().getHiringEventID()));
									}
									updateEventInfo = new MapStream("updateHumanResourcesHireEvent");
									updateEventInfo = dlrnReqDAO.updateHireEventInfo(updateEventInfo, updStfReq, UPDATE);
									query.updateObject(daoConn, queryHandler, updateEventInfo);
								}

								//Added for ATS Hiring Events.  Need to update HR_HIRE_EVNT_REQN 
								if (!updStfReq.getStaffingDtlTo().getHireEvntType().equals("SDE")) {
									//This was a Hiring Event and is Still a Hiring Event
									if (updStfReq.getStaffingDtlTo().getInsertDeleteUpdateHrHireEvntReqnTable().equals("UPDATE")) {
										if (logger.isDebugEnabled()) {
											logger.debug(String.format("Updating Hiring Event Reqn Table for hireEventId:%1$s",updStfReq.getStaffingDtlTo().getHiringEventID()));
										}
										updateEventReqnInfo = new MapStream("updateHumanResourcesHireEventRequisition");
										updateEventReqnInfo = dlrnReqDAO.updateHireEventReqnInfo(updateEventReqnInfo, updStfReq, UPDATE);
										query.updateObject(daoConn, queryHandler, updateEventReqnInfo);
									} else if (updStfReq.getStaffingDtlTo().getInsertDeleteUpdateHrHireEvntReqnTable().equals("INSERT")) {
										//This was not a Hiring Event and changed to Hiring Event
										if (logger.isDebugEnabled()) {
											logger.debug(String.format("Inserting Hiring Event Reqn Table for hireEventId:%1$s",updStfReq.getStaffingDtlTo().getHiringEventID()));
										}										
										insertEventReqnInfo = new MapStream("createHumanResourcesHireEventRequisition");
										insertEventReqnInfo = dlrnReqDAO.createHumanResourcesHireEventRequisition(insertEventReqnInfo, updStfReq, Integer.parseInt(updStfReq.getStaffingDtlTo().getHiringEventID()));
										query.insertObject(daoConn, queryHandler, insertEventReqnInfo);										
									}
								}
							} else {
								if (logger.isDebugEnabled()) {
									logger.debug(String.format("This is not a Hiring Event"));
								}
								//This was a ATS Hiring Event and changed to a Normal Calendar Req
								if (updStfReq.getStaffingDtlTo().getInsertDeleteUpdateHrHireEvntReqnTable().equals("DELETE")) {
									//Delete the requisition record from HR_HIRE_EVNT_REQN
									if (logger.isDebugEnabled()) {
										logger.debug(String.format("Delete record from HR_HIRE_EVNT_REQN for Requisition:%1$s",updStfReq.getReqNbr()));
									}	
									deleteEventReqnInfo = new MapStream("deleteHumanResourcesHireEventRequisition");
									deleteEventReqnInfo = dlrnReqDAO.deleteHireEventReqnInfo(deleteEventReqnInfo, updStfReq, UPDATE);
									query.deleteObject(daoConn, queryHandler, deleteEventReqnInfo);
								}
							}
							
							// calling the employment requisition note table
							// update for requisition note text
							// before calling it, need to call the read of the
							// same table to check whether
							// data is there or not
							// getting the qualified pool note
							// if(updStfReq.getStaffingDtlTo().getQualPoolNts()!=null
							// &&
							// !updStfReq.getStaffingDtlTo().getQualPoolNts().trim().equals(EMPTY_STRING))
							// {
							logger.info(this + "Before readEmploymentRequisitionNote");
							stfDtlsTO = dlrnReqDAO.readEmploymentRequisitionNote(Integer.parseInt(updStfReq.getReqNbr()), QUALIFIED_POOL_NOTE_TYP_CD);
							// getting the qualified pool note
							if (stfDtlsTO != null && stfDtlsTO.getQualPoolNts() != null) {
								logger.info(this + "Before updateEmploymentRequisitionNote");
								qualifiedPool = new MapStream("updateEmploymentRequisitionNote");
								qualifiedPool = dlrnReqDAO.updateEmploymentRequisitionNote(qualifiedPool, updStfReq, QUALIFIED_POOL_NOTE_TYP_CD);
								query.updateObject(daoConn, queryHandler, qualifiedPool);
							} else {
								logger.info(this + "Before createEmploymentRequisitionNote");
								qualifiedPool = new MapStream("createEmploymentRequisitionNote");
								qualifiedPool = dlrnReqDAO.createEmploymentRequisitionNote(qualifiedPool, updStfReq, QUALIFIED_POOL_NOTE_TYP_CD);
								query.insertObject(daoConn, queryHandler, qualifiedPool);
							}
							// }

							// getting the referral pool note
							logger.info(this + "Before readEmploymentRequisitionNote");
							stfDtlsTO = dlrnReqDAO.readEmploymentRequisitionNote(Integer.parseInt(updStfReq.getReqNbr()), REFERRALS_NOTE_TYP_CD);
							// getting the referals pool note
							if (stfDtlsTO != null && stfDtlsTO.getReferals() != null) {
								logger.info(this + "Before updateEmploymentRequisitionNote");
								referals = new MapStream("updateEmploymentRequisitionNote");
								referals = dlrnReqDAO.updateEmploymentRequisitionNote(referals, updStfReq, REFERRALS_NOTE_TYP_CD);
								query.updateObject(daoConn, queryHandler, referals);
							} else {
								logger.info(this + "Before createEmploymentRequisitionNote");
								referals = new MapStream("createEmploymentRequisitionNote");
								referals = dlrnReqDAO.createEmploymentRequisitionNote(referals, updStfReq, REFERRALS_NOTE_TYP_CD);
								query.insertObject(daoConn, queryHandler, referals);
							}
							// }
							
							try {
								//Update the reqCalId for all attached Phone Screens
								logger.info(this + " Before updateHrPhoneScreenCalendarIdsByReqNumber");
								updatePhoneScreenCalIdByReqNumber = new MapStream("updateHrPhoneScreenByRequestNumber");
								updatePhoneScreenCalIdByReqNumber = PhoneScreenDAO.updateHrPhoneScreenCalendarIdsByReqNumber(updatePhoneScreenCalIdByReqNumber,updStfReq);
								query.updateObject(daoConn, queryHandler, updatePhoneScreenCalIdByReqNumber);								
							} catch (UpdateException e) {
								if (e.getMessage().equals("No Rows Found")) {
									//Do Nothing, there were no Phone screens to update
								} else {
									throw e;
								}
							}
						}
						// else insert scenario, Which is an AIMS Created Requisition
						else if (updStfReq.getInsertUpdate().equals(INSERT)) {
							logger.info(this + "Enters into Insert Scenario");
							logger.info(this + "Before insertThdStoreEmploymentRequisition");
							updateInsertStaffingDtls = new MapStream("createThdStoreEmploymentRequisition");
							updateInsertStaffingDtls = dlrnReqDAO.updateInsertStaffingDetails(updateInsertStaffingDtls, updStfReq);
							query.insertObject(daoConn, queryHandler, updateInsertStaffingDtls);

							//Removed for ATS Hiring Events************
							// calling the hiring event table update
							/*logger.info(this + "Before createHumanResourcesHireEvent");
							updateEventInfo = new MapStream("createHumanResourcesHireEvent");
							updateEventInfo = dlrnReqDAO.updateHireEventInfo(updateEventInfo, updStfReq, INSERT);
							query.insertObject(daoConn, queryHandler, updateEventInfo);*/
							//Removed for ATS Hiring Events************							

							// call the insert of
							if (updStfReq.getStaffingDtlTo().getHrgEvntFlg().equals("Y")) {
								if (logger.isDebugEnabled()) {
									logger.debug(String.format("Inserting Hiring Event Req Table for hireEventId:%1$s",updStfReq.getStaffingDtlTo().getHiringEventID()));
								}								
								logger.info(this + "Before createHumanResourcesHireEventRequisition");
								insertEventReq = new MapStream("createHumanResourcesHireEventRequisition");
								insertEventReq = dlrnReqDAO.createHumanResourcesHireEventRequisition(insertEventReq, updStfReq, Integer.parseInt(updStfReq.getStaffingDtlTo().getHiringEventID()));
								query.insertObject(daoConn, queryHandler, insertEventReq);
							}
							
							if (updStfReq.getStaffingDtlTo().getReferals() != null
									&& !updStfReq.getStaffingDtlTo().getReferals().trim().equals(EMPTY_STRING)) {
								logger.info(this + "Before createEmploymentRequisitionNote");
								referals = new MapStream("createEmploymentRequisitionNote");
								referals = dlrnReqDAO.createEmploymentRequisitionNote(referals, updStfReq, REFERRALS_NOTE_TYP_CD);
								query.insertObject(daoConn, queryHandler, referals);
							}
							if (updStfReq.getStaffingDtlTo().getQualPoolNts() != null
									&& !updStfReq.getStaffingDtlTo().getQualPoolNts().trim().equals(EMPTY_STRING)) {
								logger.info(this + "Before createEmploymentRequisitionNote");
								qualifiedPool = new MapStream("createEmploymentRequisitionNote");
								qualifiedPool = dlrnReqDAO.createEmploymentRequisitionNote(qualifiedPool, updStfReq, QUALIFIED_POOL_NOTE_TYP_CD);
								query.insertObject(daoConn, queryHandler, qualifiedPool);
							}
						}
					}
				};
				connHandler.execute();
				res.setStatus(SUCCESS_APP_STATUS);
			} else {
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.UPDATING_REQUISITION_STAFFING_DETAILS_ERROR_CODE,
					RetailStaffingConstants.UPDATING_REQUISITION_STAFFING_DETAILS_ERROR_MSG + "requisition number: " + updStfReq.getReqNbr(), e);
		}
		logger.info(this + "Leaves updateStaffingDetails method in Manager with output" + res);
		return res;
	}

	/**
	 * This method is used to Submit Requisition Request details. This method is
	 * synchronized so that multiple users cannot create Requisitions at the
	 * same time, which could cause duplicate numbers.
	 * 
	 * @param submitReqReq
	 *            - Contains details of Requested Requisition
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public synchronized Response submitRequisitionRequestDetails(final UpdateStaffingRequest updStfReq) throws RetailStaffingException {
		logger.debug("Entering submitRequisitionRequestDetails()");

		if (updStfReq == null) {
			logger.error("Null update staffing requires received");
			throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
		} // end if(updStfReq == null)

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Request type: %1$s, requisition number: %2$s, calendar id: %3$d", updStfReq.getInsertUpdate(), updStfReq
					.getReqNbr(), updStfReq.getReqCalId()));
		} // end if

		Response response = new Response();
		StringBuilder respData = new StringBuilder(128);

		int insertCount = 0;
		ConnectionHandler connHandler = null;
		QueryHandler queryHandler = null;
		
		final RetailStaffingRequisitionDAO submitReqDAO = new RetailStaffingRequisitionDAO();

		try {
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);
			
			queryHandler = new RequisitionServiceHandler();
			connHandler = new UniversalConnectionHandler(true, null, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{				
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException {
					DAOConnection workforceRecruitmentDaoConn = daoConnList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = daoConnList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();

					MapStream getNextRequisitionNbr = null;
					MapStream insertHRRequisitionRequestDtls = null;
					MapStream insertTHDRequisitionRequestDtls = null;
					MapStream insertLanguageDetails = null;
					MapStream qualifiedPool = null;
					MapStream referals = null;
					MapStream insertEventReq = null;
					int hireEventId = 0;

					// Need to get the next Requisition Number...
					if (logger.isDebugEnabled()) {
						logger.debug("Getting next requisition number...");
					} // end if
					getNextRequisitionNbr = new MapStream("readHumanResourcesStoreEmploymentRequisitionForMaximumEmploymentRequisitionNumber");
					RequisitionDetailTO nextRequisitionNbr = submitReqDAO.getNextRequisitionNbr(hrHrStaffingDaoConn, hrHrStaffingQuery, getNextRequisitionNbr, updStfReq);
					updStfReq.setReqNbr(nextRequisitionNbr.getReqNbr());
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Next requisition number: %1$s", updStfReq.getReqNbr()));
					} // end if

					if (logger.isDebugEnabled()) {
						logger.debug(String.format("Selected calendar: %1$d", updStfReq.getReqCalId()));
						logger.debug("Inserting HR_STR_EMPLT_REQN record");
					} // end if

					// Insert record into HR_STR_EMPLT_REQN
					insertHRRequisitionRequestDtls = new MapStream("createHumanResourcesStoreEmploymentRequisition");
					insertHRRequisitionRequestDtls = submitReqDAO.insertStaffingDetails(insertHRRequisitionRequestDtls, updStfReq);
					workforceRecruitmentQuery.insertObject(workforceRecruitmentDaoConn, queryHandler, insertHRRequisitionRequestDtls);

					// Insert record into THD_STR_EMPLT_REQN
					logger.debug("Inserting THD_STR_EMPLT_REQN record");
					insertTHDRequisitionRequestDtls = new MapStream("createThdStoreEmploymentRequisition");
					//Added for Auto-Attach
					//When the Requisition is Auto-Attach and External Only save the value 19999999, No Queweb Ticket will be created....
					if (updStfReq.getStaffingDtlTo().isAutoAttachReqn()) {
						updStfReq.getStaffingDtlTo().setRequestNbr("19999999");
					}
					insertTHDRequisitionRequestDtls = submitReqDAO.updateInsertStaffingDetails(insertTHDRequisitionRequestDtls, updStfReq);
					hrHrStaffingQuery.insertObject(hrHrStaffingDaoConn, queryHandler, insertTHDRequisitionRequestDtls);

					// Insert records into table HR_EMPLT_REQN_LANG
					// Language Grid
					if ( !Util.isNullString(updStfReq.getStaffingDtlTo().getLanguageSkls())
							 && !updStfReq.getStaffingDtlTo().getLanguageSkls().equalsIgnoreCase("NULL")) {
						String[] languageSkills = updStfReq.getStaffingDtlTo().getLanguageSkls().split(",");
						for (int t=0; t < languageSkills.length; t++) { 
							insertLanguageDetails = new MapStream("createHumanResourcesEmploymentRequisitionLanguage");
							insertLanguageDetails = submitReqDAO.insertLangauageDetails(insertLanguageDetails, Integer.parseInt(updStfReq.getReqNbr()), Short.parseShort(languageSkills[t].toString().trim()));
							workforceRecruitmentQuery.insertObject(workforceRecruitmentDaoConn, queryHandler, insertLanguageDetails);
						}	
					}
					
					// Insert into REQN_DLY_AVAIL.. This comes from the
					// Schedule Preference Grid
					logger.debug("Inserting REQN_DLY_AVAIL records");
					if (updStfReq.getStaffingDtlTo().getSchdPrefDetailsList() != null
							&& updStfReq.getStaffingDtlTo().getSchdPrefDetailsList().size() > 0) {
						for (int i = 0; i < updStfReq.getStaffingDtlTo().getSchdPrefDetailsList().size(); i++) {
							SchedulePreferenceTO tempTO = updStfReq.getStaffingDtlTo().getSchdPrefDetailsList().get(i);
							submitReqDAO.insertSchedulePref(hrHrStaffingDaoConn, hrHrStaffingQuery, updStfReq.getReqNbr(), tempTO.getWkDayNbr(), tempTO.getDaySegCd());
						} // end for(int i = 0; i <
							// updStfReq.getStaffingDtlTo().getSchdPrefDetailsList().size();
							// i++)
					} // end

					// Insert into EMPLT_REQN_NOTE
					if (updStfReq.getStaffingDtlTo().getReferals() != null && updStfReq.getStaffingDtlTo().getReferals().trim().length() > 0) {
						logger.debug("Inserting referral EMPLT_REQN_NOTE records");
						referals = new MapStream("createEmploymentRequisitionNote");
						referals = submitReqDAO.createEmploymentRequisitionNote(referals, updStfReq, REFERRALS_NOTE_TYP_CD);
						hrHrStaffingQuery.insertObject(hrHrStaffingDaoConn, queryHandler, referals);
					} // end if(updStfReq.getStaffingDtlTo().getReferals() !=

					if (updStfReq.getStaffingDtlTo().getQualPoolNts() != null && updStfReq.getStaffingDtlTo().getQualPoolNts().trim().length() > 0) {
						logger.debug("Inserting qualified pool note EMPLT_REQN_NOTE records");
						qualifiedPool = new MapStream("createEmploymentRequisitionNote");
						qualifiedPool = submitReqDAO.createEmploymentRequisitionNote(qualifiedPool, updStfReq, QUALIFIED_POOL_NOTE_TYP_CD);
						hrHrStaffingQuery.insertObject(hrHrStaffingDaoConn, queryHandler, qualifiedPool);
					} // end if(updStfReq.getStaffingDtlTo().getQualPoolNts() !=

					// **** Removed for ATS Hiring Events *****
					// Insert into HR_HIRE_EVNT
					/*logger.debug("Inserting HR_HIRE_EVNT records");
					insertEventInfo = new MapStream("createHumanResourcesHireEvent");
					hireEventId = submitReqDAO.createHumanResourcesHireEvent(daoConn, query, insertEventInfo, updStfReq);*/
					
					if (updStfReq.getStaffingDtlTo().getHrgEvntFlg().equals("Y")) {
						// Insert into HR_HIRE_EVNT_REQN
						logger.debug("Inserting HR_HIRE_EVNT_REQN records");
						hireEventId = Integer.parseInt(updStfReq.getStaffingDtlTo().getHiringEventID());
						insertEventReq = new MapStream("createHumanResourcesHireEventRequisition");
						insertEventReq = submitReqDAO.createHumanResourcesHireEventRequisition(insertEventReq, updStfReq, hireEventId);
						hrHrStaffingQuery.insertObject(hrHrStaffingDaoConn, queryHandler, insertEventReq);
					} // end if (updStfReq.getStaffingDtlTo().getHrgEvntFlg().equals("Y"))
				
				} // end function handleQuery()
			}; // end connection handler
		} // end try
		catch (QueryException qe) {
			logger.error("Failed to create new requisition, an exception occurred initializing database objects", qe);
			throw new RetailStaffingException(APP_FATAL_ERROR_CODE, SUBMIT_REQUISITION_REQUEST_DETAILS_ERROR_CODE_MSG, qe);
		} // end catch

		// loop through and try to insert the record up to 5 times
		while (insertCount < 5) {
			// increment the insertCount first (so we don't forget)
			insertCount++;
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Attempting insert %1$d", insertCount));
			} // end if

			try {
				// execute the inserts necessary to create a req
				connHandler.execute();
				// break the loop
				break;
			} // end try
			catch (QueryException qe) {
				logger.error(String.format("An exception occurred during insert attempt %1$d", insertCount), qe);
			} // end catch
		} // end while(insertCount < 5)

		// if the insert count is < 5 then the insert worked
		//No longer need to send all the Job related parameters due to the redesign of the Availability Form. 
		if (insertCount < 5) {
			response.setStatus(SUCCESS_APP_STATUS);
			respData.append(updStfReq.getReqNbr()).append("|");
			respData.append(updStfReq.getStaffingDtlTo().getStrNo()).append("|");
			respData.append(updStfReq.getReqCalId());

			response.setSuccessMsg(respData.toString());
			
			//When RSC To Manage is Yes, create a Queweb Ticket email
			if (updStfReq.getStaffingDtlTo().getRscManageFlg().equals("Y")) {
				//Added for Auto-Attach
				if (updStfReq.getStaffingDtlTo().isAutoAttachReqn()) {
					//Do not sent a Queweb Ticket for Auto-Attach and External Only
					logger.debug("Do Not Send email to Queweb, Auto-Attach and External Only");
				} else {
					logger.debug("Send email to Queweb.");
					createQuewebEmail(updStfReq);
				}
			}
			
		} // end if(insertCount < 5)
		else {
			logger.error("Failed to create new requisition, maximum number of attempts exceeded");
			throw new RetailStaffingException(APP_FATAL_ERROR_CODE, SUBMIT_REQUISITION_REQUEST_DETAILS_ERROR_CODE_MSG);
		} // end else

		logger.debug("Exiting submitRequisitionRequestDetails()");
		return response;
	} // end function submitRequisitionRequestDetails()

	/**
	 * This method is used to return the requisitions by store
	 * 
	 * @param SummaryRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getRequisitionsByStore(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByStore method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			
			// Check for the existence of the store first.
			if(!LocationManager.isValidStoreNumber(summReq.getOrgID()))
			{
				logger.error(this + "The entered store is invalid");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "store number: "
						+ summReq.getOrgID());
			}
			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.getRequisitionsByStore(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRequisitionsByStore method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the requisitions by region
	 * 
	 * @param SummaryRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getRequisitionsByReg(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByReg method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.getRequisitionsByReg(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRequisitionsByReg method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the requisitions by district
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getRequisitionsByDist(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByDist method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.getRequisitionsByDist(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRequisitionsByDist method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the requisitions by division
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getRequisitionsByDiv(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByDiv method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.getRequisitionsByDiv(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRequisitionsByDiv method in Manager with output as" + res);
		return res;

	}

	/**
	 * 
	 * This class handles the queries.
	 * 
	 */
	protected class RequisitionServiceHandler extends AnnotatedQueryHandler {
		@QuerySelector(name = "createHumanResourcesEmploymentRequisitionLanguage", operation = QueryMethod.insertObject)
		public void createHumanResourcesEmploymentRequisitionLanguage(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException {

		}

		@QuerySelector(name = "createEmploymentRequisitionNote", operation = QueryMethod.insertObject)
		public void createEmploymentRequisitionNote(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException {

		}

		@QuerySelector(name = "updateEmploymentRequisitionNote", operation = QueryMethod.updateObject)
		public void updateEmploymentRequisitionNote(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}

		@QuerySelector(name = "updateHumanResourcesHireEvent", operation = QueryMethod.updateObject)
		public void updateHumanResourcesHireEvent(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}

		@QuerySelector(name = "updateThdStoreEmploymentRequisition", operation = QueryMethod.updateObject)
		public void updateThdStoreEmploymentRequisition(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}

		@QuerySelector(name = "updateCustomerUCM", operation = QueryMethod.updateObject)
		public void updateUCMID(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}

		@QuerySelector(name = "createThdStoreEmploymentRequisition", operation = QueryMethod.insertObject)
		public void createThdStoreEmploymentRequisition(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}

		@QuerySelector(name = "createHumanResourcesStoreEmploymentRequisition", operation = QueryMethod.insertObject)
		public void createHumanResourcesStoreEmploymentRequisition(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}

		@QuerySelector(name = "createHumanResourcesHireEvent", operation = QueryMethod.insertObject)
		public void createHumanResourcesHireEvent(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException {
			NVStream properties = ((MapStream) arg0).getNVStream();
			Map<?, ?> hs = (HashMap<?, ?>) properties.getObject("primaryKeys");

			if (null != hs.get("hireEventId").toString()) {
				eventId = Integer.parseInt(hs.get("hireEventId").toString());
			}
		}

		@QuerySelector(name = "createHumanResourcesHireEventRequisition", operation = QueryMethod.insertObject)
		public void createHumanResourcesHireEventRequisition(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}

		@QuerySelector(name = "createHumanResourcesRequisitionSchedule", operation = QueryMethod.insertObject)
		public void createHumanResourcesRequisitionSchedule(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}
		
		@QuerySelector(name = "updateHrPhoneScreenByRequestNumber", operation = QueryMethod.updateObject)
		public void updateHrPhoneScreenByRequestNumber(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}		
		
		@QuerySelector(name = "updateHumanResourcesHireEventRequisition", operation = QueryMethod.updateObject)
		public void updateHumanResourcesHireEventRequisition(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}
		
		@QuerySelector(name = "deleteHumanResourcesHireEventRequisition", operation = QueryMethod.deleteObject)
		public void deleteHumanResourcesHireEventRequisition(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) {

		}		

	}

	/**
	 * This method is used to return the inactive requisitions by store
	 * 
	 * @param SummaryRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	// @SuppressWarnings("unchecked")
	public Response getInactiveRequisitionsByStore(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getRequisitionsByStore method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;

		try {
			res = new Response();
			// Check for the existence of the store first.
			if(!LocationManager.isValidStoreNumber(summReq.getOrgID()))
			{
				logger.error(this + "The entered store is invalid");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "store number: "
						+ summReq.getOrgID());
			}
			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.readInactiveRequisitionsByStore(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}

			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getInactiveRequisitionsByStore method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the inactive requisitions by store
	 * 
	 * @param SummaryRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getInactiveRequisitionsByDist(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getInactiveRequisitionsByDist method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;

		try {
			res = new Response();

			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.readInactiveRequisitionsByDistrict(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}

			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getInactiveRequisitionsByDist method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the inactive requisitions by store
	 * 
	 * @param SummaryRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getInactiveRequisitionsByReg(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getInactiveRequisitionsByReg method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;

		try {
			res = new Response();

			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.readInactiveRequisitionsByRegion(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}

			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getInactiveRequisitionsByReg method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the inactive requisitions by store
	 * 
	 * @param SummaryRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getInactiveRequisitionsByDiv(SummaryRequest summReq) throws RetailStaffingException {
		logger.info(this + "Enters getInactiveRequisitionsByDiv method in Manager ");
		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		RetailStaffingRequisitionDAO RetailStaffingRequisitionDAO = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;

		try {
			res = new Response();

			RetailStaffingRequisitionDAO = new RetailStaffingRequisitionDAO();
			reqDetMap = RetailStaffingRequisitionDAO.readInactiveRequisitionsByDiv(summReq);
			reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);
			if (reqList == null || reqList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}

			reqDtRes = new RequisitionDetailResponse();
			reqDtRes.setReqDtlList(reqList);
			res.setReqDtlList(reqDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("CRT_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getInactiveRequisitionsByDiv method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to get the requisition request details needed to load
	 * the form.
	 * 
	 * @param
	 * @return
	 * @throws RetailStaffingException
	 */

	public Response loadRequisitionRequestDetails() throws RetailStaffingException {

		logger.info(this + " Enters loadRequisitionRequestDetails method in Manager...");

		Response res = null;

		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		ExpLvlResponse expLvlres = new ExpLvlResponse();
		LangSklsResponse langSklsres = new LangSklsResponse();
		StateDetailResponse stDetailRes = new StateDetailResponse();
		AutoAttachJobTitleResponse aaJobTtlRes = new AutoAttachJobTitleResponse();
		List<ExperienceLevelTO> oExpLvlRes = null;
		List<LanguageSkillsTO> oLangSklsRes = null;
		List<StateDetailsTO> states = null;
		List<AutoAttachJobTitlesTO> aaJobTitleList = null;
		UserDataTO userDataTo = new UserDataTO();
		
		try {
			res = new Response();

			// to fetch language skills
			oLangSklsRes = delReqMgr.readLanguageSkillsList();
			if (oLangSklsRes != null && oLangSklsRes.size() > 0) {
				langSklsres.setLangSklsList(oLangSklsRes);
				res.setLangSklsRes(langSklsres);
			}

			// to fetch desired experience level
			oExpLvlRes = delReqMgr.readNlsExperienceLevelList();
			if (oExpLvlRes != null && oExpLvlRes.size() > 0) {
				expLvlres.setExpLvlList(oExpLvlRes);
				res.setExpLvlRes(expLvlres);
			}
			
			// Get States for Drop Down
			states = delReqMgr.readStateList();
			if (states != null && states.size() > 0) {
				stDetailRes.setStrDtlList(states);
				res.setStateDtRes(stDetailRes);
			}
			
			//Get Job Titles that are Auto-Attach
			aaJobTitleList = delReqMgr.readAutoAttachJobTitles();
			if (aaJobTitleList != null && aaJobTitleList.size() > 0) {
				aaJobTtlRes.setAaJobTtlList(aaJobTitleList);
				res.setAutoAttachJobTitleRes(aaJobTtlRes);
			}
			
			//Get logged in User information
			userDataTo = UserManager.getUserData();
			res.setUserData(userDataTo);

		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + " Leaves loadRequisitionRequestDetails method in Manager with output..." + res);
		return res;
	}

	public Response getDeptByStore(String strNo) throws RetailStaffingException {

		logger.info(this + " Enters getDeptByStore method in Manager ");
		Response res = null;
		DeptResponse deptRes = new DeptResponse();
		LocationDetailsTO locInfo = new LocationDetailsTO();
		List<DeptDetailsTO> oDeptRes = null;

		try {
			res = new Response();
			// Check for the existence of the location first and see if it is a RSC Supported location.
			locInfo = LocationManager.getLocationDetailsAndIsRscSupported(strNo);
			// Put the Location Information on the Response
			res.setLocationDetails(locInfo);
			
			// Fetch the Departments By Store
			RetailStaffingDAO retailStaffingDAO = new RetailStaffingDAO();
			oDeptRes = retailStaffingDAO.getDeptsByStr(strNo);
			if (oDeptRes != null && oDeptRes.size() > 0) {
				deptRes.setDeptList(oDeptRes);
				res.setDeptRes(deptRes);
			}

		} catch (NoRowsFoundException e) {
			throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "location number: "
					+ strNo);
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getDeptByStore method in Manager with output as" + res);
		return res;

	}

	public Response getJobTtlByDeptByStore(JobTitleRequest reqTO) throws RetailStaffingException {

		logger.info(this + " Enters getJobTtlByDeptByStore method in Manager ");
		Response res = null;
		JobTtlResponse jobTtlRes = new JobTtlResponse();
		List<JobTtlDetailsTO> oJobTtlRes = null;

		try {
			res = new Response();
			// Fetch the Job Titles by Department By Store
			RetailStaffingDAO retailStaffingDAO = new RetailStaffingDAO();
			oJobTtlRes = retailStaffingDAO.getJobTtlsByDeptByStr(reqTO.getStrNo(), reqTO.getDeptNo());
			if (oJobTtlRes != null && oJobTtlRes.size() > 0) {
				jobTtlRes.setJobTtlList(oJobTtlRes);
				res.setJobTtlRes(jobTtlRes);
			}

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getJobTtlByDeptByStore method in Manager with output as " + res);
		return res;

	}

	public void createQuewebEmail(final UpdateStaffingRequest staffingUpdate) {
		String toAddresses = null;
		String fromAddress = null;
		StringBuffer subject = new StringBuffer(100);
		StringBuffer textBody = new StringBuffer(1000);
		StringBuffer jobStatus = new StringBuffer(50);
		String tempPhnNbr = "";
		List<SchedulePreferenceTO> schdPrefList = null;

		try {
			String strLCEnv = com.homedepot.ta.aa.util.TAAAResourceManager.getProperty("host.LCP");
			logger.info("***Env Value for LCP is " + strLCEnv);
			// Determine which email address to send the Queweb message to.
			if ("AD".equals(strLCEnv)) {
				// Local Environment
				toAddresses = " testcloud2_queweb@homedepot.com";
				fromAddress = " testcloud1_queweb@homedepot.com";
			} else if ("QA".equals(strLCEnv)) {
				// QA Environment
				toAddresses = " testcloud2_queweb@homedepot.com";
				fromAddress = " testcloud1_queweb@homedepot.com";
			} else if ("QP".equals(strLCEnv)) {
				// QP Environment
				toAddresses = " testcloud2_queweb@homedepot.com";
				fromAddress = " testcloud1_queweb@homedepot.com";
			} else if ("TR".equals(strLCEnv)) {
				// QP Environment
				toAddresses = " testcloud2_queweb@homedepot.com";
				fromAddress = " testcloud1_queweb@homedepot.com";
			} else if ("ST".equals(strLCEnv)) {
				// ST Environment
				toAddresses = " testcloud2_queweb@homedepot.com";
				fromAddress = " testcloud1_queweb@homedepot.com";
			} else if ("PR".equals(strLCEnv)) {
				// Production Environment
				toAddresses = "MSSemail@homedepot.com";
				fromAddress = "myTHDHR@homedepot.com";
			} else {
				// Production Environment
				toAddresses = "MSSemail@homedepot.com";
				fromAddress = "myTHDHR@homedepot.com";
			}

			Profile profile = Profile.getCurrent();
			logger.info("***Java User Id = " + profile.getProperty(Profile.USER_ID));

			/*if (!StringUtils.trim(profile.getProperty(Profile.EMAIL)).equals("")) {
				fromAddress = profile.getProperty(Profile.EMAIL);
				logger.info("***From Set to Java Email = " + fromAddress);
			}*/
			logger.info("***Requisition Nbr = " + staffingUpdate.getReqNbr());
			// Requested Subject Line from John Martin Example =
			// A new job has been created Req [requisition number] D[three
			// digit department number] [Job Title] [Job Title Description]
			// X-MAILER: [LDAP of requestor]
			subject.append("A new job has been created Req ").append(staffingUpdate.getReqNbr());
			subject.append(" D").append(staffingUpdate.getStaffingDtlTo().getDeptNo());
			subject.append(" ").append(staffingUpdate.getStaffingDtlTo().getJobTtlCd());
			subject.append(" ").append(staffingUpdate.getStaffingDtlTo().getJobTitle());
			subject.append(" X-MAILER: ").append(profile.getProperty(Profile.USER_ID));

			textBody.append("\n");
			textBody.append("============================ Staffing Needs Web-Form =======================================\n\n");
			textBody.append("--------------------------- Requisition -------------------------------\n");
			textBody.append("Requisition Number: ").append(staffingUpdate.getReqNbr()).append("\n");
			textBody.append("Store # (MET - Home Store #): ").append(staffingUpdate.getStaffingDtlTo().getStrNo()).append("\n");
			textBody.append("Department #: ").append(staffingUpdate.getStaffingDtlTo().getDeptNo()).append("\n");
			textBody.append("Job Title: ").append(staffingUpdate.getStaffingDtlTo().getJobTtlCd()).append(" - ").append(
					staffingUpdate.getStaffingDtlTo().getJobTitle()).append("\n");
			textBody.append("Date Needed: ").append(staffingUpdate.getStaffingDtlTo().getFillDt()).append("\n");
			textBody.append("Number of Positions to be filled for this requisition: ").append(staffingUpdate.getStaffingDtlTo().getNumPositions())
					.append("\n");
			textBody.append("Requested number of Interviews for this job: ").append(staffingUpdate.getStaffingDtlTo().getRequestedNbrIntvws())
					.append("\n");
			if (staffingUpdate.getStaffingDtlTo().getJobStatusPt().equalsIgnoreCase("true")) {
				jobStatus.append("Part-Time");
			}
			if (staffingUpdate.getStaffingDtlTo().getJobStatusPt().equals("true")
					&& staffingUpdate.getStaffingDtlTo().getJobStatusFt().equals("true")) {
				jobStatus.append(", ");
			}
			if (staffingUpdate.getStaffingDtlTo().getJobStatusFt().equals("true")) {
				jobStatus.append("Full-Time");
			}
			textBody.append("Job Status: ").append(jobStatus.toString()).append("\n");
			if (staffingUpdate.getStaffingDtlTo().getSealTempJob().equalsIgnoreCase("N")) {
				textBody.append("Is this a seasonal/temporary Job? No\n\n");
			} else {
				textBody.append("Is this a seasonal/temporary Job? Yes\n\n");
			}
			textBody.append("------------------------ Requestor Information ------------------------\n");
			textBody.append("Requestor Name: ").append(staffingUpdate.getStaffingDtlTo().getRequestorName()).append("\n");
			textBody.append("Requestor Title: ").append(staffingUpdate.getStaffingDtlTo().getRequestorTitle()).append("\n");
			tempPhnNbr = String.format("(%s) %s-%s", staffingUpdate.getStaffingDtlTo().getRequestorPhnNbr().substring(0, 3), staffingUpdate
					.getStaffingDtlTo().getRequestorPhnNbr().substring(3, 6), staffingUpdate.getStaffingDtlTo().getRequestorPhnNbr().substring(6));
			textBody.append("Requestor Phone #: ").append(tempPhnNbr).append("\n");
			textBody.append("Submitted By User ID: ").append(profile.getProperty(Profile.USER_ID)).append("\n");
			if (staffingUpdate.getStaffingDtlTo().getHireByFlg().equalsIgnoreCase("N")) {
				textBody.append("Are requestor and hiring manager the same person? No\n\n");
			} else {
				textBody.append("Are requestor and hiring manager the same person? Yes\n\n");
			}

			if (staffingUpdate.getStaffingDtlTo().getHireByFlg().equalsIgnoreCase("N")) {
				textBody.append("---------------------- Hiring Manager Information ----------------------\n");
				textBody.append("Requestor Name: ").append(staffingUpdate.getStaffingDtlTo().getHrgMgrName()).append("\n");
				textBody.append("Requestor Title: ").append(staffingUpdate.getStaffingDtlTo().getHrgMgrTtl()).append("\n");
				tempPhnNbr = String.format("(%s) %s-%s", staffingUpdate.getStaffingDtlTo().getHrgMgrPhn().substring(0, 3), staffingUpdate
						.getStaffingDtlTo().getHrgMgrPhn().substring(3, 6), staffingUpdate.getStaffingDtlTo().getHrgMgrPhn().substring(6));
				textBody.append("Requestor Phone #: ").append(tempPhnNbr).append("\n");
				textBody.append("Submitted By User ID: ").append(staffingUpdate.getStaffingDtlTo().getHrgMgrId()).append("\n\n");
			}

			textBody.append("------------------ Interviewer Scheduling Preference ------------------\n");
			if (staffingUpdate.getStaffingDtlTo().getRscSchdFlg().equalsIgnoreCase("Y")) {
				textBody.append("RSC will schedule: Yes\n\n");
			} else {
				textBody.append("RSC will schedule: No\n\n");
			}

			textBody.append("---------------------------- Qualified Pool ---------------------------\n");
			textBody.append("Candidate Type: ").append(staffingUpdate.getStaffingDtlTo().getCandidateType()).append("\n");
			if (staffingUpdate.getStaffingDtlTo().getDesiredExpText() != null
					&& !staffingUpdate.getStaffingDtlTo().getDesiredExpText().equals("null")) {
				textBody.append("Required Experience: ").append(staffingUpdate.getStaffingDtlTo().getDesiredExpText()).append("\n");
			} else {
				textBody.append("Required Experience: ").append("\n");
			}
			if (staffingUpdate.getStaffingDtlTo().getLanguageSkls() != null && !staffingUpdate.getStaffingDtlTo().getLanguageSkls().equals("null")) {
				textBody.append("Language Skills Ability: ").append(staffingUpdate.getStaffingDtlTo().getLanguageSkls()).append("\n");
			} else {
				textBody.append("Language Skills Ability: ").append("\n");
			}
			textBody.append("Schedule Preference: ");
			if (staffingUpdate.getStaffingDtlTo().getSchdPrefDetailsList().size() > 0) {
				schdPrefList = staffingUpdate.getStaffingDtlTo().getSchdPrefDetailsList();
				int currentDaySeg = -1;
				int previousDaySeg = -2;
				for (int i = 0; i < schdPrefList.size(); i++) {
					currentDaySeg = Integer.parseInt(schdPrefList.get(i).getDaySegCd());
					switch (currentDaySeg) {
					case 0: // Anytime
						if (currentDaySeg != previousDaySeg) {
							textBody.append("\n          Anytime: ");
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						} else {
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						}
						break;
					case 1: // Early AM
						if (currentDaySeg != previousDaySeg) {
							textBody.append("\n          Early AM: ");
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						} else {
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						}
						break;
					case 2: // Mornings
						if (currentDaySeg != previousDaySeg) {
							textBody.append("\n          Mornings: ");
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						} else {
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						}
						break;
					case 3: // Afternoons
						if (currentDaySeg != previousDaySeg) {
							textBody.append("\n          Afternoons: ");
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						} else {
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						}
						break;
					case 4: // Evenings
						if (currentDaySeg != previousDaySeg) {
							textBody.append("\n          Evenings: ");
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						} else {
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						}
						break;
					case 5: // Late PM
						if (currentDaySeg != previousDaySeg) {
							textBody.append("\n          Late PM: ");
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						} else {
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						}
						break;
					case 6: // Overnight
						if (currentDaySeg != previousDaySeg) {
							textBody.append("\n          Overnight: ");
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						} else {
							textBody.append(Util.getWeekDayName(Integer.parseInt(schdPrefList.get(i).getWkDayNbr()))).append(", ");
						}
						break;
					}
					previousDaySeg = currentDaySeg;
				}
				textBody.append("\n");
			} else {
				textBody.append("None Selected\n");
			}

			if (staffingUpdate.getStaffingDtlTo().getTargetPay() != null) {
				textBody.append("Target Pay: $/Hour ").append(staffingUpdate.getStaffingDtlTo().getTargetPay()).append("\n");
			} else {
				textBody.append("Target Pay: $/Hour ").append("\n");
			}

			textBody.append("-------------------- Instructions/Notes/Referrals ---------------------\n");
			textBody.append("Instructions/Notes/Referrals: ").append(staffingUpdate.getStaffingDtlTo().getReferals()).append("\n");

			EmailSender.sendEmail(toAddresses, fromAddress, subject.toString(), textBody.toString(), strLCEnv);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Response getQpPilotResult() throws RetailStaffingException {

		logger.info(this + " Enters getQpPilotResult method in Manager...");

		Response res = null;
		RetailStaffingRequisitionDAO reqMgr = new RetailStaffingRequisitionDAO();

		try {
			res = new Response();
			String qpStatus = reqMgr.readHrOrgParmQpStatus();
			logger.info("****getQpPilotResult method QP Status = " + qpStatus);

			if (qpStatus.equalsIgnoreCase("Production")) {
				res.setShowQpButton("true");
			} else if (qpStatus.equalsIgnoreCase("Off")) {
				res.setShowQpButton("false");
			} else if (qpStatus.equalsIgnoreCase("Pilot")) {
				Profile profile = Profile.getCurrent();
				if (profile != null) {
					String userId = profile.getProperty(Profile.USER_ID);
					logger.info("getQpPilotResult method User Id = " + userId);
					List<String> pilotUserList = reqMgr.readHrOrgParmQpStatusIsPilot();
					res.setShowQpButton("false");
					if (pilotUserList != null && pilotUserList.size() > 0) {
						int listLength = pilotUserList.size();
						for (int i = 0; i < listLength; i++) {
							if (userId.toUpperCase().equals(pilotUserList.toArray()[i].toString().toUpperCase())) {
								res.setShowQpButton("true");
								break;
							}
						}
					}
				}
			}

		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + " Leaves getQpPilotResult method in Manager with output..." + res);
		return res;
	}
	
	
	/**
	 * This function updates the calendar id on the requisition provided
	 * 
	 * @param requisitionNumber			The requisition to update
	 * @param calendarId				The calendar ID
	 * 
	 * @throws							QueryException		Thrown if an exception occurs updating the requisition
	 */
	public static void updateRequisitionCalendarId(final String requisitionNumber, final int calendarId) throws QueryException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug(String.format("Entering updateRequisitionCalendarId(), requisition %1$s, calendar id %2$d", requisitionNumber, calendarId));
		} // end if
		
		try
		{
			// make sure the calendar id is > 0
			ValidationUtils.validateRequisitionCalendarId(calendarId);
			// make sure the requisition id provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.REQUISITION_NUMBER, requisitionNumber);
			
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
			
			// create a UniversalConnectionHandler so we use one connection to execute multiple 
			UniversalConnectionHandler handler = new UniversalConnectionHandler(false, null, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{

					/*
					 * - DAO calls need to be static method calls
					 * - Incorrect type being provided to the DAO
					 * - Will there ever be multiple requisitions returned for a single ID?
					 *   Re-examine the DAO and only return one instead of a list
					 */
					// get the employment requisitions for the requisition number
					RetailStaffingRequisitionDAO dao = new RetailStaffingRequisitionDAO();
					List<ReadThdStoreEmploymentRequisitionDTO> storeEmploymentReqs = dao.readThdStoreEmploymentRequisition(requisitionNumber);
					
					// if employment reqs were returned
					if(storeEmploymentReqs != null)
					{
						if(logger.isDebugEnabled())
						{
							logger.debug(String.format("Updating %1$d employment requisitions for requisition number %2$s", storeEmploymentReqs.size(), requisitionNumber));
						} // end if
						
						// iterate the employment reqs (should only ever be one)
						for(ReadThdStoreEmploymentRequisitionDTO storeEmploymentReq : storeEmploymentReqs)
						{
							// if the requisition is not null
							if(storeEmploymentReq != null)
							{
								if(logger.isDebugEnabled())
								{
									logger.debug(String.format("Updating employment requisition %1$s with calendar id %2$d", requisitionNumber, calendarId));
								} // end if
								
								// set the id of the new calendar on the requisition
								storeEmploymentReq.setRequisitionCalendarId(calendarId);

								// update the requisition (update exceptions will be caught in the catch(QueryException block below)
								dao.updateThdStoreEmploymentRequisition(storeEmploymentReq);
								if(logger.isDebugEnabled())
								{
									logger.debug(String.format("Successfully updated requisition %1$s with calendar id %2$d", requisitionNumber, calendarId));
								}
							} // end if(storeEmploymentReq != null)									
						} // end for(ReadThdStoreEmploymentRequisitionDTO storeEmploymentReq : storeEmploymentReqs)
					} // end if(storeEmploymentReqs != null)						
				} // end function handleQuery();
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();
		} // end try
		catch(QueryException qe)
		{
			logger.error(String.format("An exception occurred updating requition %1$s with calendar id %2$d", requisitionNumber, calendarId), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		logger.debug("Exiting updateRequisitionCalendarId()");
	}// end function updateRequisitionCalendarId()	
	
	/**
	 *
	 * This method is used to resend the quewebemail listed in the textbox
	 *
	 *
	 * @param String reqNos
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response resendQueWebEmailBatch(String ReqNos) throws RetailStaffingException{

		Response res = new Response() ;
		ReqNos = ReqNos.trim() ;
		String[] reqList = ReqNos.split(",") ;
		StringBuilder reqErrors = new StringBuilder(25) ;


		for (String req: reqList)
		{
			try
			{
				req = req.trim();
				resendQueWebEmail(req) ;
			}
			catch (Exception e)
			{
				//if requisition fails to be sent it shall be placed into the response
				reqErrors.append(req);
				reqErrors.append(", ") ;
			}
		}

		if (reqErrors.toString().equals(""))
		{
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS) ;
		}
		else
		{
			reqErrors.deleteCharAt(reqErrors.length()-2) ;//remove last comma
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			res.setSuccessMsg(reqErrors.toString()) ;
		}

		return res ;

	}
	/**
	 *
	 * This method is used to resend the quewebemail
	 *
	 *
	 * @param reqNo
	 * @return
	 * @throws RetailStaffingException
	 */
	public void resendQueWebEmail(String ReqNo) throws RetailStaffingException
	{
		logger.info(this + "Enters resendQueWebEmail method in Manager with input" + ReqNo);

		List<StoreDetailsTO> ReqStrlist = null;
		StoreDetailsTO ReqstrDetTO = null;
		List<RequisitionDetailTO> reqList = null;
		RequisitionDetailTO reqDtl = null;
		PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		List<StaffingDetailsTO> stfList = null;
		StaffingDetailsTO stfDtls = new StaffingDetailsTO();
		List<ExperienceLevelTO> oExpLvlRes = null;
		UpdateStaffingRequest updstfRqst = new UpdateStaffingRequest() ;
		updstfRqst.setStaffingDtlTo(new StaffingDetailsTO()) ;

		ReqNo.trim();

		try
		{
			updstfRqst.setReqNbr(ReqNo) ;

			if (ReqNo != null && !ReqNo.trim().equals(""))
			{
				ReqstrDetTO = new StoreDetailsTO();
				// get Store Number from DAO
				reqList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(ReqNo));
				stfList = phnScrnDAO.readRequisitionStaffingDetails(Integer.parseInt(ReqNo));

				if (stfList != null && !stfList.isEmpty() && stfList.size() > 0)
				{
					logger.debug("Entering if statement for Staffing Update Request");
					stfDtls = stfList.get(0);
					//setting updstfRqst to what stfDtls contains
					updstfRqst.setStaffingDtlTo(stfDtls) ;
				}
				else
				{
					logger.debug("Did not enter if statement for Staffing Update Request") ;
				}

				updstfRqst.getStaffingDtlTo().setQualPoolNts((delReqMgr.readEmploymentRequisitionNote(Integer.parseInt(ReqNo), RetailStaffingConstants.QUALIFIED_POOL_NOTE_TYP_CD)).getQualPoolNts()) ;
				updstfRqst.setStaffingDtlTo(getDetailsFromNotes(updstfRqst.getStaffingDtlTo())) ;

				logger.info(updstfRqst.getStaffingDtlTo().getQualPoolNts()) ;
				//get information from requisition details
				if (reqList != null && !reqList.isEmpty() && reqList.size() > 0)
				{
					//Loading details from RequisitionDetailsTO
					reqDtl = reqList.get(0);
					updstfRqst.getStaffingDtlTo().setDeptNo(reqDtl.getDept()) ;
					updstfRqst.getStaffingDtlTo().setRequestorName(reqDtl.getCreator()) ;
					updstfRqst.getStaffingDtlTo().setJobTtlCd(reqDtl.getJob());
					updstfRqst.getStaffingDtlTo().setJobTitle(reqDtl.getJobTtl()) ;
					updstfRqst.getStaffingDtlTo().setDeptNo(reqDtl.getDept()) ;
					updstfRqst.getStaffingDtlTo().setNumPositions(reqDtl.getOpenings()) ;
					updstfRqst.getStaffingDtlTo().setRequestedNbrIntvws(reqDtl.getReqNumInterviews()) ;
					updstfRqst.getStaffingDtlTo().setSealTempJob(reqDtl.getSealTempJob()) ;
					updstfRqst.getStaffingDtlTo().setRscSchdFlg(reqDtl.getRscSchdFlg()) ;

					//change PT and FT to correct format
					logger.debug("Going into if statement for PT STATUS") ;
					if(reqDtl.getPt().equalsIgnoreCase("Y"))
					{
						logger.debug("IF STATEMENT FOR PT STATUS STATUS IS GETTING HIT") ;
						updstfRqst.getStaffingDtlTo().setJobStatusPt("true") ;
					}
					else updstfRqst.getStaffingDtlTo().setJobStatusPt("false") ;

					if(reqDtl.getFt().equalsIgnoreCase("Y"))
						updstfRqst.getStaffingDtlTo().setJobStatusFt("true") ;
					else updstfRqst.getStaffingDtlTo().setJobStatusFt("false") ;

					if (logger.isDebugEnabled())
					{
						logger.debug("FT STATE:  "+ updstfRqst.getStaffingDtlTo().getJobStatusFt()) ;
						logger.debug("PT STATE:  "+ updstfRqst.getStaffingDtlTo().getJobStatusPt()) ;
					}

					//CHANGE DATE TO CORRECT FORMAT
					String datephrase = reqDtl.getFillDt() ;
					String[] datetokens = datephrase.split("-");
					logger.debug("DATE BEFORE FORMAT:  "+datephrase) ;
					DateTO fillDate = new DateTO() ;
					fillDate.setDay(datetokens[0]) ;
					fillDate.setMonth(datetokens[1]) ;
					fillDate.setYear(datetokens[2]) ;
					logger.debug("DATE AFTER FORMAT: "+ fillDate.toString()) ;
					updstfRqst.getStaffingDtlTo().setFillDt(fillDate) ;

					// getting store data from DAO
					ReqstrDetTO = LocationManager.getStoreDetailsDAO20(reqDtl.getStore());
					//CHECK STORE NUMBER
					updstfRqst.getStaffingDtlTo().setStrNo(ReqstrDetTO.getStrNum()) ;
				}
				// to fetch desired experience level
				oExpLvlRes = delReqMgr.readNlsExperienceLevelList();
				if (oExpLvlRes != null && oExpLvlRes.size() > 0)
				{
					updstfRqst.getStaffingDtlTo().setDesiredExpText(oExpLvlRes.get(0).getDsplyDesc());
				}
			}
			else
			{
				logger.error(this + "Input is either null or empty string in resendquewebemail");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
				updstfRqst.setInsertUpdate("INSERT UPDATE") ;
				updstfRqst.setReqCalId(12) ;
				updstfRqst.getStaffingDtlTo().setRequestorTitle("/n") ;
				updstfRqst.getStaffingDtlTo().setRequestorPhnNbr("0000000000");
				updstfRqst.getStaffingDtlTo().setHireByFlg("N") ;
		}
		catch (RetailStaffingException e) {
			throw e;
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		createQuewebEmail(updstfRqst) ;

	}

	/**
	 *
	 * This method is used to take the schedule preference from notes back into SchdPrefDetailsList
	 *
	 *
	 * @param StaffingDetailsTO StaffingDetails
	 * @return
	 * @throws RetailStaffingException
	 */
	public StaffingDetailsTO getDetailsFromNotes(StaffingDetailsTO stfDtls) throws RetailStaffingException
	{

		//tokens is used to break each line into Day segment and Week day Numbers
		String[] segment = null ;
		Boolean isDaySegment = false ;
		List<SchedulePreferenceTO> prefList = new ArrayList<SchedulePreferenceTO>() ;
		SchedulePreferenceTO pref = new SchedulePreferenceTO() ;
		//lines is used to break the note into separate lines
		String[] lines = stfDtls.getQualPoolNts().split("\\n") ;
		//tokens break the days apart for checking
		String[] tokens = null ;

		String wkDayNbr = new String() ;
		String daySegCd = new String() ;


		logger.info(lines.toString()) ;
		logger.info("Entering getSchdPrefDetailsListFromNotes") ;

		try
		{


			for(String s: lines)
			{
				logger.debug("Inside first for loop for Schedule Preference") ;
				isDaySegment = false ;
				segment = s.split(":") ;
				segment[0] = segment[0].trim();
				logger.debug("Segment looks like: " + segment[0]) ;
				daySegCd = new String() ;

				//Checks for Candidate Type or Experience
				if(segment[0].equalsIgnoreCase("Language Skills"))
					stfDtls.setLanguageSkls(segment[1]) ;

				if(segment[0].equalsIgnoreCase("Candidate Type"))
					stfDtls.setCandidateType(segment[1]) ;

				if(segment[0].equalsIgnoreCase("Required Experience"))
					stfDtls.setDesiredExpText(segment[1]) ;

				//Checks to see if the line pertains to schedule preference by seeing if first part of line is a day segment
				if( segment[0].equalsIgnoreCase("Anytime"))
				{
					isDaySegment = true ;
					daySegCd = "0" ;
				}
				if (segment[0].equalsIgnoreCase("Early AM"))
				{
					isDaySegment = true ;
					daySegCd = "1" ;
				}
				if (segment[0].equalsIgnoreCase("Mornings"))
				{
					isDaySegment = true ;
					daySegCd = "2"  ;
				}
				if (segment[0].equalsIgnoreCase("Afternoons"))
				{
					isDaySegment = true ;
					daySegCd = "3" ;
				}
				if (segment[0].equalsIgnoreCase("Evenings"))
				{
					isDaySegment = true ;
					daySegCd = "4" ;
				}
				if (segment[0].equalsIgnoreCase("Late PM"))
				{
					isDaySegment = true ;
					daySegCd = "5" ;
				}
				if (segment[0].equalsIgnoreCase("Overnight"))
				{
					isDaySegment = true ;
					daySegCd = "6" ;
				}

				//if the line pertains to a day of the week for availability
				if (isDaySegment == true)
				{
					segment[1] = segment[1].trim() ;
					logger.info("Is a day segment line, looks like:  "+ segment[1]) ;


					tokens = segment[1].split(",") ;
					for(String t: tokens)
					{
						pref = new SchedulePreferenceTO() ;
						logger.debug("Broken line into days of week") ;
						t = t.trim() ;
						wkDayNbr = new String() ;

						//check for day of week
						if(t.equalsIgnoreCase("Mon"))
							wkDayNbr="1" ;
						if(t.equalsIgnoreCase("Tue"))
							wkDayNbr="2" ;
						if(t.equalsIgnoreCase("Wed"))
							wkDayNbr="3" ;
						if(t.equalsIgnoreCase("Thr"))
							wkDayNbr="4" ;
						if(t.equalsIgnoreCase("Fri"))
							wkDayNbr="5" ;
						if(t.equalsIgnoreCase("Sat"))
							wkDayNbr="6" ;
						if(t.equalsIgnoreCase("Sun"))
							wkDayNbr="7" ;
						if (logger.isDebugEnabled())
							logger.info("Token looks like: " +t) ;

						pref.setWkDayNbr(wkDayNbr) ;
						pref.setDaySegCd(daySegCd) ;

						prefList.add(pref) ;
					}
				}

				//for loop for debugging purposes
				logger.debug("This is the contentes of Staffing Details Schedule Preference TO") ;
				if(logger.isDebugEnabled())
				{
					for(SchedulePreferenceTO sp: prefList)
					{
						logger.debug("DaySegCd():  "+ sp.getDaySegCd() + "  WkDayNbr():  " +sp.getWkDayNbr()) ;

					}
				}

			}


		}catch (Exception e){
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e) ;
		}

		stfDtls.setSchdPrefDetailsList(prefList) ;


		return stfDtls;
	}
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * Description : This method will format the response.
	 *
	 * @param response 
	 * 			- the response
	 * 
	 * @return 
	 * 			- the formatted requisition response
	 * 
	 * @throws RetailStaffingException 
	 * 			- the retail staffing exception
	 */
	public Response getFormattedRequisitionResponse(Response response) throws RetailStaffingException 
	{
		logger.info(this + "Enters getFormattedRequisitionResponse method in Manager with input as" + response);
		if (response!=null)
		{
			ITIDetailResponse itDetailsRes = response.getItiDtRes();
			StaffingDetailsTO staffingDetails = response.getStfDtls();
			/** 1. Sort the itDetailsList by name**/
			sortITDlsListByName(itDetailsRes);
			
			/** 2. FormatDate (format MM/DD/YYYY) for interviewDate  **/
			formatInterviewDate(itDetailsRes);
			
			if(staffingDetails !=null){
				
				/** 3. FormatPhoneNumber for hrgMgrPhn **/
				staffingDetails.setFormattedstfhrgEvntLocPhn(Util.formatPhoneNbr(staffingDetails.getHrgMgrPhn()));
				
				/** 4. FormatDate (format MM/DD/YYYY) for stfHrgEvntStartDt,StfHrgEvntEndDt,weekBeginDt **/
				formatDateStaffgDtls(staffingDetails);
				
				/** 5. FormatTime for StartTime,endTime,lunch,lastIntrTm **/
				formatTimeStaffgDtls(staffingDetails);
			}
			
			/** 6. Sort ScheduleDescList in ScheduleDescDetail TO**/
			if(response.getScheduleRes()!=null && !Util.isNullList(response.getScheduleRes().getSchDescList())){
					Collections.sort(response.getScheduleRes().getSchDescList());
			}
			
			/** 7. Sort requisitionStats in StatusResponse TO **/
			if(response.getStatusListRes()!=null && !Util.isNullList(response.getStatusListRes().getRequisitionStats()))
			{
					Collections.sort(response.getStatusListRes().getRequisitionStats());
			}
		}	
		
		logger.info(this + "Leaves getFormattedRequisitionResponse method in Manager with output as" + response);
		return response;
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
	
	/**
	 * Description : This method will Sort the itDetailsList by name
	 *
	 * @param itDetailsRes the it details res
	 */
	public static void sortITDlsListByName(ITIDetailResponse itDetailsRes){
		if(itDetailsRes !=null)
		{
			List<PhoneScreenIntrwDetailsTO> itDetailsList = itDetailsRes.getITIDtlList();
			if(!Util.isNullList(itDetailsList)){
				Collections.sort(itDetailsList);
			}
		}
	}
	
	/**
	 * Description : This method will FormatDate (format MM/DD/YYYY) for interviewDate.
	 *
	 * @param itDetailsRes 
	 * 			- the it details res
	 */
	public static void formatInterviewDate( ITIDetailResponse itDetailsRes){
		if(itDetailsRes!=null && !Util.isNullList(itDetailsRes.getITIDtlList())){
			for (PhoneScreenIntrwDetailsTO itDtls : itDetailsRes.getITIDtlList()){
				IntrwLocationDetailsTO interviewLocDetails = itDtls.getIntrLocDtls();
				if(interviewLocDetails != null){
					Util.setFormattedDate(interviewLocDetails.getInterviewDate());
				}
			}
		}
	}
	
	/**
	 * Description : This method will FormatDate (format MM/DD/YYYY) for stfHrgEvntStartDt,StfHrgEvntEndDt,weekBeginDt 
	 * 				 in StaffingDetailsTO .
	 *
	 * @param staffingDetails 
	 * 				- the staffing details
	 */
	public static void  formatDateStaffgDtls(StaffingDetailsTO staffingDetails){
			Util.setFormattedDate(staffingDetails.getStfHrgEvntStartDt());
			Util.setFormattedDate(staffingDetails.getStfHrgEvntEndDt());
			Util.setFormattedDate(staffingDetails.getWeekBeginDt());
		}
	
	/**
	 * Description : This method will FormatTime for StartTime,endTime,lunch,lastIntrTm in StaffingDetailsTO.
	 *
	 * @param staffingDetails 
	 * 			- the staffing details
	 */
	public static void  formatTimeStaffgDtls(StaffingDetailsTO staffingDetails){
			Util.setFormattedDateAndTime(staffingDetails.getStartTime(),false);
			Util.setFormattedDateAndTime(staffingDetails.getEndTime(),false);
			Util.setFormattedDateAndTime(staffingDetails.getLunch(),false);
			Util.setFormattedDateAndTime(staffingDetails.getLastIntrTm(),false);
	}
	
}
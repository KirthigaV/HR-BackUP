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
package com.homedepot.hr.hr.retailstaffing.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.IntervwResultsDAO;
import com.homedepot.hr.hr.retailstaffing.dao.PhoneScreenDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingITIDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.LanguageSkillsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PagingRecordInfo;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenCallHistoryTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenDispositionsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadHumanResourcesStoreEmploymentRequisitionDTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.CreatePhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.ITIUpdateRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.CompleteITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.LangSklsResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhnScrnCallHistoryResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhnScrnDispCodeResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDetailResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.services.response.StatusResponse;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.ConnectionHandler;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.TransactionConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This Class is used to have the buisness logic for Search based on ITI Number,
 * ITI Status
 * 
 * Methods: getPhoneScreenIntrwDtls - To fetch the Phone Screen details
 * updateInsertITIDetails - to Insert or Update Phone Screen Details
 * getDetailedPhoneScreenIntrwDtlsByStore-Based on given store fetch
 * DetailedPhoneScreen Interview details
 * getDetailedPhoneScreenIntrwDtlsByReg-Based on given region fetch
 * DetailedPhoneScreen Interview details
 * getDetailedPhoneScreenIntrwDtlsByDist-Based on given district fetch
 * DetailedPhoneScreen Interview details
 * getDetailedPhoneScreenIntrwDtlsByDiv-Based on given division fetch
 * DetailedPhoneScreen Interview details getBasicPhoneScreenIntrwDtlsByStore -
 * Based on given store fetch BasicPhoneScreen Interview details
 * getBasicPhoneScreenIntrwDtlsByReg - Based on given region fetch
 * BasicPhoneScreen Interview details getBasicPhoneScreenIntrwDtlsByDist - Based
 * on given district fetch BasicPhoneScreen Interview details
 * getBasicPhoneScreenIntrwDtlsByDiv - Based on given division fetch
 * BasicPhoneScreen Interview details
 * getReviewCompletedPhoneScreenIntrwDtlsByStore - Based on given store fetch
 * ReviewCompleted Interview details getReviewCompletedPhoneScreenIntrwDtlsByReg
 * - Based on given region fetch ReviewCompleted Interview details
 * getReviewCompletedPhoneScreenIntrwDtlsByDist- Based on given district fetch
 * ReviewCompleted Interview details
 * getReviewCompletedPhoneScreenIntrwDtlsByDiv- Based on given division fetch
 * ReviewCompleted Interview details
 * getScheduleInterviewPhoneScreenIntrwDtlsByStore - Based on given store fetch
 * ScheduleInterview Interview details
 * getScheduleInterviewPhoneScreenIntrwDtlsByReg - Based on given region fetch
 * ScheduleInterview Interview details
 * getScheduleInterviewPhoneScreenIntrwDtlsByDist- Based on given District fetch
 * ScheduleInterview Interview details
 * getScheduleInterviewPhoneScreenIntrwDtlsByDiv - Based on given division fetch
 * ScheduleInterview Interview details createPhoneScreens- Create Phone screen
 * based
 * 
 * @author TCS
 * 
 */

public class RetailStaffingITIManager implements RetailStaffingConstants, DAOConstants {
	private static final Logger logger = Logger.getLogger(RetailStaffingITIManager.class);

	/**
	 * This method is used to get the ITI details for the entered ITI Number /
	 * Status
	 * 
	 * @param itiNbr
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getPhoneScreenIntrwDtls(String itiNbr) throws RetailStaffingException {
		logger.info(this + "Enters getPhoneScreenIntrwDtls method in Manager with input as" + itiNbr);
		Response res = null;

		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();
		ITIDetailResponse itiRes = new ITIDetailResponse();
		StoreDetailResponse strRes = new StoreDetailResponse();
		List<RequisitionDetailTO> reqList = new ArrayList<RequisitionDetailTO>();
		RequisitionDetailTO reqDtl = null;
		List<IntrwLocationDetailsTO> intrDtlsTO = null;
		List<PhoneScreenIntrwDetailsTO> phnScrnlist = null;
		List<PhoneScreenIntrwDetailsTO> phnScrnResTxtlist = null;
		PhoneScreenIntrwDetailsTO phnScrnIntrwDtls = null;
		PhoneScreenDAO phnScrnDAO = null;
		List<StaffingDetailsTO> stfList = null;
		StaffingDetailsTO stfDtls = null;
		IntrwLocationDetailsTO intrLocTO = null;
		int phnScrnNbr = 0;
		String hirFlag = FALSE;
		List<PhoneScreenIntrwDetailsTO> phcrnResOverReslist = null;
		List<CandidateDetailsTO> cndtList = null;
		CandidateDetailsTO cndt = null;
		List<PhoneScreenIntrwDetailsTO> phnScrIntrTo = null;
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		CompleteITIDetailResponse cmplITIRes = new CompleteITIDetailResponse();
		StoreDetailsTO store = null;
		List<ReadHumanResourcesStoreEmploymentRequisitionDTO> readStoreReq = new ArrayList<ReadHumanResourcesStoreEmploymentRequisitionDTO>();
		List<SchedulePreferenceTO> schPrefList = null;
		List<LanguageSkillsTO> oLangSklsRes = null;
		LangSklsResponse langSklsres = new LangSklsResponse();
		List<LanguageSkillsTO> reqnLangSklsList = null;		
		StatusResponse statRes = new StatusResponse();		
		IntervwResultsDAO intervwReqMgr = new IntervwResultsDAO();
		List<PhoneScreenDispositionsTO> phoneScreenDispList = null;
		PhnScrnDispCodeResponse phnScrnDispRes = new PhnScrnDispCodeResponse();		
		
		//Added for IVR Enhancements Feb/Mar 2016
		List<PhoneScreenCallHistoryTO> phoneScreenCallHistoryList = null;
		PhnScrnCallHistoryResponse phnScrnCallHistoryRes= new PhnScrnCallHistoryResponse();
		
		try {
			res = new Response();
			if (itiNbr != null && !itiNbr.trim().equals("")) {
				// input is ITI Number
				phnScrnNbr = Integer.parseInt(itiNbr);
				logger.info(this + "Input is ITI NBR" + phnScrnNbr);

				// getting data from DAO
				phnScrnDAO = new PhoneScreenDAO();
				PhoneScreenIntrwDetailsTO phnScrnIntrwDtlsPhn = null;
				// calling the readByPhoneScreenNumber method of phonescreen DAO
				phnScrnlist = phnScrnDAO.readByPhoneScreenNumber(phnScrnNbr);
				if (phnScrnlist != null && !phnScrnlist.isEmpty()) {
					phnScrnIntrwDtls = phnScrnlist.get(0);

					if (phnScrnIntrwDtls.getCndtNbr() != null) {
						// get the phoner number of the candidate if present
						phnScrnIntrwDtlsPhn = phnScrnDAO.readAssociatePhoneNumber(phnScrnIntrwDtls.getCndtNbr());
					}
					// check for internal or external based on the Associate ID
					if (phnScrnIntrwDtls.getAid() != null && !phnScrnIntrwDtls.getAid().trim().equals(EMPTY_STRING)) {
						// Set internal if AID is present
						phnScrnIntrwDtls.setInternalExternal(INTERNAL_FLAG);
						// set candidate phone number if it is internal
						phnScrnIntrwDtls.setCanPhn(phnScrnIntrwDtlsPhn.getCanPhn());
					} else {
						// Set external if no AID is present
						phnScrnIntrwDtls.setInternalExternal(EXTERNAL_FLAG);
						phnScrnIntrwDtls.setCndStrNbr(EMPTY_STRING);
						phnScrnIntrwDtls.setCndDeptNbr(EMPTY_STRING);
						phnScrnIntrwDtls.setTitle(EMPTY_STRING);
					}

					// Fetching Candidate details
					if (phnScrnIntrwDtls.getReqNbr() != null && phnScrnIntrwDtls.getCndtNbr() != null) {
						logger.info(this + "Before getting the candidate details");
						cndtList = phnScrnDAO.getCandidateDetails(phnScrnIntrwDtls.getCndtNbr(), Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
						if (cndtList != null && cndtList.size() > 0) {
							cndt = cndtList.get(0);
							if (cndt != null) {
								// set candidate phone number if it is external
								if (phnScrnIntrwDtls.getInternalExternal().equals(EXTERNAL_FLAG)) {
									phnScrnIntrwDtls.setCanPhn(cndt.getCanPhn());
								}
								if (phnScrnIntrwDtls.getName() == null) {
									phnScrnIntrwDtls.setName(cndt.getName());
								}
							}
						}
						logger.info(this + "After getting the candidate details canphone: " + phnScrnIntrwDtls.getCanPhn());
					}

					phcrnResOverReslist = PhoneScreenDAO.readHumanResourcesPhoneScreen(phnScrnNbr);
					if (phcrnResOverReslist != null && !phcrnResOverReslist.isEmpty() && phcrnResOverReslist.size() > 0) {
						phnScrnIntrwDtls.setPhoneScreenStatusCode(phcrnResOverReslist.get(0).getPhoneScreenStatusCode());
						phnScrnIntrwDtls.setInterviewStatusCode(phcrnResOverReslist.get(0).getInterviewStatusCode());
						phnScrnIntrwDtls.setInterviewMaterialStatusCode(phcrnResOverReslist.get(0).getInterviewMaterialStatusCode());
						phnScrnIntrwDtls.setOverAllStatus(phcrnResOverReslist.get(0).getOverAllStatus());
						phnScrnIntrwDtls.setPhnScreener(phcrnResOverReslist.get(0).getPhnScreener());
						phnScrnIntrwDtls.setYnstatus(phcrnResOverReslist.get(0).getYnstatus());
						phnScrnIntrwDtls.setPhnScrnDate(phcrnResOverReslist.get(0).getPhnScrnDate());
						phnScrnIntrwDtls.setPhnScrnTime(phcrnResOverReslist.get(0).getPhnScrnTime());
						phnScrnIntrwDtls.setEmailAdd(phcrnResOverReslist.get(0).getEmailAdd());
						phnScrnIntrwDtls.setPhoneScreenStatusTimestamp(phcrnResOverReslist.get(0).getPhoneScreenStatusTimestamp());
						phnScrnIntrwDtls.setInterviewStatusTimestamp(phcrnResOverReslist.get(0).getInterviewStatusTime());
						phnScrnIntrwDtls.setPhoneScreenDispositionCode(phcrnResOverReslist.get(0).getPhoneScreenDispositionCode());
					}

					// Added by p22o0mn for Defect #3323: To Get the Flag for
					// enough Phone Screens Completed or not for a given
					// requisition

					String phoneScreenStatus = phnScrnIntrwDtls.getPhoneScreenStatusCode();

					if (phoneScreenStatus != null
							 && (phoneScreenStatus.equals(String.valueOf(INITIATE_BASIC_PHONE_SCREEN)) 
									 || phoneScreenStatus.equals(String.valueOf(INITIATE_DETAILED_PHONE_SCREEN))
									 || phoneScreenStatus.equals(String.valueOf(INITIATE_SELF_SCREEN))
									 )) {									
						int remainingIntwCandCount = 0;
						remainingIntwCandCount = phnScrnDAO.readRemainingInterviewCandidateCountByPhoneScreenStatusCode(Integer
								.parseInt(phnScrnIntrwDtls.getReqNbr()),false);

						if (remainingIntwCandCount <= -2) {
							phnScrnIntrwDtls.setPhoneScreensCompletedFlg("Y");
						} else {
							phnScrnIntrwDtls.setPhoneScreensCompletedFlg("N");
						}
					}

					// calling the DAO to get the interview details
					intrDtlsTO = phnScrnDAO.readInterviewDetails(phnScrnNbr);

					// getting the requisition details
					reqList = phnScrnDAO.readRequisitionDetails(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
					if (reqList != null && !reqList.isEmpty() && reqList.size() > 0) {
						reqDtl = reqList.get(0);

						// to get the hiring event information
						stfList = phnScrnDAO.readRequisitionStaffingDetails(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
						if (stfList != null && !stfList.isEmpty() && stfList.size() > 0) {
							stfDtls = stfList.get(0);
							hirFlag = stfDtls.getHrgEvntFlg();
							stfDtls.setTargetPay(stfDtls.getTargetPay());
						}

						// get the store details (no null check, the LocationManager will throw a NoRowsFoundException if not found)
						store = LocationManager.getStoreDetailsDAO20(reqDtl.getStore());

						// setting the interview dtls to PhoneScreenTO
						// if the hiring event flag is N, then need to get the
						// values from Store Details
						if ((intrDtlsTO == null || intrDtlsTO.isEmpty() || intrDtlsTO.size() < 0 || intrDtlsTO.get(0) == null
								|| intrDtlsTO.get(0).getInterviewLocId() == null || intrDtlsTO.get(0).getInterviewLocId().trim().equals(EMPTY_STRING))
								&& (hirFlag == null || hirFlag.equals(FALSE))) 
						{
							phnScrnIntrwDtls.setInterviewLocTypCd(HIRING_STR_TYP_CD);
							intrLocTO = new IntrwLocationDetailsTO();
							intrLocTO.setAdd(store.getAdd());
							intrLocTO.setCity(store.getCity());
							intrLocTO.setState(store.getState());
							intrLocTO.setZip(store.getZip());
							intrLocTO.setPhone(store.getPhone());
							intrLocTO.setInterviewLocName(store.getStrName());
							intrLocTO.setInterviewLocId(HIRING_STR_TYP_CD);
						}
						// if the hiring event flag is Y, then need to get the
						// values from Staffing Details
						else if ((intrDtlsTO == null || intrDtlsTO.isEmpty() || intrDtlsTO.size() < 0 || intrDtlsTO.get(0) == null
								|| intrDtlsTO.get(0).getInterviewLocId() == null || intrDtlsTO.get(0).getInterviewLocId().trim().equals(EMPTY_STRING))
								&& (hirFlag != null && hirFlag.equals(TRUE))) {
							phnScrnIntrwDtls.setInterviewLocTypCd(HIRING_EVT_TYP_CD);
							intrLocTO = new IntrwLocationDetailsTO();
							if (stfDtls != null) {
								intrLocTO.setAdd(stfDtls.getAdd());
								intrLocTO.setCity(stfDtls.getCity());
								intrLocTO.setState(stfDtls.getState());
								intrLocTO.setZip(stfDtls.getZip());
								intrLocTO.setPhone(stfDtls.getStfhrgEvntLocPhn());
								intrLocTO.setInterviewLocName(stfDtls.getStfhrgEvntLoc());
								// intrLocTO.setInterviewer(stfDtls.getHrgMgrName());
							}
						} else {
							// if the interview details is having any value,
							// then need to get from ther
							if (intrDtlsTO != null && !intrDtlsTO.isEmpty() && intrDtlsTO.size() > 0 && intrDtlsTO.get(0) != null) {
								phnScrnIntrwDtls.setInterviewLocTypCd(intrDtlsTO.get(0).getInterviewLocId());
								intrLocTO = intrDtlsTO.get(0);
							}
							// worst case scenario
							else {
								phnScrnIntrwDtls.setInterviewLocTypCd(OTHR_LOC_TYP_CD);
								intrLocTO = new IntrwLocationDetailsTO();
							}
						}
						phnScrnIntrwDtls.setIntrLocDtls(intrLocTO);
					
						//Added for Auto-Attach - Get Requisition Schedule Preference
						schPrefList = delReqMgr.getReqnSchedulePref(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
						if (schPrefList != null && !schPrefList.isEmpty() && schPrefList.size() > 0) {
							reqDtl.setReqnSchdPref(schPrefList);
						}	
						
						//Added for Auto-Attach - Get requested Requisition Languages
						//Get Requisition Language Preference
						reqnLangSklsList = delReqMgr.getReqnLanguagePref(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
						if (reqnLangSklsList != null && !reqnLangSklsList.isEmpty() && reqnLangSklsList.size() > 0) {
							reqDtl.setReqnLangPref(reqnLangSklsList);
						}	
						
						//Determine if this position requires a driver's license
						List<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO> jobReqBgcList = null;
						if (reqDtl != null) {
							jobReqBgcList = intervwReqMgr.readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(reqDtl.getStore(), reqDtl.getJob(), reqDtl.getDept());
							if (jobReqBgcList != null && jobReqBgcList.size() > 0) {
								//Look for Component of 3
								for (int i = 0; i < jobReqBgcList.size(); i++ ) {
									ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO tempObj = jobReqBgcList.get(i); 
									if (tempObj.getBackgroundCheckSystemComponentId() == 3) {
										reqDtl.setDrivingPosition(true);
										break;
									} else {
										reqDtl.setDrivingPosition(false);
									}
								}	
							}
						}
						
						//Get the Phone Screen Banner Number based on Dept/Job Title
						phnScrnIntrwDtls.setPhoneScreenBannerNum(PhoneScreenDAO.getPhoneScreenBannerNum(reqDtl.getDept(), reqDtl.getJob()));
						
					}
					// setting the 10 minimum response
					phnScrnIntrwDtls.setMinResList(phnScrnDAO.readResponses(phnScrnNbr));
					// to get the note text for Response - notetypecode is 1 -
					// Contact History
					phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(phnScrnNbr, CONTACT_HISTRY_NOTE_TYP_CD);
					if (phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0) {
						phnScrnIntrwDtls.setContactHistoryTxt(phnScrnResTxtlist.get(0).getContactHistoryTxt());

					}

					// to get the note text for Response - notetypecode is 2
					// -Detail response Text History
					phnScrnResTxtlist = null;
					phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(phnScrnNbr, PHN_SCRN_RESPONSE_NOTE_TYP_CD);
					if (phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0) {
						phnScrnIntrwDtls.setDetailTxt(phnScrnResTxtlist.get(0).getDetailTxt());

					}

					// To fetch Complete phone screen details(Both Active and
					// Inactive)
					phnScrIntrTo = delReqMgr.readCompletePhoneScreenDetails(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
					if (phnScrIntrTo != null && phnScrIntrTo.size() > 0) {

						cmplITIRes.setITIDtlList(phnScrIntrTo);
						res.setCmplITIDet(cmplITIRes);
					}

					// To set Authorisation position count for requisition

					readStoreReq = phnScrnDAO.readHumanResourcesStoreEmploymentRequisition(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
					if (readStoreReq != null && readStoreReq.size() > 0) {
						res.setAuthCnt(String.valueOf(readStoreReq.get(0).getAuthorizationPositionCount()));
					}
					phnScrnIntrwDtls.setHrEvntFlg(hirFlag);
					// setting the values to Response object
					phnScrnlist = null;
					phnScrnlist = new ArrayList<PhoneScreenIntrwDetailsTO>();
					phnScrnlist.add(phnScrnIntrwDtls);

					// to fetch language skills
					oLangSklsRes = delReqMgr.readLanguageSkillsList();
					if (oLangSklsRes != null && oLangSklsRes.size() > 0) {
						langSklsres.setLangSklsList(oLangSklsRes);
						res.setLangSklsRes(langSklsres);
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
					
					//Get Phone Screen Disposition Codes
					phoneScreenDispList = delReqMgr.readPhoneScreenDispositionCodeList();
					if (phoneScreenDispList != null && phoneScreenDispList.size() > 0) {
						phnScrnDispRes.setPhnScrnDispList(phoneScreenDispList);
						res.setPhnScrDispCodeRes(phnScrnDispRes);
					}										
					
					//IVR Enhancements Feb/Mar 2016
					//Get Phone/Interview Call History
					phoneScreenCallHistoryList = delReqMgr.readCallHistoryList(phnScrnNbr);
					if (phoneScreenCallHistoryList != null && phoneScreenCallHistoryList.size() > 0) {
						phnScrnCallHistoryRes.setPhnScrnCallHistoryList(phoneScreenCallHistoryList);
						res.setPhnScrnCallHistoryRes(phnScrnCallHistoryRes);
					}
										
					reqRes.setReqDtlList(reqList);
					strRes.addStore(store);
					itiRes.setITIDtlList(phnScrnlist);

					res.setStatus(SUCCESS_APP_STATUS);
					res.setItiDtRes(itiRes);
					res.setReqDtlList(reqRes);
					res.setStrDtRes(strRes);
					res.setStfDtls(stfDtls);
					res.setStatusListRes(statRes);
				} else {
					logger.error(this + "Invalid input");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
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
		logger.info(this + "Leaves getPhoneScreenIntrwDtls method in Manager with output as" + res);
		return res;
	}

	/**
	 * This method is used to get the phone screen minimum details for the entered phone screen Number
	 * 
	 * @param phoneScreenNumber
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getPhoneScreenMinimumDtls(String itiNbr) throws RetailStaffingException {
		logger.info(this + "Enters getPhoneScreenMinimumDtls method in Manager with input as" + itiNbr);
		Response res = null;

		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();		
		ITIDetailResponse itiRes = new ITIDetailResponse();
		List<RequisitionDetailTO> reqList = new ArrayList<RequisitionDetailTO>();
		List<PhoneScreenIntrwDetailsTO> phnScrnlist = null;
		List<PhoneScreenIntrwDetailsTO> phnScrnResTxtlist = null;
		PhoneScreenIntrwDetailsTO phnScrnIntrwDtls = null;
		PhoneScreenDAO phnScrnDAO = null;
		int phnScrnNbr = 0;
		List<PhoneScreenIntrwDetailsTO> phcrnResOverReslist = null;
		List<PhoneScreenDispositionsTO> phoneScreenDispList = null;
		PhnScrnDispCodeResponse phnScrnDispRes = new PhnScrnDispCodeResponse();	
		
		try {
			res = new Response();
			if (itiNbr != null && !itiNbr.trim().equals("")) {
				phnScrnNbr = Integer.parseInt(itiNbr);

				phnScrnDAO = new PhoneScreenDAO();
				phnScrnlist = phnScrnDAO.readByPhoneScreenNumber(phnScrnNbr);
				if (phnScrnlist != null && !phnScrnlist.isEmpty()) {
					phnScrnIntrwDtls = phnScrnlist.get(0);

					phcrnResOverReslist = PhoneScreenDAO.readHumanResourcesPhoneScreen(phnScrnNbr);
					if (phcrnResOverReslist != null && !phcrnResOverReslist.isEmpty() && phcrnResOverReslist.size() > 0) {
						phnScrnIntrwDtls.setPhoneScreenStatusCode(phcrnResOverReslist.get(0).getPhoneScreenStatusCode());
						phnScrnIntrwDtls.setInterviewStatusCode(phcrnResOverReslist.get(0).getInterviewStatusCode());
						phnScrnIntrwDtls.setInterviewMaterialStatusCode(phcrnResOverReslist.get(0).getInterviewMaterialStatusCode());
						phnScrnIntrwDtls.setOverAllStatus(phcrnResOverReslist.get(0).getOverAllStatus());
						phnScrnIntrwDtls.setPhnScreener(phcrnResOverReslist.get(0).getPhnScreener());
						phnScrnIntrwDtls.setYnstatus(phcrnResOverReslist.get(0).getYnstatus());
						phnScrnIntrwDtls.setPhnScrnDate(phcrnResOverReslist.get(0).getPhnScrnDate());
						phnScrnIntrwDtls.setPhnScrnTime(phcrnResOverReslist.get(0).getPhnScrnTime());
						phnScrnIntrwDtls.setEmailAdd(phcrnResOverReslist.get(0).getEmailAdd());
						phnScrnIntrwDtls.setPhoneScreenStatusTimestamp(phcrnResOverReslist.get(0).getPhoneScreenStatusTimestamp());
						phnScrnIntrwDtls.setInterviewStatusTimestamp(phcrnResOverReslist.get(0).getInterviewStatusTime());
						phnScrnIntrwDtls.setPhoneScreenDispositionCode(phcrnResOverReslist.get(0).getPhoneScreenDispositionCode());
					}

					// Minimum Responses
					phnScrnIntrwDtls.setMinResList(phnScrnDAO.readResponses(phnScrnNbr));

					// Contact History
					phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(phnScrnNbr, CONTACT_HISTRY_NOTE_TYP_CD);
					if (phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0) {
						phnScrnIntrwDtls.setContactHistoryTxt(phnScrnResTxtlist.get(0).getContactHistoryTxt());

					}

					// Detail Response Text History
					phnScrnResTxtlist = null;
					phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(phnScrnNbr, PHN_SCRN_RESPONSE_NOTE_TYP_CD);
					if (phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0) {
						phnScrnIntrwDtls.setDetailTxt(phnScrnResTxtlist.get(0).getDetailTxt());

					}

					//Get Phone Screen Disposition Codes
					phoneScreenDispList = delReqMgr.readPhoneScreenDispositionCodeList();
					if (phoneScreenDispList != null && phoneScreenDispList.size() > 0) {
						phnScrnDispRes.setPhnScrnDispList(phoneScreenDispList);
						res.setPhnScrDispCodeRes(phnScrnDispRes);
					}
					
					// setting the values to Response object
					phnScrnlist = null;
					phnScrnlist = new ArrayList<PhoneScreenIntrwDetailsTO>();
					phnScrnlist.add(phnScrnIntrwDtls);

					reqRes.setReqDtlList(reqList);
					itiRes.setITIDtlList(phnScrnlist);

					res.setStatus(SUCCESS_APP_STATUS);
					res.setItiDtRes(itiRes);
				} else {
					logger.error(this + "Invalid input");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
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
		logger.info(this + "Leaves getPhoneScreenMinimumDtls method in Manager with output as" + res);
		return res;
	}
	
	/**
	 * Description: This method is used to get the phone screen minimum details for the entered phone screen Number
	 * @param res
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getFormmattedPhnScrnResponse(Response response) throws RetailStaffingException 
	{
		logger.info(this + "Enters getFormmattedPhnScrnResponse method in Manager with input as" + response);
		
		if(response!=null){
			//Sorting List
			if(response.getStatusListRes() !=null ){
				if(!Util.isNullList(response.getStatusListRes().getPhoneScreenStats())){
					Collections.sort(response.getStatusListRes().getPhoneScreenStats());
				}
				if(!Util.isNullList(response.getStatusListRes().getInterviewStats())){
					Collections.sort(response.getStatusListRes().getInterviewStats());
				}
			}
			
			//Setting FormattedDate(MM/DD/YYYY) AND formattedTime(HH:MM AM/PM)
			formatDateTime(response);
			
		}
		logger.info(this + "Leaves getFormmattedPhnScrnResponse method in Manager with output as" + response);
		return response;
	}
	
	/**
	 * Description: This method will set the formatted date and time
	 * @param res
	 */
	private void formatDateTime(Response response)
	{
		if(response.getItiDtRes()!=null && !Util.isNullList(response.getItiDtRes().getITIDtlList())){
			List<PhoneScreenIntrwDetailsTO> phnScrnList= response.getItiDtRes().getITIDtlList();
			for(PhoneScreenIntrwDetailsTO phnScrn: phnScrnList)
			{
				if(phnScrn.getPhnScrnDate()!=null){
					Util.setFormattedDate(phnScrn.getPhnScrnDate());
					
					Util.setFormattedDateAndTime(phnScrn.getPhnScrnTime(),true);
					Util.setFormattedDateAndTime(phnScrn.getPhoneScreenStatusTimestamp(),true);
					Util.setFormattedDateAndTime(phnScrn.getInterviewStatusTime(),true);
					
				}
				if(phnScrn.getIntrLocDtls()!=null ){
					Util.setFormattedDate(phnScrn.getIntrLocDtls().getInterviewDate());
					Util.setFormattedDateAndTime(phnScrn.getIntrLocDtls().getInterviewTime(),true);
				}
			}
		}
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015

	/**
	 * The method will be used for updating the interview screen details in the
	 * DB.
	 * 
	 * @param itiUpdate
	 *            - The object containing the phone screen details to be updated
	 *            or inserted.
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */

	public Response updateInterviewScrnDetails(ITIUpdateRequest itiUpdate) throws RetailStaffingException {
		logger.info(this + "Enters updateInterviewScrnDetails method in Manager with input as" + itiUpdate.getPhnScrnDtlTo().getItiNbr());
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;

		try 
		{
			res = new Response();

			if (itiUpdate != null && itiUpdate.getInsertUpdate().equalsIgnoreCase(INSERT)) 
			{
				logger.info(this + "Input is ITI insert" + itiUpdate.getInsertUpdate());

				res.setSuccessMsg(ITI_INSERT_SUCCESS);
			}
			// update scenario of ITI
			else if (itiUpdate != null && itiUpdate.getInsertUpdate().equalsIgnoreCase(UPDATE)) 
			{
				logger.info(this + "Input is ITI update" + itiUpdate.getInsertUpdate());
				// need to call the DAO Part
				RetailStaffingITIDAO = new RetailStaffingITIDAO();
				java.sql.Timestamp uiBegintimeslot = Util.convertTimestampTO(itiUpdate.getPhnScrnDtlTo().getIntrLocDtls().getInterviewTime());

				if (uiBegintimeslot != null) 
				{
					if (checkExtInterwTimeStamp(itiUpdate)) 
					{
						logger.debug("Interview Date is not changed, update only interview status and material status code only");
						PhoneScreenManager.updateInterviewStatusforPhoneScrn(itiUpdate.getPhnScrnDtlTo());
					} 
					else 
					{
						logger.debug("update interview phonescreen status and material status");
						String [] checkBeginTimeValue = checkBeginTimeSlots(itiUpdate, uiBegintimeslot);						
						logger.debug("checkBeginTimeValue  :"+Boolean.parseBoolean(checkBeginTimeValue[0].toString()));
						
						if (Boolean.parseBoolean(checkBeginTimeValue[0].toString())) 
						{
							RetailStaffingITIDAO.updateInterviewScrnDetails(itiUpdate,checkBeginTimeValue);
							res.setSuccessMsg(ITI_UPDATE_SUCCESS);							
						} 
						else 
						{
							logger.debug(this + "Failure to Update record - Already Allocated");
							throw new RetailStaffingException(RetailStaffingConstants.FETCHING_INTERVIEW_TIMESLOTS_ERROR_CODE);
						}
					}
				} 
				else 
				{
					RetailStaffingITIDAO.updateInterviewScrnDetailsWithOutTimeSlot(itiUpdate);
					res.setSuccessMsg(ITI_UPDATE_SUCCESS);
				}
			} 
			else 
			{
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves updateInterviewScrnDetails method in Manager with output as" + res);
		return res;
	}

	@SuppressWarnings("deprecation")
	public synchronized String[] checkBeginTimeSlots(ITIUpdateRequest itiUpdate, java.sql.Timestamp uiBegintimeslot) throws RetailStaffingException 
	{
		try 
		{
			// declaration
			List<RequisitionScheduleTO> reqSchedTimeList = null;
			List<RequisitionDetailTO> reqList = null; 
			RequisitionDetailTO reqDtl = null;
			PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
			String[] returnValue; 
			returnValue = new String[2];

			logger.info(this + "Before readRequisitionDetails Insert" + itiUpdate.getPhnScrnDtlTo().getReqNbr().toString());
			// getting the requisition details
			reqList = phnScrnDAO.readRequisitionDetails(Integer.parseInt(itiUpdate.getPhnScrnDtlTo().getReqNbr().toString()));

			if (reqList != null && !reqList.isEmpty() && reqList.size() > 0) 
			{
				reqDtl = reqList.get(0);
			}
			
			// to get the Human Resource Requisition Schedule
			logger.info(this + "Before HumanReadRequistion Schedule");
			Calendar startCal = Calendar.getInstance();
			startCal.setTimeInMillis(uiBegintimeslot.getTime());
			startCal.set(Calendar.HOUR_OF_DAY, 4);
			startCal.set(Calendar.MINUTE, 0);
			startCal.set(Calendar.SECOND, 0);
			startCal.set(Calendar.MILLISECOND, 0);
			reqSchedTimeList = ScheduleManager.getAvailableInterviewSlots(reqDtl.getRscSchdFlg(),reqDtl.getReqCalId(), new Timestamp(startCal.getTimeInMillis()),
					Short.parseShort(reqDtl.getInterviewDurtn()));
			
			if (reqSchedTimeList != null && reqSchedTimeList.size() > 0) 
			{
				uiBegintimeslot.setSeconds(0);
				uiBegintimeslot.setNanos(0);

				for (RequisitionScheduleTO slot : reqSchedTimeList) 
				{
					logger.debug(" Slots trying to book: "+ slot.getBeginTimestamp().getTime());
					logger.debug(" Slots trying to UI begin:  "+ uiBegintimeslot.getTime());
					
					if (slot != null && slot.getBeginTimestamp().getTime() == uiBegintimeslot.getTime()) 
					{
						returnValue[0] = String.valueOf(true);
						returnValue[1] = String.valueOf(slot.getInterviewerAvailabilityCount());
						logger.debug("  Feching New Slot Sequence:  "+ slot.getInterviewerAvailabilityCount());
						return returnValue;
					}					
				} // end for(RequisitionScheduleTO slot : reqSchedTimeList)
				
				returnValue[0] = String.valueOf(checkExtInterwTimeStamp(itiUpdate));				
				return returnValue;
			} // end if(reqSchedTimeList != null && reqSchedTimeList.size() > 0)
			else {
				returnValue[0] = String.valueOf(checkExtInterwTimeStamp(itiUpdate));				
				return returnValue;
			} // end else
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
	}

	public Boolean checkExtInterwTimeStamp(ITIUpdateRequest itiUpdate) throws RetailStaffingException 
	
	{
		logger.debug(this + "Enters checkExtInterwTimeStamp method");
		PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
		List<IntrwLocationDetailsTO> intrDtlsTO = null;
		intrDtlsTO = phnScrnDAO.readInterviewDetails(Integer.parseInt(itiUpdate.getPhnScrnDtlTo().getItiNbr()));

		if (intrDtlsTO != null && !intrDtlsTO.isEmpty() && intrDtlsTO.size() > 0) {
			if ((intrDtlsTO.get(0).getInterviewTime() != null) && (intrDtlsTO.get(0).getInterviewDate() != null)
					&& (itiUpdate.getPhnScrnDtlTo().getIntrLocDtls().getInterviewDate() != null)
					&& (itiUpdate.getPhnScrnDtlTo().getIntrLocDtls().getInterviewTime() != null)) {
				String existIntrwDate = intrDtlsTO.get(0).getInterviewDate().toString().trim();
				String newIntrwDate = itiUpdate.getPhnScrnDtlTo().getIntrLocDtls().getInterviewDate().toString().trim();
				java.sql.Timestamp newBegintimeslot = Util.convertTimestampTO(itiUpdate.getPhnScrnDtlTo().getIntrLocDtls().getInterviewTime());
				java.sql.Timestamp existBegintimeslot = Util.convertTimestampTO(intrDtlsTO.get(0).getInterviewTime());
				if(logger.isDebugEnabled()){
					logger.debug("Existing Interview Time :" + existBegintimeslot.getTime());
					logger.debug("Existing Interview Date : " + existIntrwDate);
					logger.debug("New Interview Date : " + newIntrwDate);
					logger.debug("New Begin Time : " + newBegintimeslot.getTime());
				}
				if ((newIntrwDate.equalsIgnoreCase(existIntrwDate) && (existBegintimeslot.getTime() == newBegintimeslot.getTime()))) {
					logger.debug("Enters into  Database Save Option");
					return true;
				} else {
					logger.debug("Enter into Exception Error Option");
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * The method will be used for updating/inserting the phone screen details
	 * in the DB.
	 * 
	 * @param itiUpdate
	 *            - The object containing the phone screen details to be updated
	 *            or inserted.
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response updateInsertITIDetails(ITIUpdateRequest itiUpdate) throws RetailStaffingException {
		logger.info(this + "Enters updateInsertITIDetails method in Manager with input as" + itiUpdate.getPhnScrnDtlTo().getItiNbr());
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		RetailStaffingRequisitionDAO reqDao = new RetailStaffingRequisitionDAO();
		RequisitionDetailTO reqDet = null;
		List<PhoneScreenIntrwDetailsTO> phnscrnDet = null;
		final RetailStaffingITIDAO itidao = new RetailStaffingITIDAO();
		PhoneScreenIntrwDetailsTO phnScrnIntrwDtls = new PhoneScreenIntrwDetailsTO();

		try 
		{
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			DAOConnection daoConn = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);

			res = new Response();
			
			// Had to change getItiNbr to getCndtNbr... MTS1876
			List<PhoneScreenIntrwDetailsTO> interviewDetails = PhoneScreenDAO.readHumanResourcesPhoneScreen(Integer.parseInt(itiUpdate.getPhnScrnDtlTo().getCndtNbr()));
			
			if(interviewDetails != null && interviewDetails.size() > 0)
			{
				phnScrnIntrwDtls.setPhoneScreenStatusCode(interviewDetails.get(0).getPhoneScreenStatusCode());
			} // end if(interviewDetails != null && interviewDetails.size() > 0)

			if (itiUpdate != null && itiUpdate.getInsertUpdate().equalsIgnoreCase(INSERT)) {
				logger.info(this + "Input is ITI insert" + itiUpdate.getInsertUpdate());
				RetailStaffingITIDAO = new RetailStaffingITIDAO();
				// Insert only if the requisition number and the candidate
				// id combination does not exist in the phone screen detail
				// table.
				if (!RetailStaffingITIDAO.checkRequisitionCandidateForExistence(itiUpdate)) {
					RetailStaffingITIDAO.insertITIDetails(itiUpdate);
				}
				res.setSuccessMsg(ITI_INSERT_SUCCESS);
			}
			// update scenario of ITI
			else if (itiUpdate != null && itiUpdate.getInsertUpdate().equalsIgnoreCase(UPDATE)) {
				logger.info(this + "Input is ITI update" + itiUpdate.getInsertUpdate());
				// need to call the DAO Part
				RetailStaffingITIDAO = new RetailStaffingITIDAO();
				RetailStaffingITIDAO.updateITIDetails(itiUpdate);
				res.setSuccessMsg(ITI_UPDATE_SUCCESS);
			} else {
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}

			reqDet = reqDao.readPhoneScreenByEmploymentRequisitionNumber(itiUpdate.getPhnScrnDtlTo().getReqNbr());
			if (reqDet != null && reqDet.getPhnScrnCnt() > 0) {
				phnscrnDet = reqDao.readPhoneScreenIdByEmploymentRequisitionNumber(itiUpdate.getPhnScrnDtlTo().getReqNbr());

			}
			if (phnscrnDet != null && phnscrnDet.size() > 0) {

				// phnScrnIntrwDtls = new PhoneScreenIntrwDetailsTO();
				List<PhoneScreenIntrwDetailsTO> phnScrnResTxtlist = null;
				PhoneScreenIntrwDetailsTO phnScrnTO = new PhoneScreenIntrwDetailsTO();

				int listSize = phnscrnDet.size();
				for (int i = 0; i < listSize; i++) 
				{
					PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
					phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(Integer.parseInt(phnscrnDet.get(i).getItiNbr()),
							CONTACT_HISTRY_NOTE_TYP_CD);
					phnScrnTO.setItiNbr(phnscrnDet.get(i).getItiNbr());
					// Added back without Auto-Hold text.. MTS1876 10/02/10
					if (phnScrnResTxtlist != null && phnScrnResTxtlist.size() > 0) {
						if (phnScrnResTxtlist.get(0).getContactHistoryTxt() != null && phnScrnResTxtlist.get(0).getContactHistoryTxt().length() > 0) {
							if ((phnScrnResTxtlist.get(0).getContactHistoryTxt()).length() > CONTACT_HIS_LENGTH) {
								String contctHis = phnScrnResTxtlist.get(0).getContactHistoryTxt();
								phnScrnTO.setContactHistoryTxt(contctHis.substring(0, CONTACT_HIS_LENGTH));
							} else {
								phnScrnTO.setContactHistoryTxt(phnScrnResTxtlist.get(0).getContactHistoryTxt());
							}
						} else {
							phnScrnTO.setContactHistoryTxt("");
						}

						logger.info(" phnScrnTO.setContactHistoryTxt " + phnScrnTO.getContactHistoryTxt());
					}

					ITIUpdateRequest itiUpdateReq = new ITIUpdateRequest();
					itiUpdateReq.setPhnScrnDtlTo(phnScrnTO);
					final ITIUpdateRequest itiUpdateFinal = itiUpdateReq;
					// itiUpdateFinal =To(phnScrnDtlTo)
					ConnectionHandler connHandler = new TransactionConnectionHandler(null, daoConn) {
						public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException {
							DAOConnection daoConn = daoConnList.get(HRSTAFFING_DAO_CONTRACT);
							logger.info(this + "The connection obtained is" + daoConn);
							Query query = daoConn.getQuery();
							logger.info(this + "The query obtained is" + query);
							logger.info(this + " Updating the Contact History");
							// modified for RSA3.0
							try {
								PhoneScreenManager.updateContactHist(Integer.parseInt(itiUpdateFinal.getPhnScrnDtlTo().getItiNbr()), itiUpdateFinal
										.getPhnScrnDtlTo().getContactHistoryTxt());
							} catch (Exception e) {
								e.printStackTrace();

							}
						}
					};
					connHandler.execute();
					// Remove Auto-Hold MTS1876
					// reqDao.updatePhoneScreenStatus(phnscrnDet.get(i).getItiNbr(),AUTO_HOLD);

				}
			}

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves updateInsertITIDetails method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the detiled phone screens by org
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getDetailedPhoneScreenIntrwDtlsByStore(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getDetailedPhoneScreenIntrwDtlsByStore method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			// Check for the existence of the store first.
			if(!LocationManager.isValidStoreNumber(summReq.getOrgID()))
			{
				logger.error(this + "The entered store is invalid");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "store number: "
						+ summReq.getOrgID());
			}
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByStore(DETAILED_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				// Fetch set of records of detail phone screen from store based
				// on first record and Last record
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getDetailedPhoneScreenIntrwDtlsByStore method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the detailed phone screens by region
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getDetailedPhoneScreenIntrwDtlsByReg(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getDetailedPhoneScreenIntrwDtlsByReg method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByReg(DETAILED_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			// Fetch set of records from Region based on first record and Last
			// record
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getDetailedPhoneScreenIntrwDtlsByReg method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the detailed phone screens by district
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getDetailedPhoneScreenIntrwDtlsByDist(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getDetailedPhoneScreenIntrwDtlsByDist method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByDist(DETAILED_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records from District based on first record and Last
			// record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getDetailedPhoneScreenIntrwDtlsByDist method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the detailed phone screens by division
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getDetailedPhoneScreenIntrwDtlsByDiv(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getDetailedPhoneScreenIntrwDtlsByDiv method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByDiv(DETAILED_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records from Division based on first record and Last
			// record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getDetailedPhoneScreenIntrwDtlsByDiv method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the basic phone screens by store
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getBasicPhoneScreenIntrwDtlsByStore(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getDetailedPhoneScreenIntrwDtlsByStore method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			// Check for the existence of the store first.
			if(!LocationManager.isValidStoreNumber(summReq.getOrgID()))
			{
				logger.error(this + "The entered store is invalid");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "store number: "
						+ summReq.getOrgID());
			}
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByStore(BASIC_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of detail phone screen based on first record
			// and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getDetailedPhoneScreenIntrwDtlsByStore method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the basic phone screens by region
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getBasicPhoneScreenIntrwDtlsByReg(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getBasicPhoneScreenIntrwDtlsByReg method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByReg(BASIC_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of basic phone screen based on first record
			// and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getBasicPhoneScreenIntrwDtlsByReg method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the basic phone screens by district
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getBasicPhoneScreenIntrwDtlsByDist(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getBasicPhoneScreenIntrwDtlsByDist method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByDist(BASIC_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of basic phone screen of district based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getBasicPhoneScreenIntrwDtlsByDist method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the basic phone screens by division
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getBasicPhoneScreenIntrwDtlsByDiv(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getBasicPhoneScreenIntrwDtlsByDiv method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByDiv(BASIC_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of basic phone screen of division based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getBasicPhoneScreenIntrwDtlsByDiv method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed phone screens by store
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getReviewCompletedPhoneScreenIntrwDtlsByStore(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByStore method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		RequisitionDetailResponse reqDtRes = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			// Check for the existence of the Store
			if(!LocationManager.isValidStoreNumber(summReq.getOrgID()))
			{
				logger.error(this + "The entered store is invalid");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "store number: "
						+ summReq.getOrgID());
			}
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			reqDetMap = RetailStaffingITIDAO.getReviewCompletedPhoneScreenIntrwDtlsByStore(summReq);
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
			/*
			 * itiDtRes = new ITIDetailResponse();
			 * itiDtRes.setITIDtlList(itiDtlList); res.setItiDtRes(itiDtRes);
			 */
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of division
			// based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					// recordInfo.setUpdatedDate(Util.converDatetoDateTO((java.sql.Date)
					// recordDet.get("LAST_UPD_TS")));
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					// recordInfo.setUpdatedDate(Util.converDatetoDateTO((java.sql.Date)
					// recordDet.get("LAST_UPD_TS")));
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByStore method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed phone screens by
	 * region
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getReviewCompletedPhoneScreenIntrwDtlsByReg(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByReg method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		RequisitionDetailResponse reqDtRes = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			reqDetMap = RetailStaffingITIDAO.getReviewCompletedPhoneScreenIntrwDtlsByReg(summReq);
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
			/*
			 * itiDtRes = new ITIDetailResponse();
			 * itiDtRes.setITIDtlList(itiDtlList); res.setItiDtRes(itiDtRes);
			 */
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of division
			// based on
			// first record and Last record
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
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByReg method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed screens by district
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getReviewCompletedPhoneScreenIntrwDtlsByDist(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByDist method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		RequisitionDetailResponse reqDtRes = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			reqDetMap = RetailStaffingITIDAO.getReviewCompletedPhoneScreenIntrwDtlsByDist(summReq);
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
			/*
			 * itiDtRes = new ITIDetailResponse();
			 * itiDtRes.setITIDtlList(itiDtlList); res.setItiDtRes(itiDtRes);
			 */
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of division
			// based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					// recordInfo.setUpdatedDate(Util.converDatetoDateTO((java.sql.Date)
					// recordDet.get("LAST_UPD_TS")));
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					// recordInfo.setUpdatedDate(Util.converDatetoDateTO((java.sql.Date)
					// recordDet.get("LAST_UPD_TS")));
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByDist method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed screens by division
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getReviewCompletedPhoneScreenIntrwDtlsByDiv(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getReviewCompletedPhoneScreenIntrwDtlsByDiv method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		RequisitionDetailResponse reqDtRes = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			reqDetMap = RetailStaffingITIDAO.getReviewCompletedPhoneScreenIntrwDtlsByDiv(summReq);
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
			/*
			 * itiDtRes = new ITIDetailResponse();
			 * itiDtRes.setITIDtlList(itiDtlList); res.setItiDtRes(itiDtRes);
			 */
			Map<String, Object> paginationToken = (Map<String, Object>) reqDetMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of division
			// based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("EMPLT_REQN_NBR")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getReviewCompletedPhoneScreenIntrwDtlsByDiv method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed phone screens by store
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getScheduleInterviewPhoneScreenIntrwDtlsByStore(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getScheduleInterviewPhoneScreenIntrwDtlsByStore method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			// Check for the existence of the store first.
			if(!LocationManager.isValidStoreNumber(summReq.getOrgID()))
			{
				logger.error(this + "The entered store is invalid");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "store number: "
						+ summReq.getOrgID());
			}
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByStore(SCHEDULEINTERVIEW_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of store
			// based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getScheduleInterviewPhoneScreenIntrwDtlsByStore method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed phone screens by
	 * region
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getScheduleInterviewPhoneScreenIntrwDtlsByReg(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getScheduleInterviewPhoneScreenIntrwDtlsByReg method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByReg(SCHEDULEINTERVIEW_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of region
			// based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getScheduleInterviewPhoneScreenIntrwDtlsByReg method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed screens by district
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getScheduleInterviewPhoneScreenIntrwDtlsByDist(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getScheduleInterviewPhoneScreenIntrwDtlsByDist method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByDist(SCHEDULEINTERVIEW_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of district
			// based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getScheduleInterviewPhoneScreenIntrwDtlsByDist method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the review completed screens by division
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getScheduleInterviewPhoneScreenIntrwDtlsByDiv(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getScheduleInterviewPhoneScreenIntrwDtlsByDiv method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();
			itiDtlMap = RetailStaffingITIDAO.getPhoneScreenIntrwDtlsByDiv(SCHEDULEINTERVIEW_ITI_STATUS, summReq);
			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether comming from nex or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			// Fetch set of records of review completed phone screen of division
			// based on
			// first record and Last record
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getScheduleInterviewPhoneScreenIntrwDtlsByDiv method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to create the phone screens. The method is made
	 * synchronised in order to take care of the scenario where multiple users
	 * may try to create the phone screens for the same requisition at same
	 * time.
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public synchronized Response createPhoneScreens(CreatePhoneScreenRequest createPhoneScreenRequest) throws RetailStaffingException {
		Response res = null;
		List<PhoneScreenIntrwDetailsTO> phnScrIntrTo = null;
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();

		ITIDetailResponse itiDetRes = new ITIDetailResponse();
		try {
			if (createPhoneScreenRequest == null) {
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			} else {
				logger.info("The requisition ID is: " + createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO().getReqNbr());
				logger.info("The humanResourcesSystemStoreNumber is" + createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO().getCndStrNbr());
				logger.info("The employmentPositionCandidateId is" + createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO().getJob());
				logger.info("The cadidate IDS are: ");
				res = new Response();
				if (createPhoneScreenRequest.getCandidateDetailsTOS() != null) {
					List<CandidateDetailsTO> candidateList = createPhoneScreenRequest.getCandidateDetailsTOS();
					CandidateDetailsTO temp = null;
					ITIUpdateRequest itiUpdate = null;

					List<Short> phoneScreenStatusCodeList = new ArrayList<Short>();
					phoneScreenStatusCodeList.add(Short.valueOf("4"));
					//Save phone screen status
					String phoneScreenStatus = createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO().getOverAllStatus();
					// Recursive call to insert Candidate details and create
					// phone screen number which is unique.
					for (int i = 0; i < candidateList.size(); i++) {
						List<PhoneScreenIntrwDetailsTO> phoneScreenDetailsList = new ArrayList<PhoneScreenIntrwDetailsTO>();

						PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO = null;
						PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsOld = null;
						temp = candidateList.get(i);
						logger.info("The cadidate ID : " + i + " " + temp.getSsnNbr());
						
						phoneScreenIntrwDetailsTO = createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO();
					
						// Retrieve the phone screen type, OE32, from TRPRX
						// table 10069, based on the
						// current phone screen job code and department number
						String phoneScreenType = PhoneScreenDAO.getPhoneScreenType(createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO().getJobTtl(),
								createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO().getCndDeptNbr());

						//Create a blank transfer object so that some values may be reset.
						PhoneScreenIntrwDetailsTO ps = new PhoneScreenIntrwDetailsTO();
						
						// Retrieve previous matching phone screen details for candidate
						phoneScreenDetailsList = PhoneScreenDAO.readHumanResourcesPhoneScreenByRequisitionCreateDateInputList(temp.getSsnNbr(),
								phoneScreenStatusCodeList, phoneScreenType);

						if (phoneScreenDetailsList != null && phoneScreenDetailsList.size() > 0) {
							//Changed because was reading the oldest phone screen rather than the newest.
							//phoneScreenIntrwDetailsOld = phoneScreenDetailsList.get(phoneScreenDetailsList.size() - 1);
							phoneScreenIntrwDetailsOld = phoneScreenDetailsList.get(0);
							// Transfer desired old phone screen data to new
							phoneScreenIntrwDetailsTO.setItiNbr(phoneScreenIntrwDetailsOld.getItiNbr());
							phoneScreenIntrwDetailsTO.setPhnScrnDate(ps.getPhnScrnDate());
							phoneScreenIntrwDetailsTO.setPhnScrnTime(ps.getPhnScrnTime());
							phoneScreenIntrwDetailsTO.setPhoneScreenStatusCode(phoneScreenStatus);
							phoneScreenIntrwDetailsTO.setOverAllStatus(phoneScreenStatus);
							phoneScreenIntrwDetailsTO.setPhoneScreenStatusTimestamp(ps.getPhoneScreenStatusTimestamp());
							phoneScreenIntrwDetailsTO.setPhnScreener(ps.getPhnScreener());
							phoneScreenIntrwDetailsTO.setYnstatus(ps.getYnstatus());
							phoneScreenIntrwDetailsTO.setPhoneScreenDispositionCode(phoneScreenIntrwDetailsOld.getPhoneScreenDispositionCode());
							phoneScreenIntrwDetailsTO.setPhoneScreenDispositionCodeTimestamp(phoneScreenIntrwDetailsOld.getPhoneScreenDispositionCodeTimestamp());
							
							// Retrieve and save screen notes for insert with new phone screen later
							phoneScreenIntrwDetailsTO.setNote(PhoneScreenDAO.readHumanResourcesPhoneScreenNote(Integer.valueOf(
									phoneScreenIntrwDetailsTO.getItiNbr()).intValue(), Short.valueOf("4").shortValue()));
							
							//Set the previous Phone screen date, time and screener on the Contact History
							StringBuffer history = new StringBuffer(100);
							history.append("Previous phone screener ");
							history.append(StringUtils.trim(phoneScreenIntrwDetailsOld.getPhnScreener())).append(" ");
							history.append(Util.convertDateTO(phoneScreenIntrwDetailsOld.getPhnScrnDate())).append(" ");
							history.append(Util.convertTimestampTOtoTime(phoneScreenIntrwDetailsOld.getPhnScrnTime()));
							phoneScreenIntrwDetailsTO.setContactHistoryTxt(history.toString());
							
						} else {
							//Because the same object reference is returned from getPhoneScreenIntrvwDetailsTO() we must reset values
							phoneScreenIntrwDetailsTO.setItiNbr(ps.getItiNbr());
							phoneScreenIntrwDetailsTO.setPhnScrnDate(ps.getPhnScrnDate());
							phoneScreenIntrwDetailsTO.setPhnScrnTime(ps.getPhnScrnTime());
							phoneScreenIntrwDetailsTO.setPhoneScreenStatusCode(phoneScreenStatus);
							phoneScreenIntrwDetailsTO.setOverAllStatus(phoneScreenStatus);
							phoneScreenIntrwDetailsTO.setPhoneScreenStatusTimestamp(ps.getPhoneScreenStatusTimestamp());
							phoneScreenIntrwDetailsTO.setPhnScreener(ps.getPhnScreener());
							phoneScreenIntrwDetailsTO.setYnstatus(ps.getYnstatus());
							phoneScreenIntrwDetailsTO.setNote(ps.getNote());
							phoneScreenIntrwDetailsTO.setMinResList(ps.getMinResList());
							phoneScreenIntrwDetailsTO.setContactHistoryTxt(ps.getContactHistoryTxt());
							phoneScreenIntrwDetailsTO.setPhoneScreenDispositionCode(ps.getPhoneScreenDispositionCode());
						}

						// Create the phone screen data for each of the
						// candidates.
						itiUpdate = new ITIUpdateRequest();
						itiUpdate.setInsertUpdate(INSERT);

						phoneScreenIntrwDetailsTO.setCndtNbr(temp.getSsnNbr());
						phoneScreenIntrwDetailsTO.setAid(temp.getAid());
						phoneScreenIntrwDetailsTO.setName(temp.getName());
						phoneScreenIntrwDetailsTO.setReqCalId(temp.getReqCalId());// REQN_CAL_ID
						
						
						logger.info("phoneScreenIntrwDetailsTO ");
						logger.info("getAid "+phoneScreenIntrwDetailsTO.getAid());
						logger.info("getAIMSDisp "+phoneScreenIntrwDetailsTO.getAIMSDisp());
						logger.info("getCanPhn "+phoneScreenIntrwDetailsTO.getCanPhn());
						logger.info("getCanStatus "+phoneScreenIntrwDetailsTO.getCanStatus());
						logger.info("getCndDeptNbr "+phoneScreenIntrwDetailsTO.getCndDeptNbr());
						logger.info("getCndStrNbr "+phoneScreenIntrwDetailsTO.getCndStrNbr());
						logger.info("getCndtNbr "+phoneScreenIntrwDetailsTO.getCndtNbr());
						logger.info("getContactHistoryTxt "+phoneScreenIntrwDetailsTO.getContactHistoryTxt());
						logger.info("getDetailTxt "+phoneScreenIntrwDetailsTO.getDetailTxt());
						logger.info("getEmailAdd "+phoneScreenIntrwDetailsTO.getEmailAdd());
						logger.info("getFillDt "+phoneScreenIntrwDetailsTO.getFillDt());
						logger.info("getHrgEvntLoc "+phoneScreenIntrwDetailsTO.getHrgEvntLoc());
						logger.info("getHrgStoreLoc "+phoneScreenIntrwDetailsTO.getHrgStoreLoc());
						logger.info("getInternalExternal "+phoneScreenIntrwDetailsTO.getInternalExternal());
						logger.info("getInterviewLocTypCd "+phoneScreenIntrwDetailsTO.getInterviewLocTypCd());
						logger.info("getInterviewLocTypCd "+phoneScreenIntrwDetailsTO.getInterviewStatusTime());
						logger.info("getPhoneScreenStatusTimestamp "+phoneScreenIntrwDetailsTO.getPhoneScreenStatusTimestamp());
						logger.info("getInterviewStatusCode "+phoneScreenIntrwDetailsTO.getInterviewStatusCode());
						logger.info("getIntrvSeqNbr "+phoneScreenIntrwDetailsTO.getIntrvSeqNbr());
						logger.info("getItiNbr "+phoneScreenIntrwDetailsTO.getItiNbr());
						logger.info("getName "+phoneScreenIntrwDetailsTO.getName());
						logger.info("getNamesOfInterviewers "+phoneScreenIntrwDetailsTO.getNamesOfInterviewers());
						logger.info("getPhnScreener "+phoneScreenIntrwDetailsTO.getPhnScreener());
						logger.info("getJob "+phoneScreenIntrwDetailsTO.getJob());
						logger.info("getJobTtl "+phoneScreenIntrwDetailsTO.getJobTtl());
						logger.info("getOverAllStatus "+phoneScreenIntrwDetailsTO.getOverAllStatus());
						logger.info("getPhoneScreenStatusCode "+phoneScreenIntrwDetailsTO.getPhoneScreenStatusCode());
						logger.info("getPhoneScreenStatus "+phoneScreenIntrwDetailsTO.getPhoneScreenStatus());
						logger.info("getPhoneScreenStatusDesc "+phoneScreenIntrwDetailsTO.getPhoneScreenStatusDesc());
						logger.info("getYnstatus "+phoneScreenIntrwDetailsTO.getYnstatus());
						logger.info("getTitle "+phoneScreenIntrwDetailsTO.getTitle());
						logger.info("getRscSchdFlg "+phoneScreenIntrwDetailsTO.getRscSchdFlg());
						logger.info("getReqNbr "+phoneScreenIntrwDetailsTO.getReqNbr());
						
						
						itiUpdate.setPhnScrnDtlTo(phoneScreenIntrwDetailsTO);
						res = updateInsertITIDetails(itiUpdate);
					}
				}

				phnScrIntrTo = delReqMgr.readRequisitionPhoneScreen(Integer.parseInt(createPhoneScreenRequest.getPhoneScreenIntrwDetailsTO()
						.getReqNbr()));

				itiDetRes.setITIDtlList(phnScrIntrTo);
				res.setItiDtRes(itiDetRes);
				res.setSuccessMsg(ITI_CREATE_SUCCESS);
				res.setStatus(SUCCESS_APP_STATUS);
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves createPhoneScreens method in ITIManager with output" + res);
		return res;
	}

	/**
	 * This method is used to return the Send Interview Materials screens by
	 * Store
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	@SuppressWarnings("unchecked")
	public Response getSendIntrwMaterialsDtlsByStore(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getSendIntrwMaterialsDtlsByStore method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			// Check for the existence of the store first.
			if(!LocationManager.isValidStoreNumber(summReq.getOrgID()))
			{
				logger.error(this + "The entered store is invalid");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + " store number: "
						+ summReq.getOrgID());
			}
			RetailStaffingITIDAO = new RetailStaffingITIDAO();

			itiDtlMap = RetailStaffingITIDAO.getSendIntrwMaterialDtlsByStore(summReq);

			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether coming from next or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				// Fetch set of records of detail phone screen from store based
				// on first record and Last record
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialsDtlsByStore method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the Send Interview Materials screens by
	 * District
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	@SuppressWarnings("unchecked")
	public Response getSendIntrwMaterialsDtlsByDist(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getSendIntrwMaterialsDtlsByDist method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();

			itiDtlMap = RetailStaffingITIDAO.getSendIntrwMaterialDtlsByDist(summReq);

			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether coming from next or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				// Fetch set of records of detail phone screen from store based
				// on first record and Last record
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialsDtlsByDist method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the Send Interview Materials screens by
	 * Region
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	@SuppressWarnings("unchecked")
	public Response getSendIntrwMaterialsDtlsByReg(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getSendIntrwMaterialsDtlsByReg method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();

			itiDtlMap = RetailStaffingITIDAO.getSendIntrwMaterialDtlsByReg(summReq);

			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether coming from next or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				// Fetch set of records of detail phone screen from store based
				// on first record and Last record
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialsDtlsByReg method in Manager with output as" + res);
		return res;

	}

	/**
	 * This method is used to return the Send Interview Materials screens by
	 * Division
	 * 
	 * @return Response
	 * @throws RetailStaffingException
	 */
	@SuppressWarnings("unchecked")
	public Response getSendIntrwMaterialsDtlsByDiv(SummaryRequest summReq) throws RetailStaffingException {

		logger.info(this + "Enters getSendIntrwMaterialsDtlsByDiv method in Manager ");
		Response res = null;
		RetailStaffingITIDAO RetailStaffingITIDAO = null;
		List<PhoneScreenIntrwDetailsTO> itiDtlList = null;
		ITIDetailResponse itiDtRes = null;
		Map<String, Object> itiDtlMap = null;
		try {
			res = new Response();
			RetailStaffingITIDAO = new RetailStaffingITIDAO();

			itiDtlMap = RetailStaffingITIDAO.getSendIntrwMaterialDtlsByDiv(summReq);

			itiDtlList = (List<PhoneScreenIntrwDetailsTO>) itiDtlMap.get(ITI_DTL_LIST);
			if (itiDtlList == null || itiDtlList.size() <= 0) {
				// Check whether coming from next or prev click
				if (summReq.getPaginationInfoTO() != null && !Boolean.parseBoolean(summReq.getPaginationInfoTO().getIsFirstHit())) {
					logger.error(this + "No further records available");
					throw new RetailStaffingException(RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE);
				} else {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}
			}
			itiDtRes = new ITIDetailResponse();
			itiDtRes.setITIDtlList(itiDtlList);
			res.setItiDtRes(itiDtRes);
			Map<String, Object> paginationToken = (Map<String, Object>) itiDtlMap.get(PAGINATION_TOKEN);
			if (paginationToken != null) {
				Map<String, Object> recordDet = null;
				PagingRecordInfo recordInfo = null;
				// Fetch set of records of detail phone screen from store based
				// on first record and Last record
				if (paginationToken.get("FIRST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("FIRST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setFirstRecordInfo(recordInfo);
				}
				if (paginationToken.get("LAST_RECORD") != null) {
					recordDet = (Map<String, Object>) paginationToken.get("LAST_RECORD");
					recordInfo = new PagingRecordInfo();
					recordInfo.setUpdatedTimestamp(Util.converTimeStampTimeStampTO((java.sql.Timestamp) recordDet.get("LAST_UPD_TS")));
					recordInfo.setId(((Integer) recordDet.get("HR_PHN_SCRN_ID")).toString());
					res.setSecondRecordInfo(recordInfo);
				}
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getSendIntrwMaterialsDtlsByDiv method in Manager with output as" + res);
		return res;

	}
}

package com.homedepot.hr.hr.retailstaffing.model;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.homedepot.hr.hr.retailstaffing.dao.IntervwResultsDAO;
import com.homedepot.hr.hr.retailstaffing.dao.PhoneScreenDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO.ReadActiveRequisitionsByStoreTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplIntervwByCandJobReqTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplIntervwQuestTO;
import com.homedepot.hr.hr.retailstaffing.dto.BackgroundCheckDtlsTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDTInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateMinorInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ComboOptionsSortTO;
import com.homedepot.hr.hr.retailstaffing.dto.ComboOptionsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadHumanResourcesStoreEmploymentRequisitionDTO;
import com.homedepot.hr.hr.retailstaffing.util.EmailSender;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgroundCheckResultsDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgroundCheckSystemConsentByInputListDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadPersonProfilesByPersonIdTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.StateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitBgcCandidateDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateIntvwRltsCandIntvwDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplIntvwDtlsResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.FirstAdvantageBgcResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.InitiateDrugTestActionTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.InterviewQuestDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.NoInterviewReasonResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.OfferDeclineListResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.OfferMadeListResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.OfferResultListResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.StateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StructuredInterviewGuideListResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.catalina.realm.Profile;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.JavaBeanStream;
import com.homedepot.ta.aa.mutualauth.SSLRepertoire;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * This Class is used to have the business logic for Interview Results based on
 * Store selected. Methods: 01. getInterviewResultScreenDetails - Included
 * methods [getIntrwResultsRequisitionByStore()] 02.
 * getInterviewResultsCandidateDetails
 * 
 * @author sxs8544
 */

public class RetailStaffingInterviewResultsManager implements RetailStaffingConstants, DAOConstants {

	private static final Logger logger = Logger.getLogger(RetailStaffingInterviewManager.class);

	/**
	 * Service Event No: 01
	 * 
	 * This method is used to get the requisition details based on the valid
	 * Store number.
	 * 
	 * @param strNbr
	 * @return
	 * @throws RetailStaffingException
	 */
	public Response getInterviewResultsRequisitionDetails(String strNbr) throws RetailStaffingException {
		logger.info(this + "Enters getInterviewResultsRequisitionDetails method in Manager with input" + strNbr);

		Response res = null;
		RequisitionDetailResponse reqDtRes = null;
		List<RequisitionDetailTO> reqList = null;
		Map<String, Object> reqDetMap = null;

		try {
			res = new Response();

			if (strNbr != null && !strNbr.trim().equals(""))
			{
				// check for the existence of the location first
				if(!LocationManager.isValidLocationNumber(strNbr.trim()))
				{					
					logger.error(this + "The entered store is invalid");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_STORE_ERROR_CODE, INVALID_STORE_ERROR_MSG + "store number: "
							+ strNbr);
				}

				// Check for the existence of the Requisition details for the
				// entered Store
				reqDetMap = getIntrwResultsRequisitionByStore(strNbr);
				reqList = (List<RequisitionDetailTO>) reqDetMap.get(REQ_DTL_LIST);

				// Check whether if we are having Results or Not
				if (reqList == null || reqList.size() <= 0) {
					logger.error(this + "No Results for input");
					throw new RetailStaffingException(RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE);
				}// end of if (reqList == null || reqList.size() <= 0) {

				reqDtRes = new RequisitionDetailResponse();
				reqDtRes.setReqDtlList(reqList);
				res.setReqDtlList(reqDtRes);

				//Check for Electronic COC Drug Test Pilot.
				res.setShowDrugTestPanel(getDrugTestPilotResult(strNbr));
				
				//Sets the Drug Test Type for the entered location.
				res.setDrugTestTypeForLoc(RetailStaffingRequisitionDAO.readLocDTStatusIsConv(strNbr));
				
				//Check for Parental Consent for Minor's Pilot.
				res.setShowMinorConsentPanel(getMinorConsentPilotResult(strNbr));
        
				//Check for New Electronic BGC Consent and BGC Order
				res.setShowNewBGCPanel(Boolean.toString(isBgcRealTimeOrder(strNbr)));
				
			} // End of if (strNbr != null && !strNbr.trim().equals("")) {
			else {
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		logger.info(this + "Leaves getInterviewResultsRequisitionDetails method in Manager with output as" + res);

		return res;

	} // end of public Response getInterviewResultsRequisitionDetails(String

	// strNbr) throws RetailStaffingException {

	/**
	 * Service Event No: 02
	 * 
	 * This method is used to get the Candidate Information details based on the
	 * valid Requisition number Selected.
	 * 
	 * @param Requsition
	 *            Number - reqNbr
	 * @return
	 * @throws RetailStaffingException
	 */
	public Response getInterviewResultsCandidateDetails(String reqNbr) throws RetailStaffingException {
		logger.debug(this + "Enters getInterviewResultsCandidateDetails method in Manager with input :" + reqNbr);

		Response res = null;
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();

		CandidateDetailResponse canDetRes = new CandidateDetailResponse();

		RequisitionDetailTO reqDtl = null;
		List<RequisitionDetailTO> reqList = null;
		List<CandidateDetailsTO> candDetTo = null;

		try {
			res = new Response();
			if (reqNbr != null && !reqNbr.trim().equals("")) {
				// get Store Number from DAO
				reqList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(reqNbr));

				if (reqList != null && !reqList.isEmpty() && reqList.size() > 0) {
					reqDtl = reqList.get(0);
					// to fetch candidate details for requisition screen
					candDetTo = delReqMgr.readRequisitionCandidate_DAO20(Integer.parseInt(reqNbr), reqDtl.getStore());
					if (candDetTo != null && candDetTo.size() > 0) {
						logger.debug("To fetch candidate details for loop size :" + candDetTo.size());
						canDetRes.setCndDtlList(candDetTo);
						res.setCanDtRes(canDetRes);
					}
				} // end of if (reqList != null && !reqList.isEmpty() &&
				// reqList.size() > 0)

			} // End of if(reqNbr != null && !reqNbr.trim().equals("")){
			else {
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INTVW_RESULT_SCRN_CAND_FETCH);
			}

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		logger.info(this + "Leaves getInterviewResultsCandidateDetails method in Manager with output as of Requisition no: " + reqNbr);

		return res;

	} // end of public Response getInterviewResultsCandidateDetails(String

	// reqNbr) throws RetailStaffingException {

	/**
	 * DAO Support Method Service Event No: 01
	 * 
	 * This method is used to get the Requisition Information details based on
	 * the valid Store number input without Pagination.
	 * 
	 * @param Requsition
	 *            Number - strNbr [Store Number]
	 * @return
	 * @throws RetailStaffingException
	 */
	public Map<String, Object> getIntrwResultsRequisitionByStore(final String strNbr) throws RetailStaffingException {

		logger.info(this + "Enters getIntrwResultsRequisitionByStore method in DAO ");

		final List<RequisitionDetailTO> reqDetList = new ArrayList<RequisitionDetailTO>();
		final Map<String, Object> reqDetMap = new HashMap<String, Object>();
		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemStoreNumber(strNbr);
		inputsList.setTabno(TABNO_DEPT_NO);
		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readActiveRequisitionsByStore");

		try {
			// Iterate through the records and populate the list
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader() {

				RequisitionDetailTO reqDet = null;

				public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

					while (results.next()) {
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
						reqDet.setStore(strNbr);
						reqDet.setHumanResourcesSystemRegionCode(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						reqDet.setHumanResourcesSystemOperationsGroupCode(StringUtils.trim(results
								.getString("humanResourcesSystemOperationsGroupCode")));
						reqDet.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
						reqDetList.add(reqDet);
					}
				}
			});

			reqDetMap.put(REQ_DTL_LIST, reqDetList);

		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_REQUISITION_DETAILS_ERROR_CODE, FETCHING_REQUISITION_DETAILS_ERROR_MSG + "storeid: " + strNbr,
					e);
		}

		logger.info(this + "Leaves getIntrwResultsRequisitionByStore method in Manager with output as" + reqDetList);
		return reqDetMap;
	}

	public Response getInterviewResultsLoadScreen() throws RetailStaffingException {
		Response res = null;
		NoInterviewReasonResponse noIntvwRes = null;
		OfferMadeListResponse offerMadeListRes = null;
		OfferResultListResponse offerResultListRes = null;
		OfferDeclineListResponse offerDeclineListRes = null;

		StructuredInterviewGuideListResponse sigListRes = new StructuredInterviewGuideListResponse();
		List<ComboOptionsSortTO> cboOptions = null;

		StateDetailResponse stDetRes = new StateDetailResponse();
		List<StateDetailsTO> states = null;
		
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		IntervwResultsDAO intvwRtlsMgr = new IntervwResultsDAO();

		try {
			res = new Response();
			// No Interview Reason Code List
			noIntvwRes = getNoInterviewReasons();
			res.setNoIntvwReasonRes(noIntvwRes);

			// Offer Made Code List
			offerMadeListRes = getOfferMadeList();
			res.setOfferMadeListRes(offerMadeListRes);

			// Offer Results Code List
			offerResultListRes = getOfferResultList();
			res.setOfferResultListRes(offerResultListRes);

			// Offer Decline Code List
			offerDeclineListRes = getOfferDeclineList();
			res.setOfferDeclineListRes(offerDeclineListRes);

			// Structured Interview Guide List
			// This will be pulled from the database
			// Object = StructuredInterviewGuideList
			cboOptions = intvwRtlsMgr.readHrOrgParmSigList();
			if (cboOptions != null && cboOptions.size() > 0) {
				Collections.sort((ArrayList<ComboOptionsSortTO>) cboOptions, ComboOptionsSortTO.SortFieldComparator);
				sigListRes.setDtlList(cboOptions);
				res.setStructuredInterviewGuideListRes(sigListRes);
			}

			// States
			states = delReqMgr.readStateList();
			if (states != null && states.size() > 0) {
				stDetRes.setStrDtlList(states);
				res.setStateDtRes(stDetRes);
			}
			
		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return res;
	}

	public NoInterviewReasonResponse getNoInterviewReasons() throws RetailStaffingException {
		NoInterviewReasonResponse optionsRes = null;
		List<ComboOptionsTO> optionsDtlList = null;
		try {
			String comboOptions = Util.getNoInterviewReasons();
			logger.info("The No Interview Reasons from properties is: " + comboOptions);
			if (comboOptions != null && comboOptions.length() > 0) {
				String[] comboOptionsToks = comboOptions.split(",");
				if (comboOptionsToks != null && comboOptionsToks.length > 0) {
					optionsRes = new NoInterviewReasonResponse();
					optionsDtlList = new ArrayList<ComboOptionsTO>();
					ComboOptionsTO stsDtl = null;
					for (int i = 0; i < comboOptionsToks.length; i++) {
						String[] statusTokDet = comboOptionsToks[i].split(":");
						if (statusTokDet != null && statusTokDet.length >= 2) {
							stsDtl = new ComboOptionsTO();
							stsDtl.setData(statusTokDet[0]);
							stsDtl.setLabel(statusTokDet[1]);
							optionsDtlList.add(stsDtl);
						}
					}
					optionsRes.setDtlList(optionsDtlList);
				}
			}
		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return optionsRes;
	}

	public OfferMadeListResponse getOfferMadeList() throws RetailStaffingException {
		OfferMadeListResponse optionsRes = null;
		List<ComboOptionsTO> optionsDtlList = null;
		try {
			String comboOptions = Util.getOfferMade();
			logger.info("The Offer made from properties is: " + comboOptions);
			if (comboOptions != null && comboOptions.length() > 0) {
				String[] comboOptionsToks = comboOptions.split(",");
				if (comboOptionsToks != null && comboOptionsToks.length > 0) {
					optionsRes = new OfferMadeListResponse();
					optionsDtlList = new ArrayList<ComboOptionsTO>();
					ComboOptionsTO stsDtl = null;
					for (int i = 0; i < comboOptionsToks.length; i++) {
						String[] statusTokDet = comboOptionsToks[i].split(":");
						if (statusTokDet != null && statusTokDet.length >= 2) {
							stsDtl = new ComboOptionsTO();
							stsDtl.setData(statusTokDet[0]);
							stsDtl.setLabel(statusTokDet[1]);
							optionsDtlList.add(stsDtl);
						}
					}
					optionsRes.setDtlList(optionsDtlList);
				}
			}
		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return optionsRes;
	}

	public OfferResultListResponse getOfferResultList() throws RetailStaffingException {
		OfferResultListResponse optionsRes = null;
		List<ComboOptionsTO> optionsDtlList = null;
		try {
			String comboOptions = Util.getOfferResult();
			logger.info("The Offer Results from properties is: " + comboOptions);
			if (comboOptions != null && comboOptions.length() > 0) {
				String[] comboOptionsToks = comboOptions.split(",");
				if (comboOptionsToks != null && comboOptionsToks.length > 0) {
					optionsRes = new OfferResultListResponse();
					optionsDtlList = new ArrayList<ComboOptionsTO>();
					ComboOptionsTO stsDtl = null;
					for (int i = 0; i < comboOptionsToks.length; i++) {
						String[] statusTokDet = comboOptionsToks[i].split(":");
						if (statusTokDet != null && statusTokDet.length >= 2) {
							stsDtl = new ComboOptionsTO();
							stsDtl.setData(statusTokDet[0]);
							stsDtl.setLabel(statusTokDet[1]);
							optionsDtlList.add(stsDtl);
						}
					}
					optionsRes.setDtlList(optionsDtlList);
				}
			}
		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return optionsRes;
	}

	public OfferDeclineListResponse getOfferDeclineList() throws RetailStaffingException {
		OfferDeclineListResponse optionsRes = null;
		List<ComboOptionsTO> optionsDtlList = null;
		try {
			String comboOptions = Util.getOfferDeclineReason();
			logger.info("The Offer Decline Reasons from properties is: " + comboOptions);
			if (comboOptions != null && comboOptions.length() > 0) {
				String[] comboOptionsToks = comboOptions.split(",");
				if (comboOptionsToks != null && comboOptionsToks.length > 0) {
					optionsRes = new OfferDeclineListResponse();
					optionsDtlList = new ArrayList<ComboOptionsTO>();
					ComboOptionsTO stsDtl = null;
					for (int i = 0; i < comboOptionsToks.length; i++) {
						String[] statusTokDet = comboOptionsToks[i].split(":");
						if (statusTokDet != null && statusTokDet.length >= 2) {
							stsDtl = new ComboOptionsTO();
							stsDtl.setData(statusTokDet[0]);
							stsDtl.setLabel(statusTokDet[1]);
							optionsDtlList.add(stsDtl);
						}
					}
					optionsRes.setDtlList(optionsDtlList);
				}
			}
		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return optionsRes;
	}

	/*
	 * Service Event No: 03
	 * 
	 * To get Interview Screen Details for Selected Candidate informations's
	 */
	public Response getCandidateInterviewQuestionDetails(String applcantID, String reqNbr, String strNbr, String deptNbr, String jobTtl,
			String applicantType, String organization) throws RetailStaffingException {
		logger.info(this + "Enters getCandidateInterviewQuestionDetails method in Manager Layer ");

		Response res = null;
		IntervwResultsDAO intervwReqMgr = new IntervwResultsDAO();
		ApplIntervwByCandJobReqTO intrDtl = null;
		CandidateInfoTO candinfoDtl = null;
		CandidateDTInfoTO candDTinfoDtl = null;
		BackgroundCheckDtlsTO backChkDtl = null;
		ReadBackgroundCheckSystemConsentByInputListDTO licenseInfo = null;
		BackgroundCheckDtlsTO tempBackChkDtl = null;

		List<ApplIntervwByCandJobReqTO> interwList = null;
		List<CandidateInfoTO> candInfoTo = null;
		List<CandidateDTInfoTO> drugTestTO = null;
		List<ApplIntervwQuestTO> intervwQuestList = null;
		List<ReadPersonProfilesByPersonIdTO> intCandCurrentJob = null;
		List<ReadBackgroundCheckSystemConsentByInputListDTO> licenseInfoList = null;

		InterviewQuestDetailResponse intrwDetRes = new InterviewQuestDetailResponse();
		ApplIntvwDtlsResponse intrDtlRes = new ApplIntvwDtlsResponse();

		try {
			res = new Response();

			if (isBgcRealTimeOrder(strNbr)) {
				res = getCandidateInterviewQuestionDetailsNewBgcProcess(applcantID, reqNbr, strNbr, deptNbr, jobTtl, applicantType, organization);
			} else {
				
			if (reqNbr != null && !reqNbr.trim().equals("")) {
				// get Interview UserId from DAO
				interwList = intervwReqMgr.readApplicantInterviewByCandidateAndJobRequisition(applcantID, Integer.parseInt(reqNbr));

				if (interwList != null && !interwList.isEmpty() && interwList.size() > 0) {
					intrDtl = interwList.get(0);

					// to fetch intervwQuestList details for Interview Results
					// screen
					if (intrDtl.getInterviewerUserId() != null && intrDtl.getInterviewerUserId().length() > 0) {

						logger.debug("readApplicantInterviewQuestion  - getInterviewerUserId: 1. " + intrDtl.getInterviewerUserId());
						logger.debug("readApplicantInterviewQuestion  - applcantID 			: 2. " + applcantID);
						logger.debug("readApplicantInterviewQuestion  - getInterviewDate	: 3. " + intrDtl.getInterviewDate());
						logger.debug("Result Indicator = " + intrDtl.getInterviewResultIndicator());

						intrDtlRes.setInterviewerUserId(intrDtl.getInterviewerUserId());
						// Need to format Date
						SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat outFmt = new SimpleDateFormat("MM/dd/yyyy");
						if (intrDtl.getInterviewDate() != null) {
							intrDtlRes.setInterviewDate(outFmt.format(inFmt.parse(intrDtl.getInterviewDate().toString())));
						}
						res.setInterviewerDtlsRes(intrDtlRes);

						intervwQuestList = intervwReqMgr.readApplicantInterviewQuestion(applcantID, intrDtl.getInterviewerUserId().toString(),
								intrDtl.getInterviewDate());

						if (intervwQuestList != null && intervwQuestList.size() > 0) {
							logger.debug("To fetch intervwQuestList details for loop size :" + intervwQuestList.size());
							intrwDetRes.setIntrwDtlList(intervwQuestList);
							res.setIntrwDtRes(intrwDetRes);
						}

					}// if(intrDtl.getInterviewerUserId() != null &&
					// intrDtl.getInterviewerUserId().length() > 0) {

				} // end of (interwList != null && !interwList.isEmpty() &&
				// interwList.size() > 0)

				// to fetch selected candidate details for Interview Results
				// screen
				candInfoTo = intervwReqMgr.readHumanResourcesStoreRequisitionCandidateDAO20(strNbr, Integer.parseInt(reqNbr), applcantID);
				drugTestTO = intervwReqMgr.readApplicantDrugTestOrderDAO20(applcantID, Integer.parseInt(reqNbr));
				float candAge = intervwReqMgr.readHumanResourcesStoreRequisitionCandidateAgeDAO20(applcantID);
				
				if (candAge < 18.00 && getMinorConsentPilotResult(strNbr).equalsIgnoreCase("true")) {
					candInfoTo.get(0).setIsCandMinorFlag(true);
					List<CandidateMinorInfoTO> getMinorFormCompletions = intervwReqMgr.readHumanResourcesParentalConsentFormsForMinorsDAO20(applcantID, Integer.parseInt(reqNbr));
				
					for(CandidateMinorInfoTO minorCandidateFormsData:getMinorFormCompletions)
					{
						if(minorCandidateFormsData.getParentalConsentFormDocsExistingRows()>0) {
							
							if(minorCandidateFormsData.getParentalConsentFormDocsIncomplete()==0) {
								candInfoTo.get(0).setParentalConsentFormsComplete("complete");
							} else {
								candInfoTo.get(0).setParentalConsentFormsComplete("incomplete");
							}
						} else {
							candInfoTo.get(0).setParentalConsentFormsComplete("notstarted");
						}
					}
				} else {
					candInfoTo.get(0).setIsCandMinorFlag(false);
				}
				
				if (candInfoTo != null && candInfoTo.size() > 0) {
					candinfoDtl = candInfoTo.get(0);
					res.setCandinfoDtRes(candinfoDtl);
				}

				
				if (drugTestTO != null && drugTestTO.size() > 0) {
					candDTinfoDtl = drugTestTO.get(0);
					res.setCandDTinfoDtRes(candDTinfoDtl);
				}
				
				// Background Check plus is a driving position
				backChkDtl = new BackgroundCheckDtlsTO();

				List<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO> newJobReqmntList = null;
				List<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO> currentJobReqmntList = null;
				List<ReadBackgroundCheckResultsDTO> currentIntCandBackgroundResultList = null;

				// See if position is a Driving Position, for Internals.  Determine if any Background Checks are needed
				if (candInfoTo != null && candInfoTo.size() > 0) {
					// get New Job Requirement List, Component
					newJobReqmntList = intervwReqMgr.readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(strNbr, jobTtl, deptNbr);
					backChkDtl.setHumanResourcesStoreTypeCode(StringUtils.trim(newJobReqmntList.get(0).getHumanResourcesStoreTypeCode()));

					if ("INT".equalsIgnoreCase(applicantType)) {
						// Get Internal Candidate current Job Info
						intCandCurrentJob = intervwReqMgr.readPersonProfilesByPersonId(applcantID);
						// Get Internal Candidate current job requirements for
						// Background Check
						currentJobReqmntList = intervwReqMgr.readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(intCandCurrentJob
								.get(0).getOrganization1(), intCandCurrentJob.get(0).getJobTitleId(), intCandCurrentJob.get(0).getOrganization2());
						if (currentJobReqmntList != null && currentJobReqmntList.size() > 0) {
							for (int i=0; i < currentJobReqmntList.size(); i++) {
								if (currentJobReqmntList.get(i).getBackgroundCheckSystemComponentId() == 3) {
									backChkDtl.setAlreadyInDrivingPos(true);
									break;
								} else {
									backChkDtl.setAlreadyInDrivingPos(false);
								}
							}
							if (backChkDtl.isAlreadyInDrivingPos()) {
								logger.debug("***Candidate already in driving position***");
							} else {
								logger.debug("***Candidate NOT already in driving position***");
							}
						} else {
							logger.debug("Current Job Requirements Not Found");
						}
					}


					if ("INT".equalsIgnoreCase(applicantType)) {
						// Internal Position
						// Check for driving position, will be component id of 3
						for (int i=0; i < newJobReqmntList.size(); i++) {
							if (newJobReqmntList.get(i).getBackgroundCheckSystemComponentId() == 3) {
								backChkDtl.setDrivingPos(true);
								backChkDtl.setBackgroundPackageNeeded(newJobReqmntList.get(i).getBackgroundCheckSystemPackageId());
								break;
							} else {
								backChkDtl.setDrivingPos(false);
							}
						}
						logger.debug(String.format("Background Package Needed:%1$s", backChkDtl.getBackgroundPackageNeeded()));
						if (backChkDtl.isDrivingPos()) {
							logger.debug("***This is driving position***");
						} else {
							logger.debug("***This is NOT driving position***");
						}
						if (backChkDtl.isDrivingPos()) {
							// Check Internal candidate to see if they need
							// another
							// background check for new position
							// Compare currentJobReqmntList with
							// newJobReqmntList to
							// determine if any other components are needed

							StringBuffer neededComponents = new StringBuffer(10);
							boolean needNewComponent = true;
							for (int i = 0; i < newJobReqmntList.size(); i++) {
								int newJobComponent = newJobReqmntList.get(i).getBackgroundCheckSystemComponentId();
								for (int j = 0; j < currentJobReqmntList.size(); j++) {
									if (newJobComponent == currentJobReqmntList.get(j).getBackgroundCheckSystemComponentId()) {
										needNewComponent = false;
										break;
									} else {
										needNewComponent = true;
									}
								}
								if (needNewComponent) {
									neededComponents.append(newJobComponent).append(",");
									needNewComponent = true;
								}
							}

							if (neededComponents.length() > 0) {
								//System.out.println("Needed Components = " + neededComponents.toString());
								// Check internal to see if they have the needed
								// components
								currentIntCandBackgroundResultList = intervwReqMgr.readBackgroundCheckResults(applcantID);
								boolean hasNeededComponent = false;
								int hasNeededComponentIndex = 0;
								// Loop through to see if they have the needed
								// component
								logger.debug("Size of currentIntCandBackgroundResultList = " + currentIntCandBackgroundResultList.size());
								if (currentIntCandBackgroundResultList.size() < 0) {
									logger.debug("currentIntCandBackgroundResultList was empty, not Candidate Background");
									//System.out.println("Background Check Needed");
									backChkDtl.setNeedsBackgroundCheck(true);
								}

								for (int i = 0; i < currentIntCandBackgroundResultList.size(); i++) {
									// Driving Position
									if (currentIntCandBackgroundResultList.get(i).getBackgroundCheckSystemComponentId() == 3) {
										hasNeededComponent = true;
										hasNeededComponentIndex = i;
										break;
									}
								}

								// Make sure that background check (MVR) is still valid if they
								// already one
								if (hasNeededComponent) {
									int alertStatusCode = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getBackgroundCheckSystemAlertStatusCode();
									Date bgcCompCmpltDt = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getBackgroundCheckSystemComponentCompletedDate();
									int favResltEffDays = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getFavorableResultEffectiveDays();
									int unFavResltEffDays = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getUnfavorableResultEffectiveDays();

									if (alertStatusCode == 3) {
										// Green
										logger.debug("Green Alert Status Code");
										if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, favResltEffDays)) {
											logger.debug("No Background Needed");
											backChkDtl.setNeedsBackgroundCheck(false);
										} else {
											logger.debug("Background Needed");
											backChkDtl.setNeedsBackgroundCheck(true);
										}
									} else if (alertStatusCode == 1) {
										// Red
										logger.debug("Red Alert Status Code");
										if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
											logger.debug("No Background Needed");
											backChkDtl.setNeedsBackgroundCheck(false);
										} else {
											logger.debug("Background Needed");
											backChkDtl.setNeedsBackgroundCheck(true);
										}
									} else if (alertStatusCode == 2) {
										// Yellow
										logger.debug("Yellow Alert Status Code");
										if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == null) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										} else if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == 1) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										} else if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == 3) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, favResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										}
									}
								} else {
									// Did not find background information on
									// Candidate
									logger.debug("Background Needed");
									backChkDtl.setNeedsBackgroundCheck(true);
								}
							} else {
								//Internal Driving, Has all components
								if (backChkDtl.getBackgroundPackageNeeded() == 24) {
									//May have all the needed components, but if Asset Protection have to order component 7 again.
									logger.debug("Asset Protection Background Needed");
									backChkDtl.setNeedsBackgroundCheck(true);									
								} else {
									logger.debug("NO Background Needed");
									backChkDtl.setNeedsBackgroundCheck(false);
								}	
							}
						} else {
							// Internal, Not a Driving Position
							logger.debug("NO Background Needed");
							backChkDtl.setNeedsBackgroundCheck(false);
						}
					} else {
						// External Candidate
						logger.debug("This is an External Applicant");
						// Determine what components are needed
						StringBuffer neededComponents = new StringBuffer(10);
						for (int i = 0; i < newJobReqmntList.size(); i++) {
							int newJobComponent = newJobReqmntList.get(i).getBackgroundCheckSystemComponentId();
							logger.debug("New Component = " + newJobComponent);
							logger.debug("Package ID = " + newJobReqmntList.get(i).getBackgroundCheckSystemPackageId());
							backChkDtl.setBackgroundPackageNeeded(newJobReqmntList.get(i).getBackgroundCheckSystemPackageId());
							neededComponents.append(newJobComponent).append(",");
						}

						//For ATS mts1876
						// Check for driving position, will be component id of 3
						for (int i = 0; i < newJobReqmntList.size(); i++) {
							ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO tempDto2 = newJobReqmntList.get(i);
							if (tempDto2.getBackgroundCheckSystemComponentId() == 3) {
								backChkDtl.setDrivingPos(true);
								break;
							} else {
								backChkDtl.setDrivingPos(false);
							}
						}
						if (backChkDtl.isDrivingPos()) {
							logger.debug("***This is driving position***");
						} else {
							logger.debug("***This is NOT driving position***");
						}
						//****************************************************
						
						
						if (neededComponents.length() > 0) {
							logger.debug("Needed Components = " + neededComponents.toString());
							backChkDtl.setBackgroundComponentsNeeded(neededComponents.toString());
							// Check internal to see if they have the needed
							// components
							currentIntCandBackgroundResultList = intervwReqMgr.readBackgroundCheckResults(applcantID);
							boolean hasNeededComponent = false;
							int hasNeededComponentIndex = 0;
							// Loop through to see if they have the needed
							// component
							logger.debug("Size of currentIntCandBackgroundResultList = " + currentIntCandBackgroundResultList.size());
							if (currentIntCandBackgroundResultList.size() < 0) {
								logger.debug("currentIntCandBackgroundResultList was empty, no Candidate Background");
								backChkDtl.setNeedsBackgroundCheck(true);
							} else {
								//  ????? Do I Need
								for (int k = 0; k < currentIntCandBackgroundResultList.size(); k++) {
									if (currentIntCandBackgroundResultList.get(k).getBackgroundCheckSystemComponentId() == 3) {
										hasNeededComponent = true;
										hasNeededComponentIndex = k;
										break;
									}
								}

								if (hasNeededComponent) {
									logger.debug("Has Needed Components!");
									int alertStatusCode = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getBackgroundCheckSystemAlertStatusCode();
									Date bgcCompCmpltDt = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getBackgroundCheckSystemComponentCompletedDate();
									int favResltEffDays = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getFavorableResultEffectiveDays();
									int unFavResltEffDays = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getUnfavorableResultEffectiveDays();

									if (alertStatusCode == 3) {
										// Green
										logger.debug("Green Alert Status Code");
										if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, favResltEffDays)) {
											logger.debug("No Background Needed");
											backChkDtl.setNeedsBackgroundCheck(false);
										} else {
											logger.debug("Background Needed");
											backChkDtl.setNeedsBackgroundCheck(true);
										}
									} else if (alertStatusCode == 1) {
										// Red
										logger.debug("Red Alert Status Code");
										if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
											logger.debug("No Background Needed");
											backChkDtl.setNeedsBackgroundCheck(false);
										} else {
											logger.debug("Background Needed");
											backChkDtl.setNeedsBackgroundCheck(true);
										}
									} else if (alertStatusCode == 2) {
										// Yellow
										logger.debug("Yellow Alert Status Code");
										if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == null) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										} else if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == 1) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										} else if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == 3) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, favResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										}
									}

								} else {
									// Did not find background information on Candidate
									logger.debug("Background Needed");
									backChkDtl.setNeedsBackgroundCheck(true);
								}
							}
						}
					}

					// Get License Information if it exists for Internals for Driving Positions Only
					if ("INT".equalsIgnoreCase(applicantType) && backChkDtl.isDrivingPos()) {
						licenseInfoList = intervwReqMgr.readBackgroundCheckSystemConsentByInputList(applcantID);
						if (licenseInfoList != null && licenseInfoList.size() > 0) {
							licenseInfo = new ReadBackgroundCheckSystemConsentByInputListDTO();
							licenseInfo.setDriverLicenseNumber(StringUtils.trim(licenseInfoList.get(0).getDriverLicenseNumber()));
							licenseInfo.setDriverLicenseStateCode(StringUtils.trim(licenseInfoList.get(0).getDriverLicenseStateCode()));
							res.setLicenseInfoRes(licenseInfo);
						}
					}
					
					// Check for Background Consent
					//backChkDtl.setHasSignedConsent(checkForBackgroundConsent(applcantID));
					//res.setBackgroundChkDtlRes(backChkDtl);
					
					if (!isBgcRealTimeOrder(strNbr)) {
						// Check for Background Consent
						backChkDtl.setHasSignedConsent(checkForBackgroundConsent(applcantID));
						res.setBackgroundChkDtlRes(backChkDtl);
					} else {
						//New Check for Background Consent
						tempBackChkDtl = checkForBackgroundConsentElectronic(applcantID);
						if (tempBackChkDtl == null) {
							backChkDtl.setHasSignedConsent(false);
						} else {
							backChkDtl.setHasSignedConsent(true);
							backChkDtl.setCnsntSigDt(tempBackChkDtl.getCnsntSigDt());
							backChkDtl.setConsentStatCd(tempBackChkDtl.getConsentStatCd());
							backChkDtl.setConsentCompleteTs(tempBackChkDtl.getConsentCompleteTs());
							backChkDtl.setAplcntLKey(tempBackChkDtl.getAplcntLKey());
						}
						
						//backChkDtl.setHasSignedConsent(checkForBackgroundConsent(applcantID));
						res.setBackgroundChkDtlRes(backChkDtl);
					}
					
					
					
					//Check to see if applicant has SSN is in XREF Table.  Not for Internals
					if ("EXT".equalsIgnoreCase(applicantType)) {
						String returnedSSN = IntervwResultsDAO.getCandSsnIfExists(applcantID);
						if (returnedSSN != null) {
							//Check if Candidate has CPD Form Completed
							BackgroundCheckDtlsTO tempBgcDtls = IntervwResultsDAO.getCpdFormInformation(applcantID);
							if (tempBgcDtls != null) {
								//Make sure that DOB is not default 01-01-0001, candidateInitialsSignatureValue
								//   , documentSignatureTimestamp, backgroundCheckAuthorizationFlag, and managerUserId is not null.
								//If any of these are missing or incorrect then set CPD Form complete to false
								if (tempBgcDtls.getDateOfBirth() == null || tempBgcDtls.getDateOfBirth().equals("0001-01-01") ||
										tempBgcDtls.getCandidateInitialsSignatureValue() == null ||	tempBgcDtls.getCandidateInitialsSignatureValue().equals("") ||
										tempBgcDtls.getDocumentSignatureTimestamp() == null ||
										tempBgcDtls.getManagerUserId() == null || tempBgcDtls.getManagerUserId().equals("")) {
									candinfoDtl.setCpdFormComplete(false);	
								} else {
									candinfoDtl.setCpdFormComplete(true);
									//Check for Rehire Eligible 
									CandidateInfoTO tempCand = IntervwResultsDAO.getRehireEligible(returnedSSN);
									candinfoDtl.setRehireEligible(tempCand.isRehireEligible());
									//Set the data needed for message when a candidate is NOT Rehire Eligible 
									if (!tempCand.isRehireEligible()) {
										candinfoDtl.setTermLoc(tempCand.getTermLoc());
										candinfoDtl.setTermRsn(tempCand.getTermRsn());
										candinfoDtl.setTermDt(tempCand.getTermDt());
									} // End - if (!tempCand.isRehireEligible()) {
								} //  End - else 
							} // End - if (tempBgcDtls != null) {
						} else {
							candinfoDtl.setCpdFormComplete(false);
						} // End - else if (tempBgcDtls != null) {
					} else { // End - if ("EXT".equalsIgnoreCase(applicantType)) {
						//Internal Candidate
						candinfoDtl.setCpdFormComplete(true);
						candinfoDtl.setRehireEligible(true);
					} // End - else if ("EXT".equalsIgnoreCase(applicantType)) {
					
				} // End - if (candInfoTo != null && candInfoTo.size() > 0)
			} // End of if(reqNbr != null && !reqNbr.trim().equals(""))
			}
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return res;
	} // public Response getCandidateInterviewQuestionDetails() throws RetailStaffingException{

	public Response updateInterviewResultsCandidateIntvwDtls(final UpdateIntvwRltsCandIntvwDtlsRequest updIntvwReq, HttpServletRequest httpRequest) throws RetailStaffingException {
		logger.info(this + "Enters updateInterviewResultsCandidateIntvwDtls method in Manager with input" + updIntvwReq);

		Response res = null;
		IntervwResultsDAO intervwRltsMgr = new IntervwResultsDAO();
		String successMsg = "";
		boolean backgroundOrdered = false;
		boolean backgroundOrderedDMV = false;
		boolean backgroundOrderedAPDMV = false;
		boolean backgroundOrderedAP = false;
		boolean backgroundNewProcess = false;
		FirstAdvantageBgcResponse firstAdvantageBgcResponse;
		String externalBgcId = "";
		short bgcOrderStatus = 0;
		
		try {
			res = new Response();
			Profile profile = Profile.getCurrent();
			String createUserId = profile.getProperty(Profile.USER_ID).toUpperCase();
			
			// Interview Information Insert
			if (updIntvwReq != null && updIntvwReq.getCandidateInfoTo() != null && updIntvwReq.getInterviewInformationTo() != null) {
				if (updIntvwReq.getInterviewInformationTo().getUpdateIntvwDtls().equals("Y")
						&& updIntvwReq.getInterviewInformationTo().getWasInterviewCompleted().equals("Y")) {
					String applicantId = updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId().trim();
					String interviewerId = updIntvwReq.getInterviewInformationTo().getInterviewerName().trim();
					java.sql.Date interviewDt = Util.convertStringDate(updIntvwReq.getInterviewInformationTo().getDateOfIntvw().trim());
					String questId = "";
					String questRslt = "";
					String intvwRslt = updIntvwReq.getCandidateInfoTo().getInterviewStatusIndicator().trim();
					int reqNbr = updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber();

					//INSERT DOES AVAILABILITY MATCH QUESTION
					ApplIntervwQuestTO availabilityMatchAnswer = intervwRltsMgr.getApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, RetailStaffingConstants.AVAILABILITY_MATCH_QUESTION_INDICATOR);
					
					if(availabilityMatchAnswer != null){
						if(!availabilityMatchAnswer.getInterviewQuestionResultIndicator().trim().equalsIgnoreCase(updIntvwReq.getInterviewInformationTo().getDoesAvailabilityMatch())) {
							intervwRltsMgr.updateApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, RetailStaffingConstants.AVAILABILITY_MATCH_QUESTION_INDICATOR, updIntvwReq.getInterviewInformationTo().getDoesAvailabilityMatch());
						}
					} else {
						intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, RetailStaffingConstants.AVAILABILITY_MATCH_QUESTION_INDICATOR, updIntvwReq.getInterviewInformationTo().getDoesAvailabilityMatch());
					}
	
					// Insert Interview Results
					ApplIntervwByCandJobReqTO applIntervwByCandJobReqTO = intervwRltsMgr.getApplicantInterviewStatus(applicantId, interviewerId, interviewDt, reqNbr);
					if(applIntervwByCandJobReqTO != null){
						if(!applIntervwByCandJobReqTO.getInterviewResultIndicator().equalsIgnoreCase(updIntvwReq.getCandidateInfoTo().getInterviewStatusIndicator())){
							intervwRltsMgr.updateApplicantInterviewStatus(applicantId, interviewerId, interviewDt, intvwRslt, reqNbr);
						}
					} else {
						intervwRltsMgr.createApplicantInterview(applicantId, interviewerId, interviewDt, intvwRslt, reqNbr);
					}

					if("Y".equals(updIntvwReq.getInterviewInformationTo().getDoesAvailabilityMatch())){
							
						
						// Insert Sig Question
						if (updIntvwReq.getInterviewInformationTo().getSelectedSig() != null) {
							questId = updIntvwReq.getInterviewInformationTo().getSelectedSig();
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, "**");
						} else {
							logger.info("SIG Question Was NULL*****");
							throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE);
						}
						
						
	
						// Insert Each Question This needs to be built better....
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion1() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion1().equals("0")) {
							questId = "1";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion1());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion2() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion2().equals("0")) {
							questId = "2";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion2());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion3() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion3().equals("0")) {
							questId = "3";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion3());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion4() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion4().equals("0")) {
							questId = "4";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion4());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion5() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion5().equals("0")) {
							questId = "5";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion5());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion6() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion6().equals("0")) {
							questId = "6";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion6());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion7() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion7().equals("0")) {
							questId = "7";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion7());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion8() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion8().equals("0")) {
							questId = "8";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion8());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion9() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion9().equals("0")) {
							questId = "9";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion9());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
						if (updIntvwReq.getInterviewInformationTo().getIntvwQuestion10() != null
								&& !updIntvwReq.getInterviewInformationTo().getIntvwQuestion10().equals("0")) {
							questId = "10";
							questRslt = convertIntvwQuest(updIntvwReq.getInterviewInformationTo().getIntvwQuestion10());
							intervwRltsMgr.createApplicantInterviewQuestion(applicantId, interviewerId, interviewDt, questId, questRslt);
						}
					}
				}
			} // Interview Information Insert
			
			//Update the date that the Interview Results were 1st entered
			if (updIntvwReq != null && updIntvwReq.getCandidateInfoTo() != null && updIntvwReq.getCandidateInfoTo().getSubmitInterviewResultsDT() != null 
					&& updIntvwReq.getCandidateInfoTo().getSubmitInterviewResultsDT().getMonth().equals("") && updIntvwReq.getInterviewInformationTo() != null 
					&& updIntvwReq.getInterviewInformationTo().getUpdateIntvwDtls().equals("Y")) {
				IntervwResultsDAO.updateInitInterviewEnteredDate(updIntvwReq.getCandidateInfoTo());
			} //End if (updIntvwReq != null && updIntvwReq.getCandidateInfoTo() != null && updIntvwReq.getCandidateInfoTo().getSubmitInterviewResultsDT() == null) {				
			
			//Update the date that the Offer Results were 1st entered
			if (updIntvwReq != null && updIntvwReq.getCandidateInfoTo() != null && updIntvwReq.getCandidateInfoTo().getSubmitOfferResultsDT() != null 
					&& updIntvwReq.getCandidateInfoTo().getSubmitOfferResultsDT().getMonth().equals("") && updIntvwReq.getInterviewInformationTo() != null 
					&& updIntvwReq.getInterviewInformationTo().getUpdateOfferDtls().equals("Y")) {
				IntervwResultsDAO.updateInitOfferEnteredDate(updIntvwReq.getCandidateInfoTo());
			} //End if (updIntvwReq != null && updIntvwReq.getCandidateInfoTo() != null && updIntvwReq.getCandidateInfoTo().getSubmitInterviewResultsDT() == null) {			
			
			//Added for FMS 7894 January 2016 CR's
			//When the Do Not Consider Flag is Y, add a record to table xxxxx
			if (updIntvwReq != null && updIntvwReq.getInterviewInformationTo() != null && updIntvwReq.getInterviewInformationTo().getDoNotConsiderFor60Days() != null
					&& updIntvwReq.getInterviewInformationTo().getDoNotConsiderFor60Days().equals("Y")) 
			{
				logger.debug(String.format("Candidate:%1$s was marked as Do Not Consider for 60 Days", updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId()));
				
				String jobCategory = getJobCategoryFromIntvwReqResults(updIntvwReq);
				boolean metIndicatorFlg = false;
				
				if (jobCategory.equals("0040")) {
					metIndicatorFlg = true;
				}

				//Check to see if the applicant has a current active record for do not consider
				CandidateInfoTO tempObj = IntervwResultsDAO.readLastDNCRow(updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber(), updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(), metIndicatorFlg);
				boolean insertNewRow = true;
				
				//If they a active record, update the EFF_END_DATE to the current date - 1 day.
				if (tempObj != null && tempObj.getEmploymentPositionCandidateId() != null && !tempObj.getEmploymentPositionCandidateId().equals("")) {
					logger.debug(String.format("ApplicantId:%1$s has a current do not consider record.", tempObj.getEmploymentPositionCandidateId()));
					//Check if it is the same data and being saved again, don't insert new record.
					if (tempObj.getEffectiveBeginDate().toString().equals(new Date(System.currentTimeMillis()).toString())) {
						IntervwResultsDAO.updateDoNotConsiderFor60DaysEffEndDt60DaysOut(tempObj.getHumanResourcesSystemStoreNumber(), tempObj.getEmploymentRequisitionNumber(),
								tempObj.getEmploymentPositionCandidateId(), metIndicatorFlg, tempObj.getEffectiveBeginDate());
						insertNewRow = false;
					} else {
						//Update the EFF_END_DATE to current date - 1 day
						IntervwResultsDAO.updateDoNotConsiderFor60DaysEffEndDt(tempObj.getHumanResourcesSystemStoreNumber(), tempObj.getEmploymentRequisitionNumber(),
								tempObj.getEmploymentPositionCandidateId(), metIndicatorFlg, tempObj.getEffectiveBeginDate());
					}
				}
				
				//Write a new record
				if (insertNewRow) {
					IntervwResultsDAO.createDoNotConsiderFor60Days(updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber(), updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber()
																 , updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(), metIndicatorFlg);
				}
			}
			
			//When the Do Not Consider Flag is N
			if (updIntvwReq != null && updIntvwReq.getInterviewInformationTo() != null && updIntvwReq.getInterviewInformationTo().getDoNotConsiderFor60Days() != null
					&& updIntvwReq.getInterviewInformationTo().getDoNotConsiderFor60Days().equals("N")) 
			{
				logger.debug(String.format("Candidate:%1$s was not marked as Do Not Consider for 60 Days", updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId()));
				
				String jobCategory = getJobCategoryFromIntvwReqResults(updIntvwReq);
				boolean metIndicatorFlg = false;
				
				if (jobCategory.equals("0040")) {
					metIndicatorFlg = true;
				}

				//Check to see if the applicant has a current record for do not consider
				CandidateInfoTO tempObj = IntervwResultsDAO.readLastDNCRow(updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber(), updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(), metIndicatorFlg);
				
				//If they have one, update the EFF_END_DATE to the current date - 1 day.
				if (tempObj != null && tempObj.getEmploymentPositionCandidateId() != null && !tempObj.getEmploymentPositionCandidateId().equals("")) {
					logger.debug(String.format("ApplicantId:%1$s has a current do not consider record.", tempObj.getEmploymentPositionCandidateId()));
					
						//Update the EFF_END_DATE to current date - 1 day
						IntervwResultsDAO.updateDoNotConsiderFor60DaysEffEndDt(tempObj.getHumanResourcesSystemStoreNumber(), tempObj.getEmploymentRequisitionNumber(),
								tempObj.getEmploymentPositionCandidateId(), metIndicatorFlg, tempObj.getEffectiveBeginDate());
				}
			}
			
			// Candidate Information Update
			if (updIntvwReq != null && updIntvwReq.getCandidateInfoTo() != null) {
				intervwRltsMgr.updateHumanResourcesStoreRequisitionCandidate(updIntvwReq.getCandidateInfoTo());
				//Added for FMS 7894 January 2016 CR's
				//When Candidate is Rehire Ineligible, need to update EAPLCNT_CAND_XREF field RHR_ELIG_IND to 'N' 
				if (updIntvwReq.getCandidateInfoTo().getCandidateOfferMadeFlag() != null && updIntvwReq.getCandidateInfoTo().getCandidateOfferMadeFlag().trim().equals("R")) {
					logger.debug(String.format("Candidate:%1$s is Rehire Ineligible!", updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId()));
					IntervwResultsDAO.updateRehireEligibleFlag(updIntvwReq.getCandidateInfoTo().getEmploymentCandidateId());
				} // End - if (updIntvwReq.getCandidateInfoTo().getCandidateOfferMadeFlag() != null && updIntvwReq.getCandidateInfoTo().getCandidateOfferMadeFlag().trim().equals("R")) {
			}
			
			//Added for new real Time BGC Order Process.  If new process is in place do not order the BGC
			if (!isBgcRealTimeOrder(updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber())) {
				//Insert to bgc_cnsnt_sig if External and user clicked yes, Order Background Check
				if (updIntvwReq != null && updIntvwReq.getInterviewInformationTo() != null && updIntvwReq.getBackgroundCheckInfo() != null &&
						updIntvwReq.getCandidateInfoTo() != null) {
					if (updIntvwReq.getInterviewInformationTo().getBackgroundConsent() != null && 
						updIntvwReq.getInterviewInformationTo().getBackgroundConsent().equals("Y") &&
						//updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent() == false &&
						updIntvwReq.getCandidateInfoTo().getCandidateOfferStatusIndicator().equals("AC") &&
						updIntvwReq.getCandidateInfoTo().getApplicantType().equals("EXT")) {
					
/*						//Insert Signed Consent record
						if (updIntvwReq.getInterviewInformationTo().getBackgroundConsent().equals("Y") && !updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent()) {
							intervwRltsMgr.createBackgroundCheckSystemConsentSig(updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId());
						}*/
						
						//Insert Signed Consent record
						if (updIntvwReq.getInterviewInformationTo().getBackgroundConsent().equals("Y") && !updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent()) {
							intervwRltsMgr.createBackgroundCheckSystemConsentSig_Non_PA_Dao2(
									updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(),
									createUserId);
						}						
							
						//Order Background Check for Job
						if (updIntvwReq.getBackgroundCheckInfo().isNeedsBackgroundCheck()) {
							String candId = updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId();
							Timestamp createTimeStamp = new Timestamp(System.currentTimeMillis());
							//String createUserId = profile.getProperty(Profile.USER_ID).toUpperCase();
							int reqNbr = updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber();
							int packageId;
							if (updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded() != 0) {
								packageId = updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded();
							} else {
								packageId = 3;
							}
							String strId="";
							if (updIntvwReq.getBackgroundCheckInfo().getHumanResourcesStoreTypeCode().equals("HD")) {
								//Store
								strId = "HDO" + updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber();
							} else {
								//DC
								strId = "HDD" + updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber();
							}
						
							//Insert Required Package
							intervwRltsMgr.createBackgroundCheckSystemOrder(candId, createTimeStamp, createUserId, reqNbr
																, packageId, strId, externalBgcId, bgcOrderStatus);
					
							//Insert Required Components
							int compsLength;
							String componentIds = "";
							String[] compToks;
							if (updIntvwReq.getBackgroundCheckInfo().getBackgroundComponentsNeeded() != null) {
								compsLength = updIntvwReq.getBackgroundCheckInfo().getBackgroundComponentsNeeded().length();
								componentIds = updIntvwReq.getBackgroundCheckInfo().getBackgroundComponentsNeeded().substring(0, compsLength -1);
								compToks = componentIds.toString().split(",");
						
								for (int i = 0;i < compToks.length;i++) {
									intervwRltsMgr.createBackgroundCheckSystemOrderDetail(candId, createTimeStamp, createUserId, i+1, Integer.parseInt(compToks[i]));
								}
								backgroundOrdered = true;						
							}
						}
					}
				} //End if (updIntvwReq != null && updIntvwReq.getInterviewInformationTo() != null && updIntvwReq.getBackgroundCheckInfo() != null &&
				//updIntvwReq.getCandidateInfoTo() != null)
			
				//Insert to bgc_cnsnt_sig if Internal and user clicked yes, Order Background Check
				if (updIntvwReq != null && updIntvwReq.getInterviewInformationTo() != null && updIntvwReq.getBackgroundCheckInfo() != null &&
						updIntvwReq.getCandidateInfoTo() != null) {
					String candId = updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId();
					if (updIntvwReq.getInterviewInformationTo().getBackgroundConsent() != null && 
						updIntvwReq.getInterviewInformationTo().getBackgroundConsent().equals("Y") &&
						//updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent() == false &&
						updIntvwReq.getCandidateInfoTo().getCandidateOfferStatusIndicator().equals("AC") &&
						updIntvwReq.getCandidateInfoTo().getApplicantType().equals("INT")) {
					
/*						//Insert Signed Consent record
						if (updIntvwReq.getInterviewInformationTo().getBackgroundConsent().equals("Y") && !updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent()) {
							intervwRltsMgr.createBackgroundCheckSystemConsentSig(updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId());
						}
*/						
						//Insert Signed Consent record
						if (updIntvwReq.getInterviewInformationTo().getBackgroundConsent().equals("Y") && !updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent()) {
							intervwRltsMgr.createBackgroundCheckSystemConsentSig_Non_PA_Dao2(
									updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(),
									createUserId);
						}

						if (updIntvwReq.getInterviewInformationTo().getUpdateLicenseDtls().equals("Y")) {		
							// Save Drivers License information to BGC_CNSNT table
							//Encrypt Drivers License Number
							logger.debug(String.format("Clear Driver License:%1$s", updIntvwReq.getInterviewInformationTo().getLicNum()));
							String encryptedDL = VoltageManager.protectDriversLicense(updIntvwReq.getInterviewInformationTo().getLicNum(), httpRequest);
							logger.debug(String.format("Encrypted Driver License:%1$s", encryptedDL));
										
							//Get the Associates DOB from TZ so that it can be added to BGC_CNSNT table along with DL information
							Date associateDob = IntervwResultsDAO.getAssociateDOB(candId);
										
							//Update DL, DOB, and DL State in BGC_CNSNT table
							IntervwResultsDAO.updateAssociateDMVDetails(candId, associateDob, encryptedDL, updIntvwReq.getInterviewInformationTo().getLicState());
						}	

						//Order Background Check for Internal DMV if required
						if (updIntvwReq.getBackgroundCheckInfo().isNeedsBackgroundCheck()) {		
							Timestamp createTimeStamp = new Timestamp(System.currentTimeMillis());
							//String createUserId = profile.getProperty(Profile.USER_ID).toUpperCase();
							int reqNbr = updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber();
							int packageId = 0;
						
							//	if (updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded() != 0) {
							logger.debug(String.format("Background Package Needed:%1$s", updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded()));
							if (updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded() == 24 && !updIntvwReq.getBackgroundCheckInfo().isAlreadyInDrivingPos()) {
								//Pkg 24 is for Asset Protection and Associate is NOT in Driving Position, so they need AP + MVR
								packageId = 27;
							} else if (updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded() == 24 && updIntvwReq.getBackgroundCheckInfo().isAlreadyInDrivingPos()) {
								//Pkg 24 is for Asset Protection and Associate is already in Driving Position, so they need AP
								packageId = 26;
							} else {
								//Associate only needs a DMV check
								packageId = 25;
							}
							//	}
						
							String strId="";
							if (updIntvwReq.getBackgroundCheckInfo().getHumanResourcesStoreTypeCode().equals("HD")) {
								//Store
								strId = "HDO" + updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber();
							} else {
								//DC
								strId = "HDD" + updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber();
							}
						
							//Insert Required Package
							intervwRltsMgr.createBackgroundCheckSystemOrder(candId, createTimeStamp, createUserId, reqNbr
								                                      , packageId, strId, externalBgcId, bgcOrderStatus);
							
							//Insert Required Components
							switch (packageId) {
							case 25:
								//MVR Component
								intervwRltsMgr.createBackgroundCheckSystemOrderDetail(candId, createTimeStamp, createUserId, 1, 3);
								backgroundOrderedDMV = true;
								break;
							case 26:
								//AP Component
								intervwRltsMgr.createBackgroundCheckSystemOrderDetail(candId, createTimeStamp, createUserId, 1, 7);
								backgroundOrderedAP = true;
								break;
							case 27:
								//MVR and AP Component
								intervwRltsMgr.createBackgroundCheckSystemOrderDetail(candId, createTimeStamp, createUserId, 1, 3);
								intervwRltsMgr.createBackgroundCheckSystemOrderDetail(candId, createTimeStamp, createUserId, 2, 7);
								backgroundOrderedAPDMV = true;
								break;
							}						
						}
					}
				} // End if (updIntvwReq != null && updIntvwReq.getInterviewInformationTo() != null && updIntvwReq.getBackgroundCheckInfo() != null &&
							//updIntvwReq.getCandidateInfoTo() != null) {
			} else {
				backgroundNewProcess = true;
			}  // End if (!isBgcRealTimeOrder(updIntvwReq.getCandidateInfoTo().getHumanResourcesSystemStoreNumber())) {
						
			// Determine what needs to go in the Success Message to show the
			// user what was updated
			if (backgroundNewProcess) {
				successMsg = "Details saved successfully.";
				res.setSuccessMsg(successMsg);
			} else if (backgroundOrdered) {
				successMsg = "Details saved successfully. The background check will be ordered.";
				res.setSuccessMsg(successMsg);
			} else if (backgroundOrderedDMV) {
				successMsg = "Details saved successfully.  Associate DMV background check will be ordered.";
				res.setSuccessMsg(successMsg);
			} else if (backgroundOrderedAPDMV) {
				successMsg = "Details saved successfully. The Associate Asset Protection + DMV background check will be ordered.";
				res.setSuccessMsg(successMsg);				
			} else if (backgroundOrderedAP) {
				successMsg = "Details saved successfully. The Associate Asset Protection background check will be ordered.";
				res.setSuccessMsg(successMsg);				
			}
			
			res.setStatus(SUCCESS_APP_STATUS);

		} catch (RetailStaffingException re) {
			throw re;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.INTVW_RESULT_SCRN_UPDATE_ERR, e);
		}
		logger.info(this + "Leaves updateInterviewResultsCandidateIntvwDtls method in Manager with output" + res);
		return res;
	}

	private String convertIntvwQuest(String inCode) throws RetailStaffingException {
		String outCode = "";

		try {
			if (inCode.equalsIgnoreCase("1")) {
				outCode = "HU";
			} else if (inCode.equalsIgnoreCase("2")) {
				outCode = "U";
			} else if (inCode.equalsIgnoreCase("3")) {
				outCode = "A";
			} else if (inCode.equalsIgnoreCase("4")) {
				outCode = "F";
			} else if (inCode.equalsIgnoreCase("5")) {
				outCode = "HF";
			}
		} catch (Exception e) {
			throw new RetailStaffingException();
		}
		return outCode;
	}

	private boolean checkForBackgroundConsent(String candId) throws RetailStaffingException {
		boolean hasConsent = false;
		Date currentDt = null;
		Calendar cal = null;
		Date currentMinusYear = null;
		IntervwResultsDAO theDao = new IntervwResultsDAO();

		try {
			// Get Current Date
			currentDt = new Date(System.currentTimeMillis());
			cal = Calendar.getInstance();
			cal.setTime(currentDt);
			// Subtracts 365 days from the date
			cal.add(Calendar.DATE, -365);
			// Convert the Calendar object back to a Date
			currentMinusYear = new Date(cal.getTime().getTime());
			hasConsent = theDao.readBackgroundCheckSystemConsentSignatureForExistenceByConsentSignatureDate(candId, currentMinusYear);
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		return hasConsent;
	}
	
	private BackgroundCheckDtlsTO checkForBackgroundConsentElectronic(String candId) throws RetailStaffingException {
		//Has Consent
		boolean hasConsent = false;
		Date currentDt = null;
		Calendar cal = null;
		Date currentMinusYear = null;
		BackgroundCheckDtlsTO bgcCnsntTo;
		IntervwResultsDAO theDao = new IntervwResultsDAO();

		try {
			// Get Current Date
			currentDt = new Date(System.currentTimeMillis());
			cal = Calendar.getInstance();
			cal.setTime(currentDt);
			// Subtracts 365 days from the date
			cal.add(Calendar.DATE, -365);
			// Convert the Calendar object back to a Date
			currentMinusYear = new Date(cal.getTime().getTime());
			bgcCnsntTo = theDao.readBackgroundCheckConsentElectronicDao2(candId, currentMinusYear);
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		return bgcCnsntTo;
	}
	
	private String getJobCategoryFromIntvwReqResults(UpdateIntvwRltsCandIntvwDtlsRequest updIntvwReq) throws RetailStaffingException {
		String jobCategory = "";
		//Is this a MET Requisition
		//Get the Job Code and Dept in order to determine if it is a MET Job
		try{
			List<ReadHumanResourcesStoreEmploymentRequisitionDTO> reqList = new ArrayList<ReadHumanResourcesStoreEmploymentRequisitionDTO>();
			ReadHumanResourcesStoreEmploymentRequisitionDTO reqInfo = null;
			reqList = PhoneScreenDAO.readHumanResourcesStoreEmploymentRequisition(updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber());
			if (reqList.size() > 0) {
				reqInfo = reqList.get(0);
			}
			logger.debug(String.format("For Requisition:%1$s found Dept:%2$s JobTitleCode:%3$s", updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber(), reqInfo.getHumanResourcesSystemDepartmentNumber(), reqInfo.getJobTitleCode()));
			//Check to see if returned Job Title and Dept is a MET Position
			//Job Title must be padded with spaces up to 6
			String paddedJobTitle = String.format("%1$-" + 6 + "s", reqInfo.getJobTitleCode());
			jobCategory = IntervwResultsDAO.readKenexaJobCategory(paddedJobTitle + reqInfo.getHumanResourcesSystemDepartmentNumber());
			logger.debug(String.format("Job Category:%1$s returned for requisition:%2$s", jobCategory, updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber()));
			
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.INTVW_RESULT_SCRN_UPDATE_ERR, e);
		}
		
		return jobCategory;
	}

/*	private void createQuewebBackgroundEmail(String name, String aid, int internalAssociateDriverId) {
		String toAddresses = null;
		String fromAddress = null;
		StringBuffer subject = new StringBuffer(100);
		StringBuffer textBody = new StringBuffer(1000);
		StringBuffer serverName = new StringBuffer(50);
		StringBuffer constructLink = new StringBuffer(100);

		try {
			String strLCEnv = com.homedepot.ta.aa.util.TAAAResourceManager
					.getProperty("host.LCP");
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Queweb Drivers License Email ****** Env Value for LCP is %1$s", strLCEnv));
			}
			// Determine which email address to send the Queweb message to.
			//Also determine the server name for the link
			if ("AD".equals(strLCEnv) || "QA".equals(strLCEnv)
					|| "QP".equals(strLCEnv) || "TR".equals(strLCEnv)
					|| "ST".equals(strLCEnv)) {
				// Non Production Environments
				toAddresses = " testcloud2_queweb@homedepot.com";
				fromAddress = " testcloud1_queweb@homedepot.com";
				if ("AD".equals(strLCEnv)) {
					serverName.append("http://localhost:8081/");					
				} else {
					serverName.append("https://webapps-").append(strLCEnv).append(".homedepot.com/");					
				}
			} else if ("PR".equals(strLCEnv)) {
				// Production Environment
				toAddresses = "myTHDHR@homedepot.com";
				fromAddress = "myTHDHR@homedepot.com";
				serverName.append("https://webapps.homedepot.com/");
			} else {
				// Production Environment
				toAddresses = "myTHDHR@homedepot.com";
				fromAddress = "myTHDHR@homedepot.com";
				serverName.append("https://webapps.homedepot.com/");				
			}

			Profile profile = Profile.getCurrent();

			subject.append("Order Background for Internal Candidate ");
			// Add Candidate Name
			subject.append(name);
			subject.append(" X-MAILER: ").append(
					profile.getProperty(Profile.USER_ID));

			constructLink.append(serverName).append("RetailStaffing/service/DlLinkService/getData?aid=").append(aid).append("&recordId=").append(internalAssociateDriverId).append("\r\n");
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("Queweb Drivers License Email ****** Constructed Link %1$s", constructLink.toString()));
			}			
			
			textBody.append("\n");
			textBody
					.append("Click on the link below to get the Internal Candidate information.\n\n");
			textBody.append(constructLink.toString()).append("\r\n");

			EmailSender.sendEmail(toAddresses, fromAddress, subject.toString(), textBody.toString(),strLCEnv);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	/*
	 * This method takes in the BCG Component Completed Date. Subtracts the
	 * Favorable or Unfavorable Effective Days from the current date. If the BCG
	 * Component Completed Date > (current date - Favorable or Unfavorable
	 * Effective Days) it returns true. Therefore no new Background Check is
	 * needed.
	 */

	private boolean isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(Date passedDate, int adjustByDays) throws RetailStaffingException {

		Date currentDt = null;
		Calendar cal = null;
		Calendar cal2 = null;
		boolean answer = false;

		try {
			cal2 = Calendar.getInstance();
			cal2.setTime(passedDate);
			logger.info("BCG Component Completed Date = " + new Date(cal2.getTime().getTime()));

			currentDt = new Date(System.currentTimeMillis());
			cal = Calendar.getInstance();
			cal.setTime(currentDt);
			cal.add(Calendar.DATE, (adjustByDays * -1));
			logger.info("Adjusted Current date = " + new Date(cal.getTime().getTime()));

			if (cal2.getTimeInMillis() > cal.getTimeInMillis()) {
				answer = true;
				logger.info("No New Background Check Required");
			} else {
				logger.info("New Background Check Required");
			}

		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		return answer;
	}
	
	public Response getCandidateRehireDetails(String applcantID) throws RetailStaffingException {
		logger.info(this + "Enters getCandidateRehireDetails method in Manager Layer ");
		
		Response res = null;
		CandidateInfoTO candinfoDtl = new CandidateInfoTO();
		
		try {
			String returnedSSN = IntervwResultsDAO.getCandSsnIfExists(applcantID);
			if (returnedSSN != null) {
				res = new Response();
				//Check for Rehire Eligible 
				CandidateInfoTO tempCand = IntervwResultsDAO.getRehireEligible(returnedSSN);
				candinfoDtl.setRehireEligible(tempCand.isRehireEligible());
				//Set the data needed for message when a candidate is NOT Rehire Eligible 
				if (!tempCand.isRehireEligible()) {
					candinfoDtl.setTermLoc(tempCand.getTermLoc());
					candinfoDtl.setTermRsn(tempCand.getTermRsn());
					candinfoDtl.setTermDt(tempCand.getTermDt());
				} // End - if (!tempCand.isRehireEligible()) { 
			} else {
				throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, "Unable to locate encrypted SSN");	
			} // End - if (returnedSSN != null) {
		
			res.setCandinfoDtRes(candinfoDtl);
			
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return res;
	}

	public static boolean updateApplicantDrugTestOrder(String EMPLT_APLCNT_ID_DT, int EMPLT_REQN_NBR, Long DTEST_ORD_NBR) throws Exception{
		boolean updateDTDB = false;
		try { updateDTDB = IntervwResultsDAO.updateApplicantDrugTestOrderDAO20(EMPLT_APLCNT_ID_DT, EMPLT_REQN_NBR, DTEST_ORD_NBR);
		} catch (Exception e) {
			throw e;
		}
		
		return updateDTDB;
	}	
	
	public String getDrugTestPilotResult(String currentLocation) throws RetailStaffingException {

		logger.info(this + " Enters getDrugTestPilotResult method in Manager...");
		String showDrugTestPanel = "false";
		try {
			String drugTestStatus = IntervwResultsDAO.readHrOrgParmDrugTestPanelStatus();
			
			logger.info("****getDrugTestPilotResult method Status = " + drugTestStatus);

			if (drugTestStatus.equalsIgnoreCase("Off")) {
				showDrugTestPanel = "true";
			} else if (drugTestStatus.equalsIgnoreCase("Pilot")) {
					List<String> pilotLocationList = IntervwResultsDAO.readHrOrgParmDrugTestPanelPilotLocations();
					if (pilotLocationList != null && pilotLocationList.size() > 0) {
						int listLength = pilotLocationList.size();
						for (int i = 0; i < listLength; i++) {
							if (currentLocation.equals(pilotLocationList.toArray()[i].toString())) {
								showDrugTestPanel = "true";
								break;
							}
						}
					}
			}
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + " Leaves getDrugTestPilotResult method in Manager with output..." + showDrugTestPanel);
		return showDrugTestPanel;
	}
	
	public String getMinorConsentPilotResult(String currentLocation) throws RetailStaffingException {

		logger.info(this + " Enters getMinorConsentPilotResult method in Manager...");
		String showMinorConsentPanel = "false";
		try {
			String minorConsentStatus = IntervwResultsDAO.readHrOrgParmMinorConsentPanelStatus();
			
			logger.info("****getMinorConsentPilotResult method Status = " + minorConsentStatus);

			if (minorConsentStatus.equalsIgnoreCase("Off")) {
				showMinorConsentPanel = "true";
			} else if (minorConsentStatus.equalsIgnoreCase("Pilot")) {
					List<String> pilotMinorConsentLocationList = IntervwResultsDAO.readHrOrgParmMinorConsentPanelPilotLocations();
					if (pilotMinorConsentLocationList != null && pilotMinorConsentLocationList.size() > 0) {
						int listMCLength = pilotMinorConsentLocationList.size();
						for (int i = 0; i < listMCLength; i++) {
							if (currentLocation.equals(pilotMinorConsentLocationList.toArray()[i].toString())) {
								showMinorConsentPanel = "true";
								break;
							}
						}
					}
			}
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + " Leaves getMinorConsentPilotResult method in Manager with output..." + showMinorConsentPanel);
		return showMinorConsentPanel;
  }
	
  	public boolean isBgcRealTimeOrder(String currentLocation) throws RetailStaffingException {

		logger.info(this + " Enters isBgcRealTimeOrder method in Manager...");
		boolean isRealTimeOrder = false;
		
		try {
			String bgcPilotStatus = IntervwResultsDAO.readHrOrgParmBgcPilotStatus();
			
			logger.info("****isBgcRealTimeOrder method Status = " + bgcPilotStatus);

			if (bgcPilotStatus.equalsIgnoreCase("Off")) {
				isRealTimeOrder = true;
			} else if (bgcPilotStatus.equalsIgnoreCase("Pilot")) {
					List<String> pilotLocationList = IntervwResultsDAO.readHrOrgParmBgcPilotLocations();
					if (pilotLocationList != null && pilotLocationList.size() > 0) {
						int listLength = pilotLocationList.size();
						for (int i = 0; i < listLength; i++) {
							if (currentLocation.equals(pilotLocationList.toArray()[i].toString())) {
								isRealTimeOrder = true;
								break;
							}
						}
					}
			}
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + " Leaves isBgcRealTimeOrder method in Manager with output..." + isRealTimeOrder);
		return isRealTimeOrder;
	}
  
	private FirstAdvantageBgcResponse orderRealTimeBgcFromFa(String applicantId, int requisitionId, int thdPackageId, String requesterLdap, String emailAddress) {
		
		FirstAdvantageBgcResponse firstAdvantageBgcResponse = null;
		Gson gson = new Gson();
		
		try {

			String strLCEnv = com.homedepot.ta.aa.util.TAAAResourceManager.getProperty("host.LCP");
			String hostName = "";
			String apiToken = "";
			SSLRepertoire.setRepertoire("DrugTestService");
			
			if (strLCEnv.equals("PR")) {
				//hostName = "https://bgccandidateinviteapiservices.apps.homedepot.com";
				hostName = "https://thdapien.homedepot.com/ebc";
				apiToken = Constants.BGC_PR_API_TOKEN;
			} else if (strLCEnv.equals("AD")) {
				//hostName = "https://bgccandidateinviteapiservices.apps-np.homedepot.com";
				//hostName = "https://thdapiqai.homedepot.com/ebc";
				hostName = "http://localhost:8081";
				apiToken = Constants.BGC_QA_API_TOKEN;
			} else {
				//hostName = "https://bgccandidateinviteapiservices.apps-np.homedepot.com";
				hostName = "https://thdapiqai.homedepot.com/ebc";
				//hostName = "http://localhost:8081";
				apiToken = Constants.BGC_QA_API_TOKEN;
			}
			
			logger.debug("Host Name:" + hostName);
			
			URL url;
			
			//If an emailAddress exists, it is an Associate and need to call the correct URL
			if (emailAddress == null) {
				url = new URL( hostName + "/api/bgcCandidateInvite/applicantId/" + applicantId + "/requisitionId/" + requisitionId
	                    + "/thdPackageId/" + thdPackageId + "/requesterLdap/" + requesterLdap);
			} else {
				url = new URL( hostName + "/api/bgcCandidateInvite/applicantId/" + applicantId + "/requisitionId/" + requisitionId
	                    + "/thdPackageId/" + thdPackageId + "/requesterLdap/" + requesterLdap
	                    + "/emailAddress/" + emailAddress);
			}
				
			Client client = Client.create();

			WebResource webResource = client.resource(url.toString());

			ClientResponse clientResponse = webResource.header(Constants.API_KEY, apiToken).post(ClientResponse.class);
			
			if (clientResponse != null) {
				firstAdvantageBgcResponse = gson.fromJson(clientResponse.getEntity(String.class), FirstAdvantageBgcResponse.class);
				
			} else {
				throw new Exception("An error occured getting data from First Advantage.");
			}
			
		} catch (Exception e) {
			Util.logFatalError("Invalid response from Background Check API", e);
			firstAdvantageBgcResponse.setSuccess(false);
			firstAdvantageBgcResponse.setMessage("Invalid response");
		}
		
		logger.debug(gson.toJson(firstAdvantageBgcResponse));
		
		return firstAdvantageBgcResponse;
	}
	
	public Response initiateWebBgcOrder(final SubmitBgcCandidateDtlsRequest updIntvwReq, HttpServletRequest httpRequest) throws RetailStaffingException {
		logger.info(this + "Enters initiateWebBgcOrder method in Manager with input" + updIntvwReq);

		Response res = null;
		IntervwResultsDAO intervwRltsMgr = new IntervwResultsDAO();
		FirstAdvantageBgcResponse firstAdvantageBgcResponse;

		try {
			res = new Response();
			Profile profile = Profile.getCurrent();
			String createUserId = profile.getProperty(Profile.USER_ID).toUpperCase();
			
			//Insert to bgc_cnsnt_sig if External
			if (updIntvwReq.getBackgroundCheckInfo() != null && updIntvwReq.getCandidateInfoTo() != null) {
				if (updIntvwReq.getCandidateInfoTo().getApplicantType().equals("EXT")) {
					if (updIntvwReq.getBackgroundCheckInfo().isNeedsBackgroundCheck()) {
						int reqNbr = updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber();
						int packageId;
						if (updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded() != 0) {
							packageId = updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded();
						} else {
							packageId = 3;
						}
						
						//Use new Real time Background Check Order System
						//Order the Background Check from FA
						logger.debug("Order BGC from FA");
						firstAdvantageBgcResponse = orderRealTimeBgcFromFa(updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId()
								, reqNbr, packageId, createUserId, null);
						if (firstAdvantageBgcResponse.isSuccess()) {
							
							logger.debug(firstAdvantageBgcResponse.getFirstAdvantageResponse().getProfileId());
							logger.debug(firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink());
							
							//Get the key value from the ApplicantLink
							String linkKeyValue = firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink()
									.substring(firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink().indexOf("?key=") + 5);
							logger.debug(linkKeyValue);
							
							//Insert Signed Consent record
							if (!updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent() && !updIntvwReq.getBackgroundCheckInfo().isReinitiateBgcConsent()) {
								intervwRltsMgr.createBackgroundCheckSystemConsentSig_PA_Dao2(
										updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(),
										createUserId, firstAdvantageBgcResponse.getFirstAdvantageResponse().getProfileId()
										,linkKeyValue);
							} else {
								if (updIntvwReq.getBackgroundCheckInfo().isReinitiateBgcConsent()) {
									intervwRltsMgr.updateBackgroundCheckSystemConsentSig_PA_Dao2(
											updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(),
											createUserId, firstAdvantageBgcResponse.getFirstAdvantageResponse().getProfileId()
											,linkKeyValue);
								}
							}
							
							//Add PA Link to the response
							res.setProfileAdvantageLink(firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink());
							
						} else {
							throw new RetailStaffingException("Error: " + firstAdvantageBgcResponse.getErrorCode() + "  Error Message: " + firstAdvantageBgcResponse.getMessage());
						}					
					}
				}
			}
			
			//Insert to bgc_cnsnt_sig if Internal and user clicked yes, Order Background Check
			if (updIntvwReq.getBackgroundCheckInfo() != null && updIntvwReq.getCandidateInfoTo() != null) {
				String candId = updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId();
				if (updIntvwReq.getCandidateInfoTo().getApplicantType().equals("INT")) {
					
					//Order Background Check for Internal DMV if required
					if (updIntvwReq.getBackgroundCheckInfo().isNeedsBackgroundCheck()) {		
						int reqNbr = updIntvwReq.getCandidateInfoTo().getEmploymentRequisitionNumber();
						int packageId = 0;
						
						logger.debug(String.format("Background Package Needed:%1$s", updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded()));
							if (updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded() == 24 && !updIntvwReq.getBackgroundCheckInfo().isAlreadyInDrivingPos()) {
								//Pkg 24 is for Asset Protection and Associate is NOT in Driving Position, so they need AP + MVR
								packageId = 27;
							} else if (updIntvwReq.getBackgroundCheckInfo().getBackgroundPackageNeeded() == 24 && updIntvwReq.getBackgroundCheckInfo().isAlreadyInDrivingPos()) {
								//Pkg 24 is for Asset Protection and Associate is already in Driving Position, so they need AP
								packageId = 26;
							} else {
								//Associate only needs a DMV check
								packageId = 25;
							}
								
						    //Use new Real time Background Check Order System
							//Order the Background Check from FA
							logger.debug("Order BGC from FA");
							firstAdvantageBgcResponse = orderRealTimeBgcFromFa(updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId()
									, reqNbr, packageId, createUserId, updIntvwReq.getCandidateInfoTo().getAssociateEmailForBgcConsent());
							if (firstAdvantageBgcResponse.isSuccess()) {
								logger.debug(firstAdvantageBgcResponse.getFirstAdvantageResponse().getProfileId());
								logger.debug(firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink());
								
								//Get the key value from the ApplicantLink
								String linkKeyValue = firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink()
										.substring(firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink().indexOf("?key=") + 5);
								logger.debug(linkKeyValue);
								
								//Insert Signed Consent record
								if (!updIntvwReq.getBackgroundCheckInfo().isHasSignedConsent() && !updIntvwReq.getBackgroundCheckInfo().isReinitiateBgcConsent()) {
									intervwRltsMgr.createBackgroundCheckSystemConsentSig_PA_Dao2(
											updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(),
											createUserId, firstAdvantageBgcResponse.getFirstAdvantageResponse().getProfileId()
											,linkKeyValue);
								} else {
									if (updIntvwReq.getBackgroundCheckInfo().isReinitiateBgcConsent()) {
										intervwRltsMgr.updateBackgroundCheckSystemConsentSig_PA_Dao2(
												updIntvwReq.getCandidateInfoTo().getEmploymentPositionCandidateId(),
												createUserId, firstAdvantageBgcResponse.getFirstAdvantageResponse().getProfileId()
												,linkKeyValue);
									}
								}
								
								//Add PA Link to the response
								res.setProfileAdvantageLink(firstAdvantageBgcResponse.getFirstAdvantageResponse().getApplicantLink());
								
							} else {
								throw new RetailStaffingException("Error:" + firstAdvantageBgcResponse.getErrorCode() + "  Error Message:" + firstAdvantageBgcResponse.getMessage());
							}				
					}
				}
			}
			
			res.setStatus(SUCCESS_APP_STATUS);

		} catch (RetailStaffingException re) {
			throw re;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.INTVW_RESULT_SCRN_UPDATE_ERR, e);
		}
		logger.info(this + "Leaves initiateWebBgcOrder method in Manager with output" + res);
		return res;
	}
	
	public Response getCandidateInterviewQuestionDetailsNewBgcProcess(String applcantID, String reqNbr, String strNbr, String deptNbr, String jobTtl,
			String applicantType, String organization) throws RetailStaffingException {
		logger.info(this + "Enters getCandidateInterviewQuestionDetailsNewBgcProcess method in Manager Layer ");
		Response res = null;
		IntervwResultsDAO intervwReqMgr = new IntervwResultsDAO();
		ApplIntervwByCandJobReqTO intrDtl = null;
		CandidateInfoTO candinfoDtl = null;
		CandidateDTInfoTO candDTinfoDtl = null;
		BackgroundCheckDtlsTO backChkDtl = null;
		ReadBackgroundCheckSystemConsentByInputListDTO licenseInfo = null;
		BackgroundCheckDtlsTO tempBackChkDtl = null;

		List<ApplIntervwByCandJobReqTO> interwList = null;
		List<CandidateInfoTO> candInfoTo = null;
		List<CandidateDTInfoTO> drugTestTO = null;
		List<ApplIntervwQuestTO> intervwQuestList = null;
		List<ReadPersonProfilesByPersonIdTO> intCandCurrentJob = null;
		List<ReadBackgroundCheckSystemConsentByInputListDTO> licenseInfoList = null;

		InterviewQuestDetailResponse intrwDetRes = new InterviewQuestDetailResponse();
		ApplIntvwDtlsResponse intrDtlRes = new ApplIntvwDtlsResponse();

		try {
			res = new Response();
				
			if (reqNbr != null && !reqNbr.trim().equals("")) {
				// get Interview UserId from DAO
				interwList = intervwReqMgr.readApplicantInterviewByCandidateAndJobRequisition(applcantID, Integer.parseInt(reqNbr));

				if (interwList != null && !interwList.isEmpty() && interwList.size() > 0) {
					intrDtl = interwList.get(0);

					// to fetch intervwQuestList details for Interview Results
					// screen
					if (intrDtl.getInterviewerUserId() != null && intrDtl.getInterviewerUserId().length() > 0) {

						logger.debug("readApplicantInterviewQuestion  - getInterviewerUserId: 1. " + intrDtl.getInterviewerUserId());
						logger.debug("readApplicantInterviewQuestion  - applcantID 			: 2. " + applcantID);
						logger.debug("readApplicantInterviewQuestion  - getInterviewDate	: 3. " + intrDtl.getInterviewDate());
						logger.debug("Result Indicator = " + intrDtl.getInterviewResultIndicator());

						intrDtlRes.setInterviewerUserId(intrDtl.getInterviewerUserId());
						// Need to format Date
						SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat outFmt = new SimpleDateFormat("MM/dd/yyyy");
						if (intrDtl.getInterviewDate() != null) {
							intrDtlRes.setInterviewDate(outFmt.format(inFmt.parse(intrDtl.getInterviewDate().toString())));
						}
						res.setInterviewerDtlsRes(intrDtlRes);

						intervwQuestList = intervwReqMgr.readApplicantInterviewQuestion(applcantID, intrDtl.getInterviewerUserId().toString(),
								intrDtl.getInterviewDate());

						if (intervwQuestList != null && intervwQuestList.size() > 0) {
							logger.debug("To fetch intervwQuestList details for loop size :" + intervwQuestList.size());
							intrwDetRes.setIntrwDtlList(intervwQuestList);
							res.setIntrwDtRes(intrwDetRes);
						}

					}// if(intrDtl.getInterviewerUserId() != null &&
					// intrDtl.getInterviewerUserId().length() > 0) {

				} // end of (interwList != null && !interwList.isEmpty() &&
				// interwList.size() > 0)

				// to fetch selected candidate details for Interview Results
				// screen
				candInfoTo = intervwReqMgr.readHumanResourcesStoreRequisitionCandidateDAO20(strNbr, Integer.parseInt(reqNbr), applcantID);
				drugTestTO = intervwReqMgr.readApplicantDrugTestOrderDAO20(applcantID, Integer.parseInt(reqNbr));
				float candAge = intervwReqMgr.readHumanResourcesStoreRequisitionCandidateAgeDAO20(applcantID);
				
				if (candAge < 18.00 && getMinorConsentPilotResult(strNbr).equalsIgnoreCase("true")) {
					candInfoTo.get(0).setIsCandMinorFlag(true);
					List<CandidateMinorInfoTO> getMinorFormCompletions = intervwReqMgr.readHumanResourcesParentalConsentFormsForMinorsDAO20(applcantID, Integer.parseInt(reqNbr));
				
					for(CandidateMinorInfoTO minorCandidateFormsData:getMinorFormCompletions)
					{
						if(minorCandidateFormsData.getParentalConsentFormDocsExistingRows()>0) {
							
							if(minorCandidateFormsData.getParentalConsentFormDocsIncomplete()==0) {
								candInfoTo.get(0).setParentalConsentFormsComplete("complete");
							} else {
								candInfoTo.get(0).setParentalConsentFormsComplete("incomplete");
							}
						} else {
							candInfoTo.get(0).setParentalConsentFormsComplete("notstarted");
						}
					}
				} else {
					candInfoTo.get(0).setIsCandMinorFlag(false);
				}
				
				if (candInfoTo != null && candInfoTo.size() > 0) {
					candinfoDtl = candInfoTo.get(0);
					res.setCandinfoDtRes(candinfoDtl);
				}

				
				if (drugTestTO != null && drugTestTO.size() > 0) {
					candDTinfoDtl = drugTestTO.get(0);
					res.setCandDTinfoDtRes(candDTinfoDtl);
				}
				
				// Background Check plus is a driving position
				backChkDtl = new BackgroundCheckDtlsTO();

				List<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO> newJobReqmntList = null;
				List<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO> currentJobReqmntList = null;
				List<ReadBackgroundCheckResultsDTO> currentIntCandBackgroundResultList = null;
				boolean isPreviousResultsWithinAYear = true;
				boolean hasAllComponentsForNewJob = true;
				
				// See if position is a Driving Position.  Determine if any Background Checks are needed
				if (candInfoTo != null && candInfoTo.size() > 0) {
					// get New Job Requirement List, Component
					newJobReqmntList = intervwReqMgr.readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(strNbr, jobTtl, deptNbr);
					backChkDtl.setHumanResourcesStoreTypeCode(StringUtils.trim(newJobReqmntList.get(0).getHumanResourcesStoreTypeCode()));

					if ("INT".equalsIgnoreCase(applicantType)) {
						// Get Internal Candidate current Job Info
						intCandCurrentJob = intervwReqMgr.readPersonProfilesByPersonId(applcantID);
						// Get Internal Candidate current job requirements for
						// Background Check
						currentJobReqmntList = intervwReqMgr.readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(intCandCurrentJob
								.get(0).getOrganization1(), intCandCurrentJob.get(0).getJobTitleId(), intCandCurrentJob.get(0).getOrganization2());
						if (currentJobReqmntList != null && currentJobReqmntList.size() > 0) {
							for (int i=0; i < currentJobReqmntList.size(); i++) {
								if (currentJobReqmntList.get(i).getBackgroundCheckSystemComponentId() == 3) {
									backChkDtl.setAlreadyInDrivingPos(true);
									break;
								} else {
									backChkDtl.setAlreadyInDrivingPos(false);
								}
							}
							if (backChkDtl.isAlreadyInDrivingPos()) {
								logger.debug("***Candidate already in driving position***");
							} else {
								logger.debug("***Candidate NOT already in driving position***");
							}
						} else {
							logger.debug("Current Job Requirements Not Found");
						}
					}


					if ("INT".equalsIgnoreCase(applicantType)) {
						// Internal Position
						// Check for driving position, will be component id of 3
						for (int i=0; i < newJobReqmntList.size(); i++) {
							if (newJobReqmntList.get(i).getBackgroundCheckSystemComponentId() == 3) {
								backChkDtl.setDrivingPos(true);
								backChkDtl.setBackgroundPackageNeeded(newJobReqmntList.get(i).getBackgroundCheckSystemPackageId());
								break;
							} else {
								backChkDtl.setDrivingPos(false);
							}
						}
						logger.debug(String.format("Background Package Needed:%1$s", backChkDtl.getBackgroundPackageNeeded()));
						if (backChkDtl.isDrivingPos()) {
							logger.debug("***This is driving position***");
						} else {
							logger.debug("***This is NOT driving position***");
						}
						if (backChkDtl.isDrivingPos()) {
							// Check Internal candidate to see if they need
							// another
							// background check for new position
							// Compare currentJobReqmntList with
							// newJobReqmntList to
							// determine if any other components are needed

							StringBuffer neededComponents = new StringBuffer(10);
							boolean needNewComponent = true;
							for (int i = 0; i < newJobReqmntList.size(); i++) {
								int newJobComponent = newJobReqmntList.get(i).getBackgroundCheckSystemComponentId();
								for (int j = 0; j < currentJobReqmntList.size(); j++) {
									if (newJobComponent == currentJobReqmntList.get(j).getBackgroundCheckSystemComponentId()) {
										needNewComponent = false;
										break;
									} else {
										needNewComponent = true;
										hasAllComponentsForNewJob = false;
									}
								}
								if (needNewComponent) {
									neededComponents.append(newJobComponent).append(",");
									needNewComponent = true;
								}
							}

							if (neededComponents.length() > 0) {
								//System.out.println("Needed Components = " + neededComponents.toString());
								// Check internal to see if they have the needed
								// components
								currentIntCandBackgroundResultList = intervwReqMgr.readBackgroundCheckResults(applcantID);
								boolean hasNeededComponent = false;
								int hasNeededComponentIndex = 0;
								// Loop through to see if they have the needed
								// component
								logger.debug("Size of currentIntCandBackgroundResultList = " + currentIntCandBackgroundResultList.size());
								if (currentIntCandBackgroundResultList.size() <= 0) {
									logger.debug("currentIntCandBackgroundResultList was empty, not Candidate Background");
									//System.out.println("Background Check Needed");
									backChkDtl.setNeedsBackgroundCheck(true);
								}

								for (int i = 0; i < currentIntCandBackgroundResultList.size(); i++) {
									// Driving Position
									if (currentIntCandBackgroundResultList.get(i).getBackgroundCheckSystemComponentId() == 3) {
										hasNeededComponent = true;
										hasNeededComponentIndex = i;
										break;
									}
								}

								// Make sure that background check (MVR) is still valid if they
								// already one
								if (hasNeededComponent) {
									int alertStatusCode = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getBackgroundCheckSystemAlertStatusCode();
									Date bgcCompCmpltDt = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getBackgroundCheckSystemComponentCompletedDate();
									int favResltEffDays = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getFavorableResultEffectiveDays();
									int unFavResltEffDays = currentIntCandBackgroundResultList.get(hasNeededComponentIndex)
											.getUnfavorableResultEffectiveDays();

									if (alertStatusCode == 3) {
										// Green
										logger.debug("Green Alert Status Code");
										if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, favResltEffDays)) {
											logger.debug("No Background Needed");
											backChkDtl.setNeedsBackgroundCheck(false);
										} else {
											logger.debug("Background Needed");
											backChkDtl.setNeedsBackgroundCheck(true);
										}
									} else if (alertStatusCode == 1) {
										// Red
										logger.debug("Red Alert Status Code");
										if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
											logger.debug("No Background Needed");
											backChkDtl.setNeedsBackgroundCheck(false);
										} else {
											logger.debug("Background Needed");
											backChkDtl.setNeedsBackgroundCheck(true);
										}
									} else if (alertStatusCode == 2) {
										// Yellow
										logger.debug("Yellow Alert Status Code");
										if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == null) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										} else if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == 1) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, unFavResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										} else if (currentIntCandBackgroundResultList.get(hasNeededComponentIndex).getOverrideAlertStatusCode() == 3) {
											if (isBgcCompCmpltDTGreaterThanCurrentDateLessEffDays(bgcCompCmpltDt, favResltEffDays)) {
												logger.debug("No Background Needed");
												backChkDtl.setNeedsBackgroundCheck(false);
											} else {
												logger.debug("Background Needed");
												backChkDtl.setNeedsBackgroundCheck(true);
											}
										}
									}
								} else {
									// Did not find background information on
									// Candidate
									logger.debug("Background Needed");
									backChkDtl.setNeedsBackgroundCheck(true);
								}
							} else {
								//Internal Driving, Has all components
								if (backChkDtl.getBackgroundPackageNeeded() == 24) {
									//May have all the needed components, but if Asset Protection have to order component 7 again.
									logger.debug("Asset Protection Background Needed");
									backChkDtl.setNeedsBackgroundCheck(true);									
								} else {
									logger.debug("NO Background Needed");
									backChkDtl.setNeedsBackgroundCheck(false);
								}	
							}
						} else {
							// Internal, Not a Driving Position
							logger.debug("NO Background Needed");
							backChkDtl.setNeedsBackgroundCheck(false);
						}
					} else {
						// External Candidate
						logger.debug("This is an External Applicant");
						// Determine what components are needed
						StringBuffer neededComponents = new StringBuffer(10);
						Map<Integer,Integer> neededBgcComponent = new HashMap<>();
						for (int i = 0; i < newJobReqmntList.size(); i++) {
							int newJobComponent = newJobReqmntList.get(i).getBackgroundCheckSystemComponentId();
							logger.debug("New Component = " + newJobComponent);
							logger.debug("Package ID = " + newJobReqmntList.get(i).getBackgroundCheckSystemPackageId());
							backChkDtl.setBackgroundPackageNeeded(newJobReqmntList.get(i).getBackgroundCheckSystemPackageId());
							neededComponents.append(newJobComponent).append(",");
							neededBgcComponent.put(newJobComponent, newJobComponent);
						}

						//For ATS mts1876
						// Check for driving position, will be component id of 3
						for (int i = 0; i < newJobReqmntList.size(); i++) {
							ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO tempDto2 = newJobReqmntList.get(i);
							if (tempDto2.getBackgroundCheckSystemComponentId() == 3) {
								backChkDtl.setDrivingPos(true);
								break;
							} else {
								backChkDtl.setDrivingPos(false);
							}
						}
						if (backChkDtl.isDrivingPos()) {
							logger.debug("***This is driving position***");
						} else {
							logger.debug("***This is NOT driving position***");
						}
						//****************************************************
						
						
						if (neededComponents.length() > 0) {
							logger.debug("Needed Components = " + neededComponents.toString());
							backChkDtl.setBackgroundComponentsNeeded(neededComponents.toString());
							
							currentIntCandBackgroundResultList = intervwReqMgr.readBackgroundCheckResults(applcantID);
							// Loop through to see if they have the needed
							// component
							logger.debug("Size of currentIntCandBackgroundResultList = " + currentIntCandBackgroundResultList.size());
							if (currentIntCandBackgroundResultList.size() <= 0) {
								logger.debug("currentIntCandBackgroundResultList was empty, no Candidate Background");
								backChkDtl.setNeedsBackgroundCheck(true);
							} else {
								//Check to see if the previous results are within 1 year
								Timestamp timestampMinusOneYear = new Timestamp(System.currentTimeMillis());
								Calendar cal = Calendar.getInstance();
								cal.setTime(timestampMinusOneYear);
								cal.add(Calendar.DAY_OF_WEEK, -365);
								timestampMinusOneYear = new Timestamp(cal.getTime().getTime());
								Map<Integer,Integer> haveBgcComponent = new HashMap<>();
								for (int myIndex = 0; myIndex < currentIntCandBackgroundResultList.size(); myIndex++) {
									haveBgcComponent.put(currentIntCandBackgroundResultList.get(myIndex).getBackgroundCheckSystemComponentId()
											, currentIntCandBackgroundResultList.get(myIndex).getBackgroundCheckSystemComponentId());
									logger.debug("Timestamp:" + currentIntCandBackgroundResultList.get(myIndex).getCreateTimestamp());
									if (currentIntCandBackgroundResultList.get(myIndex).getCreateTimestamp().before(timestampMinusOneYear)) {										
										isPreviousResultsWithinAYear=false;
										break;
									}
								}
								logger.debug("isPreviousResultsWithinAYear:" + isPreviousResultsWithinAYear);
								
								if (isPreviousResultsWithinAYear) {
									for (Map.Entry<Integer, Integer> entry : neededBgcComponent.entrySet()) {
										Integer component = entry.getKey();
										logger.debug("Needed Component:" + component);
										Integer hasComponent = haveBgcComponent.get(component);
										if (hasComponent == null) {
											hasAllComponentsForNewJob = false;
										}
									}
										
									if (hasAllComponentsForNewJob) {
										logger.debug("Have All Components");
									} else {
										logger.debug("DO NOT Have All Components");
									}
								} else {
									backChkDtl.setNeedsBackgroundCheck(true);
								}
							} // End if (currentIntCandBackgroundResultList.size() < 0) ELSE
						}
					}

					// Get License Information if it exists for Internals for Driving Positions Only
					if ("INT".equalsIgnoreCase(applicantType) && backChkDtl.isDrivingPos()) {
						licenseInfoList = intervwReqMgr.readBackgroundCheckSystemConsentByInputList(applcantID);
						if (licenseInfoList != null && licenseInfoList.size() > 0) {
							licenseInfo = new ReadBackgroundCheckSystemConsentByInputListDTO();
							licenseInfo.setDriverLicenseNumber(StringUtils.trim(licenseInfoList.get(0).getDriverLicenseNumber()));
							licenseInfo.setDriverLicenseStateCode(StringUtils.trim(licenseInfoList.get(0).getDriverLicenseStateCode()));
							res.setLicenseInfoRes(licenseInfo);
						}
					}
					
						//New Check for Background Consent
						tempBackChkDtl = checkForBackgroundConsentElectronic(applcantID);
						if (tempBackChkDtl == null) {
							//Takes care of Internal that does not need BGC and does not have a Current BGC Consent
							if ("INT".equalsIgnoreCase(applicantType) && !backChkDtl.isNeedsBackgroundCheck()) {
								backChkDtl.setHasSignedConsent(false);
								backChkDtl.setNeedsBackgroundCheck(false);
								backChkDtl.setBackgroundComponentsNeeded("0");
								backChkDtl.setBackgroundPackageNeeded(0);
							} else {
								backChkDtl.setHasSignedConsent(false);
								backChkDtl.setNeedsBackgroundCheck(true);
							}
						} else {
							backChkDtl.setHasSignedConsent(true);
							if ("INT".equalsIgnoreCase(applicantType)) {
								logger.debug("Internal Consent");
								logger.debug("Has All Components:" +  hasAllComponentsForNewJob);
								if (tempBackChkDtl.getConsentCompleteTs() == null && tempBackChkDtl.getConsentStatCd() == 0 && !hasAllComponentsForNewJob) {
									logger.debug("Consent incomplete");
									backChkDtl.setCnsntSigDt(tempBackChkDtl.getCnsntSigDt());
									backChkDtl.setConsentStatCd(tempBackChkDtl.getConsentStatCd());
									backChkDtl.setConsentCompleteTs(tempBackChkDtl.getConsentCompleteTs());
									backChkDtl.setAplcntLKey(tempBackChkDtl.getAplcntLKey());
								} else if (tempBackChkDtl.getConsentCompleteTs() != null && tempBackChkDtl.getConsentStatCd() == 3 && !hasAllComponentsForNewJob) {
									logger.debug("Consent Complete");
									backChkDtl.setCnsntSigDt(tempBackChkDtl.getCnsntSigDt());
									backChkDtl.setConsentStatCd(tempBackChkDtl.getConsentStatCd());
									backChkDtl.setConsentCompleteTs(tempBackChkDtl.getConsentCompleteTs());
									backChkDtl.setAplcntLKey(tempBackChkDtl.getAplcntLKey());
								} else if (!hasAllComponentsForNewJob) {
									logger.debug("Need New Consent");
									backChkDtl.setHasSignedConsent(false);
									backChkDtl.setCnsntSigDt(null);
									backChkDtl.setConsentStatCd(0);
									backChkDtl.setConsentCompleteTs(null);
									backChkDtl.setNeedsBackgroundCheck(true);
								}  else if (hasAllComponentsForNewJob) {
									logger.debug("No New Consent Needed");
									backChkDtl.setCnsntSigDt(tempBackChkDtl.getCnsntSigDt());
									backChkDtl.setConsentStatCd(tempBackChkDtl.getConsentStatCd());
									backChkDtl.setConsentCompleteTs(tempBackChkDtl.getConsentCompleteTs());
									backChkDtl.setAplcntLKey(tempBackChkDtl.getAplcntLKey());
								}
								
							} else 
							//This will cover the External Candidate
							//if ((!isPreviousResultsWithinAYear || !hasAllComponentsForNewJob) && tempBackChkDtl.getAplcntLKey() == null) {
							if (/*!isPreviousResultsWithinAYear ||*/ !hasAllComponentsForNewJob) {
								backChkDtl.setHasSignedConsent(false);
								backChkDtl.setCnsntSigDt(null);
								backChkDtl.setConsentStatCd(0);
								backChkDtl.setConsentCompleteTs(null);
								backChkDtl.setNeedsBackgroundCheck(true);
							} else {
								backChkDtl.setCnsntSigDt(tempBackChkDtl.getCnsntSigDt());
								backChkDtl.setConsentStatCd(tempBackChkDtl.getConsentStatCd());
								backChkDtl.setConsentCompleteTs(tempBackChkDtl.getConsentCompleteTs());
								backChkDtl.setAplcntLKey(tempBackChkDtl.getAplcntLKey());
							}
						}
						
						res.setBackgroundChkDtlRes(backChkDtl);
					
					
					
					
					//Check to see if applicant has SSN is in XREF Table.  Not for Internals
					if ("EXT".equalsIgnoreCase(applicantType)) {
						String returnedSSN = IntervwResultsDAO.getCandSsnIfExists(applcantID);
						if (returnedSSN != null) {
							//Check if Candidate has CPD Form Completed
							BackgroundCheckDtlsTO tempBgcDtls = IntervwResultsDAO.getCpdFormInformation(applcantID);
							if (tempBgcDtls != null) {
								//Make sure that DOB is not default 01-01-0001, candidateInitialsSignatureValue
								//   , documentSignatureTimestamp, backgroundCheckAuthorizationFlag, and managerUserId is not null.
								//If any of these are missing or incorrect then set CPD Form complete to false
								if (tempBgcDtls.getDateOfBirth() == null || tempBgcDtls.getDateOfBirth().equals("0001-01-01") ||
										tempBgcDtls.getCandidateInitialsSignatureValue() == null ||	tempBgcDtls.getCandidateInitialsSignatureValue().equals("") ||
										tempBgcDtls.getDocumentSignatureTimestamp() == null ||
										tempBgcDtls.getManagerUserId() == null || tempBgcDtls.getManagerUserId().equals("")) {
									candinfoDtl.setCpdFormComplete(false);	
								} else {
									candinfoDtl.setCpdFormComplete(true);
									//Check for Rehire Eligible 
									CandidateInfoTO tempCand = IntervwResultsDAO.getRehireEligible(returnedSSN);
									candinfoDtl.setRehireEligible(tempCand.isRehireEligible());
									//Set the data needed for message when a candidate is NOT Rehire Eligible 
									if (!tempCand.isRehireEligible()) {
										candinfoDtl.setTermLoc(tempCand.getTermLoc());
										candinfoDtl.setTermRsn(tempCand.getTermRsn());
										candinfoDtl.setTermDt(tempCand.getTermDt());
									} // End - if (!tempCand.isRehireEligible()) {
								} //  End - else 
							} // End - if (tempBgcDtls != null) {
						} else {
							candinfoDtl.setCpdFormComplete(false);
						} // End - else if (tempBgcDtls != null) {
					} else { // End - if ("EXT".equalsIgnoreCase(applicantType)) {
						//Internal Candidate
						candinfoDtl.setCpdFormComplete(true);
						candinfoDtl.setRehireEligible(true);
					} // End - else if ("EXT".equalsIgnoreCase(applicantType)) {
					
				} // End - if (candInfoTo != null && candInfoTo.size() > 0)
			} // End of if(reqNbr != null && !reqNbr.trim().equals(""))
			
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		return res;
	} // public Response getCandidateInterviewQuestionDetails() throws RetailStaffingException{	
	
} // end of public class RetailStaffingInterviewResultsManager implements
// RetailStaffingConstants{

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
package com.homedepot.hr.hr.retailstaffing.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.model.StatusManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to have the business logic for Search based on Candidate
 * Number. This Class has the functionality of:- getCandidateDetails- Getting
 * the candidate details for the given ssn number.
 * 
 * @author TCS
 * 
 */
public class RetailStaffingCandtDtlDAO implements DAOConstants, RetailStaffingConstants {
	private static final Logger logger = Logger.getLogger(RetailStaffingCandtDtlDAO.class);

	/**
	 * This method will be used for fetching the candidate details from DB.
	 * 
	 * Due to CDP Project had to add candRefId so that the Candidate data can be looked up, the ssnNbr is now the applicantId
	 * the ssnNbr/applicantId is used to get their Requisition and Phone Screen History
	 * @param ssnNbr - The given ssn number.  Is now the applicantId
	 * @param candRefId - Kenexa Candidate Reference Number
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response getCandidateDetails(String ssnNbr, String candRefId) throws RetailStaffingException {
		logger.info(this + "Enters getCandidateDetails method in DAO with input as" + ssnNbr);
		Response res = null;
		RequisitionDetailResponse reqRes = null;
		CandidateDetailResponse cndRes = null;
		ITIDetailResponse itiRes = null;
		List<RequisitionDetailTO> reqlist = null;
		List<CandidateDetailsTO> cndList = null;
		List<PhoneScreenIntrwDetailsTO> phoneIntwList = null;
		CandidateDetailsTO candidateDetailsTO = null;
		try {

			//Need to left Pad Candidate Id with zeros so that it is a 10 digit number
			String paddedCandRefId = candRefId;
			if (candRefId.length() < 10) {
				paddedCandRefId = String.format("%010d", Long.parseLong(candRefId));
			}
			
			if (ssnNbr != null && !ssnNbr.trim().equals("") && candRefId != null && !candRefId.trim().equals("")) {
				res = new Response();
				reqRes = new RequisitionDetailResponse();
				cndRes = new CandidateDetailResponse();
				itiRes = new ITIDetailResponse();
				// Get the candidate details.
				cndList = getCandidateDetail(paddedCandRefId);
				if (cndList != null && cndList.size() > 0) {
					candidateDetailsTO = (CandidateDetailsTO) cndList.get(0);
					candidateDetailsTO.setSsnNbr(ssnNbr);
					cndList.remove(0);
					cndList.add(candidateDetailsTO);
				}
				cndRes.setCndDtlList(cndList);
				res.setCanDtRes(cndRes);
				// Get the requisitions associated with the candidate.
				reqlist = getRequisitionDetailsForCandidate(ssnNbr);
				reqRes.setReqDtlList(reqlist);
				res.setReqDtlList(reqRes);
				// Get the phone interview associated with the candidate.
				phoneIntwList = getPhoneIntwDetailsForCandidate(ssnNbr);
				itiRes.setITIDtlList(phoneIntwList);
				res.setItiDtRes(itiRes);

			} else {
				logger.info(this + "Input is either null or empty string.");
				throw new RetailStaffingException(INVALID_INPUT_CODE);
			}
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(FETCHING_CANDIDATE_DETAILS_ERROR_CODE,
					FETCHING_CANDIDATE_DETAILS_ERROR_MSG + "candidate id: " + ssnNbr, e);
		}
		logger.info(this + "Leaves getCandidateDetails method in DAO with output as" + res);
		return res;

	}

	/**
	 * The method will be used for getting the basic candidate details from the
	 * DB.
	 * 
	 * @param ssnNbr - The Kenexa Candidate Ref Id.
	 *            
	 * @return list of CandidateDetailsTO
	 * @throws QueryException
	 */
	public static List<CandidateDetailsTO> getCandidateDetail(String ssnNbr) throws QueryException {
		final List<CandidateDetailsTO> cndList = new ArrayList<CandidateDetailsTO>();
		MapStream inputs = new MapStream("readExternalCandidateDetails");
		inputs.put("employmentCandidateId", ssnNbr);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				CandidateDetailsTO candidateDetailsTO = null;

				if (results.next()) {
					candidateDetailsTO = new CandidateDetailsTO();
					candidateDetailsTO.setSsnNbr(StringUtils.trim(results.getString("employmentApplicantId")));
					candidateDetailsTO.setName(Util.getCandidateName(results.getString("lastName"), results.getString("firstName"), results
							.getString("middleInitialName"), results.getString("suffixName")));

					//Added for CDP, remove leading zeros for display purposes.
					candidateDetailsTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
					
					cndList.add(candidateDetailsTO);
				}
			}
		});
		// For reading the internal candidate details
		inputs = new MapStream("readInternalCandidateDetails");
		inputs.put("employmentCandidateId", ssnNbr);
		inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(ACTV_FLG, true);
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);
		inputs.put("humanResourcesSystemCountryCode", "USA");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

				CandidateDetailsTO candidateDetailsTO = null;
				if (results.next()) {
					if (cndList != null && cndList.size() == 0) {
						candidateDetailsTO = new CandidateDetailsTO();
						candidateDetailsTO.setAid(StringUtils.trim(results.getString("zEmplid")));
						candidateDetailsTO.setName(StringUtils.trim(results.getString("name")));

						//Added for CDP, remove leading zeros for display purposes.
						candidateDetailsTO.setSsnNbr(StringUtils.trim(results.getString("employmentApplicantId")));
						candidateDetailsTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						cndList.add(candidateDetailsTO);
					}
				}

			}
		});
		return cndList;
	}

	/**
	 * The method will be used for getting the basic candidate details from the
	 * DB.
	 * 
	 * @param last4Ssn - Last 4 digits of SSN
	 * @param lastName - Partial/Full Last Name
	 *            
	 * @return list of CandidateDetailsTO
	 * @throws QueryException
	 */
	public List<CandidateDetailsTO> getCandidateDetail(String last4Ssn, String lastName) throws QueryException {
		final List<CandidateDetailsTO> cndList = new ArrayList<CandidateDetailsTO>();
		
		MapStream inputs = new MapStream("readExternalCandidateDetails");
		inputs.put("socialSecurityNumberSuffixNumber", last4Ssn);
		inputs.put("partialLastName", lastName.toUpperCase());

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				CandidateDetailsTO candidateDetailsTO = null;

				while (results.next()) {
					candidateDetailsTO = new CandidateDetailsTO();
					candidateDetailsTO.setSsnNbr(StringUtils.trim(results.getString("employmentApplicantId")));
					candidateDetailsTO.setName(Util.getCandidateName(results.getString("lastName"), results.getString("firstName"), results
							.getString("middleInitialName"), results.getString("suffixName")));

					//Added for CDP, remove leading zeros for display purposes.
					candidateDetailsTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
					
					cndList.add(candidateDetailsTO);
				}
			}
		});
		// For reading the internal candidate details
		inputs = new MapStream("readInternalCandidateDetails");
		inputs.put("socialSecurityNumberSuffixNumber", last4Ssn);
		inputs.put("partialName", lastName.toUpperCase());
		inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(ACTV_FLG, true);
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);	
		inputs.put("humanResourcesSystemCountryCode", "USA");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {

				CandidateDetailsTO candidateDetailsTO = null;
				while (results.next()) {
				//	if (cndList != null && cndList.size() == 0) {
						candidateDetailsTO = new CandidateDetailsTO();
						candidateDetailsTO.setAid(StringUtils.trim(results.getString("zEmplid")));
						candidateDetailsTO.setName(StringUtils.trim(results.getString("name")));

						//Added for CDP, remove leading zeros for display purposes.
						candidateDetailsTO.setSsnNbr(StringUtils.trim(results.getString("employmentApplicantId")));
						candidateDetailsTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
						
						cndList.add(candidateDetailsTO);
				//	}
				}

			}
		}); 
		return cndList;
	}
	
	/**
	 * This method will be used for getting the requisition details assosiated
	 * with the candidate.
	 * 
	 * @param ssnNbr
	 *            - the ssn number of the candidate.
	 * @return list of RequisitionDetailTO
	 * @throws QueryException
	 */
	public List<RequisitionDetailTO> getRequisitionDetailsForCandidate(String ssnNbr) throws QueryException {
		final ArrayList<RequisitionDetailTO> reqlist = new ArrayList<RequisitionDetailTO>();
		MapStream inputs = new MapStream("readRequisitionHistory");
		inputs.put("employmentPositionCandidateId", ssnNbr);
		inputs.put("effectiveBeginDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("effectiveEndDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThanEqualTo", true);			
		
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				RequisitionDetailTO requisitionDetailTO = null;

				while (results.next()) {
					requisitionDetailTO = new RequisitionDetailTO();
					if (!results.wasNull("employmentRequisitionNumber")) {
						requisitionDetailTO.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
					}
					if (!results.wasNull("activeFlag")) {
						if (results.getBoolean("activeFlag") == true) {
							requisitionDetailTO.setActive(TRUE);
						} else if (results.getBoolean("activeFlag") == false) {
							requisitionDetailTO.setActive(FALSE);
						}

					}
					if (!results.wasNull("activeFlag1")) {
						if (results.getBoolean("activeFlag1") == true) {
							requisitionDetailTO.setCanStatus(TRUE);
						} else if (results.getBoolean("activeFlag1") == false) {
							requisitionDetailTO.setCanStatus(FALSE);
						}

					}
					requisitionDetailTO.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
					requisitionDetailTO.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
					requisitionDetailTO.setJob(StringUtils.trim(results.getString("jobTitleCode")));
					requisitionDetailTO.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
					requisitionDetailTO.setIntStatus(StringUtils.trim(results.getString("interviewStatusIndicator")));
					//This is not a Flag true/false it is a string
					if (!results.wasNull("candidateOfferMadeFlag")) {
						requisitionDetailTO.setOfferMade(results.getString("candidateOfferMadeFlag"));
					}

					requisitionDetailTO.setOfferDate(Util.converDatetoDateTO(results.getDate("candidateOfferMadeDate")));
					requisitionDetailTO.setOfferStat(StringUtils.trim(results.getString("candidateOfferStatusIndicator")));

					if (!results.wasNull("offerDeclineReasonCode")) {
						int declineCode = results.getInt("offerDeclineReasonCode");
						switch (declineCode) {
						case 0:
							requisitionDetailTO.setDecCD(" ");
							break;
						case 1:
							requisitionDetailTO.setDecCD("PAY");
							break;
						case 2:
							requisitionDetailTO.setDecCD("BENEFITS");
							break;
						case 3:
							requisitionDetailTO.setDecCD("HOURS");
							break;
						case 4:
							requisitionDetailTO.setDecCD("ACCEPTED ANOTHER OFFER");
							break;
						case 5:
							requisitionDetailTO.setDecCD("OTHER");
							break;
						case 6:
							requisitionDetailTO.setDecCD("LOCATION");
							break;
						case 7:
							requisitionDetailTO.setDecCD("REFUSED DRUG TEST");
							break;
						}
					}
					// if (!results.wasNull("offerDeclineReasonCode"))
					// {
					// requisitionDetailTO
					// .setDecCD(Integer
					// .toString(results
					// .getInt("offerDeclineReasonCode")));
					// }

					requisitionDetailTO.setDrugTest(StringUtils.trim(results.getString("drugTestResultCode")));
					reqlist.add(requisitionDetailTO);
				}
			}
		});
		return reqlist;
	}

	/**
	 * The method will be used for getting the phone screen details assosiated
	 * with the candidate.
	 * 
	 * @param ssnNbr
	 *            - the ssn number of the candidate.
	 * @return list of PhoneScreenIntrwDetailsTO
	 * @throws QueryException
	 */
	private List<PhoneScreenIntrwDetailsTO> getPhoneIntwDetailsForCandidate(String ssnNbr) throws QueryException {
		final ArrayList<PhoneScreenIntrwDetailsTO> phoneIntwList = new ArrayList<PhoneScreenIntrwDetailsTO>();
		MapStream inputs = new MapStream("readHumanResourcesPhoneScreenInterviewDetails");
		inputs.put("employmentPositionCandidateId", ssnNbr);
		inputs.put("tabno", TABNO_DEPT_NO);
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO = null;

				while (results.next()) {
					phoneScreenIntrwDetailsTO = new PhoneScreenIntrwDetailsTO();
					if (!results.wasNull("humanResourcesPhoneScreenId")) {
						phoneScreenIntrwDetailsTO.setItiNbr(Integer.toString(results.getInt("humanResourcesPhoneScreenId")));
					}
					if (!results.wasNull("employmentRequisitionNumber")) {
						phoneScreenIntrwDetailsTO.setReqNbr(Integer.toString(results.getInt("employmentRequisitionNumber")));
					}
					if (!results.wasNull("phoneScreenStatusCode")) {
						phoneScreenIntrwDetailsTO.setPhoneScreenStatusCode(Integer.toString(results.getInt("phoneScreenStatusCode")));
					}
					if (!results.wasNull("interviewStatusCode")) {
						phoneScreenIntrwDetailsTO.setInterviewStatusCode(Integer.toString(results.getInt("interviewStatusCode")));
					}

					if (!results.wasNull("phoneScreenStatusDescription")) {
						phoneScreenIntrwDetailsTO.setPhoneScreenStatusDesc(results.getString("phoneScreenStatusDescription"));
					}
					
					if (!results.wasNull("statusDescription")) {
						phoneScreenIntrwDetailsTO.setInterviewStatusDesc(results.getString("statusDescription"));
					}					
					
					if (!results.wasNull("interviewDate")) {
						IntrwLocationDetailsTO intrLocDtls = new IntrwLocationDetailsTO();
						intrLocDtls.setInterviewDate(Util.converDatetoDateTO(results.getDate("interviewDate")));
						phoneScreenIntrwDetailsTO.setIntrLocDtls(intrLocDtls);
					}
					phoneScreenIntrwDetailsTO.setCndStrNbr(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
					phoneScreenIntrwDetailsTO.setCndDeptNbr(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
					phoneScreenIntrwDetailsTO.setJob(StringUtils.trim(results.getString("jobTitleCode")));
					phoneScreenIntrwDetailsTO.setScrTyp(StringUtils.trim(results.getString("oe31")));
					phoneIntwList.add(phoneScreenIntrwDetailsTO);
				}
			}
		});
		return phoneIntwList;
	}
}

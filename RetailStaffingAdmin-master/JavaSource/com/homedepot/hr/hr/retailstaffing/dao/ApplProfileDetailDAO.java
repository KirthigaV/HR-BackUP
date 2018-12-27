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

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.ApplAppHistoryInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplAppHistoryInfoTO2;
import com.homedepot.hr.hr.retailstaffing.dto.ApplEducationInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplHistoryInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplLangInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplPersonalInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplPhnScreenInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplWorkHistoryInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.AssociatePrePosTO;
import com.homedepot.hr.hr.retailstaffing.dto.AssociateReviewTO;
import com.homedepot.hr.hr.retailstaffing.dto.AssociateWorkInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.model.StatusManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to get applicant profile
 * 
 * 
 * @author TCS
 * 
 */
public class ApplProfileDetailDAO implements DAOConstants, RetailStaffingConstants
{
	private static final Logger logger = Logger.getLogger(PhoneScreenDAO.class);
	
	private static final List<String> ASSOCIATE_COMPENSATION_ACTION_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -6907301867189516978L;

		{
			add("IR"); 
			add("RR");
			add("MR");
			add("FR");
			add("RM");
		} // end constructor block
	}; // end collection ASSOCIATE_COMPENSATION_ACTION_LIST
	
	// fixing profile production issue
	private String emplApplicationDate = "";

	/**
	 * This method is used for getting the applicant personal info.
	 * 
	 * @param ssnNbr
	 *            - the candidate id (ssn) number.
	 * 
	 * @return list of personal info details
	 * @throws QueryException
	 */
	// personal info
	public List<ApplPersonalInfoTO> getApplPersonalInfo(String ssnNumber, String applType) throws RetailStaffingException
	{
		final List<ApplPersonalInfoTO> applPersonalInfoList = new ArrayList<ApplPersonalInfoTO>();
		final String ssnNbr = ssnNumber;
		final String applCode = applType;
		String sqlStr = "";
		
		if (applType.equalsIgnoreCase("AP")) {
			sqlStr = "readEmploymentApplicantDetailsByActiveFlagApplicantId";
		} 
		if (applType.equalsIgnoreCase("AS")) {
			sqlStr = "readPersonProfilesByPersonId";
		} 
		
		MapStream inputs = new MapStream(sqlStr);
		try {
			if (applType.equalsIgnoreCase("AP")) {
			inputs.put("employmentApplicantId", ssnNbr);
			inputs.put("activeFlag", true);
			inputs.put("tabno", TABNO_DEPT_NO);
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplPersonalInfoTO applPersonalInfoTO = null;

						while (results.next()){
							applPersonalInfoTO = new ApplPersonalInfoTO();
							// name
							String lastName = "";
							String firstName = "";
							String mi = "";
							String suffixName = "";
							
							applPersonalInfoTO.setApplType(applCode);
							
							if (!results.wasNull("lastName")) {
								lastName = StringUtils.trim(results.getString("lastName")) + ",";
							}
							if (!results.wasNull("firstName")) {
								firstName = StringUtils.trim(results.getString("firstName"));
							}
							if (!results.wasNull("middleInitialName")) {
								mi = StringUtils.trim(results.getString("middleInitialName"));
							}
							if (!results.wasNull("suffixName")) {
								suffixName = StringUtils.trim(results.getString("suffixName"));
							}
							
							String fullName = lastName + " " + firstName + " " + mi + " " + suffixName;
							applPersonalInfoTO.setFullName(fullName);
							// phone number			
							if (!results.wasNull("phoneAreaCityCode")) {
								applPersonalInfoTO.setPhoneNum(
										StringUtils.trim(results.getString("phoneAreaCityCode")) +
										StringUtils.trim(results.getString("phoneLocalNumber")));
							} 
							if (!results.wasNull("addressLineOneText")) {
								applPersonalInfoTO.setAddress1(StringUtils.trim(results.getString("addressLineOneText")));
							} 
							if (!results.wasNull("addressLineTwoText")) {
								applPersonalInfoTO.setAddress2(StringUtils.trim(results.getString("addressLineTwoText")));
							} 
							
							String city = "";
							String state = "";
							String zip = "";
							String cityStateZip = "";
							
							if (!results.wasNull("cityName")) {
								city = StringUtils.trim(results.getString("cityName")) + ",";
							} 
							if (!results.wasNull("stateCode")) {
								state = StringUtils.trim(results.getString("stateCode"));
							} 
							if (!results.wasNull("longZipCodeCode")) {
								zip = StringUtils.trim(results.getString("longZipCodeCode"));
							} 
							
							cityStateZip = city + " " + state + " " + zip;
							applPersonalInfoTO.setCityStateZip(cityStateZip);
							
							// SSN 
							applPersonalInfoTO.setApplID(ssnNbr);
							
							//Added for CDP, remove leading zeros for display purposes.
							applPersonalInfoTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));							
							
							// email 
							if (!results.wasNull("electronicMailAddressText")) {
								applPersonalInfoTO.setEmailAddress(StringUtils.trim(results.getString("electronicMailAddressText")));
							} 							
							// US eligibility
							if (!results.wasNull("legalEmploymentEligibilityFlag")) {
								if (results.getBoolean("legalEmploymentEligibilityFlag")) {
									applPersonalInfoTO.setUsaEligibility(TRUE);
								} else {
									applPersonalInfoTO.setUsaEligibility(FALSE);
								}
							}
							// over 18
							if (!results.wasNull("minimumEmploymentAgeFlag")) {
								if (results.getBoolean("minimumEmploymentAgeFlag")) {
									applPersonalInfoTO.setOver18(TRUE);
								} else {
									applPersonalInfoTO.setOver18(FALSE);
								}
							}
							
							// prior conviction
							// prior conviction date
							if (!results.wasNull("felonyMisdemeanorDate")) {
								if (StringUtils.trim((results.getDate("felonyMisdemeanorDate")).toString() ).length() > 0 ) {
									applPersonalInfoTO.setPriorConvictionDate((results.getDate("felonyMisdemeanorDate")).toString());
									applPersonalInfoTO.setPriorConviction(TRUE);
								}
							}
							
							// prior conviction text
							if (!results.wasNull("felonyMisdemeanorText")) {
								if (StringUtils.trim(results.getString("felonyMisdemeanorText")).length() > 0 ) {
									applPersonalInfoTO.setPriorConvictionText(StringUtils.trim(results.getString("felonyMisdemeanorText")));
									applPersonalInfoTO.setPriorConviction(TRUE);
								}
							}
							// on leave of laid off
							if (!results.wasNull("currentLeaveOfAbsenceLayoffFlag")) {
								if (results.getBoolean("currentLeaveOfAbsenceLayoffFlag")) {
									applPersonalInfoTO.setOnLeaveOrLaidOff(TRUE);
								} else {
									applPersonalInfoTO.setOnLeaveOrLaidOff(FALSE);
								}
							}
							// previous employee
							applPersonalInfoTO.setPreEmployee(FALSE);
							// previous location
							if (!results.wasNull("previousPositionLocationDescription")) {
								if (StringUtils.trim(results.getString("previousPositionLocationDescription")).length() > 0) {
									applPersonalInfoTO.setLocation(StringUtils.trim(results.getString("previousPositionLocationDescription")));
									applPersonalInfoTO.setPreEmployee(TRUE);
								}
							} 
							// previous date from
							if (!results.wasNull("previousThdPositionBeginDate")) {
								applPersonalInfoTO.setDateFrom((results.getDate("previousThdPositionBeginDate")).toString());
								applPersonalInfoTO.setPreEmployee(TRUE);
							} 
							// previous date to
							if (!results.wasNull("previousThdPositionEndDate")) {
								applPersonalInfoTO.setDateTo((results.getDate("previousThdPositionEndDate")).toString());
								applPersonalInfoTO.setPreEmployee(TRUE);
							} 
							
							// relative at HD
							applPersonalInfoTO.setRelWorkingForHD(FALSE);
							// relative name
							if (!results.wasNull("relativeEmployeeName")) {
								if (StringUtils.trim(results.getString("relativeEmployeeName")).length() > 0) {
									applPersonalInfoTO.setRelName(StringUtils.trim(results.getString("relativeEmployeeName")));
									applPersonalInfoTO.setRelWorkingForHD(TRUE);
								}
							} 	
							// military experience and branch
							applPersonalInfoTO.setMilExperience(FALSE);
							if (!results.wasNull("militaryBranchName")) {
								if (StringUtils.trim(results.getString("militaryBranchName")).length() > 0) {
									applPersonalInfoTO.setMilBranchName(StringUtils.trim(results.getString("militaryBranchName")));
									applPersonalInfoTO.setMilExperience(TRUE);
								}
							}
							if (!results.wasNull("militaryServiceBeginDate")) {
								if (StringUtils.trim((results.getDate("militaryServiceBeginDate")).toString()).length() > 0) {
									applPersonalInfoTO.setMilDateFrom(StringUtils.trim((results.getDate("militaryServiceBeginDate")).toString()));
									applPersonalInfoTO.setMilExperience(TRUE);
								}
							} 
							if (!results.wasNull("militaryServiceEndDate")) {
								if (StringUtils.trim((results.getDate("militaryServiceEndDate")).toString()).length() > 0) {
									applPersonalInfoTO.setMilDatTo(StringUtils.trim((results.getDate("militaryServiceEndDate")).toString()));
									applPersonalInfoTO.setMilExperience(TRUE);
								}
							} 
							// job type, pay and etc.., for external applicant only
							if (applCode.equalsIgnoreCase("AP")) {
								if (!results.wasNull("fullTimeOkayFlag")) {
									if (results.getBoolean("fullTimeOkayFlag")) {
										applPersonalInfoTO.setJobFullTime(TRUE);
									}
								} 
								if (!results.wasNull("partTimeOkayFlag")) {
									if (results.getBoolean("partTimeOkayFlag")) {
										applPersonalInfoTO.setJobPartTime(TRUE);
									}
								} 
								
								if (!results.wasNull("planWorkDurationIndicator")) {
									String workDuration = StringUtils.trim((results.getString("planWorkDurationIndicator")));
									if (workDuration != null && workDuration.equalsIgnoreCase("T")) {
										applPersonalInfoTO.setJobTemp(TRUE);
									}
									if (workDuration != null && workDuration.equalsIgnoreCase("M")) {
										applPersonalInfoTO.setJobPerm(TRUE);
									}
								}

								if (!results.wasNull("requestBasePayRate")) {
									applPersonalInfoTO.setWageDesired(StringUtils.trim((results.getBigDecimal("requestBasePayRate")).toString()));
								}
							}
							
							// fixing profile issue 06-06-2011
							if (!results.wasNull("employmentApplicationDate")) {
								if (StringUtils.trim((results.getDate("employmentApplicationDate")).toString()).length() > 0) {
									applPersonalInfoTO.setEmplApplicationDate(StringUtils.trim((results.getDate("employmentApplicationDate")).toString()));
								}
							} 
							
							applPersonalInfoList.add(applPersonalInfoTO);
						}
					}
				});
			} 
			// applicant
			
			// assocaite
			if (applType.equalsIgnoreCase("AS")) {
				
				inputs.put("employmentApplicantId", ssnNbr);
				inputs.put("tabno", TABNO_DEPT_NO);
				inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
				inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
				inputs.put(ACTV_FLG, true);
				inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
				inputs.addQualifier("effectiveEndDateGreaterThan", true);
				inputs.put("humanResourcesSystemCountryCode", "USA");
				
				BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION,
					inputs, new ResultsReader()
					{
						public void readResults(Results results, Query query,
								Inputs inputs) throws QueryException
						{
							ApplPersonalInfoTO applPersonalInfoTO = null;
							
							while (results.next()){
								applPersonalInfoTO = new ApplPersonalInfoTO();
								
								applPersonalInfoTO = new ApplPersonalInfoTO();
								applPersonalInfoTO.setApplType("AS");
								if (!results.wasNull("name")) {
									applPersonalInfoTO.setFullName(StringUtils.trim(results.getString("name")));
								} 
								if (!results.wasNull("phoneNumber")) {
									applPersonalInfoTO.setPhoneNum(StringUtils.trim(results.getString("phoneNumber")));
								}		
								if (!results.wasNull("addressLine1Long")) {
									applPersonalInfoTO.setAddress1(StringUtils.trim(results.getString("addressLine1Long")));
								} 
								if (!results.wasNull("addressLine2Long")) {
									applPersonalInfoTO.setAddress2(StringUtils.trim(results.getString("addressLine2Long")));
								} 
								
								String city = "";
								String state = "";
								String zip = "";
								String cityStateZip = "";
								
								if (!results.wasNull("addressCity")) {
									city = StringUtils.trim(results.getString("addressCity")) + ",";
								} 
								if (!results.wasNull("stateProvince")) {
									state = StringUtils.trim(results.getString("stateProvince"));
								} 
								if (!results.wasNull("postalCode11")) {
									zip = StringUtils.trim(results.getString("postalCode11"));
								} 
								cityStateZip = city + " " + state + " " + zip;
								applPersonalInfoTO.setCityStateZip(cityStateZip);
								// SSN 
								applPersonalInfoTO.setApplID(ssnNbr);

								//Added for CDP, remove leading zeros for display purposes.
								applPersonalInfoTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
								
								// email 
								//no email info for internal

								applPersonalInfoList.add(applPersonalInfoTO);
							}
						}
					});
				}	
			// associate
	
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applPersonalInfoList;
	}
	// personal info
	
	// associate current work info
	public List<AssociateWorkInfoTO> getAssociateInfo(String ssnNbr) throws RetailStaffingException
	{
		final List<AssociateWorkInfoTO> associateInfoList = new ArrayList<AssociateWorkInfoTO>();
		MapStream inputs = new MapStream("readPersonProfilesByPersonId");
		try {
			inputs.put("employmentApplicantId", ssnNbr);
			inputs.put("tabno", TABNO_DEPT_NO);
			inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(ACTV_FLG, true);
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThan", true);
			inputs.put("humanResourcesSystemCountryCode", "USA");
			
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						AssociateWorkInfoTO associateInfoTO = null;

						while (results.next()){
							associateInfoTO = new AssociateWorkInfoTO();
							
							if (!results.wasNull("organization1")){
								associateInfoTO.setCurrStore(StringUtils.trim(results.getString("organization1")));
							} 
							if (!results.wasNull("organization2")){
								associateInfoTO.setCurrDept(StringUtils.trim(results.getString("organization2")));
							} 
							if (!results.wasNull("jobTitleId")){
								associateInfoTO.setCurrTitle(StringUtils.trim(results.getString("jobTitleId")));
							} 
							if (!results.wasNull("employmentCat")){
								associateInfoTO.setCurrStatus(StringUtils.trim(results.getString("employmentCat")));
							} 
							if (!results.wasNull("hireDateOrigin")){
								associateInfoTO.setHireDate(results.getDate("hireDateOrigin").toString());
							} 
							
							associateInfoList.add(associateInfoTO);
							
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return associateInfoList;
	}
	// associate info history
	
	// associate review results
	public List<AssociateReviewTO> getAssociateReview(String ssnNbr) throws RetailStaffingException
	{
		final List<AssociateReviewTO> associateReviewTOList = new ArrayList<AssociateReviewTO>();
		MapStream inputs = new MapStream("readEmployeeCompensationByInputList");
		try {
			inputs.put("employmentApplicantId", ssnNbr);
			inputs.put("recordStatus", "A");
			inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(ACTV_FLG, true);
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThan", true);
			List<Object> inputCompensationActionList = new ArrayList<Object>();
			inputCompensationActionList.add("IR");
			inputCompensationActionList.add("RR");
			inputCompensationActionList.add("MR");
			inputCompensationActionList.add("FR");
			inputCompensationActionList.add("RM");
			inputs.put("compensationActionList", inputCompensationActionList);
			inputs.put("humanResourcesSystemCountryCode", "USA");
			
			//inputs.put("tabno", TAB_NO);
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						AssociateReviewTO associateReviewTO = null;

						while (results.next()){
							associateReviewTO = new AssociateReviewTO();

							if (!results.wasNull("userCompensationDate2")) {
								associateReviewTO.setDate((results.getDate("userCompensationDate2")).toString());
							} 
							if (!results.wasNull("userCompensationField1")) {
								associateReviewTO.setResults(StringUtils.trim(results.getString("userCompensationField1")));
							}
							
							associateReviewTOList.add(associateReviewTO);
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return associateReviewTOList;
	}
	// associate review
	
	// associate previous positions
	public List<AssociatePrePosTO> getAssociatePrevPosistion(String ssnNbr) throws RetailStaffingException
	{
		final List<AssociatePrePosTO> associatePrePosTOList = new ArrayList<AssociatePrePosTO>();

		MapStream inputs = new MapStream("readEmployeePositionByInputList");
		try {
			inputs.put("employmentApplicantId", ssnNbr);
			inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(ACTV_FLG, true);
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThan", true);
			inputs.put("humanResourcesSystemCountryCode", "USA");
			
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						AssociatePrePosTO associatePrePosTO = null;

						while (results.next()){
							associatePrePosTO = new AssociatePrePosTO();
							//
							if (!results.wasNull("organization2")) {
								associatePrePosTO.setDept(StringUtils.trim(results.getString("organization2")));
							}
							if (!results.wasNull("jobTitleId")) {
								associatePrePosTO.setPosition(StringUtils.trim(results.getString("jobTitleId")));
							}
							if (!results.wasNull("relatedBeginDate")) {
								associatePrePosTO.setDateFrom((results.getDate("relatedBeginDate")).toString());
							}
							if (!results.wasNull("relatedEndDate")) {
								associatePrePosTO.setDateTo((results.getDate("relatedEndDate")).toString());
							}

							associatePrePosTOList.add(associatePrePosTO);
							
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return associatePrePosTOList;
	}
	// associate previous positions
	
	// education
	public List<ApplEducationInfoTO> getApplEducationInfo(String ssnNbr, String applType, String emplApplicationDate) throws RetailStaffingException
	{
		final List<ApplEducationInfoTO> applEducationInfoList = new ArrayList<ApplEducationInfoTO>();
		
		String sqlStr = "";
		String sqlInputName = "";
		if (applType.equalsIgnoreCase("AP")) {
			sqlStr = "readApplicantEducation";
			sqlInputName = "employmentApplicantId";	
		} else if (applType.equalsIgnoreCase("AS")) {
			sqlStr = "readAssociateEducation";
			sqlInputName = "humanResourcesSystemEmployeeId";
		} else {};
		
		MapStream inputs = new MapStream(sqlStr);
		try {
			inputs.put(sqlInputName, ssnNbr);
			inputs.put("tabno", TABNO_DEPT_NO);
			// fixing profile issue 06-06-2011 
			if (applType.equalsIgnoreCase("AP") && emplApplicationDate != null && emplApplicationDate.length() > 0) {
				java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
				inputs.put("employmentApplicationDate", sqlEmplApplDate);
			}
			
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplEducationInfoTO applEducationInfoTO = null;

						while (results.next()){
							applEducationInfoTO = new ApplEducationInfoTO();

							// school name
							if (!results.wasNull("schoolName")) {
								applEducationInfoTO.setSchoolName(StringUtils.trim(results.getString("schoolName")));
							} 
							// education
							if (!results.wasNull("degreeCertificateDescription")) {
								applEducationInfoTO.setEducation(StringUtils.trim(results.getString("degreeCertificateDescription")));
							} 
							// graduate
							if (results.wasNull("completeYearCount")) {
								int completeYear = results.getInt("completeYearCount");
								applEducationInfoTO.setGraduate(FALSE);
							} else {
								applEducationInfoTO.setGraduate(TRUE);
							}					

							applEducationInfoList.add(applEducationInfoTO);
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applEducationInfoList;
	}
	// education
	
	// work history
	public List<ApplWorkHistoryInfoTO> getApplWorkHistroyInfo(String ssnNbr, String applType, String emplApplicationDate) throws RetailStaffingException
	//public List<ApplWorkHistoryInfoTO> getApplWorkHistroyInfo(String ssnNbr, String applType) throws RetailStaffingException
	{
		final List<ApplWorkHistoryInfoTO> applWorkHistoryInfoList = new ArrayList<ApplWorkHistoryInfoTO>();
		
		String sqlStr = "";
		String sqlInputName = "";
		if (applType.equalsIgnoreCase("AP")) {
			sqlStr = "readApplicantPreviousEmployment";
			sqlInputName = "employmentApplicantId";
		} if (applType.equalsIgnoreCase("AS")) {
			sqlStr = "readAssociatePreviousEmployment";
			sqlInputName = "humanResourcesSystemEmployeeId";
		}
		
		MapStream inputs = new MapStream(sqlStr);

		try {

			if (applType.equalsIgnoreCase("AP")) {
				inputs.put("employmentApplicantId", ssnNbr);
				// fixing profile issue 06-06-2011 
				if (emplApplicationDate != null && emplApplicationDate.length() > 0) {
					java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
					inputs.put("employmentApplicationDate", sqlEmplApplDate);
				}
			}
			if (applType.equalsIgnoreCase("AS")) {
				inputs.put("humanResourcesSystemEmployeeId", ssnNbr);
			}
			
			//inputs.put("tabno", TAB_NO);
			BasicDAO.getResult("HrHrStaffing", 1, 1,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplWorkHistoryInfoTO applWorkHistoryInfoTO = null;

						while (results.next()){
							applWorkHistoryInfoTO = new ApplWorkHistoryInfoTO();
							
							if (!results.wasNull("employerName")) {
								applWorkHistoryInfoTO.setCompany(StringUtils.trim(results.getString("employerName")));
							} 
							if (!results.wasNull("workTypeText")) {
								applWorkHistoryInfoTO.setPosition(StringUtils.trim(results.getString("workTypeText")));
							} 
					
							String city = "";
							String state = "";
							String location = city + state;
							if (!results.wasNull("cityName")) {
								city = StringUtils.trim(results.getString("cityName")) + ",";
							}
							if (!results.wasNull("stateCode")) {
								state = StringUtils.trim(results.getString("stateCode"));
							}
							applWorkHistoryInfoTO.setLocation(location);
							if (!results.wasNull("supervisorName")) {
								applWorkHistoryInfoTO.setSupervisor(StringUtils.trim(results.getString("supervisorName")));
							} 
							if (!results.wasNull("workBeginDate")) {
								applWorkHistoryInfoTO.setComDateFrom((results.getDate("workBeginDate")).toString());
							} 
							if (!results.wasNull("workEndDate")) {
								applWorkHistoryInfoTO.setComDateTo((results.getDate("workEndDate")).toString());
							} 
							if (!results.wasNull("basePayRate")) {
								applWorkHistoryInfoTO.setPayAtLeaving((results.getBigDecimal("basePayRate")).toString());
							} 
							if (!results.wasNull("supervisorTitleDescription")) {
								applWorkHistoryInfoTO.setSupervisorTitle(StringUtils.trim(results.getString("supervisorTitleDescription")));
							}
							
							if (!results.wasNull("leaveReasonText")) {
								applWorkHistoryInfoTO.setReasonLeaving(StringUtils.trim(results.getString("leaveReasonText")));
							}
							
							applWorkHistoryInfoList.add(applWorkHistoryInfoTO);
							
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applWorkHistoryInfoList;
	}
	// work history
	
	// job preference
	public List<SchedulePreferenceTO> getApplJobPrefInfo(String ssnNbr) throws RetailStaffingException
	{
		final List<SchedulePreferenceTO> applJobPrefInfoList = new ArrayList<SchedulePreferenceTO>();

		MapStream inputs = new MapStream("readApplicantDailyAvailabilityByEmploymentApplicantId");
	
		try {
			inputs.put("dayLanguageCode", "EN_US");
			inputs.put("weekLanguageCode", "en_US");
			inputs.put("employmentApplicantId", ssnNbr);
			
			BasicDAO.getResult("HrHrStaffing", 1, 1,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						SchedulePreferenceTO applJobPrefInfoTO = null;
						while (results.next()){
							applJobPrefInfoTO = new SchedulePreferenceTO();
							
							applJobPrefInfoTO.setDaySegCd(String.valueOf(results.getShort("daySegmentCode")));
							applJobPrefInfoTO.setWkDayNbr(String.valueOf(results.getShort("weekDayNumber")));
							/*String daySegDesc = "";
							String dayName = "";
							if (!results.wasNull("shortDaySegmentDescription")) {
								daySegDesc = StringUtils.trim(results.getString("shortDaySegmentDescription")); 
							}
							if (!results.wasNull("abbreviatedDayName")) {
								dayName = StringUtils.trim(results.getString("abbreviatedDayName"));
							}
							String jobPrefDesc = daySegDesc + "_" + dayName;*/

							//applJobPrefInfoTO.setJobPref(jobPrefDesc);
							
							applJobPrefInfoList.add(applJobPrefInfoTO);
							
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applJobPrefInfoList;
	}
	// job preference
	
	// languages
	// applicant languages
	public List<ApplLangInfoTO> getApplLangInfo(String ssnNbr, String applType, String emplApplicationDate) throws RetailStaffingException
	{
		final List<ApplLangInfoTO> applLangInfoList = new ArrayList<ApplLangInfoTO>();
		MapStream inputs = new MapStream("readJobSkillsByInputList");
		try {
			inputs.put("languageCode", "EN_US");
			inputs.put("employmentApplicantId", ssnNbr);

			Calendar cal = Calendar.getInstance();
			java.sql.Date jsqlD = new java.sql.Date( cal.getTime().getTime() );

			inputs.put("effectiveDate", jsqlD);
			inputs.put("jobSkillCategoryCode", (short) 3);
			inputs.put("displayInEmploymentApplicantTrackFlag", true);
			inputs.addQualifier("jobSkillCategoryCodeNotEquals", false); // optional
			
			// fix profile issue
			if (emplApplicationDate != null && emplApplicationDate.length() > 0) {
				java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
				inputs.put("employmentApplicationDate", sqlEmplApplDate);
			}
			
			BasicDAO.getResult("HrHrStaffing", 1, 1,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplLangInfoTO applLangInfoTO = null;

						while (results.next()){

							applLangInfoTO = new ApplLangInfoTO();
							if (!results.wasNull("shortJobSkillDescription")) {
								applLangInfoTO.setLanguage(StringUtils.trim(results.getString("shortJobSkillDescription")));
							}
							
							applLangInfoList.add(applLangInfoTO);				
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applLangInfoList;
	}
	// applicant languages
	
	// associate languages
	public List<ApplLangInfoTO> getAssoLangInfo(String ssnNbr, String applType) throws RetailStaffingException
	{
		final List<ApplLangInfoTO> applLangInfoList = new ArrayList<ApplLangInfoTO>();
		
		MapStream inputs = new MapStream("readAssociateLanguageCodeProficency");;
		
		try {
			inputs.put("employmentApplicantId", ssnNbr);
			inputs.put("supplementalType", new BigDecimal(400));
			inputs.put("recordStatus", "A");
			inputs.put("parameterValue", "");
			inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(ACTV_FLG, true);
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThan", true);			
			inputs.put("humanResourcesSystemCountryCode", "USA");
			
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplLangInfoTO applLangInfoTO = null;

						while (results.next()){

							applLangInfoTO = new ApplLangInfoTO();
							String lang1Code = "";
							String lang2Code = "";
							String lang3Code = "";
							String lang4Code = "";
							String languagesCode = "";
							String language = "";
							String languagesStr = "";
							String languageSkill = "";
							
							if (!results.wasNull("lang1")) {
								lang1Code = StringUtils.trim(results.getString("lang1"));
							}
							if (!results.wasNull("lang2")) {
								lang2Code = StringUtils.trim(results.getString("lang2"));
							}
							if (!results.wasNull("lang3")) {
								lang3Code = StringUtils.trim(results.getString("lang3"));
							}
							if (!results.wasNull("lang4")) {
								lang4Code = StringUtils.trim(results.getString("lang4"));
							}
							languagesCode = lang1Code + "_" + lang2Code + "_" + lang3Code + "_" + lang4Code;
							
							String[] langCodeArray = languagesCode.split("_");
							
							for (int i = 0; i < langCodeArray.length; i ++ ) {
								if (langCodeArray[i].equalsIgnoreCase("L01")) {
									language = "ENGLISH";
								} else if (langCodeArray[i].equalsIgnoreCase("L02")) {
									language = "SPANISH";
								} else if (langCodeArray[i].equalsIgnoreCase("L03")) {
									language = "FRENCH";
								} else if (langCodeArray[i].equalsIgnoreCase("L04")) {
									language = "GERMAN";
								} else if (langCodeArray[i].equalsIgnoreCase("L05")) {
									language = "ITALIAN";
								} else if (langCodeArray[i].equalsIgnoreCase("L06")) {
									language = "CHINESE";
								} else if (langCodeArray[i].equalsIgnoreCase("L07")) {
									language = "TAGALOG";
								} else if (langCodeArray[i].equalsIgnoreCase("L08")) {
									language = "POLISH";
								} else if (langCodeArray[i].equalsIgnoreCase("L09")) {
									language = "KOREAN";
								} else if (langCodeArray[i].equalsIgnoreCase("L10")) {
									language = "VIETNAMESE";
								} else if (langCodeArray[i].equalsIgnoreCase("L11")) {
									language = "PORTUGUESE";
								} else if (langCodeArray[i].equalsIgnoreCase("L12")) {
									language = "JAPANESE";
								} else if (langCodeArray[i].equalsIgnoreCase("L13")) {
									language = "GREEK";
								} else if (langCodeArray[i].equalsIgnoreCase("L14")) {
									language = "ARABIC";
								} else if (langCodeArray[i].equalsIgnoreCase("L15")) {
									language = "HINDI";
								} else if (langCodeArray[i].equalsIgnoreCase("L16")) {
									language = "SIGN";
								}
								
								languagesStr = languagesStr + language + ",";
								
							}
							
							languageSkill = "AS," + languagesStr;

							applLangInfoTO.setLanguage(languageSkill);
							applLangInfoList.add(applLangInfoTO);				
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applLangInfoList;
	}
	// associate languages
	// language
	
	// phone screen
	public List<ApplPhnScreenInfoTO> getApplPhnScreenInfo(String ssnNbr) throws RetailStaffingException
	{
		final List<ApplPhnScreenInfoTO> applPhnScreenInfoList = new ArrayList<ApplPhnScreenInfoTO>();
		MapStream inputs = new MapStream("readHumanResourcesPhoneScreenInterviewDetails");
		try {
			inputs.put("employmentPositionCandidateId", ssnNbr);
			inputs.put("tabno", TABNO_DEPT_NO);
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplPhnScreenInfoTO applPhnScreenInfoTO = null;
						applPhnScreenInfoTO = new ApplPhnScreenInfoTO();

						while (results.next()){
							
							applPhnScreenInfoTO = new ApplPhnScreenInfoTO();
							
							if (!results.wasNull("humanResourcesPhoneScreenId")) {
								applPhnScreenInfoTO.setRequisitionNum((Integer.toString(results.getInt("humanResourcesPhoneScreenId"))));
							}
							if (!results.wasNull("humanResourcesSystemStoreNumber")) {
								applPhnScreenInfoTO.setStoreNum(StringUtils.trim((results.getString("humanResourcesSystemStoreNumber"))));
							}
							if (!results.wasNull("humanResourcesSystemDepartmentNumber")) {
								applPhnScreenInfoTO.setDeptNum((StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber"))));
							}
							if (!results.wasNull("jobTitleCode")) {
								applPhnScreenInfoTO.setJob(StringUtils.trim(results.getString("jobTitleCode")));
							}
						
							if (!results.wasNull("phoneScreenStatusDescription")) {
								applPhnScreenInfoTO.setPhnScreenStatus(results.getString("phoneScreenStatusDescription"));
							}
							
							if (!results.wasNull("statusDescription")) {
								applPhnScreenInfoTO.setStatus(results.getString("statusDescription"));
							}											
							
							if (!results.wasNull("oe31")) {
								applPhnScreenInfoTO.setPhnScreenType(StringUtils.trim((results.getString("oe31"))));
							}
							if (!results.wasNull("phoneInterviewDate")) {
								applPhnScreenInfoTO.setLastUpdate(((results.getString("phoneInterviewDate")).toString()));
							}
								
							applPhnScreenInfoList.add(applPhnScreenInfoTO);
						}
						
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applPhnScreenInfoList;
	}
	// phone screen
	
	// applicant / candidate history
	public List<ApplHistoryInfoTO> getApplHistoryInfo(String ssnNbr) throws RetailStaffingException
	{
		
		final List<ApplHistoryInfoTO> applHistoryInfoList = new ArrayList<ApplHistoryInfoTO>();

		MapStream inputs = new MapStream("readRequisitionHistory");
		try {
			inputs.put("employmentPositionCandidateId", ssnNbr);
			inputs.put("effectiveBeginDate", new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put("effectiveEndDate", new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThanEqualTo", true);
			
			//inputs.put("tabno", TAB_NO);
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplHistoryInfoTO applHistoryInfoTO = null;

						while (results.next()){
							applHistoryInfoTO = new ApplHistoryInfoTO();
							
							if (!results.wasNull("employmentRequisitionNumber")) { 
								int reqNum = results.getInt("employmentRequisitionNumber");
								applHistoryInfoTO.setReqNumber(Integer.toString(reqNum));
							} 
							if (!results.wasNull("humanResourcesSystemStoreNumber")) {
								applHistoryInfoTO.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
							} 
							if (!results.wasNull("humanResourcesSystemDepartmentNumber")) {
								applHistoryInfoTO.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
							} 
							if (!results.wasNull("jobTitleDescription")) {
								applHistoryInfoTO.setTitle(StringUtils.trim(results.getString("jobTitleDescription")));
							} 
							
							if (!results.wasNull("interviewStatusIndicator")) {
								String interviewStatusIndicator = StringUtils.trim(results.getString("interviewStatusIndicator"));
								String interviewStatusIndicatorDesc = "";
								
								if(interviewStatusIndicator.length() > 0) {
									if (interviewStatusIndicator.equalsIgnoreCase("N")) {
										interviewStatusIndicatorDesc = "NO-SHOW FOR INTVW";
									} else if (interviewStatusIndicator.equalsIgnoreCase("D")) {
										interviewStatusIndicatorDesc = "DECLINED INTERVIEW";
									} else if (interviewStatusIndicator.equalsIgnoreCase("R")) {
										interviewStatusIndicatorDesc = "UNABLE TO REACH CAND";
									} else if (interviewStatusIndicator.equalsIgnoreCase("F")) {
										interviewStatusIndicatorDesc = "FAVORBLE";
									} else if (interviewStatusIndicator.equalsIgnoreCase("U")) {
										interviewStatusIndicatorDesc = "UNFAVORABLE";
									} else if (interviewStatusIndicator.equalsIgnoreCase("A")) {
										interviewStatusIndicatorDesc = "ACCEPTABLE";
									} else if (interviewStatusIndicator.equalsIgnoreCase("C")) {
										interviewStatusIndicatorDesc = "AVAILABILITY MISMATCH";
									}
								}
								
								applHistoryInfoTO.setIntvDisposition(interviewStatusIndicatorDesc);
							}
							
							if (!results.wasNull("candidateOfferMadeFlag")) {
								String candidateOfferMadeFlag = StringUtils.trim(results.getString("candidateOfferMadeFlag"));
								String candidateOfferMadeDesc = "";
								
								if (candidateOfferMadeFlag.length() > 0) {
									if (candidateOfferMadeFlag.equalsIgnoreCase("Y")) {
										candidateOfferMadeDesc = "YES";
									} else if (candidateOfferMadeFlag.equalsIgnoreCase("Q")) {
										candidateOfferMadeDesc = "OTHERS BETTER QUALIF";
									} else if (candidateOfferMadeFlag.equalsIgnoreCase("I")) {
										candidateOfferMadeDesc = "CAND NO LONGER INT";
									} else if (candidateOfferMadeFlag.equalsIgnoreCase("A")) {
										candidateOfferMadeDesc = "UNABLE TO VERIFY APP";
									} else if (candidateOfferMadeFlag.equalsIgnoreCase("J")) {
										candidateOfferMadeDesc = "JOB NO LONGER OPEN";
									} else if (candidateOfferMadeFlag.equalsIgnoreCase("S")) {
										candidateOfferMadeDesc = "CAND SCHEDULE AVAIL"; 
									}
								}
								
								applHistoryInfoTO.setOfferStatus(candidateOfferMadeDesc);
							}
							
							if (!results.wasNull("candidateOfferStatusIndicator")) {
								String candidateOfferStatusIndicator = StringUtils.trim(results.getString("candidateOfferStatusIndicator"));
								String candidateOfferStatusIndicatorDesc = "";
								
								if(candidateOfferStatusIndicator.length() > 0) {
									if (candidateOfferStatusIndicator.equalsIgnoreCase("AC")) {
										candidateOfferStatusIndicatorDesc = "Accepted";
									} else if (candidateOfferStatusIndicator.equalsIgnoreCase("DE")) {
										candidateOfferStatusIndicatorDesc = "Declined";
									}
								}
								
								applHistoryInfoTO.setOfferResults(candidateOfferStatusIndicatorDesc);
							}
							
							if (!results.wasNull("candidateOfferMadeDate")) {
								applHistoryInfoTO.setLastUpdated((results.getDate("candidateOfferMadeDate")).toString());
							}
							
							applHistoryInfoList.add(applHistoryInfoTO);
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applHistoryInfoList;
	}
	// applicant / candidate history
	
	// application history - 1
	public List<ApplAppHistoryInfoTO> getApplAppHistoryInfo(String ssnNbr, String applType, String applDate) throws RetailStaffingException
	{
		final List<ApplAppHistoryInfoTO> applAppHistoryInfoList = new ArrayList<ApplAppHistoryInfoTO>();
		
		MapStream inputs = new MapStream("readEmploymentApplicantStore");
		
		try {
			// for applicants
			
			inputs.put("employmentApplicantId", ssnNbr);
			
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
			java.util.Date appDate = formatter.parse(applDate);
			appDate = new java.sql.Date(appDate.getTime());
			inputs.put("employmentApplicationDate", appDate);

			//inputs.put("tabno", TAB_NO);
			BasicDAO.getResult("HrHrStaffing", 1, 1,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplAppHistoryInfoTO applAppHistoryInfoTO = null;

						while (results.next()){
							applAppHistoryInfoTO = new ApplAppHistoryInfoTO();
							
							if (!results.wasNull("humanResourcesSystemStoreNumber")) {
								applAppHistoryInfoTO.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
							}
							
							if (!results.wasNull("lastUpdateTimestamp")) {
								applAppHistoryInfoTO.setLastUpdatedS((results.getTimestamp("lastUpdateTimestamp")).toString());
							}
							
							applAppHistoryInfoList.add(applAppHistoryInfoTO);						
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applAppHistoryInfoList;
	}
	
	// associate
	public List<ApplAppHistoryInfoTO> getAssoAppHistoryInfo(String ssnNbr, String applType) throws RetailStaffingException
	{
		final List<ApplAppHistoryInfoTO> applAppHistoryInfoList = new ArrayList<ApplAppHistoryInfoTO>();
		
		MapStream inputs = new MapStream("readAssociatePreferenceStoreBySystemEmployeeId");
		
		try {
						
			inputs.put("humanResourcesSystemEmployeeId", ssnNbr);
			
			//inputs.put("tabno", TAB_NO);
			BasicDAO.getResult("HrHrStaffing", 1, 1,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplAppHistoryInfoTO applAppHistoryInfoTO = null;

						while (results.next()){
							applAppHistoryInfoTO = new ApplAppHistoryInfoTO();
							
							if (!results.wasNull("humanResourcesSystemStoreNumber")) {
								applAppHistoryInfoTO.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
							}
							
							if (!results.wasNull("createTimestamp")) {
								applAppHistoryInfoTO.setLastUpdatedS((results.getTimestamp("createTimestamp")).toString());
							}
									
							applAppHistoryInfoList.add(applAppHistoryInfoTO);						
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applAppHistoryInfoList;
	}
	
	
	// application history - 2
	// applicant
	public List<ApplAppHistoryInfoTO2> getApplAppHistoryInfo2(String ssnNbr, String applType, String applDate) throws RetailStaffingException
	{
		final List<ApplAppHistoryInfoTO2> applAppHistoryInfoList2 = new ArrayList<ApplAppHistoryInfoTO2>();
		MapStream inputs = new MapStream("readApplicantRequisitionPositionDetailsByMaximumEffectiveBeginDate");
		try {
			inputs.put("employmentApplicantId", ssnNbr);
			
			java.util.Date applicationDate = new java.util.Date();
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
				applicationDate = formatter.parse(applDate);
				applicationDate = new java.sql.Date(applicationDate.getTime());
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			
			inputs.put("employmentApplicationDate", applicationDate);
			inputs.put("effectiveBeginDate", new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put("effectiveEndDate", new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put("languageCode",  "EN_US");

			BasicDAO.getResult("HrHrStaffing", 1, 1,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplAppHistoryInfoTO2 applAppHistoryInfoTO2 = null;

						while (results.next()){
							applAppHistoryInfoTO2 = new ApplAppHistoryInfoTO2();
							
							if (!results.wasNull("jobTitleDescription")) {
								applAppHistoryInfoTO2.setPosition(StringUtils.trim(results.getString("jobTitleDescription")));
							}
							if (!results.wasNull("humanResourcesSystemDepartmentNumber")) {
								applAppHistoryInfoTO2.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
							}
							if (!results.wasNull("lastUpdateTimestamp")) {
								applAppHistoryInfoTO2.setLastUpdatedP((results.getTimestamp("lastUpdateTimestamp")).toString());
							}
	
							applAppHistoryInfoList2.add(applAppHistoryInfoTO2);		
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applAppHistoryInfoList2;
	} //
	
	// associate
	public List<ApplAppHistoryInfoTO2> getAssoAppHistoryInfo2(String ssnNbr, String applType) throws RetailStaffingException
	{
		final List<ApplAppHistoryInfoTO2> applAppHistoryInfoList2 = new ArrayList<ApplAppHistoryInfoTO2>();
		MapStream inputs = new MapStream("readStoreGroupJobTitleCandidateByEmploymentPositionCandidateId");
		try {
			inputs.put("employmentPositionCandidateId", ssnNbr);

			//inputs.put("tabno", TAB_NO);
			BasicDAO.getResult("HrHrStaffing", 1, 1,
				inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException
					{
						ApplAppHistoryInfoTO2 applAppHistoryInfoTO2 = null;

						while (results.next()){
							applAppHistoryInfoTO2 = new ApplAppHistoryInfoTO2();
							
							if (!results.wasNull("jobTitleCode")) {
								applAppHistoryInfoTO2.setPosition(StringUtils.trim(results.getString("jobTitleCode")));
							}
							if (!results.wasNull("humanResourcesSystemDepartmentNumber")) {
								applAppHistoryInfoTO2.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
							}
							if (!results.wasNull("lastUpdateTimestamp")) {
								applAppHistoryInfoTO2.setLastUpdatedP((results.getTimestamp("lastUpdateTimestamp")).toString());
							}

							applAppHistoryInfoList2.add(applAppHistoryInfoTO2);		
						}
					}
				});
		} catch (Exception e) {
			throw new RetailStaffingException(
					FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applAppHistoryInfoList2;
	} //
	
	public static List<AssociateReviewTO> getAssociateReviewRevised(String associateId) throws QueryException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("start getAssociateReview");
		}
		
		List<AssociateReviewTO> associateReviewTOList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				  .setSQL(SQL_GET_ASSOCIATE_REVIEW_SCORES)
				  .displayAs("Associates Review Scores")
					  .input(1, EVAL_CAT_CODE_1)
					  .input(2, en_US)
					  .input(3, associateId)
					  .input(4, ACTIVE)
					  .inClause(0, ASSOCIATE_COMPENSATION_ACTION_LIST)
					  .input(5, EVAL_RTG_CODE_27)
					  .input(6, EVAL_CAT_CODE_1)
					  .input(7, en_US)
					  .input(8, associateId)
					  .input(9, ACTIVE)
					  .inClause(1, ASSOCIATE_COMPENSATION_ACTION_LIST)
					  .input(10, EVAL_RTG_CODE_27)
				  .debug(logger)
				  .formatCycles(1)
				  .list(AssociateReviewTO.class);				
		
		return associateReviewTOList;
	}	
	
	public static String getAssociateIdByApplicantId(String applicantId) throws QueryException {

		if (logger.isDebugEnabled()) {
			logger.debug("start getAssociateIdByApplicantId");
		}
		
		String associateId = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_GET_ASSOCIATE_ID_FROM_APPLICANT_ID)
				.input(1, applicantId)
				.input(2, "Y")
				.get( String.class );
		
		return associateId;
		
	}	
}

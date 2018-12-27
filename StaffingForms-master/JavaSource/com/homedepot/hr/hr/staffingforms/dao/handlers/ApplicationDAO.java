package com.homedepot.hr.hr.staffingforms.dao.handlers;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.dto.ApplicationData;
import com.homedepot.hr.hr.staffingforms.dto.GenericSchedulePref;
import com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO.dao.ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO;
import com.homedepot.hr.hr.staffingforms.dto.interviewRosterDTO.dao.ReadHumanResourcesHireEventDTO;
import com.homedepot.hr.hr.staffingforms.util.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class ApplicationDAO {

	private static final Logger logger = Logger.getLogger(ApplicationDAO.class);
	private static final int BUSINESS_USE_ID = 55;
	private static final String HRHRSTAFFING_NAME = "HrHrStaffing";
	private static final int HRHRSTAFFING_VERSION = 1;
	public static final String TABNO_DEPT_NO = "10069";
	private static final SimpleDateFormat printDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	public static final String WORKFORCERECRUITMENT_CONTRACT_NAME = "WorkforceRecruitment";
	public static final int WORKFORCERECRUITMENT_BUID = 55;
	public static final int WORKFORCERECRUITMENT_VERSION = 1;
	
	// Get SSN based on Phone Screen number
	public static ApplicationData readHumanResourcesPhoneScreen(int phoneScrnId, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering readHumanResourcesPhoneScreen()");
		}

		MapStream inputs = new MapStream("readHumanResourcesPhoneScreen");
		inputs.put("humanResourcesPhoneScreenId", phoneScrnId);

		BasicDAO.getResult(HRHRSTAFFING_NAME, BUSINESS_USE_ID, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					aimsApplication.setApplicantId(results.getString("employmentPositionCandidateId").trim());
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting readHumanResourcesPhoneScreen()");
		}
		return aimsApplication;
	}

	// Get applicantId based on candidateId for External Applicant
	public static ApplicationData readExternalApplicantId(String candRefId, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering readExternalApplicantId()");
		}

		MapStream inputs = new MapStream("readExternalCandidateDetails");
		inputs.put("employmentCandidateId", candRefId);
		inputs.put("activeFlag", true);

		BasicDAO.getResult(HRHRSTAFFING_NAME, BUSINESS_USE_ID, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					aimsApplication.setApplicantId(results.getString("employmentApplicantId").trim());
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting readExternalApplicantId()");
		}
		return aimsApplication;
	}
	
	// Get applicantId based on candidateId for Internal Applicant
	public static ApplicationData readInternalApplicantId(String candRefId, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering readInternalApplicantId()");
		}

		MapStream inputs = new MapStream("readInternalCandidateDetails");
		inputs.put("employmentCandidateId", candRefId);
		inputs.put("statusCode", "A");
		inputs.put("effectiveBeginDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("effectiveEndDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("activeFlag", true);
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);
		inputs.put("humanResourcesSystemCountryCode", "USA");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					aimsApplication.setApplicantId(results.getString("employmentApplicantId").trim());
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting readInternalApplicantId()");
		}
		return aimsApplication;
	}
	
	public static ApplicationData getApplPersonalInfo(String ssnNumber, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getApplPersonalInfo()");
		}

		MapStream inputs = new MapStream("readEmploymentApplicantDetailsByActiveFlagApplicantId");

		inputs.put("employmentApplicantId", ssnNumber);
		inputs.put("activeFlag", true);
		inputs.put("tabno", TABNO_DEPT_NO);
		BasicDAO.getResult(HRHRSTAFFING_NAME, BUSINESS_USE_ID, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					// Name
					String lastName = "";
					String firstName = "";
					String mi = "";
					String suffixName = "";
					lastName = trim(results.getString("lastName")) + ",";
					firstName = trim(results.getString("firstName"));
					mi = trim(results.getString("middleInitialName"));
					suffixName = trim(results.getString("suffixName"));
					String fullName = lastName + " " + firstName + " " + mi + " " + suffixName;
					aimsApplication.setName(fullName);
					
					//Added for CDP, remove leading zeros for display purposes.
					aimsApplication.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
					
					// Phone Number
					aimsApplication.setAreaCode(trim(results.getString("phoneAreaCityCode")));
					aimsApplication.setPhnNumber(trim(results.getString("phoneLocalNumber")));

					// Address
					aimsApplication.setAddress1(trim(results.getString("addressLineOneText")));
					aimsApplication.setCity(trim(results.getString("cityName")));
					aimsApplication.setStateCd(trim(results.getString("stateCode")));
					aimsApplication.setZipCd(trim(results.getString("longZipCodeCode")));

					// US eligibility
					if (results.getBoolean("legalEmploymentEligibilityFlag")) {
						aimsApplication.setEmpltEligUsFlg("Y");
					} else {
						aimsApplication.setEmpltEligUsFlg("N");
					}

					// Over 18
					if (results.getBoolean("minimumEmploymentAgeFlag")) {
						aimsApplication.setMinEmpltAgeFlg("Y");
					} else {
						aimsApplication.setMinEmpltAgeFlg("N");
					}

					// Prior Conviction
					if (!results.wasNull("felonyMisdemeanorDate")) {
						if (trim((results.getDate("felonyMisdemeanorDate")).toString()).length() > 0) {
							aimsApplication.setPriorConviction("Y");
						} else {
							aimsApplication.setPriorConviction("N");
						}
					} else {
						aimsApplication.setPriorConviction("N");
					}

					// On Leave or Laid Off
					if (results.getBoolean("currentLeaveOfAbsenceLayoffFlag")) {
						aimsApplication.setCurrentLeaveOfAbsenceLayoffFlag("Y");
					} else {
						aimsApplication.setCurrentLeaveOfAbsenceLayoffFlag("N");
					}

					// Previous Employee
					aimsApplication.setPrevEmpltFlg("N");

					// Previous Location
					if (trim(results.getString("previousPositionLocationDescription")).length() > 0) {
						aimsApplication.setPrevEmpltLocation(trim(results.getString("previousPositionLocationDescription")));
						aimsApplication.setPrevEmpltFlg("Y");
					}

					// Previous Date From
					if (!results.wasNull("previousThdPositionBeginDate")) {
						aimsApplication.setPrevEmpltBgnDt(printDateFormat.format(results.getDate("previousThdPositionBeginDate")));
						aimsApplication.setPrevEmpltFlg("Y");
					}

					// Previous Date To
					if (!results.wasNull("previousThdPositionEndDate")) {
						aimsApplication.setPrevEmpltEndDt(printDateFormat.format(results.getDate("previousThdPositionEndDate")));
						aimsApplication.setPrevEmpltFlg("Y");
					}

					// Relative at HD
					aimsApplication.setRelativesAtHd("N");

					// Relative Name
					if (trim(results.getString("relativeEmployeeName")).length() > 0) {
						aimsApplication.setRelativesAtHdName(trim(results.getString("relativeEmployeeName")));
						aimsApplication.setRelativesAtHd("Y");
					}

					// Military Experience and Branch
					aimsApplication.setMilitaryFlg("N");
					if (trim(results.getString("militaryBranchName")).length() > 0) {
						aimsApplication.setMilitaryBranch(trim(results.getString("militaryBranchName")));
						aimsApplication.setMilitaryFlg("Y");
					}

					if (!results.wasNull("militaryServiceBeginDate")) {
						aimsApplication.setMilitaryBgnDt(printDateFormat.format(results.getDate("militaryServiceBeginDate")));
						aimsApplication.setMilitaryFlg("Y");
					}
					if (!results.wasNull("militaryServiceEndDate")) {
						aimsApplication.setMilitaryEndDt(printDateFormat.format(results.getDate("militaryServiceEndDate")));
						aimsApplication.setMilitaryFlg("Y");
					}

					// Full Time / Part Time
					if (results.getBoolean("fullTimeOkayFlag")) {
						aimsApplication.setFtFlg("Y");
					} else {
						aimsApplication.setFtFlg("N");
					}

					if (results.getBoolean("partTimeOkayFlag")) {
						aimsApplication.setPtFlg("Y");
					} else {
						aimsApplication.setPtFlg("N");
					}

					// Work Duration Indicator
					aimsApplication.setWrkDurationInd(trim(results.getString("planWorkDurationIndicator")));

					// Wage Desired
					aimsApplication.setWageDesired(trim((results.getBigDecimal("requestBasePayRate")).toString()));

					// Available Date
					if (!results.wasNull("availabilityDate")) {
						aimsApplication.setAvailableDt(printDateFormat.format(results.getDate("availabilityDate")));
					}

					// Application Date
					if (!results.wasNull("employmentApplicationDate")) {
						aimsApplication.setApplicationDate(String.valueOf(results.getDate("employmentApplicationDate")));
					}

					// Relocation Flag
					if (results.getBoolean("relocationOkayFlag")) {
						aimsApplication.setReloFlg("Y");
					} else {
						aimsApplication.setReloFlg("N");
					}

					// Dismissed from a job Flag
					if (results.getBoolean("employmentDismissFlag")) {
						aimsApplication.setDismissFlg("Y");
					} else {
						aimsApplication.setDismissFlg("N");
					}

					// Currently Employed Flag
					if (results.getBoolean("currentEmploymentFlag")) {
						aimsApplication.setCurrentlyEmployedFlg("Y");
					} else {
						aimsApplication.setCurrentlyEmployedFlg("N");
					}

					// Work Days missed in last 12 months
					aimsApplication.setWrkDaysMissed(String.valueOf(results.getInt("yearMissWorkDays")));
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getApplPersonalInfo()");
		}
		return aimsApplication;
	}

	// Availability
	public static ApplicationData getApplJobPrefInfo(String ssnNbr, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getApplJobPrefInfo()");
		}

		MapStream inputs = new MapStream("readApplicantDailyAvailabilityByEmploymentApplicantId");

		inputs.put("dayLanguageCode", "EN_US");
		inputs.put("weekLanguageCode", "en_US");
		inputs.put("employmentApplicantId", ssnNbr);

		BasicDAO.getResult(HRHRSTAFFING_NAME, 1, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					switch (results.getInt("weekDayNumber")) {
					case 1: // Monday
					case 2: // Tuesday
					case 3: // Wednesday
					case 4: // Thursday
					case 5: // Friday
						aimsApplication.setWeekdays("Y");
						switch (results.getInt("daySegmentCode")) {
						case 0: //All availability was selected
							aimsApplication.setEarlyAm("Y");
							aimsApplication.setMornings("Y");
							aimsApplication.setAfternoons("Y");
							aimsApplication.setNights("Y");
							aimsApplication.setLateNight("Y");
							aimsApplication.setOvernight("Y");
							break;
						case 1: // Early AM
							aimsApplication.setEarlyAm("Y");
							break;
						case 2: // Mornings
							aimsApplication.setMornings("Y");
							break;
						case 3: // Afternoons
							aimsApplication.setAfternoons("Y");
							break;
						case 4: // Nights
							aimsApplication.setNights("Y");
							break;
						case 5: // Late Night
							aimsApplication.setLateNight("Y");
							break;
						case 6: // Overnight
							aimsApplication.setOvernight("Y");
							break;
						}
						break;
					case 6: // Saturday
						aimsApplication.setSaturday("Y");
						switch (results.getInt("daySegmentCode")) {
						case 0: //All availability was selected
							aimsApplication.setEarlyAm("Y");
							aimsApplication.setMornings("Y");
							aimsApplication.setAfternoons("Y");
							aimsApplication.setNights("Y");
							aimsApplication.setLateNight("Y");
							aimsApplication.setOvernight("Y");
							break;
						case 1: // Early AM
							aimsApplication.setEarlyAm("Y");
							break;
						case 2: // Mornings
							aimsApplication.setMornings("Y");
							break;
						case 3: // Afternoons
							aimsApplication.setAfternoons("Y");
							break;
						case 4: // Nights
							aimsApplication.setNights("Y");
							break;
						case 5: // Late Night
							aimsApplication.setLateNight("Y");
							break;
						case 6: // Overnight
							aimsApplication.setOvernight("Y");
							break;
						}
						break;
					case 7: // Sunday
						aimsApplication.setSunday("Y");
						switch (results.getInt("daySegmentCode")) {
						case 0: //All availability was selected
							aimsApplication.setEarlyAm("Y");
							aimsApplication.setMornings("Y");
							aimsApplication.setAfternoons("Y");
							aimsApplication.setNights("Y");
							aimsApplication.setLateNight("Y");
							aimsApplication.setOvernight("Y");
							break;
						case 1: // Early AM
							aimsApplication.setEarlyAm("Y");
							break;
						case 2: // Mornings
							aimsApplication.setMornings("Y");
							break;
						case 3: // Afternoons
							aimsApplication.setAfternoons("Y");
							break;
						case 4: // Nights
							aimsApplication.setNights("Y");
							break;
						case 5: // Late Night
							aimsApplication.setLateNight("Y");
							break;
						case 6: // Overnight
							aimsApplication.setOvernight("Y");
							break;
						}
						break;
					}
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getApplJobPrefInfo()");
		}
		return aimsApplication;
	}


	// Applicant Languages
	public static ApplicationData getApplLangInfo(String ssnNbr, String emplApplicationDate, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getApplLangInfo()");
		}

		MapStream inputs = new MapStream("readJobSkillsByInputList");

		inputs.put("languageCode", "EN_US");
		inputs.put("employmentApplicantId", ssnNbr);
		Calendar cal = Calendar.getInstance();
		java.sql.Date jsqlD = new java.sql.Date(cal.getTime().getTime());
		inputs.put("effectiveDate", jsqlD);
		inputs.put("jobSkillCategoryCode", (short) 3);
		inputs.put("displayInEmploymentApplicantTrackFlag", true);
		inputs.addQualifier("jobSkillCategoryCodeNotEquals", false);

		if (emplApplicationDate != null && emplApplicationDate.length() > 0) {
			java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
			inputs.put("employmentApplicationDate", sqlEmplApplDate);
		}

		BasicDAO.getResult(HRHRSTAFFING_NAME, 1, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					switch (results.getInt("jobSkillCode")) {
					case 1:
						aimsApplication.setLangFlg1("Y");
						break;
					case 2:
						aimsApplication.setLangFlg2("Y");
						break;
					case 3:
						aimsApplication.setLangFlg3("Y");
						break;
					case 4:
						aimsApplication.setLangFlg4("Y");
						break;
					case 5:
						aimsApplication.setLangFlg5("Y");
						break;
					case 6:
						aimsApplication.setLangFlg6("Y");
						break;
					case 7:
						aimsApplication.setLangFlg7("Y");
						break;
					case 8:
						aimsApplication.setLangFlg8("Y");
						break;
					case 9:
						aimsApplication.setLangFlg9("Y");
						break;
					case 10:
						aimsApplication.setLangFlg10("Y");
						break;
					case 11:
						aimsApplication.setLangFlg11("Y");
						break;
					case 12:
						aimsApplication.setLangFlg12("Y");
						break;
					case 13:
						aimsApplication.setLangFlg13("Y");
						break;
					case 14:
						aimsApplication.setLangFlg14("Y");
						break;
					case 15:
						aimsApplication.setLangFlg15("Y");
						break;
					case 16:
						aimsApplication.setLangFlg16("Y");
						break;
					}
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getApplLangInfo()");
		}

		return aimsApplication;
	}

	// Applicant Education
	public static ApplicationData getApplEducationInfo(String ssnNbr, String emplApplicationDate, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getApplEducationInfo()");
		}

		MapStream inputs = new MapStream("readApplicantEducation");

		inputs.put("employmentApplicantId", ssnNbr);
		inputs.put("tabno", TABNO_DEPT_NO);

		if (emplApplicationDate != null && emplApplicationDate.length() > 0) {
			java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
			inputs.put("employmentApplicationDate", sqlEmplApplDate);
		}

		BasicDAO.getResult(HRHRSTAFFING_NAME, 10038, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					switch (results.getInt("sequenceNumber")) {
					case 1:
						aimsApplication.setSchoolName1(trim(results.getString("schoolName")));
						if (!results.wasNull("completeYearCount")) {
							aimsApplication.setYearsComplete1(results.getString("completeYearCount"));
						} else {
							aimsApplication.setYearsComplete1("0");
						}
						if (!results.wasNull("lastAttendantYear")) {
							aimsApplication.setLastYrAttended1(results.getString("lastAttendantYear"));
						} else {
							aimsApplication.setLastYrAttended1("0");
						}
						aimsApplication.setDegreeCert1(trim(results.getString("degreeCertificateDescription")));
						break;
					case 2:
						aimsApplication.setSchoolName2(trim(results.getString("schoolName")));
						if (!results.wasNull("completeYearCount")) {
							aimsApplication.setYearsComplete2(results.getString("completeYearCount"));
						} else {
							aimsApplication.setYearsComplete2("0");
						}
						if (!results.wasNull("lastAttendantYear")) {
							aimsApplication.setLastYrAttended2(results.getString("lastAttendantYear"));
						} else {
							aimsApplication.setLastYrAttended2("0");
						}
						aimsApplication.setDegreeCert2(trim(results.getString("degreeCertificateDescription")));
						break;
					case 3:
						aimsApplication.setSchoolName3(trim(results.getString("schoolName")));
						if (!results.wasNull("completeYearCount")) {
							aimsApplication.setYearsComplete3(results.getString("completeYearCount"));
						} else {
							aimsApplication.setYearsComplete3("0");
						}
						if (!results.wasNull("lastAttendantYear")) {
							aimsApplication.setLastYrAttended3(results.getString("lastAttendantYear"));
						} else {
							aimsApplication.setLastYrAttended3("0");
						}
						aimsApplication.setDegreeCert3(trim(results.getString("degreeCertificateDescription")));
						break;
					}
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getApplEducationInfo()");
		}

		return aimsApplication;
	}

	// Work History
	public static ApplicationData getApplWorkHistroyInfo(String ssnNbr, String emplApplicationDate, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getApplWorkHistroyInfo()");
		}

		MapStream inputs = new MapStream("readApplicantPreviousEmployment");

		inputs.put("employmentApplicantId", ssnNbr);

		if (emplApplicationDate != null && emplApplicationDate.length() > 0) {
			java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
			inputs.put("employmentApplicationDate", sqlEmplApplDate);
		}

		inputs.put("employmentApplicantId", ssnNbr);

		BasicDAO.getResult(HRHRSTAFFING_NAME, 1, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					switch (results.getInt("sequenceNumber")) {
					case 1:
						aimsApplication.setEmplrName1(trim(results.getString("employerName")));
						aimsApplication.setEmplrAddr1(trim(results.getString("addressLineOneText")));
						aimsApplication.setEmplrCity1(trim(results.getString("cityName")));
						aimsApplication.setEmplrStCd1(trim(results.getString("stateCode")));
						aimsApplication.setEmplrZipCd1(trim(results.getString("longZipCodeCode")));
						aimsApplication.setEmplrPhn1(trim(results.getString("phoneAreaCityCode")) + trim(results.getString("phoneLocalNumber")));
						aimsApplication.setEmplrWrkType1(trim(results.getString("workTypeText")));
						aimsApplication.setEmplrSupv1(trim(results.getString("supervisorName")));
						aimsApplication.setEmplrSupvTtl1(trim(results.getString("supervisorTitleDescription")));
						aimsApplication.setEmplrReasonLeaving1(trim(results.getString("leaveReasonText")));
						if (!results.wasNull("workBeginDate")) {
							aimsApplication.setEmplrBgnDt1(printDateFormat.format(results.getDate("workBeginDate")));
						}
						if (!results.wasNull("workEndDate")) {
							aimsApplication.setEmplrEndDt1(printDateFormat.format(results.getDate("workEndDate")));
						}
						aimsApplication.setEmplrPayRate1(trim(results.getString("basePayRate")));
						break;
					case 2:
						aimsApplication.setEmplrName2(trim(results.getString("employerName")));
						aimsApplication.setEmplrAddr2(trim(results.getString("addressLineOneText")));
						aimsApplication.setEmplrCity2(trim(results.getString("cityName")));
						aimsApplication.setEmplrStCd2(trim(results.getString("stateCode")));
						aimsApplication.setEmplrZipCd2(trim(results.getString("longZipCodeCode")));
						aimsApplication.setEmplrPhn2(trim(results.getString("phoneAreaCityCode")) + trim(results.getString("phoneLocalNumber")));
						aimsApplication.setEmplrWrkType2(trim(results.getString("workTypeText")));
						aimsApplication.setEmplrSupv2(trim(results.getString("supervisorName")));
						aimsApplication.setEmplrSupvTtl2(trim(results.getString("supervisorTitleDescription")));
						aimsApplication.setEmplrReasonLeaving2(trim(results.getString("leaveReasonText")));
						if (!results.wasNull("workBeginDate")) {
							aimsApplication.setEmplrBgnDt2(printDateFormat.format(results.getDate("workBeginDate")));
						}
						if (!results.wasNull("workEndDate")) {
							aimsApplication.setEmplrEndDt2(printDateFormat.format(results.getDate("workEndDate")));
						}
						aimsApplication.setEmplrPayRate2(trim(results.getString("basePayRate")));
						break;
					case 3:
						aimsApplication.setEmplrName3(trim(results.getString("employerName")));
						aimsApplication.setEmplrAddr3(trim(results.getString("addressLineOneText")));
						aimsApplication.setEmplrCity3(trim(results.getString("cityName")));
						aimsApplication.setEmplrStCd3(trim(results.getString("stateCode")));
						aimsApplication.setEmplrZipCd3(trim(results.getString("longZipCodeCode")));
						aimsApplication.setEmplrPhn3(trim(results.getString("phoneAreaCityCode")) + trim(results.getString("phoneLocalNumber")));
						aimsApplication.setEmplrWrkType3(trim(results.getString("workTypeText")));
						aimsApplication.setEmplrSupv3(trim(results.getString("supervisorName")));
						aimsApplication.setEmplrSupvTtl3(trim(results.getString("supervisorTitleDescription")));
						aimsApplication.setEmplrReasonLeaving3(trim(results.getString("leaveReasonText")));
						if (!results.wasNull("workBeginDate")) {
							aimsApplication.setEmplrBgnDt3(printDateFormat.format(results.getDate("workBeginDate")));
						}
						if (!results.wasNull("workEndDate")) {
							aimsApplication.setEmplrEndDt3(printDateFormat.format(results.getDate("workEndDate")));
						}
						aimsApplication.setEmplrPayRate3(trim(results.getString("basePayRate")));
						break;
					}
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getApplWorkHistroyInfo()");
		}

		return aimsApplication;
	}

	// Positions Applied For (External)
	public static ApplicationData getApplPositionsAppliedFor(String ssnNbr, String emplApplicationDate, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getApplPositionsAppliedFor()");
		}

		MapStream inputs = new MapStream("readApplicantRequisitionPositionDetailsByMaximumEffectiveBeginDate");

		inputs.put("employmentApplicantId", ssnNbr);
		if (emplApplicationDate != null && emplApplicationDate.length() > 0) {
			java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
			inputs.put("employmentApplicationDate", sqlEmplApplDate);
			inputs.put("effectiveBeginDate",  new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put("effectiveEndDate",  new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put("languageCode", "EN_US"); // optional
		}
		final List<String> tempList = new ArrayList<String>();

		BasicDAO.getResult(HRHRSTAFFING_NAME, 1, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				String tempValue = "";
				while (results.next()) {
					tempValue = trim(results.getString("jobTitleDescription"));
					tempList.add(tempValue);
				}

				Collections.sort(tempList);
				String currentValue = "";
				String previousValue = "";
				StringBuilder positions = new StringBuilder();
				for (int i = 0; i < tempList.size(); i++) {
					currentValue = tempList.get(i);
					if (!currentValue.equalsIgnoreCase(previousValue)) {
						positions.append(currentValue).append(", ");
						aimsApplication.setPositionsAppliedFor(positions.toString());
					}
					previousValue = currentValue;
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getApplPositionsAppliedFor()");
		}

		return aimsApplication;
	}

	// Stores Applied For (External)
	public static ApplicationData getApplStoresAppliedFor(String ssnNbr, String emplApplicationDate, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getApplStoresAppliedFor()");
		}

		MapStream inputs = new MapStream("readEmploymentApplicantStore");

		inputs.put("employmentApplicantId", ssnNbr);

		if (emplApplicationDate != null && emplApplicationDate.length() > 0) {
			java.sql.Date sqlEmplApplDate = java.sql.Date.valueOf(emplApplicationDate);
			inputs.put("employmentApplicationDate", sqlEmplApplDate);
		}
		BasicDAO.getResult(HRHRSTAFFING_NAME, 1, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				StringBuilder stores = new StringBuilder();
				while (results.next()) {
					stores.append(trim(results.getString("humanResourcesSystemStoreNumber"))).append(", ");
				}
				aimsApplication.setStoresAppliedFor(stores.toString());
			}
		});
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getApplStoresAppliedFor()");
		}
		return aimsApplication;
	}

	// Associate Current Work Info
	public static ApplicationData getAssociateInfo(String ssnNbr, final ApplicationData aimsApplication) throws Exception, QueryException {

		if (logger.isDebugEnabled()) {
			logger.debug("Entering getAssociateInfo()");
		}
		MapStream inputs = new MapStream("readPersonProfilesByPersonId");

		inputs.put("employmentApplicantId", ssnNbr);
		inputs.put("effectiveBeginDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("effectiveEndDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("activeFlag", true);
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);
		inputs.put("humanResourcesSystemCountryCode", "USA");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, BUSINESS_USE_ID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					aimsApplication.setName(trim(results.getString("name")));
					aimsApplication.setCurrentStore(trim(results.getString("organization1")));
					aimsApplication.setCurrentDept(trim(results.getString("organization2")));
					aimsApplication.setCurrentTitle(trim(results.getString("jobTitleId")));
					aimsApplication.setCurrentStatus(trim(results.getString("employmentCat")));
					if (!results.wasNull("hireDateOrigin")) {
						aimsApplication.setHireDt(printDateFormat.format(results.getDate("hireDateOrigin")));
					}

					//Added for CDP, remove leading zeros for display purposes.
					aimsApplication.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));					
				}
			}
		});
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getAssociateInfo()");
		}
		return aimsApplication;
	}

	// Associate Review Results
	public static ApplicationData getAssociateReview(String ssnNbr, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getAssociateReview()");
		}
		MapStream inputs = new MapStream("readEmployeeCompensationByInputList");

		inputs.put("employmentApplicantId", ssnNbr);
		inputs.put("recordStatus", "A");
		inputs.put("effectiveBeginDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("effectiveEndDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("activeFlag", true);
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

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, BUSINESS_USE_ID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				int counter = 0;
				while (results.next()) {
					counter++;
					switch (counter) {
					case 1:
						if (!results.wasNull("userCompensationDate2")) {
							aimsApplication.setReviewDt1(printDateFormat.format(results.getDate("userCompensationDate2")));
						}
						switch (results.getInt("userCompensationField1")) {
						case 1:
						case 2:
							aimsApplication.setReviewScore1("I");
							break;
						case 3:
						case 4:
							aimsApplication.setReviewScore1("P");
							break;
						case 5:
							aimsApplication.setReviewScore1("O");
							break;
						}
						break;
					case 2:
						if (!results.wasNull("userCompensationDate2")) {
							aimsApplication.setReviewDt2(printDateFormat.format(results.getDate("userCompensationDate2")));
						}
						switch (results.getInt("userCompensationField1")) {
						case 1:
						case 2:
							aimsApplication.setReviewScore2("I");
							break;
						case 3:
						case 4:
							aimsApplication.setReviewScore2("P");
							break;
						case 5:
							aimsApplication.setReviewScore2("O");
							break;
						}
						break;
					}
				}
			}
		});
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getAssociateReview()");
		}
		return aimsApplication;
	}

	// Associate Job Preferences
	public static ApplicationData getAssoJobPref(String ssnNbr, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getAssoJobPref()");
		}
		MapStream inputs = new MapStream("readStoreGroupJobTitleCandidateByEmploymentPositionCandidateId");

		inputs.put("employmentPositionCandidateId", ssnNbr);

		BasicDAO.getResult(HRHRSTAFFING_NAME, 1, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				StringBuilder temp = new StringBuilder();
				while (results.next()) {
					temp.append(trim(results.getString("humanResourcesSystemDepartmentNumber"))).append(" ");
					temp.append(trim(results.getString("jobTitleCode"))).append(", ");
				}
				aimsApplication.setAssoJobPref(temp.toString());
			}
		});
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getAssoJobPref()");
		}
		return aimsApplication;
	} //

	// Associate Previous Positions
	public static ApplicationData getAssociatePrevPosition(String ssnNbr, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering getAssociatePrevPosistion()");
		}

		MapStream inputs = new MapStream("readEmployeePositionByInputList");

		inputs.put("employmentApplicantId", ssnNbr);
		inputs.put("effectiveBeginDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("effectiveEndDate", new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put("activeFlag", true);
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);
		inputs.put("humanResourcesSystemCountryCode", "USA");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, BUSINESS_USE_ID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				int counter = 0;
				while (results.next()) {
					counter++;
					switch (counter) {
					case 1:
						aimsApplication.setFrmrDept1(trim(results.getString("organization2")));
						aimsApplication.setFrmrJobCd1(trim(results.getString("jobTitleId")));
						if (!results.wasNull("relatedBeginDate")) {
							aimsApplication.setFrmrFromDt1(printDateFormat.format(results.getDate("relatedBeginDate")));
						}
						if (!results.wasNull("relatedEndDate")) {
							aimsApplication.setFrmrToDt1(printDateFormat.format(results.getDate("relatedEndDate")));
						}
						break;
					case 2:
						aimsApplication.setFrmrDept2(trim(results.getString("organization2")));
						aimsApplication.setFrmrJobCd2(trim(results.getString("jobTitleId")));
						if (!results.wasNull("relatedBeginDate")) {
							aimsApplication.setFrmrFromDt2(printDateFormat.format(results.getDate("relatedBeginDate")));
						}
						if (!results.wasNull("relatedEndDate")) {
							aimsApplication.setFrmrToDt2(printDateFormat.format(results.getDate("relatedEndDate")));
						}
						break;
					case 3:
						aimsApplication.setFrmrDept3(trim(results.getString("organization2")));
						aimsApplication.setFrmrJobCd3(trim(results.getString("jobTitleId")));
						if (!results.wasNull("relatedBeginDate")) {
							aimsApplication.setFrmrFromDt3(printDateFormat.format(results.getDate("relatedBeginDate")));
						}
						if (!results.wasNull("relatedEndDate")) {
							aimsApplication.setFrmrToDt3(printDateFormat.format(results.getDate("relatedEndDate")));
						}
						break;
					case 4:
						aimsApplication.setFrmrDept4(trim(results.getString("organization2")));
						aimsApplication.setFrmrJobCd4(trim(results.getString("jobTitleId")));
						if (!results.wasNull("relatedBeginDate")) {
							aimsApplication.setFrmrFromDt4(printDateFormat.format(results.getDate("relatedBeginDate")));
						}
						if (!results.wasNull("relatedEndDate")) {
							aimsApplication.setFrmrToDt4(printDateFormat.format(results.getDate("relatedEndDate")));
						}
						break;
					}
				}
			}
		});
		if (logger.isDebugEnabled()) {
			logger.debug("Exiting getAssociatePrevPosistion()");
		}
		return aimsApplication;
	}

	public static String trim(String toTrim) {
		if (toTrim != null) {
			toTrim = toTrim.trim();
		} // end if(toTrim != null)

		return toTrim;
	} // end function trim()

	//DAO Method for DailyInterviewRoster PDF Generation

	public static List<ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO> readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetails(
			int calendarId, Timestamp beginTimeStamp, Timestamp endTimeStamp)
			throws QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("start readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetails");
		}
		final List<ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO> readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsList = new ArrayList<ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO>();

		MapStream inputs = new MapStream(
				"readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetails");
		
		inputs.put("requisitionCalendarId", calendarId);
		inputs.put("startBeginTimestamp", beginTimeStamp);
		inputs.put("endBeginTimestamp", endTimeStamp);

		if (logger.isDebugEnabled()) {
			logger.debug("executing the query");
		}

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, BUSINESS_USE_ID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO = null;
						while (results.next()) {
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO = new ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO();
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setBeginTimestamp(results
											.getTimestamp("beginTimestamp"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setSequenceNumber(results
											.getShort("sequenceNumber"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setRequisitionScheduleStatusCode(results
									.getShort("requisitionScheduleStatusCode"));
							if (results
									.wasNull("requisitionScheduleStatusCode")) {
								readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
										.setRequisitionScheduleStatusCode(null);
							}
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setRequisitionScheduleReserveTimestamp(results
											.getTimestamp("requisitionScheduleReserveTimestamp"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setEmploymentPositionCandidateId(results
									.getString("employmentPositionCandidateId"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setHumanResourcesPhoneScreenId(results
									.getInt("humanResourcesPhoneScreenId"));
							if (results.wasNull("humanResourcesPhoneScreenId")) {
								readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
										.setHumanResourcesPhoneScreenId(null);
							}
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setCandidateName(results
											.getString("candidateName"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setCandidatePersonId(results
											.getString("candidatePersonId"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setCandidatePhoneNumber(results
											.getString("candidatePhoneNumber"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setInterviewDate(results
											.getDate("interviewDate"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setInterviewTimestamp(results
									.getTimestamp("interviewTimestamp"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setElectronicMailAddressText(results
									.getString("electronicMailAddressText"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setEmploymentRequisitionNumber(results
									.getInt("employmentRequisitionNumber"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setHumanResourcesSystemStoreNumber(results
									.getString("humanResourcesSystemStoreNumber"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setHumanResourcesSystemDepartmentNumber(results
											.getString("humanResourcesSystemDepartmentNumber"));
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO
									.setJobTitleCode(results
											.getString("jobTitleCode"));
							
							//Added for CDP, remove leading zeros for display purposes.
							if (!results.wasNull("employmentCandidateId")) {
								readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO.setCandRefNbr(results.getString("employmentCandidateId").replaceAll("^0+",""));
							}
							
							readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsList
									.add(readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO);							
						}
					}
				});

		if (logger.isDebugEnabled()) {
			logger.debug("finish readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetails");
			logger.debug("returning "
					+ readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsList
							.size() + " item(s)");
		}
		return readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsList;
	}
	
	public static ReadHumanResourcesHireEventDTO readHumanResourcesHireEvent(int hireEventId)
			throws QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("start readHumanResourcesHireEvent");
		}
		final List<ReadHumanResourcesHireEventDTO> readHumanResourcesHireEventList = new ArrayList<ReadHumanResourcesHireEventDTO>();
		final ReadHumanResourcesHireEventDTO readHumanResourcesHireEventDTO =  new ReadHumanResourcesHireEventDTO();

		MapStream inputs = new MapStream("readHumanResourcesHireEvent");
		
		inputs.put("hireEventId", hireEventId);

		if (logger.isDebugEnabled()) {
			logger.debug("executing the query");
		}

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, BUSINESS_USE_ID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						
						while (results.next()) {
							readHumanResourcesHireEventDTO.setLastUpdateSystemUserId(results
									.getString("lastUpdateSystemUserId"));
							readHumanResourcesHireEventDTO
									.setLastUpdateTimestamp(results
											.getDate("lastUpdateTimestamp"));
							readHumanResourcesHireEventDTO
									.setHireEventBeginDate(results
											.getDate("hireEventBeginDate"));
							readHumanResourcesHireEventDTO
									.setHireEventEndDate(results
											.getDate("hireEventEndDate"));
							readHumanResourcesHireEventDTO.setHireEventLocationDescription(results
									.getString("hireEventLocationDescription"));
							readHumanResourcesHireEventDTO
									.setHireEventPhoneNumber(results
											.getString("hireEventPhoneNumber"));
							readHumanResourcesHireEventDTO
									.setHireEventAddressText(results
											.getString("hireEventAddressText"));
							readHumanResourcesHireEventDTO
									.setHireEventCityName(results
											.getString("hireEventCityName"));
							readHumanResourcesHireEventDTO
									.setHireEventZipCodeCode(results
											.getString("hireEventZipCodeCode"));
							readHumanResourcesHireEventDTO
									.setHireEventStateCode(results
											.getString("hireEventStateCode"));
							readHumanResourcesHireEventDTO
									.setHireEventBeginTime(results
											.getTime("hireEventBeginTime"));
							readHumanResourcesHireEventDTO
									.setHireEventEndTime(results
											.getTime("hireEventEndTime"));
							readHumanResourcesHireEventDTO
									.setHireEventBreakText(results
											.getString("hireEventBreakText"));
							readHumanResourcesHireEventDTO
									.setLunchBeginTime(results
											.getTime("lunchBeginTime"));
							readHumanResourcesHireEventDTO
									.setInterviewDurValue(results
											.getShort("interviewDurValue"));
							if (results.wasNull("interviewDurValue")) {
								readHumanResourcesHireEventDTO
										.setInterviewDurValue(null);
							}
							readHumanResourcesHireEventDTO.setInterviewTimeSlotValue(results
									.getShort("interviewTimeSlotValue"));
							if (results.wasNull("interviewTimeSlotValue")) {
								readHumanResourcesHireEventDTO
										.setInterviewTimeSlotValue(null);
							}
							readHumanResourcesHireEventDTO
									.setLastInterviewTime(results
											.getTime("lastInterviewTime"));
							readHumanResourcesHireEventDTO
									.setCreateTimestamp(results
											.getTimestamp("createTimestamp"));
							readHumanResourcesHireEventDTO
									.setCreateSystemUserId(results
											.getString("createSystemUserId"));
							readHumanResourcesHireEventDTO.setHireEventTypeIndicator(results
									.getString("hireEventTypeIndicator"));
							readHumanResourcesHireEventDTO.setEmgrHumanResourcesAssociateId(results
									.getString("emgrHumanResourcesAssociateId"));

						}
					}
				});

		if (logger.isDebugEnabled()) {
			logger.debug("finish readHumanResourcesHireEvent");
			logger.debug("returning " + readHumanResourcesHireEventList.size()
					+ " item(s)");
		}
		return readHumanResourcesHireEventDTO;
	}
	
	// Get the requisition that the applicant is attached to
	public static ApplicationData readHumanResourcesStoreRequisitionCandidateByInputList(String applicantId, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering readHumanResourcesStoreRequisitionCandidateByInputList()");
		}

		MapStream inputs = new MapStream("readHumanResourcesStoreRequisitionCandidateByInputList");
		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("activeFlag", true);

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, BUSINESS_USE_ID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					aimsApplication.setRequisitionAttachedTo(results.getString("employmentRequisitionNumber"));
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting readHumanResourcesStoreRequisitionCandidateByInputList()");
		}
		return aimsApplication;
	}
	
	// Get the phone screen with the requisition and applicant id
	public static ApplicationData readRequisitionPhoneScreen(String applicantId, int requisitionNbr, final ApplicationData aimsApplication) throws Exception, QueryException {
		if (logger.isDebugEnabled()) {
			logger.debug("Entering readRequisitionPhoneScreen()");
		}

		MapStream inputs = new MapStream("readRequisitionPhoneScreen");
		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("employmentRequisitionNumber", requisitionNbr);
		List<Object> inputActiveFlagList1 = new ArrayList<Object>();
		inputActiveFlagList1.add("Y");		
		inputs.put("activeFlagList", inputActiveFlagList1);

		BasicDAO.getResult(HRHRSTAFFING_NAME, BUSINESS_USE_ID, HRHRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					aimsApplication.setPhoneScreenId(Long.parseLong(results.getString("humanResourcesPhoneScreenId")));
					aimsApplication.setPhoneScreenStatusCode(results.getShort("phoneScreenStatusCode"));
				}
			}
		});

		if (logger.isDebugEnabled()) {
			logger.debug("Exiting readRequisitionPhoneScreen()");
		}
		return aimsApplication;
	}
	
	/**
	 * The method will be used to get the Phone Screen Schedule Responses list from
	 * DB.
	 * @param phone screen number
	 * @return Requisition Schedule Preference Values 
	 * @throws QueryException
	 */
	public static GenericSchedulePref getPhoneScreenScheduleResponses(int phoneScreenId) throws QueryException {
		final List<GenericSchedulePref> schPrefList = new ArrayList<GenericSchedulePref>();

		MapStream inputs = new MapStream("readPhoneScreenMinimumRequirement");
		inputs.put("humanResourcesPhoneScreenId", phoneScreenId);
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, BUSINESS_USE_ID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				GenericSchedulePref phnScrnResponse = null;
				while (results.next()) {
					if (phnScrnResponse == null) {
						phnScrnResponse = new GenericSchedulePref();
					}
					
					switch (results.getInt("sequenceNumber")) {
					case 14: // Weekdays
						phnScrnResponse.setWeekdays(results.getString("minimumRequirementFlag"));
						break;
					case 15: // Saturday
						phnScrnResponse.setSaturday(results.getString("minimumRequirementFlag"));		
						break;
					case 16: // Sunday
						phnScrnResponse.setSunday(results.getString("minimumRequirementFlag"));		
						break;
					case 17: // Early AM
						phnScrnResponse.setEarlyAm(results.getString("minimumRequirementFlag"));
						break;
					case 18: // Mornings						
						phnScrnResponse.setMornings(results.getString("minimumRequirementFlag"));
						break;
					case 19: // Afternoons
						phnScrnResponse.setAfternoons(results.getString("minimumRequirementFlag"));
						break;
					case 20: // Nights
						phnScrnResponse.setNights(results.getString("minimumRequirementFlag"));
						break;
					case 21: // Late Night
						phnScrnResponse.setLateNight(results.getString("minimumRequirementFlag"));
						break;
					case 22: // Overnight
						phnScrnResponse.setOvernight(results.getString("minimumRequirementFlag"));							
						break;
					case 23: // Reasonable Accommodation Requested
						phnScrnResponse.setReasonableAccommodationRequested(results.getString("minimumRequirementFlag"));
						break;						
					}
				}
				schPrefList.add(phnScrnResponse);
			}
		});

		return schPrefList.get(0);
	}	
}

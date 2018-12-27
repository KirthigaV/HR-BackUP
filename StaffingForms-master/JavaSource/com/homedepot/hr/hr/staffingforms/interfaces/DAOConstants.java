package com.homedepot.hr.hr.staffingforms.interfaces;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: DAOConstants.java
 * Application: RetailStaffing
 */
import com.homedepot.ta.aa.dao.Contract;
import com.homedepot.ta.aa.dao.basic.BasicContract;

/**
 * This interface contains SHARED constants used by different DAO components of the application 
 */
public interface DAOConstants
{
	//==========
	// DAO Contract Constants
	//==========
	public static final String HRSTAFFING_CONTRACT_NAME = "HrHrStaffing";
	public static final int HRSTAFFING_BU_ID = 10038;
	public static final int HRSTAFFING_VERSION = 1;	
	public static final Contract HRSTAFFING_DAO_CONTRACT = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
	
	// DAO constants for Contract WorkforceRecruitment
	public static final String WORKFORCERECRUITMENT_CONTRACT_NAME = "WorkforceRecruitment";
	public static final int WORKFORCERECRUITMENT_BUID = 55;
	public static final int WORKFORCERECRUITMENT_VERSION = 1;	
	public static final Contract WORKFORCERECRUITMENT_DAO_CONTRACT = new BasicContract(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION);	
	
	//==========
	// DAO Selector Constants
	//==========
	public static final String READ_STORE_DETAILS = "readStoreDetails";	
	public static final String READ_CALENDAR_SUMMARY = "readHumanResourcesRequisitionSlotCountDetailsByInputList";
	
	public static final String READ_CALENDAR_DETAILS = "readHumanResourcesRequisitionSchedulePhoneScreenDetails";
	
	public static final String READ_CALENDARS = "readHumanResourcesRequisitionCalendarV2";
	public static final String READ_REQN_SCHED_SLOT_COUNT = "readHumanResourcesRequisitionScheduleSlotCountDetails";
	public static final String READ_ACTV_REQNS = "readHumanResourcesStoreAndThdStoreEmploymentRequisitionByInputList";
	
	public static final String INSERT_CALENDAR_SLOT = "createHumanResourcesRequisitionScheduleBatch";
	
	public static final String DELETE_CALENDAR_SLOT = "deleteHumanResourcesRequisitionScheduleBatch";
	public static final String UPDATE_HR_REQN_CAL = "updateHumanResourcesRequisitionCalendarByInputList";
	public static final String CREATE_HIRING_EVENT_HR_REQN_CAL = "createHumanResourcesRequisitionCalendar";
	public static final String UPDATE_HIRING_EVENT_HR_REQN_CAL = "updateHumanResourcesRequisitionCalendar";
	public static final String CREATE_HR_HIRE_EVENT = "createHumanResourcesHireEvent";
	public static final String UPDATE_HR_HIRE_EVENT = "updateHumanResourcesHireEvent";	
	public static final String READ_HIRING_EVENT_CALENDARS = "readHumanResourcesRequisitionCalendar";
	public static final String CREATE_HR_HIRE_EVNT_STR = "createHumanResourcesHireEventStore";
	public static final String DELETE_HR_HIRE_EVNT_STR = "deleteHumanResourcesHireEventStore";
	public static final String UPDATE_HR_HIRE_EVNT_STR = "updateHumanResourcesHireEventStore";
	public static final String READ_REQUISITION_HIRING_EVENTS = "readHumanResourcesRequisitionCalendarAndEventStoreDetails";
	public static final String READ_INTVW_DETAILS_FROM_PHN_SCRN = "readInterviewDetails"; 
	public static final String UPDATE_INTVW_DETAILS_IN_PHN_SCRN = "updateHumanResourcesPhoneScreenInterviewDate";
	
	//==========
	// DAO Input/Output constants
	//==========
	public static final String HR_SYS_STR_NBR = "humanResourcesSystemStoreNumber";
	public static final String HR_SYS_STR_NM = "humanResourcesSystemStoreName";
	public static final String ADDR_LINE1_TXT = "addressLineOneText";
	public static final String ADDR_LINE2_TXT = "addressLineTwoText";
	public static final String CITY_NM = "cityName";
	public static final String LONG_ZIP_CD = "longZipCodeCode";
	public static final String ST_CD = "stateCode";
	public static final String CNTRY_CD = "countryCode";
	public static final String PHN_NBR = "phoneNumber";
	public static final String HR_SYS_RGN_CD = "humanResourcesSystemRegionCode";
	public static final String HR_SYS_OGRP_NM = "humanResourcesSystemOperationsGroupName";
	public static final String HR_SYS_DIV_NM ="humanResourcesSystemDivisionName";

	public static final String REQN_CAL_ID = "requisitionCalendarId";	
	public static final String SLOT_DATE = "slotDate";
	public static final String REQN_SCH_STAT_CD = "requisitionScheduleStatusCode";
	public static final String SLOT_COUNT = "slotCount";
	public static final String MAX_SEQ = "maximumSequenceNumber";
	public static final String REQN_CAL_DESC = "requisitionCalendarDescription";
	public static final String HR_CAL_TYP_CD = "humanResourcesCalendarTypeCode";
	public static final String ACTV_FLG = "activeFlag";
	
	public static final String BGN_TS = "beginTimestamp";
	public static final String SEQ_NBR = "sequenceNumber";
	public static final String CRT_TS = "createTimestamp";
	public static final String CRT_SYSUSR_ID = "createSystemUserId";
	public static final String LAST_UPD_SYSUSR_ID = "lastUpdateSystemUserId";
	public static final String LAST_UPD_TS = "lastUpdateTimestamp";
	public static final String REQN_SCH_RSRV_TS = "requisitionScheduleReserveTimestamp";
	public static final String HR_PHN_SCRN_ID = "humanResourcesPhoneScreenId";
	
	public static final String START_BGN_TS = "startBeginTimestamp";
	public static final String END_BGN_TS = "endBeginTimestamp";	
	public static final String LOC_TYP_CODE_LIST = "locationTypeCodeList";
	public static final String HR_REQN_SCHED_LIST = "humanResourcesRequisitionScheduleList";
	public static final String CAND_NM = "candidateName";
	public static final String INTVW_STAT_CDS = "interviewStatusCodeList";
	public static final String INTVW_STAT_INDS = "interviewStatusIndicatorList";
	
	public static final String EMPLT_REQN_NBR = "employmentRequisitionNumber";
	public static final String CRT_USER_ID = "createUserId";
	public static final String HR_SYS_DEPT_NBR = "humanResourcesSystemDepartmentNumber";
	public static final String JOB_TTL_CD = "jobTitleCode";
	public static final String JOB_TTL_DESC = "jobTitleDescription";
	public static final String REQD_POSN_FILL_DT = "requiredPositionFillDate";
	public static final String AUTH_POSN_CNT = "authorizationPositionCount";
	public static final String OPEN_POSN_CNT = "openPositionCount";
	public static final String FTM_REQD_FLG = "fullTimeRequiredFlag";
	public static final String PTM_REQD_FLG = "partTimeRequiredFlag";
	public static final String PM_REQD_FLG = "pmRequiredFlag";
	public static final String WKND_REQD_FLG = "weekendRequiredFlag";
	public static final String LAST_UPD_USER_ID = "lastUpdateUserId";
	public static final String HIRE_MGR_NM = "hireManagerName";
	public static final String HIRE_MGR_TTL = "hireManagerTitle";
	public static final String HIRE_MGR_PHN_NBR = "hireManagerPhoneNumber";
	public static final String RQST_NBR = "requestNumber";
	public static final String TRGT_EXPR_LVL_CD = "targetExperienceLevelCode";
	public static final String TRGT_PAY_AMT = "targetPayAmount";
	public static final String HIRE_EVNT_FLG = "hireEventFlag";
	public static final String WEEK_BGN_DT = "weekBeginDate";
	public static final String HIRE_MGR_AVAIL_TXT = "hireManagerAvailabilityText";
	public static final String REQN_STAT_CD = "requisitionStatusCode";
	public static final String RSC_SCHED_FLG = "rscScheduleFlag";
	public static final String APLCNT_TMP_FLG = "applicantTemporaryFlag";
	public static final String INTVW_CAND_CNT = "interviewCandidateCount";
	public static final String INTVW_MINS = "interviewMinutes";
	public static final String INTVW_SCHED_CNT = "interviewScheduleCount";
	public static final String INTVW_CMPLT_CNT = "interviewCompleteCount";
	public static final String LANG_CD = "languageCode";
	
	public static final String HIRE_EVNT_DATE_BEGIN = "hireEventBeginDate";
	public static final String HIRE_EVNT_DATE_END = "hireEventEndDate";
	public static final String HIRE_EVNT_LOC_DESC = "hireEventLocationDescription";
	public static final String HIRE_EVNT_ADDRESS = "hireEventAddressText";
	public static final String HIRE_EVNT_CITY = "hireEventCityName";
	public static final String HIRE_EVNT_ZIP = "hireEventZipCodeCode";
	public static final String HIRE_EVNT_STATE = "hireEventStateCode";
	public static final String HIRE_EVNT_TYPE = "hireEventTypeIndicator";	
	public static final String HIRE_EVNT_MGR_ASSOCIATE_ID = "emgrHumanResourcesAssociateId";
	public static final String HIRE_EVNT_PHONE_NBR = "hireEventPhoneNumber";
	public static final String HIRE_EVNT_BGN_TIME = "hireEventBeginTime";
	public static final String HIRE_EVNT_END_TIME = "hireEventEndTime";
	public static final String HIRE_EVNT_BREAK_TXT = "hireEventBreakText";
	public static final String HIRE_EVNT_LUNCH_BGN_TIME = "lunchBeginTime";
	public static final String HIRE_EVNT_INTERVIEW_DUR = "interviewDurValue";
	public static final String HIRE_EVNT_INTERVIEW_TIME_SLOT = "interviewTimeSlotValue";
	public static final String HIRE_EVNT_LAST_INTERVIEW_TIME = "lastInterviewTime";
	public static final String HIRE_EVNT_ID = "hireEventId";
	public static final String RETURN_GENERATED_KEY = "returnGeneratedKey";
	public static final String HIRE_EVNT_SITE_FLG = "eventSiteFlag";
	public static final String HIRE_EVNT_REQN_STR = "phoneHumanResourcesSystemStoreNumber";
	public static final String ASSOCIATE_ID = "candidatePersonId";
	public static final String STORE_ACTIVE_FLG = "storeActiveFlag";
	
	public static final String INTVW_LOC_TYPE_CODE = "interviewLocationTypeCode";
	public static final String INTERVIEWER_NAME = "interviewerName";
	public static final String INTVW_DATE = "interviewDate";
	public static final String INTVW_TIMESTAMP = "interviewTimestamp";
	public static final String INTVW_LOC_DESC = "interviewLocationDescription";
	public static final String INTVW_PHN_NUM = "interviewPhoneNumber";
	public static final String INTVW_ADDRESS_TEXT = "interviewAddressText";
	public static final String INTVW_CITY_NAME = "interviewCityName";
	public static final String INTVW_STATE_CODE = "interviewStateCode";
	public static final String INTVW_ZIP_CODE = "interviewZipCodeCode";
	
	
	//====================
	//DAO 2.0 Data Sources
	//====================
	public static final String DATA_SOURCE_DB2Z_PR1_005 = "java:comp/env/jdbc/DB2Z.PR1.005";
	public static final String DATA_SOURCE_DB2Z_PR1_032 = "java:comp/env/jdbc/DB2Z.PR1.032";
	
	//DAO 2.0 SQL===============
	public static final String SQL_GET_STORE_DETAILS =   "SELECT A.HR_SYS_STR_NM "
			   + "      ,A.HR_SYS_STR_NBR "
			   + "      ,B.ADDR_LINE1_TXT "
			   + "      ,B.ADDR_LINE2_TXT "
			   + "      ,B.CITY_NM "
			   + "      ,B.LONG_ZIP_CD "
			   + "      ,B.ST_CD "
			   + "      ,B.CNTRY_CD "
			   + "      ,B.PHN_NBR "
			   + "      ,A.HR_SYS_RGN_CD "
			   + "      ,A.HR_SYS_OGRP_NM "
			   + "      ,A.HR_SYS_DIV_NM "
			   + "      ,B.LOC_TYP_CD "
			   + "FROM HR_SYS_STR_ORG A "
			   + "    ,HR_LOC B "
			   + "WHERE A.HR_SYS_STR_NBR = ? "
			   + "  AND A.HR_SYS_STR_NBR = B.LOC_NBR "
			   + "  AND B.LOC_TYP_CD IN ( {0} ) "
			   + "  AND ( A.HR_STR_EFF_BGN_DT <= ? "
			   + "        AND A.HR_STR_EFF_END_DT >= ? ) "
			   + "WITH UR ";

//==========================		
} // end interface Constants
package com.homedepot.hr.hr.retailstaffing.interfaces;
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

/**humanResourcesCalendarTypeCode
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
	
	// DAO constants for Contract WorkforceEmploymentQualifications
	public static final String WORKFORCEEMPLOYMENTQUALIFICATIONS_CONTRACT_NAME = "WorkforceEmploymentQualifications";
	public static final int WORKFORCEEMPLOYMENTQUALIFICATIONS_BUID = 55;
	public static final int WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION = 1;
	
	// DAO constants for Contract WorkforceRecruitment
	public static final String WORKFORCERECRUITMENT_CONTRACT_NAME = "WorkforceRecruitment";
	public static final int WORKFORCERECRUITMENT_BUID = 55;
	public static final int WORKFORCERECRUITMENT_VERSION = 1;	
	public static final Contract WORKFORCERECRUITMENT_DAO_CONTRACT = new BasicContract(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION);	
	
	// DAO Constants for Contract BusinessOrganizationThdOrganizationStructure
	public static final String BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_CONTRACT_NAME = "BusinessOrganizationThdOrganizationStructure";
	public static final int BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_BUID = 55;
	public static final int BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_VERSION = 1;	
	public static final Contract BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_DAO_CONTRACT = new BasicContract(BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_CONTRACT_NAME, BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_BUID, BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_VERSION);

	//====================
	//DAO 2.0 Data Sources
	//====================
	public static final String DATA_SOURCE_DB2Z_PR1_005 = "java:comp/env/jdbc/DB2Z.PR1.005";
	public static final String DATA_SOURCE_DB2Z_PR1_032 = "java:comp/env/jdbc/DB2Z.PR1.032";
	
	//==========
	// TESERRACT table number constants
	//==========
	public static final String TABNO_DEPT_NO = "10069";
		
	//==========
	// Selector Constants
	//==========
	public static final String READ_NLS_PHONE_SCREEN_STATUS = "readNlsPhoneScreenStatus";
	public static final String READ_NLS_INTERVIEW_STATUS = "readNlsInterviewRespondStatusList";
	public static final String READ_NLS_MATERIAL_STATUS = "readNlsInterviewMaterialStatus";
	public static final String READ_NLS_REQUISITION_STATUS = "readRequisitionStatus";
	public static final String READ_NLS_PHONE_SCREEN_DISPOSITION = "readNlsPhoneScreenDispositionByInputList";
	public static final String READ_INTERVIEW_DETAILS_FOR_PHONE_SCREEN = "readInterviewSchedulingDetailsForPhoneScreens";		
	public static final String READ_REQUISITION_CALENDAR = "readHumanResourcesRequisitionCalendarV2";
	public static final String READ_REQUISITION_CALENDAR_WITH_COUNTS = "readHumanResourcesRequisitionCalendarDetailsByInputList";
	public static final String READ_REQUISITION_CALENDAR_TYPE_EXIST = "readHumanResourcesCalendarTypeCodeForExistence";
	public static final String READ_STORE_DETAILS = "readStoreDetails";
	public static final String READ_SCHEDULED_BLOCKS_FOR_PHONE_SCREEN = "readHumanResourcesRequisitionScheduleByInputList";
	public static final String READ_APPLICANT_PHONE_NUMBER = "readApplicantAddressPhoneByInputList";
	
	public static final String UPD_HR_REQN_SCH_RESERVED_TIME = "updateHumanResourcesRequisitionScheduleReserveTimeByInputList";
	
	public static final String INS_HR_REQN_CAL = "createHumanResourcesRequisitionCalendarV2";
	
	public static final String READ_REQUISITION_HIRING_EVENTS = "readHumanResourcesRequisitionCalendarAndEventStoreDetails";	
	public static final String READ_REQUISITION_HIRING_EVENTS_WITH_COUNTS = "readHumanResourcesHireEventStoreAndScheduleDetails";	
	public static final String READ_PARTICIPATING_STORE_DATA = "readHumanResourcesStoreEmploymentRequisitionAndEventStoreDetails";
	public static final String READ_USER_DATA = "readPartyContactMechanismRoleAndSystemUserDetails";
	public static final String READ_USER_EMAIL_DATA = "readEnterpriseEmailAddressAndSystemUserDetails";
	public static final String READ_HR_HIRE_EVENT = "readHumanResourcesHireEvent";
	public static final String READ_REQUISITION_CALENDAR_HIRING_EVENT = "readHumanResourcesRequisitionCalendar";
	
	public static final String READ_EAPLCNT_SSN_XREF = "readEmploymentApplicantSocialSecurityNumberCrossReferenceByInputList";
	public static final String INSERT_EAPLCNT_SSN_XREF = "createEmploymentApplicantSocialSecurityNumberCrossReference";
	public static final String UPDATE_EAPLCNT_SSN_XREF = "updateEmploymentApplicantSocialSecurityNumberCrossReference";
	public static final String CHECK_REHIRE_STATUS = "readPersonProfilesAndAssociationHREmployeeDetails";
	public static final String READ_BGC_CNSNT_DTLS = "readBackgroundCheckSystemConsentDetails";
	public static final String READ_HR_SYS_STR_ORG = "readHumanResourcesSystemStoreOrganizationByInputList";
	public static final String SEL_READ_MAX_CAND_CONTACT_STATUS = "readMaximumCandidateContactStatusByInputList";
	public static final String SEL_CREATE_CAND_CONTACT_STATUS = "createCandidateContactStatus";
	
	//==========
	// Column Constants
	//==========
	public static final String LAST_UPD_SYSUSR_ID = "lastUpdateSystemUserId";
	public static final String LAST_UPD_TS = "lastUpdateTimestamp";
	public static final String D_STAT_CD = "displayStatusCode";
	public static final String S_STAT_DESC = "shortStatusDescription";
	public static final String STAT_DESC = "statusDescription";
	public static final String LANG_CD = "languageCode";
	public static final String PHN_SCRN_STAT_CD = "phoneScreenStatusCode";
	public static final String INTVW_RSPN_STAT_CD = "interviewRespondStatusCode";
	public static final String INTVW_MAT_STAT_CD = "interviewMaterialStatusCode";
	public static final String REQN_STAT_CD = "requisitionStatusCode";

	public static final String HR_PHN_SCRN_ID = "humanResourcesPhoneScreenId";
	public static final String HR_SYS_STR_NBR = "humanResourcesSystemStoreNumber";
	public static final String EMPLT_POSN_CAND_ID = "employmentPositionCandidateId";
	public static final String EMPLT_REQN_NBR = "employmentRequisitionNumber";
	public static final String EMPLT_APLCNT_ID = "employmentApplicantId";
	public static final String PHN_AREA_CITY_CD = "phoneAreaCityCode";
	public static final String PHN_LOCL_NBR = "phoneLocalNumber";

	public static final String INTVW_STAT_CD = "interviewStatusCode";

	public static final String CRT_TS = "createTimestamp";
	public static final String CRT_SYSUSR_ID = "createSystemUserId";
	public static final String REQN_CAL_ID = "requisitionCalendarId";	
	public static final String REQN_CAL_DESC = "requisitionCalendarDescription";
	public static final String DFLT_CAL_FLG = "defaultCalendarFlag";
	public static final String HR_CAL_TYP_CD = "humanResourcesCalendarTypeCode";
	public static final String AVAIL_INTVW_SLOT_CNT = "availabilityInterviewSlotCount";
	public static final String ACTV_REQN_CNT = "activeRequisitionCount";
	public static final String AVAIL_TIME_ADDED_TS = "availAdded";

	public static final String HIRE_EVNT_FLG = "hireEventFlag";

	public static final String RSC_SCH_FLG = "rscScheduleFlag"; 
	public static final String INTRVW_MINS = "interviewMinutes"; 
	public static final String REQ_ACTV_FLG = "reqActiveFlag";
	public static final String CAND_ACTV_FLG = "candidateActiveFlag";	
	
	public static final String INTVW_TS = "interviewTimestamp";
	public static final String ACTV_FLG = "activeFlag";
	public static final String ACTV_FLG_INPUT = "activeFlagInput";

	public static final String BGN_TS = "beginTimestamp";
	public static final String SEQ_NBR = "sequenceNumber";
	
	public static final String REQN_SCH_STAT_CD = "requisitionScheduleStatusCode";
	public static final String NEW_REQN_SCH_STAT_CD =  "setRequisitionScheduleStatusCode";
	public static final String NEW_REQN_SCH_RSRV_TS = "requisitionScheduleReserveTimestamp";
	public static final String REQN_SCH_STAT_CD_NOT_IN = "requisitionScheduleStatusCodeNotEqualsList";
	
	public static final String STORE_ACTIVE_FLG = "storeActiveFlag";
	public static final String HIRE_EVENT_ID = "hireEventId";
	public static final String HIRE_EVENT_SITE_FLG = "eventSiteFlag";
	public static final String CALENDAR_ACTV_FLG = "calendarActiveFlag";
	public static final String EVENT_ACTV_FLG = "eventActiveFlag";
	public static final String AVAIL_SLOT_STAT_CD = "requisitionScheduleStatusCode";
	public static final String SCHEDULED_SLOT_STAT_CD = "slotRequisitionScheduleStatusCode";
	public static final String AVAIL_SLOT_GREATER_TIMESTAMP = "beginTimestampGreaterThan";
	public static final String SCHD_SLOT_GREATER_TIMESTAMP = "slotBeginTimestampGreaterThan";	
	public static final String HIRE_EVENT_DATE = "hireEventBeginDate";
	public static final String NUMBER_PARTICIPATING_STORES = "numericParticipatingStrs";
	public static final String AVAILABLE_INTERVIEW_SLOTS = "availabilitySlots";
	public static final String SCHEDULED_INTERVIEW_SLOTS = "scheduledSlots";
	public static final String HR_SYS_STR_NAME = "humanResourcesSystemStoreName";
	public static final String CONTRACT_METHOD_CODE = "contactMethodCode";
	public static final String HR_ASSOCIATE_ID = "humanResourcesAssociateId";
	public static final String OUT_HR_ASSOCIATE_ID = "humanResourceAssociateId";
	public static final String USER_LDAP_ID = "userId";
	public static final String ASSOCIATE_NAME = "employeeName";
	public static final String JOB_TITLE_CD = "ttlcd";
	public static final String EMAIL_ADDRESS = "emailAddressText";
	public static final String CONTRACT_MECHANISM_ROLE_CODE = "contactMechanismRoleCode";
	public static final String CONTRACT_MECHANISM_PREF_NUMBER = "contactMechanismPreferenceNumber";
	public static final String HIRE_EVENT_BEGIN_DT = "hireEventBeginDate";
	public static final String HIRE_EVENT_END_DT = "hireEventEndDate";	
	public static final String HIRE_EVENT_LOCATION = "hireEventLocationDescription";
	public static final String HIRE_EVENT_ADDRESS = "hireEventAddressText";
	public static final String HIRE_EVENT_CITY = "hireEventCityName";
	public static final String HIRE_EVENT_ZIP = "hireEventZipCodeCode";
	public static final String HIRE_EVENT_ST = "hireEventStateCode";
	public static final String HIRE_EVENT_TYPE = "hireEventTypeIndicator";
	public static final String HIRE_EVENT_MGR_ASSOCIATE_ID = "emgrHumanResourcesAssociateId";
	public static final String EVENT_TYPE = "eventType";
	
	public static final String EFF_BEGIN_DATE = "effectiveBeginDate";
	public static final String EFF_END_DATE = "effectiveEndDate";
	public static final String CANDIDATE_SSN_NUMBER = "applicantSocialSecurityNumberNumber";
	public static final String PERSON_ID = "personId";
	public static final String TESSERACT_EMPLOYEE_Id = "tesseractEmployeeId";
	public static final String REHIRE_STATUS_RESULT = "rehireStatusOne";
	public static final String EFF_END_DATE_INPUT = "effectiveEndDateInput";
	public static final String BGC_CNSNT_TYPE_CD = "backgroundCheckSystemConsentTypeCode";
	public static final String BIRTH_DATE = "birthDate";
	public static final String CAND_INIT_SIG_VAL = "candidateInitialsSignatureValue";
	public static final String DOC_SIG_TS = "documentSignatureTimestamp";
	public static final String BGC_AUTH_FLG = "backgroundCheckAuthorizationFlag";
	public static final String MGR_USR_ID = "managerUserId";
	
	public static final String SEQUENCE_NBR = "sequenceNumber";
	public static final String INTERVIEW_STAT_CD = "interviewStatusCode";
	public static final String CAND_CONTACT_STATUS_TYPE_CODE = "ccntctStatusTypeCode";

	//DAO 2.0 SQL===============
	public static final String SQL_GET_REQUISITION_CANDIDATE_DATA =   "SELECT C1.EMPLT_POSN_CAND_ID "
																	+ " ,''    AS Z_EMPLID "
																	+ " ,TRIM(E1.LAST_NM) || ', ' || TRIM(E1.FRST_NM) || ', ' || TRIM(E1.MI_NM) || ', ' || TRIM(E1.SFX_NM) AS APPLICANT_FULL_NAME "
																	+ " ,C1.CRT_TS "
																	+ " ,C1.INTVW_STAT_IND "
																	+ " ,C1.DRUG_TEST_RSLT_CD "
																	+ " ,C1.CAND_OFFR_MADE_DT "
																	+ " ,C1.CAND_OFFR_MADE_FLG "
																	+ " ,C1.CAND_OFFR_STAT_IND "
																	+ " ,C1.ACTV_FLG "
																	+ " ,''    AS ORGANIZATION_1 "
																	+ " ,C1.OFFR_DCLN_RSN_CD "
																	+ " ,P.HR_PHN_SCRN_ID "
																	+ " ,P.INTVW_STAT_CD "
																	+ " ,'EXT' AS APPL_TYP "
																	+ " ,'EXTERNAL' AS INTERNAL_EXTERNAL "
																	+ " ,CX.EMPLT_CAND_ID "	
																	+ " ,CASE WHEN TRC.EFF_BGN_DT IS NULL THEN '0001-01-01' ELSE TRC.EFF_BGN_DT END EFF_BGN_DT "
																	+ " ,CASE WHEN TRC.EFF_END_DT IS NULL THEN '0001-01-01' ELSE TRC.EFF_END_DT END EFF_END_DT "
																	+ " ,C1.INTVW_RSLT_CRT_DT AS INTERVIEW_SUBMITTED_DATE "
																	+ " ,C1.INIT_OFFR_MADE_DT AS OFFER_SUBMITTED_DATE "
																	+ "FROM EMPLT_APLCNT_PRIM E1 " 
																	+ "    ,HR_STR_REQN_CAND C1 "
																	+ "      LEFT OUTER JOIN HR_PHN_SCRN P "
																	+ "                   ON C1.EMPLT_REQN_NBR = P.EMPLT_REQN_NBR "
																	+ "                      AND C1.EMPLT_POSN_CAND_ID = P.EMPLT_POSN_CAND_ID "
																	+ "                      AND C1.EMPLT_REQN_NBR = P.EMPLT_REQN_NBR "
																	+ "      LEFT OUTER JOIN EPCAND_TEMP_RJTD TRC "
																	+ "                   ON TRC.EMPLT_POSN_CAND_ID = C1.EMPLT_POSN_CAND_ID "
																	+ "                      AND TRC.EMPLT_REQN_NBR = C1.EMPLT_REQN_NBR "
																	+ "                      AND TRC.EFF_BGN_DT <= ? "
																	+ "                      AND TRC.EFF_END_DT >= ? "
																	+ "    ,EAPLCNT_CAND_XREF CX "
																	+ "WHERE C1.HR_SYS_STR_NBR = ? "
																	+ "  AND C1.EMPLT_REQN_NBR = ? " 
																	+ "  AND C1.ACTV_FLG = ? " 
																	+ "  AND E1.ACTV_FLG = ? " 
																	+ "  AND C1.EMPLT_POSN_CAND_ID = E1.EMPLT_APLCNT_ID "
																	+ "  AND E1.EMPLT_APLCNT_ID = CX.EMPLT_APLCNT_ID "
																	+ "UNION ALL "
																	+ "SELECT C1.EMPLT_POSN_CAND_ID "
																	+ "      ,X1.Z_EMPLID "
																	+ "      ,TRIM(B4.NAME_LAST) || ', ' || TRIM(B4.NAME_FIRST) || ', ' || TRIM(B4.NAME_MIDDLE) || ', ' || TRIM(B4.NAME_SUFFIX) AS APPLICANT_FULL_NAME "
																	+ "      ,C1.CRT_TS "
																	+ "      ,C1.INTVW_STAT_IND "
																	+ "      ,C1.DRUG_TEST_RSLT_CD "
																	+ "      ,C1.CAND_OFFR_MADE_DT "
																	+ "      ,C1.CAND_OFFR_MADE_FLG "
																	+ "      ,C1.CAND_OFFR_STAT_IND "
																	+ "      ,C1.ACTV_FLG "
																	+ "      ,A1.ORGANIZATION_1 "
																	+ "      ,C1.OFFR_DCLN_RSN_CD "
																	+ "      ,P.HR_PHN_SCRN_ID "
																	+ "      ,P.INTVW_STAT_CD "
																	+ "      ,'INT' AS APPL_TYP "
																	+ "      ,'INTERNAL' AS INTERNAL_EXTERNAL "
																	+ "      ,CX.EMPLT_CAND_ID " 
																	+ "      ,DATE('0001-01-01') AS EFF_BGN_DT "
																	+ "      ,DATE('0001-01-01') AS EFF_END_DT "
																	+ "      ,C1.INTVW_RSLT_CRT_DT AS INTERVIEW_SUBMITTED_DATE "
																	+ "      ,C1.INIT_OFFR_MADE_DT AS OFFER_SUBMITTED_DATE "
																	+ "FROM   HR_EMPL_TSRT_XREF X1 "
																	+ "      ,PERSONAL_DEMOS_T B4 "
																	+ "      ,PERSON_PROFILES_T A1 "
																	+ "      ,EAPLCNT_SSN_XREF SX "
																	+ "      ,EAPLCNT_CAND_XREF CX "
																	+ "      ,HR_STR_REQN_CAND C1 "
																	+ "         LEFT OUTER JOIN HR_PHN_SCRN P "
																	+ "           ON C1.EMPLT_REQN_NBR = P.EMPLT_REQN_NBR "
																	+ "            AND C1.EMPLT_POSN_CAND_ID = P.EMPLT_POSN_CAND_ID "
																	+ "            AND C1.EMPLT_REQN_NBR = P.EMPLT_REQN_NBR "
																	+ "WHERE C1.HR_SYS_STR_NBR = ? "
																	+ "  AND C1.EMPLT_REQN_NBR = ? "
																	+ "  AND C1.ACTV_FLG = ? " 
																	+ "  AND A1.RECORD_STATUS = 'A' " 
																	+ "  AND A1.STATUS_CD <> 'T' " 
																	+ "  AND C1.EMPLT_POSN_CAND_ID = SX.EMPLT_APLCNT_ID "
																	+ "  AND A1.PERSON_ID = X1.TSRT_EMPL_ID " 
																	+ "  AND B4.PERSON_ID = A1.PERSON_ID "
																	+ "  AND X1.EMPL_INTL_ID = SX.APLCNT_SSN_NBR "
																	+ "  AND SX.EMPLT_APLCNT_ID = CX.EMPLT_APLCNT_ID "
																	+ "  AND B4.RECORD_STATUS = 'A' "
																	+ "  AND B4.SEQUENCE_NO = (SELECT Max(B4D.SEQUENCE_NO) "
																	+ "                        FROM PERSONAL_DEMOS_T B4D "
																	+ "                        WHERE B4.PERSON_ID = B4D.PERSON_ID "
																	+ "                          AND B4D.RECORD_STATUS = 'A' "
																	+ "                          AND B4D.EFF_DT <= CURRENT_DATE) "
																	+ "       AND SX.ACTV_FLG = ? " 
																	+ "       AND SX.EFF_BGN_DT <= ? " 
																	+ "       AND SX.EFF_END_DT > ? " 
																	+ "       AND X1.HR_SYS_CNTRY_CD = ? " 
																	+ "ORDER BY 3, 4, 5, 6 ,1 " 
																	+ "WITH UR ";
 
	//DAO 2.0 SQL===============
	public static final String SQL_GET_DRUGTESTREQUEST_CANDIDATE_DATA =   " SELECT EMPLT_APLCNT_ID "
																	    + " ,DTEST_ORD_NBR "
																	    + " ,DTEST_RSN_CD "
																	    + " ,DTEST_TYP "
																	    + " ,EMPLT_REQN_NBR "
																	    + " ,RQSTR_EADDR "
																	    + " ,APLCNT_EADDR "
																	    + " ,CRT_TS "
																	    + " ,CRT_USER_ID "
																	    + " ,RQST_CMPLT_TS "
																		+ " FROM APLCNT_DTEST_ORD DT " 
																		+ " WHERE "
																		+ " DT.EMPLT_APLCNT_ID = ? "
																		+ " AND DT.EMPLT_REQN_NBR = ? "
																		+ " AND DT.CRT_TS = (SELECT MAX(DT2.CRT_TS) "
																	    + " 				 FROM APLCNT_DTEST_ORD DT2 "        
																	    + " 				 WHERE DT2.EMPLT_APLCNT_ID = DT.EMPLT_APLCNT_ID "
																	    + " 				   AND DT2.EMPLT_REQN_NBR = DT.EMPLT_REQN_NBR "
																	    + "					   AND ((DATE(DT2.CRT_TS) >= CURRENT_DATE - 10 Days "
																	    + "						AND DT2.RQST_CMPLT_TS IS NULL) "
																	    + "						OR DT2.RQST_CMPLT_TS IS NOT NULL)) "
																	    + " WITH UR ";

	public static final String SQL_UPD_DRUGTESTREQUEST_CANDIDATE_DATA =   "UPDATE APLCNT_DTEST_ORD "
																		+ " SET RQST_CMPLT_TS = CURRENT TIMESTAMP "
																		+ " WHERE "
																		+ " EMPLT_APLCNT_ID = ? "
																		+ " AND EMPLT_REQN_NBR = ? "
																		+ " AND DTEST_ORD_NBR = ? ";
 
	public static final String SQL_GET_LOCATION_DRUGTESTTYPE = 			  "SELECT OE10 "
																		+ " FROM TRPRX000 "
																		+ " WHERE TABNO = '00049' "
																		+ " AND TBLKEYS = ? "
																		+ " AND STDATE <= CURRENT DATE "
																		+ " AND ENDATE >= CURRENT DATE "
																		+ " WITH UR ";
	
	public static final String SQL_GET_MINOR_FORMS_COMPLETION = 		  "SELECT COUNT(A.HR_ONBRD_DOC_STAT_CD) AS DOCSREMAINING "
																		+ " ,(SELECT COUNT(A.EMPLT_APLCNT_ID) "
																		+ "   FROM ASSG_HR_ONBRD_DOC A, "
																		+ "   HR_ONBRD_DOC_WRKFLW_LIST W "
																		+ "   WHERE A.HR_ONBRD_DOC_ID = W.HR_ONBRD_DOC_ID "
																		+ "   AND A.EMPLT_APLCNT_ID = ? "
																		+ "   AND A.EMPLT_REQN_NBR = ? "
																		+ "   AND W.HR_ONBRD_DOC_WRKFLW_CTGRY_CD = 1) AS ROWCOUNT "
																		+ " FROM ASSG_HR_ONBRD_DOC A, "
																		+ " HR_ONBRD_DOC_WRKFLW_LIST W "
																		+ " WHERE A.HR_ONBRD_DOC_ID = W.HR_ONBRD_DOC_ID "
																		+ " AND A.EMPLT_APLCNT_ID = ? "
																		+ " AND A.EMPLT_REQN_NBR = ? "
																		+ " AND W.HR_ONBRD_DOC_WRKFLW_CTGRY_CD = 1 "
																		+ " AND (A.HR_ONBRD_DOC_STAT_CD != 3) "
																		+ " WITH UR ";
	
	public static final String SQL_GET_CANDIDATE_AGE =   "SELECT ((DAYS(CURRENT DATE) - DAYS(DATE(BG.BRTH_DT)))/365.25) AS AGE "
													   + " FROM BGC_CNSNT BG "
													   + " WHERE EMPLT_POSN_CAND_ID = ? "
													   + " AND CNSNT_SIG_DT = (SELECT MAX(BG2.CNSNT_SIG_DT) "
													   + "					   FROM BGC_CNSNT BG2 "
													   + " 					   WHERE BG2.EMPLT_POSN_CAND_ID = BG.EMPLT_POSN_CAND_ID) "
													   + " WITH UR ";
	
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
	
	public static final String SQL_READ_TRPRX_DETAILS = "SELECT OE01 "
													  + " ,OE02 "
													  + " ,OE03 "
													  + " ,OE04 "
													  + " ,OE05 "
													  + " ,OE06 "
													  + " ,OE07 "
													  + " ,OE08 "
													  + " ,OE09 "
													  + " ,OE10 "
													  + " ,OE11 "
													  + " ,OE12 "
													  + " ,OE13 "
													  + " ,OE14 "
													  + " ,OE15 "
													  + " ,OE16 "
													  + " ,OE17 "
													  + " ,OE18 "
													  + " ,OE19 "
													  + " ,OE20 "
													  + " ,OE21 "
													  + " ,OE22 "
													  + " ,OE23 "
													  + " ,OE24 "
													  + " ,OE25 "
													  + " ,OE26 "
													  + " ,OE27 "
													  + " ,OE28 "
													  + " ,OE29 "
													  + " ,OE30 "
													  + " ,OE31 "
													  + " ,OE32 "
													  + " ,OE33 "
													  + " ,OE34 "
													  + " ,OE35 "
													  + " ,OE36 "
													  + " ,OE37 "
													  + " ,OE38 "
													  + " ,OE39 "
													  + " ,OE40 "
													  + " ,OE41 "
													  + " ,OE42 "
													  + " ,OE43 "
													  + " ,OE44 "
													  + " ,OE45 "
													  + " ,OE46 "
													  + " ,OE47 "
													  + " ,OE48 "
													  + " ,OE49 "
													  + " ,OE50 "
													  + " ,OE51 "
													  + " ,OE52 "
													  + " ,OE53 "
													  + " ,OE54 "
													  + " ,OE55 "
													  + " ,OE56 "
													  + " ,OE57 "
													  + " ,OE58 "
													  + " ,OE59 "
													  + " ,OE60 "
													  + "FROM TRPRX000 " 
													  + "WHERE TABNO = ? "
													  + "  AND TBLKEYS = ? "
													  + "  AND (STDATE <= ?  AND ENDATE >= ?) "
													  + "WITH UR ";
	
	public static final String SQL_GET_STORE_MGR_NAME = "SELECT NAME "
													  + "FROM PERSON_PROFILES_T "
													  + "WHERE STATUS_CD = ? "
													  + "  AND ORGANIZATION_1 = ? "
													  + "  AND JOB_TITLE_ID IN ( {0} ) "
													  + "WITH UR ";
	
	public static final String SQL_UPDATE_INIT_INTERVIEW_DATE = "UPDATE HR_STR_REQN_CAND " 
															  + "SET INTVW_RSLT_CRT_DT = ? " 
 															  + "WHERE HR_SYS_STR_NBR = ? " 
															  + "   AND EMPLT_REQN_NBR = ? " 
															  + "   AND EMPLT_POSN_CAND_ID = ? ";
	
	public static final String SQL_UPDATE_INIT_OFFER_DATE = "UPDATE HR_STR_REQN_CAND " 
			  											  + "SET INIT_OFFR_MADE_DT = ? " 
			  											  + "WHERE HR_SYS_STR_NBR = ? " 
			  											  + "   AND EMPLT_REQN_NBR = ? " 
			  											  + "   AND EMPLT_POSN_CAND_ID = ? ";
	

	public static final String SQL_READ_HR_STR_REQN_CAND = "SELECT HR_SYS_STR_NBR "
														 + "      ,EMPLT_REQN_NBR "
														 + "      ,EMPLT_POSN_CAND_ID "
														 + "      ,CRT_TS "
														 + "      ,CRT_USER_ID "
														 + "      ,INTVW_STAT_IND "
														 + "      ,REF_CHK_RSLT_IND "
														 + "      ,DRUG_TEST_RSLT_CD "
														 + "      ,CAND_OFFR_MADE_FLG "
														 + "      ,CAND_OFFR_STAT_IND "
														 + "      ,INACTV_DT "
														 + "      ,ACTV_FLG "
														 + "      ,LAST_UPD_USER_ID "
														 + "      ,LAST_UPD_TS "
														 + "      ,HR_ACTN_SEQ_NBR "
														 + "      ,EMPL_STAT_ACTN_CD "
														 + "      ,ACTV_DT "
														 + "      ,DRUG_TEST_ID "
														 + "      ,CAND_OFFR_MADE_DT "
														 + "      ,OFFR_DCLN_RSN_CD "
														 + "      ,LTR_OF_INTENT_DT "
														 + "      ,ADVRS_ACTN_DT "
														 + "      ,INIT_OFFR_MADE_DT "
														 + "      ,INTVW_RSLT_CRT_DT "
														 + "FROM HR_STR_REQN_CAND "
														 + "WHERE HR_SYS_STR_NBR = ? "
														 + "  AND EMPLT_REQN_NBR = ? "
														 + "  AND EMPLT_POSN_CAND_ID = ? "
														 + "WITH UR ";

	public static final String SQL_GET_ASSOCIATE_REVIEW_SCORES  = "SELECT DISTINCT CHAR(A1.EFF_DT, ISO) DATE "
															    + "               ,A1.SEQUENCE_NO SEQ "
															    + "               ,A1.USER_COMP_FLD_1 PERFORMANCE "
															    + "               ,C1.EVAL_RTG_DESC PERFORMANCE_DESC "
															    + "FROM EE_COMPS_T A1 "
															    + "     INNER JOIN HR_EMPL_TSRT_XREF AXREF ON AXREF.TSRT_EMPL_ID = A1.PERSON_ID "
															    + "     LEFT OUTER JOIN EVAL_CTGRY_HR_RTG B1 ON B1.HR_SYS_RTG_CD = A1.USER_COMP_FLD_1 "
															    + "                                         AND B1.EVAL_CTGRY_CD = ? "
															    + "                                         AND A1.EFF_DT >= B1.EFF_BGN_DT "
															    + "                                         AND A1.EFF_DT <= B1.EFF_END_DT " 
															    + "     LEFT OUTER JOIN N_EVAL_RTG_CD C1 ON C1.EVAL_RTG_CD = B1.EVAL_RTG_CD "
															    + "                                     AND C1.LANG_CD = ? "
															    + "WHERE AXREF.Z_EMPLID = ? "
															    + "  AND A1.RECORD_STATUS = ? "
															    + "  AND A1.COMP_ACTN IN ( {0} ) "
															    + "  AND (B1.EVAL_RTG_CD IS NULL OR B1.EVAL_RTG_CD <= ?) "
															    + "UNION "
															    + "SELECT DISTINCT CHAR(A1.EFF_DT, ISO) DATE "
															    + "               ,A1.SEQUENCE_NO SEQ "
															    + "               ,A1.USER_COMP_FLD_1 PERFORMANCE "
															    + "               ,''('' || TRIM(C1.D_EVAL_RTG_CD) || '') '' || C1.EVAL_RTG_DESC PERFORMANCE_DESC "
															    + "FROM EE_COMPS_T A1 "
															    + "     INNER JOIN HR_EMPL_TSRT_XREF AXREF ON AXREF.TSRT_EMPL_ID = A1.PERSON_ID "
															    + "     LEFT OUTER JOIN EVAL_CTGRY_HR_RTG B1 ON B1.HR_SYS_RTG_CD = A1.USER_COMP_FLD_1 "
															    + "                                         AND B1.EVAL_CTGRY_CD = ? "
															    + "                                         AND A1.EFF_DT >= B1.EFF_BGN_DT "
															    + "                                         AND A1.EFF_DT <= B1.EFF_END_DT " 
															    + "     LEFT OUTER JOIN N_EVAL_RTG_CD C1 ON C1.EVAL_RTG_CD = B1.EVAL_RTG_CD "
															    + "                                     AND C1.LANG_CD = ? "
															    + "WHERE AXREF.Z_EMPLID = ? "
															    + "  AND A1.RECORD_STATUS = ? "
															    + "  AND A1.COMP_ACTN IN ( {1} ) "
															    + "  AND (B1.EVAL_RTG_CD IS NOT NULL AND B1.EVAL_RTG_CD > ?) "
															    + "ORDER BY SEQ DESC, DATE DESC "
															    + "WITH UR";


	public static final String SQL_GET_ASSOCIATE_ID_FROM_APPLICANT_ID = "SELECT TXREF.Z_EMPLID "
																	  + "FROM EAPLCNT_SSN_XREF SXREF "
																	  + "    ,HR_EMPL_TSRT_XREF TXREF "
																	  + "WHERE SXREF.EMPLT_APLCNT_ID = ? "
																	  + "  AND SXREF.ACTV_FLG = ? "
																	  + "  AND SXREF.APLCNT_SSN_NBR = TXREF.EMPL_INTL_ID "
																	  + "WITH UR ";

	public static final String SQL_UPDATE_BGC_CNSNT = "UPDATE BGC_CNSNT A "
												    + "SET A.LAST_UPD_USER_ID = ? "
												    + "   ,A.LAST_UPD_TS = ? "
												    + "   ,A.BRTH_DT = ? "
												    + "   ,A.CAND_INITS_SIG_VAL = ? "
												    + "   ,A.DOC_SIG_TS = ? "
												    + "   ,A.BKGCK_AUTH_FLG = ? "
												    + "   ,A.MGR_USER_ID = ? "
												    + "   ,A.MID_NM_FLG = ? "
												    + "   ,A.MID_NM = ? "
												    + "   {0} "
												    + "WHERE A.EMPLT_POSN_CAND_ID = ? "
												    + "  AND A.BGC_CNSNT_TYP_CD = ? "
												    + "  AND A.CNSNT_SIG_DT = (SELECT Max(B.CNSNT_SIG_DT) "
												    + "                        FROM BGC_CNSNT B "
												    + "                        WHERE A.EMPLT_POSN_CAND_ID = B.EMPLT_POSN_CAND_ID) ";
			 
	public static final String SQL_UPDATE_BGC_CNSNT_PLUS_DL = " ,A.DRVR_LIC_NBR = ? " 
															+ " ,A.DRVR_LIC_ST_CD = ? ";
			        
	public static final String SQL_GET_MAX_CNSNT_DT_FROM_BGC_CNSNT_ADDR = "SELECT MAX(CNSNT_SIG_DT) AS CNSNT_DATE FROM BGC_CNSNT_ADDR WHERE EMPLT_POSN_CAND_ID = ? WITH UR ";

	public static final String SQL_GET_MAX_SEQ_NBR_FROM_BGC_CNSNT_ADDR = "SELECT MAX(SEQ_NBR) AS MAX_SEQ FROM BGC_CNSNT_ADDR WHERE EMPLT_POSN_CAND_ID = ? AND CNSNT_SIG_DT = ? WITH UR ";
	
	public static final String SQL_INSERT_BGC_CNSNT_ADDR = "INSERT INTO BGC_CNSNT_ADDR "
													     + "  (EMPLT_POSN_CAND_ID, CNSNT_SIG_DT, BGC_CNSNT_TYP_CD, SEQ_NBR, ADDR_LINE1_TXT "
													     + "  ,ADDR_LINE2_TXT, ADDR_LINE3_TXT, ADDR_LINE4_TXT, ADDR_LINE5_TXT, CITY_NM "
													     + "  ,ST_CD, PSTL_CD, CNTY_NM, CNTRY_CD, ADDR_FRMT_IND, APT_NBR) "
														 + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	public static final String SQL_READ_PHN_SCRN_BANNER_NUM = "SELECT OE32 "
			  												+ "FROM TRPRX000 " 
			  												+ "WHERE TABNO = ? "
			  												+ "  AND TBLKEYS = ? "
			  												+ "  AND (STDATE <= ?  AND ENDATE >= ?) "
			  												+ "WITH UR ";	

	public static final String SQL_READ_AIMS_USING_DIVISIONS = "SELECT DISTINCT A.HR_SYS_DIV_CD "
															 + "               ,A.HR_SYS_DIV_NM "
															 + "FROM HR_SYS_STR_ORG A "
															 + "    ,HR_LOC B "
															 + "    ,STR C "
															 + "WHERE A.HR_SYS_STR_NBR = B.LOC_NBR "
															 + "  AND C.STR_NBR = INTEGER(B.LOC_NBR) "
															 + "  AND B.LOC_TYP_CD = 'STR' "
															 + "  AND B.CNTRY_CD = 'US' "
															 + "  AND (A.HR_STR_EFF_BGN_DT <= CURRENT DATE "
															 + "       AND A.HR_STR_EFF_END_DT >= CURRENT DATE) "
															 + "  AND CURRENT_DATE < C.STR_CLS_DT "
															 + "  AND C.STR_TYP_IND <> 'D' "
															 + "ORDER  BY 2 "
															 + "WITH UR ";
	
	public static final String SQL_READ_AIMS_USING_DISTRICTS = "SELECT DISTINCT A.HR_SYS_RGN_CD "
			 												 + "               ,A.HR_SYS_RGN_NM "
			 												 + "FROM HR_SYS_STR_ORG A "
			 												 + "    ,HR_LOC B "
			 												 + "    ,STR C "
			 												 + "WHERE A.HR_SYS_STR_NBR = B.LOC_NBR "
			 												 + "  AND C.STR_NBR = INTEGER(B.LOC_NBR) "
			 												 + "  AND B.LOC_TYP_CD = 'STR' "
			 												 + "  AND B.CNTRY_CD = 'US' "
			 												 + "  AND (A.HR_STR_EFF_BGN_DT <= CURRENT DATE "
			 												 + "       AND A.HR_STR_EFF_END_DT >= CURRENT DATE) "
			 												 + "  AND CURRENT_DATE < C.STR_CLS_DT "
			 												 + "  AND C.STR_TYP_IND <> 'D' "
			 												 + "ORDER  BY 1 "
			 												 + "WITH UR ";
	
	public static final String SQL_READ_AIMS_USING_REGIONS = "SELECT DISTINCT A.HR_SYS_OGRP_CD "
			 											   + "               ,A.HR_SYS_OGRP_NM "
			 											   + "FROM HR_SYS_STR_ORG A "
			 											   + "    ,HR_LOC B "
			 											   + "    ,STR C "
			 											   + "WHERE A.HR_SYS_STR_NBR = B.LOC_NBR "
			 											   + "  AND C.STR_NBR = INTEGER(B.LOC_NBR) "
			 											   + "  AND B.LOC_TYP_CD = 'STR' "
			 											   + "  AND B.CNTRY_CD = 'US' "
			 											   + "  AND (A.HR_STR_EFF_BGN_DT <= CURRENT DATE "
			 											   + "       AND A.HR_STR_EFF_END_DT >= CURRENT DATE) "
			 											   + "  AND CURRENT_DATE < C.STR_CLS_DT "
			 											   + "  AND C.STR_TYP_IND <> 'D' "
			 											   + "ORDER  BY 2 "
			 											   + "WITH UR ";	
	
	public static final String SQL_CREATE_BGC_CNSNT_SIG_NON_PA = "INSERT INTO BGC_CNSNT_SIG "
															   + "  (EMPLT_POSN_CAND_ID "
															   + "   ,CNSNT_SIG_DT "
															   + "   ,BGC_CNSNT_TYP_CD "
															   + "   ,APLCNT_SIG_VAL "
															   + "   ,CNSNT_STAT_CD "
															   + "   ,CRT_TS "
															   + "   ,CRT_USER_ID "
															   + "   ,LAST_UPD_TS "
															   + "   ,LAST_UPD_USER_ID) "
															  // + "   ,CMPLT_TS) "
															  // + " VALUES (?, ?, 1, '', 1, CURRENT TIMESTAMP, ?, CURRENT TIMESTAMP, ?, CURRENT TIMESTAMP )";
															   + " VALUES (?, ?, 1, '', 3, CURRENT TIMESTAMP, ?, CURRENT TIMESTAMP, ? )";
															 
	public static final String SQL_CREATE_BGC_CNSNT_SIG_PA = "INSERT INTO BGC_CNSNT_SIG "
			   											   + "  (EMPLT_POSN_CAND_ID "
			   											   + "   ,CNSNT_SIG_DT "
			   											   + "   ,BGC_CNSNT_TYP_CD "
			   											   + "   ,APLCNT_SIG_VAL "
			   											   + "   ,PRFL_ID "
			   											   + "   ,CNSNT_STAT_CD "
			   											   + "   ,CRT_TS "
			   											   + "   ,CRT_USER_ID "
			   											   + "   ,LAST_UPD_TS "
			   											   + "   ,LAST_UPD_USER_ID "
			   											   + "   ,APLCNT_LINK_KEY) "
			   											   + " VALUES (?, ?, 1, '', ?,  0,  CURRENT TIMESTAMP, ?, CURRENT TIMESTAMP, ?, ? )";	
	
	public static final String SQL_READ_BGC_CNSNT_SIG_PA = "SELECT CNSNT_SIG_DT, CNSNT_STAT_CD, CMPLT_TS, APLCNT_LINK_KEY "
														 + "FROM BGC_CNSNT_SIG "
														 + "WHERE EMPLT_POSN_CAND_ID = ? AND CNSNT_SIG_DT > ? "
														 + "ORDER BY CNSNT_SIG_DT DESC "
														 + "WITH UR ";
	
	public static final String SQL_UPDATE_BGC_CNSNT_SIG_PA = "UPDATE BGC_CNSNT_SIG A "
			   											   + "SET A.CNSNT_SIG_DT = ? "
			   											   + "   ,A.PRFL_ID = CONCAT(CONCAT(RTRIM(PRFL_ID),','), ?) "
			   											   + "   ,A.LAST_UPD_TS = CURRENT TIMESTAMP "
			   											   + "   ,A.LAST_UPD_USER_ID = ? "
			   											   + "   ,A.APLCNT_LINK_KEY = ? "
			   											   + "WHERE A.EMPLT_POSN_CAND_ID = ? "
			   											   + "  AND A.CNSNT_SIG_DT = (SELECT MAX(B.CNSNT_SIG_DT) "
			   											   + "                        FROM BGC_CNSNT_SIG B "
			   											   + "                        WHERE A.EMPLT_POSN_CAND_ID = B.EMPLT_POSN_CAND_ID) ";
	
} // end interface DAOConstants
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingConstants.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Class : RetailStaffingConstants Description: This class has all the constants
 * used in RetailStaffing Application Extends:None Implements:None
 * 
 * @author TCS
 * 
 */
public interface RetailStaffingConstants
{
	// application level constants
	public static final String EMPTY_STRING = "";

	// Log Exception Constants
	public static final String RETURN_MESSAGE_FATAL_ERROR = "Application has Encountered a Fatal Error.";
	public static final String ERROR_PARSE_DATE = "Error occurred when parsing the date.";
	public static final String ERROR_CONVERT_BOOLEAN = "Error occurred while converting to boolean.";
	public static final String ERROR_PARSE_TIMESTAMP = "Error occurred when parsing the timestamp.";

	// Statuses
	public static final String SUCCESS_APP_STATUS = "SUCCESS";
	public static final String CAN_FORM = "Candidate";
	public static final String ITI_NBR_FORM = "InterviewNbr";
	public static final String REQ_FORM = "Requisition";

	// FormName Constants
	public static final String ERROR_APP_STATUS = "ERROR";

	// INSERT/Update
	public static final String INSERT = "INSERT";
	public static final String UPDATE = "UPDATE";

	// Messages
	public static final String ITI_INSERT_SUCCESS = "Phone Screen Details inserted successfully.";
	public static final String ITI_UPDATE_SUCCESS = "Phone Screen Details updated successfully.";
	public static final String ITI_CREATE_SUCCESS = "Phone Screen Details created successfully.";
	public static final String REVIEW_COMPLETED_PHONE_SCREENS = "Review Completed Phone Screens updated successfully.";
	public static final String REJECTION_DETAILS_CREATED = "Rejection Details have been created successfully" ;
	public static final String REJECTION_DETAILS_FOUND = "Rejection Details have been retrieved successfully" ;

	// summary constants
	public static final String CONDUCT_BASIC_PHONE_SCREENS = "Perform Basic Phone Screens";
	public static final String CONDUCT_DETAILED_PHONE_SCREENS = "Perform Detailed Phone Screens";
	public static final String CONDUCT_REVIEWCOMPLETED_PHONE_SCREENS = "Review Completed Phone Screens";
	public static final String CONDUCT_SCHEDULEINTERVIEW_PHONE_SCREENS = "Schedule Interviews";
	public static final String VIEW_REQUISITIONS = "Review Active Requisitions";
	public static final String VIEW_INACTIVE_REQUISITIONS = "View Inactive Requisitions";
	public static final String VIEW_SEND_INTERVIEW_MATERIALS = "Send Interview Materials";
 
	public static final String ORGID_STORE = "store";
	public static final String ORGID_DISTRICT = "district";
	public static final String ORGID_REGION = "region";
	public static final String ORGID_DIVISION = "division";
	
	// To fetch the Hone screen details of a requisition based on the Active flag	
	public static final String PHN_ACTV = "Y";
	public static final String PHN_INACTV = "N";
	public static final String PHN_HOLD = "H";

	// flag internal external
	public static final String INTERNAL_FLAG = "INTERNAL";
	public static final String EXTERNAL_FLAG = "EXTERNAL";

	// Timestamp and date formats
	public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd-kk:mm:ss.SSS";
	public static final String TIME_FORMAT = "yyyy-MM-dd-kk:mm:ss.SSS";
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	// Interview Location Typ Code
	public static final String HIRING_STR_TYP_CD = "1";
	public static final String NON_HIRING_STR_TYP_CD = "2";
	public static final String HIRING_EVT_TYP_CD = "3";
	public static final String OTHR_LOC_TYP_CD = "4";

	// minimum requirement flag
	public static final String MINIMUM_REQUR_YES_FLG = "Yes";
	public static final String MINIMUM_REQUR_NO_FLG = "No";
	public static final String MINIMUM_REQUR_NA_FLG = "N/A";
	public static final String MINIMUM_REQUR_NA_FLEX_FLG = "A";

	// minimum requirement flag - DB Update.
	public static final String MINIMUM_REQUR_YES__DB_FLG = "Y";
	public static final String MINIMUM_REQUR_NO_DB_FLG = "N";
	public static final String MINIMUM_REQUR_NA_DB_FLG = "A";

	// Note Type Code
	public static final short CONTACT_HISTRY_NOTE_TYP_CD = 1;
	public static final short PHN_SCRN_RESPONSE_NOTE_TYP_CD = 4;
	public static final short QUALIFIED_POOL_NOTE_TYP_CD = 2;
	public static final short REFERRALS_NOTE_TYP_CD = 3;

	// boolean to string conversion
	public static final String TRUE = "Y";
	public static final String FALSE = "N";
	
	public static final String AUTO_HOLD_FLAG_TRUE = "1";
	public static final String AUTO_HOLD_FLAG_FALSE = "0";

	// ITI statuses
	public static final short BASIC_ITI_STATUS = 1;
	public static final short DETAILED_ITI_STATUS = 2;
	public static final short DO_NOT_PROCEED_STAT = 5;
	public static final short PROCEED_STAT = 12;
	public static final short SCHEDULEINTERVIEW_ITI_STATUS = 9;
	public static final String IVR_USER = "IVR";
	public static final String IVR_CONDUCTED_PHONE_SCREEN = "Automated IVR Phone Screen";

	//Interview Status Codes
	public static final short  INTERVIEW_STATUS_CODE_ZERO = 0;	
	public static final short  INTERVIEW_SCHEDULED = 11;
	public static final short  SCHEDULE_INTERVIEW = 9;
	public static final short  STORE_SCHEDULING = 12;        //NEW RSA 2.0
	public static final short  UNABLE_TO_SCHEDULE = 10;
	//RSC Automation More IVR Changes
	public static final short  PENDING_SCHEDULING = 19;
	public static final short  PENDING_SCHEDULE_OFFER_INTV = 16;
	public static final short  PENDING_SCHEDULE_CALENDAR = 15;
	public static final short  INTERVIEW_LEFT_MESSAGE = 17;
	public static final short  INTERVIEW_INVALID_NUMBER = 18;
    
	// Phone Screen Status Codes (RSA 2.0)
	public static final short INITIATE_BASIC_PHONE_SCREEN = 21;
	public static final short INITIATE_DETAILED_PHONE_SCREEN = 21;
	public static final short PHONE_SCREEN_COMPLETED = 4;
	public static final short STORE_ADMINISTERED = 3;
	public static final short PHONE_SCREEN_FAVORABLE = 15;
	public static final short INITIATE_SELF_SCREEN = 21;

	// Interview Material Statuses  (RSA 2.0)
	public static final short INTERVIEW_MAT_STATUS_CODE_ZERO = 0;	
	public static final short INTERVIEW_MATERIAL_SENT = 1;     //NEW RSA 2.0
	public static final short SEND_CALENDAR_PACKET = 2;        //NEW RSA 2.0
	public static final short SEND_EMAIL_PACKET = 3;          //NEW RSA 2.0

	public static final List<Short> ALL_POSSIBLE_STATUSES = new ArrayList<Short>() {
		private static final long serialVersionUID = 1L;
		{add(Short.valueOf("0"));add(Short.valueOf("1"));add(Short.valueOf("2"));add(Short.valueOf("3"));add(Short.valueOf("4"));
		 add(Short.valueOf("5"));add(Short.valueOf("6"));add(Short.valueOf("7"));add(Short.valueOf("8"));add(Short.valueOf("9"));
		 add(Short.valueOf("10"));add(Short.valueOf("11"));add(Short.valueOf("12"));add(Short.valueOf("13"));add(Short.valueOf("14"));
		 add(Short.valueOf("15"));add(Short.valueOf("16"));add(Short.valueOf("17"));add(Short.valueOf("18"));add(Short.valueOf("19"));
		 add(Short.valueOf("20"));add(Short.valueOf("21"));add(Short.valueOf("22"));add(Short.valueOf("23"));add(Short.valueOf("24"));
		 add(Short.valueOf("25"));}
	};

	public static final short REQ_STATUS_CD_IN_PROCESS = 1;
	public static final int CONTACT_HIS_LENGTH=2000;
//	
//	//Authorisation position count included for the review completed phone screens
//	
	public static final int AuthorizationPositionCount = 1;

	public static final String ACCEPTED_STATUS = "AC";
	public static final String DECLINED_STATUS = "DE";

	// Pagination record cache size
	public static final int CACHED_RECORD_SIZE = 100;
	public static final String SPACE_STRING = " ";

	public static final String ITI_DTL_LIST = "itiDtlList";
	public static final String REQ_DTL_LIST = "reqDetList";
	public static final String PAGINATION_TOKEN = "paginationToken";

	// The various error codes used in the application.
	public static final int INVALID_INPUT_CODE = 100;
	public static final int APP_FATAL_ERROR_CODE = 1000;
	public static final int NO_RECORDS_FOUND_ERROR_CODE = 200;
	public static final int FETCHING_CANDIDATE_DETAILS_ERROR_CODE = 300;
	public static final int FETCHING_AVAILABLE_REGIONS_ERROR_CODE = 301;
	public static final int FETCHING_AVAILABLE_DIVS_ERROR_CODE = 302;
	public static final int FETCHING_AVAILABLE_DISTS_ERROR_CODE = 303;
	public static final int UPDATING_PHONE_DETAILS_ERROR_CODE = 400;
	public static final int FETCHING_PHONE_DETAILS_ERROR_CODE = 401;
	public static final int FETCHING_INTERVIEW_DETAILS_ERROR_CODE = 402;
	public static final int FETCHING_MINRESPONSES_DETAILS_ERROR_CODE = 404;
	public static final int FETCHING_REQUISITIONNOTE_DETAILS_ERROR_CODE = 405;
	public static final int FETCHING_STAFFING_DETAILS_ERROR_CODE = 406;
	public static final int FETCHING_INTERVIEW_RESPONSES_DETAILS_ERROR_CODE = 407;
	public static final int INSERTING_REJECTION_DETAILS_ERROR_CODE = 410 ;
	public static final int FETCHING_REQUISITION_DETAILS_ERROR_CODE = 500;
	public static final int FETCHING_INACTIVE_REQUISITION_DETAILS_ERROR_CODE = 508;
	public static final int UPDATING_REQUISITION_STAFFING_DETAILS_ERROR_CODE = 501;
	public static final int UPDATING_REVIEW_PHONE_SCREEN_ERROR_CODE = 510;
	public static final int INVALID_STORE_ERROR_CODE = 600;
	public static final int INSERTING_PHONE_DETAILS_ERROR_CODE = 700;
	public static final int NO_FURTHER_RECORDS_FOUND_ERROR_CODE = 800;
	public static final int FETCHING_POM_RSA_CROSS_REF_ERROR_CODE = 801;
	public static final int FETCHING_INTERVIEW_TIMESLOTS_ERROR_CODE = 900;
	public static final int UPDATING_INTERVIEW_TIMESLOTS_STATUS_ERROR_CODE = 901;
	public static final short REQ_STATUS_CD = 1;
	
	//The various error messages
	public static final String NO_RECORDS_FOUND_ERROR_MSG = "The search criteria given are ";
	public static final String FETCHING_CANDIDATE_DETAILS_ERROR_MSG = "The candidate id being fetched is ";
	public static final String UPDATING_PHONE_DETAILS_ERROR_MSG = "The phone screen number being updated is ";
	public static final String FETCHING_PHONE_DETAILS_ERROR_MSG = "The phone screen number being fetched is ";
	public static final String FETCHING_INTERVIEW_DETAILS_ERROR_MSG = "The interview details fetched for ";
	public static final String FETCHING_MINRESPONSES_DETAILS_ERROR_MSG = "The minimum response fetched for ";
	public static final String FETCHING_REQUISITIONNOTE_DETAILS_ERROR_MSG = "The requisition note details fetched for ";
	public static final String FETCHING_STAFFING_DETAILS_ERROR_MSG = "The staffing details fetched for ";
	public static final String FETCHING_REQUISITION_DETAILS_ERROR_MSG = "The requistion details fetched for ";
	public static final String UPDATING_REQUISITION_STAFFING_DETAILS_ERROR_MSG = "The requisition staffing details updated for ";
	public static final String SUBMIT_REQUISITION_REQUEST_DETAILS_ERROR_CODE_MSG = "The submit requisition request failed...";
	public static final String INVALID_STORE_ERROR_MSG = "The store is provided is ";
	public static final String INSERTING_PHONE_DETAILS_ERROR_MSG = "The phone screen details inserted for ";
	public static final String UPDATING_REVIEW_PHONE_SCREEN_DETAILS_ERROR_MSG = "The review Phone screen updated for ";
	public static final String INSERTING_REJECTION_DETAILS_ERROR_MSG = "Error occurred while inserting attach reason for ";
	
	// Status update message for contact history
	public static final short SCHEDULING_INTERVAL_30 = 30;
	public static final short SCHEDULING_INTERVAL_60 = 60;

	//RSA3.0
	public static final String APPLICANT_POOL_LIST = "applicantPoolList";
	public static final String ASSOCIATE_POOL_LIST = "associatePoolList";
	// Status codes for Interview Available Slots.
	public static final short AVALIABLE_STATUS_CODE = 1;
	public static final short RESERVED_STATUS_CODE = 2;
	public static final short SCHEDULED_STATUS_CODE = 3;
	public static final short CANCELLED_STATUS_CODE = 4;
	public static final short RECORDS_REQUESTED = 1;

	public static final String YES = "Y";
	public static final String NO = "N";
	// format string that will be used to parse String dates into date objects
	public static final String INTRVW_SVC_DATE_FORMAT = "MM-dd-yyyy";	
	// format string that will be used to format dates into time objects
	public static final String INTRVW_SVC_TIME_FORMAT = "HH:mm";		
	
	//RSA 5.0 Interview Results Errors
	public static final int INTVW_RESULT_SCRN_CAND_FETCH = 950;
	public static final int INTVW_RESULT_SCRN_UPDATE_ERR = 951;
	
	//ATS
	public static final String INSERTING_MARK_APPLICANT_AS_CONSIDERED_ERROR_MSG = "Error occurred inserting considered status for applicants.";
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	public static final String USER_PROFILE_DET_NOT_FOUND ="User Profile Details Not Found";
    
    public static final String USER_PROFILE_SERVICE_PATH = "/UserProfileService";
    public static final String GET_USER_PROFILE_DETAILS_PATH = "/getUserProfileDetails";
    
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String VERSION = "version";
    public static final int VERION_ONE = 1;
    public static final String FAILURE_APP_STATUS = "FAILURE";
    public static final int USER_PROFILE_DET_NOT_FOUND_CODE = 501; 
    public static final int USER_PROFILE_EXCEPTION_ERROR_CODE= 502;
    public static final String COMMA = ",";
    public static final String STATUS_FALSE = "false";
    public static final String APPLICATION_ERROR = "Application Error";
    public static final String TIMESTAMPTO_CLASS_NAME = "TimeStampTO";
    public static final String FORMAT_TWO_PRECISION = "#.##";
    public static final String UNDER_SCORE = "-";
    public static final String SEMI_COLON = ";";
    public static final String DISPLAY_DATE_FORMAT = "MM/dd/yyyy";
    public static final String FORMAT_24_HOURS = "hh:mm";
    public static final String FORMAT_12_HOURS = "hh:mm a";
    public static final String SLASH = "/";
    //End - Added as part of Flex to HTML Conversion - 13 May 2015
    
    public static final String DISPLAY_TIMESTAMP_FORMAT = "MM/dd/yyyy hh:mm a";
    
	//Voltage Implementation
	public static final String LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_NAME = "javax.net.ssl.trustStore";
	public static final String LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_VALUE = "C:\\LocalVoltageSecurity\\voltagekeystore.jks";
	public static final String LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_NAME = "javax.net.ssl.trustStorePassword";
	public static final String LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_VALUE = "changeit";
	
	public static final String LOCALHOST_TRUST_STORE_PROPERTY_NAME = "javax.net.ssl.trustStore";
	public static final String LOCALHOST_TRUST_STORE_PROPERTY_VALUE = "C:\\LocalVoltageSecurity\\localhost.jks";
	public static final String LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_NAME = "javax.net.ssl.trustStorePassword";
	public static final String LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_VALUE = "changeit";	
	
	public static final String LIFE_CYCLE_PROPERTY = "host.LCP";
	public static final String LIFE_CYCLE_AD = "AD";
	public static final String LIFE_CYCLE_QA = "QA";
	public static final String LIFE_CYCLE_QP = "QP";
	public static final String LIFE_CYCLE_ST = "ST";
	public static final String LIFE_CYCLE_PR = "PR";
	public static final int SSN_PROTECT = 1;
	public static final String SSN_PROTECT_URL_DEV = "https://localhost:8443/VoltageWebService/service/VoltageClientService/protectFormattedDataSSN";
	public static final String SSN_PROTECT_URL_QA = "https://webapps-qa.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataSSN";	
	public static final String SSN_PROTECT_URL_QP = "https://webapps-qp.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataSSN";	
	public static final String SSN_PROTECT_URL_ST = "https://webapps-st.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataSSN";
	public static final String SSN_PROTECT_URL_PR = "https://webapps.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataSSN";
	public static final int SSN_ACCESS = 2;
	public static final String SSN_ACCESS_URL_DEV = "https://localhost:8443/VoltageWebService/service/VoltageClientService/accessFormattedDataSSN";
	public static final String SSN_ACCESS_URL_QA = "https://webapps-qa.homedepot.com/VoltageWebService/service/VoltageClientService/accessFormattedDataSSN";	
	public static final String SSN_ACCESS_URL_QP = "https://webapps-qp.homedepot.com/VoltageWebService/service/VoltageClientService/accessFormattedDataSSN";	
	public static final String SSN_ACCESS_URL_ST = "https://webapps-st.homedepot.com/VoltageWebService/service/VoltageClientService/accessFormattedDataSSN";
	public static final String SSN_ACCESS_URL_PR = "https://webapps.homedepot.com/VoltageWebService/service/VoltageClientService/accessFormattedDataSSN";		
	public static final int DL_PROTECT = 3;
	public static final String DL_PROTECT_URL_DEV = "https://localhost:8443/VoltageWebService/service/VoltageClientService/protectFormattedDataDL";
	public static final String DL_PROTECT_URL_QA = "https://webapps-qa.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataDL";	
	public static final String DL_PROTECT_URL_QP = "https://webapps-qp.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataDL";	
	public static final String DL_PROTECT_URL_ST = "https://webapps-st.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataDL";
	public static final String DL_PROTECT_URL_PR = "https://webapps.homedepot.com/VoltageWebService/service/VoltageClientService/protectFormattedDataDL";
	
	//Added for FMS 7894 IVR Changes.	
	public static final int POSITION_LOCATION_QUEST_TYPE_CD = 11;
	public static final int EMPLOYMENT_CATEGORY_QUEST_TYPE_CD = 12;
	public static final int HOURLY_RATE_QUEST_TYPE_CD = 13;
	public static final int WEEKDAYS_QUEST_TYPE_CD = 14;
	public static final int SATURDAY_QUEST_TYPE_CD = 15;
	public static final int SUNDAY_QUEST_TYPE_CD = 16;
	public static final int EARLY_AM_QUEST_TYPE_CD = 17;
	public static final int MORNING_QUEST_TYPE_CD = 18;
	public static final int AFTERNOON_QUEST_TYPE_CD = 19;
	public static final int NIGHT_QUEST_TYPE_CD = 20;
	public static final int LATE_NIGHT_QUEST_TYPE_CD = 21;
	public static final int OVERNIGHT_QUEST_TYPE_CD = 22;
	public static final int REASONABLE_ACCOMMODATION_QUEST_TYPE_CD = 23;
	public static final int REQUIRES_DRIVERS_LICENSE_QUEST_TYPE_CD = 24;
	public static final int RELIABLE_TRANSPORTATION_QUEST_TYPE_CD = 25;
	//Added for FMS 7894 IVR Changes.	
	
	public static final Short PHN_SCRN_STAT_ZERO = 0;
	public static final Short STATUS_TYPE_CODE_PHN_SCRN = 1; 
	public static final Short INTERVIEW_STAT_ZERO = 0;
	public static final Short STATUS_TYPE_CODE_INTERVIEW_SCHD = 2;
	
	//Review Constants
	public static final int EVAL_CAT_CODE_1 = 1;
	public static final int EVAL_CAT_CODE_2 = 2;
	public static final int EVAL_CAT_CODE_4 = 4;
	public static final String en_US = "en_US";
	public static final int EVAL_RTG_CODE_27 = 27;
	public static final String ACTIVE = "A";
	
	//Interview Summary Constants
	public static final String AVAILABILITY_MATCH_QUESTION_INDICATOR = "AVL";
}

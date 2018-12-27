/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: constants.js
 * Application: Retail Staffing Admin
 *
 */
var CONSTANTS = new constants();
function constants() {
    // COMMON MESSAGES FOR SERVICES
    this.SERVICE_NOT_FOUND_ERROR = "Error contacting the service. ";
    this.STATUS_SUCCESS = "SUCCESS";
    this.POST = "POST";
    this.GET = "GET";
    this.APPLICATION_JSON = "application/json";
    this.APPLICATION_XML = "application/xml";
    this.FORM_URLENCODED = "application/x-www-form-urlencoded";
    this.DATA_TYPE_JSON = "json";
    this.DATA_TYPE_XML = "xml";
    this.PM = "PM";
    this.AM = "AM";
    this.YES_VALUE = 'YES';
    this.NO_VALUE = 'NO';
    this.SELECT_BOX_MAX_LENGTH = 10;
    this.SUCCESS_ON_HOME = false;

    // Language settings
    this.EN_US = "en_US";
    this.ES_US = "es_US";

    // Manager Home screen
    this.MANAGER_ROLE = "manager";
    this.AACG_ROLE = "aacg";
    this.ERROR_POPUP_TITLE = "Error";
    this.OK = "OK";
    this.SERVER_NOTRESPONDING_FAULTMSG = "System Error occurred, please contact Support";
    this.CONNECTION_ERROR = "CONNECTION ERROR";
    this.TRUE_FLG = "true";
    this.FALSE_FLG = "false";
    this.STATUS_CD_FINAL = "1";
    this.STATUS_CD_DRAFT = "0";
    this.ASSOCIATE_ROLE = "associate";
    this.IMAGING_ASSOCIATE_ROLE = "associate_imaging";
    this.ONE = "1";
    this.TWO = "2";
    this.TRUE = true;
    this.FALSE = false;

    // Email admin module
    this.RESEND_QUEWEB_EMAIL = "resendQueWebEmail";
    this.RESEND_QUEWEB_URL = "/RetailStaffing/service/RetailStaffingService/resendQueWebEmail";
    this.SUCCESS = "SUCCESS";
    this.GET_USER_PROFILE = "/RetailStaffing/service/UserProfileService/getUserProfileDetails ";

    // Requisition Details
    this.Zero = 0;
    this.NUM_SCHE_PREF_ENTRIES = 7;
    this.NUM_DAYS_OF_WEEK = 7;
    this.PROCEED = "PROCEED";
    this.DO_NOT_PROCEED = "DO NOT PROCEED";
    this.INSERT = "INSERT";
    this.UPDATE = "UPDATE";
    this.HOUR_ON_TWELVE = "12";
    this.YES = 'Y';
    this.NO = 'N';
    this.evntTypReq = "ReqSubmit";
    this.evntTypReqSave = "ReqSave";
    this.evntTyp = "createPhnScreen";
    this.DEFAULT_STATUS = "21";
    //Detailed Screen type
    this.SCREEN_TYPE_DET = "DETAILED";
    this.STATUS_ON_DETAIL = "21";
    this.CONSIDERED_IN_QP_CD = 5;
    //maximum Applicants allowed per page
    this.APPLICANTS_PER_PAGE = 25;
    //maximum Associates allowed per page
    this.ASSOCIATES_PER_PAGE = 25;
    this.APPEND = "00";
    this.QP_SELECTED_TAB = 0;
    this.APPL_PROF_TO_REQ_DET = false;
    //Service Root Url
    this.RSA_SERVICE_MAIN_PATH = "/RetailStaffing/service";
    this.RSA_SERVICE_MID_PATH = "/RetailStaffingService";
    this.GET_REQ_DTL_PATH = "/getRequisitionDetails";
    this.INTERVIEW_SERVICE_MID_PATH = "/InterviewService";
    this.GET_STR_HIRING_EVENTS_PATH = "/requisitionHiringEvents";
    this.GET_STR_CALENDARS_PATH = "/requisitionCalendars";
    this.HIRING_SERVICE_MID_PATH = "/HiringEventService";
    this.GET_HIRING_EVENT_DETAILS_PATH = "/getHiringEventDetails";
    this.SAVE_REQ_DTL_PATH = "/updateStaffingDetails";
    this.CREATE_PHN_SCR_PATH = "/createPhoneScreens";
    this.GET_CAND_COUNT = "/getCandidateCount";
    this.MARK_APPLICANTS_AS_CONSIDERED_IN_QP_PATH = "/markApplicantsAsConsideredInQP";
    this.ATTACH_APPL_TO_REQ_EVENT_PATH = "/attachApplToRequisition";
    this.VIEW_APPL_PROFILE_PATH = "/getApplProfile";
    this.GET_REJECT_METHD = "/getRejectDetails";
    this.CREATE_RJT_DETAILS_PATH = "/createRejectDetails";
    this.GET_PHN_SCRN_DETAILS_POPUP_PATH = "/getPhoneScreenMinimumDetails";
    /*
     * Warning message
     * 1.when service call fails
     * 2.UI validation fails
     * 3. if business rules fails
     */
    this.crtPhnStaffDtlUpdMsg = "Phone Screens Created And Staffing Details Saved Successfully";
    this.STAFF_DET_UNSAVED_DATA_PART_1 = "Unsaved data in Requisition Details.Click OK to Continue,";
    this.STAFF_DET_UNSAVED_DATA_PART_2 = "CANCEL to stay on current page";
    this.STAFF_DET_MISSING_PHONE_SCRN_PART_1 = "Phone Screens have not been created for all candidates. Click OK to Continue, ";
    this.STAFF_DET_MISSING_PHONE_SCRN_PART_2 = "CANCEL to stay on current page and create Phone Screens.";
    this.SOFT_AUTO_HOLD_MSG_PART1 = "Based on the status of existing Phone Screen records, additional Candidates are not needed for this Requisition.";
    this.SOFT_AUTO_HOLD_MSG_PART2 = "Please click 'OK', then review & update Phone Screen status to 'Do Not Proceed' for any candidates";
    this.SOFT_AUTO_HOLD_MSG_PART3 = "no longer being considered for this Requisition.";
    this.SOFT_AUTO_HOLD_MSG_PART4 = "IF new phone screens are still required, click 'Cancel' to continue";
    this.INV_QUEWEBNO = "Invalid Queweb Request Number";
    this.QUEWEBNO_LEN = "Queweb Request Number Should Have 8 Characters";
    this.FILL_MANDATORY_FLDS_HRGMGRNAME = "Please Complete The Required Field, Hiring Manager Name";
    this.FILL_MANDATORY_FLDS_HRGMGRPHONE = "Please Complete The Required Field, Hiring Manager Phone";
    this.FILL_MANDATORY_FLDS_QUEWEBNUM = "Please Complete The Required Field, Queweb Request Number";
    this.FILL_MANDATORY_FLDS = "Please Complete Required Fields in Staffing Details Section";
    this.INTVW_DUR_VAL_MSG = "Please Select an Interview Duration";
    this.FILL_REQUIRED_FLDS_CALENDAR = "Please Complete The Required Field, Select Calendar";
    this.FILL_REQUIRED_FLDS_HIRING_EVENT = "Please Complete The Required Field, Select Hiring Event";
    this.REQ_EVNT_DATE = "Please enter Hiring event end date greater than start date";
    this.REQ_STAFF_LBL_HDR = "Please Enter Queweb Request Number";
    this.HOUR_ALERT = "Please enter proper hour value for";
    this.HIRE_EVNT_STRT_ALERT = " Hiring Event Start Time";
    this.MINUTE_ALERT = "Please enter proper minute value for";
    this.AMPM_ALERT = "Please enter proper AM/PM for";
    this.HIRE_EVNT_HR_MIN_NUMBER = "Please Enter Proper Hour/Minute Value for";
    this.HIRE_EVNT_PHN_NUMBER = "Please enter valid Hiring Event Phone ";
    this.HIRE_EVNT_MGR_NUMBER = "Please enter valid Hiring Manager Phone ";
    this.PHN_SCR_AVL = "There are no candidates attached to create phone screen ";
    this.SEARCH_INFO_MSG_LBL = "Search for a Requisition, Candidate, Phone Screen Number, Summary";
    this.QUALIFIED_POOL_TTL = "QUALIFIED POOL";
    this.TGT_PAY_VAL_MSG = "Please Enter a Valid Target Pay Value";
    this.NO_RJCT_SELECTED_WARNING = "Please select a reason for not attaching the candidate";
    this.RJCT_DTLS_UNSAVED_PART_1 = "Attach candidate details have not been saved. OK to continue";
    this.RJCT_DTLS_UNSAVED_PART_2 = "Click OK to continue";

    // Retail Staffing Request
    this.HIRING_EVENT_CALENDAR_TYPE = 100;
    this.NUM_SCHE_PREF_ENTRIES = 7;
    this.NUM_DAYS_OF_WEEK = 7;
    this.SPECIAL_CALENDAR_TYPE = 10;
    this.TIME_SLOT_MESSAGE = 'Please select only the specific day and time options that a candidate must be able to work.  Only 1 or 2 time slots may be selected.  Otherwise, please select "No" for the question "Do you need candidates to fill a specific day/time slot?" and all candidates will be sent for your consideration regardless of their schedule restrictions.';
    this.SCHD_TIME_SLOT_MESSAGE = 'You must select at least one day selection and up to 2 time selections.  Otherwise, please select "No" for the question "Do you need candidates to fill a specific day/time slot?" and all candidates will be sent for your consideration regardless of their schedule restrictions.';
    // Interview results
    this.ID_HF = 5;
    this.ID_F = 4;
    this.ID_A = 3;
    this.ID_U = 2;
    this.ID_HU = 1;
    this.ID_NA = 0;

    // Interview Screen
    this.ConfirmationNo = 0;
    this.CallType = "";
    this.isEnableIntrwScrnWarnPop = false;
    /*
     * Warning message
     * 1.Maximum limit exceeds
     * 2.validate if user has special instruction
     * 3.Warning message for unsaved data
     */
    this.WARN_INTRV_SCRN_HIRED_1 = "Enough people have been extended offers or have been hired for all the open positions on this job requisition.";
    this.WARN_INTRV_SCRN_SCHEDULED_1 = "Enough people have been scheduled for all the requested interviews for this job requisition.";
    this.WARN_INTRV_SCRN_SCHEDULED_2 = "You should not schedule this interview unless you have been given special instructions. ";
    this.WARN_INTRV_SCRN_SCHEDULED_3 = "Click the Home button to return to the search page. Click 'OK' to continue.";

    // validate messages based on job title indicator in phn scrn Responses.
    this.HOUR_INTERNAL_TRANS_PHN_SCRN_MSG = "Please use the Hourly INTERNAL Transfer Phone Screen ";
    this.MERCH_EXEC_AREA_SUPV_PHN_SCRN_MSG = "Please use the MERCH EXECUTION AREA SUPV Phone Screen";
    this.MERCH_EXEC_ASSOC_INT_PHN_SCRN_MSG = "Please use the INTERNAL MERCHANDISING EXECUTION ASSOC Phone Screen";
    this.MERCH_EXEC_ASSOC_EXT_PHN_SCRN_MSG = "Please use the MERCHANDISING EXECUTION ASSOC Phone Screen";
    this.MERCH_EXEC_ASSOC_NTR_INT_PHN_SCRN_MSG = "Please use the INTERNAL NO TRAVEL MERCHANDISING EXECUTION ASSOC Phone Screen";
    this.MERCH_EXEC_ASSOC_NTR_EXT_PHN_SCRN_MSG = "Please use the NO TRAVEL MERCHANDISING EXECUTION ASSOC Phone Screen";
    this.ASSET_PROTECT_SPEC_PHN_SCRN_MSG = "Please use the ASSET PROTECTION SPECIALIST Phone Screen";
    this.GENERAL_WARE_ASSOC_II_PHN_SCRN_MSG = "Please use the GENERAL WAREHOUSE ASSOC II Phone Screen";
    //Warning message for Phone screen,Basic & Detailed phone screen
    this.DEPT_SUPER_PHN_SCRN_MSG = "Please use the DEPARTMENT SUPERVISOR Phone Screen";
    this.BASIC_PHN_SCRN_MSG = "Please use the BASIC Phone Screen";
    this.DETAILED_PHN_SCRN_MSG = "Please use the DETAILED Phone Screen";
    this.HR_COORDINATOR = "Please use the HR COORDINATOR Phone Screen";
  //Warning message to use GENERAL WAREHOUSE ASSOC Phone Screen
    this.GENERAL_WAREHOUSE_ASSOC_I = "Please use the GENERAL WAREHOUSE ASSOC I Phone Screen";
  //Warning message to use DC ADMIN ASSISTANT Phone Screen
    this.DC_ADMIN_ASSISTANT = "Please use the DC ADMIN ASSISTANT Phone Screen";
  //Warning message to use GENERAL OFFICE ASSOCIATE Phone Screen
    this.GENERAL_OFFICE_ASSOCIATE = "Please use the GENERAL OFFICE ASSOCIATE Phone Screen";
  //Warning message to use DC SYSTEMS COORDINATOR Phone Screen
    this.DC_SYSTEMS_COORDINATOR = "Please use the DC SYSTEMS COORDINATOR Phone Screen";
  //Warning message to use CUSTOMER SERVICE REP Phone Screen
    this.CUSTOMER_SERVICE_REP = "Please use the CUSTOMER SERVICE REP Phone Screen";
  //Warning message to use SWITCHER DRIVER Phone Screen
    this.SWITCHER_DRIVER = "Please use the SWITCHER DRIVER Phone Screen";
    //Warning message to use CASHIER Phone Screen
    this.CASHIER_PHN_SCRN_MSG = "Please use the CASHIER Phone Screen";
  //Warning message to use CRTV WAREHOUSE TECHNICIAN
    this.CRTV_WAREHOUSE_TECH = "Please use the CRTV WAREHOUSE TECHNICIAN Phone Screen";
    //Warning message to use RLC REPAIR TECHNICIAN
    this.RLC_REPAIR_TECH = "Please use the RLC REPAIR TECHNICIAN Phone Screen";
    //Warning message to use RLC parts specialist
    this.RLC_PARTS_SPECIALIST = "Please use the RLC PARTS SPECIALIST Phone Screen";
    //Warning message to use outbound coordinator phone screen
    this.OUTBOUND_COORDINATOR = "Please use the OUTBOUND COORDINATOR Phone Screen";
    this.TOOL_RENTAL_SERVICE_TECHNICIAN = "Please use the TOOL RENTAL SERVICE TECHNICIAN Phone Screen";
    this.RLC_INVENTORY_CONTROL_ASSOCIATE = "Please use the RLC INVENTORY CONTROL ASSOCIATE Phone Screen";
    this.PRO_PAINT_SPECIALIST = "Please use the PRO PAINT SPECIALIST Phone Screen";
    this.MET_SUPER_NO_TRAVEL = "Please use the MERCH EXEC SUPV – NO TRAVEL Phone Screen";
    this.FREIGHT_LOT = "Please use the FREIGHT/LOT Phone Screen";
    this.ASDS_PHN_SCRN_MSG = "Please use the ASSOCIATE SUPPORT DEPT SUPV Phone Screen";    
    this.PRO_ACCT_SALES_PHN_SCRN_MSG = "Please use the PRO ACCT SALES ASSOCIATE Phone Screen";    
    this.DESIGNER_PHN_SCRN_MSG = "Please use the DESIGNER Phone Screen";
    this.IB_DC_DEPT_SUP = "Please Use the DC DEPT SUPERVISOR Phone Screen";
    this.IB_DC_GENERAL_OFFICE = "Please Use the DC GENERAL OFFICE ASSOCIATE Phone Screen";
    this.IB_GENERAL_WAREHOUSE_ASSOC = "Please use the GENERAL WAREHOUSE ASSOCIATE Phone Screen";
    this.DOLLAR = "$ ";
    this.PER_HR = " /hr";
    this.VACANT = "VACANT";
    this.PROCEED = "PROCEED";
    this.DO_NOT_PROCEED = "DO NOT PROCEED";
    this.EXTERN = "EXTERNAL";
    this.evntTypPhnScrnSave = "PhnScrnSave";
    this.evntTypSubmitPhnScrn = "PhnScrnSubmit";
    this.submitPhnScrnBtnClickFlg = false;
    this.savePhnScrnBtnClickFlg = false;
    //Valiadtor messages
    this.DECIMAL_POINT_COUNT_ERROR="The decimal separator can only occur once.";
    this.EXCEEDS_MAX_ERROR="The number entered is too large.";
    this.INTEGER_ERROR="The number must be an integer.";
    this.INVALID_CHAR_ERROR="The input contains invalid characters.";
    this.LOWER_THAN_MIN_ERROR="The amount entered is too small.";
    this.PRECISION_ERROR="The amount entered has too many digits beyond the decimal point.";
    this.DISALLOWED_LOCALNAME_CHARS = "()<>,;:\\\"[] `~!#$%^&*={}|/?'";
    this.DISALLOWED_DOMAIN_CHARS = "()<>,;:\\\"[] `~!#$%^&*+={}|/?'";
    this.CREATE_HIRING_EVENT_STORE_NUMBER_TOOLTIP = "Store # (MET-Home Store #, Not District Code)";
    this.removeQSFromURL = false;
    
    this.calledFromSearchPage = false;
    this.cacheRequisitionDetailsFlg = false;
    this.cache_getRequisitionDetails ={};
    this.cacheCurrentAPpage = 0;
    this.cacheCurrentASpage = 0;
    this.cache_getRetailStaffingViewApplicantPool = {};
    this.cache_getRetailStaffingViewAssociatePool = {};
    this.applicantAttachedtoREQ = false;
    this.attachedPersonType = "";
    this.cache_applicantAttachedInfo = {};
    this.currentQProw =0;
    this.qpFromApplicants = false;
    this.qpFromAssociates = false;

    //Interview Availability
    this.INTV_COMPL_RADIO = 'isIntCmpltd';
    this.AVAIL_MATCH_RADIO = 'doesAvailabilityMatch';
}

/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: constants.js
 * Application: Staffing Administration
 */

/**
 * Method name:Constants
 * Constants have static Messages,Service URl's etc.
 * Constants can be accessed throughout the application
 * using the object
 */

/*Create a object for constants*/
var CONSTANTS = new constants();
function constants()
{
    /**
     * COMMON MESSAGES FOR SERVICES
     */
    this.STATUS_SUCCESS = "SUCCESS";
    this.STATUS_FAILURE = "FAILURE";
    this.POST = "POST";
    this.GET = "GET";
    this.APPLICATION_JSON = "application/json";
    this.APPLICATION_XML = "application/xml";
    this.DATA_TYPE_JSON = "json";
    this.DATA_TYPE_XML = "xml";
    this.AUTHENTICATION_FAILED = "Authentication Failed, you are not authorized to use this application";
    this.HEADER_MESSAGE = "<div class=cols-xs-12 cols-xs-offset-1>Welcome to your store calendars. The calendars page allows you to " +
    "select a store calendar and define availability that the Retail Staffing</div>" +
    "<div class=cols-xs-12 cols-xs-offset-1>Center will use to schedule interviews for your active requisitions. To drill into a " +
    "calendar and view/edit availability, click the calendar name.</div>"+
    "<div class=cols-xs-12 cols-xs-offset-1>To rename/delete a calendar, click on the row. If you have questions or need help, please " +
    "call 1-866-myTHDHR (1-866-698-4347).</div>";
    this.CALENDAR_HEADER_MESSAGE ="<div class=cols-xs-12 cols-xs-offset-1>Welcome to your calendar detail page. The calendar detail" +
    " page allows you to add/edit/delete availability that the Retail Staffing Center will</div>"+
    "<div class=cols-xs-12 cols-xs-offset-1>use to schedule interviews for your active requisitions.  To drill into a day and " +
    "add/edit/delete availability, click the day.  If you have questions or</div>"+
    "<div class=cols-xs-12 cols-xs-offset-1> need help, please call 1-866-myTHDHR (1-866-698-4347).</div>";
    this.HIRINGHEADER_MESSAGE="<div class=cols-xs-12 cols-xs-offset-1>Welcome to your hiring"+
         "event calendars. The hiring events page allows you to select a hiring"+
         "event and define availability that the Retail</div>"+
      "<div class=cols-xs-12 cols-xs-offset-1>Staffing Center will "+
         "use to schedule interviews for your hiring event. To drill into a"+
         "hiring event and view/edit availability, click the hiring event</div>"+
      "<div class=cols-xs-12 cols-xs-offset-1>name. To edit/delete a"+
         "hiring event, click on the row. If you have questions or need help,"+
         "please call 1-866-myTHDHR (1-866-698-4347).</div>";
    this.HIRING_CALENDAR_MESSAGE="<div class=cols-xs-12 cols-xs-offset-1>Welcome to your hiring event detail page."+
   "The hiring event detail page allows you to add/edit/delete availability that the Retail Staffing Center </div>"+
      "<div class=cols-xs-12 cols-xs-offset-1>will use to schedule interviews for your active requisitions."+
        "To drill into a day and add/edit/delete availability, click the day.  If you have </div>"+
      "<div class=cols-xs-12 cols-xs-offset-1>questions or need help,"+
       "please call 1-866-myTHDHR (1-866-698-4347).</div>";

    /**
     * Date and Time calculation
     */
    this.MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
    this.MILLISECONDS_PER_MINUTE = 1000 * 60;
    this.MILLISECONDS_PER_HOUR = 1000 * 60 * 60;
    /**
     * Common Parameters
     */
    this.FINISHEDDATE="";
    this.INITIALDATE="";
    this.SELECTEDDATE="";
    this.SHOWTIME=[];
    this.CALENDARID="";
    this.REQUISITIONDATA=[];
    this.SERVICEERROR="";

    /**
     * Common service parameters
     */
    this.NAME="name";
    this.STORENUMBER="storeNumber";
    this.VERSION="version";
    this.TYPE="type";
    /**
     * ToolTip Messages
     */
    this.RECURRING_WEEKS_TOOLTIP = "Select the number of weeks you want the" +
            "\n availability for this time slot to repeat.";
    this.CONCURRENT_INTERVIEWS_TOOLTIP = "When you have more than one Interviewer " +
            "\navailable,you can increase the number in"+
            "\nthis field to show the number of interviews " +
            "\nyou want to hold during each thirty minute"+"\n" +
                    " time slot.";
    this.HIRING_EVENT_DETAIL_DAY_TOOLTIP="Availability cannot be added/deleted " +
            "\nbecause this is not a valid day for the Hiring" +
            "\n Event.";
    this.CALENDAR_TOOLTIP="Click to view/edit the availability details for\n";
    this.CALENDAR_NOTE_TOOLTIP=" Note: 1 Slot is equal to a thirty\n" +
            "minute interview.";
    this.ADD_BLOCK_TOOLTIP = "Click here to add a block of time. For " +
            "\nexample 8am-10am or 8m-5pm.";
    this.DELETE_CHECK_ENABLE_TOOLTIP = "Select to Delete Time Slot";
    this.DELETE_CHECK_DISABLE_TOOLTIP = "Only Available Time Slots Can Be Deleted";
    this.PRINT_PACKETS_TOOLTIP = "Print Packets are only for Scheduled Time " +
            "\nSlots";
    this.SUBMIT_DAY_VIEW = "Please select either Add, Delete, or Print" +
            "\n Packet Mode before trying to Submit";
    this.PRINT_PACKETS_ENABLED_TOOLTIP = "Select to Print Packet for Candidate";
    this.ADD_AVAILABILITY_TOOLTIP = "Availability cannot be added for today.";
    this.DEL_AVAILABILITY_TOOLTIP = "Availability cannot be deleted from today.";
    this.ADD_AVAILABILITY_HIRING_TOOLTIP = "Availability cannot be added to selected Day.";
    this.DEL_AVAILABILITY_HIRING_TOOLTIP = "Availability cannot be deleted from selected Day.";
    this.CALENDARTAB_TOOLTIP = "Calendars Summary Page";
    this.HIRINGTAB_TOOLTIP="Hiring Event Summary Page";
    this.CALENDAR_SUMMARY_TOOLTIP="Click to Return to Calendar Summary Tab.";
    this.HIRING_SUMMARY_TOOLTIP="Click to Return to Hiring Event Summary Tab.";
    this.REQUISITION_DEPT="Select a Department from the Drop Down" +
            " \nand Click to filter by Department.";
    this.REQUISITION_JOBTITLE="Select a Job Title from the Drop Down and " +
            "\nClick to filter by Job Title.";
    this.REQUISITION_CALENDAR="Select a Calendar from the Drop Down and " +
            "\nClick to filter by Calendar.";
    this.NOACTIVE_REQUISITION="No active requisitions for your location";
    this.CREATE_HIRING_EVENT_STORE_NUMBER_TOOLTIP = "Store # (MET-Home Store #, Not District Code)";
    this.EDIT_HIRING_EVENT_ERROR = "Edit Hiring Event Error";
    this.MET_INTERVIEW_TOOLTIP="Enter the location where the interview will " +
            "\nbe conducted.";
    this.ROSTER_DISABLE_TOOLTIP="There is not any Roster Data Available";
    /**
     * Common Warning Messages
     */
    this.ValidMessage="Please enter a valid four digit Store/Location";
    this.CheckAddAvailability = "No Availability has been selected.";
    this.CheckDeleteAvailability ="No Available Time Slots have been selected for Deletion.";
    this.CheckPrintPacketAvailability = "No Candidates have been selected.";
    this.AttributeType="xml";
    this.CheckDateRange = "Selecting a Date Range in the Past is not allowed.";
    this.CheckPreviousDateRange="Selecting a Date Range more than 15 days in the Past is not allowed.";
    this.CheckDayRange="Selecting a Day in the Past is not allowed.";
    this.CheckEqualTime="Begin Time and End Time cannot be the same.";
    this.CheckHiringDateRange = "Selecting a Day more than 15 days in the Past is not allowed.";    
    this.CheckTimeRange="End Time must be greater than Begin Time.";
    this.SlotExceed = "<div class=cols-xs-12 cols-xs-offset-1>Due to Application Constraints, no more than</div>" +
            "<div class=cols-xs-12 cols-xs-offset-1>680 Slots can be added at one time.</div>"+
            "<div class=cols-xs-12 cols-xs-offset-1 style=margin-top:4%;></div>"+
            "<div class=cols-xs-12 cols-xs-offset-1> Please deselect Time Slots, Reduce Recurring </div>" +
            "<div class=cols-xs-12 cols-xs-offset-1>Weeks, or Reduce Concurrent Interviews.</div>";

    this.DeleteValidate ="<div class=cols-xs-12 cols-xs-offset-1>Delete Selected Availability Slots.</div>" +
            "<div class=cols-xs-12 cols-xs-offset-1 text-center>Are You Sure?</div>";
    this.printRosterUserMessage="<div class=cols-xs-12 cols-xs-offset-1>You have selected to create a PDF of the Daily Roster.</div>" +
            "<div class=cols-xs-12 cols-xs-offset-1 text-center> Are You Sure?</div>";
    this.ExportRosterUserMessage="<div class=cols-xs-12 cols-xs-offset-1>You have selected to export the Daily Roster to an Excel File.</div>" +
            "\<div class=cols-xs-12 cols-xs-offset-1 text-center> Are You Sure?</div>";
    this.printErrorMessage="There has been an issue opening the window for the PDF Day View.";
    this.RequisitionFaultMessage="Error getting Calendar Summary Data. Service Failed!";
    this.MetInputValidation="Please enter a 4 digit location number";
    /**
     * Service URL's
     */
    this.UserProfile = "/RetailStaffing/service/UserProfileService/getUserProfileDetails";
    this.ValidStoreService = "/StaffingForms/service/StoreService/validStore";
    this.CalenderTaskService = "/RetailStaffing/service/InterviewService/requisitionCalendars";
    this.CalendarSummaryService = "/StaffingForms/service/CalendarService/calendarSummary";
    this.CalendarDetailService ="/StaffingForms/service/CalendarService/calendarDetails";
    this.AddAvailabilityService = "/StaffingForms/service/CalendarService/addAvailability";
    this.RemoveAvailabilityService = "/StaffingForms/service/CalendarService/removeAvailability";
    this.requisitionTaskService ="/StaffingForms/service/RequisitionService/activeRequisitionsForStore";
    this.hiringEventTaskService="/RetailStaffing/service/InterviewService/requisitionHiringEvents";
    this.createCalendarURL="/RetailStaffing/service/InterviewService/createRequisitionCalendar";
    this.renameDeleteURL="/StaffingForms/service/CalendarService/updateCalendar";
    this.CalendarHiringService = "/RetailStaffing/service/InterviewService/requisitionHiringEvents";
    this.loadStateURL ="/RetailStaffing/service/HiringEventService/loadStateData";
    this.PrintRosterService = "/StaffingForms/service/PrintDailyInterviewRoster/getRosterPDF";
    this.ExportRosterExcelService = "/StaffingForms/service/GenerateExcelService/getExcelView";
    this.addHiringEventStoresURL="/StaffingForms/service/StoreService/getStoreDetails";
    this.getHiringEventMgrDataURL="/RetailStaffing/service/HiringEventService/getHiringEventMgrData";
    this.checkDuplicateHiringEventNameURL="/StaffingForms/service/HiringEventService/checkDuplicateHiringEventName";
    this.checkDuplicateHiringEventNameForeditURL="/StaffingForms/service/HiringEventService/checkDuplicateHiringEventName";
    this.createHiringEventURL="/StaffingForms/service/HiringEventService/createHiringEvent";
    this.deleteHiringEventURL="/StaffingForms/service/CalendarService/updateCalendar";
    this.editHiringEventDetailURL="/RetailStaffing/service/HiringEventService/getHiringEventDetailsForEdit";
    this.deleteHiringEventParticipatingStoreURL="/StaffingForms/service/HiringEventService/deleteHiringEventParticipatingStore";
    this.updateHiringEventURL="/StaffingForms/service/HiringEventService/updateHiringEvent";
    this.calendarPrintService = "/StaffingForms/service/PrintDayViewService/getPdfView";
    this.printPacketService = "/StaffingForms/service/HiringEventPrintService/getMergedPDF";
    this.Faq_Non_Prod_Url = "https://myapron-qa.homedepot.com/en-us/myApron/hr/HRKB/HRServices/Retail_Staffing/Tabbed/RSC_Resources/Interview_Availability/Interview_Availability_FAQ.htm";
    this.Faq_Prod_Url = "https://myapron.homedepot.com/en-us/myApron/hr/HRKB/HRServices/Retail_Staffing/Tabbed/RSC_Resources/Interview_Availability/Interview_Availability_FAQ.htm";
    this.NO = "NO";
}

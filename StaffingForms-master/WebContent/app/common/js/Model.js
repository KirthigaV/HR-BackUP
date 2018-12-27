/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: Model.js
 * Application: Staffing Administration
 *
 */

/**
 * Method name:Model
 * Constants have static list and data's.
 * Model data can be accessed throughout the application
 * using the model object.
 * Method has the error handling objects to validate the
 * error code and it returns the error description.
 */

/*Create a model object*/
var MODEL = new model();
//Model have constants data's and variables.
function model(){
   /**
    * ArrayList hold the key and pair values
    */
   this.ConcurrentInterviewCollection = [{
      name : "1",
      value : 1
   }, {
      name : "2",
      value : 2
   }, {
      name : "3",
      value : 3
   }, {
      name : "4",
      value : 4
   }, {
      name : "5",
      value : 5
   }, {
      name : "6",
      value : 6
   }, {
      name : "7",
      value : 7
   }, {
      name : "8",
      value : 8
   }, {
      name : "9",
      value : 9
   }, {
      name : "10",
      value : 10
   }, {
      name : "11",
      value : 11
   }, {
      name : "12",
      value : 12
   }, {
      name : "13",
      value : 13
   }, {
      name : "14",
      value : 14
   }, {
      name : "15",
      value : 15
   }, {
      name : "16",
      value : 16
   }, {
      name : "17",
      value : 17
   }, {
      name : "18",
      value : 18
   }, {
      name : "19",
      value : 19
   }, {
      name : "20",
      value : 20
   } ];
   /**
    * ArrayList hold the key and pair values
    */
   this.RecurringWeeksCollection = [{
      name : "1",
      value : 1
   }, {
      name : "2",
      value : 2
   }, {
      name : "3",
      value : 3
   }, {
      name : "4",
      value : 4
   }, {
      name : "5",
      value : 5
   }, {
      name : "6",
      value : 6
   }, {
      name : "7",
      value : 7
   }, {
      name : "8",
      value : 8
   }, {
      name : "9",
      value : 9
   }, {
      name : "10",
      value : 10
   }];
   /**
    * ArrayList hold the key and pair values of time
    */
   this.TimeCollection=[
       {name:"05:00 am", value:"05:00:00.0"},
        {name:"05:30 am", value:"05:30:00.0"},
        {name:"06:00 am", value:"06:00:00.0"},
        {name:"06:30 am", value:"06:30:00.0"},
        {name:"07:00 am", value:"07:00:00.0"},
        {name:"07:30 am", value:"07:30:00.0"},
        {name:"08:00 am", value:"08:00:00.0"},
        {name:"08:30 am", value:"08:30:00.0"},
        {name:"09:00 am", value:"09:00:00.0"},
        {name:"09:30 am", value:"09:30:00.0"},
       {name:"10:00 am", value:"10:00:00.0"},
        {name:"10:30 am", value:"10:30:00.0"},
       {name:"11:00 am", value:"11:00:00.0"},
        {name:"11:30 am", value:"11:30:00.0"},
       {name:"12:00 pm", value:"12:00:00.0"},
        {name:"12:30 pm", value:"12:30:00.0"},
       {name:"01:00 pm", value:"13:00:00.0"},
        {name:"01:30 pm", value:"13:30:00.0"},
       {name:"02:00 pm", value:"14:00:00.0"},
        {name:"02:30 pm", value:"14:30:00.0"},
       {name:"03:00 pm", value:"15:00:00.0"},
        {name:"03:30 pm", value:"15:30:00.0"},
       {name:"04:00 pm", value:"16:00:00.0"},
        {name:"04:30 pm", value:"16:30:00.0"},
       {name:"05:00 pm", value:"17:00:00.0"},
        {name:"05:30 pm", value:"17:30:00.0"},
       {name:"06:00 pm", value:"18:00:00.0"},
        {name:"06:30 pm", value:"18:30:00.0"},
       {name:"07:00 pm", value:"19:00:00.0"},
        {name:"07:30 pm", value:"19:30:00.0"},
       {name:"08:00 pm", value:"20:00:00.0"},
        {name:"08:30 pm", value:"20:30:00.0"},
       {name:"09:00 pm", value:"21:00:00.0"},
        {name:"09:30 pm", value:"21:30:00.0"},
        {name:"10:00 pm", value:"22:00:00.0"}
   ];   
 //Application Objects
   this.APPLICATION_OBJ_STORE="OBJ-0001";
   this.APPLICATION_OBJ_CALENDAR="OBJ-0002";
   this.APPLICATION_OBJ_INTERVIEW="OBJ-0003";
   this.APPLICATION_OBJ_HIRING_EVENT="OBJ-0004";
   this.APPLICATION_OBJ_ERRORS= ([{objectCode:(this.APPLICATION_OBJ_STORE), errorDesc:("Store Service")},
                                  {objectCode:(this.APPLICATION_OBJ_CALENDAR), errorDesc:("Calendar Service")},
                                  {objectCode:(this.APPLICATION_OBJ_INTERVIEW), errorDesc:("Interview Service")},
                                  {objectCode:(this.APPLICATION_OBJ_INTERVIEW), errorDesc:("Hiring Event Service")}]);
 //Error Types
    //ERR-0001 will contain a componentIdentifier of type INPUT_ERROR
    this.ERROR_INPUT_VALIDATION="ERR-0001";
    //ERR-0002 will contain a componentIdentifier of type APPLICATION_OBJECT_X
    this.ERROR_DATABASE_EXCEPTION="ERR-0002";
    //ERR-0003 will contain a componentIdentifier of type Duplicate Exception
    this.ERROR_DUPLICATE_HIRING_EVENT_NAME_EXCEPTION="ERR-0003";
    //Input Errors
    this.INPUT_ERRORS=([{errorCode:String("INP-0001"), errorDesc:String("Invalid Version Number")},
                       {errorCode:String("INP-0002"), errorDesc:String("Invalid Store Number")},
                       {errorCode:String("INP-0003"), errorDesc:String("Invalid Calendar Id")},
                       {errorCode:String("INP-0004"), errorDesc:String("Invalid Begin Date")},
                       {errorCode:String("INP-0005"), errorDesc:String("Invalid End Date")},
                       {errorCode:String("INP-0006"), errorDesc:String("Invalid Date")},
                       {errorCode:String("INP-0007"), errorDesc:String("Invalid Begin Time")},
                       {errorCode:String("INP-0008"), errorDesc:String("Invalid End Time")},
                       {errorCode:String("INP-0009"), errorDesc:String("Invalid Number of Interviewers")},
                       {errorCode:String("INP-0010"), errorDesc:String("Invalid Number of Recurring Weeks")},
                       {errorCode:String("INP-0011"), errorDesc:String("Invalid Interview Slot")},
                       {errorCode:String("INP-0012"), errorDesc:String("Invalid Calendar Name")},
                       {errorCode:String("INP-0013"), errorDesc:String("Invalid Calendar Type")},
                       {errorCode:String("INP-0112"), errorDesc:String("Invalid XML Input")},
                       {errorCode:String("INP-0119"), errorDesc:String("Invalid Hiring Event ID Input")},
                       {errorCode:String("INP-0121"), errorDesc:String("Hiring Event Name already exists.")}]);
    /*
     * Method to validate the error type from service
     * it will validate with the error code and application objecs.
     * it will return the error description for respective error codes
     * the parameter 
     */
    this.getErrorMessage = function (errorResultList)
    {
        //errorResultList will should only contain 1 entry, but needs to be an ArrayCollection in order to read the XML nodes
        var errorMessage="";
        var index=0;
        //Validate the errorType has the ERR Exception
        if (errorResultList[0].errorType == this.ERROR_DATABASE_EXCEPTION)
        {
            var applicationObjDesc="";
            for (index=0; index<this.APPLICATION_OBJ_ERRORS.length; index++)
            {
                if (errorResultList[0].componentIdentifier == this.APPLICATION_OBJ_ERRORS[index].objectCode)
                {
                    applicationObjDesc=this.APPLICATION_OBJ_ERRORS[index].errorDesc;
                    break;
                }
            }
            errorMessage = "Database Error Type: " + errorResultList[0].errorType + " Application Object: " + applicationObjDesc;
        }
        //Validate the errorType has the ERR Input validation
        else if (errorResultList[0].errorType == this.ERROR_INPUT_VALIDATION)
        {
            var validationDesc="Unknown Validation Error";
            for (index=0; index<this.INPUT_ERRORS.length; index++)
            {
                if (errorResultList[0].componentIdentifier == this.INPUT_ERRORS[index].errorCode)
                {
                    validationDesc=this.INPUT_ERRORS[index].errorDesc;
                    break;
                }
            }
            errorMessage = "Validation Error Type: " + errorResultList[0].errorType + " Validation Error Desc: " + validationDesc;
        }
        //Validate the errorType has the ERR duplicate the exception
        else if (errorResultList[0].errorType == this.ERROR_DUPLICATE_HIRING_EVENT_NAME_EXCEPTION)
        {
            var validationDesc="Unknown Validation Error";
            for (index=0; index<this.INPUT_ERRORS.length; index++)
            {
                if (errorResultList[0].componentIdentifier == this.INPUT_ERRORS[index].errorCode)
                {
                    validationDesc=this.INPUT_ERRORS[index].errorDesc;
                    break;
                }
            }
            errorMessage = "Validation Error Type: " + errorResultList[0].errorType + " Validation Error Desc: " + validationDesc;
        }
        return errorMessage;
    };
   /**
    * variable declarations
    */
   this.InterviewCount=[];
   this.RecurringCount=[];
   this.AddAvailabilitySlots=[];
   this.StoreNumber="";
   this.TotalSlotCount=0;
   this.DeleteSchedule=[];
   this.DeleteSuccessResponse="";
   this.BeforeConvertedTime=[];
   this.AddTimeBlock=false;
   this.AddBlockReponse="";
   this.AddPrintPacket=false;
   this.AddSuccessResponse="";
   this.RequisitionSchedulesCount="";
   this.CalendarType="";
   this.HiringCalendarId="";
   this.HiringSelectedDate="";
   this.ClassName="";
   this.SlotCount=0;
   this.HireEventId="";
   this.RosterType="";
   this.ShowDate="";
   this.HiringShowDate="";
   this.IsMet=false;
   this.IsNavigationclick=false;
   this.IsBackGround=false;
   this.CalendarDayDetailList=[];
   this.HiringCalendarDetailList=[];
   this.RosterDetail=[];
   this.EVENTSTORENUM="";
   this.SelectedStartDate="";
   this.SelectedEndDate="";
   this.SelectHiringStartDate="";
   this.SelectHiringEndDate="";
}
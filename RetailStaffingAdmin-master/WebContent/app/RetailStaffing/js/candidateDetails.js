/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: candidateDetails.js
 * Application: Retail Staffing Admin
 *
 */
function candidateDetails() {
    /*
     * This method is to initialize the events and to make the initail service
     * call so as to fetch the candidate details
     */
    this.initialize = function () {
        var data = {
                applicantId:CONSTANTS.retailStaffingObj.applicantId,
                candRefId:CONSTANTS.retailStaffingObj.candRefId
        };
        $("title").text("RetailStaffing.html");
        $('#ui-datepicker-div').hide();
        $('.modal-backdrop').empty().remove();
        $("#goHome").off("click");
        $("#goHome").show().on("click",function(){
        	// remove the querystrings if any
        	UTILITY.RemoveQS();
            CONSTANTS.candidateDetailsObj.navigateToHomeScreen();
        });
        $(".navCloseBtn").off("click");
        $(".navCloseBtn").on("click",function(){
            window.close();
        });
        $("#messageBar").html("CANDIDATE DETAILS");
        var that = this;
        this.popup = new rsaPopup();
        CONSTANTS.candidateDetailsObj = this;
         var callbackFunction = $.Callbacks('once');
         callbackFunction.add(that.onFetchCandidateDetailsSuccess);
         RSASERVICES.getcandidateDetails(callbackFunction,data);
    };
        /*
         * This method is invoked once the candidate details are fetched
         * successfully
         */
    this.onFetchCandidateDetailsSuccess = function (response) {
        $.unblockUI();
        if(response.Response.status && response.Response.status === "SUCCESS")
        {
            if(response.Response.CandidateDetailList)
                {
        CONSTANTS.candidateDetailsObj.buildCandidateDetails(response);
                }
            if(response.Response.RequisitionDetailList)
                {
        CONSTANTS.candidateDetailsObj.buildrequisitionNumberGrid(response);
                }
            if(response.Response.hasOwnProperty('ITIDetailList')){
        CONSTANTS.candidateDetailsObj.buildInterviewNumberGrid(response);
            }
        }
        else
            {
            if(response.Response.error.errorMsg){
                CONSTANTS.candidateDetailsObj.popup.alert(response.Response.error.errorMsg);
                }
            }
    };
        /* This is used to load the details and display in the html */
    this.buildCandidateDetails = function (response){
        $("#candidateId").html(response.Response.CandidateDetailList.CandidateDetails.candRefNbr);
        $("#AssociateId").html(response.Response.CandidateDetailList.CandidateDetails.aid?response.Response.CandidateDetailList.CandidateDetails.aid:"");
        $("#candidateName").html(response.Response.CandidateDetailList.CandidateDetails.name);
    };
        /*
         * This is the formatter that is used to place the requisition number as
         * a hyper link inside the grid on click of which the user will be
         * navigated to the requisition details screen
         */
    this.reqNbrFormatter = function (row, cell, value) {
        return '<div class="text-center reqDetailsScreen"><a href="#" class="">' + value + '</a></div>';
    };
        /*
         * This is the formatter that is used to format the date that comes from
         * the response and display the formatted date as required
         */
    this.offerDateFormatter = function (row, cell, value) {
        if(value)
        {
            return '' + value.month + '/' + value.day + '/' + value.year + '';
        }
        else
        {
            return "";
        }
    };
            /*
             * This method is used to build the requisition number grid with
             * various columns in the grid like requisition number in the form
             * of the hyper link so as to navigate to the requisition details
             * page the requisition active column so as to represent he
             * requisition active status and the candidate active column to
             * notify the candidate status the store number column to specify
             * the store number the department column so as to display the
             * department and the job column to display the job and the job
             * title column so as to display the title of the job and the
             * interview status column so as to display the interview status and
             * the the offered column so as to identify if the offer has been
             * offered or not the offer date and offer status so as to display
             * the offered date and the status of the offer and the decline code
             * an the drug test column so as to display the declination code and
             * the result of the drug test respectively
             */
    this.buildrequisitionNumberGrid = function (response){
        var gridData = response.Response;
        $(".gridContainer").empty();
        $(".gridContainer").append("<div id='reqHistory' style='width:99.8%;height:210px;'></div>");
        $(".gridContainer2 #reqHistory").css({"min-width":"1170px"});
         var grid;
          var columns = [
            {id: "reqNbr", name: "Req #", width: 90, field: "reqNbr", formatter: CONSTANTS.candidateDetailsObj.reqNbrFormatter},
            {id: "active", name: "Req Active", width: 90, field: "active"},
            {id: "canStatus", name: "Candidate Active", width: 90, field: "canStatus"},
            {id: "store", name: "Store", width: 90, field: "store"},
            {id: "dept", name: "Dept", width: 90, field: "dept"},
            {id: "job", name: "Job", width: 90, field: "job"},
            {id: "jobTtl", name: "Job Title", width: 90, field: "jobTtl"},
            {id: "intStatus", name: "Interview Status", width: 90, field: "intStatus"},
            {id: "offerMade", name: "Offered", width: 90, field: "offerMade"},
            {id: "offerDate", name: "Offered Date", width: 90, field: "offerDate", formatter: CONSTANTS.candidateDetailsObj.offerDateFormatter},
            {id: "offerStat", name: "Offer Status", width: 90, field: "offerStat"},
            {id: "decCD", name: "Decline Code", width: 90, field: "decCD"},
            {id: "drugTest", name: "Drug Test", width: 90, field: "drugTest"}
          ];
          var options = {
            enableCellNavigation: false,
            enableColumnReorder: true,
            asyncEditorLoading: true,
            syncColumnCellResize: true,
            forceFitColumns: true,rowHeight: 30
          };
          var data =[];
          var tempData = gridData.RequisitionDetailList.RequisitionDetail?gridData.RequisitionDetailList.RequisitionDetail:[];
          if(tempData.constructor !== Array)
              {
              data.push(tempData);
              }
          else
              {
              data=tempData;
              }
          if(data.length < 7){
                 var emptyData = [];
                 for(var i=0;i<6;i++)
                      {
                      emptyData[i] =  data[i];
                      }
              data = emptyData;
              }
          grid = new Slick.Grid("#reqHistory", data, columns, options);
          CONSTANTS.candidateDetailsObj.registerFutureEvents();
    };
        /*
         * This method is used for the purpose of registering the events for the
         * elements that are added in the dom dynamically
         */
    this.registerFutureEvents = function () {
    	$(".reqDetailsScreen").off("click");
        $(".reqDetailsScreen").on("click",function(e){
            CONSTANTS.candidateDetailsObj.navigateToReqScreenDetails(e);
            });
        $(".phoneScreenDetailsScreen").off("click");
        $(".phoneScreenDetailsScreen").on("click",function(e){
            CONSTANTS.candidateDetailsObj.navigateToPhoneScreenDetails(e);
        });
    };
        /*
         * This is the formatter that is being used to insert the phone screen
         * number as a hyper link in the grid so as to navigate to the phone
         * screen details screen
         */
    this.phoneScreenNumFormatter = function (row, cell, value) {
        return '<div class="text-center phoneScreenDetailsScreen"><a href="#" class="">' + value + '</a></div>';
    };
            /*
             * This method is used to build the interview number grid containing
             * various columns like the phone screen number column containing
             * the phone screen number in the form of the hyper link so as to
             * navigate the user to the phone screen details screen on the click
             * of the hyper link the requisition number column in the form of
             * the hyper link so as to navigate the user to the requisition
             * details screen on the click of the requisition number the store
             * column and the department column displaying the store number and
             * the department number respectively and the job column and the
             * screen type column the the phone screen description column
             * containing the phone screen description and the interview status
             * description column containing the interview status description
             * and the interview date displaying the date of interview
             */
    this.buildInterviewNumberGrid = function (response){
        var gridData = response.Response;
        $(".gridContainer2").empty();
        $(".gridContainer2").append("<div id='phnIntHistory' style='width:99.8%;height:210px;'></div>");
        $(".gridContainer2 #phnIntHistory").css({"min-width":"1040px"});
         var grid;
         var columns = [
                        {id: "itiNbr", name: "Phone Screen Number", width: 90, field: "itiNbr", formatter: CONSTANTS.candidateDetailsObj.phoneScreenNumFormatter},
                        {id: "reqNbr", name: "Req #", field: "reqNbr", width: 90, formatter: CONSTANTS.candidateDetailsObj.reqNbrFormatter},
                        {id: "cndStrNbr", name: "Store", width: 90, field: "cndStrNbr"},
                        {id: "cndDeptNbr", name: "Dept", width: 90, field: "cndDeptNbr"},
                        {id: "job", name: "Job", width: 90, field: "job"},
                        {id: "scrTyp", name: "Screen Type", width: 90, field: "scrTyp"},
                        {id: "phoneScreenStatusDesc", name: "Phone Screen Status", width: 200, field: "phoneScreenStatusDesc"},
                        {id: "interviewStatusDesc", name: "Interview Status", width: 200, field: "interviewStatusDesc"},
                        {id: "InterviewLocDtls", name: "Interview Date", width: 100, field: "InterviewLocDtls",formatter: CONSTANTS.candidateDetailsObj.interviewDateFormatter}
                      ];
          var options = {
            enableCellNavigation: false,
            enableColumnReorder: true,
            asyncEditorLoading: true,
            syncColumnCellResize: true,
            forceFitColumns: true,rowHeight: 30
          };
          var data =[];
          var tempData = gridData.ITIDetailList.PhoneScreenIntrwDetail?gridData.ITIDetailList.PhoneScreenIntrwDetail:[];
          if(tempData.constructor !== Array)
          {
          data.push(tempData);
          }
          else if(tempData.constructor === Array)
              {
              data=tempData;
              }
          if(data.length < 7){
                 var emptyData = [];
                 for(var i=0;i<6;i++)
                      {
                      emptyData[i] =  data[i];
                      }
              data = emptyData;
              }
          CONSTANTS.LoadITIDGDltls = data;
          grid = new Slick.Grid("#phnIntHistory", data, columns, options);
          CONSTANTS.candidateDetailsObj.registerFutureEvents();
    };
        /*
         * This method is used to format the interview date received from the
         * service and to display the date as in MM/DD/YYYY format as required
         * by the user
         */
    this.interviewDateFormatter = function (row, cell, value) {
        if(value && value.interviewDate) {
            var month = value.interviewDate.month ? value.interviewDate.month :"";
            var day = value.interviewDate.day ? value.interviewDate.day :"";
            var year = value.interviewDate.year ? value.interviewDate.year :"";
            return month+"/"+day+"/"+year;
        };
        return "";
    };
        /*
         * This method is used for the purpose of navigation the user to the
         * home screen on the click of the home button or the cancel button in
         * this screen
         */
    this.navigateToHomeScreen = function () {
        $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.candidateDetailsObj.setContent);
    };
        /*
         * This method is invoked on the click of the req number hyper link on
         * which the user will be navigated to the requisition details screen
         */
    this.navigateToReqScreenDetails = function (e) {
        CONSTANTS.retailStaffingObj.reqNumber = $(e.currentTarget).text();
        CONSTANTS.calledFromSearchPage = true;
        UTILITY.clearCache();
        $.get('app/RSARequisitionDetails/view/requisitionDetails.html', CONSTANTS.candidateDetailsObj.setContent);
    };
        /*
         * This method is invoked on the click of the phone screen number in the
         * grid so as to navigate to the phone screeen details screen
         */
    this.navigateToPhoneScreenDetails = function (e) {
        CONSTANTS.LoadITIDGDltls = [{itiNbr:$(e.currentTarget).text()}];
        $.get('app/RSAPhoneScreenDetails/view/phoneScreenDetails.html', CONSTANTS.candidateDetailsObj.setContent);
    };
        /* This method is used to replace the dom and load the page as required */
    this.setContent = function (data) {
        $("#divLandingView").html(data);
    };
        /* This method is used to replace the dom and load the page as required */
    this.setFullContent = function (data) {
        $("html").html(data);
    };
};
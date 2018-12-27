/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: interviewScreenDetails.js
 * Application: RetailStaffingAdmin
 *
 * Used to display interview details
 */
function interviewScreenDetails() {
    var currentObject = this;
    /*
     * This method is used to call service on screen load
     * Screen name and number are assigned as it is
     * got from the home screen.
     * Phone Screen Details service has been invoked.
     */
    this.intialize = function() {
        CONSTANTS.interviewScrnObj = this;
        $("#messageBar").html("INTERVIEW SCREEN DETAILS");
        $('#ui-datepicker-div').hide();
        $('.modal-backdrop').empty().remove();
        this.popup = new rsaPopup();
        CONSTANTS.ConfirmationNo = CONSTANTS.LoadINTVIEWSRCHRSLTS[0].itiNbr;
        CONSTANTS.phnscrnSaveCheck = false;
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.loadAllInterviewStatuses);
        RSASERVICES.getAllInterviewStatuses(callbackFunction, CONSTANTS.LoadINTVIEWSRCHRSLTS[0].itiNbr, true, "Please wait...");
    };
    this.loadAllInterviewStatuses = function(json) {
        $.unblockUI();
        // to remove the already loaded Statuses Details
        CONSTANTS.phoneScrnStatusDtls = [];
        CONSTANTS.getIntMatStatusDtls = [];
        CONSTANTS.getInterviewStatusDtls = [];
        CONSTANTS.interviewStatusTimestamp = [];
        CONSTANTS.phnScreenDtls = [];

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            CONSTANTS.interviewScrnObj.intrvwScreenDetails(json);
        }
        // Handling fo the error for the error
        else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewScrnObj.popup.alert(json.Response.error.errorMsg);
        }
        var callbackFunction = $.Callbacks('once');
        if (CONSTANTS.LoadINTVIEWSRCHRSLTS.length > 0) {
            callbackFunction.add(CONSTANTS.interviewScrnObj.getIntrvwScreenDetails.bind(this));
            RSASERVICES.getPhnScreenDetails(callbackFunction, CONSTANTS.LoadINTVIEWSRCHRSLTS[0].itiNbr, true, "Please wait...");
        } else {
            if ((CONSTANTS.CallType) && (CONSTANTS.CallType === "INTSCHED")) {
                callbackFunction.add(CONSTANTS.interviewScrnObj.getIntrvwScreenDetails.bind(this));
                RSASERVICES.getPhnScreenDetails(callbackFunction, CONSTANTS.ConfirmationNo.toString(), true, "Please wait...");
            }
        }
    };
    /*
     * Once phone screen details are retrieved successfully,
     * Interview Screen Details service is invoked.
     * This method is used to get interview screen details
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.getIntrvwScreenDetails = function(json) {
        $.unblockUI();

        // to remove the already loaded Statuses Details
        CONSTANTS.phoneScrnStatusDtls = [];
        CONSTANTS.getIntMatStatusDtls = [];
        CONSTANTS.getInterviewStatusDtls = [];
        CONSTANTS.interviewStatusTimestamp = [];
        CONSTANTS.phnScreenDtls = [];

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            CONSTANTS.interviewScrnObj.intrvwScreenDetails(json);
        }
        // Handling fo the error for the error
        else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewScrnObj.popup.alert(json.Response.error.errorMsg);
        }

        var callbackFunction = $.Callbacks('once');
        if ((CONSTANTS.CallType) && (CONSTANTS.CallType === "INTSCHED")) {
            callbackFunction.add(CONSTANTS.interviewScrnObj.loadIntrvwScreenDetails.bind(this));
            RSASERVICES.getIntervwScreenDetails(CONSTANTS.ConfirmationNo.toString(), callbackFunction, true, "Please wait...");
        } else {
            callbackFunction.add(CONSTANTS.interviewScrnObj.loadIntrvwScreenDetails.bind(this));
            RSASERVICES.getIntervwScreenDetails(CONSTANTS.LoadINTVIEWSRCHRSLTS[0].itiNbr, callbackFunction, true, "Please wait...");
        }
    };

    /*
     * This method is used to iterate the interview screen details response
     *
     * @param response
     * @return N/A
     */
    this.intrvwScreenDetails = function(json) {
        // check whether status is Success or Error
        // Start ITIDETAILS
        if (json.Response.hasOwnProperty("ITIDetailList")) {
            CONSTANTS.interviewScrnObj.phnDtlsITIDetailList(json);
        }
        // End ITIDEtails

        // check for the StatusList tag in the response
        if (json.Response.hasOwnProperty("StatusList")) {
            // Interview Statuses
            if (json.Response.StatusList.hasOwnProperty("interviewStats")) {
                CONSTANTS.interviewScrnObj.phnDtlsInterviewStats(json);
            }

            // Interview Materials Statuses
            if (json.Response.StatusList.hasOwnProperty("materialsStats")) {
                CONSTANTS.interviewScrnObj.phnDtlsMaterialsStats(json);
            }
        }

        // RSA4.0 CTI_ScreenPopup
        if (CONSTANTS.CallType === "INTSCHED" && CONSTANTS.isEnableIntrwScrnWarnPop) {
            CONSTANTS.interviewScrnObj.checkIntrvScheduled();
        }
    };

    /*
     * This method is used to load phone screen
     * interview details
     *
     * @param response
     * @return N/A
     */
    this.phnDtlsITIDetailList = function(json) {
        var resultList = [];
        var timeVO = {}; // TimeStampVO
        var phnScrnDtl = {}; // PhnScrDetVO
        // check whether PhoneScreenIntrwDetail is an array
        // collection
        if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail)) {
            resultList = json.Response.ITIDetailList.PhoneScreenIntrwDetail;
        }
        // PhoneScreenIntrwDetail is not an array collection
        else {
            if (json.Response.ITIDetailList.hasOwnProperty("PhoneScreenIntrwDetail")) {
                resultList = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail ];
            }
        }

        // check whether the result list is having any value object
        // or not
        if (resultList && resultList.length > 0) {
            // iterating the result list to get each item and set in
            // to model locator
            for ( var n = 0; n < resultList.length; n++) {
                phnScrnDtl = {};
                phnScrnDtl.phoneScreenStatusCode = resultList[n].phoneScreenStatusCode;
                phnScrnDtl.cndtNbr = resultList[n].cndtNbr;
                phnScrnDtl.aid = resultList[n].aid;
                phnScrnDtl.reqNbr = resultList[n].reqNbr;
                phnScrnDtl.ScreenNbr = resultList[n].ScreenNbr;
                phnScrnDtl.canName = resultList[n].name;
                phnScrnDtl.internalExternal = resultList[n].internalExternal;
                phnScrnDtl.canPhn = resultList[n].canPhn;
                phnScrnDtl.strNbr = resultList[n].strNbr;
                phnScrnDtl.dept = resultList[n].dept;
                phnScrnDtl.candTitle = resultList[n].candTitle;
                phnScrnDtl.phnScrnDate = resultList[n].phnScrnDate;
                phnScrnDtl.phnScrnTime = resultList[n].phnScrnTime;
                phnScrnDtl.contactHist = resultList[n].contactHist;
                phnScrnDtl.ynstatus = resultList[n].ynstatus;
                phnScrnDtl.detailResp = resultList[n].detailTxt;
                phnScrnDtl.overAllStatus = resultList[n].overAllStatus;
                phnScrnDtl.hrEventFlg = resultList[n].hrEventFlg;
                phnScrnDtl.ynStatusdesc = resultList[n].ynStatusdesc;
                phnScrnDtl.intrwTyp = resultList[n].intrwTyp;
                phnScrnDtl.phnScreener = resultList[n].phnScreener;
                phnScrnDtl.scrTyp = resultList[n].scrTyp;
                phnScrnDtl.emailAddr = resultList[n].emailAdd;
                phnScrnDtl.interviewStatusCode = resultList[n].interviewStatusCode;
                phnScrnDtl.interviewMaterialStatusCode = resultList[n].interviewMaterialStatusCode;
                // Added by p22o0mn for Defect #3323: New field
                // added to Get the Flag for enough Phone Screens
                // Completed or not for a given requisition
                phnScrnDtl.phoneScreensCompletedFlg = resultList[n].phoneScreensCompletedFlg;
                phnScrnDtl.candRefNbr = resultList[n].candRefNbr;
                phnScrnDtl.phoneScreenDispositionCode = resultList[n].phoneScreenDispositionCode;

                CONSTANTS.phnScreenDtls.push(phnScrnDtl);

                // Interview Status Time Stamp
                if (resultList[n].interviewStatusTime) {
                    timeVO = {};
                    timeVO.month = resultList[n].month;
                    timeVO.day = resultList[n].day;
                    timeVO.year = resultList[n].year;
                    timeVO.hour = resultList[n].hour;
                    timeVO.minute = resultList[n].minute;
                    timeVO.second = resultList[n].second;
                    timeVO.milliSecond = resultList[n].milliSecond;
                    timeVO.tmFrmt = resultList[n].tmFrmt;

                    CONSTANTS.interviewStatusTimestamp.push(timeVO);
                }
            }
        }
    };

    /*
     * This method is used to load interview status details
     *
     * @param response
     * @return N/A
     */
    this.phnDtlsInterviewStats = function(json) {
        var resultList = [];
        var loadInterviewStatus = {}; // InterviewStatusCodeVO
        // check whether interviewStats is an array collection
        if (Array.isArray(json.Response.StatusList.interviewStats)) {
            resultList = json.Response.StatusList.interviewStats[0].status;
        } else if (Array.isArray(json.Response.StatusList.interviewStats.status)) {
            resultList = json.Response.StatusList.interviewStats.status;
        }
        // interviewStats is not an array collection
        else {
            if (json.Response.StatusList.hasOwnProperty("interviewStats")) {
                resultList = [ json.Response.StatusList.interviewStats[0].status ];
            }
        }
        // check whether the result list is having any value
        // object or not
        if (resultList && resultList.length > 0) {
            // iterating the result list to get each item and
            // set in to model locator
            loadInterviewStatus.status = "0";
            loadInterviewStatus.desc = "--Select--";
            CONSTANTS.getInterviewStatusDtls.push(loadInterviewStatus);

            for ( var i = 0; i < resultList.length; i++) {
                loadInterviewStatus = {};
                loadInterviewStatus.status = resultList[i].displayStatusCode;
                loadInterviewStatus.desc = resultList[i].statusDescription;
                CONSTANTS.getInterviewStatusDtls.push(loadInterviewStatus);
            }
            //sort the Interview Status based on the
            //Status description
            CONSTANTS.getInterviewStatusDtls = _.sortBy(CONSTANTS.getInterviewStatusDtls, "desc");

            CONSTANTS.interviewScrnObj.setIntrStatus();
        }
    };

    /*
     * Interview Status combo box is loaded.
     * Sets value for interview status
     *
     * @param N/A
     * @return N/A
     */
    this.setIntrStatus = function() {
        $("#intrstatusSelect").selectBoxIt();
        $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
        CONSTANTS.interviewScrnObj.appendOptionToSelect('intrstatusSelect', 'append', CONSTANTS.getInterviewStatusDtls, 'desc', 'status');

        $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
        for ( var r = 0; r < CONSTANTS.getInterviewStatusDtls.length; r++) {
            for ( var j = 0; j < CONSTANTS.phnScreenDtls.length; j++) {
                if (CONSTANTS.phnScreenDtls[j].interviewStatusCode && CONSTANTS.getInterviewStatusDtls[r].status === CONSTANTS.phnScreenDtls[j].interviewStatusCode) {
                    $('#intrstatusSelect option:eq(' + r + ')').prop('selected', true);
                    $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
                    break;
                }
            }
        }
    };

    /*
     * This method is used to load interview
     * material status details
     *
     * @param response
     * @return N/A
     */
    this.phnDtlsMaterialsStats = function(json) {
        var resultList = [];
        var loadMatStatus = {}; // MaterialStatusCodeVO
        // check whether materialsStats is an array collection
        if (Array.isArray(json.Response.StatusList.materialsStats)) {
            resultList = json.Response.StatusList.materialsStats[0].status;
        } else if (Array.isArray(json.Response.StatusList.materialsStats.status)) {
            resultList = json.Response.StatusList.materialsStats.status;
        }
        // materialsStats is not an array collection
        else {
            if (json.Response.StatusList.hasOwnProperty("materialsStats")) {
                resultList = [ json.Response.StatusList.materialsStats[0].status ];
            }
        }
        // check whether the result list is having any value
        // object or not
        if (resultList && resultList.length > 0) {
            loadMatStatus = {};
            loadMatStatus.status = "0";
            loadMatStatus.desc = "--Select--";
            CONSTANTS.getIntMatStatusDtls.push(loadMatStatus);

            // iterating the result list to get each item and
            // set in to model locator
            for ( var i = 0; i < resultList.length; i++) {
                loadMatStatus = {};
                loadMatStatus.status = resultList[i].displayStatusCode;
                loadMatStatus.desc = resultList[i].statusDescription;
                CONSTANTS.getIntMatStatusDtls.push(loadMatStatus);

            }

            CONSTANTS.getIntMatStatusDtls = _.sortBy(CONSTANTS.getIntMatStatusDtls, "desc");
            // INTERVIEW DETAILS COMBO BOX
            CONSTANTS.interviewScrnObj.setMtrlStatus();
        }
    };

    /*
     * Interview Material Status combo box is loaded.
     * Sets value for interview material status
     *
     * @param N/A
     * @return N/A
     */
    this.setMtrlStatus = function() {
        $("#mtrlstatusSelect").selectBoxIt();
        $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        CONSTANTS.interviewScrnObj.appendOptionToSelect('mtrlstatusSelect', 'append', CONSTANTS.getIntMatStatusDtls, 'desc', 'status');
        $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        $("#mtrlstatusSelect").attr("disabled", "disabled");
        $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        var chkAvailPhnScreenDtls = (CONSTANTS.phnScreenDtls && CONSTANTS.phnScreenDtls.length > 0 && CONSTANTS.phnScreenDtls[0].interviewMaterialStatusCode && CONSTANTS.phnScreenDtls[0].interviewMaterialStatusCode !== "");

        for ( var r = 0; r < CONSTANTS.getIntMatStatusDtls.length; r++) {
            // Added Validation
            if (chkAvailPhnScreenDtls && CONSTANTS.getIntMatStatusDtls[r].status === CONSTANTS.phnScreenDtls[0].interviewMaterialStatusCode) {
                $('#mtrlstatusSelect option:eq(' + r + ')').prop('selected', true);
                $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
                break;
            }
        }
    };

    /*
     * This method is used to check interview schedule
     *
     * @param N/A
     * @return N/A
     */
    this.checkIntrvScheduled = function() {
        var intervwHired_Scheduled = "";
        if ((CONSTANTS.phnScreenReqDtls) && (CONSTANTS.phnScreenReqDtls.length > 0)) {

            if (CONSTANTS.phnScreenReqDtls[0].offerExtCandCnt <= 0) {
                intervwHired_Scheduled = CONSTANTS.WARN_INTRV_SCRN_HIRED_1;
                CONSTANTS.isEnableIntrwScrnWarnPop = false;
                $('#intrWarnModal .intrWarnMsg').empty();
                $('#intrWarnModal .intrWarnMsg').append("<p>" + intervwHired_Scheduled + "</p><p>" + CONSTANTS.WARN_INTRV_SCRN_SCHEDULED_2 + "</p><p>" + CONSTANTS.WARN_INTRV_SCRN_SCHEDULED_3);
                $('#intrWarnModal').modal('show');
                CONSTANTS.interviewScrnObj.promptStaticDraggable('#intrWarnModal');

            } else if (CONSTANTS.phnScreenReqDtls[0].interviewRemainingCandCnt <= 0) {
                intervwHired_Scheduled = CONSTANTS.WARN_INTRV_SCRN_SCHEDULED_1;
                CONSTANTS.isEnableIntrwScrnWarnPop = false;
                $('#intrWarnModal .intrWarnMsg').empty();
                $('#intrWarnModal .intrWarnMsg').append("<p>" + intervwHired_Scheduled + "</p><p>" + CONSTANTS.WARN_INTRV_SCRN_SCHEDULED_2 + "</p><p>" + CONSTANTS.WARN_INTRV_SCRN_SCHEDULED_3);
                $('#intrWarnModal').modal('show');
                CONSTANTS.interviewScrnObj.promptStaticDraggable('#intrWarnModal');
            }
        }
    };
    /*
     * This method is used to load interview screen details
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.loadIntrvwScreenDetails = function(json) {
        $.unblockUI();
        // to remove the already loaded ITI Details
        CONSTANTS.phnScreenDtls = [];
        CONSTANTS.phnScreenIntrwDtls = [];
        CONSTANTS.phnScreenReqDtls = [];
        CONSTANTS.phnScreenReqStoreDtls = [];
        CONSTANTS.minResponseList = [];
        CONSTANTS.intrwDateList = [];
        CONSTANTS.intrwTimeList = [];
        CONSTANTS.phnScrnStaffingList = [];
        CONSTANTS.LoadREQDGDltls = {};
        CONSTANTS.phnScrnTimeList = [];
        CONSTANTS.phnScrnDateList = [];
        CONSTANTS.intrDat = null;
        CONSTANTS.phnScrnDat = null;
        CONSTANTS.phnUsrId = "";
        CONSTANTS.getWeekAvail = "";

        CONSTANTS.getInActPhnScrnDtls = [];
        CONSTANTS.getTotalPhnScrnDtls = [];
        CONSTANTS.getAuthPosCnt = 0;
        CONSTANTS.IntrwSchedDate = "";
        CONSTANTS.IntrwSchedTime = "";
        CONSTANTS.intrwMatScrDate = "";
        CONSTANTS.intrwMatScrTime = "";
        CONSTANTS.intrwPacketDate = "";
        CONSTANTS.intrwPacketTime = "";
        CONSTANTS.intrwSchedDateVO = {};
        CONSTANTS.intrwSchedTimeVO = {};

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            CONSTANTS.interviewScrnObj.intScrnDtlsSuccess(json);
            CONSTANTS.interviewScrnObj.setInActPhnScrnDtls();
            // RSA4.0 CTI_ScreenPopup
            CONSTANTS.interviewScrnObj.setIntervwScreenData(json);
        }
        // checking for the error
        else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewScrnObj.popup.alert(json.Response.error.errorMsg);
        }

        CONSTANTS.interviewScrnObj.intrvwScrnDetails = json;
    };

    /*
     * This method is used to set the Inactive phone screen details
     *
     * @param N/A
     * @return N/A
     */
    this.setInActPhnScrnDtls = function() {
        if (CONSTANTS.interviewScrnObj.intScrnChkPhnScrnDtls()) {
            for ( var k = 0; k < CONSTANTS.getTotalPhnScrnDtls.length; k++) {
                var flag = 0;
                for ( var b = 0; b < CONSTANTS.getRewPhnScrnDynPanelDtls.length; b++) {
                    flag = (CONSTANTS.getRewPhnScrnDynPanelDtls[b].phnScrnNo === CONSTANTS.getTotalPhnScrnDtls[k].itiNbr) ? 1 : 0;
                    break;
                }
                if (flag === 0) {
                    CONSTANTS.getInActPhnScrnDtls.push(CONSTANTS.getTotalPhnScrnDtls[k]);
                }
            }
        }
    };

    /*
     * This method is used to process the success response of Interview Screen
     *
     * @param response
     * @return N/A
     */
    this.intScrnDtlsSuccess = function(json) {
        // check whether status is Success or Error
        // checking for the authorization position count
        if (json.Response.hasOwnProperty("authCnt")) {
            CONSTANTS.getAuthPosCnt = json.Response.authCnt;
        }
        // checking for the CmplITIDeatil tag in the response

        if (json.Response.hasOwnProperty("CmplITIDetail")) {
            CONSTANTS.interviewScrnObj.intScrnCmplITIDetail(json);
        }

        // check for the ITIDetailList tag in the response
        if (json.Response.hasOwnProperty("ITIDetailList")) {
            CONSTANTS.interviewScrnObj.intScrnITIDetaillist(json);
        }

        // //check for the RequisitionDetailList tag in the response
        if (json.Response.hasOwnProperty("RequisitionDetailList")) {
            CONSTANTS.interviewScrnObj.intScrnReqDtlList(json);
        }

        CONSTANTS.interviewScrnObj.intScrnStoreDtlList(json);

        // check for the CalendarList tag in the response
        if (json.Response.hasOwnProperty("CalendarList")) {
            if (json.Response.CalendarList && json.Response.CalendarList.hasOwnProperty("ScheduleTimeDetails")) {
                CONSTANTS.interviewScrnObj.intScrnSchdTimeDtls(json);
            } else {
                CONSTANTS.FirstWeekIntervAvailDatesCollection = [];
                CONSTANTS.SecondWeekInterAvailDatesCollection = [];
            }
        }

        // check for the staffingDetails tag in the response
        if (json.Response.hasOwnProperty("staffingDetails")) {
            CONSTANTS.interviewScrnObj.intScrnStaffingDtls(json);
        }
    };

    /*
     * This method is used to validate the Phone Screen details.
     *
     * @param N/A
     * @return N/A
     */
    this.intScrnChkPhnScrnDtls = function() {
        return (CONSTANTS.getRewPhnScrnDynPanelDtls && CONSTANTS.getRewPhnScrnDynPanelDtls.length > 0 && CONSTANTS.getTotalPhnScrnDtls && CONSTANTS.getTotalPhnScrnDtls.length > 0);
    };

    /*
     * This method is used to process the response of Interview Screen ITI
     * Detail list
     *
     * @param response
     * @return N/A
     */
    this.intScrnITIDetaillist = function(json) {
        var resultList = [];
        // check whether PhoneScreenIntrwDetail is an array
        // collection
        if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail)) {
            resultList = json.Response.ITIDetailList.PhoneScreenIntrwDetail;
        }
        // PhoneScreenIntrwDetail is not an array collection
        else if (json.Response.ITIDetailList.hasOwnProperty("PhoneScreenIntrwDetail")) {
            resultList = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail ];
        }
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("MinimumResponseDtlList")) {
            CONSTANTS.interviewScrnObj.intScrnMinimumResDtlList(json);
        }

        // added for date to
        if (json.Response.ITIDetailList.hasOwnProperty("PhoneScreenIntrwDetail")) {
            CONSTANTS.interviewScrnObj.intScrnIntrwDetail(json);
        }

        CONSTANTS.interviewScrnObj.intScrnInterviewLocDtls(json);
        CONSTANTS.interviewScrnObj.intScrnMaterialStatus(json);
        CONSTANTS.interviewScrnObj.intScrnPhnScreenDtls(resultList);
    };

    /*
     * This method is used to process the response of Interview Screen Schedule
     * time details.
     * Formats data/time of begin and end time
     *
     * @param response
     * @return N/A
     */
    this.intScrnSchdTimeDtls = function(json) {
        var resultIntervDescList = [];
        var interviewSchedVO = {}; // InterviewSchedVO
        if (Array.isArray(json.Response.CalendarList.ScheduleTimeDetails)) {
            resultIntervDescList = json.Response.CalendarList.ScheduleTimeDetails;
        } else {
            resultIntervDescList = [ json.Response.CalendarList.ScheduleTimeDetails ];
        }
        if ((resultIntervDescList) && (resultIntervDescList.length > 0)) {
            CONSTANTS.FirstWeekIntervAvailDatesCollection = [];
            CONSTANTS.SecondWeekInterAvailDatesCollection = [];

            for ( var m = 0; m < resultIntervDescList.length; m++) {
                interviewSchedVO = {};
                var beginDate = new Date();
                var begintime = new Date();
                var concurrent = "";
                //Days of the week
                var days = [ 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday' ];

                beginDate = CONSTANTS.interviewScrnObj.convertStringToDate(resultIntervDescList[m].beginTimestamp);
                begintime = CONSTANTS.interviewScrnObj.convertStringToDateTime(resultIntervDescList[m].beginTimestamp);
                concurrent = resultIntervDescList[m].interviewerAvailabilityCount;

                var dateFormattedBegintime = days[begintime.getDay()] + ' - ' + (("0" + (begintime.getMonth() + 1)).slice(-2)) + '/' + ("0" + begintime.getDate()).slice(-2) + '/' + begintime.getFullYear();

                if (CONSTANTS.interviewScrnObj.checkFirstWeekAvabDate(beginDate, new Date())) {
                    interviewSchedVO = {};
                    interviewSchedVO.date = dateFormattedBegintime;
                    interviewSchedVO.beginTime = resultIntervDescList[m].formattedBeginTime;
                    interviewSchedVO.endTime = resultIntervDescList[m].formattedEndTime;
                    interviewSchedVO.uiTimeStamp_Date = resultIntervDescList[m].formattedBeginDate;// Value
                    interviewSchedVO.uiTimeStamp_Time = resultIntervDescList[m].formatted24HrBeginTime;// Value
                    interviewSchedVO.concurrent = concurrent;
                    interviewSchedVO.storeNo = resultIntervDescList[m].humanResourcesSystemStoreNumber;
                    CONSTANTS.FirstWeekIntervAvailDatesCollection.push(interviewSchedVO);

                }

                if (CONSTANTS.interviewScrnObj.checkSecondWeekAvabDate(beginDate, new Date())) {
                    interviewSchedVO = {};
                    interviewSchedVO.date = dateFormattedBegintime;
                    interviewSchedVO.beginTime = resultIntervDescList[m].formattedBeginTime;
                    interviewSchedVO.endTime = resultIntervDescList[m].formattedEndTime;
                    interviewSchedVO.uiTimeStamp_Date = resultIntervDescList[m].formattedBeginDate;// Value
                    interviewSchedVO.uiTimeStamp_Time = resultIntervDescList[m].formatted24HrBeginTime;// Value
                    interviewSchedVO.concurrent = concurrent;
                    interviewSchedVO.storeNo = resultIntervDescList[m].humanResourcesSystemStoreNumber;
                    CONSTANTS.SecondWeekInterAvailDatesCollection.push(interviewSchedVO);
                }
            }
        }
    };

    /*
     * This method is used to get the total phone screen details.
     *
     * @param response
     * @return N/A
     */
    this.intScrnCmplITIDetail = function(json) {
        var resultListInActPhnScrns = [];
        var inActPhnScrnDtl = null; // GetInActPhnScrnDtls
        // check whether RequisitionDetail is an array collection
        if (Array.isArray(json.Response.CmplITIDetail.PhoneScreenIntrwDetail)) {
            resultListInActPhnScrns = json.Response.CmplITIDetail.PhoneScreenIntrwDetail;
        }
        // RequisitionDetail is not an array collection
        else {
            if (json.Response.CmplITIDetail.hasOwnProperty("PhoneScreenIntrwDetail")) {
                resultListInActPhnScrns = [ json.Response.CmplITIDetail.PhoneScreenIntrwDetail ];
            }
        }

        if (resultListInActPhnScrns && resultListInActPhnScrns.length > 0) {
            // iterating the result list to get each item and set in
            // to model locator
            for ( var n = 0; n < resultListInActPhnScrns.length; n++) {
                inActPhnScrnDtl = {};
                inActPhnScrnDtl.itiNbr = resultListInActPhnScrns[n].itiNbr;
                inActPhnScrnDtl.overAllStatus = resultListInActPhnScrns[n].overAllStatus;
                inActPhnScrnDtl.interviewStatusCode = resultListInActPhnScrns[n].interviewStatusCode;
                inActPhnScrnDtl.interviewMatStatusCode = resultListInActPhnScrns[n].interviewMatStatusCode;
                CONSTANTS.getTotalPhnScrnDtls.push(inActPhnScrnDtl);
            }
        }
    };

    /*
     * This method is used to process the response of Interview Screen Minimum
     * response detail list
     *
     * @param response
     * @return N/A
     */
    this.intScrnMinimumResDtlList = function(json) {
        var resultMinList = [];
        var minResTo = null; // MinRespVO
        if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList)) {
            resultMinList = json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList[0].MinimumResponseDtl;
        } else if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList !== {} && json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList.hasOwnProperty("MinimumResponseDtl")) {
            if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList.MinimumResponseDtl)) {
                resultMinList = json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList.MinimumResponseDtl;
            } else {
                resultMinList = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList.MinimumResponseDtl ];
            }
        }
        if (resultMinList && resultMinList.length > 0) {
            for ( var i = 0; i < resultMinList.length; i++) {
                minResTo = {};
                minResTo.minimumResponse = resultMinList[i].minimumResponse;
                minResTo.seqNbr = resultMinList[i].seqNbr;
                CONSTANTS.minResponseList.push(minResTo);
            }
        }
    };

    /*
     * This method is used to process the response of Interview Screen Staffing
     * details.
     *
     * @param response
     * @return N/A
     */
    this.intScrnStaffingDtls = function(json) {
        var resultStaffList = [];
        var staffDtl = null; // StaffingDetVO
        // check whether staffingDetails is an array collection
        if (Array.isArray(json.Response.staffingDetails)) {
            resultStaffList = json.Response.staffingDetails;
        }
        // staffingDetails is not an array collection
        else {
            if (json.Response.hasOwnProperty("staffingDetails")) {
                resultStaffList = [ json.Response.staffingDetails ];
            }
        }

        // check whether the result list is having any value object
        // or not
        if (resultStaffList && resultStaffList.length > 0) {
            // iterating the result list to get each item and set in
            // to model locator

            staffDtl = {};
            staffDtl.add = resultStaffList[0].add;
            staffDtl.breaks = null;
            staffDtl.city = resultStaffList[0].city;
            staffDtl.daysTmMgrAvble = resultStaffList[0].daysTmMgrAvble;
            staffDtl.desiredExp = resultStaffList[0].desiredExp;
            staffDtl.end = null;
            staffDtl.hrgMgrName = resultStaffList[0].hrgMgrName;
            staffDtl.hrgMgrPhn = resultStaffList[0].hrgMgrPhn;
            staffDtl.hrgMgrTtl = resultStaffList[0].hrgMgrTtl;
            staffDtl.interviewDurtn = resultStaffList[0].interviewDurtn;
            staffDtl.interviewTmSlt = null;
            staffDtl.lastIntrTm = null;
            staffDtl.lunch = null;
            staffDtl.requestNbr = resultStaffList[0].requestNbr;
            staffDtl.requisitionStatus = null;
            staffDtl.sealTempJob = null;
            staffDtl.start = null;
            staffDtl.state = resultStaffList[0].state;
            staffDtl.stfHrgEvntStartDt = null;
            staffDtl.stfHrgEvntEndDt = null;
            staffDtl.stfhrgEvntLoc = resultStaffList[0].stfhrgEvntLoc;
            staffDtl.stfhrgEvntLocPhn = resultStaffList[0].stfhrgEvntLocPhn;
            staffDtl.stfReqNbr = resultStaffList[0].stfReqNbr;
            staffDtl.targetPay = resultStaffList[0].targetPay;
            staffDtl.weekMgrAvble = null;
            staffDtl.zip = resultStaffList[0].zip;
            staffDtl.Referals = resultStaffList[0].Referals;
            staffDtl.qualPoolNts = resultStaffList[0].qualPoolNts;
            staffDtl.hrgEvntFlg = resultStaffList[0].hrgEvntFlg;
            staffDtl.hireEvntid = resultStaffList[0].hiringEventID;
            staffDtl.hireEventType = resultStaffList[0].hireEvntType;
            staffDtl.hireEventMgrAssociateId = resultStaffList[0].hireEventMgrAssociateId;
            CONSTANTS.phnScrnStaffingList.push(staffDtl);
        }

        // Added for the inclusion of the week available
        if (json.Response.staffingDetails.hasOwnProperty("weekBeginDt")) {
            CONSTANTS.getWeekAvail = json.Response.staffingDetails.weekBeginDt.month + "/" + json.Response.staffingDetails.weekBeginDt.day + "/" + json.Response.staffingDetails.weekBeginDt.year;
        }
    };

    /*
     * This method is used to process the response of Interview Screen details.
     *
     * @param response
     * @return N/A
     */
    this.intScrnIntrwDetail = function(json) {
        var dateVO = null; // DateVO
        var dateArray = [];
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("interviewDate")) {
            if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewDate)) {
                dateArray = json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewDate;
            } else {
                if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("interviewDate")) {
                    dateArray = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewDate ];
                }
            }
            if (dateArray) {
                for ( var j = 0; j < dateArray.length; j++) {
                    dateVO = {};
                    dateVO.month = dateArray[j].month;
                    dateVO.day = dateArray[j].day;
                    dateVO.year = dateArray[j].year;
                    var date = new Date(parseInt(dateArray[j].year), parseInt((dateArray[j].month) - 1), parseInt(dateArray[j].day));
                    CONSTANTS.intrDat = date;
                    CONSTANTS.intrwDateList.push(dateVO);
                }
            }
        }

        CONSTANTS.interviewScrnObj.intScrnInterviewTime(json);
    };

    /*
     * This method is used to process the response related to Interview
     * Screen-time details.
     *
     * @param response
     * @return N/A
     */
    this.intScrnInterviewTime = function(json) {
        var timeArray = [];
        var timeVO = null; // TimeStampVO
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("interviewTime")) {
            if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewTime)) {
                timeArray = json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewTime;
            } else {
                if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("interviewTime")) {
                    timeArray = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewTime ];
                }
            }
            if (timeArray) {
                for ( var k = 0; k < timeArray.length; k++) {
                    timeVO = {};
                    timeVO.month = timeArray[k].month;
                    timeVO.day = timeArray[k].day;
                    timeVO.year = timeArray[k].year;
                    timeVO.hour = timeArray[k].hour;
                    timeVO.minute = timeArray[k].minute;
                    timeVO.second = timeArray[k].second;
                    timeVO.milliSecond = timeArray[k].milliSecond;
                    timeVO.tmFrmt = 0;
                    CONSTANTS.intrwSchedTimeVO = timeVO;
                    CONSTANTS.intrwTimeList.push(timeVO);

                    CONSTANTS.intrwSchedDateVO.month = timeArray[k].month;
                    CONSTANTS.intrwSchedDateVO.day = timeArray[k].day;
                    CONSTANTS.intrwSchedDateVO.year = timeArray[k].year;

                    CONSTANTS.IntrwSchedDate = timeArray[k].formattedDate;
                    CONSTANTS.IntrwSchedTime = timeArray[k].formattedTime;
                }
            }
        }
    };

    /*
     * This method is used to process the response related to Interview
     * Screen-Location details.
     *
     * @param response
     * @return N/A
     */
    this.intScrnInterviewLocDtls = function(json) {
        var resultIntrwList = [];
        var intrwDtls = null; // PhnScrnIntrwDetails
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls) {
            if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls)) {
                resultIntrwList = json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList.MinimumResponseDtl;
            } else {
                if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls")) {
                    resultIntrwList = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls ];
                }
            }
            if (resultIntrwList && resultIntrwList.length > 0) {
                for ( var l = 0; l < resultIntrwList.length; l++) {
                    intrwDtls = {};
                    intrwDtls.intrwDate = "";
                    intrwDtls.intrwTime = CONSTANTS.interviewScrnObj.getTimeToDisplay(CONSTANTS.intrwSchedTimeVO);
                    intrwDtls.interViewer = resultIntrwList[l].interviewer;
                    intrwDtls.interTyp = resultIntrwList[l].interviewLocId;
                    intrwDtls.intrLocName = resultIntrwList[l].interviewLocName;
                    intrwDtls.intrLocAddr = resultIntrwList[l].add;
                    intrwDtls.intrLocCity = resultIntrwList[l].city;
                    intrwDtls.intrLocState = resultIntrwList[l].state;
                    intrwDtls.intrLocZip = resultIntrwList[l].zip;
                    intrwDtls.intrLocPhn = resultIntrwList[l].phone;
                    CONSTANTS.phnScreenIntrwDtls.push(intrwDtls);
                }
            }
        }

        CONSTANTS.interviewScrnObj.intScrnPacketDateTime(json);
    };

    /*
     * This method is used to process the response related to Interview
     * Screen-Date/Time details.
     *
     * @param response
     * @return N/A
     */
    this.intScrnPacketDateTime = function(json) {
        var packetTimeVO = null; // TimeStampVO
        var timeArray = [];
        // Send packet Time and Date to be displayed
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("sendPacketTime")) {
            if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.sendPacketTime)) {
                timeArray = json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.sendPacketTime;
            } else {
                if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("sendPacketTime")) {
                    timeArray = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.sendPacketTime ];
                }
            }
            if (timeArray) {
                for ( var k = 0; k < timeArray.length; k++) {
                    packetTimeVO = {};
                    packetTimeVO.month = timeArray[k].month;
                    packetTimeVO.day = timeArray[k].day;
                    packetTimeVO.year = timeArray[k].year;
                    packetTimeVO.hour = timeArray[k].hour;
                    packetTimeVO.minute = timeArray[k].minute;
                    packetTimeVO.second = timeArray[k].second;
                    packetTimeVO.milliSecond = timeArray[k].milliSecond;
                    packetTimeVO.tmFrmt = 0;

                    CONSTANTS.intrwPacketDate = timeArray[k].formattedDate;
                    CONSTANTS.intrwPacketTime = timeArray[k].formattedTime;

                }
            }

        }
    };

    /*
     * This method is used to process the response related to Interview
     * Screen-Material status.
     *
     * @param response
     * @return N/A
     */
    this.intScrnMaterialStatus = function(json) {
        var matTimeArray = [];
        var matTimeVO = null; // TimeStampVO
        // interviewMaterialStatusTimestamp details
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("interviewMaterialStatusTimestamp") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewMaterialStatusTimestamp) {

            if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewMaterialStatusTimestamp)) {
                matTimeArray = json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewMaterialStatusTimestamp;
            } else {
                if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("interviewMaterialStatusTimestamp")) {
                    matTimeArray = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewMaterialStatusTimestamp ];
                }
            }
            if (matTimeArray && matTimeArray.length > 0) {
                for ( var k = 0; k < matTimeArray.length; k++) {
                    matTimeVO = {};
                    matTimeVO.month = matTimeArray[k].month;
                    matTimeVO.day = matTimeArray[k].day;
                    matTimeVO.year = matTimeArray[k].year;
                    matTimeVO.hour = matTimeArray[k].hour;
                    matTimeVO.minute = matTimeArray[k].minute;
                    matTimeVO.second = matTimeArray[k].second;
                    matTimeVO.milliSecond = matTimeArray[k].milliSecond;
                    matTimeVO.tmFrmt = -0;
                    CONSTANTS.intrwMatScrDate = matTimeArray[k].formattedDate;
                    CONSTANTS.intrwMatScrTime = matTimeArray[k].formattedTime;

                }

            }
        }

        CONSTANTS.interviewScrnObj.intScrnStatusScrDateTime(json);
    };

    /*
     * This method is used to process the response related to Interview
     * Screen-Source Date/Time.
     *
     * @param response
     * @return N/A
     */
    this.intScrnStatusScrDateTime = function(json) {
        var timeArray = [];
        var timeVO = null; // TimeStampVO
        // interview StatusTimestamp details
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("interviewStatusTime") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewStatusTime) {

            if (Array.isArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewStatusTime)) {
                timeArray = json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewStatusTime;
            } else {
                if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("interviewStatusTime")) {
                    timeArray = [ json.Response.ITIDetailList.PhoneScreenIntrwDetail.interviewStatusTime ];
                }
            }
            if (timeArray && timeArray.length > 0) {
                for ( var k = 0; k < timeArray.length; k++) {
                    timeVO = {};
                    timeVO.month = timeArray[k].month;
                    timeVO.day = timeArray[k].day;
                    timeVO.year = timeArray[k].year;
                    timeVO.hour = timeArray[k].hour;
                    timeVO.minute = timeArray[k].minute;
                    timeVO.second = timeArray[k].second;
                    timeVO.milliSecond = timeArray[k].milliSecond;
                    timeVO.tmFrmt = -0;

                    CONSTANTS.intrwStatusScrDate = timeArray[k].formattedDate;
                    CONSTANTS.intrwStatusScrTime = timeArray[k].formattedTime;

                }

            }
        } else {
            CONSTANTS.intrwStatusScrDate = "";
            CONSTANTS.intrwStatusScrTime = "";
        }
    };

    /*
     * This method is used to process the result list
     * of Phone Screen details.
     *
     * @param result list
     * @return N/A
     */
    this.intScrnPhnScreenDtls = function(resultList) {
        var phnScrnDtl = {};
        // check whether the result list is having any value object
        // or not
        if (resultList && resultList.length > 0) {
            // iterating the result list to get each item and set in
            // to model locator
            for ( var n = 0; n < resultList.length; n++) {
                phnScrnDtl = {};
                phnScrnDtl.phoneScreenStatusCode = resultList[n].phoneScreenStatusCode;
                phnScrnDtl.cndtNbr = resultList[n].cndtNbr;
                phnScrnDtl.aid = resultList[n].aid;
                phnScrnDtl.reqNbr = resultList[n].reqNbr;
                phnScrnDtl.ScreenNbr = resultList[n].itiNbr;
                phnScrnDtl.canName = resultList[n].name;
                phnScrnDtl.internalExternal = resultList[n].internalExternal;
                phnScrnDtl.canPhn = resultList[n].canPhn;
                phnScrnDtl.strNbr = resultList[n].strNbr;
                phnScrnDtl.dept = resultList[n].dept;
                phnScrnDtl.candTitle = resultList[n].candTitle;
                phnScrnDtl.phnScrnDate = resultList[n].phnScrnDate;
                phnScrnDtl.phnScrnTime = resultList[n].phnScrnTime;
                phnScrnDtl.contactHist = resultList[n].contactHistoryTxt;
                phnScrnDtl.ynstatus = resultList[n].ynstatus;
                phnScrnDtl.detailResp = resultList[n].detailTxt;
                phnScrnDtl.overAllStatus = resultList[n].overAllStatus;
                phnScrnDtl.hrEventFlg = resultList[n].hrEventFlg;
                phnScrnDtl.ynStatusdesc = resultList[n].ynStatusdesc;
                phnScrnDtl.intrwTyp = resultList[n].intrwTyp;
                phnScrnDtl.phnScreener = resultList[n].phnScreener;
                phnScrnDtl.scrTyp = resultList[n].scrTyp;
                phnScrnDtl.emailAddr = resultList[n].emailAdd;
                phnScrnDtl.interviewStatusCode = resultList[n].interviewStatusCode;
                phnScrnDtl.interviewMaterialStatusCode = resultList[n].interviewMaterialStatusCode;
                // Added by p22o0mn for Defect #3323: New field
                // added to Get the Flag for enough Phone Screens
                // Completed or not for a given requisition
                phnScrnDtl.phoneScreensCompletedFlg = resultList[n].phoneScreensCompletedFlg;
                phnScrnDtl.candRefNbr = resultList[n].candRefNbr;
                phnScrnDtl.phoneScreenDispositionCode = resultList[n].phoneScreenDispositionCode;

                CONSTANTS.interviewScrnObj.phnScrnDateTimeList(resultList[n]);
                CONSTANTS.phnScreenDtls.push(phnScrnDtl);
            }
        }
    };

    /*
     * This method is used to process the result list of Phone Screen-Source
     * Date/Time.
     *
     * @param result list
     * @return N/A
     */
    this.phnScrnDateTimeList = function(resultList) {
        var timeVO = null; // TimeStampVO
        var dateVO = null; // DateVO
        // Added to implement for the phone screen date and
        // time to be saved once
        if (resultList.phnScrnDate) {
            dateVO = {};
            dateVO.month = resultList.phnScrnDate.month;
            dateVO.day = resultList.phnScrnDate.day;
            dateVO.year = resultList.phnScrnDate.year;
            CONSTANTS.phnScrnDateList.push(dateVO);

            var dateValues = new Date(parseInt(resultList.phnScrnDate.year), parseInt((resultList.phnScrnDate.month) - 1), parseInt(resultList.phnScrnDate.day));
            CONSTANTS.phnScrnDat = dateValues;

            if (resultList.phnScrnTime) {
                timeVO = {};
                timeVO.month = resultList.phnScrnTime.month;
                timeVO.day = resultList.phnScrnTime.day;
                timeVO.year = resultList.phnScrnTime.year;
                timeVO.hour = resultList.phnScrnTime.hour;
                timeVO.minute = resultList.phnScrnTime.minute;
                timeVO.second = resultList.phnScrnTime.second;
                timeVO.milliSecond = resultList.phnScrnTime.milliSecond;
                timeVO.tmFrmt = -0;

                CONSTANTS.phnScrnTimeList.push(timeVO);
            }

        }
    };

    /*
     * This method is used to process the response of Interview
     * Screen-Requisiton list.
     *
     * @param response
     * @return N/A
     */
    this.intScrnReqDtlList = function(json) {
        var resultListReq = [];
        var phnScrnReqDtl = null; // PhnScrnReqDtls
        // check whether RequisitionDetail is an array collection
        if (Array.isArray(json.Response.RequisitionDetailList.RequisitionDetail)) {
            resultListReq = json.Response.RequisitionDetailList.RequisitionDetail;
        }
        // RequisitionDetail is not an array collection
        else {
            if (json.Response.RequisitionDetailList.hasOwnProperty("RequisitionDetail")) {
                resultListReq = [ json.Response.RequisitionDetailList.RequisitionDetail ];
            }
        }

        // check whether the result list is having any value object or
        // not
        if (resultListReq && resultListReq.length > 0) {
            // iterating the result list to get each item and set in to
            // model locator

            for ( var a = 0; a < resultListReq.length; a++) {
                phnScrnReqDtl = {};
                phnScrnReqDtl.openings = resultListReq[a].openings;
                phnScrnReqDtl.fillDt = resultListReq[a].fillDt;
                phnScrnReqDtl.creator = resultListReq[a].creator;
                phnScrnReqDtl.dateCreated = resultListReq[a].dateCreate;
                phnScrnReqDtl.strNbr = resultListReq[a].store;
                phnScrnReqDtl.dept = resultListReq[a].dept;
                phnScrnReqDtl.reqAct = resultListReq[a].active;
                phnScrnReqDtl.job = resultListReq[a].job;
                phnScrnReqDtl.jobTitle = resultListReq[a].jobTtl;
                phnScrnReqDtl.ft = resultListReq[a].ft;
                phnScrnReqDtl.pt = resultListReq[a].pt;
                phnScrnReqDtl.scrnTyp = resultListReq[a].scrTyp;
                phnScrnReqDtl.targetPay = resultListReq[a].targetPay;
                phnScrnReqDtl.reqCalId = resultListReq[a].reqCalId;
                phnScrnReqDtl.sealTempJob = resultListReq[a].sealTempJob;
                phnScrnReqDtl.rscSchFlag = resultListReq[a].rscSchdFlg;
                phnScrnReqDtl.intrvDuration = resultListReq[a].interviewDurtn;
                phnScrnReqDtl.reqNumInterviews = resultListReq[a].reqNumInterviews;
                phnScrnReqDtl.offerExtCandCnt = resultListReq[a].offerExtCandCnt;
                phnScrnReqDtl.authPosCount = resultListReq[a].authPosCount;
                phnScrnReqDtl.interviewRemainingCandCnt = resultListReq[a].interviewRemainingCandCnt;
                phnScrnReqDtl.requisitionCalendarName = resultListReq[a].requisitionCalendarName;
                phnScrnReqDtl.rscToManageFlg = resultListReq[a].rscToManageFlg;
                phnScrnReqDtl.reqCandidateType = resultListReq[a].reqCandidateType;

                CONSTANTS.phnScreenReqDtls.push(phnScrnReqDtl);
            }

        }
    };

    /*
     * This method is used to process the response of Interview Screen-Store
     * detail list.
     *
     * @param response
     * @return N/A
     */
    this.intScrnStoreDtlList = function(json) {
        var resultStoreList = [];
        // check for the StoreDetailList tag in the response
        if (json.Response.hasOwnProperty("StoreDetailList") && json.Response.StoreDetailList && json.Response.StoreDetailList.hasOwnProperty("StoreDetails")) {
            // check whether StoreDetailList is an array
            // collection
            if (Array.isArray(json.Response.StoreDetailList.StoreDetails)) {
                resultStoreList = json.Response.StoreDetailList.StoreDetails;
            }
            // StoreDetailList is not an array collection
            else {
                if (json.Response.StoreDetailList.hasOwnProperty("StoreDetails")) {
                    resultStoreList = [ json.Response.StoreDetailList.StoreDetails ];
                }
            }
        }
        CONSTANTS.interviewScrnObj.setPhnScrnStrDtls(resultStoreList);
    };
    this.setPhnScrnStrDtls = function(resultStoreList) {
        var phnScrnStrDtls = null; // StoreVO
        // check whether the result list is having any value object or
        // not
        if (resultStoreList && resultStoreList.length > 0) {
            // iterating the result list to get each item and set in to
            // model locator
            for ( var b = 0; b < resultStoreList.length; b++) {
                phnScrnStrDtls = {};
                phnScrnStrDtls.addr = resultStoreList[b].addr;
                phnScrnStrDtls.city = resultStoreList[b].city;
                phnScrnStrDtls.state = resultStoreList[b].state;
                phnScrnStrDtls.zip = resultStoreList[b].zip;
                phnScrnStrDtls.phone = resultStoreList[b].phone;
                phnScrnStrDtls.storeMgr = resultStoreList[b].strMgr;
                phnScrnStrDtls.strName = resultStoreList[b].strName;
                phnScrnStrDtls.timeZoneCode = resultStoreList[b].timeZoneCode;
                phnScrnStrDtls.packetDate = resultStoreList[b].packetDateTime ? resultStoreList[b].packetDateTime.formattedDate : "";
                phnScrnStrDtls.packetTime = resultStoreList[b].packetDateTime ? resultStoreList[b].packetDateTime.formattedTime : "";
                CONSTANTS.phnScreenReqStoreDtls.push(phnScrnStrDtls);
            }
        }
    };
    /*
     * This method is used to check First week available date
     *
     * @param interview date
     * @param current date
     * @return boolean
     */
    this.checkFirstWeekAvabDate = function(intwDate, currDate) {
        for ( var i = 1; i <= 7; i++) {
            var date = new Date(currDate.getFullYear(), currDate.getMonth(), currDate.getDate() + i);
            if ((date.getMonth() === intwDate.getMonth()) && (date.getFullYear() === intwDate.getFullYear()) && (date.getDate() === intwDate.getDate())) {
                return true;
            }

        }
        return false;
    };
    /*
     * This method is used to check second week Available Date
     *
     * @param interview date
     * @param current date
     * @return boolean
     */
    this.checkSecondWeekAvabDate = function(intwDate, currDate) {
        for ( var i = 8; i <= 14; i++) {
            var date = new Date(currDate.getFullYear(), currDate.getMonth(), currDate.getDate() + i);
            if ((date.getMonth() === intwDate.getMonth()) && (date.getFullYear() === intwDate.getFullYear()) && (date.getDate() === intwDate.getDate())) {
                return true;
            }
        }
        return false;
    };
    /*
     * Convert String to date
     *
     * @param string
     * @return date
     */
    this.convertStringToDate = function(str) {
        var year = str.toString().substr(0, 4);
        var date = parseInt(str.toString().substr(8, 2));
        var mon = parseInt(str.toString().substr(5, 2)) - 1;
        return new Date(year, mon, date);
    };
    /*
     * Convert String to Datetime
     *
     * @param string
     * @return date time
     */
    this.convertStringToDateTime = function(str) {
        var year = str.toString().substr(0, 4);
        var date = parseInt(str.toString().substr(8, 2));
        var mon = parseInt(str.toString().substr(5, 2)) - 1;
        var hour = parseInt(str.toString().substr(11, 2));
        var min = parseInt(str.toString().substr(14, 2));
        return new Date(year, mon, date, hour, min);
    };
    /*
     * Get time from timestamp
     *
     * @param timestamp
     * @return time
     */
    this.getTimeToDisplay = function(timeStamp) {
        var displayTime = "";
        var no = 0;
        var strMinutes = "";
        if (timeStamp && timeStamp.hour) {
            no = parseInt(timeStamp.hour.toString().trim());
            if (timeStamp.minute) {
                strMinutes = timeStamp.minute.toString().trim();

                //Format time and append AM/PM
                if (no > 12) {
                    no = no - 12;
                    displayTime = (no < 10) ? "0" + no + ":" + strMinutes + " " + "PM" : no + ":" + strMinutes + " " + "PM";
                } else if (no === 12) {
                    displayTime = no + ":" + strMinutes + " " + "PM";
                } else if (no === 0) {
                    displayTime = "12" + ":" + strMinutes + " " + "AM";
                } else {
                    displayTime = (no < 10) ? "0" + no + ":" + strMinutes + " " + "AM" : no + ":" + strMinutes + " " + "AM";
                }
            }
        }
        return displayTime;
    };
    /*
     * Set Interview screen details from service response
     *
     * @param response
     * @return N/A
     */
    this.setIntervwScreenData = function() {
        this.displayRequisitionDetails(CONSTANTS.phnScreenReqDtls);
        this.displayCandidateDetails(CONSTANTS.phnScreenDtls);
        this.displayIntrDetails();
        CONSTANTS.interviewScrnObj.checkIntrvScheduled();
        CONSTANTS.interviewScrnObj.showHomeBtn();
    };
    /*
     * To show Home button
     *
     * @param N/A
     * @return N/A
     */
    this.showHomeBtn = function() {
        $("#goHome").show();
    };
    /*
     * Display Requisition details from service resposne
     *
     * @param requisition details
     * @return N/A
     */
    this.displayRequisitionDetails = function(reqDet) {
        if (CONSTANTS.phnScreenReqDtls && CONSTANTS.phnScreenReqDtls.length > 0) {
            $(".reqDetForm .strNbr").text(reqDet[0].strNbr);
            $(".reqDetForm .scrntype").text(reqDet[0].scrnTyp);
            $(".reqDetForm .jobCd").text(reqDet[0].job);
            $(".reqDetForm .seaTemp").text(reqDet[0].sealTempJob);
            $(".reqDetForm .created").text(reqDet[0].dateCreated);
            $(".reqDetForm .openings").text(CONSTANTS.getAuthPosCnt.toString());
            $(".reqDetForm .jobTitle").text(reqDet[0].jobTitle);
            $(".reqDetForm .ftFld").text(reqDet[0].ft);
            $(".reqDetForm .creator").text(reqDet[0].creator);
            $(".reqDetForm .toFill").text(reqDet[0].openings);
            $(".reqDetForm .dept").text(reqDet[0].dept);
            $(".reqDetForm .ptFld").text(reqDet[0].pt);
            $(".reqDetForm .dateNeeded").text(reqDet[0].fillDt);
            $(".reqDetForm .reqdIntw").text(reqDet[0].reqNumInterviews);
            $(".reqDetForm .activeFld").text(reqDet[0].reqAct);
            $(".reqDetForm .rscToSch").text(reqDet[0].rscSchFlag);
            $(".reqDetForm .calHirEnt").text(reqDet[0].requisitionCalendarName);
            $(".reqDetForm .rscToMng").text(reqDet[0].rscToManageFlg);
        }
        if (CONSTANTS.phnScreenReqStoreDtls && CONSTANTS.phnScreenReqStoreDtls.length > 0) {
            $(".reqDetForm .strMgr").text(CONSTANTS.phnScreenReqStoreDtls[0].storeMgr);
            $(".reqDetForm .phoneNbr").text(CONSTANTS.interviewScrnObj.getFormattedPhoneNumber(CONSTANTS.phnScreenReqStoreDtls[0].phone, "EXTERNAL"));
        }
        //Added for the appending of "$" and "/hr" for target pay in req section
        //Changed so that Target Pay will have 2 decimals..  MTS 1876  10/02/10
        if (CONSTANTS.phnScrnStaffingList && CONSTANTS.phnScrnStaffingList.length > 0) {
            if (CONSTANTS.phnScrnStaffingList[0].targetPay && CONSTANTS.phnScrnStaffingList[0].targetPay.toString().trim().length > 0) {
                var tgtPay = 0;
                tgtPay = parseFloat(CONSTANTS.phnScrnStaffingList[0].targetPay);
                $(".reqDetForm .targetPay").text(CONSTANTS.DOLLAR + tgtPay.toFixed(2) + CONSTANTS.PER_HR);
            } else {
                $(".reqDetForm .targetPay").text("");
            }
        }
    };
    /*
     * Formatted seven digit phone number
     *
     * @param phone number
     * @param format string
     * @return formatted string
     */
    this.sevenDigitFormatPhnNumber = function(phoneNumber, formatString) {
        for ( var g = 0; g < phoneNumber.length + 2; g++) {
            if (g === 3 || g === 0) {
                formatString = formatString + "-";
                formatString = formatString + phoneNumber.charAt(g);
            } else {
                formatString = formatString + phoneNumber.charAt(g);
            }

        }
        return formatString;
    };
    /*
     * Formatted ten digit phone number
     *
     * @param phone number
     * @param format string
     * @return formatted string
     */
    this.tenDigitFormatPhnNumber = function(phoneNumber, formatString) {
        for ( var t = 0; t < phoneNumber.length + 2; t++) {
            if (t === 3 || t === 6) {
                formatString = formatString + "-";
                formatString = formatString + phoneNumber.charAt(t);
            } else {
                formatString = formatString + phoneNumber.charAt(t);
            }
        }
        return formatString;
    };
    /*
     * get Formatted phone number
     *
     * @param phone number
     * @param phone type
     * @return formatted string
     */
    this.getFormattedPhoneNumber = function(phoneNumber, phnType) {
        if (phoneNumber && phoneNumber !== "" && CONSTANTS.EXTERN === phnType) {
            var formatString = "";
            if (phoneNumber.toString().length === 7) {
                formatString = CONSTANTS.interviewScrnObj.sevenDigitFormatPhnNumber(phoneNumber.toString(), formatString);
            }
            if (phoneNumber.toString().length === 10) {
                formatString = CONSTANTS.interviewScrnObj.tenDigitFormatPhnNumber(phoneNumber.toString(), formatString);
            }
            return formatString;
        } else {
            return phoneNumber;
        }
    };
    /*
     * Display candidate details from service response
     *
     * @param phnScrndet
     * @return N/A
     */
    this.displayCandidateDetails = function(phnScrndet) {
        if (phnScrndet && phnScrndet.length > 0) {
            $(".candDetForm .screenNbr").text(phnScrndet[0].ScreenNbr);
            $(".candDetForm .reqNbr").text(phnScrndet[0].reqNbr);
            $(".candDetForm .candName").text(phnScrndet[0].canName);
            $(".candDetForm .candId").text(phnScrndet[0].candRefNbr);
            $(".candDetForm .emailId").text(phnScrndet[0].emailAddr);
            $(".candDetForm .phnNbr").text(CONSTANTS.interviewScrnObj.getFormattedPhoneNumber(phnScrndet[0].canPhn, phnScrndet[0].internalExternal));
            $(".candDetForm .extNbr").text(phnScrndet[0].internalExternal);
        }
    };
    /*
     * Display interview details
     *
     * @param phnScrndet
     * @return N/A
     */
    this.displayIntrDetails = function() {
        $(".intrStatus .intrStatusDate").text(CONSTANTS.intrwStatusScrDate);
        $(".intrStatus .intrStatusTime").text(CONSTANTS.intrwStatusScrTime);

        $(".intrDetForm .mtrlStatusDate").text(CONSTANTS.intrwMatScrDate);
        $(".intrDetForm .mtrlStatusTime").text(CONSTANTS.intrwMatScrTime);

        if (CONSTANTS.phnScreenReqDtls && CONSTANTS.phnScreenReqDtls.length > 0) {
            $(".intrDetForm .intrDuration").text(CONSTANTS.phnScreenReqDtls[0].intrvDuration);
            $(".intrDetForm .rscSchedule").text(CONSTANTS.phnScreenReqDtls[0].rscSchFlag);
        }
        if (CONSTANTS.phnScreenIntrwDtls) {
            if (CONSTANTS.phnScreenIntrwDtls.length > 0 && CONSTANTS.IntrwSchedDate && CONSTANTS.IntrwSchedDate !== "") {
                $(".intrDetForm .intrLocName").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocName);
                $(".intrDetForm .intrLocAddr").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocAddr);
                $(".intrDetForm .intrLocCity").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocCity);
                $(".intrDetForm .intrLocPhn").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn);
                $(".intrDetForm .intrLocState").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocState);
                $(".intrDetForm .intrLocZip").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocZip);
            } else {
                $(".intrDetForm .intrLocName").text("");
                $(".intrDetForm .intrLocAddr").text("");
                $(".intrDetForm .intrLocCity").text("");
                $(".intrDetForm .intrLocPhn").text("");
                $(".intrDetForm .intrLocState").text("");
                $(".intrDetForm .intrLocZip").text("");
            }
            if (CONSTANTS.phnScreenIntrwDtls.length > 0) {
                $(".intrDetForm .intrvewr .iterviewer").val(CONSTANTS.phnScreenIntrwDtls[0].interViewer);
            } else {
                $(".intrDetForm .intrvewr .iterviewer").val("");
            }
        }
        $(".intrDetForm .intrDate").text(CONSTANTS.IntrwSchedDate);
        $(".intrDetForm .intrTime").text(CONSTANTS.IntrwSchedTime);
        $(".intrDetForm .packDate").text(CONSTANTS.intrwPacketDate);
        $(".intrDetForm .packTime").text(CONSTANTS.intrwPacketTime);
        //to load First Week Grid
        CONSTANTS.interviewScrnObj.loadfirstweekGrid("#firstWeekGrid", CONSTANTS.FirstWeekIntervAvailDatesCollection);
        //to load Second Week Grid
        CONSTANTS.interviewScrnObj.loadsecondweekGrid("#secondWeekGrid", CONSTANTS.SecondWeekInterAvailDatesCollection);
    };
    /*
     * This method is used to append options in combo box
     * Format Option values
     *
     * @param combo box id
     * @param action
     * @param options
     * @param text
     * @param value
     * @return N/A
     */
    this.appendOptionToSelect = function(drpDownId, action, optionsArray, textKey, valueKey) {
        if (action === "append") {
            $('#' + drpDownId).empty();
            for ( var i = 0; i < optionsArray.length; i++) {
                var currentObj = optionsArray[i];
                $('#' + drpDownId).append($("<option>", {
                    text : currentObj[textKey],
                    value : currentObj[valueKey]
                }));
                if (valueKey === "hireEventId") {
                    currentObj.enabled ? '' : $('#' + drpDownId + ' option[value=' + currentObj.valueKey + '] ').attr("disabled", "disabled");
                    $('#' + drpDownId).selectBoxIt();
                    $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
                }
            }
            $('#' + drpDownId).attr('disabled', false);
            $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
        } else {
            $('#' + drpDownId).find('option').remove();
            $('#' + drpDownId).attr('disabled', true);
            $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
        }

    };

    /*
     * This method will help to build second week grid
     * consisting of the following columns:
     * 1. Date
     * 2. BeginTime
     * 3. EndTime
     * On click of a row, the corresponding details will be loaded.
     *
     * @param target
     * @param grid data
     * @return N/A
     */
    this.loadfirstweekGrid = function(target, gridData) {
        $(target).empty();
        $(target).append("<div id='myfirstWeekGrid' style='width:432px; height: 208px;'></div>");
        var grid;
        var columns = [ {
            id : "date",
            sortable : true,
            name : "Date",
            field : "date",
            formatter : CONSTANTS.interviewScrnObj.titleFmtr,
            minWidth : 150
        }, {
            id : "beginTime",
            sortable : true,
            name : "Begin Time",
            field : "beginTime"
        }, {
            id : "endTime",
            sortable : true,
            name : "End Time",
            field : "endTime"
        } ];
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            defaultSortAsc : true,
            forceFitColumns : true,
            scrollToTop : true,
            enableCellNavigation : true,
            multiColumnSort : true
        };
        var data = [];

        var tempData = gridData;
        if (tempData.constructor !== Array) {
            data.push(tempData);
        } else {
            data = tempData;
        }
        if (data.length < 6) {
            var emptyData = [];
            for ( var i = 0; i < 6; i++) {
                emptyData[i] = data[i];
            }
            data = emptyData;
        }
        grid = new Slick.Grid("#myfirstWeekGrid", data, columns, options);
        $(window).resize(function() {
            $("#myfirstWeekGrid").width($(target).width());
            $(".slick-viewport").width($("#myfirstWeekGrid").width());
            grid.resizeCanvas();
        });
        //on click of a row in the grid
        grid.onClick.subscribe(function(e, args) {
            CONSTANTS.firstWeekDtlsRowSelected = args;
            $('#myfirstWeekGrid .grid-canvas .slick-row').removeClass('rowSelected');
            $('#myfirstWeekGrid .grid-canvas .slick-row.active').addClass('rowSelected');
            CONSTANTS.interviewScrnObj.getIntrwSchedData(args);
        });
        CONSTANTS.interviewScrnObj.gridsort(grid, data);

    };
    /* This method is used to implement the sort functionality in the grid */
    this.gridsort = function(grid, data) {
        grid.onSort.subscribe(function(e, args) {
            var cols = args.sortCols;
            data.sort(function(dataRow1, dataRow2) {
                for ( var i = 0, l = cols.length; i < l; i++) {
                    var field = cols[i].sortCol.field;
                    var sign = cols[i].sortAsc ? 1 : -1;
                    var value1 = dataRow1[field], value2 = dataRow2[field];
                    var result = (value1 === value2 ? 0 : (value1 > value2 ? 1 : -1)) * sign;
                    if (result !== 0) {
                        return result;
                    }
                }
                return 0;
            });
            grid.invalidate();
            grid.render();
        });
    };
    /*
     * This method will help to build second week grid
     * consisting of the following columns:
     * 1. Date
     * 2. BeginTime
     * 3. EndTime
     * On click of a row, the corresponding details will be loaded.
     *
     * @param target
     * @param grid data
     * @return N/A
     */
    this.loadsecondweekGrid = function(target, gridData) {
        $(target).empty();
        $(target).append("<div id='mySecondWeekGrid' style='width:432px; height: 208px;'></div>");
        var grid;
        var columns = [ {
            id : "date",
            sortable : true,
            name : "Date",
            field : "date",
            formatter : CONSTANTS.interviewScrnObj.titleFmtr,
            minWidth : 150
        }, {
            id : "beginTime",
            sortable : true,
            name : "Begin Time",
            field : "beginTime"
        }, {
            id : "endTime",
            sortable : true,
            name : "End Time",
            field : "endTime"
        } ];
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            defaultSortAsc : true,
            forceFitColumns : true,
            scrollToTop : true,
            enableCellNavigation : true,
            multiColumnSort : true
        };
        var data = [];

        var tempData = gridData;
        if (tempData.constructor !== Array) {
            data.push(tempData);
        } else {
            data = tempData;
        }
        if (data.length < 6) {
            var emptyData = [];
            for ( var i = 0; i < 6; i++) {
                emptyData[i] = data[i];
            }
            data = emptyData;
        }
        grid = new Slick.Grid("#mySecondWeekGrid", data, columns, options);
        $(window).resize(function() {
            $("#mySecondWeekGrid").width($(target).width());
            $(".slick-viewport").width($("#mySecondWeekGrid").width());
            grid.resizeCanvas();
        });
        //on click of a row in the grid
        grid.onClick.subscribe(function(e, args) {
            CONSTANTS.secondWeekDtlsRowSelected = args;
            $('#mySecondWeekGrid .grid-canvas .slick-row').removeClass('rowSelected');
            $('#mySecondWeekGrid .grid-canvas .slick-row.active').addClass('rowSelected');
            CONSTANTS.interviewScrnObj.getIntrwSchedData(args);
        });
        CONSTANTS.interviewScrnObj.gridsort(grid, data);

    };
    /*
     * This method is used to add title argument
     * for each cell in the grids to show the cell text
     * to the user on mouse over.
     *
     * @param row id
     * @param cell
     * @param value
     * @return N/A
     */
    this.titleFmtr = function(row, cell, value) {
        return value ? ("<span title='" + value + "'>" + value + "</span>") : "";
    };
    /*
     * Load details onclick of second week grid
     *
     * @param N/A
     * @return N/A
     */
    $("#secondGridImg").click(function() {
        $('#firstGridImg').attr('src', 'assets/images/upIcon.gif');
        (!($("#secondWeekGrid:visible").length > 0)) ? ($('#secondGridImg').attr('src', 'assets/images/selectedIcon.gif')) : ($('#secondGridImg').attr('src', 'assets/images/upIcon.gif'));
        $("#collapse2").off();
        $("#collapse2").on("shown.bs.collapse", function() {
            currentObject.loadsecondweekGrid("#secondWeekGrid", CONSTANTS.SecondWeekInterAvailDatesCollection);
        });
    });
    /*
     * Load details onclick of first week grid
     *
     * @param N/A
     * @return N/A
     */
    $("#firstGridImg").click(function() {
        (!($("#firstWeekGrid:visible").length > 0)) ? ($('#firstGridImg').attr('src', 'assets/images/selectedIcon.gif')) : ($('#firstGridImg').attr('src', 'assets/images/upIcon.gif'));
        $('#secondGridImg').attr('src', 'assets/images/upIcon.gif');
        $("#collapse1").off();
        $("#collapse1").on("shown.bs.collapse", function() {
            currentObject.loadfirstweekGrid("#firstWeekGrid", CONSTANTS.FirstWeekIntervAvailDatesCollection);
        });
    });
    /*
     * Get Interview Scheduled data
     * Based on the entered store number, hiring event
     * details/get store details service is invoked
     *
     * @param arguments
     * @return N/A
     */
    this.getIntrwSchedData = function(args) {
        CONSTANTS.interviewScrnObj.selectedRowData = args.grid.getData()[args.row];
        if (CONSTANTS.interviewScrnObj.selectedRowData && CONSTANTS.interviewScrnObj.selectedRowData.hasOwnProperty('date')) {
            CONSTANTS.IntrwSchedDate = CONSTANTS.interviewScrnObj.selectedRowData.date;
            CONSTANTS.IntrwSchedTime = CONSTANTS.interviewScrnObj.selectedRowData.beginTime;
            $(".intrDate").text(CONSTANTS.IntrwSchedDate);
            $(".intrTime").text(CONSTANTS.IntrwSchedTime);
            CONSTANTS.SeqNbr = CONSTANTS.interviewScrnObj.selectedRowData.concurrent;
            var intrwTime = CONSTANTS.interviewScrnObj.selectedRowData.uiTimeStamp_Time;
            var intrwDate = CONSTANTS.interviewScrnObj.selectedRowData.uiTimeStamp_Date;
            if (CONSTANTS.IntrwSchedTime.toLowerCase().indexOf("pm") >= 0) {
            	intrwTime = (parseInt(intrwTime.split(':')[0])+ ((intrwTime.split(':')[0] >='12')?0:12)).toString() + ":"+ intrwTime.split(':')[1];
            }
            CONSTANTS.intrwSchedTimeVO = CONSTANTS.interviewScrnObj.getInterviewTime(intrwDate, intrwTime);
            CONSTANTS.intrwSchedDateVO = CONSTANTS.interviewScrnObj.getInterviewDate(intrwDate);
            var storeNo = CONSTANTS.interviewScrnObj.selectedRowData.storeNo.toString().trim();
            var timeFormat = {};
            var dateFormat = {};
            timeFormat.formatString = "L:NN A";
            dateFormat.formatString = "EEEE - MM/DD/YYYY";
            var inputData = {};
            var callbackFunction = $.Callbacks('once');
            var isStrValid = ((storeNo === "null") || (storeNo === "") || (storeNo.length < 1));
            if (!isStrValid && CONSTANTS.phnScrnStaffingList[0].hrgEvntFlg === "Y" && CONSTANTS.phnScrnStaffingList[0].hireEventType !== "SDE") {
                CONSTANTS.intrwSchedTimeVO = {}; // (TimeStampVO)
                CONSTANTS.intrwSchedTimeVO = CONSTANTS.interviewScrnObj.getInterviewTime(intrwDate, intrwTime);
                CONSTANTS.intrwSchedDateVO = CONSTANTS.interviewScrnObj.getInterviewDate(intrwDate);
                // This is a Hiring Event, populate the Interview Location fields
                // from the Hiring Event Data.
                callbackFunction.add(CONSTANTS.interviewScrnObj.loadHiringEventDetails);
                RSASERVICES.getHiringEventDetails(CONSTANTS.phnScrnStaffingList[0].hireEvntid, callbackFunction, true);

                var zoneMappedDate = CONSTANTS.interviewScrnObj.getstoreZoneMapping(CONSTANTS.phnScreenReqStoreDtls[0].timeZoneCode);
                CONSTANTS.interviewScrnObj.setZoneMappedDateTime(zoneMappedDate);                
            } else if (!isStrValid) {
                inputData = {
                    "TimeStampTO" : CONSTANTS.intrwSchedTimeVO
                };
                callbackFunction.add(CONSTANTS.interviewScrnObj.loadStrDetails);
                RSASERVICES.getStoreDetails(storeNo, inputData, callbackFunction, true, "Please wait...");
            }
        }
    };
    this.getstoreZoneMapping = function(timeZoneCode) {
        var interviewDate = null;

        if (CONSTANTS.intrwSchedTimeVO) {

            var year = parseInt(CONSTANTS.intrwSchedTimeVO.year);
            var date = parseInt(CONSTANTS.intrwSchedTimeVO.day);
            var mon = parseInt(CONSTANTS.intrwSchedTimeVO.month) - 1;
            var hour = parseInt(CONSTANTS.intrwSchedTimeVO.hour);
            var min = parseInt(CONSTANTS.intrwSchedTimeVO.minute);

            interviewDate = new Date(year, mon, date, hour, min);
            if (timeZoneCode.toString() === ("13005")) {
                // Arizona                        
                if (UTILITY.isDST(interviewDate)) {
                    interviewDate.setHours(interviewDate.getHours() + 3);
                } else {
                    interviewDate.setHours(interviewDate.getHours() + 2);
                }
            } else if (timeZoneCode.toString() === ("13411")) {
                // Eastern
                interviewDate.setHours(interviewDate.getHours() + 0);
            } else if (timeZoneCode.toString() === ("13406")) {
                // Mountain
                interviewDate.setHours(interviewDate.getHours() + 2);
            } else if (timeZoneCode.toString() === ("13404")) {
                // Pacific
                interviewDate.setHours(interviewDate.getHours() + 3);
            } else if (timeZoneCode.toString() === ("13407") || timeZoneCode.toString() === ("13007")) {
                // Central
                interviewDate.setHours(interviewDate.getHours() + 1);
            } else if (timeZoneCode.toString() === ("13403")) {
                // Alaska
                interviewDate.setHours(interviewDate.getHours() + 4);
            }
        }
        //Store is not in Scope of Application so just return the same time                
        return interviewDate;
    };
    /*
     * Load Store details from service response
     * Details are populated in the Interview Details
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.loadStrDetails = function(json) {
        $.unblockUI();
        var phnScrnStrDtls = {};
        var resultStoreList = [];

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            // check whether status is Success or Error
            CONSTANTS.nonHrStrList = [];

            // check for the StoreDetailList tag in the response
            if (json.Response.hasOwnProperty("StoreDetailList") && json.Response.StoreDetailList.hasOwnProperty("StoreDetails")) {
                resultStoreList = CONSTANTS.interviewScrnObj.chkStrDtls(json);

            }
            // check whether the result list is having any value object or
            // not
            if (resultStoreList && resultStoreList.length > 0) {
                // iterating the result list to get each item and set in to
                // model locator
                for ( var i = 0; i < resultStoreList.length; i++) {
                    phnScrnStrDtls = {};
                    phnScrnStrDtls.addr = resultStoreList[i].add;
                    phnScrnStrDtls.city = resultStoreList[i].city;
                    phnScrnStrDtls.state = resultStoreList[i].state;
                    phnScrnStrDtls.zip = resultStoreList[i].zip;
                    phnScrnStrDtls.phone = resultStoreList[i].phone;
                    phnScrnStrDtls.storeMgr = resultStoreList[i].strMgr;
                    phnScrnStrDtls.strName = resultStoreList[i].strName;
                    phnScrnStrDtls.timeZoneCode = resultStoreList[i].timeZoneCode;
                    phnScrnStrDtls.packetDate = resultStoreList[i].packetDateTime.formattedDate;
                    phnScrnStrDtls.packetTime = resultStoreList[i].packetDateTime.formattedTime;
                    CONSTANTS.nonHrStrList.push(phnScrnStrDtls);
                }
            }

            // Added for Interview Screen
            $(".intrLocName").text(CONSTANTS.nonHrStrList[0].strName);
            $(".intrLocAddr").text(CONSTANTS.nonHrStrList[0].addr);
            $(".intrLocCity").text(CONSTANTS.nonHrStrList[0].city);
            $(".intrLocPhn").text(CONSTANTS.interviewScrnObj.getFormattedNumber(CONSTANTS.nonHrStrList[0].phone));
            $(".intrLocState").text(CONSTANTS.nonHrStrList[0].state);
            $(".intrLocZip").text(CONSTANTS.nonHrStrList[0].zip);

            var zoneMappedDate = CONSTANTS.interviewScrnObj.getstoreZoneMapping(CONSTANTS.phnScreenReqStoreDtls[0].timeZoneCode);
            CONSTANTS.interviewScrnObj.setZoneMappedDateTime(zoneMappedDate);
        }
        // Handling fo the error for the error
        else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewScrnObj.popup.alert(json.Response.error.errorMsg);
        }
    };
    this.setZoneMappedDateTime = function(zoneMappedDate) {
    	//Days of the week
        var days = [ 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday' ];
        var dateFormatted = days[zoneMappedDate.getDay()] + ' - ' + (("0" + (zoneMappedDate.getMonth() + 1)).slice(-2)) + '/' + ("0" + zoneMappedDate.getDate()).slice(-2) + '/' + zoneMappedDate.getFullYear();
        $(".packDate").text(dateFormatted);

        var hours = zoneMappedDate.getHours();
        var minutes = zoneMappedDate.getMinutes();
        var ampm = hours >= 12 ? 'PM' : 'AM';
        hours = hours % 12;
        hours = hours ? hours : 12;
        minutes = minutes < 10 ? '0' + minutes : minutes;
        var timeFormatted = hours + ':' + minutes + ' ' + ampm;
        $(".packTime").text(timeFormatted);
    };

    /*
     * This method is used to check the store details.
     *
     * @param response
     * @return N/A
     */
    this.chkStrDtls = function(json) {
        var resultStoreList = [];
        // check whether StoreDetailList is an array collection
        if (Array.isArray(json.Response.StoreDetailList.StoreDetails)) {
            resultStoreList = json.Response.StoreDetailList.StoreDetails;
        }
        // StoreDetailList is not an array collection
        else {
            if (json.Response.StoreDetailList.hasOwnProperty("StoreDetails")) {
                resultStoreList = [ json.Response.StoreDetailList.StoreDetails ];
            }
        }
        return resultStoreList;
    };
    /*
     * Format Phone Number
     *
     * @param phone number
     * @return N/A
     */
    this.getFormattedNumber = function(phoneNumber) {
        if (phoneNumber && phoneNumber.toString() !== "") {
            var formatString = "";
            for ( var h = 0; h < phoneNumber.toString().length + 2; h++) {
                if (h === 3 || h === 6) {

                    formatString = formatString + "-";
                    formatString = formatString + phoneNumber.toString().charAt(h);
                } else {
                    formatString = formatString + phoneNumber.toString().charAt(h);
                }

            }
            return formatString;
        } else {
            return phoneNumber;
        }
    };

    /*
     * Load Hiring Event Details
     * Incase of error retrieved from service, the error message
     * is passed to the ErrorHandling.js, where the error is processed.
     * The processed error message is then retieved and shown as alert.
     *
     * @param response
     * @return N/A
     */
    this.loadHiringEventDetails = function(json) {
        $.unblockUI();
        var returnedErrorMessage = "";

        // Check for returned errors
        if (json.Response.hasOwnProperty("error")) {
            var errorResultList = [];
            if (Array.isArray(json.Response.error)) {
                errorResultList = json.Response.error;
            } else {
                errorResultList = [ json.Response.error ];
            }
            returnedErrorMessage = errorHandling(errorResultList);
            CONSTANTS.interviewScrnObj.popup.alert(returnedErrorMessage);
        } else if (json.Response.hasOwnProperty("hiringEventResponse") && json.Response.hiringEventResponse.hasOwnProperty("hiringEventDetail")) {
            $(".intrLocName").text(json.Response.hiringEventResponse.hiringEventDetail.hireEventLocationDescription);
            $(".intrLocAddr").text(json.Response.hiringEventResponse.hiringEventDetail.hireEventAddressText);
            $(".intrLocCity").text(json.Response.hiringEventResponse.hiringEventDetail.hireEventCityName);
            $(".intrLocState").text(json.Response.hiringEventResponse.hiringEventDetail.hireEventStateCode);
            $(".intrLocZip").text(json.Response.hiringEventResponse.hiringEventDetail.hireEventZipCodeCode);

            // Hiring Event Manager Details
            if (json.Response.hiringEventResponse.hasOwnProperty("hiringEventMgrData")) {
                $(".intrLocPhn").text(json.Response.hiringEventResponse.hiringEventMgrData.phone);
            }
        }
    };
    /*
     * Get Interview Time details from Date value
     *
     * @param date
     * @param time
     * @return N/A
     */
    this.getInterviewTime = function(dateValue, timeValues) {
        var _dateFormatted = dateValue;
        var valuesOfDate = _dateFormatted.split("/");
        var valuesOfTime = timeValues.split(":");
        var timeobj = {};
        timeobj.month = valuesOfDate[0];
        timeobj.day = valuesOfDate[1];
        timeobj.year = valuesOfDate[2];
        timeobj.hour = valuesOfTime[0];
        timeobj.minute = valuesOfTime[1];
        timeobj.second = "00";
        timeobj.milliSecond = "0";
        return timeobj;
    };
    /*
     * Get Interview Date
     *
     * @param date
     * @return N/A
     */
    this.getInterviewDate = function(dateValue) {
        var _dateFormatted = dateValue;
        var valuesOfDate = _dateFormatted.split("/");
        var dateobj = {};
        dateobj.month = valuesOfDate[0];
        dateobj.day = valuesOfDate[1];
        dateobj.year = valuesOfDate[2];
        return dateobj;
    };
    /*
     * Display Requistion Details on click of requistion link
     *
     * @param event
     * @return N/A
     */
    $(".reqNbr").on("click", function() {
        //calling the showPopUp method to show warning popup with message
        if (CONSTANTS.phnscrnSaveCheck) {
            CONSTANTS.interviewScrnObj.showUnsavedPopUpReq("");
        } else {
            CONSTANTS.interviewScrnObj.handleUnsavedReqOk("");
        }
    });
    /*
     * Display unsaved warning message
     */
    this.showUnsavedPopUpReq = function() {
        $('#unSaveWarnModalReq .unSaveWarnMsg').empty();
        $('#unSaveWarnModalReq .unSaveWarnMsg').append("<p>Unsaved Data In Phone Screen Details.Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
        $('#unSaveWarnModalReq').modal('show');
        CONSTANTS.interviewScrnObj.promptStaticDraggable('#unSaveWarnModalReq');
    };
    /*
     * Handle unsaved warning message
     */
    this.handleUnsavedReqOk = function() {
    	if ($("#unSaveWarnModalReq:visible").length > 0) {
    		$('#unSaveWarnModalReq').on('hidden.bs.modal', function() {
	    		CONSTANTS.interviewScrnObj.navigateToReqScreenDetails();
	    	}.bind(this));
	        $('#unSaveWarnModalReq').modal('hide');  
    	} else {
    		CONSTANTS.interviewScrnObj.navigateToReqScreenDetails();
    	}    	      
    };
    /*
     * show unsaved Popup candidate
     */
    this.showUnsavedPopUpCand = function() {
        $('#unSaveWarnModalCand .unSaveWarnMsg').empty();
        $('#unSaveWarnModalCand .unSaveWarnMsg').append("<p>Unsaved Data In Phone Screen Details.Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
        $('#unSaveWarnModalCand').modal('show');
        CONSTANTS.interviewScrnObj.promptStaticDraggable('#unSaveWarnModalCand');
    };
    /*
     * Handle unsaved Popup candidate
     */
    this.handleUnsavedCandOk = function() {
        //Hide the popup
        $('#unSaveWarnModalCand').modal('hide');
        $('#unSaveWarnModalReq').modal('hide');
        CONSTANTS.interviewScrnObj.navigateToCandidateDetails();
    };
    /*
     * Display candidate details on click of candidate details
     *
     * @param event
     * @return N/A
     */
    $(".candId").on("click", function(e) {
        // calling the showPopUp method to show warning popup with message
        if (CONSTANTS.phnscrnSaveCheck) {
            CONSTANTS.interviewScrnObj.showUnsavedPopUpCand("");
        } else {
            CONSTANTS.interviewScrnObj.handleUnsavedCandOk(null);
        }
        CONSTANTS.interviewScrnObj.navigateToCandidateDetails(e);
    });
    /*
     * Navigate to candidate details
     * on click of the Candidate Id hyperlink
     *
     * @param N/A
     * @return N/A
     */
    this.navigateToCandidateDetails = function() {
        CONSTANTS.interviewScrnObj.reInitializeCTIelements();
        CONSTANTS.retailStaffingObj.candRefId = $(".candId").text();
        CONSTANTS.retailStaffingObj.applicantId = CONSTANTS.phnScreenDtls[0].cndtNbr;
        $.get('app/RetailStaffing/view/candidateDetails.html', CONSTANTS.interviewScrnObj.setContent);
    };
    /*
     * Navigate to Requistion Screen details
     * on click of the requisition number hyperlink
     *
     * @param N/A
     * @return N/A
     */
    this.navigateToReqScreenDetails = function() {
        CONSTANTS.interviewScrnObj.reInitializeCTIelements();
        CONSTANTS.retailStaffingObj.reqNumber = $(".reqNbr").text();
        CONSTANTS.calledFromSearchPage = true;
        UTILITY.clearCache();
        $.get('app/RSARequisitionDetails/view/requisitionDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
    /*
     * Landing Page
     *
     * @param data
     * @return N/A
     */
    this.setContent = function(data) {
        $("#divLandingView").html(data);
    };
    /*
     * Initialise constants value
     *
     * @param N/A
     * @return N/A
     */
    this.reInitializeCTIelements = function() {
        CONSTANTS.ConfirmationNo = 0;
        CONSTANTS.CallType = "";
        CONSTANTS.isEnableIntrwScrnWarnPop = false;
    };
    /*
     * On click of refresh Available Time button,
     * service invoked to load Available Interview
     * times for the first and second week.
     *
     * @param N/A
     * @return N/A
     */
    this.refreshAvailableTime = function() {
        var myReqCalId = 0;
        if ((CONSTANTS.phnScreenReqDtls) && (CONSTANTS.phnScreenReqDtls.length > 0)) {
            var phnScrnReqDetails;
            for ( var i = 0; i < CONSTANTS.phnScreenReqDtls.length; i++) {
                phnScrnReqDetails = CONSTANTS.phnScreenReqDtls[i];
                myReqCalId = parseInt(phnScrnReqDetails.reqCalId);
            }
        }
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(CONSTANTS.interviewScrnObj.loadAvailIntTimes);
        RSASERVICES.getAvailableInterviewTimes(myReqCalId, CONSTANTS.phnScreenReqDtls[0].intrvDuration, CONSTANTS.phnScreenReqDtls[0].rscSchFlag, callbackFunction, true, "Please wait...");

    };
    /*
     * On click of refresh Available Time button,
     * Loads Available Interview times for the first and second week.
     * Loads the retrieved values in first week grid.
     * Similarly, in second week grid.
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.loadAvailIntTimes = function(json) {
        $.unblockUI();

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS && json.Response.hasOwnProperty("CalendarList")) {
            // check whether status is Success or Error
            if (json.Response.CalendarList) {
                CONSTANTS.interviewScrnObj.scheduleAvailIntTimes(json);
            } else {
                CONSTANTS.FirstWeekIntervAvailDatesCollection = [];
                CONSTANTS.SecondWeekInterAvailDatesCollection = [];
            }
        }
        // checking for the error
        else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewScrnObj.popup.alert(json.Response.error.errorMsg);
        }

        CONSTANTS.interviewScrnObj.loadfirstweekGrid("#firstWeekGrid", CONSTANTS.FirstWeekIntervAvailDatesCollection);
        CONSTANTS.interviewScrnObj.loadsecondweekGrid("#secondWeekGrid", CONSTANTS.SecondWeekInterAvailDatesCollection);
    };

    /*
     * Formats data/tiome of begin and end time
     * Load the Week collection
     *
     * @param result list
     * @return N/A
     */
    this.weekCollection = function(resultIntervDescList) {
        var interviewSchedVO = {}; // InterviewSchedVO
        CONSTANTS.FirstWeekIntervAvailDatesCollection = [];
        CONSTANTS.SecondWeekInterAvailDatesCollection = [];

        for ( var m = 0; m < resultIntervDescList.length; m++) {
            var beginDate = new Date();
            var begintime = new Date();
            var endtime = new Date();
            var concurrent = "";
            //Days of the week
            var days = [ 'Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday' ];

            beginDate = CONSTANTS.interviewScrnObj.convertStringToDate(resultIntervDescList[m].beginTimestamp);
            begintime = CONSTANTS.interviewScrnObj.convertStringToDateTime(resultIntervDescList[m].beginTimestamp);
            endtime = CONSTANTS.interviewScrnObj.convertStringToDateTime(resultIntervDescList[m].endTimestamp);
            concurrent = resultIntervDescList[m].interviewerAvailabilityCount;

            var dateFormattedBegintime = days[begintime.getDay()] + ' - ' + (("0" + (begintime.getMonth() + 1)).slice(-2)) + '/' + ("0" + begintime.getDate()).slice(-2) + '/' + begintime.getFullYear();

            var _dateFormattedBegintime = (("0" + (begintime.getMonth() + 1)).slice(-2)) + '/' + ("0" + begintime.getDate()).slice(-2) + '/' + begintime.getFullYear();

            var hours = begintime.getHours();
            var minutes = begintime.getMinutes();
            var ampm = hours >= 12 ? 'PM' : 'AM';
            hours = hours % 12;
            hours = hours ? hours : 12;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            var timeFormattedBegintime = hours + ':' + minutes + ' ' + ampm;

            hours = begintime.getHours();
            minutes = begintime.getMinutes();
            var _timeFormattedBegintime = hours + ':' + minutes;

            hours = endtime.getHours();
            minutes = endtime.getMinutes();
            ampm = hours >= 12 ? 'PM' : 'AM';
            hours = hours % 12;
            hours = hours ? hours : 12;
            minutes = minutes < 10 ? '0' + minutes : minutes;
            var timeFormattedEndtime = hours + ':' + minutes + ' ' + ampm;

            interviewSchedVO = {};
            interviewSchedVO.date = dateFormattedBegintime;
            interviewSchedVO.beginTime = timeFormattedBegintime;
            interviewSchedVO.endTime = timeFormattedEndtime;
            interviewSchedVO.uiTimeStamp_Date = _dateFormattedBegintime;
            interviewSchedVO.uiTimeStamp_Time = _timeFormattedBegintime;
            interviewSchedVO.concurrent = concurrent;
            interviewSchedVO.storeNo = resultIntervDescList[m].humanResourcesSystemStoreNumber;

            (CONSTANTS.interviewScrnObj.checkFirstWeekAvabDate(beginDate, new Date())) ? CONSTANTS.FirstWeekIntervAvailDatesCollection.push(interviewSchedVO) : "";

            (CONSTANTS.interviewScrnObj.checkSecondWeekAvabDate(beginDate, new Date())) ? CONSTANTS.SecondWeekInterAvailDatesCollection.push(interviewSchedVO) : "";
        }
    };
    /*
     * This method is used to process the response of Available Schedule time
     * details.
     *
     * @param response
     * @return N/A
     */
    this.scheduleAvailIntTimes = function(json) {
        var resultIntervDescList = [];
        if (json.Response.CalendarList.hasOwnProperty("ScheduleTimeDetails")) {
            if (Array.isArray(json.Response.CalendarList.ScheduleTimeDetails)) {
                resultIntervDescList = json.Response.CalendarList.ScheduleTimeDetails;
            } else {
                resultIntervDescList = [ json.Response.CalendarList.ScheduleTimeDetails ];
            }
            if ((resultIntervDescList) && (resultIntervDescList.length > 0)) {
                CONSTANTS.interviewScrnObj.weekCollection(resultIntervDescList);
            }
        }
    };
    /*
     * Validate Phone screen
     *
     * @param N/A
     * @return N/A
     */
    this.valPhnScreen = function() {
        CONSTANTS.phnscrnSaveCheck = true;
    };
    /*
     * Change Interview Material Status details based
     * on the selected Interview Status
     *
     * @param N/A
     * @return N/A
     */
    this.changeInterviewStatus = function() {
        CONSTANTS.interviewScrnObj.valPhnScreen();
        if ($("#intrstatusSelect option:selected").val() === "11" && CONSTANTS.phnScrnStaffingList[0].hrgEvntFlg === "N") {
            // Interview Scheduled, Set to "Send Calendar Packet"
            $('#mtrlstatusSelect option:eq(2)').prop('selected', true);
            $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        } else if ($("#intrstatusSelect option:selected").val() === "12" && CONSTANTS.phnScrnStaffingList[0].hrgEvntFlg === "N") {
            // Store Scheduling, Set to "Send Email Packet"
            $('#mtrlstatusSelect option:eq(3)').prop('selected', true);
            $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        } else if (CONSTANTS.phnScrnStaffingList[0].hrgEvntFlg === "Y" && ($("#intrstatusSelect option:selected").val() === "11" || $("#intrstatusSelect option:selected").val() === "12")) {
            // Hiring Event, Set to "Interview Materials Sent"
            $('#mtrlstatusSelect option:eq(1)').prop('selected', true);
            $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        } else {
            // Set to "Select"
            $('#mtrlstatusSelect option:eq(0)').prop('selected', true);
            $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        }
        CONSTANTS.interviewScrnObj.materialStausOption();
        CONSTANTS.phnscrnSaveCheck = true;
    };

    /*
     * This method is used to set the material status option.
     *
     * @param N/A
     * @return N/A
     */
    this.materialStausOption = function() {
        // Set material status to "Calendar Packet Sent" whenever it is a Hiring
        // Event and Interview Status is
        // Interview Scheduled or Store Scheduled.
        if (CONSTANTS.phnScreenDtls[0].hrEventFlg === "Y" && ($("#intrstatusSelect option:selected").val() === "11" || $("#intrstatusSelect option:selected").val() === "12")) {
            // Hiring Event, Set to "Calendar Packet Sent"
            $('#mtrlstatusSelect option:eq(1)').prop('selected', true);
            $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
        }
    };

    /*
     * Unsaved data alert shown when user clicks
     * on Cancel/Home button.
     *
     * @param N/A
     * @return N/A
     */
    this.goToHome = function(event) {
        if (event.target.value === "Close") {
            CONSTANTS.isCloseBtnClick = true;
        } else {
            CONSTANTS.isCloseBtnClick = false;
        }
        CONSTANTS.interviewScrnObj.reInitializeCTIelements();
        if (CONSTANTS.phnscrnSaveCheck) {
            // calling the showPopUp method to show warning popup with message
            CONSTANTS.interviewScrnObj.showUnsavedPopUpHome("");
        } else {
            //Navigates user to home screen
            CONSTANTS.interviewScrnObj.handleUnsavedHomeOk(null);
        }
    };
    $("#goHome").off("click");
    $("#goHome").show().on("click", function(e) {
    	UTILITY.RemoveQS();
        CONSTANTS.interviewScrnObj.goToHome(e);
    });
    $(".navCloseBtn").off("click");
    $(".navCloseBtn").show().on("click", function(e) {
        CONSTANTS.interviewScrnObj.goToHome(e);
    });
    $("#cancelIntDet").off("click");
    $("#cancelIntDet").show().on("click", function(e) {
        CONSTANTS.interviewScrnObj.goToHome(e);
    });
    /*
     * Unsaved data alert shown when user clicks
     * on Cancel/Home button.
     *
     * @param N/A
     * @return N/A
     */
    this.showUnsavedPopUpHome = function() {
        $('#unSaveWarnModalHome .unSaveWarnMsg').empty();
        $('#unSaveWarnModalHome .unSaveWarnMsg').append("<p>Unsaved Data In Interview Screen Details.Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
        $('#unSaveWarnModalHome').modal('show');
        CONSTANTS.interviewScrnObj.promptStaticDraggable('#unSaveWarnModalHome');
    };
    /*
     * Returns to home screen once user clicks OK
     * in Unsave data alert pop up.
     * Refresh all the objects
     *
     * @param N/A
     * @return N/A
     */
    this.handleUnsavedHomeOk = function() {
        $("#unSaveWarnModalHome").on("hide.bs.modal", function() {
            $("#reqdetnav").removeClass("blurBody");
            $("#intrvwScreen").removeClass("blurBody");
        });
        $('#unSaveWarnModalHome').modal('hide');
        if (CONSTANTS.isCloseBtnClick) {
            CONSTANTS.isCloseBtnClick = false;
            window.close();
        } else {
            CONSTANTS.phnScreenDtls = [];
            CONSTANTS.phnScreenIntrwDtls = [];
            CONSTANTS.phnScreenReqStoreDtls = [];
            CONSTANTS.phnScreenReqDtls = [];
            CONSTANTS.phnScrnStaffingList = [];
            CONSTANTS.getAuthPosCnt = 0;
            CONSTANTS.getTotalPhnScrnDtls = [];
            CONSTANTS.getInActPhnScrnDtls = [];
            CONSTANTS.FirstWeekIntervAvailDatesCollection = [];
            CONSTANTS.SecondWeekInterAvailDatesCollection = [];
            //Navigates user to home screen
            UTILITY.RemoveQS();
            $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.interviewScrnObj.setContent);
        }
    };
    /*
     * This method will be triggered on click of save/Submit.
     * Request is formed to Save Interview details
     * Submit Interview Details service is invoked.
     *
     * @param event
     * @return N/A
     */
    $("#saveIntDet, #submitIntDet").click(function(e) {
        var saveobj = {};
        CONSTANTS.interviewScrnObj.reInitializeCTIelements();
        if(CONSTANTS.interviewScrnObj.warningPopup(e)){
        	return;
        }

        if ((CONSTANTS.minResponseList) && (CONSTANTS.minResponseList.length > 0)) {
            saveobj.minReqArray = CONSTANTS.minResponseList;
        }
        if ((CONSTANTS.phnScreenDtls) && (CONSTANTS.phnScreenDtls.length > 0)) {
            saveobj = _.extend(saveobj, CONSTANTS.interviewScrnObj.savePhnScrnDtls());
        }

        saveobj = _.extend(saveobj, CONSTANTS.interviewScrnObj.savePhnScrnDateTime());
        saveobj.locName = $(".intrLocName").text();
        saveobj.intrwAddr = $(".intrLocAddr").text();
        saveobj.intrwCity = $(".intrLocCity").text();
        saveobj.intrwPhn = $(".intrLocPhn").text();
        saveobj.intrwState = $(".intrLocState").text();
        saveobj.intrwZip = $(".intrLocZip").text();
        saveobj = _.extend(saveobj, CONSTANTS.interviewScrnObj.saveIntScrnLocStr());
        // Add for New Datamodel by SS
        saveobj.intrwSeqNbr = ((CONSTANTS.SeqNbr) && (CONSTANTS.SeqNbr.toString().length > 0)) ? CONSTANTS.SeqNbr : null;
        var myPattern = /</g;
        var iterviewer = $(".iterviewer").val().toString().replace(myPattern, "&lt;");
        saveobj.interviewer = CONSTANTS.interviewScrnObj.decodeSpecialCharacters(iterviewer);
        saveobj.intrwTimeVo = CONSTANTS.intrwSchedTimeVO;
        saveobj.intrwDateVO = CONSTANTS.intrwSchedDateVO;

        var saveIntDetails = CONSTANTS.interviewScrnObj.updateSaveObj(saveobj);
        var data = {
            data : JSON.stringify({
                "UpdatePhoneScrnDtlsRequest" : saveIntDetails.UpdatePhoneScrnDtlsRequest
            })
        };

        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(CONSTANTS.interviewScrnObj.saveIntDetailsResponse);
        RSASERVICES.submitInterviewScrnDetails(data, callbackFunction, true, "Please wait...");
    });
    /*
     * Setting interview Location Type code, store number and
     * requisition Id - in the Request to be sent to
     * service
     *
     * @param N/A
     * @return save object
     */
    this.saveIntScrnLocStr = function() {
        var i = 0;
        var saveobj = {};
        if ((CONSTANTS.phnScreenIntrwDtls) && (CONSTANTS.phnScreenIntrwDtls.length > 0)) {
            var phnScrnIntrwDetails = {}; // PhnScrnIntrwDetails
            for (i = 0; i < CONSTANTS.phnScreenIntrwDtls.length; i++) {
                phnScrnIntrwDetails = CONSTANTS.phnScreenIntrwDtls[i];
                saveobj.interviewLocTypCd = phnScrnIntrwDetails.interTyp;
            }
        }
        if ((CONSTANTS.phnScreenReqDtls) && (CONSTANTS.phnScreenReqDtls.length > 0)) {
            var phnScrnReqDetails = {}; // PhnScrnReqDtls
            for (i = 0; i < CONSTANTS.phnScreenReqDtls.length; i++) {
                phnScrnReqDetails = CONSTANTS.phnScreenReqDtls[i];
                saveobj.store = phnScrnReqDetails.strNbr;
                saveobj.reqCalId = phnScrnReqDetails.reqCalId;
            }
        }
        return saveobj;
    };

    /*
     * Setting Phone screen Details- date/time in the Request to be sent to
     * service
     *
     * @param N/A
     * @return save object
     */
    this.savePhnScrnDateTime = function() {
        var i = 0;
        var saveobj = {};
        if ((CONSTANTS.phnScrnTimeList) && (CONSTANTS.phnScrnTimeList.length > 0)) {
            var timeVO = {}; // TimeStampVO
            for (i = 0; i < CONSTANTS.phnScrnTimeList.length; i++) {
                timeVO = CONSTANTS.phnScrnTimeList[i];
                saveobj.phnScrnTimeVo = timeVO;
            }
        }

        if ((CONSTANTS.phnScrnDateList) && (CONSTANTS.phnScrnDateList.length > 0)) {
            var dateVO = {}; // DateVO
            for (i = 0; i < CONSTANTS.phnScrnDateList.length; i++) {
                dateVO = CONSTANTS.phnScrnDateList[i];
                saveobj.PhnScrnDateVo = dateVO;
            }
        }
        return saveobj;
    };

    /*
     * Save Phone screen Details
     * Formation of Request to be sent to service
     *
     * @param N/A
     * @return save object
     */
    this.savePhnScrnDtls = function() {
        var saveobj = {};
        var phnScrDetVo = {}; // PhnScrDetVO
        var qualReg = /</g;
        for ( var i = 0; i < CONSTANTS.phnScreenDtls.length; i++) {
            phnScrDetVo = CONSTANTS.phnScreenDtls[i];
            saveobj.emailAddr = phnScrDetVo.emailAddr;
            saveobj.reqNbr = phnScrDetVo.reqNbr;
            saveobj.phnScrnNbr = phnScrDetVo.ScreenNbr;
            saveobj.canNbr = phnScrDetVo.cndtNbr;
            saveobj.phoneScreenStat = phnScrDetVo.phoneScreenStatusCode;
            saveobj.intviewStat = $("#intrstatusSelect option:selected").val();

            // Set material status to "Calendar Packet Sent"
            // whenever it is a Hiring Event and Interview
            // Status is
            // Interview Scheduled or Store Scheduled.
            if (CONSTANTS.phnScrnStaffingList[0].hrgEvntFlg === "Y" && (saveobj.intviewStat === "11" || saveobj.intviewStat === "12")) {
                // Hiring Event, Set to "Calendar Packet
                // Sent"
                $('#mtrlstatusSelect option:eq(1)').prop('selected', true);
                $("#mtrlstatusSelect").data("selectBox-selectBoxIt").refresh();
            }
            saveobj.intviewMatStat = $("#mtrlstatusSelect option:selected").val();

            if (phnScrDetVo.contactHist) {
                saveobj.contactHist = phnScrDetVo.contactHist.toString().replace(qualReg, "&lt;");
            }
            if (phnScrDetVo.detailResp) {
                saveobj.phnDtlResp = phnScrDetVo.detailResp.toString().replace(qualReg, "&lt;");
            }
            saveobj.aid = phnScrDetVo.aid;
            saveobj.canName = phnScrDetVo.canName;
            saveobj.canPhn = phnScrDetVo.canPhn;
            saveobj.ynStatusCode = phnScrDetVo.ynstatus;
            saveobj.cndStrNbr = phnScrDetVo.strNbr;
            saveobj.interviewLocTypCd = "";
            saveobj.overAllStat = "";
            saveobj.reqCalId = "";
            saveobj.phnScreener = phnScrDetVo.phnScreener;
        }

        return saveobj;
    };

    /*
     * Display of warning pop up.
     * Thorw alert messages and return if
     * input not given by user
     *
     * @param N/A
     * @return
     */
    this.warningPopup = function(e) {
        if ($("#intrstatusSelect option:selected").text() === "INTERVIEW SCHEDULED") {
            if (!$(".intrDetForm .intrDate").text() || $(".intrDetForm .intrDate").text().trim().length <= 0) {
                // calling the showPopUp method to show warning
                // popup with message
                $('#intrWarnModal .intrWarnMsg').empty();
                $('#intrWarnModal .intrWarnMsg').append("<p>Please select the Date and Time:");
                $('#intrWarnModal').modal('show');
                CONSTANTS.interviewScrnObj.promptStaticDraggable('#intrWarnModal');
                return true;
            }

            if (!$(".intrDetForm .intrTime").text() || $(".intrDetForm .intrTime").text().trim().length <= 0) {
                // calling the showPopUp method to show warning
                // popup with message
                $('#intrWarnModal .intrWarnMsg').empty();
                $('#intrWarnModal .intrWarnMsg').append("<p>Please select the Date and Time:");
                $('#intrWarnModal').modal('show');
                CONSTANTS.interviewScrnObj.promptStaticDraggable('#intrWarnModal');
                return true;
            }
        }
        //set/reset flags based on the user click of Save/Submit
        if (e.target.id === "saveIntDet") {
            CONSTANTS.saveInterviewScrnBtnClickFlg = true;
            CONSTANTS.submitInterviewScrnBtnClickFlg = false;
        } else {
            CONSTANTS.submitInterviewScrnBtnClickFlg = true;
            CONSTANTS.saveInterviewScrnBtnClickFlg = false;
        }
        return false;
    };

    /*
     * Update the saved interview details.
     *
     * @param save object
     * @return save interview details
     */
    this.updateSaveObj = function(saveobj) {
        var saveIntDetails = {
            "UpdatePhoneScrnDtlsRequest" : {
                "insertUpdate" : "UPDATE",
                "PhoneScreenIntrwDetail" : {
                    "MinimumResponseDtlList" : {},
                    "emailAdd" : saveobj.emailAddr,
                    "InterviewLocDtls" : {
                        "interviewLocName" : saveobj.locName,
                        "interviewLocId" : saveobj.interviewLocTypCd,
                        "interviewer" : saveobj.interviewer,
                        "add" : saveobj.intrwAddr,
                        "city" : saveobj.intrwCity,
                        "phone" : saveobj.intrwPhn,
                        "state" : saveobj.intrwState,
                        "zip" : saveobj.intrwZip,
                        "interviewDate" : {
                            "month" : saveobj.intrwDateVO.month,
                            "day" : saveobj.intrwDateVO.day,
                            "year" : saveobj.intrwDateVO.year
                        },
                        "interviewTime" : {
                            "month" : saveobj.intrwTimeVo.month,
                            "day" : saveobj.intrwTimeVo.day,
                            "year" : saveobj.intrwTimeVo.year,
                            "hour" : saveobj.intrwTimeVo.hour,
                            "second" : saveobj.intrwTimeVo.second,
                            "minute" : saveobj.intrwTimeVo.minute,
                            "milliSecond" : saveobj.intrwTimeVo.milliSecond
                        }
                    },
                    "phnScrnDate" : {
                        "month" : saveobj.PhnScrnDateVo.month,
                        "day" : saveobj.PhnScrnDateVo.day,
                        "year" : saveobj.PhnScrnDateVo.year
                    },
                    "phnScrnTime" : {
                        "month" : saveobj.phnScrnTimeVo.month,
                        "day" : saveobj.phnScrnTimeVo.day,
                        "year" : saveobj.phnScrnTimeVo.year,
                        "hour" : saveobj.phnScrnTimeVo.hour,
                        "second" : saveobj.phnScrnTimeVo.second,
                        "minute" : saveobj.phnScrnTimeVo.minute,
                        "milliSecond" : saveobj.phnScrnTimeVo.milliSecond
                    },
                    "reqNbr" : saveobj.reqNbr,
                    "itiNbr" : saveobj.phnScrnNbr,
                    "cndtNbr" : saveobj.canNbr,
                    "phoneScreenStatusCode" : saveobj.phoneScreenStat,
                    "interviewStatusCode" : saveobj.intviewStat,
                    "interviewMaterialStatusCode" : saveobj.intviewMatStat,
                    "contactHistoryTxt" : saveobj.contactHist,
                    "detailTxt" : saveobj.phnDtlResp,
                    "aid" : saveobj.aid,
                    "name" : saveobj.canName,
                    "canPhn" : saveobj.canPhn,
                    "ynstatus" : saveobj.ynStatusCode,
                    "cndStrNbr" : saveobj.store,
                    "interviewLocTypCd" : saveobj.interviewLocTypCd,
                    "canStatus" : "Y",
                    "reqCalId" : saveobj.reqCalId,
                    "phnScreener" : saveobj.phnScreener,
                    "intrvSeqNbr" : saveobj.intrwSeqNbr
                }
            }
        };
        if (saveobj.minReqArray) {
            saveIntDetails.UpdatePhoneScrnDtlsRequest.PhoneScreenIntrwDetail.MinimumResponseDtlList.MinimumResponseDtl = [];
            for ( var i = 0; i < saveobj.minReqArray.length; i++) {
                saveobj.minReqArray[i].seqNbr = saveobj.minReqArray[i].seqNbr.toString() + 1;
                if (saveobj.minReqArray[i].minimumResponse === "Yes") {
                    saveobj.minReqArray[i].minimumResponse = "Y";
                } else if (saveobj.minReqArray[i].minimumResponse === "No") {
                    saveobj.minReqArray[i].minimumResponse = "N";
                } else if (saveobj.minReqArray[i].minimumResponse === "N/A") {
                    saveobj.minReqArray[i].minimumResponse = "A";
                }
                saveIntDetails.UpdatePhoneScrnDtlsRequest.PhoneScreenIntrwDetail.MinimumResponseDtlList.MinimumResponseDtl.push(saveobj.minReqArray[i]);
            }
        }
        return saveIntDetails;
    };
    /*
     * Save Interview Details Response from service
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.saveIntDetailsResponse = function(json) {
        $.unblockUI();
        if (CONSTANTS.submitInterviewScrnBtnClickFlg) {
            CONSTANTS.phnscrnSaveCheck = false;
            CONSTANTS.SUCCESS_ON_HOME = true;
            UTILITY.RemoveQS();
            $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.interviewScrnObj.setContent);
        } else if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            CONSTANTS.phnscrnSaveCheck = false;
            CONSTANTS.interviewScrnObj.popup.alert("Details Saved Successfully");

        }
        if (json.Response.hasOwnProperty("error") && json.Response.error.hasOwnProperty("errorMsg")) {
            CONSTANTS.interviewScrnObj.popup.alert(json.Response.error.errorMsg);
        }
    };
    /*
     * Takes the user entered text and Decodes
     * the Special Characters
     *
     * @param text to format
     * @return formatted text
     */
    this.decodeSpecialCharacters = function(textToFormat) {
        var AddInt = 0;
        var part1Add = "";
        var part2Add = "";
        for ( var hrAdd = 0; hrAdd < textToFormat.length; hrAdd++) {
            AddInt = textToFormat.charCodeAt(hrAdd);
            if (!(AddInt >= 32 && AddInt <= 125)) {
                part1Add = textToFormat.toString().substr(0, hrAdd);
                part2Add = textToFormat.toString().substr(hrAdd + 1, textToFormat.length - 1);
                textToFormat = "";

                if (AddInt > 8214 && AddInt < 8218) {
                    textToFormat = part1Add + "&apos;" + part2Add;
                } else if (AddInt >= 8218 && AddInt < 8222) {
                    textToFormat = part1Add + "&quot;" + part2Add;
                } else {
                    textToFormat = part1Add + part2Add;
                }
            }
        }
        return textToFormat;
    };
    /*
     * Remove backdrop blur and close alert popup
     * on click of OK button.
     *
     * @param id
     * @return N/A
     */
    this.alertOkClicked = function(id) {
        $(id).on("hide.bs.modal", function() {
            $("#reqdetnav").removeClass("blurBody");
            $("#intrvwScreen").removeClass("blurBody");
        });
        $(id).modal('hide');
    };
    /*
     * Make backdrop blur when pop up appears
     * Make pop ups draggable and static
     *
     * @param id
     * @return N/A
     */
    this.promptStaticDraggable = function(id) {
        if ($(".modal-backdrop.fade.in").length > 1) {
            $(".modal-backdrop.fade.in:eq(1)").css({
                "z-index" : "1600"
            });
        }
        $(id).on("shown.bs.modal", function() {
            $(id).find('.modal-dialog').addClass("modal-dialog-center");
            // Set top and left margin to look alike flex screens
            $(id).find('.modal-dialog').css({
                'margin-top' : function() {
                    return -($(id + ' .modal-dialog').outerHeight() / 2);
                },
                'margin-left' : function() {
                    return -($(id + ' .modal-dialog').outerWidth() / 2);
                }
            });
            $(".alertmodalbtn").focus();
        });
        //makes the pop up static
        $(id).modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        //allows user to drag the pop up
        $(id).draggable({
            handle : ".modal-header"
        });
    };
    /*
     * Triggered during key down in text box if data-pattern
     * attribute is specified for the input element
     */
    $("input[data-pattern]").assignKeyDownToInputs();
};
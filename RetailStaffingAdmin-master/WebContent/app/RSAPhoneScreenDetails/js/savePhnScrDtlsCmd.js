 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: savePhnScrDtlsCmd.js
 * Application: Retail Staffing Admin
 *
 */
function savePhnScrDtlsCmd(saveobj) {
/*
 * Declare variables to construct service response
 */
    var qualReg = /</g;
    var saveContHist = $("#txtAreaCntHist").val().replace(qualReg, "&lt;");
    saveobj.contactHist = CONSTANTS.phonescreenDetailsObj.decodeSpecialCharacters(saveContHist);
    var saveDtlResp = $("#txtAreaPhnDetRes").val().replace(qualReg, "&lt;");
    var validateIntrType = (($("input[name='intrType']:checked").val() === 2 && CONSTANTS.phnScreenDtls[0].hrEventFlg === "N") || $("input[name='intrType']:checked").val() === 3);
    saveobj.phnDtlResp = CONSTANTS.phonescreenDetailsObj.decodeSpecialCharacters(saveDtlResp);

    saveobj.canNbr = CONSTANTS.phnScreenDtls[0].cndtNbr;
    saveobj.reqNbr = CONSTANTS.phnScreenDtls[0].reqNbr;
    saveobj.phnScrnNbr = CONSTANTS.phnScreenDtls[0].ScreenNbr;
    saveobj.interviewLocTypCd = parseInt($("input[name='intrType']:checked").val()) + 1;
    saveobj.interviewer = CONSTANTS.phnScreenIntrwDtls[0].interViewer; // CONSTANTS.phonescreenDetailsObj.decodeSpecialCharacters($("#intrvr").val().replace(qualReg, "&lt;"));
    saveobj.phnScreener = $(".screener").text();
    saveobj.aid = $(".associateId").text();
    saveobj.canName = $(".candName").text();
    saveobj.canPhn = CONSTANTS.phnScreenDtls[0].canPhn;
    saveobj.emailAddr = $("#emailId").val();

    var checkStaffingList = (CONSTANTS.phnScrnStaffingList && CONSTANTS.phnScrnStaffingList.length > 0);
    //Set status as 5 for do not proceed
    if ($("#ynMinReqStat").text() === CONSTANTS.DO_NOT_PROCEED) {
        saveobj.ynStatusCode = "5";
    } else if ($("#ynMinReqStat").text() === CONSTANTS.PROCEED) {
          //Set status as 12 for  proceed
        saveobj.ynStatusCode = "12";
    } else if ($("#ynMinReqStat").text() === "") {
          //Set status as 0 if empty
        saveobj.ynStatusCode = "0";
    }

    // AIMS Created Reqisition
    if (parseInt(CONSTANTS.phnScreenDtls[0].reqNbr) < 150000000) {
        if ($("input[name='intrType']:checked").val() === 0 || $("input[name='intrType']:checked").val() === 1 || ($("input[name='intrType']:checked").val() === 2 && CONSTANTS.phnScreenDtls[0].hrEventFlg === "Y")) {
            //Set interview phonescreen details
            if ($("input[name='intrType']:checked").val() === 0 && (CONSTANTS.phnScreenReqStoreDtls && CONSTANTS.phnScreenReqStoreDtls.length > 0 && CONSTANTS.phnScreenReqStoreDtls[0].phone)) {
                saveobj.intrwPhn = CONSTANTS.phnScreenReqStoreDtls[0].phone;
            } else if ($("input[name='intrType']:checked").val() === 1) {
                saveobj.intrwPhn = (CONSTANTS.nonHrStrList && CONSTANTS.nonHrStrList.length > 0 && CONSTANTS.nonHrStrList[0].phone) ? CONSTANTS.nonHrStrList[0].phone : CONSTANTS.phnScreenReqStoreDtls[0].phone;
            } else if ($("input[name='intrType']:checked").val() === 2 && CONSTANTS.phnScreenDtls[0].hrEventFlg === "Y" && checkStaffingList && CONSTANTS.phnScrnStaffingList[0].stfhrgEvntLocPhn) {
                saveobj.intrwPhn = CONSTANTS.phnScrnStaffingList[0].stfhrgEvntLocPhn;
            } else if (validateIntrType && ($("#phnFirst").val() !== "" && $("#phnSecond").val() !== "" && $("#phnThird").val() !== "")) {
                saveobj.intrwPhn = $("#phnFirst").val() + $("#phnSecond").val() + $("#phnThird").val();
            }
            //Save location details
            saveobj.locName = $("#intrLocNameLbl").text;
            saveobj.intrwAddr = $("#intrLocAddrLbl").text;
            saveobj.intrwCity = $("#intrLocCityLbl").text;
            saveobj.intrwState = $("#intrLocStateLbl").text;
            saveobj.intrwZip = $("#intrLocZipLbl").text;
        } else {
            // Added to handle "<" symbol in the text input
            qualReg = /</g;
            var saveLocName = $("#intrLocName").val().replace(qualReg, "&lt;");
            saveobj.locName = CONSTANTS.phonescreenDetailsObj.decodeSpecialCharacters(saveLocName);

            qualReg = /</g;
            var saveLocAddr = $("#intrLocAddr").val().replace(qualReg, "&lt;");
            saveobj.intrwAddr = CONSTANTS.phonescreenDetailsObj.decodeSpecialCharacters(saveLocAddr);

            qualReg = /</g;
            var saveLocCity = $("#intrLocCity").val().replace(qualReg, "&lt;");
            saveobj.intrwCity = CONSTANTS.phonescreenDetailsObj.decodeSpecialCharacters(saveLocCity);

            qualReg = /</g;
            var saveLocState = $("#intrLocState").val().replace(qualReg, "&lt;");
            saveobj.intrwState = CONSTANTS.phonescreenDetailsObj.decodeSpecialCharacters(saveLocState);

            saveobj.intrwZip = $("#intrLocZip").val();

            saveobj.intrwPhn = $("#phnFirst").val() + $("#phnSecond").val() + $("#phnThird").val();
        }
    } else {
        // Staffing Request Created Reqisition
        if (CONSTANTS.phnScreenIntrwDtls[0].intrLocName && CONSTANTS.phnScreenIntrwDtls[0].intrLocName !== "" && CONSTANTS.phnScreenIntrwDtls[0].intrLocName !== "null") {
            //Set values
            saveobj.locName = CONSTANTS.phnScreenIntrwDtls[0].intrLocName;
            saveobj.intrwAddr = CONSTANTS.phnScreenIntrwDtls[0].intrLocAddr;
            saveobj.intrwCity = CONSTANTS.phnScreenIntrwDtls[0].intrLocCity;
            saveobj.intrwState = CONSTANTS.phnScreenIntrwDtls[0].intrLocState;
            saveobj.intrwZip = CONSTANTS.phnScreenIntrwDtls[0].intrLocZip;
            saveobj.intrwPhn = CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn;
        } else {
            //Reset values
            saveobj.locName = "";
            saveobj.intrwAddr = "";
            saveobj.intrwCity = "";
            saveobj.intrwState = "";
            saveobj.intrwZip = "";
            saveobj.intrwPhn = "";
        }
    }
    //Set store number
    saveobj.store = CONSTANTS.phnScreenReqDtls[0].strNbr;

    // Need Requisition Calendar Id to complete save
    saveobj.reqCalId = CONSTANTS.phnScreenReqDtls[0].reqCalId;

    var minArr = [];
    var numMinRequirements = 0;
    //Check if expanded phonescreen to set minimum requirement
    if (CONSTANTS.isExpandedPhoneScreen) {
        numMinRequirements = 25;
    } else {
        numMinRequirements = 10;
    }

    var i = 0;
    for (i = 0; i < numMinRequirements; i++) {
        var obj = "";
        obj = "minReq" + (i + 1);
        minArr.push($("#" + obj + " option:selected").index());
    }

    saveobj.minReqArray = minArr;
    var minResTo = {};
    CONSTANTS.minResponseList = [];
    //Set minimum response
    for (i = 0; i < saveobj.minReqArray.length; i++) {
        minResTo = {};
        minResTo.seqNbr = i + 1;
        //Set minimum response
        if (saveobj.minReqArray[i] === 0) {
            minResTo.minimumResponse = "A";
        } else if (saveobj.minReqArray[i] === 1) {
            minResTo.minimumResponse = "Y";
        } else if (saveobj.minReqArray[i] === 2) {
            minResTo.minimumResponse = "N";
        }
        CONSTANTS.minResponseList.push(minResTo);
    }
    //Set Phone screen details
    saveobj.phoneScreenStat = CONSTANTS.phoneScrnStatusDtls[$("#scrnstatusSelect option:selected").index()].phoneScrnStatusCode;
    //Set Interview screen details
    saveobj.intviewStat = CONSTANTS.getInterviewStatusDtls[$("#intrstatusSelect option:selected").index()].status;
    //Set Phone Screen dispositionStatus
    saveobj.phnScrnDispCode = CONSTANTS.getPhoneScrnDispositionStatusDtls[$("#selectDisposition option:selected").index()].status;

    // Added for SIR 6018
    if (saveobj.intviewStat === "11" && CONSTANTS.phnScrnStaffingList[0].hrgEvntFlg === "N") {
        // Interview Scheduled, Set to "Send Calendar Packet"
        saveobj.intviewMatStat = "2";
    } else if (saveobj.intviewStat === "12" && CONSTANTS.phnScrnStaffingList[0].hrgEvntFlg === "N") {
        // Store Scheduling, Set to "Send Email Packet"
        saveobj.intviewMatStat = "3";
    } else {
        // Save as is
        saveobj.intviewMatStat = CONSTANTS.phnScreenDtls[0].interviewMaterialStatusCode;
    }

    // Set material status to "Calendar Packet Sent" whenever it is a
    // Hiring Event and Interview Status is
    // Interview Scheduled or Store Scheduled.
    if (CONSTANTS.phnScreenDtls[0].hrEventFlg === "Y" && (saveobj.intviewStat === "11" || saveobj.intviewStat === "12")) {
        // Hiring Event, Set to "Calendar Packet Sent"
        saveobj.intviewMatStat = "1";
    }

    // ******************************

    saveobj.store = CONSTANTS.phnScreenReqDtls[0].strNbr;

    if ($("#intDate").val() && $("#intDate").val() !== "") {
        var valuesOfDate = [];
        valuesOfDate = $("#intDate").val().split("/");
        var dateobj = {};
        dateobj.month = valuesOfDate[0];
        dateobj.day = valuesOfDate[1];
        dateobj.year = valuesOfDate[2];
        saveobj.intrwDateVO = dateobj;
        var temp = 0;
        var hour, minute, timeobj = {};
        if ($("#intrwTimeHr").val() && $("#intrwTimeHr").val() !== "" && $("#intrwTimeMin").val() && $("#intrwTimeMin").val() !== "") {
            if ($("#amPMIntrw option:selected").index() === 1) {
                temp = parseInt($("#intrwTimeHr").val());
                // Added to handle the sending of 24 hour format in flex
                // 1:00 AM is send as 1:00 and so on
                // but 12:59 AM is sent as 24:59
                temp = (temp === 12) ? 0 : temp;
                hour = temp.toString();
                minute = $("#intrwTimeMin").val();
                timeobj = {}; // :TimeStampVO
                timeobj.month = valuesOfDate[0];
                timeobj.day = valuesOfDate[1];
                timeobj.year = valuesOfDate[2];
                timeobj.hour = hour;
                timeobj.minute = minute;
                timeobj.second = "0";
                timeobj.milliSecond = "0";
                timeobj.tmFrmt = 0;
                saveobj.intrwTimeVo = timeobj;
            } else if ($("#amPMIntrw option:selected").index() === 2) {
                temp = parseInt($("#intrwTimeHr").val());
                // Added to handle the sending of 24 hour format in flex
                // 1:00 PM is send as 13:00 and so on
                // but 12:59 PM is sent as 12:59
                temp = (temp !== 12) ? (temp + 12) : temp;
                hour = temp.toString();
                minute = intrwTimeMin.text;
                timeobj = {}; // :TimeStampVO
                timeobj.month = valuesOfDate[0];
                timeobj.day = valuesOfDate[1];
                timeobj.year = valuesOfDate[2];
                timeobj.hour = hour;
                timeobj.minute = minute;
                timeobj.second = "0";
                timeobj.milliSecond = "0";
                timeobj.tmFrmt = 0;
                saveobj.intrwTimeVo = timeobj;
            }
        }
        saveobj.intrwDate = $("#intDate").val();
    }

    // setting the phone screen time and date
    if ($("#minReq1 option:selected").index() !== 0 && ($("#txtAreaPhnDetRes").val() !== "" && $("#txtAreaPhnDetRes").val().trim().length > 0)) {
        // ***** Also need to add Phone Status being "Complete" or "Favorable"
        CONSTANTS.phnScrnCompleteDate = CONSTANTS.phnScrnTimeList[0].formattedDate;
        CONSTANTS.phnScrnCompleteTime = CONSTANTS.phnScrnTimeList[0].formattedTime;

        // Date/Time has not ever been set
        if (CONSTANTS.phnScrnCompleteDate === null || CONSTANTS.phnScrnCompleteDate === "") {
            var currentDate = new Date();
            var dateFormatted = (("0" + (currentDate.getMonth() + 1)).slice(-2)) + '/' + ("0" + currentDate.getDate()).slice(-2) + '/' + currentDate.getFullYear();
            var hours = currentDate.getHours();
            var minutes = currentDate.getMinutes();
            var timeFormatted = hours + ':' + minutes;

            var valuesOfPhnDate = [];
            valuesOfPhnDate = dateFormatted.split("/");
            var phnScrndateobj = {}; // :DateVO
            phnScrndateobj.month = valuesOfPhnDate[0];
            phnScrndateobj.day = valuesOfPhnDate[1];
            phnScrndateobj.year = valuesOfPhnDate[2];
            saveobj.PhnScrnDateVo = phnScrndateobj;
            //Set phone time value
            var valuesOfPhnTime = [];
            valuesOfPhnTime = timeFormatted.split(":");
            timeobj = {}; // :TimeStampVO
            timeobj.month = valuesOfPhnDate[0];
            timeobj.day = valuesOfPhnDate[1];
            timeobj.year = valuesOfPhnDate[2];
            timeobj.hour = valuesOfPhnTime[0];
            timeobj.minute = valuesOfPhnTime[1];
            timeobj.second = "0";
            timeobj.milliSecond = "0";
            timeobj.tmFrmt = 0;
            saveobj.phnScrnTimeVo = timeobj;
            saveobj.phnScreener = CONSTANTS.userProfile.userId;
        } else {
            // Date/Time has already been set, Don't want a new
            // date/time use the previous set
            valuesOfPhnDate = [];
            valuesOfPhnDate = CONSTANTS.phnScrnCompleteDate.split("/");
            phnScrndateobj = {};
            phnScrndateobj.month = valuesOfPhnDate[0];
            phnScrndateobj.day = valuesOfPhnDate[1];
            phnScrndateobj.year = valuesOfPhnDate[2];
            saveobj.PhnScrnDateVo = phnScrndateobj;
            //Set phone time value
            valuesOfPhnTime = [];
            valuesOfPhnTime = CONSTANTS.phnScrnCompleteTime.split(":");
            timeobj = {};
            timeobj.month = valuesOfPhnDate[0];
            timeobj.day = valuesOfPhnDate[1];
            timeobj.year = valuesOfPhnDate[2];
            timeobj.hour = CONSTANTS.phnScrnTimeList[0].hour;
            timeobj.minute = CONSTANTS.phnScrnTimeList[0].minute;
            timeobj.second = "0";
            timeobj.milliSecond = "0";
            timeobj.tmFrmt = 0;
            saveobj.phnScrnTimeVo = timeobj;
            saveobj.phnScreener = $(".screener").text();
        }
    }
    return saveobj;
};
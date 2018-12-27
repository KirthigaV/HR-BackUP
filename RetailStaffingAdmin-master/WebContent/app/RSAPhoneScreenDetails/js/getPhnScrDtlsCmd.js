/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getPhnScrDtlsCmd.js
 * Application: Retail Staffing Admin
 *
 *This getPhnScrDtlsCmd.js will help to construct the json response from service to the display the components in UI
 */
function getPhnScrDtlsCmd(json) {
    //Variable declaration
    var resultList = [];
    var resultList2 = [];
    var resultListReq = [];
    var resultStoreList = [];
    var phnScrnDtl = {};
    var phnScrnReqDtl = {};
    var phnScrnStrDtls = {};
    var resultMinList = [];
    var minResTo = {};
    var resultIntrwList = [];
    var intrwDtls = {};
    var timeVO = {};
    var dateVO = {};
    var timeArray = [];
    var dateArray = [];
    var resultStaffList = [];
    var staffDtl = {};
    var resultListInActPhnScrns = [];
    var inActPhnScrnDtl = {};
    var loadPhoneScrnStatus = {};
    var loadInterviewStatus = {};
    var loadPhoneScrnDispositionStatus = {};
    var langSkls = {};
    
    var resultCallDetailHistList = []; 
    
    // to remove the already loaded ITI Details
    CONSTANTS.phnScreenDtls = [];
    CONSTANTS.phnScreenIntrwDtls = [];
    CONSTANTS.phnScreenReqDtls = [];
    CONSTANTS.phnScreenReqStoreDtls = [];
    CONSTANTS.minResponseList = [];
    CONSTANTS.intrwDateList = [];
    CONSTANTS.intrwTimeList = [];
    CONSTANTS.phnScrnStaffingList = [];
    CONSTANTS.LoadREQDGDltls = [];
    CONSTANTS.phnScrnTimeList = [];
    CONSTANTS.phnScrnTimeList = [];
    CONSTANTS.intrDat = null;
    CONSTANTS.phnScrnDat = {};
    CONSTANTS.phnUsrId = "";
    CONSTANTS.getWeekAvail = "";
    //Array declaration
    CONSTANTS.getInActPhnScrnDtls = [];
    CONSTANTS.getTotalPhnScrnDtls = [];
    CONSTANTS.getAuthPosCnt = 0;
    CONSTANTS.phoneScrnStatusDtls = [];
    CONSTANTS.getInterviewStatusDtls = [];
    CONSTANTS.getPhoneScrnDispositionStatusDtls = [];
    CONSTANTS.phnScrnStatusTimestamp = [];
    CONSTANTS.interviewStatusTimestamp = [];
    CONSTANTS.schdPrefDetail = [];
    CONSTANTS.languageSkls = [];
    CONSTANTS.reqnLanguagesArr = [];
    CONSTANTS.isExpandedPhoneScreen = false;
    CONSTANTS.callHistory = [];
    
    
    /*
     * Iterate and set Status code and description
     */
    this.simpleCheckAndStore = function(resultList, objName, statusCode, modelObj) {
        if (resultList && resultList.length > 0) {
            // iterating the result list to get each item and set in
            // to CONSTANTS locator
            for ( var i = 0; i < resultList.length; i++) {
                objName = {};
                objName[statusCode] = resultList[i].displayStatusCode;
                objName.desc = resultList[i].statusDescription;
                modelObj.push(objName);
            }
            //Set default sort by description
            modelObj = _.sortBy(modelObj, "desc");
        }
    };
    /*
     * Iterate and set disposition code and description to get phone screen disposition status
     */
    this.phnScrnDispCheckAndStore = function(resultList) {
        // check whether the result list is having any value object
        // or not
        if (resultList && resultList.length > 0) {
            // iterating the result list to get each item and set in
            // to CONSTANTS locator
            for ( var x = 0; x < resultList.length; x++) {
                loadPhoneScrnDispositionStatus = {};
                loadPhoneScrnDispositionStatus.status = resultList[x].dispositionCode;
                loadPhoneScrnDispositionStatus.desc = resultList[x].dispositionDesc;
                CONSTANTS.getPhoneScrnDispositionStatusDtls.push(loadPhoneScrnDispositionStatus);
            }
        }
    };
    /*
     * This function is used to check if minimum response details is available and set the results accordingly
     */
    this.minResponseCheckAndStore = function() {
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList && json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList[0].hasOwnProperty("MinimumResponseDtl")) {
            resultMinList = UTILITY.checkAndStoreAsArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList[0].MinimumResponseDtl);
        }
        if (resultMinList && resultMinList.length > 0) {
            //Set minimum response,Sequence number
            for ( var i = 0; i < resultMinList.length; i++) {
                minResTo = {};
                minResTo.minimumResponse = resultMinList[i].minimumResponse;
                minResTo.seqNbr = resultMinList[i].seqNbr;
                CONSTANTS.minResponseList.push(minResTo);
            }
            // Added new Requisition Specific Questions to phone
            // screen, set variable so that will know if a new or
            // old phone screen
            if (CONSTANTS.minResponseList.length > 10) {
                CONSTANTS.isExpandedPhoneScreen = true;
            } else {
                CONSTANTS.isExpandedPhoneScreen = false;
            }
        }
    };
    /*
     * Check and set phone screen interview date and time details
     */
    this.intrwDateTimeCheckAndStore = function() {
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("interviewDate")) {
            dateArray = UTILITY.checkAndStoreAsArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewDate);
            if (dateArray) {
                //Set formatted date and time
                for ( var j = 0; j < dateArray.length; j++) {
                    dateVO = {};
                    dateVO.month = dateArray[j].month;
                    dateVO.day = dateArray[j].day;
                    dateVO.year = dateArray[j].year;
                    CONSTANTS.intrDat = dateArray[j].formattedDate;
                    CONSTANTS.intrwDateList.push(dateVO);
                }
            }
        }
        //Check Interview time and store
        this.intrwTimeCheckAndStore();
    };
    /*
     * check interview time and store details
     */
    this.intrwTimeCheckAndStore = function() {
        if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.hasOwnProperty("interviewTime")) {
            timeArray = UTILITY.checkAndStoreAsArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewTime);
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
                    CONSTANTS.intrwTimeList.push(timeVO);
                }
            }
        }
    };
    /*
     * Check Phone screen interview details if exist and save
     */
    this.phnScreenIntrwDtlsCheckAndStore = function() {
        resultIntrwList = UTILITY.checkAndStoreAsArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls);
        if (resultIntrwList && resultIntrwList.length > 0) {
            for ( var l = 0; l < resultIntrwList.length; l++) {
                intrwDtls = {};
                intrwDtls.intrwDate = "";
                //Display formatted time
                intrwDtls.intrwTime = $.isEmptyObject(timeVO) ? "" : json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls.interviewTime.formattedTime;
                intrwDtls.interViewer = resultIntrwList[l].interviewer;
                intrwDtls.interTyp = resultIntrwList[l].interviewLocTypCd;
                intrwDtls.intrLocName = resultIntrwList[l].interviewLocName;
                intrwDtls.intrLocAddr = resultIntrwList[l].add;
                intrwDtls.intrLocCity = resultIntrwList[l].city;
                intrwDtls.intrLocState = resultIntrwList[l].state;
                intrwDtls.intrLocZip = resultIntrwList[l].zip;
                intrwDtls.intrLocPhn = resultIntrwList[l].phone;
                CONSTANTS.phnScreenIntrwDtls.push(intrwDtls);
            }
        }
    };
    /*
     * Check if phone screen details available and save
     */
    this.phnScreenDtlsCheckAndStore = function(resultList) {
        for ( var n = 0; n < resultList.length; n++) {
            //Construct phone screen details
            phnScrnDtl = {};
            phnScrnDtl.phoneScreenStatusCode = resultList[n].phoneScreenStatusCode;
            phnScrnDtl.cndtNbr = resultList[n].cndtNbr;
            phnScrnDtl.aid = resultList[n].aid;
            phnScrnDtl.reqNbr = resultList[n].reqNbr;
            phnScrnDtl.ScreenNbr = resultList[n].itiNbr;
            phnScrnDtl.canName = resultList[n].name;
            phnScrnDtl.internalExternal = resultList[n].internalExternal;
            phnScrnDtl.canPhn = resultList[n].canPhn;
            phnScrnDtl.strNbr = resultList[n].cndStrNbr;
            phnScrnDtl.dept = resultList[n].cndDeptNbr;
            phnScrnDtl.candTitle = resultList[n].title;
            phnScrnDtl.phnScrnDate = resultList[n].phnScrnDate;
            phnScrnDtl.phnScrnTime = resultList[n].phnScrnTime;
            phnScrnDtl.contactHist = resultList[n].contactHistoryTxt;
            phnScrnDtl.ynstatus = resultList[n].ynstatus;
            phnScrnDtl.detailResp = resultList[n].detailTxt;
            phnScrnDtl.overAllStatus = resultList[n].overAllStatus;
            phnScrnDtl.hrEventFlg = resultList[n].hrEvntFlg;
            phnScrnDtl.ynStatusdesc = resultList[n].ynStatusDesc;
            phnScrnDtl.intrwTyp = resultList[n].interviewLocTypCd;
            phnScrnDtl.phnScreener = resultList[n].phnScreener;
            phnScrnDtl.scrTyp = resultList[n].scrTyp;
            phnScrnDtl.emailAddr = resultList[n].emailAdd;
            phnScrnDtl.interviewStatusCode = resultList[n].interviewStatusCode;
            phnScrnDtl.interviewMaterialStatusCode = resultList[n].interviewMaterialStatusCode;
            // Added by p22o0mn for Defect #3323: New field added to
            // Get the Flag for enough Phone Screens Completed or
            // not for a given requisition
            phnScrnDtl.phoneScreensCompletedFlg = resultList[n].phoneScreensCompletedFlg;
            phnScrnDtl.candRefNbr = resultList[n].candRefNbr;
            phnScrnDtl.phoneScreenDispositionCode = resultList[n].phoneScreenDispositionCode;
            phnScrnDtl.phoneScreenBannerNum = resultList[n].phoneScreenBannerNum;
            // Added to implement for the phone screen date and time
            // to be saved once
            if (resultList[n].phnScrnDate) {
                dateVO = {};
                dateVO.month = resultList[n].phnScrnDate.month;
                dateVO.day = resultList[n].phnScrnDate.day;
                dateVO.year = resultList[n].phnScrnDate.year;
                //Format date
                var dateValues = new Date(parseInt(resultList[n].phnScrnDate.year), parseInt((resultList[n].phnScrnDate.month) - 1), parseInt(resultList[n].phnScrnDate.day));
                CONSTANTS.phnScrnDat = dateValues;
                //Format time
                if (resultList[n].phnScrnTime) {
                    timeVO = {};
                    timeVO.month = resultList[n].phnScrnTime.month;
                    timeVO.day = resultList[n].phnScrnTime.day;
                    timeVO.year = resultList[n].phnScrnTime.year;
                    timeVO.hour = resultList[n].phnScrnTime.hour;
                    timeVO.minute = resultList[n].phnScrnTime.minute;
                    timeVO.second = resultList[n].phnScrnTime.second;
                    timeVO.milliSecond = resultList[n].phnScrnTime.milliSecond;
                    timeVO.formattedTime = resultList[n].phnScrnTime.formattedTime;
                    timeVO.formattedDate = resultList[n].phnScrnTime.formattedDate;
                    timeVO.tmFrmt = 0;
                    CONSTANTS.phnScrnTimeList.push(timeVO);
                }
            }
            // Phone Screen Status Time Stamp
            if (resultList[n].phoneScreenStatusTimestamp) {
                timeVO = {};
                timeVO.month = resultList[n].phoneScreenStatusTimestamp.month;
                timeVO.day = resultList[n].phoneScreenStatusTimestamp.day;
                timeVO.year = resultList[n].phoneScreenStatusTimestamp.year;
                timeVO.hour = resultList[n].phoneScreenStatusTimestamp.hour;
                timeVO.minute = resultList[n].phoneScreenStatusTimestamp.minute;
                timeVO.second = "00";
                timeVO.milliSecond = "000";
                timeVO.tmFrmt = 0;
                timeVO.formattedDate = resultList[n].phoneScreenStatusTimestamp.formattedDate;
                timeVO.formattedTime = resultList[n].phoneScreenStatusTimestamp.formattedTime;
                CONSTANTS.phnScrnStatusTimestamp.push(timeVO);
            }
            // Interview Status Time Stamp
            if (resultList[n].interviewStatusTime) {
                timeVO = {};
                timeVO.month = resultList[n].interviewStatusTime.month;
                timeVO.day = resultList[n].interviewStatusTime.day;
                timeVO.year = resultList[n].interviewStatusTime.year;
                timeVO.hour = resultList[n].interviewStatusTime.hour;
                timeVO.minute = resultList[n].interviewStatusTime.minute;
                timeVO.second = "00";
                timeVO.milliSecond = "000";
                timeVO.tmFrmt = 0;
                timeVO.formattedDate = resultList[n].interviewStatusTime.formattedDate;
                timeVO.formattedTime = resultList[n].interviewStatusTime.formattedTime;
                CONSTANTS.interviewStatusTimestamp.push(timeVO);
            }
            //Update details to object
            CONSTANTS.phnScreenDtls.push(phnScrnDtl);
        }
    };
    /*
     * Check the scheduled preference specific day selected by user
     */
    this.schdPrefSetSpecificDays = function(resultList2) {
        switch (parseInt(resultList2.wkDayNbr)) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            CONSTANTS.schdPrefArr[0] = true;
            // 1 - 5 = Weekdays
            break;
        case 6:
            CONSTANTS.schdPrefArr[1] = true;
            // 6 = Saturday
            break;
        case 7:
            CONSTANTS.schdPrefArr[2] = true;
            // 7 = Sunday
            break;
        default:
            break;
        }
    };
    /*
     * Check the scheduled preference specific time selected by user
     */
    this.schdPrefSetSpecificTime = function(resultList2) {
        switch (parseInt(resultList2.daySegCd)) {
        case 0:
            CONSTANTS.schdPrefArr[3] = true;
            // 1 = Early AM
            CONSTANTS.schdPrefArr[4] = true;
            // 2 = Mornings
            CONSTANTS.schdPrefArr[5] = true;
            // 3 = Afternoon
            CONSTANTS.schdPrefArr[6] = true;
            // 4 = Nights
            CONSTANTS.schdPrefArr[7] = true;
            // 5 = Late Night
            CONSTANTS.schdPrefArr[8] = true;
            // 6 = Overnight
            break;
        case 1:
            CONSTANTS.schdPrefArr[3] = true;
            // 1 = Early/ AM
            break;
        case 2:
            CONSTANTS.schdPrefArr[4] = true;
            // 2 = Mornings
            break;
        case 3:
            CONSTANTS.schdPrefArr[5] = true;
            // 3 = Afternoon
            break;
        case 4:
            CONSTANTS.schdPrefArr[6] = true;
            // 4 = Nights
            break;
        case 5:
            CONSTANTS.schdPrefArr[7] = true;
            // 5 = Late Night
            break;
        case 6:
            CONSTANTS.schdPrefArr[8] = true;
            // 6 = Overnight
            break;
        default:
            break;
        }
    };
    /*
     * Check the scheduled preference data and save it as array
     */
    this.schdPrefArrCheckAndStore = function(resultListReq) {
        for ( var j = 0; j < CONSTANTS.NUM_SCHE_PREF_ENTRIES; j++) {
            for ( var k = 0; k < CONSTANTS.NUM_DAYS_OF_WEEK; k++) {
                CONSTANTS.schdPrefArr.push(false);
            }
        }
        // Get the Schedule Preference values selected from the
        // Staffing Request Form
        if (resultListReq.hasOwnProperty("requisitionSchedulePreference") && resultListReq.requisitionSchedulePreference[0].hasOwnProperty("schdPrefDetail")) {
            // check whether schdPrefDetail is an array
            // collection
            resultList2 = UTILITY.checkAndStoreAsArray(resultListReq.requisitionSchedulePreference[0].schdPrefDetail);
            CONSTANTS.schdPrefDetail = resultList2;
            for ( var l = 0; l < resultList2.length; l++) {
                // Set the Specific Days
                this.schdPrefSetSpecificDays(resultList2[l]);

                // Set the Specific Times. We no longer use 0 -
                // Anytime, therefore ignore it. However we will
                // need it for old Requisitions
                this.schdPrefSetSpecificTime(resultList2[l]);
            }
        }
    };
    /*
     * Check the languages selected by user and save
     */
    this.reqnLanguagesCheckAndStore = function() {
    	CONSTANTS.reqnLanguagesArr = [];
    	if(json.Response && json.Response.RequisitionDetailList && json.Response.RequisitionDetailList.RequisitionDetail && json.Response.RequisitionDetailList.RequisitionDetail.requisitionLanguagePreference)
    		{
    		if (_.isArray(json.Response.RequisitionDetailList.RequisitionDetail.requisitionLanguagePreference[0].LangSklsDetail)) {
                resultList2 = json.Response.RequisitionDetailList.RequisitionDetail.requisitionLanguagePreference[0].LangSklsDetail;
            } else {
                if (json.Response.RequisitionDetailList.RequisitionDetail.requisitionLanguagePreference[0].LangSklsDetail) {
                    resultList2 = [ json.Response.RequisitionDetailList.RequisitionDetail.requisitionLanguagePreference[0].LangSklsDetail ];
                }
            }        
        for ( var l = 0; l < resultList2.length; l++) {
            CONSTANTS.reqnLanguagesArr.push(resultList2[l].langCode);
        }
    		}
    };
    /*
     * Check the languages skills selected by user iterate and form array structure
     */
    this.languageSklsCheckAndStore = function() {
        // check whether LangSklsList is an array collection
        resultList = UTILITY.checkAndStoreAsArray(json.Response.LangSklsList.LangSklsDetail);

        // check whether the result list is having any value object
        // or not
        if (resultList && resultList.length > 0) {
            // iterating the result list to get each item and set in
            // to CONSTANTS locator
            for ( var x = 0; x < resultList.length; x++) {
                langSkls = {};
                langSkls.langCode = resultList[x].langCode;
                langSkls.dsplyDesc = resultList[x].dsplyDesc;
                langSkls.selectLang = false;
                CONSTANTS.languageSkls.push(langSkls);
            }
            // Set the languages for the requsition
            this.setLangSklsForReqn();
        }
    };
    /*
     * Set Language Skills for selected requisition
     */
    this.setLangSklsForReqn = function() {
        for ( var t = 0; t < CONSTANTS.languageSkls.length; t++) {
            for ( var s = 0; s < CONSTANTS.reqnLanguagesArr.length; s++) {
                if (CONSTANTS.languageSkls[t].langCode === CONSTANTS.reqnLanguagesArr[s]) {
                    CONSTANTS.languageSkls[t].selectLang = true;
                }
            }
        }
    };
    /*
     * Set Inactive phonescreen details and save
     */
    this.inActPhnScrnDtlsCheckAndStore = function() {
        for ( var k = 0; k < CONSTANTS.getTotalPhnScrnDtls.length; k++) {
            var flag = 0;
            for ( var b = 0; b < CONSTANTS.getRewPhnScrnDynPanelDtls.length; b++) {
                //set flag as 1 if phonescreen number matches
                if (CONSTANTS.getRewPhnScrnDynPanelDtls[b].phnScrnNo === CONSTANTS.getTotalPhnScrnDtls[k].itiNbr) {
                    flag = 1;
                    break;

                }
            }
            if (flag === 0) {
                //get interview phone screen details
                CONSTANTS.getInActPhnScrnDtls.push(CONSTANTS.getTotalPhnScrnDtls[k]);
            }
        }
    };

    // check for the status tag in the response
    if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.SUCCESS) {
        // check for the StatusList tag in the response
        if (json.Response.hasOwnProperty("StatusList")) {
            // Phone Screen Statuses
            if (json.Response.StatusList.hasOwnProperty("phoneScreenStats")) {
                // check whether PhoneScreenStatus is an array collection
                resultList = UTILITY.checkAndStoreAsArray(json.Response.StatusList.phoneScreenStats[0].status);
                // check whether the result list is having any value object
                // or not
                this.simpleCheckAndStore(resultList, loadPhoneScrnStatus, "phoneScrnStatusCode", CONSTANTS.phoneScrnStatusDtls);
            }

            // Interview Statuses
            if (json.Response.StatusList.hasOwnProperty("interviewStats")) {
                // check whether PhoneScreenStatus is an array collection
                resultList = UTILITY.checkAndStoreAsArray(json.Response.StatusList.interviewStats[0].status);
                // check whether the result list is having any value object
                // or not
                this.simpleCheckAndStore(resultList, loadInterviewStatus, "status", CONSTANTS.getInterviewStatusDtls);
            }
        }

        // Phone Screen Disposition Statuses
        resultList = UTILITY.checkForArrayAndStoreAsArray(json.Response, "PhoneScreenDispositionList", "PhoneScreenDispositionDetail");
        // check whether the result list is having any value object
        // or not
        this.phnScrnDispCheckAndStore(resultList);

        // checking for the authorisation position count
        if (json.Response.hasOwnProperty("authCnt")) {
            CONSTANTS.getAuthPosCnt = json.Response.authCnt;
        }
        // checking for the CmplITIDeatil tag in the response
        resultListInActPhnScrns = UTILITY.checkForArrayAndStoreAsArray(json.Response, "CmplITIDetail", "PhoneScreenIntrwDetail");
        if (resultListInActPhnScrns && resultListInActPhnScrns.length > 0) {
            // iterating the result list to get each item and set in to
            // CONSTANTS locator
            for ( var n = 0; n < resultListInActPhnScrns.length; n++) {
                inActPhnScrnDtl = {};
                inActPhnScrnDtl.itiNbr = resultListInActPhnScrns[n].itiNbr;
                inActPhnScrnDtl.overAllStatus = resultListInActPhnScrns[n].overAllStatus;
                inActPhnScrnDtl.interviewStatusCode = resultListInActPhnScrns[n].interviewStatusCode;
                inActPhnScrnDtl.interviewMatStatusCode = resultListInActPhnScrns[n].interviewMaterialStatusCode;
                CONSTANTS.getTotalPhnScrnDtls.push(inActPhnScrnDtl);
            }
        }
        // check for the ITIDetailList tag in the response
        if (json.Response.hasOwnProperty("ITIDetailList")) {
            // check whether PhoneScreenIntrwDetail is an array collection
            resultList = UTILITY.checkAndStoreAsArray(json.Response.ITIDetailList.PhoneScreenIntrwDetail);
            if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("MinimumResponseDtlList")) {
                this.minResponseCheckAndStore();
            }

            // added for date to
            if (json.Response.ITIDetailList.hasOwnProperty("PhoneScreenIntrwDetail")) {
                this.intrwDateTimeCheckAndStore();
            }

            if (json.Response.ITIDetailList.PhoneScreenIntrwDetail.hasOwnProperty("InterviewLocDtls") && json.Response.ITIDetailList.PhoneScreenIntrwDetail.InterviewLocDtls) {
                this.phnScreenIntrwDtlsCheckAndStore();
            }

            // check whether the result list is having any value object or
            // not
            if (resultList && resultList.length > 0) {
                // iterating the result list to get each item and set in to
                // CONSTANTS locator
                this.phnScreenDtlsCheckAndStore(resultList);
            }
        }

        // //check for the RequisitionDetailList tag in the response
        if (json.Response.hasOwnProperty("RequisitionDetailList")) {
            // check whether RequisitionDetail is an array collection
            resultListReq = UTILITY.checkAndStoreAsArray(json.Response.RequisitionDetailList.RequisitionDetail);
        }

        // check whether the result list is having any value object or not
        if (resultListReq && resultListReq.length > 0) {
            // iterating the result list to get each item and set in to
            // CONSTANTS locator
            for ( var a = 0; a < resultListReq.length; a++) {
                phnScrnReqDtl = {};
                phnScrnReqDtl.openings = null;
                phnScrnReqDtl.fillDt = null;
                phnScrnReqDtl.creator = null;
                phnScrnReqDtl.dateCreated = null;
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
                phnScrnReqDtl.intrvDuration = resultListReq[a].intrvDuration;
                phnScrnReqDtl.reqNumInterviews = resultListReq[a].reqNumInterviews;
                phnScrnReqDtl.offerExtCandCnt = resultListReq[a].offerExtCandCnt;// Deferred
                // 3373
                phnScrnReqDtl.authPosCount = resultListReq[a].authPosCount;
                phnScrnReqDtl.interviewRemainingCandCnt = resultListReq[a].interviewRemainingCandCnt;// Deferred
                // 3373
                phnScrnReqDtl.requisitionCalendarName = null;
                phnScrnReqDtl.rscToManageFlg = resultListReq[a].rscToManageFlg;
                phnScrnReqDtl.reqCandidateType = resultListReq[a].reqCandidateType;
                phnScrnReqDtl.isDrivingPosition = resultListReq[a].isDrivingPosition;

                CONSTANTS.phnScreenReqDtls.push(phnScrnReqDtl);

                // Added as part of Auto Attach
                // Load the Schedule Preference Array with all false values
                CONSTANTS.schdPrefArr = [];
                this.schdPrefArrCheckAndStore(resultListReq[a]);

                CONSTANTS.reqnLanguagesArr = [];
                // Get the Language Preference values selected from the
                // Staffing Request Form
                this.reqnLanguagesCheckAndStore();
            }
        }

        if (json.Response.hasOwnProperty("LangSklsList") && json.Response.LangSklsList) {
            this.languageSklsCheckAndStore();
        }

        // check for the StoreDetailList tag in the response
        if (json.Response.hasOwnProperty("StoreDetailList") && json.Response.StoreDetailList && json.Response.StoreDetailList.hasOwnProperty("StoreDetails")) {
            // check whether StoreDetailList is an array collection
            resultStoreList = UTILITY.checkAndStoreAsArray(json.Response.StoreDetailList.StoreDetails);
        }
        // check whether the result list is having any value object or not
        if (resultStoreList && resultStoreList.length > 0) {
            // iterating the result list to get each item and set in to
            // CONSTANTS locator
            for ( var b = 0; b < resultStoreList.length; b++) {
                phnScrnStrDtls = {};
                phnScrnStrDtls.addr = resultStoreList[b].add;
                phnScrnStrDtls.city = resultStoreList[b].city;
                phnScrnStrDtls.state = resultStoreList[b].state;
                phnScrnStrDtls.zip = resultStoreList[b].zip;
                phnScrnStrDtls.phone = resultStoreList[b].phone;
                phnScrnStrDtls.storeMgr = resultStoreList[b].strMgr;
                phnScrnStrDtls.strName = resultStoreList[b].strName;
                phnScrnStrDtls.timeZoneCode = resultStoreList[b].timeZoneCode;
                CONSTANTS.phnScreenReqStoreDtls.push(phnScrnStrDtls);
            }
        }

        // check for the staffingDetails tag in the response
        if (json.Response.hasOwnProperty("staffingDetails")) {
            // check whether staffingDetails is an array collection
            resultStaffList = UTILITY.checkAndStoreAsArray(json.Response.staffingDetails);
            // check whether the result list is having any value object or
            // not
            if (resultStaffList && resultStaffList.length > 0) {
                // CONSTANTS locator
                staffDtl = {};
                staffDtl.add = resultStaffList[0].add;
                staffDtl.breaks = resultStaffList[0].breaks;
                staffDtl.city = resultStaffList[0].city;
                staffDtl.daysTmMgrAvble = resultStaffList[0].daysTmMgrAvble;
                staffDtl.desiredExp = resultStaffList[0].desiredExp;
                staffDtl.end = resultStaffList[0].end;
                staffDtl.hrgMgrName = resultStaffList[0].hrgMgrName;
                staffDtl.hrgMgrPhn = resultStaffList[0].hrgMgrPhn;
                staffDtl.hrgMgrTtl = resultStaffList[0].hrgMgrTtl;
                staffDtl.interviewDurtn = resultStaffList[0].interviewDurtn;
                staffDtl.interviewTmSlt = resultStaffList[0].interviewTmSlt;
                staffDtl.lastIntrTm = null;
                staffDtl.lunch = resultStaffList[0].lunch;
                staffDtl.requestNbr = resultStaffList[0].requestNbr;
                staffDtl.requisitionStatus = resultStaffList[0].requisitionStatus;
                staffDtl.sealTempJob = null;
                staffDtl.start = resultStaffList[0].start;
                staffDtl.state = resultStaffList[0].state;
                staffDtl.stfHrgEvntStartDt = resultStaffList[0].stfHrgEvntStartDt;
                staffDtl.stfHrgEvntEndDt = resultStaffList[0].stfHrgEvntEndDt;
                staffDtl.stfhrgEvntLoc = resultStaffList[0].stfhrgEvntLoc;
                staffDtl.stfhrgEvntLocPhn = resultStaffList[0].stfhrgEvntLocPhn;
                staffDtl.stfReqNbr = resultStaffList[0].stfReqNbr;
                staffDtl.targetPay = resultStaffList[0].targetPay;
                staffDtl.weekMgrAvble = resultStaffList[0].weekMgrAvble;
                staffDtl.zip = resultStaffList[0].zip;
                staffDtl.Referals = resultStaffList[0].Referals;
                staffDtl.qualPoolNts = resultStaffList[0].qualPoolNts;
                staffDtl.hrgEvntFlg = resultStaffList[0].hrgEvntFlg;
                staffDtl.hireEvntid = resultStaffList[0].hiringEventID;
                staffDtl.hireEventType = null;
                staffDtl.hireEventMgrAssociateId = null;
                //set the value back to object
                CONSTANTS.phnScrnStaffingList.push(staffDtl);
            }
            // Added for the inclusion of the week available
            if (json.Response.staffingDetails.hasOwnProperty("weekBeginDt")) {
                CONSTANTS.getWeekAvail = json.Response.staffingDetails.weekBeginDt.month + "/" + json.Response.staffingDetails.weekBeginDt.day + "/" + json.Response.staffingDetails.weekBeginDt.year;
            }

        }        
        
        // check for the PhoneScreenCallHistoryDetail tag in the response
        if (json.Response.hasOwnProperty("PhoneScreenCallHistoryList")) {
        	if (json.Response.PhoneScreenCallHistoryList.hasOwnProperty("PhoneScreenCallHistoryDetail")) {
        		// check whether PhoneScreenCallHistoryDetail is an array collection
        		resultCallDetailHistList = UTILITY.checkAndStoreAsArray(json.Response.PhoneScreenCallHistoryList.PhoneScreenCallHistoryDetail);
        		// check whether the result list is having any value object or
        		// not
        		if (resultCallDetailHistList && resultCallDetailHistList.length > 0) {
        			for (var i = 0; i < resultCallDetailHistList.length; i++) { 
        				CONSTANTS.callHistory.push(resultCallDetailHistList[i]);
        			}
        		} 
        	}
        } else {
        	CONSTANTS.callHistory = [];
        }
        
        //GEt phonescreen details
        if (CONSTANTS.getRewPhnScrnDynPanelDtls && CONSTANTS.getRewPhnScrnDynPanelDtls.length > 0 && CONSTANTS.getTotalPhnScrnDtls && CONSTANTS.getTotalPhnScrnDtls.length > 0) {
            this.inActPhnScrnDtlsCheckAndStore();
        }
    }
    // checking for the error
    else if (json.Response.hasOwnProperty("error")) {
        $('#alertPopupModal').modal('show');
        $(".alertModalBody").text(json.Response.error.errorMsg);
        $("#alertPopupModal").modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $("#alertPopupModal").draggable({
            handle : ".modal-header"
        });
        // make the success as false. to ensure you are not showing the success accident.
        CONSTANTS.SUCCESS_ON_HOME = false;
        UTILITY.RemoveQS();
        $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.phonescreenDetailsObj.setContent);
    }

    // Empty CTI Variables
    // empty the CTI variables.
    if (CONSTANTS.CallType === "PHNSCRN") {
        CONSTANTS.ConfirmationNo = 0;
        CONSTANTS.CallType = "";
    }

    return CONSTANTS;
};
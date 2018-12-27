 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getReqScrDtlsCmd.js
 * Application: Retail Staffing Admin
 *
 */
function getReqScrDtlsCmd(response) {
    var res = response.Response;
    var resultList = [];
    /*
     * This function initializes model variables
     *
     * @param N/A
     * @return N/A
     */
    this.initializeModelVariables = function() {
        this.reqStrDet = [];
        this.LoadREQDGDltls = [];
        this.schdPrefArr = [];
        this.reqnLanguagesArr = [];
        this.reqStaffDet = [];
        this.hiringEventDetails = {};
        this.weekAvl = {};
        this.hrEvntStrt = {};
        this.hrEvntEnd = {};
        this.hrEvntStrtTm = [];
        this.hrEvntEndTm = [];
        this.LunchTm = [];
        this.lastIntrTm = [];
        this.reqStateDet = [];
        this.reqExpDet = [];
        this.languageSkls = [];
        this.interviewScheduleCollection = [];
        this.reqStatusDet = [];
        this.createphnScrDtls = [];
        this.CndtDltls = [];
        this.interviewsHaveBeenScheduled = false;
    };
    /*
     * This function manipulates time in the response and store in the respective property
     *
     * @param N/A
     * @return N/A
     */
    this.getTimes = function(res, propName, modVar) {
        resultList = [];
        if (res.staffingDetails[propName]) {
            if (_.isArray(res.staffingDetails[propName])) {
                resultList = res.staffingDetails[propName];
            } else {
                if (res.staffingDetails[propName]) {
                    resultList = [ res.staffingDetails[propName] ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                var hr = parseInt(resultList[0].hour);
                var tmFrmt = 1;
                if (hr > 12) {
                    resultList[0].hour = hr - 12;
                    tmFrmt = 2;
                } else if (hr === 12) {
                    tmFrmt = 2;
                } else if (hr === 0) {
                    resultList[0].hour = 12;
                }
                this[modVar].push({
                    "hour" : resultList[0].hour,
                    "minute" : resultList[0].minute,
                    "tmFrmt" : tmFrmt
                });
            }
        }
    };
    /*
     * This function sets stores details
     *
     * @param N/A
     * @return N/A
     */
    this.setStoreDetails = function() {
        var resultList = [];
        // check for the StoreDetailList tag in the response
        if (res.StoreDetailList && !_.isNull(res.StoreDetailList)) {
            if (_.isArray(res.StoreDetailList.StoreDetails)) {
                resultList = res.StoreDetailList.StoreDetails;
            } else {
                if (res.StoreDetailList.StoreDetails) {
                    resultList = [ res.StoreDetailList.StoreDetails ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {

                if (!resultList[0].strMgr || resultList[0].strMgr === "") {
                    resultList[0].strMgr = "VACANT";
                }
                this.reqStrDet = [ resultList[0] ];
            }

        }
    };
    /*
     * This function sets schedule preference details of this requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setSchedulePref = function(resultList,i){
        var resultList2 = [];
        // Get the Schedule Preference values selected from the
        // Staffing
        // Request Form
        if (resultList[i].requisitionSchedulePreference && resultList[i].requisitionSchedulePreference[0].schdPrefDetail) {
            if (_.isArray(resultList[i].requisitionSchedulePreference[0].schdPrefDetail)) {
                resultList2 = resultList[i].requisitionSchedulePreference[0].schdPrefDetail;
            } else {
                if (resultList[i].requisitionSchedulePreference[0].schdPrefDetail) {
                    resultList2 = [ resultList[i].requisitionSchedulePreference[0].schdPrefDetail ];
                }
            }
            this.schdPrefArr = resultList2;
        }
    };
    /*
     * This function sets language details of this requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setReqLandDet = function(resultList,i){
         var resultList2 = [];
         // Manipulate Language Preference
         this.reqnLanguagesArr = [];
         if (resultList[i].requisitionLanguagePreference && resultList[i].requisitionLanguagePreference[0].LangSklsDetail) {
             if (_.isArray(resultList[i].requisitionLanguagePreference[0].LangSklsDetail)) {
                 resultList2 = resultList[i].requisitionLanguagePreference[0].LangSklsDetail;
             } else {
                 if (resultList[i].requisitionLanguagePreference[0].LangSklsDetail) {
                     resultList2 = [ resultList[i].requisitionLanguagePreference[0].LangSklsDetail ];
                 }
             }
             for (var l = 0; l < resultList2.length; l++) {
                 this.reqnLanguagesArr.push(resultList2[l].langCode);
             }
         }
    };
    /*
     * This function sets details of this requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setRequisitionDetails = function(resultList,i){
        this.LoadREQDGDltls.push(resultList[i]);
        // Manipulate Schedule Preference
        this.schdPrefArr = [];
        for (var row = 0; row < CONSTANTS.NUM_SCHE_PREF_ENTRIES; row++) {
            for (var col = 0; col < CONSTANTS.NUM_DAYS_OF_WEEK; col++) {
                this.schdPrefArr.push(false);
            }
        }
        this.setSchedulePref(resultList,i);
        this.setReqLandDet(resultList,i);
    };
    /*
     * This function sets details of this requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setReqDetail = function() {
        resultList = [];
        // check for the RequisitionDetailList tag in the response
        if (res.RequisitionDetailList && !_.isNull(res.RequisitionDetailList)) {
            resultList = UTILITY.checkForArrayAndStoreAsArray(res, "RequisitionDetailList", "RequisitionDetail");
            if (!_.isNull(resultList) && resultList.length > 0) {
                for (var i = 0; i < resultList.length; i++) {
                    this.setRequisitionDetails(resultList,i);
                }
                // Set RSC To Manage Radio Button... handled in UI
                // Set RSC Scheduled Radio Button... handled in UI
                // Set Seasonal/Temp Radio Button... handled in UI
            }
        }
    };
    /*
     * This function sets autohold details of this requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setAutoHold = function() {
        if (res.autoHold) {
            if (res.autoHold.toString() === CONSTANTS.ONE) {
                this.setAutoHoldFlg = CONSTANTS.ONE;
            } else {
                this.setAutoHoldFlg = CONSTANTS.Zero;
            }
        }
    };
    /*
     * This function sets staffing details VO object
     *
     * @param N/A
     * @return N/A
     */
    this.staffingDetVO = function(obj){
        var newObj = {add:null,breaks:null,city:null,daysTmMgrAvble:null,desiredExp:null,end:null,hrgMgrName:null,hrgMgrPhn:null,hrgMgrTtl:null,interviewDurtn:null,interviewTmSlt:null,lastIntrTm:null,lunch:null,requestNbr:null,requisitionStatus:null,sealTempJob:null,start:null,state:null,stfHrgEvntStartDt:null,stfHrgEvntEndDt:null,stfhrgEvntLoc:null,stfhrgEvntLocPhn:null,stfReqNbr:null,targetPay:null,weekMgrAvble:null,zip:null,Referals:null,qualPoolNts:null,hrgEvntFlg:null,hireEvntid:null,hireEventType:null,hireEventMgrAssociateId:null};
        newObj = _.extend(newObj,obj);
        newObj.lastIntrTm = "null";
        newObj.hireEvntid = obj.hiringEventID;
        newObj.hireEventType = obj.hireEvntType;
        newObj.hireEventMgrAssociateId = obj.hireEvntMgrAssociateId;
        return newObj;
    };
    /*
     * This function sets staffing details of this requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setStaffingDet = function() {
        resultList = UTILITY.checkAndStoreAsArray(res.staffingDetails);
        if (!_.isNull(resultList) && resultList.length > 0) {
            this.reqStaffDet = [ this.staffingDetVO(resultList[0]) ];

            // Populate new model for ATS Hiring Events if the type is not SDE =
            // Staffing Desktop Hiring Event
            if (resultList[0].hireEvntType !== "SDE") {
                // Make sure that a Begin Date is provided
                if (res.staffingDetails.stfHrgEvntStartDt) {
                    this.hiringEventDetails.hireEventBeginDate = res.staffingDetails.stfHrgEvntStartDt.month + "/" + res.staffingDetails.stfHrgEvntStartDt.day + "/" + res.staffingDetails.stfHrgEvntStartDt.year;
                }
                // Make sure that an End Date is provided
                if (res.staffingDetails.stfHrgEvntEndDt) {
                    this.hiringEventDetails.hireEventEndDate = res.staffingDetails.stfHrgEvntEndDt.month + "/" + res.staffingDetails.stfHrgEvntEndDt.day + "/" + res.staffingDetails.stfHrgEvntEndDt.year;
                }

                // Set the Event Type Text based on hireEventTypeIndicator value
                if (resultList[0].hireEvntType === "SSE") {
                    this.hiringEventDetails.eventTypeText = "Single Store Event";
                } else if (resultList[0].hireEvntType === "MSE") {
                    this.hiringEventDetails.eventTypeText = "Multi Store Event";
                } else if (resultList[0].hireEvntType === "OSE") {
                    this.hiringEventDetails.eventTypeText = "Off-Site Event";
                }
                this.hiringEventDetails.hireEventLocationDescription = resultList[0].stfhrgEvntLoc;
                this.hiringEventDetails.hireEventAddressText = resultList[0].add;
                this.hiringEventDetails.hireEventCityName = resultList[0].city;
                this.hiringEventDetails.hireEventStateCode = resultList[0].state;
                this.hiringEventDetails.hireEventZipCodeCode = resultList[0].zip;
                this.hiringEventDetails.hireEventId = resultList[0].hiringEventID;
                this.hiringEventDetails.hireEventTypeIndicator = resultList[0].hireEvntType;
            }
        }
    };
    /*
     * This function sets week begin date details
     *
     * @param N/A
     * @return N/A
     */
    this.setWeekBeginDt = function() {
        resultList = [];
        if (res.staffingDetails.weekBeginDt) {
            if (_.isArray(res.staffingDetails.weekBeginDt)) {
                resultList = res.staffingDetails.weekBeginDt;
            } else {
                if (res.staffingDetails.weekBeginDt) {
                    resultList = [ res.staffingDetails.weekBeginDt ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                var date = new Date(resultList[0].year, resultList[0].month - 1, resultList[0].day);
                this.weekAvl = date;
            }
        }
    };
    /*
     * This function sets hiring event start date
     *
     * @param N/A
     * @return N/A
     */
    this.setHrgEvntStDt = function() {
        resultList = [];
        if (res.staffingDetails.stfHrgEvntStartDt) {
            if (_.isArray(res.staffingDetails.stfHrgEvntStartDt)) {
                resultList = res.staffingDetails.stfHrgEvntStartDt;
            } else {
                if (res.staffingDetails.stfHrgEvntStartDt) {
                    resultList = [ res.staffingDetails.stfHrgEvntStartDt ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                var startDate = new Date(resultList[0].year, resultList[0].month - 1, resultList[0].day);
                this.hrEvntStrt = startDate;
            }
        }
    };
    /*
     * This function sets hiring event end date
     *
     * @param N/A
     * @return N/A
     */
    this.setHrgEvntEndDt = function() {
        resultList = [];
        if (res.staffingDetails.stfHrgEvntEndDt) {
            if (_.isArray(res.staffingDetails.stfHrgEvntEndDt)) {
                resultList = res.staffingDetails.stfHrgEvntEndDt;
            } else {
                if (res.staffingDetails.stfHrgEvntEndDt) {
                    resultList = [ res.staffingDetails.stfHrgEvntEndDt ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                var endDate = new Date(resultList[0].year, resultList[0].month - 1, resultList[0].day);
                this.hrEvntEnd = endDate;
            }
        }
    };
    /*
     * This function sets states details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setStates = function() {
        // to fetch states
        resultList = [];
        if (res.StateDetailList && !_.isNull(res.StateDetailList)) {
            if (_.isArray(res.StateDetailList.StateDetail)) {
                resultList = res.StateDetailList.StateDetail;
            } else {
                if (res.StateDetailList.StateDetail) {
                    resultList = [ res.StateDetailList.StateDetail ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                this.reqStateDet = resultList;
            }
        }
    };
    /*
     * This function sets experience level
     *
     * @param N/A
     * @return N/A
     */
    this.setExpLevel = function() {
        resultList = [];
        if (res.ExpLevelList && !_.isNull(res.ExpLevelList)) {
            if (_.isArray(res.ExpLevelList.ExpLevelDetail)) {
                resultList = res.ExpLevelList.ExpLevelDetail;
            } else {
                if (res.ExpLevelList.ExpLevelDetail) {
                    resultList = [ res.ExpLevelList.ExpLevelDetail ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                this.reqExpDet = resultList;
            }
        }
    };
    /*
     * This function sets language details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setLanguageDetails = function(resultList){
        this.languageSkls = resultList;
        for (var t = 0; t < this.languageSkls.length; t++) {
            for (var s = 0; s < this.reqnLanguagesArr.length; s++) {
                if (parseInt(this.languageSkls[t].langCode) === parseInt(this.reqnLanguagesArr[s])) {
                    this.languageSkls[t].selectLang = true;
                }
            }
        }
    };
    /*
     * This function sets language skill details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setLangSkill = function() {
        resultList = [];
        if (res.LangSklsList && !_.isNull(res.LangSklsList)) {
            if (_.isArray(res.LangSklsList.LangSklsDetail)) {
                resultList = res.LangSklsList.LangSklsDetail;
            } else {
                if (res.LangSklsList.LangSklsDetail) {
                    resultList = [ res.LangSklsList.LangSklsDetail ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                this.setLanguageDetails(resultList);
            }
        }
    };
    /*
     * This function sets interview details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setInterviewCollection = function(resultList,staffDtl){
        var today = new Date();
        for (var m = 0; m < resultList.length; m++) {
            if (staffDtl && staffDtl.hireEvntType !== "SDE" && staffDtl.hrgEvntFlg === "Y") {

                var eventDate = new Date(resultList[m].hireEventBeginDate);
                if (eventDate > today) {
                    resultList[m].enabled = true;
                } else {
                    resultList[m].enabled = false;
                }
            } else {
                resultList[m].enabled = true;
            }
        }
        this.interviewScheduleCollection = this.interviewScheduleCollection.concat(resultList);
    };
    /*
     * This function sets calendar details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setCalendarInfo = function() {
        // Fill Calendar Combo
        resultList = [];
        if (res.ScheduleDescList && !_.isNull(res.ScheduleDescriptionDetails)) {
            resultList = UTILITY.checkAndStoreAsArray(res.ScheduleDescList.ScheduleDescriptionDetails);
            if (!_.isNull(resultList) && resultList.length > 0) {
                var staffDtl = this.reqStaffDet[0];
                var firstline;
                if (staffDtl && staffDtl.hireEvntType !== "SDE" && staffDtl.hrgEvntFlg === "Y") {
                    firstline = {
                        "requisitionCalendarDescription" : "Select Hiring Event..",
                        "requisitionCalendarId" : "0",
                        "type" : 0,
                        "enabled" : true
                    };
                } else {
                    firstline = {
                        "requisitionCalendarDescription" : "Select Calendar..",
                        "requisitionCalendarId" : "0",
                        "type" : 0,
                        "enabled" : true
                    };
                }
                this.interviewScheduleCollection.push(firstline);
                this.setInterviewCollection(resultList,staffDtl);
                var grouped = _.groupBy(this.interviewScheduleCollection, "type");
                var sortedkeys = _.sortBy(_.keys(grouped), function(num) {
                    return parseInt(num);
                });
                var calObjArr = [];
                for (var index = 0; index < sortedkeys.length; index++) {
                    calObjArr = calObjArr.concat(_.sortBy(grouped[sortedkeys[index]], function(obj){
                    	return obj.requisitionCalendarDescription.toString().toUpperCase();
                    }));
                }

                this.interviewScheduleCollection = calObjArr;
                // Sets a variable on RequisitionDetails.mxml so that the
                // correct
                // Calendar is selected
            }
        }
    };
    /*
     * This function sets status details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setStatusInfo = function() {
        // check for the StatusList tag in the response
        resultList = [];
        if (res.StatusList && res.StatusList.requisitionStats) {
            if (_.isArray(res.StatusList.requisitionStats[0].status)) {
                resultList = res.StatusList.requisitionStats[0].status;
            } else {
                if (res.StatusList.requisitionStats[0].status) {
                    resultList = [ res.StatusList.requisitionStats[0].status ];
                }
            }
            if (!_.isNull(resultList) && resultList.length > 0) {
                this.reqStatusDet = resultList;
                this.reqStatusDet = _.sortBy(this.reqStatusDet, "statusDescription");
                if (this.LoadREQDGDltls[0].requisitionStatusCode.toString() === "0") {
                    // Default to 1 In-Process
                    this.LoadREQDGDltls[0].requisitionStatusCode = 1;
                }
                // Selection of Requisition status
            }
        }
    };
    /*
     * This function sets ynstatus details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setYnStatus = function(value){
        if (value === "12") {
            return CONSTANTS.PROCEED;
        } else if (value === "5") {
            return CONSTANTS.DO_NOT_PROCEED;
        }
    };
    /*
     * This function sets phonescreen details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setCreatePhnScrDtls = function(resultList){
        var intdate = null;
        for (var k = 0; k < resultList.length; k++) {
            if (!_.isNull(resultList[k].InterviewLocDtls) && resultList[k].InterviewLocDtls.interviewDate) {
                intdate = resultList[k].InterviewLocDtls.interviewDate.month + "/" + resultList[k].InterviewLocDtls.interviewDate.day + "/" + resultList[k].InterviewLocDtls.interviewDate.year;
            }
            if (!_.isNull(resultList[k].ynstatus) && resultList[k].ynstatus !== undefined) {
                resultList[k].ynStatusdesc = this.setYnStatus(resultList[k].ynstatus.toString());
            }
            if (!_.isNull(intdate)) {
                resultList[k].intDate = intdate;
            }
            this.createphnScrDtls.push(resultList[k]);
            // Check to see if any Interviews have been scheduled, if at
            // least one has been scheduled then the RSC cannot change
            // the
            // Calendar the Req is associated with.
            if (resultList[k].interviewStatusCode.toString() === "11" || intdate) {
                this.interviewsHaveBeenScheduled = true;
            }
            intdate = null;
        }
        this.createphnScrDtls = _.sortBy(this.createphnScrDtls, "name");
    };
    /*
     * This function sets phonescreen details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setPhoneScreenDetails = function() {
        resultList = [];
        if (res.ITIDetailList) {
            resultList = UTILITY.checkAndStoreAsArray(res.ITIDetailList.PhoneScreenIntrwDetail);
            if (!_.isNull(resultList) && resultList.length > 0) {
                this.setCreatePhnScrDtls(resultList);
            }
        }
    };
    /*
     * This function sets candidate details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setCndtDltls = function(resultList){
        var ofrDate = null;
        for (var j = 0; j < resultList.length; j++) {
            if (!_.isNull(resultList[j].offerDate) && resultList[j].offerDate) {
                ofrDate = resultList[j].offerDate.month + "/" + resultList[j].offerDate.day + "/" + resultList[j].offerDate.year;
            }
            if (!_.isNull(ofrDate)) {
                resultList[j].offerDate = ofrDate;
            }
            this.CndtDltls.push(resultList[j]);
            ofrDate = null;
        }
    };
    /*
     * This function sets candidate details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setCandidateDetails = function() {
        resultList = [];
        if (res.CandidateDetailList && !_.isNull(res.CandidateDetailList) && res.CandidateDetailList.CandidateDetails) {
            resultList = UTILITY.checkAndStoreAsArray(res.CandidateDetailList.CandidateDetails);
            if (!_.isNull(resultList) && resultList.length > 0) {
                this.setCndtDltls(resultList);
            }
        }
    };
    /*
     * This function sets manager details of the requisition
     *
     * @param N/A
     * @return N/A
     */
    this.setMgrData = function() {
        resultList = [];
        if (res.hiringEventMgrData) {
            this.hiringEventDetails.emgrHumanResourcesAssociateId = res.hiringEventMgrData.emgrHumanResourcesAssociateId;
            this.hiringEventDetails.name = res.hiringEventMgrData.name;
            this.hiringEventDetails.phone = res.hiringEventMgrData.phone;
            this.hiringEventDetails.title = res.hiringEventMgrData.title;
            this.hiringEventDetails.email = res.hiringEventMgrData.email;
        }
    };
    this.initializeModelVariables();
    this.setAutoHold();
    this.setStoreDetails();
    this.setReqDetail();

    resultList = [];
    // check for the staffingDetails tag in the response
    if (res.staffingDetails && !_.isNull(res.staffingDetails)) {
        this.setStaffingDet();
        this.setWeekBeginDt();
        this.setHrgEvntStDt();
        this.setHrgEvntEndDt();
        this.getTimes(res, "startTime", "hrEvntStrtTm");
        this.getTimes(res, "endTime", "hrEvntEndTm");
        this.getTimes(res, "lunch", "LunchTm");
        this.getTimes(res, "lastIntrTm", "lastIntrTm");

    } else {
        this.UpdIns = "INSERT";
    }

    this.setStates();
    this.setExpLevel();
    this.setLangSkill();
    this.setCalendarInfo();
    this.setStatusInfo();
    this.setPhoneScreenDetails();
    this.setCandidateDetails();
    this.setMgrData();
    return this;
}
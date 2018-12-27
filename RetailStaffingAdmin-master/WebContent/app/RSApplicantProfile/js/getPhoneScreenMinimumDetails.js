 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getPhoneScreenMinimumDetails.js
 * Application: Retail Staffing Admin
 *
 */
function getPhoneScreenMinimumDetails(response) {
    var resultList = [];
    var phnScrnDtl = {};
    var timeVO = {};
    var hourVal;
    this.phnScreenDtls = [];
    this.minResponseList = [];
    this.phnScrnTimeList = [];
    this.getPhoneScrnDispositionStatusDtls = [];
    this.phnScrnDat = null;
    this.phnScrnStatusTimestamp = [];
    /*
     * This function stores minimum responses into an array
     *
     * @param response
     * @return N/A
     */
    this.storeResultMinList = function(response){
        var resultMinList = [];
        if ("MinimumResponseDtlList" in response.ITIDetailList.PhoneScreenIntrwDetail) {
            if (response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList != null && "MinimumResponseDtl" in response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList[0]) {
                if (_.isArray(response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList[0].MinimumResponseDtl)) {
                    resultMinList = response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList[0].MinimumResponseDtl;
                } else {
                    resultMinList = [ response.ITIDetailList.PhoneScreenIntrwDetail.MinimumResponseDtlList[0].MinimumResponseDtl ];
                }
            }
            if (resultMinList && resultMinList.length > 0) {
                for (var i = 0; i < resultMinList.length; i++) {
                    this.minResponseList.push({
                        "minFlag" : resultMinList[i].minimumResponse,
                        "seqNbr" : resultMinList[i].seqNbr
                    });
                }
            }
        }
    };
    /*
     * This function stores Phone screen time
     *
     * @param resultList
     * @param n
     * @return N/A
     */
    this.storePhoneScreenTime = function(resultList,n){
        if (resultList[n].phnScrnTime && parseInt(resultList[n].phnScrnTime.day) >= 1 && resultList[n].phnScrnTime.hour) {
            var timeSlot;
            var hourSlot;
            var hr = parseInt(resultList[n].phnScrnTime.hour);
            if (hr > 12) {
                hourVal = hr - 12;
                hourSlot = hourVal.toString();
                timeSlot = CONSTANTS.PM;
            } else {
                hourSlot = hr;
                timeSlot = CONSTANTS.AM;
            }
            if (hr === 0) {
                hourVal = 12;
                hourSlot = hourVal.toString();
            }
            if (hr === 12) {
                timeSlot = CONSTANTS.PM;
            }
            resultList[n].phnScrnTime.tmFrmt = 0;
            resultList[n].phnScrnTime.hourSlot = hourSlot;
            resultList[n].phnScrnTime.timeSlot = timeSlot;
            timeVO = resultList[n].phnScrnTime;
            this.phnScrnTimeList.push(timeVO);
        }
    };
    /*
     * This function stores result list
     *
     * @param resultList
     * @return N/A
     */
    this.storeResultList = function(resultList){
        for (var n = 0; n < resultList.length; n++) {
            resultList[n].ScreenNbr = resultList[n].itiNbr;
            resultList[n].canName = resultList[n].name;
            resultList[n].strNbr = resultList[n].cndStrNbr;
            resultList[n].dept = resultList[n].cndDeptNbr;
            resultList[n].candTitle = resultList[n].title;
            resultList[n].contactHist = resultList[n].contactHistoryTxt;
            resultList[n].detailResp = resultList[n].detailTxt;
            resultList[n].hrEventFlg = resultList[n].hrEvntFlg;
            resultList[n].intrwTyp = resultList[n].interviewLocTypCd;
            resultList[n].emailAddr = resultList[n].emailAdd;
            phnScrnDtl = resultList[n];
            // Added to implement for the phone screen date and time to be saved once
            if (resultList[n].phnScrnDate) {
                var dateValues = new Date(parseInt(resultList[n].phnScrnDate.year), parseInt(resultList[n].phnScrnDate.month) - 1, parseInt(resultList[n].phnScrnDate.day));
                this.phnScrnDat = dateValues;
                this.storePhoneScreenTime(resultList,n);
            }
            // Phone Screen Status Time Stamp
            if (resultList[n].phoneScreenStatusTimestamp) {
                resultList[n].phoneScreenStatusTimestamp.second = "00";
                resultList[n].phoneScreenStatusTimestamp.milliSecond = "000";
                resultList[n].phoneScreenStatusTimestamp.tmFrmt = 0;
                this.phnScrnStatusTimestamp.push(resultList[n].phoneScreenStatusTimestamp);
            }
            this.phnScreenDtls.push(phnScrnDtl);
        }
    };
    // Proceed only if status is SUCCESS
    if ("status" in response && response.status === CONSTANTS.SUCCESS) {
        resultList = UTILITY.checkForArrayAndStoreAsArray(response,"PhoneScreenDispositionList","PhoneScreenDispositionDetail");
        if (resultList && resultList.length > 0) {
            for (var x = 0; x < resultList.length; x++) {
                this.getPhoneScrnDispositionStatusDtls.push({
                    "status" : resultList[x].dispositionCode,
                    "desc" : resultList[x].dispositionDesc
                });
            }
            this.getPhoneScrnDispositionStatusDtls = _.sortBy(this.getPhoneScrnDispositionStatusDtls, "desc");
        }
        resultList = [];
        if ("ITIDetailList" in response) {
            resultList = UTILITY.checkForArrayAndStoreAsArray(response,"ITIDetailList","PhoneScreenIntrwDetail");
            this.storeResultMinList(response);
            if (resultList && resultList.length > 0) {
                this.storeResultList(resultList);
            }
        }
    }
    return this;
}
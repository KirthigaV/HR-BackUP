 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: retailStaffingCreatePhnScr.js
 * Application: Retail Staffing Admin
 *
 */
function retailStaffingCreatePhnScr(response) {
    var resultList = [];
    this.createphnScrDtls = [];
    this.getYnStatusdesc = function(value){
        if (value != null) {
            if (parseInt(value) === 12) {
                return CONSTANTS.PROCEED;
            } else if (parseInt(value) === 5) {
                return CONSTANTS.DO_NOT_PROCEED;
            }
        }
        return null;
    };
    this.storeResultList = function(resultList){
        if (resultList && resultList.length > 0) {
            var intDate = "";
            for (var i = 0; i < resultList.length; i++) {
                if (resultList[i].InterviewLocDtls != null && resultList[i].InterviewLocDtls.interviewDate) {
                    intDate = resultList[i].InterviewLocDtls.interviewDate.month + "/" + resultList[i].InterviewLocDtls.interviewDate.day + "/" + resultList[i].InterviewLocDtls.interviewDate.year;
                }
                resultList[i].ynStatusdesc = this.getYnStatusdesc(resultList[i].ynStatus);
                resultList[i].creator = "";
                resultList[i].dateCreate = "";
                resultList[i].fillDt = "";
                resultList[i].ft = "";
                resultList[i].openings = "";
                resultList[i].phnScrTyp = "";
                resultList[i].pt = "";
                resultList[i].scrTyp = "";
                resultList[i].intDate = intDate;
                this.createphnScrDtls.push(resultList[i]);
                intDate = null;
            }
            this.createphnScrDtls = _.sortBy(this.createphnScrDtls, "name");
        }
    };
    if ("status" in response && response.status === CONSTANTS.SUCCESS) {
        if ("ITIDetailList" in response && response.ITIDetailList != null) {
             resultList = UTILITY.checkForArrayAndStoreAsArray(response, "ITIDetailList", "PhoneScreenIntrwDetail");
             this.storeResultList(resultList);
        }
        this.crtPhnScrnBtnClickFlg = true;
    }
    return this;
}
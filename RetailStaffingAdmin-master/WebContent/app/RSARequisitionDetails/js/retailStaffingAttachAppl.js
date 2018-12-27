 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: retailStaffingAttachAppl.js
 * Application: Retail Staffing Admin
 *
 */
function retailStaffingAttachAppl(response) {
    var resultList = [];
    var applUnavailInfoDGDtlVO = {};
    this.LoadApplUnavailInfo = {};
    this._hasDuplicateAppl = "";
    this.storeResultList = function(resultList){
        if (resultList != null && resultList.length > 0) {
            for (var i = 0; i < resultList.length; i++) {
                applUnavailInfoDGDtlVO = {};
                applUnavailInfoDGDtlVO = resultList[i];
                applUnavailInfoDGDtlVO.applUnavailName = resultList[i].applUnavailName;
                this.LoadApplUnavailInfo.push(applUnavailInfoDGDtlVO);
            }

            this._hasDuplicateAppl = "true";
        }
    };
    if (response.status && response.status === CONSTANTS.SUCCESS) {
        this._hasDuplicateAppl = "false";
        // check for the applUnavailInfoTOList tag in the response
        if ("ApplUnavailList" in response && "ApplUnavail" in response.ApplUnavailList) {
            resultList = UTILITY.checkForArrayAndStoreAsArray(response,"ApplUnavailList","ApplUnavail");
            // check whether the result list is having any value object or not
            this.storeResultList(resultList);
        }
    }
    return this;
};
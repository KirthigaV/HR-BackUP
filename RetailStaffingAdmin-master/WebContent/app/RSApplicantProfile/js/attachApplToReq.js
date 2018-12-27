 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: attachApplToReq.js
 * Application: Retail Staffing Admin
 *
 */
function attachApplToReq(response) {

    var resultList = [];
    this.LoadApplUnavailInfo = [];
    this._hasDuplicateAppl = "";
    // Proceed only if status is SUCCESS
    if ("status" in response && response.status === CONSTANTS.SUCCESS) {
        this._hasDuplicateAppl = "false";

        if ("ApplUnavailList" in response && "ApplUnavail" in response.ApplUnavailList) {
            resultList = UTILITY.checkForArrayAndStoreAsArray(response,"ApplUnavailList","ApplUnavail");
            if (resultList != null && resultList.length > 0) {
                this.LoadApplUnavailInfo = response;
                this._hasDuplicateAppl = "true";
            }
        }
    }
    return this;
}
 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: loadRejectDetails.js
 * Application: Retail Staffing Admin
 *
 */
function loadRejectDetails(response) {
    var resultList = [];
    this.rejectComboLoad = [];
    /*
     * This function stores result list
     *
     * @param resultList
     * @return N/A
     */
    this.storeResultList = function(resultList) {
        if (resultList && resultList.length > 0) {
            for (var m = 0; m < resultList.length; m++) {
                if (parseInt(resultList[m].employmentPositionCandidateRejectReasonCode) !== 5) {
                    this.rejectComboLoad.push({
                        "code" : resultList[m].employmentPositionCandidateRejectReasonCode,
                        "desc" : resultList[m].ReasonDescription,
                        "displayField" : resultList[m].shortReasonDescription
                    });
                }
            }
        }
    };
    if ("rejComboRes" in response && "rejectionReason" in response.rejComboRes) {
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "rejComboRes", "rejectionReason");
        this.storeResultList(resultList);
    }
    return this;
}
 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getCandidateCount.js
 * Application: Retail Staffing Admin
 *
 */
function getCandidateCount(response) {
    var resultList = [];
    var applPoolInfoDGDtlVO = null;
    this.LoadApplPoolCandidateCount = [];
    var valid = "status" in response && response.status === CONSTANTS.SUCCESS && "CandidateCountList" in response && "CandidateCountDetail" in response.CandidateCountList;
    if (valid) {
        if (_.isArray(response.CandidateCountList.CandidateCountDetail)) {
            resultList = response.CandidateCountList.CandidateCountDetail;
        } else {
            if ("CandidateCountDetail" in response.CandidateCountList) {
                resultList = [ response.CandidateCountList.CandidateCountDetail ];
            }
        }
        if (resultList && resultList.length > 0) {
            for (var i = 0; i < resultList.length; i++) {
                applPoolInfoDGDtlVO = {
                    "candidateCount" : resultList[i].candidateCount
                };
                this.LoadApplPoolCandidateCount.push(applPoolInfoDGDtlVO);
            }
        }
    }
    return this;
}
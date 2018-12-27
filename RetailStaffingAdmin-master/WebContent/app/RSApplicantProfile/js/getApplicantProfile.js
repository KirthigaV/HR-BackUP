 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: getApplicantProfile.js
 * Application: Retail Staffing Admin
 *
 */
function getApplicantProfile(response) {
    var resultList = [];
    this.LoadApplPersonalInfoDtls = [];
    this.LoadApplAssociateInfoDtls = [];
    this.LoadApplAssociateReviewDtls = [];
    this.LoadApplAssociatePrevPosDtls = [];
    this.LoadApplEducationDtls = [];
    this.LoadApplWorkHistoryDtls = [];
    this.LoadApplJobPrefDtls = [];
    this.LoadApplLanguageDtls = [];
    this.LoadApplPhnScrnDtls = [];
    this.LoadApplStatusHistoryDtls = [];
    this.LoadApplAppHistoryDtls = [];
    this.LoadApplAppHistoryDtls2 = [];
    /*
     * This function checks if the resultl;ist id valid and stores it in a property
     *
     * @param resultList
     * @param objName
     * @return N/A
     */
    this.simpleCheckAndStore = function(resultList, objName) {
        if (resultList != null && resultList.length > 0) {
            this[objName] = this[objName].concat(resultList);
        }
    };
    // Proceed only if status is SUCCESS
    if ("status" in response && response.status === CONSTANTS.SUCCESS) {
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "PersonalInfoList", "PersonalInfo");
        this.simpleCheckAndStore(resultList, "LoadApplPersonalInfoDtls");
        resultList = [];
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "AssociateInfoList", "AssociateInfo");
        this.simpleCheckAndStore(resultList, "LoadApplAssociateInfoDtls");

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "AssociateReviewList", "ReviewResults");
        if (resultList != null && resultList.length > 0) {
            for (var i = 0; i < resultList.length; i++) {
                var revVO = {
                    "reviewDate" : resultList[i].date,
                    "reviewResult" : resultList[i].results
                };
                this.LoadApplAssociateReviewDtls.push(revVO);
            }
        }

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "AssociatePrePosList", "PreviousPosition");
        this.simpleCheckAndStore(resultList, "LoadApplAssociatePrevPosDtls");

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "AssociatePrePosList", "PreviousPosition");
        this.simpleCheckAndStore(resultList, "LoadApplAssociatePrevPosDtls");

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "EducationList", "Education");
        this.simpleCheckAndStore(resultList, "LoadApplEducationDtls");

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "WorkHistoryList2", "WorkHistory2");

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "WorkHistoryList", "WorkHistory");
        this.simpleCheckAndStore(resultList, "LoadApplWorkHistoryDtls");

        this.applicantSchdPrefArr = [];
        for (var j = 0; j < 9; j++) {
            this.applicantSchdPrefArr.push(false);
        }
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "JobPrefList", "schdPrefDetail");
        var valid = resultList != null && resultList.length > 0;
        if (valid) {
            this.applicantSchdPrefArr = UTILITY.setSchedulePrefArray(resultList);
        }

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "JobPrefList", "JobPrefSet");
        this.simpleCheckAndStore(resultList, "LoadApplJobPrefDtls");

        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "JobPrefList2", "JobPref2");
        this.simpleCheckAndStore(resultList, "LoadApplJobPrefDtls2");

        resultList = [];
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "LanguageList", "LanguageSet");
        this.simpleCheckAndStore(resultList, "LoadApplLanguageDtls");

        resultList = [];
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "PhnScreenHistoryList", "PhnScreenHistory");
        this.simpleCheckAndStore(resultList, "LoadApplPhnScrnDtls");

        resultList = [];
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "CandidateHistoryList", "CandidateHistory");
        this.simpleCheckAndStore(resultList, "LoadApplStatusHistoryDtls");

        resultList = [];
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "ApplicationHistoryList", "ApplicationHistory");
        this.simpleCheckAndStore(resultList, "LoadApplAppHistoryDtls");

        resultList = [];
        resultList = UTILITY.checkForArrayAndStoreAsArray(response, "ApplicationHistoryList2", "ApplicationHistory2");
        this.simpleCheckAndStore(resultList, "LoadApplAppHistoryDtls2");

    }
    return this;
}
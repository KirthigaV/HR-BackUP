 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: rsaViewApplPool.js
 * Application: Retail Staffing Admin
 *
 */
function rsaViewApplPool(response, reqNumber) {
    var resultList = [];
    var viewedList = [];
    this.LoadApplicantPoolDgDtls = [];
    this.LoadMasterApplicantPoolDgDtls = [];
    this._tieringApplicant = "";
    var viewedInQPVO = {};
    this._currentApplicantPage = 0;
    this._applicantPagesAlreadyUpdated = 1;
    this._currentApplicantGroupStartValue = 0;
    this._currentApplicantGroupEndValue = 0;
    this._currentApplicantGroupTotalValue = 0;
    this.rsaServices = RSASERVICES;
    this.getTiering = function(tiering){
        if (tiering != null) {
            if (parseInt(tiering) === 0) {
                return "Off";
            } else if (parseInt(tiering) === 1) {
                return "On";
            }
        }
    };
    this.success = function() {
        // =>
    };
    this.fail = function() {
        // =>
    };
    this.storeResultList = function(resultList){
        if (resultList != null && resultList.length > 0) {
            for (var i = 0; i < resultList.length; i++) {
                resultList[i].attachedToReq = false;
                this.LoadMasterApplicantPoolDgDtls.push(resultList[i]);
            }
            var endIndex = 0;
            if (this.LoadMasterApplicantPoolDgDtls.length < CONSTANTS.APPLICANTS_PER_PAGE) {
                endIndex = this.LoadMasterApplicantPoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.APPLICANTS_PER_PAGE;
            }
            for (var j = 0; j < endIndex; j++) {
                var tempVo = null;
                tempVo = JSON.parse(JSON.stringify(this.LoadMasterApplicantPoolDgDtls[j]));
                this.LoadApplicantPoolDgDtls.push(tempVo);

                viewedInQPVO = {
                    "applicantId" : tempVo.id,
                    "reqNbr" : reqNumber,
                    "consideredCode" : CONSTANTS.CONSIDERED_IN_QP_CD
                };
                viewedList.push(viewedInQPVO);
            }
            var ApplicantsConsideredInQPRequest = {};
            ApplicantsConsideredInQPRequest.applicantList = {};
            ApplicantsConsideredInQPRequest.applicantList.applicant = [];
            ApplicantsConsideredInQPRequest.applicantList.applicant = viewedList;
            var input = {};
            input.ApplicantsConsideredInQPRequest = ApplicantsConsideredInQPRequest;        
            //TODO remove this check if it is not called.
            if (!CONSTANTS.cacheRequisitionDetailsFlg)
        	{
            	this.rsaServices.markApplicantsAsConsideredInQPEvent("data=" + JSON.stringify(input), this.success.bind(this), this.fail.bind(this));
        	}
            this._currentApplicantPage = 1;

            // Set the value of Associate Group start and end value
            if ((this._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE) <= this.LoadMasterApplicantPoolDgDtls.length) {
                this._currentApplicantGroupStartValue = 1;
                this._currentApplicantGroupEndValue = (this._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE);
            } else {
                this._currentApplicantGroupStartValue = 1;
                this._currentApplicantGroupEndValue = this.LoadMasterApplicantPoolDgDtls.length;
            }
            this._currentApplicantGroupTotalValue = this.LoadMasterApplicantPoolDgDtls.length;
        }
    };
    this.getResultList = function(response){
        var resultList = [];
        if (response.candidates != null && "candidates" in response) {
            if (_.isArray(response.candidates)) {
                resultList = response.candidates;
            } else {
                if ("candidates" in response) {
                    resultList = [ response.candidates ];
                }
            }
        }
        return resultList;
    };
    this._tieringApplicant = this.getTiering(response.tiering);
    if (response.candidates && "candidates" in response) {
        resultList = this.getResultList(response);
        this.storeResultList(resultList);
    }
    // => error handling
    return this;
};

 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: rsaViewAssocPool.js
 * Application: Retail Staffing Admin
 *
 */
function rsaViewAssocPool(response, reqNumber) {
    var resultList = [];
    var viewedInQPVO = null;
    var viewedList = [];
    this.LoadAssociatePoolDgDtls = [];
    this.LoadMasterAssociatePoolDgDtls = [];
    this._tieringAssociate = "";

    this._currentAssociatePage = 0;
    this._associatePagesAlreadyUpdated = 1;
    this._currentAssociateGroupStartValue = 0;
    this._currentAssociateGroupEndValue = 0;
    this._currentAssociateGroupTotalValue = 0;
    this._tieringAssociate = "N/A";
    this.rsaServices = RSASERVICES;
    this.success = function() {

    };
    this.fail = function() {

    };
    this.storeResultList = function(resultList){
        if (resultList != null && resultList.length > 0) {
            for (var i = 0; i < resultList.length; i++) {
                resultList[i].attachedToReq = false;
                resultList[i].assocConsideredFlg = resultList[i].consideredFlg;
                resultList[i].assocAvailability = resultList[i].availability;
                this.LoadMasterAssociatePoolDgDtls.push(resultList[i]);
            }
            var endIndex = 0;
            if (this.LoadMasterAssociatePoolDgDtls.length < CONSTANTS.ASSOCIATES_PER_PAGE) {
                endIndex = this.LoadMasterAssociatePoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.ASSOCIATES_PER_PAGE;
            }
            for (var j = 0; j < endIndex; j++) {
                var tempVo = null;
                tempVo = JSON.parse(JSON.stringify(this.LoadMasterAssociatePoolDgDtls[j]));
                this.LoadAssociatePoolDgDtls.push(tempVo);

                // Load VO that will be sent to Java to update that the
                // associate has showed up in the QP
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
            if (!CONSTANTS.cacheRequisitionDetailsFlg)
        	{
            	this.rsaServices.markApplicantsAsConsideredInQPEvent("data=" + JSON.stringify(input), this.success.bind(this), this.fail.bind(this));
        	}
            this._currentAssociatePage = 1;

            // Set the value of Associate Group start and end value
            if ((this._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE) <= this.LoadMasterAssociatePoolDgDtls.length) {
                this._currentAssociateGroupStartValue = 1;
                this._currentAssociateGroupEndValue = (this._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE);
            } else {
                this._currentAssociateGroupStartValue = 1;
                this._currentAssociateGroupEndValue = this.LoadMasterAssociatePoolDgDtls.length;
            }
            this._currentAssociateGroupTotalValue = this.LoadMasterAssociatePoolDgDtls.length;
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
    if (response.candidates != null) {
        resultList = this.getResultList(response);
        this.storeResultList(resultList);
    }
    // => error handling
    return this;
}
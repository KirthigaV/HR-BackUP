/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: retailStaffingServices.js
 * Application: Retail Staffing Admin
 *
 */
function retailStaffingServices() {
    var ALL_USE_STUB_FLAG = false;
    this.GET_USER_PROFILE = "/RetailStaffing/service/UserProfileService/getUserProfileDetails";
    /*
     * This method is used to get loggedin user details like role,name etc...
     */
    this.getUserProfile = function(role, callback, useStub, freezeScreen, screenFreezeMessage) {
        var url = this.GET_USER_PROFILE;
        var data = {
            "roles" : role
        };
        this.POSTservices(url, data, callback, useStub, freezeScreen, screenFreezeMessage);
    };

    /*
     * This method is used to get locale details
     */
    this.getLocaleDetails = function(callback) {
        var url = "/RetailStaffing/service/RetailStaffingService/getLocaleDetails";
        this.GETservices(url, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get search details
     */
    this.getSearchDetails = function(callback, data) {
        var url = "/RetailStaffing/service/RetailStaffingService/getSearchDetails";
        this.POSTservices(url, data, callback, false, true, "Please Wait...");
    };

    /*
     * This method is used to get requisition summary results
     */
    this.getSummaryResults = function(callback, data) {
        var url = "/RetailStaffing/service/RetailStaffingService/getSummaryResults";
        this.POSTservices(url, data, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get candidate details
     */
    this.getcandidateDetails = function(callback, data) {
        var url = "/RetailStaffing/service/RetailStaffingService/getCandidateDetails";
        this.POSTservices(url, data, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get Review phone screen details
     */
    this.getRevPhnScrnDetails = function(callback, data) {
        var url = "/RetailStaffing/service/RetailStaffingService/getRevPhnScrnDetails";
        this.POSTservices(url, data, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to save updated review phone screen details
     */
    this.saveRevPhnScrnDtl = function(callback, data) {
        var url = "/RetailStaffing/service/RetailStaffingService/saveRevPhnScrnDtl";
        this.POSTservices(url, data, callback, false, true, "Please Wait...");
    };
    /*
     * This method is used to get Phone screen details
     */
    this.getPhoneScreenValue = function(callback) {
        callback.fire(JSON_STUB.phoneScreenNumberdata);
    };
    /*
     * This method is used to get Interview Screen details
     */
    this.getInterviewScreenValue = function(callback) {
        callback.fire(JSON_STUB.interviewScreenData);
    };
    /*
     * This method is used to get review completed phone screen details
     */
    this.reviewCompletedPhoneScreens = function(callback) {
        callback.fire(JSON_STUB.reviewCompletedPhoneScreens);
    };
    /*
     * This method is used to get scheduled Interview details
     */
    this.getScheduledInterviewsData = function(callback) {
        callback.fire(JSON_STUB.scheduledInterviewsData);
    };
    /*
     * This method is used to send Interview details
     */
    this.getSendInterviewDetailsData = function(callback) {
        callback.fire(JSON_STUB.SendInterviewDetailsData);
    };
    /*
     * This method is used to get review Active requisition details
     */
    this.getreviewActiveReqData = function(callback) {
        callback.fire(JSON_STUB.reviewActiveReqData);
    };
    /*
     * This method is used to get Inactive requisition details
     */
    this.getviewInactiveReqData = function(callback) {
        callback.fire(JSON_STUB.viewInactiveReqData);
    };
    /*
     * This method helps to do AJAX calls for POST
     */
    this.POSTservices = function(url, data, callback, useStub, freezeScreen, screenFreezeMessage) {
        if (!useStub && !ALL_USE_STUB_FLAG) {
            if (freezeScreen) {
                if (UTILITY.isEmptyString(screenFreezeMessage)) {
                    screenFreezeMessage = "Processing. Please wait...";
                }
                // Freeze the background until response comes from service
                UTILITY.screenFreeze(screenFreezeMessage);
            }
            $.ajax({
                type : "POST",
                data : data,
                url : url,
                cache : false,
                datatype : CONSTANTS.DATA_TYP_JSON,
                contentType : CONSTANTS.APPLICATION_JSON,
                success : function(successJson) {
                    callback.fire(successJson);
                },
                error : function() {
                    var successJson = "Error Calling Details Service Error";
                    callback.fire(successJson);
                }
            });
        } else {
            // Trigger call back function if stub response is required
            callback.fire(JSON_STUB.partnerViewPartnerTypeList);
        }
    };
    /*
     * This method helps to do AJAX calls for GET
     */
    this.GETservices = function(url, callback, useStub, freezeScreen, screenFreezeMessage) {
        if (!useStub && !ALL_USE_STUB_FLAG) {
            if (freezeScreen) {
                if (UTILITY.isEmptyString(screenFreezeMessage)) {
                    screenFreezeMessage = "Processing. Please wait...";
                }
                // Freeze the background until response comes from service
                UTILITY.screenFreeze(screenFreezeMessage);
            }
            $.ajax({
                type : "GET",
                url : url,
                cache : false,
                datatype : CONSTANTS.DATA_TYP_JSON,
                contentType : CONSTANTS.APPLICATION_JSON,
                success : function(successJson) {
                    callback.fire(successJson);
                },
                error : function() {
                    var successJson = "Error Calling Details Service Error";
                    callback.fire(successJson);
                }
            });
        } else {
            // Trigger call back function if stub response is required
            callback.fire(JSON_STUB.partnerViewPartnerTypeList);
        }
    };
    /*
     * This method helps to do file upload
     */
    this.uploadFile = function(formData, uploadURL, successCallback, errorCallBack, freezeScreen, screenFreezeMessage) {
        // Freeze the background until response comes from service
        if (freezeScreen) {
            if (UTILITY.isEmptyString(screenFreezeMessage)) {
                screenFreezeMessage = "Processing. Please wait...";
            }
            UTILITY.screenFreeze(screenFreezeMessage);
        }
        $.ajax({
            type : "POST",
            data : formData,
            url : uploadURL,
            cache : false,
            datatype : CONSTANTS.DATA_TYP_JSON,
            contentType : false,
            // Don't process the files
            processData : false,
            success : function(successJson) {
                if (parseInt(successJson.responseCode) !== 100) {
                    errorCallBack.fire(successJson);
                    return;
                } else {
                    successCallback.fire(successJson);
                }
            },
            error : function() {
                jErrorAlert("Error Calling Upload Service", "Error");
            }
        });

    };

}

/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: emailAdministrationTool.js
 * Application: RetailStaffingAdmin
 *
 * Email Administration tool is used to resend emails using requistionNumber
 */
function emailAdministrationTool() {
    /*
     * This Method is used to call getUserprofile service and handle
     * requistionNumber validation
     */
    this.initialize = function() {
        $("#requisitionNumber")
                .keypress(
                        function(e) {
                            var key = String.fromCharCode(e.which);
                            // Allow only 0 to 9 and ,
                            var regex = /^[0-9,,]/;
                            if ((!regex.test(key))
                                    && !(e.which === 13 || e.which === 8 || e.which === 32)) {
                                return false;
                            }
                        });
        $("#requisitionNumber").focus();
        this.getUserProfileDetails();
    };
    $(".navCloseBtn").on("click",function(){
        window.close();
    });
    /*
     * This method is used to get logged in user role details
     */
    this.getUserProfileDetails = function() {

        var userProfileRequestJSON = {
            "roles" : ""
        };
        var userProfileURL = CONSTANTS.GET_USER_PROFILE;
        var successCallbackFunction = $.Callbacks('once');
        successCallbackFunction.add(this.getUserProfileSuccessHandler);
        var errorCallbackFunction = $.Callbacks('once');
        errorCallbackFunction.add(this.onSubmitError);
        RSASERVICES.callMethodPOST(userProfileRequestJSON, userProfileURL,
                this.getUserProfileSuccessHandler.bind(this), this.onSubmitError.bind(this));
    };
    /*
     * This method is launched on success of getUserprofile and display user
     * information
     */
    this.getUserProfileSuccessHandler = function(responseJSON) {
        var userProfileDetails = responseJSON.UserProfileResponseDTO;
        MODEL.userProfileDetails = userProfileDetails;
        if (userProfileDetails.Status === CONSTANTS.STATUS_SUCCESS) {
            MODEL.loginUserId = userProfileDetails.userId;
            $("#loggedInUserName").text(
                    "Welcome" + " " + userProfileDetails.firstName + " "
                            + userProfileDetails.lastName);
        }
    };
    /*
     * This method is used to validate requisition details and call service for
     * update.
     */
    this.validate = function() {
        var $requistionValue = $("#requisitionNumber").val();
        if ($requistionValue === "") {
            $('#emailModalId').modal('show');
            $("#emailModalLabel").text("No Requistions Entered.");
            $(".emailModalBody").text(
                    "Please enter at least one requisition number.");
        } else {
            var serviceURL = CONSTANTS.RESEND_QUEWEB_URL;
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.onSubmitSuccess);
            var errorCallbackFunction = $.Callbacks('once');
            errorCallbackFunction.add(this.onSubmitError);
            var quewebRequestJSON = $requistionValue;
            RSASERVICES.resendQueWebEmail(quewebRequestJSON, serviceURL,
                    callbackFunction, errorCallbackFunction);

        }
    };
    /*
     * This method is called on submit
     */
    this.onSubmitError = function() {
        $.unblockUI();
    };
    /*
     * This Method is called on close
     */
    this.closeWindow = function() {
        window.close();
    };
    /*
     * This method is called on success of submit service call
     */
    this.onSubmitSuccess = function(successJson) {
        var response = successJson.Response;
        if (response.status === CONSTANTS.SUCCESS) {
            if (response.successMsg) {
                $('#emailModalId').modal('show');
                $(".emailModalBody").text(
                        "The following requisitions were unable to send:"
                                + response.successMsg);
                $("#emailModalLabel").text("Some emails were not sent");
            } else {
                $('#emailModalId').modal('show');
                $("#emailModalLabel").text("");
                $(".emailModalBody").text(
                        "All emails have been sent successfully");
            }
        }
    };
};
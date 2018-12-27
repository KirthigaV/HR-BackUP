/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: staffingform.js
 * Application: Staffing Administration
 *
 * Used to load the staffing forms
 * This method has the staffing forms details,
 *
 * @param inputXml
 * XML/JSON the parameters are boolean variable and callbacks
 *
 * @param version
 * Optional parameter, can be used later to change the request/response/logic that gets
 * applied.
 * Version 1 XML Input/Output,
 * version 2 JSON Input/Output
 *
 * @return
 * XML/JSON containing the user profile for
 * data provided to load.
 * This XML/JSON is generated and returns
 * CONSTANTS.userProfileDetailsInform object
 */
/*
 * Method is used to load the staffing
 * form details with three links.
 * it has validate the user profile service to
 * check initially.
 * It has 3 links,
 * 1 - staffing forms
 * 2 - Interview availability
 * 3 - Interview results
 */
function staffingForm() {
    this.initialize = function() {
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.successUserProfile.bind(this));
        var errorCallbackFunction = $.Callbacks('once');
        errorCallbackFunction.add(this.serviceErrorHandler.bind(this));
        RSASERVICES.getUserProfile("", callbackFunction, errorCallbackFunction,
                false, true, "");
        var currentLocation = window.location.href;
        //Click on staffing request link redirects to staffing forms page
        $("#staffingReqLink").on(
                "click",
                function() {
                    window.location.href = currentLocation.split(
                            "StaffingForms/StaffingFormsMain.html").join(
                            "RetailStaffing/RetailStaffingRequest.html");
                });
      //Click on interviewAvailability link redirects to interviewAvailability page
        $("#interviewAvailabilityLink").on(
                "click",
                function() {
                    window.location.href = currentLocation.split(
                            "StaffingForms/StaffingFormsMain.html").join(
                            "StaffingForms/InterviewAvailability.html");
                });
      //Click on interviewAvailability link redirects to interviewAvailability page
        $("#interviewSummaryLink").on(
                "click",
                function() {
                    window.location.href = currentLocation.split(
                            "StaffingForms/StaffingFormsMain.html").join(
                            "RetailStaffing/InterviewResults.html");
                });
    };
    /*
     * Method has the service handler
     * if service fails it will throw popup
     * for the message provided
     */
    this.serviceErrorHandler = function() {
        this
                .showModalPopUp("Unable to get your user profile information. Please try again later.");
    };
    /*
     * Method has the functions can used to
     * show,block and drag the popup
     */
    this.showModalPopUp = function(msg) {
        $("#warningpopup").modal({
            backdrop : 'static',
            keyboard : false
        });
        $("#warningpopup").modal("show");
        $("#warningpopup").on('shown.bs.modal', function() {
            $("#warningpopup #warningOkbutton").focus();
        });
        $("#warningpopup").draggable({
            handle : ".modal-header"
        });
        $("#warningpopup .sureMsg").html(msg);
        $("#warningpopup #warningOkbutton").attr("data-dismiss", "modal");
        $("#warningpopup #warningCancelbutton").hide();
    };
    /*
     * Method has the success response for
     * user profile service
     */
    this.successUserProfile = function(response) {
        $.unblockUI();
        CONSTANTS.userProfile = {};
        CONSTANTS.userProfileDetailsInform = {};
        //validate the condition in which the error code is equal to 0
        if (response && response.UserProfileResponseDTO
                && response.UserProfileResponseDTO.ErrorCd === 0) {
            CONSTANTS.userProfileDetailsInform = response;
            CONSTANTS.userProfile.userId = response.UserProfileResponseDTO.userId;
            $('.navbar-right .WelcomeText').text(
                    "Welcome " + response.UserProfileResponseDTO.firstName
                            + " " + response.UserProfileResponseDTO.lastName);
            $('.navbar-right .Storenumber').text(
                    "Store# " + response.UserProfileResponseDTO.strNbr + "");
        } else {
            this
                    .showModalPopUp("Unable to get your user profile information. Please try again later.");
        }
    };
}
function interviewResults() {
    /*
     * This method is used to call service on screen load
     * to populate the user details.
     * All the variables are initialized.
     * DatePicker is set for the Interview date and Offer Date fields.
     * Question section has been appended.
     */
    this.initialize = function() {
    
    	CONSTANTS.interviewResultsObj = this;
    	var isInitDTFlag = false;
    	var isInitCMFlag = false;
    	var isInitBgcFlag = false;
    	var envVars = "";
    	var fapl = "";
    	var fapAuthLp = "";
    	var bgcFAPAWholeLink = "";
    	// Used for showing or hiding Minor Consent Panel.
    	var isCandMinorFlag = false;
    	var allParentalConsentFormsComplete = "notstarted";
    	// Used for Paperless Onboarding Minor Consent URI
    	var dcsuri = "";
    	// Used for showing or hiding DrugTestPanel in pilot and allowing BG Checks Panel to unlock.
    	var showDTPanel = false;
    	//Used for showing or hiding New BGC Panel in Pilot
    	var showNewBgcPanel = false;
    	
    	var faauth = false;
    	var eorigin = "";
    	var faportal = null;
    	this.popup = new rsaPopup();
        CONSTANTS.interviewFormCancel = false;
        CONSTANTS.isDirtyIntvwSection = false;
        CONSTANTS.isDirtyOfferSection = false;
        CONSTANTS.isDirtyLicenseSection = false;
        this.flag_nameOfInterviewer = false;
        this.flag_dateOfInterview = false;
        this.flag_SIG = false;
        this.flag_1RdBtn = false;
        this.flag_2RdBtn = false;
        this.flag_3RdBtn = false;
        this.flag_4RdBtn = false;
        this.flag_5RdBtn = false;
        this.flag_6RdBtn = false;
        this.flag_7RdBtn = false;
        this.flag_8RdBtn = false;
        this.flag_9RdBtn = false;
        this.flag_10RdBtn = false;
        document.msCapsLockWarningOff = true;
        CONSTANTS.interviewResultsObj.selectedCandidate = {};
        CONSTANTS.interviewResultsObj.selectedRequisition = {};

    	var days = [ "S", "M", "T", "W", "T", "F", "S" ];
        $('#interviewDt, #offerDt').datepicker({
            beforeShow : this.styleDatePicker.bind(this),
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top",
            dayNamesMin : days,
            maxDate: new Date()
        });
        var appendDiv = "";
        var dtestType = "";
        $("#qnsSections").empty();
        $("#ID_ShowUnfavorable").hide();
        $("#interviewDetailsMessage").hide();
        $("#rehireMessage").hide();
        $("#backgroundCheckText").text("");
        this.blockFormElements("#intAvailMatchDiv");
        this.blockFormElements("#nameRowDiv");
        this.blockFormElements("#dateRowDiv");
        
        $("#bgcFAPAMsg").text("");
        $('#ui-datepicker-div').hide();
        $('.modal-backdrop').empty().remove();
        this.blockFormElements(".intDet");
        this.blockFormElements(".offerDet");
        this.blockFormElements(".driverLicDet");
        this.blockFormElements(".bgcOrderDet");  
        this.blockFormElements(".candidatePersonalData");
        $(".minorConsent").css("display", "none");
        this.blockFormElements(".minorConsent"); $("#minorConsentButton").hide(); $("#minorConsentMsg").hide(); $("#minorConsentFormsStatusLabel").hide(); $("#minorConsentFormsStatus").hide();
        this.blockFormElements(".drugTestProf"); $("#drugTestButtons").hide(); $("#drugTestMsg").hide(); $("#drugTestConvMsg").hide();
        this.blockFormElements(".bgcFAPA");
        this.clearBgcFAPAPanel();
        
        for ( var k = 1; k < 11; k++) {
            appendDiv = "<div class='row marginTopAlign'><div class='col-xs-12'><div class='row'><div class='col-xs-1 text-center noPaddingDiv'><label>Question " + k + "</label></div><div id='radioBtnsAppendQn" + k + "'></div></div></div></div>";
            $("#qnsSections").append(appendDiv);
        }

        for ( var i = 1; i < 11; i++) {
            for ( var j = 1; j < 7; j++) {
                $("#radioBtnsAppendQn" + i).empty();
                $("#radioBtnsAppendQn" + i).append(
                        '<div id="ID_' + i + 'QusRDGroup"><div class="col-xs-2 text-center alignRadio"><input type="radio" id="ID_' + i + 'QusHF" name="' + i + 'Qus" value="5" onclick=""></div><div class="col-xs-1 text-center"><input type="radio" id="ID_' + i + 'QusF" name="' + i + 'Qus" value="4" onclick=""></div><div class="col-xs-1 text-center"><input type="radio" id="ID_' + i + 'QusA" name="' + i
                                + 'Qus" value="3" onclick=""></div><div class="col-xs-1 text-center"><input type="radio" id="ID_' + i + 'QusU" name="' + i + 'Qus" value="2" onclick=""></div><div class="col-xs-2 text-center"><input type="radio" id="ID_' + i + 'QusHU" name="' + i + 'Qus" value="1" onclick=""></div><div class="col-xs-1 text-center"><input type="radio" id="ID_' + i + 'QusNA" checked="checked" name="' + i
                                + 'Qus" value="0" onclick=""></div></div><div class="col-xs-2 text-center"><label id="ID_' + i + 'QusLbl"></label></div>');
            }

        }
        
        //MTS1876 This will used to get the Store Number, Requisition Number, and Applicant ID from the URL if it exists.  This will be 
        //  coming from Staffing Workflow.
        this._params = {};
        this.urlStoreNumberValue = "";
        this.urlRequisitionNumberValue = "";
        this.urlApplicantIdValue = "";
        //query string from window location
        var queryString = window.location.search.substring(1);
        if (queryString) {
            //split the query string with special character
            var params = queryString.split('&');
            var queryStringLength = params.length;
            for ( var i = 0, index = -1; i < queryStringLength; i++) {
                var kvPair = params[i];
                if ((index = kvPair.indexOf("=")) > 0) {
                    var key = kvPair.substring(0, index);
                    var value = kvPair.substring(index + 1);
                    this._params[key] = value;
                }
            }
            this.urlStoreNumberValue = this._params.strNbr;
            this.urlRequisitionNumberValue = this._params.reqNbr;
            this.urlApplicantIdValue = this._params.applId;
        }
        //*************************************************************************************************
        
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.setUserProfileValues);
        RSASERVICES.getUserProfileDetailss("", callbackFunction, false, true, "Please wait...");
        $("#interviewResults").bind("scroll",function(){
            $(".selectboxit-options.selectboxit-list").css("display", "none");
            $(".selectboxit-options.selectboxit-list").css("cursor", "wait");
        });
    };

    /*
     * set styling for date picker
     *
     * @param N/A
     * @return N/A
     */
    this.styleDatePicker = function() {
        $('#ui-datepicker-div').removeClass(function() {
            return $('input').get(0).id;
        });
        $('#ui-datepicker-div').addClass("datepicker-flex");
    };
    $('#interviewDt, #offerDt').keypress(function() {
        event = event || window.event;
        var charCode = event.which || event.keyCode;
        var charStr = String.fromCharCode(charCode);
        if(charStr === "-"){
            var carBegin = $(this).caret().begin;
            var carEnd = $(this).caret().end;
            var datestr = $(this).val().toString().substring(0,carBegin)+ "-" + $(this).val().toString().substring(carEnd);
            $(this).val(datestr);
            if(carBegin === carEnd){
                $(this).setCaretPosition(carBegin+1);
            }
            else{
                $(this).setCaretPosition(carBegin+1);
            }
        }
        return true;
    });
    /*
     * This method is used to set logged in user information
     * Sets user as Guest if user details not retrieved from service
     * Invokes Interview Results Screen details service
     *
     * @param response
     * @return N/A
     */
    this.setUserProfileValues = function(json) {
        $.unblockUI();
        CONSTANTS.userProfile = json.UserProfileResponseDTO;
        if (CONSTANTS.userProfile && CONSTANTS.userProfile.lastName) {
            CONSTANTS.userName = "Welcome " + CONSTANTS.userProfile.firstName + " " + CONSTANTS.userProfile.lastName;
            CONSTANTS.phnUsrId = CONSTANTS.userProfile.userId;
            CONSTANTS.userEmail = CONSTANTS.userProfile.emailAddress;
        } else {
            CONSTANTS.userProfile = {};
            CONSTANTS.userName = "Guest";
            CONSTANTS.userProfile.firstName = "Test";
            CONSTANTS.userProfile.lastName = "User";
            CONSTANTS.userProfile.userId = "test1234";
        }
        $("#userWelcome").text(CONSTANTS.userName);

        document.getElementById("currentMgrLdap").value = CONSTANTS.userProfile.userId;
        
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(CONSTANTS.interviewResultsObj.loadIntResScrDtls);
        RSASERVICES.getInterviewResultsLoadScreen(callbackFunction, true, "Please wait...");
    };
    /*
     * This method is used to load the interview results details
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.loadIntResScrDtls = function(json) {
        $.unblockUI();

        CONSTANTS.InterviewNoReasonList = [];
        CONSTANTS.OfferMadeList = [];
        CONSTANTS.OfferResultList = [];
        CONSTANTS.OfferDeclineReasonList = [];
        CONSTANTS.StructuredInterviewGuideList = [];
        CONSTANTS.reqStateDet = [];
        
        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            // check whether status is Success or Error
            // Get the InterviewNoReason details from the response
            if (json.Response.hasOwnProperty("InterviewNoReasonList")) {
                CONSTANTS.interviewResultsObj.interviewNoReasonList(json);
            }

            // Get the OfferMadeList details from the response
            if (json.Response.hasOwnProperty("OfferMadeList")) {
                CONSTANTS.interviewResultsObj.offerMadeList(json);
            }

            // Get the OfferResultList details from the response
            if (json.Response.hasOwnProperty("OfferResultList")) {
                CONSTANTS.interviewResultsObj.offerResultList(json);
            }

            // Get the OfferDeclineReasonList details from the response
            if (json.Response.hasOwnProperty("OfferDeclineReasonList")) {
                CONSTANTS.interviewResultsObj.offerDeclineReasonList(json);
            }

            // Get the StructuredInterviewGuideList details from the response
            if (json.Response.hasOwnProperty("StructuredInterviewGuideList")) {
                CONSTANTS.interviewResultsObj.structuredInterviewGuideList(json);
            }

            // Get the State details from the response
            if (json.Response.hasOwnProperty("StateDetailList")) {
                CONSTANTS.interviewResultsObj.stateDetailList(json);
            }

        } else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewResultsObj.popup.alert(json.Response.error.errorMsg);
        }

        CONSTANTS.interviewResultsObj.populatingFields();
    };

    /*
     * This method is used to populate fields.
     * Prompt shown to enter the store number
     * to the load the corresponding requisitions
     *
     * @param N/A
     * @return N/A
     */
    this.populatingFields = function() {
        // populating fields
        $(".userName").text(CONSTANTS.userProfile.firstName + " " + CONSTANTS.userProfile.lastName);
        $(".userID").text(CONSTANTS.userProfile.userId);

        $("#noInterviewReason").selectBoxIt({dynamicPositioning:false});
        $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
        $("#sigNum").selectBoxIt({dynamicPositioning:false});
        $("#sigNum").data("selectBox-selectBoxIt").refresh();
        $("#offerMadeCbo").selectBoxIt({dynamicPositioning:false});
        $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
        $("#offerResultCbo").selectBoxIt({dynamicPositioning:false});
        $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
        $("#declineReasonCbo").selectBoxIt({dynamicPositioning:false});
        $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
        $("#LI_licStateCbo").selectBoxIt({dynamicPositioning:false});
        $("#addressState").selectBoxIt({dynamicPositioning:false});
        $("#driversLicenseState").selectBoxIt({dynamicPositioning:false});
        $("#LI_licStateCbo, #noInterviewReason, #sigNum, #offerMadeCbo, #offerResultCbo, #declineReasonCbo").bind({
            "open": function(ev) {
                $(ev.currentTarget).siblings().find(".selectboxit-options.selectboxit-list").css("position","fixed");
                                        $(ev.currentTarget).siblings().find(
                                ".selectboxit-options.selectboxit-list")
                                .css(
                                        "top",
                                        $(ev.currentTarget).siblings()
                                                .offset().top
                                                + $(ev.currentTarget)
                                                        .siblings()
                                                        .height());
            }
        });
        $("#LI_licStateCbo").data("selectBox-selectBoxIt").refresh();
        $("#addressState").data("selectBox-selectBoxIt").refresh();
        $("#driversLicenseState").data("selectBox-selectBoxIt").refresh();
        CONSTANTS.interviewResultsObj.appendOptionToSelect('noInterviewReason', 'append', CONSTANTS.InterviewNoReasonList, 'label', 'data');
        CONSTANTS.interviewResultsObj.appendOptionToSelect('sigNum', 'append', CONSTANTS.StructuredInterviewGuideList, 'label', 'data');
        CONSTANTS.interviewResultsObj.appendOptionToSelect('offerMadeCbo', 'append', CONSTANTS.OfferMadeList, 'label', 'data');
        CONSTANTS.interviewResultsObj.appendOptionToSelect('offerResultCbo', 'append', CONSTANTS.OfferResultList, 'label', 'data');
        CONSTANTS.interviewResultsObj.appendOptionToSelect('declineReasonCbo', 'append', CONSTANTS.OfferDeclineReasonList, 'label', 'data');
        CONSTANTS.interviewResultsObj.appendOptionToSelect('LI_licStateCbo', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
        CONSTANTS.interviewResultsObj.appendOptionToSelect('addressState', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
        CONSTANTS.interviewResultsObj.appendOptionToSelect('driversLicenseState', 'append', CONSTANTS.reqStateDet, 'stateName', 'stateCode');
        $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
        $("#sigNum").data("selectBox-selectBoxIt").refresh();
        $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
        $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
        $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
        $("#LI_licStateCbo").data("selectBox-selectBoxIt").refresh();
        $("#addressState").data("selectBox-selectBoxIt").refresh();
        $("#driversLicenseState").data("selectBox-selectBoxIt").refresh();

        var d = new Date();
        var month = d.getMonth() + 1;
        var day = d.getDate();
        var currentDate = (('' + month).length < 2 ? '0' : '') + month + '/' + (('' + day).length < 2 ? '0' : '') + day + '/' + d.getFullYear();
        $("#interviewDt").val(currentDate);
        $("#offerDt").val(currentDate);

        //MTS1876 If there is not a store number in the URL, the Store Selector Popup will fire.  Coming from Staffing Workflow will have the
        //  Store Number in the URL.  Coming from MyApron will not.
        if (CONSTANTS.interviewResultsObj.urlStoreNumberValue == "") {
        	$("#enterStrNumModal").off("shown.bs.modal");
        	$("#enterStrNumModal").on("shown.bs.modal", function() {
            $("#StrNumModalText").focus().select();
        	});
        
        	$("#enterStrNumModal").modal('show');
        	//makes the pop up static
        	$("#enterStrNumModal").modal({
        		"backdrop" : "static",
        		"keyboard" : false
        	});
        	CONSTANTS.interviewResultsObj.promptStaticDraggable('#enterStrNumModal');
        } else {
        	this.saveStoreNo();
        }
        //*************************************************************************************
    };

    /*
     * This method is used to load the InterviewNoReasonList
     *
     * @param response
     * @return N/A
     */
    this.interviewNoReasonList = function(json) {
        var resultList = [];
        var optionDtls = {};
        if (json.Response.InterviewNoReasonList !== {}) {
            // check whether InterviewNoReasonList is an array
            // collection
            if (Array.isArray(json.Response.InterviewNoReasonList.ComboOptions)) {
                resultList = json.Response.InterviewNoReasonList.ComboOptions;
            }
            // InterviewNoReasonList is not an array collection
            else {
                if (json.Response.InterviewNoReasonList.hasOwnProperty("ComboOptions")) {
                    resultList = json.Response.InterviewNoReasonList.ComboOptions;
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                for ( var x = 0; x < resultList.length; x++) {
                    optionDtls = {};
                    optionDtls.label = resultList[x].label;
                    optionDtls.data = resultList[x].data;
                    optionDtls.enabled = null;
                    CONSTANTS.InterviewNoReasonList.push(optionDtls);
                }
            }
        }
    };

    /*
     * This method is used to load the offerMadeList
     *
     * @param response
     * @return N/A
     */
    this.offerMadeList = function(json) {
        var resultList = [];
        var optionDtls = {};
        if (json.Response.OfferMadeList !== {}) {
            // check whether OfferMadeList is an array collection
            if (Array.isArray(json.Response.OfferMadeList.ComboOptions)) {
                resultList = json.Response.OfferMadeList.ComboOptions;
            }
            // OfferMadeList is not an array collection
            else {
                if (json.Response.OfferMadeList.hasOwnProperty("ComboOptions")) {
                    resultList = [ json.Response.OfferMadeList.ComboOptions ];
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                for ( var x = 0; x < resultList.length; x++) {
                    optionDtls = {};
                    optionDtls.label = resultList[x].label;
                    optionDtls.data = resultList[x].data;
                    optionDtls.enabled = null;
                    optionDtls.enabled = (resultList[x].data === "R") ? false : true;
                    CONSTANTS.OfferMadeList.push(optionDtls);
                }
            }
        }
    };

    /*
     * This method is used to load the offerResultList
     *
     * @param response
     * @return N/A
     */
    this.offerResultList = function(json) {
        var resultList = [];
        var optionDtls = {};
        if (json.Response.OfferResultList !== {}) {
            // check whether OfferResultList is an array collection
            if (Array.isArray(json.Response.OfferResultList.ComboOptions)) {
                resultList = json.Response.OfferResultList.ComboOptions;
            }
            // OfferResultList is not an array collection
            else {
                if (json.Response.OfferResultList.hasOwnProperty("ComboOptions")) {
                    resultList = [ json.Response.OfferResultList.ComboOptions ];
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                for ( var y = 0; y < resultList.length; y++) {
                    optionDtls = {};
                    optionDtls.label = resultList[y].label;
                    optionDtls.data = resultList[y].data;
                    optionDtls.enabled = null;
                    CONSTANTS.OfferResultList.push(optionDtls);
                }
            }
        }
    };

    /*
     * This method is used to load the offerDeclineReasonList
     *
     * @param response
     * @return N/A
     */
    this.offerDeclineReasonList = function(json) {
        var resultList = [];
        var optionDtls = {};
        if (json.Response.OfferDeclineReasonList !== {}) {
            // check whether OfferDeclineReasonList is an array
            // collection
            if (Array.isArray(json.Response.OfferDeclineReasonList.ComboOptions)) {
                resultList = json.Response.OfferDeclineReasonList.ComboOptions;
            }
            // OfferDeclineReasonList is not an array collection
            else {
                if (json.Response.OfferDeclineReasonList.hasOwnProperty("ComboOptions")) {
                    resultList = [ json.Response.OfferDeclineReasonList.ComboOptions ];
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                for ( var z = 0; z < resultList.length; z++) {
                    optionDtls = {};
                    optionDtls.label = resultList[z].label;
                    optionDtls.data = resultList[z].data;
                    optionDtls.enabled = null;
                    CONSTANTS.OfferDeclineReasonList.push(optionDtls);
                }
            }
        }
    };

    /*
     * This method is used to load the structuredInterviewGuideList
     *
     * @param response
     * @return N/A
     */
    this.structuredInterviewGuideList = function(json) {
        var resultList = [];
        var optionDtls = {};
        if (json.Response.StructuredInterviewGuideList !== {}) {
            // check whether StructuredInterviewGuideList is an
            // array collection
            if (Array.isArray(json.Response.StructuredInterviewGuideList.ComboOptions)) {
                resultList = json.Response.StructuredInterviewGuideList.ComboOptions;
            }
            // StructuredInterviewGuideList is not an array
            // collection
            else {
                if (json.Response.StructuredInterviewGuideList.hasOwnProperty("ComboOptions")) {
                    resultList = [ json.Response.StructuredInterviewGuideList.ComboOptions ];
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                for ( var m = 0; m < resultList.length; m++) {
                    optionDtls = {};
                    optionDtls.label = resultList[m].label;
                    optionDtls.data = resultList[m].data;
                    optionDtls.enabled = resultList[m].enabled;
                    CONSTANTS.StructuredInterviewGuideList.push(optionDtls);
                }
            }
        }
    };

    /*
     * This method is used to load the stateDetailList
     *
     * @param response
     * @return N/A
     */
    this.stateDetailList = function(json) {
        var resultList = [];
        var stateDtls = {};
        if (json.Response.StateDetailList !== {}) {
            // check whether StateDetailList is an array collection
            if (Array.isArray(json.Response.StateDetailList.StateDetail)) {
                resultList = json.Response.StateDetailList.StateDetail;
            }
            // StateDetailList is not an array collection
            else {
                if (json.Response.StateDetailList.hasOwnProperty("StateDetail")) {
                    resultList = [ json.Response.StateDetailList.StateDetail ];
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                for ( var n = 0; n < resultList.length; n++) {
                    stateDtls = {};
                    stateDtls.stateNbr = resultList[n].stateNbr;
                    stateDtls.stateName = resultList[n].stateName + " - " + resultList[n].stateCode;
                    stateDtls.stateCode = resultList[n].stateCode;
                    CONSTANTS.reqStateDet.push(stateDtls);
                }
            }
        }
    };

    /*
     * This method is used to append options in combo box
     *
     * @param combo box id
     * @param action
     * @param options
     * @param text
     * @param value
     * @return N/A
     */
    this.appendOptionToSelect = function(drpDownId, action, optionsArray, textKey, valueKey) {
        if (action === "append") {
            for ( var i = 0; i < optionsArray.length; i++) {
                var currentObj = optionsArray[i];
                $('#' + drpDownId).append($("<option>", {
                    text : currentObj[textKey],
                    value : currentObj[valueKey]
                }));
                if (drpDownId === "offerMadeCbo") {
                    currentObj.enabled ? '' : $('#' + drpDownId + ' option[value=' + currentObj[valueKey] + '] ').attr("disabled", "disabled");
                }
                if(drpDownId === "sigNum") {
                    currentObj.enabled ? '' : $('#' + drpDownId + ' option[value=' + currentObj[valueKey] + '] ').attr("disabled", "disabled");
                }
            }
        } else {
            $('#' + drpDownId).find('option').remove();
            $('#' + drpDownId).attr('disabled', true);
        }
        $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();

    };
    /*
     * This method is used to save store number
     * Used to load the interview results
     * for the saved Store Number.
     * Thorw alert messages and return if
     * invalid store number
     *
     * @param N/A
     * @return N/A
     */
    this.saveStoreNo = function() {
        
    	//MTS1876 If there is a store number in the URL, it will be saved in the CONSTANTS.storeNo variable else gets the Store Number
    	// from the Store Selector Popup.  Added for Staffing Workflow.
    	if (CONSTANTS.interviewResultsObj.urlStoreNumberValue == "") {
       	  $(".userLoc").text($("#StrNumModalText").val());

          if (($("#StrNumModalText").val().length < 4) || ($("#StrNumModalText").val().length > 4)) {
            CONSTANTS.interviewResultsObj.popup.alert("Please enter a valid four digit StoreNo");
            return;
          }
       	  CONSTANTS.storeNo = $("#StrNumModalText").val();
        } else {
       	  $(".userLoc").text(CONSTANTS.interviewResultsObj.urlStoreNumberValue);
       	  CONSTANTS.storeNo = CONSTANTS.interviewResultsObj.urlStoreNumberValue;
        }
       //*************************************************************************************************************************
        
        $("#enterStrNumModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#interviewResults").removeClass("blurBody");
        });
        $("#enterStrNumModal").modal('hide');

        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.loadInterviewResultsReqsByStr);
        RSASERVICES.getInterviewResultsReqsByStr(CONSTANTS.storeNo, callbackFunction, true, "Please wait...");
    };
    /*
     * This method is used to cancel and
     * close the store no pop up
     *
     * @param N/A
     * @return N/A
     */
    this.cancelStoreNo = function() {
        $.blockUI();
        window.close();
        $(".blockMsg").empty().css({
            "border" : "none"
        });
        $(".blockUI.blockOverlay").empty().css({
            "background-color" : "#fff",
            "cursor" : "auto"
        });
    };
    /*
     * This method is used to load interview results based on selected requision
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.loadInterviewResultsReqsByStr = function(json) {
        $.unblockUI();
        CONSTANTS.RequisitionDtlList = [];
        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS 
            && json.Response.hasOwnProperty("RequisitionDetailList") && json.Response.hasOwnProperty("showDrugTestPanel") 
            && json.Response.hasOwnProperty("showMinorConsentPanel") && json.Response.hasOwnProperty("showNewBGCPanel")) {
            if (json.Response.RequisitionDetailList !== {}) {
            	// unblock the UI, if you have clear data.
            	$('#enterStrNumModal').modal('hide');
            	$('body').removeClass('modal-open');
            	$('.modal-backdrop').remove();
                CONSTANTS.interviewResultsObj.intRequisitionDtlList(json);
                dtestType=json.Response.drugTestTypeForLoc;
            }
            if (!json.Response.showDrugTestPanel) {
                $(".drugTestProf").css("display", "none");
            } 
            showDTPanel = json.Response.showDrugTestPanel;
            
            if (!json.Response.showMinorConsentPanel) {
                $(".minorConsent").css("display", "none");
            } 
            showMinorConsentPanel = json.Response.showMinorConsentPanel;
          
            if (!json.Response.showNewBgcPanel) {
                $(".bgcFAPA").css("display", "none");
            }
            showNewBgcPanel = json.Response.showNewBGCPanel;
            
            if (showNewBgcPanel) {
				$(".driverLicDet").css("display", "none");
				$(".bgcOrderDet").css("display", "none");
				$(".bgcFAPA").css("display", "inherit");
			} else {
				$(".bgcFAPA").css("display", "none");
			}
            
        } else if (json.Response.hasOwnProperty("error")) {
        	//MTS1876 Drill Down Reset
        	//Clear all query parms that were on the URL
            CONSTANTS.interviewResultsObj.urlStoreNumberValue = "";
            CONSTANTS.interviewResultsObj.urlRequisitionNumberValue = "";
            CONSTANTS.interviewResultsObj.urlApplicantIdValue = "";
            CONSTANTS.interviewResultsObj.popup.alert(json.Response.error.errorMsg,function() {
            	$("#enterStrNumModal").modal('show');
                $("#StrNumModalText").val('');
                $("#StrNumModalText").focus().select();
                CONSTANTS.interviewResultsObj.promptStaticDraggable('#enterStrNumModal');
            });
        }
        
        //MTS1876  When there is a store number and requisition number in the URL.  Used from Staffing Workflow.
        if (CONSTANTS.interviewResultsObj.urlRequisitionNumberValue != "") {
          var myIndex = -1;
          //Find the passed in requisition number index in the data grid
          for (var i = 0; i < CONSTANTS.RequisitionDtlList.length; i++) { 
            if (CONSTANTS.RequisitionDtlList[i].reqNbr == CONSTANTS.interviewResultsObj.urlRequisitionNumberValue) { 
        	  myIndex = i;
        	  break; 
        	} 
          } 
          
          if (myIndex < 0) {
        	  CONSTANTS.interviewResultsObj.popup.alert("Selected Requisition not found");
        	  //Clear all query parms that were on the URL
              CONSTANTS.interviewResultsObj.urlStoreNumberValue = "";
              CONSTANTS.interviewResultsObj.urlRequisitionNumberValue = "";
              CONSTANTS.interviewResultsObj.urlApplicantIdValue = "";
          }
          
          CONSTANTS.interviewResultsObj.reqDtlsGrid.setSelectedRows(myIndex); 
          $('#myReqDtlsGrid .grid-canvas').children().removeClass('active');
          $($('#myReqDtlsGrid .grid-canvas').children()[myIndex]).addClass('active');   
          CONSTANTS.interviewResultsObj.reqDtlsGrid.scrollRowIntoView(myIndex, false);
          
          //Fires an onclick notify event so that the requisitions will be loaded.
          CONSTANTS.interviewResultsObj.reqDtlsGrid.onClick.notify({row:myIndex, grid:CONSTANTS.interviewResultsObj.reqDtlsGrid});
        }
    };

    /*
     * This method is used to load the Requisition Details
     *
     * @param response
     * @return N/A
     */
    this.intRequisitionDtlList = function(json) {
        var requisitionDtls = {}; // InterviewResultsRequisitionDGDtlsVO
        var resultList = [];
        var emptyData = [];
        // check whether RequisitionDetailList is an array
        // collection
        if (Array.isArray(json.Response.RequisitionDetailList.RequisitionDetail)) {
            resultList = json.Response.RequisitionDetailList.RequisitionDetail;
        }
        // RequisitionDetailList is not an array collection
        else if (json.Response.RequisitionDetailList.hasOwnProperty("RequisitionDetail")) {
            resultList = [ json.Response.RequisitionDetailList.RequisitionDetail ];
        }
        // check whether the result list is having any value
        // object or not
        if (resultList && resultList.length > 0) {
            for ( var x = 0; x < resultList.length; x++) {
                requisitionDtls = {};
                requisitionDtls.reqNbr = resultList[x].reqNbr;
                requisitionDtls.dept = resultList[x].dept;
                requisitionDtls.jobTtl = resultList[x].jobTtl;
                requisitionDtls.ft = resultList[x].ft;
                requisitionDtls.pt = resultList[x].pt;
                requisitionDtls.fillDt = resultList[x].fillDt;
                requisitionDtls.openings = resultList[x].openings;
                requisitionDtls.store = resultList[x].store;
                requisitionDtls.jobCd = resultList[x].job;
                CONSTANTS.RequisitionDtlList.push(requisitionDtls);
            }
            CONSTANTS.RequisitionDtlList.sort(function(i, j) {
                var d1 = i.reqNbr;
                var d2 = j.reqNbr;
                return d1 - d2;
            });
            emptyData = [];
            CONSTANTS.interviewResultsObj.buildRequisitionDtlGrid(CONSTANTS.RequisitionDtlList);
            CONSTANTS.interviewResultsObj.buildCandidatesDtlGrid(emptyData);
        }
    };
    /*
     * This method is used to build requistion details grid
     * consisting of the following columns:
     * 1. Requistion Number
     * 2. Department
     * 3. Job title
     * 4. FT
     * 5. PT
     * 6. Openings
     * 7. Fill Date
     * On click of a row, the candidates associated
     * with the selected requisition will be loaded
     * in the Candidates grid.
     *
     * @param grid data
     * @return N/A
     */
    this.buildRequisitionDtlGrid = function(gridData) {
        $(".gridContainer").empty();
        $(".gridContainer").append("<div id='myReqDtlsGrid' style='width:100%;'></div>");
        var grid;
        var columns = [ {
            id : "reqNbr",
            sortable : false,
            name : "Number",
            field : "reqNbr",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "dept",
            sortable : false,
            name : "Dept#",
            field : "dept",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "jobTtl",
            sortable : false,
            name : "Job Title",
            field : "jobTtl",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "ft",
            sortable : false,
            name : "FT",
            field : "ft",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "pt",
            sortable : false,
            name : "PT",
            field : "pt",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "openings",
            sortable : false,
            name : "# Of Openings",
            field : "openings",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "fillDt",
            sortable : false,
            name : "Date Needed",
            field : "fillDt",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        } ];
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            defaultSortAsc : true,
            forceFitColumns : true,
            scrollToTop : true,
            enableCellNavigation : true
        };
        var data = [];

        var tempData = gridData;
        if (tempData.constructor !== Array) {
            data.push(tempData);
        } else {
            data = tempData;
        }
        if (data.length < 6) {
            var emptyData = [];
            for ( var i = 0; i < 5; i++) {
                emptyData[i] = data[i];
            }
            data = emptyData;
        }
        CONSTANTS.interviewResultsObj.reqDtlsGrid = new Slick.Grid("#myReqDtlsGrid", data, columns, options);
        CONSTANTS.interviewResultsObj.reqDtlsGrid.setSelectionModel(new Slick.RowSelectionModel());
        CONSTANTS.interviewResultsObj.reqDtlsGrid.invalidate();
        $(window).resize(function() {
            $("#myReqDtlsGrid").width($(".gridContainer").width());
            $(".slick-viewport").width($("#myReqDtlsGrid").width());
            CONSTANTS.interviewResultsObj.reqDtlsGrid.resizeCanvas();
        });
        //on click of a row in the grid
        CONSTANTS.interviewResultsObj.reqDtlsGrid.onClick.subscribe(function(e, args, event) {
            if(CONSTANTS.interviewResultsObj.chkUnsavedData(args)){
            	CONSTANTS.interviewResultsObj.reqDtlsGrid.setSelectedRows([CONSTANTS.reqDtlsRowSelected.oldRow]);
            	e.stopImmediatePropagation();
            } else {
            	CONSTANTS.interviewResultsObj.reqDtlsGrid.setSelectedRows([CONSTANTS.reqDtlsRowSelected.row]);
            	$('#myReqDtlsGrid .grid-canvas').children().removeClass('active');
                $($('#myReqDtlsGrid .grid-canvas').children()[CONSTANTS.reqDtlsRowSelected.row]).addClass('active');
            }
        });
    };

    /*
     * This method is used to add title argument
     * for each cell in the grids to show the cell text
     * to the user on mouse over.
     *
     * @param row id
     * @param cell
     * @param value
     * @return N/A
     */
    this.titleFmtr = function(row, cell, value) {
        return value ? ("<span title='" + value + "'>" + value + "</span>") : "";
    };
    /*
     * This method is used to validate unsaved data
     * in the following scenarios:
     * 1. When user makes any change to Interview section
     * 2. When user makes any change to Offer Details section
     * 3. When user makes changes to both Interview and
     * Offer Details section
     *
     * @param arguments
     * @return N/A
     */
    this.chkUnsavedData = function(args) {
        CONSTANTS.interviewFormCancel = false;
        CONSTANTS.continueLoadCandidates = true;
        CONSTANTS.continueSelectCandidates = false;
        CONSTANTS.reqDtlsRowSelected = args;
        CONSTANTS.reqDtlsRowSelected.oldRow = CONSTANTS.interviewResultsObj.reqDtlsGrid.getSelectedRows()[0];
        if (CONSTANTS.isDirtyIntvwSection && CONSTANTS.isDirtyOfferSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Interview and Offer Details Sections.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return true;
        } else if (CONSTANTS.isDirtyIntvwSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Interview Details Section.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return true;
        } else if (CONSTANTS.isDirtyOfferSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Offer Details Section.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return true;
        } else {
            CONSTANTS.continueLoadCandidates = true;
            CONSTANTS.interviewResultsObj.continueLoadCandidates(args);
        }
        return false;
    };
    /*
     * This method will trigger when ok button
     * clicked in warning popup
     *
     * @param N/A
     * @return N/A
     */
    this.warningOkClicked = function() {
        if (CONSTANTS.interviewFormCancel) {
            CONSTANTS.interviewResultsObj.onCancelButtonOK();
        } else {
        	if (CONSTANTS.continueLoadCandidates) {
        		CONSTANTS.interviewResultsObj.reqDtlsGrid.setSelectedRows([CONSTANTS.reqDtlsRowSelected.row]);
        		$('#myReqDtlsGrid .grid-canvas').children().removeClass('active');
        		$($('#myReqDtlsGrid .grid-canvas').children()[CONSTANTS.reqDtlsRowSelected.row]).addClass('active');
        	}
        	if (CONSTANTS.continueSelectCandidates) {
        		CONSTANTS.interviewResultsObj.candDtlsGrid.setSelectedRows([CONSTANTS.candDtlsRowSelected.row]);
            	$('#myCandDtlsGrid .grid-canvas').children().removeClass('active');
                $($('#myCandDtlsGrid .grid-canvas').children()[CONSTANTS.candDtlsRowSelected.row]).addClass('active');
        	}
            CONSTANTS.continueLoadCandidates ? CONSTANTS.interviewResultsObj.continueLoadCandidates(CONSTANTS.reqDtlsRowSelected) : "";
            CONSTANTS.continueSelectCandidates ? CONSTANTS.interviewResultsObj.continueSelectCandidate(CONSTANTS.candDtlsRowSelected) : "";
        }

    };
    /*
     * This method will trigger when cancel button
     * clicked in warning popup
     *
     * @param N/A
     * @return
     */
    this.warningCANCElClicked = function() {
        if (!CONSTANTS.interviewFormCancel) {
        	if (CONSTANTS.continueLoadCandidates) {
        		CONSTANTS.interviewResultsObj.reqDtlsGrid.setSelectedRows([CONSTANTS.reqDtlsRowSelected.oldRow]);
        		$('#myReqDtlsGrid .grid-canvas').children().removeClass('active');
        		$($('#myReqDtlsGrid .grid-canvas').children()[CONSTANTS.reqDtlsRowSelected.oldRow]).addClass('active');
        	}
        	if (CONSTANTS.continueSelectCandidates) {
        		CONSTANTS.interviewResultsObj.candDtlsGrid.setSelectedRows([CONSTANTS.candDtlsRowSelected.oldRow]);
        		$('#myCandDtlsGrid .grid-canvas').children().removeClass('active');
        		$($('#myCandDtlsGrid .grid-canvas').children()[CONSTANTS.candDtlsRowSelected.oldRow]).addClass('active');
        	}
        }
        return;
    };
    /*
     * This method will get the candidate details based on requisition selected.
     * The retrieved candidate details will be loaded in
     * the Candidate details grid.
     *
     * @param arguments
     * @return N/A
     */
    this.continueLoadCandidates = function(args) {

        if (args.row < args.grid.getDataLength()) {
            // Set currently selected Requisition if one has been selected
            if (args.row !== -1) {
                CONSTANTS.interviewResultsObj.currentSelectedRequisition = args.row;
            }

            CONSTANTS.interviewResultsObj.selectedRequisition = args.grid.getData()[args.row];

            // Clear the Interview Panel Details
            CONSTANTS.interviewResultsObj.clearInterviewDetails();
            // Clear the Offer Details Panel
            CONSTANTS.interviewResultsObj.clearOfferDetails();
            // Clear the Internal License Details Panel
            CONSTANTS.interviewResultsObj.clearIntLicenseDetails();
            // Clear the CPD Form Panel
            CONSTANTS.interviewResultsObj.clearCPDPanel();
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            // Clear the Minor Consent Panel
            CONSTANTS.interviewResultsObj.clearMinorConsentPanel();
            CONSTANTS.interviewResultsObj.clearMinorConsentPanelValues();
            // Clear the Drug Test Panel
            CONSTANTS.interviewResultsObj.clearDrugTestPanel();
            CONSTANTS.interviewResultsObj.clearDrugTestPanelValues();
            // Clear the BGC FAPA Panel
            CONSTANTS.interviewResultsObj.clearBgcFAPAPanel();
            // Reset Screen Dirty Flags
            CONSTANTS.isDirtyIntvwSection = false;
            CONSTANTS.isDirtyOfferSection = false;
            CONSTANTS.isDirtyLicenseSection = false;

            // Clear any CPD or Rehire Messages
            CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage();
            
            // Get Candidates for selected requisition
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(CONSTANTS.interviewResultsObj.loadCandidatesByReq);
            RSASERVICES.getInterviewResultsCandidatesByReq(CONSTANTS.RequisitionDtlList[args.row].reqNbr, callbackFunction, true, "Please wait...");
        }
    };
    /*
     * This method will load the candidate details based on requisition selected.
     * The retrieved candidate details will be loaded in
     * the Candidate details grid.
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.loadCandidatesByReq = function(json) {
        $.unblockUI();
        CONSTANTS.interviewResultsObj.selectedCandidate = {};

        CONSTANTS.CandidateDtlList = [];

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            // check whether status is Success or Error
            if (json.Response.hasOwnProperty("CandidateDetailList") && json.Response.CandidateDetailList !== {}) {
                CONSTANTS.interviewResultsObj.intCandidateDtlList(json);
            } else {
                var emptyData = [];
                CONSTANTS.interviewResultsObj.buildCandidatesDtlGrid(emptyData);
            }
        } else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewResultsObj.popup.alert(json.Response.error.errorMsg);
        }
        
        //MTS1876  When there is a store number, requisition number, and applicant ID in the URL.  Used from Staffing Workflow.
        if (CONSTANTS.interviewResultsObj.urlApplicantIdValue != "") {
          var myIndex = -1;
          //Find the passed in applicant ID index in the data grid
          for (var i = 0; i < CONSTANTS.CandidateDtlList.length; i++) { 
            if (CONSTANTS.CandidateDtlList[i].candId == CONSTANTS.interviewResultsObj.urlApplicantIdValue) { 
        	  myIndex = i;
        	  break; 
        	} 
          } 
          
          if (myIndex < 0) {
        	  CONSTANTS.interviewResultsObj.popup.alert("Selected Candidate not found");
          }
          
          CONSTANTS.interviewResultsObj.candDtlsGrid.setSelectedRows(myIndex); 
          $('#myCandDtlsGrid .grid-canvas').children().removeClass('active');
          $($('#myCandDtlsGrid .grid-canvas').children()[myIndex]).addClass('active');   
          CONSTANTS.interviewResultsObj.candDtlsGrid.scrollRowIntoView(myIndex, false);
          CONSTANTS.interviewResultsObj.candDtlsGrid.onClick.notify({row:myIndex, grid:CONSTANTS.interviewResultsObj.candDtlsGrid});
          if (myIndex >= 0) {
        	  $('#interviewDetailDiv')[0].scrollIntoView(true);
          }
          
          //Clear all query parms that were on the URL
          CONSTANTS.interviewResultsObj.urlStoreNumberValue = "";
          CONSTANTS.interviewResultsObj.urlRequisitionNumberValue = "";
          CONSTANTS.interviewResultsObj.urlApplicantIdValue = "";
        }
        //*********************************************************************************************************************
        
    };

    /*
     * This method is used to load the Candidate Details
     * and builds the Candidate Details grid.
     *
     * @param response
     * @return N/A
     */
    this.intCandidateDtlList = function(json) {
        var candidateDtls = {}; // InterviewResultsCandidateDGDtlsVO
        var resultList = [];
        // check whether CandidateDetailList is an array
        // collection
        if (Array.isArray(json.Response.CandidateDetailList.CandidateDetails)) {
            resultList = json.Response.CandidateDetailList.CandidateDetails;
        }
        // CandidateDetailList is not an array collection
        else if (json.Response.CandidateDetailList.hasOwnProperty("CandidateDetails")) {
            resultList = [ json.Response.CandidateDetailList.CandidateDetails ];
        }
        // check whether the result list is having any value
        // object or not
        if (resultList && resultList.length > 0) {
            for ( var x = 0; x < resultList.length; x++) {
                // Offer Date
                var offerDate = "";
                if (resultList[x].offerDate) {
                    offerDate = resultList[x].offerDate.month + "/" + resultList[x].offerDate.day + "/" + resultList[x].offerDate.year;
                }

                // Interview Submitted Date
                var submitInterviewResultsDT = "";
                if (resultList[x].submitInterviewResultsDT) {
                	submitInterviewResultsDT = resultList[x].submitInterviewResultsDT.month + "/" + resultList[x].submitInterviewResultsDT.day + "/" + resultList[x].submitInterviewResultsDT.year;
                }

                // Offer Submitted Date
                var submitOfferResultsDT = "";
                if (resultList[x].submitOfferResultsDT) {
                	submitOfferResultsDT = resultList[x].submitOfferResultsDT.month + "/" + resultList[x].submitOfferResultsDT.day + "/" + resultList[x].submitOfferResultsDT.year;
                }

                candidateDtls = {};
                candidateDtls.candId = resultList[x].ssnNbr;
                candidateDtls.intvwStatus = CONSTANTS.interviewResultsObj.setIntvwStatus(resultList[x].intStatus);
                candidateDtls.offerMade = CONSTANTS.interviewResultsObj.setOfferMade(resultList[x].offerMade);
                candidateDtls.offerDate = offerDate;
                candidateDtls.offerResult = CONSTANTS.interviewResultsObj.setOfferResult(resultList[x].offerStat);
                candidateDtls.declineReason = resultList[x].decCD;
                candidateDtls.name = resultList[x].name;
                candidateDtls.appAssIndicator = CONSTANTS.interviewResultsObj.setAssocAp(resultList[x].applicantType, resultList[x].organization);
                candidateDtls.aid = resultList[x].aid;
                candidateDtls.candRefNbr = resultList[x].candRefNbr;
              //Added for FMS 7894 January 2016 CR's
                candidateDtls.doNotConsiderFor60Days = resultList[x].doNotConsiderFor60Days;
                candidateDtls.submitInterviewResultsDT = submitInterviewResultsDT;
                candidateDtls.submitOfferResultsDT = submitOfferResultsDT;

                CONSTANTS.CandidateDtlList.push(candidateDtls);
            }
            CONSTANTS.interviewResultsObj.buildCandidatesDtlGrid(CONSTANTS.CandidateDtlList);
        }
    };
    /*
     * This method will help to return the overall interview Status
     * based on the interview status code.
     *
     * @param interview status code
     * @return ""
     */
    this.setIntvwStatus = function(intvwStatusCd) {
        var i = 0;

        if (intvwStatusCd === "F") {
            return "FAVORABLE";
        } else if (intvwStatusCd === "A") {
            return "ACCEPTABLE";
        } else if (intvwStatusCd === "U") {
            return "UNFAVORABLE";
        }else if (intvwStatusCd === "C") {
            return "AVAILABILITY MISMATCH";
        } else {
            for (i = 0; i < CONSTANTS.InterviewNoReasonList.length; i++) {
                if (intvwStatusCd === CONSTANTS.InterviewNoReasonList[i].data) {
                    return CONSTANTS.InterviewNoReasonList[i].label;
                }
            }
        }
        return "";
    };
    /*
     * This method will return OfferMade based on the offerMadeCd
     *
     * @param offer made code
     * @return ""
     */
    this.setOfferMade = function(offerMadeCd) {

        for ( var i = 1; i < CONSTANTS.OfferMadeList.length; i++) {
            if (offerMadeCd === CONSTANTS.OfferMadeList[i].data) {
                return CONSTANTS.OfferMadeList[i].label;
            }
        }
        return "";
    };
    /*
     * This method help to return offer results
     * based on the offerResultCd
     *
     * @param offer result code
     * @return N/A
     */
    this.setOfferResult = function(offerResultCd) {
        for ( var i = 0; i < CONSTANTS.OfferResultList.length; i++) {
            if (offerResultCd === CONSTANTS.OfferResultList[i].data) {
                return CONSTANTS.OfferResultList[i].label;
            }
        }
        return "";
    };
    /*
     * This method will return Associate Applicant type
     * based on service response
     *
     * @param applicant type
     * @param organization
     * @return organization
     */
    this.setAssocAp = function(applicantType, organization) {
        if (applicantType === "INT") {
            return organization;
        }

        return "AP";
    };
    /*
     * This method will help to clear Interview Details
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearInterviewDetails = function() {
        $(".showCandidateNameInterviewPnl").text("");
        $(".showCandidateNameOfferPnl").text("");
        $("input[name='isIntCmpltd']").prop('checked', false);
        $("input[name='doesAvailabilityMatch']").prop('checked', false);
        $("#interviewDetailsMessage").hide();
        //Added for FMS 7894 January 2016 CR's
       $("#doNotConsiderFor60Days").prop('checked', false);
        $("#noInterviewReason option[value='select']").prop('selected', true);
        $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
        $("#interviewerName").val("");
        $("#interviewDt").val("");
        $("#sigNum option[value='select']").prop('selected', true);
        $("#sigNum").data("selectBox-selectBoxIt").refresh();

        for ( var i = 1; i < 11; i++) {
            $("#ID_" + i + "QusNA").prop('checked', true);
            $("#ID_" + i + "QusLbl").text("");
        }
        $("#ID_OverallResultsLbl").text("");

        this.flag_nameOfInterviewer = false;
        this.flag_dateOfInterview = false;
        this.flag_SIG = false;
        this.flag_1RdBtn = false;
        this.flag_2RdBtn = false;
        this.flag_3RdBtn = false;
        this.flag_4RdBtn = false;
        this.flag_5RdBtn = false;
        this.flag_6RdBtn = false;
        this.flag_7RdBtn = false;
        this.flag_8RdBtn = false;
        this.flag_9RdBtn = false;
        this.flag_10RdBtn = false;

        $("#ID_ShowUnfavorable").hide();
        CONSTANTS.isDirtyIntvwSection = false;
        this.blockFormElements(".intDet");
        this.blockFormElements("#doesAvailabilityMatch");
    };
    /*
     * This method will help to clear Offer Details
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearOfferDetails = function() {
        $("#offerMadeCbo option[value='0']").prop('selected', true);
        $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
        $("#offerResultCbo option[value='select']").prop('selected', true);
        $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
        $("#offerDt").val("");
        $("#declineReasonCbo option[value='select']").prop('selected', true);
        $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
        $("input[name='isSignCmpltd']").prop('checked', false);
        $("#backgroundCheckText").text("");

        $("#bgcFAPAMsg").text("");
        CONSTANTS.isDirtyOfferSection = false;
        this.blockFormElements(".offerDet");
        this.hideCpdFormOrRehireEligibleMessage();
    };
    
    /*
     * This method will help to clear CPD Panel
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearCPDPanel = function() {
		$("#cpdFormStatus").text("");
		$("#cpdFormStatus").css("color", "black");
    	this.blockFormElements(".candidatePersonalData");
    };
    
    /*
     * This method will hide the below messages:
     * 1. Candidate's personal details not shared
     * 2. Candidate rehiring message
     *
     * @param N/A
     * @return N/A
     */
    this.hideCpdFormOrRehireEligibleMessage = function() {
		$("#cpdFormStatus").text("");
		$("#cpdFormStatus").css("color", "black");
        $("#rehireMessage").hide();
    };
    
    /*
     * This method will help to clear Minor Consent Panel
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearMinorConsentPanel = function() {
    	$("input[name='isMinorConsentReady']").prop('checked', false);
//        $("input[name='isMinorConsentReady']").attr("enabled", "enabled");
        $("input[name='isMinorConsentReady']").removeAttr("disabled");
        $("#yes_minorConsentRadioBtn").attr("disabled", false);
        $("#minorConsentButton").css("display", "none");
		$("#minorConsentMsgNR").css("display", "none");
		$("#minorConsentFormsStatusLabel").css("display", "none");
		$("#minorConsentFormsStatus").css("display", "none");
		$("#minorConsentFormsStatus").text("");
		$("#minorConsentFormsStatus").css("color", "orangered");
//		allParentalConsentFormsComplete = "";
//		isCandMinorFlag = false;
		isInitCMFlag = false;
		
    $(".minorConsent").css("display", "none");
		$(".minorConsentRadio").unblock();
		this.blockFormElements(".minorConsent");
    };    

    /*
     * This method will help to clear Minor Consent Panel Data Values
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearMinorConsentPanelValues = function() {
		allParentalConsentFormsComplete = "";
		isCandMinorFlag = false;
    };

    
    
    /*
     * This method will help to clear Drug Test Panel UI
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearDrugTestPanel = function() {
    	$("input[name='isDrugTestReady']").prop('checked', false);
        $("#yes_drugTestRadioBtn").attr("disabled", false);
    	$("#drugTestButtons").css("display", "none");
		$("#drugTestMsgNR").css("display", "none");
		$("#VCCOC_Btn").css("display", "inherit");
		$("#drugTestMsg").css("display", "none");
		$("#drugTestConvMsg").css("display", "none");
		isInitDTFlag = false; 
		$("#drugTestInitiatedDate").text("");
		$("#drugTestInitiatedDate").css("color", "black");
		$("#drugTestConvMsg").hide();$("#drugTestMsg").hide(); $("#drugTestButtons").hide();
		this.blockFormElements(".drugTestProf");
    };

    /*
     * This method will help to clear Drug Test Data Values
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearDrugTestPanelValues = function() {
    	$("#orderNbrDT").val("");
    	$("#ApplEmailDT").val("");
    	$("#RequesterEmailDT").val("");
    	$("#orderInitiatedTsDT").val("");
    	$("#orderCompletedTsDT").val("");
    };

	/*
     * This method will help to clear BGC FAPA Panel
     * based on requisition
     *
     * @param N/A
     * @return N/A
     */
    this.clearBgcFAPAPanel = function() {
    	$("#bgcFAPAMsgSection").css("display", "none");
    	$("#bgcFAPAMsg").text("");
    	$("#bgcFAPAQuestion").css("display", "none"); 				//not currently used
    	$("#bgcFAPARadio").css("display", "none"); 					//not currently used
    	$("input[name='isbgcFAPAReady']").prop('checked', false);	//not currently used
        $("#yes_bgcFAPARadioBtn").attr("disabled", false);			//not currently used
		$("#bgcFAPAMsgNR").css("display", "none");					//not currently used
        $("#bgcFAPAEmailEntry").css("display", "none");
        $("#bgcFAPAButton").css("display", "inherit");
        	$("#bgcFAPAInitBtn").css("display", "inherit");
        	$("#bgcFAPAViewBtn").css("display", "none");
        //		isInitBGCFlag = false; 
        $("#bgcFAPAStatusSection").css("display", "none");
        $("#bgcFAPAStatusLabel").hide(); $("bgcFAPAStatus").hide();
        $("#bgcFAPAStatus").val("");
//		this.blockFormElements(".bgcFAPA");
		this.clearEmailDataForm();
    };
    
    /*
     * This method will help to clear Interview License details
     *
     * @param N/A
     * @return N/A
     */
    this.clearIntLicenseDetails = function() {
        $("#LI_licNumTxt").val("");
        $("#LI_licNumTxt_2").val("");
        $("#LI_licStateCbo option[value='select']").prop('selected', true);
        $("#LI_licStateCbo").data("selectBox-selectBoxIt").refresh();
        $("#LI_expDt").val("MM/DD/YYYY");
        CONSTANTS.isDirtyLicenseSection = false;
        this.blockFormElements(".driverLicDet");
    };

    /*
     * This method will help to build candidate details
     * consisting of the following columns:
     * 1. Name
     * 2. Applicant/Associate Indicator
     * 3. Interview Status
     * 4. Offer Made
     * 5. Offer date
     * 6. Offer Result
     * 7. Decline Reason
     * On click of a row, the corresponding Candidate's details will be loaded.
     *
     * @param grid data
     * @return N/A
     */
    this.buildCandidatesDtlGrid = function(gridData) {
        $(".candidateGridContainer").empty();
        $(".candidateGridContainer").append("<div id='myCandDtlsGrid' style='width:100%;'></div>");
        var grid;
        var columns = [ {
            id : "name",
            sortable : false,
            name : "Name",
            field : "name",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "appAssIndicator",
            sortable : false,
            name : "Assoc/Ap",
            field : "appAssIndicator",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "intvwStatus",
            sortable : false,
            name : "Interview",
            field : "intvwStatus",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "submitInterviewResultsDT",
            sortable : false,
            name : "Interview Submitted",
            field : "submitInterviewResultsDT",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "offerMade",
            sortable : false,
            name : "Offer Made",
            field : "offerMade",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "offerDate",
            sortable : false,
            name : "Offer Date",
            field : "offerDate",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "offerResult",
            sortable : false,
            name : "Offer Result",
            field : "offerResult",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "declineReason",
            sortable : false,
            name : "Decline Reason",
            field : "declineReason",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }, {
            id : "submitOfferResultsDT",
            sortable : false,
            name : "Offer Submitted",
            field : "submitOfferResultsDT",
            formatter : CONSTANTS.interviewResultsObj.titleFmtr
        }];
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            defaultSortAsc : true,
            forceFitColumns : true,
            scrollToTop : true,
            enableCellNavigation : true
        };
        var data = [];

        var tempData = gridData;
        if (tempData.constructor !== Array) {
            data.push(tempData);
        } else {
            data = tempData;
        }
        if (data.length < 5) {
            var emptyData = [];
            for ( var i = 0; i < 4; i++) {
                emptyData[i] = data[i];
            }
            data = emptyData;
        }
        CONSTANTS.interviewResultsObj.candDtlsGrid = new Slick.Grid("#myCandDtlsGrid", data, columns, options);
        CONSTANTS.interviewResultsObj.candDtlsGrid.setSelectionModel(new Slick.RowSelectionModel());
        $(window).resize(function() {
            $("#myCandDtlsGrid").width($(".candidateGridContainer").width());
            $(".slick-viewport").width($("#myCandDtlsGrid").width());
            CONSTANTS.interviewResultsObj.candDtlsGrid.resizeCanvas();
        });
        CONSTANTS.interviewResultsObj.candDtlsGrid.onClick.subscribe(function(e, args, event) {
            if(CONSTANTS.interviewResultsObj.candChkUnsavedData(args)){
            	CONSTANTS.interviewResultsObj.candDtlsGrid.setSelectedRows([CONSTANTS.candDtlsRowSelected.oldRow]);
            	e.stopImmediatePropagation();
            } else {
            	CONSTANTS.interviewResultsObj.candDtlsGrid.setSelectedRows([CONSTANTS.candDtlsRowSelected.row]);
            	$('#myCandDtlsGrid .grid-canvas').children().removeClass('active');
                $($('#myCandDtlsGrid .grid-canvas').children()[CONSTANTS.candDtlsRowSelected.row]).addClass('active');
            }
        });
    };
    /*
     * This method is used to validate unsaved data
     * in the following scenarios:
     * 1. When user makes any change to Interview section
     * 2. When user makes any change to Offer Details section
     * 3. When user makes changes to both Interview and
     * Offer Details section
     *
     * @param arguments
     * @return N/A
     */
    this.candChkUnsavedData = function(args) {
        CONSTANTS.interviewFormCancel = false;
        CONSTANTS.continueSelectCandidates = true;
        CONSTANTS.continueLoadCandidates = false;
        CONSTANTS.candDtlsRowSelected = args;
        CONSTANTS.candDtlsRowSelected.oldRow = CONSTANTS.interviewResultsObj.candDtlsGrid.getSelectedRows()[0];
        if (CONSTANTS.isDirtyIntvwSection && CONSTANTS.isDirtyOfferSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Interview and Offer Details Sections.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return true;
        } else if (CONSTANTS.isDirtyIntvwSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Interview Details Section.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return true;
        } else if (CONSTANTS.isDirtyOfferSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Offer Details Section.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return true;
        } else {
            CONSTANTS.continueSelectCandidates = true;
            CONSTANTS.interviewResultsObj.continueSelectCandidate(args);
        }
        return false;
    };
    /*
     * This method is used to load the details of the selected candidate.
     * The screen details are cleared before loading the details
     * of the selected candidate.
     *
     * @param arguments
     * @return N/A
     */
    this.continueSelectCandidate = function(args) {
        if (args.row < args.grid.getDataLength()) {
            // Set currently selected Candidate
            CONSTANTS.interviewResultsObj.currentSelectedCandidate = args.row;
            // Clear the Interview Panel Details
            CONSTANTS.interviewResultsObj.clearInterviewDetails();
            // Clear the Offer Details Panel
            CONSTANTS.interviewResultsObj.clearOfferDetails();
            // Clear the Internal License Details Panel
            CONSTANTS.interviewResultsObj.clearIntLicenseDetails();
            // Clear the CPD Form Panel
            CONSTANTS.interviewResultsObj.clearCPDPanel();
            /* this.blockFormElements(".bgcOrderDet");*/
            // Clear the Minor Consent Panel
            CONSTANTS.interviewResultsObj.clearMinorConsentPanel();
            CONSTANTS.interviewResultsObj.clearMinorConsentPanelValues();
            // Clear the Drug Test Panel
            CONSTANTS.interviewResultsObj.clearDrugTestPanel();
            CONSTANTS.interviewResultsObj.clearDrugTestPanelValues();
            CONSTANTS.interviewResultsObj.clearBgcFAPAPanel();
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            // Reset Screen Dirty Flags
            CONSTANTS.isDirtyIntvwSection = false;
            CONSTANTS.isDirtyOfferSection = false;
            CONSTANTS.isDirtyLicenseSection = false;
            CONSTANTS.interviewResultsObj.selectedCandidate = args.grid.getData()[args.row];
            if (CONSTANTS.interviewResultsObj.selectedCandidate && CONSTANTS.interviewResultsObj.selectedCandidate.hasOwnProperty('name')) {
                // Set Selected Candidate Name in Interview Details
                $(".showCandidateNameInterviewPnl").text(CONSTANTS.interviewResultsObj.selectedCandidate.name);
                // Set Selected Candidate Name in Offer Details
                $(".showCandidateNameOfferPnl").text(CONSTANTS.interviewResultsObj.selectedCandidate.name);
                // Get Candidate Interview Details
                if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator === "AP") {
                    CONSTANTS.interviewResultsObj.applicantType = "EXT";
                    CONSTANTS.interviewResultsObj.organization = "";
                } else {
                    CONSTANTS.interviewResultsObj.applicantType = "INT";
                    CONSTANTS.interviewResultsObj.organization = CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator;
                }
                var inputData = {
                    "IntvwResultsCandIntvwDtlsRequest" : {
                        "reqNbr" : CONSTANTS.interviewResultsObj.selectedRequisition.reqNbr,
                        "candId" : CONSTANTS.interviewResultsObj.selectedCandidate.candId,
                        "strNo" : CONSTANTS.storeNo,
                        "jobTtlCd" : CONSTANTS.interviewResultsObj.selectedRequisition.jobCd,
                        "deptNo" : CONSTANTS.interviewResultsObj.selectedRequisition.dept,
                        "applicantType" : CONSTANTS.interviewResultsObj.applicantType,
                        "organization" : CONSTANTS.interviewResultsObj.organization
                    }
                };
                var callbackFunction = $.Callbacks('once');
                callbackFunction.add(CONSTANTS.interviewResultsObj.loadInterviewResultsCandidateIntvwDtls);
                RSASERVICES.getInterviewResultsCandidateIntvwDtls(inputData, callbackFunction, true, "Please wait...");
                CONSTANTS.interviewResultsObj.setCandidateDetails();
            }
        }
    };

    /*
     * This method is used to load the InterviewDtls
     *
     * @param N/A
     * @return N/A
     */
    this.setInterviewDtls = function() {
        var i = 0;
        // Sets the Yes or No Was Interview Completed
        if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === "FAVORABLE" || CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === "ACCEPTABLE" || CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === "UNFAVORABLE") {
            $("input[name='isIntCmpltd'][value='Y']").prop('checked', true);
            $("input[name='doesAvailabilityMatch'][value='Y']").prop('checked', true);
            this.blockFormElements("#intAvailMatchDiv");
            this.blockFormElements("#nameRowDiv");
            this.blockFormElements("#dateRowDiv");
            this.blockFormElements(".doNotConsiderDiv");
        }  else if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === "AVAILABILITY MISMATCH") {
            $("input[name='isIntCmpltd'][value='Y']").prop('checked', true);
            $("input[name='doesAvailabilityMatch'][value='N']").prop('checked', true);
            $("input[name='doesAvailabilityMatch']:checked").value = CONSTANTS.NO;
            $("#intAvailMatchDiv").unblock();
            $("#nameRowDiv").unblock();
            $("#dateRowDiv").unblock();
            $(".doNotConsiderDiv").unblock();

        }   else if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus !== "") {
            $("input[name='isIntCmpltd'][value='N']").prop('checked', true);
            this.blockFormElements("#intAvailMatchDiv");
            this.blockFormElements("#nameRowDiv");
            this.blockFormElements("#dateRowDiv");
            this.blockFormElements(".doNotConsiderDiv");
            // Sets the No Reason Combo
            for (i = 0; i < CONSTANTS.InterviewNoReasonList.length; i++) {
                if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === CONSTANTS.InterviewNoReasonList[i].label) {
                    $('#noInterviewReason option:eq(' + (i + 1) + ')').prop('selected', true);
                    $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
                }
            }
            $('#noInterviewReason').removeAttr("disabled");
            $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
            $(".noReasonDiv").unblock();
        } else if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === ""){
            this.blockFormElements("#intAvailMatchDiv");
            this.blockFormElements("#nameRowDiv");
            this.blockFormElements("#dateRowDiv");
            this.blockFormElements(".doNotConsiderDiv");
            // Sets the No Reason Combo
            for (i = 0; i < CONSTANTS.InterviewNoReasonList.length; i++) {
                if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === CONSTANTS.InterviewNoReasonList[i].label) {
                    $('#noInterviewReason option:eq(' + (i + 1) + ')').prop('selected', true);
                    $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
                }
            }
            $('#noInterviewReason').removeAttr("disabled");
            $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
            $(".noReasonDiv").unblock();
        }

        //Added for FMS 7894 January 2016 CR's
        if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "Y") {
        	$("input[name='doNotConsiderFor60Days']").prop('checked', true);
        	$("#interviewDetailsMessage").hide();
        }
    };

    /*
     * This method is used to set the Candidate Details
     *
     * @param N/A
     * @return N/A
     */
    this.setCandidateDetails = function() {
        var i = 0;
        // Set Interview Details
        CONSTANTS.interviewResultsObj.setInterviewDtls();

        // Set Offer Details based on Candidate
        for (i = 0; i < CONSTANTS.OfferMadeList.length; i++) {
            if (CONSTANTS.interviewResultsObj.selectedCandidate.offerMade === CONSTANTS.OfferMadeList[i].label) {
                $('#offerMadeCbo option:eq(' + i + ')').prop('selected', true);
                $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
            }
        }
        // Set Offer Date
        $("#offerDt").val(CONSTANTS.interviewResultsObj.selectedCandidate.offerDate);

        // Set Offer Result
        for (i = 0; i < CONSTANTS.OfferResultList.length; i++) {
            if (CONSTANTS.interviewResultsObj.selectedCandidate.offerResult === CONSTANTS.OfferResultList[i].label) {
                $('#offerResultCbo option:eq(' + (i + 1) + ')').prop('selected', true);
                $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
            }
        }
        // Offer Decline Reason
        for (i = 0; i < CONSTANTS.OfferDeclineReasonList.length; i++) {
            if (CONSTANTS.interviewResultsObj.selectedCandidate.declineReason === CONSTANTS.OfferDeclineReasonList[i].label) {
                $('#declineReasonCbo option:eq(' + (i + 1) + ')').prop('selected', true);
                $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
            }
        }
        CONSTANTS.interviewResultsObj.setOfferDtls();

        if ($("#offerMadeCbo option:selected").index() !== 1) {
            this.blockFormElements(".backgrndRadio");
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            this.blockFormElements(".drugTestProf");
            $("input[name='isSignCmpltd']").prop('checked', false);
            $("#backgroundCheckText").text("");

            $("#bgcFAPAMsg").text("");
            this.blockFormElements(".driverLicDet");
        }
    };

    /*
     * This method is used to load the OfferDtls
     *
     * @param N/A
     * @return N/A
     */
    this.setOfferDtls = function() {
        // Set the fields for Interview and Offer Panels and fields
        if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus !== "") {
            if ($("#noInterviewReason option:selected").index() > 0) {
                $(".intDet").unblock();
                this.blockFormElements(".nameRowDiv");
                this.blockFormElements(".dateRowDiv");
                //this.blockFormElements(".strcutRow");
                this.blockFormElements(".categoryNameRow");
                this.blockFormElements("#qnsSections");
                this.blockFormElements(".overallStatRow");
                //Added for FMS 7894 January 2016 CR's
                $(".strcutRow").unblock();
                this.blockFormElements(".strcutDiv");
                $(".intCompltdDiv").unblock();
                if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "N") {
                	$(".doNotConsiderDiv").unblock();
                } else {
                	this.blockFormElements(".doNotConsiderDiv");
                }
                //*****************************
            } else {
            	$(".intDet").unblock();
                this.blockFormElements(".nameRowDiv");
                this.blockFormElements(".dateRowDiv");
            	this.blockFormElements(".categoryNameRow");
            	this.blockFormElements("#qnsSections");
            	this.blockFormElements(".overallStatRow");
            	this.blockFormElements(".intCompltdDiv");
            	this.blockFormElements(".noReasonDiv");
                //Added for FMS 7894 January 2016 CR's
            	this.blockFormElements(".strcutDiv");
            	//this.blockFormElements(".doNotConsiderDiv");
            }
            if (CONSTANTS.interviewResultsObj.selectedCandidate.intvwStatus === "UNFAVORABLE") {
                this.blockFormElements(".offerDet");
                CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage();
//                alert("Stopped Hide 3");
                this.blockFormElements(".drugTestProf");
                this.blockFormElements(".driverLicDet");
            } else {
                if ($("#noInterviewReason option:selected").index() > 0) {
                    $(".intDet").unblock();
                    this.blockFormElements(".offerDet");
                    CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage();
//                    alert("Stopped Hide 4");
                    this.blockFormElements(".drugTestProf");
                    this.blockFormElements(".driverLicDet");
                } else if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "Y") {
                	this.blockFormElements(".offerDet");
                } else {
                    $(".offerDet").unblock();
                    CONSTANTS.interviewResultsObj.enable_OD_Fields();
                    CONSTANTS.interviewResultsObj.enable_OD_DeclineReason();
                }
            }
        } else {
            $(".intDet").unblock();
            $(".intCompltdDiv").unblock();
            this.blockFormElements(".noReasonDiv");
            this.blockFormElements(".nameDateRow");
            this.blockFormElements(".nameRowDiv");
            this.blockFormElements(".dateRowDiv");
            //this.blockFormElements(".strcutRow");
            this.blockFormElements(".categoryNameRow");
            this.blockFormElements("#qnsSections");
            this.blockFormElements(".overallStatRow");
        }
    };
    /*
     * This method will enable\ disable decline reason based
     * on offer result selected
     *
     * @param N/A
     * @return N/A
     */
    this.enable_OD_DeclineReason = function() {
        if ($("#offerResultCbo option:selected").text() === "ACCEPT" || $("#offerResultCbo option:selected").index() === 0) {
            $('#declineReasonCbo option:eq(0)').prop('selected', true);
            $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
            $("#declineReasonCbo").attr("disabled", "disabled");
            $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
        } else {
            $("#declineReasonCbo").removeAttr("disabled");
            $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
        }
    };
    /*
     * This method will load interview results of the selected candidate.
     * Incase of error in retrieving the interview results of the
     * selected candidate, an alert is thrown with the error message.
     *
     * @param response
     * @return N/A
     */
    this.loadInterviewResultsCandidateIntvwDtls = function(json) {
        $.unblockUI();

        CONSTANTS.CandidateIntvwDtls = {};
        CONSTANTS.CandidateIntvwQuestDtlsList = [];
        CONSTANTS.CandidateIntvwSigQuest = {}; // ApplIntervwQuestVO
        CONSTANTS.CandidateAvailMatchQuestion = {};
        CONSTANTS.InterviewDtls = {}; // InterviewerInfoVO
        CONSTANTS.intvwStatus = "";
        CONSTANTS.CandidateDrugTestInfo = {};
        CONSTANTS.BackgroundCheckDtls = {}; // BackgroundChkVO
        CONSTANTS.DriverLicenseDtls = {}; // DriverLicenseVO
        CONSTANTS.DriverLicenseDtls.driverLicenseNumber= "";
        CONSTANTS.DriverLicenseDtls.driverLicenseStateCode= "";

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status")) {
            // check whether status is Success or Error
            if (json.Response.status === CONSTANTS.STATUS_SUCCESS) {
                // Load Applicant Questions and SIG
                if (json.Response.hasOwnProperty("InterviewQuestionsList") && json.Response.InterviewQuestionsList !== {}) {
                    CONSTANTS.interviewResultsObj.interviewQuestionsList(json);
                }

                // Interview/Interviewer Info
                if (json.Response.hasOwnProperty("InterviewerInfo")) {
                    CONSTANTS.InterviewDtls.interviewerUserId = json.Response.InterviewerInfo.interviewerUserId;
                    CONSTANTS.InterviewDtls.interviewDate = json.Response.InterviewerInfo.interviewDate;
                    $("#interviewerName").val(CONSTANTS.InterviewDtls.interviewerUserId);
                    $("#interviewDt").val(CONSTANTS.InterviewDtls.interviewDate);
                }

                // Candidate Info
                if (json.Response.hasOwnProperty("CandidateInformation")) {
                    CONSTANTS.interviewResultsObj.candidateInformation(json);
                }
                
                // Candidate Drug Test Panel Info
                if (json.Response.hasOwnProperty("CandidateDTInformation")) {
                	CONSTANTS.interviewResultsObj.CandidateDTInformation(json);
                }

                // Background Information
                if (json.Response.hasOwnProperty("BackgroundCheckInfo")) {

                    CONSTANTS.BackgroundCheckDtls.hasSignedConsent = json.Response.BackgroundCheckInfo.hasSignedConsent;
                    CONSTANTS.BackgroundCheckDtls.isDrivingPos = json.Response.BackgroundCheckInfo.isDrivingPos;
                    CONSTANTS.BackgroundCheckDtls.isAlreadyInDrivingPos = json.Response.BackgroundCheckInfo.isAlreadyInDrivingPos;
                    CONSTANTS.BackgroundCheckDtls.needBackgroundCheck = json.Response.BackgroundCheckInfo.needBackgroundCheck;
                    CONSTANTS.BackgroundCheckDtls.backgroundComponentsNeeded = json.Response.BackgroundCheckInfo.backgroundComponentsNeeded;
                    CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded = json.Response.BackgroundCheckInfo.backgroundPackageNeeded;
                    CONSTANTS.BackgroundCheckDtls.humanResourcesStoreTypeCode = json.Response.BackgroundCheckInfo.humanResourcesStoreTypeCode;
                    CONSTANTS.BackgroundCheckDtls.consentStatCd = json.Response.BackgroundCheckInfo.consentStatCd;
                    CONSTANTS.BackgroundCheckDtls.consentCompleteTs = json.Response.BackgroundCheckInfo.consentCompleteTs;
                    CONSTANTS.BackgroundCheckDtls.consentSigDt = json.Response.BackgroundCheckInfo.consentSigDt;
                    CONSTANTS.BackgroundCheckDtls.aplcntLKey = json.Response.BackgroundCheckInfo.aplcntLKey;
                    CONSTANTS.BackgroundCheckDtls.reinitiateBgcConsent = json.Response.BackgroundCheckInfo.reinitiateBgcConsent; 
                    
                    // Set Background Consent
                    CONSTANTS.interviewResultsObj.setBackgroundConsent();
                    CONSTANTS.interviewResultsObj.setBgcFAPAPanel();

                }

                // Set the CPD or Rehire messages if needed
                CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
                //Unlock CPD Panel if Offer Result is Accept.
                CONSTANTS.interviewResultsObj.setCPDPanel();
                CONSTANTS.interviewResultsObj.setMinorConsentPanel();
                CONSTANTS.interviewResultsObj.setDrugTestPanel();
                CONSTANTS.interviewResultsObj.setBgcFAPAPanel();
                //Collapse Interview Details
        		$(".interviewDetailsDiv").css("display", "none");
        		$("#interviewDetailsToggle").val("Show Interview Details");
        		
        		if (CONSTANTS.interviewResultsObj.selectedCandidate.submitInterviewResultsDT !== "") {
        			$("#interviewDetailsMessage").hide();
        		} else {
        			/*$("#interviewDetailsMessage").show();*/
        		}
            } else if (json.Response.hasOwnProperty("error")) {
                CONSTANTS.interviewResultsObj.popup.alert(json.Response.error.errorMsg);
            }
        } else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.interviewResultsObj.popup.alert(json.Response.error.errorMsg);
        }
    };

    /*
     * This method is used to set the Questions List
     *
     * @param result list
     * @return N/A
     */
    this.setQuestionsList = function(resultList) {
        var questionDtls = {};
        for ( var x = 0; x < resultList.length; x++) {
            // Pull out SIG Info from results
            if (resultList[x].interviewQuestRsltInd === "**") {
                CONSTANTS.CandidateIntvwSigQuest.emplIntervwQuestId = resultList[x].emplIntervwQuestId.toString().trim();
            } else if (resultList[x].emplIntervwQuestId ==="AVL") {
                CONSTANTS.CandidateAvailMatchQuestion.interviwQuestRsltInd = resultList[x].interviewQuestRsltInd.toString().trim();
            }else{
                // Interview Questions
                questionDtls = {};
                questionDtls.emplIntervwQuestId = resultList[x].emplIntervwQuestId.toString().trim();
                questionDtls.interviewQuestRsltInd = resultList[x].interviewQuestRsltInd.toString().trim();
                questionDtls.createTimestamp = resultList[x].createTimestamp;
                CONSTANTS.CandidateIntvwQuestDtlsList.push(questionDtls);
            }
        }
        // Set Radio Button Values
        if (CONSTANTS.CandidateIntvwQuestDtlsList && CONSTANTS.CandidateIntvwQuestDtlsList.length > 0) {
            CONSTANTS.interviewResultsObj.setInterviewRadioButtons();
        }

        // Set SIG Number based on Interview Questions
        if (CONSTANTS.StructuredInterviewGuideList && CONSTANTS.StructuredInterviewGuideList.length > 0 && CONSTANTS.CandidateIntvwSigQuest.emplIntervwQuestId) {
            for ( var i = 0; i < CONSTANTS.StructuredInterviewGuideList.length; i++) {
                if (CONSTANTS.CandidateIntvwSigQuest.emplIntervwQuestId === CONSTANTS.StructuredInterviewGuideList[i].data) {
                    CONSTANTS.interviewResultsObj.setSigGuide(i + 1);
                }
            }
        }
    };

    /*
     * This method is used to load the Interview Questions List
     *
     * @param response
     * @return N/A
     */
    this.interviewQuestionsList = function(json) {
        var resultList = [];
        // check whether InterviewQuestionsList is an array
        // collection
        if (Array.isArray(json.Response.InterviewQuestionsList.ApplIntervwQuestions)) {
            resultList = json.Response.InterviewQuestionsList.ApplIntervwQuestions;
        }
        // InterviewQuestionsList is not an array collection
        else {
            if (json.Response.InterviewQuestionsList.hasOwnProperty("ApplIntervwQuestions")) {
                resultList = [ json.Response.InterviewQuestionsList.ApplIntervwQuestions ];
            }
        }
        // check whether the result list is having any value
        // object or not
        if (resultList && resultList.length > 0) {
            CONSTANTS.interviewResultsObj.setQuestionsList(resultList);
        }
    };

    /*
     * This method is used to set the candidate Information
     *
     * @param response
     * @return N/A
     */
    this.candidateInformation = function(json) {
        // Offer Date
        var offerMadeDate = "";
        if (json.Response.CandidateInformation.candidateOfferMadeDate) {
            offerMadeDate = json.Response.CandidateInformation.candidateOfferMadeDate.month + "/" + json.Response.CandidateInformation.candidateOfferMadeDate.day + "/" + json.Response.CandidateInformation.candidateOfferMadeDate.year;
        }
        // Inactive Date
        var inactiveDate = "";
        if (json.Response.CandidateInformation.inactiveDate) {
            inactiveDate = json.Response.CandidateInformation.inactiveDate.month + "/" + json.Response.CandidateInformation.inactiveDate.day + "/" + json.Response.CandidateInformation.inactiveDate.year;
        }
        // Active Date
        var activeDate = "";
        if (json.Response.CandidateInformation.inactiveDate) {
            activeDate = json.Response.CandidateInformation.activeDate.month + "/" + json.Response.CandidateInformation.activeDate.day + "/" + json.Response.CandidateInformation.activeDate.year;
        }
        // Letter Of Intent Date
        var intentDate = "";
        if (json.Response.CandidateInformation.letterOfIntentDate) {
            intentDate = json.Response.CandidateInformation.letterOfIntentDate.month + "/" + json.Response.CandidateInformation.letterOfIntentDate.day + "/" + json.Response.CandidateInformation.letterOfIntentDate.year;
        }
        // AdverseActionDate
        var adverseActionDate = "";
        if (json.Response.CandidateInformation.adverseActionDate) {
            adverseActionDate = json.Response.CandidateInformation.adverseActionDate.month + "/" + json.Response.CandidateInformation.adverseActionDate.day + "/" + json.Response.CandidateInformation.adverseActionDate.year;
        }

        // submitInterviewResultsDT
        var submitInterviewResultsDT = "";
        if (json.Response.CandidateInformation.submitInterviewResultsDT) {
        	submitInterviewResultsDT = json.Response.CandidateInformation.submitInterviewResultsDT.month + "/" + json.Response.CandidateInformation.submitInterviewResultsDT.day + "/" + json.Response.CandidateInformation.submitInterviewResultsDT.year;
        }

        // submitOfferResultsDT
        var submitOfferResultsDT = "";
        if (json.Response.CandidateInformation.submitOfferResultsDT) {
        	submitOfferResultsDT = json.Response.CandidateInformation.submitOfferResultsDT.month + "/" + json.Response.CandidateInformation.submitOfferResultsDT.day + "/" + json.Response.CandidateInformation.submitOfferResultsDT.year;
        }
        
        // isCandMinorFlag
        if (json.Response.CandidateInformation.hasOwnProperty('IsCandMinorFlag')) {
        	isCandMinorFlag = json.Response.CandidateInformation.IsCandMinorFlag;
        }
        
        // parentalConsentFormsComplete
        if (json.Response.CandidateInformation.hasOwnProperty('parentalConsentFormsComplete')) {
        	allParentalConsentFormsComplete = json.Response.CandidateInformation.parentalConsentFormsComplete;
        } 
        
        CONSTANTS.CandidateIntvwDtls.strNbr = json.Response.CandidateInformation.humanResourcesSystemStoreNumber;
        CONSTANTS.CandidateIntvwDtls.reqNbr = json.Response.CandidateInformation.employmentRequisitionNumber;
        CONSTANTS.CandidateIntvwDtls.candId = json.Response.CandidateInformation.employmentPositionCandidateId;
        CONSTANTS.CandidateIntvwDtls.createUserId = json.Response.CandidateInformation.createUserId;
        CONSTANTS.CandidateIntvwDtls.intvwStat = json.Response.CandidateInformation.interviewStatusIndicator;
        CONSTANTS.CandidateIntvwDtls.refChkInd = json.Response.CandidateInformation.referenceCheckResultIndicator;
        CONSTANTS.CandidateIntvwDtls.drugTestRsltCd = json.Response.CandidateInformation.drugTestResultCode;
        CONSTANTS.CandidateIntvwDtls.candOfferMade = json.Response.CandidateInformation.candidateOfferMadeFlag;
        CONSTANTS.CandidateIntvwDtls.candOfferStat = json.Response.CandidateInformation.candidateOfferStatusIndicator;
        CONSTANTS.CandidateIntvwDtls.inactiveDate = inactiveDate;
        CONSTANTS.CandidateIntvwDtls.hrActSeqNum = json.Response.CandidateInformation.humanResourcesActionSequenceNumber;
        CONSTANTS.CandidateIntvwDtls.empStatActCd = json.Response.CandidateInformation.employeeStatusActionCode;
        CONSTANTS.CandidateIntvwDtls.activeDate = activeDate;
        CONSTANTS.CandidateIntvwDtls.drugTestId = json.Response.CandidateInformation.drugTestId;
        CONSTANTS.CandidateIntvwDtls.candOfferDate = offerMadeDate;
        CONSTANTS.CandidateIntvwDtls.offerDeclineReasonCd = json.Response.CandidateInformation.offerDeclineReasonCode;
        CONSTANTS.CandidateIntvwDtls.letterOfIntentDate = intentDate;
        CONSTANTS.CandidateIntvwDtls.adverseActDate = adverseActionDate;
        CONSTANTS.CandidateIntvwDtls.submitInterviewResultsDT = submitInterviewResultsDT;
        CONSTANTS.CandidateIntvwDtls.submitOfferResultsDT = submitOfferResultsDT;
        CONSTANTS.CandidateIntvwDtls.isCandMinorFlag = isCandMinorFlag;
        CONSTANTS.CandidateIntvwDtls.allParentalConsentFormsComplete = allParentalConsentFormsComplete;
        CONSTANTS.CandidateIntvwDtls.activeFlg = json.Response.CandidateInformation.activeFlag;
        CONSTANTS.CandidateIntvwDtls.applicantType = null;
        CONSTANTS.CandidateIntvwDtls.cpdFormComplete = json.Response.CandidateInformation.cpdFormComplete;
        CONSTANTS.CandidateIntvwDtls.rehireEligible = json.Response.CandidateInformation.rehireEligible;
        
        //Need to set the CPD Form Status....
        CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
        
        // These will only load if candidate is not rehire eligible
        if (json.Response.CandidateInformation.hasOwnProperty("termLoc")) {
            CONSTANTS.CandidateIntvwDtls.termLoc = json.Response.CandidateInformation.termLoc;
        }
        if (json.Response.CandidateInformation.hasOwnProperty("termRsn")) {
            CONSTANTS.CandidateIntvwDtls.termRsn = json.Response.CandidateInformation.termRsn;
        }
        if (json.Response.CandidateInformation.hasOwnProperty("termDt")) {
            CONSTANTS.CandidateIntvwDtls.termDt = json.Response.CandidateInformation.termDt;
        }

    };

    /*
     * This method will help to set interview details
     *
     * @param N/A
     * @return N/A
     */
    this.setInterviewRadioButtons = function() {
        // Set Radio Button Values
        if (CONSTANTS.CandidateIntvwQuestDtlsList && CONSTANTS.CandidateIntvwQuestDtlsList.length > 0) {
            var obj1 = "";
            var lblObj = "";

            for ( var x = 0; x < CONSTANTS.CandidateIntvwQuestDtlsList.length; x++) {
                obj1 = "ID_" + CONSTANTS.CandidateIntvwQuestDtlsList[x].emplIntervwQuestId + "Qus" + CONSTANTS.CandidateIntvwQuestDtlsList[x].interviewQuestRsltInd;
                lblObj = "ID_" + CONSTANTS.CandidateIntvwQuestDtlsList[x].emplIntervwQuestId + "QusLbl";
                // this[obj].selection = this[obj1]
                $('#' + obj1).prop('checked', true);

                if (CONSTANTS.CandidateIntvwQuestDtlsList[x].interviewQuestRsltInd === "HF") {
                    $('#' + lblObj).show();
                    $('#' + lblObj).text("Highly Favorable");
                    $('#' + lblObj).css("color", "#0a742b");
                } else if (CONSTANTS.CandidateIntvwQuestDtlsList[x].interviewQuestRsltInd === "F") {
                    $('#' + lblObj).show();
                    $('#' + lblObj).text("Favorable");
                    $('#' + lblObj).css("color", "#79e354");
                } else if (CONSTANTS.CandidateIntvwQuestDtlsList[x].interviewQuestRsltInd === "A") {
                    $('#' + lblObj).show();
                    $('#' + lblObj).text("Acceptable");
                    $('#' + lblObj).css("color", "#f5e926");
                } else if (CONSTANTS.CandidateIntvwQuestDtlsList[x].interviewQuestRsltInd === "U") {
                    $('#' + lblObj).show();
                    $('#' + lblObj).text("Unfavorable");
                    $('#' + lblObj).css("color", "#f1964c");
                } else if (CONSTANTS.CandidateIntvwQuestDtlsList[x].interviewQuestRsltInd === "HU") {
                    $('#' + lblObj).show();
                    $('#' + lblObj).text("Highly Unfavorable");
                    $('#' + lblObj).css("color", "#e71a0f");
                } else {
                    $('#' + lblObj).hide();
                    $('#' + lblObj).text("");
                }
            }
            // Set overall Interview Results
            CONSTANTS.interviewResultsObj.showOverAllIntvwResults();
        }
    };
    /*
     * This method will help to show overall interview results
     * of the selected candidate. The overall result of a candidate
     * can be anyone of the following:
     * 1. Favorable
     * 2. Acceptable
     * 3. Unfavorable
     * A color code is also specified for each overall status
     *
     * @param N/A
     * @return N/A
     */
    this.showOverAllIntvwResults = function() {
        var overAllScore = CONSTANTS.interviewResultsObj.scoreInterviewResults();
        if (overAllScore === "invalid") {
            $("#ID_OverallResultsLbl").text("");
			$("#ID_ShowUnfavorable").hide();								
            $("#interviewDetailsMessage").show();
            CONSTANTS.intvwStatus = "";
        } else {
            $("#ID_OverallResultsLbl").text(overAllScore);
            if (overAllScore === "Favorable") {
                $("#ID_OverallResultsLbl").css("color", "#0a742b");
                $("#ID_ShowUnfavorable").hide();
                $("#interviewDetailsMessage").show();
                CONSTANTS.intvwStatus = "F";
            } else if (overAllScore === "Acceptable") {
                $("#ID_OverallResultsLbl").css("color", "#f5e926");
                $("#ID_ShowUnfavorable").hide();
                $("#interviewDetailsMessage").show();
                CONSTANTS.intvwStatus = "A";
            } else if (overAllScore === "Unfavorable") {
                $("#ID_OverallResultsLbl").css("color", "#e71a0f");
                $("#ID_ShowUnfavorable").show();
                $("#interviewDetailsMessage").hide();
                CONSTANTS.intvwStatus = "U";
            }
        }
    };
    /*
     * This method will calculate Inteview results based on
     * the overall status of the candidate
     *
     * @param N/A
     * @return score text
     */
    this.scoreInterviewResults = function() {
        var i = 0;
        var score = 0;
        var scoreText = "";
        var interviewTotal = 0;
        var numQuestionsAnswered = 0;

        for (i = 1; i < 11; i++) {
            var obj = "";
            obj = i + "Qus";
            interviewTotal = interviewTotal + parseInt($("input[name='" + obj + "']:checked").val());
            if (parseInt($("input[name='" + obj + "']:checked").val()) !== 0) {
                numQuestionsAnswered = numQuestionsAnswered + 1;
            }
        }

        if (numQuestionsAnswered > 0) {
            score = interviewTotal / numQuestionsAnswered;
            if (score >= 4) {
                scoreText = "Favorable";
            } else if (score < 3) {
                scoreText = "Unfavorable";
            } else {
                scoreText = "Acceptable";
            }
        } else {
            scoreText = "invalid";
        }
        return scoreText;
    };
    /*
     * This method helps to set Structured Interview Guide
     *
     * @param N/A
     * @return N/A
     */
    this.setSigGuide = function(i) {
        $('#sigNum option:eq(' + i + ')').prop('selected', true);
        $("#sigNum").data("selectBox-selectBoxIt").refresh();
    };
    /*
     * Set background area when offerMade is Yes
     *
     * @param N/A
     * @return N/A
     */
    this.setBackgroundConsent = function() {
        // Only set Background area when Offer Made is Yes
        if ($("#offerMadeCbo option:selected").index() === 1) {
            if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator === "AP") {
                CONSTANTS.interviewResultsObj.appAssindicatorAP();
            } else if (CONSTANTS.BackgroundCheckDtls.isDrivingPos && !CONSTANTS.BackgroundCheckDtls.isAlreadyInDrivingPos) {
                CONSTANTS.interviewResultsObj.hasSignedConsent();
            } else if (CONSTANTS.BackgroundCheckDtls.isDrivingPos && CONSTANTS.BackgroundCheckDtls.isAlreadyInDrivingPos) {
                CONSTANTS.interviewResultsObj.hasSignedConsentAndNeedBGC();
                //Added for Asset Protection when an associate is already in a driving position.  They still need the AP Component
                if (CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded.toString() === "24") {
                	$("input[name='isSignCmpltd'][value='Y']").prop('checked', true);
                }
            } else if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator !== "AP") {
                $("input[name='isSignCmpltd']").prop('checked', false);
                $("input[name='isSignCmpltd']").attr("disabled", "disabled");
                this.blockFormElements(".backgrndRadio");
                this.blockFormElements(".bgcFAPA");
                this.blockFormElements(".bgcOrderDet");
                this.blockFormElements(".drugTestProf");
                $("#backgroundCheckText").text("");

                $("#bgcFAPAMsg").text("");
            }
        }
    };

    /*
     * Set CPD Panel when offerResult is Accept
     *
     * @param N/A
     * @return N/A
     */
    this.setCPDPanel = function() {
        // Only set CPD Panel when Offer Result is Accept
    	if ($("#offerResultCbo option:selected").text() && $("#offerResultCbo option:selected").val() === "AC"
    		&& $("#offerDt").val() !== "" && ($("#offerDt").val().trim().length > 0)
    		&& $("#offerMadeCbo option:selected").text() && $("#offerMadeCbo option:selected").val() === "Y"    			
    		&& CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator === "AP") {
    		$(".candidatePersonalData").unblock();
    		this.setMinorConsentPanel();
    		this.setDrugTestPanel();
    	} else {
    		this.blockFormElements(".candidatePersonalData");
    		this.blockFormElements(".drugTestProf");
    		if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator !== "AP") {
    			$("#cpdFormStatus").text("Personal Data not required for Associates.");
    			$("#cpdFormStatus").css("color", "green");
    		}
    	}
    };
    
    /*
     * Set Minor Consent Panel when CPD Panel is Complete and Candidate is a minor
     *
     * @param N/A
     * @return N/A
     */
    this.setMinorConsentPanel = function() {
    	if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator === "AP"
			&& $("#cpdFormStatus").text() === "Complete"
			&& isCandMinorFlag 
			&& showMinorConsentPanel
			&& $("#offerResultCbo option:selected").text()
			&& $("#offerResultCbo option:selected").val() === "AC"
	    	&& $("#offerDt").val() !== "" && ($("#offerDt").val().trim().length > 0)
	    	&& $("#offerMadeCbo option:selected").text() && $("#offerMadeCbo option:selected").val() === "Y"    			
    	) {
	    	// $(".drugTestProf").unblock();
    		$(".minorConsent").css("display", "inherit");
    		$(".minorConsent").unblock();
    		if (!CONSTANTS.CandidateIntvwDtls.rehireEligible) {
	    		this.blockFormElements(".minorConsent");
			} else
				if ((!isInitCMFlag && CONSTANTS.CandidateIntvwDtls.isCandMinorFlag && CONSTANTS.CandidateIntvwDtls.allParentalConsentFormsComplete == "complete")
	        			||(isCandMinorFlag && allParentalConsentFormsComplete == "complete")){
	        		$('input:radio[name="isMinorConsentReady"]').filter('[value="Y"]').attr('checked', true);
	    		    	$("input[name='isMinorConsentReady']").attr("disabled", "disabled");
	    		    	$("#minorConsentButton").css("display", "none");
	    		    	$("#minorConsentFormsStatus").text("Complete");
	    	        	$("#minorConsentFormsStatus").css("color", "green");
	    	        	$("#minorConsentFormsStatusLabel").css("display", "inherit");
	    				$("#minorConsentFormsStatus").css("display", "inherit");
	    				this.blockFormElements(".minorConsentRadio");
	    				$(".drugTestProf").unblock(); //mcp blocking
	    				this.setDrugTestPanel();
	        		} else
	        			if (CONSTANTS.CandidateIntvwDtls.isCandMinorFlag && CONSTANTS.CandidateIntvwDtls.allParentalConsentFormsComplete != "notstarted"){
	        				isCandMinorFlag = CONSTANTS.CandidateIntvwDtls.isCandMinorFlag;
	        				allParentalConsentFormsComplete = CONSTANTS.CandidateIntvwDtls.allParentalConsentFormsComplete;
	        			} else if (isCandMinorFlag && CONSTANTS.CandidateIntvwDtls.allParentalConsentFormsComplete == ""){
	        				//do nothing, keep values the same.
	        			} else {
	        				allParentalConsentFormsComplete = "notstarted";
	        			}
    			this.minorConsentBtn();
    		
    	} else {
			$(".minorConsent").css("display", "none");  //Added to remove Minor Consent Panel if DOB changed on CPD form to no longer be a minor.
    		this.blockFormElements(".minorConsent"); // Following code not seen once panel no longer displays
    		if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator !== "AP") {
    			$(".minorConsent").css("display", "none");
    			$("#minorConsentMsgNR").css("display", "inherit");
    			$("#minorConsentMsgNR").text("Minor Consent forms are not required for Associates.");
    			$("#minorConsentMsgNR").css("color", "green");
    		}
    	}
    };
    
    this.minorConsentBtn = function() {
    	if ($('input[name=isMinorConsentReady]:checked').val() == 'Y') {
    		if (allParentalConsentFormsComplete !="complete"){
        		$("#minorConsentButton").css("display", "inherit");
    		}
    		$("#minorConsentMsgNR").css("display", "none");
    		$("#yes_minorConsentRadioBtn").attr("disabled", true);
    		if (allParentalConsentFormsComplete == "notstarted"){
    			$("#minorConsentFormsStatusLabel").css("display", "none");
    			$("#minorConsentFormsStatus").css("display", "none");
    		} else if (allParentalConsentFormsComplete == "incomplete"){
    			$("#minorConsentFormsStatusLabel").css("display", "inherit");
    			$("#minorConsentFormsStatus").text("Incomplete");
    			$("#minorConsentFormsStatus").css("color", "orangered");
    			$("#minorConsentFormsStatus").css("display", "inherit");
    		}
    	} else if ($('input[name=isMinorConsentReady]:checked').val() == 'N') {
    		$("#minorConsentButton").css("display", "none");
    		$("#minorConsentMsgNR").css("display", "inherit");
    		$("#minorConsentMsgNR").text("Do not proceed with the minor consent forms until a parent/guardian of the minor candidate is present.");
        	$("#minorConsentMsgNR").css("color", "red");
    		$("#minorConsentMsg").css("display", "none");
    		$("#minorConsentFormsStatusLabel").css("display", "none");
    		$("#minorConsentFormsStatus").css("display", "none");
    		$("#yes_minorConsentRadioBtn").attr("disabled", false);
    				
    	} else {
    		$("#minorConsentButton").css("display", "none");
			$("#minorConsentMsgNR").css("display", "none");
			$("#minorConsentFormsStatusLabel").css("display", "none");
			$("#minorConsentFormsStatus").css("display", "none");
    	}
    };
    	
    this.launchDCS_PCFModalPortal = function() {
    	isInitCMFlag = true;
    	envVars = checkEnv();
    	dcsuri = envVars.dcsuri;
    	var statusmessageDCS = "";
    	$("#minorConsentFormsStatusLabel").css("display", "inherit");
    	if ($("#minorConsentFormsStatus").text() == "Incomplete" || $("#minorConsentFormsStatus").text() == "" || allParentalConsentFormsComplete == "incomplete" || allParentalConsentFormsComplete == "notstarted") {
    		allParentalConsentFormsComplete = "incomplete";
    		$("#minorConsentFormsStatus").text("Incomplete");
            $("#minorConsentFormsStatus").css("color", "orangered");
        }
		$("#minorConsentFormsStatus").css("display", "inherit");
    	
    	$(window).on('message onmessage', function(eDCS) {
            if (eDCS.originalEvent.origin === dcsuri) {
                statusmessageDCS=eDCS.originalEvent.data;
                if (statusmessageDCS.indexOf("All Forms Complete") > -1) {
                	allParentalConsentFormsComplete = "complete";
                	CONSTANTS.interviewResultsObj.setMinorConsentPanel();
                } else if (statusmessageDCS.indexOf("Parental Consent Closed") > -1) {
                	$('#DCSModal').modal('hide');
                }
            }
    	})
    	document.getElementById("showDCSPortal").src=(dcsuri+"/?wrkflw=1&langCode=en_US&applicantId="+CONSTANTS.CandidateIntvwDtls.candId+"&requistionNumber="+CONSTANTS.CandidateIntvwDtls.reqNbr)+"&mgrLDAP="+CONSTANTS.userProfile.userId;

    	$('#DCSModal').modal('show');
    };
    
    /*
     * This gets the Drug Test Initiation information from the DB if it exists for a Candidate
     */
    
	this.CandidateDTInformation = function(json) {
		// Drug Test Applicant Email Address
		var ApplEmailDTDB = "";
		if (json.Response.CandidateDTInformation.applicantEmailAddress__DT) {
			ApplEmailDTDB = json.Response.CandidateDTInformation.applicantEmailAddress__DT;
		}

		// DT Initiate Date
		var orderInitiatedTsDTDB = "";
		if (json.Response.CandidateDTInformation.createTimestamp__DT) {
			orderInitiatedTsDTDB = json.Response.CandidateDTInformation.createTimestamp__DT+' UTC';
		}

		// DT Order Number
		var orderNbrDTDB = "";
		if (json.Response.CandidateDTInformation.drugTestOrderNumber__DT) {
			orderNbrDTDB = json.Response.CandidateDTInformation.drugTestOrderNumber__DT;
		}

		// Requester Email Address
		var RequesterEmailDTDB = "";
		if (json.Response.CandidateDTInformation.requesterEmailAddress__DT) {
			RequesterEmailDTDB = json.Response.CandidateDTInformation.requesterEmailAddress__DT;
		}

		// DT Request completed Time Stamp
		var orderCompletedTsDTDB = "";
		if (json.Response.CandidateDTInformation.requestCompTimestamp__DT) {
			orderCompletedTsDTDB = json.Response.CandidateDTInformation.requestCompTimestamp__DT;
		}
		
		isInitDTFlag = false;
		CONSTANTS.CandidateDrugTestInfo.ApplEmailDTDB = ApplEmailDTDB;
		CONSTANTS.CandidateDrugTestInfo.orderInitiatedTsDTDB = orderInitiatedTsDTDB;
		CONSTANTS.CandidateDrugTestInfo.orderNbrDTDB = orderNbrDTDB;
		CONSTANTS.CandidateDrugTestInfo.RequesterEmailDTDB = RequesterEmailDTDB;
		CONSTANTS.CandidateDrugTestInfo.orderCompletedTsDTDB = orderCompletedTsDTDB;
		CONSTANTS.CandidateDrugTestInfo.isInitDTFlag = isInitDTFlag;
	};
    
    /*
	 * Set Drug Test Panel when CPD Form Status is Complete
	 * 
	 * @param N/A @return N/A
	 */
	this.setDrugTestPanel = function() {
		if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator === "AP"
			&& $("#cpdFormStatus").text() === "Complete"
			&& $("input[name='isIntCmpltd']:checked").val() === CONSTANTS.YES
			&& ($("#offerDt").val() !== "" && $("#offerDt").val().trim().length > 0)
			&& $("#offerResultCbo option:selected").val() === "AC") {
	    	// $(".drugTestProf").unblock();
	    	if ( (!CONSTANTS.CandidateIntvwDtls.rehireEligible) ||
	    			($("#minorConsentFormsStatus").text() != "Complete"
	    			 && isCandMinorFlag 
	    			 && showMinorConsentPanel) ){ //mcp blocking
	    		this.blockFormElements(".drugTestProf");
	    		this.blockFormElements(".bgcFAPA");
	    		this.blockFormElements(".bgcOrderDet");
			} else {
				// Unblock Background Check when CPD Form Status is Complete and the showDrugTestPanel for pilot flag is true.
			   
				if (!showDTPanel) {
			    	$(".bgcOrderDet").unblock();
			    	$(".bgcFAPA").unblock();
	    		}
				
			    $(".drugTestProf").unblock();
//			    $(".bgcOrderDet").unblock(); //bugfix5
			    if (CONSTANTS.CandidateDrugTestInfo.orderNbrDTDB != undefined && CONSTANTS.CandidateDrugTestInfo.orderNbrDTDB != null && CONSTANTS.CandidateDrugTestInfo.orderNbrDTDB !== "") {
			    	if($("#orderNbrDT").val()==""){
				    	$("#orderNbrDT").val(CONSTANTS.CandidateDrugTestInfo.orderNbrDTDB);
			    	}
			    	if($("#ApplEmailDT").val()==""){
			    		$("#ApplEmailDT").val(CONSTANTS.CandidateDrugTestInfo.ApplEmailDTDB);
			    	}
			    	if($("#RequesterEmailDT").val()==""){
			    		$("#RequesterEmailDT").val(CONSTANTS.CandidateDrugTestInfo.RequesterEmailDTDB);
			    	}
			    	if (isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/)) ){
			    		var ISODateStrDTI=((CONSTANTS.CandidateDrugTestInfo.orderInitiatedTsDTDB).substr(0, 10) + 'T' + (CONSTANTS.CandidateDrugTestInfo.orderInitiatedTsDTDB).substr(11).replace(' UTC','Z'));	
			    		var ISODateStrDTC=((CONSTANTS.CandidateDrugTestInfo.orderCompletedTsDTDB).substr(0, 10) + 'T' + (CONSTANTS.CandidateDrugTestInfo.orderCompletedTsDTDB).substr(11).replace(' UTC','Z'));
			    	} else {
			    		var ISODateStrDTI=(CONSTANTS.CandidateDrugTestInfo.orderInitiatedTsDTDB);
			    		var ISODateStrDTC=(CONSTANTS.CandidateDrugTestInfo.orderCompletedTsDTDB);
			    	}
				
			    	var dateDTI = new Date(ISODateStrDTI);
		    		dateDTI = (dateDTI.toLocaleDateString() + " " + dateDTI.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) );			    	
		    		if($("#orderInitiatedTsDT").val()==""){
		    			$("#orderInitiatedTsDT").val(dateDTI);
		    		}
		    		
			    	if (CONSTANTS.CandidateDrugTestInfo.orderCompletedTsDTDB == "") {
			    		if($("#orderCompletedTsDT").val()==""){
			    			$("#orderCompletedTsDT").val("Not completed")
			    			$("#orderCompletedTsDT").css("color", "orangered");
			    		}
			    	} else {
			    		var dateDTC = new Date(ISODateStrDTC);
			    		dateDTC = (dateDTC.toLocaleDateString() + " " + dateDTC.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) );
			    		if($("#orderCompletedTsDT").val()==""){
			    			$("#orderCompletedTsDT").val(dateDTC);
			    			$("#orderCompletedTsDT").css("color", "green");
			    		}
			    	}
			    	$('input:radio[name="isDrugTestReady"]').filter('[value="Y"]').attr('checked', true);
			    	this.drugtestBtn();
			    	getDTOrderNbr(dtestType);
			    }
			}
	    } else {
	    	this.blockFormElements(".drugTestProf");
	    	if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator !== "AP") {
	    		$("#drugTestMsgNR").css("display", "inherit");
	    		$("#drugTestMsgNR").text("Drug Test Initiation not required for Associates.");
	    		$("#drugTestMsgNR").css("color", "green");
	    	}
	    }
	};

    this.drugtestBtn = function() {
    	if ($('input[name=isDrugTestReady]:checked').val() == 'Y') {
    				$("#drugTestInitDT").css("display", "inherit");
    				 $("#drugTestAccessTProfile").css("display", "none");
    				$("#drugTestButtons").css("display", "inherit");
    				 $("#drugTestMsgNR").css("display", "none");
    				 $("#drugTestMsg").css("display", "none");
    				 $("#yes_drugTestRadioBtn").attr("disabled", true);

    	} else if ($('input[name=isDrugTestReady]:checked').val() == 'N') {
/*    				$("#drugTestButtons").css("display", "none");*/
    				/*$("#drugTestMsgNR").css("display", "inherit");
    				$("#drugTestMsgNR").text("If the candidate refuses to take the drug test, they will not be able to move forward in the hiring process.");
        			$("#drugTestMsgNR").css("color", "red");*/
/*    				$("#drugTestMsg").css("display", "none");
    				$("#drugTestConvMsg").css("display", "none");*/
//    				this.blockFormElements(".bgcOrderDet");
    				DTRefusalWarning();
    				
    	    } else {
    	    	$("#drugTestButtons").css("display", "none");
				$("#drugTestMsgNR").css("display", "none");
				$("#drugTestMsg").css("display", "none");
				$("#drugTestConvMsg").css("display", "none");
				this.blockFormElements(".bgcFAPA");
				this.blockFormElements(".bgcOrderDet");
    		}
    	};

    /*
     * This method turns the flag on if the initiate Drug Test Chain of Custody button has been clicked on.
     * It allows the correct sequence to occur to read from Database or create a new request in getting Drug Test Initiated data.
     */
	this.initDTCC = function() {
		$("#drugTestInitDT").attr("disabled", "disabled");
		isInitDTFlag = true;
//		if ($('#drugTestMsg').is(':visible')) {
		if ($("#orderNbrDT").val()!="") {
			DTReInitWarning();
		} else {
			if (dtestType === "ORAL"){
//				faportal = window.open("","FAPortlet");
//				faportal.document.write("<center><p>First Advantage Portlet is now loading...</p><br><img src='assets/images/loadingAnimation.gif'></br></center>");
				if (typeof faauth === 'undefined' || faauth === false){
//					$("#drugTestInitDT").attr("disabled", "disabled");
					$.blockUI({
		    			message : $('#initiating')
		    		});
					envVars = checkEnv();
			    	fapl = envVars.fapl;
			    	fapAuthLp = envVars.fapAuthLp;
//			    	faportal = window.open(fapl,"auth","Height=1px,Width=1px,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no,top=100000000");
			    	faportal = window.open(fapAuthLp,"auth","Height=1px,Width=1px,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no,top=100000000");
			    	
//				    faportal = window.open("https://uat.collection.fadv.com/?idp=https%3A%2F%2Fdevsaml.homedepot.com","","Height=1px,Width=1px,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no,top=100000000");
//				    faportal = window.open("https://uat.collection.fadv.com/SecureLanding/?idp=https%3A%2F%2Fdevsaml.homedepot.com","auth","Height=1px,Width=1px,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no,top=100000000,left=100000000,visible=none");

			    	// Polling to check when window is closed
				    var interval = setInterval(function() {
				    	if (faportal.closed){
				            clearInterval(interval);
						    faauth = true;
						    $("#drugTestInitDT").removeAttr("disabled");
					    	getDTOrderNbr(dtestType);
				        }    
				    }, 100);
			    	
/*			    	setTimeout(function() {
				    	faportal.close();
				    	faauth = true;
				    	$("#drugTestInitDT").removeAttr("disabled");
				    	getDTOrderNbr(dtestType);
				    }, 1750);
*/
				} else if (faauth = true){
					getDTOrderNbr(dtestType);
				};
			} else { // if (dtestType === "CONV")
				getDTOrderNbr(dtestType);
			}
//			getDTOrderNbr(dtestType);

		}
	};

    /*
	 * Set Background Check FAPA Panel
	 * 
	 * @param N/A @return N/A
	 */
	this.setBgcFAPAPanel = function() {
		if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator === "AP"
			&& $("#cpdFormStatus").text() === "Complete"
			&& $("input[name='isIntCmpltd']:checked").val() === CONSTANTS.YES
			&& ($("#offerDt").val() !== "" && $("#offerDt").val().trim().length > 0)
			&& $("#offerResultCbo option:selected").val() === "AC"
			&& $("#offerResultCbo option:selected").text() 
	    	&& $("#offerMadeCbo option:selected").text()
	    	&& $("#offerMadeCbo option:selected").val() === "Y"
	    	&& ($('#drugTestAccessTProfile').is(':visible') || $('#drugTestMsg').is(':visible')) ) {
			
			$("#bgcFAPAEmailEntry").css("display", "none");
	    	if ( (!CONSTANTS.CandidateIntvwDtls.rehireEligible) ||
	    			($("#minorConsentFormsStatus").text() != "Complete"
	    			 && showMinorConsentPanel
	    			 && isCandMinorFlag) ){ //mcp blocking
	    		this.blockFormElements(".bgcFAPA");
			} else {
				
				// Unblock Background Check when CPD Form Status is Complete and the showDTPanel for pilot flag is true.
				if (!showDTPanel) {
			    	$(".bgcFAPA").unblock();
	    		}
				$(".bgcFAPA").unblock();
				$("#bgcFAPAInitBtn").removeAttr("disabled");

				if (CONSTANTS.BackgroundCheckDtls.hasSignedConsent) { //True when data is showing in the bgc_cnsnt_sig table

					//Old process and has a complete BGC Consent
					if (CONSTANTS.BackgroundCheckDtls.aplcntLKey =="" || typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey === 'undefined'
						&& CONSTANTS.BackgroundCheckDtls.consentStatCd == 3){
			    		$("#bgcFAPAMsgSection").css("display", "inherit");
			    		$("#bgcFAPAMsg").text("Background Check Consent on file");
			    		$("#bgcFAPAMsg").css("color", "green");
			    		$("#bgcFAPAInitBtn").css("display", "none");

					}
					//Has an incomplete BGC Consent; should be able to view/complete the BGC Consent in PA
					if (CONSTANTS.BackgroundCheckDtls.aplcntLKey !="" && typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey !== 'undefined'
						&& CONSTANTS.BackgroundCheckDtls.consentStatCd==0){
						$("#bgcFAPAEmailEntry").css("display", "none");
				    	$("#bgcFAPAInitBtn").css("display", "none");
				    	$("#bgcFAPAViewBtn").css("display", "inherit");
				    	$("#bgcFAPAStatusLabel").css("display", "inherit");
				    	
				    	if ($("#bgcFAPAStatus").val() =="" || $("#bgcFAPAStatus").val()=="Invalid Date Invalid Date" || $("#bgcFAPAStatus").val()=="Not completed") {
				            $("#bgcFAPAStatus").val("Not completed");
				            $("#bgcFAPAStatus").css("color", "orangered");
				            $("#bgcFAPAInitBtn").css("display", "inherit");
				    	}
				    	
						$("#bgcFAPAStatus").css("display", "inherit");
						$("#bgcFAPAStatusSection").css("display", "inherit");
					}
					//Has a complete BGC Consent and consentCompleteTs has a value.
					if (CONSTANTS.BackgroundCheckDtls.consentCompleteTs != undefined
							&& CONSTANTS.BackgroundCheckDtls.consentCompleteTs !=null
							&& CONSTANTS.BackgroundCheckDtls.consentCompleteTs !=""
							&& CONSTANTS.BackgroundCheckDtls.aplcntLKey !="" 
							&& typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey !== 'undefined' 
							&& CONSTANTS.BackgroundCheckDtls.consentStatCd ==3){
						
						if (isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/)) ){
				    		var ISODateStrBgcFAPAC=((CONSTANTS.BackgroundCheckDtls.consentCompleteTs).substr(0, 10) + 'T' + (CONSTANTS.BackgroundCheckDtls.consentCompleteTs).substr(11).replace(' UTC','Z'));
				    	} else {
				    		var ISODateStrBgcFAPAC=(CONSTANTS.BackgroundCheckDtls.consentCompleteTs);
				    	}
						
						var dateBgcFAPA = new Date(ISODateStrBgcFAPAC);
	                    dateBgcFAPA = (dateBgcFAPA.toLocaleDateString() + " " + dateBgcFAPA.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) );
	                    						
						$("#bgcFAPAEmailEntry").css("display", "none");
				    	$("#bgcFAPAInitBtn").css("display", "none");
				    	$("#bgcFAPAViewBtn").css("display", "inherit");
				    	$("#bgcFAPAStatusLabel").css("display", "inherit");
				    	$("#bgcFAPAStatus").css("color", "green");
						$("#bgcFAPAStatus").val(dateBgcFAPA);
				        $("#bgcFAPAStatus").css("display", "inherit");
				        $("#bgcFAPAStatusSection").css("display", "inherit");
					}
				} else {
					//Does not have a BGC Consent; hasSignedConsent is false; should be able to initiate a BGC Consent.
					if (CONSTANTS.BackgroundCheckDtls.consentStatCd==0){
						$("#bgcFAPAEmailEntry").css("display", "none");
				    	$("#bgcFAPAInitBtn").css("display", "inherit");
				    	$("#bgcFAPAViewBtn").css("display", "none");
				    	$("#bgcFAPAStatusLabel").css("display", "none");
				        $("#bgcFAPAStatus").css("display", "none");
				        $("#bgcFAPAStatusSection").css("display", "none");
				        
				        //Display results if a date is present from filling out the PA wizard.
				        if ($("#bgcFAPAStatus").val()!="" && $("#bgcFAPAStatus").val()!="Not completed" ){
					    	$("#bgcFAPAInitBtn").css("display", "none");
					    	$("#bgcFAPAViewBtn").css("display", "inherit");
					    	$("#bgcFAPAStatusLabel").css("display", "inherit");
					    	$("#bgcFAPAStatus").css("color", "green");
					        $("#bgcFAPAStatus").css("display", "inherit");
					        $("#bgcFAPAStatusSection").css("display", "inherit");
				        }
					}
				}
			}
	    } else {
	    	if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator !== "AP" 
	    		&& $("input[name='isIntCmpltd']:checked").val() === CONSTANTS.YES
	    		&& ($("#offerDt").val() !== "" && $("#offerDt").val().trim().length > 0)
	    		&& $("#offerResultCbo option:selected").val() === "AC"
	    		&& $("#offerResultCbo option:selected").text() 
	        	&& $("#offerMadeCbo option:selected").text() 
	        	&& $("#offerMadeCbo option:selected").val() === "Y") {
	    		if (CONSTANTS.BackgroundCheckDtls.hasSignedConsent) { //True when data is showing in the bgc_cnsnt_sig table
	    			
	    			//Old process and has a complete BGC Consent; should not be able to do anything, it is assumed that a BGC has been ordered.
					if ((CONSTANTS.BackgroundCheckDtls.aplcntLKey =="" || typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey === 'undefined')
						&& CONSTANTS.BackgroundCheckDtls.consentStatCd ==3){
			    		$("#bgcFAPAMsgSection").css("display", "inherit");
			    		$("#bgcFAPAMsg").text("No background check required for this internal hire.");
			    		$("#bgcFAPAMsg").css("color", "green");
				    	$("#bgcFAPAInitBtn").css("display", "none");
				    	$("#bgcFAPAViewBtn").css("display", "none");
				    	$("#bgcFAPAStatusSection").css("display", "none");
					}
					//Has a current Consent but does not need any new components. Basically do nothing, just a message.
					if ((CONSTANTS.BackgroundCheckDtls.aplcntLKey !="" || typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey != 'undefined')
						&& CONSTANTS.BackgroundCheckDtls.consentStatCd ==3
						&& CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded ==0){
			    		$("#bgcFAPAMsgSection").css("display", "inherit");
			    		$("#bgcFAPAMsg").text("No background check required for this internal hire.");
			    		$("#bgcFAPAMsg").css("color", "green");
				    	$("#bgcFAPAInitBtn").css("display", "none");
				    	$("#bgcFAPAViewBtn").css("display", "none");
				    	$("#bgcFAPAStatusSection").css("display", "none");
					}
					
					//Has an incomplete BGC Consent and backgroundPackageNeeded; should be able to view/complete the BGC Consent in PA.
					if (CONSTANTS.BackgroundCheckDtls.consentCompleteTs==null && CONSTANTS.BackgroundCheckDtls.aplcntLKey !="" && typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey !== 'undefined'
						&& CONSTANTS.BackgroundCheckDtls.consentStatCd ==0
						&& CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded !=0){
						$(".bgcFAPA").unblock();
				    	$("#bgcFAPAInitBtn").css("display", "none");
				    	$("#bgcFAPAViewBtn").css("display", "inherit");
				    	$("#bgcFAPAStatusLabel").css("display", "inherit");
				    	$("#bgcFAPAEmailEntry").css("display", "none");
				    	
				    	if ($("#bgcFAPAStatus").val() =="" || $("#bgcFAPAStatus").val()=="Invalid Date Invalid Date" || $("#bgcFAPAStatus").val()=="Not completed") {
				            $("#bgcFAPAStatus").val("Not completed");
				            $("#bgcFAPAStatus").css("color", "orangered");
				            $("#bgcFAPAInitBtn").css("display", "inherit");
				            $("#bgcFAPAInitBtn").attr("disabled", "disabled");
				            $("#bgcFAPAEmailEntry").css("display", "inherit");
				    		}
				    	
						$("#bgcFAPAStatus").css("display", "inherit");
						$("#bgcFAPAStatusSection").css("display", "inherit");
					}
					//Has a complete BGC Consent and backgroundPackageNeeded and consentCompleteTs has a value.
					if (CONSTANTS.BackgroundCheckDtls.consentCompleteTs != undefined
							&& CONSTANTS.BackgroundCheckDtls.consentCompleteTs !=null
							&& CONSTANTS.BackgroundCheckDtls.consentCompleteTs !=""
							&& CONSTANTS.BackgroundCheckDtls.aplcntLKey !="" 
							&& typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey !== 'undefined' 
							&& CONSTANTS.BackgroundCheckDtls.consentStatCd ==3
							&& CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded !=0){
						$(".bgcFAPA").unblock();
						if (isAtLeastIE11 = !!(navigator.userAgent.match(/Trident/) && !navigator.userAgent.match(/MSIE/)) ){
				    		var ISODateStrBgcFAPAC=((CONSTANTS.BackgroundCheckDtls.consentCompleteTs).substr(0, 10) + 'T' + (CONSTANTS.BackgroundCheckDtls.consentCompleteTs).substr(11).replace(' UTC','Z'));
				    	} else {
				    		var ISODateStrBgcFAPAC=(CONSTANTS.BackgroundCheckDtls.consentCompleteTs);
				    	}
						
						var dateBgcFAPA = new Date(ISODateStrBgcFAPAC);
	                    dateBgcFAPA = (dateBgcFAPA.toLocaleDateString() + " " + dateBgcFAPA.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) );
	                    						
						$("#bgcFAPAEmailEntry").css("display", "none");
				    	$("#bgcFAPAInitBtn").css("display", "none");
				    	$("#bgcFAPAViewBtn").css("display", "inherit");
				    	$("#bgcFAPAStatusLabel").css("display", "inherit");
				    	$("#bgcFAPAStatus").css("color", "green");
						$("#bgcFAPAStatus").val(dateBgcFAPA);
				        $("#bgcFAPAStatus").css("display", "inherit");
				        $("#bgcFAPAStatusSection").css("display", "inherit");
					}
				} else {
					//Does not have a current Consent but does not need any new components; do nothing, just a message.
					if (CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded==0
							&& CONSTANTS.BackgroundCheckDtls.consentStatCd==0){
						$("#bgcFAPAMsgSection").css("display", "inherit");
			    		$("#bgcFAPAMsg").text("No background check required for this internal hire.");
			    		$("#bgcFAPAMsg").css("color", "green");
					}
					//Does not have a BGC Consent but needs new component; should be able to initiate a BGC Consent and enter email.
					if (CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded !=0
							&& CONSTANTS.BackgroundCheckDtls.consentStatCd==0){
						$(".bgcFAPA").unblock();
						$("#bgcFAPAEmailEntry").css("display", "inherit");
				    	$("#bgcFAPAInitBtn").css("display", "inherit");
						$("#bgcFAPAInitBtn").attr("disabled", "disabled");
						
						//Display results if a date is present from filling out the PA wizard.
				        if ($("#bgcFAPAStatus").val()!="" && $("#bgcFAPAStatus").val()!="Not completed" ){
				        	$("#bgcFAPAEmailEntry").css("display", "none");
					    	$("#bgcFAPAInitBtn").css("display", "none");
					    	$("#bgcFAPAViewBtn").css("display", "inherit");
					    	$("#bgcFAPAStatusLabel").css("display", "inherit");
					    	$("#bgcFAPAStatus").css("color", "green");
					        $("#bgcFAPAStatus").css("display", "inherit");
					        $("#bgcFAPAStatusSection").css("display", "inherit");
				        }
					}
					
		    		if ( (!CONSTANTS.CandidateIntvwDtls.rehireEligible) || (showMinorConsentPanel && isCandMinorFlag && $("#minorConsentFormsStatus").text() != "Complete") ){ //mcp blocking
			    		this.blockFormElements(".bgcFAPA");
			    	}
				}
	    	} else {
	    		this.blockFormElements(".bgcFAPA");
	    	}
	    }
	};
	
	this.validBgcEmail = function() {
		if ($("#emailDataForm").valid() && $("#emailEntry1").val() !="" && $("#emailEntry1").val().ignoreCase == $("#emailEntry2").val().ignoreCase){
			$("#bgcFAPAInitBtn").removeAttr("disabled");
		} else {
			$("#bgcFAPAInitBtn").attr("disabled", "disabled");
		}
	};
	
	// eMail Validation
	$('#emailDataForm')
			.validate(
					{
						ignore : '',
						rules : {
							'emailEntry1' : {
								required : true,
								minlength : 1,
								checkValidEmailAddress : "#emailEntry1"
							},
							'emailEntry2' : {
								required : true,
								minlength : 1,
								checkValidEmailAddress : "#emailEntry2",
								equalToIgnoreCase : "#emailEntry1"
							},
						},

						messages : {
							'emailEntry1' : {
								checkValidEmailAddress : "Must use valid e-mail format",
							},
							'emailEntry2' : {
								checkValidEmailAddress : "Must use valid e-mail format",
								equalToIgnoreCase : "E-mails do not match"
							}
						}
					}); // End
					
//Must Use Valid email format for emailEntry
	$.validator.addMethod("checkValidEmailAddress", function(value, element, param) {
		return $(param).val().match(/^(([^<>()\[\]\.,;:\s@\"]+(\.[^<>()\[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i);
	});
	
	// Clear anything entered for email.
	this.clearEmailDataForm = function() {
		// Clear all entered data
		$("#emailEntry1").val("");
		$("#emailEntry2").val("");
		$('#emailDataForm').trigger("reset");
		// Clear any Validation error messages
		var validator = $("#emailDataForm").validate();
		validator.resetForm();
	}
	
/*	this.bgcNullModal = function() {
		$('#bgcFAPAModal').modal('hide').data('bs.modal',null);
		$("#bgcFAPAModal").on('hidden.bs.modal', function () {
		    $(this).data('bs.modal', null);
		});
	}
*/	
	this.launchBgcFAPAPortal = function(bgcFAPALink) {
		envVars = checkEnv();
    	bgcfapauri = envVars.bgcfapauri;
    	var statusmessageFAPA = "";
    	$("#bgcFAPAEmailEntry").css("display", "none");
    	$("#bgcFAPAInitBtn").css("display", "none");
    	$("#bgcFAPAViewBtn").css("display", "inherit");
    	$("#bgcFAPAStatusLabel").css("display", "inherit");
    	
    	if ($("#bgcFAPAStatus").val() =="" || $("#bgcFAPAStatus").val()=="Invalid Date Invalid Date" || $("#bgcFAPAStatus").val()=="Not completed") {
            $("#bgcFAPAStatus").val("Not completed");
            $("#bgcFAPAStatus").css("color", "orangered");
            $("#bgcFAPAInitBtn").css("display", "inherit");
            if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator !== "AP"){
                $("#bgcFAPAEmailEntry").css("display", "inherit");            	
            }
    	}    

		$("#bgcFAPAStatus").css("display", "inherit");
		$("#bgcFAPAStatusSection").css("display", "inherit");
		
    	$(window).on('message onmessage', function(eFAPA) {
            if (eFAPA.originalEvent.origin === bgcfapauri) {
                statusmessageFAPA=eFAPA.originalEvent.data;
                if (statusmessageFAPA.indexOf("workflow_completed") > -1 ||
                		statusmessageFAPA.indexOf("wotc_optout") > -1) {
                	var dateBgcFAPAUI = new Date();
                    dateBgcFAPAUI = (dateBgcFAPAUI.toLocaleDateString() + " " + dateBgcFAPAUI.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) );
                    $("#bgcFAPAStatus").val(dateBgcFAPAUI)
                    $("#bgcFAPAStatus").css("color", "green");
                    $("#bgcFAPAInitBtn").css("display", "none");
                	CONSTANTS.interviewResultsObj.setBgcFAPAPanel();
//                  $('#bgcFAPAModal').modal('hide');
                	$('#bgcFAPAModal').modal('hide').data('bs.modal',null);  //test
                }
                if (statusmessageFAPA.indexOf("workflow_stopped") > -1 || 
                		statusmessageFAPA.indexOf("signed_out") > -1 || 
                		statusmessageFAPA.indexOf("workflow_exited") > -1) {
//                	$('#bgcFAPAModal').modal('hide');
                	$('#bgcFAPAModal').modal('hide').data('bs.modal',null);  //test
                }
            }
    	})
    	if (typeof isInitBgcFlag === 'undefined') {
    		isInitBgcFlag = false;
    	}

    	if (isInitBgcFlag){
    		bgcFAPAWholeLink=bgcFAPALink;
    		isInitBgcFlag = false;
    	}
    	
    	if (CONSTANTS.BackgroundCheckDtls.aplcntLKey !="" && typeof CONSTANTS.BackgroundCheckDtls.aplcntLKey !== 'undefined' && !CONSTANTS.BackgroundCheckDtls.reinitiateBgcConsent){
    		bgcFAPAWholeLink = (bgcfapauri+"/#/invite/?key="+CONSTANTS.BackgroundCheckDtls.aplcntLKey);
    	}
    
        recreateIframe( bgcFAPAWholeLink );
    	// document.getElementById("showBgcFAPAPortal").src=bgcFAPAWholeLink;
    	$('#bgcFAPAModal').modal('show');
    };
    
	function recreateIframe( url ) {
		// Remove old iFrame
		$('#bgcFAPAContent').html("");
		// Add new iFrame content
		$('#bgcFAPAContent').html('<iframe id="showBgcFAPAPortal" name="showBgcFAPAPortal" class="iframe" src="" seamless height="99%" width="100%"></iframe>');
		// Retrieve it and set a new src
		$('#showBgcFAPAPortal').attr('src', url);
	}
	
    /*
     * This method is used to check if the candidate has signed consent
     *
     * @param N/A
     * @return N/A
     */
    this.hasSignedConsent = function() {
        if (CONSTANTS.BackgroundCheckDtls.hasSignedConsent) {
            $("input[name='isSignCmpltd'][value='Y']").prop('checked', true);
            this.blockFormElements(".backgrndRadio");
//remove1            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            $("input[name='isSignCmpltd']").attr("disabled", "disabled");
            $("#backgroundCheckText").text("There is a signed consent form on file for this candidate.  The background will be ordered.");

            if ($("#offerResultCbo option:selected").text() && $("#offerResultCbo option:selected").val() === "DE") {
                $("#backgroundCheckText").text("");
                $("input[name='isSignCmpltd']").prop('checked', false);
                
                $("#bgcFAPAMsg").text("");
            }
        } else {
            CONSTANTS.interviewResultsObj.needBackgroundCheck();
        }
    };

    /*
     * This method is used to check if the candidate has signed consent or if
     * backbround check needed
     *
     * @param N/A
     * @return N/A
     */
    this.hasSignedConsentAndNeedBGC = function() {
        if (CONSTANTS.BackgroundCheckDtls.hasSignedConsent || !CONSTANTS.BackgroundCheckDtls.needBackgroundCheck) {
            $("input[name='isSignCmpltd']").prop('checked', false);
            this.blockFormElements(".backgrndRadio");
//            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            $("input[name='isSignCmpltd']").attr("disabled", "disabled");
            $("#backgroundCheckText").text("");
            
            $("#bgcFAPAMsg").text("");
        } else {
            if ($("#offerResultCbo option:selected").text() && $("#offerResultCbo option:selected").val() === "AC") {
                $("input[name='isSignCmpltd']").removeAttr("disabled");
                $(".backgrndRadio").unblock();
                /*$(".bgcOrderDet").unblock();*/
            } else {
                this.blockFormElements(".backgrndRadio");
//remove2                this.blockFormElements(".bgcFAPA");
                this.blockFormElements(".bgcOrderDet");
                $("input[name='isSignCmpltd']").attr("disabled", "disabled");
            }
            if ($("input[name='isSignCmpltd']:checked").val() === CONSTANTS.YES) {
                $("#backgroundCheckText").text("Click the Submit button to order the background check. DO NOT FAX SIGNED CONSENT FORMS TO ORDER THE BACKGROUND CHECK.  Please include signed background consent forms with new hire paperwork.");
            } else {
                $("#backgroundCheckText").text("Have the candidate sign the background consent form then select 'Yes' and click submit to order the background check");
            }
        }
    };

    /*
     * This method is used to set the candidate Background Details
     *
     * @param N/A
     * @return N/A
     */
    this.needBackgroundCheck = function() {
        if (CONSTANTS.BackgroundCheckDtls.needBackgroundCheck) {
            if ($("#offerResultCbo option:selected").text() && $("#offerResultCbo option:selected").val() === "AC" && $("#offerDt").val() !== "") {
                $("input[name='isSignCmpltd']").removeAttr("disabled");
                $(".backgrndRadio").unblock();
                $(".bgcOrderDet").unblock();  //For bugfix
                $(".bgcFAPA").unblock();
            } else {
                $("input[name='isSignCmpltd']").prop('checked', false);
                this.blockFormElements(".backgrndRadio");
//remove3                this.blockFormElements(".bgcFAPA");
                this.blockFormElements(".bgcOrderDet");
                $("input[name='isSignCmpltd']").attr("disabled", "disabled");
            }
            if ($("input[name='isSignCmpltd']:checked").val() === CONSTANTS.YES) {
                $("#backgroundCheckText").text("Click the Submit button to order the background check. DO NOT FAX SIGNED CONSENT FORMS TO ORDER THE BACKGROUND CHECK.  Please include signed background consent forms with new hire paperwork.");

            } else {
                $("#backgroundCheckText").text("Have the candidate sign the background consent form then select 'Yes' and click submit to order the background check");
            }
        } else {
            this.blockFormElements(".backgrndRadio");
            $("input[name='isSignCmpltd']").attr("disabled", "disabled");
            $("#backgroundCheckText").text("");

            $("#bgcFAPAMsg").text("");
//remove4            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
        }
    };

    /*
     * This method is used to set the candidate Information when the associate
     * indicator is AP
     *
     * @param N/A
     * @return N/A
     */
    this.appAssindicatorAP = function() {
        if (CONSTANTS.BackgroundCheckDtls.hasSignedConsent && CONSTANTS.CandidateIntvwDtls.cpdFormComplete) {
            $("input[name='isSignCmpltd'][value='Y']").prop('checked', true);
            this.blockFormElements(".backgrndRadio");
//            this.blockFormElements(".bgcOrderDet"); //bugfix4
            $("input[name='isSignCmpltd']").attr("disabled", "disabled");
            $("#backgroundCheckText").text("There is a signed consent form on file for this candidate.  The background will be ordered.");
            
            // Order Background Check
            if ($("#offerResultCbo option:selected").text() && $("#offerResultCbo option:selected").val() === "DE") {
                $("#backgroundCheckText").text("");
                $("input[name='isSignCmpltd']").prop('checked', true);
                
                $("#bgcFAPAMsg").text("");
            }
        } else {
            if ($("#offerResultCbo option:selected").text() && $("#offerResultCbo option:selected").val() === "AC" && CONSTANTS.CandidateIntvwDtls.cpdFormComplete) {
                $("input[name='isSignCmpltd']").removeAttr("disabled");
                $(".backgrndRadio").unblock();
                /*$(".bgcOrderDet").unblock();*/
            } else {
                this.blockFormElements(".backgrndRadio");
                this.blockFormElements(".bgcFAPA");
                this.blockFormElements(".bgcOrderDet");
                $("input[name='isSignCmpltd']").attr("disabled", "disabled");
                $("#backgroundCheckText").text("");

                $("#bgcFAPAMsg").text("");
                $("input[name='isSignCmpltd']").prop('checked', false);
            }
            if ($("input[name='isSignCmpltd']:checked").val() === CONSTANTS.YES) {
                $("#backgroundCheckText").text("Click the Submit button to order the background check. DO NOT FAX SIGNED CONSENT FORMS TO ORDER THE BACKGROUND CHECK.  Please include signed background consent forms with new hire paperwork.");
            } else if ($("input[name='isSignCmpltd']:checked").val() === CONSTANTS.NO) {
            	$("#backgroundCheckText").text("Have the candidate sign the background consent form then select 'Yes' and click submit to order the background check.");
            } else {
                $("#backgroundCheckText").text("");
                
                $("#bgcFAPAMsg").text("");
            }
        }
    };
    /*
     * Show rehire popup based on the eligibility
     *
     * @param N/A
     * @return N/A
     */
    this.showCpdFormOrRehireEligibleMessage = function() {
        if (!CONSTANTS.CandidateIntvwDtls.cpdFormComplete) {
            $("#cpdFormStatus").text("Incomplete");
			$("#cpdFormStatus").css("color", "red");
            $("#offerMadeCbo").removeAttr("disabled");
            $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
        } else if (!CONSTANTS.CandidateIntvwDtls.rehireEligible) {
            $("#rehireMessage").text("This candidate is not eligible for rehire.  Location Number:" + CONSTANTS.CandidateIntvwDtls.termLoc + " , Term Reason:" + CONSTANTS.CandidateIntvwDtls.termRsn + " , Term Date:" + CONSTANTS.interviewResultsObj.convertSqlDateFormatToStdDateFormat(CONSTANTS.CandidateIntvwDtls.termDt));
            $("#rehireMessage").show();
            //Set CPD Status to complete because this code will only execute if they have completed the CPD Form.
        	$("#cpdFormStatus").text("Complete");
			$("#cpdFormStatus").css("color", "green");
            for ( var i = 0; i < CONSTANTS.OfferMadeList.length; i++) {
                if (CONSTANTS.OfferMadeList[i].data === "R") {
                    $('#offerMadeCbo option:eq(' + i + ')').prop('selected', true);
                    $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
                    // Clear Offer Date, Offer Result. May have been entered in
                    // before CPD Form was filled out.
                    $('#offerResultCbo option:eq(0)').prop('selected', true);
                    $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
                    $("#offerDt").val("");
                }
            }
            // Need to set Offer Section Dirty because the Offer Made selection
            // is made by the application and user cannot change to trigger
            // dirty flag.
            CONSTANTS.isDirtyOfferSection = true;
            $("#offerMadeCbo").attr("disabled", "disabled");
            $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
            $("#offerResultCbo").attr("disabled", "disabled");
            $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
            $("#offerDt").attr("disabled", "disabled");
            this.blockFormElements(".offerDtDiv");
            $(".offerDtLblDiv sub").remove();
            $("input[name='isSignCmpltd']").prop('checked', false);
            this.blockFormElements(".drugTestProf");
            this.blockFormElements(".backgrndRadio");
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            $("input[name='isSignCmpltd']").attr("disabled", "disabled");
            //$(".waitOnCpdInput").unblock();
        } else {
        	$("#cpdFormStatus").text("Complete");
			$("#cpdFormStatus").css("color", "green");
            $("#rehireMessage").hide();
            $("#offerMadeCbo").removeAttr("disabled");
            $("#offerMadeCbo").data("selectBox-selectBoxIt").refresh();
            $("#offerResultCbo").removeAttr("disabled");
            $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
            $("#offerDt").removeAttr("disabled");
            $(".offerDtDiv").unblock();
            this.setDrugTestPanel();
         // $(".waitOnCpdInput").unblock();
        }
    };


    this.hideShowIntvwCompleted = function() {
        if ($("input[name='isIntCmpltd']:checked").val() === CONSTANTS.YES){
            this.hideShowAvailabilityMatch();
            $("#intAvailMatchDiv").unblock();
            // Reset interviewCompletedFlg
            $("input[name='isIntCmpltd'][value='Y']").prop('checked', true);
            $("#noInterviewReason option[value='select']").prop('selected', true);
            $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
            this.blockFormElements(".noReasonDiv");
            $("#nameRowDiv").unblock();
            $("#dateRowDiv").unblock();

        }else if ($("input[name='isIntCmpltd']:checked").val() === CONSTANTS.NO){
            this.hideShowAvailabilityMatch();

            $("input[name='isIntCmpltd'][value='N']").prop('checked', true);
            $("input[name='doesAvailabilityMatch'][value='N']").prop('checked', false);//gxn5764
            $("input[name='doesAvailabilityMatch'][value='Y']").prop('checked', false);//gxn5764
            $("#interviewDetailsMessage").hide();
            $("#interviewerName").val("");
            $("#interviewDt").val("");
            $("#sigNum option[value='select']").prop('selected', true);
            $("#sigNum").data("selectBox-selectBoxIt").refresh();

            for ( var i = 1; i < 11; i++) {
                $("#ID_" + i + "QusNA").prop('checked', true);
                $("#ID_" + i + "QusLbl").text("");
            }
            $("#ID_OverallResultsLbl").text("");

            this.flag_nameOfInterviewer = false;
            this.flag_dateOfInterview = false;
            this.flag_SIG = false;
            this.flag_1RdBtn = false;
            this.flag_2RdBtn = false;
            this.flag_3RdBtn = false;
            this.flag_4RdBtn = false;
            this.flag_5RdBtn = false;
            this.flag_6RdBtn = false;
            this.flag_7RdBtn = false;
            this.flag_8RdBtn = false;
            this.flag_9RdBtn = false;
            this.flag_10RdBtn = false;

            $("#ID_ShowUnfavorable").hide();
            this.blockFormElements("#intAvailMatchDiv");
            $(".noReasonDiv").unblock();
            $('#noInterviewReason').removeAttr("disabled");
            $("#noInterviewReason").data("selectBox-selectBoxIt").refresh();
            this.blockFormElements(".strcutDiv");
            this.blockFormElements("#qnsSections");
            this.blockFormElements(".categoryNameRow");
            this.blockFormElements(".overallStatRow");
            this.blockFormElements("#nameRowDiv");
            this.blockFormElements("#dateRowDiv");
        }

        //Added for FMS 7894 January 2016 CR's
        $(".strcutRow").unblock();
        this.blockFormElements(".strcutDiv");
        $(".intCompltdDiv").unblock();
        if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "N") {
            $(".doNotConsiderDiv").unblock();
        } else {
            this.blockFormElements(".doNotConsiderDiv");
        }
        //*****************************

    }

    /*
     * Hide \ Unhide based on interview completed status
     *
     * @param N/A
     * @return N/A
     */
    this.hideShowAvailabilityMatch = function() {
        // if interviewCompletedFlg is YES
        if ($("input[name='doesAvailabilityMatch']:checked").val() === CONSTANTS.YES){
            CONSTANTS.isDirtyIntvwSection = true;
            this.blockFormElements(".noReasonDiv");
            $(".nameDateRow").unblock();
            $("#nameRowDiv").unblock();
            $("#dateRowDiv").unblock();
            $(".strcutRow").unblock();
            //Added for FMS 7894 January 2016 CR's
            $(".strcutDiv").unblock();
            if (CONSTANTS.interviewResultsObj.selectedCandidate.appAssIndicator === "AP") {
                if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "N") {
                    $(".doNotConsiderDiv").unblock();
                } else {
                    this.blockFormElements(".noReasonDiv");
                }
            }
            //*************************************
            $("#qnsSections").unblock();
            $(".categoryNameRow").unblock();
            $(".overallStatRow").unblock();
            // Clear Interview Details
            //gxn5764 CONSTANTS.interviewResultsObj.clearInterviewDetails();
            // Clear the Offer Details Panel
            CONSTANTS.interviewResultsObj.clearOfferDetails();
            // Clear the Internal License Details Panel
            CONSTANTS.interviewResultsObj.clearIntLicenseDetails();
            // Clear the CPD Form Panel
            CONSTANTS.interviewResultsObj.clearCPDPanel();
//            CONSTANTS.interviewResultsObj.clearMinorConsentPanel();
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            // Reset Screen Dirty Flags
            CONSTANTS.isDirtyIntvwSection = false;
            CONSTANTS.isDirtyOfferSection = false;
            CONSTANTS.isDirtyLicenseSection = false;

            //MTS1876 Added for Data Privacy Apr 2017
            $(".offerDet").unblock();
            CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();

            // Turn Interview Details Panel back on
            $(".intDet").unblock();

            if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "Y") {
                $('#doNotConsiderFor60Days').prop('checked', true);
                $("#interviewDetailsMessage").hide();
            }


            // Reset interviewCompletedFlg
            $("input[name='doesAvailabilityMatch'][value='Y']").prop('checked', true);
            $("input[name='isIntCmpltd'][value='Y']").prop('checked', true);
            $("#interviewDetailsMessage").show();
            // Reset Candidate Name in Interview Panel
            $(".showCandidateNameInterviewPnl").text(CONSTANTS.interviewResultsObj.selectedCandidate.name);
            // Reset Candidate Name in Offer Panel
            $(".showCandidateNameOfferPnl").text(CONSTANTS.interviewResultsObj.selectedCandidate.name);
        } else if ($("input[name='doesAvailabilityMatch']:checked").val() === CONSTANTS.NO) {
            // if interviewCompletedFlg is NO
            $("#interviewDetailsMessage").hide();
            $(".nameDateRow").unblock();
            $("#nameRowDiv").unblock();
            $("#dateRowDiv").unblock();
            $(".strcutRow").unblock();
            this.blockFormElements(".strcutDiv");
           //gxn5764 $(".intCompltdDiv").unblock();
            if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "N") {
                $(".doNotConsiderDiv").unblock();
            } else {
                this.blockFormElements(".doNotConsiderDiv");
            }
            //*****************************
            this.blockFormElements("#qnsSections");
            this.blockFormElements(".categoryNameRow");
            this.blockFormElements(".overallStatRow");
            // Clear Interview Details
            //gxn5764 CONSTANTS.interviewResultsObj.clearInterviewDetails();
            $("#sigNum option[value='select']").prop('selected', true);
            $("#sigNum").data("selectBox-selectBoxIt").refresh();

            for ( var i = 1; i < 11; i++) {
                $("#ID_" + i + "QusNA").prop('checked', true);
                $("#ID_" + i + "QusLbl").text("");
            }
            $("#ID_OverallResultsLbl").text("");

            this.flag_nameOfInterviewer = false;
            this.flag_dateOfInterview = false;
            this.flag_SIG = false;
            this.flag_1RdBtn = false;
            this.flag_2RdBtn = false;
            this.flag_3RdBtn = false;
            this.flag_4RdBtn = false;
            this.flag_5RdBtn = false;
            this.flag_6RdBtn = false;
            this.flag_7RdBtn = false;
            this.flag_8RdBtn = false;
            this.flag_9RdBtn = false;
            this.flag_10RdBtn = false;

            $("#ID_ShowUnfavorable").hide();
            // Clear the Offer Details Panel
            CONSTANTS.interviewResultsObj.clearOfferDetails();
            // Clear the Internal License Details Panel
            CONSTANTS.interviewResultsObj.clearIntLicenseDetails();
            // Clear the CPD Form Panel
            CONSTANTS.interviewResultsObj.clearCPDPanel();
            // Clear the Minor Consent Panel
            CONSTANTS.interviewResultsObj.clearMinorConsentPanel();
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");

            this.blockFormElements(".drugTestProf");


            // Reset Screen Dirty Flags
            CONSTANTS.isDirtyIntvwSection = false;
            CONSTANTS.isDirtyOfferSection = false;
            CONSTANTS.isDirtyLicenseSection = false;
            // Turn Interview Details Panel back on
            $(".intDet").unblock();

            if (CONSTANTS.interviewResultsObj.selectedCandidate.doNotConsiderFor60Days === "Y") {
                $('#doNotConsiderFor60Days').prop('checked', true);
                $("#interviewDetailsMessage").hide();
            }

            // Reset interviewCompletedFlg
            $("input[name='doesAvailabilityMatch'][value='N']").prop('checked', true);
            $("input[name='isIntCmpltd'][value='Y']").prop('checked', true);
            $("#interviewDetailsMessage").hide();
            // Reset Candidate Name in Interview Panel
            $(".showCandidateNameInterviewPnl").text(CONSTANTS.interviewResultsObj.selectedCandidate.name);
            // Reset Candidate Name in Offer Panel
            $(".showCandidateNameOfferPnl").text(CONSTANTS.interviewResultsObj.selectedCandidate.name);
            CONSTANTS.intvwStatus = "C";
        }
        CONSTANTS.interviewResultsObj.setScreenIntvwDirty();
    };
    /*
     * Check Interview Conditions
     *
     * @param N/A
     * @return boolean
     */
    this.checkInterviewConditions = function() {
        var cond1 = (CONSTANTS.interviewResultsObj.flag_nameOfInterviewer && CONSTANTS.interviewResultsObj.flag_dateOfInterview && CONSTANTS.interviewResultsObj.flag_SIG && !$("#doNotConsiderFor60Days").is(":checked"));
        return cond1;
    };

    /*
     * Check the answers selected for the Interview questions
     *
     * @param N/A
     * @return boolean
     */
    this.checkflag_RdBtn = function() {
        var firstCondition = (CONSTANTS.interviewResultsObj.flag_1RdBtn || CONSTANTS.interviewResultsObj.flag_2RdBtn || CONSTANTS.interviewResultsObj.flag_3RdBtn || CONSTANTS.interviewResultsObj.flag_4RdBtn);
        var secondCondition = (CONSTANTS.interviewResultsObj.flag_5RdBtn || CONSTANTS.interviewResultsObj.flag_6RdBtn || CONSTANTS.interviewResultsObj.flag_7RdBtn);
        var cond2 = firstCondition || secondCondition || !$("#doNotConsiderFor60Days").is(":checked");
        return cond2;
    };

    /*
     * Set Flag based on few prameters of interviewer and update details in UI
     *
     * @param N/A
     * @return N/A
     */
    this.flag_NOI = function() {
        CONSTANTS.interviewResultsObj.flag_nameOfInterviewer = true;
        var cond1 = CONSTANTS.interviewResultsObj.checkInterviewConditions();
        var cond2 = (CONSTANTS.interviewResultsObj.checkflag_RdBtn() || CONSTANTS.interviewResultsObj.flag_8RdBtn && CONSTANTS.interviewResultsObj.flag_9RdBtn || CONSTANTS.interviewResultsObj.flag_10RdBtn);
        if (cond1 && cond2 && $("#ID_ShowUnfavorable").is(':hidden') && $("ID_OverallResultsLbl").is(':visible')) {
            $(".offerDet").unblock();
           // CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
            
        } else {
         //   this.blockFormElements(".offerDet");
         //   CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage();
         //   this.blockFormElements(".driverLicDet");
        }
        CONSTANTS.interviewResultsObj.setScreenIntvwDirty();
    };

    /*
     * Set Flag based on few parameters of Do Not Consider Flag and update details in UI
     *
     * @param N/A
     * @return N/A
     */
    this.flag_DNC = function() {
        var cond1 = CONSTANTS.interviewResultsObj.checkInterviewConditions();
        var cond2 = (CONSTANTS.interviewResultsObj.checkflag_RdBtn() || CONSTANTS.interviewResultsObj.flag_8RdBtn && CONSTANTS.interviewResultsObj.flag_9RdBtn || CONSTANTS.interviewResultsObj.flag_10RdBtn);
        if (cond1 && cond2 && $("#ID_ShowUnfavorable").is(':hidden') && $("ID_OverallResultsLbl").is(':visible')  && !$("#doNotConsiderFor60Days").is(":checked")) {
            if ($("#ID_ShowUnfavorable").is(':hidden')) {
            	$(".offerDet").unblock();
            	$("#interviewDetailsMessage").show();
			} else {
            	this.blockFormElements(".offerDet");
				$("#interviewDetailsMessage").hide();								  
            }

            CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
        } else {
            this.blockFormElements(".offerDet");
			$("#interviewDetailsMessage").hide();									 
            CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage();
            this.clearOfferDetails();
            this.blockFormElements(".candidatePersonalData");
            this.blockFormElements(".minorConsent");
            this.blockFormElements(".drugTestProf");
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet"); //bugfix
            this.blockFormElements(".driverLicDet");
        }
        CONSTANTS.interviewResultsObj.setScreenIntvwDirty();
    };
    
    // MTS1876 Added for Data Privacy Apr 2017
    //  Because Offer Results opens when the user clicks Has Interview Been Completed = Yes, if they uncheck Do Not Consider after they checked
    // it by mistake need to unlock Offer Details again.  
    this.flag_DNC_uncheck = function() {
    	if (!$("#doNotConsiderFor60Days").is(":checked") && $("input[name='isIntCmpltd']:checked").val() === CONSTANTS.YES) {
    		if ($("#ID_ShowUnfavorable").is(':hidden')) {
            	$(".offerDet").unblock();
        		CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
        		$("#interviewDetailsMessage").show();
            } else {
            	this.blockFormElements(".offerDet");
            	$("#interviewDetailsMessage").hide();
            }
    	}
    }

    /*
     * Set Flag based on few prameters of interviewer
     *
     * @param N/A
     * @return N/A
     */
    this.flagSIG = function() {
    	$("#sigNum + span ul li:contains('Select')").empty();
        CONSTANTS.interviewResultsObj.setScreenIntvwDirty();
        CONSTANTS.interviewResultsObj.flag_SIG = true;
        var cond1 = CONSTANTS.interviewResultsObj.checkInterviewConditions();
        var cond2 = (CONSTANTS.interviewResultsObj.checkflag_RdBtn() || CONSTANTS.interviewResultsObj.flag_8RdBtn && CONSTANTS.interviewResultsObj.flag_9RdBtn || CONSTANTS.interviewResultsObj.flag_10RdBtn);
        if (cond1 && cond2 && $("#ID_ShowUnfavorable").is(':hidden') && $("ID_OverallResultsLbl").is(':visible')) {
            $(".offerDet").unblock();
        //    CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
        } else {
        //    this.blockFormElements(".offerDet");
        //    CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage();
        //    this.blockFormElements(".driverLicDet");
        }
    };
    /*
     * Set the flag if user makes a change in Interview
     * Details Section
     *
     * @param N/A
     * @return N/A
     */
    this.setScreenIntvwDirty = function() {
        CONSTANTS.isDirtyIntvwSection = true;
    };
    /*
     * Set Date of interview flag based on few parameters
     *
     * @param N/A
     * @return N/A
     */
    this.flag_DOI = function() {
        CONSTANTS.interviewResultsObj.setScreenIntvwDirty();
        CONSTANTS.interviewResultsObj.flag_dateOfInterview = true;
        var cond1 = CONSTANTS.interviewResultsObj.checkInterviewConditions();
        var cond2 = (CONSTANTS.interviewResultsObj.checkflag_RdBtn() || CONSTANTS.interviewResultsObj.flag_8RdBtn && CONSTANTS.interviewResultsObj.flag_9RdBtn || CONSTANTS.interviewResultsObj.flag_10RdBtn);
        if (cond1 && cond2 && $("#ID_ShowUnfavorable").is(':hidden') && $("ID_OverallResultsLbl").is(':visible')) {
            $(".offerDet").unblock();
         //   CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
        } else {
       //     this.blockFormElements(".offerDet");
         //   CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage();
         //   this.blockFormElements(".driverLicDet");
        }
        $('#interviewDt').val($('#interviewDt').val().split('-').join('/'));
        this.stringToDate('#interviewDt') == null ? "" : $('#interviewDt').val(this.stringToDate('#interviewDt'));
        if ($("#interviewDt").val() !== "" && $("#interviewDt").val().trim().length > 0) {
            $("#interviewDt").validateDate();
        } else {
            $('#interviewDt').removeClass('redBorder');
            $('#interviewDt[data-toggle="tooltip"]').removeAttr("title");
            $('#interviewDt[data-toggle="tooltip"]').removeAttr("data-original-title");
            $('#interviewDt[data-toggle="tooltip"]').tooltip();
            $('#interviewDt[data-toggle="tooltip"]').tooltip('hide');
        }
    };
    this.chkTempType = function(temp) {
        return (isNaN(parseInt(temp)) || temp === " ");
    };
    this.stringToDate = function(id) {
        var valueString = $(id).val();
        var inputFormat = "MM/DD/YYY";
        var mask = "";
        var temp = "";
        var dateString = "";
        var monthString = "";
        var yearString = "";
        var j = 0;

        var n = inputFormat.length;
        for (var i = 0; i < n; i++,j++) {
            temp = "" + valueString.charAt(j);
            mask = "" + inputFormat.charAt(i);

            if (mask === "M")
            {
                if (this.chkTempType(temp)) {
                    j--;
                } else {
                    monthString += temp;
                }
            }
            else if (mask === "D") {
                if (this.chkTempType(temp)) {
                    j--;
                } else {
                    dateString += temp;
                }
            }
            else if (mask === "Y") {
                yearString += temp;
            } else if (!this.chkTempType(temp)) {
                return null;
            }
        }

        temp = "" + valueString.charAt(inputFormat.length - i + j);
        if (!(temp === "") && (temp !== " ")) {
            return null;
        }

        var monthNum = parseInt(monthString);
        var dayNum = parseInt(dateString);
        var yearNum = yearString.toString().trim() === "" ? 0 : parseInt(yearString);

        if (isNaN(yearNum) || isNaN(monthNum) || isNaN(dayNum)) {
            return null;
        }

        if (yearString.length === 2 && yearNum < 70) {
            yearNum+=2000;
        }

        var newDate = new Date(yearNum, monthNum - 1, dayNum);

        if (dayNum !== newDate.getDate() || (monthNum - 1) !== newDate.getMonth()) {
            return null;
        }
        return newDate.getMonth()+1 + "/" + newDate.getDate() + "/" + newDate.getFullYear();
    };

    /*
     * set the status of each question
     *
     * @param event
     * @param i
     * @param j
     * @return N/A
     */
    this.setanswerStatus = function(event, i, j) {
        if (event.target.name === j + "Qus") {
            switch (parseInt(event.target.value)) {
            case CONSTANTS.ID_HF:
                $("#ID_" + j + "QusLbl").text("Highly Favorable");
                $("#ID_" + j + "QusLbl").css("color", "#0a742b");
                CONSTANTS.interviewResultsObj["flag_" + j + "RdBtn"] = true;
                break;
            case CONSTANTS.ID_F:
                $("#ID_" + j + "QusLbl").text("Favorable");
                $("#ID_" + j + "QusLbl").css("color", "#79e354");
                CONSTANTS.interviewResultsObj["flag_" + j + "RdBtn"] = true;
                break;
            case CONSTANTS.ID_A:
                $("#ID_" + j + "QusLbl").text("Acceptable");
                $("#ID_" + j + "QusLbl").css("color", "#f5e926");
                CONSTANTS.interviewResultsObj["flag_" + j + "RdBtn"] = true;
                break;
            case CONSTANTS.ID_U:
                $("#ID_" + j + "QusLbl").text("Unfavorable");
                $("#ID_" + j + "QusLbl").css("color", "#f1964c");
                CONSTANTS.interviewResultsObj["flag_" + j + "RdBtn"] = true;
                break;
            case CONSTANTS.ID_HU:
                $("#ID_" + j + "QusLbl").text("Highly Unfavorable");
                $("#ID_" + j + "QusLbl").css("color", "#e71a0f");
                CONSTANTS.interviewResultsObj["flag_" + j + "RdBtn"] = true;
                break;
            case CONSTANTS.ID_NA:
                $("#ID_" + j + "QusLbl").text("");
                CONSTANTS.interviewResultsObj["flag_" + j + "RdBtn"] = false;
                break;
            default:
                break;
            }
        }
    };
    /*
     * This method triggers on click of question section
     *
     * @param event
     * @return N/A
     */
    $("#qnsSections").on('click', 'input[type="radio"]', function(event) {
        CONSTANTS.interviewResultsObj.setScreenIntvwDirty();
        for ( var i = 0; i < 6; i++) {
            for ( var j = 1; j < 11; j++) {
                CONSTANTS.interviewResultsObj.setanswerStatus(event, i, j);
            }
        }

        // Show Overall Interview Score
        CONSTANTS.interviewResultsObj.showOverAllIntvwResults();

        CONSTANTS.interviewResultsObj.showHideRehireMsg();
    });

    /*
     * This method used to confirm the display of rehire message
     *
     * @param N/A
     * @return N/A
     *
     */
    this.showHideRehireMsg = function() {
        var cond1 = CONSTANTS.interviewResultsObj.checkInterviewConditions();
        var cond2 = (CONSTANTS.interviewResultsObj.checkflag_RdBtn() || CONSTANTS.interviewResultsObj.flag_9RdBtn || CONSTANTS.interviewResultsObj.flag_10RdBtn);
        if (cond1 && cond2 && $("#ID_ShowUnfavorable").is(':hidden') && !$("#doNotConsiderFor60Days").is(":checked")) {
            $(".offerDet").unblock();
            CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage();
            CONSTANTS.interviewResultsObj.setCPDPanel(); //bugfix
//            $(".candidatePersonalData").unblock(); //bugfix2
//            $(".minorConsent").unblock(); //bugfix2
            if (isCandMinorFlag) {
                CONSTANTS.interviewResultsObj.setMinorConsent(); //bugfix2            	
            }
            CONSTANTS.interviewResultsObj.setDrugTestPanel();
            CONSTANTS.interviewResultsObj.setBackgroundConsent(); //bugfix
            CONSTANTS.interviewResultsObj.setBgcFAPAPanel();
        } else {
            CONSTANTS.interviewResultsObj.clearOfferDetails();
            CONSTANTS.interviewResultsObj.clearIntLicenseDetails();
            if ($("#ID_OverallResultsLbl").text() === "Unfavorable") {
                this.blockFormElements(".offerDet");
            } else {
            	if ($("#doNotConsiderFor60Days").is(":checked")) {
            		this.blockFormElements(".offerDet");
            	} else {
            		CONSTANTS.interviewResultsObj.showCpdFormOrRehireEligibleMessage(); //bugfix3
            		$(".offerDet").unblock(); // Unblocks when no longer showing as Unfavorable
            	}
            }
//            CONSTANTS.interviewResultsObj.hideCpdFormOrRehireEligibleMessage(); //bugfix3
            this.blockFormElements(".candidatePersonalData");
            this.blockFormElements(".minorConsent");
            this.blockFormElements(".drugTestProf");
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet"); //bugfix
            this.blockFormElements(".driverLicDet");
        }
    };

    this.onSelect_RadioBtn = function() {

    };
    /*
     * This method used to convert sql date format to standard format
     *
     * @param N/A
     * @return N/A
     */
    this.convertSqlDateFormatToStdDateFormat = function(inDate) {
        var formattedDateString = "";

        // Check that inDate is not null
        if (inDate && inDate !== "") {
            // Format Date
            formattedDateString = inDate.toString().substr(5, 2) + "/" + inDate.toString().substr(8, 2) + "/" + inDate.toString().substr(0, 4);
        }
        return formattedDateString;
    };
    /*
     * This method enable \ disable offer details
     *
     * @param N/A
     * @return N/A
     */
    this.enable_OD_Fields = function() {
        if ($("#offerMadeCbo option:selected").text() === "YES") {
            $("#offerResultCbo").removeAttr("disabled");
            $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
            $("#offerDt").removeAttr("disabled");
            $(".offerDtLblDiv sub").remove();
            $(".offerDtLblDiv").append("<sub> *</sub>");
            $(".offerDtDiv").unblock();
        } else {
            $("#offerResultCbo").attr("disabled", "disabled");
            $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
            $('#offerResultCbo option:eq(0)').prop('selected', true);
            $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
            $("#offerDt").attr("disabled", "disabled");
            $(".offerDtLblDiv sub").remove();
            this.blockFormElements(".offerDtDiv");
            $("#offerDt").val("");
            $('#declineReasonCbo option:eq(0)').prop('selected', true);
            $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
            $("#declineReasonCbo").attr("disabled", "disabled");
            $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
            $("input[name='isSignCmpltd']").prop('checked', false);
            this.blockFormElements(".backgrndRadio");
            this.blockFormElements(".drugTestProf");
            this.blockFormElements(".bgcFAPA");
            this.blockFormElements(".bgcOrderDet");
            $("#backgroundCheckText").text("");

            $("#bgcFAPAMsg").text("");
            this.blockFormElements(".driverLicDet");
        }
    };
    /*
     * Enable \ Disable offer made fields
     *
     * @param N/A
     * @return N/A
     */
    this.offerMadeChange = function() {
        CONSTANTS.interviewResultsObj.enable_OD_Fields();
        CONSTANTS.interviewResultsObj.setScreenOfferDirty();
        CONSTANTS.interviewResultsObj.setCPDPanel();
        CONSTANTS.interviewResultsObj.setMinorConsentPanel();
//        CONSTANTS.interviewResultsObj.setDrugTestPanel(); //This gets set from SetCPDPanel
    };
    this.offerDateChange = function() {
        if ($("#offerDt").val() !== "" && $("#offerDt").val().trim().length > 0) {
            $('#offerDt').val($('#offerDt').val().split('-').join('/'));
            this.stringToDate('#offerDt') == null ? "" : $('#offerDt').val(this.stringToDate('#offerDt'));
            $("#offerDt").validateDate();
        } else {
            $('#offerDt').removeClass('redBorder');
            $('#offerDt[data-toggle="tooltip"]').removeAttr("title");
            $('#offerDt[data-toggle="tooltip"]').removeAttr("data-original-title");
            $('#offerDt[data-toggle="tooltip"]').tooltip();
            $('#offerDt[data-toggle="tooltip"]').tooltip('hide');
        }
        CONSTANTS.interviewResultsObj.setScreenOfferDirty();
    };
    /*
     * This method will be triggered on change
     * of the OfferMade
     *
     * @param N/A
     * @return N/A
     */
    this.offerResultChange = function() {
    	$("#offerResultCbo + span ul li:contains('Select')").empty();
        CONSTANTS.interviewResultsObj.enable_OD_DeclineReason();
        CONSTANTS.interviewResultsObj.setScreenOfferDirty();

        isInitDTFlag = false; //This makes sure that the Initiate DrugTest Flag is off.
        CONSTANTS.interviewResultsObj.setCPDPanel();
        CONSTANTS.interviewResultsObj.setMinorConsentPanel();
//        CONSTANTS.interviewResultsObj.setDrugTestPanel(); //This gets set from SetCPDPanel
        CONSTANTS.interviewResultsObj.setBackgroundConsent();
        CONSTANTS.interviewResultsObj.setBgcFAPAPanel();
        CONSTANTS.interviewResultsObj.setIntDriverLicense();
    };
    this.declineResultChange = function() {
    	$("#declineReasonCbo + span ul li:contains('Select')").empty();
    	this.setScreenOfferDirty();
    };
    /*
     * Sets the flag when user makes changes
     * in Offer Details section
     *
     * @param N/A
     * @return N/A
     */
    this.setScreenOfferDirty = function() {
        CONSTANTS.isDirtyOfferSection = true;
    };
    /*
     * Set Interview driver License
     *
     * @param N/A
     * @return N/A
     */
    this.setIntDriverLicense = function() {
        if ($("#offerResultCbo option:selected").text()) {

            var cond1 = (CONSTANTS.interviewResultsObj.applicantType === "INT" && CONSTANTS.BackgroundCheckDtls.isDrivingPos && !CONSTANTS.BackgroundCheckDtls.isAlreadyInDrivingPos);
            var cond2 = ($("#offerResultCbo option:selected").val() === "AC" && CONSTANTS.BackgroundCheckDtls.needBackgroundCheck);
            if (cond1 && (CONSTANTS.BackgroundCheckDtls.hasSignedConsent || $("input[name='isSignCmpltd']:checked").val() === CONSTANTS.YES) && cond2) {
                CONSTANTS.interviewResultsObj.setLicValues();
            } else {
                CONSTANTS.interviewResultsObj.clearIntLicenseDetails();
            }
        }
    };
    /*
     * Set Licence Values
     *
     * @param N/A
     * @return N/A
     */
    this.setLicValues = function() {
        if (CONSTANTS.DriverLicenseDtls.driverLicenseNumber && CONSTANTS.DriverLicenseDtls.driverLicenseStateCode && CONSTANTS.DriverLicenseDtls.driverLicenseNumber !== "" && CONSTANTS.DriverLicenseDtls.driverLicenseStateCode !== "") {

            $("#LI_licNumTxt").val(CONSTANTS.DriverLicenseDtls.driverLicenseNumber);
            // Set State Combo Box
            if (CONSTANTS.reqStateDet && CONSTANTS.reqStateDet.length > 0 && CONSTANTS.DriverLicenseDtls.driverLicenseStateCode) {

                for ( var i = 0; i < CONSTANTS.reqStateDet.length; i++) {
                    CONSTANTS.DriverLicenseDtls.driverLicenseStateCode === CONSTANTS.reqStateDet[i].stateCode ? $('#LI_licStateCbo option:eq(' + i + ')').prop('selected', true) : "";
                    $("#LI_licStateCbo").data("selectBox-selectBoxIt").refresh();
                }
            }
        }
        $(".driverLicDet").unblock();
    };
    /*
     * Candidate Sign completed check
     *
     * @param N/A
     * @return N/A
     */
    this.signCmpltdchkd = function() {
        CONSTANTS.interviewResultsObj.setScreenOfferDirty();
        CONSTANTS.interviewResultsObj.setBackgroundConsent();
        CONSTANTS.interviewResultsObj.setBgcFAPAPanel();
        CONSTANTS.interviewResultsObj.setIntDriverLicense();
    };
    this.licExpDateChange = function() {
        if ($("#LI_expDt").val() !== "" && $("#LI_expDt").val().trim().length > 0) {
            $('#LI_expDt').val($('#LI_expDt').val().split('-').join('/'));
            this.stringToDate('#LI_expDt') == null ? "" : $('#LI_expDt').val(this.stringToDate('#LI_expDt'));
            $("#LI_expDt").validateDate();
        } else {
            $('#LI_expDt').removeClass('redBorder');
            $('#LI_expDt[data-toggle="tooltip"]').removeAttr("title");
            $('#LI_expDt[data-toggle="tooltip"]').removeAttr("data-original-title");
            $('#LI_expDt[data-toggle="tooltip"]').tooltip();
            $('#LI_expDt[data-toggle="tooltip"]').tooltip('hide');
        }
        CONSTANTS.interviewResultsObj.setScreenLicenseDirty();
    };
    /*
     * Sets the flag when user makes changes
     * in License Details section
     *
     * @param N/A
     * @return N/A
     */
    this.setScreenLicenseDirty = function() {
        CONSTANTS.isDirtyLicenseSection = true;
    };
    /*
     * This method is used to validate unsaved data
     * in the following scenarios:
     * 1. When user makes any change to Interview section
     * 2. When user makes any change to Offer Details section
     * 3. When user makes changes to both Interview and
     * Offer Details section
     *
     * @param N/A
     * @return N/A
     */
    this.onCancelBtnClick = function() {
        CONSTANTS.interviewFormCancel = true;

        if (CONSTANTS.isDirtyIntvwSection && CONSTANTS.isDirtyOfferSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Interview and Offer Details Sections.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return;
        } else if (CONSTANTS.isDirtyIntvwSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Interview Details Section.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return;
        } else if (CONSTANTS.isDirtyOfferSection) {
            CONSTANTS.interviewResultsObj.popup.warnUnsavedData("Unsaved data in Offer Details Section.", "Click OK to continue, CANCEL to stay on current page.", this.warningOkClicked.bind(this), this.warningCANCElClicked.bind(this), 150, 400);
            return;
        } else {
            CONSTANTS.interviewResultsObj.onCancelButtonOK();
        }
    };
    /*
     * On click of cancel button
     *
     * @param N/A
     * @return N/A
     */
    this.onCancelButtonOK = function() {
        window.close();
    };
    $(".closeBtnInt").click(function() {
        CONSTANTS.interviewResultsObj.onCancelBtnClick();
    });
    /*
     * launcher page
     *
     * @param data
     * @return N/A
     */
    this.setContent = function(data) {
        $("#divLandingView").html(data);
    };

    /*
     * Check before Submitting the form
     * Thorw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.chkBeforeSubmit = function() {
        // Check that a requisition has been selected
        if (!CONSTANTS.interviewResultsObj.selectedRequisition || $.isEmptyObject(CONSTANTS.interviewResultsObj.selectedRequisition)) {
            CONSTANTS.interviewResultsObj.popup.alert("A Requisition has not been selected.  Please select a Requisition.");
            return true;
        }

        // Check that a candidate has beed selected
        if (!CONSTANTS.interviewResultsObj.selectedCandidate || $.isEmptyObject(CONSTANTS.interviewResultsObj.selectedCandidate)) {
            CONSTANTS.interviewResultsObj.popup.alert("A Candidate has not been selected.  Please select a Candidate.");
            return true;
        }

        // Validate Interview Details
        if (!CONSTANTS.interviewResultsObj.validateInterviewDetails()) {
            return true;
        }
    };

    /*
     * Check Interview Status before Submitting the form
     * Throw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.chkStatusBeforeSubmit = function() {
        // The poups below will have to be configured based on other
        // criteria******
        CONSTANTS.overallInterviewResultText = "";
        if (CONSTANTS.intvwStatus === "F") {
            CONSTANTS.overallInterviewResultText = "Favorable";
        } else if (CONSTANTS.intvwStatus === "A") {
            CONSTANTS.overallInterviewResultText = "Acceptable";
        } else if (CONSTANTS.intvwStatus === "U") {
            CONSTANTS.overallInterviewResultText = "Unfavorable";
        }

        // Make sure that something has changed before continuing....
        if (!CONSTANTS.isDirtyIntvwSection && !CONSTANTS.isDirtyOfferSection && !CONSTANTS.isDirtyLicenseSection) {
            CONSTANTS.interviewResultsObj.popup.alert("No changes have been made to current candidate.");
            return true;
        }
    };

    /*
     * Submit interview Offer Results.
     * Throw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return
     */
    this.submitInterviewOfferResults = function() {
        var isAlertThrown = false;
        isAlertThrown = CONSTANTS.interviewResultsObj.chkBeforeSubmit();
        if (isAlertThrown) {
            return;
        }
        // Validate Offer Details
        if ($("#offerMadeCbo option:selected").index() > 0 && !CONSTANTS.interviewResultsObj.validateOfferResults()) {
            return;
        }
        // Validate License Details
        if (CONSTANTS.interviewResultsObj.validateDrivingLicDtls()) {
            return;
        }

        isAlertThrown = CONSTANTS.interviewResultsObj.chkStatusBeforeSubmit();
        if (isAlertThrown) {
            return;
        }
        CONSTANTS.interviewResultsObj.checkIntCmpltAndSubmit();
    };
    /*
     * Validate Driving License details
     *
     * @param N/A
     * @return N/A
     */
    this.validateDrivingLicDtls = function() {
        return (!$(".driverLicDet .blockUI").length > 0 && !CONSTANTS.interviewResultsObj.validateLicenseDetails());
    };
    this.noIntReasonChange = function() {
    	$("#noInterviewReason + span ul li:contains('Select')").empty();
    	this.setScreenIntvwDirty();
    };
    /*
     * Validate Interview Overall Status and Submit Results
     *
     * @param N/A
     * @return N/A
     */
    this.checkIntCmpltAndSubmit = function() {
        if (CONSTANTS.isDirtyIntvwSection && $("input[name='doesAvailabilityMatch']:checked").val() === CONSTANTS.YES) {
            $('#submitModal .submitWarnMsg').empty();
            $('#submitModal .submitWarnMsg').append("<p>Based on the interview results you have entered, the overall interview result is: " + CONSTANTS.overallInterviewResultText + "</p><p>You cannot change interview results once submitted.  Are you sure you want to proceed?</p>");
            $('#submitModal').modal('show');
            CONSTANTS.interviewResultsObj.promptStaticDraggable('#submitModal');
        } else {
            CONSTANTS.interviewResultsObj.submitResults();
        }

        // Clear the results just calculated...
        CONSTANTS.overallInterviewResultText = "";
    };

    /*
     * Validate Interview Completed
     * Throw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.validateIntCmpltd = function() {
        // Validate Was Interview Completed
        if (!$("input[name='isIntCmpltd']:checked").val()) {
            CONSTANTS.interviewResultsObj.popup.alert("'Was Interview Completed?' is a required field.  Please select an answer.");
            return false;
        }

        // Validate If No, Reason
        if ($("input[name='isIntCmpltd']:checked").val() === CONSTANTS.NO && $("#noInterviewReason option:selected").index() <= 0) {
            CONSTANTS.interviewResultsObj.popup.alert("'If No, Reason' is a required field.  Please select an answer.");
            return false;
        }

        if($("input[name='isIntCmpltd']:checked").val() === CONSTANTS.YES) {
            if (!$("input[name='doesAvailabilityMatch']:checked").val()) {
                CONSTANTS.interviewResultsObj.popup.alert("'Does the candidates availability match with the store scheduling need?' is a required field.  Please select an answer.");
                return false;
            }
        }
        return true;
    };

    /*
     * Validate Interview Name and Date
     * Throw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.validateIntNamedate = function() {
        // Validate Name of Interviewer
        if ($("#interviewerName").val() === "") {
            CONSTANTS.interviewResultsObj.popup.alert("'Interviewer Name' is a required field. please enter a value.");
            return false;
        }

        // Validate Interview Date
        if ($("#interviewDt").val() === "") {
            CONSTANTS.interviewResultsObj.popup.alert("'Interview Date' is a required field.  Please enter a value.");
            return false;
        }
        var vResult = CONSTANTS.interviewResultsObj.validateDate($("#interviewDt").val());
        if (!vResult) {
            CONSTANTS.interviewResultsObj.popup.alert("'Interview Date' is invalid, please enter a valid Date. MM/DD/YYYY");
            return false;
        }
        return true;
    };

    /*
     * Validate Interview Details
     * Throw alert messages and return if
     * input not given by user
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.validateInterviewDetails = function() {
        var scoreResult = "";
        var isReturn = true;

        isReturn = CONSTANTS.interviewResultsObj.validateIntCmpltd();
        if (!isReturn) {
            return false;
        }
        
        // Only validate if Interview Was marked Completed
        if ($("input[name='doesAvailabilityMatch']:checked").val() === CONSTANTS.YES) {
        	scoreResult = CONSTANTS.interviewResultsObj.scoreInterviewResults();
        	if ($("#interviewerName").val() === "" && $("#interviewDt").val() === "" && $("#sigNum option:selected").index() <= 0 
        			&& scoreResult === "invalid") {
        		CONSTANTS.interviewResultsObj.popup.alert("Interview Details are required before submitting.  Please complete Interview Details.");
        		return false;
            } else {
            	isReturn = CONSTANTS.interviewResultsObj.validateIntNamedate();
            	if (!isReturn) {
            		return false;
            	}

            	// Validate SIG Selection
            	if ($("#sigNum option:selected").index() <= 0) {
            		CONSTANTS.interviewResultsObj.popup.alert("'Structured Interview Guide (SIG)#', is a required field.  Please select an answer.");
            		return false;
            	}

            	// Validate that at lease one interview result was entered
            	//scoreResult = CONSTANTS.interviewResultsObj.scoreInterviewResults();
            	if (scoreResult === "invalid") {
            		CONSTANTS.interviewResultsObj.popup.alert("Please enter Interview results for at least one question.");
            		return false;
            	}
            }	
        } else if ($("input[name='doesAvailabilityMatch']:checked").val() === CONSTANTS.NO) {
            if ($("#interviewerName").val() === "" && $("#interviewDt").val() === "") {
                CONSTANTS.interviewResultsObj.popup.alert("Interview Details are required before submitting.  Please complete Interview Details.");
                return false;
            }else {
                isReturn = CONSTANTS.interviewResultsObj.validateIntNamedate();
                if (!isReturn) {
                    return false;
                }
            }
        }

        return true;
    };

    /*
     * Validate Offer Date and Offer Result
     * Thorw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.validateOfferDateResult = function() {
        // Validate Offer Date
        if ($("#offerDt").val() === "") {
            CONSTANTS.interviewResultsObj.popup.alert("'Offer Date' is a required field.  Please enter a value.");
            return false;
        }
        var vResult = CONSTANTS.interviewResultsObj.validateDate($("#offerDt").val());
        if (!vResult) {
            CONSTANTS.interviewResultsObj.popup.alert("'Offer Date' is invalid, please enter a valid Date. MM/DD/YYYY");
            return false;
        }

        // Validate Offer Result
        if ($("#offerMadeCbo option:selected").text() === "YES" && $("#offerResultCbo option:selected").index() <= 0) {
            CONSTANTS.interviewResultsObj.popup.alert("'Offer Result' is a required field.  Please select an answer.");
            return false;
        }
        return true;
    };

    /*
     * Validate Background Consent
     * Throw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.validateBGConsent = function() {
        // Validate Background Consent
    	//When using new BGC Panel, don't need this validation as it is moved to FA PA
    	if (!showNewBgcPanel){
            if ($("#offerResultCbo option:selected").val() === "AC" && $("input[name='isSignCmpltd']:disabled").length <= 0 && !$("input[name='isSignCmpltd']:checked").val()) {
                CONSTANTS.interviewResultsObj.popup.alert("Background Consent is a required field.  Please select an answer.");
                return false;
            }
    	}
    	return true;
    };
    /*
     * Validate the selected OfferResult and Decline Reason
     *
     * @param N/A
     * @return boolean
     */
    this.chkOfferResAndDeclineReason = function() {
        return ($("#offerResultCbo option:selected").val() === "DE" && $("#declineReasonCbo option:selected").index() <= 0);
    };

    /*
     * Validate Offer Results
     * Thorw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.validateOfferResults = function() {
        var isReturn = true;
        // Validate Offer Made
        if ($("#offerMadeCbo option:selected").index() <= 0) {
            CONSTANTS.interviewResultsObj.popup.alert("Offer Made is a required field.  Please select an answer.");
            return false;
        }

        // Validate Offer Date
        if ($("#offerMadeCbo option:selected").text() === "YES") {
            isReturn = CONSTANTS.interviewResultsObj.validateOfferDateResult();
            if (!isReturn) {
                return false;
            }

            // Validate Decline Reason
            if (CONSTANTS.interviewResultsObj.chkOfferResAndDeclineReason()) {
                CONSTANTS.interviewResultsObj.popup.alert("'Decline Reason' is a required field.  Please select an answer.");
                return false;
            }
            //validate Background Consent
            isReturn = CONSTANTS.interviewResultsObj.validateBGConsent();
            if (!isReturn) {
                return false;
            }

        }
        return true;
    };
    /*
     * Validate License Details
     * Thorw alert messages and return if
     * input not given by user.
     * Stops the update process
     *
     * @param N/A
     * @return boolean
     */
    this.validateLicenseDetails = function() {
    	//When using new BGC Panel, don't need this validation as it is moved to FA PA
    	if (!showNewBgcPanel){
    		if ($("#LI_licNumTxt").val() === "") {
    			CONSTANTS.interviewResultsObj.popup.alert("Driver's License Number is required.  Please enter a value.");
    			return false;
    		}

    		if ($("#LI_licNumTxt_2").val() === "") {
    			CONSTANTS.interviewResultsObj.popup.alert("Confirm Driver's License Number is required.  Please enter a value.");
    			return false;
    		}

    		if ($("#LI_licNumTxt").val().toUpperCase() !== $("#LI_licNumTxt_2").val().toUpperCase()) {
    			CONSTANTS.interviewResultsObj.popup.alert("Confirm Driver's License Number does not match Driver's License Number.  Please correct.");
    			return false;
    		}

    		if ($("#LI_licStateCbo option:selected").index() <= 0) {
    			CONSTANTS.interviewResultsObj.popup.alert("Driver's License State is required.  Please select a value.");
    			return false;
    		}

    		var vResult = CONSTANTS.interviewResultsObj.validateDate($("#LI_expDt").val());
    		if (!vResult) {
    			CONSTANTS.interviewResultsObj.popup.alert("Invalid Driver's License Expiration Date.  Please enter a valid Date.");
    			return false;
    		}
    	}
        return true;
    };
    /*
     * Remove backdrop blur and close warning popup
     * on click of NO button.
     *
     * @param N/A
     * @return N/A
     */
    this.warningNOClicked = function() {
        $("#submitModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#interviewResults").removeClass("blurBody");
        });
        $('#submitModal').modal('hide');
    };

    this.warningNOdoNotConsiderClicked = function() {
        $("#doNotConsiderWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#interviewResults").removeClass("blurBody");
        });
        $('#doNotConsiderWarnModal').modal('hide');
        $('#doNotConsiderFor60Days').prop('checked', false);
        if ($("input[name='doesAvailabilityMatch']:checked").val() === CONSTANTS.YES && $("#ID_ShowUnfavorable").is(':hidden')) {
            $("#interviewDetailsMessage").show();
        } else {
        	$("#interviewDetailsMessage").hide();
        }
        //CONSTANTS.interviewResultsObj.flag_DNC();
    };

    this.warningYESdoNotConsiderClicked = function() {
        $("#doNotConsiderWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#interviewResults").removeClass("blurBody");
        });
        $('#doNotConsiderWarnModal').modal('hide');
        CONSTANTS.interviewResultsObj.flag_DNC();
    };

    /*
     * Check if the Interview Completed
     *
     * @param N/A
     * @return N/A
     */
    this.isIntCmpltd = function() {
        // Offer = Select
        if ($("#offerMadeCbo option:selected").val() === "0") {
            CONSTANTS.CandidateIntvwDtls.candOfferMade = "";
            CONSTANTS.CandidateIntvwDtls.candOfferStat = "";
            CONSTANTS.CandidateIntvwDtls.candOfferDate = "";
            CONSTANTS.CandidateIntvwDtls.offerDeclineReasonCd = "0";
        } else if ($("#offerMadeCbo option:selected").val() === "Y") {
            CONSTANTS.CandidateIntvwDtls.candOfferMade = $("#offerMadeCbo option:selected").val();
            CONSTANTS.CandidateIntvwDtls.candOfferStat = $("#offerResultCbo option:selected").val();
            CONSTANTS.CandidateIntvwDtls.candOfferDate = $("#offerDt").val();
            if ($("#offerResultCbo option:selected").val() === "DE") {
                CONSTANTS.CandidateIntvwDtls.offerDeclineReasonCd = $("#declineReasonCbo option:selected").val();
            } else {
                CONSTANTS.CandidateIntvwDtls.offerDeclineReasonCd = "0";
            }
        } else if ($("#offerResultCbo option:selected").val() !== "Y") {
            CONSTANTS.CandidateIntvwDtls.candOfferMade = $("#offerMadeCbo option:selected").val();
            CONSTANTS.CandidateIntvwDtls.candOfferStat = "";
            CONSTANTS.CandidateIntvwDtls.candOfferDate = "";
            CONSTANTS.CandidateIntvwDtls.offerDeclineReasonCd = "0";
        }
    };

    /*
     * Update Interview Result Details
     * Formation of Request to update the details
     *
     * @param N/A
     * @return N/A
     */
    this.submitResults = function() {
        $("#submitModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#interviewResults").removeClass("blurBody");
        });
        $('#submitModal').modal('hide');
        var saveObj = {}; // InterviewResultsSaveVO
        saveObj.userId = CONSTANTS.userProfile.userId;
        // Set Applicant Type
        CONSTANTS.CandidateIntvwDtls.applicantType = CONSTANTS.interviewResultsObj.applicantType;
        // Set Associate ID
        saveObj.aid = CONSTANTS.interviewResultsObj.selectedCandidate.aid;
        //Candidate Reference Number
        saveObj.candRefNbr = CONSTANTS.interviewResultsObj.selectedCandidate.candRefNbr;
        // Set Candidate Name
        saveObj.candidateName = CONSTANTS.interviewResultsObj.selectedCandidate.name;
        // Get Interview Details
        if (CONSTANTS.isDirtyIntvwSection) {
            saveObj.updateIntvwDtls = "Y";

            //Added for FMS 7894 January 2016 CR's
            saveObj.doNotConsiderFor60Days = $("#doNotConsiderFor60Days").is(":checked") ? "Y" : "N";

            if ($("input[name='isIntCmpltd']:checked").val() === CONSTANTS.NO) {
                CONSTANTS.CandidateIntvwDtls.intvwStat = $("#noInterviewReason option:selected").val();
                saveObj.wasInterviewCompleted = "N";
            } else {

                if ($("input[name='doesAvailabilityMatch']:checked").val() === CONSTANTS.NO) {
                    this.hideShowAvailabilityMatch();
                    saveObj.doesAvailabilityMatch = "N";
                    CONSTANTS.CandidateIntvwDtls.intvwStat = CONSTANTS.intvwStatus;
                    saveObj.wasInterviewCompleted = "Y";
                    saveObj.interviewerName = $("#interviewerName").val();
                    saveObj.dateOfIntvw = $("#interviewDt").val();
                } else {
                    saveObj.doesAvailabilityMatch = "Y";
                    CONSTANTS.CandidateIntvwDtls.intvwStat = CONSTANTS.intvwStatus;
                    saveObj.wasInterviewCompleted = "Y";
                    saveObj.interviewerName = $("#interviewerName").val();
                    saveObj.dateOfIntvw = $("#interviewDt").val();
                    saveObj.selectedSig = $("#sigNum option:selected").val();
                    for ( var i = 1; i < 11; i++) {
                        saveObj["intvwQuestion" + i] = $("input[name='" + i + "Qus']:checked").val().toString();
                    }
                }

            }



        } else {
            saveObj.updateIntvwDtls = "N";
        }
        if (CONSTANTS.isDirtyOfferSection) {
            saveObj.backgroundConsent = $("input[name='isSignCmpltd']:checked").val() ? $("input[name='isSignCmpltd']:checked").val().toString() : "";
            if ($("input[name='isIntCmpltd']:checked").val() === CONSTANTS.YES) {
                saveObj.updateOfferDtls = "Y";
                CONSTANTS.interviewResultsObj.isIntCmpltd();

            } else {
                saveObj.updateOfferDtls = "N";
            }
        } else {
            saveObj.updateOfferDtls = "N";
        }
        // License Information
        if (CONSTANTS.interviewResultsObj.submitCheckLicInfo()) {
            saveObj.updateDriverLicense = CONSTANTS.isDirtyLicenseSection ? "Y" : "N";

            saveObj.licNum = $("#LI_licNumTxt").val();
            saveObj.licState = $("#LI_licStateCbo option:selected").index() > 0 ? $("#LI_licStateCbo option:selected").val() : "";
            saveObj.licExpDate = $("#LI_expDt").val();
        }
        CONSTANTS.interviewResultsObj.submitCandData(saveObj);

    };

    /*
     * Validate Applicant type and the Driving Details
     *
     * @param N/A
     * @return boolean
     */
    this.submitCheckLicInfo = function() {
        return (CONSTANTS.CandidateIntvwDtls.applicantType === "INT" && CONSTANTS.BackgroundCheckDtls.isDrivingPos && !$(".driverLicDet .blockUI").length > 0);
    };
    this.chkEmptyVal = function(val) {
        return (val ? val : "");
    };
    this.chkNullVal = function(val) {
        return (val ? val : "null");
    };

    /*
     * Updated candidate details object
     * Formation of Request to update the details
     *
     * @param N/A
     * @return N/A
     */
    this.submitCandData = function(saveObj) {
        CONSTANTS.submitCandData = {
            "UpdateIntvwRltsCandIntvwDtlsRequest" : {
                "CandidateInformation" : {
                    "applicantType" : CONSTANTS.CandidateIntvwDtls.applicantType,
                    "humanResourcesSystemStoreNumber" : CONSTANTS.CandidateIntvwDtls.strNbr,
                    "employmentRequisitionNumber" : CONSTANTS.CandidateIntvwDtls.reqNbr,
                    "aid" : saveObj.aid,
                    "name" : saveObj.candidateName,
                    "activeFlag" : CONSTANTS.CandidateIntvwDtls.activeFlg,
                    "employmentPositionCandidateId" : CONSTANTS.CandidateIntvwDtls.candId,
                    "candidateOfferMadeFlag" : CONSTANTS.interviewResultsObj.chkEmptyVal(CONSTANTS.CandidateIntvwDtls.candOfferMade),
                    "candidateOfferStatusIndicator" : CONSTANTS.interviewResultsObj.chkEmptyVal(CONSTANTS.CandidateIntvwDtls.candOfferStat),
                    "createUserId" : CONSTANTS.CandidateIntvwDtls.createUserId,
                    "drugTestId" : CONSTANTS.interviewResultsObj.chkEmptyVal(CONSTANTS.CandidateIntvwDtls.drugTestId),
                    "drugTestResultCode" : CONSTANTS.interviewResultsObj.chkEmptyVal(CONSTANTS.CandidateIntvwDtls.drugTestRsltCd),
                    "employeeStatusActionCode" : CONSTANTS.interviewResultsObj.chkEmptyVal(CONSTANTS.CandidateIntvwDtls.empStatActCd),
                    "humanResourcesActionSequenceNumber" : CONSTANTS.CandidateIntvwDtls.hrActSeqNum,
                    "interviewStatusIndicator" : CONSTANTS.CandidateIntvwDtls.intvwStat,
                    "offerDeclineReasonCode" : CONSTANTS.CandidateIntvwDtls.offerDeclineReasonCd,
                    "referenceCheckResultIndicator" : CONSTANTS.interviewResultsObj.chkEmptyVal(CONSTANTS.CandidateIntvwDtls.refChkInd),
                    "employmentCandidateId" : saveObj.candRefNbr
                },
                "BackgroundCheckInfo" : {
                    "hasSignedConsent" : CONSTANTS.BackgroundCheckDtls.hasSignedConsent,
                    "isDrivingPos" : CONSTANTS.BackgroundCheckDtls.isDrivingPos,
                    "isAlreadyInDrivingPos" : CONSTANTS.BackgroundCheckDtls.isAlreadyInDrivingPos,
                    "needBackgroundCheck" : CONSTANTS.BackgroundCheckDtls.needBackgroundCheck,
                    "backgroundPackageNeeded" : CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded,
                    "backgroundComponentsNeeded" : CONSTANTS.BackgroundCheckDtls.backgroundComponentsNeeded,
                    "humanResourcesStoreTypeCode" : CONSTANTS.BackgroundCheckDtls.humanResourcesStoreTypeCode,
                    "dateOfBirth" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.dateOfBirth),
                    "extLicenseNum" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.ext_licNum),
                    "extLicenseState" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.ext_licState)
                }
            }
        };
        if (CONSTANTS.CandidateIntvwDtls.activeDate) {
            CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.CandidateInformation.activeDate = {
                "month" : CONSTANTS.CandidateIntvwDtls.activeDate.toString().substr(0, 2),
                "day" : CONSTANTS.CandidateIntvwDtls.activeDate.toString().substr(3, 2),
                "year" : CONSTANTS.CandidateIntvwDtls.activeDate.toString().substr(6, 4)
            };
            CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.CandidateInformation.adverseActionDate = {
                "month" : CONSTANTS.CandidateIntvwDtls.adverseActDate.toString().substr(0, 2),
                "day" : CONSTANTS.CandidateIntvwDtls.adverseActDate.toString().substr(3, 2),
                "year" : CONSTANTS.CandidateIntvwDtls.adverseActDate.toString().substr(6, 4)
            };
        }
        CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.CandidateInformation.candidateOfferMadeDate = {
            "month" : CONSTANTS.CandidateIntvwDtls.candOfferDate.toString().substr(0, 2),
            "day" : CONSTANTS.CandidateIntvwDtls.candOfferDate.toString().substr(3, 2),
            "year" : CONSTANTS.CandidateIntvwDtls.candOfferDate.toString().substr(6, 4)
        };
        if (CONSTANTS.CandidateIntvwDtls.inactiveDate) {
            CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.CandidateInformation.inactiveDate = {
                "month" : CONSTANTS.CandidateIntvwDtls.inactiveDate.toString().substr(0, 2),
                "day" : CONSTANTS.CandidateIntvwDtls.inactiveDate.toString().substr(3, 2),
                "year" : CONSTANTS.CandidateIntvwDtls.inactiveDate.toString().substr(6, 4)
            };
        }
        CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.CandidateInformation.letterOfIntentDate = {
            "month" : CONSTANTS.CandidateIntvwDtls.letterOfIntentDate.toString().substr(0, 2),
            "day" : CONSTANTS.CandidateIntvwDtls.letterOfIntentDate.toString().substr(3, 2),
            "year" : CONSTANTS.CandidateIntvwDtls.letterOfIntentDate.toString().substr(6, 4)
        };

        CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.CandidateInformation.submitInterviewResultsDT = {
                "month" : CONSTANTS.CandidateIntvwDtls.submitInterviewResultsDT.toString().substr(0, 2),
                "day" : CONSTANTS.CandidateIntvwDtls.submitInterviewResultsDT.toString().substr(3, 2),
                "year" : CONSTANTS.CandidateIntvwDtls.submitInterviewResultsDT.toString().substr(6, 4)
        };

        CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.CandidateInformation.submitOfferResultsDT = {
                "month" : CONSTANTS.CandidateIntvwDtls.submitOfferResultsDT.toString().substr(0, 2),
                "day" : CONSTANTS.CandidateIntvwDtls.submitOfferResultsDT.toString().substr(3, 2),
                "year" : CONSTANTS.CandidateIntvwDtls.submitOfferResultsDT.toString().substr(6, 4)
        };

        CONSTANTS.submitCandData = this.setInterviewInformation(CONSTANTS.submitCandData, saveObj);
        var data = {
                data : JSON.stringify({
                    "UpdateIntvwRltsCandIntvwDtlsRequest" : CONSTANTS.submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest
                })
            };
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.updatedInterviewResultsCandIntDtls);
        RSASERVICES.updateInterviewResultsCandidateIntvwDtls(data, callbackFunction, true, "Please wait...");
    };
    /*
     * Formation of the Interview Information object
     *
     * @param submit candidate data
     * @param save object
     * @return submit candidate data
     */
    this.setInterviewInformation = function(submitCandData, saveObj) {
        submitCandData.UpdateIntvwRltsCandIntvwDtlsRequest.InterviewInformation = {
                "updateIntvwDtls" : saveObj.updateIntvwDtls,
                "updateOfferDtls" : saveObj.updateOfferDtls,
                "userId" : saveObj.userId,
                "wasInterviewCompleted" : saveObj.wasInterviewCompleted,
                "doesAvailabilityMatch" :saveObj.doesAvailabilityMatch,
                "noInterviewReason" : saveObj.noInterviewReason,
                "interviewerName" : saveObj.interviewerName,
                "selectedSig" : saveObj.selectedSig,
                "dateOfIntvw" : saveObj.dateOfIntvw,
                "intvwQuestion1" : saveObj.intvwQuestion1,
                "intvwQuestion2" : saveObj.intvwQuestion2,
                "intvwQuestion3" : saveObj.intvwQuestion3,
                "intvwQuestion4" : saveObj.intvwQuestion4,
                "intvwQuestion5" : saveObj.intvwQuestion5,
                "intvwQuestion6" : saveObj.intvwQuestion6,
                "intvwQuestion7" : saveObj.intvwQuestion7,
                "intvwQuestion8" : saveObj.intvwQuestion8,
                "intvwQuestion9" : saveObj.intvwQuestion9,
                "intvwQuestion10" : saveObj.intvwQuestion10,
                "licNum" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.licNum),
                "licState" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.licState),
                "licExpDate" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.licExpDate),
                "backgroundConsent" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.backgroundConsent),
                "updateLicenseDtls" : CONSTANTS.interviewResultsObj.chkEmptyVal(saveObj.updateDriverLicense),
                //Added for FMS 7894 January 2016 CR's
                "doNotConsiderFor60Days" : saveObj.doNotConsiderFor60Days
            };
        return submitCandData;
    };
    /*
     * Update Interview details based on selected candidate
     * In case of error retrieved from service,
     * Alert is thrown with the error message
     *
     * @param response
     * @return N/A
     */
    this.updatedInterviewResultsCandIntDtls = function(json) {
        $.unblockUI();
        var userMessage = "";

        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
            if (json.Response.hasOwnProperty("successMsg")) {
                userMessage = json.Response.successMsg;
            }
            if (userMessage !== "") {
                CONSTANTS.interviewResultsObj.popup.alert(userMessage);
            } else {
                CONSTANTS.interviewResultsObj.popup.alert("Details Saved Successfully");
            }

            // Clear userMessage
            userMessage = "";
            // calls function to reset
            // candidates
            CONSTANTS.interviewResultsObj.refreshCandidatesAfterSubmit();
        }

        if (json.Response.hasOwnProperty("error") && json.Response.error.hasOwnProperty("errorMsg")) {
            CONSTANTS.interviewResultsObj.popup.alert(json.Response.error.errorMsg);
        }
    };

    /*
     * Remove backdrop blur and close alert popup
     * on click of OK button.
     *
     * @param N/A
     * @return N/A
     */
    this.alertOkClicked = function() {
        $("#alertPopupModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#interviewResults").removeClass("blurBody");
        });
        $('#alertPopupModal').modal('hide');
    };
    /*
     * Refresh Candidate details after submit and
     * invoke Candidate details again and load the grid.
     *
     * @param N/A
     * @return N/A
     */
    this.refreshCandidatesAfterSubmit = function() {
        CONSTANTS.candDtlsRowSelected.row = -1;
        // Clear the Interview Panel Details
        CONSTANTS.interviewResultsObj.clearInterviewDetails();
        // Clear the Offer Details Panel
        CONSTANTS.interviewResultsObj.clearOfferDetails();
        // Clear the Internal License Details Panel
        CONSTANTS.interviewResultsObj.clearIntLicenseDetails();
        // Clear the CPD Form Panel
        CONSTANTS.interviewResultsObj.clearCPDPanel();
        // Clear the Minor Consent Panel
        CONSTANTS.interviewResultsObj.clearMinorConsentPanel();
        CONSTANTS.interviewResultsObj.clearMinorConsentPanelValues();
        // Clear the Drug Test Panel
        CONSTANTS.interviewResultsObj.clearDrugTestPanel();
        CONSTANTS.interviewResultsObj.clearDrugTestPanelValues();
        CONSTANTS.interviewResultsObj.clearBgcFAPAPanel();
        // Reset Screen Dirty Flags
        CONSTANTS.isDirtyIntvwSection = false;
        CONSTANTS.isDirtyOfferSection = false;
        CONSTANTS.isDirtyLicenseSection = false;

        // Reset Background Consent text
        $("#backgroundCheckText").text("");

        $("#bgcFAPAMsg").text("");
        this.blockFormElements(".bgcFAPA");
        this.blockFormElements(".bgcOrderDet");
        
        //Reset the Show Interview Details to collapse 
		$(".interviewDetailsDiv").css("display", "none");
		$("#interviewDetailsToggle").val("Show Interview Details");
		/* $("#interviewDetailsMessage").show(); */

		// Get Candidates for selected requisition
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(CONSTANTS.interviewResultsObj.loadCandidatesByReq);
        RSASERVICES.getInterviewResultsCandidatesByReq(CONSTANTS.interviewResultsObj.selectedRequisition.reqNbr, callbackFunction, true, "Please wait...");
    };
    /*
     * Validate date format
     *
     * @param N/A
     * @return boolean
     */
    this.validateDate = function(dtValue) {
        var dtRegex = new RegExp(/\b\d{1,2}[\/]\d{1,2}[\/]\d{4}\b/);
        return dtRegex.test(dtValue);
    };
    /*
     * Make backdrop blur when pop up appears
     * Make pop ups draggable and static
     *
     * @param id
     * @return N/A
     */
    this.promptStaticDraggable = function(id) {
        //makes the pop up static
        $(id).modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $(id).off("shown.bs.modal");
        $(id).on("shown.bs.modal", function() {
            $(".alertmodalbtn").focus();
            $(id).find('.modal-dialog').addClass("modal-dialog-center");
            // Set top and left margin to look alike flex screens
            $(id).find('.modal-dialog').css({
                'margin-top' : function() {
                    return -($(id+' .modal-dialog').outerHeight() / 2);
                },
                'margin-left' : function() {
                    return -($(id+' .modal-dialog').outerWidth() / 2);
                }
            });
            $("#StrNumModalText").focus();
        });
        //allows user to drag the pop up
        $(id).draggable({
            handle : ".modal-header"
        });
    };
    /*
     * blocks UI elements
     *
     * @param id
     * @return N/A
     */
    this.blockFormElements = function(id) {
        if ($(".modal-backdrop.fade.in").length > 1) {
            $(".modal-backdrop.fade.in:eq(1)").css({"z-index":"1600"});
        }
        $(id).block({
            message : null,
            overlayCSS : {
                backgroundColor : "#FFF",
                opacity : 0.6,
                cursor : "auto"
            },
            ignoreIfBlocked : false,
            baseZ : 100
        });
    };
    this.onCloseStrPopup = function() {
        this.blockFormElements('html');
    };
    /*
     * Triggered during key down in text box if data-pattern
     * attribute is specified for the input element
     */
    $("input[data-pattern]").assignKeyDownToInputs();

    this.doNotConsiderWarning = function() {
    	CONSTANTS.doNotConsiderOk = true;
    	if ($("#doNotConsiderFor60Days").is(":checked")) {
            $('#doNotConsiderWarnModal .doNotConsiderMsg').empty();
            $('#doNotConsiderWarnModal .doNotConsiderMsg').append("<p>By checking this box, this candidate will NOT be available to your store for 60 days.</p><p>Are you sure you want to proceed?</p>");
            $('#doNotConsiderWarnModal').modal('show');
            CONSTANTS.interviewResultsObj.promptStaticDraggable('#doNotConsiderWarnModal');
            $("#interviewDetailsMessage").hide();
    	}
    };
    
    this.showHideInterviewResults = function() {
    	if ($(".interviewDetailsDiv").is(":visible")) {
    		$(".interviewDetailsDiv").css("display", "none");
    		$("#interviewDetailsToggle").val("Show Interview Details");
    		$("#interviewDetailsToggle").prop("title", "Show Interview Details");
    		if (CONSTANTS.interviewResultsObj.selectedCandidate.submitInterviewResultsDT !== "") {
    		/*	$("#interviewDetailsMessage").hide();*/
    		} else {
    			/*$("#interviewDetailsMessage").hide();*/
    		}
    		$('#interviewDetailDiv')[0].scrollIntoView(true);
    	} else {
    		$(".interviewDetailsDiv").css("display", "block");
    		$("#interviewDetailsToggle").val("Hide Interview Details");
    		$("#interviewDetailsToggle").prop("title", "Hide Interview Details");
    		/*$("#interviewDetailsMessage").hide();*/
    		$('#interviewDetailDiv')[0].scrollIntoView(true);
    	}
    }

    // Drug Test Panel Refusal Yes and No Buttons
    this.warningYESAcceptDTRefusal = function() {
        $("#DTRefusalWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#drugTestProf").removeClass("blurBody");
        });
        $('#DTRefusalWarnModal').modal('hide');
        /*            $("input[name='isDrugTestReady']").prop('checked', false);*/
        this.clearDrugTestPanel();
        isInitDTFlag = false;
        document.getElementById("offerDet").scrollIntoView();
        
        for ( var i = 0; i < CONSTANTS.OfferResultList.length; i++) {
            if (CONSTANTS.OfferResultList[i].data === "DE") {
            	$('#offerResultCbo option:eq(' + (i+1) + ')').prop('selected', true);
                $("#offerResultCbo").data("selectBox-selectBoxIt").refresh();
            }
        }
        for ( var j = 0; j < CONSTANTS.OfferDeclineReasonList.length; j++) {
            if (CONSTANTS.OfferDeclineReasonList[j].data === 7) {
                  $('#declineReasonCbo option:eq(' + (j+1) + ')').prop('selected', true);
                $("#declineReasonCbo").data("selectBox-selectBoxIt").refresh();
            }
        }
        
        this.blockFormElements(".candidatePersonalData");
        this.blockFormElements(".minorConsent");
        this.blockFormElements(".drugTestProf")
        this.blockFormElements(".bgcFAPA");
        this.blockFormElements(".bgcOrderDet");
        this.blockFormElements(".driverLicDet");
        CONSTANTS.isDirtyOfferSection = true;
    };
    
    this.warningNOCancelDTRefusal = function() {
        $("#DTRefusalWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#drugTestProf").removeClass("blurBody");
/*          $("input[name='isDrugTestReady']").prop('checked', false);*/
            if ($('#drugTestInitDT').is(':visible')) {
            	$('input:radio[name="isDrugTestReady"]').filter('[value="Y"]').attr('checked', true);            	
            } else {
            	$("input[name='isDrugTestReady']").prop('checked', false);
            }
        });
        $('#DTRefusalWarnModal').modal('hide');
    };
    
    // Drug Test Panel Re-initiate Yes and No Buttons
    this.warningYESAcceptDTReInit = function() {
        $("#DTReInitWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#drugTestProf").removeClass("blurBody");
        });
        $('#DTReInitWarnModal').modal('hide');
        if (dtestType === "ORAL"){
//NewWindowMethod			faportal = window.open("","FAPortlet");
//NewWindowMethod			faportal.document.write("<html><body><center><p>First Advantage Portlet is now loading...</p><br><img src='assets/images/loadingAnimation.gif'></center></body></html>");
        	if (typeof faauth === 'undefined' || faauth === false){
        		$.blockUI({
	    			message : $('#initiating')
	    		});
        		envVars = checkEnv();
		    	fapl = envVars.fapl;
		    	fapAuthLp = envVars.fapAuthLp;
		    	faportal = window.open(fapAuthLp,"auth","Height=1px,Width=1px,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no,top=100000000");
		    	
 			    // Polling to check when window is closed
			    var interval = setInterval(function() {
			    	if (faportal.closed){
			            clearInterval(interval);
					    faauth = true;
				    	getDTOrderNbr(dtestType);
			        }    
			    }, 100);
			} else if(faauth === true){
				getDTOrderNbr(dtestType);
			}
        } else { // if (dtestType === "CONV") {
        	getDTOrderNbr(dtestType);
        }
    };

    this.warningNOCancelDTReInit = function() {
        $("#DTReInitWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#drugTestProf").removeClass("blurBody");
        });
        $('#DTReInitWarnModal').modal('hide');
        $("#drugTestInitDT").removeAttr("disabled");
    };
    
    // Popup for Drug Test Panel Refusal
    function DTRefusalWarning() {
    	$('#DTRefusalWarnModal .DTRefusalMsg').empty();
        $('#DTRefusalWarnModal .DTRefusalMsg').append("<p>If the candidate refuses to take a drug test, they will not be allowed to move forward in the hiring process.</p><p>Are you sure you want to continue and record a refusal?</p>");
        $('#DTRefusalWarnModal').modal('show');
        CONSTANTS.interviewResultsObj.promptStaticDraggable('DTRefusalWarnModal');
    };
    
    // Popup Warning for Drug Test Panel Re-initiate - Are you sure you wish to initiate the request again?
    function DTReInitWarning(){
    	$('#DTReInitWarnModal .DTReInitMsg').empty();
        $('#DTReInitWarnModal .DTReInitMsg').append("<p>A completed chain of custody is already on file.</p><p>Are you sure you wish to submit a new order?</p>");
        $('#DTReInitWarnModal').modal('show');
        CONSTANTS.interviewResultsObj.promptStaticDraggable('DTReInitWarnModal');	
    };

    // Popup Warning for BGC Panel Re-initiate - Are you sure you wish to initiate the request again?
    function BGCReInitWarning(){
    	$('#BGCReInitWarnModal .BGCReInitMsg').empty();
        $('#BGCReInitWarnModal .BGCReInitMsg').append("<p>A background check consent has already been initiated. A new consent should only be created if the candidate's personal information has been updated.</p><p>Are you sure you wish to initiate a new consent?</p>");
        $('#BGCReInitWarnModal').modal('show');
        CONSTANTS.interviewResultsObj.promptStaticDraggable('BGCReInitWarnModal');	
    };
    
    checkEnv = function(){
    	var urlEnv = window.location.hostname;
    	var eorigin = "";
    	var fapl = "";
    	var fapAuthLp = "";
    	var dcsuri = "";

    	if(urlEnv.indexOf("webapps.") > -1){
    		eorigin = "https://collection.fadv.com";
    		fapl = "https://collection.fadv.com/?idp=https%3A%2F%2Fthdsaml.homedepot.com";
    		fapAuthLp = "https://collection.fadv.com/SecureLanding/?idp=https%3A%2F%2Fthdsaml.homedepot.com";
    		dcsuri = "https://paperless-onboarding-ui.apps.homedepot.com";
    		bgcfapauri = "https://pa.fadv.com";
    	} else {
    		eorigin = "https://uat.collection.fadv.com";
    		fapl = "https://uat.collection.fadv.com/?idp=https%3A%2F%2Fdevsaml.homedepot.com";
    		fapAuthLp = "https://uat.collection.fadv.com/SecureLanding/?idp=https%3A%2F%2Fdevsaml.homedepot.com";
    		dcsuri = "https://paperless-onboarding-ui.apps-np.homedepot.com";
    		//bgcfapauri = "https://pa-test.fadv.com";  //For QA normally.
    		bgcfapauri = "https://pa-ua.fadv.com";  //For Performance
    	}
    	return {fapl: fapl, fapAuthLp: fapAuthLp, eorigin: eorigin, dcsuri: dcsuri, bgcfapauri: bgcfapauri};
    };
    
    // First Advantage Modal iFrame call - View current Chain of Custody
    this.launchFAModalPortal = function(){
		if (typeof faportal === 'undefined' || typeof faportal !== 'object' || faportal == null || faportal.closed) {
			if (typeof faauth === 'undefined' || faauth === false){
				
				$.blockUI({
	    			message : $('#loading')
	    		});
				
				envVars = checkEnv();
		    	fapl = envVars.fapl;
		    	fapAuthLp = envVars.fapAuthLp;
			    faportal = window.open(fapAuthLp,"auth","Height=1px,Width=1px,menubar=no,resizable=no,scrollbars=no,status=no,titlebar=no,toolbar=no,top=100000000");

			 // Polling to check when window is closed
			    var interval = setInterval(function() {
			    	if (faportal.closed){
			            clearInterval(interval);
					    faauth = true;
					    launchFAModal();
					    $.unblockUI();
			        }    
			    }, 100);
			    
			} else if (faauth = true){
				launchFAModal();
			}
//NewWindowMethod    		faportal = window.open("","FAPortlet");
//NewWindowMethod			faportal.document.write("<html><body><center><p>First Advantage Portlet is now loading...</p><br><img src='assets/images/loadingAnimation.gif'></center></body></html>");

		}
		
//    	launchFAModal();
    }

    var checkDTOrderVal = function(fapl){
        if($("#orderNbrDT").val()!=""){
        	setTimeout(function(){
        		$.unblockUI();
//NewWindowMethod      		faportal.location = fapl;
//NewWindowMethod      		faportal.focus();	
        		document.getElementById("showDTPortal").src=(fapl+"&orderid="+$("#orderNbrDT").val());
        		$('#FAModal').modal('show');
        	},1000);
        }
        else {
            setTimeout(function(){
            	alert("This is taking some time. Let's try again");
            	checkDTOrderVal(fapl);
            },1000); // check again in a second
        }
    }
    
    function launchFAModal() {
    	envVars = checkEnv();
    	fapl = envVars.fapl;
    	eorigin = envVars.eorigin;
        var statusmessageFA = "";
   	
    	$(window).on('message onmessage', function(e) {
            if (e.originalEvent.origin === eorigin) {
                statusmessageFA=e.originalEvent.data;
                if ($("#orderCompletedTsDT").val() =="" || $("#orderCompletedTsDT").val()=="Invalid Date Invalid Date" || $("#orderCompletedTsDT").val()=="Not completed") {
                    $("#orderCompletedTsDT").val("Not completed");
                    $("#orderCompletedTsDT").css("color", "orangered");
                }
                
                if ((statusmessageFA.indexOf("Collection Complete and Closed") > -1) || (statusmessageFA.indexOf("Collection Stopped") > -1) ){
//                if ((statusmessageFA.message.indexOf("Collection Complete and Closed") > -1) || (statusmessageFA.message.indexOf("Collection Stopped") > -1) ){
     	    		$('#FAModal').modal('hide');
//NewWindowMethod	    		    faportal.close();
       	    		document.getElementById("showDTPortal").src=eorigin+"/SecureLanding/";
                } else if (statusmessageFA.indexOf("Collection Complete") > -1){
//                } else if (statusmessageFA.message.indexOf("Collection Complete") > -1){
       	    		postDTOrderCompTS();
                    var dateDTOC = new Date();
                    var getTime = dateDTOC.toLocaleTimeString(e.originalEvent.timeStamp);
                    var remSec = getTime.slice(0, getTime.lastIndexOf(":"))+" "+getTime.slice(getTime.lastIndexOf(":")+4);
                    //           			dateDTOC = (dateDTOC.toLocaleDateString(e.originalEvent.timeStamp) + " " + dateDTOC.toLocaleTimeString(e.originalEvent.timeStamp).replace(/:(\d{2}) (?=[AP]M)/, " ") );
                    dateDTOC = (dateDTOC.toLocaleDateString(e.originalEvent.timeStamp) + " " + remSec );
                    $("#orderCompletedTsDT").val(dateDTOC);
                    $("#orderCompletedTsDT").css("color", "green");
       	    	} else {
//       	    			this.blockFormElements(".bgcOrderDet");	
       	    	}
            }
        });
    	checkDTOrderVal(fapl);
    	$("#drugTestAccessTProfile").css("display", "inherit");
    	$("#VCCOC_Btn").css("display", "inherit");
    	if ($("#orderNbrDT").val() != "" && $("#orderCompletedTsDT").val() !="" && $("#orderCompletedTsDT").val() !="Not completed") {
    		$(".bgcOrderDet").unblock();
    		$(".bgcFAPA").unblock();
    		CONSTANTS.interviewResultsObj.setBgcFAPAPanel();
    	}
    };

    //Make Service Call to Post DT Completed Time Stamp
    function postDTOrderCompTS() {
    		$.blockUI({
    			message : $('#updating')
    		});
    $.ajax({
		type : "POST",
		url: "/RetailStaffing/service/RetailStaffingService/updateCandidateDTCompTS/",
		timeout: 15000 , 
		dataType : "json",
		contentType : "application/json",
		data : {EMPLT_APLCNT_ID: CONSTANTS.CandidateIntvwDtls.candId, EMPLT_REQN_NBR: CONSTANTS.CandidateIntvwDtls.reqNbr, DTEST_ORD_NBR: $("#orderNbrDT").val() },
		success : bgcPanelReady,
		failure : callFailedFA
	}); // End - $.ajaxInitiateRequestFA
    }
    
    function bgcPanelReady(json) {
    	if ($("#orderNbrDT").val() != "" && $("#orderCompletedTsDT").val() !="" && $("#orderCompletedTsDT").val() !="Not Completed") {
    		$.unblockUI();
    		$(".bgcOrderDet").unblock();
    		$(".bgcFAPA").unblock();
    	}
    }
    
    //Make Service Call for DT Order Number
    function getDTOrderNbr(dtestType) {
//    	if ($("#orderNbrDT").val() == CONSTANTS.CandidateDrugTestInfo.orderNbrDTDB && isInitDTFlag != true){
    	if (isInitDTFlag != true){
    		displayDTPanelFromDBInfo(dtestType);
    	}
    	else {
    		if ($('#initiating').is(':hidden')){
    			$.blockUI({
        			message : $('#initiating')
        		});
			}
    		isInitDTFlag = false;
    		$("#drugTestInitDT").removeAttr("disabled");
    		$("#orderNbrDT").val("");
    $.ajax({
		type : "POST",
		url: "/RetailStaffing/service/DrugTestDataService/getOrderNbr",
		timeout: 15000 , 
		dataType : "json",
		contentType : "application/json",
		data : {applicantId: CONSTANTS.CandidateIntvwDtls.candId, requisitionId: CONSTANTS.CandidateIntvwDtls.reqNbr, testType: dtestType, testReasonType: "PRE", requesterLdap: CONSTANTS.userProfile.userId},
		success : displayDTButton,
		failure : callFailedFA
	}); // End - $.ajaxInitiateRequestFA
    	}
    }
    
   	function displayDTButton(json) {
   		if (json.hasOwnProperty("success"))  //drug_test_api_services_data_response
   				// && json.Response.status === CONSTANTS.STATUS_SUCCESS)
   		{
   			$("#orderNbrDT").val(json.orderNumber);
   			$("#ApplEmailDT").val(json.emailaddress);
   			$("#RequesterEmailDT").val(json.requesterEmailAddress);
   			$("#errorMsgDT").val(json.message);
   			
   			var dateDT = new Date(json.orderInitiatedTs);
//   			dateDT = (dateDT.toLocaleDateString() + " " + dateDT.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}).replace(/:(\d{2}) (?=[AP]M)/, " ") );
   			dateDT = (dateDT.toLocaleDateString() + " " + dateDT.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'}) );
   			$("#orderInitiatedTsDT").val(dateDT);
   			$("#orderCompletedTsDT").val("Not Completed");
   			$("#orderCompletedTsDT").css("color", "orangered");
   			
   			if (json.success) {
   		   		if (dtestType === "CONV" && $("#orderNbrDT").val()!== "") {
   		   			$("#drugTestAccessTProfile").css("display", "none");
   		   			$("#drugTestMsg").css("display", "inherit");
   		   		    $("#orderNbrDT").css("display", "inherit");
   		   			$("#drugTestConvMsg").css("display", "inherit");
   		   			$.unblockUI();
   		   			$(".bgcOrderDet").unblock();
   		   			$(".bgcFAPA").unblock();
   		   			CONSTANTS.interviewResultsObj.setBgcFAPAPanel();
   		   		} else if (dtestType === "ORAL" && $("#orderNbrDT").val()!== ""){
   		   			$("#drugTestConvMsg").css("display", "none");
   		   			$("#drugTestMsg").css("display", "none"); //hide initdate and order number
//    		   		$("#drugTestMsg").css("display", "inherit");
//   		   			$("#orderNbrDT").css("display", "inherit");
   		   			/*$("#drugTestAccessTProfile").css("display", "inherit");*/
//   		   			$.unblockUI();
   		   		launchFAModal();
   		   		} else {
   		   			$.unblockUI();
    		   		$('#FAModal').modal('hide');
//NewWindowMethod					faportal.close();
   		   			errorMessageDT = (json.message+"... an error has occurred retrieving an order. Please try again later.")
   	   	   	   	   	jAlert(errorMessageDT, '');
   		   			$.unblockUI();
   		   		}
   			} else {
   	   	   		$("#orderNbrDT").css("display", "none");
   	   	   		$("#drugTestAccessTProfile").css("display", "none");
   	   	   		$("#drugTestMsg").css("display", "none");
   	   	   		$("#drugTestConvMsg").css("display", "none");
   	   	   		$.unblockUI();
   	   	   		$('#FAModal').modal('hide');
//NewWindowMethod   	   	   		faportal.close();
   	   	   		errorMessageDT = (json.message+"... an error has occurred retrieving an order. Please correct the issue where possible, and try again later.")
	   	   	   	jAlert(errorMessageDT, '');
   	   	   		$.unblockUI();
   			}
   		}   		
   	}

    function callFailedFA(jqXHR, textStatus) {
    	if(textStatus === 'timeout')
        {     
    		$('#FAModal').modal('hide');
//NewWindowMethod    		faportal.close();
    		jAlert('Failed from timeout, please try again');
        } else {
        	$.unblockUI();
        	$('#FAModal').modal('hide');
//NewWindowMethod        	faportal.close();
    		jAlert("Application Failed to send request to First Advantage. Please try again later.");
    		$.unblockUI();
    	}
	}

	function displayDTPanelFromDBInfo(dtestType) {
		if (dtestType === "CONV" && $("#orderNbrDT").val() !== "") {
			$("#drugTestAccessTProfile").css("display", "none");
			$("#drugTestMsg").css("display", "inherit");
			$("#orderNbrDT").css("display", "inherit");
			$("#drugTestConvMsg").css("display", "inherit");
			$.unblockUI();
			$(".bgcOrderDet").unblock();
			$(".bgcFAPA").unblock();
		} else if (dtestType === "ORAL" && $("#orderNbrDT").val() !== "") {
			$("#drugTestConvMsg").css("display", "none");
			$("#drugTestMsg").css("display", "none"); //hide initdate and order number
//			$("#drugTestMsg").css("display", "inherit");
//			$("#orderNbrDT").css("display", "inherit");
			$("#drugTestAccessTProfile").css("display", "inherit");
			$("#VCCOC_Btn").css("display", "inherit");
			var linkExpDate = new Date();
			var daysTillExp = 10;
			linkExpDate.setDate(linkExpDate.getDate()-daysTillExp);
			var mm = linkExpDate.getMonth();
			var dd = linkExpDate.getDate();
			var yyyy = linkExpDate.getFullYear();
			var formatLinkExpDate = new Date(yyyy, mm, dd, 00, 00, 00);
			
			var date_TsDT = $("#orderInitiatedTsDT").val().toString().match(/\d+/g);
			var year_TsDT = date_TsDT[2];
			var month_TsDT = date_TsDT[0]-1;
			var day_TsDT = date_TsDT[1];
			var hr_TsDT = date_TsDT[3];
			var min_TsDT = date_TsDT[4];
			var formatTsDT = new Date(year_TsDT, month_TsDT, day_TsDT, hr_TsDT, min_TsDT, 00);
			var isExpired = formatTsDT < formatLinkExpDate;

			if(isInitDTFlag == false && isExpired) {
				$("#VCCOC_Btn").css("display", "none");
			} else {
				$("#VCCOC_Btn").css("display", "inherit");
			}
			$.unblockUI();
			$(".bgcOrderDet").unblock();
			$(".bgcFAPA").unblock();
		}
	}
	
 // BGC Panel Re-initiate No and Yes Buttons
	this.warningNOCancelBGCReInit = function() {
        $("#BGCReInitWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#bgcFAPA").removeClass("blurBody");
        });
        $('#BGCReInitWarnModal').modal('hide');
        $("#bgcFAPAInitBtn").removeAttr("disabled");
    };
	
    this.warningYESAcceptBGCReInit = function() {
        $("#BGCReInitWarnModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $("#bgcFAPA").removeClass("blurBody");
        });
        $('#BGCReInitWarnModal').modal('hide');
        this.initiateBackgroundCheckConsent();
    };

    this.checkInitStatus = function() {
    	if ($("#bgcFAPAStatus").val()=="Not completed"){
    		BGCReInitWarning();
		} else {
			this.initiateBackgroundCheckConsent();
		}
    };
    
 // BGC Initiate
	this.initiateBackgroundCheckConsent = function() {
		$.blockUI({
			message : $('#initiating')
		});
        var saveObj = {}; // InterviewResultsSaveVO
        saveObj.userId = CONSTANTS.userProfile.userId;
        // Set Applicant Type
        CONSTANTS.CandidateIntvwDtls.applicantType = CONSTANTS.interviewResultsObj.applicantType;
        // Set Associate ID
        saveObj.aid = CONSTANTS.interviewResultsObj.selectedCandidate.aid;
        //Candidate Reference Number
        saveObj.candRefNbr = CONSTANTS.interviewResultsObj.selectedCandidate.candRefNbr;
        // Set Candidate Name
        saveObj.candidateName = CONSTANTS.interviewResultsObj.selectedCandidate.name;

        saveObj.backgroundConsent = $("input[name='isSignCmpltd']:checked").val() ? $("input[name='isSignCmpltd']:checked").val().toString() : "";
        
        this.submitBackgroundCheckConsentData(saveObj);
    };
    
    this.submitBackgroundCheckConsentData = function(saveObj) {
    	// Check if BGC request is reinitiated
    	if ($("#bgcFAPAStatus").val()=="Not completed") {
    		CONSTANTS.BackgroundCheckDtls.reinitiateBgcConsent = true;
    		}
    	
    	CONSTANTS.submitBgcCandData = {
    			"SubmitBgcCandidateDtlsRequest" : {
    				"CandidateInformation" : {
    					"applicantType" : CONSTANTS.CandidateIntvwDtls.applicantType,
    					"humanResourcesSystemStoreNumber" : CONSTANTS.CandidateIntvwDtls.strNbr,
    					"employmentRequisitionNumber" : CONSTANTS.CandidateIntvwDtls.reqNbr,
    					"employmentPositionCandidateId" : CONSTANTS.CandidateIntvwDtls.candId,
    					"associateEmailForBgcConsent" : $("#emailEntry2").val().toString()
                    	},
                    "BackgroundCheckInfo" : {
                    	"hasSignedConsent" : CONSTANTS.BackgroundCheckDtls.hasSignedConsent,
                    	"isDrivingPos" : CONSTANTS.BackgroundCheckDtls.isDrivingPos,
                    	"isAlreadyInDrivingPos" : CONSTANTS.BackgroundCheckDtls.isAlreadyInDrivingPos,
                    	"needBackgroundCheck" : CONSTANTS.BackgroundCheckDtls.needBackgroundCheck,
                    	"backgroundPackageNeeded" : CONSTANTS.BackgroundCheckDtls.backgroundPackageNeeded,
                    	"backgroundComponentsNeeded" : CONSTANTS.BackgroundCheckDtls.backgroundComponentsNeeded,
                    	"humanResourcesStoreTypeCode" : CONSTANTS.BackgroundCheckDtls.humanResourcesStoreTypeCode,
                    	"reinitiateBgcConsent" : CONSTANTS.BackgroundCheckDtls.reinitiateBgcConsent
                    	}
    			}
        };
         
    //    CONSTANTS.submitBgcCandData = this.setInterviewInformation(CONSTANTS.submitBgcCandData, saveObj);
        var data = {
                data : JSON.stringify({
                    "SubmitBgcCandidateDtlsRequest" : CONSTANTS.submitBgcCandData.SubmitBgcCandidateDtlsRequest
                })
            };
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.submitBackgroundConsentByCandidate);
        RSASERVICES.submitBackgroundConsentByCandidate(data, callbackFunction, true, "Please wait...");
    };
    
    this.submitBackgroundConsentByCandidate = function(json) {
        $.unblockUI();
        var bgcFAPALink = "";

        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.STATUS_SUCCESS) {
        	if (json.Response.hasOwnProperty("profileAdvantageLink")) {
        		var finalData = json.Response.profileAdvantageLink.replace(/\\/g, "");
        		bgcFAPALink = finalData;
        		isInitBgcFlag = true;
                CONSTANTS.interviewResultsObj.launchBgcFAPAPortal(bgcFAPALink);
        	} else {
        		CONSTANTS.interviewResultsObj.popup.alert("An error occured where a link to Profile Advantage was not received! Initiating a new background check consent cannot be performed for this person at this time. Please contact support for assistance.");
        	}
        }

        // Error checking... add friendly message with error object's error code
        if (json.Response.hasOwnProperty("error") && json.Response.error.hasOwnProperty("errorMsg")) {
            CONSTANTS.interviewResultsObj.popup.alert(json.Response.error.errorMsg);
//            CONSTANTS.interviewResultsObj.popup.alert("An error has occurred. Please contact support."+" ("+json.Response.error.errorCode+")");
        }
    };
   	
};
//# sourceURL=interviewResults.js

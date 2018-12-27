/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: retailStaffing.js
 * Application: Retail Staffing Admin
 *
 */
function retailStaffing() {
        /*
         * initializing the event for the change in the search type and making
         * the service call when the search type is of type work queue
         * requisition so as to fetch the drop down values also initializing the
         * key press and the key up events for the various text boxes so as to
         * restrict the user for enter the invalid values in the text box and
         * display in the corresponding drop downs and making the service calls
         * to get user profile details to check QA pilot status and to get
         * Stores Driver License Exempt
         */
    this.initialize = function () {
        var that = this;
        this.popup = new rsaPopup();
        $("#goHome").hide();
        $("#goHome").unbind();
        CONSTANTS.booleanTrue = true;
        this.setMarginTopPopUp();
        $(".navCloseBtn").off("click");
        $(".navCloseBtn").on("click",function(){
            window.close();
        });
        $("title").text("Retail Staffing Administration");
        CONSTANTS.retailStaffingObj = this;
        CONSTANTS.retailStaffingObj.allServiceFinish = 0;
        $(".firstNavButton").hide();
        $("#messageBar").html("Search for a Requisition, Candidate, Phone Screen Number, Summary");
        $('.modal-backdrop').empty().remove();
        $('#ui-datepicker-div').hide();
        $(".btn-green").attr("disabled","disabled");
        $("#retailStaffing").off("keypress","input[type='text']:focus");
        $("#retailStaffing").on("keypress","input[type='text']:focus",function(e){
            if(e.keyCode === 13)
                {
                that.submitButtonClicked();
                }
        });
        $("input[type='radio'][name='searchType']").off('change');
        $("input[type='radio'][name='searchType']").on('change',function(){
            CONSTANTS.retailStaffingObj.fromGrid = "";
            that.resetAllValues();
            $(".searchType").hide();
            $('#currentGridTitle').html("");
            $("#paginationBtnGrp").empty().remove();
            $("[class *= '"+$(this).attr('id')+"']").show();
            var selectedRadioBtn = $(this).attr('id');
            if(selectedRadioBtn === 'candidate')
                {
                $("#candidateRefNumber").click();
                }
            else if(selectedRadioBtn === 'workQueuesResquistions')
                {
                $(".btn-green").attr("disabled","disabled");
                $(".WorkQueuesSearchType").hide();
                var prevSelectedRadio = $("input[type='radio'][name='WorkQueuesSearchType']:checked").attr("id");
                $(".workQueuesResquistions.searchType input").val("");
                $("input[type='radio'][name='WorkQueuesSearchType']").attr("checked",false);
                $(".workQueuesResquistions.searchType select option:nth-child(1)").attr("selected","selcted");
                if(prevSelectedRadio) {
                    $("[id='"+prevSelectedRadio+"']").click();
                $("#WorkQueuesProcess").attr("disabled","disabled");
                $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
                }
                if(!CONSTANTS.WorkQueueslocaleDetails)
                    {
                     var callbackFunction = $.Callbacks('once');
                     callbackFunction.add(that.onFetchworkQueuesDetailsSuccess);
                     RSASERVICES.getLocaleDetails(callbackFunction);
                    }
                else if(CONSTANTS.WorkQueueslocaleDetails)
                    {
                    that.onFetchworkQueuesDetailsSuccess(CONSTANTS.WorkQueueslocaleDetails);
                    }
                }
            that.initializeEvents(that);
            $(".gridContainer").empty().addClass("custom-grid");
        });
        $("#requisitionNumber").click();
        $(".allowOnlyNumbers").off("keypress");
        $(".allowOnlyNumbers").on("keypress",function(e){
            that.allowOnlyNumbers(e);
        });
        $(".allowOnlyAlphabets").off("keypress");
        $(".allowOnlyAlphabets").on("keypress",function(e){
            that.allowOnlyAlphabets(e);
            $(".btn-green").removeAttr("disabled");
        });
        $(".allowOnlyNumbers").off('paste');
        $(".allowOnlyNumbers").on('paste', function(e) {
            setTimeout(function(){
                var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
                $(".btn-green").removeAttr("disabled");
            },100);
        });
        $(".allowOnlyAlphabets").off('paste');
        $(".allowOnlyAlphabets").on('paste', function(e) {
            setTimeout(function(){
                var tempNum1 = $(e.currentTarget).val().match(/[a-zA-Z '-]+/g);
                $(e.currentTarget).val(((tempNum1) ? (tempNum1.join("")) : ""));
                $(".btn-green").removeAttr("disabled");
            },100);
        });
        $(".allowOnlyNumbers").off("keyup");
        $(".allowOnlyNumbers").on("keyup",function(e){
            that.enableDisableSearchButtonOnKeyUp(e);
        });
        that.checkRetainAndCallService();
    };
    this.setMarginTopPopUp = function () {
        var tempMargintp = parseInt((($(window).height()/2 - $("#warningpopup .modal-content").outerHeight()/2)));
        var tempMargintp1 = tempMargintp.toString() + "px";
        $("#warningpopup .modal-content").css({"margin-top":tempMargintp1});
        $(window).unbind();
        $(window).resize(function() {
            var tempMargintop = parseInt((($(window).height()/2 - $("#warningpopup .modal-content").outerHeight()/2)));
            var tempMargintop1 = tempMargintop.toString() + "px";
            $("#warningpopup .modal-content").css({"margin-top":tempMargintop1});
        });
    };
        /*
         * This method is used to verify if there is necessity to make the
         * service call once the service call is been made the data is being
         * stored and is used for the further operations and if the data is
         * cleared then the service call is being made once again
         */
    this.checkRetainAndCallService = function () {
        var that = this;
        if(CONSTANTS.userProfileDetailsInform && !$.isEmptyObject(CONSTANTS.userProfileDetailsInform)){
            that.successUserProfile(CONSTANTS.userProfileDetailsInform);
        }
        else {
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(that.successUserProfile);
            RSASERVICES.getUserProfileDetailss(" ",callbackFunction, false, true, "Please wait...");
        }
        if(CONSTANTS.checkQpPilotStatusResponse && !$.isEmptyObject(CONSTANTS.checkQpPilotStatusResponse)){
            that.successcheckQpPilotStatus(CONSTANTS.checkQpPilotStatusResponse);
        }
        else {
            var callbackFunction1 = $.Callbacks('once');
            callbackFunction1.add(that.successcheckQpPilotStatus);
            RSASERVICES.checkQpPilotStatus(callbackFunction1);
        }
        if(CONSTANTS.driverLicenseExemptResponse && !$.isEmptyObject(CONSTANTS.driverLicenseExemptResponse)){
            that.successDriverLicenseExempt(CONSTANTS.driverLicenseExemptResponse);
        }
        else {
            var callbackFunction2 = $.Callbacks('once');
            callbackFunction2.add(that.successDriverLicenseExempt);
            RSASERVICES.getStoresDriverLicenseExempt(callbackFunction2);
        }
        if(CONSTANTS.resetHomeValue && !$.isEmptyObject(CONSTANTS.resetHomeValue)){
            that.setCurrentStateInfo();
        }
    };
        /*
         * This function is used for the navigating the user to the phone screen
         * details screen on giving the details directly in the URl
         */
    this.navigateToPhoneScreenDetailsURl = function (e) {
        CONSTANTS.LoadITIDGDltls = [{
            "itiNbr":e
        }];
        $.get('app/RSAPhoneScreenDetails/view/phoneScreenDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
    /*
     * This function is used for the navigating the user to the interview screen
     * details screen on giving the details directly in the URl
     */
    this.navigateToInterviewScreenDetailsURL = function (e) {
        CONSTANTS.LoadINTVIEWSRCHRSLTS = [{
            "itiNbr":e
        }];
        $.get('app/RSAInterviewScreenDetails/view/interviewScreenDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
    
    this.showSuccessMessage = function () {
    	if (CONSTANTS.SUCCESS_ON_HOME) {
            CONSTANTS.SUCCESS_ON_HOME = false;
            $("#reqdetnav").removeClass("blurBody");
            this.popup.alert("Details Saved Successfully");
        }
    }
    
        /*
         * This method is used to decide whether the user needs to be redirected
         * to another screen or need to load the default screen based upon the
         * URl
         */
    this.loadViewStack = function ()
    {
        $.unblockUI();
        // remove the QS from URL.
        if(CONSTANTS.removeQSFromURL)
		{
        	CONSTANTS.removeQSFromURL = false;
        	CONSTANTS.retailStaffingObj.showSuccessMessage();
        	UTILITY.RefreshURL();			
		}
        CONSTANTS.appController = {};
        var qs = CONSTANTS.retailStaffingObj.returnUrlObject();
        CONSTANTS.ConfirmationNo = 0;
        CONSTANTS.CallType ="";
        CONSTANTS.isEnableIntrwScrnWarnPop = false;
        CONSTANTS.isEnablePhnScrnWarnPop = false;
        CONSTANTS.ConfirmationNo = qs.Conf_Num ? qs.Conf_Num : 0;
        CONSTANTS.CallType = qs.Call_Type? qs.Call_Type.trim() : "";
        // reset the URL, No need of URL any more.
        UTILITY.RefreshURL();
          if( (CONSTANTS.ConfirmationNo > 0) && (CONSTANTS.CallType.length > 0) ){
              if( (CONSTANTS.CallType !== "") && (CONSTANTS.CallType === "PHNSCRN" ) )
			  {
			      CONSTANTS.isEnablePhnScrnWarnPop = true;
			    $("#messageBar").html("PHONE SCREEN DETAILS");
			    CONSTANTS.retailStaffingObj.navigateToPhoneScreenDetailsURl(CONSTANTS.ConfirmationNo);
			  }
              else if( (CONSTANTS.CallType !== "") && (CONSTANTS.CallType === "INTSCHED" ) )
				{
				    CONSTANTS.isEnableIntrwScrnWarnPop = true;
				     $("#messageBar").html("INTERVIEW SCREEN DETAILS");
				     CONSTANTS.retailStaffingObj.navigateToInterviewScreenDetailsURL(CONSTANTS.ConfirmationNo);
				}
            else{
                 $("#messageBar").html("Search for a Requisition, Candidate, Phone Screen Number, Summary");
            }
         }
         else{
             $("#messageBar").html("Search for a Requisition, Candidate, Phone Screen Number, Summary");
         }
    };
    /*This method is used to form the object from the URL as required*/
    this.returnUrlObject = function () {
        var params1 = {};
        var queryString = window.location.search.substring(1);
        if(queryString)
        {
            var params = queryString.split('&');
            var length = params.length;
            var index = -1;
            for (var i=0; i<length; i++)
            {
                var kvPair = params[i];
                if((index = kvPair.indexOf("=")) > 0)
                {
                    var key = kvPair.substring(0,index);
                    var value = kvPair.substring(index+1);
                    params1[key] = value;
                }
            }
        }
        return params1;
    };
        /*
         * On the success of the check QA Pilot Status this method is being
         * called where the success and the error response from the service is
         * being handled
         */
    this.successcheckQpPilotStatus = function (response) {
        if(response && response.Response.status === "SUCCESS" && response.Response.showQpButton)
            {
            CONSTANTS.checkQpPilotStatusResponse = response;
            CONSTANTS.isQPButtonVisible = response.Response.showQpButton;
            CONSTANTS.retailStaffingObj.allServiceFinish = CONSTANTS.retailStaffingObj.allServiceFinish + 1;
            if(CONSTANTS.retailStaffingObj.allServiceFinish === 3)
                {
                CONSTANTS.retailStaffingObj.loadViewStack();
                }
            }
    	else if(response.Response.error)
        {
            $.unblockUI();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
        }
    	else
    	{
    		// unblocks the UI, if the user is not having the access to Qualified Pool in pilot mode.
    		$.unblockUI();
    		CONSTANTS.retailStaffingObj.loadViewStack();
    	}
    };
        /*
         * On the success of the Driver License Exempt this method is being
         * called where the success and the error response from the service is
         * being handled In the case of the success the various store numbers
         * available in the drivers license exempt list and to push the same in
         * to the list so as to use at later point of time
         */
    this.successDriverLicenseExempt = function (response) {
        var resultList = [];
        CONSTANTS.storeDriversLicenseExemptList = [];
        if(response && response.Response.status === "SUCCESS" && response.Response.StoreDriversLicenseExemptList && response.Response.StoreDriversLicenseExemptList.StoreDetails)
            {
            CONSTANTS.driverLicenseExemptResponse = response;
            if(response.Response.StoreDriversLicenseExemptList.StoreDetails.constructor === Array)
                {
                resultList = response.Response.StoreDriversLicenseExemptList.StoreDetails;
                }
            else
                {
                resultList.push(response.Response.StoreDriversLicenseExemptList.StoreDetails);
                }
            if(resultList.length > 0)
                {
                for(var i=0;i<resultList.length;i++){
                CONSTANTS.storeDriversLicenseExemptList.push(resultList[i].strNum);
                }
                }
            CONSTANTS.retailStaffingObj.allServiceFinish = CONSTANTS.retailStaffingObj.allServiceFinish + 1;
            if(CONSTANTS.retailStaffingObj.allServiceFinish === 3)
                {
                CONSTANTS.retailStaffingObj.loadViewStack();
                }
            }
        else if(response.Response.error)
            {
            $.unblockUI();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        else
        	{
        	// unblocks the UI, if the user is not having the access to Qualified Pool in pilot mode.
        	$.unblockUI();
        	}
    };
    /*
     * On the success of the get User Profile this method is being
     * called where the success and the error response from the service is
     * being handled
     */
    this.successUserProfile = function(response) {
        CONSTANTS.userProfile = {};
        CONSTANTS.userProfileDetailsInform = {};
        if(response && response.UserProfileResponseDTO && response.UserProfileResponseDTO.ErrorCd===0)
            {
	            CONSTANTS.userProfileDetailsInform = response;
	            CONSTANTS.userProfile.userId = response.UserProfileResponseDTO.userId;
	            $('.navbar-right li:nth-child(2)').text("Welcome "+response.UserProfileResponseDTO.firstName+" "+response.UserProfileResponseDTO.lastName);
	            CONSTANTS.retailStaffingObj.allServiceFinish = CONSTANTS.retailStaffingObj.allServiceFinish + 1;
	            if(CONSTANTS.retailStaffingObj.allServiceFinish === 3)
	                {
	                	CONSTANTS.retailStaffingObj.loadViewStack();
	                }
	            $.unblockUI();
            }
        else
            {
	            $.unblockUI();
	            CONSTANTS.retailStaffingObj.popup.alert("Unable to get your user profile information. Please try again later.");
            }
    };
            /*
             * This method is used for the purpose of initialization of the
             * event that needs to be triggered in the change of the radio
             * button in the candidate search type and on the change of the work
             * queue search type and the event for the submit button click and
             * the drop down change events this method is also used to set the
             * grid titles as required based on the search type
             */
    this.initializeEvents = function (that) {
        $("input[type='radio'][name='CandidateSearchType']").off('change');
        $("input[type='radio'][name='CandidateSearchType']").on('change',function(){
            $(".CandidateSearchType").hide();
            $("[class *= '"+$(this).attr('id')+"']").show();
        });
        $("input[type='radio'][name='WorkQueuesSearchType']").off('change');
        $("input[type='radio'][name='WorkQueuesSearchType']").on('change',function(){
            if($("#WorkQueuesProcess option").length === 0) {
            $("#WorkQueuesProcess").empty().append(CONSTANTS.retailStaffingObj.WorkQueuesProcessValue);
            $("#WorkQueuesProcess").attr("disabled","disabled");
            $("#WorkQueuesProcess").selectBoxIt();
            }
            else
                {
                 $("#WorkQueuesProcess").attr("disabled","disabled");
                 $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
                }
            $(".gridContainer").empty();
            $("#currentGridTitle").html("");
            $("#paginationBtnGrp").empty().remove();
            $(".WorkQueuesProcessSubmit").attr("disabled","disabled");
            $(".WorkQueuesSearchType").hide();
            $("#WorkQueuesStoreValue").val("");
            $("#WorkQueuesDistrictValue option:nth-child(1)").attr("selected","selected");
            $("#WorkQueuesRegionValue option:nth-child(1)").attr("selected","selected");
            $("#WorkQueuesDivisionValue option:nth-child(1)").attr("selected","selected");
            $("#WorkQueuesProcess option:nth-child(1)").attr("selected","selected");
            $("#WorkQueuesDistrictValue").data("selectBox-selectBoxIt").refresh();
            $("#WorkQueuesRegionValue").data("selectBox-selectBoxIt").refresh();
            $("#WorkQueuesDivisionValue").data("selectBox-selectBoxIt").refresh();
            $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
            $(".locationValue").off("change");
            $(".locationValue").on("change",function(e){
                $("#"+$(this).attr("id")).data("selectBox-selectBoxIt").refresh();
                if($(this).val()!=="0")
                {
                $("#WorkQueuesProcess").removeAttr("disabled");
                $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
                that.enableProcessSubmitButton(e);
                }
                else
                    {
                    $("#WorkQueuesProcess").attr("disabled","disabled");
                    $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
                    }
            });
            $("#WorkQueuesProcess").off("change");
            $("#WorkQueuesProcess").on("change",function(e){
                $(this).data("selectBox-selectBoxIt").refresh();
                that.enableProcessSubmitButton(e);
            });
            $("[class *= '"+$(this).attr('id')+"']").show();
            var selectedRadioBtn = $(this).attr('id');
            if(selectedRadioBtn === 'WorkQueuesStore')
                {
                $(".WorkQueuesTitleLable").html("Please Enter Store Number");
                }
            else if(selectedRadioBtn === 'WorkQueuesDistrict')
                {
                $(".WorkQueuesTitleLable").html("Please Select District Number");
                }
            else if(selectedRadioBtn === 'WorkQueuesRegion')
                {
                $(".WorkQueuesTitleLable").html("Please Select Region Number");
                }
            else if(selectedRadioBtn === 'WorkQueuesDivision')
                {
                $(".WorkQueuesTitleLable").html("Please Select Division Number");
                }
        });
        $(".btn.btn-green").off("click");
        $(".btn.btn-green").on("click",function()
        {
            that.submitButtonClicked();
        });
    };
        /*
         * This method is being invoked on the click of the ok button or on the
         * press of the enter key
         */
    this.submitButtonClicked = function() {
        CONSTANTS.retailStaffingObj.currentGridData = {};
        CONSTANTS.retailStaffingObj.pageCounter = 0;
        CONSTANTS.retailStaffingObj.isForwardFlag = true;
        CONSTANTS.retailStaffingObj.isFirstHit = true;
        CONSTANTS.retailStaffingObj.WorkQueuesDistrictValue = $("#WorkQueuesDistrictValue").val();
        CONSTANTS.retailStaffingObj.WorkQueuesRegionValue = $("#WorkQueuesRegionValue").val();
        CONSTANTS.retailStaffingObj.WorkQueuesDivisionValue = $("#WorkQueuesDivisionValue").val();
        CONSTANTS.retailStaffingObj.WorkQueuesProcess = $("#WorkQueuesProcess").val();
        CONSTANTS.retailStaffingObj.onSubmitButtonClick();
    };
        /*
         * This function is used to figure out if the submit button is to be
         * enabled or not
         */
    this.enableProcessSubmitButton = function (e) {
    if(($("#WorkQueuesStoreValue").val().length === 4 || $(e.currentTarget).val() !== "0")&& $("#WorkQueuesProcess").val() !== "0")
        {
            $(".WorkQueuesProcessSubmit").removeAttr("disabled");
            $(".WorkQueuesProcessSubmit").css({"color": "yellow","border":"1px solid orangered"});
            setTimeout(function() {
                $(".WorkQueuesProcessSubmit").focus();
            }, 400);
        }
        else{
            $(".WorkQueuesProcessSubmit").attr("disabled","disabled");
            $(".WorkQueuesProcessSubmit").css({"color": "#c3c3c3","border": "none"});
        }
};
    /*
     * This method is used to restrict the users from entering other special
     * characters other than alphabets
     */
this.allowOnlyAlphabets = function (e) {
    var booleanVariable = ((e.keyCode === 39) || (e.keyCode === 45) || (e.keyCode >= 97 && e.keyCode <= 122));
        if(!((e.keyCode === 32) || booleanVariable || (e.keyCode >= 65 && e.keyCode <= 90))) {
            e.preventDefault();
        }
};
    /* This method is used to reset all in the page */
this.resetAllValues = function () {
    $("#requisitionValue").val("");
    $("#candidateNumberValue").val("");
    $("#candidateSSNValue").val("");
    $("#candidateLastNameValue").val("");
    $("#phoneScreenValue").val("");
    $("#interviewScreenValue").val("");
    $("#WorkQueuesStoreValue").val("");
};
    /*
     * This function is used to restrict the user from enters other characters
     * apart from numbers
     */
    this.allowOnlyNumbers = function (e) {
         if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57))) {
                e.preventDefault();
            }
         else
             {
        	 if(e.currentTarget.id !== "WorkQueuesStoreValue")
        		 {
             this.enableDisableSearchButtonOnKeyPress(e);
        		 }
             }
    };
        /* This is used to enable the search button on the key up event */
    this.enableDisableSearchButtonOnKeyUp = function(e)
    {
        if(e.keyCode!==13 && e.currentTarget.id !== "WorkQueuesStoreValue"){
        $(".btn-green").removeAttr("disabled");
        }
    };
        /* This is used to enable the search button on the key press event */
    this.enableDisableSearchButtonOnKeyPress = function(e)
    {
        if(e.keyCode!==13){
        $(".btn-green").removeAttr("disabled");
        }
    };
        /*
         * This function is triggered when the details are being fetched
         * successfully from the service call and is used to display the data in
         * the grid
         */
    this.onFetchworkQueuesDetailsSuccess = function (response1) {
        if(response1.Response){
        $.unblockUI();
        CONSTANTS.WorkQueueslocaleDetails = response1;
        var response = CONSTANTS.WorkQueueslocaleDetails.Response;
        var WorkQueuesDistrictValue="<option value='0'>Please Select a Location</option>";
        var WorkQueuesRegionValue = "<option value='0'>Please Select a Location</option>";
        var WorkQueuesDivisionValue = "<option value='0'>Please Select a Location</option>";
        var WorkQueuesProcess = "<option value='0'>Please Select a Process</option>";
        WorkQueuesDistrictValue = CONSTANTS.retailStaffingObj.buildWorkQueuesDistrictValue(WorkQueuesDistrictValue,response);
        WorkQueuesRegionValue = CONSTANTS.retailStaffingObj.buildWorkQueuesRegionValue(WorkQueuesRegionValue,response);
        WorkQueuesDivisionValue = CONSTANTS.retailStaffingObj.buildWorkQueuesDivisionValue(WorkQueuesDivisionValue,response);
        WorkQueuesProcess = CONSTANTS.retailStaffingObj.buildWorkQueuesProcess(WorkQueuesProcess,response);
        $("#WorkQueuesDistrictValue,#WorkQueuesRegionValue,#WorkQueuesDivisionValue").empty();
        $("#WorkQueuesDistrictValue").append(WorkQueuesDistrictValue);
        $("#WorkQueuesRegionValue").append(WorkQueuesRegionValue);
        $("#WorkQueuesDivisionValue").append(WorkQueuesDivisionValue);
        $("#WorkQueuesDistrictValue,#WorkQueuesRegionValue,#WorkQueuesProcess,#WorkQueuesDivisionValue").selectBoxIt();
        $("#WorkQueuesProcess").attr("disabled","disabled");
        $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
        $("#WorkQueuesStoreValue").off("keypress");
        $("#WorkQueuesStoreValue").on("keypress",function(e) {
        	$("#WorkQueuesProcess").removeAttr("disabled","disabled");
            $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
        	CONSTANTS.retailStaffingObj.allowOnlyNumbers(e);
        });
        CONSTANTS.retailStaffingObj.WorkQueuesProcessValue = WorkQueuesProcess;
        }
    };
    /*This function is used to append the values in the Work Queues Process drop down*/
    this.buildWorkQueuesProcess = function (WorkQueuesProcess,response) {
        if(response.ProcessStatusList && response.ProcessStatusList.Status) {
            if(response.ProcessStatusList.Status.constructor === Array) {
                for(var l=0 ;l<response.ProcessStatusList.Status.length;l++)
                {
                    WorkQueuesProcess = WorkQueuesProcess+"<option value='"+response.ProcessStatusList.Status[l].statusDesc+"'>"+response.ProcessStatusList.Status[l].statusDesc+"</option>";
                }
            }
            else
                {
                WorkQueuesProcess = WorkQueuesProcess+"<option value='"+response.ProcessStatusList.Status.statusDesc+"'>"+response.ProcessStatusList.Status.statusDesc+"</option>";
                }
            return WorkQueuesProcess;
        }
    };
    /*This function is used to append the values in the Work Queues Region value drop down*/
    this.buildWorkQueuesRegionValue = function (WorkQueuesRegionValue,response) {
        if(response.RegionList && response.RegionList.OrgUnitDetailsList && response.RegionList.OrgUnitDetailsList[0].orgUnitDetail) {
            if(response.RegionList.OrgUnitDetailsList[0].orgUnitDetail.constructor === Array) {
                for(var i=0 ;i<response.RegionList.OrgUnitDetailsList[0].orgUnitDetail.length;i++)
                {
                    WorkQueuesRegionValue = WorkQueuesRegionValue+"<option value='"+response.RegionList.OrgUnitDetailsList[0].orgUnitDetail[i].code+"'>"+response.RegionList.OrgUnitDetailsList[0].orgUnitDetail[i].code+" - "+response.RegionList.OrgUnitDetailsList[0].orgUnitDetail[i].desciption+"</option>";
                }
            }
            else
                {
                WorkQueuesRegionValue =  WorkQueuesRegionValue+"<option value='"+response.RegionList.OrgUnitDetailsList[0].orgUnitDetail.code+"'>"+response.RegionList.OrgUnitDetailsList[0].orgUnitDetail.code+" - "+response.RegionList.OrgUnitDetailsList[0].orgUnitDetail.desciption+"</option>";
                }
            return WorkQueuesRegionValue;
        }
    };
    /*This function is used to append the values in the Work Queues district drop down*/
    this.buildWorkQueuesDistrictValue = function (WorkQueuesDistrictValue,response) {
        if(response.DistList && response.DistList.OrgUnitDetailsList && response.DistList.OrgUnitDetailsList[0].orgUnitDetail) {
            if(response.DistList.OrgUnitDetailsList[0].orgUnitDetail.constructor === Array) {
                for(var i=0 ;i<response.DistList.OrgUnitDetailsList[0].orgUnitDetail.length;i++)
                {
                    WorkQueuesDistrictValue = WorkQueuesDistrictValue+"<option value='"+response.DistList.OrgUnitDetailsList[0].orgUnitDetail[i].code+"'>"+response.DistList.OrgUnitDetailsList[0].orgUnitDetail[i].code+" - "+response.DistList.OrgUnitDetailsList[0].orgUnitDetail[i].desciption+"</option>";
                }
            }
            else
                {
                WorkQueuesDistrictValue =  WorkQueuesDistrictValue+"<option value='"+response.DistList.OrgUnitDetailsList[0].orgUnitDetail.code+"'>"+response.DistList.OrgUnitDetailsList[0].orgUnitDetail.code+" - "+response.DistList.OrgUnitDetailsList[0].orgUnitDetail.desciption+"</option>";
                }
            return WorkQueuesDistrictValue;
        }
    };
    /*This function is used to append the values in the Work Queues division drop down*/
    this.buildWorkQueuesDivisionValue = function (WorkQueuesDivisionValue,response) {
        if(response.DivList && response.DivList.OrgUnitDetailsList && response.DivList.OrgUnitDetailsList[0].orgUnitDetail) {
            if(response.DivList.OrgUnitDetailsList[0].orgUnitDetail.constructor === Array) {
                for(var i=0 ;i<response.DivList.OrgUnitDetailsList[0].orgUnitDetail.length;i++)
                {
                    WorkQueuesDivisionValue = WorkQueuesDivisionValue+"<option value='"+response.DivList.OrgUnitDetailsList[0].orgUnitDetail[i].code+"'>"+response.DivList.OrgUnitDetailsList[0].orgUnitDetail[i].code+" - "+response.DivList.OrgUnitDetailsList[0].orgUnitDetail[i].desciption+"</option>";
                }
            }
            else
                {
                WorkQueuesDivisionValue =  WorkQueuesDivisionValue+"<option value='"+response.DivList.OrgUnitDetailsList[0].orgUnitDetail.code+"'>"+response.DivList.OrgUnitDetailsList[0].orgUnitDetail.code+" - "+response.DivList.OrgUnitDetailsList[0].orgUnitDetail.desciption+"</option>";
                }
            return WorkQueuesDivisionValue;
        }
    };
        /* This is used to check the type of search */
     this.chkSearchType = function() {
            return (($("input[type='radio'][name='searchType']:checked").attr('id') === 'workQueuesResquistions') && ($("input[type='radio'][name='WorkQueuesSearchType']:checked").length > 0 ));
        };
        /*This function is triggered on the click of submit button*/
    this.onSubmitButtonClick = function ()
    {
        var that = this;
        if($("input[type='radio'][name='searchType']:checked").attr('id') !== 'workQueuesResquistions')
            {
            this.getSearchServiceCalls(that);
            }
        else if(CONSTANTS.retailStaffingObj.chkSearchType())
                {
                    var tempObj = that.setOrgSearchCodeId();
                    var  SummaryRequest= {
                            SummaryRequest:{
                            orgSearchCode:    tempObj.orgSearchCode,
                            orgID: tempObj.orgID,
                            processSearchCode:CONSTANTS.retailStaffingObj.WorkQueuesProcess,
                            paginationDet:{
                                isFirstHit:CONSTANTS.retailStaffingObj.isFirstHit,
                                isForward:CONSTANTS.retailStaffingObj.isForwardFlag
                            },
                    }};
                    var tempFirstRecord = CONSTANTS.retailStaffingObj.currentGridData?(CONSTANTS.retailStaffingObj.currentGridData.Response?(CONSTANTS.retailStaffingObj.currentGridData.Response.firstRecord?(CONSTANTS.retailStaffingObj.currentGridData.Response.firstRecord.updatedTimestamp?CONSTANTS.retailStaffingObj.currentGridData.Response.firstRecord:{}):{}):{}):{};
                    var tempSecondRecord = CONSTANTS.retailStaffingObj.currentGridData?(CONSTANTS.retailStaffingObj.currentGridData.Response?(CONSTANTS.retailStaffingObj.currentGridData.Response.secondRecord?(CONSTANTS.retailStaffingObj.currentGridData.Response.secondRecord.updatedTimestamp?CONSTANTS.retailStaffingObj.currentGridData.Response.secondRecord:{}):{}):{}):{};
                    if(!$.isEmptyObject(tempFirstRecord))
                    	{
                    	SummaryRequest.SummaryRequest.firstRecord = tempFirstRecord;
                    	}
                    if(!$.isEmptyObject(tempSecondRecord))
                	{
                    	SummaryRequest.SummaryRequest.secondRecord = tempSecondRecord;
                	}
                    var callbackFunction = this.defineCallBackFuntion(that);
                    if($("#WorkQueuesStoreValue:visible").length > 0)
                    	{
                    	if($("#WorkQueuesStoreValue:visible").val() === "")
                    		{
                    		CONSTANTS.retailStaffingObj.showModalPopUp("Please enter proper search value before search");
                    		return false;
                    		}
                    	if($("#WorkQueuesStoreValue:visible").val().length < 4)
                		{
                		CONSTANTS.retailStaffingObj.showModalPopUp("Please Enter 4 Digit Store Number");
                		return false;
                		}
                    		CONSTANTS.retailStaffingObj.workQueuesMakeServiceCall(callbackFunction,SummaryRequest);
                    	}
                    else {
                    	CONSTANTS.retailStaffingObj.workQueuesMakeServiceCall(callbackFunction,SummaryRequest);
                    }
                }
    };
    this.workQueuesMakeServiceCall = function (callbackFunction,SummaryRequest){
    	if(CONSTANTS.retailStaffingObj.WorkQueuesProcess !== "0")
        {
        $("#currentGridTitle").parent().parent(".row").addClass("secondContentRow");
        RSASERVICES.getSummaryResults(callbackFunction,JSON.stringify(SummaryRequest));
        }
    };
        /*
         * This method is used to identify the option selected or checked and
         * the corresponding service call is being made
         */
    this.getSearchServiceCalls = function (that) {
        if($("input[type='radio'][name='searchType']:checked").attr('id') === 'requisitionNumber')
        {
            this.requisitionNumberServiceCall(that);
        }
    else if($("input[type='radio'][name='searchType']:checked").attr('id') === 'candidate')
    {
        this.candidateDetailsServiceCall(that);
    }
    else if($("input[type='radio'][name='searchType']:checked").attr('id') === 'phoneScreenNumber')
    {
        this.phoneScreenNumberServiceCall(that);
    }
    else if($("input[type='radio'][name='searchType']:checked").attr('id') === 'interviewScreenNumber')
    {
        this.interviewScreenNumberServiceCall(that);
    }
    };
        /*
         * This method is used to create a request as required so as to get the
         * corresponding data according to the search parameters
         */
    this.setOrgSearchCodeId = function () {
        var orgSearchCode = "";
        var orgID ="";
            if($("input[type='radio'][name='WorkQueuesSearchType']:checked").attr('id') === 'WorkQueuesStore' && $("#WorkQueuesStoreValue").val() !== "" && !isNaN($("#WorkQueuesStoreValue").val()))
                {
                orgSearchCode="store";
                orgID = $("#WorkQueuesStoreValue").val();
                }
            else if($("input[type='radio'][name='WorkQueuesSearchType']:checked").attr('id') === 'WorkQueuesDistrict' && CONSTANTS.retailStaffingObj.WorkQueuesProcess !== "0")
                {
                orgSearchCode="district";
                orgID = CONSTANTS.retailStaffingObj.WorkQueuesDistrictValue;
                }
            else if($("input[type='radio'][name='WorkQueuesSearchType']:checked").attr('id') === 'WorkQueuesRegion' && CONSTANTS.retailStaffingObj.WorkQueuesProcess !== "0")
            {
                orgSearchCode="region";
                orgID = CONSTANTS.retailStaffingObj.WorkQueuesRegionValue;
            }
            else if($("input[type='radio'][name='WorkQueuesSearchType']:checked").attr('id') === 'WorkQueuesDivision' && CONSTANTS.retailStaffingObj.WorkQueuesProcess !== "0")
            {
                orgSearchCode="division";
                orgID = CONSTANTS.retailStaffingObj.WorkQueuesDivisionValue;
            }
            var tempObj = {
                    orgSearchCode : orgSearchCode,
                    orgID : orgID
            };
            return tempObj;
    };
        /*
         * This method is used to defined the call back function for service
         * depending upon the search criteria
         */
    this.defineCallBackFuntion = function (that) {
        var callbackFunction = $.Callbacks('once');
        if(CONSTANTS.retailStaffingObj.WorkQueuesProcess==="Perform Basic Phone Screens")
        {
        $('#currentGridTitle').html("Basic Phone Screens");
     callbackFunction.add(that.buildWorkQueuesGrid);
        }
    else if(CONSTANTS.retailStaffingObj.WorkQueuesProcess==="Perform Detailed Phone Screens")
        {
        $('#currentGridTitle').html("Detailed Phone Screens");
         callbackFunction.add(that.buildWorkQueuesGrid);
        }
    else if(CONSTANTS.retailStaffingObj.WorkQueuesProcess==="Review Completed Phone Screens")
    {
        $('#currentGridTitle').html("Review Completed Phone Screens");
     callbackFunction.add(that.buildreviewCompPhnScrGrid);
    }
    else if(CONSTANTS.retailStaffingObj.WorkQueuesProcess==="Schedule Interviews")
    {
        $('#currentGridTitle').html("Schedule Interviews");
     callbackFunction.add(that.buildScheduledInterviewGrid);
    }
    else if(CONSTANTS.retailStaffingObj.WorkQueuesProcess==="Send Interview Materials")
    {
        $('#currentGridTitle').html("SEND INTERVIEW MATERIALS");
     callbackFunction.add(that.buildInterviewMaterialsGrid);
    }
    else if(CONSTANTS.retailStaffingObj.WorkQueuesProcess==="Review Active Requisitions")
    {
        $('#currentGridTitle').html("ACTIVE REQUISITIONS");
     callbackFunction.add(that.buildreviewActiveReqGrid);
    }
    else if(CONSTANTS.retailStaffingObj.WorkQueuesProcess==="View Inactive Requisitions")
    {
        $('#currentGridTitle').html("INACTIVE REQUISITIONS");
     callbackFunction.add(that.buildviewInactiveReqGrid);
    }
    else if(CONSTANTS.retailStaffingObj.WorkQueuesProcess=== "0") {
        $("#currentGridTitle").parent().parent(".row").removeClass("secondContentRow");
        CONSTANTS.retailStaffingObj.showModalPopUp("Please select a process");
    }
        return callbackFunction;
    };
        /*
         * This method is used to make the service call so as to fetch the
         * interiew details data
         */
    this.interviewScreenNumberServiceCall = function (that) {
        if($("#interviewScreenValue").val() !== "" && !isNaN($("#interviewScreenValue").val()))
        {
        if($("#interviewScreenValue").val().length === 9){
        var  SearchRequest5= {SearchRequest:{
                inputNbr:    $("#interviewScreenValue").val().trim(),
                formName: "InterviewNbr"
        }};
        var callbackFunction5 = $.Callbacks('once');
         callbackFunction5.add(that.buildInterviewNumberGrid);
         RSASERVICES.getSearchDetails(callbackFunction5,SearchRequest5, true, "Please wait...");
        }
         else
            {
             CONSTANTS.retailStaffingObj.showModalPopUp("Please Enter 9 Digit Interview Number");
            }
        }
        else
            {
            CONSTANTS.retailStaffingObj.showModalPopUp("Please enter proper search value before search");
            }
    };
        /*
         * This method is used to make the service call so as to fetch the
         * requisition number details
         */
    this.requisitionNumberServiceCall = function (that) {
        if($("#requisitionValue").val() !== "" && !isNaN($("#requisitionValue").val()))
        {
        if($("#requisitionValue").val().length === 9){
        var  SearchRequest1= {SearchRequest:{
                inputNbr:    $("#requisitionValue").val().trim(),
                formName: "Requisition"
        }};
        var callbackFunction1 = $.Callbacks('once');
         callbackFunction1.add(that.buildrequisitionNumberGrid);
         RSASERVICES.getSearchDetails(callbackFunction1,SearchRequest1, true, "Please wait...");
        }
        else
            {
            CONSTANTS.retailStaffingObj.showModalPopUp("Please Enter 9 Digit Requisition Number");
            }
        }
    else
        {
        CONSTANTS.retailStaffingObj.showModalPopUp("Please enter proper search value before search");
        }
    };
        /* This service call is made as to retrive the candidate details */
    this.candidateDetailsServiceCall = function (that) {
        if($("input[type='radio'][name='CandidateSearchType']:checked").attr('id') === 'candidateRefNumber')
        {
    if($("#candidateNumberValue").val() !== "" && !isNaN($("#candidateNumberValue").val()))
    {
    var  SearchRequest2= {SearchRequest:{
            inputNbr:    $("#candidateNumberValue").val().trim(),
            formName: "Candidate"
    }};
    var callbackFunction2 = $.Callbacks('once');
     callbackFunction2.add(that.buildCandidateGrid);
     RSASERVICES.getSearchDetails(callbackFunction2,SearchRequest2, true, "Please wait...");
    }
    else
        {
        CONSTANTS.retailStaffingObj.showModalPopUp("Please enter at least a 1 Digit Candidate Reference Number");
        }
        }
    else if($("input[type='radio'][name='CandidateSearchType']:checked").attr('id') === 'ssnLastName')
        {
            if($("#candidateSSNValue").val() !== "" && !isNaN($("#candidateSSNValue").val()) && $("#candidateSSNValue").val().length === 4)
            {
                if($("#candidateLastNameValue").val() !== "")
                    {
            var  SearchRequest3= {SearchRequest:{
                    inputNbr:    ""+$("#candidateSSNValue").val().trim()+"|"+$("#candidateLastNameValue").val().trim(),
                    formName: "Candidate"
            }};
            var callbackFunction3 = $.Callbacks('once');
             callbackFunction3.add(that.buildCandidateGrid);
             RSASERVICES.getSearchDetails(callbackFunction3,SearchRequest3, true, "Please wait...");
                    }
                else
                    {
                    CONSTANTS.retailStaffingObj.showModalPopUp("Please Enter Partial or Full Last Name");
                    }
            }
            else
                {
                CONSTANTS.retailStaffingObj.showModalPopUp("Please Enter 4 Digit SSN Number");
                }
        }
    };
        /*
         * This methos is used to make the service call so as to fetch the phone
         * screen details
         */
    this.phoneScreenNumberServiceCall = function (that) {
        if($("#phoneScreenValue").val() !== "" && !isNaN($("#phoneScreenValue").val()))
        {
        if($("#phoneScreenValue").val().length === 9)
            {
        var  SearchRequest4= {SearchRequest:{
                inputNbr:    $("#phoneScreenValue").val().trim(),
                formName: "InterviewNbr"
        }};
        var callbackFunction4 = $.Callbacks('once');
         callbackFunction4.add(that.buildPhoneNumberGrid);
         RSASERVICES.getSearchDetails(callbackFunction4,SearchRequest4, true, "Please wait...");
            }
        else
            {
            CONSTANTS.retailStaffingObj.showModalPopUp("Please Enter 9 Digit Phone Screen Number");
            }
        }
        else
            {
            CONSTANTS.retailStaffingObj.showModalPopUp("Please enter proper search value before search");
            }
    };
        /*
         * This method is used to display the the alert popUp or the informaton
         * popup
         */
    this.showModalPopUp = function(msg)
    {
        $('#warningpopup').modal({
            backdrop : 'static',
            keyboard : false
        });
        $("#warningpopup").on('shown.bs.modal', function () {
            $("#warningpopup #warningOkbutton").focus();
            });
        $("#warningpopup").draggable({
            handle: ".modal-header"
        });
        $("#warningpopup").modal("show");
        $(".sureMsg").html(msg);
        $("#warningOkbutton").attr("data-dismiss","modal");
        $("#warningCancelbutton").hide();
    };
        /* This method is used to define the grid options */
    this.gridOptions = function(){
        var options = {
            enableCellNavigation: false,
            enableColumnReorder: true,
            asyncEditorLoading: true,
            syncColumnCellResize: true,
            autoHeight: true,
            forceFitColumns: true,rowHeight: 30,multiColumnSort: true
          };
        return options;
    };
        /*
         * this is used to format the data coming from the service call so as to
         * display in the grid after formatting
         */
    this.getExactDataForGrid = function(tempData){
        var data = [];
        tempData = tempData? tempData :[];
         if(tempData.constructor !== Array)
             {
             data.push(tempData);
             }
         else if(tempData.constructor === Array)
             {
             data=tempData;
             }
         if(data.length < 8){
            var emptyData = [];
            for(var i=0;i<7;i++)
                 {
                 emptyData[i] =  data[i];
                 }
         data = emptyData;
         }
         return data;
    };
        /*
         * This metod is used to empty the grid add the grid title and build the
         * grid
         */
    this.createAndSetGridTitle = function (title) {
        $(".gridContainer").empty().addClass("custom-grid");
        $("#currentGridTitle").parent().parent(".row").addClass("secondContentRow");
        $('#currentGridTitle').html(title);
        $("#paginationBtnGrp").empty().remove();
        $(".gridContainer").append("<div id='myGrid' style='width:99.8%;'></div>");
    };
        /* This method is used to build the requistion number grid which containfs the followign columns
         * 1. The requisition number in the form of the hyper link so as to navigate the user to the requisition details screenon the click of the link
         * 2. The date created column showing the date of creation
         * 3. Creator column displaying the creator
         * 4. Department column displaying the department
         * 5. Job column dipalying the job
         * 6. job title column displaying the title of the job
         * 7. The screen type to display the type of screen
         * 8. Date needed displaying the date when it is required
         * 9. Openings displying the openings available
         * 10. FT and PT columns for the purpose of displating the FT and PT*/
    this.buildrequisitionNumberGrid = function (response){
        $.unblockUI();
        if(response.Response.status && response.Response.status==="SUCCESS" && response.Response.RequisitionDetailList &&  response.Response.RequisitionDetailList.RequisitionDetail)
            {
        var gridData = response.Response;
        CONSTANTS.retailStaffingObj.createAndSetGridTitle("REQUISITION SEARCH RESULTS");
         var grid;
         $(".gridContainer #myGrid").css({"min-width":"1310px"});
          var columns = [
            {id: "reqNbr", sortable: true, name: "Req. Number", width: 100, field: "reqNbr", formatter: CONSTANTS.retailStaffingObj.requistionNumberFormatter},
            {id: "dateCreate", sortable: true, name: "Date Created", width: 100, field: "dateCreate", formatter: CONSTANTS.retailStaffingObj.requistionNumberDateFormatter},
            {id: "creator", sortable: true, name: "Creator", width: 100, field: "creator"},
            {id: "dept", sortable: true, name: "Dept", width: 100, field: "dept"},
            {id: "job", sortable: true, name: "Job", width: 100, field: "job"},
            {id: "jobTtl", sortable: true, name: "Job Title", width: 100, field: "jobTtl"},
            {id: "scrTyp", sortable: true, name: "Screen Type", width: 100, field: "scrTyp"},
            {id: "fillDt", sortable: true, name: "Date Needed", width: 100, field: "fillDt", formatter: CONSTANTS.retailStaffingObj.requistionNumberDateFormatter},
            {id: "openings", sortable: true, name: "Openings", width: 100, field: "openings"},
            {id: "ft", sortable: true, name: "FT", width: 100, field: "ft"},
            {id: "pt", sortable: true, name: "PT", width: 100, field: "pt"},
            {id: "rscToManageFlg", sortable: true, name: "TAC To Manage", width: 100, field: "rscToManageFlg"},
            {id: "active", sortable: true, name: "Req. Active", width: 100, field: "active"}
          ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          data = CONSTANTS.retailStaffingObj.getExactDataForGrid(gridData.RequisitionDetailList.RequisitionDetail);
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.registeringFutureevents();
            }
        else if(response.Response.error)
            {
            if(response.Response.error.errorMsg){
                CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
                }
            $(".gridContainer").empty().addClass("custom-grid");
            var emptyResponse = {"Response":{
                "status":"SUCCESS",
                "RequisitionDetailList":{
                    "RequisitionDetail":{}
                }
            }};
            CONSTANTS.retailStaffingObj.buildrequisitionNumberGrid(emptyResponse);
            }
    };
        /*
         * This method is used for the formatting of the date that is being
         * received from the service so as todisplay in the grid as rewuired
         */
    this.requistionNumberDateFormatter = function (row, cell, value) {
        var dateToDisplay = "";
        if(value && value !== "")
            {
            var tempDate = value.split("-");
            dateToDisplay = tempDate[1]+"/"+tempDate[2]+"/"+tempDate[0];
            }
        return dateToDisplay;
    };
        /*
         * This method is used for the purpose of registering the events for the
         * futurestic elements
         */
    this.registeringFutureevents = function () {
        $(".gridContainer").off("click",".reqDetailsScreen");
        $(".gridContainer").off("click",".candidateDetailsScreen");
        $(".gridContainer").off("click",".candidateDetailsScreen1");
        $(".gridContainer").off("click",".phoneScreenDetailsScreen");
        $(".gridContainer").off("click",".intScreenDetailsScreen");
        $(".gridContainerRowClass").off("click",".paginationButtons");
        $(".gridContainer").off("click",".reviewCompletedPhoneScreenDetails");
        $(".gridContainer").on("click",".reqDetailsScreen",function(e){
            CONSTANTS.retailStaffingObj.navigateToReqScreenDetails(e);
          });
        $(".gridContainer").on("click",".candidateDetailsScreen",function(e){
            var result = CONSTANTS.retailStaffingObj.currentGrid.getData();
            var selectedObj = {};
            for(var i=0;i<result.length;i++)
        	{
        	if (result[i] && result[i].candRefNbr === parseInt($(e.currentTarget).text()))
        		{
        		selectedObj = result[i];
        		}
        	}
            CONSTANTS.retailStaffingObj.applicantId = selectedObj.ssnNbr;
            CONSTANTS.retailStaffingObj.candRefId = selectedObj.candRefNbr;
            CONSTANTS.retailStaffingObj.navigateToCandidateDetails(e);
        });
        $(".gridContainer").on("click",".candidateDetailsScreen1",function(e){
            var result = CONSTANTS.retailStaffingObj.currentGrid.getData();
            var selectedObj = {};
            for(var i=0;i<result.length;i++)
        	{
        	if (result[i] && result[i].candRefNbr === parseInt($(e.currentTarget).text()))
        		{
        		selectedObj = result[i];
        		}
        	}
            CONSTANTS.retailStaffingObj.applicantId = selectedObj.cndtNbr;
            CONSTANTS.retailStaffingObj.candRefId = selectedObj.candRefNbr;
            CONSTANTS.retailStaffingObj.navigateToCandidateDetails(e);
        });
        $(".gridContainer").on("click",".phoneScreenDetailsScreen",function(e){
             CONSTANTS.retailStaffingObj.navigateToPhoneScreenDetails(e);
         });
         $(".gridContainer").on("click",".intScreenDetailsScreen",function(e){
            CONSTANTS.retailStaffingObj.navigateToInterviewScreenDetails(e);
        });
         $(".gridContainerRowClass").on('click',".paginationButtons",function(e){
             CONSTANTS.retailStaffingObj.onPagingButtonsClicked(e);
         });
         $(".gridContainer").on("click",".reviewCompletedPhoneScreenDetails",function(e){
             CONSTANTS.retailStaffingObj.navigateToReviewCompletedPhoneScreenDetails(e);
         });
    };
        /* This method is used to build the grid showing the candidate details with the following columns
         * Candidate ID
         * Associate ID
         * Name
         */
    this.buildCandidateGrid = function (response){
        $.unblockUI();
        if(response.Response.status && response.Response.status==="SUCCESS" && response.Response.CandidateDetailList &&  response.Response.CandidateDetailList.CandidateDetails)
        {
        var gridData = response.Response;
        CONSTANTS.retailStaffingObj.createAndSetGridTitle("CANDIDATE SEARCH RESULTS");
         var grid;
          var columns = [
            {id: "candRefNbr", sortable: true, name: "Candidate ID", field: "candRefNbr", formatter: CONSTANTS.retailStaffingObj.candidateDetailsFormatter},
            {id: "aid", sortable: true, name: "Associate ID", field: "aid"},
            {id: "name", sortable: true, name: "Name", field: "name"}
          ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          data = CONSTANTS.retailStaffingObj.getExactDataForGrid(gridData.CandidateDetailList.CandidateDetails);
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else if(response.Response.error)
        {
        if(response.Response.error.errorMsg){
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        var emptyResponse = {"Response":{
            "status":"SUCCESS",
            "CandidateDetailList":{
                "CandidateDetails":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildCandidateGrid(emptyResponse);
        }
    };
        /* This method is used to build the phone screen details in the grid with the following columns
         * Phone Screen Number
         * Candidate ID
         * Associate ID
         * Name
         * Requisition Number
         * Job
         * Screen Type
         * Phone Screen Status
         */
    this.buildPhoneNumberGrid = function (response){
        $.unblockUI();
        if(response.Response.status && response.Response.status==="SUCCESS" && response.Response.ITIDetailList &&  response.Response.ITIDetailList.PhoneScreenIntrwDetail)
        {
        var gridData = response.Response;
        CONSTANTS.retailStaffingObj.createAndSetGridTitle("PHONE SCREEN NUMBER");
        $(".gridContainer #myGrid").css({"min-width":"1130px"});
         var grid;
         var columns = [
                        {id: "itiNbr", sortable: true, name: "Phone Screen Number", width: 220, field: "itiNbr", formatter: CONSTANTS.retailStaffingObj.phoneScreenDetailsFormatter},
                        {id: "candRefNbr", sortable: true, name: "Candidate ID", width: 100, field: "candRefNbr", formatter: CONSTANTS.retailStaffingObj.candidateDetailsFormatter1},
                        {id: "aid", sortable: true, name: "Associate ID", width: 100, field: "aid"},
                        {id: "name", sortable: true, name: "Name", width: 100, field: "name"},
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 100, field: "reqNbr", formatter: CONSTANTS.retailStaffingObj.requistionNumberFormatter},
                        {id: "job", sortable: true, name: "Job", width: 100, field: "job"},
                        {id: "scrTyp", sortable: true, name: "Screen Type", width: 100, field: "scrTyp"},
                        {id: "phoneScreenStatusDesc", sortable: true, name: "Phone Screen Status", width: 300, field: "phoneScreenStatusDesc"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          data = CONSTANTS.retailStaffingObj.getExactDataForGrid(gridData.ITIDetailList.PhoneScreenIntrwDetail);
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else if(response.Response.error)
        {
        if(response.Response.error.errorMsg){
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        var emptyResponse = {"Response":{
            "status":"SUCCESS",
            "ITIDetailList":{
                "PhoneScreenIntrwDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildPhoneNumberGrid(emptyResponse);
        }
    };
        /*
         * This is a formatter so as to have a hyper link inside the grid so as
         * to navidate to the candidate details Screen
         */
    this.candidateDetailsFormatter = function (row, cell, value) {
        return value?('<div class="text-center candidateDetailsScreen"><a href="#" class="">' + value + '</a></div>'):"";
    };
    /*
     * This is a formatter so as to have a hyper link inside the grid so as
     * to navidate to the candidate details Screen
     */
this.candidateDetailsFormatter1 = function (row, cell, value) {
    return value?('<div class="text-center candidateDetailsScreen1"><a href="#" class="">' + value + '</a></div>'):"";
};
    /*
     * This is a formatter so as to have a hyper link inside the grid so as
     * to navidate to the Phone Screen details Screen
     */
    this.phoneScreenDetailsFormatter = function (row, cell, value) {
        return value?('<div class="text-center phoneScreenDetailsScreen"><a href="#" class="">' + value + '</a></div>'):"";
    };
        /* This method is used to build the interview number grid with the following columns
         * Interview screen number
         * Candidate ID
         * Name
         * Requisition Number
         * job
         * Screen Type and
         * Interview status
         */
    this.buildInterviewNumberGrid = function (response){
        $.unblockUI();
        if(response.Response.status && response.Response.status==="SUCCESS" && response.Response.ITIDetailList &&  response.Response.ITIDetailList.PhoneScreenIntrwDetail)
        {
        var gridData = response.Response;
        CONSTANTS.retailStaffingObj.createAndSetGridTitle("INTERVIEW SCREEN NUMBER");
        $(".gridContainer #myGrid").css({"min-width":"940px"});
         var grid;
         var columns = [
                        {id: "itiNbr", sortable: true, name: "Interview Screen Number", width: 220, field: "itiNbr",formatter: CONSTANTS.retailStaffingObj.interviewScreenDetailsFormatter},
                        {id: "candRefNbr", sortable: true, name: "Candidate ID", width: 110, field: "candRefNbr", formatter: CONSTANTS.retailStaffingObj.candidateDetailsFormatter1},
                        {id: "name", sortable: true, name: "Name", width: 110, field: "name"},
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 110, field: "reqNbr", formatter: CONSTANTS.retailStaffingObj.requistionNumberFormatter},
                        {id: "job", sortable: true, name: "Job", width: 110, field: "job"},
                        {id: "scrTyp", sortable: true, name: "Screen Type", width: 110, field: "scrTyp"},
                        {id: "interviewStatusDesc", sortable: true, name: "Interview Status", width: 160, field: "interviewStatusDesc"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          data = CONSTANTS.retailStaffingObj.getExactDataForGrid(gridData.ITIDetailList.PhoneScreenIntrwDetail);
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else if(response.Response.error)
        {
        if(response.Response.error.errorMsg){
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        var emptyResponse = {"Response":{
            "status":"SUCCESS",
            "ITIDetailList":{
                "PhoneScreenIntrwDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildInterviewNumberGrid(emptyResponse);
        }
    };
        /*
         * This function is used to build the basic phone sreens and detailed
         * phone screens grid with the following columns
         * Phone screen number
         * Requisition number
         * Name
         * Candidate ID
         * Associate ID
         * Job and
         * Job title
         */
    this.buildWorkQueuesGrid = function (response){
        $.unblockUI();
        if(!response.Response.error)
        {
        CONSTANTS.retailStaffingObj.currentGridData = response;
        $(".gridContainer").empty().addClass("custom-grid");
        $(".gridContainer").append("<div id='myGrid' style='width:99.8%;'></div>");
        $("#paginationBtnGrp").empty().remove();
        $(".gridContainer").after("<div id='paginationBtnGrp'><button class='paginationButtons' id='paginationPrevButton'>&lt;&nbsp;Previous</button><button class='paginationButtons' id='paginationNextButton'>Next&nbsp;&gt;</button></div>");
         var grid;
         $(".gridContainer #myGrid").css({"min-width":"820px"});
         var columns = [
                        {id: "itiNbr", sortable: true, name: "Phone Screen Number", width: 220, field: "itiNbr", formatter: CONSTANTS.retailStaffingObj.phoneScreenDetailsFormatter},
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 110, field: "reqNbr"},
                        {id: "name", sortable: true, name: "Name", width: 110, field: "name"},
                        {id: "candRefNbr", sortable: true, name: "Candidate ID", width: 110, field: "candRefNbr"},
                        {id: "aid", sortable: true, name: "Associate ID", width: 110, field: "aid"},
                        {id: "job", sortable: true, name: "Job", width: 110, field: "job"},
                        {id: "jobTtl", sortable: true, name: "Job Title", width: 150, field: "jobTtl"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          var fullData1 = CONSTANTS.retailStaffingObj.currentGridData.Response.ITIDetailList.PhoneScreenIntrwDetail;
          var tempData = CONSTANTS.retailStaffingObj.commonPaginationCode(fullData1?fullData1:[]);
          data=tempData;
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.fromGrid = "buildWorkQueuesGrid";
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else
        {
        if(response.Response.error.errorMsg){
            if(response.Response.error.errorCode !== "800"){
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "0";
            }
            else
                {
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "800";
                }
            $(".gridContainer").empty();
            $("#paginationBtnGrp").empty().remove();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        else
        	{
        var emptyResponse = {"Response":{
            "ITIDetailList":{
                "PhoneScreenIntrwDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildWorkQueuesGrid(emptyResponse);
        	}
        }
    };
        /* This method is used to build the scheduled interview grid with the following columns
         * Interview Screen Number
         * Name
         * Candidate ID
         * Associate ID
         * Job
         * Job Title
         */
    this.buildScheduledInterviewGrid = function (response){
        $.unblockUI();
        if(!response.Response.error)
        {
        CONSTANTS.retailStaffingObj.currentGridData = response;
        $(".gridContainer").empty().addClass("custom-grid");
        $(".gridContainer").append("<div id='myGrid' style='width:99.8%;'></div>");
        $("#paginationBtnGrp").empty().remove();
        $(".gridContainer").after("<div id='paginationBtnGrp'><button class='paginationButtons' id='paginationPrevButton'>&lt;&nbsp;Previous</button><button class='paginationButtons' id='paginationNextButton'>Next&nbsp;&gt;</button></div>");
         var grid;
         $(".gridContainer #myGrid").css({"min-width":"820px"});
         var columns = [
                        {id: "itiNbr", sortable: true, name: "Interview Screen Number", width: 220, field: "itiNbr",formatter: CONSTANTS.retailStaffingObj.interviewScreenDetailsFormatter},
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 110, field: "reqNbr"},
                        {id: "name", sortable: true, name: "Name", width: 110, field: "name"},
                        {id: "candRefNbr", sortable: true, name: "Candidate ID", width: 110, field: "candRefNbr"},
                        {id: "aid", sortable: true, name: "Associate ID", width: 110, field: "aid"},
                        {id: "job", sortable: true, name: "Job", width: 110, field: "job"},
                        {id: "jobTtl", sortable: true, name: "Job Title", width: 150, field: "jobTtl"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          var fullData1 = CONSTANTS.retailStaffingObj.currentGridData.Response.ITIDetailList.PhoneScreenIntrwDetail;
          var tempData = CONSTANTS.retailStaffingObj.commonPaginationCode(fullData1?fullData1:[]);
            data=tempData;
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.fromGrid = "buildScheduledInterviewGrid";
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else
        {
        if(response.Response.error.errorMsg){
            if(response.Response.error.errorCode !== "800"){
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "0";
            }
            else
                {
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "800";
                }
            $(".gridContainer").empty();
            $("#paginationBtnGrp").empty().remove();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        else
    	{
        var emptyResponse = {"Response":{
            "ITIDetailList":{
                "PhoneScreenIntrwDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildScheduledInterviewGrid(emptyResponse);
    	}
        }
    };
        /* This method is used to build the interview materials grid With the following columns
         * Interview Screen number
         * Requisition Number
         * Store
         * Date Needed
         * Job
         * Candidate ID
         */
    this.buildInterviewMaterialsGrid = function (response){
        $.unblockUI();
        if(!response.Response.error)
        {
        CONSTANTS.retailStaffingObj.currentGridData = response;
        $(".gridContainer").empty().addClass("custom-grid");
        $(".gridContainer").append("<div id='myGrid' class='InterviewMaterialsGrid pagingIncluded' style='width:99.8%;'></div>");
        $("#paginationBtnGrp").empty().remove();
        $(".gridContainer").after("<div id='paginationBtnGrp'><button class='paginationButtons' id='paginationPrevButton'>&lt;&nbsp;Previous</button><button class='paginationButtons' id='paginationNextButton'>Next&nbsp;&gt;</button></div>");
         var grid;
         $(".gridContainer #myGrid").css({"min-width":"920px"});
         var columns = [
                        {id: "itiNbr", sortable: true, name: "Interview Screen Number", width: 240, field: "itiNbr",formatter: CONSTANTS.retailStaffingObj.interviewScreenDetailsFormatter},
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 230, field: "reqNbr"},
                        {id: "cndStrNbr", sortable: true, name: "Store #", width: 110, field: "cndStrNbr"},
                        {id: "fillDt", sortable: true, name: "Date Needed", width: 110, field: "fillDt"},
                        {id: "job", sortable: true, name: "Job", width: 110, field: "job"},
                        {id: "candRefNbr", sortable: true, name: "Candidate ID", width: 110, field: "candRefNbr"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          var fullData1 = CONSTANTS.retailStaffingObj.currentGridData.Response.ITIDetailList.PhoneScreenIntrwDetail;
          var tempData = CONSTANTS.retailStaffingObj.commonPaginationCode(fullData1?fullData1:[]);
            data=tempData;
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.fromGrid = "buildInterviewMaterialsGrid";
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else
        {
        if(response.Response.error.errorMsg){
            if(response.Response.error.errorCode !== "800"){
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "0";
            }
            else
                {
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "800";
                }
            $(".gridContainer").empty();
            $("#paginationBtnGrp").empty().remove();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        else
    	{
        var emptyResponse = {"Response":{
            "ITIDetailList":{
                "PhoneScreenIntrwDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildInterviewMaterialsGrid(emptyResponse);
    	}
        }
    };
        /*
         * This method is formatter that is used to insert a hyper link inside
         * the grid so as to naviagte to the interview screen details screen
         */
    this.interviewScreenDetailsFormatter = function (row, cell, value) {
        return value?('<div class="text-center intScreenDetailsScreen"><a href="#" class="">' + value + '</a></div>'):"";
    };
        /* This method is used to build the review completed phone screen grid with the following columns
         * Requisition Number
         * date Created
         * Creator
         * Department
         * Job
         * Job Title
         * Screen Type
         * Data Needed
         * Openings
         * Ft
         * PT
         * RSC to Manage
         * Store #
         * District
         * Region
         * Division
         */
    this.buildreviewCompPhnScrGrid = function (response){
        $.unblockUI();
        if(!response.Response.error)
        {
        CONSTANTS.retailStaffingObj.currentGridData = response;
        $(".gridContainer").empty().addClass("custom-grid");
        $(".gridContainer").append("<div id='myGrid' class='reviewActiveReqGrid pagingIncluded' style='width:99.8%;'></div>");
        $("#paginationBtnGrp").empty().remove();
        $(".gridContainer").after("<div id='paginationBtnGrp'><button class='paginationButtons' id='paginationPrevButton'>&lt;&nbsp;Previous</button><button class='paginationButtons' id='paginationNextButton'>Next&nbsp;&gt;</button></div>");
         var grid;
         $(".gridContainer #myGrid").css({"min-width":"1380px"});
         var columns = [
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 110, field: "reqNbr", formatter: CONSTANTS.retailStaffingObj.revCompPhnScrnFormatter},
                        {id: "dateCreate", sortable: true, name: "Date Created", width: 90, field: "dateCreate"},
                        {id: "creator", sortable: true, name: "Creator", width: 90, field: "creator"},
                        {id: "dept", sortable: true, name: "Department", width: 90, field: "dept"},
                        {id: "job", sortable: true, name: "Job", width: 90, field: "job"},
                        {id: "jobTtl", sortable: true, name: "Job Title", width: 90, field: "jobTtl"},
                        {id: "phnScrTyp", sortable: true, name: "Screen Type", width: 90, field: "phnScrTyp"},
                        {id: "fillDt", sortable: true, name: "Date Needed", width: 90, field: "fillDt"},
                        {id: "openings", sortable: true, name: "Openings", width: 90, field: "openings"},
                        {id: "ft", sortable: true, name: "FT", width: 45, field: "ft"},
                        {id: "pt", sortable: true, name: "PT", width: 45, field: "pt"},
                        {id: "rscToManageFlg", sortable: true, name: "TAC to Manage", width: 90, field: "rscToManageFlg"},
                        {id: "store", sortable: true, name: "Store #", width: 90, field: "store"},
                        {id: "humanResourcesSystemRegionCode", sortable: true, name: "District", width: 90, field: "humanResourcesSystemRegionCode"},
                        {id: "humanResourcesSystemOperationsGroupCode", sortable: true, name: "Region", width: 90, field: "humanResourcesSystemOperationsGroupCode"},
                        {id: "humanResourcesSystemDivisionCode", sortable: true, name: "Division", width: 90, field: "humanResourcesSystemDivisionCode"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          var fullData1 = CONSTANTS.retailStaffingObj.currentGridData.Response.RequisitionDetailList.RequisitionDetail;
          var tempData = CONSTANTS.retailStaffingObj.commonPaginationCode(fullData1?fullData1:[]);
            data=tempData;
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.fromGrid = "buildreviewCompPhnScrGrid";
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else
        {
        if(response.Response.error.errorMsg){
            if(response.Response.error.errorCode !== "800"){
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "0";
            }
            else
                {
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "800";
                }
            $(".gridContainer").empty();
            $("#paginationBtnGrp").empty().remove();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        else
    	{
        var emptyResponse = {"Response":{
            "RequisitionDetailList":{
                "RequisitionDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildreviewCompPhnScrGrid(emptyResponse);
    	}
        }
    };
        /* This method is used to build the review active requistion grid with the following columns
         * Requisition Number
         * date Created
         * Creator
         * Department
         * Job
         * Job Title
         * Screen Type
         * Data Needed
         * Openings
         * Ft
         * PT
         * RSC to Manage
         * Store #
         * District
         * Region
         * Division
         */
    this.buildreviewActiveReqGrid = function (response){
        $.unblockUI();
        if(!response.Response.error)
        {
        CONSTANTS.retailStaffingObj.currentGridData = response;
        $(".gridContainer").empty().addClass("custom-grid");
        $(".gridContainer").append("<div id='myGrid' class='reviewActiveReqGrid pagingIncluded' style='width:99.8%;'></div>");
        $("#paginationBtnGrp").empty().remove();
        $(".gridContainer").after("<div id='paginationBtnGrp'><button class='paginationButtons' id='paginationPrevButton'>&lt;&nbsp;Previous</button><button class='paginationButtons' id='paginationNextButton'>Next&nbsp;&gt;</button></div>");
         var grid;
         $(".gridContainer #myGrid").css({"min-width":"1380px"});
         var columns = [
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 110, field: "reqNbr", formatter: CONSTANTS.retailStaffingObj.requistionNumberFormatter},
                        {id: "dateCreate", sortable: true, name: "Date Created", width: 90, field: "dateCreate"},
                        {id: "creator", sortable: true, name: "Creator", width: 90, field: "creator"},
                        {id: "dept", sortable: true, name: "Department", width: 90, field: "dept"},
                        {id: "job", sortable: true, name: "Job", width: 90, field: "job"},
                        {id: "jobTtl", sortable: true, name: "Job Title", width: 90, field: "jobTtl"},
                        {id: "phnScrTyp", sortable: true, name: "Screen Type", width: 90, field: "phnScrTyp"},
                        {id: "fillDt", sortable: true, name: "Date Needed", width: 90, field: "fillDt"},
                        {id: "openings", sortable: true, name: "Openings", width: 90, field: "openings"},
                        {id: "ft", sortable: true, name: "FT", width: 45, field: "ft"},
                        {id: "pt", sortable: true, name: "PT", width: 45, field: "pt"},
                        {id: "rscToManageFlg", sortable: true, name: "TAC to Manage", width: 90, field: "rscToManageFlg"},
                        {id: "store", sortable: true, name: "Store #", width: 90, field: "store"},
                        {id: "humanResourcesSystemRegionCode", sortable: true, name: "District", width: 90, field: "humanResourcesSystemRegionCode"},
                        {id: "humanResourcesSystemOperationsGroupCode", sortable: true, name: "Region", width: 90, field: "humanResourcesSystemOperationsGroupCode"},
                        {id: "humanResourcesSystemDivisionCode", sortable: true, name: "Division", width: 90, field: "humanResourcesSystemDivisionCode"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          var fullData1 = CONSTANTS.retailStaffingObj.currentGridData.Response.RequisitionDetailList.RequisitionDetail;
          var tempData = CONSTANTS.retailStaffingObj.commonPaginationCode(fullData1?fullData1:[]);
            data=tempData;
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.fromGrid = "buildreviewActiveReqGrid";
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else
        {
        if(response.Response.error.errorMsg){
            if(response.Response.error.errorCode !== "800"){
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "0";
            }
            else
                {
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "800";
                }
            $(".gridContainer").empty();
            $("#paginationBtnGrp").empty().remove();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        else
    	{
        var emptyResponse = {"Response":{
            "RequisitionDetailList":{
                "RequisitionDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildreviewActiveReqGrid(emptyResponse);
    	}
        }
    };
    /*
     * This is a formatter so as to have a hyper link inside the grid so as
     * to navidate to the review phone screen details Screen
     */
    this.revCompPhnScrnFormatter = function (row, cell, value) {
        return value?('<div class="text-center reviewCompletedPhoneScreenDetails"><a href="#" class="">' + value + '</a></div>'):"";
    };
    /* This method is used to build the view inactive requistion grid with the following columns
         * Requisition Number
         * date Created
         * Creator
         * Department
         * Job
         * Job Title
         * Screen Type
         * Data Needed
         * Openings
         * Ft
         * PT
         * RSC to Manage
         * Store #
         * District
         * Region
         * Division
         */
    this.buildviewInactiveReqGrid = function (response){
        $.unblockUI();
        if(!response.Response.error)
        {
        CONSTANTS.retailStaffingObj.currentGridData = response;
        $(".gridContainer").empty().addClass("custom-grid");
        $(".gridContainer").append("<div id='myGrid' class='viewInactiveReqGrid pagingIncluded' style='width:99.8%;'></div>");
        $("#paginationBtnGrp").empty().remove();
        $(".gridContainer").after("<div id='paginationBtnGrp'><button class='paginationButtons' id='paginationPrevButton'>&lt;&nbsp;Previous</button><button class='paginationButtons' id='paginationNextButton'>Next&nbsp;&gt;</button></div>");
         var grid;
         $(".gridContainer #myGrid").css({"min-width":"1510px"});
         var columns = [
                        {id: "reqNbr", sortable: true, name: "Requisition Number", width: 150, field: "reqNbr", formatter: CONSTANTS.retailStaffingObj.requistionNumberFormatter},
                        {id: "quewebNbr", sortable: true, name: "Queweb Number", width: 90, field: "quewebNbr"},
                        {id: "dateCreate", sortable: true, name: "Date Created", width: 90, field: "dateCreate"},
                        {id: "creator", sortable: true, name: "Creator", width: 90, field: "creator"},
                        {id: "dept", sortable: true, name: "Department", width: 90, field: "dept"},
                        {id: "job", sortable: true, name: "Job", width: 90, field: "job"},
                        {id: "jobTtl", sortable: true, name: "Job Title", width: 90, field: "jobTtl"},
                        {id: "phnScrTyp", sortable: true, name: "Screen Type", width: 90, field: "phnScrTyp"},
                        {id: "fillDt", sortable: true, name: "Date Needed", width: 90, field: "fillDt"},
                        {id: "openings", sortable: true, name: "Openings", width: 90, field: "openings"},
                        {id: "ft", sortable: true, name: "FT", width: 45, field: "ft"},
                        {id: "pt", sortable: true, name: "PT", width: 45, field: "pt"},
                        {id: "rscToManageFlg", sortable: true, name: "TAC to Manage", width: 90, field: "rscToManageFlg"},
                        {id: "store", sortable: true, name: "Store #", width: 90, field: "store"},
                        {id: "humanResourcesSystemRegionCode", sortable: true, name: "District", width: 90, field: "humanResourcesSystemRegionCode"},
                        {id: "humanResourcesSystemOperationsGroupCode", sortable: true, name: "Region", width: 90, field: "humanResourcesSystemOperationsGroupCode"},
                        {id: "humanResourcesSystemDivisionCode", sortable: true, name: "Division", width: 90, field: "humanResourcesSystemDivisionCode"}
                      ];
          var options = CONSTANTS.retailStaffingObj.gridOptions();
          var data =[];
          var fullData1 = CONSTANTS.retailStaffingObj.currentGridData.Response.RequisitionDetailList.RequisitionDetail;
        var tempData = CONSTANTS.retailStaffingObj.commonPaginationCode(fullData1?fullData1:[]);
        data=tempData;
          grid = new Slick.Grid("#myGrid", data, columns, options);
          CONSTANTS.retailStaffingObj.currentGrid = {};
          CONSTANTS.retailStaffingObj.currentGrid = grid;
          CONSTANTS.retailStaffingObj.gridsort(grid,data);
          CONSTANTS.retailStaffingObj.fromGrid = "buildviewInactiveReqGrid";
          CONSTANTS.retailStaffingObj.registeringFutureevents();
    }
    else
        {
        if(response.Response.error.errorMsg){
            if(response.Response.error.errorCode !== "800"){
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "0";
            }
            else
                {
                CONSTANTS.retailStaffingObj.prevFlagErrCode = "800";
                }
            $(".gridContainer").empty();
            $("#paginationBtnGrp").empty().remove();
            CONSTANTS.retailStaffingObj.popup.alert(response.Response.error.errorMsg);
            }
        else
    	{
        var emptyResponse = {"Response":{
            "RequisitionDetailList":{
                "RequisitionDetail":{}
            }
        }};
        CONSTANTS.retailStaffingObj.buildviewInactiveReqGrid(emptyResponse);
    	}
        }
    };
        /*
         * This is a formatter so as to have a hyper link inside the grid so as
         * to navidate to the requisition details screen
         */
    this.requistionNumberFormatter = function (row, cell, value) {
        return value?('<div class="text-center reqDetailsScreen"><a href="#" class="">' + value + '</a></div>'):"";
    };
        /*
         * This is used for the purpose of including the pagination options in
         * the grid
         */
    this.commonPaginationCode =function (fullData1)
    {
        var tempData = [];
        var fullData = [];
        if(fullData1.constructor !== Array)
          {
              fullData.push(fullData1);
          }
          else if(fullData1.constructor === Array)
              {
              fullData=fullData1;
              }
          if(!CONSTANTS.retailStaffingObj.pageCounter)
            {
            CONSTANTS.retailStaffingObj.pageCounter = 0;
            }
          var index;
                  /*
                     * Checking if the page is the initail page of the set of 5
                     * pages and enabling and disabling the next and prev
                     * buttons accordingly
                     */
          if(CONSTANTS.retailStaffingObj.pageCounter === 0 || CONSTANTS.retailStaffingObj.pageCounter % 5 === 0 )
              {
              for(var i = 0;i<20;i++)
                  {
                  if(!fullData[i])
                      {
                      $("#paginationNextButton").attr("disabled","disabled");
                      }
                  else
                      {
                      $("#paginationNextButton").removeAttr("disabled");
                      }
                  tempData[i] =  fullData[i];
                  }
              }
          else
              {
              if(CONSTANTS.retailStaffingObj.pageCounter < 5)
                  {
                 index = CONSTANTS.retailStaffingObj.pageCounter;
                  }
              else {
                  index = CONSTANTS.retailStaffingObj.pageCounter % 5;
                  }
              var k = -1;
              for(var j = (index*20);j<((index*20)+20);j++)
                  {
                  k++;
                  if(fullData[j])
                      {
                      tempData[k] =  fullData[j];
                      $("#paginationNextButton").removeAttr("disabled");
                      }
                  else{
                      tempData[k] =  fullData[j];
                      $("#paginationNextButton").attr("disabled","disabled");
                      CONSTANTS.retailStaffingObj.isForwardFlag = false;
                  }
                  }
              }
          CONSTANTS.retailStaffingObj.createNavBar();
          return tempData;
    };
        /*
         * This method is invoked on the click of the pagination links in the
         * bottom of the grid
         */
    this.onPagingButtonsClicked = function (e) {

        CONSTANTS.retailStaffingObj.isFirstHit = false;

        if($(e.currentTarget).attr("id") === "paginationNextButton")
            {
            CONSTANTS.retailStaffingObj.pageCounter = CONSTANTS.retailStaffingObj.pageCounter + 1;
            $("#paginationPrevButton").removeAttr("disabled");
            }
        else if($(e.currentTarget).attr("id") === "paginationPrevButton" && CONSTANTS.retailStaffingObj.pageCounter !== 0)
        {
            CONSTANTS.retailStaffingObj.pageCounter = CONSTANTS.retailStaffingObj.pageCounter - 1;
        }
        /* enabling and disabling of the nav buttons */
        CONSTANTS.retailStaffingObj.createNavBar();
        /* making service calls on the prev and next click when required */
        CONSTANTS.retailStaffingObj.paginationHandling(e);
    };
        /*
         * This method is used for the purpose of handling the ppagination and
         * setting the first hit flag to true or false so as to get the proper
         * response from the service
         */
    this.paginationHandling = function (e) {
        if($(e.currentTarget).attr("id") === "paginationPrevButton" && (CONSTANTS.retailStaffingObj.pageCounter + 1)%5 === 0 && (CONSTANTS.retailStaffingObj.pageCounter +1) >=5 )
        {
        if(CONSTANTS.retailStaffingObj.prevFlagErrCode !== "800")
            {
                    if(CONSTANTS.retailStaffingObj.pageCounter < 5)
                    {
                    CONSTANTS.retailStaffingObj.isFirstHit = true;
                    }
                else
                    {
                    CONSTANTS.retailStaffingObj.isFirstHit = false;
                    }
            CONSTANTS.retailStaffingObj.onSubmitButtonClick();
            }
        else
            {
            CONSTANTS.retailStaffingObj.buildRespectiveGrid();
            }
        }
    else if($(e.currentTarget).attr("id") === "paginationNextButton" && (CONSTANTS.retailStaffingObj.pageCounter %5 === 0) && CONSTANTS.retailStaffingObj.isFirstHit !== CONSTANTS.booleanTrue)
        {
                if(CONSTANTS.retailStaffingObj.pageCounter < 5)
                {
                CONSTANTS.retailStaffingObj.isFirstHit = true;
                }
            else
                {
                CONSTANTS.retailStaffingObj.isFirstHit = false;
                }
        CONSTANTS.retailStaffingObj.onSubmitButtonClick();
        }
    else
        {
        CONSTANTS.retailStaffingObj.buildRespectiveGrid();
        }
    };
        /* This method is used to rebuild the corresponding grid */
    this.buildRespectiveGrid = function () {
        if(CONSTANTS.retailStaffingObj.fromGrid === "buildWorkQueuesGrid"){
            CONSTANTS.retailStaffingObj.buildWorkQueuesGrid(CONSTANTS.retailStaffingObj.currentGridData);
        }else if(CONSTANTS.retailStaffingObj.fromGrid === "buildScheduledInterviewGrid"){
            CONSTANTS.retailStaffingObj.buildScheduledInterviewGrid(CONSTANTS.retailStaffingObj.currentGridData);
        }else if(CONSTANTS.retailStaffingObj.fromGrid === "buildInterviewMaterialsGrid"){
            CONSTANTS.retailStaffingObj.buildInterviewMaterialsGrid(CONSTANTS.retailStaffingObj.currentGridData);
        }else if(CONSTANTS.retailStaffingObj.fromGrid === "buildreviewCompPhnScrGrid"){
            CONSTANTS.retailStaffingObj.buildreviewCompPhnScrGrid(CONSTANTS.retailStaffingObj.currentGridData);
        }else if(CONSTANTS.retailStaffingObj.fromGrid === "buildreviewActiveReqGrid"){
            CONSTANTS.retailStaffingObj.buildreviewActiveReqGrid(CONSTANTS.retailStaffingObj.currentGridData);
        }else if(CONSTANTS.retailStaffingObj.fromGrid === "buildviewInactiveReqGrid"){
            CONSTANTS.retailStaffingObj.buildviewInactiveReqGrid(CONSTANTS.retailStaffingObj.currentGridData);
        }
    };
        /* This methos is used for the purpose of building the navigation bar */
    this.createNavBar = function ()
    {
        if(CONSTANTS.retailStaffingObj.pageCounter === 0)
            {
            $("#paginationPrevButton").attr("disabled","disabled");
            }
        else if(CONSTANTS.retailStaffingObj.isForwardFlag !== CONSTANTS.booleanTrue)
            {
            $("#paginationNextButton").attr("disabled","disabled");
            }
        else
            {
            $("#paginationNextButton").removeAttr("disabled","disabled");
            }
    };
        /* This method is used to implement the sort functionality in the grid */
    this.gridsort = function (grid,data)
    {
        grid.onSort.subscribe(function (e, args) {
              var cols = args.sortCols;
              data.sort(function (dataRow1, dataRow2) {
                for (var i = 0, l = cols.length; i < l; i++) {
                  var field = cols[i].sortCol.field;
                  var sign = cols[i].sortAsc ? 1 : -1;
                  var value1 = dataRow1[field], value2 = dataRow2[field];
                  var result = (value1 === value2 ? 0 : (value1 > value2 ? 1 : -1)) * sign;
                  if (result !== 0) {
                    return result;
                  }
                }
                return 0;
              });
              grid.invalidate();
              grid.render();
            });
        CONSTANTS.retailStaffingObj.registeringFutureevents();
    };
        /*
         * This method is used to get the current state of the page that is to
         * be stored before navigating to other screens so as to display the
         * prev selected data when the user navigates back to the home screen
         */
    this.getCurrentStateInfo = function () {
        CONSTANTS.resetHomeValue = {};
        CONSTANTS.resetHomeValue.searchType= $("input[type='radio'][name='searchType']:checked").attr("id");
        var tempSearchType = CONSTANTS.resetHomeValue.searchType;
        if(tempSearchType === "candidate"){
            CONSTANTS.resetHomeValue.selectedSubCandidate = $("input[type='radio'][name='CandidateSearchType']:checked").attr("id");
        }
        else if(tempSearchType === "workQueuesResquistions"){
            CONSTANTS.resetHomeValue.subWorkQueuesSearchType = $("input[type='radio'][name='WorkQueuesSearchType']:checked").attr("id");
            var subWorkQueuesSearchType = CONSTANTS.resetHomeValue.subWorkQueuesSearchType;
            if(subWorkQueuesSearchType === "WorkQueuesStore")
                {
                CONSTANTS.resetHomeValue.WorkQueuesStoreValue = $("#WorkQueuesStoreValue").val();
                }
            else if(subWorkQueuesSearchType === "WorkQueuesDistrict")
            {
                CONSTANTS.resetHomeValue.WorkQueuesDistrictValue = $("#WorkQueuesDistrictValue").val();
            }
            else if(subWorkQueuesSearchType === "WorkQueuesRegion")
            {
                CONSTANTS.resetHomeValue.WorkQueuesRegionValue = $("#WorkQueuesRegionValue").val();
            }
            else if(subWorkQueuesSearchType === "WorkQueuesDivision")
            {
                CONSTANTS.resetHomeValue.WorkQueuesDivisionValue = $("#WorkQueuesDivisionValue").val();
            }
            CONSTANTS.resetHomeValue.WorkQueuesProcess = $("#WorkQueuesProcess").val();
        }
    };
        /*
         * Once the user navigates back to the home screen the prev details
         * entered by the user is being loaded with the help of this method
         */
    this.setCurrentStateInfo = function () {
        var tempSearchType = CONSTANTS.resetHomeValue.searchType;
        $('#'+tempSearchType+'').click();
        if(tempSearchType === 'candidate') {
            var selectedSubCandidate = CONSTANTS.resetHomeValue.selectedSubCandidate;
            $('#'+selectedSubCandidate+'').click();
        }
        else if(tempSearchType === 'workQueuesResquistions') {
            var subWorkQueuesSearchType = CONSTANTS.resetHomeValue.subWorkQueuesSearchType;
            $('#'+subWorkQueuesSearchType+'').click();
            if(subWorkQueuesSearchType === "WorkQueuesStore")
                {
                $("#WorkQueuesStoreValue").val(CONSTANTS.resetHomeValue.WorkQueuesStoreValue);
                }
            else if(subWorkQueuesSearchType === "WorkQueuesDistrict")
            {
                $("#WorkQueuesDistrictValue option[value='"+CONSTANTS.resetHomeValue.WorkQueuesDistrictValue+"']").attr("selected","selected");
            }
            else if(subWorkQueuesSearchType === "WorkQueuesRegion")
            {
                $("#WorkQueuesRegionValue option[value='"+CONSTANTS.resetHomeValue.WorkQueuesRegionValue+"']").attr("selected","selected");
            }
            else if(subWorkQueuesSearchType === "WorkQueuesDivision")
            {
                $("#WorkQueuesDivisionValue option[value='"+CONSTANTS.resetHomeValue.WorkQueuesDivisionValue+"']").attr("selected","selected");
            }
            $("#WorkQueuesProcess option[value='"+CONSTANTS.resetHomeValue.WorkQueuesProcess+"']").attr("selected","selected");
            $("#WorkQueuesProcess").removeAttr("disabled","disabled");
            $("#WorkQueuesDistrictValue").data("selectBox-selectBoxIt").refresh();
            $("#WorkQueuesRegionValue").data("selectBox-selectBoxIt").refresh();
            $("#WorkQueuesDivisionValue").data("selectBox-selectBoxIt").refresh();
            $("#WorkQueuesProcess").data("selectBox-selectBoxIt").refresh();
        }
        $(".btn.btn-green").removeAttr("disabled","disabled");
        this.resetAllHomeValue();
        if (CONSTANTS.SUCCESS_ON_HOME) {
            CONSTANTS.SUCCESS_ON_HOME = false;
            $("#reqdetnav").removeClass("blurBody");
            this.popup.alert("Details Saved Successfully");
        }
    };
        /* This methos is used to reset all the values in the home screen */
    this.resetAllHomeValue = function () {
        CONSTANTS.resetHomeValue.searchType = "";
        CONSTANTS.resetHomeValue.reqValue = "";
        CONSTANTS.resetHomeValue.selectedSubCandidate = "";
        CONSTANTS.resetHomeValue.candidateValue = "";
        CONSTANTS.resetHomeValue.candidateSSNValue = "";
        CONSTANTS.resetHomeValue.candidateLastNameValue = "";
        CONSTANTS.resetHomeValue.phoneScreenValue = "";
        CONSTANTS.resetHomeValue.interviewScreenValue = "";
        CONSTANTS.resetHomeValue.subWorkQueuesSearchType = "";
        CONSTANTS.resetHomeValue.WorkQueuesStoreValue = "";
        CONSTANTS.resetHomeValue.WorkQueuesDistrictValue = "";
        CONSTANTS.resetHomeValue.WorkQueuesRegionValue = "";
        CONSTANTS.resetHomeValue.WorkQueuesDivisionValue = "";
        CONSTANTS.resetHomeValue.WorkQueuesProcess = "";
    };
        /*
         * This method is invoked on the click of the cadaidate number hyper
         * link by which the user will be navigated to the candidate details
         * screen
         */
    this.navigateToCandidateDetails = function () {
        CONSTANTS.retailStaffingObj.getCurrentStateInfo();
        $.get('app/RetailStaffing/view/candidateDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
        /*
         * This method is invoked on the click of the review phone screen number
         * hyper link by which the user will be navigated to the review phone
         * screen details page
         */
    this.navigateToReviewCompletedPhoneScreenDetails = function (e) {
        CONSTANTS.retailStaffingObj.getCurrentStateInfo();
        CONSTANTS.retailStaffingObj.phoneScreenDataValue = $(e.currentTarget).text();
        $.get('app/RetailStaffing/view/reviewPhoneScreens.html', CONSTANTS.retailStaffingObj.setContent);
    };
        /* This method is used to replace the dom and load the page as required */
    this.setContent = function (data) {
        $("#divLandingView").html(data);
    };
        /*
         * This method is invoked on the click of the phone screen number in the
         * grid so as to navidate to the phone screeen details screen
         */
    this.navigateToPhoneScreenDetails = function (e) {
        CONSTANTS.retailStaffingObj.getCurrentStateInfo();
        CONSTANTS.retailStaffingObj.phoneScreenNumber = $(e.currentTarget).text();
        CONSTANTS.CallType = "PHNSCRN";
        CONSTANTS.isEnableIntrwScrnWarnPop = true;
        CONSTANTS.LoadITIDGDltls = [{
            "itiNbr":$(e.currentTarget).text()
        }];
        $.get('app/RSAPhoneScreenDetails/view/phoneScreenDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
        /*
         * This method is invoked on the click of the req number hyper link on
         * which the user will be navigated to the requisition details screen
         */
    this.navigateToReqScreenDetails = function (e) {
        CONSTANTS.retailStaffingObj.getCurrentStateInfo();
        CONSTANTS.retailStaffingObj.reqNumber = $(e.currentTarget).text();
        CONSTANTS.calledFromSearchPage = true;
        UTILITY.clearCache();
        $.get('app/RSARequisitionDetails/view/requisitionDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
        /*
         * This method is invoked on the click of the interview number hyper link on
         * which the user will be navigated to the interview details screen
         */
    this.navigateToInterviewScreenDetails = function (e) {
        CONSTANTS.retailStaffingObj.getCurrentStateInfo();
        CONSTANTS.retailStaffingObj.itiNumber = $(e.currentTarget).text();
        CONSTANTS.CallType = "INTSCHED";
        CONSTANTS.isEnableIntrwScrnWarnPop = true;
        CONSTANTS.LoadINTVIEWSRCHRSLTS = [{
            "itiNbr":$(e.currentTarget).text()
        }];
        $.get('app/RSAInterviewScreenDetails/view/interviewScreenDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
    /* This method is used to replace the dom and load the page as required */
    this.setFullContent = function (data) {
        $("html").html(data);
    };
};

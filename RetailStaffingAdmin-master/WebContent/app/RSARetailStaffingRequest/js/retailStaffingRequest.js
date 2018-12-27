/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: retailStaffingRequest.js
 * Application: Retail Staffing Admin
 *
 */
function retailStaffingRequest() {
    // Variable declaration
    var currentScope = this;
    var titleText = $('#titleText');
    var hireMgrName = $('#hireMgrName');
    var managerPhoneNum = $('#managerPhoneNum');
    var hireMgrTtl = $('#hireMgrTtl');
    var managerUserId = $('#managerUserId');
    var strNum = $('#strNum');
    var nofP = $('#nofP');
    var numIntrViews = $('#numIntrViews');
    var fillDt = $('#fillDt');
    var candTypInt = $('#candTypInt');
    var candTypExt = $('#candTypExt');
    var tgtPay = $('#tgtPay');
    var rscManageYes = $('#rscManageYes');
    var rscManageNo = $('#rscManageNo');
    var rscSchdYes = $('#rscSchdYes');
    var rscSchdNo = $('#rscSchdNo');
    var hiringEvntYes = $('#hiringEvntYes');
    var hiringEvntNo = $('#hiringEvntNo');
    var rscToManageItem = $('#rscToManageItem');
    var rscToSchdItem = $('#rscToSchdItem');
    var hiringEventItem = $('#hiringEventItem');
    var calendarItem = $('#calendarItem');
    var interviewDuration = $('#interviewDuration');
    var interviewers = $('#interviewers');
    var requestorName = $('#requestorName');
    var requestorPhnNum = $('#requestorPhnNum');
    var requestorId = $('#requestorId');
    var calendarIemLbl = $('#calendarIemLbl');
    var addCalendarBtn = $('#addCalendarBtn');
    var jobTitle = $('#jobTtl');
    var deptNum = $('#deptNum');
    var addCalendarBtnHldr = $('.addCalendarHldr');
    var calendarCbo = $('#calendarCbo');
    $('.hiringMangerInfoHolder').css('display', 'none');

    /*
     * This method is used to call service on screen load call userprofile
     * service to get loggedin user details
     */
    this.initialize = function() {
        $("#messageBar").html("US Retail & MET Only");
        $('#ui-datepicker-div').hide();
        $('.modal-backdrop').empty().remove();
        this.popup = new rsaPopup();
        this.LangSklsDetail = [];
        CONSTANTS.deptDet = [];
        CONSTANTS.checkedArray = [];
        this.validRequestorPhnNum = true;
        this.validHireMgrPhnNum = true;
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.setUserProfileValues);
        RSASERVICES.getUserProfileDetailss("", callbackFunction, false, true, "Please wait...");
        CONSTANTS.rscSchdFlg = CONSTANTS.YES_VALUE;
        CONSTANTS.seaTempJobFlg = CONSTANTS.NO_VALUE;
        $('#submitModal').modal('hide');
        $('#alert').modal('hide');
        $('#alertPopupModal').modal('hide');
        $("#refreshMgrData").blur();
        $.get('app/RSARetailStaffingRequest/view/createHiringEventPopUp.html', function(data) {
            $(".retailStaffing .form-horizontal").append(data);
        });
        var days = [ "S", "M", "T", "W", "T", "F", "S" ];
        // Set datepicker date format mm/dd/yy
        $('#fillDt').datepicker({
            minDate : 0,
            beforeShow : this.styleDatePicker.bind(this),
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top",
            dayNamesMin : days
        });
        // Apply style to look alike flex
        $("#schdPrefDays").addClass("hide");
        $(".schdPrefTimes").addClass("hide");
        $(".slotsHeader").addClass("hide");
        $(".weekHeader").addClass("hide");
        $("#schdPrefDays").removeClass("show");
        $(".schdPrefTimes").removeClass("show");
        $(".slotsHeader").removeClass("show");
        $(".weekHeader").removeClass("show");
        $('#schdPref0').prop('checked', false);
        $('#schdPref1').prop('checked', false);
        $('#schdPref2').prop('checked', false);
        $('#schdPref3').prop('checked', false);
        $('#schdPref4').prop('checked', false);
        $('#schdPref5').prop('checked', false);
        $('#schdPref6').prop('checked', false);
        $('#schdPref7').prop('checked', false);
        $('#schdPref8').prop('checked', false);
        $("#intvwDur").selectBoxIt();
        $("#intvwDur").data("selectBox-selectBoxIt").refresh();
        this.addToolTip();
        $("#deptNum").selectBoxIt();
        $("#deptNum").data("selectBox-selectBoxIt").refresh();
        this.addToolTip();
        //$('span#deptNumSelectBoxItText').addClass("boldOption");
        $("#jobTtl").selectBoxIt();
        $("#jobTtl").data("selectBox-selectBoxIt").refresh();
        this.addToolTip();
        //$('span#jobTtlSelectBoxItText').addClass("boldOption");
    };
    /*
     * validate textbox on focus out
     */
    $("#nofP").focusout(function() {
        if (nofP.val() !== "" && parseInt(nofP.val()) > 0) {
            if (parseInt(nofP.val()) === 1) {
                numIntrViews.val(parseInt(nofP.val()) + parseInt("1"));
            } else {
                numIntrViews.val((parseInt(nofP.val()) + parseInt("2")));
            }
        }
    });
    /*
     * validate textbox on focus out of Number of interviewModal
     */
    $("#numIntrViews").focusout(function() {
        if (nofP.val() !== "" && parseInt(nofP.val()) > 0 && numIntrViews.val() !== "" && !CONSTANTS.requestedNumInterviewsAccepted) {
            if (parseInt(nofP.val()) === 1) {
                // check for n+1
                if (parseInt(numIntrViews.val()) > parseInt(nofP.val()) + 1) {
                    $("#noOfIntr").text(numIntrViews.val());
                    currentScope.promptStaticDraggable('#noOfIntrModal');
                }
            } else {
                // Check for n+2
                if (parseInt(numIntrViews.val()) > parseInt(nofP.val()) + 2) {
                    $("#noOfIntr").text(numIntrViews.val());
                    currentScope.promptStaticDraggable('#noOfIntrModal');
                }
            }
        }
    });

    this.setReqstdBool = function() {
        CONSTANTS.requestedNumInterviewsAccepted = false;
    };
    this.addToolTip = function() {
        var selectBox = $("select + span ul li a");
        for (var i=0;i<selectBox.length;i++)
        {
            if($(selectBox[i]).text().length > CONSTANTS.SELECT_BOX_MAX_LENGTH) {
                $(selectBox[i]).attr("title",$(selectBox[i]).text());
            }
        }
        var selectBox1 = $("select + span .selectboxit-text");
        for (i=0;i<selectBox1.length;i++)
        {
            if($(selectBox1[i]).text().length > CONSTANTS.SELECT_BOX_MAX_LENGTH) {
                $(selectBox1[i]).attr("title",$(selectBox1[i]).text());
            }
        }
    };
    $('.retailStaffing').on("change",'select',function(){
        currentScope.addToolTip();
    });
    /*
     * Date picker style format
     */
    this.styleDatePicker = function() {
        $('#ui-datepicker-div').removeClass(function() {
            return $('input').get(0).id;
        });
        $('#ui-datepicker-div').addClass("datepicker-flex");
    };
    /*
     * Apply style for interview popup
     */
    this.acceptValue = function() {
        $("#noOfIntrModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $("#noOfIntrModal").modal('hide');
        CONSTANTS.requestedNumInterviewsAccepted = true;
    };
    /*
     * Set focus on click of cancel button in popup
     */
    this.rejectValue = function() {
        $("#noOfIntrModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $("#noOfIntrModal").modal('hide');
        CONSTANTS.requestedNumInterviewsAccepted = false;
        $("#numIntrViews").focus().select();
    };
    /*
     * Set logged in userprofile details
     */
    this.setUserProfileValues = function(json) {
        $.unblockUI();
        CONSTANTS.userProfile = {};
        CONSTANTS.userProfile = json.UserProfileResponseDTO;
        if (CONSTANTS.userProfile && CONSTANTS.userProfile.firstName) {
            CONSTANTS.userName = "Welcome " + CONSTANTS.userProfile.firstName + " " + CONSTANTS.userProfile.lastName;
            CONSTANTS.phnUsrId = CONSTANTS.userProfile.userId;
            CONSTANTS.userEmail = CONSTANTS.userProfile.emailAddress;
            $("#userProfile").text(CONSTANTS.userName);
            $(".welcomeUser").text(CONSTANTS.userName);

        } else {
            CONSTANTS.userProfile = {};
            CONSTANTS.userName = "Guest";
            CONSTANTS.userProfile.firstName = "Test";
            CONSTANTS.userProfile.lastName = "User";
            CONSTANTS.userProfile.userId = "test1234";
        }
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(currentScope.loadRequestScrDtls);
        // Load details to UI
        RSASERVICES.loadRetailStaffingRequestScreen(callbackFunction, true, "Please wait...");
    };
    /*
     * Load response from service to UI
     */
    this.loadRequestScrDtlsRsponseHnd = function(json) {
        var resultList = [];
        currentScope.buildExpList(json);
        currentScope.buildLangList(json);
        if (json.Response.hasOwnProperty("StateDetailList")) {
            currentScope.buildStateDtlList(json);
        }

        if (json.Response.hasOwnProperty("AutoAttachJobTitleList") && (json.Response.AutoAttachJobTitleList)) {
            resultList = [];
            // check whether AutoAttachJobTitleList is an array
            // collection
            if (Array.isArray(json.Response.AutoAttachJobTitleList.AutoAttachJobTitles)) {
                resultList = json.Response.AutoAttachJobTitleList.AutoAttachJobTitles;
            }
            // AutoAttachJobTitleList is not an array collection
            else {
                if (json.Response.AutoAttachJobTitleList.hasOwnProperty("AutoAttachJobTitles")) {
                    resultList.push(json.Response.AutoAttachJobTitleList.AutoAttachJobTitles);
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                CONSTANTS.autoAttachJobTitleDet = resultList;
            }
        } else if (json.Response.hasOwnProperty("AutoAttachJobTitleList")) {
            resultList = [];
        }
        currentScope.setPageData();
    };
    /*
     * Build the details list from service response
     */
    this.buildStateDtlList = function(json) {
        var resultList = [];
        if (json.Response.StateDetailList) {
            // check whether StateDetailList is an array collection
            if (Array.isArray(json.Response.StateDetailList.StateDetail)) {
                resultList = json.Response.StateDetailList.StateDetail;
            }
            // StateDetailList is not an array collection
            else {
                if (json.Response.StateDetailList.hasOwnProperty("StateDetail")) {
                    resultList.push(json.Response.StateDetailList.StateDetail);
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                CONSTANTS.reqStateDet = resultList;
            }
        }
    };
    /*
     * Build Experience list from service response
     */
    this.buildExpList = function(json) {
        var reqExpLvl = [];
        var resultList = [];
        if (json.Response.hasOwnProperty("ExpLevelList") && json.Response.ExpLevelList) {
            if (Array.isArray(json.Response.ExpLevelList.ExpLevelDetail)) {
                resultList = json.Response.ExpLevelList.ExpLevelDetail;
            } else {
                if (json.Response.ExpLevelList.hasOwnProperty("ExpLevelDetail")) {
                    resultList.push(json.Response.ExpLevelList.ExpLevelDetail);
                }
            }

            if (resultList && resultList.length > 0) {
                reqExpLvl = resultList;
                // Set default text in dropdown
                reqExpLvl.splice(0, 0, {
                    'expLevelCode' : '--Select--',
                    'shortDesc' : '--Select--'
                });
                CONSTANTS.reqExpDet = reqExpLvl;
                $("#reqExp").selectBoxIt();
                $("#reqExp").data("selectBox-selectBoxIt").refresh();
                currentScope.addToolTip();
                currentScope.appendOptionToSelect('reqExp', 'append', CONSTANTS.reqExpDet, 'shortDesc', 'expLevelCode');
                $("#reqExp").data("selectBox-selectBoxIt").refresh();
                currentScope.addToolTip();
            }

        }
    };
    /*
     * Build Language list from service response
     */
    this.buildLangList = function(json) {
        var resultList = [];
        if (json.Response.hasOwnProperty("LangSklsList") && json.Response.LangSklsList) {
            // check whether LangSklsList is an array collection
            if (Array.isArray(json.Response.LangSklsList.LangSklsDetail)) {
                resultList = json.Response.LangSklsList.LangSklsDetail;
            }
            // LangSklsList is not an array collection
            else {
                if (json.Response.LangSklsList.hasOwnProperty("LangSklsDetail")) {
                    resultList.push(json.Response.LangSklsList.LangSklsDetail);
                }
            }
            // check whether the result list is having any value
            // object or not
            if (resultList && resultList.length > 0) {
                // iterating the result list to get each item and
                // set in to model locator
                CONSTANTS.languageSkills = resultList;
                $('.checkboxGrid').empty();
                $('.checkboxGrid').createCheckBoxGrid({
                    rows : 4,
                    labelsArray : CONSTANTS.languageSkills,
                    cols : 4,
                    onchange : currentScope.languageChanged
                });
            }
        }
    };
    /*
     * Load request Screen details
     */
    this.loadRequestScrDtls = function(json) {
        $.unblockUI();
        CONSTANTS.autoAttachJobTitleDet = [];
        CONSTANTS.languageSkills = [];
        CONSTANTS.reqExpDet = [];
        CONSTANTS.reqStateDet = [];

        if (json.Response.hasOwnProperty("status")) {
            if (json.Response.status === CONSTANTS.STATUS_SUCCESS) {
                currentScope.loadRequestScrDtlsRsponseHnd(json);
            } else if (json.Response.hasOwnProperty("error")) {
                currentScope.popup.alert(json.Response.error.errorMsg);
            }

        } else if (json.Response.hasOwnProperty("error")) {
            // Display warning message
            currentScope.popup.alert(json.Response.error.errorMsg);
        }
    };
    /*
     * Display logged in user information
     */
    this.setPageData = function() {
        $('#requestorName').text(CONSTANTS.userProfile.firstName + " " + CONSTANTS.userProfile.lastName);
        $('#requestorId').text(CONSTANTS.userProfile.userId);

    };
    /*
     * Get department list
     */
    this.getDepartmentList = function() {
        if ($('#strNum').val().length === 4) {
            CONSTANTS.deptDet = [];
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(currentScope.setDepartmentList);
            // service call
            RSASERVICES.getDeptsByStr(callbackFunction, $('#strNum').val(), true, "Please wait...");

        } else if (CONSTANTS.deptDet.length > 0) {
            CONSTANTS.deptDet = [];
            $("#deptNum").selectBoxIt();
            $("#deptNum").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            currentScope.appendOptionToSelect('deptNum', 'remove', CONSTANTS.deptDet);
            $("#deptNum").data("selectBox-selectBoxIt").refresh();
            CONSTANTS.jobTtlDet = [];
            $("#jobTtl").selectBoxIt();
            $("#jobTtl").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            currentScope.appendOptionToSelect('jobTtl', 'remove', CONSTANTS.jobTtlDet);
            $("#jobTtl").data("selectBox-selectBoxIt").refresh();
            jobTitle.attr('disabled', true);
            $('#jobTtl').attr('disabled', true);
            $("#jobTtl").data("selectBox-selectBoxIt").refresh();
            candTypExt.removeAttr('checked');
            candTypInt.removeAttr('checked');
            currentScope.setRSCToManageDefaults();
        }
        // Enable Store Hiring Event Calendar
        currentScope.EnableStoreHireEvntCalendars();
    };
    /*
     * Enable Store Hiring event calendar if hiring event flag is true
     */
    this.EnableStoreHireEvntCalendars = function() {
        CONSTANTS.hiringEvntFlg = $("input[name=hiringEvent]:checked").val();
        if ($('#strNum').val().length === 4) {
            if (CONSTANTS.hiringEvntFlg === CONSTANTS.YES_VALUE) {
                var callbackFunction = $.Callbacks('once');
                callbackFunction.add(currentScope.loadHiringEventDetails);
                RSASERVICES.getHiringEventCalendars(callbackFunction, $('#strNum').val(), true, "Please wait...");
                $("#calendarCbo").selectBoxIt();
                $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
                calendarCbo.attr('disabled', false);
                $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
                addCalendarBtn.attr('disabled', false);
            } else {
                // Get Store Calendars
                this.getStoreCalendars();
                $("#calendarCbo").selectBoxIt();
                $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
                calendarCbo.attr('disabled', false);
                $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
                addCalendarBtn.attr('disabled', false);
            }
        } else {
            $("#calendarCbo").selectBoxIt();
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.appendOptionToSelect('calendarCbo', 'remove', CONSTANTS.interviewScheduleCollection);
            calendarCbo.attr('disabled', true);
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            addCalendarBtn.attr('disabled', true);
        }
    };
    /*
     * Get Job tile list from service
     */
    this.GetJobTtlList = function() {
        this.setScreenDirty();
        if ($('#deptNum').val() !== "0") {
            CONSTANTS.jobTtlDet = [];
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(currentScope.loadJobTtlDtl);
            var param = {
                "JobTitleRequest" : {
                    "strNo" : strNum.val(),
                    "deptNo" : deptNum.val()
                }
            };
            // call service
            RSASERVICES.getJobTtlByStrByDept(callbackFunction, param, true, "Please wait...");
        } else {
            CONSTANTS.jobTtlDet = [];
            jobTitle.attr('disabled', true);
        }
    };
    /*
     * Load Job title details on success
     */
    this.loadJobTtlDtlSuccessHnd = function(json) {
        var resultList = [];
        if (json.Response.hasOwnProperty("JobTitleList")) {
            if (Array.isArray(json.Response.JobTitleList.JobTitleDetail)) {
                resultList = json.Response.JobTitleList.JobTitleDetail;
            } else {
                if (json.Response.JobTitleList.hasOwnProperty("JobTitleDetail")) {
                    resultList.push(json.Response.JobTitleList.JobTitleDetail);
                }
            }
            if (resultList && resultList.length > 0) {
                CONSTANTS.jobTtlDet = JSON.stringify(resultList);
                CONSTANTS.jobTtlDet = $.parseJSON(CONSTANTS.jobTtlDet);
                CONSTANTS.jobTtlDet.splice(0, 0, {
                    'jobTtlCode' : '0',
                    'shortDesc' : '--Select--'
                });
                $("#jobTtl").selectBoxIt();
                $("#jobTtl").data("selectBox-selectBoxIt").refresh();
                currentScope.addToolTip();
                $("#jobTtl").empty();
                // iterating the result list to get each item and
                // set in to model locator
                //$('span#jobTtlSelectBoxItText').removeClass("boldOption");
                currentScope.appendOptionToSelect('jobTtl', 'append', CONSTANTS.jobTtlDet, 'shortDesc', 'jobTtlCode');
                $("#jobTtl").data("selectBox-selectBoxIt").refresh();
                currentScope.addToolTip();
            }
        }
    };
    /*
     * load Job title details
     */
    this.loadJobTtlDtl = function(json) {
        $.unblockUI();
        if (json.Response.hasOwnProperty("status")) {
            if (json.Response.status === CONSTANTS.STATUS_SUCCESS) {
                currentScope.loadJobTtlDtlSuccessHnd(json);
            } else if (json.Response.hasOwnProperty("error")) {
                currentScope.popup.alert(json.Response.error.errorMsg);
            }
        } else if (json.Response.hasOwnProperty("error")) {
            currentScope.popup.alert(json.Response.error.errorMsg);
        }
    };
    /*
     * Set department list
     */
    this.setDepartmentListSuccessHnd = function(json) {
        var resultList = [];
        if (Array.isArray(json.Response.DepartmentList.DepartmentDetail)) {
            resultList = json.Response.DepartmentList.DepartmentDetail;
        } else {
            if (json.Response.DepartmentList.hasOwnProperty("DepartmentDetail")) {
                resultList.push(json.Response.DepartmentList.DepartmentDetail);
            }
        }
        // Set default value as select
        if (resultList && resultList.length > 0) {
            CONSTANTS.deptDet = JSON.stringify(resultList);
            CONSTANTS.deptDet = $.parseJSON(CONSTANTS.deptDet);
            CONSTANTS.deptDet.splice(0, 0, {
                'deptCode' : '0',
                'shortDesc' : '--Select--'
            });
        }
        if (json.Response.hasOwnProperty("LocationDetails")) {
            CONSTANTS.isRSCSupportedLocation = json.Response.LocationDetails.isRSCSupportedLocation;
            CONSTANTS.isRSCSupportedLocationException = json.Response.LocationDetails.isRSCSupportedLocationException;
        }
        deptNum.empty();
        $("#deptNum").selectBoxIt();
        $("#deptNum").data("selectBox-selectBoxIt").refresh();
        currentScope.addToolTip();
        //$('span#deptNumSelectBoxItText').removeClass("boldOption");
        currentScope.appendOptionToSelect('deptNum', 'append', CONSTANTS.deptDet, 'shortDesc', 'deptCode');
        $("#deptNum").data("selectBox-selectBoxIt").refresh();
        currentScope.addToolTip();
    };
    /*
     * Set department list
     */
    this.setDepartmentList = function(json) {
        $.unblockUI();
        if (json.Response.hasOwnProperty("status")) {
            if (json.Response.status === CONSTANTS.STATUS_SUCCESS) {
                currentScope.setDepartmentListSuccessHnd(json);
            } else if (json.Response.hasOwnProperty("error")) {
                currentScope.popup.alert(json.Response.error.errorMsg);
            }
        } else if (json.Response.hasOwnProperty("error")) {
            currentScope.popup.alert(json.Response.error.errorMsg);
        }

    };
    /*
     * Set Auto Attach
     */
    this.setAutoAttach = function() {
        CONSTANTS.isJobTitleAutoAttach = false;
        var isRSCintExtFlag = (CONSTANTS.rscManageFlg === CONSTANTS.YES_VALUE && ((candTypInt.is(':checked') && candTypExt.is(':checked')) || candTypExt.is(':checked')));
        for ( var i = 0; i < CONSTANTS.autoAttachJobTitleDet.length; i++) {
            if (jobTitle.val() === CONSTANTS.autoAttachJobTitleDet[i].aaJobTitle && deptNum.val().toString() === CONSTANTS.autoAttachJobTitleDet[i].aaDeptCd.toString() && isRSCintExtFlag) {
                CONSTANTS.isJobTitleAutoAttach = true;
                break;
            }
        }
        this.setScreenDirty();
    };
    /*
     * Set if internal checked by user
     */
    this.isInternalChecked = function() {
        rscManageNo.prop('checked', true);
        CONSTANTS.rscManageFlg = CONSTANTS.NO_VALUE;
        rscManageYes.attr('disabled', false);
        rscManageNo.attr('disabled', false);
        rscToManageItem.removeClass('invisible');
        rscSchdNo.prop('checked', true);
        CONSTANTS.rscSchdFlg = CONSTANTS.NO_VALUE;
        rscSchdYes.attr('disabled', true);
        rscSchdNo.attr('disabled', true);
        rscToSchdItem.addClass('invisible');
        hiringEventItem.addClass('invisible');
        hiringEvntNo.prop('checked', true);
        CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
        calendarItem.addClass('invisible');
        addCalendarBtnHldr.addClass('invisible');
        interviewDuration.addClass('invisible');
        interviewers.addClass('invisible');
        currentScope.hiringEventSelected();
        this.getStoreCalendars();
    };
    /*
     * Set if both internal and external checked by user
     */
    this.isNotIntExtChecked = function() {
        rscManageNo.prop('checked', true);
        CONSTANTS.rscManageFlg = CONSTANTS.NO_VALUE;
        rscManageYes.attr('disabled', true);
        rscManageNo.attr('disabled', true);
        rscToManageItem.addClass('invisible');
        rscSchdNo.prop('checked', true);
        CONSTANTS.rscSchdFlg = CONSTANTS.NO_VALUE;
        rscSchdYes.attr('disabled', true);
        rscSchdNo.attr('disabled', true);
        rscToSchdItem.addClass('invisible');
        hiringEventItem.addClass('invisible');
        hiringEvntNo.prop('checked', true);
        CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
        calendarItem.addClass('invisible');
        addCalendarBtnHldr.addClass('invisible');
        interviewDuration.addClass('invisible');
        interviewers.addClass('invisible');
    };
    /*
     * Set RSC to manage default RSC Supported location check if candidate type
     * external if RSC Supportedlocation exception check if candidate type
     * external
     */
    this.setRSCToManageDefaults = function() {
        if (CONSTANTS.isRSCSupportedLocation) {
            if (candTypExt.is(':checked')) {
                rscManageYes.prop('checked', true);
                CONSTANTS.rscManageFlg = CONSTANTS.YES_VALUE;
                rscManageYes.attr('disabled', false);
                rscManageNo.attr('disabled', false);
                rscToManageItem.removeClass('invisible');
                rscSchdYes.prop('checked', true);
                CONSTANTS.rscSchdFlg = CONSTANTS.YES_VALUE;
                rscSchdYes.attr('disabled', false);
                rscSchdNo.attr('disabled', false);
                rscToSchdItem.removeClass('invisible');
                hiringEventItem.removeClass('invisible');
                hiringEvntNo.prop('checked', true);
                CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
                calendarItem.removeClass('invisible');
                addCalendarBtnHldr.removeClass('invisible');
                interviewDuration.removeClass('invisible');
                interviewers.removeClass('invisible');
                currentScope.hiringEventSelected();
                this.getStoreCalendars();
            } else if (candTypInt.is(':checked')) {
                this.isInternalChecked();
            } else {
                this.isNotIntExtChecked();
            }
        }

        else if (CONSTANTS.isRSCSupportedLocationException) {
            if (candTypExt.is(':checked')) {
                rscManageNo.prop('checked', true);
                CONSTANTS.rscManageFlg = CONSTANTS.NO_VALUE;
                rscManageYes.attr('disabled', false);
                rscManageNo.attr('disabled', false);
                rscToManageItem.removeClass('invisible');
                rscSchdYes.prop('checked', true);
                CONSTANTS.rscSchdFlg = CONSTANTS.YES_VALUE;
                rscSchdYes.attr('disabled', true);
                rscSchdNo.attr('disabled', true);
                rscToSchdItem.addClass('invisible');
                hiringEventItem.addClass('invisible');
                hiringEvntNo.prop('checked', true);
                CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
                calendarItem.addClass('invisible');
                addCalendarBtnHldr.addClass('invisible');
                interviewDuration.addClass('invisible');
                interviewers.addClass('invisible');
                currentScope.hiringEventSelected();
                this.getStoreCalendars();
            } else if (candTypInt.is(':checked')) {
                this.isInternalChecked();
            } else {
                this.isNotIntExtChecked();
            }
        }

        else if (!CONSTANTS.isRSCSupportedLocation) {
            // Default RSC to Manage to No and Disable. Internal and External
            // works the same
            CONSTANTS.rscManageFlg = CONSTANTS.NO_VALUE;
            rscManageYes.attr('disabled', true);
            rscManageNo.attr('disabled', true);
            CONSTANTS.rscSchdFlg = CONSTANTS.NO_VALUE;
            rscToSchdItem.addClass('invisible');
            hiringEventItem.addClass('invisible');
            CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
            calendarItem.addClass('invisible');
            interviewDuration.addClass('invisible');
            interviewers.addClass('invisible');

            if (candTypExt.is(':checked') || candTypInt.is(':checked')) {
                // Candidate Type has been selected, Show RSC to Manage and
                // disable. Hide all other fields
                rscToManageItem.removeClass('invisible');
                rscManageNo.prop('checked', true);
            } else {
                // No Candidate Type selected, Hide RSC to Manage and all other
                // fields
                rscToManageItem.addClass('invisible');
            }
        }
    };
    /*
     * Load calendardetails Success
     */
    this.loadCalendarDtlsSuccessHnd = function(json) {
        var resultList = [];
        if (json.Response.hasOwnProperty("ScheduleDescriptionDetails")) {
            if (Array.isArray(json.Response.ScheduleDescList.ScheduleDescriptionDetails)) {
                resultList = json.Response.ScheduleDescList.ScheduleDescriptionDetails;
            } else {
                if (json.Response.DepartmentList.hasOwnProperty("ReqScrnDtlsPg")) {
                    resultList.push(json.Response.ScheduleDescList.ScheduleDescriptionDetails);
                }
            }
        }

        if (resultList && resultList.length > 0) {
            CONSTANTS.interviewScheduleCollection = resultList;
        }
    };
    /*
     * Load calendar details
     */
    this.loadCalendarDtls = function(json) {
        if (json.Response.hasOwnProperty("status")) {
            if (json.Response.status === CONSTANTS.STATUS_SUCCESS) {
                this.loadCalendarDtlsSuccessHnd(json);
            } else if (json.Response.hasOwnProperty("error")) {
                currentScope.popup.alert(json.Response.error.errorMsg);
            }
        } else if (json.Response.hasOwnProperty("error")) {
            currentScope.popup.alert(json.Response.error.errorMsg);
        }
    };
    /*
     * Load hiring event details Success
     */
    this.loadHiringEventDetailsSuccessHnd = function(json) {
        var resultList = [];
        CONSTANTS.interviewScheduleCollection = [];
        if (json.Response.hasOwnProperty("ScheduleDescList")) {
            if (Array.isArray(json.Response.ScheduleDescList.ScheduleDescriptionDetails)) {
                resultList = json.Response.ScheduleDescList.ScheduleDescriptionDetails;
            } else if (json.Response.ScheduleDescList.ScheduleDescriptionDetails) {
                resultList.push(json.Response.ScheduleDescList.ScheduleDescriptionDetails);

            }
        }

        if (resultList && resultList.length > 0) {

            CONSTANTS.interviewScheduleCollection = JSON.stringify(resultList);
            CONSTANTS.interviewScheduleCollection = $.parseJSON(CONSTANTS.interviewScheduleCollection);

            for ( var m = 0; m < CONSTANTS.interviewScheduleCollection.length; m++) {
                var eventDate = currentScope.convertStringToDate(CONSTANTS.interviewScheduleCollection[m].hireEventBeginDate);
                var today = new Date();
                // Enable interview schedule if event date is today date
                if (today < eventDate) {
                    CONSTANTS.interviewScheduleCollection[m].enabled = true;
                } else {
                    CONSTANTS.interviewScheduleCollection[m].enabled = false;
                }
            }
        }
        CONSTANTS.interviewScheduleCollection.splice(0, 0, {
            'requisitionCalendarDescription' : 'Select Hiring Event..',
            'requisitionCalendarId' : '0',
            'type' : "0",
            'enabled' : true
        });

        $("#calendarCbo").selectBoxIt();
        $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
        currentScope.addToolTip();
        calendarCbo.empty();

        currentScope.appendOptionToSelect('calendarCbo', 'append', CONSTANTS.interviewScheduleCollection, 'requisitionCalendarDescription', 'hireEventId');
        $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
        currentScope.addToolTip();
        
    };
    /*
     * Convert String to date
     */
    this.convertStringToDate = function(str) {
        var year = str.toString().substr(0, 4);
        var date = parseInt(str.toString().substr(8, 2));
        var mon = parseInt(str.toString().substr(5, 2)) - 1;
        return new Date(year, mon, date);
    };
    /*
     * Load hiring event details
     */
    this.loadHiringEventDetails = function(json) {
        $.unblockUI();
        if (json.Response.hasOwnProperty("status")) {
            if (json.Response.status === CONSTANTS.STATUS_SUCCESS) {
                currentScope.loadHiringEventDetailsSuccessHnd(json);
            } else if (json.Response.hasOwnProperty("error")) {
                currentScope.popup.alert(json.Response.error.errorMsg);
            }
        } else if (json.Response.hasOwnProperty("error")) {
            //Don't show Invalid Store Number message because it will be shown in the GetDeptsByStrCommand.as
            if (json.Response.error.errorMsg !== "The store number entered is invalid.") {
                currentScope.popup.alert(json.Response.error.errorMsg);
            }
            //Error fetching Store Calendars
            $("#calendarCbo").selectBoxIt();
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            currentScope.appendOptionToSelect('calendarCbo', 'remove', CONSTANTS.interviewScheduleCollection);
            calendarCbo.attr('disabled', true);
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            addCalendarBtn.attr('disabled', true);
        }
    };
    /*
     * Load Hiring event selected
     */
    this.hiringEventSelected = function() {
        CONSTANTS.hiringEvntFlg = $("input[name=hiringEvent]:checked").val();
        if (hiringEvntYes.is(':checked')) {
            calendarIemLbl.text('Attach to hiring Event:');
            addCalendarBtn.text('Create New Hiring Event');
        } else if (hiringEvntNo.is(':checked')) {
            calendarIemLbl.text('Attach to Calendar:');
            addCalendarBtn.text('Create New Calendar');
        }
    };
    /*
     * Load Hiring event options
     */
    this.hiringEventOptions = function() {
        this.hiringEventSelected();
        if (hiringEvntYes.prop('checked')) {
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.loadHiringEventDetails);
            RSASERVICES.getHiringEventCalendars(callbackFunction, $('#strNum').val(), true, "Please wait...");
        } else {
            this.getStoreCalendars();
        }
        this.setScreenDirty();

    };
    /*
     * Create new Store calendar
     */
    this.createNewStoreCalendar = function() {
        // Set Store Number
        CONSTANTS.storeNo = strNum.val();

        // Hiring Event is NO, so create a New Calendar
        if (CONSTANTS.hiringEvntFlg === CONSTANTS.NO_VALUE) {
            $('#addCalendarBtn').attr('href', '');
            $("#createCalendarModal").off("shown.bs.modal");
            $("#createCalendarModal").on("shown.bs.modal", function() {
                $("#CalendarNameModalText").val("");
                $("#CalendarNameModalText").focus().select();
            });
            currentScope.promptStaticDraggable('#createCalendarModal');
        }
        // Hiring Event is YES, so create a New Hiring Event
        else if (CONSTANTS.hiringEvntFlg === CONSTANTS.YES_VALUE) {
            // Clear Participating Stores List
            CONSTANTS.HiringEventStoreList = [];
            $('#createHiringEventPopUp').off("shown.bs.modal");
            $('#createHiringEventPopUp').on("shown.bs.modal", createHiringEvent.initialize.bind(createHiringEvent));
            $('#addCalendarBtn').attr('href', '#createHiringEventPopUp');
            $("#createHiringEventPopUp").modal({
                "backdrop" : "static",
                "keyboard" : false
            });
            $("#createHiringEventPopUp").draggable({
                handle : ".modal-header"
            });

        }
        this.setScreenDirty();
    };
    /*
     * Create Calendar calendar name should not begin with storenumber or MET
     * Display warning message if deviating from business
     */
    this.createCalendar = function() {
        CONSTANTS.calendarView = this;
        this.newCalendarName = $("#CalendarNameModalText").val();
        CONSTANTS.calendarView.calNameExist = false;
        var patt = new RegExp(/^met/i);
        var resPatern = patt.test(this.newCalendarName);
        if ($("#CalendarNameModalText").val() === "" || $("#CalendarNameModalText").val().trim().length <= 0) {
            currentScope.popup.alert("Please enter a calendar name to Create.");
        } else if (CONSTANTS.storeNo === this.newCalendarName.toString().substr(0, 4)) {
            currentScope.popup.alert("Calendar Name cannot begin with the Store Number.");
            $("#CalendarNameModalText").val("");
        } else if (resPatern) {
            currentScope.popup.alert("Calendar Name cannot begin with the letters MET.");
            $("#CalendarNameModalText").val("");
        } else {
            for ( var i = 0; i < CONSTANTS.calendarView.calendarDetails.length; i++) {
                if (this.newCalendarName === CONSTANTS.calendarView.calendarDetails[i].requisitionCalendarDescription) {
                    CONSTANTS.calendarView.calNameExist = true;

                    currentScope.popup.alert("Calendar Name: '" + this.newCalendarName + "' already exists. \n" + "Please choose another name.");
                    $("#CalendarNameModalText").val("");
                }
            }
            if (!CONSTANTS.calendarView.calNameExist) {
                var callbackFunction = $.Callbacks('once');
                callbackFunction.add(CONSTANTS.calendarView.createCalendarSuccess);
                // Create requisition calendar
                RSASERVICES.createRequisitionCalendar(callbackFunction, CONSTANTS.storeNo, this.newCalendarName, null, true, "Please wait...");
            }
        }
    };
    /*
     * this keypress for create new calendar text box validation
     */
    $("#CalendarNameModalText").keypress(function(e) {
        var temp = String.fromCharCode(e.which);
        if (!(/[0-9A-Za-z\-\,\'\(\)\_\. ]/.test(temp))) {
            return false;
        }
    });
    /*
     * Create on calendar success
     */
    this.createCalendarSuccess = function(json) {
        $.unblockUI();
        var schDescVo = {};
        currentScope.createBtnClickSuccess = true;
        $("#createCalendarModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $("#createCalendarModal").modal('hide');
        if (json.interviewResponse.hasOwnProperty("requisitionCalendarId")) {

            var calId = json.interviewResponse.requisitionCalendarId;
            var calType = CONSTANTS.SPECIAL_CALENDAR_TYPE;
            var calDesc = currentScope.newCalendarName;

            schDescVo = {};
            schDescVo.requisitionCalendarDescription = calDesc;
            schDescVo.requisitionCalendarId = calId;
            schDescVo.type = calType;
            CONSTANTS.interviewScheduleCollection.push(schDescVo);
            currentScope.calendarDetails.push(schDescVo);

            $("#calendarCbo").selectBoxIt();
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            calendarCbo.empty();
            currentScope.appendOptionToSelect('calendarCbo', 'append', CONSTANTS.interviewScheduleCollection, 'requisitionCalendarDescription', 'requisitionCalendarId');
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();

            // Select the Newly created Hiring Event
            for ( var m = 0; m < CONSTANTS.interviewScheduleCollection.length; m++) {
                if (calDesc === CONSTANTS.interviewScheduleCollection[m].requisitionCalendarDescription) {
                    $('#calendarCbo option:eq(' + (m) + ')').prop('selected', true);
                    $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
                    currentScope.addToolTip();
                    break;
                }
            }
            currentScope.popup.alert("Calendar '" + calDesc + "' added successfully.");
        } else if (json.interviewResponse.hasOwnProperty("error")) {
            currentScope.popup.alert("Error occured creating new calendar.  Error Type:" + json.interviewResponse.error.errorType + " Identifier:" + json.interviewResponse.error.componentIdentifier);
        } else {
            currentScope.popup.alert("Error occured creating new calendar.");
        }

    };
    /*
     * Add option to hiiring event
     */
    this.appendOptionToSelect = function(drpDownId, action, optionsArray, textKey, valueKey) {
        if (action === "append") {
            //$('#' + drpDownId).removeClass("boldOption");
            for ( var i = 0; i < optionsArray.length; i++) {
                var currentObj = optionsArray[i];
                $('#' + drpDownId).append($("<option>", {
                    text : currentObj[textKey],
                    value : currentObj[valueKey]
                }));
                if (valueKey === "hireEventId") {
                    currentObj.enabled ? '' : $('#' + drpDownId + ' option[value=' + currentObj[valueKey] + '] ').attr("disabled", "disabled");
                }
            }
            $('#' + drpDownId).attr('disabled', false);
        } else {
            $('#' + drpDownId).find('option').remove();
            $('#' + drpDownId).attr('disabled', true);
            if (drpDownId === "deptNum" || drpDownId === "calendarCbo") {
                //$('span#deptNumSelectBoxItText').addClass("boldOption");
                $('#' + drpDownId).append($("<option>", {
                    text : "Enter 4 Digit Store #...",
                    value : -1
                }));
            }
            if (drpDownId === "jobTtl") {
                //$('span#jobTtlSelectBoxItText').addClass("boldOption");
                $('#' + drpDownId).append($("<option>", {
                    text : "Select Department #...",
                    value : -1
                }));
            }
            $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
        }

    };
    /*
     * Show hiring event manager
     */
    this.showHiringManger = function(e) {
        if ($(e.currentTarget).is(':checked')) {
            $('.hiringMangerInfoHolder').css('display', 'block');
        }
       this.setScreenDirty();
    };
    /*
     * Hide hiring event manager
     */
    this.hideHiringManger = function(e) {
        if ($(e.currentTarget).is(':checked')) {
            $('.hiringMangerInfoHolder').css('display', 'none');
        }
       this.setScreenDirty();
    };
    /*
     * handle if language changed by user
     */
    this.languageChanged = function() {
        var length = $(".langSkills input:checked").length;
        var checkedArrayInputs = $(".langSkills input:checked");
        CONSTANTS.checkedArray = [];
        for ( var i = 0; i < length; i++) {
            if (checkedArrayInputs[i].value) {
                CONSTANTS.checkedArray.push(checkedArrayInputs[i].value);
            }
        }
    };
    /*
     * RSA Manage options clicked
     */
    this.rscManageOptionsCLicked = function() {
        this.setRSCToManageOptions();
        this.setAutoAttach();
    };
    /*
     * Candidate type selected
     */
    this.candidateTypeSelected = function() {
        this.setRSCToManageDefaults();
        this.setAutoAttach();
    };
    /*
     * Set RSC to manage option selected
     */
    this.setRSCToManageOptions = function() {
        CONSTANTS.rscManageFlg = $("input[name=rscMange]:checked").val();
        if (CONSTANTS.isRSCSupportedLocation) {

            if (candTypExt.is(':checked')) {
                if (CONSTANTS.rscManageFlg === CONSTANTS.YES_VALUE) {
                    CONSTANTS.rscSchdFlg = CONSTANTS.YES_VALUE;
                    rscToSchdItem.removeClass('invisible');
                    rscSchdYes.attr('disabled', false);
                    rscSchdNo.attr('disabled', false);
                    rscSchdYes.prop('checked', true);
                    hiringEventItem.removeClass('invisible');
                    CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
                    calendarItem.removeClass('invisible');
                    addCalendarBtnHldr.removeClass('invisible');
                    interviewDuration.removeClass('invisible');
                    interviewers.removeClass('invisible');
                } else {
                    rscToSchdItem.addClass('invisible');
                    rscToSchdItem.enabled = false;
                    rscSchdYes.attr('disabled', true);
                    rscSchdNo.attr('disabled', true);
                    rscSchdYes.prop('checked', true);
                    hiringEvntNo.prop('checked', true);
                    hiringEventItem.addClass('invisible');
                    CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
                    calendarItem.addClass('invisible');
                    addCalendarBtnHldr.addClass('invisible');
                    interviewDuration.addClass('invisible');
                    interviewers.addClass('invisible');
                    currentScope.hiringEventSelected();
                    this.getStoreCalendars();
                }
            } else if (candTypInt.is(':checked')) {
                this.isRSCIntChecked();
            }
        } else if (CONSTANTS.isRSCSupportedLocationException) {
            if (candTypExt.is(':checked')) {
                if (CONSTANTS.rscManageFlg === CONSTANTS.YES_VALUE) {
                    CONSTANTS.rscSchdFlg = CONSTANTS.YES_VALUE;
                    rscToSchdItem.removeClass('invisible');
                    rscSchdYes.attr('disabled', false);
                    rscSchdNo.attr('disabled', false);
                    hiringEventItem.removeClass('invisible');
                    CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
                    calendarItem.removeClass('invisible');
                    addCalendarBtnHldr.removeClass('invisible');
                    interviewDuration.removeClass('invisible');
                    interviewers.removeClass('invisible');
                } else {
                    rscToSchdItem.addClass('invisible');
                    rscSchdYes.attr('disabled', true);
                    rscSchdNo.attr('disabled', true);
                    hiringEventItem.addClass('invisible');
                    CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
                    calendarItem.addClass('invisible');
                    addCalendarBtnHldr.addClass('invisible');
                    interviewDuration.addClass('invisible');
                    interviewers.addClass('invisible');
                    currentScope.hiringEventSelected();
                    this.getStoreCalendars();
                }
            } else if ($('#candTypInt:checked').length > 0) {
                this.isRSCIntChecked();
            }
        }
    };
    /*
     * Is RSC Interview validated
     */
    this.isRSCIntChecked = function() {
        if (CONSTANTS.rscManageFlg === CONSTANTS.YES_VALUE) {
            rscToSchdItem.removeClass('invisible');
            rscSchdYes.attr('disabled', true);
            rscSchdNo.attr('disabled', true);
        } else {
            CONSTANTS.rscSchdFlg = CONSTANTS.NO_VALUE;
            rscToSchdItem.addClass('invisible');
            rscSchdYes.attr('disabled', true);
            rscSchdNo.attr('disabled', true);
        }
    };
    /*
     * Get Store calendar details
     */
    this.getStoreCalendars = function() {
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.loadCalendarEventDetails);
        RSASERVICES.getStoreCalendars(callbackFunction, $('#strNum').val(), true, "Please wait...");
    };
    /*
     * Load calendar event details
     */
    this.loadCalendarEventDetailsSuccessHnd = function(json) {
        var resultList = [];
        CONSTANTS.interviewScheduleCollection = [];
        if (json.Response.ScheduleDescList.hasOwnProperty("ScheduleDescriptionDetails")) {
            currentScope.calendarDetails = json.Response.ScheduleDescList.ScheduleDescriptionDetails;
            if (Array.isArray(json.Response.ScheduleDescList.ScheduleDescriptionDetails)) {
                resultList = json.Response.ScheduleDescList.ScheduleDescriptionDetails;
            } else if (json.Response.ScheduleDescList.ScheduleDescriptionDetails) {
                resultList.push(json.Response.ScheduleDescList.ScheduleDescriptionDetails);
            }

            if (resultList && resultList.length > 0) {
                // Add a Select Calendar Entry
                CONSTANTS.interviewScheduleCollection = JSON.stringify(resultList);
                CONSTANTS.interviewScheduleCollection = $.parseJSON(CONSTANTS.interviewScheduleCollection);
            }
            CONSTANTS.interviewScheduleCollection.splice(0, 0, {
                // Display select calendar as default
                'requisitionCalendarDescription' : 'Select Calendar..',
                'requisitionCalendarId' : '0',
                'type' : "0",
                'enabled' : true
            });
            $("#calendarCbo").selectBoxIt();
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            calendarCbo.empty();
            currentScope.appendOptionToSelect('calendarCbo', 'append', CONSTANTS.interviewScheduleCollection, 'requisitionCalendarDescription', 'requisitionCalendarId');
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();            

        }

    };
    /*
     * Load calendar event details
     */
    this.loadCalendarEventDetails = function(json) {
        $.unblockUI();
        CONSTANTS.interviewScheduleCollection = [];

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status")) {
            // Fill Calendar Combo
            if (json.Response.hasOwnProperty("ScheduleDescList") && (json.Response.ScheduleDescList)) {
                currentScope.loadCalendarEventDetailsSuccessHnd(json);
            } else if (json.Response.hasOwnProperty("error") && (json.Response.error.errorMsg !== "The store number entered is invalid.")) {
                // Don't show Invalid Store Number message because it will be
                // shown in the GetDeptsByStrCommand.as
                currentScope.popup.alert(json.Response.error.errorMsg);
            }
        } else if (json.Response.hasOwnProperty("error")) {
            //Don't show Invalid Store Number message because it will be shown in the GetDeptsByStrCommand
            if (json.Response.error.errorMsg !== "The store number entered is invalid.") {
                currentScope.popup.alert(json.Response.error.errorMsg);
            }
            //Error fetching Store Calendars
            $("#calendarCbo").selectBoxIt();
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            currentScope.appendOptionToSelect('calendarCbo', 'remove', CONSTANTS.interviewScheduleCollection);
            calendarCbo.attr('disabled', true);
            $("#calendarCbo").data("selectBox-selectBoxIt").refresh();
            currentScope.addToolTip();
            addCalendarBtn.attr('disabled', true);
        }
    };
    /*
     * Hide and show interview duration
     */
    this.hideShowIntvwDur = function() {
        // if rscSchdFlg is YES
        CONSTANTS.rscSchdFlg = $("input[name=rscSch]:checked").val();
        if (CONSTANTS.rscSchdFlg === CONSTANTS.YES_VALUE) {
            interviewDuration.removeClass('invisible');
            interviewers.removeClass('invisible');
            calendarItem.removeClass('invisible');
            addCalendarBtnHldr.removeClass('invisible');
            hiringEventItem.removeClass('invisible');
            addCalendarBtnHldr.removeClass('invisible');
        }
        // if rscSchdFlg is NO
        if (CONSTANTS.rscSchdFlg === CONSTANTS.NO_VALUE) {
            interviewDuration.addClass('invisible');
            interviewers.addClass('invisible');
            calendarItem.addClass('invisible');
            addCalendarBtnHldr.addClass('invisible');
            hiringEventItem.addClass('invisible');
            CONSTANTS.hiringEvntFlg = CONSTANTS.NO_VALUE;
            hiringEvntNo.prop('checked', true);
            currentScope.hiringEventSelected();
            this.getStoreCalendars();
        }
        this.setScreenDirty();
    };
    /*
     * Submit request
     */
    this.submitRequest = function() {
        currentScope.promptStaticDraggable('#submitModal');
    };
    /*
     * Display notification
     */
    this.showNotification = function(message) {
        $("#submitModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $('#submitModal').modal('hide');
        $("#alert .modal-body .alertModalBody").text(message);
        currentScope.promptStaticDraggable('#alert');
    };
    /*
     * Show Error popup
     */
    this.showErrorPopUp = function(message) {
        $("#submitModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $('#submitModal').modal('hide');
        $("#schdErrorModal .modal-body .schdErrorModalBody").text(message);
        currentScope.promptStaticDraggable('#schdErrorModal');
    };
    /*
     * In popup OK button clicked
     */
    this.warningOkClicked = function() {
        $("#alert").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $('#alert').modal('hide');

        // Requestor Title
        if (titleText.val() === "" || titleText.val().trim().length <= 0) {
            titleText.focus().select();
            return;
        }
        // Requestor Phone Number
        else if (requestorPhnNum.val() === "" || requestorPhnNum.val().trim().length <= 0 || !this.validRequestorPhnNum) {
            requestorPhnNum.focus().select();
            return;
        }
        // Is requestor the hiring mgr, if no check hiring mgr info
        else if ($("input[name=hiringEvent]:checked").val() === CONSTANTS.NO_VALUE) {
            currentScope.chkHiringMgrDtls();
        }
        // Store
        else {
            currentScope.splittedAlertOkClicked();
        }
    };
    /*
     * Split the content in warning popup
     */
    this.splittedAlertOkClicked = function() {
        if (strNum.val() === "" || strNum.val().trim().length <= 0) {
            strNum.focus().select();
            return;
        }
        // Department
        else if ($("#deptNum option:selected").index() < 1) {
            $("#deptNum").focus().select();
            return;
        }
        // Job Title
        else if ($("#jobTtl option:selected").index() < 1) {
            $("#jobTtl").focus().select();
            return;
        } else {
            currentScope.chkAlertOkClicked();
        }
    };
    /*
     * Check if ok button clicked
     */
    this.chkAlertOkClicked = function() {
        // Date Needed / Fill Date
        if (!fillDt.val() || fillDt.val().trim().length <= 0) {
            fillDt.focus().select();
            return;
        }
        // Target Pay
        else if (CONSTANTS.vResult.type === "INVALID") {
            tgtPay.focus().select();
            return;
        }
        // Number of positions
        else if (!nofP.val() || nofP.val().trim().length <= 0) {
            nofP.focus().select();
            return;
        } else {
            currentScope.chkSplittedAlertOkClicked();
        }
    };
    /*
     * Check if ok button clicked in splitted popup
     */
    this.chkSplittedAlertOkClicked = function() {
        if (nofP.val().trim().length > 0) {
            if (parseInt(nofP.val()) < 1) {
                nofP.focus().select();
                return;
            }
        }
        // Job Status
        else if (!$('#ptPosn:checked').length > 0 && !$('#ftPosn:checked').length > 0) {
            $('#ptPosn').focus().select();
            return;
        } else {
            currentScope.chkCandTypeIntDurAlert();
        }
    };
    /*
     * Check candidate type interview duration
     */
    this.chkCandTypeIntDurAlert = function() {
        // No need to set focus on Seasonal/Temp because one will alwasy be
        // selected
        // Candidate Type
        if (!$('#candTypInt:checked').length > 0 && !$('#candTypExt:checked').length > 0) {
            $('#candTypInt').focus().select();
            return;
        }
        // Interview Duration
        else if ($('#intvwDur option:selected').index() < 1) {
            $('#intvwDur').focus().select();
            return;
        } else {
            return;
        }
    };
    /*
     * check hiring manager details
     */
    this.chkHiringMgrDtls = function() {
        // Hire Mgr Name
        if (hireMgrName.val() === "" || hireMgrName.val().trim().length <= 0) {
            hireMgrName.focus().select();
            return;
        }
        // Hire Mgr Title
        else if (hireMgrTtl.val() === "" || hireMgrTtl.val().length <= 0) {
            hireMgrTtl.focus().select();
            return;
        }
        // Hire Mgr Phone Num
        else {
            currentScope.chkHiringMgrPhnId();
        }
    };
    /*
     * check hiring manager phone details
     */
    this.chkHiringMgrPhnId = function() {
        if (managerPhoneNum.val() === "" || managerPhoneNum.val().trim().length <= 0 || !this.validHireMgrPhnNum) {
            managerPhoneNum.focus().select();
            return;
        }
        // Hire Mgr User Id
        else if (managerUserId.val() === "" || managerUserId.val().trim().length <= 0) {
            managerUserId.focus().select();
            return;
        }
    };
    /*
     * Format target pay
     */
    this.formatTgtPay = function() {
        if(!$("#tgtPay").validateNumber()){
            $('[data-toggle="tooltip"]').tooltip();
        }
        CONSTANTS.vResult = {};
        CONSTANTS.vResult.type = "INVALID";
        if (tgtPay.val() && tgtPay.val() !== '') {
            var splittedTgtPay = tgtPay.val().toString().split('.');
            if (parseFloat(tgtPay.val()) <= 99.99 && parseFloat(tgtPay.val()) !== "NaN" && splittedTgtPay.length <= 2) {
                tgtPay.val(parseFloat($("#tgtPay").val()).toFixed(2));
                CONSTANTS.vResult.type = "VALID";
            }
        }
    };
    /*
     * Format phone number
     */
    this.formatPhnNo = function(input) {
        var pattern = /-/gi;

        if (input && input.trim().length > 0) {
            var str = input.toString().replace(pattern, "");

            var j = 0;
            var result = "";
            for ( var i = 0; i < str.length; i++, j++) {
                if (str.charAt(i) === '(' || str.charAt(i) === ')') {
                    // do nothing
                } else {
                    result = result + str.charAt(i);
                }
            }
            return result;
        } else {
            return input;
        }
    };
    /*
     * check timeslot
     */
    this.checkTimeSlots = function() {
        var numSlotsChecked = 0;

        if ($("#schdPref3").is(":checked")) {
            numSlotsChecked++;
        }
        if ($("#schdPref4").is(":checked")) {
            numSlotsChecked++;
        }
        if ($("#schdPref5").is(":checked")) {
            numSlotsChecked++;
        }
        if ($("#schdPref6").is(":checked")) {
            numSlotsChecked++;
        }
        if ($("#schdPref7").is(":checked")) {
            numSlotsChecked++;
        }
        if ($("#schdPref8").is(":checked")) {
            numSlotsChecked++;
        }

        return numSlotsChecked;
    };
    /*
     * Handle schedule preference check
     */
    $(".schedulePrefRow").on('click', 'input[type="radio"]', function() {
        if ($("input[name=schdPrefFlg]:checked").val() === CONSTANTS.YES_VALUE) {
            $("#schdPrefDays").addClass("show");
            $(".schdPrefTimes").addClass("show");
            $(".slotsHeader").addClass("show");
            $(".weekHeader").addClass("show");
            $("#schdPrefDays").removeClass("hide");
            $(".schdPrefTimes").removeClass("hide");
            $(".slotsHeader").removeClass("hide");
            $(".weekHeader").removeClass("hide");
        } else {
            $("#schdPrefDays").addClass("hide");
            $(".schdPrefTimes").addClass("hide");
            $(".slotsHeader").addClass("hide");
            $(".weekHeader").addClass("hide");
            $("#schdPrefDays").removeClass("show");
            $(".schdPrefTimes").removeClass("show");
            $(".slotsHeader").removeClass("show");
            $(".weekHeader").removeClass("show");
            $('#schdPref0').prop('checked', false);
            $('#schdPref1').prop('checked', false);
            $('#schdPref2').prop('checked', false);
            $('#schdPref3').prop('checked', false);
            $('#schdPref4').prop('checked', false);
            $('#schdPref5').prop('checked', false);
            $('#schdPref6').prop('checked', false);
            $('#schdPref7').prop('checked', false);
            $('#schdPref8').prop('checked', false);
        }
    });
    /*
     * Handle schedule preference value click
     */
    $(".schedulePrefRow").on('click', 'input[type="checkBox"]', function(event) {
        if (currentScope.checkTimeSlots() > 2) {
            $("#" + event.target.id).prop('checked', false);
            currentScope.showErrorPopUp(CONSTANTS.TIME_SLOT_MESSAGE);
        }
    });
    /*
     * Validate Hiring manager details 1.Validate Hiring manager Name 2.Hiring
     * Manager title 3.Hiring Manager phone number 4.Hiring Manager user Id
     * 5.Display warning message if it is not available
     */
    this.validateHiringMgrDtls = function() {
        var isReturn = false;
        // Hiring Mgr Name
        if (hireMgrName.val() === "" || hireMgrName.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Hiring Mgr Name");
            hireMgrName.focus().select();
            isReturn = true;
        }
        // Hiring Mgr Title
        else if (hireMgrTtl.val() === "" || hireMgrTtl.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Hiring Mgr Title");
            hireMgrTtl.focus().select();
            isReturn = true;
        }
        // Hiring Mgr Phone Num
        else if (managerPhoneNum.val() === "" || managerPhoneNum.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Hiring Mgr Phone Number");
            managerPhoneNum.focus().select();
            isReturn = true;
        } else {
            CONSTANTS.validateHireMgrPhnNo = this.formatPhnNo(managerPhoneNum.val());
            if (CONSTANTS.validateHireMgrPhnNo.length === 0 || CONSTANTS.validateHireMgrPhnNo.length === 10) {
                this.validHireMgrPhnNum = true;
            } else {
                this.validHireMgrPhnNum = false;
                this.showNotification("Please enter a valid Hiring Mgr Phone Number");
                managerPhoneNum.focus().select();
                isReturn = true;
            }
        }
        if (isReturn) {
            return isReturn;
        }
        // Hiring Mgr ID
        if (managerUserId.val() === "" || managerUserId.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Hiring Mgr User ID");
            managerUserId.focus().select();
            isReturn = true;
        } else {
            isReturn = this.validateReqFields();
        }
        return isReturn;
    };
    /*
     * Validate mandatory fields
     */
    this.validateReqFields = function() {
        var isReturn = false;
        // Store Number
        if (strNum.val() === "" || strNum.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Store #");
            strNum.focus().select();
            isReturn = true;
        } else if ($("#deptNum option:selected").index() < 1) {
            this.showNotification("Please Complete The Required Field, Select Department #");
            deptNum.focus().select();
            isReturn = true;
        }
        // Job Title
        else if ($("#jobTtl option:selected").index() < 1) {
            this.showNotification("Please Complete The Required Field, Select Job Title");
            $("#jobTtl").focus().select();
            isReturn = true;
        } else {
            isReturn = this.validateFillDtNoP();
        }
        return isReturn;
    };
    /*
     * Validate filled details
     */
    this.validateFillDtNoP = function() {
        var isReturn = false;
        if (!fillDt.val() || fillDt.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Date Needed");
            isReturn = true;
        }
        // Number of positions
        else if (nofP.val() === "" || nofP.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Number of Positions");
            $("#nofP").focus().select();
            isReturn = true;
        } else if ((nofP.val() !== "" || nofP.val().trim().length > 0) && (parseInt(nofP.val()) < 1)) {
            this.showNotification("Number of Positions must be greater than zero.");
            $("#nofP").focus().select();
            isReturn = true;
        } else {
            isReturn = this.validateTgtPayPtFtSchdPrf();
        }
        return isReturn;
    };
    this.chkDaySelection = function() {
        return ((!$("#schdPref0").is(":checked") && !$("#schdPref1").is(":checked") && !$("#schdPref2").is(":checked")) || this.checkTimeSlots() < 1);
    };
    /*
     * validate target pay
     */
    this.validateTgtPayPtFtSchdPrf = function() {
        var isReturn = false;
        if (tgtPay.val() && tgtPay.val() !== '' && CONSTANTS.vResult.type === "INVALID") {
            this.showNotification("Please Enter a Valid Target Pay Value");
            $("#tgtPay").focus().select();
            isReturn = true;
        }
        // Job Status
        else if (!$('#ptPosn:checked').length > 0 && !$('#ftPosn:checked').length > 0) {
            this.showNotification("Please Complete The Required Field, Job Status, Select PT and/or FT");
            $("#ptPosn").focus().select();
            isReturn = true;
        } else if (!$("input[name=schdPrefFlg]:checked").length > 0) {
            this.showNotification("Please Complete the Required Field, Schedule Preference Question.");
            isReturn = true;
        } else if (CONSTANTS.schdPrefFlg === CONSTANTS.YES_VALUE && this.chkDaySelection()) {
            this.showErrorPopUp(CONSTANTS.SCHD_TIME_SLOT_MESSAGE);
            isReturn = true;
        } else {
            isReturn = this.validateCandTypPhnVal();
        }
        return isReturn;
    };
    /*
     * Validate candidate type
     */
    this.validateCandTypPhnVal = function() {
        var isReturn = false;
        if (!$('#candTypInt:checked').length > 0 && !$('#candTypExt:checked').length > 0) {
            this.showNotification("Please Complete The Required Field, Candidate Type, Select Internal and/or External");
            isReturn = true;
        }
        return isReturn;
    };
    /*
     * Show alert before submit
     */
    this.showAlertsBeforeSubmit = function() {
        var isReturn = false;
        CONSTANTS.hiringEvntFlg = $("input[name=isHiringManager]:checked").val();
        CONSTANTS.schdPrefFlg = $("input[name=schdPrefFlg]:checked").val();
        this.formatTgtPay();
        // Requestor Title
        if (titleText.val() === "" || titleText.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Requestor Title");
            titleText.focus().select();
            isReturn = true;
        }
        // Requestor Phone Num
        else if (requestorPhnNum.val() === "" || requestorPhnNum.val().trim().length <= 0) {
            this.showNotification("Please Complete The Required Field, Requestor Phone Number");
            requestorPhnNum.focus().select();
            isReturn = true;
        } else {
            CONSTANTS.validateRequestPhnNo = this.formatPhnNo(requestorPhnNum.val());
            if (CONSTANTS.validateRequestPhnNo.length === 0 || CONSTANTS.validateRequestPhnNo.length === 10) {
                this.validRequestorPhnNum = true;
            } else {
                this.validRequestorPhnNum = false;
                this.showNotification("Please enter a valid Requestor Phone Number");
                requestorPhnNum.focus().select();
                isReturn = true;
            }
        }
        if (isReturn) {
            return isReturn;
        }
        // Is requestor the hiring mgr, if no check hiring mgr info
        if (CONSTANTS.hiringEvntFlg === CONSTANTS.NO_VALUE) {
            isReturn = this.validateHiringMgrDtls();
        } else {
            isReturn = this.validateReqFields();
        }
        return isReturn;

    };
    /*
     * Save request object for service call
     */
    this.saveRetStafReq = function() {
        var saveStaffingDetObj = {};
        CONSTANTS.hiringEvntFlg = $("input[name=isHiringManager]:checked").val();
        saveStaffingDetObj.hireByFlg = CONSTANTS.hiringEvntFlg === "YES" ? "Y" : "N";

        saveStaffingDetObj.requestorName = requestorName.text();
        saveStaffingDetObj.requestorTitle = titleText.val();
        saveStaffingDetObj.requestorId = requestorId.text();
        saveStaffingDetObj.requestorPhnNbr = CONSTANTS.validateRequestPhnNo;

        if (CONSTANTS.hiringEvntFlg === CONSTANTS.YES_VALUE) {
            saveStaffingDetObj.hrgMgrName = requestorName.text();
            saveStaffingDetObj.hrgMgrTtl = titleText.val();
            saveStaffingDetObj.hrgMgrPhn = CONSTANTS.validateRequestPhnNo;
            saveStaffingDetObj.hrgMgrId = requestorId.text();
        } else {
            saveStaffingDetObj.hrgMgrName = hireMgrName.val();
            saveStaffingDetObj.hrgMgrTtl = hireMgrTtl.val();
            saveStaffingDetObj.hrgMgrPhn = CONSTANTS.validateHireMgrPhnNo;
            saveStaffingDetObj.hrgMgrId = managerUserId.val();
        }

        saveStaffingDetObj.strNo = strNum.val();

        saveStaffingDetObj.deptNo = $('#deptNum').val();

        saveStaffingDetObj.jobTtlNbr = $('#jobTtl').val();
        var jobTtlDesc = (CONSTANTS.jobTtlDet[$("#jobTtl option:selected").index()].shortDesc).toString().substring((CONSTANTS.jobTtlDet[$("#jobTtl option:selected").index()].shortDesc).indexOf("-") + 1);
        saveStaffingDetObj.jobTtl = jobTtlDesc.trim();

        var tFillDt = fillDt.val().split("/");
        saveStaffingDetObj.fillDt = tFillDt;

        saveStaffingDetObj.targetPay = tgtPay.val();

        saveStaffingDetObj.numPositions = nofP.val();

        // Number of Interviews
        saveStaffingDetObj.requestedNbrIntvws = numIntrViews.val();

        saveStaffingDetObj.jobStatusPt = $("#ptPosn").is(":checked").toString();
        saveStaffingDetObj.jobStatusFt = $("#ftPosn").is(":checked").toString();

        // Seasonal / Temp Job
        CONSTANTS.seaTempJobFlg = $("input[name=seaTemp]:checked").val();
        if (CONSTANTS.seaTempJobFlg === CONSTANTS.YES_VALUE) {
            saveStaffingDetObj.sealTempJob = "Y";
        } else {
            saveStaffingDetObj.sealTempJob = "N";
        }

        return saveStaffingDetObj;
    };
    /*
     * Store schedule preference changes
     */
    this.storeSchdPrefStartTime = function(startTime1, startTime2) {
        // Get Start Times, there can be at most 2 Start Times. The if
        // statements are in order as they are on the page. Test each Start Time
        // check box and set startTimeX variables
        if ($("#schdPref3").is(":checked")) {
            startTime1 = 1;
        }
        if ($("#schdPref4").is(":checked")) {
            if (startTime1 === 0) {
                startTime1 = 2;
            } else {
                startTime2 = 2;
            }
        }
        if ($("#schdPref5").is(":checked")) {
            if (startTime1 === 0) {
                startTime1 = 3;
            } else {
                startTime2 = 3;
            }
        }
        if ($("#schdPref6").is(":checked")) {
            if (startTime1 === 0) {
                startTime1 = 4;
            } else {
                startTime2 = 4;
            }
        }
        if ($("#schdPref7").is(":checked")) {
            if (startTime1 === 0) {
                startTime1 = 5;
            } else {
                startTime2 = 5;
            }
        }
        var startTimeArr = [];
        startTimeArr.push(startTime1);
        startTimeArr.push(startTime2);

        return startTimeArr;
    };
    /*
     * SAve Return staff request schedule preference
     */
    this.saveRetStafReqSchdPref = function(schdPrefArr) {
        var saveStaffingDetObj = {};
        var startTime1 = 0;
        var startTime2 = 0;
        var codeDay = "";

        var startTimeArr = currentScope.storeSchdPrefStartTime(startTime1, startTime2);
        startTime1 = startTimeArr[0];
        startTime2 = startTimeArr[1];
        if ($("#schdPref8").is(":checked")) {
            if (startTime1 === 0) {
                startTime1 = 6;
            } else {
                startTime2 = 6;
            }
        }
        // Indexes 0-4 will hold startTime1 Weekdays, Indexes 5-9 will hold
        // startTimes2 Weekdays
        if ($("#schdPref0").is(":checked")) {
            for ( var i = 0; i < 5; i++) {
                // codeDay contains, 1st char is Schedule Pref Time, 2nd char is
                // day of week
                codeDay = startTime1.toString() + (i + 1).toString();
                schdPrefArr[i] = codeDay;

                if (startTime2 > 0) {
                    // codeDay contains, 1st char is Schedule Pref Time, 2nd
                    // char is day of week
                    codeDay = startTime2.toString() + (i + 1).toString();
                    schdPrefArr[i + 5] = codeDay;
                }
            }
        }
        // Index 10 will hold startTime1 Saturday, Index 11 will hold startTime2
        // Saturday
        if ($("#schdPref1").is(":checked")) {
            // codeDay contains, 1st char is Schedule Pref Time, 2nd char is day
            // of week
            codeDay = startTime1.toString() + (6).toString();
            schdPrefArr[10] = codeDay;

            if (startTime2 > 0) {
                // codeDay contains, 1st char is Schedule Pref Time, 2nd char is
                // day of week
                codeDay = startTime2.toString() + (6).toString();
                schdPrefArr[11] = codeDay;
            }
        }
        // Index 12 will hold startTime1 Sunday, Index 13 will hold startTime2
        // Sunday
        if ($("#schdPref2").is(":checked")) {
            // codeDay contains, 1st char is Schedule Pref Time, 2nd char is day
            // of week
            codeDay = startTime1.toString() + (7).toString();
            schdPrefArr[12] = codeDay;

            if (startTime2 > 0) {
                // codeDay contains, 1st char is Schedule Pref Time, 2nd char is
                // day of week
                codeDay = startTime2.toString() + (7).toString();
                schdPrefArr[13] = codeDay;
            }
        }

        saveStaffingDetObj.schdPref = schdPrefArr;
        return saveStaffingDetObj;
    };
    /*
     * Save staff request candidate type
     */
    this.saveRetStafReqCandTypRSC = function() {
        var saveStaffingDetObj = {};
        // Add Candidate Type to Qualified Pool Notes
        if ($('#candTypInt:checked').length > 0 && $('#candTypExt:checked').length > 0) {
            saveStaffingDetObj.qualPoolNts = "Candidate Type:Internal, External";
            saveStaffingDetObj.candidateType = "Internal, External";
        } else if ($('#candTypInt:checked').length > 0) {
            saveStaffingDetObj.qualPoolNts = "Candidate Type:Internal";
            saveStaffingDetObj.candidateType = "Internal";
        } else if ($('#candTypExt:checked').length > 0) {
            saveStaffingDetObj.qualPoolNts = "Candidate Type:External";
            saveStaffingDetObj.candidateType = "External";
        }
        
        //Removed per Cindy/Kelly Snow Part of SIR 8663 04/25/2016
        // Instructions, Notes, Etc... Add to Referral Notes
        /*var noteTxtTmp = currentScope.decodeSpecialCharacters($("#noteTxt").val());
        saveStaffingDetObj.Referals = noteTxtTmp;*/

        // Required Experience
        saveStaffingDetObj.desiredExp = $("#reqExp option:selected").index().toString();
        if ($("#reqExp option:selected").index() !== 0) {
            saveStaffingDetObj.desiredExpText = $("#reqExp option:selected").text();
        }

        // RSC to Manage Flag
        CONSTANTS.rscManageFlg = $("input[name=rscMange]:checked").val();
        CONSTANTS.rscSchdFlg = $("input[name=rscSch]:checked").val();
        if (CONSTANTS.rscManageFlg === CONSTANTS.YES_VALUE) {
            saveStaffingDetObj.rscToManageFlg = "Y";
        } else {
            saveStaffingDetObj.rscToManageFlg = "N";
            CONSTANTS.rscSchdFlg = CONSTANTS.NO_VALUE;
        }

        // RSC Schedule Interview Flag
        if (CONSTANTS.rscSchdFlg === CONSTANTS.YES_VALUE) {
            saveStaffingDetObj.rscToSchdFlg = "Y";
        } else {
            saveStaffingDetObj.rscToSchdFlg = "N";
        }
        return saveStaffingDetObj;
    };

    this.isIntExtChecked = function() {
        return (($('#candTypInt:checked').length > 0 && $('#candTypExt:checked').length > 0) || $('#candTypExt:checked').length > 0);
    };
    /*
     * say staff request extended
     */
    this.saveRetStafReqExtendObj = function(Obj) {
        var saveStaffingDetObj = {};
        // Interview Duration
        if (CONSTANTS.rscSchdFlg === CONSTANTS.YES_VALUE) {
            if ($('#intvwDur option:selected').index() < 1) {
                this.showNotification("Please Complete The Required Field, Select Interview Duration");
                return true;
            }
            saveStaffingDetObj.interviewDurtn = $('#intvwDur option:selected').val();
        }
        // Name(s) of Interviewer(s)
        // Added for Defect #3449 by p22o0mn for handling special characters
        var namesOfIntrwrs = currentScope.decodeSpecialCharacters($("#nameOfInterviewers").val());
        saveStaffingDetObj.namesOfInterviewers = namesOfIntrwrs;

        // User LDAP email address
        saveStaffingDetObj.userLdapEmailAddress = CONSTANTS.userEmail;

        // Added for Auto-Attach.
        // Has to be RSC To Manage Yes, Job Title is an Auto-Attach Job Title,
        // Internal/External or External Only
        if (CONSTANTS.rscManageFlg === CONSTANTS.YES_VALUE && CONSTANTS.isJobTitleAutoAttach && this.isIntExtChecked()) {
            saveStaffingDetObj.isAutoAttachReqn = true;
        } else {
            saveStaffingDetObj.isAutoAttachReqn = false;
        }

        CONSTANTS.requestedNumInterviewsAccepted = false;
        saveStaffingDetObj.schdPrefDetail = [];

        for ( var i1 = 0; i1 < Obj.schdPref.length; i1++) {
            if (Obj.schdPref[i1] !== 0) {
                saveStaffingDetObj.schdPrefDetail.push({
                    "daySegCd" : Obj.schdPref[i1].toString().substring(0, 1),
                    "wkDayNbr" : Obj.schdPref[i1].toString().substring(1, 2)
                });
            }
        }
        return saveStaffingDetObj;
    };
    /*
     * Save staff request scheduled preference
     */
    this.saveRetStafReqRscSchd = function() {
        var saveStaffingDetObj = {};
        // Calendar Selected
        CONSTANTS.hiringEvntFlg = $("input[name=hiringEvent]:checked").val();
        if (CONSTANTS.rscSchdFlg === CONSTANTS.YES_VALUE) {
            if ($('#calendarCbo option:selected').index() === 0) {
                if (CONSTANTS.hiringEvntFlg === CONSTANTS.YES_VALUE) {
                    this.showNotification("Please Complete The Required Field, Select Hiring Event");
                } else {
                    this.showNotification("Please Complete The Required Field, Select Calendar");
                }
                return true;
            }
            // Set Hiring Event Information
            if (CONSTANTS.hiringEvntFlg === CONSTANTS.YES_VALUE) {
                saveStaffingDetObj.hrgEvntFlg = "Y";
                saveStaffingDetObj.hrEvntId = $('#calendarCbo option:selected').val();
                saveStaffingDetObj.reqCalId = _.where(CONSTANTS.interviewScheduleCollection, { 'hireEventId': parseInt($('#calendarCbo option:selected').val())})[0].requisitionCalendarId;
            } else {
                saveStaffingDetObj.hrgEvntFlg = "N";
                saveStaffingDetObj.hrEvntId = "0";
                saveStaffingDetObj.reqCalId = $('#calendarCbo option:selected').val();
            }
        } else if (CONSTANTS.rscSchdFlg === CONSTANTS.NO_VALUE) {
            saveStaffingDetObj.reqCalId = "0";
            saveStaffingDetObj.hrgEvntFlg = "N";
            saveStaffingDetObj.hrEvntId = "0";
        }
        return saveStaffingDetObj;
    };

    this.chkBilingualPos = function(langArr) {
        return ($('#jobTtl').val().toString().substr(0,2) === "BL" && langArr.length === 0);
    };
    /*
     * format schedule preference array
     */
    this.formSchdPrefArr = function() {
        var i;
        var j;
        var schdPrefArr = [];
        for (i = 0; i < CONSTANTS.NUM_SCHE_PREF_ENTRIES; i++) {
            for (j = 0; j < CONSTANTS.NUM_DAYS_OF_WEEK; j++) {
                schdPrefArr.push(0);
            }
        }
        return schdPrefArr;
    };
    /*
     * handle ok button click
     */
    this.submitOkClicked = function() {
    	$("#submitModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
    	$("#submitModal").modal('hide');
        var saveStaffingDetObj = {};

        // Validate that required fields have been filled in and save to VO
        // Object
        var isAlertThrown = currentScope.showAlertsBeforeSubmit();
        if (isAlertThrown) {
            return;
        }

        saveStaffingDetObj = _.extend(saveStaffingDetObj, currentScope.saveRetStafReq());
        // Schedule Preference
        // When Schedule Preference question is Yes
        CONSTANTS.schdPrefFlg = $("input[name=schdPrefFlg]:checked").val();

        var schdPrefArr = [];

        // Load Array with zeros based on number of Schedule Preference Entries
        // times number of days in week
        schdPrefArr = this.formSchdPrefArr();
        saveStaffingDetObj = _.extend(saveStaffingDetObj, currentScope.saveRetStafReqSchdPref(schdPrefArr));

        // Language Skills
        var langArr = [];
        langArr = CONSTANTS.checkedArray;
        saveStaffingDetObj.languageSkls = "NULL";
        // Gather Language Skills and place on languageSkls object
        if (langArr.length > 0) {
            saveStaffingDetObj.languageSkls = langArr.join(", ");
        } else if (this.chkBilingualPos(langArr)) {
            this.showNotification("This is a Bilingual position, select at least one Language");
            return;
        }

        saveStaffingDetObj = _.extend(saveStaffingDetObj, currentScope.saveRetStafReqCandTypRSC());

        saveStaffingDetObj = _.extend(saveStaffingDetObj, currentScope.saveRetStafReqRscSchd());

        if ($.type(saveStaffingDetObj) === "boolean") {
            return;
        }

        saveStaffingDetObj = _.extend(saveStaffingDetObj, currentScope.saveRetStafReqExtendObj(saveStaffingDetObj));
        if ($.type(saveStaffingDetObj) === "boolean") {
            return;
        }
        CONSTANTS.submitReqScrnRequest = this.updateSubmitRequest(saveStaffingDetObj);
        var data = {
                data : JSON.stringify({
                    "UpdateStaffingRequest" : CONSTANTS.submitReqScrnRequest.UpdateStaffingRequest
                })
            };

        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(currentScope.submitRequestResult);
        RSASERVICES.submitReqScrnDtls(callbackFunction, data, true, "Please wait...");

    };
    /*
     * Update submit request
     */
    this.updateSubmitRequest = function(saveStaffingDetObj) {
        var submitReqScrnRequest = {
            "UpdateStaffingRequest" : {
                "reqCalId" : saveStaffingDetObj.reqCalId.toString(),
                "staffingDetails" : {
                    "requestorName" : saveStaffingDetObj.requestorName,
                    "requestorTitle" : saveStaffingDetObj.requestorTitle,
                    "requestorPhnNbr" : saveStaffingDetObj.requestorPhnNbr,
                    "requestorId" : saveStaffingDetObj.requestorId,
                    "hireByFlg" : saveStaffingDetObj.hireByFlg,
                    "hrgMgrName" : saveStaffingDetObj.hrgMgrName,
                    "hrgMgrTtl" : saveStaffingDetObj.hrgMgrTtl,
                    "hrgMgrPhn" : saveStaffingDetObj.hrgMgrPhn,
                    "hrgMgrId" : saveStaffingDetObj.hrgMgrId,
                    "strNo" : saveStaffingDetObj.strNo,
                    "deptNo" : saveStaffingDetObj.deptNo,
                    "jobTtlCd" : saveStaffingDetObj.jobTtlNbr,
                    "jobTitle" : saveStaffingDetObj.jobTtl,
                    "fillDt" : {
                        "month" : saveStaffingDetObj.fillDt[0],
                        "day" : saveStaffingDetObj.fillDt[1],
                        "year" : saveStaffingDetObj.fillDt[2]
                    },
                    "qualPoolNts" : saveStaffingDetObj.qualPoolNts,
                    "languageSkls" : saveStaffingDetObj.languageSkls,
                    "candidateType" : saveStaffingDetObj.candidateType,
                    "targetPay" : saveStaffingDetObj.targetPay,
                    "Referals" : saveStaffingDetObj.Referals,
                    "numPositions" : saveStaffingDetObj.numPositions,
                    "requestedNbrIntvws" : saveStaffingDetObj.requestedNbrIntvws,
                    "jobStatusPt" : saveStaffingDetObj.jobStatusPt,
                    "jobStatusFt" : saveStaffingDetObj.jobStatusFt,
                    "sealTempJob" : saveStaffingDetObj.sealTempJob,
                    "desiredExp" : saveStaffingDetObj.desiredExp,
                    "desiredExpText" : saveStaffingDetObj.desiredExpText,
                    "rscSchdFlg" : saveStaffingDetObj.rscToSchdFlg,
                    "rscManageFlg" : saveStaffingDetObj.rscToManageFlg,
                    "reqStatus" : CONSTANTS.DEFAULT_STATUS,
                    "interviewDurtn" : saveStaffingDetObj.interviewDurtn,
                    "namesOfInterviewers" : saveStaffingDetObj.namesOfInterviewers,
                    "userLdapEmailAddress" : saveStaffingDetObj.userLdapEmailAddress,
                    "hrgEvntFlg" : saveStaffingDetObj.hrgEvntFlg,
                    "hiringEventID" : saveStaffingDetObj.hrEvntId,
                    "isAutoAttachReqn" : saveStaffingDetObj.isAutoAttachReqn.toString()
                }
            }
        };
        if (saveStaffingDetObj.schdPrefDetail.length > 0) {
            submitReqScrnRequest.UpdateStaffingRequest.staffingDetails.schdPrefDetailsList = {
                "schdPrefDetail" : saveStaffingDetObj.schdPrefDetail
            };
        } else {
            submitReqScrnRequest.UpdateStaffingRequest.staffingDetails.schdPrefDetailsList = [{}];
        }
        return submitReqScrnRequest;
    };
    /*
     * Submit request result
     */
    this.submitRequestResult = function(data) {
        $.unblockUI();
        currentScope.requestParmsDt = [];
        if (data.Response.hasOwnProperty("status") && data.Response.status === CONSTANTS.STATUS_SUCCESS) {
            $("#submitModal").on("hide.bs.modal", function() {
                $("#header.header-orange-theme").removeClass("blurBody");
                $(".firstWhiteContent").removeClass("blurBody");
            });
            $("#submitModal").modal('hide');
            currentScope.requestParmsDt = data.Response.successMsg.split("|");
            if (CONSTANTS.submitReqScrnRequest.UpdateStaffingRequest.staffingDetails.rscSchdFlg === "Y") {
                if (CONSTANTS.submitReqScrnRequest.UpdateStaffingRequest.staffingDetails.hrgEvntFlg === "Y") {
                    $('#infoModal .submitMsg').empty();
                    $('#infoModal .submitMsg').append("<p>A new Hiring Event Requisition #" + currentScope.requestParmsDt[0] + " has been created in AIMS based on job and information entered.</p><p>Please make note of your requisition number: " + currentScope.requestParmsDt[0] + "</p><p>Click OK to close window, or CANCEL to enter another Requisition.</p>");
                    currentScope.promptStaticDraggable('#infoModal');
                } else {
                    $(".reqNbr").text(currentScope.requestParmsDt[0]);
                    currentScope.promptStaticDraggable('#infoCalModal');
                }
            } else {
                $('#infoModal .submitMsg').empty();
                $('#infoModal .submitMsg').append("<p>A new Requisition has been created in AIMS based on job and information entered.</p><p>Click OK to close window, or CANCEL to enter another Requisition.</p>");
                currentScope.promptStaticDraggable('#infoModal');
            }
        }

        if (data.Response.hasOwnProperty("error") && data.Response.error.hasOwnProperty("errorMsg")) {
        	$("#submitModal").on("hide.bs.modal", function() {
                $("#header.header-orange-theme").removeClass("blurBody");
                $(".firstWhiteContent").removeClass("blurBody");
            });
        	$("#submitModal").modal('hide');
            currentScope.popup.alert(data.Response.error.errorMsg);
        }
    };
    /*
     * Decode special characters
     */
    currentScope.decodeSpecialCharacters = function(textToFormat) {
        var AddInt;
        var part1Add;
        var part2Add;
        for ( var hrAdd = 0; hrAdd < textToFormat.length; hrAdd++) {
            AddInt = textToFormat.charCodeAt(hrAdd);
            if (!(AddInt >= 32 && AddInt <= 125)) {
                part1Add = textToFormat.toString().substr(0, hrAdd);
                part2Add = textToFormat.toString().substr(hrAdd + 1, textToFormat.length - 1);
                textToFormat = "";

                if (AddInt > 8214 && AddInt < 8218) {
                    textToFormat = part1Add + "&apos;" + part2Add;
                } else if (AddInt >= 8218 && AddInt < 8222) {
                    textToFormat = part1Add + "&quot;" + part2Add;
                } else {
                    textToFormat = part1Add + part2Add;
                }
            }
        }
        return textToFormat;
    };
    /*
     * Cancel submit
     */
    currentScope.cancelSubmit = function() {
        $("#infoModal").modal("hide");
        $("#infoModal").on("hidden.bs.modal", function() {
        window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + '/RetailStaffing/RetailStaffingRequest.html';
        });
    };
    currentScope.windowCloseFunc = function() {
        $("#infoModal").modal("hide");
        $("#infoModal").on("hidden.bs.modal", function() {
           window.close();
           window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + '/RetailStaffing/RetailStaffingRequest.html';
        });
    };
    currentScope.onCancelButtonOK = function() {
        $("#cancelUnsaveModal").modal("hide");
        $("#cancelUnsaveModal").on("hidden.bs.modal", function() {
           window.close();
        });
    };
    currentScope.onCancelButtonCancel = function() {
        $("#cancelUnsaveModal").modal("hide");
    };
    this.onCancelBtnClick = function() {
        if (CONSTANTS.isDirtyScreen)
        {
            currentScope.promptStaticDraggable('#cancelUnsaveModal');
        }
        else
        {
            window.close();
        }
    };
    $(".closeBtnReq").click(function() {
        currentScope.onCancelBtnClick();
    });
    this.setScreenDirty = function() {
        CONSTANTS.isDirtyScreen=true;
    };

    this.setContent = function(data) {
        $("#divLandingView").html(data);
    };

    /*
     * Remove backdrop blur and close alert popup on click of OK button.
     */
    this.alertOkClicked = function(id) {
        $(id).on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $(id).modal('hide');
    };
    /*
     * Redirect to calendar
     */
    currentScope.redirectToCalandar = function() {
        $("#infoCalModal").on("hide.bs.modal", function() {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $('#infoCalModal').modal('hide');
        window.location.href = window.location.protocol + "//" + window.location.hostname + ":" + window.location.port + '/StaffingForms/InterviewAvailability.html?sessionId=' + new Date().getTime() + '&reqNbr=' + currentScope.requestParmsDt[0] + '&strNo=' + currentScope.requestParmsDt[1] + '&calId=' + currentScope.requestParmsDt[2];
    };
    /*
     * create schedule preference
     */
    $('.schedulePrefRow').empty();
    $('.schedulePrefRow').createSchedulePreferenceGrid({
        colMd : "3",
        daysLabel : "Please indicate the specific days the candidate MUST be able to work:",
        slotsLabel : "Please indicate the specific time slots the candidate MUST be able to start work (2 selections maximum):",
        isMandatory : true,
        isRetailStaffingRequest : true
    });

    /*
     * make pop ups draggable and static
     */
    this.promptStaticDraggable = function(id) {
        $(id).modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $(id).draggable({
            handle : ".modal-header"
        });
        $(id).on("shown.bs.modal", function() {
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
            $(".alertmodalbtn").focus();
        });
        $(id).on("show.bs.modal", function() {
            $("#header.header-orange-theme").addClass("blurBody");
            $(".firstWhiteContent").addClass("blurBody");
        });
        $(id).on('hidden.bs.modal', function () {
            $("#header.header-orange-theme").removeClass("blurBody");
            $(".firstWhiteContent").removeClass("blurBody");
        });
        $(id).modal("show");
    };

};
 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: phoneScreenDetails.js
 * Application: Retail Staffing Admin
 *
 *this phoneScreenDetails.js is used to display the phone screen details of associate in UI
 */
function phoneScreenDetails() {
/*
 * This function is used to get phone screen details
 */
    this.getPhoneScreenDetails = function() {

        $("#messageBar").html("PHONE SCREEN DETAILS");
        CONSTANTS.phonescreenDetailsObj = this;
        this.model = {};
        this.popup = new rsaPopup();
        $('.modal-backdrop').empty().remove();
        //Declare weekdays
        var days = [ "S", "M", "T", "W", "T", "F", "S" ];
        this.isSubmitEmailValidation = false;
        //Set Datepicker
        $('#intDate').datepicker({
            beforeShow : this.styleDatePicker.bind(this),
            minDate : 0,
            dateFormat : 'mm/dd/yy',
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            direction : "top",
            dayNamesMin : days
        });
        this.blockFormElements(".phnScrnLangGrid");

        CONSTANTS.ConfirmationNo = CONSTANTS.LoadITIDGDltls[0].itiNbr;

        var callbackFunction = $.Callbacks('once');
        //Call phonescreendetails service to get details
        if ((CONSTANTS.CallType) && (CONSTANTS.CallType === "PHNSCRN")) {
            callbackFunction.add(this.displayValues.bind(this));
            RSASERVICES.getPhnScreenDetails(callbackFunction, CONSTANTS.ConfirmationNo, true, "Please wait...");
        } else {
            callbackFunction.add(this.displayValues.bind(this));
            RSASERVICES.getPhnScreenDetails(callbackFunction, CONSTANTS.LoadITIDGDltls[0].itiNbr, true, "Please wait...");
        }
    };
/*
 * Set style to datepicker to appear like flex component
 */
    this.styleDatePicker = function() {
        $('#ui-datepicker-div').removeClass(function() {
            return $('input').get(0).id;
        });
        $('#ui-datepicker-div').addClass("datepicker-flex");
    };
    /*
     * Set a landing page
     */
    this.setContent = function(data) {
        $("#divLandingView").html(data);
    };
    /*
     * Display the phone screen service details in UI
     */
    this.displayValues = function(response) {
        $.unblockUI();
        $('#ui-datepicker-div').hide();
        CONSTANTS.phonescreenDetailsObj.model = _.extend(CONSTANTS.phonescreenDetailsObj.model, new getPhnScrDtlsCmd(response));
        CONSTANTS.phonescreenDetailsObj.displayPhnScrnDetails(CONSTANTS.phnScreenDtls);
        CONSTANTS.phonescreenDetailsObj.displayRequisitionDetails(CONSTANTS.phnScreenReqDtls, CONSTANTS.phnScrnStaffingList, CONSTANTS.languageSkls);
        CONSTANTS.phonescreenDetailsObj.displayStoreLocDetails(CONSTANTS.phnScreenReqStoreDtls);
        CONSTANTS.phonescreenDetailsObj.displayPhnScrnresponses(CONSTANTS.phnScreenDtls, CONSTANTS.phnScreenDtls,
        // data.PhoneScreenDispositionList.PhoneScreenDispositionDetail
        CONSTANTS.phoneScrnStatusDtls);
        CONSTANTS.phonescreenDetailsObj.initApp();
        //Load Call History
        this.createCallHistoryGrid(CONSTANTS.callHistory);
    };
    
    /*
     * Initialise the phone screen details
     */
    this.initApp = function() {
        if ($("#scrnstatusSelect option:selected").index() === 5 || $("#scrnstatusSelect option:selected").index() === 6) {
            $("#intrstatusSelect").removeAttr("disabled");
            $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
        } else {
            $("#intrstatusSelect").attr("disabled", "disabled");
            $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
        }

        if ($("#minReq1 option:selected").index() !== 0) {
            // Added validation
            if (CONSTANTS.phnScreenDtls.length > 0) {
                if (CONSTANTS.phnScreenDtls[0].phnScreener && CONSTANTS.phnScreenDtls[0].phnScreener.toString().trim().length > 0) {
                    $(".screener").text(CONSTANTS.phnScreenDtls[0].phnScreener);
                }

                else {
                    $(".screener").text(CONSTANTS.userProfile.userId);
                }
            }
        } else {
            $(".screener").text("");
        }

        // RSA4.0 CTI_ScreenPopup
        // To call onshow() maually, we added the EventListener to load all data
        // in the Screen.
        if (CONSTANTS.CallType === "PHNSCRN") {
            CONSTANTS.phonescreenDetailsObj.showInit();
        }
        CONSTANTS.phonescreenDetailsObj.showInit();
    };
    /*
     * Set the controls on initial load
     */
    this.showInit = function() {
        CONSTANTS.phonescreenDetailsObj.setReminderText();
        CONSTANTS.phonescreenDetailsObj.setPhoneScnStatus();
        CONSTANTS.phonescreenDetailsObj.checkPhoneScreenStatusCode();
        if (CONSTANTS.getInterviewStatusDtls) {
            CONSTANTS.phonescreenDetailsObj.setInterviewStatus();
        }
        if (CONSTANTS.getPhoneScrnDispositionStatusDtls) {
            CONSTANTS.phonescreenDetailsObj.setPhoneScreenDispositionStatus();
        }
        CONSTANTS.phonescreenDetailsObj.getIntrwDtls();
        CONSTANTS.phonescreenDetailsObj.checkPhnScrnCompleted();
        CONSTANTS.phonescreenDetailsObj.setStrFieldsNotEditable();
        //Added for IVR Dec 2015 Changes
        CONSTANTS.phonescreenDetailsObj.setResetNeedAccommodation();
        CONSTANTS.phonescreenDetailsObj.setStatusOnOpen();
    };
    /*
     * Check job code if external \ internal and save accordingly
     */
  //  this.checkJobCdIntExt = function(jobCd, internal_External, jobCdVal, intExtVal) {
   //     return (jobCd === jobCdVal && internal_External === intExtVal);
   // };
    /*
     * Set job code based on user selection
     */
  /*  this.chkJobCd01 = function(jobCd, internal_External) {
        if (jobCd === 'HMDSUP') {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_AREA_SUPV_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'MRCHEA', 'INTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_INT_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'MRCHEA', 'EXTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_EXT_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'MEANT1', 'INTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_NTR_INT_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'MEANT1', 'EXTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_NTR_EXT_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'METNGT', 'INTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_NTR_INT_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'METNGT', 'EXTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_NTR_EXT_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'HMSSPT', 'INTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_INT_PHN_SCRN_MSG);
        } else if (CONSTANTS.phonescreenDetailsObj.checkJobCdIntExt(jobCd, internal_External, 'HMSSPT', 'EXTERNAL')) {
            $(".reminderText").text(CONSTANTS.MERCH_EXEC_ASSOC_EXT_PHN_SCRN_MSG);
        } else if (jobCd === 'LPASO') {
            $(".reminderText").text(CONSTANTS.ASSET_PROTECT_SPEC_PHN_SCRN_MSG);
        } else if (jobCd === 'HGWAII') {
            $(".reminderText").text(CONSTANTS.GENERAL_WARE_ASSOC_II_PHN_SCRN_MSG);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd02(jobCd, internal_External);
        }
    };*/
    /*
     * Check job code as per the warehouse message selected
     */
    /*this.chkJobCd02 = function(jobCd, internal_External) {
        if (jobCd === 'HTFASC' || jobCd === 'HWHASO' || jobCd === 'HMAINT') {
            $(".reminderText").text(CONSTANTS.GENERAL_WAREHOUSE_ASSOC_I);
        } else if (jobCd === 'HDCHRC') {
            $(".reminderText").text(CONSTANTS.HR_COORDINATOR);
        } else if (jobCd === 'HADMDC') {
            $(".reminderText").text(CONSTANTS.DC_ADMIN_ASSISTANT);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd03(jobCd, internal_External);
        }
    };*/
    /*
     *  Check job code as per the warehouse message selected
     *  1.HR coordinator
     *  2.District Admin Assistant
     *  3.Office assistant
     */
    /*this.chkJobCd03 = function(jobCd, internal_External) {
        if (jobCd === 'OFFICE' || jobCd === 'HDCSCR') {
            $(".reminderText").text(CONSTANTS.GENERAL_OFFICE_ASSOCIATE);
        } else if (jobCd === 'HINVCL' || jobCd === 'HTRANS') {
            $(".reminderText").text(CONSTANTS.GENERAL_WAREHOUSE_ASSOC_I);
        } else if (jobCd === 'HCSSRP') {
            $(".reminderText").text(CONSTANTS.DETAILED_PHN_SCRN_MSG);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd04(jobCd, internal_External);
        }
    };*/
    /*
     *  Check job code as per the warehouse message selected
     *  1.Detailed Phone screen
     *  2.Switcher Driver
     *  3.RLC Repair technician
     */
    /*this.chkJobCd04 = function(jobCd, internal_External) {
        if (jobCd === 'HSWHDR') {
            $(".reminderText").text(CONSTANTS.SWITCHER_DRIVER);
        } else if (jobCd === 'HRTOLT' || jobCd === 'HRMSTH') {
            $(".reminderText").text(CONSTANTS.RLC_REPAIR_TECH);
        } else if (jobCd === 'HRRSPT') {
            $(".reminderText").text(CONSTANTS.GENERAL_OFFICE_ASSOCIATE);
        } else if (jobCd === 'HRPTSP') {
            $(".reminderText").text(CONSTANTS.RLC_PARTS_SPECIALIST);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd05(jobCd, internal_External);
        }
    };*/
    /*
     *  Check job code as per the warehouse message selected
     *  1.RLC parts specialist
     *  2.Outbound coordinator
     *  3.Tool rental service technician
     *  4.RLC inventory control associate
     *  5.PRO paint specialist
     */
    /*this.chkJobCd05 = function(jobCd, internal_External) {
        if (jobCd === 'HASPRA') {
            $(".reminderText").text(CONSTANTS.GENERAL_WAREHOUSE_ASSOC_I);
        } else if (jobCd === 'HOBCOR') {
            $(".reminderText").text(CONSTANTS.OUTBOUND_COORDINATOR);
        } else if (jobCd === 'TECH') {
            $(".reminderText").text(
                    CONSTANTS.TOOL_RENTAL_SERVICE_TECHNICIAN);
        } else if (jobCd === 'HRLIVC') {
            $(".reminderText").text(
                    CONSTANTS.RLC_INVENTORY_CONTROL_ASSOCIATE);
        } else if (jobCd === 'HPPSPT') {
            $(".reminderText").text(CONSTANTS.PRO_PAINT_SPECIALIST);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd06(jobCd, internal_External);
        }
    };*/
    /*
     *  Check job code as per the warehouse message selected
     *  1.MET Supervisior
     *  2.Department supervisor phone screen message
     */
    /*this.chkJobCd06 = function(jobCd, internal_External) {
        if (jobCd === 'MNTSUP' || jobCd === 'MONSUP' ) {
            $(".reminderText").text(CONSTANTS.MET_SUPER_NO_TRAVEL);
        } else if (jobCd === 'FRTMLD' || CONSTANTS.isSuper) {
            $(".reminderText").text(CONSTANTS.DEPT_SUPER_PHN_SCRN_MSG);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd07(jobCd, internal_External);
        }
    };*/
    /*
     *  Check job code as per the warehouse message selected
     *  1.Freight location
     */
    /*this.chkJobCd07 = function(jobCd, internal_External) {
        if (jobCd === 'MNTSUP' || jobCd === 'MONSUP') {
            $(".reminderText").text(CONSTANTS.MET_SUPER_NO_TRAVEL);
        } else if (jobCd === 'FRTMLD') {
            $(".reminderText").text(CONSTANTS.DEPT_SUPER_PHN_SCRN_MSG);
        } else if (jobCd === 'HRLGOA') {
            $(".reminderText").text(CONSTANTS.GENERAL_OFFICE_ASSOCIATE);
        } else if (jobCd === 'HRFTCH') {
            $(".reminderText").text(CONSTANTS.RLC_REPAIR_TECH);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd08(jobCd, internal_External);
        }
    };*/
    /*
     *  Check job code as per the warehouse message selected
     *  1.Internal phone screen message
     *  Added Cashier and Pro Cashier per Cindy Dec 2015
     */
    /*this.chkJobCd08 = function(jobCd, internal_External) {
        var splitCondition = (jobCd === 'FFTMAS' || jobCd === 'FFTSSA'
            || jobCd === 'FRTTM' || jobCd === 'LOT');
        if (splitCondition || jobCd === 'PLOT'
            || jobCd === 'BLLOT') {
            $(".reminderText").text(CONSTANTS.FREIGHT_LOT);
        } else if (jobCd === 'CASHR' || jobCd === 'PRCSHR') {
            $(".reminderText").text(CONSTANTS.CASHIER_PHN_SCRN_MSG);
        } else if (jobCd === 'ASDSUP') {
                $(".reminderText").text(CONSTANTS.ASDS_PHN_SCRN_MSG);
        } else if (jobCd === 'PASALS') {
                $(".reminderText").text(CONSTANTS.PRO_ACCT_SALES_PHN_SCRN_MSG);                
        } else if (jobCd === 'DESIGN') {
                $(".reminderText").text(CONSTANTS.DESIGNER_PHN_SCRN_MSG);        
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd09(jobCd, internal_External);
        }
    };*/
    
    /*
     * Interline Job Codes
     */
    /*this.chkJobCd09 = function(jobCd, internal_External) {
        if (jobCd === 'IBDWLC' || jobCd === 'IBFABL' || jobCd === 'IBIVCL' || jobCd === 'IBQCLD' || jobCd === 'IBSSUP' || jobCd === "IBHSUP") {
            $(".reminderText").text(CONSTANTS.IB_DC_DEPT_SUP);
        } else if (jobCd === 'IBDCAA' || jobCd === 'IBGOFA' || jobCd === 'IBOSAD' || jobCd === 'IBWHAD') {
            $(".reminderText").text(CONSTANTS.IB_DC_GENERAL_OFFICE);
        } else if (jobCd === 'IBFABR' || jobCd === 'IBGNMA' || jobCd === 'IBGWHA' || jobCd === 'IBJANT' || jobCd === 'IBP1CP' || jobCd === 'IBP2SP'
        	    || jobCd === 'IBPSTC' || jobCd === 'IBQATC' || jobCd === 'IBQCAS' || jobCd === 'IBRUTR' || jobCd === 'IBSVCO' || jobCd === 'IBSVCA'
        	    || jobCd === 'IBWAUD' || jobCd === 'IBWHCO' || jobCd === 'IBWHIC' || jobCd === 'IBWHPR'	) {
            $(".reminderText").text(CONSTANTS.IB_GENERAL_WAREHOUSE_ASSOC);
        } else {
            CONSTANTS.phonescreenDetailsObj.chkJobCd10(jobCd, internal_External);
        }
    };*/
    
    
    /*
     *  Check job code as per the warehouse message selected
     *  1.Freight location
     */
    /*this.chkJobCd10 = function(jobCd, internal_External) {
        if (CONSTANTS.isSuper) {
            $(".reminderText").text(CONSTANTS.DEPT_SUPER_PHN_SCRN_MSG);
        } else if (internal_External === 'INTERNAL' && !CONSTANTS.isSuper) {
            $(".reminderText").text(
                    CONSTANTS.HOUR_INTERNAL_TRANS_PHN_SCRN_MSG);
        } else if (CONSTANTS.scrnType === 'BASIC') {
            $(".reminderText").text(CONSTANTS.BASIC_PHN_SCRN_MSG);
        } else if (CONSTANTS.scrnType === 'DETAILED') {
            $(".reminderText").text(CONSTANTS.DETAILED_PHN_SCRN_MSG);
        }
    };*/
    /*
     *  Set Reminder text if phone screen details exist
     */
    this.setReminderText = function() {
    	$(".reminderText").text("Please use Phone Screen # " + CONSTANTS.phnScreenDtls[0].phoneScreenBannerNum);  
    	
    	/* var internal_External = "";
        var jobCd = "";
        CONSTANTS.isSuper = false;
        CONSTANTS.scrnType = "";

        // Added validation before reading data from xml.
        if (CONSTANTS.phnScreenDtls.length > 0) {
            internal_External = CONSTANTS.phnScreenDtls[0].internalExternal;
            jobCd = CONSTANTS.phnScreenReqDtls[0].job;
            if (jobCd.toString().substring(jobCd.length - 3, jobCd.length) === 'SUP' && jobCd !== 'ASDSUP' && jobCd !== 'IBSSUP' && jobCd !== 'IBHSUP') {
                CONSTANTS.isSuper = true;
            } else {
                CONSTANTS.isSuper = false;
            }
            CONSTANTS.scrnType = CONSTANTS.phnScreenReqDtls[0].scrnTyp;
            CONSTANTS.phonescreenDetailsObj.chkJobCd01(jobCd, internal_External);
        }*/
    };
    /*
     *  Set Phone screen status
     */
    this.setPhoneScnStatus = function() {
        // Added to check RSC users to use Interview schedule screen for req’s
        // that start with 15xxxx.
        CONSTANTS.phonescreenDetailsObj.rscUsrCheck();
        // Added to set the the Phone Screen Status
        for ( var r = 0; r < CONSTANTS.phoneScrnStatusDtls.length; r++) {
            if (CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode && CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode !== "") {
                if (CONSTANTS.phoneScrnStatusDtls[r].phoneScrnStatusCode === CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode) {
                    $('#scrnstatusSelect option:eq(' + r + ')').prop('selected', true);
                    $("#scrnstatusSelect").data("selectBox-selectBoxIt").refresh();
                    break;
                } else {
                    $('#scrnstatusSelect option:eq(3)').prop('selected', true); // INITIATE
                    // BASIC
                    // PHONE
                    // SCREEN
                    $("#scrnstatusSelect").data("selectBox-selectBoxIt").refresh();
                }
            }
        }
    };
    /*
     * check if it is RSC USer display & hide phonescreen accordingly
     */
    this.rscUsrCheck = function() {
        if (CONSTANTS.phnScreenDtls && CONSTANTS.phnScreenDtls.length > 0) {
            var reqNbr = parseInt(CONSTANTS.phnScreenDtls[0].reqNbr);

            if (reqNbr > 0 && reqNbr >= 150000000) {
                $(".intDet").addClass('hide');
                $(".intDet").removeClass('show');
            } else {
                $(".intDet").addClass('show');
                $(".intDet").removeClass('hide');
            }

        }
    };
    /*
     * Check phone screen status based on the user selection
     */
    this.checkPhoneScreenStatusCode = function() {
        CONSTANTS.phnscrnSaveCheck = true;
        // Enabling the Interview Status when Phone Screen Status is Store
        // Administrated or Phone Screen Complete
        if ($("#scrnstatusSelect option:selected").index() > -1) {
            if (CONSTANTS.phoneScrnStatusDtls[$("#scrnstatusSelect option:selected").index()].phoneScrnStatusCode === 3 || CONSTANTS.phoneScrnStatusDtls[$("#scrnstatusSelect option:selected").index()].phoneScrnStatusCode === 4) {
                $("#intrstatusSelect").removeAttr("disabled");
                $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
            } else {
                $("#intrstatusSelect").attr("disabled", "disabled");
                $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
            }
        }
    };
    /*
     * Set interview status based on the interview status code
     */
    this.setInterviewStatus = function() {
        for ( var r = 0; r < CONSTANTS.getInterviewStatusDtls.length; r++) {
            if (CONSTANTS.phnScreenDtls[0].interviewStatusCode && CONSTANTS.phnScreenDtls[0].interviewStatusCode !== "") {
                if (CONSTANTS.getInterviewStatusDtls[r].status === CONSTANTS.phnScreenDtls[0].interviewStatusCode) {
                    $('#intrstatusSelect option:eq(' + r + ')').prop('selected', true);
                    $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
                    break;
                } else {
                    $('#intrstatusSelect option:eq(0)').prop('selected', true); // Select
                    $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
                }
            }
        }
    };
    /*
     * Set phonescreen disposition status based on user selection
     */
    this.setPhoneScreenDispositionStatus = function() {
        for ( var r = 0; r < CONSTANTS.getPhoneScrnDispositionStatusDtls.length; r++) {
            if (CONSTANTS.phnScreenDtls[0].phoneScreenDispositionCode && CONSTANTS.phnScreenDtls[0].phoneScreenDispositionCode !== "") {
                if (CONSTANTS.getPhoneScrnDispositionStatusDtls[r].status === CONSTANTS.phnScreenDtls[0].phoneScreenDispositionCode) {
                    $('#selectDisposition option:eq(' + r + ')').prop('selected', true);
                    $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
                    break;
                } else {
                    $('#selectDisposition option:eq(0)').prop('selected', true);
                    $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
                }
            }
        }
    };
    /*
     * Format Target pay
     */
    this.formatTgtPay = function() {
        // Added for the appending of "$" and "/hr" for target pay in req
        // section
        // Changed so that Target Pay will have 2 decimals.. MTS 1876 10/02/10
        // Added Validation before reading data
        if (CONSTANTS.phnScrnStaffingList.length > 0 && CONSTANTS.phnScrnStaffingList[0].targetPay && CONSTANTS.phnScrnStaffingList[0].targetPay.toString().trim().length > 0) {
            var tgtPay = 0;
            tgtPay = parseFloat(CONSTANTS.phnScrnStaffingList[0].targetPay.toString());
            $("#targetPay").text(CONSTANTS.DOLLAR + tgtPay.toFixed(2) + CONSTANTS.PER_HR);
        }

        $("#intDate").val(CONSTANTS.intrDat);

        CONSTANTS.phnscrnSaveCheck = false;
    };
    /*
     * Set phone screen status based on the response from server
     */
    this.setPhnScrnStatus = function() {
        var chkPhnScrnStatus = (CONSTANTS.phnScreenDtls && CONSTANTS.phnScreenDtls.length > 0);
        // setting the value to phone screen date
        // to set the over all status for different screen types
        if (chkPhnScrnStatus && (!CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode || CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode === "0") && CONSTANTS.phnScreenDtls[0].scrTyp) {
            if (CONSTANTS.phnScreenDtls[0].scrTyp === "DETAILED") {
                $('#scrnstatusSelect option:eq(1)').prop('selected', true);
                $("#scrnstatusSelect").data("selectBox-selectBoxIt").refresh();
            } else {
                $('#scrnstatusSelect option:eq(0)').prop('selected', true);
                $("#scrnstatusSelect").data("selectBox-selectBoxIt").refresh();
            }
        }
    };
    /*
     * Set interview time details based on the time slot
     */
    this.setIntTimeDtls = function() {
        $("#amPMIntrw").selectBoxIt();
        $("#amPMIntrw").data("selectBox-selectBoxIt").refresh();
        // included to display the interview time properly
        if (CONSTANTS.intrwTimeList && CONSTANTS.intrwTimeList.length > 0) {
            if (CONSTANTS.intrwTimeList[0].hour && CONSTANTS.intrwTimeList[0].hour.toString().trim() !== "") {
                //Set AM PM option based onthe selection
                $('#amPMIntrw option:eq(1)').prop('selected', true);
                $("#amPMIntrw").data("selectBox-selectBoxIt").refresh();
                var value = parseInt(CONSTANTS.intrwTimeList[0].hour);
                if (value > 12) {
                    $('#amPMIntrw option:eq(2)').prop('selected', true);
                    $("#amPMIntrw").data("selectBox-selectBoxIt").refresh();
                    value = value - 12;
                }
                if (value === 12) {
                    $('#amPMIntrw option:eq(2)').prop('selected', true);
                    $("#amPMIntrw").data("selectBox-selectBoxIt").refresh();
                }
                if (value === 0) {
                    value = 12;
                    $('#amPMIntrw option:eq(1)').prop('selected', true);
                    $("#amPMIntrw").data("selectBox-selectBoxIt").refresh();
                }
                $("#intrwTimeHr").val(value.toString());
            }
        } else {
            //Reset the values
            $("#intrwTimeHr").val("");
            $("#intrwTimeMin").val("");
            $('#amPMIntrw option:eq(0)').prop('selected', true);
            $("#amPMIntrw").data("selectBox-selectBoxIt").refresh();
        }
    };
    /*
     * Enable disposition based on the status of phonescreen
     * Phonescreen status code should be 5 or 8
     */
    this.enableDisposition = function() {
        // disabling the panel when the overstatus is do not proceed or declined
        // interview
        if (CONSTANTS.phnScreenDtls.length > 0 && CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode) {
            if (CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode === "5" || CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode === "8") {
                this.blockFormElements(".intDet");
            } else {
                $(".intDet").unblock();
            }

            // CHANGED FOR CR 2289 DISABLING THE FOLLOWING FIELDS
            /*
             * Min. Requirement 01 - 10 Phone Screen Detailed Response WHEN THE
             * OVERALL STATUS IS 1)PHONE SCREEN COMPLETED 2)DO NOT PROCEED
             * 3)DECLINED INTERVIEW 4)SCHEDULE INTERVIEW 5)UNABLE TO SCHEDULE
             * 6)INTERVIEW SCHEDULED 7)PROCEED
             */

            var statusArr = [ 4, 5, 8, 9, 10, 11, 12 ];
            if ($.inArray(CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode, statusArr) !== -1) {
                CONSTANTS.phonescreenDetailsObj.disablePositionSpecificMinRequirements();
                CONSTANTS.phonescreenDetailsObj.disableRequisitionSpecificMinRequirements();
                $('#selectDisposition').attr("disabled", "disabled");
                $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
                $("#txtAreaPhnDetRes").attr("readonly", "readonly");
                $("#txtAreaPhnDetRes").css("background-color", "#F3F3F3");
            } else {
                CONSTANTS.phonescreenDetailsObj.enablePositionSpecificMinRequirements();
                CONSTANTS.phonescreenDetailsObj.enableRequisitionSpecificMinRequirements();
                $('#selectDisposition').removeAttr("disabled");
                $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
                $("#txtAreaCntHist").removeAttr("readonly");
                $("#txtAreaCntHist").css("background-color", "#FFFFFF");
                $("#txtAreaPhnDetRes").removeAttr("readonly");
                $("#txtAreaPhnDetRes").css("background-color", "#FFFFFF");
            }

            // If Phone Screen was created before Requisition Specific
            // Requirements were added then they need to be disabled
            if (!CONSTANTS.isExpandedPhoneScreen) {
                CONSTANTS.phonescreenDetailsObj.disableRequisitionSpecificMinRequirements();
            }
        }
    };

    this.chkIsAnyReqNo = function() {
        return (CONSTANTS.phonescreenDetailsObj.isAnyPositionSpecificRequirementsNo() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificNonSchdRequirementsNo() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificSchdRequirementsNo());
    };
    /*
     * Set minimum resource expected based on the requirement
     */
    this.setMinResListSelected = function() {
        // Set all of the Minimum Requirement combo boxes
        for ( var i = 0; i < CONSTANTS.minResponseList.length; i++) {
            var obj = "";
            obj = "minReq" + (i + 1);
            $('#' + obj + ' option:contains("' + CONSTANTS.minResponseList[i].minimumResponse + '")').attr('selected', 'selected');
        }

        // setting the minimum response for the first time
        if (CONSTANTS.phonescreenDetailsObj.isAllMinRequirementsNA()) {
            $("#ynMinReqStat").text("");
        } else if (CONSTANTS.phonescreenDetailsObj.chkIsAnyReqNo()) {
            $("#ynMinReqStat").text(CONSTANTS.DO_NOT_PROCEED);
        } else if (CONSTANTS.phonescreenDetailsObj.isAnyPositionSpecificRequirementsYesOrNA() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificNonSchdRequirementsYesOrNA() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificSchdRequirementsYesOrNA()) {
            $("#ynMinReqStat").text(CONSTANTS.PROCEED);
        }

        if ($("#minReq1 option:selected").index() === 0) {
            $("#txtAreaPhnDetRes").attr("readonly", "readonly");
            $("#txtAreaPhnDetRes").css("background-color", "#F3F3F3");
        } else {
            var statusArr = [ 4, 5, 8, 9, 10, 11, 12 ];
            // added this condition for the inclusion of CR 2289
            if ($.inArray(CONSTANTS.phnScreenDtls[0].phoneScreenStatusCode, statusArr) !== -1) {
                $("#txtAreaPhnDetRes").attr("readonly", "readonly");
                $("#txtAreaPhnDetRes").css("background-color", "#F3F3F3");
            } else {
                $("#txtAreaPhnDetRes").removeAttr("readonly");
                $("#txtAreaPhnDetRes").css("background-color", "#FFFFFF");
            }
        }
    };
/*
 * Hide and show labels
 */
    this.intDtlsLblShow = function() {
        /*
         * Hide and display label caption
         */
        //Add class
        $("#intrLocNameLbl").addClass("show");
        $("#intrLocAddrLbl").addClass("show");
        $("#intrLocCityLbl").addClass("show");
        $("#intrLocPhnLbl").addClass("show");
        $("#intrLocStateLbl").addClass("show");
        $("#intrLocZipLbl").addClass("show");
        //Remove class
        $("#intrLocNameLbl").removeClass("hide");
        $("#intrLocAddrLbl").removeClass("hide");
        $("#intrLocCityLbl").removeClass("hide");
        $("#intrLocPhnLbl").removeClass("hide");
        $("#intrLocStateLbl").removeClass("hide");
        $("#intrLocZipLbl").removeClass("hide");
        //Add class
        $("#intrLocName").addClass("hide");
        $("#intrLocAddr").addClass("hide");
        $("#intrLocCity").addClass("hide");
        $("#intrLocPhn1").addClass("hide");
        $("#intrLocPhn2").addClass("hide");
        $("#intrLocPhn3").addClass("hide");
        $("#intrLocState").addClass("hide");
        $("#intrLocZip").addClass("hide");
      //Remove class
        $("#intrLocName").removeClass("show");
        $("#intrLocAddr").removeClass("show");
        $("#intrLocCity").removeClass("show");
        $("#intrLocPhn1").removeClass("show");
        $("#intrLocPhn2").removeClass("show");
        $("#intrLocPhn3").removeClass("show");
        $("#intrLocState").removeClass("show");
        $("#intrLocZip").removeClass("show");
    };
/*
 * Hide and show details label
 */
    this.intDtlsLblHide = function() {
        /*
         * Hide and display label values controls
         */
        //Add class
        $("#intrLocName").addClass("show");
        $("#intrLocAddr").addClass("show");
        $("#intrLocCity").addClass("show");
        $("#intrLocPhn1").addClass("show");
        $("#intrLocPhn2").addClass("show");
        $("#intrLocPhn3").addClass("show");
        $("#intrLocState").addClass("show");
        $("#intrLocZip").addClass("show");
        //Remove class
        $("#intrLocName").removeClass("hide");
        $("#intrLocAddr").removeClass("hide");
        $("#intrLocCity").removeClass("hide");
        $("#intrLocPhn1").removeClass("hide");
        $("#intrLocPhn2").removeClass("hide");
        $("#intrLocPhn3").removeClass("hide");
        $("#intrLocState").removeClass("hide");
        $("#intrLocZip").removeClass("hide");
        //Set values
        $("#intrLocName").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocName);
        $("#intrLocAddr").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocAddr);
        $("#intrLocCity").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocCity);

        if (CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn && CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn !== "null" && CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.toString().trim().length > 0) {
            $("#phnFirst").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.toString().substring(0, 3));
            $("#phnSecond").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.toString().substring(3, 6));
            $("#phnThird").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.toString().substring(6, 10));
        }
        $("#intrLocState").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocState);
        $("#intrLocZip").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocZip);
        //Add class
        $("#intrLocNameLbl").addClass("hide");
        $("#intrLocAddrLbl").addClass("hide");
        $("#intrLocCityLbl").addClass("hide");
        $("#intrLocPhnLbl").addClass("hide");
        $("#intrLocStateLbl").addClass("hide");
        $("#intrLocZipLbl").addClass("hide");
        //Remove class
        $("#intrLocNameLbl").removeClass("show");
        $("#intrLocAddrLbl").removeClass("show");
        $("#intrLocCityLbl").removeClass("show");
        $("#intrLocPhnLbl").removeClass("show");
        $("#intrLocStateLbl").removeClass("show");
        $("#intrLocZipLbl").removeClass("show");
    };
/*
 * Set interview details based on hiring event
 */
    this.setIntDtlsTypBased = function(i) {
        if (CONSTANTS.phnScreenDtls[i].intrwTyp === 1) {
            $("#hrStr").prop('checked', true);
            CONSTANTS.phonescreenDetailsObj.intDtlsLblShow();
        } else if (CONSTANTS.phnScreenDtls[i].intrwTyp === 2) {
            $("#nonHrStr").prop('checked', true);
            CONSTANTS.phonescreenDetailsObj.intDtlsLblShow();
        } else if (CONSTANTS.phnScreenDtls[i].intrwTyp === 3) {
            $("#hrEvnt").prop('checked', true);
            if (CONSTANTS.phnScreenDtls[i].hrEventFlg === "Y") {
                CONSTANTS.phonescreenDetailsObj.intDtlsLblShow();
            } else if (CONSTANTS.phnScreenDtls[i].hrEventFlg === "N") {
                $("#hrEvnt").prop('checked', true);
                CONSTANTS.phonescreenDetailsObj.intDtlsLblHide();
            }
        } else if (CONSTANTS.phnScreenDtls[i].intrwTyp === 4) {
            $("#others").prop('checked', true);
            CONSTANTS.phonescreenDetailsObj.intDtlsLblHide();
        }
    };
/*
 * Set phone screen details only if storedetails and manager details is available
 */
    this.chkPhnScrnStrDtl = function() {
        return (CONSTANTS.phnScreenReqStoreDtls && CONSTANTS.phnScreenReqStoreDtls.length > 0 && !CONSTANTS.phnScreenReqStoreDtls[0].storeMgr );
    };
/*
 * Get interview details
 */
    this.getIntrwDtls = function() {
        $("#goHome").show();
        $("#emailHbox").addClass('show');
        //Format Target Pay
        CONSTANTS.phonescreenDetailsObj.formatTgtPay();
        //Set Phone screen status
        CONSTANTS.phonescreenDetailsObj.setPhnScrnStatus();
        //Set Interview Time details
        CONSTANTS.phonescreenDetailsObj.setIntTimeDtls();
        // setting the store manager to vacant if no value is there
        if (CONSTANTS.phonescreenDetailsObj.chkPhnScrnStrDtl()) {
            $("#strMgr").text(CONSTANTS.VACANT);
        }
        //Enable Disposition
        CONSTANTS.phonescreenDetailsObj.enableDisposition();

        if (CONSTANTS.minResponseList && CONSTANTS.minResponseList.length > 0) {
            CONSTANTS.phonescreenDetailsObj.setMinResListSelected();
        }
        //check minimum requirements
        if ($("#minReq1 option:selected").index() !== 0) {
            if (CONSTANTS.phnScreenDtls[0].phnScreener && CONSTANTS.phnScreenDtls[0].phnScreener.toString().trim().length > 0) {
                $(".screener").text(CONSTANTS.phnScreenDtls[0].phnScreener);
            } else {
                $(".screener").text(CONSTANTS.userProfile.userId);
            }
        } else {
            $(".screener").text("");
        }
        //check phone screen details
        if (CONSTANTS.phnScreenDtls) {
            for ( var i = 0; i < CONSTANTS.phnScreenDtls.length; i++) {
                CONSTANTS.phonescreenDetailsObj.setIntDtlsTypBased(i);
            }
        }
    };
/*
 * Check phonescreen if completed
 */
    this.checkPhnScrnCompleted = function() {
        // Added validation before reading the data from response.
        if (CONSTANTS.phnScreenDtls.length > 0 && CONSTANTS.phnScreenDtls[0].phoneScreensCompletedFlg === "Y") {
            $('#unSaveWarnModal .unSaveWarnMsg').empty();
            $('#unSaveWarnModal .unSaveWarnMsg').append('<p>Enough phone screens have been conducted.</p><p>Click OK to continue.</p>');
            $('#unSaveWarnModal').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModal');
        }
    };
/*
 * Check if Phone screen interview is editable
 */
    this.setStrFieldsEditable = function() {
        CONSTANTS.phonescreenDetailsObj.intDtlsLblHide();
        //Set values if intervie wtype is 4
        if (CONSTANTS.phnScreenIntrwDtls && CONSTANTS.phnScreenIntrwDtls.length > 0 && CONSTANTS.phnScreenDtls[0].intrwTyp === 4) {
            $("#intrLocName").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocName);
            $("#intrLocAddr").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocAddr);
            $("#intrLocCity").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocCity);

            if (CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn && CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.length >= 10) {
                $("#phnFirst").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.toString().substring(0, 3));
                $("#phnSecond").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.toString().substring(3, 6));
                $("#phnThird").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn.toString().substring(6, 10));
            }
            $("#intrLocState").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocState);
            $("#intrLocZip").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocZip);
        }
    };
/*
 * Check if Store details are not editable
 */
    this.setStrFieldsNotEditable = function() {
        // added to clear the data fields on click of other radio buttons apart
        // from other interview location
        $("#intrLocName").text("");
        $("#intrLocAddr").text("");
        $("#intrLocCity").text("");
        $("#phnFirst").text("");
        $("#phnSecond").text("");
        $("#phnThird").text("");
        $("#intrLocState").text("");
        $("#intrLocZip").text("");
        var isHiringEvnt = ($("#hrEvnt").is(':checked') && CONSTANTS.phnScreenDtls[0].hrEventFlg === "Y");

        if ($("#hrStr").is(':checked') || $("#nonHrStr").is(':checked') || (isHiringEvnt)) {
            CONSTANTS.phonescreenDetailsObj.intDtlsLblShow();
        } else if (($("#hrEvnt").is(':checked') && CONSTANTS.phnScreenDtls[0].hrEventFlg === "N") || $("#others").is(':checked')) {
            CONSTANTS.phonescreenDetailsObj.intDtlsLblHide();
        }

        if ($("#hrStr").is(':checked')) {
            //set if hiring store is checked
            $("#intrLocNameLbl").text(CONSTANTS.phnScreenReqStoreDtls[0].strName);
            $("#intrLocAddrLbl").text(CONSTANTS.phnScreenReqStoreDtls[0].addr);
            $("#intrLocCityLbl").text(CONSTANTS.phnScreenReqStoreDtls[0].city);
            $("#intrLocPhnLbl").text(CONSTANTS.phonescreenDetailsObj.getFormattedNumber(CONSTANTS.phnScreenReqStoreDtls[0].phone));
            $("#intrLocStateLbl").text(CONSTANTS.phnScreenReqStoreDtls[0].state);
            $("#intrLocZipLbl").text(CONSTANTS.phnScreenReqStoreDtls[0].zip);
        } else if (isHiringEvnt) {
            //Set if it is hiring event
            $("#intrLocNameLbl").text(CONSTANTS.phnScrnStaffingList[0].stfhrgEvntLoc);
            $("#intrLocAddrLbl").text(CONSTANTS.phnScrnStaffingList[0].add);
            $("#intrLocCityLbl").text(CONSTANTS.phnScrnStaffingList[0].city);
            $("#intrLocPhnLbl").text(CONSTANTS.phonescreenDetailsObj.getFormattedNumber(CONSTANTS.phnScrnStaffingList[0].stfhrgEvntLocPhn));
            $("#intrLocStateLbl").text(CONSTANTS.phnScrnStaffingList[0].state);
            $("#intrLocZip").text(CONSTANTS.phnScrnStaffingList[0].zip);

        }
    };
/*
 * get store details based on the store number entered
 */
    this.getStrDetails = function() {
        $("#enterStrNumModal").modal("show");
        CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#enterStrNumModal');
        //reset the values
        if ($("#StrNumModalText").val() !== "") {
            $("#StrNumModalText").val("");
        }
        CONSTANTS.phonescreenDetailsObj.intDtlsLblShow();
    };
/*
 * Get store values
 */
    this.getStrValues = function() {
        CONSTANTS.storeNO = $("#StrNumModalText").val();
        if (CONSTANTS.storeNO === "") {
            // calling the showPopUp method to show warning popup with message
            $('#unSaveWarnModalEnterStr .unSaveWarnMsg').empty();
            $('#unSaveWarnModalEnterStr .unSaveWarnMsg').append("<p>Please Enter The Store Number</p>");
            $('#unSaveWarnModalEnterStr').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalEnterStr');
            return;
        } else {
            // calling the web service to get non hiring tore details
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(CONSTANTS.phonescreenDetailsObj.loadStrDtls);
            RSASERVICES.getStoreDetailsPhnScrn(CONSTANTS.storeNO, callbackFunction, true, "Please wait...");
            //Reset the values
            $("#intrLocName").text("");
            $("#intrLocAddr").text("");
            $("#intrLocCity").text("");
            $("#phnFirst").text("");
            $("#phnSecond").text("");
            $("#phnThird").text("");
            $("#intrLocState").text("");
            $("#intrLocZip").text("");
            //Add class
            $("#intrLocNameLbl").addClass("show");
            $("#intrLocAddrLbl").addClass("show");
            $("#intrLocCityLbl").addClass("show");
            $("#intrLocPhnLbl").addClass("show");
            $("#intrLocStateLbl").addClass("show");
            $("#intrLocZipLbl").addClass("show");
            //Remove class
            $("#intrLocNameLbl").removeClass("hide");
            $("#intrLocAddrLbl").removeClass("hide");
            $("#intrLocCityLbl").removeClass("hide");
            $("#intrLocPhnLbl").removeClass("hide");
            $("#intrLocStateLbl").removeClass("hide");
            $("#intrLocZipLbl").removeClass("hide");
            //Hide popup
            $("#enterStrNumModal").modal("hide");
        }
    };
/*
 * Load Store details
 */
    this.loadStrDtls = function(json) {
        $.unblockUI();
        var timeFormat = {};
        var dateFormat = {};
        var resultStoreList = [];
        var phnScrnStrDtls = {};

        timeFormat.formatString = "L:NN A";
        dateFormat.formatString = "EEEE - MM/DD/YYYY";

        // check for the status tag in the response
        if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.SUCCESS) {
            CONSTANTS.nonHrStrList = [];

            // check for the StoreDetailList tag in the response
            if (json.Response.hasOwnProperty("StoreDetailList") && json.Response.StoreDetailList.hasOwnProperty("StoreDetails")) {
                // check whether StoreDetailList is an array collection
                if (Array.isArray(json.Response.StoreDetailList.StoreDetails)) {
                    resultStoreList = json.Response.StoreDetailList.StoreDetails;
                }
                // StoreDetailList is not an array collection
                else {
                    resultStoreList = [ json.Response.StoreDetailList.StoreDetails ];
                }

            }
            // check whether the result list is having any value object or
            // not
            if (resultStoreList && resultStoreList.length > 0) {
                // iterating the result list to get each item and set in to
                // model locator
                for ( var i = 0; i < resultStoreList.length; i++) {
                    phnScrnStrDtls = {};
                    phnScrnStrDtls.addr = resultStoreList[i].add;
                    phnScrnStrDtls.city = resultStoreList[i].city;
                    phnScrnStrDtls.state = resultStoreList[i].state;
                    phnScrnStrDtls.zip = resultStoreList[i].zip;
                    phnScrnStrDtls.phone = resultStoreList[i].phone;
                    phnScrnStrDtls.storeMgr = resultStoreList[i].strMgr;
                    phnScrnStrDtls.strName = resultStoreList[i].strName;
                    phnScrnStrDtls.timeZoneCode = resultStoreList[i].timeZoneCode;
                    CONSTANTS.nonHrStrList.push(phnScrnStrDtls);
                }
            }

            $("#intrLocNameLbl").text(CONSTANTS.storeNO);
            $("#intrLocAddrLbl").text(CONSTANTS.nonHrStrList[0].addr);
            $("#intrLocCityLbl").text(CONSTANTS.nonHrStrList[0].city);
            $("#intrLocPhnLbl").text(CONSTANTS.phonescreenDetailsObj.getFormattedNumber(CONSTANTS.nonHrStrList[0].phone));
            $("#intrLocStateLbl").text(CONSTANTS.nonHrStrList[0].state);
            $("#intrLocZipLbl").text(CONSTANTS.nonHrStrList[0].zip);
        }
        // Handling the error for the error
        else if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.phonescreenDetailsObj.popup.alert(json.Response.error.errorMsg);
        }
    };
/*
 * Get formatted phone number
 * xxx-xxx-xxxx
 */
    this.getFormattedNumber = function(phoneNumber) {
        var i = 0;
        if (phoneNumber && phoneNumber !== "" && phoneNumber !== "null") {
            var formatString = "";
            var phoneNo = phoneNumber.toString();
            //Split the characters to format phonenumber as xxx-xxx-xxxx
            var formatArr = [ 3, 6 ];
            for (i = 0; i < phoneNo.length + 2; i++) {
                if ($.inArray(i, formatArr) !== -1) {
                    formatString = formatString + "-";
                    formatString = formatString + phoneNo.charAt(i);
                } else {
                    formatString = formatString + phoneNo.charAt(i);
                }

            }
            return formatString;
        } else {
            if (phoneNumber === "null") {
                return "";
            } else {
                return phoneNumber;
            }
        }
    };
/*
 * Check if any position specific requirement is available
 */
    this.isAnyPositionSpecificRequirementsNo = function() {
        var returnValue = false;

        for ( var i = 1; i < 11; i++) {
            if ($("#minReq" + i + " option:selected").index() === 2) {
                returnValue = true;
            }
        }
        return returnValue;
    };
/*
 * Check if any specific non scheduled requirements available
 */
    this.isAnyReqSpecificNonSchdRequirementsNo = function() {
        var returnValue = false;
        var chkIndex = ($("#minReq11 option:selected").index() === 2 || $("#minReq12 option:selected").index() === 2 || $("#minReq13 option:selected").index() === 2);

        if (chkIndex || $("#minReq24 option:selected").index() === 2 || $("#minReq25 option:selected").index() === 2) {
            returnValue = true;
        }
        return returnValue;
    };
    /*
     * Check if any specific  scheduled requirements available
     */
    this.isAnyReqSpecificSchdRequirementsNo = function() {
        var returnValue = false;

        for ( var i = 14; i < 23; i++) {
            if ($("#minReq" + i + " option:selected").index() === 2) {
                returnValue = true;
            }
        }
        return returnValue;
    };
    /*
     * Check if accomodation number needed
     */
    this.isNeedAccommodationNo = function() {
        var returnValue = false;

        if ($("#minReq23 option:selected").index() === 2) {
            returnValue = true;
        }
        return returnValue;
    };
    /*
     * Check if any position specific requirement
     */
    this.isAnyPositionSpecificRequirementsYesOrNA = function() {
        var returnValue = false;
        var returnArr = [];

        for ( var i = 1; i < 11; i++) {
            if ($("#minReq" + i + " option:selected").index() === 1 || $("#minReq" + i + " option:selected").index() === 0) {
                returnArr.push(returnValue);
            }
        }

        return returnArr.length === 10 ? true : false;
    };
    /*
     * Check if any schedule specific requirement
     */
    this.chkSchdReqYesOrNA = function() {
        var cond1 = (($("#minReq11 option:selected").index() === 1 || $("#minReq11 option:selected").index() === 0) && ($("#minReq12 option:selected").index() === 1 || $("#minReq12 option:selected").index() === 0));
        var cond2 = (cond1 && ($("#minReq13 option:selected").index() === 1 || $("#minReq13 option:selected").index() === 0));
        var cond3 = (cond2 && ($("#minReq24 option:selected").index() === 1 || $("#minReq24 option:selected").index() === 0));
        var cond4 = (cond3 && ($("#minReq25 option:selected").index() === 1 || $("#minReq25 option:selected").index() === 0));
        return cond4;
    };
    /*
     * Check if any specific non sechedule requirement yes or NA
     */
    this.isAnyReqSpecificNonSchdRequirementsYesOrNA = function() {
        var returnValue = false;

        if (CONSTANTS.phonescreenDetailsObj.chkSchdReqYesOrNA()) {
            returnValue = true;
        }
        return returnValue;
    };
    /*
     * Check if any requirement specific non sechedule requirement yes or NA
     */
    this.isAnyReqSpecificSchdRequirementsYesOrNA = function() {
        var returnValue = false;

        var returnArr = [];

        for ( var i = 14; i < 24; i++) {
            if ($("#minReq" + i + " option:selected").index() === 1 || $("#minReq" + i + " option:selected").index() === 0) {
                returnArr.push(returnValue);
            }
        }

        return returnArr.length === 10 ? true : false;
    };
/*
 * Dsiable if position specific minimu requirement is achieved
 */
    this.disablePositionSpecificMinRequirements = function() {
        for ( var i = 0; i < 10; i++) {
            var obj = "";
            obj = "minReq" + (i + 1);
            $("#" + obj).attr("disabled", "disabled");
        }
    };
/*
 * Disable requisition specific minimum requirements
 */
    this.disableRequisitionSpecificMinRequirements = function() {
        for ( var i = 10; i < 25; i++) {
            var obj = "";
            obj = "minReq" + (i + 1);
            $("#" + obj).attr("disabled", "disabled");
        }
    };
/*
 * Enable position specific minimum requirements
 */
    this.enablePositionSpecificMinRequirements = function() {
        for ( var i = 0; i < 10; i++) {
            var obj = "";
            obj = "minReq" + (i + 1);
            $("#" + obj).removeAttr("disabled");
        }
    };
/*
 * Enable for minimum requirements
 */
    this.minReqEnable = function() {
        // Set Schedule Questions based on Requisition Availability
        // Days
        // Weekdays
        CONSTANTS.schdPrefArr[0] ? $("#minReq14").removeAttr("disabled") : $("#minReq14").attr("disabled", "disabled");
        // Saturday
        CONSTANTS.schdPrefArr[1] ? $("#minReq15").removeAttr("disabled") : $("#minReq15").attr("disabled", "disabled");
        // Sunday
        CONSTANTS.schdPrefArr[2] ? $("#minReq16").removeAttr("disabled") : $("#minReq16").attr("disabled", "disabled");

        // Time Slots
        // Early AM
        CONSTANTS.schdPrefArr[3] ? $("#minReq17").removeAttr("disabled") : $("#minReq17").attr("disabled", "disabled");
        // Mornings
        CONSTANTS.schdPrefArr[4] ? $("#minReq18").removeAttr("disabled") : $("#minReq18").attr("disabled", "disabled");
        // Afternoons
        CONSTANTS.schdPrefArr[5] ? $("#minReq19").removeAttr("disabled") : $("#minReq19").attr("disabled", "disabled");
        // Nights
        CONSTANTS.schdPrefArr[6] ? $("#minReq20").removeAttr("disabled") : $("#minReq20").attr("disabled", "disabled");
        // Late Night
        CONSTANTS.schdPrefArr[7] ? $("#minReq21").removeAttr("disabled") : $("#minReq21").attr("disabled", "disabled");
        // Overnight
        CONSTANTS.schdPrefArr[8] ? $("#minReq22").removeAttr("disabled") : $("#minReq22").attr("disabled", "disabled");
    };
/*
 * Split minimun requirement
 */
    this.splittedMinReqDisable = function() {
        return (CONSTANTS.schdPrefArr[0] && CONSTANTS.schdPrefArr[1] && CONSTANTS.schdPrefArr[2] && CONSTANTS.schdPrefArr[3]);
    };
/*
 * Disable for minimum requirement
 */
    this.minReqDisable = function() {
        // Handle legacy Requisitions when all Availability has been selected.
        // This is the same as not selecting any availability. Don;t ask any of
        // the
        // Schedule Questions.
        var schdPref03 = CONSTANTS.phonescreenDetailsObj.splittedMinReqDisable();
        var schdPref27 = (CONSTANTS.schdPrefArr[4] && CONSTANTS.schdPrefArr[5] && CONSTANTS.schdPrefArr[6] && CONSTANTS.schdPrefArr[7]);
        if (schdPref03 && schdPref27 && CONSTANTS.schdPrefArr[8]) {
            for ( var i = 14; i < 23; i++) {
                var obj = "";
                obj = "minReq" + i;
                $("#" + obj).attr("disabled", "disabled");
            }
        }
    };
/*
 * Enable requistion specific minimum requirement
 */
    this.enableRequisitionSpecificMinRequirements = function() {
        for ( var i = 10; i < 25; i++) {
            var obj = "";
            obj = "minReq" + (i + 1);
            $("#" + obj).removeAttr("disabled");
        }
        CONSTANTS.phonescreenDetailsObj.minReqEnable();
        CONSTANTS.phonescreenDetailsObj.minReqDisable();

        // Disable Drvier's License Field (minReq24) and Reliable Transportation
        // Field (minReq25) if Job Title is not a Driving Position
        if (!CONSTANTS.phnScreenReqDtls[0].isDrivingPosition) {
            $("#minReq24").attr("disabled", "disabled");
            $("#minReq25").attr("disabled", "disabled");
        }
        // It is a Driving Position but the store does not require a license
        else if (CONSTANTS.phnScreenReqDtls[0].isDrivingPosition) {
            for ( var t = 0; t < CONSTANTS.storeDriversLicenseExemptList.length; t++) {
                if (CONSTANTS.storeDriversLicenseExemptList[t] === CONSTANTS.phnScreenReqDtls[0].strNbr) {
                    $("#minReq24").attr("disabled", "disabled");
                    break;
                }
            }
        }

        // Need Accommodation (minReq23) will be grayed out unless Any of the
        // Schedule questions is No
        $("#minReq23").attr("disabled", "disabled");
        for (i = 14; i < 23; i++) {
            obj = "";
            obj = "minReq" + i;
            if ($("#" + obj + " option:selected").index() === 2) {
                $("#minReq23").removeAttr("disabled");
            }
        }
    };
/*
 * Check if all minimum requirement not applicable
 */
    this.isAllMinRequirementsNA = function() {
        var returnValue = false;
        var returnArr = [];

        for ( var i = 1; i < 26; i++) {
            if ($("#minReq" + i + " option:selected").index() === 0) {
                returnArr.push(returnValue);
            }
        }

        return returnArr.length === 25 ? true : false;
    };
    /*
     * Formatted seven digit phone number
     */
    this.sevenDigitFormatPhnNumber = function(phoneNumber, formatString) {
        for ( var g = 0; g < phoneNumber.length + 2; g++) {
            if (g === 3 || g === 0) {
                formatString = formatString + "-";
                formatString = formatString + phoneNumber.charAt(g);
            } else {
                formatString = formatString + phoneNumber.charAt(g);
            }

        }
        return formatString;
    };
    /*
     * Formatted ten digit phone number
     */
    this.tenDigitFormatPhnNumber = function(phoneNumber, formatString) {
        for ( var t = 0; t < phoneNumber.length + 2; t++) {
            if (t === 3 || t === 6) {
                formatString = formatString + "-";
                formatString = formatString + phoneNumber.charAt(t);
            } else {
                formatString = formatString + phoneNumber.charAt(t);
            }
        }
        return formatString;
    };
    /*
     * get Formatted phone number
     */
    this.getFormattedPhoneNumber = function(phoneNumber) {
        if (phoneNumber && phoneNumber !== "" && CONSTANTS.EXTERN === CONSTANTS.phnScreenDtls[0].internalExternal) {
            var formatString = "";
            if (phoneNumber.toString().length === 7) {
                formatString = CONSTANTS.phonescreenDetailsObj.sevenDigitFormatPhnNumber(phoneNumber.toString(), formatString);
            }
            if (phoneNumber.toString().length === 10) {
                formatString = CONSTANTS.phonescreenDetailsObj.tenDigitFormatPhnNumber(phoneNumber.toString(), formatString);
            }
            return formatString;
        } else {
            return phoneNumber;
        }
    };
    /*
     * check overall status
     */
    this.checkOverallStatus = function() {
        CONSTANTS.phnscrnSaveCheck = true;
        // disabling the interview details section when the overall status is DO
        // NOT PROCEED
        // AND DECLINED INTERVIEW
        if ($("#scrnstatusSelect option:selected").index() === 3 || $("#scrnstatusSelect option:selected").index() === 7) {
            this.blockFormElements(".intDet");
        } else {
            $(".intDet").unblock();
        }

        CONSTANTS.phonescreenDetailsObj.enablePositionSpecificMinRequirements();

        $("#txtAreaCntHist").removeAttr("readonly");
        $("#txtAreaCntHist").css("background-color", "#FFFFFF");

        if ($("#minReq1 option:selected").index() === 0) {
            $("#txtAreaPhnDetRes").attr("readonly", "readonly");
            $("#txtAreaPhnDetRes").css("background-color", "#F3F3F3");
        } else {
            $("#txtAreaPhnDetRes").removeAttr("readonly");
            $("#txtAreaPhnDetRes").css("background-color", "#FFFFFF");
        }
    };
    /*
     * Validate phone screen
     */
    this.valPhnScreen = function() {
        CONSTANTS.phnscrnSaveCheck = true;
    };
/*
 * Show unsaved Popup details
 */
    this.showUnsavedPopUpHome = function() {
        $('#unSaveWarnModalHome .unSaveWarnMsg').empty();
        $('#unSaveWarnModalHome .unSaveWarnMsg').append("<p>Unsaved Data In Phone Screen Details.Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
        $('#unSaveWarnModalHome').modal('show');
        CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalHome');
    };
/*
 * Handle unsaved data when home button is pressed
 */
    this.handleUnsavedHomeOk = function() {
        if (CONSTANTS.isCloseBtnClick) {
            CONSTANTS.isCloseBtnClick = false;
            window.close();
        } else {
            //Hide popup
            $('#unSaveWarnModalReq').modal('hide');
            $('#unSaveWarnModalHome').modal('hide');
            if($(".modal-backdrop.fade.in").length > 2)
        	{
        	$($(".modal-backdrop.fade.in")[0]).empty().remove();
        	}
            //Reset text values
            $("#intrLocName").text("");
            $("#intrLocAddr").text("");
            $("#intrLocCity").text("");
            $("#phnFirst").text("");
            $("#phnSecond").text("");
            $("#phnThird").text("");
            $("#intrLocState").text("");
            $("#intrLocZip").text("");
            CONSTANTS.phonescreenDetailsObj.resetAllMinRequirementsToNA();
            $("#intrwTimeHr").val("");
            $("#intrwTimeMin").val("");
            CONSTANTS.phnScrnCompleteDate = "";
            CONSTANTS.phnScrnCompleteTime = "";
            $("#intDate").val("");
            $(".screener").text("");
            //clear constants
            CONSTANTS.LoadPanelTitle = "";
            CONSTANTS.phnScreenDtls = [];
            CONSTANTS.phnScreenIntrwDtls = [];
            CONSTANTS.phnScreenReqStoreDtls = [];
            CONSTANTS.phnScreenReqDtls = [];
            CONSTANTS.phnScrnStaffingList = [];
            CONSTANTS.getAuthPosCnt = 0;
            CONSTANTS.getTotalPhnScrnDtls = [];
            CONSTANTS.getInActPhnScrnDtls = [];
            CONSTANTS.SUCCESS_ON_HOME = false;
            UTILITY.RemoveQS();
            $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.phonescreenDetailsObj.setContent);
        }
    };
/*
 * Display unsaved warning message
 */
    this.showUnsavedPopUpReq = function() {
        $('#unSaveWarnModalReq .unSaveWarnMsg').empty();
        $('#unSaveWarnModalReq .unSaveWarnMsg').append("<p>Unsaved Data In Phone Screen Details.Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
        $('#unSaveWarnModalReq').modal('show');
        CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalReq');
    };
    this.redirectToReqScrn = function() {
		if($(".modal-backdrop.fade.in").length > 2)
    	{
    	$($(".modal-backdrop.fade.in")[0]).empty().remove();
    	}
        //Reset the values
        $("#intrLocName").text("");
        $("#intrLocAddr").text("");
        $("#intrLocCity").text("");
        $("#phnFirst").text("");
        $("#phnSecond").text("");
        $("#phnThird").text("");
        $("#intrLocState").text("");
        $("#intrLocZip").text("");
        CONSTANTS.phonescreenDetailsObj.resetAllMinRequirementsToNA();

        CONSTANTS.phonescreenDetailsObj.navigateToReqScreenDetails();    
    };
    /*
     * Handle unsaved warning message
     */
    this.handleUnsavedReqOk = function() {
    	if ($("#unSaveWarnModalReq:visible").length > 0) {
    		$('#unSaveWarnModalReq').on('hidden.bs.modal', function() {
    			CONSTANTS.phonescreenDetailsObj.redirectToReqScrn();
    		}.bind(this));
    		$('#unSaveWarnModalReq').modal('hide');
    	} else {
    		CONSTANTS.phonescreenDetailsObj.redirectToReqScrn();
    	}     
    };
    /*
     * show unsaved Popup candidate
     */
    this.showUnsavedPopUpCand = function() {
        $('#unSaveWarnModalCand .unSaveWarnMsg').empty();
        $('#unSaveWarnModalCand .unSaveWarnMsg').append("<p>Unsaved Data In Phone Screen Details.Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
        $('#unSaveWarnModalCand').modal('show');
        CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalCand');
    };
    /*
     * Handle unsaved Popup candidate
     */
    this.handleUnsavedCandOk = function() {
        //Hide the popup
        $('#unSaveWarnModalCand').modal('hide');
        $('#unSaveWarnModalReq').modal('hide');
        if($(".modal-backdrop.fade.in").length > 2)
    	{
    	$($(".modal-backdrop.fade.in")[0]).empty().remove();
    	}
        //Reset the values
        $("#intrLocName").text("");
        $("#intrLocAddr").text("");
        $("#intrLocCity").text("");
        $("#phnFirst").text("");
        $("#phnSecond").text("");
        $("#phnThird").text("");
        $("#intrLocState").text("");
        $("#intrLocZip").text("");
        CONSTANTS.phonescreenDetailsObj.resetAllMinRequirementsToNA();

        CONSTANTS.phonescreenDetailsObj.navigateToCandidateDetails();
    };
    /*
     * Show unsaved Popup candidate
     */
    this.showUnsavedPopUpInt = function() {
        $('#unSaveWarnModalInt .unSaveWarnMsg').empty();
        $('#unSaveWarnModalInt .unSaveWarnMsg').append("<p>Unsaved Data In Phone Screen Details.Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
        $('#unSaveWarnModalInt').modal('show');
        CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalInt');
    };
    /*
     * Show unsaved Interview
     */
    this.handleUnsavedInterviewOk = function() {
        //Hide the popup
        $('#unSaveWarnModalCand').modal('hide');
        $('#unSaveWarnModalReq').modal('hide');
        if($(".modal-backdrop.fade.in").length > 2)
    	{
    	$($(".modal-backdrop.fade.in")[0]).empty().remove();
    	}
        //Reset the values
        $("#intrLocName").text("");
        $("#intrLocAddr").text("");
        $("#intrLocCity").text("");
        $("#phnFirst").text("");
        $("#phnSecond").text("");
        $("#phnThird").text("");
        $("#intrLocState").text("");
        $("#intrLocZip").text("");
        CONSTANTS.phonescreenDetailsObj.resetAllMinRequirementsToNA();
        CONSTANTS.LoadINTVIEWSRCHRSLTS = [ {
            itiNbr : $(".phnDet .screenNbr").text()
        } ];
        $.get('app/RSAInterviewScreenDetails/view/interviewScreenDetails.html', CONSTANTS.phonescreenDetailsObj.setContent);
    };
    /*
     * Reset all minimum requirement
     */
    this.resetAllMinRequirementsToNA = function() {
        for ( var i = 0; i < 25; i++) {
            var obj = "";
            obj = "minReq" + (i + 1);
            $('#' + obj + ' option:eq(0)').prop('selected', true);
        }
    };
    /*
     * Show warning message and navigate to
     * Click of requistion number should navigate to requistion details screen
     */
    $(".reqNbr").on("click", function() {
        // calling the showPopUp method to show warning popup with message
        if (CONSTANTS.phnscrnSaveCheck) {
            CONSTANTS.phonescreenDetailsObj.showUnsavedPopUpReq("");
        } else {
            CONSTANTS.phonescreenDetailsObj.handleUnsavedReqOk("");
        }
    });
    /*
     * Show warning message
     * Click of requistion number should navigate to candidate details screen
     */
    $(".candId").on("click", function() {
        // calling the showPopUp method to show warning popup with message
        if (CONSTANTS.phnscrnSaveCheck) {
            CONSTANTS.phonescreenDetailsObj.showUnsavedPopUpCand("");
        } else {
            CONSTANTS.phonescreenDetailsObj.handleUnsavedCandOk(null);
        }
    });
    /*
     * Click of requistion number should navigate to candidate details screen
     */
    this.navigateToCandidateDetails = function() {
        CONSTANTS.retailStaffingObj.candRefId = $(".candId").text();
        CONSTANTS.retailStaffingObj.applicantId = CONSTANTS.phnScreenDtls[0].cndtNbr;
        $.get('app/RetailStaffing/view/candidateDetails.html', CONSTANTS.phonescreenDetailsObj.setContent);
    };
    /*
     * Click of requistion number should navigate to requistion details screen
     */
    this.navigateToReqScreenDetails = function() {
        CONSTANTS.retailStaffingObj.reqNumber = $(".reqNbr").text();
        CONSTANTS.calledFromSearchPage = true;
        UTILITY.clearCache();
        $.get('app/RSARequisitionDetails/view/requisitionDetails.html', CONSTANTS.phonescreenDetailsObj.setContent);
    };
    /*
     * Display modal popup
     */
    this.showModalPopUp = function(msg) {
        $("#warningpopup").modal("show");
        CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#warningpopup');
        $(".sureMsg").html(msg);
        $("#warningOkbutton").attr("data-dismiss", "modal");
        $("#warningCancelbutton").hide();
    };
    /*
     * Display phone screen details
     */
    this.displayPhnScrnDetails = function(phnScrndet) {
        //Set the service response to UI
        $(".phnDet .screenNbr").text(phnScrndet[0].ScreenNbr);
        $(".phnDet .reqNbr").text(phnScrndet[0].reqNbr);
        $(".phnDet .candName").text(phnScrndet[0].canName);
        $(".phnDet .candId").text(phnScrndet[0].candRefNbr);
        $(".phnDet .extNbr").text(phnScrndet[0].internalExternal);
        $(".phnDet .associateId").text(phnScrndet[0].aid);
        $(".phnDet .strNbr").text(phnScrndet[0].strNbr);
        $(".phnDet .dept").text(phnScrndet[0].dept);
        $(".phnDet .phnNbr").text(CONSTANTS.phonescreenDetailsObj.getFormattedPhoneNumber(phnScrndet[0].canPhn));
        $(".phnDet .title").text(phnScrndet[0].title);
        $(".phnDet .email").val(phnScrndet[0].emailAddr);
        CONSTANTS.phonescreenDetailsObj.isSubmitEmailValidation = false;
        CONSTANTS.phonescreenDetailsObj.emailIdValidation();
    };
    /*
     * Display requisition details
     */
    this.displayRequisitionDetails = function(reqdet, stfdet, langdet) {
        //Set the service response to UI
        $(".reqDetails .strNbr").text(reqdet[0].strNbr);
        $(".reqDetails .dept").text(reqdet[0].dept);
        $(".reqDetails .jobTitle").text(reqdet[0].jobTitle.toString().trim());
        $(".reqDetails .jobCd").text(reqdet[0].job);
        $(".reqDetails .targetPay").text(stfdet[0].targetPay);
        $(".reqDetails .activeFld").text(reqdet[0].reqAct);
        $(".reqDetails .ftFld").text(reqdet[0].ft);
        $(".reqDetails .ptFld").text(reqdet[0].pt);
        $(".reqDetails .screenType").text(reqdet[0].scrnTyp);
        $(".reqDetails .seaTemp").text(reqdet[0].sealTempJob);
        $(".reqDetails .manage").text(reqdet[0].rscToManageFlg);
        $(".reqDetails .schedule").text(reqdet[0].rscSchFlag);
        $(".reqDetails .qualPoolNts").val(stfdet[0].qualPoolNts);
        $(".reqDetails .referals").val(stfdet[0].Referals);
        $('.langCheckboxGrid').empty();
        $('.langCheckboxGrid').createCheckBoxGrid({
            rows : 4,
            labelsArray : langdet,
            cols : 4
        });
        for (var j = 0; j < CONSTANTS.reqnLanguagesArr.length; j++) {
            $(".langCheckboxGrid input[value='" + CONSTANTS.reqnLanguagesArr[j] + "']").prop('checked', true);
        }
        $('.schedulePrefRow').empty();
        $('.schedulePrefRow').createSchedulePreferenceGrid({
            colMd : "4",
            isMandatory : false,
            daysLabel : "Specific Days the candidate MUST be able to work:",
            slotsLabel : "Specific Time Slots the candidate MUST be able to start work:",
            defaultChecked : CONSTANTS.schdPrefDetail
        });
        this.blockFormElements(".weekHeader");
        this.blockFormElements(".slots");
        $('.qualPoolNts').attr('readonly', 'readonly');
        $('.referals').attr('readonly', 'readonly');
    };
    /*
     * Display store location details
     */
    this.displayStoreLocDetails = function(strdet) {
        $(".reqStrLoc .address").text(strdet[0].addr);
        $(".reqStrLoc .state").text(strdet[0].state);
        $(".reqStrLoc #strMgr").text(strdet[0].storeMgr);
        $(".reqStrLoc .city").text(strdet[0].city);
        $(".reqStrLoc .zip").text(strdet[0].zip);
        $(".reqStrLoc .phnNbrReq").text(CONSTANTS.phonescreenDetailsObj.getFormattedNumber(strdet[0].phone));
    };
    /*
     * Display phone screen response
     */
    this.displayPhnScrnresponses = function(phnScrndet, phnRes, stats) {
   
    	$(".phnScnRes .reminderText").text(CONSTANTS.phnScreenDtls[0].phoneScreenBannerNum);
    	$(".phnScnRes .scrnDate").text(CONSTANTS.phnScrnTimeList[0].formattedDate);
        $(".phnScnRes .scrnTime").text(CONSTANTS.phnScrnTimeList[0].formattedTime);
        $(".phnScnRes .screener").text(phnScrndet[0].phnScreener);
        $(".phnScnRes #txtAreaCntHist").text(phnScrndet[0].contactHist);
        $(".phnScnRes #txtAreaPhnDetRes").val(phnScrndet[0].detailResp);
        $('.phnScnRes #txtAreaPhnDetRes').attr('readonly', 'readonly');
        CONSTANTS.getPhoneScrnDispositionStatusDtls ? $("#selectDisposition").empty() : "";
        $("#selectDisposition").selectBoxIt();
        $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
        CONSTANTS.phonescreenDetailsObj.appendOptionToSelect('selectDisposition', 'append', CONSTANTS.getPhoneScrnDispositionStatusDtls, 'desc', 'status');
        var selectedDisposition = phnScrndet.phoneScreenDispositionCode;
        $('#selectDisposition option[value="' + selectedDisposition + '"]').attr("selected", "selected");
        $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
        $('#selectDisposition').attr('disabled', 'disabled');
        $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
        $(".phnScnRes .minStatus").text(CONSTANTS.phnScreenDtls[0].ynStatusdesc);
        $(".phnScnRes .statusDate").text(CONSTANTS.phnScrnStatusTimestamp[0].formattedDate);
        $(".phnScnRes .statusTime").text(CONSTANTS.phnScrnStatusTimestamp[0].formattedTime);
        if (CONSTANTS.interviewStatusTimestamp.length > 0) {
            $(".phnScnRes .intrStatusDate").text(CONSTANTS.interviewStatusTimestamp[0].formattedDate);
            $(".phnScnRes .intrStatusTime").text(CONSTANTS.interviewStatusTimestamp[0].formattedTime);
        }

        $("#scrnstatusSelect").selectBoxIt();
        $("#scrnstatusSelect").data("selectBox-selectBoxIt").refresh();
        CONSTANTS.phonescreenDetailsObj.appendOptionToSelect('scrnstatusSelect', 'append', stats, 'desc', 'phoneScrnStatusCode');

        $("#intrstatusSelect").selectBoxIt();
        $("#intrstatusSelect").data("selectBox-selectBoxIt").refresh();
        CONSTANTS.getInterviewStatusDtls.splice(0, 0, {
            'desc' : '--Select--',
            'status' : '0'
        });
        CONSTANTS.phonescreenDetailsObj.appendOptionToSelect('intrstatusSelect', 'append', CONSTANTS.getInterviewStatusDtls, 'desc', 'status');

        CONSTANTS.minReqCombo = [ "N/A", "Yes", "No" ];
        $("#minReqRow").empty();
        var j = 0;
        for ( var i = 0; i < 10; i++) {
            var row = "";
            j = i + 1;
            var minReq = i < 9 ? "<div class='row' id='minReqDiv'><label class='col-xs-3 text-right minreqRows' id='row" + i + "'>Q0" + j + ":</label>" : "<div class='row'  id='minReqDiv'><label class='col-xs-3 text-right minreqRows' id='row" + i + "'>Q" + j + ":</label>";
            var selectOption = "";

            selectOption = "<select class='col-xs-5 selectMinReq' id='minReq" + j + "' name='minRequirement'>";

            var minReqOptions = "";
            minReqOptions = "<option value='" + i + "'>" + CONSTANTS.minReqCombo[0] + "</option><option value='" + i + "'>" + CONSTANTS.minReqCombo[1] + "</option><option value='" + i + "'>" + CONSTANTS.minReqCombo[2] + "</option>";

            row = minReq + selectOption + minReqOptions + "</div>";
            $("#minReqRow").append(row);
        }
        $('#selectMinReq').attr('disabled', 'disabled');

        for ( var k = 11; k < 26; k++) {
            minReqOptions = "";
            minReqOptions = "<option value='" + k + "'>" + CONSTANTS.minReqCombo[0] + "</option><option value='" + k + "'>" + CONSTANTS.minReqCombo[1] + "</option><option value='" + k + "'>" + CONSTANTS.minReqCombo[2] + "</option>";
            $("#minReq" + k).append(minReqOptions);
        }
        // interview Details
        $("#WkAvl").text(CONSTANTS.getWeekAvail);
        $("#IntDurMin").text(CONSTANTS.phnScrnStaffingList[0].interviewDurtn);
        $("#IntPerTmSlot").text(CONSTANTS.phnScrnStaffingList[0].interviewTmSlt);
        if (CONSTANTS.intrwTimeList.length > 0) {
            $("#intrwTimeHr").val(CONSTANTS.intrwTimeList[0].hour);
            $("#intrwTimeMin").val(CONSTANTS.intrwTimeList[0].minute);
        }
        $("#txtAreaAvail").text(CONSTANTS.phnScrnStaffingList[0].daysTmMgrAvble);
        $("#intrvr").text(CONSTANTS.phnScreenIntrwDtls[0].interViewer);
        $("#intrLocNameLbl").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocName);
        $("#intrLocAddrLbl").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocAddr);
        $("#intrLocCityLbl").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocCity);
        $("#intrLocPhnLbl").text(CONSTANTS.phonescreenDetailsObj.getFormattedNumber(CONSTANTS.phnScreenIntrwDtls[0].intrLocPhn));
        $("#intrLocStateLbl").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocState);
        $("#intrLocZipLbl").text(CONSTANTS.phnScreenIntrwDtls[0].intrLocZip);

    };
    this.valEmailId = function() {
        CONSTANTS.phonescreenDetailsObj.valPhnScreen();
        CONSTANTS.phonescreenDetailsObj.isSubmitEmailValidation = false;
        CONSTANTS.phonescreenDetailsObj.emailIdValidation();
    };
    /*
     * check if any unsaved data exist
     */
    this.unSavedVal = function() {
        CONSTANTS.phonescreenDetailsObj.valPhnScreen();
    };
    /*
     * change the status based on minimum requisition
     */
    this.changeStatus = function() {
        CONSTANTS.phnscrnSaveCheck = true;
        if ($("#minReq1 option:selected").index() === 0) {
            $("#txtAreaPhnDetRes").val("");
            $("#txtAreaPhnDetRes").attr("readonly", "readonly");
            $("#txtAreaPhnDetRes").css("background-color", "#F3F3F3");
            $(".screener").text("");
            $('#selectDisposition option:eq(0)').prop('selected', true);
            $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
            $('#selectDisposition').attr("disabled", "disabled");
            $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
        } else {
            $(".screener").text(CONSTANTS.userProfile.userId);
            $("#txtAreaPhnDetRes").removeAttr("readonly");
            $("#txtAreaPhnDetRes").css("background-color", "#FFFFFF");
            $('#selectDisposition').removeAttr("disabled");
            $("#selectDisposition").data("selectBox-selectBoxIt").refresh();
        }

        if (CONSTANTS.phonescreenDetailsObj.isAllMinRequirementsNA()) {
            $("#ynMinReqStat").text("");
        } else if (CONSTANTS.phonescreenDetailsObj.isAnyPositionSpecificRequirementsNo() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificNonSchdRequirementsNo() || (CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificSchdRequirementsNo() && CONSTANTS.phonescreenDetailsObj.isNeedAccommodationNo())) {
            $("#ynMinReqStat").text(CONSTANTS.DO_NOT_PROCEED);
            $('#scrnstatusSelect option:eq(1)').prop('selected', true);
            $("#scrnstatusSelect").data("selectBox-selectBoxIt").refresh();
        } else if (CONSTANTS.phonescreenDetailsObj.isAnyPositionSpecificRequirementsYesOrNA() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificNonSchdRequirementsYesOrNA() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificSchdRequirementsYesOrNA()) {
            $("#ynMinReqStat").text(CONSTANTS.PROCEED);
        }
        CONSTANTS.phonescreenDetailsObj.setResetNeedAccommodation();
    };
    
    this.setStatusOnOpen = function() {
        if (CONSTANTS.phonescreenDetailsObj.isAllMinRequirementsNA()) {
            $("#ynMinReqStat").text("");
        } else if (CONSTANTS.phonescreenDetailsObj.isAnyPositionSpecificRequirementsNo() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificNonSchdRequirementsNo() || (CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificSchdRequirementsNo() && CONSTANTS.phonescreenDetailsObj.isNeedAccommodationNo())) {
            $("#ynMinReqStat").text(CONSTANTS.DO_NOT_PROCEED);
            $('#scrnstatusSelect option:eq(1)').prop('selected', true);
            $("#scrnstatusSelect").data("selectBox-selectBoxIt").refresh();
        } else if (CONSTANTS.phonescreenDetailsObj.isAnyPositionSpecificRequirementsYesOrNA() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificNonSchdRequirementsYesOrNA() || CONSTANTS.phonescreenDetailsObj.isAnyReqSpecificSchdRequirementsYesOrNA()) {
            $("#ynMinReqStat").text(CONSTANTS.PROCEED);
        }
        CONSTANTS.phonescreenDetailsObj.setResetNeedAccommodation();
    };
    
    /*
     * Set Reset need accomadationflag based on minimum requisition
     */
    this.setResetNeedAccommodation = function() {
        // When Need Accommodation is enabled check to see if all of the
        // Schedule Specific questions are NA or Yes, if true the disable Need
        // Accommodation and reset to NA.
        var i = 0;
        var returnArr = [];
        var returnValue = false;
        if ($("#minReq23[disabled]").length === 0) {
            for (i = 14; i < 23; i++) {
                if ($("#minReq" + i + " option:selected").index() === 1 || $("#minReq" + i + " option:selected").index() === 0) {
                    returnArr.push(returnValue);
                }
            }
            if (returnArr.length === 9) {
                $('#minReq23 option:eq(0)').prop('selected', true);
                $("#minReq23").attr("disabled", "disabled");
            }
        } else {
            // If any of the Schedule Specific questions are No, enable Need
            // Accommodation
            for (i = 14; i < 23; i++) {
                var obj = "";
                obj = "minReq" + i;
                if ($("#" + obj + " option:selected").index() === 2) {
                    $("#minReq23").removeAttr("disabled");
                }
            }
            //Added for IVR Chnages Dec 2015
            //Must disable Need Accommodation if the Phone Screen Status is the following
            if ($('#scrnstatusSelect').val() == 4 || $('#scrnstatusSelect').val() == 5 || $('#scrnstatusSelect').val() == 8 ||
            	$('#scrnstatusSelect').val() == 9 || $('#scrnstatusSelect').val() == 10 || $('#scrnstatusSelect').val() == 11 ||
            	$('#scrnstatusSelect').val() == 12) {
            		$("#minReq23").attr("disabled", "disabled");
            }
        }
    };

    $("#minReqRow").off('click');
    $("#minReqRow").on('click', '.selectMinReq', function(e) {
        CONSTANTS.phonescreenDetailsObj.unSavedVal(e);
    });

    $("#minReqRow").off('change');
    $("#minReqRow").on('change', '.selectMinReq', function(e) {
        CONSTANTS.phonescreenDetailsObj.changeStatus(e);
    });
/*
 * Dynamically append option for selection
 */
    this.appendOptionToSelect = function(drpDownId, action, optionsArray, textKey, valueKey) {
        if (action === "append") {
            for ( var i = 0; i < optionsArray.length; i++) {
                var currentObj = optionsArray[i];
                $('#' + drpDownId).append($("<option>", {
                    text : currentObj[textKey],
                    value : currentObj[valueKey]
                }));
                if (valueKey === "hireEventId") {
                    currentObj.enabled ? '' : $('#' + drpDownId + ' option[value=' + currentObj.valueKey + '] ').attr("disabled", "disabled");
                }
            }
            $('#' + drpDownId).attr('disabled', false);
        } else {
            $('#' + drpDownId).find('option').remove();
            $('#' + drpDownId).attr('disabled', true);
        }
        $('#' + drpDownId).data("selectBox-selectBoxIt").refresh();
    };
/*
 * display warning message and back to home screen on clik of OK button
 */
    this.goToHome = function(event) {
        if (event.target.value === "Close") {
            CONSTANTS.isCloseBtnClick = true;
        } else {
            CONSTANTS.isCloseBtnClick = false;
        }
        if (CONSTANTS.phnscrnSaveCheck) {
            // calling the showPopUp method to show warning popup with message
            CONSTANTS.phonescreenDetailsObj.showUnsavedPopUpHome("");
        } else {
            CONSTANTS.phonescreenDetailsObj.handleUnsavedHomeOk(null);
        }
    };
    $("#goHome").off("click");
    $("#goHome").show().on("click",function(e){
    	UTILITY.RemoveQS();
        CONSTANTS.phonescreenDetailsObj.goToHome(e);
    });
    $(".navCloseBtn").off("click");
    $(".navCloseBtn").show().on("click",function(e){
        CONSTANTS.phonescreenDetailsObj.goToHome(e);
    });
    $("#cancelPhnDet").off("click");
    $("#cancelPhnDet").show().on("click",function(e){
        CONSTANTS.phonescreenDetailsObj.goToHome(e);
    });
    /*
     * display warning message and back to interview screen on clik of OK button
     */
    $('#intrwDet').click(function() {
        // calling the showPopUp method to show warning popup with message
        if (CONSTANTS.phnscrnSaveCheck) {
            CONSTANTS.phonescreenDetailsObj.showUnsavedPopUpInt("");
        } else {
            CONSTANTS.phonescreenDetailsObj.handleUnsavedInterviewOk("");
        }
    });
    /*
     * show warning message based on requisition
     */
    this.showRequisitionSpecificPopUp = function(btnClickDiff) {
        var requisitionSpecificIsNa = false;
        
        // remove the query string if so any from the URL.
        UTILITY.RemoveQS();
        
        if (btnClickDiff === CONSTANTS.evntTypPhnScrnSave) {
            CONSTANTS.savePhnScrnBtnClickFlg = true;
            CONSTANTS.submitPhnScrnBtnClickFlg = false;
        } else {
            CONSTANTS.submitPhnScrnBtnClickFlg = true;
            CONSTANTS.savePhnScrnBtnClickFlg = false;
        }

        for ( var i = 11; i < 25; i++) {
            var obj = "";
            obj = "minReq" + i;
            if (!$("#" + obj + "[disabled]").length > 0 && $("#" + obj + " option:selected").index() === 0) {
                requisitionSpecificIsNa = true;
            }
        }

        if (requisitionSpecificIsNa) {
            $('#unSaveWarnModalSave .unSaveWarnMsg').empty();
            $('#unSaveWarnModalSave .unSaveWarnMsg').append("<p>Not all Requisition Specific questions have been answered. Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
            $('#unSaveWarnModalSave').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalSave');
        } else {
            CONSTANTS.phonescreenDetailsObj.showPhoneScreenDispositionPopUp("");
        }
    };
    /*
     * show phone screen disposition popup
     */
    this.showPhoneScreenDispositionPopUp = function(btnClickDiff) {
        $('#unSaveWarnModalSave').modal('hide');
        if($(".modal-backdrop.fade.in").length > 2)
        	{
        	$($(".modal-backdrop.fade.in")[0]).empty().remove();
        	}
        if ($("#selectDisposition option:selected").index() === 0) {
            $('#unSaveWarnModalSubmit .unSaveWarnMsg').empty();
            $('#unSaveWarnModalSubmit .unSaveWarnMsg').append("<p>Phone Screen Disposition has not been selected. Click OK to Continue,</p><p>CANCEL to stay on current page</p>");
            $('#unSaveWarnModalSubmit').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalSubmit');
        } else {
            CONSTANTS.phonescreenDetailsObj.submitPhnScrnDtls(btnClickDiff);
        }
    };
    /*
     * Decode special characters
     */
    this.decodeSpecialCharacters = function(textToFormat) {
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
 * Validate email box
 */
    this.emailBoxValidate = function(msg) {
        $('.email').addClass('redBorder');
        $('[data-toggle="tooltip"]').attr("title", msg);
        $('[data-toggle="tooltip"]').attr("data-original-title", msg);
        $('[data-toggle="tooltip"]').tooltip();
        $('[data-toggle="tooltip"]').tooltip('show');
        if (CONSTANTS.phonescreenDetailsObj.isSubmitEmailValidation) {
            CONSTANTS.phonescreenDetailsObj.isSubmitEmailValidation = false;
            $('#alertPopupModal').modal('show');
            $('.alertModalBody').empty();
            $("#alertPopupModal .modal-header").text("Invalid Values");
            $(".alertModalBody").append("<p>" + msg + "</p>");
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#alertPopupModal');
        }
    };
/*
 * Format Email
 */
    this.emailFormattingCheck = function(dotSplit) {
        return ((dotSplit.length === 2 && dotSplit[dotSplit.length - 1].length > 6) || (dotSplit.length > 2 && dotSplit[dotSplit.length - 1].length > 4));
    };
/*
 * Validate Email
 */
    this.emailIdValidation = function() {
        if ($("#emailId").val() !== "" && $("#emailId").val().trim().length > 0) {
            var isInValidEmailId = false;
            isInValidEmailId = CONSTANTS.phonescreenDetailsObj.validateEmailId();
            if (isInValidEmailId) {
                return true;
            }
        }
        $('.email').removeClass('redBorder');
        $('[data-toggle="tooltip"]').removeAttr("title");
        $('[data-toggle="tooltip"]').removeAttr("data-original-title");
        $('[data-toggle="tooltip"]').tooltip();
        $('[data-toggle="tooltip"]').tooltip('hide');
    };
/*
 * Validate Email Id
 */
    this.validateEmailId = function() {
        var ampPos = $("#emailId").val().indexOf('@');
        var isReturn = false;
        isReturn = CONSTANTS.phonescreenDetailsObj.atSignInEmail(ampPos);
        if (isReturn) {
            isReturn = false;
            return true;
        }
        // Separate the address into username and domain.
        username = $("#emailId").val().toString().substring(0, ampPos);
        domain = $("#emailId").val().toString().substring(ampPos + 1);
        // Validate username has no illegal characters
        // and has at least one character.
        var usernameLen = username.length;
        isReturn = CONSTANTS.phonescreenDetailsObj.usernameInEmail(username, usernameLen);
        if (isReturn) {
            isReturn = false;
            return true;
        }
        var domainLen = domain.length;
        // check for IP address
        if (CONSTANTS.phonescreenDetailsObj.chkDomainExists(domain, domainLen))
        {
            // Validate IP address
            if (!CONSTANTS.phonescreenDetailsObj.isValidIPAddress(domain.toString().substring(1, domainLen - 1)))
            {
                CONSTANTS.phonescreenDetailsObj.emailBoxValidate("The IP domain in your e-mail address is incorrectly formatted.");
                return true;
            }
        } else {
            isReturn = CONSTANTS.phonescreenDetailsObj.periodsInEmail(domain);
            if (isReturn) {
                isReturn = false;
                return true;
            }
        }
    };
    this.chkDomainExists = function(domain, domainLen) {
        return ((domain.charAt(0) === "[") && (domain.charAt(domainLen - 1) === "]"));
    };
    this.usernameInEmail = function(username, usernameLen) {
        if (usernameLen === 0) {
            CONSTANTS.phonescreenDetailsObj.emailBoxValidate("The username in your e-mail address is missing.");
            return true;
        }
        for (var i = 0; i < usernameLen; i++) {
            if (CONSTANTS.DISALLOWED_LOCALNAME_CHARS.indexOf(username.charAt(i)) !== -1) {
                CONSTANTS.phonescreenDetailsObj.emailBoxValidate("Your e-mail address contains invalid characters.");
                return true;
            }
        }
    };
    this.atSignInEmail = function(ampPos) {
        if (ampPos === -1) {
            CONSTANTS.phonescreenDetailsObj.emailBoxValidate("An at sign (@) is missing in your e-mail address.");
            return true;
        } else if ($("#emailId").val().indexOf("@", ampPos + 1) !== -1) {
            CONSTANTS.phonescreenDetailsObj.emailBoxValidate("Your e-mail address contains too many @ characters.");
            return true;
        }
    };
    this.lastDomainInEmail = function(domain, periodPos) {
        var nextPeriodPos = 0;
        var lastDomain = "";
        while (true) {
            nextPeriodPos = domain.indexOf(".", periodPos + 1);
            if (nextPeriodPos === -1)
            {
                lastDomain = domain.toString().substring(periodPos + 1);
                if (lastDomain.length !== 3 && lastDomain.length !== 2 && lastDomain.length !== 4 && lastDomain.length !== 6) {
                    CONSTANTS.phonescreenDetailsObj.emailBoxValidate("The domain in your e-mail address is incorrectly formatted.");
                    return true;
                }
                break;
            }
            else if (nextPeriodPos === periodPos + 1)
            {
                CONSTANTS.phonescreenDetailsObj.emailBoxValidate("The domain in your e-mail address has consecutive periods.");
                return true;
            }
            periodPos = nextPeriodPos;
        }
    };
    this.illegalCharsInEmail = function(domain, domainLen) {
        // Check that there are no illegal characters in the domain.
        for (var i = 0; i < domainLen; i++)
        {
            if (CONSTANTS.DISALLOWED_DOMAIN_CHARS.indexOf(domain.charAt(i)) !== -1)
            {
                CONSTANTS.phonescreenDetailsObj.emailBoxValidate("Your e-mail address contains invalid characters.");
                return true;
            }
        }
        // Check that the character immediately after the @ is not a period.
        if (domain.charAt(0) === ".")
        {
            CONSTANTS.phonescreenDetailsObj.emailBoxValidate("The domain in your e-mail address is incorrectly formatted.");
            return true;
        }
    };
    this.periodsInEmail = function(domain) {
        // Must have at least one period
        var periodPos = domain.indexOf(".");
        var domainLen = domain.length;
        if (periodPos === -1) {
            CONSTANTS.phonescreenDetailsObj.emailBoxValidate("The domain in your e-mail address is missing a period.");
            return true;
        }
        var isReturn = false;
        isReturn = CONSTANTS.phonescreenDetailsObj.lastDomainInEmail(domain, periodPos);
        if (isReturn) {
            isReturn = false;
            return true;
        }
        isReturn = CONSTANTS.phonescreenDetailsObj.illegalCharsInEmail(domain, domainLen);
        if (isReturn) {
            isReturn = false;
            return true;
        }
    };
    this.formIpArray = function(ipAddr, seperator,ipArray) {
        while (true)
        {
            newpos = ipAddr.toString().indexOf(seperator, CONSTANTS.phonescreenDetailsObj.pos);
            if (newpos !== -1)
            {
                ipArray.push(ipAddr.toString().substring(CONSTANTS.phonescreenDetailsObj.pos,newpos));
            }
            else
            {
                ipArray.push(ipAddr.toString().substring(CONSTANTS.phonescreenDetailsObj.pos));
                break;
            }
            CONSTANTS.phonescreenDetailsObj.pos = newpos + 1;
        }
        return ipArray;
    };

    this.isValidIPAddress = function(ipAddr) {
        var ipArray = [];
        CONSTANTS.phonescreenDetailsObj.pos = 0;
        var newpos = 0;
        var item = 0;
        var n = 0;
        // if you have :, you're in IPv6 mode
        // if you have ., you're in IPv4 mode=
        if (ipAddr.indexOf(":") !== -1)
        {
            var hasUnlimitedZeros = (ipAddr.indexOf("::") !== -1);
            if (hasUnlimitedZeros)
            {
                ipAddr = ipAddr.toString().replace(/^::/, "");
                ipAddr = ipAddr.toString().replace(/::/g, ":");
            }
            return CONSTANTS.phonescreenDetailsObj.ipAddrWithColon(ipAddr, hasUnlimitedZeros, n, item, ipArray);
        }
        if (ipAddr.indexOf(".") !== -1) {
            return CONSTANTS.phonescreenDetailsObj.ipAddrWithDot(ipAddr, n, item, ipArray);
        }
        return false;
    };

    this.ipAddrWithColon = function(ipAddr, hasUnlimitedZeros, n, item, ipArray) {
        // IPv6=
        // validate by splitting on the colons
        // to make it easier, since :: means zeros,
        // lets rid ourselves of these wildcards in the beginning
        // and then validate normally
        // get rid of unlimited zeros notation so we can parse better        
        var seperator = ":";
        ipArray = CONSTANTS.phonescreenDetailsObj.formIpArray(ipAddr, seperator, ipArray);
        n = ipArray.length;
        var lastIsV4 = ipArray[n-1].indexOf(".") !== -1;
        if (lastIsV4) {
            CONSTANTS.phonescreenDetailsObj.hasReturn = false;
            var returnVal = CONSTANTS.phonescreenDetailsObj.ipAddrWithColonAndDot(ipArray, hasUnlimitedZeros, n, item);
            if (CONSTANTS.phonescreenDetailsObj.hasReturn) {
                return returnVal;
            }
        }
        else {
            CONSTANTS.phonescreenDetailsObj.hasReturn = false;
            var returnValue = CONSTANTS.phonescreenDetailsObj.ipAddrWithColonWithoutDot(ipArray, hasUnlimitedZeros, n, item);
            if (CONSTANTS.phonescreenDetailsObj.hasReturn) {
                return returnValue;
            }
        }
        return true;
    };

    this.ipAddrWithColonAndDot = function(ipArray, hasUnlimitedZeros, n, item) {
        // if no wildcards, length must be 7
        // always, never more than 7
        var len = 7;
        if (CONSTANTS.phonescreenDetailsObj.chkIpArrayLen(ipArray, hasUnlimitedZeros, len)) {
            CONSTANTS.phonescreenDetailsObj.hasReturn = true;
            return false;
        }
        for (var i = 0; i < n; i++) {
            if (i === n-1) {
                // IPv4 part...
                CONSTANTS.phonescreenDetailsObj.hasReturn = true;
                return CONSTANTS.phonescreenDetailsObj.isValidIPAddress(ipArray[i]);
            }
            item = parseInt(ipArray[i], 16);
            if (item !== 0) {
                CONSTANTS.phonescreenDetailsObj.hasReturn = true;
                return false;
            }
        }
    };

    this.ipAddrWithColonWithoutDot = function(ipArray, hasUnlimitedZeros, n, item) {
        var size = 0;
        // if no wildcards, length must be 8
        // always, never more than 8
        var len = 8;
        if (CONSTANTS.phonescreenDetailsObj.chkIpArrayLen(ipArray, hasUnlimitedZeros, len)) {
            CONSTANTS.phonescreenDetailsObj.hasReturn = true;
            return false;
        }
        for (var i = 0; i < n; i++) {
            item = parseInt(ipArray[i], 16);
            size = 65535;
            if (CONSTANTS.phonescreenDetailsObj.chkItemSize(item, size)) {
                CONSTANTS.phonescreenDetailsObj.hasReturn = true;
                return false;
            }
        }
    };

    this.ipAddrWithDot = function(ipAddr, n, item, ipArray) {
        var size = 0;
        // IPv4
        // validate by splling on the periods
        var seperator = ".";
        ipArray = CONSTANTS.phonescreenDetailsObj.formIpArray(ipAddr, seperator, ipArray);
        if (ipArray.length !== 4) {
            return false;
        }
        n = ipArray.length;
        for (var i = 0; i < n; i++) {
            item = parseInt(ipArray[i]);
            size = 255;
            if (CONSTANTS.phonescreenDetailsObj.chkItemSize(item, size)) {
                return false;
            }
        }
        return true;
    };

    this.chkIpArrayLen = function(ipArray, hasUnlimitedZeros, len) {
        return ((ipArray.length !== len && !hasUnlimitedZeros) || (ipArray.length > len));
    };
    this.chkItemSize = function(item, size) {
        return (isNaN(item) || item < 0 || item > size);
    };

    this.chkMinResCnthist = function() {
        var cond1 = ($("#minReq1 option:selected").index() === 0 && ($("#txtAreaPhnDetRes").val() === "" || $("#txtAreaPhnDetRes[readonly]").length > 0 || !$("#txtAreaPhnDetRes").val().trim().length > 0));
        return (cond1 && ($("#txtAreaCntHist").val() === "" || $("#txtAreaCntHist").val().trim().length <= 0));
    };

    this.chkPhnScrnDtlResp = function() {
        var cond1 = ($("#minReq1 option:selected").index() !== 0 && ($("#txtAreaCntHist").val() !== "" && $("#txtAreaCntHist").val().trim().length > 0));
        return (cond1 && ($("#txtAreaPhnDetRes").val() === "" || !$("#txtAreaPhnDetRes").val().trim().length > 0));
    };

    this.cntHistPhnDtlResValidation = function() {
        if (CONSTANTS.phonescreenDetailsObj.chkMinResCnthist()) {
            // calling the showPopUp method to show warning popup with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter Minimum Responses/Contact History</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            return true;
        }
        if (CONSTANTS.phonescreenDetailsObj.chkPhnScrnDtlResp()) {
            // calling the showPopUp method to show warning popup with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter The Phone Screen Detail Response</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            return true;

        }
    };

    this.chkMinResPhnRespHist = function() {
        var cond1 = ($("#minReq1 option:selected").index() !== 0 && ($("#txtAreaPhnDetRes").val() && $("#txtAreaPhnDetRes").val() !== "" && $("#txtAreaPhnDetRes").val().trim().length > 0));
        return (cond1 || ($("#txtAreaCntHist").val() !== "" && $("#txtAreaCntHist").val().trim().length > 0));
    };

    this.minResCntHistPhnResValidation = function() {
        if (!CONSTANTS.phonescreenDetailsObj.chkMinResPhnRespHist()) {
            // validate the phone screen date ,phone screen time and phone
            // screener if min responses are answered
            // For the validation to check whether the first min req should not
            // be Not Applicable
            if ($("#minReq1 option:selected").index() === 0) {
                // calling the showPopUp method to show warning popup with
                // message
                $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
                $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Selecting First Minimum Requirement Is Mandatory</p>");
                $('#unSaveWarnModalValidate').modal('show');
                CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
                return true;

            }

            if ($("#txtAreaPhnDetRes").val() === "" || !$("#txtAreaPhnDetRes").val().trim().length > 0) {
                // calling the showPopUp method to show warning popup with
                // message
                $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
                $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter The Phone Screen Detail Response</p>");
                $('#unSaveWarnModalValidate').modal('show');
                CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
                return true;
            }

            if ($("#txtAreaCntHist").val() === "" || !$("#txtAreaCntHist").val().trim().length > 0) {
                // calling the showPopUp method to show warning popup with
                // message
                $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
                $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter Contact History</p>");
                $('#unSaveWarnModalValidate').modal('show');
                CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
                return true;
            }
        }
    };

    this.validatePhnNumber = function() {
        var val1 = ($("#phnFirst").val() === "" || $("#phnSecond").val() === "" || $("#phnThird").val() === "" || !$("#phnFirst").val().trim().length > 0);
        return (val1 || !$("#phnSecond").val().trim().length > 0 || !$("#phnThird").val().trim().length > 0);
    };

    this.validateIntrvwLocDtls = function(id) {
        return ($(id).val() === "" || $(id).val().trim().length <= 0);
    };

    this.validatePhnNoLength = function() {
        return ($("#phnFirst").val().trim().length !== 3 || $("#phnSecond").val().trim().length !== 3 || $("#phnThird").val().trim().length !== 4);
    };

    this.intrvwLocPhnValidation = function() {
        var isReturn = false;
        if (CONSTANTS.phonescreenDetailsObj.validateIntrvwLocDtls("#intrLocName")) {
            // calling the showPopUp method to show warning popup
            // with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter The Interview Location Name</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        } else if (CONSTANTS.phonescreenDetailsObj.validatePhnNumber() || CONSTANTS.phonescreenDetailsObj.validatePhnNoLength()) {
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter Ten digit Phone Number</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        } else if (CONSTANTS.phonescreenDetailsObj.validateIntrvwLocDtls("#intrLocAddr")) {
            // calling the showPopUp method to show warning popup
            // with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter The Interview Location Address</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;

        } else if (CONSTANTS.phonescreenDetailsObj.validateIntrvwLocDtls("#intrLocCity")) {
            // calling the showPopUp method to show warning popup
            // with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter The Interview Location City</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;

        } else if (CONSTANTS.phonescreenDetailsObj.validateIntrvwLocDtls("#intrLocState")) {
            // calling the showPopUp method to show warning popup
            // with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter The Interview Location State</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        } else if (CONSTANTS.phonescreenDetailsObj.validateIntrvwLocDtls("#intrLocZip") || $("#intrLocZip").val().trim().length < 4) {
            // calling the showPopUp method to show warning popup
            // with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter The Interview Location Zip Code</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        }
        return isReturn;
    };

    this.validateIntrwDate = function(id) {
        return ($(id).val() !== "" && $(id).val().trim().length > 0);
    };

    this.splittedDateValidation = function() {
        return (CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intrLocName") || CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#phnFirst") || CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#phnSecond") || CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#phnThird"));
    };

    this.intrvwDateValidation = function() {
        var val1 = (CONSTANTS.phonescreenDetailsObj.splittedDateValidation());
        var val2 = (CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intrLocAddr") || CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intrLocCity") || CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intrLocState") || CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intrLocZip"));
        if (($("#intrLocPhn1").hasClass("show") && CONSTANTS.phonescreenDetailsObj.validateIntrvwLocDtls("#intDate")) && (val1 || val2)) {
            // calling the showPopUp method to show warning popup
            // with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter Interview Date</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            return true;
        }
    };

    this.validateIntrwTime = function() {
        return ($("#intrwTimeHr").val() === "" || !$("#intrwTimeMin").val()  || !$("#intrwTimeHr").val().trim().length > 0 || !$("#intrwTimeMin").val().trim().length > 0);
    };

    this.validateIntrvwTimeFormat = function() {
        return ($("#intrwTimeHr").val() !== "" || $("#intrwTimeMin").val() !== "" || !$("#intrwTimeHr").val().trim().length > 0 || !$("#intrwTimeMin").val().trim().length > 0);
    };

    this.validateUserDate = function() {
        var val1 = (($("#intrwTimeHr").val() !== "" && $("#intrwTimeMin").val() !== "") && ($("#intrwTimeHr").val().trim().length > 0 && $("#intrwTimeMin").val().trim().length > 0));
        return ((!$("#intDate").val()  || !$("#intDate").val().trim().length > 0) && val1);
    };

    this.validateUsertime = function() {
        return (($("#intrwTimeHr").val() === "" && $("#intrwTimeMin").val() === "") || (!$("#intrwTimeHr").val().trim().length > 0 && !$("#intrwTimeMin").val().trim().length > 0));
    };

    this.chkSelectedAmPm = function() {
        var val = (($("#intrwTimeHr").val() !== "" && $("#intrwTimeMin").val() !== "") && ($("#intrwTimeHr").val().trim().length > 0 && $("#intrwTimeMin").val().trim().length > 0));
        return (CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intDate") && val && $("#amPMIntrw option:selected").index() === 0);
    };

    this.validateIntrvwFrMin = function(hr, min) {
        return ((hr < 0 || hr > 12) || (min < 0 || min > 59));
    };
    /*
     * Interview Date time validation
     * 1.Validate Interview Date
     * 2.Validate Interview Time format
     * 3.Validate User Date
     * 4.Validate Interview Date and user time
     * 5.Validate selected AM PM
     * 6.Validate Requisition Number
     */
    this.intrvwDateTimeValidation = function() {
        var isReturn = false;
        if (CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intDate") && CONSTANTS.phonescreenDetailsObj.validateIntrwTime()) {
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter interview Date and Time</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        } else if (CONSTANTS.phonescreenDetailsObj.validateIntrvwTimeFormat()) {
            var hr = parseInt($("#intrwTimeHr").val());
            var min = parseInt($("#intrwTimeMin").val());
            if (CONSTANTS.phonescreenDetailsObj.validateIntrvwFrMin(hr, min)) {
                $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
                $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter Time In 12 Hour Format</p>");
                $('#unSaveWarnModalValidate').modal('show');
                CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
                isReturn = true;
            }
        } else if (CONSTANTS.phonescreenDetailsObj.validateUserDate()) {
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<p>Please Enter interview Date</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        }
        // check for time when date is entered
        else if (CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intDate") && CONSTANTS.phonescreenDetailsObj.validateUsertime()) {
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<pPlease Enter interview Date and Time</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        } else if (CONSTANTS.phonescreenDetailsObj.chkSelectedAmPm()) {
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<pPlease Select AM or PM for Interview Time</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        } else if (CONSTANTS.phonescreenDetailsObj.validateReqNbr()) {
            // calling the showPopUp method to show warning
            // popup with message
            $('#unSaveWarnModalValidate .unSaveWarnMsg').empty();
            $('#unSaveWarnModalValidate .unSaveWarnMsg').append("<pPlease Enter The Interviewer</p>");
            $('#unSaveWarnModalValidate').modal('show');
            CONSTANTS.phonescreenDetailsObj.promptStaticDraggable('#unSaveWarnModalValidate');
            isReturn = true;
        }
        return isReturn;
    };
    /*
     * validate requisition number
     */
    this.validateReqNbr = function() {
        return (CONSTANTS.phonescreenDetailsObj.validateIntrwDate("#intDate") && parseInt(CONSTANTS.phnScreenDtls[0].reqNbr) < 150000000 && CONSTANTS.phonescreenDetailsObj.validateIntrvwLocDtls("#intrvr"));
    };
    /*
     * validate interview location phone screen date
     */
    this.validateIntLocPhnAndDate = function() {
        return ($("#intrLocPhn1").hasClass("show") && $("#intDate").val() !== "" && $("#intDate").val().trim().length > 0);
    };
/*
 * validate interview status
 */
    this.validateIntStatusAndSection = function() {
        return ($("#intrstatusSelect option:selected").text() === "INTERVIEW SCHEDULED" && $(".intDet .blockUI").length > 0);
    };
/*
 * Check interview status and Section
 */
    this.chkIntStatusAndIntSection = function() {
        var isAlertThrown = false;
        if (CONSTANTS.phonescreenDetailsObj.validateIntStatusAndSection()) {
            // To Validate the interview location Details when the interview
            // date is populated.
            if (CONSTANTS.phonescreenDetailsObj.validateIntLocPhnAndDate()) {
                isAlertThrown = CONSTANTS.phonescreenDetailsObj.intrvwLocPhnValidation();
                if (isAlertThrown) {
                    return true;
                }
            }
            // /Added for date check
            isAlertThrown = CONSTANTS.phonescreenDetailsObj.intrvwDateValidation();
            if (isAlertThrown) {
                return true;
            }
            isAlertThrown = CONSTANTS.phonescreenDetailsObj.intrvwDateTimeValidation();
            if (isAlertThrown) {
                return true;
            }
        }
    };
/*
 * Validate email Id and Phone screen details
 */
    this.fieldsValidation = function() {
        // Email validation
        CONSTANTS.phonescreenDetailsObj.isSubmitEmailValidation = true;
        var isAlertThrown = CONSTANTS.phonescreenDetailsObj.emailIdValidation();
        if (isAlertThrown) {
            return true;
        }
        isAlertThrown = CONSTANTS.phonescreenDetailsObj.cntHistPhnDtlResValidation();
        if (isAlertThrown) {
            return true;
        }
        isAlertThrown = CONSTANTS.phonescreenDetailsObj.minResCntHistPhnResValidation();
        if (isAlertThrown) {
            return true;
        }
    };
/*
 * Submit phone screen details
 */
    this.submitPhnScrnDtls = function(btnClickDiff) {
        $('#unSaveWarnModalSubmit').modal('hide');
        $('#unSaveWarnModalSave').modal('hide');
        if($(".modal-backdrop.fade.in").length > 2)
    	{
    	$($(".modal-backdrop.fade.in")[0]).empty().remove();
    	}
        $('.email').removeClass('redBorder');

        var isAlertThrown = CONSTANTS.phonescreenDetailsObj.fieldsValidation();
        if (isAlertThrown) {
            return;
        }

        // validate the phone screen if Interview Status is Interview Scheduled
        // and if section is enabled
        isAlertThrown = CONSTANTS.phonescreenDetailsObj.chkIntStatusAndIntSection();
        if (isAlertThrown) {
            return;
        }

        var saveobj = {}; // SavePhnScrnDetailsVO
        saveobj = _.extend(saveobj, new savePhnScrDtlsCmd(saveobj));

        // Added for displaying an alert after create phone screen if n+2 and
        // n+1 condition gets satisfied
        var openings = CONSTANTS.getAuthPosCnt;

        if (openings > 0) {
            var savePhnScrnDetails = CONSTANTS.phonescreenDetailsObj.savePhnScnDtlsRequest(saveobj);
            CONSTANTS.phonescreenDetailsObj.updatePhnScrnDtls(savePhnScrnDetails);
            CONSTANTS.phonescreenDetailsObj.resetIntrwDtls(btnClickDiff);
        }
    };
/*
 * Reset interview details
 */
    this.resetIntrwDtls = function(btnClickDiff) {
        if (btnClickDiff === CONSTANTS.evntTypSubmitPhnScrn) {
            $("#intrLocName").text("");
            $("#intrLocAddr").text("");
            $("#intrLocCity").text("");
            $("#phnFirst").text("");
            $("#phnSecond").text("");
            $("#phnThird").text("");
            $("#intrLocState").text("");
            $("#intrLocZip").text("");
            CONSTANTS.phonescreenDetailsObj.resetAllMinRequirementsToNA();
            $("#intrwTimeHr").val("");
            $("#intrwTimeMin").val("");
            $(".screener").text("");
        }
    };
    /*
     * Construct object for phone screen request
     */
    this.savePhnScnDtlsRequest = function(saveobj) {
        var savePhnScrnDetails = {
            "UpdatePhoneScrnDtlsRequest" : {
                "insertUpdate" : "UPDATE",
                "PhoneScreenIntrwDetail" : {
                    "MinimumResponseDtlList" : {
                        "MinimumResponseDtl" : CONSTANTS.minResponseList
                    },
                    "emailAdd" : saveobj.emailAddr,
                    "InterviewLocDtls" : {
                        "interviewLocName" : saveobj.locName,
                        "interviewer" : saveobj.interviewer,
                        "interviewLocId" : saveobj.interviewLocTypCd,
                        "add" : saveobj.intrwAddr,
                        "city" : saveobj.intrwCity,
                        "phone" : saveobj.intrwPhn,
                        "state" : saveobj.intrwState,
                        "zip" : saveobj.intrwZip
                    },
                    "reqNbr" : saveobj.reqNbr,
                    "itiNbr" : saveobj.phnScrnNbr,
                    "cndtNbr" : saveobj.canNbr,
                    "phoneScreenStatusCode" : saveobj.phoneScreenStat,
                    "phoneScreenDispositionCode" : saveobj.phnScrnDispCode,
                    "interviewStatusCode" : saveobj.intviewStat,
                    "interviewMaterialStatusCode" : saveobj.intviewMatStat,
                    "contactHistoryTxt" : saveobj.contactHist,
                    "detailTxt" : saveobj.phnDtlResp,
                    "aid" : saveobj.aid,
                    "name" : saveobj.canName,
                    "canPhn" : saveobj.canPhn,
                    "ynstatus" : saveobj.ynStatusCode,
                    "cndStrNbr" : saveobj.store,
                    "interviewLocTypCd" : saveobj.interviewLocTypCd,
                    "canStatus" : "Y",
                    "reqCalId" : saveobj.reqCalId

                }
            }
        };
        if (saveobj.phnScreener) {
            savePhnScrnDetails.UpdatePhoneScrnDtlsRequest.PhoneScreenIntrwDetail.phnScreener = saveobj.phnScreener;
        }
        if (saveobj.intrwDateVO) {
            savePhnScrnDetails.UpdatePhoneScrnDtlsRequest.PhoneScreenIntrwDetail.InterviewLocDtls.interviewDate = {
                "month" : saveobj.intrwDateVO.month,
                "day" : saveobj.intrwDateVO.day,
                "year" : saveobj.intrwDateVO.year
            };
        }
        if (saveobj.intrwTimeVo) {
            savePhnScrnDetails.UpdatePhoneScrnDtlsRequest.PhoneScreenIntrwDetail.InterviewLocDtls.interviewTime = {
                "month" : saveobj.intrwTimeVo.month,
                "day" : saveobj.intrwTimeVo.day,
                "year" : saveobj.intrwTimeVo.year,
                "hour" : saveobj.intrwTimeVo.hour,
                "second" : saveobj.intrwTimeVo.second,
                "minute" : saveobj.intrwTimeVo.minute,
                "milliSecond" : saveobj.intrwTimeVo.milliSecond
            };
        }
        if (saveobj.PhnScrnDateVo) {
            savePhnScrnDetails.UpdatePhoneScrnDtlsRequest.PhoneScreenIntrwDetail.phnScrnDate = {
                "month" : saveobj.PhnScrnDateVo.month,
                "day" : saveobj.PhnScrnDateVo.day,
                "year" : saveobj.PhnScrnDateVo.year
            };
        }
        if (saveobj.phnScrnTimeVo) {
            savePhnScrnDetails.UpdatePhoneScrnDtlsRequest.PhoneScreenIntrwDetail.phnScrnTime = {
                "month" : saveobj.phnScrnTimeVo.month,
                "day" : saveobj.phnScrnTimeVo.day,
                "year" : saveobj.phnScrnTimeVo.year,
                "hour" : saveobj.phnScrnTimeVo.hour,
                "second" : saveobj.phnScrnTimeVo.second,
                "minute" : saveobj.phnScrnTimeVo.minute,
                "milliSecond" : saveobj.phnScrnTimeVo.milliSecond
            };
        }

        return savePhnScrnDetails;
    };
    /*
     * Update phone screen details
     */
    this.updatePhnScrnDtls = function(savePhnScrnDetails) {
        var data = {
                data : JSON.stringify({
                    "UpdatePhoneScrnDtlsRequest" : savePhnScrnDetails.UpdatePhoneScrnDtlsRequest
                })
            };
        var callbackFunction = $.Callbacks('once');
        CONSTANTS.phnscrnSaveCheck = false;
        callbackFunction.add(CONSTANTS.phonescreenDetailsObj.submitPhnDetResponse);
        RSASERVICES.updatePhnScreenDetails(data, callbackFunction, true, "Please wait...");
    };
    /*
     * Save phone screen details
     */
    this.savePhnScrnDtlsSuccess = function(json) {
        var overAllStatus = json.Response.PhoneScreenIntrwDetail.overAllStatus;
        // setting the overall status
        for ( var i = 0; i < CONSTANTS.getOverallStatusDtls.length; i++) {
            if (CONSTANTS.getOverallStatusDtls[i].status === overAllStatus) {
                // commented - bcoz in flex not able to find a component named
                // overAllStatus
                // Application.application.phoneScrnDtlsPg.overAllStatus.selectedIndex=i
                break;
            }
        }
    };
/*
 * Submit phone screen details
 */
    this.submitPhnDetResponse = function(json) {
        $.unblockUI();
        if (CONSTANTS.submitPhnScrnBtnClickFlg) {
            CONSTANTS.SUCCESS_ON_HOME = true;
            UTILITY.RemoveQS();
            $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.phonescreenDetailsObj.setContent);
            $('#currentGridTitle').text("");
        }else if (json.Response.hasOwnProperty("status") && json.Response.status === CONSTANTS.SUCCESS) {
            // check for the ITIDetailList tag in the response
            if (json.Response.hasOwnProperty("ITIDetailList") && json.Response.hasOwnProperty("PhoneScreenIntrwDetail")) {
                CONSTANTS.phonescreenDetailsObj.savePhnScrnDtlsSuccess(json);
            }
            CONSTANTS.phonescreenDetailsObj.popup.alert("Details Saved Successfully");

        }
        if (json.Response.hasOwnProperty("error") && json.Response.error.hasOwnProperty("errorMsg")) {
            CONSTANTS.phonescreenDetailsObj.popup.alert(json.Response.error.errorMsg);
        }
    };
    /*
     * blocks UI elements
     */
    this.blockFormElements = function(id) {
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

    /*
     * make pop ups draggable and static
     */
    this.promptStaticDraggable = function(id) {
        if ($(".modal-backdrop.fade.in").length > 1) {
            $(".modal-backdrop.fade.in:eq(1)").css({"z-index":"1600"});
        }
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
        $(id).modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $(id).draggable({
            handle : ".modal-header"
        });
    };

    $("input[data-pattern]").assignKeyDownToInputs();

    
    this.createCallHistoryGrid = function(data) {
    	if (!data) {
            data = [];
        }
    	
        var options = {
            enableCellNavigation : false,
            headerRowHeight : 25,
            rowHeight : 30,
            enableColumnReorder: false,
            forceFitColumns : false
        };
        var columns = [ {
            id : "callType",
            name : "Call Type",
            field : "callType",
            width : 200,
            sortable : false
        }, {
            id : "callTs",
            name : "Call Date/Time",
            field : "callTs",
            width : 200,
            sortable : false
        },{
            id : "callDisposition",
            name : "Call Disposition",
            field : "callDisposition",
            width : 300,
            sortable : false
        }];
        
        if(this.callHistoryGrid){
            this.callHistoryGrid.setData(data);
            this.callHistoryGrid.invalidate();
        }else{
        	this.callHistoryGrid = new Slick.Grid("#callHistory-grid", data, columns, options);
        }
    };    
};
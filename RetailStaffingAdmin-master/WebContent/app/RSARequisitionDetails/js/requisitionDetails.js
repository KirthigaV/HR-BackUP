/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: requisitionDetails.js
 * Application: Retail Staffing Admin
 *
 */
var reqDet = new requisitionDetails();
function requisitionDetails() {
    this.model = {};
    this.model.editStaffingDet = false;
    this.model.candidateCt = 0;
    this.qpCurrentASpage =0;
    this.qpCurrentAPpage =0;
    this.regExprLt = /</g;
    this.apIdToReqArray = [];
    this.asIdToReqArray = [];
    this.applToReqArray = [];
    this.rsaServices = RSASERVICES;
    this.popup = new rsaPopup();
    this.model.languageArray = [ "", "ENGLISH", "SPANISH", "FRENCH", "GERMAN", "ITALIAN", "CHINESE", "TAGALOG", "POLISH", "KOREAN", "VIETNAMESE", "PORTUGUESE", "JAPANESE", "GREEK", "ARABIC", "HINDI",
            "SIGN" ];
    this.makeServiceCall = function() {
      	if($('.modal-backdrop').length >= 2){
    		$('.modal-backdrop').empty().remove();
    	}
        $("#reqdetnav .reqmessageBar").text("REQUISITION DETAILS");
        $("#goHome.reqnavHeaderButtons").show();
        var requisitionNum = CONSTANTS.retailStaffingObj.reqNumber;
        this.initialBlockApplication();
        
        this.unbindEvents();
        this.bindEvents();
        

        // if the requisition details are not cached, call the service to fetch details.
        if (!CONSTANTS.cacheRequisitionDetailsFlg)
    	{
        	this.rsaServices.getRequisitionDetails({data:requisitionNum}, this.displayValues.bind(this), this.getRequisitionDetailsError.bind(this));
        	if((this.candidatesGrid || this.phoneScreenGrid) && CONSTANTS.calledFromSearchPage)
    		{
        		this.candidatesGrid = undefined;
        		this.phoneScreenGrid = undefined;
        		CONSTANTS.calledFromSearchPage = false;
    		}
        	this.displayCandidatesGrid([]);
            this.displayPhoneScreenGrid([]);
            $(".reqDetContainer select").selectBoxIt();
    	}
        else
    	{
        	
        	this.displayCandidatesGrid([]);
            this.displayPhoneScreenGrid([]);
            $(".reqDetContainer select").selectBoxIt();
            
        	if(!CONSTANTS.cache_getRequisitionDetails || CONSTANTS.cache_getRequisitionDetails !== {}) {
        		this.displayValues(CONSTANTS.cache_getRequisitionDetails);
        	}
        	else {
        		this.rsaServices.getRequisitionDetails({data:requisitionNum}, this.displayValues.bind(this), this.getRequisitionDetailsError.bind(this));
        	}
    	}
        
        
    };
    
    this.printStats = function(stepname) {
    	var d = new Date();    	
    	console.log(stepname + " " + d.getMinutes().toString() + ":" +  d.getSeconds().toString() + ":" + d.getMilliseconds().toString() );
    };
    /*
     * Initialises model variables with empty values
     *
     * @param N/A @return N/A
     */
    this.initializeModelVariables = function() {
        this.model.reqStrDet = [];
        this.model.LoadREQDGDltls = [];
        this.model.schdPrefArr = [];
        this.model.reqnLanguagesArr = [];
        this.model.reqStaffDet = [];
        this.model.hiringEventDetails = {};
        this.model.weekAvl = {};
        this.model.hrEvntStrt = {};
        this.model.hrEvntEnd = {};
        this.model.hrEvntStrtTm = [];
        this.model.hrEvntEndTm = [];
        this.model.LunchTm = [];
        this.model.lastIntrTm = [];
        this.model.reqStateDet = [];
        this.model.reqExpDet = [];
        this.model.languageSkls = [];
        this.model.interviewScheduleCollection = [];
        this.model.reqStatusDet = [];
        this.model.createphnScrDtls = [];
        this.model.CndtDltls = [];
        this.model.interviewsHaveBeenScheduled = false;
    };
    /*
     * This is the success handler of getRequisitionDetails function This sets
     * values for all the controls in the page
     *
     * @param response @return N/A
     */
    this.displayValues = function(response) {    	
    	//cache the response.
    	CONSTANTS.cache_getRequisitionDetails = response;    	
        this.unblockApplication();
        var res = response.Response;
        this.initializeModelVariables();
        if ("status" in res && res.status === CONSTANTS.SUCCESS) {
            this.model = _.extend(this.model, new getReqScrDtlsCmd(response));
        } else if ("error" in res) {
            this.popup.alert(res.error.errorMsg);
            return;
        }
       
        this.displayRequisitionValues(this.model.LoadREQDGDltls[0]);
        this.displayStoreDetails(this.model.reqStrDet[0]);
        if (!CONSTANTS.cacheRequisitionDetailsFlg)
    	{
        	this.displayStaffingDetails(this.model.reqStaffDet[0], this.model.reqStatusDet, this.model.LoadREQDGDltls[0], this.model.interviewScheduleCollection, this.model.reqExpDet, this.model.languageSkls, this.model.reqStateDet);
    	}
        else{
        	//Insert DOM
        	if (CONSTANTS.retailStaffingObj.DOMCache) {
        		$(".staffDet").empty();
        		$(".staffDet").html(CONSTANTS.retailStaffingObj.DOMCache);
        	}        	
        }
     	if (!CONSTANTS.cacheRequisitionDetailsFlg)
    	{
            this.displayCandidatesGrid(this.model.CndtDltls);
            this.displayPhoneScreenGrid(this.model.createphnScrDtls);
    	}
        this.showHomeBtn();
        if (!CONSTANTS.cacheRequisitionDetailsFlg)
    	{
        	this.setInterviewDurCombo();
    	}
        this.initQPViewStack();
    };
    /*
     * Failure callback for getRequisitionDetails Service.
     *
     * @param N/A @return N/A
     */
    this.getRequisitionDetailsError = function() {
        // Unblock full page handled in rsaServices
    };
    /*
     * This function unbinds events to all interactable controls in the page
     *
     * @param N/A @return N/A
     */
    this.unbindEvents = function(){
        $("#goHome.reqnavHeaderButtons").unbind();
        $(".reqnavHeaderButtons.navCloseBtn").unbind();
        $(".reqDetContainer input[type=text]").unbind();
        $(".reqDetContainer select").unbind();
        $(".reqDetContainer input[type=radio]").unbind();
        $(".filterPool #filterAppl").unbind();
        $(".reqNbrLink .reqNbr").unbind();
        $(".reqDetContainer .action-btn-row #saveReqDtlsBtn").unbind();
        $(".reqDetContainer .action-btn-row #submitPhnScrnDtlsBtn").unbind();
        $(".reqDetContainer .action-btn-row #cancelBtn").unbind();
        $(".reqDetContainer .action-btn-row #CrtPhnScr").unbind();
        $(".reqDetContainer .action-btn-row #qualifiedPoolBtn").unbind();
        $(".reqDetContainer .action-btn-row #saveQPButton").unbind();
        $(".reqDetContainer .action-btn-row #submitQPButton").unbind();
        $(".reqDetContainer .action-btn-row #cancelQPButton").unbind();
    };
    /*
     * This function binds events to all interactable controls in the page
     *
     * @param N/A @return N/A
     */
    this.bindEvents = function() {
        $("#goHome.reqnavHeaderButtons").click(this.homeBtnClick.bind(this));
        $(".reqnavHeaderButtons.navCloseBtn").click(this.onCloseBtnClick.bind(this));
        $(".reqDetContainer input[type=text]").change(this.staffingDetEdit.bind(this));
        $(".reqDetContainer select").click(this.staffingDetEdit.bind(this));
        $(".reqDetContainer input[type=radio]").click(this.staffingDetEdit.bind(this));
        $(".filterPool #filterAppl").click(this.filterQualifiedPool.bind(this));
        $(".reqNbrLink .reqNbr").click(this.loadRequisitionDetail.bind(this));
        $(".reqDetContainer .action-btn-row #saveReqDtlsBtn").click(this.submitReqScrnDtls.bind(this, CONSTANTS.evntTypReqSave));
        $(".reqDetContainer .action-btn-row #submitPhnScrnDtlsBtn").click(this.submitReqScrnDtls.bind(this, CONSTANTS.evntTypReq));
        $(".reqDetContainer .action-btn-row #cancelBtn").click(this.cancelReq.bind(this));
        $(".reqDetContainer .action-btn-row #CrtPhnScr").click(this.createPhoneScreen.bind(this));
        $(".reqDetContainer .action-btn-row #qualifiedPoolBtn").click(this.viewQualifiedPool.bind(this));
        $(".reqDetContainer .action-btn-row #saveQPButton").click(this.processSave.bind(this));
        $(".reqDetContainer .action-btn-row #submitQPButton").click(this.processSubmit.bind(this));
        $(".reqDetContainer .action-btn-row #cancelQPButton").click(this.cancelAttachment.bind(this));
        $("input[data-restrict]").restrictInputFeature();
    };
    
    // Reload requisitionDetails again
    this.loadRequisitionDetail = function() {
    	UTILITY.clearCache();
    	CONSTANTS.calledFromSearchPage = true;
    	
    	this.applToReqArray = [];
        $(".topform").unblock();
        $(".reqNbrText").show();
        $(".reqNbrLink").hide();
        $(".reqQua").show();
        $(".qualPool").hide();
        this.resetApplicantGroupDisplay();
        
    	this.makeServiceCall();
    	
    	this.displayCandidatesGrid([]);   	
    	this.displayPhoneScreenGrid([]);
    	
    	this.displayCandidatesGrid(this.model.CndtDltls);
    	this.displayPhoneScreenGrid(this.model.createphnScrDtls);
    	//Scroll back to top.
    	$('.reqDetMainContainer').animate({ scrollTop: 0 });
        
    };
    
    this.homeBtnClick = function(){
        this.cancelReq();
    };
    /*
     * This function sets editStaffingDet variable if any edit event occurs in
     * the page This variable is used to show prompt the user for any unsaved
     * data before proceeding to any other page
     *
     * @param N/A @return N/A
     */
    this.staffingDetEdit = function() {
        this.model.editStaffingDet = true;
    };
    /*
     * This function shows/hides the Home button. And clears the form if there
     * is no data
     *
     * @param N/A @return N/A
     */
    this.showHomeBtn = function() {
        this.model.editStaffingDet = false;

        this.showHideQPButton();
        this.checkPhoneScreenPerCand();
        $("#goHome.reqnavHeaderButtons").show();
        $("#goPrevious.reqnavHeaderButtons").hide();
        if (!_.isNull(this.model.reqStaffDet) && this.model.reqStaffDet.length > 0) {
            this.model.hrEvntId = this.model.reqStaffDet[0].hireEvntid;
            this.setUpdIns();
            // Desired Experience Setting - Handled in screen flow
            // Selection of Requisition status - Handled in screen flow
            // Set Calendar Drop Down enabled property based on has any
            // Interviews been scheduled - Handled in screen flow
            this.blockUnblockDurEvtCal(".staffDet .duration");
            this.blockUnblockDurEvtCal(".staffDet .calendar");
            this.blockUnblockDurEvtCal(".staffDet .hrgEvnt");
            // To select AM/PM for the hire event start time - Handled in screen
            // flow
            // To select for the hire event end time - Handled in screen flow
            // To select AM/PM for lunch time - Handled in screen flow
            // To select AM/PM for last interview time - Handled in screen flow
            // To select state - Handled in screen flow
            // To display the phone number in specified format (xxx-xxx-xxxx) -
            // Handled in screen flow
            // To display the hire event phone in specified format
            // (xxx-xxx-xxxx) - Handled in screen flow
            if (this.model.reqStaffDet[0].hrgEvntFlg) {
                if (this.model.reqStaffDet[0].hrgEvntFlg === "N") {
                    $(".staffDet .hrgEvntY").hide();
                    $(".staffDet .hrgEvntFlg").val([ this.model.reqStaffDet[0].hrgEvntFlg ]);
                    $(".staffDet .interviewDurtnRow").show();
                    $(".staffDet .calendarLabel").text("Attached to Calendar:");
                } else if (this.model.reqStaffDet[0].hrgEvntFlg === "Y") {
                    this.hiringEventY();
                }
                this.setTgtPay();
            } else {
                $(".staffDet .hrgEvntY").hide();
                $(".staffDet .hrgEvntFlg").val([ "N" ]);
                $(".staffDet .calendarLabel").text("Attached to Calendar:");
            }
        } else {
            // Clear the form
            $(".staffDet .hrgEvntY").hide();
            $(".staffDet .hrgEvntFlg").val([ "N" ]);

            $(".staffDet .desiredExp").val("0");
            $(".staffDet .desiredExp").data("selectBox-selectBoxIt").refresh();
            $(".staffDet .hrgEvntState").val("-1");
            $(".staffDet .hrgEvntState").data("selectBox-selectBoxIt").refresh();
            $(".staffDet .hrgEvntStTime .tmFrmt").val("0");
            $(".staffDet .hrgEvntEndTime .tmFrmt").val("0");
            $(".staffDet .hrgEvntLunch .tmFrmt").val("0");
            $(".staffDet .lastIntrvTime .tmFrmt").val("0");
            $(".staffDet .tmFrmt").data("selectBox-selectBoxIt").refresh();

            $(".reqDetContainer .reqQua").show();
            $(".reqDetContainer .qualPool").hide();

            $(".reqDetContainer .action-btn-row .btn").show();
            $(".reqDetContainer .topform").unblock();
            $(".reqDetForm .reqNbrText").show();
            $(".reqDetForm .reqNbrLink").hide();

            if ($(".staffDet .hrgMgrPhnTxt")) {
                $(".staffDet .hrgMgrPhnTxt").val("");
            }
            if ($(".staffDet .hrgEvntPhn")) {
                $(".staffDet .hrgEvntPhn").val("");
            }
        }

    };
    /*
     * This function shows/hides the Back to Qualified pool button.
     *
     * @param N/A @return N/A
     */
    this.showHideQPButton = function() {
        if (CONSTANTS.isQPButtonVisible) {
            $("#qualifiedPoolBtn").css("visibility", "visible");
        } else {
            $("#qualifiedPoolBtn").css("visibility", "hidden");
        }
    };
    /*
     * This function sets UpdIns flag to decide whether to update or insert
     *
     * @param N/A @return N/A
     */
    this.setUpdIns = function() {
        if (_.isNull(this.model.reqStaffDet[0].hireEvntid)) {
            this.model.UpdIns = CONSTANTS.INSERT;
        } else {
            this.model.UpdIns = CONSTANTS.UPDATE;
        }
    };
    /*
     * This function disabled the calendar dropdown and its related controls
     *
     * @param N/A @return N/A
     */
    this.blockUnblockDurEvtCal = function(className) {
        if (this.model.interviewsHaveBeenScheduled) {
            this.blockFormElements(className);
        } else {
            $(className).unblock();
        }
    };
    /*
     * This function formats the target pay value.
     *
     * @param N/A @return N/A
     */
    this.setTgtPay = function() {
        if (!this.model.reqStaffDet[0].targetPay) {
            this.model.reqStaffDet[0].targetPay = null;
        } else {
            this.model.reqStaffDet[0].targetPay = Number(this.model.reqStaffDet[0].targetPay).toFixed(2);
        }
    };
    /*
     * This function shows parts of form when hiring event is Y
     *
     * @param N/A @return N/A
     */
    this.hiringEventY = function() {
        $(".staffDet .hrgEvntFlg").val([ this.model.reqStaffDet[0].hrgEvntFlg ]);
        if (this.model.reqStaffDet[0].hireEvntType !== "SSE" && this.model.reqStaffDet[0].hireEvntType !== "MSE" && this.model.reqStaffDet[0].hireEvntType !== "OSE") {

            $(".staffDet .calendarLabel").text("Attached to Calendar:");
            $(".hrgEvntY.oldHrngDetForm").show();
            $(".hrgEvntY.newHrngDetForm").hide();
            $(".staffDet .interviewDurtnRow").hide();

        } else {
            $(".staffDet .calendarLabel").text("Attached to Hiring Event:");
            $(".hrgEvntY.oldHrngDetForm").hide();
            $(".hrgEvntY.newHrngDetForm").show();
            $(".staffDet .interviewDurtnRow").show();
        }
    };
    /*
     * This function checks where phone screen is created for all candidates and
     * sets the flag
     *
     * @param N/A @return N/A
     */
    this.checkPhoneScreenPerCand = function() {
        var flag = false;
        if (!_.isNull(this.model.CndtDltls) && this.model.CndtDltls.length > 0) {
            // Has Candidates, Check for Phone Screens
            if (!_.isNull(this.model.createphnScrDtls) && this.model.createphnScrDtls.length > 0) {
                // Has Phone Screens
                flag = this.cmpPhnScrDetCandScrDet(flag);
            } else {
                // No Phone Screens found, but has Candidates, set to True
                flag = true;
            }
        }
        this.model.isCandPhnScrnMissing = flag;
    };
    /*
     * This function compares phone screen details with candidate details
     *
     * @param flag @return flag
     */
    this.cmpPhnScrDetCandScrDet = function(flag) {
        // compare list of phone screen details with candidate screen details
        for (var i = 0; i < this.model.CndtDltls.length; i++) {
            flag = true;
            for (var j = 0; j < this.model.createphnScrDtls.length; j++) {
                if (this.model.CndtDltls[i].ssnNbr === this.model.createphnScrDtls[j].cndtNbr) {
                    flag = false;
                }
            }

            if (!flag) {
                this.model.isCandPhnScrnMissing = false;
            }
        }

        return flag;
    };
    /*
     * This function sets, enables/disables the interview duration combo box
     *
     * @param N/A @return N/A
     */
    this.setInterviewDurCombo = function() {
        if ($(".staffDet .rscToManageFlgBtn:checked").val() === "Y") {
            if (!this.model.interviewsHaveBeenScheduled) {
                $(".staffDet .rscSchdFlg").unblock();
            } else {
                this.blockFormElements(".staffDet .rscSchdFlg");
            }
        } else {
            this.blockFormElements(".staffDet .rscSchdFlg");
        }
        if ($(".staffDet .rscSchdFlgBtn:checked").val() === "Y") {
            if (this.model.LoadREQDGDltls[0].interviewDurtn.toString() === "30" || this.model.LoadREQDGDltls[0].interviewDurtn.toString() === "60") {
                $(".staffDet .interviewDurtn").val(this.model.LoadREQDGDltls[0].interviewDurtn);
            } else {
                $(".staffDet .interviewDurtn").val("0");
            }
            $(".staffDet .interviewDurtn").data("selectBox-selectBoxIt").refresh();
            if ($(".staffDet .rscSchdFlg .blockUI").length === 0) {
                $(".staffDet .interviewDurtnRow .duration").unblock();
                $(".staffDet .calendar").unblock();
                $(".staffDet .hrgEvnt").unblock();
            }
        } else {
            $(".staffDet .interviewDurtn").val("0");
            $(".staffDet .interviewDurtn").data("selectBox-selectBoxIt").refresh();
            this.blockFormElements(".staffDet .interviewDurtnRow .duration");
            this.blockFormElements(".staffDet .calendar");
            this.blockFormElements(".staffDet .hrgEvnt");
        }
    };
    /*
     * This function display qualified pool when navigating back to this page
     * from any other page
     *
     * @param N/A @return N/A
     */
    this.initQPViewStack = function() {
        $(".goPrevious.reqnavHeaderButtons").hide();
        this.model.sortedLanguageArray = this.model.languageArray.slice().sort();
        if (CONSTANTS.backToQualifiedPool === "BackToQualifiedPool") {
            $(".reqDetContainer .reqQua").hide();
            $(".reqDetContainer .qualPool").show();
            this.blockFormElements(".reqDetContainer .topform");
            $(".reqDetForm .reqNbrText").hide();
            $(".reqDetForm .reqNbrLink").show();
            $(".reqDetContainer .action-btn-row.reqQua").hide();
            $("#messageBar").text(CONSTANTS.QUALIFIED_POOL_TTL);
            CONSTANTS.backToQualifiedPool = "";
            // Added for making service call to get updated data
            this.viewQualifiedPool();
        } else {
            $(".reqDetContainer .reqQua").show();
            $(".reqDetContainer .qualPool").hide();
            $(".reqDetContainer .topform").unblock();
            $(".reqDetForm .reqNbrText").show();
            $(".reqDetForm .reqNbrLink").hide();
            $(".reqDetContainer .action-btn-row.qualPool").hide();
            // This is added to hide the action button row in qualified pool
        }
        if (this.model.LoadREQDGDltls[0].active === "Y") {
            $(".action-btn-row #qualifiedPoolBtn").removeAttr("disabled");
        } else {
            $(".action-btn-row #qualifiedPoolBtn").attr("disabled","disabled");
        }
    };
    /*
     * This function resets all variables used for displaying qualified pool
     * screen before showing it
     *
     * @param N/A @return N/A
     */
    this.viewQualifiedPool = function() {
        if (this.model.editStaffingDet) {
            this.popup
                    .warnUnsavedData(CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_1, CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_2, this.handleViewQualifiedPool.bind(this), this.handleClose.bind(this), 150, 400);
        } else {
            this.model._currentApplicantGroupStartValue = 0;
            this.model._currentApplicantGroupEndValue = 0;
            this.model._currentApplicantGroupTotalValue = 0;
            this.model._currentAssociateGroupStartValue = 0;
            this.model._currentAssociateGroupEndValue = 0;
            this.model._currentAssociateGroupTotalValue = 0;
            this.model._tieringApplicant = "";
            this.model._tieringAssociate = "";
            this.goTOQualifiedPool();
        }
    };
    /*
     * This function filters the applicants and associates in the 
ified pool grids
     * with respect to language
     *
     * @param N/A @return N/A
     */
    this.filterQualifiedPool = function(){
        var request = JSON.stringify(this.getretailStaffingViewAssoApplPoolReq(this.model.LoadREQDGDltls[0].reqNbr, $(".filterPool .langSel").val()));
        var that = this;
        $.when(this.rsaServices.getRetailStaffingViewApplicantPool({data:request}, this.retailStaffingViewApplicantPoolSuccess.bind(this), this.retailStaffingViewApplicantPoolFailure.bind(this)), this.rsaServices.getRetailStaffingViewAssociatePool({data:request}, this.retailStaffingViewAssociatePoolSuccess.bind(this), this.retailStaffingViewAssociatePoolFailure.bind(this))).done(function(){        	
        	that.unblockFullPage();
        }); 
       // this.rsaServices.getRetailStaffingViewApplicantPool({data:request}, this.retailStaffingViewApplicantPoolSuccess.bind(this), this.retailStaffingViewApplicantPoolFailure.bind(this));
        //this.rsaServices.getRetailStaffingViewAssociatePool({data:request}, this.retailStaffingViewAssociatePoolSuccess.bind(this), this.retailStaffingViewAssociatePoolFailure.bind(this));
    };
    /*
     * This function hides any popup if visible before showing qualified pool
     * screen
     *
     * @param N/A @return N/A
     */
    this.handleViewQualifiedPool = function() {
        if ($('#genericPopup').hasClass('in')) {
            $("#genericPopup").modal("hide");
        }
        this.goTOQualifiedPool();
    };
    /*
     * This function sets title and makes service calls and block the top part
     * of the screen
     *
     * @param N/A @return N/A
     */
    this.goTOQualifiedPool = function() {
        $("#reqdetnav .reqmessageBar").text("QUALIFIED POOL");
        $("#qpTabs").tabs();
        this.showNoAssocApplMsg = !CONSTANTS.APPL_PROF_TO_REQ_DET;
        if(CONSTANTS.APPL_PROF_TO_REQ_DET){
            CONSTANTS.APPL_PROF_TO_REQ_DET = false;
            $("#qpTabs").tabs("option", "active", CONSTANTS.QP_SELECTED_TAB);
        }
        CONSTANTS.QP_SELECTED_TAB = 0;
        this.model.LoadAssociatePoolDgDtls = [];
        var reqNum = this.model.LoadREQDGDltls[0].reqNbr;

        this.model.LoadPanelTitle = CONSTANTS.VIEW_APPL_POOL_TTL;
        $(".filterPool .langSel").empty();
        for (var i = 0; i < this.model.sortedLanguageArray.length; i++) {
            $(".filterPool .langSel").append("<option value=" + this.model.languageArray.indexOf(this.model.sortedLanguageArray[i]) + ">" + this.model.sortedLanguageArray[i] + "</option>");
        }
        $(".filterPool .langSel").data("selectBox-selectBoxIt").refresh();
        this.rsaServices.getCandidateCount({data:reqNum}, this.getCandidateCountSuccess.bind(this), this.getCandidateCountFail.bind(this));
        if (!CONSTANTS.cacheRequisitionDetailsFlg)
    	{
	        var request = JSON.stringify(this.getretailStaffingViewAssoApplPoolReq(reqNum, $(".filterPool .langSel").val()));
	        var that = this;
	        $.when(this.rsaServices.getRetailStaffingViewApplicantPool({data:request}, this.retailStaffingViewApplicantPoolSuccess.bind(this), this.retailStaffingViewApplicantPoolFailure.bind(this)), this.rsaServices.getRetailStaffingViewAssociatePool({data:request}, this.retailStaffingViewAssociatePoolSuccess.bind(this), this.retailStaffingViewAssociatePoolFailure.bind(this))).done(function(){
	        	that.unblockFullPage();
	        }); 
	        //this.rsaServices.getRetailStaffingViewApplicantPool({data:request}, this.retailStaffingViewApplicantPoolSuccess.bind(this), this.retailStaffingViewApplicantPoolFailure.bind(this));
	        //this.rsaServices.getRetailStaffingViewAssociatePool({data:request}, this.retailStaffingViewAssociatePoolSuccess.bind(this), this.retailStaffingViewAssociatePoolFailure.bind(this));
	        if ($(".reqQua:visible").length > 0) {
	            this.blockFormElements(".topform");
	            CONSTANTS.retailStaffingObj.DOMCache = $(".staffDet").children();
	            $(".reqNbrText").hide();
	            $(".reqNbrLink").show();
	            $(".reqQua").hide();
	            $(".qualPool").show();
	        }
	        this.model.candidateCt = 0;

	        // clear language filter
	        $(".filterPool .langSel").val(0);
	        $(".filterPool .langSel").data("selectBox-selectBoxIt").refresh();
	        this.generateGrids();
    	}
        else
    	{
        	 if ($(".reqQua:visible").length > 0) {
                 this.blockFormElements(".topform");
                 CONSTANTS.retailStaffingObj.DOMCache = $(".staffDet").children();
                 $(".reqNbrText").hide();
                 $(".reqNbrLink").show();
                 $(".reqQua").hide();
                 $(".qualPool").show();
             }
             this.model.candidateCt = 0;

             // clear language filter
             $(".filterPool .langSel").val(0);
             $(".filterPool .langSel").data("selectBox-selectBoxIt").refresh();
             this.generateGrids();
        	
    		this.retailStaffingViewApplicantPoolSuccess(CONSTANTS.cache_getRetailStaffingViewApplicantPool);
    		this.retailStaffingViewAssociatePoolSuccess(CONSTANTS.cache_getRetailStaffingViewAssociatePool);
    		// navigate to appropriate page.
    		this.qpCurrentAPpage = CONSTANTS.cacheCurrentAPpage;
    		this.qpCurrentASpage = CONSTANTS.cacheCurrentASpage;
    		this.moveToApplntCurrentPage();
    		this.moveToAssociateCurrentPage();
    		
            try {
            	// scroll to the QP area
        		$('.reqDetMainContainer').animate({ scrollTop: $(".qualPool").offset().top });
        		// and then scroll the grid.
            	// Check from which grid the applicant number is sent and call the particular grid appropriate. 30 - is the default height of row
            	if(CONSTANTS.qpFromApplicants) {
            		$('#qpapplGrid .slick-viewport').animate({ scrollTop: parseInt(CONSTANTS.currentQProw)*30 });
            	} else {
            		$('#qpassocGrid .slick-viewport').animate({ scrollTop: parseInt(CONSTANTS.currentQProw)*30 });
            	}
            }
            catch(ex) {
            	// Exception catched.
            	console.log("unable to move. Error: "+ ex.toString());
            }
    		
    	}

    };
    /*
     * This function generates request for making service call
     *
     * @param requisitionNum language @return request
     */
    this.getretailStaffingViewAssoApplPoolReq = function(requisitionNum, language) {
        var request = {
            "qualifiedPoolRequest" : {}
        };
        if (!language || parseInt(language) === 0) {
            request.qualifiedPoolRequest.requisition = requisitionNum;
        } else {
            request.qualifiedPoolRequest.requisition = requisitionNum;
            request.qualifiedPoolRequest.jobSkillFilter = language;
        }
        return request;
    };
    /*
     * This function is success handler for retailstaffingviewassociatepool
     * service this function shows popup if any error occurs
     *
     * @param response @return N/A
     */
    this.retailStaffingViewAssociatePoolSuccess = function(response) {
    	CONSTANTS.cache_getRetailStaffingViewAssociatePool = response;
        response = response.Response;
        this.model = _.extend(this.model, new rsaViewAssocPool(response, this.model.LoadREQDGDltls[0].reqNbr));
        $(".qpassocTier .qpassocTierValue").text(this.model._tieringAssociate);
        this.createAssocGrid(this.model.LoadAssociatePoolDgDtls);
        if ("errorDetails" in response) {
            this.popup.alert("An Error has Occurred retrieving Associates for the Qualified Pool.");
        } else if (this.model.LoadAssociatePoolDgDtls.length === 0 && this.showNoAssocApplMsg) {
            this.popup.noassocAlert("No Associates Found for the Qualified Pool.");
        }
    };

    this.retailStaffingViewAssociatePoolFailure = function() {
        // Unblock full page handled in rsaServices
    };
    /*
     * This function is success handler for retailStaffingViewApplicantPool
     * service this function shows popup if any error occurs
     *
     * @param response @return N/A
     */
    this.retailStaffingViewApplicantPoolSuccess = function(response) {
    	// cache the response for the future use.
    	CONSTANTS.cache_getRetailStaffingViewApplicantPool = response;
    	
        response = response.Response;
        this.model = _.extend(this.model, new rsaViewApplPool(response, this.model.LoadREQDGDltls[0].reqNbr));
        this.createApplGrid(this.model.LoadApplicantPoolDgDtls);
        $(".qpapplTier .qpapplTierValue").text(this.model._tieringApplicant);
        if ("errorDetails" in response) {
            this.popup.alert("An Error has Occurred retrieving Applicants for the Qualified Pool.");
        } else if (this.model.LoadApplicantPoolDgDtls.length === 0 && this.showNoAssocApplMsg) {
            this.popup.alert("No Applicants Found for the Qualified Pool.");
        }
    };
    this.retailStaffingViewApplicantPoolFailure = function() {
        // Unblock full page handled in rsaServices
    };
    /*
     * This function is success handler for getCandidateCount service
     *
     * @param response @return N/A
     */
    this.getCandidateCountSuccess = function(response) {
    	if (!CONSTANTS.cacheRequisitionDetailsFlg){
    		this.rsaServices.blockFullPage();
    	}
        response = response.Response;
        this.model = _.extend(this.model, new getCandidateCount(response));
        // load the candidate count from the response.
        if(response && response.CandidateCountList && response.CandidateCountList.CandidateCountDetail && response.CandidateCountList.CandidateCountDetail.candidateCount) {
        	$(".candCountField .candCount").text(response.CandidateCountList.CandidateCountDetail.candidateCount);
        }
        else {
        	$(".candCountField .candCount").text(this.model.CndtDltls.length);
        }
        
        if ("error" in response) {
            this.getCandidateCountFail(response);
        }
    };
    /*
     * This function shows popup of any error message from service
     *
     * @param N/A @return N/A
     */
    this.getCandidateCountFail = function(response) {
        this.popup.alert(response.error.errorMsg);
    };
    /*
     * This function generates both applicant and associate grids as empty
     *
     * @param N/A @return N/A
     */
    this.generateGrids = function() {
        this.createApplGrid();
        this.createAssocGrid();
    };
    /*
     * This function generates applicant grid with data if available
     *
     * @param data @return N/A
     */
    this.createApplGrid = function(data) {
        if (!data) {
            data = [];
        } else {
            $(".currentApplicantGroupStartValue").text(this.model._currentApplicantGroupStartValue);
            $(".currentApplicantGroupEndValue").text(this.model._currentApplicantGroupEndValue);
            $(".currentApplicantGroupTotalValue").text(this.model._currentApplicantGroupTotalValue);
        }
        var options = {
            enableCellNavigation : false,
            headerRowHeight : 25,
            rowHeight : 30,
            enableColumnReorder: false,
            forceFitColumns : true
        };
        var columns = [ {
            id : "name",
            name : "Applicant Name",
            field : "name",
            width : 220,
            sortable : false,
            formatter : function(row, cell, value) {
                return "<div class='linkcell applNameLink'><a data-value='" + value + "' class=''>" + value + "</a></div>";
            }
        }, {
            id : "consideredFlg",
            name : "Considered",
            field : "consideredFlg",
            width : 75,
            sortable : false
        }, {
            id : "availInd",
            name : "FT/PT",
            field : "availInd",
            width : 60,
            sortable : false
        }, {
            id : "availability",
            name : "Availability",
            field : "availability",
            width : 540,
            sortable : false
        }, {
            id : "updTs",
            name : "Last Applied",
            field : "updTs",
            width : 290,
            sortable : false,
            formatter: function(row, cell, value){
                return "<div class='linkcell'>"+$.datepicker.formatDate('mm/dd/yy', new Date(value.replace(/-/, '/')))+"</div>";
            }
        } ];
        var selectedTab = $("#qpTabs").tabs('option', 'active');
        $("#qpTabs").tabs("option", "active", 0);
        this.applGrid = new Slick.Grid("#qpapplGrid", data, columns, options);
        $("#qpapplGrid").off("click", ".applNameLink");
        $("#qpapplGrid").on("click", ".applNameLink", function(e) {
            // Navigate to Applicant profile
            CONSTANTS.retailStaffingObj.applicantObj = this.loadApplicantDetails(e);
            this.markSingleApplicantAsConsidered();
            CONSTANTS.currentQProw = this.applGrid.getCellFromEvent(e).row; 
            CONSTANTS.qpFromApplicants = true;
            this.navigateToApplProfile();
        }.bind(this));
        $("#qpTabs").tabs("option", "active", selectedTab);
        $( "#qpTabs" ).on( "tabsactivate", function( event, ui ) {
        	try {
        		
            	// scroll to the last scrolled position. in cached mode.
            	if (CONSTANTS.cacheRequisitionDetailsFlg) {
            		if(CONSTANTS.qpFromApplicants) {
                		$('#qpapplGrid .slick-viewport').animate({ scrollTop: parseInt(CONSTANTS.currentQProw)*30 });
                	}
                	else {
                		$('#qpassocGrid .slick-viewport').animate({ scrollTop: parseInt(CONSTANTS.currentQProw)*30 });
                	}
            	}
            	else
            		{
	            		//set data.
	            		this.applGrid.setData(this.model.LoadApplicantPoolDgDtls);
	            		this.applGrid.render();
	            		this.assocGrid.setData(this.model.LoadAssociatePoolDgDtls);
	            		// render the grid.
	            		this.assocGrid.render();
            		}
        	}
        	catch(ex) {
        		// console.log("exception: " + ex.toString());
        		// error happen. catch. nothing important.
        	}
        }.bind(this));
    };
    
    // mark the applicant as considered in QP to avoid any network delays.
    this.markSingleApplicantAsConsidered = function() {
    	var tempVo = {};
        var consideredInQPVO = {};
        var consideredList = [];
        if(CONSTANTS.retailStaffingObj.applicantObj)
        {
	        tempVo = CONSTANTS.retailStaffingObj.applicantObj;
	        consideredInQPVO.id = tempVo.id;
	        consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
	        consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
	        consideredList.push(consideredInQPVO);
			this.checkAndMarkApplAsConsd(consideredList, consideredList);
        }
    };
    
    // mark the associate as considered in QP before moving to applicant profile.
    this.markSingleAssocAsConsitdered = function() {
    	var tempVo = {};
        var consideredInQPVO = {};
        var consideredList = [];
        if(CONSTANTS.retailStaffingObj.applicantObj)
        	{
		        tempVo = CONSTANTS.retailStaffingObj.applicantObj;
		        consideredInQPVO.id = tempVo.id;
		        consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
		        consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
		        consideredList.push(consideredInQPVO);
				this.checkAndMarkAssocAsConsd(consideredList, consideredList);
        	}
    };
    
    /*
     * This function generates associate grid with data if available
     *
     * @param data @return N/A
     */
    this.createAssocGrid = function(data) {
        if (!data) {
            data = [];
        } else {
            $(".currentAssociateGroupStartValue").text(this.model._currentAssociateGroupStartValue);
            $(".currentAssociateGroupEndValue").text(this.model._currentAssociateGroupEndValue);
            $(".currentAssociateGroupTotalValue").text(this.model.LoadMasterAssociatePoolDgDtls.length);
        }
        var options = {
            enableCellNavigation : false,
            headerRowHeight : 25,
            rowHeight : 30,
            enableColumnReorder: false,
            forceFitColumns : true
        };
        var columns = [ {
            id : "name",
            name : "Applicant Name",
            field : "name",
            width : 200,
            sortable : false,
            formatter : function(row, cell, value) {
                return "<div class='linkcell assocNameLink'><a data-value='" + value + "' class='' href='#'>" + value + "</a></div>";
            }
        }, {
            id : "store",
            name : "Assoc/Appl",
            field : "store",
            width : 100,
            sortable : false
        }, {
            id : "timeInPosition",
            name : "Time In Pos",
            field : "timeInPosition",
            width : 100,
            sortable : false
        }, {
            id : "reviewScore",
            name : "Last Review",
            field : "reviewScore",
            width : 150,
            sortable : false
        },{
            id : "consideredFlg",
            name : "Considered",
            field : "consideredFlg",
            width : 75,
            sortable : false
        },{
            id : "emplCat",
            name : "FT/PT",
            field : "emplCat",
            width : 60,
            sortable : false
        }, {
            id : "availability",
            name : "Availability",
            field : "availability",
            width : 480,
            sortable : false
        }/*, {
            id : "updTs",
            name : "Last Updated",
            field : "updTs",
            width : 212,
            sortable : false,
            formatter: function(row, cell, value){
                return "<div class='linkcell'>"+$.datepicker.formatDate('mm/dd/yy', new Date(value.split(" ")[0]))+"</div>";
            }
        }*/ ];
        var selectedTab = $("#qpTabs").tabs('option', 'active');
        $("#qpTabs").tabs("option", "active", 1);
        this.assocGrid = new Slick.Grid("#qpassocGrid", data, columns, options);
        $("#qpassocGrid").off("click", ".assocNameLink");
        $("#qpassocGrid").on("click", ".assocNameLink", function(e) {
            // Navigate to Applicant profile
            CONSTANTS.retailStaffingObj.applicantObj = this.loadAssociateDetails(e);
            this.markSingleAssocAsConsitdered();
            CONSTANTS.currentQProw = this.assocGrid.getCellFromEvent(e).row;
            this.navigateToApplProfile();
        }.bind(this));
        $("#qpTabs").tabs("option", "active", selectedTab);
    };
    /*
     * This function filters the selected applicant detail from list of
     * applicants
     *
     * @param e @return appl
     */
    this.loadApplicantDetails = function(e) {
        var name = $(e.currentTarget).find("a").attr("data-value");
        var appl = _.where(this.model.LoadApplicantPoolDgDtls, {
            "name" : name
        })[0];
        return appl;
    };
    /*
     * This function filters the selected associate detail from list of
     * associates
     *
     * @param e @return appl
     */
    this.loadAssociateDetails = function(e) {
        var name = $(e.currentTarget).find("a").attr("data-value");
        var appl = _.where(this.model.LoadAssociatePoolDgDtls, {
            "name" : name
        })[0];
        return appl;
    };
    /*
     * This function displays requisition details values in the screen
     *
     * @param reqdet @return N/A
     */
    this.displayRequisitionValues = function(reqdet) {
        $(".reqDetForm .reqNbr").text(reqdet.reqNbr);
        $(".reqDetForm .ft").text(reqdet.ft);
        $(".reqDetForm .dateCreate").text(this.formatDate(reqdet.dateCreate));
        $(".reqDetForm .pt").text(reqdet.pt);
        $(".reqDetForm .creator").text(reqdet.creator);
        $(".reqDetForm .dept").text(reqdet.dept);
        $(".reqDetForm .active").text(reqdet.active);
        $(".reqDetForm .job").text(reqdet.job);
        $(".reqDetForm .openings").text(reqdet.openings);
        $(".reqDetForm .jobTtl").text(reqdet.jobTtl);
        $(".reqDetForm .fillDt").text(this.formatDate(reqdet.fillDt));
        $(".reqDetForm .scrTyp").text(reqdet.scrTyp);
    };
    /*
     * This function displays store details values in the screen
     *
     * @param strDet @return N/A
     */
    this.displayStoreDetails = function(strDet) {
        $(".reqStrDetForm .strNum").text(strDet.strNum);
        $(".reqStrDetForm .strName").text(strDet.strName);
        $(".reqStrDetForm .phone").text(this.formatPhoneNum(strDet.phone));
        $(".reqStrDetForm .strMgr").text(strDet.strMgr);
        $(".reqStrDetForm .add").text(strDet.add);
        $(".reqStrDetForm .state").text(strDet.state);
        $(".reqStrDetForm .city").text(strDet.city);
        $(".reqStrDetForm .dstCode").text(strDet.dstCode);
        $(".reqStrDetForm .reg").text(strDet.reg);
        $(".reqStrDetForm .zip").text(strDet.zip);
        $(".reqStrDetForm .div").text(strDet.div);
    };
    /*
     * This function displays satff details values in the screen
     *
     * @param staffDet, status, reqdet, calDet, expDet, langDet, stateDet
     * @return N/A
     */
    this.displayStaffingDetails = function(staffDet, status, reqdet, calDet, expDet, langDet, stateDet) {
        staffDet = (staffDet)?staffDet:{};
        $(".staffDet .reqStatus").empty();
        // Selection of Requisition status
        for (var i = 0; i < status.length; i++) {
            $(".staffDet .reqStatus").append("<option value='" + status[i].displayStatusCode + "''>" + status[i].statusDescription + "</option>");
        }
        // setReqStatus()
        $(".staffDet .reqStatus").val(reqdet.requisitionStatusCode);
    	$(".staffDet .reqStatus").data("selectBox-selectBoxIt").refresh();
        
        $(".staffDet .hrgMgrNameTxt").val(staffDet.hrgMgrName);
        $(".staffDet .hrgMgrPhnTxt").val((staffDet.hrgMgrPhn) ? this.formatPhoneNum(staffDet.hrgMgrPhn) : "");
        $(".staffDet .requestNbrTxt").val(staffDet.requestNbr);
        // Set RSC To Manage Radio Button
        $(".staffDet .rscToManageFlgBtn").val([ reqdet.rscToManageFlg ]);
        if (this.model.interviewsHaveBeenScheduled) {
            this.blockFormElements(".staffDet .rscToManageFlg");
        } else {
            $(".staffDet .rscToManageFlg").unblock();
        }
        $(".staffDet .rscToManageFlgBtn").unbind();
        $(".staffDet .rscToManageFlgBtn").click(function() {
        	this.editFields();
            this.rscToManageClick();
        }.bind(this));
        // Set RSC Scheduled Radio Button
        $(".staffDet .rscSchdFlgBtn").val([ reqdet.rscSchdFlg ]);
        if (this.model.interviewsHaveBeenScheduled) {
            this.blockFormElements(".staffDet .rscSchdFlg");
        } else {
            $(".staffDet .rscSchdFlg").unblock();
        }
        $(".staffDet .rscSchdFlgBtn").unbind();
        $(".staffDet .rscSchdFlgBtn").click(function() {
        	this.editFields();
            this.resetCalendarCbo();
        }.bind(this));
        this.fillCalendarOptions(calDet);
        // Sets a variable so that the correct Calendar is selected
        $(".staffDet .calendarLst").val(reqdet.reqCalId);
        $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
        $(".staffDet .calendarLst").unbind("change");
        $(".staffDet .calendarLst").change(function() {
            this.getAtsHiringEventData();
        }.bind(this));
        $(".staffDet .calendarLst").prop("disabled", this.model.interviewsHaveBeenScheduled);
        $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
        $(".staffDet .calendarLst+.selectboxit-options .selectboxit-option .selectboxit-option-anchor").each(function(index,e){
        	if($(e).text().length > 40){
        		$(e).attr("title",$(e).text());
        	} else {
        		$(e).attr("title","");
        	}
        });
        $(".staffDet .qualPoolNts").val(staffDet.qualPoolNts);
        $(".staffDet .langskillablcontainer").empty();
        $(".staffDet .langskillablcontainer").createCheckBoxGrid({
            labelsArray : langDet
        });
        $(".staffDet .langskillablcontainer input").prop("checked", false);
        for (var j = 0; j < this.model.reqnLanguagesArr.length; j++) {
            $(".staffDet .langskillablcontainer input[value='" + this.model.reqnLanguagesArr[j] + "']").prop('checked', true);
        }
        this.blockFormElements(".staffDet .langskillabl");
        $(".staffDet .hrgEvntFlg").val([ staffDet.hrgEvntFlg ]);
        $(".staffDet #hrgEvntFlgY").unbind();
        $(".staffDet #hrgEvntFlgY").click(function() {
            this.editFields();
            this.getHiringEventCalendars(this.model.reqStrDet[0].strNum);
        }.bind(this));
        $(".staffDet #hrgEvntFlgN").unbind();
        $(".staffDet #hrgEvntFlgN").click(function() {
            this.editFields();
            this.getStoreCalendars(this.model.reqStrDet[0].strNum);
        }.bind(this));
        this.displayStaffDetDateFields();
        $(".staffDet .hrgEvntPhn").val((staffDet.stfhrgEvntLocPhn) ? this.formatPhoneNum(staffDet.stfhrgEvntLocPhn) : "");
        this.displayStaffDetTimeFields();
        $(".staffDet .interviewDurtn").val(reqdet.interviewDurtn);
        $(".staffDet .interviewDurtn").data("selectBox-selectBoxIt").refresh();
        $(".staffDet .interviewDurtnMin").val(reqdet.interviewDurtn);
        $(".staffDet .interviewsperTimeSlot").val(staffDet.interviewTmSlt);
        this.displayHiringEventDetails();
        $(".staffDet .hrgMgrTtl").val(staffDet.hrgMgrTtl);
        $(".staffDet .targetPay").val((staffDet.targetPay) ? parseFloat(staffDet.targetPay).toFixed(2) : "");
        this.displayStaffDetDesiredExp(staffDet, expDet);
        $(".staffDet .reqNumInterviews").val(reqdet.reqNumInterviews);
        // Set Seasonal/Temp Radio Button
        $(".staffDet .sealTempJob").val([ reqdet.sealTempJob ]);
        $(".staffDet .Referals").val(staffDet.Referals);
        $(".staffDet .daysTmMgrAvble").val(staffDet.daysTmMgrAvble);
        $(".staffDet .schedulePrefRow").empty();
        $(".staffDet .schedulePrefRow").createSchedulePreferenceGrid({
            "defaultChecked" : this.model.schdPrefArr
        });
        this.blockFormElements(".staffDet .schedulePrefRow .weekHeader");
        this.blockFormElements(".staffDet .schedulePrefRow .slots");
        $(".staffDet .hrgEvntAddr").val(staffDet.add);
        $(".staffDet .hrgEvntLoc").val(staffDet.stfhrgEvntLoc);
        $(".staffDet .hrgEvntCity").val(staffDet.city);
        this.displayStaffDetStates(staffDet, stateDet);
        $(".staffDet .hrgEvntZip").val(staffDet.zip);
        $(".staffDet .hrgEvntBrks").val(staffDet.breaks);
        this.displayStaffDetLunchLastIntrTm();
        $(".staffDet .newHrngDetForm .hrgEvntLocNew").text(this.model.hiringEventDetails.hireEventLocationDescription);
        $(".staffDet .newHrngDetForm .eventAddrNew").text(this.model.hiringEventDetails.hireEventAddressText);
        $(".staffDet .newHrngDetForm .eventCityNew").text(this.model.hiringEventDetails.hireEventCityName);
        $(".staffDet .newHrngDetForm .eventStateNew").text(this.model.hiringEventDetails.hireEventStateCode);
        $(".staffDet .newHrngDetForm .eventPostNew").text(this.model.hiringEventDetails.hireEventZipCodeCode);
     };
    /*
     * This function displays lunch/lastintr time values
     *
     * @param N/A @return N/A
     */
    this.displayStaffDetLunchLastIntrTm = function() {
        if (!_.isNull(this.model.LunchTm[0]) && this.model.LunchTm[0] !== undefined) {
            $(".staffDet .hrgEvntLunch .hours").val(this.model.LunchTm[0].hour);
            $(".staffDet .hrgEvntLunch .minutes").val(this.model.LunchTm[0].minute);
            $(".staffDet .hrgEvntLunch .tmFrmt").val(this.model.LunchTm[0].tmFrmt);
        } else {
            $(".staffDet .hrgEvntLunch .hours").val("");
            $(".staffDet .hrgEvntLunch .minutes").val("");
            $(".staffDet .hrgEvntLunch .tmFrmt").val(0);
        }
        $(".staffDet .hrgEvntLunch .tmFrmt").data("selectBox-selectBoxIt").refresh();
        if (!_.isNull(this.model.lastIntrTm[0]) && this.model.lastIntrTm[0] !== undefined) {
            $(".staffDet .lastIntrvTime .hours").val(this.model.lastIntrTm[0].hour);
            $(".staffDet .lastIntrvTime .minutes").val(this.model.lastIntrTm[0].minute);
            $(".staffDet .lastIntrvTime .tmFrmt").val(this.model.lastIntrTm[0].tmFrmt);
        } else {
            $(".staffDet .lastIntrvTime .hours").val("");
            $(".staffDet .lastIntrvTime .minutes").val("");
            $(".staffDet .lastIntrvTime .tmFrmt").val(0);
        }
        $(".staffDet .lastIntrvTime .tmFrmt").data("selectBox-selectBoxIt").refresh();
    };
    /*
     * This function generates state combo box and selects the required value
     *
     * @param staffDet, stateDet @return N/A
     */
    this.displayStaffDetStates = function(staffDet, stateDet) {
        $(".staffDet .hrgEvntState").empty();
        $(".staffDet .hrgEvntState").append("<option value='-1'>--Select--</option>");
        for (var i = 0; i < stateDet.length; i++) {
            $(".staffDet .hrgEvntState").append("<option value='" + stateDet[i].stateNbr + "'>" + stateDet[i].stateCode + "</option>");
        }
        $(".staffDet .hrgEvntState").val((staffDet.state) ? staffDet.state : -1);
        $(".staffDet .hrgEvntState").data("selectBox-selectBoxIt").refresh();
    };
    /*
     * This function generates desired experience combo box and selects the
     * required value
     *
     * @param staffDet, expDet @return N/A
     */
    this.displayStaffDetDesiredExp = function(staffDet, expDet) {
        $(".staffDet .desiredExp").empty();
        $(".staffDet .desiredExp").append("<option value='0'>--Select--</option>");
        for (var i = 0; i < expDet.length; i++) {
            $(".staffDet .desiredExp").append("<option value='" + expDet[i].expLevelCode + "'>" + expDet[i].shortDesc + "</option>");
        }
        if (!_.isNull(staffDet.desiredExp) && staffDet.desiredExp !== undefined) {
            $(".staffDet .desiredExp").val(staffDet.desiredExp);
        }
        $(".staffDet .desiredExp").data("selectBox-selectBoxIt").refresh();
    };
    /*
     * This function displays date fields and configure datepickers
     *
     * @param N/A @return N/A
     */
    this.displayStaffDetDateFields = function() {
        var days = [ "S", "M", "T", "W", "T", "F", "S" ];
        $(".staffDet .hrgEvntStartDate").datepicker({
            beforeShow : this.styleDatePicker.bind(this),
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            dayNamesMin : days
        });
        $(".staffDet .hrgEvntEndDate").datepicker({
            beforeShow : this.styleDatePicker.bind(this),
            showOn : "both",
            buttonImageOnly : true,
            buttonImage : "./assets/images/calendar.png",
            buttonText : "",
            dayNamesMin : days
        });
        $(".ui-datepicker").hide();
        $(".staffDet .hrgEvntStartDate").val(($.trim(this.model.hrEvntStrt) === "") ? "" : this.formatDate(this.model.hrEvntStrt));
        $(".staffDet .hrgEvntEndDate").val(($.trim(this.model.hrEvntEnd) === "") ? "" : this.formatDate(this.model.hrEvntEnd));
        $(".staffDet .hrgEvntStartDate").unbind();
        $(".staffDet .hrgEvntStartDate").click(function() {
            $(".staffDet .hrgEvntStartDate+.ui-datepicker-trigger").trigger("click");
            this.clearStrDt();
        }.bind(this));
        $(".staffDet .hrgEvntEndDate").unbind();
        $(".staffDet .hrgEvntEndDate").click(function() {
            $(".staffDet .hrgEvntEndDate+.ui-datepicker-trigger").trigger("click");
            this.clearEndDt();
        }.bind(this));
    };
    /*
     * This function displays time fields
     *
     * @param N/A @return N/A
     */
    this.displayStaffDetTimeFields = function() {
        if (!_.isNull(this.model.hrEvntStrtTm[0]) && this.model.hrEvntStrtTm[0] !== undefined) {
            $(".staffDet .hrgEvntStTime .hours").val(this.model.hrEvntStrtTm[0].hour);
            $(".staffDet .hrgEvntStTime .minutes").val(this.model.hrEvntStrtTm[0].minute);
            $(".staffDet .hrgEvntStTime .tmFrmt").val(this.model.hrEvntStrtTm[0].tmFrmt);
        } else {
            $(".staffDet .hrgEvntStTime .hours").val("");
            $(".staffDet .hrgEvntStTime .minutes").val("");
            $(".staffDet .hrgEvntStTime .tmFrmt").val(0);
        }
        $(".staffDet .hrgEvntStTime .tmFrmt").data("selectBox-selectBoxIt").refresh();
        if (!_.isNull(this.model.hrEvntEndTm[0]) && this.model.hrEvntEndTm[0] !== undefined) {
            $(".staffDet .hrgEvntEndTime .hours").val(this.model.hrEvntEndTm[0].hour);
            $(".staffDet .hrgEvntEndTime .minutes").val(this.model.hrEvntEndTm[0].minute);
            $(".staffDet .hrgEvntEndTime .tmFrmt").val(this.model.hrEvntEndTm[0].tmFrmt);
        } else {
            $(".staffDet .hrgEvntEndTime .hours").val("");
            $(".staffDet .hrgEvntEndTime .minutes").val("");
            $(".staffDet .hrgEvntEndTime .tmFrmt").val(0);
        }
        $(".staffDet .hrgEvntEndTime .tmFrmt").data("selectBox-selectBoxIt").refresh();
    };
    /*
     * This function styles the datepicker to look like flex
     *
     * @param staffDet, stateDet @return N/A
     */
    this.styleDatePicker = function() {
        $('#ui-datepicker-div').removeClass(function() {
            return $('input').get(0).id;
        });
        $('#ui-datepicker-div').addClass("datepicker-flex");
    };
    /*
     * This function changes between Calendar and hiring event
     *
     * @param N/A @return N/A
     */
    this.editFields = function() {

        this.model.editStaffingDet = true;

        if ($(".staffDet .hrgEvntFlg:checked").val() === 'N') {
            $(".staffDet .hrgEvntY").hide();
            $(".staffDet .calendarLabel").text("Attached to Calendar:");
            this.model.hiringEventDetails = {};
            $(".staffDet .newHrngDetForm .hrgEvntStartDateNew").text("");
            $(".staffDet .newHrngDetForm .hrgEvntEndDateNew").text("");
            $(".staffDet .newHrngDetForm .eventTypeText").text("");
            $(".staffDet .newHrngDetForm .eventMgrName").text("");
            $(".staffDet .newHrngDetForm .eventMgrTitle").text("");
            $(".staffDet .newHrngDetForm .eventMgrPhone").text("");
            $(".staffDet .newHrngDetForm .eventMgrEmail").text("");
            $(".staffDet .hrgEvntLocNew").text("");
            $(".staffDet .eventAddrNew").text("");
            $(".staffDet .eventCityNew").text("");
            $(".staffDet .eventStateNew").text("");
            $(".staffDet .eventPostNew").text("");
            $(".staffDet .interviewDurtnRow").hide();
        } else if ($(".staffDet .hrgEvntFlg:checked").val() === 'Y') {
            if (this.model.reqStaffDet.length > 0) {
                if (this.checkIfCalendar()) {
                    // This is an Old Hiring Event
                    $(".hrgEvntY.oldHrngDetForm").show();
                    $(".hrgEvntY.newHrngDetForm").hide();
                    $(".staffDet .calendarLabel").text("Attached to Calendar:");
                    $(".staffDet .interviewDurtnRow").hide();
                } else {
                    // This is a new Hiring Event
                    $(".hrgEvntY.oldHrngDetForm").hide();
                    $(".hrgEvntY.newHrngDetForm").show();
                    $(".staffDet .calendarLabel").text("Attached to Hiring Event:");
                    $(".staffDet .interviewDurtnRow").show();
                }
            } else {
                // This is a new Hiring Event
                $(".hrgEvntY.oldHrngDetForm").hide();
                $(".hrgEvntY.newHrngDetForm").show();
                $(".staffDet .calendarLabel").text("Attached to Hiring Event:");
                $(".staffDet .interviewDurtnRow").show();
            }
        }
    };
    /*
     * This function check for calendar or hiring event
     *
     *
     * @param N/A @return boolean
     */
    this.checkIfCalendar = function() {
        var testCond1 = this.model.reqStaffDet[0].hireEvntType !== "SSE" && this.model.reqStaffDet[0].hireEvntType !== "MSE";
        var testCond2 = this.model.reqStaffDet[0].hireEvntType !== "OSE" && this.model.reqStaffDet[0].hireEvntType !== undefined;
        return testCond1 && testCond2 && !_.isNull(this.model.reqStaffDet[0].hireEvntType);
    };
    /*
     * This function makes a service call to get all hiring event calendars
     *
     *
     * @param data @return N/A
     */
    this.getHiringEventCalendars = function(data) {
        if (this.isHrngEvtTypeNotSDE()) {
            this.showWaitCursor();
            this.rsaServices.getStoreHiringEventList(data, this.getHiringEventsByStrSuccess.bind(this), this.getHiringEventsByStrFail.bind(this));
            this.model.interviewScheduleCollection = [];
            this.fillCalendarOptions(this.model.interviewScheduleCollection);
            $(".staffDet .calendarLst").val(0);
            $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
            $(".staffDet .calendarLst+.selectboxit-options .selectboxit-option .selectboxit-option-anchor").each(function(index,e){
            	if($(e).text().length > 40){
            		$(e).attr("title",$(e).text());
            	} else {
            		$(e).attr("title","");
            	}
            });
        }
    };
    /*
     * This function is a success handler of getHiringEvents service call
     * this function reloads the calendar combobox and resets it
     *
     * @param response @return N/A
     */
    this.getHiringEventsByStrSuccess = function(response) {
        this.showDefaultCursor();
        response = response.Response;
        this.model = _.extend(this.model, new getHiringEventsByStr(response));
        if (response.status && "ScheduleDescList" in response) {
            this.fillCalendarOptions(this.model.interviewScheduleCollection);
            $(".staffDet .calendarLst").val(0);
            $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
        } else {
            if (response.error.errorMsg !== "The store number entered is invalid.") {
                this.popup.alert(response.error.errorMsg);
            }
            // Error fetching Store Calendars
            // => prompt calendar combox box for "Enter 4 DigitStore #..."
            $(".staffDet .calendarLst").val(-1);
            $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
        }
        $(".staffDet .calendarLst+.selectboxit-options .selectboxit-option .selectboxit-option-anchor").each(function(index,e){
        	if($(e).text().length > 40){
        		$(e).attr("title",$(e).text());
        	} else {
        		$(e).attr("title","");
        	}
        });
    };
    /*
     * This function is a failure handler of getHiringEvents service call
     * this function enables the cursor
     *
     * @param N/A @return N/A
     */
    this.getHiringEventsByStrFail = function() {
        this.showDefaultCursor();
    };
    /*
     * This function is a event handler of RSC to Manage radio button
     *
     * @param N/A @return N/A
     */
    this.rscToManageClick = function() {
        if ($(".staffDet .rscToManageFlgBtn:checked").val() === "Y") {
            $(".staffDet .rscSchdFlg").unblock();
            this.resetCalendarCbo();
        } else {
            $(".staffDet .rscSchdFlgBtn").val([ "N" ]);
            this.resetCalendarCbo();
            this.blockFormElements(".staffDet .rscSchdFlg");
        }

    };
    /*
     * This function resets the calendar combo box and label
     *
     * @param N/A @return N/A
     */
    this.resetCalendarCbo = function() {
        if ($(".staffDet .rscSchdFlg .blockUI").length === 0) {
            if ($(".staffDet .rscSchdFlgBtn:checked").val() === "N") {
                $(".staffDet .calendarLst").val(0);
                $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
                $(".staffDet .calendarLst+.selectboxit-options .selectboxit-option .selectboxit-option-anchor").each(function(index,e){
                	if($(e).text().length > 40){
                		$(e).attr("title",$(e).text());
                	} else {
                		$(e).attr("title","");
                	}
                });
                this.blockFormElements(".staffDet .calendar");
                $(".staffDet .interviewDurtn").val(0);
                $(".staffDet .interviewDurtn").data("selectBox-selectBoxIt").refresh();
                this.blockFormElements(".staffDet .duration");
                this.blockFormElements(".staffDet .hrgEvnt");
                $(".staffDet .interviewDurtnRow").show();

                this.getStoreCalendars(this.model.reqStrDet[0].strNum);

                this.model.hiringEventDetails = null;
                $(".staffDet .hrgEvntY ").hide();

                $(".staffDet .hrgEvntFlg").val([ "N" ]);
                $(".staffDet .calendarLabel").text("Attached to Calendars:");
            } else {
                $(".staffDet .calendar").unblock();
                $(".staffDet .duration").unblock();
                $(".staffDet .hrgEvnt").unblock();
                $(".staffDet .interviewDurtnRow").show();
            }
        }
    };
    /*
     * This function makes a service call to get store calendars
     *
     * @param data @return N/A
     */
    this.getStoreCalendars = function(data) {
        if (this.isHrngEvtTypeNotSDE()) {
            this.showWaitCursor();
            this.rsaServices.getStoreCalendarList(data, this.getCalendarsByStrSuccess.bind(this), this.getCalendarsByStrFail.bind(this));
            this.model.interviewScheduleCollection = [];
            this.fillCalendarOptions(this.model.interviewScheduleCollection);
            $(".staffDet .calendarLst").val(0);
            $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
            $(".staffDet .calendarLst+.selectboxit-options .selectboxit-option .selectboxit-option-anchor").each(function(index,e){
            	if($(e).text().length > 40){
            		$(e).attr("title",$(e).text());
            	} else {
            		$(e).attr("title","");
            	}
            });
        }
    };
    /*
     * This function is a success handler of get store calendars service call
     * this function reloads the calendar combobox and resets it
     *
     * @param response @return N/A
     */
    this.getCalendarsByStrSuccess = function(response) {
        this.showDefaultCursor();
        response = response.Response;
        this.model = _.extend(this.model, new getCalendarsByStr(response));
        if (response.status && response.ScheduleDescList && response.ScheduleDescList.ScheduleDescriptionDetails) {
            this.fillCalendarOptions(this.model.interviewScheduleCollection);
            $(".staffDet .calendarLst").val(0);
            $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
        } else {
            if (response.error.errorMsg !== "The store number entered is invalid.") {
                this.popup.alert(response.error.errorMsg);
            }
            // Error fetching Store Calendars
            // => prompt calendar combox box for "Enter 4 DigitStore #..."
            $(".staffDet .calendarLst").val(-1);
            $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
        }
        $(".staffDet .calendarLst+.selectboxit-options .selectboxit-option .selectboxit-option-anchor").each(function(index,e){
        	if($(e).text().length > 40){
        		$(e).attr("title",$(e).text());
        	} else {
        		$(e).attr("title","");
        	}
        });
    };
    /*
     * This function is a failure handler of get store calendars service call
     * this function enables the cursor
     *
     * @param response @return N/A
     */
    this.getCalendarsByStrFail = function() {
        this.showDefaultCursor();
    };
    /*
     * This function makes getHiringEventDetails service call
     *
     * @param N/A
     * @return N/A
     */
    this.getAtsHiringEventData = function() {
        // Only execute if ATS Hiring Event. Gets new Hiring Event Data when a
        // different Event is selected

        if (parseInt($(".staffDet .calendarLst").val()) !== 0) {
            if ($(".staffDet .hrgEvntFlg:checked").val() === "Y" && (this.isHrngEvtTypeNotSDE())) {
                this.blockFullPage();
                var calId = $(".staffDet .calendarLst").val();
                var calObj = _.where(this.model.interviewScheduleCollection, {
                    "requisitionCalendarId" : parseInt(calId)
                });
                this.rsaServices.getHiringEventDetailsEvtID(calObj[0].hireEventId, this.getHiringEventDetailsSuccess.bind(this), this.getHiringEventDetailsFail.bind(this));
            }
        } else {
            this.model.hiringEventDetails = {};
            $(".staffDet .newHrngDetForm .hrgEvntStartDateNew").text("");
            $(".staffDet .newHrngDetForm .hrgEvntEndDateNew").text("");
            $(".staffDet .newHrngDetForm .eventTypeText").text("");
            $(".staffDet .newHrngDetForm .eventMgrName").text("");
            $(".staffDet .newHrngDetForm .eventMgrTitle").text("");
            $(".staffDet .newHrngDetForm .eventMgrPhone").text("");
            $(".staffDet .newHrngDetForm .eventMgrEmail").text("");
            $(".staffDet .hrgEvntLocNew").text("");
            $(".staffDet .eventAddrNew").text("");
            $(".staffDet .eventCityNew").text("");
            $(".staffDet .eventStateNew").text("");
            $(".staffDet .eventPostNew").text("");
        }
    };
    /*
     * This function is a success handler of getHiringEventDetails service call
     *
     * @param response
     * @return N/A
     */
    this.getHiringEventDetailsSuccess = function(response) {
        this.unblockFullPage();
        response = response.Response;
        this.model = _.extend(this.model, new getHiringEventDetails(response));
        if (response.error) {
            this.popup.alert(this.model.returnedErrorMessage);
        } else {
            this.displayHiringEventDetails();
            $(".staffDet .hrgEvntLocNew").text(this.model.hiringEventDetails.hireEventLocationDescription);
            $(".staffDet .eventAddrNew").text(this.model.hiringEventDetails.hireEventAddressText);
            $(".staffDet .eventCityNew").text(this.model.hiringEventDetails.hireEventCityName);
            $(".staffDet .eventStateNew").text(this.model.hiringEventDetails.hireEventStateCode);
            $(".staffDet .eventPostNew").text(this.model.hiringEventDetails.hireEventZipCodeCode);
        }
    };
    /*
     * This function is a failure handler of getHiringEventDetails service call
     * this function enables the page
     *
     * @param N/A
     * @return N/A
     */
    this.getHiringEventDetailsFail = function() {
        this.unblockFullPage();
    };
    /*
     * This function appends options to calendar select box
     *
     * @param calDet
     * @return N/A
     */
    this.fillCalendarOptions = function(calDet) {
        $(".staffDet .calendarLst").empty();
        for (var i = 0; i < calDet.length; i++) {
            $(".staffDet .calendarLst")
                    .append("<option value='" + calDet[i].requisitionCalendarId + "' " + ((calDet[i].enabled) ? "" : "disabled") + ">" + calDet[i].requisitionCalendarDescription + "</option>");
        }
        $(".staffDet .calendarLst").data("selectBox-selectBoxIt").refresh();
        
        $(".staffDet .calendarLst+.selectboxit-options .selectboxit-option .selectboxit-option-anchor").each(function(index,e){
        	if($(e).text().length > 40){
        		$(e).attr("title",$(e).text());
        	} else {
        		$(e).attr("title","");
        	}
        });
    };
    /*
     * This function clears hiringEvrnt start date
     *
     * @param N/A
     * @return N/A
     */
    this.clearStrDt = function() {
        this.model.editStaffingDet = true;
        $(".staffDet .hrgEvntStartDate").val("");
    };
    /*
     * This function clears hiringEvrnt end date
     *
     * @param N/A
     * @return N/A
     */
    this.clearEndDt = function() {
        this.model.editStaffingDet = true;
        $(".staffDet .hrgEvntEndDate").val("");
    };
    /*
     * This function displays candidates grid
     * Clicking on the Candidate link will navigate to candidate details
     *
     * @param data
     * @return N/A
     */
    this.displayCandidatesGrid = function(data) {
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true
        };
        var columns = [ {
            id : "candRefNbr",
            name : "Candidate ID",
            field : "candRefNbr",
            width : 100,
            sortable : false,
            formatter : function(row, cell, value) {
                return "<div class='linkcell candidateIDLink'><a data-value=" + value + " class=''>" + value + "</a></div>";
            }
        }, {
            id : "aid",
            name : "Associate ID",
            field : "aid",
            width : 100,
            sortable : false
        }, {
            id : "name",
            name : "Name",
            field : "name",
            width : 150,
            sortable : false
        }, {
            id : "internalExternal",
            name : "Int/Ext",
            field : "internalExternal",
            width : 100,
            sortable : false
        }, {
            id : "canStatus",
            name : "Cand.Active",
            field : "canStatus",
            width : 100,
            sortable : false
        }, {
            id : "offerMade",
            name : "Offered",
            field : "offerMade",
            width : 100,
            sortable : false
        }, {
            id : "offerDate",
            name : "Offer Date",
            field : "offerDate",
            width : 100,
            sortable : false
        }, {
            id : "offerStat",
            name : "Offer Status",
            field : "offerStat",
            width : 100,
            sortable : false
        }, {
            id : "decCD",
            name : "Decline Code",
            field : "decCD",
            width : 100,
            sortable : false
        }, {
            id : "drugTest",
            name : "Drug Test",
            field : "drugTest",
            width : 250,
            sortable : false
        } ];
        if(this.candidatesGrid){
            this.candidatesGrid.setData(data);
            this.candidatesGrid.invalidate();
        }else{
            this.candidatesGrid = new Slick.Grid("#candidates-grid", data, columns, options);
        }
        $("#candidates-grid").off("click", ".candidateIDLink");
        $("#candidates-grid").on("click", ".candidateIDLink", function(e) {
            var candId = $(e.currentTarget).find("a").attr("data-value");
            var ssnNbr = _.where(this.model.CndtDltls, {
                "candRefNbr" : parseInt(candId)
            })[0].ssnNbr;
            CONSTANTS.retailStaffingObj.applicantId = ssnNbr;
            CONSTANTS.retailStaffingObj.candRefId = candId;
            this.candLinkClick();
        }.bind(this));
    };
    /*
     * This function loads candidates details
     *
     * @param N/A
     * @return N/A
     */
    this.candLinkClick = function() {
        this.linkClick(this.loadCandidateDetails.bind(this), this.openPopUpMisPhnSrnLoadCandPhn.bind(this, this.loadCandidateDetails.bind(this)));
    };
    /*
     * This function navigates to candidates details screen
     *
     * @param N/A
     * @return N/A
     */
    this.loadCandidateDetails = function() {
        this.navigateToCandidateDetails();
    };
    /*
     * This function navigates to candidates details screen
     *
     * @param N/A
     * @return N/A
     */
    this.navigateToCandidateDetails = function() {
        $.get('app/RetailStaffing/view/candidateDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
    /*
     * This function displays phonescreen grid
     * Clicking on the Candidate link will navigate to phone screen details
     *
     * @param data
     * @return N/A
     */
    this.displayPhoneScreenGrid = function(data) {
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true
        };
        var columns = [ {
            id : "itiNbr",
            name : "Phone Screen/Interview Number",
            field : "itiNbr",
            width : 150,
            sortable : false,
            formatter : function(row, cell, value) {
                return "<div class='linkcell phnScrIDLink'><a data-value=" + value + " class=''>" + value + "</a></div>";
            }
        }, {
            id : "candRefNbr",
            name : "Candidate ID",
            field : "candRefNbr",
            width : 100,
            sortable : false,
            formatter : function(row, cell, value) {
                return "<div class='linkcell candidateIDLink'><a data-value=" + value + " class=''>" + value + "</a></div>";
            }
        }, {
            id : "aid",
            name : "Associate ID",
            field : "aid",
            width : 100,
            sortable : false
        }, {
            id : "name",
            name : "Name",
            field : "name",
            width : 200,
            sortable : false
        }, {
            id : "ynStatusdesc",
            name : "Min Reqrmt Status",
            field : "ynStatusdesc",
            width : 100,
            sortable : false
        }, {
            id : "phoneScreenStatusDesc",
            name : "Phone Screen Status",
            field : "phoneScreenStatusDesc",
            width : 200,
            sortable : false
        }, {
            id : "interviewStatusDesc",
            name : "Interview Status",
            field : "interviewStatusDesc",
            width : 200,
            sortable : false
        }, {
            id : "intDate",
            name : "Interview Date",
            field : "intDate",
            width : 165,
            sortable : false
        } ];
        CONSTANTS.LoadITIDGDltls = data;
        if(this.phoneScreenGrid){
            this.phoneScreenGrid.setData(data);
            this.phoneScreenGrid.invalidate();
        }
        else{
            this.phoneScreenGrid = new Slick.Grid("#phnscr-grid", data, columns, options);
        }
        $("#phnscr-grid").off("click", ".candidateIDLink");
        $("#phnscr-grid").on("click", ".candidateIDLink", function(e) {
            var candId = $(e.currentTarget).find("a").attr("data-value");
            var cndtNbr = _.where(this.model.createphnScrDtls, {
                "candRefNbr" : parseInt(candId)
            })[0].cndtNbr;
            CONSTANTS.retailStaffingObj.applicantId = cndtNbr;
            CONSTANTS.retailStaffingObj.candRefId = candId;
            this.candLinkClick();
        }.bind(this));
        $("#phnscr-grid").off("click", ".phnScrIDLink");
        $("#phnscr-grid").on("click", ".phnScrIDLink", function(e) {
            CONSTANTS.retailStaffingObj.phoneScreenNumber = $(e.currentTarget).find("a").attr("data-value");
            CONSTANTS.LoadITIDGDltls = [{"itiNbr":$(e.currentTarget).find("a").attr("data-value")}];
            this.phnScrLinkClick();
        }.bind(this));
    };
    /*
     * This function loads phonescreen details
     *
     * @param N/A
     * @return N/A
     */
    this.phnScrLinkClick = function() {
        this.linkClick(this.loadPhoneDetails.bind(this), this.openPopUpMisPhnSrnLoadCandPhn.bind(this, this.loadPhoneDetails.bind(this)));
    };
    /*
     * This function navigates to phonescreen details screen
     *
     * @param N/A
     * @return N/A
     */
    this.loadPhoneDetails = function() {
        this.navigateToPhoneScreenDetails();
    };
    /*
     * This function navigates to phonescreen details screen
     *
     * @param N/A
     * @return N/A
     */
    this.navigateToPhoneScreenDetails = function() {
        $.get('app/RSAPhoneScreenDetails/view/phoneScreenDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
    /*
     * This function shows popup if there is unsaved data
     * or navigates to other screens if not
     *
     * @param okclick1
     * @param okclick2
     * @return N/A
     */
    this.linkClick = function(okclick1, okclick2, closeBtn) {
        // Check if screen has been edited and there are not any missing phone
        // screens
        if (this.model.editStaffingDet && !this.model.isCandPhnScrnMissing) {
            this.popup.warnUnsavedData(CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_1, CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_2, okclick1, this.handleClose.bind(this), 150, 400);
        }
        // Check if screen has been edited and there are missing phone screens
        else if (this.model.editStaffingDet && this.model.isCandPhnScrnMissing) {
            this.popup.warnUnsavedData(CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_1, CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_2, okclick2, this.handleClose.bind(this), 150, 400);
        }
        // Check if screen has not been edited and there are missing phone
        // screens
        else if (!this.model.editStaffingDet && this.model.isCandPhnScrnMissing) {
            okclick2();
        }
        else if(closeBtn && applProf && applProf.model.rjctSaveCheck){
            this.popup.warnUnsavedData(CONSTANTS.RJCT_DTLS_UNSAVED_PART_1, CONSTANTS.RJCT_DTLS_UNSAVED_PART_2, okclick1, this.handleClose.bind(this), 150, 400);
        }
        else {
            okclick1();
        }
    };
    /*
     * This function displays staff details missing popup
     * Hides any popup if already present
     *
     * @param N/A
     * @return N/A
     */
    this.openPopUpMisPhnSrnLoadCandPhn = function(okclick) {
        if ($('#genericPopup').hasClass('in')) {
            $("#genericPopup").modal("hide");
        }
        this.popup.warnMissingPhnSrc(CONSTANTS.STAFF_DET_MISSING_PHONE_SCRN_PART_1, CONSTANTS.STAFF_DET_MISSING_PHONE_SCRN_PART_2, okclick, this.handleClose.bind(this), 160, 475);
    };
    /*
     * This function creates schedule preference grid
     *
     * @param ele
     * @return N/A
     */
    this.createSchedulePreferenceGrid = function(ele) {

        var splabelsArray = [ "Anytime", "Early Am", "Mornings", "Afternoons", "Evenings", "Late PM", "Overnight" ];
        var headerRow = '<div class="col-xs-12 row"><div class="col-xs-4 field-align">Schedule Preference:</div><div class="col-xs-8 row value-align schedulePref">' + '<div class="col-xs-12 row weekHeader">' + '<label  class="col-xs-1 control-label">Mon</label>' + '<label  class="col-xs-1 control-label">Tue</label>' + '<label  class="col-xs-1 control-label">Wed</label>' + '<label  class="col-xs-1 control-label">Thr</label>' + '<label  class="col-xs-1 control-label">Fri</label>' + '<label  class="col-xs-1 control-label">Sat</label>' + '<label  class="col-xs-1 control-label">Sun</label>' + '</div></div></div>';

        $(ele).append(headerRow);
        for (var i = 0; i < splabelsArray.length; i++) {
            var row = '<div class=" col-xs-12 row"><div class="col-xs-4 field-align">' + splabelsArray[i] + '</div><div class="col-xs-8 row value-align schedulePref">' + '<div class="col-xs-12 row checkBoxRow">' + '<div class=" col-xs-1"><input type="checkBox" class=" spCheckBox"></div>' + '<div class=" col-xs-1"><input type="checkBox" class="  spCheckBox"></div>' + '<div class=" col-xs-1"><input type="checkBox" class=" spCheckBox"></div>' + '<div class=" col-xs-1"><input type="checkBox" class=" spCheckBox"></div>' + '<div class=" col-xs-1"><input type="checkBox" class=" spCheckBox"></div>' + '<div class=" col-xs-1"><input type="checkBox" class=" spCheckBox"></div>' + '<div class=" col-xs-1"><input type="checkBox" ></div>' + '</div></div>';

            $(ele).append(row);
        }

    };
    /*
     * This function formats the date object into a required string
     *
     * @param date_object
     * @return formatteddate string
     */
    this.formatDate = function(strDate) {
        if(($.isPlainObject(strDate) && $.isEmptyObject(strDate)) || Date.parse(strDate) === NaN){
            return strDate;
        }
        strDate = strDate.toString().split("-").join("/");
        var date = new Date(strDate);
        return ("0" + (date.getMonth() + 1)).slice(-2) + "/" + ("0" + date.getDate()).slice(-2) + "/" + date.getFullYear();
    };
    /*
     * This function formats the unformatted phone number into a required string
     *
     * @param unformatted phonenumber
     * @return formatted phonenumber
     */
    this.formatPhoneNum = function(phone) {
        if (phone) {
            phone = phone.toString();
            if (phone[3] === "-") {
                return phone;
            }
            return phone.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
        }

    };
    /*
     * This function blocks the particular form elements
     *
     * @param id
     * @return N/A
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
     * This function return true if current event type is not SDE
     *
     * @param N/A
     * @return true/false
     */
    this.isHrngEvtTypeNotSDE = function() {
        return this.model.reqStaffDet.length === 0 || this.model.reqStaffDet[0].hireEvntType !== "SDE";
    };
    /*
     * This function return true if RSC Scheduled
     *
     * @param N/A
     * @return true/false
     */
    this.ifRSCScheduled = function() {
        if (this.isHrngEvtTypeNotSDE()) {
            if (parseInt($(".staffDet .interviewDurtn").val()) === 0) {
                this.showPopUp(CONSTANTS.INTVW_DUR_VAL_MSG);
                return true;
            }
        } else {
            if ($(".staffDet .hrgEvntFlg:checked").val() === "Y") {
                if ($(".staffDet .interviewDurtnMin").val() === "") {
                    this.showPopUp(CONSTANTS.INTVW_DUR_VAL_MSG);
                    return true;
                }
            } else {
                if (parseInt($(".staffDet .interviewDurtn").val()) === 0) {
                    this.showPopUp(CONSTANTS.INTVW_DUR_VAL_MSG);
                    return true;
                }
            }
        }
        return false;
    };
    /*
     * This function return true if RSC is Scheduled and selected as Y
     *
     * @param N/A
     * @return true/false
     */
    this.validateIntvwDur = function() {
        // Validate that a Interview Duration has been selected if RSC To
        // Schedule is Yes
        if ($(".staffDet .rscSchdFlgBtn:checked").val() === "Y" && this.ifRSCScheduled()) {
            return true;
        }
        return false;
    };
    /*
     * This function validates the hiring event
     *
     * @param N/A
     * @return true/false
     */
    this.validateHiringEvent = function() {
        // Validate that a Calendar or Hiring Event has been selected if RSC To
        // Schedule is Yes
        if ($(".staffDet .rscSchdFlgBtn:checked").val() === "Y" && parseInt($(".staffDet .calendarLst").val()) === 0) {
            if ($(".staffDet .hrgEvntFlg:checked").val() === "N") {
                // Not a Hiring Event
                this.showPopUp(CONSTANTS.FILL_REQUIRED_FLDS_CALENDAR);
            } else if ($(".staffDet .hrgEvntFlg:checked").val() === "Y" && (this.isHrngEvtTypeNotSDE())) {
                // Is ATS Hiring Event Only
                this.showPopUp(CONSTANTS.FILL_REQUIRED_FLDS_HIRING_EVENT);
            }
            return true;
        }
        return false;
    };
    /*
     * This function validates the pre ATS hiring event
     *
     * @param N/A
     * @return true/false
     */
    this.validatePreATSHiringEvent = function() {
        // Pre ATS Hiring Event, only check if Old Hiring Event
        if (this.model.reqStaffDet.length > 0 && this.model.reqStaffDet[0].hireEvntType === "SDE" && $(".staffDet .hrgEvntStartDate")) {
            var HrStrDt = new Date($(".staffDet .hrgEvntStartDate").val());
            var HrEndDt = new Date($(".staffDet .hrgEvntEndDate").val());
            // to validate if hiring event start date is lesser than hiring
            // event end date
            if (HrStrDt && HrEndDt && HrEndDt < HrStrDt) {
                this.showPopUp(CONSTANTS.REQ_EVNT_DATE);
                $(".staffDet .hrgEvntEndDate").val("");
                return true;
            }
        }
        return false;
    };
    /*
     * This function saves hiring event data
     *
     * @param N/A
     * @return N/A
     */
    this.saveHrngEvtDataUpd = function() {
        // Set a flag as to if Hiring Event Data should be updated
        if (this.model.reqStaffDet.length > 0) {
            if (this.model.reqStaffDet[0].hireEvntType === "SDE") {
                // Old Hiring Event
                this.saveReqDtlsobj.hireEventType = this.model.reqStaffDet[0].hireEvntType;
                this.saveReqDtlsobj.hrEvntId = this.model.hrEvntId;
            } else {
                // This is a Requisition created after ATS Hire Events
                // Implmentation, MUST let Java know this so it knows how to
                // handle the record in HR_HIRE_EVNT_REQN
                if (!this.model.hiringEventDetails || (!this.model.hiringEventDetails.hireEventTypeIndicator && this.model.reqStaffDet[0].hireEvntType)) {
                    // This was changed from a Hiring Event to a Calendar
                    // Requisition or RSC to Schedule was changed to No
                    this.saveReqDtlsobj.insertDeleteUpdateHrHireEvntReqnTable = "DELETE";
                } else if (this.model.hiringEventDetails.hireEventTypeIndicator && !this.model.reqStaffDet[0].hireEvntType) {
                    // This was changed from a Calendar Requisition to Hiring
                    // Event
                    this.saveReqDtlsobj.insertDeleteUpdateHrHireEvntReqnTable = "INSERT";
                    this.saveReqDtlsobj.hireEventType = this.model.hiringEventDetails.hireEventTypeIndicator;
                    this.saveReqDtlsobj.hrEvntId = this.getHiringEventIDAsZeroIfUndef();
                } else {
                    // This is still a Hiring Event or Calendar Req
                    this.saveReqDtlsobj.insertDeleteUpdateHrHireEvntReqnTable = "UPDATE";
                    this.saveReqDtlsobj.hireEventType = this.model.hiringEventDetails.hireEventTypeIndicator;
                    this.saveReqDtlsobj.hrEvntId = this.getHiringEventIDAsZeroIfUndef();
                }

            }
        } else {
            // This is an AIMS created requisition and the 1st time it has been
            // saved in RSA
            this.saveReqDtlsobj.hireEventType = this.model.hiringEventDetails.hireEventTypeIndicator;
            this.saveReqDtlsobj.hrEvntId = this.getHiringEventIDAsZeroIfUndef();
        }
    };
    /*
     * This function returns 0 as hiring event ID if hiring event ID is not defined
     *
     * @param N/A
     * @return 0/id
     */
    this.getHiringEventIDAsZeroIfUndef = function(){
        return (this.model.hiringEventDetails.hireEventId)?this.model.hiringEventDetails.hireEventId.toString():"0";
    };
    /*
     * This function validates form elements and show prompts
     *
     * @param HrEvntHr
     * @param HrEvntMin
     * @param HrEvntTmAm
     * @return true/valse
     */
    this.checkAndShowMsg = function(HrEvntHr, HrEvntMin, HrEvntTmAm) {
        if ((parseInt(HrEvntHr)) < 1 || (parseInt(HrEvntHr)) > 12) {
            this.showPopUp(CONSTANTS.HOUR_ALERT + CONSTANTS.HIRE_EVNT_STRT_ALERT);
            return true;
        }

        if ((parseInt(HrEvntMin)) < 0 || (parseInt(HrEvntMin)) > 59) {
            this.showPopUp(CONSTANTS.MINUTE_ALERT + CONSTANTS.HIRE_EVNT_STRT_ALERT);
            return true;
        }

        if (parseInt(HrEvntTmAm) === 0) {
            this.showPopUp(CONSTANTS.AMPM_ALERT + CONSTANTS.HIRE_EVNT_STRT_ALERT);
            return true;
        }
        return false;
    };
    /*
     * This function validates Hour Mins of times
     *
     * @param HrEvntHr
     * @param HrEvntMin
     * @param HrEvntTmAm
     * @param propName
     * @return true/false
     */
    this.validateHourMin = function(HrEvntHr, HrEvntMin, HrEvntTmAm, propName, message) {
        // to validate hour and minute value
        if (this.isHrEvntHrMinNotNull(HrEvntMin)) {

            if (this.checkAndShowMsg(HrEvntHr, HrEvntHr, HrEvntTmAm)) {
                return true;
            }
            var temp = parseInt(HrEvntHr);
            // to change hour to 24 hour format
            if (HrEvntTmAm === 2) {
                if (HrEvntHr !== CONSTANTS.HOUR_ON_TWELVE) {
                    temp = parseInt(HrEvntHr.text) + 12;
                }
            } else if (HrEvntHr === CONSTANTS.HOUR_ON_TWELVE) {
                temp = 24;
            }
            var hour = temp.toString();
            var min = HrEvntMin;
            var timeobj = {
                "hour" : hour,
                "minute" : min,
                "tmFrmt" : 0
            };
            this.saveReqDtlsobj[propName] = timeobj;
        } else {
            this.showPopUp(CONSTANTS.MINUTE_ALERT + message);
            return true;
        }
        return false;
    };
    /*
     * This function process the time fields
     *
     * @param className
     * @param propName
     * @return true/false
     */
    this.processHrngEvtTime = function(className, propName, message) {
        var HrEvntHr = $(".staffDet ." + className + " .hours").val();
        var HrEvntMin = $(".staffDet ." + className + " .minutes").val();
        var HrEvntTmAm = $(".staffDet ." + className + " .tmFrmt").val();
        if (this.isHrEvntHrMinNotNull(HrEvntHr) && this.validateHourMin(HrEvntHr, HrEvntMin, HrEvntTmAm, propName, message)) {
            return true;
        }
        // to validate if minute value is entered without hour value
        if (this.HrEvntMinNotNullHrNull()) {
            this.showPopUp(CONSTANTS.HOUR_ALERT + message);
            return true;
        }
        // If AM/PM is selected without proper hour or minute value

        if (this.isHrEvntHrMinNull(HrEvntMin) && this.isHrEvntHrMinNull(HrEvntHr) && (parseInt(HrEvntTmAm) !== 0)) {
            this.showPopUp(CONSTANTS.HIRE_EVNT_HR_MIN_NUMBER + message);
            return true;
        }
        return false;
    };
    /*
     * This function returns true if min is not null and hr is null
     *
     * @param HrEvntMin
     * @param HrEvntHr
     * @return true/false
     */
    this.HrEvntMinNotNullHrNull = function(HrEvntMin, HrEvntHr) {
        return this.isHrEvntHrMinNotNull(HrEvntMin) && this.isHrEvntHrMinNull(HrEvntHr);
    };
    /*
     * This function returns true if min is null
     *
     * @param HrEvntMin
     * @return true/false
     */
    this.isHrEvntHrMinNull = function(HrEvntHrMin) {
        return !HrEvntHrMin || HrEvntHrMin === "";
    };
    /*
     * This function returns true if min is not null
     *
     * @param HrEvntMin
     * @return true/false
     */
    this.isHrEvntHrMinNotNull = function(HrEvntHrMin) {
        return HrEvntHrMin && HrEvntHrMin !== "";
    };
    /*
     * This function validates hiring event time
     *
     * @param N/A
     * @return true/false
     */
    this.validateHrngEvtTime = function() {
        // to handle Hire Event Start time
        if (this.processHrngEvtTime("hrgEvntStTime", "startTime", CONSTANTS.HIRE_EVNT_STRT_ALERT)) {
            return true;
        }

        // to handle Hire Event End time
        if (this.processHrngEvtTime("hrgEvntEndTime", "endTime", CONSTANTS.HIRE_EVNT_END_ALERT)) {
            return true;
        }

        // to handle Lunch time
        if (this.processHrngEvtTime("hrgEvntLunch", "lunch", CONSTANTS.HIRE_EVNT_LNCH_ALERT)) {
            return true;
        }

        // TO handle Lst Interview Time LstIntrTm
        if (this.processHrngEvtTime("lastIntrvTime", "lastIntrTm", CONSTANTS.HIRE_EVNT_LST_ALERT)) {
            return true;
        }

        return false;
    };
    /*
     * This function sets hiring event dates
     *
     * @param N/A
     * @return N/A
     */
    this.HrEvntDates = function() {
        var HrEvntStrDt = $(".staffDet .hrgEvntStartDate").val();
        var HrEvntEndDt = $(".staffDet .hrgEvntEndDate").val();
        // to Handle Hire Event Start Date
        if (HrEvntStrDt && HrEvntStrDt !== "") {
            var valuesDt = HrEvntStrDt.split("/");
            var dateobj = {
                "month" : valuesDt[0],
                "day" : valuesDt[1],
                "year" : valuesDt[2]
            };
            this.saveReqDtlsobj.stfHrgEvntStartDt = dateobj;

        }
        // to handle Hire Event End Date
        if (HrEvntEndDt && HrEvntEndDt !== "") {
            var EndDt = HrEvntEndDt.split("/");
            var dateobjEn = {
                "month" : EndDt[0],
                "day" : EndDt[1],
                "year" : EndDt[2]
            };
            this.saveReqDtlsobj.stfHrgEvntEndDt = dateobjEn;

        }
    };
    /*
     * This function sets Flags for passed properties
     *
     * @param className
     * @param propName
     * @return N/A
     */
    this.setIndivFlag = function(className, propName) {
        if ($(".staffDet ." + className + ":checked").val() === "Y") {
            this.saveReqDtlsobj[propName] = CONSTANTS.YES;
        } else {
            this.saveReqDtlsobj[propName] = CONSTANTS.NO;
        }
    };
    /*
     * This function sets Flags to save obj
     *
     * @param N/A
     * @return N/A
     */
    this.setSaveObjFlags = function() {
        this.setIndivFlag("hrgEvntFlg", "hrgEvntFlg");
        this.setIndivFlag("rscSchdFlgBtn", "rscToSchdFlg");
        this.setIndivFlag("rscToManageFlgBtn", "rscToManageFlg");
        this.setIndivFlag("sealTempJob", "sealTempJob");
    };
    /*
     * This function sets interview duration to save obj
     *
     * @param N/A
     * @return N/A
     */
    this.setSaveObjIntrDur = function() {
        if ($(".staffDet .hrgEvntFlg:checked").val() === "Y") {
            if (this.model.reqStaffDet > 0 && this.model.reqStaffDet[0].hireEvntType === "SDE") {
                // Old Hiring Event
                this.saveReqDtlsobj.interviewDurtn = $(".staffDet .interviewDurtnMin").val();
            } else {
                this.saveReqDtlsobj.interviewDurtn = $(".staffDet .interviewDurtn").val();
            }
        } else {
            if (parseInt($(".staffDet .interviewDurtn").val()) === 0) {
                this.saveReqDtlsobj.interviewDurtn = "";
            } else {
                this.saveReqDtlsobj.interviewDurtn = $(".staffDet .interviewDurtn").val();
            }
        }
    };
    /*
     * This function sets staffing details to save obj
     *
     * @param N/A
     * @return N/A
     */
    this.setSaveObjWithSaffingDet = function() {
        var qual = $(".staffDet .qualPoolNts").val().replace(this.regExprLt, "&lt;");
        qual = this.decodeSpecialCharacters(qual);
        var ref = $(".staffDet .Referals").val().replace(this.regExprLt, "&lt;");
        ref = this.decodeSpecialCharacters(ref);
        this.saveReqDtlsobj.qualPoolNts = qual;
        this.saveReqDtlsobj.Referals = ref;
        if (this.model.LoadREQDGDltls && this.model.LoadREQDGDltls.length > 0) {
            this.saveReqDtlsobj.requestNbr = this.model.LoadREQDGDltls[0].reqNbr; // requisition
            // number
        }
        if (parseInt($(".staffDet .hrgEvntState").val()) === -1) {
            this.saveReqDtlsobj.state = null;
        } else {
            this.saveReqDtlsobj.state = $(".staffDet .hrgEvntState option:selected").text();
        }
        var hrEvntLocation = $(".staffDet .hrgEvntLoc").val().replace(this.regExprLt, "&lt;");
        hrEvntLocation = this.decodeSpecialCharacters(hrEvntLocation);
        this.saveReqDtlsobj.stfhrgEvntLoc = hrEvntLocation;
        this.saveReqDtlsobj.stfReqNbr = $(".staffDet .requestNbrTxt").val(); // request
        // number
        this.saveReqDtlsobj.targetPay = $(".staffDet .targetPay").val();
        this.saveReqDtlsobj.zip = $(".staffDet .hrgEvntZip").val();
        this.saveReqDtlsobj.UpdIns = this.model.UpdIns;
        this.saveReqDtlsobj.reqCalId = $(".staffDet .calendarLst").val();
        if($(".staffDet .calendarLst").val() === null && $(".staffDet .calendarLst").val() !== $(".staffDet .calendarLst .selectboxit-text").attr("data-val")){
        	this.saveReqDtlsobj.reqCalId = $(".staffDet .calendarLst .selectboxit-text").attr("data-val");
        }
    };
    /*
     * This function sets old hiring details to save obj
     *
     * @param N/A
     * @return true/false
     */
    this.setSaveObjOldHrngEvntDet = function() {
        // Only for an Old Hire Event
        if (this.model.reqStaffDet.length > 0 && this.model.reqStaffDet[0].hireEvntType === "SDE") {
            // to handle Hire Event times
            if(this.validateHrngEvtTime()){
                return true;
            }

            var validatePhnNo = $(".staffDet .hrgEvntPhn").val().replace(/-/gi, '');
            if (this.validatePhnNumLength(validatePhnNo)) {
                this.saveReqDtlsobj.stfhrgEvntLocPhn = validatePhnNo;
            } else {
                this.showPopUp(CONSTANTS.HIRE_EVNT_PHN_NUMBER);
                return true;
            }
            this.HrEvntDates();

            var hrEvntAddress = $(".staffDet .hrgEvntAddr").val().replace(this.regExprLt, "&lt;");
            hrEvntAddress = this.decodeSpecialCharacters(hrEvntAddress);
            this.saveReqDtlsobj.add = hrEvntAddress;

            var hrEvntBreak = $(".staffDet .hrgEvntBrks").val().replace(this.regExprLt, "&lt;");
            hrEvntBreak = this.decodeSpecialCharacters(hrEvntBreak);
            this.saveReqDtlsobj.breaks = hrEvntBreak;
            this.saveReqDtlsobj.city = $(".staffDet .hrgEvntCity").val();
        }
        var validateHrPhnNo = $(".staffDet .hrgMgrPhnTxt").val().replace(/-/gi, '');
        if (this.validatePhnNumLength(validateHrPhnNo)) {
            this.saveReqDtlsobj.hrgMgrPhn = validateHrPhnNo;
        } else {
            this.showPopUp(CONSTANTS.HIRE_EVNT_MGR_NUMBER);
            return true;
        }
        return false;
    };
    /*
     * This function validates the phonenumber passed based on the length
     *
     * @param phnNo
     * @return true/falso
     */
    this.validatePhnNumLength = function(phnNo){
        return phnNo.toString().length === 0 || phnNo.toString().length === 10;
    };
    /*
     * This function sets old hiring details to save obj
     *
     * @param N/A
     * @return true/false
     */
    this.setSaveObjOtherDet = function() {
        var namesOfIntrwrs = $(".staffDet .daysTmMgrAvble").val().replace(this.regExprLt, "&lt;");
        namesOfIntrwrs = this.decodeSpecialCharacters(namesOfIntrwrs);
        this.saveReqDtlsobj.namesOfInterviewers = namesOfIntrwrs;
        this.saveReqDtlsobj.desiredExp = $(".staffDet .desiredExp").val();
        this.saveReqDtlsobj.interviewCandCnt = $(".staffDet .reqNumInterviews").val();
        this.saveReqDtlsobj.requisitionStatus = parseInt($(".staffDet .reqStatus").val());

        this.setSaveObjFlags();

        this.saveReqDtlsobj.hrgMgrName = $(".staffDet .hrgMgrNameTxt").val();
        this.saveReqDtlsobj.hrgMgrTtl = $(".staffDet .hrgMgrTtl").val();
        if (this.model.LoadREQDGDltls && this.model.LoadREQDGDltls.length > 0) {
            this.saveReqDtlsobj.jobTtl = this.model.LoadREQDGDltls[0].jobTtl;
        }
        this.setSaveObjIntrDur();

        this.setSaveObjWithSaffingDet();
    };
    /*
     * This function checks if the passed value is empty
     *
     * @param value
     * @return true/false
     */
    this.checkEmptyFieldValue = function(value) {
        return !value || $.trim(value).length <= 0;
    };
    /*
     * This function validates the passed parametres and shows popup
     *
     * @param hrgMgrName
     * @param hrgMgrPhn
     * @param reqNum
     * @return true/false
     */
    this.validateMgrNamePhnReqNum = function(hrgMgrName,hrgMgrPhn,reqNum) {
        // validating Hiring manager name
        if (this.checkEmptyFieldValue(hrgMgrName)) {
            this.showFocusPopUp(CONSTANTS.FILL_MANDATORY_FLDS_HRGMGRNAME);
            return true;
        }
        // validating Hiring manager Phone number
        if (this.checkEmptyFieldValue(hrgMgrPhn)) {
            this.showFocusPopUp(CONSTANTS.FILL_MANDATORY_FLDS_HRGMGRPHONE);
            return true;
        }
        // validating Que web number
        if (this.checkEmptyFieldValue(reqNum)) {
            this.showFocusPopUp(CONSTANTS.FILL_MANDATORY_FLDS_QUEWEBNUM);
            return true;
        }
        return false;
    };
    /*
     * This function validates the controls in staffing details form
     *
     * @param N/A
     * @return true/false
     */
    this.validateStaffingDetailsForm = function() {
        // Validate that a valid Target Pay has been entered. Should be blank or
        // < 99.99, 2 decimals.
        if (!this.formatTgtPay()) {
            this.showPopUp(CONSTANTS.TGT_PAY_VAL_MSG);
            return true;
        }
        // Checks to make sure that a Phone Screen has been added for all
        // Candidates
        this.checkPhoneScreenPerCand();
        if (this.validateIntvwDur()) {
            return true;
        }
        if (this.validateHiringEvent()) {
            return true;
        }
        if (this.validatePreATSHiringEvent()) {
            return true;
        }
        return false;
    };
    /*
     * This function validates the controls in whole page
     *
     * @param reqNum
     * @return true/false
     */
    this.validatePage = function(reqNum) {
        var hrgMgrName = $(".staffDet .hrgMgrNameTxt").val();
        var hrgMgrPhn = $(".staffDet .hrgMgrPhnTxt").val();
        // included for validating the mandatory fields
        if (this.checkEmptyFieldValue(hrgMgrName) && this.checkEmptyFieldValue(hrgMgrPhn) && this.checkEmptyFieldValue(reqNum)) {
            this.showFocusPopUp(CONSTANTS.FILL_MANDATORY_FLDS);
            return true;
        }
        if (this.validateMgrNamePhnReqNum(hrgMgrName,hrgMgrPhn,reqNum)) {
            return true;
        }
        if (this.validateStaffingDetailsForm()) {
            return true;
        }
        return false;
    };
    /*
     * This function validates the queweb number control
     *
     * @param reqNum
     * @return true/false
     */
    this.validateQueWebNum = function(reqNum) {
        reqNum = reqNum.toString();
        if (reqNum && $.trim(reqNum).length > 0) {
            //Removed per Kelly Snow Dec 2015
        	/*if (reqNum[0] !== "1") {
                this.showPopUp(CONSTANTS.INV_QUEWEBNO);
                return true;
            }*/
            // validation to check whether the queweb request number is of 8
            // digits
            if (reqNum.length !== 8) {
                this.showPopUp(CONSTANTS.QUEWEBNO_LEN);
                return true;
            }
        }
        return false;
    };
    /*
     * This function validates before submitting the page
     *
     * @param reqNum
     * @return true/false
     */
    this.validateBeforeSubmit = function(reqNum) {
        if (this.validateQueWebNum(reqNum)) {
            return true;
        }
        if (this.validatePage(reqNum)) {
            return true;
        }
        return false;
    };
    /*
     * This function generates JSON request for createphonescreen service
     *
     * @param model
     * @param reqNo
     * @param strNo
     * @param namesOfInterviewers
     * @param rscSchdFlg
     * @return retObj
     */
    this.createPhnScreenToJSON = function(model, reqNo, strNo, namesOfInterviewers, rscSchdFlg) {
        var retObj = {};
        retObj.CreatePhoneScreenRequest = {};
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail = {};
        retObj.CreatePhoneScreenRequest.CandidateList = [];
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail.overAllStatus = model.crtPhnScr[0].ovrAllSts;
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail.cndDeptNbr = model.LoadREQDGDltls[0].dept;
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail.jobTtl = model.LoadREQDGDltls[0].job;
        var candls = [];
        for (var i = 0; i < model.crtPhnScr.length; i++) {
            candls[i] = {};
            candls[i].ssnNbr = model.crtPhnScr[i].candNbr;
            candls[i].reqCalId = model.crtPhnScr[i].reqCalId;
            if (model.crtPhnScr[i].aid !== null) {
                candls[i].aid = model.crtPhnScr[i].aid;
            }
            if (model.crtPhnScr[i].name !== null) {
                candls[i].name = model.crtPhnScr[i].name;
            }
        }
        retObj.CreatePhoneScreenRequest.CandidateList[0] = {};
        if (candls.length > 1) {
            retObj.CreatePhoneScreenRequest.CandidateList[0].CandidateDetails = candls;
        } else if (candls.length === 1) {
            retObj.CreatePhoneScreenRequest.CandidateList[0].CandidateDetails = candls[0];
        }
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail.reqNbr = reqNo;
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail.cndStrNbr = strNo;
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail.namesOfInterviewers = namesOfInterviewers;
        retObj.CreatePhoneScreenRequest.PhoneScreenIntrwDetail.rscSchdFlg = rscSchdFlg;
        return retObj;
    };
    /*
     * This function performs save/submit action
     *
     * @param evntTyp
     * @return N/A
     */
    this.performAction = function(evntTyp) {
        if (evntTyp === CONSTANTS.evntTypReq) {
            if (this.model.isCandPhnScrnMissing) {
                this.popup
                        .warnMissingPhnSrc(CONSTANTS.STAFF_DET_MISSING_PHONE_SCRN_PART_1, CONSTANTS.STAFF_DET_MISSING_PHONE_SCRN_PART_2, this.clearScrAndSave.bind(this), this.handleClose.bind(this), 160, 475);
            } else {
                this.clearScrAndSaveSub();
            }
        } else if (evntTyp === CONSTANTS.evntTypReqSave) {
            $(".staffDet .hrgMgrPhnTxt").val(this.formatPhoneNum($(".staffDet .hrgMgrPhnTxt").val()));
            $(".staffDet .hrgEvntPhn").val(this.formatPhoneNum($(".staffDet .hrgEvntPhn").val()));
            // to Bring back the screen to top after saving the details
            $(".reqDetContainer").animate({
                scrollTop : 0
            }, "fast");
            this.model.saveReqBtnClickFlg = true;
            this.blockFullPage();
            this.model.crtPhnScrnBtnClickFlg = false;
            this.model.submitReqScrnBtnClickFlg = false;
            this.rsaServices.saveReqScrnDtls({data:JSON.stringify(this.saveReqScrenDataToJSON(this.saveReqDtlsobj))}, this.saveReqDtlsSuccess.bind(this), this.saveReqDtlsFail.bind(this));
            this.model.editStaffingDet = false;
        } else if (evntTyp === CONSTANTS.evntTyp) {
            this.model.saveReqBtnClickFlg = false;
            this.blockFullPage();
            // calling the create phn screen event
            this.model.submitReqScrnBtnClickFlg = false;
            var data = this
                    .createPhnScreenToJSON(this.model, this.model.LoadREQDGDltls[0].reqNbr, this.model.LoadREQDGDltls[0].store, this.saveReqDtlsobj.namesOfInterviewers, this.saveReqDtlsobj.rscToSchdFlg);
            this.rsaServices.createPhoneScr({data:JSON.stringify(data)}, this.createPhnScrSuccess.bind(this), this.createPhnScrFail.bind(this));
            this.model.editStaffingDet = false;
            this.model.isCandPhnScrnMissing = false;
        }
    };
    /*
     * This function performs some validations and then submits the page
     *
     * @param evntTyp
     * @return N/A
     */
    this.submitReqScrnDtls = function(evntTyp) {
        var reqNum = $(".staffDet .requestNbrTxt").val();
        if (this.validateBeforeSubmit(reqNum)) {
            return true;
        }
        // to validate if queweb request number is entered
        if (this.checkEmptyFieldValue(reqNum)) {
            this.showPopUp(CONSTANTS.REQ_STAFF_LBL_HDR);
            return;
        } else {
            this.saveReqDtlsobj = {};
            this.saveHrngEvtDataUpd();
            if (this.setSaveObjOldHrngEvntDet()) {
                return true;
            }
            this.setSaveObjOtherDet();
            this.performAction(evntTyp);
        }
    };
    /*
     * This function generates JSON time obj for save object
     *
     * @param propName
     * @param timeobj
     * @param root
     * @return root
     */
    this.setTimeForSaveObj = function(propName, timeobj, root) {
        root.UpdateStaffingRequest.staffingDetails[propName]={};
        if (timeobj) {
            root = this.setDateForSaveObj(propName, timeobj, root);
            root.UpdateStaffingRequest.staffingDetails[propName].hour = timeobj.hour;
            root.UpdateStaffingRequest.staffingDetails[propName].second = CONSTANTS.APPEND;
            root.UpdateStaffingRequest.staffingDetails[propName].minute = timeobj.minute;
            root.UpdateStaffingRequest.staffingDetails[propName].milliSecond = CONSTANTS.APPEND;
        }
        return root;
    };
    /*
     * This function generates JSON date obj for save object
     *
     * @param propName
     * @param dateobj
     * @param root
     * @return root
     */
    this.setDateForSaveObj = function(propName, dateobj, root) {
        root.UpdateStaffingRequest.staffingDetails[propName]={};
        if (dateobj) {
            root.UpdateStaffingRequest.staffingDetails[propName] = {};
            root.UpdateStaffingRequest.staffingDetails[propName].month = dateobj.month;
            root.UpdateStaffingRequest.staffingDetails[propName].day = dateobj.day;
            root.UpdateStaffingRequest.staffingDetails[propName].year = dateobj.year;
        }
        return root;
    };
    /*
     * This function returns null if passed value is undefined
     *
     * @param val
     * @return val/null
     */
    this.setNullIfUnDef = function(val) {
        if (val) {
            return val;
        } else {
            return null;
        }
    };
    /*
     * This function generates JSON request for savereqscrdetails service
     *
     * @param saveObj
     * @return root
     */
    this.saveReqScrenDataToJSON = function(saveObj) {
        var root = {};
        root.UpdateStaffingRequest = {};
        root.UpdateStaffingRequest.staffingDetails = {};
        root = this.setTimeForSaveObj("startTime", saveObj.startTime, root);
        root = this.setTimeForSaveObj("endTime", saveObj.endTime, root);
        root = this.setTimeForSaveObj("lastIntrTm", saveObj.lastIntrTm, root);
        root = this.setTimeForSaveObj("lunch", saveObj.lunch, root);
        root = this.setDateForSaveObj("stfHrgEvntStartDt", saveObj.stfHrgEvntStartDt, root);
        root = this.setDateForSaveObj("stfHrgEvntEndDt", saveObj.stfHrgEvntEndDt, root);
        root = this.setDateForSaveObj("weekBeginDt", saveObj.weekBeginDt, root);
        root.UpdateStaffingRequest.staffingDetails.hireEvntType = this.setNullIfUnDef(saveObj.hireEventType);
        root.UpdateStaffingRequest.staffingDetails.insertDeleteUpdateHrHireEvntReqnTable = this.setNullIfUnDef(saveObj.insertDeleteUpdateHrHireEvntReqnTable);
        root.UpdateStaffingRequest.staffingDetails.namesOfInterviewers = this.setNullIfUnDef(saveObj.namesOfInterviewers);
        root.UpdateStaffingRequest.staffingDetails.jobTitle = this.setNullIfUnDef(saveObj.hrgMgrTtl);
        root.UpdateStaffingRequest.staffingDetails.hrgMgrName = this.setNullIfUnDef(saveObj.hrgMgrName);
        root.UpdateStaffingRequest.staffingDetails.hrgMgrTtl = this.setNullIfUnDef(saveObj.hrgMgrTtl);
        root.UpdateStaffingRequest.staffingDetails.targetPay = this.setNullIfUnDef(saveObj.targetPay);
        root.UpdateStaffingRequest.staffingDetails.desiredExp = this.setNullIfUnDef(saveObj.desiredExp);
        root.UpdateStaffingRequest.staffingDetails.requestNbr = this.setNullIfUnDef(saveObj.stfReqNbr);
        root.UpdateStaffingRequest.staffingDetails.hrgEvntFlg = this.setNullIfUnDef(saveObj.hrgEvntFlg);
        root.UpdateStaffingRequest.staffingDetails.rscSchdFlg = this.setNullIfUnDef(saveObj.rscToSchdFlg);
        root.UpdateStaffingRequest.staffingDetails.rscManageFlg = this.setNullIfUnDef(saveObj.rscToManageFlg);
        root.UpdateStaffingRequest.staffingDetails.sealTempJob = this.setNullIfUnDef(saveObj.sealTempJob);
        root.UpdateStaffingRequest.staffingDetails.stfhrgEvntLoc = this.setNullIfUnDef(saveObj.stfhrgEvntLoc);
        root.UpdateStaffingRequest.staffingDetails.stfhrgEvntLocPhn = this.setNullIfUnDef(saveObj.stfhrgEvntLocPhn);
        root.UpdateStaffingRequest.staffingDetails.add = this.setNullIfUnDef(saveObj.add);
        root.UpdateStaffingRequest.staffingDetails.city = this.setNullIfUnDef(saveObj.city);
        root.UpdateStaffingRequest.staffingDetails.zip = this.setNullIfUnDef(saveObj.zip);
        if (saveObj.state && saveObj.state !== "") {
            root.UpdateStaffingRequest.staffingDetails.state = saveObj.state;
        }
        
        if (saveObj.Referals && saveObj.Referals !== "") {
            root.UpdateStaffingRequest.staffingDetails.Referals = saveObj.Referals;
        }
        root.UpdateStaffingRequest.staffingDetails.interviewDurtn = this.setNullIfUnDef(saveObj.interviewDurtn);
        root.UpdateStaffingRequest.staffingDetails.qualPoolNts = this.setNullIfUnDef(saveObj.qualPoolNts);
        root.UpdateStaffingRequest.staffingDetails.hiringEventID = this.setNullIfUnDef(saveObj.hrEvntId);
        root.UpdateStaffingRequest.staffingDetails.breaks = this.setNullIfUnDef(saveObj.breaks);
        root.UpdateStaffingRequest.staffingDetails.weekMgrAvble = this.setNullIfUnDef(saveObj.daysTmMgrAvble);
        root.UpdateStaffingRequest.staffingDetails.hrgMgrPhn = this.setNullIfUnDef(saveObj.hrgMgrPhn);
        root.UpdateStaffingRequest.staffingDetails.reqStatus = this.setNullIfUnDef(saveObj.requisitionStatus);
        root.UpdateStaffingRequest.staffingDetails.requestedNbrIntvws = this.setNullIfUnDef(saveObj.interviewCandCnt);
        root.UpdateStaffingRequest.reqCalId = this.setNullIfUnDef(saveObj.reqCalId);
        root.UpdateStaffingRequest.reqNbr = this.setNullIfUnDef(saveObj.requestNbr);
        root.UpdateStaffingRequest.insertUpdate = this.setNullIfUnDef(saveObj.UpdIns);
        return root;
    };
    /*
     * This function is a success handler of create phone screen service
     *
     * @param response
     * @return N/A
     */
    this.createPhnScrSuccess = function(response) {
        this.unblockFullPage();
        response = response.Response;
        this.model = _.extend(this.model, new retailStaffingCreatePhnScr(response));
        if ("status" in response && response.status === CONSTANTS.SUCCESS) {
            this.displayPhoneScreenGrid(this.model.createphnScrDtls);
            // save service call
            this.rsaServices.saveReqScrnDtls({data:JSON.stringify(this.saveReqScrenDataToJSON(this.saveReqDtlsobj))}, this.saveReqDtlsSuccess.bind(this), this.saveReqDtlsFail.bind(this));
        } else if ("error" in response && "errorMsg" in response.error) {
            this.popup.alert(response.error.errorMsg);
        }

    };
    /*
     * This function is a failure handler of create phone screen service
     *
     * @param N/A
     * @return N/A
     */
    this.createPhnScrFail = function() {
        this.unblockFullPage();
    };
    /*
     * This function is a success handler of save req screen details service
     *
     * @param response
     * @return N/A
     */
    this.saveReqDtlsSuccess = function(response) {
        this.unblockFullPage();
        response = response.Response;
        if (response.status && response.status === CONSTANTS.SUCCESS) {
            if (this.model.crtPhnScrnBtnClickFlg) {

                this.popup.alert(CONSTANTS.crtPhnStaffDtlUpdMsg);

            } else if (this.model.saveReqBtnClickFlg) {
                if (this.model.isCandPhnScrnMissing) {
                    this.popup.alert("Details Saved Successfully. Warning - Phone Screens have not been created for all candidates.");
                } else {
                    this.popup.alert("Details Saved Successfully");
                }
            } else if (this.model.submitReqScrnBtnClickFlg) {
                CONSTANTS.SUCCESS_ON_HOME = true;
                this.navigateToSearchScreen();
                $("#messageBar").text(CONSTANTS.SEARCH_INFO_MSG_LBL);
                $("#goHome.reqnavHeaderButtons").show();
            }
        }
        else if (this.checkForErrorInResponse(response)) {
            this.popup.alert(response.error.errorMsg);
        }
    };
    /*
     * This function returns true if there is error in response
     *
     * @param response
     * @return true/false
     */
    this.checkForErrorInResponse = function(response){
        return "error" in response && "errorMsg" in response.error;
    };
    /*
     * This function is a failure handler of create phone screen service
     *
     * @param N/A
     * @return N/A
     */
    this.saveReqDtlsFail = function() {
        this.unblockFullPage();
    };
    /*
     * This function formats the value entered in the target pay text box to 2 decimal precision
     *
     * @param N/A
     * @return true/false
     */
    this.formatTgtPay = function() {
        var pay = $(".staffDet .targetPay").val();
        if(!$(".staffDet .targetPay").validateNumber()){
            $('[data-toggle="tooltip"]').tooltip("destroy");
            $('[data-toggle="tooltip"]').tooltip();
            return false;
        }
        else{
            $('[data-toggle="tooltip"]').tooltip("destroy");
        }
        if(pay.trim() === ""){
            $(".staffDet .targetPay").val(pay.trim());
            return true;
        }
        var value = parseFloat(pay).toFixed(2);
        if (value !== NaN && value <= 99.99) {
            $(".staffDet .targetPay").val(value);
            return true;
        }
        return false;
    };
    /*
     * This function decodes the special characters in the passed text
     *
     * @param textToFormat
     * @return textToFormat
     */
    this.decodeSpecialCharacters = function(textToFormat) {
        var AddInt;
        var part1Add;
        var part2Add;
        for (var hrAdd = 0; hrAdd < textToFormat.length; hrAdd++) {
            AddInt = textToFormat.charCodeAt(hrAdd);
            if (!(AddInt >= 32 && AddInt <= 125)) {
                part1Add = textToFormat.substr(0, hrAdd);
                part2Add = textToFormat.substr(hrAdd + 1, textToFormat.length - 1);
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
     * This function resets all the control in the page and saves
     *
     * @param N/A
     * @return N/A
     */
    this.clearScrAndSave = function() {
        if ($('#genericPopup').hasClass('in')) {
            $("#genericPopup").modal("hide");
        }
        this.clearScrAndSaveSub();
    };
    /*
     * This function resets all the control in the page and saves
     *
     * @param N/A
     * @return N/A
     */
    this.clearScrAndSaveSub = function() {
        $(".staffDet .cls").val("");
        $(".staffDet .interviewDurtn").val(0);
        $(".staffDet .interviewDurtn").data("selectBox-selectBoxIt").refresh();
        $(".staffDet .hrgEvntState").val(-1);
        $(".staffDet .hrgEvntState").data("selectBox-selectBoxIt").refresh();
        this.blockFullPage();
        this.model.crtPhnScrnBtnClickFlg = false;
        this.model.saveReqBtnClickFlg = false;
        this.model.submitReqScrnBtnClickFlg = true;
        this.model.editStaffingDet = false;
        this.rsaServices.saveReqScrnDtls({data:JSON.stringify(this.saveReqScrenDataToJSON(this.saveReqDtlsobj))}, this.saveReqDtlsSuccess.bind(this), this.saveReqDtlsFail.bind(this));
    };
    /*
     * This function shows popup with a passed message
     *
     * @param iMessage
     * @return N/A
     */
    this.showPopUp = function(iMessage) {
        this.popup.commonPopup(iMessage, this.handleClose.bind(this), 120, 450);
    };
    /*
     * This function shows popup with a passed message
     *
     * @param iMessage
     * @return N/A
     */
    this.showFocusPopUp = function(iMessage) {
        this.popup.commonPopup(iMessage, this.handleCloseEvnt.bind(this), 120, 450);
    };
    /*
     * This function handles event when cancel is clicked
     *
     * @param N/A
     * @return N/A
     */
    this.cancelReq = function() {
        // Check that all candidates have a Phone Screen
        this.checkPhoneScreenPerCand();

        // Check if screen has been edited and there are not any missing phone
        // screens
        if (this.model.editStaffingDet && !this.model.isCandPhnScrnMissing) {
            this.popup.warnUnsavedData(CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_1, CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_2, this.goToHome.bind(this), this.handleClose.bind(this), 150, 400);

        }
        // Check if screen has been edited and there are missing phone screens
        else if (this.model.editStaffingDet && this.model.isCandPhnScrnMissing) {
            this.popup.warnUnsavedData(CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_1, CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_2, this.openPopUpMisPhnSrn.bind(this), this.handleClose.bind(this), 150, 400);

        }
        // Check if screen has not been edited and there are missing phone
        // screens
        else if (!this.model.editStaffingDet && this.model.isCandPhnScrnMissing) {
            this.openPopUpMisPhnSrn();
        } else {
            this.goToHome();
        }
    };
    /*
     * This function shows missing phone screen popup
     *
     * @param N/A
     * @return N/A
     */
    this.openPopUpMisPhnSrn = function() {
        if ($('#genericPopup').hasClass('in')) {
            $("#genericPopup").modal("hide");
        }
        this.popup.warnMissingPhnSrc(CONSTANTS.STAFF_DET_MISSING_PHONE_SCRN_PART_1, CONSTANTS.STAFF_DET_MISSING_PHONE_SCRN_PART_2, this.goToHome.bind(this), this.handleClose.bind(this), 160, 475);

    };
    /*
     * This function closes the current popup
     *
     * @param N/A
     * @return N/A
     */
    this.handleClose = function() {
        $("#genericPopup").modal("hide");
    };
    /*
     * This function closes the current popup and brings focus to the desired control
     *
     * @param N/A
     * @return N/A
     */
    this.handleCloseEvnt = function() {
        $("#genericPopup").modal("hide");
        var val;
        val = $(".staffDet .hrgMgrNameTxt").val();
        if (!val || $.trim(val).length <= 0) {
            $(".staffDet .hrgMgrNameTxt").focus();
            $( ".staffDet .hrgMgrNameTxt" ).select();
        }
        val = $(".staffDet .hrgMgrPhnTxt").val();
        if (!val || $.trim(val).length <= 0) {
            $(".staffDet .hrgMgrPhnTxt").focus();
            $( ".staffDet .hrgMgrPhnTxt" ).select();
        }
        val = $(".staffDet .requestNbrTxt").val();
        if (!val || $.trim(val).length <= 0) {
            $(".staffDet .requestNbrTxt").focus();
            $( ".staffDet .requestNbrTxt" ).select();
        }
    };
    /*
     * This function navigates to home screen
     *
     * @param N/A
     * @return N/A
     */
    this.goToHome = function() {
        if ($('#genericPopup').hasClass('in')) {
            $("#genericPopup").modal("hide");
        }
        $(".staffDet .cls").val("");
        $(".staffDet .interviewDurtn").val(0);
        $(".staffDet .hrgEvntState").val(-1);
        // there are some times when these slect box fails the DOM loading itself.
        try {
        	 $(".staffDet .interviewDurtn").data("selectBox-selectBoxIt").refresh();
        	 $(".staffDet .hrgEvntState").data("selectBox-selectBoxIt").refresh();
        }
        catch(ex) {
        	console.log("Select box error.");
        }
        
        UTILITY.RemoveQS();
        this.navigateToSearchScreen();
        $("#messageBar").text(CONSTANTS.SEARCH_INFO_MSG_LBL);
        $("#goHome.reqnavHeaderButtons").hide();

        this.model.LoadPanelTitle = "";

        this.model.LoadITIDGDltls = [];
        this.model.LoadREQDGDltls = [];
    };
    /*
     * This function navigates to home screen
     *
     * @param N/A
     * @return N/A
     */
    this.navigateToSearchScreen = function() {
    	UTILITY.RemoveQS();
    	UTILITY.clearCache();
        $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.retailStaffingObj.setContent);
    };
    
    
    
    /*
     * This function navigates to applicant profile screen
     *
     * @param N/A
     * @return N/A
     */
    this.navigateToApplProfile = function() {
        CONSTANTS.QP_SELECTED_TAB = $("#qpTabs").tabs('option', 'active');
        CONSTANTS.cacheRequisitionDetailsFlg = true;       
        // set the current QP page
        CONSTANTS.cacheCurrentAPpage = this.qpCurrentAPpage;
        CONSTANTS.cacheCurrentASpage = this.qpCurrentASpage;
        $.get('app/RSApplicantProfile/view/applicantProfile.html', CONSTANTS.retailStaffingObj.setContent);
    };
    /*
     * This function filters the candidates without the phone screen details
     *
     * @param oCrtPhnScr
     * @param canID
     * @param ovrAll
     * @param calendarId
     * @return N/A
     */
    this.getCandWithoutPhnScr = function(oCrtPhnScr, canID, ovrAll, calendarId) {
        var flag;
        // to compare list of phone screen details with candidate screen details
        for (var i = 0; i < this.model.CndtDltls.length; i++) {
            flag = 0;
            for (var j = 0; j < this.model.createphnScrDtls.length; j++) {
                if (this.model.CndtDltls[i].ssnNbr === this.model.createphnScrDtls[j].cndtNbr) {
                    flag = 1;
                }
            }
            if (flag === 0) {
                canID[i] = this.model.CndtDltls[i].ssnNbr;
                oCrtPhnScr = {
                    "candNbr" : this.model.CndtDltls[i].ssnNbr,
                    "aid" : this.model.CndtDltls[i].aid,
                    "name" : this.model.CndtDltls[i].name,
                    "ovrAllSts" : ovrAll,
                    "reqCalId" : calendarId
                };
                this.model.crtPhnScr.push(oCrtPhnScr);
            }
        }
        this.model.candNewPhn = canID;
    };
    /*
     * This function validates details and creates phone screen
     *
     * @param N/A
     * @return N/A
     */
    this.checkAndCreatePhnScreen = function() {
        // To dispatch the event to create phone screen
        if (this.model.candNewPhn && this.model.candNewPhn.length > 0) {
            if (this.model.setAutoHoldFlg === CONSTANTS.ONE) {
                this.blockFullPage();
                this.popup
                        .autoholdWarn(CONSTANTS.SOFT_AUTO_HOLD_MSG_PART1, CONSTANTS.SOFT_AUTO_HOLD_MSG_PART2, CONSTANTS.SOFT_AUTO_HOLD_MSG_PART3 + "%_?" + CONSTANTS.SOFT_AUTO_HOLD_MSG_PART4, this.handleAutoHoldOk
                                .bind(this), this.handleAutoHoldClose.bind(this), 180, 670);
                this.unblockFullPage();
            } else {
                this.submitReqScrnDtls(CONSTANTS.evntTyp);
            }
        } else {
            this.showPopUp(CONSTANTS.PHN_SCR_AVL);
            return;
        }
    };
    /*
     * This function validates details and creates phone screen
     *
     * @param N/A
     * @return N/A
     */
    this.createPhoneScreen = function() {
        this.model.candNewPhn = null;
        this.model.crtPhnScr = [];
        if (this.model.CndtDltls && this.model.CndtDltls.length > 0) {
            var oCrtPhnScr = null;
            var canID = [];
            var ovrAll = CONSTANTS.DEFAULT_STATUS;
            var calendarId = $(".staffDet .calendarLst").val();
            if($(".staffDet .calendarLst").val() === null && $(".staffDet .calendarLst").val() !== $(".staffDet .calendarLst .selectboxit-text").attr("data-val")){
            	calendarId = $(".staffDet .calendarLst .selectboxit-text").attr("data-val");
            }
            // to check overall status type
            if (this.model.LoadREQDGDltls[0].scrTyp === CONSTANTS.SCREEN_TYPE_DET) {
                ovrAll = CONSTANTS.STATUS_ON_DETAIL;
            }
            // to find if phone scrren details are already available
            if (this.model.createphnScrDtls && this.model.createphnScrDtls.length > 0) {
                // to check if candidate details are already available
                if (this.model.CndtDltls.length > this.model.createphnScrDtls.length) {
                    this.getCandWithoutPhnScr(oCrtPhnScr, canID, ovrAll, calendarId);
                }
            } else {
                // if phone screen is epmty,move all candidate to create phone
                // screen
                for (var k = 0; k < this.model.CndtDltls.length; k++) {
                    canID[k] = this.model.CndtDltls[k].ssnNbr;

                    oCrtPhnScr = {
                        "candNbr" : this.model.CndtDltls[k].ssnNbr,
                        "aid" : this.model.CndtDltls[k].aid,
                        "name" : this.model.CndtDltls[k].name,
                        "ovrAllSts" : ovrAll,
                        "reqCalId" : calendarId
                    };
                    this.model.crtPhnScr.push(oCrtPhnScr);
                }
                this.model.candNewPhn = canID;
            }
        }
        this.checkAndCreatePhnScreen();
    };
    /*
     * This function closes the popup and calls submit function
     *
     * @param N/A
     * @return N/A
     */
    this.handleAutoHoldClose = function() {
        $("#genericPopup").modal("hide");
        this.submitReqScrnDtls(CONSTANTS.evntTyp);
    };
    /*
     * This function closes the popup
     *
     * @param N/A
     * @return N/A
     */
    this.handleAutoHoldOk = function() {
        $("#genericPopup").modal("hide");
    };
    /*
     * This function displays hiring event details
     *
     * @param N/A
     * @return N/A
     */
    this.displayHiringEventDetails = function() {
        $(".staffDet .newHrngDetForm .hrgEvntStartDateNew").text(this.model.hiringEventDetails.hireEventBeginDate);
        $(".staffDet .newHrngDetForm .hrgEvntEndDateNew").text(this.model.hiringEventDetails.hireEventEndDate);
        $(".staffDet .newHrngDetForm .eventTypeText").text(this.model.hiringEventDetails.eventTypeText);
        $(".staffDet .newHrngDetForm .eventMgrName").text(this.model.hiringEventDetails.name);
        $(".staffDet .newHrngDetForm .eventMgrTitle").text(this.model.hiringEventDetails.title);
        $(".staffDet .newHrngDetForm .eventMgrPhone").text(this.formatPhoneNum(this.model.hiringEventDetails.phone));
        $(".staffDet .newHrngDetForm .eventMgrEmail").text(this.model.hiringEventDetails.email);
    };
    /*
     * This function blocks the container initially while making service call
     *
     * @param N/A
     * @return N/A
     */
    this.initialBlockApplication = function() {
        $(".reqDetContainer").block({
            message : null,
            overlayCSS : {
                backgroundColor : "#FFF",
                opacity : 1,
                cursor : "auto"
            },
            ignoreIfBlocked : false
        });
    };
    /*
     * This function unblocks the container after service call
     *
     * @param N/A
     * @return N/A
     */
    this.unblockApplication = function() {
        $(".reqDetContainer").unblock();
    };
    /*
     * This function changes the cursor to auto
     *
     * @param N/A
     * @return N/A
     */
    this.showDefaultCursor = function() {
        $('html, body').css("cursor", "auto");
    };
    /*
     * This function changes the cursor to wait cursor
     *
     * @param N/A
     * @return N/A
     */
    this.showWaitCursor = function() {
        $('html, body').css("cursor", "wait");
    };
    /*
     * This function blocks the full page
     *
     * @param N/A
     * @return N/A
     */
    this.blockFullPage = function() {
        $("body").block({
            message : null,
            overlayCSS : {
                backgroundColor : "rgb(139,139,139)",
                opacity : 0.6,
                cursor : "wait"
            },
            ignoreIfBlocked : false
        });
    };
    /*
     * This function unblocks the full page
     *
     * @param N/A
     * @return N/A
     */
    this.unblockFullPage = function() {
        $("body").unblock();
    };
    /*
     * This function displays the prev page applicants in the applicant grid
     *
     * @param N/A
     * @return N/A
     */
    this.selectPreviousPageApplicants = function() {
        var startIndex;
        var endIndex;
        startIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage - 1);
        if (((CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage - 1)) + CONSTANTS.APPLICANTS_PER_PAGE) >= this.model.LoadMasterApplicantPoolDgDtls.length) {
            endIndex = this.model.LoadMasterApplicantPoolDgDtls.length;
        } else {
            endIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage);
        }

        for (var i = startIndex; i < endIndex; i++) {

            this.model.LoadMasterApplicantPoolDgDtls[i].consideredFlg = "Y";
        }
               
        this.model._currentApplicantPage--;

        if (this.model._currentApplicantPage === 0) {
            this.popup.alert("Beginning of Applicant List has been reached.");
            this.model._currentApplicantPage = 1;
        } else {
        	this.qpCurrentAPpage--;
            this.model.LoadApplicantPoolDgDtls = [];
            startIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage - 1);
            endIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage);

            for (var index = startIndex; index < endIndex; index++) {
                this.model.LoadApplicantPoolDgDtls.push(this.model.LoadMasterApplicantPoolDgDtls[index]);
            }

            if ((this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE) <= this.model.LoadMasterApplicantPoolDgDtls.length) {
                this.model._currentApplicantGroupStartValue = startIndex + 1;
                this.model._currentApplicantGroupEndValue = (this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE);
            } else {
                this.model._currentApplicantGroupStartValue = startIndex + 1;
                this.model._currentApplicantGroupEndValue = this.model.LoadMasterApplicantPoolDgDtls.length;
            }
            this.model._currentApplicantGroupTotalValue = this.model.LoadMasterApplicantPoolDgDtls.length;
            this.createApplGrid(this.model.LoadApplicantPoolDgDtls);
        }
    };
    /*
     * This function checks and marks applicants as considered
     * this is called every time the next button is clicked.
     * @param consideredList
     * @param consideredList2
     * @return N/A
     */
    this.checkAndMarkApplAsConsd = function(consideredList, consideredList2) {    	
        var ApplicantsConsideredInQPRequest = {};
        var viewedInQPVO = {};
        var indexer = 0;
        var input = {};
        if (this.model._currentApplicantPage > this.model._applicantPagesAlreadyUpdated) {
            ApplicantsConsideredInQPRequest.applicantList = {};
            ApplicantsConsideredInQPRequest.applicantList.applicant = [];

            for (indexer = 0; indexer < consideredList.length; indexer++) {
                viewedInQPVO = consideredList[indexer];
                ApplicantsConsideredInQPRequest.applicantList.applicant.push({"applicantId":viewedInQPVO.id,"reqNbr":viewedInQPVO.reqNbr,"consideredCode":viewedInQPVO.consideredCode});
            }
            input.ApplicantsConsideredInQPRequest = ApplicantsConsideredInQPRequest;
            this.rsaServices
                    .markApplicantsAsConsideredInQPEvent({data:JSON.stringify(input)}, this.markApplicantsAsConsideredInQPEventSuccess.bind(this), this.markApplicantsAsConsideredInQPEventFailure
                            .bind(this));
            this.model._applicantPagesAlreadyUpdated++;
        } else if (this.model._currentApplicantPage <= this.model._applicantPagesAlreadyUpdated && consideredList2.length > 0) {

            ApplicantsConsideredInQPRequest.applicantList = {};
            ApplicantsConsideredInQPRequest.applicantList.applicant = [];

            for (var k = 0; k < consideredList2.length; k++) {
                viewedInQPVO = consideredList2[k];
                ApplicantsConsideredInQPRequest.applicantList.applicant.push({"applicantId":viewedInQPVO.id,"reqNbr":viewedInQPVO.reqNbr,"consideredCode":viewedInQPVO.consideredCode});
            }
            input.ApplicantsConsideredInQPRequest = ApplicantsConsideredInQPRequest;
            this.rsaServices
                    .markApplicantsAsConsideredInQPEvent({data:JSON.stringify(input)}, this.markApplicantsAsConsideredInQPEventSuccess.bind(this), this.markApplicantsAsConsideredInQPEventFailure
                            .bind(this));
        }
    };
    /*
     * This function displays the next page applicants in the applicant grid
     *
     * @param N/A
     * @return N/A
     */
    this.selectNextPageApplicants = function() {
        if (this.model._currentApplicantPage === Math.ceil(this.model.LoadMasterApplicantPoolDgDtls.length / CONSTANTS.APPLICANTS_PER_PAGE)) {
            this.popup.alert("End of Applicant List has been reached.");
        } else {
            this.model.LoadApplicantPoolDgDtls = [];

            var consideredInQPVO = {};
            var consideredList = [];
            var consideredList2 = [];

            var startIndex = 0;
            var endIndex = 0;
            this.qpCurrentAPpage++;
            startIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage - 1);
            if (CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage - 1) >= this.model.LoadMasterApplicantPoolDgDtls.length) {
                endIndex = this.model.LoadMasterApplicantPoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage);
            }
            for (var i = startIndex; i < endIndex; i++) {

                this.model.LoadMasterApplicantPoolDgDtls[i].consideredFlg = "Y";
            }

            startIndex = CONSTANTS.APPLICANTS_PER_PAGE * this.model._currentApplicantPage;
            if (CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage + 1) >= this.model.LoadMasterApplicantPoolDgDtls.length) {
                endIndex = this.model.LoadMasterApplicantPoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage + 1);
            }

            this.model._currentApplicantPage++;
            for (var j = startIndex; j < endIndex; j++) {
                var tempVo = {};
                consideredInQPVO = {};
                tempVo = this.model.LoadMasterApplicantPoolDgDtls[j];
                if (tempVo.consideredFlg !== "Y" && this.model._currentApplicantPage <= this.model._applicantPagesAlreadyUpdated) {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList2.push(consideredInQPVO);
                } else {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList.push(consideredInQPVO);
                }
                this.model.LoadApplicantPoolDgDtls.push(tempVo);
            }
            this.checkAndMarkApplAsConsd(consideredList, consideredList2);
            if ((this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE) <= this.model.LoadMasterApplicantPoolDgDtls.length) {
                this.model._currentApplicantGroupStartValue = startIndex + 1;
                this.model._currentApplicantGroupEndValue = (this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE);
            } else {
                this.model._currentApplicantGroupStartValue = startIndex + 1;
                this.model._currentApplicantGroupEndValue = this.model.LoadMasterApplicantPoolDgDtls.length;
            }
            this.model._currentApplicantGroupTotalValue = this.model.LoadMasterApplicantPoolDgDtls.length;
            this.createApplGrid(this.model.LoadApplicantPoolDgDtls);
        }
    };
    /*
     * This function displays the prev page associates in the associates grid
     *
     * @param N/A
     * @return N/A
     */
    this.selectPreviousPageAssociates = function() {
        var startIndex;
        var endIndex;

        startIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage - 1);
        if (((CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage - 1)) + CONSTANTS.ASSOCIATES_PER_PAGE) >= this.model.LoadMasterAssociatePoolDgDtls.length) {
            endIndex = this.model.LoadMasterAssociatePoolDgDtls.length;
        } else {
            endIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage);
        }

        for (var i = startIndex; i < endIndex; i++) {
            this.model.LoadMasterAssociatePoolDgDtls[i].assocConsideredFlg = "Y";
        }

        this.model._currentAssociatePage--;

        if (this.model._currentAssociatePage === 0) {
            this.popup.alert("Beginning of Associate List has been reached.");
            this.model._currentAssociatePage = 1;
        } else {
        	this.qpCurrentASpage--;
            this.model.LoadAssociatePoolDgDtls = [];
            startIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage - 1);
            endIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage);

            for (var j = startIndex; j < endIndex; j++) {
                this.model.LoadAssociatePoolDgDtls.push(this.model.LoadMasterAssociatePoolDgDtls[j]);
            }

            if ((this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE) <= this.model.LoadMasterAssociatePoolDgDtls.length) {
                this.model._currentAssociateGroupStartValue = startIndex + 1;
                this.model._currentAssociateGroupEndValue = (this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE);
            } else {
                this.model._currentAssociateGroupStartValue = startIndex + 1;
                this.model._currentAssociateGroupEndValue = this.model.LoadMasterAssociatePoolDgDtls.length;
            }
            this.model._currentAssociateGroupTotalValue = this.model.LoadMasterAssociatePoolDgDtls.length;
            this.createAssocGrid(this.model.LoadAssociatePoolDgDtls);
        }
    };
    /*
     * This function checks and marks associates as considered
     * @param consideredList
     * @param consideredList2
     * @return N/A
     */
    this.checkAndMarkAssocAsConsd = function(consideredList, consideredList2) {    	   	
        var ApplicantsConsideredInQPRequest = {};
        var viewedInQPVO = {};
        var input = {};
        if (this.model._currentAssociatePage > this.model._associatePagesAlreadyUpdated) {

            ApplicantsConsideredInQPRequest.applicantList = {};
            ApplicantsConsideredInQPRequest.applicantList.applicant = [];

            for (var k = 0; k < consideredList.length; k++) {
                viewedInQPVO = consideredList[k];
                ApplicantsConsideredInQPRequest.applicantList.applicant.push({"applicantId":viewedInQPVO.id,"reqNbr":viewedInQPVO.reqNbr,"consideredCode":viewedInQPVO.consideredCode});
            }

            input.ApplicantsConsideredInQPRequest = ApplicantsConsideredInQPRequest;
            this.rsaServices
                    .markApplicantsAsConsideredInQPEvent({data:JSON.stringify(input)}, this.markApplicantsAsConsideredInQPEventSuccess.bind(this), this.markApplicantsAsConsideredInQPEventFailure
                            .bind(this));
            this.model._associatePagesAlreadyUpdated++;
        } else if (this.model._currentAssociatePage <= this.model._associatePagesAlreadyUpdated && consideredList2.length > 0) {
            ApplicantsConsideredInQPRequest.applicantList = {};
            ApplicantsConsideredInQPRequest.applicantList.applicant = [];

            for (var m = 0; m < consideredList2.length; m++) {
                viewedInQPVO = consideredList2[m];
                ApplicantsConsideredInQPRequest.applicantList.applicant.push({"applicantId":viewedInQPVO.id,"reqNbr":viewedInQPVO.reqNbr,"consideredCode":viewedInQPVO.consideredCode});
            }
            input.ApplicantsConsideredInQPRequest = ApplicantsConsideredInQPRequest;
            this.rsaServices
                    .markApplicantsAsConsideredInQPEvent({data:JSON.stringify(input)}, this.markApplicantsAsConsideredInQPEventSuccess.bind(this), this.markApplicantsAsConsideredInQPEventFailure
                            .bind(this));
        }
    };
    /*
     * This function displays the next page associates in the associates grid
     *
     * @param N/A
     * @return N/A
     */
    this.selectNextPageAssociates = function() {
        if (this.model._currentAssociatePage === Math.ceil(this.model.LoadMasterAssociatePoolDgDtls.length / CONSTANTS.ASSOCIATES_PER_PAGE)) {
            this.popup.alert("End of Associate List has been reached.");
        } else {
            this.model.LoadAssociatePoolDgDtls = [];

            var consideredInQPVO = {};
            var consideredList = [];
            var consideredList2 = [];

            var startIndex = 0;
            var endIndex = 0;
            this.qpCurrentASpage++;
            startIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage - 1);
            if (CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage - 1) >= this.model.LoadMasterAssociatePoolDgDtls.length) {
                endIndex = this.model.LoadMasterAssociatePoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage);
            }
            for (var i = startIndex; i < endIndex; i++) {
                this.model.LoadMasterAssociatePoolDgDtls[i].assocConsideredFlg = "Y";
            }

            startIndex = CONSTANTS.ASSOCIATES_PER_PAGE * this.model._currentAssociatePage;
            if (CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage + 1) >= this.model.LoadMasterAssociatePoolDgDtls.length) {
                endIndex = this.model.LoadMasterAssociatePoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage + 1);
            }
            this.model._currentAssociatePage++;
            for (var j = startIndex; j < endIndex; j++) {

                var tempVo = {};
                consideredInQPVO = {};
                tempVo = this.model.LoadMasterAssociatePoolDgDtls[j];

                if (tempVo.assocConsideredFlg !== "Y" && this.model._currentAssociatePage <= this.model._associatePagesAlreadyUpdated) {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList2.push(consideredInQPVO);
                } else {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList.push(consideredInQPVO);
                }

                this.model.LoadAssociatePoolDgDtls.push(tempVo);

            }
            this.checkAndMarkAssocAsConsd(consideredList, consideredList2);
            if ((this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE) <= this.model.LoadMasterAssociatePoolDgDtls.length) {
                this.model._currentAssociateGroupStartValue = startIndex + 1;
                this.model._currentAssociateGroupEndValue = (this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE);
            } else {
                this.model._currentAssociateGroupStartValue = startIndex + 1;
                this.model._currentAssociateGroupEndValue = this.model.LoadMasterAssociatePoolDgDtls.length;
            }
            this.model._currentAssociateGroupTotalValue = this.model.LoadMasterAssociatePoolDgDtls.length;
            this.createAssocGrid(this.model.LoadAssociatePoolDgDtls);
        }
    };
    
    this.moveToAssociateCurrentPage = function() {
    	if(CONSTANTS.applicantAttachedtoREQ &&  CONSTANTS.attachedPersonType=="AS"){
    		var newAssocIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (CONSTANTS.cacheCurrentASpage + 1) - 1;
    		var tempVo = {};
            var consideredInQPVO = {};
            var consideredList = [];
            if(this.model.LoadMasterAssociatePoolDgDtls[newAssocIndex])
            	{
            tempVo = this.model.LoadMasterAssociatePoolDgDtls[newAssocIndex];
            consideredInQPVO.id = tempVo.id;
            consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
            consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
            consideredList.push(consideredInQPVO);
    		this.checkAndMarkAssocAsConsd(consideredList, consideredList);
            	}
    	}
    	if(CONSTANTS.cacheCurrentASpage === 0)
    		{
    		return;
    		}
        //if ( CONSTANTS.cacheCurrentASpage === Math.ceil(this.model.LoadMasterAssociatePoolDgDtls.length / CONSTANTS.ASSOCIATES_PER_PAGE)) {
           //if(CONSTANTS.cacheCurrentASpage > 0) {
        	  // this.popup.alert("End of Associate List has been reached.");
           //}
        	   
        //} else {
            this.model.LoadAssociatePoolDgDtls = [];

            var consideredInQPVO = {};
            var consideredList = [];
            var consideredList2 = [];

            var startIndex = 0;
            var endIndex = 0;
            
            startIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (CONSTANTS.cacheCurrentASpage - 1);
            if (CONSTANTS.ASSOCIATES_PER_PAGE * (CONSTANTS.cacheCurrentASpage - 1) >= this.model.LoadMasterAssociatePoolDgDtls.length) {
                endIndex = this.model.LoadMasterAssociatePoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (CONSTANTS.cacheCurrentASpage);
            }
            for (var i = startIndex; i < endIndex; i++) {
                this.model.LoadMasterAssociatePoolDgDtls[i].assocConsideredFlg = "Y";
            }

            startIndex = CONSTANTS.ASSOCIATES_PER_PAGE * CONSTANTS.cacheCurrentASpage;
            if (CONSTANTS.ASSOCIATES_PER_PAGE * (CONSTANTS.cacheCurrentASpage + 1) >= this.model.LoadMasterAssociatePoolDgDtls.length) {
                endIndex = this.model.LoadMasterAssociatePoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (CONSTANTS.cacheCurrentASpage + 1);
            }
            //set the current page to model
            this.model._currentAssociatePage = CONSTANTS.cacheCurrentASpage + 1;  // ---->please check as this should be incremented only when next page button is clicked
            for (var j = startIndex; j < endIndex; j++) {

                var tempVo = {};
                consideredInQPVO = {};
                tempVo = this.model.LoadMasterAssociatePoolDgDtls[j];

                if (tempVo.assocConsideredFlg !== "Y" && CONSTANTS.cacheCurrentASpage <= this.model._associatePagesAlreadyUpdated) {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList2.push(consideredInQPVO);
                } else {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList.push(consideredInQPVO);
                }

                this.model.LoadAssociatePoolDgDtls.push(tempVo);

            }
            //making service call only when the page is full. If page is not full we would have called considerinQP already
            if(endIndex - startIndex == (CONSTANTS.ASSOCIATES_PER_PAGE - 1)){
            	//considering only last associate
            	consideredList = [consideredList[consideredList.length-1]];
            	consideredList2 = [consideredList2[consideredList2.length-1]];
            	this.checkAndMarkAssocAsConsd(consideredList, consideredList2);
            }
            if ((this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE) <= this.model.LoadMasterAssociatePoolDgDtls.length) {
                this.model._currentAssociateGroupStartValue = startIndex + 1;
                this.model._currentAssociateGroupEndValue = (this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE);
            } else {
                this.model._currentAssociateGroupStartValue = startIndex + 1;
                this.model._currentAssociateGroupEndValue = this.model.LoadMasterAssociatePoolDgDtls.length;
            }
            this.model._currentAssociateGroupTotalValue = this.model.LoadMasterAssociatePoolDgDtls.length;
            this.createAssocGrid(this.model.LoadAssociatePoolDgDtls);
       // }
    };
    
    this.moveToApplntCurrentPage = function() {
    	if(CONSTANTS.applicantAttachedtoREQ && CONSTANTS.attachedPersonType == "AP"){
    		var newAssocIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (CONSTANTS.cacheCurrentAPpage + 1) - 1;
    		var tempVo = {};
            var consideredInQPVO = {};
            var consideredList = [];
            if(this.model.LoadMasterApplicantPoolDgDtls[newAssocIndex])
            {
            tempVo = this.model.LoadMasterApplicantPoolDgDtls[newAssocIndex];
            consideredInQPVO.id = tempVo.id;
            consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
            consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
            consideredList.push(consideredInQPVO);
    		this.checkAndMarkApplAsConsd(consideredList, consideredList);
            }
    	}
    	if(CONSTANTS.cacheCurrentAPpage === 0)
		{
    		return;
		}
    	
        //if (CONSTANTS.cacheCurrentAPpage === Math.ceil(this.model.LoadMasterApplicantPoolDgDtls.length / CONSTANTS.APPLICANTS_PER_PAGE)) {
            //if(CONSTANTS.cacheCurrentAPpage > 0) {
            //	this.popup.alert("End of Applicant List has been reached.");
            //}
        //} else {
            this.model.LoadApplicantPoolDgDtls = [];

            var consideredInQPVO = {};
            var consideredList = [];
            var consideredList2 = [];

            var startIndex = 0;
            var endIndex = 0;
            this.qpCurrentPage++;
            startIndex = CONSTANTS.APPLICANTS_PER_PAGE * (CONSTANTS.cacheCurrentAPpage - 1);
            if (CONSTANTS.APPLICANTS_PER_PAGE * (CONSTANTS.cacheCurrentAPpage - 1) >= this.model.LoadMasterApplicantPoolDgDtls.length) {
                endIndex = this.model.LoadMasterApplicantPoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.APPLICANTS_PER_PAGE * (CONSTANTS.cacheCurrentAPpage);
            }
            for (var i = startIndex; i < endIndex; i++) {

                this.model.LoadMasterApplicantPoolDgDtls[i].consideredFlg = "Y";
            }

            startIndex = CONSTANTS.APPLICANTS_PER_PAGE * CONSTANTS.cacheCurrentAPpage;
            if (CONSTANTS.APPLICANTS_PER_PAGE * (CONSTANTS.cacheCurrentAPpage + 1) >= this.model.LoadMasterApplicantPoolDgDtls.length) {
                endIndex = this.model.LoadMasterApplicantPoolDgDtls.length;
            } else {
                endIndex = CONSTANTS.APPLICANTS_PER_PAGE * (CONSTANTS.cacheCurrentAPpage + 1);
            }
            // set the current page to model
            this.model._currentApplicantPage = CONSTANTS.cacheCurrentAPpage + 1;// ---->please check as this should be incremented only when next page button is clicked
            for (var j = startIndex; j < endIndex; j++) {
                var tempVo = {};
                consideredInQPVO = {};
                tempVo = this.model.LoadMasterApplicantPoolDgDtls[j];
                if (tempVo.consideredFlg !== "Y" && CONSTANTS.cacheCurrentAPpage <= this.model._applicantPagesAlreadyUpdated) {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList2.push(consideredInQPVO);
                } else {
                    consideredInQPVO.id = tempVo.id;
                    consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                    consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                    consideredList.push(consideredInQPVO);
                }
                this.model.LoadApplicantPoolDgDtls.push(tempVo);
            }
          //making service call only when the page is full. If page is not full we would have called considerinQP already
            if(endIndex - startIndex == (CONSTANTS.ASSOCIATES_PER_PAGE - 1)){
            	//considering only last applicant
            	consideredList = [consideredList[consideredList.length-1]];
            	consideredList2 = [consideredList2[consideredList2.length-1]];
            	this.checkAndMarkApplAsConsd(consideredList, consideredList2);
            }
            //
            if ((this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE) <= this.model.LoadMasterApplicantPoolDgDtls.length) {
                this.model._currentApplicantGroupStartValue = startIndex + 1;
                this.model._currentApplicantGroupEndValue = (this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE);
            } else {
                this.model._currentApplicantGroupStartValue = startIndex + 1;
                this.model._currentApplicantGroupEndValue = this.model.LoadMasterApplicantPoolDgDtls.length;
            }
            this.model._currentApplicantGroupTotalValue = this.model.LoadMasterApplicantPoolDgDtls.length;
            this.createApplGrid(this.model.LoadApplicantPoolDgDtls);
            
        //}
    };
    
    
    /*
     * This function process applicants
     *
     * @param N/A
     * @return N/A
     */
    this.processApplicants = function() {
        var k = 0;

        for (var i = 0; i < this.model.LoadApplicantPoolDgDtls.length; i++) {
            if (this.model.LoadApplicantPoolDgDtls[i].attachedToReq) {
                this.apIdToReqArray.push(i);
            }
        }

        for (var j = 0; j < this.apIdToReqArray.length; j++) {
            this.model.candidateCt++;
            this.model.LoadApplicantPoolDgDtls.splice(this.apIdToReqArray[j] - k, 1);
            this.model.LoadMasterApplicantPoolDgDtls.splice(this.apIdToReqArray[j] - k, 1);
            k++;
        }

        if (this.apIdToReqArray.length > 0) {
            this.applicantsRemovedUpdateConsideredAndRebuildCurrentPage(this.apIdToReqArray.length);
        }
    };
    /*
     * This function process associates
     *
     * @param N/A
     * @return N/A
     */
    this.processAssociates = function() {
        var k = 0;

        for (var m = 0; m < this.model.LoadAssociatePoolDgDtls.length; m++) {
            if (this.model.LoadAssociatePoolDgDtls[m].attachedToReq) {
                this.asIdToReqArray.push(m);
            }
        }

        for (var n = 0; n < this.asIdToReqArray.length; n++) {
            this.model.candidateCt++;
            this.model.LoadAssociatePoolDgDtls.splice(this.asIdToReqArray[n] - k);
            this.model.LoadMasterAssociatePoolDgDtls.splice(this.asIdToReqArray[m] - k);
            k++;
        }

        if (this.asIdToReqArray.length > 0) {
            this.associatesRemovedUpdateConsideredAndRebuildCurrentPage(this.asIdToReqArray.length);
        }

    };
    /*
     * This function process save
     *
     * @param N/A
     * @return N/A
     */
    this.processSave = function() {
        this.processApplicants();
        this.processAssociates();
        this.apIdToReqArray = [];
        this.asIdToReqArray = [];
        this.applInfoStr = "";
        this.applInfoArray = [];
        var storeNum;
        var reqNum;
        var applId;
        var applName;
        var applType;
        var inputreq = {
            "ApplToReqRequest" : {
                "ApplToReqList" : [ {} ]
            }
        };
        var ApplToReq = [];
        var input = {};
        for (var l = 0; l < this.applToReqArray.length; l++) {

            this.applInfoStr = applToReqArray[l].toString();
            this.applInfoArray = this.applInfoStr.split("_");
            storeNum = this.applInfoArray[0];
            reqNum = this.applInfoArray[1];
            applId = this.applInfoArray[2];
            applName = this.applInfoArray[3];
            applType = this.applInfoArray[4];

            input.StoreNumber = storeNum;
            input.ReqNumber = reqNum;
            input.ApplId = applId;
            input.ApplName = applName;
            input.ApplType = applType;
            ApplToReq.push(input);
        }
        if (ApplToReq.length > 1) {
            inputreq.ApplToReqRequest.ApplToReqList.push({
                "ApplToReq" : ApplToReq
            });
        } else if (ApplToReq.length === 1) {
            inputreq.ApplToReqRequest.ApplToReqList.push({
                "ApplToReq" : input
            });

        }
        this.rsaServices.retailStaffingAttachApplToReq({data:JSON.stringify(inputreq)}, this.retailStaffingAttachApplToReqSuccess.bind(this), this.retailStaffingAttachApplToReqFailure.bind(this));
        this.applToReqArray = [];
        this.createApplGrid(this.model.LoadApplicantPoolDgDtls);
        this.showApplUnavail();
    };
    /*
     * This function updates considered and rebuild current page
     *
     * @param numApplicantsAttached
     * @return N/A
     */
    this.applicantsRemovedUpdateConsideredAndRebuildCurrentPage = function(numApplicantsAttached) {
    	var consideredInQPVO = {};
        var consideredList = [];

        var startIndex = (CONSTANTS.APPLICANTS_PER_PAGE * this.model._currentApplicantPage) - numApplicantsAttached;
        var startIndexForGroup = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage - 1);
        var endIndex = 0;
        if (CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage) >= this.model.LoadMasterApplicantPoolDgDtls.length) {
            endIndex = this.model.LoadMasterApplicantPoolDgDtls.length;
        } else {
            endIndex = CONSTANTS.APPLICANTS_PER_PAGE * (this.model._currentApplicantPage);
        }

        for (var i = startIndex; i < endIndex; i++) {
            var tempVo = {};
            tempVo = this.model.LoadMasterApplicantPoolDgDtls[i];

            if (tempVo.consideredFlg !== "Y") {
                this.model.LoadMasterApplicantPoolDgDtls[i].consideredFlg = "Y";
                tempVo.consideredFlg = "";
                consideredInQPVO.id = tempVo.id;
                consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                if (i > endIndex - 1 - numApplicantsAttached) {
                    consideredList.push(consideredInQPVO);
                }
            }

            this.model.LoadApplicantPoolDgDtls.push(tempVo);

        }

        this.createApplGrid(this.model.LoadApplicantPoolDgDtls);

        var ApplicantsConsideredInQPRequest = {};
        ApplicantsConsideredInQPRequest.applicantList = {};
        ApplicantsConsideredInQPRequest.applicantList.applicant = [];
        var viewedInQPVO = {};

        for (var j = 0; j < consideredList.length; j++) {
            viewedInQPVO = consideredList[j];
            ApplicantsConsideredInQPRequest.applicantList.applicant.push({"applicantId":viewedInQPVO.id,"reqNbr":viewedInQPVO.reqNbr,"consideredCode":viewedInQPVO.consideredCode});
        }
        var input = {};
        input.ApplicantsConsideredInQPRequest = ApplicantsConsideredInQPRequest;
        	this.rsaServices
                .markApplicantsAsConsideredInQPEvent({data:JSON.stringify(input)}, this.markApplicantsAsConsideredInQPEventSuccess.bind(this), this.markApplicantsAsConsideredInQPEventFailure
                        .bind(this));

        if ((this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE) <= this.model.LoadMasterApplicantPoolDgDtls.length) {
            this.model._currentApplicantGroupStartValue = startIndexForGroup + 1;
            this.model._currentApplicantGroupEndValue = (this.model._currentApplicantPage * CONSTANTS.APPLICANTS_PER_PAGE);
        } else {
            this.model._currentApplicantGroupStartValue = startIndexForGroup + 1;
            this.model._currentApplicantGroupEndValue = this.model.LoadMasterApplicantPoolDgDtls.length;
        }
        this.model._currentApplicantGroupTotalValue = this.model.LoadMasterApplicantPoolDgDtls.length;
    };
    /*
     * This function updates considered and rebuild current page
     *
     * @param numAssociatesAttached
     * @return N/A
     */
    this.associatesRemovedUpdateConsideredAndRebuildCurrentPage = function(numAssociatesAttached) {
        var consideredInQPVO = {};
        var consideredList = [];
        var startIndex = (CONSTANTS.ASSOCIATES_PER_PAGE * this.model._currentAssociatePage) - numAssociatesAttached;
        var startIndexForGroup = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage - 1);
        var endIndex = 0;
        if (CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage) >= this.model.LoadMasterAssociatePoolDgDtls.length) {
            endIndex = this.model.LoadMasterAssociatePoolDgDtls.length;
        } else {
            endIndex = CONSTANTS.ASSOCIATES_PER_PAGE * (this.model._currentAssociatePage);
        }

        for (var i = startIndex; i < endIndex; i++) {
            var tempVo = {};
            tempVo = this.model.LoadMasterAssociatePoolDgDtls[i];

            if (tempVo.assocConsideredFlg !== "Y") {
                this.model.LoadMasterAssociatePoolDgDtls[i].assocConsideredFlg = "Y";
                tempVo.assocConsideredFlg = "";
                consideredInQPVO.id = tempVo.id;
                consideredInQPVO.reqNbr = this.model.LoadREQDGDltls[0].reqNbr;
                consideredInQPVO.consideredCode = CONSTANTS.CONSIDERED_IN_QP_CD;
                if (i > endIndex - 1 - numAssociatesAttached) {
                    consideredList.push(consideredInQPVO);
                }
            }

            this.model.LoadAssociatePoolDgDtls.push(tempVo);
        }

        var ApplicantsConsideredInQPRequest = {};
        ApplicantsConsideredInQPRequest.applicantList = {};
        ApplicantsConsideredInQPRequest.applicantList.applicant = [];
        var viewedInQPVO = {};

        for (var j = 0; j < consideredList.length; j++) {
            viewedInQPVO = consideredList[j];
            ApplicantsConsideredInQPRequest.applicantList.applicant.push({"applicantId":viewedInQPVO.id,"reqNbr":viewedInQPVO.reqNbr,"consideredCode":viewedInQPVO.consideredCode});
        }
        var input = {};
        input.ApplicantsConsideredInQPRequest = ApplicantsConsideredInQPRequest;
        	this.rsaServices
                .markApplicantsAsConsideredInQPEvent({data:JSON.stringify(input)}, this.markApplicantsAsConsideredInQPEventSuccess.bind(this), this.markApplicantsAsConsideredInQPEventFailure
                        .bind(this));
        this.createAssocGrid(this.model.LoadAssociatePoolDgDtls);

        if ((this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE) <= this.model.LoadMasterAssociatePoolDgDtls.length) {
            this.model._currentAssociateGroupStartValue = startIndexForGroup + 1;
            this.model._currentAssociateGroupEndValue = (this.model._currentAssociatePage * CONSTANTS.ASSOCIATES_PER_PAGE);
        } else {
            this.model._currentAssociateGroupStartValue = startIndexForGroup + 1;
            this.model._currentAssociateGroupEndValue = this.model.LoadMasterAssociatePoolDgDtls.length;
        }
        this.model._currentAssociateGroupTotalValue = this.model.LoadMasterAssociatePoolDgDtls.length;
    };
    /*
     * This function shows if appl is unavailable
     *
     * @param N/A
     * @return N/A
     */
    this.showApplUnavail = function() {
        var applUnavailNameStr = "";

        if (this.model._hasDuplicateAppl === "true") {

            for (var i = 0; i < this.model.LoadApplUnavailInfo.length; i++) {
                applUnavailNameStr += this.model.LoadApplUnavailInfo[i].applUnavailName;
            }

            this.popup.alert("Applicant(s) " + applUnavailNameStr + " not available for this requisition.");

        }
        this.model._hasDuplicateAppl = "";
        this.model.LoadApplUnavailInfo = "";

    };
    /*
     * This function process the submit event
     *
     * @param N/A
     * @return N/A
     */
    this.processSubmit = function() {
        this.processSave();
        this.resetApplicantGroupDisplay();
        CONSTANTS.SUCCESS_ON_HOME = true;
        this.navigateToSearchScreen();
    };
    /*
     * This function resets values for applicant group display
     *
     * @param N/A
     * @return N/A
     */
    this.resetApplicantGroupDisplay = function() {
        this.model._currentApplicantGroupStartValue = 0;
        this.model._currentApplicantGroupEndValue = 0;
        this.model._currentApplicantGroupTotalValue = 0;
    };
    /*
     * This function handles cancel event of Qualified pool
     *
     * @param N/A
     * @return N/A
     */
    this.cancelAttachment = function() {
    	// on cnacel clear the cache values.
    	UTILITY.clearCache();
    	
        var applicantPoolDtlVO = {};
        var associatePoolDtlVO = {};
        for (var i = 0; i < this.apIdToReqArray.length; i++) {
            applicantPoolDtlVO = {};
            applicantPoolDtlVO.id = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].id;
            applicantPoolDtlVO.name = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].name;
            applicantPoolDtlVO.store = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].store;
            applicantPoolDtlVO.jobTitle = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].jobTitle;
            applicantPoolDtlVO.type = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].type;
            applicantPoolDtlVO.availInd = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].availInd;
            applicantPoolDtlVO.updTs = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].updTs;
            applicantPoolDtlVO.prefIndicator = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].prefIndicator;
            applicantPoolDtlVO.rehireEligibilityFlg = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].rehireEligibilityFlg;
            applicantPoolDtlVO.availDate = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].availDate;
            applicantPoolDtlVO.applDate = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].applDate;
            applicantPoolDtlVO.attachedToReq = false;
            applicantPoolDtlVO.consideredFlg = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].consideredFlg;
            applicantPoolDtlVO.availability = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].availability;

            this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]] = applicantPoolDtlVO;
        }
        for (var j = 0; j < this.asIdToReqArray.length; j++) {
            associatePoolDtlVO = {};
            associatePoolDtlVO.id = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].id;
            associatePoolDtlVO.name = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].name;
            associatePoolDtlVO.store = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].store;
            associatePoolDtlVO.jobTitle = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].jobTitle;
            associatePoolDtlVO.type = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].type;
            associatePoolDtlVO.availInd = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].availInd;
            associatePoolDtlVO.emplCat = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].emplCat;
            associatePoolDtlVO.updTs = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].updTs;
            associatePoolDtlVO.prefIndicator = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].prefIndicator;
            associatePoolDtlVO.attachedToReq = false;
            associatePoolDtlVO.assocConsideredFlg = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].consideredFlg;
            associatePoolDtlVO.assocAvailability = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]].availability;
            this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[j]] = associatePoolDtlVO;

        }
        //this.rsaServices.getRequisitionDetails({data:this.model.LoadREQDGDltls[0].reqNbr}, this.displayValues.bind(this), this.getRequisitionDetailsError.bind(this));
        $("#reqdetnav .reqmessageBar").text("REQUISITION DETAILS");
        this.applToReqArray = [];
        $(".topform").unblock();
        $(".reqNbrText").show();
        $(".reqNbrLink").hide();
        $(".reqQua").show();
        $(".qualPool").hide();
        this.resetApplicantGroupDisplay();
        
        this.makeServiceCall();
        
        this.displayCandidatesGrid([]);
        this.displayCandidatesGrid(this.model.CndtDltls);
        //Scroll back to top.
        $('.reqDetMainContainer').animate({ scrollTop: 0 });
    };
    /*
     * This function attaches applicant to requisition
     *
     * @param applId
     * @param applName
     * @param applType
     * @return N/A
     */
    this.attachApplIdtoReq = function(applId, applName, applType) {

        var storeNum = "";
        var reqNum = "";
        var checkCB = false;
        storeNum = this.model.LoadREQDGDltls[0].store;
        reqNum = this.model.LoadREQDGDltls[0].reqNbr;
        var applToReqInfoStr = storeNum + "_" + reqNum + "_" + applId + "_" + applName + "_" + applType;

        if (!this.applToReqArray.contains(applToReqInfoStr)) {

            checkCB = true;
            this.applToReqArray.push(applToReqInfoStr);
        } else {
            checkCB = false;
            var index = this.applToReqArray.indexOf(applToReqInfoStr);
            this.applToReqArray.splice(index, 1);
        }
        var applicantPoolDtlVO = {};
        if (applType === "AP") {
            for (var m = 0; m < this.model.LoadApplicantPoolDgDtls.length; m++) {
                if (applId === this.model.LoadApplicantPoolDgDtls[m].id) {
                    applicantPoolDtlVO = {};
                    applicantPoolDtlVO.id = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].id;
                    applicantPoolDtlVO.name = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].name;
                    applicantPoolDtlVO.store = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].store;
                    applicantPoolDtlVO.jobTitle = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].jobTitle;
                    applicantPoolDtlVO.type = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].type;
                    applicantPoolDtlVO.availInd = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].availInd;
                    applicantPoolDtlVO.updTs = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].updTs;
                    applicantPoolDtlVO.prefIndicator = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].prefIndicator;
                    applicantPoolDtlVO.rehireEligibilityFlg = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].rehireEligibilityFlg;
                    applicantPoolDtlVO.availDate = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].availDate;
                    applicantPoolDtlVO.applDate = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].applDate;
                    applicantPoolDtlVO.attachedToReq = checkCB;
                    applicantPoolDtlVO.consideredFlg = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].consideredFlg;
                    applicantPoolDtlVO.availability = this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]].availability;

                    this.model.LoadApplicantPoolDgDtls[this.apIdToReqArray[i]] = applicantPoolDtlVO;
                }

            }

        } else if (applType === "AS") {
            var associatePoolDtlVO = {};
            for (var n = 0; n < this.model.LoadAssociatePoolDgDtls.length; n++) {
                if (applId === this.model.LoadAssociatePoolDgDtls[n].id) {
                    associatePoolDtlVO = {};
                    associatePoolDtlVO.id = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].id;
                    associatePoolDtlVO.name = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].name;
                    associatePoolDtlVO.store = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].store;
                    associatePoolDtlVO.jobTitle = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].jobTitle;
                    associatePoolDtlVO.type = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].type;
                    associatePoolDtlVO.availInd = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].availInd;
                    associatePoolDtlVO.emplCat = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].emplCat;
                    associatePoolDtlVO.updTs = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].updTs;
                    associatePoolDtlVO.prefIndicator = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].prefIndicator;
                    associatePoolDtlVO.attachedToReq = checkCB;
                    associatePoolDtlVO.assocConsideredFlg = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].consideredFlg;
                    associatePoolDtlVO.assocAvailability = this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]].availability;
                    this.model.LoadAssociatePoolDgDtls[this.asIdToReqArray[i]] = associatePoolDtlVO;
                }
            }

        }
        this.createApplGrid(this.model.LoadApplicantPoolDgDtls);
        this.createAssocGrid(this.model.LoadAssociatePoolDgDtls);
    };
    /*
     * This function is success handler of mark applicants as considered in QP
     *
     * @param successjson
     * @return N/A
     */
    this.markApplicantsAsConsideredInQPEventSuccess = function(successjson) {
        if (successjson.Response.status) {
            if (successjson.Response.status === CONSTANTS.SUCCESS) {
                // do nothing
            } else if (successjson.Response.error) {
                this.popup.alert(successjson.Response.error.errorMsg);
            }
        } else {
            this.popup.alert(successjson.Response.error.errorMsg);
        }

    };
    /*
     * This function is success handler of atach applicant
     *
     * @param response
     * @return N/A
     */
    this.retailStaffingAttachApplToReqSuccess = function(response) {
        response = response.Response;
        this.model = _.extend(this.model, new retailStaffingAttachAppl(response));
        if (response.error) {
            this.popup.alert(response.error.errorMsg);
        }
    };
    this.retailStaffingAttachApplToReqFailure = function() {
        // Unblock full page handled in rsaServices
    };
    this.markApplicantsAsConsideredInQPEventFailure = function() {
        // Unblock full page handled in rsaServices
    };
    /*
     * This function shows confirmation popup to close window
     *
     * @param N/A
     * @return N/A
     */
    this.onCloseBtnClick = function(){
        this.linkClick(this.onCloseButtonOK.bind(this),this.openPopUpMisPhnSrnLoadCandPhn.bind(this,this.onCloseButtonOK.bind(this)));
    };
    /*
     * This function closes the window
     *
     * @param N/A
     * @return N/A
     */
    this.onCloseButtonOK = function(){
        window.open('', '_self', '');
        window.close();
    };
};

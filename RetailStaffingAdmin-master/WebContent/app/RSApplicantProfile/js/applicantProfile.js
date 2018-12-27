 /*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: applicantProfile.js
 * Application: Retail Staffing Admin
 *
 */
var applProf = new applicantProfile();
function applicantProfile() {
    this.model = {};
    this.rsaServices = RSASERVICES;
    this.popup = new rsaPopup();
    this.model.rjctSaveCheck = false;
    CONSTANTS.applicantAttachedtoREQ = false;
    CONSTANTS.attachedPersonType = "AS";
   /* this.model.langDet = [ {
        "langCode" : "1",
        "dsplyDesc" : "English",
        "shortDesc" : "ENGLISH"
    }, {
        "langCode" : "2",
        "dsplyDesc" : "Spanish",
        "shortDesc" : "PANISH"
    }, {
        "langCode" : "3",
        "dsplyDesc" : "French",
        "shortDesc" : "FRENCH"
    }, {
        "langCode" : "4",
        "dsplyDesc" : "German",
        "shortDesc" : "GERMAN"
    }, {
        "langCode" : "5",
        "dsplyDesc" : "Italian",
        "shortDesc" : "ITALIAN"
    }, {
        "langCode" : "6",
        "dsplyDesc" : "Chinese",
        "shortDesc" : "CHINESE"
    }, {
        "langCode" : "7",
        "dsplyDesc" : "Tagalog",
        "shortDesc" : "TAGALOG"
    }, {
        "langCode" : "8",
        "dsplyDesc" : "Polish",
        "shortDesc" : "POLISH"
    }, {
        "langCode" : "9",
        "dsplyDesc" : "Korean",
        "shortDesc" : "KOREAN"
    }, {
        "langCode" : "10",
        "dsplyDesc" : "Vietnamese",
        "shortDesc" : "VIETNAMESE"
    }, {
        "langCode" : "11",
        "dsplyDesc" : "Portuguese",
        "shortDesc" : "PORTUGUESE"
    }, {
        "langCode" : "12",
        "dsplyDesc" : "Japanese",
        "shortDesc" : "JAPANESE"
    }, {
        "langCode" : "13",
        "dsplyDesc" : "Greek",
        "shortDesc" : "GREEK"
    }, {
        "langCode" : "14",
        "dsplyDesc" : "Arabic",
        "shortDesc" : "ARABIC"
    }, {
        "langCode" : "15",
        "dsplyDesc" : "Hindi",
        "shortDesc" : "HINDI"
    }, {
        "langCode" : "16",
        "dsplyDesc" : "American Sign Language",
        "shortDesc" : "SIGN"
    } ];*/
    this.model.skillsRef = {
        "Interior Design" : "intDesignCheck",
        "Window Treatments" : "winTreatCheck",
        "Wallpaper" : "wallpaperCheck",
        "Cash Register" : "cashRegCheck",
        "Electrical" : "electCheck",
        "Lighting" : "lightCheck",
        "Floors: Carpet Sales" : "cptSalesCheck",
        "Floors: Wood - Vinyl" : "wdVinylCheck",
        "Floors: Ceramic Tile" : "ceraTileCheck",
        "Hand Tools" : "hdToolsCheck",
        "Power Tools" : "pwrToolsCheck",
        "Hardware" : "hdwareCheck",
        "Appliance Sales" : "applSalesCheck",
        "Kitchen Sales" : "ktchnSalesCheck",
        "Bath Sales" : "bthSalesCheck",
        "Kitchen/Bath Design" : "kbDesignCheck",
        "Carpentry" : "carpentryCheck",
        "Foundation" : "foundCheck",
        "Home Remodeling" : "homeRemCheck",
        "Masonry" : "masCheck",
        "Roofing" : "roofCheck",
        "Drywall" : "drywallCheck",
        "Framing" : "framingCheck",
        "Lumber" : "lumberCheck",
        "Roofing2" : "roofing2Check",
        "Siding" : "sidingCheck",
        "Millwork" : "millworkCheck",
        "Window/Door Install" : "winDoorInstallCheck",
        "Trim Carpentry" : "trimCarpCheck",
        "Computer" : "computerCheck",
        "Contractor" : "contractorCheck",
        "Installation" : "installCheck",
        "Consultation Sales" : "conSalesCheck",
        "General Sales" : "genSalesCheck",
        "Painting" : "paintCheck",
        "HVAC" : "hvacCheck",
        "Plumbing" : "plumbCheck",
        "Pro Sales" : "proSalesCheck",
        "Freight Management" : "frtMgmntCheck",
        "Stocking" : "stockCheck",
        "Shipping/Receiving" : "shipReceCheck",
        "Forklift" : "fkliftCheck",
        "Hard Scaping" : "hdScapingCheck",
        "Landscape Design" : "laDesignCheck",
        "Nursery/Horticulture" : "nurHorCheck",
        "Irrigation" : "irrigationCheck",
        "Lawn Equipment" : "lawnEquipCheck",
        "Engine Repair" : "engRepairCheck",
        "Tool Rental" : "toolRentalCheck"
    };
    /*
     * This method initializes the view with empty grids and empty forms before
     * making service calls.
     */
    this.initializePage = function() {
    	$('.modal-backdrop').empty().remove();
        $(".attachCand .rjctRsncb").selectBoxIt();
        //this.generateLanguageSkills();
        /*this.generateJobPreference();
        this.createReviewResultsGrid();
        this.createPreviousPositionGrid();
        this.createPhnScrHistGrid();
        this.createCandStatHistGrid();
        this.createApplicationdtls1Grid();
        this.createApplicationdtls2Grid();
        this.blockFormElements(".block-control");*/
        this.unbindEvents();
        this.bindEvents();
        // This performs service call to get the Rejection details details.
        this.rsaServices.loadRejectDetails("", this.loadRejectDetailsSuccess.bind(this), this.loadRejectDetailsFail.bind(this));
    };
    this.unbindEvents = function(){
        $("#goHome.reqnavHeaderButtons").unbind();
        $(".reqnavHeaderButtons.navCloseBtn").unbind();
        $(".attachCand .attachreq").unbind();
        $(".attachCand #saveBtn").unbind();
    };
    /*
     * This method bind events to all enabled components in the page.
     */
    this.bindEvents = function() {
        $("#goHome.reqnavHeaderButtons").click(this.homeBtnClick.bind(this));
        $(".reqnavHeaderButtons.navCloseBtn").click(this.onCloseBtnClick.bind(this));
        $(".attachCand .attachreq").change(this.attachReqChange.bind(this));
        $(".attachCand #saveBtn").click(this.saveClick.bind(this));
    };
    this.homeBtnClick = function(){
        this.popup.warnUnsavedData(CONSTANTS.RJCT_DTLS_UNSAVED_PART_1,CONSTANTS.STAFF_DET_UNSAVED_DATA_PART_2,this.navigateToSearchScreen.bind(this),this.handleClose.bind(this), 150, 400);
    };
    this.navigateToSearchScreen = function() {
    	UTILITY.RemoveQS();
        $.get('app/RetailStaffing/view/retailStaffing.html', CONSTANTS.retailStaffingObj.setContent);
    };
    this.handleClose = function() {
        $("#genericPopup").modal("hide");
    };
    /*
     * This method is event handler for attach requisition radio button change.
     */
    this.attachReqChange = function() {
        if ($(".attachCand .attachreq:checked").val() === "Y") {
            //this.blockFormElements(".attachCand .rjctRsncb");
            $(".attachCand .rjctRsncb").attr("disabled","disabled");
        } else {
            $(".attachCand .rjctRsncb").unblock();
            $(".attachCand .rjctRsncb").removeAttr("disabled");
        }
        $(".attachCand .rjctRsncb").data("selectBox-selectBoxIt").refresh();
    };
    /*
     * This method is event handler for save button click.
     */
    this.saveClick = function() {
        if ($(".attachCand .attachreq:checked").val() === "N") {
            if (parseInt($(".attachCand .rjctRsncb").val()) === 0) {
                this.popup.alert(CONSTANTS.NO_RJCT_SELECTED_WARNING);
            } else {
                this.createRejectDetails($(".attachCand .rjctRsncb").val(), true);
            }
        } else {
            this.attachAndReturn();
        }
        // turn off save check
        this.model.rjctSaveCheck = false;
    };
    /*
     * This method performs service call to get the candidate details.
     */
    this.loadCandidateDetails = function(appl) {
        this.rsaServices.loadCandidateDetails({data:appl.id + "_" + appl.type + "_" + ((appl.type === "AP")?appl.applDate:appl.store)}, this.loadCandidateDetailsSuccess.bind(this), this.loadCandidateDetailsFail.bind(this));
    };
    /*
     * This method is success handler of get candidate details service call.
     */
    this.loadCandidateDetailsSuccess = function(response) {
        response = response.Response;
        this.model = _.extend(this.model, new getApplicantProfile(response));
        this.fillGrids();
        this.onPageShown();
    };
    /*
     * This method is failure handler of get candidate details service call.
     */
    this.loadCandidateDetailsFail = function() {
        // Unblock full page handled in rsaServices
    };
    /*
     * This method is success handler of get rejection details service call.
     */
    this.loadRejectDetailsSuccess = function(response) {
        this.generateJobPreference();
        this.createReviewResultsGrid();
        this.createPreviousPositionGrid();
        this.createPhnScrHistGrid();
        this.createCandStatHistGrid();
        this.createApplicationdtls1Grid();
        this.createApplicationdtls2Grid();
        //$(".block-control").append('<div class="blockUI blockOverlay" style="margin: 0px; padding: 0px; border: currentColor; border-image: none; left: 0px; top: 0px; width: 100%; height: 100%; position: absolute; z-index: 100; cursor: auto; opacity: 0.6; background-color: rgb(255, 255, 255);"></div>');
        response = response.Response;
        this.model = _.extend(this.model, new loadRejectDetails(response));
        this.loadCandidateDetails(CONSTANTS.retailStaffingObj.applicantObj);
    };
    /*
     * This method is failure handler of get rejection details service call.
     */
    this.loadRejectDetailsFail = function() {
        this.generateJobPreference();
        this.createReviewResultsGrid();
        this.createPreviousPositionGrid();
        this.createPhnScrHistGrid();
        this.createCandStatHistGrid();
        this.createApplicationdtls1Grid();
        this.createApplicationdtls2Grid();
        //$(".block-control").append('<div class="blockUI blockOverlay" style="margin: 0px; padding: 0px; border: currentColor; border-image: none; left: 0px; top: 0px; width: 100%; height: 100%; position: absolute; z-index: 100; cursor: auto; opacity: 0.6; background-color: rgb(255, 255, 255);"></div>');
        this.loadCandidateDetails(CONSTANTS.retailStaffingObj.applicantObj);
        // Unblock full page handled in rsaServices
    };
    /*
     * This method fills all grids with data.
     */
    this.fillGrids = function() {
        this.createReviewResultsGrid(this.model.LoadApplAssociateReviewDtls);
        this.createPreviousPositionGrid(this.model.LoadApplAssociatePrevPosDtls);
        this.createPhnScrHistGrid(this.model.LoadApplPhnScrnDtls);
        this.createCandStatHistGrid(this.model.LoadApplStatusHistoryDtls);
        this.createApplicationdtls1Grid(this.model.LoadApplAppHistoryDtls);
        this.createApplicationdtls2Grid(this.model.LoadApplAppHistoryDtls2);
    };
    /*
     * This method is called after the whole page is completely initialized.
     */
    this.onPageShown = function() {
        // Flex logics applied hereafter
        this.showHomeBtn();
        this.initProfileInfo();
        this.displayProfileInfo();
    };
    /*this.generateLangSkillsRows = function(row,langDet,k,j){
        if (langDet[k]) {
            if (j < 3) {
                row = row + '<div class="col-xs-2"><div class="col-xs-8">' + langDet[k].dsplyDesc + '</div><div class="col-xs-3 block-control"><span class="uncheckSpan gridCheckBox" name=check' + ' value=' + langDet[k].langCode + ' /></div></div>';
            } else {
                row = row + '<div class="col-xs-3"><div class="col-xs-10 nowrap">' + langDet[k].dsplyDesc + '</div><div class="col-xs-2 block-control"><span class="uncheckSpan gridCheckBox" name=check' + ' value=' + langDet[k].langCode + ' /></div></div>';
            }
        }
        return row;
    };*/
    /*
     * This method generates Language skillset checkbox grid
     */
    /*this.generateLanguageSkills = function() {
        var langDet = this.model.langDet;
        var k = 0;
        for (var i = 0; i < 4; i++) {
            var row = '<div class="row">';
            for (var j = 0; j < 4; j++) {
                row = this.generateLangSkillsRows(row,langDet, k, j);
                k++;
            }
            $(".langAbl .langskillablcontainer").append(row + '</div>');
        }
    };*/
    /*
     * This method generates Job preference grid
     */
    this.generateJobPreference = function() {
        var daysLabel = "Days candidate is available to work:";
        var slotsLabel = "Times candidate is available to work:";
        var splabelsArray = [ "Early Am (4am - 6am)", "Mornings (6am - noon)", "Afternoons (noon - 5pm)", "Nights (5pm - 8pm)", "Late Night (8pm - midnight)", "Overnight (midnight - 5am)" ];
        var headerRow = '<div class="col-xs-12 row"><div class="col-xs-12 row value-align schedulePref">';
        var weekRows = '<div class="col-xs-12 row schedLbl"><label>' + daysLabel + '</label></div><div class="col-xs-12 row weekHeader block-control"><div class=" col-xs-1"><span class="uncheckSpan spCheckBox anyTime" id="schdPref0"></div><label  class="col-xs-1 control-label">Weekdays</label>' + '</div><div class="col-xs-12 row weekHeader block-control">' + '<div class=" col-xs-1"><span class="uncheckSpan spCheckBox anyTime" id="schdPref1"></div><label  class="col-xs-1 control-label">Saturday</label>' + '</div><div class="col-xs-12 row weekHeader block-control"><div class=" col-xs-1"><span class="uncheckSpan spCheckBox anyTime" id="schdPref2"></div>' + '<label  class="col-xs-1 control-label">Sunday</label>' + '</div>';
        var j = 3;
        var slotsheader = '<div class="col-xs-12 row slotsHeader schedLbl"><label>' + slotsLabel + '</label></div>';
        var firstRow = '';
        for (var i = 0; i < splabelsArray.length; i++) {
            firstRow = firstRow + '<div class="col-xs-12 row slots block-control"><div class=" col-xs-1"><span class="uncheckSpan spCheckBox anyTime" id=schdPref' + j + '></div>' + '<label class="col-xs-9">' + splabelsArray[i] + '</label>' + '</div>';
            j = j + 1;
        }
        var lastrow = '<div class="col-xs-12 row slots block-control accomreq"><div class=" col-xs-1"><span class="uncheckSpan spCheckBox accomreq" id=""></div><label class="col-xs-11">Reasonable accommodation requested - discuss during interview</label></div>';
        var closingDOM = '</div>';
        $(".jobPref .jobPrefContainer").append(headerRow + weekRows + slotsheader + firstRow + lastrow + closingDOM);
    };
    /*
     * This method displays the review result grid
     */
    this.createReviewResultsGrid = function(data) {
        if (!data) {
            data = [];
        }
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : false,
            headerRowHeight : 25,
            rowHeight : 25,
            forceFitColumns : true
        };
        var columns = [ {
            id : "reviewDate",
            name : "Date",
            field : "reviewDate",
            width : 100,
            sortable : false
        }, {
            id : "reviewResult",
            name : "Results",
            field : "reviewResult",
            width : 230,
            sortable : false
        }];
        this.reviewResultGrid = new Slick.Grid("#reviewResultsGrid", data, columns, options);
    };
    /*
     * This method displays the Previous position grid
     */
    this.createPreviousPositionGrid = function(data) {
        if (!data) {
            data = [];
        }
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true
        };
        var columns = [ {
            id : "dept",
            name : "Dept",
            field : "dept",
            width : 100,
            sortable : false
        }, {
            id : "position",
            name : "Position",
            field : "position",
            width : 100,
            sortable : false
        }, {
            id : "dateFrom",
            name : "Date From",
            field : "dateFrom",
            width : 100,
            sortable : false
        }, {
            id : "dateTo",
            name : "Results",
            field : "dateTo",
            width : 100,
            sortable : false
        } ];
        this.prevPositionGrid = new Slick.Grid("#prevPositionGrid", data.splice(0,3), columns, options);
    };
    /*
     * This method displays thePhone Screen History grid
     */
    this.createPhnScrHistGrid = function(data) {
        if (!data) {
            data = [];
        }
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true
        };
        var columns = [ {
            id : "requisitionNum",
            name : "Number",
            field : "requisitionNum",
            width : 100,
            sortable : false,
            formatter : function(row, cell, value) {
                return "<div class='linkcell reqNumLink'><a data-value=" + value + " class=''>" + value + "</a></div>";
            }
        }, {
            id : "storeNum",
            name : "Store",
            field : "storeNum",
            width : 100,
            sortable : false
        }, {
            id : "deptNum",
            name : "Dept.",
            field : "deptNum",
            width : 100,
            sortable : false
        }, {
            id : "job",
            name : "Job",
            field : "job",
            width : 100,
            sortable : false
        }, {
            id : "phnScreenStatus",
            name : "Phone Interview Status",
            field : "phnScreenStatus",
            width : 100,
            sortable : false
        }, {
            id : "status",
            name : "Interview Status",
            field : "status",
            width : 100,
            sortable : false
        }, {
            id : "phnScreenType",
            name : "Type",
            field : "phnScreenType",
            width : 100,
            sortable : false
        }, {
            id : "lastUpdate",
            name : "Phone Interview Date",
            field : "lastUpdate",
            width : 100,
            sortable : false
        } ];
        this.phnScrHistGrid = new Slick.Grid("#phnScrHistGrid", data, columns, options);
        $("#phnScrHistGrid").off("click",".reqNumLink");
        $("#phnScrHistGrid").on("click",".reqNumLink", function(e) {
            this.showPhoneDetails($(e.currentTarget).find("a").attr("data-value"));
        }.bind(this));
    };
    /*
     * This method displays the Candidate Status/History grid
     */
    this.createCandStatHistGrid = function(data) {
        if (!data) {
            data = [];
        }
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true
        };
        var columns = [ {
            id : "reqNumber",
            name : "Req. No.",
            field : "reqNumber",
            width : 100,
            sortable : false
        }, {
            id : "store",
            name : "Store",
            field : "store",
            width : 100,
            sortable : false
        }, {
            id : "dept",
            name : "Dept.",
            field : "dept",
            width : 100,
            sortable : false
        }, {
            id : "title",
            name : "Title",
            field : "title",
            width : 100,
            sortable : false
        }, {
            id : "intvDisposition",
            name : "Interview Disposition",
            field : "intvDisposition",
            width : 100,
            sortable : false
        }, {
            id : "offerStatus",
            name : "Offer Status",
            field : "offerStatus",
            width : 100,
            sortable : false
        }, {
            id : "offerResults",
            name : "Offer Results",
            field : "offerResults",
            width : 100,
            sortable : false
        }, {
            id : "lastUpdated",
            name : "Offer Made Date",
            field : "lastUpdated",
            width : 100,
            sortable : false
        } ];
        this.candStatHistGrid = new Slick.Grid("#candStatHistGrid", data, columns, options);
    };
    /*
     * This method displays the Application History 1 grid
     */
    this.createApplicationdtls1Grid = function(data) {
        if (!data) {
            data = [];
        }
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true
        };
        var columns = [ {
            id : "store",
            name : "Store",
            field : "store",
            width : 100,
            sortable : false
        }/*, {
            id : "lastUpdatedS",
            name : "Last Updated",
            field : "lastUpdatedS",
            width : 250,
            sortable : false
        }*/
        ];
        this.appHistGrid1 = new Slick.Grid("#appHistGrid1", data, columns, options);
    };
    /*
     * This method displays the Application History 2 grid
     */
    this.createApplicationdtls2Grid = function(data) {
        if (!data) {
            data = [];
        }
        var options = {
            enableCellNavigation : false,
            enableColumnReorder : true,
            headerRowHeight : 25,
            rowHeight : 30,
            forceFitColumns : true
        };
        var columns = [ {
            id : "position",
            name : "Positions",
            field : "position",
            width : 200,
            sortable : false
        }, {
            id : "dept",
            name : "Dept",
            field : "dept",
            width : 100,
            sortable : false
        }/*, {
            id : "lastUpdatedP",
            name : "Last Updated",
            field : "lastUpdatedP",
            width : 150,
            sortable : false
        }*/ ];
        this.appHistGrid2 = new Slick.Grid("#appHistGrid2", data, columns, options);
    };
    /*
     * This method disables the controls in the screen
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
     * This method shows the Home button in the top nav bar
     */
    this.showHomeBtn = function() {
        $("#goHome.reqnavHeaderButtons").show();
    };
    /*
     * This method clears all selected data in the screen
     */
    this.initProfileInfo = function() {
        // clear all fields
        var applType = this.model.LoadApplPersonalInfoDtls[0].applType;
        // if internal do not show personal info right
        if (applType === "AS") {
            $(".applProfContainer .personalInfo .personalInfoRt").hide();
        } else {
            $(".applProfContainer .personalInfo .personalInfoRt").show();
        }
        // if external do not show associate info
        if (applType === "AP") {
            $(".applProfContainer .assocInfo").hide();
        } else {
            $(".applProfContainer .assocInfo").show();
        }
        // Clear all text fields
        $(".applProfContainer .value-text").empty();
        // Reset All check boxes
        $(".applProfContainer .block-control input[type=checkbox]").prop("checked", false);
        $('.applProfContainer .rjctRsncb').prop('disabled', false);
        $(".applProfContainer .attachreq").val([ "N" ]);

        this.model.rjctSaveCheck = true;

        $(".attachCand .rjctRsncb").empty();
        var options = "";
        for (var i = 0; i < this.model.rejectComboLoad.length; i++) {
            options += "<option value='" + this.model.rejectComboLoad[i].code + "'>" + this.model.rejectComboLoad[i].displayField + "</option>";
        }
        $(".attachCand .rjctRsncb").append(options);
        $(".attachCand .rjctRsncb").data("selectBox-selectBoxIt").refresh();
        this.createRejectDetails(0);
    };
    /*
     * This method makes create reject details service calls
     */
    this.createRejectDetails = function(rejectionCode, returnToQP) {
        var applID = CONSTANTS.retailStaffingObj.applicantObj.id;
        var reqNo = reqDet.model.LoadREQDGDltls[0].reqNbr;
        var data = {
            "CreateRejectionRequest" : {
                "RejectionDetail" : {
                    "reqNbr" : reqNo,
                    "candID" : applID,
                    "rejectionCode" : rejectionCode
                }
            }
        };
        if (!returnToQP) {
            this.rsaServices.createRejectDetails({data: JSON.stringify(data)}, this.createRejectDetailsSuccess.bind(this), this.createRejectDetailsFail.bind(this));
        } else {
            this.rsaServices.createRejectDetails({data: JSON.stringify(data)}, this.createRejectDetailsSuccessAndReturn.bind(this), this.createRejectDetailsFail.bind(this));
        }
        
        
    };
    /*
     * This method is success handler of create reject details service call and
     * goes back to Qualified Pool
     */
    this.createRejectDetailsSuccessAndReturn = function(response) {
        this.createRejectDetailsSuccess(response);
        // Calling in the previous method to be more reactive positively.
        this.returnToQualifiedPool();
    };
    /*
     * This method is success handler of create reject details service call
     */
    this.createRejectDetailsSuccess = function(response) {
        response = response.Response;
        if (response.status === CONSTANTS.SUCCESS && !this.model.rjctSaveCheck) {
            this.popup.alert("Details Saved Successfully");
        }
        if ("error" in response && "errorMsg" in response.error) {
            this.popup.alert(response.error.errorMsg);
        }
    };
    /*
     * This method is failure handler of create reject details service call
     */
    this.createRejectDetailsFail = function() {
        // Unblock full page handled in rsaServices
    };
    /*
     * This method displays data from the service calls on the page
     */
    this.displayProfileInfo = function() {
        this.displayPersonalInfo();
        this.displayAssocInfo();
        this.displayEducationInfo();
        this.displayWorkHistoryInfo();
        this.displayJobPref();
        this.displayLangInfo();
    };
    /*
     * This function displays details in personal info form
     *
     * @param N/A
     * @return N/A
     */
    this.displayPersonalInfo = function() {
        var personalInfo = this.model.LoadApplPersonalInfoDtls[0];
        $(".personalInfo .fullname").text(personalInfo.fullName);
        $(".personalInfo .phoneNum").text(personalInfo.phoneNum);
        $(".personalInfo .address1").text(personalInfo.address1);
        $(".personalInfo .address2").text(personalInfo.address2);
        $(".personalInfo .cityStateZip").text(personalInfo.cityStateZip);
        $(".personalInfo .candRefNbr").text(personalInfo.candRefNbr);
        $(".personalInfo .email").text(personalInfo.emailAddress);
        $(".personalDet .value-text").each(function(index,e){
        	if(e.offsetWidth < e.scrollWidth){
        		$(e).attr("title",$(e).text());
        	}
        });

        this.addSpanCheck($(".personalInfo .usaEligCheck.yck"),personalInfo.usaEligibility,"Y");
        this.addSpanCheck($(".personalInfo .usaEligCheck.nck"),personalInfo.usaEligibility,"N");
        this.addSpanCheck($(".personalInfo .ageEligCheck.yck"), personalInfo.over18 , "Y");
        this.addSpanCheck($(".personalInfo .ageEligCheck.nck"), personalInfo.over18 , "N");
        this.addSpanCheck($(".personalInfo .leaveorlaidCheck.yck"),personalInfo.onLeaveOrLaidOff, "Y");
        this.addSpanCheck($(".personalInfo .leaveorlaidCheck.nck"),personalInfo.onLeaveOrLaidOff, "N");
        this.addSpanCheck($(".personalInfo .prevEmpCheck.yck"),personalInfo.preEmployee, "Y");
        this.addSpanCheck($(".personalInfo .prevEmpCheck.nck"),personalInfo.preEmployee, "N");
        $(".personalInfo .location").text(personalInfo.location);
        $(".personalInfo .dateFrom").text(personalInfo.dateFrom);
        $(".personalInfo .dateTo").text(personalInfo.dateTo);
        this.addSpanCheck($(".personalInfo .relForHDCheck.yck"),personalInfo.relWorkingForHD, "Y");
        this.addSpanCheck($(".personalInfo .relForHDCheck.nck"),personalInfo.relWorkingForHD, "N");
        $(".personalInfo .relName").text(personalInfo.relName);

        this.addSpanCheck($(".workHist .militaryExp.yck"), personalInfo.milExperience, "Y");
        this.addSpanCheck($(".workHist .militaryExp.nck"), personalInfo.milExperience, "N");
        $(".workHist .milBranchNameText").text(personalInfo.milBranchName);
        $(".workHist .milDateFromText").text(personalInfo.milDateFrom);
        $(".workHist .milDatToText").text(personalInfo.milDatTo);

        this.addSpanCheck($(".jobPref .jbtypft"), personalInfo.jobFullTime, "Y");
        this.addSpanCheck($(".jobPref .jbtyppt"), personalInfo.jobPartTime, "Y");
        this.addSpanCheck($(".jobPref .jbstperm"), personalInfo.jobPerm, "Y");
        this.addSpanCheck($(".jobPref .jbsttemp"), personalInfo.jobTemp, "Y");
        var wagedes = personalInfo.wageDesired;
        if (wagedes && wagedes !== "0.00") {
            $(".jobPref .wagedesired").text(parseFloat(wagedes).toString() + "/hr");
        } else {
        	this.addSpanCheck($(".jobPref .openneg"), "Y", "Y");
        }
    };
    
    this.addSpanCheck = function(selc, modProp, modVal) {
    	
    	if(modProp === modVal) {
    		selc.removeClass("uncheckSpan").addClass("checkSpan");
    	}
    	//(modProp === modVal)? selc.removeClass("uncheckSpan").addClass("checkSpan") : selc.addClass("uncheckSpan");
    };
    /*
     * This function displays details in associate info form
     *
     * @param N/A
     * @return N/A
     */
    this.displayAssocInfo = function() {
        var associateInfo = this.model.LoadApplAssociateInfoDtls;
        if (associateInfo && associateInfo.length > 0) {
            $(".assocInfo .currStoreText").text(associateInfo[0].currStore);
            $(".assocInfo .currDeptText").text(associateInfo[0].currDept);
            $(".assocInfo .currTittleText").text(associateInfo[0].currTitle);
            $(".assocInfo .currStatusText").text(associateInfo[0].currStatus);
            $(".assocInfo .hireDateText").text(associateInfo[0].hireDate);
        }
    };
    /*
     * This function displays details in education info form
     *
     * @param N/A
     * @return N/A
     */
    this.displayEducationInfo = function() {
        var educationInfo = this.model.LoadApplEducationDtls;
        if (educationInfo && educationInfo.length > 0) {
            for (var i = 0; i < educationInfo.length && i <= 2; i++) {
                $(".eduInfo ._" + i + "SchoolName").text(educationInfo[i].schoolName);
                $(".eduInfo ._" + i + "Education").text(educationInfo[i].education);
                this.addSpanCheck($(".eduInfo ._" + i + "Graduate.yck"), ((educationInfo[i].graduate !== "" || educationInfo[i].graduate || educationInfo[i].graduate === "true") ? "Y" : "") , "Y");
                this.addSpanCheck($(".eduInfo ._" + i + "Graduate.nck"), ((educationInfo[i].graduate !== "" || educationInfo[i].graduate || educationInfo[i].graduate === "true") ? "Y" : "N") , "N");
            }
        }
    };
    /*
     * This function displays details in work history info form
     *
     * @param N/A
     * @return N/A
     */
    this.displayWorkHistoryInfo = function() {
        var workHistory = this.model.LoadApplWorkHistoryDtls;
        if (workHistory && workHistory.length > 0) {
            for (var i = 0; i < workHistory.length && i <= 3; i++) {
                $(".workHist ._" + i + "Company").text(workHistory[i].company);
                $(".workHist ._" + i + "Position").text(workHistory[i].position);
                $(".workHist ._" + i + "Location").text(workHistory[i].location);
                $(".workHist ._" + i + "Supervisor").text(workHistory[i].supervisor);
                $(".workHist ._" + i + "DateFrom").text(workHistory[i].comDateFrom);
                $(".workHist ._" + i + "DateTo").text(workHistory[i].comDateTo);
                $(".workHist ._" + i + "PayAtLeaving").text(parseFloat(workHistory[i].payAtLeaving).toString());
                $(".workHist ._" + i + "SupervisorTitle").text(workHistory[i].supervisorTitle);
                $(".workHist ._" + i + "JobDescription").text(workHistory[i].jobDescription);
                $(".workHist ._" + i + "ReasonLeaving").text(workHistory[i].reasonLeaving);
            }
        }
    };
    /*
     * This function selectsthe preferred time and day in job schedule preference grid
     *
     * @param N/A
     * @return N/A
     */
    this.displayJobPref = function() {
        for (var i = 0; i < this.model.applicantSchdPrefArr.length; i++) {
            if (this.model.applicantSchdPrefArr[i] || this.model.applicantSchdPrefArr[i] === "true") {
                $('.jobPref #schdPref' + i).removeClass("uncheckSpan").addClass("checkSpan");
            }
        }
    };
    /*
     * This function checks whether passed array is valid
     *
     * @param languageArray
     * @return true/false
     */
    this.isLangArrayValid = function(languageArray){
        return languageArray && languageArray.length > 0;
    };
    /*
     * This function selects the known languages in the language grid
     *
     * @param valid
     * @param languageArray
     * @param isObjArr
     * @return N/A
     */
    this.setLangSkillGrid = function(valid,languageArray,isObjArr){
        if (valid) {
            //var values = [];
            for (var i = 0; i < languageArray.length; i++) {
                var langObj = _.where(this.model.langDet, {
                    "shortDesc" : (isObjArr)?languageArray[i].language:languageArray[i]
                });
                if(langObj.length > 0){
                   // values.push(langObj[0].langCode);
                   $('.langskillablcontainer .gridCheckBox[value="'+ langObj[0].langCode.toString() +'"]').removeClass('uncheckSpan').addClass('checkSpan');
                }
            }
            //$(".langskillablcontainer .gridCheckBox").val(values);
            
        }
        
    };
    
    
    /*
     * This function displays language info grid
     *
     * @param N/A
     * @return N/A
     */
    this.displayLangInfo = function() {
        var applType = this.model.LoadApplPersonalInfoDtls[0].applType;
        var assoLanguage = "";
        var assoLanguageSkill = "";
        var languageArray = this.model.LoadApplLanguageDtls;
        var isAssoLanguage = false;
        var valid = false;
        var assoLanguageSkillArray = [];
        if (applType === "AS") {
            valid = this.isLangArrayValid(languageArray);
            if (valid) {
                assoLanguage = languageArray[0].language;
                assoLanguageSkill = "";
                isAssoLanguage = false;
                assoLanguageSkillArray = [];

                if (assoLanguage && assoLanguage.indexOf("AS,") >= 0) {
                    isAssoLanguage = true;
                    assoLanguageSkill = assoLanguage.toString().substring(assoLanguage.indexOf(",") + 1, assoLanguage.lastIndexOf(","));
                } else {
                    isAssoLanguage = false;
                }
            }
        }
        if (!isAssoLanguage) {
        	if (languageArray && languageArray.length > 0) {
        		var lstToDisplay = "<ul>";
        		for (var i = 0; i < languageArray.length; i++) {
        			lstToDisplay += "<li class='lstSkill'>" + languageArray[i].language + "</li>";
        		}
        		lstToDisplay += "</ul>";
        	
        		$("#LangDisplay").append(lstToDisplay);
        	}
            
            //valid = this.isLangArrayValid(languageArray);
            //this.setLangSkillGrid(valid,languageArray,true);
        }
        if (isAssoLanguage) {
            assoLanguageSkillArray = assoLanguageSkill.split(",");
        	if (assoLanguageSkillArray && assoLanguageSkillArray.length > 0) {
        		var lstToDisplay = "<ul>";
        		for (var i = 0; i < assoLanguageSkillArray.length; i++) {
        			lstToDisplay += "<li class='lstSkill'>" + assoLanguageSkillArray[i] + "</li>";
        		}
        		lstToDisplay += "</ul>";
        	
        		$("#LangDisplay").append(lstToDisplay);
        	}            
            
            //valid = this.isLangArrayValid(assoLanguageSkillArray);
            //this.setLangSkillGrid(valid,assoLanguageSkillArray,false);
        }
    };

    /*
     * This method takes us back to qualified pool after saving
     */
    this.returnToQualifiedPool = function() {
        // Need to save applType in order select the correct call below

        this.model.LoadApplPersonalInfoDtls = [];
        this.model.LoadApplAssociateInfoDtls = [];
        this.model.LoadApplAssociateReviewDtls = [];
        this.model.LoadApplAssociatePrevPosDtls = [];
        this.model.LoadApplEducationDtls = [];
        this.model.LoadApplWorkHistoryDtls = [];
        this.model.LoadApplJobPrefDtls = [];
        this.model.LoadApplJobPrefDtls2 = [];
        this.model.LoadApplLanguageDtls = [];
        this.model.LoadApplPhnScrnDtls = [];
        this.model.LoadApplStatusHistoryDtls = [];
        this.model.LoadApplAppHistoryDtls = [];
        this.model.LoadApplAppHistoryDtls2 = [];

        CONSTANTS.backToQualifiedPool = "BackToQualifiedPool";
        this.navigateToReqDetScreen();
        this.showApplUnavail();

    };
    /*
     * This function attaches the applicant to requisition
     *
     * @param N/A
     * @return N/A
     */
    this.attachAndReturn = function() {
        var applToReqArray = [];
        var personalInfo = this.model.LoadApplPersonalInfoDtls[0];

        var applToReqInfoStr = reqDet.model.LoadREQDGDltls[0].store + "%?%" + reqDet.model.LoadREQDGDltls[0].reqNbr + "%?%" + personalInfo.applID + "%?%" + personalInfo.fullName + "%?%" + personalInfo.applType;
        applToReqArray.push(applToReqInfoStr);
        this.attachApplToReq(applToReqArray);
    };
    /*
     * This function attaches the applicant to requisition
     *
     * @param N/A
     * @return N/A
     */
    this.attachApplToReq = function(applToReqArray) {
        var inputData = {};
        var inputStr = [];
        for (var i = 0; i < applToReqArray.length; i++) {

            var applInfoStr = applToReqArray[i].toString();
            var applInfoArray = applInfoStr.split("%?%");
            var storeNum = applInfoArray[0];
            var reqNum = applInfoArray[1];
            var applId = applInfoArray[2];
            var applName = applInfoArray[3];
            var applType = applInfoArray[4];
            var input = {
                "StoreNumber" : storeNum,
                "ReqNumber" : reqNum,
                "ApplId" : applId,
                "ApplName" : applName,
                "ApplType" : applType
            };
            inputStr.push(input);
        }
        inputData = {
            "ApplToReqRequest" : {
                "ApplToReqList" : {

                }
            }
        };
        if (inputStr.length === 1) {
            inputData.ApplToReqRequest.ApplToReqList.ApplToReq = inputStr[0];
        } else if (inputStr.length > 1) {
            inputData.ApplToReqRequest.ApplToReqList.ApplToReq = inputStr;
        }
        this.rsaServices.retailStaffingAttachApplToReq({data:JSON.stringify(inputData)}, this.retailStaffingAttachApplToReqSuccess.bind(this), this.retailStaffingAttachApplToReqFail.bind(this));
        this.afterAttachToReq();
    };
    /*
     * This function is a success handler of attach applicant to requisition service call
     *
     * @param response
     * @return N/A
     */
    this.retailStaffingAttachApplToReqSuccess = function(response) {
        response = response.Response;
        this.model = _.extend(this.model, new attachApplToReq(response));
        if ("error" in response && "errorMsg" in response.error) {
            this.popup.alert(response.error.errorMsg);
        }
        // removed this and called in previous method itself.
        //this.afterAttachToReq();
    };
    /*
     * This function is a failure handler of attach applicant to requisition service call
     *
     * @param N/A
     * @return N/A
     */
    this.retailStaffingAttachApplToReqFail = function() {
        // Unblock full page handled in rsaServices
        this.afterAttachToReq();
    };
    /*
     * This function removes the attached applicant/associate from the QP list
     *
     * @param N/A
     * @return N/A
     */
    this.afterAttachToReq = function() {
        var personalInfo = this.model.LoadApplPersonalInfoDtls[0];
        if (personalInfo.applType === "AP") {
            // Removes the Applicant from the Master Array
            reqDet.model.LoadMasterApplicantPoolDgDtls = this.removeApplFromList(reqDet.model.LoadMasterApplicantPoolDgDtls, personalInfo.applID);
            // Removes the Applicant from the Sub-Array
            reqDet.model.LoadApplicantPoolDgDtls = this.removeApplFromList(reqDet.model.LoadApplicantPoolDgDtls, personalInfo.applID);
        } else if (personalInfo.applType === "AS") {
            // Removes the Associate from the Master Array
            reqDet.model.LoadMasterAssociatePoolDgDtls = this.removeApplFromList(reqDet.model.LoadMasterAssociatePoolDgDtls, personalInfo.applID);
            // Removes the Associate from the Sub-Array
            reqDet.model.LoadAssociatePoolDgDtls = this.removeApplFromList(reqDet.model.LoadAssociatePoolDgDtls, personalInfo.applID);
        }
        reqDet.model.candidateCt++;
        // remove candidates from cache also.
        try {
        	if(CONSTANTS.cacheRequisitionDetailsFlg) {
            	if (personalInfo.applType === "AP") {
            		CONSTANTS.attachedPersonType = "AP";
            		CONSTANTS.cache_getRetailStaffingViewApplicantPool.Response.candidates = this.removeApplFromList(CONSTANTS.cache_getRetailStaffingViewApplicantPool.Response.candidates,personalInfo.applID);
        		} else if (personalInfo.applType === "AS") {
        			CONSTANTS.attachedPersonType = "AS";
        			CONSTANTS.cache_getRetailStaffingViewAssociatePool.Response.candidates = this.removeApplFromList(CONSTANTS.cache_getRetailStaffingViewAssociatePool.Response.candidates,personalInfo.applID);
        		}
        	}
        }
        catch(ex)
        {
        	console.log("Problem in caching.");
        }
        CONSTANTS.applicantAttachedtoREQ = true;
        this.returnToQualifiedPool();
    };

    /*
     * This function removes the oject with passed id from the passed list
     *
     * @param list
     * @param id
     * @return list
     */
    this.removeApplFromList = function(list, id) {
        list = _.reject(list, function(obj) {
            return parseInt(obj.id) === parseInt(id);
        }.bind(this));
        return list;
    };
    /*
     * This function navigates to requisition details screen
     *
     * @param N/A
     * @return N/A
     */
    this.navigateToReqDetScreen = function() {
        CONSTANTS.APPL_PROF_TO_REQ_DET = true;
        $.get('app/RSARequisitionDetails/view/requisitionDetails.html', CONSTANTS.retailStaffingObj.setContent);
    };
    /*
     * This function shows if appl is unavailable
     *
     * @param N/A
     * @return N/A
     */
    this.showApplUnavail = function() {

        var applUnavailNameStr = "";

        if (reqDet.model._hasDuplicateAppl === "true") {

            for (var i = 0; i < reqDet.model.LoadApplUnavailInfo.length; i++) {
                applUnavailNameStr += reqDet.model.LoadApplUnavailInfo[i].applUnavailName;
            }

            this.popup.alert("Applicant(s) " + applUnavailNameStr + " not available for this requisition.");

        }
        reqDet.model._hasDuplicateAppl = "";
        reqDet.model.LoadApplUnavailInfo = "";

    };
    /*
     * This function makes service call to get minimum phone screen details
     *
     * @param phnScrNbr
     * @return N/A
     */
    this.showPhoneDetails = function(phnScrNbr) {
        this.model.getPhoneScrnDispositionStatusDtls = [];
        this.model.phnScreenDtls = [];
        this.model.minResponseList = [];
        this.rsaServices.getPhoneScreenDetailsPopUp({data:phnScrNbr}, this.getPhoneScreenDetailsPopUpSuccess.bind(this), this.getPhoneScreenDetailsPopUpFail.bind(this));
    };
    /*
     * This function is a success handler of get minumum phone screen details
     *
     * @param response
     * @return N/A
     */
    this.getPhoneScreenDetailsPopUpSuccess = function(response) {
        response = response.Response;
        this.model = _.extend(this.model, new getPhoneScreenMinimumDetails(response));
        if ("error" in response && "errorMsg" in response.error) {
            this.popup.alert(response.error.errorMsg);
        }
        this.showPhoneScreenDetailsPopup();
    };
    this.getPhoneScreenDetailsPopUpFail = function() {
        // Unblock full page handled in rsaServices
    };
    /*
     * This function shows minumum phone screen details popup
     *
     * @param N/A
     * @return N/A
     */
    this.showPhoneScreenDetailsPopup = function() {
        $("#phnScrDetpopup").modal({
            "backdrop" : "static",
            "keyboard" : false
        });
        $("#phnScrDetpopup").draggable({
            handle : ".modal-header"
        });
        $("#phnScrDetpopup .phnScrDisp").empty();
        for (var i = 0; i < this.model.getPhoneScrnDispositionStatusDtls.length; i++) {
            $("#phnScrDetpopup .phnScrDisp")
                    .append("<option value='" + this.model.getPhoneScrnDispositionStatusDtls[i].status + "'>" + this.model.getPhoneScrnDispositionStatusDtls[i].desc + "</option>");
        }
        //this.blockPopUpElements();
        this.displayPopUpData();
    };
    /*
     * This function displays data in minumum phone screen details popup
     *
     * @param N/A
     * @return N/A
     */
    this.displayPopUpData = function() {
        $("#phnScrDetpopup #contacthistTextarea").val("");
        $("#phnScrDetpopup #phnDtlResp").val("");
        $("#phnScrDetpopup .phnScrner").text("");
        for (var i = 0; i < this.model.minResponseList.length; i++) {
            $("#phnScrDetpopup .minReq" + i).val(this.model.minResponseList[i].minFlag);
        }
        $("#phnScrDetpopup #contacthistTextarea").val(this.model.phnScreenDtls[0].contactHist);
        $("#phnScrDetpopup #phnDtlResp").val(this.model.phnScreenDtls[0].detailResp);
        if(this.model.phnScrnTimeList.length > 0){
            if(parseInt(this.model.phnScrnTimeList[0].year) < 70){
                this.model.phnScrnTimeList[0].year = 2000 + parseInt(this.model.phnScrnTimeList[0].year);
            }
            else{
                this.model.phnScrnTimeList[0].year = new Date(this.model.phnScrnTimeList[0].year, this.model.phnScrnTimeList[0].month-1, this.model.phnScrnTimeList[0].day).getFullYear();
            }
            $("#phnScrDetpopup .phnScrDate").text(this.model.phnScrnTimeList[0].month + "/" + this.model.phnScrnTimeList[0].day + "/" + this.model.phnScrnTimeList[0].year);
            $("#phnScrDetpopup .phnScrTime").text(this.model.phnScrnTimeList[0].hourSlot + ":" + this.model.phnScrnTimeList[0].minute + " " + this.model.phnScrnTimeList[0].timeSlot);
        }
        else{
            $("#phnScrDetpopup .phnScrDate").text("");
            $("#phnScrDetpopup .phnScrTime").text("");
        }
        $("#phnScrDetpopup .phnScrner").text(this.model.phnScreenDtls[0].phnScreener);

        if (this.model.getPhoneScrnDispositionStatusDtls && this.model.phnScreenDtls[0].phoneScreenDispositionCode >= 0) {
            $("#phnScrDetpopup .phnScrDisp").val(this.model.phnScreenDtls[0].phoneScreenDispositionCode);
        }
    };
    /*
     * This function blocks fields in minumum phone screen details popup
     *
     * @param N/A
     * @return N/A
     */
    this.blockPopUpElements = function() {
    	//$("#phnScrDetpopup .selectCont .blockUI").remove();
    	//$("#phnScrDetpopup .selectCont").append('<div class="blockUI blockOverlay" style="margin: 0px; padding: 0px; border: currentColor; border-image: none; left: 0px; top: 0px; width: 100%; height: 100%; position: absolute; z-index: 100; cursor: auto; opacity: 0.6; background-color: rgb(255, 255, 255);"></div>');
        /*$("#phnScrDetpopup .selectCont").block({
            message : null,
            overlayCSS : {
                backgroundColor : "#FFF",
                opacity : 0.6,
                cursor : "auto"
            },
            ignoreIfBlocked : false,
            baseZ : 100
        });*/
    };
    /*
     * This function shows confirmation popup to close window
     *
     * @param N/A
     * @return N/A
     */
    this.onCloseBtnClick = function(){
        if(this.model.rjctSaveCheck){
            this.popup.warnUnsavedData(CONSTANTS.RJCT_DTLS_UNSAVED_PART_1, CONSTANTS.RJCT_DTLS_UNSAVED_PART_2, this.onCloseButtonOK.bind(this), this.handleClose.bind(this), 150, 400);
        }
        else{
            this.onCloseButtonOK();
        }
    };
    /*
     * This function closes the window
     *
     * @param N/A
     * @return N/A
     */
    this.onCloseButtonOK = function(){
        window.close();
    };
}

//# sourceURL=applicantProfile.js
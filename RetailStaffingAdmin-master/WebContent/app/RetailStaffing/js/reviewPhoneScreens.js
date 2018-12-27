/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: reviewPhoneScreensDetails.js
 * Application: Retail Staffing Admin
 *
 */
function reviewPhoneScreensDetails() {
    /*
     * This method is used to initialize the events on the load of the page for
     * the navigation buttons and the submit and save buttons
     */
    this.initialize = function () {
        $("#messageBar").html("REVIEW PHONE SCREENS");
        $("title").text("RetailStaffing.html");
        $('#ui-datepicker-div').hide();
        this.popup = new rsaPopup();
        $('.modal-backdrop').empty().remove();
        CONSTANTS.reviewPhoneScreensDetailsObj = this;
        CONSTANTS.reviewPhoneScreensDetailsObj.isPageEditedFlag = false;
        CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage = "home";
        this.booleanTrue = true;
        var data = CONSTANTS.retailStaffingObj.phoneScreenDataValue;
        $("#save").on("click",function (e){
        	UTILITY.RemoveQS();
            CONSTANTS.reviewPhoneScreensDetailsObj.onSaveButtonClicked(e);
        });
        $("#goHome").off("click");
        $("#goHome").show().on("click",function(){
        	UTILITY.RemoveQS();
            CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage = "home";
            CONSTANTS.reviewPhoneScreensDetailsObj.displaySavePop();
        });
        $(".navCloseBtn").off("click");
        $(".navCloseBtn").on("click",function(){
            CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage = "close";
            CONSTANTS.reviewPhoneScreensDetailsObj.displaySavePop();
        });
        $("#submit").on("click",function (e){
        	UTILITY.RemoveQS();
            CONSTANTS.reviewPhoneScreensDetailsObj.onSaveButtonClicked(e);
        });
        $("#reviewPhoneScreensDetails").on("click","select",function (){
            CONSTANTS.reviewPhoneScreensDetailsObj.isPageEditedFlag = true;
        });
         var callbackFunction = $.Callbacks('once');
         callbackFunction.add(CONSTANTS.reviewPhoneScreensDetailsObj.onFetchreviewPhoneScreensDetailsSuccess);
         RSASERVICES.getRevPhnScrnDetails(callbackFunction,data);
    };
        /*
         * This method is invoked when the review phone screen details are
         * fetched successfully and the values are being loaded in the dom
         */
    this.onFetchreviewPhoneScreensDetailsSuccess = function (response) {
        $.unblockUI();
        var mainDetails1 = [];
        if(response.Response && response.Response.status && response.Response.status === "SUCCESS")
        {
            if (response.Response.RequisitionDetailList)
                {
                if(response.Response.RequisitionDetailList.RequisitionDetail.constructor !== Array)
                    {
                    mainDetails1.push(response.Response.RequisitionDetailList.RequisitionDetail);
                    }
                else {
                    mainDetails1 = response.Response.RequisitionDetailList.RequisitionDetail;
                }
                var mainDetails = mainDetails1[0];
                $("#requisitionValue").html("<a href='#' class='reqDetailsScreen' >"+mainDetails.reqNbr+"</a>");
                $("#strNumValue").text(mainDetails.store);
                $("#ftValue").text(mainDetails.ft);
                $("#departmentValue").text(mainDetails.dept);
                $("#openingsValue").text(mainDetails.openings);
                $("#ptValue").text(mainDetails.pt);
                $("#jobCodeValue").text(mainDetails.job);
                $("#interviewsValue").text(mainDetails.reqNumInterviews);
                $("#scrTypeValue").text(mainDetails.scrTyp);
                $("#jobTitleValue").text(mainDetails.jobTtl);
                var tempDate = mainDetails.fillDt.split("-");
                $("#dateNeededValue").text(tempDate[1]+"/"+tempDate[2]+"/"+tempDate[0]);
                $("#seasonalTempValue").text(mainDetails.sealTempJob);
                $("#rscToManageValue").text(mainDetails.rscToManageFlg);
                $("#rscToScheduleValue").text(mainDetails.rscSchdFlg);
                }
            $(".reqDetailsScreen").on("click",function(e){
                  CONSTANTS.reviewPhoneScreensDetailsObj.navigateToReqScreenDetails(e);
                  });
            CONSTANTS.reviewPhoneScreensDetailsObj.buildStatusLists(response);
            CONSTANTS.reviewPhoneScreensDetailsObj.buildTreeStructure(response);
        }
        else
            {
            if(response.Response.error.errorMsg){
                CONSTANTS.reviewPhoneScreensDetailsObj.popup.alert(response.Response.error.errorMsg);
                }
            }
    };
        /*
         * This method is used to build the collapsable tree structure with the
         * active detail list and inactive detail list
         */
    this.buildTreeStructure = function (response) {
        if(response.Response.staffingDetails) {
            $("#queWebReqNumberValue").text(response.Response.staffingDetails.requestNbr);
            $("#referralsValue").text(response.Response.staffingDetails.Referals);
            }
            if(response.Response.ITIDetailList && response.Response.ITIDetailList.PhoneScreenIntrwDetail)
                {
            CONSTANTS.reviewPhoneScreensDetailsObj.generateCollapsableTree(response.Response.ITIDetailList.PhoneScreenIntrwDetail,"phoneScreensContent");
                }
            if(response.Response.ITIDetailListInactive && response.Response.ITIDetailListInactive.PhoneScreenIntrwDetail)
                {
            CONSTANTS.reviewPhoneScreensDetailsObj.generateCollapsableTree(response.Response.ITIDetailListInactive.PhoneScreenIntrwDetail,"inActphoneScreensContent");
                }
    };
        /* This is to load the status lists in the corresponding drop downs */
    this.buildStatusLists = function (response)
    {
        if(response.Response.StatusList)
        {
        if(response.Response.StatusList.requisitionStats){
    CONSTANTS.reviewPhoneScreensDetailsObj.requisitionStatsOptions = CONSTANTS.reviewPhoneScreensDetailsObj.buildOptions(_.sortBy(response.Response.StatusList.requisitionStats[0].status, 'statusDescription'));
    $("#requisitionStatusValue").append(CONSTANTS.reviewPhoneScreensDetailsObj.requisitionStatsOptions);
    for(var i=0;i<response.Response.StatusList.requisitionStats[0].status.length;i++)
    	{
    	if (response.Response.RequisitionDetailList)
        {
    		var mainDetails1 = [];
        if(response.Response.RequisitionDetailList.RequisitionDetail.constructor !== Array)
            {
            mainDetails1.push(response.Response.RequisitionDetailList.RequisitionDetail);
            }
        else {
            mainDetails1 = response.Response.RequisitionDetailList.RequisitionDetail;
        }
        var mainDetails = mainDetails1[0];
        var requisitionStatusCode = parseInt(mainDetails.requisitionStatusCode);
        if(parseInt(requisitionStatusCode) === 0)
        	{
        	requisitionStatusCode = 1;
        	}
        var actualArray = _.where(response.Response.StatusList.requisitionStats[0].status, {displayStatusCode: parseInt(requisitionStatusCode)});
        $("#requisitionStatusValue option[value*='"+actualArray[0].displayStatusCode+"']").attr("selected","selected");
        }
    	}
    $("#requisitionStatusValue").selectBoxIt();
    $("#requisitionStatusValue").data("selectBox-selectBoxIt").refresh();
        }
        if(response.Response.StatusList.interviewStats){
    CONSTANTS.reviewPhoneScreensDetailsObj.interviewStatsOptions = CONSTANTS.reviewPhoneScreensDetailsObj.buildOptions(response.Response.StatusList.interviewStats[0].status);
        }
        if(response.Response.StatusList.materialsStats){
    CONSTANTS.reviewPhoneScreensDetailsObj.materialsStatsOptions = CONSTANTS.reviewPhoneScreensDetailsObj.buildOptions(response.Response.StatusList.materialsStats[0].status);
        }
        if(response.Response.StatusList.phoneScreenStats){
    CONSTANTS.reviewPhoneScreensDetailsObj.phoneScreenStatsOptions = CONSTANTS.reviewPhoneScreensDetailsObj.buildOptions(response.Response.StatusList.phoneScreenStats[0].status);
        }
        }
    };
    this.buildOptions = function (data1)
    {
        var data =[];
        if(data1.constructor === Array)
            {
            data = data1;
            }
        else
            {
            data.push(data1);
            }
        var tempOptions = "";
        for(var i=0;i<data.length;i++)
        {
            tempOptions = tempOptions + "<option value='"+data[i].code+"'>"+data[i].statusDescription+"</option>";
        }
        return tempOptions;
    };
        /*
         * This method is used to display the save pop up if the user performs
         * any change in the screen and tries to navigate to another screen with
         * out saving data of the current screen
         */
    this.displaySavePop = function()
    {
         $("#saveWarningpopup").on('shown.bs.modal', function () {
             var tempMargintp = parseInt((($(window).height()/2 - $("#saveWarningpopup .modal-content").outerHeight()/2)));
             var tempMargintp1 = tempMargintp.toString() + "px";
             $("#saveWarningpopup .modal-content").css({"margin-top":tempMargintp1});
         });
        $(window).unbind();
        $(window).resize(function() {
            var tempMargintop = parseInt((($(window).height()/2 - $("#saveWarningpopup .modal-content").outerHeight()/2)));
            var tempMargintop1 = tempMargintop.toString() + "px";
            $("#saveWarningpopup .modal-content").css({"margin-top":tempMargintop1});
        });
        if( CONSTANTS.reviewPhoneScreensDetailsObj.isPageEditedFlag ===  CONSTANTS.reviewPhoneScreensDetailsObj.booleanTrue){
        $('#saveWarningpopup').modal({
            backdrop : 'static',
            keyboard : false
        });
        $("#saveWarningpopup").draggable({
            handle: ".modal-header"
        });
        $("#saveWarningpopup").modal("show");
        $("#saveWarningCancelbutton").attr("data-dismiss","modal");
        }
        else
            {
            CONSTANTS.reviewPhoneScreensDetailsObj.navigateToHomeScreen();
            }
    };
        /*
         * This method used for loading the content of the tree structure
         * dynamically
         */
    this.generateCollapsableTree = function (tempData1,divId)
    {
        var tempData = [];
        if(tempData1.constructor === Array)
            {
            tempData = tempData1;
            }
        else
            {
            tempData.push(tempData1);
            }
        var mainButton ="<div class='row noMarginLeftRight'><div class='col-xs-12'><span class='plusButton mainPlusButton'>+</span><label style='text-indent:10px'>View All</label></div></div>";
        var data = [];
        if(tempData.constructor !== Array)
          {
          data.push(tempData);
          }
      else
          {
          data=tempData;
          }
        var subCollapsableDivs = "";
        var lastRowId = 0;
        if($("[id*='subPlusButton']").length > 0)
            {
        var tempVar = $("[id*='subPlusButton']").last().attr("id");
        lastRowId = parseInt(tempVar.split("subPlusButton")[1]);
            }
        var i=0;
        for(var j=(lastRowId+1) ;j<(data.length+lastRowId+1);j++)
            {

            subCollapsableDivs = CONSTANTS.reviewPhoneScreensDetailsObj.buildCollapseableSection(subCollapsableDivs,data,i);
            i++;
            }
        var appenedDiv = mainButton + subCollapsableDivs;
        $("[id='"+divId+"']").empty().append(appenedDiv);
        $(".candidateDetailsScreen").off("click");
         $(".candidateDetailsScreen").on("click",function(e){
              CONSTANTS.reviewPhoneScreensDetailsObj.navigateToCandidateDetails(e);
          });
         $(".phoneScreenDetailsScreen").off("click");
         $(".phoneScreenDetailsScreen").on("click",function(e){
              CONSTANTS.reviewPhoneScreensDetailsObj.navigateToPhoneScreenDetails(e);
          });
        this.buildOptionsInSelectEvent(divId);
        for(var l=0 ;l<data.length;l++){
            this.assignSectionValue(l,divId,data);
        }
        $("[id='"+divId+"'] [id*='subPlusButton']").off('click');
        $("[id='"+divId+"'] [id*='subPlusButton']").on('click',function(e){
            var tempVar = $(e.currentTarget).attr("id");
            var rowId = tempVar.split("subPlusButton")[1];
            var tempMainDivId = "";
            if($(e.currentTarget).parents("#phoneScreensContent").length > 0)
                {
                tempMainDivId = "phoneScreensContent";
                }
            else
                {
                tempMainDivId = "inActphoneScreensContent";
                }
            if($("[id='"+tempMainDivId+"'] [id='subCollapsableDiv"+rowId+"']:visible").length === 0)
                {
            $("[id='"+tempMainDivId+"'] [id='subCollapsableDiv"+rowId+"']").slideDown();
            $(e.currentTarget).text("-");
                }
            else
                {
                $("[id='"+tempMainDivId+"'] [id='subCollapsableDiv"+rowId+"']").slideUp();
                $(e.currentTarget).text("+");
                }
        });
        $("[id='"+divId+"'] .mainPlusButton").off('click');
        $("[id='"+divId+"'] .mainPlusButton").on('click',function(e){
            var tempMainDivId = "";
            if($(e.currentTarget).parents("#phoneScreensContent").length > 0)
                {
                tempMainDivId = "phoneScreensContent";
                }
            else
                {
                tempMainDivId = "inActphoneScreensContent";
                }
            if($(e.currentTarget).text() === '+')
                {
                $(e.currentTarget).text('-');
                $(e.currentTarget).siblings("label").text("Hide All");
                $("[id='"+tempMainDivId+"'] [id*='subPlusButton']").text('-');
                $("[id='"+tempMainDivId+"'] [id*='subCollapsableDiv']").slideDown();
                }
            else
                {
                $(e.currentTarget).text('+');
                $(e.currentTarget).siblings("label").text("View All");
                $("[id='"+tempMainDivId+"'] [id*='subPlusButton']").text('+');
                $("[id='"+tempMainDivId+"'] [id*='subCollapsableDiv']").slideUp();
                }
        });
    };
        /* This method is used to load the options to the select box */
    this.buildOptionsInSelectEvent = function (divId) {
        $("[id='"+divId+"'] [id*='interviewScreenStatus']").empty().append(CONSTANTS.reviewPhoneScreensDetailsObj.interviewStatsOptions);
        $("[id='"+divId+"'] [id*='phoneScreenStatus']").empty().append(CONSTANTS.reviewPhoneScreensDetailsObj.phoneScreenStatsOptions);
        $("[id='"+divId+"'] [id*='intvwMtrlStatus']").empty().append(CONSTANTS.reviewPhoneScreensDetailsObj.materialsStatsOptions);
        $("[id='"+divId+"'] [id*='interviewScreenStatus']").selectBoxIt();
        $("[id='"+divId+"'] [id*='phoneScreenStatus']").selectBoxIt();
        $("[id='"+divId+"'] [id*='intvwMtrlStatus']").selectBoxIt();
        $("#phoneScreensContent").off("change","[id*='phoneScreenStatus']");
        $("#phoneScreensContent").on("change","[id*='phoneScreenStatus']",function (e){
            var idNumber = $(e.currentTarget).attr("id").split("phoneScreenStatus");
            if($(e.currentTarget).val() === "3" || $(e.currentTarget).val() === "4"){
            $("[id='interviewScreenStatus"+idNumber[1]+"']").removeAttr("disabled");
            }
            else
                {
                $("[id='interviewScreenStatus"+idNumber[1]+"']").attr("disabled","disabled");
                }
            $("[id='interviewScreenStatus"+idNumber[1]+"']").data("selectBox-selectBoxIt").refresh();
        });
        $("#phoneScreensContent").off("change","[id*='interviewScreenStatus']");
        $("#phoneScreensContent").on("change","[id*='interviewScreenStatus']",function (e){
            var idNumber1 = $(e.currentTarget).attr("id").split("interviewScreenStatus");
            if($(e.currentTarget).val() === "9" || $(e.currentTarget).val() === "11" || $(e.currentTarget).val() === "12"){
            $("[id='intvwMtrlStatus"+idNumber1[1]+"']").removeAttr("disabled");
            }
            else
                {
                $("[id='intvwMtrlStatus"+idNumber1[1]+"']").attr("disabled","disabled");
                }
            $("[id='intvwMtrlStatus"+idNumber1[1]+"']").data("selectBox-selectBoxIt").refresh();
        });
        if($("#inActphoneScreensContent select").length > 0){
        $("#inActphoneScreensContent select").attr("disabled","disabled");
        $("#inActphoneScreensContent select").data("selectBox-selectBoxIt").refresh();
        }
    };
        /*
         * Generating the dom as a string and appending to the corresponding
         * parent
         */
    this.buildCollapseableSection = function (subCollapsableDivs,data,i) {
        var minReqStatus = "";
        if(data[i].ynstatus)
            {
            if(data[i].ynstatus.toString() === "12")
                {
                minReqStatus="PROCEED";
                }
            else if(data[i].ynstatus.toString() === "5"){
                minReqStatus="DO NOT PROCEED";
            }
            }
        data[i].phnScrnTime.minute = (data[i].phnScrnTime.minute) ? (data[i].phnScrnTime.minute) : "0";
    var subCollapsableDivs1 = subCollapsableDivs +"<div class='mainRows noMarginLeftRight row "+((i % 2 === 0)?"even":"odd")+"' >" +
        "<div class='text-center subPlusButtonContainer' ><span class='plusButton' id='subPlusButton"+i+"'>+</span></div>" +
        "<div class='col-xs-11'><div class='row'>" +
"<div class='col-xs-2'>" +
"<div class='col-xs-7 text-right'><label>Screen #:</label></div>" +
"<div class='col-xs-5 text-left'><span id='screenValue"+i+"'><a href='#' class='phoneScreenDetailsScreen'>"+data[i].itiNbr+"</a></span></div>" +
"</div>" +
"<div class='col-xs-2'>" +
"<div class='col-xs-7 text-right'><label>Candidate ID:</label></div>" +
"<div class='col-xs-5 text-left'><span id='candidateId"+i+"'><a href='#' data-assoId='"+data[i].cndtNbr+"' class='candidateDetailsScreen'>"+data[i].candRefNbr+"</a></span></div>" +
"</div>" +
"<div class='col-xs-3'>" +
"<div class='col-xs-5 text-right'><label>Name:</label></div>" +
"<div class='col-xs-7 text-left'><span id='name"+i+"'>"+data[i].name+"</span></div>" +
"</div>" +
"<div class='col-xs-4'>" +
"<div class='col-xs-4 text-right'><label>Phone Scrn Status:</label></div>" +
"<div class='col-xs-8 text-left'><select id='phoneScreenStatus"+i+"'></select></div>" +
"</div></div>" +
"<div class='row'>" +
"<div class='col-xs-2'>" +
"<div class='col-xs-7 text-right'><label>Int/Ext:</label></div>" +
"<div class='col-xs-5 text-left'><span id='intExt"+i+"'>"+data[i].internalExternal+"</span></div>" +
"</div>" +
"<div class='col-xs-2'>" +
"<div class='col-xs-7 text-right'><label>Offered:</label></div>" +
"<div class='col-xs-5 text-left'><span id='offered"+i+"'>"+data[i].offered+"</span></div>" +
"</div>" +
"<div class='col-xs-3'>" +
"<div class='col-xs-5 text-right'><label>Offered Status:</label></div>" +
"<div class='col-xs-7 text-left'><span id='offeredStatus"+i+"'>"+data[i].offerStatus+"</span></div>" +
"</div>" +
"<div class='col-xs-4'>" +
"<div class='col-xs-4 text-right'><label>Interview Status:</label></div>" +
"<div class='col-xs-8 text-left'><select id='interviewScreenStatus"+i+"'></select></div>" +
"</div>" +
"</div>" +
"<div class='row' style='display:none;' id='subCollapsableDiv"+i+"'>" +
"<div class='col-xs-12'>" +
"<div class='row'>" +
"<div class='col-xs-4'>" +
"<div class='col-xs-6 text-right'><label>Min. Requirement Status:</label></div><div class='col-xs-6 text-left'><span id='minReqStatus"+i+"'>"+minReqStatus+"</span></div>" +
"</div>" +
"<div class='col-xs-6 col-xs-offset-2'>" +
"<div class='col-xs-6 text-right'><label>Intvw Mtrl Status:</label></div><div class='col-xs-6 text-left'><select id='intvwMtrlStatus"+i+"'></select></div>" +
"</div>" +
"</div>" +
"<div class='row'>" +
"<div class='col-xs-4'>" +
"<div class='col-xs-6 text-right'><label>Phone Screener:</label></div><div class='col-xs-6 text-left'><span id='phoneScreenerValue"+i+"'>"+(data[i].phnScreener?data[i].phnScreener:"")+"</span></div>" +
"</div>" +
"</div>" +
"<div class='row'>" +
"<div class='col-xs-4'>" +
"<div class='row'><div class='col-xs-6 text-right'><label>Phone Screen Date:</label></div><div class='col-xs-6 text-left'><span id='phoneScreenDateValue"+i+"'>"+data[i].phnScrnDate.month+"/"+data[i].phnScrnDate.day+"/"+data[i].phnScrnDate.year+"</span></div></div>" +
"<div class='row'><div class='col-xs-6 text-right'><label>Phone Screen Time:</label></div><div class='col-xs-6 text-left'><span id='phoneScreenTimeValue"+i+"'>"+((parseInt(data[i].phnScrnTime.hour) > 12) ? (parseInt(data[i].phnScrnTime.hour) - 12):(parseInt(data[i].phnScrnTime.hour))) +":"+((parseInt(data[i].phnScrnTime.minute) < 10 )?("0"+parseInt(data[i].phnScrnTime.minute)):(parseInt(data[i].phnScrnTime.minute)))+" "+((parseInt(data[i].phnScrnTime.hour) > 12) ? "PM":"AM")+"</span></div></div>" +
"</div>" +
"<div class='col-xs-8'>" +
"<div class='col-xs-2 text-right'><label>Phone Screen Detail Response:</label></div><div class='col-xs-9 text-left'><textarea id='phoneScreenDetailResponseValue"+i+"' disabled='disabled' value='"+(data[i].detailTxt?data[i].detailTxt:'')+"'></textarea></div>" +
"</div>" +
"</div>" +
"<div>" +
"</div></div></div>" +
        "</div></div>";
    return subCollapsableDivs1;
    };
        /*
         * loading the options in the select text boxes present inside the tree
         * structure
         */
    this.assignSectionValue = function(i,divId,data) {
        $("[id='phoneScreenDetailResponseValue"+i+"']").val($("[id='phoneScreenDetailResponseValue"+i+"']").attr("value"));
        $("[id='"+divId+"'] [id='phoneScreenStatus"+i+"']").attr("disabled","disabled");
        $("[id='"+divId+"'] [id='interviewScreenStatus"+i+"']").attr("disabled","disabled");
        $("[id='"+divId+"'] [id='intvwMtrlStatus"+i+"']").attr("disabled","disabled");
        if(data[i].phoneScreenStatusCode && data[i].phoneScreenStatusCode !== 0){
            $("[id='"+divId+"'] [id='phoneScreenStatus"+i+"'] option[value*='"+data[i].phoneScreenStatusCode+"']").attr("selected","selected");
            $("[id='"+divId+"'] [id='phoneScreenStatus"+i+"']").removeAttr("disabled","disabled");
            CONSTANTS.reviewPhoneScreensDetailsObj.disableCorrespondingOptions1(data[i].phoneScreenStatusCode.toString(),divId,i);
        }
        else
        {
            $("[id='"+divId+"'] [id='phoneScreenStatus"+i+"']").prepend("<option value='0'>--Select--</option>");
            $("[id='"+divId+"'] [id='phoneScreenStatus"+i+"'] option[value='0']").attr("selected","selected");
        }
        if(data[i].interviewStatusCode && data[i].interviewStatusCode !== 0)
        {
    $("[id='"+divId+"'] [id='interviewScreenStatus"+i+"'] option[value*='"+data[i].interviewStatusCode+"']").attr("selected","selected");
    CONSTANTS.reviewPhoneScreensDetailsObj.disableCorrespondingOptions(data[i].interviewStatusCode.toString(),divId,i);
        }
    else
        {
        $("[id='"+divId+"'] [id='interviewScreenStatus"+i+"']").prepend("<option value='0'>--Select--</option>");
        $("[id='"+divId+"'] [id='interviewScreenStatus"+i+"'] option[value='0']").attr("selected","selected");
        }
    if(data[i].interviewMaterialStatusCode && data[i].interviewMaterialStatusCode !== 0){
        $("[id='"+divId+"'] [id='intvwMtrlStatus"+i+"'] option[value*='"+data[i].interviewMaterialStatusCode+"']").attr("selected","selected");
    }
    else
    {
        $("[id='"+divId+"'] [id='intvwMtrlStatus"+i+"']").prepend("<option value='0'>--Select--</option>");
        $("[id='"+divId+"'] [id='intvwMtrlStatus"+i+"'] option[value='0']").attr("selected","selected");
    }
    if($("#inActphoneScreensContent select").length > 0){
        $("#inActphoneScreensContent select").attr("disabled","disabled");
        $("#inActphoneScreensContent select").data("selectBox-selectBoxIt").refresh();
        }
    $("[id='"+divId+"'] [id*='interviewScreenStatus']").data("selectBox-selectBoxIt").refresh();
    $("[id='"+divId+"'] [id*='phoneScreenStatus']").data("selectBox-selectBoxIt").refresh();
    $("[id='"+divId+"'] [id*='intvwMtrlStatus']").data("selectBox-selectBoxIt").refresh();
    };
    /*
     * This method is used for disabling the corresponding oprions for the
     * select option
     */
    this.disableCorrespondingOptions = function (statusCode,divId,i){
        if(statusCode === "9" || statusCode === "11" || statusCode === "12")
        {
        $("[id='"+divId+"'] [id='intvwMtrlStatus"+i+"']").removeAttr("disabled","disabled");
        }
        else
        {
        $("[id='"+divId+"'] [id='intvwMtrlStatus"+i+"']").attr("disabled","disabled");
        }
    };
    this.disableCorrespondingOptions1 = function (statusCode,divId,i){
        if(statusCode === "3" || statusCode === "4")
        {
        $("[id='"+divId+"'] [id='interviewScreenStatus"+i+"']").removeAttr("disabled","disabled");
        }
        else
        {
        $("[id='"+divId+"'] [id='interviewScreenStatus"+i+"']").attr("disabled","disabled");
        }
    };
        /*
         * This method is invoke on the click of the save button by which the
         * save service called so as to save the values
         */
    this.onSaveButtonClicked = function (e){

        var tempData = [];
        tempData = CONSTANTS.reviewPhoneScreensDetailsObj.buildSubmitData("phoneScreensContent");
        if($(e.currentTarget).attr("id") === "save")
            {
            CONSTANTS.reviewPhoneScreensDetailsObj.fromSaveOrSubmit = "save";
            }
        else
            {
            CONSTANTS.reviewPhoneScreensDetailsObj.fromSaveOrSubmit = "submit";
            }
        var phoneScreensContentObj  = {
                "UpdateReviewPhnScrnRequest":{
                 "RequisitionDetail": {
                      "reqNbr": $("#requisitionValue").text(),
                      "requisitionStatusCode": $("#requisitionStatusValue").val()
                    },
                "ITIDetailList":  [{
                    "PhoneScreenIntrwDetail" :(tempData.length===1)?tempData[0]:tempData
                }]
        }
        };
        var data = JSON.stringify(phoneScreensContentObj);
         var callbackFunction = $.Callbacks('once');
         callbackFunction.add(CONSTANTS.reviewPhoneScreensDetailsObj.onSaveSubmitPhoneScreensDetailsSuccess);
         RSASERVICES.saveRevPhnScrnDtl(callbackFunction,data);
    };
        /* This method is invoked after the date is being saved successfully */
    this.onSaveSubmitPhoneScreensDetailsSuccess = function (response)
    {
        $.unblockUI();
        if(response.Response.status === "SUCCESS" )
            {
            if(CONSTANTS.reviewPhoneScreensDetailsObj.fromSaveOrSubmit === "save")
            {
            	CONSTANTS.reviewPhoneScreensDetailsObj.isPageEditedFlag = false;
                CONSTANTS.reviewPhoneScreensDetailsObj.popup.alert("Data Saved Successfully");
            }
        else
            {
            CONSTANTS.reviewPhoneScreensDetailsObj.navigateToHomeScreen();
            }
            }
        else
            {
                if(response.Response.error.errorMsg){
                    CONSTANTS.reviewPhoneScreensDetailsObj.popup.alert(response.Response.error.errorMsg);
                    }
            }
    };
        /*
         * This method is used to navigate the user to the home screen on the
         * slick of the home button
         */
	    this.navigateToHomeScreen = function() {
		if ($("#saveWarningpopup:visible").length > 0) {
			$("#saveWarningpopup").modal("hide");
			$('#saveWarningpopup').on('hidden.bs.modal', function() {
				$('.modal-backdrop').empty().remove();
				CONSTANTS.reviewPhoneScreensDetailsObj.navigateToResScreens();
			}.bind(this));
		} else {
			CONSTANTS.reviewPhoneScreensDetailsObj.navigateToResScreens();
		}
	};    

	    this.navigateToResScreens = function() {
		if (CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage === "home") {
			UTILITY.RemoveQS();
			$.get('app/RetailStaffing/view/retailStaffing.html',
					CONSTANTS.reviewPhoneScreensDetailsObj.setContent);
		} else if (CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage === "close") {
			window.close();
		} else if (CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage === "phoneScreenDetails") {
			$.get('app/RSAPhoneScreenDetails/view/phoneScreenDetails.html',
					CONSTANTS.reviewPhoneScreensDetailsObj.setContent);
		} else if (CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage === "candidateDetails") {
			$.get('app/RetailStaffing/view/candidateDetails.html',
					CONSTANTS.reviewPhoneScreensDetailsObj.setContent);
		} else if (CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage === "reqScreenDetails") {
			CONSTANTS.calledFromSearchPage = true;
			UTILITY.clearCache();
			$.get('app/RSARequisitionDetails/view/requisitionDetails.html',
					CONSTANTS.reviewPhoneScreensDetailsObj.setContent);
		}
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
        CONSTANTS.LoadITIDGDltls = [{itiNbr:$(e.currentTarget).text()}];
        CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage = "phoneScreenDetails";
        CONSTANTS.reviewPhoneScreensDetailsObj.displaySavePop();
    };
        /*
         * This method is invoked on the click of the cadaidate number hyper
         * link by which the user will be navigated to the candidate details
         * screen
         */
    this.navigateToCandidateDetails = function (e) {
        CONSTANTS.retailStaffingObj.applicantId = $(e.currentTarget).attr("data-assoId");
        CONSTANTS.retailStaffingObj.candRefId = $(e.currentTarget).text();
        CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage = "candidateDetails";
        CONSTANTS.reviewPhoneScreensDetailsObj.displaySavePop();
    };
        /*
         * This method is invoked on the click of the req number hyper link on
         * which the user will be navigated to the requisition details screen
         */
    this.navigateToReqScreenDetails = function (e) {
        CONSTANTS.retailStaffingObj.reqNumber = $(e.currentTarget).text();
        CONSTANTS.reviewPhoneScreensDetailsObj.gotoPage = "reqScreenDetails";
        CONSTANTS.reviewPhoneScreensDetailsObj.displaySavePop();
    };
        /*
         * This method is used to build the request param so as to save the data
         * on the click of the save button
         */
    this.buildSubmitData = function(id)
    {
        var tempData = [];
        for(var i=0 ; i<$("[id='"+id+"'] [id*='subPlusButton']").length ; i++)
        {
        tempData[i] = {
                "itiNbr":$("[id='"+id+"'] [id='screenValue"+i+"']").text(),
                "phoneScreenStatusCode":$("[id='"+id+"'] [id='phoneScreenStatus"+i+"']").val(),
                "interviewStatusCode":$("[id='"+id+"'] [id='interviewScreenStatus"+i+"']").val(),
                "interviewMaterialStatusCode":$("[id='"+id+"'] [id='intvwMtrlStatus"+i+"']").val()
        };
        }
        return tempData;
    };
};
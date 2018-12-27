/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: calendar.js
 * Application: Staffing Administration
 *
 * Used to track the calendar details for the given store number.
 * This method has the calendar details for respective store number,
 *
 * @param inputXml
 * XML/JSON containing the store number.
 *
 * @param version
 * Optional parameter, can be used later to change the request/response/logic that gets
 * applied. If not provided, it will be defaulted to 1.
 * Version 1 XML Input/Output,
 * version 2 JSON Input/Output
 *
 * @return
 * XML/JSON containing the event details for the calendar
 * data provided.
 * This XML/JSON is generated and return the response
 * CONSTANTS.calendarView.data
 *
 */
/**
 * Method to show the calendar details for respective
 * store number.
 * Method has the option to create,rename and delete
 * the calendar which user needs.
 */
function calendarTab() {
    /*
     * this method is used for initializing the calendar tab whenever the tab is
     * clicked this initialize will be called
     */
    /*
     * Method initialize will have calendar details to show
     * on page load.
     */
    var currentObject = this;
    this.initialize = function() {
        this.getUserprofile();
        calendarTab.loadState();
        $('select:not(select[multiple])').selectBoxIt();
        $(".calendarText").html(CONSTANTS.HEADER_MESSAGE);
        $(".appBodyparent").addClass("appBodyColor");
        $("#calendarList").attr("title",CONSTANTS.CALENDARTAB_TOOLTIP);
        $("#hiringList").attr("title",CONSTANTS.HIRINGTAB_TOOLTIP);
    };
    /*
     * Method: It will get the user profile details
     * to validate the user
     * Parameter will be sent as null
     *
     */
    this.getUserprofile = function(){
        var successUserProfile = $.Callbacks('once');
        successUserProfile.add(this.successUserProfile);
        var failureUserProfile = $.Callbacks('once');
        failureUserProfile.add(this.failureUserProfile);
        var userProfileUrl = CONSTANTS.UserProfile;
        //params is null
        var params = {"roles":"null"};
        //service call for validateUserProfile
        RSASERVICES.validateUserProfile(userProfileUrl, params,
                successUserProfile, failureUserProfile);
    };
    /*
     * Method:It will fvalidate the profile details
     * entered by the user.
     * This will have success handler
     *
     */
    this.successUserProfile = function(successResponse){
        //compare the response status
        var userResponse = successResponse.UserProfileResponseDTO;
        if (userResponse.Status === CONSTANTS.STATUS_SUCCESS) {
            //set the value for logged user id label
            $("#loggedUserId").text(
                    "Welcome" + " " + userResponse.firstName + " "
                            + userResponse.lastName);
            //set the location for store
            $("#storeLocation").text(
                    "Store#" + " " + userResponse.strNbr + "");
            //validate the role
            if (userResponse.ErrorCd===0){
                //on success the page will initialize
                calendarTab.intializeCalendar();
            } else {
                //on failure show the authentication failure message
                 currentObject.setDraggableFocus();
                $("#alert .modal-body .alertModalBody").text(CONSTANTS.AUTHENTICATION_FAILED);
            }
        } else {
             currentObject.setDraggableFocus();
            $("#alert .modal-body .alertModalBody").text(CONSTANTS.AUTHENTICATION_FAILED);
        }
    };
    /*
     * Method: Validate the error handler on getting failure
     * response from service
     */
    this.failureUserProfile = function(failureResponse){
        //validate the status
        var errorResponse = failureResponse.UserProfileResponseDTO;
        if(errorResponse.Status === CONSTANTS.STATUS_FAILURE){
             currentObject.setDraggableFocus();
           //show the error message
           $("#alert .modal-body .alertModalBody").text(errorResponse.ErrorDesc);
        }
    };
     this.intializeCalendar = function() {
        /*
         * Open Store Popup if user is NOT coming from the Staffing Request Form
         * in order to get the store to work with.
         */
        CONSTANTS.calendarView = {};
        CONSTANTS.calendarView.data = [];
        MODEL.IsNavigationclick=false;
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
         * this keypress for rename calendar text box validation
         */
        $("#newCalendarNameModalText").keypress(function(e) {
            var temp = String.fromCharCode(e.which);
            if (!(/[0-9A-Za-z\-\,\'\(\)\_\. ]/.test(temp))) {
                return false;
            }
        });
        /*
         * this keypress for store pop up text box validation
         */
        $("#storeNo").keypress(function(e) {
            var temp = String.fromCharCode(e.which);
            if (!(/[0-9]/.test(temp))) {
                return false;
            }
        });
        this.inputSelectText();
        /*
         * this text is added to display the message in message bar
         */
        $("#tabMessageBarLabel").html("Select a calendar below...");
        this.createBtnClickSuccess = false;
        this.renameeBtnClickSuccess = false;
        this.deleteBtnClick = false;
        this.calNameExist = false;
        this._params = {};
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
            //Assign the calendar id and store number to model object
            MODEL.calendarId = this._params.calId;
            MODEL.StoreNumber = this._params.strNo;
        }
        /*
         * if store number is not null then it will redirect
         * to calendar detail screen
         */
        if (MODEL.StoreNumber != null && MODEL.StoreNumber > 0) {
            UTILITY.blockFullPage();
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(calendarTab.loadCalendarDetailsScreen);
            requisitionServiceUrl = CONSTANTS.CalenderTaskService;
            RSASERVICES.requisitionCalendars(callbackFunction,
                    requisitionServiceUrl, MODEL.StoreNumber);
        } else {
            //Else it will sshow store number input popup
            this.blockFullPage("#storeModal");
            $('#storeModal').off("shown.bs.modal");
            $('#storeModal').on("shown.bs.modal", function() {
                $("#storeNo").focus();
            });
            $("#storeModal").draggable({
                 handle: ".modal-header"
             });
            $('#storeModal').modal('show');
            /*
             * this method is called to show the grid in initialize
             */
            this.loadGrid();
        }
    };
    /*
     * Method used to select, copy and
     * paste the text in input box.
     * Regex will be validated for accepting
     * few sepcial characters.
     * 
     */
    this.inputSelectText = function(){
    	/*
    	  * Copy and paste the selected store text
    	  * It allows some special characters with alphabets
    	  */
         $("#storeNo").off('paste');
         $("#storeNo").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                 $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
             },100);
         });
         /*
     	  * Copy and paste the selected calendar text
     	  * It allows some special characters with alphabets
     	  */
         $("#CalendarNameModalText").off('paste');
         $("#CalendarNameModalText").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
                 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
             },100);
         });
         /*
     	  * Copy and paste the selected new calendar text
     	  * It allows some special characters with alphabets
     	  */
         $("#newCalendarNameModalText").off('paste');
         $("#newCalendarNameModalText").on('paste', function(e) {
             setTimeout(function(){
                 var tempNum = $(e.currentTarget).val().match(/[0-9A-Za-z\-\,\'\(\)\_\. ]+/g);
                 $(e.currentTarget).val(((tempNum)? (tempNum.join("")) : ""));
             },100);
         });
    };
    /*
     * This method is used for calling the service to get the values for state
     * detail which is used to display the list of states in create hiring event
     */
    this.loadState = function() {
        var callbackFunction = $.Callbacks('once');
        /*
         * successstate is the success handler for load state
         */
        callbackFunction.add(this.onsuccessstate);
        var loadStateURL = CONSTANTS.loadStateURL;
        /* call the loadstate service */
        RSASERVICES.loadstates(callbackFunction, loadStateURL);
    };
    /*
     * this method is the success handler for loadstate which is assign the
     * value CONSTANTS.reqStateDet
     */
    this.onsuccessstate = function(json) {
        var resultList = [];
        /*
         * if response from service is error for loadstate throw error
         */
        if (json.Response.hasOwnProperty("error")) {
            CONSTANTS.calendarView.resultList = json.Response.error.errorMsg;
            currentObject.setDraggableFocus();
            $("#alert .modal-body").addClass("autoHeight");
            $("#alert .modal-body .alertModalBody").text(
                    CONSTANTS.calendarView.resultList);
        }

        /*
         * check whether the result json have state detaillist property or not
         */
        else if (json.Response.hasOwnProperty("StateDetailList")) {
            resultList = null;
            if (json.Response.status === "SUCCESS"
                    && json.Response.StateDetailList !== null) {
                /*
                 * check whether StateDetailList is an array collection
                 */
                if (Array.isArray(json.Response.StateDetailList.StateDetail)) {
                    resultList = json.Response.StateDetailList.StateDetail;
                }
                /*
                 * StateDetailList is not an array collection
                 */
                else {
                    resultList.push(json.Response.StateDetailList.StateDetail);
                }
            }
            /*
             * check whether the result list is having any value object or not
             */
            if (resultList != null && resultList.length > 0) {
                /*
                 * ssign the value of result to constants
                 */
                CONSTANTS.reqStateDet = resultList;
            }
        }
    };
    /*
     * this function is used to set the column for the grid
     */
    this.setColumns = function() {
        var columns = [
                /*
                 * this column is used to show the calendar name in the grid
                 */
                {
                    id : "requisitionCalendarDescription",
                    name : "<div style=line-height:45px title='Calendar Name'>Calendar Name</div>",
                    field : "requisitionCalendarDescription",
                    width : 285,
                    sortable : true,
                    cssClass : "cellalignleft",
                    formatter : function(row, cell, value, columnDef,
                            dataContext) {
                        if(value.toString().length>35){
                            return "<a class='calendarNameIdLink' title='"+ value.toString().replace(/\s/g, '')+"' data-value='" + value.toString().replace(/\s/g, '')
                                + "' id='"
                                + dataContext.requisitionCalendarId + ":"
                                + dataContext.type + "'>" + value + "</a>";
                        }else{
                            return "<a class='calendarNameIdLink' data-value='"+ value.toString().replace(/\s/g, '')
                            + "' id='" + dataContext.requisitionCalendarId + ":"
                            + dataContext.type + "'>" + value + "</a>";
                        }
                    }
                },
                /*
                 * this column is used to show the last updated time stamp for
                 * that calendar.
                 * Changed for FMS 7894 January 2016 CR's
                 * This is now the last date availability was added to a calendar 
                 */
                {
                    id : "availabilityAddedTimestamp",
                    name : "<div style='text-align:right;line-height:17px;white-space:normal' title='Last Date Availability Added'>Date Availability Added</div>",
                    field : "availabilityAddedTimestamp",
                    width : 150,
                    sortable : true,
                    cssClass : "cellalignleft",
                    formatter : this.dateFormatter
                },

                /*
                 * this column is used to show the no of interview slots
                 * available for that calendar
                 */
                {
                    id : "availableInterviewSlots",
                    name : "<div style='text-align:right;line-height:17px;white-space:normal' title='Available Interview Slots'>Available Interview Slots</div>",
                    field : "availableInterviewSlots",
                    cssClass : "cellalignright",
                    width : 130,
                    sortable : true
                },

                /*
                 * this column is used to display the userid who created that
                 * calendar
                 */
                {
                    id : "createSystemUserId",
                    name : "<div style=line-height:45px title='Created By'>Created By</div>",
                    field : "createSystemUserId",
                    width : 130,
                    cssClass : "cellalignleft",
                    sortable : true
                },
                /*
                 * this column is used to display the no of reqs associated with
                 * that calendar
                 */
                {
                    id : "activeRequisitionCount",
                    name : "<div style='text-align:right;line-height:13px;white-space:normal' title='Number of Req\'s associated with calendar'>Number of Req's associated with calendar</div>",
                    field : "activeRequisitionCount",
                    width : 150,
                    cssClass : "cellalignright",
                    sortable : true
                } ];
        return columns;
    };
    /*
     * Method is used to build the grid using
     * predefined properties which is in turn
     * of the slik gird
     */
    this.loadGrid = function() {
        var data = CONSTANTS.calendarView.data;
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 40,
            rowHeight : 30,
            forceFitColumns : true,
            enableCellNavigation : true
        };
      //initialize the columns
        var columns = this.setColumns();
        if(data.length <= 0){
        	columns = _.map(columns,function(value,key){
        		value.sortable = false;
        		return value;
        	});
        }
        /*
         * create a calendar slick grid
         */
        this.calendarGrid = new Slick.Grid("#calendarGrid", data, columns,
                options);
        /*
         * this method is called to bind grid click and sort functionalities
         */
        this.gridView();
        $("#calendarGrid .slick-row .slick-cell:first-child").addClass("textEllipsis");
    };
    /*
     * this method is used to do the functionality on the grid say click,sort
     * etc.,
     */
    this.gridView = function() {
        /*
         * this is used to disable and enable the rename button and delete
         * button in calendar tab
         */
        this.calendarGrid.onClick
                .subscribe(function(e, args) {
                    var row = args.grid.getData()[args.row];
                    CONSTANTS.calendarView.selectedRow = row;
                    /*
                     * check whether the calendar type is greater than 10 or not .
                     * if it is greater than 10 then enable based on active
                     * requisition count if requisition count is zero both
                     * delete and rename can be enable if requisition count is
                     * not zero only rename should be enabled
                     */
                    if (row.type >= 10) {
                        if (row.activeRequisitionCount === 0) {
                            $("#renameBtn").removeAttr('disabled');
                            $("#deleteBtn").removeAttr('disabled');
                            $("#renameBtn").removeClass("disablegreybtn");
                            $("#renameBtn").addClass("darkOrangeBtn");
                            $("#deleteBtn").removeClass("disablegreybtn");
                            $("#deleteBtn").addClass("darkOrangeBtn");
                            $("#createNewCalendar").attr(
                                    "title","Create a new Calendar for store  "
                                            + MODEL.StoreNumber);
                            $("#renameBtn")
                                    .attr(
                                            "title","Click to rename calendar '"
                                                    + row.requisitionCalendarDescription
                                                    + "'");
                            $("#deleteBtn")
                                    .attr(
                                            "title",
                                            "Click to delete calendar '"
                                                    + row.requisitionCalendarDescription
                                                    + "'");
                        } else {
                            $("#renameBtn").removeAttr('disabled');
                            $("#renameBtn").removeClass("disablegreybtn");
                            $("#renameBtn").addClass("darkOrangeBtn");
                            $("#deleteBtn").attr('disabled', 'disabled');
                            $("#deleteBtn").removeClass("darkOrangeBtn");
                            $("#deleteBtn").addClass("disablegreybtn");
                            $("#createNewCalendar").attr("title","Create a new Calendar for store  "
                                            + MODEL.StoreNumber);
                            $("#renameBtn")
                                    .attr("title",
                                            "Click to rename calendar '"
                                                    + row.requisitionCalendarDescription
                                                    + "'");
                            $("#deleteBtn")
                                    .attr( "title",
                                            "Calendar '" + row.requisitionCalendarDescription
                                                    + "'cannot be deleted because there is an Active Requisition attached.");
                        }
                    }
                    /*
                     * if calendar type is less than 10 then disble both rename
                     * and delete buttons
                     */
                    else {
                        $("#renameBtn").removeClass("darkOrangeBtn");
                        $("#renameBtn").addClass("disablegreybtn");
                        $("#deleteBtn").removeClass("darkOrangeBtn");
                        $("#deleteBtn").addClass("disablegreybtn");
                        $("#renameBtn").attr('disabled', 'disabled');
                        $("#deleteBtn").attr('disabled', 'disabled');
                        $("#createNewCalendar").attr("title",
                                "Create a new Calendar for store  "
                                        + MODEL.StoreNumber);
                        $("#renameBtn")
                                .attr("title",
                                        "Default Store and MET Calendars cannot be renamed.");
                        $("#deleteBtn")
                                .attr("title",
                                        "Default Store and MET Calendars cannot be deleted.");
                    }
                });
        this.gridSortAndLinkClick();
        this.calendarGrid.setSortColumn("requisitionCalendarDescription",true);
        this.calendarGrid.onColumnsResized.subscribe(function(){
            $.each($("#calendarGrid .slick-header-column"),function(index,value){
                $(value).find(".slick-column-name div").css("width","100%");
                if($(value).find(".slick-sort-indicator-desc").length >0 || $(value).find(".slick-sort-indicator-asc").length >0){
                    var fullwidth = $(value).find(".slick-column-name div").width();
                    var redWidth = fullwidth-15;
                    $(value).find(".slick-column-name div").css("width",redWidth+"px");
                }
            });
        });
    };
    /*
     * this function is used to sort the grid and click for the links
     */
    this.gridSortAndLinkClick = function() {
        $("#createNewCalendar").attr(
                "title",
                "Create a new Calendar for store  "
                        + MODEL.StoreNumber);
        $("#renameBtn")
                .attr("title",
                        "Select a Calendar Row to Rename a Calendar.");
        $("#deleteBtn")
                .attr("title",
                        "Select a Calendar Row to Delete a Calendar.");
        /*
         * this is used to sort the calendar grid whenever user clicks header
         */
        this.calendarGrid.onSort.subscribe(function(e, args) {
            UTILITY.gridSorter(args.sortCol.field, args.sortAsc,
                    currentObject.calendarGrid,
                    CONSTANTS.calendarView.data);
            $("#calendarGrid .slick-header-column span  div").css("width","");
            var ele;
            if($(e.target).parents(".slick-header-column").length > 0){
                ele = $(e.target).parents(".slick-header-column");
            }
            else if($(e.target).hasClass("slick-header-column")){
                ele = $(e.target);
            }
            var width = $(ele).css("width");
            var redWid = parseInt(width.substring(0, width.length - 2))-15;
            $(ele).find("span div").css("width",redWid+"px");
        });
        /*
         * this is used to navigate to the calendar details screen once calendar
         * name is clicked
         */
        $("#calendarGrid").off("click", ".calendarNameIdLink");
        $("#calendarGrid").on("click", ".calendarNameIdLink", function(e) {
            MODEL.IsNavigationclick=true;
            calendarTab.navigateToCalendarDetailsScreen(e);
        });
        /*
         * this is used to disable delete and rename button once grid load
         * initially
         */
        $("#renameBtn").attr('disabled', 'disabled');
        $("#deleteBtn").attr('disabled', 'diabled');
        $("#renameBtn").removeClass("darkOrangeBtn");
        $("#deleteBtn").removeClass("darkOrangeBtn");
        $("#renameBtn").addClass("disablegreybtn");
        $("#deleteBtn").addClass("disablegreybtn");
    };
    /*
     * this function is used to return the value in the format(mm/dd/yyyy
     * hour:mins am/pm)
     */
    this.dateFormatter = function(row, cell, value) {
    	//Added for FMS 7894 January 2016 CR's
    	//This should only happen if it is a new calendar or no availability has been added
    	if (value == null) {
        	return "";
        }
    	
    	var dateSplitted="";
        var dateValue = value.substring(0,10);
        var timeStamp = value.substring(11,19);
        //split the date value with hyphen
        dateValue = dateValue.split("-");
        //format the date with formatted string
        dateSplitted = dateValue[1]+"/"+dateValue[2]+"/"+dateValue[0]+" "+timeStamp;
        var dateCreate = new Date(dateSplitted);
        var date = ("0" + (dateCreate.getMonth() + 1)).slice(-2) + "/"
                + ("0" + dateCreate.getDate()).slice(-2) + "/"
                + dateCreate.getFullYear();
        //get full year and hours,minutes
        var hours = dateCreate.getHours();
        var time = dateCreate.getHours() < 12 ? "am" : "pm";
        hours = hours== 0 ? "12" : dateCreate.getHours() > 12 ? dateCreate.getHours() - 12 : dateCreate.getHours();
        var mins = dateCreate.getMinutes();
        mins  = ((mins < 10) ? "0" : "") + mins;    
        //form the time zone
        var timezone = hours + ":" + mins + " " + time;
        return date + " " + timezone;
    };
    /*
     * this function is used to navigate to the calendar detail screen once
     * calendar name link is clicked in the grid
     */
    this.navigateToCalendarDetailsScreen = function(e) {
        $("#tabMessageBarLabel").html("Select a day below...");
        $("#calendarTask").hide();
        $("#calendarDiv").show();
        $("#calendarSelect").empty();
        var calendarIdType="";
        if(e && e.currentTarget){
            calendarIdType = $(e.currentTarget).attr('id');
        }
        //Iterate the calendar data
        for (var desc = 0; desc < CONSTANTS.calendarView.data.length; desc++) {
        //Append the data in dropdown option
        $("#calendarSelect")
                    .append(
                            '<option id='
                                    + CONSTANTS.calendarView.data[desc].requisitionCalendarId
                                    + ':'
                                    + CONSTANTS.calendarView.data[desc].type
                                    + ' value="'
                                    + CONSTANTS.calendarView.data[desc].requisitionCalendarDescription.toString()
                                            .replace(/\s/g, '')
                                    + '">'
                                    + CONSTANTS.calendarView.data[desc].requisitionCalendarDescription
                                    + '</option>');
        }
        //Set the default selected value
        $("#calendarSelect option[value=" + '"' + $(e.currentTarget).text().toString().replace(/\s/g, '') + '"' +"]")
                .attr('selected', 'selected');
        //Refresh and get the data in selectbox it
        $("#calendarSelect").data("selectBox-selectBoxIt").refresh();
        if ($("#calendarSelect").data("selectBox-selectBoxIt")) {
            $("#calendarSelect").data("selectBox-selectBoxIt").refresh();
        }
        $(".calendarText").empty();
        $(".calendarText").html(CONSTANTS.CALENDAR_HEADER_MESSAGE);
        //Intialize the calendar details pae
        var showCalendarView = new showCalendar();
        showCalendarView.initialize(calendarIdType);
        $("#calendarList").attr("title",CONSTANTS.CALENDAR_SUMMARY_TOOLTIP);
    };
    /*
     * Method which redirects calendar detail screen automatically
     * comes from Retail staffing screen.
     * Instead of getting store popup
     * it will show the calendar details screen for respective
     * calendar id and store number
     */
    this.loadCalendarDetailsScreen = function(calendarResponse) {
        $("#tabMessageBarLabel").html("Select a day below...");
        $(".appBodyparent").removeClass("appBodyColor");
        UTILITY.unblockFullPage();
        var calendarParams="";
        var calendarType="";
        var calendarDesc="";
        $("#calendarTask").hide();
        $("#calendarDiv").show();
        $("#calendarSelect").empty();
        var requisitionResponse = calendarResponse.Response;
        if (requisitionResponse.status==="SUCCESS" && requisitionResponse.ScheduleDescList
                && requisitionResponse.ScheduleDescList.ScheduleDescriptionDetails !== null) {
            if (_
                    .isArray(requisitionResponse.ScheduleDescList.ScheduleDescriptionDetails)) {
                CONSTANTS.calendarView.data = requisitionResponse.ScheduleDescList.ScheduleDescriptionDetails;
            } else {
                CONSTANTS.calendarView.data = [ requisitionResponse.ScheduleDescList.ScheduleDescriptionDetails ];
            }
            CONSTANTS.REQUISITIONDATA = requisitionResponse.ScheduleDescList.ScheduleDescriptionDetails;
          }
        //Iterate the calendar response
        for(var schedule=0;schedule< CONSTANTS.calendarView.data.length;schedule++){
             //Append the data in dropdown option
            $("#calendarSelect")
             .append(
                     '<option id='
                             +  CONSTANTS.calendarView.data[schedule].requisitionCalendarId
                             + ':'
                             +  CONSTANTS.calendarView.data[schedule].type
                             + ' value="'
                             +  CONSTANTS.calendarView.data[schedule].requisitionCalendarDescription.toString()
                                     .replace(/\s/g, '')
                             + '">'
                             +  CONSTANTS.calendarView.data[schedule].requisitionCalendarDescription
                             + '</option>');
            if( CONSTANTS.calendarView.data[schedule].requisitionCalendarId===Number(MODEL.calendarId)){
                 calendarType = CONSTANTS.calendarView.data[schedule].type;
                 calendarDesc = CONSTANTS.calendarView.data[schedule].requisitionCalendarDescription;
            }
        }
      //Set the default selected value       
        $("#calendarSelect option[value=" +'"'+ calendarDesc.toString().replace(/\s/g, '') +'"' +"]")
        .attr('selected', 'selected');
        $("#calendarSelect").data("selectBox-selectBoxIt").refresh();
        if ($("#calendarSelect").data("selectBox-selectBoxIt")) {
            $("#calendarSelect").data("selectBox-selectBoxIt").refresh();
        }
        $(".calendarText").empty();
        $(".calendarText").html(CONSTANTS.CALENDAR_HEADER_MESSAGE);
        //Refresh and get the data in selectbox it
        var showCalendarView = new showCalendar();
        //form the params to send in intialize
        calendarParams=MODEL.calendarId+":"+calendarType;
        //if navigaton click is false then it will
        //automatically redirects the calendar detail screen
        if(!MODEL.IsNavigationclick){
            showCalendarView.initialize(calendarParams);
        }
        $("#calendarList").attr("title",CONSTANTS.CALENDAR_SUMMARY_TOOLTIP);
    };
    /*
     * this function is used to check whether the store number entered in store
     * popup is valid or not
     */
    this.validStore = function() {
        this.storeNo = $("#storeNo").val();
        $('#storeModal').modal('hide');
        //validate the store number is empty or length is less than 4
        if ($("#storeNo").val() === "" || $("#storeNo").val().length < 4) {
            this.showNotification(CONSTANTS.ValidMessage);
        } else {
            //store number mets the condition and if satisfies
            $('#alert').modal('hide');
            $.extend(CONSTANTS.calendarView,this);
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.onOkClick);
            //service call for store validation
            var validStoreUrl = CONSTANTS.ValidStoreService;
            RSASERVICES.validStore(callbackFunction, validStoreUrl,
                    this.storeNo);
        }
    };
    /*
     * this function is used to show the store popup
     */
    this.notification = function() {
        $('#alert').modal('hide');
        calendarTab.blockFullPage("#storeModal");
        $('#storeModal').modal('show');
    };

    /*
     * this function is the success handler for validstore service. if it
     * returns true call the requisition calendar service else show the popup
     * invalid store
     */
    this.onOkClick = function(successJson) {
        //on sucess of store validation and assign the response to variable
        var response = successJson.storeResponse;
        this.storeNumber = $("#storeNo").val();
        $("#requistionTab").attr("title", "All Active Requisitions For Location " + this.storeNumber);
        if (response.error && response.error.hasOwnProperty("error")) {
            $('#storeModal').modal('show');
            if (_.isArray(response.error)) {
                CONSTANTS.calendarView.resultList = response.error;
            } else {
                CONSTANTS.calendarView.resultList = [ response.error ];
            }
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.calendarView.resultList);
             currentObject.setDraggableFocus();
            $("#alert .modal-content").addClass("metStoreContentWidth");
            $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
            $("#alert .modal-body").addClass("autoHeight");
            $("#alert .modal-body .alertModalBody").text(
                    returnedErrorMessage);
        }
        /*
         * if response has valid as false throw error and show store pop
         * up again
         */
        else if (!response.valid) {
            var errResponse = "Store/Location Number " + this.storeNumber
            + " is invalid.";
            calendarTab.blockFullPage("#storeModal");
            $('#storeModal').modal('show');
            $('#storeModal').css('display', 'block !important');
             currentObject.setDraggableFocus();
            $("#alert .modal-content").addClass("largestoreModal");
            $("#alert .modal-body").addClass("largeBodystoreModal");
            $("#alert .modal-body .alertModalBody").text(errResponse);
            $("#storeNo").val("");
        }
        /*
         * if response has valid property as true then call
         * getrequisitioncalendar service
         */
        else if (response.valid && response.hasOwnProperty("valid")) {
            $(".appBodyparent").removeClass("appBodyColor");
            MODEL.StoreNumber = this.storeNumber;
            $("#createNewCalendar").attr(
                    "title",
                    "Create a new Calendar for store  "
                    + MODEL.StoreNumber);
            /*
             * Get the Store Calendars List to show on the Calendar
             * Summay Page...
             */
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(calendarTab.getResultForRequistionCalendars);
            var requisitionServiceUrl = CONSTANTS.CalenderTaskService;
            RSASERVICES.requisitionCalendars(callbackFunction,
                    requisitionServiceUrl, this.storeNumber);
        }
    };
    /*
     * this is used to sshow the pop up based on the message we sent
     */
    this.showNotification = function(message) {
        calendarTab.blockFullPage("#storeModal");
        $('#storeModal').modal('show');
        $('#storeModal').css('display', 'block !important');
        currentObject.setDraggableFocus();
        $("#alert .modal-body .alertModalBody").text(message);
        $("#storeNo").val("");
    };
    /*
     * this methios is the success handler for requisition calendar service
     * call.If it contains 'ScheduleDescriptionDetails' load the grid else throw
     * error
     */
    this.getResultForRequistionCalendars = function(calendarResponse) {
        var response = calendarResponse.Response;
        if (response.status) {
            if (response.ScheduleDescList
                    && response.ScheduleDescList.ScheduleDescriptionDetails !== null) {
                if (_
                        .isArray(response.ScheduleDescList.ScheduleDescriptionDetails)) {
                    CONSTANTS.calendarView.data = response.ScheduleDescList.ScheduleDescriptionDetails;
                } else {
                    CONSTANTS.calendarView.data = [ response.ScheduleDescList.ScheduleDescriptionDetails ];
                }
                CONSTANTS.REQUISITIONDATA = response.ScheduleDescList.ScheduleDescriptionDetails;
                calendarTab.loadGrid();
            } else if (response.error) {
                 currentObject.setDraggableFocus();
                $("#alert .modal-body").addClass("autoHeight");
                $("#alert .modal-body .alertModalBody").text(
                        response.error.errorMsg);
            }
        } else if (response.error) {
             currentObject.setDraggableFocus();
            $("#alert .modal-body").addClass("autoHeight");
            $("#alert .modal-body .alertModalBody").text(
                    response.error.errorMsg);
        }
        else{
             currentObject.setDraggableFocus();
             $("#alert .modal-body .alertModalBody").text(
                     CONSTANTS.RequisitionFaultMessage);
        }
    };
    /*
     * this function is used to show the create calendar
     * pop up once calendar
     * button is clicked
     */
    this.calendarButtonClick = function() {
        this.blockFullPage("#createCalendarModal");
        $('#createCalendarModal').off("shown.bs.modal");
        $('#createCalendarModal').on("shown.bs.modal", function(){
            $("#CalendarNameModalText").focus();
        });
        $('#createCalendarModal').modal('show');
        $("#createCalendarModal").draggable({
             handle: ".modal-header"
         });
        $("#CalendarNameModalText").val("");
    };
    /*
     * this function is called when create is clicked in create popup.
     * it includes validations like empty or prefix store
     * number or met or existing
     * calendar name. If all validation passed service call is
     * done to create a new calendar
     *
     */
    this.createCalendar = function() {
        $.extend(CONSTANTS.calendarView,this);
        this.newCalendarName = $("#CalendarNameModalText").val();
        var patt = new RegExp(/^met/i);
        var resPatern = patt.test(this.newCalendarName);
        //if calendar name is empty
        if ($("#CalendarNameModalText").val().trim() === "") {
            $("#alert .alertModalBody").text(
                    "Please enter a calendar name to Create.");
            currentObject.setDraggableFocus();
            $("#alert .modal-content").addClass("largestoreModal");
            $("#alert .modal-body").addClass("largeBodystoreModal");
        }
        //validate the calendar name starts with store number
        else if (CONSTANTS.calendarView.storeNo === this.newCalendarName
                .substr(0, 4)) {
            $("#alert .alertModalBody").text(
                    "Calendar Name cannot begin with the Store Number.");
            currentObject.setDraggableFocus();
            $("#CalendarNameModalText").val("");
        }
        //validate the calendar name starts with letters MET
        else if (resPatern) {
            $("#alert .alertModalBody").text(
                    "Calendar Name cannot begin with the letters MET.");
            currentObject.setDraggableFocus();
            $("#CalendarNameModalText").val("");
        } else {
            for (var i = 0; i < CONSTANTS.calendarView.data.length; i++) {
                if (this.newCalendarName.toString().toUpperCase() === CONSTANTS.calendarView.data[i].requisitionCalendarDescription.toString().toUpperCase()) {
                    CONSTANTS.calendarView.calNameExist = true;
                    $("#alert .alertModalBody").html(
                            "Calendar Name: '" + this.newCalendarName
                                    + "' already exists."
                                    +"<br/>"
                                    + "Please choose another name.");
                    currentObject.setDraggableFocus();
                    $("#CalendarNameModalText").val("");
                    $("#alert .modal-body").addClass("autoHeight");
                    $("#alert .modal-content").addClass("sameCalendarExistsModal");
                }
            }
            $('#alert').on("shown.bs.modal", function() {
                $("#alertOK").focus();
            });
            if (!CONSTANTS.calendarView.calNameExist) {
                $("#createCalendarModal").modal('hide');
                var callbackFunction = $.Callbacks('once');
                callbackFunction
                        .add(CONSTANTS.calendarView.createCalendarSuccess);
                var createCalendarURL = CONSTANTS.createCalendarURL;
                var RSAServices = new rsaServices();
                var createCalendarRequestJSON = {};
                createCalendarRequestJSON[CONSTANTS.NAME] = UTILITY.trim(this.newCalendarName);
                createCalendarRequestJSON[CONSTANTS.STORENUMBER] = MODEL.StoreNumber;
                createCalendarRequestJSON[CONSTANTS.VERSION] = 1;
                createCalendarRequestJSON[CONSTANTS.TYPE] = 10;
                RSAServices.createRequisitionCalendar(callbackFunction,
                        createCalendarURL, createCalendarRequestJSON);
            }
        }
    };
    /*
     * this function is the success handler for create calendar which inturn
     * call getrequisitoncalendar service to get the new list
     */
    this.createCalendarSuccess = function(json) {
        //On success of  create calendar
    	  $("#CalendarNameModalText").val("");
          $("#newCalendarNameModalText").val("");
        var response = json.interviewResponse;
        //validate the response has error
        if (response.error) {
            if (_.isArray(response.error)) {
                CONSTANTS.calendarView.resultList = response.error;
            } else {
                CONSTANTS.calendarView.resultList = [ response.error ];
            }
            //validate the error code and returns the error description
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.calendarView.resultList);
            currentObject.setDraggableFocus();
            $("#alert .modal-content").addClass("metStoreContentWidth");
            $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
            $("#alert .modal-body").addClass("autoHeight");
            $("#alert .modal-body .alertModalBody").text(
                    returnedErrorMessage);
        } else if (response.requisitionCalendarId) {
            CONSTANTS.calendarView.createBtnClickSuccess = true;
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(calendarTab.getResultForRequistionCalendars);
            var requisitionServiceUrl = CONSTANTS.CalenderTaskService;
            RSASERVICES.requisitionCalendars(callbackFunction,
                    requisitionServiceUrl, MODEL.StoreNumber);
            $('#alert').off("shown.bs.modal");
            $('#alert').on("shown.bs.modal", function() {
                 $('#alert').off();
                $("#alertOK").focus();
            });
            calendarTab.blockFullPage("#alert");
            calendarTab.removeAlertCss();
            $("#alert").draggable({
                 handle: ".modal-header"
             });
            $("#alert .modal-content").addClass("smallalertmodal");
            $("#alert .modal-body").addClass("smallalertModal");
            $("#alert .alertModalBody").addClass("alignalertModal");
            $("#alert .alertModalBody").text("Calendar created");
        }
    };
    /*
     * this function is handling the ok click for the alert popups for
     * create/rename functionalities
     */
    this.alertOkClicked = function() {
        $("#alert").modal('hide');
        if (CONSTANTS.calendarView.createBtnClickSuccess) {
            CONSTANTS.calendarView.createBtnClickSuccess = false;
        }else {
            CONSTANTS.calendarView.calNameExist = false;
        }
    };
    /*
     * this function is called when rename button is clicked
     */
    this.renameButtonClick = function() {
        $("#CalendarNameModalText").val("");
        $("#newCalendarNameModalText").val("");
         this.deleteBtnClick = false;
        $("#oldCalName")
                .text(
                        "'"
                                + CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription
                                + "'");
        $('#renameCalendarModal').off("shown.bs.modal");
        $('#renameCalendarModal').on("shown.bs.modal", function() {
            $("#newCalendarNameModalText").focus();
        });
        $('#renameCalendarModal').modal('show');
        $("#renameCalendarModal").draggable({
             handle: ".modal-header"
         });
    };
    /*
     * this function is called when submit is clicked in rename calendar popup.
     * it includes validation like empty,prefix store number/met or already
     * existing name if validations passed service call is done to update the
     * name of the calendar
     */
    this.renameCalendar = function() {
        $.extend(CONSTANTS.calendarView,this);
        $("#warningpopup .modal-body").removeClass("scrollPopupBody");
        this.newCalendarName = $("#newCalendarNameModalText").val();
        var patt = new RegExp(/^met/i);
        var resPatern = patt.test(this.newCalendarName);
        //Validate the calendar name is empty
        if ($("#newCalendarNameModalText").val().trim() === "") {
            $("#alert .alertModalBody").text(
                    "Please enter a new calendar name.");
            $("#alert #alertModalLabel").text("");
            currentObject.setDraggableFocus();
        }
        //validate the calendar name starts with store number
        else if (CONSTANTS.calendarView.storeNo === this.newCalendarName
                .substr(0, 4)) {
            $("#alert .alertModalBody").text(
                    "Calendar Name cannot begin with the Store Number.");
            currentObject.setDraggableFocus();
            $("#alert #alertModalLabel").text("Calendar Rename Error");
        }
        //validate the calendar starts with letter MET
        else if (resPatern) {
            $("#alert .alertModalBody").text(
                    "Calendar Name cannot begin with the letters MET.");
            currentObject.setDraggableFocus();
            $("#alert #alertModalLabel").html("Calendar Rename Error");
        } else {
            for (var i = 0; i < CONSTANTS.calendarView.data.length; i++) {
                if (this.newCalendarName === CONSTANTS.calendarView.data[i].requisitionCalendarDescription) {
                    CONSTANTS.calendarView.calNameExist = true;                   
                    $("#alert .alertModalBody").html(
                            "<div>Calendar Name: '" + this.newCalendarName
                                    + "' already exists.</div>"
                                    + "<div>Please choose another name.</div>");
                    currentObject.setDraggableFocus();
                    $("#alert .modal-content").addClass("renameErrorWidth");
                    $("#alert .modal-body").addClass("autoHeight");
                    $("#alert #alertModalLabel").text("Calendar Rename Error");
                    $("#newCalendarNameModalText").val("");
                }
            }
            //calendar name if already exists
            if (!CONSTANTS.calendarView.calNameExist) {
                $("#WarningModalLabel").text("Warning");
                $(".cautionMsg").text("CAUTION, you are about to rename");
                $(".sureMsg").text("Are You Sure?");
                $("#oldRenameCalName")
                        .text(
                                "'"
                                        + CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription
                                        + ".'");
                calendarTab.blockFullPage("#warningpopup");
                if(CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription.length>50){
                    $("#warningpopup .modal-body").addClass("scrollPopupBody");
                }else{
                    $("#warningpopup .modal-body").removeClass("scrollPopupBody");
                }
                $("#warningpopup .modal-dialog").removeClass("deleteWarn");
                $("#warningpopup .modal-content").removeClass(
                        "deleteWarnContent");
                $("#warningpopup").modal('show');
            }
        }
    };
    /*
     * this method is called when warning popup shown for delete or rename yes
     * click.
     */
    this.yesClicked = function() {
        $("#warningpopup").modal('hide');
         CONSTANTS.calendarView.lastUpdateTimestamp = CONSTANTS.calendarView.selectedRow.lastUpdateTimestamp;
         //valida the new calendar name is not undefined
         //then assign to calendar description
        if(this.newCalendarName!==undefined && this.newCalendarName!==""){
             CONSTANTS.calendarView.requisitionCalendarDescription = this.newCalendarName;
             this.newCalendarName="";
        }else{
             CONSTANTS.calendarView.requisitionCalendarDescription = CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription;
        }
        //Assign the calendar id
        CONSTANTS.calendarView.requisitionCalendarId = CONSTANTS.calendarView.selectedRow.requisitionCalendarId;
        //valida the flag for button click
        if (this.deleteBtnClick) {
            CONSTANTS.calendarView.activeFlag = false;
        } else {
            CONSTANTS.calendarView.activeFlag = true;
        }
        var renameDeleteURL = CONSTANTS.renameDeleteURL;
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(currentObject.renameCalendarSuccess);
        var RSAServices = new rsaServices();
        //service call for renaming the calendar
        RSAServices.updateCalendar(callbackFunction, renameDeleteURL,
                CONSTANTS.calendarView.activeFlag,
                CONSTANTS.calendarView.requisitionCalendarDescription,
                CONSTANTS.calendarView.lastUpdateTimestamp,
                CONSTANTS.calendarView.requisitionCalendarId);
    };
    /*
     * this function is success handler for rename and delete service which is
     * again in turn call getrequisition calendar service to update the new list
     * in the grid
     */
    this.renameCalendarSuccess = function(json) {
        CONSTANTS.calendarView.renameeBtnClickSuccess = true;
        var callbackFunction="";
        var requisitionServiceUrl="";
        var response = json.calendarResponse;
        //validate the response has error
        if (response.error) {
            if (_.isArray(response.error)) {
                CONSTANTS.calendarView.resultList = response.error;
            } else {
                CONSTANTS.calendarView.resultList = [ response.error ];
            }
            //retur the error description
            $('#renameCalendarModal').modal('hide');
            var returnErrorMessage = MODEL.getErrorMessage(CONSTANTS.calendarView.resultList);
            currentObject.setDraggableFocus();
            $("#alert .modal-content").addClass("metStoreContentWidth");
            $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
            $("#alert .modal-body").addClass("autoHeight");
            $("#alert .modal-body .alertModalBody").text(
                    returnErrorMessage);
        } else if (response.reqnCalId) {
            if (CONSTANTS.calendarView.activeFlag) {
                $("#warningpopup").modal('hide');
                callbackFunction = $.Callbacks('once');
                callbackFunction.add(calendarTab.getResultForRequistionCalendars);
                requisitionServiceUrl = CONSTANTS.CalenderTaskService;
                RSASERVICES.requisitionCalendars(callbackFunction,
                        requisitionServiceUrl, MODEL.StoreNumber);
                $("#alert .alertModalBody").empty();
                $("#alert #alertModalLabel").empty();
                $("#alert .alertModalBody")
                        .text(
                                "\n \n Selected Calendar was Renamed to '"
                                        + CONSTANTS.calendarView.requisitionCalendarDescription
                                        + "'");
                $("#alert #alertModalLabel").text(
                        "Calendar Rename Confirmation");
                $("#renameCalendarModal").modal('hide');
                currentObject.setDraggableFocus();
                $("#alert #alertModalLabel").text(
                        "Calendar Rename Confirmation");
                $("#alert .modal-content").addClass("largeModal");
                $("#alert .modal-body ").addClass("largeBody");
                $("#alert .alertModalBody").addClass("alignModal");
            } else {
                $("#warningpopup").modal('hide');
                callbackFunction = $.Callbacks('once');
                callbackFunction.add(calendarTab.getResultForRequistionCalendars);
                requisitionServiceUrl = CONSTANTS.CalenderTaskService;
                RSASERVICES.requisitionCalendars(callbackFunction,
                        requisitionServiceUrl, MODEL.StoreNumber);
                $("#alert .alertModalBody").empty();
                currentObject.setDraggableFocus();
                $("#alert .alertModalBody")
                        .text(
                                "Selected Calendar '"
                                        + CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription
                                        + "' was Deleted.");               
                $("#alert #alertModalLabel").text(
                        "Calendar Delete Confirmation");
                $("#alert .modal-content").addClass("largeModal");
                $("#alert .modal-body ").addClass("largeBody");
                $("#alert .alertModalBody").addClass("alignModal");
            }
        }
    };
    /*
     * this function is used to show the warning pop up when delete button is
     * clicked
     */
    this.deleteButtonClick = function() {
        this.deleteBtnClick = true;
        $("#warningpopup .modal-body").removeClass("scrollPopupBody");
        $("#WarningModalLabel").text("Warning");
        $(".cautionMsg").text("CAUTION, you are about to delete");
        $(".sureMsg").text("Are You Sure?");
        $("#oldRenameCalName")
                .text(
                        "'"+ CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription
                                + ".'");
        calendarTab.blockFullPage("#warningpopup");
        $("#warningpopup .modal-dialog").addClass("deleteWarn");
        $("#warningpopup .modal-content").addClass("deleteWarnContent");
        $("#warningpopup").modal('show');
    };
    /*
     * this methosd is invoked whenever the tab is clicked
     */
    this.tabClick = function() {
        $(".calendarText").empty();
        $(".calendarText").html(CONSTANTS.HEADER_MESSAGE);
        $("#calendarTask").show();
        $("#calendarDiv").hide();
        CONSTANTS.calendarView = {};
        CONSTANTS.calendarView.data = [];
        this.createBtnClickSuccess = false;
        this.renameeBtnClickSuccess = false;
        this.deleteBtnClick = false;
        this.calNameExist = false;
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(calendarTab.getResultForRequistionCalendars);
        var requisitionServiceUrl = CONSTANTS.CalenderTaskService;
        RSASERVICES.requisitionCalendars(callbackFunction,
                requisitionServiceUrl, MODEL.StoreNumber);
    };
    /*
     * Method for dragging the popup wherever
     * needs in window
     */
    this.setDraggableFocus = function(){
         $('#alert').off("shown.bs.modal");
        $('#alert').on("shown.bs.modal", function() {
            $("#alertOK").focus();
        });
        calendarTab.blockFullPage("#alert");
        calendarTab.removeAlertCss();
         $("#alert").draggable({
              handle: ".modal-header"
          });
    };
    /*
     * Method is used to block the background on
     * showing popup orelse on click of outside
     * the popup will be closed
     *
     * Method to block the background
     * and cannot be able to handle the
     * keyboards events
     */
    this.blockFullPage = function(popupname) {
        $(popupname).modal({
            backdrop : 'static',
            keyboard : false
        });
    };
    /*
     * this function is used to remove the css aplied for that popup. it is
     * called once popup ok is clicked
     */
    this.removeAlertCss = function() {
        $("#alert .modal-content").removeClass("largeModal");
        $("#alert .modal-body ").removeClass("largeBody");
        $("#alert .modal-content").removeClass("largestoreModal");
        $("#alert .modal-body").removeClass("largeBodystoreModal");
        $("#alert .modal-content").removeClass("smallalertmodal");
        $("#alert .modal-body").removeClass("smallalertModal");
        $("#alert .alertModalBody").removeClass("alignalertModal");
        $("#alert .modal-content").removeClass("longModalContent");
        $("#alert .modal-body ").removeClass("longModalBody");
        $("#alert .alertModalBody").removeClass("alignModal");
        $("#alert .modal-body").removeClass("autoHeight");
        $("#alert #alertModalLabel").html("");
        $("#alert .modal-content").removeClass("metStoreContentWidth");
        $("#alert .modal-body .alertModalBody").removeClass("metStoreBodyWidth");
        $("#alert .modal-content").removeClass("renameErrorWidth");
    };
    /*
     * Method is used on click of cancel
     * button the background gets blocked
     * with the color specified
     */
    this.storeCancel = function(){
        $('#storeModal').modal('hide');
        window.close();
        $("body").block({
            message : null,
            overlayCSS : {
                backgroundColor : "rgb(139,139,139)",
                opacity : 0.6,
                cursor : "auto"
            },
            ignoreIfBlocked : false
        });
    };
};
/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: hiringEvent.js
 * Application: Staffing Administration
 *
 * Used to track the Hiring event details for the given store number.
 * This method has the event details for respective store number,
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
 * XML/JSON containing the event details for the hiring
 * data provided.
 * This XML/JSON is generated and return the response
 * CONSTANTS.hiringEventView.data
 *
 */
/**
 * Method is used to populate the hiring
 * details of provided store.
 * the hiring events has actions to do,
 * 1.create hiring event
 * 2.rename hiring event
 * 3.Delete hiring event.
 *
 * Every hiring event has id to get the respective
 * calendar details.
 *
 * Associated calendar details will have the interview
 * slots to be add or delete or print
 */
function hiringEventTab(){
    /*
     * this function is used to initialize hiring event tab
     */
    var currentObject = this;
    this.initialize = function() {
        $('select:not(select[multiple])').selectBoxIt();
        $("#hiringEventTask").show();
        $("#hiringCalendarDiv").hide();
        CONSTANTS.hiringEventView = {};
        CONSTANTS.hiringEventView.data = [];
        $(".hiringCalendarText").html(CONSTANTS.HIRINGHEADER_MESSAGE);
        /*
         * load create hiring event tab and edit hiring event popup in
         * initialize
         */
        $.get('app/RSAInterviewAvailability/view/editHiringEventPopUp.html',
                function(editData) {
                    $(".tabRow").append(editData);
                });
        $.get('app/RSAInterviewAvailability/view/createHiringEventPopUp.html',
                function(data) {
                    $(".tabRow").append(data);
                });        
        /*
         * this functionj is called to call the service
         * getrequisitionHiringEvent
         */
        this.loadData();
    };
    /*
     * this function is used to set the column for the grid
     */
    this.setColumns = function() {
        var columns = [
                /*
                 * this column is used to show the hiring event name in the grid
                 */
                {
                    id : "requisitionCalendarDescription",
                    name : "<div style=line-height:40px title='Hiring Event Name'>Hiring Event Name</div>",
                    field : "requisitionCalendarDescription",
                    width : 215,
                    sortable : true,
                    cssClass : "cellalignleft",
                    formatter : function(row, cell, value, columnDef,
                            dataContext) {
                        if(value.toString().length>35){
                             return "<a  class='hiringEventIdIdLink' id="
                             + dataContext.requisitionCalendarId + ":"
                             + dataContext.hireEventBeginDate + ":"
                             + dataContext.hireEventEndDate + ":"
                             + dataContext.hireEventId + " title="+value.toString().replace(/\s/g, '').replace(/"/g,'&quot;').replace(/>/g,'&gt;').replace(/</,'&lt;')
                             + " data-value="+value.toString().replace(/\s/g, '').replace(/"/g,'&quot;').replace(/>/g,'&gt;').replace(/</,'&lt;')
                             + ">" + value.toString().replace(/"/g,'&quot;').replace(/>/g,'&gt;').replace(/</,'&lt;')
                             + "</a>";
                        }else{
                             return "<a class='hiringEventIdIdLink' id="
                             + dataContext.requisitionCalendarId + ":"
                             + dataContext.hireEventBeginDate + ":"
                             + dataContext.hireEventEndDate + ":"
                             + dataContext.hireEventId + " data-value="+ value.toString().replace(/\s/g, '').replace(/"/g,'&quot;').replace(/>/g,'&gt;').replace(/</,'&lt;')
                             + ">" + value.toString().replace(/"/g,'&quot;').replace(/>/g,'&gt;').replace(/</,'&lt;')
                             + "</a>";
                        }
                    }
                },
                /*
                 * this column is used to show the hiring event begin date in the
                 * grid
                 */
                {
                    id : "hireEventBeginDate",
                    name : "<div style=line-height:40px title='Event Start'>Event Start</div>",
                    field : "hireEventBeginDate",
                    width : 95,
                    sortable : true,
                    formatter : this.dateFormatter,
                    cssClass : "cellalignleft"
                },
                /*
                 * this column is used to show the hiring event end date in the
                 * grid
                 */
                {
                    id : "hireEventEndDate",
                    name : "<div style=line-height:40px title='Event End'>Event End</div>",
                    field : "hireEventEndDate",
                    width : 95,
                    sortable : true,
                    formatter : this.dateFormatter,
                    cssClass : "cellalignleft"
                }, /*
                 * this column is used to show the no of scheduled interview
                 * slots in the grid
                 */
                {
                    id : "scheduledInterviewSlots",
                    name : "<div style='text-align:right;white-space:normal' title='Candidates Scheduled'>Candidates Scheduled</div>",
                    field : "scheduledInterviewSlots",
                    width : 80,
                    sortable : true,
                    cssClass : "cellalignright"
                },
                /*
                 * this column is used to show the available interview slots in
                 * the grid
                 */
                {
                    id : "availableInterviewSlots",
                    name : "<div style='text-align:right;line-height:15px;white-space:normal' title='Available Interview slots'>Available Interview slots</div>",
                    field : "availableInterviewSlots",
                    width : 90,
                    sortable : true,
                    cssClass : "cellalignright"
                },
                /*
                 * this column is used to show the store number where this event
                 * is created in the grid
                 */
                {
                    id : "eventCreatedByStore",
                    name : "<div style=line-height:35px title='Created By Store'>Created By Store</div>",
                    field : "eventCreatedByStore",
                    width : 115,
                    sortable : true,
                    cssClass : "cellalignright"
                },
                /*
                 * this column is used to show the count of the participating
                 * stores in the grid
                 */
                {
                    id : "numParticipatingStores",
                    name : "<div style='line-height:20px;white-space:normal' title='# Stores Participating'># Stores Participating</div>",
                    field : "numParticipatingStores",
                    width : 140,
                    sortable : true,
                    cssClass : "cellalignright",
                    formatter : this.participatingStoresToolTip
                } ];
        return columns;
    };
    /*  this function is used to show tool tip for participating stores*/
    this.participatingStoresToolTip = function(row, cell, value, columnDef,
            dataContext) {
        var rowObject = dataContext;
        return "<div title='" + rowObject.participatingStores + "'>" + value
                + "</div>";
    };
    /*
     * Method is used to build the grid using
     * predefined properties which is in turn
     * of the slik gird
     */
    this.loadGrid = function() {    	
        var data = CONSTANTS.hiringEventView.data;
        var options = {
            enableColumnReorder : true,
            headerRowHeight : 35,
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
        //initialize the grid
        this.hiringEventGrid = new Slick.Grid("#hiringEventGrid", data,
                columns, options);
        /*
         * this function is called to handle the grid events like click,sort
         * etc.
         */
        this.gridEvents();
        $("#hiringEventGrid .slick-row .slick-cell:first-child").addClass("textEllipsis");
        };
        
    /*
     * this function is called to handle click,sort in the grid
     */
    this.gridEvents = function() {
        $("#addCalendarBtn").attr("title","Create a new Hiring Event");
        $("#edithiringEvent").attr("title","Select a Hiring Event Row to Edit a Hiring Event");
        $("#deleteHiringEventBtn").attr("title","Select a Hiring Event Row to Delete a Hiring Event.");
        /*
         * this is called when we click any row in the grid
         */
        this.hiringEventGrid.onClick
                .subscribe(function(e, args) {
                    var row = args.grid.getData()[args.row];
                    CONSTANTS.calendarView.selectedRow = row;
                    /*
                     * check whether requisition count is zero or not if it is
                     * zero enable both delete and rename button else enable
                     * rename button alone
                     */
                    if (row.activeRequisitionCount === 0) {
                    	
                        $("#edithiringEvent").removeAttr('disabled');
                        
                        $("#edithiringEvent").removeClass("disablegreybtn");
                        $("#edithiringEvent").addClass("darkOrangeBtn");
                        
                        $("#deleteHiringEventBtn").addClass("darkOrangeBtn");
                        $("#addCalendarBtn").attr("title",
                                "Create a new Hiring Event");
                        $("#edithiringEvent").attr("title",
                                "Click to edit Hiring Event '"
                                        + row.requisitionCalendarDescription+ "'");
                        $("#deleteHiringEventBtn").removeClass("disablegreybtn");
                        $("#deleteHiringEventBtn").removeAttr('disabled');
                        $("#deleteHiringEventBtn").attr('onClick', 'hiringEvent.deleteButtonClick()');
                        $("#deleteHiringEventBtn").attr("title",
                                "Click to delete Hiring Event'"
                                        + row.requisitionCalendarDescription+ "'");
                    } else {
                        $("#edithiringEvent").removeAttr('disabled');
                        $("#edithiringEvent").removeClass("disablegreybtn");
                        $("#edithiringEvent").addClass("darkOrangeBtn");
                        $("#deleteHiringEventBtn").attr('disabled', 'disabled');
                        $("#deleteHiringEventBtn").removeClass("darkOrangeBtn");
                        $("#deleteHiringEventBtn").addClass("disablegreybtn");
                        // Remove the click event attribute. Fix for the Chrome.
                        $("#deleteHiringEventBtn").removeAttr('onClick');
                        $("#addCalendarBtn").attr("title","Create a new Hiring Event");
                        $("#edithiringEvent").attr("title",
                                "Click to edit Hiring Event '"
                                        + row.requisitionCalendarDescription+ "'");
                        $("#deleteHiringEventBtn")
                                .attr("title","Hiring Event '"+ row.requisitionCalendarDescription
                                  + "'cannot be deleted because there is an Active Requisition attached.");
                    }
                    
                });
        /*
         * this is used to sort the hiring event grid
         */
        this.hiringEventGrid.onSort.subscribe(function(e, args) {
            UTILITY.gridSorter(args.sortCol.field,
                    args.sortAsc, currentObject.hiringEventGrid,
                    CONSTANTS.hiringEventView.data);
            $("#hiringEventGrid .slick-header-column span  div").css("width","");
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
        this.hiringEventGrid.setSortColumn("hireEventBeginDate",false);
        this.hiringEventGrid.onColumnsResized.subscribe(function(e){
            $.each($("#hiringEventGrid .slick-header-column"),function(index,value){
                $(value).find(".slick-column-name div").css("width","100%");
                if($(value).find(".slick-sort-indicator-desc").length >0 || $(value).find(".slick-sort-indicator-asc").length >0){
                    var fullwidth = $(value).find(".slick-column-name div").width();
                    var redWidth = fullwidth-15;
                    $(value).find(".slick-column-name div").css("width",redWidth+"px");
                }
            });
        });
        /*
         * this is used to call the hiring event summary page once hiring event
         * name is clicked
         */
        $("#hiringEventGrid").off("click", ".hiringEventIdIdLink");
        $("#hiringEventGrid").on("click", ".hiringEventIdIdLink", function(e) {
            CONSTANTS.hiringEventView.navigateToCalendarDetailsScreen(e);
            $("#tabMessageBarLabel").html("Select a day below...");
            $("#hiringList").attr("title",CONSTANTS.HIRING_SUMMARY_TOOLTIP);
        });
        $("#edithiringEvent").attr('disabled', 'disabled');
        $("#deleteHiringEventBtn").attr('disabled', 'diabled');
        $("#edithiringEvent").removeClass("darkOrangeBtn");
        $("#deleteHiringEventBtn").removeClass("darkOrangeBtn");
        $("#edithiringEvent").addClass("disablegreybtn");
        $("#deleteHiringEventBtn").addClass("disablegreybtn");
        // fix for Chrome to remove the onClick attribute.
        $("#deleteHiringEventBtn").removeAttr('onClick');
    };
    /*
     * Methos is used to sort the grid in order of default
     * sort.
     * Default sort will be always as ASC
     */
    this.gridSorter = function(columnField, isAsc, grid, gridData) {
        var sign = isAsc ? 1 : -1;
        var field = columnField;
        //sort the row data
        gridData.sort(function(dataRow1, dataRow2) {
            var value1 = dataRow1[field], value2 = dataRow2[field];
            var result = (value1 === value2) ? 0 : ((value1 > value2 ? 1 : -1))
                    * sign;
            return result;
        });
        grid.invalidate();
        //render the whole grid
        grid.render();
    };
    /*
     * Method is used to return the value in
     * the format(mm/dd/yyyy)
     */
    this.dateFormatter = function(row, cell, value) {
      var dateSplitted="";
      var dateValue = value.substring(0,10);
      var timeStamp = value.substring(11,19);
      dateValue = dateValue.split("-");
      dateSplitted = dateValue[1]+"/"+dateValue[2]+"/"+dateValue[0]+" "+timeStamp;
      var dateCreate = new Date(dateSplitted);
      dateCreate = ("0" + (dateCreate.getMonth() + 1)).slice(-2) + "/"
                + ("0" + dateCreate.getDate()).slice(-2) + "/"
                + dateCreate.getFullYear();
        return dateCreate;
    };
    /*
     * this function is calling the service requistion hiring event
     */
    this.loadData = function() {
        $("#addCalendarBtn").attr("title",
        "Create a new Hiring Event");
        CONSTANTS.hiringEventView = this;
        var callbackFunction = $.Callbacks('once');
        callbackFunction
                .add(CONSTANTS.hiringEventView.getResultForRequisitionHiringEvents);
        var RSAServices = new rsaServices();
        var hiringEventTaskUrl = CONSTANTS.hiringEventTaskService;
        /*
         * Get the Hiring Event List to show on the Hiring Event Summary
         * Page...
         */
        RSAServices.requisitionHiringEvents(callbackFunction,
                hiringEventTaskUrl, MODEL.StoreNumber);
    };
    /*
     * this function is the success handler for the service requistion hiring
     * event
     */
    this.getResultForRequisitionHiringEvents = function(successJson) {
        var response = successJson.Response;
        //check the response has status and ScheduleDescList
        if (response.status) {
        	//initialise the hiring event data, because this is not cleared when there is no data, for the last hiring event.
        	CONSTANTS.hiringEventView.data = [];
            if (response.ScheduleDescList && response.ScheduleDescList.ScheduleDescriptionDetails !== null) {
                if (_
                        .isArray(response.ScheduleDescList.ScheduleDescriptionDetails)) {
                    CONSTANTS.hiringEventView.data = response.ScheduleDescList.ScheduleDescriptionDetails;
                  } else {
                    CONSTANTS.hiringEventView.data = [ response.ScheduleDescList.ScheduleDescriptionDetails ];
                }
            }
            else{
                CONSTANTS.hiringEventView.removePopupCss();
                CONSTANTS.hiringEventView.blockPopup('#alertPopupModal');
                CONSTANTS.hiringEventView.removeAlertCss();
                $('#alertPopupModal .modal-content').addClass("storeNotActive");
                $('#alertPopupModal .modal-body').addClass("longModalBody");
                $('#alertPopupModal .alertModalBody').addClass("storeNotActiveBody");
                $('#alertPopupModal').modal('show');
                $(".alertModalBody")
                        .html("<div class='cols-xs-12 cols-xs-offset-1 cellalignleft'>Store "+ MODEL.StoreNumber+" is not currently participating in</div>" +
                                "<div class='cols-xs-12 cols-xs-offset-1 cellalignleft'>any Hiring Events.  Please feel free to</div>" +
                                "<div class='cols-xs-12 cols-xs-offset-1 cellalignleft'>create a New Hiring Event.</div>");
            }
        }
        /*
         * Check response has error
         */
        else if (response.error) {
            CONSTANTS.hiringEventView.removePopupCss();
            CONSTANTS.hiringEventView.blockPopup('#alertPopupModal');
            CONSTANTS.hiringEventView.removeAlertCss();
            $('#alertPopupModal').modal('show');
            $(".alertModalBody").text(response.error.errorMsg);
        }else{
            CONSTANTS.hiringEventView.removePopupCss();
            CONSTANTS.hiringEventView.blockPopup('#alertPopupModal');
            CONSTANTS.hiringEventView.removeAlertCss();
            $('#alertPopupModal').modal('show');
            $(".alertModalBody").text(CONSTANTS.RequisitionFaultMessage);
        }
        //sort the data
        CONSTANTS.hiringEventView.data = _.sortBy(CONSTANTS.hiringEventView.data,function(obj){
            return new Date(obj.hireEventBeginDate).getTime();
        });
        //sort the whole data by descending order
        CONSTANTS.hiringEventView.data.reverse();
        //Load the grid
        CONSTANTS.hiringEventView.loadGrid();

    };
    /*
     * Method is used to navigate to hiring event summary screen once
     * link of the hiring event name is clicked
     */
    this.navigateToCalendarDetailsScreen = function(e) {
        var calenderDetailId = "";
        $("#hiringEventTask").hide();
        $("#hiringCalendarDiv").show();
        var calendarIdType="";
        //Get the current calendar id on click of calendar summary link
        if(e && e.currentTarget){
        	calendarIdType = $(e.currentTarget).attr('id');
        }
        var hiringSelectControl = $("#hiringSelect");
        $(hiringSelectControl).empty();
        //Iterate the hiringEventView list
        for ( var desc = 0; desc < CONSTANTS.hiringEventView.data.length; desc++) {
            //Form the id with neccasity parameters
            calenderDetailId = CONSTANTS.hiringEventView.data[desc].requisitionCalendarId
                    + ":"
                    + CONSTANTS.hiringEventView.data[desc].hireEventBeginDate
                    + ":"
                    + CONSTANTS.hiringEventView.data[desc].hireEventEndDate
                    + ":" + CONSTANTS.hiringEventView.data[desc].hireEventId;
            //Append the caelndar dropdown with the values
            $(hiringSelectControl)
                    .append(
                            '<option id='
                                    + calenderDetailId
                                    + ' value="'
                                    + CONSTANTS.hiringEventView.data[desc].requisitionCalendarDescription.toString().replace(/\s/g, '')
                                    + '">'
                                    + CONSTANTS.hiringEventView.data[desc].requisitionCalendarDescription.toString()
                                    + '</option>');
        }
        //set the dropdown
        $("#hiringSelect option[value=" + '"' + $(e.currentTarget).text().toString().replace(/\s/g, '') + '"' +"]")
        .attr('selected', 'selected');
        $("#hiringSelect").data("selectBox-selectBoxIt").refresh();
        if ($("#hiringSelect").data("selectBox-selectBoxIt")) {
            $("#hiringSelect").data("selectBox-selectBoxIt").refresh();
        }
        //initialize the hiring event calendar
        var hiringCalendarView = new showHiringCalendar();
        $(".hiringCalendarText").empty();
        $(".hiringCalendarText").html(CONSTANTS.HIRING_CALENDAR_MESSAGE);
        hiringCalendarView.initialize(calendarIdType);
    };
    /*
     * this function is used to call the create hiring event pop up
     */
    this.createNewStoreCalendar = function() {
        CONSTANTS.HiringEventStoreList = [];
        $('#createHiringEventPopUp').off("shown.bs.modal");
        $('#createHiringEventPopUp').on("shown.bs.modal", createHiringEventObj.initialize.bind(createHiringEventObj));
        CONSTANTS.hiringEventView.blockPopup('#createHiringEventPopUp');
        CONSTANTS.hiringEventView.removeAlertCss();
        $("#createHiringEventPopUp").draggable({
             handle: ".modal-header"
         });
        $("#createHiringEventPopUp").find('input').val('');
    };
    /*
     * when create hiring event is create success fully need to load the grid
     * with calling service requisition hiring event
     */
    this.successCreate = function() {
        this.loadData();
    };
    /*
     * Method is called when delete button is clicked
     */
    this.deleteButtonClick = function() {
        $("#deletewarningpopup #WarningModalLabel").text("Warning");
        $(".cautionMsg")
                .text(
                        "CAUTION, you are about to delete '"
                                +CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription+".'");
        $(".sureMsg").text("Are You Sure?");
        $("#oldRenameCalName")
                .text(
                        "'"
                                + CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription
                                + "'");
        CONSTANTS.hiringEventView.blockPopup('#deletewarningpopup');
        CONSTANTS.hiringEventView.removeAlertCss();
        $("#deletewarningpopup").modal('show');
    };
    /*
     * Method is called when we click yes in delete warning pop up which
     * is caling delete hiring event service
     */
    this.yesClicked = function() {
        CONSTANTS.hiringEventView = this;
        var completeDesc = CONSTANTS.calendarView.selectedRow.eventCreatedByStore
                + "~|~"
                + CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription;
        var reqCalDesc = CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription;
        var requisitionCalendarId = CONSTANTS.calendarView.selectedRow.requisitionCalendarId;
        var lastUpdateTimestamp = CONSTANTS.calendarView.selectedRow.lastUpdateTimestamp;
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(CONSTANTS.hiringEventView.deleteSuccess);
        this.deleteHiringEventURL = CONSTANTS.deleteHiringEventURL;
        var RSAServices = new rsaServices();
        RSAServices.deleteHiringEvent(callbackFunction,
                this.deleteHiringEventURL, completeDesc, reqCalDesc,
                requisitionCalendarId, lastUpdateTimestamp);
    };
    /*
     * Method is used for success handler of delete hiring event which in turn
     * call the service requisition hiring event to reload the data
     */
    this.deleteSuccess = function(json) {
        var response = json.calendarResponse;
        //check the response returns error
        if (response.error) {
            if (_.isArray(response.error)) {
                CONSTANTS.hiringEventView.resultList = response.error;
            } else {
                CONSTANTS.hiringEventView.resultList = [ response.error ];
            }
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.hiringEventView.resultList);
            CONSTANTS.hiringEventView.removePopupCss();
            CONSTANTS.hiringEventView.blockPopup('#alertPopupModal');
            CONSTANTS.hiringEventView.removeAlertCss();
            $("#alertPopupModal .modal-body ").addClass("autoHeight");
            $('#alertPopupModal').modal('show');
            $(".alertModalBody").text(returnedErrorMessage);
        } else if (response.reqnCalId) {
            $("#deletewarningpopup").modal('hide');
            CONSTANTS.hiringEventView.removePopupCss();
            CONSTANTS.hiringEventView.blockPopup('#alertPopupModal');
            CONSTANTS.hiringEventView.removeAlertCss();
            $('#alertPopupModal').modal('show');
            $("#alertPopupModal #alertPopupModalLabel").text(
                    "Hiring Event Delete Confirmation");
            $(".alertModalBody")
                    .text(
                            "Selected Hiring Event '"
                                    + CONSTANTS.calendarView.selectedRow.requisitionCalendarDescription
                                    + "' was Deleted.");
            $("#alertPopupModal .modal-content").addClass("largeModal");
            $("#alertPopupModal .modal-body ").addClass("largeBody");
            $("#alertPopupModal .alertModalBody").addClass("alignModal");
            CONSTANTS.hiringEventView.loadData();
        }
    };
    /*
     * Method is called when we click any row in the grid and click on
     * edit hiring event button which will calll the service edit hiring event
     * detail to get detail of that hiring event
     */
    this.editHiringEvent = function() {
        MODEL.interviewsScheduled = false;
        //check the scheduledInterviewSlots greater than 0
        if (CONSTANTS.calendarView.selectedRow.scheduledInterviewSlots > 0) {
            MODEL.interviewsScheduled = true;
        }
        MODEL.reqnCalId = CONSTANTS.calendarView.selectedRow.requisitionCalendarId;
        $.blockUI();
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(CONSTANTS.hiringEventView.editSuccess);
        //service URL for editHiringEventDetailURL
        this.editHiringEventDetailURL = CONSTANTS.editHiringEventDetailURL;
        var RSAServices = new rsaServices();
        //service call for edit hiring event
        RSAServices.editHiringEventDetail(callbackFunction,
                this.editHiringEventDetailURL,
                CONSTANTS.calendarView.selectedRow.hireEventId,
                CONSTANTS.calendarView.selectedRow.requisitionCalendarId,
                MODEL.interviewsScheduled);
    };
    /*
     * Method for success handler for edit hiring event detail service
     * which will assign the values from the response
     */
    this.editSuccess = function(json){
        var response = json.Response;
        var returnedErrorMessage = "";
        //check the response has error
        if (response.error) {
            if (_.isArray(response.error)) {
                returnedErrorMessage = response.error.errorMsg;
            } else {
                returnedErrorMessage = [ response.error.errorMsg ];
            }
            CONSTANTS.hiringEventView.removePopupCss();
            CONSTANTS.hiringEventView.blockPopup('#alertPopupModal');
            CONSTANTS.hiringEventView.removeAlertCss();
            $("#alertPopupModal .alertModalBody").text(returnedErrorMessage);
            $("#alertPopupModal #alertPopupModalLabel").text(CONSTANTS.EDIT_HIRING_EVENT_ERROR);
        } else {
            //check the response object has hiringEventResponse
            if (response.hiringEventResponse) {
                MODEL.hiringEventDetail = {};
                MODEL.hiringEventMgrData = {};
                if (response.hiringEventResponse.hiringEventMgrData) {
                    MODEL.hiringEventMgrData.eventMgrName = response.hiringEventResponse.hiringEventMgrData.name;
                    MODEL.hiringEventMgrData.eventMgrTitle = response.hiringEventResponse.hiringEventMgrData.title;
                    MODEL.hiringEventMgrData.eventMgrPhone = response.hiringEventResponse.hiringEventMgrData.phone;
                    MODEL.hiringEventMgrData.eventMgrEmail = response.hiringEventResponse.hiringEventMgrData.email;
                    MODEL.hiringEventMgrData.eventMgrAssociateId = response.hiringEventResponse.hiringEventMgrData.associateId;
                }
                /*
                 * check the response has hiringEventResponse.hiringEventDetail object
                 */
                if (response.hiringEventResponse.hiringEventDetail) {
                    //Get and set the response value in to an model object
                    MODEL.hiringEventDetail.eventName = response.hiringEventResponse.hiringEventDetail.hireEventName;
                    MODEL.hiringEventDetail.hireEventId = response.hiringEventResponse.hiringEventDetail.hireEventId;
                    MODEL.hiringEventDetail.reqnCalId = MODEL.reqnCalId;
                    MODEL.hiringEventDetail.eventDate = response.hiringEventResponse.hiringEventDetail.hireEventBeginDate;
                    MODEL.hiringEventDetail.orginalEventDate = response.hiringEventResponse.hiringEventDetail.hireEventBeginDate;
                    MODEL.hiringEventDetail.eventDateEnd = response.hiringEventResponse.hiringEventDetail.hireEventEndDate;
                    MODEL.hiringEventDetail.orginalEventDateEnd = response.hiringEventResponse.hiringEventDetail.hireEventEndDate;
                    MODEL.hiringEventDetail.venueStoreName = response.hiringEventResponse.hiringEventDetail.hireEventLocationDescription;
                    MODEL.hiringEventDetail.eventAddress1 = response.hiringEventResponse.hiringEventDetail.hireEventAddressText;
                    MODEL.hiringEventDetail.eventCity = response.hiringEventResponse.hiringEventDetail.hireEventCityName;
                    MODEL.hiringEventDetail.eventState = response.hiringEventResponse.hiringEventDetail.hireEventStateCode;
                    MODEL.hiringEventDetail.eventZip = response.hiringEventResponse.hiringEventDetail.hireEventZipCodeCode;
                    MODEL.hiringEventDetail.hiringEventType = response.hiringEventResponse.hiringEventDetail.hireEventTypeIndicator;
                    MODEL.hiringEventDetail.hireEventCreatedByStore = response.hiringEventResponse.hiringEventDetail.hireEventCreatedByStore;
                    /*
                     * this function is called to set the hiring event type
                     * based on the response
                     */
                    CONSTANTS.hiringEventView.sethiringEventType();
                }
                /*
                 * this function is called to set the no of participating stores
                 * in the grid based on the response
                 */
                CONSTANTS.hiringEventView.setParticipatingStore(response);
                $("#editHiringEventPopUp").off("shown.bs.modal");
                $('#editHiringEventPopUp').on("shown.bs.modal",
                        editHiringEvent.initialize.bind(editHiringEvent));
                $("#editHiringEventPopUp").modal("show");
                $("#editHiringEventPopUp").modal({
                    "backdrop" : "static",
                    "keyboard" : false
                });
                /*
                 * Drag a popup
                 */
                $("#editHiringEventPopUp").draggable({
                    handle : ".modal-header"
                });
            }
        }
    };
    /*
     * Method is used to set the hiring
     *  event type based on the response
     */
    this.sethiringEventType = function() {
        //if hiringEventType is OSE the set y else N
        if (MODEL.hiringEventDetail.hiringEventType === "OSE") {
            MODEL.hiringEventDetail.offSiteEvent = "Y";
        } else {
            MODEL.hiringEventDetail.offSiteEvent = "N";
        }
    };
    /*
     * Method is used to find the participating
     *  stores are array or not
     */
    this.setParticipatingStore = function(response) {
        if (response.hiringEventResponse.hiringEventParticipatingStores
                && response.hiringEventResponse.hiringEventParticipatingStores[0].HiringEventsStoreDetails) {
            if (_
                    .isArray(response.hiringEventResponse.hiringEventParticipatingStores[0].HiringEventsStoreDetails)) {
                CONSTANTS.hiringEventView.resultList = response.hiringEventResponse.hiringEventParticipatingStores[0].HiringEventsStoreDetails;
            } else {
                CONSTANTS.hiringEventView.resultList = [ response.hiringEventResponse.hiringEventParticipatingStores[0].HiringEventsStoreDetails ];
            }
            this.setValueForParticipatingStores();
        }
    };
    /*
     * this function is used to set the value for participating stores based on
     * the response
     */
    this.setValueForParticipatingStores = function() {
        //check the data is not empty or length is not 0
        if (CONSTANTS.hiringEventView.resultList !== null
                && CONSTANTS.hiringEventView.resultList.length > 0) {
            MODEL.hiringEventDetail.participatingStores = [];
            for ( var m = 0; m < CONSTANTS.hiringEventView.resultList.length; m++) {
                CONSTANTS.hiringEventView.hiringEventStoreDetailsVO = {};
                CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.number = CONSTANTS.hiringEventView.resultList[m].strNum;
                CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.name = CONSTANTS.hiringEventView.resultList[m].strName;
                CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.storeDeleteAllowed = CONSTANTS.hiringEventView.resultList[m].storeDeleteAllowed;
                CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.eventSiteFlg = CONSTANTS.hiringEventView.resultList[m].eventSiteFlg;
                // Sets the Host/Event Held at Store Number
                if (CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.eventSiteFlg === "Y") {
                    MODEL.hiringEventDetail.strNumber = CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.number;
                }
                CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.hireEventId = CONSTANTS.hiringEventView.resultList[m].hireEventId;
                CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.activeReqCount = CONSTANTS.hiringEventView.resultList[m].activeReqCount;
                if (CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.activeReqCount > 0) {
                    CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.activeReqToolTip = CONSTANTS.hiringEventView.hiringEventStoreDetailsVO.activeReqCount
                            + " Active Requisition(s)";
                }
                MODEL.hiringEventDetail.participatingStores
                        .push(CONSTANTS.hiringEventView.hiringEventStoreDetailsVO);
            }
        }
    };
    /*
     * Method for on click of "ok"the
     * popup will hide
     */
    this.okAlertClicked = function() {
        $("#alertPopupModal").modal("hide");
    };
    /*
     * Method for removing the common css class
     * for alert popup
     */
    this.removePopupCss = function() {
        $("#alertPopupModal .modal-content").removeClass("largeModal");
        $("#alertPopupModal .modal-body ").removeClass("largeBody");
        $("#alertPopupModal .alertModalBody").removeClass("alignModal");
    };
    /*
     * Method has css class to remove
     * for alertPopupModal popup
     */
    this.removeAlertCss = function() {
        $("#alertPopupModal .modal-content").removeClass("largeModal");
        $("#alertPopupModal .modal-body ").removeClass("largeBody");
        $("#alertPopupModal .modal-content").removeClass("largestoreModal");
        $("#alertPopupModal .modal-body").removeClass("largeBodystoreModal");
        $("#alertPopupModal .modal-content").removeClass("smallalertmodal");
        $("#alertPopupModal .modal-body").removeClass("smallalertModal");
        $("#alertPopupModal .alertModalBody").removeClass("alignalertModal");
        $("#alertPopupModal .modal-content").removeClass("longModalContent");
        $("#alertPopupModal .modal-body ").removeClass("longModalBody");
        $("#alertPopupModal .alertModalBody").removeClass("alignModal");
        $('#alertPopupModal .modal-content').removeClass("storeNotActive");
        $('#alertPopupModal .alertModalBody').removeClass("storeNotActiveBody");
        $('#alertPopupModal .modal-body').removeClass("longModalBody");
        $("#alertPopupModal .modal-body ").removeClass("autoHeight");
        $("#alertPopupModal #alertModalLabel").html("");
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
    this.blockPopup = function(popupname) {
        $(popupname).modal({
            backdrop : 'static',
            keyboard : false
        });
    };
    /*
     * Keypress event for button presented
     * in popups
     * on press of enter button should in focus and
     * popup should closed
     */
    $("#hiringAlert").keypress(function(evt){
        if(evt && evt.keyCode===13){
            $("#alert").modal('hide');
        }
    });
    /*
     * Keypress event for button presented
     * in popups
     * on press of enter button should in focus and
     * popup should closed
     */
    $("#alertPopupModal").keypress(function(evt){
        if(evt && evt.keyCode===13){
            $("#alert").modal('hide');
        }
    });
};
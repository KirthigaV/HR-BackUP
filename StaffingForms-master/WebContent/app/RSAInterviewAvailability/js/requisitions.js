/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: requisition.js
 * Application: Staffing Administration
 *
 * Used to track the requisition details for the given store number.
 * This method has the requisition details for respective store number,
 *
 * @param inputXml
 * XML/JSON containing the store number for requisition details.
 *
 * @param version
 * Optional parameter, can be used later to change the request/response/logic that gets
 * applied. If not provided, it will be defaulted to 1.
 * Version 1 XML Input/Output,
 * version 2 JSON Input/Output
 *
 * @return
 * XML/JSON containing the requisition details for
 * data provided.
 * This XML/JSON is generated and return the response
 * CONSTANTS.requisitionView.data
 */
/**
 * Method to check the requisition details with respective
 * filters.
 * Filters have department,calendar name and job title.
 * Can check all the requisitions
 */
function requisitionTab(){
    /*
     * this function is called when we click requisition tab
     */
    var currentObject = this;
    this.initialize = function() {
        $('select:not(select[multiple])').selectBoxIt();
        $("#deptFilter").attr("title",CONSTANTS.REQUISITION_DEPT);
        $("#jobFilter").attr("title",CONSTANTS.REQUISITION_JOBTITLE);
        $("#calendarFilter").attr("title",CONSTANTS.REQUISITION_CALENDAR);
        $(window).on( "resize",
                function() {
                    var $grid_requisitiongrid = $("#grid_Summary"), newWidth = $grid_requisitiongrid
                            .closest(".ui-jqgrid").parent().width();
                    $grid_requisitiongrid.jqGrid("setGridWidth",
                            newWidth, true);
                });
        /*
         * Declare the objects and array
         */
        CONSTANTS.requisitionView = {};
        CONSTANTS.requisitionView.data = [];
        /*
         * this function is called to call the service active requisition for
         * that store
         */
        this.loadData();
    };
    /*
     * this method is used to set the options for the requistion grid
     */
    this.getOptions = function() {
        var options = {
            enableColumnReorder : false,
            headerRowHeight : 25,
            rowHeight : 25,
            forceFitColumns : true,
            enableCellNavigation : true
        };
        return options;
    };
    /*
     * this method is used to set the columns for the requistion grid
     */
    this.getColumns = function() {
        var columns = [ {
            /*
             * this column is used to show the requisition number in the grid
             */
            id : "number",
            name : "<div title='Active Requisition Number'>Requisition</div>",
            field : "number",
            width : 80,
            sortable : true
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "createTimestamp",
            name : "<div title='Date Requisition Created'>Date Created</div>",
            field : "createTimestamp",
            width : 85,
            sortable : true,
            formatter : this.dateFormatter
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "departmentNumber",
            name : "<div title='Department Number'>Dept</div>",
            field : "departmentNumber",
            width : 40,
            sortable : true
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "jobTitleDescription",
            name : "<div style=text-align:center title='Job Title'>Job title</div>",
            field : "jobTitleDescription",
            width : 210,
            sortable : true,
            cssClass : "cellalignleft"
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "openPositionCount",
            name : "<div title='Number Of Openings'># of Openings</div>",
            field : "openPositionCount",
            width : 70,
            sortable : true
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "fullTimeRequired",
            name : "<div title='Full Time / Part time / Both'>FT/PT/B</div>",
            field : "fullTimeRequired",
            width : 50,
            sortable : true,
            formatter : this.jobTypeformatter
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "interviewMinutes",
            name : "<div title='Interview Length'>Intv Length</div>",
            field : "interviewMinutes",
            width : 60,
            sortable : true
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "interviewCompletedCount",
            name : "<div title='Number of Interviews Requested / Number \nScheduled / Number Completed'># Intv Requested / #scheduled / #completed</div>",
            field : "interviewCompletedCount",
            width : 130,
            sortable : true,
            formatter : this.count
        },
        /*
         * this column is used to show the requisition number in the grid
         */
        {
            id : "hiringEvent",
            name : "<div style=text-align:center title='Calendar Name Requisition is Associated\n with.'>Cal.Name</div>",
            field : "hiringEvent",
            width : 160,
            sortable : true,
            cssClass : "cellalignleft",
            formatter : this.calendarNameFormatter
        } ];
        return columns;
    };
    /*
     * this function is used to build the grid with the response from active
     * requisition service
     */
    this.loadGrid = function(gridData) {
        var data = gridData;
        var options = this.getOptions();
        var columns = this.getColumns();
        if(data.length <= 0 || data===undefined){
        	columns = _.map(columns,function(value,key){
        		value.sortable = false;
        		return value;
        	});
        }
            this.requisitionGrid = new Slick.Grid("#requisitionGrid", data,
                columns, options);
            /*
             * this function is used to call the sort functionality when header is
             * clicked
             */
            this.requisitionGrid.onSort.subscribe(function(e, args) {
                this.requisitionGridSorter(args.sortCol.field,
                        args.sortAsc, currentObject.requisitionGrid,
                        data);
                $("#requisitionGrid .slick-header-column span  div").css("width","");
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

            }.bind(this));
             this.requisitionGridSorter = function(columnField, isAsc, grid, gridData){
                    var sign = isAsc ? 1 : -1;
                    var field = columnField;
                    //sort the row data
                    gridData.sort(function(dataRow1, dataRow2) {
                        var value1 = dataRow1[field], value2 = dataRow2[field];
                        if(field === "fullTimeRequired"){
                            value1 = this.jobTypeformatter(null,null,value1,null,dataRow1);
                            value2 = this.jobTypeformatter(null,null,value2,null,dataRow2);
                        }
                        else if(field === "interviewCompletedCount"){
                            value1 = this.count(null,null,value1,null,dataRow1);
                            value2 = this.count(null,null,value2,null,dataRow2);
                        }
                        else if(field === "interviewMinutes"){
                            value1 = ($.trim(dataRow1[field]) === "")?0:parseInt($.trim(dataRow1[field]));
                            value2 = ($.trim(dataRow2[field]) === "")?0:parseInt($.trim(dataRow2[field]));
                        }
                        else if(field === "hiringEvent"){
                            value1 = $.trim(this.calendarNameFormatter(null,null,value1,null,dataRow1));
                            value2 = $.trim(this.calendarNameFormatter(null,null,value2,null,dataRow2));
                        }
                        var result = (value1 === value2) ? 0 : ((value1 > value2 ? 1 : -1))
                                * sign;
                        return result;
                    }.bind(this));
                    grid.invalidate();
                    //render the whole grid
                    grid.render();
                };
                this.requisitionGrid.onColumnsResized.subscribe(function(){
                    $.each($("#requisitionGrid .slick-header-column"),function(index,value){
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
     * Method for set title to grid fields
     */
    this.formatFunction = function(row, cell, value) {
        return "<div title='" + value + "'>" + value + "</div>";

    };
    /*
     * this function is used to format the value as FT/PT/Both this depends on
     * parttime required or full time required object
     */
    this.jobTypeformatter = function(row, cell, value, columnDef, dataContext) {
        if($.trim(value) === ""){
            return "";
        }
        var rowObject = dataContext;
        /*
         * if both fulltime required and parttime required is true then set both
         * as a value
         */
        if (rowObject.fullTimeRequired
                && rowObject.partTimeRequired) {
            return "Both";
        }
        /*
         * if full time required alone is true then set FT
         */
        else if (rowObject.fullTimeRequired
                && !rowObject.partTimeRequired) {
            return "FT";
        }
        /*
         * if part time required alone is true then set FT
         */
        else if (!rowObject.fullTimeRequired
                && rowObject.partTimeRequired) {
            return "PT";
        }
    };
    /*
     * this function is used to format the column calendar name if the
     * requisition calendar object is present then ser value as requisition
     * calendar else set ""
     */
    this.calendarNameFormatter = function(row, cell, value, columnDef,
            dataContext) {
        if($.trim(value) === ""){
            return "";
        }
        var rowObject = dataContext;
        if (rowObject.requisitionCalendar !== null
                && rowObject.requisitionCalendar !== undefined) {
            return rowObject.requisitionCalendar.description;
        } else {
            return "";
        }
    };
    /*
     * this function is used to display # Intv Requested/#scheduled/#completed
     * column in the format candidatecount/scheduledcount/completedcount
     */
    this.count = function(row, cell, value, columnDef, dataContext) {
        if($.trim(value) === ""){
            return "";
        }
        var rowObject = dataContext;
        return rowObject.interviewCandidateCount + ' / '
                + rowObject.interviewScheduledCount + ' / '
                + rowObject.interviewCompletedCount;
    };
    /*
     * Method is used to format the date from
     * the parameters
     */
    this.dateFormatter = function(row, cell, value) {
        if($.trim(value) === ""){
            return "";
        }
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
     * method is used to call the service active requisition
     */
    this.loadData = function() {
        CONSTANTS.requisitionView = this;
        var callbackFunction = $.Callbacks('once');
        callbackFunction
                .add(CONSTANTS.requisitionView.getResultForRequisitionSummary);
        var RSAServices = new rsaServices();
        //service call for Requisition summary
        var requisitionEventTaskUrl = CONSTANTS.requisitionTaskService;
        RSAServices.activeRequisitionsForStore(callbackFunction,
                requisitionEventTaskUrl, MODEL.StoreNumber);
    };
    /*
     * Method is used for the success handler of active requisition service
     */
    this.getResultForRequisitionSummary = function(successJson) {
        var response = successJson.requisitionResponse;
        //check if response has error
        if (response.hasOwnProperty("error")) {
            if (_.isArray(response.error)) {
                this.resultList = response.error;
            } else {
                this.resultList = [ response.error ];
            }
            var errorMessage = MODEL.getErrorMessage(this.resultList);
            CONSTANTS.requisitionView.blockFullPage('#alertRequisition');
            CONSTANTS.requisitionView.removeAlertCss();
            currentObject.setDraggable();
            $("#alertRequisition .modal-body").addClass("autoHeight");
            $('#alertRequisition').modal('show');
            $("#alertRequisition .modal-body .alertModalBody").text(
                    errorMessage);
        }
        /*
         * check if response has requisitions object
         */
        else if (response.hasOwnProperty("requisitions")) {
            if(response.requisitions[0]==="" && response.requisitions[0]!==null){
                 $("#dept").empty();
                 $("#job").empty();
                 $("#calendarName").empty();
                  $("#dept").append('<option>Please select a Dept...</option><option> </option>');
                  $("#job").append('<option>Please select a Job Title...</option><option> </option>');
                  $("#calendarName").append('<option>Please select a Calendar...</option>');
                  $("#dept").data("selectBox-selectBoxIt").refresh();
                  $("#job").data("selectBox-selectBoxIt").refresh();
                  $("#calendarName").data("selectBox-selectBoxIt").refresh();
                  CONSTANTS.requisitionView.loadGrid();
                 CONSTANTS.requisitionView.blockFullPage('#alertRequisition');
                 CONSTANTS.requisitionView.removeAlertCss();
                 currentObject.setDraggable();
                  $('#alertRequisition').modal('show');
                  $("#alertRequisition .modal-body .alertModalBody").text(
                          CONSTANTS.NOACTIVE_REQUISITION);
            }
            else if(response.requisitions !== undefined && response.requisitions !== null){
                if (response.requisitions[0].requisition!==undefined) {
                    if (_.isArray(response.requisitions[0].requisition)) {
                        CONSTANTS.requisitionView.data = response.requisitions[0].requisition;
                    } else {
                        CONSTANTS.requisitionView.data = [ response.requisitions[0].requisition ];
                    }
                    CONSTANTS.requisitionView.setValuesForDropDowns();
                }
            }
            else{
                $("#dept").empty();
                $("#job").empty();
                $("#calendarName").empty();
                $("#dept").append('<option>Please select a Dept...</option><option> </option>');
                 $("#job").append('<option>Please select a Job Title...</option><option> </option>');
                 $("#calendarName").append('<option>Please select a Calendar...</option>');
                 $("#dept").data("selectBox-selectBoxIt").refresh();
                 $("#job").data("selectBox-selectBoxIt").refresh();
                 $("#calendarName").data("selectBox-selectBoxIt").refresh();
                 CONSTANTS.requisitionView.loadGrid();
                 CONSTANTS.requisitionView.blockFullPage('#alertRequisition');
                 CONSTANTS.requisitionView.removeAlertCss();
                 currentObject.setDraggable();
                 $('#alertRequisition').modal('show');
                 $("#alertRequisition .modal-body .alertModalBody").text(
                         CONSTANTS.NOACTIVE_REQUISITION);
            }
            /*
             * this function is called to set the options for the dropdown
             */
        }
    };
    /*
     * this function is used to set the unique options in the
     * dropdowns(departmenr,job title,calendar name)
     */
    this.setValuesForDropDowns = function() {
        CONSTANTS.requisitionView.dept = [];
        CONSTANTS.requisitionView.jobTitle = [];
        CONSTANTS.requisitionView.CalendarName = [];
        CONSTANTS.requisitionView.calendarArray = [ {
            "typeCode" : 0,
            "name" : ""
        } ];
        for (var m = 0; m < CONSTANTS.requisitionView.data.length; m++) {
            CONSTANTS.requisitionView.dept[m] = CONSTANTS.requisitionView.data[m].departmentNumber;
            CONSTANTS.requisitionView.jobTitle[m] = CONSTANTS.requisitionView.data[m].jobTitleDescription;
            CONSTANTS.requisitionView.setCalendarArray(m);
        }
        /*
         * this is used to group the calendar name based on the type code
         */
        var grouped = _.groupBy(CONSTANTS.requisitionView.calendarArray,
                "typeCode");
        /*
         * this is used to sort the calendar name based on the grouped values
         */
        var sortedkeys = _.sortBy(_.keys(grouped), function(num) {
            return parseInt(num);
        });
        /*
         * this is used to find the unique values based on grouped and sorting
         */
        var calObjArr = [];
        for (var k = 0; k < sortedkeys.length; k++) {
            calObjArr = calObjArr.concat(_.sortBy(grouped[sortedkeys[k]],
                    this.returnUpperCase));
        }
        for (var calendarList = 0; calendarList < calObjArr.length; calendarList++) {
            CONSTANTS.requisitionView.CalendarName
                    .push(calObjArr[calendarList].name);
        }
        /*
         * this is used to find the unique department and sort the values
         */
        CONSTANTS.requisitionView.dept = _.uniq(CONSTANTS.requisitionView.dept);
        CONSTANTS.requisitionView.dept = _.sortBy(
                CONSTANTS.requisitionView.dept, function(num) {
                    return Number(num);
                });
        /*
         * this is used to find the unique job title and sort the values
         */
        CONSTANTS.requisitionView.jobTitle = _
                .uniq(CONSTANTS.requisitionView.jobTitle);
        CONSTANTS.requisitionView.jobTitle = _.sortBy(
                CONSTANTS.requisitionView.jobTitle, function(num) {
                    return num;
                });
        /*
         * this function is called to add the values in to the options of the
         * corresponding dropdowns
         */
        CONSTANTS.requisitionView.populateValues();
    };
    /*
     * this function is used to convert the object name into upper case
     */
    this.returnUpperCase = function(obj) {
        return obj.name.toUpperCase();
    };
    /*
     * this function is used to set values for calname and cal type calendar
     * array
     */
    this.setCalendarArray = function(m) {
        if (CONSTANTS.requisitionView.data[m].requisitionCalendar !== null
                && CONSTANTS.requisitionView.data[m].requisitionCalendar !== undefined) {
            var calName = CONSTANTS.requisitionView.data[m].requisitionCalendar.description;
            var calType = CONSTANTS.requisitionView.data[m].requisitionCalendar.calendarTypeCode;
            this.checkDuplicateCalendarNames(calName, calType);
        }
    };
    /*
     * this function is used to check whether duplicate names available in the
     * calendar array
     */
    this.checkDuplicateCalendarNames = function(calName, calType) {
        var calendarFound = false;
        if (calName.length > 0) {
            for (var j = 0; j < CONSTANTS.requisitionView.calendarArray.length; j++) {
                if (CONSTANTS.requisitionView.calendarArray[j].name === calName) {
                    calendarFound = true;
                    break;
                }
            }
            if (!calendarFound) {
                CONSTANTS.requisitionView.calendarArray.push({
                    typeCode : calType,
                    name : calName
                });
            }
        }
    };
    /*
     * this function is used to populate the values into the all
     * dropdowns presented in requisition screens.
     * Refresh the data and set the data in dropdowns.
     */
    this.populateValues = function() {
        $("#dept").empty();
        $("#dept").append('<option>Please select a Dept...</option><option> </option>');
        for (var l = 0; l < CONSTANTS.requisitionView.dept.length; l++) {
            $("#dept").append(
                    '<option>' + CONSTANTS.requisitionView.dept[l]
                            + '</option>');
        }        $("#dept").data("selectBox-selectBoxIt").refresh();
        $("#job").empty();
        $("#job").append('<option>Please select a Job Title...</option><option> </option>');
        for (var n = 0; n < CONSTANTS.requisitionView.jobTitle.length; n++) {
            $("#job").append(
                    '<option>' + CONSTANTS.requisitionView.jobTitle[n]
                            + '</option>');
        }
        $("#calendarName").empty();
        $("#calendarName").append('<option>Please select a Calendar...</option>');
        for (var cn = 0; cn < CONSTANTS.requisitionView.CalendarName.length; cn++) {
            $("#calendarName").append(
                    '<option value="'
                            + CONSTANTS.requisitionView.CalendarName[cn] + '">'
                            + CONSTANTS.requisitionView.CalendarName[cn]
                            + '</option>');
        }
        //Refresh the dept data and set the data in dropdown
        $("#dept").data("selectBox-selectBoxIt").refresh();
        if ($("#dept").data("selectBox-selectBoxIt")) {
            $("#dept").data("selectBox-selectBoxIt").refresh();
        }
      //Refresh the job data and set the data in dropdown
        $("#job").data("selectBox-selectBoxIt").refresh();
        if ($("#job").data("selectBox-selectBoxIt")) {
            $("#job").data("selectBox-selectBoxIt").refresh();
        }
      //Refresh the calendar data and set the data in dropdown
        $("#calendarName").data("selectBox-selectBoxIt").refresh();
        if ($("#calendarName").data("selectBox-selectBoxIt")) {
            $("#calendarName").data("selectBox-selectBoxIt").refresh();
        }
        $("#dept + span ul li:contains('Please')").empty();
        $("#job + span ul li:contains('Please')").empty();
        $("#calendarName + span ul li:contains('Please')").empty();
        CONSTANTS.requisitionView.loadGrid(CONSTANTS.requisitionView.data);
    };
    /*
     * this function is called when filter by dept button is clicked. it will
     * filter the department and load the grid with the selected dept
     */
    this.filterByDept = function() {
        CONSTANTS.requisitionView.deptFilter = _.where(
                CONSTANTS.requisitionView.data, {
                    departmentNumber : $("#dept").val()
                });
        CONSTANTS.requisitionView
                .loadGrid(CONSTANTS.requisitionView.deptFilter);
        $("#calendarName").find('option:eq(0)').attr('selected', 'selected');
        if ($("#calendarName").data("selectBox-selectBoxIt")) {
            $("#calendarName").data("selectBox-selectBoxIt").refresh();
        }
        $("#job").find('option:eq(0)').attr('selected', 'selected');
        if ($("#job").data("selectBox-selectBoxIt")) {
            $("#job").data("selectBox-selectBoxIt").refresh();
        }
        $("#job + span ul li:contains('Please')").empty();
        $("#calendarName + span ul li:contains('Please')").empty();
    };
    /*
     * this function is called when filter by job title button is clicked. it will
     * filter the job title and load the grid with the selected job title
     */
    this.filterByJobTitle = function() {
        CONSTANTS.requisitionView.JobTitleFilter = _.where(
                CONSTANTS.requisitionView.data, {
                    jobTitleDescription : $("#job").val()
                });
        CONSTANTS.requisitionView
                .loadGrid(CONSTANTS.requisitionView.JobTitleFilter);
        $("#calendarName").find('option:eq(0)').attr('selected', 'selected');
        if ($("#calendarName").data("selectBox-selectBoxIt")) {
            $("#calendarName").data("selectBox-selectBoxIt").refresh();
        }
        $("#dept").find('option:eq(0)').attr('selected', 'selected');
        if ($("#dept").data("selectBox-selectBoxIt")) {
            $("#dept").data("selectBox-selectBoxIt").refresh();
        }
        $("#dept + span ul li:contains('Please')").empty();
        $("#calendarName + span ul li:contains('Please')").empty();
    };
    /*
     * this function is called when filter by calendar name button is clicked. it will
     * filter the calendar name and load the grid with the selected calendar name
     */
    this.filterByCalendarName = function() {
        $("#dept").find('option:eq(0)').attr('selected', 'selected');
        if ($("#dept").data("selectBox-selectBoxIt")) {
            $("#dept").data("selectBox-selectBoxIt").refresh();
        }
        $("#job").find('option:eq(0)').attr('selected', 'selected');
        if ($("#job").data("selectBox-selectBoxIt")) {
            $("#job").data("selectBox-selectBoxIt").refresh();
        }
        $("#dept + span ul li:contains('Please')").empty();
        $("#job + span ul li:contains('Please')").empty();
        var temp = [];
        for (var i = 0; i < CONSTANTS.requisitionView.data.length; i++) {
            if (CONSTANTS.requisitionView.data[i].requisitionCalendar !== null
                    && CONSTANTS.requisitionView.data[i].requisitionCalendar !== undefined
                    && CONSTANTS.requisitionView.data[i].requisitionCalendar.description === $("#calendarName option:selected").text()) {
                temp.push(CONSTANTS.requisitionView.data[i]);
            }
        }
        CONSTANTS.requisitionView.loadGrid(temp);
    };
    /*
     * this function is called when clear button is clicked it will clear all
     * filters and reload the grid
     */
    this.clearButtonClick = function() {
        $("#job").find('option:eq(0)').attr('selected', 'selected');
        if ($("#job").data("selectBox-selectBoxIt")) {
            $("#job").data("selectBox-selectBoxIt").refresh();
        }
        $("#job + span ul li:contains('Please')").empty();
         $("#dept").find('option:eq(0)').attr('selected', 'selected');
         if ($("#dept").data("selectBox-selectBoxIt")) {
             $("#dept").data("selectBox-selectBoxIt").refresh();
         }
         $("#dept + span ul li:contains('Please')").empty();
         $("#calendarName").find('option:eq(0)').attr('selected', 'selected');
         if ($("#calendarName").data("selectBox-selectBoxIt")) {
             $("#calendarName").data("selectBox-selectBoxIt").refresh();
         }
         $("#calendarName + span ul li:contains('Please')").empty();
        CONSTANTS.requisitionView.loadGrid(CONSTANTS.requisitionView.data);
    };
    /*
     * this function is called to cvlose the alert
     * pop up once ok is clicked
     */
    this.alertOkClicked = function() {
        $("#alertRequisition").modal('hide');
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
        $("#alertRequisition .modal-body").removeClass("autoHeight");
    };
    /*
     * Keypress event for button presented
     * in popups
     * on press of enter button should in focus and
     * popup should closed
     */
    $("#alertRequisition").keypress(function(evt){
        if(evt && evt.keyCode===13){
            $("#alertRequisition").modal('hide');
        }
    });
    /*
     * Method to drag the popups
     */
    this.setDraggable = function() {
        $("#alertRequisition").draggable({
             handle: ".modal-header"
         });
    };
};
/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: hiringCalendar.js
 * Application:Staffing Administration
 *
 * This method gets hiring calendar details for the data provided
 *
 * @param inputXml
 * XML/JSON containing the parameters for hiring calendar summary
 * is store number,calendar id,begin date and end date.
 *
 * @param version
 * Optional parameter, can be used later to change the request/response/logic that gets
 * applied. If not provided, it will be defaulted to 1.
 * Version 1 XML Input/Output,
 * version 2 JSON Input/Output
 *
 * @return
 * XML/JSON containing the hiring calendar summary and details for
 * respective calendar id.
 *
 */
/**
 * Method name:showHiringCalendar
 * Return a 3 weeks hiring calendar with 21 days.
 * Hiring Calendar will also have a daily basis actions.
 */
function showHiringCalendar(){
    var self = this;
    /*
     * Method loads the calendar with details
     * */
    this.initialize = function(calendarIdType){
        var startDate = "";
        var endDate = "";
        var todayDate = new Date();
        var finishDate = new Date();
        //Add 14 days to set date from current date
        finishDate.setDate(finishDate.getDate() + 14);
        startDate = UTILITY.formatedMonth(todayDate);
        endDate = UTILITY.formatedMonth(finishDate);
      //Extract the value from passed parameter
        if(calendarIdType) {
            //Split the param and get the values
            var splitValue = calendarIdType.split(":");
            MODEL.HiringCalendarId = splitValue[0];
            MODEL.BEGINDATE = splitValue[1];
            MODEL.ENDDATE = splitValue[2];
            MODEL.HireEventId = splitValue[3];
        }
        MODEL.SelectHiringStartDate = startDate;
        MODEL.SelectHiringEndDate = endDate;
        /*Service call for calendar summary*/
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getCalendarSummary);
        var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
        RSASERVICES.calendarSummaryGet(callbackFunction, calendarSumaryUrl,
                startDate, MODEL.HiringCalendarId, endDate);
    };
    /*
     * Method for loading the calendar summary based
     *  on calendar drop down
     *  */
    this.changeCalendarSummary = function(){
        var calendarStartDate = "";
        var calendarEndDate = "";
        var calendarTodayDate = new Date();
        var calendarFinishDate = new Date();
        calendarFinishDate.setDate(calendarFinishDate.getDate() + 14);
        calendarStartDate = UTILITY.formatedMonth(calendarTodayDate);
        calendarEndDate = UTILITY.formatedMonth(calendarFinishDate);
        var hiringCalendarId = $("#hiringSelect option:selected").attr("id");
        hiringCalendarId = hiringCalendarId.split(":");
        MODEL.HiringCalendarId = hiringCalendarId[0];
        MODEL.BEGINDATE = hiringCalendarId[1];
        MODEL.ENDDATE = hiringCalendarId[2];
        MODEL.HireEventId = hiringCalendarId[3];
        //service call for calendar summary details
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getCalendarSummary);
        var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
        RSASERVICES.calendarSummaryGet(callbackFunction, calendarSumaryUrl,
                calendarStartDate, MODEL.HiringCalendarId, calendarEndDate);
    };
    /*
     * Method for load the next 14 days from
     *  end date on clicking next button
     *  */
    this.selectNext = function(){
        var beginDate = "";
        var finishDate = "";
        var hiringNextDate = UTILITY.formatDate(CONSTANTS.FINISHEDDATE);
        var date = new Date(hiringNextDate);
        //Take the selectedEndDate and add 1 Day to get the new StartDate
        var startDate = new Date(date.getTime()
                + (1 * CONSTANTS.MILLISECONDS_PER_DAY));
        /* Take the new StartDate and add 14 Days to get the new EndDate */
        var endDate = new Date(startDate.getTime()
                + (14 * CONSTANTS.MILLISECONDS_PER_DAY));
        beginDate = UTILITY.formatedMonth(startDate);
        finishDate = UTILITY.formatedMonth(endDate);
        MODEL.SelectHiringStartDate = beginDate;
        MODEL.SelectHiringEndDate = finishDate;
        $(".calendarDue").html(
                UTILITY.getMonthFormatted(CONSTANTS.INITIALDATE) + " - "
                        + UTILITY.getMonthFormatted(CONSTANTS.FINISHEDDATE));
        //service call for getting details for next 14 days
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getCalendarSummary);
        var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
        RSASERVICES.calendarSummaryGet(callbackFunction, calendarSumaryUrl,
                beginDate, MODEL.HiringCalendarId, finishDate);

    };
    /*
     * Method returns previous 14 days from
     * initial date from response
     **/
    this.selectPrevious = function(){
        var calendarBeginDate = "";
        var calendarFinishDate = "";
        var firstDate = "";
        var responseFirstDate = "";
        var hiringInitialDate = UTILITY.formatDate(CONSTANTS.INITIALDATE);
        var previousDate = new Date(hiringInitialDate);
        firstDate = UTILITY.formatedMonth(previousDate);
        //setting dynamic value for calendar label on click of previous button
        $(".calendarDue").html(
                UTILITY.getMonthFormatted(CONSTANTS.INITIALDATE) + " - "
                        + UTILITY.getMonthFormatted(CONSTANTS.FINISHEDDATE));
        var compareNewDate = new Date();
        var leastPastDate = new Date(compareNewDate.getTime() - (15 * CONSTANTS.MILLISECONDS_PER_DAY));
        responseFirstDate = UTILITY.formatedMonth(leastPastDate);
        //if selected date and initial date from response is same then it should throw message
        if (firstDate <=  responseFirstDate) {
            self.setDraggableFocus();
            $("#hiringAlert .modal-content").addClass('popupWidth');
            $("#hiringAlert .modal-body .alertModalBody").html(
                '<div class="rangeMessage">' + CONSTANTS.CheckPreviousDateRange
                        + '</div>');
        } else {
            // Take the selectedStartDate and subtract 15 Days to get the new
            // StartDate
            var startDate = new Date(previousDate.getTime()
                    - (15 * CONSTANTS.MILLISECONDS_PER_DAY));
            // Take the new StartDate and add 14 Days to get the new EndDate
            var endDate = new Date(startDate.getTime()
                    + (14 * CONSTANTS.MILLISECONDS_PER_DAY));
            calendarBeginDate = UTILITY.formatedMonth(startDate);
            calendarFinishDate = UTILITY.formatedMonth(endDate);
            MODEL.SelectHiringStartDate = calendarBeginDate;
            MODEL.SelectHiringEndDate = calendarFinishDate;
            //service call for getting details for previous 14 days
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.getCalendarSummary);
            var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
            RSASERVICES.calendarSummaryGet(callbackFunction, calendarSumaryUrl,
                    calendarBeginDate, MODEL.HiringCalendarId, calendarFinishDate);
        }
    };
    /*
     * Method to show the dynamic calendar based on
     * dayOfWeekIndicator from response
     **/
    this.getCalendarSummary = function(response) {
        var calendarSummary = [];
        var calendarSummaryResponse = [];
        if(response.calendarResponse && response.calendarResponse.hasOwnProperty("error")){
             if (_.isArray(response.calendarResponse.error)) {
                 CONSTANTS.SERVICEERROR = response.calendarResponse.error;
             } else {
                 CONSTANTS.SERVICEERROR = [response.calendarResponse.error];
             }
             var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.SERVICEERROR);
             self.setDraggableFocus();
             $("#hiringAlert .modal-body").addClass("autoHeight");
             $("#hiringAlert .modal-body .alertModalBody").text(
                     returnedErrorMessage);
        }
        else if(response.calendarResponse && response.calendarResponse.hasOwnProperty("daySummaries")){
            calendarSummary = response.calendarResponse.daySummaries;
            //get the whole response object
            if (calendarSummary) {
                for ( var i = 0; i < calendarSummary.length; i++) {
                    calendarSummaryResponse = calendarSummary[i].daySummary;
                }
            }
            self.appendCalendar(calendarSummaryResponse);
        }
    };
    /*
     * Method to append the dynamic calendar with slots
     * Calendar has 3 weeks and 21 days.
     * It has scheduled,received and available slots
     *
     */
    this.appendCalendar = function(calendarSummaryResponse){
        var slotCountResponse = [];
        var calendarShowDate = "";
        var showDateId = "";
        var calendarTable = $(".hiringcalendarTable>tbody");
        var indexCount = 0;
         //Getting First dayOfWeekIndicator from response
        var startCount = calendarSummaryResponse[0].dayOfWeekIndicator;
        CONSTANTS.INITIALDATE = calendarSummaryResponse[0].date;
        CONSTANTS.FINISHEDDATE = calendarSummaryResponse[calendarSummaryResponse.length - 1].date;
        $(".calendarDue").html(UTILITY.getMonthFormatted(CONSTANTS.INITIALDATE) + " - "
                        + UTILITY.getMonthFormatted(CONSTANTS.FINISHEDDATE));
        //Initially the empty the table to write a fresh calendar
        $(calendarTable).empty();
        //Loop iterates for first 3 weeks
        for ( var m = 1; m <= 3; m++) {
            var row = $('<tr></tr>').appendTo(calendarTable);
            //Loop iterates for 7 days based on 3 weeks
            for ( var j = 1; j <= 7; j++) {
                indexCount = (m - 1) * 7 + j;
                //To check the first dayOfWeekIndicator is less than index and comparison of response length
                if (indexCount >= startCount && ((indexCount - startCount) < calendarSummaryResponse.length)) {
                        showDateId = calendarSummaryResponse[indexCount- startCount].date;
                        MODEL.HiringShowDate = showDateId;
                        calendarShowDate = calendarSummaryResponse[indexCount- startCount].date;
                        calendarShowDate = calendarShowDate.split("-");
                        var calendarTooltip = CONSTANTS.CALENDAR_TOOLTIP + ''
                                + UTILITY.stringFormattedDate(showDateId) + ''
                                + CONSTANTS.CALENDAR_NOTE_TOOLTIP;
                        calendarShowDate[2] = calendarShowDate[2]<10?calendarShowDate[2].replace("0",""):calendarShowDate[2];
                        //To check If dayOfWeekIndicator is not 7 and isDateInHiringEvent method return true or false
                            if (calendarSummaryResponse[indexCount - startCount].dayOfWeekIndicator !== 7
                            || calendarSummaryResponse[indexCount - startCount].dayOfWeekIndicator === 7) {
                                 self.appendCalendarSummary(calendarTooltip,calendarShowDate[2],row);
                            }
                         //To check the response having scheduled slots
                        if (calendarSummaryResponse[indexCount - startCount] && calendarSummaryResponse[indexCount
                                        - startCount].StatusSlotCountDtls) {
                            slotCountResponse = calendarSummaryResponse[indexCount - startCount].StatusSlotCountDtls;
                            //To get the scheduled and reserved slots from response
                            self.appendCalendarSlots(slotCountResponse);
                        }
                    }
                else {
                    $('<td></td>').appendTo(row).addClass('blockColumn');
                }
            self.getPopup();
            }
        }
    };
    /*
     * Method to validate the date is belong to hiring event.
     */
    this.appendCalendarSummary = function(tooltip,calendarShowDate,row){
        //validate the dates
         if (UTILITY.isDateInHiringEvent(MODEL.HiringShowDate,MODEL.BEGINDATE, MODEL.ENDDATE)) {
             $('<td id="hiringcalendar'+ MODEL.HiringShowDate
                     + '" class="HIREEVENTDATEBACKGROUNDCOLOR"></td>')
                     .text(calendarShowDate).appendTo(row);
             MODEL.IsBackGround=true;
             $("#hiringcalendar" + MODEL.HiringShowDate + "").attr("title", tooltip);
         } else {
             $('<td id="hiringcalendar' + MODEL.HiringShowDate+ '"></td>').text(
                     calendarShowDate).appendTo(row);
             $("#hiringcalendar" + MODEL.HiringShowDate + "").attr(
                 "title",CONSTANTS.HIRING_EVENT_DETAIL_DAY_TOOLTIP);
             MODEL.IsBackGround=false;
         }
    };
    /*
     * Method to append the slots for a day basis.
     * It has scheduled,received and available slots
     * for hiring event.
     *
     */
    this.appendCalendarSlots = function(slotResponse){
        var availableCountValue="";
        var reserveCountValue="";
        var sloCountValue="";
        //Append a list of blocks in calendar on getting param values
         for ( var slotCount = 0; slotCount < slotResponse.length; slotResponse++) {
             sloCountValue = slotResponse[slotCount].StatusSlotCountDtl.availabeCount;
             availableCountValue = slotResponse[slotCount].StatusSlotCountDtl.bookedCount;
             reserveCountValue = slotResponse[slotCount].StatusSlotCountDtl.reservedCount;
             //Method for passing values to write a list
             $("#hiringcalendar" + MODEL.HiringShowDate + "").append(
                $('<ul id="hiringcalendar' + MODEL.HiringShowDate + '" >'+
                '<li style="background:#C6F3D5;" id="hiringcalendar'
                + MODEL.HiringShowDate
                + '" class="slotSummaryList">'
                + sloCountValue
                + ' Slots(s) Avail</li>'
                + '<li style="background:#F5F4A3;" id="hiringcalendar'
                + MODEL.HiringShowDate
                + '" class="slotSummaryList">'
                + availableCountValue
                + ' Slots(s) Sched</li>'
                + '<li style="background:#E8E3E3;" id="hiringcalendar'
                + MODEL.HiringShowDate
                + '" class="slotSummaryList">'
                + reserveCountValue
                + ' Slots(s) Rsvd</li></ul>'
                + '<input type="hidden" id="hiddenCount'
                + MODEL.HiringShowDate
                + '" value='
                + sloCountValue
                + '>'));
         }
    };
    /*
     * Method : A popup show on clicking a particular
     *  day from calendar
     *  */
    this.getPopup = function(){
        $("#hiringcalendar" + MODEL.HiringShowDate + "").on('click',function(event) {
            //to get an id for a particular column
            var selectedDate = event.target.id;
            //to get an classname for a particular column
            selectedDate = selectedDate.replace("hiringcalendar","");
            //to show the day view modal popup
            self.blockPopup('#hiringDayViewModal');
            $("#hiringDayViewModal").draggable({
                handle: ".modal-header"
            });
            $("#hiringDayViewModal").modal('show');
            //to load the popup and param values for loading details in popup
            self.loadPopUp(selectedDate);
        });
    };
    /*
     * Method for loading popup with default
     *  view block
     *  */
    this.loadPopUp = function(selectedDate) {
        MODEL.HiringSelectedDate = selectedDate;
        MODEL.AddPrintPacket = false;
        $(".hiringViewContainer").show();
        //View is the default view in popup
        $('.model-radio input:radio[value="hiringView"]').prop('checked', true);
        this.setHiringButtons(selectedDate);
        $("#printView").attr('disabled', true);
        $(".hiringAddContainer").hide();
        $(".hiringDeleteContainer").hide();
        $(".hiringPrintPacketContainer").hide();
        $("#hiringAlert .modal-body").removeClass('popupHeight');
        $("#hiringAlert .modal-content").removeClass('popupWidth');
        $("#hiringAlertLabel").empty();
        $(".tableContainer").scrollTop(0);
        $(".tableContainer").bind("scroll",function(){
            $(".selectboxit-options.selectboxit-list").css("display", "none");
        });
        $("#hiringDateSlotDetail").html("Date: "+
                UTILITY.getMonthFormatted(MODEL.HiringSelectedDate)
                        + " / Hiring Event: "
                        + $("#hiringSelect option:selected").text());
        //service call for getting calendar details
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getHiringCalendarDetail);
        var calendarDetailUrl = CONSTANTS.CalendarDetailService;
        RSASERVICES.calendarDetailsGet(callbackFunction, calendarDetailUrl,
                MODEL.HiringCalendarId, MODEL.HiringSelectedDate);

    };
    /*
     * Method to return and get the whole response
     * for hiring event detail service
     */
    this.getHiringCalendarDetail = function(detailResponse){
        if(detailResponse.calendarResponse){
            //check the response has requisitionSchedules
            if(detailResponse.calendarResponse && detailResponse.calendarResponse.hasOwnProperty("requisitionSchedules")){
                MODEL.RosterDetail = detailResponse.calendarResponse;
             //check the response has error
            }else if(detailResponse.calendarResponse.hasOwnProperty("error")){
                self.calendarErrorResponse(detailResponse.calendarResponse);
            }
          //check the response has requisitionScheduleSlots
            if(detailResponse.calendarResponse && detailResponse.calendarResponse.hasOwnProperty("requisitionScheduleSlots")){
                MODEL.HiringCalendarDetailList = detailResponse.calendarResponse.requisitionScheduleSlots[0];
                self.viewTimeBlock();
                $(".hiringViewContainer").show();
                //View is the default view in popup
                $('.model-radio input:radio[value="hiringView"]').prop('checked', true);
                $("#printView").attr('disabled', true);
                $(".hiringAddContainer").hide();
                $(".hiringDeleteContainer").hide();
                $(".hiringPrintPacketContainer").hide();
                $(".tableContainer").scrollTop(0);
                self.addTimeBlock();
                self.deleteTimeBlock();
                self.loadPrintPacket();
            }
          //check the response has error
            else if(detailResponse.calendarResponse.hasOwnProperty("error")){
                self.calendarErrorResponse(detailResponse.calendarResponse);
            }
       }
    };
    /*
     * Method is used to show the error message
     * on hiring calendar service fails
     */
    this.calendarErrorResponse = function(errorResponse){
         CONSTANTS.SERVICEERROR = errorResponse.error.errorMsg;       
         self.setDraggableFocus();
         $("#hiringAlert .modal-body").addClass("autoHeight");
         $("#hiringAlert .modal-body .alertModalBody").text(
                 CONSTANTS.SERVICEERROR);
    };
    /*
     * Method to validate the hiring dayview popup
     * radio buttons are add and delte should disabled ot
     * not based on condition
     */
    this.setHiringButtons = function(dateSelected){
        //Get the current date
        var today = new Date();
        //format the current date
        var currentDate = UTILITY.formatedMonth(today);
        /*compare the selected date and current is not equal
         * compare the BEGINDATE and selected date is less than
         * compare the ENDDATE and selected date is greater than.
         * If Below condition satisfies then add and delete
         * radio button will be disabled
         */
        if (dateSelected <= currentDate ||
                  MODEL.HiringSelectedDate < MODEL.BEGINDATE ||
                  MODEL.HiringSelectedDate > MODEL.ENDDATE){
             $('.model-radio input:radio[value="hiringAdd"]').prop('disabled',
                     true);
             $('.model-radio input:radio[value="hiringDelete"]').prop(
                     'disabled', true);
             $("#submitHiringView").attr('disabled', true).removeClass("submit")
                     .addClass('greyPrintBtn');
        }
        /*
         * if above condition met fails then both buttons will be enabled
         */
        else{
            $('.model-radio input:radio[value="hiringDelete"]').prop(
                     'disabled', false);
             $('.model-radio input:radio[value="hiringAdd"]').prop('disabled',
                     false);
             $("#submitHiringView").attr('disabled', false)
                     .removeClass("greyPrintBtn").addClass('submit');
        }
        self.setToolTips();
    };
    /*
     * Method: To Selects the previous day's data.
     * */
    this.selectDayPrevious = function() {
        var selectedDate = "";
        var checkDate = "";
        var responseFirstDate = "";
        var prevDate = MODEL.HiringSelectedDate.split("-");
        var splittedDate =prevDate[1]+"/"+prevDate[2]+"/"+prevDate[0];
        var date = new Date(splittedDate);
        checkDate = UTILITY.formatedMonth(date);
        var compareNewDate = new Date();
        var leastPastDate = new Date(compareNewDate.getTime() - (15 * CONSTANTS.MILLISECONDS_PER_DAY));
        responseFirstDate = UTILITY.formatedMonth(leastPastDate);
        if (checkDate <= responseFirstDate) {
            self.setDraggableFocus();
            $("#hiringAlert .modal-content").addClass('popupWidth');
            $("#hiringAlert .modal-body .alertModalBody").html(
                    '<div class="rangeMessage">' + CONSTANTS.CheckHiringDateRange
                            + '</div>');
        } else {
            //Take the selectedDate and subtract 1 Day to get the Previous Day
            var selectedPreviousDate = new Date(splittedDate);
            var previousDate = new Date(selectedPreviousDate.getTime()
                    - (1 * CONSTANTS.MILLISECONDS_PER_DAY));

            selectedDate = UTILITY.formatedMonth(previousDate);
            MODEL.HiringSelectedDate = selectedDate;
            $("#hiringDateSlotDetail").html("Date: "+
                    UTILITY.getMonthFormatted(selectedDate)
                            + " / Hiring Event: "
                            + $("#hiringSelect option:selected").text());
            var callbackFunction = $.Callbacks('once');
            //based on the selection, the calendar details will be loaded
            callbackFunction.add(self.getHiringCalendarDetail);
            self.setHiringButtons(selectedDate);
            //service call for getting calendar details for a calendar id
            var calendarDetailUrl = CONSTANTS.CalendarDetailService;
            RSASERVICES.calendarDetailsGet(callbackFunction, calendarDetailUrl,
                    MODEL.HiringCalendarId, selectedDate);
        }
    };
    /*
     * Method: To Selects the Next day's data
     * */
    this.selectDayNext = function() {
        var date = MODEL.HiringSelectedDate.split("-");
        var splittedDate =date[1]+"/"+date[2]+"/"+date[0];
        var selectedDate = new Date(splittedDate);
        //Take the selectedDate and add 1 Day to get the Next Day
        var nextDate = new Date(selectedDate.getTime()
                + (1 * CONSTANTS.MILLISECONDS_PER_DAY));

        var selectedNextDate = UTILITY.formatedMonth(nextDate);
        MODEL.HiringSelectedDate = selectedNextDate;
        $("#hiringDateSlotDetail").html("Date: "+
                UTILITY.getMonthFormatted(selectedNextDate)
                + " / Hiring Event: "
                + $("#hiringSelect option:selected").text());
        //based on the selection, the calendar details will be loaded
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(self.getHiringCalendarDetail);
        self.setHiringButtons(selectedNextDate);
        //service call for getting calendar details for a calendar id
        var calendarDetailUrl = CONSTANTS.CalendarDetailService;
        RSASERVICES.calendarDetailsGet(callbackFunction, calendarDetailUrl,
                MODEL.HiringCalendarId, selectedNextDate);
    };
    /*
     * Method:View Mode Selected or default
     * selection on loading popup
     * */
    this.viewTimeBlock = function(){
        var splitTime = "";
        var viewTimeResponse = [];
        var schedule = [];
        var requisitionSchedulesSlot = [];
        var row = "";
        var ampm = "";
        var convertedTime = "";
        $('#hiringViewBody').empty();
        if(MODEL.HiringCalendarDetailList){
            viewTimeResponse = MODEL.HiringCalendarDetailList;
            //Assigning response to model variable
            for ( var slotCount = 0; slotCount < viewTimeResponse.requisitionScheduleSlotDtl.length; slotCount++) {
                schedule = viewTimeResponse.requisitionScheduleSlotDtl[slotCount];
                splitTime = schedule.slotTime.split(":");
                ampm = splitTime[0] < 12 ? "am" : "pm";
                convertedTime = splitTime[0] > 12 ? Number(splitTime[0]) - 12
                        : (splitTime[0] !== "10" ? splitTime[0].replace("0", "")
                                : splitTime[0]);
                CONSTANTS.SHOWTIME = convertedTime + ":" + splitTime[1];
                row = '<tr>';
                //To check whether slot time having schedules
                if (schedule && schedule.requisitionSchedules) {
                    requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                    MODEL.RequisitionSchedulesCount = requisitionSchedulesSlot;
                    //To check schedules length more than 1
                    if(requisitionSchedulesSlot.length > 1){
                          row = self.multipleScheduleSlot(requisitionSchedulesSlot,convertedTime,splitTime[1],ampm,schedule,row);
                    }else{
                          row = self.singleScheduleSlot(requisitionSchedulesSlot,convertedTime,splitTime[1],ampm,schedule,row);
                    }
                   row += '</tr>';
                }
                //Block will have multiple row with unique time.
                else {
                    MODEL.RequisitionSchedulesCount = "";
                    row += splitTime[1]==="00"?
                        '<td class="col-xs-5 bold-span">' + convertedTime
                        + ':' + splitTime[1] + ' ' + ampm
                        + '</td>':'<td class="col-xs-5">' + convertedTime
                         + ':' + splitTime[1] + ' ' + ampm
                         + '</td>';
                    row += '<td class="col-xs-6"></td>';
                    row += '<td class="col-xs-1"></td>';
                    row += '</tr>';
                }
                $('#hiringViewBody').append(row);
             }
        }
        if(MODEL.RosterDetail && MODEL.RosterDetail.hasOwnProperty("requisitionSchedules")){
            if(MODEL.RosterDetail && MODEL.RosterDetail.requisitionSchedules[0]!==null
                    &&  MODEL.RosterDetail.requisitionSchedules[0]!==""){
                $("#exportRosterLabel").removeAttr("title");
                $("#printRosterLabel").removeAttr("title");
                $("#printRoster").attr('disabled', false).removeClass(
                'disableRoaster').addClass('roaster');
                   $("#exportRoster").attr('disabled', false).removeClass(
                   'disableRoaster').addClass('roaster');
            }else{
                $("#printRoster").attr('disabled', true).removeClass('roaster')
                        .addClass('disableRoaster');
                $("#exportRoster").attr('disabled', true).removeClass('roaster')
                        .addClass('disableRoaster');
                $("#exportRosterLabel").attr("title",CONSTANTS.ROSTER_DISABLE_TOOLTIP);
                    $("#printRosterLabel").attr("title",CONSTANTS.ROSTER_DISABLE_TOOLTIP);
            }
            self.setToolTips();
        }
        $("#hiringViewBody tr>td:nth-child(1),#hiringViewBody tr>td:nth-child(2)").addClass("selectable SelectIE");
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method to show the multiple slot for view block
     * it has scheduled slots for a particular time.
     * The slots will have one or more than also.
     */
    this.multipleScheduleSlot = function(requisitionSchedulesSlot,convertTime,splitTime,timeStamp,scheduleTime,row){
        for ( var i = 0; i < requisitionSchedulesSlot.length; i++) {
            var dateTime = scheduleTime.requisitionSchedules[0].requisitionSchedule[i].beginTimestamp
                   .substring(11, 16);
            //to check the response time and slot time is equal
           if (dateTime === scheduleTime.slotTime) {
           //Block will have single row with unique time and background
               if (i === 0){
                   row += splitTime ==="00"?'<td class="col-xs-5 bold-span">' + convertTime
                           + ':' + splitTime + ' ' + timeStamp
                           + '</td>':'<td class="col-xs-5">' + convertTime
                           + ':' + splitTime + ' ' + timeStamp
                           + '</td>';
                   row=self.showHiringSlots(requisitionSchedulesSlot[i], row);
                }
               //Block will have multiple row with background
               else {
                   row += '</tr>';
                   row += '<tr><td class="col-xs-5"></td>';
                   row=self.showHiringSlots(requisitionSchedulesSlot[i], row);
               }
           }
       }
        return row;
   };
   /*
    * Method to show the single slot with time for view block.
    * it has scheduled,received and availabel as well.
    * it has single slot for a particular time
    *
    */
   this.singleScheduleSlot = function(requisitionSchedulesSlot,convertTime,singleSplitTime,stamp,scheduleTime,row){
       var singleDateTime="";
        singleDateTime = requisitionSchedulesSlot.beginTimestamp.substring(11, 16);
     //check the time is equal
            if (singleDateTime === scheduleTime.slotTime){
                row+= singleSplitTime==="00"?'<td class="col-xs-5 bold-span">' + convertTime
                    + ':' + singleSplitTime + ' ' + stamp
                    + '</td>':
                     '<td class="col-xs-5">' + convertTime
                     + ':' + singleSplitTime+ ' ' + stamp
                     + '</td>';
               row=self.showHiringSlots(requisitionSchedulesSlot, row);
            }
            row += '</tr>';
            return row;
      };
      /*
       * Method to show the slot based on
       * requisitionScheduleStatusCode
       */
      this.showHiringSlots = function(scheduleSlot,scheduleRow){
          if(scheduleSlot && scheduleSlot.requisitionScheduleStatusCode){
              if(scheduleSlot.requisitionScheduleStatusCode===3){
                  if(scheduleSlot.candidateName!==undefined){
                      scheduleRow += '<td class="col-xs-6 candidateBackground">'+scheduleSlot.candidateName+'</td>';
                  }else{
                      scheduleRow += '<td class="col-xs-6 candidateBackground">Scheduled</td>';
                  }
                  scheduleRow += '<td class="col-xs-1">'+scheduleSlot.requisitionLocation+'</td>';
              }
              else if(scheduleSlot.requisitionScheduleStatusCode===2){
                  scheduleRow += '<td class="col-xs-6 reserveBackground">Reserved</td>';
                  scheduleRow += '<td class="col-xs-1"></td>';
              }
              else if(scheduleSlot.requisitionScheduleStatusCode===1){
                  scheduleRow += '<td class="col-xs-6 slotBackground">Available</td>';
                  scheduleRow += '<td class="col-xs-1"></td>';
              }
              else if(scheduleSlot.requisitionScheduleStatusCode===0){
                  scheduleRow += '<td class="col-xs-6"></td>';
                  scheduleRow += '<td class="col-xs-1"></td>';
              }
          }
          return scheduleRow;
      };
      /*
       * Method is used to set the tooltips for radio buttons
       * in hiring day view popup
       */
    this.setToolTips = function(){
        if($('.model-radio input:radio[value="hiringAdd"]').prop('disabled') && $('.model-radio input:radio[value="hiringDelete"]').prop(
        'disabled')){
            $('.model-radio input:radio[value="hiringDelete"]').attr("title",
                    CONSTANTS.DEL_AVAILABILITY_HIRING_TOOLTIP);
            $('.model-radio input:radio[value="hiringAdd"]').attr("title",
                    CONSTANTS.ADD_AVAILABILITY_HIRING_TOOLTIP);
            $('#addHiringRadioBtn').attr("title",
                    CONSTANTS.ADD_AVAILABILITY_HIRING_TOOLTIP);
            $('#deleteHiringRadioBtn').attr("title",
                    CONSTANTS.DEL_AVAILABILITY_HIRING_TOOLTIP);
        }
        else {
            $('.model-radio input:radio[value="hiringDelete"]').removeAttr("title");
            $('.model-radio input:radio[value="hiringAdd"]').removeAttr("title");
            $('#addHiringRadioBtn').removeAttr("title");
            $('#deleteHiringRadioBtn').removeAttr("title");
        }
        /*
         * If print and export roster button is disabled then set the
         * tooltip
         */
        if($("#printRoster").attr('disabled') && $("#exportRoster").attr('disabled')){
            $("#printRoster").attr("title",CONSTANTS.ROSTER_DISABLE_TOOLTIP);
            $("#exportRoster").attr("title",CONSTANTS.ROSTER_DISABLE_TOOLTIP);
        }
        else {
            $("#printRoster").removeAttr("title");
            $("#exportRoster").removeAttr("title");
        }
    };
    /*
     * Method: To view the slot time with details
     * for add action.
     * it has scheduled,received and available slots
     * It will add the blocks as required.
     */
    this.addTimeBlock = function() {
        var addSplitTime = "";
        var timeResponse = [];
        var row = "";
        var convertedTime = "";
        var schedule=[];
        var requisitionSchedulesSlot=[];
        $('#hiringAddSlotBody').empty();
        if(MODEL.HiringCalendarDetailList){
            timeResponse = MODEL.HiringCalendarDetailList;
            for ( var slotCount = 0; slotCount < timeResponse.requisitionScheduleSlotDtl.length; slotCount++) {
                schedule = timeResponse.requisitionScheduleSlotDtl[slotCount];
                addSplitTime = schedule.slotTime.split(":");
                var BeforeConvertedTime = schedule.slotTime.split(":");
                var hiddenTime = BeforeConvertedTime[0] + BeforeConvertedTime[1];
                var ampm = addSplitTime[0] < 12 ? "am" : "pm";
                convertedTime = addSplitTime[0] > 12 ? Number(addSplitTime[0]) - 12
                : (addSplitTime[0] !== "10" ? addSplitTime[0].replace("0","") : addSplitTime[0]);
                CONSTANTS.SHOWTIME = convertedTime + addSplitTime[1];
                row = '<tr>';
                //To check whether slot time having schedules
                if (schedule && schedule.requisitionSchedules) {
                    requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                    if(requisitionSchedulesSlot.length > 1){
                        row = self.addMultipleSlot(requisitionSchedulesSlot, schedule,
                                convertedTime, hiddenTime, row, ampm,
                                addSplitTime[1]);
                    }else{
                        row = self.addSingleSlot(
                                 requisitionSchedulesSlot, schedule, convertedTime,
                                 hiddenTime, row, ampm, addSplitTime[1]);
                    }
                    row += '</tr>';
                }
                //Block will have time with no scheduled slot with interview dropdown
                else {
                     row+= addSplitTime[1]==="00"? '<td class="col-xs-3 bold-span">' + convertedTime
                             + ':' + addSplitTime[1] + ' ' + ampm:'<td class="col-xs-3">' + convertedTime
                             + ':' + addSplitTime[1] + ' ' + ampm;
                    row += '<td class="col-xs-7"></td>';
                    row += '<td class="col-xs-1"><input type="checkbox" id='+ hiddenTime
                            + ' name="hiringAddCheckSlot" value="addSlot'
                            + CONSTANTS.SHOWTIME + '"><input type="hidden" value='
                            + schedule.slotTime + ' id="hiddenTime' + hiddenTime+ '"></label></td>';
                    row += '<td class="col-xs-1"><select class="form-control small-Select addInterviewSelect" data-size="5" id="hiringConcurrentInterview'
                            + hiddenTime + '">';
                        for (var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
                            row += '<option value="interview'
                                    + MODEL.ConcurrentInterviewCollection[interview].value
                                    + '">'
                                    + MODEL.ConcurrentInterviewCollection[interview].name
                                    + '</option>';
                        }
                    row += '</select></td>';
                    row += '</tr>';
                }
                $('#hiringAddSlotBody').append(row);
                //set tooltip
                $("#hiringConcurrentInterview" + hiddenTime + "").attr("title",CONSTANTS.CONCURRENT_INTERVIEWS_TOOLTIP);
            }
            $(".addInterviewSelect").selectBoxIt();
            $(".addInterviewSelect").bind({
                "open": function(ev) {
                    //check chrome browser and set the dynamic height for chrome
                    if(typeof chrome !== "undefined"){
                        $(ev.currentTarget).siblings().find(".selectboxit-options.selectboxit-list").css("position","fixed");
                        $(ev.currentTarget).siblings().find(
                ".selectboxit-options.selectboxit-list")
                .css(
                        "top",
                        $(ev.currentTarget).siblings()
                                .position().top
                                + $(ev.currentTarget)
                                        .siblings()
                                        .height()+55);
                    }
                    //Microsoft internet explorer browser based
                    else{
                        $(ev.currentTarget).siblings().find(".selectboxit-options.selectboxit-list").css("position","fixed");
                        $(ev.currentTarget).siblings().find(
                ".selectboxit-options.selectboxit-list")
                .css(
                        "top",
                        $(ev.currentTarget).siblings()
                                .position().top
                                + $(ev.currentTarget)
                                        .siblings()
                                        .height()+115);
                    }
                }
              });
            if ($(".addInterviewSelect").data("selectBox-selectBoxIt")) {
                $(".addInterviewSelect").data("selectBox-selectBoxIt").refresh();
            }
            self.addSelectAll();
        }
        $("#hiringAddSlotBody tr>td:nth-child(1),#hiringAddSlotBody tr>td:nth-child(2)").addClass("selectable SelectIE");
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method to show the multiple slot for add block
     * it has scheduled slots for a particular time.
     * The slots will have one or more than also.
     */
     this.addMultipleSlot = function(requisitionSchedulesSlot,schedule,addConvertedTime,timeHidden,row,timeStamp,addTime) {
            var addDateEqual="";
            for ( var i = 0; i < requisitionSchedulesSlot.length; i++) {
                addDateEqual = schedule.requisitionSchedules[0].requisitionSchedule[i].beginTimestamp
                    .substring(11, 16);
            // To check whether slot time is equal to response time
            if (addDateEqual === schedule.slotTime) {
                // Block will have time with scheduled slot with interview
                // dropdown
                if (i === 0) {
                    row+= addTime === "00"?'<td class="col-xs-3 bold-span">'
                                + addConvertedTime + ':' + addTime + ' '
                                + timeStamp:'<td class="col-xs-3">' + addConvertedTime + ':'
                                + addTime + ' ' + timeStamp;
                    row = self.showAddSlots(requisitionSchedulesSlot[i], row);
                    row += '<td class="col-xs-1"><input type="checkbox" name="hiringAddCheckSlot" id='
                            + timeHidden
                            + ' value="addSlot'
                            + CONSTANTS.SHOWTIME
                            + '"><input type="hidden" value='
                            + schedule.slotTime
                            + ' id="hiddenTime'
                            + timeHidden + '"></label></td>';
                    row += '<td class="col-xs-1"><select class="form-control small-Select addInterviewSelect" data-size="5" id="hiringConcurrentInterview'
                            + timeHidden + '">';
                    for (var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
                        row += '<option value="interview'
                                + MODEL.ConcurrentInterviewCollection[interview].value
                                + '">'
                                + MODEL.ConcurrentInterviewCollection[interview].name
                                + '</option>';
                    }
                    row += '</select></td>';
                }
                // Block will have scheduled slot for a time
                else {
                    row += '</tr>';
                    row += '<tr><td class="col-xs-3"></td>';
                    row = self.showAddSlots(requisitionSchedulesSlot[i], row);
                    row += '<td class="col-xs-1"></td>';
                    row += '<td class="col-xs-1"></td>';
                }
            }
        }
        return row;
    };
    /*
     * Method to show single slots with time for
     * add option.
     * it has scheduled,available and recieved slots.
     * To add and view scheduled slots
     */
    this.addSingleSlot =  function(requisitionSchedulesSlot,schedule,addSingleConvertedTime,timeHidden,row,timeStamp,addTime){
        var slotDate="";
        slotDate = requisitionSchedulesSlot.beginTimestamp.substring(11, 16);
        //comapare the slot time
        if (slotDate === schedule.slotTime) {
             if(addTime==="00"){
                 row += '<td class="col-xs-3 bold-span">' + addSingleConvertedTime
                 + ':' + addTime + ' ' + timeStamp;
            }else{
                 row += '<td class="col-xs-3">' + addSingleConvertedTime
                 + ':' + addTime + ' ' + timeStamp;
            }
            row = self.showAddSlots(requisitionSchedulesSlot, row);
            row += '<td class="col-xs-1"><input type="checkbox" name="hiringAddCheckSlot" id='
                    + timeHidden+ ' value="addSlot'+ CONSTANTS.SHOWTIME
                    + '"><input type="hidden" value='+ schedule.slotTime+ ' id="hiddenTime'
                    + timeHidden + '"></label></td>';
            row += '<td class="col-xs-1"><select class="form-control small-Select addInterviewSelect" data-size="5" id="hiringConcurrentInterview'
                    + timeHidden + '">';
            for (var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
                row += '<option value="interview'
                        + MODEL.ConcurrentInterviewCollection[interview].value
                        + '">'
                        + MODEL.ConcurrentInterviewCollection[interview].name
                        + '</option>';
            }
            row += '</select></td>';
        }
        return row;
    };
    /*
     * Method to show the slot based on
     * requisitionScheduleStatusCode
     */
    this.showAddSlots = function(scheduleSlot,scheduleRow){
        if(scheduleSlot && scheduleSlot.requisitionScheduleStatusCode){
            //based on requisitionScheduleStatusCode is 3 then show the candidate name
            if(scheduleSlot.requisitionScheduleStatusCode===3){
                if(scheduleSlot.candidateName!==undefined){
                    scheduleRow += '<td class="col-xs-7 candidateBackground">'+scheduleSlot.candidateName+'</td>';
                }else{
                    scheduleRow += '<td class="col-xs-7 candidateBackground">Scheduled</td>';
                }
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===2){
                scheduleRow += '<td class="col-xs-7 reserveBackground">Reserved</td>';
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===1){
                scheduleRow += '<td class="col-xs-7 slotBackground">Available</td>';
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===0){
                scheduleRow += '<td class="col-xs-7"></td>';
            }
        }
        return scheduleRow;
    };
    /*
     * Method to Selects/Unselects all check boxes
     **/
    this.addSelectAll = function(){
        $('input:checkbox[name=hiringAddAllCheck]').click(function() {
            if ($("input:checkbox[name=hiringAddAllCheck]").prop(
                    'checked')) {
                //Select All Checked
                $("input:checkbox[name=hiringAddCheckSlot]").prop(
                        'checked', true);
            }
            //Select All unChecked
            else {
                $("input:checkbox[name=hiringAddCheckSlot]").prop(
                        'checked', false);
                    }
        });
        $("input:checkbox[name=hiringAddCheckSlot]").click(function(){
            $("input:checkbox[name=hiringAddAllCheck]").prop('checked',false);
        });
    };
    /*
     * Method: Load time,interview details in
     * add block of time popup
     **/
    this.loadBlockTime = function() {
        MODEL.AddTimeBlock = true;
        self.blockPopup('#hiringAddBlocksTime');
        $("#hiringAddBlocksTime").draggable({
            handle: ".modal-header"
        });
        $("#hiringAddBlocksTime").modal('show');
        var beginTimeSelect = $("#hiringBlockBeginTime");
        var endTimeSelect = $("#hiringBlockEndTime");
        var interviewBlock = $("#hiringBlockInterview");
        $("#hiringAddTimeDetail").empty();
        $("#hiringAddTimeDetail").html(" "+
                $("#hiringSelect option:selected").text() + " for "
                        + UTILITY.getMonthFormatted(MODEL.HiringSelectedDate));
        var beginTimeOption = "";
        var endTimeOption = "";
        var interviewOption = "";
        $("#interviewHiringLabel").attr("title",CONSTANTS.CONCURRENT_INTERVIEWS_TOOLTIP);
        $(endTimeSelect).empty();
        $(interviewBlock).empty();
        $(beginTimeSelect).empty();
        //To get the collection of time and push in dropdown
        for ( var time = 0; time < MODEL.TimeCollection.length; time++) {
            beginTimeOption = '<option value="'
                    + MODEL.TimeCollection[time].value + '">'
                    + MODEL.TimeCollection[time].name + '</option>';
            $(beginTimeSelect).append(beginTimeOption);
            endTimeOption = '<option value="'
                    + MODEL.TimeCollection[time].value + '">'
                    + MODEL.TimeCollection[time].name + '</option>';
            $(endTimeSelect).append(endTimeOption);

        }
        self.selectBox("hiringBlockEndTime");
        self.selectBox("hiringBlockBeginTime");
        //To get the count of numbers as interview and push in dropdown
        for ( var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
            interviewOption = '<option value="'
                    + MODEL.ConcurrentInterviewCollection[interview].value
                    + '">'
                    + MODEL.ConcurrentInterviewCollection[interview].name
                    + '</option>';
            $(interviewBlock).append(interviewOption);
        }
        self.selectBox("hiringBlockInterview");
     };
    /*
     * Method for jquery plugin dropdown.
     * Default dropdown "Id" is a parameter
     * to pass and append
     */
    this.selectBox = function(selectId){
         $("#"+selectId).data("selectBox-selectBoxIt").refresh();
         if ($("#"+selectId).data("selectBox-selectBoxIt")) {
             $("#"+selectId).data("selectBox-selectBoxIt").refresh();
         }
    };
    /*
     * Method: to show the warning message to confirm the
     *  selected slot for add time block
     **/
    this.submitAddConfirmBlock = function() {
        var beginBlockTime = "";
        var endBlockTime = "";
        var numOfInterviews = "";
        var beginTimeIndex = 0;
        var endTimeIndex = 0;
        var addedSlotsPerDay = 0;
        var addedSlots = 0;
        var addBlockMessage = "";
        beginBlockTime = $("#hiringBlockBeginTime option:selected").val();
        endBlockTime = $("#hiringBlockEndTime option:selected").val();
        numOfInterviews = $("#hiringBlockInterview option:selected").text();
        beginTimeIndex = $("#hiringBlockBeginTime option:selected").index();
        endTimeIndex = $("#hiringBlockEndTime option:selected").index();
        $("#hiringAlert .modal-content").removeClass("slotExceedWidth");
        $("#hiringAlert .modal-body").removeClass("slotExceedBodyHeight");
        $("#hiringAlert .modal-body .alertModalBody").removeClass("slotExceedModalBody");
        //get the slots per day
        $("#warningHiringPopup .modal-body #hiringActionMessage").empty();
        addedSlotsPerDay = Number(endTimeIndex - beginTimeIndex)
                * Number(numOfInterviews);
        //get the slots
        addedSlots = Number(endTimeIndex - beginTimeIndex)
                * Number(numOfInterviews) * 1;
        //Make sure that no more than 680 Slots have been selected
        if (beginBlockTime === endBlockTime) {
            self.setDraggableFocus();
            $("#hiringAlert .modal-body .alertModalBody").html(
                    CONSTANTS.CheckEqualTime);
        } else if (endBlockTime < beginBlockTime) {
            $('#hiringAlert').off("shown.bs.modal");
            $('#hiringAlert').on("shown.bs.modal", function() {
                $("#hiringAlertOk").focus();
            });
            self.blockPopup('#hiringAlert');
            $("#hiringAlert .modal-body .alertModalBody").html(
                    CONSTANTS.CheckTimeRange);
        } else {
            if (addedSlots > 680) {
                self.setDraggableFocus();
                $("#showSlotPopup").modal('hide');
                $('#hiringAlert #alertModalLabel').text("Add Slot Number Exceeded");
                $("#hiringAlert .modal-content").addClass("slotExceedWidth");
                $("#hiringAlert .modal-body").addClass("slotExceedBodyHeight");
                $("#hiringAlert .modal-body .alertModalBody").addClass("slotExceedModalBody");
                $("#hiringAlert .modal-body .alertModalBody").html(CONSTANTS.SlotExceed);
            }
            //check the slots been equal
            else if (addedSlotsPerDay === addedSlots){
                addBlockMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add " + addedSlots
                        + " thirty minute interview slot(S) for "
                        + UTILITY.stringFormattedDate(MODEL.HiringSelectedDate) + ".</div><div class='cols-xs-12 cols-xs-offset-1'>Are You Sure?</div>";
                self.blockPopup('#warningHiringPopup');
                $("#warningHiringPopup").draggable({
                    handle: ".modal-header"
                });
                $("#warningHiringPopup").modal('show');
                $("#warningHiringPopup .modal-body #hiringActionMessage")
                        .html(addBlockMessage);
                $("#warningHiringPopup .modal-body").removeClass('deleteActionMessage').addClass('actionResponseMessage');
                $("#warningHiringPopup .modal-content").removeClass('deletePopupWidth');
            } else {
                addBlockMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add "
                        + addedSlotsPerDay
                        + " thirty minute interview slot(s) for "
                        + UTILITY.stringFormattedDate(MODEL.HiringSelectedDate)
                        + "</div><div class='cols-xs-12 cols-xs-offset-1'> a total of "
                        + addedSlots
                        + " slot(s) will be added based on recurring weeks selection(s).</div><div class='cols-xs-12 cols-xs-offset-1'>Are You Sure?</div>";
                $("#warningHiringPopup .modal-body #hiringActionMessage")
                        .html(addBlockMessage);
                self.blockPopup('#warningHiringPopup');
                $("#warningHiringPopup").draggable({
                    handle: ".modal-header"
                });
                $("#warningHiringPopup").modal('show');
                $("#warningHiringPopup .modal-body").removeClass('deleteActionMessage').addClass('actionResponseMessage');
                $("#warningHiringPopup .modal-content").removeClass('deletePopupWidth');
            }
        }
    };
    /*
     * Method: to validate and show the warning message
     * to confirm the selected slot for add,delete and print
     **/
    this.confirmSlot = function() {
        //get the action value
        var actionValue = $('input:radio[name=hiringDayViewOption]:checked').val();
        //check the length of checked slots
        var checkSlotArray = $('input:checkbox[name=hiringAddCheckSlot]:checked').length;
        var deleteSlotArray = $('input:checkbox[name=hiringDeleteSlot]:checked').length;
        var hiringPrintPacketArray = $('input:checkbox[name=hiringPrintSlot]:checked').length;
        var responseMessage ="";
        $("#warningHiringPopup .modal-body #hiringActionMessage").empty();
        //validate the slots and show the warning message
        if (actionValue === "hiringAdd" && checkSlotArray === 0) {
            self.setDraggableFocus();
            $("#hiringAlert .modal-body .alertModalBody").text(
                    CONSTANTS.CheckAddAvailability);
        } else if (actionValue === "hiringDelete" && deleteSlotArray === 0) {
            self.setDraggableFocus();
            $("#hiringAlert .modal-body .alertModalBody").text(
                    CONSTANTS.CheckDeleteAvailability);
        }
        else if (actionValue === "hiringPrintPackets" && hiringPrintPacketArray ===0) {
            self.setDraggableFocus();
            $("#hiringAlert .modal-body .alertModalBody").html(
                    CONSTANTS.CheckPrintPacketAvailability);
        }
        else if (actionValue === "hiringView") {
            if($("#submitHiringView").text()==="Submit") {
                self.setDraggableFocus();
                $("#hiringAlert .modal-body .alertModalBody").html(CONSTANTS.SUBMIT_DAY_VIEW);
                $("#hiringAlert .modal-content").addClass('manualWidth');
            }
            //on click of printRoster button the warning popup will throw
            $("#printRoster").click(function(){
                $('#hiringAlert').modal('hide');
                 $("#warningHiringPopup").draggable({
                     handle: ".modal-header"
                 });
                 $("#warningHiringPopup .modal-body #hiringActionMessage")
                 .html(CONSTANTS.printRosterUserMessage);
                 self.blockPopup('#warningHiringPopup');
                 $("#warningHiringPopup").modal('show');
                 $("#warningHiringPopup .modal-content").removeClass(
                         'deletePopupWidth');
            });
            //on click of export Roster button the warning popup will throw
            $("#exportRoster").click(function(){
                $('#hiringAlert').modal('hide');
                 $("#warningHiringPopup").draggable({
                     handle: ".modal-header"
                 });
                 $("#warningHiringPopup .modal-body #hiringActionMessage")
                 .html(CONSTANTS.ExportRosterUserMessage);
                 self.blockPopup('#warningHiringPopup');
                 $("#warningHiringPopup").modal('show');
                 $("#warningHiringPopup .modal-content").removeClass(
                         'deletePopupWidth');
            });
        }
        //show the notification based on the action(add,delete)slot selection
        else {
            if (actionValue === "hiringAdd" && checkSlotArray>0) {
                self.addWarningNotifications();
            } else if (actionValue === "hiringDelete"&& deleteSlotArray>0) {
                responseMessage = "Delete Selected Availability Slots. Are You Sure?";
                $("#warningHiringPopup .modal-body #hiringActionMessage").html(responseMessage);
                $("#warningHiringPopup .modal-body #hiringActionMessage").removeClass('actionResponseMessage');
                $("#warningHiringPopup .modal-body #hiringActionMessage").removeClass('printPacketMessage');
                $("#warningHiringPopup .modal-body #hiringActionMessage").addClass("deleteActionMessage");
                self.blockPopup('#warningHiringPopup');
                $("#warningHiringPopup").draggable({
                    handle: ".modal-header"
                });
                $("#warningHiringPopup").modal('show');
                $("#warningHiringPopup .modal-content").addClass('deletePopupWidth').removeClass("printPacketWidth");
            } else if (actionValue === "hiringPrintPackets" && hiringPrintPacketArray>0) {
                var slotMessage = "<div class='cols-xs-12 cols-xs-offset-1 cellaligncenetr'>You have selected to create Interview Packets for "
                        + hiringPrintPacketArray
                        + " Candidate(s).</div>"
                        + "<div class='cols-xs-12 cols-xs-offset-1'>Depending on the number of Candidates selected this could take a couple of minutes to create.</div>" +
                                "<div class='cols-xs-12 cols-xs-offset-1 cellaligncenetr'>Are You Sure?</div>";
                $("#warningHiringPopup .modal-body #hiringActionMessage").html(slotMessage);
                self.blockPopup('#warningHiringPopup');
                $("#warningHiringPopup").draggable({
                    handle: ".modal-header"
                });
                $("#warningHiringPopup").modal('show');
                $("#warningHiringPopup .modal-content").removeClass('deletePopupWidth').addClass("printPacketWidth");
                $("#warningHiringPopup .modal-body #hiringActionMessage").removeClass('actionResponseMessage');
                $("#warningHiringPopup .modal-body #hiringActionMessage").removeClass('deleteActionMessage').addClass('printPacketMessage');
            }
        }
    };
    /*
     * Method to validate and show the warning message for
     * "ADD" action
     **/
    this.addWarningNotifications = function(){
        var addSelected = [];
        var numOfInterviews = 0;
        var addedSlotsPerDay = 0;
        var addedSlots=0;
        var totalAddedSlots=0;
        var totalAddedSlotsPerDay=0;
        var numOfRecurring=1;
        var responseMessage="";
        $("#showSlotPopup .modal-body #hiringActionMessage").empty();
        //get an id for selected check
        $('input:checkbox[name=hiringAddCheckSlot]:checked').each(function() {
            addSelected.push($(this).attr('id'));
        });
        //check the length is 1 and show the count of interview
        if (addSelected.length === 1) {
            numOfInterviews = $(
                    "#hiringConcurrentInterview" + addSelected[0]
                        + " option:selected").text();
            addedSlotsPerDay = Number(numOfInterviews);
            addedSlots = Number(numOfInterviews) * Number(numOfRecurring);
            totalAddedSlots += addedSlots;
            totalAddedSlotsPerDay += addedSlotsPerDay;
        } else {
            for ( var i = 0; i < addSelected.length; i++) {
                //add the count of interview
                numOfInterviews = Number($(
                "#hiringConcurrentInterview"+ addSelected[i]+ " option:selected").text());
                addedSlotsPerDay = Number(numOfInterviews);
                addedSlots = Number(numOfInterviews) * Number(numOfRecurring);
                totalAddedSlots += addedSlots;
                totalAddedSlotsPerDay += addedSlotsPerDay;
            }
        }
        /*
         * If totalAddedSlots is greater than 680 slots
         * then throw the warning
         */
        if(totalAddedSlots > 680){
            self.setDraggableFocus();
            $("#showSlotPopup").modal('hide');
            $('#hiringAlert #alertModalLabel').text("Add Slot Number Exceeded");
            $("#hiringAlert .modal-content").addClass("slotExceedWidth");
            $("#hiringAlert .modal-body").addClass("slotExceedBodyHeight");
            $("#hiringAlert .modal-body .alertModalBody").addClass("slotExceedModalBody");
            $("#hiringAlert .modal-body .alertModalBody").html(CONSTANTS.SlotExceed);
        }
        //Check the total slot is equal
        else if(totalAddedSlotsPerDay === totalAddedSlots){
             self.blockPopup('#warningHiringPopup');
             $("#warningHiringPopup").draggable({
                 handle: ".modal-header"
             });
             responseMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add "
            + totalAddedSlots + " thirty minute interview slot(s) for "
            + UTILITY.stringFormattedDate(MODEL.HiringSelectedDate)+".</div> <div class='cols-xs-12 cols-xs-offset-1'> Are You Sure?</div>";
             $("#warningHiringPopup .modal-body #hiringActionMessage").html(responseMessage);
             $("#warningHiringPopup").modal('show');
             $("#warningHiringPopup .modal-content").removeClass('deletePopupWidth');
             $("#warningHiringPopup .modal-content").removeClass("printPacketWidth");
             $("#warningHiringPopup .modal-body  #hiringActionMessage").removeClass('printPacketMessage');
             $("#warningHiringPopup .modal-body").removeClass('deleteActionMessage').addClass('actionResponseMessage');
        }else{
            responseMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add "
                + totalAddedSlotsPerDay
                + " thirty minute interview slot(s) for "
                +  UTILITY.stringFormattedDate(MODEL.HiringSelectedDate) + "</div><div class='cols-xs-12 cols-xs-offset-1'> a total of "
                + totalAddedSlots
                + " slot(s) will be added based on recurring weeks selection(s).</div> <div class='cols-xs-12 cols-xs-offset-1'>Are You Sure?</div>";
            self.blockPopup('#warningHiringPopup');
            $("#warningHiringPopup .modal-body #hiringActionMessage").html(responseMessage);
            $("#warningHiringPopup").modal('show');
            $("#warningHiringPopup .modal-content").removeClass("printPacketWidth");
            $("#warningHiringPopup .modal-body  #hiringActionMessage").removeClass('printPacketMessage');
            $("#warningHiringPopup .modal-content").removeClass('deletePopupWidth');
            $("#warningHiringPopup .modal-body  #hiringActionMessage").removeClass('deleteActionMessage').addClass('actionResponseMessage');
        }
    };
    /*
     * Method to submit the roster
     * it will open a pdf or excel in another
     * tab with data
     **/
    this.submitViewAction = function() {
        var beginTimeStamp = MODEL.HiringSelectedDate + " 00:00:00";
        var hireEventId = MODEL.HireEventId;
        var calendarId = MODEL.HiringCalendarId;
        var endTimeStamp = MODEL.HiringSelectedDate + " 23:59:59";
        var hireEventName = $("#hiringSelect option:selected").text();
        //if its print roster the PDF will be open in another window with the details
        if (MODEL.RosterType === "printRoster") {
            var printRosterServiceUrl = CONSTANTS.PrintRosterService
                    + "?beginTimeStamp=" + beginTimeStamp + "&hireEventId="
                    + hireEventId + "&calendarId=" + calendarId
                    + "&endTimeStamp=" + endTimeStamp + "&hireEventName="
                    + hireEventName;
            //it will open the individual window in browser
            window.open(printRosterServiceUrl, '_blank');
        }
        //if its export roster the excel will be open with the details
        else if (MODEL.RosterType === "exportRoster") {
            var exportRosterServiceUrl = CONSTANTS.ExportRosterExcelService
                    + "?calendarId=" + calendarId + "&endTimeStamp="
                    + endTimeStamp + "&hireEventName=" + hireEventName
                    + "&beginTimeStamp=" + beginTimeStamp + "&hireEventId="
                    + hireEventId;
            //it will open the individual window in browser and parellely excel
            window.open(exportRosterServiceUrl, '_blank');
        }
    };
    /*
     * Method: To submit the Add action and
     * service called up
     * service returns the requested number of
     * request and slots
     */
    this.submitAddAction = function() {
        var beginTime = "";
        var endTime = "";
        var numOfInterviews = "";
        var mycalendar = {};
        var myRequest = {};
        var selectedCheckId = [];
        var addAvailabilityJSON = {};
        myRequest.calendarRequest = {};
        myRequest.calendarRequest.availabilityBlocks = {};
        myRequest.calendarRequest.availabilityBlocks.availabilityBlock = [];
        //block for adding slots from Addblock of time popup
        if (MODEL.AddTimeBlock) {
            //method call will get the details of Addblock
            self.submitAddTimeBlock();
        }      
        else {
            //get the id for selected slots
            $('input:checkbox[name=hiringAddCheckSlot]:checked').each(function() {
                        selectedCheckId.push($(this).attr('id'));
            });
            //Single request forms for selected check length is 1
            if (selectedCheckId.length === 1) {
                numOfInterviews = $(
                        "#hiringConcurrentInterview" + selectedCheckId[0]
                                + " option:selected").text();
                beginTime = UTILITY.formatTimeStamp(MODEL.HiringSelectedDate
                        + "-" + $("#hiddenTime" + selectedCheckId[0]).val());
                endTime = UTILITY
                        .addThirtyMinutesToStamp(MODEL.HiringSelectedDate + "-"
                                + $("#hiddenTime" + selectedCheckId[0]).val());
                mycalendar = {
                    "calendarId" : MODEL.HiringCalendarId,
                    "beginTimestamp" : beginTime,
                    "endTimestamp" : endTime,
                    "storeNumber" : MODEL.StoreNumber,
                    "numberOfInterviewers" : numOfInterviews,
                    "numberOfWeeksRecurring" : 1
                };
                myRequest.calendarRequest.availabilityBlocks.availabilityBlock
                        .push(mycalendar);
                //stringify the request and pass to service
                addAvailabilityJSON[CONSTANTS.AttributeType] = JSON
                        .stringify(myRequest);
            } else {
                //Multiple request forms for selected check length is greater than 1
                for ( var j = 0; j < selectedCheckId.length; j++) {
                    numOfInterviews = $(
                            "#hiringConcurrentInterview" + selectedCheckId[j]
                                    + " option:selected").text();
                    beginTime = UTILITY
                            .formatTimeStamp(MODEL.HiringSelectedDate
                                    + "-"
                                    + $("#hiddenTime" + selectedCheckId[j])
                                            .val());
                    endTime = UTILITY
                            .addThirtyMinutesToStamp(MODEL.HiringSelectedDate
                                    + "-"
                                    + $("#hiddenTime" + selectedCheckId[j])
                                            .val());
                    mycalendar = {
                        "calendarId" : MODEL.HiringCalendarId,
                        "beginTimestamp" : beginTime,
                        "endTimestamp" : endTime,
                        "storeNumber" : MODEL.StoreNumber,
                        "numberOfInterviewers" : numOfInterviews,
                        "numberOfWeeksRecurring" : 1
                    };
                    //form the params
                    myRequest.calendarRequest.availabilityBlocks.availabilityBlock
                            .push(mycalendar);
                    //stringify the request and pass to service
                    addAvailabilityJSON[CONSTANTS.AttributeType] = JSON
                            .stringify(myRequest);
                }}
            //service call for adding slots
            MODEL.AddAvailabilitySlots = addAvailabilityJSON;
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.addTimeSlots);
            //Service URl for adding slots availability
            var addAvailabilityUrl = CONSTANTS.AddAvailabilityService;
            RSASERVICES.addAvailabilityPost(callbackFunction,
                    addAvailabilityUrl, MODEL.AddAvailabilitySlots);
        }
    };
    /*
     * Method: To submit the Add action and
     * service called up
     * service returns the requested number of
     * request and slots
     */
    this.submitAddTimeBlock = function(){
        var addBlock= {};
        var params={};
        var request={};
        request.calendarRequest = {};
        request.calendarRequest.availabilityBlocks = {};
        request.calendarRequest.availabilityBlocks.availabilityBlock = [];
        var beginBlockTime = $("#hiringBlockBeginTime option:selected").val();
        var endBlockTime = $("#hiringBlockEndTime option:selected").val();
        var interviews = $("#hiringBlockInterview option:selected").text();
        params = {
            "calendarId" : MODEL.HiringCalendarId,
            "beginTimestamp" : MODEL.HiringSelectedDate + " "
                    + beginBlockTime,
            "endTimestamp" : MODEL.HiringSelectedDate + " " + endBlockTime,
            "storeNumber" : MODEL.StoreNumber,
            "numberOfInterviewers" : interviews,
            "numberOfWeeksRecurring" : 1
        };
        request.calendarRequest.availabilityBlocks.availabilityBlock
                .push(params);
        //stringify the request and pass to service
        addBlock[CONSTANTS.AttributeType] = JSON
                .stringify(request);
        MODEL.AddAvailabilitySlots = addBlock;
        //service call for adding slots
        var addBlockCallBackFunction = $.Callbacks('once');
        addBlockCallBackFunction.add(this.getAddBlockTimeResponse);
        //Service URl for adding slots availability
        var addTimeAvailabilityUrl = CONSTANTS.AddAvailabilityService;
        RSASERVICES.addAvailabilityPost(addBlockCallBackFunction,
                addTimeAvailabilityUrl, MODEL.AddAvailabilitySlots);
    };
    /*
     * Method to show the final notofication message
     * getting slots added after
     **/
    this.getAddBlockTimeResponse = function(reponse) {
        MODEL.AddBlockResponse = reponse.calendarResponse;
        var resp = MODEL.AddBlockResponse;
        //check the response status and if numSlotsAffected is greater than 0 then show
        //the notification message
        if (resp && resp.numSlotsRequested>0){
            var slotsRequested=resp.numSlotsRequested;
            var slotsAffected=resp.numSlotsAffected;
            self.setDraggableFocus();
            $("#hiringAlertLabel").empty(); 
            //compare the slotsAffected and slotsRequested is equal
            if (slotsAffected === slotsRequested)
            {
                $("#hiringAlert .modal-body .alertModalBody").html(
                    resp.numSlotsAffected + " thirty minute Slot(s) added");
                 $("#hiringAlertLabel").text("Slot Addition Confirmation");
                 $("#hiringAlert .modal-body").addClass('popupHeight');
            }else{
                $("#hiringAlert .modal-body .alertModalBody").html('<div>'+
                        slotsRequested + " thirty Minute Slot(s) Requested,</div><div>however only " + slotsAffected +  " were added.</div></br>" +
                "<div>There can only be at most 20 slots per </div><div>thirty minute time slot and 680 slots per</div> day.");
                 $("#hiringAlertLabel").text("Slot Addition Error");
                 $("#hiringAlert .modal-body").addClass('autoHeight');
                 $("#hiringAlert .modal-content").addClass("slotContentTop");
            }
            $("#hiringAlert .modal-content").addClass('popupWidth');
        }
        //check the respone has error
        else if(resp && resp.hasOwnProperty("error")){
            if (_.isArray(resp.error)) {
                CONSTANTS.SERVICEERROR = resp.error;
            } else {
                CONSTANTS.SERVICEERROR = [resp.error];
            }
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.SERVICEERROR);
            self.setDraggableFocus();
            $("#hiringAlert .modal-body .alertModalBody").html(returnedErrorMessage);
            $("#hiringAlert .modal-body").addClass("autoHeight");
            $("#hiringAlert .modal-content").addClass('popupWidth');
            $("#hiringAlertLabel").text("");
       }
    };
    /*
     * Method: on click of cancel and close button
     **/
    this.closeBlockTimePopup = function() {
        MODEL.AddTimeBlock = false;
        $('#hiringAddBlocksTime').modal('hide');
        var resp = MODEL.AddBlockResponse;
        //check the status of numSlotsAffected
        if (resp && resp.numSlotsRequested>0) {
            var callbackFunction = $.Callbacks('once');
                callbackFunction.add(self.getHiringCalendarDetail);
                //service call for calendar details service
                var calendarDetailUrl = CONSTANTS.CalendarDetailService;
                RSASERVICES.calendarDetailsGet(callbackFunction,
                        calendarDetailUrl, MODEL.HiringCalendarId,
                        MODEL.HiringSelectedDate);
                $(".hiringViewContainer").show();
                $('.model-radio input:radio[value="hiringView"]').prop(
                        'checked', true);
                $(".hiringAddContainer").hide();
                $(".hiringDeleteContainer").hide();
                $(".hiringPrintPacketContainer").hide();
        }
        else{
        	 $(".hiringViewContainer").show();
             $('.model-radio input:radio[value="hiringView"]').prop(
                     'checked', true);
             $(".hiringAddContainer").hide();
             $(".hiringDeleteContainer").hide();
             $(".hiringPrintPacketContainer").hide();
        }
    };
    /*
     * Method: on submit of delete action,
     * the slots will be deleted in which
     * selected slots.
     **/
    this.submitDeleteAction = function() {
        var selectedArray = [];
        var selectedSubArray = [];
        var request = {};
        var deleteBlock = {};
        var deleteAvailabilityJSON = {};
        deleteBlock.calendarRequest = {};
        deleteBlock.calendarRequest.requisitionSchedules = [];
        var checkSchedule = [];
        var checkHiringId=[];
        var scheduleObj = {};
        scheduleObj.requisitionSchedule = [];
        //get the value for selected slots
        $('input:checkbox[name=hiringDeleteSlot]:checked').each(function() {
            checkSchedule.push($(this).val());
            checkHiringId.push($(this).attr('id'));
        });
        //check the model value having data
        if(MODEL.HiringCalendarDetailList){
            for(var j=0;j<MODEL.HiringCalendarDetailList.requisitionScheduleSlotDtl.length;j++){
                selectedArray = MODEL.HiringCalendarDetailList.requisitionScheduleSlotDtl[j];
                //Assign the array on validating the data is a single array or more than
              //check object and assign it to another object
                if(selectedArray.requisitionSchedules){
                    if(UTILITY.IsArray(selectedArray.requisitionSchedules[0].requisitionSchedule)){
                    selectedSubArray = selectedArray.requisitionSchedules[0].requisitionSchedule;
                }else{
                    selectedSubArray[0]=selectedArray.requisitionSchedules[0].requisitionSchedule;
                }
                    /*
                     * Check if selected deleted length is 1
                     */
                    if (checkHiringId.length === 1) {
                        var time = checkSchedule[0].split("-")[0];
                        var slotSequenceNum = checkSchedule[0].split("-")[1];
                        //compare those slots time is equal to selected time
                        if (selectedArray.slotTime === time) {
                            for(var block=0;block<selectedSubArray.length;block++){
                                if(selectedSubArray[block].sequenceNumber=== Number(slotSequenceNum)){
                                    request = {
                                        "calendarId" : selectedSubArray[block].calendarId,
                                        "beginTimestamp" : selectedSubArray[block].beginTimestamp,
                                        "createTimestamp" : selectedSubArray[block].createTimestamp,
                                        "createSystemUserId" : selectedSubArray[block].createSystemUserId,
                                        "lastUpdateSystemUserId" : selectedSubArray[block].lastUpdateSystemUserId,
                                        "lastUpdateTimestamp" : selectedSubArray[block].lastUpdateTimestamp,
                                        "storeNumber" : selectedSubArray[block].storeNumber,
                                        "sequenceNumber" : selectedSubArray[block].sequenceNumber,
                                        "requisitionScheduleStatusCode" : selectedSubArray[block].requisitionScheduleStatusCode
                                    };
                                }
                            }
                            //form the request
                            scheduleObj.requisitionSchedule.push(request);
                            deleteBlock.calendarRequest.requisitionSchedules[0] = scheduleObj;
                            deleteAvailabilityJSON[CONSTANTS.AttributeType] = JSON
                                    .stringify(deleteBlock);
                        }
                    }
                    /*
                     * Check if selected deleted length is more than 1
                     */
                else if(checkHiringId.length>1){
                    for ( var r = 0; r < checkSchedule.length; r++) {
                        var schedTime = checkSchedule[r].split("-")[0];
                        var sequenceNum = checkSchedule[r].split("-")[1];
                      //compare those slots time is equal to selected time
                        if (selectedArray.slotTime === schedTime) {
                            for ( var m = 0; m < selectedSubArray.length; m++) {
                                if (selectedSubArray[m].sequenceNumber === Number(sequenceNum)) {
                                    request = {
                                        "calendarId" : selectedSubArray[m].calendarId,
                                        "beginTimestamp" : selectedSubArray[m].beginTimestamp,
                                        "createTimestamp" : selectedSubArray[m].createTimestamp,
                                        "createSystemUserId" : selectedSubArray[m].createSystemUserId,
                                        "lastUpdateSystemUserId" : selectedSubArray[m].lastUpdateSystemUserId,
                                        "lastUpdateTimestamp" : selectedSubArray[m].lastUpdateTimestamp,
                                        "storeNumber" : selectedSubArray[m].storeNumber,
                                        "sequenceNumber" : selectedSubArray[m].sequenceNumber,
                                        "requisitionScheduleStatusCode" : selectedSubArray[m].requisitionScheduleStatusCode

                                    };
                                    scheduleObj.requisitionSchedule.push(request);
                                }
                            }
                        }
                    }
                    deleteBlock.calendarRequest.requisitionSchedules[0]=scheduleObj;
                    deleteAvailabilityJSON[CONSTANTS.AttributeType] = JSON.stringify(deleteBlock);
                   }
                }
            }
        }
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.deletTimeSlot);
        //service call for delete
        var removeAvailabilityUrl = CONSTANTS.RemoveAvailabilityService;
        RSASERVICES.removeAvailabilityPost(callbackFunction,
                removeAvailabilityUrl, deleteAvailabilityJSON);
    };
    /*
     *Method: to get response on callback for add
     *availability service
     **/
    this.addTimeSlots = function(response) {
        if(response && response.calendarResponse.hasOwnProperty("error")){
            if (_.isArray(response.calendarResponse.error)) {
                CONSTANTS.SERVICEERROR = response.calendarResponse.error;
            } else {
                CONSTANTS.SERVICEERROR = [response.calendarResponse.error];
            }
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.SERVICEERROR);
            $(".hiringViewContainer").show();
            $('.model-radio input:radio[value="hiringView"]').prop(
                    'checked', true);
            $(".hiringAddContainer").hide();
            $(".hiringDeleteContainer").hide();
            $(".hiringPrintPacketContainer").hide();
            self.setDraggableFocus();
            $("#hiringAlert .modal-body .alertModalBody").html(returnedErrorMessage);
            $("#hiringAlert .modal-body").addClass("autoHeight");
            $("#hiringAlert .modal-content").addClass('popupWidth');
       }
        //check the numSlotsAffected is greater than 0
        else if (response.calendarResponse.hasOwnProperty("numSlotsRequested")) {
                MODEL.AddBlockResponse = response.calendarResponse;
                var callbackFunction = $.Callbacks('once');
                callbackFunction.add(self.getHiringCalendarDetail);
                var calendarDetailUrl = CONSTANTS.CalendarDetailService;
                RSASERVICES.calendarDetailsGet(callbackFunction, calendarDetailUrl,
                        MODEL.HiringCalendarId, MODEL.HiringSelectedDate);
                $(".hiringViewContainer").show();
                $('.model-radio input:radio[value="hiringView"]').prop(
                        'checked', true);
                $(".hiringAddContainer").hide();
                $(".hiringDeleteContainer").hide();
                $(".hiringPrintPacketContainer").hide();
                var slotsRequested=response.calendarResponse.numSlotsRequested;
                   var slotsAffected=response.calendarResponse.numSlotsAffected;
                   $("#hiringAlertLabel").empty();
                   self.setDraggableFocus();
                   //compare the slotsAffected and slotsRequested is equal
                if (slotsAffected === slotsRequested)
                 {
                     $("#hiringAlert .modal-body .alertModalBody").html(
                             slotsAffected + " thirty minute Slot(s) added");
                      $("#hiringAlertLabel").text("Slot Addition Confirmation");
                      $("#hiringAlert .modal-body").addClass('popupHeight');
                 }else{
                     $("#hiringAlert .modal-body .alertModalBody").html('<div>'+
                             slotsRequested + " thirty Minute Slot(s) Requested,</div><div>however only " + slotsAffected +  " were added.</div></br>" +
                                     "<div>There can only be at most 20 slots per </div><div>thirty minute time slot and 680 slots per</div> day.");
                      $("#hiringAlertLabel").text("Slot Addition Error");
                      $("#hiringAlert .modal-body").addClass('autoHeight');
                      $("#hiringAlert .modal-content").addClass("slotContentTop");
                 }
                 $("#hiringAlert .modal-content").addClass('popupWidth');
         }
    };
    /*
     * Method: to get response on callback for delete
     * availability service
     **/
    this.deletTimeSlot = function(response) {
        if(response && response.calendarResponse.hasOwnProperty("error")){
            if (_.isArray(response.calendarResponse.error)) {
                CONSTANTS.SERVICEERROR = response.calendarResponse.error;
            } else {
                CONSTANTS.SERVICEERROR = [response.calendarResponse.error];
            }
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.SERVICEERROR);
            self.setDraggableFocus();
            $(".hiringViewContainer").show();
            $('.model-radio input:radio[value="hiringView"]').prop(
                    'checked', true);
            $(".hiringAddContainer").hide();
            $(".hiringDeleteContainer").hide();
            $(".hiringPrintPacketContainer").hide();
            $("#hiringAlert .modal-body .alertModalBody").html(returnedErrorMessage);
            $("#hiringAlert .modal-body").addClass("autoHeight");
            $("#hiringAlert .modal-content").addClass('popupWidth');           
       }
        //check the numSlotsAffected is greater than 0
        else if (response && response.calendarResponse
            && response.calendarResponse.numSlotsAffected > 0) {
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(self.getHiringCalendarDetail);
            var calendarDetailUrl = CONSTANTS.CalendarDetailService;
            RSASERVICES.calendarDetailsGet(callbackFunction, calendarDetailUrl,
                    MODEL.HiringCalendarId, MODEL.HiringSelectedDate);
            $(".hiringViewContainer").show();
            $('.model-radio input:radio[value="hiringView"]').prop('checked',
                    true);
            $(".hiringAddContainer").hide();
            $(".hiringDeleteContainer").hide();
            self.setDraggableFocus();
            $("#hiringAlert .modal-body .alertModalBody").html(
                    '<div>' + response.calendarResponse.numSlotsRequested
                            + " slot(s) requested for deletion.</div><div>"
                            + response.calendarResponse.numSlotsAffected
                            + " thirty minute slot(s) deleted.</div>");
            $("#hiringAlert .modal-body").addClass('popupHeight');
            $("#hiringAlert .modal-content").addClass('popupWidth');
            $("#hiringAlertLabel").text("Slot Deletion Confirmation");
            MODEL.DeleteSuccessResponse = response.calendarResponse.numSlotsRequested;
         }
    };
    /*
     * Method: to load the calendar summary details
     **/
    this.loadSummary = function() {
        MODEL.AddPrintPacket = false;
        $('#hiringDayViewModal').modal({
            backdrop: 'static',
            keyboard: false
        });
        $("#hiringDayViewModal").modal('hide');
        var addBlockResp = MODEL.AddBlockResponse;
        var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
        /*
         * It will check Delete success response and
         * other responses also will validate of add
         * and addblockoftime
         */
        if (MODEL.DeleteSuccessResponse || (addBlockResp && addBlockResp.numSlotsRequested > 0)) {
            //check the service response and based on load calendar summary
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.getCalendarSummary);
            RSASERVICES.calendarSummaryGet(callbackFunction, calendarSumaryUrl,
                    MODEL.SelectHiringStartDate, MODEL.HiringCalendarId, MODEL.SelectHiringEndDate);
        }
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method: To show the delete slot details
     * This option has delete action if required
     * to delete the slots
     */
    this.deleteTimeBlock = function() {
        var deleteSplitTime = "";
        var viewTimeResponse = [];
        var schedule = [];
        var requisitionSchedulesSlot = [];
        var row = "";
        var ampm = "";
        var convertedTime = "";
        $('#hiringDeleteSlotBody').empty();
        if(MODEL.HiringCalendarDetailList){
            viewTimeResponse = MODEL.HiringCalendarDetailList;
            for ( var slotCount = 0; slotCount < viewTimeResponse.requisitionScheduleSlotDtl.length; slotCount++) {
                schedule = viewTimeResponse.requisitionScheduleSlotDtl[slotCount];
                deleteSplitTime = schedule.slotTime.split(":");
                //check the hours validation
                ampm = deleteSplitTime[0] < 12 ? "am" : "pm";
                convertedTime = deleteSplitTime[0] > 12 ? Number(deleteSplitTime[0]) - 12
                        : (deleteSplitTime[0] !== "10" ? deleteSplitTime[0].replace(
                                "0", "") : deleteSplitTime[0]);
                CONSTANTS.SHOWTIME = convertedTime + deleteSplitTime[1];
                row = '<tr>';
                //check the slot having requisitionSchedules
                if (schedule && schedule.requisitionSchedules) {
                    requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                    if (requisitionSchedulesSlot.length > 1) {
                        row=self.multipleDeleteTimeBlock(requisitionSchedulesSlot,ampm,convertedTime,row,deleteSplitTime[1],schedule,row);
                    }
                    //block will have multiple row with unique time and schedule
                    else if (requisitionSchedulesSlot) {
                        row=self.singleDeleteTimeBlock(requisitionSchedulesSlot,ampm,convertedTime,row,deleteSplitTime[1],schedule,row);
                    }
                    row += '</tr>';
                }
                //block will have multiple row with unique time only
                else {
                      row += deleteSplitTime[1]==="00"?'<td class="col-xs-5 bold-span">' + convertedTime + ':'
                         + deleteSplitTime[1] + ' ' + ampm
                         + '</td>':'<td class="col-xs-5">' + convertedTime + ':'
                         + deleteSplitTime[1] + ' ' + ampm
                         + '</td>';
                    row += '<td class="col-xs-6"></td>';
                    row += '<td class="col-xs-1"><label id="hiringDeleteLabel'+CONSTANTS.SHOWTIME+'">'+
                    '<input type="checkbox" name="hiringDeleteSlot" id='
                            + CONSTANTS.SHOWTIME+ ' value='+ schedule.slotTime
                            + ' disabled="disabled"/></label></td>';
                    row += '</tr>';
                }
                $('#hiringDeleteSlotBody').append(row);
                //set the tooltip for enabled or disabled checkbox
                $( "hiringDeleteLabel"+CONSTANTS.SHOWTIME).attr("title",
                        CONSTANTS.DELETE_CHECK_DISABLE_TOOLTIP);
                $("input:checkbox[name=hiringDeleteSlot]:enabled").attr("title",
                        CONSTANTS.DELETE_CHECK_ENABLE_TOOLTIP);
                $("input:checkbox[name=hiringDeleteSlot]:disabled").attr("title",
                        CONSTANTS.DELETE_CHECK_DISABLE_TOOLTIP);
            }
            self.deleteAllCheck();
        }
        $("#hiringDeleteSlotBody tr>td:nth-child(1),#hiringDeleteSlotBody tr>td:nth-child(2)").addClass("selectable SelectIE");
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method to show delete option block with scheduled
     * slots
     */
    this.multipleDeleteTimeBlock = function(requisitionSchedulesSlot,stamp,convertedTimeSlot,row,SplitDeleteTime,schedule,deleteRow){
        var deleteDate="";
        for ( var i = 0; i < requisitionSchedulesSlot.length; i++) {
            deleteDate = schedule.requisitionSchedules[0].requisitionSchedule[i].beginTimestamp
                    .substring(11, 16);
            //check the slot time is equal or not
            if (deleteDate === schedule.slotTime) {
                //block will have single row with unique time and schedule
                if (i === 0) {
                    deleteRow += SplitDeleteTime==="00"?
                         '<td class="col-xs-5 bold-span">' + convertedTimeSlot + ':'
                         + SplitDeleteTime + ' ' + stamp
                         + '</td>':'<td class="col-xs-5">' + convertedTimeSlot + ':'
                         + SplitDeleteTime + ' ' + stamp
                         + '</td>';
                    deleteRow = self.showDeleteSlots(requisitionSchedulesSlot[i], deleteRow);
                    if (requisitionSchedulesSlot
                        && (requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 3 ||
                                requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 2)) {
                           deleteRow += '<td class="col-xs-1"><label id="hiringDeleteLabel'+CONSTANTS.SHOWTIME+'">'+
                           '<input type="checkbox" disabled="disabled" name="hiringDeleteSlot" id='
                               + CONSTANTS.SHOWTIME+ ' value='+ schedule.slotTime
                               + '-'+ requisitionSchedulesSlot[i].sequenceNumber+ '>'
                               + '<input type="hidden" value="'
                               + requisitionSchedulesSlot[i].sequenceNumber
                               + '" id="hiddenTime'+ CONSTANTS.SHOWTIME+ '"></label></td>';
                     }else{
                           deleteRow += '<td class="col-xs-1"><input type="checkbox" name="hiringDeleteSlot" id='
                               + CONSTANTS.SHOWTIME+ ' value='+ schedule.slotTime
                               + '-'+ requisitionSchedulesSlot[i].sequenceNumber+ '>'
                               + '<input type="hidden" value="'
                               + requisitionSchedulesSlot[i].sequenceNumber
                               + '" id="hiddenTime'+ CONSTANTS.SHOWTIME+ '"></label></td>';
                     }
                 }
                //block will have mulitple row for a unique time and schedule
                else {
                    deleteRow += '</tr>';
                    deleteRow += '<tr><td></td>';
                    deleteRow = self.showDeleteSlots(requisitionSchedulesSlot[i], deleteRow);
                    if (requisitionSchedulesSlot
                            && (requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 3 ||
                                    requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 2)) {
                         deleteRow += '<td class="col-xs-1"><label id="hiringDeleteLabel'+CONSTANTS.SHOWTIME+'">'+
                         '<input type="checkbox" disabled="disabled" name="hiringDeleteSlot" id='
                             + CONSTANTS.SHOWTIME+ ' value='+ schedule.slotTime
                             + '-'+ requisitionSchedulesSlot[i].sequenceNumber+ '>'
                             + '<input type="hidden" value="'
                             + requisitionSchedulesSlot[i].sequenceNumber
                             + '" id="hiddenTime'+ CONSTANTS.SHOWTIME+ '"></label></td>';
                     }else{
                         deleteRow += '<td class="col-xs-1"><input type="checkbox" name="hiringDeleteSlot" id='
                             + CONSTANTS.SHOWTIME+ ' value='+ schedule.slotTime
                             + '-'+ requisitionSchedulesSlot[i].sequenceNumber+ '>'
                             + '<input type="hidden" value="'
                             + requisitionSchedulesSlot[i].sequenceNumber
                             + '" id="hiddenTime'+ CONSTANTS.SHOWTIME+ '"></label></td>';
                     }
                 }
            }
        }
        return deleteRow;
    };
    /*
     * Method to show the delete scheduled slots
     * for a particular time.
     */
    this.singleDeleteTimeBlock = function(requisitionSchedulesSlot,deleteStamp,deleteTimeSlot,row,SplitDeletedTime,schedule,deletSingleRow){
        var date="";
        date = requisitionSchedulesSlot.beginTimestamp
        .substring(11, 16);
      //check the slot time is equal or not
        if (date === schedule.slotTime) {
            deletSingleRow +=SplitDeletedTime==="00"?'<td class="col-xs-5 bold-span">' + deleteTimeSlot + ':'
                 + SplitDeletedTime + ' ' + deleteStamp
                 + '</td>':'<td class="col-xs-5">' + deleteTimeSlot + ':'
                 + SplitDeletedTime + ' ' + deleteStamp
                 + '</td>';
            deletSingleRow = self.showDeleteSlots(requisitionSchedulesSlot, deletSingleRow);
            if (requisitionSchedulesSlot
                    && (requisitionSchedulesSlot.requisitionScheduleStatusCode === 3 ||
                            requisitionSchedulesSlot.requisitionScheduleStatusCode === 2)) {
                 deletSingleRow += '<td class="col-xs-1"><label id="hiringDeleteLabel'+CONSTANTS.SHOWTIME+'">'+
                 '<input type="checkbox" disabled="disabled" name="hiringDeleteSlot" id='
                     + CONSTANTS.SHOWTIME+ ' value='+ schedule.slotTime+ '-'
                     + requisitionSchedulesSlot.sequenceNumber+ '>'
                     + '<input type="hidden" value="'
                     + requisitionSchedulesSlot.sequenceNumber+ '" id="hiddenTime'
                     + CONSTANTS.SHOWTIME+ '"></label></td>';
             }else{
                 deletSingleRow += '<td class="col-xs-1"><input type="checkbox" name="hiringDeleteSlot" id='
                     + CONSTANTS.SHOWTIME+ ' value='+ schedule.slotTime+ '-'
                     + requisitionSchedulesSlot.sequenceNumber+ '>'
                     + '<input type="hidden" value="'
                     + requisitionSchedulesSlot.sequenceNumber+ '" id="hiddenTime'
                     + CONSTANTS.SHOWTIME+ '"></label></td>';
             }
          }
        return deletSingleRow;
    };
    /*
     * Method to show the slot based on
     * requisitionScheduleStatusCode
     */
    this.showDeleteSlots = function(scheduleSlot,scheduleRow){
        if(scheduleSlot && scheduleSlot.requisitionScheduleStatusCode){
            if(scheduleSlot.requisitionScheduleStatusCode===3){
                //check if scheduleSlot has candidateName
                if(scheduleSlot.candidateName!==undefined){
                    scheduleRow += '<td class="col-xs-6 candidateBackground">'+scheduleSlot.candidateName+'</td>';
                }else{
                    scheduleRow += '<td class="col-xs-6 candidateBackground">Scheduled</td>';
                }
             }
            else if(scheduleSlot.requisitionScheduleStatusCode===2){
                scheduleRow += '<td class="col-xs-6 reserveBackground">Reserved</td>';
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===1){
                scheduleRow += '<td class="col-xs-6 slotBackground">Available</td>';
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===0){
                scheduleRow += '<td class="col-xs-6"></td>';
            }
        }
        return scheduleRow;
    };
    /*
     * Method to check or uncheck
     **/
    this.deleteAllCheck = function(){
        //check the select all
        $('input:checkbox[name=hiringDeleteAllSlot]').click(
        function() {
            if ($("input:checkbox[name=hiringDeleteAllSlot]").prop(
                    'checked')) {
                //Select All Checked
                $("input:checkbox[name=hiringDeleteSlot]:enabled")
                        .prop('checked', true);
            }
            //Select All unChecked
            else {
                $("input:checkbox[name=hiringDeleteSlot]:enabled")
                        .prop('checked', false);
            }
            $("input:checkbox[name=deleteSlot]:disabled").prop('checked',false);
        });
        $("input:checkbox[name=hiringDeleteSlot]:enabled").click(function(){
            $("input:checkbox[name=hiringDeleteAllSlot]").prop('checked',false);
        });
    };
    /*
     * Method: To show the slot time details for Print packet
     * scheduled slots can view in PDF forat also
     * If requisition status code is 3 ,check boxes will be enabled
     */
    this.loadPrintPacket = function() {
        var timeSplit = "";
        var printpacketResponse = [];
        var schedule = [];
        var requisitionSchedulesSlot = [];
        var row = "";
        var ampm = "";
        var convertedTime = "";
        var displayTime = "";
        $('#hiringPrintPacketSlotBody').empty();
        if(MODEL.HiringCalendarDetailList){
            printpacketResponse = MODEL.HiringCalendarDetailList;
            for ( var slotCount = 0; slotCount < printpacketResponse.requisitionScheduleSlotDtl.length; slotCount++) {
                schedule = printpacketResponse.requisitionScheduleSlotDtl[slotCount];
                timeSplit = schedule.slotTime.split(":");
                ampm = timeSplit[0] < 12 ? "am" : "pm";
                displayTime = timeSplit[0] > 12 ? Number(timeSplit[0]) - 12
                   : (timeSplit[0] !== "10" ? timeSplit[0].replace("0", "") : timeSplit[0]);
                convertedTime = timeSplit[0] < 10 ? timeSplit[0].replace("0", "") : timeSplit[0];
                CONSTANTS.SHOWTIME = convertedTime + timeSplit[1];
                row = '<tr>';
                //check the slot having requisitionSchedules
                if (schedule && schedule.requisitionSchedules) {
                    requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                    if (requisitionSchedulesSlot.length > 1) {
                        row = self.loadMultiplePrintPacket(requisitionSchedulesSlot,ampm,schedule,displayTime,timeSplit[1],row);
                    }
                    //block will have multiple row with unique time and schedule
                    else if (requisitionSchedulesSlot) {
                        row = self.loadSinglePrintPacket(requisitionSchedulesSlot,ampm,schedule,displayTime,timeSplit[1],row);
                    }
                    row += '</tr>';
                }
                //block will have multiple row with unique time only
                else {
                    row = self.loadDefaultHiringPacket(row,timeSplit[1],schedule,displayTime,ampm);
                    row += '</tr>';
                }
                $('#hiringPrintPacketSlotBody').append(row);
                self.checkScheduled();
              }
             self.selectAllPrint();
        }
        $("#hiringPrintPacketSlotBody tr>td:nth-child(1),#hiringPrintPacketSlotBody tr>td:nth-child(2)").addClass("selectable SelectIE");
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method to show the default print packets slots.
     * It will have slot time with no scheduled
     */
    this.loadMultiplePrintPacket = function(requisitionSchedulesSlot,printStamp,schedule,convertedPrintTime,printTimeSplit,row){
        var multiplePrintDate="";
        for ( var i = 0; i < requisitionSchedulesSlot.length; i++) {
            multiplePrintDate = requisitionSchedulesSlot[i].beginTimestamp
                    .substring(11, 16);
            //check the slot time and schedule slot time is equal
            if (multiplePrintDate === schedule.slotTime) {
                //block will have single row with unique time and schedule
                if (i === 0) {
                    row += printTimeSplit==="00"?
                           '<td class="col-xs-3 bold-span">' + convertedPrintTime
                        + ':' + printTimeSplit + ' ' + printStamp+ '</td>':
                        '<td class="col-xs-3">' + convertedPrintTime
                        + ':' + printTimeSplit + ' ' + printStamp+ '</td>';
                    row=self.showPacketSlots(requisitionSchedulesSlot[i], row);
                    if(requisitionSchedulesSlot && requisitionSchedulesSlot[i].requisitionScheduleStatusCode===3){
                        row += '<td class="col-xs-1"><input type="checkbox" name="hiringPrintSlot" id='
                             + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot[i].sequenceNumber+'>'
                             + '<input type="hidden" value='+ schedule.slotTime
                             + ' id="hiddenTime'+ CONSTANTS.SHOWTIME + '">'
                             +'<input type="hidden" value="' + requisitionSchedulesSlot[i].phoneScreenId
                             + '" id="phoneId' + CONSTANTS.SHOWTIME + requisitionSchedulesSlot[i].sequenceNumber + '">'
                             +'<input type="hidden" value="' + requisitionSchedulesSlot[i].candidateType
                             + '" id="candidateType' + CONSTANTS.SHOWTIME + requisitionSchedulesSlot[i].sequenceNumber + '"></td>';
                     }else{
                         row += '<td class="col-xs-1"><label id="hiringPrintSlot'+CONSTANTS.SHOWTIME+'">'+
                         '<input type="checkbox" disabled="disabled" name="hiringPrintSlot" id='
                             + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot[i].sequenceNumber+'></label></td>';
                     }
                  }
                //block will have mulitple row for a unique time and schedule
                else {
                    row += '</tr>';
                    row += '<tr><td class="col-xs-3"></td>';
                    row=self.showPacketSlots(requisitionSchedulesSlot[i], row);
                    if(requisitionSchedulesSlot && requisitionSchedulesSlot[i].requisitionScheduleStatusCode===3){
                        row += '<td class="col-xs-1"><input type="checkbox" name="hiringPrintSlot" id='
                          + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot[i].sequenceNumber+'>'
                          + '<input type="hidden" value='+ schedule.slotTime
                          + ' id="hiddenTime'+ CONSTANTS.SHOWTIME + '">'
                          +'<input type="hidden" value="' + requisitionSchedulesSlot[i].phoneScreenId
                          + '" id="phoneId' + CONSTANTS.SHOWTIME + requisitionSchedulesSlot[i].sequenceNumber + '">'
                          +'<input type="hidden" value="' + requisitionSchedulesSlot[i].candidateType
                          + '" id="candidateType' + CONSTANTS.SHOWTIME + requisitionSchedulesSlot[i].sequenceNumber + '">';
                          '</label></td>';
                     }else{
                         row += '<td class="col-xs-1"><label id="hiringPrintSlot'+CONSTANTS.SHOWTIME+'">'+
                         '<input type="checkbox" disabled="disabled" name="hiringPrintSlot" id='
                              + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot[i].sequenceNumber+'>'+
                              '</label></td>';
                     }
                 }
           }}
        return row;
    };
    /*
     * Method to show the default print packets slots.
     * It will have slot time with no scheduled
     */
    this.loadDefaultHiringPacket = function(row, timeSplit, schedule,
            convertedSingleTime, timeStamp) {
        row += timeSplit === "00" ? '<td class="col-xs-3 bold-span">'
                + convertedSingleTime + ':' + timeSplit + ' ' + timeStamp
                + '</td>' : '<td class="col-xs-3">' + convertedSingleTime + ':'
                + timeSplit + ' ' + timeStamp + '</td>';
        row += '<td class="col-xs-6"></td>';
        row += '<td class="col-xs-1"><label id="hiringPrintSlot'+CONSTANTS.SHOWTIME+'">'+
         '<input type="checkbox" disabled="disabled" name="hiringPrintSlot" id='
                + CONSTANTS.SHOWTIME
                + '/>'
                + '</label><input type="hidden" value='
                + schedule.slotTime
                + ' id="hiddenTime' + CONSTANTS.SHOWTIME + '"></label></td>';
        return row;
    };
    /*
     * Method to load empty slots with slottime
     */
    this.loadSinglePrintPacket = function(requisitionSchedulesSlot,timestamp,schedule,convertedSingleTime,singleTimeSplit,row){
        var singlePrintDate="";
        singlePrintDate = requisitionSchedulesSlot.beginTimestamp
                .substring(11, 16);
        //compare the slot time
        if (singlePrintDate === schedule.slotTime) {
            row += singleTimeSplit==="00"?'<td class="col-xs-3 bold-span">' + convertedSingleTime
                + ':' + singleTimeSplit + ' ' + timestamp+ '</td>':
                '<td class="col-xs-3">' + convertedSingleTime
                + ':' + singleTimeSplit + ' ' + timestamp+ '</td>';
            row=self.showPacketSlots(requisitionSchedulesSlot, row);
            if(requisitionSchedulesSlot && requisitionSchedulesSlot.requisitionScheduleStatusCode===3){
                row += '<td class="col-xs-1"><input type="checkbox" name="hiringPrintSlot" id='
                       + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot.sequenceNumber+'>'
                       + '<input type="hidden" value='+ schedule.slotTime
                       + ' id="hiddenTime'+ CONSTANTS.SHOWTIME + '">'
                       +'<input type="hidden" value="' + requisitionSchedulesSlot.phoneScreenId
                       + '" id="phoneId' + CONSTANTS.SHOWTIME + requisitionSchedulesSlot.sequenceNumber + '">'
                       +'<input type="hidden" value="' + requisitionSchedulesSlot.candidateType
                       + '" id="candidateType' + CONSTANTS.SHOWTIME + requisitionSchedulesSlot.sequenceNumber + '">';
               '</label></td>';
             }else{
                 row += '<td class="col-xs-1"><label id="hiringPrintSlot'+CONSTANTS.SHOWTIME+'">'+
                 '<input type="checkbox" disabled="disabled" name="hiringPrintSlot" id='
                       + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot.sequenceNumber+'>'+
                       '</label></td>';
             }
           }
     return row;
    };
    /*
     * Method to show the slot based on
     * requisitionScheduleStatusCode
     */
    this.showPacketSlots = function(scheduleSlot,scheduleRow){
        if(scheduleSlot && scheduleSlot.requisitionScheduleStatusCode){
            //check if requisitionScheduleStatusCode has 3
            if(scheduleSlot.requisitionScheduleStatusCode===3){
                //check if scheduleSlot has candidateName
                if(scheduleSlot.candidateName!==undefined){
                    scheduleRow += '<td class="col-xs-6 candidateBackground">'+scheduleSlot.candidateName+'</td>';
                }else{
                    scheduleRow += '<td class="col-xs-6 candidateBackground">Scheduled</td>';
                }
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===2){
                scheduleRow += '<td class="col-xs-6 reserveBackground">Reserved</td>';
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===1){
                scheduleRow += '<td class="col-xs-6 slotBackground">Available</td>';
            }
            else if(scheduleSlot.requisitionScheduleStatusCode===0){
                scheduleRow += '<td class="col-xs-6"></td>';
            }
        }
        return scheduleRow;
    };
    /*
     * Method: To validate the select all
     **/
    this.checkScheduled = function(){
        //set the tooltip for enabled or disabled checkbox
         $("#hiringPrintSlot"+CONSTANTS.SHOWTIME).attr('title',
                 CONSTANTS.PRINT_PACKETS_TOOLTIP);
        $("input:checkbox[name=hiringPrintSlot]:disabled").attr('title',
                CONSTANTS.PRINT_PACKETS_TOOLTIP);
        $("input:checkbox[name=hiringPrintSlot]:enabled").attr('title',
                CONSTANTS.PRINT_PACKETS_ENABLED_TOOLTIP);
    };
    /*
     * Method To check or uncheck the select all checkbox
     **/
    this.selectAllPrint = function(){
        //check the select all
        $('input:checkbox[name=hiringPacketCheckAll]').click(
        function() {
            if ($("input:checkbox[name=hiringPacketCheckAll]")
                    .prop('checked')) {
                //check all
                $("input:checkbox[name=hiringPrintSlot]:enabled")
                        .prop('checked', true);
            } else {
                //uncheck all
                $("input:checkbox[name=hiringPrintSlot]:enabled")
                        .prop('checked', false);
            }
            //Default uncheck for disabled checkboxes
            $("input:checkbox[name=hiringPrintSlot]:disabled")
                    .prop('checked', false);
        });
        $("input:checkbox[name=hiringPrintSlot]:enabled").click(function(){
               $("input:checkbox[name=hiringPacketCheckAll]").prop('checked',false);
         });
    };
    /*
     * Method: Submit the print packet action
     **/
    this.submitPrintPacketAction = function() {
        var print = {};
        var printPacketRequest = {};
        print.HiringEventPacket = {};
        print.HiringEventPacket.hiringEventDetail = {};
        print.HiringEventPacket.Applicants = {};
        print.HiringEventPacket.Applicants.ApplicantDetails = [];
        var printSlotId = [];
        var phoneScrenId = "";
        var candidateType = "";
        var id = "";
        //get an id of selected
        $('input:checkbox[name=hiringPrintSlot]:checked').each(function() {
            printSlotId.push($(this).attr('id'));
        });
        //Selected length is 1
        if(printSlotId.length===1){
             var singlePacketSlot =  printSlotId[0].split(":");
             id=singlePacketSlot[0]+""+singlePacketSlot[1];
             phoneScrenId = $("#phoneId"+id).val()!== undefined?$("#phoneId"+id).val():"0";
             candidateType = $("#candidateType"+id).val()!== undefined?$("#candidateType"+id).val():"null";
             print.HiringEventPacket.hiringEventDetail = {
                "eventName" : $("#hiringSelect option:selected").text(),
                "hireEventId" : MODEL.HireEventId
             };
             printPacketRequest = {
                "interviewDateTime" : MODEL.HiringSelectedDate + " "
                        + $("#hiddenTime" + singlePacketSlot[0]).val() + ":00.0",
                "phnscrnId" : phoneScrenId,
                "intExtFlg" : candidateType
            };
            //push the params into hiringEventDetail array
            //push the params into ApplicantDetails array
            print.HiringEventPacket.Applicants.ApplicantDetails
                    .push(printPacketRequest);
        }
        //Selected length is greater than 1
        else if(printSlotId.length > 1) {
            print.HiringEventPacket.hiringEventDetail = {
                "eventName" : $("#hiringSelect option:selected").text(),
                "hireEventId" : MODEL.HireEventId
            };
            for(var j=printSlotId.length-1;j>=0;j--){
                var packetSlot = printSlotId[j].split(":");
                id=packetSlot[0]+""+packetSlot[1];
                phoneScrenId = $("#phoneId"+id).val()!== undefined?$("#phoneId"+id).val():"0";
                candidateType = $("#candidateType"+id).val()!== undefined?$("#candidateType"+id).val():"null";
                printPacketRequest = {
                    "interviewDateTime" : MODEL.HiringSelectedDate + " "
                            + $("#hiddenTime" + packetSlot[0]).val() + ":00.0",
                    "phnscrnId" : phoneScrenId,
                    "intExtFlg" : candidateType
                };
                //push the params into hiringEventDetail array
                //push the params into ApplicantDetails array
                print.HiringEventPacket.Applicants.ApplicantDetails
                        .push(printPacketRequest);
            }
        }
        //Assign the json value to hidden to send service
        var printPacketServiceUrl = CONSTANTS.printPacketService;
        $("#hiringPacketForm").attr("action",printPacketServiceUrl);
        $("input[name='hiringPacket']").attr("value", JSON.stringify(print));
        $("input[name='Content-Type']").attr("value",
                CONSTANTS.APPLICATION_JSON);
        //Refer the event for submit button
        var event = jQuery.Event("submit");
        //On posting form submit the button event will be triggered
        $("#hiringPacketForm").trigger(event);
        var callbackFunction = $.Callbacks('once');
        //call back method
        callbackFunction.add(self.getHiringCalendarDetail);
        var calendarDetailUrl = CONSTANTS.CalendarDetailService;
        //service call
        RSASERVICES.calendarDetailsGet(callbackFunction, calendarDetailUrl,
                MODEL.HiringCalendarId, MODEL.HiringSelectedDate);
        $(".hiringViewContainer").show();
        $('.model-radio input:radio[value="hiringView"]').prop('checked', true);
        $(".hiringAddContainer").hide();
        $(".hiringDeleteContainer").hide();
        $(".hiringPrintPacketContainer").hide();
    };
    /*
     * Method: Method calling based on the action selection
     **/
    this.actionBlocks = function() {
        $("#warningHiringPopup").modal('hide');
        var actionValue = $('input:radio[name=hiringDayViewOption]:checked')
                .val();
        if (actionValue === "hiringAdd") {
            //On submit of add
            self.submitAddAction();
        } else if (actionValue === "hiringDelete") {
            //On submit of delete
            self.submitDeleteAction();
        } else if (actionValue === "hiringView") {
            //On submit of view
            self.submitViewAction();
        } else if (actionValue === "hiringPrintPackets") {
            //On submit of printPackets
            self.submitPrintPacketAction();
        }
    };
    /*
     * Method will make the popup remain in page
     * on keyboard and mosue events.
     */
    this.setDraggableFocus = function(){
        $('#hiringAlert').off("shown.bs.modal");
        $('#hiringAlert').on("shown.bs.modal", function() {
           $("#hiringAlertOk").focus();
        });
        self.blockPopup("#hiringAlert");
        self.removeAlertCss();
        $("#hiringAlert").draggable({
            handle: ".modal-header"
        });      
    };
    this.removeAlertCss = function(){
    	 $("#hiringAlert .modal-body").removeClass("autoHeight");
    	 $("#hiringAlert .modal-content").removeClass('popupWidth');
    	 $("#hiringAlert .modal-body").removeClass('popupHeight');
    	 $("#hiringAlertLabel").text('');
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
     * On change event for actions
     **/
    $('.model-radio input:radio[name=hiringDayViewOption]').off();
    $('.model-radio input:radio[name=hiringDayViewOption]').on('change',function() {
            //on click of view
            if ($(this).val() === "hiringView") {
                 $(".tableContainer").scrollTop(0);
                MODEL.AddPrintPacket = false;
                $(".hiringViewContainer").show();
                $(".hiringAddContainer").hide();
                $(".hiringDeleteContainer").hide();
                $(".hiringPrintPacketContainer").hide();
                //callback method
                self.viewTimeBlock();
                //uncheck the delete and add select all check box
                $('input:checkbox[name=hiringDeleteAllSlot]').attr(
                        'checked', false);
                $('input:checkbox[name=hiringAddAllCheck]').attr('checked',
                        false);
            }
            //on click of delete
            else if ($(this).val() === "hiringDelete") {
                 $(".tableContainer").scrollTop(0);
                MODEL.AddPrintPacket = false;
                //cll back method
                self.deleteTimeBlock();
                $(".hiringDeleteContainer").show();
                $(".hiringAddContainer").hide();
                $(".hiringViewContainer").hide();
                $(".hiringPrintPacketContainer").hide();
                //uncheck the delete and add select all check box
                $('input:checkbox[name=hiringDeleteAllSlot]').attr(
                        'checked', false);
                $('input:checkbox[name=hiringAddAllCheck]').attr('checked',
                        false);
            }
            //on click of add
            else if ($(this).val() === "hiringAdd") {
                 $(".tableContainer").scrollTop(0);
                MODEL.AddTimeBlock = false;
                MODEL.AddPrintPacket = true;
                //callback method
                self.addTimeBlock();
                $("#hiringAddBlockTimeButton").attr("title",CONSTANTS.ADD_BLOCK_TOOLTIP);
                $(".hiringAddContainer").show();
                $(".hiringViewContainer").hide();
                $(".hiringDeleteContainer").hide();
                $(".hiringPrintPacketContainer").hide();
                //uncheck the delete and add select all check box
                $('input:checkbox[name=hiringDeleteAllSlot]').attr(
                        'checked', false);
                $('input:checkbox[name=hiringAddAllCheck]').attr('checked',
                        false);
            }
            //on click of print packets
            else if ($(this).val() === "hiringPrintPackets") {
                 $(".tableContainer").scrollTop(0);
                self.loadPrintPacket();
                //uncheck the delete and add select all check box
                $('input:checkbox[name=hiringDeleteAllSlot]').attr(
                        'checked', false);
                $('input:checkbox[name=hiringAddAllCheck]').attr('checked',
                        false);
                $('input:checkbox[name=hiringPacketCheckAll]').attr('checked',
                        false);
                $(".hiringPrintPacketContainer").show();
                $(".hiringAddContainer").hide();
                $(".hiringViewContainer").hide();
                $(".hiringDeleteContainer").hide();
            }
            $("#hiringAlert .modal-body").removeClass('popupHeight');
            $("#hiringAlert .modal-content").removeClass('popupWidth');
            $("#hiringAlertLabel").empty();
    });
    //set the static value on click "print roster" button
    $("#printRoster").click(function() {
        MODEL.RosterType = "printRoster";
    });
    //set the static value on click "export roster" button
    $("#exportRoster").click(function() {
        MODEL.RosterType = "exportRoster";
    });
};

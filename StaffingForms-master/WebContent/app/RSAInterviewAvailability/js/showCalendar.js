/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: showCalendar.js
 * Application: Retail Staffing Administration
 *
 * This method gets calendar details for the data provided
 *
 * @param inputXml
 * XML/JSON containing the parameters for calendar summary
 * is store number,calendar id,begin date and end date.
 *
 * @param version
 * Optional parameter, can be used later to change the request/response/logic that gets
 * applied. If not provided, it will be defaulted to 1.
 * Version 1 XML Input/Output,
 * version 2 JSON Input/Output
 *
 * @return
 * XML/JSON containing the calendar summary and details for
 * respective calendar id.
 *
 */
/* Method name:ShowCalendar
 * Return a 3 weeks calendar with 21 days.
 * Calendar will also have a daily basis actions.
 */
function showCalendar(){
    var self = this;
    /*
     * Method loads the calendar
     *  with details
     **/
    this.initialize = function(calendarIdType) {
        $('select:not(select[multiple])').selectBoxIt();
        var startDate = "";
        var endDate = "";
        var todayDate = new Date();
        var finishDate = new Date();
        //Add 14 days to set date from current date
        finishDate.setDate(finishDate.getDate() + 14);
        startDate = UTILITY.formatedMonth(todayDate);
        endDate = UTILITY.formatedMonth(finishDate);
        MODEL.SelectedStartDate = startDate;
        MODEL.SelectedEndDate = endDate;
        //Extract the value from passed parameter
        if (calendarIdType) {
            //Split the param and get the values
            var splitValue = calendarIdType.split(":");
            CONSTANTS.CALENDARID = splitValue[0];
            MODEL.CalendarType = splitValue[1];
        }
         /*Service call for calendar summary*/
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getCalendarSummary);
        var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
        RSASERVICES.calendarSummaryGet(callbackFunction,calendarSumaryUrl, startDate,
                CONSTANTS.CALENDARID, endDate);
    };
    /*
     * Method for loading the calendar summary
     * based on calendar drop down option
     * */
    this.changeCalendarSummary = function(){
        var startDate = "";
        var endDate = "";
        var todayDate = new Date();
        var finishDate = new Date();
        finishDate.setDate(finishDate.getDate() + 14);
        startDate = UTILITY.formatedMonth(todayDate);
        endDate = UTILITY.formatedMonth(finishDate);
        //Extract the values from calendar id
        var calendarDetail = $("#calendarSelect option:selected").attr("id");
        calendarDetail = calendarDetail.split(":");
        CONSTANTS.CALENDARID = calendarDetail[0];
        MODEL.CalendarType = calendarDetail[1];
        //service call for calendar summary
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getCalendarSummary);
        var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
        RSASERVICES.calendarSummaryGet(callbackFunction,calendarSumaryUrl, startDate,
                CONSTANTS.CALENDARID, endDate);
    };
    /*
     * Method: To view next 14 days in
     *  calendar summary
     * */
    this.selectNext = function() {
        var nextBeginDate = "";
        var nextFinishDate = "";
        var nextDate = UTILITY.formatDate(CONSTANTS.FINISHEDDATE);
        var date = new Date(nextDate);
        var startDate = new Date(date.getTime()
                + (1 * CONSTANTS.MILLISECONDS_PER_DAY));
        /* Take the new StartDate and add 14 Days to get the new EndDate */
        var endDate = new Date(startDate.getTime()
                + (14 * CONSTANTS.MILLISECONDS_PER_DAY));
        //formatted the date
        nextBeginDate = UTILITY.formatedMonth(startDate);
        nextFinishDate = UTILITY.formatedMonth(endDate);
        MODEL.SelectedStartDate=nextBeginDate;
        MODEL.SelectedEndDate=nextFinishDate;
        $(".calendarDue").html(
                UTILITY.getMonthFormatted(CONSTANTS.INITIALDATE) + " - "
                        + UTILITY.getMonthFormatted(CONSTANTS.FINISHEDDATE));
        //service call to get next 15 days
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getCalendarSummary);
        var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
        RSASERVICES.calendarSummaryGet(callbackFunction,calendarSumaryUrl, nextBeginDate,
                CONSTANTS.CALENDARID, nextFinishDate);

    };
    /*
     * Method: To view previous 14 days details
     * in calendar
     * */
    this.selectPrevious = function() {
        var beginDate = "";
        var finishDate = "";
        var firstDate = "";
        var responseFirstDate = "";
        var intialDate = UTILITY.formatDate(CONSTANTS.INITIALDATE);
        var date = new Date(intialDate);
        firstDate = UTILITY.formatedMonth(date);
        //set the label value
        $(".calendarDue").html(
                UTILITY.getMonthFormatted(CONSTANTS.INITIALDATE) + " - "
                        + UTILITY.getMonthFormatted(CONSTANTS.FINISHEDDATE));
        var compareNewDate = new Date();
        responseFirstDate = UTILITY.formatedMonth(compareNewDate);
        //Compare the date
        if (firstDate === responseFirstDate) {
             self.setAlertDraggableFocus();
            $("#alert .modal-content").addClass('popupWidth');
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").html('<div class="rangeMessage">'+CONSTANTS.CheckDateRange+'</div>');
        } else {
            // Take the selectedStartDate and subtract 15 Days to get the new
            // StartDate
            var startDate = new Date(date.getTime()
                    - (15 * CONSTANTS.MILLISECONDS_PER_DAY));
            // Take the new StartDate and add 14 Days to get the new EndDate
            var endDate = new Date(startDate.getTime()
                    + (14 * CONSTANTS.MILLISECONDS_PER_DAY));
            beginDate = UTILITY.formatedMonth(startDate);
            finishDate = UTILITY.formatedMonth(endDate);
            MODEL.SelectedStartDate=beginDate;
            MODEL.SelectedEndDate=finishDate;
            //service call to get previous 15 days
            var callbackFunction = $.Callbacks('once');
            callbackFunction.add(this.getCalendarSummary);
            var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
            RSASERVICES.calendarSummaryGet(callbackFunction,calendarSumaryUrl, beginDate,
                    CONSTANTS.CALENDARID, finishDate);
        }
    };
    /*
     * Method: To view the calendar with scheduled and
     * received slot details
     * */
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
             self.setAlertDraggableFocus();
             $("#alert .modal-body").addClass("autoHeight");
             $("#alert .modal-body .alertModalBody").text(
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
            self.appendCalendarSummary(calendarSummaryResponse);
        }
    };
    /*
     * Method to append the dynamic calendar with slots
     * Calendar has 3 weeks and 21 days.
     * It has scheduled,received and available slots
     *
     */
    this.appendCalendarSummary = function(calendarSummaryResponse){
        var slotCountResponse = [];
        var calendarShowDate = "";
        var showDateId = "";
        var calendarTooltip="";
        var calendarTable = $(".calendarDetailTable>tbody");
        var indexCount = 0;
         //get the first dayOfWeekIndicator from response
        var startCount = calendarSummaryResponse[0].dayOfWeekIndicator;
        CONSTANTS.INITIALDATE = calendarSummaryResponse[0].date;
        CONSTANTS.FINISHEDDATE = calendarSummaryResponse[calendarSummaryResponse.length - 1].date;
        $(".calendarDue").html(UTILITY.getMonthFormatted(CONSTANTS.INITIALDATE) + " - "
                        + UTILITY.getMonthFormatted(CONSTANTS.FINISHEDDATE));
        $(calendarTable).empty();
        //Iterate the loop for row i.e (for 3 weeks)
        for ( var m = 1; m <= 3; m++) {
            var row = $('<tr></tr>').appendTo(calendarTable);
            //Iterate the loop for column (7 days
            for ( var j = 1; j <= 7; j++) {
                indexCount = (m - 1) * 7 + j;
                if (indexCount >= startCount && ((indexCount - startCount) < calendarSummaryResponse.length)) {
                    showDateId = calendarSummaryResponse[indexCount- startCount].date;
                     MODEL.ShowDate = showDateId;
                    calendarShowDate = calendarSummaryResponse[indexCount
                            - startCount].date;
                    //split the day value from date to show
                    calendarShowDate = calendarShowDate.split("-");
                    calendarShowDate[2] = calendarShowDate[2]<10?calendarShowDate[2].replace("0",""):calendarShowDate[2];
                    calendarTooltip = CONSTANTS.CALENDAR_TOOLTIP+''+UTILITY.stringFormattedDate(showDateId)+''+CONSTANTS.CALENDAR_NOTE_TOOLTIP;
                    //Insert the day value in column and append to row
                    $('<td id="calendarColumn' + showDateId + '" ></td>')
                            .text(calendarShowDate[2]).appendTo(row);
                    $("#calendarColumn" + showDateId + "").attr("title",calendarTooltip);
                    //check the response having StatusSlotCountDtls
                    if (calendarSummaryResponse[indexCount - startCount] &&  calendarSummaryResponse[indexCount
                                    - startCount].StatusSlotCountDtls) {
                        slotCountResponse = calendarSummaryResponse[indexCount - startCount].StatusSlotCountDtls;
                        self.appendCalendarSlots(slotCountResponse);
                    }
             }
             else {
                 //If service not returns dayOfWeekIndicator for a date
                $('<td></td>').appendTo(row).addClass('blockColumn');
            }
                //Method call to get the popup
            self.getPopup();
            }
        }
    };
    /*
     * Method to append the slots for a day basis.
     * It has scheduled,received and available slots.
     *
     */
    this.appendCalendarSlots = function(slotResponse){
        var sloCountValue="";
        var availableCountValue="";
        var reserveCountValue="";
          //Iterate the StatusSlotCountDtls array to get availabeCount,bookedCount and reservedCount
        for ( var slotCount = 0; slotCount < slotResponse.length; slotResponse++) {
            sloCountValue =  slotResponse[slotCount].StatusSlotCountDtl.availabeCount;
            availableCountValue = slotResponse[slotCount].StatusSlotCountDtl.bookedCount;
            reserveCountValue = slotResponse[slotCount].StatusSlotCountDtl.reservedCount;
            //method call to pass data to append list
        //append the list for available schedule slots
        $("#calendarColumn" +  MODEL.ShowDate + "").append(
                $('<ul id="calendarColumn'+  MODEL.ShowDate+ '"><li style="background:#C6F3D5;" id="calendarColumn'
                        +  MODEL.ShowDate
                        + '" class="slotSummaryList">'
                        + sloCountValue
                        + ' Slots(s) Avail</li>'
                        + '<li style="background:#F5F4A3;" id="calendarColumn'
                        +  MODEL.ShowDate
                        + '" class="slotSummaryList">'+availableCountValue+' Slots(s) Sched</li>'
                        + '<li style="background:#E8E3E3;" id="calendarColumn'
                        +  MODEL.ShowDate
                        + '" class="slotSummaryList">'+reserveCountValue+' Slots(s) Rsvd</li></ul>'));
        }
    };
    /*
     * Method: To get and show the day view
     * popup on click of every day
     * */
    this.getPopup = function(){
        $("#calendarColumn" + MODEL.ShowDate + "").on('click',function(event) {
            //get an selected date
            var selectedDate = event.target.id;
            selectedDate = selectedDate.replace(
                    "calendarColumn", "");
            //popup won't close on esc key and outside of mouse
            $('#dayViewModal').modal({
                backdrop: 'static',
                keyboard: false
            });
            $("#dayViewModal").modal('show');
            $("#dayViewModal").draggable({
                handle: ".modal-header"
            });
            //method to pass selected date to get calendar day details
            self.loadPopUp(selectedDate);
        });
    };
    /*
     * Method: To show the popup on click of
     *  day view
     * */
    this.loadPopUp = function(selectedDate) {
        //assign the selected date to constant variable
        CONSTANTS.SELECTEDDATE = selectedDate;
        MODEL.AddPrintPacket =false;
        $("#addBlockTimeButton").attr("title",CONSTANTS.ADD_BLOCK_TOOLTIP);
        $(".daySlotContainer").show();
        $('.model-radio input:radio[value="View"]').prop('checked', true);
        self.validateDayViewButtons(selectedDate);
        $("#postPrintPacketForm").hide();
        $(".addDaySlotContainer").hide();
        $(".deleteDaySlotContainer").hide();
        $(".printPacketSlotContainer").hide();
        $("#alert .modal-body").removeClass('popupHeight');
        $("#alert .modal-content").removeClass('popupWidth');
        $(".tableContainer").scrollTop(0);
        $("#alertModalLabel").empty();
        $(".tableContainer").bind("scroll",function(){
            $(".selectboxit-options.selectboxit-list").css("display", "none");
        });
        //set the label value
        $("#dateSlotDetail").html("Date: "+
                UTILITY.getMonthFormatted(CONSTANTS.SELECTEDDATE)
                        + " / Location: " + MODEL.StoreNumber + " / Calendar: "
                        + $("#calendarSelect option:selected").text());
        var callbackFunction = $.Callbacks('once');
        //viewTimeBlock callback will be called
        callbackFunction.add(this.getCalendarDetail);
        //service call for getting day details in calendar
        var calendarDetailUrl = CONSTANTS.CalendarDetailService;
                RSASERVICES.calendarDetailsGet(callbackFunction,calendarDetailUrl,
                CONSTANTS.CALENDARID, CONSTANTS.SELECTEDDATE);
    };
    /*
     * Method to return and get the whole response
     * for calendar detail service
     */
    this.getCalendarDetail = function(detailResponse){
        MODEL.CalendarDayDetailList=[];
        //check the response has requisitionScheduleSlots object
        if(detailResponse.calendarResponse && detailResponse.calendarResponse.hasOwnProperty("requisitionScheduleSlots")){
            MODEL.CalendarDayDetailList = detailResponse.calendarResponse.requisitionScheduleSlots[0];
            self.viewTimeBlock();
            self.addTimeBlock();
            self.deleteTimeBlock();
            self.loadPrintPacket();
            $(".daySlotContainer").show();
            $('.model-radio input:radio[value="View"]').prop('checked', true);
            $("#postPrintPacketForm").hide();
            $(".addDaySlotContainer").hide();
            $(".deleteDaySlotContainer").hide();
            $(".printPacketSlotContainer").hide();
        }
        //check the response has error property
        else if(detailResponse.calendarResponse.hasOwnProperty("error")){
            CONSTANTS.SERVICEERROR = detailResponse.calendarResponse.error.errorMsg;
            self.setAlertDraggableFocus();
            $("#alert .modal-body").addClass("autoHeight");
            $("#alert .modal-body .alertModalBody").text(
                    CONSTANTS.SERVICEERROR);
        }
    };
    /*
     * Method to validate the radio buttons based on the
     * user selected date.
     * if its current date the add and del buttons
     * will be disabled
     */
    this.validateDayViewButtons = function(dateSelected){
          var todayDate = new Date();
          /*Compare the current date with selected date. Add and delete options will be
           *disabled if current date is selected date
           */
          if(UTILITY.formatedMonth(todayDate)===dateSelected){
              $("#deleteRadioBtn").attr("title",CONSTANTS.DEL_AVAILABILITY_TOOLTIP);
              $("#addRadioBtn").attr("title",CONSTANTS.ADD_AVAILABILITY_TOOLTIP);
              $('.model-radio input:radio[value="Delete"]').attr("title",
                      CONSTANTS.DEL_AVAILABILITY_TOOLTIP);
              $('.model-radio input:radio[value="Add"]').attr("title",
                      CONSTANTS.ADD_AVAILABILITY_TOOLTIP);
              $('.model-radio input:radio[value="Add"]').prop('disabled', true);
              $('.model-radio input:radio[value="Delete"]').prop('disabled', true);
              $("#submitView").attr('disabled', true);
              $("#submitView").addClass('greyPrintBtn').removeClass('submit');
          }else{
              $('.model-radio input:radio[value="Add"]').prop('disabled', false);
              $('.model-radio input:radio[value="Delete"]').prop('disabled', false);
              $("#submitView").attr('disabled', false);
              $("#submitView").removeClass('greyPrintBtn').addClass('submit');
              $("#deleteRadioBtn").removeAttr("title");
              $("#addRadioBtn").removeAttr("title");
              $('.model-radio input:radio[value="Delete"]').removeAttr("title");
              $('.model-radio input:radio[value="Add"]').removeAttr("title");
          }
    };
    /*
     * Method: Selects the previous day's data
     * */
    this.selectDayPrevious = function(){
        var selectedDate = "";
        var firstDate = "";
        var responseFirstDate = "";
        var prevDate = CONSTANTS.SELECTEDDATE.split("-");
        var splittedDate =prevDate[1]+"/"+prevDate[2]+"/"+prevDate[0];
        var date = new Date(splittedDate);
        firstDate = UTILITY.formatedMonth(date);
        var compareNewDate = new Date();
        responseFirstDate = UTILITY.formatedMonth(compareNewDate);
        //Compare the current date with selected date
        if (firstDate === responseFirstDate) {
             self.validateDayViewButtons(firstDate);
             self.setAlertDraggableFocus();
            $("#alert .modal-content").addClass('popupWidth');
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").html('<div class="rangeMessage">'+CONSTANTS.CheckDayRange+'</div>');
        } else {
            var selectedPreviousDate= new Date(splittedDate);
            //Take the selectedDate and subtract 1 Day to get the Previous Day
            var previousDate = new Date(selectedPreviousDate.getTime()
                    - (1 * CONSTANTS.MILLISECONDS_PER_DAY));
            selectedDate = UTILITY.formatedMonth(previousDate);
            CONSTANTS.SELECTEDDATE = selectedDate;
            $("#dateSlotDetail").html("Date: "+
                    UTILITY.getMonthFormatted(selectedDate)
                            + " / Location: " + MODEL.StoreNumber + "/ Calendar: "
                            + $("#calendarSelect option:selected").text());
            //based on action the method will be called for previous day data
            var callbackFunction = $.Callbacks('once');
            self.validateDayViewButtons(selectedDate);
            callbackFunction.add(this.getCalendarDetail);
          //service call for getting previous day details
            var calendarDetailUrl = CONSTANTS.CalendarDetailService;
            RSASERVICES.calendarDetailsGet(callbackFunction,calendarDetailUrl,
                    CONSTANTS.CALENDARID, selectedDate);
        }
    };
    /*
     * Method: Selects the Next day's data
     * */
    this.selectDayNext = function(){
        var date = CONSTANTS.SELECTEDDATE.split("-");
        var splittedDate =date[1]+"/"+date[2]+"/"+date[0];
        var selectedDate= new Date(splittedDate);
        //Take the selectedDate and add 1 Day to get the Next Day
        var nextDate = new Date(selectedDate.getTime()
                + (1 * CONSTANTS.MILLISECONDS_PER_DAY));
        var selectedNextDate = UTILITY.formatedMonth(nextDate);
        //assign the current selected date
        CONSTANTS.SELECTEDDATE = selectedNextDate;
        $("#dateSlotDetail").html("Date: "+
                UTILITY.getMonthFormatted(selectedNextDate)
                        + " / Location: " + MODEL.StoreNumber + "/ Calendar: "
                        + $("#calendarSelect option:selected").text());
        //based on action the method will be called for next day data
        self.validateDayViewButtons(selectedNextDate);
        var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.getCalendarDetail);
        //service call for getting next day details
        var calendarDetailUrl = CONSTANTS.CalendarDetailService;
        RSASERVICES.calendarDetailsGet(callbackFunction,calendarDetailUrl,
                CONSTANTS.CALENDARID, selectedNextDate);
    };
    /*
     * Method: To view the slot time details for
     * a day in calendar
     * */
    this.viewTimeBlock = function() {
        var splitTime = "";
        var viewTimeResponse = [];
        var schedule = [];
        var requisitionSchedulesSlot = [];
        var row = "";
        var ampm = "";
        var convertedTime = "";
        //empty the view block table body
        $('#daySlotBody').empty();
        if(MODEL.CalendarType==="2"){
            $('#metLoclabel').show();
            $("#daySlotContainer").addClass("viewMetContainer");
       }else{
            $('#metLoclabel').hide();
            $("#daySlotContainer").removeClass("viewMetContainer");
       }
        //assign response to defined array
        if(MODEL.CalendarDayDetailList){
            viewTimeResponse = MODEL.CalendarDayDetailList;
            for ( var slotCount = 0; slotCount < viewTimeResponse.requisitionScheduleSlotDtl.length; slotCount++) {
                schedule = viewTimeResponse.requisitionScheduleSlotDtl[slotCount];
                splitTime = schedule.slotTime.split(":");
                //check the am or pm timestamp
                ampm = splitTime[0] < 12 ? "am" : "pm";
                convertedTime = splitTime[0] > 12 ? Number(splitTime[0]) - 12
                : (splitTime[0] !== "10" ? splitTime[0].replace("0", "")
                        : splitTime[0]);
                CONSTANTS.SHOWTIME = convertedTime + ":" + splitTime[1];
                row = '<tr>';
                //check the schedule has the array requisitionSchedules
                if (schedule && schedule.requisitionSchedules) {
                    requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                    //if requisitionSchedules has greater than 1
                    if (requisitionSchedulesSlot.length > 1) {
                        row = self.viewMultipleSlot(requisitionSchedulesSlot,schedule,ampm,splitTime[1],convertedTime,row);
                    }
                    //if requisitionSchedules array has a single object
                    else if(requisitionSchedulesSlot) {
                        row = self.viewSingleSlot(requisitionSchedulesSlot,schedule,ampm,splitTime[1],convertedTime,row);
                    }
                    row += '</tr>';
                }
                else {
                    row += splitTime[1]==="00"?
                            '<td class="col-xs-5 bold-span">' + convertedTime
                            + ':' + splitTime[1] + ' ' + ampm
                            + '</td>':'<td class="col-xs-5">' + convertedTime
                             + ':' + splitTime[1] + ' ' + ampm
                             + '</td>';
                        row += '<td class="col-xs-6"></td>';
                        row += MODEL.CalendarType==="2"? '<td class="col-xs-1"></td>':"";
                        self.validatePrintBtn();
                        row += '</tr>';
                }
                $('#daySlotBody').append(row);
                }
            }
        $("#daySlotBody tr>td:nth-child(1),#daySlotBody tr>td:nth-child(2)").addClass("selectable SelectIE");
        $("#daySlotBody>tr>td::selection").addClass("tableBodyBackground");
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method to show the multiple slot for view block
     * it has scheduled slots for a particular time.
     * The slots will have one or more than also.
     */
    this.viewMultipleSlot = function(requisitionSchedulesSlot,schedule,viewTimeStamp,splitViewTime,convertedViewTime,row){
        var viewDatMultiple="";
        var storeNumber="";
        //Traverse through the requisitionSchedulesSlot array for getting scheduled slot
        for ( var i = 0; i < requisitionSchedulesSlot.length; i++) {
            viewDatMultiple = schedule.requisitionSchedules[0].requisitionSchedule[i].beginTimestamp
                    .substring(11, 16);
            storeNumber = schedule.requisitionSchedules[0].requisitionSchedule[i].storeNumber;
            //compare the slot time with received time
            if (viewDatMultiple === schedule.slotTime) {
                //first scheduled slot
                if (i === 0) {
                    row += splitViewTime==="00"? '<td class="col-xs-5 bold-span">' + convertedViewTime + ':' + splitViewTime + ' '
                           + viewTimeStamp + '</td>':
                           '<td class="col-xs-5">' + convertedViewTime + ':' + splitViewTime + ' '
                           + viewTimeStamp + '</td>';
                    row = self.showSlots(requisitionSchedulesSlot[i],row);
                    self.validatePrintBtn(true);
                    row +=  MODEL.CalendarType==="2"?'<td class="col-xs-1">'+storeNumber+'</td>':"";
                    } else {
                    row += '</tr>';
                    row += '<tr><td class="col-xs-5"></td>';
                    row = self.showSlots(requisitionSchedulesSlot[i],row);
                    row +=  MODEL.CalendarType==="2"?'<td class="col-xs-1">'+storeNumber+'</td>':"";
                    self.validatePrintBtn();
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
    this.viewSingleSlot = function(requisitionSchedulesSlot,schedule,singleViewStamp,splitSingleTime,convertedViewTime,row){
        var viewDateSingle="";
        viewDateSingle = requisitionSchedulesSlot.beginTimestamp
                .substring(11, 16);
        //compare the slot time
        if (viewDateSingle === schedule.slotTime) {
             if(splitSingleTime==="00"){
                 row += '<td class="col-xs-5 bold-span">' + convertedViewTime + ':' + splitSingleTime + ' '
                         + singleViewStamp + '</td>';
                  }else{
                       row += '<td class="col-xs-5">' + convertedViewTime + ':' + splitSingleTime + ' '
                       + singleViewStamp + '</td>';
                  }
             row = self.showSlots(requisitionSchedulesSlot,row);
            //for MET calendar type
            if(MODEL.CalendarType==="2"){
                row += '<td class="col-xs-1">'+requisitionSchedulesSlot.storeNumber+'</td>';
            }
            self.validatePrintBtn();
        }
       return row;
    };
    /*
     * Method to show the slot based on
     * requisitionScheduleStatusCode
     */
    this.showSlots = function(scheduleSlot,scheduleRow){
        if(scheduleSlot && scheduleSlot.requisitionScheduleStatusCode){
            //for status code 3 set candidate name
            if(scheduleSlot.requisitionScheduleStatusCode===3){
                if(scheduleSlot.candidateName!==undefined){
                    scheduleRow += '<td class="col-xs-6 candidateBackground">'+scheduleSlot.candidateName+'</td>';
                }
                //if candidatename is not avail then scheduled text has shown
                else{
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
      * Method to check the scheduled interviews
      * for Calendar Day Detail
      *
      */
	 this.checkForScheduledInterviews = function() {
		var printRequisitionSchedule = [];
		if (MODEL.CalendarDayDetailList.length === 0) {
			return false;
		} else {
			var printScheduleList = MODEL.CalendarDayDetailList.requisitionScheduleSlotDtl;
			for ( var i = 0; i < printScheduleList.length; i++) {
				if (printScheduleList[i]
						&& printScheduleList[i].requisitionSchedules) {
					printRequisitionSchedule = printScheduleList[i].requisitionSchedules[0].requisitionSchedule;
					if (printRequisitionSchedule.length > 1) {
						// returns false when there are no details for a day
						// else return true
						for ( var slotCount = 0; slotCount < printRequisitionSchedule.length; slotCount++) {
							if (Number(printRequisitionSchedule[slotCount].requisitionScheduleStatusCode) === 3) {
								return true;
							}
						}
					} else {
						if (Number(printRequisitionSchedule.requisitionScheduleStatusCode) === 3) {
							return true;
						}

					}
				}
			}
		}
	};
    /*
	 * Method: for checking whether or not the printButton should be enabled
	 */
    this.validatePrintBtn= function()
    {
        if(($('input:radio[name=inlineRadioOptions]:checked').val() === "View") && (CONSTANTS.SELECTEDDATE !== null))
        {
            // Print enabled when the method retruns true and flag true
            if (self.checkForScheduledInterviews())
            {
                $("#printView").attr("title","Click to view a printable version of "+ UTILITY.getMonthFormatted(CONSTANTS.SELECTEDDATE)+".");
                $("#printView").attr('disabled',false);
                $("#printView").removeClass('greyPrintBtn').addClass('submit');
            }
            //Print disabled
            else
            {
                $("#printView").attr('disabled',true);
                $("#printView").removeClass('submit').addClass('greyPrintBtn');
                $("#printView").attr("title","There are no interviews scheduled for " + UTILITY.getMonthFormatted(CONSTANTS.SELECTEDDATE)+".");
                $("#printTooltip").attr("title","There are no interviews scheduled for " + UTILITY.getMonthFormatted(CONSTANTS.SELECTEDDATE)+".");
            }
        }
    };
    /*
     * Method: On Submit of print
     **/
    this.printAction = function(){
        //Make sure the print button should enabled
        if(!$("#printView").prop('disabled')){
            //service url with params                
            //It will open a new window with PDF on print success
			if (CONSTANTS.SELECTEDDATE !== "" && CONSTANTS.CALENDARID !== ""
				&& $("#calendarSelect option:selected").text() !== ""
				&& MODEL.StoreNumber !== "") {
            	  var printServiceUrl = CONSTANTS.calendarPrintService+ "?date=" + CONSTANTS.SELECTEDDATE + "&store=" +MODEL.StoreNumber+
                  "&calendarName=" +  $("#calendarSelect option:selected").text() +"&calendarId=" + CONSTANTS.CALENDARID; 
            	  window.open(printServiceUrl,'_blank');
            }           	
            //Failure message on print failure -changes on aug24
            else{
            	self.setAlertDraggableFocus();
                $("#alert .modal-body").addClass("autoHeight");
                $("#alert .modal-body .alertModalBody").html(CONSTANTS.printErrorMessage);
            }
        }
    };
    /*
     * Method: To view the slot time with details
     * for add action.
     * it has scheduled,received and available slots
     * It will add the blocks as required.
     */
    this.addTimeBlock = function(){
        var addSplitTime = "";
        var addTimeResponse = [];
        var row = "";
        var convertedTime="";
        var requisitionSchedulesSlot=[];
        var schedule=[];
        //empty the table
        $('#addDaySlotBody').empty();
        //For met calendar
        if(MODEL.CalendarType==="2"){
             $('#metInterviewLabel').show();
             $('#metInterviewLabel').text("Interview Location");
             $(".addDaySlotContainer").addClass("addMetContainer");
             $('#hiddenLableDiv').addClass("hiddenSpaceDiv");
        }else{
                $('#metInterviewLabel').hide().empty();
                $(".addDaySlotContainer").removeClass("addMetContainer");
                $('#hiddenLableDiv').removeClass("hiddenSpaceDiv");
        }
        if(MODEL.CalendarDayDetailList){
            addTimeResponse = MODEL.CalendarDayDetailList;
            for (var slotCount = 0; slotCount < addTimeResponse.requisitionScheduleSlotDtl.length; slotCount++) {
                schedule = addTimeResponse.requisitionScheduleSlotDtl[slotCount];
                addSplitTime = schedule.slotTime.split(":");
                var BeforeConvertedTime = schedule.slotTime.split(":");
                var hiddenTime = BeforeConvertedTime[0]+BeforeConvertedTime[1];
                var ampm = addSplitTime[0] < 12 ? "am" : "pm";
                convertedTime = addSplitTime[0] > 12 ? Number(addSplitTime[0]) - 12
                   : (addSplitTime[0] !== "10" ? addSplitTime[0].replace("0", ""): addSplitTime[0]);
                CONSTANTS.SHOWTIME = convertedTime + addSplitTime[1];
                row = '<tr>';
                //check response having requisitionSchedules (ie. scheduled slot)
                if (schedule && schedule.requisitionSchedules) {
                    requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                    if (requisitionSchedulesSlot.length > 1) {
                        row = self.addMultipleSlot(requisitionSchedulesSlot,schedule,convertedTime,addSplitTime[1],ampm,row,hiddenTime);
                    }
                    else if(requisitionSchedulesSlot) {
                        row = self.addSingleSlot(requisitionSchedulesSlot,schedule,convertedTime,addSplitTime[1],ampm,row,hiddenTime);
                    }
                    row += '</tr>';
                }
                else {
                    row +=  addSplitTime[1]==="00"?
                        '<td class="col-xs-3 bold-span">' + convertedTime + ':' + addSplitTime[1] + ' ' + ampm+ '</td>':
                        '<td class="col-xs-3">' + convertedTime + ':' + addSplitTime[1] + ' ' + ampm+ '</td>';
                    row += '<td class="col-xs-5"></td>';
                    row += '<td class="col-xs-1"><input type="checkbox" title="Select to Add Availability to Time Slot" id='+hiddenTime+' name="chkSlot" value="addSlot'
                            + CONSTANTS.SHOWTIME + '"><input type="hidden" value='+schedule.slotTime+' id="hiddenTime'+hiddenTime+'"></label></td>';
                    row += '<td class="col-xs-1"><select class="form-control small-Select addTimeSelect" data-size="5" id="recurringWeeks'
                            +hiddenTime+ '" >';
                    for (var recurWeeks = 0; recurWeeks < MODEL.RecurringWeeksCollection.length; recurWeeks++) {
                          row +='<option value="recurWeeks'
                                + MODEL.RecurringWeeksCollection[recurWeeks].value
                                + '">'
                                + MODEL.RecurringWeeksCollection[recurWeeks].name
                                + '</option>';
                    }
                        row += '</select></td>';
                    row += '<td class="col-xs-1"><select class="form-control small-Select addInterviewSelect" data-size="5" id="concurrentinterviews'
                            +hiddenTime+ '">';
                            for (var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
                                 row +='<option value="interview'
                                        + MODEL.ConcurrentInterviewCollection[interview].value
                                        + '">'
                                        + MODEL.ConcurrentInterviewCollection[interview].name
                                        + '</option>';
                            }
                    row += '</select></td>';
                    row += MODEL.CalendarType==="2"?'<td class="col-xs-1"><input type="text" id=strNum'+hiddenTime+' class="storeNumText" maxlength="4" value=""/></td>':"";
                    row += '</tr>';
            }
            $('#addDaySlotBody').append(row);
            //set tooltip
            $("#recurringWeeks" + hiddenTime + "").attr("title",CONSTANTS.RECURRING_WEEKS_TOOLTIP);
            $("#concurrentinterviews" + hiddenTime + "").attr("title",CONSTANTS.CONCURRENT_INTERVIEWS_TOOLTIP);
            }
            //Bind the data and set the dynamic height and position to options
            $(".addTimeSelect").selectBoxIt();
            $(".addTimeSelect").bind({
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
            if ($(".addTimeSelect").data("selectBox-selectBoxIt")) {
                $(".addTimeSelect").data("selectBox-selectBoxIt").refresh();
            }
            //Bind the data and set the dynamic height and position to options
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
        //select all method
        self.addCheckAll();
        }
        $(".storeNumText").off('paste');
        $(".storeNumText").on('paste', function(e) {
            setTimeout(function(){
                var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
            },100);
        });
        $(".storeNumText").keypress(function(e) {
            var temp = String.fromCharCode(e.which);
            if (!(/[0-9]/.test(temp))) {
                return false;
            }
        });
        $("#addDaySlotBody tr>td:nth-child(1),#addDaySlotBody tr>td:nth-child(2)").addClass("selectable SelectIE");
        $("#addDaySlotBody tr>td:nth-child(1)::selection,#addDaySlotBody tr>td:nth-child(2)::selection").addClass("tableBodyBackground");
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method to show the multiple slot for add block
     * it has scheduled slots for a particular time.
     * The slots will have one or more than also.
     */
    this.addMultipleSlot = function(requisitionSchedulesSlot,schedule,convertedMultipleTime,addMultipleTime,addStamp,row,multipleHiddenTime){
        var multipleAdddDate="";
        for (var i = 0; i < requisitionSchedulesSlot.length; i++) {
            multipleAdddDate = schedule.requisitionSchedules[0].requisitionSchedule[i].beginTimestamp
                    .substring(11, 16);
            var storeNumber = schedule.requisitionSchedules[0].requisitionSchedule[i].storeNumber;
            //compare the scheduled slot time
            if (multipleAdddDate === schedule.slotTime) {
                if (i === 0) {
                     row += addMultipleTime === "00" ? '<td class="col-xs-3 bold-span">'
                            + convertedMultipleTime
                            + ':'
                            + addMultipleTime
                            + ' ' + addStamp + '</td>'
                            : '<td class="col-xs-3">' + convertedMultipleTime
                                    + ':' + addMultipleTime + ' ' + addStamp
                                    + '</td>';
                    row=self.showSlots(requisitionSchedulesSlot[i], row);
                    row += '<td class="col-xs-1"><input type="checkbox" title="Select to Add Availability to Time Slot" name="chkSlot" id='
                            + multipleHiddenTime
                            + ' value="addSlot'
                            + CONSTANTS.SHOWTIME
                            + '"><input type="hidden" value='
                            + schedule.slotTime
                            + ' id="hiddenTime'
                            + multipleHiddenTime + '"></label></td>';
                    row += '<td class="col-xs-1"><select class="form-control small-Select addTimeSelect" data-size="5" id="recurringWeeks'
                            + multipleHiddenTime + '" >';
                        for (var recurWeeks = 0; recurWeeks < MODEL.RecurringWeeksCollection.length; recurWeeks++) {
                            row +='<option value="recurWeeks'
                                  + MODEL.RecurringWeeksCollection[recurWeeks].value
                                  + '">'
                                  + MODEL.RecurringWeeksCollection[recurWeeks].name
                                  + '</option>';
                        }
                    row += '</select></td>';
                    row += '<td class="col-xs-1"><select class="form-control small-Select addInterviewSelect" data-size="5" id="concurrentinterviews'
                            + multipleHiddenTime + '">';
                    for (var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
                        row +='<option value="interview'
                               + MODEL.ConcurrentInterviewCollection[interview].value
                               + '">'
                               + MODEL.ConcurrentInterviewCollection[interview].name
                               + '</option>';
                   }
                    row += '</select></td>';
                    // if MET calendar type input box should display to get
                    // store number
                    row += MODEL.CalendarType === "2"?'<td class="col-xs-1"><input type="text" maxlength="4" id=strNum'
                                + multipleHiddenTime
                                + ' class="storeNumText" value='
                                + storeNumber
                                + '></td>':"";
                }
                else {
                    row += '</tr>';
                    row += '<tr><td class="col-xs-3"></td>';
                    row=self.showSlots(requisitionSchedulesSlot[i], row);
                    row += '<td class="col-xs-1"></td>';
                    row += '<td class="col-xs-1">';
                    row += '<td class="col-xs-1">';
                    row += MODEL.CalendarType === "2" ? '<td class="col-xs-1"><input type="text" maxlength="4" id=strNum'
                    + multipleHiddenTime
                    + ' class="storeNumText" value='
                    + storeNumber + '></td>'
                    : "";
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
    this.addSingleSlot = function(requisitionSchedulesSlot,schedule,convertedSingleTime,addSingleSplitTime,timeStamp,row,singleHiddenTime){
        var singleAddDate="";
        singleAddDate = requisitionSchedulesSlot.beginTimestamp.substring(11, 16);
        if (singleAddDate === schedule.slotTime) {
            row += addSingleSplitTime==="00"?
                '<td class="col-xs-3 bold-span">' + convertedSingleTime + ':' + addSingleSplitTime + ' ' + timeStamp+ '</td>':
                '<td class="col-xs-3">' + convertedSingleTime + ':' + addSingleSplitTime + ' ' + timeStamp+ '</td>';
            row=self.showSlots(requisitionSchedulesSlot, row);
            row += '<td class="col-xs-1"><input type="checkbox" title="Select to Add Availability to Time Slot" name="chkSlot" id='+singleHiddenTime+' value="addSlot'
                + CONSTANTS.SHOWTIME + '"><input type="hidden" value='+schedule.slotTime+' id="hiddenTime'+singleHiddenTime+'"></label></td>';
        row += '<td class="col-xs-1"><select class="form-control small-Select addTimeSelect" data-size="5" id="recurringWeeks'
                +singleHiddenTime+ '" >';
            for (var recurWeeks = 0; recurWeeks < MODEL.RecurringWeeksCollection.length; recurWeeks++) {
                row +='<option value="recurWeeks'
                  + MODEL.RecurringWeeksCollection[recurWeeks].value
                  + '">'
                  + MODEL.RecurringWeeksCollection[recurWeeks].name
                  + '</option>';
            }
        row += '</select></td>';
        row += '<td class="col-xs-1"><select class="form-control small-Select addInterviewSelect" data-size="5" id="concurrentinterviews'
                +singleHiddenTime+ '">';
        for (var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
            row +='<option value="interview'
                   + MODEL.ConcurrentInterviewCollection[interview].value
                   + '">'
                   + MODEL.ConcurrentInterviewCollection[interview].name
                   + '</option>';
       }
        row += '</select></td>';
        //For met type calendar
        if(MODEL.CalendarType==="2"){
                row += '<td class="col-xs-1"><input type="text" id=strNum'+singleHiddenTime+' maxlength="4" class="storeNumText" value='+requisitionSchedulesSlot.storeNumber+'></td>';
            }
        }
        return row;
    };
    /*
     * Method to check and uncheck the add option checkboxes
     * on check of select All option
     */
    this.addCheckAll = function(){
          $('input:checkbox[name=addAllCheck]').click(function(){
              if($("input:checkbox[name=addAllCheck]").prop('checked')){
                  //check all
                   $("input:checkbox[name=chkSlot]").prop('checked',true);
              }else{
                  //uncheck all
                  $("input:checkbox[name=chkSlot]").prop('checked',false);
              }
          });
           $("input:checkbox[name=chkSlot]").click(function(){
               $("input:checkbox[name=addAllCheck]").prop('checked',false);
           });
    };

    /*
     * Method: To show details in add block of time popup
     * */
    this.loadBlockTime = function() {
        MODEL.AddTimeBlock = true;
        self.setAddblockTime();
        var beginTimeSelect = $("#addBlockBeginTime");
        var endTimeSelect = $("#addBlockEndTime");
        var recurringBlock = $("#addBlcokRecurring");
        var interviewBlock = $("#addBlockInterview");
        var beginTimeOption="";
        var endTimeOption="";
        var recurDayOption="";
        var interviewOption="";
        $("#BlcokTimeDetail").empty();
        $("#recurLabelAddBlock").attr("title",CONSTANTS.RECURRING_WEEKS_TOOLTIP);
        $("#interviewLabelAddBlock").attr("title",CONSTANTS.CONCURRENT_INTERVIEWS_TOOLTIP);
        $(endTimeSelect).empty();
        $(interviewBlock).empty();
        $(beginTimeSelect).empty();
        $(recurringBlock).empty();
        //set label for popup
        $("#BlcokTimeDetail").html(" "+
                $("#calendarSelect option:selected").text() + " for "
                        + UTILITY.getMonthFormatted(CONSTANTS.SELECTEDDATE));
        //Add time collection to begin and end time
        for ( var time = 0; time < MODEL.TimeCollection.length; time++) {
            beginTimeOption = '<option value="'
                    + MODEL.TimeCollection[time].value
                    + '">'
                    + MODEL.TimeCollection[time].name
                    + '</option>';
            $(beginTimeSelect).append(beginTimeOption);
            endTimeOption = '<option value="'
                + MODEL.TimeCollection[time].value
                + '">'
                + MODEL.TimeCollection[time].name
                + '</option>';
            $(endTimeSelect).append(endTimeOption);
        }
        self.appendSelectControl("addBlockBeginTime");
        self.appendSelectControl("addBlockEndTime");
        //add recurring weeks
        for ( var recurWeeks = 0; recurWeeks < MODEL.RecurringWeeksCollection.length; recurWeeks++) {
            recurDayOption = '<option value="'
                    + MODEL.RecurringWeeksCollection[recurWeeks].value
                    + '">'
                    + MODEL.RecurringWeeksCollection[recurWeeks].name
                    + '</option>';
            $(recurringBlock).append(recurDayOption);
        }
        self.appendSelectControl("addBlcokRecurring");
        //add interview count
        for ( var interview = 0; interview < MODEL.ConcurrentInterviewCollection.length; interview++) {
            interviewOption = '<option value="'
                    + MODEL.ConcurrentInterviewCollection[interview].value
                    + '">'
                    + MODEL.ConcurrentInterviewCollection[interview].name
                    + '</option>';
            $(interviewBlock).append(interviewOption);
        }
        self.appendSelectControl("addBlockInterview");
        if(MODEL.CalendarType==="2"){
            $("#interviewLabel").attr("title",CONSTANTS.MET_INTERVIEW_TOOLTIP);
            $("#interviewLoc").val("");
            $("#interviewLabel").show();
            $("#interviewLoc").focus();
            $("#interviewLocDiv").show();
            $("#interviewLoc").off('paste');
            $("#interviewLoc").on('paste', function(e) {
                setTimeout(function(){
                    var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                    $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
                },100);
            });
            $("#interviewLoc").keypress(function(e) {
                var temp = String.fromCharCode(e.which);
                if (!(/[0-9]/.test(temp))) {
                    return false;
                }
            });
        }else{
            $("#interviewLabel").hide();
            $("#interviewLocDiv").hide();
        }
    };
    /*
     * Method for jquery plugin dropdown.
     * Default dropdown "Id" is a parameter
     * to pass and append
     */
    this.appendSelectControl = function(selectId){
         $("#"+selectId).data("selectBox-selectBoxIt").refresh();
         if ($("#"+selectId).data("selectBox-selectBoxIt")) {
             $("#"+selectId).data("selectBox-selectBoxIt").refresh();
         }
    };
    /*
     * Method: To show the warning message for
     *  acknowledgement
     * */
    this.submitAddConfirmBlock = function(){
        var beginBlockTime="";
        var endBlockTime="";
        var numOfInterviews="";
        var numOfRecurring="";
        var beginTimeIndex=0;
        var endTimeIndex=0;
        var addedSlotsPerDay=0;
        var addedSlots=0;
        var addBlockMessage="";
        $("#alert .modal-content").removeClass("slotExceedWidth");
        $("#alert .modal-body").removeClass("slotExceedBodyHeight");
        $("#alert .modal-body .alertModalBody").removeClass("slotExceedModalBody");
        $("#showSlotPopup .modal-body #actionMessage").empty();
        beginBlockTime = $("#addBlockBeginTime option:selected").val();
        endBlockTime = $("#addBlockEndTime option:selected").val();
        numOfRecurring = $("#addBlcokRecurring option:selected").text();
        numOfInterviews = $("#addBlockInterview option:selected").text();
        beginTimeIndex = $("#addBlockBeginTime option:selected").index();
        endTimeIndex = $("#addBlockEndTime option:selected").index();    
        //Compare the begin and end time ,equal time slot won't get add
       if(beginBlockTime===endBlockTime){
            self.setAlertDraggableFocus();
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").html(CONSTANTS.CheckEqualTime);
            return;
            //End range should not be less than start range
        }else if(endBlockTime<beginBlockTime){
            self.setAlertDraggableFocus();
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").html(CONSTANTS.CheckTimeRange);
            return;
        }
        else if(MODEL.CalendarType==="2"){
           var $interviewLocValue = $.trim($("#interviewLoc").val());
            if ($interviewLocValue === "" || $interviewLocValue.length < 4) {
                self.setAlertDraggableFocus();
                $("#alert .modal-body").addClass('autoHeight');
                $("#alert .modal-content").addClass("metStoreContentWidth");
                $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
                $("#alert .modal-body .alertModalBody").html(CONSTANTS.MetInputValidation);
                return;
            } 
        } 
       
       //Get slots per day
       addedSlotsPerDay = Number(endTimeIndex - beginTimeIndex)
       * Number(numOfInterviews);
       //Get Total slots for week
       addedSlots = Number(endTimeIndex - beginTimeIndex)
       * Number(numOfInterviews)* Number(numOfRecurring);
            //Slot Exceed condition
        
         if (addedSlots > 680)
            {
                self.setAlertDraggableFocus();
                $("#showSlotPopup").modal('hide');
                $('#alert #alertModalLabel').text("Add Slot Number Exceeded");
                $("#alert .modal-content").addClass("slotExceedWidth");
                $("#alert .modal-body").addClass("slotExceedBodyHeight");
                $("#alert .modal-body .alertModalBody").addClass("slotExceedModalBody");
                $("#alert .modal-body .alertModalBody").html(CONSTANTS.SlotExceed);
            }
            //Slots per day and total slots is equal then show warning message to acknowledge
            else if (addedSlotsPerDay === addedSlots)
            {
                addBlockMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add "
                    + addedSlots
                    + " thirty minute interview slot(S) for "
                    + UTILITY.stringFormattedDate(CONSTANTS.SELECTEDDATE)+".</div> <div class='cols-xs-12 cols-xs-offset-1'> Are You Sure?</div>";
                self.setWarningDraggable();
                $("#showSlotPopup .modal-body #actionMessage").html(addBlockMessage);
                $("#showSlotPopup").modal('show');
                $("#showSlotPopup .modal-body").removeClass('deleteActionMessage').addClass('actionResponseMessage');
                $("#showSlotPopup .modal-content").removeClass('deletePopupWidth');
            }
            //slots per day is not equal
            else{
                addBlockMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add "
                        + addedSlotsPerDay
                        + " thirty minute interview slot(s) for "
                        + UTILITY.stringFormattedDate(CONSTANTS.SELECTEDDATE)
                        + ";</div> <div class='cols-xs-12 cols-xs-offset-1'>a total of "
                        + addedSlots
                        + " slot(s) will be added based on recurring weeks selection(s).</div>" +
                                "<div class='cols-xs-12 cols-xs-offset-1'>Are You Sure?</div>";
                self.setWarningDraggable();
                $("#showSlotPopup .modal-body #actionMessage").html(addBlockMessage);
                $("#showSlotPopup").modal('show');
                $("#showSlotPopup .modal-body").removeClass('deleteActionMessage').addClass('actionResponseMessage');
                $("#showSlotPopup .modal-content").removeClass('deletePopupWidth');
            }
        
    };
    /*
     * Method: To confirm the slot
     * */
    this.confirmSlot = function(){
        var actionValue = $('input:radio[name=inlineRadioOptions]:checked')
        .val();
        var checkSlotArray = $('input:checkbox[name=chkSlot]:checked').length;
        var deleteSlotArray = $('input:checkbox[name=deleteSlot]:checked').length;
        var printPacketSlotArray = $('input:checkbox[name=printSlot]:checked').length;
        var slotMessage="";
        $("#alert .modal-content").removeClass("slotExceedWidth");
        $("#alert .modal-body").removeClass("slotExceedBodyHeight");
        $("#alert .modal-body .alertModalBody").removeClass("slotExceedModalBody");
        //Validate the view action
        if(actionValue==="View"){
            self.setAlertDraggableFocus();
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").text(CONSTANTS.SUBMIT_DAY_VIEW);
            $("#alert .modal-content").addClass('manualWidth');
        }
        //Validate the checkbox for add,delete and printpacket actions
        else if (actionValue==="Add" && checkSlotArray === 0 ) {
            self.setAlertDraggableFocus();
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").text(
                    CONSTANTS.CheckAddAvailability);
        }else if(actionValue==="Delete" && deleteSlotArray ===0){
            self.setAlertDraggableFocus();
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").text(
                    CONSTANTS.CheckDeleteAvailability);
            $("#alert").modal('show');
        }else if(actionValue==="PrintPackets" && printPacketSlotArray ===0){
            self.setAlertDraggableFocus();
            $("#alert .modal-body").addClass('autoHeight');
            $("#alert .modal-body .alertModalBody").text(
                    CONSTANTS.CheckPrintPacketAvailability);
        }
        //Confirm message for selected action slots
        //greater than 0
        else if (actionValue === "Add" && checkSlotArray>0){
                self.confirmAddAction();
         }
        else if(actionValue === "Delete" && deleteSlotArray>0){
           self.setWarningDraggable();
            $("#showSlotPopup .modal-body #actionMessage").empty();
            $("#showSlotPopup .modal-body #actionMessage").html(
                    CONSTANTS.DeleteValidate);
            $("#showSlotPopup .modal-body #actionMessage").removeClass('actionResponseMessage');
            $("#showSlotPopup .modal-body #actionMessage").addClass("deleteActionMessage");
            $("#showSlotPopup .modal-content").addClass('deletePopupWidth');
        }else if(actionValue === "PrintPackets" && printPacketSlotArray>0){
            self.setWarningDraggable();
            $("#showSlotPopup .modal-body #actionMessage").empty();
            slotMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to create Interview Packets for "+printPacketSlotArray+ " Candidates.</div>" +
                    "<div class='cols-xs-12 cols-xs-offset-1'>Are You Sure?</div>";
            $("#showSlotPopup .modal-body #actionMessage").html(slotMessage);
            $("#showSlotPopup .modal-content").removeClass('deletePopupWidth');
            $("#showSlotPopup .modal-body").removeClass('deleteActionMessage').addClass('actionResponseMessage');
        }
     };
    /*
     * Method: To confirm the add action.
     * It will validate the slot and show the warning
     * notification based on condition
     */
    this.confirmAddAction = function(){
        var addedSlotsPerDay = 0;
        var addedSlots=0;
        var totalAddedSlots=0;
        var totalAddedSlotsPerDay=0;
        var addSelected=[];
        var numOfInterviews=0;
        var numOfRecurring=0;
        var slotMessage="";
        //Empty the popup message
        $("#showSlotPopup .modal-body #actionMessage").empty();
        //Get an id of checkboxes
         $('input:checkbox[name=chkSlot]:checked').each(function() {
             addSelected.push($(this).attr('id'));
         });
         //Get the initial value if lenth is 1
         if(addSelected.length===1){
             numOfInterviews=$("#concurrentinterviews"+addSelected[0]+" option:selected").text();
             numOfRecurring=$("#recurringWeeks"+addSelected[0]+" option:selected").text();
             addedSlotsPerDay = Number(numOfInterviews);
             addedSlots = Number(numOfInterviews) * Number(numOfRecurring);
             totalAddedSlots += addedSlots;
             totalAddedSlotsPerDay += addedSlotsPerDay;
             //For Met calendar Type
             self.validateMetStoreNum(addSelected[0]);
         }else if(addSelected.length>1){
             for(var i=0;i<addSelected.length;i++){
                numOfInterviews=Number($("#concurrentinterviews"+addSelected[i]+" option:selected").text());
                numOfRecurring=Number($("#recurringWeeks"+addSelected[i]+" option:selected").text());
                addedSlotsPerDay = Number(numOfInterviews);
                addedSlots = Number(numOfInterviews) * Number(numOfRecurring);
                totalAddedSlots += addedSlots;
                totalAddedSlotsPerDay += addedSlotsPerDay;
               //For Met calendar Type
                self.validateMetStoreNum(addSelected[i]);
             }
         }
         //Check Exceed slot.Range is 680
         if(!MODEL.IsMet && totalAddedSlots > 680){
             self.setAlertDraggableFocus();
             $("#showSlotPopup").modal('hide');
             $('#alert #alertModalLabel').text("Add Slot Number Exceeded");
             $("#alert .modal-content").addClass("slotExceedWidth");
             $("#alert .modal-body").addClass("slotExceedBodyHeight");
             $("#alert .modal-body .alertModalBody").addClass("slotExceedModalBody");
             $("#alert .modal-body .alertModalBody").html(CONSTANTS.SlotExceed);
          }
         //Check the total slot is equal
         else if(!MODEL.IsMet && totalAddedSlotsPerDay === totalAddedSlots){
             self.setWarningDraggable();
             slotMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add "
             + totalAddedSlots + " thirty minute interview slot(s) for "
             + UTILITY.stringFormattedDate(CONSTANTS.SELECTEDDATE)+".</div><div class='cols-xs-12 cols-xs-offset-1'>Are You Sure?</div>";
             $("#showSlotPopup .modal-body #actionMessage").html(slotMessage);
             $("#showSlotPopup .modal-content").removeClass('deletePopupWidth');
             $("#showSlotPopup .modal-body #actionMessage").removeClass('deleteActionMessage').addClass('actionResponseMessage');
         }else if(!MODEL.IsMet){
             self.setWarningDraggable();
             slotMessage = "<div class='cols-xs-12 cols-xs-offset-1'>You have selected to add "
                 + totalAddedSlotsPerDay
                 + " thirty minute interview slot(s) for "
                 + UTILITY.stringFormattedDate(CONSTANTS.SELECTEDDATE) + ";</div><div class='cols-xs-12 cols-xs-offset-1'> a total of "
                 + totalAddedSlots
                 + " slot(s) will be added based on recurring weeks selection(s).</div> <div class='cols-xs-12 cols-xs-offset-1'>Are You Sure?</div>";
             $("#showSlotPopup .modal-body #actionMessage").html(slotMessage);
             $("#showSlotPopup .modal-content").removeClass('deletePopupWidth');
             $("#showSlotPopup .modal-body #actionMessage").removeClass('deleteActionMessage').addClass('actionResponseMessage');
         }
    };
    /*
     * Method for If MET calendar make sure that a four
     *  digit store number has been entered.
     */
    this.validateMetStoreNum = function(selectedStoreNumber){
        //If MET calendar make sure that a four digit store number has been entered.
        if(MODEL.CalendarType==="2" && selectedStoreNumber!==undefined){
            //make sure that a four digit store number has been entered.
            if($("#strNum"+selectedStoreNumber+"").val().length<4){
                MODEL.IsMet = true;
                self.setAlertDraggableFocus();
                $("#alert .modal-content").addClass("metStoreContentWidth");
                $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
                $("#alert .modal-body").addClass('autoHeight');
                $("#alert .modal-body .alertModalBody").text(CONSTANTS.MetInputValidation);
            }else{
                $('#alert').modal('hide');
                MODEL.IsMet = false;
            }
        }else{
            MODEL.IsMet = false;
        }
    };
    /*
     * Method: To submit the Add action and
     * service called up
     * service returns the requested number of
     * request and slots
     */
    this.submitAddAction = function() {
        var beginTime="";
        var endTime="";
        var numOfInterviews="";
        var numOfRecurring="";
        var mycalendar = {};
        var myRequest = {};
        var selectedCheckId=[];
        var addAvailabilityJSON = {};
        myRequest.calendarRequest = {};
        myRequest.calendarRequest.availabilityBlocks = {};
        myRequest.calendarRequest.availabilityBlocks.availabilityBlock = [];
        var storeNumber="";
        var serviceStoreNum="";
        //Submit add block of time service
        if(MODEL.AddTimeBlock){
            self.submitAddTimeBlock();
        }
        else{
            //Get an id of add action checked
            $('input:checkbox[name=chkSlot]:checked').each(function(){
                selectedCheckId.push($(this).attr('id'));
            });
            //Check If length is 1
            if(selectedCheckId.length===1){
                numOfRecurring=$("#recurringWeeks"+selectedCheckId[0]+" option:selected").text();
                numOfInterviews=$("#concurrentinterviews"+selectedCheckId[0]+" option:selected").text();
                beginTime = UTILITY.formatTimeStamp(CONSTANTS.SELECTEDDATE+"-"+$("#hiddenTime"+selectedCheckId[0]).val());
                endTime = UTILITY.addThirtyMinutesToStamp(CONSTANTS.SELECTEDDATE+"-"+$("#hiddenTime"+selectedCheckId[0]).val());
                storeNumber = $("#strNum"+selectedCheckId[0]+"").val();
                //For Met calendar Type get store number
                serviceStoreNum = storeNumber===undefined?MODEL.StoreNumber:storeNumber;
                mycalendar = {
                        "calendarId" : CONSTANTS.CALENDARID,
                        "beginTimestamp" :beginTime,
                        "endTimestamp" : endTime,
                        "storeNumber" : serviceStoreNum,
                        "numberOfInterviewers" : numOfInterviews,
                        "numberOfWeeksRecurring" :numOfRecurring
                    };
                //push the params to availabilityBlock array
                myRequest.calendarRequest.availabilityBlocks.availabilityBlock.push(mycalendar);
              //Stringify to JSON from object
                addAvailabilityJSON[CONSTANTS.AttributeType] = JSON.stringify(myRequest);
            }else{
                 //Checked Length is greater than 1
                for(var j=0;j<selectedCheckId.length;j++){
                    numOfRecurring=$("#recurringWeeks"+selectedCheckId[j]+" option:selected").text();
                    numOfInterviews=$("#concurrentinterviews"+selectedCheckId[j]+" option:selected").text();
                    beginTime = UTILITY.formatTimeStamp(CONSTANTS.SELECTEDDATE+"-"+$("#hiddenTime"+selectedCheckId[j]).val());
                    endTime = UTILITY.addThirtyMinutesToStamp(CONSTANTS.SELECTEDDATE+"-"+$("#hiddenTime"+selectedCheckId[j]).val());
                    storeNumber = $("#strNum"+selectedCheckId[j]+"").val();
                  //For Met calendar Type get store number
                    serviceStoreNum = storeNumber===undefined?MODEL.StoreNumber:storeNumber;
                   mycalendar = {
                            "calendarId" : CONSTANTS.CALENDARID,
                            "beginTimestamp" :beginTime,
                            "endTimestamp" : endTime,
                            "storeNumber" : serviceStoreNum,
                            "numberOfInterviewers" : numOfInterviews,
                            "numberOfWeeksRecurring" :numOfRecurring
                        };
                  //push the params to availabilityBlock array
                    myRequest.calendarRequest.availabilityBlocks.availabilityBlock
                    .push(mycalendar);
                    //Stringify to JSON from object
                    addAvailabilityJSON[CONSTANTS.AttributeType] = JSON.stringify(myRequest);
                    }
            }
            MODEL.AddAvailabilitySlots = addAvailabilityJSON;
            var callbackFunction = $.Callbacks('once');
            //callback method
            callbackFunction.add(this.addTimeSlots);
            //Service call for add availability service
            var addAvailabilityUrl = CONSTANTS.AddAvailabilityService;
            RSASERVICES.addAvailabilityPost(callbackFunction,addAvailabilityUrl,
                    MODEL.AddAvailabilitySlots);
        }
    };
    /*
     * Method: To Submit service for add block of time
     * */
    this.submitAddTimeBlock = function(){
        var timeBlock = {};
        var timeRequest = {};
        var timeJSON = {};
        timeRequest.calendarRequest = {};
        timeRequest.calendarRequest.availabilityBlocks = {};
        timeRequest.calendarRequest.availabilityBlocks.availabilityBlock = [];
        var beginBlockTime="";
        var endBlockTime="";
        var interviews="";
        var recurring="";
        var serviceStoreNum="";
        //get the value for begin time,end time and recurring,interview
        beginBlockTime = $("#addBlockBeginTime option:selected").val();
        endBlockTime = $("#addBlockEndTime option:selected").val();
        recurring = $("#addBlcokRecurring option:selected").text();
        interviews = $("#addBlockInterview option:selected").text();
        var interviewLocation = $("#interviewLoc").val();
        //For Met calendar Type get store number
        if(MODEL.CalendarType==="2" && interviewLocation!=="" && interviewLocation!==undefined){
            serviceStoreNum = interviewLocation;
        }else{
            serviceStoreNum =  MODEL.StoreNumber;
        }
        timeBlock = {
                "calendarId" : CONSTANTS.CALENDARID,
                "beginTimestamp" : CONSTANTS.SELECTEDDATE+" "+beginBlockTime,
                "endTimestamp" :  CONSTANTS.SELECTEDDATE+" "+endBlockTime,
                "storeNumber" : serviceStoreNum,
                "numberOfInterviewers" : interviews,
                "numberOfWeeksRecurring" :recurring
            };
        //push the params to availabilityBlock array
        timeRequest.calendarRequest.availabilityBlocks.availabilityBlock.push(timeBlock);
        //Stringify to JSON from object
        timeJSON[CONSTANTS.AttributeType] = JSON.stringify(timeRequest);
        MODEL.AddAvailabilitySlots = timeJSON;
        var submitCallbackFunction = $.Callbacks('once');
        //callback method
        submitCallbackFunction.add(this.getAddBlockTimeResponse);
        //Service call for add availability service
        var addTimeBlockAvailabilityUrl = CONSTANTS.AddAvailabilityService;
        RSASERVICES.addAvailabilityPost(submitCallbackFunction,addTimeBlockAvailabilityUrl,
                MODEL.AddAvailabilitySlots);
    };
    /*
     * Method: (Callback method)
     * To Get response from service
     * */
    this.getAddBlockTimeResponse = function(response){
        MODEL.AddBlockResponse = response.calendarResponse;
        var resp = MODEL.AddBlockResponse;
        //check the success response will have numSlotsAffected
        if(resp && resp.numSlotsRequested>0){
            var slotsRequested=resp.numSlotsRequested;
            var slotsAffected=resp.numSlotsAffected;
            self.setAlertDraggableFocus();
            $("#alertModalLabel").empty();
            $("#alert .modal-body .alertModalBody").empty();
            //check the slots is equal with affected and requested
             if (slotsAffected === slotsRequested)
             {
                 $("#alert .modal-body .alertModalBody").html(
                     resp.numSlotsAffected + " thirty minute Slot(s) added");
                  $("#alertModalLabel").text("Slot Addition Confirmation");
                  $("#alert .modal-body").addClass('popupHeight');
             }else{
                 $("#alert .modal-body .alertModalBody").html('<div>'+
                         slotsRequested + " thirty Minute Slot(s) Requested,</div><div>however only " + slotsAffected +  " were added.</div></br>" +
                 "<div>There can only be at most 20 slots per </div><div>thirty minute time slot and 680 slots per</div> day.");
                  $("#alertModalLabel").text("Slot Addition Error");
                  $("#alert .modal-body").addClass('autoHeight');
                  $("#alert .modal-content").addClass("slotContentTop");
             }
             $("#alert .modal-content").addClass('popupWidth');
         }
        //check response has error
         else if(resp && resp.hasOwnProperty("error")){
            if (_.isArray(resp.error)) {
                CONSTANTS.SERVICEERROR = resp.error;
            } else {
                CONSTANTS.SERVICEERROR = [resp.error];
            }
            //check the method returns error description to throw
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.SERVICEERROR);
            self.setAlertDraggableFocus();
            $("#alert .modal-body .alertModalBody").html(returnedErrorMessage);
            $("#alert .modal-body").addClass("autoHeight");
            $("#alert .modal-content").addClass("metStoreContentWidth");
            $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
            $("#alertModalLabel").text("");
       }
    };
    /*
     * Method: To close the add block time popup
     * */
    this.closeBlockTimePopup = function(){
        MODEL.AddTimeBlock = false;
        $('#addBlocksTime').modal('hide');
        var addBlockResp = MODEL.AddBlockResponse;
      //check the success response will have numSlotsAffected
        if (addBlockResp && addBlockResp.numSlotsRequested>0){
            var closeTimeFunction = $.Callbacks('once');
            closeTimeFunction.add(self.getCalendarDetail);
            //service call to get calendar detail
            var calendarDetailUrl = CONSTANTS.CalendarDetailService;
            RSASERVICES.calendarDetailsGet(closeTimeFunction,calendarDetailUrl,
                    CONSTANTS.CALENDARID, CONSTANTS.SELECTEDDATE);
            $(".daySlotContainer").show();
            $('.model-radio input:radio[value="View"]').prop('checked', true);
            $(".addDaySlotContainer").hide();
            $(".deleteDaySlotContainer").hide();
            $(".printPacketSlotContainer").hide();
        }else{
        	 $(".daySlotContainer").show();
             $('.model-radio input:radio[value="View"]').prop('checked', true);
             $(".addDaySlotContainer").hide();
             $(".deleteDaySlotContainer").hide();
             $(".printPacketSlotContainer").hide();
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
        var checkSheduleId=[];
        var scheduleObj = {};
        scheduleObj.requisitionSchedule = [];
        //
        $('input:checkbox[name=deleteSlot]:checked').each(function() {
            checkSchedule.push($(this).val());
            checkSheduleId.push($(this).attr('id'));
        });
        //check the response object
        if (MODEL.CalendarDayDetailList) {
            for ( var j = 0; j <MODEL.CalendarDayDetailList.requisitionScheduleSlotDtl.length; j++) {
                selectedArray = MODEL.CalendarDayDetailList.requisitionScheduleSlotDtl[j];
                if (selectedArray.requisitionSchedules) {
                    //check object and assign it to another object
                     //Assign the array on validating the data is a single array or more than
                    if (UTILITY.IsArray(selectedArray.requisitionSchedules[0].requisitionSchedule)) {
                        selectedSubArray = selectedArray.requisitionSchedules[0].requisitionSchedule;
                    } else {
                        selectedSubArray[0] = selectedArray.requisitionSchedules[0].requisitionSchedule;
                    }
                    //check the length of selected checkbox
                    if (checkSheduleId.length === 1) {
                        //split the value
                        var time = checkSchedule[0].split("-")[0];
                        var slotSequenceNum = checkSchedule[0].split("-")[1];
                        //compare the time slots is equal
                        if (selectedArray.slotTime === time) {
                            for(var block=0;block<selectedSubArray.length;block++){
                                //compare the sequence number is equal
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
                            //push the request into an array
                            scheduleObj.requisitionSchedule.push(request);
                            deleteBlock.calendarRequest.requisitionSchedules[0] = scheduleObj;
                            //form the JSON
                            deleteAvailabilityJSON[CONSTANTS.AttributeType] = JSON
                                    .stringify(deleteBlock);
                        }
                    }
                    //Check the selected delete checkbox is more than 1
                    else if (checkSheduleId.length > 1) {
                        for ( var r = 0; r < checkSchedule.length; r++) {
                            //split the every value
                            var schedTime = checkSchedule[r].split("-")[0];
                            var sequenceNum = checkSchedule[r].split("-")[1];
                            if (selectedArray.slotTime === schedTime) {
                                for ( var m = 0; m < selectedSubArray.length; m++) {
                                    //compare the sequence number
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
                        //push the request and for the json object
                        deleteBlock.calendarRequest.requisitionSchedules[0] = scheduleObj;
                        deleteAvailabilityJSON[CONSTANTS.AttributeType] = JSON
                                .stringify(deleteBlock);
            }}}
        }
        //service call for delete availability
       var callbackFunction = $.Callbacks('once');
        callbackFunction.add(this.deletTimeSlot);
        var removeAvailabilityUrl = CONSTANTS.RemoveAvailabilityService;
        RSASERVICES.removeAvailabilityPost(callbackFunction,
                removeAvailabilityUrl, deleteAvailabilityJSON);
    };
    /*
     * Method:Call back method
     * for getting response from Add action
     * */
    this.addTimeSlots = function(response) {
        ///Check for returned errors
        if(response && response.calendarResponse.hasOwnProperty("error")){
             if (_.isArray(response.calendarResponse.error)) {
                 CONSTANTS.SERVICEERROR = response.calendarResponse.error;
             } else {
                 CONSTANTS.SERVICEERROR = [response.calendarResponse.error];
             }
             //method to check and return the error description
             var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.SERVICEERROR);
             self.setAlertDraggableFocus();
             $(".daySlotContainer").show();
             $('.model-radio input:radio[value="View"]').prop('checked', true);
             $(".addDaySlotContainer").hide();
             $(".deleteDaySlotContainer").hide();
             $("#alert .modal-body .alertModalBody").html(returnedErrorMessage);
             $("#alert .modal-body").addClass("autoHeight");
             $("#alert .modal-content").addClass("metStoreContentWidth");
             $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
             $("#alertModalLabel").text("");
        }
        //check the response will have numSlotsRequested
        else if(response && response.calendarResponse.hasOwnProperty("numSlotsRequested")){
        	   MODEL.AddBlockResponse = response.calendarResponse;
               if(response.calendarResponse.numSlotsRequested>0){
                   var addtimeFunction = $.Callbacks('once');
                   //call back method
                   addtimeFunction.add(self.getCalendarDetail);
                   var calendarDetailUrl = CONSTANTS.CalendarDetailService;
                   RSASERVICES.calendarDetailsGet(addtimeFunction,calendarDetailUrl,
                           CONSTANTS.CALENDARID, CONSTANTS.SELECTEDDATE);
                   $(".daySlotContainer").show();
                   $('.model-radio input:radio[value="View"]').prop('checked', true);
                   $(".addDaySlotContainer").hide();
                   $(".deleteDaySlotContainer").hide();
                       var slotsRequested=response.calendarResponse.numSlotsRequested;
                       var slotsAffected=response.calendarResponse.numSlotsAffected;
                       self.setAlertDraggableFocus();
                   $("#alertModalLabel").empty();
                   $("#alert .modal-body .alertModalBody").empty();
                   //Show user a message that not all slots were added  and slot are equal to request and affect
                    if (slotsAffected === slotsRequested)
                    {
                        $("#alert .modal-body .alertModalBody").html(
                                slotsAffected + " thirty minute Slot(s) added");
                         $("#alertModalLabel").text("Slot Addition Confirmation");
                         $("#alert .modal-body").addClass('popupHeight');
                    }
                  //Show user a message that not all slots were added
                    else{
                        $("#alert .modal-body .alertModalBody").html('<div>'+
                                slotsRequested + " thirty Minute Slot(s) Requested,</div><div>however only " + slotsAffected +  " were added.</div></br>" +
                        "<div>There can only be at most 20 slots per </div><div>thirty minute time slot and 680 slots per</div> day.");
                         $("#alertModalLabel").text("Slot Addition Error");
                         $("#alert .modal-body").addClass('autoHeight');
                         $("#alert .modal-content").addClass("slotContentTop");
                    }
                    $("#alert .modal-content").addClass('popupWidth');
                }
        }
    };
    /*
     * Method:Call back method for getting
     * response from Delete action
     * */
    this.deletTimeSlot = function(response) {
        //check the response
        if(response && response.calendarResponse.hasOwnProperty("error")){
            if (_.isArray(response.calendarResponse.error)) {
                CONSTANTS.SERVICEERROR = response.calendarResponse.error;
            } else {
                CONSTANTS.SERVICEERROR = [response.calendarResponse.error];
            }
            var returnedErrorMessage=MODEL.getErrorMessage(CONSTANTS.SERVICEERROR);
            self.setAlertDraggableFocus();
            $(".daySlotContainer").show();
            $('.model-radio input:radio[value="View"]').prop('checked', true);
            $(".addDaySlotContainer").hide();
            $(".deleteDaySlotContainer").hide();
            $("#alert .modal-content").addClass("metStoreContentWidth");
            $("#alert .modal-body .alertModalBody").addClass("metStoreBodyWidth");
            $("#alert .modal-body .alertModalBody").html(returnedErrorMessage);
            $("#alert .modal-body").addClass("autoHeight");
            $("#alertModalLabel").text("");
       }
      //check the response will have numSlotsRequested
        else if(response && response.calendarResponse && response.calendarResponse.numSlotsAffected>0){
             var detailFunction = $.Callbacks('once');
             //call back method
             detailFunction.add(self.getCalendarDetail);
             var calendarDetailUrl = CONSTANTS.CalendarDetailService;
             RSASERVICES.calendarDetailsGet(detailFunction,calendarDetailUrl,
                     CONSTANTS.CALENDARID, CONSTANTS.SELECTEDDATE);
             $(".daySlotContainer").show();
             $('.model-radio input:radio[value="View"]').prop('checked', true);
             $(".addDaySlotContainer").hide();
             $(".deleteDaySlotContainer").hide();
             self.setAlertDraggableFocus();
             $("#alert .modal-body .alertModalBody").html('<div>'+
             response.calendarResponse.numSlotsRequested+" slot(s) requested for deletion.</div><div>"+
             response.calendarResponse.numSlotsAffected+" thirty minute slot(s) deleted.</div>");
             $("#alert .modal-body").addClass('popupHeight');
             $("#alert .modal-content").addClass('popupWidth');
             $("#alertModalLabel").text("Slot Deletion Confirmation");
             MODEL.DeleteSuccessResponse = response.calendarResponse.numSlotsAffected;
        }
    };
    /*
     * Method: Load the calendar summary details on
     *  success of service
     */
    this.loadSummary = function(){
        MODEL.AddPrintPacket =false;
        $('#dayViewModal').modal({
            backdrop: 'static',
            keyboard: false
        });
        $("#dayViewModal").modal('hide');
        var resp = MODEL.AddBlockResponse;
        //check the response will have numSlotsRequested
        if(MODEL.DeleteSuccessResponse || (resp && resp.numSlotsRequested>0)){
            var callbackFunction = $.Callbacks('once');
            //callback method
            callbackFunction.add(this.getCalendarSummary);
            var calendarSumaryUrl = CONSTANTS.CalendarSummaryService;
            RSASERVICES.calendarSummaryGet(callbackFunction,calendarSumaryUrl, MODEL.SelectedStartDate,
                    CONSTANTS.CALENDARID, MODEL.SelectedEndDate);
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
        var deleteTimeResponse = [];
        var schedule = [];
        var requisitionSchedulesSlot = [];
        var row = "";
        var ampm = "";
        var convertedTime = "";
        $('#deleteSlotBody').empty();
        //set the response value to an array
        if(MODEL.CalendarDayDetailList){
        deleteTimeResponse = MODEL.CalendarDayDetailList;
        MODEL.DeleteSchedule = MODEL.CalendarDayDetailList;
        //Iterate the response
        for (var slotCount = 0; slotCount < deleteTimeResponse.requisitionScheduleSlotDtl.length; slotCount++) {
            schedule = deleteTimeResponse.requisitionScheduleSlotDtl[slotCount];
            deleteSplitTime = schedule.slotTime.split(":");
            ampm = deleteSplitTime[0] < 12 ? "am" : "pm";
            convertedTime = deleteSplitTime[0] > 12 ? Number(deleteSplitTime[0]) - 12
                    : (deleteSplitTime[0] !== "10" ? deleteSplitTime[0].replace("0", "")
                            : deleteSplitTime[0]);
            CONSTANTS.SHOWTIME = convertedTime + deleteSplitTime[1];
            row = '<tr>';
            //check if schedule object having requisitionSchedules
            if (schedule && schedule.requisitionSchedules){
                requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                if (requisitionSchedulesSlot.length > 1) {
                    row = self.deleteMultipleSlot(requisitionSchedulesSlot,convertedTime,ampm,deleteSplitTime[1],schedule,row);
                }
                //Block will have multiple unique time with scheduled slot
                else if(requisitionSchedulesSlot) {
                    row = self.deleteSingleSlot(requisitionSchedulesSlot,convertedTime,ampm,deleteSplitTime[1],schedule,row);
                }
                row += '</tr>';
            }
            //Block will have unique time
            else {
                  row += deleteSplitTime[1] === "00" ? '<td class="col-xs-5 bold-span">'
                        + convertedTime
                        + ':'
                        + deleteSplitTime[1]
                        + ' '
                        + ampm
                        + '</td>'
                        : '<td class="col-xs-4">' + convertedTime + ':'
                                + deleteSplitTime[1] + ' ' + ampm + '</td>';
                row += '<td class="col-xs-7"></td>';
                row += '<td class="col-xs-1"><label id="deleteSlotLabel'+ CONSTANTS.SHOWTIME+'"><input type="checkbox" name="deleteSlot" id='
                        + CONSTANTS.SHOWTIME
                        + ' value='
                        + schedule.slotTime
                        + ' disabled="disabled"/></label></td>';
                row += '</tr>';
            }
            $('#deleteSlotBody').append(row);
            //set the tooltip
            $("#deleteSlotLabel"+ CONSTANTS.SHOWTIME).attr("title",CONSTANTS.DELETE_CHECK_DISABLE_TOOLTIP);
            $("input:checkbox[name=deleteSlot]:disabled").attr("title",CONSTANTS.DELETE_CHECK_DISABLE_TOOLTIP);
        }
        //set the tooltip
        $("input:checkbox[name=deleteSlot]:enabled").attr("title",CONSTANTS.DELETE_CHECK_ENABLE_TOOLTIP);
        self.deleteSelectAll();
        }
        $("#deleteSlotBody tr>td:nth-child(1),#deleteSlotBody tr>td:nth-child(2)").addClass("selectable SelectIE");
        $("#deleteSlotBody tr>td:nth-child(1)::selection,#deleteSlotBody tr>td:nth-child(2)::selection").addClass("tableBodyBackground");
        //scroll should be top whenever screen loads
        $(".tableContainer").scrollTop(0);
    };
    /*
     * Method to show delete option block with scheduled
     * slots
     */
    this.deleteMultipleSlot = function(requisitionSchedulesSlot,
            convertDeleteTime, deleteStamp, deletetTime, schedule, row) {
        var deleteMultipleSlotDate = "";
        for ( var i = 0; i < requisitionSchedulesSlot.length; i++) {
            deleteMultipleSlotDate = schedule.requisitionSchedules[0].requisitionSchedule[i].beginTimestamp
                    .substring(11, 16);
            // compare the slot time
            if (deleteMultipleSlotDate === schedule.slotTime) {
                // Block will have unique time and scheduled slot
                if (i === 0) {
                    row += deletetTime === "00" ? '<td class="col-xs-5 bold-span">'
                            + convertDeleteTime
                            + ':'
                            + deletetTime
                            + ' '
                            + deleteStamp + '</td>'
                            : '<td class="col-xs-5">' + convertDeleteTime + ':'
                                    + deletetTime + ' ' + deleteStamp + '</td>';
                    row=self.showSlots(requisitionSchedulesSlot[i], row);
                    //for status code 3 the checkbox should be disabled
                        if (requisitionSchedulesSlot
                            && (requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 3 ||
                                    requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 2)) {
                        row += '<td class="col-xs-1"><label id="deleteSlotLabel'+ CONSTANTS.SHOWTIME+'">'
                            +'<input type="checkbox" disabled="disabled" name="deleteSlot" id='
                            + CONSTANTS.SHOWTIME
                            + ' value='
                            + schedule.slotTime
                            + '-'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '>'
                            + '<input type="hidden" value="'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '" id="hiddenTime'
                            + CONSTANTS.SHOWTIME
                            + '"></label></td>';
                    }else{
                        row += '<td class="col-xs-1"><input type="checkbox" name="deleteSlot" id='
                            + CONSTANTS.SHOWTIME
                            + ' value='
                            + schedule.slotTime
                            + '-'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '>'
                            + '<input type="hidden" value="'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '" id="hiddenTime'
                            + CONSTANTS.SHOWTIME
                            + '"></label></td>';
                    }
                } else {
                    row += '</tr>';
                    row += '<tr><td></td>';
                    row=self.showSlots(requisitionSchedulesSlot[i], row);
                    //for status code 3 the checkbox should be disabled
                    if (requisitionSchedulesSlot
                            && (requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 3 ||
                                    requisitionSchedulesSlot[i].requisitionScheduleStatusCode === 2)) {
                        row += '<td class="col-xs-1"><label id="deleteSlotLabel'+ CONSTANTS.SHOWTIME+'">'
                            +'<input type="checkbox" disabled="disabled" name="deleteSlot" id='
                            + CONSTANTS.SHOWTIME
                            + ' value='
                            + schedule.slotTime
                            + '-'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '>'
                            + '<input type="hidden" value="'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '" id="hiddenTime'
                            + CONSTANTS.SHOWTIME
                            + '"></label></td>';
                    }else{
                        row += '<td class="col-xs-1"><input type="checkbox" name="deleteSlot" id='
                            + CONSTANTS.SHOWTIME
                            + ' value='
                            + schedule.slotTime
                            + '-'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '>'
                            + '<input type="hidden" value="'
                            + requisitionSchedulesSlot[i].sequenceNumber
                            + '" id="hiddenTime'
                            + CONSTANTS.SHOWTIME
                            + '"></label></td>';
                    }
                }
            }
         }
        return row;
    };
    /*
     * Method to show the delete scheduled slots
     * for a particular time.
     */
     this.deleteSingleSlot = function(requisitionSchedulesSlot,
            deleteConvertedTime, timeStamp, SplitDeleteTime, schedule, row) {
        var deleteSingleDate = "";
        deleteSingleDate = requisitionSchedulesSlot.beginTimestamp.substring(
                11, 16);
        //compare the slot time
        if (deleteSingleDate === schedule.slotTime) {
            row += SplitDeleteTime === "00" ? '<td class="col-xs-5 bold-span">'
                    + deleteConvertedTime + ':' + SplitDeleteTime + ' '
                    + timeStamp + '</td>' : '<td class="col-xs-5">'
                    + deleteConvertedTime + ':' + SplitDeleteTime + ' '
                    + timeStamp + '</td>';
            row=self.showSlots(requisitionSchedulesSlot, row);
            if (requisitionSchedulesSlot
                    && (requisitionSchedulesSlot.requisitionScheduleStatusCode === 3 ||
                            requisitionSchedulesSlot.requisitionScheduleStatusCode === 2)) {
                row += '<td class="col-xs-1"><label id="deleteSlotLabel'+ CONSTANTS.SHOWTIME+'">'
                    +'<input type="checkbox" disabled="disabled" name="deleteSlot" id='
                        + CONSTANTS.SHOWTIME
                        + ' value='
                        + schedule.slotTime
                        + '-'
                        + requisitionSchedulesSlot.sequenceNumber
                        + '>'
                        + '<input type="hidden" value="'
                        + requisitionSchedulesSlot.sequenceNumber
                        + '" id="hiddenTime'
                        + CONSTANTS.SHOWTIME
                        + '"></label></td>';
            }else{
                 row += '<td class="col-xs-1"><input type="checkbox" name="deleteSlot" id='
                        + CONSTANTS.SHOWTIME
                        + ' value='
                        + schedule.slotTime
                        + '-'
                        + requisitionSchedulesSlot.sequenceNumber
                        + '>'
                        + '<input type="hidden" value="'
                        + requisitionSchedulesSlot.sequenceNumber
                        + '" id="hiddenTime'
                        + CONSTANTS.SHOWTIME
                        + '"></label></td>';
            }
        }
        return row;
    };
    /*
     * Method: Select All To check or uncheck
     * */
    this.deleteSelectAll = function(){
         $('input:checkbox[name=deleteAllSlot]').click(function(){
             if($("input:checkbox[name=deleteAllSlot]").prop('checked')){
                 //Check all enabled checkboxes
                 $("input:checkbox[name=deleteSlot]:enabled").prop('checked',true);
             }else{
                 //uncheck all enabled checkboxes
                 $("input:checkbox[name=deleteSlot]:enabled").prop('checked',false);
             }
             //Check wont allow for disabled checkboxes
             $("input:checkbox[name=deleteSlot]:disabled").prop('checked',false);
         });
         $("input:checkbox[name=deleteSlot]:enabled").click(function(){
             $("input:checkbox[name=deleteAllSlot]").prop('checked',false);
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
            $('#printPacketSlotBody').empty();
            // For met calendar type
            if (MODEL.CalendarType === "2") {
                $('.printPacketSlotContainer').addClass("printPacketMet");
                $('#printInterviewLoc').show();
            }else{
                $('.printPacketSlotContainer').removeClass("printPacketMet");
                $('#printInterviewLoc').hide();
            }
            if (MODEL.AddPrintPacket) {
                 $('#recurWeekLabel').show();
                 $("#recurWeekLabel label").text("Recurring Weeks");
            }else{
                 $('#recurWeekLabel').hide();
            }
            if(MODEL.CalendarDayDetailList){
            printpacketResponse = MODEL.CalendarDayDetailList;
            MODEL.DeleteSchedule = MODEL.CalendarDayDetailList;
            for ( var slotCount = 0; slotCount < printpacketResponse.requisitionScheduleSlotDtl.length; slotCount++) {
                schedule = printpacketResponse.requisitionScheduleSlotDtl[slotCount];
                timeSplit = schedule.slotTime.split(":");
                ampm = timeSplit[0] < 12 ? "am" : "pm";
                displayTime = timeSplit[0] > 12 ? Number(timeSplit[0]) - 12
                        : (timeSplit[0] !== "10" ? timeSplit[0].replace("0", "")
                                : timeSplit[0]);
                convertedTime = timeSplit[0] < 10 ? timeSplit[0].replace("0", "") : timeSplit[0];
                CONSTANTS.SHOWTIME = convertedTime + timeSplit[1];
                row = '<tr>';
                //if schedule object having requisitionSchedules array
                if (schedule && schedule.requisitionSchedules) {
                    requisitionSchedulesSlot = schedule.requisitionSchedules[0].requisitionSchedule;
                    //check the length of requisitionSchedules array
                    if (requisitionSchedulesSlot.length > 1) {
                        row = self.loadMultiplePackets(requisitionSchedulesSlot,
                                schedule, timeSplit[1], ampm, row, displayTime);
                    } else if(requisitionSchedulesSlot) {
                        row = self.loadSinglePackets(requisitionSchedulesSlot, schedule,
                                timeSplit[1], ampm, row, displayTime);
                    }
                    row += '</tr>';
                } else {
                        row = self.loadDefaultPackets(displayTime,ampm,timeSplit[1],schedule,row);
                    row += '</tr>';
                }
                $('#printPacketSlotBody').append(row);
                // Method will check the scheduled interviews
                $(".addTimeSelect").attr("title",CONSTANTS.RECURRING_WEEKS_TOOLTIP);
                $("#printSlotLabel"+CONSTANTS.SHOWTIME).attr('title',CONSTANTS.PRINT_PACKETS_TOOLTIP);
                $("input:checkbox[name=printSlot]:disabled").attr('title',CONSTANTS.PRINT_PACKETS_TOOLTIP);
                $("input:checkbox[name=printSlot]:enabled").attr('title',CONSTANTS.PRINT_PACKETS_ENABLED_TOOLTIP);
                }
            if (MODEL.AddPrintPacket) {
                $(".addTimeSelect").selectBoxIt();
                //Bind the data and set the dynamic height and position to options
                $(".addTimeSelect").bind({
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
                //refresh the dropdowns
                if ($(".addTimeSelect").data("selectBox-selectBoxIt")) {
                    $(".addTimeSelect").data("selectBox-selectBoxIt").refresh();
                }
            }
            // Select all method call
            self.printSelectAll();
            }
            if (MODEL.AddPrintPacket) {
                $('.printPacketSlotContainer').addClass("printPacketMet");
                $('#checkPrintPacket').removeClass("checkNormal").addClass("checkAdd");
                $('#recurWeekLabel').show();
                $("#recurWeekLabel label").text("Recurring Weeks");
                //For met calendar type add some classes to selectall checkbox
                if(MODEL.CalendarType === "2"){
                    $('#checkPrintPacket').removeClass("checkNormal").addClass("checkMetAdd");
                    $('#checkPrintPacket').removeClass("checkAdd");
                    $('#checkPrintPacket').removeClass("checkMet");
                    $("#printpacketCheckAll").css({"margin-left": "4px", "margin-top": "0px", "margin-right":"0px","margin-bottom":"0px"});
                    $("#printInterviewLoc").css("margin-left","10px");
                    $('#recurWeekLabel').css("margin-left","2px");
                    $('#checkPrintPacket').removeClass("printMet");
                }else{
                    $('#checkPrintPacket').removeClass("checkNormal").addClass("checkMet");
                    $('#checkPrintPacket').removeClass("checkMetAdd");
                    $('#checkPrintPacket').removeClass("checkAdd");
                    $("#printpacketCheckAll").css({"margin-left": "4px", "margin-top": "0px", "margin-right":"0px","margin-bottom":"0px"});
                    $("#printInterviewLoc").css("margin-left","0px");
                    $('#recurWeekLabel').css("margin-left","10px");
                    $('#checkPrintPacket').removeClass("printMet");
                }
            }else{
                if(MODEL.CalendarType === "2"){
                    $('#checkPrintPacket').removeClass("checkNormal").removeClass("checkMet");
                    $('#checkPrintPacket').removeClass("checkMetAdd");
                    $('#checkPrintPacket').removeClass("checkAdd");
                    $("#printpacketCheckAll").css({"margin-left": "10px", "margin-top": "0px", "margin-right":"0px","margin-bottom":"0px"});
                    $("#printInterviewLoc").css("margin-left","0px");
                    $('#recurWeekLabel').css("margin-left","0px");
                    if($(".storeDisabled").text()!==""){
                        $('#checkPrintPacket').addClass("checkMet");
                        $('#checkPrintPacket').removeClass("printMet");
                        $('#checkPrintPacket').removeClass("checkNormal");
                        $('#checkPrintPacket').removeClass("checkMetAdd");
                        $('#checkPrintPacket').removeClass("checkAdd");
                    }else{
                        $('#checkPrintPacket').addClass("printMet");
                        $('#checkPrintPacket').removeClass("checkMet");
                    }
                }else{
                    $('#checkPrintPacket').removeClass("checkAdd").addClass("checkNormal");
                    $('#checkPrintPacket').removeClass("checkMet");
                    $('#checkPrintPacket').removeClass("checkMetAdd");
                    $("#printpacketCheckAll").css({"margin-left": "0px", "margin-top": "0px", "margin-right":"4px","margin-bottom":"0px"});
                    $("#printInterviewLoc").css("margin-left","0px");
                    $('#recurWeekLabel').css("margin-left","0px");
                    $('#checkPrintPacket').removeClass("printMet");
                }
                $('#recurWeekLabel').hide();
                $('.printPacketSlotContainer').removeClass("printPacketMet");
            }
            //Restrict the alphabets and special charaters
            $(".storeNumText").off('paste');
            $(".storeNumText").on('paste', function(e) {
                setTimeout(function(){
                    var tempNum = $(e.currentTarget).val().match(/[0-9]+/g);
                    $(e.currentTarget).val(((tempNum) ? (tempNum.join("")) : ""));
                },100);
            });
            $(".storeNumText").keypress(function(e) {
                var temp = String.fromCharCode(e.which);
                if (!(/[0-9]/.test(temp))) {
                    return false;
                }
            });
            $("#printPacketSlotBody tr>td:nth-child(1),#printPacketSlotBody tr>td:nth-child(2)").addClass("selectable SelectIE");
            $("#printPacketSlotBody tr>td:nth-child(1)::selection,#printPacketSlotBody tr>td:nth-child(2)::selection").addClass("tableBodyBackground");            
            //scrolltop whenever the screen loads
            $(".tableContainer").scrollTop(0);
        };
        /*
         * Method to show the default print packets slots.
         * It will have slot time with no scheduled
         */
        this.loadDefaultPackets = function(convertedDefaultTime,timeStamp,timeSplit,schedule,row){
            // check the minutes is 00 then add class "bold"
            row += timeSplit === "00" ? '<td class="col-xs-3 bold-span">'
                    + convertedDefaultTime
                    + ':'
                    + timeSplit
                    + ' '
                    + timeStamp
                    + '</td>'
                    : '<td class="col-xs-3">' + convertedDefaultTime + ':'
                            + timeSplit + ' ' + timeStamp + '</td>';
            row += '<td class="col-xs-6"></td>';
            row += '<td class="col-xs-1"><label id="printSlotLabel'+ CONSTANTS.SHOWTIME+'">'
                +'<input type="checkbox" disabled="disabled" name="printSlot" id='
                    + CONSTANTS.SHOWTIME
                    + '/>'
                    + '<input type="hidden" value='
                    + schedule.slotTime
                    + ' id="hiddenTime' + CONSTANTS.SHOWTIME + '"></label></td>';
            if (MODEL.CalendarType === "2" && !MODEL.AddPrintPacket) {
                row += '<td class="col-xs-1"></td>';
            } else {
                //if traverse through option add then should show dropdown in printpacket block
                if (MODEL.AddPrintPacket) {
                    row += '<td class="col-xs-2"><select class="form-control small-Select addTimeSelect printPacketSelect" data-size="5" id="recurringWeeks'
                            + schedule.slotTime + '">';
                    for (var recurWeeks = 0; recurWeeks < MODEL.RecurringWeeksCollection.length; recurWeeks++) {
                        row +='<option value="recurWeeks'
                              + MODEL.RecurringWeeksCollection[recurWeeks].value
                              + '">'
                              + MODEL.RecurringWeeksCollection[recurWeeks].name
                              + '</option>';
                  }
                    row += '</select></td>';
                    // For Met type calendar
                    if (MODEL.CalendarType === "2") {
                        row += '<td class="col-xs-1"><input type="text" class="storeNumText" maxlength="4" value=""/></td>';
                    }
                }
            }
            return row;
        };
        /*
         * Method to load multiple and single scheduled slots
         * for a particular time.
         */
        this.loadMultiplePackets = function(requisitionSchedulesSlot, schedule,
            printTimeSplit, printTimeStamp, row, printConvertedTime) {
            var printPacketDate = "";
            var printStoreNumber = "";
            for ( var i = 0; i < requisitionSchedulesSlot.length; i++) {
            printPacketDate = requisitionSchedulesSlot[i].beginTimestamp
                    .substring(11, 16);
            printStoreNumber = requisitionSchedulesSlot[i].storeNumber;
            // compare the slot time
            if (printPacketDate === schedule.slotTime) {
                if (i === 0) {
                    // check the minutes is 00 then add class "bold"
                    row += printTimeSplit === "00" ? '<td class="col-xs-3 bold-span">'
                            + printConvertedTime
                            + ':'
                            + printTimeSplit
                            + ' '
                            + printTimeStamp + '</td>'
                            :'<td class="col-xs-3">'
                                    + printConvertedTime + ':' + printTimeSplit
                                    + ' ' + printTimeStamp + '</td>';
                    row=self.showSlots(requisitionSchedulesSlot[i], row);
                    if(requisitionSchedulesSlot && requisitionSchedulesSlot[i].requisitionScheduleStatusCode===3){
                        row += '<td class="col-xs-1"><input type="checkbox" name="printSlot" id='
                             + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot[i].sequenceNumber
                                + '>'
                                + '<input type="hidden" value='
                                + schedule.slotTime
                                + ' id="hiddenTime'+CONSTANTS.SHOWTIME+'">'
                            //if id id is not undefined then the value is set to hidden type
                                +'<input type="hidden" value="' +requisitionSchedulesSlot[i].phoneScreenId
                                + '" id="phoneId'+CONSTANTS.SHOWTIME+''+requisitionSchedulesSlot[i].sequenceNumber+'">'
                                + '<input type="hidden" value="' +requisitionSchedulesSlot[i].candidateType
                                + '" id="candidateType'+CONSTANTS.SHOWTIME+''+requisitionSchedulesSlot[i].sequenceNumber+'">';
                        '</td>';
                    }else{
                        row += '<td class="col-xs-1"><label id="printSlotLabel'+ CONSTANTS.SHOWTIME+'">'
                        +'<input type="checkbox" disabled="disabled" name="printSlot" id='
                            + CONSTANTS.SHOWTIME
                            + '>'
                            + '<input type="hidden" value='
                            + schedule.slotTime
                            + ' id="hiddenTime' + CONSTANTS.SHOWTIME + '">';
                        '</label></td>';
                    }
                    /*
                     * if MET calendar type store number and its is not traversed
                     * through add,then store number field should be default view in column
                     */
                    if (MODEL.CalendarType === "2" && !MODEL.AddPrintPacket) {
                        row += '<td class="col-xs-1 storeDisabled">' + printStoreNumber
                                + '</td>';
                    } else {
                        // Traverse through add option
                       row = self.metCalendarForAdd(printStoreNumber,row,schedule);
                    }
                } else {
                    row += '</tr>';
                    row += '<tr><td class="col-xs-3"></td>';
                    row=self.showSlots(requisitionSchedulesSlot[i], row);
                    if(requisitionSchedulesSlot && requisitionSchedulesSlot[i].requisitionScheduleStatusCode===3){
                        row += '<td class="col-xs-1"><input type="checkbox" name="printSlot" id='
                                + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot[i].sequenceNumber
                                + '>'
                                + '<input type="hidden" value='
                                + schedule.slotTime
                                + ' id="hiddenTime' + + CONSTANTS.SHOWTIME+'">'
                            //if id id is not undefined then the value is set to hidden type
                            +'<input type="hidden" value="' +requisitionSchedulesSlot[i].phoneScreenId
                                    + '" id="phoneId'+CONSTANTS.SHOWTIME+''+requisitionSchedulesSlot[i].sequenceNumber+'">'
                           + '<input type="hidden" value="' +requisitionSchedulesSlot[i].candidateType
                                    + '" id="candidateType'+CONSTANTS.SHOWTIME+''+requisitionSchedulesSlot[i].sequenceNumber+'">';
                        '</td>';
                    }else{
                         row += '<td class="col-xs-1"><label id="printSlotLabel'+ CONSTANTS.SHOWTIME+'">'+
                         '<input type="checkbox" disabled="disabled" name="printSlot" id='
                             + CONSTANTS.SHOWTIME
                             + '>'
                             + '<input type="hidden" value='
                             + schedule.slotTime
                             + ' id="hiddenTime' + CONSTANTS.SHOWTIME + '">';
                     '</label></td>';
                    }
                    row = self.metCalendarPackets(row,printStoreNumber);
                }
            }
        }
       return row;
      };
    /*
     * Method for If calendar type is "MET" and traverse through
     * add option then should show the store number input field
     */
    this.metCalendarPackets = function(row, storeNumber) {
        if (MODEL.CalendarType === "2" && !MODEL.AddPrintPacket) {
            row += '<td class="col-xs-1 storeDisabled">' + storeNumber + '</td>';
        } else {
            // For Met type calendar
            if (MODEL.AddPrintPacket) {
                row += '<td class="col-xs-2"></td>';
                if (MODEL.CalendarType === "2") {
                    row += '<td class="col-xs-1"><input type="text" class="storeNumText" maxlength="4" value='
                            + storeNumber + '></td>';
                }
            }
        }
        return row;
    };
    /*
     * Method for calendar type "MET"
     */
    this.metCalendarForAdd = function(storeNumber, row, schedule) {
        if (MODEL.AddPrintPacket) {
            $("#recurWeekLabel").show();
            $("#recurWeekLabel label").text("Recurring Weeks");
            row += '<td class="col-xs-2"><select class="form-control small-Select addTimeSelect printPacketSelect" data-size="5" id="recurringWeeks'
                    + schedule.slotTime + '">';
            for (var recurWeeks = 0; recurWeeks < MODEL.RecurringWeeksCollection.length; recurWeeks++) {
                row +='<option value="recurWeeks'
                      + MODEL.RecurringWeeksCollection[recurWeeks].value
                      + '">'
                      + MODEL.RecurringWeeksCollection[recurWeeks].name
                      + '</option>';
            }
            row += '</select></td>';
            // For Met type calendar
            if (MODEL.CalendarType === "2") {
                row += '<td class="col-xs-1"><input type="text" class="storeNumText" maxlength="4" value='
                        + storeNumber + '></td>';
            }
        }
        return row;
    };
    /*
     * Method to load empty slots with slottime
     */
    this.loadSinglePackets = function(requisitionSchedulesSlot, schedule,
            printSingleTime, printStamp, row, convertedSingleTime) {
        var printSingleDate = "";
        printSingleDate = requisitionSchedulesSlot.beginTimestamp.substring(11,
                16);
        //compare the slot time
        if (printSingleDate === schedule.slotTime) {
            // check the minutes is 00 then add class "bold"
            row += printSingleTime === "00" ? '<td class="col-xs-3 bold-span">'
                    + convertedSingleTime + ':' + printSingleTime + ' '
                    + printStamp + '</td>' : '<td class="col-xs-3">'
                    + convertedSingleTime + ':' + printSingleTime + ' '
                    + printStamp + '</td>';
            row=self.showSlots(requisitionSchedulesSlot, row);
            if(requisitionSchedulesSlot && requisitionSchedulesSlot.requisitionScheduleStatusCode===3){
                row += '<td class="col-xs-1"><input type="checkbox" name="printSlot" id='
                        + CONSTANTS.SHOWTIME+":"+requisitionSchedulesSlot.sequenceNumber
                        + '>'
                        + '<input type="hidden" value='
                        + schedule.slotTime
                        + ' id="hiddenTime' +CONSTANTS.SHOWTIME+
                         '">'
                //method call to get phone screen id
                    //if id id is not undefined then the value is set to hidden type
                    +'<input type="hidden" value="' +  requisitionSchedulesSlot.phoneScreenId
                            + '" id="phoneId'+CONSTANTS.SHOWTIME+''+ requisitionSchedulesSlot.sequenceNumber + '">'
                    +'<input type="hidden" value="' +  requisitionSchedulesSlot.candidateType
                            + '" id="candidateType'+CONSTANTS.SHOWTIME+''+ requisitionSchedulesSlot.sequenceNumber+ '">';
            '</td>';
            }else{
                 row += '<td class="col-xs-1"><label id="printSlotLabel'+ CONSTANTS.SHOWTIME+'">'+
                 '<input type="checkbox" disabled="disabled" name="printSlot" id='
                        + CONSTANTS.SHOWTIME
                        + '>'
                        + '<input type="hidden" value='
                        + schedule.slotTime
                        + ' id="hiddenTime'
                        + CONSTANTS.SHOWTIME + '"></label></td>';
                //method call to get phone screen id
            }
            //for met calendar type
            if (MODEL.CalendarType === "2" && !MODEL.AddPrintPacket) {
                row += '<td class="col-xs-1 storeDisabled">'
                        + requisitionSchedulesSlot.storeNumber + '</td>';
            } else {
                if (MODEL.AddPrintPacket) {
                    $("#recurWeekLabel").show();
                    $("#recurWeekLabel label").text("Recurring Weeks");
                    row += '<td class="col-xs-2"><select class="form-control small-Select addTimeSelect printPacketSelect" data-size="5" id="recurringWeeks'
                            + schedule.slotTime + '">';
                    for (var recurWeeks = 0; recurWeeks < MODEL.RecurringWeeksCollection.length; recurWeeks++) {
                        row +='<option value="recurWeeks'
                              + MODEL.RecurringWeeksCollection[recurWeeks].value
                              + '">'
                              + MODEL.RecurringWeeksCollection[recurWeeks].name
                              + '</option>';
                  }
                    row += '</select></td>';
                    // For Met type calendar
                        row += MODEL.CalendarType === "2"?'<td class="col-xs-1"><input type="text" class="storeNumText" maxlength="4" value='
                                + requisitionSchedulesSlot.storeNumber
                                + '></td>':"";
                    }
                }
            }
        return row;
    };
    /*
     * Method: Select All for print packet
     * */
    this.printSelectAll = function(){
        //Check or uncheck
         $('input:checkbox[name=printPacketSlot]').click(function(){
             if($("input:checkbox[name=printPacketSlot]").prop('checked')){
                 //check all
                 $("input:checkbox[name=printSlot]:enabled").prop('checked',true);
             }else{
                 //uncheck all
                 $("input:checkbox[name=printSlot]:enabled").prop('checked',false);
             }
             //check will not allow for disabled
             $("input:checkbox[name=printSlot]:disabled").prop('checked',false);
         });
         $("input:checkbox[name=printSlot]:enabled").click(function(){
             $("input:checkbox[name=printPacketSlot]").prop('checked',false);
         });
    };
    /*
     * Method: Submit the service for print packet
     * */
    this.submitPrintPacketAction = function(){
        var print={};
        var printPacketRequest = {};
        print.HiringEventPacket={};
        print.HiringEventPacket.hiringEventDetail={};
        print.HiringEventPacket.Applicants={};
        print.HiringEventPacket.Applicants.ApplicantDetails=[];
        var printSlotId = [];
        var id="";
        var phoneScrenId="";
        var candidateType="";
        //Get an id of selected checkbox
         $('input:checkbox[name=printSlot]:checked').each(function(){
             printSlotId.push($(this).attr('id'));
        });
         //Selected check length is 1
         if(printSlotId.length===1){
             var singlePacketSlot =  printSlotId[0].split(":");
              id=singlePacketSlot[0]+""+singlePacketSlot[1];
              phoneScrenId = $("#phoneId"+id).val()!== undefined?$("#phoneId"+id).val():"0";
              candidateType = $("#candidateType"+id).val()!== undefined?$("#candidateType"+id).val():"null";
             //form the params
              printPacketRequest = {
                  "interviewDateTime" : CONSTANTS.SELECTEDDATE+" "+$("#hiddenTime"+singlePacketSlot[0]).val()+":00.0",
                  "phnscrnId": phoneScrenId,
                  "intExtFlg": candidateType
               };
              //Push the params into an ApplicantDetails array
              print.HiringEventPacket.Applicants.ApplicantDetails.push(printPacketRequest);
        }
         //Selected check length is greater than 1
        else if(printSlotId.length>1){
            for(var j=printSlotId.length-1;j>=0;j--){
                var packetSlot = printSlotId[j].split(":");
                   id=packetSlot[0]+""+packetSlot[1];
                   phoneScrenId = $("#phoneId"+id ).val()!== undefined?$("#phoneId"+id).val():"0";
                   candidateType = $("#candidateType"+id).val()!== undefined?$("#candidateType"+id).val():"null";
                   var slot = CONSTANTS.SELECTEDDATE+" "+$("#hiddenTime"+packetSlot[0]).val()+":00.0";
                   //form the params
               printPacketRequest = {
                  "interviewDateTime" :slot,
                  "phnscrnId": phoneScrenId,
                  "intExtFlg":candidateType
                };
              //Push the params into an ApplicantDetails array
                print.HiringEventPacket.Applicants.ApplicantDetails.push(printPacketRequest);
            }
        }
         //Assign the json value to hidden to send service
        var printPacketServiceUrl = CONSTANTS.printPacketService;
        $("#printPacketForm").attr("action",printPacketServiceUrl);
        $("input[name='hiringPacket']").attr("value", JSON.stringify(print));
        $("input[name='Content-Type']").attr("value",CONSTANTS.APPLICATION_JSON);
        //Refer the event for submit button
        var event = jQuery.Event("submit");
        //On posting form submit the button event will be triggered
        $("#printPacketForm").trigger(event);
        var printPacketFunction = $.Callbacks('once');
        //callback method
        printPacketFunction.add(self.getCalendarDetail);
        //Service call
        var calendarDetailUrl = CONSTANTS.CalendarDetailService;
        RSASERVICES.calendarDetailsGet(printPacketFunction,calendarDetailUrl,
                CONSTANTS.CALENDARID, CONSTANTS.SELECTEDDATE);
        $(".daySlotContainer").show();
        $('.model-radio input:radio[value="View"]').prop('checked', true);
        $(".addDaySlotContainer").hide();
        $(".deleteDaySlotContainer").hide();
        $(".printPacketSlotContainer").hide();
    };
    /*
     * Method: It will be executed based on action
     * */
    this.actionBlocks = function(){
        $("#showSlotPopup").modal('hide');
        var actionValue = $('input:radio[name=inlineRadioOptions]:checked')
        .val();
        if (actionValue === "Add") {
            //On submit of add
            self.submitAddAction();
        }
        else if(actionValue === "Delete"){
            //On submit of delete
            self.submitDeleteAction();
        }
        else if(actionValue === "PrintPackets"){
            //On submit of PrintPackets
            self.submitPrintPacketAction();
        }
    };
    /*
     * Method will make the popup remain in page
     * on keyboard and mosue events.
     */
    this.setAlertDraggableFocus = function(){
        $('#alert').off("shown.bs.modal");
        $('#alert').on("shown.bs.modal", function() {
            $("#alertOK").focus();
        });
        //To drag the popup
        self.blockPopup("#alert");
        self.removeAlertCss();
        $("#alert").draggable({
            handle: ".modal-header"
        });
    };
    this.removeAlertCss = function(){
	   	 $("#alert .modal-body").removeClass("autoHeight");
	   	 $("#alert .modal-content").removeClass('popupWidth');
	   	 $("#alert .modal-body").removeClass('popupHeight');
	     $('#alert #alertModalLabel').text("");
         $("#alert .modal-content").removeClass("slotExceedWidth");
         $("#alert .modal-body").removeClass("slotExceedBodyHeight");
         $("#alert .modal-body .alertModalBody").removeClass("slotExceedModalBody");
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
         $("#alert .modal-content").removeClass("metStoreContentWidth");
         $("#alert .modal-body .alertModalBody").removeClass("metStoreBodyWidth");
         $("#alert .modal-content").removeClass("slotContentTop");
    };
    /*
     * Method to block and drag the warning
     * popup
     */
    this.setWarningDraggable = function(){
        self.blockPopup("#showSlotPopup");
        $("#showSlotPopup").draggable({
            handle: ".modal-header"
        });
    };
    /*
     * Method to block and drag the addblocktime
     * popup
     */
    this.setAddblockTime = function(){
        self.blockPopup("#addBlocksTime");
        /*
         * Method to drag the popups
         */
        $("#addBlocksTime").draggable({
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
    $("#alert").keypress(function(evt){
        if(evt && evt.keyCode===13){
            $("#alert").modal('hide');
        }
    });
    /*
     * On change event for actions
     * */
    $('.model-radio input:radio[name=inlineRadioOptions]').off();
    $('.model-radio input:radio[name=inlineRadioOptions]').on('change',function() {
        $(".tableContainer").scrollTop(0);
        //on click of view
        if ($(this).val() === "View") {
             $(".tableContainer").scrollTop(0);
            MODEL.AddPrintPacket = false;
            $(".daySlotContainer").show();
            $(".addDaySlotContainer").hide();
            $(".deleteDaySlotContainer").hide();
            $(".printPacketSlotContainer").hide();
            self.viewTimeBlock();
            //uncheck the delete and add select all check box
            $('input:checkbox[name=deleteAllSlot]').attr('checked',false);
            $('input:checkbox[name=addAllCheck]').attr('checked',false);
        }
        //on click of delete
        else if ($(this).val() === "Delete") {
             $(".tableContainer").scrollTop(0);
            MODEL.AddPrintPacket = false;
          //service call for calendar details
            $(".deleteDaySlotContainer").show();
            $(".addDaySlotContainer").hide();
            $(".daySlotContainer").hide();
            $(".printPacketSlotContainer").hide();
            self.deleteTimeBlock();
            //uncheck the delete and add select all check box
            $('input:checkbox[name=deleteAllSlot]').attr('checked',false);
            $('input:checkbox[name=addAllCheck]').attr('checked',false);
        }
        //on click of add
        else if ($(this).val() === "Add") {
             $(".tableContainer").scrollTop(0);
            MODEL.AddTimeBlock = false;
            MODEL.AddPrintPacket = true;
            self.addTimeBlock();
            $("#addBlockTimeButton").attr("title",CONSTANTS.ADD_BLOCK_TOOLTIP);
            $("#showSlotPopup .modal-body #actionMessage").empty();
            $(".addDaySlotContainer").show();
            $(".daySlotContainer").hide();
            $(".deleteDaySlotContainer").hide();
            $(".printPacketSlotContainer").hide();
            //uncheck the delete and add select all check box
            $('input:checkbox[name=deleteAllSlot]').attr('checked',false);
            $('input:checkbox[name=addAllCheck]').attr('checked',false);
        }
        //on click of print packets
        else if ($(this).val() === "PrintPackets") {
             $(".tableContainer").scrollTop(0);
             self.loadPrintPacket();
            //uncheck the delete and add select all check box
            $('input:checkbox[name=deleteAllSlot]').attr('checked',false);
            $('input:checkbox[name=addAllCheck]').attr('checked',false);
            $('input:checkbox[name=printPacketSlot]').attr('checked',false);
            $(".printPacketSlotContainer").show();
            $(".addDaySlotContainer").hide();
            $(".daySlotContainer").hide();
            $(".deleteDaySlotContainer").hide();
        }
        //Empty the popup class
        $("#alert .modal-body").removeClass('popupHeight');
        $("#alert .modal-content").removeClass('popupWidth');
        $("#alertModalLabel").empty();
    });
};
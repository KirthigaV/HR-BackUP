/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: Utility.js
 * Application: Retail Staffing Administration
 *
 */


/**
 * Method name: Utility
 * Common methods are defined in this file.
 * The Util methods can be used throughout
 * the application.
 * The methods are common to use in throughout application
 * for reusability
 */
/*Create a object for Utility*/
var UTILITY = new utility();
function utility()
{
    /**
     *To Validate passed Date Formatted "YYYY-MM-DD"
     */
    this.formattedDate=function(date){
        var formattedDate="";
        //get Date from param value
        var day = date.getDate();
      //get Month from param value and add 1
        var month=date.getMonth()+1;
      //get Year from param value
        var year = date.getFullYear();
        //Formatted the date as required
        formattedDate = year+"-"+month+"-"+day;
        //returns string
        return formattedDate;
    };
    /**
     * Formatted month using the passed date
     */
    this.formatedMonth = function(date){
        var formattedMonth="";
        //get Date from param value
        var day = date.getDate();
        //add "0" if day<10
        day = day<10?"0"+day:day;
        var month=date.getMonth()+1;
        month = month<10?"0"+month:month;
        var year = date.getFullYear();
        //format the date
        formattedMonth = year+"-"+month+"-"+day;
        return formattedMonth;
    };
    /**
     *
     * To Validate the date and return the string type date
     */
    this.stringFormattedDate = function(date){
        var theSplitDate = "";
        //Split the "-" from the date
        date = date.split("-");
        //form the date
        theSplitDate = date[1]+"/"+date[2]+"/"+date[0];
        //return the date
        return theSplitDate;
    };
    /**
     * To Check the valid month from passed date
     */
    this.getMonthFormatted = function(date){
        //convert the date object into string and get substring for month
        var splittedDate="";
        dateTimeStamp = date.split("-");
        splittedDate = dateTimeStamp[1]+"/"+dateTimeStamp[2]+"/"+dateTimeStamp[0];
        var dateFormat = new Date(splittedDate);
        //return formatted date stamp
        return dateFormat.toString().substring(0,7)+" "+dateFormat.getDate()+" "+dateFormat.getFullYear();
    };
    /**
     * To Validate passed time stamp
     */
    this.formatTimeStamp = function(dateTimeStamp){
        var formattedDate="";
        var splittedDate="";
        //Create a date object and pass the value
        dateTimeStamp = dateTimeStamp.split("-");
        splittedDate = dateTimeStamp[1]+"/"+dateTimeStamp[2]+"/"+dateTimeStamp[0]+" "+dateTimeStamp[3];
        var date = new Date(splittedDate);
        var day = date.getDate();
        day  = ((day < 10) ? "0" : "") + day;
        var month=date.getMonth()+1;
        month  = ((month < 10) ? "0" : "") + month;
        var year = date.getFullYear();
        var hour = date.getHours();
        var minute = date.getMinutes();
        var seconds = date.getSeconds();
        //Get minutes from date object and add :"0" if min<10
        minute  = ((minute < 10) ? "0" : "") + minute;
      //Get seconds from date object and add :"0" if sec<10
        seconds  += ((seconds < 10) ? "0" : "");
        var milliseconds = date.getMilliseconds();
        //format the time stamp
        formattedDate = year+"-"+month+"-"+day+" "+hour + ":" + minute + ":" + seconds + "." + milliseconds;
        //return the time stamp
        return formattedDate;
    };
    /**
     * To Validate passed time stamp and Add 30 minutes to actual stamp
     */
    this.addThirtyMinutesToStamp = function(dateTimeStamp){
        var formattedDate="";
        var splittedDate="";
        dateTimeStamp = dateTimeStamp.split("-");
        splittedDate = dateTimeStamp[1]+"/"+dateTimeStamp[2]+"/"+dateTimeStamp[0]+" "+dateTimeStamp[3];
      //Create a date object and pass the value
        var date = new Date(splittedDate);
        //Get the minutes from data object and add 30 minutes
        //to actual time stamp and set the minutes
        date.setMinutes(date.getMinutes() + 30);
        var day = date.getDate();
        day  = ((day < 10) ? "0" : "") + day;
        var month=date.getMonth()+1;
        month  = ((month < 10) ? "0" : "") + month;
        var year = date.getFullYear();
        var hour = date.getHours();
        var minute = date.getMinutes();
        var seconds = date.getSeconds();
        var milliseconds = date.getMilliseconds();
        //Get minutes from date object and add :"0" if min<10
        minute  = ((minute < 10) ? "0" : "") + minute;
        //Get seconds from date object and add :"0" if sec<10
        seconds  += ((seconds < 10) ? "0" :"");
        //format the 30 min added time stamp
        formattedDate = year+"-"+month+"-"+day+" "+hour + ":" +minute+ ":" + seconds + "." + milliseconds;
        //return the time stamp
        return formattedDate;
    };
    /**
     * To check whether the passed value is an array or object
     */
    this.IsArray = function(currentArray) {
        /* Initializing the flag as false */
        var isArray = false;
        /* setting the flag as true based on the below logic*/
        if ($.isArray(currentArray)) {
            isArray = true;
        }
        /* returning the flag*/
        return isArray;
    };
    /**
     * To Validate the passed Milliseconds
     */
    this.isDateInHiringEvent = function(theDate, eventBeginDate, eventEndDate)
    {
        var inputFlag=false;
        //Split the value using "-"
        theDate = theDate.split("-");
        eventBeginDate =  eventBeginDate.split("-");
        eventEndDate =  eventEndDate.split("-");
        //Form the date using backslash
        var theSplitDate = theDate[1]+"/"+theDate[2]+"/"+theDate[0];
        var eventStartDate = eventBeginDate[1]+"/"+eventBeginDate[2]+"/"+eventBeginDate[0];
        var eventFinishDate = eventEndDate[1]+"/"+eventEndDate[2]+"/"+eventEndDate[0];
        /*create a date object and pass the split date.
         *  Convert the split date into Parse Int and slice the value to form the actual date*/
        var inputDate = new Date(parseInt(theSplitDate.substr(6)), parseInt(theSplitDate.substr(0,2))-1, parseInt(theSplitDate.substr(3,2)), 0, 0, 0, 0);
        //Get time and milliseconds from actual date
        var inputDateMilliSeconds = inputDate.getTime();
        /*create a date object and pass the split date.
         *  Convert the split date into Parse Int and slice the value to form the actual date*/
        var inputEventBeginDate = new Date(parseInt(eventStartDate.substr(6)), parseInt(eventStartDate.substr(0,2))-1, parseInt(eventStartDate.substr(3,2)), 0, 0, 0, 0);
      //Get second input time and milliseconds from actual date
        var milliSeconds2 = inputEventBeginDate.getTime();
        /*create a date object and pass the split date.
         *  Convert the split date into Parse Int and slice the value to form the actual date*/
        var inputEventEndDate = new Date(parseInt(eventFinishDate.substr(6)), parseInt(eventFinishDate.substr(0,2))-1, parseInt(eventFinishDate.substr(3,2)), 23, 59, 59, 0);
      //Get third input time and milliseconds from actual date
        var milliSeconds3 = inputEventEndDate.getTime();
        //1.check the first input is greater than second input
        //2.check the first input is lesser than third input
        //3.If both satisfies return true else return false
        if (inputDateMilliSeconds >= milliSeconds2 && inputDateMilliSeconds <= milliSeconds3)
        {
            inputFlag = true;
        }
       return inputFlag;
    };
    /**
     * Method will freeze the screen until service
     * returns response
     */
    this.screenFreeze = function(){
        //Block screen
        $.blockUI();
        //Border apply for element
        $(".blockMsg").empty().css({"border":"none"});
        //Background color for overlay div
        $(".blockUI.blockOverlay").empty().css({"background-color":"#fff"});
    };
    /**
     * Method will block the page background.
     * it blocks the body of page.
     */
    this.blockFullPage = function() {
        $("body").block({
            message : null,
            overlayCSS : {
                backgroundColor : "rgb(139,139,139)",
                opacity : 0.6,
                cursor : "wait"
            },
            ignoreIfBlocked : false,
            baseZ: 1500
        });
    };
    /**
     * Method will unblock the blocked page background.
     * it unblocks the blocked body of page.
     */
    this.unblockFullPage = function() {
        $("body").unblock();
    };
    this.blockSlots = function(){
        $(".container").css("cursor","wait");
    };
    this.unBlockSlots = function(){
        $(".container").css("cursor","auto");
    };
    /*
     * This function is used to sort the grid
     *  when header is clicked asc or desc
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
     *  Removes all whitespace characters from the beginning and end
     *  of the specified string.
     *
     *  @param str The String whose whitespace should be trimmed.
     *
     *  @return Updated String where whitespace was removed from the
     *  beginning and end.
     */
    this.trim = function (str)
    {
        if (str == null)
        { 
            return '';
        }
        var startIndex = 0;
        while (this.isWhitespace(str.charAt(startIndex))){
            ++startIndex;
        }
        var endIndex = str.length - 1;
        while (this.isWhitespace(str.charAt(endIndex))){
            --endIndex;
        }
        if (endIndex >= startIndex){
            return str.slice(startIndex, endIndex + 1);
        }
        else{
            return "";
        }
    };
    /*
     *  Returns <code>true</code> if the specified string is
     *  a single space, tab, carriage return, newline, or formfeed character.
     *
     *  @param str The String that is is being queried.
     *
     *  @return <code>true</code> if the specified string is
     *  a single space, tab, carriage return, newline, or formfeed character.
     */
    this.isWhitespace = function (character)
    {
        switch (character)
        {
            case " ":
            case "\t":
            case "\r":
            case "\n":
            case "\f":
                return true;

            default:
                return false;
        }
    };
    /*
     * Format the date to MM/DD/YYYY
     */
    this.formatDate = function(formatdate){
        formatdate = formatdate.split("-");
        var splittedFormatdate = formatdate[1]+"/"+formatdate[2]+"/"+formatdate[0];
        return splittedFormatdate;
    };   
}
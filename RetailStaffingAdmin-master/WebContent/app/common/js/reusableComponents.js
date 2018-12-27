/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: reusablecomponents.js
 * Application: Retail Staffing Admin
 *
 */
(function($) {
    //Assign keydown\keypress to textbox
    $.fn.assignKeyDownToInputs = function() {
        $(this).on('keypress', function(event) {
            event = event || window.event;
            var elementPatter = new RegExp($(event.currentTarget).attr('data-pattern'));
            var charCode = event.which || event.keyCode;
            var charStr = String.fromCharCode(charCode);

            if (!elementPatter.test(charStr)) {
                return false;
            }
        });
        $(this).on("paste",function(e){
            var pastedText = undefined;
            if(window && window.clipboardData){
                pastedText = window.clipboardData.getData("Text");
            }
            else if(e.originalEvent.clipboardData || e.clipboardData){
                pastedText = (e.originalEvent || e).clipboardData.getData('text/plain');
            }
            var element = $(this);
            var origValue = $(this).val();
            var value = pastedText;
            var pattern = new RegExp($(element).attr("data-pattern"));
            var outputvalue = "";
            for(var i=0;i<value.length;i++){
                if(pattern.test(value[i])){
                    outputvalue += value[i];
                }
            }
            var result = origValue.substring(0,$(this).caret().begin) + outputvalue + origValue.substring($(this).caret().end);
            var inpMaxlen = parseInt($(this).attr("maxlength"));
            if(inpMaxlen < result.length){
                var diff = result.length - inpMaxlen;
                outputvalue = outputvalue.substring(0, outputvalue.length - diff);
            }
            var caretpos = $(this).caret().begin+outputvalue.length;
            $(element).val(origValue.substring(0,$(this).caret().begin) + outputvalue + origValue.substring($(this).caret().end));
            $(element).setCaretPosition(caretpos);
            $(this).trigger('keyup');
            return false;
        });
    };
    //Create Checkbox grid structure as per expected design
    $.fn.createCheckBoxGrid = function(options) {
        // This is the easiest way to have default options.
        var settings = $.extend({
            // These are the defaults.
            rows : "4",
            labelsArray : [],
            cols : "4",
            onchange : '',
            pattern : 'CL'
        }, options);
        //To get language details from user input
        var languageChecked = function() {
            var length = $(".langSkills input:checked").length;
            var checkedArrayInputs = $(".langSkills input:checked");
            CONSTANTS.checkedArray = [];
            for (var i = 0; i < length; i++) {
                if (checkedArrayInputs[i].value) {
                    CONSTANTS.checkedArray.push(checkedArrayInputs[i].value);
                }
            }

            CONSTANTS.checkedArray = CONSTANTS.checkedArray.toString();
            if (settings.onchange) {
                settings.onchange(CONSTANTS.checkedArray);
            }
        };
        //Generate the language selection checkbox control as per flex
        var generateRow = function(settings,row,k){
            if (settings.pattern === 'CL') {
                row = row + ' <div class="col-xs-3 langSkills"><div class="checkbox"><label><input type="checkbox" class="gridCheckBox" name=check' + ' value=' + settings.labelsArray[k].langCode + ' />' + settings.labelsArray[k].dsplyDesc + '</label></div></div>';
            } else {
                row = row + '<div class="col-xs-3"><div class="col-xs-7"><label>' + settings.labelsArray[k].dsplyDesc + '</label></div><div class="col-xs-3"><input type="checkbox" class="gridCheckBox" name=check' + ' value=' + settings.labelsArray[k].langCode + ' /></div></div>';
            }
            return row;
        };
        var k = 0;
      //Generate the checkbox control as per flex
        for (var i = 0; i < settings.rows; i++) {
            var row = '<div class="form-group ">';

            for (var j = 0; j < settings.cols; j++) {
                if (settings.labelsArray[k]) {
                    row = generateRow(settings,row,k);
                }
                k++;
            }
            this.append(row + '</div>');
        }
        // handle language checked
        $(".gridCheckBox").on("change", languageChecked);
    };
 /*
  * Create SchedulePreference structure as per expected design
  * Parameter1: Option will dynamically pass the preferred values
  */
    $.fn.createSchedulePreferenceGrid = function(options) {
        var fillSchdPrefLabel = "Do you need candidates to fill a specific day/time slot?";
        var fillSchdPref = "";
        var splabelsArray = [ "Early Am (4am - 6am)", "Mornings (6am - noon)", "Afternoons (noon - 5pm)", "Nights (5pm - 8pm)", "Late Night (8pm - midnight)", "Overnight (midnight - 5am)" ];
        var isMandatoryMarkup = "";
        var settings = $.extend({
            onchange : '',
            colMd : "4",
            daysLabel : "Specific Days the candidate MUST be able to work:",
            slotsLabel : "Specific Time Slots the candidate MUST be able to start work:",
            isMandatory : false,
            isRetailStaffingRequest : false,
            defaultChecked : []

        }, options);
        //Asterisk for mandatory symbol
        if (settings.isMandatory) {
            isMandatoryMarkup = "<sub>*</sub>";
        } else {
            isMandatoryMarkup = "";
        }
        //Display Scheduled preference
        var headerRow = '<div class="col-xs-12 row"><div class="col-xs-' + settings.colMd + ' field-align">Schedule Preference: ' + isMandatoryMarkup + '</div><div class="col-xs-8 row value-align schedulePref noPaddingLeft">';
        if (settings.isRetailStaffingRequest) {
            fillSchdPref = '<div class="col-xs-12 row"><div class="col-xs-5 noPaddingLeft"><label>' + fillSchdPrefLabel + '</label></div><div class="col-xs-3"><div class="col-xs-4 noPaddingLeft"><div class="radio schdRadio" style="padding-top: 0px;"><label><input type="radio" value="YES" id="schdPrefYes" name="schdPrefFlg">Yes</label></div></div><div class="col-xs-4 noPaddingLeft"><div class="radio schdRadio" style="padding-top: 0px;"><label><input type="radio" value="NO" id="schdPrefNo" name="schdPrefFlg">No</label></div></div></div></div>';
        }
        //Display day label
        var weekRows = '<div class="col-xs-12 row" id="schdPrefDays">' + isMandatoryMarkup + '<label>' + settings.daysLabel + '</label></div><div class="col-xs-12 row weekHeader"><div class=" col-xs-1"><input type="checkBox" class="spCheckBox anyTime" id="schdPref0"></div><label  class="col-xs-1 control-label">Weekdays</label>' + '</div><div class="col-xs-12 row weekHeader">' + '<div class=" col-xs-1"><input type="checkBox" class="spCheckBox anyTime" id="schdPref1"></div><label  class="col-xs-1 control-label">Saturday</label>' + '</div><div class="col-xs-12 row weekHeader"><div class=" col-xs-1"><input type="checkBox" class="spCheckBox anyTime" id="schdPref2"></div>' + '<label  class="col-xs-1 control-label">Sunday</label>' + '</div>';
        //Display slots
        var j = 3;
        var slotsheader = '<div class="col-xs-12 row slotsHeader">' + isMandatoryMarkup + '<label>' + settings.slotsLabel + '</label></div>';
        var firstRow = '';
        for (var i = 0; i < splabelsArray.length; i++) {

            firstRow = firstRow + '<div class="col-xs-12 row slots schdPrefTimes"><div class=" col-xs-1"><input type="checkBox" class="spCheckBox anyTime" id=schdPref' + j + '></div>' + '<label class="col-xs-9">' + splabelsArray[i] + '</label>' + '</div>';
            j = j + 1;

        }
        var closingDOM = '</div>';
        //for retailstaffing request add caption as mentioned in flex
        if (settings.isRetailStaffingRequest) {
            this.append(headerRow + fillSchdPref + weekRows + slotsheader + firstRow + closingDOM);
        } else {
            //others add captions without fill as per flex
            this.append(headerRow + weekRows + slotsheader + firstRow + closingDOM);
        }
        $('.spLabel').css('padding-top', '0px');
        $('.spCheckBox').on('change', UTILITY.schedulePrefCheckBoxChange.bind(this,settings));
        //Construct the components and set Preferred options as per flex
        UTILITY.setSchedPref(settings);
    };
   //Restrict the textbox input as per Regex pattern
    $.fn.restrictInputFeature = function() {
        $(this).keypress(function(e) {
            var temp = String.fromCharCode(e.which);
            var pattern = new RegExp($(e.currentTarget).attr("data-restrict"));
            if (!(pattern.test(temp))){
                return false;
            }
        });
        $(this).on("paste",function(e){
            var pastedText = undefined;
            if(window && window.clipboardData){
                pastedText = window.clipboardData.getData("Text");
            }
            else if(e.originalEvent.clipboardData || e.clipboardData){
                pastedText = (e.originalEvent || e).clipboardData.getData('text/plain');
            }
            var element = $(this);
            var origValue = $(this).val();
            var value = pastedText;
            var pattern = new RegExp($(element).attr("data-restrict"));
            var outputvalue = "";
            for(var i=0;i<value.length;i++){
                if(pattern.test(value[i])){
                    outputvalue += value[i];
                }
            }
            var result = origValue.substring(0,$(this).caret().begin) + outputvalue + origValue.substring($(this).caret().end);
            var inpMaxlen = parseInt($(this).attr("maxlength"));
            if(inpMaxlen < result.length){
                var diff = result.length - inpMaxlen;
                outputvalue = outputvalue.substring(0, outputvalue.length - diff);
            }
            var caretpos = $(this).caret().begin+outputvalue.length;
            $(element).val(origValue.substring(0,$(this).caret().begin) + outputvalue + origValue.substring($(this).caret().end));
            $(element).setCaretPosition(caretpos);
            return false;
        });
    };
    $.fn.validateNumber = function(){
            var min = parseFloat($(this).attr("data-min"));
            var max = parseFloat($(this).attr("data-max"));
            var domain = $(this).attr("data-domain");
            var precision = parseFloat($(this).attr("data-precision"));
            var value = $(this).val();
            var title = "";
            $(this).addClass("invalid-red-outline");
            $(this).attr("data-toggle","tooltip");
            $(this).attr("data-placement","right");
            if(value.indexOf(".") !== value.lastIndexOf(".")){
                title = CONSTANTS.DECIMAL_POINT_COUNT_ERROR;
            }
            else if(value.indexOf(".") > 0 && domain !== "real"){
                title = CONSTANTS.INTEGER_ERROR;
            }
            else if(value < min){
                title = CONSTANTS.LOWER_THAN_MIN_ERROR;
            }
            else if(value > max){
                title = CONSTANTS.EXCEEDS_MAX_ERROR;
            }
            else if(value.substr(value.indexOf(".")+1).length > precision){
                title = CONSTANTS.PRECISION_ERROR;
            }
            if(title !== ""){
                $(this).attr("title",title);
                return false;
            }
            $(this).removeClass("invalid-red-outline");
            $(this).removeAttr("data-toggle");
            $(this).removeAttr("data-placement");
            $(this).removeAttr("title");
            return true;
    };
    $.fn.validateDate = function() {
        UTILITY.hideTooltip(this);
        var inputFormat = "mm/dd/yyyy";
        var DECIMAL_DIGITS = "0123456789";
        var allowedFormatChars = "*#~/";
        var validInput = DECIMAL_DIGITS + allowedFormatChars;
        var advanceValueCounter = true;
        var monthRequired = false;
        var dayRequired = false;
        var yearRequired = false;
        var isReturn = false;
        var dateObj = {};
        dateObj.month = "";
        dateObj.day = "";
        dateObj.year = "";
        var temp = "";
        var len = $(this).val().trim().length;
        isReturn = UTILITY.validateDateLength(this, len, inputFormat);
        if (isReturn) {
            isReturn = false;
            return;
        }
        var j = 0;
        var n = inputFormat.length;
        for (var i = 0; i < n; i++) {
            temp = "" + $(this).val().trim().substring(j, j + 1);
            var mask = "" + inputFormat.substring(i, i + 1);
            // Check each character to see if it is allowed.
            isReturn = UTILITY.validateCharAllowed(this, validInput, temp);
            if (isReturn) {
                isReturn = false;
                return;
            }
            if (mask === "m" || mask === "M") {
                monthRequired = true;
                if (isNaN(parseInt(temp))) {
                    advanceValueCounter = false;
                } else {
                    dateObj.month += temp;
                }
            } else if (mask === "d" || mask === "D") {
                dayRequired = true;
                if (isNaN(parseInt(temp))) {
                    advanceValueCounter = false;
                } else {
                    dateObj.day += temp;
                }
            } else if (mask === "y" || mask === "Y") {
                yearRequired = true;
                if (isNaN(parseInt(temp))) {
                    UTILITY.showTooltip(this, "Type the date in the format. mm/dd/yyyy");
                    return;
                } else {
                    dateObj.year += temp;
                }
            } else {
                isReturn = UTILITY.validateCharsFormat(this, allowedFormatChars, temp);
                if (isReturn) {
                    isReturn = false;
                    return;
                }
            }
            if (advanceValueCounter) {
                j++;
            }
            advanceValueCounter = true;
        }
        if (UTILITY.checkDateFormat(monthRequired, dayRequired, yearRequired, dateObj) || (j !== len)) {
            UTILITY.showTooltip(this, "Type the date in the format. mm/dd/yyyy");
            return;
        }
        // Now, validate the sub-elements, which may have been set directly.
        isReturn = UTILITY.validateChars(this, n, dateObj, temp);
        if (isReturn) {
            isReturn = false;
            return;
        }
        var monthNum = parseInt(dateObj.month);
        var dayNum = parseInt(dateObj.day);
        var yearNum = parseInt(dateObj.year).valueOf();
        isReturn = UTILITY.validateMonth(this, monthNum);
        if (isReturn) {
            isReturn = false;
            return;
        }
        var maxDay = UTILITY.maxValueCalc(monthNum, yearNum);
        isReturn = UTILITY.validateDay(this, dayRequired, dayNum, maxDay);
        if (isReturn) {
            isReturn = false;
            return;
        }
        isReturn = UTILITY.validateYear(this, yearRequired, yearNum);
        if (isReturn) {
            isReturn = false;
            return;
        }
    };
    $.fn.caret = function (begin, end)
    {
        if (this.length === 0) {
            return;
        }
        if (typeof begin === 'number')
        {
            end = (typeof end === 'number') ? end : begin;
            return this.each(function ()
            {
                if (this.setSelectionRange)
                {
                    this.setSelectionRange(begin, end);
                } else if (this.createTextRange)
                {
                    var range = this.createTextRange();
                    range.collapse(true);
                    range.moveEnd('character', end);
                    range.moveStart('character', begin);
                    try {
                        range.select();
                   } catch (ex) {
                       //catch nothing
                   }
                }
            });
        } else
        {
            if (this[0].setSelectionRange)
            {
                begin = this[0].selectionStart;
                end = this[0].selectionEnd;
            } else if (document.selection && document.selection.createRange)
            {
                var range = document.selection.createRange();
                begin = 0 - range.duplicate().moveStart('character', -100000);
                end = begin + range.text.length;
            }
            return { begin: begin, end: end };
        }
    };
    $.fn.setCaretPosition = function (caretPos) {
        var el = $(this).get(0);
        if (el !== null) {
            if (el.createTextRange) {
                var range = el.createTextRange();
                range.move('character', caretPos);
                range.select();
                return true;
            }
            else {
                if (el.selectionStart || el.selectionStart === 0) {
                    el.focus();
                    el.setSelectionRange(caretPos, caretPos);
                    return true;
                }
                else  {
                    el.focus();
                    return false;
                }
            }
        }
    };
}(jQuery));

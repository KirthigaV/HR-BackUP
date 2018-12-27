/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: utility.js
 * Application: Retail Staffing Admin
 *
 */
var UTILITY = new utility();
function utility() {
    /*
     * This function is used to check is empty string
     */
    this.isEmptyString = function(input) {
        return (input === null || $.trim(input).length === 0);
    };
    /*
     * This function is used to check is empty array
     */
    this.isEmptyArray = function(input) {
        return (input === null || input.length === 0);
    };

    /*
     * This function is used to freeze screen
     */
    this.screenFreeze = function() {    	
         $.blockUI();
         $(".blockMsg").empty().css({
             "border" : "none"
         });
         $(".blockUI.blockOverlay").empty().css({
             //Set background for freeze as per flex
             "background-color" : "#fff"
         });
    };

    /*
     * This function is used to check if passed values is empty
     */
    this.isEmptyInput = function(id) {
        var trimmedVal = $.trim($("#" + id).val());
        if (trimmedVal !== null && trimmedVal !== '' && trimmedVal !== "") {
            return false;
        }
        return true;
    };

    /*
     * This function is used to get total number of records
     */
    this.getTotalNumberOfRecords = function(searchResult) {
        var totalRecords = 0;
        if (searchResult) {
            totalRecords = parseInt(searchResult.total);
        }
        return totalRecords;
    };

    /*
     * Returns the current date time as ISO string
     */
    this.getCurrentDateTimeAsISOString = function() {
        var currentDate = new Date();
        var currentDateStr = $.datepicker.formatDate('yy-mm-dd', currentDate);
        var patt = /\d{2}:\d{2}:\d{2}/;
        var currentTimeStamp = patt.exec(currentDate);
        var currentDateTime = currentDateStr + 'T' + currentTimeStamp;
        return currentDateTime;
    };
    /*
     * Returns ISO date Time format for date with this format yy-mm-dd
     * hr:min:sec eg: 2013-09-10 07:29:36.481631
     */
    this.getISODateTime = function(dateTime) {
        var splitArr = dateTime.split(' ', 2);
        var isodateTime = splitArr[0] + 'T' + splitArr[1];
        return isodateTime;
    };
    /*
     * ISO date format as per flex
     */
    Date.fromISO = function(s) {
        var day, tz, rx = /^(\d{4}\-\d\d\-\d\d([tT][\d:\.]*)?)([zZ]|([+\-])(\d\d):(\d\d))?$/, p = rx.exec(s) || [];
        if (p[1]) {
            day = p[1].split(/\D/).map(function(itm) {
                return parseInt(itm, 10) || 0;
            });
            day[1] -= 1;
            day = new Date(Date.UTC.apply(Date, day));
            if (!day.getDate()) {
                return NaN;
            }
            if (p[5]) {
                tz = parseInt(p[5], 10) * 60;
                if (p[6]) {
                    tz += parseInt(p[6], 10);
                }
                if (p[4] === "+") {
                    tz *= -1;
                }
                if (tz) {
                    day.setUTCMinutes(day.getUTCMinutes() + tz);
                }
            }
            return day;
        }
        return NaN;
    };

    Array.prototype.map = Array.prototype.map || function(fun, scope) {
        var L = this.length, A = [], i = 0;
        if (typeof fun === 'function') {
            while (i < L) {
                if (i in this) {
                    A[i] = fun.call(scope, this[i], i, this);
                }
                ++i;
            }
            return A;
        }
    };
    /*
     * This function is used to find the date difference between two dates
     */
    this.dayDiffISODates = function(start, end) {
        return (Date.fromISO(end) - Date.fromISO(start)) / (1000 * 60 * 60 * 24);
    };
     /*
     * Convert Sql date format to standard date format
     */
    this.convertSqlDateFormatToStdDateFormat = function(inDate) {
        // InDate Format yyyy-mm-dd
        // formattedDateString mm/dd/yyyy
        var formattedDateString = "";
        // Check that inDate is not null
        if (inDate !== null && inDate !== "") {
            // Format Date
            formattedDateString = inDate.substr(5, 2) + "/" + inDate.substr(8, 2) + "/" + inDate.substr(0, 4);
        }
        return formattedDateString;

    };
    /*
     * this function is used to display as array dynamically
     */
    this.checkAndStoreAsArray = function(value) {
        var resultListValue = [];
        if (_.isArray(value)) {
            resultListValue = value;
        } else {
            if (value) {
                resultListValue = [ value ];
            }
        }
        return resultListValue;
    };
    /*
     * this function is used to check as arraydisplay as array dynamically
     */
    this.checkForArrayAndStoreAsArray = function(response, listName, itemName) {
        var resultListValue = [];
        if (listName in response && itemName in response[listName]) {
            if (_.isArray(response[listName][itemName])) {
                resultListValue = response[listName][itemName];
            } else {
                if (itemName in response[listName]) {
                    resultListValue = [ response[listName][itemName] ];
                }
            }
        }
        return resultListValue;
    };
    /*
     * Based on the scheduled preference input enable \ Disable controls.
     */
    this.schedulePrefCheckBoxChange = function(settings) {
        var length = $(".schedulePrefRow input:checked").length;
        var checkedArrayInputs = $(".schedulePrefRow input:checked");
        var checkedArray = [];

        checkedArrayInputs = $(".schedulePrefRow input:checked");
        length = $(".schedulePrefRow input:checked").length;
        for (var i = 0; i < length; i++) {
            var currntInputId = checkedArrayInputs[i].id.substr(8, 9);
            if (currntInputId) {
                checkedArray.push({
                    "daySegCd" : (currntInputId[0] - 1).toString(),
                    "wkDayNbr" : currntInputId[1]
                });
            }
        }
        if (settings.onchange) {
            settings.onchange(checkedArray);
        }
    };
    /*
     * Set weekdays mon to Friday in array position -1 Set Saturday in 2 Set
     * Sunday in 3
     */
    var setSpecificDays = function(wkDayNbr) {
        // Set the Specific Days
        switch (wkDayNbr) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            // 1 - 5 = Weekdays
            CONSTANTS.schdPrefArr[0] = true;
            break;
        case 6:
            // 6 = Saturday
            CONSTANTS.schdPrefArr[1] = true;
            break;
        case 7:
            // 7 = Sunday
            CONSTANTS.schdPrefArr[2] = true;
            break;
        default:
            break;
        }
    };
    /*
     * Set scheduled prefrence as selected by user
     */
    var setSpecificTimes = function(daySegCd) {
        // Set the Specific Times. We no longer use 0 - Anytime,
        // therefore ignore it. However we will need it for old
        // Requisitions
        switch (daySegCd) {
        case 0:
            // 1 = Early AM
            CONSTANTS.schdPrefArr[3] = true;
            // 2 = Mornings
            CONSTANTS.schdPrefArr[4] = true;
            // 3 = Afternoon
            CONSTANTS.schdPrefArr[5] = true;
            // 4 = Nights
            CONSTANTS.schdPrefArr[6] = true;
            // 5 = Late Night
            CONSTANTS.schdPrefArr[7] = true;
            // 6 = Overnight
            CONSTANTS.schdPrefArr[8] = true;
            break;
        case 1:
            // 1 = Early AM
            CONSTANTS.schdPrefArr[3] = true;
            break;
        case 2:
            // 2 = Mornings
            CONSTANTS.schdPrefArr[4] = true;
            break;
        case 3:
            // 3 = Afternoon
            CONSTANTS.schdPrefArr[5] = true;
            break;
        case 4:
            // 4 = Nights
            CONSTANTS.schdPrefArr[6] = true;
            break;
        case 5:
            // 5 = Late Night
            CONSTANTS.schdPrefArr[7] = true;
            break;
        case 6:
            // 6 = Overnight
            CONSTANTS.schdPrefArr[8] = true;
            break;
        default:
            break;
        }
    };
    /*
     * Set scheduled preference days & time as selected by user
     */
    this.setSchedPref = function(settings) {
        if (settings.defaultChecked.length > 0) {
            CONSTANTS.schdPrefArr = [];
            for (var x = 0; x < 9; x++) {
                CONSTANTS.schdPrefArr[x] = false;
            }
            for (var i = 0; i < settings.defaultChecked.length; i++) {
                //Set Specific days and time
                setSpecificDays(parseInt(settings.defaultChecked[i].wkDayNbr));
                setSpecificTimes(parseInt(settings.defaultChecked[i].daySegCd));
            }
            for (var j = 0; j < CONSTANTS.schdPrefArr.length; j++) {
                if (CONSTANTS.schdPrefArr[j].toString() === "true") {
                    //Set schedule preference
                    $('#schdPref' + j).prop('checked', true);
                    $('#schdPref' + j).trigger('change');
                }
            }
        }
    };
    /*
     * Set scheduled preference days & time as array selected by user
     */
    this.setSchedulePrefArray = function(resultList) {
        if (resultList.length > 0) {
            CONSTANTS.schdPrefArr = [];
            for (var x = 0; x < 9; x++) {
                CONSTANTS.schdPrefArr[x] = false;
            }
            for (var i = 0; i < resultList.length; i++) {
                //Set Specific days and time
                setSpecificDays(parseInt(resultList[i].wkDayNbr));
                setSpecificTimes(parseInt(resultList[i].daySegCd));
            }
            return CONSTANTS.schdPrefArr;
        }
    };
    this.UTFEncodeSplChars = function(text) {
        text = JSON.stringify(text);
        var encodedText = text.split('%').join('\\u0025');
        encodedText = encodedText.split('&').join('\\u0026');
        encodedText = encodedText.split('+').join('\\u002B');
        return JSON.parse(encodedText);
    };
    this.validateChars = function(el, n, dateObj, temp) {
        var i = 0;
        var DECIMAL_DIGITS = "0123456789";
        n = dateObj.month.length;
        for (i = 0; i < n; i++) {
            temp = "" + dateObj.month.substring(i, i + 1);
            if (DECIMAL_DIGITS.indexOf(temp) === -1) {
                this.showTooltip(el, "The date contains invalid characters.");
                return true;
            }
        }
        n = dateObj.day.length;
        for (i = 0; i < n; i++) {
            temp = "" + dateObj.day.substring(i, i + 1);
            if (DECIMAL_DIGITS.indexOf(temp) === -1) {
                this.showTooltip(el, "The date contains invalid characters.");
                return true;
            }
        }
        n = dateObj.year.length;
        for (i = 0; i < n; i++) {
            temp = "" + dateObj.year.substring(i, i + 1);
            if (DECIMAL_DIGITS.indexOf(temp) === -1) {
                this.showTooltip(el, "The date contains invalid characters.");
                return true;
            }
        }
    };
    this.validateDay = function(el, dayRequired, dayNum, maxDay) {
        if (dayRequired && (dayNum > maxDay || dayNum < 1)) {
            this.showTooltip(el, "Enter a valid day for the month.");
            return true;
        }
    };
    this.validateYear = function(el, yearRequired, yearNum) {
        if (yearRequired && (yearNum > 9999 || yearNum < 0)) {
            this.showTooltip(el, "Enter a year between 0 and 9999.");
            return true;
        }
    };
    this.maxValueCalc = function(monthNum, yearNum) {
        var maxDay = 31;

        if (monthNum === 4 || monthNum === 6 || monthNum === 9 || monthNum === 11) {
            maxDay = 30;
        } else if (monthNum === 2) {
            if (yearNum % 4 > 0) {
                maxDay = 28;
            } else if (yearNum % 100 === 0 && yearNum % 400 > 0) {
                maxDay = 28;
            } else {
                maxDay = 29;
            }
        }
        return maxDay;
    };
    this.validateMonth = function(el, monthNum) {
        if (monthNum > 12 || monthNum < 1) {
            this.showTooltip(el, "Enter a month between 1 and 12.");
            return true;
        }
    };
    this.validateDateLength = function(el, len, inputFormat) {
        if (len > inputFormat.length || len + 2 < inputFormat.length) {
            this.showTooltip(el, "Type the date in the format. mm/dd/yyyy");
            return true;
        }
    };
    this.showTooltip  = function(el, msg) {
        $(el).addClass('redBorder');
        $(el).attr("title", msg);
        $(el).attr("data-original-title", msg);
        $(el).tooltip();
        $(el).tooltip('show');
    };
    this.validateCharAllowed = function(el, validInput, temp) {
        if (validInput.indexOf(temp) === -1) {
            this.showTooltip(el, "The date contains invalid characters.");
            return true;
        }
    };
    this.validateCharsFormat = function(el, allowedFormatChars, temp) {
        if (allowedFormatChars.indexOf(temp) === -1) {
            this.showTooltip(el, "The date contains invalid characters.");
            return;
        }
    };
    this.checkDateFormat = function(monthRequired, dayRequired, yearRequired, dateObj) {
        var valMonth = (monthRequired && dateObj.month === "");
        var valDay = (dayRequired && dateObj.day === "");
        var valYear = (yearRequired && dateObj.year === "");
        return (valMonth || valDay || valYear);
    };
    this.hideTooltip = function(el) {
        $(el).removeClass('redBorder');
        $(el).removeAttr("title");
        $(el).removeAttr("data-original-title");
        $(el).tooltip();
        $(el).tooltip('hide');
    };
    this.dateCompare = function(a, b) {
    	if (a == null && b == null) {
    		return 0;
    	}            

        if (a == null) {
        	return 1;
        }          

        if (b == null) {
        	return -1;
        }           

        var na = a.getTime();
        var nb = b.getTime();

        if (na < nb) {
        	return -1;
        }            

        if (na > nb) {
        	return 1;
        }            

        return 0;
    };
    this.isDST = function(date) {
    	return ((this.dateCompare(date, this.DSTStart(date)) > 0 ) &&
				(this.dateCompare(this.DSTEnd(date), date) > 0 ));
    };
    /**
	 *
	 * Calculates the start of DST for a year prior to new DST rules
	 *
	 * @param date	The year of the date to check
	 *
	 * @return date	The actual start of DST
	 */
	this.DSTStartBefore2007 = function(date) {
		// 1st Sunday in April
		if ( date.getFullYear() > 1986 ) {
			return DateUtils.dayOfWeekIterationOfMonth( 1, DateUtils.SUNDAY, new Date( date.getFullYear(), DateUtils.monthAsNumber( DateUtils.APRIL ) ) );
		} else if ( date.getFullYear() == 1975 ) {
			return new Date( date.getFullYear(), DateUtils.monthAsNumber( DateUtils.FEBRUARY ), 23 );
		} else if ( date.getFullYear() == 1974 ) {
			return new Date( date.getFullYear(), DateUtils.monthAsNumber( DateUtils.JANUARY ), 6 );
		// last week of April
		} else {
			return DateUtils.dayOfWeekIterationOfMonth( DateUtils.LAST, DateUtils.SUNDAY, new Date( date.getFullYear(), DateUtils.monthAsNumber( DateUtils.APRIL ) ) );
		}
	};
	
	/**
	 * @private
	 * 
	 * Calculates the end of DST for a year prior to new DST rules
	 * 
	 * @param date	The year of the date to check
	 * 
	 * @return date	The actual end of DST
	 */
	this.DSTEndBefore2007 = function( date ) {
		// last Sunday in October
		return DateUtils.dayOfWeekIterationOfMonth( DateUtils.LAST, DateUtils.SUNDAY, new Date( date.getFullYear(), DateUtils.monthAsNumber( DateUtils.OCTOBER ) ) );
	};
	
	/**
	 * Calculates the start of DST for a year
	 * 
	 * @param date	The year of the date to check
	 * 
	 * @return date	The actual start of DST
	 */
	this.DSTStart = function( date ) {
		var _localDate;
		if ( date.getFullYear() < 2007 ) {
			_localDate = this.DSTStartBefore2007( date );
		// 2nd Sunday in March
		} else {
			_localDate = DateUtils.dayOfWeekIterationOfMonth( 2, DateUtils.SUNDAY, new Date( date.getFullYear(), DateUtils.monthAsNumber( DateUtils.MARCH ) ) );
		}
		_localDate = new Date( _localDate.getFullYear(), _localDate.getMonth(), _localDate.getDate(), 2 );
		return _localDate;
	};
	
	/**
	 * Calculates the end of DST for a year
	 * 
	 * @param date	The year of the date to check
	 * 
	 * @return date	The actual end of DST
	 */
	this.DSTEnd = function( date ) {
		var _localDate;
		if ( date.getFullYear() < 2007 ) {
			_localDate = this.DSTEndBefore2007( date );
		// 1st Sunday in November
		} else {
			_localDate = DateUtils.dayOfWeekIterationOfMonth( 1, DateUtils.SUNDAY, new Date( date.getFullYear(), DateUtils.monthAsNumber( DateUtils.NOVEMBER ) ) );
		}
		_localDate = new Date( _localDate.getFullYear(), _localDate.getMonth(), _localDate.getDate(), 2 );
		return _localDate;
	};
	
	/*
	 * trim the Query Stirng in the URL
	 */
	this.RemoveQS = function() {
		try
		{
			// Check if the URL has Query string.
			if(window.location.href.split('?').length >1)
			{
				CONSTANTS.removeQSFromURL = true;
			}
		}
		catch(ex)
		{
			console.log("RSA: Something happen, Details " + ex);
		}
		
		
	};
	
	/*
	 * trim the Query Stirng in the URL
	 */
	this.RefreshURL = function() {
		try
		{
			// Check if the URL has Query string.
			if(window.location.href.split('?').length >1)
			{
				
				// HTML5 URL part manipulation
				// Remove the query string.
				if (history.pushState) {
				    var newurl = window.location.protocol + "//" + window.location.host + window.location.pathname + '';
				    window.history.pushState({path:newurl},'',newurl);
				}
				  
			}
		}
		catch(ex)
		{
			console.log("RSA: Something happen, Details " + ex);
		}
		
		
	};
	
	/*
	 * Clear the cahe from the retail staffing details page.
	 */
	this.clearCache = function() {
    	// reset the cache status
    	CONSTANTS.cacheRequisitionDetailsFlg = false;
    	CONSTANTS.cache_getRequisitionDetails ={};
    	CONSTANTS.cacheCurrentAPpage = 0;
    	CONSTANTS.cacheCurrentASpage = 0;
    	CONSTANTS.cache_getRetailStaffingViewApplicantPool = {};
    	CONSTANTS.cache_getRetailStaffingViewAssociatePool = {};
    	CONSTANTS.currentQProw =0;
    	CONSTANTS.qpFromApplicants = false;
    	CONSTANTS.qpFromAssociates = false;
    };
    
}

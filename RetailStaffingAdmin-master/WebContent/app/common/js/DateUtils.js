/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: DateUtils.js
 * Application: Retail Staffing Admin
 *
 */
var DateUtils = new DateUtils();
function DateUtils() {

	// Days of week
	this.MONDAY = "monday";
	this.TUESDAY = "tuesday";
	this.WEDNESDAY = "wednesday";
	this.THURSDAY = "thursday";
	this.FRIDAY = "friday";
	this.SATURDAY = "saturday";
	this.SUNDAY = "sunday";

	// Months of year
	this.JANUARY = "january";
	this.FEBRUARY = "february";
	this.MARCH = "march";
	this.APRIL = "april";
	this.MAY = "may";
	this.JUNE = "june";
	this.JULY = "july";
	this.AUGUST = "august";
	this.SEPTEMBER = "september";
	this.OCTOBER = "october";
	this.NOVEMBER = "november";
	this.DECEMBER = "december";

	// Date parts
	this.YEAR = "fullYear";
	this.MONTH = "month";
	this.WEEK = "week";
	this.DAY_OF_MONTH = "date";
	this.HOURS = "hours";
	this.MINUTES = "minutes";
	this.SECONDS = "seconds";
	this.MILLISECONDS = "milliseconds";
	this.DAY_OF_WEEK = "day";

	// Numeric value of "last", to get last item for a specific time
	this.LAST = -1;

	// Date masks
	this.SHORT_DATE_MASK = "MM/DD/YYYY";
	this.MEDIUM_DATE_MASK = "MMM D, YYYY";
	this.LONG_DATE_MASK = "MMMM D, YYYY";
	this.FULL_DATE_MASK = "EEEE, MMMM D, YYYY";

	// Time masks
	this.SHORT_TIME_MASK = "L:NN A";
	this.MEDIUM_TIME_MASK = "L:NN:SS A";
	// TZD = TimeZoneDesignation = GMT + or - X hours, non-standard, requires a slight hack
	this.LONG_TIME_MASK = this.MEDIUM_TIME_MASK + " TZD";

	// Internal values for using in date/time calculations
	this.SECOND_VALUE = 1000;
	this.MINUTE_VALUE = this.SECOND_VALUE * 60;
	this.HOUR_VALUE = this.MINUTE_VALUE * 60;
	this.DAY_VALUE = this.HOUR_VALUE * 24;
	this.WEEK_VALUE = this.DAY_VALUE * 7;
	
	// a generic object for holding day of the week values
	var _objDaysOfWeek = {};
	_objDaysOfWeek[ this.SUNDAY ]		= 0;
	_objDaysOfWeek[ this.MONDAY ]		= 1;
	_objDaysOfWeek[ this.TUESDAY ]		= 2;
	_objDaysOfWeek[ this.WEDNESDAY ]	= 3;
	_objDaysOfWeek[ this.THURSDAY ]	= 4;
	_objDaysOfWeek[ this.FRIDAY ]		= 5;
	_objDaysOfWeek[ this.SATURDAY ]	= 6;
	// a generic object for holding month values
	var _objMonth = {};
	_objMonth[ this.JANUARY ]		= 0;
	_objMonth[ this.FEBRUARY ]		= 1;
	_objMonth[ this.MARCH ]		= 2;
	_objMonth[ this.APRIL ]		= 3;
	_objMonth[ this.MAY ]			= 4;
	_objMonth[ this.JUNE ]			= 5;
	_objMonth[ this.JULY ]			= 6;
	_objMonth[ this.AUGUST ]		= 7;
	_objMonth[ this.SEPTEMBER ]	= 8;
	_objMonth[ this.OCTOBER ]		= 9;
	_objMonth[ this.NOVEMBER ]		= 10;
	_objMonth[ this.DECEMBER ]		= 11;
	
	/**
	 *
	 * This function will remove any invalid characters from the date/time mask based upon a pattern
	 * 
	 * @param mask			The string for matching
	 * @param pattern		The valid characters for this mask
	 * @param defaultValue	The default value to return to the calling page should the mask not match the pattern
	 * 
	 * @return				Returns a validated <code>mask</code> based upon the original pattern
	 */
	this.removeInvalidDateTimeCharacters = function( mask, pattern, defaultValue ) {
		// test for invalid date and time characters
		if ( mask.toString().replace( new RegExp( pattern, "ig" ), "" ).length > 0 ) {
			// if user is passing an invalid mask, default to defaultValue
			mask = defaultValue;
		}
		// temporarily replace TZD with lowercase tzd for replacing later
		return mask.replace( new RegExp( "TZD", "i" ), "tzd" );
	};
	
	/**
	 * Adds the specified number of "date parts" to a date, e.g. 6 days
	 * 
	 * @param datePart	The part of the date that will be added
	 * @param number	The total number of "dateParts" to add to the date
	 * @param date		The date on which to add
	 * 
	 * @return			The new date
	 */
	this.dateAdd = function( datePart, number, date ) {
		var _returnDate = new Date( date );
	
		switch ( datePart ) {
			case this.YEAR:
			case this.MONTH:
			case this.DAY_OF_MONTH:
			case this.HOURS:
			case this.MINUTES:
			case this.SECONDS:
			case this.MILLISECONDS:
				_returnDate.setDate(_returnDate.getDate() + number);
				break;
			case this.WEEK:
				_returnDate.setDate(_returnDate.getDate()+ (number * 7));
				break;
			default:
				/* Unknown date part, do nothing. */
				break;
		}
		return _returnDate;
	};
	
	/**
	 * Gets the day of the week
	 * 
	 * @param date	The date for which to get the day of the week
	 * 
	 * @return		A number representing the day of the week, 0 to 6
	 */
	this.dayOfWeek = function( date ) {
		return date.getDay();
	};
	
	/**
	 * Gets the ordinal value or day of the year
	 *
	 * @param date	The date for which to get the day of the year
	 * 
	 * @return		A number representing the day of the year, 1 to 365 or 366 for a leap year
	 */
	this.dayOfYear = function( date ) {
		// add one as it has to include first of year
		return this.dateDiff( this.DAY_OF_MONTH, new Date( date.getFullYear(), this.monthAsNumber( this.JANUARY ), 1 ), date ) + 1;
	};
	
	/**
	 * Gets the week of the year
	 * 
	 * @param date	The date for which to get the week of the year
	 * 
	 * @return		A number representing the week of the year, 1 to 53 ( as there are slightly more than 52 weeks of days in a year)
	 */
	this.weekOfYear = function( date ) {
		var _weekOne;
		// if date is 12/29 - 12/31
		if ( this.dateDiff( this.DAY_OF_MONTH, new Date( date.getFullYear(), this.monthAsNumber( this.DECEMBER ), 29 ), date ) >= 0 ) {
			// add 1 year
			_weekOne = this.getISOWeekOne( date.getFullYear() + 1 );
			// if date is before weekOne
			if ( this.dateDiff( this.DAY_OF_MONTH, date, _weekOne ) > 0 ) {
				// use year of date argument
				_weekOne = this.getISOWeekOne( date.getFullYear() );
			}
		} else {
			// get iso week
			_weekOne = this.getISOWeekOne( date.getFullYear() );
			// if date is before weekOne
			if ( this.dateDiff( this.DAY_OF_MONTH, date, _weekOne ) > 0 ) {
				// subtract 1 year
				_weekOne = this.getISOWeekOne( date.getFullYear() - 1 );
			}
		}
		return Math.floor( ( this.dateDiff( this.DAY_OF_MONTH, _weekOne, date ) / 7 ) + 1 );
	};
	
	/**
	 *
	 * Gets the first iso week of the year
	 * 
	 * @param date	The date for which to get the first week of the year
	 * 
	 * @return		The date of the Monday of the first week of the year
	 */
	this.getISOWeekOne = function( isoYear ) {
		var _januaryFourth = new Date( isoYear, this.monthAsNumber( this.JANUARY ), 4 );
		var _dayNumber = this.dayOfWeek( _januaryFourth );
		if ( _dayNumber == 0 ) {
			_dayNumber = 7;
		}
		return this.dateAdd( this.DAY_OF_MONTH, ( 1 - _dayNumber ), _januaryFourth );
	};
	
	/**
	 * Converts the day of the week to a Flex day of the week
	 * 
	 * @param date	The human readable day of week
	 * 
	 * @return		The Flex converted day of week or 0 aka Sunday
	 */
	this.toFlexDayOfWeek = function( localDayOfWeek ) {
		return ( localDayOfWeek > 0 && localDayOfWeek < 8 ) ? localDayOfWeek - 1 : 0;
	};
	
	/**
	 * Gets the Xth day of the month.
	 * e.g. get the 3rd Wednesday of the month
	 * 
	 * @param iteration		The iteration of the month to get e.g. 4th or Last
	 * @param strDayOfWeek	The day of the week as a string
	 * @param date			The date containing the month and year
	 * 
	 * @return				The date of the xth dayOfWeek of the month 
	 */
	this.dayOfWeekIterationOfMonth = function( iteration, strDayOfWeek, date ) {
		// get the numeric day of the week for the requested day
		var _dayOfWeek = this.dayOfWeekAsNumber( strDayOfWeek );
		// get the date for the first of the month
		var _firstOfMonth = new Date( date.getFullYear(), date.getMonth(), 1 );
		// calculate how many days to add to get to the requested day from the first of the month
		var _daysToAdd = _dayOfWeek - this.dayOfWeek( _firstOfMonth );
		// if dayOfWeek is before the first of the month, get the dayOfWeek for the following week
		if ( _daysToAdd < 0 ) {
			_daysToAdd += 7;
		}
		// set the date to the first day of the week for the requested date
		var _firstDayOfWeekOfMonth = this.dateAdd( this.DAY_OF_MONTH, _daysToAdd, _firstOfMonth );
		// return the date if iteration is 1
		if ( iteration == 1 ) {
			return _firstDayOfWeekOfMonth;
		} else {
			// if requesting an iteration that is more than is in that month or requesting the last day of week of month
			// return last date for that day of week of month
			if ( ( this.totalDayOfWeekInMonth( strDayOfWeek, date ) < iteration ) || ( iteration == this.LAST ) ) {
				iteration = this.totalDayOfWeekInMonth( strDayOfWeek, date );
			}
			// subtract 1 as it starts from the first dayOfWeek of month
			return this.dateAdd( this.WEEK, iteration - 1, _firstDayOfWeekOfMonth );
		}
	};
	
	/**
	 * Gets the days in the month
	 * 
	 * @param date	The date to check
	 * 
	 * @return		The number of days in the month
	 */
	this.daysInMonth = function( date ) {
		// as only need month...reset date to first day of month requested to prevent miscalculations
		// when adding 1 month to "date" parameter
		// e.g. someone passes last day of 31 day month, dateAdd could add too many days
		date = new Date( date.getFullYear(), date.getMonth(), 1 );
		// get the first day of the next month
		var _localDate = new Date( date.getFullYear(), this.dateAdd( this.MONTH, 1, date ).getMonth(), 1 );
		// subtract 1 day to get the last day of the requested month
		return this.dateAdd( this.DAY_OF_MONTH, -1, _localDate ).getDate();
	};
	
	/**
	 * Gets the total number of dayOfWeek in the month
	 * 
	 * @param strDayOfWeek	The day of week to check
	 * @param date			The date containing the month and year
	 * 
	 * @return				The number of <code>strDayOfWeek</code> in that month and year
	 */
	this.totalDayOfWeekInMonth = function( strDayOfWeek, date ) {
		var _startDate = this.dayOfWeekIterationOfMonth( 1, strDayOfWeek, date );
		var _totalDays = this.dateDiff( this.DAY_OF_MONTH, _startDate, new Date( date.getFullYear(), date.getMonth(), this.daysInMonth( date ) ) );
		// have to add 1 because have to include first day that is found i.e. if wed is on 2nd of 31 day month, would total 5, of if wed on 6th, would total 4
		return Math.floor( _totalDays / 7 ) + 1;
	};
	
	/**
	 * Converts the month to a Flex month
	 * 
	 * @param date	The human readable month
	 * 
	 * @return		The Flex converted month or 0 aka January
	 */
	this.toFlexMonth = function( localMonth ) {
		return ( localMonth > 0 && localMonth < 13 ) ? localMonth - 1 : 0;
	};
	
	/**
	 * Formats a date to the numeric version of the day of the week
	 * 
	 * @param strDayOfWeek	The day of week to convert
	 * 
	 * @return				A formatted day of week or -1 if day not found
	 */
	this.dayOfWeekAsNumber = function( strDayOfWeek ) {
		return ( _objDaysOfWeek[ strDayOfWeek ] >= 0 ) ? _objDaysOfWeek[ strDayOfWeek ] : -1;
	};
	
	/**
	 * Formats a month to the numeric version of the month
	 * 
	 * @param strMonth	The month to convert
	 * 
	 * @return			A formatted month or -1 if month not found
	 */
	this.monthAsNumber = function( strMonth ) {
		return ( _objMonth[ strMonth ] >= 0 ) ? _objMonth[ strMonth ] : -1;
	};
	
	/**
	 *
	 * Determines the number of weeks in a given month, including partial weeks
	 *
	 * @param date        The date to check
	 *
	 * @return            Returns the number of weeks that a month contains, either 4, 5, or 6 depending on how the days fall
	 *
	 */
	this.weeksInMonth = function( date ) {
		var _result = 0;
	
		var _daysInMonth = this.daysInMonth( date );
		// find the number of days in the first week
		var _daysInFirstWeek = 7 - ( this.dayOfWeek( new Date( date.getFullYear(), date.getMonth(), 1 ) ) );
	
		// ( daysInMonth - daysInFirstWeek ) % DaysInWeek = number of days left over
		var _daysInLastWeek = ( ( _daysInMonth - _daysInFirstWeek ) % 7 );
	
		// find the number of full weeks in the month
		_result += Math.floor( ( ( _daysInMonth - _daysInFirstWeek ) / 7 ) ) + 1; // number of full weeks in month + 1 for leading days

		// if daysInLastWeek is 0 the the modulus gave no remainder which means last week was a full week and it was already counted do nothing,
		// otherwise add 1
		if ( _daysInLastWeek != 0 ) {
			_result += 1;
		}

		return _result;
	};
	
	/**
	 * Gets the number of days in the year
	 * 
	 * @param date	The date to check
	 * 
	 * @return		The total number of days in the year
	 */
	this.daysInYear = function( date ) {
		return this.dateDiff(
					this.DAY_OF_MONTH,
					new Date( date.getFullYear(), this.monthAsNumber( this.JANUARY ), 1 ),
					this.dateAdd( this.YEAR, 1, new Date( date.getFullYear(), this.monthAsNumber( this.JANUARY ), 1 ) ) );
	};
	
	/**
	 * Determines whether the year is a leap year or not
	 * 
	 * @param date	The date to check
	 * 
	 * @return		<code>true</code> means it is a leap year, <code>false</code> means it is not a leap year.
	 */
	this.isLeapYear = function( date ) {
		return this.daysInYear( date ) > 365;
	};
	
	/**
	 * Determines the number of "dateParts" difference between 2 dates
	 * 
	 * @param datePart	The part of the date that will be checked
	 * @param startDate	The starting date
	 * @param endDate	The ending date
	 * 
	 * @return			The number of "dateParts" difference
	 */
	this.dateDiff = function( datePart, startDate, endDate ) {
		var _returnValue = 0;

		switch ( datePart ) {
			case this.MILLISECONDS:
				var _adjustment = ( startDate.getTimezoneOffset() - endDate.getTimezoneOffset() ) * 60 * 1000;
				_returnValue = endDate.getTime() - startDate.getTime() + _adjustment;
				break;
			case this.SECONDS:
				_returnValue = Math.floor( this.dateDiff( this.MILLISECONDS, startDate, endDate ) / this.SECOND_VALUE );
				break;
			case this.MINUTES:
				_returnValue = Math.floor( this.dateDiff( this.MILLISECONDS, startDate, endDate ) / this.MINUTE_VALUE );
				break;
			case this.HOURS:
				_returnValue = Math.floor( this.dateDiff( this.MILLISECONDS, startDate, endDate ) / this.HOUR_VALUE );
				break;
			case this.DAY_OF_MONTH:
				_returnValue = Math.floor( this.dateDiff( this.MILLISECONDS, startDate, endDate ) / this.DAY_VALUE );
				break;
			case this.WEEK:
				_returnValue = Math.floor( this.dateDiff( this.MILLISECONDS, startDate, endDate ) / this.WEEK_VALUE );
				break;
			case this.MONTH:
				// if start date is after end date, switch values and take inverse of return value
				if ( this.dateDiff( this.MILLISECONDS, startDate, endDate ) < 0 ) {
					_returnValue -= this.dateDiff( this.MONTH, endDate, startDate );
				} else {
					 // get number of months based upon years
					_returnValue = this.dateDiff( this.YEAR, startDate, endDate ) * 12;
					// subtract months then perform test to verify whether to subtract one month or not
					// the check below gets the correct starting number of months (but may need to have one month removed after check)
					if ( endDate.getMonth() != startDate.getMonth() ) {
						_returnValue += ( endDate.getMonth() <= startDate.getMonth() ) ? 12 - startDate.getMonth() + endDate.getMonth() : endDate.getMonth() - startDate.getMonth();
					// else they are the same, but the years are potentially not the same
					} else if ( ( startDate.getFullYear() != endDate.getFullYear() ) && ( endDate.getUTCDate() < startDate.getUTCDate() ) ) {
						_returnValue += 12;
					}
					// have to perform same checks as YEAR
					// i.e. if end date day is <= start date day, and end date milliseconds < start date milliseconds
					if ( ( endDate.getDate() < startDate.getDate() ) ||
							( endDate.getDate() == startDate.getDate() &&
								endDate.getMilliseconds() < startDate.getMilliseconds() ) ) {
						_returnValue -= 1;
					}
				}
				break;
			case this.YEAR:
				_returnValue = endDate.getFullYear() - startDate.getFullYear();
				// this fixes the previous problem with dates that ran into 2 calendar years
				// previously, if 2 dates were in separate calendar years, but the months were not > 1 year apart, then it was returning too many years
				// e.g. 11/2008 to 2/2009 was returning 1, but should have been returning 0 (zero)
				// if start date before end date and months are less than 1 year apart, add 1 to year to fix offset issue
				// if end date before start date and months are less than 1 year apart, subtract 1 year to fix offset issue
				// added month and milliseconds check to make sure that a date that was e.g. 9/11/07 9:15AM would not return 1 year if the end date was 9/11/08 9:14AM
				if ( _returnValue != 0 ) {
					// if start date is after end date
					if ( _returnValue < 0 ) {
						// if end date month is >= start date month, and end date day is >= start date day, and end date milliseconds > start date milliseconds
						if ( ( endDate.getMonth() > startDate.getMonth() ) ||
								( endDate.getMonth() == startDate.getMonth() && endDate.getDate() > startDate.getDate() ) ||
								( endDate.getMonth() == startDate.getMonth() && endDate.getDate() == startDate.getDate() &&
									endDate.getMilliseconds() > startDate.getMilliseconds() ) ) {
							_returnValue += 1;
						}
					} else {
						// if end date month is <= start date month, and end date day is <= start date day, and end date milliseconds < start date milliseconds
						if ( ( endDate.getMonth() < startDate.getMonth() ) ||
								( endDate.getMonth() == startDate.getMonth() && endDate.getDate() < startDate.getDate() ) ||
								( endDate.getMonth() == startDate.getMonth() && endDate.getDate() == startDate.getDate() &&
									endDate.getMilliseconds() < startDate.getMilliseconds() ) ) {
							_returnValue -= 1;
						}
					}
				}
				break;		
		}

		return _returnValue;
		
	};
	

};
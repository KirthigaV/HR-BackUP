/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: util.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.DateTO;
import com.homedepot.hr.hr.retailstaffing.dto.ExceptionTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.ErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.GenericErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.POMRsaStatusCrossRefResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * This class consists f methods which will be used commonly
 * 
 * @author 209546
 * 
 */
public class Util implements RetailStaffingConstants
{
	private final static Logger logger = Logger.getLogger(Util.class);

	/**
	 * This method logs the error
	 * 
	 * @param prexp
	 */
	public static void logError(String message, RetailStaffingException dexp)
	{
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append("\n There was an exception at time :: ");
		errorMsg.append(dexp.getTimestamp());
		errorMsg.append("\n Class name :: ");
		errorMsg.append(dexp.getClassName());
		errorMsg.append("\n Method name :: ");
		errorMsg.append(dexp.getMethodName());
		if(dexp.getTechnicalErrorMessage() != null)
		{
			errorMsg.append("\n Error Details :: ");
			errorMsg.append(dexp.getTechnicalErrorMessage());
		}
		if(dexp.getRootCauseException() != null)
		{
			logger.error(new ClientApplLogger(message + errorMsg.toString() + dexp.getRootCauseException().getMessage()), dexp.getRootCauseException());
		}
		else
		{
			logger.error(new ClientApplLogger(message + errorMsg.toString()));
		}
	}

	/**
	 * This method logs the fatal error
	 * 
	 * @param prexp
	 */
	public static void logFatalError(String message, Exception e)
	{
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append(RetailStaffingConstants.RETURN_MESSAGE_FATAL_ERROR);
		errorMsg.append("at time :: ");
		errorMsg.append(getCurrentTimestamp().toString());
		errorMsg.append(e.toString());
		logger.fatal(new ClientApplLogger(message + errorMsg.toString()), e);
	}

	/**
	 * This method logs the fatal error
	 * 
	 * @param prexp
	 */
	public static void logFatalError(String message, RetailStaffingException dexp)
	{
		StringBuilder errorMsg = new StringBuilder();
		errorMsg.append("\n There was an exception at time :: ");
		errorMsg.append(dexp.getTimestamp());
		errorMsg.append("\n Class name :: ");
		errorMsg.append(dexp.getClassName());
		errorMsg.append("\n Method name :: ");
		errorMsg.append(dexp.getMethodName());
		if(dexp.getTechnicalErrorMessage() != null)
		{
			errorMsg.append("\n Error Details :: ");
			errorMsg.append(dexp.getTechnicalErrorMessage());
		}
		if(dexp.getRootCauseException() != null)
		{
			if(dexp.getErrorCode() != RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE
			    && dexp.getErrorCode() != RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE
			    && dexp.getErrorCode() != RetailStaffingConstants.INVALID_STORE_ERROR_CODE)
			{
				logger.fatal(new ClientApplLogger(message + errorMsg.toString() + dexp.getRootCauseException().getMessage()), dexp.getRootCauseException());
			}

		}
		else
		{
			if(dexp.getErrorCode() != RetailStaffingConstants.NO_RECORDS_FOUND_ERROR_CODE
			    && dexp.getErrorCode() != RetailStaffingConstants.NO_FURTHER_RECORDS_FOUND_ERROR_CODE
			    && dexp.getErrorCode() != RetailStaffingConstants.INVALID_STORE_ERROR_CODE)
			{
				logger.fatal(new ClientApplLogger(message + errorMsg.toString()));
			}
		}
	}

	/**
	 * This method returns the current timestamp.
	 * 
	 * @return Timestamp
	 */
	public static Timestamp getCurrentTimestamp()
	{
		Timestamp currTimestamp = null;
		String msgDataVal = null;
		try
		{
			currTimestamp = new Timestamp(System.currentTimeMillis());
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in Util.getCurrentTimestamp()" + e.getMessage();
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_PARSE_DATE));
		}
		return currTimestamp;
	}

	/**
	 * The method is used for converting the given time stamp string to sql time
	 * stamp object.
	 * 
	 * @param timeStampString
	 *            - the given time stamp string in yyyyMMddhhmmssSSS format.
	 * @return
	 */
	public static java.sql.Timestamp convertTimestampTO(TimeStampTO timeStampTO)
	{
		java.sql.Timestamp timestamp = null;
		StringBuilder timeStampStringBuf = null;
		String msgDataVal = null;
		try
		{
			if(timeStampTO != null && timeStampTO.getYear() != null && timeStampTO.getMonth() != null && timeStampTO.getDay() != null)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FORMAT);
				timeStampStringBuf = new StringBuilder();
				timeStampStringBuf.append(timeStampTO.getYear());
				timeStampStringBuf.append("-");
				timeStampStringBuf.append(timeStampTO.getMonth());
				timeStampStringBuf.append("-");
				timeStampStringBuf.append(timeStampTO.getDay());

				if(timeStampTO.getHour() != null)
				{
					timeStampStringBuf.append("-");
					timeStampStringBuf.append(timeStampTO.getHour());

				}
				else
				{
					timeStampStringBuf.append("-");
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getMinute() != null)
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append(timeStampTO.getMinute());

				}
				else
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getSecond() != null)
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append(timeStampTO.getSecond());

				}
				else
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getMilliSecond() != null)
				{
					timeStampStringBuf.append(".");
					timeStampStringBuf.append(timeStampTO.getMilliSecond());

				}
				else
				{
					timeStampStringBuf.append(".");
					timeStampStringBuf.append("000");

				}
				logger.info("The timestamp string obtained is:" + timeStampStringBuf.toString());
				java.util.Date parsedDate = dateFormat.parse(timeStampStringBuf.toString());
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
				logger.info("The converted timestamp is:" + timestamp);
			}
		}
		catch (ParseException e)
		{
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_PARSE_TIMESTAMP));
		}
		return timestamp;
	}

	/**
	 * The method will be used for creating a date object for the given date
	 * string.
	 * 
	 * @param DateTO
	 *            - the given object containing the date details. format.
	 * @return
	 */
	public static java.sql.Date convertDateTO(DateTO dateTO)
	{
		java.sql.Date date = null;
		StringBuilder dateStringBuf = null;
		String msgDataVal = null;
		try
		{
			if(dateTO != null && dateTO.getYear() != null && dateTO.getMonth() != null && dateTO.getDay() != null)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				dateStringBuf = new StringBuilder();
				dateStringBuf.append(dateTO.getYear());
				dateStringBuf.append("-");
				dateStringBuf.append(dateTO.getMonth());
				dateStringBuf.append("-");
				dateStringBuf.append(dateTO.getDay());
				logger.info("The date string obtained is:" + dateStringBuf.toString());
				java.util.Date parsedDate = dateFormat.parse(dateStringBuf.toString());
				date = new java.sql.Date(parsedDate.getTime());
				logger.info("The converted date is:" + date);
			}
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in Util.convertDateTO()";
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_PARSE_DATE));
		}
		return date;
	}

	public static TimeStampTO converTimeStampTimeStampTO(java.sql.Timestamp timestamp)
	{
		TimeStampTO timeStampTO = null;
		if(timestamp != null)
		{
			timeStampTO = new TimeStampTO();
			logger.info("The timestamp obtained is:" + timestamp.toString());
			String timeStampString = timestamp.toString();
			String[] timeStampToks = timeStampString.split(" ");
			logger.info("The token length is" + timeStampToks.length);

			if(timeStampToks != null && timeStampToks.length >= 2)
			{
				String datePart = timeStampToks[0];
				String timePart = timeStampToks[1];
				if(datePart != null)
				{
					String[] dateToks = datePart.split("-");
					if(dateToks != null && dateToks.length >= 3)
					{
						timeStampTO.setYear(dateToks[0]);
						timeStampTO.setMonth(dateToks[1]);
						timeStampTO.setDay(dateToks[2]);
					}
				}
				if(timePart != null)
				{
					String[] timeToks = timePart.split(":");
					if(timeToks != null && timeToks.length >= 3)
					{
						timeStampTO.setHour((timeToks[0]));
						timeStampTO.setMinute(timeToks[1]);
						logger.info("The timeToks[2] is" + timeToks[2]);
						int sec = timeToks[2].indexOf(".");
						if(sec > 0)
						{
							timeStampTO.setSecond(timeToks[2].substring(0, sec));
							if(sec + 1 < timeToks[2].length())
								timeStampTO.setMilliSecond(timeToks[2].substring(sec + 1, timeToks[2].length()));
						}
					}
				}
			}

			logger.info("The converted timestampTo is:" + timeStampTO.toString());

		}

		return timeStampTO;

	}

	public static DateTO converTimeStampTOtoDateTO(java.sql.Timestamp timestamp)
	{
		DateTO dateTO = null;
		String msgDataVal = null;
		try
		{
			if(timestamp != null)
			{
				dateTO = new DateTO();
				logger.info("The timestamp obtained is:" + timestamp.toString());
				String timeStampString = timestamp.toString();
				String[] timeStampToks = timeStampString.split(" ");
				logger.info("The token length is" + timeStampToks.length);

				if(timeStampToks != null && timeStampToks.length >= 2)
				{
					String datePart = timeStampToks[0];
					// String timePart = timeStampToks[1];
					if(datePart != null)
					{
						String[] dateToks = datePart.split("-");
						if(dateToks != null && dateToks.length >= 3)
						{
							dateTO.setYear(dateToks[0]);
							dateTO.setMonth(dateToks[1]);
							dateTO.setDay(dateToks[2]);
						}
					}

				}

				logger.info("The converted timestampTo is:" + dateTO.toString());

			}
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in Util.converTimeStampTOtoDateTO()";
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_PARSE_TIMESTAMP));
		}
		return dateTO;

	}

	public static TimeStampTO converTimeTimeStampTO(java.sql.Time time)
	{
		TimeStampTO timeStampTO = null;

		if(time != null)
		{
			logger.info("The time obtained is:" + time.toString());
			String timeString = time.toString();
			String[] timeStampToks = timeString.split(":");
			if(timeStampToks != null && timeStampToks.length >= 2)
			{
				timeStampTO = new TimeStampTO();
				timeStampTO.setHour(timeStampToks[0]);
				timeStampTO.setMinute(timeStampToks[1]);
				if(timeStampToks[2] != null)
					timeStampTO.setSecond(timeStampToks[2]);
				logger.info("The converted timestampTo is:" + timeStampTO.toString());
			}

		}

		return timeStampTO;

	}

	public static java.sql.Time convertTimestampTOtoTime(TimeStampTO timeStampTO)
	{
		java.sql.Time time = null;
		StringBuilder timeStampStringBuf = null;
		GregorianCalendar gc = null;
		String msgDataVal = null;
		try
		{
			if(timeStampTO != null)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
				gc = new GregorianCalendar();
				gc.setTime(new java.util.Date());
				timeStampStringBuf = new StringBuilder();
				timeStampStringBuf.append(gc.get(Calendar.YEAR));
				timeStampStringBuf.append("-");
				timeStampStringBuf.append(gc.get(Calendar.MONTH));
				timeStampStringBuf.append("-");
				timeStampStringBuf.append(gc.get(Calendar.DAY_OF_MONTH));
				timeStampStringBuf.append("-");
				if(timeStampTO.getHour() != null)
				{
					timeStampStringBuf.append(timeStampTO.getHour());

				}
				else
				{
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getMinute() != null)
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append(timeStampTO.getMinute());

				}
				else
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getSecond() != null)
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append(timeStampTO.getSecond());

				}
				else
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getMilliSecond() != null)
				{
					timeStampStringBuf.append(".");
					timeStampStringBuf.append(timeStampTO.getMilliSecond());

				}
				else
				{
					timeStampStringBuf.append(".");
					timeStampStringBuf.append("000");

				}
				logger.info("The timestamp string obtained is:" + timeStampStringBuf.toString());
				java.util.Date parsedDate = dateFormat.parse(timeStampStringBuf.toString());
				time = new java.sql.Time(parsedDate.getTime());
				logger.info("The converted time is:" + time);
			}
		}
		catch (ParseException e)
		{
			msgDataVal = "Exception : Error occured in Util.convertTimestampTOtoTime()";
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_PARSE_TIMESTAMP));
		}
		return time;
	}

	public static java.sql.Timestamp convertTimestampTOtoTimestamp(TimeStampTO timeStampTO)
	{
		java.sql.Timestamp time = null;
		StringBuilder timeStampStringBuf = null;
		String msgDataVal = null;
		try
		{
			if(timeStampTO != null)
			{
				timeStampStringBuf = new StringBuilder();
				timeStampStringBuf.append(timeStampTO.getYear());
				timeStampStringBuf.append("-");
				timeStampStringBuf.append(timeStampTO.getMonth());
				timeStampStringBuf.append("-");
				timeStampStringBuf.append(timeStampTO.getDay());
				timeStampStringBuf.append(" ");
				if(timeStampTO.getHour() != null)
				{
					timeStampStringBuf.append(timeStampTO.getHour());

				}
				else
				{
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getMinute() != null)
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append(timeStampTO.getMinute());

				}
				else
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getSecond() != null)
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append(timeStampTO.getSecond());

				}
				else
				{
					timeStampStringBuf.append(":");
					timeStampStringBuf.append("00");

				}
				if(timeStampTO.getMilliSecond() != null)
				{
					timeStampStringBuf.append(".");
					timeStampStringBuf.append(timeStampTO.getMilliSecond());

				}
				else
				{
					timeStampStringBuf.append(".");
					timeStampStringBuf.append("000");

				}
				logger.info("The timestamp string obtained is:" + timeStampStringBuf.toString());
				time = java.sql.Timestamp.valueOf(timeStampStringBuf.toString());
				logger.info("The converted time is:" + time);
			}
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in Util.convertTimestampTOtoTimestamp()";
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_PARSE_TIMESTAMP));
		}
		return time;
	}

	public static DateTO converDatetoDateTO(java.sql.Date date)
	{
		DateTO dateTO = null;

		if(date != null)
		{
			dateTO = new DateTO();

			String[] dateToks = date.toString().split("-");
			if(dateToks != null && dateToks.length >= 3)
			{
				dateTO.setYear(dateToks[0]);
				dateTO.setMonth(dateToks[1]);
				dateTO.setDay(dateToks[2]);
			}
			logger.info("The converted dateTo is:" + dateTO.toString());
		}

		return dateTO;

	}

	/**
	 * The method will return boolean value for a given string.
	 * 
	 * @param status
	 *            - the string for which the boolean value is to be returned.
	 *            The String should have value as 'Y' or 'N'.
	 * @return
	 */
	public static boolean convertToBoolean(String status)
	{
		boolean result = false;
		String msgDataVal = null;
		try
		{
			if(status != null && status.equalsIgnoreCase(TRUE))
			{
				result = true;
			}
			else if(status != null && status.equalsIgnoreCase(FALSE))
			{
				result = false;
			}
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in Util.convertToBoolean()";
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_CONVERT_BOOLEAN));
		}
		return result;
	}

	public static String getProcessStatuses()
	{
		String statuses = null;
		statuses = PropertyReader.getInstance().getProperty("processIDs");
		return statuses;

	}

	/**
	 * This method creates the candidate name in the format lastname ,firstname
	 * ,middleInitialname ,suffixname.
	 * 
	 * @param lastName
	 * @param firstName
	 * @param middleInitialName
	 * @param suffixName
	 * @return
	 */
	public static String getCandidateName(String lastName, String firstName, String middleInitialName, String suffixName)
	{
		String name = null;
		StringBuilder nameBuf = new StringBuilder();
		if(lastName != null && lastName.trim().length() > 0)
		{
			nameBuf.append(lastName.trim());
			nameBuf.append(",");

		}
		if(firstName != null && firstName.trim().length() > 0)
		{
			nameBuf.append(SPACE_STRING);
			nameBuf.append(firstName.trim());
			nameBuf.append(",");

		}
		if(middleInitialName != null && middleInitialName.trim().length() > 0)
		{
			nameBuf.append(SPACE_STRING);
			nameBuf.append(middleInitialName.trim());
			nameBuf.append(",");
		}
		if(suffixName != null && suffixName.trim().length() > 0)
		{
			nameBuf.append(SPACE_STRING);
			nameBuf.append(suffixName.trim());

		}

		// The below logic is for trimming ',' character provided it is the last
		// character
		if(nameBuf != null && nameBuf.length() > 0 && (nameBuf.lastIndexOf(",") + 1) == nameBuf.length())
		{
			name = nameBuf.substring(0, nameBuf.lastIndexOf(","));
		}
		else if(nameBuf != null && nameBuf.length() > 0)
		{
			name = nameBuf.toString();
		}
		return name;
	}

	/**
	 * This method creates the candidate name in the format firstname
	 * ,middleInitialname, LastName ,suffixname.
	 * 
	 * @param lastName
	 * @param firstName
	 * @param middleInitialName
	 * @param suffixName
	 * @return
	 */
	public static String combineCandidateName(String lastName, String firstName, String middleInitialName, String suffixName)
	{
		StringBuilder nameBuf = new StringBuilder();

		if(firstName != null && firstName.trim().length() > 0)
		{
			nameBuf.append(firstName.trim());
			nameBuf.append(SPACE_STRING);

		}
		if(middleInitialName != null && middleInitialName.trim().length() > 0)
		{
			nameBuf.append(middleInitialName.trim());
			nameBuf.append(SPACE_STRING);
		}
		if(lastName != null && lastName.trim().length() > 0)
		{
			nameBuf.append(lastName.trim());

		}
		if(suffixName != null && suffixName.trim().length() > 0)
		{
			nameBuf.append(SPACE_STRING);
			nameBuf.append(suffixName.trim());

		}

		return nameBuf.toString();
	}
	
	/**
	 * This method will be used for returning the day of the week string value.
	 * 
	 * @param int - Day Number
	 * @return String - Day Name
	 */
	public static String getWeekDayName(int weekDayNum)
	{
		String dayName = null;
		switch(weekDayNum)
		{
			case 1:
				dayName = "Mon";
				break;
			case 2:
				dayName = "Tue";
				break;
			case 3:
				dayName = "Wed";
				break;
			case 4:
				dayName = "Thr";
				break;
			case 5:
				dayName = "Fri";
				break;
			case 6:
				dayName = "Sat";
				break;
			case 7:
				dayName = "Sun";
				break;
		}

		return dayName;
	}

	/**
	 * This method will be used for getting the next Interview TimeStamp
	 * 
	 * @param beginTimeStamp
	 *            - The interview Begin TimeStamp.
	 * @param minutes
	 *            - Additional minutes
	 * @return Timestamp - The next interview TimeStamp
	 */
	public static Timestamp getInterviewTimeStampByMinutes(Timestamp beginTimeStamp, int minutes)
	{
		Calendar blockCal = Calendar.getInstance();
		// initialize our calendar object to the beginning time of the block
		blockCal.setTimeInMillis(beginTimeStamp.getTime());
		// increment the begin by the duration provided
		blockCal.add(Calendar.MINUTE, minutes);

		Timestamp nextTimeStamp = new Timestamp(blockCal.getTimeInMillis());
		return nextTimeStamp;
	}

	/**
	 * This method will be used for getting the next Interview TimeStamp
	 * 
	 * @param beginTimeStamp
	 *            - The interview Begin TimeStamp.
	 * @param hours
	 *            - Additional hours
	 * @return Timestamp - The next interview TimeStamp
	 */
	public static Timestamp getInterviewTimeStampByHours(Timestamp beginTimeStamp, int hours)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Enter Util - getInterviewTimeStampByHours( )");
			logger.debug("beginTimeStamp :" + beginTimeStamp);
			logger.debug("Additional hours :" + hours);
		}

		Calendar blockCal = Calendar.getInstance();
		// initialize our calendar object to the beginning time of the block
		blockCal.setTimeInMillis(beginTimeStamp.getTime());
		// increment the begin by the duration provided
		blockCal.add(Calendar.HOUR, hours);
		Timestamp nextTimeStamp = new Timestamp(blockCal.getTimeInMillis());

		if(logger.isDebugEnabled())
		{
			logger.debug("Next Interview TimeStamp :" + nextTimeStamp);
			logger.debug("Exit Util - getInterviewTimeStampByHours( )");
		}

		return nextTimeStamp;
	}

	/**
	 * This method will be used for getting the next Interview TimeStamp
	 * 
	 * @param beginTimeStamp
	 *            - The interview Begin TimeStamp.
	 * @param hours
	 *            - Additional hours
	 * @return Timestamp - The next interview TimeStamp
	 */
	public static Timestamp getInterviewTimeStampByHoursAndMinutes(Timestamp beginTimeStamp, int hours, int minutes)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("Enter Util - getInterviewTimeStampByHours( )");
			logger.debug("beginTimeStamp :" + beginTimeStamp);
			logger.debug("Additional hours :" + hours);
			logger.debug("Additional Minutes :" + minutes);
		}

		Calendar blockCal = Calendar.getInstance();
		// initialize our calendar object to the beginning time of the block
		blockCal.setTimeInMillis(beginTimeStamp.getTime());
		// increment the begin by the duration provided
		blockCal.add(Calendar.HOUR, hours);
		blockCal.add(Calendar.MINUTE, minutes);
		Timestamp nextTimeStamp = new Timestamp(blockCal.getTimeInMillis());

		if(logger.isDebugEnabled())
		{
			logger.debug("Next Interview TimeStamp :" + nextTimeStamp);
			logger.debug("Exit Util - getInterviewTimeStampByHours( )");
		}

		return nextTimeStamp;
	}

	public static String getNoInterviewReasons()
	{
		String statuses = null;
		statuses = PropertyReader.getInstance().getProperty("intvwNotCmpltReasonList");
		return statuses;

	}

	public static String getOfferMade()
	{
		String statuses = null;
		statuses = PropertyReader.getInstance().getProperty("offerMadeList");
		return statuses;

	}

	public static String getOfferResult()
	{
		String statuses = null;
		statuses = PropertyReader.getInstance().getProperty("offerResultList");
		return statuses;

	}

	public static String getOfferDeclineReason()
	{
		String statuses = null;
		statuses = PropertyReader.getInstance().getProperty("offerDeclineReason");
		return statuses;

	}

	public static java.sql.Date convertStringDate(String inDate)
	{
		java.sql.Date date = null;
		StringBuilder dateStringBuf = null;
		String msgDataVal = null;
		try
		{
			if(inDate != null)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				dateStringBuf = new StringBuilder();
				dateStringBuf.append(inDate.substring(6));
				dateStringBuf.append("-");
				dateStringBuf.append(inDate.substring(0, 2));
				dateStringBuf.append("-");
				dateStringBuf.append(inDate.substring(3, 5));
				logger.info("The date string obtained is:" + dateStringBuf.toString());
				java.util.Date parsedDate = dateFormat.parse(dateStringBuf.toString());
				date = new java.sql.Date(parsedDate.getTime());
				logger.info("The converted date is:" + date);
			}
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in Util.convertDateTO()";
			logError(msgDataVal, new RetailStaffingException(RetailStaffingConstants.ERROR_PARSE_DATE));
		}
		return date;
	}
	
	/**
	 * This method compares the two dates provided to determine if they are the
	 * same date (regardless of the time)
	 * 
	 * @param date1
	 *            First date
	 * @param date2
	 *            Second date
	 * 
	 * @return True if the two dates provided are the same, false otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown if either of the dates provided are null
	 */
	public static boolean isSameDate(Date date1, Date date2) throws IllegalArgumentException
	{
		// validate the dates provided are not null
		if(date1 == null || date2 == null)
		{
			throw new IllegalArgumentException("Null date provided");
		} // end if(date1 == null || date2 == null)

		// create the first calendar
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(date1.getTime());
		// set time to midnight
		cal1.set(Calendar.HOUR_OF_DAY, cal1.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal1.set(Calendar.MINUTE, cal1.getActualMinimum(Calendar.MINUTE));
		cal1.set(Calendar.SECOND, cal1.getActualMinimum(Calendar.SECOND));
		cal1.set(Calendar.MILLISECOND, cal1.getActualMinimum(Calendar.MILLISECOND));

		// create the second calendar
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(date2.getTime());
		// set time to midnight
		cal2.set(Calendar.HOUR_OF_DAY, cal2.getActualMinimum(Calendar.HOUR_OF_DAY));
		cal2.set(Calendar.MINUTE, cal2.getActualMinimum(Calendar.MINUTE));
		cal2.set(Calendar.SECOND, cal2.getActualMinimum(Calendar.SECOND));
		cal2.set(Calendar.MILLISECOND, cal2.getActualMinimum(Calendar.MILLISECOND));

		return (cal1.compareTo(cal2) == 0);
	} // end function isSameDate()

	/**
	 * Determine if the date provided is after today (regardless of the time on
	 * the date object)
	 * 
	 * @param date
	 *            The date to compare
	 * 
	 * @return true if the date provided is after today, false otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown if the date provided is null
	 */
	public static boolean isAfterToday(Date date) throws IllegalArgumentException
	{
		// validate the date is not null
		if(date == null)
		{
			throw new IllegalArgumentException("Null date provided");
		} // end if(date == null)

		// create today
		Calendar today = Calendar.getInstance();
		// set time to midnight
		today.set(Calendar.HOUR_OF_DAY, today.getActualMinimum(Calendar.HOUR_OF_DAY));
		today.set(Calendar.MINUTE, today.getActualMinimum(Calendar.MINUTE));
		today.set(Calendar.SECOND, today.getActualMinimum(Calendar.SECOND));
		today.set(Calendar.MILLISECOND, today.getActualMinimum(Calendar.MILLISECOND));

		Calendar other = (Calendar)today.clone();
		// seed with the date provided
		other.setTimeInMillis(date.getTime());
		// set the time to midnight
		other.set(Calendar.HOUR_OF_DAY, other.getActualMinimum(Calendar.HOUR_OF_DAY));
		other.set(Calendar.MINUTE, other.getActualMinimum(Calendar.MINUTE));
		other.set(Calendar.SECOND, other.getActualMinimum(Calendar.SECOND));
		other.set(Calendar.MILLISECOND, other.getActualMinimum(Calendar.MILLISECOND));

		return other.after(today);
	} // end function isAfterToday()

	/**
	 * Determine if the date provided is tomorrow (regardless of time)
	 * 
	 * @param date
	 *            The date to compare
	 * 
	 * @return true if the date provided is tomorrow, false otherwise
	 * 
	 * @throws IllegalArgumentException
	 *             Thrown if the date provided is null
	 */
	public static boolean isTomorrow(Date date) throws IllegalArgumentException
	{
		// validate the date is not null
		if(date == null)
		{
			throw new IllegalArgumentException("Null date provided");
		} // end if(date == null)

		// create today
		Calendar tomorrow = Calendar.getInstance();
		// set time to midnight
		tomorrow.set(Calendar.HOUR_OF_DAY, tomorrow.getActualMinimum(Calendar.HOUR_OF_DAY));
		tomorrow.set(Calendar.MINUTE, tomorrow.getActualMinimum(Calendar.MINUTE));
		tomorrow.set(Calendar.SECOND, tomorrow.getActualMinimum(Calendar.SECOND));
		tomorrow.set(Calendar.MILLISECOND, tomorrow.getActualMinimum(Calendar.MILLISECOND));
		// add a day (making it tomorrow)
		tomorrow.add(Calendar.DATE, 1);

		Calendar other = (Calendar)tomorrow.clone();
		// seed with the date provided
		other.setTimeInMillis(date.getTime());
		// set the time to midnight
		other.set(Calendar.HOUR_OF_DAY, other.getActualMinimum(Calendar.HOUR_OF_DAY));
		other.set(Calendar.MINUTE, other.getActualMinimum(Calendar.MINUTE));
		other.set(Calendar.SECOND, other.getActualMinimum(Calendar.SECOND));
		other.set(Calendar.MILLISECOND, other.getActualMinimum(Calendar.MILLISECOND));

		return (other.compareTo(tomorrow) == 0);
	} // end function isTomorrow()
	
	public static Response setError(Response res)
	{
		GenericErrorTO ge = new GenericErrorTO();
		if(res != null)
		{
			res.setErrorResponse(ge);
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
		}
		else
		{
			res = new Response();
			res.setErrorResponse(ge);
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
		}
		return res;
	}
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * Description : This method format the response based on the mediaType.
	 *
	 * @param mediaType 
	 * 			- the media type
	 * 
	 * @param paramObject 
	 * 			- the param object
	 *  
	 * @return 
	 * 			- the required format res
	 * 
	 * @throws Exception 
	 * 			- the exception
	 */
	public static String getRequiredFormatRes(String mediaType,Object paramObject) {
		String response = null;
		if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_XML))
		{
			/** Serialize the java objects into XML **/
			response = XMLHandler.toXML(paramObject);
		}
		else if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON))
		{
			/** Serialize the java objects into JSON **/
			response=getJsonResponse(paramObject);
		}
		logger.debug(response);
		return response;
		
	}
	
	/**
	 * Description : This method is used to convert the java objects into JSON object and return the response
	 * @param paramObject
	 * @return  the json response
	 */
	public static String getJsonResponse(Object paramObject) 
	{
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		  xstream.autodetectAnnotations(true);
			        xstream.setMode(XStream.NO_REFERENCES);
			        xstream.alias(paramObject.getClass().getSimpleName(),
			        		paramObject.getClass());
			        return xstream.toXML(paramObject);
		
	}
	
	/**
	 * Description : This method will set the exception in ExceptionTO and 
	 * 					-  Invoke getRequiredFormatRes to format the response in required format (XML /JSON) 
	 * @param mediaType
	 * @param e
	 * @return Response
	 * 
	 * This method will inturn invoke the getRequiredFormatRes to format the response in required format (XML /JSON)
	 */
	public static String exceptionResponse(String mediaType,Exception e){
		String response = null;
		ExceptionTO exceptionTo = new ExceptionTO();
		exceptionTo.setUserMessage(e);
		response = getRequiredFormatRes(mediaType,exceptionTo);
		
		logger.debug(response);
		return response;
	}
	
	/**
	 * Description : This method will sets application error message and 
	 * 					- Invoke getRequiredFormatRes to format the response in required format (XML /JSON)  
	 *
	 * @param mediaType the media type
	 * @return the string
	 * 
	 * This method will inturn invoke getRequiredFormatRes method to format the response in required format (XML /JSON) 
	 */
	public static String setApplErrorRes(String mediaType){
		String response = null;
		ErrorTO errorTo = new ErrorTO(VERION_ONE);
		POMRsaStatusCrossRefResponse errorResponse = new POMRsaStatusCrossRefResponse();
		
		errorResponse.setStatus(STATUS_FALSE);
		errorTo.setErrMsg(APPLICATION_ERROR);
		errorResponse.setError(errorTo);
		
		response = Util.getRequiredFormatRes(mediaType,errorResponse);
		logger.debug(response);
		return response;
	}
	
	/**
	 * This method is used to check whether the string is null or not.
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isNullString(String value) {

		boolean isNullString = true;
		if ( value != null && (! value.trim().isEmpty())) {
			isNullString = false;
		}
		return isNullString;
	}
	
	/**
	 * This method will roundOff to 2 decimal places for the given input
	 * @param value
	 * @return
	 */
	public static String setTargetPay(String amount)
	{
		if(!isNullString(amount))
		{
			Double num = Double.parseDouble(amount);
			DecimalFormat df = new DecimalFormat(FORMAT_TWO_PRECISION);
			return df.format(num);
		}
		return amount;
	}
	
	/**
	 * This method will format the given phoneNbr for display purpose
	 * @param phoneNbr
	 * @return
	 */
	public static String formatPhoneNbr(String phoneNbr)
	{
		String formattedPhoneNbr = phoneNbr;
		if(!isNullString(phoneNbr))
		{
			if(phoneNbr.length()==7){
				formattedPhoneNbr = UNDER_SCORE + phoneNbr.substring(0, 3) + UNDER_SCORE + phoneNbr.substring(3, 8);
			}else if(phoneNbr.length()==10){
				formattedPhoneNbr = phoneNbr.substring(0, 3) + UNDER_SCORE + phoneNbr.substring(3, 7) + UNDER_SCORE + phoneNbr.substring(7, 10);
			}else{
				formattedPhoneNbr = phoneNbr;
			}
			return formattedPhoneNbr;
		}
		return phoneNbr;
	}
	
	/**
	 * This method will return the date in the format(MM/dd/yyyy) from the given Date
	 * @param date
	 * @return
	 */
	public static String convertDateFormat(Date date)
	{
		SimpleDateFormat sm = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
		String strDate=null;
		try{
			strDate = sm.format(date);
		}
		catch(Exception e){
			throw e;
		}
		return strDate;
	}
	
	/**
	 * This method will return the date and time in the format(MM/dd/yyyy hh:mm AM/PM) from the given timestamp
	 * @param timestamp
	 * @return
	 */
	public static String convertTimestampFormat(Timestamp timestamp)
	{
		SimpleDateFormat sm = new SimpleDateFormat(DISPLAY_TIMESTAMP_FORMAT);
		String strDate=null;
		try{
			strDate = sm.format(timestamp);
		}
		catch(Exception e){
			throw e;
		}
		return strDate;
	}	
	
	/**
	 * This method will return the date in the format(MM/dd/yyyy) from the given timestamp
	 * @param ts
	 * @return
	 */
	public static String getDateFromTs(Timestamp ts)
	{
		if(ts!=null){
			Date date = new Date(ts.getTime());
			return convertDateFormat(date);
		}
		return null;
	}
	
	/**
	 * This method will return the time in either (HH:MM) or (HH:MM AM/PM) format
	 * @param ts
	 * @param is12HourFormat
	 * @return
	 */
	public static String getTimeFromTs(Timestamp ts, boolean is12HourFormat)
	{
		SimpleDateFormat sm =null;
		if(ts!=null){
			Date dt = new Date(ts.getTime());
			if(is12HourFormat){
				sm = new SimpleDateFormat(FORMAT_12_HOURS);
			}else{
				sm = new SimpleDateFormat(FORMAT_24_HOURS);
			}
			try{
				return sm.format(dt);
			}
			catch(Exception e){
				throw e;
			}
		}
		return null;
	}
	
	/**
	 * This method will return Time in the HH:MM AM/PM format
	 * @param hourStr
	 * @param minutesStr
	 * @return
	 */
	public static String getDisplayTime(TimeStampTO timeStampTO)
	{
		Date date = getDateFromInput(timeStampTO);
		SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_12_HOURS);
		if(date!=null){
			try{
				return dateFormat.format(date);
			}
			catch(Exception e){
				throw e;
			}
		}
		return null; 
	}
	
	/**
	 * This method will set the formattedDate(MM/DD/YYYY) and formattedTime(HH:MM AM/PM) in the TimeStampTO
	 * @param timeStampTO
	 */
	public static void setFormattedDateAndTime(TimeStampTO timeStampTO,boolean isFormatDate)
	{
		if(timeStampTO!=null)
		{
			//Set Formatted Date in the format MM/DD/YYYY
			if( isFormatDate && !isNullString(timeStampTO.getMonth()) && !isNullString(timeStampTO.getDay()) 
					&& !isNullString(timeStampTO.getYear())){
				if(isValidDate(timeStampTO.getYear(), timeStampTO.getDay())){
					String formattedDate = timeStampTO.getMonth() + SLASH + timeStampTO.getDay() + SLASH + timeStampTO.getYear();
					timeStampTO.setFormattedDate(formattedDate);
				}else{
					timeStampTO.setFormattedDate(EMPTY_STRING);
				}
			}
			//Set Formatted Time in the format HH:MM AM/PM 
			
			if(!isNullString(timeStampTO.getHour()) && !isNullString(timeStampTO.getMinute())){
				String formattedTime = getDisplayTime(timeStampTO);
				timeStampTO.setFormattedTime(formattedTime);
			}
		}
	}
	
	public static boolean isValidDate(String year, String date)
	{
		if(Integer.parseInt(year)>0 && Integer.parseInt(date)>0){
			return true;
		}
		return false;
	}
	

	/**
	 * This method will set the formattedDate(MM/DD/YYYY) and formattedTime(HH:MM AM/PM) in the TimeStampTO
	 * @param timeStampTO
	 */
	public static void setFormattedDate(DateTO dateTO)
	{
		String formattedDate=null;
		//Set Formatted Date in the format MM/DD/YYYY
		if(dateTO!=null && !isNullString(dateTO.getMonth()) && !isNullString(dateTO.getDay()) 
				&& !isNullString(dateTO.getYear())){
			if(isValidDate(dateTO.getYear(), dateTO.getDay())){
				formattedDate = dateTO.getMonth() + SLASH + dateTO.getDay() + SLASH + dateTO.getYear();
				dateTO.setFormattedDate(formattedDate);
			}else{
				dateTO.setFormattedDate(EMPTY_STRING);
			}
		}
	}
	
	/**
	 * This method is used to check whether a list is null or not.
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isNullList(Object obj) {
		if (obj instanceof List) {
			List<Object> list = (List<Object>) obj;
			if (!list.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method used to load the scheduled Preference Selection Check box list (For 7 days and 7 entries for each day)
	 * @param res
	 * @return
	 */
	public static List<Boolean> getSchPrefChkBxSelectionList(Response response)
	{
		int daysInWeek = 7;
		int entryPerDay = 7;
		int total = daysInWeek * entryPerDay;
		List<Boolean> schPrefChkBxSelectionList =  new ArrayList<Boolean>();
		
		for(int i=0; i<total; i++)
		{
			schPrefChkBxSelectionList.add(false);
		}
		
		if(response!=null && response.getReqDtlList()!=null && !Util.isNullList(response.getReqDtlList().getReqDtlList()))
		{
			 for(RequisitionDetailTO reqDtl : response.getReqDtlList().getReqDtlList())
			 {
				 if(!Util.isNullList(reqDtl.getReqnSchdPref()))
				 {
					 setChkBxSelectionList(reqDtl, daysInWeek, schPrefChkBxSelectionList );
				 }
			 }
		}
		
		return schPrefChkBxSelectionList;		
	}
	
	/**
	 * Description: This method is used the set the schedule preference check box selection list
	 * @param reqDtl
	 * @param daysInWeek
	 * @param schPrefChkBxSelectionList
	 */
	private static void setChkBxSelectionList(RequisitionDetailTO reqDtl, int daysInWeek, 
											  List<Boolean> schPrefChkBxSelectionList)
	{
		for(SchedulePreferenceTO schPrefTO : reqDtl.getReqnSchdPref())
		 {
			int index = Integer.parseInt(schPrefTO.getDaySegCd()) + Integer.parseInt(schPrefTO.getWkDayNbr());
			index = index + Integer.parseInt(schPrefTO.getDaySegCd()) * (daysInWeek -1);

			if(index>=0){
				schPrefChkBxSelectionList.set(index, true);
			}
		 }
	}
	
	
	/**
	 * Description: This method is used to convert the json String to Object 
	 * 
	 * @param jsonRequest
	 * @param cl
	 * @return
	 */
	public static Object getObjectFromJson(String jsonRequest, Class<?> cl)
	{
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.processAnnotations(cl);
		String className = cl.getSimpleName();
		if(className!=null && TIMESTAMPTO_CLASS_NAME.equals(className)){
			xstream.alias(TIMESTAMPTO_CLASS_NAME, TimeStampTO.class);
		}
		return xstream.fromXML(jsonRequest );
	}
	
	/**
	 * Description: This method is used to convert the input String(xml/json) to Object
	 * 
	 * @param mediaType
	 * @param request
	 * @param cl
	 * @return
	 */
	public static Object getObjectFromInput(String mediaType, String request, Class<?> cl){
		if(mediaType!=null && mediaType.equals(MediaType.APPLICATION_JSON)) {
			return getObjectFromJson(request, cl);
		}else{
			return XMLHandler.fromXML(request);
		}
	}
	
	/**
	 * Description: This method is used to remove the get the mediaType text before ';'
	 * @param mediaType
	 * @return
	 */
	public static String getMediaType(String mediaType)
	{
		String mType = mediaType;
		if(mType!=null && mType.contains(SEMI_COLON)){
			mType = mType.split(SEMI_COLON)[0];	
		}
		return mType;
	}
	
	//Start - Added as part of calculating day light saving time - For Flex to HTML Conversion - 26 May 2015
	/**
	 * Description : This method will set the time and date according store zone
	 *
	 * @@param StoreDetailsTO
	 * 		- The StoreDetailsTO
	 * @param interviewDate 
	 * 			- the interview date
	 * 
	 * @param timeZoneCode 
	 * 			- the time zone code
	 * 
	 * @return 
	 * 			-the store zone mapping
	 */
	public static TimeStampTO getStorePacketDateTime(TimeStampTO interviewTimeStampTO,String timeZoneCode){
		logger.info("Entering into LocationManager :: getStoreZoneMapping method");
		Date interviewDate = null;
		
		int hour = 0;
		
		if(interviewTimeStampTO!=null && !Util.isNullString(timeZoneCode))
		{
			hour = Integer.parseInt(interviewTimeStampTO.getHour());
			
			interviewDate = getDateFromInput(interviewTimeStampTO);
			
			 if (timeZoneCode.equalsIgnoreCase(Constants.ARIZONA_ZONE_CD)) {
	              /** Arizona **/   
				 setArizonaTime(interviewDate, interviewTimeStampTO); 
			 } else if (timeZoneCode.equalsIgnoreCase (Constants.MOUNTAIN_ZONE_CD)) {
				 /** Mountain **/   
				 interviewTimeStampTO.setHour(String.valueOf(hour +2));  
		 	} else if (timeZoneCode.equalsIgnoreCase(Constants.PACIFIC_ZONE_CD)) {
		 		/** Pacific **/   
		 		interviewTimeStampTO.setHour(String.valueOf(hour + 3));  
		 	} else if (timeZoneCode.equalsIgnoreCase(Constants.CENTRAL_ZONE_CD) || 
		 			timeZoneCode.equalsIgnoreCase(Constants.CENTRALL_ZONE_CD)) {
		 		/** Central **/  
		 		interviewTimeStampTO.setHour(String.valueOf(hour + 1));  
	 		} else if (timeZoneCode.equalsIgnoreCase(Constants.ALASKA_ZONE_CD)) {
	 			/** Alaska **/  
	 			interviewTimeStampTO.setHour(String.valueOf(hour + 4)); 
	 		}
			
			interviewDate = getDateFromInput(interviewTimeStampTO);
			//Start - Added as part of calculating day light saving time - For Flex to HTML Conversion - 26 May 2015
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE - MM/dd/YYYY");
			if(interviewDate!=null){
				interviewTimeStampTO.setFormattedDate(dateFormat.format(interviewDate));
			}
		    
		    Util.setFormattedDateAndTime(interviewTimeStampTO, false);			
		    //End - Added as part of calculating day light saving time - For Flex to HTML Conversion - 26 May 2015
				
		}	
		logger.debug("interviewDate :: "+interviewDate);
		logger.info("Exiting into LocationManager :: getStoreZoneMapping method");
			
		return interviewTimeStampTO;
	}
	
	public static void setArizonaTime(Date interviewDate, TimeStampTO interviewTimeStampTO)
	{
		int hour = Integer.parseInt(interviewTimeStampTO.getHour());
		 /** Validate whether the interviewDate is on DST **/
		 if (interviewDate!=null &&isDST(interviewDate)) {
			 interviewTimeStampTO.setHour(String.valueOf(hour + 3));
		 } else {
			 interviewTimeStampTO.setHour(String.valueOf(hour  + 2));
		 }    
	}
	/**
	 * Description : This method will validate whether the requested interview date is on DST (DayLight Saving Time) .
	 *
	 * @param intervwDate 
	 * 			- the intervw date
	 * 
	 * @return 
	 * 			- true, if is dst
	 */
	public static boolean isDST(Date intervwDate){
		logger.info("Exiting into LocationManager :: isDST method");
		boolean isDst = false;
		
		/** Get the timeZone **/
		TimeZone timezoneone = TimeZone.getDefault();
		
		/** Check DST **/
	    isDst = timezoneone.inDaylightTime(intervwDate);
	    logger.debug("intervwDate is in DST ? ::" + isDst);
	    logger.info("Exiting into LocationManager :: isDST method");
		return isDst;
	}
	
	/**
	 * Description: This method used to set the Date from the TimeStampTO
	 * @param interviewTimeStampTO
	 * @return
	 */
	private static Date getDateFromInput(TimeStampTO interviewTimeStampTO)
	{
		Date interviewDate = null;
		int year = getIntValue(interviewTimeStampTO.getYear());
		int month = getIntValue(interviewTimeStampTO.getMonth()) - 1;
		int date = getIntValue(interviewTimeStampTO.getDay());
		int hour = getIntValue(interviewTimeStampTO.getHour());
		int minute = getIntValue(interviewTimeStampTO.getMinute());
		int second = getIntValue(interviewTimeStampTO.getSecond());
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, date, hour, minute,second);
		if(cal!=null){
			interviewDate = new Date(cal.getTimeInMillis());
		}
		return interviewDate;
	}
	
	/**
	 * Description: This method used to handle null check for the given string and convert to int
	 * @param str
	 * @return
	 */
	private static int getIntValue(String str)
	{
		if(Util.isNullString(str)){
			return 0;
		}
		return Integer.parseInt(str);
	}
	//End - Added as part of calculating day light saving time - For Flex to HTML Conversion - 26 May 2015
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
}

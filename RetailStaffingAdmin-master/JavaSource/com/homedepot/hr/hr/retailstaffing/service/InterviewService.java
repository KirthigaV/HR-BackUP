/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: InterviewService.java
 */

package com.homedepot.hr.hr.retailstaffing.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.interfaces.TimePreference;
import com.homedepot.hr.hr.retailstaffing.dto.InterviewAvaliableSlotsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDate;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.InterviewAvailRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.IntrvwAvailDatesRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.IntrvwSchedRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.InterviewAvailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.IntrvwAvailDatesResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.IntrvwSchedResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.IntrvwTimePrefResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.ScheduleDescResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdateResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.enumerations.ErrorType;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.ValidationConstants;
import com.homedepot.hr.hr.retailstaffing.model.PhoneScreenManager;
import com.homedepot.hr.hr.retailstaffing.model.ScheduleManager;
import com.homedepot.hr.hr.retailstaffing.services.response.InterviewResponse;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

@Path("/InterviewService")
public class InterviewService implements Constants, Service, ValidationConstants, RetailStaffingConstants
{
	// number of available dates to return
	private static final int MAX_AVAIL_DATES_TO_RETURN = 3;
	// number of available times to return
	private static final int MAX_AVAIL_TIMES_TO_RETURN = 3;
	
	// logger instance
	private static final Logger mLogger = Logger.getLogger(InterviewService.class);

	/**
	 * This method is used to update the interview status of a phone screen
	 * 
	 * @param phoneScreenId			The phone screen to update
	 * @param scheduleStatus		Description of the new status
	 * @param version				Version, used to indicate the XML response that should be returned. The default
	 * 								if not provided is version 0
	 * 
	 * @return						XML response indicating the update results or error details
	 * 								if an exception occurred
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/updateScheduleStatus")
	public String updatePhoneScreenScheduleStatus(@QueryParam("phoneScreenId") int phoneScreenId, @QueryParam("scheduleStatus") String scheduleStatus, 
		@DefaultValue("0") @QueryParam("version") int version,@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updatePhoneScreenScheduleStatus(), phoneScreenId: %1$d, scheduleStatus: %2$s, version: %3$d",
				phoneScreenId, scheduleStatus, version));
		} // end if
		
		// initialize a response object, will be used for both success and error responses
		UpdateResponse response = new UpdateResponse(version);
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the phone screen id provided
			ValidationUtils.validatePhoneScreenId(phoneScreenId);
			// validate the schedule status provide is not null or empty
			ValidationUtils.validateNotNull(InputField.INTERVIEW_STATUS, scheduleStatus);
			// validate the version is supported
			ValidationUtils.validateVersion(version, VER0, VER1);
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Attempting to get code for status %1$s: phoneScreenId: %2$d", scheduleStatus, phoneScreenId));
			} // end if
			
			// TODO: They really should be providing the code directly!
			// check to see if the status provided exists (maps to a code)
			short statusCode = PhoneScreenManager.checkInterviewScreenStatusCodeExist(scheduleStatus);			
			// if the status is valid, the status code will be greater than 0
			ValidationUtils.validateGreaterThan(InputField.STATUS_CODE, statusCode, 0);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Updating interview status of phone screen %1$d, setting status to %2$d", phoneScreenId, statusCode));
			} // end if
			// invoke the logic to change the status
			PhoneScreenManager.updateInterviewStatusForCTI(phoneScreenId, statusCode);
			response.setUpdated(Boolean.TRUE.toString());
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup an error response
			response.setError(new ErrorTO(ve.getMessage(), version));
		} // end catch
		catch(RetailStaffingException rse)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting status code for interview status %1$s", scheduleStatus));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred getting status code for interview status %1$s. %2$s", scheduleStatus, rse.getMessage()), version));
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred updating the interview status of phone screen %1$d", phoneScreenId));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred updating the interview status of phone screen %1$d. %2$s", phoneScreenId, qe.getMessage()), version));
		} // end catch
			
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }			
			mLogger.debug(String.format("Exiting updatePhoneScreenScheduleStatus(), phoneScreenId: %1$d, scheduleStatus: %2$s, version: %3$d. Total time to process request: %4$.9f seconds",
				phoneScreenId, scheduleStatus, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function updatePhoneScreenScheduleStatus()

	/**
	 * This method is used to get available interview dates for the confirmation number provided
	 * 
	 * @param request				HttpRequest object; The XML request that is used as input to this
	 * 								method will be read from here
	 * @param version				controls the version of logic and XML format that will be returned.
	 * 								This value is defaulted to 1 if not specified.
	 * 
	 * @return						XML containing available interview dates for the confirmation number
	 * 								provided
	 * 
	 * @see	IntrvwAvailDatesResponse
	 */
	// Commented @Produces(APPLICATION_XML),@Consumes(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Path("/getInterviewAvailableDates")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getInterviewAvailableDates(@Context HttpServletRequest request, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getInterviewAvailableDates(), version: %1$d", version));
		} // end if
		
		// initialize a response object, will be used for both success and error responses
		IntrvwAvailDatesResponse response = new IntrvwAvailDatesResponse();
		
		// declare a request object (so it's in scope for the catch blocks)
		IntrvwAvailDatesRequest datesRequest = null;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the version provided is supported
			ValidationUtils.validateVersion(version, VER1);
			// Read the input XML and un-marshal it into the appropriate request object
			if(mediaType!=null && mediaType.equals(MediaType.APPLICATION_JSON))
			{
				String res =Util.getRequiredFormatRes(mediaType,request.getInputStream());
				/** Deserialize the input data (XML /JSON) to java objects **/
				datesRequest = (IntrvwAvailDatesRequest) Util.getObjectFromInput(mediaType, res, IntrvwAvailDatesRequest.class);
			}
			else {
				datesRequest = (IntrvwAvailDatesRequest)XMLHandler.fromXML(request.getInputStream());
			}
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			// validate the datesRequest is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, datesRequest);
			// validate the phone screen id provided
			ValidationUtils.validatePhoneScreenId(datesRequest.getPhnScrnId());
			
			// List of dates provided (these will be fed into the BL)
			List<Date> offeredDates = null;
			
			// if dates were provided
			if(datesRequest.getOfferedDates() != null)
			{
				// initialize the list
				offeredDates = new ArrayList<Date>();
				
				// iterate the dates provided
				for(String dateString : datesRequest.getOfferedDates())
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Converting and validating offered date %1$s for phone screen %2$d", dateString, datesRequest.getPhnScrnId()));
					} // end if
					
					// convert and validate the offered date string and add the date object to the list
					offeredDates.add(convertAndValidate(dateString));
				} // end for(String dateString : datesRequest.getOfferedDates())
			} // end if(datesRequest.getOfferedDates() != null)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting available interview dates for phone screen %1$d", datesRequest.getPhnScrnId()));
			} // end if	
			// invoke the logic to get the available dates
			List<Date> availDates = ScheduleManager.getInterviewAvailableDates(datesRequest.getPhnScrnId(), offeredDates);
			
			// set the confirmation number on the response
			response.setPhnScrnId(String.valueOf(datesRequest.getPhnScrnId()));
			// set the response available dates to an empty collection (forces the outer tag to always be present in the success response)
			response.setAvailDates(new ArrayList<String>());
			
			// if dates were returned
			if(availDates != null && !availDates.isEmpty())
			{
				// create a formatter to format the date into the correct response format
				SimpleDateFormat formatter = new SimpleDateFormat(INTRVW_SVC_DATE_FORMAT);
				
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("%1$d available interview dates returned for phone screen %2$d", availDates.size(), datesRequest.getPhnScrnId()));
				} // end if
				
				/*
				 * The dates should already be sorted, so iterate while we're less than max records
				 * to return, format the date in the appropriate response format, and add it to the
				 * collection going back 
				 */
				int dateCounter = 0;
				Iterator<Date> iterator = availDates.iterator();
				
				while((iterator.hasNext()) && (dateCounter < MAX_AVAIL_DATES_TO_RETURN))
				{
					response.addAvailDate(formatter.format(iterator.next()));
					dateCounter++;
				} // end while((iterator.hasNext()) && (dateCounter < MAX_AVAIL_DATES_TO_RETURN))
			} // end if(availDates != null && !availDates.isEmpty())			
		} // end try
		catch(IOException ioe)
		{
			// log the error
			mLogger.error("An exception occurred reading input XML from the HttpRequest", ioe);
			// setup an error response
			response.setError(new ErrorTO("An exception occurred reading input XML from the HttpRequest", version));
		} // end catch
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup an error response
			response.setError(new ErrorTO(ve.getMessage(), version));
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting available interview dates for confirmation no %1$d", datesRequest.getPhnScrnId()));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred getting available interview dates for confirmation no %1$d. %2$s", 
				datesRequest.getPhnScrnId(), qe.getMessage()), version));
		} // end catch
		//Start - Added to catch general exception - For Flex to HTML Conversion - 13 May 2015
		catch(Exception e)
		{
			// log the error
			mLogger.error(e.getMessage(), e);
			// setup an error response
			response.setError(new ErrorTO("An exception occurred reading input XML from the HttpRequest", version));
		} // end catch
		//End - Added to catch general exception - For Flex to HTML Conversion - 13 May 2015
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getInterviewAvailableDates(), version: %1$d, total time to process request: %2$.9f seconds", 
				version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// marshal the response object into an XML and return it
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function getInterviewAvailableDates()

	/**
	 * This method is used to get interview time availability (AM/PM/BOTH) for the confirmation number and the preferred interview date provided
	 * 
	 * @param phnScrnId					The confirmation number
	 * @param prefDate					The preferred interview date 
	 * @param version					controls the version of logic and XML format that will be returned.
	 * 									This value is defaulted to 1 if not specified.
	 * 
	 * @return							XML containing  interview time availability (AM/PM/BOTH) for the input data provided
	 * 
	 * @see								IntrvwTimePrefResponse
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Path("/getInterviewTimePreference")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getInterviewTimePreference(@QueryParam("confirmationNo") int phnScrnId, @QueryParam("preferredDate") String prefDate, @DefaultValue("1") @QueryParam("version") int version
			,@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getInterviewTimePreference(), phnScrnId: %1$d, prefDate: %2$s, version: %3$d", phnScrnId, prefDate, version));
		} // end if
		
		// initialize a response object, will be used for both success and error responses
		IntrvwTimePrefResponse response = new IntrvwTimePrefResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the phone screen id provided
			ValidationUtils.validatePhoneScreenId(phnScrnId);
			// validate the version number provided is supported
			ValidationUtils.validateVersion(version, VER1);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Converting and validating preferred date %1$s for phone screen id %2$d", prefDate, phnScrnId));
			} // end if
			// convert and validate the preferred date
			Date preferredDate = convertAndValidate(prefDate);
						
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting interview time preference for phone screen %1$d and date %2$s", phnScrnId, prefDate));
			} // end if	
			
			// invoke the logic to get the available dates
			TimePreference timePreference = ScheduleManager.getInterviewTimePreference(phnScrnId, preferredDate);
			
			// if there is a time preference available for the date specified (it won't be null)
			if(timePreference != null)
			{
				// setup the response
				response.setIntrvwDate(prefDate);
				response.setIntrvwAvailTime(String.valueOf(timePreference));
			} // end if(timePreference != null)
			else
			{
				// there is no time available (could've been taken by a different candidate already)
				mLogger.error(String.format("No time preference returned for phone screen %1$d on preferred date %2$s", phnScrnId, prefDate));
				// setup the error response
				response.setError(new ErrorTO(String.format("There are no interview slots available for phone screen %1$d on preferred date %2$s", phnScrnId, prefDate), version));
			} // end else			
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup an error response
			response.setError(new ErrorTO(ve.getMessage(), version));
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting interview time preference confirmation no %1$d and preferred date: %2$s", phnScrnId, prefDate));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred getting interview time preference confirmation no %1$d and preferred date: %2$s. %3$s", 
				phnScrnId, prefDate, qe.getMessage()), version));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getInterviewTimePreference(), phnScrnId: %1$d, prefDate: %2$s, version: %3$d, total time to process request: %4$.9f seconds", 
				phnScrnId, prefDate, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));			
		} // end if
		
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function getInterviewTimePreference()

	/**
	 * This method is used to get multiple interview slots for the phone screen and date provided
	 * 
	 * @param request				HttpRequest object; The XML request that is used as input to this
	 * 								method will be read from here
	 * @param version				controls the version of logic and XML format that will be returned.
	 * 								This value is defaulted to 1 if not specified.
	 * 
	 * @return						XML containing available interview dates for the confirmation number
	 * 								provided
	 * 
	 * @see							InterviewAvailRequest
	 * @see							InterviewAvailResponse
	 */
	// Commented @Produces(APPLICATION_XML),@Consumes(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Path("/getMultipleInterviewAvailableSlots")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getMultipleInterviewAvailableSlots(@Context HttpServletRequest request, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getMultipleInterviewAvailableSlots(), version: %1$d", version));
		} // end if
		
		// initialize a response object, will be used for both success and error responses
		InterviewAvailResponse response = new InterviewAvailResponse(version);
		
		// request object
		InterviewAvailRequest availRequest = null;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the version provided is supported
			ValidationUtils.validateVersion(version, VER1);

			// Read the input XML and un-marshal it into the appropriate request object
			if(mediaType!=null && mediaType.equals(MediaType.APPLICATION_JSON))
			{
				String res =Util.getRequiredFormatRes(mediaType,request.getInputStream());
				/** Deserialize the input data (XML /JSON) to java objects **/
				availRequest=(InterviewAvailRequest)Util.getObjectFromInput(mediaType, res, InterviewAvailRequest.class);
			}
			else
			{
				availRequest = (InterviewAvailRequest)XMLHandler.fromXML(request.getInputStream());
			}
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			
			// validate the datesRequest is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, availRequest);
			// validate the phone screen id provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.PHONE_SCREEN_ID, availRequest.getPhoneScreenId());			
			int phoneScreenId = 0;
			
			// TODO : IF THIS IS AN INT, THEN WHY HAVE IS IT A STRING IN THE TRANSFER OBJECT?
			try
			{
				phoneScreenId = Integer.parseInt(availRequest.getPhoneScreenId());
				// validate the phone screen id provided is numeric and > 0
				ValidationUtils.validatePhoneScreenId(phoneScreenId);
			} // end try
			catch(NumberFormatException nfe)
			{
				throw new ValidationException(InputField.PHONE_SCREEN_ID, String.format("Invalid phone screen id %1$s provided", availRequest.getPhoneScreenId()));
			} // end catch
						
			// validate the time preference provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.INTERVIEW_TIME_PREFERENCE, availRequest.getTimePref());
			// convert the string into a time preference object (if it is invalid, it will throw an IllegalArgumentException)
			TimePreference timePreference = TimePreference.valueOf(availRequest.getTimePref().toUpperCase());
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Converting preferred date %1$s for phone screen id %2$d", availRequest.getPrefDate(), phoneScreenId));
			} // end if
			
			// convert and validate the preferred date
			Date preferredDate = convertAndValidate(availRequest.getPrefDate());
			
			// if interview available dates were provided (this is a subsequent call)
			if(availRequest.getIntrvwAvailDates() != null)
			{
				// iterate the interview available dates
				for(IntrvwAvailDate availDate : availRequest.getIntrvwAvailDates())
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Interview Date %1$s provided for phone screen %2$d", availDate.getIntrvwDt(), phoneScreenId));
					} // end if
					
					// validate the interview date is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.INTERVIEW_DATE, availDate.getIntrvwDt(), VALID_REGEX_DATE_FORMAT, false);
					// validate the begin time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.INTERVIEW_BEGIN_TIME, availDate.getBgnTm(), VALID_REGEX_TIME_FORMAT, false);
					// validate the end time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.INTERVIEW_END_TIME, availDate.getEndTm(), VALID_REGEX_TIME_FORMAT, false);
					
					// every available interview date must have at least one slot
					if(availDate.getIntrvwAvailSlots() == null || availDate.getIntrvwAvailSlots().size() == 0)
					{
						throw new ValidationException(InputField.INTERVIEW_SLOT, String.format("Interview date %1$s did not contain any interview slots", availDate.getIntrvwDt()));
					} // end if(availDate.getIntrvwAvailSlots() == null || availDate.getIntrvwAvailSlots().size() == 0)
					
					// iterate the interview slots that make up this interview date
					for(InterviewAvaliableSlotsTO slot : availDate.getIntrvwAvailSlots())
					{
						// validate the interview slot is not null
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
						// validate the begin timestamp is not null
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
						// validate the calendar id is not <= 0
						ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
						// validate the reservation date time is not null
						ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
						// validate the sequence number is not <= 0
						ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
						// validate the store number
						ValidationUtils.validateStoreNumber(slot.getStoreNumber());
					} // end for(InterviewAvaliableSlotsTO slot : availDate.getIntrvwAvailSlots())
				} // end for(IntrvwAvailDate availDate : availRequest.getIntrvwAvailDates())
			} // end if(availRequest.getIntrvwAvailDates() != null)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting available %1$s interview slots on %2$s for phone screen %3$d",	availRequest.getTimePref(), preferredDate.toString(), 
					phoneScreenId));
			} // end if

			// invoke the logic to get the interview slots
			List<IntrvwAvailDate> intrvwDates = ScheduleManager.getMultipleInterviewAvailableSlots(phoneScreenId, timePreference, preferredDate, 
				MAX_AVAIL_TIMES_TO_RETURN, availRequest.getIntrvwAvailDates());
			
			// setup the response
			response.setPrefTime(availRequest.getTimePref());
			response.setPrefDate(availRequest.getPrefDate());
			response.setPhoneScreenId(availRequest.getPhoneScreenId());
			// set the available dates
			response.setAvailDates(intrvwDates);
		} // end try
		catch(IOException ioe)
		{
			// log the error
			mLogger.error("An exception occurred reading input XML from the HttpRequest", ioe);
			// setup an error response
			response.setError(new ErrorTO("An exception occurred reading input XML from the HttpRequest", version));
		} // end catch
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup an error response
			response.setError(new ErrorTO(ve.getMessage(), version));
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting available interview slots for confirmation no %1$s", availRequest.getPhoneScreenId()));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred getting available interview slots for confirmation no %1$s. %2$s", 
				availRequest.getPhoneScreenId(), qe.getMessage()), version));
		} // end catch
		//Start - Added to catch general exception - For Flex to HTML Conversion - 13 May 2015
		catch (Exception e)
		{
			// log the error
			mLogger.error(e.getMessage(), e);
			// setup an error response
			response.setError(new ErrorTO(e.getMessage(), version));
		}
		//End - Added to catch general exception - For Flex to HTML Conversion - 13 May 2015
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			mLogger.debug(String.format("Exiting getMultipleInterviewAvailableSlots(), version: %1$d, total time to process request: %2$.9f seconds", 
				version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if		
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function getMultipleInterviewAvailableSlots()

	/**
	 * This method is used to schedule an interview for the phone screen and slot data provided
	 * 
	 * @param request				HttpRequest object; The XML request that is used as input to this
	 * 								method will be read from here
	 * @param version				controls the version of logic and XML format that will be returned.
	 * 								This value is defaulted to 1 if not specified.
	 * 
	 * @return						XML containing results from scheduling the interview for the phone screen id provided
	 * 
	 * @see							IntrvwSchedRequest
	 * @see							IntrvwSchedResponse
	 */
	// Commented @Produces(APPLICATION_XML),@Consumes(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Path("/scheduleInterviewTime")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String scheduleInterviewTime(@Context HttpServletRequest request, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering scheduleInterviewTime(), version: %1$d", version));
		} // end if

		// initialize a response object, will be used for both success and error responses
		IntrvwSchedResponse response = new IntrvwSchedResponse();

		// request object
		IntrvwSchedRequest schedRequest = null;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the version provided is supported
			ValidationUtils.validateVersion(version, VER1);

			// Read the input XML and un-marshal it into the appropriate request object
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			//schedRequest = (IntrvwSchedRequest)XMLHandler.fromXML(request.getInputStream());
			if(mediaType!=null && mediaType.equals(MediaType.APPLICATION_JSON))
			{
				String res =Util.getRequiredFormatRes(mediaType,request.getInputStream());
				/** Deserialize the input data (XML /JSON) to java objects **/
				schedRequest=(IntrvwSchedRequest) Util.getObjectFromInput(mediaType, res, IntrvwSchedRequest.class);
			}
			else
			{
				schedRequest = (IntrvwSchedRequest)XMLHandler.fromXML(request.getInputStream());
			}
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			// validate the schedule request is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, schedRequest);
			// validate the phone screen id is greater than zero
			ValidationUtils.validatePhoneScreenId(schedRequest.getPhnScrnId());
			
			IntrvwAvailDate scheduleDate = schedRequest.getScheduleDate();
			
			// validate there is a schedule date
			ValidationUtils.validateNotNull(InputField.INTERVIEW_DATE_DETAILS, scheduleDate);
			// validate the schedule date contains a validate interview date
			ValidationUtils.validateUsingRegex(InputField.INTERVIEW_DATE, scheduleDate.getIntrvwDt(), VALID_REGEX_DATE_FORMAT, false);
			// validate the schedule date contains a valid begin time
			ValidationUtils.validateUsingRegex(InputField.INTERVIEW_BEGIN_TIME, scheduleDate.getBgnTm(), VALID_REGEX_TIME_FORMAT, false);
			// validate the schedule date contains a valid end time
			ValidationUtils.validateUsingRegex(InputField.INTERVIEW_END_TIME, scheduleDate.getEndTm(), VALID_REGEX_TIME_FORMAT, false);

			// validate the schedule date has at least one slot
			if(scheduleDate.getIntrvwAvailSlots() == null || scheduleDate.getIntrvwAvailSlots().size() == 0)
			{
				throw new ValidationException(InputField.INTERVIEW_SLOT, "schedule date did not contain any interview slots");
			} // end if(scheduleDate.getIntrvwAvailSlots() == null || scheduleDate.getIntrvwAvailSlots().size() == 0)
			
			// iterate the interview slots that make up the schedule date
			for(InterviewAvaliableSlotsTO slot : scheduleDate.getIntrvwAvailSlots())
			{
				// validate the interview slot is not null
				ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
				// validate the begin timestamp is not null
				ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
				// validate the calendar id is not <= 0
				ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
				// validate the reservation date time is not null
				ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
				// validate the sequence number is not <= 0
				ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
				// validate the store number is not null or empty
				ValidationUtils.validateStoreNumber(slot.getStoreNumber());
			} // end for(InterviewAvaliableSlotsTO slot : scheduleDate.getIntrvwAvailSlots())
			
			// if there are slots to release
			if(schedRequest.getReleaseDates() != null)
			{
				// iterate the release dates
				for(IntrvwAvailDate releaseDate : schedRequest.getReleaseDates())
				{
					// make sure the release date object is not null
					ValidationUtils .validateNotNull(InputField.RELEASE_DATE, releaseDate);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Release Date %1$s @ %2$s provided for phone screen %3$d", releaseDate.getIntrvwDt(), releaseDate.getBgnTm(), schedRequest.getPhnScrnId()));
					} // end if
					
					// validate the interview date is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.RELEASE_DATE, releaseDate.getIntrvwDt(), VALID_REGEX_DATE_FORMAT, false);				
					// validate the begin time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.RELEASE_DATE_BEGIN_TIME, releaseDate.getBgnTm(), VALID_REGEX_TIME_FORMAT, false);
					// validate the end time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.RELEASE_DATE_END_TIME, releaseDate.getEndTm(), VALID_REGEX_TIME_FORMAT, false);
					
					// every available interview date must have at least one slot
					if(releaseDate.getIntrvwAvailSlots() == null || releaseDate.getIntrvwAvailSlots().size() == 0)
					{
						throw new ValidationException(InputField.INTERVIEW_SLOT, String.format("Release date %1$s did not contain any interview slots", releaseDate.getIntrvwDt()));
					} // end if(releaseDate.getIntrvwAvailSlots() == null || releaseDate.getIntrvwAvailSlots().size() == 0)
					
					// iterate the interview slots that make up this interview date
					for(InterviewAvaliableSlotsTO slot : releaseDate.getIntrvwAvailSlots())
					{
						// validate the interview slot is not null
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
						// validate the begin timestamp is not null
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
						// validate the calendar id is not <= 0
						ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
						// validate the reservation date time is not null
						ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
						// validate the sequence number is not <= 0
						ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
						// validate the store number is not null or empty
						ValidationUtils.validateStoreNumber(slot.getStoreNumber());
					} // end for(InterviewAvaliableSlotsTO slot : releaseDate.getIntrvwAvailSlots())
				} // end for(IntrvwAvailDate availDate : schedRequest.getReleaseDates())				
			} // end if(schedRequest.getReleaseDates() != null)

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Scheduling interview on %1$s from %2$s - %3$s for phone screen %4$d", 
					schedRequest.getScheduleDate().getIntrvwDt(), schedRequest.getScheduleDate().getBgnTm(), schedRequest.getScheduleDate().getEndTm(), schedRequest.getPhnScrnId()));
			} // end if
			// invoke the business logic to schedule/release the appropriate dates
			ScheduleManager.scheduleInterview(schedRequest.getPhnScrnId(), scheduleDate, schedRequest.getReleaseDates());			
			response.setScheduled(Boolean.TRUE);
		} // end try
		catch(IOException ioe)
		{
			// log the error
			mLogger.error("An exception occurred reading input XML from the HttpRequest", ioe);
			// setup an error response
			response.setError(new ErrorTO("An exception occurred reading input XML from the HttpRequest", version));
		} // end catch
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup an error response
			response.setError(new ErrorTO(ve.getMessage(), version));
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred scheduling interview for phone screen %1$d", schedRequest.getPhnScrnId()));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred scheduling interview for phone screen %1$d. %2$s", 
				schedRequest.getPhnScrnId(), qe.getMessage()), version));
		} // end catch
		//Start - Added to catch general exception - For Flex to HTML Conversion - 13 May 2015
		catch (Exception e)
		{
			// log the error
			mLogger.error(e.getMessage(), e);
			// setup an error response
			response.setError(new ErrorTO(e.getMessage(), version));
		}
		//End - Added to catch general exception - For Flex to HTML Conversion - 13 May 2015
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting scheduleInterviewTime(), version: %1$d, total time to process request: %2$.9f seconds", 
				version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function scheduleInterviewTime()
	
	/**
	 * Convenience method convert and validate a string representation of a Date
	 * 
	 * @param dateString					The date to convert and validate
	 * 
	 * @return								Date object created by parsing the date string provided
	 * 
	 * @throws ValidationException		    Thrown if any of the following are true:
	 * 										<ul>
	 * 											<li>The date string provided is null or empty</li>
	 * 											<li>The date string provided is not in the correct format (MM-dd-yyyy)</li>
	 * 											<li>An exception occurs parsing the date string into a Date object</li>
	 * 											<li>The date parsed from the date string is prior to today</li>
	 * 										</ul>
	 */
	private Date convertAndValidate(String dateString) throws ValidationException
	{
		Date date = null;
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering convertAndValidate(), dateString: %1$s", dateString));
		} // end if

		// validate the date string is not null or empty and matches the correct regular expression pattern
		ValidationUtils.validateUsingRegex(InputField.DATE, dateString, VALID_REGEX_DATE_FORMAT, false);
		
		try
		{
			// create a date format object
			DateFormat formatter = new SimpleDateFormat(INTRVW_SVC_DATE_FORMAT);
			// convert the date string into a Date object
			date = formatter.parse(dateString);
			// validate the date is not prior to today
			ValidationUtils.validateNotPriorToToday(InputField.DATE, date);
		} // end try
		catch(ParseException pe)
		{
			// should never happen with regex check above, but never know
			throw new ValidationException(InputField.DATE, String.format("Invalid %1$s %2$s provided", InputField.DATE.getFieldLabel(), dateString));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting convertAndValidate(), dateString: %1$s", dateString));
		} // end if
		
		return date;
	} // end function convertAndValidate()	
	
	/**
	 * This method is used to get active requisition calendars for the store number provided
	 * 
	 * @param storeNumber the store to get calendars for
	 * @param version this service supports two versions. Version 1 will get a list of active calendars for the
	 *                store provided, version 2 will get a list of active calendars with counts (# of active requisitions
	 *                and # of future dated interviews)
	 * 
	 * @return List of active requisition calendars for the store OR error details in the event of an exception
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/requisitionCalendars")
	public String getRequisitionCalendars(@QueryParam("storeNumber") String storeNumber, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		String results = null;
		
		// create a ResponseObject, it will be used to return the results of this function even if there is an error
		Response response = new Response();
		
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 22 May 2015
		String mediaType = contentType;

		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getRequisitionCalendars(), storeNumber: %1$s, version: %2$d", storeNumber, version));
		} // end if
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the store number provided is in the correct format
			ValidationUtils.validateStoreNumber(storeNumber);
			
			/*
			 * validate the version numbers provided are supported. we currently support
			 * two version:
			 * 
			 * 1 - return a list of active calendars
			 * 2 - return a list of active calendars with active requisition counts AND scheduled interview counts
			 */
			ValidationUtils.validateVersion(version, VER1, VER2);
			List<RequisitionCalendarTO> calendars = null;
			
			// use the version to determine which business logic method to invoke
			switch(version)
			{
				case VER1:
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting calendars (without counts) for store %1$s", storeNumber));
					} // end if					
					calendars = ScheduleManager.getRequisitionCalendarsForStore(storeNumber, false);					
					break;
				} // end case VER1
				case VER2:
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting calendars (with counts) for store %1$s", storeNumber));
					} // end if					
					calendars = ScheduleManager.getRequisitionCalendarsAndCountsForStore(storeNumber);					
					break;
				} // end case VER2				
			} // end switch(version)
			
			//Start - Added to sort the list based upon the fields type and requisitionCalendarDescription - For Flex to HTML conversion - 13 May 2015 
			if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON) && !Util.isNullList(calendars)){
				Collections.sort(calendars);
			}
			//End - Added to sort the list based upon the fields type and requisitionCalendarDescription - For Flex to HTML conversion - 13 May 2015
			
			// create and populate the calendar list in the response
			ScheduleDescResponse calendarResponse = new ScheduleDescResponse();
			calendarResponse.setSchDescList(calendars);
			response.setScheduleRes(calendarResponse);
			// set the response code to success
			response.setStatus(SUCCESS_APP_STATUS);
			// generate an XML from the response object
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			
			/** Serialize the java objects into required format (XML/JSON) **/
			results = Util.getRequiredFormatRes(mediaType,response);
			
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting requisition calendars for store %1$s", storeNumber), ve);
			// serialize it into an XML
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			results = XMLHandler.createErrorXML(response, new RetailStaffingException((ve.getInputField() == InputField.STORE_NUMBER ? INVALID_STORE_ERROR_CODE : INVALID_INPUT_CODE),
				ve.getMessage()),mediaType);
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting requisition calendars for store %1$s", storeNumber), qe);
			// serialize it into an XML
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			results = XMLHandler.createErrorXML(response, qe,mediaType);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			// if the start time was not initialized, set it to the same value as end time (otherwise we'll get a negative timing)
			if(startTime == 0){ startTime = endTime; }			
			mLogger.debug(String.format("Exiting getRequisitionCalendars(), storeNumber: %1$s, version: %2$d. Total time to process request: %3$.9f seconds",
				storeNumber, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		return results;
	} // end function getRequisitionCalendars()
	
	/**
	 * This method is used to get active requisition hiring events for the store number provided
	 * 
	 * @param storeNumber the store to get hiring events for
	 * 
	 * @return List of active requisition hiring events for the store OR error details in the event of an exception
	 * 
	 * Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	 * as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	 */
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/requisitionHiringEvents")
	public String getRequisitionHiringEvents(@QueryParam("storeNumber") String storeNumber, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		String results = null;

		// create a ResponseObject, it will be used to return the results of this function even if there is an error
		Response response = new Response();
		
		String mediaType = contentType;

		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getRequisitionHiringEvents(), storeNumber: %1$s, version: %2$d", storeNumber, version));
		} // end if
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the store number provided is in the correct format
			ValidationUtils.validateStoreNumber(storeNumber);
			
			/*
			 * validate the version numbers provided are supported. we currently support
			 * two version:
			 * 
			 * 1 - return a list of active Hiring Events
			 * 2 - return a list of active Hiring Event with Available Interview Slots AND Scheduled Interview Slots
			 */
			ValidationUtils.validateVersion(version, VER1, VER2);
			List<RequisitionCalendarTO> hiringEvents = null;
			
			// use the version to determine which business logic method to invoke
			switch(version)
			{
				case VER1:
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting Hiring Events (without counts) for store %1$s", storeNumber));
					} // end if					
					hiringEvents = ScheduleManager.getRequisitionHiringEventsForStore(storeNumber);					
					break;
				} // end case VER1
				case VER2:
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting Hiring Events (with counts) for store %1$s", storeNumber));
					} // end if					
					hiringEvents = ScheduleManager.getRequisitionHiringEventsAndCountsForStore(storeNumber);
					
					break;
				} // end case VER2				
			} // end switch(version)
			
			//Start - Added to sort the list based upon the fields type and requisitionCalendarDescription //Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON) && !Util.isNullList(hiringEvents)){
				Collections.sort(hiringEvents);
			}
			//End - Added to sort the list based upon the fields type and requisitionCalendarDescription //Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			
			// create and populate the calendar list in the response
			ScheduleDescResponse calendarResponse = new ScheduleDescResponse();
			calendarResponse.setSchDescList(hiringEvents);
			response.setScheduleRes(calendarResponse);
			// set the response code to success
			response.setStatus(SUCCESS_APP_STATUS);
			// generate an XML from the response object
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			results = Util.getRequiredFormatRes(mediaType,response);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		} // end try
		
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting requisition hiring events for store %1$s", storeNumber), ve);
			// serialize it into an XML
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			results = XMLHandler.createErrorXML(response, new RetailStaffingException((ve.getInputField() == InputField.STORE_NUMBER ? INVALID_STORE_ERROR_CODE : INVALID_INPUT_CODE),
				ve.getMessage()),mediaType);
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting requisition hiring events for store %1$s", storeNumber), qe);
			// serialize it into an XML
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			results = XMLHandler.createErrorXML(response, qe,mediaType);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			// if the start time was not initialized, set it to the same value as end time (otherwise we'll get a negative timing)
			if(startTime == 0){ startTime = endTime; }			
			mLogger.debug(String.format("Exiting getRequisitionHiringEvents(), storeNumber: %1$s, version: %2$d. Total time to process request: %3$.9f seconds",
				storeNumber, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		return results;
	} // end function getRequisitionHiringEvents()
	
	/**
	 * This method is used to create a requisition calendar
	 * 
	 * @param name the name of the calendar
	 * @param storeNumber the number of the store this calendar is for
	 * @param type the type of calendar
	 * @param version version, used to control logic flow. Currently this method only supports one version
	 * 
	 * @return ID of the calendar created or error details in the event of an exception
	 * 
	 * @see InterviewResponse
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/createRequisitionCalendar")
	public String createRequisitionCalendar(@FormParam("name") String name, @FormParam("storeNumber") String storeNumber, @FormParam("type") short type,
		@DefaultValue("1") @QueryParam("version") int version,@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering createRequisitionCalendar(), name: %1$s, storeNumber: %2$s, type: %3$d, version: %4$d", name, storeNumber, type, version));
		} // end if
		
		// create a response object, it will be used to return both success and error responses
		InterviewResponse response = new InterviewResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the name provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.REQUISITION_CALENDAR_NAME, name);
			// validate the store number provided
			ValidationUtils.validateStoreNumber(storeNumber);
			// validate the version provided is supported
			ValidationUtils.validateVersion(version, VER1);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Creating requisition calendar for store %1$s named %2$s of type %3$d", storeNumber, name, type));
			} // end if
			
			// create the calendar and set the calendar id returned on the response object
			response.setRequisitionCalendarId(ScheduleManager.createRequisitionCalendar(name, storeNumber, type));
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// set the error details on the response object
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred creating requisition calendar %1$s for store %2$s", name, storeNumber), qe);
			// set the error details on the response object
			response.setErrorDetails(ErrorType.QUERY_DATABASE, ApplicationObject.REQUISITION_CALENDAR, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting createRequisitionCalendar(), name: %1$s, storeNumber: %2$s, type: %3$d, version: %4$d. Total time to create store calendar %5$.9f seconds",
				name, storeNumber, type, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// serialize the response object into an XML
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function createRequisitionCalendar()
} // end class InterviewService
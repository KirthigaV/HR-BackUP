package com.homedepot.hr.hr.staffingforms.service;
/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: CalendarService.java
 */

import java.sql.Date;
import java.sql.Timestamp;

import javax.ws.rs.DefaultValue;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.bl.CalendarManager;
import com.homedepot.hr.hr.staffingforms.dto.AvailabilityBlock;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.enumerations.ApplicationObject;
import com.homedepot.hr.hr.staffingforms.enumerations.ErrorType;
import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.service.request.CalendarRequest;
import com.homedepot.hr.hr.staffingforms.service.response.CalendarResponse;
import com.homedepot.hr.hr.staffingforms.util.Utils;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.hr.hr.staffingforms.util.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.thoughtworks.xstream.XStreamException;
/**
 * RESTful web service that contains methods store calendars
 * 
 * @see CalendarResponse
 */
@Path("/CalendarService")
public class CalendarService implements Constants, Service
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(CalendarService.class);
	
	/**
	 * Description:  This method is used to get interview slot availability summary details for the calendar id and date range provided
	 * @param calendarId the calendar id
	 * @param beginDate the beginning of the date range
	 * @param endDate the ending of the date range
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * @param contentType - content type of the request
	 * @return summary details or exception details if an exception occurs
	 * 
	 * Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	 * as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	 */
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/calendarSummary")
	public String getCalendarSummary(@QueryParam("calendarId") int calendarId, @QueryParam("beginDate") Date beginDate,
		@QueryParam("endDate") Date endDate, @DefaultValue("1") @QueryParam("version") int version,
		@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 22 May 2015
		String mediaType = contentType;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getCalendarSummary(), calendarId: %1$d, beginDate: %2$s, endDate: %3$s, version: %4$d",
				calendarId, beginDate, endDate, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		CalendarResponse response = new CalendarResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the calendar ID provided			
			ValidationUtils.validateCalendarId(calendarId);
			// validate the begin date and end date
			ValidationUtils.validateBefore(InputField.BEGIN_DATE, beginDate, InputField.END_DATE, endDate);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting calendar summary for calendar %1$d from %2$s - %3$s", calendarId, beginDate, endDate));
			} // end if

			// get the summary data and set it up on the response
			response.setDaySummaries(CalendarManager.getCalendarSummaryForDateRange(calendarId, beginDate, endDate));
			//Start Added to frame the updated response - as part of  Flex to HTML Conversion - 12 May 2015
			if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON)){
				response.setDaySummaries(CalendarManager.getFormattedCalendarResponse(response.getDaySummaries()));
			}
			//End Added to frame the updated response - as part of  Flex to HTML Conversion - 12 May 2015
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup the error response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error (business logic logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred getting calendar summary details for calendar id %1$d from %2$s --> %3$s", calendarId, beginDate, endDate), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.CALENDAR, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getCalendarSummary(), calendarId: %1$d, beginDate: %2$s, endDate: %3$s, version: %4$d. Total time to process request: %5$.9f seconds",
				calendarId, beginDate, endDate, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(XMLHandler.toXML(response));
		}
		// serialize the response into an XML
		return Utils.getRequiredFormatRes(mediaType, response);
	} // end function getCalendarSummary()
	
	/**
	 * This method gets calendar details for the calendar id and date provided
	 * 
	 * @param calendarId the calendar id
	 * @param date the date to get details for
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * 
	 * @return calendar details or exception details if an exception occurs
	 * @see CalendarResponse
	 */
	
	/**
	 * Description: This method gets calendar details for the calendar id and date provided
	 * @param calendarId the calendar id
	 * @param date the date to get details for
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * @param contentType - contentType of the request
	 * @return
	 * 
	 * Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
     * as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	 */
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/calendarDetails")
	public String getCalendarDetails(@QueryParam("calendarId") int calendarId, @QueryParam("date") Date date, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 22 May 2015
		String mediaType = contentType;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getCalendarDetails(), calendarId: %1$d, date: %2$s, version: %3$d", calendarId, date, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		CalendarResponse response = new CalendarResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the calendar ID provided			
			ValidationUtils.validateCalendarId(calendarId);
			// validate the date provided is not null
			ValidationUtils.validateNotNull(InputField.DATE, date);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting calendar details for calendar %1$d on %2$s", calendarId, date));
			} // end if

			// get the detail data and set it up on the response
			response.setSchedules(CalendarManager.getCalendarDetailsForDate(calendarId, date));
			//Start Added to frame the updated response - as part of  Flex to HTML Conversion - 12 May 2015
			
			
			if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON))
			{
				response.setScheduleSlots(CalendarManager.getReqSchSlotDtls(response.getSchedules()));
			}
			
			
			//End Added to frame the updated response - as part of  Flex to HTML Conversion - 12 May 2015
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup the error response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error (business logic logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred getting details for calendar id %1$d on date %2$s", calendarId, date), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.CALENDAR, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getCalendarDetails(), calendarId: %1$d, date: %2$s, version: %3$d. Total time to process request: %4$.9f seconds", 
				calendarId, date, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if	
		// serialize the response into an XML
		//Start - Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function calendarDetails()

	/**
	 * This method is used to add availability to a requisition calendar
	 * 
	 * @param xml input XML containing the availability blocks that will be parsed into interview slots
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * 
	 * @return results of the insert or exception details if an exception occurs
	 * @see CalendarRequest
	 * @see CalendarResponse
	 */
	// Commented @Produces(MediaType.APPLICATION_XML),@Consumes(APPLICATION_FORM_URLENCODED) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/addAvailability")
	public String addAvailability(@FormParam("xml") String xml, @DefaultValue("1") @QueryParam("version") int version,
								@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering addAvailability(), xml: %1$s, version: %2$d", xml, version));
		} // end if
	
		// create a response object, it will be used to transmit both success and error responses
		CalendarResponse response = new CalendarResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the input XML provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.INPUT_XML, xml);

			// convert the input XML into a request object
			//Start - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
        	CalendarRequest request = (CalendarRequest) Utils.getObjectFromInput(mediaType, xml,CalendarRequest.class);
        	//End - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
        	
			// validate the request object generated is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, request);
			// validate the collection of availability objects in the request is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.AVAILABILITY_BLOCK, request.getAvailBlocks());
			
			// the number of slots requested for inserted
			int slotsRequested = 0;
			
			// iterate each block provided
			for(AvailabilityBlock block : request.getAvailBlocks())
			{
				// validate the block is not null
				ValidationUtils.validateNotNull(InputField.AVAILABILITY_BLOCK, block);
				// validate the block calendar id is valid
				ValidationUtils.validateCalendarId(block.getCalendarId());
				// validate the blocks time stamps (not null and begin is before end)
				ValidationUtils.validateTimestampBefore(InputField.BEGIN_TIME, block.getBgnTs(), InputField.END_TIME, block.getEndTs());
				// validate the block store number
				ValidationUtils.validateStoreNumber(block.getStoreNbr());
				// validate the blocks number of interviewers is in an acceptable range
				ValidationUtils.validateInRange(InputField.NUMBER_OF_INTERVIEWERS, block.getNumIntrvwrs(), MIN_INTERVIEWERS, MAX_INTERVIEWERS);
				// validate the blocks number of weeks re-occurring is in an acceptable range
				ValidationUtils.validateInRange(InputField.NUMBER_OF_RECURRING_WEEKS, block.getWeeksRecurring(), MIN_RECURRING_WEEKS, MAX_RECURRING_WEEKS);
				
				// use the values to add increment the slot total
				slotsRequested += (int)(((block.getEndTs().getTime() - block.getBgnTs().getTime()) / SLOT_DURATION_IN_MS) * block.getNumIntrvwrs() * block.getWeeksRecurring());				
			} // end for(AvailabilityBlock block : request.getAvailBlocks())
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Add request made with %1$d slots", slotsRequested));
				mLogger.debug(String.format("Adding interview slots using the %1$d availability blocks provided", request.getAvailBlocks().size()));
			} // end if
			
			// verify the number of slots being inserted is below the maximum allowed in a single request
			ValidationUtils.validateLessThan(InputField.INTERVIEW_SLOT, slotsRequested, MAX_SLOT_THRESHOLD);			
			
			// invoke the business logic to insert the interview slots and set the number inserted on the response
			response.setNumSlotsAffected(CalendarManager.addAvailability(request.getAvailBlocks()));
			// set the number of slots requested on the response, so the UI will be able to determine if all the slots were added
			response.setNumSlotsRequested(slotsRequested);
		} // end try
		catch(XStreamException xe)
		{
			// log the error
			mLogger.error("An exception occurred un-marshalling the XML into an object", xe);
			// setup the response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ApplicationObject.CALENDAR, xe);
		} // end catch
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup the error response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);			
		} // end catch
		catch(QueryException qe)
		{
			// log the error (business logic logs to APPL_LOG so will not log it to DB again)
			mLogger.error("An exception occurred adding interview availability", qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.CALENDAR, qe);			
		} // end catch
		
		//Start - Added to catch general exception - For Flex to HTML Conversion - 12 May 2015
		catch(Exception e)
		{
			// log the error
			mLogger.error(e.getMessage(), e);
			// setup the error response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ApplicationObject.CALENDAR, e);	
		}
		//End - Added to catch general exception - For Flex to HTML Conversion - 12 May 2015
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			startTime = (startTime == 0)?endTime:startTime;
			mLogger.debug(String.format("Exiting addAvailability(), xml; %1$s, version: %2$d. Total time to process request: %3$.9f seconds", xml, version,
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		// serialize the response into an XML
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function addAvailability()
	
	/**
	 * This method removes availability from a requisition calendar
	 *
	 * @param xml request data in XML format
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * 
	 * @return calendar details or exception details if an exception occurs
	 * @see CalendarResponse
	 */
	// Commented @Produces(MediaType.APPLICATION_XML),@Consumes(APPLICATION_FORM_URLENCODED) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/removeAvailability")
	public String removeAvailability(@FormParam("xml") String xml, @DefaultValue("1") @QueryParam("version") int version,
									 @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering removeAvailability(), version: %1$d", version));
		} // end if
		// create a response object, it will be used to transmit both success and error responses
		CalendarResponse response = new CalendarResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the data provided is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, xml);
			// convert the XML into a CalendarRequest object
			//Start - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			CalendarRequest request = (CalendarRequest) Utils.getObjectFromInput(mediaType, xml,CalendarRequest.class);
			//End - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			
			// validate the request object created is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, request);
			// validate the collection of RequisitionSchedule objects is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.INTERVIEW_SLOT, request.getReqSchedules());
			
			// iterate each slot and make sure the values required are not null or empty
			for(RequisitionSchedule schedule : request.getReqSchedules())
			{
				// validate the schedule is not null
				ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, schedule);
				// validate the calendar id
				ValidationUtils.validateCalendarId(schedule.getCalendarId());
				// validate the begin timestamp is not null
				ValidationUtils.validateNotNull(InputField.BEGIN_TIME, schedule.getBgnTs());
				// validate the sequence number provided
				ValidationUtils.validateGreaterThan(InputField.SEQUENCE_NUMBER, schedule.getSeqNbr(), MIN_SEQUENCE_NUMBER);
				// validate the schedule status code
				ValidationUtils.validateInRange(InputField.REQUISITION_SCHEDULE_STATUS_CODE, schedule.getReqnSchStatCd(), INTRW_SLOT_AVAILABLE, INTRW_SLOT_BOOKED);
			} // end for(RequisitionSchedule schedule : request.getReqSchedules())
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Attempting to remove %1$d availability slots", request.getReqSchedules().size()));
			} // end if
			
			// invoke the business logic to remove the available slots and set the slot total removed on the response
			response.setNumSlotsAffected(CalendarManager.removeAvailability(request.getReqSchedules()));
			// set the number of slots provided in the initial request, will allow the UI to determine if everything was deleted or not
			response.setNumSlotsRequested(request.getReqSchedules().size());
		} // end try
		catch(XStreamException xe)
		{
			// log the error
			mLogger.error("An exception occurred un-marshalling the XML into an object", xe);
			// setup the response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ApplicationObject.CALENDAR, xe);
		} // end catch		
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup the error response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);			
		} // end catch
		catch(QueryException qe)
		{
			// log the error (business logic logs to APPL_LOG so will not log it to DB again)
			mLogger.error("An exception occurred removing availability", qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.CALENDAR, qe);			
		} // end catch
		//Start - Added to catch general exception - For Flex to HTML Conversion - 12 May 2015
		catch(Exception e)
		{
			// log the error
			mLogger.error(e.getMessage(), e);
			// setup the error response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ApplicationObject.CALENDAR, e);	
		}
		//End - Added to catch general exception - For Flex to HTML Conversion - 12 May 2015
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Entering removeAvailability, version: %1$d. Total time to process request: %2$.9f seconds",
				version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// serialize the response into an XML
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function removeAvailability()

	/**
	 * This method updates either the calendar name or active flag for the calendar id provided
	 * 
	 * @param calendarId the calendar id
	 * @param reqnCalDesc is the new calendar name
	 * @param lastUpdateTimestamp is the last update timestamp of the calendar record.  This prevents us from updating a record that had been updated by someone else since lookup
	 * @param activeFlag is the active flag of the calendar
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * 
	 * @return results of the update or exception details if an exception occurs
	 * @see CalendarResponse
	 */
	// Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/updateCalendar")
	public String updateCalendar(@QueryParam("calendarId") int calendarId, @QueryParam("lastUpdateTimestamp") Timestamp lastUpdTs, @QueryParam("reqnCalDesc") String reqnCalDesc, @QueryParam("activeFlag") boolean activeFlag, @DefaultValue("1") @QueryParam("version") int version,
								@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updateCalendar(), calendarId: %1$d, last updated: %2$s, reqnCalDesc: %3$s, activeFlag: %4$b, version: %5$d", calendarId, lastUpdTs, reqnCalDesc, activeFlag, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		CalendarResponse response = new CalendarResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the calendar ID provided			
			ValidationUtils.validateCalendarId(calendarId);
			// validate the calendar Name provided is not null
			ValidationUtils.validateNotNull(InputField.LAST_UPDATE_TIMESTAMP, lastUpdTs);			
			// validate the calendar Name provided is not null
			ValidationUtils.validateNotNullOrEmpty(InputField.CALENDAR_NAME, reqnCalDesc);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("updating calendar for calendar %1$d last updated %2$s named %3$s active %4$b", calendarId, lastUpdTs, reqnCalDesc, activeFlag));
			} // end if

			// invoke the business logic to update the calendar and set  the response
			CalendarManager.updateCalendar(calendarId, lastUpdTs, reqnCalDesc, activeFlag);
			
			// set the calendar name and ID on the response
			response.setReqnCalId(calendarId);
			response.setReqnCalDesc(reqnCalDesc);
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// setup the error response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error (business logic logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred getting details for calendar id %1$d last updated %2$s name %3$s activeflag %4$b", calendarId, lastUpdTs, reqnCalDesc, activeFlag), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.CALENDAR, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting updateCalendar(), calendarId: %1$d, last updated: %2$s, name: %3$s, active: %4$b, version: %5$d. Total time to process request: %6$.9f seconds", 
				calendarId, lastUpdTs, reqnCalDesc, activeFlag, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
				
		// serialize the response into an XML
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function updateCalendar()
}// end class CalendarService
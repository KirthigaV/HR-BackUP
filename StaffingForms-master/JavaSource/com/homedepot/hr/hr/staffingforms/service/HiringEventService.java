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
 * File Name: HiringEventService.java
 */
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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
import com.homedepot.hr.hr.staffingforms.bl.HiringEventManager;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.enumerations.ApplicationObject;
import com.homedepot.hr.hr.staffingforms.enumerations.ErrorType;
import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.hr.hr.staffingforms.exceptions.DuplicateHireEventNameException;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.service.request.CreateHiringEventRequest;
import com.homedepot.hr.hr.staffingforms.service.request.DeleteParticipatingStoreRequest;
import com.homedepot.hr.hr.staffingforms.service.request.UpdateHiringEventRequest;
import com.homedepot.hr.hr.staffingforms.service.response.Response;
import com.homedepot.hr.hr.staffingforms.util.Utils;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.hr.hr.staffingforms.util.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * RESTful web service that contains methods related to Hiring Events
 * 
 */

@Path("/HiringEventService")
public class HiringEventService implements Service, Constants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(HiringEventService.class);
	
	/**
	 * This method is used to Create a Hiring Event
	 * 
	 * 
	 * @return success or exception details if an exception occurred
	 */
	// Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/createHiringEvent")
	public String createHiringEvent(@FormParam("xml") String xml, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering createHiringEvent(), xml: %1$s, version: %2$d", xml, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		Response response = new Response();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the data provided is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, xml);
			// convert the XML into a CreateHiringEventRequest object
			//Start - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			CreateHiringEventRequest request = (CreateHiringEventRequest)Utils.getObjectFromInput(mediaType, xml, CreateHiringEventRequest.class);
			//End - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			// validate the request object created is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, request);
			
			//Call Business Logic to Create Hiring Event and set the created reqnCalId on response
			response.setReqnCalId(HiringEventManager.createRequisitionHiringEvent(request));
			
			//Need to get the Hiring Event Id based on the reqnCalId
			response.setHireEventId(HiringEventManager.getHireEventId(response.getReqnCalId()));
			
			//Set Success on Response
			response.setStatus(Constants.SUCCESS);
			
			
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// set the error on the response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error (bl logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred creating Hiring Event"), qe);
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
			mLogger.debug(String.format("Exiting createHiringEvent(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// generate the XML the response
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015

	} // end function createHiringEvent()
		
	/**
	 * This method is used to see if a Hiring Event exists with the same name
	 * 
	 * @return success or exception details if an exception occurred
	 */
	// Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/checkDuplicateHiringEventName")
	public String checkDuplicateHiringEventName(@QueryParam("hiringEventName") String hiringEventName, @DefaultValue("0") @QueryParam("hireEventId") int hireEventId, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering checkDuplicateHiringEventName(), Hiring Event Name: %1$s, version: %2$d", hiringEventName, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		Response response = new Response();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the data provided is not null
			ValidationUtils.validateNotNull(InputField.HIRE_EVENT_NAME, hiringEventName);
			
			//Call Business Logic to Create Hiring Event
			HiringEventManager.checkDuplicateHiringEventName(hiringEventName, hireEventId);
			
			//Set Success on Response
			response.setStatus(Constants.SUCCESS);
			
		} // end try
		catch(DuplicateHireEventNameException de)
		{
			// log the error
			mLogger.error(de.getMessage(), de);
			// set the error on the response
			response.setErrorDetails(ErrorType.DUPLICATE_EXCEPTION, de.getInputField(), de);
		} // end catch
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// set the error on the response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
		qe.printStackTrace();
			// log the error (bl logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred getting Hiring Event Calendars"), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.CALENDAR, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting checkDuplicateHiringEventName(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		// generate the XML the response
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function createHiringEvent()
	
	/**
	 * This method is used to delete participating store from a Hiring Event
	 * 
	 * 
	 * @return success or exception details if an exception occurred
	 */
	// Commented @Produces(MediaType.APPLICATION_XML),@Consumes(APPLICATION_FORM_URLENCODED) and  Added @Produces and @Consumes, method argument mediaType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/deleteHiringEventParticipatingStore")
	public String deleteHiringEventParticipatingStore(@FormParam("data") String xml, @DefaultValue("1") @QueryParam("version") int version,
											         @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{

		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering deleteHiringEventParticipatingStore(), data: %1$s, version: %2$d", xml, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		Response response = new Response();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the data provided is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, xml);
			// convert the XML into a CreateHiringEventRequest object
			//Start - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			DeleteParticipatingStoreRequest request = (DeleteParticipatingStoreRequest)Utils.getObjectFromInput(mediaType, xml, DeleteParticipatingStoreRequest.class);
			//End - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			// validate the request object created is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, request);
			
			//Call Business Logic to Delete Participating Store
			HiringEventManager.deleteHiringEventParticipatingStore(request.getStoreNum(), request.getHireEventId());
			
			//Set Success on Response
			response.setStatus(Constants.SUCCESS);
			
			
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// set the error on the response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error (bl logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred creating Hiring Event"), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.HIRING_EVENT, qe);
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
			mLogger.debug(String.format("Exiting deleteHiringEventParticipatingStore(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(XMLHandler.toXML(response));
		}

		// generate the XML the response
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015

	} // end function deleteHiringEventParticipatingStore()
	
	/**
	 * This method is used to add participating store to a Hiring Event
	 * 
	 * 
	 * @return success or exception details if an exception occurred
	 */
	// Commented @Produces(MediaType.APPLICATION_XML),@Consumes(APPLICATION_FORM_URLENCODED) and  Added @Produces and @Consumes, method argument mediaType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@GET
	//@Produces(APPLICATION_XML)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/addHiringEventParticipatingStore")
	public String addHiringEventParticipatingStore(@QueryParam("storeNum") String storeNum, @QueryParam("hireEventId") int hireEventId, @QueryParam("eventSiteFlg") Boolean eventSiteFlg, @DefaultValue("1") @QueryParam("version") int version,
													@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering addHiringEventParticipatingStore(), storeNum: %1$s, hireEventId:%2$d, version: %3$d", storeNum, hireEventId, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		Response response = new Response();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the Store Number
			ValidationUtils.validateStoreNumber(storeNum);
			
			//Validate that Hiring Event Id is greater than zero
			ValidationUtils.validateGreaterThan(InputField.HIRE_EVENT_ID, hireEventId, 0);
						
			//Call Business Logic to Add Participating Store
			HiringEventManager.addHiringEventParticipatingStore(storeNum, hireEventId, eventSiteFlg);
			
			//Set Success on Response
			response.setStatus(Constants.SUCCESS);
			
			
		} // end try 
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// set the error on the response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error (bl logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred adding Hiring Event Participating Store"), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.HIRING_EVENT, qe);
		} // end catch */ 
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting addHiringEventParticipatingStore(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(XMLHandler.toXML(response));
		}

		// generate the XML the response
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015

	} // end function addHiringEventParticipatingStore()	
	
	/**
	 * This method is used to update a Hiring Event
	 * 
	 * 
	 * @return success or exception details if an exception occurred
	 */
	// Commented @Produces(MediaType.APPLICATION_XML),@Consumes(APPLICATION_FORM_URLENCODED) and  Added @Produces and @Consumes, method argument mediaType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/updateHiringEvent")
	public String updateHiringEvent(@FormParam("xml") String xml, @DefaultValue("1") @QueryParam("version") int version,
									@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updateHiringEvent(), xml: %1$s, version: %2$d", xml, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		Response response = new Response();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the data provided is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, xml);
			// convert the XML into a CreateHiringEventRequest object
			//Start - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			UpdateHiringEventRequest request = (UpdateHiringEventRequest)Utils.getObjectFromInput(mediaType, xml, UpdateHiringEventRequest.class);
			//End - Commented and Added as part of converting XML/JSON to Object - For Flex to HTML Conversion - 12 May 2015
			
			// validate the request object created is not null
			ValidationUtils.validateNotNull(InputField.INPUT_XML, request);
			
			//Call Business Logic to Update Hiring Event and Participating Store data
			HiringEventManager.updateRequisitionHiringEvent(request);
			
			//Loop through each Requested Availability Modification 
			if (request.getAvailabilityModifications() != null) {
				HiringEventManager.moveAndOrDeleteHiringEventAvailability(request);
			}
			
			//Update candidates interview location information in HR_PHN_SCRN table for each candidate scheduled
			if (request.getHiringEventDetail().isLocationChange()) {
				if (mLogger.isDebugEnabled()) {
					mLogger.debug(String.format("Getting all candidate phone screens for Hiring Event reqnCalId:%1$d due to Location Change.", request.getHiringEventDetail().getReqnCalId()));
				}
				List<RequisitionSchedule> list = CalendarManager.getPhoneScreenIdsByCalendarByDateRange(request.getHiringEventDetail().getReqnCalId(), Utils.convertToSQLDate(request.getHiringEventDetail().getEventDt())
							, Utils.convertToSQLDate(request.getHiringEventDetail().getEventDtEnd()));
				mLogger.debug(String.format("Hiring Event - Number of Phone Screens to Update:%1$d", list.size()));
				if (list.size() > 0) {
					HiringEventManager.updatePhoneScreenHiringEventLocation(request,list);
				}
			}
			
			//Set Success on Response
			response.setStatus(Constants.SUCCESS);
			
		} // end try
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			// set the error on the response
			response.setErrorDetails(ErrorType.INPUT_VALIDATION, ve.getInputField(), ve);
		} // end catch
		catch(QueryException qe)
		{
			// log the error (bl logs to APPL_LOG so will not log it to DB again)
			mLogger.error(String.format("An exception occurred updating Hiring Event"), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.CALENDAR, qe);
		} // end catch
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting updateHiringEvent(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(XMLHandler.toXML(response).toString());
		}

		// generate the XML the response
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015

	} // end function updateHiringEvent()	
} // end class HiringEventsService
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
 * File Name: LocationService.java
 */
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.bl.StoreManager;
import com.homedepot.hr.hr.staffingforms.enumerations.ApplicationObject;
import com.homedepot.hr.hr.staffingforms.enumerations.ErrorType;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.service.response.StoreResponse;
import com.homedepot.hr.hr.staffingforms.util.Utils;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.hr.hr.staffingforms.util.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * RESTful web service that contains methods related to locations (stores, DC's, etc.)
 * 
 * @see StoreResponse
 */

@Path("/StoreService")
public class StoreService implements Service, Constants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(StoreService.class);
	
	/**
	 * This method is used to validate a store number
	 * 
	 * @param storeNumber the store number to validate
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * 
	 * @return validation results or exception details if an exception occurred
	 */
	// Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/validStore")
	public String isValidStore(@QueryParam("storeNumber") String storeNumber, @DefaultValue("1") @QueryParam("version") int version,
							   @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering isValidStore(), storeNumber: %1$s, version: %2$d", storeNumber, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		StoreResponse response = new StoreResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the store number provided
			ValidationUtils.validateStoreNumber(storeNumber);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Validating storeNumber number %1$s", storeNumber));
			} // end if
			
			// invoke the business logic method to validate the store and set the results on the response object
			response.setValid(StoreManager.isValidStore(storeNumber));
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
			mLogger.error(String.format("An exception occurred validating store number %1$s", storeNumber), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.STORE, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.error(String.format("Exiting isValidStore(), storeNumber: %1$s, version: %2$d. Total time to process request: %3$.9f seconds",
				storeNumber, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// generate the XML the response
		mLogger.debug(XMLHandler.toXML(response));
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function isValidStore()
	
	/**
	 * This method is used to get store details
	 * 
	 * @param storeNumber the store number to validate and get details 
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * 
	 * @return validation results or exception details if an exception occurred
	 */
	// Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
    // as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@GET
	//@Produces(APPLICATION_XML)
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getStoreDetails")
	public String getStoreDetails(@QueryParam("storeNumber") String storeNumber, @DefaultValue("1") @QueryParam("version") int version,
							      @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getStoreDetails(), storeNumber: %1$s, version: %2$d", storeNumber, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		StoreResponse response = new StoreResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the store number provided
			ValidationUtils.validateStoreNumber(storeNumber);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Validating storeNumber number %1$s", storeNumber));
			} // end if
			
			// invoke the business logic method to validate the store and set the results on the response object
			response.setStoreDetails(StoreManager.getStoreDetails(storeNumber));
			
			if (response.getStoreDetails() != null) {
				response.setValid(true);
			} else {
				response.setValid(false);
			}
			//response.setValid(StoreManager.isValidStore(storeNumber));
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
			mLogger.error(String.format("An exception occurred validating store number %1$s", storeNumber), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.STORE, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.error(String.format("Exiting isValidStore(), storeNumber: %1$s, version: %2$d. Total time to process request: %3$.9f seconds",
				storeNumber, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// generate the XML the response
		mLogger.debug(XMLHandler.toXML(response));
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function isValidStore()	
} // end class LocationService
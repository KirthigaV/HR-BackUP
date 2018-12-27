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
 * File Name: Service.java
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

import com.homedepot.hr.hr.staffingforms.bl.RequisitionManager;
import com.homedepot.hr.hr.staffingforms.enumerations.ApplicationObject;
import com.homedepot.hr.hr.staffingforms.enumerations.ErrorType;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.service.response.RequisitionResponse;
import com.homedepot.hr.hr.staffingforms.util.Utils;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.hr.hr.staffingforms.util.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * RESTful web service that contains methods related to requisitions
 * 
 * @see RequisitionResponse
 */
@Path("/RequisitionService")
public class RequisitionService implements Service, Constants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(StoreService.class);

	/**
	 * This method is used to get active requisitions for the store number provided
	 *  
	 * @param storeNumber the store number
	 * @param languageCode the language that description fields should be returned in (defaulted to EN_US if not provided)
	 * @param version version of logic/response format to return. Currently this method only supports version 1
	 * 
	 * @return active requisitions or exception details if an exception occurs
	 * @see RequisitionResponse
	 */
	// Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	@GET
	@Path("/activeRequisitionsForStore")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String getActiveRequisitionsForStore(@QueryParam("storeNumber") String storeNumber, @DefaultValue("EN_US") @QueryParam("languageCode") String langCd,
		@DefaultValue("1") @QueryParam("version") int version, @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getActiveRequisitionsForStore(), storeNumber: %1$s, languageCode: %2$s, version: %3$d", storeNumber, langCd, version));
		} // end if
		
		// create a response object, it will be used to transmit both success and error responses
		RequisitionResponse response = new RequisitionResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the store number provided
			ValidationUtils.validateStoreNumber(storeNumber);
			// validate the language code provided
			ValidationUtils.validateLanguageCode(langCd);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting active requisitions for store %1$s", storeNumber));
			} // end if
			
			// get the active requisitions and set them on the response
			response.setRequisitions(RequisitionManager.getActiveRequisitionsForStore(storeNumber, langCd));
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
			mLogger.error(String.format("An exception occurred getting active requisitions for store %1$s using language code %2$s", storeNumber, langCd), qe);
			// setup the error details
			response.setErrorDetails(ErrorType.DATABASE_EXCEPTION, ApplicationObject.REQUISITION, qe);
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getActiveRequistionsForStore(), storeNumber: %1$s, languageCode: %2$s, version: %3$d. Total time to process request: %4$.9f seconds",
				storeNumber, langCd, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(XMLHandler.toXML(response));
		}
		// serialize the response into an XML
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
		return Utils.getRequiredFormatRes(mediaType, response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
	} // end function getActiveRequisitionsForStore()
} // end class RequistionService
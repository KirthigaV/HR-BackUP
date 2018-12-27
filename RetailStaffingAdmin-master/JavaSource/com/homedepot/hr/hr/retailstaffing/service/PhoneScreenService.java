/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenService.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.service;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.InsertPhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdatePhoneScreenReqnSpecKnockOutRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenInboundResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenKOQuestResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenQuestResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenReqnSpecKODetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdatePhoneScreenReqnSpecKnockOutResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdateResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.VDNDetailsResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.ValidationConstants;
import com.homedepot.hr.hr.retailstaffing.model.CallTypVdnManager;
import com.homedepot.hr.hr.retailstaffing.model.PhoneScreenManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * RESTful web service containing methods related to Retail Staffing Admin phone screen records 
 */
@Path("/PhoneScreenService")
public class PhoneScreenService implements Constants, Service, ValidationConstants, RetailStaffingConstants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(RetailStaffingService.class);

	/**
	 * Web service method used to update the phone screen status for the candidate in the RSA system
	 *
	 * @param phnScrnId				Phone screen identifier that needs to be updated
	 * @param status				New status of the phone screen
	 * @param version				Version of the response XML, will default to version 0 if not specified
	 * 
	 * @return						XML representation of the update status or error details if an error occurred
	 */	
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/updateStatus")
	public String updatePhoneScreenStatus(@QueryParam("phoneScreenId") int phnScrnId, @QueryParam("status") String status, @DefaultValue("0") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updatePhoneScreenStatus(), phnScrnId: %1$d, status: %2$s, version: %3$d", phnScrnId, status, version));
		} // end if
		
		// initialize a response object, will be used for both success and error responses
		UpdateResponse response = new UpdateResponse(version);
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate a valid phone screen id was provided
			if(phnScrnId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phnScrnId));
			} // end if(phnScrnId < 1) 
			
			// validate a status was provided
			if(status == null || status.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s status provided", (status == null ? "Null" : "Empty")));
			} // end if(status == null || status.trim().length() == 0)
			
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			// errorMsg
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting status code for status %1$s", status.trim()));
			} // end if
			
			// next invoke business logic to get the status code for the status string provided
			short statCd = PhoneScreenManager.checkPhoneScreenStatusCodeExist(status.trim());
			
			// if a valid status code is returned
			if(statCd > 0)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Updating phone screen %1$d, setting status to %2$d", phnScrnId, statCd));
				} // end if
				
				response.setUpdated(Boolean.toString(PhoneScreenManager.updateHumanResourcesPhoneScreenStatus(phnScrnId, statCd)));
			} // end if(statCd > 0)
			else
			{
				throw new IllegalArgumentException(String.format("Invalid status %1$s provided", status));
			} // end else
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));			
		} // end catch()		
		catch(Exception e)
		{
			// log the error
			mLogger.error(String.format("An exception occurred changing the status of phone screen %1$d to %2$s", phnScrnId, status));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred changing the status of phone screen %1$d status to %2$s", phnScrnId, status), version));
		} // end catch

		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting updatePhoneScreenStatus(), phnScrnId: %1$d, status: %2$s, version: %3$d. Total time to process request: %4$.9f seconds",
				phnScrnId, status, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function updatePhoneScreenStatus()

	/**
	 *  Web service method used to insert the candidate responses to phone screen questions and receive back a pass or fail decision
	 * 
	 * @param version				Version of the response XML, will default to version 0 if not specified
	 * @param request				HttpRequest object containing the inbound XML needed to process this request
	 * 
	 * @return						XML representation of pass/fail decision or error details if an error occurred
	 */
	// Commented @Produces(APPLICATION_XML), @Consumes(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/insertQuestionResponses")
	public String insertQuestionsResponses(@DefaultValue("0") @QueryParam("version") int version, @Context HttpServletRequest request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering insertQuestionsResponses(), version: %1$d", version));
		} // end if

		// create a response object (will be used regardless of a success or error)
		PhoneScreenQuestResponse response = new PhoneScreenQuestResponse(version);
		InsertPhoneScreenRequest insRequest = null;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			// un-marshal the XML to an InsertPhoneScreenRequest object
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			if(mediaType!=null && mediaType.equals(MediaType.APPLICATION_JSON))
			{
				String res =Util.getRequiredFormatRes(mediaType,request.getInputStream());
				/** Deserialize the input data (XML /JSON) to java objects **/
				insRequest = (InsertPhoneScreenRequest) Util.getObjectFromInput(mediaType, res, InsertPhoneScreenRequest.class);
			}
			else
			{
				insRequest = (InsertPhoneScreenRequest)XMLHandler.fromXML(request.getInputStream());
				
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("in insertQuestionsResponses(), Request: %1$s", insRequest.toString()));
			}
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015		
			// make sure an object was generated
			if(insRequest != null)
			{
				// validate the request contained a phone screen id was provided
				if(insRequest.getPhoneScreenId() == null || insRequest.getPhoneScreenId().trim().length() == 0)
				{
					throw new IllegalArgumentException(String.format("Input XML contained a %1$s phone screen id", (insRequest.getPhoneScreenId() == null ? "null" : "empty")));
				} // end if(insRequest.getPhoneScreenId() == null || insRequest.getPhoneScreenId().trim().length() == 0)
				
				// validate the request contained a list of questions
				if(insRequest.getPhoneScreenMinReqTOList() == null || insRequest.getPhoneScreenMinReqTOList().isEmpty())
				{
					throw new IllegalArgumentException(String.format("Input XML contained a %1$s list of questions", (insRequest.getPhoneScreenMinReqTOList() == null ? "null" : "empty")));
				} // end if(insRequest.getPhoneScreenMinReqTOList() == null || insRequest.getPhoneScreenMinReqTOList().isEmpty())
				
				mLogger.debug("Inserting minimum requirements for phone screen");
				response.setResults(PhoneScreenManager.insertMinimumReqsForPhoneScreenForCTI(insRequest, version));
			} // end if(insRequest != null)
			else
			{
				throw new IllegalArgumentException("Invalid request XML provided");
			} // end else
		} // end try
		catch(IOException ioe)
		{
			// log the error
			mLogger.error("An exception occurred reading input XML from the HttpRequest", ioe);
			// setup an error response
			response.setError(new ErrorTO("An exception occurred reading input XML from the HttpRequest", version));
		} // end catch
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch()
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred inserting question responses for phone screen %1$s", insRequest.getPhoneScreenId()), qe);
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred inserting question responses for phone screen %1$s", insRequest.getPhoneScreenId()), version));			
		} // end catch
		catch(RetailStaffingException re)
		{
			// log the error
			mLogger.error(String.format("An exception occurred inserting question responses for phone screen %1$s", insRequest.getPhoneScreenId()), re);
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred inserting question responses for phone screen %1$s", insRequest.getPhoneScreenId()), version));
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
			mLogger.debug(String.format("Exiting insertQuestionResponses(), version: %1$d. Total time to process request: %2$.9f seconds", 
				version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		// use the XStream API to generate the response
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function insertQuestionResponses()

	/**
	 * Web service method used to get details about the inbound calling candidate.  This was changed for the CDP Project.  No changes to the service call, changes are in 
	 *   PhoneScreenManager.getPhoneScreenCandidateDetails
	 *  
	 * @param candidateId			last 6 digits of the candidate id to retrieve data for.  This is the Kenexa Candidate Ref Number, changed for CDP.
	 * @param pstlCd				Postal code of the candidate (used to reduce the likely hood of duplicates)
	 * @param version				Version of the response XML, will default to version 1 if not specified
	 * 
	 * @return						XML representation of the details about the calling candidate or error
	 * 								details if an error occurred.
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInBoundCandidateDetails")
	public String getInBoundCandidateDetails(@QueryParam("candidateId") String candidateId, @QueryParam("zipCode") String pstlCd, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getInBoundCandidateDetails(), candidateId: %1$s, pstlCd: %2$s, version: %3$d", candidateId, pstlCd, version));
			startTime = System.nanoTime();
		} // end if
		
		// create a response object (will be used to transmit successful and error responses)
		PhoneScreenInboundResponse response = new PhoneScreenInboundResponse();

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the candidate id provided is not null or empty
			if(candidateId == null || candidateId.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s candidate id provided", (candidateId == null ? "Null" : "Empty")));
			} // end if(candidateId == null || candidateId.trim().length() == 0)
			
			// validate the length of the candidate id is 6 AND that all characters in the string are digits
			if(!Pattern.matches(VALID_REGEX_LAST6_CANDID, candidateId))
			{
				throw new IllegalArgumentException(String.format("Invalid candidate id %1$s provided", candidateId));
			} // end if(!Pattern.matches(VALID_REGEX_LAST6_CANDID, candidateId))
			
			// validate the postal code provided is not null or empty
			if(pstlCd == null || pstlCd.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s zip code provided", (pstlCd == null ? "Null" : "Empty")));
			} // end if(pstlCd == null || pstlCd.trim().length() == 0)
			
			// validate the length of the postal code provided is 5 AND that all characters in the string are digits
			// TODO : THIS WILL HAVE TO BE CHANGED IF WE EVER ALLOW CANADIAN POSTAL CODES
			if(!Pattern.matches(VALID_REGEX_US_ZIP, pstlCd))
			{
				throw new IllegalArgumentException(String.format("Invalid zip code %1$s provided", pstlCd));
			} // end if(!Pattern.matches(VALID_REGEX_US_ZIP, pstlCd))
			
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting candidate details using partial candidate id %1$s and postal code %2$s", candidateId, pstlCd));
			} // end if
			
			// invoke the business logic method to get the candidate data for the phone screen id
			PhoneScreenIntrwDetailsTO details = PhoneScreenManager.getPhoneScreenCandidateDetails(candidateId, pstlCd);
			
			// no need to check for details being returned, the business logic throws an exception if no rows are found
			response.setPhoneScreenId(details.getItiNbr());
			
			// set the details returned
			response.setDetails(details);
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch		
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred retrieving candidate details using partial candidate id %1$s and zip code %2$s", candidateId, pstlCd), qe);
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred retrieving candidate details using partial candidate id %1$s and zip code %2$s", candidateId, pstlCd), version));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getInBoundCandidateDetails(), candidateId: %1$s, pstlCd: %2$s, version: %3$d, total time to process request: %4$.9f seconds", 
				candidateId, pstlCd, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function getInBoundCandidateDetails()

	/**
	 * Web service method used to get details about the inbound calling candidate
	 *  
	 * @param confirmationNo		Phone screen id identifying the calling candidate
	 * @param version				Version of the response XML, will default to version 1 if not specified
	 * 
	 * @return						XML representation of the details about the calling candidate or error
	 * 								details if an error occurred.
	 */	
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInBoundCandidateConfirmationDetails")
	public String getInBoundCandidateConfirmationDetails(@QueryParam("confirmationNo") int phoneScreenId, @DefaultValue("1") @QueryParam("version") int version
			,@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getInBoundCandidateConfirmationDetails(), phoneScreenId: %1$d, version: %2$d", phoneScreenId, version));
		} // end if
		
		// create a response object, it will be needed to generate the response XML in the case of a success or error
		PhoneScreenInboundResponse response = new PhoneScreenInboundResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the phone screen id provided is > 0
			if(phoneScreenId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phoneScreenId));
			} // end if(phoneScreenId < 1)

			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting candidate details using phone screen id %1$d", phoneScreenId));
			} // end if
			
			// invoke the business logic method to get the candidate data for the phone screen id
			PhoneScreenIntrwDetailsTO details = PhoneScreenManager.getPhoneScreenCandidateDetails(phoneScreenId);
			
			// no need to check for details being returned, the business logic throws an exception if no rows are found
			response.setPhoneScreenId(String.valueOf(phoneScreenId));
			
			// set the details returned
			response.setDetails(details);
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch()
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred retrieving candidate details using phone screen id %1$s", phoneScreenId), qe);
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred retrieving candidate details using phone screen id %1$s", phoneScreenId), version));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getInBoundCandidateConfirmationDetails(), phoneScreenId: %1$s, version: %2$d, total time to process request: %3$.9f seconds", 
				phoneScreenId, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function getInBoundCandidateConfirmationDetails()	

	/**
	 * Web service method used to update the RSA status for the candidate in the RSA system
	 *
	 * @param phnScrnId				Phone screen identifier that needs to be updated
	 * @param status				New status of the phone screen
	 * @param version				Version of the response XML, will default to version 0 if not specified
	 * 
	 * @return						XML representation of the update status or error details if an error occurred
	 */	
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/updateRSAStatus")
	public String updateRSAStatus(@QueryParam("phoneScreenId") int phnScrnId, @QueryParam("phoneScreenStatus") String status, @DefaultValue("0") @QueryParam("version") int version
			,@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updateRSAStatus(), phnScrnId: %1$d, status: %2$s, version: %3$d", phnScrnId, status, version));
		} // end if

		// initialize a response object, it will be used for both success and error responses
		UpdateResponse response = new UpdateResponse(version);
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate a valid phone screen id was provided
			if(phnScrnId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phnScrnId));
			} // end if(phnScrnId < 1) 
			
			// validate a status was provided
			if(status == null || status.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s status provided", (status == null ? "Null" : "Empty")));
			} // end if(status == null || status.trim().length() == 0)
			
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting status code for status %1$s", status.trim()));
			} // end if			
			
			// next invoke business logic to get the status code for the status string provided
			short statCd = PhoneScreenManager.checkPhoneScreenStatusCodeExist(status.trim());
			
			if(statCd > 0)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Updating phone screen %1$d, setting status to %2$d", phnScrnId, statCd));
				} // end if
				
				response.setUpdated(Boolean.toString(PhoneScreenManager.updateHumanResourcesPhoneScreenStatus(phnScrnId, statCd)));
			} // end if(statCd > 0)
			else
			{
				throw new IllegalArgumentException(String.format("Invalid status %1$s provided", status));
			} // end else
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch()		
		catch(Exception e)
		{
			// log the error
			mLogger.error(String.format("An exception occurred changing the status of phone screen %1$d to %2$s", phnScrnId, status));
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred changing the status of phone screen %1$d status to %2$s", phnScrnId, status), version));
		} // end catch

		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			mLogger.debug(String.format("Exiting updatePhoneScreenStatus(), phnScrnId: %1$d, status: %2$s, version: %3$d. Total time to process request %4$.9f seconds",
				phnScrnId, status, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function updateRSAStatus()
	
	/**
	 * Web service method used to retrieve phone screen questions to be played to the candidate
	 * 
	 * @param phoneScreenId			Phone screen identifier for the inbound candidate
	 * @param jobTitle				Requisition job title
	 * @param version				Version of the response XML, will default to version 1 if not specified
	 * 
	 * @return						XML representation of the knock-out questions for the candidate requisition
	 * 								or error details if an error occurred
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getPhoneScreenKnockOutQuest")
	public String getPhoneScreenKnockOutQuest(@QueryParam("confirmationNo") int phoneScreenId, @QueryParam("jobTitle") String jobTitle, @DefaultValue("1") @QueryParam("version") int version
			,@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getPhoneScreenKnockOutQuest(), phoneScreenId: %1$d, jobTitle: %2$s, version: %3$d", phoneScreenId, jobTitle, version));
		} // end if
		
		// create a response object, it will be needed to generate the response XML in the case of a success or error
		PhoneScreenKOQuestResponse response = new PhoneScreenKOQuestResponse();

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the phone screen id provided is > 0
			if(phoneScreenId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phoneScreenId));
			} // end if(phoneScreenId < 1)
			
			// validate a job title was provided
			if(jobTitle == null || jobTitle.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s job title provided", (jobTitle == null ? "Null" : "Empty")));
			} // end if(jobTitle == null || jobTitle.trim().length() == 0)

			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting knockout questions for phone screen id %1$d", phoneScreenId));
			} // end if
			
			List<String> questions = PhoneScreenManager.getPhoneScreenKnockOutQuest(phoneScreenId, true);
			
			response.setConfirmationNbr(String.valueOf(phoneScreenId));
			
			if(questions != null)
			{
				for(String question : questions)
				{
					response.addQuestion(question);
				} // end for(String question : questions)
			} // end if(questions != null)
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch()
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting knockout questions for phone screen id %1$d", phoneScreenId), qe);
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred getting knockout questions for phone screen id %1$d", phoneScreenId), version));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime;}
			mLogger.debug(String.format("Exiting getPhoneScreenKnockOutQuest(), phoneScreenId: %1$s, jobTitle: %2$s, version: %3$d, total time to process request: %4$.9f seconds", 
				phoneScreenId, jobTitle, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function getPhoneScreenKnockOutQuest()
	
	/**
	 * Web service method used to retrieve vector destination number details
	 *  
	 * @param jobCode				job code to get VDN details for
	 * @param callType				call type to get VDN details for
	 * @param langCd				language code
	 * @param strIntvGuideId		structured interview guide id
	 * @param version				Version of the response XML, will default to version 1 if not specified
	 * 
	 * @return						XML representation of the VDN details for the input parameters provided
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getVDNDetails")
	public String getVDNDetails(@QueryParam("jobcode") String jobCode, @QueryParam("callType") String callType, @DefaultValue("en_US") @QueryParam("langCd") String langCd,
		@DefaultValue("DEFAULT") @QueryParam("strIntvGuideId") String strIntvGuideId, @DefaultValue("1") @QueryParam("version") int version,
		@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getVDNDetails(), jobCode: %1$s, callType: %2$s, version: %3$d", jobCode, callType, version));
		} // end if
		
		// create a response object (will be used to transmit successful and error responses)
		VDNDetailsResponse response = new VDNDetailsResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// validate the job code provided is not null or empty
			if(jobCode == null || jobCode.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s job code provided", (callType == null ? "Null" : "Empty")));
			} // end if(jobCode == null || jobCode.trim().length() == 0)
			
			// validate the call type provided is not null or empty
			if(callType == null || callType.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s call type provided", (callType == null ? "Null" : "Empty")));
			} // end if(callType == null || callType.trim().length() == 0)
			
			// validate the language code was provided
			if(langCd == null || langCd.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s language code provided", (langCd == null ? "Null" : "Empty")));
			} // end if(langCd == null || langCd.trim().length() == 0)
			
			// validate a structured interview guide id was provided (null check only, empty could be valid
			if(strIntvGuideId == null)
			{
				throw new IllegalArgumentException("Null structured interview guide id provided");
			} // if(strIntvGuideId == null)
			
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version number %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting call type VDN using langCd: %1$s, strintvGuideId: %2$s and callType: %3$s", 
					langCd, strIntvGuideId, callType));
			} // end if
			
			// now invoke the business logic method to get the VDN for the call type provided
			int vdn = CallTypVdnManager.getCallTypeVdn(langCd, strIntvGuideId, callType);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Call type VDN %1$d returned using langCd: %2$s, strintvGuideId: %3$s and callType: %4$s",
					vdn, langCd, strIntvGuideId, callType));
			} // end if
			
			// set the VDN on there response object
			response.setVdn(String.valueOf(vdn));
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch		
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred retrieving vdn details using job code %1$s and call type %2$s", jobCode, callType), qe);
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred retrieving vdn details using job code %1$s and call type %2$s", jobCode, callType), version));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getVDNDetails(), jobCode: %1$s, callType: %2$s, version: %3$d, total time to process request: %4$.9f seconds",
				jobCode, callType, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Serialize the java objects into required format (XML/JSON) **/
		return Util.getRequiredFormatRes(mediaType,response);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	} // end function getVDNDetails()}
	
	//Added for FMS 7894 IVR Changes.
	/**
	 * Web service method used to retrieve requisition specific phone screen information and questions to be asked to the candidate
	 * 
	 * @param phoneScreenId			Phone screen identifier for the inbound candidate
	 * @param version				Version of the response XML, will default to version 1 if not specified
	 * 
	 * @return						XML representation of the knock-out questions and other details for the candidate requisition
	 * 								or error details if an error occurred
	 */
	@GET
	@Produces(APPLICATION_XML)
	@Path("/getPhoneScreenReqnSpecKnockOutDetails")
	public String getPhoneScreenReqnSpecKnockOutDetails(@QueryParam("confirmationNo") int phoneScreenId, @DefaultValue("1") @QueryParam("version") int version)
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getPhoneScreenReqnSpecKnockOutDetails(), phoneScreenId: %1$d, version: %2$d", phoneScreenId, version));
		} // end if
		
		// create a response object, it will be needed to generate the response XML in the case of a success or error
		PhoneScreenReqnSpecKODetailResponse response = new PhoneScreenReqnSpecKODetailResponse(); 

		try
		{
			// validate the phone screen id provided is > 0
			if(phoneScreenId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phoneScreenId));
			} // end if(phoneScreenId < 1)		
			
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting requisition specific knockout detail and questions for phone screen id %1$d", phoneScreenId));
			} // end if
			
			response = PhoneScreenManager.getPhoneScreenReqSpecKnockOutDetail(phoneScreenId);			
			response.setConfirmationNbr(String.valueOf(phoneScreenId));
			
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch()
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred getting requisition specific knockout questions for phone screen id %1$d", phoneScreenId), qe);
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred getting requisition specific knockout questions for phone screen id %1$d", phoneScreenId), version));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime;}
			mLogger.debug(String.format("Exiting getPhoneScreenReqnSpecKnockOutDetails(), phoneScreenId: %1$s, version: %2$d, total time to process request: %3$.9f seconds", 
					phoneScreenId, version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return XMLHandler.toXML(response);
	} // end function getPhoneScreenReqnSpecKnockOutDetails()	
	
	//Added for FMS 7894 IVR Changes.	
	/**
	 *  Web service method used to update the candidate responses to phone screen requisition specific questions and receive back a pass or fail decision as well as
	 *  other details in order for the call to continue 
	 * 
	 * @param version				Version of the response XML, will default to version 0 if not specified
	 * @param request				HttpRequest object containing the inbound XML needed to process this request
	 * 
	 * @return						XML representation of pass/fail decision and other data points or error details if an error occurred
	 */
	@POST
	@Produces(APPLICATION_XML)
	@Consumes(APPLICATION_XML)
	@Path("/updatePhoneScreenReqnSpecKnockOutDetails")
	public String updatePhoneScreenReqnSpecKnockOutDetails(@DefaultValue("0") @QueryParam("version") int version, @Context HttpServletRequest request)
	{
		long startTime = 0;

		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updatePhoneScreenReqnSpecKnockOutDetails(), version: %1$d", version));
		} // end if

		// create a response object (will be used regardless of a success or error)
		UpdatePhoneScreenReqnSpecKnockOutResponse response = new UpdatePhoneScreenReqnSpecKnockOutResponse();
		
		UpdatePhoneScreenReqnSpecKnockOutRequest updateRequest = null;
		
		try
		{
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			// un-marshal the XML to an UpdatePhoneScreenReqnSpecKnockOutRequest object
			updateRequest = (UpdatePhoneScreenReqnSpecKnockOutRequest)XMLHandler.fromXML(request.getInputStream());
						
			// make sure an object was generated
			if(updateRequest != null)
			{
				// validate the request contained a phone screen id was provided
				if(updateRequest.getPhoneScreenId() == null || updateRequest.getPhoneScreenId().trim().length() == 0)
				{
					throw new IllegalArgumentException(String.format("Input XML contained a %1$s phone screen id", (updateRequest.getPhoneScreenId() == null ? "null" : "empty")));
				} // end if(insRequest.getPhoneScreenId() == null || insRequest.getPhoneScreenId().trim().length() == 0)
				
				// validate the request contained at least one question
				if(updateRequest.getAcceptsPosition() == null)
				{
					throw new IllegalArgumentException(String.format("Input XML contained a %1$s list of questions", (updateRequest.getAcceptsPosition() == null ? "null" : "empty")));
				} // end if(updateRequest.getAcceptsPosition() == null)

				//Set Phone Screen ID on the response
				response.setPhoneScrnId(updateRequest.getPhoneScreenId());
				
				//Update the Requisition Specific KO Phone Screen questions, returns PASS/FAIL
				response.setCandidatePhnScrnResult(PhoneScreenManager.updateReqnSpecKOQuestionsForCTI(updateRequest));
				
				//Get other responses if candidate PASSED
				if (response.getCandidatePhnScrnResult().equals("PASS")) {
					
					UpdatePhoneScreenReqnSpecKnockOutResponse tempResponse = PhoneScreenManager.getPhoneScreenReqSpecKnockOutResponse(Integer.parseInt(updateRequest.getPhoneScreenId()));
					
					//Added in order to update the Min Requirement Status and the Phone Screener information as the IVR conducted the phone screen
					if (tempResponse.getPhoneScreenRequired().equals("N")) {
						PhoneScreenManager.updatePhoneScreenNotes(Integer.parseInt(updateRequest.getPhoneScreenId()), IVR_CONDUCTED_PHONE_SCREEN , "");
						PhoneScreenManager.updateHumanResourcesMinimumRequirementStatusCode(Integer.parseInt(updateRequest.getPhoneScreenId()), IVR_USER, PROCEED_STAT);
					}	
					
					//Set RSC To Schedule Flag
					response.setRscToSchedule(tempResponse.getRscToSchedule());
					//Set Offer and Interview check flag
					response.setOffersInterviewsCheck(tempResponse.getOffersInterviewsCheck());
					//Set Phone Screen Required flag
					response.setPhoneScreenRequired(tempResponse.getPhoneScreenRequired());
					//Set Calendar Availability
					response.setCalendarAvailability(tempResponse.getCalendarAvailability());
					
				} else {
					//Set RSC To Schedule Flag
					response.setRscToSchedule("");
					//Set Offer and Interview check flag
					response.setOffersInterviewsCheck("");
					//Set Phone Screen Required flag
					response.setPhoneScreenRequired("");
					//Set Calendar Availability
					response.setCalendarAvailability("");					
				}
				
			} // end if(updateRequest != null)
			else
			{
				throw new IllegalArgumentException("Invalid request XML provided");
			} // end else 
			
		} // end try
		catch(IOException ioe)
		{
			// log the error
			mLogger.error("An exception occurred reading input XML from the HttpRequest", ioe);
			//Clear any responses that may have been added
			response = new UpdatePhoneScreenReqnSpecKnockOutResponse();
			// setup an error response
			response.setError(new ErrorTO("An exception occurred reading input XML from the HttpRequest", version));
		} // end catch
		catch(IllegalArgumentException iae)
		{
			// log the error
			mLogger.error(String.format(iae.getMessage()), iae);
			//Clear any responses that may have been added
			response = new UpdatePhoneScreenReqnSpecKnockOutResponse();
			// setup an error response
			response.setError(new ErrorTO(iae.getMessage(), version));
		} // end catch()
		catch(QueryException qe)
		{
			// log the error
			mLogger.error(String.format("An exception occurred inserting question responses for phone screen %1$s", updateRequest.getPhoneScreenId()), qe);
			//Clear any responses that may have been added
			response = new UpdatePhoneScreenReqnSpecKnockOutResponse();
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred inserting question responses for phone screen %1$s", updateRequest.getPhoneScreenId()), version));			
		} // end catch
		catch(RetailStaffingException re)
		{
			// log the error
			mLogger.error(String.format("An exception occurred inserting question responses for phone screen %1$s", updateRequest.getPhoneScreenId()), re);
			//Clear any responses that may have been added
			response = new UpdatePhoneScreenReqnSpecKnockOutResponse();
			// setup an error response
			response.setError(new ErrorTO(String.format("An exception occurred inserting question responses for phone screen %1$s", updateRequest.getPhoneScreenId()), version));
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }			
			mLogger.debug(String.format("Exiting updatePhoneScreenReqnSpecKnockOutDetails(), version: %1$d. Total time to process request: %2$.9f seconds", 
				version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		// use the XStream API to generate the response
		return XMLHandler.toXML(response);
	} // end function insertQuestionResponses()	
} // end class PhoneScreenService
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingService.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.UserDataTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitCandidatePersonalDataRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.StateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.model.CandidateDataFormManager;
import com.homedepot.hr.hr.retailstaffing.model.UserManager;
import com.homedepot.hr.hr.retailstaffing.services.formatters.ExceptionXMLFormatter;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.log4j.SimpleExceptionLogger;

@Path("/CandidateDataFormService")
public class CandidateDataFormService implements Constants, Service, RetailStaffingConstants
{
	private static final Logger mLogger = Logger.getLogger(CandidateDataFormService.class);

	/**
	 * @param mediaType
	 * @param version
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/loadCandidateDataForm")
	public String loadCandidateDataForm(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType,
			@DefaultValue("1") @QueryParam("version") int version) {
		long startTime = 0;
		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Service loadCandidateDataForm()"));
		}

		String result = null;
		Response res = new Response();
		UserDataTO userDataTo = new UserDataTO();
		List<StateDetailsTO> stateDtlList = null;
		StateDetailResponse stateDetailRes = new StateDetailResponse();
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try {
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 7 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// Get Logged in user data
			userDataTo = UserManager.getUserData();
			res.setUserData(userDataTo);

			// Get State data for Drivers License drop down 
			stateDtlList = CandidateDataFormManager.getStateList();
			stateDetailRes.setStrDtlList(stateDtlList);
			res.setStateDtRes(stateDetailRes);
			
			//Set the Current Date on the Response
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat outFmt = new SimpleDateFormat("MM/dd/yyyy");
			res.setCurrentDate(outFmt.format(cal.getTime()));
			Calendar calDob = Calendar.getInstance();
			calDob.add(Calendar.YEAR, -16);
			res.setMaxDOBDate(outFmt.format(calDob.getTime()));
			
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			
			// Call XMLHandler to generate the response XML.
			/** Serialize the java objects into required format (XML/JSON) **/
			result = Util.getRequiredFormatRes(mediaType,res);

		} catch (Exception e) {
			SimpleExceptionLogger.log(e, "CandidateDataForm");
			/** Prepare Exception Response in required format (XML /JSON) **/
			//result = Util.exceptionResponse(mediaType,e);
			result = ExceptionXMLFormatter.formatMessage(e);
		}

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug("\n" + result);
			mLogger.debug(String.format("Exiting Service loadCandidateDataForm(). Total time to process request: %1$.9f seconds", (((double) endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return result;
	}
	
	/**
	 * @param candidateId
	 * @param mediaType
	 * @param version
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/searchCandidateId")
	public String searchCandidateId(@FormParam("candidateId") long candidateId, @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType,
			@DefaultValue("1") @QueryParam("version") int version) {
		long startTime = 0;
		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Service searchCandidateId(), with candidateId:%1$d",candidateId));
		}

		String result = null;
		Response res = new Response();
		CandidateDetailsTO candidateDetail = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try {
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 7 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			// Get candidate name 
			candidateDetail = CandidateDataFormManager.getCandidateData(candidateId);
			res.setCandidateDetails(candidateDetail);
			
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			
			// Call XMLHandler to generate the response XML.
			/** Serialize the java objects into required format (XML/JSON) **/
			result = Util.getRequiredFormatRes(mediaType,res);
			

		} catch (Exception e) {
			SimpleExceptionLogger.log(e, "CandidateDataForm");
			/** Prepare Exception Response in required format (XML /JSON) **/
			if (mediaType.equals("application/json")) {
				result = Util.exceptionResponse(mediaType,e);
			} else {
				result = ExceptionXMLFormatter.formatMessage(e);
			}
		}

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug("\n" + result);
			mLogger.debug(String.format("Exiting Service searchCandidateId(). Total time to process request: %1$.9f seconds", (((double) endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return result;
	}	
	
	/**
	 * @param request
	 * @param mediaType
	 * @param version
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/submitCandidateDetails")
	public String submitCandidateDetails(@Context HttpServletRequest request, @FormParam("request") String formRequest, @DefaultValue("1") @QueryParam("version") int version,
			    @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType) {
		long startTime = 0;
		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Service submitCandidateDetails(), with request:%1$s", formRequest));
		}

		String result = null;
		Response res = new Response();
		SubmitCandidatePersonalDataRequest data = new SubmitCandidatePersonalDataRequest();
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try {

			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 7 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if (formRequest != null  && !formRequest.trim().equals("")) {
				 /** Deserialize the input data (XML /JSON) to java objects **/
				data = (SubmitCandidatePersonalDataRequest) Util.getObjectFromInput(mediaType, formRequest, SubmitCandidatePersonalDataRequest.class);

				//Encrypt SSN and Driver's License, this will be using Voltage
				SubmitCandidatePersonalDataRequest encryptedResults = CandidateDataFormManager.encryptDataElements(request, data);
				data.setSsnEntry1(encryptedResults.getSsnEntry1());
				data.setSsnEntry2(encryptedResults.getSsnEntry2());
				data.setDlNumber1(encryptedResults.getDlNumber1());
				data.setDlNumber2(encryptedResults.getDlNumber2());
				
				//Call Manager to Insert/Update CPD SSN Data
				CandidateDataFormManager.submitCandidateSSNDetails(data);
				
				//Call Manager to Update Other fields on the CPD Form
				CandidateDataFormManager.submitCandidateCPDDetails(data);

				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			} else {
				throw new Exception("Invalid input provided.");
			}
			
			// Call XMLHandler to generate the response XML.
			/** Serialize the java objects into required format (XML/JSON) **/
			result = Util.getRequiredFormatRes(mediaType,res);

		} catch (Exception e) {
			SimpleExceptionLogger.log(e, "CandidateDataForm");
			/** Prepare Exception Response in required format (XML /JSON) **/
			if (mediaType.equals("application/json")) {
				result = Util.exceptionResponse(mediaType,e);
			} else {
				result = ExceptionXMLFormatter.formatMessage(e);
			}
		}

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug("\n" + result);
			mLogger.debug(String.format("Exiting Service submitCandidateDetails(). Total time to process request: %1$.9f seconds", (((double) endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return result;
	}	
}

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

import java.io.ByteArrayOutputStream;

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

import com.homedepot.hr.hr.retailstaffing.dto.CandidateDTInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.ApplToReqRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.ApplicantPoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.CreatePhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.CreateRejectionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.GetLatestRejectionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.HrRetlStffReconsialtionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.ITIUpdateRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.IntvwResultsCandIntvwDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.JobTitleRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.POMRsaStatusCrossRefRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.QPConsideredApplicantRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SearchRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitBgcCandidateDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateIntvwRltsCandIntvwDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateReviewPhnScrnRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateStaffingRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplPoolInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplProfileResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplUnavailInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplicantPoolResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.GenericErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.POMRsaStatusCrossRefResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PomRetailStaffingReconciliationResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.ScheduleDescResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdateFromPOMResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdateResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.model.DriversLicenseManager;
import com.homedepot.hr.hr.retailstaffing.model.LocationManager;
import com.homedepot.hr.hr.retailstaffing.model.PhoneScreenManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingApplPoolInfoManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingApplProfileManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingApplicantPoolManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingCandtDtlManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingITIManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingInterviewManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingInterviewResultsManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingRequisitionManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingSearchManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingUpdateApplManager;
import com.homedepot.hr.hr.retailstaffing.model.ScheduleManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This Class acts as a rest full web service for the RetailStaffing
 * Application. This class will have the following functionalities:- Getting the
 * interview statuses. Searching the details(requisition,candidate, and phone
 * screen) for the given input parameter. Getting the candidate details for the
 * given ssn number. Getting the requisition number for the given requisition
 * number. Getting the phone screen details for the given phone screen number.
 * Getting the store details for the given store number. Updating the phone
 * screen details. Updating the staffing details. Getting the summary details.
 * Creating the phone screen details. Getting the locale details(available
 * regions.divisions and districts). Getting the available process statuses.
 * 
 * @author TCS
 * 
 */
@Path("/RetailStaffingService")
public class RetailStaffingService implements Service, RetailStaffingConstants
{
	private static final Logger logger = Logger.getLogger(RetailStaffingService.class);

	/**
	 * This method is used to get the search results based on the input.
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getSearchDetails")
	public String getSearchDetails(@FormParam(FORM_PARAM_INPUT_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getSearchDetails method in service" + request);
		String response = "";
		SearchRequest reqTO = null;
		Response res = new Response();
		RetailStaffingSearchManager srchMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null)
			{
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				reqTO= (SearchRequest)Util.getObjectFromInput(mediaType, request, SearchRequest.class);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				logger.info("input object" + reqTO);
				srchMgr = new RetailStaffingSearchManager();
				res = srchMgr.getSearchDetails(reqTO);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getSearchDetails()";
			Util.logFatalError(msgDataVal, de);
			res = Util.setError(res);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getSearchDetails()";
			Util.logFatalError(msgDataVal, e);
			res = Util.setError(res);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getSearchDetails method in service" + response);
		return response;

	}

	/**
	 * This method is used to get all the interview statuses.(includes Phone
	 * Screen & Material Statuses)
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getAllInterviewStatuses")
	public String getAllInterviewStatuses(@FormParam(FORM_PARAM_DATA) String phoneScrnNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getAllInterviewStatuses method in service");
		String response = "";
		Response res = new Response();
		RetailStaffingManager dlrnhMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			dlrnhMgr = new RetailStaffingManager();
			res = dlrnhMgr.getAllInterviewStatusesDetails(phoneScrnNbr);
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			// Call XMLHandler to generate the response XML.
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getAllInterviewStatuses()";
			Util.logFatalError(msgDataVal, de);
			res = Util.setError(res);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getAllInterviewStatuses()";
			Util.logFatalError(msgDataVal, e);
			res = Util.setError(res);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getAllInterviewStatuses method in service" + response);
		return response;

	}

	/**
	 * This method is used to get the candidate details based on the input.
	 * Due to CDP Project had to add candRefId so that the Candidate data can be looked up, the ssnNbr is now the applicantId
	 * the ssnNbr/applicantId is used to get their Requisition and Phone Screen History
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getCandidateDetails")
	public String getCandidateDetails(@FormParam("applicantId") String ssnNbr, @FormParam("candRefId") String candRefId,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Entering Service getCandidateDetails() with applicantId:%1$s and candRefId:%2$s",ssnNbr,candRefId));
		}
		String response = "";
		Response res = new Response();
		RetailStaffingCandtDtlManager dlnCndtMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(ssnNbr != null && !ssnNbr.trim().equals("") && candRefId != null && !candRefId.trim().equals(""))
			{
				logger.info("input ssn nbr" + ssnNbr);
				dlnCndtMgr = new RetailStaffingCandtDtlManager();
				res = dlnCndtMgr.getCandidateDetails(ssnNbr, candRefId);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015				
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getCandidateDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getCandidateDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getCandidateDetails method in service" + response);
		return response;

	}

	/**
	 * 
	 * @param reqNbr
	 * @param contentType
	 * @return Response
	 * 
	 * Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	 * as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getRequisitionDetails")
	public String getRequisitionDetails(@FormParam(FORM_PARAM_DATA) String reqNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getRequisitionDetails method in service" + reqNbr);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;

		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 22 May 2015
		String mediaType = contentType;
				
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(reqNbr != null && !reqNbr.trim().equals(EMPTY_STRING))
			{
				logger.info("input requisition nbr" + reqNbr);
				dlnReqMgr = new RetailStaffingRequisitionManager();
				res = dlnReqMgr.getRequisitionDetails(reqNbr);
				
				/** Start - If mediaType is JSON then format the response - For Flex to HTML conversion - 13 May 2015**/
				if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON)){
					res = dlnReqMgr.getFormattedRequisitionResponse(res);
				}
				/** End - If mediaType is JSON then format the response - For Flex to HTML conversion - 13 May 2015 **/
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getRequisitionDetails()";
			Util.logFatalError(msgDataVal, de);
			//Added argument mediatype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getRequisitionDetails()";
			Util.logFatalError(msgDataVal, e);
			//Added argument mediatype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getRequisitionDetails method in service" + response);
		return response;

	}

	/**
	 * This method is used to get the requisition details based on the input.
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInactiveRequisitionDetails")
	public String getInactiveRequisitionDetails(@FormParam(FORM_PARAM_DATA) String reqNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getInactiveRequisitionDetails method in service" + reqNbr);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(reqNbr != null && !reqNbr.trim().equals(""))
			{
				logger.info("input requisition nbr" + reqNbr);
				dlnReqMgr = new RetailStaffingRequisitionManager();
				res = dlnReqMgr.getInactiveRequisitionDetails(reqNbr);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getInactiveRequisitionDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediatype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getInactiveRequisitionDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediatype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getInactiveRequisitionDetails method in service" + response);
		return response;

	}

	/**
	 * Description: This method is used to get the phone screen details based on the input.
	 * @param itiNbrStatus
	 * @param contentType
	 * @return Response
	 * 
	 * Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	 * as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getPhoneScreenDetails")
	public String getPhoneScreenDetails(@FormParam(FORM_PARAM_DATA) String itiNbrStatus,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getPhoneScreenDetails method in service" + itiNbrStatus);
		String response = "";
		Response res = new Response();
		RetailStaffingITIManager dlnITIMgr = null;
		String msgDataVal = null;
		
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 22 May 2015
		String mediaType = contentType;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(itiNbrStatus != null && !itiNbrStatus.trim().equals(EMPTY_STRING))
			{
				logger.info("input itiNbrStatus nbr" + itiNbrStatus);
				dlnITIMgr = new RetailStaffingITIManager();
				res = dlnITIMgr.getPhoneScreenIntrwDtls(itiNbrStatus);
				//Start - Added  to format the response as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON)){
					res = dlnITIMgr.getFormmattedPhnScrnResponse(res);
				}
				//End - Added  to format the response as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getPhoneScreenDetails()";
			Util.logFatalError(msgDataVal, de);
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getPhoneScreenDetails()";
			Util.logFatalError(msgDataVal, e);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getPhoneScreenDetails method in service" + response);
		return response;

	}
	
	
	/**
	 * This method is used to get the phone screen minimum requirement detail history based on the input.
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 26 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getPhoneScreenMinimumDetails")
	public String getPhoneScreenMinimumDetails(@FormParam(FORM_PARAM_DATA) String itiNbrStatus,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getPhoneScreenMinimumDetails method in service" + itiNbrStatus);
		String response = "";
		Response res = new Response();
		RetailStaffingITIManager dlnITIMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 26 May 2015
		String mediaType = contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 26 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(itiNbrStatus != null && !itiNbrStatus.trim().equals(""))
			{
				logger.info("input itiNbrStatus nbr" + itiNbrStatus);
				dlnITIMgr = new RetailStaffingITIManager();
				res = dlnITIMgr.getPhoneScreenMinimumDtls(itiNbrStatus);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				//Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				response = Util.getRequiredFormatRes(mediaType,res);
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getPhoneScreenDetails()";
			Util.logFatalError(msgDataVal, de);
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 26 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getPhoneScreenDetails()";
			Util.logFatalError(msgDataVal, e);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 26 May 2015
			response = XMLHandler.createErrorXML(res, e, mediaType);
		}
		logger.info(this.getClass() + "Leaving getPhoneScreenMinimumDetails method in service" + response);
		return response;

	}

	/**
	 * Description: This method is used to get the interview details based on the input.
	 * @param itiNbr
	 * @param contentType
	 * @return Response
	 * 
	 * Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	 * as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInterviewDetails")
	public String getInterviewDetails(@FormParam(FORM_PARAM_DATA) String itiNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getInterviewDetails method in service" + itiNbr);
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewManager dlnReqMgr = null;
		String msgDataVal = null;
		
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 22 May 2015
		String mediaType = contentType;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(itiNbr != null && !itiNbr.trim().equals(EMPTY_STRING))
			{
				logger.info("input requisition nbr" + itiNbr);
				dlnReqMgr = new RetailStaffingInterviewManager();
				res = dlnReqMgr.getInterviewDetails(itiNbr);
				
				//Start - Added to update the response for formatting phone number, Date and Time
				if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON)){
					res = dlnReqMgr.getFormattedInterviewResponse(res);
				}
				//End - Added to update the response for formatting phone number, Date and Time
				
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				
				
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getRequisitionDetails()";
			Util.logFatalError(msgDataVal, de);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getRequisitionDetails()";
			Util.logFatalError(msgDataVal, e);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getInterviewDetails method in service" + response);
		return response;

	}

	/**
	 * Description: This method is used to get the store details based on the input.
	 * @param strNbr
	 * @param interviewDate
	 * @param contentType
	 * @return response
	 * 
	 * Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
     * as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	 */
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getStoreDetails")
	public String getStoreDetails(@FormParam(FORM_PARAM_DATA) String strNbr,
			@DefaultValue("") @FormParam("interviewDate")String interviewDate,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug(String.format("Entering getStoreDetails() method, store number: %1$s", strNbr));
		}

		String response = "";
		Response res = new Response();

		String msgDataVal = null;
		String mediaType = contentType;
		
		//Added as part of calculating day light saving time - For Flex to HTML Conversion - 26 May 2015
		TimeStampTO interviewTimeStampTO = null;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(strNbr != null && strNbr.trim().length() > 0)
			{
				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("input Store number : %1$s", strNbr));
				} // end if
				
				//Start - Added as part of calculating day light saving time - For Flex to HTML Conversion - 26 May 2015
				if(!Util.isNullString(interviewDate)){
					interviewTimeStampTO= (TimeStampTO)Util.getObjectFromInput(mediaType, interviewDate, TimeStampTO.class);
				}
				//End -Added as part of calculating day light saving time - For Flex to HTML Conversion - 26 May 2015
				
				StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(strNbr);

				/** Start - If mediaType is JSON then format the response - For Flex to HTML conversion - 13 May 2015 **/
				if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON)){
					store = LocationManager.getFormattedStoreDtlsResponse(store,interviewTimeStampTO); 
				}
				/** End - If mediaType is JSON then format the response - For Flex to HTML conversion - 13 May 2015 **/
				
				res.setStrDtRes(new StoreDetailResponse());
				res.getStrDtRes().addStore(store);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getStoreDetails()";
			Util.logFatalError(msgDataVal, de);
			//Added argument mediatype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getStoreDetails()";
			Util.logFatalError(msgDataVal, e);
			//Added argument mediatype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);

		}
		logger.info(this.getClass() + "Leaving getStoreDetails method in service" + response);
		return response;
	}

	/**
	 * This method is used to update or insert the phone screen informations.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/submitITIDetails")
	// SUBMIT_ITI_DTL_MTHD_PATH
	public String submitITIDetails(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters submitITIDetails method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingITIManager dlnITIMgr = null;
		ITIUpdateRequest itiUpdateReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				itiUpdateReq= (ITIUpdateRequest)Util.getObjectFromInput(mediaType, request, ITIUpdateRequest.class);
				//End- Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				dlnITIMgr = new RetailStaffingITIManager();
				res = dlnITIMgr.updateInsertITIDetails(itiUpdateReq);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in submitITIDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in submitITIDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving submitITIDetails method in service" + response);
		return response;
	}

	/**
	 * This method is used to update or insert the Interview screen
	 * informations.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/submitInterviewScrnDetails")
	// SUBMIT_ITI_DTL_MTHD_PATH
	public String submitInterviewScrnDetails(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters submitInterviewScrnDetails method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingITIManager dlnITIMgr = null;
		ITIUpdateRequest itiUpdateReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015	
				/** Deserialize the input data (XML /JSON) to java objects **/
				itiUpdateReq= (ITIUpdateRequest)Util.getObjectFromInput(mediaType, request, ITIUpdateRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				dlnITIMgr = new RetailStaffingITIManager();
				res = dlnITIMgr.updateInterviewScrnDetails(itiUpdateReq);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in submitITIDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in submitITIDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving submitInterviewScrnDetails method in service" + response);
		return response;
	}

	/**
	 * This method is used to update the staffing details.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/updateStaffingDetails")
	public String updateStaffingDetails(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters updateStaffingDetails method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		UpdateStaffingRequest updateStfReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				updateStfReq= (UpdateStaffingRequest)Util.getObjectFromInput(mediaType, request, UpdateStaffingRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				dlnReqMgr = new RetailStaffingRequisitionManager();
				res = dlnReqMgr.updateStaffingDetails(updateStfReq);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015				
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in updateStaffingDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in updateStaffingDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving updateStaffingDetails method in service" + response);
		return response;
	}

	/**
	 * This method is used to update or insert the phone screen informations.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getSummaryResults")
	public String getSummaryResults(@FormParam(FORM_PARAM_DATA) String searchReq,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getSummaryResults method in service with req: " + searchReq);
		String response = "";
		Response res = new Response();
		RetailStaffingManager dlnMgr = null;
		SummaryRequest summReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if((searchReq != null && !searchReq.trim().equals("")))
			{

				dlnMgr = new RetailStaffingManager();
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				summReq= (SummaryRequest)Util.getObjectFromInput(mediaType, searchReq, SummaryRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				if(summReq.getOrgSearchCode() == null || summReq.getOrgSearchCode().trim().equals(RetailStaffingConstants.EMPTY_STRING)
				    || summReq.getProcessSearchCode() == null || summReq.getProcessSearchCode().trim().equals(RetailStaffingConstants.EMPTY_STRING)
				    || summReq.getOrgID() == null || summReq.getOrgID().trim().equals(RetailStaffingConstants.EMPTY_STRING))
				{
					res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
				else
				{
					res = dlnMgr.getSummaryResults(summReq);
					res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
					// Call XMLHandler to generate the response XML.
					//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
					/** Serialize the java objects into required format (XML/JSON) **/
					response = Util.getRequiredFormatRes(mediaType,res);
					//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				}
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getSummaryResults()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getSummaryResults()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getSummaryResults method in service" + response);
		return response;
	}

	/**
	 * This method will be used for creating phone screens for the given
	 * candidates for a given requisition number.
	 * 
	 * @param request
	 *            - The object containing the requisition number and the
	 *            candidate ID list.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/createPhoneScreens")
	public String createPhoneScreens(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters createPhoneScreens method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingITIManager dlnITIMgr = null;
		CreatePhoneScreenRequest reqTO = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{

				dlnITIMgr = new RetailStaffingITIManager();
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				reqTO= (CreatePhoneScreenRequest)Util.getObjectFromInput(mediaType, request, CreatePhoneScreenRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				
				res = dlnITIMgr.createPhoneScreens(reqTO);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in createPhoneScreens()";
			Util.logFatalError(msgDataVal, de);
			
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in createPhoneScreens()";
			Util.logFatalError(msgDataVal, e);
			
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving createPhoneScreens method in service" + response);
		return response;

	}

	/**
	 * This method will be used for fetching review phone screen details
	 * 
	 * @param reqNbr
	 *            - Requistion Number.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getRevPhnScrnDetails")
	public String getRevPhnScrnDetails(@FormParam(FORM_PARAM_DATA) String reqNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getRevPhnScrnDetails method in service" + reqNbr);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(reqNbr != null && !reqNbr.trim().equals(""))
			{

				dlnReqMgr = new RetailStaffingRequisitionManager();
				res = dlnReqMgr.getRevCmplPhnScrnDetails(reqNbr);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getRevPhnScrnDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getRevPhnScrnDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getRevPhnScrnDetails method in service" + response);
		return response;

	}

	/**
	 * The service method will return the list of all available
	 * divisions,districts and regions.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getLocaleDetails")
	public String getLocaleDetails(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getLocaleDetails method in service");
		String response = "";
		Response res = new Response();
		RetailStaffingManager dlnMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			dlnMgr = new RetailStaffingManager();
			res = dlnMgr.getLocaleDetails();
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			logger.info("The region list is" + response);
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getLocaleDetails()";
			Util.logFatalError(msgDataVal, de);
			
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getLocaleDetails()";
			Util.logFatalError(msgDataVal, e);
			
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getLocaleDetails method in service" + response);
		return response;

	}
	
	

	/**
	 * This method will be used for updating review phone screens
	 * 
	 * @param request
	 *            - The object containing the requisition and phone screen
	 *            details
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/saveRevPhnScrnDtl")
	public String saveRevPhnScrnDtl(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters updateRevPhnScrnDetails method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;
		UpdateReviewPhnScrnRequest reqTO = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{

				dlnReqMgr = new RetailStaffingRequisitionManager();
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015

				/** Deserialize the input data (XML /JSON) to java objects **/
				reqTO= (UpdateReviewPhnScrnRequest)Util.getObjectFromInput(mediaType, request, UpdateReviewPhnScrnRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				
				res = dlnReqMgr.updateRevCmplPhnScrnDetails(reqTO);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in updateRevPhnScrnDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in updateRevPhnScrnDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving updateRevPhnScrnDetails method in service" + response);
		return response;

	}

	/**
	 * This method will be used for getting the Departments based on the passed
	 * in Store Number, also the Store number will be validated
	 * 
	 * @param request
	 *            - Store Number
	 * 
	 * @return
	 */

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getDeptDetails")
	public String getDeptsByStr(@FormParam(FORM_PARAM_DATA) String strNo,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + " Enters getDeptsByStr method in service.. Store = " + strNo);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(strNo != null && !strNo.trim().equals(""))
			{
				dlnReqMgr = new RetailStaffingRequisitionManager();

				res = dlnReqMgr.getDeptByStore(strNo);

				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);

				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getDeptsByStr()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getDeptsByStr()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + " Leaving getDeptsByStr method in service " + response);
		return response;

	}

	/**
	 * This method will be used for getting the Job Titles by Department by
	 * Store Number
	 * 
	 * @param request
	 *            - Store Number, Department Number
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getJobTtlDetails")
	public String getJobTtlByStrByDept(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getJobTtlByStrByDept method in service..");
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		JobTitleRequest reqTO = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				reqTO= (JobTitleRequest)Util.getObjectFromInput(mediaType, request, JobTitleRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				logger.info(this.getClass() + " Store = " + reqTO.getStrNo() + "Dept No = " + reqTO.getDeptNo());
				if(reqTO.getStrNo() != null && !StringUtils.trim(reqTO.getStrNo()).equals("") && reqTO.getDeptNo() != null
				    && !StringUtils.trim(reqTO.getDeptNo()).equals(""))
				{
					dlnReqMgr = new RetailStaffingRequisitionManager();

					res = dlnReqMgr.getJobTtlByDeptByStore(reqTO);
					res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);

					// Call XMLHandler to generate the response XML.
					//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

					/** Serialize the java objects into required format (XML/JSON) **/
					response = Util.getRequiredFormatRes(mediaType,res);
					//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				}
				else
				{
					res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getJobTtlByStrByDept()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getJobTtlByStrByDept()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getJobTtlByStrByDept method in service" + response);
		return response;

	}

	/**
	 * This method will be used for loading the data into the Req Request form
	 * 
	 * @param
	 * 
	 * @return
	 */

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/loadReqRequestForm")
	public String loadReqRequestForm(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters loadReqRequestForm method in service...");
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			dlnReqMgr = new RetailStaffingRequisitionManager();
			res = dlnReqMgr.loadRequisitionRequestDetails();
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);

			// Call XMLHandler to generate the response XML.
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in loadReqRequestForm()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in loadReqRequestForm()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + " Leaving loadReqRequestForm method in service " + response);
		return response;

	}

	/**
	 * This method is used to submit request screen details.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/submitReqScrnDtls")
	public String submitReqScrnDetails(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters submitReqScrnDetails method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager retailStaffMgr = null;
		UpdateStaffingRequest submitRequisitionReqReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
			//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				submitRequisitionReqReq= (UpdateStaffingRequest)Util.getObjectFromInput(mediaType, request, UpdateStaffingRequest.class);
			//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				retailStaffMgr = new RetailStaffingRequisitionManager();
				res = retailStaffMgr.submitRequisitionRequestDetails(submitRequisitionReqReq);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in submitReqScrnDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in submitReqScrnDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving submitReqScrnDetails method in service" + response);
		return response;
	}
	
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getSchedDescription")
	public String getSchdCalDesc(@FormParam(FORM_PARAM_DATA) String strNo,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getSchdCalDesc method in service");
		String response = "";
		Response res = new Response();
		String msgDataVal = null;
		logger.info("input Store No" + strNo);
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(strNo != null && !strNo.trim().equals("") && LocationManager.isValidStoreNumber(strNo))
			{
				ScheduleDescResponse schDescRes = new ScheduleDescResponse();
				schDescRes.setSchDescList(ScheduleManager.getRequisitionCalendarsForStore(strNo, true));
				res.setScheduleRes(schDescRes);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				logger.info("The region list is" + response);
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getSchdCalDesc()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getSchdCalDesc()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getSchdCalDesc method in service" + response);
		return response;
	}

	// RSA3.0
	// applicant pool
	/**
	 * This method is used to update or insert the phone screen informations.
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getApplicantPool")
	public String getApplicantPool(@FormParam(FORM_PARAM_INPUT_DATA) String searchReq,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getApplicantPool method in service with req: " + searchReq);
		String response = "";

		ApplicantPoolResponse applPoolRes = new ApplicantPoolResponse();
		RetailStaffingApplicantPoolManager applicantPoolMgr = null;
		ApplicantPoolRequest applicantPoolReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(searchReq != null && !searchReq.trim().equals(""))
			{
				logger.info("input storeId" + searchReq);
				applicantPoolMgr = new RetailStaffingApplicantPoolManager();
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				applicantPoolReq= (ApplicantPoolRequest)Util.getObjectFromInput(mediaType, searchReq, ApplicantPoolRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				applPoolRes = applicantPoolMgr.getApplicantPool(applicantPoolReq);
				applPoolRes.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,applPoolRes);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				// }
			}
			else
			{
				applPoolRes.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getApplicantPool()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(applPoolRes, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getApplicantPool()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(applPoolRes, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getApplicantPool method in service" + response);
		return response;
	}

	/**
	 * This method is used to get the requisition details based on the input.
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getApplProfile")
	public String getApplProfile(@FormParam(FORM_PARAM_DATA) String profileID,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getApplProfile method in service" + profileID);
		String response = "";

		ApplProfileResponse applProfileRes = new ApplProfileResponse();
		RetailStaffingApplProfileManager profileMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(profileID != null && !profileID.trim().equals(""))
			{
				logger.info("input candidateId" + profileID);
				profileMgr = new RetailStaffingApplProfileManager();
				applProfileRes = profileMgr.getApplicantProfile(profileID);
				applProfileRes.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,applProfileRes);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				applProfileRes.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getApplProfile()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(applProfileRes, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getApplProfile()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(applProfileRes, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getApplProfile method in service" + response);
		return response;
	}

	// appl profile

	// attach applicant to requisition
	/**
	 * This method is used to get the requisition details based on the input.
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/attachApplToRequisition")
	public String attachApplToRequisition(@FormParam(FORM_PARAM_DATA) String applToReqParam,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{

		logger.info(this.getClass() + "Enters attachApplToRequisition method in service" + applToReqParam);

		String response = "";
		// SIR 4444
		ApplUnavailInfoResponse applUnavailInfoRes = null;

		RetailStaffingUpdateApplManager updateApplMgr = null;
		String msgDataVal = null;
		ApplToReqRequest applToReqRequest = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(applToReqParam != null && !applToReqParam.trim().equals(""))
			{
				logger.info("input candidateId" + applToReqParam);
				updateApplMgr = new RetailStaffingUpdateApplManager();
				applToReqRequest =(ApplToReqRequest) Util.getObjectFromInput(mediaType, applToReqParam, ApplToReqRequest.class);
				applUnavailInfoRes = updateApplMgr.attachApplToRequisition(applToReqRequest, mediaType);
				applUnavailInfoRes.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,applUnavailInfoRes);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in attachApplToRequisition()";
			Util.logFatalError(msgDataVal, de);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getApplProfile()";
			Util.logFatalError(msgDataVal, e);
		}
		logger.info(this.getClass() + "Leaving getApplProfile method in service" + response);

		return response;
	}

	// attach applicant to requisition

	// candidate count
	/**
	 * This method is used to get the candidate count on the input.
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getCandidateCount")
	public String getCandidateCount(@FormParam(FORM_PARAM_DATA) String reqNumber,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{

		logger.info(this.getClass() + "Enters getCandidateCount method in service" + reqNumber);

		String response = "";

		ApplPoolInfoResponse applPoolInfoRes = new ApplPoolInfoResponse();

		RetailStaffingApplPoolInfoManager applPoolManager = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(reqNumber != null && !reqNumber.trim().equals(""))
			{
				logger.info("input requisition number: " + reqNumber);
				applPoolManager = new RetailStaffingApplPoolInfoManager();
				applPoolInfoRes = applPoolManager.getCandidateCount(reqNumber);
				applPoolInfoRes.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,applPoolInfoRes);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				applPoolInfoRes.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getCandidateCount()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(applPoolInfoRes, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getCandidateCount()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(applPoolInfoRes, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getCandidateCount method in service" + response);

		return response;
	}

	// candidate count
	// RSA3.0

	/*
	 * Added For POM Call Back Handler
	 */

	@POST
	@Produces(APPLICATION_XML)
	@Path("/pomRsaStatusCrossRef")
	public String pomRsaStatusCrossRef(@Context HttpServletRequest request)
	{

		if(logger.isDebugEnabled())
		{
			logger.debug("this.getClass() + Enter into pomRsaStatusCrossRef()");
			logger.debug("Input request XML : " + request);
		}
		StringBuilder response = new StringBuilder(256);
		POMRsaStatusCrossRefRequest crossRefReq = null;
		ByteArrayOutputStream baos = null;
		String xml = null;
		String msgDataVal = "";

		try
		{
			if(request != null)
			{
				baos = new ByteArrayOutputStream();
				int bytesRead = 0;
				byte[] bytes = new byte[2048];
				while((bytesRead = request.getInputStream().read(bytes, 0, bytes.length)) != -1)
				{
					baos.write(bytes, 0, bytesRead);
				}
				xml = baos.toString();

				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("\n====================\nxml: %1$s", xml));
				}
				crossRefReq = (POMRsaStatusCrossRefRequest)XMLHandler.fromXML(xml);
				if(crossRefReq != null)
				{
					logger.debug("Request Object :" + crossRefReq.toString());
					POMRsaStatusCrossRefResponse responseValue = PhoneScreenManager.pomRsaCrossRef(crossRefReq);
					if(responseValue != null)
					{
						String responseXml = PhoneScreenManager.generatePOMResponseXML(responseValue);
						logger.debug("response Output :" + responseXml.toString());
						return responseXml;
					}
					else
					{
						response.append("<pomRsaStatusCrossRefResponse>");
						response.append("<status>false</status>");
						response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
						response.append("</pomRsaStatusCrossRefResponse>");
					}
				}
			}
			else
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("Input Request is Empty:");
				}
				response.append("<pomRsaStatusCrossRefResponse>");
				response.append("<status>false</status>");
				response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
				response.append("</pomRsaStatusCrossRefResponse>");
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, de);
			response.append("<pomRsaStatusCrossRefResponse>");
			response.append("<status>false</status>");
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</pomRsaStatusCrossRefResponse>");
		}
		catch (NumberFormatException ne)
		{
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, ne);
			response.append("<pomRsaStatusCrossRefResponse>");
			response.append("<status>false</status>");
			response.append(String.format("<error><errorMsg>Invalid Input Data</errorMsg></error>"));
			response.append("</pomRsaStatusCrossRefResponse>");
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, e);
			response.append("<pomRsaStatusCrossRefResponse>");
			response.append("<status>false</status>");
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</pomRsaStatusCrossRefResponse>");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("this.getClass() + Exit from pomRsaStatusCrossRef()");
		}
		return response.toString();
	}
	
	@POST
	@Produces(APPLICATION_XML)
	@Path("/pomRetailStaffingReconciliation")
	public String pomHandlerRetailStaffingReconciliation(@Context HttpServletRequest request)
	{

		if(logger.isDebugEnabled())
		{
			logger.debug("this.getClass() + Enter into pomHandlerRetailStaffingReconciliation()");
			logger.debug("Input request XML : " + request);
		}
		StringBuilder response = new StringBuilder(256);
		HrRetlStffReconsialtionRequest reconReq = null;
		ByteArrayOutputStream baos = null;
		String xml = null;
		String msgDataVal = "";
		try
		{
			if(request != null)
			{

				baos = new ByteArrayOutputStream();

				int bytesRead = 0;
				byte[] bytes = new byte[2048];

				while((bytesRead = request.getInputStream().read(bytes, 0, bytes.length)) != -1)
				{
					baos.write(bytes, 0, bytesRead);
				}
				xml = baos.toString();

				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("\n====================\nxml value: %1$s", xml));
				}
				reconReq = (HrRetlStffReconsialtionRequest)XMLHandler.fromXML(xml);
				if(reconReq != null)
				{
					logger.debug("Request Object :" + reconReq.toString());
					if(PhoneScreenManager.pomRsHandlerRecon(reconReq))
					{
						response.append("<pomRetailStaffingReconciliationResponse>");
						response.append(String.format("<HrRetlStffReconResult>true</HrRetlStffReconResult>"));
						response.append("</pomRetailStaffingReconciliationResponse>");
					}
					else
					{
						response.append("<pomRetailStaffingReconciliationResponse>");
						response.append(String.format("<HrRetlStffReconResult>false</HrRetlStffReconResult>"));
						response.append("</pomRetailStaffingReconciliationResponse>");
					}
				}
			}
			else
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("Input Request is Empty:");
				}
				response.append("<pomRsaStatusCrossRefResponse>");
				response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
				response.append("</pomRsaStatusCrossRefResponse>");
			}

		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, de);
			response.append("<pomRetailStaffingReconciliationResponse>");
			response.append(String.format("<HrRetlStffReconResult>false</HrRetlStffReconResult>"));
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</pomRetailStaffingReconciliationResponse>");
		}
		catch (NumberFormatException ne)
		{
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, ne);
			response.append("<pomRetailStaffingReconciliationResponse>");
			response.append(String.format("<HrRetlStffReconResult>false</HrRetlStffReconResult>"));
			response.append(String.format("<error><errorMsg>Invalid Input Data</errorMsg></error>"));
			response.append("</pomRetailStaffingReconciliationResponse>");
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, e);
			response.append("<pomRetailStaffingReconciliationResponse>");
			response.append(String.format("<HrRetlStffReconResult>false</HrRetlStffReconResult>"));
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</pomRetailStaffingReconciliationResponse>");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug("this.getClass() + Exit from pomHandlerRetailStaffingReconciliation()");
		}
		return response.toString();
	}

	@GET
	@Produces(APPLICATION_XML)
	@Path("/pomUpdatePhoneScreenStatus")
	public String pomUpdatePhoneScreenStatus(@QueryParam("phoneScreenId") String phoneScreenId, @QueryParam("status") String status)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug(this.getClass() + String.format("Enter pomUpdatePhoneScreenStatus()," + "phoneScrnId: %1$s, status: %2$s", phoneScreenId, status));
		}

		StringBuilder response = new StringBuilder(256);
		String msgDataVal = "";

		try
		{
			if(phoneScreenId == null || phoneScreenId.trim().length() < 1)
			{
				logger.error(String.format("Invalid PhoneScreen %1$s provided ", phoneScreenId));
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE, String.format("invalid phoneScreen id %1$s provided",
				    phoneScreenId));
			}

			if(status == null || status.trim().length() < 1)
			{
				logger.error(String.format("Invalid statusCode %1$s provided ", status));
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE, String.format("invalid status Value %1$s provided", status));
			}
			short statusCode = Short.parseShort(status.trim());
			if(statusCode <= 0)
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("Invalid Status code");
				}
				response.append("<Response>");
				response.append(String.format("<updated>false</updated>"));
				response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
				response.append("</updateFromPOMResponse>");

			}
			else
			{
				if(PhoneScreenManager.updateHumanResourcesPhoneScreenStatus(Integer.parseInt(phoneScreenId), statusCode))
				{
					logger.debug("Enter updateHumanResourcesPhoneScreenStatus success condition");
					response.append("<updateFromPOMResponse>");
					response.append(String.format("<updated>true</updated>"));
					response.append("</updateFromPOMResponse>");
				}
				else
				{
					logger.debug("Enter updateHumanResourcesPhoneScreenStatus failure condition");
					response.append("<updateFromPOMResponse>");
					response.append(String.format("<updated>false</updated>"));
					response.append("</updateFromPOMResponse>");
				}
			}

		}
		catch (RetailStaffingException de)
		{
			de.printStackTrace();
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, de);
			response.append("<updateFromPOMResponse>");
			response.append(String.format("<updated>false</updated>"));
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</updateFromPOMResponse>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, e);
			response.append("<updateFromPOMResponse>");
			response.append(String.format("<updated>false</updated>"));
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</updateFromPOMResponse>");
		}
		if(logger.isDebugEnabled())
		{
			logger.debug(this.getClass() + "Exit pomUpdatePhoneScreenStatus()");
		}
		return response.toString();
	}

	@GET
	@Produces(APPLICATION_XML)
	@Path("/pomUpdateScheduleStatus")
	public String pomUpdateScheduleStatus(@QueryParam("phoneScreenId") String phoneScreenId, @QueryParam("scheduleStatus") String scheduleStatus)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug(this.getClass()
			    + String.format("Enter pomUpdateScheduleStatus()," + "phoneScrnId: %1$s, statusCode: %2$s", phoneScreenId, scheduleStatus));
		}

		StringBuilder response = new StringBuilder(256);
		String msgDataVal = "";

		try
		{
			if(phoneScreenId == null || phoneScreenId.trim().length() < 1)
			{
				if(logger.isDebugEnabled())
				{
					logger.error(String.format("Invalid PhoneScreen %1$s provided ", phoneScreenId));
				}
				response.append("<updateResponse>");
				response.append(String.format("<updated>false</updated>"));
				response.append(String.format("<error><errorMsg>Invalid Input</errorMsg></error>"));
				response.append("</updateResponse>");
				return response.toString();
			}

			if(scheduleStatus == null || scheduleStatus.trim().length() < 1)
			{
				if(logger.isDebugEnabled())
				{
					logger.error(String.format("Invalid scheduleStatus %1$s provided ", scheduleStatus));
				}
				response.append("<updateResponse>");
				response.append(String.format("<updated>false</updated>"));
				response.append(String.format("<error><errorMsg>Invalid Input</errorMsg></error>"));
				response.append("</updateResponse>");
				return response.toString();
			}
			short statusCode = Short.parseShort(scheduleStatus.trim());
			if(statusCode <= 0)
			{
				if(logger.isDebugEnabled())
				{
					logger.error(String.format("Invalid scheduleStatus %1$s provided ", scheduleStatus));
				}
				response.append("<updateResponse>");
				response.append(String.format("<updated>false</updated>"));
				response.append(String.format("<error><errorMsg>Invalid Input</errorMsg></error>"));
				response.append("</updateResponse>");
				return response.toString();

			}
			else
			{
				PhoneScreenManager.updateInterviewStatusForCTI(Integer.parseInt(phoneScreenId), statusCode);
				response.append("<updateResponse><updated>true</updated></updateResponse>");
			}

		}
		catch (QueryException qe)
		{
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, qe);
			response.append("<updateResponse>");
			response.append(String.format("<updated>false</updated>"));
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</updateResponse>");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msgDataVal = "Exception : Error occured in RetailStaffingService: pomUpdateScheduleStatus()";
			Util.logFatalError(msgDataVal, e);
			response.append("<updateResponse>");
			response.append(String.format("<updated>false</updated>"));
			response.append(String.format("<error><errorMsg>Application Error</errorMsg></error>"));
			response.append("</updateResponse>");
		}

		if(logger.isDebugEnabled())
		{
			logger.debug(this.getClass() + "Exit pomUpdateScheduleStatus()");
		}
		return response.toString();
	}

	/**
	 * This method will be used to get the QP Pilot Status
	 * 
	 * @param
	 * 
	 * @return
	 */

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/checkQpPilotStatus")
	public String checkQpPilotStatus(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters checkQpPilotStatus method in service...");
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			dlnReqMgr = new RetailStaffingRequisitionManager();

			res = dlnReqMgr.getQpPilotResult();
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);

			// Call XMLHandler to generate the response XML.
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

			/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in checkQpPilotStatus()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in checkQpPilotStatus()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + " Leaving checkQpPilotStatus method in service " + response);
		return response;

	}
	
	/**
	 * This method will be used to get the stores that do not require a drivers license even though the job requires it.
	 * 
	 * @param
	 * 
	 * @return 
	 */

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 26 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getStoresDriverLicenseExempt")
	public String getStoresDriverLicenseExempt(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getStoresDriverLicenseExempt method in service...");
		String response = "";
		Response res = new Response();
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 26 May 2015
		String mediaType = contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 26 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			res.setStrDlExemptRes(DriversLicenseManager.getStoresDriversLicenseExempt());
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			
			//Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = Util.getRequiredFormatRes(mediaType,res);
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getStoresDriverLicenseExempt()";
			Util.logFatalError(msgDataVal, de);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 26 May 2015
			response = XMLHandler.createErrorXML(res, de, mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getStoresDriverLicenseExempt()";
			Util.logFatalError(msgDataVal, e);
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 26 May 2015
			response = XMLHandler.createErrorXML(res, e, mediaType);
		}
		logger.info(this.getClass() + " Leaving getStoresDriverLicenseExempt method in service " + response);
		return response;

	}

	/**
	 * This method will be used to get the list of requisitions by store for
	 * Interview Details and Offer results
	 * 
	 * @param
	 * 
	 * @return
	 */

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInterviewResultsReqsByStr")
	public String getInterviewResultsReqsByStr(@FormParam(FORM_PARAM_DATA) String strNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getInterviewResultsReqsByStr method in service...");
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewResultsManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			dlnReqMgr = new RetailStaffingInterviewResultsManager();
			logger.info("Store Number Passed = " + strNbr);
			res = dlnReqMgr.getInterviewResultsRequisitionDetails(strNbr);
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);

			// Call XMLHandler to generate the response XML.
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

			/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsReqsByStr()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsReqsByStr()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + " Leaving getInterviewResultsReqsByStr method in service " + response);
		return response;

	}

	/**
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInterviewResultsLoadScreen")
	public String getInterviewResultsLoadScreen(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getInterviewResultsLoadScreen method in service");
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewResultsManager dlnMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			dlnMgr = new RetailStaffingInterviewResultsManager();
			res = dlnMgr.getInterviewResultsLoadScreen();
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsLoadScreen()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsLoadScreen()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getInterviewResultsLoadScreen method in service" + response);
		return response;

	}

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInterviewResultsCandidatesByReq")
	public String getInterviewResultsCandidatesByReq(@FormParam(FORM_PARAM_DATA) String reqNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getInterviewResultsCandidatesByReq method in service...");
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewResultsManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			dlnReqMgr = new RetailStaffingInterviewResultsManager();
			logger.info("Req Number Passed = " + reqNbr);
			res = dlnReqMgr.getInterviewResultsCandidateDetails(reqNbr);
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);

			// Call XMLHandler to generate the response XML.
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsCandidatesByReq()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsCandidatesByReq()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + " Leaving getInterviewResultsCandidatesByReq method in service " + response);
		return response;

	}

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getInterviewResultsCandidateIntvwDtls")
	public String getInterviewResultsCandidateIntvwDtls(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getInterviewResultsCandidateIntvwDtls method in service...");
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewResultsManager dlnReqMgr = null;
		String msgDataVal = null;
		IntvwResultsCandIntvwDtlsRequest theRequest = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
			/** Deserialize the input data (XML /JSON) to java objects **/
			theRequest= (IntvwResultsCandIntvwDtlsRequest)Util.getObjectFromInput(mediaType, request, IntvwResultsCandIntvwDtlsRequest.class);
			//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
			if (logger.isDebugEnabled()) {
				logger.debug("Req Number Passed = " + theRequest.getReqNbr());
				logger.debug("Cand ID Passed = " + theRequest.getCandId());
				logger.debug("Store Number Passed = " + theRequest.getStrNo());
				logger.debug("Dept Number Passed = " + theRequest.getDeptNo());
				logger.debug("Job Code Passed = " + theRequest.getJobTtlCd()); 
				logger.debug("Applicant Type Passed = " + theRequest.getApplicantType());
				logger.debug("Organization Passed = " + theRequest.getOrganization());
			}

			dlnReqMgr = new RetailStaffingInterviewResultsManager();

			res = dlnReqMgr.getCandidateInterviewQuestionDetails(theRequest.getCandId(), theRequest.getReqNbr(), theRequest.getStrNo(), theRequest.getDeptNo(),
			    theRequest.getJobTtlCd(), theRequest.getApplicantType(), theRequest.getOrganization());
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);

			// Call XMLHandler to generate the response XML.
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsCandidateIntvwDtls()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getInterviewResultsCandidateIntvwDtls()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + " Leaving getInterviewResultsCandidateIntvwDtls method in service " + response);
		return response;

	}

	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/updateInterviewResultsCandidateIntvwDtls")
	public String updateInterviewResultsCandidateIntvwDtls(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType,
			@Context HttpServletRequest httpRequest)
	{
		logger.info(this.getClass() + "Enters updateInterviewResultsCandidateIntvwDtls method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewResultsManager intvwRltsMgr = null;
		UpdateIntvwRltsCandIntvwDtlsRequest updateReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				updateReq= (UpdateIntvwRltsCandIntvwDtlsRequest)Util.getObjectFromInput(mediaType, request, UpdateIntvwRltsCandIntvwDtlsRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				intvwRltsMgr = new RetailStaffingInterviewResultsManager();
				res = intvwRltsMgr.updateInterviewResultsCandidateIntvwDtls(updateReq, httpRequest);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in updateInterviewResultsCandidateIntvwDtls()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			de.setEndUserMessage(de.getTechnicalErrorMessage());
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in updateInterviewResultsCandidateIntvwDtls()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving updateInterviewResultsCandidateIntvwDtls method in service" + response);
		return response;
	}
	
	//RSA 7.0
	/**
	 * This method is used to resend the queweb email.
	 * @param request
	 * @return
	 */
	
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/resendQueWebEmail")
	public String resendQueWebEmail(@FormParam(FORM_PARAM_INPUT_DATA) String reqnNbr,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass()
				+ "Enters resendQueWebEmail method in service" + reqnNbr);
		String response = "";
		Response res = new Response();
		RetailStaffingRequisitionManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try 
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if (reqnNbr != null && !reqnNbr.trim().equals("")) {
				logger.info("input requisition nbr" + reqnNbr);
				dlnReqMgr = new RetailStaffingRequisitionManager();
				res = dlnReqMgr.resendQueWebEmailBatch(reqnNbr);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLGenerator to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			} else {
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(
						RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		} catch (RetailStaffingException de) {
			msgDataVal = "Exception : Error occured in resendQueWebEmail()";
			Util.logFatalError(msgDataVal, de);
			
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		} catch (Exception e) {
			msgDataVal = "Exception : Error occured in resendQueWebEmail()";
			Util.logFatalError(msgDataVal, e);
			
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass()
				+ "Leaving resendQueWebEmail method in service" + response);
		return response;

	}
	
	/**
	 * This method is used to get the available Interview Time details based on the input.
	 * 
	 * @param request
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getAvailableInterviewTimes")
	public String getAvailableInterviewTimes(@QueryParam("rscSchdFlg") String rscSchdFlg, @QueryParam("reqCalId") int reqCalId, @QueryParam("interviewDuration") String interviewDuration,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(String.format("Enters getAvailableInterviewTimes method in service with rscSchdFlg: %1$s, reqCalId: %2$d, interviewDuration: %3$s", rscSchdFlg, reqCalId, interviewDuration));
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewManager dlnReqMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(rscSchdFlg != null && !rscSchdFlg.trim().equals("") && interviewDuration != null && !interviewDuration.trim().equals("") && reqCalId != -1)
			{
				dlnReqMgr = new RetailStaffingInterviewManager();
				res = dlnReqMgr.readHumanRequisitionSchedule(rscSchdFlg, reqCalId, interviewDuration);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getAvailableInterviewTimes()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getAvailableInterviewTimes()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info("Leaving getAvailableInterviewTimes method in service " + response);
		return response;

	}
	
	//RSA 7.2
	/**
	 * This service method will return the latest 
	 * rejection reason for this candidate for this requisition
	 * 
	 * @return int   
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getLatestRejectReason")
	public String getLatestRejectReason(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getLatestRejectReason method in service");
		String response = "";
		Response res = new Response();
		RetailStaffingApplProfileManager profileMgr = null;
		String msgDataVal = null;
		GetLatestRejectionRequest getLatestRejectDetails = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		//Commented as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
		/** Deserialize the input data (XML /JSON) to java objects **/
		
		
		try
		{
			//Start - Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
			getLatestRejectDetails= (GetLatestRejectionRequest)Util.getObjectFromInput(mediaType, request, GetLatestRejectionRequest.class);
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			//End - Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
			profileMgr = new RetailStaffingApplProfileManager();
			res = profileMgr.getLatestRejectReason(getLatestRejectDetails);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			logger.info("The latest reject reason is" + response);
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getLatestRejectReason()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getLatestRejectReason()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getLatestRejectReason method in service" + response);
		return response;

	}
	
	//RSA 7.2
	/**
	 * The service method will return the list of all available
	 * rejection reasons
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getRejectDetails")
	public String getRejectDetails(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getRejectDetails method in service");
		String response = "";
		Response res = new Response();
		RetailStaffingApplProfileManager profileMgr = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			profileMgr = new RetailStaffingApplProfileManager();
			res = profileMgr.getRejectDetails();
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

			logger.info("The reject reason list is" + response);
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getRejectDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getRejectDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving getRejectDetails method in service" + response);
		return response;
	}
	
	/**
	 * This method will be used for creating rejection details for a candidate reqn combination
	 * 
	 * @param request
	 *            - The object containing the requisition and candidate
	 *            details //see saveRevPhnScrnDtl for example
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/createRejectDetails")
	public String createRejectDetails(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters createRejectDetails method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingApplProfileManager profileMgr = new RetailStaffingApplProfileManager();
		String msgDataVal = null;
		CreateRejectionRequest rejTO = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{

				
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015

				/** Deserialize the input data (XML /JSON) to java objects **/
				rejTO= (CreateRejectionRequest)Util.getObjectFromInput(mediaType, request, CreateRejectionRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				res = profileMgr.createEmploymentPositionCandidateRejected(rejTO);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in createRejectDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in createRejectDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving updateRevPhnScrnDetails method in service" + response);
		return response;

	}
	
	/**
	 * This method will be used for marking applicants and associates as Considered in the QP
	 * Even though all the methods here and the ones called only reference Applicants.  Both are
	 * being written to the same table with the same data. 
	 * @param request
	 *            - The object containing the requisition and candidate id and code
	 *           
	 * 
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	// as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/markApplicantsAsConsideredInQP")
	public String markApplicantsAsConsideredInQP(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		long startTime = 0;
		if (logger.isDebugEnabled()) {
			startTime = System.nanoTime();
			logger.debug(String.format("Entering Service markApplicantsAsConsideredInQP()"));
		}
		
		String response = "";
		Response res = new Response();
		RetailStaffingApplProfileManager profileMgr = new RetailStaffingApplProfileManager();
		String msgDataVal = null;
		QPConsideredApplicantRequest requestTO = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				requestTO= (QPConsideredApplicantRequest)Util.getObjectFromInput(mediaType, request, QPConsideredApplicantRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				profileMgr.markApplicantsAsConsideredInQP(requestTO);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in markApplicantsAsConsideredInQP()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in markApplicantsAsConsideredInQP()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		if (logger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			logger.debug("\n" + response);
			logger.debug(String.format("Exiting Service markApplicantsAsConsideredInQP(). Total time to process request: %1$.9f seconds", (((double) endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return response;

	}
	
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON })
	@Path("/getCandidateRehireDtls")
	public String getCandidateRehireDtls(@QueryParam("applicantId") String applicantId,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{
		logger.info(this.getClass() + "Enters getCandidateRehireDtls method in service...");
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewResultsManager dlnReqMgr = null;
		String msgDataVal = null;

		String mediaType = MediaType.APPLICATION_JSON;

		try
		{
			dlnReqMgr = new RetailStaffingInterviewResultsManager();

			res = dlnReqMgr.getCandidateRehireDetails(applicantId);
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			response = Util.getRequiredFormatRes(mediaType,res);
			
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getCandidateRehireDtls()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getCandidateRehireDtls()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + " Leaving getCandidateRehireDtls method in service " + response);
		return response;

	}

	/** This method will be used to post the completed Time Stamp for the Drug Test Panel
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
	@Path("/updateCandidateDTCompTS")
	public String updateCandidateDTDBSvc(@FormParam("EMPLT_APLCNT_ID") String EMPLT_APLCNT_ID, @FormParam("EMPLT_REQN_NBR") int EMPLT_REQN_NBR, @FormParam("DTEST_ORD_NBR") long DTEST_ORD_NBR, 
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType, @DefaultValue("1") @QueryParam("version") int version) {

		long startTime = 0;
		if (logger.isDebugEnabled()) {
			startTime = System.nanoTime();
			logger.debug(String.format("Entering method updateCandidateDTCompTS() in service, with Drug Test Order Number:%1$s", DTEST_ORD_NBR));
		}
		
		String result = null;
		Response res = new Response();

		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType = contentType;
		try {

			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 7 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if (DTEST_ORD_NBR != 0) {

				RetailStaffingInterviewResultsManager.updateApplicantDrugTestOrder(EMPLT_APLCNT_ID, EMPLT_REQN_NBR, DTEST_ORD_NBR);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			} else {
				throw new Exception("Invalid Drug Test Order Number provided.");
			}
			
			// Call XMLHandler to generate the response XML.
			/** Serialize the java objects into required format (XML/JSON) **/
			result = Util.getRequiredFormatRes(mediaType,res);

		} catch (Exception e) {
			String msgDataVal = "Exception : Error occured in updating the DB with the completed Time Stamp from First Advantage";
				Util.logFatalError(msgDataVal, e);
				
				/** Prepare ErrorMessage in required Format **/
				GenericErrorTO errorRes = new GenericErrorTO();
				errorRes.setEndUserErrorMsg(msgDataVal);
				res.setErrorResponse(errorRes);
			}

		if (logger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			logger.debug("\n" + result);
			logger.debug(String.format("Exiting method updateCandidateDTDBSvc() in Service. Total time to process request: %1$.9f seconds", (((double) endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		
		return result;

	}
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/submitBackgroundConsentByCandidate")
	public String submitBackgroundConsentByCandidate(@FormParam(FORM_PARAM_DATA) String request,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType,
			@Context HttpServletRequest httpRequest)
	{
		logger.info(this.getClass() + "Enters submitBackgroundConsentByCandidate method in service" + request);
		String response = "";
		Response res = new Response();
		RetailStaffingInterviewResultsManager intvwRltsMgr = null;
		SubmitBgcCandidateDtlsRequest updateReq = null;
		String msgDataVal = null;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			if(request != null && !request.trim().equals(""))
			{
				//Start - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				/** Deserialize the input data (XML /JSON) to java objects **/
				updateReq= (SubmitBgcCandidateDtlsRequest)Util.getObjectFromInput(mediaType, request, SubmitBgcCandidateDtlsRequest.class);
				//End - Commented and Added as part of converting XML/JSON to object- For Flex to HTML Conversion - 13 May 2015
				intvwRltsMgr = new RetailStaffingInterviewResultsManager();
				res = intvwRltsMgr.initiateWebBgcOrder(updateReq, httpRequest);
				res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
				
				// Call XMLHandler to generate the response XML.
				//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
				/** Serialize the java objects into required format (XML/JSON) **/
				response = Util.getRequiredFormatRes(mediaType,res);
				//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			}
			else
			{
				res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in updateInterviewResultsCandidateIntvwDtls()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			de.setEndUserMessage(de.getTechnicalErrorMessage());
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in updateInterviewResultsCandidateIntvwDtls()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaype as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		logger.info(this.getClass() + "Leaving updateInterviewResultsCandidateIntvwDtls method in service" + response);
		return response;
	}
	
}
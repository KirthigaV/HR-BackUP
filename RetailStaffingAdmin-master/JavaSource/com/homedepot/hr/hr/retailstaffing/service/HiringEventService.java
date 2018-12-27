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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.HiringEventsStoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.GenericErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.HiringEventResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.model.HiringEventsManager;
import com.homedepot.hr.hr.retailstaffing.model.LocationManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;

@Path("/HiringEventService")
public class HiringEventService implements Constants, Service, RetailStaffingConstants
{
	private static final Logger mLogger = Logger.getLogger(HiringEventService.class);

	/**
	 * @param mediaType
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
    // as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/loadStateData")
	public String loadStateData(@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{

		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering loadStateData()"));
		} // end if
		
		String response = "";
		Response res = new Response();
		String msgDataVal = null;

		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			res = HiringEventsManager.loadStateData();
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in loadStateData()";
			Util.logFatalError(msgDataVal, de);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Prepare ErrorMessage in required Format **/
			response = XMLHandler.createErrorXML(res, de,mediaType);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in loadStateData()";
			Util.logFatalError(msgDataVal, e);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Prepare ErrorMessage in required Format **/
			response = XMLHandler.createErrorXML(res, e,mediaType);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }			
			mLogger.debug(String.format("Exiting loadStateData(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return response;
	}
	
	/**
	 * @param ldapId
	 * @param associateId
	 * @param mediaType
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
    // as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getHiringEventMgrData")
	public String getHiringEventMgrData(@QueryParam("ldapId") String ldapId, @QueryParam("associateId") String associateId,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{

		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Service getHiringEventMgrData() with LDAP ID:%1$s or Associate ID:%2$s", ldapId, associateId));
		} // end if
		
		String response = "";
		Response res = new Response();
		String msgDataVal = null;
		HiringEventResponse hiringEventResponse = new HiringEventResponse();
		
		try
		{
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			hiringEventResponse.setHiringEventMgrTO(HiringEventsManager.getHiringEventMgrData(ldapId, associateId));
			res.setHiringEventResponse(hiringEventResponse);
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (NoRowsFoundException nrf)
		{
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			GenericErrorTO errorRes = new GenericErrorTO(); 
			errorRes.setEndUserErrorMsg("Associate Data Not Found");
			res.setErrorResponse(errorRes);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015

			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getHiringEventMgrData()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getHiringEventMgrData()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }			
			mLogger.debug(String.format("Exiting Service getHiringEventMgrData(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return response;
	}	
	
	/**
	 * @param hireEventId
	 * @param version
	 * @param mediaType
	 * @return
	 */
	/**
	 * Description: This method is used to get the hiring event details for the given hiring event id
	 * @param hireEventId
	 * @param timeZoneCode
	 * @param interviewDate
	 * @param version
	 * @param contentType
	 * @return response
	 * 
	 * Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
	 * as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	 */
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getHiringEventDetails")
	public String getHiringEventDetails(@QueryParam("hireEventId") int hireEventId, 
			@DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{

		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Service getHiringEventDetails() with Hire Event ID:%1$s", hireEventId));
		} // end if
		
		String response = "";
		Response res = new Response();
		String msgDataVal = null;
		HiringEventResponse hiringEventResponse = new HiringEventResponse();

		try
		{		
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			//Validate that Hiring Event Id is greater than zero
			ValidationUtils.validateGreaterThan(InputField.HIRE_EVNT_ID, hireEventId, 0);
			
			//Get Hiring Event Data
			hiringEventResponse.setHiringEventDetailTO(HiringEventsManager.getHiringEventDetail(hireEventId));

			if (hiringEventResponse.getHiringEventDetailTO().getEmgrHumanResourcesAssociateId() != null) {
				//Get Hiring Event Mgr Data
				hiringEventResponse.setHiringEventMgrTO(HiringEventsManager.getHiringEventMgrData(null, hiringEventResponse.getHiringEventDetailTO().getEmgrHumanResourcesAssociateId()));				
			}
			
			res.setHiringEventResponse(hiringEventResponse);
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			
		
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (NoRowsFoundException nrf)
		{
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			GenericErrorTO errorRes = new GenericErrorTO(); 
			errorRes.setEndUserErrorMsg("Hiring Event Data Not Found");
			res.setErrorResponse(errorRes);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getHiringEventDetails()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			GenericErrorTO errorRes = new GenericErrorTO(); 
			errorRes.setEndUserErrorMsg("Invalid Input");
			res.setErrorResponse(errorRes);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		} // end catch
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getHiringEventDetails()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(XMLHandler.toXML(res));
			mLogger.debug(response);
			long endTime = System.nanoTime();
			startTime = (startTime == 0) ? endTime : startTime;			
			mLogger.debug(String.format("Exiting Service getHiringEventDetails(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return response;
	}
	
	/**
	 * @param hireEventId
	 * @param reqnCalId
	 * @param version
	 * @param mediaType
	 * @return
	 */
	// Commented @Produces(APPLICATION_XML) and  Added @Produces and @Consumes, method argument mediaType as part of converting object to XML/JSON.
    // as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015 
	@GET
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getHiringEventDetailsForEdit")
	public String getHiringEventDetailsForEdit(@QueryParam("hireEventId") int hireEventId, @QueryParam("reqnCalId") int reqnCalId, @DefaultValue("1") @QueryParam("version") int version,
			@DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
	{

		long startTime = 0;
		//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
		String mediaType =  contentType;
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Service getHiringEventDetailsForEdit() with Hire Event ID:%1$s", hireEventId));
		} // end if
		
		String response = "";
		Response res = new Response();
		String msgDataVal = null;
		HiringEventResponse hiringEventResponse = new HiringEventResponse();
		

		try
		{			
			//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 13 May 2015
			mediaType = Util.getMediaType(mediaType);
			
			//Validate that Hiring Event Id is greater than zero
			ValidationUtils.validateGreaterThan(InputField.HIRE_EVNT_ID, hireEventId, 0);
			
			//Get Hiring Event Data
			hiringEventResponse.setHiringEventDetailTO(HiringEventsManager.getHiringEventDetail(hireEventId));

			if (hiringEventResponse.getHiringEventDetailTO().getEmgrHumanResourcesAssociateId() != null) {
				//Get Hiring Event Mgr Data
				hiringEventResponse.setHiringEventMgrTO(HiringEventsManager.getHiringEventMgrData(null, hiringEventResponse.getHiringEventDetailTO().getEmgrHumanResourcesAssociateId()));				
			}
			
			//Get Participating Stores, this will have the needed reqnCalId in all the returned data
			List<HiringEventsStoreDetailsTO> storeList = HiringEventsManager.getHiringEventParticipatingStores(hireEventId);
			hiringEventResponse.setParticipatingStores(storeList);
			
			//Get the eventReqnCalId from the storeList so that Event Name and created by Store can be added to the HiringEventDetail
			if (storeList.size() > 0) {
				HiringEventsStoreDetailsTO oneRecord = (HiringEventsStoreDetailsTO) storeList.get(0);
				if (mLogger.isDebugEnabled()) {
					mLogger.debug(String.format("Returned eventReqnCalId:%1$d", oneRecord.getEventReqnCalId()));
				}
				RequisitionCalendarTO requisitionCalendarTO = null;
				requisitionCalendarTO = HiringEventsManager.getHiringEventRequisitionCalendarForReqCalId(oneRecord.getEventReqnCalId());
				hiringEventResponse.getHiringEventDetailTO().setHireEventName(requisitionCalendarTO.getRequisitionCalendarDescription());
				hiringEventResponse.getHiringEventDetailTO().setHireEventCreatedByStore(requisitionCalendarTO.getEventCreatedByStore());
			} else {
				throw new RetailStaffingException();
			}
			
			//Get the Timezone data and set it on the response
			StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(hiringEventResponse.getHiringEventDetailTO().getHireEventCreatedByStore());
			if (store != null) {
				hiringEventResponse.getHiringEventDetailTO().setHireEventCreatedByStoreTimezone(store.getTimeZoneCode());
			} else {
				throw new RetailStaffingException();
			}
			
			res.setHiringEventResponse(hiringEventResponse);
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS);
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(XMLHandler.toXML(res));
			}
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(response);
			}
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (NoRowsFoundException nrf)
		{
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			GenericErrorTO errorRes = new GenericErrorTO(); 
			errorRes.setEndUserErrorMsg("Hiring Event Data Not Found");
			res.setErrorResponse(errorRes);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		}
		catch (RetailStaffingException de)
		{
			msgDataVal = "Exception : Error occured in getHiringEventDetailsForEdit()";
			Util.logFatalError(msgDataVal, de);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, de,mediaType);
		}
		catch(ValidationException ve)
		{
			// log the error
			mLogger.error(ve.getMessage(), ve);
			res.setStatus(RetailStaffingConstants.ERROR_APP_STATUS);
			GenericErrorTO errorRes = new GenericErrorTO(); 
			errorRes.setEndUserErrorMsg("Invalid Input");
			res.setErrorResponse(errorRes);
			//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			/** Serialize the java objects into required format (XML/JSON) **/
			response = Util.getRequiredFormatRes(mediaType,res);
			//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		} // end catch
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in getHiringEventDetailsForEdit()";
			Util.logFatalError(msgDataVal, e);
			/** Prepare ErrorMessage in required Format **/
			//Added argument mediaType as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
			response = XMLHandler.createErrorXML(res, e,mediaType);
		}
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }			
			mLogger.debug(String.format("Exiting Service getHiringEventDetailsForEdit(). Total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(response);
		}
		return response;
	}

}

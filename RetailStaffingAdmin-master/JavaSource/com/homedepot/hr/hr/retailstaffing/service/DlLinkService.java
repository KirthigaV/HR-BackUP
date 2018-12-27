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

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.model.DriversLicenseManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;

/**
 * RESTful web service containing methods related to Retail Staffing Admin Drivers License records 
 */
@Path("/DlLinkService")
public class DlLinkService implements Service, RetailStaffingConstants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(RetailStaffingService.class);

	/**
	 * Web service method used to get Drivers License Data from the RSA System
	 *
	 * @param aid				Associate ID
	 * @param recordId			Internal Record Id 
	 * @param version			Version of the response XML, will default to version 0 if not specified
	 * 
	 * @return					HTML with the Drivers License Data or error message if an error occurred
	 */	
	@GET
	@Produces({MediaType.TEXT_HTML})
	@Path("/getData")
	public String getDlData(@Context HttpServletRequest request, @QueryParam("aid") String aid, @QueryParam("recordId") int recordId, @DefaultValue("0") @QueryParam("version") int version)
	{
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getDlData(), aid: %1$s, recordId: %2$s, version: %3$d", aid, recordId, version));
		}
		
		String response = null;
		
		try
		{
			//This has been turned off due to the Internal DMV check now being ordered automatically.
			
			// validate an Associate ID was provided
			/*if(aid == null || aid.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s Associate ID provided", (aid == null ? "Null" : "Empty")));
			}
			
			// validate a valid recordId was provided
			if(recordId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid Record Id %1$d provided", recordId));
			} 
			
			// validate the version provided is supported
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			}*/
			
			//response = DriversLicenseManager.getDriversLicenseData(aid, recordId, request);
			response = "<b>This link has been turned off and is no longer valid.</b><br><br>";
			
		//} // end try
		//catch (RetailStaffingException e){
		//	response = "<b>An Error occured getting Drivers License Data.</b><br><br>";
		}
		catch(IllegalArgumentException iae)
		{
			mLogger.error(String.format(iae.getMessage()), iae);
			response = "<b>An Error occured getting Drivers License Data.</b><br><br>";			
		} // end catch()		
		catch(Exception e)
		{
			mLogger.error(String.format(e.getMessage()), e);
			response = "<b>An Error occured getting Drivers License Data.</b><br><br>";
		} // end catch
		
		return response;
	}
}
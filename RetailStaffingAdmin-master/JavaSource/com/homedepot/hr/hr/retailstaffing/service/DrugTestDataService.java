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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.homedepot.hr.hr.retailstaffing.dto.response.InitiateDrugTestActionTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.VoltageServiceResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.CookieUtils;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
/*import java.util.Calendar;*/
/*import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitCandidatePersonalDataRequest;*/
/*import com.homedepot.hr.hr.retailstaffing.model.CandidateDataFormManager;*/
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Path("/DrugTestDataService")
public class DrugTestDataService implements Constants, Service, RetailStaffingConstants
{
	private static final Logger mLogger = Logger.getLogger(DrugTestDataService.class);
	
	@POST
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/getOrderNbr")
	public String getOrderNbr(@FormParam("applicantId") long applicantId, @FormParam("requisitionId") long requisitionId, @FormParam("testType") String testType, @FormParam("testReasonType") String testReasonType, @FormParam("requesterLdap") String requesterLdap, @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType,
			@DefaultValue("1") @QueryParam("version") int version)
			{
		
		final String USER_AGENT = "Mozilla/5.0";
		InitiateDrugTestActionTO initiateDrugTestActionTO = new InitiateDrugTestActionTO();
		Gson gson = new Gson();
		String response = "";
		
		try {

			String strLCEnv = com.homedepot.ta.aa.util.TAAAResourceManager.getProperty("host.LCP");
			String hostName = "";
			
			if (strLCEnv.equals("PR")) {
				hostName = "drugtestapiservices.apps.homedepot.com";
			}  else  {
			    hostName = "drugtestapiservices.apps-np.homedepot.com";
			} 
			
			mLogger.debug("Host Name:" + hostName);
			
			URL url = new URL("https://" + hostName + "/api/orderTest/applicantId/"+applicantId+"/requisitionId/"+requisitionId+"/testType/"+testType+"/testReasonType/"+testReasonType+"/requesterLdap/"+requesterLdap);
			
			/*HttpsURLConnection con;

			con = (HttpsURLConnection) url.openConnection();
		
			//Add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");*/

			Client client = Client.create();

			WebResource webResource = client.resource(url.toString());

			ClientResponse clientResponse = webResource.post(ClientResponse.class);

			if (clientResponse != null) {
				initiateDrugTestActionTO = gson.fromJson(clientResponse.getEntity(String.class), InitiateDrugTestActionTO.class);
				response = gson.toJson(initiateDrugTestActionTO);
			} else {
				throw new Exception("An error occured getting data from First Advantage.");
			}
			
		} catch (Exception e) {
			Util.logFatalError("Invalid response from Drug Test API", e);
			initiateDrugTestActionTO.setSuccess(false);
			initiateDrugTestActionTO.setMessage("Invalid response");
			response = gson.toJson(initiateDrugTestActionTO);
		}
		mLogger.debug(response);
		return response;  	
	}
	
	
}
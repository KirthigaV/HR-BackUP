/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: VoltageManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.response.VoltageServiceResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.util.CookieUtils;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.util.TAAAResourceManager;

public class VoltageManager implements RetailStaffingConstants
{
	private static final Logger mLogger = Logger.getLogger(VoltageManager.class);

	//The Voltage Dev server does not have an entry in DNS, therefore you have to point to a local Key Store
	private static void setLocalKeyStore() {
		System.setProperty(LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_NAME, LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_VALUE);			
		System.setProperty(LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_NAME, LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_VALUE); 
		//For localhost SSL 
		System.setProperty(LOCALHOST_TRUST_STORE_PROPERTY_NAME, LOCALHOST_TRUST_STORE_PROPERTY_VALUE);			
		System.setProperty(LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_NAME, LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_VALUE);
	}
	
	/**
	 * Based on the life cycle and the requested URL Type this method returns the correct URL to the Voltage Web Service Client 
	 * @param returnURLType The type of URL needed.  Protect SSN, Protect DL, or Access SSN
	 * @return returnURL requested URL to the Voltage Web Service 
	 * @throws RetailStaffingException
	 */
	private static String getVoltageURL(int returnURLType) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getVoltageURL() with returnURLType: %1$s", returnURLType));
		}

		String lifeCycle = null;
		String ssnAccessUrl = null;
		String ssnProtectUrl = null;
		String dlProtectUrl = null;
        String returnURL = null;
        
		try
		{
			lifeCycle = TAAAResourceManager.getProperty(LIFE_CYCLE_PROPERTY);
			if (lifeCycle != null) {
				if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_AD)) {
					setLocalKeyStore();
					ssnAccessUrl = SSN_ACCESS_URL_DEV;
					ssnProtectUrl = SSN_PROTECT_URL_DEV;
					dlProtectUrl = DL_PROTECT_URL_DEV;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_QA)) {
					ssnAccessUrl = SSN_ACCESS_URL_QA;
					ssnProtectUrl = SSN_PROTECT_URL_QA;
					dlProtectUrl = DL_PROTECT_URL_QA;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_QP)) {
					ssnAccessUrl = SSN_ACCESS_URL_QP;
					ssnProtectUrl = SSN_PROTECT_URL_QP;
					dlProtectUrl = DL_PROTECT_URL_QP;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_ST)) {
					ssnAccessUrl = SSN_ACCESS_URL_ST;
					ssnProtectUrl = SSN_PROTECT_URL_ST;
					dlProtectUrl = DL_PROTECT_URL_ST;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_PR)) {
					ssnAccessUrl = SSN_ACCESS_URL_PR;
					ssnProtectUrl = SSN_PROTECT_URL_PR;
					dlProtectUrl = DL_PROTECT_URL_PR;
				} else {
					ssnAccessUrl = SSN_ACCESS_URL_PR;
					ssnProtectUrl = SSN_PROTECT_URL_PR;
					dlProtectUrl = DL_PROTECT_URL_PR;
				}
			} else {
				throw new Exception("Life Cycle returned NULL.");
			}			
		
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred getting Voltage URL", e);
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		
		switch (returnURLType) {
		case SSN_PROTECT:
			returnURL = ssnProtectUrl;
			break;
		case SSN_ACCESS:
			returnURL = ssnAccessUrl;
			break;
		case DL_PROTECT:
			returnURL = dlProtectUrl;
			break;			
		}

		return returnURL;
	}
	
	
	public static String protectDriversLicense(String inData, HttpServletRequest request) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering protectDriversLicense() with inData: %1$s", inData));
		}

	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    VoltageServiceResponse outData = new VoltageServiceResponse();
	    HttpPost httpPost = null;
	    HttpResponse vResponse = null;
        HttpEntity entity = null;
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(1);
        
		try
		{
			//Get the SSO Cookie
			Cookie thdSSO = CookieUtils.getSSOCookie(request);
					
			//Setup Cookie information to be passed to Voltage
			CookieStore cookieStore = httpclient.getCookieStore();
		    BasicClientCookie cookie = new BasicClientCookie(thdSSO.getName(), thdSSO.getValue());
		    mLogger.debug("Server Name:" + request.getServerName());
		    cookie.setDomain(request.getServerName());
		    cookie.setPath("/");
		    cookieStore.addCookie(cookie);
		    httpclient.setCookieStore(cookieStore);			
			
		    //Call Voltage to encrypt DL			
            httpPost = new HttpPost(getVoltageURL(DL_PROTECT));
            params = new ArrayList<BasicNameValuePair>(1);
            params.add(new BasicNameValuePair("data", StringUtils.trim(inData)));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            vResponse = httpclient.execute(httpPost);
            entity = vResponse.getEntity();

            if (entity != null) {
            	mLogger.debug("Response content length: " + entity.getContentLength());
            	outData = (VoltageServiceResponse) XMLHandler.fromXML(entity.getContent());
            	//If error details is not null throw and exception based on the response from Voltage Service
            	if (outData.getErrorDetails() != null) {
            		throw new Exception("Error Code:" + outData.getErrorDetails().getErrorCode() + " Error Message:" + outData.getErrorDetails().getErrorMessage());
            	}
            }			
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred fetching DL Data in getDriversLicenseData()", e);
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}

		return outData.getOutData();
	}	
}

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingStoreManager.java
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

import com.homedepot.hr.hr.retailstaffing.dao.DriversLicenseEncryptionDAO;
import com.homedepot.hr.hr.retailstaffing.dto.DriversLicenseDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDriverLicenseExemptResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.VoltageServiceResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.util.CookieUtils;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.util.TAAAResourceManager;

public class DriversLicenseManager implements RetailStaffingConstants
{
	private static final Logger mLogger = Logger.getLogger(DriversLicenseManager.class);

	//The Voltage Dev server does not have an entry in DNS, therefore you have to point to a local Key Store
	private static void setLocalKeyStore() {
		System.setProperty(LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_NAME, LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_VALUE);			
		System.setProperty(LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_NAME, LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_VALUE); 
		//For localhost SSL 
		System.setProperty(LOCALHOST_TRUST_STORE_PROPERTY_NAME, LOCALHOST_TRUST_STORE_PROPERTY_VALUE);			
		System.setProperty(LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_NAME, LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_VALUE);
	}
	
	/**
	 * This method will be used for saving the Drivers License data from the
	 * Interview Results Form.
	 * 
	 * @param candidateId
	 *            , dlNum, dlExpDate, dlState
	 * 
	 * @return internalAssociateDriverId
	 * @throws RetailStaffingException
	 */
	public int saveDriversLicenseData(String aid, String dlNum, String dlExpDate, String dlState) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format(
			    "Entering saveDriversLicenseData() Associate Id: %1$s, Drivers License Num: %2$s, License Exp Date: %3$s, License State: %4$s", aid, dlNum,
			    dlExpDate, dlState));
		}

		int internalAssociateDriverId = 0;

		try
		{
			if(aid != null && !aid.trim().equals("") && dlNum != null && !dlNum.trim().equals("") && dlExpDate != null && !dlExpDate.trim().equals("")
			    && dlState != null && !dlState.trim().equals(""))
			{
				// Encrypt Drivers License Number
				byte[] encryptedDlNumber = EncryptionManager.encryptItem(dlNum);

				// Insert record into License Table
				internalAssociateDriverId = Integer.parseInt(DriversLicenseEncryptionDAO.createInternalAssociateDriver(aid, encryptedDlNumber, Util
				    .convertStringDate(dlExpDate.trim()), dlState.trim()));
			}
			else
			{
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Interview Results in saveDriversLicenseData()", e);
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting saveDriversLicenseData() Drivers License Num: %1$s, License Exp Date: %2$s, License State: %3$s", dlNum,
			    dlExpDate, dlState));
		}
		return internalAssociateDriverId;
	}

	/**
	 * This method will be used for fetching the Drivers License data from the
	 * DB.
	 * 
	 * @param aid
	 *            , recordId
	 * 
	 * @return HTML of returned data
	 * @throws RetailStaffingException
	 */
	public static String getDriversLicenseData(String aid, int recordId, HttpServletRequest request) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getDriversLicenseData() Associate Id: %1$s, recordId: %2$d", aid, recordId));
		}

		StringBuilder response = new StringBuilder(300);
		String decryptedLicNum = null;
		String lifeCycle = null;
		String ssnUrl = null;
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    VoltageServiceResponse outData = new VoltageServiceResponse();
	    HttpPost httpPost = null;
	    HttpResponse vResponse = null;
        HttpEntity entity = null;
        List params = new ArrayList(1);
        
		try
		{

			DriversLicenseDetailsTO dlData = DriversLicenseEncryptionDAO.readDriversLicenseDetailsByAidRecordId(aid, recordId);

			// Decrypt the SSN via Voltage
			//Get the SSO Cookie
			Cookie thdSSO = CookieUtils.getSSOCookie(request);
			
			//Set SSN and DL URL's based on lifecycle
			lifeCycle = TAAAResourceManager.getProperty(LIFE_CYCLE_PROPERTY);
			if (lifeCycle != null) {
				if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_AD)) {
					setLocalKeyStore();
					ssnUrl = SSN_ACCESS_URL_DEV;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_QA)) {
					ssnUrl = SSN_ACCESS_URL_QA;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_QP)) {
					ssnUrl = SSN_ACCESS_URL_QP;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_ST)) {
					ssnUrl = SSN_ACCESS_URL_ST;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_PR)) {
					ssnUrl = SSN_ACCESS_URL_PR;
				} else {
					ssnUrl = SSN_ACCESS_URL_PR;
				}
			} else {
				throw new Exception("Life Cycle returned NULL.");
			}			
		
			//Setup Cookie information to be passed to Voltage
			CookieStore cookieStore = httpclient.getCookieStore();
		    BasicClientCookie cookie = new BasicClientCookie(thdSSO.getName(), thdSSO.getValue());
		    mLogger.debug("Server Name:" + request.getServerName());
		    cookie.setDomain(request.getServerName());
		    cookie.setPath("/");
		    cookieStore.addCookie(cookie);
		    httpclient.setCookieStore(cookieStore);			
			
		    //Call Voltage to decrypt SSN			
            httpPost = new HttpPost(ssnUrl);
            params = new ArrayList(1);
            params.add(new BasicNameValuePair("data", StringUtils.trim(dlData.getCandidateId())));
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
			
			// Decrypt Drivers License Number
			decryptedLicNum = EncryptionManager.decryptItem(dlData.getEncryptDriverLicenseNumber());

			// Build the HTML to Pass back to Service
			response.append("<b>Please order background check using information below.</b><br><br>");
			response.append("Candidate ID: ").append(outData.getOutData()).append("<br>");
			response.append("Candidate Name: ").append(StringUtils.trim(dlData.getName())).append("<br>");
			response.append("License Number: ").append(decryptedLicNum).append("<br>");
			response.append("License State: ").append(StringUtils.trim(dlData.getDriverLicenseStateCode())).append("<br>");
			response.append("Expiration Date: ").append(dlData.getDriverLicenseExpirationDate()).append("<br>");
			response.append("Birth Date: ").append(dlData.getBirthDate()).append("<br>");
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred fetching DL Data in getDriversLicenseData()", e);
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getDriversLicenseData() Response: %1$s", response.toString()));
		}
		return response.toString();
	}
	
	/**
	 * This method will be used for fetching the Stores that do not require a Drivers License even though the job requires it.
	 * 
	 * 
	 * @return StoreDriverLicenseExemptResponse
	 * @throws RetailStaffingException
	 */
	public static StoreDriverLicenseExemptResponse getStoresDriversLicenseExempt() throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Entering getStoresDriversLicenseExempt()");
		}

		StoreDriverLicenseExemptResponse res = new StoreDriverLicenseExemptResponse();

		try
		{
			res = DriversLicenseEncryptionDAO.readStoresDriversLicenseExempt();
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred fetching Stores in getStoresDriversLicenseExempt()", e);
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getStoresDriversLicenseExempt() Response: %1$s", res.toString()));
		}
		return res;
	}	
}

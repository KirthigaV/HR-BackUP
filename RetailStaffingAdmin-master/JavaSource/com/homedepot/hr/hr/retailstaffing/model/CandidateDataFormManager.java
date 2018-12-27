package com.homedepot.hr.hr.retailstaffing.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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

import com.homedepot.hr.hr.retailstaffing.dao.CandidateDataFormDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.dao.handlers.CandidateDataFormHandler;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.SsnXrefTO;
import com.homedepot.hr.hr.retailstaffing.dto.StateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitCandidatePersonalDataRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.VoltageServiceResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.CookieUtils;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.XMLHandler;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;
import com.homedepot.ta.aa.util.TAAAResourceManager;

public class CandidateDataFormManager implements RetailStaffingConstants, DAOConstants 
{
	private static final Logger mLogger = Logger.getLogger(CandidateDataFormManager.class);
	
	//The Voltage Dev server does not have an entry in DNS, therefore you have to point to a local Key Store
	private static void setLocalKeyStore() {
		System.setProperty(LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_NAME, LOCAL_TRUST_STORE_VOLTAGE_PROPERTY_VALUE);			
		System.setProperty(LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_NAME, LOCAL_TRUST_STORE_VOLTAGE_PASSWORD_PROPERTY_VALUE); 
		//For localhost SSL 
		System.setProperty(LOCALHOST_TRUST_STORE_PROPERTY_NAME, LOCALHOST_TRUST_STORE_PROPERTY_VALUE);			
		System.setProperty(LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_NAME, LOCALHOST_TRUST_STORE_PASSWORD_PROPERTY_VALUE);
	}
	
	public static List<StateDetailsTO> getStateList() throws Exception
	{
		long startTime = 0;
		List<StateDetailsTO> states = null;
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		 
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug("Entering getStateList()");
		}
		
		try
		{
			// Get States for Drivers License Drop Down
			states = delReqMgr.readStateList();
		} 
		catch(Exception e)
		{
			mLogger.error(e.getMessage(), e);
			throw e;
		} 
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getStateList(), total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return states; 
	} 
	
	public static CandidateDetailsTO getCandidateData(long candidateId) throws Exception
	{
		long startTime = 0;
		CandidateDetailsTO candidateData = null;
		String paddedCandidateId = "";
		 
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getCandidateData() with candidateId:%1$s", candidateId));
		}
		
		try
		{
			//Need to pad the candidateId with leading zeros (10 characters)
			paddedCandidateId = String.format("%010d", candidateId);
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Padded candidateId:%1$s", paddedCandidateId));
			}			
			 
			candidateData = CandidateDataFormDAO.getCandidateDetail(paddedCandidateId);
			if (candidateData.getName() == null) {
				throw new NoRowsFoundException(ApplicationObject.CANDIDATE, String.format("Candidate Reference Number:%1$s not found", candidateId));
			}
		} 
		catch(Exception e)
		{
			mLogger.error(e.getMessage(), e);
			throw e;
		} 
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getCandidateData(), total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return candidateData; 
	} 
	
	public static void submitCandidateSSNDetails(final SubmitCandidatePersonalDataRequest data) throws Exception
	{
		long startTime = 0;
		 
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering submitCandidateSSNDetails() with candidateId:%1$s", data.getCandidateId()));
		}
		
		// create a handler object that will be used to process the results of the query
		CandidateDataFormHandler resultsHandler = new CandidateDataFormHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
			
			// create a UniversalConnectionHandler (so the same connection can be used to execute multiple queries)
			UniversalConnectionHandler handler = new UniversalConnectionHandler(true, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					// get the DAOConnection
					DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					// get a query instance from the connection
					Query query = connection.getQuery();
					// cast the handler the correct type
					CandidateDataFormHandler resultsHandler = (CandidateDataFormHandler)handler;
					//Current Date used in below inputs				
					Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
					
					//See if passed in SSN exists in EAPLCNT_SSN_XREF table for another applicantId.  Cannot have duplicate Active SSN's in DB 
					MapStream inputs = new MapStream(READ_EAPLCNT_SSN_XREF);
					inputs.put(CANDIDATE_SSN_NUMBER, data.getSsnEntry1());
					inputs.put(EFF_BEGIN_DATE, currentDate);
					inputs.put(EFF_END_DATE, currentDate);
					inputs.put(ACTV_FLG, true);
					inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
					inputs.addQualifier("effectiveEndDateGreaterThan", true);
					query.getResult(connection, resultsHandler, inputs);
					
					if (resultsHandler.getmSsnXrefList().size() > 0) {
						mLogger.debug(String.format("Number of instances of SSN:%1$s found:%2$d", data.getSsnEntry1(), resultsHandler.getmSsnXrefList().size()));
						for (int i = 0; i < resultsHandler.getmSsnXrefList().size(); i++) {
							SsnXrefTO temp = (SsnXrefTO) resultsHandler.getmSsnXrefList().get(i);
							if (!temp.getEmploymentApplicantId().equals(data.getApplicantId())) {
								mLogger.debug(String.format("Inactivate SSN:%1$s for applicantId:%2$s  Cannot have Duplicate Active SSN's in DB.", temp.getApplicantSocialSecurityNumberNumber(), temp.getEmploymentApplicantId()));
								//Inactivate record, Duplicate SSN found
								inputs.clear();
								inputs.setSelectorName(UPDATE_EAPLCNT_SSN_XREF);
								inputs.put(EMPLT_APLCNT_ID, temp.getEmploymentApplicantId());
								inputs.put(EFF_BEGIN_DATE, currentDate);
								inputs.put(EFF_END_DATE, currentDate);
								inputs.put(ACTV_FLG, true);
								inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
								inputs.addQualifier("effectiveEndDateGreaterThanEqualTo", true);
								inputs.put(ACTV_FLG_INPUT, false);
								inputs.put(EFF_END_DATE_INPUT, currentDate);
								query.updateObject(connection, resultsHandler, inputs);
							} else {
								//Do nothing, the below process will handle
								mLogger.debug("Do nothing, below process will handle....");	
							} // End else if (!temp.getEmploymentApplicantId().equals(data.getApplicantId())) {
						} // End - for (int i = 0; i < resultsHandler.getmSsnXrefList().size(); i++) {
					} // End - if (resultsHandler.getmSsnXrefList().size() > 0) {
					
					//See if ApplicantId exists in EAPLCNT_SSN_XREF table
					inputs.clear();
					inputs = new MapStream(READ_EAPLCNT_SSN_XREF);
					inputs.put(EMPLT_APLCNT_ID, data.getApplicantId());
					inputs.put(EFF_BEGIN_DATE, currentDate);
					inputs.put(EFF_END_DATE, currentDate);
					inputs.put(ACTV_FLG, true);
					inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
					inputs.addQualifier("effectiveEndDateGreaterThan", true);
					query.getResult(connection, resultsHandler, inputs);
										
					//If applicantId does not exist then insert new record
					if (resultsHandler.getmSsnXref() == null) {
						mLogger.debug(String.format("No SSN found for applicantId:%1$s  Insert New Record", data.getApplicantId()));
						//Insert New Record
						Calendar effEndDate = Calendar.getInstance();
						//Month is zero based Date is 2099-12-31
						effEndDate.set(2099, 11, 31, 00, 00, 00);
						inputs.clear();
						inputs.setSelectorName(INSERT_EAPLCNT_SSN_XREF);
						inputs.put(EMPLT_APLCNT_ID, data.getApplicantId());
						inputs.put(EFF_BEGIN_DATE, currentDate);
						inputs.put(EFF_END_DATE, new Date(effEndDate.getTimeInMillis()));
						inputs.put(CANDIDATE_SSN_NUMBER, data.getSsnEntry1());
						inputs.put(ACTV_FLG, true);
						query.insertObject(connection, resultsHandler, inputs);
					} else {
						//applicantId does exist, compare entered SSN to DB2 SSN
						mLogger.debug(String.format("Found an SSN for applicantId:%1$s  Check to see if equal to the passed in value.", data.getApplicantId()));
						if (data.getSsnEntry1().equals(resultsHandler.getmSsnXref().getApplicantSocialSecurityNumberNumber())) {
							//Do Nothing, there is already a SSN for the Applicant ID
							mLogger.debug(String.format("Entered SSN and DB2 SSN match for applicantId:%1$s  No need to do anything.", data.getApplicantId()));
						} else {
							//The SSN's are not the same so inactivate the current row and add the new value
							mLogger.debug(String.format("Entered SSN and DB2 SSN DO NOT match for applicantId:%1$s  Inactivate current and add new.", data.getApplicantId()));
							
							//Inactivate current record
							inputs.clear();
							inputs.setSelectorName(UPDATE_EAPLCNT_SSN_XREF);
							inputs.put(EMPLT_APLCNT_ID, data.getApplicantId());
							inputs.put(EFF_BEGIN_DATE, currentDate);
							inputs.put(EFF_END_DATE, currentDate);
							inputs.put(ACTV_FLG, true);
							inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
							inputs.addQualifier("effectiveEndDateGreaterThanEqualTo", true);
							inputs.put(ACTV_FLG_INPUT, false);
							inputs.put(EFF_END_DATE_INPUT, currentDate);
							query.updateObject(connection, resultsHandler, inputs);
							
							//Insert New Record
							Calendar effEndDate = Calendar.getInstance();
							//Month is zero based Date is 2099-12-31
							effEndDate.set(2099, 11, 31, 00, 00, 00);
							inputs.clear();
							inputs.setSelectorName(INSERT_EAPLCNT_SSN_XREF);
							inputs.put(EMPLT_APLCNT_ID, data.getApplicantId());
							inputs.put(EFF_BEGIN_DATE, currentDate);
							inputs.put(EFF_END_DATE, new Date(effEndDate.getTimeInMillis()));
							inputs.put(CANDIDATE_SSN_NUMBER, data.getSsnEntry1());
							inputs.put(ACTV_FLG, true);
							query.insertObject(connection, resultsHandler, inputs);							
						} // End - Else if (data.getSsnEntry1().equals(resultsHandler.getmSsnXref().getApplicantSocialSecurityNumberNumber())) { 
					} // End - if (resultsHandler.getmSsnXref() == null) {
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();				
		} 
		catch(Exception e)
		{
			//Insert Failed, unique key constraint.
			if (e.getMessage().contains("SQLCODE=-803")) {
				mLogger.error("An error has occurred updating Candidate. Possible issues, candidate can only change SSN once per day or candidate is trying to change SSN back to a previous value.", e);
				throw new Exception("An error has occurred updating Candidate.  Possible issues, candidate can only change SSN once per day or candidate is trying to change SSN back to a previous value.");
			} else {
				mLogger.error(e.getMessage(), e);
				throw e;
			}
			
		} 
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting submitCandidateSSNDetails(), total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / Constants.NANOS_IN_SECOND)));
		} 
	}	
	
	public static void submitCandidateCPDDetails(final SubmitCandidatePersonalDataRequest data) throws Exception
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering submitCandidateCPDDetails() with candidateId:%1$s", data.getCandidateId()));
		}
		
		try
		{
			//Update CPD Data and Middle Name
			CandidateDataFormDAO.updateCandidateCPDDetailsDAO20(data);
			
			//Add Previous Address if have not lived at current address for required amount of time.
			if (data.getLivedAtCurrentAddressRequiredTime().equals("N")) {
				CandidateDataFormDAO.insertPreviousAddressDataDAO20(data);
			}
		} 
		catch(Exception e)
		{
			mLogger.error(e.getMessage(), e);
			throw e;
		} 
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting submitCandidateCPDDetails(), total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / Constants.NANOS_IN_SECOND)));
		} 
	}	
	
	public static SubmitCandidatePersonalDataRequest encryptDataElements(HttpServletRequest request, SubmitCandidatePersonalDataRequest data) throws Exception
	{
		long startTime = 0;
		SubmitCandidatePersonalDataRequest updatedData = new SubmitCandidatePersonalDataRequest();
		String lifeCycle = null;
		String ssnUrl = null;
		String dlUrl = null;
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    VoltageServiceResponse outData = new VoltageServiceResponse();
	    HttpPost httpPost = null;
	    HttpResponse response = null;
        HttpEntity entity = null;
        List params = new ArrayList(1);
        
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering encryptDataElements()"));
		}
		
		try
		{
			updatedData = data;
			
			//Get the SSO Cookie
			Cookie thdSSO = CookieUtils.getSSOCookie(request);
			
			//Set SSN and DL URL's based on lifecycle
			lifeCycle = TAAAResourceManager.getProperty(LIFE_CYCLE_PROPERTY);
			if (lifeCycle != null) {
				if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_AD)) {
					setLocalKeyStore();
					ssnUrl = SSN_PROTECT_URL_DEV;
					dlUrl = DL_PROTECT_URL_DEV;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_QA)) {
					ssnUrl = SSN_PROTECT_URL_QA;
					dlUrl = DL_PROTECT_URL_QA;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_QP)) {
					ssnUrl = SSN_PROTECT_URL_QP;
					dlUrl = DL_PROTECT_URL_QP;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_ST)) {
					ssnUrl = SSN_PROTECT_URL_ST;
					dlUrl = DL_PROTECT_URL_ST;
				} else if (lifeCycle.equalsIgnoreCase(LIFE_CYCLE_PR)) {
					ssnUrl = SSN_PROTECT_URL_PR;
					dlUrl = DL_PROTECT_URL_PR;
				} else {
					ssnUrl = SSN_PROTECT_URL_PR;
					dlUrl = DL_PROTECT_URL_PR;
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
		    
		    //Call Voltage to get encrypted SSN			
            httpPost = new HttpPost(ssnUrl);
            params = new ArrayList(1);
            params.add(new BasicNameValuePair("data", data.getSsnEntry1()));
            httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            response = httpclient.execute(httpPost);
            entity = response.getEntity();

           if (entity != null) {
        	   mLogger.debug("Response content length: " + entity.getContentLength());
        	   outData = (VoltageServiceResponse) XMLHandler.fromXML(entity.getContent());
        	   //If error details is not null throw and exception based on the response from Voltage Service
        	   if (outData.getErrorDetails() != null) {
        		   throw new Exception("Error Code:" + outData.getErrorDetails().getErrorCode() + " Error Message:" + outData.getErrorDetails().getErrorMessage());
        	   }
        	   updatedData.setSsnEntry1(outData.getOutData());
        	   updatedData.setSsnEntry2(outData.getOutData());
           }
           
           //Call Voltage to get encrypted Driver's License
           if (!data.getDlNumber1().equals("")) {
               httpPost = new HttpPost(dlUrl);
               params = new ArrayList(1);
               params.add(new BasicNameValuePair("data", data.getDlNumber1()));
               httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
               response = httpclient.execute(httpPost);
               entity = response.getEntity();

              if (entity != null) {
           	   mLogger.debug("Response content length: " + entity.getContentLength());
           	   outData = (VoltageServiceResponse) XMLHandler.fromXML(entity.getContent());
           	   //If error details is not null throw and exception based on the response from Voltage Service
           	   if (outData.getErrorDetails() != null) {
           		   throw new Exception("Error Code:" + outData.getErrorDetails().getErrorCode() + " Error Message:" + outData.getErrorDetails().getErrorMessage());
           	   }
           	   updatedData.setDlNumber1(outData.getOutData());
           	   updatedData.setDlNumber2(outData.getOutData());
              }        	   
           }			
		} 
		catch(Exception e)
		{
			mLogger.error(e.getMessage(), e);
			throw e;
		}  finally {
			httpclient.getConnectionManager().shutdown();
		}
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting encryptDataElements(), total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return updatedData; 
	}	
} 
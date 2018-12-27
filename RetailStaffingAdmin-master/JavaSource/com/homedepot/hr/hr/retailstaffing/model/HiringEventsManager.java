package com.homedepot.hr.hr.retailstaffing.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.dao.handlers.HiringEventsHandler;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventMgrTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventsStoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.dto.StateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.HiringEventResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.StateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class HiringEventsManager implements Constants, RetailStaffingConstants, DAOConstants {

	private static final Logger mLogger = Logger.getLogger(HiringEventsManager.class);
	
	private static final short CONTRACT_METHOD_CD_USER_DATA = 1; 
	private static final short CONTRACT_METHOD_CD_EMAIL_DATA = 5;
	private static final short CONTRACT_MECHANISM_ROLE_CD_VALUE = 1;
	private static final short CONTRACT_MECHANISM_PREF_NUMBER_VALUE = 1;

	public static Response loadStateData() throws RetailStaffingException {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering loadStateData()"));
		} // end if

		Response res = null;

		StateDetailResponse stDetailRes = new StateDetailResponse();
		List<StateDetailsTO> states = null;

		RetailStaffingRequisitionDAO myMgr = new RetailStaffingRequisitionDAO();

		try {
			res = new Response();

			// Get States for Drop Down
			states = myMgr.readStateList();
			if (states != null && states.size() > 0) {
				stDetailRes.setStrDtlList(states);
				res.setStateDtRes(stDetailRes);
			}
		}

		catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting loadStateData(). Total time to process %1$.9f seconds", (((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return res;
	}

	/**
	 * Get details about the person whose LDAP or Associate ID was provided
	 * 
	 * @param LDAP Id provided
	 * @param Associate Id provided 
	 * @return HiringEventMgrTO object
	 * 
	 * @throws QueryException if the data for the ID provided is not found in the database (in the form of a NoRowsFoundException),
	 * 						  or if an exception occurs querying the database.
	 */
	public static HiringEventMgrTO getHiringEventMgrData(final String ldapId, final String associateId) throws RetailStaffingException, QueryException {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Manager getHiringEventMgrData() with LDAP ID:%1$s and Associate ID:%2$s", ldapId, associateId));
		} // end if

		HiringEventMgrTO hiringEventMgrTO = null;
		
		try
		{			
			// use the DAO to get a database connection
			DAOConnection connection = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT).getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// create a new handler object that will be used to process the query results
			HiringEventsHandler resultsHandler = new HiringEventsHandler();
			
			// create a universal connection handler that can be used to execute multiple queries with a single connection (non-transacted)
			UniversalConnectionHandler handler = new UniversalConnectionHandler(false, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
                public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
                {
					// get the DAO connection
					DAOConnection connection = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					// get the DAO query object that will be used to execute the query
					Query query = connection.getQuery();
					// prepare the first query
					MapStream inputs = new MapStream(READ_USER_DATA);
					inputs.put(CONTRACT_METHOD_CODE, CONTRACT_METHOD_CD_USER_DATA);
					
					//Determine the input based on if LDAP ID or Associate ID was passed in.
					String typeOfIdUsed = "";
					if (ldapId != null) {
						inputs.put(USER_LDAP_ID, ldapId);
						typeOfIdUsed = "LDAP ID:" + ldapId;
					} else if (associateId != null) {
						inputs.put(HR_ASSOCIATE_ID, associateId);
						typeOfIdUsed = "Associate ID:" + associateId;
					} else {
						throw new ValidationException(InputField.EVENT_MGR, String.format("Hiring Event Mgr data, missing ID"));
					}
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting associate details using %1$s", typeOfIdUsed));
					} // end if
					
					// execute the first query
					query.getResult(connection, handler, inputs);
					
					// if the first query did not return a result, throw a NoRowsFoundException
					if(((HiringEventsHandler)handler).getEventMgrDetails() == null) {
						throw new NoRowsFoundException(ApplicationObject.HIRING_EVENT, String.format("%1$s not found", typeOfIdUsed));
					}
					
					//prepare the second query
					inputs.clear();
					inputs = new MapStream(READ_USER_EMAIL_DATA);
					inputs.put(CONTRACT_MECHANISM_ROLE_CODE, CONTRACT_MECHANISM_ROLE_CD_VALUE);
					inputs.put(CONTRACT_MECHANISM_PREF_NUMBER, CONTRACT_MECHANISM_PREF_NUMBER_VALUE);
					inputs.put(CONTRACT_METHOD_CODE, CONTRACT_METHOD_CD_EMAIL_DATA);	
					if (ldapId != null) {
						inputs.put(USER_LDAP_ID, ldapId);
					} else if (associateId != null) {
						inputs.put(HR_ASSOCIATE_ID, associateId);
					}
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting associate email using %1$s", typeOfIdUsed));
					} // end if
					
					query.getResult(connection, handler, inputs); 
                } // end function handleQuery()				
			}; // end UniversalConnectionHandler
			
			// execute the queries
			handler.execute();
			
			hiringEventMgrTO = (HiringEventMgrTO) resultsHandler.getEventMgrDetails();
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting details for LDAP:%1$s or Associate:%2$s", ldapId, associateId), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting Manager getHiringEventMgrData(), with LDAP ID:%1$s and Associate ID:%2$s. Total time to get details: %3$.9f seconds.",
					ldapId, associateId, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return hiringEventMgrTO;
	}
	
	/**
	 * Get details about a Hiring Event
	 * 
	 * @param hireEventId 
	 * @return HiringEventDetailTO object
	 * 
	 * @throws QueryException if an exception occurs querying the database.
	 */
	public static HiringEventDetailTO getHiringEventDetail(final int hireEventId) throws RetailStaffingException, QueryException {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Manager getHiringEventDetail() with hireEventId:%1$d", hireEventId));
		} // end if

		HiringEventDetailTO hiringEventDetailTO = null;
		
		try
		{			
			// use the DAO to get a database connection
			DAOConnection connection = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT).getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// create a new handler object that will be used to process the query results
			HiringEventsHandler resultsHandler = new HiringEventsHandler();
			
			// create a universal connection handler that can be used to execute multiple queries with a single connection (non-transacted)
			UniversalConnectionHandler handler = new UniversalConnectionHandler(false, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
                public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
                {
					// get the DAO connection
					DAOConnection connection = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					// get the DAO query object that will be used to execute the query
					Query query = connection.getQuery();

					MapStream inputs = new MapStream(READ_HR_HIRE_EVENT);
					inputs.put(HIRE_EVENT_ID, hireEventId);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting Hiring Event details using hireEventId:%1$s", hireEventId));
					} // end if
							
					query.getResult(connection, handler, inputs); 
					
					// if query did not return a result, throw a NoRowsFoundException
					if(((HiringEventsHandler)handler).getEventDetails() == null) {
						throw new NoRowsFoundException(ApplicationObject.HIRING_EVENT, String.format("Hiring Event %1$d not found", hireEventId));
					}
                } // end function handleQuery()				
			}; // end UniversalConnectionHandler
			
			// execute the queries
			handler.execute();
		
			hiringEventDetailTO = (HiringEventDetailTO) resultsHandler.getEventDetails();
		} // end try
		catch(QueryException qe)
		{
			qe.printStackTrace();
			// log the exception
			mLogger.error(String.format("An exception occurred getting Hiring Event details for hireEventId:%1$s", hireEventId), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting Manager getHiringEventDetail(), hireEventId:%1$s. Total time to get details: %2$.9f seconds.",
					hireEventId, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return hiringEventDetailTO;
	}
	
	/**
	 * Get stores participating in a Hiring Event
	 * 
	 * @param hireEventId 
	 * @return StoreDetailsTO object
	 * 
	 * @throws QueryException if an exception occurs querying the database.
	 */
	public static List<HiringEventsStoreDetailsTO> getHiringEventParticipatingStores(final int hireEventId) throws RetailStaffingException, QueryException {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Manager getHiringEventParticipatingStores() with hireEventId:%1$d", hireEventId));
		} // end if

		final List<HiringEventsStoreDetailsTO> participatingStoresList = new ArrayList<HiringEventsStoreDetailsTO>();
		
		try
		{			
			// use the DAO to get a database connection
			DAOConnection connection = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT).getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// create a new handler object that will be used to process the query results
			HiringEventsHandler resultsHandler = new HiringEventsHandler();
			
			// create a universal connection handler that can be used to execute multiple queries with a single connection (non-transacted)
			UniversalConnectionHandler handler = new UniversalConnectionHandler(false, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
                public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
                {
					// get the DAO connection
					DAOConnection connection = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					// get the DAO query object that will be used to execute the query
					Query query = connection.getQuery();

					MapStream inputs = new MapStream(READ_PARTICIPATING_STORE_DATA);
					inputs.put(HIRE_EVENT_ID, hireEventId);
					inputs.put(ACTV_FLG, true);
					inputs.put(STORE_ACTIVE_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting Stores Participating in a Hiring Event using hireEventId:%1$s", hireEventId));
					} // end if
							
					query.getResult(connection, handler, inputs); 
					
                } // end function handleQuery()				
			}; // end UniversalConnectionHandler
			
			// execute the queries
			handler.execute();
		
			participatingStoresList.addAll(resultsHandler.getParticipatingStores());

		} // end try
		catch(QueryException qe)
		{
			qe.printStackTrace();
			// log the exception
			mLogger.error(String.format("An exception occurred getting Participating Stores for Hiring Event hireEventId:%1$s", hireEventId), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting Manager getHiringEventParticipatingStores(), hireEventId:%1$s. Total time to get details: %2$.9f seconds.",
					hireEventId, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return participatingStoresList;
	} 	
		
	/**
	 * This method gets an active hiring event requisition calendar for the calendar id provided
	 * 
	 * @param reqCalId the calendar ID to get requisition calendar for
	 *                      
	 * @return requisition calendars for the Calendar Id
	 * 
	 * @throws QueryException thrown if the calendar Id provided is not valid (in the form of a ValidationException)
	 *                        or if an exception occurs querying the database
	 */	
	public static RequisitionCalendarTO getHiringEventRequisitionCalendarForReqCalId(final int reqCalId) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getHiringEventRequisitionCalendarForReqCalId(), reqCalId: %1$s", reqCalId));
		} // end if
		
		// create a handler object that will be used to process the results of the query
		HiringEventsHandler resultsHandler = new HiringEventsHandler();
		
		try
		{
			// validate the Calendar Id is at least 0
			ValidationUtils.validateRequisitionCalendarId(reqCalId);
			
			// get the query manager for the DAO contract
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// use the query manager to get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			
			// create a UniversalConnectionHandler (so the same connection can be used to execute multiple queries)
			UniversalConnectionHandler handler = new UniversalConnectionHandler(false, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					// get the DAOConnection
					DAOConnection connection = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					// get a query instance from the connection
					Query query = connection.getQuery();
					// cast the handler the correct type
					HiringEventsHandler resultsHandler = (HiringEventsHandler)handler;
										
					// next prepare the query to get the calendars for the store
					MapStream inputs = new MapStream(READ_REQUISITION_CALENDAR_HIRING_EVENT);
					inputs.put(REQN_CAL_ID, reqCalId);
					inputs.put(ACTV_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to get active hiring event requisition calendar for Calendar Id %1$s", reqCalId));
					} // end if
					query.getResult(connection, resultsHandler, inputs);
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();
		} // end try
		catch (NoRowsFoundException ex) {
			//Do nothing, return a null RequisitionCalendarTO
			mLogger.error(String.format("NoRowsFoundException occurred getting calendar for reqCalId %1$s", reqCalId));
			RequisitionCalendarTO reqCalTO = new RequisitionCalendarTO();
			return reqCalTO;
		}
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting calendar for reqCalId %1$s", reqCalId), qe);
			// throw the exception
			throw qe;	
		} // end catch 

		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getHiringEventRequisitionCalendarForReqCalId(), reqCalId: %1$s. Returning requisition calendar in %2$.9f seconds",
				reqCalId, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return resultsHandler.getCalendar();
	} // end function getHiringEventRequisitionCalendarForReqCalId()	
	

} // end of public class HiringEventsManager implements RetailStaffingConstants,
// DAOConstants

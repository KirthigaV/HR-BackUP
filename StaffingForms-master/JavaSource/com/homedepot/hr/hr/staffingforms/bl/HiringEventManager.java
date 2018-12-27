package com.homedepot.hr.hr.staffingforms.bl;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: CalendarManager.java
 * Application: RetailStaffing
 *
 */

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.dao.handlers.HiringEventHandler;
import com.homedepot.hr.hr.staffingforms.dto.AvailabilityBlock;
import com.homedepot.hr.hr.staffingforms.dto.HiringEventModifyAvailability;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.dto.Store;
import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.hr.hr.staffingforms.exceptions.DuplicateHireEventNameException;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.service.request.CreateHiringEventRequest;
import com.homedepot.hr.hr.staffingforms.service.request.UpdateHiringEventRequest;
import com.homedepot.hr.hr.staffingforms.util.Utils;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains methods specific to store calendars
 */
public class HiringEventManager implements Constants, DAOConstants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(HiringEventManager.class);
	
	private static final String HIRE_EVNT_STORE_DEFAULT = "0000";
	private static final short HIRE_EVNT_CALENDAR_TYPE = 100; 
	
	
	/**
	 * This method creates a new requisition hiring event using the information provided
	 * 
	 * @param CreateHiringEventRequest
	 * 
	 * @return id of the newly created requisition hiring event
	 * 
	 * @throws QueryException thrown if the name provided is null or empty, if the store number provided
	 *                        is null, empty, is not a 4 digit value, or does not exist, if the requisition calendar
	 *                        type does not exist, if the type is default or MET and the store already has that type
	 *                        of calendar, or if an exception occurs querying the database
	 *                        
	 */
	public static int createRequisitionHiringEvent(final CreateHiringEventRequest inputData) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering createRequisitionHiringEvent(), createStoreNumber: %1$s, hiringEventName: %2$s", inputData.getHiringEventDetail().getStrNumber(),
										 inputData.getHiringEventDetail().getEventName()));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results of the query
		HiringEventHandler resultsHandler = new HiringEventHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// get a DAO connection
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
					HiringEventHandler resultsHandler = (HiringEventHandler)handler;
					
					//Create Hiring Event HR_HIRE_EVNT
					MapStream inputs = new MapStream(CREATE_HR_HIRE_EVENT);
					inputs.put(HIRE_EVNT_DATE_BEGIN, Utils.convertToSQLDate(inputData.getHiringEventDetail().getEventDt()));	
					inputs.put(HIRE_EVNT_DATE_END, Utils.convertToSQLDate(inputData.getHiringEventDetail().getEventDtEnd()));
					inputs.put(HIRE_EVNT_LOC_DESC, inputData.getHiringEventDetail().getVenueStoreName());
					inputs.put(HIRE_EVNT_PHONE_NBR, inputData.getHiringEventDetail().getEventMgrPhone());
					inputs.put(HIRE_EVNT_ADDRESS, inputData.getHiringEventDetail().getEventAddress1()); 	
					inputs.put(HIRE_EVNT_CITY, inputData.getHiringEventDetail().getEventCity()); 		
					inputs.put(HIRE_EVNT_ZIP, inputData.getHiringEventDetail().getEventZip()); 		
					inputs.put(HIRE_EVNT_STATE, inputData.getHiringEventDetail().getEventState()); 	
					inputs.put(HIRE_EVNT_TYPE, inputData.getHiringEventDetail().getHiringEventType()); 		
					inputs.put(HIRE_EVNT_MGR_ASSOCIATE_ID, inputData.getHiringEventDetail().getEventMgrAssociateId());
					inputs.putAllowNull(HIRE_EVNT_BGN_TIME, null);
					inputs.putAllowNull(HIRE_EVNT_END_TIME, null);
					inputs.putAllowNull(HIRE_EVNT_BREAK_TXT, null);
					inputs.putAllowNull(HIRE_EVNT_LUNCH_BGN_TIME, null);
					inputs.putAllowNull(HIRE_EVNT_INTERVIEW_DUR, null);
					inputs.putAllowNull(HIRE_EVNT_INTERVIEW_TIME_SLOT, null);
					inputs.putAllowNull(HIRE_EVNT_LAST_INTERVIEW_TIME, null);
					inputs.addQualifier(RETURN_GENERATED_KEY, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Creating record in HR_HIRE_EVNT"));
					} // end if
					// execute the query
					query.insertObject(connection, resultsHandler, inputs);	
					
					// setup the query for HR_REQN_CAL
					inputs.clear();
					inputs.setSelectorName(CREATE_HIRING_EVENT_HR_REQN_CAL);
					//Hire Event Description is made up of the Creating Store Number and Hiring Event Name.  Delimited with ~|~
					StringBuilder storeAndName = new StringBuilder(50);
					storeAndName.append(inputData.getHiringEventDetail().getStrNumber()).append("~|~").append(inputData.getHiringEventDetail().getEventName());
					inputs.put(REQN_CAL_DESC, storeAndName.toString());
					inputs.put(HR_SYS_STR_NBR, HIRE_EVNT_STORE_DEFAULT );
					inputs.put(HR_CAL_TYP_CD, HIRE_EVNT_CALENDAR_TYPE);
					inputs.put(ACTV_FLG, true);
					inputs.put(HIRE_EVNT_ID, resultsHandler.getHiringEventId());
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Creating record in HR_REQN_CAL"));
					} // end if
					// execute the query
					query.insertObject(connection, resultsHandler, inputs);					
					
					//Setup the query for adding Participating Stores.  HR_HIRE_EVNT_STR
					inputs.clear();
					inputs.setSelectorName(CREATE_HR_HIRE_EVNT_STR);
					for (int stores = 0; stores < inputData.getStoreNumbers().size(); stores++) {
						Store eachStr = inputData.getStoreNumbers().get(stores);
						inputs.put(HIRE_EVNT_ID, resultsHandler.getHiringEventId());
						inputs.put(HR_SYS_STR_NBR, eachStr.getNumber());
						inputs.put(ACTV_FLG, true);
						if (eachStr.getEventSiteFlg().equals("Y")) {
							inputs.put(HIRE_EVNT_SITE_FLG,true);
						} else {
							inputs.put(HIRE_EVNT_SITE_FLG,false);
						}
						
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Creating record in HR_HIRE_EVNT_STR"));
						} // end if
						// execute the query
						query.insertObject(connection, resultsHandler, inputs);						
					}
							
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					

		} // end Try 
			
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred creating Hiring Event: %1$d", inputData.getHiringEventDetail().getEventName()));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting createRequisitionHiringEvent(),  Successfully created Hiring Event reqnCalId %1$d in %2$.9f seconds", 
					resultsHandler.getCalendarId(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return resultsHandler.getCalendarId();

	}
	
	/**
	 * This method checks to see if a hiring event exists with the same name provided
	 * 
	 * @param hiringEventName
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 * @throws DuplicateHireEventNameException thrown if a duplicate hiring event name is found
	 *                        
	 */
	public static void checkDuplicateHiringEventName(final String hiringEventName, final int hireEventId) throws QueryException, DuplicateHireEventNameException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering Manager checkDuplicateHiringEventName(), hiringEventName: %1$s", hiringEventName));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results of the query
		HiringEventHandler resultsHandler = new HiringEventHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// get a DAO connection
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
					HiringEventHandler resultsHandler = (HiringEventHandler)handler;
					
					//Read HR_REQN_CAL table for all Hiring Events of type 100
					MapStream inputs = new MapStream(READ_HIRING_EVENT_CALENDARS);
					inputs.put(HR_CAL_TYP_CD, HIRE_EVNT_CALENDAR_TYPE);
					inputs.put(ACTV_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Reading HR_REQN_CAL Table"));
					} // end if
					// execute the query
					query.getResult(connection, resultsHandler, inputs);					
					
					//Loop through the list of Hiring Event calendars to check for duplicate name
					List<RequisitionCalendar> theList = resultsHandler.getHringEventCalendars();
					for (int i=0;i<theList.size();i++) {
						RequisitionCalendar requisitionCalendar = theList.get(i);
						//The Hiring Event description is used to hold the store created value and the description, delimited by ~|~
						//the description is in index 1
						String[] desc = null;
						String compareDesc = "";
						if (requisitionCalendar.getDesc().contains("~|~")) {
							desc = requisitionCalendar.getDesc().split("~\\|~");
							compareDesc = desc[1].toUpperCase();
						} else {
							compareDesc = requisitionCalendar.getDesc().toUpperCase();
						}
						
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Passed in hireEventId:%1$d, DB hireEventId:%2$d", hireEventId, requisitionCalendar.getHireEventId()));
						}
						
						//This is used for the Create and Update of a Hiring Event.  If the names are the same and the hireEventId's are the same then it is ok
						// to have a duplicate name.  Don't throw an exception.
						if (hiringEventName.toUpperCase().equals(compareDesc) && requisitionCalendar.getHireEventId() != hireEventId) {
							throw new DuplicateHireEventNameException(InputField.DUPLICATE_HIRE_EVENT_NAME, String.format("%1$s", InputField.DUPLICATE_HIRE_EVENT_NAME.getFieldLabel()));
							
						}
					} // end for (int i=0;i<theList.size();i++)
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					
		} // end Try 
			
		catch(QueryException qe)
		{
			qe.printStackTrace();
			// log the exception
			mLogger.error(String.format("An exception occurred getting hiring event calendars"));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting checkDuplicateHiringEventName(),  Successfully checked Hiring Event name %1$s in %2$.9f seconds", 
					hiringEventName, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end checkDuplicateHiringEventName()
	
	/**
	 * This method gets the hireEventId based on the passed in reqnCalId
	 * 
	 * @param reqnCalId
	 * 
	 * @return hireEventId
	 * 
	 * @throws QueryException if an exception occurs querying the database
	 *                        
	 */
	public static int getHireEventId(final int reqnCalId) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getHireEventId(), reqnCalId: %1$s", reqnCalId));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results of the query
		HiringEventHandler resultsHandler = new HiringEventHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// get a DAO connection
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
					HiringEventHandler resultsHandler = (HiringEventHandler)handler;
					
					//Read HR_REQN_CAL table
					MapStream inputs = new MapStream(READ_HIRING_EVENT_CALENDARS);
					inputs.put(REQN_CAL_ID, reqnCalId);	

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting hireEventId"));
					} // end if
					// execute the query
					query.getResult(connection, resultsHandler, inputs);	
							
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					

		} // end Try 
			
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred hireEventId"));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting getHireEventId(),  Successfully fetched hireEventId:%1$d in %2$.9f seconds", 
					resultsHandler.getHiringEventId(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return resultsHandler.getHiringEventId();

	} // end getHireEventId(final int reqnCalId)
	
	/**
	 * This method deletes Hiring Event Participating Store
	 * 
	 * @param storeNum, hireEventId
	 * 
	 * @return void
	 * 
	 * @throws QueryException if an exception occurs querying the database
	 *                        
	 */
	public static void deleteHiringEventParticipatingStore(final String storeNum, final int hireEventId) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering deleteHiringEventParticipatingStore(), storeNum: %1$s, hireEventId:%2$s", storeNum, hireEventId));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results of the query
		HiringEventHandler resultsHandler = new HiringEventHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// get a DAO connection
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
					HiringEventHandler resultsHandler = (HiringEventHandler)handler;
					
					//Delete Store from HR_HIRE_EVNT_STR table
					MapStream inputs = new MapStream(DELETE_HR_HIRE_EVNT_STR);
					inputs.put(HIRE_EVNT_ID, hireEventId);
					inputs.put(HR_SYS_STR_NBR, storeNum);

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Delete Participating Store"));
					} // end if
					// execute the query
					query.deleteObject(connection, resultsHandler, inputs);	
							
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					

		} // end Try 
			
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred deleting Participating Store"));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting deleteHiringEventParticipatingStore() successfully in %1$.9f seconds", 
					(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
	} // end deleteHiringEventParticipatingStore(final String storeNum, final int hireEventId)	
	
	/**
	 * This method adds Hiring Event Participating Store
	 * 
	 * @param storeNum, hireEventId
	 * 
	 * @return void
	 * 
	 * @throws QueryException if an exception occurs querying the database
	 *                        
	 */
	public static void addHiringEventParticipatingStore(final String storeNum, final int hireEventId, final boolean eventSiteFlg) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering addHiringEventParticipatingStore(), storeNum: %1$s, hireEventId:%2$s, eventSiteFlg:%3$b", storeNum, hireEventId, eventSiteFlg));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results of the query
		HiringEventHandler resultsHandler = new HiringEventHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// get a DAO connection
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
					HiringEventHandler resultsHandler = (HiringEventHandler)handler;
					
					//Add Store to HR_HIRE_EVNT_STR table
					MapStream inputs = new MapStream(CREATE_HR_HIRE_EVNT_STR);
					inputs.put(HIRE_EVNT_ID, hireEventId);
					inputs.put(HR_SYS_STR_NBR, storeNum);
					inputs.put(ACTV_FLG, true);
					inputs.put(HIRE_EVNT_SITE_FLG, eventSiteFlg);

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Add Participating Store"));
					} // end if
					// execute the query
					query.insertObject(connection, resultsHandler, inputs);	
							
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					

		} // end Try 
			
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred deleting Participating Store"));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting deleteHiringEventParticipatingStore() successfully in %1$.9f seconds", 
					(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
	} // end deleteHiringEventParticipatingStore(final String storeNum, final int hireEventId)	
	
	/**
	 * This method updates a hiring event using the information provided
	 * 
	 * @param UpdateHiringEventRequest
	 * 
	 * @return nothing
	 * 
	 * @throws QueryException if an exception occurs updating the database
	 *                        
	 */
	public static void updateRequisitionHiringEvent(final UpdateHiringEventRequest inputData) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updateRequisitionHiringEvent(), hireEventId: %1$d, hiringEventName: %2$s", inputData.getHiringEventDetail().getHireEventId(),
										 inputData.getHiringEventDetail().getEventName()));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results of the query
		HiringEventHandler resultsHandler = new HiringEventHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// get a DAO connection
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
					HiringEventHandler resultsHandler = (HiringEventHandler)handler;
					
					//Update Hiring Event HR_HIRE_EVNT
					MapStream inputs = new MapStream(UPDATE_HR_HIRE_EVENT);
					inputs.put(HIRE_EVNT_ID, inputData.getHiringEventDetail().getHireEventId());
					inputs.put(HIRE_EVNT_DATE_BEGIN, Utils.convertToSQLDate(inputData.getHiringEventDetail().getEventDt()));	
					inputs.put(HIRE_EVNT_DATE_END, Utils.convertToSQLDate(inputData.getHiringEventDetail().getEventDtEnd()));
					inputs.put(HIRE_EVNT_LOC_DESC, inputData.getHiringEventDetail().getVenueStoreName());
					inputs.put(HIRE_EVNT_PHONE_NBR, inputData.getHiringEventDetail().getEventMgrPhone());
					inputs.put(HIRE_EVNT_ADDRESS, inputData.getHiringEventDetail().getEventAddress1()); 	
					inputs.put(HIRE_EVNT_CITY, inputData.getHiringEventDetail().getEventCity()); 		
					inputs.put(HIRE_EVNT_ZIP, inputData.getHiringEventDetail().getEventZip()); 		
					inputs.put(HIRE_EVNT_STATE, inputData.getHiringEventDetail().getEventState()); 	
					inputs.put(HIRE_EVNT_TYPE, inputData.getHiringEventDetail().getHiringEventType()); 		
					inputs.put(HIRE_EVNT_MGR_ASSOCIATE_ID, inputData.getHiringEventDetail().getEventMgrAssociateId());
					inputs.putAllowNull(HIRE_EVNT_BGN_TIME, null);
					inputs.putAllowNull(HIRE_EVNT_END_TIME, null);
					//This gets set if Interviews have been scheduled and the location changes.  This field is used by the Cobol email program to trigger a location change email
					if (inputData.getHiringEventDetail().isLocationChange()) {
						inputs.putAllowNull(HIRE_EVNT_BREAK_TXT, "Y|");
					} else {
						inputs.putAllowNull(HIRE_EVNT_BREAK_TXT, null);
					}
					
					inputs.putAllowNull(HIRE_EVNT_LUNCH_BGN_TIME, null);
					inputs.putAllowNull(HIRE_EVNT_INTERVIEW_DUR, null);
					inputs.putAllowNull(HIRE_EVNT_INTERVIEW_TIME_SLOT, null);
					inputs.putAllowNull(HIRE_EVNT_LAST_INTERVIEW_TIME, null);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Updating record in HR_HIRE_EVNT for hireEventId:%1$d", inputData.getHiringEventDetail().getHireEventId()));
					} // end if
					// execute the query
					query.updateObject(connection, resultsHandler, inputs);	
					
					
					// setup the query to update HR_REQN_CAL
					inputs.clear();
					inputs.setSelectorName(UPDATE_HIRING_EVENT_HR_REQN_CAL);
					//Hire Event Description is made up of the Creating Store Number and Hiring Event Name.  Delimited with ~|~
					StringBuilder storeAndName = new StringBuilder(50);
					storeAndName.append(inputData.getHiringEventDetail().getHireEventCreatedByStore()).append("~|~").append(inputData.getHiringEventDetail().getEventName());
					inputs.put(REQN_CAL_ID, inputData.getHiringEventDetail().getReqnCalId());
					inputs.put(REQN_CAL_DESC, storeAndName.toString());
					inputs.put(HR_SYS_STR_NBR, HIRE_EVNT_STORE_DEFAULT );
					inputs.put(HR_CAL_TYP_CD, HIRE_EVNT_CALENDAR_TYPE);
					inputs.put(ACTV_FLG, true);
					inputs.put(HIRE_EVNT_ID, inputData.getHiringEventDetail().getHireEventId());
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Updating record in HR_REQN_CAL for reqnCalId:%1$d", inputData.getHiringEventDetail().getReqnCalId()));
					} // end if
					// execute the query
					query.updateObject(connection, resultsHandler, inputs);					
					
					//Setup the queries for insert or update Participating Stores.  HR_HIRE_EVNT_STR
					for (int stores = 0; stores < inputData.getStoreNumbers().size(); stores++) {
						Store eachStr = inputData.getStoreNumbers().get(stores);
						inputs.clear();
						if (eachStr.getUpdateInsertRecord().equals("INSERT")) {
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Insert record in HR_HIRE_EVNT_STR for Store:%1$s",eachStr.getNumber()));
							}
							inputs.setSelectorName(CREATE_HR_HIRE_EVNT_STR);
							inputs.put(HIRE_EVNT_ID, eachStr.getHireEventId());
							inputs.put(HR_SYS_STR_NBR, eachStr.getNumber());
							inputs.put(ACTV_FLG, true);
							if (eachStr.getEventSiteFlg().equals("Y")) {
								inputs.put(HIRE_EVNT_SITE_FLG,true);
							} else {
								inputs.put(HIRE_EVNT_SITE_FLG,false);
							}
							// execute the query
							query.insertObject(connection, resultsHandler, inputs);	
						} else if (eachStr.getUpdateInsertRecord().equals("UPDATE")) {
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Update record in HR_HIRE_EVNT_STR for Store :%1$s",eachStr.getNumber()));
							}
							inputs.setSelectorName(UPDATE_HR_HIRE_EVNT_STR);
							inputs.put(HIRE_EVNT_ID, eachStr.getHireEventId());
							inputs.put(HR_SYS_STR_NBR, eachStr.getNumber());
							inputs.put(ACTV_FLG, true);
							if (eachStr.getEventSiteFlg().equals("Y")) {
								inputs.put(HIRE_EVNT_SITE_FLG,true);
							} else {
								inputs.put(HIRE_EVNT_SITE_FLG,false);
							}
							// execute the query
							query.updateObject(connection, resultsHandler, inputs);							
						} //end if (eachStr.getUpdateInsertRecord().equals("INSERT")) else if (eachStr.getUpdateInsertRecord().equals("UPDATE")) { 
					} // end for (int stores = 0; stores < inputData.getStoreNumbers().size(); stores++) {
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					

		} // end Try 
			
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred creating Hiring Event: %1$d", inputData.getHiringEventDetail().getEventName()));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting updateRequisitionHiringEvent(),  Successfully updated Hiring Event in %1$.9f seconds", 
					(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end updateRequisitionHiringEvent(final UpdateHiringEventRequest inputData)
	
	public static void moveAndOrDeleteHiringEventAvailability(final UpdateHiringEventRequest request) throws ValidationException, QueryException, Exception
	{
		long startTime = 0;

		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering moveAndOrDeleteHiringEventAvailability(), hireEventId: %1$d, hiringEventName: %2$s", request.getHiringEventDetail().getHireEventId(),
										 request.getHiringEventDetail().getEventName()));
		} // end if(mLogger.isDebugEnabled())
		
		try
		{
			//Loop through each Modification and perform the Move/Delete or Delete based on the request data
			for (int i = 0; i < request.getAvailabilityModifications().size(); i++) {
				//Get the Modification Record
				HiringEventModifyAvailability hiringEventModifyAvailability = request.getAvailabilityModifications().get(i);
				
				//Read the Calendar Availability based on the calendarId and the Date
				List<RequisitionSchedule> requisitionScheduleList = CalendarManager.getCalendarDetailsForDate(hiringEventModifyAvailability.getReqnCalId(), hiringEventModifyAvailability.getTheDate());
				
				if (mLogger.isDebugEnabled()) {
					mLogger.debug("Availability Slots Returned:" + requisitionScheduleList.size());
					mLogger.debug("Delete Or Move:" + hiringEventModifyAvailability.getMoveDeleteAvailability());
				}
				
				if (hiringEventModifyAvailability.getMoveDeleteAvailability().equals("DELETE")) {
					//Call the Delete Method with the results from the read to delete the Availability
					if (requisitionScheduleList.size() > 0) {
						int deletedSlots = CalendarManager.removeAvailability(requisitionScheduleList);
						mLogger.debug("Slots Deleted:" + deletedSlots);						
					} else {
						mLogger.debug("No Availability to Delete");
					}

				} else if (hiringEventModifyAvailability.getMoveDeleteAvailability().equals("MOVE")) {
					//This will move Availability from one single Date to Another Date
					if (requisitionScheduleList.size() > 0) {
						//Add a dummy Record to the end of requisitionSchedule, reason for this we have to count the number of interviews per time slot and record the 
						//Availability data in a AvailabilityBlock object.  If there are multiple interviews in a time slot we cannot go by the sequence numbers because
						//slots may have been deleted from the middle of the sequence.  When the Dummy record has been reached it shows that all the time slots have been read
						//and it's value is not added to the AvailabilityBlock Object.
						RequisitionSchedule dummyRecord = new RequisitionSchedule();
						//Seed the timestamp with a value so that it is valid in the object
						dummyRecord.setBgnTs(new Timestamp(1));
						requisitionScheduleList.add(dummyRecord);
					
						//These timestamps are used to walk through the array.
						Timestamp currentTimeStamp;
						Timestamp previousTimeStamp = null;
						
						//Modify the Begin Timestamp with the new Event Date
						
						mLogger.debug("New Event Date:" + request.getHiringEventDetail().getEventDt());
					
						AvailabilityBlock availabilityBlock = null;
						List<AvailabilityBlock> availabilityList = new ArrayList<AvailabilityBlock>();
						
						int numInterviewers = 0;
						//Loop through all the records and add them to the availabilityList, only single time slots will be added even though there 
						//could be multiple slots in the requisitionScheduleList, this is where the numInterviewers variable comes in.
						for (int k = 0; k < requisitionScheduleList.size(); k++) {
							RequisitionSchedule timeSlot = requisitionScheduleList.get(k);
							//Set currentTimeStamp to the value of the Begin Timestamp of the timeSlot record
							currentTimeStamp = timeSlot.getBgnTs();
						
							//When the currentTimeStamp and previousTimeStamp are not equal, add the time slot of the previous record to the 
							//availabilityList.  Reason is we have already ready read the next time slot and counted the numInterviewers. 
							if (previousTimeStamp != null && currentTimeStamp.getTime() != previousTimeStamp.getTime()) {
								availabilityBlock = new AvailabilityBlock();
								if (mLogger.isDebugEnabled()) {
									mLogger.debug("Time Slot:" + requisitionScheduleList.get(k-1).getBgnTs() + " Number of Interviews for Last Slot:" + numInterviewers);
								}
													
								//Have to change the Date in the Hiring Event from the original Date to the new Hire Event Date selected when adding to the availabilityBlock
								Timestamp newEventDate = new Timestamp(1);
								//Move the date forward or backward based on the passed in value from the UI getDaysBetweenOriginalStartDateAndNewStartDate
								newEventDate.setTime(requisitionScheduleList.get(k-1).getBgnTs().getTime() + 
										(hiringEventModifyAvailability.getDaysBetweenOriginalStartDateAndNewStartDate() * MILLISECONDS_PER_DAY));
								//Start setting the values in the availabilityBlock
								availabilityBlock.setBgnTs(newEventDate);
								//Have to set the end timestamp 30 minutes ahead of the begin timestamp  
								Timestamp endTimeStamp = new Timestamp(1); 
								endTimeStamp.setTime(newEventDate.getTime() + (MILLISECONDS_PER_MINUTE * 30));
								availabilityBlock.setEndTs(endTimeStamp);
								availabilityBlock.setCalendarId(requisitionScheduleList.get(k-1).getCalendarId());
								availabilityBlock.setStoreNbr(requisitionScheduleList.get(k-1).getStrNbr());
								availabilityBlock.setWeeksRecurring((short) 1);
								availabilityBlock.setNumIntrvwrs((short) numInterviewers);

								//Reset numInterviews to 1 because the first record has already been read
								numInterviewers = 1;
								//Add availabilityBlock to the list
								availabilityList.add(availabilityBlock);
							} else {
								//currentTimeStamp and previousTimeStamp are equal, same timeslot, increment the numInterviewers
								numInterviewers++;
							} // end if (previousTimeStamp != null && currentTimeStamp.getTime() != previousTimeStamp.getTime()) else
							
							//Set previousTimestamp to the currentTimestamp before reading the next record
							previousTimeStamp = currentTimeStamp;
						} // end (int k = 0; k < requisitionScheduleList.size(); k++)

						//Remove Dummy Record, no longer needed 
						requisitionScheduleList.remove(requisitionScheduleList.size()-1);
					
						//Call the Add method with the results from the above read
						int slotsAdded = CalendarManager.addAvailability(availabilityList);
					
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Number of Slots Added:%1$d", slotsAdded));
						}

						//Delete Old Event Date Availability
						int deletedSlots = CalendarManager.removeAvailability(requisitionScheduleList);
						
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Number of Slots Deleted:%1$d", deletedSlots));
						}
						
					} else {
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("No Availability to Move or Delete."));
						}
					}
				}
			}
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("A query exception occurred Moving/Deleting Hiring Event time slots for hireEventId: %1$d", request.getHiringEventDetail().getHireEventId()));
			// throw the exception
			throw qe;
		} // end catch
		catch(Exception e)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred Moving/Deleting Hiring Event time slots for hireEventId: %1$d", request.getHiringEventDetail().getHireEventId()));
			// throw the exception
			throw e;
		} // end catch	
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting moveAndOrDeleteHiringEventAvailability() in %1$.9f seconds", 
					(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if		
	} // end moveAndOrDeleteHiringEventAvailability()
	
	/**
	 * This method updates a phone screen when a hiring event location changes using the information provided
	 * 
	 * @param UpdateHiringEventRequest
	 * @param List<RequisitionSchedule> Phone Screens
	 * 
	 * @return nothing
	 * 
	 * @throws QueryException if an exception occurs updating the database
	 *                        
	 */
	public static void updatePhoneScreenHiringEventLocation(final UpdateHiringEventRequest inputData, final List<RequisitionSchedule> phoneScreenList) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updatePhoneScreenHiringEventLocation(), reqnCalId: %1$d", inputData.getHiringEventDetail().getReqnCalId()));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results of the query
		HiringEventHandler resultsHandler = new HiringEventHandler();
		
		try
		{
			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
			
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
					DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					// get a query instance from the connection
					Query query = connection.getQuery();
					// cast the handler the correct type
					HiringEventHandler resultsHandler = (HiringEventHandler)handler;
					
					//Get Interview data from the Phone Screen record that we don't have in the inputData
					MapStream inputs = new MapStream(READ_INTVW_DETAILS_FROM_PHN_SCRN);
					
					//Setup the queries for read and update of interview data in HR_PHN_SCRN
					for (int phoneScrn = 0; phoneScrn < phoneScreenList.size(); phoneScrn++) {
						
						//setup the read query
						RequisitionSchedule phoneScrnData = phoneScreenList.get(phoneScrn);
						inputs.clear();
						inputs = new MapStream(READ_INTVW_DETAILS_FROM_PHN_SCRN);
						inputs.put(HR_PHN_SCRN_ID, phoneScrnData.getPhnScrnId());
						
						// execute the query
						query.getResult(connection, resultsHandler, inputs);	
						
						//setup the update query
						inputs.clear();
						inputs = new MapStream(UPDATE_INTVW_DETAILS_IN_PHN_SCRN);
						inputs.put(INTVW_LOC_TYPE_CODE, resultsHandler.getPhoneScreenIntvwDetails().getInterviewLocationTypeCode());
						if (resultsHandler.getPhoneScreenIntvwDetails().getInterviewerName() != null) {
							inputs.put(INTERVIEWER_NAME, resultsHandler.getPhoneScreenIntvwDetails().getInterviewerName());
						} else {
							inputs.putAllowNull(INTERVIEWER_NAME, null);
						}
						if (resultsHandler.getPhoneScreenIntvwDetails().getInterviewDate() != null) {
							inputs.put(INTVW_DATE, resultsHandler.getPhoneScreenIntvwDetails().getInterviewDate());
						} else {
							inputs.putAllowNull(INTVW_DATE, null);
						}
						if (resultsHandler.getPhoneScreenIntvwDetails().getInterviewTimestamp() != null) {
							inputs.put(INTVW_TIMESTAMP, resultsHandler.getPhoneScreenIntvwDetails().getInterviewTimestamp());
						} else {
							inputs.putAllowNull(INTVW_TIMESTAMP, null);
						}
						//The rest should have a value coming from the form
						inputs.put(INTVW_LOC_DESC, inputData.getHiringEventDetail().getVenueStoreName());
						inputs.put(INTVW_PHN_NUM, inputData.getHiringEventDetail().getEventMgrPhone());
						inputs.put(INTVW_ADDRESS_TEXT, inputData.getHiringEventDetail().getEventAddress1());
						inputs.put(INTVW_CITY_NAME, inputData.getHiringEventDetail().getEventCity());
						inputs.put(INTVW_STATE_CODE, inputData.getHiringEventDetail().getEventState());
						inputs.put(INTVW_ZIP_CODE, inputData.getHiringEventDetail().getEventZip());						
						inputs.put(HR_PHN_SCRN_ID, phoneScrnData.getPhnScrnId());						
						
						// execute the query
						query.updateObject(connection, resultsHandler, inputs);						
 
					} // end for (int phoneScrn = 0; phoneScrn < phoneScreenList.size(); phoneScrn++) {
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					

		} // end Try 
			
		catch(QueryException qe)
		{
			// log the exception
			qe.printStackTrace();
			mLogger.error(String.format("An exception occurred updating Hiring Event phoneScreen:"));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0) { 
				startTime = endTime; 
			}
			mLogger.debug(String.format("Exiting updatePhoneScreenHiringEventLocation(),  Successfully updated Hiring Event Phone Screens in %1$.9f seconds", 
					(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end updatePhoneScreenHiringEventLocation(final UpdateHiringEventRequest inputData, final List<RequisitionSchedule> phoneScreenList)	
} // end class HiringEventManager
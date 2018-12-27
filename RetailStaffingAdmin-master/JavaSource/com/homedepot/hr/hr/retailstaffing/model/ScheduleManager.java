package com.homedepot.hr.hr.retailstaffing.model;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ScheduleManager.java
 * Application: ScheduleManager
 *
 */

import java.sql.Time;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.ScheduleDAO;
import com.homedepot.hr.hr.retailstaffing.dao.PhoneScreenDAO;
import com.homedepot.hr.hr.retailstaffing.dao.handlers.ScheduleHandler;
import com.homedepot.hr.hr.retailstaffing.dao.interfaces.TimePreference;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventsStoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDate;
import com.homedepot.hr.hr.retailstaffing.dto.InterviewAvaliableSlotsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwScheduleDetails;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.OrgParamTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.interfaces.ValidationConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.exceptions.UpdateException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class ScheduleManager implements Constants, DAOConstants, ValidationConstants, RetailStaffingConstants 
{	
	// time format string
	private static final String TIME_FORMAT_STRING = "HH:mm:ss";
	// hr_org_parm.sub_sys_cd value for interview schedule cutoff entries
	private static final String HR_SUBSYS_CD = "HR";
	// hr_org_parm.bu_id value for interview schedule cutoff entries
	private static final String BU = "1";
	// hr_org_parm.prcss_typ_ind for interview schedule cutoff entries
	private static final String SVC_TYP_IND = "S";
	// org parameter names
	private static final String ORG_PARM_INTSCH_CUTOFF = "intsch.cutoff.time";
	private static final String ORG_PARM_INTSCH_PRECUTOFF_OFFSET = "intsch.precutoff.off";
	private static final String ORG_PARM_INTSCH_POSTCUTOFF_OFFSET = "intsch.postcutoff.off";
	// scheduling parameters
	private static final int ADDL_DURATION_MINS = 30;
	private static final int MAX_INTRVW_SLOTS = 500;
	// default calendar name format string
	private static final String DFLT_CAL_DESC_FORMAT_STRING = "%1$s - %2$s";
	
	private static final short HIRING_EVENT_TYPE_CD = 100;
	private static final short SLOT_AVAIL_CD = 1;
	private static final short SLOT_SCHD_CD = 3;
	
	private static final Logger mLogger = Logger.getLogger(ScheduleManager.class);
	
	/**
	 * This method gets a list of active requisition calendars (HR_REQN_SCH) for the store number provided
	 * 
	 * @param storeNbr the store number to get requisition calendars for
	 * @param createDefault true if a default calendar should be created if no calendars are returned for the
	 *                      store number provided, false otherwise
	 *                      
	 * @return              list of requisition calendars for the store number
	 * 
	 * @throws QueryException thrown if the store number provided is not valid (in the form of a ValidationException)
	 *                        or if an exception occurs querying the database
	 */
	public static List<RequisitionCalendarTO> getRequisitionCalendarsForStore(final String storeNbr, final boolean createDefault) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getRequistionCalendarsForStore(), storeNumber: %1$s, createDefault: %2$b", storeNbr, createDefault));
		} // end if
		
		// create a handler object that will be used to process the results of the query
		ScheduleHandler resultsHandler = new ScheduleHandler();
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateStoreNumber(storeNbr);
			
			// get the query manager for the DAO contract
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// use the query manager to get a DAO connection
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
					ScheduleHandler resultsHandler = (ScheduleHandler)handler;
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to validate store number %1$s", storeNbr));
					} // end if					
					// first validate the location number provided is valid
					if(!LocationManager.isValidLocationNumber(storeNbr))
					{
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNbr));
					} // end if(!LocationManager.isValidStoreNumber(storeNbr))
					
					// next prepare the query to get the calendars for the store
					MapStream inputs = new MapStream(READ_REQUISITION_CALENDAR);
					inputs.put(HR_SYS_STR_NBR, storeNbr);
					inputs.put(ACTV_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to get active requisition calendars for store %1$s", storeNbr));
					} // end if
					query.getResult(connection, resultsHandler, inputs);
					
					// if no calendars were found (and createDefault is true)
					if((resultsHandler.getCalendars() == null || resultsHandler.getCalendars().size() == 0) && createDefault)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("No requisition calendars found for store %1$s, creating default", storeNbr));
						} // end if
						
						// get the store details
						StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(storeNbr);
						/*
						 * create a default requisition calendar for the location. The description of this default calendar
						 * should be store number - store name. No null check here, if the store is not found the LocationManager
						 * will throw a NoRowsFoundException
						 */				
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Creating default calendar for store %1$s", storeNbr));
						} // end if			
						ScheduleManager.createRequisitionCalendar(String.format(DFLT_CAL_DESC_FORMAT_STRING, storeNbr, store.getStrName().trim()), storeNbr, REQN_CAL_TYP_DEFAULT);
						
						// get the default calendar we just created
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Re-querying to get requisition calendars for store %1$s", storeNbr));
						} // end if
						
						query.getResult(connection, handler, inputs);
					} // end if((resultsHandler.getCalendars() == null || resultsHandler.getCalendars().size() == 0) && createDefault)
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting calendars for store %1$s", storeNbr), qe);
			// throw the exception
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getRequisitionCalendarsForStore(), storeNumber: %1$s, createDefault: %2$b. Returning %3$d requisition calendars in %4$.9f seconds",
				storeNbr, createDefault, (resultsHandler.getCalendars() == null ? 0 : resultsHandler.getCalendars().size()), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return resultsHandler.getCalendars();
	} // end function getRequisitionCalendarsForStore()
	
	/**
	 * This method gets a list of active requisition calendars (HR_REQN_SCH), including counts (active requisitions
	 * and future dated interviews) for the store number provided
	 * 
	 * @param storeNbr the store number to get requisition calendars for
	 *                      
	 * @return              list of requisition calendars (including counts) for the store
	 * 
	 * @throws QueryException thrown if the store number provided is not valid (in the form of a ValidationException)
	 *                        or if an exception occurs querying the database
	 */
	public static List<RequisitionCalendarTO> getRequisitionCalendarsAndCountsForStore(final String storeNbr) throws QueryException
	{
		long startTime = 0;				
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getRequisitionCalendarsAndCountsForStore(), storeNumber: %1$s", storeNbr));
		} // end if

		// create a handler object that will be used to process the results of the query
		ScheduleHandler resultsHandler = new ScheduleHandler();
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateStoreNumber(storeNbr);
			
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
					ScheduleHandler resultsHandler = (ScheduleHandler)handler;
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to validate store number %1$s", storeNbr));
					} // end if			
					// first validate the location number provided is valid
					if(!LocationManager.isValidStoreNumber(storeNbr))
					{
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNbr));
					} // end if(!LocationManager.isValidStoreNumber(storeNbr))
					
					// next prepare the query to get the calendars for the store
					MapStream inputs = new MapStream(READ_REQUISITION_CALENDAR_WITH_COUNTS);
					inputs.put(HR_SYS_STR_NBR, storeNbr);
					inputs.put(REQN_SCH_STAT_CD, AVALIABLE_STATUS_CODE);
					inputs.put(ACTV_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to get active requisition calendars (with counts) for store %1$s", storeNbr));
					} // end if
					query.getResult(connection, resultsHandler, inputs);
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting calendars for store %1$s", storeNbr), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getRequisitionCalendarsAndCountsForStore(), store: %1$s. Returning %2$d calendars in %3$.9f seconds",
				storeNbr, (resultsHandler.getCalendars() == null ? 0 : resultsHandler.getCalendars().size()), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return resultsHandler.getCalendars();
	} // end function getRequisitionCalendarsForStore()
	
	/**
	 * This method creates a new requisition calendar using the information provided
	 * 
	 * @param name calendar name
	 * @param storeNumber number of the store the calendar is for
	 * @param type calendar type (from HR_CAL_TYP_CD).
	 * 
	 * @return id of the newly created requisition calendar
	 * 
	 * @throws QueryException thrown if the name provided is null or empty, if the store number provided
	 *                        is null, empty, is not a 4 digit value, or does not exist, if the requisition calendar
	 *                        type does not exist, if the type is default or MET and the store already has that type
	 *                        of calendar, or if an exception occurs querying the database
	 *                        
	 */
	public static int createRequisitionCalendar(final String name, final String storeNumber, final short type) throws QueryException
	{
		long startTime = 0;
		int calendarId = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering createRequistionCalendar(), name: %1$s, storeNumber: %2$s, type: %3$d", name, storeNumber, type));
		} // end if
		
		try
		{
			// validate the name provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.REQUISITION_CALENDAR_NAME, name);
			// validate the store number provided
			ValidationUtils.validateStoreNumber(storeNumber);

			// get an instance of the DAO QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
			// create a handler object that will be used to process the results of the query
			ScheduleHandler resultsHandler = new ScheduleHandler();
			
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
					ScheduleHandler resultsHandler = (ScheduleHandler)handler;
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to validate store number %1$s", storeNumber));
					} // end if			
					// first validate the location number provided is valid
					if(!LocationManager.isValidStoreNumber(storeNumber))
					{
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNumber));
					} // end if(!LocationManager.isValidStoreNumber(storeNbr))
					
					// TODO : switch this over to cache the type codes once the language code is added to the NLS table
					// next prepare the query that will validate the calendar type provided (in the HR_CAL_TYP_CD table)
					MapStream inputs = new MapStream(READ_REQUISITION_CALENDAR_TYPE_EXIST);
					inputs.put(HR_CAL_TYP_CD, type);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Validating requisition calendar type %1$d", type));
					} // end if
					// execute the query
					query.getObject(connection, resultsHandler, inputs);
					
					if(!resultsHandler.isValid())
					{
						throw new ValidationException(InputField.REQUISITION_CALENDAR_TYPE, String.format("Requisition calendar type %1$d is not valid", type));
					} // end if(!resultsHandler.isValid())
					
					// if this is a MET or a DEFAULT calendar, make sure that one does not already exist for the store before creating a new one
					if(type == REQN_CAL_TYP_DEFAULT || type == REQN_CAL_TYP_MET)
					{
						// setup the query
						inputs.clear();
						inputs.setSelectorName(READ_REQUISITION_CALENDAR);
						inputs.put(HR_SYS_STR_NBR, storeNumber);
						inputs.put(HR_CAL_TYP_CD, type);

						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Request to create a new %1$s, verifying one does not already exist for store %2$s",
								(type == REQN_CAL_TYP_DEFAULT ? "default" : "MET"), storeNumber));
						} // end if
						// execute the query
						query.getResult(connection, resultsHandler, inputs);
						
						// if a row was found, throw an exception
						if(resultsHandler.getCalendars() != null && resultsHandler.getCalendars().size() > 0)
						{
							throw new ValidationException(InputField.REQUISITION_CALENDAR_TYPE, String.format("A %1$s calendar already exists for store %2$s", 
								(type == REQN_CAL_TYP_DEFAULT ? "default" : "MET"), storeNumber));
						} // end if(resultsHandler.getCalendars() != null && resultsHandler.getCalendars().size() > 0)
					} // end if(type == REQN_CAL_TYP_DEFAULT || type == REQN_CAL_TYP_MET)
					
					// lastly create the calendar
					inputs.clear();
					inputs.setSelectorName(INS_HR_REQN_CAL);
					inputs.put(REQN_CAL_DESC, StringUtils.trim(name));
					inputs.put(HR_SYS_STR_NBR, StringUtils.trim(storeNumber));
					inputs.put(HR_CAL_TYP_CD, type);
					inputs.put(ACTV_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Creating type %1$d requisition calendar for store %2$s named %3$s", type, storeNumber, name));
					} // end if
					query.insertObject(connection, resultsHandler, inputs);					
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();					
			
			// set the return value to the ID of the calendar just created
			calendarId = resultsHandler.getCalendarId();
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred creating type %1$d requisition calendar named %2$s for store %3$s", 
				type, name, storeNumber));
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Entering createRequistionCalendar(), name: %1$s, storeNumber: %2$s, type: %3$d. Successfully created calendar %4$d in %5$.9f seconds", 
				name, storeNumber, type, calendarId, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return calendarId;
	} // end function createRequisitionCalendar()

	/**
	 * This method gets available interview slots using the criteria provided.
	 * 
	 * @param rscFlag
	 *            Indicates if requisition scheduling is being managed by the
	 *            RSC or not
	 * @param calendarId
	 *            The requisition calendar to get available blocks for
	 * @param beginTime
	 *            Beginning of the time range to get blocks for
	 * @param duration
	 *            Duration of the interview block. CURRENTLY ONLY 30 MINUTE AND
	 *            60 MINUTE interview blocks are supported
	 * 
	 * @return A list of available interview blocks between beginTime and
	 *         endTime. An empty collection will be returned if no blocks are
	 *         available
	 * 
	 * @throws RetailStaffingException
	 *             Thrown if : The calendar ID is < 0 The duration provided is
	 *             not 30 or 60 beginTime is null endTime is null beginTime is
	 *             <= today endTime < beginTime An exception occurs querying the
	 *             database
	 */
	@SuppressWarnings("deprecation")
	public static List<RequisitionScheduleTO> getAvailableInterviewSlots(String rscFlag, int calendarId, Timestamp beginTime, short duration)
	    throws RetailStaffingException
	{
		List<InterviewAvaliableSlotsTO> interviewSlots = null;
		List<RequisitionScheduleTO> rawBlocks = null;

		long startTime = 0;
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getAvailableInterviewSlots(), RSC flag: %1$s, calendar id: %2$d, begin time: %3$s, duration: %4$d", rscFlag,
			    calendarId, beginTime, duration));
			startTime = System.nanoTime();
		} // end if

		// TODO : Why even pass this in, why not just change whatever is calling
		// this not to call it if the RSC flag is not YES?
		// make sure the rscFlag is false
		if(rscFlag != null && rscFlag.toString().trim().equalsIgnoreCase("N"))
		{
			mLogger.error(String.format("Invalid RSC Flag id %1$s provided", rscFlag.toString()));
			return new ArrayList<RequisitionScheduleTO>();
		} // end if

		// make sure the calendar id is > 0
		if(calendarId <= 0)
		{
			mLogger.error(String.format("Invalid calendar id %1$d provided", calendarId));
			return new ArrayList<RequisitionScheduleTO>();
			// TODO : follow-up, will a calendar id ever be < 0? if not need to
			// re-visit this validation and add the throws back in
			// throw new RetailStaffingException(INVALID_INPUT_CODE,
			// String.format("invalid calendar id %1$d provided", calendarId));
		} // end if(calendarId <= 0)

		// make sure the duration is either 30 or 60 minutes
		if(duration != SCHEDULING_INTERVAL_30 && duration != SCHEDULING_INTERVAL_60)
		{
			mLogger.error(String.format("Invalid duration %1$d provided, only %2$d and %3$d are supported", duration, SCHEDULING_INTERVAL_30, SCHEDULING_INTERVAL_60));
			return new ArrayList<RequisitionScheduleTO>();
			// TODO: why is this commented out, this is a valid error condition
			// since the query has logic hard-coded for 30 and 60 minute
			// durations only!
			// throw new RetailStaffingException(INVALID_INPUT_CODE,
			// String.format("Invalid duration %1$d provided, only %2$d and %3$d are supported",
			// duration, SCHEDULING_INTERVAL_30, SCHEDULING_INTERVAL_60));
		} // end if(duration != SCHEDULING_INTERVAL_30 && duration !=
		  // SCHEDULING_INTERVAL_60)

		// make sure the begin date is not null
		if(beginTime == null)
		{
			mLogger.error("Null begin time provided");
			throw new RetailStaffingException(INVALID_INPUT_CODE, "Null begin time provided");
		} // end if(beginTime == null)

		// create a calendar object with the earliest possible time for today
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, today.getActualMinimum(Calendar.HOUR));
		today.set(Calendar.MINUTE, today.getActualMinimum(Calendar.MINUTE));
		today.set(Calendar.SECOND, today.getActualMinimum(Calendar.SECOND));
		today.set(Calendar.MILLISECOND, today.getActualMinimum(Calendar.MILLISECOND));

		// table precision should've stopped at mins, but since it doesn't set
		// the seconds and milliseconds to 0
		beginTime.setSeconds(0);
		beginTime.setNanos(0);

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Earliest time today = %1$s", today.getTime().toString()));
			mLogger.debug(String.format("Modified begin time = %1$s", beginTime.toString()));
		} // end if

		// make sure the begin time is not prior to today
		if(beginTime.before(today.getTime()))
		{
			mLogger.error(String.format("Invalid begin time %1$s provided, begin time must be later than %2$s", beginTime.toString(), today.getTime().toString()));
			throw new RetailStaffingException(INVALID_INPUT_CODE, String.format("Invalid begin time %1$s provided, begin time must be later than %2$s",
			    beginTime.toString(), today.getTime().toString()));
		} // end if(beginTime.before(today.getTime()))

		try
		{
			// get all interview blocks after the begin time
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Querying for available interview slots using calendar id: %1$d, begin time: %2$s and duration: %3$d", calendarId,
				    beginTime, duration));
			} // end if

			interviewSlots = ScheduleDAO.readHumanResourcesRequisitionScheduleAvailableSlotsByInputList(calendarId, beginTime, ADDL_DURATION_MINS,
			    AVALIABLE_STATUS_CODE, MAX_INTRVW_SLOTS, (duration != SCHEDULING_INTERVAL_30));

			rawBlocks = new ArrayList<RequisitionScheduleTO>();
			RequisitionScheduleTO scheduleBlock = null;

			// collection of interview blocks that are within the range with the
			// correct duration
			Map<Timestamp, RequisitionScheduleTO> scheduleBlocks = new TreeMap<Timestamp, RequisitionScheduleTO>();

			// check the Block has details
			if(interviewSlots != null)
			{
				// Check for the Duplicates and Store the unique Time stamp
				for(InterviewAvaliableSlotsTO slot : interviewSlots)
				{
					scheduleBlock = new RequisitionScheduleTO();
					scheduleBlock.setBeginTimestamp(slot.getBeginTimestamp());
					scheduleBlock.setEndTimestamp(Util.getInterviewTimeStampByMinutes(slot.getBeginTimestamp(), duration));
					scheduleBlock.setHumanResourcesSystemStoreNumber(slot.getStoreNumber());
					scheduleBlock.setInterviewerAvailabilityCount(slot.getSequenceNumber());
					scheduleBlock.setCalendarId(calendarId);

					// add it to the Schedule block collection with the begin timestamp as the key
					scheduleBlocks.put(scheduleBlock.getBeginTimestamp(), scheduleBlock);
				} // end for(InterviewAvaliableSlotsTO slot : interviewSlots)

				// only need to continue processing if there were scheduleBlocks
				// found in the range provided
				if(scheduleBlocks != null && scheduleBlocks.size() > 0)
				{
					for(Entry<Timestamp, RequisitionScheduleTO> block : scheduleBlocks.entrySet())
					{
						if(block.getValue().getInterviewerAvailabilityCount() > 0)
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Adding interview slot with begin time %1$s and store number %2$s", 
									block.getValue().getBeginTimestamp().toString(), block.getValue().getHumanResourcesSystemStoreNumber()));
							} // end if

							rawBlocks.add(block.getValue());
						} // end
						  // if(block.getValue().getInterviewerAvailabilityCount()
						  // > 0)
					} // end for(Entry<Timestamp, RequisitionScheduleTO> block :
					  // scheduleBlocks.entrySet())
				} // end if(scheduleBlocks != null && scheduleBlocks.size() > 0)
			} // end if(interviewSlots != null)
		} // end try
		catch (QueryException qe)
		{
			mLogger.error(String.format("An exception occurred getting available times for calendar %1$d", calendarId), qe);
			throw new RetailStaffingException(APP_FATAL_ERROR_CODE, qe);
		} // end catch

		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			
			if(startTime == 0) { startTime = endTime; }
			
			mLogger.debug(String.format("Exiting getAvailableInterviewSlots(), RSC flag: %1$s, calendar id: %2$d, begin time: %3$s, duration: %4$d, total time to process request: %5$.9f seconds",
	            rscFlag, calendarId, beginTime, duration, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		return rawBlocks;
	} // end function getAvailableInterviewSlots()

	/**
	 * This method is used to get available interview dates for the phone screen
	 * id provided excluding any dates that may have previously been offered
	 * 
	 * @param phoneScreenId				Phone screen to get available interview dates for
	 * @param prevOfferedDates			List of dates that were previously offered or null if no dates
	 *            						were previously offered
	 * 
	 * @return 							List of dates that are available for interviews
	 * 
	 * @throws QueryException			Thrown if the phone screen id is &lt; 1, if any of the previously offered dates are null,
	 * 									if any of the previously offered dates are prior to today, or if an exception occurs querying 
	 * 									the database for available interview dates
	 */
	public static List<Date> getInterviewAvailableDates(int phoneScreenId, List<Date> prevOfferedDates) throws QueryException
	{
		List<Date> availDates = null;

		if(mLogger.isDebugEnabled())
		{
			StringBuilder data = new StringBuilder();
			data.append("Entering getInterviewAvailableDates(), phoneScreenId: ").append(phoneScreenId).append(", prevOfferedDates: ");

			if(prevOfferedDates == null)
			{
				data.append("null");
			} // end if(prevOfferedDates == null)
			else
			{
				for(Date offeredDate : prevOfferedDates)
				{
					data.append(offeredDate.toString()).append("    ");
				} // end for(Date offeredDate : prevOfferedDates)
			} // end else

			mLogger.debug(data.toString());
		} // end if

		try
		{
			// validate the phone screen id provided is not < 0
			ValidationUtils.validatePhoneScreenId(phoneScreenId);

			// the latest previously offered date
			Date maxPrevOffDate = null;

			// if dates were provided
			if(prevOfferedDates != null && prevOfferedDates.size() > 0)
			{
				// iterate the dates provided
				for(Date offeredDate : prevOfferedDates)
				{
					// validate the date is not null and is not prior to today
					ValidationUtils.validateNotPriorToToday(InputField.OFFERED_DATE, offeredDate);

					// determine if this is the maximum previously offered date
					if(maxPrevOffDate == null || offeredDate.after(maxPrevOffDate))
					{
						// the current date is later, so use it
						maxPrevOffDate = offeredDate;

						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Latest previously offered date for phone screen %1$d set to %2$s", phoneScreenId, maxPrevOffDate));
						} // end if
					} // end if(maxPrevOffDate == null ||
					  // offeredDate.after(maxPrevOffDate))
				} // end for(Date offeredDate : prevOfferedDates)
			} // end if(prevOfferedDates != null && prevOfferedDates.size() > 0)

			// determine what begin time to use when pulling available interview slots
			Timestamp beginTime = null;

			// if no dates have been previously offered, the maxPrevOffDate will be null
			if(maxPrevOffDate == null)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("No previous dates were offered for phone screen %1$d", phoneScreenId));
				} // end if

				// get the cutoff time
				OrgParamTO schCutoffTime = OrgParamManager.getOrgParam(HR_SUBSYS_CD, BU, ORG_PARM_INTSCH_CUTOFF, SVC_TYP_IND, true);
				// make sure the parameter existed with a value
				if(schCutoffTime == null || schCutoffTime.getTimeVal() == null)
				{
					throw new QueryException(String.format("Unable to load cutoff time organization parameter (%1$s)", ORG_PARM_INTSCH_CUTOFF));
				} // end if(schCutoffTime == null || schCutoffTime.getTimeVal() == null)

				// will contain one of two offset values based on how current time compares to the offset
				OrgParamTO offset = null;

				/*
				 * Convert current date into a time object. Have to use a
				 * formatter here, otherwise, the milliseconds for the current
				 * date will always make current be later than the time object
				 * being read from the database which does not have any date
				 * associated with it
				 */
				SimpleDateFormat formatter = new SimpleDateFormat(TIME_FORMAT_STRING);
				Calendar current = Calendar.getInstance();
				Time currentTime = Time.valueOf(formatter.format(current.getTime()));

				// if the current time is before the offset
				if(currentTime.before(schCutoffTime.getTimeVal()))
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Current time %1$s is before cutoff time %2$s", currentTime.toString(), schCutoffTime.getTimeVal().toString()));
					} // end if(currentTime.before(schCutoffTime.getTimeVal()))

					// get the pre-cutoff time offset parameter
					offset = OrgParamManager.getOrgParam(HR_SUBSYS_CD, BU, ORG_PARM_INTSCH_PRECUTOFF_OFFSET, SVC_TYP_IND, true);
					// make sure the time offset parameter is not null
					if(offset == null)
					{
						throw new QueryException(String.format("Unable to load pre-cutoff time offset organization parameter (%1$s)", ORG_PARM_INTSCH_PRECUTOFF_OFFSET));
					} // end if(offset == null)

					// add the offset hours to the calendar
					current.add(Calendar.HOUR, offset.getIntVal());
					// set the begin time
					beginTime = new Timestamp(current.getTimeInMillis());
				} // end if(currentTime.before(schCutoffTime.getTimeVal()))
				else // the current time is after the cut-off time
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Current time %1$s is after cutoff time %2$s", currentTime.toString(), schCutoffTime.getTimeVal().toString()));
					} // end if(currentTime.before(schCutoffTime.getTimeVal()))

					// get the post-cutoff time offset parameter
					offset = OrgParamManager.getOrgParam(HR_SUBSYS_CD, BU, ORG_PARM_INTSCH_POSTCUTOFF_OFFSET, SVC_TYP_IND, true);
					// make sure the time offset parameter is not null
					if(offset == null)
					{
						throw new QueryException(String.format("Unable to load post-cutoff time offset organization parameter (%1$s)", ORG_PARM_INTSCH_POSTCUTOFF_OFFSET));
					} // end if(offset == null)

					// add the offset hours to the calendar
					current.add(Calendar.HOUR, offset.getIntVal());
					/*
					 * Offset hour just ensures we're on the correct day (day
					 * after tomorrow unless the offset value changes from 48
					 * hours). so all we have to do is set the new time to
					 * midnight on the new date
					 */
					current.set(Calendar.HOUR_OF_DAY, current.getActualMinimum(Calendar.HOUR_OF_DAY));
					current.set(Calendar.MINUTE, current.getActualMinimum(Calendar.MINUTE));
					current.set(Calendar.SECOND, current.getActualMinimum(Calendar.SECOND));
					current.set(Calendar.MILLISECOND, current.getActualMinimum(Calendar.MILLISECOND));
					// set the begin time
					beginTime = new Timestamp(current.getTimeInMillis());
				} // end else
			} // end if(maxPrevOffDate == null)
			else // previous dates were offered
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Previous dates were offered for phone screen %1$d, setting begin time be later than %2$s", 
						phoneScreenId, maxPrevOffDate.toString()));
				} // end else

				// initialize a calendar with the max date as the seed date/time
				Calendar max = Calendar.getInstance();
				max.setTime(maxPrevOffDate);
				// set the time to midnight of the max
				max.set(Calendar.HOUR_OF_DAY, max.getActualMinimum(Calendar.HOUR_OF_DAY));
				max.set(Calendar.MINUTE, max.getActualMinimum(Calendar.MINUTE));
				max.set(Calendar.SECOND, max.getActualMinimum(Calendar.SECOND));
				max.set(Calendar.MILLISECOND, max.getActualMinimum(Calendar.MILLISECOND));
				// add one day to the max
				max.add(Calendar.DATE, 1);
				// set the begin time
				beginTime = new Timestamp(max.getTimeInMillis());
			} // end else

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Begin time for phone screen id %1$d set to %2$s", phoneScreenId, beginTime.toString()));
				mLogger.debug(String.format("Getting available interview slots for phone screen %1$d, using begin time: %2$s", phoneScreenId, beginTime
				    .toString()));
			} // end if

			// invoke the logic to get the available interview slots for the phone screen
			List<RequisitionScheduleTO> availSlots = getAvailIntrvwSlotsForPhoneScreen(phoneScreenId, beginTime);

			// if slots were returned
			if(availSlots != null && availSlots.size() > 0)
			{
				mLogger.debug(String.format("Found %2$d available interview slots for phone screen %2$d", availSlots.size(), phoneScreenId));

				// initialize the return list
				availDates = new ArrayList<Date>();

				Calendar slotDate = Calendar.getInstance();
				// iterate the slots returned and put them in the return map
				for(RequisitionScheduleTO availSlot : availSlots)
				{
					// seed the calendar from the begin time of the interview slot
					slotDate.setTimeInMillis(availSlot.getBeginTimestamp().getTime());
					// 0 out the times so we only compare the dates
					slotDate.set(Calendar.HOUR_OF_DAY, slotDate.getActualMinimum(Calendar.HOUR_OF_DAY));
					slotDate.set(Calendar.MINUTE, slotDate.getActualMinimum(Calendar.MINUTE));
					slotDate.set(Calendar.SECOND, slotDate.getActualMinimum(Calendar.SECOND));
					slotDate.set(Calendar.MILLISECOND, slotDate.getActualMinimum(Calendar.MILLISECOND));

					// if it's not already in the return list
					if(!availDates.contains(slotDate.getTime()))
					{
						// add it to the return list
						availDates.add(slotDate.getTime());
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Added date %1$s to the avail dates list for phone screen %2$d", slotDate.getTime().toString(), phoneScreenId));
						} // end if
					} // end if(!availDates.contains(slotDate.getTime()))
				} // end for(RequisitionScheduleTO availSlot : availSlots)

				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Sorting available dates list for phone screen %1$d", phoneScreenId));
				} // end if

				// now sort the array list going back
				Collections.sort(availDates);
			} // end if(availSlots != null && availSlots.size() > 0)
		} // end try
		catch (QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred retrieving available interview dates for phone screen id %1$s", phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getInterviewAvailableDates(), phoneScreenId: %1$d", phoneScreenId));
		} // end if

		return availDates;
	} // end function getInterviewAvailableDates()
	
	/**
	 * This method is used to get interview time availability (preference) for
	 * the phone screen id and date provided.
	 * 
	 * @param phoneScreenId				The phone screen to get time availability for
	 * @param preferredDate				The date to get time availability for
	 * 
	 * @return 							'AM' if the date provided only has AM interview slots available,
	 *         							'PM' if the date provided only has PM interview slots available,
	 *         							'BOTH' if the date provided has both AM and PM interview slots
	 *         							available, or null if the date provided does not have any
	 *         							interview slots available
	 * 
	 * @throws ValidationException	Thrown if any of the following are true:
	 *				<ul>
	 *					<li>the phone screen id provided is &lt;= 0</li>
	 *					<li>the preferred date provided is null</li>
	 *					<li>the preferred date provided is before today</li>
	 *				</ul>
	 * 
	 * @throws QueryException			Thrown if the phone screen id provided is &lt;= 0, if the preferred date provided is null, if the
	 * 									preferred date provided is prior to today, or if an exception occurs querying the database for
	 *             						interview time availability
	 */
	public static TimePreference getInterviewTimePreference(int phoneScreenId, Date preferredDate) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getInterviewTimePreference(), phoneScreenId: %1$d, preferredDate: %2$s", 
				phoneScreenId, (preferredDate == null ? "null" : preferredDate.toString())));
		} // end if
		TimePreference timePreference = null;

		try
		{
			// validate the phone screen id provided is > 0
			ValidationUtils.validatePhoneScreenId(phoneScreenId);
			// validate the preferredDate is not null or prior to today
			ValidationUtils.validateNotPriorToToday(InputField.PREFERRED_DATE, preferredDate);

			// convert the preferredDate into a calendar object
			Calendar prefCal = Calendar.getInstance();

			/*
			 * Will only populate preference if the preferred date provided is after today. This should
			 * work because the hours of operations for the RSC are 08:00 - 20:00 EST. If the hours of 
			 * operations for the RSC ever begin prior to 06:00 EST then we will need to re-examine
			 * this logic.
			 */
			if(Util.isAfterToday(preferredDate))
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("%1$s is after today, calculating begin time", preferredDate.toString()));
				} // end if
				
				/*
				 * Set the earliest time for the day provided based on whether it is tomorrow or later.
				 * If the date provided is after tomorrow, use the date provided and set the begin time to
				 * midnight. If the date provided is tomorrow, add the offset the the current time and use that
				 * as the begin time.
				 */			
				if(Util.isTomorrow(preferredDate))
				{
					// get the pre-cutoff time offset parameter
					OrgParamTO offset = OrgParamManager.getOrgParam(HR_SUBSYS_CD, BU, ORG_PARM_INTSCH_PRECUTOFF_OFFSET, SVC_TYP_IND, true);
					// make sure the time offset parameter is not null
					if(offset == null)
					{
						throw new QueryException(String.format("Unable to load pre-cutoff time offset organization parameter (%1$s)", ORG_PARM_INTSCH_PRECUTOFF_OFFSET));
					} // end if(offset == null)

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Preferred date provided for phone screen %1$d is tomorrow, adding %2$d hours to current time %3$s", 
							phoneScreenId, offset.getIntVal(), prefCal.getTime().toString()));
					} // end if

					// add the offset hours to the calendar
					prefCal.add(Calendar.HOUR, offset.getIntVal());					
				} // end if(Util.isTomorrow(preferredDate))
				else // it's after tomorrow
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Preferred date provided for phone screen %1$d is after tomorrow, using earliest time on preferred date %2$s", 
							phoneScreenId, preferredDate.toString()));
					} // end if
					// seed the preferred calendar
					prefCal.setTime(preferredDate);
					// set the time to the earliest time for the day
					prefCal.set(Calendar.HOUR_OF_DAY, prefCal.getActualMinimum(Calendar.HOUR_OF_DAY));
					prefCal.set(Calendar.MINUTE, prefCal.getActualMinimum(Calendar.MINUTE));
					prefCal.set(Calendar.SECOND, prefCal.getActualMinimum(Calendar.SECOND));
					prefCal.set(Calendar.MILLISECOND, prefCal.getActualMinimum(Calendar.MILLISECOND));									
				} // end else
				
				// convert the preferred calendar into a timestamp so we can feed it to the BL function
				Timestamp beginTime = new Timestamp(prefCal.getTimeInMillis());

				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Getting available interview slots for phone screen %1$d, using begin time: %2$s", phoneScreenId, beginTime.toString()));
				} // end if

				// invoke the logic to get the available interview slots for the phone screen
				List<RequisitionScheduleTO> availSlots = getAvailIntrvwSlotsForPhoneScreen(phoneScreenId, beginTime);

				// if interview slots were returned
				if(availSlots != null && !availSlots.isEmpty())
				{
					// set the prefCalendar time to the last time on the preferred day
					prefCal.set(Calendar.HOUR_OF_DAY, prefCal.getActualMaximum(Calendar.HOUR_OF_DAY));
					prefCal.set(Calendar.MINUTE, prefCal.getActualMaximum(Calendar.MINUTE));
					prefCal.set(Calendar.SECOND, prefCal.getActualMaximum(Calendar.SECOND));
					prefCal.set(Calendar.MILLISECOND, prefCal.getActualMaximum(Calendar.MILLISECOND));

					// initialize the calendar to noon on the preferred day
					Calendar noon = (Calendar)prefCal.clone();
					noon.set(Calendar.HOUR_OF_DAY, 12);
					noon.set(Calendar.MINUTE, noon.getActualMinimum(Calendar.MINUTE));
					noon.set(Calendar.SECOND, noon.getActualMinimum(Calendar.SECOND));
					noon.set(Calendar.MILLISECOND, noon.getActualMinimum(Calendar.MILLISECOND));

					// will hold am/pm availability
					boolean amAvail = false;
					boolean pmAvail = false;

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Last time on preferred date %1$s for phone screen id %2$d is: %3$s, noon on preferred date is: %4$s",
						    preferredDate.toString(), phoneScreenId, prefCal.getTime().toString(), noon.getTime().toString()));
					} // end if

					/*
					 * Slots are returned in order by date, so iterate until we get past the preferred date or 
					 * until both flags are true. 
					 */
					for(RequisitionScheduleTO slot : availSlots)
					{
						// if this slot is for the preferred day
						if(slot.getBeginTimestamp().before(prefCal.getTime()))
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("%1$s is on the preferred date %2$s for phone screen %3$d", slot.getBeginTimestamp().toString(),
								    prefCal.getTime().toString(), phoneScreenId));
							} // end if

							// if the am available flag has not been set yet
							if(!amAvail)
							{
								// set it to true if this slot is before noon
								amAvail = slot.getBeginTimestamp().before(noon.getTime());

								if(mLogger.isDebugEnabled())
								{
									mLogger.debug(String.format("Checking if %1$s is before %2$s, amAvail set to %3$b for phone screen %4$d", 
										slot.getBeginTimestamp().toString(), noon.getTime().toString(), amAvail, phoneScreenId));
								} // end if
							} // end if(!amAvail)

							// if the pm available flag has not been set yet
							if(!pmAvail)
							{
								// set it to true if this slot is after noon (doing
								// a !before to handle equals and after)
								pmAvail = !(slot.getBeginTimestamp().before(noon.getTime()));

								if(mLogger.isDebugEnabled())
								{
									mLogger.debug(String.format("Checking if %1$s is equal to/after %2$s, pmAvail set to %3$b for phone screen %4$d", 
										slot.getBeginTimestamp().toString(), noon.getTime().toString(), pmAvail, phoneScreenId));
								} // end if
							} // end if(!pmAvail)

							// if both am and pm flags are set to true, break out of the loop
							if(amAvail && pmAvail)
							{
								if(mLogger.isDebugEnabled())
								{
									mLogger.debug(String.format("Both flags are set for phone screen %1$d, breaking", phoneScreenId));
								} // end if
								break;
							} // end if(amAvail && pmAvail)
						} // end
						  // if(slot.getBeginTimestamp().before(prefCal.getTime()))
						else
						{
							// we've passed onto the next day, so quit looping
							break;
						} // end else
					} // end for(RequisitionScheduleTO slot : availSlots)

					// now setup the return value based on the flags we set while iterating the slots
					if(amAvail && pmAvail)
					{
						// if both are available, set the time preference to both
						timePreference = TimePreference.BOTH;

						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("%1$s has BOTH AM and PM slots available for phone screen %2$d", preferredDate.toString(), phoneScreenId));
						} // end if
					} // end if(amAvail && pmAvail)
					else if(amAvail)
					{
						// if only AM is available
						timePreference = TimePreference.AM;

						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("%1$s has ONLY AM slots available for phone screen %2$d", preferredDate.toString(), phoneScreenId));
						} // end if
					} // end else if(amAvail)
					else if(pmAvail)
					{
						// if only PM is available
						timePreference = TimePreference.PM;

						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("%1$s has ONLY PM slots available for phone screen %2$d", preferredDate.toString(), phoneScreenId));
						} // end if
					} // end else if(pmAvail)
					// no preference will return null;

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Time preference for phone screen id %1$d on preferred date %2$s set to %3$s", 
							phoneScreenId, preferredDate.toString(), timePreference));
					} // end if
				} // end if(availSlots != null && !availSlots.isEmpty())				
			} // end if(Util.isAfterToday(preferredDate))			
		} // end try
		catch(QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred retrieving interview time preference for phone screen id %1$s and preferredDate %2$s",
			    phoneScreenId, preferredDate.toString()), qe);
			// throw the error
			throw qe;
		} // end catch

		return timePreference;
	} // end function getInterviewTimePreference()	
	
	/**
	 * This method retrieves and reserves multiple interview slots for the phone screen and date/time preference provided
	 * 
	 * @param phoneScreenId						Phone screen id that we are reserving slots for
	 * @param timePreference					AM/PM time preference
	 * @param preferredDate						Date to reserve slots on
	 * @param maxRecordsToReturn				Maximum number of interview slots to return 
	 * @param offeredSlots						The InterviewSchedule web service allows multiple calls to this function. If
	 * 											a subsequent call is made for the same phone screen that already has slots
	 * 											reserved, the slots previously reserved can be passed in here. All slots received
	 * 											in this variable will be "released" and not sent back in the next return
	 * 
	 * @return									List of reserved slots or an empty list if no slots were available
	 * 
	 * @throws QueryException					Thrown if the phone screen id provided is &lt; 1, if the time preference provided is null,
	 * 											if the time preference provided is not AM or PM, if the preferred date provided is null,
	 * 											if the preferred date provided is prior to today, if hte maximum number of records to return is &lt;
	 * 											1, if any of the offered slot details are invalid or if an exception occurs looking up phone screen data, 
	 * 											looking up interview slots, or reserving interview slots.
	 */
	public static List<IntrvwAvailDate> getMultipleInterviewAvailableSlots(final int phoneScreenId, final TimePreference timePreference, Date preferredDate, 
		final int maxRecordsToReturn, final List<IntrvwAvailDate> offeredSlots) throws QueryException
	{
		final List<IntrvwAvailDate> availSlots = new ArrayList<IntrvwAvailDate>(); 
		
		if(mLogger.isDebugEnabled())
		{
			StringBuilder data = new StringBuilder();
			data.append("Entering getMultipleInterviewAvailableSlots(), phoneScreenId: ")
				.append(phoneScreenId)
				.append(", timePreference: ")
				.append(String.valueOf(timePreference))
				.append(", preferredDate: ")
				.append(preferredDate.toString())
				.append(", maxRecordsToReturn: ")
				.append(maxRecordsToReturn);
			
			// if there were previously offered dates
			if(offeredSlots != null)
			{
				data.append(", previously offered slots: ");
				// iterate and add them to the log statement
				for(IntrvwAvailDate slot : offeredSlots)
				{
					if(slot != null)
					{
						data.append(slot.getBgnTm()).append(" - ").append(slot.getEndTm()).append(" ");
					} // end if(slot != null)
					else
					{
						data.append("null");
					} // end else
				} // end for(IntrvwAvailDate slot : offeredSlots)
			} // end if(offeredSlots != null)
			
			mLogger.debug(data.toString());
		} // end if
		
		try
		{
			// validate the phone screen id provided is greater than 0	
			ValidationUtils.validatePhoneScreenId(phoneScreenId);
			// validate the time preference is not null
			ValidationUtils.validateNotNull(InputField.INTERVIEW_TIME_PREFERENCE, timePreference);
			// validate the time preference provided is AM or PM
			if(timePreference != TimePreference.AM && timePreference != TimePreference.PM)
			{
				throw new ValidationException(InputField.INTERVIEW_TIME_PREFERENCE, String.format("Invalid time preference %1$s provided, only AM and PM are valid", 
					String.valueOf(timePreference)));
			} // end if(timePreference != TimePreference.AM && timePreference != TimePreference.PM)
			
			// validate the preferred date is not null or prior to today
			ValidationUtils.validateNotNull(InputField.PREFERRED_DATE, preferredDate);
			// validate the max records to return is greater than 0
			ValidationUtils.validateGreaterThan(InputField.MAX_RECORD_COUNT, maxRecordsToReturn, MIN_MAX_RECORD_COUNT);
			
			/*
			 * collection of slots to release. I would rather initialize this when I know I will use it,
			 * but in order to be able to use it in the anonymous inner class it has to be declared final.
			 */
			final List<InterviewAvaliableSlotsTO> slotsToRelease = new ArrayList<InterviewAvaliableSlotsTO>();
			Timestamp latestBeginTime = null;
			
			// if interview available dates were previously offered (this is a subsequent call)
			if(offeredSlots != null)
			{
				// iterate the interview available dates
				for(IntrvwAvailDate offered : offeredSlots)
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Interview Date %1$s provided for phone screen %2$d", offered.getIntrvwDt(), phoneScreenId));
					} // end if
					
					// validate the interview date is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.INTERVIEW_DATE, offered.getIntrvwDt(), VALID_REGEX_DATE_FORMAT, false);
					// validate the begin time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.INTERVIEW_BEGIN_TIME, offered.getBgnTm(), VALID_REGEX_TIME_FORMAT, false);
					// validate the end time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.INTERVIEW_END_TIME, offered.getEndTm(), VALID_REGEX_TIME_FORMAT, false);
					
					// every available interview date must have at least one offered
					if(offered.getIntrvwAvailSlots() == null || offered.getIntrvwAvailSlots().size() == 0)
					{
						throw new ValidationException(InputField.INTERVIEW_SLOT, String.format("Interview date %1$s did not contain any interview slots", offered.getIntrvwDt()));
					} // end if(offered.getIntrvwAvailSlots() == null || offered.getIntrvwAvailSlots().size() == 0)
					
					// iterate the interview slots that make up this interview date
					for(InterviewAvaliableSlotsTO slot : offered.getIntrvwAvailSlots())
					{
						// validate the slot
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
						ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
						ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
						ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
						ValidationUtils.validateStoreNumber(slot.getStoreNumber());						
						
						// if this is slot has the latest begin time
						if(latestBeginTime == null || slot.getBeginTimestamp().after(latestBeginTime))
						{
							// set it
							latestBeginTime = slot.getBeginTimestamp();
							
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Setting latest begin time for phone screen %1$d to %2$s", phoneScreenId, latestBeginTime.toString()));
							} // end if
						} // end if(latestBeginTime == null || slot.getBeginTimestamp().after(latestBeginTime))
						
						// add this slot to the collection of slots to be released
						slotsToRelease.add(slot);
					} // end for(InterviewAvaliableSlotsTO slot : offered.getIntrvwAvailSlots())
				} // end for(IntrvwAvailDate offered : offeredSlots)
			} // end if(offeredSlots != null)

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting available %1$s interview slots on %2$s for phone screen %3$d", String.valueOf(timePreference), 
					preferredDate.toString(), phoneScreenId));
			} // end if
			
			/*
			 * We will only retrieve slots if the preferred date provided is after today. This should 
			 * work because the hours of operations for the RSC are 08:00 - 20:00 EST. If the hours of
			 * operations for the RSC ever go prior to 06:00 EST then we will have to re-examine this
			 * logic.
			 */
			if(Util.isAfterToday(preferredDate))
			{
				// determine the date range that applies (using the time preference and any previously offered dates, and the current time)
				Calendar beginCal = Calendar.getInstance();
				// have to make this final to use it in the anonymous inner class
				final Calendar endCal = (Calendar)beginCal.clone();
				
				// if the preferred date provided is tomorrow
				if(Util.isTomorrow(preferredDate))
				{
					// get the pre-cutoff time offset parameter
					OrgParamTO offset = OrgParamManager.getOrgParam(HR_SUBSYS_CD, BU, ORG_PARM_INTSCH_PRECUTOFF_OFFSET, SVC_TYP_IND, true);
					// make sure the time offset parameter is not null
					if(offset == null)
					{
						throw new QueryException(String.format("Unable to load pre-cutoff time offset organization parameter (%1$s)", ORG_PARM_INTSCH_PRECUTOFF_OFFSET));
					} // end if(offset == null)

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Preferred date provided for phone screen %1$d is tomorrow, adding %2$d hours to current time %3$s", 
							phoneScreenId, offset.getIntVal(), beginCal.getTime().toString()));
					} // end if

					// add the offset hours to the calendar
					beginCal.add(Calendar.HOUR, offset.getIntVal());
					// seed the end calendar with the calculated date/time
					endCal.setTime(beginCal.getTime());
										
					// If the time preference provided is morning 
					if(timePreference == TimePreference.AM)					
					{
						/*
						 * We will only set the end time here, because given the cutoff (13:00 EST) the current time + the offset
						 * currently 18 hours should always be in the morning. This logic will work as long as the offset value is
						 * less than 22 hours. So the only value we have to set is the end date to noon on the calculated date
						 */
						endCal.set(Calendar.HOUR_OF_DAY, 12);
						endCal.set(Calendar.MINUTE, endCal.getActualMinimum(Calendar.MINUTE));
						endCal.set(Calendar.SECOND, endCal.getActualMinimum(Calendar.SECOND));
						endCal.set(Calendar.MILLISECOND, endCal.getActualMinimum(Calendar.MILLISECOND));												
					} // end if(timePreference == TimePreference.AM)
					else // it's PM because the method only allows AM and PM
					{
						/*
						 * calculate "noon", really 11:59:59 AM on the calculated date because the DAO
						 * selector we will invoke does a > on the begin_ts instead of a >=
						 */
						Calendar noon = (Calendar)beginCal.clone();
						noon.set(Calendar.HOUR_OF_DAY, 11);
						noon.set(Calendar.MINUTE, noon.getActualMaximum(Calendar.MINUTE));
						noon.set(Calendar.SECOND, noon.getActualMaximum(Calendar.SECOND));
						noon.set(Calendar.MILLISECOND, noon.getActualMaximum(Calendar.MILLISECOND));
						
						// if the calculated begin time is prior to noon
						if(beginCal.before(noon))
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Calculated begin time for phone screen %1$d is prior to noon, resetting it to noon", phoneScreenId));
							} // end if
							
							// set the begin time to 11:59:59 AM on the calculated date
							beginCal.setTime(noon.getTime());
						} // end if(beginCal.before(noon))
												
						// set the end time to 11:59 PM on the calculated date
						endCal.set(Calendar.HOUR_OF_DAY, endCal.getActualMaximum(Calendar.HOUR_OF_DAY));
						endCal.set(Calendar.MINUTE, endCal.getActualMaximum(Calendar.MINUTE));
						endCal.set(Calendar.SECOND, endCal.getActualMaximum(Calendar.SECOND));
						endCal.set(Calendar.MILLISECOND, endCal.getActualMaximum(Calendar.MILLISECOND));						
					} // end else (it's PM)					
				} // end if(Util.isTomorrow(preferredDate))
				else // preferred date provided is after tomorrow
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Preferred date provided for phone screen %1$d is after tomorrow, using earliest time on preferred date %2$s for preference %3$s", 
							phoneScreenId, preferredDate.toString(), String.valueOf(timePreference)));
					} // end if

					// setup the begin and end time based on the preferred date and time preference provided
					beginCal.setTime(preferredDate);
					endCal.setTime(preferredDate);
					
					// If the time preference provided is morning 
					if(timePreference == TimePreference.AM)
					{
						// set the begin time to the earliest possible time on the preferred date
						beginCal.set(Calendar.HOUR_OF_DAY, beginCal.getActualMinimum(Calendar.HOUR_OF_DAY));
						beginCal.set(Calendar.MINUTE, beginCal.getActualMinimum(Calendar.MINUTE));
						beginCal.set(Calendar.SECOND, beginCal.getActualMinimum(Calendar.SECOND));
						beginCal.set(Calendar.MILLISECOND, beginCal.getActualMinimum(Calendar.MILLISECOND));

						// set the end time to noon on the preferred date
						endCal.set(Calendar.HOUR_OF_DAY, 12);
						endCal.set(Calendar.MINUTE, endCal.getActualMinimum(Calendar.MINUTE));
						endCal.set(Calendar.SECOND, endCal.getActualMinimum(Calendar.SECOND));
						endCal.set(Calendar.MILLISECOND, endCal.getActualMinimum(Calendar.MILLISECOND));						
					} // end if(timePreference == TimePreference.AM)
					else // it's PM because the method only allows AM and PM
					{
						// set the begin time to 11:59:59 AM on the preferred date
						beginCal.set(Calendar.HOUR_OF_DAY, 11);
						beginCal.set(Calendar.MINUTE, endCal.getActualMaximum(Calendar.MINUTE));
						beginCal.set(Calendar.SECOND, endCal.getActualMaximum(Calendar.SECOND));
						beginCal.set(Calendar.MILLISECOND, endCal.getActualMaximum(Calendar.MILLISECOND));
						
						// set the end time to 11:59 PM on the preferred date
						endCal.set(Calendar.HOUR_OF_DAY, endCal.getActualMaximum(Calendar.HOUR_OF_DAY));
						endCal.set(Calendar.MINUTE, endCal.getActualMaximum(Calendar.MINUTE));
						endCal.set(Calendar.SECOND, endCal.getActualMaximum(Calendar.SECOND));
						endCal.set(Calendar.MILLISECOND, endCal.getActualMaximum(Calendar.MILLISECOND));
					} // end else										
				} // end else

				/*
				 * If previous times were provided, the latestBeginTime object will not be null. If the preferred date is still the same
				 * as the date slots are being released for we will set the range begin time to the latest previously offered time to prevent
				 * the same slots from being returned again
				 */
				if(latestBeginTime != null && (Util.isSameDate(latestBeginTime, preferredDate)))
				{
					// use the latest begin time to determine if the same preference was provided
					Calendar lastBeginCal = Calendar.getInstance();
					lastBeginCal.setTimeInMillis(latestBeginTime.getTime());
					
					// determine the last preference provided based on the hour of day for the latest timeslot previously offered
					TimePreference lastPref = (lastBeginCal.get(Calendar.HOUR_OF_DAY) < 12 ? TimePreference.AM : TimePreference.PM);

					// only override the calculated begin time if the last request preference is the same as the current request preference
					if(timePreference == lastPref)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Previous time preference for phone screen %1$s is the same as the current %2$s, overriding begin time with %3$s",
								phoneScreenId, timePreference, latestBeginTime.toString()));
						} // end if
						
						beginCal.setTime(latestBeginTime);
					} // end if(timePreference == lastPref)
				} // end if(latestBeginTime != null && (Util.isSameDate(latestBeginTime, preferredDate)))
			
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Interview slot range for phone screen %1$d is %2$s --> %3$s", phoneScreenId, beginCal.getTime().toString(), 
						endCal.getTime().toString()));
				} // end if
				
				// create a timestamp with the begin time calculated above
				final Timestamp beginTime = new Timestamp(beginCal.getTimeInMillis());
				
				// get an instance of the QueryManager
				QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
				// get the DAO connection from the QueryManager
				DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
				
				// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
				UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, null, connection)
				{
					/*
					 * (non-Javadoc)
					 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
					 */
	                @SuppressWarnings("deprecation")
	                public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
	                {
						// first unlock any slots that were previously offered (so they are available to others)
						if(slotsToRelease != null && slotsToRelease.size() > 0)
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Releasing previously reserved slots for phone screen %1$d", phoneScreenId));
							} // end if
							// invoke the function to release the slots
							releaseIntrvwSlots(slotsToRelease);
						} // end if(slotsToRelease != null && slotsToRelease.size() > 0)	
						
						// next need to get the available interview slots for the phone screen id provided
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Querying for available %1$s interview slots for phone screen %2$d using begin time %3$s",
								String.valueOf(timePreference), phoneScreenId, beginTime.toString()));
						} // end if
						
						// execute the query to get the available interview slots
						List<RequisitionScheduleTO> slots = getAvailIntrvwSlotsForPhoneScreen(phoneScreenId, beginTime);
						
						// if slots came back
						if(slots != null && slots.size() > 0)
						{
							// collection to hold the slots that will need to be reserved
							List<InterviewAvaliableSlotsTO> slotsToReserve = new ArrayList<InterviewAvaliableSlotsTO>();
							
							IntrvwAvailDate availDate = null;
							
							// create format objects that will be used to populate the return objects
							SimpleDateFormat dateFormatter = new SimpleDateFormat(INTRVW_SVC_DATE_FORMAT);
							SimpleDateFormat timeFormatter = new SimpleDateFormat(INTRVW_SVC_TIME_FORMAT);
							
							// iterate the slots
							for(RequisitionScheduleTO slot : slots)
							{
								// if the slot is on the preferred date
								if((slot.getBeginTimestamp().before(endCal.getTime())) && (availSlots.size() < maxRecordsToReturn))
								{
									if(mLogger.isDebugEnabled())
									{
										mLogger.debug(String.format("Processing slot %1$s --> %2$s for phone screen %3$d", 
											slot.getBeginTimestamp().toString(), slot.getEndTimestamp().toString(), phoneScreenId));
									} // end if
									
									availDate = new IntrvwAvailDate();
									availDate.setIntrvwDt(dateFormatter.format(slot.getBeginTimestamp()));
									availDate.setBgnTm(timeFormatter.format(slot.getBeginTimestamp()));
									availDate.setEndTm(timeFormatter.format(slot.getEndTimestamp()));
									
									// TODO : Only works because we only have 30 and 60 minute durations
									/*
									 * Now we get to break the schedule transfer object into slot objects. To determine the number
									 * of slot objects we have we will subtract the end time from the begin time. Since we currently only
									 * have 30 and 60 minute interviews this will work, however, if we ever have more durations than this
									 * this will have to be revisited. 
									 */
									InterviewAvaliableSlotsTO slotToReserve = new InterviewAvaliableSlotsTO();
									slotToReserve.setBeginTimestamp(slot.getBeginTimestamp());
									slotToReserve.setCalendarId(slot.getCalendarId());
									slotToReserve.setSequenceNumber(slot.getInterviewerAvailabilityCount());
									slotToReserve.setStoreNumber(slot.getHumanResourcesSystemStoreNumber());
									slotToReserve.setReservedDateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
									// set it on the availDate
									availDate.addIntrvwAvailSlot(slotToReserve);									
									// add it to the list of slots to reserve
									slotsToReserve.add(slotToReserve);
							
									/*
									 * Only need to add a second slot if this slot spans an hour. This slot spans an hour if the
									 * endTime hour is different than the beginTime hour and the minutes are both the same
									 */
									if((slot.getEndTimestamp().getHours() != slot.getBeginTimestamp().getHours()) &&
										(slot.getEndTimestamp().getMinutes() == slot.getBeginTimestamp().getMinutes()))
									{
										// can't use the smae timestamp object here because the XStream API puts in some weird reference value if we do
										Timestamp beginTs = (Timestamp)slot.getBeginTimestamp().clone();
										// set the begin time to 30 minutes
										beginTs.setMinutes(30);
										InterviewAvaliableSlotsTO addlSlotToReserve = new InterviewAvaliableSlotsTO();
										addlSlotToReserve.setBeginTimestamp(beginTs);
										addlSlotToReserve.setCalendarId(slot.getCalendarId());
										addlSlotToReserve.setSequenceNumber(slot.getInterviewerAvailabilityCount());
										addlSlotToReserve.setStoreNumber(slot.getHumanResourcesSystemStoreNumber());
										addlSlotToReserve.setReservedDateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
										// set it on the availDate
										availDate.addIntrvwAvailSlot(addlSlotToReserve);									
										// add it to the list of slots to reserve
										slotsToReserve.add(addlSlotToReserve);
									} // end if((slot.getEndTimestamp().getHours() != slot.getBeginTimestamp().getHours()) && (slot.getEndTimestamp().getMinutes() == slot.getBeginTimestamp().getMinutes()))
									
									if(mLogger.isDebugEnabled())
									{
										mLogger.debug(String.format("Available date populated with date %1$s, begin time: %2$s, and end time: %3$s",
											availDate.getIntrvwDt(), availDate.getBgnTm(), availDate.getEndTm()));
									} // end if
									
									availSlots.add(availDate);
								} // end if((slot.getBeginTimestamp().before(endCal.getTime())) && (availSlots.size() < maxRecordsToReturn))
								else
								{
									// we've moved past the preferred date, so quit iterating
									break;
								} // end else
							} // end for(RequisitionScheduleTO slot : slots)
							
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Reserving %1$d interview slots for phone screen %2$d", slotsToReserve.size(), phoneScreenId));
							} // end if
							
							// before we can return, we have to reserve the slots we are sending back (if there are any)
							if(slotsToReserve != null && slotsToReserve.size() > 0)
							{
								reserveIntrvwSlots(slotsToReserve);							
							} // end if(slotsToReserve != null && slotsToReserve.size() > 0)
						} // end if(slots != null && slots.size() > 0)
	                } // end function handleQuery()				
				}; // end anonymous inner class
				
				// execute the query
				connectionHandler.execute();																
			} // end if(Util.isAfterToday(preferredDate))		
		} // end try
		catch(QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred retrieving available %1$s interview slots on %2$s for phone screen %3$d",
				String.valueOf(timePreference), preferredDate.toString(), phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getMultipleInterviewAvailableSlots(), phoneScreenId: %1$d, timePreference: %2$s, preferredDate: %3$s, maxRecordsToReturn: %4$d",
				phoneScreenId, timePreference, preferredDate.toString(), maxRecordsToReturn));
		} // end if
		
		return availSlots;
	} // end function getMultipleInterviewAvailableSlots()	
	
	/**
	 * This function is used to schedule an interview for a phone screen. Upon successful execution, this function
	 * will book the interview slots (provided in the schedule date object), release the unused reserved interview
	 * slots (provided in the releaseDates object), store the interview location details on the phone screen record,
	 * change the interview status of the phone screen to INTERVIEW_SCHEDULED (11) and change the interview materials
	 * status to 2
	 * 
	 * @param phoneScreenId						The phone screen that the interview should be scheduled for
	 * @param scheduleDate						Slot details for reserved interview slot(s) that should be booked
	 * @param releaseDates						Slot details for reserved interview slot(s) that should be released
	 *  
	 * @throws QueryException					Thrown if the phone screen id provided is &lt; 1, if the schedule date provided is null or invalid, if the schedule
	 * 											interview begin time is null or invalid, if the interview end time is null or invalid, if the interview slot data is
	 * 											not complete, if any of the release date objects provided are null, if any of the release date interview dates
	 * 											are null or invalid, if any of the release date interview begin times are null or invalid, if any of the release date
	 * 											interview end times are null are invalid, if any of the release date interview slot data elements provided are invalid,
	 * 											of if an exception occurs querying for data in the database OR updating data in the database
	 */
	public static void scheduleInterview(final int phoneScreenId, final IntrvwAvailDate scheduleDate, final List<IntrvwAvailDate> releaseDates) 
		throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering scheduleInterview(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
		
		try
		{		
			// validate the phone screen id provided is greater than 0
			ValidationUtils.validatePhoneScreenId(phoneScreenId);
			// validate there is a schedule date
			ValidationUtils.validateNotNull(InputField.SCHEDULE_DATE_DETAILS, scheduleDate);
			// validate the schedule date contains a validate slot date
			ValidationUtils.validateUsingRegex(InputField.INTERVIEW_DATE, scheduleDate.getIntrvwDt(), VALID_REGEX_DATE_FORMAT, false);
			// validate the schedule date contains a valid begin time
			ValidationUtils.validateUsingRegex(InputField.INTERVIEW_BEGIN_TIME, scheduleDate.getBgnTm(), VALID_REGEX_TIME_FORMAT, false);
			// validate the schedule date contains a valid end time
			ValidationUtils.validateUsingRegex(InputField.INTERVIEW_END_TIME, scheduleDate.getEndTm(), VALID_REGEX_TIME_FORMAT, false);
	
			// has to be final so i can use it in the anonymous inner class later
			final Timestamp firstSchSlotBeginTime = new Timestamp(0);
	
			// validate the schedule date has at least one slot
			if(scheduleDate.getIntrvwAvailSlots() == null || scheduleDate.getIntrvwAvailSlots().size() == 0)
			{
				throw new ValidationException(InputField.INTERVIEW_SLOT, "Schedule date did not contain any interview slots");
			} // end if(scheduleDate.getIntrvwAvailSlots() == null || scheduleDate.getIntrvwAvailSlots().size() == 0)
			
			InterviewAvaliableSlotsTO schedSlot = null;
			
			// iterate the interview slots that make up the schedule date
			for(int i = 0; i < scheduleDate.getIntrvwAvailSlots().size(); i++)
			{
				// get the slot
				schedSlot = scheduleDate.getIntrvwAvailSlots().get(i);
				// validate the slot
				ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, schedSlot);
				ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, schedSlot.getBeginTimestamp());
				ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, schedSlot.getReservedDateTime());
				ValidationUtils.validateRequisitionCalendarId(schedSlot.getCalendarId());
				ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, schedSlot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
				ValidationUtils.validateStoreNumber(schedSlot.getStoreNumber());				
				
				/*
				 * Need to capture the begin time of the first slot so we can set the interview date and time appropriately.
				 * So if this is the first slot in the loop, or if the begin timestamp of the current slot is prior
				 * to the one already there, grab it
				 */
				if(i == 0 || (schedSlot.getBeginTimestamp().before(firstSchSlotBeginTime)))
				{
					// capture this time
					firstSchSlotBeginTime.setTime(schedSlot.getBeginTimestamp().getTime());
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Setting first schedule interview slot time to %1$s for phone screen %2$d", firstSchSlotBeginTime, phoneScreenId));
					} // end if
				} // if(i == 0 || (schedSlot.getBeginTimestamp().before(firstSchSlotBeginTime)))
			} // end for(int i = 0; i < scheduleDate.getIntrvwAvailSlots().size(); i++)
			
			// if there are slots to release
			if(releaseDates != null)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug("Release dates provided");
				} // end if
				
				// iterate the release dates
				for(IntrvwAvailDate releaseDate : releaseDates)
				{
					// validate the release date is not null
					ValidationUtils.validateNotNull(InputField.RELEASE_DATE, releaseDate);
					
					// validate the interview date is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.INTERVIEW_DATE, releaseDate.getIntrvwDt(), VALID_REGEX_DATE_FORMAT, false);
					// validate the begin time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.RELEASE_DATE_BEGIN_TIME, releaseDate.getBgnTm(), VALID_REGEX_TIME_FORMAT, false);
					// validate the end time is not null or empty and matches the correct regular expression pattern
					ValidationUtils.validateUsingRegex(InputField.RELEASE_DATE_END_TIME, releaseDate.getEndTm(), VALID_REGEX_TIME_FORMAT, false);
					
					// every available interview date must have at least one slot
					if(releaseDate.getIntrvwAvailSlots() == null || releaseDate.getIntrvwAvailSlots().size() == 0)
					{
						throw new ValidationException(InputField.INTERVIEW_SLOT, String.format("Release date %1$s did not contain any interview slots", releaseDate.getIntrvwDt()));
					} // end if(releaseDate.getIntrvwAvailSlots() == null || releaseDate.getIntrvwAvailSlots().size() == 0)
					
					// iterate the interview slots that make up this interview date
					for(InterviewAvaliableSlotsTO slot : releaseDate.getIntrvwAvailSlots())
					{
						// validate the slot
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
						ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
						ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
						ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
						ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
						ValidationUtils.validateStoreNumber(slot.getStoreNumber());							
					} // end for(InterviewAvaliableSlotsTO slot : releaseDate.getIntrvwAvailSlots())					
				} // end for(IntrvwAvailDate releaseDate : releaseDates)							
			} // end if(releaseDates != null)
			
			// get an instance of the QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get the DAO connection from the QueryManager
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
			
			// create a new transacted UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(true, null, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
	            public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
	            {		
					// get the DAO connection from the connection list
					DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
	            	
	            	// first get the phone screen record
	            	PhoneScreenIntrwDetailsTO phoneScreen = null;
	            	List<PhoneScreenIntrwDetailsTO> phoneScreenList = null;
	            	
	            	// first query to get the phone screen
	            	try
	            	{
	            		if(mLogger.isDebugEnabled())
	            		{
	            			mLogger.debug(String.format("Querying for details about phone screen %1$d", phoneScreenId));
	            		} // end if
	            		
	            		// TODO : This should only ever return a single row, why is it returning a list?
	            		phoneScreenList = PhoneScreenDAO.readHumanResourcesPhoneScreen(phoneScreenId);
	            		
	            		// if the phone screen was not found
	            		if(phoneScreenList == null || phoneScreenList.size() == 0)
	            		{
	            			throw new QueryException(String.format("Phone screen %1$d not found", phoneScreenId));
	            		} // end if(phoneScreenList == null || phoneScreenList.size() == 0)
	            		
	            		// get the phone screen from the list
	            		phoneScreen = phoneScreenList.get(0);
	            		
	            		// make sure the phone screen is not null
	            		if(phoneScreen == null)
	            		{
	            			throw new QueryException(String.format("Null phone screen returned for phone screen id %1$d", phoneScreenId));
	            		} // end if(phoneScreen == null)
	            	} // end try
	            	catch(RetailStaffingException rse)
	            	{
	            		// log the root details
	            		mLogger.error(String.format("An exception occurred querying for phone screen %1$d", phoneScreenId), rse);
	            		// throw the exception
	            		throw new QueryException(rse.getMessage());
	            	} // end catch
	            	
	            	// next query to get the location details for the interview store
	            	// get the store number from the first interview slot on the schedule date
            		String storeNumber = scheduleDate.getIntrvwAvailSlots().get(0).getStoreNumber();
	            		
            		if(mLogger.isDebugEnabled())
            		{
            			mLogger.debug(String.format("Querying for store details for store %1$s", storeNumber));
            		} // end if
	            		
            		StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(storeNumber);
	            	                	
	            	// next schedule the interview
	            	if(mLogger.isDebugEnabled())
	            	{
	            		mLogger.debug(String.format("Scheduling reserved interview slots on %1$s @ %2$s for phone screen %3$s", scheduleDate.getIntrvwDt(), scheduleDate.getBgnTm(), phoneScreenId));
	            	} // end if        
	            	// invoke the logic to schedule the slots
	            	scheduleReservedIntrvwSlots(phoneScreenId, scheduleDate.getIntrvwAvailSlots());
	            	
	            	// setup the interview location details on the phone screen
	            	IntrwLocationDetailsTO intrvwLoc = new IntrwLocationDetailsTO();
	            	
	            	/*
	            	 * If the requisition store number matches the store number for the interview, set
	            	 * the "location id" really the type code to 1 (same store), otherwise, set the type
	            	 * code to 2 (other store)
	            	 */
	            	if(phoneScreen.getCndStrNbr() != null && store.getStrNum() != null && phoneScreen.getCndStrNbr().trim().equals(store.getStrNum().trim()))
	            	{
	            		if(mLogger.isDebugEnabled())
	            		{
	            			mLogger.debug(String.format("Interview for phone screen %1$d is in the same location as the requisition, setting the location type to %2$s",
	            				phoneScreenId, HIRING_STR_TYP_CD));
	            		} // end if
	            		
	            		intrvwLoc.setInterviewLocId(RetailStaffingConstants.HIRING_STR_TYP_CD);
	            	} // end if(phoneScreen.getCndStrNbr() != null && store.getStrNum() != null && phoneScreen.getCndStrNbr().trim().equals(store.getStrNum().trim()))
	            	else
	            	{
	            		if(mLogger.isDebugEnabled())
	            		{
	            			mLogger.debug(String.format("Interview for phone screen %1$d IS NOT in the same location as the requisition, setting the location type to %2$s",
	            				phoneScreenId, NON_HIRING_STR_TYP_CD));
	            		} // end if	            	
	            		
	            		intrvwLoc.setInterviewLocId(RetailStaffingConstants.NON_HIRING_STR_TYP_CD);
	            	} // end else
	            	
	            	intrvwLoc.setInterviewLocName(store.getStrName());
	            	intrvwLoc.setAdd(store.getAdd());
	            	intrvwLoc.setCity(store.getCity());
	            	intrvwLoc.setState(store.getState());
	            	intrvwLoc.setZip(store.getZip());
	            	intrvwLoc.setPhone(store.getPhone());
	            	// set the interview date and time
	            	intrvwLoc.setIntrvwDtTm(firstSchSlotBeginTime);
	            	intrvwLoc.setInterviewTime(Util.converTimeStampTimeStampTO(firstSchSlotBeginTime));
	            	intrvwLoc.setInterviewDate(Util.converDatetoDateTO(new java.sql.Date(firstSchSlotBeginTime.getTime())));                	
	            	// add the details to the phone screen
	            	phoneScreen.setIntrLocDtls(intrvwLoc);
	            	
	            	// set the phone screen candidate status to yes
	            	phoneScreen.setCanStatus(YES);
	            	
	            	// invoke the logic to update the phone screen                	
	            	try
	            	{
	                	if(mLogger.isDebugEnabled())
	                	{
	                		mLogger.debug(String.format("Setting interview details (date/time and location) on phone screen %1$d", phoneScreenId));
	                	} // end if
	            		PhoneScreenManager.updateHumanResourcesPhoneScreen(phoneScreen);
	
	            		//Removed for IVR Project April 2016 because Nuance is updating the Interview Status as well and if we do it here it puts 2 updates in the History table. 
	            		/*if(mLogger.isDebugEnabled())
	            		{
	            			mLogger.debug(String.format("Changing interview status of phone screen %1$d to %2$d", phoneScreenId, INTERVIEW_SCHEDULED));
	            		} // end if
	            		// now update the interview status of the phone screen
	            		PhoneScreenManager.updateInterviewStatusForCTI(phoneScreenId, INTERVIEW_SCHEDULED);*/
	            	} // end try
	            	catch(RetailStaffingException rse)
	            	{
	            		// rollback the update
	            		connection.getControls().rollback();
	            		
	            		// log the root details
	            		mLogger.error(String.format("An exception occurred updating phone screen %1$d", phoneScreenId), rse);
	            		// throw the exception
	            		throw new QueryException(rse.getMessage());   
	            	} // end catch
	            	
	            	// next if interview slots were provided for release, release them
	            	if(releaseDates != null && releaseDates.size() > 0)
	            	{
	            		List<InterviewAvaliableSlotsTO> toRelease = new ArrayList<InterviewAvaliableSlotsTO>();
	            		
	            		// iterate and add all the slots that make up the release dates so they can be released at once
	            		for(IntrvwAvailDate releaseDate : releaseDates)
	            		{
	            			if(mLogger.isDebugEnabled())
	            			{
	            				mLogger.debug(String.format("Adding release slot details on %1$s @ %2$s for phone screen %3$s", releaseDate.getIntrvwDt(), releaseDate.getBgnTm(), phoneScreenId));
	            			} // end if                			
	            			toRelease.addAll(releaseDate.getIntrvwAvailSlots());
	            		} // end for(IntrvwAvailDate releaseDate : releaseDates)
	            		
	            		// invoke the logic to release the slots
	            		releaseIntrvwSlots(toRelease);
	            		if(mLogger.isDebugEnabled())
	            		{
	            			mLogger.debug(String.format("Successfully released %1$d unused interview slots for phone screen %2$d", toRelease.size(), phoneScreenId));
	            		} // end if
	            	} // end if(releaseDates != null && releaseDates.size() > 0)
	            } // end function handleQuery()				
			}; // end anonymous inner class
			
			// execute the query
			connectionHandler.execute();	               	
		} // end try
		catch(QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred scheduling interview for phone screen %1$d", phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting scheduleInterview(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
	} // end function scheduleInterview()	
	
	/**
	 * This method is used to get available interview slots for the phone screen provided
	 * 
	 * @param phoneScreenId					The phone screen to get interview slots for
	 * @param beginTime						Minimum interview slot begin time (in range)
	 * 
	 * @return 								List of available interview slots for the phone screen id
	 *         								provided that are after the begin time provided
	 * 
	 * @throws QueryException				Thrown if :
	 *										<ul>
	 *											<li>The phone screen id provided is &lt;= 0</li>
	 *											<li>The begin time provided is null</li>
	 *											<li>The begin time provided is prior to today</li>
	 *											<li>The phone screen provided is not in SCHEDULE INTERVIEW status (9)</li>
	 *											<li>The requisition calendar id on the phone screen provided is &lt;= 0</li>
	 *											<li>The phone screen id provided is for a requisition that is part of a hiring event that is not of type SSE</li>
	 *											<li>The phone screen id provided is for a requisition that is NOT being schedule by the RSC (Store Scheduling)</li>
	 *											<li>The phone screen id provided is for a requisition that has a null duration</li>
	 *											<li>The phone screen id provided is for a requisition that has a duration &lt; 0</li>
	 *											<li>The phone screen id provided is on a requisition that is not active</li>
	 *											<li>The phone screen id provided is for a candidate that is not active</li>
	 *											<li>The phone screen id provided could not be found</li>
	 *											<li>An exception occurs querying the database for interview scheduling key data for the phone screen id provided</li>
	 *											<li>An exception occurs querying the database for available interview slots for the phone screen id provided</li>
	 *										</ul>
	 */
	private static List<RequisitionScheduleTO> getAvailIntrvwSlotsForPhoneScreen(final int phoneScreenId, final Timestamp beginTime) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getAvailIntrvwSlotsForPhoneScreen(), phoneScreenId: %1$d, beginTime: %2$s", phoneScreenId,
			    (beginTime == null ? "null" : beginTime.toString())));
		} // end if

		/*
		 * Unfortunately, to use this within an anonymous inner class it has to be declared final
		 * (even though it may not be used)
		 */
		final List<RequisitionScheduleTO> availSlots = new ArrayList<RequisitionScheduleTO>();

		try
		{
			// validate the phone screen id provided is not < 0
			ValidationUtils.validatePhoneScreenId(phoneScreenId);

			// validate the begin time is not null or empty
			ValidationUtils.validateNotNull(InputField.BEGIN_TIMESTAMP, beginTime);

			// create a calendar object with the latest possible time for today
			Calendar today = Calendar.getInstance();

			// validate the begin timestamp is after current time
			if(beginTime.before(today.getTime()))
			{
				throw new ValidationException(InputField.BEGIN_TIMESTAMP, String.format("Invalid beginTime %1$s provided, beginTime cannot be prior to %2$s", 
					beginTime.toString(), today.getTime().toString()));
			} // end if(beginTime.before(today.getTime()))

			// get an instance of the QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get the DAO connection from the QueryManager
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);

			// create an instance of the handler that will be used to process the results from this query
			ScheduleHandler handler = new ScheduleHandler();

			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, handler, connection)
			{
				/*
				 * (non-Javadoc)
				 * 
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					// get the DAO connection from the connection list
					DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					// get the DAO query object from the connection
					Query query = connection.getQuery();

					// cast the handler to the right type
					ScheduleHandler schedHandler = (ScheduleHandler)handler;

					// initialize the query parameters required to execute the first query
					MapStream inputs = new MapStream(READ_INTERVIEW_DETAILS_FOR_PHONE_SCREEN);
					inputs.put(HR_PHN_SCRN_ID, phoneScreenId);

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying for interview details using phone screen id %1$d", phoneScreenId));
					} // end if

					// execute the query
					query.getResult(connection, schedHandler, inputs);

					// grab the interview scheduling details object that would be populated if a record was found
					IntrvwScheduleDetails details = schedHandler.getSchedDetails();

					// if no records were found
					if(details == null)
					{
						throw new QueryException(String.format("No interview scheduling details found for phone screen id %1$d", phoneScreenId));
					} // end if(details == null)

					// validate the phone screen has an interview status of schedule interview or unable to schedule
					// For IVR enhancements Mar/Apr 2016 added PENDING_SCHEDULING, PENDING_SCHEDULE_OFFER_INTV, PENDING_SCHEDULE_CALENDAR, INTERVIEW_LEFT_MESSAGE, INTERVIEW_INVALID_NUMBER
					if((details.getIntrvwStatCd() != SCHEDULE_INTERVIEW) && (details.getIntrvwStatCd() != UNABLE_TO_SCHEDULE) && (details.getIntrvwStatCd() != PENDING_SCHEDULING) 
							&& (details.getIntrvwStatCd() != PENDING_SCHEDULE_OFFER_INTV) && (details.getIntrvwStatCd() != PENDING_SCHEDULE_CALENDAR)
							&& (details.getIntrvwStatCd() != INTERVIEW_LEFT_MESSAGE) && (details.getIntrvwStatCd() != INTERVIEW_INVALID_NUMBER))
					{
						throw new QueryException(String.format("Phone screen %1$d does not have a valid interview status. Valid interview statuses are \"Schedule Interview\" and \"Unable to Schedule\"." + 
			                                " and \"Pending Scheduling\" and \"Pending Schedule-Offer/Interview\" and \"Pending Schedule-Calendar\" and \"Left Message\" and \"Invalid Number \"", phoneScreenId));
					} // end if((details.getIntrvwStatCd() != SCHEDULE_INTERVIEW) && (details.getIntrvwStatCd() != UNABLE_TO_SCHEDULE))

					// validate the calendar id for this phone screen is > 0
					if(details.getReqCalId() < 1)
					{
						throw new QueryException(String.format("Phone screen %1$d contained an invalid calendar id %2$d", phoneScreenId, details.getReqCalId()));
					} // end if(details.getReqCalId() < 1)

					//This changed for ATS Hiring Events*********
					// validate the requisition this phone screen is for is not part of a hiring event
					if(details.getHireEvntFlg() != null && details.getHireEvntFlg().equalsIgnoreCase(YES))
					{
						//Make sure that it is a Single Store Event, if not throw the exception
						if (details.getEventType() == null || !details.getEventType().equals("SSE"))
						{
							throw new QueryException(String.format("Phone screen %1$d is for a requisition that was part of a hiring event", phoneScreenId));
						} // end if (details.getEventType() == null || !details.getEventType().equals("SSE"))
					} // end if(details.getHireEvntFlg() != null && details.getHireEvntFlg().equalsIgnoreCase(YES))
					//******************************************
					
					
					// validate the requisition is flagged for RSC scheduling
					if(details.getRscSchFlg() != null && details.getRscSchFlg().equalsIgnoreCase(NO))
					{
						throw new QueryException(String.format("Phone screen %1$d is on a requisition that is flagged for store scheduling", phoneScreenId));
					} // end if(details.getRscSchFlg() != null && details.getRscSchFlg().equalsIgnoreCase(NO))

					// validate the duration is > 0
					if(details.getIntrvwMins() < 1)
					{
						throw new QueryException(String.format("Phone screen %1$d contained an invalid interview duration: %2$d", phoneScreenId, details.getIntrvwMins()));
					} // end if(details.getIntrvwMins() < 1)

					// validate the requisition is active
					if(details.getReqActvFlg() == null || details.getReqActvFlg().equalsIgnoreCase(NO))
					{
						throw new QueryException(String.format("Phone screen %1$d is for an inactive requisition", phoneScreenId));
					} // end if(details.getReqActvFlg() == null || details.getReqActvFlg().equalsIgnoreCase(NO))

					// validate the candidate is active
					if(details.getCandActvFlg() == null || details.getCandActvFlg().equalsIgnoreCase(NO))
					{
						throw new QueryException(String.format("Phone screen %1$d contains an inactive candidate", phoneScreenId));
					} // end if(details.getCandActvFlg() == null || details.getCandActvFlg().equalsIgnoreCase(NO))
					
					// next we need to check if this phone screen is on a MET calendar (check the type indicator)
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to determine if phone screen %1$d is on a MET calendar (using calendar id %2$d)", phoneScreenId, details.getReqCalId()));
					} // end if
					
					// initialize the query parameters required to execute the next query
					inputs.clear();
					inputs.setSelectorName(READ_REQUISITION_CALENDAR);
					inputs.put(REQN_CAL_ID, details.getReqCalId());

					// execute the query
					query.getResult(connection, schedHandler, inputs);
					
					// get the requisition calendar
					RequisitionCalendarTO reqCal = schedHandler.getCalendar();
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Checking calendar %1$s for phone screen %2$d to see if it's a MET calendar", 
							reqCal.getRequisitionCalendarDescription(), phoneScreenId));
					} // end if
					
					/*
					 * No null check here, if the calendar was not found the handler would have thrown an exception.
					 * Check the calendar object returned to see if the type indicates it's a MET calendar. If it is 
					 * a MET calendar, we will not return any availability.
					 */
					if(reqCal.getType() != null && reqCal.getType() != REQN_CAL_TYP_MET)
					{
						try
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Phone screen %1$d IS NOT on a MET calendar", phoneScreenId));
								mLogger.debug(String.format("Querying for available interview slots for phone screen %1$d using calendar id %2$d and begin time %3$s", phoneScreenId,
								    details.getReqCalId(), beginTime.toString()));
							} // end if
	
							// if we passed all the validations, invoke the logic to get available interview slots
							List<RequisitionScheduleTO> slots = getAvailableInterviewSlots(details.getRscSchFlg(), details.getReqCalId(), beginTime, details.getIntrvwMins());
	
							// if slots were returned
							if(slots != null)
							{
								// add them all to the return list
								availSlots.addAll(slots);
							} // end if(slots != null)
						} // end try
						catch (RetailStaffingException rse)
						{
							// make sure the error gets logged
							mLogger.error(String.format("An exception occurred getting available interview slots for phone screen %1$d", phoneScreenId), rse);
							throw new QueryException(rse.getMessage());
						} // end catch()	
					} // end if(reqCal.getType() != null && reqCal.getType() != REQN_CAL_TYP_MET)
				} // end function handleQuery()
			}; // end anonymous inner class

			// execute the query
			connectionHandler.execute();
		} // end try
		catch (QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred retrieving available interview dates for phone screen id %1$s", phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getAvailIntrvwSltosForPhoneScreen(), phoneScreenId: %1$d, beginTime: %2$s. Total available slots found: %3$d",
			    phoneScreenId, beginTime.toString(), availSlots.size()));
		} // end if

		return availSlots;
	} // end function getAvaliableIntrvwSlotsForPhoneScreen()	
	
	/**
	 * Convenience method to reserve the list of interview slots provided
	 * 
	 * @param slots							List of interview slots to reserve
	 * 
	 * @throws QueryException				
	 *						<ul>
	 *							<li>No interview slots are provided</li>
	 *							<li>A null interview slot is provided</li>
	 *							<li>a null interview slot begin time is provided</li>
	 *							<li>an interview slot with a calendar id &lt; 1 is provided</li>
	 *							<li>an interview slot with a sequence number &lt; 1 is provided</li>
	 *							<li>An interview slot with a null or empty store number is provided</li>
	 *							<li>Any exceptions occur updating the interview slots provided</li>
	 *						</ul>

	 */
	public static void reserveIntrvwSlots(final List<InterviewAvaliableSlotsTO> slots) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Entering reserveIntrvwSlots()");
		} // end if

		// make sure we got slots to reserve
		if(slots == null || slots.isEmpty())
		{
			throw new ValidationException(InputField.INTERVIEW_SLOT, String.format("%1$s interview slots list provided", (slots == null ? "Null" : "Empty")));
		} // end if(slots == null || slots.isEmpty())
		
		// iterate the slots and validate them
		for(InterviewAvaliableSlotsTO slot : slots)
		{
			ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
			ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
			ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
			ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
			ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
			ValidationUtils.validateStoreNumber(slot.getStoreNumber());
		} // end for(InterviewAvaliableSlotsTO slot : slots)

		// get an instance of the QueryManager
		QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
		// get the DAO connection from the QueryManager
		DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);

		// create a new transacted UniversalConnectionHandler that will be used
		// to execute all the queries needed using a single database connection
		UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(true, null, connection)
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see	com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
			 */
			public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
			{
				// get the DAO connection from the connection list
				DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);

				/*
				 * since we can only reserve slots that are not reserved, scheduled or canceled,
				 * generate a list of slots that contains these three statuses
				 */
				List<Short> codesCannotReserve = new ArrayList<Short>();
				codesCannotReserve.add(RESERVED_STATUS_CODE);
				codesCannotReserve.add(SCHEDULED_STATUS_CODE);
				codesCannotReserve.add(CANCELLED_STATUS_CODE);

				try
				{
					// iterate the interview slots that we want to reserve
					for(InterviewAvaliableSlotsTO slot : slots)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Reserving interview slot for store %1$s with calendar id %2$d and sequence number %3$d and begin time %4$s",
							    slot.getStoreNumber(), slot.getCalendarId(), slot.getSequenceNumber(), slot.getBeginTimestamp()));
						} // end if
	
						/*
						 * invoke the DAO method to "reserve" the slot. Since this is an update
						 * if no rows were updated an UpdateException will be thrown and rollback any
						 * updates that have occurred
						 */
						updateInterviewSlot(RESERVED_STATUS_CODE, slot.getReservedDateTime(), slot.getCalendarId(), slot.getBeginTimestamp(), slot.getSequenceNumber(), 
							codesCannotReserve, null);
							
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Reserved interview slot for store %1$s with calendar id %2$d and sequence number %3$d and begin time %4$s", slot.getStoreNumber(),
							    slot.getCalendarId(), slot.getSequenceNumber(), slot.getBeginTimestamp()));
						} // end if						
					} // end for(InterviewAvaliableSlotsTO slot : slots)
				} // end try
				catch(UpdateException ue)
				{
					// force a rollback
					connection.getControls().rollback();
					throw ue;
				} // end catch
			} // end function handleQuery()
		}; // end anonymous inner class

		// execute the query
		connectionHandler.execute();

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exiting reserveIntrvwSlots()");
		} // end if
	} // end function reserveIntrvwSlots()

	/**
	 * Convenience method to release the list of reserved interview slots
	 * provided
	 * 
	 * @param slots							List of reserved interview slots to release
	 * 
	 * @throws QueryException				Thrown if:
	 * 	 						<ul>
	 *								<li>No interview slots are provided</li>
	 *								<li>A null interview slot is provided</li>
	 *								<li>a null interview slot begin time is provided</li>
	 *								<li>an interview slot with a calendar id &lt; 1 is provided</li>
	 *								<li>an interview slot with a sequence number &lt; 1 is provided</li>
	 *								<li>An interview slot with a null or empty store number is provided</li>
	 *								<li>Any exceptions occur updating the interview slots provided</li>
	 *							</ul>			
	 */
	public static void releaseIntrvwSlots(final List<InterviewAvaliableSlotsTO> slots) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Entering releaseIntrvwSlots()");
		} // end if

		// make sure we got slots to reserve
		if(slots == null || slots.isEmpty())
		{
			throw new ValidationException(InputField.INTERVIEW_SLOT, String.format("%1$s interview slots list provided", (slots == null ? "Null" : "Empty")));
		} // end if(slots == null || slots.isEmpty())
		
		// iterate the slots and validate them
		for(InterviewAvaliableSlotsTO slot : slots)
		{
			ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
			ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
			ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
			ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
			ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
			ValidationUtils.validateStoreNumber(slot.getStoreNumber());
		} // end for(InterviewAvaliableSlotsTO slot : slots)

		// get an instance of the QueryManager
		QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
		// get the DAO connection from the QueryManager
		DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);

		// create a new transacted UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
		UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(true, null, connection)
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see	com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
			 */
			public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
			{
				// we can only release slots that are reserved, so we need to ignore all the other statuses
				List<Short> codesCannotReserve = new ArrayList<Short>();
				codesCannotReserve.add(AVALIABLE_STATUS_CODE);
				codesCannotReserve.add(SCHEDULED_STATUS_CODE);
				codesCannotReserve.add(CANCELLED_STATUS_CODE);

				// iterate the interview slots that we want to release
				for(InterviewAvaliableSlotsTO slot : slots)
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Releasing interview slot for store %1$s with calendar id %2$d and sequence number %3$d and begin time %4$s",
						    slot.getStoreNumber(), slot.getCalendarId(), slot.getSequenceNumber(), slot.getBeginTimestamp()));
					} // end if

					/*
					 * invoke the DAO method to "release" the slot. Since this is an update, if no rows were updated
					 * an UpdateException will be thrown and rollback any updates that have occurred
					 */
					try
					{
						updateInterviewSlot(AVALIABLE_STATUS_CODE, null, slot.getCalendarId(), slot.getBeginTimestamp(), slot.getSequenceNumber(), codesCannotReserve, null);
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Released interview slot for store %1$s with calendar id %2$d and sequence number %3$d and begin time %4$s", slot.getStoreNumber(),
							    slot.getCalendarId(), slot.getSequenceNumber(), slot.getBeginTimestamp()));
						} // end if
					} // end try
					catch(UpdateException ue)
					{
						// not going to rollback here, it's better to release some than not to release any
						mLogger.error(String.format("An exception occurred releasing interview slot for store %1$s with calendar id %2$d and sequence number %3$d and begin time %4$s",
							slot.getStoreNumber(), slot.getCalendarId(), slot.getSequenceNumber(), slot.getBeginTimestamp()));
					} // end catch
				} // end for(InterviewAvaliableSlotsTO slot : slots)
			} // end function handleQuery()
		}; // end anonymous inner class

		// execute the query
		connectionHandler.execute();

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exiting releaseIntrvwSlots()");
		} // end if
	} // end function releaseIntrvwSlots()

	/**
	 * Convenience method to schedule the list of reserved interview slots provided
	 * 
	 * @param slots							List of interview slots to "book"
	 * 
	 * @throws QueryException				Thrown if:
	 * 	 						<ul>
	 *								<li>No interview slots are provided</li>
	 *								<li>The phone screen id provided is &lt; 1</li>
	 *								<li>A null interview slot is provided</li>
	 *								<li>a null interview slot begin time is provided</li>
	 *								<li>an interview slot with a calendar id &lt; 1 is provided</li>
	 *								<li>an interview slot with a sequence number &lt; 1 is provided</li>
	 *								<li>An interview slot with a null or empty store number is provided</li>
	 *								<li>Any exceptions occur updating the interview slots provided</li>
	 *							</ul>	
	 */
	public static void scheduleReservedIntrvwSlots(final int phoneScreenId, final List<InterviewAvaliableSlotsTO> slots) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Entering scheduleReservedIntrvwSlots()");
		} // end if

		// make sure we got slots to reserve
		if(slots == null || slots.isEmpty())
		{
			throw new ValidationException(InputField.INTERVIEW_SLOT, String.format("%1$s interview slots list provided", (slots == null ? "Null" : "Empty")));
		} // end if(slots == null || slots.isEmpty())
		
		// validate the phone screen id provided
		ValidationUtils.validatePhoneScreenId(phoneScreenId);
		
		// iterate the slots and validate them
		for(InterviewAvaliableSlotsTO slot : slots)
		{
			ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
			ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT_BEGIN_TIME, slot.getBeginTimestamp());
			ValidationUtils.validateNotNull(InputField.RESERVATION_DATETIME, slot.getReservedDateTime());
			ValidationUtils.validateRequisitionCalendarId(slot.getCalendarId());
			ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, slot.getSequenceNumber(), MIN_SEQUENCE_NUMBER);
			ValidationUtils.validateStoreNumber(slot.getStoreNumber());
		} // end for(InterviewAvaliableSlotsTO slot : slots)

		// get an instance of the QueryManager
		QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
		// get the DAO connection from the QueryManager
		DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);

		/*
		 * create a new transacted UniversalConnectionHandler that will be used to execute
		 * all the queries needed using a single database connection
		 */
		UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(true, null, connection)
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see		com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
			 */
			public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
			{
				// get the DAO connection from the connection list
				DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);

				// in this method we can only schedule slots that are reserved, so we need to ignore all the other statuses
				List<Short> codesCannotReserve = new ArrayList<Short>();
				codesCannotReserve.add(AVALIABLE_STATUS_CODE);
				codesCannotReserve.add(SCHEDULED_STATUS_CODE);
				codesCannotReserve.add(CANCELLED_STATUS_CODE);
				
				try
				{
					// iterate the interview slots that we want to reserve
					for(InterviewAvaliableSlotsTO slot : slots)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Booking interview slot for store %1$s with calendar id %2$d and sequence number %3$d and begin time %4$s",
							    slot.getStoreNumber(), slot.getCalendarId(), slot.getSequenceNumber(), slot.getBeginTimestamp()));
						} // end if
	
						/*
						 * invoke the DAO method to "book" the slot. Since this is an update, an UpdateException will be 
						 * throw if now rows were updated and all updates that have already occurred will be rolled back
						 */
						updateInterviewSlot(SCHEDULED_STATUS_CODE, null, slot.getCalendarId(), slot.getBeginTimestamp(), slot.getSequenceNumber(), codesCannotReserve, phoneScreenId);
	
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Booked interview slot for store %1$s with calendar id %2$d and sequence number %3$d and begin time %4$s",
							    slot.getStoreNumber(), slot.getCalendarId(), slot.getSequenceNumber(), slot.getBeginTimestamp()));
						} // end if
					} // end for(InterviewAvaliableSlotsTO slot : slots)
				} // end try
				catch(UpdateException ue)
				{
					// force a rollback
					connection.getControls().rollback();
					throw ue;
				} // end catch					
			} // end function handleQuery()
		}; // end anonymous inner class

		// execute the query
		connectionHandler.execute();

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exiting scheduleReservedIntrvwSlots()");
		} // end if
	} // end function scheduleReservedIntrvwSlots()
	
	/**
	 * This method is used to update interview slot info (status, time, phone screen id, etc.)
	 * 
	 * @param statusCode new interview slot status
	 * @param reserveTime new interview reserve time
	 * @param calendarId interview slot calendar id (part of the key)
	 * @param beginTime interview slot begin time (part of the key)
	 * @param seqNo interview slot sequence number (part of the key)
	 * @param scheduleCodeList list of status the current block cannot be in
	 * @param phoneScreenId phone screen id (that is reserving or booking a slot)
	 * 
	 * @throws QueryException thrown if the status code provided is &lt; 1, if the calendar id provided is &lt; 1,
	 * 						  if the begin timestamp provided is null, if the sequence number provided is &lt; 1,
	 * 						  if the phone screen id provided is &lt; 1 or if an exception occurs querying the database
	 */
	public static void updateInterviewSlot(final short statusCode, final Timestamp reserveTime, final int calendarId, final Timestamp beginTime, 
		final short seqNo, List<Short> scheduleCodeList, Integer phoneScreenId) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updateInterviewSlot(), statusCode: %1$s, reserveTime: %2$s, calendarId: %3$d, beginTime: %4$s, seqNo: %5$d, phoneScreenId: %6$s",
				statusCode, reserveTime, calendarId, beginTime, seqNo, phoneScreenId));
		} // end if
		
		try
		{
			// validate the status code provided is > 0 (should really be hitting the DB here but the code that calls this all use constants that are valid)
			ValidationUtils.validateStatusCode(statusCode);
			// validate the calendar id provided
			ValidationUtils.validateRequisitionCalendarId(calendarId);
			// validate the begin time provided is not null
			ValidationUtils.validateNotNull(InputField.BEGIN_TIMESTAMP, beginTime);
			// validate the sequence number provided is > 0
			ValidationUtils.validateGreaterThan(InputField.INTERVIEW_SLOT_SEQUENCE, seqNo, MIN_SEQUENCE_NUMBER);
			
			// if a phone screen id is provided, need to validate it
			if(phoneScreenId != null)
			{
				ValidationUtils.validatePhoneScreenId(phoneScreenId);
			} // end if(phoneScreenId != null)
						
			// prepare the query
			MapStream inputs = new MapStream(UPD_HR_REQN_SCH_RESERVED_TIME);
			inputs.putAllowNull(NEW_REQN_SCH_STAT_CD, statusCode);
			inputs.putAllowNull(NEW_REQN_SCH_RSRV_TS, reserveTime);
			inputs.put(REQN_CAL_ID, calendarId);
			inputs.put(BGN_TS, beginTime);
			inputs.put(SEQ_NBR, seqNo);
			inputs.putAllowNull(REQN_SCH_STAT_CD_NOT_IN, scheduleCodeList);
			inputs.putAllowNull(HR_PHN_SCRN_ID, phoneScreenId);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Updating interview slot status for calendar %1$d, beginTime: %2$s and seqNo: %3$d", calendarId, beginTime, seqNo));
			} // end if
			
			// execute the query
			BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier()
			{		
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.UpdateNotifier#notifyUpdate(java.lang.Object, boolean, int, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
				 */
				public void notifyUpdate(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
				{
					/*
					 * Only a single row should've been updated, if more than one was updated throw an exception. The DAO
					 * infrastructure will throw an UpdateException if no rows were updated.
					 */
					if(count != 1)
					{
						throw new UpdateException(String.format("An exception occurred changing interview slot status to %1$d for calendar %2$d, begin time: %3$s and seqNo: %4$d. %5$d rows updated",
							statusCode, calendarId, beginTime, seqNo, count));
					} // end if(count != 1)
				} // end function notifyUpdate()
			}); // end function updateObject()
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred updating interview slot %1$s for calendar %2$d", beginTime, calendarId), qe);
			// throw the exception
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting updateInterviewSlotStatus() statusCode : %1$s,  reserveTime : %2$s, calendarId : %3$s, beginTime : %4$s, seqNumber : %5$s. Total time to execute update %6$.9f seconds", 
				statusCode, reserveTime, calendarId, beginTime, seqNo, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end function updateInterviewSlotStatus()
	
	/**
	 * This method gets an active requisition calendar (HR_REQN_SCH) for the calendar id provided
	 * 
	 * @param reqCalId the calendar ID to get requisition calendar for
	 *                      
	 * @return requisition calendars for the Calendar Id
	 * 
	 * @throws QueryException thrown if the calendar Id provided is not valid (in the form of a ValidationException)
	 *                        or if an exception occurs querying the database
	 */	
	public static RequisitionCalendarTO getRequisitionCalendarForReqCalId(final int reqCalId) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getRequisitionCalendarForReqCalId(), reqCalId: %1$s", reqCalId));
		} // end if
		
		// create a handler object that will be used to process the results of the query
		ScheduleHandler resultsHandler = new ScheduleHandler();
		
		try
		{
			// validate the Calendar Id is at least 0
			ValidationUtils.validateRequisitionCalendarId(reqCalId);
			
			// get the query manager for the DAO contract
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// use the query manager to get a DAO connection
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
					ScheduleHandler resultsHandler = (ScheduleHandler)handler;
										
					// next prepare the query to get the calendars for the store
					MapStream inputs = new MapStream(READ_REQUISITION_CALENDAR);
					inputs.put(REQN_CAL_ID, reqCalId);
					inputs.put(ACTV_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to get active requisition calendar for Calendar Id %1$s", reqCalId));
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
			mLogger.debug(String.format("Exiting getRequisitionCalendarForReqCalId(), reqCalId: %1$s. Returning requisition calendar in %2$.9f seconds",
				reqCalId, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return resultsHandler.getCalendar();
	} // end function getRequisitionCalendarsForStore()	
	
	/**
	 * This method gets a list of active requisition hiring events (HR_REQN_SCH) for the store number provided
	 * 
	 * @param storeNbr the store number to get requisition hiring event for
	 *                      
	 * @return              list of requisition hiring events for the store number
	 * 
	 * @throws QueryException thrown if the store number provided is not valid (in the form of a ValidationException)
	 *                        or if an exception occurs querying the database
	 */
	public static List<RequisitionCalendarTO> getRequisitionHiringEventsForStore(final String storeNbr) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getRequisitionHiringEventsForStore(), storeNumber: %1$s", storeNbr));
		} // end if
		
		// create a handler object that will be used to process the results of the query
		ScheduleHandler resultsHandler = new ScheduleHandler();
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateStoreNumber(storeNbr);
			
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
					ScheduleHandler resultsHandler = (ScheduleHandler)handler;
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to validate store number %1$s", storeNbr));
					} // end if					
					// first validate the location number provided is valid
					if(!LocationManager.isValidStoreNumber(storeNbr))
					{
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNbr));
					} // end if(!LocationManager.isValidStoreNumber(storeNbr))
					
					// next prepare the query to get the hiring events for the store
					MapStream inputs = new MapStream(READ_REQUISITION_HIRING_EVENTS);
					inputs.put(HR_SYS_STR_NBR, storeNbr);
					inputs.put(ACTV_FLG, true);
					inputs.put(STORE_ACTIVE_FLG, true);
					inputs.put(HR_CAL_TYP_CD, HIRING_EVENT_TYPE_CD);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to get active requisition hiring events for store %1$s", storeNbr));
					} // end if
					query.getResult(connection, resultsHandler, inputs);
					
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting hiring events for store %1$s", storeNbr), qe);
			// throw the exception
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getRequisitionHiringEventsForStore(), storeNumber: %1$s. Returning %2$d requisition hiring events in %3$.9f seconds",
				storeNbr, (resultsHandler.getCalendars() == null ? 0 : resultsHandler.getCalendars().size()), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return resultsHandler.getCalendars();
	} // end function getRequisitionHiringEventsForStore()
	
	/**
	 * This method gets a list of active requisition hiring events (HR_REQN_SCH) for the store number provided and the count data
	 * 
	 * @param storeNbr the store number to get requisition hiring event for
	 *                      
	 * @return              list of requisition hiring events for the store number
	 * 
	 * @throws QueryException thrown if the store number provided is not valid (in the form of a ValidationException)
	 *                        or if an exception occurs querying the database
	 */
	public static List<RequisitionCalendarTO> getRequisitionHiringEventsAndCountsForStore(final String storeNbr) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getRequisitionHiringEventsAndCountsForStore(), storeNumber: %1$s", storeNbr));
		} // end if
		
		// create a handler object that will be used to process the results of the query
		ScheduleHandler resultsHandler = new ScheduleHandler();
		
		//Create list to hold the hiring events
		final List<RequisitionCalendarTO> hiringEvents = new ArrayList<RequisitionCalendarTO>();
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateStoreNumber(storeNbr);
			
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
					ScheduleHandler resultsHandler = (ScheduleHandler)handler;
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to validate store number %1$s", storeNbr));
					} // end if					
					// first validate the location number provided is valid
					if(!LocationManager.isValidStoreNumber(storeNbr))
					{
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNbr));
					} // end if(!LocationManager.isValidStoreNumber(storeNbr))
					Calendar current = Calendar.getInstance();					
					// next prepare the query to get the hiring events for the store with counts
					MapStream inputs = new MapStream(READ_REQUISITION_HIRING_EVENTS_WITH_COUNTS);
					inputs.put(HR_SYS_STR_NBR, storeNbr);
					inputs.put(ACTV_FLG, true);
					inputs.put(STORE_ACTIVE_FLG, true);
					inputs.put(HR_CAL_TYP_CD, HIRING_EVENT_TYPE_CD);
					inputs.put(AVAIL_SLOT_STAT_CD, SLOT_AVAIL_CD);
					inputs.put(AVAIL_SLOT_GREATER_TIMESTAMP, new Timestamp(current.getTimeInMillis()));
					inputs.put(SCHEDULED_SLOT_STAT_CD, SLOT_SCHD_CD);
					inputs.put(SCHD_SLOT_GREATER_TIMESTAMP, new Timestamp(current.getTimeInMillis()));
					inputs.put(CALENDAR_ACTV_FLG, true);
					inputs.put(EVENT_ACTV_FLG, true);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying to get active requisition hiring events for store %1$s", storeNbr));
					} // end if
					
					query.getResult(connection, resultsHandler, inputs);
					
					hiringEvents.addAll(resultsHandler.getCalendars());
					
					//Get the List of Participating Stores to add to the Hiring Event List
					for (int i = 0; i < hiringEvents.size(); i++) 
					{
						RequisitionCalendarTO theEvent = (RequisitionCalendarTO) hiringEvents.get(i);
						inputs.clear();
						inputs = new MapStream(READ_PARTICIPATING_STORE_DATA);
						inputs.put(ACTV_FLG, true);
						inputs.put(STORE_ACTIVE_FLG, true);
						inputs.put(HIRE_EVENT_ID, theEvent.getHireEventId());
						
						query.getResult(connection, resultsHandler, inputs);
						
						List<HiringEventsStoreDetailsTO> participatingStores = resultsHandler.getParticipatingStores();
						StringBuilder stores = new StringBuilder();
						for (int m = 0; m < participatingStores.size(); m++) {
							HiringEventsStoreDetailsTO theStore = (HiringEventsStoreDetailsTO) participatingStores.get(m);
							stores.append(theStore.getStrNum()).append(",");
						} // end for (int m = 0; m < participatingStores.size(); m++)
						theEvent.setParticipatingStores(stores.toString());
					} // end for (int i = 0; i<hiringEvents.size();i++)
				} // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
			// execute the queries
			handler.execute();
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting hiring events for store %1$s", storeNbr), qe);
			// throw the exception
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())		
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getRequisitionHiringEventsAndCountsForStore(), storeNumber: %1$s. Returning %2$d requisition hiring events in %3$.9f seconds",
				storeNbr, (resultsHandler.getCalendars() == null ? 0 : resultsHandler.getCalendars().size()), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return hiringEvents;
	} // end function getRequisitionHiringEventsAndCountsForStore()	
} // end class ScheduleManager
package com.homedepot.hr.hr.staffingforms.dao.handlers;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: CalendarHandler.java
 * Application: RetailStaffing
 */
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.hr.hr.staffingforms.dto.SlotCounter;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.util.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.annotations.AnnotatedQueryHandler;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;
import com.homedepot.ta.aa.dao.exceptions.InsertException;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * Annotated query handler used to process queries in complicated or transacted flows 
 */
public class CalendarHandler extends AnnotatedQueryHandler implements DAOConstants
{
	// number of records affected by an insert/update/delete
	private int mRecordsAffected;
	
	// collection of date/time --> counter objects
	private Map<Timestamp, SlotCounter> mSlotCounters;
	
	// collection of requisition calendar objects
	private Map<Integer, RequisitionCalendar> mCalendars;
	
	/**
	 * This method processes the results of the "createHumanResourcesRequisitionScheduleV2" selector
	 * 
	 * @param target object inserted
	 * @param success true if the insert was successful, false otherwise
	 * @param count the number of records inserted
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an the number of records inserted did not match the input collection size,
	 * 						  or if an exception occurred querying the database
	 */
	@QuerySelector(name = INSERT_CALENDAR_SLOT, operation = QueryMethod.insertObject)
	public void addAvailability(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		// if the correct number of records was not added
		if(!success && (count != inputs.getNVStream().getList(HR_REQN_SCHED_LIST).size()))
		{
			throw new InsertException(String.format("An exception occurred inserting interview slots. Expected record count: %1$d, actual records inserted: %2$d. Rolling back",
				inputs.getNVStream().getList(HR_REQN_SCHED_LIST).size(), count));
		} // end if(!success && (count != inputs.getNVStream().getList(HR_REQN_SCHED_LIST).size()))
		
		// add the number of inserted to the count (because these are done in batch, this is a running total)
		mRecordsAffected += count;
	} // end function addAvailability()
	
	/**
	 * This method processes the results of the "deleteHumanResourcesRequisitionScheduleBatch" selector
	 * 
	 * @param target object deleted
	 * @param success true if the delete was successful, false otherwise
	 * @param count the number of records deleted
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an the number of records deleted did not match the input collection size,
	 * 						  or if an exception occurred querying the database
	 */
	@QuerySelector(name = DELETE_CALENDAR_SLOT, operation = QueryMethod.deleteObject)
	public void removeAvailability(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		// if the correct number of records was not deleted
		if(!success && (count != inputs.getNVStream().getList(HR_REQN_SCHED_LIST).size()))
		{
			throw new InsertException(String.format("An exception occurred deleting interview slots. Expected record count: %1$d, actual records deleted: %2$d.",
				inputs.getNVStream().getList(HR_REQN_SCHED_LIST).size(), count));
		} // end if(!success && (count != inputs.getNVStream().getList(HR_REQN_SCHED_LIST).size()))
		
		// add the number of deleted to the count (because these are done in batch, this is a running total)
		mRecordsAffected += count;		
	} // end function removeAvailability()
	
	/**
	 * This method processes the results of the "readHumanResourcesRequisitionScheduleSlotCountDetails" selector
	 * 
	 * @param results DAO results object
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	@QuerySelector(name = READ_REQN_SCHED_SLOT_COUNT, operation = QueryMethod.getResult)
	public void readRequisitionScheduleSlotCount(Results results, Query query, Inputs inputs) throws QueryException
	{
		// initialize the map
		mSlotCounters = new HashMap<Timestamp, SlotCounter>();
		
		// iterate the results returned 
		while(results.next())
		{
			// put the record into the map
			mSlotCounters.put(results.getTimestamp(BGN_TS), new SlotCounter(results.getInt(SLOT_COUNT), results.getShort(MAX_SEQ)));
		} // end while(results.next())
	} // end function readRequisitionScheduleSlotCount()
	
	/**
	 * This method processes the results of the "readHumanResourcesRequisitionCalendarV2" selector
	 * 
	 * @param results DAO results object
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	@QuerySelector(name = READ_CALENDARS, operation = QueryMethod.getResult)
	public void readRequisitionCalendar(Results results, Query query, Inputs inputs) throws QueryException
	{
		// initialize the collection
		mCalendars = new HashMap<Integer, RequisitionCalendar>();
		RequisitionCalendar calendar = null;
		
		// while there are records
		while(results.next())
		{
			// iterate and populate a new calendar object for each record
			calendar = new RequisitionCalendar();
			calendar.setId(results.getInt(REQN_CAL_ID));
			calendar.setCrtTs(results.getTimestamp(CRT_TS));
			calendar.setCrtSysUsrId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
			calendar.setLastUpdSysUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
			calendar.setLastUpdTs(results.getTimestamp(LAST_UPD_TS));
			calendar.setDesc(StringUtils.trim(results.getString(REQN_CAL_DESC)));
			calendar.setStrNbr(StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
			calendar.setActv(results.getBoolean(ACTV_FLG));
			
			if(!results.wasNull(HR_CAL_TYP_CD))
			{
				calendar.setCalTypCd(results.getShort(HR_CAL_TYP_CD));
			} // end if(!results.wasNull(HR_CAL_TYP_CD))			
			
			// add the calendar object to the collection
			mCalendars.put(calendar.getId(), calendar);
		} // end while(results.next())
	} // end function readRequisitionCalendar()	
	
	/**
	 * @return the number of records affected by an insert/update/delete
	 */
	public int getRecordsAffected()
	{
		return mRecordsAffected;
	} // end function getRecordsAffected()
	
	/**
	 * @param recordsAffected the number of records affected by an insert/update/delete
	 */
	public void setRecordsAffected(int recordsAffected)
	{
		mRecordsAffected = recordsAffected;
	} // end function setRecordsAffected()
	
	/**
	 * @return collection of date/time --> counter objects
	 */
	public Map<Timestamp, SlotCounter> getSlotCounters()
	{
		return mSlotCounters;
	} // end function getSlotCounters()

	/**
	 * @param counters collection of date/time --> counter objects
	 */
	public void setSlotCounters(Map<Timestamp, SlotCounter> counters)
	{
		mSlotCounters = counters;
	} // end function setSlotCounters()
	
	/**
	 * @return collection of requisition calendars
	 */
	public Map<Integer, RequisitionCalendar> getCalendars()
	{
		return mCalendars;
	} // end function getCalendars()
	
	/**
	 * @param calendars collection of requisition calendars
	 */
	public void setCalendars(Map<Integer, RequisitionCalendar> calendars)
	{
		mCalendars = calendars;
	} // end function setCalendars()
	/**
	 * This method processes the results of the "updateHumanResourcesRequisitionCalendarByInputList" selector
	 * 
	 * @param target object updated
	 * @param success true if the update was successful, false otherwise
	 * @param count the number of records updated
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an the number of records deleted did not match the input collection size,
	 * 						  or if an exception occurred querying the database
	 */
	@QuerySelector(name = UPDATE_HR_REQN_CAL, operation = QueryMethod.updateObject)
	public void updateHRReqnCalendar(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		// if the correct number of records was not deleted
		if(!success)
		{
			throw new InsertException(String.format("An exception occurred updating calendar."));
		} // end if(!success)
				
	} // end function updateHRReqnCalendar()
	
	/**
	 * This method processes the results of the "readHumanResourcesRequisitionCalendarAndEventStoreDetails" selector
	 * 
	 * @param results DAO results object
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	@QuerySelector(name = READ_REQUISITION_HIRING_EVENTS, operation = QueryMethod.getResult)
	public void readRequisitionHiringEventsCalendar(Results results, Query query, Inputs inputs) throws QueryException
	{
		// initialize the collection
		mCalendars = new HashMap<Integer, RequisitionCalendar>();

		RequisitionCalendar calendar = null;
		
		// while there are records
		while(results.next())
		{
			// iterate and populate a new calendar object for each record
			calendar = new RequisitionCalendar();
			calendar.setId(results.getInt(REQN_CAL_ID));
			//The requisitionCalendarDescription is used to hold multiple pieces of data with ~|~ as the delimiter
			//Field 0 = Event Created by Store Number
			//Field 1 = Event Description
			String[] splitField = StringUtils.trim(results.getString(REQN_CAL_DESC)).split("~\\|~");
			calendar.setDesc(splitField[1]);
			calendar.setStrNbr(StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
			calendar.setActv(results.getBoolean(ACTV_FLG));
			
			if(!results.wasNull(HR_CAL_TYP_CD))
			{
				calendar.setCalTypCd(results.getShort(HR_CAL_TYP_CD));
			} // end if(!results.wasNull(HR_CAL_TYP_CD))			
			
			// add the calendar object to the collection
			mCalendars.put(calendar.getId(), calendar);
		} // end while(results.next())
	} // end function readRequisitionHiringEventsCalendar()		
} // end class CalendarHandler
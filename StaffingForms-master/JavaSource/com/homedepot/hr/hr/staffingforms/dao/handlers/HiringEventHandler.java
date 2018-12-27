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
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.util.StringUtils;
import com.homedepot.hr.hr.staffingforms.dto.HiringEventDetail;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.annotations.AnnotatedQueryHandler;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * Annotated query handler used to process queries in complicated or transacted flows 
 */
public class HiringEventHandler extends AnnotatedQueryHandler implements DAOConstants
{
	// number of records affected by an insert/update/delete
	private int mRecordsAffected;
	
	// returned event id
	private int mHiringEventId;
	
	// returned calendarId
	private int mCalendarId;
	
	// returned hiring event calendar list
	private List<RequisitionCalendar> mHiringEventCalendars;
	
	// returned list of hiring event calendars
	private boolean mIsDuplicateHiringEventName = false;
	
	// returned Interview Data from Phone Screen table
	private HiringEventDetail mPhoneScreenIntvwDetails;
	
	/**
	 * Process the results of the createHumanResourcesHireEvent selector
	 * 
	 * @param target 			Key of the object(s) inserted
	 * @param success			true if the insert was successful, false otherwise
	 * @param count				number of rows inserted
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = CREATE_HR_HIRE_EVENT, operation = QueryMethod.insertObject)
	public void insertHiringEvent(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(count != 1)
		{
			throw new QueryException("An exception occurred inserting Hiring Event, insert count != 1");
		} // end if(count != 1)
	
		MapStream primaryKeysStream;
		StringBuilder genratedHireEventIdString = new StringBuilder(10);
		primaryKeysStream = (MapStream) target;
		genratedHireEventIdString.append(primaryKeysStream.getMap("primaryKeys").get("hireEventId"));
		
		mHiringEventId = Integer.parseInt(genratedHireEventIdString.toString());
		
	} // end function insertHiringEvent()

	/**
	 * Process the results of the updateHumanResourcesHireEvent selector
	 * 
	 * @param target 			Key of the object(s) updated
	 * @param success			true if the update was successful, false otherwise
	 * @param count				number of rows updated
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = UPDATE_HR_HIRE_EVENT, operation = QueryMethod.updateObject)
	public void updateHireEvent(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(count != 1)
		{
			throw new QueryException("An exception occurred updating Hiring Event, update count != 1");
		} // end if(count != 1)
		
	} // end function updateHireEvent()
	
	/**
	 * Process the results of the insertHireEventReqnCal selector
	 * 
	 * @param target 			Key of the object(s) inserted
	 * @param success			true if the insert was successful, false otherwise
	 * @param count				number of rows inserted
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = CREATE_HIRING_EVENT_HR_REQN_CAL, operation = QueryMethod.insertObject)
	public void insertHireEventReqnCal(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(count != 1)
		{
			throw new QueryException("An exception occurred inserting Hiring Event Calendar, insert count != 1");
		} // end if(count != 1)
			
		mCalendarId = ((Integer)target).intValue();
		
	} // end function insertHireEventReqnCal()

	/**
	 * Process the results of the updateHumanResourcesRequisitionCalendar selector
	 * 
	 * @param target 			Key of the object(s) updated
	 * @param success			true if the update was successful, false otherwise
	 * @param count				number of rows updated
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = UPDATE_HIRING_EVENT_HR_REQN_CAL, operation = QueryMethod.updateObject)
	public void updateHireEventReqnCal(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(count != 1)
		{
			throw new QueryException("An exception occurred updating Hiring Event Calendar, update count != 1");
		} // end if(count != 1)
		
	} // end function updateHireEventReqnCal()
	
	/**
	 * Process the results of the readHumanResourcesRequisitionCalendar selector
	 * 
	 * @param results DAO results object
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	@QuerySelector(name = READ_HIRING_EVENT_CALENDARS, operation = QueryMethod.getResult)
	public void readHumanResourcesRequisitionCalendar(Results results, Query query, Inputs inputs) throws QueryException
	{	
		//This selector returns a list of hiring event calendars
		mHiringEventCalendars = new ArrayList<RequisitionCalendar>();

		// loop the results and add the calendar objects to the return collection
		RequisitionCalendar calendar = null;
		while(results.next())
		{
			calendar = new RequisitionCalendar();

			calendar.setId(results.getInt(REQN_CAL_ID));
			calendar.setCrtTs(results.getTimestamp(CRT_TS));
			calendar.setCrtSysUsrId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
			calendar.setLastUpdSysUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
			calendar.setLastUpdTs(results.getTimestamp(LAST_UPD_TS));
			calendar.setDesc(StringUtils.trim(results.getString(REQN_CAL_DESC)));
			calendar.setStrNbr(results.getString(HR_SYS_STR_NBR));
			// if the type is not null, set it
			if(!results.wasNull(HR_CAL_TYP_CD))
			{
				calendar.setCalTypCd(results.getShort(HR_CAL_TYP_CD));
			} // end if(!results.wasNull(HR_CAL_TYP_CD))				
			calendar.setActv(results.getBoolean(ACTV_FLG));
			// Set Hiring Event Id on the Calendars and hireEventId because this handler is used for multiple things 
			calendar.setHireEventId(results.getInt(HIRE_EVNT_ID));
			mHiringEventId = results.getInt(HIRE_EVNT_ID);
			
			// add the calendar to the collection
			mHiringEventCalendars.add(calendar);
		} // end while(results.next())
			
	} // end function readHumanResourcesRequisitionCalendar()
	
	/**
	 * Process the results of the createHumanResourcesHireEventStore selector
	 * 
	 * @param target 			Key of the object(s) inserted
	 * @param success			true if the insert was successful, false otherwise
	 * @param count				number of rows inserted
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = CREATE_HR_HIRE_EVNT_STR, operation = QueryMethod.insertObject)
	public void insertHireEventStr(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(count != 1)
		{
			throw new QueryException("An exception occurred inserting Hiring Event Store, insert count != 1");
		} // end if(count != 1)
		
	} // end function insertHireEventStr()
	
	/**
	 * Process the results of the updateHumanResourcesHireEventStore selector
	 * 
	 * @param target 			Key of the object(s) updated
	 * @param success			true if the update was successful, false otherwise
	 * @param count				number of rows updated
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = UPDATE_HR_HIRE_EVNT_STR, operation = QueryMethod.updateObject)
	public void updateHireEventStr(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(count != 1)
		{
			throw new QueryException("An exception occurred updating Hiring Event Store, update count != 1");
		} // end if(count != 1)
		
	} // end function updateHireEventStr()
	
	/**
	 * Process the results of the deleteHumanResourcesHireEventStore selector
	 * 
	 * @param target 			Key of the object(s) inserted
	 * @param success			true if the insert was successful, false otherwise
	 * @param count				number of rows inserted
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = DELETE_HR_HIRE_EVNT_STR, operation = QueryMethod.deleteObject)
	public void deleteHumanResourcesHireEventStore(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(!success)
		{
			throw new QueryException("An exception occurred deleting Hiring Event Participating Store");
		} // end (!success)
		
	} // end function deleteHumanResourcesHireEventStore()
	

	/**
	 * Process the results of the readInterviewDetails selector
	 * 
	 * @param results DAO results object
	 * @param query DAO query object
	 * @param inputs DAO input collection
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	@QuerySelector(name = READ_INTVW_DETAILS_FROM_PHN_SCRN, operation = QueryMethod.getResult)
	public void readInterviewDetailsFromPhoneScreen(Results results, Query query, Inputs inputs) throws QueryException
	{	
		//This selector returns a single Phone Screen Interview Data
		HiringEventDetail phoneScreenIntvwDetails = new HiringEventDetail();

		if (results.next())
		{
			phoneScreenIntvwDetails.setInterviewLocationTypeCode(results.getShort(INTVW_LOC_TYPE_CODE));
			phoneScreenIntvwDetails.setInterviewerName(results.getString(INTERVIEWER_NAME));
			phoneScreenIntvwDetails.setInterviewDate(results.getDate(INTVW_DATE));
			phoneScreenIntvwDetails.setInterviewTimestamp(results.getTimestamp(INTVW_TIMESTAMP));
		} // end if(results.next())
		
		mPhoneScreenIntvwDetails = phoneScreenIntvwDetails;
	} // end function readInterviewDetailsFromPhoneScreen()	
	
	
	/**
	 * Process the results of the updateHumanResourcesPhoneScreenInterviewDate selector
	 * 
	 * @param target 			Key of the object(s) updated
	 * @param success			true if the update was successful, false otherwise
	 * @param count				number of rows updated
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 *            
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = UPDATE_INTVW_DETAILS_IN_PHN_SCRN, operation = QueryMethod.updateObject)
	public void updateInterviewDetailsInPhoneScreen(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{	
		if(count != 1)
		{
			throw new QueryException("An exception occurred updating interview details in phone screen, update count != 1");
		} // end if(count != 1)
		
	} // end function updateInterviewDetailsInPhoneScreen()	
	
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
	 * @return the Hiring Event ID
	 */
	public int getHiringEventId()
	{
		return mHiringEventId;
	} // end function getHiringEventId()	
	
	/**
	 * @return the calendar Id
	 */
	public int getCalendarId()
	{
		return mCalendarId;
	} // end function getCalendarId()

	/**
	 * @return if duplicate hiring event name
	 */
	public boolean getIsDuplicateHiringEventName() {
		return mIsDuplicateHiringEventName;
	} //end getIsDuplicateHiringEventName()
	
	/**
	 * @return List of Hiring Event Calendars
	 */
	public List<RequisitionCalendar> getHringEventCalendars() {
		return mHiringEventCalendars;
	} //end getHringEventCalendars()
	
	/**
	 * @return Phone Screen Interview Details
	 */
	public HiringEventDetail getPhoneScreenIntvwDetails() {
		return mPhoneScreenIntvwDetails;
	} // end getPhoneScreenIntvwDetails()
} // end class HiringEventHandler
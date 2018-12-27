package com.homedepot.hr.hr.retailstaffing.dao.handlers;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ScheduleHandler.java
 * Application: RetailStaffing
 *
 */
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.HiringEventsStoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwScheduleDetails;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.annotations.AnnotatedQueryHandler;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * Class used to process results of employment requisition interviews DAO selectors 
 */
public class ScheduleHandler extends AnnotatedQueryHandler implements DAOConstants
{
	// key information used to get interview scheduling details
	private IntrvwScheduleDetails mSchedDetails;

	/** Collection of requisition calendars, populated whenever multiple requisition calendars could be returned */
	private List<RequisitionCalendarTO> mCalendars;

	/** Requisition calendar, populated when a single requisition calendar could be returned (key search) */
	private RequisitionCalendarTO mCalendar;

	// requisition calendar id, populated whenever a new calendar object is created
	private int mCalendarId;

	// populated whenever an existence query is executed
	private boolean mValid;
	
	/** Collection of Hiring Event Participating Store Details */
	private List<HiringEventsStoreDetailsTO> mParticipatingStores;

	/**
	 * Process the results returned by the readInterviewSchedulingDetailsForPhoneScreen
	 * selector
	 * 
	 * @param results			Query results returned by the DAO framework
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 * 
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = READ_INTERVIEW_DETAILS_FOR_PHONE_SCREEN, operation = QueryMethod.getResult)
	public void readIntrvwSchDetailsForPhnScrns(Results results, Query query, Inputs inputs) throws QueryException
	{
		// if a record was returned (should only ever be one)
		if(results.next())
		{
			mSchedDetails = new IntrvwScheduleDetails();
			mSchedDetails.setPhnScrnId(inputs.getNVStream().getInt(HR_PHN_SCRN_ID));
			mSchedDetails.setStoreNbr(results.getString(HR_SYS_STR_NBR));
			mSchedDetails.setEmpltPosnCandId(results.getString(EMPLT_POSN_CAND_ID));
			mSchedDetails.setEmpltReqNbr(results.getInt(EMPLT_REQN_NBR));
			mSchedDetails.setHireEvntFlg(results.getString(HIRE_EVNT_FLG));
			mSchedDetails.setRscSchFlg(results.getString(RSC_SCH_FLG));
			mSchedDetails.setReqActvFlg(results.getString(REQ_ACTV_FLG));
			mSchedDetails.setCandActvFlg(results.getString(CAND_ACTV_FLG));

			// if the calendar id was not null, set it
			if(!results.wasNull(REQN_CAL_ID))
			{
				mSchedDetails.setReqCalId(results.getInt(REQN_CAL_ID));
			} // end if(!results.wasNull(REQN_CAL_ID))

			// if the interview status code was not null, set it
			if(!results.wasNull(INTVW_STAT_CD))
			{
				mSchedDetails.setIntrvwStatCd(results.getShort(INTVW_STAT_CD));
			} // end if(!results.wasNull(INTRVW_STAT_CD))

			// if the interview minutes was not null, set it
			if(!results.wasNull(INTRVW_MINS))
			{
				mSchedDetails.setIntrvwMins(results.getShort(INTRVW_MINS));
			} // end if(!results.wasNull(INTRVW_MINS))
			
			// if the event type was not null, set it
			if (!results.wasNull(EVENT_TYPE)) {
				mSchedDetails.setEventType(results.getString(EVENT_TYPE));
			} // end if (!results.wasNull(EVENT_TYPE))
		} // end if(results.next())
	} // end function readIntrvwSchDetailsForPhnScrn()

	/**
	 * Process the results of the readHumanResourcesRequisitionCalendar selector
	 * 
	 * @param results			Query results returned by the DAO framework
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 * 
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = READ_REQUISITION_CALENDAR, operation = QueryMethod.getResult)
	public void readHumanResourcesRequisitionCalendar(Results results, Query query, Inputs inputs) throws QueryException
	{		
		/*
		 * the same DAO selector is used for different searches. if this is a PK search, the inputs collection
		 * will contain the "requisitionCalendarId" value, otherwise, this is assumed to be a "generic" search
		 * that can return multiple rows
		 */
		if(inputs != null && inputs.getNVStream() != null && inputs.getNVStream().containsName(REQN_CAL_ID))
		{
			// "PK" search
			if(results.next())
			{
				mCalendar = new RequisitionCalendarTO();
				mCalendar.setRequisitionCalendarId(results.getInt(REQN_CAL_ID));
				mCalendar.setCreateTimestamp(results.getTimestamp(CRT_TS));
				mCalendar.setCreateSystemUserId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
				mCalendar.setLastUpdateSystemUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
				mCalendar.setLastUpdateTimestamp(results.getTimestamp(LAST_UPD_TS));
				mCalendar.setRequisitionCalendarDescription(StringUtils.trim(results.getString(REQN_CAL_DESC)));
				mCalendar.setHumanResourcesSystemStoreNumber(results.getString(HR_SYS_STR_NBR));
				// if the type is not null, set it
				if(!results.wasNull(HR_CAL_TYP_CD))
				{
					mCalendar.setType(results.getShort(HR_CAL_TYP_CD));
				} // end if(!results.wasNull(HR_CAL_TYP_CD))				
				mCalendar.setActiveFlag(results.getBoolean(ACTV_FLG));
			} // end if(results.next())
			else
			{
				throw new NoRowsFoundException(ApplicationObject.REQUISITION_CALENDAR, String.format("Requisition calendar %1$s not found", 
					inputs.getNVStream().getInt(REQN_CAL_ID)));
			} // end else
		} // end if(inputs != null && inputs.getNVStream() != null && inputs.getNVStream().containsName(REQN_CAL_ID))
		else
		{
			// assumed "generic" search
			// initialize the collection
			mCalendars = new ArrayList<RequisitionCalendarTO>();

			// loop the results and add the calendar objects to the return collection
			RequisitionCalendarTO calendar = null;
			while(results.next())
			{
				calendar = new RequisitionCalendarTO();

				calendar.setRequisitionCalendarId(results.getInt(REQN_CAL_ID));
				calendar.setCreateTimestamp(results.getTimestamp(CRT_TS));
				calendar.setCreateSystemUserId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
				calendar.setLastUpdateSystemUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
				calendar.setLastUpdateTimestamp(results.getTimestamp(LAST_UPD_TS));
				calendar.setRequisitionCalendarDescription(StringUtils.trim(results.getString(REQN_CAL_DESC)));
				calendar.setHumanResourcesSystemStoreNumber(results.getString(HR_SYS_STR_NBR));
				// if the type is not null, set it
				if(!results.wasNull(HR_CAL_TYP_CD))
				{
					calendar.setType(results.getShort(HR_CAL_TYP_CD));
				} // end if(!results.wasNull(HR_CAL_TYP_CD))				
				calendar.setActiveFlag(results.getBoolean(ACTV_FLG));
				// add the calendar to the collection
				mCalendars.add(calendar);
			} // end while(results.next())
		} // end else
	} // end function readHumanResourcesRequisitionCalendar()	

	/**
	 * Process the results of the createHumanResourcesRequisitionCalendar selector
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
	@QuerySelector(name = INS_HR_REQN_CAL, operation = QueryMethod.insertObject)
	public void insertCalendar(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		if(count != 1)
		{
			throw new QueryException("An exception occurred inserting requisition calendar, insert count != 1");
		} // end if(count != 1)

		// get the id of the calendar just inserted
		mCalendarId = ((Integer)target).intValue();
	} // end function insertCalendar()

	/**
	 * Process the results of the readHumanResourcesRequisitionCalendarDetailsByInputList selector
	 * 
	 * @param results			Query results returned by the DAO framework
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 * 
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = READ_REQUISITION_CALENDAR_WITH_COUNTS, operation = QueryMethod.getResult)
	public void readHumanResourcesRequisitionCalendarDetailsByInputList(Results results, Query query, Inputs inputs) throws QueryException
	{
		// initialize the collection
		mCalendars = new ArrayList<RequisitionCalendarTO>();

		// loop the results and add the calendar objects to the return collection
		RequisitionCalendarTO calendar = null;
		while(results.next())
		{
			calendar = new RequisitionCalendarTO();
			calendar.setRequisitionCalendarId(results.getInt(REQN_CAL_ID));
			calendar.setCreateTimestamp(results.getTimestamp(CRT_TS));
			//Added for FMS 7894 January 2016 CR's
			calendar.setAvailabilityAddedTimestamp(results.getTimestamp(AVAIL_TIME_ADDED_TS));
			calendar.setCreateSystemUserId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
			calendar.setLastUpdateSystemUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
			calendar.setLastUpdateTimestamp(results.getTimestamp(LAST_UPD_TS));
			calendar.setRequisitionCalendarDescription(StringUtils.trim(results.getString(REQN_CAL_DESC)));
			calendar.setHumanResourcesSystemStoreNumber(results.getString(HR_SYS_STR_NBR));
			// if the type is not null, set it
			if(!results.wasNull(HR_CAL_TYP_CD))
			{
				calendar.setType(results.getShort(HR_CAL_TYP_CD));
			} // end if(!results.wasNull(HR_CAL_TYP_CD))				
			calendar.setActiveFlag(results.getBoolean(ACTV_FLG));
			calendar.setActvReqnCount(results.getInt(ACTV_REQN_CNT));
			calendar.setAvailIntvwSlots(results.getInt(AVAIL_INTVW_SLOT_CNT));

			// add the calendar to the collection
			mCalendars.add(calendar);
		} // end while(results.next())
	} // end function readHumanResourcesRequisitionCalendarDetailsByInputList()

	/**
	 * Process the results of the readHumanResourcesRequisitionHiringEvent selector
	 * 
	 * @param results			Query results returned by the DAO framework
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 * 
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = READ_REQUISITION_HIRING_EVENTS, operation = QueryMethod.getResult)
	public void readHumanResourcesRequisitionHiringEvent(Results results, Query query, Inputs inputs) throws QueryException
	{		
			// initialize the collection
			mCalendars = new ArrayList<RequisitionCalendarTO>();

			// loop the results and add the calendar objects to the return collection
			RequisitionCalendarTO calendar = null;
			while(results.next())
			{
				calendar = new RequisitionCalendarTO();
				calendar.setRequisitionCalendarId(results.getInt(REQN_CAL_ID));				
				//The requisitionCalendarDescription is used to hold multiple pieces of data with ~|~ as the delimiter
				//Field 0 = Event Created by Store Number
				//Field 1 = Event Description
				String[] splitField = StringUtils.trim(results.getString(REQN_CAL_DESC)).split("~\\|~");
				calendar.setEventCreatedByStore(splitField[0]);
				calendar.setRequisitionCalendarDescription(splitField[1]);
				calendar.setHumanResourcesSystemStoreNumber(results.getString(HR_SYS_STR_NBR));
				calendar.setHireEventId(results.getInt(HIRE_EVENT_ID));
				calendar.setHireEventSiteFlg(results.getBoolean(HIRE_EVENT_SITE_FLG));
				//Type is not returned from the DB.  Set it to 100 by default 
				calendar.setType((short) 100);
				if (!results.wasNull(HIRE_EVENT_BEGIN_DT)) {
					calendar.setHireEventBeginDate(results.getDate(HIRE_EVENT_BEGIN_DT));
				}
				if (!results.wasNull(HIRE_EVENT_END_DT)) {
					calendar.setHireEventEndDate(results.getDate(HIRE_EVENT_END_DT));
				}			
				calendar.setHireEventLocationDescription(StringUtils.trim(results.getString(HIRE_EVENT_LOCATION)));
				
				// add the calendar to the collection
				mCalendars.add(calendar);
			} // end while(results.next())
	} // end function readHumanResourcesRequisitionHiringEvent()
	
	/**
	 * Process the results of the readHumanResourcesHireEventStoreAndScheduleDetails selector
	 * 
	 * @param results			Query results returned by the DAO framework
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 * 
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = READ_REQUISITION_HIRING_EVENTS_WITH_COUNTS, operation = QueryMethod.getResult)
	public void readHumanResourcesRequisitionHiringEventDetailsWithCounts(Results results, Query query, Inputs inputs) throws QueryException
	{
		// initialize the collection
		mCalendars = new ArrayList<RequisitionCalendarTO>();

		// loop the results and add the calendar objects to the return collection
		RequisitionCalendarTO calendar = null;
		while(results.next())
		{
			calendar = new RequisitionCalendarTO();
			calendar.setRequisitionCalendarId(results.getInt(REQN_CAL_ID));
			//The requisitionCalendarDescription is used to hold multiple pieces of data with ~|~ as the delimiter
			//Field 0 = Event Created by Store Number
			//Field 1 = Event Description
			String[] splitField = StringUtils.trim(results.getString(REQN_CAL_DESC)).split("~\\|~");
			calendar.setEventCreatedByStore(splitField[0]);
			calendar.setRequisitionCalendarDescription(splitField[1]);
			calendar.setHireEventId(results.getInt(HIRE_EVENT_ID));
			if (!results.wasNull(HIRE_EVENT_BEGIN_DT)) {
				calendar.setHireEventBeginDate(results.getDate(HIRE_EVENT_BEGIN_DT));
			}
			if (!results.wasNull(HIRE_EVENT_END_DT)) {
				calendar.setHireEventEndDate(results.getDate(HIRE_EVENT_END_DT));
			}
			
			if ("Y".equals(results.getString(HIRE_EVENT_SITE_FLG))) {
				calendar.setHireEventSiteFlg(true);
			} else {
				calendar.setHireEventSiteFlg(false);
			}
			calendar.setNumParticipatingStores(results.getInt(NUMBER_PARTICIPATING_STORES));
			calendar.setAvailIntvwSlots(results.getInt(AVAILABLE_INTERVIEW_SLOTS));
			calendar.setScheduledInterviewSlots(results.getInt(SCHEDULED_INTERVIEW_SLOTS));
			calendar.setHumanResourcesSystemStoreNumber(results.getString(HR_SYS_STR_NBR));
			calendar.setActvReqnCount(results.getInt(ACTV_REQN_CNT));
			calendar.setLastUpdateTimestamp(results.getTimestamp(LAST_UPD_TS));

			// add the calendar to the collection
			mCalendars.add(calendar);
		} // end while(results.next())
	} // end function readHumanResourcesRequisitionHiringEventDetailsWithCounts()

	/**
	 * Process the results of the readHumanResourcesStoreEmploymentRequisitionAndEventStoreDetails selector
	 * 
	 * @param results			Query results returned by the DAO framework
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 * 
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = READ_PARTICIPATING_STORE_DATA, operation = QueryMethod.getResult)
	public void readHumanResourcesStoreEmploymentRequisitionAndEventStoreDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// initialize the collection
		mParticipatingStores = new ArrayList<HiringEventsStoreDetailsTO>();

		// loop the results and add the objects to the return collection
		HiringEventsStoreDetailsTO participatingStores = null;
		while(results.next())
		{
			participatingStores = new HiringEventsStoreDetailsTO();
			participatingStores.setStrNum(results.getString(HR_SYS_STR_NBR));
			participatingStores.setStrName(results.getString(HR_SYS_STR_NAME));
			participatingStores.setHireEventId(results.getInt(HIRE_EVENT_ID));
			participatingStores.setActiveReqCount(results.getInt(ACTV_REQN_CNT));

			// add the  to the collection
			mParticipatingStores.add(participatingStores);
		} // end while(results.next())
	} // end function readHumanResourcesStoreEmploymentRequisitionAndEventStoreDetails()	
	
	/**
	 * Process the results of the readHumanResourcesCalendarTypeCodeForExistence selector
	 * 
	 * @param target			BOOLEAN indicator for object existence
	 * @param query				DAO framework query object
	 * @param inputs			Inputs collection provided to the DAO framework for this
	 *            				queries execution
	 * 
	 * @throws QueryException	Thrown if an exception occurs using the DAO framework to
	 *             				execute the query
	 */
	@QuerySelector(name = READ_REQUISITION_CALENDAR_TYPE_EXIST, operation = QueryMethod.getObject)
	public void readHumanResourcesCalendarTypeCodeForExistence(Object target, Query query, Inputs inputs) throws QueryException
	{
		// if a record was returned, it exists
		if(target != null)
		{
			mValid = ((Boolean)target).booleanValue();
		} // end if(target != null)
	} // end function readHumanResourcesCalendarTypeCodeForExistence()
	
	
	/**
	 * Get key information used to get interview scheduling details
	 * 
	 * @return				Key information used to get interview scheduling details
	 */
	public IntrvwScheduleDetails getSchedDetails()
	{
		return mSchedDetails;
	} // end function getSchedDetails()

	/**
	 * Get the collection of requisition calendar objects
	 * 
	 * @return collection of requisition calendar objects
	 */
	public List<RequisitionCalendarTO> getCalendars()
	{
		return mCalendars;
	} // end function getCalendars()	

	/**
	 * Get the collection of participating store HiringEventsStoreDetailsTO objects
	 * 
	 * @return collection of participating store HiringEventsStoreDetailsTO objects
	 */
	public List<HiringEventsStoreDetailsTO> getParticipatingStores()
	{
		return mParticipatingStores;
	} // end function getParticipatingStores()
	
	/**
	 * Get the requisition calendar object
	 * 
	 * @return the requisition calendar object
	 */
	public RequisitionCalendarTO getCalendar()
	{
		return mCalendar;
	} // end function getCalendar()

	/**
	 * Get the requisition calendar id (populated whenever an insert is performed)
	 * 
	 * @return the requisition calendar id
	 */
	public int getCalendarId()
	{
		return mCalendarId;
	} // end function getCalendarId()

	/**
	 * Get the existence value
	 * 
	 * @return true if the value exists, false otherwise
	 */
	public boolean isValid()
	{
		return mValid;
	} // end function isValid()
}// end class ScheduleHandler
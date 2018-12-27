package com.homedepot.hr.hr.retailstaffing.dao.handlers;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: LocationHandler.java
 * Application: RetailStaffing
 */
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.HiringEventDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventMgrTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventsStoreDetailsTO;
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
 * DAO AnnotatedQueryHandler containing call back methods used to process complex hiring events query results
 * 
 * @see AnnotatedQueryHandler
 */
public class HiringEventsHandler extends AnnotatedQueryHandler implements DAOConstants
{
	/** hiring event manager details object */
	private HiringEventMgrTO mEventMgrDetails;
	
	/** hiring event details object */
	private HiringEventDetailTO mEventDetails;
	
	/** Collection of Participating Stores in a Hiring Event */
	private List<HiringEventsStoreDetailsTO> mParticipatingStores;	
	
	/** Hiring Event Requisition calendar, populated when a single requisition calendar could be returned */
	private RequisitionCalendarTO mCalendar;
	
	@QuerySelector(name=READ_USER_DATA, operation=QueryMethod.getResult)
	public void readPartyContactMechanismRoleAndSystemUserDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only be a single record returned
		if(results.next())
		{
			// initialize the event manager details object
			mEventMgrDetails = new HiringEventMgrTO();
			mEventMgrDetails.setName(StringUtils.trim(results.getString(ASSOCIATE_NAME)));			
			mEventMgrDetails.setTitle(StringUtils.trim(results.getString(JOB_TITLE_CD)));
			StringBuilder completePhone = new StringBuilder(12);
			completePhone.append(StringUtils.trim(results.getString(PHN_AREA_CITY_CD))).append("-");
			if (StringUtils.trim(results.getString(PHN_LOCL_NBR)).length() == 7) {
				completePhone.append(StringUtils.trim(results.getString(PHN_LOCL_NBR).substring(0,3)));
				completePhone.append("-").append(StringUtils.trim(results.getString(PHN_LOCL_NBR).substring(3)));
			} else {
				completePhone.append(StringUtils.trim(results.getString(PHN_LOCL_NBR)));
			}
			mEventMgrDetails.setPhone(completePhone.toString());
			mEventMgrDetails.setAssociateId(StringUtils.trim(results.getString(OUT_HR_ASSOCIATE_ID)));
		} // end if(results.next())
	} // end function readPartyContactMechanismRoleAndSystemUserDetails()
	
/*	@QuerySelector(name=READ_USER_EMAIL_DATA, operation=QueryMethod.getResult)
	public void readEnterpriseEmailAddressAndSystemUserDetails(Results results, Query query, Inputs inputs) throws QueryException
	{	
		// there should only be a single record returned
		if(results.next())
		{
			//Should not happen, normal flow is to run readPartyContactMechanismRoleAndSystemUserDetails first. But if it does.
			if (mEventMgrDetails == null) {
				mEventMgrDetails = new HiringEventMgrTO();
			}
			mEventMgrDetails.setEmail(StringUtils.trim(results.getString(EMAIL_ADDRESS)));			
		} // end if(results.next())
	} // end function readEnterpriseEmailAddressAndSystemUserDetails()
	*/
	@QuerySelector(name=READ_HR_HIRE_EVENT, operation=QueryMethod.getResult)
	public void readHiringEventDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only be a single record returned
		if(results.next())
		{
			// initialize the event details object
			mEventDetails = new HiringEventDetailTO();
			mEventDetails.setHireEventId(results.getInt(HIRE_EVENT_ID));
			mEventDetails.setHireEventBeginDate(results.getDate(HIRE_EVENT_BEGIN_DT));
			mEventDetails.setHireEventEndDate(results.getDate(HIRE_EVENT_END_DT));
			mEventDetails.setHireEventLocationDescription(StringUtils.trim(results.getString(HIRE_EVENT_LOCATION)));
			mEventDetails.setHireEventAddressText(StringUtils.trim(results.getString(HIRE_EVENT_ADDRESS)));
			mEventDetails.setHireEventCityName(StringUtils.trim(results.getString(HIRE_EVENT_CITY)));
			mEventDetails.setHireEventStateCode(StringUtils.trim(results.getString(HIRE_EVENT_ST)));
			mEventDetails.setHireEventZipCodeCode(StringUtils.trim(results.getString(HIRE_EVENT_ZIP)));
			mEventDetails.setHireEventTypeIndicator(StringUtils.trim(results.getString(HIRE_EVENT_TYPE)));
			mEventDetails.setEmgrHumanResourcesAssociateId(StringUtils.trim(results.getString(HIRE_EVENT_MGR_ASSOCIATE_ID)));
		} // end if(results.next())
	} // end function readHiringEventDetails()
	
	@QuerySelector(name=READ_USER_EMAIL_DATA, operation=QueryMethod.getResult)
	public void readEnterpriseEmailAddressAndSystemUserDetails(Results results, Query query, Inputs inputs) throws QueryException
	{	
		// there should only be a single record returned
		if(results.next())
		{
			//Should not happen, normal flow is to run readPartyContactMechanismRoleAndSystemUserDetails first. But if it does.
			if (mEventMgrDetails == null) {
				mEventMgrDetails = new HiringEventMgrTO();
			}
			mEventMgrDetails.setEmail(StringUtils.trim(results.getString(EMAIL_ADDRESS)));			
		} // end if(results.next())
	} // end function readEnterpriseEmailAddressAndSystemUserDetails()
	
	@QuerySelector(name=READ_PARTICIPATING_STORE_DATA, operation=QueryMethod.getResult)
	public void readHumanResourcesStoreEmploymentRequisitionAndEventStoreDetails(Results results, Query query, Inputs inputs) throws QueryException
	{	
		// initialize the collection
		mParticipatingStores = new ArrayList<HiringEventsStoreDetailsTO>();

		// loop the results and add the objects to the return collection
		HiringEventsStoreDetailsTO participatingStores = null;
		while(results.next())
		{
			participatingStores = new HiringEventsStoreDetailsTO();
			participatingStores.setStrNum(StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
			participatingStores.setStrName(StringUtils.trim(results.getString(HR_SYS_STR_NAME)));
			participatingStores.setEventReqnCalId(results.getInt(REQN_CAL_ID));
			participatingStores.setActiveReqCount(results.getInt(ACTV_REQN_CNT));
			participatingStores.setHireEventId(results.getInt(HIRE_EVENT_ID));
			if (results.getBoolean(HIRE_EVENT_SITE_FLG)) {
				participatingStores.setEventSiteFlg("Y");
			} else {
				participatingStores.setEventSiteFlg("N");
			}
			if (results.getInt(ACTV_REQN_CNT) > 0) {
				participatingStores.setStoreDeleteAllowed(false);
			} else {
				participatingStores.setStoreDeleteAllowed(true);
			}

			// add the  to the collection
			mParticipatingStores.add(participatingStores);
		} // end while(results.next())
	} // end function readHumanResourcesStoreEmploymentRequisitionAndEventStoreDetails()	
	
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
	@QuerySelector(name = READ_REQUISITION_CALENDAR_HIRING_EVENT, operation = QueryMethod.getResult)
	public void readHumanResourcesHiringEventRequisitionCalendar(Results results, Query query, Inputs inputs) throws QueryException
	{		
			if(results.next())
			{
				mCalendar = new RequisitionCalendarTO();
				mCalendar.setRequisitionCalendarId(results.getInt(REQN_CAL_ID));
				mCalendar.setCreateTimestamp(results.getTimestamp(CRT_TS));
				mCalendar.setCreateSystemUserId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
				mCalendar.setLastUpdateSystemUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
				mCalendar.setLastUpdateTimestamp(results.getTimestamp(LAST_UPD_TS));
				
				//The requisitionCalendarDescription is used to hold multiple pieces of data with ~|~ as the delimiter
				//Field 0 = Event Created by Store Number
				//Field 1 = Event Description
				String[] splitField = StringUtils.trim(results.getString(REQN_CAL_DESC)).split("~\\|~");
				mCalendar.setEventCreatedByStore(splitField[0]);
				mCalendar.setRequisitionCalendarDescription(splitField[1]);
				
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

	} // end function readHumanResourcesHiringEventRequisitionCalendar()
	
	/**
	 * get the hiring event manager details object
	 * 
	 * @return hiring event manager details object
	 */
	public HiringEventMgrTO getEventMgrDetails()
	{
		return mEventMgrDetails;
	} // end function getEventMgrDetails()
	
	/**
	 * get the hiring event detail object
	 * 
	 * @return hiring event detail object
	 */
	public HiringEventDetailTO getEventDetails()
	{
		return mEventDetails;
	} // end function getEventDetails()	
	
	/**
	 * Get the collection of Participating Store objects
	 * 
	 * @return collection of Participating Store objects
	 */
	public List<HiringEventsStoreDetailsTO> getParticipatingStores()
	{
		return mParticipatingStores;
	} // end function getParticipatingStores()	
	
	/**
	 * Get the hiring event requisition calendar object
	 * 
	 * @return the hiring event requisition calendar object
	 */
	public RequisitionCalendarTO getCalendar()
	{
		return mCalendar;
	} // end function getCalendar()	
} // end class HiringEventsHandler
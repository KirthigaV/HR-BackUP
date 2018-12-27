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

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.dao.handlers.CalendarHandler;
import com.homedepot.hr.hr.staffingforms.dto.AvailabilityBlock;
import com.homedepot.hr.hr.staffingforms.dto.DaySummary;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionScheduleSlotDtl;
import com.homedepot.hr.hr.staffingforms.dto.SlotCounter;
import com.homedepot.hr.hr.staffingforms.dto.StatusSlotCountDtl;
import com.homedepot.hr.hr.staffingforms.dto.StatusSlotSummary;
import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;

import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.monitoring.StaffingFormsApplLogMessage;
import com.homedepot.hr.hr.staffingforms.util.StringUtils;
import com.homedepot.hr.hr.staffingforms.util.Utils;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.exceptions.UpdateException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains methods specific to store calendars
 */
public class CalendarManager implements Constants, DAOConstants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(CalendarManager.class);
	
	// maximum number of insert/update/delete queries to execute during one batch cycle
	private static final int MAX_BATCH_RECORDS = 100;
	
	// seed for number of interview slots for a given date/time
	private static final int SLOT_COUNT_SEED = 0;
	
	// sequence number seed for a given date/time
	private static final short SEQ_NBR_SEED = 0;
	
	// message returned by the DAO selector when a delete fails
	private static final String NO_ROWS_FOUND_MSG = " No Rows Found for Input Record :  ";
	
	private static final short HIRING_EVENT_TYPE_CD = 100;	
	
	/**
	 * This method is used to get interview slot availability summary details for the calendar id and date range provided
	 * 
	 * @param calendarId the calendar id
	 * @param beginDate the beginning of the date range
	 * @param endDate the ending of the date range
	 * 
	 * @return summary of interview slot availability
	 * 
	 * @throws ValidationException thrown if the calendar id is &lt;= 0, if the begin date is null, if the end date is null,
	 * 							   or if the end date is before the begin date
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static List<DaySummary> getCalendarSummaryForDateRange(int calendarId, Date beginDate, Date endDate) throws ValidationException, QueryException
	{
		long startTime = 0;
		// declared final so it can be accessed from the anonymous inner class that will be used to process the DAO results
		final Map<Date, DaySummary> summaries = new TreeMap<Date, DaySummary>();
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getCalendarSummaryForDateRange(), calendarId: %1$d, beginDate: %2$s, endDate: %3$s",
				calendarId, beginDate, endDate));
		} // end if
		
		try
		{
			// validate the calendar id provided
			ValidationUtils.validateCalendarId(calendarId);
			// validate the end date is not before the begin date
			ValidationUtils.validateBefore(InputField.BEGIN_DATE, beginDate, InputField.END_DATE, endDate);
			
			// prepare the query
			MapStream inputs = new MapStream(READ_CALENDAR_SUMMARY);
			inputs.put(REQN_CAL_ID, calendarId);
			inputs.put(START_BGN_TS, beginDate);
			inputs.put(END_BGN_TS, endDate);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Querying summary data for calendar %1$d from %2$s --> %3$s", calendarId, beginDate, endDate));
			} // end if
			
			// execute the query
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()			
			{				
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
				 */
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					DaySummary summary = null;
					Date date = null;
					
					// iterate the rows returned
					while(results.next())
					{
						date = results.getDate(SLOT_DATE);
						summary = summaries.get(date);
						
						// if the date is not in the map, create a new one
						if(summary == null){ summary = new DaySummary(); }
						
						// populate the summary data
						summary.setDate(date);
						if(!results.wasNull(REQN_SCH_STAT_CD))
						{
							summary.addSlotSummary(new StatusSlotSummary(results.getShort(REQN_SCH_STAT_CD), results.getInt(SLOT_COUNT)));
						} // end if(!results.wasNull(REQN_SCH_STAT_CD))
						
						// add it to the collection
						summaries.put(date, summary);
					} // end while(results.next())
				} // end function readResults()
			}); // end BasicDAO.getResult()
			
			// next have to fill in what is missing, seed a calendar with begin date (zeroing out the time)
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(beginDate);
			calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
			calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
			calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
			calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
			
			/*
			 * now iterate until we reach the end date provided. if the current date is in the collection,
			 * set the day of the week indicator in the summary object. if the current date is not in the 
			 * collection, add an empty summary object (with the date and day of week indicator set)
			 */
			DaySummary summary = null;
			Date date = null;
			
			while(!calendar.getTime().after(endDate))
			{
				date = new Date(calendar.getTimeInMillis());
				summary = summaries.get(date);
				
				// summary does not exist for the date, so add an "empty" one
				if(summary == null)
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Summary for date %1$s not found, adding one", date));
					} // end if
					
					summary = new DaySummary();
					summary.setDate(date);
					summary.setDayOfWeekIndicator(calendar.get(Calendar.DAY_OF_WEEK));
					summaries.put(date, summary);
				} // end if(summary == null)
				else // day summary exists, so just set the day of the week indicator on the summary object
				{
					summary.setDayOfWeekIndicator(calendar.get(Calendar.DAY_OF_WEEK));
				} // end else
				
				// add a day 
				calendar.add(Calendar.DATE, 1);
			} // end while(!calendar.getTime().after(endDate))
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage(String.format("An exception occurred querying summary data for calendar %1$d from %2$s --> %3$s", 
				calendarId, beginDate, endDate)), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getCalendarSummaryForDateRange(), calendarId: %1$d, beginDate: %2$s, endDate: %3$s. Returning %4$d summary objects in %5$.9f seconds",
				calendarId, beginDate, endDate, summaries.size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		// create a list to return
		return new ArrayList<DaySummary>(summaries.values());
	} // end function getCalendarSummaryForDateRange()
	
	/**
	 * Added as part of Flex to HTML Conversion - 12 May 2015
	 * Description: Added to set the formatted CalendarResponse
	 * @param daySummaryList
	 * @return
	 */
	public static List<DaySummary> getFormattedCalendarResponse(List<DaySummary> daySummaryList)
	{
		if(!Utils.isNullList(daySummaryList))
		{
			for(DaySummary daySummary:daySummaryList )
			{
				if(daySummary.getDate()!=null){
					daySummary.setFormattedDate(Utils.convertDateFormat(daySummary.getDate()));
				}
				setSlotCountList(daySummary);
			}
		}
		return daySummaryList;
	}
	/**
	 * Added as part of Flex to HTML Conversion - 2 June 2015
	 * Description: Added to set the slotCountList
	 * @param daySummary
	 */
	public static void setSlotCountList(DaySummary daySummary)
	{
		List<StatusSlotSummary> statusSlotSummaryList = daySummary.getSlotSummaries();
		
		if(!Utils.isNullList(statusSlotSummaryList))
		{
			List<StatusSlotCountDtl> statusSlotCountList = new ArrayList<StatusSlotCountDtl>();
			StatusSlotCountDtl statusSlotCountDtl = new StatusSlotCountDtl();
			
			for(StatusSlotSummary slotSummary: statusSlotSummaryList){
				setSlotCount(slotSummary,statusSlotCountDtl);
			}
			statusSlotCountList.add(statusSlotCountDtl);
			daySummary.setmSlotCountDtls(statusSlotCountList);
		}
	}
	
	/**
	 * Added as part of Flex to HTML Conversion - 2 June 2015
	 * Description: Added to set the available/reserved/booked slot details
	 * @param statusSlotSummary
	 * @param statusSlotCountDtl
	 */
	public static void setSlotCount(StatusSlotSummary statusSlotSummary, StatusSlotCountDtl statusSlotCountDtl)
	{
		short statusCode = statusSlotSummary.getStatusCode();
		int slotCount = statusSlotSummary.getSlotCount();
		
		if(statusCode==INTRW_SLOT_AVAILABLE){
			statusSlotCountDtl.setAvailabeCount(slotCount);
		}else if(statusCode==INTRW_SLOT_RESERVED){
			statusSlotCountDtl.setReservedCount(slotCount);
		}else if(statusCode==INTRW_SLOT_BOOKED){
			statusSlotCountDtl.setBookedCount(slotCount);
		}
	}
	/**
	 * This method gets calendar details for the calendar id and date provided
	 * 
	 * @param calendarId the calendar id
	 * @param date the date
	 * 
	 * @return collection of RequisitionSchedule objects for the calendar id and date provided
	 * 
	 * @throws ValidationException thrown if the calendar id provided is &lt;= 0 or the date provided is null
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static List<RequisitionSchedule> getCalendarDetailsForDate(final int calendarId, Date date) throws ValidationException, QueryException
	{
		long startTime = 0;
		// declared final so it can be accessed from the anonymous inner class that will be used to process the DAO results
		final List<RequisitionSchedule> blocks = new ArrayList<RequisitionSchedule>();
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getCalendarDetailsForDate(), calendarId: %1$d, date: %2$s", calendarId, date));
		} // end if
		
		try
		{
			// validate the calendar id provided
			ValidationUtils.validateCalendarId(calendarId);
			// validate the date provided is not null
			ValidationUtils.validateNotNull(InputField.DATE, date);
			
			// prepare the query
			MapStream inputs = new MapStream(READ_CALENDAR_DETAILS);
			inputs.put(REQN_CAL_ID, calendarId);
			inputs.put(START_BGN_TS, Timestamp.valueOf(date.toString() + " 00:00:00"));
			inputs.put(END_BGN_TS, Timestamp.valueOf(date.toString() + " 23:59:59"));
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Querying detail data for calendar %1$d on %2$s", calendarId, date));
			} // end if
			
			// execute the query
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
				 */
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					RequisitionSchedule block = null;
					
					// while there are more records
					while(results.next())
					{
						// populate the schedule block data
						block = new RequisitionSchedule();
						block.setCalendarId(calendarId);
						block.setBgnTs(results.getTimestamp(BGN_TS));
						block.setSeqNbr(results.getShort(SEQ_NBR));
						block.setCrtTs(results.getTimestamp(CRT_TS));
						block.setCrtSysUsrId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
						block.setLastUpdTs(results.getTimestamp(LAST_UPD_TS));
						block.setLastUpdSysUsrId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
						block.setStrNbr(StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
						block.setCandidateName(StringUtils.trim(results.getString(CAND_NM)));
						if (!results.wasNull(HIRE_EVNT_REQN_STR)) {
							block.setRequisitionLocation(StringUtils.trim(results.getString(HIRE_EVNT_REQN_STR)));
						}
						
						if (results.wasNull(ASSOCIATE_ID) && !results.wasNull(HR_PHN_SCRN_ID)) {
							//Associate Id = null then candidate is External
							block.setCandidateType("E");
						} else if (!results.wasNull(ASSOCIATE_ID) && !results.wasNull(HR_PHN_SCRN_ID)) {
							block.setCandidateType("I");
						}
						
						if(!results.wasNull(REQN_SCH_STAT_CD))
						{
							block.setReqnSchStatCd(results.getShort(REQN_SCH_STAT_CD));
						} // end if(!results.wasNull(REQN_SCH_STAT_CD))
						
						block.setReqnSchRsrvTs(results.getTimestamp(REQN_SCH_RSRV_TS));

						if(!results.wasNull(HR_PHN_SCRN_ID))
						{
							block.setPhnScrnId(results.getInt(HR_PHN_SCRN_ID));
						} // end if(!results.wasNull(HR_PHN_SCRN_ID))
						
						// add it to the collection
						blocks.add(block);
					} // end while(results.next())
				} // end function readResults()
			}); // end BasicDAO.getResult()
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage(String.format("An exception occurred querying detail data for calendar %1$d on date %2$s", 
				calendarId, date)), qe);
			// throw the exception
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getCalendarDetailsForDate(), calendarId: %1$d, date: %2$s. Returning %3$d blocks in %4$.9f seconds",
				calendarId, date, blocks.size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return blocks;
	} // end function getCalendarDetailsForDate()
	
	/**
	 * Added as part of Flex to HTML Conversion - 12 May 2015
	 * Description: This method is used to set the available/reserved/booked details for all the slots in a day
	 * @param reqSchList
	 * @return
	 */
	public static List<RequisitionScheduleSlotDtl> getReqSchSlotDtls(List<RequisitionSchedule> reqSchList)
	{
		List<RequisitionScheduleSlotDtl> reqSchSlotDtlList= new ArrayList<RequisitionScheduleSlotDtl>();
		List<String> timeSlots = Utils.getTimeSlots();
		
		for(String time: timeSlots)
		{
			RequisitionScheduleSlotDtl reqSchSlotDtl = new RequisitionScheduleSlotDtl();
			List<RequisitionSchedule> reqSchfFinalList = new ArrayList<RequisitionSchedule>();
			reqSchSlotDtl.setSlotTime(time);
			
			for(RequisitionSchedule reqSch :  reqSchList)
			{
				if(isSameScheduleTime(reqSch, time)){
					reqSchfFinalList.add(reqSch);
				}
			}
			if(!Utils.isNullList(reqSchfFinalList))
			{
				reqSchSlotDtl.setReqSchList(reqSchfFinalList);
			}
			reqSchSlotDtlList.add(reqSchSlotDtl);
		}
		return reqSchSlotDtlList;
	}
	
	/**
	 * Added as part of Flex to HTML Conversion - 2 June 2015
	 * Description: This method is used to whether the give time and the requisition schedule time is same or not
	 * @param reqSch
	 * @param time
	 * @return
	 */
	private static boolean isSameScheduleTime(RequisitionSchedule reqSch, String time)
	{
		if(reqSch.getCrtTs()!=null)
		{
			String crtTime = reqSch.getCrtTs().toString().trim();
			if(!Utils.isNullString(crtTime) && crtTime.length()>=TIMESTAMP_LENGTH){
				String schTime = reqSch.getBgnTs().toString().substring(TIMESTAMP_TIME_POSITION, TIMESTAMP_LENGTH);
				if(time.equals(schTime)){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * This method adds interview slots derived from the availability blocks provided
	 * 
	 * @param blocks collection of availability blocks
	 * 
	 * @return number of interview slots inserted
	 * 
	 * @throws ValidationException thrown if the block collection provided is null or empty,
	 * 							   if any of the blocks in the collection are null, if the calendar
	 * 							   id on any block is &lt;= 0, if the begin time or end time of any
	 * 							   block is null, if the end time is prior to the begin time on any 
	 * 							   block, if the store number provided for any block is null, empty
	 * 							   or not a 4 digit value, if the number of interviewers or number
	 * 							   of re-occurring weeks for any block is &lt;= 0, or if the total
	 * 							   number of slots being inserted exceeds 680, or the store number
	 * 							   provided is not valid
	 * @throws QueryException thrown if an exception occurs inserting the slots into the database
	 */
	public static int addAvailability(final List<AvailabilityBlock> blocks) throws ValidationException, QueryException
	{
		long startTime = 0;
		// the number of slots requested for inserted
		int slotsRequested = 0;
		// create a handler object that will be used to process the results of the insert(s)
		CalendarHandler resultsHandler = new CalendarHandler();
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering addAvailability(), %1$d blocks provided", (blocks == null ? 0 : blocks.size())));
		} // end if
		
		try
		{
			// validate the collection of slots provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.AVAILABILITY_BLOCK, blocks);
			
			/*
			 * date/times that will be used to determine if the database already has > 20 slots for the given
			 * date and time provided. These calendar objects will be used to create timestamp objects later that
			 * the DAO selectors will use to pull max threshold dates
			 */
			Calendar rangeBegin = null;
			Calendar rangeEnd = null;
			
			Calendar tempEnd = Calendar.getInstance();
			
			/* 
			 * collection of unique store numbers, declared final so it can be accessed from the anonymous inner class
			 * we will use to execute queries
			 */
			final List<String> storeNumbers = new ArrayList<String>();
			
			// iterate each block provided
			for(AvailabilityBlock block : blocks)
			{
				// validate the block is not null
				ValidationUtils.validateNotNull(InputField.AVAILABILITY_BLOCK, block);
				// validate the block calendar id is valid
				ValidationUtils.validateCalendarId(block.getCalendarId());
				// validate the blocks time stamps (not null and begin is before end)
				ValidationUtils.validateTimestampBefore(InputField.BEGIN_TIME, block.getBgnTs(), InputField.END_TIME, block.getEndTs());
				// validate the block store number
				ValidationUtils.validateStoreNumber(block.getStoreNbr());
				// validate the blocks number of interviewers is in an acceptable range
				ValidationUtils.validateInRange(InputField.NUMBER_OF_INTERVIEWERS, block.getNumIntrvwrs(), MIN_INTERVIEWERS, MAX_INTERVIEWERS);
				// validate the blocks number of weeks re-occurring is in an acceptable range
				ValidationUtils.validateInRange(InputField.NUMBER_OF_RECURRING_WEEKS, block.getWeeksRecurring(), MIN_RECURRING_WEEKS, MAX_RECURRING_WEEKS);
				
				// determine if the store number for this block is already in the list of stores that has to be validated, if not add it
				if(!storeNumbers.contains(block.getStoreNbr()))
				{
					storeNumbers.add(block.getStoreNbr().trim());
				} // end if(!storeNumbers.contains(block.getStoreNbr()))
				
				// use the values to add increment the slot total
				slotsRequested += (int)(((block.getEndTs().getTime() - block.getBgnTs().getTime()) / SLOT_DURATION_IN_MS) * block.getNumIntrvwrs() * block.getWeeksRecurring());
				
				// if range begin is null, initialize it with the block's begin timestamp
				if(rangeBegin == null)
				{
					rangeBegin = Calendar.getInstance();
					rangeBegin.setTimeInMillis(block.getBgnTs().getTime());
				} // end if(rangeBegin == null)
				else if(rangeBegin.getTime().after(block.getBgnTs()))
				{
					// if the current value of range begin is after the block's begin time
					rangeBegin.setTimeInMillis(block.getBgnTs().getTime());
				} // end else if(rangeBegin.getTime().after(block.getBgnTs()))

				// calculate the latest time this block represents
				tempEnd.setTimeInMillis(block.getEndTs().getTime());
				// add the correct number of week(s) to get to the latest possible date
				tempEnd.add(Calendar.DATE, ((block.getWeeksRecurring() - 1) * tempEnd.getActualMaximum(Calendar.DAY_OF_WEEK)));

				// if range end is null, initialize it with tempEnd
				if(rangeEnd == null)
				{
					rangeEnd = Calendar.getInstance();
					rangeEnd.setTimeInMillis(tempEnd.getTimeInMillis());
				} // end if(rangeEnd == null)
				else if(rangeEnd.before(tempEnd))
				{
					rangeEnd.setTimeInMillis(tempEnd.getTimeInMillis());
				} // end else if(rangeEnd.before(tempEnd))
			} // end for(AvailabilityBlock block : blocks)
						
			// verify the number of slots being inserted is below the maximum allowed in a single request
			ValidationUtils.validateLessThan(InputField.INTERVIEW_SLOT, slotsRequested, MAX_SLOT_THRESHOLD);
			
			// create final timestamp objects with the range begin/end times so they can be accessed by the anonymous inner class being used to execute queries
			final Timestamp rangeBgnTs = new Timestamp(rangeBegin.getTimeInMillis());
			final Timestamp rangeEndTs = new Timestamp(rangeEnd.getTimeInMillis());
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Add request made with %1$d slots, rangeBegin: %2$s --> rangeEnd: %3$s", slotsRequested, rangeBgnTs, rangeEndTs));
			} // end if			
			
			// get an instance of the QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);

			// create a DAO connection handler that can be used to execute the inserts in a transaction
			UniversalConnectionHandler handler = new UniversalConnectionHandler(true, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
                public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
                {
					// get the connection
					DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					// get the query
					Query query = connection.getQuery();
					// cast the handler to the correct type
					CalendarHandler resultsHandler = (CalendarHandler)handler;
					
					// first make sure all of the store numbers provided are valid
					for(String storeNumber : storeNumbers)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Validating store number %1$s", storeNumber));
						} // end if(mLogger.isDebugEnabled())
						
						if(!StoreManager.isValidStore(storeNumber))
						{
							mLogger.error(String.format("Store %1$s is not valid", storeNumber));
							throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNumber));
						} // end if(!StoreManager.isValidStore(storeNumber))
					} // end for(String storeNumber : storeNumbers)					
					
					/*
					 * next need to execute a query to get all slots within the date range provided including two counts (current number
					 * of slots for the given date range and the maximum sequence number for each slot in the date range). We will 
					 * programmatically keep track of slot counts and max sequence number to avoid the overhead of constantly querying
					 * the database for the values. doing this will improve performance, however, there is a chance (although a small one)
					 * that multiple updates to the same calendar/date could be occurring simultaneously. If this occurs, one of the clients
					 * will fail. If this becomes a regular occurrence, we will need to adjust this logic to do counts/max sequence number mining
					 * prior to doing each insert which will affect performance quite a bit for larger inserts.
					 */
					MapStream inputs = new MapStream(READ_REQN_SCHED_SLOT_COUNT);
					// grab the calendar id from the first slot (already validated not null)
					inputs.put(REQN_CAL_ID, blocks.get(0).getCalendarId());
					inputs.put(START_BGN_TS, rangeBgnTs);
					inputs.put(END_BGN_TS, rangeEndTs);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying for interview slots for calendar %1$d from %2$s --> %3$s",
							blocks.get(0).getCalendarId(), rangeBgnTs, rangeEndTs));
					} // end if
					// execute the query
					query.getResult(connection, resultsHandler, inputs);
					
					// get the counter collection from the results handler
					Map<Timestamp, SlotCounter> slotCounters = resultsHandler.getSlotCounters();
					
					// local calendars so time can be adjusted without changing the original values
					Calendar localBegin = Calendar.getInstance();
					Calendar localEnd = Calendar.getInstance();
							
					// next prepare to do the inserts
					inputs.clear();
					inputs.setSelectorName(INSERT_CALENDAR_SLOT);
					// collection of insert values
					List<Map<String, Object>> batchValues = new ArrayList<Map<String,Object>>();
					// map of values for a given interview slot
					Map<String, Object> slotValues = null;
					// date/time the slot will begin
					Timestamp beginTs = null;

					// iterate the availability blocks
					for(AvailabilityBlock block : blocks)
					{
						// iterate the interviewer count for the current block
						for(short interviewer = 0; interviewer < block.getNumIntrvwrs(); interviewer++)
						{
							// iterate the recurring number of weeks provided
							for(short weekCount = 0; weekCount < block.getWeeksRecurring(); weekCount++)
							{
								// seed the begin time
								localBegin.setTime(block.getBgnTs());
								// add the correct number of days based on the week count
								localBegin.add(Calendar.DATE, (localBegin.getActualMaximum(Calendar.DAY_OF_WEEK) * weekCount));
								// seed the end time
								localEnd.setTime(block.getEndTs());
								// add the correct number of days based on the week count
								localEnd.add(Calendar.DATE, (localBegin.getActualMaximum(Calendar.DAY_OF_WEEK) * weekCount));
								
								// iterate until we reach end time
								while(localBegin.before(localEnd))
								{
									// capture the begin time of the slot
									beginTs = new Timestamp(localBegin.getTimeInMillis());
									
									// check to see if this slot already exists in the slot threshold map, if not add it with a value of 0
									if(!slotCounters.containsKey(beginTs))
									{
										slotCounters.put(beginTs, new SlotCounter(SLOT_COUNT_SEED, SEQ_NBR_SEED));
									} // end if(!slotCounters.containsKey(beginTs))
									
									// check to see if the number of slots in the threshold map exceeds the maximum allowed for a single date/time
									if(slotCounters.get(beginTs).getSlotCount() < MAX_SINGLE_SLOT_THRESHOLD)
									{
										if(mLogger.isDebugEnabled())
										{
											mLogger.debug(String.format("%1$s is below threshold, adding it to batch", beginTs));
										} // end if

										// set the slot specifics
										slotValues = new HashMap<String, Object>();
										slotValues.put(REQN_CAL_ID, block.getCalendarId());
										slotValues.put(BGN_TS, beginTs);
										slotValues.put(SEQ_NBR, slotCounters.get(beginTs).getNextSeq());
										slotValues.put(HR_SYS_STR_NBR, block.getStoreNbr());
										slotValues.put(REQN_SCH_STAT_CD, INTRW_SLOT_AVAILABLE);
										slotValues.put(REQN_SCH_RSRV_TS, null);
										slotValues.put(HR_PHN_SCRN_ID, null);									
	
										// add the map of row specific values to the batch values list
										batchValues.add(slotValues);
																			
										// if we have details about 100 requisition schedule rows, execute the batch insert
										if(batchValues.size() == MAX_BATCH_RECORDS)
										{
											// setup the batch
											inputs.put(HR_REQN_SCHED_LIST, batchValues);
											
											if(mLogger.isDebugEnabled())
											{
												mLogger.debug(String.format("Executing batch insert with %1$d records", batchValues.size()));
											} // end if
											
											// execute the inserts
											query.insertObject(connection, handler, inputs);
											
											// clear the batch values and continue on the loop
											batchValues.clear();
										} // end if(batchValues.size() == MAX_BATCH_RECORDS)
					
										// increment the slot count for the slot by 1
										slotCounters.get(beginTs).incrementSlotCount();
									} // end if(slotCounters.get(beginTs).getSlotCount() < MAX_SINGLE_SLOT_THRESHOLD)

									// add 30 minutes to the begin time
									localBegin.add(Calendar.MINUTE, SLOT_DURATION_IN_MINS);
								} // end while(localBegin.before(localEnd))
							} // end for(short weekCount = 0; weekCount < block.getWeeksRecurring(); weekCount++)							
						} // end for(short interviewer = 0; interviewer < block.getNumIntrvwrs(); interviewer++)
					} // end for(AvailabilityBlock block : blocks)
					
					// if there are still records in the batch values collection, insert them
					if(batchValues.size() > 0)
					{
						// setup the last batch
						inputs.put(HR_REQN_SCHED_LIST, batchValues);
						
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Executing LAST batch insert with %1$d records", batchValues.size()));
						} // end if
						
						// execute the inserts
						query.insertObject(connection, handler, inputs);						
					} // end if(batchValues.size() > 0)
                } // end function handleQuery()				
			}; // end UniversalConnectionHandler()
			
			// execute the inserts
			handler.execute();
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage("An exception occurred adding availability to calendar"), qe);
			// throw the exception
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.error(String.format("Exiting addAvailability(). Inserted %1$d interview slots in %2$.9f seconds", resultsHandler.getRecordsAffected(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// return the number of records inserted
		return resultsHandler.getRecordsAffected();
	} // end function addAvailabilty()
	
	/**
	 * This method removes availability from a requisition calendar
	 * 
	 * @param slots the interview slots to be removed
	 * 
	 * @return the number of interview slots removed
	 * 
	 * @throws ValidationException thrown if the slots collection is null or empty, the calendar id of a slot
	 * 							   is &lt;= 0, the begin time of a slot is null, the sequence number of a slot
	 * 							   is &lt;= 0, the requisition schedule status of a slot is invalid, or if the total
	 * 							   number of slots being deleted exceeds 680
	 * @throws QueryException thrown if an exception occurs deleting the slots from the database
	 */
	public static int removeAvailability(List<RequisitionSchedule> slots) throws ValidationException, QueryException
	{
		long startTime = 0;
		int recordsDeleted = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering removeAvailability(), %1$d interview slots provided", (slots == null ? 0 : slots.size())));
		} // end if
		
		try
		{
			// validate the collection of RequisitionSchedule objects is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.INTERVIEW_SLOT, slots);
			
			// iterate each slot and make sure the values required are not null or empty
			for(RequisitionSchedule slot : slots)
			{
				// validate the schedule is not null
				ValidationUtils.validateNotNull(InputField.INTERVIEW_SLOT, slot);
				// validate the calendar id
				ValidationUtils.validateCalendarId(slot.getCalendarId());
				// validate the begin timestamp is not null
				ValidationUtils.validateNotNull(InputField.BEGIN_TIME, slot.getBgnTs());
				// validate the sequence number provided
				ValidationUtils.validateGreaterThan(InputField.SEQUENCE_NUMBER, slot.getSeqNbr(), MIN_SEQUENCE_NUMBER);
				// validate the schedule status code
				ValidationUtils.validateInRange(InputField.REQUISITION_SCHEDULE_STATUS_CODE, slot.getReqnSchStatCd(), INTRW_SLOT_AVAILABLE, INTRW_SLOT_BOOKED);
			} // end for(RequisitionSchedule slot : slots)
			
			// verify the number of slots being deleted is below the maximum allowed in a single request
			ValidationUtils.validateLessThan(InputField.INTERVIEW_SLOT, slots.size(), MAX_SLOT_THRESHOLD);		
			
			/*
			 * we want to batch up the deletes, to prevent this from being network intensive if there are a large
			 * number of records. To do this we will break the list into more manageable "batches" of 100 records 
			 * and perform batch deletes on these smaller "batches". Each batch of records will run in it's own
			 * transaction AND has the logic to handle any slot that was originally available to be deleted and
			 * due to IVR or someone else booking it is not any longer.
			 */
			int totalBatches = (slots.size() / MAX_BATCH_RECORDS) + ((slots.size() % MAX_BATCH_RECORDS > 0) ? 1 : 0);
			int bpos = 0;
			int epos = 0;
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Total # of batches to delete %1$d", totalBatches));
			} // end if
			
			List<RequisitionSchedule> batchList = null;			
			
			for(int i = 0; i < totalBatches; i++)
			{
				// set the beginning position based on which batch we're on
				bpos = (i * MAX_BATCH_RECORDS);						
				// set the ending position based on which batch we're on
				epos = (i == (totalBatches - 1) ? slots.size() : bpos + MAX_BATCH_RECORDS);

				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("bpos = %1$d, epos = %2$d", bpos, epos));
				} // end if

				// grab this subsection of the main collection (doing a new so I don't delete items from the input list) 
				batchList = new ArrayList<RequisitionSchedule>(slots.subList(bpos, epos));
				// invoke the logic to delete and increment the number of records deleted
				recordsDeleted += performBatchDelete(batchList);
			} // end for(int i = 0; i < totalBatches; i++)
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage("An exception occurred removing availability"), qe);
			// throw the exception
			throw qe;
		} // end catch	
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting removeAvailability(). Successfully removed %1$d interview slots in %2$.9f seconds", 
				recordsDeleted, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return recordsDeleted;
	} // end function removeAvailability()
	
	/*
	 * Recursive method that performs batch deletes of the slots provided. If an exception occurs deleting
	 * a batch of records, the "bad" record is removed from the list and the delete is tried again.
	 *  
	 * @param slots the slots to remove
	 * 
	 * @return the number of slots removed
	 * 
	 * @throws QueryException thrown if an actual database error occurs deleting the slots
	 * 						  from the database
	 */
	private static int performBatchDelete(List<RequisitionSchedule> slots) throws QueryException
	{
		long startTime = 0;
		int rowsAffected = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering performBatchDelete(), %1$d slots provided", slots.size()));
		} // end if
		
		/*
		 * this method includes no validation logic. It is invoked by the removeAvailability function which would 
		 * have already performed validation prior to invoking this method.
		 */
		final List<Map<String, Object>> batchSlots = new ArrayList<Map<String,Object>>();
		Map<String, Object> slotValues = null;

		// iterate the slot values provided and populate the batch slots collection
		for(RequisitionSchedule slot : slots)
		{
			// set the slot specifics
			slotValues = new HashMap<String, Object>();
			slotValues.put(REQN_CAL_ID, slot.getCalendarId());
			slotValues.put(BGN_TS, slot.getBgnTs());
			slotValues.put(SEQ_NBR, slot.getSeqNbr());
			slotValues.put(REQN_SCH_STAT_CD, slot.getReqnSchStatCd());
			
			// add the map of row specific values to the batch values list
			batchSlots.add(slotValues);
		} // end for(RequisitionSchedule slot : slots)
				
		// get an instance of the QueryManager
		QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
		// get a DAO connection
		DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
		// create a handler object that will be used to process the results
		CalendarHandler resultsHandler = new CalendarHandler();
		
		// create a DAO connection handler that can be used to execute the inserts in a transaction
		UniversalConnectionHandler handler = new UniversalConnectionHandler(true, resultsHandler, connection)
		{
			/*
			 * (non-Javadoc)
			 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
			 */
			@Override
            public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
            {
				// get the connection
				DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
				// get the query
				Query query = connection.getQuery();
				
				// query inputs
				MapStream inputs = new MapStream(DELETE_CALENDAR_SLOT);
				// setup the batch values
				inputs.put(HR_REQN_SCHED_LIST, batchSlots);
				
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Executing delete batch containing %1$d records", batchSlots.size()));
				} // end if
				
				// execute the batch of queries
				query.deleteObject(connection, handler, inputs);
            } // end function handleQuery()
		}; // end UniversalConnectionHandler()

		do
		{
			try
			{
				// execute the batch queries
				handler.execute();
				// the deletes were successful, set the rows deleted for the return
				rowsAffected = resultsHandler.getRecordsAffected();
			} // end try
			catch(QueryException qe)
			{
				/*
				 * check the QueryException. If the QueryException was due to a row not being deleted,
				 * remove that row from the collection and try again.
				 */
				int bpos = -1;
				int badRecord = 0;

				// if the exception message is not null
				if(qe.getMessage() != null)
				{
					// search the message for the NoRowsFound indicator
					bpos = qe.getMessage().indexOf(NO_ROWS_FOUND_MSG); 
				} // end if(qe.getMessage() != null)
				
				// the NoRowsFound indicator was found
				if(bpos != -1)
				{
					// increment the position by the length of the message
					bpos += NO_ROWS_FOUND_MSG.length();

					try
					{
						// get the record number that was in error
						badRecord = Integer.parseInt(qe.getMessage().substring(bpos));
						
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Removing \"bad record\" %1$d", badRecord));
						} // end if
						
						// remove the item from the list (and try again)
						batchSlots.remove(badRecord);
					} // end try
					catch(NumberFormatException nfe)
					{
						// can't determine what to do here, so rollback and throw the exception
						throw qe;
					} // end catch
				} // end if(bpos != -1)
				else
				{
					// can't say for sure it was a NoRowsFound condition, so rollback and throw the exception
					throw qe;
				} // end else
			} // end catch
		} // end do
		while((rowsAffected == 0 && batchSlots.size() > 0));
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting performBatchDelete(), successfully deleted %1$d records in %2$.9f seconds", rowsAffected, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return rowsAffected;
	} // end function performBatchDelete()
	
	/**
	 * This method gets all active requisition calendars for the store number provided
	 * 
	 * @param storeNumber the store number
	 * 
	 * @return a map of all the active requisition calendars for the store number provided
	 * 
	 * @throws ValidationException thrown if the store number provided is null, empty, not a four digit value or is not in the database
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static Map<Integer, RequisitionCalendar> getActiveCalendarsForStore(final String storeNumber) throws ValidationException, QueryException
	{
		long startTime = 0;

		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getActiveCalendarsForStore(), storeNumber: %1$s", storeNumber));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results
		CalendarHandler resultsHandler = new CalendarHandler();
		
		try
		{
			// validate the store number provided is valid
			ValidationUtils.validateStoreNumber(storeNumber);

			// get an instance of the QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
			
			// create a DAO connection handler that will be used to execute multiple queries on the same connection
			UniversalConnectionHandler handler = new UniversalConnectionHandler(false, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
	            public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
	            {
					// get the connection
					DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					// get the query
					Query query = connection.getQuery();

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Validating store %1$s", storeNumber));
					} // end if
					
					// first want to validate the store number provides is valid
					if(StoreManager.isValidStore(storeNumber))
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Store %1$s is valid, querying for all active calendars", storeNumber));
						} // end if
						
						// next we want to query the database for active calendars for the store number provided
						MapStream inputs = new MapStream(READ_CALENDARS);
						inputs.put(HR_SYS_STR_NBR, storeNumber);
						inputs.put(ACTV_FLG, true);
						
						// execute the query
						query.getResult(connection, handler, inputs);						
					} // end if(StoreManager.isValidStore(storeNumber))
					else
					{
						mLogger.error(String.format("Store %1$s is not valid", storeNumber));						
						// the store is not valid, throw an exception
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNumber));
					} // end else					
	            } // end function handleQuery()
			}; // end UniversalConnectionHandler
			
			// execute the queries
			handler.execute();
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage(String.format("An exception occurred getting active calendars for store %1$s", storeNumber)), qe);
			// throw the exception
			throw qe;
		} // end catch
				
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getActiveCalendarsForStore(), storeNumber: %1$s. Returning %2$d calendars in %3$.9f seconds",
				storeNumber, resultsHandler.getCalendars().size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// return the calendar objects read
		return resultsHandler.getCalendars();
	} // end function getActiveCalendarsForStore()
	
	/**
	 * This method is used to update certain characteristics of a requisition calendar
	 * 
	 * @param reqnCalId the calendar to update
	 * @param lastUpdateTimestamp the date/time that should be on the record to ensure it has not been changed by a different session/user
	 * @param reqnCalDesc calendar name(this is one of the elements that could be updated)
	 * @param activeFlag active flag (this is one of the elements that could be updated)
	 * 
	 * @return number of records updated
	 * 
	 * @throws ValidationException thrown if the calendar id provided is invalid, or if the calendar name provided is null or empty
	 * @throws QueryException thrown if no records are updated OR if an exception occurred updating the database
	 */
	public static int updateCalendar(int reqnCalId, Timestamp lastUpdateTimestamp, String reqnCalDesc, boolean activeFlag) throws ValidationException, QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering updateCalendar(), calendar ID %1$d timestamped %2$s calendar name %3$s active flag %4$b", reqnCalId, lastUpdateTimestamp, reqnCalDesc, activeFlag));
		} // end if
		
		try
		{
			// validate the calendar ID provided			
			ValidationUtils.validateCalendarId(reqnCalId);
			// validate the calendar Name provided is not null
			ValidationUtils.validateNotNullOrEmpty(InputField.CALENDAR_NAME, reqnCalDesc);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("preparing to update calendar %1$d, timestamped %2$s named %3$s, active %4$b", reqnCalId, lastUpdateTimestamp, reqnCalDesc, activeFlag));
			} // end if
			// prepare the query
			MapStream inputs = new MapStream(UPDATE_HR_REQN_CAL);
			inputs.put(REQN_CAL_ID, reqnCalId);
			inputs.put(LAST_UPD_TS, lastUpdateTimestamp);
			inputs.put(REQN_CAL_DESC, reqnCalDesc);
			inputs.put(ACTV_FLG, activeFlag);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Updating calendar %1$d, last updated %2$s named: %3$s active: %4$b", reqnCalId, lastUpdateTimestamp, reqnCalDesc, activeFlag));
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
						throw new UpdateException(String.format("An exception occurred updating calendar"));
					} // end if(count != 1)
				} // end function notifyUpdate()
			}); // end function updateObject()
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage("An exception occurred updating the calendar"), qe);
			// throw the exception
			throw qe;
		} // end catch	
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting updateCalendar(). Successfully updated calendar id %1$d, last updated %2$s, name %3$s, active %4$b in %5$.9f seconds", 
				reqnCalId, lastUpdateTimestamp, reqnCalDesc, activeFlag, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return 0;
	} // end function updateCalendar()
	
	/**
	 * This method gets all active requisition hiring event calendars for the store number provided
	 * 
	 * @param storeNumber the store number
	 * 
	 * @return a map of all the active requisition calendars for the store number provided
	 * 
	 * @throws ValidationException thrown if the store number provided is null, empty, not a four digit value or is not in the database
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static Map<Integer, RequisitionCalendar> getActiveHiringEventCalendarsForStore(final String storeNumber) throws ValidationException, QueryException
	{
		long startTime = 0;

		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getActiveHiringEventCalendarsForStore(), storeNumber: %1$s", storeNumber));
		} // end if(mLogger.isDebugEnabled())
		
		// create a handler object that will be used to process the results
		CalendarHandler resultsHandler = new CalendarHandler();
		
		try
		{
			// validate the store number provided is valid
			ValidationUtils.validateStoreNumber(storeNumber);

			// get an instance of the QueryManager
			QueryManager queryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			
			// create a DAO connection handler that will be used to execute multiple queries on the same connection
			UniversalConnectionHandler handler = new UniversalConnectionHandler(false, resultsHandler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
	            public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
	            {
					// get the connection
					DAOConnection connection = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					// get the query
					Query query = connection.getQuery();

					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Validating store %1$s", storeNumber));
					} // end if
					
					// first want to validate the store number provides is valid
					if(StoreManager.isValidStore(storeNumber))
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Store %1$s is valid, querying for all active hiring event calendars", storeNumber));
						} // end if
						
						// next we want to query the database for active calendars for the store number provided
						MapStream inputs = new MapStream(READ_REQUISITION_HIRING_EVENTS);
						inputs.put(HR_SYS_STR_NBR, storeNumber);
						inputs.put(ACTV_FLG, true);
						inputs.put(STORE_ACTIVE_FLG, true);
						inputs.put(HR_CAL_TYP_CD, HIRING_EVENT_TYPE_CD);
						
						// execute the query
						query.getResult(connection, handler, inputs);						
					} // end if(StoreManager.isValidStore(storeNumber))
					else
					{
						mLogger.error(String.format("Store %1$s is not valid", storeNumber));						
						// the store is not valid, throw an exception
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNumber));
					} // end else					
	            } // end function handleQuery()
			}; // end UniversalConnectionHandler
			
			// execute the queries
			handler.execute();
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage(String.format("An exception occurred getting active hiring event calendars for store %1$s", storeNumber)), qe);
			// throw the exception
			throw qe;
		} // end catch
				
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getActiveHiringEventCalendarsForStore(), storeNumber: %1$s. Returning %2$d calendars in %3$.9f seconds",
				storeNumber, resultsHandler.getCalendars().size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// return the calendar objects read
		return resultsHandler.getCalendars();
	} // end function getActiveHiringEventCalendarsForStore()	
	
	/**
	 * This method gets the phone screen Id's for the calendar id and date range provided
	 * 
	 * @param calendarId the calendar id
	 * @param date the date
	 * @param date the date2
	 * 
	 * @return collection of RequisitionSchedule objects that contain phone screen id's for the calendar id and date range provided
	 * 
	 * @throws ValidationException thrown if the calendar id provided is &lt;= 0 or the dates provided are null
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static List<RequisitionSchedule> getPhoneScreenIdsByCalendarByDateRange(final int calendarId, Date date, Date date2) throws ValidationException, QueryException
	{
		long startTime = 0;
		// declared final so it can be accessed from the anonymous inner class that will be used to process the DAO results
		final List<RequisitionSchedule> blocks = new ArrayList<RequisitionSchedule>();
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getPhoneScreenIdsByCalendarByDateRange(), calendarId: %1$d, Start Date: %2$s, End Date: %3$s", calendarId, date, date2));
		} // end if
		
		try
		{
			// validate the calendar id provided
			ValidationUtils.validateCalendarId(calendarId);
			// validate the date provided is not null
			ValidationUtils.validateNotNull(InputField.DATE, date);
			// validate the date provided is not null
			ValidationUtils.validateNotNull(InputField.DATE, date2);
			
			// prepare the query
			MapStream inputs = new MapStream(READ_CALENDAR_DETAILS);
			inputs.put(REQN_CAL_ID, calendarId);
			inputs.put(START_BGN_TS, Timestamp.valueOf(date.toString() + " 00:00:00"));
			inputs.put(END_BGN_TS, Timestamp.valueOf(date2.toString() + " 23:59:59"));
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Querying detail data for calendar %1$d ", calendarId));
			} // end if
			
			// execute the query
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
				 */
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					RequisitionSchedule block = null;
					
					// while there are more records
					while(results.next())
					{
						// populate the schedule block data
						block = new RequisitionSchedule();
						block.setCalendarId(calendarId);
						//Only add to collection if a phone screen id is found
						if(!results.wasNull(HR_PHN_SCRN_ID))
						{
							block.setPhnScrnId(results.getInt(HR_PHN_SCRN_ID));
							// add it to the collection
							blocks.add(block);
						} // end if(!results.wasNull(HR_PHN_SCRN_ID))
						
						
					} // end while(results.next())
				} // end function readResults()
			}); // end BasicDAO.getResult()
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new StaffingFormsApplLogMessage(String.format("An exception occurred querying detail data for calendar %1$d on date %2$s", 
				calendarId, date)), qe);
			// throw the exception
			throw qe;
		} // end catch		
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getPhoneScreenIdsByCalendarByDateRange(), calendarId: %1$d, Start Date: %2$s, End Date: %3$s. Returning %4$d blocks in %5$.9f seconds",
				calendarId, date, date2, blocks.size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return blocks;
	} // end function getPhoneScreenIdsByCalendarByDateRange()	
} // end class CalendarManager
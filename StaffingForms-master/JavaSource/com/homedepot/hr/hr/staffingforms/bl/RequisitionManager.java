package com.homedepot.hr.hr.staffingforms.bl;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionManager.java
 * Application: RetailStaffing
 *
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.dao.handlers.RequisitionHandler;
import com.homedepot.hr.hr.staffingforms.dto.Requisition;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.monitoring.StaffingFormsApplLogMessage;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains methods specific to requisitions
 */
public class RequisitionManager implements Constants, DAOConstants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(RequisitionManager.class);

	/** collection of statuses that indicate an interview is scheduled/has been conducted */
	private static final List<Short> INTVW_STATS = new ArrayList<Short>()
	{
        private static final long serialVersionUID = -9076504878881024734L;

		{
			add((short)11); // SCHEDULED
			add((short)12); // STORE SCHEDULED
			add((short)14); // SCHEDULED, NOT SELECTED
		} // end static block
	}; // end INTVW_STATS
	
	/** collection of interview result indicators */
	private static final List<String> INTVW_RSLT_INDS = new ArrayList<String>()
	{
        private static final long serialVersionUID = 2330776755222010317L;

		{
			add("A"); // Acceptable
			add("F"); // Favorable
			add("U"); // Unfavorable
		} // end static block
	}; // end INTVW_RSLT_INDS
	
	
	/**
	 * This method is used to get active requisition data for the store number provided
	 * 
	 * @param storeNumber the store number
	 * @param langCd the language that should be used to retrieve job title descriptions
	 * 
	 * @return list of active requisition objects for the store number provided
	 * 
	 * @throws ValidationException thrown if the store number provided is null, empty, is not a four digit value,
	 * 							   or is not a valid store in the database, or if the language code provided is null,
	 * 							   empty or is not in the correct format
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static List<Requisition> getActiveRequisitionsForStore(final String storeNumber, final String langCd) throws ValidationException, QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getActiveRequisitionsForStore(), storeNumber: %1$s, langCd: %2$s", storeNumber, langCd));
		} // end if
		
		// create a handler object that will be used to process the results of the insert(s)
		RequisitionHandler resultsHandler = new RequisitionHandler();
		
		try
		{
			// validate the store number provided
			ValidationUtils.validateStoreNumber(storeNumber);
			// validate the language code provided
			ValidationUtils.validateLanguageCode(langCd);
			
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
					RequisitionHandler resultsHandler = (RequisitionHandler)handler;
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Validating store %1$s", storeNumber));
					} // end if
					// first validate the store number provided is valid					
					if(StoreManager.isValidStore(storeNumber))
					{					
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Store %1$s is valid, querying for active requisition calendars using language code %2$s", storeNumber, langCd));
						} // end if
						/*
						 * next get a collection of active requisition calendars for the store and hiring event calendars and set them on the results handler. 
						 * These objects will be used to set calendar information on the requisition objects being returned. 
						 */					
						
						//Get Active Calendars for Store
						Map<Integer, RequisitionCalendar> calendars =  CalendarManager.getActiveCalendarsForStore(storeNumber);
						
						//Get Active Hiring Event Calendars for Store
						calendars.putAll(CalendarManager.getActiveHiringEventCalendarsForStore(storeNumber));

						//Set all the Calendars on the results handler
						resultsHandler.setCalendars(calendars);
											
						// next prepare the query to get active requisitions for the store
						MapStream inputs = new MapStream(READ_ACTV_REQNS);
						inputs.put(ACTV_FLG, true);
						inputs.put(HR_SYS_STR_NBR, storeNumber);
						inputs.put(INTVW_STAT_CDS, INTVW_STATS);
						inputs.put(INTVW_STAT_INDS, INTVW_RSLT_INDS);
						inputs.put(LANG_CD, langCd);
						
						// execute the query
						query.getResult(connection, handler, inputs);						
					} // end if(StoreManager.isValidStore(storeNumber))
					else
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Store %1$s is not valid", storeNumber));
						} // end if
						
						// the store is not valid, throw an exception
						throw new ValidationException(InputField.STORE_NUMBER, String.format("Store %1$s is not valid", storeNumber));						
					} // end else
                } // end function handleQuery()
			}; // end UniversalConnectionHandler()
			
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
			mLogger.error(new StaffingFormsApplLogMessage(String.format("An exception occurred querying active requisitions for store %1$s using language code %2$s", storeNumber, langCd)), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getActiveRequisitionsForStore(), storeNumber: %1$s, langCd: %2$s. Returning %3$d active requisitions in %4$.9f seconds",
				storeNumber, langCd, resultsHandler.getRequisitions().size(), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		// return the requisitions
		return resultsHandler.getRequisitions();
	} // end function getActiveRequisitionsForStore()
} // end class RequisitionManager
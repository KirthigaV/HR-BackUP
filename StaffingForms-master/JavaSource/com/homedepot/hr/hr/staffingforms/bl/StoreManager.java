package com.homedepot.hr.hr.staffingforms.bl;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: LocationManager.java
 * Application: RetailStaffing
 *
 */

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.dto.Store;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.monitoring.StaffingFormsApplLogMessage;
import com.homedepot.hr.hr.staffingforms.util.StringUtils;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains methods specific to stores (stores, dc's, etc.)
 */
public class StoreManager implements Constants, DAOConstants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(StoreManager.class);
	/** list of location type codes that should be included when searching for a store */
	private static final List<String> STORE_DETAILS_LOC_TYPES = new ArrayList<String>()
	{
        private static final long serialVersionUID = 1L;

		{
			add("DC");
			add("STR");
		} // end static block
	}; // end of STORE_DETAILS_LOC_TYPES	
	
	/**
	 * This method validates the locationNumber provided (using the getStoreDetails() method)
	 * 
	 * @param storeNumber the store number
	 * 
	 * @return true if the store is valid (found in the database), false otherwise
	 * 
	 * @throws ValidationException thrown if the store number provided is invalid (null, empty, or not a 4 digit value)
	 * @throws QueryException thrown if an exception occurs querying the database
	 * 
	 * @see StoreManager#getStoreDetails(String)
	 */
	public static boolean isValidStore(String storeNumber) throws ValidationException, QueryException
	{
		long startTime = 0;
		boolean valid = false;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering isValidLocation(), storeNumber: %1$s", storeNumber));
		} // end if		

		// invoke the get store details method (if the store is not valid, a null value will be returned)
		Store store = getStoreDetails(storeNumber);
		// setup the response
		valid = (store != null);
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Store %1$s %2$s valid", storeNumber, (valid ? "is" : "is not")));
			
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting isValidLocation(), storeNumber: %1$s. Total time to process request: %2$.9f seconds",
				storeNumber, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return valid;
	} // end function isValidStore()
	
	/**
	 * This method gets details about the store number provided
	 * 
	 * @param storeNumber the store number
	 * @return details about the store number provided
	 * 
	 * @throws ValidationException thrown if the store number provided is invalid (null, empty, or not a 4 digit value)
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static Store getStoreDetails(final String storeNumber) throws ValidationException, QueryException
	{
		long startTime = 0;
		Store store = null;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getStoreDetails(), storeNumber: %1$s", storeNumber));
		} // end if		
		
		try
		{
			// validate the store number provided is not null, empty, and is a numeric value 4 digits long
			ValidationUtils.validateStoreNumber(storeNumber);

			final Date currentDate = new Date(System.currentTimeMillis());
			
			Store strDtls = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
							.setSQL(SQL_GET_STORE_DETAILS)
							.displayAs("Read Store Location Details")
							.input(1, storeNumber) 
							.input(2, currentDate)
							.input(3, currentDate)
							.inClause(0, STORE_DETAILS_LOC_TYPES)
							.debug(mLogger)
							.formatCycles(1)
							.get(Store.class);
			
			store = strDtls;

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
			mLogger.error(new StaffingFormsApplLogMessage(String.format("An exception occurred validating store number %1$s", storeNumber)), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getStoreDetails(), storeNumber: %1$s. Total time to process request: %2$.9f seconds",
				storeNumber, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if		
		
		return store;
	} // end function getStoreDetails()
} // end class LocationManager
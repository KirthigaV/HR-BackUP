package com.homedepot.hr.hr.retailstaffing.model;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.handlers.LocationHandler;
import com.homedepot.hr.hr.retailstaffing.dto.LocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.OrgParamTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
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
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.builder.UnitOfWork;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains business logic methods related to locations (stores/dc/etc). 
 */
public class LocationManager implements Constants, DAOConstants, RetailStaffingConstants
{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(LocationManager.class);
	/** list of location type codes that should be included when searching for a store */
	private static final List<String> STORE_DETAILS_LOC_TYPES = new ArrayList<String>()
	{
        private static final long serialVersionUID = -146183220399922082L;

		{
			add("DC");
			add("STR");
		} // end static block
	}; // end of STORE_DETAILS_LOC_TYPES	
	
	/** list of store manager job titles that should be included when searching for a manager name */
	private static final List<String> MANAGER_JOB_TITLES = new ArrayList<String>()
	{
        private static final long serialVersionUID = -4580370433239948515L;

		{
			add("STRMGR");
			add("SDCGM1");
			add("SDCGM2");
			add("IBDCMG");
			add("IBDCOM");
		} // end static block
	}; // end of MANAGER_JOB_TITLES
	
	/** list of divisions that should be included when validating a location */
	private static final List<String> LOCATION_DIVISIONS = new ArrayList<String>()
	{
		private static final long serialVersionUID = -5687873524503367253L;

		{		
			add("ND");
			add("SD");
			add("WC");
			add("DC");
			add("YW");
			add("RL");
			add("ID");
			add("IB");
			add("KW");
		} // end static block
	}; // end of LOCATION_DIVISIONS	
	
	
	/**
	 * Get details about the store number provided
	 * 
	 * @param storeNumber the store to get details for
	 * @return store details object
	 * 
	 * @throws QueryException thrown if the store number provided is null, if the store number
	 * 						  provided is not found in the database (in the form of a NoRowsFoundException),
	 * 						  or if an exception occurs querying the database.
	 */
	public static StoreDetailsTO getStoreDetails(final String storeNumber) throws QueryException
	{
		long startTime = 0;
		StoreDetailsTO store = null;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getStoreDetails(), storeNumber: %1$s", storeNumber));
		} // end if
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.STORE_NUMBER, storeNumber);
			
			// get an instance of the DAO query manager
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			//get a database connection from the DAO query manager
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);

			// create a new handler object that will be used to process the query results
			LocationHandler resultsHandler = new LocationHandler();
			
			// create a universal connection handler that can be used to execute multiple queries with a single connection (non-transacted)
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, resultsHandler, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
                public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
                {
					DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
					
					// prepare the first query
					MapStream inputs = new MapStream(READ_STORE_DETAILS);
					inputs.put("humanResourcesSystemStoreNumber", storeNumber);
					inputs.put("locationTypeCodeList", STORE_DETAILS_LOC_TYPES);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting store details using store number %1$s", storeNumber));
					} // end if
					
					// execute the first query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, handler, inputs);
					
					// if the first query did not return a result, throw a NoRowsFoundException
					if(((LocationHandler)handler).getStoreDetails() == null)
					{
						// the store was not found
						throw new NoRowsFoundException(ApplicationObject.STORE, String.format("Store %1$s not found", storeNumber));
					} // end if(((LocationHandler)handler).getStoreDetails() == null)
					
					// prepare the second query
					inputs.clear();
					inputs.setSelectorName("readTRPRX000ByInputList");
					inputs.put("tabno", "00049");
					inputs.put("tableKeys", storeNumber);
					inputs.put("startEndDate", new java.sql.Date(System.currentTimeMillis()));
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting timezone fore store number %1$s", storeNumber));
					} // end if
					
					// execute the second query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, handler, inputs);
					
					// prepare the third query
					inputs.clear();
					inputs.setSelectorName("readPersonProfilesTable");
					inputs.put("jobTitleIdList", MANAGER_JOB_TITLES);
					inputs.put("organizationOne", storeNumber);
					inputs.put("statusCode", "A");
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting manager name for store %1$s", storeNumber));
					} // end if
					
					// execute the third query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, handler, inputs);
                } // end function handleQuery()				
			}; // end UniversalConnectionHandler
			
			// execute the queries
			connectionHandler.execute();
			
			// get the store details object that was populated (no null check here, if the store wasn't found then a NoRowsFoundException would have been thrown
			store = resultsHandler.getStoreDetails();
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting details for store number %1$s", storeNumber), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting getStoreDetails(), storeNumber: %1$s. Total time to get store details: %2$.9f seconds.",
				storeNumber, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return store;
	} // end function getStoreDetails()
	
	/**
	 * Validate the store number provided is valid (exists in the database)
	 * 
	 * @param storeNumber the store to validate
	 * @return true if the store is found in the database, false otherwise
	 * 
	 * @throws QueryException thrown if the store number provided is null or
	 * 						  if an exception occurs querying the database.
	 */	
	public static boolean isValidStoreNumber(String storeNumber) throws QueryException
	{		
		long startTime = 0;
		boolean isValid = false;

		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering isValidStoreNumber(), storeNumber: %1$s", storeNumber));
		} // end if
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.STORE_NUMBER, storeNumber);			
			
			// query to get the store details
			StoreDetailsTO store = getStoreDetailsDAO20(storeNumber);
			// if the store is not null, it's a valid store
			isValid = (store != null);
		} // end try
		catch(NoRowsFoundException nfe)
		{
			// this is thrown whenever store details can't be retrieved for the store number provided (so it's not valid)
			isValid = false;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred validating store number %1$s", storeNumber), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting isValidStoreNumber(), storeNumber: %1$s %2$s. Total time to validate store number: %3$.9f seconds.",
				storeNumber, (isValid ? "is valid" : "is not valid"), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if		
		
		return isValid;
	} // end function isValidStoreNumber()
	
	/**
	 * Validate the location number provided is valid (exists in the database)
	 * 
	 * @param locationNumber the location to validate.  Will work with YOW as well
	 * @return true if the store is found in the database, false otherwise
	 * 
	 * @throws QueryException thrown if the store number provided is null or
	 * 						  if an exception occurs querying the database.
	 */	
	public static boolean isValidLocationNumber(String locationNumber) throws QueryException
	{		
		long startTime = 0;
		boolean isValid = false;

		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering isValidLocationNumber(), storeNumber: %1$s", locationNumber));
		} // end if
		
		try
		{
			// validate the location number provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.STORE_NUMBER, locationNumber);			
			
			// query to get the store details
			LocationDetailsTO locationData = getLocationDetailsAndIsRscSupported(locationNumber);
			// if the store is not null, it's a valid store
			isValid = (locationData.getHumanResourcesSystemStoreNumber() != null);
		} // end try
		catch(NoRowsFoundException nfe)
		{
			// this is thrown whenever store details can't be retrieved for the store number provided (so it's not valid)
			isValid = false;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred validating location number %1$s", locationNumber), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting isValidLocationNumber(), locationNumber: %1$s %2$s. Total time to validate store number: %3$.9f seconds.",
				locationNumber, (isValid ? "is valid" : "is not valid"), (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if		
		
		return isValid;
	} // end function isValidLocationNumber()	
	
	/**
	 * Get minor details about the location number provided so that it can be determined if it is a RSC Supported Store or Not.
	 * 
	 * @param locationNumber the location to get details for
	 * @return location details object
	 * 
	 * @throws QueryException thrown if the location number provided is null, if the location number
	 * 						  provided is not found in the database (in the form of a NoRowsFoundException),
	 * 						  or if an exception occurs querying the database.
	 */
	public static LocationDetailsTO getLocationDetailsAndIsRscSupported(final String locationNumber) throws QueryException
	{
		long startTime = 0;
		LocationDetailsTO location = null;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getLocationDetailsAndIsRscSupported(), locationNumber: %1$s", locationNumber));
		} // end if
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.STORE_NUMBER, locationNumber);
			
			//Get the States that the RSC does not support from HR_ORG_PARM
			OrgParamTO orgParam = OrgParamManager.getOrgParam("HR", "1", "non.rsc.supported.states", "B", true);
		
			//Get the States that the RSC does not support exceptions, can manage some job titles from HR_ORG_PARM
			OrgParamTO orgParam1 = OrgParamManager.getOrgParam("HR", "1", "non.rsc.support.except", "B", true);			
			
			// use the DAO to get a database connection
			DAOConnection connection = QueryManager.getInstance(BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_DAO_CONTRACT).getDAOConnection(BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_DAO_CONTRACT);
			// create a new handler object that will be used to process the query results
			LocationHandler resultsHandler = new LocationHandler();
			
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
					DAOConnection connection = connectionList.get(BUSINESSORGANIZATIONTHDORGANIZATIONSTRUCTURE_DAO_CONTRACT);
					// get the DAO query object that will be used to execute the query
					Query query = connection.getQuery();
					// prepare the first query
					MapStream inputs = new MapStream(READ_HR_SYS_STR_ORG);
					inputs.put("humanResourcesSystemStoreNumber", locationNumber);
					inputs.put("humanResourcesSystemDivisionCodeList", LOCATION_DIVISIONS);
					inputs.put("humanResourcesStoreEffectiveBeginDate", new java.sql.Date(System.currentTimeMillis())); 
					inputs.put("humanResourcesStoreEffectiveEndDate", new java.sql.Date(System.currentTimeMillis())); 
					inputs.addQualifier("humanResourcesStoreEffectiveBeginDateLessThanEqualTo", true); 
					inputs.addQualifier("humanResourcesStoreEffectiveEndDateGreaterThanEqualTo", true); 
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting location details using location number %1$s", locationNumber));
					} // end if
					
					// execute the first query
					query.getResult(connection, handler, inputs);
					
					// if the first query did not return a result, throw a NoRowsFoundException
					if(((LocationHandler)handler).getLocationDetails() == null)
					{
						// the location was not found
						throw new NoRowsFoundException(ApplicationObject.STORE, String.format("Location %1$s not found", locationNumber));
					} // end if(((LocationHandler)handler).getLocationDetails() == null)
					
                } // end function handleQuery()				
			}; // end UniversalConnectionHandler
			
			// execute the queries
			handler.execute();
			
			// get the location details object that was populated (no null check here, if the location wasn't found then a NoRowsFoundException would have been thrown
			location = resultsHandler.getLocationDetails();
			
			// Get the non RSC Supported states 
			String[] nonSupportedLocations = null;
			if (orgParam.getCharVal() != null && !orgParam.getCharVal().equals("")) {
				nonSupportedLocations = orgParam.getCharVal().split(",");
			}	
			
			// Set the flag for supported / non supported RSC locations
			location.setRscSupportedLocation(true);
			for (String element : nonSupportedLocations) {
				if (location.getStateCode().equals(element)) {
					location.setRscSupportedLocation(false);
					break;
				}
			}
			
			// Get the non RSC Supported states exceptions 
			String[] nonSupportedLocationExceptions = null;
			if (orgParam1.getCharVal() != null && !orgParam1.getCharVal().equals("")) {
				nonSupportedLocationExceptions = orgParam1.getCharVal().split(",");
			}			
			
			// Set the flag for supported / non supported RSC location Exceptions
			location.setRSCSupportedLocationException(false);
			for (String element : nonSupportedLocationExceptions) {
				if (location.getStateCode().equals(element)) {
					location.setRSCSupportedLocationException(true);
					break;
				}
			}
			
			//Removed because YOW was added to the base tables and will be supported by RSC.  Changed 4/20/2016
			// Cannot use state to set non supported RSC for YOW, so have to look at Division
			/*if (location.getHumanResourcesSystemDivisionCode().equals("YW")) {
				location.setRscSupportedLocation(false);
				location.setRSCSupportedLocationException(false);
			}*/
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting details for location number %1$s", locationNumber), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting getLocationDetailsAndIsRscSupported(), locationNumber: %1$s. Total time to get location details: %2$.9f seconds.",
				locationNumber, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return location;
	} // end function getLocationDetailsAndIsRscSupported()		
	
	/**
	 * Description : This method will format the store details response.
	 *
	 * @param store
	 * 			- the store
	 * @param interviewTimeStampTO 
	 * 			- the interview date
	 * @return 
	 * 			- the formatted store dtls response
	 * 
	 * Added as part of Flex to HTML Conversion - 13 May 2015
	 */
	public static StoreDetailsTO getFormattedStoreDtlsResponse(StoreDetailsTO store,TimeStampTO interviewTimeStampTO){
		mLogger.info("Entering into LocationManager :: getFormattedStoreDtlsResponse method");
		
		if (store !=null){
			/** 1. FormatPhoneNumber for Phone in StoreDetailsTO **/
			if (!Util.isNullString(store.getPhone())){
				store.setFormattedPhone(Util.formatPhoneNbr(store.getPhone()));
			}
			
			/** 2. Get Store Zone mapping for the given timezone code and interview Date  **/	
			if(interviewTimeStampTO!=null){
				String timeZoneCode = store.getTimeZoneCode();
				store.setPacketDateTime(Util.getStorePacketDateTime(interviewTimeStampTO,timeZoneCode));
			}
		}
		mLogger.info("Exiting into LocationManager :: getFormattedStoreDtlsResponse method");
		return store;
		
	}
	
	public static StoreDetailsTO getStoreDetailsDAO20(final String storeNumber) throws QueryException
	{
		long startTime = 0;
		StoreDetailsTO store = null;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getStoreDetails(), storeNumber: %1$s", storeNumber));
		} // end if
		
		try
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.STORE_NUMBER, storeNumber);
			
			final Date currentDate = new Date(System.currentTimeMillis());

			StoreDetailsTO storeUnitOfWork = new UnitOfWork<StoreDetailsTO>()
					{ @Override
					public StoreDetailsTO runQueries() throws Exception
					{
						//StoreDetailsTO myStore = new StoreDetailsTO();
						StoreDetailsTO strDtls = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
								.setSQL(SQL_GET_STORE_DETAILS)
								.displayAs("Read Store Location Details")
								.input(1, storeNumber) 
								.input(2, currentDate)
								.input(3, currentDate)
								.inClause(0, STORE_DETAILS_LOC_TYPES)
								.debug(mLogger)
								.formatCycles(1)
								.get(StoreDetailsTO.class);
						
						// if the first query did not return a result, throw a NoRowsFoundException
						if (strDtls == null)
						{
							// the store was not found
							throw new NoRowsFoundException(ApplicationObject.STORE, String.format("Store %1$s not found", storeNumber));
						} // end if(strDtls == null)
						
						StoreDetailsTO strDtls2 = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
								.setSQL(SQL_READ_TRPRX_DETAILS)
								.displayAs("Read Store Time Zone")
								.input(1, "00049") 
								.input(2, storeNumber)
								.input(3, currentDate)
								.input(4, currentDate)
								.debug(mLogger)
								.get(StoreDetailsTO.class);
						
						if (strDtls2 != null && strDtls2.getTimeZoneCode() != null) {
							strDtls.setTimeZoneCode(strDtls2.getTimeZoneCode());
						}	
						
						StoreDetailsTO strDtls3 = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
								.setSQL(SQL_GET_STORE_MGR_NAME)
								.displayAs("Read Store Manager Name")
								.input(1, "A") 
								.input(2, storeNumber)
								.inClause(0, MANAGER_JOB_TITLES)
								.debug(mLogger)
								.formatCycles(1)
								.get(StoreDetailsTO.class);
						
						if (strDtls3 != null && strDtls3.getStrMgr() != null) {
							strDtls.setStrMgr(strDtls3.getStrMgr());
						}
						
						return strDtls;
					} // End StoreDetailsTO runQueries()
					}.getResult();			
						
			store = storeUnitOfWork;
			
		} // end try
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(String.format("An exception occurred getting details for store number %1$s", storeNumber), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting getStoreDetails(), storeNumber: %1$s. Total time to get store details: %2$.9f seconds.",
				storeNumber, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return store;
	} // end function getStoreDetailsDAO20()	
	
} // end class LocationManager
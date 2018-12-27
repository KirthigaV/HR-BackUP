package com.homedepot.hr.hr.retailstaffing.dao;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StoreDAO.java
 * Application: RetailStaffing
 */

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetails;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains data access methods related to store data
 * 
 * @author rlp05
 */
public class StoreDAO implements Constants
{	
	// logger instance
	private static final Logger mLogger = Logger.getLogger(StoreDAO.class);
	
	// selector constants
	private static final String SELECTOR_READ_STORE_DETAILS = "readHumanResourcesStoreGroupBySystemStoreNumber";
	// column constants
	private static final String COL_BASE_STORE_GROUP_FLG = "baseStoreGroupFlag";
	private static final String COL_STORE_GROUP_CODE = "humanResourcesStoreGroupCode";
	private static final String COL_LOC_TYP_CD = "locationTypeCode";
	private static final String COL_STORE_TYP_CD = "humanResourcesStoreTypeCode";
	private static final String COL_HR_CNTRY_CD = "humanResourcesSystemCountryCode";
	private static final String COL_HR_STORE_NBR = "humanResourcesSystemStoreNumber";
	
	//DAO 2.0 SQL===============
	public static final String SQL_GET_STORE_GROUP_CODES = "SELECT DISTINCT HR_STR_GRP_CD "
														 + "FROM HR_STR_GRP "
														 + "WHERE LOC_TYP_CD = ? "
														 + "  AND HR_STR_TYP_CD = ? "
														 + "  AND HR_SYS_CNTRY_CD = ? "
														 + "  AND (EFF_BGN_DT <= CURRENT DATE "
														 + "       AND EFF_END_DT >= CURRENT DATE) "
														 + "WITH UR ";
			
	/**
	 * Get store details for the store number provided
	 * 
	 * @param strNbr					The store number
	 * 
	 * @return							Store details for the store number provided
	 * 
	 * @throws QueryException			Thrown if an exception occurs querying the database for store details
	 */
	public static StoreDetails getStoreDetails(String storeNbr) throws QueryException
	{
		// store object that will be populated whenever the query is executed
		 final StoreDetails details = new StoreDetails();
		 
		 // create the MapStream used to pass the input values to the DAO selector
		 MapStream inputs = new MapStream(SELECTOR_READ_STORE_DETAILS);
		// add the input parameters
		 inputs.put(COL_BASE_STORE_GROUP_FLG, true);
		 inputs.put(COL_HR_STORE_NBR, storeNbr);
		 
		// invoke the query using the DAO		 
		 BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		 {
			 /*
			  * (non-Javadoc)
			  * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
			  */
			 public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			 {
				 // if a record was returned
				 if(results.next())
				 {
					 // populate the transfer object
					 details.setStoreNbr(inputs.getNVStream().getString(COL_HR_STORE_NBR));
					 details.setStoreGroupCd((results.getString(COL_STORE_GROUP_CODE) == null ? null : results.getString(COL_STORE_GROUP_CODE).trim()));
					 details.setLocTypCd((results.getString(COL_LOC_TYP_CD) == null ? null : results.getString(COL_LOC_TYP_CD).trim()));
					 details.setStoreTypCd((results.getString(COL_STORE_TYP_CD) == null ? null : results.getString(COL_STORE_TYP_CD).trim()));
					 details.setCntryCd((results.getString(COL_HR_CNTRY_CD) == null ? null : results.getString(COL_HR_CNTRY_CD).trim()));
				 } // end if(results.next())
				 else
				 {
					 // no rows, throw an exception
					 throw new QueryException(String.format("Store %1$s not found", inputs.getNVStream().getString(COL_HR_STORE_NBR)));
				 } // end else
			 } // end function readResults()
		 }); // end ResultsReader implementation
		 
		 return details;
	} // end function getStoreDetails()
	
	public static List<String> getHrStoreGroupCodesDAO20(String locTypCd, String strTypCd, String cntryCd) throws QueryException {
		
		List<String> readResultsList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_GET_STORE_GROUP_CODES)
				.displayAs("Get Store group codes for use in Associates Qualified Pool")
				.input(1, locTypCd)
				.input(2, strTypCd)
				.input(3, cntryCd) 
				.debug(mLogger)
				.formatCycles(1)
				.list(String.class);

		return readResultsList;
	} // end function getHrStoreGroupCodesDAO20()	
	
} // end class StoreDAO
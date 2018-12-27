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
import com.homedepot.hr.hr.retailstaffing.dto.LocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
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
 * DAO AnnotatedQueryHandler containing call back methods used to process complex location query results
 * 
 * @see AnnotatedQueryHandler
 */
public class LocationHandler extends AnnotatedQueryHandler implements DAOConstants
{
	/** store details object */
	private StoreDetailsTO mStoreDetails;
	
	/** location details object */
	private LocationDetailsTO mLocationDetails;
	
	/**
	 * This method processes the results returned when executing the "readStoreDetails"
	 * DAO selector
	 * 
	 * @param results				DAO infrastructure results object
	 * @param query					DAO infrastructure query object
	 * @param inputs				DAO infrastructure inputs object
	 * 
	 * @throws QueryException		Thrown if any exception occurs within the DAO infrastructure
	 */	
	@QuerySelector(name=READ_STORE_DETAILS, operation=QueryMethod.getResult)
	public void readStoreDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only be a single record returned
		if(results.next())
		{
			// initialize the store details object
			
			mStoreDetails = new StoreDetailsTO();
			mStoreDetails.setStrNum(inputs.getNVStream().getString("humanResourcesSystemStoreNumber"));
			mStoreDetails.setStrName(StringUtils.trim(results.getString("humanResourcesSystemStoreName")));
			mStoreDetails.setAdd(StringUtils.trim(results.getString("addressLineOneText")));
			mStoreDetails.setCity(StringUtils.trim(results.getString("cityName")));
			mStoreDetails.setZip(StringUtils.trim(results.getString("longZipCodeCode")));
			mStoreDetails.setState(StringUtils.trim(results.getString("stateCode")));
			mStoreDetails.setCountryCode(StringUtils.trim(results.getString("countryCode")));
			
			if(!(results.wasNull("phoneNumber")))
			{
				mStoreDetails.setPhone(StringUtils.trim(results.getString("phoneNumber")));
			} // end if(!(results.wasNull("phoneNumber")))
			
			mStoreDetails.setReg(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
			mStoreDetails.setDstCode(StringUtils.trim(results.getString("humanResourcesSystemOperationsGroupName")));
			mStoreDetails.setDiv(StringUtils.trim(results.getString("humanResourcesSystemDivisionName")));
		} // end if(results.next())
	} // end function readStoreDetails()
	
	/**
	 * This method processes the results returned when executing the "readTRPRX000ByInputList"
	 * DAO selector
	 * 
	 * @param results				DAO infrastructure results object
	 * @param query					DAO infrastructure query object
	 * @param inputs				DAO infrastructure inputs object
	 * 
	 * @throws QueryException		Thrown if any exception occurs within the DAO infrastructure
	 */		
	@QuerySelector(name="readTRPRX000ByInputList", operation=QueryMethod.getResult)
	public void readStoreTimeZone(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only be a single record returned
		if(results.next())
		{
			// this should not happen, normal flow should be to process readStoreDetails first, but just in case
			if(mStoreDetails == null) { mStoreDetails = new StoreDetailsTO(); }
			// set the time zone on the store details object
			mStoreDetails.setTimeZoneCode(StringUtils.trim(results.getString("oe22")));
		} // end if(results.next())
	} // end function readStoreTimeZone()
	
	/**
	 * This method processes the results returned when executing the "readPersonProfilesTable"
	 * DAO selector
	 * 
	 * @param results				DAO infrastructure results object
	 * @param query					DAO infrastructure query object
	 * @param inputs				DAO infrastructure inputs object
	 * 
	 * @throws QueryException		Thrown if any exception occurs within the DAO infrastructure
	 */	
	@QuerySelector(name="readPersonProfilesTable", operation=QueryMethod.getResult)
	public void readPersonProfilesTable(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only be a single record returned
		if(results.next())
		{
			// this should not happen, normal flow should be to process readStoreDetails first, but just in case
			if(mStoreDetails == null) { mStoreDetails = new StoreDetailsTO(); }
			// set the store manager name
			mStoreDetails.setStrMgr(StringUtils.trim(results.getString("name")));
		} // end if(results.next())
	} // end function readPersonProfilesTable()
	
	/**
	 * This method processes the results returned when executing the "readHumanResourcesSystemStoreOrganizationByInputList"
	 * DAO selector
	 * 
	 * @param results				DAO infrastructure results object
	 * @param query					DAO infrastructure query object
	 * @param inputs				DAO infrastructure inputs object
	 * 
	 * @throws QueryException		Thrown if any exception occurs within the DAO infrastructure
	 */	
	@QuerySelector(name=READ_HR_SYS_STR_ORG, operation=QueryMethod.getResult)
	public void readHumanResourcesSystemStoreOrganizationByInputList(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only be a single record returned
		if(results.next())
		{
			// initialize the store details object			
			mLocationDetails = new LocationDetailsTO();
			mLocationDetails.setHumanResourcesSystemStoreNumber(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
			mLocationDetails.setHumanResourcesSystemStoreName(StringUtils.trim(results.getString("humanResourcesSystemStoreName")));
			mLocationDetails.setHumanResourcesSystemDivisionCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
			mLocationDetails.setStateCode(StringUtils.trim(results.getString("stateCode")));
		} // end if(results.next())
	} // end function readHumanResourcesSystemStoreOrganizationByInputList()

	/**
	 * get the store details object
	 * 
	 * @return store details object
	 */
	public StoreDetailsTO getStoreDetails()
	{
		return mStoreDetails;
	} // end function getStoreDetails()
	
	/**
	 * get the location details object
	 * 
	 * @return location details object
	 */
	public LocationDetailsTO getLocationDetails()
	{
		return mLocationDetails;
	} // end function getLocationDetails()	
} // end class LocationHandler
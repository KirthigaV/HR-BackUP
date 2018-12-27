/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingRequisitionManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.sql.Timestamp;
import java.util.Arrays;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.DriversLicenseDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.GenericResults;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDriverLicenseExemptResponse;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.InsertNotifier;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class DriversLicenseEncryptionDAO implements DAOConstants, RetailStaffingConstants
{

	private static final Logger mLogger = Logger.getLogger(DriversLicenseEncryptionDAO.class);
	private static final String SUB_SYSTEM_CODE = "HR";
	private static final String BUSINESS_UNIT_ID = "1";
	private static final String PROCESS_TYPE_IND = "B";

	/**
	 * The method will be used to get the full path and file name of the
	 * encryption configuration file from HR_ORG_PARM tables in DB.
	 * 
	 * @return File name with full path
	 * @throws QueryException
	 */
	public static String getEncryptionConfigFileName() throws QueryException {
		final StringBuffer fileName = new StringBuffer(80);

		MapStream inputs = new MapStream(
				"readHumanResourcesOrganizationParameter");
		inputs.put("subSystemCode", SUB_SYSTEM_CODE);
		inputs.put("businessUnitId", BUSINESS_UNIT_ID);
		inputs.put("parameterId", "encrypt.config.file.name");
		inputs.put("processTypeIndicator", PROCESS_TYPE_IND);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION,
				inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						if (results.next()) {
							fileName.append(StringUtils.trim(results
									.getString("parameterCharacterValue")));
						}
					}
				});

		return fileName.toString();
	}

	/**
	 * The method will be used to get the Key Class
	 * from HR_ORG_PARM tables in DB.
	 * 
	 * @return Key Class
	 * @throws QueryException
	 */
	public static String getEncryptionKeyClass() throws QueryException {
		final StringBuffer fileName = new StringBuffer(80);

		MapStream inputs = new MapStream(
				"readHumanResourcesOrganizationParameter");
		inputs.put("subSystemCode", SUB_SYSTEM_CODE);
		inputs.put("businessUnitId", BUSINESS_UNIT_ID);
		inputs.put("parameterId", "encrypt.key.class.name");
		inputs.put("processTypeIndicator", PROCESS_TYPE_IND);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION,
				inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						if (results.next()) {
							fileName.append(StringUtils.trim(results
									.getString("parameterCharacterValue")));
						}
					}
				});

		return fileName.toString();
	}
	
	public static String createInternalAssociateDriver(String aid, byte[] encryptedDlNumber, java.sql.Date dlExpireDt, String stateCode) throws QueryException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering createInternalAssociateDriver(), aid: %1$s, encryptedDLNum: %2$s, dlExpireDt: %3$s, stateCode: %4$s", aid,
			    Arrays.toString(encryptedDlNumber), dlExpireDt, stateCode));
		}

		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("createInternalAssociateDriverV2");
		inputs.put("associateId", aid);
		inputs.put("electronicMailTransmissionTimestamp", new Timestamp(System.currentTimeMillis()));
		inputs.putAllowNull("electronicMailRetransmissionTimestamp", null);
		inputs.putAllowNull("encryptDriverLicenseNumber", encryptedDlNumber);
		inputs.putAllowNull("driverLicenseExpirationDate", dlExpireDt);
		inputs.putAllowNull("driverLicenseStateCode", stateCode);

		BasicDAO.insertObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new InsertNotifier()
		{
			public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
			}
		});

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting createInternalAssociateDriver with internalAssociateDriverId: %1$s", result.getTarget().toString()));
		}

		return result.getTarget().toString();
	}

	public static DriversLicenseDetailsTO readDriversLicenseDetailsByAidRecordId(String aid, int recordId) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering readDriversLicenseDetailsByAidRecordId aid: %1$s, recordId: %2$d", aid, recordId));
		}
		final DriversLicenseDetailsTO readDriversLicenseDetails = new DriversLicenseDetailsTO();

		MapStream inputs = new MapStream("readDriversLicenseDetailsV2");
		inputs.put("zEmplid", aid);
		inputs.put("internalAssociateDriverId", recordId);
		inputs.put("humanResourcesSystemCountryCode", "USA");

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				if(results.next())
				{
					readDriversLicenseDetails.setCandidateId(results.getString("employeeInternationalId"));
					readDriversLicenseDetails.setName(results.getString("name"));
					readDriversLicenseDetails.setBirthDate(results.getDate("birthDate"));
					readDriversLicenseDetails.setEncryptDriverLicenseNumber(results.getBytes("encryptDriverLicenseNumber"));
					readDriversLicenseDetails.setDriverLicenseExpirationDate(results.getDate("driverLicenseExpirationDate"));
					readDriversLicenseDetails.setDriverLicenseStateCode(results.getString("driverLicenseStateCode"));
				}
			}
		});

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting readDriversLicenseDetailsByAidRecordId aid: %1$s, recordId: %2$d", aid, recordId));
		}
		return readDriversLicenseDetails;
	}
	
	public static StoreDriverLicenseExemptResponse readStoresDriversLicenseExempt() throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering readStoresDriversLicenseExempt"));
		}
		final StoreDriverLicenseExemptResponse stores = new StoreDriverLicenseExemptResponse();

		MapStream inputs = new MapStream("readHumanResourcesLocationDmvExemptionByInputList");
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				while (results.next())
				{
					StoreDetailsTO storeTo = new StoreDetailsTO();
					storeTo.setStrNum(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
					stores.addStore(storeTo);
				}
			}
		});

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting readStoresDriversLicenseExempt"));
		}
		return stores;
	}	
}

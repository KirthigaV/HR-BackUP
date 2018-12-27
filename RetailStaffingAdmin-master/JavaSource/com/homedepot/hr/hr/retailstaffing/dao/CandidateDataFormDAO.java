/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingCandtDtlManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.sql.Date;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.GenericResults;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitCandidatePersonalDataRequest;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.model.CandidateDataFormManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.builder.UnitOfWork;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class CandidateDataFormDAO implements DAOConstants, RetailStaffingConstants {

	private static final Logger mLogger = Logger.getLogger(CandidateDataFormDAO.class);
	/**
	 * The method will be used for getting the Name of the Candidate from the DB.
	 * 
	 * @param candRefNbr - Candidate Reference Number
	 *            
	 * @return list of CandidateDetailsTO
	 * @throws QueryException
	 */
	public static CandidateDetailsTO getCandidateDetail(String candRefNbr) throws QueryException {
	
		final CandidateDetailsTO candidateDetailsTO = new CandidateDetailsTO();
		MapStream inputs = new MapStream("readExternalCandidateDetails");
		inputs.put("employmentCandidateId", candRefNbr);
		inputs.put("activeFlag", true);
 
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					candidateDetailsTO.setSsnNbr(results.getString("employmentApplicantId").trim());
					candidateDetailsTO.setName(Util.combineCandidateName(results.getString("lastName"), results.getString("firstName"), results
							.getString("middleInitialName"), results.getString("suffixName")));
					candidateDetailsTO.setAddress1(StringUtils.trim(results.getString("addressLineOneText")));
					candidateDetailsTO.setAddress2(StringUtils.trim(results.getString("addressLineTwoText")));
					candidateDetailsTO.setCity(StringUtils.trim(results.getString("cityName")));
					candidateDetailsTO.setStateCode(StringUtils.trim(results.getString("stateCode")));
					candidateDetailsTO.setZip(StringUtils.trim(results.getString("longZipCodeCode")));
				}
			}
		});
		return candidateDetailsTO;
	}
	
	public static boolean updateCandidateCPDDetails(SubmitCandidatePersonalDataRequest inputData) throws QueryException
	{
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("updateBackgroundCheckSystemConsentDetailsByInputList");

		inputs.put("backgroundCheckSystemConsentTypeCode", (short) 1);
		inputs.put("employmentPositionCandidateId", inputData.getApplicantId());
		inputs.put("birthDate", Util.convertStringDate(inputData.getDobEntry1()));
		inputs.put("candidateInitialsSignatureValue", inputData.getCandidateInitials().toUpperCase());
		inputs.put("documentSignatureTimestamp", new Timestamp(System.currentTimeMillis()));
		inputs.put("backgroundCheckAuthorizationFlag", true);
		inputs.put("managerUserId", inputData.getMgrLdap().toUpperCase());
		if (inputData.getDlNumber1() != null && !inputData.getDlNumber1().equals("")) {
			inputs.put("driverLicenseNumber", inputData.getDlNumber1());
			inputs.put("driverLicenseStateCode", inputData.getDriversLicenseState());
		}

		BasicDAO.updateObject(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new UpdateNotifier()
		{
			public void notifyUpdate(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		return result.isSuccess();
	}	
	
	public static boolean updateCandidateCPDDetailsDAO20(SubmitCandidatePersonalDataRequest inputData) throws QueryException
	{
		Timestamp currentTS = new Timestamp(System.currentTimeMillis());
		boolean hasDLData = false;
		boolean hasMiddleName = false;
		
		if (inputData.getDlNumber1() != null && !inputData.getDlNumber1().equals("")) {
			hasDLData = true;
		}
		if (inputData.getHasMiddleName().equals("Y")) {
			hasMiddleName = true;
		} else {
			hasMiddleName = false;
		}
		
		boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_UPDATE_BGC_CNSNT)
				.displayAs("Update CPD Form")
				.input(1, inputData.getMgrLdap().toUpperCase())
				.input(2, currentTS)
				.input(3, Util.convertStringDate(inputData.getDobEntry1()))
				.input(4, inputData.getCandidateInitials().toUpperCase())
				.input(5, currentTS)
				.input(6, true)
				.input(7, inputData.getMgrLdap().toUpperCase())
				.input(8, inputData.getHasMiddleName())
				.input(9, inputData.getMiddleName().toUpperCase())
				
				.formatOnCondition(hasDLData, 0, SQL_UPDATE_BGC_CNSNT_PLUS_DL)
				.inputOnCondition(hasDLData, 10, inputData.getDlNumber1())
				.inputOnCondition(hasDLData, 11, inputData.getDriversLicenseState())
				.inputOnCondition(hasDLData, 12, inputData.getApplicantId())
				.inputOnCondition(hasDLData, 13, 1)
								
				.inputOnCondition(!hasDLData, 10, inputData.getApplicantId())
				.inputOnCondition(!hasDLData, 11, 1)
				.debug(mLogger)
				.success();
		
		return isSuccess;
	}
	
	public static void insertPreviousAddressDataDAO20(SubmitCandidatePersonalDataRequest inputData) throws QueryException
	{
		new UnitOfWork<Void>()
		{ @Override
				public Void runQueries() throws QueryException
				{
					Date maxCnsntDt = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
							.setSQL(SQL_GET_MAX_CNSNT_DT_FROM_BGC_CNSNT_ADDR)
							.displayAs("Get MAX CNSNT DT from BGC_CNSNT_ADDR")
							.input(1, inputData.getApplicantId())
							.debug(mLogger)
							.get(Date.class);
					
					if (maxCnsntDt == null)
					{
						throw new NoRowsFoundException(ApplicationObject.BGC_CNSNT_DT, "Background Consent Date Not Found, Previous Address not saved.");
					} 
						
					mLogger.debug(String.format("Max CNSNT DT:%1$s", maxCnsntDt));
					
					int maxSeqNbr = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
							.setSQL(SQL_GET_MAX_SEQ_NBR_FROM_BGC_CNSNT_ADDR)
							.displayAs("Get MAX SEQ_NBR from BGC_CNSNT_ADDR")
							.input(1, inputData.getApplicantId()) 
							.input(2, maxCnsntDt)
							.debug(mLogger)
							.get(int.class);
					
					maxSeqNbr++;
						
					mLogger.debug(String.format("Max SEQ_NBR:%1$d", maxSeqNbr));
					
					boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
							.setSQL(SQL_INSERT_BGC_CNSNT_ADDR)
							.displayAs("Insert BGC_CNSNT_ADDR")
							.input(1, inputData.getApplicantId())
							.input(2, maxCnsntDt)
							.input(3, (short) 1)
							.input(4, maxSeqNbr)
							.input(5, inputData.getAddress1().toUpperCase())
							.input(6, inputData.getAddress2().toUpperCase())
							.input(7, "")
							.input(8, "")
							.input(9, "")
							.input(10, inputData.getCity().toUpperCase())
							.input(11, inputData.getAddressState())
							.input(12, inputData.getZipCode())
							.input(13, "")
							.input(14, "")
							.input(15, "")
							.input(16, "")
							.debug(mLogger)
							.success();
					
					
					return null;
				} // End public Void runQueries() throws QueryException
			}.getResult();  //End new UnitOfWork<Void>()
	}	// End insertPreviousAddressDataDAO20
}

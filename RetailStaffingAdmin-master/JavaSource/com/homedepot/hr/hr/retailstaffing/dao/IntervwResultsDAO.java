/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: IntervwResultsDAO.java
 * Application: RetailStaffing
 *
 */

package com.homedepot.hr.hr.retailstaffing.dao;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.ApplIntervwByCandJobReqTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplIntervwQuestTO;
import com.homedepot.hr.hr.retailstaffing.dto.BackgroundCheckDtlsTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDTInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateMinorInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ComboOptionsSortTO;
import com.homedepot.hr.hr.retailstaffing.dto.GenericResults;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgroundCheckResultsDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgroundCheckSystemConsentByInputListDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadPersonProfilesByPersonIdTO;
import com.homedepot.hr.hr.retailstaffing.dto.SsnXrefTO;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.InsertNotifier;
import com.homedepot.ta.aa.dao.ObjectReader;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class IntervwResultsDAO implements DAOConstants, RetailStaffingConstants
{

	private static final Logger logger = Logger.getLogger(IntervwResultsDAO.class);

	/**
	 * This method is used to get the Candidate detail
	 * 
	 * @param
	 * @return list of Candidate details
	 * @throws QueryException
	 */
	public List<CandidateInfoTO> readHumanResourcesStoreRequisitionCandidate(String strNbr, int reqNbr, String applcantID) throws QueryException
	{

		final List<CandidateInfoTO> readHumanResourcesStoreRequisitionCandidateList = new ArrayList<CandidateInfoTO>();

		MapStream inputs = new MapStream("readHumanResourcesStoreRequisitionCandidate");

		logger.debug(String.format("Entering readHumanResourcesStoreRequisitionCandidate(), strNbr: %1$s, reqNbr: %2$s, applcantID: %3$s", strNbr, reqNbr,
		    applcantID));

		inputs.put("humanResourcesSystemStoreNumber", strNbr);
		inputs.put("employmentRequisitionNumber", reqNbr);
		inputs.put("employmentPositionCandidateId", applcantID);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				CandidateInfoTO readHumanResourcesStoreRequisitionCandidateDTO = null;
				while(results.next())
				{
					readHumanResourcesStoreRequisitionCandidateDTO = new CandidateInfoTO();
					readHumanResourcesStoreRequisitionCandidateDTO.setHumanResourcesSystemStoreNumber(results.getString("humanResourcesSystemStoreNumber"));
					readHumanResourcesStoreRequisitionCandidateDTO.setEmploymentRequisitionNumber(results.getInt("employmentRequisitionNumber"));
					readHumanResourcesStoreRequisitionCandidateDTO.setEmploymentPositionCandidateId(results.getString("employmentPositionCandidateId"));
					readHumanResourcesStoreRequisitionCandidateDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
					readHumanResourcesStoreRequisitionCandidateDTO.setCreateUserId(StringUtils.trim(results.getString("createUserId")));
					readHumanResourcesStoreRequisitionCandidateDTO.setInterviewStatusIndicator(StringUtils.trim(results.getString("interviewStatusIndicator")));
					readHumanResourcesStoreRequisitionCandidateDTO.setReferenceCheckResultIndicator(StringUtils.trim(results
					    .getString("referenceCheckResultIndicator")));
					readHumanResourcesStoreRequisitionCandidateDTO.setDrugTestResultCode(StringUtils.trim(results.getString("drugTestResultCode")));
					readHumanResourcesStoreRequisitionCandidateDTO.setCandidateOfferMadeFlag(StringUtils.trim(results.getString("candidateOfferMadeFlag")));
					readHumanResourcesStoreRequisitionCandidateDTO.setCandidateOfferStatusIndicator(StringUtils.trim(results
					    .getString("candidateOfferStatusIndicator")));
					readHumanResourcesStoreRequisitionCandidateDTO.setInactiveDate(Util.converDatetoDateTO(results.getDate("inactiveDate")));
					readHumanResourcesStoreRequisitionCandidateDTO.setActiveFlag(results.getBoolean("activeFlag"));
					readHumanResourcesStoreRequisitionCandidateDTO.setLastUpdateUserId(results.getString("lastUpdateUserId"));
					readHumanResourcesStoreRequisitionCandidateDTO.setLastUpdateTimestamp(results.getTimestamp("lastUpdateTimestamp"));
					readHumanResourcesStoreRequisitionCandidateDTO
					    .setHumanResourcesActionSequenceNumber(results.getShort("humanResourcesActionSequenceNumber"));
					readHumanResourcesStoreRequisitionCandidateDTO.setEmployeeStatusActionCode(StringUtils.trim(results.getString("employeeStatusActionCode")));
					readHumanResourcesStoreRequisitionCandidateDTO.setActiveDate(Util.converDatetoDateTO(results.getDate("activeDate")));
					readHumanResourcesStoreRequisitionCandidateDTO.setDrugTestId(StringUtils.trim(results.getString("drugTestId")));
					readHumanResourcesStoreRequisitionCandidateDTO
					    .setCandidateOfferMadeDate(Util.converDatetoDateTO(results.getDate("candidateOfferMadeDate")));
					readHumanResourcesStoreRequisitionCandidateDTO.setOfferDeclineReasonCode(results.getInt("offerDeclineReasonCode"));
					readHumanResourcesStoreRequisitionCandidateDTO.setLetterOfIntentDate(Util.converDatetoDateTO(results.getDate("letterOfIntentDate")));
					readHumanResourcesStoreRequisitionCandidateDTO.setAdverseActionDate(Util.converDatetoDateTO(results.getDate("adverseActionDate")));
					readHumanResourcesStoreRequisitionCandidateList.add(readHumanResourcesStoreRequisitionCandidateDTO);
				}
			}
		});

		return readHumanResourcesStoreRequisitionCandidateList;
	}

	/**
	 * This method is used to get the interview Status details of the applicant
	 * 
	 * @param
	 * @return list of interview Questions details
	 * @throws QueryException
	 */
	public List<ApplIntervwByCandJobReqTO> readApplicantInterviewByCandidateAndJobRequisition(String applcantID, int reqNo) throws QueryException
	{
		final List<ApplIntervwByCandJobReqTO> readApplicantInterviewByCandidateAndJobRequisitionList = new ArrayList<ApplIntervwByCandJobReqTO>();

		MapStream inputs = new MapStream("readApplicantInterviewByCandidateAndJobRequisition");

		logger.debug(String.format("Entering readApplicantInterviewByCandidateAndJobRequisition(), reqNbr: %1$s, applcantID: %2$s", reqNo, applcantID));

		inputs.put("employmentApplicantId", applcantID);
		inputs.put("employmentRequisitionNumber", reqNo);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				ApplIntervwByCandJobReqTO readApplicantInterviewByCandidateAndJobRequisitionDTO = null;
				while(results.next())
				{
					readApplicantInterviewByCandidateAndJobRequisitionDTO = new ApplIntervwByCandJobReqTO();
					readApplicantInterviewByCandidateAndJobRequisitionDTO.setInterviewerUserId(results.getString("interviewerUserId"));
					readApplicantInterviewByCandidateAndJobRequisitionDTO.setInterviewDate(results.getDate("interviewDate"));
					readApplicantInterviewByCandidateAndJobRequisitionDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
					readApplicantInterviewByCandidateAndJobRequisitionDTO.setInterviewResultIndicator(results.getString("interviewResultIndicator"));
					readApplicantInterviewByCandidateAndJobRequisitionDTO.setLastUpdateUserId(results.getString("lastUpdateUserId"));
					readApplicantInterviewByCandidateAndJobRequisitionDTO.setLastUpdateTimestamp(results.getTimestamp("lastUpdateTimestamp"));
					readApplicantInterviewByCandidateAndJobRequisitionList.add(readApplicantInterviewByCandidateAndJobRequisitionDTO);
				}
			}
		});

		return readApplicantInterviewByCandidateAndJobRequisitionList;
	}

	/**
	 * This method is used to get the interview Question details to applicant
	 * 
	 * @param
	 * @return list of interview Questions details
	 * @throws QueryException
	 */
	public List<ApplIntervwQuestTO> readApplicantInterviewQuestion(String applcantID, String interviewerUserId, Date intervwDate) throws QueryException
	{
		final List<ApplIntervwQuestTO> readApplicantInterviewQuestionList = new ArrayList<ApplIntervwQuestTO>();

		MapStream inputs = new MapStream("readApplicantInterviewQuestion");

		logger.debug(String.format("Entering readApplicantInterviewQuestion(), IntvwerId: %1$s, applcantID: %2$s, intervwDt: %3$s", interviewerUserId,
		    applcantID, intervwDate.toString()));

		inputs.put("employmentApplicantId", applcantID);
		inputs.put("interviewerUserId", interviewerUserId);
		inputs.put("interviewDate", intervwDate);
		// inputs.put("employmentInterviewQuestionId", "value"); // optional

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				ApplIntervwQuestTO readApplicantInterviewQuestionDTO = null;
				while(results.next())
				{
					readApplicantInterviewQuestionDTO = new ApplIntervwQuestTO();
					readApplicantInterviewQuestionDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
					readApplicantInterviewQuestionDTO.setInterviewQuestionResultIndicator(results.getString("interviewQuestionResultIndicator"));
					readApplicantInterviewQuestionDTO.setEmploymentInterviewQuestionId(results.getString("employmentInterviewQuestionId"));
					readApplicantInterviewQuestionList.add(readApplicantInterviewQuestionDTO);
				}
			}
		});

		return readApplicantInterviewQuestionList;
	}

	/**
	 * The method will be used to get the SIGS from the hr_org_Parm table in DB.
	 * 
	 * @return List of SIGS
	 * @throws QueryException
	 */
	public List<ComboOptionsSortTO> readHrOrgParmSigList() throws QueryException
	{
		final List<ComboOptionsSortTO> readSigList = new ArrayList<ComboOptionsSortTO>();

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");

		logger.debug(String.format("Entering readHrOrgParmSigList()"));

		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "sig.%");
		inputs.addQualifier("parameterIdSearch", true); // optional, when true
		// will use a Like for
		// parameterId

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				ComboOptionsSortTO comboOptionsTO = null;
				while(results.next())
				{
					comboOptionsTO = new ComboOptionsSortTO();
					comboOptionsTO.setData(results.getString("parameterCharacterValue"));
					comboOptionsTO.setLabel(results.getString("parameterDescription"));
					// Add a 200 to discontinued SIG's so that when active ones
					// are added the sort will still function correctly
					// Also set enabled to false
					if(results.getString("parameterIdOutput").indexOf("active") == -1)
					{
						comboOptionsTO.setSortOrder(results.getInt("parameterIntegerValue") + 200);
						comboOptionsTO.setEnabled(false);
					}
					else
					{
						comboOptionsTO.setSortOrder(results.getInt("parameterIntegerValue"));
						comboOptionsTO.setEnabled(true);
					}
					readSigList.add(comboOptionsTO);
				}
			}
		});

		return readSigList;
	}

	public boolean updateHumanResourcesStoreRequisitionCandidate(CandidateInfoTO candInfo) throws QueryException
	{
		final GenericResults result = new GenericResults();
		Date currentDate = new Date(System.currentTimeMillis());
		MapStream inputs = new MapStream("updateHumanResourcesStoreRequisitionCandidate");

		logger.info(String.format("Entering updateHumanResourcesStoreRequisitionCandidate(), candInfo: %1$s", candInfo.toString()));

		inputs.put("humanResourcesSystemStoreNumber", candInfo.getHumanResourcesSystemStoreNumber());
		inputs.put("employmentRequisitionNumber", candInfo.getEmploymentRequisitionNumber());
		inputs.put("employmentPositionCandidateId", candInfo.getEmploymentPositionCandidateId());
		inputs.put("createUserId", candInfo.getCreateUserId());
		inputs.put("interviewStatusIndicator", candInfo.getInterviewStatusIndicator());
		inputs.put("referenceCheckResultIndicator", candInfo.getReferenceCheckResultIndicator());
		inputs.put("drugTestResultCode", candInfo.getDrugTestResultCode());
		inputs.put("candidateOfferMadeFlag", candInfo.getCandidateOfferMadeFlag());
		inputs.put("candidateOfferStatusIndicator", candInfo.getCandidateOfferStatusIndicator());
		inputs.put("inactiveDate", Util.convertDateTO(candInfo.getInactiveDate()));
		inputs.put("activeFlag", candInfo.getActiveFlag());
		inputs.put("humanResourcesActionSequenceNumber", (short)candInfo.getHumanResourcesActionSequenceNumber());
		inputs.put("employeeStatusActionCode", candInfo.getEmployeeStatusActionCode());
		inputs.put("activeDate", Util.convertDateTO(candInfo.getActiveDate()));
		inputs.put("drugTestId", candInfo.getDrugTestId());

		if(candInfo.getCandidateOfferMadeDate().getMonth() != null && !candInfo.getCandidateOfferMadeDate().getMonth().equals(""))
		{
			inputs.putAllowNull("candidateOfferMadeDate", Util.convertDateTO(candInfo.getCandidateOfferMadeDate())); // can
			// be
			// null
		}
		else
		{
			inputs.putAllowNull("candidateOfferMadeDate", null); // can be null
		}

		inputs.put("offerDeclineReasonCode", candInfo.getOfferDeclineReasonCode());

		if(candInfo.getLetterOfIntentDate().getMonth() != null && !candInfo.getLetterOfIntentDate().getMonth().equals(""))
		{
			inputs.putAllowNull("letterOfIntentDate", Util.convertDateTO(candInfo.getLetterOfIntentDate())); // can
			// be
			// null
		}
		else
		{
			inputs.putAllowNull("letterOfIntentDate", null); // can be null
		}

		if(candInfo.getAdverseActionDate().getMonth() != null && !candInfo.getAdverseActionDate().getMonth().equals(""))
		{
			inputs.putAllowNull("adverseActionDate", Util.convertDateTO(candInfo.getAdverseActionDate())); 
		}
		else
		{
			inputs.putAllowNull("adverseActionDate", null);
		}

		BasicDAO.updateObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new UpdateNotifier()
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

	public boolean createApplicantInterviewQuestion(String applicantId, String interviewerId, java.sql.Date interviewDt, String questId, String questRslt)
	    throws QueryException
	{
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("createApplicantInterviewQuestion");

		inputs.put("employmentApplicantId", applicantId);
		inputs.put("interviewerUserId", interviewerId);
		inputs.put("interviewDate", interviewDt);
		inputs.put("employmentInterviewQuestionId", questId);
		inputs.put("interviewQuestionResultIndicator", questRslt);

		BasicDAO.insertObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new InsertNotifier()
		{
			public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		return result.isSuccess();
	}
	
	public ApplIntervwQuestTO getApplicantInterviewQuestion(String applicantId, String interviewerId, java.sql.Date interviewDt, String questionIndicator) throws QueryException {

		if (logger.isDebugEnabled()) {
			logger.debug("start getApplicantInterviewQuestion");
		}
		
		final String datasource = DATA_SOURCE_DB2Z_PR1_005;
		final StringBuilder sql = new StringBuilder(300);
		
		sql.append("SELECT CRT_TS, INTVW_QST_RSLT_IND, EMPLT_INTVW_QST_ID FROM APLCNT_INTVW_QST ");
		sql.append("WHERE EMPLT_APLCNT_ID = ? ");
		sql.append("AND INTVWR_USER_ID = ? ");
		sql.append("AND INTVW_DT = ? ");
		sql.append("AND EMPLT_INTVW_QST_ID = ? ");
		sql.append("WITH UR");
		
		ApplIntervwQuestTO interviewQuestion = DAO.useJNDI(datasource)
				.setSQL(sql.toString())
				.debug(logger)
				.input(1, applicantId)
				.input(2, interviewerId)
				.input(3, interviewDt)
				.input(4, questionIndicator)
				.get(ApplIntervwQuestTO.class);
		
		
		return interviewQuestion;
	}
	
	public Integer updateApplicantInterviewQuestion(String applicantId, String interviewerId, java.sql.Date interviewDt, String questionIndicator, String questionResult) throws QueryException {

		if (logger.isDebugEnabled()) {
			logger.debug("start updateApplicantInterviewQuestion");
		}
		
		final String datasource = DATA_SOURCE_DB2Z_PR1_005;
		final StringBuilder sql = new StringBuilder(300);
		
		
		sql.append("  UPDATE APLCNT_INTVW_QST SET INTVW_QST_RSLT_IND = ? ");
		sql.append(" WHERE EMPLT_APLCNT_ID = ? ");
		sql.append("AND INTVWR_USER_ID = ? "); 
		sql.append("AND INTVW_DT = ? ");
		sql.append("AND EMPLT_INTVW_QST_ID = ? ");
		
		Integer count = DAO.useJNDI(datasource)
				.setSQL(sql.toString())
				.debug(logger)
				.input(1, questionResult)
				.input(2, applicantId)
				.input(3, interviewerId)
				.input(4, interviewDt)
				.input(5, questionIndicator)
				.count();
		
		return count;
	}

	public boolean createApplicantInterview(String applicantId, String interviewerId, java.sql.Date interviewDt, String intvwRslt, int reqNbr)
	    throws QueryException
	{
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("createApplicantInterview");

		inputs.put("employmentApplicantId", applicantId);
		inputs.put("interviewerUserId", interviewerId);
		inputs.put("interviewDate", interviewDt);
		inputs.put("interviewResultIndicator", intvwRslt);
		inputs.put("employmentRequisitionNumber", reqNbr);

		BasicDAO.insertObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new InsertNotifier()
		{
			public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		return result.isSuccess();
	}

	public Boolean readBackgroundCheckSystemConsentSignatureForExistenceByConsentSignatureDate(String candId, java.sql.Date currentDateMinusYear)
	    throws QueryException
	{
		final GenericResults result = new GenericResults();

		logger.info(String.format("Entering readBackgroundCheckSystemConsentSignatureForExistenceByConsentSignatureDate(), candInfo: %1$s, Date: %2$s", candId,
		    currentDateMinusYear.toString()));

		MapStream inputs = new MapStream("readBackgroundCheckSystemConsentSignatureForExistenceByConsentSignatureDate");

		inputs.put("consentSignatureDate", currentDateMinusYear);
		inputs.put("employmentPositionCandidateId", candId);

		BasicDAO.getObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ObjectReader()
		{
			public void readObject(Object target, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
			}
		});
		logger.info("Has Consent = " + result.getTarget());
		return (Boolean)result.getTarget();
	}
	
	public BackgroundCheckDtlsTO readBackgroundCheckConsentElectronicDao2(String candId, java.sql.Date currentDateMinusYear)
		    throws QueryException
		{
			
		logger.debug(String.format("Entering readBackgroundCheckConsentElectronicDao2(), candidateId: %1$s, currentDateMinusYear: %2$s", candId, currentDateMinusYear));

		BackgroundCheckDtlsTO bcgConsentSig = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_READ_BGC_CNSNT_SIG_PA)
				.input(1, candId)
				.input(2, currentDateMinusYear)
				.debug(logger)
				.get(BackgroundCheckDtlsTO.class);
		
		return bcgConsentSig;
		
		}
	

	public boolean createBackgroundCheckSystemConsentSig(String applicantId) throws QueryException
	{
		final GenericResults result = new GenericResults();

		logger.info(String.format("Entering createBackgroundCheckSystemConsentSig(), applicantId: %1$s", applicantId));

		MapStream inputs = new MapStream("createBackgroundCheckSystemConsentSig");

		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("consentSignatureDate", new Date(System.currentTimeMillis()));
		inputs.put("backgroundCheckSystemConsentTypeCode", (short)1);
		inputs.put("applicantSignatureValue", " ");

		BasicDAO.insertObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new InsertNotifier()
		{
			public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		return result.isSuccess();
	}
	
	public boolean createBackgroundCheckSystemConsentSig_Non_PA_Dao2(String applicantId, String createUserId) throws QueryException
	{
		Date currentDate = new Date(System.currentTimeMillis());
		
		boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_CREATE_BGC_CNSNT_SIG_NON_PA)
				.input(1, applicantId)
				.input(2, currentDate)
				.input(3, createUserId)
				.input(4, createUserId)
				.success();
		
		return isSuccess;
	}
	
	public boolean createBackgroundCheckSystemConsentSig_PA_Dao2(String applicantId, String createUserId, String profileId, String applicantLink) throws QueryException
	{
		Date currentDate = new Date(System.currentTimeMillis());
		
		boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_CREATE_BGC_CNSNT_SIG_PA)
				.input(1, applicantId)
				.input(2, currentDate)
				.input(3, profileId)
				.input(4, createUserId)
				.input(5, createUserId)
				.input(6, applicantLink)
				.success();
		
		return isSuccess;
	}	
	
	public boolean updateBackgroundCheckSystemConsentSig_PA_Dao2(String applicantId, String createUserId, String profileId, String applicantLink) throws QueryException
	{
		Date currentDate = new Date(System.currentTimeMillis());
		
		boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_UPDATE_BGC_CNSNT_SIG_PA)
				.input(1, currentDate)
				.input(2, profileId)
				.input(3, createUserId)
				.input(4, applicantLink)
				.input(5, applicantId)
				.success();
		
		return isSuccess;
	}	
	
	public List<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO> readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(String strNbr,
	    String jobTtlCd, String deptNbr) throws QueryException
	{
		final List<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO> readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCodeList = new ArrayList<ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO>();

		logger.info(String.format("Entering readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(), strNbr: %1$s, jobTtlCd: %2$s, deptNbr: %3$s",
		    strNbr, jobTtlCd, deptNbr));

		MapStream inputs = new MapStream("readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode");

		inputs.put("humanResourcesSystemStoreNumber", strNbr);
		inputs.put("baseStoreGroupFlag", true);
		inputs.put("jobTitleCode", jobTtlCd);
		inputs.put("humanResourcesSystemDepartmentNumber", deptNbr);
		inputs.put("effectiveDate", new Date(System.currentTimeMillis()));

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO theTo = null;
				while(results.next())
				{
					theTo = new ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO();
					theTo.setHumanResourcesStoreTypeCode(results.getString("humanResourcesStoreTypeCode"));
					theTo.setBackgroundCheckSystemPackageId(results.getInt("backgroundCheckSystemPackageId"));
					theTo.setBackgroundCheckSystemComponentId(results.getInt("backgroundCheckSystemComponentId"));
					readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCodeList.add(theTo);
				}
			}
		});

		return readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCodeList;
	}

	public List<ReadPersonProfilesByPersonIdTO> readPersonProfilesByPersonId(String candId) throws QueryException
	{
		final List<ReadPersonProfilesByPersonIdTO> readPersonProfilesByPersonIdList = new ArrayList<ReadPersonProfilesByPersonIdTO>();

		logger.info(String.format("Entering readPersonProfilesByPersonId(), candId: %1$s", candId));

		MapStream inputs = new MapStream("readPersonProfilesByPersonId");
		inputs.put("employmentApplicantId", candId);
		inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(ACTV_FLG, true);
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);
		inputs.put("humanResourcesSystemCountryCode", "USA");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				ReadPersonProfilesByPersonIdTO readPersonProfilesByPersonIdTO = null;
				while(results.next())
				{
					readPersonProfilesByPersonIdTO = new ReadPersonProfilesByPersonIdTO();
					readPersonProfilesByPersonIdTO.setName(results.getString("name"));
					readPersonProfilesByPersonIdTO.setOrganization1(results.getString("organization1"));
					readPersonProfilesByPersonIdTO.setOrganization2(results.getString("organization2"));
					readPersonProfilesByPersonIdTO.setJobTitleId(results.getString("jobTitleId"));
					readPersonProfilesByPersonIdTO.setHireDateOrigin(results.getDate("hireDateOrigin"));
					readPersonProfilesByPersonIdTO.setEmploymentCat(results.getString("employmentCat"));
					readPersonProfilesByPersonIdTO.setAddressLine1Long(results.getString("addressLine1Long"));
					readPersonProfilesByPersonIdTO.setAddressLine2Long(results.getString("addressLine2Long"));
					readPersonProfilesByPersonIdTO.setAddressCity(results.getString("addressCity"));
					readPersonProfilesByPersonIdTO.setStateProvince(results.getString("stateProvince"));
					readPersonProfilesByPersonIdTO.setPostalCode11(results.getString("postalCode11"));
					readPersonProfilesByPersonIdTO.setPhoneNumber(results.getString("phoneNumber"));
					readPersonProfilesByPersonIdList.add(readPersonProfilesByPersonIdTO);
				}
			}
		});

		return readPersonProfilesByPersonIdList;
	}

	public List<ReadBackgroundCheckResultsDTO> readBackgroundCheckResults(String candId) throws QueryException
	{
		final List<ReadBackgroundCheckResultsDTO> readBackgroundCheckResultsList = new ArrayList<ReadBackgroundCheckResultsDTO>();

		logger.info(String.format("Entering readBackgroundCheckResults(), candId: %1$s", candId));

		MapStream inputs = new MapStream("readBackgroundCheckResults");

		inputs.put("employmentPositionCandidateId", candId);
		inputs.put("activeFlag", "A");
		inputs.put("effectiveDate", new Date(System.currentTimeMillis()));

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				ReadBackgroundCheckResultsDTO readBackgroundCheckResultsDTO = null;
				while(results.next())
				{
					readBackgroundCheckResultsDTO = new ReadBackgroundCheckResultsDTO();
					readBackgroundCheckResultsDTO.setBackgroundCheckId(results.getInt("backgroundCheckId"));
					readBackgroundCheckResultsDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
					readBackgroundCheckResultsDTO.setBackgroundCheckSystemPackageId(results.getInt("backgroundCheckSystemPackageId"));
					readBackgroundCheckResultsDTO.setBackgroundCheckSystemComponentId(results.getInt("backgroundCheckSystemComponentId"));
					readBackgroundCheckResultsDTO.setBackgroundCheckSystemComponentSequenceNumber(results
					    .getShort("backgroundCheckSystemComponentSequenceNumber"));
					readBackgroundCheckResultsDTO
					    .setBackgroundCheckSystemComponentCompletedDate(results.getDate("backgroundCheckSystemComponentCompletedDate"));
					readBackgroundCheckResultsDTO.setBackgroundCheckSystemAlertStatusCode(results.getShort("backgroundCheckSystemAlertStatusCode"));
					readBackgroundCheckResultsDTO.setOverrideAlertStatusCode(results.getShort("overrideAlertStatusCode"));
					if(results.wasNull("overrideAlertStatusCode"))
					{
						readBackgroundCheckResultsDTO.setOverrideAlertStatusCode(null);
					}
					readBackgroundCheckResultsDTO.setAlertStatusOverrideDate(results.getDate("alertStatusOverrideDate"));
					readBackgroundCheckResultsDTO.setFavorableResultEffectiveDays(results.getShort("favorableResultEffectiveDays"));
					readBackgroundCheckResultsDTO.setUnfavorableResultEffectiveDays(results.getShort("unfavorableResultEffectiveDays"));
					readBackgroundCheckResultsDTO.setBackgroundCheckTypeCode(results.getInt("backgroundCheckTypeCode"));
					readBackgroundCheckResultsList.add(readBackgroundCheckResultsDTO);
				}
			}
		});

		return readBackgroundCheckResultsList;
	}

	public List<ReadBackgroundCheckSystemConsentByInputListDTO> readBackgroundCheckSystemConsentByInputList(String candId) throws QueryException
	{
		final List<ReadBackgroundCheckSystemConsentByInputListDTO> readBackgroundCheckSystemConsentByInputListList = new ArrayList<ReadBackgroundCheckSystemConsentByInputListDTO>();

		logger.info(String.format("Entering readBackgroundCheckSystemConsentByInputList(), candId: %1$s", candId));

		MapStream inputs = new MapStream("readBackgroundCheckSystemConsentByInputList");

		inputs.put("employmentPositionCandidateId", candId);
		inputs.put("backgroundCheckSystemConsentTypeCode", (short)1);
		inputs.put("listOrder", "value"); // optional

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				ReadBackgroundCheckSystemConsentByInputListDTO readBackgroundCheckSystemConsentByInputListDTO = null;
				while(results.next())
				{
					readBackgroundCheckSystemConsentByInputListDTO = new ReadBackgroundCheckSystemConsentByInputListDTO();
					readBackgroundCheckSystemConsentByInputListDTO.setConsentSignatureDate(results.getDate("consentSignatureDate"));
					readBackgroundCheckSystemConsentByInputListDTO.setCreateUserId(results.getString("createUserId"));
					readBackgroundCheckSystemConsentByInputListDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
					readBackgroundCheckSystemConsentByInputListDTO.setLastUpdateUserId(results.getString("lastUpdateUserId"));
					readBackgroundCheckSystemConsentByInputListDTO.setLastUpdateTimestamp(results.getTimestamp("lastUpdateTimestamp"));
					readBackgroundCheckSystemConsentByInputListDTO.setManualSignatureReferenceId(results.getInt("manualSignatureReferenceId"));
					if(results.wasNull("manualSignatureReferenceId"))
					{
						readBackgroundCheckSystemConsentByInputListDTO.setManualSignatureReferenceId(null);
					}
					readBackgroundCheckSystemConsentByInputListDTO.setLastName(results.getString("lastName"));
					readBackgroundCheckSystemConsentByInputListDTO.setFirstName(results.getString("firstName"));
					readBackgroundCheckSystemConsentByInputListDTO.setMiddleInitialName(results.getString("middleInitialName"));
					readBackgroundCheckSystemConsentByInputListDTO.setApplicantAliasName(results.getString("applicantAliasName"));
					readBackgroundCheckSystemConsentByInputListDTO.setDriverLicenseNumber(results.getString("driverLicenseNumber"));
					readBackgroundCheckSystemConsentByInputListDTO.setDriverLicenseStateCode(results.getString("driverLicenseStateCode"));
					readBackgroundCheckSystemConsentByInputListDTO.setBirthDate(results.getDate("birthDate"));
					readBackgroundCheckSystemConsentByInputListDTO.setBirthPlaceName(results.getString("birthPlaceName"));
					readBackgroundCheckSystemConsentByInputListDTO.setMotherMaidName(results.getString("motherMaidName"));
					readBackgroundCheckSystemConsentByInputListList.add(readBackgroundCheckSystemConsentByInputListDTO);
				}
			}
		});

		return readBackgroundCheckSystemConsentByInputListList;
	}

	public boolean createBackgroundCheckSystemOrder(String candId, Timestamp createTimeStamp, String createUserId
			                                       , int reqNbr, int packageId, String strId, String externalBgcId
			                                       , short bgcOrderStatus)
	    throws QueryException
	{
		final GenericResults result = new GenericResults();

		logger.info(String.format("Entering createBackgroundCheckSystemOrder(), candId: %1$s, packageId: %2$s", candId, packageId));

		MapStream inputs = new MapStream("createBackgroundCheckSystemOrder");

		inputs.put("employmentPositionCandidateId", candId);
		inputs.put("orderCreateTimestamp", createTimeStamp);
		inputs.put("createUserId", createUserId);
		inputs.put("employmentRequisitionNumber", reqNbr);
		inputs.put("backgroundCheckSystemPackageId", packageId);
		inputs.put("backgroundCheckSystemVendorId", 1);
		inputs.put("externalBackgroundCheckId", externalBgcId);
		inputs.put("externalBackgroundCheckSystemLocationId", strId);
		inputs.put("backgroundCheckSystemOrderStatusCode", (short) bgcOrderStatus);
		inputs.putAllowNull("letterOfIntentDate", null); // can be null
		inputs.putAllowNull("adverseActionDate", null); // can be null

		BasicDAO.insertObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new InsertNotifier()
		{
			public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		return result.isSuccess();
	}

	public boolean createBackgroundCheckSystemOrderDetail(String candId, Timestamp createTimeStamp, String createUserId, int seqNum, int componentId)
	    throws QueryException
	{
		final GenericResults result = new GenericResults();

		logger.info(String.format("Entering createBackgroundCheckSystemOrderDetail(), candId: %1$s, componentId: %2$s", candId, componentId));

		MapStream inputs = new MapStream("createBackgroundCheckSystemOrderDetail");

		inputs.put("employmentPositionCandidateId", candId);
		inputs.put("orderCreateTimestamp", createTimeStamp);
		inputs.put("detailSequenceNumber", (short)seqNum);
		inputs.put("createUserId", createUserId);
		inputs.put("backgroundCheckSystemComponentId", componentId);
		inputs.put("externalBackgroundCheckSystemItemId", "null");
		inputs.put("backgroundCheckSystemOrderStatusCode", (short)0);
		inputs.put("backgroundCheckSystemAlertStatusCode", (short)0);

		BasicDAO.insertObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new InsertNotifier()
		{
			public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		return result.isSuccess();
	}
	
	//ATS mts1876.  Updates the applicant DOB and if Drivers License Data is not null it as well (MVR Check Needed)
	//Moved to CandidateDataFormDAO for CDP Project....
	/*public static boolean updateDOB_DLInfoWhenNotNull(UpdateIntvwRltsCandIntvwDtlsRequest inputData) throws QueryException
	{
		final GenericResults result = new GenericResults();
		logger.info(String.format("Entering updateDOB_DLInfoWhenNotNull(), candInfo: %1$s", inputData.getCandidateInfoTo().getEmploymentPositionCandidateId()));

		MapStream inputs = new MapStream("updateBackgroundCheckSystemConsentDetailsByInputList");

		inputs.put("backgroundCheckSystemConsentTypeCode", (short) 1);
		inputs.put("employmentPositionCandidateId", inputData.getCandidateInfoTo().getEmploymentPositionCandidateId());
		inputs.put("birthDate", Util.convertStringDate(inputData.getBackgroundCheckInfo().getDateOfBirth()));
		if (!inputData.getBackgroundCheckInfo().getExtLicenseNum().equals("null") && inputData.getBackgroundCheckInfo().getExtLicenseNum() != null) {
			inputs.put("driverLicenseNumber", inputData.getBackgroundCheckInfo().getExtLicenseNum());
			inputs.put("driverLicenseStateCode", inputData.getBackgroundCheckInfo().getExtLicenseState());
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
	}*/
	
	public static String getCandSsnIfExists(String applicantId) throws QueryException
	{
		final SsnXrefTO ssnXrefTO = new SsnXrefTO();

		logger.info(String.format("Entering getCandSsnIfExists(), applicantId: %1$s", applicantId));

		MapStream inputs = new MapStream(READ_EAPLCNT_SSN_XREF);
		inputs.put(EMPLT_APLCNT_ID, applicantId);
		inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
		inputs.put(ACTV_FLG, true);
		inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
		inputs.addQualifier("effectiveEndDateGreaterThan", true);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				if (results.next())
				{
					ssnXrefTO.setApplicantSocialSecurityNumberNumber(StringUtils.trim(results.getString(CANDIDATE_SSN_NUMBER)));
				}
			}
		});

		return ssnXrefTO.getApplicantSocialSecurityNumberNumber();
	}
	
	public static BackgroundCheckDtlsTO getCpdFormInformation(String applicantId) throws QueryException
	{
		final BackgroundCheckDtlsTO backgroundCheckDtlsTO = new BackgroundCheckDtlsTO();

		logger.info(String.format("Entering getCpdFormInformation(), applicantId: %1$s", applicantId));

		MapStream inputs = new MapStream(READ_BGC_CNSNT_DTLS);
		inputs.put(EMPLT_POSN_CAND_ID, applicantId);
		inputs.put(BGC_CNSNT_TYPE_CD, (short) 1);

		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				if (results.next())
				{
					backgroundCheckDtlsTO.setDateOfBirth(results.getDate(BIRTH_DATE).toString());
					if (!results.wasNull(CAND_INIT_SIG_VAL)) {
						backgroundCheckDtlsTO.setCandidateInitialsSignatureValue(StringUtils.trim(results.getString(CAND_INIT_SIG_VAL)));
					}
					if (!results.wasNull(DOC_SIG_TS)) {
						backgroundCheckDtlsTO.setDocumentSignatureTimestamp(results.getString(DOC_SIG_TS));
					}
					if (!results.wasNull(BGC_AUTH_FLG)) {
						backgroundCheckDtlsTO.setBackgroundCheckAuthorizationFlag(results.getBoolean(BGC_AUTH_FLG));
					}
					if (!results.wasNull(MGR_USR_ID)) {
						backgroundCheckDtlsTO.setManagerUserId(StringUtils.trim(results.getString(MGR_USR_ID)));
					}
				}
			}
		});

		return backgroundCheckDtlsTO;
	}
	
	public static CandidateInfoTO getRehireEligible(String ssn) throws QueryException
	{
		logger.info(String.format("Entering getRehireEligible(), ssn: %1$s", ssn));
		
		final CandidateInfoTO candInfo = new CandidateInfoTO();
		
		//Seed candInfo so if no results are found
		candInfo.setRehireEligible(true);
		
		MapStream inputs = new MapStream(CHECK_REHIRE_STATUS);
		inputs.put("employeeInternationalId", ssn);
		inputs.put(ACTV_FLG, true);
		inputs.put("humanResourcesSystemCountryCode", "USA");
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
		{
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{
				if (results.next())
				{
					if (!results.wasNull(REHIRE_STATUS_RESULT)) {
						if (StringUtils.trim(results.getString(REHIRE_STATUS_RESULT)).equals("N")) {
							candInfo.setRehireEligible(false);
							candInfo.setTermLoc(StringUtils.trim(results.getString("location")));
							candInfo.setTermRsn(StringUtils.trim(results.getString("terminationReason")));
							candInfo.setTermDt(results.getString("terminationDate"));
						}
					}
				}
			}
		});

		return candInfo;
	}	
	
	//Added for FMS 7894 January 2016 CR's
	//When Candidate is Rehire Ineligible, need to update EAPLCNT_CAND_XREF field RHR_ELIG_IND to 'N'
	public static boolean updateRehireEligibleFlag(String employmentCandidateId) throws QueryException
	{
		final GenericResults result = new GenericResults();

		String paddedCandRefId = String.format("%010d", Integer.parseInt(employmentCandidateId));
		logger.info(String.format("Entering updateRehireEligibleFlag(), applicantId: %1$s", paddedCandRefId));
		
		MapStream inputs = new MapStream("updateEmploymentApplicantCandidateCrossReference");
		inputs.put("employmentCandidateId", paddedCandRefId);
		inputs.put("rehireEligibilityIndicator", NO);

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
	
	//Added for FMS 7894 January 2016 CR's	
	public static String readKenexaJobCategory(String jobTitleDept) throws QueryException {
		final StringBuffer jobCategory = new StringBuffer();
		
		MapStream inputs = new MapStream("readTRPRX000ByInputList");
		inputs.put("tabno", "10069");
		inputs.put("tableKeys", jobTitleDept);
		inputs.put("startEndDate", new java.sql.Date(System.currentTimeMillis()));
		
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					jobCategory.append(StringUtils.trim(results.getString("oe33")));
				}
			}
		});
		
		return jobCategory.toString();
	}
	
	//Added for FMS 7894 January 2016 CR's
	public static boolean createDoNotConsiderFor60Days(String storeNbr, int reqnNbr, String applicantId, boolean metIndicatorFlg) throws QueryException
	{
		final GenericResults result = new GenericResults();

		logger.info(String.format("Entering createDoNotConsiderFor60Days(), applicantId: %1$s", applicantId));

		Calendar current = Calendar.getInstance();
		// clone current date and add 60 days
		Calendar currentPlus60 = (Calendar) current.clone();
		currentPlus60.add(Calendar.DAY_OF_YEAR, 60);
		
		MapStream inputs = new MapStream("createEpcandTemporaryReject");

		inputs.put("humanResourcesSystemStoreNumber", storeNbr);
		inputs.put("employmentRequisitionNumber", reqnNbr);
		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("effectiveBeginDate", new Date(System.currentTimeMillis()));
		
		inputs.put("effectiveEndDate", new Date(currentPlus60.getTimeInMillis()));
		inputs.put("metFlag", metIndicatorFlg);

		BasicDAO.insertObject(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new InsertNotifier()
		{
			public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
			{
				result.setTarget(target);
				result.setSuccess(success);
				result.setCount(count);
			}
		});

		return result.isSuccess();
	}
	
	//Added for FMS 7894 January 2016 CR's	
	public static CandidateInfoTO readDoNotConsiderFor60Days(String storeNbr, String applicantId, boolean metFlag) throws QueryException {
		
		logger.info(String.format("Entering readDoNotConsiderFor60Days(), applicantId: %1$s", applicantId));
		
		final CandidateInfoTO tempObj = new CandidateInfoTO();
		
		MapStream inputs = new MapStream("readEpcandTemporaryRejectByInputList");
		inputs.put("humanResourcesSystemStoreNumber", storeNbr);
		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("metFlag", metFlag);
		inputs.put("currentDate", new Date(System.currentTimeMillis()));
		
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					tempObj.setHumanResourcesSystemStoreNumber(results.getString("humanResourcesSystemStoreNumber"));
					tempObj.setEmploymentRequisitionNumber(results.getInt("employmentRequisitionNumber"));
					tempObj.setEmploymentCandidateId(results.getString("employmentPositionCandidateId"));
					tempObj.setEffectiveBeginDate(results.getDate("effectiveBeginDate"));
					tempObj.setEffectiveEndDate(results.getDate("effectiveEndDate"));
				}
			}
		});
		
		return tempObj;
	}
	
	public static CandidateInfoTO readLastDNCRow(String storeNbr, String applicantId, boolean metFlag) throws QueryException {

		if (logger.isDebugEnabled()) {
			logger.debug("start readLastDNCRow");
		}
		
		final String datasource = DATA_SOURCE_DB2Z_PR1_005;
		final StringBuilder sql = new StringBuilder(300);
		
		sql.append("SELECT A.HR_SYS_STR_NBR ");
		sql.append("	,A.EMPLT_REQN_NBR ");
		sql.append("	,A.EMPLT_POSN_CAND_ID ");
		sql.append("	,A.EFF_BGN_DT ");
		sql.append("	,A.EFF_END_DT ");
		sql.append("FROM EPCAND_TEMP_RJTD A ");
		sql.append(" WHERE A.HR_SYS_STR_NBR = ? ");
		sql.append(" AND A.EMPLT_POSN_CAND_ID = ? ");
		sql.append(" AND A.MET_FLG = '" + (metFlag ? "Y" : "N") + "'");
		sql.append(" ORDER BY EFF_BGN_DT DESC ");
		sql.append(" FETCH FIRST 1 ROW ONLY");
		sql.append(" WITH UR");
		
		CandidateInfoTO tempObj = DAO.useJNDI(datasource)
				.setSQL(sql.toString())
				.debug(logger)
				.input(1, storeNbr)
				.input(2, applicantId)
				.get( CandidateInfoTO.class );
		
		
		return tempObj;
//		logger.debug("Candidate Has needed Tier Code: " + categoryDetailsTO.getEMPTST_ID());
//		if (categoryDetailsTO.getEMPTST_ID() != 0) {
//			return true;
//		} else {
//			return false;
//		}
	}


	//Added for FMS 7894 January 2016 CR's
	public static boolean updateDoNotConsiderFor60DaysEffEndDt(String storeNbr, int reqnNbr, String applicantId, boolean metIndicatorFlg, Date effBgnDate) throws QueryException
	{
		final GenericResults result = new GenericResults();

		logger.info(String.format("Entering updateDoNotConsiderFor60DaysEffEndDt(), applicantId: %1$s", applicantId));

		Calendar current = Calendar.getInstance();
		// clone current date and subtract 1 day
		Calendar currentMinus1 = (Calendar) current.clone();
		currentMinus1.add(Calendar.DAY_OF_YEAR, -1);
		
		MapStream inputs = new MapStream("updateEpcandTemporaryReject");

		inputs.put("humanResourcesSystemStoreNumber", storeNbr);
		inputs.put("employmentRequisitionNumber", reqnNbr);
		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("metFlag", metIndicatorFlg);
		inputs.put("effectiveBeginDate", effBgnDate);
		inputs.put("effectiveEndDate", new Date(currentMinus1.getTimeInMillis()));


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
	
	public static boolean updateDoNotConsiderFor60DaysEffEndDt60DaysOut(String storeNbr, int reqnNbr, String applicantId, boolean metIndicatorFlg, Date effBgnDate) throws QueryException
	{
		final GenericResults result = new GenericResults();

		logger.info(String.format("Entering updateDoNotConsiderFor60DaysEffEndDt(), applicantId: %1$s", applicantId));

		Calendar current = Calendar.getInstance();
		// clone current date and subtract 1 day
		Calendar currentMinus1 = (Calendar) current.clone();
		currentMinus1.add(Calendar.DATE, 60);
		
		MapStream inputs = new MapStream("updateEpcandTemporaryReject");

		inputs.put("humanResourcesSystemStoreNumber", storeNbr);
		inputs.put("employmentRequisitionNumber", reqnNbr);
		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("metFlag", metIndicatorFlg);
		inputs.put("effectiveBeginDate", effBgnDate);
		inputs.put("effectiveEndDate", new Date(currentMinus1.getTimeInMillis()));


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
	
	public static Date getAssociateDOB(String applicantId) throws QueryException {

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Begin getAssociateDOB with applicantId:%1$s", applicantId));
		}
		
		final String datasource = DATA_SOURCE_DB2Z_PR1_032;		
		final StringBuilder sql = new StringBuilder(300);

		sql.append("SELECT C.BIRTH_DT ");
		sql.append("FROM EAPLCNT_SSN_XREF A ");
		sql.append("    ,HR_EMPL_TSRT_XREF B ");
		sql.append("    ,PERSON_PROFILES_T C ");
		sql.append("WHERE A.EMPLT_APLCNT_ID = ? ");
		sql.append("  AND A.ACTV_FLG = ? ");
		sql.append("  AND A.EFF_BGN_DT <= ? ");
		sql.append("  AND A.EFF_END_DT > ? ");
		sql.append("  AND A.APLCNT_SSN_NBR = B.EMPL_INTL_ID ");
		sql.append("  AND B.HR_SYS_CNTRY_CD = ? ");
		sql.append("  AND B.TSRT_EMPL_ID = C.PERSON_ID ");
		sql.append("WITH UR");
		
		Date dob = DAO.useJNDI(datasource)
				.setSQL(sql.toString())
				.input(1, applicantId)
				.input(2, "Y")
				.input(3, new Date(System.currentTimeMillis()))
				.input(4, new Date(System.currentTimeMillis()))
				.input(5, "USA")
				.get( Date.class );
		
		logger.debug("Candidate DOB: " + dob);
		return dob;
	}	
	
	public static boolean updateAssociateDMVDetails(String applicantId, Date dob, String DlNbr, String DlStateCd) throws QueryException
	{
		final GenericResults result = new GenericResults();

		MapStream inputs = new MapStream("updateBackgroundCheckSystemConsentDetailsByInputList");

		inputs.put("backgroundCheckSystemConsentTypeCode", (short) 1);
		inputs.put("employmentPositionCandidateId", applicantId);
		inputs.put("birthDate", dob);
		//inputs.put("candidateInitialsSignatureValue", inputData.getCandidateInitials().toUpperCase());
		//inputs.put("documentSignatureTimestamp", new Timestamp(System.currentTimeMillis()));
		//inputs.put("backgroundCheckAuthorizationFlag", true);
		//inputs.put("managerUserId", inputData.getMgrLdap().toUpperCase());
		//if (inputData.getDlNumber1() != null && !inputData.getDlNumber1().equals("")) {
			inputs.put("driverLicenseNumber", DlNbr);
			inputs.put("driverLicenseStateCode", DlStateCd);
		//}

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
	
	public static boolean updateInitInterviewEnteredDate (CandidateInfoTO candInfo) throws QueryException
	{
		Date currentDate = new Date(System.currentTimeMillis());
		
		boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_UPDATE_INIT_INTERVIEW_DATE)
				.input(1, currentDate)
				.input(2, candInfo.getHumanResourcesSystemStoreNumber())
				.input(3, candInfo.getEmploymentRequisitionNumber())
				.input(4, candInfo.getEmploymentPositionCandidateId())
				.success();
		
		return isSuccess;
	}
	
	public static boolean updateInitOfferEnteredDate (CandidateInfoTO candInfo) throws QueryException
	{
		Date currentDate = new Date(System.currentTimeMillis());
		
		boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_UPDATE_INIT_OFFER_DATE)
				.input(1, currentDate)
				.input(2, candInfo.getHumanResourcesSystemStoreNumber())
				.input(3, candInfo.getEmploymentRequisitionNumber())
				.input(4, candInfo.getEmploymentPositionCandidateId())
				.success();
		
		return isSuccess;
	}	
	
	/**
	 * This method is used to get the Candidate detail
	 * 
	 * @param
	 * @return list of Candidate details
	 * @throws QueryException
	 */
	public List<CandidateInfoTO> readHumanResourcesStoreRequisitionCandidateDAO20(String strNbr, int reqNbr, String applcantID) throws QueryException
	{

		logger.debug(String.format("Entering readHumanResourcesStoreRequisitionCandidateDAO20(), strNbr: %1$s, reqNbr: %2$s, applcantID: %3$s", strNbr, reqNbr,
		    applcantID));

		List<CandidateInfoTO> readHumanResourcesStoreRequisitionCandidateList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_READ_HR_STR_REQN_CAND)
				.displayAs("Read Requisition / Candidate Data for use on Interview Summary Form")
				.input(1, strNbr)
				.input(2, reqNbr)
				.input(3, applcantID)
				.debug(logger)
				.list(CandidateInfoTO.class);
		
		return readHumanResourcesStoreRequisitionCandidateList;
	}	

	/**
	 * This method is used to get Parental Consent Forms for Minors Completion Data to determine completion status
	 * 
	 * @param
	 * @return list of two results for Parental Consent Forms for Minors, details
	 * @throws QueryException
	 */
	public List<CandidateMinorInfoTO> readHumanResourcesParentalConsentFormsForMinorsDAO20(String applcantID, int reqNbr)throws QueryException
	{

		logger.debug(String.format("Entering readHumanResourcesParentalConsentFormsForMinorsDAO20(), applcantID: %1$s, reqNbr: %2$s", applcantID, reqNbr));

		List<CandidateMinorInfoTO> readHumanResourcesParentalConsentFormsForMinorsList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_GET_MINOR_FORMS_COMPLETION)
				.displayAs("Read Requisition / Candidate Data to determine if Parental Consent forms are complete, for use on Interview Summary Form")
				.input(1, applcantID)
				.input(2, reqNbr)
				.input(3, applcantID)
				.input(4, reqNbr)
				.debug(logger)
				.list(CandidateMinorInfoTO.class);
		
		return readHumanResourcesParentalConsentFormsForMinorsList;
	}
	
	/**
	 * This method is used to get the Candidate's age, to later determine if they are a Minor, for use in ISF.
	 * 
	 * @param
	 * @return only one value of age with two decimal places
	 * @throws QueryException
	 */
	public float readHumanResourcesStoreRequisitionCandidateAgeDAO20(String applcantID) throws QueryException
	{

		logger.debug(String.format("Entering readHumanResourcesStoreRequisitionCandidateAgeDAO20(), applcantID: %1$s", applcantID));

		Float readHumanResourcesStoreRequisitionCandidateAge = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_GET_CANDIDATE_AGE)
				.displayAs("Get Candidate Age to determine if Candidate is a Minor for use on Interview Summary Form")
				.input(1, applcantID)
				.debug(logger)
				.get(Float.class);
		
		return readHumanResourcesStoreRequisitionCandidateAge;
	}
	
	
	
	/**
	 * This method is used to get the Candidate's Drug Test Panel details
	 * 
	 * @param
	 * @return list of Candidate's Drug Test Order Submission details within 10 days
	 * @throws QueryException
	 */
	public List<CandidateDTInfoTO> readApplicantDrugTestOrderDAO20(String employmentApplicantID_DT, int employmentRequisitionNumber_DT) throws QueryException
	{

		logger.debug(String.format("Entering readApplicantDrugTestOrderDAO20(), employmentApplicantID_DT: %1$s, employmentRequisitionNumber_DT: %2$s", employmentApplicantID_DT, employmentRequisitionNumber_DT));

		List<CandidateDTInfoTO> readApplicantDrugTestOrderList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_GET_DRUGTESTREQUEST_CANDIDATE_DATA)
				.displayAs("Read Drug Test Order Submission details of Candidate for use on Interview Summary Form's Drug Test Panel")
				.input(1, employmentApplicantID_DT)
				.input(2, employmentRequisitionNumber_DT)
				.debug(logger)
				.list(CandidateDTInfoTO.class);
		
		return readApplicantDrugTestOrderList;
	}

	/**
	 * This method is used to update the Candidate's Drug Test Panel details with a completion date
	 * 
	 * @param
	 * @return
	 * @throws QueryException
	 */
	public static boolean updateApplicantDrugTestOrderDAO20(String employmentApplicantID_DT, int employmentRequisitionNumber_DT, long DTEST_ORD_NBR_DT) throws QueryException
	{
		logger.debug(String.format("Entering updateApplicantDrugTestOrderDAO20(), employmentApplicantID_DT: %1$s, employmentRequisitionNumber_DT: %2$s, DTEST_ORD_NBR_DT: %3$s", employmentApplicantID_DT, employmentRequisitionNumber_DT, DTEST_ORD_NBR_DT));

		boolean isSuccess = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_UPD_DRUGTESTREQUEST_CANDIDATE_DATA)
				.displayAs("Update Drug Test Order Completion Timestamp of Candidate for use on Interview Summary Form's Drug Test Panel")
				.input(1, employmentApplicantID_DT)
				.input(2, employmentRequisitionNumber_DT)
				.input(3, DTEST_ORD_NBR_DT)
				.debug(logger)
				.success();
		
		return isSuccess;
	}

	/**
	 * The method will be used to get the status of the Drug Test Panel from Parm
	 * tables in DB.
	 * 
	 * @return Off, Production, Pilot
	 * @throws QueryException
	 */
	public static String readHrOrgParmDrugTestPanelStatus() throws QueryException {
		final StringBuffer drugPanelStatus = new StringBuffer(15);

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "drug.test.panel.status");

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					drugPanelStatus.append(StringUtils.trim(results.getString("parameterCharacterValue")));
				}
			}
		});

		return drugPanelStatus.toString();
	}

	/**
	 * The method will be used to get the locations that are in the Drug Test Pilot from Parm
	 * tables in DB.
	 * 
	 * @return List of Locations
	 * @throws QueryException
	 */
	public static List<String> readHrOrgParmDrugTestPanelPilotLocations() throws QueryException {
		final List<String> pilotList = new ArrayList<String>();

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "drug.test.pilot.loc.");
		inputs.addQualifier("parameterIdSearch", true);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					if (results.getString("parameterCharacterValue") != null && !results.getString("parameterCharacterValue").equals("")) {
						String[] locationList = results.getString("parameterCharacterValue").split(",");
						for (int i = 0; i < locationList.length; i++) {
							pilotList.add(StringUtils.trim(locationList[i]));
						}
					}
				}
			}
		});

		return pilotList;
	}
	
	public static String readHrOrgParmMinorConsentPanelStatus() throws QueryException {
		final StringBuffer minorConsentPanelStatus = new StringBuffer(15);
		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "minor.cons.panel.status");

      BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					minorConsentPanelStatus.append(StringUtils.trim(results.getString("parameterCharacterValue")));
				}
			}
		});
		return minorConsentPanelStatus.toString();
	}
	
	/**
	 * The method will be used to get the locations that are in the Parental Consent forms for Minors Pilot from Parm
	 * tables in DB.
	 * 
	 * @return List of Locations
	 * @throws QueryException
	 */
	public static List<String> readHrOrgParmMinorConsentPanelPilotLocations() throws QueryException {
		final List<String> pilotMinorConsentList = new ArrayList<String>();

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "minor.cons.pilot.loc.");
		inputs.addQualifier("parameterIdSearch", true);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					if (results.getString("parameterCharacterValue") != null && !results.getString("parameterCharacterValue").equals("")) {
						String[] locationMinorConsentList = results.getString("parameterCharacterValue").split(",");
						for (int i = 0; i < locationMinorConsentList.length; i++) {
							pilotMinorConsentList.add(StringUtils.trim(locationMinorConsentList[i]));
						}
					}
				}
			}
		});

		return pilotMinorConsentList;
	}

	/**
	 * The method will be used to get the status of the Real Time Background Check from Parm
	 * tables in DB.
	 * 
	 * @return Off, Pilot
	 * @throws QueryException
	 */
	public static String readHrOrgParmBgcPilotStatus() throws QueryException {
		final StringBuffer bgcPilotStatus = new StringBuffer(15);

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "bgc.web.pilot.process");

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				if (results.next()) {
					bgcPilotStatus.append(StringUtils.trim(results.getString("parameterCharacterValue")));
				}
			}
		});

		return bgcPilotStatus.toString();
	}

/**
	 * The method will be used to get the locations that are in the Background Check Pilot from Parm
	 * tables in DB.
	 * 
	 * @return List of Locations
	 * @throws QueryException
	 */
	public static List<String> readHrOrgParmBgcPilotLocations() throws QueryException {
		final List<String> pilotList = new ArrayList<String>();

		MapStream inputs = new MapStream("readHumanResourcesOrganizationParameterByInputList");
		inputs.put("subSystemCode", "HR");
		inputs.put("businessUnitId", "1");
		inputs.put("parameterId", "bgc.web.pilot.loc.");
		inputs.addQualifier("parameterIdSearch", true);

		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				while (results.next()) {
					if (results.getString("parameterCharacterValue") != null && !results.getString("parameterCharacterValue").equals("")) {
						String[] locationList = results.getString("parameterCharacterValue").split(",");
						for (int i = 0; i < locationList.length; i++) {
							pilotList.add(StringUtils.trim(locationList[i]));
						}
					}
				}
			}
		});

		return pilotList;
	}

public ApplIntervwByCandJobReqTO getApplicantInterviewStatus(String applicantId, String interviewerId, Date interviewDt, int reqNbr) throws QueryException {
	
	if (logger.isDebugEnabled()) {
		logger.debug("start getApplicantInterviewStatus");
	}
	
	final String datasource = DATA_SOURCE_DB2Z_PR1_005;
	final StringBuilder sql = new StringBuilder(300);
	
	sql.append("SELECT CRT_TS, INTVW_RSLT_IND FROM APLCNT_INTVW ");
	sql.append("WHERE EMPLT_APLCNT_ID = ? ");
	sql.append("AND INTVWR_USER_ID = ? ");
	sql.append("AND INTVW_DT = ? ");
	sql.append("AND EMPLT_REQN_NBR = ? ");
	sql.append("WITH UR");
	
	ApplIntervwByCandJobReqTO interviewQuestion = DAO.useJNDI(datasource)
			.setSQL(sql.toString())
			.debug(logger)
			.input(1, applicantId)
			.input(2, interviewerId)
			.input(3, interviewDt)
			.input(4, reqNbr)
			.get(ApplIntervwByCandJobReqTO.class);
	
	
	return interviewQuestion;

}

public Integer updateApplicantInterviewStatus(String applicantId, String interviewerId, Date interviewDt, String intvwRslt, int reqNbr) throws QueryException {

	if (logger.isDebugEnabled()) {
		logger.debug("start updateApplicantInterviewStatus");
	}
	
	final String datasource = DATA_SOURCE_DB2Z_PR1_005;
	final StringBuilder sql = new StringBuilder(300);
	
	
	sql.append("  UPDATE APLCNT_INTVW SET INTVW_RSLT_IND = ? ");
	sql.append(" WHERE EMPLT_APLCNT_ID = ? ");
	sql.append("AND INTVWR_USER_ID = ? "); 
	sql.append("AND INTVW_DT = ? ");
	sql.append("AND EMPLT_REQN_NBR = ? ");
	
	Integer count = DAO.useJNDI(datasource)
			.setSQL(sql.toString())
			.debug(logger)
			.input(1, intvwRslt)
			.input(2, applicantId)
			.input(3, interviewerId)
			.input(4, interviewDt)
			.input(5, reqNbr)
			.count();
	
	return count;
}

}

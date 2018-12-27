/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenDAO.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO.ReadActiveRequisitionsByStoreTO;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.JavaBeanStream;

// RSA3.0
import com.homedepot.hr.hr.retailstaffing.dto.request.ApplicantPoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.AssociatePoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.ApplicantTO;
import com.homedepot.hr.hr.retailstaffing.dto.AssociateTO; //RSA3.0

/**
 * This Class is used to have DAO logic for Phone Screen Functionality This
 * Class will have the following functionalities:- readByPhoneScreenNumber -
 * Reading the phone screen details by phone number. readInterviewDetails -
 * Reading the interview detail for a particular phone screen.
 * readRequisitionStoreLocation - Reading the requisition store location details
 * for the candidate. readStoreDetails - Reading the store details for the store
 * number. readRequisitionDetails - Reading the requisition details for the
 * given requisition number. readResponses - Reading the responses for a given
 * phone number. readPhoneScreenEmploymentRequesitionNote - Reading the employee
 * requisition note for a given phone number. readRequisitionStaffingDetails -
 * Reading the staffing details for a given requisition number.
 * readNlsInterviewRespondStatus - Reading the interview response status codes
 * for the given phone number. readHumanResourcesPhoneScreen - Reading the phone
 * screen details by phone screen number. readNlsInterviewRespondStatusList -
 * Reading the nls response status list. getCandidateDetails - Reading the
 * candidate details by phone screen number and requisition number.
 * 
 * @author TCS
 * 
 */
public class ApplPoolDAO implements DAOConstants, RetailStaffingConstants
{
	private static final Logger logger = Logger.getLogger(PhoneScreenDAO.class);

	/**
	 * This method is used for getting the applicant personal info.
	 * 
	 * @param ssnNbr
	 *            - the candidate id (ssn) number.
	 * 
	 * @return list of personal info details
	 * @throws QueryException
	 */
	// applicant pool
	// public List<ApplicantTO> getApplicantInfoList(String storeNo) throws
	// RetailStaffingException
	public Map<String, Object> getApplicantInfoList(final ApplicantPoolRequest applicantPoolReq) throws RetailStaffingException
	{
		logger.info(this + "Enters getApplicantInfoList method in DAO ");
		// temp
		// List<ApplicantTempTO> applicantTempList = new
		// ArrayList<ApplicantTempTO>();
		// temp

		final List<ApplicantTO> applicantInfoList = new ArrayList<ApplicantTO>();
		final Map<String, Object> applicantPoolMap = new HashMap<String, Object>();

		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemStoreNumber(applicantPoolReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readActiveRequisitionsByStore");
		try
		{

			if(applicantPoolReq.getPaginationInfoTO() != null)
			{
				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(applicantPoolReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);

			}
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					ApplicantTO applApplicantTO = null;

					List<Object> paginationTokenList = new ArrayList<Object>();
					// temp
					String[] firstNameArray =
					{
					    "TONYA", "CHELSEY", "LISA"
					};
					String[] lastNameArray =
					{
					    "STEPHENS", "PHILLIPS", "HYMANGOODRIDGE"
					};
					String[] categoryArray =
					{
					    "Cashier", "Manager", "Service"
					};
					String[] appIdArray =
					{
					    "008060024", "314002935", "314002900"
					};
					String[] ftPtArray =
					{
					    "F", "P", "F"
					};
					String[] applTypeArray =
					{
					    "AP", "AP", "AP"
					};
					String[] lastUpdatedArray =
					{
					    "2010-02-11", "2010-03-11", "2010-04-11"
					};
					// temp

					// while (results.next()){
					for(int i = 0; i < firstNameArray.length; i++)
					{

						HashMap paginationToken = new HashMap();
						applApplicantTO = new ApplicantTO();

						applApplicantTO.setFirstName(firstNameArray[i]);
						applApplicantTO.setLastName(lastNameArray[i]);
						applApplicantTO.setApplicantId(appIdArray[i]);
						applApplicantTO.setCategory(categoryArray[i]);
						applApplicantTO.setFt_pt(ftPtArray[i]);
						applApplicantTO.setApplType(applTypeArray[i]);
						applApplicantTO.setLastUpdated(lastUpdatedArray[i]);

						applicantInfoList.add(applApplicantTO);

						// temp build pagination
						// PaginationBean paginationBean = new PaginationBean();
						Object applicantIdObj = (Object)applApplicantTO.getApplicantId();
						Object applicantTsObj = (Object)applApplicantTO.getLastUpdated();
						// Object
						// paginationBean.setCRT_TS(((ApplicantTempTO)tempApplPoolArray.get(i)).getLastUpdated());

						paginationToken.put("APPLICANT_ID", applicantIdObj);
						paginationToken.put("UPDATED_TS", applicantTsObj);

						paginationTokenList.add(paginationToken);
						// temp

					}

					Map<String, Map<String, Object>> pagingMap = new HashMap<String, Map<String, Object>>();
					if(paginationTokenList != null && paginationTokenList.size() > 0)
					{

						if(Boolean.parseBoolean(applicantPoolReq.getPaginationInfoTO().getIsForward()))
						{
							pagingMap.put("FIRST_RECORD", (Map<String, Object>)paginationTokenList.get(0));
							pagingMap.put("LAST_RECORD", (Map<String, Object>)paginationTokenList.get(paginationTokenList.size() - 1));
						}
						else
						{
							pagingMap.put("FIRST_RECORD", (Map<String, Object>)paginationTokenList.get(paginationTokenList.size() - 1));
							pagingMap.put("LAST_RECORD", (Map<String, Object>)paginationTokenList.get(0));
						}

					}

					applicantPoolMap.put(PAGINATION_TOKEN, pagingMap);

				}
			});

			if(applicantPoolReq.getPaginationInfoTO() != null)
			{
				// In case the previous link was clicked
				// then reverse the array.
				if(!Boolean.parseBoolean(applicantPoolReq.getPaginationInfoTO().getIsForward()))
				{
					if(applicantInfoList != null && applicantInfoList.size() > 0)
					{
						Collections.reverse(applicantInfoList);
					}
				}
			}

			applicantPoolMap.put(APPLICANT_POOL_LIST, applicantInfoList);

		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return applicantPoolMap;
	}

	// associate pool
	public Map<String, Object> getAssociateInfoList(final AssociatePoolRequest associatePoolReq) throws RetailStaffingException
	{
		logger.info(this + "Enters getApplicantInfoList method in DAO ");
		// temp
		// List<AssociateTempTO> associateTempList = new
		// ArrayList<AssociateTempTO>();
		// temp

		final List<AssociateTO> associateInfoList = new ArrayList<AssociateTO>();
		final Map<String, Object> associatePoolMap = new HashMap<String, Object>();

		ReadActiveRequisitionsByStoreTO inputsList = new ReadActiveRequisitionsByStoreTO();
		inputsList.setHumanResourcesSystemStoreNumber(associatePoolReq.getOrgID());
		inputsList.setTabno(TABNO_DEPT_NO);

		JavaBeanStream inputList = new JavaBeanStream(inputsList);
		inputList.setSelectorName("readActiveRequisitionsByStore");
		try
		{

			if(associatePoolReq.getPaginationInfoTO() != null)
			{
				/*
				 * // this part needs to be implemented after we have the DAO //
				 * Case when the hit to the service is not the first hit. if
				 * (!Boolean.parseBoolean(summReq.getPaginationInfoTO()
				 * .getIsFirstHit())) { PagingRecordInfo pagingRecordInfo =
				 * null; Map<String, Object> paginationToken = new
				 * HashMap<String, Object>(); if
				 * (Boolean.parseBoolean(summReq.getPaginationInfoTO()
				 * .getIsForward())) { pagingRecordInfo =
				 * summReq.getSecondRecordInfo(); } else { pagingRecordInfo =
				 * summReq.getFirstRecordInfo(); } if (pagingRecordInfo != null)
				 * { paginationToken.put("CRT_TS", Util
				 * .convertDateTO(pagingRecordInfo .getUpdatedDate()));
				 * paginationToken.put("EMPLT_REQN_NBR", Integer
				 * .parseInt(pagingRecordInfo.getId()));
				 * 
				 * } inputList.addQualifier("paginationToken", paginationToken);
				 * }
				 */

				inputList.addQualifier("pagingDirectionForward", Boolean.parseBoolean(associatePoolReq.getPaginationInfoTO().getIsForward()));
				inputList.addQualifier("recordsRequested", CACHED_RECORD_SIZE);

			}
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputList, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					AssociateTO applAssociateTO = null;

					// temp
					String[] firstNameArray =
					{
					    "ROBERT", "ELISE", "OUZI"
					};
					String[] lastNameArray =
					{
					    "MACDONALD", "HARRISON", "ZACCAI"
					};
					String[] categoryArray =
					{
					    "Cashier A", "Manager A", "Service A"
					};
					String[] appIdArray =
					{
					    "001000001", "001000002", "001000003"
					};
					String[] ftPtArray =
					{
					    "F", "F", "F"
					};
					String[] applTypeArray =
					{
					    "AS", "AS", "AS"
					};
					String[] lastUpdatedArray =
					{
					    "2010-09-11", "2010-10-11", "2010-11-11"
					};
					// temp

					List<Object> paginationTokenList2 = new ArrayList<Object>();
					// while (results.next()){
					for(int i = 0; i < firstNameArray.length; i++)
					{

						HashMap paginationToken2 = new HashMap();
						applAssociateTO = new AssociateTO();

						applAssociateTO.setFirstName(firstNameArray[i]);
						applAssociateTO.setLastName(lastNameArray[i]);
						applAssociateTO.setCategory(categoryArray[i]);
						applAssociateTO.setApplicantId(appIdArray[i]);
						applAssociateTO.setFt_pt(ftPtArray[i]);
						applAssociateTO.setApplType(applTypeArray[i]);
						applAssociateTO.setLastUpdated(lastUpdatedArray[i]);

						associateInfoList.add(applAssociateTO);

						// temp build pagination
						// PaginationBean paginationBean = new PaginationBean();
						Object associateIdObj = (Object)applAssociateTO.getApplicantId();
						Object associateTsObj = (Object)applAssociateTO.getLastUpdated();
						// Object
						// paginationBean.setCRT_TS(((ApplicantTempTO)tempApplPoolArray.get(i)).getLastUpdated());

						paginationToken2.put("APPLICANT_ID", associateIdObj);
						paginationToken2.put("UPDATED_TS", associateTsObj);

						paginationTokenList2.add(paginationToken2);
						// temp

					}

					Map<String, Map<String, Object>> pagingMap2 = new HashMap<String, Map<String, Object>>();
					if(paginationTokenList2 != null && paginationTokenList2.size() > 0)
					{

						if(Boolean.parseBoolean(associatePoolReq.getPaginationInfoTO().getIsForward()))
						{
							pagingMap2.put("FIRST_RECORD", (Map<String, Object>)paginationTokenList2.get(0));
							pagingMap2.put("LAST_RECORD", (Map<String, Object>)paginationTokenList2.get(paginationTokenList2.size() - 1));
						}
						else
						{
							pagingMap2.put("FIRST_RECORD", (Map<String, Object>)paginationTokenList2.get(paginationTokenList2.size() - 1));
							pagingMap2.put("LAST_RECORD", (Map<String, Object>)paginationTokenList2.get(0));
						}

					}

					associatePoolMap.put(PAGINATION_TOKEN, pagingMap2);

				}
			});

			if(associatePoolReq.getPaginationInfoTO() != null)
			{
				// In case the previous link was clicked
				// then reverse the array.
				if(!Boolean.parseBoolean(associatePoolReq.getPaginationInfoTO().getIsForward()))
				{
					if(associateInfoList != null && associateInfoList.size() > 0)
					{
						Collections.reverse(associateInfoList);
					}
				}
			}

			associatePoolMap.put(ASSOCIATE_POOL_LIST, associateInfoList);

		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_CANDIDATE_DETAILS_ERROR_CODE, e);
		}
		return associatePoolMap;
	}
	// associate pool
}

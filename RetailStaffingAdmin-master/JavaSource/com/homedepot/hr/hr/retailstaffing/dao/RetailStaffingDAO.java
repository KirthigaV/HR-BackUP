/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.DeptDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.JobTtlDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.OrgUnitDetailsTO;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to have the business across the application like on load
 * need to get the interview status details. The class will have the
 * functionality for:- getAllAvailableRegions - Getting all the available
 * regions. getAllAvailableDivs - Getting all the available divisions.
 * getAllAvailableDists - Getting all the available districts.
 * checkStoreForExistence - Checking the store for existence.
 * 
 * @author TCS
 * 
 */
public class RetailStaffingDAO implements DAOConstants, RetailStaffingConstants
{
	private static final Logger logger = Logger.getLogger(RetailStaffingDAO.class);

	/**
	 * This method will fetch all the available regions from DB.
	 * 
	 * @return list of OrgUnitDetailsTO
	 * @throws QueryException
	 */
	public List<OrgUnitDetailsTO> getAllAvailableRegions() throws RetailStaffingException
	{
		final List<OrgUnitDetailsTO> orgList = new ArrayList<OrgUnitDetailsTO>();
		MapStream inputs = new MapStream("readAIMSUsingRegions");
		try
		{
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					OrgUnitDetailsTO orgUnitDetailsTO = null;
					while(results.next())
					{
						orgUnitDetailsTO = new OrgUnitDetailsTO();
						if(results.getString("humanResourcesSystemOperationsGroupCode") != null)
						{
							orgUnitDetailsTO.setCode(StringUtils.trim(results.getString("humanResourcesSystemOperationsGroupCode")));
							orgUnitDetailsTO.setDesciption(StringUtils.trim(results.getString("humanResourcesSystemOperationsGroupName")));
							orgList.add(orgUnitDetailsTO);
						}

					}
				}
			});
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_AVAILABLE_REGIONS_ERROR_CODE, e);
		}
		return orgList;
	}
	
	/**
	 * This method will fetch all the available regions from DB.
	 * 
	 * @return list of OrgUnitDetailsTO
	 * @throws QueryException
	 */
	public List<OrgUnitDetailsTO> getAllAvailableRegionsDAO20() throws QueryException
	{
		List<OrgUnitDetailsTO> readResultsList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_READ_AIMS_USING_REGIONS)
				.debug(logger)
				.list(OrgUnitDetailsTO.class);

		return readResultsList;
	}

	/**
	 * This method will fetch all the available divisions from DB.
	 * 
	 * @return list of OrgUnitDetailsTO
	 * @throws QueryException
	 */
	public List<OrgUnitDetailsTO> getAllAvailableDivs() throws RetailStaffingException
	{
		final List<OrgUnitDetailsTO> orgList = new ArrayList<OrgUnitDetailsTO>();
		MapStream inputs = new MapStream("readAIMSUsingDivisions");
		try
		{
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					OrgUnitDetailsTO orgUnitDetailsTO = null;
					while(results.next())
					{
						orgUnitDetailsTO = new OrgUnitDetailsTO();
						if(results.getString("humanResourcesSystemDivisionCode") != null)

						{
							orgUnitDetailsTO.setCode(StringUtils.trim(results.getString("humanResourcesSystemDivisionCode")));
							orgUnitDetailsTO.setDesciption(StringUtils.trim(results.getString("humanResourcesSystemDivisionName")));
							orgList.add(orgUnitDetailsTO);
						}

					}
				}
			});
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_AVAILABLE_DIVS_ERROR_CODE, e);
		}
		return orgList;
	}

	/**
	 * This method will fetch all the available divisions from DB.
	 * 
	 * @return list of OrgUnitDetailsTO
	 * @throws QueryException
	 */
	public List<OrgUnitDetailsTO> getAllAvailableDivsDAO20() throws QueryException
	{
		List<OrgUnitDetailsTO> readResultsList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_READ_AIMS_USING_DIVISIONS)
				.debug(logger)
				.list(OrgUnitDetailsTO.class);

		return readResultsList;
	}
	
	/**
	 * This method will fetch all the available districts from DB.
	 * 
	 * @return list of OrgUnitDetailsTO
	 * @throws QueryException
	 */
	public List<OrgUnitDetailsTO> getAllAvailableDists() throws RetailStaffingException
	{
		final List<OrgUnitDetailsTO> orgList = new ArrayList<OrgUnitDetailsTO>();
		MapStream inputs = new MapStream("readAIMSUsingDistricts");
		try
		{
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					OrgUnitDetailsTO orgUnitDetailsTO = null;
					while(results.next())
					{
						orgUnitDetailsTO = new OrgUnitDetailsTO();
						orgUnitDetailsTO.setCode(StringUtils.trim(results.getString("humanResourcesSystemRegionCode")));
						orgUnitDetailsTO.setDesciption(StringUtils.trim(results.getString("humanResourcesSystemRegionName")));
						orgList.add(orgUnitDetailsTO);
					}
				}
			});
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_AVAILABLE_DISTS_ERROR_CODE, e);
		}
		return orgList;
	}

	/**
	 * This method will fetch all the available districts from DB.
	 * 
	 * @return list of OrgUnitDetailsTO
	 * @throws QueryException
	 */
	public List<OrgUnitDetailsTO> getAllAvailableDistsDAO20() throws QueryException
	{
		List<OrgUnitDetailsTO> readResultsList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_READ_AIMS_USING_DISTRICTS)
				.debug(logger)
				.list(OrgUnitDetailsTO.class);

		return readResultsList;
	}
	
	/**
	 * This method will fetch all the available departments by store from DB.
	 * 
	 * @return list of DeptDetailsTO
	 * @throws QueryException
	 */
	public List<DeptDetailsTO> getDeptsByStr(String strNo) throws RetailStaffingException
	{
		final List<DeptDetailsTO> deptList = new ArrayList<DeptDetailsTO>();

		// Will Need to Change
		MapStream inputs = new MapStream("readNlsHumanResourcesSystemDeptByStoreNumber");

		logger.info("In getDeptsByStr and Str No passed = " + strNo);
		try
		{
			inputs.put("humanResourcesSystemStoreNumber", strNo);
			// TODO : pass this in
			inputs.put("languageCode", "EN_US");

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					DeptDetailsTO deptDetailsTO = null;
					while(results.next())
					{
						// Exclude Management Departments, Anything with a
						// Department Number < 10, but include Dept 4
						if(Integer.parseInt(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber"))) >= 10
						    || Integer.parseInt(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber"))) == 4)
						{
							deptDetailsTO = new DeptDetailsTO();
							deptDetailsTO.setDeptCode(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
							deptDetailsTO.setShortDesc(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")) + " - "
							    + StringUtils.trim(results.getString("humanResourcesSystemDepartmentName")));
							deptList.add(deptDetailsTO);
						}
					}
				}
			});

		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_AVAILABLE_DISTS_ERROR_CODE, e);
		}
		return deptList;
	}

	/**
	 * This method will fetch all the available Job Titles by departments by
	 * store from DB.
	 * 
	 * @return list of JobTtlDetailsTO
	 * @throws QueryException
	 */
	public List<JobTtlDetailsTO> getJobTtlsByDeptByStr(String strNo, String deptNo) throws RetailStaffingException
	{
		final List<JobTtlDetailsTO> jobTtlList = new ArrayList<JobTtlDetailsTO>();

		// Will Need to Change
		MapStream inputs = new MapStream("readNlsJobTitleByHumanResourcesDepartmentNumber");

		logger.info("In getJobTtlsByDeptByStr and Str No passed = " + strNo + " and Dept = " + deptNo);
		try
		{
			inputs.put("humanResourcesSystemDepartmentNumber", deptNo);
			inputs.put("humanResourcesSystemStoreNumber", strNo);
			// todo : pass this in
			inputs.put("languageCode", "EN_US");

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					JobTtlDetailsTO jobTtlDetailsTO = null;
					while(results.next())
					{
						jobTtlDetailsTO = new JobTtlDetailsTO();
						jobTtlDetailsTO.setJobTtlCode(StringUtils.trim(results.getString("jobTitleCode")));
						jobTtlDetailsTO.setShortDesc(StringUtils.trim(results.getString("jobTitleCode")) + " - "
						    + StringUtils.trim(results.getString("jobTitleDescription")));
						jobTtlList.add(jobTtlDetailsTO);
					}
				}
			});

		}
		catch (Exception e)
		{
			throw new RetailStaffingException(FETCHING_AVAILABLE_DISTS_ERROR_CODE, e);
		}
		return jobTtlList;
	}
}

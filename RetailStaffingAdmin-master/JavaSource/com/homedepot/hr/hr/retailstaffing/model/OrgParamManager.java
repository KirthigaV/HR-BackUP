package com.homedepot.hr.hr.retailstaffing.model;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: OrgParamManager.java
 * 
 * Application: RetailStaffing
 *
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.OrgParamTO;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.basic.TimeLimitedMap;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;
/**
 * This class contains methods related to retrieving HR Organization Parameters 
 */
public class OrgParamManager implements RetailStaffingConstants, DAOConstants
{
	// DAO selector constants
	private static final String SEL_READ_HR_ORG_PARAM = "readHumanResourcesOrganizationParameter";
	private static final String SEL_READ_HR_ORG_PARAMS = "readHumanResourcesOrganizationParameterByInputList";
	
	// column constants
	private static final String SUB_SYS_CD = "subSystemCode";
	private static final String BU_ID = "businessUnitId";
	private static final String PARAM_ID = "parameterId";
	private static final String PRCSS_TYP_IND = "processTypeIndicator";
	
	private static final String CRT_USER_ID = "createSystemUserId";
	private static final String CRT_TS = "createTimestamp";
	private static final String LAST_UPD_USER_ID = "lastUpdateSystemUserId";
	private static final String LAST_UPD_TS = "lastUpdateTimestamp";
	private static final String PARM_DESC = "parameterDescription";
	private static final String PARM_INT_VAL = "parameterIntegerValue";
	private static final String PARM_DEC_VAL = "parameterDecimalValue";
	private static final String PARM_CHAR_VAL = "parameterCharacterValue";
	private static final String PARM_DATE = "parameterDate";
	private static final String PARM_TIME = "parameterTime";
	private static final String PARM_TS = "parameterTimestamp";
	private static final String PARM_DATA_TYP_IND = "parameterDataTypeIndicator";
	
	private static final String PARM_ID_SEARCH = "parameterIdSearch";

	// amount of parameters information will be cached before the cache is cleared (4 hours)
	private static final long CACHE_INTERVAL = 14400000;
	
	// logger instance
	private static final Logger mLogger = Logger.getLogger(OrgParamManager.class);
	
	// collection that will be used to cache parameters up to CACHE_INTERVAL (currently 4 hours)
	private static Map<ParameterCacheKey, OrgParamTO> mParameterCache = 
		new TimeLimitedMap<ParameterCacheKey, OrgParamTO>(Collections.synchronizedMap(new HashMap<ParameterCacheKey, OrgParamTO>()), CACHE_INTERVAL);
	
	/**
	 * Get the HR Organization parameter using the key values provided
	 * 
	 * @param subSysCd							Sub-system code of the parameter
	 * @param buId								Business unit ID of the parameter
	 * @param paramId							ID of the parameter
	 * @param prcssTypInd						Process type indicator of the parameter
	 * @param cacheParameter					true if the parameter should be cached, false otherwise
	 * 
	 * @return									Parameter details that match the key data provided or
	 * 											null if a matching parameter could not be found
	 * 
	 * @throws IllegalArgumentException			Thrown if any of the following conditions are true:
	 * 											<ul>
	 * 												<li>the sub-system code provided is null or empty</li>
	 * 												<li>the business unit id provided is null or empty</li>
	 * 												<li>the parameter id provided is null or empty</li>
	 * 												<li>the process type indicator provided is null or empty</li>
	 * 											</ul>
	 * @throws QueryException					Thrown if an exception occurs querying the database
	 */
	public static OrgParamTO getOrgParam(String subSysCd, String buId, String paramId, String prcssTypInd, boolean cacheParameter) throws IllegalArgumentException, QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getOrgParam(), subSysCd: %1$s, buId: %2$s, paramId: %3$s, prcssTypInd: %4$s, cacheParameter: %5$b", 
				subSysCd, buId, paramId, prcssTypInd, cacheParameter));
		} // end if
		
		OrgParamTO param = null;
		
		try
		{
			// validate the subsystem code provided is not null or empty
			if(subSysCd == null || subSysCd.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s subsystem code provided", (subSysCd == null ? "Null" : "Empty")));
			} // end if(subSysCd == null || subSysCd.trim().length() == 0)
			
			// validate the buId provided is not null or empty
			if(buId == null || buId.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s business unit id provided", (buId == null ? "Null" : "Empty")));
			} // end if(buId == null || buId.trim().length() == 0)
			
			// validate the parameter id provided is not null or empty
			if(paramId == null || paramId.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s parameter id provided", (paramId == null ? "Null" : "Empty")));
			} // end if(paramId == null || paramId.trim().length() == 0)
			
			// validate the process type indicator provided is not null or empty
			if(prcssTypInd == null || prcssTypInd.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s process type indicator provided", (prcssTypInd == null ? "Null" : "Empty")));
			} // end if(prcssTypInd == null || prcssTypInd.trim().length() == 0)
			
			ParameterCacheKey cacheKey = null;
			
			// if this parameter is flagged to be cached, check the cache before querying the database
			if(cacheParameter)
			{
				mLogger.debug(String.format("Org parameter with subsystem code %1$s, business unit id %2$s, parameter id %3$s, and process type indicator %4$s is flagged to be cached, checking to see if it's already cached",
					subSysCd, buId, paramId, prcssTypInd));
				
				// create a cache key
				cacheKey = new ParameterCacheKey(subSysCd, buId, paramId, prcssTypInd);
				// try to get the parameter from the cache
				param = mParameterCache.get(cacheKey);
				
				if(mLogger.isDebugEnabled())
				{
					if(param != null)
					{
						mLogger.debug(String.format("Found org parameter with subsystem code %1$s, business unit id %2$s, parameter id %3$s, and process type indicator %4$s in the cache",
							subSysCd, buId, paramId, prcssTypInd));
					} // end if(param != null)
					else
					{
						mLogger.debug(String.format("Org parameter with subsystem code %1$s, business unit id %2$s, parameter id %3$s, and process type indicator %4$s WAS NOT in the cache",
							subSysCd, buId, paramId, prcssTypInd));
					} // end else
				} // end if
			} // end if(cacheParameter)
		
			// if the parameter was not cached, or not supposed to be cached, query the DB for it
			if(param == null)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Querying for org parameter data for subsystem code: %1$s, business unit id: %2$s, parameter id: %3$s, and process type indicator: %4$s",
						subSysCd, buId, paramId, prcssTypInd));
				} // end if

				// initialize the inputs required to run the query
				MapStream inputs = new MapStream(SEL_READ_HR_ORG_PARAM);
				inputs.put(SUB_SYS_CD, subSysCd);
				inputs.put(BU_ID, buId);
				inputs.put(PARAM_ID, paramId);
				inputs.put(PRCSS_TYP_IND, prcssTypInd);
				
				// create a collection that can be accessed from the anonymous inner class that will be used to read the results
				final List<OrgParamTO> parameters = new ArrayList<OrgParamTO>();
				
				// execute the query
				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{				
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						// if a record was returned
						if(results.next())
						{
							OrgParamTO parameter = new OrgParamTO();
							
							parameter.setCrtSysUserId(results.getString(CRT_USER_ID));
							parameter.setCrtTs(results.getTimestamp(CRT_TS));
							parameter.setLastUpdSysUserId(results.getString(LAST_UPD_USER_ID));
							parameter.setLastUpdTs(results.getTimestamp(LAST_UPD_TS));
							parameter.setDesc(results.getString(PARM_DESC));
							parameter.setIntVal(results.getInt(PARM_INT_VAL));
							parameter.setDecimalVal(results.getBigDecimal(PARM_DEC_VAL));
							parameter.setCharVal(results.getString(PARM_CHAR_VAL));
							parameter.setDateVal(results.getDate(PARM_DATE));
							parameter.setTimeVal(results.getTime(PARM_TIME));
							parameter.setTsVal(results.getTimestamp(PARM_TS));
							parameter.setDataTypeInd(results.getString(PARM_DATA_TYP_IND));
							
							// add the parameter to the list
							parameters.add(parameter);
						} // end if(results.next())
					} // end function readResults()
				}); // end anonymous inner class	
				
				// if a parameter was found, set the return value
				if(!parameters.isEmpty())
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Found organization parameter value using subSysCd: %1$s, buId: %2$s, paramId: %3$s, prcssTypInd: %4$s",
							subSysCd, buId, paramId, prcssTypInd));
					} // end if

					// set the return value
					param = parameters.get(0);
					
					// if this parameter should be cached
					if(cacheParameter && (cacheKey != null))
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Adding organization parameter with subsystem code %1$s, business unit id %2$s, parameter id %3$s, and process type indicator %4$s to the cache",
								subSysCd, buId, paramId, prcssTypInd));
						} // end if
						
						// add it to the cache
						mParameterCache.put(cacheKey, param);
					} // end if(cacheParameter && (cacheKey != null))
				} // end if(!parameters.isEmpty())				 
			} // end if(param == null)			
		} // end try
		catch(IllegalArgumentException iae)
		{
			// make sure the error gets logged
			mLogger.error(String.format("Illegal Argument Provided: %1$s", iae.getMessage()), iae);
			// throw the error
			throw iae;
		} // end catch
		catch(QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred retrieving organization parameter value using subSysCd: %1$s, buId: %2$s, paramId: %3$s, prcssTypInd: %4$s",
				subSysCd, buId, paramId, prcssTypInd));
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getOrgParam(), subSysCd: %1$s, buId: %2$s, paramId: %3$s, prcssTypInd: %4$s, cacheParameter: %5$b",
				subSysCd, buId, paramId, prcssTypInd, cacheParameter));
		} // end if
		
		return param;
	} // end function getOrgParam()
	
	/**
	 * Get the HR Organization parameters using the key values provided
	 * 
	 * @param subSysCd							Sub-system code of the parameter
	 * @param buId								Business unit ID of the parameter
	 * @param paramId							ID of the parameter
	 * @param prcssTypInd						Process type indicator of the parameter (this is an optional value)
	 * @param isLikeSearch						True if this is a like search using the parameter id, false otherwise
	 * 
	 * @return									List of parameters matching the key data provided or an empty list if no matching
	 * 											parameters could be found
	 * 
	 * @throws IllegalArgumentException			Thrown if any of the following conditions are true:
	 * 											<ul>
	 * 												<li>the sub-system code provided is null or empty</li>
	 * 												<li>the business unit id provided is null or empty</li>
	 * 												<li>the parameter id provided is null or empty</li>
	 * 											</ul>
	 * @throws QueryException					Thrown if an exception occurs querying the database
	 */
	public static List<OrgParamTO> getOrgParams(String subSysCd, String buId, String paramId, String prcssTypInd, boolean isLikeSearch) throws IllegalArgumentException, QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getOrgParams(), subSysCd: %1$s, buId: %2$s, paramId: %3$s, prcssTypInd: %4$s, isLikeSearch: %5$b", 
				subSysCd, buId, paramId, prcssTypInd, isLikeSearch));
		} // end if
		
		// create a collection that can be accessed from the anonymous inner class that will be used to read the results
		final List<OrgParamTO> parameters = new ArrayList<OrgParamTO>();

		try
		{
			// validate the subsystem code provided is not null or empty
			if(subSysCd == null || subSysCd.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s subsystem code provided", (subSysCd == null ? "Null" : "Empty")));
			} // end if(subSysCd == null || subSysCd.trim().length() == 0)
			
			// validate the buId provided is not null or empty
			if(buId == null || buId.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s business unit id provided", (buId == null ? "Null" : "Empty")));
			} // end if(buId == null || buId.trim().length() == 0)
			
			// validate the parameter id provided is not null or empty
			if(paramId == null || paramId.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s parameter id provided", (paramId == null ? "Null" : "Empty")));
			} // end if(paramId == null || paramId.trim().length() == 0)

			// process type indicator is optional, so not going to validate it
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting org parameters for subsystem code: %1$s, business unit id: %2$s, parameter id: %3$s, process type indicator: %4$s, like search: %5$b",
					subSysCd, buId, paramId, prcssTypInd, isLikeSearch));
			} // end if

			// initialize the inputs required to run the query
			MapStream inputs = new MapStream(SEL_READ_HR_ORG_PARAMS);
			inputs.put(SUB_SYS_CD, subSysCd);
			inputs.put(BU_ID, buId);
			inputs.put(PARAM_ID, paramId);
			
			// only add the process type indicator if it's not null or empty
			if(prcssTypInd != null && prcssTypInd.trim().length() > 0)
			{
				inputs.put(PRCSS_TYP_IND, prcssTypInd);	
				mLogger.debug("Added process type indicator to query inputs");
			} // end if(prcssTypInd != null && prcssTypInd.trim().length() > 0)
			
			// only add the like qualifier if isLikeSearch is true
			if(isLikeSearch)
			{
				inputs.addQualifier(PARM_ID_SEARCH, isLikeSearch);
				mLogger.debug("Added like qualifier to query inputs");
			} // end if(isLikeSearch)
						
			// execute the query
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{				
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					OrgParamTO parameter = null;
					
					// if records were returned, iterate them and add them to the return collection
					while(results.next())
					{
						parameter = new OrgParamTO();
						
						parameter.setCrtSysUserId(results.getString(CRT_USER_ID));
						parameter.setCrtTs(results.getTimestamp(CRT_TS));
						parameter.setLastUpdSysUserId(results.getString(LAST_UPD_USER_ID));
						parameter.setLastUpdTs(results.getTimestamp(LAST_UPD_TS));
						parameter.setDesc(results.getString(PARM_DESC));
						parameter.setIntVal(results.getInt(PARM_INT_VAL));
						parameter.setDecimalVal(results.getBigDecimal(PARM_DEC_VAL));
						parameter.setCharVal(results.getString(PARM_CHAR_VAL));
						parameter.setDateVal(results.getDate(PARM_DATE));
						parameter.setTimeVal(results.getTime(PARM_TIME));
						parameter.setTsVal(results.getTimestamp(PARM_TS));
						parameter.setDataTypeInd(results.getString(PARM_DATA_TYP_IND));
						
						// add the parameter to the list
						parameters.add(parameter);						
					} // end while(results.next())
				} // end function readResults()
			}); // end anonymous inner class	

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Found %1$d organization parameter values using subSysCd: %2$s, buId: %3$s, paramId: %4$s, prcssTypInd: %5$s, isLikeSearch: %6$b",
					parameters.size(), subSysCd, buId, paramId, prcssTypInd, isLikeSearch));
			} // end if				
		} // end try
		catch(IllegalArgumentException iae)
		{
			// make sure the error gets logged
			mLogger.error(String.format("Illegal Argument Provided: %1$s", iae.getMessage()), iae);
			// throw the error
			throw iae;
		} // end catch
		catch(QueryException qe)
		{
			// make sure the error gets logged
			mLogger.error(String.format("An exception occurred retrieving organization parameter value using subSysCd: %1$s, buId: %2$s, paramId: %3$s, prcssTypInd: %4$s, isLikeSearch: %5$b",
				subSysCd, buId, paramId, prcssTypInd, isLikeSearch));
			// throw the error
			throw qe;
		} // end catch

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getOrgParams(), subSysCd: %1$s, buId: %2$s, paramId: %3$s, prcssTypInd: %4$s, isLikeSearch: %5$b", 
				subSysCd, buId, paramId, prcssTypInd, isLikeSearch));
		} // end if
		
		return parameters;
	} // end function getOrgParams()
	
	/*
	 * Key class for the org parameter cache
	 */
	private static class ParameterCacheKey implements Serializable
	{
        private static final long serialVersionUID = 638277804992905069L;
        
        // subsystem code of the parameter
		private String mSubSysCd;
		// business unit id of the parameter
		private String mBuId;
		// parameter id of the parameter
		private String mParamId;
		// process type code of the parameter
		private String mPrcssTypCd;
		
		public ParameterCacheKey(String subsysCd, String buId, String paramId, String prcssTypCd)
		{
			setSubSysCd(subsysCd);
			setBuId(buId);
			setParamId(paramId);
			setPrcssTypCd(prcssTypCd);
		} // end constructor
		
		/**
		 * Get the subsystem code of the parameter
		 * 
		 * @return				The subsystem code of the parameter
		 */
		public String getSubSysCd()
		{
			return mSubSysCd;
		} // end function getSubSysCd()
		
		/**
		 * Set the subsystem code of the parameter
		 * 
		 * @param subSysCd		The subsystem code of the parameter
		 */
		public void setSubSysCd(String subSysCd)
		{
			mSubSysCd = subSysCd;
		} // end function setSubSysCd()
		
		/**
		 * Get the business unit id of the parameter
		 * 
		 * @return				The business unit id of the parameter
		 */
		public String getBuId()
		{
			return mBuId;
		} // end function getBuId()
		
		/**
		 * Set the business unit id of the parameter
		 * 
		 * @param buId			The business unit id of the parameter
		 */
		public void setBuId(String buId)
		{
			mBuId = buId;
		} // end function setBuId()
	
		/**
		 * Get the parameter id of the parameter
		 * 
		 * @return				The parameter id of the parameter
		 */
		public String getParamId()
		{
			return mParamId;
		} // end function getParamId()
		
		/**
		 * Set the parameter id of the parameter
		 * 
		 * @param paramId		The parameter id of the parameter
		 */
		public void setParamId(String paramId)
		{
			mParamId = paramId;
		} // end function setParamId()
		
		/**
		 * Get the process type code of the parameter
		 * 
		 * @return				The process type code of the parameter
		 */
		public String getPrcssTypCd()
		{
			return mPrcssTypCd;
		} // end function getPrcssTypCd()
		
		/**
		 * Set the process type code of the parameter
		 * 
		 * @param typCd			The process type code of the parameter
		 */
		public void setPrcssTypCd(String typCd)
		{
			mPrcssTypCd = typCd;
		} // end function setPrcssTypCd()

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
        public int hashCode()
        {
	        final int prime = 31;
	        int result = 1;
	        result = prime * result + ((mBuId == null) ? 0 : mBuId.hashCode());
	        result = prime * result + ((mParamId == null) ? 0 : mParamId.hashCode());
	        result = prime * result + ((mPrcssTypCd == null) ? 0 : mPrcssTypCd.hashCode());
	        result = prime * result + ((mSubSysCd == null) ? 0 : mSubSysCd.hashCode());
	        return result;
        } // end function hashCode()

		/*
		 * (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
        public boolean equals(Object obj)
        {
			if(this == obj)
			{
				return true;
			} // end if(this == obj)
			
			if(obj == null)
			{
				return false;
			} // end if(obj == null)
			
			if(getClass() != obj.getClass())
			{
				return false;
			} // end if(getClass() != obj.getClass())
			
	        ParameterCacheKey other = (ParameterCacheKey)obj;
	        
	        if(mBuId == null)
	        {
	        	if(other.getBuId() != null)
	        	{
	        		return false;
	        	} // end if(other.getBuId() != null)
	        } // end if(mBuId == null)
	        else if(!mBuId.equals(other.getBuId()))
	        {
	        	return false;
	        } // end else if(!mBuId.equals(other.getBuId()))
	        
	        if(mParamId == null)
	        {
	        	if(other.getParamId() != null)
	        	{
	        		return false;
	        	} // end if(other.getParamId() != null)
	        } // end if(mParamId == null)
	        else if(!mParamId.equals(other.getParamId()))
	        {
	        	return false;
	        } // end else if(!mParamId.equals(other.getParamId()))

	        if(mPrcssTypCd == null)
	        {
	        	if(other.getPrcssTypCd() != null)
	        	{
	        		return false;
	        	} // end if(getPrcssTypCd() != null)
	        } // end if(mPrcssTypCd == null)
	        else if(!mPrcssTypCd.equals(other.getPrcssTypCd()))
	        {
	        	return false;
	        } // end else if(!mPrcssTypCd.equals(other.getPrcssTypCd()))
	        
	        if(mSubSysCd == null)
	        {
	        	if(other.getSubSysCd() != null)
	        	{
	        		return false;
	        	} // end if(getSubSysCd() != null)
	        } // end if(mSubSysCd == null)
	        else if(!mSubSysCd.equals(other.getSubSysCd()))
	        {
	        	return false;
	        } // end else if(!mSubSysCd(other.getSubSysCd()))	        

	        return true;
        } // end function equals()
	} // end class ParameterMapKey	
} // end class OrgParamManager
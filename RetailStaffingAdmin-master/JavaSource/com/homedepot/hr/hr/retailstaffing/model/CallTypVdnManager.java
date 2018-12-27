package com.homedepot.hr.hr.retailstaffing.model;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: CallTypVdnManager.java
 * 
 * Application: RetailStaffing
 */
import java.util.Collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.CallTypVdnKeyTO;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.interfaces.ValidationConstants;
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
 * This class contains methods related to call type vector destination numbers (a.k.a. VDN)
 */
public class CallTypVdnManager implements RetailStaffingConstants, DAOConstants, ValidationConstants
{
	// amount of time job category information will be cached before the cache is cleared (4 hours)
	private static final long CACHE_INTERVAL = 14400000;
		
	private static final String SEL_READ_HR_CALLTYPE_VDN = "readHumanResourcesCallTypeVDNByShortTypeDescription";
	private static final String S_TYP_DESC = "shortTypeDescription";
	private static final String LANG_CD = "languageCode";
	private static final String STRINTV_GUIDE_ID = "strintvGuideId";
	private static final String VDN_NBR = "vectorDestinationNumberNumber";
	
	// logger instance
	private static final Logger mLogger = Logger.getLogger(CallTypVdnManager.class);
	
	// cache that will store VDNs for CACHE_INTERVAL before forcing a refresh
	private static Map<CallTypVdnKeyTO, Integer> mVdnCache = 
		new TimeLimitedMap<CallTypVdnKeyTO, Integer>(Collections.synchronizedMap(new HashMap<CallTypVdnKeyTO, Integer>()), CACHE_INTERVAL);
	
	/**
	 * This method gets the call type vector destination number (a.k.a. VDN) for the criteria provided
	 * 
	 * @param langCd							language code (i.e. en_US)
	 * @param strIntvGuideId					structured interview guide id
	 * @param callType							call type
	 * 
	 * @return									The vector destination number for the criteria provided
	 * 
	 * @throws IllegalArgumentException			Thrown if:
	 * 											<ul>
	 * 												<li>the langCd is null or empty</li>
	 * 												<li>The strIntvGuideId is null or empty</li>
	 * 												<li>The callType is null or empty</li>
	 * 											</ul>
	 * 
	 * @throws QueryException					Thrown if an exception occurs querying the database for the vector 
	 * 											destination number
	 */
	public static int getCallTypeVdn(String langCd, String strIntvGuideId, String callType) throws IllegalArgumentException, QueryException
	{
		int vdn = 0;
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getCallTypeVdn, langCd: %1$s, strIntvGuideId: %2$s, callType: %3$s", langCd, strIntvGuideId, callType));
		} // end if
		
		try
		{
			// validate the language code provided is not null or empty
			if(langCd == null || langCd.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s language code provided", (langCd == null ? "Null" : "Empty")));
			} // end if(langCd == null || langCd.trim().length() == 0)
			
			// validate the language code provided is in the correct format (en_US)
			if(!Pattern.matches(VALID_REGEX_LANG_CD, langCd))
			{
				throw new IllegalArgumentException(String.format("Invalid language code %1$s provided", langCd));
			} // end if(!Pattern.matches(VALID_REGEX_LANG_CD, langCd))
			
			// validate the structured interview guide provided
			if(strIntvGuideId == null || strIntvGuideId.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s structured interview guide provided", (strIntvGuideId == null ? "Null" : "Empty")));
			} // end if(strIntvGuideId == null || strIntvGuideId.trim().length() == 0)
			
			// validate the call type provided
			if(callType == null || callType.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s call type provided", (callType == null ? "Null" : "Empty")));
			} // end if(callType == null || callType.trim().length() == 0)
			
			// create a key using the input data provided
			CallTypVdnKeyTO key = new CallTypVdnKeyTO();
			key.setLangCd(langCd);
			key.setStrIntvGuideId(strIntvGuideId);
			key.setTypDesc(callType);

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Checking cache for VDN using langCd: %1$s, strIntvGuideId: %2$s, and callType: %3$s", langCd, strIntvGuideId, callType));
			} // end if

			// check the cache for the VDN
			if(mVdnCache.containsKey(key))			
			{
				vdn = mVdnCache.get(key);
				
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("VDN %1$d found in cache using langCd: %2$s, strIntvGuideId: %3$s, and callType: %4$s", vdn, langCd, strIntvGuideId, callType));
				} // end if
			} // end if(mVdnCache.containsKey(key))
			else // the VDN is not in the cache
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("VDN not found in cache, querying database for VDN using langCd: %1$s, strIntvGuideId: %2$s, and callType: %3$s", 
						langCd, strIntvGuideId, callType));
				} // end if
				
				// setup the inputs needed to execute the query
				MapStream inputs = new MapStream(SEL_READ_HR_CALLTYPE_VDN);
				inputs.put(S_TYP_DESC, callType);
				inputs.put(LANG_CD, langCd);
				inputs.put(STRINTV_GUIDE_ID, strIntvGuideId);
				
				// create a list the anonymous inner class can write results to
				final List<Integer> vdns = new ArrayList<Integer>();
				
				// execute the query				
				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, 
					new ResultsReader()
					{					
						//@Override
                        public void readResults(Results results, Query query, Inputs inputs) throws QueryException
                        {
							// if a result is returned
							if(results.next())
							{
								vdns.add(results.getInt(VDN_NBR));
							} // end if(results.next())
                        } // end method readResults()
					} // end anonymous resultsReader
				); // end call to getResults()
				
				// if the VDN was found
				if(vdns.size() == 1)
				{
					// get the VDN
					vdn = vdns.get(0);
					// add the VDN to the cache
					mVdnCache.put(key, vdn);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Added VDN %1$d to the cache with key langCd: %2$s, strIntvGuideId: %3$s, and callType: %4$s", vdn, langCd, strIntvGuideId, callType));
					} // end if
				} // end if(vdns.size() == 1)
				else
				{
					// the VDN was not found, so throw a QueryException
					throw new QueryException(String.format("No VDN found using langCd: %1$s, strIntvGuideId: %2$s, and callType: %3$s", langCd, strIntvGuideId, callType));
				} // end else
			} // end else
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
			mLogger.error(String.format("An exception occurred querying the database for VDN using langCd: %1$s, strIntvGuideId: %2$s, and callType: %3$s", langCd, strIntvGuideId, callType), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getCallTypeVdn, langCd: %1$s, strIntvGuideId: %2$s, callType: %3$s", langCd, strIntvGuideId, callType));
		} // end if
		
		return vdn;
	} // end function getCallTypeVdn()
} // end class callTypeVdnManager
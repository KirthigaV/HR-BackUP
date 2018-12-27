/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PomRsaCrossRefHandler.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao.handlers;


import com.homedepot.ta.aa.dao.Inputs;
import org.apache.log4j.Logger;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.hr.hr.retailstaffing.dto.response.POMRsaStatusCrossRefResponse;

public class PomRsaCrossRefHandler implements ResultsReader 
{

private static final Logger logger = Logger.getLogger(PomRsaCrossRefHandler.class);
	
	POMRsaStatusCrossRefResponse response = null;
	
	public PomRsaCrossRefHandler()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("Enter PomRsaCrossRefHandler - PomRsaCrossRefHandler()");
		}
		
	} // end constructor()

	public POMRsaStatusCrossRefResponse getPOMCompleteStatus()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("Enter PomRsaCrossRefHandler - getPOMCompleteStatus()");
		}
		return response;
	} // end function getResults()

	public void readResults(Results results, Query query, Inputs inputs) throws QueryException
    {
		
		if (logger.isDebugEnabled()) {
			logger.debug("Enter PomRsaCrossRefHandler - readResults()");
		}
		// iterate the results and populate a new RequisitionCalendarTO object for each record returned
		while (results.next()) {			
			response = new POMRsaStatusCrossRefResponse();
			response.setPomCompleteStatusCode(results.getShort("pomCompleteStatusCode"));
			response.setInterviewRespondStatusCode(results.getShort("interviewRespondStatusCode"));
			response.setRsaUpdateFlag(results.getBoolean("rsaUpdateFlag"));
		}
    } // end function readResults()
}

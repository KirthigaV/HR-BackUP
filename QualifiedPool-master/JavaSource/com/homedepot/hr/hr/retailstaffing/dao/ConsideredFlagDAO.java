package com.homedepot.hr.hr.retailstaffing.dao;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionManager.java
 * Application: RetailStaffing
 */
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This class contains data access methods related to the Considered Flag
 * 
 * @author mts1876
 */
public class ConsideredFlagDAO implements Constants
{
	// selector constants
	private static final String SELECTOR_READ_CANDIDATE_CONSIDERED_FLAGS = "readEmploymentPositionCandidateRejectDetailsByInputList";
	// column constants
	private static final String COL_CAND_ID = "employmentPositionCandidateId";
	private static final String COL_REQ_NBR = "employmentRequisitionNumber";
	private static final String COL_SEQ_NUM = "sequenceNumber";
	private static final String COL_LAST_UPDATED_USERID = "lastUpdateSystemUserId";
	private static final String COL_LAST_UPDATED_TIMESTAMP = "lastUpdateTimestamp";
	private static final String COL_CAND_REJECT_CD = "employmentCandidateRejectReasonCode";
	
	/**
	 * Get the considered flags for the input provided
	 * 
	 * @param requisitionNbr				Unique identifier for the employment requisition
	 * @param userId						Current User Id
	 * 
	 * @return								List of QualifiedCandidate transfer objects
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database for the data
	 */	
	public static List<QualifiedCandidate> getConsideredCandidates(final int requisitionNbr, final String userId) throws QueryException
	{
		// List that will hold transfer object condiseredCandidates
		final List<QualifiedCandidate> theList = new ArrayList<QualifiedCandidate>();

		// create the MapStream used to pass the input values to the DAO selector
		MapStream inputs = new MapStream(SELECTOR_READ_CANDIDATE_CONSIDERED_FLAGS);
		// add the requisition input parameter
		inputs.put(COL_REQ_NBR, requisitionNbr);
		//add the userId input parameter
		inputs.put(COL_LAST_UPDATED_USERID, userId.toUpperCase());
		
		// invoke the query using the DAO
		BasicDAO.getResult(WORKFORCEEMPLOYMENTQUALIFICATIONS_CONTRACT_NAME, WORKFORCEEMPLOYMENTQUALIFICATIONS_BUID, WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION, inputs, new ResultsReader()
		{
			/*
			 * (non-Javadoc)
			 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException
			{		
				// populate a QualifiedCandidate object and add to list
				while (results.next())
				{
					QualifiedCandidate qualifiedCandidates = new QualifiedCandidate();
					qualifiedCandidates.setId(results.getString(COL_CAND_ID));
					theList.add(qualifiedCandidates);
				} // end while (results.next())
			} // end function readResults()
		}); // end ResultsReader implementation
		
		return theList;
	} // end function getConsideredCandidates()
} // end class ConsideredFlagDAO
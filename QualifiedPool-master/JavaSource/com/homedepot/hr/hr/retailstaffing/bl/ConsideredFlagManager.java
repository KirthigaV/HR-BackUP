package com.homedepot.hr.hr.retailstaffing.bl;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ConsideredFlagManager.java
 * Application: RetailStaffing
 */
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.ConsideredFlagDAO;
import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.QualifiedPoolMessage;
import com.homedepot.ta.aa.catalina.realm.Profile;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to adding the considered
 * flag to the List<QualifiedCandidate> object based on results from the DB
 * 
 * @author mts1876
 */
public class ConsideredFlagManager implements Constants {
	// logger instance
	private static final Logger mLogger = Logger.getLogger(ConsideredFlagManager.class);

	/**
	 * Add Considered Flag value to QualifiedCandidate object
	 * 
	 * @param candidates
	 *            List of candidates from Qualified Pool
	 * @param requisitionNbr
	 *            Unique identifier for the employment requisition
	 * 
	 * @return void
	 * 
	 * @throws QualifiedPoolException
	 *             Thrown if any of the following conditions are true:
	 *             <ul>
	 *             <li>The requisition number provided is &lt; 1</li>
	 *             <li>Missing User ID</li>
	 *             <li>An exception occurs querying the database for the
	 *             requisition</li>
	 *             <ul>
	 */
	public static void getConsideredFlagData(List<QualifiedCandidate> candidates, int requisitionNbr) throws QualifiedPoolException {
		long startTime = 0;
		List<QualifiedCandidate> theList = null;

		// get current user id
		Profile profile = Profile.getCurrent();
		String userId = null;
		userId = profile.getProperty(Profile.USER_ID);

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getConsideredFlagData(), requisitionNbr: %1$d, userId: %2$s", requisitionNbr, userId));
		} // end if

		try {
			// first validate the requisition number provided is valid
			if (requisitionNbr < 1) {
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_REQNBR, String.format("Invalid requisition number %1$d provided", requisitionNbr));
			} // end if(requisitionNbr < 1)

			// validate a User Id was found
			if (userId == null || userId.equals("")) {
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_USERID, String.format("Invalid userId: %1$d provided", userId));
			} // end if (userId == null || userId.equals(""))

			// invoke the DAO method to get the candidates that have been
			// considered based
			// on the Requisition Number and User ID
			theList = ConsideredFlagDAO.getConsideredCandidates(requisitionNbr, userId);

			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("Candidates with Considered Flags Returned: %1$d", theList.size()));
			}

			// Loop through the candidates list checking if they are in the
			// considered list
			// if found set ConsideredFlg to 'Y'
			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate allCandidates = (QualifiedCandidate) candidates.get(i);
				if (theList.size() == 0) {
					break;
				} // end if (theList.size() == 0)
				for (int j = 0; j < theList.size(); j++) {
					QualifiedCandidate consideredCandidates = (QualifiedCandidate) theList.get(j);
					if (consideredCandidates.getId().trim().equals(allCandidates.getId().trim())) {
						allCandidates.setConsideredFlg("Y");
						theList.remove(j);
						break;
					} else {
						allCandidates.setConsideredFlg("");
					} // end else
				} // end for (int j=0;j<theList.size();j++)
			} // end for (int i=0;i<candidates.size();i++)
		} // end try
		catch (QualifiedPoolException qpe) {
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(qpe.getMessage()));
			// make sure the error shows up in the log
			mLogger.error("An exception occurred validating input data", qpe);
			// throw the exception
			throw qpe;
		} // end catch
		catch (QueryException qe) {
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for requisition %1$d in getConsideredFlagData()", requisitionNbr)));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for requisition %1$d in getConsideredFlagData()", requisitionNbr), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting getConsideredFlagData(), requisitionNbr: %1$d, total time to process request: %2$.9f seconds", requisitionNbr,
					(((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

	} // end function getConsideredFlagData()
} // end class ConsideredFlagManager
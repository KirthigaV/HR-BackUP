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
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.QualifiedPoolDAO;
import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.QualifiedPoolMessage;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to the Associates
 *  in the List<QualifiedCandidate> object based on results from the DB
 * 
 * @author mts1876
 */
public class AssociateManager implements Constants {
	// logger instance
	private static final Logger mLogger = Logger.getLogger(AssociateManager.class);
	
	public static void getAssociatesLastReviewScore(List<QualifiedCandidate> candidates) throws QualifiedPoolException {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getAssociateLastReviewScore()"));
		} // end if

		try {
			List<String> applicants = new ArrayList<String>();

			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate temp = (QualifiedCandidate) candidates.get(i);
				applicants.add(temp.getAssociateId().trim());
			}
			
			List<QualifiedCandidate> returnedCandidates = QualifiedPoolDAO.getAssociateLastReviewDAO20(applicants);

			// Multiple review scores are returned, only pickup the 1st record which is the last review score.
			// if we got candidates back, sort and add them to the response
			if(returnedCandidates != null)
			{
				
				Collections.sort(candidates, new Comparator<QualifiedCandidate>() {
			        @Override
			        public int compare(final QualifiedCandidate object1, final QualifiedCandidate object2) {
			        	String i = object1.getAssociateId(); 
			        	int result = i.compareTo(object2.getAssociateId());
			            return result;
			        }
			    });
			}
						
			String current = "";
			String previous = "";
			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate temp = (QualifiedCandidate) candidates.get(i);
				current = temp.getAssociateId();
				for (int j = 0; j < returnedCandidates.size(); j++) {
					QualifiedCandidate temp2 = (QualifiedCandidate) returnedCandidates.get(j);
					if (!current.equals(previous)) {
						if (temp.getAssociateId().equals(temp2.getAssociateId())) {	
							temp.setReviewScore(temp2.getReviewScore());
							break;
						}
					}
				} // End - for (int j = 0; j < returnedCandidates.size(); j++) {
				previous = current;
			} // End -for (int i = 0; i < candidates.size(); i++) {	
		} // end try
		catch (QueryException qe) {
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for review scores in getAssociateLastReviewScore()")));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for review scores in getAssociateLastReviewScore()"), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting getAssociateLastReviewScore(), total time to process request: %1$.9f seconds", 
					(((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end function getAssociateLastReviewScore()
	
	public static void getAssociatesTimeInPosition(List<QualifiedCandidate> candidates) throws QualifiedPoolException {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getAssociatesTimeInPosition()"));
		} // end if

		try {
			List<String> applicants = new ArrayList<String>();
			//Get list of applicants to pass into the DAO call
			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate temp = (QualifiedCandidate) candidates.get(i);
				applicants.add(temp.getId().trim());
			}
			
			List<QualifiedCandidate> returnedCandidates = QualifiedPoolDAO.getAssociatesCurrentPositionStartDateDAO20(applicants);

			LocalDate positionStartDate;
			LocalDate currentDate = LocalDate.now();
			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate temp = (QualifiedCandidate) candidates.get(i);
				for (int j = 0; j < returnedCandidates.size(); j++) {
					QualifiedCandidate temp2 = (QualifiedCandidate) returnedCandidates.get(j);
					if (temp.getId().equals(temp2.getId())) {
						positionStartDate = LocalDate.parse(temp2.getCurrentPositionStartDate().toString());
						Period diff = Period.between(positionStartDate, currentDate);
						int numYears = diff.getYears();
						Period totalMonth = diff.plusMonths(numYears * 12);
						String timeInPosition = String.format("%1$d Mths %2$d Days", totalMonth.getMonths(), diff.getDays());
						mLogger.debug(String.format("Start Date:%1$s  TimeInPosition:%2$s", temp2.getCurrentPositionStartDate(), timeInPosition));
						temp.setTimeInPosition(timeInPosition);
						break;
					}
				} // End - for (int j = 0; j < returnedCandidates.size(); j++) {
			} // End -for (int i = 0; i < candidates.size(); i++) { 	
		} // end try
		catch (QueryException qe) {
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for review scores in getAssociatesTimeInPosition()")));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for time in position in getAssociatesTimeInPosition()"), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting getAssociatesTimeInPosition, total time to process request: %1$.9f seconds", 
					(((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end function getAssociatesTimeInPosition
	
} // end class AssociateManager
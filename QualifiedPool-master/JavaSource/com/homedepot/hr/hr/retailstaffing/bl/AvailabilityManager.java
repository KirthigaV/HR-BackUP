package com.homedepot.hr.hr.retailstaffing.bl;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: AvailabilityManager.java
 * Application: RetailStaffing
 */
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.AvailabilityDAO;
import com.homedepot.hr.hr.retailstaffing.dto.AvailabilityDetails;
import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.QualifiedPoolMessage;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to adding availability to
 * the List<QualifiedCandidate> object based on results from the DB
 * 
 * @author mts1876
 */
public class AvailabilityManager implements Constants {
	// logger instance
	private static final Logger mLogger = Logger.getLogger(AvailabilityManager.class);

	/**
	 * Add applicants Availability to the QualifiedCandidate object
	 * 
	 * @param candidates
	 *            List of candidates from Qualified Pool
	 * 
	 * @return void
	 * 
	 * @throws QualifiedPoolException
	 *             Thrown if the following condition is true:
	 *             <ul>
	 *             <li>An exception occurs querying the database for
	 *             availability</li>
	 *             <ul>
	 */
	public static void getApplicantAvailability(List<QualifiedCandidate> candidates) throws QualifiedPoolException {
		long startTime = 0;
		List<AvailabilityDetails> theList = null;
		List<String> applicants = new ArrayList<String>();

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug("Entering getApplicantAvailability() method");
		} // end if

		try {
			// Create applicant List<String> from candidates list, used in the
			// DAO call
			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate temp = (QualifiedCandidate) candidates.get(i);
				applicants.add(temp.getId().trim());
			}

			if (candidates.size() > 0) {
				// invoke the DAO method to get the availability of Applicants
				theList = AvailabilityDAO.getApplicantAvailability(applicants);

				// Loop through all candidates and add availability
				for (int i = 0; i < candidates.size(); i++) {
					QualifiedCandidate allCandidates = (QualifiedCandidate) candidates.get(i);
					if (theList.size() == 0) {
						break;
					} // end if (theList.size() == 0)
					for (int j = 0; j < theList.size(); j++) {
						AvailabilityDetails availabilityDetails = (AvailabilityDetails) theList.get(j);
						if (availabilityDetails.getApplicantId().trim().equals(allCandidates.getId().trim())) {

							StringBuilder availability = new StringBuilder();
							// Applicants Day Availability
							// Set Sunday
							if (availabilityDetails.getDaySeg().contains(7)) {
								availability.append("Sun, ");
							}
							if (availabilityDetails.getDaySeg().contains(1) || availabilityDetails.getDaySeg().contains(2) || availabilityDetails.getDaySeg().contains(3)
									|| availabilityDetails.getDaySeg().contains(4) || availabilityDetails.getDaySeg().contains(5)) {
								availability.append("Weekdays, ");
							}
							if (availabilityDetails.getDaySeg().contains(6)) {
								availability.append("Sat, ");
							}

							// Remove trailing comma
							if (availability.length() > 0) {
								availability.replace(availability.length() - 2, availability.length(), "");
							}
							// Add New Line Character
							availability.append("\n");

							// Applicants Start Time(s)
							if (availabilityDetails.getStartTimeSeg().contains(0)) {
								availability.append("Anytime, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(1)) {
								availability.append("Early Morning, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(2)) {
								availability.append("Morning, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(3)) {
								availability.append("Afternoon, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(4)) {
								availability.append("Evening, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(5)) {
								availability.append("Late Evening, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(6)) {
								availability.append("Overnight, ");
							}
							// Remove trailing comma
							if (availability.length() > 0) {
								availability.replace(availability.length() - 2, availability.length(), "");
							}
							// Set availability of the applicant on the
							// candidates
							// list passed in to method.
							allCandidates.setAvailability(availability.toString());
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Applicant:%1$s Availability:%2$s", allCandidates.getId().trim(), allCandidates.getAvailability()));
							}
							theList.remove(j);
							break;
						} else {
							allCandidates.setAvailability("");
						} // end else
					} // end for (int j=0;j<theList.size();j++)
				} // end for (int i=0;i<candidates.size();i++)
			} // End if (candidates.size() > 0)
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
			mLogger.warn(new QualifiedPoolMessage("An exception occurred querying for Applicant Availability Data"));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for Applicant Availability Data"), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting getApplicantAvailability() method, total time to process request: %1$.9f seconds", (((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end function getApplicantAvailability()

	/**
	 * Add associate Availability to the QualifiedCandidate object
	 * 
	 * @param candidates
	 *            List of candidates from Qualified Pool
	 * 
	 * @return void
	 * 
	 * @throws QualifiedPoolException
	 *             Thrown if the following condition is true:
	 *             <ul>
	 *             <li>An exception occurs querying the database for
	 *             availability</li>
	 *             <ul>
	 */
	public static void getAssociateAvailability(List<QualifiedCandidate> candidates) throws QualifiedPoolException {
		long startTime = 0;
		List<AvailabilityDetails> theList = null;
		List<String> applicants = new ArrayList<String>();

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug("Entering getAssociateAvailability() method");
		} // end if

		try {
			// Create associate List<String> from candidates list, used in the
			// DAO call
			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate temp = (QualifiedCandidate) candidates.get(i);
				applicants.add(temp.getId().trim());
			}

			if (candidates.size() > 0) {
				// invoke the DAO method to get the availability of Associates
				theList = AvailabilityDAO.getAssociateAvailability(applicants);

				// Loop through all candidates and add availability
				for (int i = 0; i < candidates.size(); i++) {
					QualifiedCandidate allCandidates = (QualifiedCandidate) candidates.get(i);
					if (theList.size() == 0) {
						break;
					} // end if (theList.size() == 0)
					for (int j = 0; j < theList.size(); j++) {
						AvailabilityDetails availabilityDetails = (AvailabilityDetails) theList.get(j);
						if (availabilityDetails.getApplicantId().trim().equals(allCandidates.getId().trim())) {

							StringBuilder availability = new StringBuilder();
							// Applicants Day Availability
							// Set Sunday
							if (availabilityDetails.getDaySeg().contains(7)) {
								availability.append("Sun, ");
							}
							if (availabilityDetails.getDaySeg().contains(1) || availabilityDetails.getDaySeg().contains(2) || availabilityDetails.getDaySeg().contains(3)
									|| availabilityDetails.getDaySeg().contains(4) || availabilityDetails.getDaySeg().contains(5)) {
								availability.append("Weekdays, ");
							}
							if (availabilityDetails.getDaySeg().contains(6)) {
								availability.append("Sat, ");
							}

							// Remove trailing comma
							if (availability.length() > 0) {
								availability.replace(availability.length() - 2, availability.length(), "");
							}
							// Add New Line Character
							availability.append("\n");

							// Applicants Start Time(s)
							if (availabilityDetails.getStartTimeSeg().contains(0)) {
								availability.append("Anytime, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(1)) {
								availability.append("Early Morning, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(2)) {
								availability.append("Morning, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(3)) {
								availability.append("Afternoon, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(4)) {
								availability.append("Evening, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(5)) {
								availability.append("Late Evening, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(6)) {
								availability.append("Overnight, ");
							}
							// Remove trailing comma
							if (availability.length() > 0) {
								availability.replace(availability.length() - 2, availability.length(), "");
							}
							// Set availability of the associate on the
							// candidates
							// list passed in to method.
							allCandidates.setAvailability(availability.toString());
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Associate:%1$s Availability:%2$s", allCandidates.getId().trim(), allCandidates.getAvailability()));
							}
							theList.remove(j);
							break;
						} else {
							allCandidates.setAvailability("");
						} // end else
					} // end for (int j=0;j<theList.size();j++)
				} // end for (int i=0;i<candidates.size();i++)
			} // end if (candidates.size() > 0)
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
			mLogger.warn(new QualifiedPoolMessage("An exception occurred querying for Associate Availability Data"));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for Associate Availability Data"), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting getAssociateAvailability() method, total time to process request: %1$.9f seconds", (((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end function getAssociateAvailability()

	public static void getAssociateTimeInPosition(List<QualifiedCandidate> candidates) throws QualifiedPoolException {
		long startTime = 0;
		List<AvailabilityDetails> theList = null;
		List<String> applicants = new ArrayList<String>();

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug("Entering getApplicantAvailability() method");
		} // end if

		try {
			// Create applicant List<String> from candidates list, used in the
			// DAO call
			for (int i = 0; i < candidates.size(); i++) {
				QualifiedCandidate temp = (QualifiedCandidate) candidates.get(i);
				applicants.add(temp.getId().trim());
			}

			if (candidates.size() > 0) {
				// invoke the DAO method to get the availability of Applicants
				theList = AvailabilityDAO.getApplicantAvailability(applicants);

				// Loop through all candidates and add availability
				for (int i = 0; i < candidates.size(); i++) {
					QualifiedCandidate allCandidates = (QualifiedCandidate) candidates.get(i);
					if (theList.size() == 0) {
						break;
					} // end if (theList.size() == 0)
					for (int j = 0; j < theList.size(); j++) {
						AvailabilityDetails availabilityDetails = (AvailabilityDetails) theList.get(j);
						if (availabilityDetails.getApplicantId().trim().equals(allCandidates.getId().trim())) {

							StringBuilder availability = new StringBuilder();
							// Applicants Day Availability
							// Set Sunday
							if (availabilityDetails.getDaySeg().contains(7)) {
								availability.append("Sun, ");
							}
							if (availabilityDetails.getDaySeg().contains(1) || availabilityDetails.getDaySeg().contains(2) || availabilityDetails.getDaySeg().contains(3)
									|| availabilityDetails.getDaySeg().contains(4) || availabilityDetails.getDaySeg().contains(5)) {
								availability.append("Weekdays, ");
							}
							if (availabilityDetails.getDaySeg().contains(6)) {
								availability.append("Sat, ");
							}

							// Remove trailing comma
							if (availability.length() > 0) {
								availability.replace(availability.length() - 2, availability.length(), "");
							}
							// Add New Line Character
							availability.append("\n");

							// Applicants Start Time(s)
							if (availabilityDetails.getStartTimeSeg().contains(0)) {
								availability.append("Anytime, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(1)) {
								availability.append("Early Morning, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(2)) {
								availability.append("Morning, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(3)) {
								availability.append("Afternoon, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(4)) {
								availability.append("Evening, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(5)) {
								availability.append("Late Evening, ");
							}
							if (availabilityDetails.getStartTimeSeg().contains(6)) {
								availability.append("Overnight, ");
							}
							// Remove trailing comma
							if (availability.length() > 0) {
								availability.replace(availability.length() - 2, availability.length(), "");
							}
							// Set availability of the applicant on the
							// candidates
							// list passed in to method.
							allCandidates.setAvailability(availability.toString());
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Applicant:%1$s Availability:%2$s", allCandidates.getId().trim(), allCandidates.getAvailability()));
							}
							theList.remove(j);
							break;
						} else {
							allCandidates.setAvailability("");
						} // end else
					} // end for (int j=0;j<theList.size();j++)
				} // end for (int i=0;i<candidates.size();i++)
			} // End if (candidates.size() > 0)
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
			mLogger.warn(new QualifiedPoolMessage("An exception occurred querying for Applicant Availability Data"));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for Applicant Availability Data"), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting getApplicantAvailability() method, total time to process request: %1$.9f seconds", (((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
	} // end function getApplicantAvailability()
} // end class AvailabilityManager
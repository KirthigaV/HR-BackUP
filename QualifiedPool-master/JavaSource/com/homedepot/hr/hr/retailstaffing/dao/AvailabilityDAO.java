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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.bl.AvailabilityManager;
import com.homedepot.hr.hr.retailstaffing.dto.AvailabilityDetails;
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
public class AvailabilityDAO implements Constants {
	// logger instance
	private static final Logger mLogger = Logger.getLogger(AvailabilityDAO.class);
	// selector constants
	private static final String SELECTOR_READ_APPLICANT_AVAILABILITY_BY_INPUT_LIST = "readEmploymentApplicantPrimaryAndDailyAvailabilityByInputList";
	private static final String SELECTOR_READ_ASSOCIATE_AVAILABILITY_BY_INPUT_LIST = "readEmploymentDailyAvailabilityDetailsByInputList";
	// column constants
	private static final String COL_APPLICANT_ID = "employmentApplicantId";
	private static final String COL_WEEK_DAY_NBR = "weekDayNumber";
	private static final String COL_DAY_SEG_CD = "daySegmentCode";
	// inputs
	private static final String INPUT_ACTV_FLG = "activeFlag";
	private static final String INPUT_APPLICANT_ID_LIST = "employmentApplicantIdList";
	

	/**
	 * Get the Availability for the list of applicants passed
	 * 
	 * @param applicants
	 *            List of applicant id's
	 * 
	 * @return List of AvailabilityDetails transfer objects
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs querying the database for the
	 *             data
	 */
	public static List<AvailabilityDetails> getApplicantAvailability(final List<String> applicants) throws QueryException {
		// List that will hold transfer object AvailabilityDetails
		final List<AvailabilityDetails> theList = new ArrayList<AvailabilityDetails>();

		// create the MapStream used to pass the input values to the DAO
		// selector
		MapStream inputs = new MapStream(SELECTOR_READ_APPLICANT_AVAILABILITY_BY_INPUT_LIST);

		// add the list of applicants
		inputs.put(INPUT_APPLICANT_ID_LIST, applicants);
		// add the active flag input
		inputs.put(INPUT_ACTV_FLG, true);

		// invoke the query using the DAO
		BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION, inputs, new ResultsReader() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot
			 * .ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query,
			 * com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				// populate a AvailabilityDetails object and add to list

				String currentId = null;
				String previousId = null;
				int applicantCount = 0;
				int recordCount = 0;
				AvailabilityDetails availDetails = null;
				Set<Integer> daySeg = null;
				Set<Integer> startTimeSeg = null;

				while (results.next()) {
					currentId = results.getString(COL_APPLICANT_ID).trim();
					if (currentId.equals(previousId) || previousId == null) {
						// This section will only run for the first record in
						// the results, see the else for all other records. Both
						// will work the same. if it is not here the 1st
						// Applicant will be missed.
						if (previousId == null) {
							applicantCount++;
							if (availDetails == null) {
								availDetails = new AvailabilityDetails();
								daySeg = new HashSet<Integer>();
								startTimeSeg = new HashSet<Integer>();
							}
							availDetails.setApplicantId(results.getString(COL_APPLICANT_ID).trim());
							daySeg.add(results.getInt(COL_WEEK_DAY_NBR));
							availDetails.setDaySeg(daySeg);
							startTimeSeg.add(results.getInt(COL_DAY_SEG_CD));
							availDetails.setStartTimeSeg(startTimeSeg);
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Applicant:%1$s Weekday Code:%2$d Start Time Code:%3$d", currentId, results.getInt(COL_WEEK_DAY_NBR), results.getInt(COL_DAY_SEG_CD)));
							}
							recordCount++;
						} else {
							// This code will be the same as above for all
							// remaining records.
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Applicant:%1$s Weekday Code:%2$d Start Time Code:%3$d", currentId, results.getInt(COL_WEEK_DAY_NBR), results.getInt(COL_DAY_SEG_CD)));
							}
							if (availDetails == null) {
								availDetails = new AvailabilityDetails();
								daySeg = new HashSet<Integer>();
								startTimeSeg = new HashSet<Integer>();
							}
							availDetails.setApplicantId(results.getString(COL_APPLICANT_ID).trim());
							daySeg.add(results.getInt(COL_WEEK_DAY_NBR));
							availDetails.setDaySeg(daySeg);
							startTimeSeg.add(results.getInt(COL_DAY_SEG_CD));
							availDetails.setStartTimeSeg(startTimeSeg);
							recordCount++;
						}
					} else {
						// Applicant Id changed so add applicant details to list
						// and reset object
						recordCount++;
						applicantCount++;

						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Applicant:%1$s Added to List", availDetails.getApplicantId()));
						}
						// add current availDetails to list
						theList.add(availDetails);

						// Null current availDetails object
						availDetails = null;

						// Due to the first record of the next Applicant already
						// being read from the results it has to be added to a
						// new object or it will be lost
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Applicant:%1$s Weekday Code:%2$d Start Time Code:%3$d", currentId, results.getInt(COL_WEEK_DAY_NBR), results.getInt(COL_DAY_SEG_CD)));
						}
						if (availDetails == null) {
							availDetails = new AvailabilityDetails();
							daySeg = new HashSet<Integer>();
							startTimeSeg = new HashSet<Integer>();
						}
						availDetails.setApplicantId(results.getString(COL_APPLICANT_ID).trim());
						daySeg.add(results.getInt(COL_WEEK_DAY_NBR));
						availDetails.setDaySeg(daySeg);
						startTimeSeg.add(results.getInt(COL_DAY_SEG_CD));
						availDetails.setStartTimeSeg(startTimeSeg);
						// End *****************************

					} // end else - (currentId.equals(previousId) || previousId == null)
					// Set previousId to currentId
					previousId = results.getString(COL_APPLICANT_ID).trim();

				} // end while (results.next())

				// Add last Applicant availDetails object because the results
				// have moved
				// past the last record, therefore it did not get added in the
				// while loop.
				if (availDetails != null) {
					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Applicant Added to List: %1$s", availDetails.getApplicantId()));
					}
					theList.add(availDetails);
				}

				// Debug Statements
				if (mLogger.isDebugEnabled()) {
					mLogger.debug(String.format("Total Records Returned: %1$d", recordCount));
					mLogger.debug(String.format("Applicant Count: %1$d", applicantCount));
					mLogger.debug(String.format("Applicants added to List: %1$d", theList.size()));
				}
			} // end function readResults()
		}); // end ResultsReader implementation

		return theList;
	} // end function getApplicantAvailability()
	
	/**
	 * Get the Availability for the list of associates passed
	 * 
	 * @param associates
	 *            List of associates id's
	 * 
	 * @return List of AvailabilityDetails transfer objects
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs querying the database for the
	 *             data
	 */
	public static List<AvailabilityDetails> getAssociateAvailability(final List<String> associates) throws QueryException {
		// List that will hold transfer object AvailabilityDetails
		final List<AvailabilityDetails> theList = new ArrayList<AvailabilityDetails>();

		// create the MapStream used to pass the input values to the DAO
		// selector
		MapStream inputs = new MapStream(SELECTOR_READ_ASSOCIATE_AVAILABILITY_BY_INPUT_LIST);

		// add the list of associates
		inputs.put(INPUT_APPLICANT_ID_LIST, associates);

		// invoke the query using the DAO
		BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot
			 * .ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query,
			 * com.homedepot.ta.aa.dao.Inputs)
			 */
			public void readResults(Results results, Query query, Inputs inputs) throws QueryException {
				// populate a AvailabilityDetails object and add to list

				String currentId = null;
				String previousId = null;
				int applicantCount = 0;
				int recordCount = 0;
				AvailabilityDetails availDetails = null;
				Set<Integer> daySeg = null;
				Set<Integer> startTimeSeg = null;

				while (results.next()) {
					currentId = results.getString(COL_APPLICANT_ID).trim();
					if (currentId.equals(previousId) || previousId == null) {
						// This section will only run for the first record in
						// the results, see the else for all other records. Both
						// will work the same. if it is not here the 1st
						// Applicant will be missed.
						if (previousId == null) {
							applicantCount++;
							if (availDetails == null) {
								availDetails = new AvailabilityDetails();
								daySeg = new HashSet<Integer>();
								startTimeSeg = new HashSet<Integer>();
							}
							availDetails.setApplicantId(results.getString(COL_APPLICANT_ID).trim());
							daySeg.add(results.getInt(COL_WEEK_DAY_NBR));
							availDetails.setDaySeg(daySeg);
							startTimeSeg.add(results.getInt(COL_DAY_SEG_CD));
							availDetails.setStartTimeSeg(startTimeSeg);
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Associate:%1$s Weekday Code:%2$d Start Time Code:%3$d", currentId, results.getInt(COL_WEEK_DAY_NBR), results.getInt(COL_DAY_SEG_CD)));
							}
							recordCount++;
						} else {
							// This code will be the same as above for all
							// remaining records.
							if (mLogger.isDebugEnabled()) {
								mLogger.debug(String.format("Associate:%1$s Weekday Code:%2$d Start Time Code:%3$d", currentId, results.getInt(COL_WEEK_DAY_NBR), results.getInt(COL_DAY_SEG_CD)));
							}
							if (availDetails == null) {
								availDetails = new AvailabilityDetails();
								daySeg = new HashSet<Integer>();
								startTimeSeg = new HashSet<Integer>();
							}
							availDetails.setApplicantId(results.getString(COL_APPLICANT_ID).trim());
							daySeg.add(results.getInt(COL_WEEK_DAY_NBR));
							availDetails.setDaySeg(daySeg);
							startTimeSeg.add(results.getInt(COL_DAY_SEG_CD));
							availDetails.setStartTimeSeg(startTimeSeg);
							recordCount++;
						}
					} else {
						// Applicant Id changed so add applicant details to list
						// and reset object
						recordCount++;
						applicantCount++;

						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Associate:%1$s Added to List", availDetails.getApplicantId()));
						}
						// add current availDetails to list
						theList.add(availDetails);

						// Null current availDetails object
						availDetails = null;

						// Due to the first record of the next Associate already
						// being read from the results it has to be added to a
						// new object or it will be lost
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Associate:%1$s Weekday Code:%2$d Start Time Code:%3$d", currentId, results.getInt(COL_WEEK_DAY_NBR), results.getInt(COL_DAY_SEG_CD)));
						}
						if (availDetails == null) {
							availDetails = new AvailabilityDetails();
							daySeg = new HashSet<Integer>();
							startTimeSeg = new HashSet<Integer>();
						}
						availDetails.setApplicantId(results.getString(COL_APPLICANT_ID).trim());
						daySeg.add(results.getInt(COL_WEEK_DAY_NBR));
						availDetails.setDaySeg(daySeg);
						startTimeSeg.add(results.getInt(COL_DAY_SEG_CD));
						availDetails.setStartTimeSeg(startTimeSeg);
						// End *****************************

					} // end else - (currentId.equals(previousId) || previousId == null)
					// Set previousId to currentId
					previousId = results.getString(COL_APPLICANT_ID).trim();

				} // end while (results.next())

				// Add last Associate availDetails object because the results
				// have moved
				// past the last record, therefore it did not get added in the
				// while loop.
				if (availDetails != null) {
					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Associate Added to List: %1$s", availDetails.getApplicantId()));
					}
					theList.add(availDetails);
				}

				// Debug Statements
				if (mLogger.isDebugEnabled()) {
					mLogger.debug(String.format("Total Records Returned: %1$d", recordCount));
					mLogger.debug(String.format("Associate Count: %1$d", applicantCount));
					mLogger.debug(String.format("Associates added to List: %1$d", theList.size()));
				}
			} // end function readResults()
		}); // end ResultsReader implementation

		return theList;
	} // end function getAssociateAvailability()	
} // end class AvailabilityDAO
package com.homedepot.hr.hr.retailstaffing.bl;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: QualifiedPoolManager.java
 * Application: RetailStaffing
 */

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.QualifiedPoolDAO;
import com.homedepot.hr.hr.retailstaffing.dto.JobCategoryDetails;
import com.homedepot.hr.hr.retailstaffing.dto.OrgParamTO;
import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.homedepot.hr.hr.retailstaffing.dto.Requisition;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetails;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.ta.aa.dao.ConnectionHandler;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to generating a qualified
 * pool of candidates for an employment requisition
 */
public class QualifiedPoolManager implements Constants {
	/*
	 * List of candidate availability indicators that will be used to limit the
	 * candidates that are being returned
	 */
	private static final List<String> CANDIDATE_AVAIL_INDICATORS = new ArrayList<String>() {
		private static final long serialVersionUID = -3902825879039091377L;

		{
			add("R"); // RESERVED
			add("Y"); // AVAILABLE
		} // end constructor block
	}; // end collection CANDIDATE_AVAIL_INDICATORS

	// full-time employment status
	private static final String EMP_CAT_FULLTIME = "FT";
	// part-time employment status
	private static final String EMP_CAT_PARTTIME = "PT";
	// full-time salaried employment status
	private static final String EMP_CAT_FULLTIME_SALARIED = "FS";
	// temporary employment status
	private static final String EMP_CAT_TEMP = "TP";

	// collection of employment statuses (excluding temporary)
	private static final List<String> EMP_CATS_WO_TEMP = new ArrayList<String>() {
		private static final long serialVersionUID = 844893593633130532L;

		{
			add(EMP_CAT_FULLTIME);
			add(EMP_CAT_PARTTIME);
			add(EMP_CAT_FULLTIME_SALARIED);
		} // end constructor block
	}; // end collection EMP_CATS_WO_TEMP

	// collection of employment statuses (Part time only)
	private static final List<String> EMP_CAT_PT_ONLY = new ArrayList<String>() {
		private static final long serialVersionUID = 7235572202119323366L;

		{
			add(EMP_CAT_PARTTIME);
		} // end constructor block
	}; // end collection EMP_CAT_PT_ONLY
	
	// range start (inclusive) for the number of days post starting a position
	// that a temporary associate can be considered for another position
	private static final int TEMP_BEGIN_OFFSET_DAYS = 30;
	// range end (inclusive) for the number of days post starting a position
	// that a temporary associate can be considered for another position
	private static final int TEMP_END_OFFSET_DAYS = 120;
	// number of days before a former temporary associate can be included in the
	// qualified applicant pool
	private static final int TEMP_APPL_THRESHOLD_DAYS = -120;
	// maximum number of candidates to be returned by the methods.
	private static final int MAX_CANDIDATES = 300;
	// basic background check package id
	private static final int BASIC_BGCHK_PKG_ID = 1;
	// store type prefix that indicates a store does not perform testing
	private static final String STORE_TYPE_PREFIX_DO_NOT_TEST = "DTE";
	// list of position category codes, used to get the position preference code
	private static final List<Integer> POSN_CTGRY_CDS = new ArrayList<Integer>() {
		private static final long serialVersionUID = 7552685592837899405L;

		{
			add(1);
			add(2);
			add(3);
			add(101);
			add(4);
			add(8);
			add(9);
			add(900);
			add(901);
		} // end constructor block
	}; // end collection POSN_CTGRY_CDS
	// list of drug test failure codes
	private static final List<String> DRUG_TEST_FAILURE_CODES = new ArrayList<String>() {
		private static final long serialVersionUID = 6451419102977748562L;

		{
			add("00002");
			add("00003");
			add("00005");
			add("00006");
			add("00007");
		} // end constructor block
	}; // end collection DRUG_TEST_FAILURE_CODES
	// logger instance
	private static final Logger mLogger = Logger.getLogger(QualifiedPoolManager.class);
	// LCP environment value
	private static final String lcpEnvironment = com.homedepot.ta.aa.util.TAAAResourceManager.getProperty("host.LCP");

	/**
	 * This function generates a list of qualified associates for an employment
	 * requisition
	 * 
	 * @param requisitionNbr
	 *            The requisition number that the pool is being generated for
	 * @param jobSkillFilters
	 *            List of filters that should be applied when generating the
	 *            pool. This is an optional parameter and will only be applied
	 *            if not null or empty
	 * 
	 * @return List of associates qualified for the position (and matching the
	 *         filters supplied)
	 * 
	 * @throws QualifiedPoolException
	 *             Thrown if any of the following
	 */
	public static List<QualifiedCandidate> getQualifiedAssociatesForRequisition(final int requisitionNbr, final List<Short> jobSkillFilters) throws QualifiedPoolException {
		final List<QualifiedCandidate> qualCandidates = new ArrayList<QualifiedCandidate>();

		if (mLogger.isDebugEnabled()) {
			mLogger.debug(String.format("Entering getQualifiedAssociatesForRequisition(), requisitionNbr: %1$d", requisitionNbr));
		} // end if

		ConnectionHandler connHandler = null;
		
		try {
			// first validate the requisition number provided in the request is
			// valid
			if (requisitionNbr < 1) {
				throw new QualifiedPoolException(ERRCD_INVALID_REQNBR, String.format("Invalid requisition number %1$d provided", requisitionNbr));
			} // end if(requisitionNbr < 1)

			// if there were job skill filters provided
			if (jobSkillFilters != null && jobSkillFilters.size() > 0) {
				// iterate the job skill filters
				for (short jobSkillFilter : jobSkillFilters) {
					// validate the job skill filter is > 0
					if (jobSkillFilter < 1) {
						throw new QualifiedPoolException(ERRCD_INVALID_JOBSKILL, String.format("Invalid job skill filter %1$d provided", jobSkillFilter));
					} // end if(jobSkillFilter < 1)
				} // end for(short jobSkillFilter : jobSkillFilters)
			} // end if(jobSkillFilters != null && jobSkillFilters.size() > 0)

			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);
			
			/*
			 * Create a new non-transacted UniversalConnectionHandler instance
			 * that will be used to group all of the queries together so they
			 * are executed using the same database connection
			 */
			connHandler = new UniversalConnectionHandler(true, null, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)			 {
				{
					// set the debugging level of the connection handler based
					// on level of logging currently enabled in the application
					setDebugging(mLogger.isDebugEnabled());
				} // end constructor block

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#
				 * handleQuery(java.util.Map,
				 * com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler queryHandler) throws QueryException {
					if (mLogger.isDebugEnabled()) {
						mLogger.debug("Entering getQualifiedAssociatesForRequisition handleQuery() method");
						mLogger.debug(String.format("Getting requisition %1$d", requisitionNbr));
					} // end if

					// invoke the method to get the requisition
					// no need to check for null here, if the requisition isn't
					// found then it throws a QualifiedPoolException
					Requisition requisition = RequisitionManager.getRequisition(requisitionNbr);

					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Getting store details for requisition %1$d, store number %2$s", requisitionNbr, requisition.getStoreNbr()));
					} // end if
					// next get details about the requisitions store
					// no need to check for null here, if the store isn't found
					// then it throws a QualifiedPoolException
					StoreDetails store = StoreManager.getStoreDetails(requisition.getStoreNbr());

					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Getting background check package information for requisition %1$d", requisitionNbr));
					} // end if
					// next lookup the background check package for the
					// requisition
					// no need to validate here, if the background check package
					// id is not found then it throws a QualifiedPoolException
					int bgCheckPkgId = BackgroundCheckManager.getBGCheckPkgForJob(store.getStoreTypCd(), store.getCntryCd(), requisition.getDeptNbr(), requisition.getJobTtlCd());

					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Getting job category details for requisition %1$d", requisitionNbr));
					} // end if
					// next lookup the job category information for the
					// requisition
					// no need to validate here, if the job category information
					// is not found then it throws a QualifiedPoolException
					JobCategoryDetails jobCategory = JobCategoryManager.getJobCategoryDetails(requisition.getStoreNbr(), requisition.getDeptNbr(), requisition.getJobTtlCd(), -1);

					// next lookup HR STR GRP Codes
					List<String> hrStrGrpCds = StoreManager.getHrStoreGroupCodes(requisition.getStoreNbr(), store.getLocTypCd(), store.getStoreTypCd(), store.getCntryCd());
					
					// now use the data returned by prior queries to get the
					// qualified associates
					if (requisition.getFtReqdFlg()) {
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Requisition %1$d has full-time requirements, getting all qualified associates", requisitionNbr));
						} // end if

						/*
						 * Execute the query to get all qualified associates for
						 * the employment requisition data retrieved above. Add
						 * all of the candidates returned to the return list.
						 * There is no need to check for a null collection of
						 * candidates because the DAO method will always return
						 * an empty list if no records are found.
						 */
						qualCandidates.addAll(QualifiedPoolDAO.getActiveQualifiedAssociatesDAO20(requisition.getStoreNbr(), requisition.getDeptNbr(), requisition.getJobTtlCd(), CANDIDATE_AVAIL_INDICATORS,
								jobCategory.getConsentDecreeJobCtgryCd(), EMP_CATS_WO_TEMP,	EMP_CAT_TEMP, TEMP_BEGIN_OFFSET_DAYS, TEMP_END_OFFSET_DAYS, jobSkillFilters, hrStrGrpCds, true));
						/*qualCandidates.addAll(QualifiedPoolDAO.getActiveQualifiedAssociatesDAO20(requisition.getStoreNbr(), requisition.getDeptNbr(), requisition.getJobTtlCd(), CANDIDATE_AVAIL_INDICATORS,
								jobCategory.getConsentDecreeJobCtgryCd(), jobCategory.getConsentDecreeJobCtgryDesc(), store.getLocTypCd(), store.getStoreTypCd(), store.getCntryCd(), EMP_CATS_WO_TEMP,
								EMP_CAT_TEMP, TEMP_BEGIN_OFFSET_DAYS, TEMP_END_OFFSET_DAYS, jobCategory.getJobCtrgyCds(), jobSkillFilters, (bgCheckPkgId != BASIC_BGCHK_PKG_ID), bgCheckPkgId,dd));*/
												
					} // end if(requisition.getFtReqdFlg())
					else {
						if (mLogger.isDebugEnabled()) {
							mLogger.debug(String.format("Requisition %1$d does not have full-time requirements, getting only part-time qualified associates", requisitionNbr));
						} // end if

						/*
						 * Execute the query to get all qualified associates
						 * (part-time only) for the employment requisition data
						 * retrieved above. Add all of the candidates returned
						 * to the return list. There is no need to check for a
						 * null collection of candidates because the DAO method
						 * will always return an empty list if no records are
						 * found.
						 */
						qualCandidates.addAll(QualifiedPoolDAO.getActiveQualifiedAssociatesDAO20(requisition.getStoreNbr(), requisition.getDeptNbr(), requisition.getJobTtlCd(), CANDIDATE_AVAIL_INDICATORS,
								jobCategory.getConsentDecreeJobCtgryCd(), EMP_CAT_PT_ONLY,	"", 0, 0, jobSkillFilters, hrStrGrpCds,false));
						/*qualCandidates.addAll(QualifiedPoolDAO.getActiveQualifiedAssociates(requisition.getStoreNbr(), requisition.getDeptNbr(), requisition.getJobTtlCd(), CANDIDATE_AVAIL_INDICATORS,
								jobCategory.getConsentDecreeJobCtgryCd(), jobCategory.getConsentDecreeJobCtgryDesc(), store.getLocTypCd(), store.getStoreTypCd(), store.getCntryCd(), EMP_CAT_PARTTIME,
								jobCategory.getJobCtrgyCds(), jobSkillFilters, (bgCheckPkgId != BASIC_BGCHK_PKG_ID), bgCheckPkgId));*/						
					} // end else
				} // end function handleQuery()
			}; // end UniversalConnectionHandler

			// execute all the queries to build the pool of qualified associates
			connHandler.execute();
		} // end try
		catch (QueryException qe) {
			if (qe instanceof QualifiedPoolException) {
				throw (QualifiedPoolException) qe;
			} // end if(qe instanceof QualifiedPoolException)
			else {
				// throw a wrapped exception (it's logged in the service that's
				// calling it)
				throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
			} // end else
		} // end catch

		/*
		 * If there are > MAX_CANDIDATES records in the list, remove the rest.
		 * Have to physically remove them instead of doing a sublist because the
		 * XStream will still serialize all of the objects if we do a sublist.
		 */
		if (qualCandidates.size() >= MAX_CANDIDATES) {
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("%1$d qualified candidates returned, removing down to %2$d", qualCandidates.size(), MAX_CANDIDATES));
			} // end if
			// get a list iterator starting at MAX_CANDIDATES in the list
			ListIterator<QualifiedCandidate> iterator = qualCandidates.listIterator(MAX_CANDIDATES);
			// iterate while there are more records and remove the "extras"
			while (iterator.hasNext()) {
				// point the iterator to the next record
				iterator.next();
				// remove the current record
				iterator.remove();
			} // end while(iterator.hasNext())
		} // end if(qualCandidates.size() >= MAX_CANDIDATES)

		if (mLogger.isDebugEnabled()) {
			mLogger.debug(String.format("Exiting getQualifiedAssociatesForRequisition(), requisitionNbr: %1$d", requisitionNbr));
		} // end if

		return qualCandidates;
	} // end function getQualifiedAssociatesForRequisition()

	/**
	 * This function generates a list of qualified applicants for an employment
	 * requisition
	 * 
	 * @param requisitionNbr
	 *            The requisition number that the pool is being generated for
	 * @param jobSkillFilters
	 *            List of filters that should be applied when generating the
	 *            pool. This is an optional parameter and will only be applied
	 *            if not null or empty
	 * 
	 * @return List of applicants qualified for the position (and matching the
	 *         filters supplied)
	 * 
	 * @throws QualifiedPoolException
	 *             Thrown if any of the following
	 */
	public static List<QualifiedCandidate> getQualifiedApplicantsForRequisition(final int requisitionNbr, final List<Short> jobSkillFilters, final int tieringFlg) throws QualifiedPoolException {
		final List<QualifiedCandidate> qualCandidates = new ArrayList<QualifiedCandidate>();

		if (mLogger.isDebugEnabled()) {
			mLogger.debug(String.format("Entering getQualifiedApplicantsForRequisition(), requisitionNbr: %1$d", requisitionNbr));
		} // end if

		try {
			// first validate the requisition number provided in the request is
			// valid
			if (requisitionNbr < 1) {
				throw new QualifiedPoolException(ERRCD_INVALID_REQNBR, String.format("Invalid requisition number %1$d provided", requisitionNbr));
			} // end if(requisitionNbr < 1)

			// if there were job skill filters provided
			if (jobSkillFilters != null && jobSkillFilters.size() > 0) {
				// iterate the job skill filters
				for (short jobSkillFilter : jobSkillFilters) {
					// validate the job skill filter is > 0
					if (jobSkillFilter < 1) {
						throw new QualifiedPoolException(ERRCD_INVALID_JOBSKILL, String.format("Invalid job skill filter %1$d provided", jobSkillFilter));
					} // end if(jobSkillFilter < 1)
				} // end for(short jobSkillFilter : jobSkillFilters)
			} // end if(jobSkillFilters != null && jobSkillFilters.size() > 0)

			// get an instance of the query manager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get a DAO connection
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);

			/*
			 * Create a new non-transacted UniversalConnectionHandler instance
			 * that will be used to group all of the queries together so they
			 * are executed using the same database connection
			 */
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, null, connection) {
				{
					// set the debugging level of the connection handler based
					// on level of logging currently enabled in the application
					setDebugging(mLogger.isDebugEnabled());
				} // end constructor block

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#
				 * handleQuery(java.util.Map,
				 * com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler queryHandler) throws QueryException {
					if (mLogger.isDebugEnabled()) {
						mLogger.debug("Entering getQualifiedApplicantsForRequisition handleQuery() method");
						mLogger.debug(String.format("Getting requisition %1$d", requisitionNbr));
					} // end if

					// invoke the method to get the requisition
					// no need to check for null here, if the requisition isn't
					// found then it throws a QualifiedPoolException
					Requisition requisition = RequisitionManager.getRequisition(requisitionNbr);

					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Getting store details for requisition %1$d, store number %2$s", requisitionNbr, requisition.getStoreNbr()));
					} // end if
					// next get details about the requisitions store
					// no need to check for null here, if the store isn't found
					// then it throws a QualifiedPoolException
					StoreDetails store = StoreManager.getStoreDetails(requisition.getStoreNbr());

					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Getting background check package information for requisition %1$d", requisitionNbr));
					} // end if
					// next lookup the background check package for the
					// requisition
					// no need to validate here, if the background check package
					// id is not found then it throws a QualifiedPoolException
					int bgCheckPkgId = BackgroundCheckManager.getBGCheckPkgForJob(store.getStoreTypCd(), store.getCntryCd(), requisition.getDeptNbr(), requisition.getJobTtlCd());

					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Getting job category details for requisition %1$d", requisitionNbr));
					} // end if
					// next lookup the job category information for the
					// requisition
					// no need to validate here, if the job category information
					// is not found then it throws a QualifiedPoolException
					JobCategoryDetails jobCategory = JobCategoryManager.getJobCategoryDetails(requisition.getStoreNbr(), requisition.getDeptNbr(), requisition.getJobTtlCd(), tieringFlg);

					// get the current date
					Calendar current = Calendar.getInstance();
					// clone current date and subtract a year
					Calendar currentMinusAYear = (Calendar) current.clone();
					currentMinusAYear.add(Calendar.YEAR, -1);
					// clone current date and subtract the temp threshold days
					Calendar currMinusTempThreshold = (Calendar) current.clone();
					currMinusTempThreshold.add(Calendar.DATE, TEMP_APPL_THRESHOLD_DAYS);

					if (mLogger.isDebugEnabled()) {
						mLogger.debug(String.format("Getting qualified applicants for requisition %1$d, full-time required: %2$b, part-time required: %3$b", requisition.getNumber(), requisition
								.getFtReqdFlg(), requisition.getPtReqdFlg()));
					} // end if
					/*
					 * Execute the query to get all qualified applicant for the
					 * employment requisition data retrieved above. Add all of
					 * the candidates returned to the return list. There is no
					 * need to check for a null collection of candidates because
					 * the DAO method will always return an empty list if no
					 * records are found.
					 */
					qualCandidates.addAll(QualifiedPoolDAO.getActiveQualifiedApplicantsDAO20(requisition.getStoreNbr(), new Date(current.getTimeInMillis()), CANDIDATE_AVAIL_INDICATORS,  
							DRUG_TEST_FAILURE_CODES, new Timestamp(currentMinusAYear.getTimeInMillis()), jobSkillFilters, bgCheckPkgId, 
							jobCategory.getTieringCtgryCd()));
				} // end function handleQuery()
			}; // end UniversalConnectionHandler

			// execute all the queries to build the pool of qualified associates
			connectionHandler.execute();
		} // end try
		catch (QueryException qe) {
			if (qe instanceof QualifiedPoolException) {
				throw (QualifiedPoolException) qe;
			} // end if(qe instanceof QualifiedPoolException)
			else {
				// throw a wrapped exception (it's logged in the service that's
				// calling it)
				throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
			} // end else
		} // end catch

		/*
		 * If there are > MAX_CANDIDATES records in the list, remove the rest.
		 * Have to physically remove them instead of doing a sublist because the
		 * XStream will still serialize all of the objects if we do a sublist.
		 */
		if (qualCandidates.size() >= MAX_CANDIDATES) {
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("%1$d qualified candidates returned, removing down to %2$d", qualCandidates.size(), MAX_CANDIDATES));
			} // end if
			// get a list iterator starting at MAX_CANDIDATES in the list
			ListIterator<QualifiedCandidate> iterator = qualCandidates.listIterator(MAX_CANDIDATES);
			// iterate while there are more records and remove the "extras"
			while (iterator.hasNext()) {
				// point the iterator to the next record
				iterator.next();
				// remove the current record
				iterator.remove();
			} // end while(iterator.hasNext())
		} // end if(qualCandidates.size() >= MAX_CANDIDATES)

		if (mLogger.isDebugEnabled()) {
			mLogger.debug(String.format("Exiting getQualifiedAssociatesForRequisition(), requisitionNbr: %1$d", requisitionNbr));
		} // end if

		return qualCandidates;
	} // end function getQualifiedAssociatesForRequisition()

	// Looks at HR_ORG_PARM table, returns the value for Tiering
	public static int checkTieringFlg() throws QualifiedPoolException {
		OrgParamTO parameter;

		try {
			if (lcpEnvironment.equalsIgnoreCase("AD")) {
				//For Local Development Only
				parameter = OrgParamManager.getOrgParam("HR", "1", "AD.ats.qp.tiering.flg", "B", true);
			} else {
				//For all other environments except AD
				parameter = OrgParamManager.getOrgParam("HR", "1", "ats.qp.tiering.flg", "B", true);
			}

			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("LCP Environment Value:%1$s", lcpEnvironment));
				mLogger.debug(String.format("Tiering Value:%1$s", parameter.getIntVal()));
			}
		} catch (Exception e) {
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, e);
		}
		return parameter.getIntVal();
	}// end - checkTieringFlg()	
} // end class QualifiedPoolManager
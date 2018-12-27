package com.homedepot.hr.hr.retailstaffing.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.PhoneScreenDAO;
import com.homedepot.hr.hr.retailstaffing.dao.handlers.PhoneScreenHandler;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.MinimumResponseTO;
import com.homedepot.hr.hr.retailstaffing.dto.OrgParamTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenMinReqTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenNoteTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenReqSpecMinReqTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchdIntrvwChecksTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.StatusCacheKey;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.HrRetlStffReconsialtionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.InsertPhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.POMRsaStatusCrossRefRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdatePhoneScreenReqnSpecKnockOutRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.POMRsaStatusCrossRefResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenReqnSpecKODetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdatePhoneScreenReqnSpecKnockOutResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.StatusType;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.interfaces.ValidationConstants;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingRequisitionManager.RequisitionServiceHandler;
import com.homedepot.hr.hr.retailstaffing.util.ClientApplLogger;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.dao.ConnectionHandler;
import com.homedepot.ta.aa.dao.Contract;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicContract;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.basic.TimeLimitedMap;
import com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/*
 * This program is proprietary to The Home Depot and is not to be reproduced, used, or disclosed without permission of:
 * The Home Depot 2455 Paces Ferry Road, N.W. Atlanta, GA 30339-4053 File Name: PhoneScreenManager.java Application:
 * PhoneScreenManager
 */

public class PhoneScreenManager implements Constants, RetailStaffingConstants, DAOConstants, ValidationConstants
{
	private static final String SEL_READ_HR_PHN_SCRN = "readHumanResourcesPhoneScreen";
	private static final String SEL_READ_APLCNT_BY_PHN_SCRN_ID = "readApplicantByPhoneScreenId";
	private static final String SEL_READ_ASSOC_BY_PHN_SCRN_ID = "readAssociateDetailsByPhoneScreenId";
	//Moved to a new Selector for CDP Project.
	//private static final String SEL_READ_APLCNT_BY_PRTL_APLCNT_ID_ZIP = "readApplicantByZipCodeAndPartialEmploymentApplicantId";
	private static final String SEL_READ_APLCNT_BY_PRTL_APLCNT_ID_ZIP = "readEmployementApplicantPrimaryAndApplicantAddressPhoneDetails";
	private static final String SEL_READ_REQ_PHN_SCRN = "readRequisitionPhoneScreen";
	private static final String SEL_READ_HR_PHN_SCRN_INTRW_DTLS = "readHumanResourcesPhoneScreenInterviewDetails";
	private static final String SEL_READ_EMPLT_MOD_QST_BY_EMPLT_ID = "readEmploymentModuleQuestionByEmploymentId";
	private static final String SEL_READ_EMPLT_POSN_CAND_BY_CAND_ID = "readEmploymentPositionCandidateByEmploymentPositionCandateId";

	//Added for FMS 7894 IVR Change
	private static final String SEL_READ_HR_REQN_DAILY_AVAIL = "readHumanResourcesRequisitionDailyAvailableByInputList";
	private static final String SEL_READ_REQN_DETAILS = "readRequisitionDetails";
	private static final String SEL_READ_REQN_STAFFING_DETAILS = "readRequisitionStaffingDetails";
	private static final String SEL_READ_BGC_JOB_REQUIREMENTS = "readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode";	
	private static final String SEL_READ_HR_LOC_DMV_EXEMPT = "readHumanResourcesLocationDmvExemptionByInputList";
	private static final String SEL_READ_THD_STR_EMPLT_REQN = "readThdStoreEmploymentRequisition";
	private static final String SEL_READ_REMAINING_INTRW_CAND_COUNT_BY_STAT_CD = "readRemainingInterviewCandidateCountByPhoneScreenStatusCode";
	private static final String SEL_READ_HR_STR_EMPLT_REQN_COUNT = "readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList";
	private static final String SEL_READ_TRPRX = "readTRPRX000ByInputList";
	private static final String SEL_READ_COUNT_HR_REQN_SCHD_DTLS = "readCountHumanResourcesRequisitionScheduleDetails";
	
	private static final String HR_PHN_SCRN_ID = "humanResourcesPhoneScreenId";
	private static final String STR_NBR_TABNO = "storeNumberTabno";
	private static final String DEPT_NBR_TABNO = "departmentNumberTabno";
	private static final String LANG_CD = "languageCode";
	private static final String CURRENT_DATE = "currentDate";
	private static final String RECORD_STATUS = "recordStatus";
	private static final String ADDR_TYP = "addressType";
	private static final String STAT_CD = "statusCode";
	private static final String EMPLT_APLCNT_ID = "employmentApplicantId";
	private static final String ZIP_CODE = "zipCode";
	private static final String EMPLT_POSN_CAND_ID = "employmentPositionCandidateId";
	private static final String ACTV_FLG_LIST = "activeFlagList";
	private static final String TABNO = "tabno";
	private static final String EMPLT_TST_ID = "employmentTestId";

	//Added for FMS 7894 IVR Change
	private static final String EMPLT_REQN_NBR = "employmentRequisitionNumber";
	private static final String HR_SYS_STR_NBR = "humanResourcesSystemStoreNumber";
	private static final String BASE_STORE_GROUP_FLG = "baseStoreGroupFlag";
	private static final String JOB_TTL_CD = "jobTitleCode";
	private static final String HR_SYS_DEPT_NBR = "humanResourcesSystemDepartmentNumber";
	private static final String EFF_DATE = "effectiveDate";
	private static final String EFF_BEGIN_DT_LESS_THAN_EQUAL_TO = "effectiveBeginDateLessThanEqualTo";
	private static final String EFF_END_DT_GREATER_THAN = "effectiveEndDateGreaterThan";
	private static final String INTERVIEW_STAT_CD_LIST = "interviewStatusCodeList";
	private static final String ACTV_FLG = "activeFlag";
	private static final String CAND_OFFER_MADE_FLG = "candidateOfferMadeFlag";
	private static final String CAND_OFFER_STAT_IND = "candidateOfferStatusIndicator";
	private static final String REQN_CAL_ID = "requisitionCalendarId";
	private static final String BEGIN_TIMESTAMP = "beginTimestamp";
	private static final String HR_BEGIN_TIMESTAMP = "humanResourceBeginTimestamp";
	private static final String REQN_SCHD_STAT_CD = "requisitionScheduleStatusCode";
	private static final String BEGIN_TS_GREATER_THAN_EQUAL_TO = "beginTimestampGreaterThanEqualTo";
	private static final String HR_BEGIN_TS_LESS_THAN_EQUAL_TO = "humanResourceBeginTimestampLessThanEqualTo";
	private static final String TABLE_KEYS = "tableKeys";
	private static final String START_END_DT = "startEndDate";
	
	private static final String TABNO_STR_NBR = "00049";
	private static final String TABNO_DEPT_NBR = "10069";

	private static final String TERMINATED = "T";
	private static final String ACTIVE = "A";
	
	private static final String YES = "Y";
	private static final String NO = "N";
	private static final String NA = "A";
	
	private static final String IVR_USER = "IVR";
	private static final String DETAILS_TXT = "Do not Proceed";

	private static final BigDecimal ADDR_TYP_PRIM = new BigDecimal(1);
	private static final List<String> ACTIVE_FLAGS = new ArrayList<String>()
	{
		private static final long serialVersionUID = -8422415652142495421L;

		{
			add(YES);
		}
	};

	private static final String EN_US = "EN_US";
	
	private static final String PREV_QUEST_SUBSYS_CD = "HR";
	private static final String PREV_QUEST_BU = "1";
	private static final String PREV_QUEST_PARAM_ID = "phnscrn.prev.qsts";
	private static final String PREV_QUEST_PRCSS_TYP_IND = "O";
	private static final String PREV_QUEST_PARM_DELIMITER = ",";
	
	// list of HR_REQN_SCH.REQN_SCH_STAT_CDs that indicate a slot is NOT scheduled
	private static final List<Short> NON_SCHEDULED_STATUSES = new ArrayList<Short>()
	{
        private static final long serialVersionUID = 5636926602384586166L;

		{
			add(AVALIABLE_STATUS_CODE);
			add(RESERVED_STATUS_CODE);
			add(CANCELLED_STATUS_CODE);
		}
	};

	// list of HR_REQN_SCH.REQN_SCH_STAT_CDs that indicate a slot is scheduled
	private static final List<Short> SCHEDULED_STATUSES = new ArrayList<Short>()
	{
        private static final long serialVersionUID = 5150485323904095553L;

		{
			add(SCHEDULED_STATUS_CODE);
		}
	};

	/** timed map used to cache stores that are exempt from needing a DL for a specified duration (4 hours by default) */
	private static Map<String, String> mStoreDMVExemptCache = new TimeLimitedMap<String, String>(
			Collections.synchronizedMap(new HashMap<String, String>()), 14400000);
	
	private static final Logger mLogger = Logger.getLogger(PhoneScreenManager.class);

	/**
	 * The method will be used for updating the Phone Screen Status details in
	 * the DB.
	 * 
	 * @param phoneScrnId
	 *            - The unique id for the candidate.
	 * @param phoneScrnStatus
	 *            - The Phone Screen Status code.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updateHumanResourcesPhoneScreenStatus(final int phoneScrnId, final short phoneScrnStatus) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateHumanResourcesPhoneScreenStatus( )" + "phoneScrnId: %1$s, phoneScrnStatus: %2$s",
			    phoneScrnId, phoneScrnStatus));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			// get an instance of the DAO query manager
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			//get a database connection from the DAO query manager
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);			
			
			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, phnScrnHandler, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();			
			
					//Update the Status History table
					//Get the Max Sequence Number
					MapStream inputs = new MapStream(SEL_READ_MAX_CAND_CONTACT_STATUS);
					inputs.put(HR_PHN_SCRN_ID, phoneScrnId);
					workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
					
					mLogger.debug(String.format("Next Seq Num:%1$d for Phone Screen:%2$d", phnScrnHandler.getMaxSeqNumPlusOne(), phoneScrnId));
					
					//Insert Phone Screen Status record
					inputs.clear();
					inputs.setSelectorName(SEL_CREATE_CAND_CONTACT_STATUS);
					inputs.put(HR_PHN_SCRN_ID, phoneScrnId);
					inputs.put(CAND_CONTACT_STATUS_TYPE_CODE, STATUS_TYPE_CODE_PHN_SCRN);
					inputs.put(SEQUENCE_NBR, phnScrnHandler.getMaxSeqNumPlusOne());
					inputs.put(PHN_SCRN_STAT_CD, phoneScrnStatus);
					inputs.put(INTERVIEW_STAT_CD, INTERVIEW_STAT_ZERO);
					
					workforceRecruitmentQuery.insertObject(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
					
					//Update the Phone Screen Status
					phnScrnHandler.resetStatus();
					inputs.clear();
					inputs.setSelectorName("updatePhoneScreenStatusByPhoneScreenStatCode");					
					inputs.put(HR_PHN_SCRN_ID, phoneScrnId);
					inputs.putAllowNull("phoneScreenStatusCode", phoneScrnStatus);
					workforceRecruitmentQuery.updateObject(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
				}
			};
			connectionHandler.execute();
		}
		catch (QueryException qe)
		{
			// log the error if it is not a No Rows Found Exception
			if (qe.getMessage().equals("No Rows Found")){
				mLogger.error("An exception occurred updating the phone screen status", qe);
			} else {
			mLogger.error("An exception occurred updating Screen Status to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe); }
		}
		catch (Exception e)
		{
			if (e.getMessage().equals("No Rows Found")){
				mLogger.error("An exception occurred updating the phone screen status", e);
			} else { mLogger.error("An exception occurred updating Screen Status to the PhoneScreenId", e);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);}
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateHumanResourcesPhoneScreenStatus( )");
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for updating the Interview Schedule Status
	 * details in the DB.
	 * 
	 * @param phoneScrnId
	 *            - The unique id for the candidate.
	 * @param interviewSchedStatus
	 *            - The interview Schedule Status code.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updateInterviewStatus(final int phoneScrnId, final short interviewSchedStatus) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateInterviewStatus( )" + "phoneScrnId: %1$s, phoneScrnStatus: %2$s", phoneScrnId,
			    interviewSchedStatus));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			// get an instance of the DAO query manager
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			//get a database connection from the DAO query manager
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);			
			
			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, phnScrnHandler, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();			
			
					PhoneScreenDAO.updateStatusHistoryTable(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, phoneScrnId, STATUS_TYPE_CODE_INTERVIEW_SCHD, interviewSchedStatus);
					PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, phoneScrnId, interviewSchedStatus);
				}
			};
			connectionHandler.execute();
		}
		catch (QueryException qe)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", e);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateInterviewStatus( )");
		}
		return phnScrnHandler.isSuccess();
	}

	
	public static boolean updateInterviewStatusHistory(final int phoneScrnId, final short interviewSchedStatus) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateInterviewStatusHistory( )" + "phoneScrnId: %1$s, phoneScrnStatus: %2$s", phoneScrnId,
			    interviewSchedStatus));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			final Contract contract = new BasicContract(WORKFORCERECRUITMENT_CONTRACT_NAME,WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION);
			QueryManager queryManager = QueryManager.getInstance(contract);
			DAOConnection conn = queryManager.getDAOConnection(contract);
			ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
			{
				public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
				{
					DAOConnection daoConn = daoConnList.get(contract);
					mLogger.info(this + "The connection obtained is" + daoConn);
					Query query = daoConn.getQuery();
					mLogger.info(this + "The query obtained is" + query);
					PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(daoConn, query, phnScrnHandler, phoneScrnId, interviewSchedStatus);
					//PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(daoConn, query, phnScrnHandler, phoneScrnId, interviewSchedStatus);
				}
			};
			connHandler.execute();
		}
		catch (QueryException qe)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", e);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateInterviewStatus( )");
		}
		return phnScrnHandler.isSuccess();
	}	
	
	
	/**
	 * The method will be used for updating the Interview material Status
	 * details in the DB.
	 * 
	 * @param phoneScrnId
	 *            - The unique id for the candidate.
	 * @param interviewSchedStatus
	 *            - The interview material Status code.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updatePhoneScreenStatusByInterviewMaterialStatCode(final int phoneScrnId, final short materialStatus) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updatePhoneScreenStatusByInterviewMaterialStatCode( )"
			    + "phoneScrnId: %1$s, materialStatus: %2$s", phoneScrnId, materialStatus));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
			QueryManager queryManager = QueryManager.getInstance(contract);
			DAOConnection conn = queryManager.getDAOConnection(contract);
			ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
			{
				public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
				{
					DAOConnection daoConn = daoConnList.get(contract);
					mLogger.info(this + "The connection obtained is" + daoConn);
					Query query = daoConn.getQuery();
					mLogger.info(this + "The query obtained is" + query);
					PhoneScreenDAO.updatePhoneScreenStatusByInterviewMaterialStatus(daoConn, query, phnScrnHandler, phoneScrnId, materialStatus);
				}
			};
			connHandler.execute();
		}
		catch (QueryException qe)
		{
			mLogger.error("An exception occurred updating material Status to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", e);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updatePhoneScreenStatusByInterviewMaterialStatCode( )");
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for updating the MinimumRequirement details for
	 * the Phone Screen Id in the DB.
	 * 
	 * @param phoneScrnId
	 *            - The unique id for the candidate.
	 * @param minResList
	 *            - The list of minimum Responses.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */
	public static boolean updateMinimumReqsForPhoneScreen(final int phoneScrnId, final List<MinimumResponseTO> minResList) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateMinimumReqsForPhoneScreen( )" + "phoneScrnId: %1$s", phoneScrnId));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();

		if(minResList != null && minResList.size() > 0)
		{
			try
			{
				final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
				QueryManager queryManager = QueryManager.getInstance(contract);
				DAOConnection conn = queryManager.getDAOConnection(contract);
				ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						DAOConnection daoConn = daoConnList.get(contract);
						mLogger.info(this + "The connection obtained is" + daoConn);
						Query query = daoConn.getQuery();
						mLogger.info(this + "The query obtained is" + query);
						updateMinimumRequirement(daoConn, query, phnScrnHandler, phoneScrnId, minResList);
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating min Requirement to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating min Requirement to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateMinimumReqsForPhoneScreen( )");
			}
		}
		return phnScrnHandler.isSuccess();

	}

	/**
	 * The method will be used for updating the MinimumRequirement details in
	 * the DB.
	 * 
	 * @param daoConn
	 *            - The DAO connection Object.
	 * @param query
	 *            -
	 * @param handler
	 *            - Query Handler
	 * @param phoneScrnId
	 *            - The Unique id for the candidate.
	 * @param resList
	 *            - The List of minimumResponses.
	 * @throws RetailStaffingException
	 */

	public static void updateMinimumRequirement(DAOConnection daoConn, Query query, PhoneScreenHandler handler, int phoneScrnId, List<MinimumResponseTO> resList)
	    throws QueryException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateMinimumRequirement( )" + "phoneScrnId: %1$s", phoneScrnId));
		}

		// final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			for(int j = 0; j < resList.size(); j++)
			{
				final StringBuffer stat = new StringBuffer();
				PhoneScreenDAO
				    .readPhoneScreenMinimumRequirementForExistence(daoConn, query, handler, phoneScrnId, Short.parseShort(resList.get(j).getSeqNbr()));
				stat.append(handler.isSuccess());
				mLogger.info("The status of minimum req existence is" + stat.toString());
				if("true".equals(stat.toString()))
				{
					if(resList != null && resList.size() > 0 && resList.get(j).getMinimumStatus() != null)
					{
						if(resList.get(j).getMinimumStatus().equalsIgnoreCase(TRUE))
						{
							PhoneScreenDAO.updatePhoneScreenMinimumRequirement(daoConn, query, handler, phoneScrnId, Short.parseShort(resList.get(j)
							    .getSeqNbr()), MINIMUM_REQUR_YES__DB_FLG);
						}
						else if(resList.get(j).getMinimumStatus().equalsIgnoreCase(FALSE))
						{
							PhoneScreenDAO.updatePhoneScreenMinimumRequirement(daoConn, query, handler, phoneScrnId, Short.parseShort(resList.get(j)
							    .getSeqNbr()), MINIMUM_REQUR_NO_DB_FLG);
						}
						else if(resList.get(j).getMinimumStatus().equalsIgnoreCase(MINIMUM_REQUR_NA_FLEX_FLG))
						{
							PhoneScreenDAO.updatePhoneScreenMinimumRequirement(daoConn, query, handler, phoneScrnId, Short.parseShort(resList.get(j)
							    .getSeqNbr()), MINIMUM_REQUR_NA_DB_FLG);
						}
					}
				}
				else
				{
					if(resList != null && resList.size() > 0 && resList.get(j).getMinimumStatus() != null)
					{
						if(resList.get(j).getMinimumStatus().equalsIgnoreCase(TRUE))
						{
							PhoneScreenDAO.createPhoneScreenMinimumRequirement(daoConn, query, handler, phoneScrnId, Short.parseShort(resList.get(j)
							    .getSeqNbr()), MINIMUM_REQUR_YES__DB_FLG, Short.parseShort(resList.get(j).getSeqNbr()));
						}
						else if(resList.get(j).getMinimumStatus().equalsIgnoreCase(FALSE))
						{
							PhoneScreenDAO.createPhoneScreenMinimumRequirement(daoConn, query, handler, phoneScrnId, Short.parseShort(resList.get(j)
							    .getSeqNbr()), MINIMUM_REQUR_NO_DB_FLG, Short.parseShort(resList.get(j).getSeqNbr()));
						}
						else if(resList.get(j).getMinimumStatus().equalsIgnoreCase(MINIMUM_REQUR_NA_FLEX_FLG))
						{
							PhoneScreenDAO.createPhoneScreenMinimumRequirement(daoConn, query, handler, phoneScrnId, Short.parseShort(resList.get(j)
							    .getSeqNbr()), MINIMUM_REQUR_NA_DB_FLG, Short.parseShort(resList.get(j).getSeqNbr()));
						}
					}
				}
			}
		}
		catch (QueryException qe)
		{
			qe.printStackTrace();
			throw qe;
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateMinimumRequirement( )");
		}
	}

	/**
	 * The method will be used for updating the Phone Screen Notes details in
	 * the DB.
	 * 
	 * @param phoneScrnId
	 *            - The DAO connection Object.
	 * @param detailsText
	 *            - The description about the detailed response.
	 * @param historyText
	 *            - The description about the contact history.
	 * @return boolean - Update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updatePhoneScreenNotes(final int phoneScrnId, final String detailsText, final String historyText) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updatePhoneScreenNotes( )" + "phoneScrnId: %1$s, detailsTxt: %2$s, historyTxt: %3$s",
			    phoneScrnId, detailsText, historyText));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		if(phoneScrnId > 0)
		{
			try
			{
				final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
				QueryManager queryManager = QueryManager.getInstance(contract);
				DAOConnection conn = queryManager.getDAOConnection(contract);
				ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						DAOConnection daoConn = daoConnList.get(contract);
						mLogger.info(this + "The connection obtained is" + daoConn);
						Query query = daoConn.getQuery();
						mLogger.info(this + "The query obtained is" + query);
						updateNotesForPhoneScreen(daoConn, query, phnScrnHandler, phoneScrnId, detailsText, historyText);
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating PhoneScreen Notes to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updatePhoneScreenNotes( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for updating the Phone Screen Notes details in
	 * the DB.
	 * 
	 * @param daoConn
	 *            - The DAO connection Object.
	 * @param query
	 *            -
	 * @param handler
	 *            - Query Handler
	 * @param phoneScrnId
	 *            - The DAO connection Object.
	 * @param detailsText
	 *            - The description about the detailed response.
	 * @param historyText
	 *            - The description about the contact history.
	 * @throws RetailStaffingException
	 */

	public static void updateNotesForPhoneScreen(DAOConnection daoConn, Query query, PhoneScreenHandler handler, int phoneScrnId, String detailsText,
	    String historyText) throws QueryException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateNotesForPhoneScreen( )" + "phoneScrnId: %1$s, detailsTxt: %2$s, historyTxt: %3$s",
			    phoneScrnId, detailsText, historyText));
		}

		// final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			StringBuffer status = new StringBuffer();
			PhoneScreenDAO.readHumanResourcesPhoneScreenNoteForExistence(daoConn, query, handler, phoneScrnId, (short)PHN_SCRN_RESPONSE_NOTE_TYP_CD);
			status.append(handler.isSuccess());
			mLogger.debug("The status of mininum response text existence is" + status.toString());
			if("true".equals(status.toString()))
			{
				PhoneScreenDAO.updateHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, (short)PHN_SCRN_RESPONSE_NOTE_TYP_CD, detailsText);
			}
			else
			{
				PhoneScreenDAO.createHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, (short)PHN_SCRN_RESPONSE_NOTE_TYP_CD, detailsText);
			}

			status.delete(0, status.length());
			mLogger.debug("The status of mininum response text after deletion is" + status.toString());

			PhoneScreenDAO.readHumanResourcesPhoneScreenNoteForExistence(daoConn, query, handler, phoneScrnId, (short)CONTACT_HISTRY_NOTE_TYP_CD);
			status.append(handler.isSuccess());
			mLogger.debug("The status of mininum response text existence is" + status.toString());
			if("true".equals(status.toString()))
			{
				PhoneScreenDAO.updateHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, (short)CONTACT_HISTRY_NOTE_TYP_CD, historyText);
			}
			else
			{
				PhoneScreenDAO.createHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, (short)CONTACT_HISTRY_NOTE_TYP_CD, historyText);
			}

		}
		catch (QueryException qe)
		{
			qe.printStackTrace();
			throw qe;
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateMinimumRequirement( )");
		}
	}

	/**
	 * The method will be used for updating the Phone Screen Notes details in
	 * the DB.
	 * 
	 * @param phoneScrnDetails
	 *            - The object containing PhoneScreenIntrwDetailsTO details to
	 *            be updated or inserted.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updateHumanResourcesPhoneScreen(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateHumanResourcesPhoneScreen( )");

		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		if(phoneScrnDetails != null)
		{
			try
			{
				final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
				QueryManager queryManager = QueryManager.getInstance(contract);
				DAOConnection conn = queryManager.getDAOConnection(contract);
				ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						DAOConnection daoConn = daoConnList.get(contract);
						mLogger.info(this + "The connection obtained is" + daoConn);
						Query query = daoConn.getQuery();
						mLogger.info(this + "The query obtained is" + query);
						PhoneScreenDAO.updateHumanResourcesPhoneScreenNonStatusCodes(daoConn, query, phnScrnHandler, phoneScrnDetails);
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating HumanResource PhoneScreen to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating HumanResource PhoneScreen to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateHumanResourcesPhoneScreen( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for updating the contact history details in the
	 * DB.
	 * 
	 * @param phoneScrnId
	 *            - The Unique id for the candidate.
	 * @param historyText
	 *            - The description about the contact history.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updateContactHist(final int phoneScrnId, final String historyText) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateContactHist( )" + "phoneScrnId: %1$s, historyText: %2$s", phoneScrnId, historyText));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		if(phoneScrnId > 0)
		{
			try
			{
				final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
				QueryManager queryManager = QueryManager.getInstance(contract);
				DAOConnection conn = queryManager.getDAOConnection(contract);
				ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						DAOConnection daoConn = daoConnList.get(contract);
						mLogger.info(this + "The connection obtained is" + daoConn);
						Query query = daoConn.getQuery();
						mLogger.info(this + "The query obtained is" + query);
						updateContactHistoryForPhoneScreen(daoConn, query, phnScrnHandler, phoneScrnId, historyText);
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating Contact History to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating Contact History to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateContactHist( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for updating the contact history details in the
	 * DB.
	 * 
	 * @param daoConn
	 *            - The DAO connection Object.
	 * @param query
	 *            -
	 * @param handler
	 *            - Query Handler
	 * @param phoneScrnId
	 *            - The Unique id for the candidate.
	 * @param historyText
	 *            - The description about the contact history.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static void updateContactHistoryForPhoneScreen(DAOConnection daoConn, Query query, PhoneScreenHandler phnScrnHandler, int phoneScrnId,
	    String historyText) throws QueryException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateContactHistoryForPhoneScreen( )" + "phoneScrnId: %1$s, historyTxt: %2$s",
			    phoneScrnId, historyText));
		}

		try
		{
			StringBuffer status = new StringBuffer();
			PhoneScreenDAO.readHumanResourcesPhoneScreenNoteForExistence(daoConn, query, phnScrnHandler, phoneScrnId, (short)CONTACT_HISTRY_NOTE_TYP_CD);
			status.append(phnScrnHandler.isSuccess());
			mLogger.debug("The status of mininum response text existence is" + status.toString());
			if("true".equals(status.toString()))
			{
				PhoneScreenDAO.updateHumanResourcesPhoneScreenNote(daoConn, query, phnScrnHandler, phoneScrnId, (short)CONTACT_HISTRY_NOTE_TYP_CD, historyText);
			}
			else
			{
				PhoneScreenDAO.createHumanResourcesPhoneScreenNote(daoConn, query, phnScrnHandler, phoneScrnId, (short)CONTACT_HISTRY_NOTE_TYP_CD, historyText);
			}

		}
		catch (QueryException qe)
		{
			qe.printStackTrace();
			throw qe;
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateContactHistoryForPhoneScreen( )");
		}
	}

	/**
	 * The method will be used for updating the ITI details in the DB.
	 * 
	 * @param phoneScrnDetails
	 *            - The object containing the PhoneScreenIntrwDetailsTO details
	 *            to be updated or inserted.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updateITIDetails(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateITIDetails( )");
		}
		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		ConnectionHandler connHandler = null;
		
		if(phoneScrnDetails != null)
		{
			try
			{
				QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
				QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
				
				final DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
				final DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);
				
				connHandler = new UniversalConnectionHandler(true, null, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)				
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();
						Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
				
						PhoneScreenDAO.updateHumanResourcesPhoneScreenNonStatusCodes(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, phoneScrnDetails);
						
						PhoneScreenDAO.updateHumanResourcesPhoneScreenStatus(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails.getItiNbr()),
						    Short.parseShort(StringUtils.trim(phoneScrnDetails.getPhoneScreenStatusCode())));
						
						//Update Status History Table for Phone Screen Status
						PhoneScreenDAO.updateStatusHistoryTable(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
								.getItiNbr()), STATUS_TYPE_CODE_PHN_SCRN, Short.parseShort(StringUtils.trim(phoneScrnDetails.getPhoneScreenStatusCode())));
						
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
						    .getItiNbr()), Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
						
						//Update Status History Table for Interview Status
						if (!StringUtils.trim(phoneScrnDetails.getInterviewStatusCode()).equals("0")) {
							PhoneScreenDAO.updateStatusHistoryTable(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
									.getItiNbr()), STATUS_TYPE_CODE_INTERVIEW_SCHD, Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
						}
						
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewMaterialStatus(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
							    .getItiNbr()), Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewMaterialStatusCode())));

						PhoneScreenDAO.updatePhoneScreenDispositionCode(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
								.getItiNbr()), phoneScrnDetails.getPhoneScreenDispositionCode());
						
						updateMinimumRequirement(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails.getItiNbr()), phoneScrnDetails
						    .getMinResList());
						
						updateNotesForPhoneScreen(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails.getItiNbr()), phoneScrnDetails
						    .getDetailTxt(), phoneScrnDetails.getContactHistoryTxt());

					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating HumanResource PhoneScreen to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating HumanResource PhoneScreen to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateHumanResourcesPhoneScreen( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for updating all the phone status in the DB.
	 * 
	 * @param phoneScrnDetails
	 *            - The object containing the PhoneScreenIntrwDetailsTO details
	 *            to be updated or inserted.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updateAllStatuses(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateITIDetails( )");
		}
		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		if(phoneScrnDetails != null)
		{
			try
			{
				final Contract contract = new BasicContract(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION);
				QueryManager queryManager = QueryManager.getInstance(contract);
				DAOConnection conn = queryManager.getDAOConnection(contract);
				ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						DAOConnection daoConn = daoConnList.get(contract);
						mLogger.info(this + "The connection obtained is" + daoConn);
						Query query = daoConn.getQuery();
						mLogger.info(this + "The query obtained is" + query);
						PhoneScreenDAO.updateHumanResourcesPhoneScreenStatus(daoConn, query, phnScrnHandler, Integer.parseInt(phoneScrnDetails.getItiNbr()),
						    Short.parseShort(StringUtils.trim(phoneScrnDetails.getPhoneScreenStatusCode())));
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(daoConn, query, phnScrnHandler, Integer.parseInt(phoneScrnDetails
						    .getItiNbr()), Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewMaterialStatus(daoConn, query, phnScrnHandler, Integer.parseInt(phoneScrnDetails
						    .getItiNbr()), Short.parseShort(phoneScrnDetails.getInterviewMaterialStatusCode().trim()));

					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating ITI Details to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating ITI Details to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateITIDetails( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for updating the interview screen details in the
	 * DB.
	 * 
	 * @param phoneScrnDetails
	 *            - The object containing the PhoneScreenIntrwDetailsTO details
	 *            to be updated or inserted.
	 * @return boolean - update successfully or not.
	 * @throws RetailStaffingException
	 */

	public static boolean updateInterviewScrnDetails(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateInterviewScrnDetails( )");
		}
		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		if(phoneScrnDetails != null)
		{
			try
			{
				final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
				QueryManager queryManager = QueryManager.getInstance(contract);
				DAOConnection conn = queryManager.getDAOConnection(contract);
				ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						DAOConnection daoConn = daoConnList.get(contract);
						mLogger.info(this + "The connection obtained is" + daoConn);
						Query query = daoConn.getQuery();
						mLogger.info(this + "The query obtained is" + query);
						PhoneScreenDAO.updateHumanResourcesPhoneScreenNonStatusCodes(daoConn, query, phnScrnHandler, phoneScrnDetails);
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewMaterialStatus(daoConn, query, phnScrnHandler, Integer.parseInt(phoneScrnDetails
						    .getItiNbr()), Short.parseShort(phoneScrnDetails.getInterviewMaterialStatusCode().trim()));
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(daoConn, query, phnScrnHandler, Integer.parseInt(phoneScrnDetails
						    .getItiNbr()), Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating Interview Screen Details to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateInterviewScrnDetails( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for inserting a batch of minimum response in the DB.
	 * 
	 * @param daoConn
	 *            - The database connection object.
	 * @param query
	 *            - The object for executing the query.
	 * @param handler
	 *            - The handler for the database operation.
	 * @param generatedPhoneScrnId
	 *            - generated unique phone screen id for the candidate.
	 * @throws RetailStaffingException
	 */

	public static void insertBatchMinimumReqsForPhoneScreen(DAOConnection daoConn, Query query, QueryHandler handler, int generatedPhoneScrnId)
	    throws QueryException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - insertBatchMinimumReqsForPhoneScreen()" + "phoneScrnId: %1$s", generatedPhoneScrnId));
		}

		try
		{
			List<Object> inputCreatePhoneScreenMinimumRequirementList = new ArrayList<Object>();
			
			for(int j = 0; j < 25; j++)
			{
				MapStream inputLIST = new MapStream();
				inputLIST.put("humanResourcesPhoneScreenId", generatedPhoneScrnId);
				inputLIST.put("sequenceNumber", (short) (j + 1));
				inputLIST.put("minimumRequirementFlag", MINIMUM_REQUR_NA_DB_FLG);
				inputLIST.putAllowNull("phoneScreenQuestionTypeCode", (short) (j + 1));
				inputCreatePhoneScreenMinimumRequirementList.add(inputLIST);
			}

			PhoneScreenDAO.createPhoneScreenMinimumRequirementBatch(daoConn, query, handler, generatedPhoneScrnId, inputCreatePhoneScreenMinimumRequirementList);

		}
		catch (QueryException qe)
		{
			qe.printStackTrace();
			throw qe;
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - insertBatchMinimumReqsForPhoneScreen()");
		}
	}

	/**
	 * The method will be used for inserting the phone screen notes in the DB.
	 * 
	 * @param daoConn
	 *            - The database connection object.
	 * @param query
	 *            - The object for executing the query.
	 * @param handler
	 *            - The handler for the database operation.
	 * @param phoneScrnId
	 *            - unique phone screen id for the candidate.
	 * @param historyText
	 *            - The description for contact history.
	 * @param detailText
	 *            - The description for detailed response.
	 * @throws RetailStaffingException
	 */

	public static void insertPhoneScreenNotes(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, String historyText, String detailText)
	    throws QueryException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - insertPhoneScreenNotes( )" + "phoneScrnId: %1$s, historyTxt: %2$s, detailText: %3$s ",
			    phoneScrnId, historyText, detailText));
		}

		try
		{
			for(int j = 0; j < 2; j++)
			{
				if(j == 0)
				{
					PhoneScreenDAO.createHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, CONTACT_HISTRY_NOTE_TYP_CD, historyText);
				}
				else
				{
					PhoneScreenDAO.createHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, PHN_SCRN_RESPONSE_NOTE_TYP_CD, detailText);
				}
			}

		}
		catch (QueryException qe)
		{
			qe.printStackTrace();
			throw qe;
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - insertPhoneScreenNotes( )");
		}
	}

	/**
	 * The method will be used for inserting ITI details in the DB.
	 * 
	 * @param phoneScrnDetails
	 *            - The object containing PhoneScreenIntrwDetailsTO object for
	 *            insert and update.
	 * @throws RetailStaffingException
	 */
	public static boolean insertITIDetails(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - insertITIDetails( )");
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		ConnectionHandler connHandler = null;
		
		if(phoneScrnDetails != null)
		{
			try
			{
				QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
				QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
				
				final DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
				final DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);
				
				connHandler = new UniversalConnectionHandler(true, null, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)				
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();
						Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
						
						//Create Phone Screen Record
						PhoneScreenDAO.createHumanResourcesPhoneScreen(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, phoneScrnDetails);
						int generatedId = phnScrnHandler.getPhoneScreenId();
						mLogger.debug("Generated PhoneScreen Id " + generatedId);

						//Create Phone Screen Minimum Requirement records
						insertBatchMinimumReqsForPhoneScreen(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, generatedId);

						//Create Phone Screen Notes records
						if(phoneScrnDetails.getNote() != null)
						{
							copyPhoneScreenNote(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, generatedId, phoneScrnDetails.getNote(), (short) 4);
							
							if (phoneScrnDetails.getContactHistoryTxt() != null) {
								copyPhoneScreenContactHistory(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, generatedId, phoneScrnDetails.getContactHistoryTxt(), (short) 1);
							}
						}
						else
						{
							insertPhoneScreenNotes(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, generatedId, phoneScrnDetails.getContactHistoryTxt(), phoneScrnDetails
							    .getDetailTxt());
						}
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred inserting ITIDetails to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred inserting ITIDetails to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - insertITIDetails( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for inserting ITI details in the DB.
	 * 
	 * @param phoneScrnDetails
	 *            - The object containing PhoneScreenIntrwDetailsTO object for
	 *            insert and update.
	 * @throws RetailStaffingException
	 */

	public static boolean insertHumanResourcesPhoneScreen(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - insertHumanResourcesPhoneScreen( )");

		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		if(phoneScrnDetails != null)
		{
			try
			{
				final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
				QueryManager queryManager = QueryManager.getInstance(contract);
				DAOConnection conn = queryManager.getDAOConnection(contract);
				ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						DAOConnection daoConn = daoConnList.get(contract);
						mLogger.info(this + "The connection obtained is" + daoConn);
						Query query = daoConn.getQuery();
						mLogger.info(this + "The query obtained is" + query);
						PhoneScreenDAO.createHumanResourcesPhoneScreen(daoConn, query, phnScrnHandler, phoneScrnDetails);
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating HumanResource PhoneScreen to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			catch (Exception e)
			{
				mLogger.error("An exception occurred updating HumanResource PhoneScreen to the PhoneScreenId", e);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - insertHumanResourcesPhoneScreen( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will returns the phone screen status code for the appropriate
	 * phone screen status description.
	 * 
	 * @param statusDesc
	 *            - The Phone screen status code description.
	 * @return statuscode - The Phone Screen Status code.
	 * @throws RetailStaffingException
	 */

	public static short checkPhoneScreenStatusCodeExist(final String statusDesc) throws RetailStaffingException
	{
		List<Status> statusList = null;
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - checkPhoneScreenStatusCodeExist( )" + "statusDesc: %1$s", statusDesc));
		}
		try
		{
			Map<StatusCacheKey, Status> phoneScreenStats = StatusManager.getStatusObjects(StatusType.PHONE_SCREEN_STATUS, "EN_US");
			statusList = new ArrayList<Status>(phoneScreenStats.values());
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("count -->" + statusList.size());
			}
			for(int index = 0; index < statusList.size(); index++)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Values Retrieving  from DB - " + "Status Description: %1$s, statusCode: %2$s", statusList.get(index)
					    .getStatDesc(), statusList.get(index).getDispStatCd()));
				}

				if(statusList.get(index).getStatDesc().trim().equalsIgnoreCase(statusDesc.trim()))
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Status Code Returning for the input: %1$s", statusList.get(index).getDispStatCd()));
					}
					return Short.parseShort(statusList.get(index).getDispStatCd());
				}
			}
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", e);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - checkPhoneScreenStatusCodeExist ( )");

		}
		return 0;
	}

	/**
	 * The method will returns the interview screen status code for the
	 * appropriate interview screen status description.
	 * 
	 * @param statusDesc
	 *            - The interview screen status code description.
	 * @return statuscode - The interview Screen Status code.
	 * @throws RetailStaffingException
	 */

	public static short checkInterviewScreenStatusCodeExist(final String statusDesc) throws RetailStaffingException
	{
		List<Status> statusList = null;
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - checkInterviewScreenStatusCodeExist( )" + "statusDesc: %1$s", statusDesc));
		}
		try
		{
			// TODO : pass this in!
			statusList = new ArrayList<Status>(StatusManager.getStatusObjects(StatusType.INTERVIEW_STATUS, "EN_US").values());
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("count -->" + statusList.size());
			}
			for(int index = 0; index < statusList.size(); index++)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Values Retrieving  from DB - " + "Status Description: %1$s, statusCode: %2$s", statusList.get(index)
					    .getStatDesc(), statusList.get(index).getDispStatCd()));
				}
				if(statusList.get(index).getStatDesc().trim().equalsIgnoreCase(statusDesc))
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Status Code Returning for the input: %1$s", statusList.get(index).getDispStatCd()));
					}
					return statusList.get(index).getCode();
				}
			}
		}

		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", e);
			throw new RetailStaffingException(FETCHING_INTERVIEW_RESPONSES_DETAILS_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - checkInterviewScreenStatusCodeExist ( )");
		}
		return 0;
	}

	/**
	 * This method changes the phone screen interview status. If the status is changing
	 * to INTERVIEW SCHEDULED (11), the phone screen materials status is also changed to 
	 * SEND CALENDAR PACKET (2)
	 * 
	 * @param phoneScreenId				The phone screen to update
	 * @param statusCode				The new interview status code
	 * 
	 * @throws QueryException			Thrown if an exception occurs updating the phone screen
	 * 									status
	 */
	public static void updateInterviewStatusForCTI(final int phoneScreenId, final short statusCode) throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering updateInterviewStatusForCTI(), phoneScreenId: %1$d, statusCode: %2$d",
				phoneScreenId, statusCode));
		} // end if
		
		// get an instance of the QueryManager
		QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
		// get the DAO connection from the QueryManager
		DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
		
		// create a new UniversalConnectionHandler that will be used to
		// execute all the queries needed using a single database connection
		UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(true, null, connection)
		{
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#
			 * handleQuery(java.util.Map,
			 * com.homedepot.ta.aa.dao.QueryHandler)
			 */
			public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
			{
				// get the DAO connection from the connection list
				DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);

				try
				{
					// first update the phone screen interview status
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Updating interview status for phone screen %1$d, setting to %2$d", phoneScreenId, statusCode));
					} // end if
					PhoneScreenManager.updateInterviewStatus(phoneScreenId, statusCode);
					
					//Added for FMS 7894, IVR Changes.  Added when Store Scheduling status, update the Materials Status as well. 
					if(statusCode == INTERVIEW_SCHEDULED)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("New interview status for phone screen %1$d set to INTERVIEW SCHEDULED, updating materials status to %2$d", 
									phoneScreenId, SEND_CALENDAR_PACKET));
						} // end if
						
						PhoneScreenManager.updatePhoneScreenStatusByInterviewMaterialStatCode(phoneScreenId, SEND_CALENDAR_PACKET);
					}
					else if(statusCode == STORE_SCHEDULING)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("New interview status for phone screen %1$d set to STORE SCHEDULING, updating materials status to %2$d", 
									phoneScreenId, SEND_EMAIL_PACKET));								
						} // end if
						
						PhoneScreenManager.updatePhoneScreenStatusByInterviewMaterialStatCode(phoneScreenId, SEND_EMAIL_PACKET);
					}
				} // end try
				catch(RetailStaffingException rse)
				{
					// log the root exception
					mLogger.error(String.format("An exception occurred updating the interview status of phone screen %1$d", phoneScreenId), rse);
					
					// rollback the transaction
					connection.getControls().rollback();
					
					// throw the exception
					throw new QueryException(rse.getMessage());
				} // end catch
			} // end function handleQuery()
		}; // end anonymous inner class

		// execute the query
		connectionHandler.execute();
	}	
	
	/**
	 * The method will be used for inserting Minimum PhoneScreen Req details in
	 * the DB.
	 * 
	 * @param request
	 *            - The object contains the phoneScreen Minimum Requirement
	 *            details
	 * @param version
	 * 			  - Version of the client invoking this function. Based on version
	 * 			    different logic branches will occur
	 * @return status - The boolean value whether the update is successful or
	 *         not.
	 * @throws RetailStaffingException
	 */

	public static boolean insertMinimumReqsForPhoneScreenForCTI(InsertPhoneScreenRequest request, int version) throws IllegalArgumentException, QueryException, RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering insertMinimumReqsForPhoneScreenForCTI(), request: %1$s", request.toString()));
		} // end if
		
		boolean passed = true;
		int phnScrnId;
		
		try
		{
			// validate a phone screen id was provided			
			if(request.getPhoneScreenId() == null || request.getPhoneScreenId().trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s phone screen id provided", (request.getPhoneScreenId() == null ? "null" : "empty")));
			} // end if(request.getPhoneScreenId() == null || request.getPhoneScreenId().trim().length() == 0)
			
			// make sure the version provided is supported (currently only version 0 and 1 are supported)
			if(version != 0 && version != 1)
			{
				throw new IllegalArgumentException(String.format("Invalid version %1$d provided", version));
			} // end if(version != 0 && version != 1)
			
			// convert the phone screen id to an int since that's what's needed for the update
			// TODO : why not transport this as an int if that's what it is?
			try
			{
				phnScrnId = Integer.parseInt(request.getPhoneScreenId());
			} // end try
			catch(NumberFormatException nfe)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$s provided", request.getPhoneScreenId()));
			} // end catch
			
			// validate the phone screen id is > 0
			if(phnScrnId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phnScrnId));
			} // end if(phnScrnId < 1)
			
			// validate we received at least one question to insert
			if(request.getPhoneScreenMinReqTOList() == null || request.getPhoneScreenMinReqTOList().size() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s phone screen question response list provided for phone screen %2$d", 
					(request.getPhoneScreenMinReqTOList() == null ? "Null" : "Empty"), phnScrnId));
			} // end if(request.getPhoneScreenMinReqTOList() == null || request.getPhoneScreenMinReqTOList().size() == 0)
			
			// TODO: WHY ARE WE USING A DIFFERENT OBJECT FOR THIS?
			MinimumResponseTO response = null;
			List<MinimumResponseTO> responses = new ArrayList<MinimumResponseTO>(request.getPhoneScreenMinReqTOList().size());
			// question sequence number
			int seq = 1;
			
			// the logic executed by this method changes based on the client version the request is from
			switch(version)
			{
				case 1: // version 1 clients require the answers to be inserted in the same order that they were received by the getPhoneScreenKnockOutQuest() method
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Phone screen %1$s provided version %2$d, sorting knockout questions prior to insert", phnScrnId, version));
						mLogger.debug(String.format("Phone screen %1$s question order prior to sorting was: ", phnScrnId));
						
						for(PhoneScreenMinReqTO inputResponse : request.getPhoneScreenMinReqTOList())
						{
							mLogger.debug(String.format("%1$s = %2$s", inputResponse.getId(), inputResponse.getAnswer()));
						} // end for(PhoneScreenMinReqTO inputResponse : request.getPhoneScreenMinReqTOList())

						mLogger.debug(String.format("Getting phone screen knockout questions for phone screen id %1$d", phnScrnId));
					} // end if
					
					// call to get the knockout questions in the correct order
					List<String> sortedQuestionIds = getPhoneScreenKnockOutQuest(phnScrnId, false);
															
					Map<String, String> inputResponses = new HashMap<String, String>();
					
					mLogger.debug("Populating phone screen questions map");
					// iterate the phone screen questions and convert them into a map, key = id, value = answer
					for(PhoneScreenMinReqTO inputResponse : request.getPhoneScreenMinReqTOList())
					{
						// validate the objects values
						validatePhoneScreenQuestionResponse(inputResponse, phnScrnId);
						// add the question id and answer to the map
						inputResponses.put(inputResponse.getId().trim(), inputResponse.getAnswer().trim());
					} // end for(PhoneScreenMinReqTO inputResponse : request.getPhoneScreenMinReqTOList())
					
					String answer = null;
					
					// now iterate the sorted question id's 
					for(String id : sortedQuestionIds)
					{
						// get the answer for this question id from the map
						answer = inputResponses.get(id);
							
						// make sure this id exists in the response map
						if(answer == null)
						{
							throw new IllegalArgumentException(String.format("Phone screen %1$d is missing a response for question id %2$s", phnScrnId, id));
						} // end if(!inputResponses.containsKey(id))
						
						// initialize a new MinimumResponseTO
						// TODO : WHY CAN'T WE USE THE SAME OBJECT FOR BOTH?
						response = new MinimumResponseTO();
							
						// set the sequence number (and increment it by 1)
						response.setSeqNbr(String.format("%1$s", seq++));
						// set the answer
						response.setMinimumStatus(answer);
							
						// if any of the answers are "NO" then the candidate did not pass the knockout question test
						if(answer.equals(NO))
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Question %1$s contained a \"%2$s\" answer, setting passed to false for phone screen %3$d", id, NO, phnScrnId));
							} // end if
							passed = false;
						} // end if(answer.equals(NO))
						
						// add this to the collection that will be inserted into the database
						responses.add(response);
							
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Added question %1$s with answer %2$s to map for phone screen %3$d", id, answer, phnScrnId));
						} // end if							
					} // end for(String id : sortedQuestionIds)

					break;
				} // end case 1
				default:
				{
					// the default is to insert the questions in the same order they were received in
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Phone screen %1$s provided version %2$d, inserting knockout questions in the order they were received", phnScrnId, version));
					} // end if
					
					// iterate the input responses and convert them into a list of objects required for the insert
					for(PhoneScreenMinReqTO inputResponse : request.getPhoneScreenMinReqTOList())
					{
						// validate the objects values
						validatePhoneScreenQuestionResponse(inputResponse, phnScrnId);
						
   						// initialize a new MinimumResponseTO
						// TODO : WHY CAN'T WE USE THE SAME OBJECT FOR BOTH?
						response = new MinimumResponseTO();
						
						// set the sequence number (and increment it by 1)
						response.setSeqNbr(String.format("%1$s", seq++));
						// set the answer
						response.setMinimumStatus(inputResponse.getAnswer());
						
						// if any of the answers are "NO" then the candidate did not pass the knockout question test
						if(inputResponse.getAnswer().equals(NO))
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Question %1$s contained a \"%2$s\" answer, setting passed to false for phone screen %3$d", 
									inputResponse.getId(), NO, phnScrnId));
							} // end if
							passed = false;
						} // end if(inputResponse.getAnswer().equals(NO))
						
						// add this to the collection that will be inserted into the database
						responses.add(response);
					} // end for(PhoneScreenMinReqTO inputResponse : request.getPhoneScreenMinReqTOList())
				} // end default
			} // end switch(version)			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Inserting phone screen knockout question responses for phone screen %1$d", phnScrnId));
			} // end if
			
			// insert the phone screen knockout question responses			
			if(updateMinimumReqsForPhoneScreen(phnScrnId, responses))
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Successfully inserted phone screen knockout question responses for phone screen %1$d", phnScrnId));
				} // end if
				
				// if the user answered NO to any questions, also switch the status of the phone screen to do not proceed
				if(!passed)
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Switching status of phone screen %1$d to \"Do Not Proceed\"", phnScrnId));
					} // end if
					
					//Added for Defect 3952
					updatePhoneScreenNotes(phnScrnId, DETAILS_TXT ,"" );
					// switch the phone screen status
					updateHumanResourcesMinimumRequirementStatusCode(phnScrnId, IVR_USER, DO_NOT_PROCEED_STAT);
				} // end if(!passed)
			} // end if(updateMinimumReqsForPhoneScreen(phnScrnId, responses))
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the exception
			mLogger.error(String.format("Illegal argument provided: %1$s", iae.getMessage()), iae);
			// throw the exception
			throw iae;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error("An exception occurred inserting phone screen knockout question responses", qe);
			// throw the exception
			throw qe;			
		} // end catch
		catch(RetailStaffingException re)
		{
			// log the exception
			mLogger.error("An exception occurred inserting phone screen knockout question responses", re);
			// throw the exception
			throw re;
		} // end catch
		
		return passed;
	} // end function insertMinimumReqsForPhoneScreenForCTI()
	
	/*
	 * Convenience method to validate a phone screen question response object
	 * 
	 * @param inputResponse					The response object to validate
	 * @param phnScrnId						The phone screen id this response was for
	 * 
	 * @throws IllegalArgumentException		Thrown if any of the following are true:
	 * 										<ul>
	 * 											<li>the question response object in null</li>
	 * 											<li>the question response contains a null or empty id</li>
	 * 											<li>the question response contains a null or empty answer</li>
	 * 											<li>the question response contains an invalid answer, a.k.a. not Y/N/A</li>
	 * 										</ul>
	 */
	private static void validatePhoneScreenQuestionResponse(PhoneScreenMinReqTO inputResponse, int phnScrnId) throws IllegalArgumentException
	{
		// only process the response if it's not null
		if(inputResponse == null)
		{					
			throw new IllegalArgumentException(String.format("Null input response object provided for phone screen %1$d", phnScrnId));
		} // end if(inputResponse == null)

		// validate the input response object contains the fields it is supposed to have
		if(inputResponse.getId() == null || inputResponse.getId().trim().length() == 0)
		{
			throw new IllegalArgumentException(String.format("Input response contained a %1$s id for phone screen %2$d", 
				(inputResponse.getId() == null ? "null" : "empty"), phnScrnId));
		} // end if(inputResponse.getId() == null || inputResponse.getId().trim().length() == 0)
		
		// validate an answer was provided
		if(inputResponse.getAnswer() == null || inputResponse.getAnswer().trim().length() == 0)
		{
			throw new IllegalArgumentException(String.format("%1$s answer provided for question id %2$s for phone screen id %3$d", 
				(inputResponse.getAnswer() == null ? "Null" : "Empty"), inputResponse.getId(), phnScrnId));
		} // end if(inputResponse.getAnswer() == null || inputResponse.getAnswer().trim().length() == 0)
		
		// trim the answer
		inputResponse.setAnswer(inputResponse.getAnswer().trim());
		
		// validate the answer is a single character
		if(inputResponse.getAnswer().length() > 1)
		{
			throw new IllegalArgumentException(String.format("%1$s contained an invalid answer %2$s for phone screen %3$d", 
				inputResponse.getId(), inputResponse.getAnswer(), phnScrnId));
		} // end if(inputResponse.getAnswer().length() > 1)
		
		// validate the answer contained is a Y/N
		if(!inputResponse.getAnswer().equalsIgnoreCase(YES) && !inputResponse.getAnswer().equalsIgnoreCase(NO) && !inputResponse.getAnswer().equals(NA))
		{
			throw new IllegalArgumentException(String.format("%1$s contained an invalid answer %2$s for phone screen %3$d", 
				inputResponse.getId(), inputResponse.getAnswer(), phnScrnId));
		} // end if(!inputResponse.getAnswer().equalsIgnoreCase(YES) && !inputResponse.getAnswer().equalsIgnoreCase(NO) && !inputResponse.getAnswer().equals(NA))
	} // end function validatePhoneScreenQuestionResponse()
	
	
	
	
	
	
	
	

	public static boolean updateHumanResourcesMinimumRequirementStatusCode(final int phoneScrnId, final String phoneScreenName, final short phoneScrnStatus)
	    throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - updateHumanResourcesMinimumRequirementStatusCode( )"
			    + "phoneScrnId: %1$s, phoneScreenName: %2$s,  phoneScrnStatus: %3$s", phoneScrnId, phoneScreenName, phoneScrnStatus));
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
			QueryManager queryManager = QueryManager.getInstance(contract);
			DAOConnection conn = queryManager.getDAOConnection(contract);
			ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
			{
				public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
				{
					DAOConnection daoConn = daoConnList.get(contract);
					mLogger.info(this + "The connection obtained is" + daoConn);
					Query query = daoConn.getQuery();
					mLogger.info(this + "The query obtained is" + query);
					PhoneScreenDAO.updateHumanResourcesPhoneScreenMinimumRequirementStatusCode(daoConn, query, phnScrnHandler, phoneScrnId, phoneScreenName,
					    phoneScrnStatus);

				}
			};
			connHandler.execute();
		}
		catch (QueryException qe)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Screen Status to the PhoneScreenId", e);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateHumanResourcesPhoneScreenStatus( )");
		}
		return phnScrnHandler.isSuccess();
	}

	public static POMRsaStatusCrossRefResponse pomRsaCrossRef(final POMRsaStatusCrossRefRequest reconreq) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - pomRsaCrossRef( )");
		}

		final List<POMRsaStatusCrossRefResponse> pomCrossRefResponse = new ArrayList<POMRsaStatusCrossRefResponse>(1);
		try
		{
			final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);
			QueryManager queryManager = QueryManager.getInstance(contract);
			DAOConnection conn = queryManager.getDAOConnection(contract);
			ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
			{
				public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
				{
					DAOConnection daoConn = daoConnList.get(contract);
					mLogger.info(this + "The connection obtained is" + daoConn);
					Query query = daoConn.getQuery();
					mLogger.info(this + "The query obtained is" + query);
					pomCrossRefResponse.add(PhoneScreenDAO.readNlsPomCompleteStatusByInputList(reconreq.getCampaignType(), reconreq.getCompletionCode(),
					    reconreq.getLanguageCode()));
				}
			};
			connHandler.execute();
		}
		catch (QueryException qe)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", qe);
			throw new RetailStaffingException(FETCHING_POM_RSA_CROSS_REF_ERROR_CODE, qe);
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Screen Status to the PhoneScreenId", e);
			throw new RetailStaffingException(FETCHING_POM_RSA_CROSS_REF_ERROR_CODE, e);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - pomRsaCrossRef( )");
		}

		return pomCrossRefResponse.get(0);
	}

	public static boolean pomRsHandlerRecon(final HrRetlStffReconsialtionRequest reconreq) throws RetailStaffingException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - pomRsHandlerRecon( )");
		}

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		boolean status = false;
		try
		{
			final Contract contract = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION);

			QueryManager queryManager = QueryManager.getInstance(contract);

			DAOConnection conn = queryManager.getDAOConnection(contract);

			ConnectionHandler connHandler = new UniversalConnectionHandler(false, null, conn)
			{

				java.sql.Timestamp complete = null;

				java.sql.Timestamp callBegin = null;

				java.sql.Timestamp callEnd = null;
				short interviewRespStatCode = 0;

				public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
				{

					final String campaignType = reconreq.getCampaignType();
					final int jobId = Integer.parseInt(reconreq.getJobId());
					final String contactID = reconreq.getContactId();
					final short completeStatusCode = Short.parseShort(reconreq.getCompletionCode());
					/*if(reconreq.getInterviewResponseStatus() != null && reconreq.getInterviewResponseStatus().length() > 0)
					{
						interviewRespStatCode = Short.parseShort(reconreq.getInterviewResponseStatus());
					}*/

					if(reconreq.getCmpTS() != null && reconreq.getCmpTS().length() > 0)
					{
						complete = java.sql.Timestamp.valueOf(reconreq.getCmpTS());
					}
					if(reconreq.getCallBegTS() != null && reconreq.getCallBegTS().length() > 0)
					{
						callBegin = java.sql.Timestamp.valueOf(reconreq.getCallBegTS());
					}

					if(reconreq.getCallEndTS() != null && reconreq.getCallEndTS().length() > 0)
					{
						callEnd = java.sql.Timestamp.valueOf(reconreq.getCallEndTS());
					}

					DAOConnection daoConn = daoConnList.get(contract);
					mLogger.info(this + "The connection obtained is" + daoConn);
					Query query = daoConn.getQuery();
					mLogger.info(this + "The query obtained is" + query);
					PhoneScreenDAO.updateHumanResourcesRetailStaffReconciliation(daoConn, query, phnScrnHandler, campaignType, jobId, contactID,
					    completeStatusCode, reconreq.getInterviewResponseStatus(), complete, callBegin, callEnd);

				}

			};

			connHandler.execute();

		}
		catch (QueryException qe)
		{
			mLogger.error("An exception occurred updating Schedule Status to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
		}
		catch (Exception e)
		{
			mLogger.error("An exception occurred updating Screen Status to the PhoneScreenId", e);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, e);
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - pomRsHandlerRecon( )");
		}
		return phnScrnHandler.isSuccess();
	}

	public static boolean updateInterviewDetails(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateInterviewDetails( )");
		}
		List<RequisitionDetailTO> requisitionDetailTOList = null;
		short interviewDuration = 0;
		short seqNbr = 1;
		Timestamp firstInterviewSlotTimestamp = null;
		Timestamp secondInterviewSlotTimestamp = null;

		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		try
		{
			// requisitionDetailTOList = PhoneScreenDAO.readRequisitionDetails(
			// Integer.parseInt(phoneScrnDetails.getReqCalId() ));
			requisitionDetailTOList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(phoneScrnDetails.getReqNbr().toString()));
			if(requisitionDetailTOList != null)
			{
				interviewDuration = Short.parseShort(requisitionDetailTOList.get(0).getInterviewDurtn());

				seqNbr = Short.parseShort(phoneScrnDetails.getIntrvSeqNbr());

				mLogger.debug(String.format("@@@@@@@@@@@@@@@@@ interviewDuration :%1$d @@@@@@@@@@@@@@@@@", interviewDuration));

				firstInterviewSlotTimestamp = Util.convertTimestampTO(phoneScrnDetails.getIntrLocDtls().getInterviewTime());

				if(interviewDuration == SCHEDULING_INTERVAL_60)
				{
					Calendar blockCal = Calendar.getInstance();
					blockCal.setTimeInMillis(firstInterviewSlotTimestamp.getTime());
					blockCal.add(Calendar.MINUTE, SCHEDULING_INTERVAL_30);
					secondInterviewSlotTimestamp = new Timestamp(blockCal.getTimeInMillis());
				}

			}
			updateInterviewScrnDetails(phoneScrnDetails, interviewDuration, seqNbr, firstInterviewSlotTimestamp, secondInterviewSlotTimestamp, phnScrnHandler);
		}

		catch (RetailStaffingException qe)
		{
			qe.printStackTrace();
			mLogger.error("An exception occurred updating Interview Screen Details to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateInterviewDetails( )");
		}
		return phnScrnHandler.isSuccess();
	}

	public static boolean updateInterviewStatusAndMaterialStatusCode(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateInterviewStatusAndMaterialStatusCode( )");
		}
		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		if(phoneScrnDetails != null)
		{
			try
			{
				QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
				QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
				
				final DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
				final DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);
				
				UniversalConnectionHandler connHandler = new UniversalConnectionHandler(true, null, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)				
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();
						Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
				
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewMaterialStatus(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
							    .getItiNbr()), Short.parseShort(phoneScrnDetails.getInterviewMaterialStatusCode().trim()));
						
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
						    .getItiNbr()), Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
						
						//Update Status History Table for Interview Status
						if (!StringUtils.trim(phoneScrnDetails.getInterviewStatusCode()).equals("0")) {
							PhoneScreenDAO.updateStatusHistoryTable(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
									.getItiNbr()), STATUS_TYPE_CODE_INTERVIEW_SCHD, Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
						}						
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating Interview Screen Details to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateInterviewStatusAndMaterialStatusCode( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	// Added for New Datamodel Change by SS
	// TODO : If the calendar ID is supposed to be an int, why transport it around as a String?
	public static void updateInterviewScrnDetails(final PhoneScreenIntrwDetailsTO phoneScrnDetails, final short duration, final short seqNbr,
	    final Timestamp firstSlotBeginTimestamp, final Timestamp secondSlotBeginTimestamp, final PhoneScreenHandler phnScrnHandler)
	    throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("\n Enter PhoneScreenManager - updateInterviewScrnDetails( )"
			    + "duration: %1$d, firstSlotBeginTimestamp: %2$s, secondSlotBeginTimestamp %3$s, seqNbr %4$d", duration, firstSlotBeginTimestamp,
			    secondSlotBeginTimestamp, seqNbr));
		}

		try
		{
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			final DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			final DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);
			
			UniversalConnectionHandler connHandler = new UniversalConnectionHandler(true, null, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)				
			{
				public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
				{
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
			
					// first check to see if there are already interview slots scheduled for this phone screen
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Checking to see if phone screen %1$s already has scheduled interview blocks", phoneScrnDetails.getItiNbr()));
					} // end if
					
					List<RequisitionScheduleTO> scheduledSlots = getScheduledInterviewBlocksForPhoneScreen(Integer.parseInt(phoneScrnDetails.getItiNbr()));
					
					// if there are scheduled slots
					if(scheduledSlots != null && scheduledSlots.size() > 0)
					{
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Phone screen %1$s has %2$d scheduled interview slots, releasing them.", phoneScrnDetails.getItiNbr(), scheduledSlots.size()));
						} // end if
						
						// call to release the blocks currently scheduled
						for(RequisitionScheduleTO schedule : scheduledSlots)
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Releasing slot %1$s, sequence %2$d on calendar %3$d", schedule.getBeginTimestamp(), schedule.getInterviewerAvailabilityCount(), 
									schedule.getCalendarId()));
							} // end if
							
							ScheduleManager.updateInterviewSlot(AVALIABLE_STATUS_CODE, null, schedule.getCalendarId(), schedule.getBeginTimestamp(), 
								schedule.getInterviewerAvailabilityCount(), NON_SCHEDULED_STATUSES, null);
						} // end for(RequisitionScheduleTO schedule : scheduledSlots)
					} // end if(scheduledSlots != null && scheduledSlots.size() > 0)

					// now schedule the new slots
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Scheduling new interview times for phone screen %1$s", phoneScrnDetails.getItiNbr()));
					} // end if					
					ScheduleManager.updateInterviewSlot(SCHEDULED_STATUS_CODE, null, Integer.parseInt(phoneScrnDetails.getReqCalId()), firstSlotBeginTimestamp, 
						seqNbr, SCHEDULED_STATUSES, Integer.parseInt(phoneScrnDetails.getItiNbr()));
						
					if(duration == SCHEDULING_INTERVAL_60)
					{
						ScheduleManager.updateInterviewSlot(SCHEDULED_STATUS_CODE, null, Integer.parseInt(phoneScrnDetails.getReqCalId()), secondSlotBeginTimestamp, 
							seqNbr, SCHEDULED_STATUSES, Integer.parseInt(phoneScrnDetails.getItiNbr()));							
					}					
					
					PhoneScreenDAO.updateHumanResourcesPhoneScreenNonStatusCodes(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, phoneScrnDetails);
					
					PhoneScreenDAO.updatePhoneScreenStatusByInterviewMaterialStatus(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, 
						Integer.parseInt(phoneScrnDetails.getItiNbr()), Short.parseShort(phoneScrnDetails.getInterviewMaterialStatusCode().trim()));
										
					PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
					    .getItiNbr()), Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
					
					//Update Status History Table for Interview Status
					if (!StringUtils.trim(phoneScrnDetails.getInterviewStatusCode()).equals("0")) {
						PhoneScreenDAO.updateStatusHistoryTable(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
								.getItiNbr()), STATUS_TYPE_CODE_INTERVIEW_SCHD, Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
					}
				}
			};
			connHandler.execute();
		}
		catch (QueryException qe)
		{
			qe.printStackTrace();
			mLogger.error("An exception occurred updating Interview Screen Details to the PhoneScreenId and Interview Tables", qe);
			// throw new
			// RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			// logger.info("Failure to Update record - Already Allocated");
			throw new RetailStaffingException(RetailStaffingConstants.FETCHING_INTERVIEW_TIMESLOTS_ERROR_CODE);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - updateInterviewScrnDetails( )");
		}
	}

	public static boolean updateInterviewStatusforPhoneScrn(final PhoneScreenIntrwDetailsTO phoneScrnDetails) throws RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateInterviewStatusAndMaterialStatusCode( )");
		}
		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();
		ConnectionHandler connHandler = null;
		
		if(phoneScrnDetails != null)
		{
			try
			{
				QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
				QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
				
				final DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
				final DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);
				
				connHandler = new UniversalConnectionHandler(true, null, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)				
				{
					public void handleQuery(Map<DAOConnection, DAOConnection> daoConnList, QueryHandler queryHandler) throws QueryException
					{
						Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();
						Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
				
						PhoneScreenDAO.updateHumanResourcesPhoneScreenNonStatusCodes(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, phoneScrnDetails);
						
						PhoneScreenDAO.updateHumanResourcesPhoneScreenStatus(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails.getItiNbr()),
						    Short.parseShort(StringUtils.trim(phoneScrnDetails.getPhoneScreenStatusCode())));
												
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewScreenStatCode(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
							    .getItiNbr()), Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
						
						//Update Status History Table for Interview Status
						if (!StringUtils.trim(phoneScrnDetails.getInterviewStatusCode()).equals("0")) {
							PhoneScreenDAO.updateStatusHistoryTable(workforceRecruitmentDaoConn, workforceRecruitmentQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
									.getItiNbr()), STATUS_TYPE_CODE_INTERVIEW_SCHD, Short.parseShort(StringUtils.trim(phoneScrnDetails.getInterviewStatusCode())));
						}				
						
						PhoneScreenDAO.updatePhoneScreenStatusByInterviewMaterialStatus(hrHrStaffingDaoConn, hrHrStaffingQuery, phnScrnHandler, Integer.parseInt(phoneScrnDetails
							    .getItiNbr()), Short.parseShort(phoneScrnDetails.getInterviewMaterialStatusCode().trim()));						
					}
				};
				connHandler.execute();
			}
			catch (QueryException qe)
			{
				mLogger.error("An exception occurred updating Interview Screen Details to the PhoneScreenId", qe);
				throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
			}
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - updateInterviewStatusAndMaterialStatusCode( )");
			}
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * The method will be used for inserting the phone screen note or contact history in the DB
	 * during the copy phone screen process.
	 * 
	 * @param daoConn
	 *            - The database connection object.
	 * @param query
	 *            - The object for executing the query.
	 * @param handler
	 *            - The handler for the database operation.
	 * @param phoneScrnId
	 *            - unique phone screen id for the candidate.
	 * @param phoneNote
	 *            - PhoneScreenNoteTO with data from the copied phone screen
	 * @throws RetailStaffingException
	 */

	public static void copyPhoneScreenNote(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, PhoneScreenNoteTO phoneNote, short noteType)
	    throws QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - copyPhoneScreenNote( )" + "phoneScrnId: %1$s ", phoneScrnId));
		}

		try
		{
			PhoneScreenDAO.createHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, noteType, phoneNote.getPhoneScreenNoteText());
		}
		catch (QueryException qe)
		{
			throw qe;
		}

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - copyPhoneScreenNote( )");
		}
	}

	public static void copyPhoneScreenContactHistory(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, String contactHistory, short noteType)
		    throws QueryException
		{
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Enter PhoneScreenManager - copyPhoneScreenNote( )" + "phoneScrnId: %1$s ", phoneScrnId));
			}

			try
			{
				PhoneScreenDAO.createHumanResourcesPhoneScreenNote(daoConn, query, handler, phoneScrnId, noteType, contactHistory);
			}
			catch (QueryException qe)
			{
				throw qe;
			}

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug("Exit PhoneScreenManager - copyPhoneScreenNote( )");
			}
		}	
	
	public static String generatePOMResponseXML(POMRsaStatusCrossRefResponse response)
	{
		StringBuilder responseXML = new StringBuilder(256);
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - generatePOMResponseXML( )");
		}
		if(response != null)
		{
			responseXML.append("<pomRsaStatusCrossRefResponse>");
			responseXML.append("<status>" + true + "</status>");
			responseXML.append("<pomCompleteStatusCode>" + response.getPomCompleteStatusCode() + "</pomCompleteStatusCode>");
			responseXML.append("<interviewRespondStatusCode>" + response.getInterviewRespondStatusCode() + "</interviewRespondStatusCode>");
			responseXML.append("<rsaUpdateFlag>" + response.getRsaUpdateFlag() + "</rsaUpdateFlag>");
			responseXML.append("</pomRsaStatusCrossRefResponse>");
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - generatePOMResponseXML( )");
		}

		return responseXML.toString();
	}

	/*
	 * public static String getVDNDetails( String jobcode, String callType ) {
	 * String vdnDetails = null;
	 * 
	 * // call the DAO here to populate the TO // TODO : add call to DAO here
	 * 
	 * return vdnDetails; }
	 */
	
	// Used by RSA3.0 updates
	public static boolean updateInterviewSchedulePhoneScrnDetails(final PhoneScreenIntrwDetailsTO phoneScrnDetails, String[] checkBeginTimeValue)
	    throws RetailStaffingException
	{

		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateInterviewSchedulePhoneScrnDetails( )");
		}
		List<RequisitionDetailTO> requisitionDetailTOList = null;
		short interviewDuration = 0;
		short seqNbr = 1;
		Timestamp firstInterviewSlotTimestamp = null;
		Timestamp secondInterviewSlotTimestamp = null;
		final PhoneScreenHandler phnScrnHandler = new PhoneScreenHandler();

		try
		{

			requisitionDetailTOList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(phoneScrnDetails.getReqNbr().toString()));
			if(requisitionDetailTOList != null)
			{
				interviewDuration = Short.parseShort(requisitionDetailTOList.get(0).getInterviewDurtn());

				if(checkBeginTimeValue != null && checkBeginTimeValue.length > 0)
				{
					seqNbr = Short.parseShort(checkBeginTimeValue[1].toString());
				}

				mLogger.debug(String.format(" New SeqNbr       :%1$d ", seqNbr));
				mLogger.debug(String.format(" Existing SeqNbr  :%1$d ", Short.parseShort(phoneScrnDetails.getIntrvSeqNbr())));
				mLogger.debug(String.format(" InterviewDuration :%1$d ", interviewDuration));
				firstInterviewSlotTimestamp = Util.convertTimestampTO(phoneScrnDetails.getIntrLocDtls().getInterviewTime());

				if(interviewDuration == SCHEDULING_INTERVAL_60)
				{
					Calendar blockCal = Calendar.getInstance();
					blockCal.setTimeInMillis(firstInterviewSlotTimestamp.getTime());
					blockCal.add(Calendar.MINUTE, SCHEDULING_INTERVAL_30);
					secondInterviewSlotTimestamp = new Timestamp(blockCal.getTimeInMillis());
				}

			}
			updateInterviewScrnDetails(phoneScrnDetails, interviewDuration, seqNbr, firstInterviewSlotTimestamp, secondInterviewSlotTimestamp, phnScrnHandler);
		}

		catch (RetailStaffingException qe)
		{
			qe.printStackTrace();
			mLogger.error("An exception occurred updating Interview Screen Details to the PhoneScreenId", qe);
			throw new RetailStaffingException(UPDATING_PHONE_DETAILS_ERROR_CODE, qe);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Enter PhoneScreenManager - updateInterviewSchedulePhoneScrnDetails( )");
		}
		return phnScrnHandler.isSuccess();
	}

	/**
	 * This method gets phone screen candidate details for the phone screen id provided
	 * 
	 * @param phoneScreenId					The phone screen identifier
	 * 
	 * @return								Details about the candidate, phone screen, and interview for
	 * 										the phone screen identifier provided
	 * 
	 * @throws IllegalArgumentException		Thrown if the phone screen id provided is < 1
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database
	 */
	public static PhoneScreenIntrwDetailsTO getPhoneScreenCandidateDetails(final int phoneScreenId) throws IllegalArgumentException, QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getPhoneScreenCandidateDetails(), phoneScreenId: %1$d", phoneScreenId));
		} // end if

		// create a handler object that will be used to process the results of the queries about to be executed
		PhoneScreenHandler handler = new PhoneScreenHandler();		

		try
		{
			// validate the phone screen id provided is > 0
			if(phoneScreenId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phoneScreenId));
			} // end if(phoneScreenId < 1)
			
			// get an instance of the DAO query manager
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			//get a database connection from the DAO query manager
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);			
			
			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, handler, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
					
					// cast the handler to the right type
					PhoneScreenHandler phnScrnHandler = (PhoneScreenHandler)handler;
			
					// initialize the query parameters required to execute the first query
					MapStream inputs = new MapStream(SEL_READ_HR_PHN_SCRN);
					inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying for phone screen details using phone screen id %1$d", phoneScreenId));
					} // end if
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
					
					// get the object populated by the query handler
					PhoneScreenIntrwDetailsTO details = phnScrnHandler.getPhnScrnIntrvwDetails();
					
					// make sure the first query brought back results
					if(details != null)
					{
						/*
						 * Check the internal/external flag on the detail object returned. If a row was returned, this "flag" is 
						 * guaranteed to have a value
						 */
						if(details.getInternalExternal().equalsIgnoreCase(EXTERNAL_FLAG))
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Querying for EXTERNAL candidate information using phone screen id %1$d", phoneScreenId));
							} // end if
							
							// clear out the inputs
							inputs.clear();
							// setup the query parameters required to execute the second query
							inputs.setSelectorName(SEL_READ_APLCNT_BY_PHN_SCRN_ID);
							inputs.put(STR_NBR_TABNO, TABNO_STR_NBR);
							inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
							inputs.put(DEPT_NBR_TABNO, TABNO_DEPT_NBR);
							inputs.put(LANG_CD, EN_US);
							inputs.put(CURRENT_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
							
							// execute the second query
							hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);							
						} // end if(details.getInternalExternal().equalsIgnoreCase(EXTERNAL_FLAG))
						else
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Querying for INTERNAL candidate information using phone screen id %1$d", phoneScreenId));
							} // end if
							
							// clear out the inputs
							inputs.clear();
							// setup the query parameters required to execute the second query
							inputs.setSelectorName(SEL_READ_ASSOC_BY_PHN_SCRN_ID);
							inputs.put(LANG_CD, EN_US);
							inputs.put(RECORD_STATUS, ACTIVE);
							inputs.put(ADDR_TYP, ADDR_TYP_PRIM);
							inputs.put(STAT_CD, ACTIVE);
							inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
							inputs.put(STR_NBR_TABNO, TABNO_STR_NBR);
							inputs.put(DEPT_NBR_TABNO, TABNO_DEPT_NBR);
							inputs.put(CURRENT_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
							inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
							inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
							inputs.put(ACTV_FLG, true);
							inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
							inputs.addQualifier("effectiveEndDateGreaterThan", true);
							inputs.put("humanResourcesSystemCountryCode", "USA");
							
							// execute the second query
							workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);							
						} // end else
												
						// next go through and replace the time zone key values with the actual descriptions
						String tzKey = details.getTimeZone();
						
						if(tzKey != null)
						{
							tzKey = tzKey.substring(0, 5);
							String timezone = TimeZoneManager.getTimeZone(tzKey);
							// set it on the base record
							details.setTimeZone(timezone);
							// set it on the interview location details if they're present
							if(details.getIntrLocDtls() != null)
							{
								details.getIntrLocDtls().setTimeZone(timezone);
							} // end if(details.getIntrLocDtls() != null)
						} // end if(tzKey != null)
						
						// check the interview status
						if(details.getInterviewStatusCode() != null)
						{
							// TODO : if this is a short, why transport it around as a String in the DTO?
							switch(Short.valueOf(details.getInterviewStatusCode()))
							{
								// if it's store scheduling, have to put the phone number of the store into the response
								case STORE_SCHEDULING:
								{
									if(mLogger.isDebugEnabled())
									{
										mLogger.debug(String.format("Phone screen %1$d is in %2$s status, getting requisition location data", phoneScreenId, details.getInterviewStatusDesc()));
									} // end if
									
									// get the store details
									StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
									/* 
									 * create an interview location details object if one has not been created yet. no null check on the
									 * store object here, if the store could not be found the LocationManager will throw a NoRowsFoundException
									 */
									if(details.getIntrLocDtls() == null)
									{
										details.setIntrLocDtls(new IntrwLocationDetailsTO());
									} // end if(details.getIntrLocDtls() == null)
												
									/*
									 * Set the phone number of the interview location of the phone number of the
									 * store the requisition is for. The IVR for handling inbound phone screens
									 * needs this value set to handle some of it's error flows
									 */
									details.getIntrLocDtls().setPhone(store.getPhone());
									
									if(mLogger.isDebugEnabled())
									{
										mLogger.debug(String.format("Interview phone number for phone screen %1$d set to %2$s", phoneScreenId, store.getPhone()));
									} // end if
									break;
								} // end case STORE_SCHEDULING
								case SCHEDULE_INTERVIEW:
								case UNABLE_TO_SCHEDULE:
								//RSC Automation More IVR Changes
								case PENDING_SCHEDULING:
								case PENDING_SCHEDULE_CALENDAR:
								case PENDING_SCHEDULE_OFFER_INTV:	
								{
									/*
									 * if it's in schedule interview, unable to schedule, Pending Scheduling, Pending Schedule - Offer/Intv, or Pending Schedule - Calendar status, have to add the 
									 * requisition store details to the return object
									 */
									if(mLogger.isDebugEnabled())
									{
										mLogger.debug(String.format("Phone screen %1$d is in %2$s status, getting requisition location data", phoneScreenId, details.getInterviewStatusDesc()));
									} // end if
									
									// get the store details
									StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
									/* 
									 * create an interview location details object if one has not been created yet. no null check on the
									 * store object here, if the store could not be found the LocationManager will throw a NoRowsFoundException
									 */
									IntrwLocationDetailsTO locDetails = new IntrwLocationDetailsTO();
									// populate the requisition location details
									locDetails.setInterviewLocId(store.getStrNum());
									locDetails.setInterviewLocName(store.getStrName());
									locDetails.setAdd(store.getAdd());
									locDetails.setCity(store.getCity());
									locDetails.setState(store.getState());
									locDetails.setZip(store.getZip());
									locDetails.setPhone(store.getPhone());
									locDetails.setTimeZone(details.getTimeZone());
									// set it on the response object
									details.setIntrLocDtls(locDetails);
									break;
								} // end case SCHEDULE_INTERVIEW case UNABLE_TO_SCHEDULE:	
								case INTERVIEW_SCHEDULED:
								{
									/*
									 * if it's in interview scheduled status, get the location details for the 
									 * requisition location and compare the address to the interview address. If
									 * the interview address is different then clear out the timezone.
									 */
									if(mLogger.isDebugEnabled())
									{
										mLogger.debug(String.format("Phone screen %1$d is in %2$s status, getting requisition location data", phoneScreenId, details.getInterviewStatusDesc()));
									} // end if
									
									// make sure the interview location details are not null (should not be)
									if(details.getIntrLocDtls() != null)
									{
										// make sure the interview location address is not null
										if(details.getIntrLocDtls().getAdd() != null)
										{
											// get the requisition location data
											StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
											/*
											 * if the store address is not null, no null check on the store here, if
											 * the store was not found the LocationManager will throw a NoRowsFoundException
											 */
											if(store.getAdd() != null)
											{
												// if the requisition store address does not equal the interview address
												if(!store.getAdd().trim().equals(details.getIntrLocDtls().getAdd().trim()))
												{
													// clear out the timezone
													details.getIntrLocDtls().setTimeZone(null);
												} // end if(!store.getAdd().trim().equals(details.getIntrLocDtls().getAdd().trim()))
											} // end if(store.getAdd() != null)
											else
											{
												// don't know so clear it to be safe
												details.getIntrLocDtls().setTimeZone(null);
											} // end else
										} // end if(details.getIntrLocDtls().getAdd() != null)
										else
										{
											// don't know so clear it to be safe
											details.getIntrLocDtls().setTimeZone(null);
										} // end else											
									} // end if(details.getIntrLocDtls() != null)
									
									break;
								} // end case INTERVIEW_SCHEDULED
								default: {
									//Had to add for Phone Screen as well as part of RSC Automation More IVR Changes Mar 2016.  It they are in one of the Pending Statuses need to give store information as well.
									if(mLogger.isDebugEnabled())
									{
										mLogger.debug(String.format("Phone screen %1$d, getting requisition location data", phoneScreenId));
									} // end if
									
									// get the store details
									StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
									/* 
									 * create an interview location details object if one has not been created yet. no null check on the
									 * store object here, if the store could not be found the LocationManager will throw a NoRowsFoundException
									 */
									IntrwLocationDetailsTO locDetails = new IntrwLocationDetailsTO();
									// populate the requisition location details
									locDetails.setInterviewLocId(store.getStrNum());
									locDetails.setInterviewLocName(store.getStrName());
									locDetails.setAdd(store.getAdd());
									locDetails.setCity(store.getCity());
									locDetails.setState(store.getState());
									locDetails.setZip(store.getZip());
									locDetails.setPhone(store.getPhone());
									locDetails.setTimeZone(details.getTimeZone());
									// set it on the response object
									details.setIntrLocDtls(locDetails);
									break;
								} //end case default								
							} // end switch(Short.valueOf(details.getInterviewStatusCode()))
						} // end if(details.getInterviewStatusCode() != null)

						/*RSC Automation More IVR Changes
						 * check to see if candidates are still needed (# offers < # open) AND (# candidates sent for interviews < # interviews requested) and 
						 * then if there is schedule availability on the Interview Availability calendar associated with the requisition
						 */
						
						//Get Phone Screen Information in order to obtain the requisition number, store number, and calendar Id 
						// clear out the inputs
						inputs.clear();
						inputs.setSelectorName(SEL_READ_HR_PHN_SCRN);										
						inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
						
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Querying for phone screen details using phone screen id %1$d", phoneScreenId));
						} // end if
						
						// execute the query
						hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
							
						mLogger.debug(String.format("Found Requisition %1$s associated with phone screen %2$d", details.getReqNbr(), phoneScreenId));										
							
						//Query to check #Candidates Sent for Interview < #Requested Interviews
						inputs.clear();
						inputs.setSelectorName(SEL_READ_REMAINING_INTRW_CAND_COUNT_BY_STAT_CD);
						inputs.put(EMPLT_REQN_NBR, Integer.parseInt(details.getReqNbr()));
						List<Short> interviewStatCodeList = new ArrayList<Short>();
						interviewStatCodeList.add(INTERVIEW_SCHEDULED);
						interviewStatCodeList.add(STORE_SCHEDULING);
						inputs.put(INTERVIEW_STAT_CD_LIST, interviewStatCodeList);
						inputs.put(ACTV_FLG, true);
						
						// execute the query
						workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
							
						//Get the results for #Candidates Sent for Interview < #Requested Interviews.  When value returned is > zero then condition is met.
						int candidatesSentVsRequestedInterviews = phnScrnHandler.getInterviewsStillNeeded();
						mLogger.debug(String.format("Returned value for candidatesSentVsRequestedInterviews:%1$s", candidatesSentVsRequestedInterviews));
							
						//Query to check #Offers < #Open Positions
						inputs.clear();
						inputs.setSelectorName(SEL_READ_HR_STR_EMPLT_REQN_COUNT);					
						inputs.put(EMPLT_REQN_NBR, Integer.parseInt(details.getReqNbr()));
						inputs.put(CAND_OFFER_MADE_FLG, true);
						inputs.put(CAND_OFFER_STAT_IND, DECLINED_STATUS);
						List<String> activeFlgList = new ArrayList<String>();
						activeFlgList.add("Y");
						activeFlgList.add("H");
						inputs.put(ACTV_FLG_LIST, activeFlgList);					
							
						// execute the query
						workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
							
						//Get the results for #Offers < #Open.  When value returned is > zero then condition is met.
						int candidatesOfferedVsOpen = phnScrnHandler.getOffersStillNeeded();
						mLogger.debug(String.format("Returned value for candidatesOfferedVsOpen:%1$s", candidatesOfferedVsOpen));										
							
						SchdIntrvwChecksTO schdIntrvwChecks =  new SchdIntrvwChecksTO();
						if (candidatesOfferedVsOpen > 0 && candidatesSentVsRequestedInterviews > 0) {
							schdIntrvwChecks.setOffersInterviewsCheck(YES);
						} else {
							schdIntrvwChecks.setOffersInterviewsCheck(NO);
						}
							
						mLogger.debug(String.format("Offers and Interview Check:%1$s", schdIntrvwChecks.getOffersInterviewsCheck()));										
							
						//Get the results of Interview Availability
						Calendar startTime = Calendar.getInstance();
						startTime.setTimeInMillis(System.currentTimeMillis());
						startTime.add(Calendar.HOUR, 18);
						mLogger.debug(String.format("Availability Start Time:%1$s", new java.sql.Timestamp(startTime.getTimeInMillis())));
						Calendar endTime = Calendar.getInstance();
						endTime.setTimeInMillis(System.currentTimeMillis());
						endTime.add(Calendar.DATE, 14);
						mLogger.debug(String.format("Availability End Time:%1$s", new java.sql.Timestamp(endTime.getTimeInMillis())));					
						inputs.clear();
						inputs.setSelectorName(SEL_READ_COUNT_HR_REQN_SCHD_DTLS);
						inputs.put(REQN_CAL_ID, Integer.parseInt(details.getReqCalId()));
						inputs.put(BEGIN_TIMESTAMP, new java.sql.Timestamp(startTime.getTimeInMillis()));
						inputs.put(HR_BEGIN_TIMESTAMP, new java.sql.Timestamp(endTime.getTimeInMillis()));
						inputs.put(REQN_SCHD_STAT_CD, (short) 1);
						inputs.addQualifier(BEGIN_TS_GREATER_THAN_EQUAL_TO, true);
						inputs.addQualifier(HR_BEGIN_TS_LESS_THAN_EQUAL_TO, true);
							
						// execute the query
						workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);					
						
						if (phnScrnHandler.getAvailableInterviewSlots() > 0 || Integer.parseInt(details.getReqCalId()) == 0) {
							schdIntrvwChecks.setCalendarAvailability(YES);
						} else {
							schdIntrvwChecks.setCalendarAvailability(NO);
						}

						mLogger.debug(String.format("Has Interview Availability:%1$s", schdIntrvwChecks.getCalendarAvailability()));	
						details.setSchdIntrvwChecks(schdIntrvwChecks);						
					} // end if(details != null)
					else
					{
						mLogger.error(String.format("No candidate data found using phone screen id %1$d", phoneScreenId));
						throw new QueryException(String.format("No candidate data found using phone screen id %1$d", phoneScreenId));
					} // end else
	            } // end method handleQuery()
			}; // end UniversalConnectionHandler inner class
			
			// execute all the queries with a single connection
			connectionHandler.execute();
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
			mLogger.error(String.format("An exception occurred retrieving candidate details using phone screen id %1$s", phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getPhoneScreenCandidateDetails(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
		
		return handler.getPhnScrnIntrvwDetails();
	} // end function getPhnScrnIntrvwDetails()
	
	
	/**
	 * This method gets phone screen candidate details for the partial candidate id and postal code provided.  This was changed for the CDP Project.
	 * 
	 * @param candidateId					last 6 digits of the candidate identifier.  Kenexa Candidate Ref Number
	 * @param pstlCd						postal code of the candidate
	 * 
	 * @return								Details about the candidate, phone screen, and interview for
	 * 										the partial candidate id and postal code provided
	 * 
	 * @throws IllegalArgumentException		Thrown if the partial candidate id is null, empty, or not 6 digits. Also
	 * 										thrown if the postal code provided is null, empty or not 5 digits.
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database
	 */	
	public static PhoneScreenIntrwDetailsTO getPhoneScreenCandidateDetails(final String candidateId, final String pstlCd) throws IllegalArgumentException, QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getPhoneScreenCandidateDetails(), candidateId: %1$s, pstlCd: %2$s", candidateId, pstlCd));
		} // end if

		// create a handler object that will be used to combine the results of the queries about to be executed into a single object
		PhoneScreenHandler handler = new PhoneScreenHandler();

		try
		{
			// validate the candidate id provided is not null or empty
			if(candidateId == null || candidateId.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s candidate id provided", (candidateId == null ? "Null" : "Empty")));
			} // end if(candidateId == null || candidateId.trim().length() == 0)
			
			// validate the length of the candidate id is 6 AND that all characters in the string are digits
			if(!Pattern.matches(VALID_REGEX_LAST6_CANDID, candidateId))
			{
				throw new IllegalArgumentException(String.format("Invalid candidate id %1$s provided", candidateId));
			} // end if(!Pattern.matches(VALID_REGEX_LAST6_CANDID, candidateId))
			
			// validate the postal code provided is not null or empty
			if(pstlCd == null || pstlCd.trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s zip code provided", (pstlCd == null ? "Null" : "Empty")));
			} // end if(pstlCd == null || pstlCd.trim().length() == 0)
			
			// validate the length of the postal code provided is 5 AND that all characters in the string are digits
			// TODO : THIS WILL HAVE TO BE CHANGED IF WE EVER ALLOW CANADIAN POSTAL CODES
			if(!Pattern.matches(VALID_REGEX_US_ZIP, pstlCd))
			{
				throw new IllegalArgumentException(String.format("Invalid zip code %1$s provided", pstlCd));
			} // end if(!Pattern.matches(VALID_REGEX_US_ZIP, pstlCd))
			
			// get an instance of the DAO query manager
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			//get a database connection from the DAO query manager
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);			
			
			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, handler, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
					
					// cast the handler to the right type
					PhoneScreenHandler phnScrnHandler = (PhoneScreenHandler)handler;

					// initialize the query parameters required to execute the first query
					MapStream inputs = new MapStream(SEL_READ_APLCNT_BY_PRTL_APLCNT_ID_ZIP);

					inputs.put(EMPLT_APLCNT_ID, candidateId);
					inputs.put(ZIP_CODE, pstlCd);
					inputs.put(STAT_CD, TERMINATED);
					inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
					inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
					inputs.put(ACTV_FLG, true);
					inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
					inputs.addQualifier("effectiveEndDateGreaterThan", true);
					inputs.put("humanResourcesSystemCountryCode", "USA");
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Getting candidate id and type using partial candidate id %1$s and postal code %2$s", candidateId, pstlCd));
					} // end if
					
					// execute the query
					workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
					
					// get the object populated by the query handler
					PhoneScreenIntrwDetailsTO details = phnScrnHandler.getPhnScrnIntrvwDetails();
					
					// if a single candidate id was returned for the partial candidate id / postal code combination
					if(details != null)
					{
						// initialize the query parameters required to execute the second query
						inputs.clear();
						inputs.setSelectorName(SEL_READ_REQ_PHN_SCRN);
						inputs.put(EMPLT_POSN_CAND_ID, details.getCndtNbr());
						inputs.put(ACTV_FLG_LIST, ACTIVE_FLAGS);
						
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Getting active requisition for %1$s candidate %2$s", details.getInternalExternal(), details.getCndtNbr()));
						} // end if
						
						// execute the query
						hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
						
						// if the query was successful, then the "ITI Number" (AKA phone screen id will be set on the details object)
						if(details.getItiNbr() != null && details.getItiNbr().trim().length() > 0)
						{
							int phoneScreenId = Integer.parseInt(details.getItiNbr());
							
							/*
							 * Check the internal/external flag on the detail object returned (populated by the first query). If a row was
							 * returned then this "flag" is guaranteed to have a value
							 */
							if(details.getInternalExternal().equalsIgnoreCase(EXTERNAL_FLAG))
							{
								if(mLogger.isDebugEnabled())
								{
									mLogger.debug(String.format("Querying for EXTERNAL candidate information using partial candidate id %1$s and postal code %2$s", candidateId, pstlCd));
								} // end if
								
								// clear out the inputs
								inputs.clear();
								// setup the query parameters required to execute the third query
								inputs.setSelectorName(SEL_READ_APLCNT_BY_PHN_SCRN_ID);
								inputs.put(STR_NBR_TABNO, TABNO_STR_NBR);
								inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
								inputs.put(DEPT_NBR_TABNO, TABNO_DEPT_NBR);
								inputs.put(LANG_CD, EN_US);
								inputs.put(CURRENT_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
								
								// execute the third query
								hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
							} // end if(details.getInternalExternal().equalsIgnoreCase(EXTERNAL_FLAG))
							else
							{
								if(mLogger.isDebugEnabled())
								{
									mLogger.debug(String.format("Querying for INTERNAL candidate information using partial candidate id %1$s and postal code %2$s", candidateId, pstlCd));
								} // end if
								
								// clear out the inputs
								inputs.clear();
								// setup the query parameters required to execute the third query
								inputs.setSelectorName(SEL_READ_ASSOC_BY_PHN_SCRN_ID);
								inputs.put(LANG_CD, EN_US);
								inputs.put(RECORD_STATUS, ACTIVE);
								inputs.put(ADDR_TYP, ADDR_TYP_PRIM);
								inputs.put(STAT_CD, ACTIVE);
								inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
								inputs.put(STR_NBR_TABNO, TABNO_STR_NBR);
								inputs.put(DEPT_NBR_TABNO, TABNO_DEPT_NBR);
								inputs.put(CURRENT_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
								inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
								inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
								inputs.put(ACTV_FLG, true);
								inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
								inputs.addQualifier("effectiveEndDateGreaterThan", true);
								inputs.put("humanResourcesSystemCountryCode", "USA");
								
								// execute the third query
								workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
								
							} // end else
							
							// next go through and replace the timezone key values with the actual descriptions
							String tzKey = details.getTimeZone();
							
							if(tzKey != null)
							{
								tzKey = tzKey.substring(0, 5);
								String timezone = TimeZoneManager.getTimeZone(tzKey);
								// set it on the base record
								details.setTimeZone(timezone);
								// set it on the interview location details if they're present
								if(details.getIntrLocDtls() != null)
								{
									details.getIntrLocDtls().setTimeZone(timezone);
								} // end if(details.getIntrLocDtls() != null)
							} // end if(tzKey != null)		
							
							// check the interview status
							if(details.getInterviewStatusCode() != null)
							{
								// TODO : if this is a short, why transport it around as a String in the DTO?
								switch(Short.valueOf(details.getInterviewStatusCode()))
								{
									// if it's store scheduling, have to put the phone number of the store into the response
									case STORE_SCHEDULING:
									{
										if(mLogger.isDebugEnabled())
										{
											mLogger.debug(String.format("Phone screen %1$d is in %2$s status, getting requisition location data", phoneScreenId, details.getInterviewStatusDesc()));
										} // end if
										
										// get the store details
										StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
										/* 
										 * create an interview location details object if one has not been created yet. no null check on the
										 * store object here, if the store could not be found the LocationManager will throw a NoRowsFoundException
										 */
										if(details.getIntrLocDtls() == null)
										{
											details.setIntrLocDtls(new IntrwLocationDetailsTO());
										} // end if(details.getIntrLocDtls() == null)
													
										/*
										 * Set the phone number of the interview location of the phone number of the
										 * store the requisition is for. The IVR for handling inbound phone screens
										 * needs this value set to handle some of it's error flows
										 */
										details.getIntrLocDtls().setPhone(store.getPhone());
												
										if(mLogger.isDebugEnabled())
										{
											mLogger.debug(String.format("Interview phone number for phone screen %1$d set to %2$s", phoneScreenId, store.getPhone()));
										} // end if
										break;
									} // end case STORE_SCHEDULING		
									case SCHEDULE_INTERVIEW:
									case UNABLE_TO_SCHEDULE:
									//RSC Automation More IVR Changes
									case PENDING_SCHEDULING:
									case PENDING_SCHEDULE_CALENDAR:
									case PENDING_SCHEDULE_OFFER_INTV:	
									{
										/*
										 * if it's in schedule interview, unable to schedule, Pending Scheduling, Pending Schedule - Offer/Intv, or Pending Schedule - Calendar status, have to add the 
										 * requisition store details to the return object
										 */
										if(mLogger.isDebugEnabled())
										{
											mLogger.debug(String.format("Phone screen %1$d is in %2$s status, getting requisition location data", phoneScreenId, details.getInterviewStatusDesc()));
										} // end if
										
										// get the store details
										StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
										/* 
										 * create an interview location details object if one has not been created yet. no null check on the
										 * store object here, if the store could not be found the LocationManager will throw a NoRowsFoundException
										 */
										IntrwLocationDetailsTO locDetails = new IntrwLocationDetailsTO();
										// populate the requisition location details
										locDetails.setInterviewLocId(store.getStrNum());
										locDetails.setInterviewLocName(store.getStrName());
										locDetails.setAdd(store.getAdd());
										locDetails.setCity(store.getCity());
										locDetails.setState(store.getState());
										locDetails.setZip(store.getZip());
										locDetails.setPhone(store.getPhone());
										locDetails.setTimeZone(details.getTimeZone());
										// set it on the response object
										details.setIntrLocDtls(locDetails);
										break;
									} // end case SCHEDULE_INTERVIEW case case UNABLE_TO_SCHEDULE			
									case INTERVIEW_SCHEDULED:
									{
										/*
										 * if it's in interview scheduled status, get the location details for the 
										 * requisition location and compare the address to the interview address. If
										 * the interview address is different then clear out the timezone.
										 */
										if(mLogger.isDebugEnabled())
										{
											mLogger.debug(String.format("Phone screen %1$d is in %2$s status, getting requisition location data", phoneScreenId, details.getInterviewStatusDesc()));
										} // end if
										
										// make sure the interview location details are not null (should not be)
										if(details.getIntrLocDtls() != null)
										{
											// make sure the interview location address is not null
											if(details.getIntrLocDtls().getAdd() != null)
											{
												// get the requisition location data
												StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
												/*
												 * if the store address is not null, no null check on the store here, if
												 * the store was not found the LocationManager will throw a NoRowsFoundException
												 */
												if(store.getAdd() != null)
												{
													// if the requisition store address does not equal the interview address
													if(!store.getAdd().trim().equals(details.getIntrLocDtls().getAdd().trim()))
													{
														// clear out the timezone
														details.getIntrLocDtls().setTimeZone(null);
													} // end if(!store.getAdd().trim().equals(details.getIntrLocDtls().getAdd().trim()))
												} // end if(store.getAdd() != null)
												else
												{
													// don't know so clear it to be safe
													details.getIntrLocDtls().setTimeZone(null);
												} // end else
											} // end if(details.getIntrLocDtls().getAdd() != null)
											else
											{
												// don't know so clear it to be safe
												details.getIntrLocDtls().setTimeZone(null);
											} // end else											
										} // end if(details.getIntrLocDtls() != null)
										
										break;
									} // end case INTERVIEW_SCHEDULED	
									default: {
										//Had to add for Phone Screen as well as part of RSC IVR Enhancements Mar 2016.  It they are in one of the Pending Statuses need to give store information as well.
										if(mLogger.isDebugEnabled())
										{
											mLogger.debug(String.format("Phone screen %1$d, getting requisition location data", phoneScreenId));
										} // end if
										
										// get the store details
										StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(details.getHrgStoreLoc());
										/* 
										 * create an interview location details object if one has not been created yet. no null check on the
										 * store object here, if the store could not be found the LocationManager will throw a NoRowsFoundException
										 */
										IntrwLocationDetailsTO locDetails = new IntrwLocationDetailsTO();
										// populate the requisition location details
										locDetails.setInterviewLocId(store.getStrNum());
										locDetails.setInterviewLocName(store.getStrName());
										locDetails.setAdd(store.getAdd());
										locDetails.setCity(store.getCity());
										locDetails.setState(store.getState());
										locDetails.setZip(store.getZip());
										locDetails.setPhone(store.getPhone());
										locDetails.setTimeZone(details.getTimeZone());
										// set it on the response object
										details.setIntrLocDtls(locDetails);
										break;
									} //end case default
								} // end switch(Short.valueOf(details.getInterviewStatusCode()))
							} // end if(details.getInterviewStatusCode() != null)	

							/*RSC Automation More IVR Changes
							 * check to see if candidates are still needed (# offers < # open) AND (# candidates sent for interviews < # interviews requested) and 
							 * then if there is schedule availability on the Interview Availability calendar associated with the requisition
							 */
							
							//Get Phone Screen Information in order to obtain the requisition number, store number, and calendar Id 
							// clear out the inputs
							inputs.clear();
							inputs.setSelectorName(SEL_READ_HR_PHN_SCRN);										
							inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
							
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Querying for phone screen details using phone screen id %1$d", phoneScreenId));
							} // end if
							
							// execute the query
							hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
							
							mLogger.debug(String.format("Found Requisition %1$s associated with phone screen %2$d", details.getReqNbr(), phoneScreenId));										
							
							//Query to check #Candidates Sent for Interview < #Requested Interviews
							inputs.clear();
							inputs.setSelectorName(SEL_READ_REMAINING_INTRW_CAND_COUNT_BY_STAT_CD);
							inputs.put(EMPLT_REQN_NBR, Integer.parseInt(details.getReqNbr()));
							List<Short> interviewStatCodeList = new ArrayList<Short>();
							interviewStatCodeList.add(INTERVIEW_SCHEDULED);
							interviewStatCodeList.add(STORE_SCHEDULING);
							inputs.put(INTERVIEW_STAT_CD_LIST, interviewStatCodeList);
							inputs.put(ACTV_FLG, true);
							
							// execute the query
							workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
							
							//Get the results for #Candidates Sent for Interview < #Requested Interviews.  When value returned is > zero then condition is met.
							int candidatesSentVsRequestedInterviews = phnScrnHandler.getInterviewsStillNeeded();
							mLogger.debug(String.format("Returned value for candidatesSentVsRequestedInterviews:%1$s", candidatesSentVsRequestedInterviews));
							
							//Query to check #Offers < #Open Positions
							inputs.clear();
							inputs.setSelectorName(SEL_READ_HR_STR_EMPLT_REQN_COUNT);					
							inputs.put(EMPLT_REQN_NBR, Integer.parseInt(details.getReqNbr()));
							inputs.put(CAND_OFFER_MADE_FLG, true);
							inputs.put(CAND_OFFER_STAT_IND, DECLINED_STATUS);
							List<String> activeFlgList = new ArrayList<String>();
							activeFlgList.add("Y");
							activeFlgList.add("H");
							inputs.put(ACTV_FLG_LIST, activeFlgList);					
							
							// execute the query
							workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
							
							//Get the results for #Offers < #Open.  When value returned is > zero then condition is met.
							int candidatesOfferedVsOpen = phnScrnHandler.getOffersStillNeeded();
							mLogger.debug(String.format("Returned value for candidatesOfferedVsOpen:%1$s", candidatesOfferedVsOpen));										
							
							SchdIntrvwChecksTO schdIntrvwChecks =  new SchdIntrvwChecksTO();
							if (candidatesOfferedVsOpen > 0 && candidatesSentVsRequestedInterviews > 0) {
								schdIntrvwChecks.setOffersInterviewsCheck(YES);
							} else {
								schdIntrvwChecks.setOffersInterviewsCheck(NO);
							}
							
							mLogger.debug(String.format("Offers and Interview Check:%1$s", schdIntrvwChecks.getOffersInterviewsCheck()));										
							
							//Get the results of Interview Availability
							Calendar startTime = Calendar.getInstance();
							startTime.setTimeInMillis(System.currentTimeMillis());
							startTime.add(Calendar.HOUR, 18);
							mLogger.debug(String.format("Availability Start Time:%1$s", new java.sql.Timestamp(startTime.getTimeInMillis())));
							Calendar endTime = Calendar.getInstance();
							endTime.setTimeInMillis(System.currentTimeMillis());
							endTime.add(Calendar.DATE, 14);
							mLogger.debug(String.format("Availability End Time:%1$s", new java.sql.Timestamp(endTime.getTimeInMillis())));					
							inputs.clear();
							inputs.setSelectorName(SEL_READ_COUNT_HR_REQN_SCHD_DTLS);
							inputs.put(REQN_CAL_ID, Integer.parseInt(details.getReqCalId()));
							inputs.put(BEGIN_TIMESTAMP, new java.sql.Timestamp(startTime.getTimeInMillis()));
							inputs.put(HR_BEGIN_TIMESTAMP, new java.sql.Timestamp(endTime.getTimeInMillis()));
							inputs.put(REQN_SCHD_STAT_CD, (short) 1);
							inputs.addQualifier(BEGIN_TS_GREATER_THAN_EQUAL_TO, true);
							inputs.addQualifier(HR_BEGIN_TS_LESS_THAN_EQUAL_TO, true);
							
							// execute the query
							workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);					
							
							if (phnScrnHandler.getAvailableInterviewSlots() > 0 || Integer.parseInt(details.getReqCalId()) == 0) {
								schdIntrvwChecks.setCalendarAvailability(YES);
							} else {
								schdIntrvwChecks.setCalendarAvailability(NO);
							}

							mLogger.debug(String.format("Has Interview Availability:%1$s", schdIntrvwChecks.getCalendarAvailability()));	
							details.setSchdIntrvwChecks(schdIntrvwChecks);
							
						} // end if(details.getItiNbr() != null && details.getItiNbr().trim().length() > 0)
						else
						{
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("No active phone screens found using partial candidate id %1$s and zip code %2$s, querying for candidate status data",
									candidateId, pstlCd));
							} // end if
							
							// clear out the inputs
							inputs.clear();
							// setup to run the next query
							inputs.setSelectorName(SEL_READ_EMPLT_POSN_CAND_BY_CAND_ID);
							inputs.put(EMPLT_POSN_CAND_ID, details.getCndtNbr());
							
							// execute the query
							hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
							
							if(mLogger.isDebugEnabled())
							{
								mLogger.debug(String.format("Candidate with partial candidate id %1$s and zip code %2$s has applicant status %3$s and requisition status %4$s",
									candidateId, pstlCd, details.getCanStatus(), details.getReqStat()));
							} // end if
						} // end else
					} // end if(details != null)
					else
					{
						throw new QueryException(String.format("No candidate data found using partial candidate id %1$s and zip code %2$s", candidateId, pstlCd));	
					} // end else
                } // end function handleQuery()
			}; // end UniversalConnectionHandler anonymous inner class
			
			// execute all the queries with a single connection
			connectionHandler.execute();
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
			mLogger.error(String.format("An exception occurred retrieving candidate details using partial candidate id %1$s and zip code %2$s", candidateId, pstlCd), qe);
			// throw the error
			throw qe;
		} // end catch
				
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getPhoneScreenCandidateDetails(), candidateId: %1$s, pstlCd: %2$s", candidateId, pstlCd));
		} // end if
		
		return handler.getPhnScrnIntrvwDetails();
	} // end function getPhoneScreenCandidateDetails()
	
	/**
	 * Get the phone screen knock out questions for job title of the requisition the phone screen id provided is
	 * attached to
	 * 
	 * @param phoneScreenId					phone screen identifier
	 * @param includeJobPreviewQuests		true if job preview questions should be returned, false otherwise
	 * 
	 * @return								List of questions for the job title of the requisition the phone screen id provided is attached to
	 * 
	 * @throws IllegalArgumentException		Thrown if the phone screen id provided is < 1
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database
	 */
	public static List<String> getPhoneScreenKnockOutQuest(final int phoneScreenId, boolean includeJobPreviewQuests) throws IllegalArgumentException, QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getPhoneScreenKnockOutQuest(), phoneScreenId: %1$d, includeJobPreviewQuests: %2$b", phoneScreenId, includeJobPreviewQuests));
		} // end if
		
		// create a handler object that will be used to process the results of the queries about to be executed
		PhoneScreenHandler handler = new PhoneScreenHandler();
		
		// create a collection to store the question id's
		List<String> questions = new ArrayList<String>();

		try
		{
			// validate the phone screen id provided is > 0
			if(phoneScreenId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phoneScreenId));
			} // end if(phoneScreenId < 1)
			
			// get an instance of the QueryManager
			QueryManager queryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			// get the DAO connection from the QueryManager
			DAOConnection connection = queryManager.getDAOConnection(HRSTAFFING_DAO_CONTRACT);
			
			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, handler, connection)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					// get the DAO connection from the connection list
					DAOConnection connection = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					// get the DAO query object from the connection
					Query query = connection.getQuery();
					
					// cast the handler to the right type
					PhoneScreenHandler phnScrnHandler = (PhoneScreenHandler)handler;
					
					// initialize the query parameters required to execute the first query
					MapStream inputs = new MapStream(SEL_READ_HR_PHN_SCRN);
					inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Executing query to get phone screen data for phone screen id %1$d", phoneScreenId));
					} // end if					
					// execute the first query
					query.getResult(connection, phnScrnHandler, inputs);
					
					// get the results of the first query
					PhoneScreenIntrwDetailsTO details = phnScrnHandler.getPhnScrnIntrvwDetails();
					
					// make sure we found the phone screen
					if(details == null)
					{
						throw new QueryException(String.format("Details for phone screen %1$d not found", phoneScreenId));
					} // end if(details == null)
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("%1$s candidate found for phone screen id %2$d", details.getInternalExternal(), phoneScreenId));
					} // end if
					
					// if the first query returned results AND this is an external candidate, query to get knockout questions
					if(details.getInternalExternal().equals(EXTERNAL_FLAG))
					{
						// clear out the inputs
						inputs.clear();
						// initialize the query parameters required to execute the second query
						inputs.setSelectorName(SEL_READ_HR_PHN_SCRN_INTRW_DTLS);
						inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
						inputs.put(TABNO, TABNO_DEPT_NBR);
						
						if(mLogger.isDebugEnabled())
						{
							mLogger.debug(String.format("Executing query to get job skill type details for phone screen id %1$d", phoneScreenId));
						} // end if
						// execute the second query
						query.getResult(connection, phnScrnHandler, inputs);
						
						// make sure we got the job skill
						if(details != null && (details.getSkillType() != null && details.getSkillType().trim().length() > 0))
						{
							try
							{
								// clear out the inputs
								inputs.clear();
								// setup the query parameters required to execute the third query
								inputs.setSelectorName(SEL_READ_EMPLT_MOD_QST_BY_EMPLT_ID);
								inputs.put(EMPLT_TST_ID, Short.valueOf(details.getSkillType()));
								if(mLogger.isDebugEnabled())
								{
									mLogger.debug(String.format("Executing query to get knockout questions for phone screen id %1$d having a job skill of %2$s", 
										phoneScreenId, details.getSkillType()));
								} // end if
								
								// execute the third query
								query.getResult(connection, phnScrnHandler, inputs);
							} // end try
							catch(NumberFormatException nfe)
							{
								/*
								 * Since the skill type value is stored in a VARCHAR field, there is no guarantee it will be numeric.
								 * In lower life cycles the data is actually not always numeric. So we'll wrap this call in a try/catch
								 * block just in case.
								 */
								throw new QueryException(String.format("Non-numeric job skill type code returned for phone screen %1$d", phoneScreenId));
							} // end catch							
						} // end if(details != null && (details.getSkillType() != null && details.getSkillType().trim().length() > 0))
						else
						{
							mLogger.debug(String.format("No job skill type details found for phone screen %1$d", phoneScreenId));
						} // end if
					} // end if(details.getInternalExternal().equals(EXTERNAL_FLAG))
	            } // end method handleQuery()
			}; // end UniversalConnectionHandler inner class
			
			// execute all the queries with a single connection
			connectionHandler.execute();
			
			// if preview questions should be included in the response
			if(includeJobPreviewQuests)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Getting job preview questions for phone screen id %1$d", phoneScreenId));
				} // end if
				// now let's get all the job skill questions
				List<String> previewQuestions = getPhoneScreenJobPreviewQuests();
				
				// if preview questions were found, add them to the response list in the order they were received
				if(previewQuestions != null && previewQuestions.size() > 0)
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Found job preview questions for phone screen id %1$d", phoneScreenId));
					} // end if
					
					// add them all to the response list
					questions.addAll(previewQuestions);
				} // end if(previewQuestions != null && previewQuestions.size() > 0)				
			} // end if(includeJobPreviewQuests)
							
			// next if knockout questions were found
			if(handler.getPhnScrnKnockOutQuestions() != null && handler.getPhnScrnKnockOutQuestions().size() > 0)
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Found job knockout questions for phone screen id %1$d", phoneScreenId));
				} // end if

				/*
				 * Sort the phone screen knock-out question ids. These id's are
				 * numbera_numberb so using default string sorting will put them in
				 * ascending order
				 */
				Collections.sort(handler.getPhnScrnKnockOutQuestions());
				
				// now add all of the knock-out questions to the response list
				questions.addAll(handler.getPhnScrnKnockOutQuestions());
			} // end if(handler.getPhnScrnKnockOutQuestions() != null && handler.getPhnScrnKnockOutQuestions().size() > 0)
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
			mLogger.error(String.format("An exception occurred retrieving phone screen knock-out questions using phone screen id %1$s", phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getPhoneScreenKnockOutQuest(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
	
		return questions;
	} // end function getPhoneScreenKnockOutQuest()	

	/**
	 * This method gets the phone screen job preview question id's from the HR org parameter
	 * table
	 * 
	 * @return						List of job preview question id's or null if no job preview
	 * 								questions exist
	 * 
	 * @throws QueryException		Thrown if an exception occurs looking up the org parameter 
	 * 								containing the job preview questions
	 */
	private static List<String> getPhoneScreenJobPreviewQuests() throws QueryException
	{
		mLogger.debug("Entering getPhoneScreenJobPreviewQuests()");
		List<String> questions = null;

		try
		{
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Getting org parameter named %1$s", PREV_QUEST_PARAM_ID));
			} // end if
			
			// get the phone screen job preview questions parameter (, delimited list of id's if it exists)
			OrgParamTO parameter = OrgParamManager.getOrgParam(PREV_QUEST_SUBSYS_CD, PREV_QUEST_BU, PREV_QUEST_PARAM_ID, PREV_QUEST_PRCSS_TYP_IND, true);

			// if the parameter came back
			if(parameter != null && (parameter.getCharVal() != null && parameter.getCharVal().trim().length() > 0))
			{
				// initialize the collection
				questions = new ArrayList<String>();
				
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Found org parameter named %1$s with value %2$s", PREV_QUEST_PARAM_ID, parameter.getCharVal()));
				} // end if
				
				// now separate the value into unique question id's using the delimiter
				StringTokenizer tokenizer = new StringTokenizer(parameter.getCharVal(), PREV_QUEST_PARM_DELIMITER);
				
				// iterate the tokens and add them to the list to be returned
				while(tokenizer.hasMoreTokens())
				{
					questions.add(tokenizer.nextToken());
				} // end while(tokenizer.hasMoreTokens())
				
				if(mLogger.isDebugEnabled())
				{
					for(String question : questions)
					{
						mLogger.debug(String.format("Found job preview question %1$s", question));
					} // end for(String question : questions)
				} // end if
			} // end if(parameter != null && (parameter.getCharVal() != null && parameter.getCharVal().trim().length() > 0))
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
			mLogger.error("An exception occurred getting phone screen job preview questions", qe);
			// throw the error
			throw qe;
		} // end catch
		
		mLogger.debug("Exiting getPhoneScreenJobPreviewQuests()");
		return questions;
	} // end function getPhoneScreenJobPreviewQuests()
	
	
	/**
	 * This method gets a list of scheduled interview blocks for the phone screen id provided
	 * 
	 * @param phoneScreenId the phone screen to get scheduled blocks for
	 * 
	 * @return List of RequsitionScheduleTO objects
	 * 
	 * @throws ValidationException thrown if the phone screen id provided is &lt;= 0
	 * 
	 * @throws QueryException thrown if an exception occurs querying the database
	 */
	public static List<RequisitionScheduleTO> getScheduledInterviewBlocksForPhoneScreen(final int phoneScreenId) throws ValidationException, QueryException 
	{
		long startTime = 0;
		// declared final so it can be accessed by the anonymous inner class used to query the database
		final List<RequisitionScheduleTO> scheduledBlocks = new ArrayList<RequisitionScheduleTO>();
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getScheduledInterviewBlocksForPhoneScreen(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
		
		try
		{
			// validate the phone screen id provided
			ValidationUtils.validatePhoneScreenId(phoneScreenId);
			
			// prepare the query
			MapStream inputs = new MapStream(READ_SCHEDULED_BLOCKS_FOR_PHONE_SCREEN);
			inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Querying for scheduled interview blocks for phone screen %1$d", phoneScreenId));
			} // end if
			
			// execute the query
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
				 */
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					RequisitionScheduleTO schedule = null;
					
					// iterate the records returned
					while(results.next())
					{
						schedule = new RequisitionScheduleTO();
						schedule.setCalendarId(results.getInt(REQN_CAL_ID));
						schedule.setBeginTimestamp(results.getTimestamp(BGN_TS));
						schedule.setInterviewerAvailabilityCount(results.getShort(SEQ_NBR));
						schedule.setCreateTimestamp(results.getTimestamp(CRT_TS));
						schedule.setCreateSystemUserId(StringUtils.trim(results.getString(CRT_SYSUSR_ID)));
						schedule.setLastUpdateSystemUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID)));
						schedule.setLastUpdateTimestamp(results.getTimestamp(LAST_UPD_TS));
						schedule.setHumanResourcesSystemStoreNumber(StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
						
						// add it to the collection
						scheduledBlocks.add(schedule);
					} // end while(results.next())
				} // end function readResults()
			}); // end call BasicDAO.getResult()
			
		} // end try
		catch(ValidationException ve)
		{
			// log the exception
			mLogger.error(ve.getMessage(), ve);
			// throw the exception
			throw ve;
		} // end catch
		catch(QueryException qe)
		{
			// log the exception
			mLogger.error(new ClientApplLogger(String.format("An exception occurred getting scheduled interview blocks for phone screen %1$d", phoneScreenId)), qe);
			// throw the exception
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getScheduledInterviewBlocksForPhoneScreen(), phoneScreenId: %1$d. Returning %2$d scheduled interview slots in %3$.9f seconds",
				phoneScreenId, scheduledBlocks.size(), ((((double)endTime - startTime) / NANOS_IN_SECOND))));
		} // end if
		
		return scheduledBlocks;
	} // end function getScheduledInterviewBlocksForPhoneScreen()
	
	/**
	 * Description : This method will set the setpomRsaStatCrsRefResp success message and format in required (XML/JSON).
	 *
	 * @param pomRsaStatCrsRefResp 
	 * 			- the pom rsa stat crs ref resp
	 * @param mediaType 
	 * 			- the media type
	 * @return 
	 * 			- the string
	 * 
	 * Added as part of Flex to HTML Conversion - 13 May 2015
	 */
	public static String setpomRsaStatCrsRefResp(POMRsaStatusCrossRefResponse pomRsaStatCrsRefResp,String mediaType)
	{
		String response = null;
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Enter PhoneScreenManager - setpomRsaStatCrsRefResp( )" + "mediaType: %1$s", mediaType));
		}
		if(pomRsaStatCrsRefResp != null)
		{
			pomRsaStatCrsRefResp.setStatus("true");
			pomRsaStatCrsRefResp.setPomCompleteStatusCode(pomRsaStatCrsRefResp.getPomCompleteStatusCode());
			pomRsaStatCrsRefResp.setInterviewRespondStatusCode(pomRsaStatCrsRefResp.getInterviewRespondStatusCode());
			pomRsaStatCrsRefResp.setRsaUpdateFlag(pomRsaStatCrsRefResp.getRsaUpdateFlag());
			
			/** Get required Format (XML /JSON)**/
			response =Util.getRequiredFormatRes(mediaType,pomRsaStatCrsRefResp);
		}
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug("Exit PhoneScreenManager - setpomRsaStatCrsRefResp( )");
		}

		return response;
	}
	//Added for FMS 7894 IVR Changes
	/**
	 * Get the phone screen requisition specific knock out questions for the requisition the phone screen id provided is
	 * attached to
	 * 
	 * @param phoneScreenId					phone screen identifier
	 * @param requisitionNbr				requisition number the phone screen is attached to
	 * 
	 * @return								List of questions for the job title of the requisition the phone screen id provided is attached to
	 * 
	 * @throws IllegalArgumentException		Thrown if the phone screen id provided is < 1
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database
	 */
	public static PhoneScreenReqnSpecKODetailResponse getPhoneScreenReqSpecKnockOutDetail(final int phoneScreenId) throws IllegalArgumentException, QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getPhoneScreenReqSpecKnockOutDetail(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
		
		// create a handler object that will be used to process the results of the queries about to be executed
		PhoneScreenHandler handler = new PhoneScreenHandler();
		
		// create a collection to store the question id's
		final List<PhoneScreenReqnSpecKODetailResponse> theResponse = new ArrayList<PhoneScreenReqnSpecKODetailResponse>();
		final PhoneScreenReqnSpecKODetailResponse responseRecord = new PhoneScreenReqnSpecKODetailResponse();
		
		try
		{
			// validate the phone screen id provided is > 0
			if(phoneScreenId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phoneScreenId));
			} // end if(phoneScreenId < 1)
			
			// get an instance of the DAO query manager
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			//get a database connection from the DAO query manager
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);	
			
			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, handler, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
					
					// cast the handler to the right type
					PhoneScreenHandler phnScrnHandler = (PhoneScreenHandler)handler;
					
					// initialize the query parameters required to execute the first query
					
					//Get Phone Screen Information in order to obtain the requisition number and store number
					MapStream inputs = new MapStream(SEL_READ_HR_PHN_SCRN);
					inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying for phone screen details using phone screen id %1$d", phoneScreenId));
					} // end if
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
					
					// get the object populated by the query handler
					PhoneScreenIntrwDetailsTO phnScrnDetails = phnScrnHandler.getPhnScrnIntrvwDetails();					
					
					// make sure we found the phone screen details
					if(phnScrnDetails == null)
					{
						throw new QueryException(String.format("Details for phone screen %1$d not found", phoneScreenId));
					} // end if(phnScrnDetails == null)
					
					mLogger.debug(String.format("Found Requisition %1$s associated with phone screen %2$d", phnScrnDetails.getReqNbr(), phoneScreenId));
					
					//Getting 1st set of Requisition Details
					inputs.clear();
					inputs.setSelectorName(SEL_READ_REQN_DETAILS);
					inputs.put(EMPLT_REQN_NBR, Integer.parseInt(phnScrnDetails.getReqNbr()));
					inputs.put(TABNO, TABNO_DEPT_NO);
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);					
					
					//get the object populated by the query handler
					RequisitionDetailTO reqnDetails = phnScrnHandler.getRequisitionDetails();
					
					if (reqnDetails == null) {
						throw new QueryException(String.format("Details for requisition %1$d not found", phnScrnDetails.getReqNbr()));						
					}

					//Set all the default Question ID's and Questions selected to N
					responseRecord.setQuestionAndIdDefaults();
					
					responseRecord.setPositionTitle(StringUtils.trim(reqnDetails.getJobTtl()));
					responseRecord.setStoreLocation(StringUtils.trim(reqnDetails.getStore()));
					if (reqnDetails.getPt().equals(YES)) {
						responseRecord.setEmploymentCategory("partTime");
					} else if (reqnDetails.getFt().equals(YES)) {
						responseRecord.setEmploymentCategory("fullTime");
					}
					
					//Getting 2nd set of Requisition Details
					inputs.clear();
					inputs.setSelectorName(SEL_READ_REQN_STAFFING_DETAILS);
					inputs.put(EMPLT_REQN_NBR, Integer.parseInt(phnScrnDetails.getReqNbr()));
					
					// execute the query
					workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);										

					if (reqnDetails.getHourlyRate() != null) {
						responseRecord.setHourlyRate(StringUtils.trim(reqnDetails.getHourlyRate()));
					} else {
						responseRecord.setHourlyRate("");
					}
					
					//See if position is a Driving Position
					inputs.clear();
					inputs.setSelectorName(SEL_READ_BGC_JOB_REQUIREMENTS);
					inputs.put(HR_SYS_STR_NBR, reqnDetails.getStore());
					inputs.put(BASE_STORE_GROUP_FLG, true);
					inputs.put(JOB_TTL_CD, reqnDetails.getJob());
					inputs.put(HR_SYS_DEPT_NBR, reqnDetails.getDept());
					inputs.put(EFF_DATE, new Date(System.currentTimeMillis()));
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
					
					//When position is a driving position, check to see if store requires DL
					if (reqnDetails.isDrivingPosition()) {
						//This map is cached for 4 hours
						if (mStoreDMVExemptCache == null || mStoreDMVExemptCache.isEmpty()) {
							mLogger.debug("DMV Store Exempt values are NOT cached, running query to get them.");
							inputs.clear();
							inputs.setSelectorName(SEL_READ_HR_LOC_DMV_EXEMPT);
							inputs.addQualifier(EFF_BEGIN_DT_LESS_THAN_EQUAL_TO, true);
							inputs.addQualifier(EFF_END_DT_GREATER_THAN, true);
							
							// execute the query
							workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);	
							
							//Get the map created by the query handler
							if (!phnScrnHandler.getExemptDMVStores().isEmpty()) {
								mStoreDMVExemptCache.putAll(phnScrnHandler.getExemptDMVStores());
							} else {
								throw new QueryException("DMV Store Exempt Details not found");
							}
						} else {
							mLogger.debug("DMV Store Exempt values are cached, using the cached object.");
						}
						//Set if job requires a DL
						if (mStoreDMVExemptCache.containsKey(reqnDetails.getStore())) {
							mLogger.debug(String.format("Store %1$s does not require a DL.", reqnDetails.getStore()));
							responseRecord.setRequiresDriversLicense(NO);
						} else {
							mLogger.debug(String.format("Store %1$s does require a DL.", reqnDetails.getStore()));
							responseRecord.setRequiresDriversLicense(YES);							
						}
						//Set Reliable Transportation because this is a Driving Position
						responseRecord.setReliableTransportation(YES);
						//Set Driving Position
						responseRecord.setTravelingPosition(YES);
					} else {
						responseRecord.setTravelingPosition(NO);
					}
										
					//Getting Requisition Availability
					inputs.clear();
					inputs.setSelectorName(SEL_READ_HR_REQN_DAILY_AVAIL);
					inputs.put(EMPLT_REQN_NBR, Integer.parseInt(phnScrnDetails.getReqNbr()));
					
					// execute the query
					workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
										
					// get the results of the query
					PhoneScreenReqSpecMinReqTO availResults = phnScrnHandler.getPhoneScreenReqSpecMinReq();
					
					if (availResults.getBypassDateTime() != null) {
						responseRecord.setBypassDateTime(availResults.getBypassDateTime());
					}
					
					//There is Requisition Availability, so set it
					if (responseRecord.getBypassDateTime().equals(NO)) {
						//Weekdays
						if (availResults.getWeekdaysSelected() != null) {
							responseRecord.setWeekdaysSelected(availResults.getWeekdaysSelected());
						}
						//Saturdays
						if (availResults.getSaturdaysSelected() != null) {
							responseRecord.setSaturdaysSelected(availResults.getSaturdaysSelected());
						}
						//Sundays
						if (availResults.getSundaysSelected() != null) {
							responseRecord.setSundaysSelected(availResults.getSundaysSelected());
						}
						//Early AM
						if (availResults.getEarlyMorningSelected() != null) {
							responseRecord.setEarlyMorningSelected(availResults.getEarlyMorningSelected());
						}					
						//Mornings
						if (availResults.getMorningSelected() != null) {
							responseRecord.setMorningSelected(availResults.getMorningSelected());
						}										
						//Afternoon
						if (availResults.getAfternoonSelected() != null) {
							responseRecord.setAfternoonSelected(availResults.getAfternoonSelected());
						}
						//Nights
						if (availResults.getNightSelected() != null) {
							responseRecord.setNightSelected(availResults.getNightSelected());
						}
						//Late Nights
						if (availResults.getLateNightSelected() != null) {
							responseRecord.setLateNightSelected(availResults.getLateNightSelected());
						}
						//Overnight
						if (availResults.getOvernightSelected() != null) {
							responseRecord.setOvernightSelected(availResults.getOvernightSelected());
						}					
					} // end if (responseRecord.getBypassDateTime().equals(NO)) {
					
					theResponse.add(responseRecord);
	            } // end method handleQuery()
			}; // end UniversalConnectionHandler inner class
			
			// execute all the queries with a single connection
			connectionHandler.execute();
						
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
			mLogger.error(String.format("An exception occurred retrieving phone screen requisition specific knock-out questions using phone screen id %1$s", phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getPhoneScreenReqSpecKnockOutDetail(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
	
		return theResponse.get(0);
	} // end function getPhoneScreenReqSpecKnockOutDetail()	
	
	//Added for FMS 7894 IVR Changes
	/**
	 * The method will be used for updating PhoneScreen Requisition Specific details in the DB.
	 * 
	 * @param request
	 *            - The object contains the phoneScreen Minimum Requirement
	 *            details
	 *            
	 * @return status - The String value PASS/FAIL
	 * @throws RetailStaffingException
	 */

	public static String updateReqnSpecKOQuestionsForCTI(UpdatePhoneScreenReqnSpecKnockOutRequest request) throws IllegalArgumentException, QueryException, RetailStaffingException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering updateReqnSpecKOQuestionsForCTI(), request: %1$s", request.toString()));
		} // end if
		
		boolean passed = true;
		int phnScrnId;
		
		try
		{
			// validate a phone screen id was provided			
			if(request.getPhoneScreenId() == null || request.getPhoneScreenId().trim().length() == 0)
			{
				throw new IllegalArgumentException(String.format("%1$s phone screen id provided", (request.getPhoneScreenId() == null ? "null" : "empty")));
			} // end if(request.getPhoneScreenId() == null || request.getPhoneScreenId().trim().length() == 0)
			
			// convert the phone screen id to an int since that's what's needed for the update
			try
			{
				phnScrnId = Integer.parseInt(request.getPhoneScreenId());
			} // end try
			catch(NumberFormatException nfe)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$s provided", request.getPhoneScreenId()));
			} // end catch
			
			// validate the phone screen id is > 0
			if(phnScrnId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phnScrnId));
			} // end if(phnScrnId < 1)
			
			// validate we received at least one question to insert
			if (request.getAcceptsPosition() == null)
			{
				throw new IllegalArgumentException(String.format("%1$s phone screen question response list provided for phone screen %2$d", 
					(request.getAcceptsPosition() == null ? "Null" : "Empty"), phnScrnId));
			} // end if (request.getAcceptsPosition() == null)
			
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Phone screen %1$s provided, processing knockout questions", phnScrnId));
			} // end if
									
			//Sort questionMap by Key which is ID
			Map<Integer, String> sortedQuestionMap = new TreeMap<Integer, String>();
	        sortedQuestionMap.putAll(request.createMapOfQuestions());
			        
			mLogger.debug("Number of questions mapped/answered:" + sortedQuestionMap.size());
					
			// iterate the sortedQuestionMap and convert them into a list of objects required for the insert
			List<MinimumResponseTO> minResList = new ArrayList<MinimumResponseTO>();
					
			for (Map.Entry<Integer, String> entry : sortedQuestionMap.entrySet()) {
				MinimumResponseTO obj = new MinimumResponseTO();
				obj.setSeqNbr(entry.getKey().toString());
				obj.setMinimumStatus(entry.getValue());
				minResList.add(obj);
				mLogger.debug(String.format("Setting Sequence:%1$d to Answer:%2$s", entry.getKey(), entry.getValue()));
			}
					
			passed = request.determinePassFail();
			mLogger.debug(String.format("Candidate Requisition Specific KO Question Final Result:%1$b for phone screen %2$s", passed, phnScrnId));

			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Inserting phone screen knockout question responses for phone screen %1$d", phnScrnId));
			} // end if
			
			// insert the phone screen knockout question responses			
			if(updateMinimumReqsForPhoneScreen(phnScrnId, minResList))
			{
				if(mLogger.isDebugEnabled())
				{
					mLogger.debug(String.format("Successfully inserted phone screen knockout question responses for phone screen %1$d", phnScrnId));
				} // end if
				
				// if the user answered NO to any questions, also switch the status of the phone screen to do not proceed
				if(!passed)
				{
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Switching status of phone screen %1$d to \"Do Not Proceed\"", phnScrnId));
					} // end if
					
					updatePhoneScreenNotes(phnScrnId, DETAILS_TXT ,"" );
					updateHumanResourcesMinimumRequirementStatusCode(phnScrnId, IVR_USER, DO_NOT_PROCEED_STAT);
				} // end if(!passed) 
				else
				{
					updateHumanResourcesMinimumRequirementStatusCode(phnScrnId, IVR_USER, PROCEED_STAT);
				} // end - Else
			} // end if(updateMinimumReqsForPhoneScreen(phnScrnId, responses)) 
		} // end try
		catch(IllegalArgumentException iae)
		{
			// log the exception
			mLogger.error(String.format("Illegal argument provided: %1$s", iae.getMessage()), iae);
			// throw the exception
			throw iae;
		} // end catch
		catch(RetailStaffingException re)
		{
			// log the exception
			mLogger.error("An exception occurred inserting phone screen knockout question responses", re);
			// throw the exception
			throw re;
		} // end catch
		
		if (passed) {
			return "PASS";
		} else {
			return "FAIL";
		}
	} // end function updateReqnSpecKOQuestionsForCTI()
	
	//Added for FMS 7894 IVR Changes
	/**
	 * Get the phone screen requisition specific knock out question response for the requisition the phone screen id provided is
	 * attached to
	 * 
	 * @param phoneScreenId					phone screen identifier
	 * 
	 * @return								Parameters that will allow the IVR to determine how to proceed with a Phone Screen
	 * 
	 * @throws IllegalArgumentException		Thrown if the phone screen id provided is < 1
	 * 
	 * @throws QueryException				Thrown if an exception occurs querying the database
	 */
	public static UpdatePhoneScreenReqnSpecKnockOutResponse getPhoneScreenReqSpecKnockOutResponse(final int phoneScreenId) throws IllegalArgumentException, QueryException
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getPhoneScreenReqSpecKnockOutResponse(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
		
		// create a handler object that will be used to process the results of the queries about to be executed
		PhoneScreenHandler handler = new PhoneScreenHandler();
		
		// create a collection to store the question id's
		final List<UpdatePhoneScreenReqnSpecKnockOutResponse> theResponse = new ArrayList<UpdatePhoneScreenReqnSpecKnockOutResponse>();
		final UpdatePhoneScreenReqnSpecKnockOutResponse responseRecord = new UpdatePhoneScreenReqnSpecKnockOutResponse();
		
		try
		{
			// validate the phone screen id provided is > 0
			if(phoneScreenId < 1)
			{
				throw new IllegalArgumentException(String.format("Invalid phone screen id %1$d provided", phoneScreenId));
			} // end if(phoneScreenId < 1)
			
			// get an instance of the DAO query manager
			QueryManager workforceRecruitmentQueryManager = QueryManager.getInstance(WORKFORCERECRUITMENT_DAO_CONTRACT);
			QueryManager hrHrStaffingQueryManager = QueryManager.getInstance(HRSTAFFING_DAO_CONTRACT);
			
			//get a database connection from the DAO query manager
			DAOConnection workforceRecruitmentDaoConn = workforceRecruitmentQueryManager.getDAOConnection(WORKFORCERECRUITMENT_DAO_CONTRACT);
			DAOConnection hrHrStaffingDaoConn = workforceRecruitmentDaoConn.shareControl(HRSTAFFING_DAO_CONTRACT, hrHrStaffingQueryManager);	
			
			// create a new UniversalConnectionHandler that will be used to execute all the queries needed using a single database connection
			UniversalConnectionHandler connectionHandler = new UniversalConnectionHandler(false, handler, workforceRecruitmentDaoConn, hrHrStaffingDaoConn)
			{
				/*
				 * (non-Javadoc)
				 * @see com.homedepot.ta.aa.dao.basic.UniversalConnectionHandler#handleQuery(java.util.Map, com.homedepot.ta.aa.dao.QueryHandler)
				 */
				@Override
				public void handleQuery(Map<DAOConnection, DAOConnection> connectionList, QueryHandler handler) throws QueryException
				{
					DAOConnection workforceRecruitmentDaoConn = connectionList.get(WORKFORCERECRUITMENT_DAO_CONTRACT);
					Query workforceRecruitmentQuery = workforceRecruitmentDaoConn.getQuery();

					DAOConnection hrHrStaffingDaoConn = connectionList.get(HRSTAFFING_DAO_CONTRACT);
					Query hrHrStaffingQuery = hrHrStaffingDaoConn.getQuery();
					
					// cast the handler to the right type
					PhoneScreenHandler phnScrnHandler = (PhoneScreenHandler)handler;
					
					// initialize the query parameters required to execute the first query
					
					//Get Phone Screen Information in order to obtain the requisition number, store number, and calendar Id 
					MapStream inputs = new MapStream(SEL_READ_HR_PHN_SCRN);
					inputs.put(HR_PHN_SCRN_ID, phoneScreenId);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Querying for phone screen details using phone screen id %1$d", phoneScreenId));
					} // end if
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
					
					// get the object populated by the query handler
					PhoneScreenIntrwDetailsTO phnScrnDetails = phnScrnHandler.getPhnScrnIntrvwDetails();					
					
					// make sure we found the phone screen details
					if(phnScrnDetails == null)
					{
						throw new QueryException(String.format("Details for phone screen %1$d not found", phoneScreenId));
					} // end if(phnScrnDetails == null)
					
					mLogger.debug(String.format("Found Requisition %1$s associated with phone screen %2$d", phnScrnDetails.getReqNbr(), phoneScreenId));
					
					//Getting 1st set of Requisition Details
					inputs.clear();
					inputs.setSelectorName(SEL_READ_REQN_DETAILS);
					inputs.put(EMPLT_REQN_NBR, Integer.parseInt(phnScrnDetails.getReqNbr()));
					inputs.put(TABNO, TABNO_DEPT_NO);
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);					
					
					//get the object populated by the query handler
					RequisitionDetailTO reqnDetails = phnScrnHandler.getRequisitionDetails();
					
					if (reqnDetails == null) {
						throw new QueryException(String.format("Details for requisition %1$d not found", phnScrnDetails.getReqNbr()));						
					}
					
					//Getting Other Requisition Details RSC To Schedule
					inputs.clear();
					inputs.setSelectorName(SEL_READ_THD_STR_EMPLT_REQN);
					inputs.put(EMPLT_REQN_NBR, Integer.parseInt(phnScrnDetails.getReqNbr()));
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);										
					
					mLogger.debug(String.format("RSC To Schedule Flag:%1$s", reqnDetails.getRscSchdFlg()));
					responseRecord.setRscToSchedule(reqnDetails.getRscSchdFlg());
					
					//Query to check #Candidates Sent for Interview < #Requested Interviews
					inputs.clear();
					inputs.setSelectorName(SEL_READ_REMAINING_INTRW_CAND_COUNT_BY_STAT_CD);
					inputs.put(EMPLT_REQN_NBR, Integer.parseInt(phnScrnDetails.getReqNbr()));
					List<Short> interviewStatCodeList = new ArrayList<Short>();
					interviewStatCodeList.add(INTERVIEW_SCHEDULED);
					interviewStatCodeList.add(STORE_SCHEDULING);
					inputs.put(INTERVIEW_STAT_CD_LIST, interviewStatCodeList);
					inputs.put(ACTV_FLG, true);
					
					// execute the first query
					workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
					
					//Get the results for #Candidates Sent for Interview < #Requested Interviews.  When value returned is > zero then condition is met.
					int candidatesSentVsRequestedInterviews = phnScrnHandler.getInterviewsStillNeeded();
										
					//Query to check #Offers < #Open Positions
					inputs.clear();
					inputs.setSelectorName(SEL_READ_HR_STR_EMPLT_REQN_COUNT);					
					inputs.put(EMPLT_REQN_NBR, Integer.parseInt(phnScrnDetails.getReqNbr()));
					inputs.put(CAND_OFFER_MADE_FLG, true);
					inputs.put(CAND_OFFER_STAT_IND, DECLINED_STATUS);
					List<String> activeFlgList = new ArrayList<String>();
					activeFlgList.add("Y");
					activeFlgList.add("H");
					inputs.put(ACTV_FLG_LIST, activeFlgList);					
					
					// execute the query
					workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);
					
					//Get the results for #Offers < #Open.  When value returned is > zero then condition is met.
					int candidatesOfferedVsOpen = phnScrnHandler.getOffersStillNeeded();
					
					if (candidatesOfferedVsOpen > 0 && candidatesSentVsRequestedInterviews > 0) {
						responseRecord.setOffersInterviewsCheck(YES);
					} else {
						responseRecord.setOffersInterviewsCheck(NO);
					}
					mLogger.debug(String.format("Offers and Interview Check:%1$s", responseRecord.getOffersInterviewsCheck()));
					
					//Job Title must be padded with spaces up to 6
					String paddedJobTitle = String.format("%1$-" + 6 + "s", reqnDetails.getJob());
					
					//Get results for is a Phone Screen required
					inputs.clear();
					inputs.setSelectorName(SEL_READ_TRPRX);
					inputs.put(TABNO, TABNO_DEPT_NBR);
					inputs.put(TABLE_KEYS, paddedJobTitle + reqnDetails.getDept());
					inputs.put(START_END_DT, new java.sql.Date(System.currentTimeMillis()));
					
					// execute the query
					hrHrStaffingQuery.getResult(hrHrStaffingDaoConn, phnScrnHandler, inputs);
					
					if (phnScrnHandler.getAgentPhnScrnRequired() != null) {
						responseRecord.setPhoneScreenRequired(phnScrnHandler.getAgentPhnScrnRequired());
					} else {
						throw new QueryException(String.format("Details for phone screen required for requisition %1$d not found", phnScrnDetails.getReqNbr()));	
					}

					mLogger.debug(String.format("Phone Screen required Check:%1$s", responseRecord.getPhoneScreenRequired()));
					
					//Get the results of Interview Availability
					Calendar startTime = Calendar.getInstance();
					startTime.setTimeInMillis(System.currentTimeMillis());
					startTime.add(Calendar.HOUR, 18);
					mLogger.debug(String.format("Availability Start Time:%1$s", new java.sql.Timestamp(startTime.getTimeInMillis())));
					Calendar endTime = Calendar.getInstance();
					endTime.setTimeInMillis(System.currentTimeMillis());
					endTime.add(Calendar.DATE, 14);
					mLogger.debug(String.format("Availability End Time:%1$s", new java.sql.Timestamp(endTime.getTimeInMillis())));					
					inputs.clear();
					inputs.setSelectorName(SEL_READ_COUNT_HR_REQN_SCHD_DTLS);
					inputs.put(REQN_CAL_ID, Integer.parseInt(phnScrnDetails.getReqCalId()));
					inputs.put(BEGIN_TIMESTAMP, new java.sql.Timestamp(startTime.getTimeInMillis()));
					inputs.put(HR_BEGIN_TIMESTAMP, new java.sql.Timestamp(endTime.getTimeInMillis()));
					inputs.put(REQN_SCHD_STAT_CD, (short) 1);
					inputs.addQualifier(BEGIN_TS_GREATER_THAN_EQUAL_TO, true);
					inputs.addQualifier(HR_BEGIN_TS_LESS_THAN_EQUAL_TO, true);
					
					// execute the query
					workforceRecruitmentQuery.getResult(workforceRecruitmentDaoConn, phnScrnHandler, inputs);					
					
					if (phnScrnHandler.getAvailableInterviewSlots() > 0 || responseRecord.getRscToSchedule().equals(NO)) {
						responseRecord.setCalendarAvailability(YES);
					} else {
						responseRecord.setCalendarAvailability(NO);
					}

					mLogger.debug(String.format("Has Interview Availability:%1$s", responseRecord.getCalendarAvailability()));					
					
					theResponse.add(responseRecord);
	            } // end method handleQuery()
			}; // end UniversalConnectionHandler inner class
			
			// execute all the queries with a single connection
			connectionHandler.execute();
						
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
			mLogger.error(String.format("An exception occurred retrieving phone screen requisition specific knock-out questions using phone screen id %1$s", phoneScreenId), qe);
			// throw the error
			throw qe;
		} // end catch
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getPhoneScreenReqSpecKnockOutDetail(), phoneScreenId: %1$d", phoneScreenId));
		} // end if
	
		return theResponse.get(0);
	} // end function getPhoneScreenReqSpecKnockOutDetail()		
} // end class PhoneScreenManager

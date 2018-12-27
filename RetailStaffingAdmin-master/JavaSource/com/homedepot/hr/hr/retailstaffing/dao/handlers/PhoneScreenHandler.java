package com.homedepot.hr.hr.retailstaffing.dao.handlers;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenReqSpecMinReqTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.annotations.AnnotatedQueryHandler;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;
import com.homedepot.ta.aa.dao.exceptions.InsertException;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

public class PhoneScreenHandler extends AnnotatedQueryHandler
{
	// type constants
	private static final String CAND_TYP_APLCNT = "AP";
	private static final String CAND_TYP_ASSOC = "AS";
	// format string constants
	private static final String PHN_FORMAT_STRING = "%1$s%2$s";
	private static final String SKILL_QUEST_FORMAT_STRING = "%1$d_%2$d";
	// delimiter constants
	private static final String NM_DELIMITER = ",";
	// column constants
	private static final String HR_PHN_SCRN_ID = "humanResourcesPhoneScreenId";
	private static final String EMPLT_POSN_CAND_ID = "employmentPositionCandidateId";
	private static final String HR_SYS_STR_NBR = "humanResourcesSystemStoreNumber";
	private static final String EMPLT_REQN_NBR = "employmentRequisitionNumber";
	private static final String CAND_PRSN_ID = "candidatePersonId";
	private static final String CAND_NM = "candidateName";
	private static final String OVRL_RSPN_STAT_CD = "overallRespondStatusCode";
	private static final String PHN_SCRN_STAT_CD = "phoneScreenStatusCode";
	private static final String INTRW_STAT_CD = "interviewStatusCode";
	private static final String INTRW_MAT_STAT_CD = "interviewMaterialStatusCode";
	private static final String MIN_REQMT_STAT_CD = "minimumRequirementStatusCode";
	private static final String PHN_SCRN_NM = "phoneScreenName";
	private static final String EMAIL_ADDR_TXT = "electronicMailAddressText";
	private static final String EMAIL_ADDR_TXT2 = "emailAddressText";
	private static final String REQ_CAL_ID = "requisitionCalendarId";
	private static final String PHN_INTRW_DT = "phoneInterviewDate";
	private static final String PHN_INTRW_TS = "phoneInterviewTimestamp";
	private static final String PHN_SCRN_STAT_TS = "phoneScreenStatusTimestamp";
	private static final String INTRW_STAT_TS = "interviewStatusTimestamp";
	private static final String INTRW_MAT_STAT_TS = "interviewMaterialStatusTimestamp";
	private static final String LAST_NM = "lastName";
	private static final String FRST_NM = "firstName";
	private static final String PHN_AREA_CITY_CD = "phoneAreaCityCode";
	private static final String PHN_LOCL_NBR = "phoneLocalNumber";
	private static final String PHONE1 = "phone1";
	private static final String PHONE2 = "phone2";
	private static final String TZ_KEY1 = "oe22";
	private static final String TZ_KEY2 = "timeZoneKey";
	private static final String LANG_PREF = "languagePreference";
	private static final String REQ_SKILL_TYP = "requisitionSkillType";
	private static final String JOB_TTL_DESC = "jobTitleDescription";
	private static final String APLCNT_STAT = "applicantStatus";
	private static final String INTRW_STAT_DESC = "interviewStatusDescription";
	private static final String PHN_SCRN_STAT_DESC = "phoneScreenStatusDescription";
	private static final String REQ_STAT = "reqStatus";
	private static final String INTRW_DT = "interviewDate";
	private static final String INTRW_TS = "interviewTimestamp";
	private static final String INTRW_LOC_DESC = "interviewLocationDescription";
	private static final String INTRW_ADDR_TXT = "interviewAddressText";
	private static final String INTRW_CITY_NM = "interviewCityName";
	private static final String INTRW_ST_CD = "interviewStateCode";
	private static final String INTRW_ZIP_CD = "interviewZipCodeCode";
	private static final String INTRW_PHN_NBR = "interviewPhoneNumber";
	private static final String NM = "name";
	private static final String ACC_INFO6 = "accessInformation6";
	private static final String APLCNT_ID = "applicantId";
	private static final String CAND_TYP_IND = "candidateTypeIndicator";
	private static final String ZIP_CODE = "zipCode";
	private static final String EMPLT_TST_MOD_ID = "employmentTestModuleId";
	private static final String EMPLT_TST_MOD_QST_ID = "employmentTestModuleQuestionId";
	private static final String REQN_LOC = "requisitionLocation";
	private static final String EMPLT_APLCNT_ID = "employmentApplicantId";
	private static final String CAND_AVAIL_IND = "candidateAvailabilityIndicator";
	private static final String ACTV_FLG = "activeFlag";
	private static final String Z_EMPL_ID = "zEmplid";
	private static final String CAND_REFERENCE_NBR = "employmentCandidateId";

	//Added for FMS 7894 IVR Change
	private static final String WEEK_DAY_NBR = "weekDayNumber";
	private static final String DAY_SEG_CODE = "daySegmentCode";
	private static final String TARGET_PAY_AMOUNT = "targetPayAmount";
	private static final String BGC_SYS_PKG_ID = "backgroundCheckSystemPackageId";
	private static final String BGC_SYS_COMPONENT_ID = "backgroundCheckSystemComponentId";
	private static final String RSC_SCHEDULE_FLG = "rscScheduleFlag";
	private static final String AUTH_POSITION_COUNT = "authorizationPositionCount";
	private static final String OE43 = "oe43";
	private static final String COUNT = "count";
	private static final String TOTAL = "total";
	private static final int MVR_PKG_ID = 3;
	private static final int MVR_COMPONENT_ID = 3;
	private static final String SEQUENCE_NUMBER = "sequenceNumber";
	
	private static final String NO = "N";
	private static final String YES = "Y";

	private boolean mStatus = false;
	private final Logger mLogger = Logger.getLogger(PhoneScreenHandler.class);
	private int generatedPhoneScreenId = 0;
	private int mAffectedRecords;
	private short mMaxSeqNumPlusOne = 1;

	/*
	 * Phone screen interview details object
	 */
	private PhoneScreenIntrwDetailsTO mPhnScrnIntrvwDetails;
	
	//Added for FMS 7894 IVR Change
	/*
	 * Requisition Details object
	 */
	private RequisitionDetailTO mRequisitionDetails;
	
	//Added for FMS 7894 IVR Change
	/*
	 * Exempt DMV Store Map object
	 */
	private Map<String, String> mExemptDMVStores;	
	
	//Added for FMS 7894 IVR Change
	/*
	 * Number of Interviews still needed for a requisition
	 */	
	private int mInterviewsStillNeeded;
	
	//Added for FMS 7894 IVR Change
	/*
	 * Number of Offers still needed for a requisition
	 */	
	private int mOffersStillNeeded;
	
	//Added for FMS 7894 IVR Change
	/*
	 * Requisition Specific KO Question object
	 */	
	private PhoneScreenReqSpecMinReqTO mPhoneScreenReqSpecMinReq;
	
	//Added for FMS 7894 IVR Change
	/*
	 * Is a phone screen with an agent required
	 */	
	private String mAgentPhnScrnRequired;
	
	//Added for FMS 7894 IVR Change
	/*
	 * Number of available interview slots
	 */	
	private int mAvailableInterviewSlots;
	
	/*
	 * Array of phone screen knock-out question id's
	 */
	private List<String> mPhnScrnKnockOutQuestions;
	
	private List<PhoneScreenReqSpecMinReqTO> mPhnScrnReqSpecKnockOutQuestions;
	
	public void setStatus(boolean mStatus)
	{
		this.mStatus = mStatus;
	}

	public boolean isStatus()
	{
		return mStatus;
	}

	public int getPhoneScreenId()
	{
		return generatedPhoneScreenId;
	}

	public boolean isSuccess()
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter PhoneScreenHandler isSuccess():" + mStatus);
		}
		return mStatus;
	} // end function exists()

	public void resetStatus()
	{
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter PhoneScreenHandler resetStatus():" + mStatus);
		}
		mStatus = false;
	}

	@QuerySelector(name = "updatePhoneScreenStatusByPhoneScreenStatCode", operation = QueryMethod.updateObject)
	public void updatePhoneScreenStatusByPhoneScreenStat(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updatePhoneScreenStatusByPhoneScreenStat() status : " + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "updatePhoneScreenStatusByInterviewScreenStatCode", operation = QueryMethod.updateObject)
	public void updatePhoneScreenStatusByInterviewSchedStat(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updatePhoneScreenStatusByInterviewSchedStat() status: " + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "updatePhoneScreenStatusByInterviewMaterialStatCode", operation = QueryMethod.updateObject)
	public void updatePhoneScreenStatusByMaterialStat(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updatePhoneScreenStatusByInterviewMaterialStatCode() status: " + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "updateHumanResourcesPhoneScreen", operation = QueryMethod.updateObject)
	public void updatePhoneScreenDispositionCode(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updatePhoneScreenDispositionCode() status: " + success);
		}
		mStatus = success;
	}
	
	@QuerySelector(name = "readPhoneScreenMinimumRequirementForExistence", operation = QueryMethod.getObject)
	public void readPhoneScreenMinReq(Object target, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter readPhoneScreenMinReq() status:" + Boolean.valueOf(target.toString().toString()));
		}
		if(target != null)
		{
			mStatus = Boolean.valueOf(target.toString());
		}
	}

	@QuerySelector(name = "updatePhoneScreenMinimumRequirement", operation = QueryMethod.updateObject)
	public void updatePhoneScreenMinReq(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updatePhoneScreenMinReq() status:" + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "createPhoneScreenMinimumRequirement", operation = QueryMethod.insertObject)
	public void createPhoneScreenMinReq(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter createPhoneScreenMinReq() status:" + successFlag);
		}
		mStatus = successFlag;
	}

	@QuerySelector(name = "createPhoneScreenMinimumRequirementBatch", operation = QueryMethod.insertObject)
	public void createPhoneScreenMinReqBatch(Object arg0, boolean successFlag, int count, Query query, Inputs inputs) throws QueryException
	{
		// if the correct number of records was not added
		if(!successFlag && (count != inputs.getNVStream().getList("createPhoneScreenMinimumRequirementList").size()))
		{
			throw new InsertException(String.format("An exception occurred inserting minimum requirements. Expected record count: %1$d, actual records inserted: %2$d. Rolling back",
				inputs.getNVStream().getList("createPhoneScreenMinimumRequirementList").size(), count));
		} // end if(!success && (count != inputs.getNVStream().getList("createPhoneScreenMinimumRequirementList").size()))
		
		// add the number of inserted to the count (because these are done in batch, this is a running total)
		mAffectedRecords += count;		
	}
	
	@QuerySelector(name = "updateHumanResourcesPhoneScreenNote", operation = QueryMethod.updateObject)
	public void updatePhoneScreenNote(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updatePhoneScreenNote() status:" + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "createHumanResourcesPhoneScreenNote", operation = QueryMethod.insertObject)
	public void createPhoneScreenNote(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter createPhoneScreenNote() status:" + successFlag);
		}
		mStatus = successFlag;
	}

	@QuerySelector(name = "readHumanResourcesPhoneScreenNoteForExistence", operation = QueryMethod.getObject)
	public void readPhoneScreenNote(Object target, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter readPhoneScreenNote() status:" + Boolean.valueOf(target.toString().toString()));
		}
		if(target != null)
		{
			mStatus = Boolean.valueOf(target.toString());
		}
	}

	@QuerySelector(name = "updateHumanResourcesPhoneScreenNonStatusCodes", operation = QueryMethod.updateObject)
	public void updateHumanResourcesPhoneScreen(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updateHumanResourcesPhoneScreen() status:" + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "createHumanResourcesPhoneScreen", operation = QueryMethod.insertObject)
	public void createHumanResourcesPhoneScreen(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter createHumanResourcesPhoneScreen() status:" + successFlag);
		}
		if(successFlag && count > 0)
		{
			generatedPhoneScreenId = Integer.parseInt(arg0.toString());
		}
	}

	@QuerySelector(name = "updateHumanResourcesPhoneScreenMinimumRequirementStatusCode", operation = QueryMethod.updateObject)
	public void updateHumanResourcesPhoneScreenMinimumRequirementStatusCode(Object target, boolean success, int count, Query query, Inputs inputs)
	    throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updateHumanResourcesPhoneScreenMinimumRequirementStatusCode() status : " + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "updateHumanResourcesRetailStaffReconciliation", operation = QueryMethod.updateObject)
	public void updateHumanResourcesRetailStaffReconciliation(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter updateHumanResourcesRetailStaffReconciliation() status : " + success);
		}
		mStatus = success;
	}

	@QuerySelector(name = "readHumanResourcesPhoneScreenByRequisitionCreateDateInputList", operation = QueryMethod.getObject)
	public void readHumanResourcesPhoneScreenByRequisitionCreateDateInputList(Object target, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter readHumanResourcesPhoneScreenByRequisitionCreateDateInputList() status:"
			    + Boolean.valueOf(target.toString().toString()));
		}
		if(target != null)
		{
			mStatus = Boolean.valueOf(target.toString());
		}
	}

	@QuerySelector(name = "readHumanResourcesPhoneScreenNote", operation = QueryMethod.getObject)
	public void readHumanResourcesPhoneScreenNoteList(Object target, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter readHumanResourcesPhoneScreenNoteList() status:" + Boolean.valueOf(target.toString().toString()));
		}
		if(target != null)
		{
			mStatus = Boolean.valueOf(target.toString());
		}
	}

	@QuerySelector(name = "readTRPRX000ByInputList", operation = QueryMethod.getObject)
	public void read032ForPhoneScreen(Object target, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter read032ForPhoneScreen() status:" + Boolean.valueOf(target.toString().toString()));
		}
		if(target != null)
		{
			mStatus = Boolean.valueOf(target.toString());
		}
	}

	/**
	 * This method processes the results returned when executing the "readTRPRX000ByInputList"
	 * DAO selector
	 * 
	 * @param results				DAO infrastructure results object
	 * @param query					DAO infrastructure query object
	 * @param inputs				DAO infrastructure inputs object
	 * 
	 * @throws QueryException		Thrown if any exception occurs within the DAO infrastructure
	 */		
	@QuerySelector(name="readTRPRX000ByInputList", operation=QueryMethod.getResult)
	public void readPhoneScrnRequired(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only be a single record returned
		if(results.next())
		{
			// set the Phone Screen Required object.
			if (results.wasNull(OE43) || StringUtils.trim(results.getString(OE43)).equals("")) {
				mAgentPhnScrnRequired = "Y";
			} else {
				mAgentPhnScrnRequired = StringUtils.trim(results.getString(OE43));
			}
		} // end if(results.next())
	} // end function readPhoneScrnRequired()
	
	@QuerySelector(name = "readPhoneScreenMinimumRequirement", operation = QueryMethod.getObject)
	public void readPhoneScreenMinimumRequirementList(Object target, Query query, Inputs inputs) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter readPhoneScreenMinimumRequirementList() status:" + Boolean.valueOf(target.toString().toString()));
		}
		if(target != null)
		{
			mStatus = Boolean.valueOf(target.toString());
		}
	}

	
	/**
	 * Get the phone screen interview details object that would be populated by
	 * one or many methods in this QueryHandler
	 * 
	 * @return Phone screen interview details object 
	 */
	public PhoneScreenIntrwDetailsTO getPhnScrnIntrvwDetails()
	{
		return mPhnScrnIntrvwDetails;
	} // end function getPhnScrnIntrvwDetails()
	
	/**
	 * Get the phone screen knockout questions that would be populated
	 * by one or many methods in this Query Handler
	 * 
	 * @return Phone screen knockout questions
	 */
	public List<String> getPhnScrnKnockOutQuestions()
	{
		return mPhnScrnKnockOutQuestions;
	} // end function getPhnScrnKnockOutQuestions()

	/**
	 * Process the results returned by the readHumanResourcesPhoneScreen selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readHumanResourcesPhoneScreen", operation = QueryMethod.getResult)
	public void readHumanResourcesPhoneScreen(Results results, Query query, Inputs inputs) throws QueryException
	{
		// the query SHOULD only ever return at most one row (and should always
		// be called first in the sequence of queries it's used in)
		if(results.next())
		{
			/*
			 * The details object could already be initialized and contain data, so only initialize
			 * it if it's null so we preserve the data that would've been populated already
			 */
			if(mPhnScrnIntrvwDetails == null)
			{
				mPhnScrnIntrvwDetails = new PhoneScreenIntrwDetailsTO();	
			} // end if(mPhnScrnIntrvwDetails == null)
			
			mPhnScrnIntrvwDetails.setCndtNbr(results.getString(EMPLT_POSN_CAND_ID));
			mPhnScrnIntrvwDetails.setCndStrNbr(results.getString(HR_SYS_STR_NBR));
			mPhnScrnIntrvwDetails.setReqNbr(String.valueOf(results.getInt(EMPLT_REQN_NBR)));
			mPhnScrnIntrvwDetails.setAid(results.getString(CAND_PRSN_ID));
			mPhnScrnIntrvwDetails.setPhnScreener(results.getString(PHN_SCRN_NM));
			mPhnScrnIntrvwDetails.setEmailAdd(results.getString(EMAIL_ADDR_TXT));
			mPhnScrnIntrvwDetails.setReqCalId(String.valueOf(results.getInt(REQ_CAL_ID)));
			mPhnScrnIntrvwDetails.setName(results.getString(CAND_NM));
			mPhnScrnIntrvwDetails.setPhnScrnDate(Util.converDatetoDateTO(results.getDate(PHN_INTRW_DT)));
			mPhnScrnIntrvwDetails.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp(PHN_INTRW_TS)));
			mPhnScrnIntrvwDetails.setPhoneScreenStatusTimestamp(Util.converTimeStampTimeStampTO(results.getTimestamp(PHN_SCRN_STAT_TS)));
			mPhnScrnIntrvwDetails.setInterviewStatusTimestamp(Util.converTimeStampTimeStampTO(results.getTimestamp(INTRW_STAT_TS)));
			mPhnScrnIntrvwDetails.setInterviewMaterialStatusTimestamp(Util.converTimeStampTimeStampTO(results.getTimestamp(INTRW_MAT_STAT_TS)));
			//The Access Code is the last 6 digits of the KCR#, no need to check for length if someone has a phoneScreenId then they would have 
			//a KCR# because they would have applied through Kenexa 
			mPhnScrnIntrvwDetails.setAccessCode(StringUtils.trim(results.getString(CAND_REFERENCE_NBR)).substring(4));
			
			// if there is an associate id on the table then this is an internal candidate
			if(mPhnScrnIntrvwDetails.getAid() == null || mPhnScrnIntrvwDetails.getAid().trim().length() == 0)
			{
				mPhnScrnIntrvwDetails.setInternalExternal(RetailStaffingConstants.EXTERNAL_FLAG);
			} // end if(mPhnScrnIntrvwDetails.getAid() == null || mPhnScrnIntrvwDetails.getAid().trim().length() == 0)			
			else // not internal, so external
			{
				mPhnScrnIntrvwDetails.setInternalExternal(RetailStaffingConstants.INTERNAL_FLAG);
			} // end else

			if(!results.wasNull(OVRL_RSPN_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setOverAllStatus(String.valueOf(results.getShort(OVRL_RSPN_STAT_CD)));
			} // end if(!results.wasNull(OVRL_RSPN_STAT_CD))

			if(!results.wasNull(PHN_SCRN_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setPhoneScreenStatusCode(results.getString(PHN_SCRN_STAT_CD));
			} // end if(!results.wasNull(PHN_SCRN_STAT_CD))

			if(!results.wasNull(INTRW_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setInterviewStatusCode(String.valueOf(results.getShort(INTRW_STAT_CD)));
			} // end if(!results.wasNull(INTRW_STAT_CD))

			if(!results.wasNull(INTRW_MAT_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setInterviewMaterialStatusCode(String.valueOf(results.getShort(INTRW_MAT_STAT_CD)));
			} // end if(!results.wasNull(INTRW_MAT_STAT_CD))

			if(!results.wasNull(MIN_REQMT_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setYnstatus(String.valueOf(results.getShort(MIN_REQMT_STAT_CD)));
			} // end if(!results.wasNull(MIN_REQMT_STAT_CD))
		} // end if(results.next())
	} // end function readHumanResourcesPhoneScreen()

	/**
	 * Process the results returned by the readApplicantByPhoneScreenId selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readApplicantByPhoneScreenId", operation = QueryMethod.getResult)
	public void readApplicantByPhoneScreenId(Results results, Query query, Inputs inputs) throws QueryException
	{
		// the query SHOULD only ever return at most one row
		if(results.next())
		{
			// if it hasn't been instantiated yet, instantiate it (could be used stand alone or along with other queries)
			if(mPhnScrnIntrvwDetails == null)
			{
				mPhnScrnIntrvwDetails = new PhoneScreenIntrwDetailsTO();
			} // end if(mPhnScrnIntrvwDetails == null)

			mPhnScrnIntrvwDetails.setLastName(StringUtils.trim(results.getString(LAST_NM)));
			mPhnScrnIntrvwDetails.setFirstName(StringUtils.trim(results.getString(FRST_NM)));
			mPhnScrnIntrvwDetails.setCanPhn(String.format(PHN_FORMAT_STRING, StringUtils.trim(results.getString(PHN_AREA_CITY_CD)), StringUtils.trim(results.getString(PHN_LOCL_NBR))));
			mPhnScrnIntrvwDetails.setPhone2(StringUtils.trim(results.getString(PHONE2)));
			mPhnScrnIntrvwDetails.setEmailAdd(StringUtils.trim(results.getString(EMAIL_ADDR_TXT)));
			mPhnScrnIntrvwDetails.setTimeZone(StringUtils.trim(results.getString(TZ_KEY1)));
			mPhnScrnIntrvwDetails.setLangPref(StringUtils.trim(results.getString(LANG_PREF)));
			mPhnScrnIntrvwDetails.setSkillType(StringUtils.trim(results.getString(REQ_SKILL_TYP)));
			mPhnScrnIntrvwDetails.setJobTtl(StringUtils.trim(results.getString(JOB_TTL_DESC)));
			mPhnScrnIntrvwDetails.setCanStatus(StringUtils.trim(results.getString(APLCNT_STAT)));
			mPhnScrnIntrvwDetails.setReqStat(StringUtils.trim(results.getString(REQ_STAT)));
			mPhnScrnIntrvwDetails.setHrgStoreLoc(StringUtils.trim(results.getString(REQN_LOC)));
			//The Access Code is the last 6 digits of the KCR#, no need to check for length if someone has a phoneScreenId then they would have 
			//a KCR# because they would have applied through Kenexa 			
			mPhnScrnIntrvwDetails.setAccessCode(StringUtils.trim(results.getString(CAND_REFERENCE_NBR)).substring(4));
			
			// if there is an interview status
			if(!results.wasNull(INTRW_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setInterviewStatusCode(String.valueOf(results.getShort(INTRW_STAT_CD)));
				mPhnScrnIntrvwDetails.setInterviewStatusDesc(StringUtils.trim(results.getString(INTRW_STAT_DESC)));
			} // end if(!results.wasNull(INTRW_STAT_CD))

			// if there is a phone status
			if(!results.wasNull(PHN_SCRN_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setPhoneScreenStatusCode(String.valueOf(results.getShort(PHN_SCRN_STAT_CD)));
				mPhnScrnIntrvwDetails.setPhoneScreenStatusDesc(StringUtils.trim(results.getString(PHN_SCRN_STAT_DESC)));
			} // end if(!results.wasNull(PHN_SCRN_STAT_CD))

			// if there is an interview date, populate the interview details
			if(!results.wasNull(INTRW_DT))
			{
				IntrwLocationDetailsTO intrvwLoc = new IntrwLocationDetailsTO();
				intrvwLoc.setInterviewDate(Util.converDatetoDateTO(results.getDate(INTRW_DT)));
				intrvwLoc.setInterviewTime(Util.converTimeStampTimeStampTO(results.getTimestamp(INTRW_TS)));
				intrvwLoc.setIntrvwDtTm(results.getTimestamp(INTRW_TS));
				intrvwLoc.setInterviewLocName(StringUtils.trim(results.getString(INTRW_LOC_DESC)));
				intrvwLoc.setAdd(StringUtils.trim(results.getString(INTRW_ADDR_TXT)));
				intrvwLoc.setCity(StringUtils.trim(results.getString(INTRW_CITY_NM)));
				intrvwLoc.setState(StringUtils.trim(results.getString(INTRW_ST_CD)));
				intrvwLoc.setZip(StringUtils.trim(results.getString(INTRW_ZIP_CD)));
				intrvwLoc.setPhone(StringUtils.trim(results.getString(INTRW_PHN_NBR)));
				intrvwLoc.setInterviewLocId(StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
				intrvwLoc.setTimeZone(StringUtils.trim(results.getString(TZ_KEY1)));

				mPhnScrnIntrvwDetails.setIntrLocDtls(intrvwLoc);
			} // end if(!results.wasNull(INTRW_DT))
		} // end if(results.next())
		else // no rows found, throw an exception
		{
			throw new QueryException(String.format("Unable to load external applicant data using phone screen id %1$d. No rows found", inputs.getNVStream().getInt(HR_PHN_SCRN_ID)));
		} // end else
	} // end function readApplicantByPhoneScreenId()

	/**
	 * Process the results returned by the readAssociateDetailsByPhoneScreenId
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readAssociateDetailsByPhoneScreenId", operation = QueryMethod.getResult)
	public void readAssociateDetailsByPhoneScreenId(Results results, Query query, Inputs inputs) throws QueryException
	{
		// the query SHOULD only ever return at most one row
		if(results.next())
		{
			// if it hasn't been instantiated yet, instantiate it (could be used stand alone or along with other queries)
			if(mPhnScrnIntrvwDetails == null)
			{
				mPhnScrnIntrvwDetails = new PhoneScreenIntrwDetailsTO();
			} // end if(mPhnScrnIntrvwDetails == null)

			// name cannot be null, so not going to check for a null value
			String name = StringUtils.trim(results.getString(NM));
			int delimPos = name.indexOf(NM_DELIMITER);
			// split the name into first and last using , as the delimiter
			String lastName = StringUtils.trim(name.substring(0, delimPos));
			String firstName = StringUtils.trim(name.substring(delimPos + 1));

			mPhnScrnIntrvwDetails.setLastName(lastName);
			mPhnScrnIntrvwDetails.setFirstName(firstName);
			mPhnScrnIntrvwDetails.setCanPhn(StringUtils.trim(results.getString(PHONE1)));
			mPhnScrnIntrvwDetails.setPhone2(StringUtils.trim(results.getString(PHONE2)));
			mPhnScrnIntrvwDetails.setEmailAdd(StringUtils.trim(results.getString(ACC_INFO6)));
			mPhnScrnIntrvwDetails.setTimeZone(StringUtils.trim(results.getString(TZ_KEY2)));
			mPhnScrnIntrvwDetails.setLangPref(StringUtils.trim(results.getString(LANG_PREF)));
			mPhnScrnIntrvwDetails.setSkillType(StringUtils.trim(results.getString(REQ_SKILL_TYP)));
			mPhnScrnIntrvwDetails.setJobTtl(StringUtils.trim(results.getString(JOB_TTL_DESC)));
			mPhnScrnIntrvwDetails.setCanStatus(StringUtils.trim(results.getString(APLCNT_STAT)));
			//The Access Code is the last 6 digits of the KCR#, no need to check for length if someone has a phoneScreenId then they would have 
			//a KCR# because they would have applied through Kenexa 
			mPhnScrnIntrvwDetails.setAccessCode(StringUtils.trim(results.getString(CAND_REFERENCE_NBR)).substring(4));

			// if the candidate status is not null or NO, set it to YES
			if(mPhnScrnIntrvwDetails.getCanStatus() != null && !mPhnScrnIntrvwDetails.getCanPhn().trim().equalsIgnoreCase(NO))
			{
				mPhnScrnIntrvwDetails.setCanStatus(YES);
			} // end if(mPhnScrnIntrvwDetails.getCanStatus() != null && !mPhnScrnIntrvwDetails.getCanPhn().trim().equalsIgnoreCase(NO))
			
			mPhnScrnIntrvwDetails.setReqStat(StringUtils.trim(results.getString(REQ_STAT)));
			mPhnScrnIntrvwDetails.setHrgStoreLoc(StringUtils.trim(results.getString(REQN_LOC)));

			// if there is an interview status
			if(!results.wasNull(INTRW_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setInterviewStatusCode(String.valueOf(results.getShort(INTRW_STAT_CD)));
				mPhnScrnIntrvwDetails.setInterviewStatusDesc(StringUtils.trim(results.getString(INTRW_STAT_DESC)));
			} // end if(!results.wasNull(INTRW_STAT_CD))

			// if there is a phone status
			if(!results.wasNull(PHN_SCRN_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setPhoneScreenStatusCode(String.valueOf(results.getShort(PHN_SCRN_STAT_CD)));
				mPhnScrnIntrvwDetails.setPhoneScreenStatusDesc(StringUtils.trim(results.getString(PHN_SCRN_STAT_DESC)));
			} // end if(!results.wasNull(PHN_SCRN_STAT_CD))

			// if there is an interview date, populate the interview details
			if(!results.wasNull(INTRW_DT))
			{
				IntrwLocationDetailsTO intrvwLoc = new IntrwLocationDetailsTO();
				intrvwLoc.setInterviewDate(Util.converDatetoDateTO(results.getDate(INTRW_DT)));
				intrvwLoc.setInterviewTime(Util.converTimeStampTimeStampTO(results.getTimestamp(INTRW_TS)));
				intrvwLoc.setIntrvwDtTm(results.getTimestamp(INTRW_TS));
				intrvwLoc.setInterviewLocName(StringUtils.trim(results.getString(INTRW_LOC_DESC)));
				intrvwLoc.setAdd(StringUtils.trim(results.getString(INTRW_ADDR_TXT)));
				intrvwLoc.setCity(StringUtils.trim(results.getString(INTRW_CITY_NM)));
				intrvwLoc.setState(StringUtils.trim(results.getString(INTRW_ST_CD)));
				intrvwLoc.setZip(StringUtils.trim(results.getString(INTRW_ZIP_CD)));
				intrvwLoc.setPhone(StringUtils.trim(results.getString(INTRW_PHN_NBR)));
				intrvwLoc.setInterviewLocId(StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
				intrvwLoc.setTimeZone(StringUtils.trim(results.getString(TZ_KEY2)));

				mPhnScrnIntrvwDetails.setIntrLocDtls(intrvwLoc);
			} // end if(!results.wasNull(INTRW_DT))
		} // end if(results.next())
		else // no rows found, throw an exception
		{
			throw new QueryException(String.format("Unable to load internal applicant data using phone screen id %1$d. No rows found", inputs.getNVStream().getInt(HR_PHN_SCRN_ID)));
		} // end else
	} // end function readApplicantByPhoneScreenId()

	/**
	 * Process the results returned by the readApplicantByZipCodeAndPartialEmploymentApplicantId
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query or if multiple candidate details were found
	 *              readApplicantByZipCodeAndPartialEmploymentApplicantId
	 */
	@QuerySelector(name = "readEmployementApplicantPrimaryAndApplicantAddressPhoneDetails", operation = QueryMethod.getResult)
	public void readAplcntByZipPartialAplcntId(Results results, Query query, Inputs inputs) throws QueryException
	{
		/*
		 * The query can return multiple rows. There are only two cases where we can successfully determine
		 * if this is the correct candidate (one row returned or exactly two rows returned for the same candidate
		 * id where one row is an AP row and one row is an AS row with the AS row winning).
		 */
		if(results.next())
		{
			mPhnScrnIntrvwDetails = new PhoneScreenIntrwDetailsTO();
			mPhnScrnIntrvwDetails.setCndtNbr(StringUtils.trim(results.getString(APLCNT_ID)));

			// if the query returned back a candidate type of AP then this is an external applicant
			if(results.getString(CAND_TYP_IND).trim().equalsIgnoreCase(CAND_TYP_APLCNT))
			{
				mPhnScrnIntrvwDetails.setInternalExternal(RetailStaffingConstants.EXTERNAL_FLAG);
			} // end if(results.getString(CAND_TYP_IND).trim().equalsIgnoreCase(CAND_TYP_APLCNT))
			else // this is an internal applicant
			{
				mPhnScrnIntrvwDetails.setInternalExternal(RetailStaffingConstants.INTERNAL_FLAG);
				mPhnScrnIntrvwDetails.setAid(StringUtils.trim(results.getString(Z_EMPL_ID)));
			} // end else

			// if a second row was returned
			if(results.next())
			{
				// check to see if this is the same candidate
				if(mPhnScrnIntrvwDetails.getCndtNbr() != null && mPhnScrnIntrvwDetails.getCndtNbr().equals(StringUtils.trim(results.getString(APLCNT_ID))))
				{
					// if it's the same candidate and the last row was an external
					if(mPhnScrnIntrvwDetails.getInternalExternal().trim().equalsIgnoreCase(RetailStaffingConstants.EXTERNAL_FLAG))
					{
						// the only way this is valid is if this row is an internal
						if(results.getString(CAND_TYP_IND).equals(CAND_TYP_ASSOC))
						{
							// the new row is an internal and the prior row was an external, so we'll use this row
							mPhnScrnIntrvwDetails.setInternalExternal(RetailStaffingConstants.INTERNAL_FLAG);							
						} // end if(results.getString(CAND_TYP_IND).equals(CAND_TYP_ASSOC))
						else
						{
							// both rows are external
							throw new QueryException(String.format("Multiple candidates found using partial candidate id %1$s and zip code %2$s",
								inputs.getNVStream().getString(EMPLT_APLCNT_ID), inputs.getNVStream().getString(ZIP_CODE)));
						} // end else
					} // end if(mPhnScrnIntrvwDetails.getInternalExternal().trim().equalsIgnoreCase(RetailStaffingConstants.EXTERNAL_FLAG))
					else
					{
						// the last row was an internal, just double-check we don't have another internal row here
						if(results.getString(CAND_TYP_IND).equals(CAND_TYP_ASSOC))
						{
							throw new QueryException(String.format("Multiple candidates found using partial candidate id %1$s and zip code %2$s",
								inputs.getNVStream().getString(EMPLT_APLCNT_ID), inputs.getNVStream().getString(ZIP_CODE)));
						} // end if(results.getString(CAND_TYP_IND).equals(CAND_TYP_ASSOC))
					} // end else
					
					// if we got a second row for the same candidate, make sure there are no more rows
					if(results.next())
					{
						// there's a 3rd row, so don't know what to do
						throw new QueryException(String.format("Multiple candidates found using partial candidate id %1$s and zip code %2$s",
							inputs.getNVStream().getString(EMPLT_APLCNT_ID), inputs.getNVStream().getString(ZIP_CODE)));
					} // end if(results.next())
				} // end if(mPhnScrnIntrvwDetails.getCndtNbr() != null && mPhnScrnIntrvwDetails.getCndtNbr().equals(StringUtils.trim(results.getString(APLCNT_ID))))
				else
				{
					// it's a different candidate, so don't know what to do
					throw new QueryException(String.format("Multiple candidates found using partial candidate id %1$s and zip code %2$s",
						inputs.getNVStream().getString(EMPLT_APLCNT_ID), inputs.getNVStream().getString(ZIP_CODE)));
				} // end else
			} // end if(results.next())
			// if there are multiple rows returned, then we can't safely select one, so throw an exception
		} // end if(results.next())
	} // end function readAplcntByZipPartialAplcntId()

	/**
	 * Process the results returned by the readRequisitionPhoneScreen
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readRequisitionPhoneScreen", operation = QueryMethod.getResult)
	public void readRequisitionPhoneScreen(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one active phone screen for a candidate
		if(results.next())
		{
			// if it hasn't been instantiated yet, instantiate it (could be used stand alone or along with other queries)
			if(mPhnScrnIntrvwDetails == null)
			{
				mPhnScrnIntrvwDetails = new PhoneScreenIntrwDetailsTO();
			} // end if(mPhnScrnIntrvwDetails == null)

			mPhnScrnIntrvwDetails.setItiNbr(String.valueOf(results.getString(HR_PHN_SCRN_ID)));
			mPhnScrnIntrvwDetails.setCndtNbr(StringUtils.trim(results.getString(EMPLT_POSN_CAND_ID)));
			mPhnScrnIntrvwDetails.setAid(StringUtils.trim(results.getString(CAND_PRSN_ID)));
			mPhnScrnIntrvwDetails.setName(StringUtils.trim(results.getString(CAND_NM)));
			mPhnScrnIntrvwDetails.setEmailAdd(StringUtils.trim(results.getString(EMAIL_ADDR_TXT2)));

			if(!results.wasNull(PHN_SCRN_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setPhoneScreenStatusCode(String.valueOf(results.getShort(PHN_SCRN_STAT_CD)));
			} // end if(!results.wasNull(PHN_SCRN_STAT_CD))

			if(!results.wasNull(INTRW_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setInterviewStatusCode(String.valueOf(results.getShort(INTRW_STAT_CD)));
			} // end if(!results.wasNull(INTRW_STAT_CD))

			if(!results.wasNull(INTRW_MAT_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setInterviewMaterialStatusCode(String.valueOf(results.getShort(INTRW_MAT_STAT_CD)));
			} // end if(!results.wasNull(INTRW_MAT_STAT_CD))

			if(!results.wasNull(MIN_REQMT_STAT_CD))
			{
				mPhnScrnIntrvwDetails.setYnstatus(String.valueOf(results.getShort(MIN_REQMT_STAT_CD)));
			} // end if(!results.wasNull(MIN_REQMT_STAT_CD))
			
			/*
			 * Check if multiple rows were returned. If multiple rows were returned then
			 * we do not know which record we should return so throw an exception
			 */
			if(results.next())
			{
				throw new QueryException(String.format("Multiple active requisitions found for candidate %1$s", mPhnScrnIntrvwDetails.getCndtNbr()));
			} // end if(results.next())
		} // end if(results.next())
	} // end function readRequisitionPhoneScreen()
	
	/**
	 * Process the results returned by the readHumanResourcesPhoneScreenInterviewDetails
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readHumanResourcesPhoneScreenInterviewDetails", operation = QueryMethod.getResult)
	public void readHrPhnScrnIntrwDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// should only ever be a single row returned
		if(results.next())
		{
			/*
			 * The details object could already be initialized and contain data, so only initialize
			 * it if it's null so we preserve the data that would've been populated already
			 */
			if(mPhnScrnIntrvwDetails == null)
			{
				mPhnScrnIntrvwDetails = new PhoneScreenIntrwDetailsTO();	
			} // end if(mPhnScrnIntrvwDetails == null)
			
			mPhnScrnIntrvwDetails.setItiNbr(String.valueOf(inputs.getNVStream().getInt(HR_PHN_SCRN_ID)));
			mPhnScrnIntrvwDetails.setSkillType(StringUtils.trim(results.getString(REQ_SKILL_TYP)));
		} // end if(results.next())
	} // end function readHrPhnScrnIntrwDetails()	
	
	/**
	 * Process the results returned by the readEmploymentModuleQuestionByEmploymentId
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readEmploymentModuleQuestionByEmploymentId", operation = QueryMethod.getResult)
	public void readEmpltModQuestByEmpltId(Results results, Query query, Inputs inputs) throws QueryException
	{
		mPhnScrnKnockOutQuestions = new ArrayList<String>();
		
		while(results.next())
		{
			mPhnScrnKnockOutQuestions.add(String.format(SKILL_QUEST_FORMAT_STRING, results.getShort(EMPLT_TST_MOD_ID),
				results.getShort(EMPLT_TST_MOD_QST_ID)));
		} // end while(results.next())
	} // end function readEmpltModQuestByEmpltId()	
	
	/**
	 * Process the results returned by the readEmploymentPositionCandidateByEmploymentPositionCandateId
	 * selector
	 * 
	 * @param results				Query results returned by the DAO framework
	 * @param query					DAO framework query object
	 * @param inputs				Inputs collection provided to the DAO framework for this queries execution
	 * 
	 * @throws QueryException		Thrown if an exception occurs using the DAO framework to execute the query
	 */
	@QuerySelector(name = "readEmploymentPositionCandidateByEmploymentPositionCandateId", operation = QueryMethod.getResult)
	public void readEmpltPosnCandByEmpltPosnCandId(Results results, Query query, Inputs inputs) throws QueryException
	{
		/*
		 * The details object could already be initialized and contain data, so only initialize
		 * it if it's null so we preserve the data that would've been populated already
		 */
		if(mPhnScrnIntrvwDetails == null)
		{
			mPhnScrnIntrvwDetails = new PhoneScreenIntrwDetailsTO();	
		} // end if(mPhnScrnIntrvwDetails == null)
		
		// should only ever be a single row returned
		if(results.next())
		{
			mPhnScrnIntrvwDetails.setCanStatus(StringUtils.trim(results.getString(CAND_AVAIL_IND)));
			
			if(results.getString(ACTV_FLG) != null)
			{
				mPhnScrnIntrvwDetails.setReqStat(StringUtils.trim(results.getString(ACTV_FLG)));	
			} // end if(results.getString(ACTV_FLG) != null)
			else
			{
				mPhnScrnIntrvwDetails.setReqStat(NO);
			} // end else			
		} // end if(results.next())
		else
		{
			// no records found, set both statuses to NO
			mPhnScrnIntrvwDetails.setCanStatus(NO);
			mPhnScrnIntrvwDetails.setReqStat(NO);
		} // end else
	} // end function readEmpltPosnCandByEmpltPosnCandId()

	//Added for FMS 7894 IVR Changes
	/**
	 * Process the results returned by the readHumanResourcesRequisitionDailyAvailableByInputList
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readHumanResourcesRequisitionDailyAvailableByInputList", operation = QueryMethod.getResult)
	public void readHumanResourcesRequisitionDailyAvailableByInputList(Results results, Query query, Inputs inputs) throws QueryException
	{
	//	mPhnScrnReqSpecKnockOutQuestions = new ArrayList<PhoneScreenReqSpecMinReqTO>();
		mPhoneScreenReqSpecMinReq = new PhoneScreenReqSpecMinReqTO();
		
		boolean weekdayFound = false;
		boolean saturdayFound = false;
		boolean sundayFound = false;
		boolean earlyAMFound = false;
		boolean morningFound = false;
		boolean afternoonFound = false;
		boolean nightFound = false;
		boolean lateNightFound = false;
		boolean overnightFound = false;
		
		while(results.next())
		{
			//Required Days
			switch (results.getShort(WEEK_DAY_NBR)) {
			//Weekdays
			case 1: case 2: case 3: case 4: case 5:
				if (!weekdayFound) { 
					mLogger.debug(String.format("Requisition %1$d setting Weekday required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setWeekdaysSelected(YES);
					weekdayFound = true;
				}
				break;
			//Saturday	
			case 6:
				if (!saturdayFound) {
					mLogger.debug(String.format("Requisition %1$d setting Saturday required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setSaturdaysSelected(YES);
					saturdayFound = true;
				}
				break;
			
			//Sunday	
			case 7:
				if (!sundayFound) {
					mLogger.debug(String.format("Requisition %1$d setting Sunday required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setSundaysSelected(YES);
					sundayFound = true;
				}
				break;	
			} //end switch (results.getShort(WEEK_DAY_NBR)) {
			
			//Required Time Slots
			switch (results.getShort(DAY_SEG_CODE)) {
			//Early AM
			case 1:
				if (!earlyAMFound) {
					mLogger.debug(String.format("Requisition %1$d setting Early AM required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setEarlyMorningSelected(YES);					
					earlyAMFound = true;
				}
				break;
			//Mornings	
			case 2:
				if (!morningFound) {
					mLogger.debug(String.format("Requisition %1$d setting Mornings required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setMorningSelected(YES);
					morningFound = true;
				}
				break;
			//Afternoon	
			case 3:
				if (!afternoonFound) {
					mLogger.debug(String.format("Requisition %1$d setting Afternoons required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setAfternoonSelected(YES);					
					afternoonFound = true;
				}
				break;
			//Nights
			case 4:
				if (!nightFound) {
					mLogger.debug(String.format("Requisition %1$d setting Nights required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setNightSelected(YES);					
					nightFound = true;
				}
				break;
			//Late Nights	
			case 5:
				if (!lateNightFound) {
					mLogger.debug(String.format("Requisition %1$d setting Late Nights required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setLateNightSelected(YES);
					lateNightFound = true;
				}
				break;
			//Overnight
			case 6:
				if (!overnightFound) {
					mLogger.debug(String.format("Requisition %1$d setting Overnights required.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
					mPhoneScreenReqSpecMinReq.setOvernightSelected(YES);
					overnightFound = true;
				}
				break;
			} // end switch (results.getShort(DAY_SEG_CODE)) {
		} // end while(results.next())
		
		//Add the Need Schedule Accommodation question if there are Schedule questions.
		if (!weekdayFound && !saturdayFound && !sundayFound && !earlyAMFound && !morningFound && !afternoonFound && !nightFound && !lateNightFound && !overnightFound) {
			mLogger.debug(String.format("Requisition %1$d did not have any requested availability.", inputs.getNVStream().getInt(EMPLT_REQN_NBR)));
			mPhoneScreenReqSpecMinReq.setBypassDateTime(YES);
		} // end if (mPhnScrnReqSpecKnockOutQuestions != null && mPhnScrnReqSpecKnockOutQuestions.size() >0) {
	} // end function readHumanResourcesRequisitionDailyAvailableByInputList()	
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readRequisitionDetails
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readRequisitionDetails", operation = QueryMethod.getResult)
	public void readRequisitionDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one requisition
		if(results.next())
		{
			// if it hasn't been instantiated yet, instantiate it (could be used stand alone or along with other queries)
			if(mRequisitionDetails == null)
			{
				mRequisitionDetails = new RequisitionDetailTO();
			} // end if(mRequisitionDetails == null)

			mRequisitionDetails.setReqNbr(String.valueOf(results.getInt("employmentRequisitionNumber")));
			mRequisitionDetails.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
			mRequisitionDetails.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
			mRequisitionDetails.setJob(StringUtils.trim(results.getString("jobTitleCode")));
			mRequisitionDetails.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
			if(results.getBoolean("fullTimeRequiredFlag"))	{
				mRequisitionDetails.setFt(YES);
			} else	{
				mRequisitionDetails.setFt(NO);
			}

			if(results.getBoolean("partTimeRequiredFlag")) {
				mRequisitionDetails.setPt(YES);
			} else {
				mRequisitionDetails.setPt(NO);
			}
		} // end if(results.next())
	} // end function readRequisitionPhoneScreen()
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readRequisitionStaffingDetails
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readRequisitionStaffingDetails", operation = QueryMethod.getResult)
	public void readRequisitionStaffingDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one requisition
		if(results.next())
		{
			// if it hasn't been instantiated yet, instantiate it (could be used stand alone or along with other queries)
			if(mRequisitionDetails == null)
			{
				mRequisitionDetails = new RequisitionDetailTO();
			} // end if(mRequisitionDetails == null)

			if(!(results.wasNull(TARGET_PAY_AMOUNT)))
			{
				mRequisitionDetails.setHourlyRate(String.valueOf(results.getBigDecimal(TARGET_PAY_AMOUNT).setScale(2, RoundingMode.CEILING)));
			}
			
		} // end if(results.next())
	} // end function readRequisitionStaffingDetails()
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode", operation = QueryMethod.getResult)
	public void readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode(Results results, Query query, Inputs inputs) throws QueryException
	{
		// multiple records will be returned, see if a component of 3 is returned
		while (results.next())
		{
			// if it hasn't been instantiated yet, instantiate it (could be used stand alone or along with other queries)
			if(mRequisitionDetails == null)
			{
				mRequisitionDetails = new RequisitionDetailTO();
			} // end if(mRequisitionDetails == null)

			if (results.getInt(BGC_SYS_COMPONENT_ID) == MVR_COMPONENT_ID) {
				mRequisitionDetails.setDrivingPosition(true);
				break;
			} else {
				mRequisitionDetails.setDrivingPosition(false);
			}
		} // end while (results.next())
	} // end function readBackgroundJobRequirementByDepartmentStoreNumberandJobTitleCode()
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readHumanResourcesLocationDmvExemptionByInputList
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readHumanResourcesLocationDmvExemptionByInputList", operation = QueryMethod.getResult)
	public void readHumanResourcesLocationDmvExemptionByInputList(Results results, Query query, Inputs inputs) throws QueryException
	{
		mExemptDMVStores = new HashMap<String, String>();
		
		while (results.next())
		{
			mExemptDMVStores.put(StringUtils.trim(results.getString(HR_SYS_STR_NBR)), StringUtils.trim(results.getString(HR_SYS_STR_NBR)));
		} // end while (results.next())
	} // end function readHumanResourcesLocationDmvExemptionByInputList()
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readThdStoreEmploymentRequisition
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readThdStoreEmploymentRequisition", operation = QueryMethod.getResult)
	public void readThdStoreEmploymentRequisition(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one requisition
		if(results.next())
		{
			// if it hasn't been instantiated yet, instantiate it (could be used stand alone or along with other queries)
			if(mRequisitionDetails == null)
			{
				mRequisitionDetails = new RequisitionDetailTO();
			} // end if(mRequisitionDetails == null)

			if (results.getBoolean(RSC_SCHEDULE_FLG)) {
				mRequisitionDetails.setRscSchdFlg(YES);
			} else {
				mRequisitionDetails.setRscSchdFlg(NO);
			}
			
		} // end if(results.next())
	} // end function readThdStoreEmploymentRequisition()	
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readRemainingInterviewCandidateCountByPhoneScreenStatusCode
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readRemainingInterviewCandidateCountByPhoneScreenStatusCode", operation = QueryMethod.getResult)
	public void readPhoneScreenCandidateAndStoresRequisitionDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one record
		if(results.next())
		{
			mInterviewsStillNeeded = results.getInt(TOTAL);
			
		} // end if(results.next())
	} // end function readRemainingInterviewCandidateCountByPhoneScreenStatusCode()
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList", operation = QueryMethod.getResult)
	public void readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one record
		if(results.next())
		{
			mOffersStillNeeded = results.getShort(AUTH_POSITION_COUNT) - results.getInt(COUNT);
		} // end if(results.next())
	} // end function readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList()
	
	//Added for FMS 7894 IVR Change	
	/**
	 * Process the results returned by the readCountHumanResourcesRequisitionScheduleDetails
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readCountHumanResourcesRequisitionScheduleDetails", operation = QueryMethod.getResult)
	public void readCountHumanResourcesRequisitionScheduleDetails(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one record
		if(results.next())
		{
			mAvailableInterviewSlots = results.getInt(COUNT);
		} // end if(results.next())
	} // end function readCountHumanResourcesRequisitionScheduleDetails()	
	
	
	/**
	 * Process the results returned by the readMaximumCandidateContactStatusByInputList
	 * selector
	 * 
	 * @param results
	 *            Query results returned by the DAO framework
	 * @param query
	 *            DAO framework query object
	 * @param inputs
	 *            Inputs collection provided to the DAO framework for this
	 *            queries execution
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs using the DAO framework to
	 *             execute the query
	 */
	@QuerySelector(name = "readMaximumCandidateContactStatusByInputList", operation = QueryMethod.getResult)
	public void readMaximumCandidateContactStatusByInputList(Results results, Query query, Inputs inputs) throws QueryException
	{
		// there should only ever be one record
		if(results.next())
		{
			if (!results.wasNull(SEQUENCE_NUMBER)) {
				mMaxSeqNumPlusOne = results.getShort(SEQUENCE_NUMBER);
			} else {
				mMaxSeqNumPlusOne = 1;
			}
		} // end if(results.next())
	} // end function readMaximumCandidateContactStatusByInputList()
	
	@QuerySelector(name = "createCandidateContactStatus", operation = QueryMethod.insertObject)
	public void createCandidateContactStatus(Object arg0, boolean successFlag, int count, Query arg3, Inputs arg4) throws QueryException
	{
		resetStatus();
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(this + "Enter createCandidateContactStatus() status:" + successFlag);
		}
		mStatus = successFlag;
	}
	
	public int getmAffectedRecords() {
		return mAffectedRecords;
	}

	public void setmAffectedRecords(int mAffectedRecords) {
		this.mAffectedRecords = mAffectedRecords;
	}

	public List<PhoneScreenReqSpecMinReqTO> getPhnScrnReqSpecKnockOutQuestions() {
		return mPhnScrnReqSpecKnockOutQuestions;
	}

	public void setPhnScrnReqSpecKnockOutQuestions(
			List<PhoneScreenReqSpecMinReqTO> mPhnScrnReqSpecKnockOutQuestions) {
		this.mPhnScrnReqSpecKnockOutQuestions = mPhnScrnReqSpecKnockOutQuestions;
	}
	
	//Added for FMS 7894 IVR Change
	public RequisitionDetailTO getRequisitionDetails() {
		return mRequisitionDetails;
	}
	
	public void setRequisitionDetails(RequisitionDetailTO requisitionDetails) {
		this.mRequisitionDetails = requisitionDetails;
	}
	
	//Added for FMS 7894 IVR Change
	public Map<String, String> getExemptDMVStores() {
		return mExemptDMVStores;
	}
	
	public void setExemptDMVStores(Map<String, String> exemptDMVStores) {
		this.mExemptDMVStores = exemptDMVStores;
	}	
	
	//Added for FMS 7894 IVR Change
	public PhoneScreenReqSpecMinReqTO getPhoneScreenReqSpecMinReq() {
		return mPhoneScreenReqSpecMinReq;
	}
	
	public void setPhoneScreenReqSpecMinReq(PhoneScreenReqSpecMinReqTO phoneScreenReqSpecMinReq) {
		this.mPhoneScreenReqSpecMinReq = phoneScreenReqSpecMinReq;
	}	
	
	//Added for FMS 7894 IVR Change
	public int getInterviewsStillNeeded() {
		return mInterviewsStillNeeded;
	}
	
	public void setInterviewsStillNeeded(int interviewsStillNeeded) {
		this.mInterviewsStillNeeded = interviewsStillNeeded;
	}	
	
	//Added for FMS 7894 IVR Change
	public int getOffersStillNeeded() {
		return mOffersStillNeeded;
	}
	
	public void setOffersStillNeeded(int offersStillNeeded) {
		this.mOffersStillNeeded = offersStillNeeded;
	}	
	
	//Added for FMS 7894 IVR Change
	public String getAgentPhnScrnRequired() {
		return mAgentPhnScrnRequired;
	}
	
	public void setAgentPhnScrnRequired(String agentPhnScrnRequired) {
		this.mAgentPhnScrnRequired = agentPhnScrnRequired;
	}	
	
	//Added for FMS 7894 IVR Change
	public int getAvailableInterviewSlots() {
		return mAvailableInterviewSlots;
	}
	
	public void setAvailableInterviewSlots(int availableInterviewSlots) {
		this.mAvailableInterviewSlots = availableInterviewSlots;
	}	
	
	public short getMaxSeqNumPlusOne() {
		return mMaxSeqNumPlusOne;
	}
	
	public void setMaxSeqNumPlusOne(short maxSeqNumPlusOne) {
		this.mMaxSeqNumPlusOne = maxSeqNumPlusOne;
	}	
	
}

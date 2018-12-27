package com.homedepot.hr.hr.retailstaffing.dao.handlers;

import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.SsnXrefTO;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.annotations.AnnotatedQueryHandler;
import com.homedepot.ta.aa.dao.annotations.QueryMethod;
import com.homedepot.ta.aa.dao.annotations.QuerySelector;
import com.homedepot.ta.aa.dao.exceptions.InsertException;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * DAO AnnotatedQueryHandler containing call back methods used to process complex hiring events query results
 * 
 * @see AnnotatedQueryHandler
 */
public class CandidateDataFormHandler extends AnnotatedQueryHandler implements DAOConstants
{
	/** SSN Xref details object */
	private SsnXrefTO mSsnXref;
	
	/** Holds List of Xref details objects used for checking if duplicate SSN Exists */
	private List<SsnXrefTO> mSsnXrefList;
	
	
	@QuerySelector(name=READ_EAPLCNT_SSN_XREF, operation=QueryMethod.getResult)
	public void readEmploymentApplicantSocialSecurityNumberCrossReferenceByInputList(Results results, Query query, Inputs inputs) throws QueryException
	{
		//Clear because this handler is called multiple times from the same method
		mSsnXref = null;
		
		//When passing in a SSN there could be multiple records returned
		if(inputs != null && inputs.getNVStream() != null && inputs.getNVStream().containsName(CANDIDATE_SSN_NUMBER))
		{
			mSsnXrefList = new ArrayList<SsnXrefTO>();
			while (results.next())
			{
				// initialize the SsnXrefTO object
				mSsnXref = new SsnXrefTO();
				mSsnXref.setEmploymentApplicantId(StringUtils.trim(results.getString(EMPLT_APLCNT_ID)));
				mSsnXref.setApplicantSocialSecurityNumberNumber(StringUtils.trim(results.getString(CANDIDATE_SSN_NUMBER)));
				mSsnXref.setEffectiveBeginDate(results.getString(EFF_BEGIN_DATE));
				mSsnXref.setEffectiveEndDate(results.getString(EFF_END_DATE));
				mSsnXref.setLastUpdateSystemUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID))); 
				mSsnXref.setLastUpdateTimestamp(results.getString(LAST_UPD_TS));  
				mSsnXref.setActiveFlag(results.getString(ACTV_FLG));
				mSsnXrefList.add(mSsnXref);
			} // end while(results.next())
		} else {
			// there should only be a single record returned when the input is applicantId
			if(results.next())
			{
				// initialize the SsnXrefTO object
				mSsnXref = new SsnXrefTO();
				mSsnXref.setEmploymentApplicantId(StringUtils.trim(results.getString(EMPLT_APLCNT_ID)));
				mSsnXref.setApplicantSocialSecurityNumberNumber(StringUtils.trim(results.getString(CANDIDATE_SSN_NUMBER)));
				mSsnXref.setEffectiveBeginDate(results.getString(EFF_BEGIN_DATE));
				mSsnXref.setEffectiveEndDate(results.getString(EFF_END_DATE));
				mSsnXref.setLastUpdateSystemUserId(StringUtils.trim(results.getString(LAST_UPD_SYSUSR_ID))); 
				mSsnXref.setLastUpdateTimestamp(results.getString(LAST_UPD_TS));  
				mSsnXref.setActiveFlag(results.getString(ACTV_FLG));
			} // end if(results.next())
		} //End - if(inputs != null && inputs.getNVStream() != null && inputs.getNVStream().containsName(CANDIDATE_SSN_NUMBER))
		
	} // end function readEmploymentApplicantSocialSecurityNumberCrossReferenceByInputList()

	@QuerySelector(name = INSERT_EAPLCNT_SSN_XREF, operation = QueryMethod.insertObject)
	public void createEmploymentApplicantSocialSecurityNumberCrossReference(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		// if the correct number of records was not deleted
		if(!success)
		{
			throw new InsertException(String.format("An exception occurred inserting SSN record."));
		} // end if(!success)
				
	} // end function createEmploymentApplicantSocialSecurityNumberCrossReference()
	
	@QuerySelector(name = UPDATE_EAPLCNT_SSN_XREF, operation = QueryMethod.updateObject)
	public void updateEmploymentApplicantSocialSecurityNumberCrossReference(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
	{
		// if the correct number of records was not deleted
		if(!success)
		{
			throw new InsertException(String.format("An exception occurred inactivating SSN record."));
		} // end if(!success)
				
	} // end function updateEmploymentApplicantSocialSecurityNumberCrossReference()	
	
	
	public SsnXrefTO getmSsnXref() {
		return mSsnXref;
	}

	public void setmSsnXref(SsnXrefTO mSsnXref) {
		this.mSsnXref = mSsnXref;
	}

	public List<SsnXrefTO> getmSsnXrefList() {
		return mSsnXrefList;
	}

	public void setmSsnXrefList(List<SsnXrefTO> mSsnXrefList) {
		this.mSsnXrefList = mSsnXrefList;
	}
	

}

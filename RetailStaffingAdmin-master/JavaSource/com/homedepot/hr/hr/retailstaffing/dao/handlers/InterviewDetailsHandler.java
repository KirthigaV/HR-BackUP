package com.homedepot.hr.hr.retailstaffing.dao.handlers;

import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.InterviewDetailsTO;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

public class InterviewDetailsHandler implements ResultsReader
{
	private List<InterviewDetailsTO> mInterviews;

	public InterviewDetailsHandler()
	{
		mInterviews = new ArrayList<InterviewDetailsTO>();
	} // end constructor
	
	public void readResults(Results results, Query query, Inputs inputs) throws QueryException
	{
		// if results were returned
		if(results != null)
		{
			InterviewDetailsTO interview = null;
			
			// iterate the results
			while(results.next())
			{
				// create a new interview details object
				interview = new InterviewDetailsTO();
				// populate the interview object
				interview.setPhoneScreenId(results.getInt("humanResourcesPhoneScreenId"));
				interview.setStoreNumber(results.getString("humanResourcesSystemStoreNumber"));
				interview.setEmplPosnCandId(results.getString("employmentPositionCandidateId"));
				interview.setEmplReqNbr(results.getInt("employmentRequisitionNumber"));
				interview.setCandPersonId(results.getString("candidatePersonId"));
				interview.setCandName(results.getString("candidateName"));
				interview.setCandPhoneNbr(results.getString("candidatePhoneNumber"));
				interview.setLocTypCd(results.getShort("interviewLocationTypeCode"));
				interview.setName(results.getString("interviewerName"));
				interview.setDate(results.getDate("interviewDate"));
				interview.setTimestamp(results.getTimestamp("interviewTimestamp"));
				interview.setLocDesc(results.getString("interviewLocationDescription"));
				interview.setPhoneNbr(results.getString("interviewPhoneNumber"));
				interview.setAddress(results.getString("interviewAddressText"));
				interview.setCity(results.getString("interviewCityName"));
				interview.setStateCd(results.getString("interviewStateCode"));
				interview.setZipCd(results.getString("interviewZipCodeCode"));
				interview.setActvFlg(results.getString("activeFlag"));
				interview.setInterviewMins(results.getShort("interviewMinutes"));
				// add it to the interview collection
				mInterviews.add(interview);
			} // end while(results.next())
		} // end if(results != null)
	} // end function readResults()
	
	public List<InterviewDetailsTO> getInterviews()
	{
		return mInterviews;
	} // end function getInterviews()
}

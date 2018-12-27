package com.homedepot.hr.hr.retailstaffing.dao.handlers;

import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

public class RequisitionScheduleHandler implements ResultsReader 
{
	private List<RequisitionScheduleTO> mSchedules;
	
	public RequisitionScheduleHandler()
	{
		mSchedules = new ArrayList<RequisitionScheduleTO>();
	} // end constructor
	
	public void readResults(Results results, Query query, Inputs inputs) throws QueryException
    {
		RequisitionScheduleTO dto = null;
		
		// iterate the results and populate a new RequisitionScheduleTO object for each record returned
		while(results.next())
		{
			dto = new RequisitionScheduleTO();
			dto.setCreateTimestamp(results.getTimestamp("createTimestamp"));
			dto.setCreateSystemUserId(results.getString("createSystemUserId"));
			dto.setLastUpdateSystemUserId(results.getString("lastUpdateSystemUserId"));
			dto.setLastUpdateTimestamp(results.getTimestamp("lastUpdateTimestamp"));
			dto.setBeginTimestamp(results.getTimestamp("beginTimestamp"));
			dto.setEndTimestamp(results.getTimestamp("endTimestamp"));
			dto.setHumanResourcesSystemStoreNumber(results.getString("humanResourcesSystemStoreNumber"));
			dto.setInterviewerAvailabilityCount(results.getShort("interviewerAvailabilityCount"));
			
			// add this schedule to the list
			mSchedules.add(dto);
		} // end while(results.next())
    } // end function readResults()
	
	public List<RequisitionScheduleTO> getRequisitionSchedules()
	{
		return mSchedules;
	} // end function getRequisitionSchedules()
}

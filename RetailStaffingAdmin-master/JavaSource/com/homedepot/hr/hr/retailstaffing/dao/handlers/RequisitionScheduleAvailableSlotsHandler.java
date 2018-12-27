package com.homedepot.hr.hr.retailstaffing.dao.handlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.InterviewAvaliableSlotsTO;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

public class RequisitionScheduleAvailableSlotsHandler implements ResultsReader {
	
   List<InterviewAvaliableSlotsTO> interviewAvaliableSlotsList = null;
   private  final Logger mLogger = Logger.getLogger(RequisitionScheduleAvailableSlotsHandler.class);
	
	public RequisitionScheduleAvailableSlotsHandler()
	{
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(this + "Enter RequisitionScheduleAvailableSlotsHandler: RequisitionScheduleAvailableSlotsHandler()");
		}
		interviewAvaliableSlotsList = new ArrayList<InterviewAvaliableSlotsTO>();
	}	
	
	public void readResults(Results results, Query query, Inputs inputs) throws QueryException
    {
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(this + "Enter RequisitionScheduleAvailableSlotsHandler : readResults()");
		}
		
		InterviewAvaliableSlotsTO interviewAvaliableSlotsTO = null;
		// iterate the results and populate a new RequisitionCalendarTO object for each record returned
		while (results.next()) {
			interviewAvaliableSlotsTO = new InterviewAvaliableSlotsTO();
			interviewAvaliableSlotsTO.setBeginTimestamp(results
						.getTimestamp("beginTimestamp"));
			interviewAvaliableSlotsTO.setSequenceNumber(results
						.getShort("sequenceNumber"));
			interviewAvaliableSlotsTO
						.setStoreNumber(results
								.getString("humanResourcesSystemStoreNumber"));
			interviewAvaliableSlotsTO
						.setReservedDateTime(results
								.getTimestamp("requisitionScheduleReserveTimestamp"));
			interviewAvaliableSlotsList
						.add(interviewAvaliableSlotsTO);
		}
				
		if (mLogger.isDebugEnabled()) {
			mLogger.debug("interviewAvaliableSlotsList size -->" + interviewAvaliableSlotsList.size());
			mLogger.debug(this + "Exit RequisitionScheduleAvailableSlotsHandler : readResults()");
		}
		
    } // end function readResults()
	
	
	public List<InterviewAvaliableSlotsTO> getInterviewAvaliableSlotsList()
	{
		if (mLogger.isDebugEnabled()) {
			mLogger.debug(this + "Enter RequisitionScheduleAvailableSlotsHandler : getInterviewAvaliableSlotsList()");
		}
		return interviewAvaliableSlotsList;
	}

}

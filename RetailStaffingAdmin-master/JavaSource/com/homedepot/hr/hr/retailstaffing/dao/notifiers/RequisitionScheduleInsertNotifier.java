package com.homedepot.hr.hr.retailstaffing.dao.notifiers;

import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.InsertNotifier;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

public class RequisitionScheduleInsertNotifier implements InsertNotifier{
	
	public void notifyInsert(Object target, boolean success, int count, Query query, Inputs inputs) throws QueryException
    {
		// if exactly one row was not inserted, throw an exception
		if(count != 1)
		{
			throw new QueryException("An exception occurred inserting requisition calendar, insert count != 1");
		} // end if(count != 1)
    } // end function notifyInsert()

}

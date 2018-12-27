package com.homedepot.hr.hr.retailstaffing.dao.handlers;

import org.apache.log4j.Logger;


import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.ObjectReader;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

public class RequisitionScheduleExistenceHandler implements ObjectReader
{
	private boolean mExists;
	private static final Logger mLogger = Logger.getLogger(RequisitionScheduleExistenceHandler.class);	
	
	public void readObject(Object target, Query query, Inputs inputs) throws QueryException
    {
		if(target != null)
		{
			mLogger.info("target value -->" + target.toString());
			mExists = Boolean.valueOf(target.toString());
		}
		else
		{
			mLogger.info("target value -->" + null);
		}
		//mExists = (target != null);
    } // end function readObject()
	
	public boolean exists()
	{
		return mExists;
	} // end function exists()
}

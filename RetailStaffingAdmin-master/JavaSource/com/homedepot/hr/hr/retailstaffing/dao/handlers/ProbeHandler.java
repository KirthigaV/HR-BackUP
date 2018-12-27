package com.homedepot.hr.hr.retailstaffing.dao.handlers;

/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, N.W.
 *    Atlanta, GA 30339-4024
 *
 */
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.ObjectReader;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * Callback handler used to process results returned from the
 * DAO library selector "readHumanResourcesPhoneScreenForExistence". This
 * DAO method queries SYSTABLES to see if the HR_PHN_SCRN table exists
 * and is used by the application probe.
 *
 * @author rlp05
 */
public class ProbeHandler implements ObjectReader
{
	/** True if the table is available (query worked, false otherwise) */
	private boolean mAvailable;

	/**
	 * Method reads the results and populates the mAvailable member variable
	 *
	 * @param		results			Results to be processed
	 * @param		query			Query that was executed to get results
	 * @param		inputs			Inputs provided to the query
	 *
	 * @exception	QueryException	Thrown whenever an exception occurs processing
	 * 								the results
	 */
	public void readObject(Object target, Query query, Inputs inputs) throws QueryException
    {
		// true and false are both valid responses, as long as I didn't take an error return true
		mAvailable = true;
    } // end function readObject()

	/**
	 * Get the mAvailable member variable populated by the callback handler
	 *
	 * @return		True if the query worked and the table exists, false otherwise
	 */
	public boolean isAvailable()
	{
		return mAvailable;
	} // end function isAvailable()
} // end class ProbeHandler
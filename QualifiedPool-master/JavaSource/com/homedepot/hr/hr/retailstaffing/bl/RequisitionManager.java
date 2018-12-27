package com.homedepot.hr.hr.retailstaffing.bl;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionManager.java
 * Application: RetailStaffing
 */
import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.RequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.dto.Requisition;
import com.homedepot.hr.hr.retailstaffing.exception.QualifiedPoolException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.QualifiedPoolMessage;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains business logic methods related to employment requisitions
 * 
 * @author rlp05
 */
public class RequisitionManager implements Constants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(RequisitionManager.class);
	
	/**
	 * Get the employment requisition for the identifier provided
	 * 
	 * @param requisitionNbr				Unique identifier for the employment requisition
	 * 
	 * @return								Employment requisition for the identifier provided
	 * 
	 * @throws QualifiedPoolException		Thrown if any of the following conditions are true:
	 * 										<ul>
	 * 											<li>The requisition number provided is &lt; 1</li>
	 * 											<li>An exception occurs querying the database for the requisition</li>
	 * 										<ul>
	 */
	public static Requisition getRequisition(int requisitionNbr) throws QualifiedPoolException
	{		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Entering getRequisition(), requisitionNbr: %1$d", requisitionNbr));
		} // end if
		Requisition requisition = null;
				
		try
		{
			// first validate the requisition number provided is valid
			if(requisitionNbr < 1)
			{
				// throw the exception
				throw new QualifiedPoolException(ERRCD_INVALID_REQNBR, String.format("Invalid requisition number %1$d provided", requisitionNbr));
			} // end if(requisitionNbr < 1)		

			// invoke the DAO method to get the requisition data
			requisition = RequisitionDAO.getRequisition(requisitionNbr);
		} // end try
		catch(QualifiedPoolException qpe)
		{
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(qpe.getMessage()));
			// make sure the error shows up in the log
			mLogger.error("An exception occurred validating input data", qpe);
			// throw the exception
			throw qpe;
		} // end catch
		catch(QueryException qe)
		{
			// log a warning to APPL_LOG
			mLogger.warn(new QualifiedPoolMessage(String.format("An exception occurred querying for requisition %1$d", requisitionNbr)));
			// make sure the error details show up in the log
			mLogger.error(String.format("An exception occurred querying for requisition %1$d", requisitionNbr), qe);
			// throw the exception
			throw new QualifiedPoolException(ERRCD_DATA_ACCESS, qe);
		} // end catch		
		
		if(mLogger.isDebugEnabled())
		{
			mLogger.debug(String.format("Exiting getRequisition(), requisitionNbr: %1$d", requisitionNbr));
		} // end if

		// return the Requisition object populated by the DAO call back
		return requisition;		
	} // end function getRequisition()
} // end class RequisitionManager
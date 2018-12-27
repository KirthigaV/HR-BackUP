package com.homedepot.hr.hr.retailstaffing.exceptions;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: NoRowsFoundException.java
 * Application: RetailStaffing
 */
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class is used throughout the application whenever queries do not return
 * any rows.
 */
public class NoRowsFoundException extends QueryException
{
    private static final long serialVersionUID = 1184454134513826302L;
    
	/** application object enumerated type that can be used to determine which object was not found */
    private ApplicationObject mApplicationObject;

    /**
     * constructor
     * 
     * @param appObject application object enumerated type that can be used to determine which object was not found
     * @param message exception message
     * 
     * @see QueryException#QueryException(String)
     */
    public NoRowsFoundException(ApplicationObject appObject, String message)
    {
    	super(message);
    	mApplicationObject = appObject;
    } // end constructor()
    
    /**
     * constructor
     * 
     * @param appObject application object enumerated type that can be used to determine which object was not found
     * @param message exception message
     * @param cause the cause
     */
    public NoRowsFoundException(ApplicationObject appObject, String message, Throwable cause)
    {
    	super(message, cause);
    	mApplicationObject = appObject;
    } // end constructor()
    
    /**
     * @return application object enumerated type that can be used to determine which object was not found
     */
    public ApplicationObject getApplicationObject()
    {
    	return mApplicationObject;
    } // end function getApplicationObject()
} // end class NoRowsFoundException
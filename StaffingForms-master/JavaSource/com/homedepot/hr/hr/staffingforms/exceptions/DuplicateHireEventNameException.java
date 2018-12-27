package com.homedepot.hr.hr.staffingforms.exceptions;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ValidationException.java
 * Application: RetailStaffing
 */
import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class is used to raise duplicate hiring event name exception
 */
public class DuplicateHireEventNameException extends QueryException
{    
	private static final long serialVersionUID = 228685675421200736L;
	/** input field enumerated type containing details about the field that failed validation */
    private InputField mInputField;

    /**
     * @param inputField input field enumerated type
     * @param message validation exception message
     */
	public DuplicateHireEventNameException(InputField inputField, String message)
	{
		super(message);
		mInputField = inputField;
	} // end constructor()
	
	/**
	 * @return input field enumerated type containing details about the field that failed validation
	 */
	public InputField getInputField()
	{
		return mInputField;
	} // end function getInputField()
} // end class DuplicateHireEventNameException
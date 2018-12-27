package com.homedepot.hr.hr.retailstaffing.utils;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ValidationUtils.java
 * Application: RetailStaffing
 */

import java.util.Calendar;
import java.util.Date;

import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.interfaces.ValidationConstants;

/**
 * This class contains convenience methods used for validation throughout
 * the application
 */
public class ValidationUtils implements ValidationConstants
{
	/* threshold value for phone screen id */
	private static final short VALID_THRESHOLD_PHONE_SCREEN_ID = 0;
	/* threshold value for requisition number */
	private static final int VALID_THRESHOLD_REQUISITION_NUMBER = 0;
	/* threshold value for a requisition calendar id */
	private static final int VALID_THRESHOLD_REQUISITION_CALENDAR_ID = -1;
	/* threshold value for status codes */
	public static final short VALID_THRESHOLD_STATUS_CODE = 0;	
	
	/* Regular expression used to validate store number */
	private static final String VALID_REGEX_STORE_NBR = "[0-9]{4}";
	
	/**
	 * Convenience method used to validate a version number is supported
	 * 
	 * @param version the version to validate
	 * @param supportedVersions the version(s) supported
	 * 
	 * @throws IllegalArgumentException thrown if no supported versions were provided
	 * @throws ValidationException thrown if the version provided is not supported (a.k.a.
	 * 							   is not one of the supported versions provided)
	 */
	public static void validateVersion(int version, int ... supportedVersions) throws IllegalArgumentException, ValidationException
	{
		// validate that at least one supported version was provided
		if(supportedVersions.length == 0)
		{
			throw new IllegalArgumentException("At least one supported version must be provided");
		} // end if(supportedVersions.length == 0)
		
		boolean supported = false;
		int versionCounter = 0;
		
		do
		{
			/*
			 * Compare the version provided to each version in the supported 
			 * versions array UNTIL the version provided is found to be supported
			 * or we have iterated through each supported version in the array
			 */
			supported = (version == supportedVersions[versionCounter++]);
		} // end do
		while(!supported && (versionCounter < supportedVersions.length));
		
		// if the version provided is not supported, throw an exception
		if(!supported)
		{		
			throw new ValidationException(InputField.VERSION_NUMBER, String.format("%1$s %2$s is not supported", InputField.VERSION_NUMBER.getFieldLabel(), version));
		} // end if(!supported)
	} // end function validateVersion()	
	
	/**
	 * Convenience method to validate a status code
	 * 
	 * @param toValidate the status code to validate
	 * 
	 * @throws ValidationException thrown if the status code provided is &lt;= 0
	 */
	public static void validateStatusCode(short toValidate) throws ValidationException
	{
		validateGreaterThan(InputField.STATUS_CODE, toValidate, VALID_THRESHOLD_STATUS_CODE);
	} // end function validateStatusCode()
	
	/**
	 * Convenience method to validate a phone screen id
	 * 
	 * @param toValidate the status code to validate
	 * 
	 * @throws ValidationException thrown if the phone screen id provided is &lt;= 0
	 */	
	public static void validatePhoneScreenId(int toValidate) throws ValidationException
	{
		validateGreaterThan(InputField.PHONE_SCREEN_ID, toValidate, VALID_THRESHOLD_PHONE_SCREEN_ID);
	} // end function validatePhoneScreenId()
	
	/**
	 * Convenience method to validate a language code
	 * 
	 * @param toValidate the language code to validate
	 * 
	 * @throws ValidationException thrown if the language code provided is null, empty,
	 * 							   or does not match the pattern <i>ll_cc (language_country)</i>
	 */
	public static void validateLanguageCode(String toValidate) throws ValidationException
	{
		validateUsingRegex(InputField.LANGUAGE_CODE, toValidate, VALID_REGEX_LANG_CD, false);
	} // end function validateLanguageCode()
	
	/**
	 * Convenience method to validate a requisition number
	 * 
	 * @param toValidate the requisition number to validate
	 * 
	 * @throws ValidationException thrown if the requisition number is &lt;= 0
	 */
	public static void validateRequisitionNumber(int toValidate) throws ValidationException
	{
		validateGreaterThan(InputField.REQUISITION_NUMBER, toValidate, VALID_THRESHOLD_REQUISITION_NUMBER);
	} // end function validateRequisitionNumber()
	
	/**
	 * Convenience method to validate a store number
	 * 
	 * @param toValidate the store number to validate
	 * 
	 * @throws ValidationException throw if the store number is null, empty, or not exactly four digit characters
	 */
	public static void validateStoreNumber(String toValidate) throws ValidationException
	{
		validateUsingRegex(InputField.STORE_NUMBER, toValidate, VALID_REGEX_STORE_NBR, false);
	} // end function validateStoreNumber()
	
	/**
	 * Convenience method to validate a requisition calendar id
	 * 
	 * @param toValidate the requisition calendar id to validate
	 * @throws ValidationException thrown if the requisition calendar id provided is &lt;= 0
	 */
	public static void validateRequisitionCalendarId(int toValidate) throws ValidationException
	{
		validateGreaterThan(InputField.REQUISITION_CALENDAR_ID, toValidate, VALID_THRESHOLD_REQUISITION_CALENDAR_ID);
	} // end function validateRequisitionCalendarId()
	
	/**
	 * Convenience method to validate a String object is not null
	 * 
	 * @param field							Info about the field being validated
	 * @param toValidate					Value to evaluate
	 * 
	 * @throws IllegalArgumentException 	Thrown if the InputField provided is null
	 * @throws ValidationException			Thrown if the String object provided is null
	 */
	public static void validateNotNull(InputField field, Object toValidate) throws ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
		
		// validate the Object provided is not null
		if(toValidate == null)
		{
			throw new ValidationException(field, String.format("Null %1$s provided", field.getFieldLabel()));
		} // end if(toValidate == null)
	} // end function validateNotNull()
	
	/**
	 * Convenience method to validate a short field value is greater than some threshold
	 * 
	 * @param field							Info about the field being validated
	 * @param toCompare						Value to compare
	 * @param threshold						Threshold the value provided must be greater than
	 * 
	 * @throws IllegalArgumentException 	Thrown if the InputField provided is null
	 * @throws ValidationException			Thrown if the value provided is <= the threshold value provided
	 */
	public static void validateGreaterThan(InputField field, short toCompare, short threshold) throws ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
				
		// if the field to compare is <= threshold, it's an error
		if(toCompare <= threshold)
		{
			throw new ValidationException(field, String.format("Invalid %1$s provided. %1$s must be greater than %2$d", field.getFieldLabel(), threshold));
		} // end if(toCompare <= threshold)
	} // end function validateGreaterThan()	
	
	/**
	 * Convenience method to validate an int field value is greater than some threshold
	 * 
	 * @param field							Info about the field being validated
	 * @param toCompare						Value to compare
	 * @param threshold						Threshold the value provided must be greater than
	 * 
	 * @throws IllegalArgumentException 	Thrown if the InputField provided is null
	 * @throws ValidationException			Thrown if the value provided is <= the threshold value provided
	 */
	public static void validateGreaterThan(InputField field, int toCompare, int threshold) throws ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
				
		// if the field to compare is <= threshold, it's an error
		if(toCompare <= threshold)
		{
			throw new ValidationException(field, String.format("Invalid %1$s provided. %1$s must be greater than %2$d", field.getFieldLabel(), threshold));
		} // end if(toCompare <= threshold)
	} // end function validateGreaterThan()		
		
	/**
	 * Convenience method to validate a String object is not null or empty
	 * 
	 * @param field							Info about the field being validated
	 * @param toValidate					String object to evaluate
	 * 
	 * @throws IllegalArgumentException 	Thrown if the InputField provided is null
	 * @throws ValidationException			Thrown if the String object provided is null or empty
	 */
	public static void validateNotNullOrEmpty(InputField field, String toValidate) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
		
		// validate the String object provided is not null
		validateNotNull(field, toValidate);
		// validate the String object provided is not empty
		if(toValidate.trim().length() == 0)
		{
			throw new ValidationException(field, String.format("Empty %1$s provided", field.getFieldLabel()));
		} // end if(toValidate.trim().length() == 0)
	} // end function validateNotNullOrEmpty()
	
	/**
	 * Convenience method to validate an Object[] is not null or empty
	 * 
	 * @param field							Info about the field being validated
	 * @param toValidate					Object[] to evaluate
	 * 
	 * @throws IllegalArgumentException		Thrown if the InputField provided is null
	 * @throws ValidationException			Thrown if the Object[] provided is null or empty
	 */
	public static void validateNotNullOrEmpty(InputField field, Object[] toValidate) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
		
		// validate the object array provided is not null
		validateNotNull(field, toValidate);
		// validate the array is not empty
		if(toValidate.length == 0)
		{
			throw new ValidationException(field, String.format("Empty %1$s provided", field.getFieldLabel()));
		} // end if(toValidate.length == 0)
	} // end function validateNotNullOrEmpty()
	
	/**
	 * Convenience method to validate an byte[] is not null or empty
	 * 
	 * @param field							Info about the field being validated
	 * @param toValidate					byte[] to evaluate
	 * 
	 * @throws IllegalArgumentException		Thrown if the InputField provided is null
	 * @throws ValidationException			Thrown if the byte[] provided is null or empty
	 */
	public static void validateNotNullOrEmpty(InputField field, byte[] toValidate) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
		
		// validate the object array provided is not null
		validateNotNull(field, toValidate);
		// validate the array is not empty
		if(toValidate.length == 0)
		{
			throw new ValidationException(field, String.format("Empty %1$s provided", field.getFieldLabel()));
		} // end if(toValidate.length == 0)
	} // end function validateNotNullOrEmpty()	
	
	/**
	 * Convenience method to validate a String object using a regular expression pattern
	 * 
	 * @param field							Info about the field being validated
	 * @param toValidate					String object to evaluate
	 * @param regexPattern					Regular Expression pattern to apply
	 * @param maskValue						true if the value provided should be masked in the Exception stack
	 * 										that may be generated, false otherwise
	 * 
	 * @throws IllegalArgumentException		Thrown if the regular expression pattern provided is null or empty
	 * 										of if the InputField provided is null
	 * 
	 * @throws ValidationException			Thrown if the String object provided is null, empty, or does not
	 * 										match the pattern provided
	 */
	public static void validateUsingRegex(InputField field, String toValidate, String regexPattern, boolean maskValue) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
		
		// validate the regular expression pattern was provided
		if(regexPattern == null || regexPattern.trim().length() == 0)
		{
			throw new IllegalArgumentException("A regular expression pattern must be provided");
		} // end if(regexPattern == null || regexPattern.trim().length() == 0)
		
		// next validate the input field provided is not null or empty
		validateNotNullOrEmpty(field, toValidate);
		
		// finally validate the input field using the pattern provided
		if(!toValidate.trim().matches(regexPattern))
		{
			if(maskValue)
			{
				throw new ValidationException(field, String.format("Invalid %1$s %2$s provided, %1$s must match pattern %3$s",
					field.getFieldLabel(), StringUtils.mask(toValidate), regexPattern));
			} // end if(maskValue)
			else
			{
				throw new ValidationException(field, String.format("Invalid %1$s %2$s provided, %1$s must match pattern %3$s",
					field.getFieldLabel(), toValidate, regexPattern));				
			} // end else
		} // end if(!toValidate.trim().matches(regexPattern))		
	} // end function validateUsingRegex()	
     
	/**
	 * Convenience method to validate a Date object is not null, empty or prior to today's date
	 * 
	 * @param field							Info about the field being validated
	 * @param toValidate					Date object to evaluate
	 * 
	 * @throws IllegalArgumentException		Thrown if the InputField provided is null
	 * 
	 * @throws ValidationException			Thrown if the Date object provided is null, empty, or is prior to today's date
	 */	
 	public static void validateNotPriorToToday(InputField field, Date date) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
		
		// validate the date provided is not null
		validateNotNull(field, date);

		// get today
		Calendar today = Calendar.getInstance();
		// initialize to the earliest possible time today
		today.set(Calendar.HOUR_OF_DAY, today.getActualMinimum(Calendar.HOUR_OF_DAY));
		today.set(Calendar.MINUTE, today.getActualMinimum(Calendar.MINUTE));
		today.set(Calendar.SECOND, today.getActualMinimum(Calendar.SECOND));
		today.set(Calendar.MILLISECOND, today.getActualMinimum(Calendar.MILLISECOND));
		// check if it's prior to today
		if(date.before(today.getTime()))
		{
			throw new ValidationException(field, String.format("Invalid %1$s %2$s provided, %1$s cannot be prior to %3$s", field.getFieldLabel(), date.toString(), 
				today.getTime().toString()));
		} // end if(date.before(today.getTime()))
	} // end function validateNotPriorToToday()
} // end class ValidationUtils
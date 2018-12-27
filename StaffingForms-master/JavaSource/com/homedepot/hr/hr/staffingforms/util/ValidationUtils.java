package com.homedepot.hr.hr.staffingforms.util;
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
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.hr.hr.staffingforms.exceptions.HiringEventException;
import com.homedepot.hr.hr.staffingforms.exceptions.ValidationException;

/**
 * This class contains convenience methods used for validation throughout
 * the application
 */
public class ValidationUtils
{
	
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(ValidationUtils.class);
	
	/* threshold value calendar id's must exceed */
	private static final int CALENDAR_ID_THREHSOLD = 0;
	
	/* threshold value hiring event id's must exceed */
	private static final int HIRE_EVENT_ID_THREHSOLD = 0;
	
	/* threshold value Phone Screen Number must exceed */
	private static final int PHONE_SCREEN_THREHSOLD = 0;
	
	/* Regular expression used to validate store number */
	private static final String VALID_REGEX_STORE_NBR = "[0-9]{4}";
	
	/* language code validation regular expression pattern */
	public static final String VALID_REGEX_LANG_CD = "[a-zA-Z]{2}_[a-zA-Z]{2}";	
		
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
	 * Convenience method to validate a calendar id
	 * 
	 * @param toValidate the calendar id to validate
	 * 
	 * @throws ValidationException thrown if the calendar id provided is not &gt; 0
	 */
	public static void validateCalendarId(int toValidate) throws ValidationException
	{
		validateGreaterThan(InputField.CALENDAR_ID, toValidate, CALENDAR_ID_THREHSOLD);
	} // end function validateCalendarId()
	
	/**
	 * Convenience method to validate a hiring event id
	 * 
	 * @param toValidate the hiring event id to validate
	 * 
	 * @throws ValidationException thrown if the hiring event id provided is not &gt; 0
	 */
	public static void validateHiringEventId(int toValidate) throws ValidationException
	{
		validateGreaterThan(InputField.HIRE_EVENT_ID, toValidate, HIRE_EVENT_ID_THREHSOLD);
	} // end function validateHiringEventId()
	
	/**
	 * Convenience method to validate a Phone Screen Number
	 * 
	 * @param toValidate the Phone Screen Number to validate
	 * 
	 * @throws ValidationException thrown if the Phone Screen provided is not &gt; 0
	 */
	public static void validatePhoneScrnNum(long toValidate) throws ValidationException
	{
		validateGreaterThan(InputField.PHONE_SCREEN_NUM, toValidate, CALENDAR_ID_THREHSOLD);
	} // end function validatePhoneScrnNum()
	
	/**
	 * Convenience method to validate an int is greater than a threshold
	 * 
	 * @param field Info about the field being validated
	 * @param toValidate the int to validate
	 * @param threshold the threshold to validate against
	 * 
	 * @throws IllegalArgumentException thrown if the field provided is null
	 * @throws ValidationException thrown if the int provided is not greater than the threshold provided
	 */
	public static void validateGreaterThan(InputField field, long toValidate, long threshold) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)

		// validate the int provided is greater than the threshold
		if(toValidate <= threshold)
		{
			throw new ValidationException(field, String.format("Invalid %1$s %2$d provided. %1$s must be greater than %3$d", 
				field.getFieldLabel(), toValidate, threshold));
		} // end if(toValidate <= threshold)
	} // end function validateGreaterThan()
	
	/**
	 * Convenience method to validate an int is less than a threshold
	 * 
	 * @param field Info about the field being validated
	 * @param toValidate the int to validate
	 * @param threshold the threshold to validate against
	 * 
	 * @throws IllegalArgumentException thrown if the field provided is null
	 * @throws ValidationException thrown if the int provided is not less than the threshold provided
	 */
	public static void validateLessThan(InputField field, int toValidate, int threshold) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)

		// validate the int provided is greater than the threshold
		if(toValidate > threshold)
		{
			throw new ValidationException(field, String.format("Invalid %1$s %2$d provided. %1$s must be less than %3$d", 
				field.getFieldLabel(), toValidate, threshold));
		} // end if(toValidate > threshold)
	} // end function validateLessThan()
	
	/**
	 * Convenience method to validate a short value is within a range (inclusive)
	 * 
	 * @param field info about the field being validated
	 * @param toValidate the short being validated
	 * @param minThreshold the min threshold (inclusive) of the range
	 * @param maxThreshold the max threshold (inclusive) of the range
	 * 
	 * @throws IllegalArgumentException thrown if the field provided is null
	 * @throws ValidationException thrown if the short value is less than minThreshold or greater than maxThreshold
	 */
	public static void validateInRange(InputField field, short toValidate, int minThreshold, int maxThreshold) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)

		// validate minThreshold >= toValidate <= maxThreshold
		if(toValidate < minThreshold || toValidate > maxThreshold)
		{
			throw new ValidationException(field, String.format("Invalid %1$s %2$d provided. %1$s must be >= %3$d and <= %4$d",
				field.getFieldLabel(), toValidate, minThreshold, maxThreshold));
		} // end if(toValidate < minThreshold || toValidate > maxThreshold)
	} // end function validateInRange()
	
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
	 * Convenience method to validate a collection object is not null or empty
	 * 
	 * @param field info about the field being validated
	 * @param toValidate collection object to validate
	 * 
	 * @throws IllegalArgumentException thrown if the InputField provided is null
	 * @throws ValidationException Thrown if the collection object provided is null or empty
	 */
	public static void validateNotNullOrEmpty(InputField field, Collection<?> toValidate) throws IllegalArgumentException, ValidationException
	{
		// validate the InputField provided is not null
		if(field == null)
		{
			throw new IllegalArgumentException("An InputField must be provided");
		} // end if(field == null)
		
		// validate the list provided is not null
		validateNotNull(field, toValidate);
		// validate the list provided is not empty
		if(toValidate.isEmpty())
		{
			throw new ValidationException(field, String.format("Empty %1$s provided", field.getFieldLabel()));
		} // end if(toValidate.isEmpty())
	} // end function validateNotNullOrEmpty()
	
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
	 * Convenience method to validate one date is before another date
	 * 
	 * @param field1 info about the first date
	 * @param toValidate the date that must be earliest
	 * @param field2 info about the second date
	 * @param toValidateAgainst the date that must be later
	 * 
	 * @throws IllegalArgumentException thrown if either InputField is null
	 * @throws ValidationException thrown if either Date provided is null, or if toValidate is not before toValidateAgainst
	 */
	public static void validateBefore(InputField field1, Date toValidate, InputField field2, Date toValidateAgainst) throws IllegalArgumentException, ValidationException
	{
 		// validate both InputField provided are not null
 		if(field1 == null || field2 == null)
 		{
 			throw new IllegalArgumentException(String.format("InputField%1$d must be provided", (field1 == null ? 1 : 2)));
 		} // end if(field1 == null || field2 == null)

 		// validate the first date is not null
 		validateNotNull(field1, toValidate);
 		// validate the second date is not null
 		validateNotNull(field2, toValidateAgainst);
 		
 		// convert both dates to calendar objects so the time portions can be cleared (basically ignored by the compare)
 		Calendar cal1 = Calendar.getInstance();
 		cal1.setTime(toValidate);
 		cal1.set(Calendar.HOUR_OF_DAY, cal1.getActualMinimum(Calendar.HOUR_OF_DAY));
 		cal1.set(Calendar.MINUTE, cal1.getActualMinimum(Calendar.MINUTE));
 		cal1.set(Calendar.SECOND, cal1.getActualMinimum(Calendar.SECOND));
 		cal1.set(Calendar.MILLISECOND, cal1.getActualMinimum(Calendar.MILLISECOND));
 		
 		Calendar cal2 = Calendar.getInstance();
 		cal2.setTime(toValidateAgainst);
 		cal2.set(Calendar.HOUR_OF_DAY, cal1.getActualMinimum(Calendar.HOUR_OF_DAY));
 		cal2.set(Calendar.MINUTE, cal1.getActualMinimum(Calendar.MINUTE));
 		cal2.set(Calendar.SECOND, cal1.getActualMinimum(Calendar.SECOND));
 		cal2.set(Calendar.MILLISECOND, cal1.getActualMinimum(Calendar.MILLISECOND)); 	
 		
 		// if toValidate is not before toValidateAgainst
 		if(!cal1.before(cal2))
 		{
 			throw new ValidationException(field2, String.format("Invalid %1$s %2$s provided, %1$s must be before %3$s.", field1.getFieldLabel(), toValidate, field2.getFieldLabel()));
 		} // end if(!cal1.before(cal2))
	} // end function validateBefore()
	
	/**
	 * Convenience method to validate one timestamp is before another timestamp
	 * 
	 * @param field1 info about the first date
	 * @param toValidate the timestamp that must be earliest
	 * @param field2 info about the second date
	 * @param toValidateAgainst the timestamp that must be later
	 * 
	 * @throws IllegalArgumentException thrown if either InputField is null
	 * @throws ValidationException thrown if either Timestamp provided is null, or if toValidate is not before toValidateAgainst
	 */	
	public static void validateTimestampBefore(InputField field1, Timestamp toValidate, InputField field2, Timestamp toValidateAgainst) throws IllegalArgumentException, ValidationException
	{
 		// validate both InputField provided are not null
 		if(field1 == null || field2 == null)
 		{
 			throw new IllegalArgumentException(String.format("InputField%1$d must be provided", (field1 == null ? 1 : 2)));
 		} // end if(field1 == null || field2 == null)

 		// validate the first date is not null
 		validateNotNull(field1, toValidate);
 		// validate the second date is not null
 		validateNotNull(field2, toValidateAgainst);
 		
 		// if toValidate is not before toValidateAgainst
 		if(!toValidate.before(toValidateAgainst))
 		{
 			throw new ValidationException(field2, String.format("Invalid %1$s %2$s provided, %1$s must be before %3$s.", field1.getFieldLabel(), toValidate, field2.getFieldLabel()));
 		} // end if(!toValidate.before(toValidateAgainst))
	} // end function validateTimestampBefore()
	
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
	 * Util method that returns a java.util.Date object representing the current
	 * GMT
	 * 
	 * 
	 * @return Date a java.util.Date, may be null
	 * @throws IVRServiceException
	 */
	public static Date getCurrentDate() throws HiringEventException {

		if (mLogger.isDebugEnabled()) {
			mLogger.debug("Start getCurrentDate() in ValidationUtil");
		}
		Date currentDate = null;
		currentDate = new Date(System.currentTimeMillis());

		if (mLogger.isDebugEnabled()) {
			mLogger.debug("Finish getCurrentDate() in ValidationUtil - returning: "
					+ currentDate);
		}
		return currentDate;
	}
	
	/**
	 * This method checks for the empty string
	 * 
	 * @param string
	 * @return boolean
	 */
	public static boolean isEmptyString(String string) {
		boolean isEmptyString = false;

		if (string == null || "".equals(string.trim())) {
			isEmptyString = true;
		}
		return isEmptyString;
	}	
} // end class ValidationUtils
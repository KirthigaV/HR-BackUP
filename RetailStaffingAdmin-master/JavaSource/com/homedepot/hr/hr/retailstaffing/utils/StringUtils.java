package com.homedepot.hr.hr.retailstaffing.utils;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: SpecialCharacterFilter.java
 * Application: RetailStaffing
 */
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;

/**
 * This class contains methods used throughout the application to
 * filter special characters from input String objects
 */
public class StringUtils implements Constants
{
	/** Regular expression used to filter out non-standard input characters */
	private static final String INVALID_CHAR_REGEX = "[^\\p{Graph}\\p{Blank}]";
	
	/** Default mask replacement character */
	private static final String MASK_CHAR_STRING = "x";

	/** Regular expression used to mask input characters */
	private static final String MASK_CHAR_REGEX = String.format("[^%1$s]", MASK_CHAR_STRING);
	
	/**
	 * Convenience method to trim a String object if it is not null
	 * 
	 * @param toTrim input string to trim
	 * 
	 * @return trimmed string object or null if the string object provided was null
	 */
	public static String trim(String toTrim)
	{
		if(toTrim != null)
		{
			toTrim = toTrim.trim();
		} // end if(toTrim != null)
		
		return toTrim;
	} // end function trim()
	
	/**
	 * Convenience method to replace non-standard characters with a blank space. 
	 *
	 * @param toFilter input string to filter
	 * 
	 * @return filtered String object or NULL if the String object provided as input is NULL
	 */
	public static String filterSpecialCharacters(String toFilter)
	{
		return filterSpecialCharacters(toFilter, EMPTY_SPACE);
	} // end function filterSpecialCharacters()
	
	/**
	 * Convenience method to replace non-standard characters with the replacement
	 * string provided
	 * 
	 * @param toFilter input string to filter
	 * @param replacementString replacement string to replace any non-standard characters with
	 * @return filtered String object or NULL if the String object provided as input is NULL
	 */
	public static String filterSpecialCharacters(String toFilter, String replacementString)
	{
		if(replacementString == null)
		{
			throw new IllegalArgumentException("Null replacement string provided");
		} // end if(replacementString == null)
		
		// if the input string provided is not null
		if(toFilter != null)
		{
			// replace all of the special characters with a blank space
			toFilter = toFilter.replaceAll(INVALID_CHAR_REGEX, replacementString);
		} // end if(toFilter != null)
		
		return toFilter;		
	} // end function filterSpecialCharacters()
	
	/**
	 * Convenience method to mask all characters in string with an X
	 *
	 * @param toMask input string to mask
	 * 
	 * @return masked String object or NULL if the String object provided as input is NULL
	 */
	public static String mask(String toMask)
	{
		// if the input string provided is not null
		if(toMask != null)
		{
			// replace all non mask filter characters with the mask character
			toMask = toMask.replaceAll(MASK_CHAR_REGEX, MASK_CHAR_STRING);
		} // end if(toMask != null)
		
		return toMask;
	} // end function mask()
} // end class StringUtils
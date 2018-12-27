/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ClientApplLogger.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.homedepot.ta.aa.log4j.ApplLogMessage;

/**
 * Application specific APPL_LOG appender
 */
public class ClientApplLogger extends ApplLogMessage
{
	private static final int GENERIC_ERROR = 1;
	private static final String PROGRAM_ID = "RetailStaffing";
	private static final String SUBSYSTEM_CODE = "hr";
	private static final String SEPARATOR = ":";
	private static final String UNKNOWN_HOST = "Unknown Host";
	
	private final Logger mLogger = Logger.getLogger(getClass());

	public ClientApplLogger(int messageNumber, String messageText)
	{
		super(messageNumber, messageText);
	}

	/**
	 * @param messageNumber
	 *            The Message Number as it appears in the msg table
	 */
	public ClientApplLogger(int messageNumber)
	{
		super(messageNumber);
	}

	/**
	 * Generic Fatal Error message is logged in appl_log table.
	 * 
	 */
	public ClientApplLogger()
	{
		super(GENERIC_ERROR);
	}

	/**
	 * @param messageText
	 *            Any message that the developer would like to send to appl_log
	 */
	public ClientApplLogger(String messageText)
	{
		super(GENERIC_ERROR, messageText);
	}

	/**
	 * @see com.homedepot.ta.aa.log4j.ApplLogMessage#getProgramID()
	 */
	public String getProgramID()
	{
		return PROGRAM_ID;
	}

	/**
	 * @see com.homedepot.ta.aa.log4j.ApplLogMessage#getSubsystemCode()
	 */
	public String getSubsystemCode()
	{
		return SUBSYSTEM_CODE;
	}
	
	/**
	 * Get the message value that will be used to create the APPL_LOG entry. This
	 * method is being overridden so the host name the exception occurred on can be prepended
	 * to the message text. This should make troubleshooting easier because the APPL_LOG 
	 * entry will have the box in the cluster the exception took place on
	 * 
	 * @return 					String : The message value that will be used to create the 
	 * 							APPL_LOG entry
	 */
	@Override
	public String getMessageText()
	{
		StringBuilder messageText = new StringBuilder(256);
		String hostName = null;
		
		try
		{
			// Get the name of the host this thread is executing on
			hostName = InetAddress.getLocalHost().getHostName();
		} // end try
		catch(UnknownHostException uhe)
		{
			// log the error, but keep going with "Unknown" as the host name
			mLogger.error("An exception occurred getting hostName for APPL_LOG entry, defaulting to Unknown", uhe);
			hostName = UNKNOWN_HOST;
		} // end catch
		
		// build the message to be returned (hostName : exceptionMessage)
		messageText.append(hostName)
				   .append(SEPARATOR)
				   .append(super.getMessageText());
		
		return messageText.toString();
	} // end function getMessageText()
} // end class ClientApplLogger
package com.homedepot.hr.hr.retailstaffing.util;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: QualifiedPoolMessage.java
 * Application: RetailStaffing
 */
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.homedepot.ta.aa.log4j.ApplLogMessage;

/**
 * This class is used to insert entries in the APPL_LOG database
 * 
 * @author rlp05
 */
public class QualifiedPoolMessage extends ApplLogMessage
{
	// logger instance
	private final Logger mLogger = Logger.getLogger(getClass());

	// here instead of in the constants file because these value is only used here
	private static final String PROGRAM_ID = "RetailStaffing";
	private static final String SUBSYSTEM_CD = "hr";
	private static final String SEPARATOR = ":";
	private static final String UNKNOWN_HOST = "Unknown Host";	
	
	/**
	 * Constructor
	 * 
	 * @param messageText			Exception message that will be written to the APPL_LOG database
	 */
	public QualifiedPoolMessage(String messageText)
    {
	    super(messageText);
    } // end constructor

	/**
	 * @returns						The program ID this APPL_LOG entry will be created with
	 */
	@Override
    public String getProgramID()
    {
	    return PROGRAM_ID;
    } // end function getProgramID()

	/**
	 * @returns						The subystem code this APPL_LOG entry will be created with
	 */
	@Override
    public String getSubsystemCode()
    {
	    return SUBSYSTEM_CD;
    } // end function getSubsystemCode()
	
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
} // end class QualifiedPoolMessage
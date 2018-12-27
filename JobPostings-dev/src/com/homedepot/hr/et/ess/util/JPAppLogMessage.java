package com.homedepot.hr.et.ess.util;

import com.homedepot.ta.aa.log4j.ApplLogMessage;

public class JPAppLogMessage extends ApplLogMessage {
	/**
	* @param messageNumber The Message Number as it appears in the msg table
	* @param messageText Any additional message that the developer would like to send to appl_log
	*/
	public JPAppLogMessage(int messageNumber, String messageText) {
	super(messageNumber,messageText);
	}
	/**
	* @param messageNumber The Message Number as it appears in the msg table
	*/
	public JPAppLogMessage(int messageNumber) {
	super(messageNumber);
	}
	/**
	* @param messageText Any message that the developer would like to send to appl_log
	*/
	public JPAppLogMessage(String messageText) {
	super(messageText);
	}
	/**
	* @see com.homedepot.ta.aa.log4j.ApplLogMessage#getProgramID()
	*/
	public String getProgramID() {
	return "JobPostings";
	}
	/**
	* @see com.homedepot.ta.aa.log4j.ApplLogMessage#getSubsystemCode()
	*/
	public String getSubsystemCode() {
	return "et";
	}
	}

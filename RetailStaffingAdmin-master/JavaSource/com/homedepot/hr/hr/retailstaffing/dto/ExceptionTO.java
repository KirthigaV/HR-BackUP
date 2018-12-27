/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ExceptionTO.java
 * Application: Retail Staffing Admin
 *
 */
//This file is newly added as part of Flex to HTML Conversion - 29 April 2015
package com.homedepot.hr.hr.retailstaffing.dto;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Exception")
public class ExceptionTO {
	
	/** userMessage */
	@XStreamAlias("userMessage")
	public Object  userMessage;

	/**
	 * @return the userMessage
	 */
	public Object getUserMessage() {
		return userMessage;
	}

	/**
	 * @param userMessage the userMessage to set
	 */
	public void setUserMessage(Object userMessage) {
		this.userMessage = userMessage;
	}
}

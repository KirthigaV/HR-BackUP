/*
 * Created on Nov 20, 2009
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: InterviewStatusTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Status")
public class ProcessStatusTO implements Serializable
{
	private static final long serialVersionUID = 1L;

	//private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("statusCode")
	private String statusCode;
	
	@XStreamAlias("statusDesc")
	private String statusDesc;
	
	/**
	 * @return the statusCode
	 */
	public String getStatusCode()
	{
		return statusCode;
	}

	/**
	 * @param statusCode
	 *            the statusCode to set
	 */
	public void setStatusCode(String statusCode)
	{
		this.statusCode = statusCode;
	}

	/**
	 * @return the statusDesc
	 */
	public String getStatusDesc()
	{
		return statusDesc;
	}

	/**
	 * @param statusDesc
	 *            the statusDesc to set
	 */
	public void setStatusDesc(String statusDesc)
	{
		this.statusDesc = statusDesc;
	}

}

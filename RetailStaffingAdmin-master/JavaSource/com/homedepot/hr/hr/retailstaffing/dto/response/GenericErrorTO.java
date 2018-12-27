/*
 * Created on Feb 12, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application: RetailStaffing
 *
 * File Name: GenericErrorTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;

import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Generic;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * ErrorTO - to get the exceptions in this.
 * 
 * @author Tcs
 */
@XStreamAlias("error")
public class GenericErrorTO implements RetailStaffingConstants, Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("errorMsg")
	private String endUserErrorMsg;
	@XStreamAlias("errorCode")
	private String errorCode;

	/**
	 * @return the errorCode
	 */
	@Generic(action = "GET")
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	@Generic(action = "SET")
	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	/**
	 * @return the endUserErrorMsg
	 */
	@Generic(action = "GET")
	public String getEndUserErrorMsg()
	{
		return endUserErrorMsg;
	}

	/**
	 * @param endUserErrorMsg
	 *            the endUserErrorMsg to set
	 */
	@Generic(action = "SET")
	public void setEndUserErrorMsg(String endUserErrorMsg)
	{
		this.endUserErrorMsg = endUserErrorMsg;
	}

}

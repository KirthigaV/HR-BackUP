/*
 * Created on Feb 12, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: StateDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send US States  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("StateDetail")
public class StateDetailsTO
{

	@XStreamAlias("stateNbr")
	private String stateNbr;
	@XStreamAlias("stateName")
	private String stateName;
	@XStreamAlias("stateCode")
	private String stateCode;

	/**
	 * @return the stateNbr
	 */
	public String getStateNbr()
	{
		return stateNbr;
	}

	/**
	 * @param stateNbr
	 *            the stateNbr to set
	 */
	public void setStateNbr(String stateNbr)
	{
		this.stateNbr = stateNbr;
	}

	/**
	 * @return the stateName
	 */
	public String getStateName()
	{
		return stateName;
	}

	/**
	 * @param stateName
	 *            the stateName to set
	 */
	public void setStateName(String stateName)
	{
		this.stateName = stateName;
	}

	/**
	 * @return the stateCode
	 */
	public String getStateCode()
	{
		return stateCode;
	}

	/**
	 * @param stateCode
	 *            the stateCode to set
	 */
	public void setStateCode(String stateCode)
	{
		this.stateCode = stateCode;
	}

}

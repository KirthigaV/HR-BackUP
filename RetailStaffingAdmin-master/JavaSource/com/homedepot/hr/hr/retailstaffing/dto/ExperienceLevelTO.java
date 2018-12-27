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
 * File Name: ExperienceLevelTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Experience level details  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("ExpLevelDetail")
public class ExperienceLevelTO
{
	@XStreamAlias("expLevelCode")
	String expLevelCode;
	@XStreamAlias("dsplyDesc")
	String dsplyDesc;
	@XStreamAlias("shortDesc")
	String shortDesc;
	/**
	 * @return the expLevelCode
	 */
	public String getExpLevelCode()
	{
		return expLevelCode;
	}
	/**
	 * @param expLevelCode the expLevelCode to set
	 */
	public void setExpLevelCode(String expLevelCode)
	{
		this.expLevelCode = expLevelCode;
	}
	/**
	 * @return the dsplyDesc
	 */
	public String getDsplyDesc()
	{
		return dsplyDesc;
	}
	/**
	 * @param dsplyDesc the dsplyDesc to set
	 */
	public void setDsplyDesc(String dsplyDesc)
	{
		this.dsplyDesc = dsplyDesc;
	}
	/**
	 * @return the shortDesc
	 */
	public String getShortDesc()
	{
		return shortDesc;
	}
	/**
	 * @param shortDesc the shortDesc to set
	 */
	public void setShortDesc(String shortDesc)
	{
		this.shortDesc = shortDesc;
	}

	
}

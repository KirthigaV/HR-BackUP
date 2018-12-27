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
 * File Name: PaginationInfoTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Pagination details  response in XML format
 * 
 * @author TCS
 * 
 */
public class PaginationInfoTO
{

	@XStreamAlias("isFirstHit")
	String isFirstHit;
	@XStreamAlias("isForward")
	String isForward;
	/**
	 * @return the isFirstHit
	 */
	public String getIsFirstHit()
	{
		return isFirstHit;
	}
	/**
	 * @param isFirstHit the isFirstHit to set
	 */
	public void setIsFirstHit(String isFirstHit)
	{
		this.isFirstHit = isFirstHit;
	}
	/**
	 * @return the isForward
	 */
	public String getIsForward()
	{
		return isForward;
	}
	/**
	 * @param isForward the isForward to set
	 */
	public void setIsForward(String isForward)
	{
		this.isForward = isForward;
	}

	

}

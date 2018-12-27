/*
 * 
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: GetCalendarInfoRequest.java
 */

package com.homedepot.hr.hr.retailstaffing.dto.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("GetCalendarInfoRequest")
public class GetCalendarInfoRequest {

	@XStreamAlias("reqNbr")
	private String reqNbr;
	
	@XStreamAlias("reqCalId")
	private String reqCalId;
	
	public String getReqNbr()
	{
		return reqNbr;
	}
	
	public void setReqNbr(String reqNbr)
	{
		this.reqNbr = reqNbr;
	}
	
	public String getReqCalId()
	{
		return reqCalId;
	}
	
	public void setReqCalId(String reqCalId)
	{
		this.reqCalId = reqCalId;
	}
}

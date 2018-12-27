/*
 * 
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: BlcksInterviewTimeTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BlcksInterviewTime")
public class BlcksInterviewTimeTO implements Serializable{
	
	private static final long serialVersionUID = 3334665243490199629L;
	
	@XStreamAlias("beginTimestamp")
	private TimeStampTO beginTimestamp;
	@XStreamAlias("endTimestamp")
	private TimeStampTO endTimestamp;
	@XStreamAlias("humanResourcesSystemStoreNumber")
	private String humanResourcesSystemStoreNumber;
	@XStreamAlias("interviewerAvailabilityCount")
	private short interviewerAvailabilityCount;
	
	public String getHumanResourcesSystemStoreNumber() {
		return humanResourcesSystemStoreNumber;
	}

	public void setHumanResourcesSystemStoreNumber(String humanResourcesSystemStoreNumber) {
		this.humanResourcesSystemStoreNumber = humanResourcesSystemStoreNumber;
	}
	
	public short getInterviewerAvailabilityCount() {
		return interviewerAvailabilityCount;
	}

	public void setInterviewerAvailabilityCount(short interviewerAvailabilityCount) {
		this.interviewerAvailabilityCount = interviewerAvailabilityCount;
	}
	
	public TimeStampTO getBeginTimestamp() {
		return beginTimestamp;
	}

	public void setBeginTimestamp(TimeStampTO beginTimestamp) {
		this.beginTimestamp = beginTimestamp;
	}
	
	public TimeStampTO getEndTimestamp() {
		return endTimestamp;
	}

	public void setEndTimestamp(TimeStampTO endTimestamp) {
		this.endTimestamp = endTimestamp;
	}

}

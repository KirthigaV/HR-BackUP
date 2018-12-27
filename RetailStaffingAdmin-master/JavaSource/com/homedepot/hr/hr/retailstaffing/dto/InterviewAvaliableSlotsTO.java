/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: InterviewAvaliableSlotsTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("interviewAvailDetails")
public class InterviewAvaliableSlotsTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;
	
	
	//Instance variable for beginTimestamp
	@XStreamAlias("calendarId")
	private int calendarId;
	
	//Instance variable for beginTimestamp
	@XStreamAlias("interviewDateTime")
	private Timestamp beginTimestamp;

	//Instance variable for sequenceNumber
	@XStreamAlias("seqNumber")
	private short sequenceNumber;

	//Instance variable for humanResourcesSystemStoreNumber
	@XStreamAlias("storeNo")
	private String StoreNumber;

	//Instance variable for requisitionScheduleReserveTimestamp
	@XStreamAlias("reservedDateTime")
	private Timestamp reservedDateTime;

	//getter method for beginTimestamp
	public Timestamp getBeginTimestamp() {
		return beginTimestamp;
	}

	//setter method for beginTimestamp
	public void setBeginTimestamp(Timestamp aValue) {
		this.beginTimestamp = aValue;
	}

	//getter method for sequenceNumber
	public short getSequenceNumber() {
		return sequenceNumber;
	}

	//setter method for sequenceNumber
	public void setSequenceNumber(short aValue) {
		this.sequenceNumber = aValue;
	}

	//getter method for humanResourcesSystemStoreNumber
	public String getStoreNumber() {
		return StoreNumber;
	}

	//setter method for humanResourcesSystemStoreNumber
	public void setStoreNumber(String StoreNumber) {
		this.StoreNumber = StoreNumber;
	}

	//getter method for ReservedDateTime
	public Timestamp getReservedDateTime() {
		return reservedDateTime;
	}

	//setter method for ReservedDateTime
	public void setReservedDateTime(Timestamp reservedDateTime) {
		this.reservedDateTime = reservedDateTime;
	}
	
	//getter method for CalendarId
	public int getCalendarId() {		
		return calendarId;
	}

	//setter method for CalendarId
	public void setCalendarId(int calendarId) {	
		this.calendarId = calendarId;
	}
}

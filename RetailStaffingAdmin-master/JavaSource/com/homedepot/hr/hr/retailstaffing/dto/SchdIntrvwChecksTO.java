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
 * File Name: IntrwLocationDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to send Schedule Interview Check Details response in XML
 * format
 * 
 * 
 */
@XStreamAlias("candidateSchdIntrwChecks")
public class SchdIntrvwChecksTO implements Serializable
{

	private static final long serialVersionUID = 6530307363543327582L;

	// Offers / Interviews Check
	@XStreamAlias("offersInterviewsCheck")
	private String offersInterviewsCheck;

	// Calendar Availability Check
	@XStreamAlias("calendarAvailability")
	private String calendarAvailability;

	public String getOffersInterviewsCheck() {
		return offersInterviewsCheck;
	}

	public void setOffersInterviewsCheck(String offersInterviewsCheck) {
		this.offersInterviewsCheck = offersInterviewsCheck;
	}

	public String getCalendarAvailability() {
		return calendarAvailability;
	}

	public void setCalendarAvailability(String calendarAvailability) {
		this.calendarAvailability = calendarAvailability;
	}

}

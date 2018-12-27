package com.homedepot.hr.hr.retailstaffing.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: AvailabilityDetails.java
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.util.Set;

/**
 * Transfer object containing Availability data
 * 
 * @author mts1876
 */
public class AvailabilityDetails implements Serializable
{
	private static final long serialVersionUID = -6938500534554662019L;
	
	private String applicantId;
	private Set<Integer> daySeg;
	private Set<Integer> startTimeSeg;

	public AvailabilityDetails()
	{
		
	} // end constructor

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public Set<Integer> getDaySeg() {
		return daySeg;
	}

	public void setDaySeg(Set<Integer> daySeg) {
		this.daySeg = daySeg;
	}

	public Set<Integer> getStartTimeSeg() {
		return startTimeSeg;
	}

	public void setStartTimeSeg(Set<Integer> startTimeSeg) {
		this.startTimeSeg = startTimeSeg;
	}
	


} // end class AvailabilityDetails()
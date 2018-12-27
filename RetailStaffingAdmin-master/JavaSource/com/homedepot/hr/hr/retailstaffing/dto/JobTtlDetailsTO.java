/*
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffingRequest
 *
 * File Name: JobTtlDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as to send Job Title details response in XML format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("JobTitleDetail")
public class JobTtlDetailsTO {
	@XStreamAlias("jobTtlCode")
	String jobTtlCode;
	@XStreamAlias("shortDesc")
	String shortDesc;

	/**
	 * @return the jobTtlCode
	 */
	public String getJobTtlCode() {
		return jobTtlCode;
	}

	/**
	 * @param jobTtlCode the jobTtlCode to set
	 */
	public void setJobTtlCode(String jobTtlCode) {
		this.jobTtlCode = jobTtlCode;
	}

	/**
	 * @return the shortDesc
	 */
	public String getShortDesc() {
		return shortDesc;
	}

	/**
	 * @param shortDesc
	 *            the shortDesc to set
	 */
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

}

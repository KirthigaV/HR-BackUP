/*
 * Created on October 15, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: ApplicantPersonalInfoTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class FirstAdvantageResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	private String providerReferenceId;
	private String orderStatus;
	
	//For New BGC Process
	private String profileId;
	private String applicantLink;

	public String getProviderReferenceId() {
		return providerReferenceId;
	}

	public void setProviderReferenceId(String providerReferenceId) {
		this.providerReferenceId = providerReferenceId;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getProfileId() {
		return profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

	public String getApplicantLink() {
		return applicantLink;
	}

	public void setApplicantLink(String applicantLink) {
		this.applicantLink = applicantLink;
	}
		
}

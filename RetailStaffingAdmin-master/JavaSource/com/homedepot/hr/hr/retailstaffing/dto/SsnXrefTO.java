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
 * File Name: StoreDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used to hold SSN Xref table details  response in XML format
 * 
 * 
 */
@XStreamAlias("ssnXrefTO")
public class SsnXrefTO implements Serializable
{
	
	private static final long serialVersionUID = 5562864835566477854L;

	@XStreamAlias("employmentApplicantId")
	private String employmentApplicantId;
	
	@XStreamAlias("effectiveBeginDate")
	private String effectiveBeginDate;

	@XStreamAlias("effectiveEndDate")
	private String effectiveEndDate;
	
	@XStreamAlias("lastUpdateSystemUserId")
	private String lastUpdateSystemUserId;
	
	@XStreamAlias("lastUpdateTimestamp")
	private String lastUpdateTimestamp;
	
	@XStreamAlias("applicantSocialSecurityNumberNumber")
	private String applicantSocialSecurityNumberNumber;
	
	@XStreamAlias("activeFlag")
	private String activeFlag;

	public String getEmploymentApplicantId() {
		return employmentApplicantId;
	}

	public void setEmploymentApplicantId(String employmentApplicantId) {
		this.employmentApplicantId = employmentApplicantId;
	}

	public String getEffectiveBeginDate() {
		return effectiveBeginDate;
	}

	public void setEffectiveBeginDate(String effectiveBeginDate) {
		this.effectiveBeginDate = effectiveBeginDate;
	}

	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getLastUpdateSystemUserId() {
		return lastUpdateSystemUserId;
	}

	public void setLastUpdateSystemUserId(String lastUpdateSystemUserId) {
		this.lastUpdateSystemUserId = lastUpdateSystemUserId;
	}

	public String getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(String lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getApplicantSocialSecurityNumberNumber() {
		return applicantSocialSecurityNumberNumber;
	}

	public void setApplicantSocialSecurityNumberNumber(String applicantSocialSecurityNumberNumber) {
		this.applicantSocialSecurityNumberNumber = applicantSocialSecurityNumberNumber;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
}

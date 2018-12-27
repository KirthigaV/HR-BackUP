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
 * File Name: RequisitionDetailTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as to send Applicant details response in XML format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("applicant")
public class QPConsideredApplicantDetailTO implements Serializable {
	private static final long serialVersionUID = 7575746680713548574L;

	@XStreamAlias("applicantId")
	private String applicantId;
	@XStreamAlias("reqNbr")
	private String reqNbr;
	@XStreamAlias("consideredCode")
	private String consideredCode;

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getReqNbr() {
		return reqNbr;
	}

	public void setReqNbr(String reqNbr) {
		this.reqNbr = reqNbr;
	}

	public String getConsideredCode() {
		return consideredCode;
	}

	public void setConsideredCode(String consideredCode) {
		this.consideredCode = consideredCode;
	}

}

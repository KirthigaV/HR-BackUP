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
 * File Name: JobTitleRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create Candidate Interview Details Request object  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("IntvwResultsCandIntvwDtlsRequest")
public class IntvwResultsCandIntvwDtlsRequest 
{

	@XStreamAlias("reqNbr")
	private String reqNbr;
	@XStreamAlias("candId")
	private String candId;
	@XStreamAlias("strNo")
	private String strNo;
	@XStreamAlias("jobTtlCd")
	private String jobTtlCd;
	@XStreamAlias("deptNo")
	private String deptNo;
	@XStreamAlias("applicantType")
	private String applicantType;
	@XStreamAlias("organization")
	private String organization;
	
	public String getReqNbr() {
		return reqNbr;
	}
	public void setReqNbr(String reqNbr) {
		this.reqNbr = reqNbr;
	}
	public String getCandId() {
		return candId;
	}
	public void setCandId(String candId) {
		this.candId = candId;
	}
	public String getStrNo() {
		return strNo;
	}
	public void setStrNo(String strNo) {
		this.strNo = strNo;
	}
	public String getJobTtlCd() {
		return jobTtlCd;
	}
	public void setJobTtlCd(String jobTtlCd) {
		this.jobTtlCd = jobTtlCd;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getApplicantType() {
		return applicantType;
	}
	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}


}

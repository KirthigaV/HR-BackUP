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
/**
 * This class is used as to send education response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("WorkHistory")
public class ApplWorkHistoryInfoTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("company")
	private String company;
	
	@XStreamAlias("position")
	private String position;
	
	@XStreamAlias("location")
	private String location;
	
	@XStreamAlias("supervisor")
	private String supervisor;
	
	@XStreamAlias("comDateFrom")
	private String comDateFrom;
	
	@XStreamAlias("comDateTo")
	private String comDateTo;
	
	@XStreamAlias("payAtLeaving")
	private String payAtLeaving;
	
	@XStreamAlias("supervisorTitle")
	private String supervisorTitle;
	
	@XStreamAlias("jobDescription")
	private String jobDescription;
	
	@XStreamAlias("reasonLeaving")
	private String reasonLeaving;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getComDateFrom() {
		return comDateFrom;
	}

	public void setComDateFrom(String comDateFrom) {
		this.comDateFrom = comDateFrom;
	}

	public String getComDateTo() {
		return comDateTo;
	}

	public void setComDateTo(String comDateTo) {
		this.comDateTo = comDateTo;
	}

	public String getPayAtLeaving() {
		return payAtLeaving;
	}

	public void setPayAtLeaving(String payAtLeaving) {
		this.payAtLeaving = payAtLeaving;
	}

	public String getSupervisorTitle() {
		return supervisorTitle;
	}

	public void setSupervisorTitle(String supervisorTitle) {
		this.supervisorTitle = supervisorTitle;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public String getReasonLeaving() {
		return reasonLeaving;
	}

	public void setReasonLeaving(String reasonLeaving) {
		this.reasonLeaving = reasonLeaving;
	}		
}

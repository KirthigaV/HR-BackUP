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
 * This class is used as to send personal info response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("PersonalInfo")
public class ApplPersonalInfoTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;
	@XStreamAlias("applType")
	private String applType;
	@XStreamAlias("fullName")
	private String fullName;
	@XStreamAlias("phoneNum")
	private String phoneNum;
	@XStreamAlias("address1")
	private String address1;
	@XStreamAlias("address2")
	private String address2;
	@XStreamAlias("cityStateZip")
	private String cityStateZip;
	
	@XStreamAlias("applID")
	private String applID;
	@XStreamAlias("emailAddress")
	private String emailAddress;
	@XStreamAlias("usaEligibility")
	private String usaEligibility;
	@XStreamAlias("over18")
	private String over18;
	
	@XStreamAlias("priorConviction")
	private String priorConviction;
	@XStreamAlias("priorConvictionDate")
	private String priorConvictionDate;
	@XStreamAlias("priorConvictionText")
	private String priorConvictionText;
	
	@XStreamAlias("onLeaveOrLaidOff")
	private String onLeaveOrLaidOff;
	@XStreamAlias("preEmployee")
	private String preEmployee;
	@XStreamAlias("location")
	private String location;
	@XStreamAlias("dateFrom")
	private String dateFrom;
	@XStreamAlias("dateTo")
	private String dateTo;
	@XStreamAlias("relWorkingForHD")
	private String relWorkingForHD;
	@XStreamAlias("relName")
	private String relName;
	
	@XStreamAlias("milExperience")
	private String milExperience;
	@XStreamAlias("milBranchName")
	private String milBranchName;
	@XStreamAlias("milDateFrom")
	private String milDateFrom;
	@XStreamAlias("milDatTo")
	private String milDatTo;
	@XStreamAlias("jobFullTime")
	private String jobFullTime;
	@XStreamAlias("jobPartTime")
	private String jobPartTime;
	
	@XStreamAlias("jobPerm")
	private String jobPerm;
	@XStreamAlias("jobTemp")
	private String jobTemp;
	
	@XStreamAlias("wageDesired")
	private String wageDesired;
	@XStreamAlias("wageOpen")
	private String wageOpen;
	// fixing profile issue 06-06-2011
	@XStreamAlias("emplApplicationDate")
	private String emplApplicationDate;
	
	@XStreamAlias("candRefNbr")
	private String candRefNbr;
	
	public String getApplType() {
		return applType;
	}
	public void setApplType(String applType) {
		this.applType = applType;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCityStateZip() {
		return cityStateZip;
	}
	public void setCityStateZip(String cityStateZip) {
		this.cityStateZip = cityStateZip;
	}
	public String getApplID() {
		return applID;
	}
	public void setApplID(String applID) {
		this.applID = applID;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getUsaEligibility() {
		return usaEligibility;
	}
	public void setUsaEligibility(String usaEligibility) {
		this.usaEligibility = usaEligibility;
	}
	public String getOver18() {
		return over18;
	}
	public void setOver18(String over18) {
		this.over18 = over18;
	}
	public String getPriorConviction() {
		return priorConviction;
	}
	public void setPriorConviction(String priorConviction) {
		this.priorConviction = priorConviction;
	}
	public String getPriorConvictionDate() {
		return priorConvictionDate;
	}
	public void setPriorConvictionDate(String priorConvictionDate) {
		this.priorConvictionDate = priorConvictionDate;
	}
	public String getPriorConvictionText() {
		return priorConvictionText;
	}
	public void setPriorConvictionText(String priorConvictionText) {
		this.priorConvictionText = priorConvictionText;
	}
	public String getOnLeaveOrLaidOff() {
		return onLeaveOrLaidOff;
	}
	public void setOnLeaveOrLaidOff(String onLeaveOrLaidOff) {
		this.onLeaveOrLaidOff = onLeaveOrLaidOff;
	}
	public String getPreEmployee() {
		return preEmployee;
	}
	public void setPreEmployee(String preEmployee) {
		this.preEmployee = preEmployee;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getRelWorkingForHD() {
		return relWorkingForHD;
	}
	public void setRelWorkingForHD(String relWorkingForHD) {
		this.relWorkingForHD = relWorkingForHD;
	}
	public String getRelName() {
		return relName;
	}
	public void setRelName(String relName) {
		this.relName = relName;
	}
	public String getMilExperience() {
		return milExperience;
	}
	public void setMilExperience(String milExperience) {
		this.milExperience = milExperience;
	}
	public String getMilBranchName() {
		return milBranchName;
	}
	public void setMilBranchName(String milBranchName) {
		this.milBranchName = milBranchName;
	}
	public String getMilDateFrom() {
		return milDateFrom;
	}
	public void setMilDateFrom(String milDateFrom) {
		this.milDateFrom = milDateFrom;
	}
	public String getMilDatTo() {
		return milDatTo;
	}
	public void setMilDatTo(String milDatTo) {
		this.milDatTo = milDatTo;
	}
	public String getJobFullTime() {
		return jobFullTime;
	}
	public void setJobFullTime(String jobFullTime) {
		this.jobFullTime = jobFullTime;
	}
	public String getJobPartTime() {
		return jobPartTime;
	}
	public void setJobPartTime(String jobPartTime) {
		this.jobPartTime = jobPartTime;
	}
	public String getJobPerm() {
		return jobPerm;
	}
	public void setJobPerm(String jobPerm) {
		this.jobPerm = jobPerm;
	}
	public String getJobTemp() {
		return jobTemp;
	}
	public void setJobTemp(String jobTemp) {
		this.jobTemp = jobTemp;
	}
	public String getWageDesired() {
		return wageDesired;
	}
	public void setWageDesired(String wageDesired) {
		this.wageDesired = wageDesired;
	}
	public String getWageOpen() {
		return wageOpen;
	}
	public void setWageOpen(String wageOpen) {
		this.wageOpen = wageOpen;
	}
			
	public String getEmplApplicationDate() {
		return emplApplicationDate;
	}
	public void setEmplApplicationDate(String emplApplicationDate) {
		this.emplApplicationDate = emplApplicationDate;
	}

	public String getCandRefNbr() {
		return candRefNbr;
	}
	public void setCandRefNbr(String candRefNbr) {
		this.candRefNbr = candRefNbr;
	}
	
}

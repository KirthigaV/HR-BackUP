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
@XStreamAlias("Applicant")
public class ApplicantTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("firstName")
	private String firstName;
	@XStreamAlias("lastName")
	private String lastName;
	@XStreamAlias("applicantId")
	private String applicantId;
	@XStreamAlias("category")
	private String category;
	@XStreamAlias("ft_pt")
	private String ft_pt;
	@XStreamAlias("applType")
	private String applType;
	@XStreamAlias("lastUpdated")
	private String lastUpdated;
	
	@XStreamAlias("areaCode")
	private String mAreaCode;
	@XStreamAlias("phoneNumber")
	private String mPhoneNumber;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getApplicantId() {
		return applicantId;
	}
	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getFt_pt() {
		return ft_pt;
	}
	public void setFt_pt(String ft_pt) {
		this.ft_pt = ft_pt;
	}
	public String getApplType() {
		return applType;
	}
	public void setApplType(String applType) {
		this.applType = applType;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public String getAreaCode()
	{
		return mAreaCode;
	} // end function getAreaCode()
	
	public void setAreaCode(String areaCode)
	{
		mAreaCode = areaCode;
	} // end function setAreaCode()
	
	public String getPhoneNumber()
	{
		return mPhoneNumber;
	} // end function getPhoneNumber()
	
	public void setPhoneNumber(String phoneNumber)
	{
		mPhoneNumber = phoneNumber;
	} // end function setPhoneNumber()
}

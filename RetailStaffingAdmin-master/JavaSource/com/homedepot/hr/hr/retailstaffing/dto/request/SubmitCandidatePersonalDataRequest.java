/*
 * Created on Apr 23 2014
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: SubmitCandidatePersonalDataRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create SubmitCandidatePersonalDataRequest object for Input XML
 * 
 * 
 */
@XStreamAlias("SubmitCandidatePersonalDataRequest")
public class SubmitCandidatePersonalDataRequest implements Serializable
{
	
	private static final long serialVersionUID = -8025859056168571862L;

	@XStreamAlias("ssnEntry1")
	private String ssnEntry1 ;

	@XStreamAlias("ssnEntry2")
	private String ssnEntry2 ;

	@XStreamAlias("dobEntry1")
	private String dobEntry1;
	
	@XStreamAlias("dobEntry2")
	private String dobEntry2;
	
	@XStreamAlias("dlNumber1")
	private String dlNumber1;
	
	@XStreamAlias("dlNumber2")
	private String dlNumber2;	
	
	@XStreamAlias("driversLicenseState")
	private String driversLicenseState;
	
	@XStreamAlias("acceptChecked")
	private String acceptChecked;
	
	@XStreamAlias("candidateInitials")
	private String candidateInitials;
	
	@XStreamAlias("applicantId")
	private String applicantId;
	
	@XStreamAlias("candidateId")
	private String candidateId;
	
	@XStreamAlias("name")
	private String name;
	
	@XStreamAlias("mgrLdap")
	private String mgrLdap;
	
	@XStreamAlias("hasMiddleName")
	private String hasMiddleName;
	
	@XStreamAlias("middleName")
	private String middleName;
	
	@XStreamAlias("livedAtCurrentAddressRequiredTime")
	private String livedAtCurrentAddressRequiredTime;
	
	@XStreamAlias("prevAddressOutsideUS")
	private String prevAddressOutsideUS;

	@XStreamAlias("address1")
	private String address1;
	
	@XStreamAlias("address2")
	private String address2;
	
	@XStreamAlias("city")
	private String city;
	
	@XStreamAlias("addressState")
	private String addressState;
	
	@XStreamAlias("zipCode")
	private String zipCode;
	
	public String getSsnEntry1() {
		return ssnEntry1;
	}

	public void setSsnEntry1(String ssnEntry1) {
		this.ssnEntry1 = ssnEntry1;
	}

	public String getDobEntry1() {
		return dobEntry1;
	}

	public void setDobEntry1(String dobEntry1) {
		this.dobEntry1 = dobEntry1;
	}

	public String getDlNumber1() {
		return dlNumber1;
	}

	public void setDlNumber1(String dlNumber1) {
		this.dlNumber1 = dlNumber1;
	}

	public String getSsnEntry2() {
		return ssnEntry2;
	}

	public void setSsnEntry2(String ssnEntry2) {
		this.ssnEntry2 = ssnEntry2;
	}

	public String getDobEntry2() {
		return dobEntry2;
	}

	public void setDobEntry2(String dobEntry2) {
		this.dobEntry2 = dobEntry2;
	}

	public String getDlNumber2() {
		return dlNumber2;
	}

	public void setDlNumber2(String dlNumber2) {
		this.dlNumber2 = dlNumber2;
	}

	public String getDriversLicenseState() {
		return driversLicenseState;
	}

	public void setDriversLicenseState(String driversLicenseState) {
		this.driversLicenseState = driversLicenseState;
	}

	public String getAcceptChecked() {
		return acceptChecked;
	}

	public void setAcceptChecked(String acceptChecked) {
		this.acceptChecked = acceptChecked;
	}

	public String getCandidateInitials() {
		return candidateInitials;
	}

	public void setCandidateInitials(String candidateInitials) {
		this.candidateInitials = candidateInitials;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMgrLdap() {
		return mgrLdap;
	}

	public void setMgrLdap(String mgrLdap) {
		this.mgrLdap = mgrLdap;
	}

	public String getHasMiddleName() {
		return hasMiddleName;
	}

	public void setHasMiddleName(String hasMiddleName) {
		this.hasMiddleName = hasMiddleName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLivedAtCurrentAddressRequiredTime() {
		return livedAtCurrentAddressRequiredTime;
	}

	public void setLivedAtCurrentAddressRequiredTime(
			String livedAtCurrentAddressRequiredTime) {
		this.livedAtCurrentAddressRequiredTime = livedAtCurrentAddressRequiredTime;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddressState() {
		return addressState;
	}

	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPrevAddressOutsideUS() {
		return prevAddressOutsideUS;
	}

	public void setPrevAddressOutsideUS(String prevAddressOutsideUS) {
		this.prevAddressOutsideUS = prevAddressOutsideUS;
	}
	
}

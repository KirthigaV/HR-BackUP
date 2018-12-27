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
import java.sql.Date;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send personal info response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("BackgroundCheckSystemConsent")
public class ReadBackgroundCheckSystemConsentByInputListDTO implements Serializable
{

	private static final long serialVersionUID = 385936170771774906L;

	@XStreamAlias("consentSignatureDate")
	private Date consentSignatureDate;

	@XStreamAlias("createUserId")
	private String createUserId;

	@XStreamAlias("createTimestamp")
	private Timestamp createTimestamp;

	@XStreamAlias("lastUpdateUserId")
	private String lastUpdateUserId;

	@XStreamAlias("lastUpdateTimestamp")
	private Timestamp lastUpdateTimestamp;

	@XStreamAlias("manualSignatureReferenceId")
	private Integer manualSignatureReferenceId;

	@XStreamAlias("lastName")
	private String lastName;

	@XStreamAlias("firstName")
	private String firstName;

	@XStreamAlias("middleInitialName")
	private String middleInitialName;

	@XStreamAlias("applicantAliasName")
	private String applicantAliasName;

	@XStreamAlias("driverLicenseNumber")
	private String driverLicenseNumber;

	@XStreamAlias("driverLicenseStateCode")
	private String driverLicenseStateCode;

	@XStreamAlias("birthDate")
	private Date birthDate;

	@XStreamAlias("birthPlaceName")
	private String birthPlaceName;

	@XStreamAlias("motherMaidName")
	private String motherMaidName;

	public Date getConsentSignatureDate() {
		return consentSignatureDate;
	}

	public void setConsentSignatureDate(Date consentSignatureDate) {
		this.consentSignatureDate = consentSignatureDate;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Timestamp lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public Integer getManualSignatureReferenceId() {
		return manualSignatureReferenceId;
	}

	public void setManualSignatureReferenceId(Integer manualSignatureReferenceId) {
		this.manualSignatureReferenceId = manualSignatureReferenceId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleInitialName() {
		return middleInitialName;
	}

	public void setMiddleInitialName(String middleInitialName) {
		this.middleInitialName = middleInitialName;
	}

	public String getApplicantAliasName() {
		return applicantAliasName;
	}

	public void setApplicantAliasName(String applicantAliasName) {
		this.applicantAliasName = applicantAliasName;
	}

	public String getDriverLicenseNumber() {
		return driverLicenseNumber;
	}

	public void setDriverLicenseNumber(String driverLicenseNumber) {
		this.driverLicenseNumber = driverLicenseNumber;
	}

	public String getDriverLicenseStateCode() {
		return driverLicenseStateCode;
	}

	public void setDriverLicenseStateCode(String driverLicenseStateCode) {
		this.driverLicenseStateCode = driverLicenseStateCode;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlaceName() {
		return birthPlaceName;
	}

	public void setBirthPlaceName(String birthPlaceName) {
		this.birthPlaceName = birthPlaceName;
	}

	public String getMotherMaidName() {
		return motherMaidName;
	}

	public void setMotherMaidName(String motherMaidName) {
		this.motherMaidName = motherMaidName;
	}

		
}

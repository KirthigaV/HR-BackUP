package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
/**
 * This class is used as to send candidate details in response in XML
 * 
 * @author TCS
 * 
 */
public class DriversLicenseDetailsTO implements Serializable
{
	private static final long serialVersionUID = 7429085486427615403L;

	private String candidateId;
	private String name;
	private Date birthDate;
	private byte[] encryptDriverLicenseNumber;
	private Date driverLicenseExpirationDate;
	private String driverLicenseStateCode;
	private Timestamp electronicMailTransmissionTimestamp;
	private Timestamp electronicMailRetransmissionTimestamp;

	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public byte[] getEncryptDriverLicenseNumber() {
		return encryptDriverLicenseNumber;
	}
	public void setEncryptDriverLicenseNumber(byte[] encryptDriverLicenseNumber) {
		this.encryptDriverLicenseNumber = encryptDriverLicenseNumber;
	}
	public Date getDriverLicenseExpirationDate() {
		return driverLicenseExpirationDate;
	}
	public void setDriverLicenseExpirationDate(Date driverLicenseExpirationDate) {
		this.driverLicenseExpirationDate = driverLicenseExpirationDate;
	}
	public String getDriverLicenseStateCode() {
		return driverLicenseStateCode;
	}
	public void setDriverLicenseStateCode(String driverLicenseStateCode) {
		this.driverLicenseStateCode = driverLicenseStateCode;
	}
	public Timestamp getElectronicMailTransmissionTimestamp() {
		return electronicMailTransmissionTimestamp;
	}
	public void setElectronicMailTransmissionTimestamp(Timestamp electronicMailTransmissionTimestamp) {
		this.electronicMailTransmissionTimestamp = electronicMailTransmissionTimestamp;
	}
	public Timestamp getElectronicMailRetransmissionTimestamp() {
		return electronicMailRetransmissionTimestamp;
	}
	public void setElectronicMailRetransmissionTimestamp(Timestamp electronicMailRetransmissionTimestamp) {
		this.electronicMailRetransmissionTimestamp = electronicMailRetransmissionTimestamp;
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
}

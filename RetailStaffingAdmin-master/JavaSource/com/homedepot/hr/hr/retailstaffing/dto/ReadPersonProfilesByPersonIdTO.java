package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Interview Status details  response in XML format
 * 
 * @author dxg8002
 * 
 */
@XStreamAlias("personProfilesT")
public class ReadPersonProfilesByPersonIdTO implements Serializable {
	
	private static final long serialVersionUID = -7335190051277186729L;

	@XStreamAlias("name")
	private String name;

	@XStreamAlias("organization1")
	private String organization1;

	@XStreamAlias("organization2")
	private String organization2;

	@XStreamAlias("jobTitleId")
	private String jobTitleId;

	@XStreamAlias("hireDateOrigin")
	private Date hireDateOrigin;

	@XStreamAlias("employmentCat")
	private String employmentCat;

	@XStreamAlias("addressLine1Long")
	private String addressLine1Long;

	@XStreamAlias("addressLine2Long")
	private String addressLine2Long;

	@XStreamAlias("addressCity")
	private String addressCity;

	@XStreamAlias("stateProvince")
	private String stateProvince;

	@XStreamAlias("postalCode11")
	private String postalCode11;

	@XStreamAlias("phoneNumber")
	private String phoneNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganization1() {
		return organization1;
	}

	public void setOrganization1(String organization1) {
		this.organization1 = organization1;
	}

	public String getOrganization2() {
		return organization2;
	}

	public void setOrganization2(String organization2) {
		this.organization2 = organization2;
	}

	public String getJobTitleId() {
		return jobTitleId;
	}

	public void setJobTitleId(String jobTitleId) {
		this.jobTitleId = jobTitleId;
	}

	public Date getHireDateOrigin() {
		return hireDateOrigin;
	}

	public void setHireDateOrigin(Date hireDateOrigin) {
		this.hireDateOrigin = hireDateOrigin;
	}

	public String getEmploymentCat() {
		return employmentCat;
	}

	public void setEmploymentCat(String employmentCat) {
		this.employmentCat = employmentCat;
	}

	public String getAddressLine1Long() {
		return addressLine1Long;
	}

	public void setAddressLine1Long(String addressLine1Long) {
		this.addressLine1Long = addressLine1Long;
	}

	public String getAddressLine2Long() {
		return addressLine2Long;
	}

	public void setAddressLine2Long(String addressLine2Long) {
		this.addressLine2Long = addressLine2Long;
	}

	public String getAddressCity() {
		return addressCity;
	}

	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	public String getStateProvince() {
		return stateProvince;
	}

	public void setStateProvince(String stateProvince) {
		this.stateProvince = stateProvince;
	}

	public String getPostalCode11() {
		return postalCode11;
	}

	public void setPostalCode11(String postalCode11) {
		this.postalCode11 = postalCode11;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}

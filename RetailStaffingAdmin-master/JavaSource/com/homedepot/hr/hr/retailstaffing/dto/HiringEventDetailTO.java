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

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as to send Hiring Event Detail response in XML format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("HiringEventDetailTO")
public class HiringEventDetailTO implements Serializable {

	private static final long serialVersionUID = -8674802413800429712L;

	@XStreamAlias("hireEventId")
	private int hireEventId;
	@XStreamAlias("hireEventName")
	private String hireEventName;
	@XStreamAlias("hireEventBeginDate")
	private Date hireEventBeginDate;
	@XStreamAlias("hireEventEndDate")
	private Date hireEventEndDate;	
	@XStreamAlias("hireEventLocationDescription")
	private String hireEventLocationDescription;
	@XStreamAlias("hireEventAddressText")
	private String hireEventAddressText;
	@XStreamAlias("hireEventCityName")
	private String hireEventCityName;
	@XStreamAlias("hireEventZipCodeCode")
	private String hireEventZipCodeCode;
	@XStreamAlias("hireEventStateCode")
	private String hireEventStateCode;
	@XStreamAlias("hireEventTypeIndicator")
	private String hireEventTypeIndicator;
	@XStreamAlias("emgrHumanResourcesAssociateId")
	private String emgrHumanResourcesAssociateId;
	@XStreamAlias("hireEventCreatedByStore")
	private String hireEventCreatedByStore;
	@XStreamAlias("hireEventCreatedByStoreTimezone")
	private String hireEventCreatedByStoreTimezone;
		

	public Date getHireEventBeginDate() {
		return hireEventBeginDate;
	}

	public void setHireEventBeginDate(Date hireEventBeginDate) {
		this.hireEventBeginDate = hireEventBeginDate;
	}

	public String getHireEventLocationDescription() {
		return hireEventLocationDescription;
	}

	public void setHireEventLocationDescription(String hireEventLocationDescription) {
		this.hireEventLocationDescription = hireEventLocationDescription;
	}

	public String getHireEventAddressText() {
		return hireEventAddressText;
	}

	public void setHireEventAddressText(String hireEventAddressText) {
		this.hireEventAddressText = hireEventAddressText;
	}

	public String getHireEventCityName() {
		return hireEventCityName;
	}

	public void setHireEventCityName(String hireEventCityName) {
		this.hireEventCityName = hireEventCityName;
	}

	public String getHireEventZipCodeCode() {
		return hireEventZipCodeCode;
	}

	public void setHireEventZipCodeCode(String hireEventZipCodeCode) {
		this.hireEventZipCodeCode = hireEventZipCodeCode;
	}

	public String getHireEventStateCode() {
		return hireEventStateCode;
	}

	public void setHireEventStateCode(String hireEventStateCode) {
		this.hireEventStateCode = hireEventStateCode;
	}

	public String getHireEventTypeIndicator() {
		return hireEventTypeIndicator;
	}

	public void setHireEventTypeIndicator(String hireEventTypeIndicator) {
		this.hireEventTypeIndicator = hireEventTypeIndicator;
	}

	public String getEmgrHumanResourcesAssociateId() {
		return emgrHumanResourcesAssociateId;
	}

	public void setEmgrHumanResourcesAssociateId(String emgrHumanResourcesAssociateId) {
		this.emgrHumanResourcesAssociateId = emgrHumanResourcesAssociateId;
	}

	public int getHireEventId() {
		return hireEventId;
	}

	public void setHireEventId(int hireEventId) {
		this.hireEventId = hireEventId;
	}

	public Date getHireEventEndDate() {
		return hireEventEndDate;
	}

	public void setHireEventEndDate(Date hireEventEndDate) {
		this.hireEventEndDate = hireEventEndDate;
	}

	public String getHireEventName() {
		return hireEventName;
	}

	public void setHireEventName(String hireEventName) {
		this.hireEventName = hireEventName;
	}

	public String getHireEventCreatedByStore() {
		return hireEventCreatedByStore;
	}

	public void setHireEventCreatedByStore(String hireEventCreatedByStore) {
		this.hireEventCreatedByStore = hireEventCreatedByStore;
	}

	public String getHireEventCreatedByStoreTimezone() {
		return hireEventCreatedByStoreTimezone;
	}

	public void setHireEventCreatedByStoreTimezone(String hireEventCreatedByStoreTimezone) {
		this.hireEventCreatedByStoreTimezone = hireEventCreatedByStoreTimezone;
	}

}

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
 * File Name: StoreDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Location details  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("LocationDetails")
public class LocationDetailsTO
{
	@XStreamAlias("humanResourcesSystemStoreNumber")
	private String humanResourcesSystemStoreNumber;
	
	@XStreamAlias("humanResourcesSystemStoreName")
	private String humanResourcesSystemStoreName;
	
	@XStreamAlias("humanResourcesSystemDivisionCode")
	private String humanResourcesSystemDivisionCode;
	
	@XStreamAlias("stateCode")
	private String stateCode;
	
	@XStreamAlias("isRSCSupportedLocation")
	private boolean rscSupportedLocation;
	
	@XStreamAlias("isRSCSupportedLocationException")
	private boolean isRSCSupportedLocationException;

	public String getHumanResourcesSystemStoreNumber() {
		return humanResourcesSystemStoreNumber;
	}

	public void setHumanResourcesSystemStoreNumber(String humanResourcesSystemStoreNumber) {
		this.humanResourcesSystemStoreNumber = humanResourcesSystemStoreNumber;
	}

	public String getHumanResourcesSystemStoreName() {
		return humanResourcesSystemStoreName;
	}

	public void setHumanResourcesSystemStoreName(String humanResourcesSystemStoreName) {
		this.humanResourcesSystemStoreName = humanResourcesSystemStoreName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public boolean isRscSupportedLocation() {
		return rscSupportedLocation;
	}

	public void setRscSupportedLocation(boolean rscSupportedLocation) {
		this.rscSupportedLocation = rscSupportedLocation;
	}

	public String getHumanResourcesSystemDivisionCode() {
		return humanResourcesSystemDivisionCode;
	}

	public void setHumanResourcesSystemDivisionCode(String humanResourcesSystemDivisionCode) {
		this.humanResourcesSystemDivisionCode = humanResourcesSystemDivisionCode;
	}

	public boolean isRSCSupportedLocationException() {
		return isRSCSupportedLocationException;
	}

	public void setRSCSupportedLocationException(boolean isRSCSupportedLocationException) {
		this.isRSCSupportedLocationException = isRSCSupportedLocationException;
	}
}

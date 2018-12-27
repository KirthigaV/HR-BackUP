package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as to send Department details response in XML format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("JobRequirments")
public class ReadBackgrdJobRequirementByDeptStoreJobTitleCodeDTO {

	@XStreamAlias("humanResourcesStoreTypeCode")
	private String humanResourcesStoreTypeCode;

	@XStreamAlias("backgroundCheckSystemPackageId")
	private int backgroundCheckSystemPackageId;

	@XStreamAlias("backgroundCheckSystemComponentId")
	private int backgroundCheckSystemComponentId;

	public String getHumanResourcesStoreTypeCode() {
		return humanResourcesStoreTypeCode;
	}

	public void setHumanResourcesStoreTypeCode(String humanResourcesStoreTypeCode) {
		this.humanResourcesStoreTypeCode = humanResourcesStoreTypeCode;
	}

	public int getBackgroundCheckSystemPackageId() {
		return backgroundCheckSystemPackageId;
	}

	public void setBackgroundCheckSystemPackageId(int backgroundCheckSystemPackageId) {
		this.backgroundCheckSystemPackageId = backgroundCheckSystemPackageId;
	}

	public int getBackgroundCheckSystemComponentId() {
		return backgroundCheckSystemComponentId;
	}

	public void setBackgroundCheckSystemComponentId(int backgroundCheckSystemComponentId) {
		this.backgroundCheckSystemComponentId = backgroundCheckSystemComponentId;
	}
	
}

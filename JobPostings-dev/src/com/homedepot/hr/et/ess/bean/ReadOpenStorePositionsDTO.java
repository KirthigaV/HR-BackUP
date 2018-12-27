/**
 * 
 */
package com.homedepot.hr.et.ess.bean;

import java.io.Serializable;

public class ReadOpenStorePositionsDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Instance variable for humanResourcesSystemDepartmentNumber
	private String humanResourcesSystemDepartmentNumber;

	//Instance variable for jobTitleDescription
	private String jobTitleDescription;

	//Instance variable for openPositionCount
	private short openPositionCount;

	//Instance variable for fullTimeRequiredFlag
	private String fullTimeRequiredFlag;

	//Instance variable for partTimeRequiredFlag
	private String partTimeRequiredFlag;

	//getter method for humanResourcesSystemDepartmentNumber
	public String getHumanResourcesSystemDepartmentNumber() {
		return humanResourcesSystemDepartmentNumber;
	}

	//setter method for humanResourcesSystemDepartmentNumber
	public void setHumanResourcesSystemDepartmentNumber(String aValue) {
		humanResourcesSystemDepartmentNumber = aValue;
	}

	//getter method for jobTitleDescription
	public String getJobTitleDescription() {
		return jobTitleDescription;
	}

	//setter method for jobTitleDescription
	public void setJobTitleDescription(String aValue) {
		jobTitleDescription = aValue;
	}

	//getter method for openPositionCount
	public short getOpenPositionCount() {
		return openPositionCount;
	}

	//setter method for openPositionCount
	public void setOpenPositionCount(short aValue) {
		openPositionCount = aValue;
	}

	//getter method for fullTimeRequiredFlag
	public String getFullTimeRequiredFlag() {
		return fullTimeRequiredFlag;
	}

	//setter method for fullTimeRequiredFlag
	public void setFullTimeRequiredFlag(String aValue) {
		fullTimeRequiredFlag = aValue;
	}

	//getter method for partTimeRequiredFlag
	public String getPartTimeRequiredFlag() {
		return partTimeRequiredFlag;
	}

	//setter method for partTimeRequiredFlag
	public void setPartTimeRequiredFlag(String aValue) {
		partTimeRequiredFlag = aValue;
	}
}
package com.homedepot.hr.hr.retailstaffing.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("reqStoreDetails")
public class ReadHumanResourcesStoreEmploymentRequisitionDTO
{

	@XStreamAlias("humanResourcesSystemStoreNumber")
	private String humanResourcesSystemStoreNumber;
	@XStreamAlias("createTimestamp")
	private Timestamp createTimestamp;
	@XStreamAlias("createUserId")
	private String createUserId;
	@XStreamAlias("humanResourcesSystemDepartmentNumber")
	private String humanResourcesSystemDepartmentNumber;
	@XStreamAlias("jobTitleCode")
	private String jobTitleCode;
	@XStreamAlias("requiredPositionFillDate")
	private Date requiredPositionFillDate;
	@XStreamAlias("authorizationPositionCount")
	private short authorizationPositionCount;
	@XStreamAlias("openPositionCount")
	private short openPositionCount;
	@XStreamAlias("fullTimeRequiredFlag")
	private String fullTimeRequiredFlag;
	@XStreamAlias("partorpeakTimeRequiredFlag")
	private String partorpeakTimeRequiredFlag;
	@XStreamAlias("pmRequiredFlag")
	private String pmRequiredFlag;
	@XStreamAlias("weekendRequiredFlag")
	private String weekendRequiredFlag;
	@XStreamAlias("activeFlag")
	private String activeFlag;
	@XStreamAlias("lastUpdateUserId")
	private String lastUpdateUserId;
	@XStreamAlias("lastUpdateTimestamp")
	private Timestamp lastUpdateTimestamp;

	public String getHumanResourcesSystemStoreNumber()
	{
		return humanResourcesSystemStoreNumber;
	}

	public void setHumanResourcesSystemStoreNumber(String aValue)
	{
		humanResourcesSystemStoreNumber = aValue;
	}

	public Timestamp getCreateTimestamp()
	{
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp aValue)
	{
		createTimestamp = aValue;
	}

	public String getCreateUserId()
	{
		return createUserId;
	}

	public void setCreateUserId(String aValue)
	{
		createUserId = aValue;
	}

	public String getHumanResourcesSystemDepartmentNumber()
	{
		return humanResourcesSystemDepartmentNumber;
	}

	public void setHumanResourcesSystemDepartmentNumber(String aValue)
	{
		humanResourcesSystemDepartmentNumber = aValue;
	}

	public String getJobTitleCode()
	{
		return jobTitleCode;
	}

	public void setJobTitleCode(String aValue)
	{
		jobTitleCode = aValue;
	}

	//getter method for requiredPositionFillDate
	public Date getRequiredPositionFillDate()
	{
		return requiredPositionFillDate;
	}

	//setter method for requiredPositionFillDate
	public void setRequiredPositionFillDate(Date aValue)
	{
		requiredPositionFillDate = aValue;
	}

	//getter method for authorizationPositionCount
	public short getAuthorizationPositionCount()
	{
		return authorizationPositionCount;
	}

	//setter method for authorizationPositionCount
	public void setAuthorizationPositionCount(short aValue)
	{
		authorizationPositionCount = aValue;
	}

	//getter method for openPositionCount
	public short getOpenPositionCount()
	{
		return openPositionCount;
	}

	//setter method for openPositionCount
	public void setOpenPositionCount(short aValue)
	{
		openPositionCount = aValue;
	}

	//getter method for fullTimeRequiredFlag
	public String getFullTimeRequiredFlag()
	{
		return fullTimeRequiredFlag;
	}

	//setter method for fullTimeRequiredFlag
	public void setFullTimeRequiredFlag(String aValue)
	{
		fullTimeRequiredFlag = aValue;
	}

	//getter method for partorpeakTimeRequiredFlag
	public String getPartorpeakTimeRequiredFlag()
	{
		return partorpeakTimeRequiredFlag;
	}

	//setter method for partorpeakTimeRequiredFlag
	public void setPartorpeakTimeRequiredFlag(String aValue)
	{
		partorpeakTimeRequiredFlag = aValue;
	}

	//getter method for pmRequiredFlag
	public String getPmRequiredFlag()
	{
		return pmRequiredFlag;
	}

	//setter method for pmRequiredFlag
	public void setPmRequiredFlag(String aValue)
	{
		pmRequiredFlag = aValue;
	}

	//getter method for weekendRequiredFlag
	public String getWeekendRequiredFlag()
	{
		return weekendRequiredFlag;
	}

	//setter method for weekendRequiredFlag
	public void setWeekendRequiredFlag(String aValue)
	{
		weekendRequiredFlag = aValue;
	}

	//getter method for activeFlag
	public String getActiveFlag()
	{
		return activeFlag;
	}

	//setter method for activeFlag
	public void setActiveFlag(String aValue)
	{
		activeFlag = aValue;
	}

	//getter method for lastUpdateUserId
	public String getLastUpdateUserId()
	{
		return lastUpdateUserId;
	}

	//setter method for lastUpdateUserId
	public void setLastUpdateUserId(String aValue)
	{
		lastUpdateUserId = aValue;
	}

	//getter method for lastUpdateTimestamp
	public Timestamp getLastUpdateTimestamp()
	{
		return lastUpdateTimestamp;
	}

	//setter method for lastUpdateTimestamp
	public void setLastUpdateTimestamp(Timestamp aValue)
	{
		lastUpdateTimestamp = aValue;
	}

}

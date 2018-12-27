package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

//TODO This seems to be the same as ReqScheduleTO.java, need to combine later
@XStreamAlias("ScheduleTimeDetails")

public class RequisitionScheduleTO implements Serializable
{
	private static final long serialVersionUID = -6254679706022754863L;

	// Instance variable for beginTimestamp
	@XStreamAlias("beginTimestamp")
	private Timestamp mBeginTimestamp;

	// Instance variable for interviewerAvailabilityCount (SEQ_NBR in HR_REQN_SCH)
	@XStreamAlias("interviewerAvailabilityCount")
	private short mInterviewerAvailabilityCount;

	// Instance variable for createTimestamp
	@XStreamAlias("createTimestamp")
	private Timestamp mCreateTimestamp;

	// Instance variable for createSystemUserId
	@XStreamAlias("createSystemUserId")
	private String mCreateSystemUserId;

	// Instance variable for lastUpdateSystemUserId
	@XStreamAlias("lastUpdateSystemUserId")
	private String mLastUpdateSystemUserId;

	// Instance variable for lastUpdateTimestamp
	@XStreamAlias("lastUpdateTimestamp")
	private Timestamp mLastUpdateTimestamp;

	// Instance variable for humanResourcesSystemStoreNumber
	@XStreamAlias("humanResourcesSystemStoreNumber")
	private String mHumanResourcesSystemStoreNumber;

	// Instance variable for endTimestamp
	@XStreamAlias("endTimestamp")
	private Timestamp mEndTimestamp;
	
	// reservation calendar id
	@XStreamOmitField()
	private int mCalendarId;	
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	@XStreamAlias("formattedBeginDate")
	private String formattedBeginDate;
	
	@XStreamAlias("formattedBeginTime")
	private String formattedBeginTime;
	
	@XStreamAlias("formatted24HrBeginTime")
	private String formatted24HrBeginTime;
	
	@XStreamAlias("formattedEndDate")
	private String formattedEndDate;
	
	@XStreamAlias("formattedEndTime")
	private String formattedEndTime;
	
	@XStreamAlias("formatted24HrEndTime")
	private String formatted24HrEndTime;
	//End - Added as part of Flex to HTML Conversion - 13 May 2015

	// getter method for createTimestamp
	public Timestamp getCreateTimestamp()
	{
		return mCreateTimestamp;
	} // end function getCreateTimestamp()

		// setter method for createTimestamp
	public void setCreateTimestamp(Timestamp crtTs)
	{
		mCreateTimestamp = crtTs;
	} // end function setCreateTimestamp()

	// getter method for createSystemUserId
	public String getCreateSystemUserId()
	{
		return mCreateSystemUserId;
	} // end function getCreateSystemUserId()

	// setter method for createSystemUserId
	public void setCreateSystemUserId(String crtSysUserId)
	{
		mCreateSystemUserId = crtSysUserId;
	} // end function setCreateSystemUserId()

	// getter method for lastUpdateSystemUserId
	public String getLastUpdateSystemUserId()
	{
		return mLastUpdateSystemUserId;
	} // end function getLastUpdateSystemUserId()

		// setter method for lastUpdateSystemUserId
	public void setLastUpdateSystemUserId(String lastUpdSysUserId)
	{
		mLastUpdateSystemUserId = lastUpdSysUserId;
	} // end function setLastUpdateSystemUserId()

	// getter method for lastUpdateTimestamp
	public Timestamp getLastUpdateTimestamp()
	{
		return mLastUpdateTimestamp;
	} // end function getLastUpdateTimestamp()

	// setter method for lastUpdateTimestamp
	public void setLastUpdateTimestamp(Timestamp lastUpdTs)
	{
		mLastUpdateTimestamp = lastUpdTs;
	} // end function setLastUpdateTimestamp()

	public Timestamp getBeginTimestamp()
	{
		return mBeginTimestamp;
	} // end function getBeginTimestamp()

	public void setBeginTimestamp(Timestamp beginTimestamp)
	{
		mBeginTimestamp = beginTimestamp;
	} // end function setBeginTimestamp()

	// getter method for endTimestamp
	public Timestamp getEndTimestamp()
	{
		return mEndTimestamp;
	} // end function getEndTimestamp()

	// setter method for endTimestamp
	public void setEndTimestamp(Timestamp endTs)
	{
		mEndTimestamp = endTs;
	} // end function setEndTimestamp()

	// getter method for humanResourcesSystemStoreNumber
	public String getHumanResourcesSystemStoreNumber()
	{
		return mHumanResourcesSystemStoreNumber;
	} // end function getHumanResourcesSystemStoreNumber()

		// setter method for humanResourcesSystemStoreNumber
	public void setHumanResourcesSystemStoreNumber(String storeNbr)
	{
		mHumanResourcesSystemStoreNumber = storeNbr;
	} // end function setHumanResourcesSystemStoreNumber()

	// getter method for interviewerAvailabilityCount
	public short getInterviewerAvailabilityCount()
	{
		return mInterviewerAvailabilityCount;
	} // end function getInterviewerAvailabilityCount()

	// setter method for interviewerAvailabilityCount
	public void setInterviewerAvailabilityCount(short count)
	{
		mInterviewerAvailabilityCount = count;
	} // end function setInterviewerAvailabilityCount()	
	
	public int getCalendarId()
	{
		return mCalendarId;
	} // end function getCalendarId()
	
	public void setCalendarId(int calendarId)
	{
		mCalendarId = calendarId;
	} // end function setCalendarId()	

	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * @return formatted begin date
	 */
	public String getFormattedBeginDate() {
		return formattedBeginDate;
	}

	/**
	 * @param formattedBeginDate
	 */
	public void setFormattedBeginDate(String formattedBeginDate) {
		this.formattedBeginDate = formattedBeginDate;
	}

	/**
	 * @return formatted begin time
	 */
	public String getFormattedBeginTime() {
		return formattedBeginTime;
	}

	/**
	 * @param formattedBeginTime
	 */
	public void setFormattedBeginTime(String formattedBeginTime) {
		this.formattedBeginTime = formattedBeginTime;
	}

	/**
	 * @return formatted end date
	 */
	public String getFormattedEndDate() {
		return formattedEndDate;
	}

	/**
	 * @param formattedEndDate
	 */
	public void setFormattedEndDate(String formattedEndDate) {
		this.formattedEndDate = formattedEndDate;
	}

	/**
	 * @return formatted end time
	 */
	public String getFormattedEndTime() {
		return formattedEndTime;
	}

	/**
	 * @param formattedEndTime
	 */
	public void setFormattedEndTime(String formattedEndTime) {
		this.formattedEndTime = formattedEndTime;
	}

	/**
	 * @return formatted begin time in 24 hours format
	 */
	public String getFormatted24HrBeginTime() {
		return formatted24HrBeginTime;
	}

	/**
	 * @param formatted24HrBeginTime
	 */
	public void setFormatted24HrBeginTime(String formatted24HrBeginTime) {
		this.formatted24HrBeginTime = formatted24HrBeginTime;
	}

	/**
	 * @return formatted end time in 24 hours format
	 */
	public String getFormatted24HrEndTime() {
		return formatted24HrEndTime;
	}

	/**
	 * @param formatted24HrEndTime
	 */
	public void setFormatted24HrEndTime(String formatted24HrEndTime) {
		this.formatted24HrEndTime = formatted24HrEndTime;
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
	
}

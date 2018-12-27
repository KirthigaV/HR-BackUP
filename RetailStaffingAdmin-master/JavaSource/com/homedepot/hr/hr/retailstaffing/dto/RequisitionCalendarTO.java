/*
 * 
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: RequisitionCalendarTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.thoughtworks.xstream.annotations.XStreamAlias;

//Added the interface Comparable<RequisitionCalendarTO> for sorting purpose - For Flex to HTML conversion - 13 May 2015
@XStreamAlias("ScheduleDescriptionDetails")
public class RequisitionCalendarTO implements Comparable<RequisitionCalendarTO>
{
	// Instance variable for requisitionCalendarId
	@XStreamAlias("requisitionCalendarId")
	private int mRequisitionCalendarId;

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

	// Instance variable for requisitionCalendarDescription
	@XStreamAlias("requisitionCalendarDescription")
	private String mRequisitionCalendarDescription;

	// Instance variable for humanResourcesSystemStoreNumber
	@XStreamAlias("humanResourcesSystemStoreNumber")
	private String mHumanResourcesSystemStoreNumber;

	// Instance variable for the active flag
	@XStreamAlias("activeFlag")
	private boolean mActiveFlag;
	
	// calendar type indicator
	@XStreamAlias("type")
	private Short mType;
	
	// hire event id
	@XStreamAlias("hireEventId")
	private int mHireEventId;	
	
	// hire event site flag
	@XStreamAlias("hireEventSiteFlg")
	private boolean mHireEventSiteFlg;
	
	//hire event date
	@XStreamAlias("hireEventdate")
	private Date mHireEventDate;
	
	// number of stores participating in event
	@XStreamAlias("numParticipatingStores")
	private int mNumParticipatingStores;

	// participating stores in event
	@XStreamAlias("participatingStores")
	private String mParticipatingStores;
	
	// number of scheduled slots
	@XStreamAlias("scheduledInterviewSlots")
	private int mScheduledInterviewSlots;
	
	// Store number that the Event was created by
	@XStreamAlias("eventCreatedByStore")
	private String mEventCreatedByStore;
	/*
	 * Number of active requisitions associated with this
	 * calendar. This is an object instead of a primitive so
	 * it can be null in the XStream generated XML
	 */
	@XStreamAlias("activeRequisitionCount")
	private Integer mActvReqnCount;
	
	// Number of available future interview slots 
	@XStreamAlias("availableInterviewSlots")
	private int mAvailIntvwSlots;

	//Hire Event Begin Date
	@XStreamAlias("hireEventBeginDate")
	private Date mHireEventBeginDate;
	
	//Hire Event End Date
	@XStreamAlias("hireEventEndDate")
	private Date mHireEventEndDate;
	
	//Hire Event Location Desc
	@XStreamAlias("hireEventLocationDescription")
	private String mHireEventLocationDescription;
	
	//Added for FMS 7894 January 2016 CR's
	//The Last Timestamp that Availability was added to a Calendar
	@XStreamAlias("availabilityAddedTimestamp")
	private Timestamp availabilityAddedTimestamp;
	
	/**
	 * Added as part of Flex to HTML conversion - 13 May 2015
	 * Description: To sort the PhoneScreenIntrwDetailsTO based upon the type and calendarDescription
	 */
	@Override
	public int compareTo(RequisitionCalendarTO requisitionCalendarTO) {
		int result = 0;
		if(mType!=null && requisitionCalendarTO!=null && requisitionCalendarTO.getType()!=null){
			result = mType.compareTo(requisitionCalendarTO.getType());
		}

		if(result!=0){
			return result;
		}
		if(!Util.isNullString(mRequisitionCalendarDescription) 
				&& requisitionCalendarTO!=null
				&& !Util.isNullString(requisitionCalendarTO.getRequisitionCalendarDescription()))
		{
			result= mRequisitionCalendarDescription.toUpperCase().compareTo(requisitionCalendarTO.getRequisitionCalendarDescription().toUpperCase());
		}
		return result; 
	} 
	
	
	@Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
 
        if (!(o instanceof RequisitionCalendarTO)) {
            return false;
        }
        return false;
    }
	
	@Override
	public int hashCode()
	{
		return 0;
	}
	
	// getter method for requisitionCalendarId
	public int getRequisitionCalendarId()
	{
		return mRequisitionCalendarId;
	} // end function getRequisitionCalendarId()

	// setter method for requisitionCalendarId
	public void setRequisitionCalendarId(int calId)
	{
		mRequisitionCalendarId = calId;
	} // end function setRequisitionCalendarId()

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
	public void setLastUpdateSystemUserId(String crtSysUserId)
	{
		mLastUpdateSystemUserId = crtSysUserId;
	} // end function setLastUpdateSystemUserId)(

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

	// getter method for requisitionCalendarDescription
	public String getRequisitionCalendarDescription()
	{
		return mRequisitionCalendarDescription;
	} // end function getRequisitionCalendarDescription()

	// setter method for requisitionCalendarDescription
	public void setRequisitionCalendarDescription(String reqCalDesc)
	{
		mRequisitionCalendarDescription = reqCalDesc;
	} // end function setRequisitionCalendarDescription()

	// getter method for humanResourcesSystemStoreNumber
	public String getHumanResourcesSystemStoreNumber()
	{
		return mHumanResourcesSystemStoreNumber;
	} // end function getHumanResourcesSystemStoreNumber()

	// setter method for humanResourcesSystemStoreNumber
	public void setHumanResourcesSystemStoreNumber(String hrSysStrNbr)
	{
		mHumanResourcesSystemStoreNumber = hrSysStrNbr;
	} // end function setHumanResourcesSystemStoreNumber()

	// getter method for activeFlag
	public boolean getActiveFlag()
	{
		return mActiveFlag;
	} // end function getActiveFlag()
	
	// setter method for activeFlag
	public void setActiveFlag(boolean activeFlag)
	{
		mActiveFlag = activeFlag;
	} // end function setActiveFlag()

	// getter method for active requisition count
	public Integer getActvReqnCount()
	{
		return mActvReqnCount;
	} // end function getActvReqnCount()
	
	// setter method for active requisition count
	public void setActvReqnCount(Integer count)
	{
		mActvReqnCount = count;
	} // end function setActvReqnCount()
	
	// getter method for future interview count
	public int getAvailIntvwSlots()
	{
		return mAvailIntvwSlots;
	} // end function getAvailIntvwSlots()

	public void setAvailIntvwSlots(int slots)
	{
		mAvailIntvwSlots = slots;
	} // end function setAvailIntvwSlots()
	
	// getter method for the calendar type indicator
	public Short getType()
	{
		return mType;
	} // end function getType()
	
	// setter method for the calendar type indicator
	public void setType(Short type)
	{
		mType = type;
	} // end function setType()
	
	// getter method for the hire event id
	public int getHireEventId()
	{
		return mHireEventId;
	} // end function getHireEventId()
	
	// setter method for hire event id
	public void setHireEventId(int hireEventId)
	{
		mHireEventId = hireEventId;
	} // end function setHireEventId()
	
	// getter method for the hire event site flag
	public boolean getHireEventSiteFlg()
	{
		return mHireEventSiteFlg;
	} // end function getHireEventSiteFlg()
	
	// setter method for hire event site glag
	public void setHireEventSiteFlg(Boolean hireEventSiteFlg)
	{
		mHireEventSiteFlg = hireEventSiteFlg;
	} // end function setHireEventId()	
	
	// getter method for the hire event date
	public Date getHireEventDate()
	{
		return mHireEventDate;
	} // end function getHireEventDate()
	
	// setter method for hire event date
	public void setHireEventDate(Date hireEventDate)
	{
		mHireEventDate = hireEventDate;
	} // end function setHireEventDate()	
	
	// getter method for the number of participating stores
	public int getNumParticipatingStores()
	{
		return mNumParticipatingStores;
	} // end function getNumParticipatingStores()
	
	// setter method for the number of participating stores
	public void setNumParticipatingStores(int numParticipatingStores)
	{
		mNumParticipatingStores = numParticipatingStores;
	} // end function setNumParticipatingStores()	

	// getter method for the participating stores
	public String getParticipatingStores()
	{
		return mParticipatingStores;
	} // end function getParticipatingStores()
	
	// setter method for the participating stores
	public void setParticipatingStores(String participatingStores)
	{
		mParticipatingStores = participatingStores;
	} // end function setNumParticipatingStores()
	
	// getter method for the number of scheduled slots
	public int getScheduledInterviewSlots()
	{
		return mScheduledInterviewSlots;
	} // end function getScheduledInterviewSlots()
	
	// setter method for the number of scheduled slots
	public void setScheduledInterviewSlots(int scheduledInterviewSlots)
	{
		mScheduledInterviewSlots = scheduledInterviewSlots;
	} // end function setScheduledInterviewSlots()	

	// getter method for the event created by store
	public String getEventCreatedByStore()
	{
		return mEventCreatedByStore;
	} // end function getEventCreatedByStore()
	
	// setter method for the event created by store
	public void setEventCreatedByStore(String eventCreatedByStore)
	{
		mEventCreatedByStore = eventCreatedByStore;
	} // end function setEventCreatedByStore()	
	
	// getter method for the event begin date
	public Date getHireEventBeginDate()
	{
		return mHireEventBeginDate;
	} // end function getHireEventBeginDate()
	
	// setter method for the event begin date
	public void setHireEventBeginDate(Date hireEventBeginDate)
	{
		mHireEventBeginDate = hireEventBeginDate;
	} // end function setHireEventBeginDate()	
	
	// getter method for the event end date
	public Date getHireEventEndDate()
	{
		return mHireEventEndDate;
	} // end function getHireEventEndDate()
	
	// setter method for the event end date
	public void setHireEventEndDate(Date hireEventEndDate)
	{
		mHireEventEndDate = hireEventEndDate;
	} // end function setHireEventEndDate()	
	
	// getter method for the event location desc
	public String getHireEventLocationDescription()
	{
		return mHireEventLocationDescription;
	} // end function getHireEventLocationDescription()
	
	// setter method for the event location desc
	public void setHireEventLocationDescription(String hireEventLocationDescription)
	{
		mHireEventLocationDescription = hireEventLocationDescription;
	} // end function setHireEventLocationDescription()	


	/**
	 * @return the availabilityAddedTimestamp
	 */
	public Timestamp getAvailabilityAddedTimestamp() {
		return availabilityAddedTimestamp;
	}


	/**
	 * @param availabilityAddedTimestamp the availabilityAddedTimestamp to set
	 */
	public void setAvailabilityAddedTimestamp(Timestamp availabilityAddedTimestamp) {
		this.availabilityAddedTimestamp = availabilityAddedTimestamp;
	}

}

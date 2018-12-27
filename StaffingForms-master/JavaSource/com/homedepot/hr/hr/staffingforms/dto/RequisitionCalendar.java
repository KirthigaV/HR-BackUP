package com.homedepot.hr.hr.staffingforms.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionCalendar.java
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class represents represent the breakdown of interview slots by status for a given day
 * <br>
 * Using the XStream API to marshal this object into an XML will produce the following
 * format:<br>
 * <br>
 * <code>
 * &lt;requisitionCalendar&gt;<br>
 * &nbsp;&lt;id&gt;&lt;/id&gt;<br>
 * &nbsp;&lt;createTimestamp&gt;&lt;/createTimestamp&gt;<br>
 * &nbsp;&lt;createSystemUserId&gt;&lt;/createSystemUserId&gt;<br>
 * &nbsp;&lt;lastUpdateTimestamp&gt;&lt;/lastUpdateTimestamp&gt;<br>
 * &nbsp;&lt;lastUpdateSystemUserId&gt;&lt;/lastUpdateSystemUserId&gt;<br>
 * &nbsp;&lt;description&gt;&lt;/description&gt;<br>
 * &nbsp;&lt;storeNumber&gt;&lt;/storeNumber&gt;<br>
 * &nbsp;&lt;calendarTypeCode&gt;&lt;/calendarTypeCode&gt;<br>
 * &nbsp;&lt;activeFlag&gt;&lt;/activeFlag&gt;<br>
 * &lt;/requisitionCalendar&gt;<br>
 * </code>
 * <br>
 * Only non-null member variables will be present in the XML generated by
 * the XStream API.
 */
@XStreamAlias("requisitionCalendar")
public class RequisitionCalendar implements Serializable
{
    private static final long serialVersionUID = -6422029227080104060L;
    
    /** calendar id */
    @XStreamAlias("id")
	private int mId;
    /** date/time the calendar was created */
    @XStreamAlias("createTimestamp")
	private Timestamp mCrtTs;
    /** id of the user that created the calendar */
    @XStreamAlias("createSystemUserId")
	private String mCrtSysUsrId;
    /** date/time this calendar was last updated */
    @XStreamAlias("lastUpdateTimestamp")
	private Timestamp mLastUpdTs;
    /** id of the last user that updated this calendar */
    @XStreamAlias("lastUpdateSystemUserId")
	private String mLastUpdSysUserId;
    /** calendar name */
    @XStreamAlias("description")
	private String mDesc;
    /** store number */
    @XStreamAlias("storeNumber")
	private String mStrNbr;
    /** calendar type code */
    @XStreamAlias("calendarTypeCode")
	private Short mCalTypCd;
    /** active flag */
    @XStreamAlias("activeFlag")
	private boolean mActv;
    /** hire event id */
    @XStreamAlias("hireEventId")
    private int mHireEventId;
	
    /**
     * @return the calendar id
     */
	public int getId()
	{
		return mId;
	} // end function getId()
	
	/**
	 * @param id the calendar id
	 */
	public void setId(int id)
	{
		mId = id;
	} // end function setId()
	
	/**
	 * @return date/time the calendar was created
	 */
	public Timestamp getCrtTs()
	{
		return mCrtTs;
	} // end function getCrtTs()
	
	/**
	 * @param crtTs date/time the calendar was created
	 */
	public void setCrtTs(Timestamp crtTs)
	{
		mCrtTs = crtTs;
	} // end function setCrtTs()
	
	/**
	 * @return id of the user that created the calendar
	 */
	public String getCrtSysUsrId()
	{
		return mCrtSysUsrId;
	} // end function getCrtSysUsrId()
	
	/**
	 * @param crtSysUsrId id of the user that created the calendar
	 */
	public void setCrtSysUsrId(String crtSysUsrId)
	{
		mCrtSysUsrId = crtSysUsrId;
	} // end function setCrtSysUsrId()
	
	/**
	 * @return date/time this calendar was last updated
	 */
	public Timestamp getLastUpdTs()
	{
		return mLastUpdTs;
	} // end function getLastUpdTs()
	
	/**
	 * @param lastUpdTs date/time this calendar was last updated
	 */
	public void setLastUpdTs(Timestamp lastUpdTs)
	{
		mLastUpdTs = lastUpdTs;
	} // end function setLastUpdTs()
	
	/**
	 * @return id of the last user that updated this calendar
	 */
	public String getLastUpdSysUserId()
	{
		return mLastUpdSysUserId;
	} // end function getLastUpdSysUserId()
	
	/**
	 * @param lastUpdSysUserId id of the last user that updated this calendar 
	 */
	public void setLastUpdSysUserId(String lastUpdSysUserId)
	{
		mLastUpdSysUserId = lastUpdSysUserId;
	} // end function setLastUpdSysUserId()
	
	/**
	 * @return calendar name 
	 */
	public String getDesc()
	{
		return mDesc;
	} // end function getDesc()
	
	/**
	 * @param desc calendar name
	 */
	public void setDesc(String desc)
	{
		mDesc = desc;
	} // end function setDesc()
	
	/**
	 * @return store number
	 */
	public String getStrNbr()
	{
		return mStrNbr;
	} // end function getStrNbr()
	
	/**
	 * @param strNbr store number
	 */
	public void setStrNbr(String strNbr)
	{
		mStrNbr = strNbr;
	} // end function setStrNbr()
	
	/**
	 * @return calendar type code
	 */
	public Short getCalTypCd()
	{
		return mCalTypCd;
	} // end function getCalTypCd()
	
	/**
	 * @param calTypCd calendar type code
	 */
	public void setCalTypCd(Short calTypCd)
	{
		mCalTypCd = calTypCd;
	} // end function setCalTypCd()
	
	/**
	 * @return true if the calendar is active false otherwise
	 */
	public boolean isActv()
	{
		return mActv;
	} // end function isActv()
	
	/**
	 * @param actv calendar active flag
	 */
	public void setActv(boolean actv)
	{
		mActv = actv;
	} // end function setActv()

	/* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */

    /**
     * @return the hire event id
     */
	public int getHireEventId()
	{
		return mHireEventId;
	} // end function getHireEventId()
	
	/**
	 * @param id the hire Event id
	 */
	public void setHireEventId(int hireEventId)
	{
		mHireEventId = hireEventId;
	} // end function setHireEventId()	
	
	
	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + mId;
	    return result;
    } // end function hashCode()

	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
	    if(this == obj)
	    {
		    return true;
	    } // end if(this == obj)
	    
	    if(obj == null)
	    {
		    return false;
	    } // end if(obj == null)
	    
	    if(!(obj instanceof RequisitionCalendar))
	    {
		    return false;
	    } // end if(!(obj instanceof RequisitionCalendar))
	    
	    RequisitionCalendar other = (RequisitionCalendar)obj;
	    if(mId != other.getId())
	    {
		    return false;
	    } // end if(mId != other.getId())
	    
	    return true;
    } // end function equals()
} // end class RequisitionCalendar
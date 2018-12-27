/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: ReqScheduleTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;



@XStreamAlias("ReqSchedule")
public class ReqScheduleTO implements Serializable
{
	private static final long serialVersionUID = 3334665243490199629L;

	@XStreamAlias("requisitionCalendarId")
	private String mReqCalId;

	@XStreamAlias("beginTimestamp")
	private Timestamp mBgnTs;
	@XStreamAlias("createTimestamp")
	private Timestamp mCrtTs;
	@XStreamAlias("createSystemUserId")
	private String mCrtSysUsrId;
	@XStreamAlias("lastUpdateSystemUserId")
	private String mLastUpdSysUsrId;
	@XStreamAlias("lastUpdateTimestamp")
	private Timestamp mLastUpdTs;
	@XStreamAlias("endTimestamp")
	private Timestamp mEndTs;
	@XStreamAlias("humanResourcesSystemStoreNumber")
	private String mHrSysStrNbr;
	@XStreamAlias("interviewerAvailabilityCount")
	private short mSeqNbr;
	@XStreamAlias("requisitionScheduleStatusCode")
	private Short mReqnSchStatCd;
	@XStreamAlias("requisitionScheduleReservationTimestamp")
	private Timestamp mReqnSchRsrvTs;
	@XStreamAlias("phoneScreenId")
	private Integer mPhnScrnId;
	
	public String getReqCalId()
	{
		return mReqCalId;
	} // end function getReqCalId()

	public void setReqCalId(String reqCalId)
	{
		mReqCalId = reqCalId;
	} // end function setReqCalId()
	
	public Timestamp getCrtTs()
	{
		return mCrtTs;
	} // end function getCrtTs()
	
	public void setCrtTs(Timestamp crtTs)
	{
		mCrtTs = crtTs;
	} // end function setCrtTs()

	public String getCrtSysUsrId()
	{
		return mCrtSysUsrId;
	} // end function getCrtSysUsrId()
	
	public void setCrtSysUsrId(String crtSysUsrId)
	{
		mCrtSysUsrId = crtSysUsrId;
	} // end function setCrtSysUsrId()

	public Timestamp getBgnTs()
	{
		return mBgnTs;
	} // end function getBgnTs()
	
	public void setBgnTs(Timestamp bgnTs)
	{
		mBgnTs = bgnTs;
	} // end function setBgnTs()

	public String getLastUpdSysUsrId()
	{
		return mLastUpdSysUsrId;
	} // end function getLastUpdSysUsrId()
	
	public void setLastUpdSysUsrId(String lastUpdSysUsrId)
	{
		mLastUpdSysUsrId = lastUpdSysUsrId;
	} // end function setLastUpdSysUsrId()

	public Timestamp getLastUpdTs()
	{
		return mLastUpdTs;
	} // end function getLastUpdTs()
	
	public void setLastUpdTs(Timestamp lastUpdTs)
	{
		mLastUpdTs = lastUpdTs;
	} // end function setLastUpdTs()

	public Timestamp getEndTs()
	{
		return mEndTs;
	} // end function getEndTs()
	
	public void setEndTs(Timestamp endTs)
	{
		mEndTs = endTs;
	} // end function setEndTs()

	public String getHrSysStrNbr()
	{
		return mHrSysStrNbr;
	} // end function getHrSysStrNbr()
	
	public void setHrSysStrNbr(String hrSysStrNbr)
	{
		mHrSysStrNbr = hrSysStrNbr;
	} // end function setHrSysStrNbr()
	
	public short getSeqNbr() 
	{
		return mSeqNbr;
	} // end function getSeqNbr()
	
	public void setSeqNbr(short seqNbr)
	{
		mSeqNbr = seqNbr;
	} // end function setSeqNbr()
	
	public Short getReqnSchStatCd()
	{
		return mReqnSchStatCd;
	} // end function getReqnSchStatCd()
	
	public void setReqnSchStatCd(Short reqnSchStatCd)
	{
		mReqnSchStatCd = reqnSchStatCd;
	} // end function setReqnSchStatCd()
	
	public Timestamp getReqnSchRsrvTs()
	{
		return mReqnSchRsrvTs;
	} // end function getReqnSchRsrvTs()
	
	public void setReqnSchRsrvTs(Timestamp reqnSchRsrvTs)
	{
		mReqnSchRsrvTs = reqnSchRsrvTs;
	} // end function setReqnSchRsrvTs()
	
	public Integer getPhnScrnId()
	{
		return mPhnScrnId;
	} // end function getPhnScrnId()
	
	public void setPhnScrnId(Integer phnScrnId)
	{
		mPhnScrnId = phnScrnId;
	} // end function setPhnScrnId()
} // end class ReqScheduleTO

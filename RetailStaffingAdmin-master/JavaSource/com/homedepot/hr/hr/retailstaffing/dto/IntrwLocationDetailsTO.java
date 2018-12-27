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
 * File Name: IntrwLocationDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * This class is used as to send Interview Location Details response in XML
 * format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("InterviewLocDtls")
public class IntrwLocationDetailsTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("interviewLocName")
	private String interviewLocName;
	@XStreamAlias("interviewLocId")
	private String interviewLocId;
	@XStreamAlias("interviewTyp")
	private String interviewTyp;
	@XStreamAlias("interviewer")
	private String interviewer;
	@XStreamAlias("interviewDate")
	private DateTO interviewDate;
	@XStreamAlias("interviewTime")
	private TimeStampTO interviewTime;
	@XStreamAlias("hiringStrEvntId")
	private String hiringStrEvntId;
	@XStreamAlias("nonHiringStrEvntId")
	private String nonHiringStrEvntId;
	@XStreamAlias("hiringEvntId")
	private String hiringEvntId;
	@XStreamAlias("otherLocId")
	private String otherLocId;
	@XStreamAlias("phone")
	private String phone;
	@XStreamAlias("add")
	private String add;
	@XStreamAlias("city")
	private String city;
	@XStreamAlias("state")
	private String state;
	@XStreamAlias("zip")
	private String zip;
	@XStreamAlias("sendPacketTime")
	private TimeStampTO sendPacketTime;
	
	// these two fields were added during RSA release 4 (for CTI phase 2 (inbound phone screen services))
	
	/*
	 * Timestamp representing the interview date/time
	 */
	@XStreamOmitField
	private Timestamp mIntrvwDtTm;
	/*
	 * Timezone of the interview location
	 */
	@XStreamOmitField
	private String mTimeZone;
	

	/**
	 * @return the interviewLocName
	 */
	public String getInterviewLocName()
	{
		return interviewLocName;
	}

	/**
	 * @param interviewLocName
	 *            the interviewLocName to set
	 */
	public void setInterviewLocName(String interviewLocName)
	{
		this.interviewLocName = interviewLocName;
	}

	/**
	 * @return the interviewLocId
	 */
	public String getInterviewLocId()
	{
		return interviewLocId;
	}

	/**
	 * @param interviewLocId
	 *            the interviewLocId to set
	 */
	public void setInterviewLocId(String interviewLocId)
	{
		this.interviewLocId = interviewLocId;
	}

	/**
	 * @return the interviewTyp
	 */
	public String getInterviewTyp()
	{
		return interviewTyp;
	}

	/**
	 * @param interviewTyp
	 *            the interviewTyp to set
	 */
	public void setInterviewTyp(String interviewTyp)
	{
		this.interviewTyp = interviewTyp;
	}

	/**
	 * @return the interviewer
	 */
	public String getInterviewer()
	{
		return interviewer;
	}

	/**
	 * @param interviewer
	 *            the interviewer to set
	 */
	public void setInterviewer(String interviewer)
	{
		this.interviewer = interviewer;
	}

	public DateTO getInterviewDate()
	{
		return interviewDate;
	}

	public void setInterviewDate(DateTO interviewDate)
	{
		this.interviewDate = interviewDate;
	}

	public TimeStampTO getInterviewTime()
	{
		return interviewTime;
	}

	public void setInterviewTime(TimeStampTO interviewTime)
	{
		this.interviewTime = interviewTime;
	}

	/**
	 * @return the hiringStrEvntId
	 */
	public String getHiringStrEvntId()
	{
		return hiringStrEvntId;
	}

	/**
	 * @param hiringStrEvntId
	 *            the hiringStrEvntId to set
	 */
	public void setHiringStrEvntId(String hiringStrEvntId)
	{
		this.hiringStrEvntId = hiringStrEvntId;
	}

	/**
	 * @return the nonHiringStrEvntId
	 */
	public String getNonHiringStrEvntId()
	{
		return nonHiringStrEvntId;
	}

	/**
	 * @param nonHiringStrEvntId
	 *            the nonHiringStrEvntId to set
	 */
	public void setNonHiringStrEvntId(String nonHiringStrEvntId)
	{
		this.nonHiringStrEvntId = nonHiringStrEvntId;
	}

	/**
	 * @return the hiringEvntId
	 */
	public String getHiringEvntId()
	{
		return hiringEvntId;
	}

	/**
	 * @param hiringEvntId
	 *            the hiringEvntId to set
	 */
	public void setHiringEvntId(String hiringEvntId)
	{
		this.hiringEvntId = hiringEvntId;
	}

	/**
	 * @return the otherLocId
	 */
	public String getOtherLocId()
	{
		return otherLocId;
	}

	/**
	 * @param otherLocId
	 *            the otherLocId to set
	 */
	public void setOtherLocId(String otherLocId)
	{
		this.otherLocId = otherLocId;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the add
	 */
	public String getAdd()
	{
		return add;
	}

	/**
	 * @param add
	 *            the add to set
	 */
	public void setAdd(String add)
	{
		this.add = add;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip()
	{
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip)
	{
		this.zip = zip;
	}

	public TimeStampTO getSendPacketTime()
	{
		return sendPacketTime;
	}

	public void setSendPacketTime(TimeStampTO sendPacketTime)
	{
		this.sendPacketTime = sendPacketTime;
	}

	/**
	 * Get the interview date/time
	 * 
	 * @return				Interview date/time
	 */
	public Timestamp getIntrvwDtTm()
	{
		return mIntrvwDtTm;
	} // end function getIntrvwDtTm()
	
	/**
	 * Set the interview date/time
	 * 
	 * @param intrvwDtTm	Interview date/time
	 */
	public void setIntrvwDtTm(Timestamp intrvwDtTm)
	{
		mIntrvwDtTm = intrvwDtTm;
	} // end function setIntrvwDtTm()
	
	/**
	 * Get the time zone of the interview location
	 * 
	 * @return				Time zone of the interview location
	 */
	public String getTimeZone()
	{
		return mTimeZone;
	} // end function getTimeZone()
	
	/**
	 * Set the time zone of the interview location
	 * @param timezone
	 */
	public void setTimeZone(String timezone)
	{
		mTimeZone = timezone;
	} // end function setTimeZone()
}

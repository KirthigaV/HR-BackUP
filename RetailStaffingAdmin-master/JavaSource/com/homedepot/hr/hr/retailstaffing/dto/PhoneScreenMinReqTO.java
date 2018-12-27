/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: PhoneScreenMinReqTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("question")
public class PhoneScreenMinReqTO implements Serializable
{
	private static final long serialVersionUID = 3334665243490199629L;

	@XStreamAlias("id")
	private String mId;

	@XStreamAlias("answer")
	private String mAnswer;

	private Short mSequenceNumber;

	public void setId(String idValue)
	{
		mId = idValue;
	}

	public String getId()
	{
		return mId;
	}

	public void setAnswer(String answer)
	{
		mAnswer = answer;
	}

	public String getAnswer()
	{
		return mAnswer;
	}

	public void setSequenceNumber(Short sequenceNumber)
	{
		mSequenceNumber = sequenceNumber;
	}

	public Short getSequenceNumber()
	{
		return mSequenceNumber;
	}

	@Override
	public String toString()
	{
		return String.format("PhoneScreenMinReq: id: %1$s, answer: %2$s, sequenceNbr: %3$d",
			mId, mAnswer, mSequenceNumber);
	} // end function toString()
}

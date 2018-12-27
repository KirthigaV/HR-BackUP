/*
 * 
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: InterviewScheduleRequestDetailsTO.java
 */


package com.homedepot.hr.hr.retailstaffing.dto;



import com.homedepot.hr.hr.retailstaffing.dto.BlcksInterviewTimeTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("InterviewScheduleRequestDetails")
public class InterviewScheduleRequestDetailsTO {
	
private static final long serialVersionUID = 362498820763181265L;
	
	@XStreamAlias("insertUpdate")
	private String insertUpdate ;
	
	@XStreamAlias("reqCalId")
	private int reqCalId;
	
	@XStreamAlias("BlcksInterviewTime")
	private BlcksInterviewTimeTO blcksInterviewTimeTO;

	public String getInsertUpdate() {
		return insertUpdate;
	}

	public void setInsertUpdate(String insertUpdate) {
		this.insertUpdate = insertUpdate;
	}

	public int getReqCalId() {
		return reqCalId;
	}

	public void setReqCalId(int reqCalId) {
		this.reqCalId = reqCalId;
	}

	public BlcksInterviewTimeTO getBlcksInterviewTimeTO() {
		return blcksInterviewTimeTO;
	}

	public void setBlcksInterviewTimeTO(BlcksInterviewTimeTO blcksInterviewTimeTO) {
		this.blcksInterviewTimeTO = blcksInterviewTimeTO;
	}
	
}

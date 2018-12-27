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
 * File Name: UpdateStaffingRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;
// import java.util.List;

// import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create UpdateStaffingRequest object  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("UpdateStaffingRequest")
public class UpdateStaffingRequest implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;
	
	@XStreamAlias("insertUpdate")
	private String insertUpdate ;
	
	@XStreamAlias("reqNbr")
	private String reqNbr ;
	
	@XStreamAlias("reqCalId")
	private int reqCalId ;	
	
	@XStreamAlias("staffingDetails")
	private StaffingDetailsTO staffingDtlTo ;

	public String getInsertUpdate() {
		return insertUpdate;
	}

	public void setInsertUpdate(String insertUpdate) {
		this.insertUpdate = insertUpdate;
	}

	public String getReqNbr() {
		return reqNbr;
	}

	public void setReqNbr(String reqNbr) {
		this.reqNbr = reqNbr;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public StaffingDetailsTO getStaffingDtlTo() {
		return staffingDtlTo;
	}

	public void setStaffingDtlTo(StaffingDetailsTO staffingDtlTo) {
		this.staffingDtlTo = staffingDtlTo;
	}

	public int getReqCalId() {
		return reqCalId;
	}

	public void setReqCalId(int reqCalId) {
		this.reqCalId = reqCalId;
	}
	

	
}

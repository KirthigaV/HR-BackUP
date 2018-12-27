package com.homedepot.hr.hr.staffingforms.service.request;
/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: CalendarRequest.java
 */
import java.io.Serializable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("DeleteParticipatingStoreRequest")
public class DeleteParticipatingStoreRequest implements Serializable
{

	private static final long serialVersionUID = -3493935852065093084L;

	@XStreamAlias("storeNum")
	private String storeNum;
	
	@XStreamAlias("hireEventId")
	private int hireEventId;

	public String getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(String storeNum) {
		this.storeNum = storeNum;
	}

	public int getHireEventId() {
		return hireEventId;
	}

	public void setHireEventId(int hireEventId) {
		this.hireEventId = hireEventId;
	}
		
} // end class DeleteParticipatingStoreRequest
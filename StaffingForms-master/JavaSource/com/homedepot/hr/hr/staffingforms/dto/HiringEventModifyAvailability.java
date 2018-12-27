package com.homedepot.hr.hr.staffingforms.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: Store.java
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.sql.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("modification")
public class HiringEventModifyAvailability implements Serializable
{

	private static final long serialVersionUID = 7571410702219135548L;


    @XStreamAlias("theDate")
	private Date theDate;

    @XStreamAlias("reqnCalId")
    private int reqnCalId;

    @XStreamAlias("moveDeleteAvailability")
    private String moveDeleteAvailability;
    
    @XStreamAlias("daysBetweenOriginalStartDateAndNewStartDate")
    private int daysBetweenOriginalStartDateAndNewStartDate;

	public Date getTheDate() {
		return theDate;
	}

	public void setTheDate(Date theDate) {
		this.theDate = theDate;
	}

	public int getReqnCalId() {
		return reqnCalId;
	}

	public void setReqnCalId(int reqnCalId) {
		this.reqnCalId = reqnCalId;
	}

	public String getMoveDeleteAvailability() {
		return moveDeleteAvailability;
	}

	public void setMoveDeleteAvailability(String moveDeleteAvailability) {
		this.moveDeleteAvailability = moveDeleteAvailability;
	}

	public int getDaysBetweenOriginalStartDateAndNewStartDate() {
		return daysBetweenOriginalStartDateAndNewStartDate;
	}

	public void setDaysBetweenOriginalStartDateAndNewStartDate(int daysBetweenOriginalStartDateAndNewStartDate) {
		this.daysBetweenOriginalStartDateAndNewStartDate = daysBetweenOriginalStartDateAndNewStartDate;
	}    
    
    
    
} // end class Store
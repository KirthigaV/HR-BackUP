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
 * File Name: StoreDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Hiring Event Participating Store details response in XML format
 * 
 * 
 */
@XStreamAlias("HiringEventsStoreDetails")
public class HiringEventsStoreDetailsTO
{

	@XStreamAlias("strNum")
	private String strNum;
	
	@XStreamAlias("strName")
	private String strName;

	@XStreamAlias("hireEventId")
	private int hireEventId;
	
	@XStreamAlias("activeReqCount")
	private int activeReqCount;

    @XStreamAlias("storeDeleteAllowed")
    private boolean storeDeleteAllowed;        
  
    @XStreamAlias("eventSiteFlg")
    private String eventSiteFlg;
    
    @XStreamAlias("eventReqnCalId")
    private int eventReqnCalId;
    
	public String getStrNum() {
		return strNum;
	}

	public void setStrNum(String strNum) {
		this.strNum = strNum;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public int getHireEventId() {
		return hireEventId;
	}

	public void setHireEventId(int hireEventId) {
		this.hireEventId = hireEventId;
	}

	public int getActiveReqCount() {
		return activeReqCount;
	}

	public void setActiveReqCount(int activeReqCount) {
		this.activeReqCount = activeReqCount;
	}

	public boolean isStoreDeleteAllowed() {
		return storeDeleteAllowed;
	}

	public void setStoreDeleteAllowed(boolean storeDeleteAllowed) {
		this.storeDeleteAllowed = storeDeleteAllowed;
	}

	public String getEventSiteFlg() {
		return eventSiteFlg;
	}

	public void setEventSiteFlg(String eventSiteFlg) {
		this.eventSiteFlg = eventSiteFlg;
	}

	public int getEventReqnCalId() {
		return eventReqnCalId;
	}

	public void setEventReqnCalId(int eventReqnCalId) {
		this.eventReqnCalId = eventReqnCalId;
	}


}

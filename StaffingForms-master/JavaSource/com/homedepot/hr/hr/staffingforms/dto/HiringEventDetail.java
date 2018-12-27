package com.homedepot.hr.hr.staffingforms.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("hiringEventDetail")
public class HiringEventDetail implements Serializable {

	private static final long serialVersionUID = 1356214927779223659L;

	@XStreamAlias("hireEventId")
	private int hireEventId;
	
	@XStreamAlias("reqnCalId")
	private int reqnCalId;
	
	@XStreamAlias("hiringEventType")
	private String hiringEventType;
	
	@XStreamAlias("eventName")
	private String eventName = null;
	
	@XStreamAlias("eventDt")
	private String eventDt = null;
	
	@XStreamAlias("eventDtEnd")
	private String eventDtEnd = null;
	
	@XStreamAlias("eventMgrName")
	private String eventMgrName = null;

	@XStreamAlias("eventMgrId")
	private String eventMgrId = null;

	@XStreamAlias("eventMgrTitle")
	private String eventMgrTitle = null;

	@XStreamAlias("eventMgrPhone")
	private String eventMgrPhone = null;

	@XStreamAlias("eventMgrEmail")
	private String eventMgrEmail = null;

	@XStreamAlias("strNumber")
	private String strNumber = null;

	@XStreamAlias("venueStoreName")
	private String venueStoreName = null;

	@XStreamAlias("eventAddress1")
	private String eventAddress1 = null;

	@XStreamAlias("eventCity")
	private String eventCity = null;

	@XStreamAlias("eventState")
	private String eventState = null;

	@XStreamAlias("eventZip")
	private String eventZip = null;

	@XStreamAlias("offSiteEvent")
	private String offSiteEvent = null;
	
	@XStreamAlias("eventMgrAssociateId")
	private String eventMgrAssociateId = null;
	
	@XStreamAlias("hireEventCreatedByStore")
	private String hireEventCreatedByStore;
	
	@XStreamAlias("locationChange")
	private boolean locationChange;
	
	@XStreamAlias("stores")
	private List<Store> stores;
	
	//These are not needed in any XML
	private short interviewLocationTypeCode;
	private String interviewerName;
	private Date interviewDate;
	private Timestamp interviewTimestamp;
	//*****************************
	
	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDt() {
		return eventDt;
	}

	public void setEventDt(String eventDt) {
		this.eventDt = eventDt;
	}

	public String getEventMgrName() {
		return eventMgrName;
	}

	public void setEventMgrName(String eventMgrName) {
		this.eventMgrName = eventMgrName;
	}

	public String getEventMgrId() {
		return eventMgrId;
	}

	public void setEventMgrId(String eventMgrId) {
		this.eventMgrId = eventMgrId;
	}

	public String getEventMgrTitle() {
		return eventMgrTitle;
	}

	public void setEventMgrTitle(String eventMgrTitle) {
		this.eventMgrTitle = eventMgrTitle;
	}

	public String getEventMgrPhone() {
		return eventMgrPhone;
	}

	public void setEventMgrPhone(String eventMgrPhone) {
		this.eventMgrPhone = eventMgrPhone;
	}

	public String getEventMgrEmail() {
		return eventMgrEmail;
	}

	public void setEventMgrEmail(String eventMgrEmail) {
		this.eventMgrEmail = eventMgrEmail;
	}

	public String getStrNumber() {
		return strNumber;
	}

	public void setStrNumber(String strNumber) {
		this.strNumber = strNumber;
	}

	public String getVenueStoreName() {
		return venueStoreName;
	}

	public void setVenueStoreName(String venueStoreName) {
		this.venueStoreName = venueStoreName;
	}

	public String getEventAddress1() {
		return eventAddress1;
	}

	public void setEventAddress1(String eventAddress1) {
		this.eventAddress1 = eventAddress1;
	}

	public String getEventCity() {
		return eventCity;
	}

	public void setEventCity(String eventCity) {
		this.eventCity = eventCity;
	}

	public String getEventState() {
		return eventState;
	}

	public void setEventState(String eventState) {
		this.eventState = eventState;
	}

	public String getEventZip() {
		return eventZip;
	}

	public void setEventZip(String eventZip) {
		this.eventZip = eventZip;
	}

	public String getOffSiteEvent() {
		return offSiteEvent;
	}

	public void setOffSiteEvent(String offSiteEvent) {
		this.offSiteEvent = offSiteEvent;
	}

	public String getEventMgrAssociateId() {
		return eventMgrAssociateId;
	}

	public void setEventMgrAssociateId(String eventMgrAssociateId) {
		this.eventMgrAssociateId = eventMgrAssociateId;
	}

	public List<Store> getStores() {
		return stores;
	}

	public void setStores(List<Store> stores) {
		this.stores = stores;
	}

	public String getHiringEventType() {
		return hiringEventType;
	}

	public void setHiringEventType(String hiringEventType) {
		this.hiringEventType = hiringEventType;
	}

	public String getEventDtEnd() {
		return eventDtEnd;
	}

	public void setEventDtEnd(String eventDtEnd) {
		this.eventDtEnd = eventDtEnd;
	}

	public int getHireEventId() {
		return hireEventId;
	}

	public void setHireEventId(int hireEventId) {
		this.hireEventId = hireEventId;
	}

	public int getReqnCalId() {
		return reqnCalId;
	}

	public void setReqnCalId(int reqnCalId) {
		this.reqnCalId = reqnCalId;
	}

	public String getHireEventCreatedByStore() {
		return hireEventCreatedByStore;
	}

	public void setHireEventCreatedByStore(String hireEventCreatedByStore) {
		this.hireEventCreatedByStore = hireEventCreatedByStore;
	}

	public boolean isLocationChange() {
		return locationChange;
	}

	public void setLocationChange(boolean locationChange) {
		this.locationChange = locationChange;
	}

	public short getInterviewLocationTypeCode() {
		return interviewLocationTypeCode;
	}

	public void setInterviewLocationTypeCode(short interviewLocationTypeCode) {
		this.interviewLocationTypeCode = interviewLocationTypeCode;
	}

	public String getInterviewerName() {
		return interviewerName;
	}

	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	public Date getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(Date interviewDate) {
		this.interviewDate = interviewDate;
	}

	public Timestamp getInterviewTimestamp() {
		return interviewTimestamp;
	}

	public void setInterviewTimestamp(Timestamp interviewTimestamp) {
		this.interviewTimestamp = interviewTimestamp;
	}

}

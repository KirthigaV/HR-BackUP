/*
 * Created on October 15, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: ApplicantPersonalInfoTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send personal info response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("CandidateHistory")
public class ApplHistoryInfoTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("reqNumber")
	private String reqNumber;
	@XStreamAlias("store")
	private String store;
	@XStreamAlias("dept")
	private String dept;
	@XStreamAlias("title")
	private String title;
	@XStreamAlias("intvDisposition")
	private String intvDisposition;
	@XStreamAlias("offerStatus")
	private String offerStatus;
	@XStreamAlias("offerResults")
	private String offerResults;
	@XStreamAlias("lastUpdated")
	private String lastUpdated;	
	
	public String getReqNumber() {
		return reqNumber;
	}
	public void setReqNumber(String reqNumber) {
		this.reqNumber = reqNumber;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntvDisposition() {
		return intvDisposition;
	}
	public void setIntvDisposition(String intvDisposition) {
		this.intvDisposition = intvDisposition;
	}
	public String getOfferStatus() {
		return offerStatus;
	}
	public void setOfferStatus(String offerStatus) {
		this.offerStatus = offerStatus;
	}
	public String getOfferResults() {
		return offerResults;
	}
	public void setOfferResults(String offerResults) {
		this.offerResults = offerResults;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
	
}

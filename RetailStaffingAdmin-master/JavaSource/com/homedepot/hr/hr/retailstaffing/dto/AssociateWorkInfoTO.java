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
 * File Name: ApplAssociateInfoTO.java
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
@XStreamAlias("AssociateInfo")
public class AssociateWorkInfoTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("currStore")
	private String currStore;
	@XStreamAlias("currDept")
	private String currDept;
	@XStreamAlias("currTitle")
	private String currTitle;
	@XStreamAlias("currStatus")
	private String currStatus;
	@XStreamAlias("hireDate")
	private String hireDate;
	public String getCurrStore() {
		return currStore;
	}
	public void setCurrStore(String currStore) {
		this.currStore = currStore;
	}
	public String getCurrDept() {
		return currDept;
	}
	public void setCurrDept(String currDept) {
		this.currDept = currDept;
	}
	public String getCurrTitle() {
		return currTitle;
	}
	public void setCurrTitle(String currTitle) {
		this.currTitle = currTitle;
	}
	public String getCurrStatus() {
		return currStatus;
	}
	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}
	public String getHireDate() {
		return hireDate;
	}
	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}
	
}

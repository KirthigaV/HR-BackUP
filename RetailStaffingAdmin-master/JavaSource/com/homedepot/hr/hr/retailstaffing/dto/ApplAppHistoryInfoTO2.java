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
@XStreamAlias("ApplicationHistory2")
public class ApplAppHistoryInfoTO2 implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("position")
	private String position;
	@XStreamAlias("dept")
	private String dept;
	@XStreamAlias("lastUpdatedP")
	private String lastUpdatedP;
	
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getLastUpdatedP() {
		return lastUpdatedP;
	}
	public void setLastUpdatedP(String lastUpdatedP) {
		this.lastUpdatedP = lastUpdatedP;
	}
		
}

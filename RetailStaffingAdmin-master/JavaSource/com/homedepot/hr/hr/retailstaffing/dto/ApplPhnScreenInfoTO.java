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
@XStreamAlias("PhnScreenHistory")
public class ApplPhnScreenInfoTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("requisitionNum")
	private String requisitionNum;
	@XStreamAlias("storeNum")
	private String storeNum;
	@XStreamAlias("deptNum")
	private String deptNum;
	@XStreamAlias("job")
	private String job;
	@XStreamAlias("phnScreenStatus")
	private String phnScreenStatus;
	@XStreamAlias("status")
	private String status;
	@XStreamAlias("phnScreenType")
	private String phnScreenType;
	@XStreamAlias("lastUpdate")
	private String lastUpdate;
	
	public String getRequisitionNum() {
		return requisitionNum;
	}
	public void setRequisitionNum(String requisitionNum) {
		this.requisitionNum = requisitionNum;
	}
	public String getStoreNum() {
		return storeNum;
	}
	public void setStoreNum(String storeNum) {
		this.storeNum = storeNum;
	}
	public String getDeptNum() {
		return deptNum;
	}
	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getPhnScreenStatus() {
		return phnScreenStatus;
	}
	public void setPhnScreenStatus(String phnScreenStatus) {
		this.phnScreenStatus = phnScreenStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPhnScreenType() {
		return phnScreenType;
	}
	public void setPhnScreenType(String phnScreenType) {
		this.phnScreenType = phnScreenType;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}

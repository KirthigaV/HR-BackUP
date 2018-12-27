/*
 * Created on Nov 20, 2009
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application: RetailStaffing
 *
 * File Name: ApplToReqDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send candidate details in response in XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("ApplToReq")
public class ApplToReqDetailsTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;
	
	@XStreamAlias("StoreNumber")
	private String StoreNumber;
	@XStreamAlias("ReqNumber")
	private String ReqNumber;
	@XStreamAlias("ApplId")
	private String ApplId;
	@XStreamAlias("ApplName")
	private String ApplName;
	@XStreamAlias("ApplType")
	private String ApplType;
	
	
	public String getStoreNumber() {
		return StoreNumber;
	}
	public void setStoreNumber(String storeNumber) {
		StoreNumber = storeNumber;
	}
	public String getReqNumber() {
		return ReqNumber;
	}
	public void setReqNumber(String reqNumber) {
		ReqNumber = reqNumber;
	}
	public String getApplId() {
		return ApplId;
	}
	public void setApplId(String applId) {
		ApplId = applId;
	}
	public String getApplName() {
		return ApplName;
	}
	public void setApplName(String applName) {
		ApplName = applName;
	}
	public String getApplType() {
		return ApplType;
	}
	public void setApplType(String applType) {
		ApplType = applType;
	}
	
}

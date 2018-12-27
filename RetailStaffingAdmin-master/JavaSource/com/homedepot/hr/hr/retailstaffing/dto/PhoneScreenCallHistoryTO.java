/*
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffingRequest
 *
 * File Name: LanguageSkillsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used to send Phone Screen/Interview Status details response in XML format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("PhoneScreenCallHistoryDetail")
public class PhoneScreenCallHistoryTO
{
	@XStreamAlias("callType")
	String callType;
	@XStreamAlias("callTs")
	String callTs;
	@XStreamAlias("callDisposition")
	String callDisposition;
	
	public String getCallType() {
		return callType;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public String getCallTs() {
		return callTs;
	}
	public void setCallTs(String callTs) {
		this.callTs = callTs;
	}
	public String getCallDisposition() {
		return callDisposition;
	}
	public void setCallDisposition(String callDisposition) {
		this.callDisposition = callDisposition;
	}

	
}

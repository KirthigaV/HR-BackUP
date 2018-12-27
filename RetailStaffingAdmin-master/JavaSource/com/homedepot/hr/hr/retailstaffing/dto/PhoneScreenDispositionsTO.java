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
 * This class is used as to send Phone Screen Disposition details  response in XML format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("PhoneScreenDispositionDetail")
public class PhoneScreenDispositionsTO
{
	@XStreamAlias("dispositionCode")
	short dispositionCode;
	@XStreamAlias("dispositionDesc")
	String dispositionDesc;
	
	public short getDispositionCode() {
		return dispositionCode;
	}
	public void setDispositionCode(short dispositionCode) {
		this.dispositionCode = dispositionCode;
	}
	public String getDispositionDesc() {
		return dispositionDesc;
	}
	public void setDispositionDesc(String dispositionDesc) {
		this.dispositionDesc = dispositionDesc;
	}
	
}

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
 * This class is used as to send Schedule Preference details response in XML
 * format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("schdPrefDetail")
public class SchedulePreferenceTO {
	@XStreamAlias("daySegCd")
	String daySegCd;
	@XStreamAlias("wkDayNbr")
	String wkDayNbr;

	public String getDaySegCd() {
		return daySegCd;
	}

	public void setDaySegCd(String daySegCd) {
		this.daySegCd = daySegCd;
	}

	public String getWkDayNbr() {
		return wkDayNbr;
	}

	public void setWkDayNbr(String wkDayNbr) {
		this.wkDayNbr = wkDayNbr;
	}

}

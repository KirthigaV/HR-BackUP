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
 * File Name: StateDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Auto-Attach Job Titles response in XML format
 * 
 * 
 */
@XStreamAlias("AutoAttachJobTitles")
public class AutoAttachJobTitlesTO
{

	@XStreamAlias("aaJobTitle")
	private String aaJobTitle;

	@XStreamAlias("aaDeptCd")
	private String aaDeptCd;
	
	public String getAaJobTitle() {
		return aaJobTitle;
	}

	public void setAaJobTitle(String aaJobTitle) {
		this.aaJobTitle = aaJobTitle;
	}

	public String getAaDeptCd() {
		return aaDeptCd;
	}

	public void setAaDeptCd(String aaDeptCd) {
		this.aaDeptCd = aaDeptCd;
	}	

}

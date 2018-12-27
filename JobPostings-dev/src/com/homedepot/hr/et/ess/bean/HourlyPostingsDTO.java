package com.homedepot.hr.et.ess.bean;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

public class HourlyPostingsDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String storeIdValue;	// Contains id of a store		
	private List hourlyPostingsList;// Store job posting entity objects in a collection
	private String storeLocale;
	private List consentDecreeJobCategoryCodeList;
	private String activeFlag;
	private Date endDate;
	
	
	public String getStoreIdValue() {
		return storeIdValue;
	}
	public void setStoreIdValue(String storeIdValue) {
		this.storeIdValue = storeIdValue;
	}
	public List getHourlyPostingsList() {
		return hourlyPostingsList;
	}
	public void setHourlyPostingsList(List hourlyPostingsList) {
		this.hourlyPostingsList = hourlyPostingsList;
	}
	public String getStoreLocale() {
		return storeLocale;
	}
	public void setStoreLocale(String storeLocale) {
		this.storeLocale = storeLocale;
	}
	public List getConsentDecreeJobCategoryCodeList() {
		return consentDecreeJobCategoryCodeList;
	}
	public void setConsentDecreeJobCategoryCodeList(
			List consentDecreeJobCategoryCodeList) {
		this.consentDecreeJobCategoryCodeList = consentDecreeJobCategoryCodeList;
	}
	public String getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	

}

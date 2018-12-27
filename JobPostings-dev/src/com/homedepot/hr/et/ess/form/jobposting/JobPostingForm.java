package com.homedepot.hr.et.ess.form.jobposting;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.ValidatorActionForm;

/**
 * Action form class to handle job posting data
 * @author HP
 * @version 1.0
 */
public class JobPostingForm extends ValidatorActionForm{
	
	private static final long serialVersionUID = 8288264912346174046L;
	private String hiddenValue; // Contains page type to be displayed
	private String pageType; // Contains page type to be displayed
	private String storeIdValue;	// Contains id of a store		
	private List hourlyPostingsList;// Store job posting entity objects in a collection
	private ArrayList storeIDList; // Store store objects in a collection 
	private String pageNo;
	private String storeLocale;
	private List consentDecreeJobCategoryCodeList;
	private String activeFlag;
	private Date endDate;
	private boolean jobPosInd;
	private boolean searchInd;
	
	

	public JobPostingForm() 
	{
		super();
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		super.reset(mapping, request);
		
	}
	
	public ActionErrors validate(ActionMapping mapping,	HttpServletRequest request){
		ActionErrors errors = null;
		ActionMessages messages = new ActionMessages();

		if ("HPpage".equals(pageNo)) {
			errors = super.validate(mapping, request);
			
			if (!errors.isEmpty()) {
				return errors;
			}
		}

		return errors;

	}
	
	public void clearMyForm(){
		hiddenValue=null;
		pageType=null;
		storeIDList=null;
		hourlyPostingsList=null;
		storeIdValue=null;
		pageNo=null;
		storeLocale=null;
		consentDecreeJobCategoryCodeList=null;
		activeFlag=null;
		endDate=null;
		searchInd=false;
	}

	
	public boolean isSearchInd() {
		return searchInd;
	}

	public void setSearchInd(boolean searchInd) {
		this.searchInd = searchInd;
	}

	public boolean isJobPosInd() {
		return jobPosInd;
	}

	public void setJobPosInd(boolean jobPosInd) {
		this.jobPosInd = jobPosInd;
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

	public String getStoreIdValue() {
		return storeIdValue;
	}

	public void setStoreIdValue(String storeIdValue) {
		this.storeIdValue = storeIdValue;
	}

	public ArrayList getStoreIDList() {
		return storeIDList;
	}

	public void setStoreIDList(ArrayList storeIDList) {
		this.storeIDList = storeIDList;
	}

	public String getHiddenValue() {
		return hiddenValue;
	}

	public void setHiddenValue(String hiddenValue) {
		this.hiddenValue = hiddenValue;
	}

	public String getPageType() {
		return pageType;
	}

	public void setPageType(String pageType) {
		this.pageType = pageType;
	}

	

	public List getHourlyPostingsList() {
		return hourlyPostingsList;
	}

	public void setHourlyPostingsList(List hourlyPostingsList) {
		this.hourlyPostingsList = hourlyPostingsList;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}
	
}

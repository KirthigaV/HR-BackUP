package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Interview Status details  response in XML format
 * 
 * @author dxg8002
 * 
 */
@XStreamAlias("intvwInfo")
public class InterviewInformationTO implements Serializable {
	
	private static final long serialVersionUID = -1612116675287981814L;

	@XStreamAlias("updateIntvwDtls")
	private String updateIntvwDtls;
	
	@XStreamAlias("updateOfferDtls")
	private String updateOfferDtls;   

	@XStreamAlias("userId")
	private String userId;
	
	@XStreamAlias("wasInterviewCompleted")
	private String wasInterviewCompleted;
	
	@XStreamAlias("doesAvailabilityMatch")
	private String doesAvailabilityMatch;
	
	@XStreamAlias("noInterviewReason")
	private String noInterviewReason;
	
	@XStreamAlias("interviewerName")
	private String interviewerName;
	
	@XStreamAlias("selectedSig")
	private String selectedSig;	
 
	@XStreamAlias("dateOfIntvw")
	private String dateOfIntvw;
	
	@XStreamAlias("intvwQuestion1")
	private String intvwQuestion1;
	
	@XStreamAlias("intvwQuestion2")
	private String intvwQuestion2;
	
	@XStreamAlias("intvwQuestion3")
	private String intvwQuestion3;
	
	@XStreamAlias("intvwQuestion4")
	private String intvwQuestion4;
	
	@XStreamAlias("intvwQuestion5")
	private String intvwQuestion5;
	
	@XStreamAlias("intvwQuestion6")
	private String intvwQuestion6;
	
	@XStreamAlias("intvwQuestion7")
	private String intvwQuestion7;
	
	@XStreamAlias("intvwQuestion8")
	private String intvwQuestion8;
	
	@XStreamAlias("intvwQuestion9")
	private String intvwQuestion9;
	
	@XStreamAlias("intvwQuestion10")
	private String intvwQuestion10;	
	
	@XStreamAlias("licNum")
	private String licNum;
	
	@XStreamAlias("licState")
	private String licState;	

	@XStreamAlias("licExpDate")
	private String licExpDate;
	
	@XStreamAlias("backgroundConsent")
	private String backgroundConsent;
	
	@XStreamAlias("updateLicenseDtls")
	private String updateLicenseDtls;

	//Added for FMS 7894 January 2016 CR's
	@XStreamAlias("doNotConsiderFor60Days")
	private String doNotConsiderFor60Days;
	
	public String getUpdateIntvwDtls() {
		return updateIntvwDtls;
	}

	public void setUpdateIntvwDtls(String updateIntvwDtls) {
		this.updateIntvwDtls = updateIntvwDtls;
	}

	public String getUpdateOfferDtls() {
		return updateOfferDtls;
	}

	public void setUpdateOfferDtls(String updateOfferDtls) {
		this.updateOfferDtls = updateOfferDtls;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getWasInterviewCompleted() {
		return wasInterviewCompleted;
	}

	public void setWasInterviewCompleted(String wasInterviewCompleted) {
		this.wasInterviewCompleted = wasInterviewCompleted;
	}

	public String getNoInterviewReason() {
		return noInterviewReason;
	}

	public void setNoInterviewReason(String noInterviewReason) {
		this.noInterviewReason = noInterviewReason;
	}

	public String getInterviewerName() {
		return interviewerName;
	}

	public void setInterviewerName(String interviewerName) {
		this.interviewerName = interviewerName;
	}

	public String getSelectedSig() {
		return selectedSig;
	}

	public void setSelectedSig(String selectedSig) {
		this.selectedSig = selectedSig;
	}

	public String getDateOfIntvw() {
		return dateOfIntvw;
	}

	public void setDateOfIntvw(String dateOfIntvw) {
		this.dateOfIntvw = dateOfIntvw;
	}

	public String getIntvwQuestion1() {
		return intvwQuestion1;
	}

	public void setIntvwQuestion1(String intvwQuestion1) {
		this.intvwQuestion1 = intvwQuestion1;
	}

	public String getIntvwQuestion2() {
		return intvwQuestion2;
	}

	public void setIntvwQuestion2(String intvwQuestion2) {
		this.intvwQuestion2 = intvwQuestion2;
	}

	public String getIntvwQuestion3() {
		return intvwQuestion3;
	}

	public void setIntvwQuestion3(String intvwQuestion3) {
		this.intvwQuestion3 = intvwQuestion3;
	}

	public String getIntvwQuestion4() {
		return intvwQuestion4;
	}

	public void setIntvwQuestion4(String intvwQuestion4) {
		this.intvwQuestion4 = intvwQuestion4;
	}

	public String getIntvwQuestion5() {
		return intvwQuestion5;
	}

	public void setIntvwQuestion5(String intvwQuestion5) {
		this.intvwQuestion5 = intvwQuestion5;
	}

	public String getIntvwQuestion6() {
		return intvwQuestion6;
	}

	public void setIntvwQuestion6(String intvwQuestion6) {
		this.intvwQuestion6 = intvwQuestion6;
	}

	public String getIntvwQuestion7() {
		return intvwQuestion7;
	}

	public void setIntvwQuestion7(String intvwQuestion7) {
		this.intvwQuestion7 = intvwQuestion7;
	}

	public String getIntvwQuestion8() {
		return intvwQuestion8;
	}

	public void setIntvwQuestion8(String intvwQuestion8) {
		this.intvwQuestion8 = intvwQuestion8;
	}

	public String getIntvwQuestion9() {
		return intvwQuestion9;
	}

	public void setIntvwQuestion9(String intvwQuestion9) {
		this.intvwQuestion9 = intvwQuestion9;
	}

	public String getIntvwQuestion10() {
		return intvwQuestion10;
	}

	public void setIntvwQuestion10(String intvwQuestion10) {
		this.intvwQuestion10 = intvwQuestion10;
	}

	public String getLicNum() {
		return licNum;
	}

	public void setLicNum(String licNum) {
		this.licNum = licNum;
	}

	public String getLicState() {
		return licState;
	}

	public void setLicState(String licState) {
		this.licState = licState;
	}

	public String getLicExpDate() {
		return licExpDate;
	}

	public void setLicExpDate(String licExpDate) {
		this.licExpDate = licExpDate;
	}

	public String getBackgroundConsent() {
		return backgroundConsent;
	}

	public void setBackgroundConsent(String backgroundConsent) {
		this.backgroundConsent = backgroundConsent;
	}

	public String getUpdateLicenseDtls() {
		return updateLicenseDtls;
	}

	public void setUpdateLicenseDtls(String updateLicenseDtls) {
		this.updateLicenseDtls = updateLicenseDtls;
	}

	/**
	 * @return the doNotConsiderFor60Days
	 */
	public String getDoNotConsiderFor60Days() {
		return doNotConsiderFor60Days;
	}

	/**
	 * @param doNotConsiderFor60Days the doNotConsiderFor60Days to set
	 */
	public void setDoNotConsiderFor60Days(String doNotConsiderFor60Days) {
		this.doNotConsiderFor60Days = doNotConsiderFor60Days;
	}

	public String getDoesAvailabilityMatch() {
		return doesAvailabilityMatch;
	}

	public void setDoesAvailabilityMatch(String doesAvailabilityMatch) {
		this.doesAvailabilityMatch = doesAvailabilityMatch;
	}

}

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
 * File Name: CandidateDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.homedepot.hr.hr.retailstaffing.util.CandRefNumTrimmer;
import com.homedepot.hr.hr.retailstaffing.util.DAODateConverterToDateTO;
import com.homedepot.hr.hr.retailstaffing.util.DB2Trimmer;
import com.homedepot.hr.hr.retailstaffing.util.DoNotConsiderFlagConverter;
import com.homedepot.hr.hr.retailstaffing.util.InterviewDeclineReason;
import com.homedepot.hr.hr.retailstaffing.util.RemoveTrailingComma;
import com.homedepot.ta.aa.dao.builder.BooleanStringFlagProcessor;
import com.homedepot.ta.aa.dao.builder.DAOConverter;
import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send candidate details in response in XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("CandidateDetails")
public class CandidateDetailsTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;
	
	@XStreamAlias("ssnNbr")
	@DAOElement("EMPLT_POSN_CAND_ID")
	@DAOConverter(DB2Trimmer.class)
	private String ssnNbr;
	
	@XStreamAlias("aid")
	@DAOElement("Z_EMPLID")
	@DAOConverter(DB2Trimmer.class)
	private String aid;
	
	@DAOElement("APPLICANT_FULL_NAME")
	@DAOConverter(RemoveTrailingComma.class)	
	@XStreamAlias("name")
	private String name;
	
	@DAOElement("ACTV_FLG")
	@DAOConverter(BooleanStringFlagProcessor.class)
	@XStreamAlias("canStatus")
	private String canStatus;
	
	@XStreamAlias("intStatus")
	@DAOElement("INTVW_STAT_IND")
	@DAOConverter(DB2Trimmer.class)
	private String intStatus;
	
	@XStreamAlias("offerMade")
	@DAOElement("CAND_OFFR_MADE_FLG")
	@DAOConverter(DB2Trimmer.class)
	private String offerMade;
	
	@XStreamAlias("offerDate")
	@DAOElement("CAND_OFFR_MADE_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	private DateTO offerDate;
	
	@XStreamAlias("offerStat")
	@DAOElement("CAND_OFFR_STAT_IND")
	@DAOConverter(DB2Trimmer.class)
	private String offerStat;
	
	@XStreamAlias("decCD")
	@DAOElement("OFFR_DCLN_RSN_CD")
	@DAOConverter(InterviewDeclineReason.class)
	private String decCD;
	
	@DAOElement("DRUG_TEST_RSLT_CD")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("drugTest")
	private String drugTest;
	
	@DAOElement("INTERNAL_EXTERNAL")
	@XStreamAlias("internalExternal")
	private String internalExternal;
	
	@XStreamAlias("canPhn")
	private String canPhn;
	
	@XStreamAlias("reqCalId")
	private String reqCalId;
	
	@DAOElement("ORGANIZATION_1")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("organization")
	private String organization;
	
	@DAOElement("APPL_TYP")
	@XStreamAlias("applicantType")
	private String applicantType;
	
	@DAOElement("HR_PHN_SCRN_ID")
	@XStreamAlias("phnScrnId")
	private String phnScrnId;
	
	@DAOElement("EMPLT_CAND_ID")
	@DAOConverter(CandRefNumTrimmer.class)
	@XStreamAlias("candRefNbr")
	private String candRefNbr;
	
	@XStreamAlias("address1")
	private String address1;
	
	@XStreamAlias("address2")
	private String address2;
	
	@XStreamAlias("city")
	private String city;
	
	@XStreamAlias("stateCode")
	private String stateCode;
	
	@XStreamAlias("zip")
	private String zip;
	
	//Added for FMS 7894 January 2016 CR's
	@DAOElement({"EFF_BGN_DT","doNotConsiderFor60Days"})
	@DAOConverter(DoNotConsiderFlagConverter.class)
	@XStreamAlias("doNotConsiderFor60Days")
	private String doNotConsiderFor60Days;
	
	@XStreamAlias("submitInterviewResultsDT")
	@DAOElement("INTERVIEW_SUBMITTED_DATE")
	@DAOConverter(DAODateConverterToDateTO.class)
	private DateTO submitInterviewResultsDT;
	
	@XStreamAlias("submitOfferResultsDT")
	@DAOElement("OFFER_SUBMITTED_DATE")
	@DAOConverter(DAODateConverterToDateTO.class)
	private DateTO submitOfferResultsDT;	
	
	/**
	 * @return the ssnNbr
	 */
	public String getSsnNbr() {
		return ssnNbr;
	}
	/**
	 * @param ssnNbr the ssnNbr to set
	 */
	public void setSsnNbr(String ssnNbr) {
		this.ssnNbr = ssnNbr;
	}
	/**
	 * @return the aid
	 */
	public String getAid() {
		return aid;
	}
	/**
	 * @param aid the aid to set
	 */
	public void setAid(String aid) {
		this.aid = aid;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the canStatus
	 */
	public String getCanStatus() {
		return canStatus;
	}
	/**
	 * @param canStatus the canStatus to set
	 */
	public void setCanStatus(String canStatus) {
		this.canStatus = canStatus;
	}
	/**
	 * @return the intStatus
	 */
	public String getIntStatus() {
		return intStatus;
	}
	/**
	 * @param intStatus the intStatus to set
	 */
	public void setIntStatus(String intStatus) {
		this.intStatus = intStatus;
	}
	/**
	 * @return the offerMade
	 */
	public String getOfferMade() {
		return offerMade;
	}
	/**
	 * @param offerMade the offerMade to set
	 */
	public void setOfferMade(String offerMade) {
		this.offerMade = offerMade;
	}
	/**
	 * @return the offerDate
	 */
	public DateTO getOfferDate() {
		return offerDate;
	}
	/**
	 * @param offerDate the offerDate to set
	 */
	public void setOfferDate(DateTO offerDate) {
		this.offerDate = offerDate;
	}
	/**
	 * @return the offerStat
	 */
	public String getOfferStat() {
		return offerStat;
	}
	/**
	 * @param offerStat the offerStat to set
	 */
	public void setOfferStat(String offerStat) {
		this.offerStat = offerStat;
	}
	/**
	 * @return the decCD
	 */
	public String getDecCD() {
		return decCD;
	}
	/**
	 * @param decCD the decCD to set
	 */
	public void setDecCD(String decCD) {
		this.decCD = decCD;
	}
	/**
	 * @return the drugTest
	 */
	public String getDrugTest() {
		return drugTest;
	}
	/**
	 * @param drugTest the drugTest to set
	 */
	public void setDrugTest(String drugTest) {
		this.drugTest = drugTest;
	}
	/**
	 * @return the internalExternal
	 */
	public String getInternalExternal() {
		return internalExternal;
	}
	/**
	 * @param internalExternal the internalExternal to set
	 */
	public void setInternalExternal(String internalExternal) {
		this.internalExternal = internalExternal;
	}
	/**
	 * @return canPhn the candidate phone number
	 */
	public String getCanPhn() {
		return canPhn;
	}
	/**
	 * @param canPhn the candidate phone number
	 */
	public void setCanPhn(String canPhn) {
		this.canPhn = canPhn;
	}
	public String getReqCalId() {
		return reqCalId;
	}
	public void setReqCalId(String reqCalId) {
		this.reqCalId = reqCalId;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public String getApplicantType() {
		return applicantType;
	}
	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}
	public String getPhnScrnId() {
		return phnScrnId;
	}
	public void setPhnScrnId(String phnScrnId) {
		this.phnScrnId = phnScrnId;
	}
	public String getCandRefNbr() {
		return candRefNbr;
	}
	public void setCandRefNbr(String candRefNbr) {
		this.candRefNbr = candRefNbr;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
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
	public DateTO getSubmitInterviewResultsDT() {
		return submitInterviewResultsDT;
	}
	public void setSubmitInterviewResultsDT(DateTO submitInterviewResultsDT) {
		this.submitInterviewResultsDT = submitInterviewResultsDT;
	}
	public DateTO getSubmitOfferResultsDT() {
		return submitOfferResultsDT;
	}
	public void setSubmitOfferResultsDT(DateTO submitOfferResultsDT) {
		this.submitOfferResultsDT = submitOfferResultsDT;
	}
	
}

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
 * File Name: RequisitionDetailTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Requisition details  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("RequisitionDetail")
public class RequisitionDetailTO implements Serializable
{
	

	private static final long serialVersionUID = 362498820763181265L;


	@XStreamAlias("reqNbr")
	private String reqNbr;
	@XStreamAlias("active")
	private String active;
	@XStreamAlias("store")
	private String store;
	@XStreamAlias("dept")
	private String dept;
	@XStreamAlias("job")
	private String job;
	@XStreamAlias("canStatus")
	private String canStatus;
	@XStreamAlias("intStatus")
	private String intStatus;
	@XStreamAlias("offerMade")
	private String offerMade;
	@XStreamAlias("offerDate")
	private DateTO offerDate;
	@XStreamAlias("offerStat")
	private String offerStat;
	@XStreamAlias("decCD")
	private String decCD;
	@XStreamAlias("drugTest")
	private String drugTest;
	@XStreamAlias("dateCreate")
	private String dateCreate;
	@XStreamAlias("creator")
	private String creator;
	@XStreamAlias("jobTtl")
	private String jobTtl;
	@XStreamAlias("scrTyp")
	private String scrTyp;
	@XStreamAlias("phnScrTyp")
	private String phnScrTyp;
	@XStreamAlias("fillDt")
	private String fillDt;
	@XStreamAlias("openings")
	private String openings;
	@XStreamAlias("ft")
	private String ft;
	@XStreamAlias("pt")
	private String pt;
	@XStreamAlias("insertUpdate")
	private String insertUpdate;
	@XStreamAlias("humanResourcesSystemRegionCode")
	private String humanResourcesSystemRegionCode;
	@XStreamAlias("humanResourcesSystemOperationsGroupCode")
	private String humanResourcesSystemOperationsGroupCode;
	@XStreamAlias("humanResourcesSystemDivisionCode")
	private String humanResourcesSystemDivisionCode;
	@XStreamAlias("requisitionStatusCode")
	private String  requisitionStatusCode;
	@XStreamAlias("referrals")
	private String  referrals;
	@XStreamAlias("quewebNbr")
	private String  quewebNbr;
	@XStreamAlias("PhnScrnCnt")
	private int  PhnScrnCnt;
	@XStreamAlias("reqCalId")
	private int reqCalId;
	@XStreamAlias("sealTempJob")
	private String sealTempJob;
	@XStreamAlias("rscSchdFlg")
	private String rscSchdFlg;
	@XStreamAlias("interviewDurtn")
	private String interviewDurtn;
	@XStreamAlias("reqNumInterviews")
	private String reqNumInterviews;	
	@XStreamAlias("authPosCount")
	private String authPosCount;
	
	@XStreamAlias("offerExtCandCnt")
	private Integer offerExtCandCnt;	
	
	@XStreamAlias("interviewRemainingCandCnt")
	private Integer total;
	
	@XStreamAlias("requisitionCalendarName")
	private String requisitionCalendarName;
	
	@XStreamAlias("rscToManageFlg")
	private String rscToManageFlg;
	
	@XStreamAlias("reqCandidateType")
	private String reqCandidateType;

	@XStreamAlias("requisitionSchedulePreference")
	private List<SchedulePreferenceTO> reqnSchdPref;
	
	@XStreamAlias("requisitionLanguagePreference")
	private List<LanguageSkillsTO> reqnLangPref;
	
	@XStreamAlias("isDrivingPosition")
	private boolean isDrivingPosition;
	
	//Added for FMS 7894 IVR Change	
	@XStreamAlias("hourlyRate")
	private String hourlyRate;
	
	/**
	 * @param authPosCount the authPosCount to set
	 */
	public void setAuthPosCount(String authPosCount)
	{
		this.authPosCount = authPosCount;
	}

	/**
	 * @return the authPosCount
	 */
	public String getAuthPosCount()
	{
		return authPosCount;
	}
	
	/**
	 * @param offerExtCandCnt the offerExtCandCnt to set
	 */
	public void setOfferExtCandCnt(Integer offerExtCandCnt)
	{
		this.offerExtCandCnt = offerExtCandCnt;
	}

	/**
	 * @return the offerExtCandCnt
	 */
	public Integer getOfferExtCandCnt()
	{
		return offerExtCandCnt;
	}
	
	
	/**
	 * @return the quewebNbr
	 */
	public String getQuewebNbr()
	{
		return quewebNbr;
	}
	
	/**
	 * @param quewebNbr the quewebNbr to set
	 */
	public void setQuewebNbr(String quewebNbr)
	{
		this.quewebNbr = quewebNbr;
	}

	/**
	 * @return the requisitionStatusCode
	 */
	public String getRequisitionStatusCode()
	{
		return requisitionStatusCode;
	}

	/**
	 * @return the referrals
	 */
	public String getReferrals()
	{
		return referrals;
	}

	/**
	 * @param referrals the referrals to set
	 */
	public void setReferrals(String referrals)
	{
		this.referrals = referrals;
	}

	/**
	 * @param requisitionStatusCode the requisitionStatusCode to set
	 */
	public void setRequisitionStatusCode(String requisitionStatusCode)
	{
		this.requisitionStatusCode = requisitionStatusCode;
	}

	/**
	 * @param insertUpdate
	 *            the insertUpdate to set
	 */
	public void setInsertUpdate(String insertUpdate)
	{
		this.insertUpdate = insertUpdate;
	}

	/**
	 * @return the insertUpdate
	 */
	public String getInsertUpdate()
	{
		return insertUpdate;
	}

	/**
	 * @return the reqNbr
	 */
	public String getReqNbr()
	{
		return reqNbr;
	}

	/**
	 * @param reqNbr
	 *            the reqNbr to set
	 */
	public void setReqNbr(String reqNbr)
	{
		this.reqNbr = reqNbr;
	}

	/**
	 * @return the active
	 */
	public String getActive()
	{
		return active;
	}

	/**
	 * @param active
	 *            the active to set
	 */
	public void setActive(String active)
	{
		this.active = active;
	}

	/**
	 * @return the store
	 */
	public String getStore()
	{
		return store;
	}

	/**
	 * @param store
	 *            the store to set
	 */
	public void setStore(String store)
	{
		this.store = store;
	}

	/**
	 * @return the dept
	 */
	public String getDept()
	{
		return dept;
	}

	/**
	 * @param dept
	 *            the dept to set
	 */
	public void setDept(String dept)
	{
		this.dept = dept;
	}

	/**
	 * @return the job
	 */
	public String getJob()
	{
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(String job)
	{
		this.job = job;
	}

	/**
	 * @return the canStatus
	 */
	public String getCanStatus()
	{
		return canStatus;
	}

	/**
	 * @param canStatus
	 *            the canStatus to set
	 */
	public void setCanStatus(String canStatus)
	{
		this.canStatus = canStatus;
	}

	/**
	 * @return the intStatus
	 */
	public String getIntStatus()
	{
		return intStatus;
	}

	/**
	 * @param intStatus
	 *            the intStatus to set
	 */
	public void setIntStatus(String intStatus)
	{
		this.intStatus = intStatus;
	}

	/**
	 * @return the offerMade
	 */
	public String getOfferMade()
	{
		return offerMade;
	}

	/**
	 * @param offerMade
	 *            the offerMade to set
	 */
	public void setOfferMade(String offerMade)
	{
		this.offerMade = offerMade;
	}

	public DateTO getOfferDate()
	{
		return offerDate;
	}

	public void setOfferDate(DateTO offerDate)
	{
		this.offerDate = offerDate;
	}

	/**
	 * @return the offerStat
	 */
	public String getOfferStat()
	{
		return offerStat;
	}

	/**
	 * @param offerStat
	 *            the offerStat to set
	 */
	public void setOfferStat(String offerStat)
	{
		this.offerStat = offerStat;
	}

	/**
	 * @return the decCD
	 */
	public String getDecCD()
	{
		return decCD;
	}

	/**
	 * @param decCD
	 *            the decCD to set
	 */
	public void setDecCD(String decCD)
	{
		this.decCD = decCD;
	}

	/**
	 * @return the drugTest
	 */
	public String getDrugTest()
	{
		return drugTest;
	}

	/**
	 * @param drugTest
	 *            the drugTest to set
	 */
	public void setDrugTest(String drugTest)
	{
		this.drugTest = drugTest;
	}

	/**
	 * @return the dateCreate
	 */
	public String getDateCreate()
	{
		return dateCreate;
	}

	/**
	 * @param dateCreate
	 *            the dateCreate to set
	 */
	public void setDateCreate(String dateCreate)
	{
		this.dateCreate = dateCreate;
	}

	/**
	 * @return the creator
	 */
	public String getCreator()
	{
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	/**
	 * @return the jobTtl
	 */
	public String getJobTtl()
	{
		return jobTtl;
	}

	/**
	 * @param jobTtl
	 *            the jobTtl to set
	 */
	public void setJobTtl(String jobTtl)
	{
		this.jobTtl = jobTtl;
	}

	/**
	 * @return the scrTyp
	 */
	public String getScrTyp()
	{
		return scrTyp;
	}

	/**
	 * @param scrTyp
	 *            the scrTyp to set
	 */
	public void setScrTyp(String scrTyp)
	{
		this.scrTyp = scrTyp;
	}

	/**
	 * @param phnScrTyp
	 *            the phnScrTyp to set
	 */
	public void setPhnScrTyp(String phnScrTyp)
	{
		this.phnScrTyp = phnScrTyp;
	}

	/**
	 * @return the phnScrTyp
	 */
	public String getPhnScrTyp()
	{
		return phnScrTyp;
	}

	/**
	 * @return the fillDt
	 */
	public String getFillDt()
	{
		return fillDt;
	}

	/**
	 * @param fillDt
	 *            the fillDt to set
	 */
	public void setFillDt(String fillDt)
	{
		this.fillDt = fillDt;
	}

	/**
	 * @return the openings
	 */
	public String getOpenings()
	{
		return openings;
	}

	/**
	 * @param openings
	 *            the openings to set
	 */
	public void setOpenings(String openings)
	{
		this.openings = openings;
	}

	/**
	 * @return the ft
	 */
	public String getFt()
	{
		return ft;
	}

	/**
	 * @param ft
	 *            the ft to set
	 */
	public void setFt(String ft)
	{
		this.ft = ft;
	}

	/**
	 * @return the pt
	 */
	public String getPt()
	{
		return pt;
	}

	/**
	 * @param pt
	 *            the pt to set
	 */
	public void setPt(String pt)
	{
		this.pt = pt;
	}

	public String getHumanResourcesSystemRegionCode()
	{
		return humanResourcesSystemRegionCode;
	}

	public void setHumanResourcesSystemRegionCode(
			String humanResourcesSystemRegionCode)
	{
		this.humanResourcesSystemRegionCode = humanResourcesSystemRegionCode;
	}

	public String getHumanResourcesSystemOperationsGroupCode()
	{
		return humanResourcesSystemOperationsGroupCode;
	}

	public void setHumanResourcesSystemOperationsGroupCode(
			String humanResourcesSystemOperationsGroupCode)
	{
		this.humanResourcesSystemOperationsGroupCode = humanResourcesSystemOperationsGroupCode;
	}

	public String getHumanResourcesSystemDivisionCode()
	{
		return humanResourcesSystemDivisionCode;
	}

	public void setHumanResourcesSystemDivisionCode(
			String humanResourcesSystemDivisionCode)
	{
		this.humanResourcesSystemDivisionCode = humanResourcesSystemDivisionCode;
	}
	
	/**
	 * @return the phnScrnCnt
	 */
	public int getPhnScrnCnt()
	{
		return PhnScrnCnt;
	}

	/**
	 * @param phnScrnCnt the phnScrnCnt to set
	 */
	public void setPhnScrnCnt(int phnScrnCnt)
	{
		PhnScrnCnt = phnScrnCnt;
	}

	public int getReqCalId() {
		return reqCalId;
	}

	public void setReqCalId(int reqCalId) {
		this.reqCalId = reqCalId;
	}

	public String getSealTempJob() {
		return sealTempJob;
	}

	public void setSealTempJob(String sealTempJob) {
		this.sealTempJob = sealTempJob;
	}

	public String getRscSchdFlg() {
		return rscSchdFlg;
	}

	public void setRscSchdFlg(String rscSchdFlg) {
		this.rscSchdFlg = rscSchdFlg;
	}

	public String getInterviewDurtn() {
		return interviewDurtn;
	}

	public void setInterviewDurtn(String interviewDurtn) {
		this.interviewDurtn = interviewDurtn;
	}

	public String getReqNumInterviews() {
		return reqNumInterviews;
	}

	public void setReqNumInterviews(String reqNumInterviews) {
		this.reqNumInterviews = reqNumInterviews;
	}

	//getter method for total
	public Integer getTotal() {
		return total;
	}

	//setter method for total
	public void setTotal(Integer aValue) {
		this.total = aValue;
	}

	public String getRequisitionCalendarName() {
		return requisitionCalendarName;
	}

	public void setRequisitionCalendarName(String requisitionCalendarName) {
		this.requisitionCalendarName = requisitionCalendarName;
	}

	public String getRscToManageFlg() {
		return rscToManageFlg;
	}

	public void setRscToManageFlg(String rscToManageFlg) {
		this.rscToManageFlg = rscToManageFlg;
	}

	public String getReqCandidateType() {
		return reqCandidateType;
	}

	public void setReqCandidateType(String reqCandidateType) {
		this.reqCandidateType = reqCandidateType;
	}

	public List<SchedulePreferenceTO> getReqnSchdPref() {
		return reqnSchdPref;
	}

	public void setReqnSchdPref(List<SchedulePreferenceTO> reqnSchdPref) {
		this.reqnSchdPref = reqnSchdPref;
	}

	public List<LanguageSkillsTO> getReqnLangPref() {
		return reqnLangPref;
	}

	public void setReqnLangPref(List<LanguageSkillsTO> reqnLangPref) {
		this.reqnLangPref = reqnLangPref;
	}

	public boolean isDrivingPosition() {
		return isDrivingPosition;
	}

	public void setDrivingPosition(boolean isDrivingPosition) {
		this.isDrivingPosition = isDrivingPosition;
	}

	/**
	 * @return the hourlyRate
	 */
	public String getHourlyRate() {
		return hourlyRate;
	}

	/**
	 * @param hourlyRate the hourlyRate to set
	 */
	public void setHourlyRate(String hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

}

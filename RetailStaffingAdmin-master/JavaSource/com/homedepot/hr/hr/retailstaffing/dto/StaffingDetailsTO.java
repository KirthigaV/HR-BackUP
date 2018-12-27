/*
 * Created on Feb 12, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 *Application:RetailStaffing
 *
 * File Name: StaffingDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send  Staffing details  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("StaffingDetails")
public class StaffingDetailsTO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3334665243490199629L;
	@XStreamAlias("hrgMgrName")
	private String hrgMgrName;
	@XStreamAlias("hrgMgrTtl")
	private String hrgMgrTtl;
	@XStreamAlias("hrgMgrPhn")
	private String hrgMgrPhn;
	@XStreamAlias("hrgMgrId")
	private String hrgMgrId;	
	@XStreamAlias("requestorName")
	private String requestorName;	
	@XStreamAlias("requestorTitle")
	private String requestorTitle;
	@XStreamAlias("requestorPhnNbr")
	private String requestorPhnNbr;	
	@XStreamAlias("requestorId")
	private String requestorId;	
	@XStreamAlias("hireByFlg")
	private String hireByFlg;
	@XStreamAlias("targetPay")
	private String targetPay;
	@XStreamAlias("desiredExp")
	private String desiredExp;
	@XStreamAlias("desiredExpText")
	private String desiredExpText;
	@XStreamAlias("stfReqNbr")
	private String stfReqNbr;
	@XStreamAlias("requestNbr")
	private String requestNbr;
	@XStreamAlias("requisitionStatus")
	private String requisitionStatus;
	@XStreamAlias("weekMgrAvble")
	private String weekMgrAvble;
	@XStreamAlias("daysTmMgrAvble")
	private String daysTmMgrAvble;
	@XStreamAlias("hrgEvntFlg")
	private String hrgEvntFlg;
	@XStreamAlias("reqTxtNt")
	private String reqTxtNt;
	@XStreamAlias("weekBeginDt")
	private DateTO weekBeginDt;
	@XStreamAlias("stfHrgEvntStartDt")
	private DateTO stfHrgEvntStartDt;
	@XStreamAlias("stfHrgEvntEndDt")
	private DateTO stfHrgEvntEndDt;
	@XStreamAlias("stfhrgEvntLoc")
	private String stfhrgEvntLoc;
	@XStreamAlias("stfhrgEvntLocPhn")
	private String stfhrgEvntLocPhn;
	@XStreamAlias("add")
	private String add;
	@XStreamAlias("city")
	private String city;
	@XStreamAlias("state")
	private String state;
	@XStreamAlias("zip")
	private String zip;
	@XStreamAlias("start")
	private DateTO start;
	@XStreamAlias("end")
	private DateTO end;
	@XStreamAlias("startTime")
	private TimeStampTO startTime;
	@XStreamAlias("endTime")
	private TimeStampTO endTime;
	@XStreamAlias("breaks")
	private String breaks;
	@XStreamAlias("languageSkls")
	private String languageSkls;
	@XStreamAlias("lunch")
	private TimeStampTO lunch;
	@XStreamAlias("interviewTmSlt")
	private String interviewTmSlt;
	@XStreamAlias("lastIntrTm")
	private TimeStampTO lastIntrTm;
	@XStreamAlias("interviewDurtn")
	private String interviewDurtn;
	@XStreamAlias("jobTitle")
	private String jobTitle;
	@XStreamAlias("qualPoolNts")
	private String qualPoolNts;
	@XStreamAlias("Referals")
	private String Referals;
	@XStreamAlias("hiringEventID")
	private String hiringEventID;
	@XStreamAlias("reqStatus")
	private String reqStatus;
	@XStreamAlias("strNo")
	private String strNo;
	@XStreamAlias("deptNo")
	private String deptNo;	
	@XStreamAlias("jobTtlCd")
	private String jobTtlCd;
	@XStreamAlias("fillDt")
	private DateTO fillDt;	
	@XStreamAlias("numPositions")
	private String numPositions;
	@XStreamAlias("requestedNbrIntvws")
	private String requestedNbrIntvws;
	@XStreamAlias("jobStatusPt")
	private String jobStatusPt;
	@XStreamAlias("jobStatusFt")
	private String jobStatusFt;
	@XStreamAlias("sealTempJob")
	private String sealTempJob;
	@XStreamAlias("rscSchdFlg")
	private String rscSchdFlg;
	@XStreamAlias("schdPrefDetailsList")
	private List<SchedulePreferenceTO> schdPrefDetailsList;
	@XStreamAlias("candidateType")
	private String candidateType;
	@XStreamAlias("namesOfInterviewers")
	private String namesOfInterviewers;
	@XStreamAlias("interviewCandidateCount")
	private String interviewCandidateCount;
	@XStreamAlias("userLdapEmailAddress")
	private String userLdapEmailAddress;
	@XStreamAlias("hireEvntType")
	private String hireEvntType;
	@XStreamAlias("hireEvntMgrAssociateId")
	private String hireEvntMgrAssociateId;
	@XStreamAlias("insertDeleteUpdateHrHireEvntReqnTable")
	private String insertDeleteUpdateHrHireEvntReqnTable;	
	@XStreamAlias("rscManageFlg")
	private String rscManageFlg;	
	@XStreamAlias("isAutoAttachReqn")
	private boolean isAutoAttachReqn;

	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	@XStreamAlias("targetPayRounded")
	private String targetPayRounded;
	
	@XStreamAlias("formattedstfhrgEvntLocPhn")
	private String formattedstfhrgEvntLocPhn;
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
	
	public String getHrgMgrName() {
		return hrgMgrName;
	}
	public void setHrgMgrName(String hrgMgrName) {
		this.hrgMgrName = hrgMgrName;
	}
	public String getHrgMgrTtl() {
		return hrgMgrTtl;
	}
	public void setHrgMgrTtl(String hrgMgrTtl) {
		this.hrgMgrTtl = hrgMgrTtl;
	}
	public String getHrgMgrPhn() {
		return hrgMgrPhn;
	}
	public void setHrgMgrPhn(String hrgMgrPhn) {
		this.hrgMgrPhn = hrgMgrPhn;
	}
	public String getHrgMgrId() {
		return hrgMgrId;
	}
	public void setHrgMgrId(String hrgMgrId) {
		this.hrgMgrId = hrgMgrId;
	}
	public String getRequestorId() {
		return requestorId;
	}
	public void setRequestorId(String requestorId) {
		this.requestorId = requestorId;
	}
	public String getTargetPay() {
		return targetPay;
	}
	public void setTargetPay(String targetPay) {
		this.targetPay = targetPay;
	}
	public String getDesiredExp() {
		return desiredExp;
	}
	public void setDesiredExp(String desiredExp) {
		this.desiredExp = desiredExp;
	}
	public String getStfReqNbr() {
		return stfReqNbr;
	}
	public void setStfReqNbr(String stfReqNbr) {
		this.stfReqNbr = stfReqNbr;
	}
	public String getRequestNbr() {
		return requestNbr;
	}
	public void setRequestNbr(String requestNbr) {
		this.requestNbr = requestNbr;
	}
	public String getRequisitionStatus() {
		return requisitionStatus;
	}
	public void setRequisitionStatus(String requisitionStatus) {
		this.requisitionStatus = requisitionStatus;
	}
	public String getWeekMgrAvble() {
		return weekMgrAvble;
	}
	public void setWeekMgrAvble(String weekMgrAvble) {
		this.weekMgrAvble = weekMgrAvble;
	}
	public String getDaysTmMgrAvble() {
		return daysTmMgrAvble;
	}
	public void setDaysTmMgrAvble(String daysTmMgrAvble) {
		this.daysTmMgrAvble = daysTmMgrAvble;
	}
	public String getHrgEvntFlg() {
		return hrgEvntFlg;
	}
	public void setHrgEvntFlg(String hrgEvntFlg) {
		this.hrgEvntFlg = hrgEvntFlg;
	}
	public String getReqTxtNt() {
		return reqTxtNt;
	}
	public void setReqTxtNt(String reqTxtNt) {
		this.reqTxtNt = reqTxtNt;
	}
	public DateTO getWeekBeginDt() {
		return weekBeginDt;
	}
	public void setWeekBeginDt(DateTO weekBeginDt) {
		this.weekBeginDt = weekBeginDt;
	}
	public DateTO getStfHrgEvntStartDt() {
		return stfHrgEvntStartDt;
	}
	public void setStfHrgEvntStartDt(DateTO stfHrgEvntStartDt) {
		this.stfHrgEvntStartDt = stfHrgEvntStartDt;
	}
	public DateTO getStfHrgEvntEndDt() {
		return stfHrgEvntEndDt;
	}
	public void setStfHrgEvntEndDt(DateTO stfHrgEvntEndDt) {
		this.stfHrgEvntEndDt = stfHrgEvntEndDt;
	}
	public String getStfhrgEvntLoc() {
		return stfhrgEvntLoc;
	}
	public void setStfhrgEvntLoc(String stfhrgEvntLoc) {
		this.stfhrgEvntLoc = stfhrgEvntLoc;
	}
	public String getStfhrgEvntLocPhn() {
		return stfhrgEvntLocPhn;
	}
	public void setStfhrgEvntLocPhn(String stfhrgEvntLocPhn) {
		this.stfhrgEvntLocPhn = stfhrgEvntLocPhn;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public DateTO getStart() {
		return start;
	}
	public void setStart(DateTO start) {
		this.start = start;
	}
	public DateTO getEnd() {
		return end;
	}
	public void setEnd(DateTO end) {
		this.end = end;
	}
	public TimeStampTO getStartTime() {
		return startTime;
	}
	public void setStartTime(TimeStampTO startTime) {
		this.startTime = startTime;
	}
	public TimeStampTO getEndTime() {
		return endTime;
	}
	public void setEndTime(TimeStampTO endTime) {
		this.endTime = endTime;
	}
	public String getBreaks() {
		return breaks;
	}
	public void setBreaks(String breaks) {
		this.breaks = breaks;
	}
	public TimeStampTO getLunch() {
		return lunch;
	}
	public void setLunch(TimeStampTO lunch) {
		this.lunch = lunch;
	}
	public String getInterviewTmSlt() {
		return interviewTmSlt;
	}
	public void setInterviewTmSlt(String interviewTmSlt) {
		this.interviewTmSlt = interviewTmSlt;
	}
	public TimeStampTO getLastIntrTm() {
		return lastIntrTm;
	}
	public void setLastIntrTm(TimeStampTO lastIntrTm) {
		this.lastIntrTm = lastIntrTm;
	}
	public String getInterviewDurtn() {
		return interviewDurtn;
	}
	public void setInterviewDurtn(String interviewDurtn) {
		this.interviewDurtn = interviewDurtn;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getQualPoolNts() {
		return qualPoolNts;
	}
	public void setQualPoolNts(String qualPoolNts) {
		this.qualPoolNts = qualPoolNts;
	}
	public String getReferals() {
		return Referals;
	}
	public void setReferals(String referals) {
		Referals = referals;
	}
	public String getHiringEventID() {
		return hiringEventID;
	}
	public void setHiringEventID(String hiringEventID) {
		this.hiringEventID = hiringEventID;
	}
	public String getReqStatus() {
		return reqStatus;
	}
	public void setReqStatus(String reqStatus) {
		this.reqStatus = reqStatus;
	}
	public String getStrNo() {
		return strNo;
	}
	public void setStrNo(String strNo) {
		this.strNo = strNo;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getJobTtlCd() {
		return jobTtlCd;
	}
	public void setJobTtlCd(String jobTtlCd) {
		this.jobTtlCd = jobTtlCd;
	}
	public DateTO getFillDt() {
		return fillDt;
	}
	public void setFillDt(DateTO fillDt) {
		this.fillDt = fillDt;
	}
	public String getNumPositions() {
		return numPositions;
	}
	public void setNumPositions(String numPositions) {
		this.numPositions = numPositions;
	}
	public String getRequestedNbrIntvws() {
		return requestedNbrIntvws;
	}
	public void setRequestedNbrIntvws(String requestedNbrIntvws) {
		this.requestedNbrIntvws = requestedNbrIntvws;
	}
	public String getJobStatusPt() {
		return jobStatusPt;
	}
	public void setJobStatusPt(String jobStatusPt) {
		this.jobStatusPt = jobStatusPt;
	}
	public String getJobStatusFt() {
		return jobStatusFt;
	}
	public void setJobStatusFt(String jobStatusFt) {
		this.jobStatusFt = jobStatusFt;
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
	public List<SchedulePreferenceTO> getSchdPrefDetailsList() {
		return schdPrefDetailsList;
	}
	public void setSchdPrefDetailsList(
			List<SchedulePreferenceTO> schdPrefDetailsList) {
		this.schdPrefDetailsList = schdPrefDetailsList;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getRequestorName() {
		return requestorName;
	}
	public void setRequestorName(String requestorName) {
		this.requestorName = requestorName;
	}
	public String getRequestorTitle() {
		return requestorTitle;
	}
	public void setRequestorTitle(String requestorTitle) {
		this.requestorTitle = requestorTitle;
	}
	public String getRequestorPhnNbr() {
		return requestorPhnNbr;
	}
	public void setRequestorPhnNbr(String requestorPhnNbr) {
		this.requestorPhnNbr = requestorPhnNbr;
	}
	public String getHireByFlg() {
		return hireByFlg;
	}
	public void setHireByFlg(String hireByFlg) {
		this.hireByFlg = hireByFlg;
	}
	public String getDesiredExpText() {
		return desiredExpText;
	}
	public void setDesiredExpText(String desiredExpText) {
		this.desiredExpText = desiredExpText;
	}
	public String getLanguageSkls() {
		return languageSkls;
	}
	public void setLanguageSkls(String languageSkls) {
		this.languageSkls = languageSkls;
	}
	public String getCandidateType() {
		return candidateType;
	}
	public void setCandidateType(String candidateType) {
		this.candidateType = candidateType;
	}
	public String getNamesOfInterviewers() {
		return namesOfInterviewers;
	}
	public void setNamesOfInterviewers(String namesOfInterviewers) {
		this.namesOfInterviewers = namesOfInterviewers;
	}
	public String getInterviewCandidateCount() {
		return interviewCandidateCount;
	}
	public void setInterviewCandidateCount(String interviewCandidateCount) {
		this.interviewCandidateCount = interviewCandidateCount;
	}
	public String getUserLdapEmailAddress() {
		return userLdapEmailAddress;
	}
	public void setUserLdapEmailAddress(String userLdapEmailAddress) {
		this.userLdapEmailAddress = userLdapEmailAddress;
	}
	public String getHireEvntType() {
		return hireEvntType;
	}
	public void setHireEvntType(String hireEvntType) {
		this.hireEvntType = hireEvntType;
	}
	public String getHireEvntMgrAssociateId() {
		return hireEvntMgrAssociateId;
	}
	public void setHireEvntMgrAssociateId(String hireEvntMgrAssociateId) {
		this.hireEvntMgrAssociateId = hireEvntMgrAssociateId;
	}
	public String getInsertDeleteUpdateHrHireEvntReqnTable() {
		return insertDeleteUpdateHrHireEvntReqnTable;
	}
	public void setInsertDeleteUpdateHrHireEvntReqnTable(String insertDeleteUpdateHrHireEvntReqnTable) {
		this.insertDeleteUpdateHrHireEvntReqnTable = insertDeleteUpdateHrHireEvntReqnTable;
	}
	public String getRscManageFlg() {
		return rscManageFlg;
	}
	public void setRscManageFlg(String rscManageFlg) {
		this.rscManageFlg = rscManageFlg;
	}
	public boolean isAutoAttachReqn() {
		return isAutoAttachReqn;
	}
	public void setAutoAttachReqn(boolean isAutoAttachReqn) {
		this.isAutoAttachReqn = isAutoAttachReqn;
	}
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * @return target pay rounded to 2 decimal places
	 */
	public String getTargetPayRounded() {
		return targetPayRounded;
	}
	
	/**
	 * @param targetPayRounded
	 */
	public void setTargetPayRounded(String targetPayRounded) {
		this.targetPayRounded = targetPayRounded;
	}
	
	/**
	 * @return the formattedstfhrgEvntLocPhn
	 */
	public String getFormattedstfhrgEvntLocPhn() {
		return formattedstfhrgEvntLocPhn;
	}
	/**
	 * @param formattedstfhrgEvntLocPhn the formattedstfhrgEvntLocPhn to set
	 */
	public void setFormattedstfhrgEvntLocPhn(String formattedstfhrgEvntLocPhn) {
		this.formattedstfhrgEvntLocPhn = formattedstfhrgEvntLocPhn;
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015

}

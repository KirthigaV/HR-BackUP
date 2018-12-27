package com.homedepot.hr.hr.staffingforms.dto;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplicationData.java
 * Application: StaffingForms
 * Used to transfer XML to XSL Application PDF Creation
 */
import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("app")
public class ApplicationData implements Serializable {
	private static final long serialVersionUID = 1283675876160425277L;

	@XStreamAlias("applicantId")
	private String applicantId;

	@XStreamAlias("candRefNbr")
	private String candRefNbr;
	
	@XStreamAlias("currentTimeStamp")
	private String currentTimeStamp;

	@XStreamAlias("name")
	private String name;

	@XStreamAlias("areaCode")
	private String areaCode;

	@XStreamAlias("phnNumber")
	private String phnNumber;

	@XStreamAlias("address1")
	private String address1;

	@XStreamAlias("address2")
	private String address2;

	@XStreamAlias("city")
	private String city;

	@XStreamAlias("stateCd")
	private String stateCd;

	@XStreamAlias("zipCd")
	private String zipCd;

	@XStreamAlias("minEmpltAgeFlg")
	private String minEmpltAgeFlg;

	@XStreamAlias("empltEligUsFlg")
	private String empltEligUsFlg;

	@XStreamAlias("priorConviction")
	private String priorConviction;

	@XStreamAlias("currentLeaveOfAbsenceLayoffFlag")
	private String currentLeaveOfAbsenceLayoffFlag;

	@XStreamAlias("prevEmpltFlg")
	private String prevEmpltFlg;

	@XStreamAlias("prevEmpltLocation")
	private String prevEmpltLocation;

	@XStreamAlias("prevEmpltBgnDt")
	private String prevEmpltBgnDt;

	@XStreamAlias("prevEmpltEndDt")
	private String prevEmpltEndDt;

	@XStreamAlias("relativesAtHd")
	private String relativesAtHd;

	@XStreamAlias("relativesAtHdName")
	private String relativesAtHdName;

	@XStreamAlias("militaryFlg")
	private String militaryFlg;

	@XStreamAlias("militaryBranch")
	private String militaryBranch;

	@XStreamAlias("militaryBgnDt")
	private String militaryBgnDt;

	@XStreamAlias("militaryEndDt")
	private String militaryEndDt;

	@XStreamAlias("ftFlg")
	private String ftFlg;

	@XStreamAlias("ptFlg")
	private String ptFlg;

	@XStreamAlias("wrkDurationInd")
	private String wrkDurationInd;

	@XStreamAlias("wageDesired")
	private String wageDesired;

	@XStreamAlias("availableDt")
	private String availableDt;

	@XStreamAlias("applicationDate")
	private String applicationDate;

	@XStreamAlias("reloFlg")
	private String reloFlg;

	@XStreamAlias("dismissFlg")
	private String dismissFlg;

	@XStreamAlias("currentlyEmployedFlg")
	private String currentlyEmployedFlg;

	@XStreamAlias("wrkDaysMissed")
	private String wrkDaysMissed;

	@XStreamAlias("jskilFlg21")
	private String jskilFlg21;

	@XStreamAlias("jskilFlg22")
	private String jskilFlg22;

	@XStreamAlias("jskilFlg23")
	private String jskilFlg23;

	@XStreamAlias("jskilFlg24")
	private String jskilFlg24;

	@XStreamAlias("jskilFlg25")
	private String jskilFlg25;

	@XStreamAlias("jskilFlg26")
	private String jskilFlg26;

	@XStreamAlias("jskilFlg27")
	private String jskilFlg27;

	@XStreamAlias("jskilFlg28")
	private String jskilFlg28;

	@XStreamAlias("jskilFlg29")
	private String jskilFlg29;

	@XStreamAlias("jskilFlg30")
	private String jskilFlg30;

	@XStreamAlias("jskilFlg31")
	private String jskilFlg31;

	@XStreamAlias("jskilFlg32")
	private String jskilFlg32;

	@XStreamAlias("jskilFlg33")
	private String jskilFlg33;

	@XStreamAlias("jskilFlg34")
	private String jskilFlg34;

	@XStreamAlias("jskilFlg35")
	private String jskilFlg35;

	@XStreamAlias("jskilFlg36")
	private String jskilFlg36;

	@XStreamAlias("jskilFlg37")
	private String jskilFlg37;

	@XStreamAlias("jskilFlg38")
	private String jskilFlg38;

	@XStreamAlias("jskilFlg39")
	private String jskilFlg39;

	@XStreamAlias("jskilFlg40")
	private String jskilFlg40;

	@XStreamAlias("jskilFlg41")
	private String jskilFlg41;

	@XStreamAlias("jskilFlg42")
	private String jskilFlg42;

	@XStreamAlias("jskilFlg43")
	private String jskilFlg43;

	@XStreamAlias("jskilFlg44")
	private String jskilFlg44;

	@XStreamAlias("jskilFlg45")
	private String jskilFlg45;

	@XStreamAlias("jskilFlg46")
	private String jskilFlg46;

	@XStreamAlias("jskilFlg47")
	private String jskilFlg47;

	@XStreamAlias("jskilFlg48")
	private String jskilFlg48;

	@XStreamAlias("jskilFlg49")
	private String jskilFlg49;

	@XStreamAlias("jskilFlg50")
	private String jskilFlg50;

	@XStreamAlias("jskilFlg51")
	private String jskilFlg51;

	@XStreamAlias("jskilFlg52")
	private String jskilFlg52;

	@XStreamAlias("jskilFlg53")
	private String jskilFlg53;

	@XStreamAlias("jskilFlg54")
	private String jskilFlg54;

	@XStreamAlias("jskilFlg55")
	private String jskilFlg55;

	@XStreamAlias("jskilFlg56")
	private String jskilFlg56;

	@XStreamAlias("jskilFlg57")
	private String jskilFlg57;

	@XStreamAlias("jskilFlg58")
	private String jskilFlg58;

	@XStreamAlias("jskilFlg59")
	private String jskilFlg59;

	@XStreamAlias("jskilFlg60")
	private String jskilFlg60;

	@XStreamAlias("jskilFlg61")
	private String jskilFlg61;

	@XStreamAlias("jskilFlg62")
	private String jskilFlg62;

	@XStreamAlias("jskilFlg63")
	private String jskilFlg63;

	@XStreamAlias("jskilFlg64")
	private String jskilFlg64;

	@XStreamAlias("jskilFlg65")
	private String jskilFlg65;

	@XStreamAlias("jskilFlg66")
	private String jskilFlg66;

	@XStreamAlias("jskilFlg67")
	private String jskilFlg67;

	@XStreamAlias("jskilFlg68")
	private String jskilFlg68;

	@XStreamAlias("langFlg1")
	private String langFlg1;

	@XStreamAlias("langFlg2")
	private String langFlg2;

	@XStreamAlias("langFlg3")
	private String langFlg3;

	@XStreamAlias("langFlg4")
	private String langFlg4;

	@XStreamAlias("langFlg5")
	private String langFlg5;

	@XStreamAlias("langFlg6")
	private String langFlg6;

	@XStreamAlias("langFlg7")
	private String langFlg7;

	@XStreamAlias("langFlg8")
	private String langFlg8;

	@XStreamAlias("langFlg9")
	private String langFlg9;

	@XStreamAlias("langFlg10")
	private String langFlg10;

	@XStreamAlias("langFlg11")
	private String langFlg11;

	@XStreamAlias("langFlg12")
	private String langFlg12;

	@XStreamAlias("langFlg13")
	private String langFlg13;

	@XStreamAlias("langFlg14")
	private String langFlg14;

	@XStreamAlias("langFlg15")
	private String langFlg15;

	@XStreamAlias("langFlg16")
	private String langFlg16;

	@XStreamAlias("schoolName1")
	private String schoolName1;

	@XStreamAlias("yearsComplete1")
	private String yearsComplete1;

	@XStreamAlias("lastYrAttended1")
	private String lastYrAttended1;

	@XStreamAlias("degreeCert1")
	private String degreeCert1;

	@XStreamAlias("schoolName2")
	private String schoolName2;

	@XStreamAlias("yearsComplete2")
	private String yearsComplete2;

	@XStreamAlias("lastYrAttended2")
	private String lastYrAttended2;

	@XStreamAlias("degreeCert2")
	private String degreeCert2;

	@XStreamAlias("schoolName3")
	private String schoolName3;

	@XStreamAlias("yearsComplete3")
	private String yearsComplete3;

	@XStreamAlias("lastYrAttended3")
	private String lastYrAttended3;

	@XStreamAlias("degreeCert3")
	private String degreeCert3;

	@XStreamAlias("emplrName1")
	private String emplrName1;

	@XStreamAlias("emplrAddr1")
	private String emplrAddr1;

	@XStreamAlias("emplrCity1")
	private String emplrCity1;

	@XStreamAlias("emplrStCd1")
	private String emplrStCd1;

	@XStreamAlias("emplrZipCd1")
	private String emplrZipCd1;

	@XStreamAlias("emplrPhn1")
	private String emplrPhn1;

	@XStreamAlias("emplrWrkType1")
	private String emplrWrkType1;

	@XStreamAlias("emplrSupv1")
	private String emplrSupv1;

	@XStreamAlias("emplrSupvTtl1")
	private String emplrSupvTtl1;

	@XStreamAlias("emplrReasonLeaving1")
	private String emplrReasonLeaving1;

	@XStreamAlias("emplrBgnDt1")
	private String emplrBgnDt1;

	@XStreamAlias("emplrEndDt1")
	private String emplrEndDt1;

	@XStreamAlias("emplrPayRate1")
	private String emplrPayRate1;

	@XStreamAlias("emplrName2")
	private String emplrName2;

	@XStreamAlias("emplrAddr2")
	private String emplrAddr2;

	@XStreamAlias("emplrCity2")
	private String emplrCity2;

	@XStreamAlias("emplrStCd2")
	private String emplrStCd2;

	@XStreamAlias("emplrZipCd2")
	private String emplrZipCd2;

	@XStreamAlias("emplrPhn2")
	private String emplrPhn2;

	@XStreamAlias("emplrWrkType2")
	private String emplrWrkType2;

	@XStreamAlias("emplrSupv2")
	private String emplrSupv2;

	@XStreamAlias("emplrSupvTtl2")
	private String emplrSupvTtl2;

	@XStreamAlias("emplrReasonLeaving2")
	private String emplrReasonLeaving2;

	@XStreamAlias("emplrBgnDt2")
	private String emplrBgnDt2;

	@XStreamAlias("emplrEndDt2")
	private String emplrEndDt2;

	@XStreamAlias("emplrPayRate2")
	private String emplrPayRate2;

	@XStreamAlias("emplrName3")
	private String emplrName3;

	@XStreamAlias("emplrAddr3")
	private String emplrAddr3;

	@XStreamAlias("emplrCity3")
	private String emplrCity3;

	@XStreamAlias("emplrStCd3")
	private String emplrStCd3;

	@XStreamAlias("emplrZipCd3")
	private String emplrZipCd3;

	@XStreamAlias("emplrPhn3")
	private String emplrPhn3;

	@XStreamAlias("emplrWrkType3")
	private String emplrWrkType3;

	@XStreamAlias("emplrSupv3")
	private String emplrSupv3;

	@XStreamAlias("emplrSupvTtl3")
	private String emplrSupvTtl3;

	@XStreamAlias("emplrReasonLeaving3")
	private String emplrReasonLeaving3;

	@XStreamAlias("emplrBgnDt3")
	private String emplrBgnDt3;

	@XStreamAlias("emplrEndDt3")
	private String emplrEndDt3;

	@XStreamAlias("emplrPayRate3")
	private String emplrPayRate3;

	@XStreamAlias("currentStore")
	private String currentStore;

	@XStreamAlias("currentDept")
	private String currentDept;

	@XStreamAlias("currentTitle")
	private String currentTitle;

	@XStreamAlias("currentStatus")
	private String currentStatus;

	@XStreamAlias("hireDt")
	private String hireDt;

	@XStreamAlias("AssoJobPref")
	private String AssoJobPref;

	@XStreamAlias("positionsAppliedFor")
	private String positionsAppliedFor;

	@XStreamAlias("storesAppliedFor")
	private String storesAppliedFor;

	@XStreamAlias("reviewDt1")
	private String reviewDt1;
	
	@XStreamAlias("reviewScore1")
	private String reviewScore1;
	
	@XStreamAlias("reviewDt2")
	private String reviewDt2;
	
	@XStreamAlias("reviewScore2")
	private String reviewScore2;
	
	@XStreamAlias("frmrDept1")
	private String frmrDept1;
	
	@XStreamAlias("frmrJobCd1")
	private String frmrJobCd1;
	
	@XStreamAlias("frmrFromDt1")
	private String frmrFromDt1;
	
	@XStreamAlias("frmrToDt1")
	private String frmrToDt1;
	
	@XStreamAlias("frmrDept2")
	private String frmrDept2;
	
	@XStreamAlias("frmrJobCd2")
	private String frmrJobCd2;
	
	@XStreamAlias("frmrFromDt2")
	private String frmrFromDt2;
	
	@XStreamAlias("frmrToDt2")
	private String frmrToDt2;
	
	@XStreamAlias("frmrDept3")
	private String frmrDept3;
	
	@XStreamAlias("frmrJobCd3")
	private String frmrJobCd3;
	
	@XStreamAlias("frmrFromDt3")
	private String frmrFromDt3;
	
	@XStreamAlias("frmrToDt3")
	private String frmrToDt3;
	
	@XStreamAlias("frmrDept4")
	private String frmrDept4;
	
	@XStreamAlias("frmrJobCd4")
	private String frmrJobCd4;
	
	@XStreamAlias("frmrFromDt4")
	private String frmrFromDt4;
	
	@XStreamAlias("frmrToDt4")
	private String frmrToDt4;
	
	@XStreamAlias("weekdays")
	private String weekdays;
	
	@XStreamAlias("saturday")
	private String saturday;
	
	@XStreamAlias("sunday")
	private String sunday;
	
	@XStreamAlias("earlyAm")
	private String earlyAm;
	
	@XStreamAlias("mornings")
	private String mornings;
	
	@XStreamAlias("afternoons")
	private String afternoons;
	
	@XStreamAlias("nights")
	private String nights;
	
	@XStreamAlias("lateNight")
	private String lateNight;
	
	@XStreamAlias("overnight")
	private String overnight;
	
	@XStreamAlias("reasonableAccommodationRequested")
	private String reasonableAccommodationRequested;	
	
	@XStreamAlias("requisitionAttachedTo")
	private String requisitionAttachedTo;
	
	@XStreamAlias("phoneScreenId")
	private long phoneScreenId;
	
	@XStreamAlias("phoneScreenStatusCode")
	private short phoneScreenStatusCode;
	
	public String getCurrentTimeStamp() {
		return currentTimeStamp;
	}

	public void setCurrentTimeStamp(String currentTimeStamp) {
		this.currentTimeStamp = currentTimeStamp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(String applicantId) {
		this.applicantId = applicantId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getPhnNumber() {
		return phnNumber;
	}

	public void setPhnNumber(String phnNumber) {
		this.phnNumber = phnNumber;
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

	public String getStateCd() {
		return stateCd;
	}

	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}

	public String getZipCd() {
		return zipCd;
	}

	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}

	public String getMinEmpltAgeFlg() {
		return minEmpltAgeFlg;
	}

	public void setMinEmpltAgeFlg(String minEmpltAgeFlg) {
		this.minEmpltAgeFlg = minEmpltAgeFlg;
	}

	public String getEmpltEligUsFlg() {
		return empltEligUsFlg;
	}

	public void setEmpltEligUsFlg(String empltEligUsFlg) {
		this.empltEligUsFlg = empltEligUsFlg;
	}

	public String getPriorConviction() {
		return priorConviction;
	}

	public void setPriorConviction(String priorConviction) {
		this.priorConviction = priorConviction;
	}

	public String getCurrentLeaveOfAbsenceLayoffFlag() {
		return currentLeaveOfAbsenceLayoffFlag;
	}

	public void setCurrentLeaveOfAbsenceLayoffFlag(String currentLeaveOfAbsenceLayoffFlag) {
		this.currentLeaveOfAbsenceLayoffFlag = currentLeaveOfAbsenceLayoffFlag;
	}

	public String getPrevEmpltFlg() {
		return prevEmpltFlg;
	}

	public void setPrevEmpltFlg(String prevEmpltFlg) {
		this.prevEmpltFlg = prevEmpltFlg;
	}

	public String getPrevEmpltLocation() {
		return prevEmpltLocation;
	}

	public void setPrevEmpltLocation(String prevEmpltLocation) {
		this.prevEmpltLocation = prevEmpltLocation;
	}

	public String getPrevEmpltBgnDt() {
		return prevEmpltBgnDt;
	}

	public void setPrevEmpltBgnDt(String prevEmpltBgnDt) {
		this.prevEmpltBgnDt = prevEmpltBgnDt;
	}

	public String getPrevEmpltEndDt() {
		return prevEmpltEndDt;
	}

	public void setPrevEmpltEndDt(String prevEmpltEndDt) {
		this.prevEmpltEndDt = prevEmpltEndDt;
	}

	public String getRelativesAtHd() {
		return relativesAtHd;
	}

	public void setRelativesAtHd(String relativesAtHd) {
		this.relativesAtHd = relativesAtHd;
	}

	public String getRelativesAtHdName() {
		return relativesAtHdName;
	}

	public void setRelativesAtHdName(String relativesAtHdName) {
		this.relativesAtHdName = relativesAtHdName;
	}

	public String getMilitaryFlg() {
		return militaryFlg;
	}

	public void setMilitaryFlg(String militaryFlg) {
		this.militaryFlg = militaryFlg;
	}

	public String getMilitaryBranch() {
		return militaryBranch;
	}

	public void setMilitaryBranch(String militaryBranch) {
		this.militaryBranch = militaryBranch;
	}

	public String getMilitaryBgnDt() {
		return militaryBgnDt;
	}

	public void setMilitaryBgnDt(String militaryBgnDt) {
		this.militaryBgnDt = militaryBgnDt;
	}

	public String getMilitaryEndDt() {
		return militaryEndDt;
	}

	public void setMilitaryEndDt(String militaryEndDt) {
		this.militaryEndDt = militaryEndDt;
	}

	public String getFtFlg() {
		return ftFlg;
	}

	public void setFtFlg(String ftFlg) {
		this.ftFlg = ftFlg;
	}

	public String getPtFlg() {
		return ptFlg;
	}

	public void setPtFlg(String ptFlg) {
		this.ptFlg = ptFlg;
	}

	public String getWrkDurationInd() {
		return wrkDurationInd;
	}

	public void setWrkDurationInd(String wrkDurationInd) {
		this.wrkDurationInd = wrkDurationInd;
	}

	public String getWageDesired() {
		return wageDesired;
	}

	public void setWageDesired(String wageDesired) {
		this.wageDesired = wageDesired;
	}

	public String getAvailableDt() {
		return availableDt;
	}

	public void setAvailableDt(String availableDt) {
		this.availableDt = availableDt;
	}

	public String getApplicationDate() {
		return applicationDate;
	}

	public void setApplicationDate(String applicationDate) {
		this.applicationDate = applicationDate;
	}

	public String getReloFlg() {
		return reloFlg;
	}

	public void setReloFlg(String reloFlg) {
		this.reloFlg = reloFlg;
	}

	public String getDismissFlg() {
		return dismissFlg;
	}

	public void setDismissFlg(String dismissFlg) {
		this.dismissFlg = dismissFlg;
	}

	public String getCurrentlyEmployedFlg() {
		return currentlyEmployedFlg;
	}

	public void setCurrentlyEmployedFlg(String currentlyEmployedFlg) {
		this.currentlyEmployedFlg = currentlyEmployedFlg;
	}

	public String getWrkDaysMissed() {
		return wrkDaysMissed;
	}

	public void setWrkDaysMissed(String wrkDaysMissed) {
		this.wrkDaysMissed = wrkDaysMissed;
	}

	public String getJskilFlg21() {
		return jskilFlg21;
	}

	public void setJskilFlg21(String jskilFlg21) {
		this.jskilFlg21 = jskilFlg21;
	}

	public String getJskilFlg22() {
		return jskilFlg22;
	}

	public void setJskilFlg22(String jskilFlg22) {
		this.jskilFlg22 = jskilFlg22;
	}

	public String getJskilFlg23() {
		return jskilFlg23;
	}

	public void setJskilFlg23(String jskilFlg23) {
		this.jskilFlg23 = jskilFlg23;
	}

	public String getJskilFlg24() {
		return jskilFlg24;
	}

	public void setJskilFlg24(String jskilFlg24) {
		this.jskilFlg24 = jskilFlg24;
	}

	public String getJskilFlg25() {
		return jskilFlg25;
	}

	public void setJskilFlg25(String jskilFlg25) {
		this.jskilFlg25 = jskilFlg25;
	}

	public String getJskilFlg26() {
		return jskilFlg26;
	}

	public void setJskilFlg26(String jskilFlg26) {
		this.jskilFlg26 = jskilFlg26;
	}

	public String getJskilFlg27() {
		return jskilFlg27;
	}

	public void setJskilFlg27(String jskilFlg27) {
		this.jskilFlg27 = jskilFlg27;
	}

	public String getJskilFlg28() {
		return jskilFlg28;
	}

	public void setJskilFlg28(String jskilFlg28) {
		this.jskilFlg28 = jskilFlg28;
	}

	public String getJskilFlg29() {
		return jskilFlg29;
	}

	public void setJskilFlg29(String jskilFlg29) {
		this.jskilFlg29 = jskilFlg29;
	}

	public String getJskilFlg30() {
		return jskilFlg30;
	}

	public void setJskilFlg30(String jskilFlg30) {
		this.jskilFlg30 = jskilFlg30;
	}

	public String getJskilFlg31() {
		return jskilFlg31;
	}

	public void setJskilFlg31(String jskilFlg31) {
		this.jskilFlg31 = jskilFlg31;
	}

	public String getJskilFlg32() {
		return jskilFlg32;
	}

	public void setJskilFlg32(String jskilFlg32) {
		this.jskilFlg32 = jskilFlg32;
	}

	public String getJskilFlg33() {
		return jskilFlg33;
	}

	public void setJskilFlg33(String jskilFlg33) {
		this.jskilFlg33 = jskilFlg33;
	}

	public String getJskilFlg34() {
		return jskilFlg34;
	}

	public void setJskilFlg34(String jskilFlg34) {
		this.jskilFlg34 = jskilFlg34;
	}

	public String getJskilFlg35() {
		return jskilFlg35;
	}

	public void setJskilFlg35(String jskilFlg35) {
		this.jskilFlg35 = jskilFlg35;
	}

	public String getJskilFlg36() {
		return jskilFlg36;
	}

	public void setJskilFlg36(String jskilFlg36) {
		this.jskilFlg36 = jskilFlg36;
	}

	public String getJskilFlg37() {
		return jskilFlg37;
	}

	public void setJskilFlg37(String jskilFlg37) {
		this.jskilFlg37 = jskilFlg37;
	}

	public String getJskilFlg38() {
		return jskilFlg38;
	}

	public void setJskilFlg38(String jskilFlg38) {
		this.jskilFlg38 = jskilFlg38;
	}

	public String getJskilFlg39() {
		return jskilFlg39;
	}

	public void setJskilFlg39(String jskilFlg39) {
		this.jskilFlg39 = jskilFlg39;
	}

	public String getJskilFlg40() {
		return jskilFlg40;
	}

	public void setJskilFlg40(String jskilFlg40) {
		this.jskilFlg40 = jskilFlg40;
	}

	public String getJskilFlg41() {
		return jskilFlg41;
	}

	public void setJskilFlg41(String jskilFlg41) {
		this.jskilFlg41 = jskilFlg41;
	}

	public String getJskilFlg42() {
		return jskilFlg42;
	}

	public void setJskilFlg42(String jskilFlg42) {
		this.jskilFlg42 = jskilFlg42;
	}

	public String getJskilFlg43() {
		return jskilFlg43;
	}

	public void setJskilFlg43(String jskilFlg43) {
		this.jskilFlg43 = jskilFlg43;
	}

	public String getJskilFlg44() {
		return jskilFlg44;
	}

	public void setJskilFlg44(String jskilFlg44) {
		this.jskilFlg44 = jskilFlg44;
	}

	public String getJskilFlg45() {
		return jskilFlg45;
	}

	public void setJskilFlg45(String jskilFlg45) {
		this.jskilFlg45 = jskilFlg45;
	}

	public String getJskilFlg46() {
		return jskilFlg46;
	}

	public void setJskilFlg46(String jskilFlg46) {
		this.jskilFlg46 = jskilFlg46;
	}

	public String getJskilFlg47() {
		return jskilFlg47;
	}

	public void setJskilFlg47(String jskilFlg47) {
		this.jskilFlg47 = jskilFlg47;
	}

	public String getJskilFlg48() {
		return jskilFlg48;
	}

	public void setJskilFlg48(String jskilFlg48) {
		this.jskilFlg48 = jskilFlg48;
	}

	public String getJskilFlg49() {
		return jskilFlg49;
	}

	public void setJskilFlg49(String jskilFlg49) {
		this.jskilFlg49 = jskilFlg49;
	}

	public String getJskilFlg50() {
		return jskilFlg50;
	}

	public void setJskilFlg50(String jskilFlg50) {
		this.jskilFlg50 = jskilFlg50;
	}

	public String getJskilFlg51() {
		return jskilFlg51;
	}

	public void setJskilFlg51(String jskilFlg51) {
		this.jskilFlg51 = jskilFlg51;
	}

	public String getJskilFlg52() {
		return jskilFlg52;
	}

	public void setJskilFlg52(String jskilFlg52) {
		this.jskilFlg52 = jskilFlg52;
	}

	public String getJskilFlg53() {
		return jskilFlg53;
	}

	public void setJskilFlg53(String jskilFlg53) {
		this.jskilFlg53 = jskilFlg53;
	}

	public String getJskilFlg54() {
		return jskilFlg54;
	}

	public void setJskilFlg54(String jskilFlg54) {
		this.jskilFlg54 = jskilFlg54;
	}

	public String getJskilFlg55() {
		return jskilFlg55;
	}

	public void setJskilFlg55(String jskilFlg55) {
		this.jskilFlg55 = jskilFlg55;
	}

	public String getJskilFlg56() {
		return jskilFlg56;
	}

	public void setJskilFlg56(String jskilFlg56) {
		this.jskilFlg56 = jskilFlg56;
	}

	public String getJskilFlg57() {
		return jskilFlg57;
	}

	public void setJskilFlg57(String jskilFlg57) {
		this.jskilFlg57 = jskilFlg57;
	}

	public String getJskilFlg58() {
		return jskilFlg58;
	}

	public void setJskilFlg58(String jskilFlg58) {
		this.jskilFlg58 = jskilFlg58;
	}

	public String getJskilFlg59() {
		return jskilFlg59;
	}

	public void setJskilFlg59(String jskilFlg59) {
		this.jskilFlg59 = jskilFlg59;
	}

	public String getJskilFlg60() {
		return jskilFlg60;
	}

	public void setJskilFlg60(String jskilFlg60) {
		this.jskilFlg60 = jskilFlg60;
	}

	public String getJskilFlg61() {
		return jskilFlg61;
	}

	public void setJskilFlg61(String jskilFlg61) {
		this.jskilFlg61 = jskilFlg61;
	}

	public String getJskilFlg62() {
		return jskilFlg62;
	}

	public void setJskilFlg62(String jskilFlg62) {
		this.jskilFlg62 = jskilFlg62;
	}

	public String getJskilFlg63() {
		return jskilFlg63;
	}

	public void setJskilFlg63(String jskilFlg63) {
		this.jskilFlg63 = jskilFlg63;
	}

	public String getJskilFlg64() {
		return jskilFlg64;
	}

	public void setJskilFlg64(String jskilFlg64) {
		this.jskilFlg64 = jskilFlg64;
	}

	public String getJskilFlg65() {
		return jskilFlg65;
	}

	public void setJskilFlg65(String jskilFlg65) {
		this.jskilFlg65 = jskilFlg65;
	}

	public String getJskilFlg66() {
		return jskilFlg66;
	}

	public void setJskilFlg66(String jskilFlg66) {
		this.jskilFlg66 = jskilFlg66;
	}

	public String getJskilFlg67() {
		return jskilFlg67;
	}

	public void setJskilFlg67(String jskilFlg67) {
		this.jskilFlg67 = jskilFlg67;
	}

	public String getJskilFlg68() {
		return jskilFlg68;
	}

	public void setJskilFlg68(String jskilFlg68) {
		this.jskilFlg68 = jskilFlg68;
	}

	public String getLangFlg1() {
		return langFlg1;
	}

	public void setLangFlg1(String langFlg1) {
		this.langFlg1 = langFlg1;
	}

	public String getLangFlg2() {
		return langFlg2;
	}

	public void setLangFlg2(String langFlg2) {
		this.langFlg2 = langFlg2;
	}

	public String getLangFlg3() {
		return langFlg3;
	}

	public void setLangFlg3(String langFlg3) {
		this.langFlg3 = langFlg3;
	}

	public String getLangFlg4() {
		return langFlg4;
	}

	public void setLangFlg4(String langFlg4) {
		this.langFlg4 = langFlg4;
	}

	public String getLangFlg5() {
		return langFlg5;
	}

	public void setLangFlg5(String langFlg5) {
		this.langFlg5 = langFlg5;
	}

	public String getLangFlg6() {
		return langFlg6;
	}

	public void setLangFlg6(String langFlg6) {
		this.langFlg6 = langFlg6;
	}

	public String getLangFlg7() {
		return langFlg7;
	}

	public void setLangFlg7(String langFlg7) {
		this.langFlg7 = langFlg7;
	}

	public String getLangFlg8() {
		return langFlg8;
	}

	public void setLangFlg8(String langFlg8) {
		this.langFlg8 = langFlg8;
	}

	public String getLangFlg9() {
		return langFlg9;
	}

	public void setLangFlg9(String langFlg9) {
		this.langFlg9 = langFlg9;
	}

	public String getLangFlg10() {
		return langFlg10;
	}

	public void setLangFlg10(String langFlg10) {
		this.langFlg10 = langFlg10;
	}

	public String getLangFlg11() {
		return langFlg11;
	}

	public void setLangFlg11(String langFlg11) {
		this.langFlg11 = langFlg11;
	}

	public String getLangFlg12() {
		return langFlg12;
	}

	public void setLangFlg12(String langFlg12) {
		this.langFlg12 = langFlg12;
	}

	public String getLangFlg13() {
		return langFlg13;
	}

	public void setLangFlg13(String langFlg13) {
		this.langFlg13 = langFlg13;
	}

	public String getLangFlg14() {
		return langFlg14;
	}

	public void setLangFlg14(String langFlg14) {
		this.langFlg14 = langFlg14;
	}

	public String getLangFlg15() {
		return langFlg15;
	}

	public void setLangFlg15(String langFlg15) {
		this.langFlg15 = langFlg15;
	}

	public String getLangFlg16() {
		return langFlg16;
	}

	public void setLangFlg16(String langFlg16) {
		this.langFlg16 = langFlg16;
	}

	public String getSchoolName1() {
		return schoolName1;
	}

	public void setSchoolName1(String schoolName1) {
		this.schoolName1 = schoolName1;
	}

	public String getYearsComplete1() {
		return yearsComplete1;
	}

	public void setYearsComplete1(String yearsComplete1) {
		this.yearsComplete1 = yearsComplete1;
	}

	public String getLastYrAttended1() {
		return lastYrAttended1;
	}

	public void setLastYrAttended1(String lastYrAttended1) {
		this.lastYrAttended1 = lastYrAttended1;
	}

	public String getDegreeCert1() {
		return degreeCert1;
	}

	public void setDegreeCert1(String degreeCert1) {
		this.degreeCert1 = degreeCert1;
	}

	public String getSchoolName2() {
		return schoolName2;
	}

	public void setSchoolName2(String schoolName2) {
		this.schoolName2 = schoolName2;
	}

	public String getYearsComplete2() {
		return yearsComplete2;
	}

	public void setYearsComplete2(String yearsComplete2) {
		this.yearsComplete2 = yearsComplete2;
	}

	public String getLastYrAttended2() {
		return lastYrAttended2;
	}

	public void setLastYrAttended2(String lastYrAttended2) {
		this.lastYrAttended2 = lastYrAttended2;
	}

	public String getDegreeCert2() {
		return degreeCert2;
	}

	public void setDegreeCert2(String degreeCert2) {
		this.degreeCert2 = degreeCert2;
	}

	public String getSchoolName3() {
		return schoolName3;
	}

	public void setSchoolName3(String schoolName3) {
		this.schoolName3 = schoolName3;
	}

	public String getYearsComplete3() {
		return yearsComplete3;
	}

	public void setYearsComplete3(String yearsComplete3) {
		this.yearsComplete3 = yearsComplete3;
	}

	public String getLastYrAttended3() {
		return lastYrAttended3;
	}

	public void setLastYrAttended3(String lastYrAttended3) {
		this.lastYrAttended3 = lastYrAttended3;
	}

	public String getDegreeCert3() {
		return degreeCert3;
	}

	public void setDegreeCert3(String degreeCert3) {
		this.degreeCert3 = degreeCert3;
	}

	public String getEmplrName1() {
		return emplrName1;
	}

	public void setEmplrName1(String emplrName1) {
		this.emplrName1 = emplrName1;
	}

	public String getEmplrAddr1() {
		return emplrAddr1;
	}

	public void setEmplrAddr1(String emplrAddr1) {
		this.emplrAddr1 = emplrAddr1;
	}

	public String getEmplrCity1() {
		return emplrCity1;
	}

	public void setEmplrCity1(String emplrCity1) {
		this.emplrCity1 = emplrCity1;
	}

	public String getEmplrStCd1() {
		return emplrStCd1;
	}

	public void setEmplrStCd1(String emplrStCd1) {
		this.emplrStCd1 = emplrStCd1;
	}

	public String getEmplrZipCd1() {
		return emplrZipCd1;
	}

	public void setEmplrZipCd1(String emplrZipCd1) {
		this.emplrZipCd1 = emplrZipCd1;
	}

	public String getEmplrPhn1() {
		return emplrPhn1;
	}

	public void setEmplrPhn1(String emplrPhn1) {
		this.emplrPhn1 = emplrPhn1;
	}

	public String getEmplrWrkType1() {
		return emplrWrkType1;
	}

	public void setEmplrWrkType1(String emplrWrkType1) {
		this.emplrWrkType1 = emplrWrkType1;
	}

	public String getEmplrSupv1() {
		return emplrSupv1;
	}

	public void setEmplrSupv1(String emplrSupv1) {
		this.emplrSupv1 = emplrSupv1;
	}

	public String getEmplrSupvTtl1() {
		return emplrSupvTtl1;
	}

	public void setEmplrSupvTtl1(String emplrSupvTtl1) {
		this.emplrSupvTtl1 = emplrSupvTtl1;
	}

	public String getEmplrReasonLeaving1() {
		return emplrReasonLeaving1;
	}

	public void setEmplrReasonLeaving1(String emplrReasonLeaving1) {
		this.emplrReasonLeaving1 = emplrReasonLeaving1;
	}

	public String getEmplrBgnDt1() {
		return emplrBgnDt1;
	}

	public void setEmplrBgnDt1(String emplrBgnDt1) {
		this.emplrBgnDt1 = emplrBgnDt1;
	}

	public String getEmplrEndDt1() {
		return emplrEndDt1;
	}

	public void setEmplrEndDt1(String emplrEndDt1) {
		this.emplrEndDt1 = emplrEndDt1;
	}

	public String getEmplrPayRate1() {
		return emplrPayRate1;
	}

	public void setEmplrPayRate1(String emplrPayRate1) {
		this.emplrPayRate1 = emplrPayRate1;
	}

	public String getEmplrName2() {
		return emplrName2;
	}

	public void setEmplrName2(String emplrName2) {
		this.emplrName2 = emplrName2;
	}

	public String getEmplrAddr2() {
		return emplrAddr2;
	}

	public void setEmplrAddr2(String emplrAddr2) {
		this.emplrAddr2 = emplrAddr2;
	}

	public String getEmplrCity2() {
		return emplrCity2;
	}

	public void setEmplrCity2(String emplrCity2) {
		this.emplrCity2 = emplrCity2;
	}

	public String getEmplrStCd2() {
		return emplrStCd2;
	}

	public void setEmplrStCd2(String emplrStCd2) {
		this.emplrStCd2 = emplrStCd2;
	}

	public String getEmplrZipCd2() {
		return emplrZipCd2;
	}

	public void setEmplrZipCd2(String emplrZipCd2) {
		this.emplrZipCd2 = emplrZipCd2;
	}

	public String getEmplrPhn2() {
		return emplrPhn2;
	}

	public void setEmplrPhn2(String emplrPhn2) {
		this.emplrPhn2 = emplrPhn2;
	}

	public String getEmplrWrkType2() {
		return emplrWrkType2;
	}

	public void setEmplrWrkType2(String emplrWrkType2) {
		this.emplrWrkType2 = emplrWrkType2;
	}

	public String getEmplrSupv2() {
		return emplrSupv2;
	}

	public void setEmplrSupv2(String emplrSupv2) {
		this.emplrSupv2 = emplrSupv2;
	}

	public String getEmplrSupvTtl2() {
		return emplrSupvTtl2;
	}

	public void setEmplrSupvTtl2(String emplrSupvTtl2) {
		this.emplrSupvTtl2 = emplrSupvTtl2;
	}

	public String getEmplrReasonLeaving2() {
		return emplrReasonLeaving2;
	}

	public void setEmplrReasonLeaving2(String emplrReasonLeaving2) {
		this.emplrReasonLeaving2 = emplrReasonLeaving2;
	}

	public String getEmplrBgnDt2() {
		return emplrBgnDt2;
	}

	public void setEmplrBgnDt2(String emplrBgnDt2) {
		this.emplrBgnDt2 = emplrBgnDt2;
	}

	public String getEmplrEndDt2() {
		return emplrEndDt2;
	}

	public void setEmplrEndDt2(String emplrEndDt2) {
		this.emplrEndDt2 = emplrEndDt2;
	}

	public String getEmplrPayRate2() {
		return emplrPayRate2;
	}

	public void setEmplrPayRate2(String emplrPayRate2) {
		this.emplrPayRate2 = emplrPayRate2;
	}

	public String getEmplrName3() {
		return emplrName3;
	}

	public void setEmplrName3(String emplrName3) {
		this.emplrName3 = emplrName3;
	}

	public String getEmplrAddr3() {
		return emplrAddr3;
	}

	public void setEmplrAddr3(String emplrAddr3) {
		this.emplrAddr3 = emplrAddr3;
	}

	public String getEmplrCity3() {
		return emplrCity3;
	}

	public void setEmplrCity3(String emplrCity3) {
		this.emplrCity3 = emplrCity3;
	}

	public String getEmplrStCd3() {
		return emplrStCd3;
	}

	public void setEmplrStCd3(String emplrStCd3) {
		this.emplrStCd3 = emplrStCd3;
	}

	public String getEmplrZipCd3() {
		return emplrZipCd3;
	}

	public void setEmplrZipCd3(String emplrZipCd3) {
		this.emplrZipCd3 = emplrZipCd3;
	}

	public String getEmplrPhn3() {
		return emplrPhn3;
	}

	public void setEmplrPhn3(String emplrPhn3) {
		this.emplrPhn3 = emplrPhn3;
	}

	public String getEmplrWrkType3() {
		return emplrWrkType3;
	}

	public void setEmplrWrkType3(String emplrWrkType3) {
		this.emplrWrkType3 = emplrWrkType3;
	}

	public String getEmplrSupv3() {
		return emplrSupv3;
	}

	public void setEmplrSupv3(String emplrSupv3) {
		this.emplrSupv3 = emplrSupv3;
	}

	public String getEmplrSupvTtl3() {
		return emplrSupvTtl3;
	}

	public void setEmplrSupvTtl3(String emplrSupvTtl3) {
		this.emplrSupvTtl3 = emplrSupvTtl3;
	}

	public String getEmplrReasonLeaving3() {
		return emplrReasonLeaving3;
	}

	public void setEmplrReasonLeaving3(String emplrReasonLeaving3) {
		this.emplrReasonLeaving3 = emplrReasonLeaving3;
	}

	public String getEmplrBgnDt3() {
		return emplrBgnDt3;
	}

	public void setEmplrBgnDt3(String emplrBgnDt3) {
		this.emplrBgnDt3 = emplrBgnDt3;
	}

	public String getEmplrEndDt3() {
		return emplrEndDt3;
	}

	public void setEmplrEndDt3(String emplrEndDt3) {
		this.emplrEndDt3 = emplrEndDt3;
	}

	public String getEmplrPayRate3() {
		return emplrPayRate3;
	}

	public void setEmplrPayRate3(String emplrPayRate3) {
		this.emplrPayRate3 = emplrPayRate3;
	}

	public String getCurrentStore() {
		return currentStore;
	}

	public void setCurrentStore(String currentStore) {
		this.currentStore = currentStore;
	}

	public String getCurrentDept() {
		return currentDept;
	}

	public void setCurrentDept(String currentDept) {
		this.currentDept = currentDept;
	}

	public String getCurrentTitle() {
		return currentTitle;
	}

	public void setCurrentTitle(String currentTitle) {
		this.currentTitle = currentTitle;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getHireDt() {
		return hireDt;
	}

	public void setHireDt(String hireDt) {
		this.hireDt = hireDt;
	}

	public String getAssoJobPref() {
		return AssoJobPref;
	}

	public void setAssoJobPref(String assoJobPref) {
		AssoJobPref = assoJobPref;
	}

	public String getPositionsAppliedFor() {
		return positionsAppliedFor;
	}

	public void setPositionsAppliedFor(String positionsAppliedFor) {
		this.positionsAppliedFor = positionsAppliedFor;
	}

	public String getStoresAppliedFor() {
		return storesAppliedFor;
	}

	public void setStoresAppliedFor(String storesAppliedFor) {
		this.storesAppliedFor = storesAppliedFor;
	}

	public String getReviewDt1() {
		return reviewDt1;
	}

	public void setReviewDt1(String reviewDt1) {
		this.reviewDt1 = reviewDt1;
	}

	public String getReviewScore1() {
		return reviewScore1;
	}

	public void setReviewScore1(String reviewScore1) {
		this.reviewScore1 = reviewScore1;
	}

	public String getReviewDt2() {
		return reviewDt2;
	}

	public void setReviewDt2(String reviewDt2) {
		this.reviewDt2 = reviewDt2;
	}

	public String getReviewScore2() {
		return reviewScore2;
	}

	public void setReviewScore2(String reviewScore2) {
		this.reviewScore2 = reviewScore2;
	}

	public String getFrmrDept1() {
		return frmrDept1;
	}

	public void setFrmrDept1(String frmrDept1) {
		this.frmrDept1 = frmrDept1;
	}

	public String getFrmrJobCd1() {
		return frmrJobCd1;
	}

	public void setFrmrJobCd1(String frmrJobCd1) {
		this.frmrJobCd1 = frmrJobCd1;
	}

	public String getFrmrFromDt1() {
		return frmrFromDt1;
	}

	public void setFrmrFromDt1(String frmrFromDt1) {
		this.frmrFromDt1 = frmrFromDt1;
	}

	public String getFrmrToDt1() {
		return frmrToDt1;
	}

	public void setFrmrToDt1(String frmrToDt1) {
		this.frmrToDt1 = frmrToDt1;
	}

	public String getFrmrDept2() {
		return frmrDept2;
	}

	public void setFrmrDept2(String frmrDept2) {
		this.frmrDept2 = frmrDept2;
	}

	public String getFrmrJobCd2() {
		return frmrJobCd2;
	}

	public void setFrmrJobCd2(String frmrJobCd2) {
		this.frmrJobCd2 = frmrJobCd2;
	}

	public String getFrmrFromDt2() {
		return frmrFromDt2;
	}

	public void setFrmrFromDt2(String frmrFromDt2) {
		this.frmrFromDt2 = frmrFromDt2;
	}

	public String getFrmrToDt2() {
		return frmrToDt2;
	}

	public void setFrmrToDt2(String frmrToDt2) {
		this.frmrToDt2 = frmrToDt2;
	}

	public String getFrmrDept3() {
		return frmrDept3;
	}

	public void setFrmrDept3(String frmrDept3) {
		this.frmrDept3 = frmrDept3;
	}

	public String getFrmrJobCd3() {
		return frmrJobCd3;
	}

	public void setFrmrJobCd3(String frmrJobCd3) {
		this.frmrJobCd3 = frmrJobCd3;
	}

	public String getFrmrFromDt3() {
		return frmrFromDt3;
	}

	public void setFrmrFromDt3(String frmrFromDt3) {
		this.frmrFromDt3 = frmrFromDt3;
	}

	public String getFrmrToDt3() {
		return frmrToDt3;
	}

	public void setFrmrToDt3(String frmrToDt3) {
		this.frmrToDt3 = frmrToDt3;
	}

	public String getFrmrDept4() {
		return frmrDept4;
	}

	public void setFrmrDept4(String frmrDept4) {
		this.frmrDept4 = frmrDept4;
	}

	public String getFrmrJobCd4() {
		return frmrJobCd4;
	}

	public void setFrmrJobCd4(String frmrJobCd4) {
		this.frmrJobCd4 = frmrJobCd4;
	}

	public String getFrmrFromDt4() {
		return frmrFromDt4;
	}

	public void setFrmrFromDt4(String frmrFromDt4) {
		this.frmrFromDt4 = frmrFromDt4;
	}

	public String getFrmrToDt4() {
		return frmrToDt4;
	}

	public void setFrmrToDt4(String frmrToDt4) {
		this.frmrToDt4 = frmrToDt4;
	}

	public String getCandRefNbr() {
		return candRefNbr;
	}

	public void setCandRefNbr(String candRefNbr) {
		this.candRefNbr = candRefNbr;
	}

	public String getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}

	public String getSaturday() {
		return saturday;
	}

	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}

	public String getSunday() {
		return sunday;
	}

	public void setSunday(String sunday) {
		this.sunday = sunday;
	}

	public String getEarlyAm() {
		return earlyAm;
	}

	public void setEarlyAm(String earlyAm) {
		this.earlyAm = earlyAm;
	}

	public String getMornings() {
		return mornings;
	}

	public void setMornings(String mornings) {
		this.mornings = mornings;
	}

	public String getAfternoons() {
		return afternoons;
	}

	public void setAfternoons(String afternoons) {
		this.afternoons = afternoons;
	}

	public String getNights() {
		return nights;
	}

	public void setNights(String nights) {
		this.nights = nights;
	}

	public String getLateNight() {
		return lateNight;
	}

	public void setLateNight(String lateNight) {
		this.lateNight = lateNight;
	}

	public String getOvernight() {
		return overnight;
	}

	public void setOvernight(String overnight) {
		this.overnight = overnight;
	}

	public String getReasonableAccommodationRequested() {
		return reasonableAccommodationRequested;
	}

	public void setReasonableAccommodationRequested(String reasonableAccommodationRequested) {
		this.reasonableAccommodationRequested = reasonableAccommodationRequested;
	}

	public String getRequisitionAttachedTo() {
		return requisitionAttachedTo;
	}

	public void setRequisitionAttachedTo(String requisitionAttachedTo) {
		this.requisitionAttachedTo = requisitionAttachedTo;
	}

	public long getPhoneScreenId() {
		return phoneScreenId;
	}

	public void setPhoneScreenId(long phoneScreenId) {
		this.phoneScreenId = phoneScreenId;
	}

	public short getPhoneScreenStatusCode() {
		return phoneScreenStatusCode;
	}

	public void setPhoneScreenStatusCode(short phoneScreenStatusCode) {
		this.phoneScreenStatusCode = phoneScreenStatusCode;
	}

}
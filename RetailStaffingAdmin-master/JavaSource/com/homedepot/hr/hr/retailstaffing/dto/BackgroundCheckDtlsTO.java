package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Background Check details  response in XML format
 * 
 * @author dxg8002
 * 
 */
@XStreamAlias("backgroundChkDtls")
public class BackgroundCheckDtlsTO implements Serializable {

	private static final long serialVersionUID = 8902782063909881410L;

	@XStreamAlias("hasSignedConsent")
	private boolean hasSignedConsent;
	
	@XStreamAlias("isDrivingPos")
	private boolean isDrivingPos;
	
	@XStreamAlias("isAlreadyInDrivingPos")
	private boolean isAlreadyInDrivingPos;
	
	@XStreamAlias("needBackgroundCheck")
	private boolean needsBackgroundCheck;
	
	@XStreamAlias("backgroundComponentsNeeded")
	private String backgroundComponentsNeeded;
	
	@XStreamAlias("backgroundPackageNeeded")
	private int backgroundPackageNeeded;
	
	@XStreamAlias("humanResourcesStoreTypeCode")
	private String humanResourcesStoreTypeCode;
	
	@XStreamAlias("consentSigDt")
	@DAOElement("CNSNT_SIG_DT")
	private Date cnsntSigDt;
	
	@XStreamAlias("consentStatCd")
	@DAOElement("CNSNT_STAT_CD")
	private int consentStatCd;
	
	@XStreamAlias("consentCompleteTs")
	@DAOElement("CMPLT_TS")
	private Timestamp consentCompleteTs;
	
	@XStreamAlias("aplcntLKey")
	@DAOElement("APLCNT_LINK_KEY")
	private String aplcntLKey;
	
	//ATS mts1876, Date Of Birth
	@XStreamAlias("dateOfBirth")
	private String dateOfBirth;
	
	//ATS mts1876, External Drivers License Info
	@XStreamAlias("extLicenseNum")
	private String extLicenseNum;
	
	@XStreamAlias("extLicenseState")
	private String extLicenseState;
	
	//CDP Project
	@XStreamAlias("candidateInitialsSignatureValue")
	private String candidateInitialsSignatureValue;
	
	@XStreamAlias("documentSignatureTimestamp")
	private String documentSignatureTimestamp;
	
	@XStreamAlias("backgroundCheckAuthorizationFlag")
	private boolean backgroundCheckAuthorizationFlag;
	
	@XStreamAlias("managerUserId")
	private String managerUserId;
	
	@XStreamAlias("reinitiateBgcConsent")
	private boolean reinitiateBgcConsent;
	
	public boolean isHasSignedConsent() {
		return hasSignedConsent;
	}

	public void setHasSignedConsent(boolean hasSignedConsent) {
		this.hasSignedConsent = hasSignedConsent;
	}

	public boolean isDrivingPos() {
		return isDrivingPos;
	}

	public void setDrivingPos(boolean isDrivingPos) {
		this.isDrivingPos = isDrivingPos;
	}

	public boolean isAlreadyInDrivingPos() {
		return isAlreadyInDrivingPos;
	}

	public void setAlreadyInDrivingPos(boolean isAlreadyInDrivingPos) {
		this.isAlreadyInDrivingPos = isAlreadyInDrivingPos;
	}

	public boolean isNeedsBackgroundCheck() {
		return needsBackgroundCheck;
	}

	public void setNeedsBackgroundCheck(boolean needsBackgroundCheck) {
		this.needsBackgroundCheck = needsBackgroundCheck;
	}

	public String getBackgroundComponentsNeeded() {
		return backgroundComponentsNeeded;
	}

	public void setBackgroundComponentsNeeded(String backgroundComponentsNeeded) {
		this.backgroundComponentsNeeded = backgroundComponentsNeeded;
	}

	public int getBackgroundPackageNeeded() {
		return backgroundPackageNeeded;
	}

	public void setBackgroundPackageNeeded(int backgroundPackageNeeded) {
		this.backgroundPackageNeeded = backgroundPackageNeeded;
	}

	public String getHumanResourcesStoreTypeCode() {
		return humanResourcesStoreTypeCode;
	}

	public void setHumanResourcesStoreTypeCode(String humanResourcesStoreTypeCode) {
		this.humanResourcesStoreTypeCode = humanResourcesStoreTypeCode;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getExtLicenseNum() {
		return extLicenseNum;
	}

	public void setExtLicenseNum(String extLicenseNum) {
		this.extLicenseNum = extLicenseNum;
	}

	public String getExtLicenseState() {
		return extLicenseState;
	}

	public void setExtLicenseState(String extLicenseState) {
		this.extLicenseState = extLicenseState;
	}

	public String getCandidateInitialsSignatureValue() {
		return candidateInitialsSignatureValue;
	}

	public void setCandidateInitialsSignatureValue(String candidateInitialsSignatureValue) {
		this.candidateInitialsSignatureValue = candidateInitialsSignatureValue;
	}

	public String getDocumentSignatureTimestamp() {
		return documentSignatureTimestamp;
	}

	public void setDocumentSignatureTimestamp(String documentSignatureTimestamp) {
		this.documentSignatureTimestamp = documentSignatureTimestamp;
	}

	public boolean isBackgroundCheckAuthorizationFlag() {
		return backgroundCheckAuthorizationFlag;
	}

	public void setBackgroundCheckAuthorizationFlag(boolean backgroundCheckAuthorizationFlag) {
		this.backgroundCheckAuthorizationFlag = backgroundCheckAuthorizationFlag;
	}

	public String getManagerUserId() {
		return managerUserId;
	}

	public void setManagerUserId(String managerUserId) {
		this.managerUserId = managerUserId;
	}

	public Date getCnsntSigDt() {
		return cnsntSigDt;
	}

	public void setCnsntSigDt(Date cnsntSigDt) {
		this.cnsntSigDt = cnsntSigDt;
	}

	public int getConsentStatCd() {
		return consentStatCd;
	}

	public void setConsentStatCd(int consentStatCd) {
		this.consentStatCd = consentStatCd;
	}

	public Timestamp getConsentCompleteTs() {
		return consentCompleteTs;
	}

	public void setConsentCompleteTs(Timestamp consentCompleteTs) {
		this.consentCompleteTs = consentCompleteTs;
	}

	public String getAplcntLKey() {
		return aplcntLKey;
	}

	public void setAplcntLKey(String aplcntLKey) {
		this.aplcntLKey = aplcntLKey;
	}

	public boolean isReinitiateBgcConsent() {
		return reinitiateBgcConsent;
	}

	public void setReinitiateBgcConsent(boolean reinitiateBgcConsent) {
		this.reinitiateBgcConsent = reinitiateBgcConsent;
	}
	

}

package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.homedepot.hr.hr.retailstaffing.util.DAODateConverterToDateTO;
import com.homedepot.hr.hr.retailstaffing.util.DB2Trimmer;
import com.homedepot.ta.aa.dao.builder.BooleanStringFlagProcessor;
import com.homedepot.ta.aa.dao.builder.DAOConverter;
import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CandidateInformation")
	
public class CandidateInfoTO implements Serializable {
	private static final long serialVersionUID = 362498820763181265L;
	
	@DAOElement("HR_SYS_STR_NBR")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("humanResourcesSystemStoreNumber")
	private String humanResourcesSystemStoreNumber;
	
	@DAOElement("EMPLT_REQN_NBR")
	@XStreamAlias("employmentRequisitionNumber")
	private int employmentRequisitionNumber;
	
	@DAOElement("EMPLT_POSN_CAND_ID")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("employmentPositionCandidateId")
	private String employmentPositionCandidateId;
	
	@DAOElement("CRT_TS")
	@XStreamAlias("createTimestamp")
	//Instance variable for createTimestamp
	private Timestamp createTimestamp;

	@DAOElement("CRT_USER_ID")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("createUserId")
	//Instance variable for createUserId
	private String createUserId;

	@DAOElement("INTVW_STAT_IND")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("interviewStatusIndicator")
	//Instance variable for interviewStatusIndicator
	private String interviewStatusIndicator;

	@DAOElement("REF_CHK_RSLT_IND")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("referenceCheckResultIndicator")
	//Instance variable for referenceCheckResultIndicator
	private String referenceCheckResultIndicator;

	@DAOElement("DRUG_TEST_RSLT_CD")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("drugTestResultCode")
	//Instance variable for drugTestResultCode
	private String drugTestResultCode;

	@DAOElement("CAND_OFFR_MADE_FLG")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("candidateOfferMadeFlag")
	//Instance variable for candidateOfferMadeFlag
	private String candidateOfferMadeFlag;
	
	@DAOElement("CAND_OFFR_STAT_IND")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("candidateOfferStatusIndicator")
	//Instance variable for candidateOfferStatusIndicator
	private String candidateOfferStatusIndicator;
	
	@DAOElement("INACTV_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	@XStreamAlias("inactiveDate")
	//Instance variable for inactiveDate
	private DateTO inactiveDate;

	@DAOElement("ACTV_FLG")
	@DAOConverter(BooleanStringFlagProcessor.class)
	@XStreamAlias("activeFlag")
	//Instance variable for activeFlag
	private boolean activeFlag;

	@DAOElement("LAST_UPD_USER_ID")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("lastUpdateUserId")
	//Instance variable for lastUpdateUserId
	private String lastUpdateUserId;

	@DAOElement("LAST_UPD_TS")
	@XStreamAlias("lastUpdateTimestamp")
	//Instance variable for lastUpdateTimestamp
	private Timestamp lastUpdateTimestamp;

	@DAOElement("HR_ACTN_SEQ_NBR")
	@XStreamAlias("humanResourcesActionSequenceNumber")
	//Instance variable for humanResourcesActionSequenceNumber
	private short humanResourcesActionSequenceNumber;

	@DAOElement("EMPL_STAT_ACTN_CD")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("employeeStatusActionCode")
	//Instance variable for employeeStatusActionCode
	private String employeeStatusActionCode;

	@DAOElement("ACTV_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	@XStreamAlias("activeDate")
	//Instance variable for activeDate
	private DateTO activeDate;

	@DAOElement("DRUG_TEST_ID")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("drugTestId")
	//Instance variable for drugTestId
	private String drugTestId;

	@DAOElement("CAND_OFFR_MADE_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	@XStreamAlias("candidateOfferMadeDate")
	//Instance variable for candidateOfferMadeDate
	private DateTO candidateOfferMadeDate;

	@DAOElement("OFFR_DCLN_RSN_CD")
	@XStreamAlias("offerDeclineReasonCode")
	//Instance variable for offerDeclineReasonCode
	private int offerDeclineReasonCode;

	@DAOElement("LTR_OF_INTENT_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	@XStreamAlias("letterOfIntentDate")
	//Instance variable for letterOfIntentDate
	private DateTO letterOfIntentDate;

	@DAOElement("ADVRS_ACTN_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	@XStreamAlias("adverseActionDate")
	//Instance variable for adverseActionDate
	private DateTO adverseActionDate;

	@XStreamAlias("applicantType")
	private String applicantType;
	
	@XStreamAlias("aid")
	private String aid;
	
	@XStreamAlias("name")
	private String name;
	
	@XStreamAlias("cpdFormComplete")
	private boolean cpdFormComplete;
	
	@XStreamAlias("parentalConsentFormsComplete")
	private String parentalConsentFormsComplete;
	
	@XStreamAlias("rehireEligible")
	private boolean rehireEligible;
	
	@XStreamAlias("termDt")
	private String termDt;
	
	@XStreamAlias("termRsn")
	private String termRsn;
	
	@XStreamAlias("termLoc")
	private String termLoc;
	
	//Added for FMS 7894 January 2016 CR's
	@XStreamAlias("doNotConsiderFor60Days")
	private String doNotConsiderFor60Days;	
	
	@XStreamAlias("employmentCandidateId")
	private String employmentCandidateId;	
	
	@DAOElement("EFF_BGN_DT")
	@XStreamAlias("effectiveBeginDate")
	private Date effectiveBeginDate;
	
	@DAOElement("EFF_END_DT")
	@XStreamAlias("effectiveEndDate")
	private Date effectiveEndDate;
	//***********************************
	
	@DAOElement("INTVW_RSLT_CRT_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	@XStreamAlias("submitInterviewResultsDT")
	private DateTO submitInterviewResultsDT;
	
	@DAOElement("INIT_OFFR_MADE_DT")
	@DAOConverter(DAODateConverterToDateTO.class)
	@XStreamAlias("submitOfferResultsDT")
	private DateTO submitOfferResultsDT;

	@XStreamAlias("IsCandMinorFlag")
	private boolean IsCandMinorFlag;
	
	@XStreamAlias("associateEmailForBgcConsent")
	private String associateEmailForBgcConsent;
	
	//getter method for createTimestamp
	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	//setter method for createTimestamp
	public void setCreateTimestamp(Timestamp aValue) {
		createTimestamp = aValue;
	}

	//getter method for createUserId
	public String getCreateUserId() {
		return createUserId;
	}

	//setter method for createUserId
	public void setCreateUserId(String aValue) {
		createUserId = aValue;
	}

	//getter method for interviewStatusIndicator
	public String getInterviewStatusIndicator() {
		return interviewStatusIndicator;
	}

	//setter method for interviewStatusIndicator
	public void setInterviewStatusIndicator(String aValue) {
		interviewStatusIndicator = aValue;
	}

	//getter method for referenceCheckResultIndicator
	public String getReferenceCheckResultIndicator() {
		return referenceCheckResultIndicator;
	}

	//setter method for referenceCheckResultIndicator
	public void setReferenceCheckResultIndicator(String aValue) {
		referenceCheckResultIndicator = aValue;
	}

	//getter method for drugTestResultCode
	public String getDrugTestResultCode() {
		return drugTestResultCode;
	}

	//setter method for drugTestResultCode
	public void setDrugTestResultCode(String aValue) {
		drugTestResultCode = aValue;
	}

	//getter method for candidateOfferMadeFlag
	public String getCandidateOfferMadeFlag() {
		return candidateOfferMadeFlag;
	}

	//setter method for candidateOfferMadeFlag
	public void setCandidateOfferMadeFlag(String aValue) {
		candidateOfferMadeFlag = aValue;
	}

	//getter method for candidateOfferStatusIndicator
	public String getCandidateOfferStatusIndicator() {
		return candidateOfferStatusIndicator;
	}

	//setter method for candidateOfferStatusIndicator
	public void setCandidateOfferStatusIndicator(String aValue) {
		candidateOfferStatusIndicator = aValue;
	}

	//getter method for inactiveDate
	public DateTO getInactiveDate() {
		return inactiveDate;
	}

	//setter method for inactiveDate
	public void setInactiveDate(DateTO aValue) {
		inactiveDate = aValue;
	}

	//getter method for activeFlag
	public boolean getActiveFlag() {
		return activeFlag;
	}

	//setter method for activeFlag
	public void setActiveFlag(boolean aValue) {
		activeFlag = aValue;
	}

	//getter method for lastUpdateUserId
	public String getLastUpdateUserId() {
		return lastUpdateUserId;
	}

	//setter method for lastUpdateUserId
	public void setLastUpdateUserId(String aValue) {
		lastUpdateUserId = aValue;
	}

	//getter method for lastUpdateTimestamp
	public Timestamp getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	//setter method for lastUpdateTimestamp
	public void setLastUpdateTimestamp(Timestamp aValue) {
		lastUpdateTimestamp = aValue;
	}

	//getter method for humanResourcesActionSequenceNumber
	public short getHumanResourcesActionSequenceNumber() {
		return humanResourcesActionSequenceNumber;
	}

	//setter method for humanResourcesActionSequenceNumber
	public void setHumanResourcesActionSequenceNumber(short aValue) {
		humanResourcesActionSequenceNumber = aValue;
	}

	//getter method for employeeStatusActionCode
	public String getEmployeeStatusActionCode() {
		return employeeStatusActionCode;
	}

	//setter method for employeeStatusActionCode
	public void setEmployeeStatusActionCode(String aValue) {
		employeeStatusActionCode = aValue;
	}

	//getter method for activeDate
	public DateTO getActiveDate() {
		return activeDate;
	}

	//setter method for activeDate
	public void setActiveDate(DateTO aValue) {
		activeDate = aValue;
	}

	//getter method for drugTestId
	public String getDrugTestId() {
		return drugTestId;
	}

	//setter method for drugTestId
	public void setDrugTestId(String aValue) {
		drugTestId = aValue;
	}

	//getter method for candidateOfferMadeDate
	public DateTO getCandidateOfferMadeDate() {
		return candidateOfferMadeDate;
	}

	//setter method for candidateOfferMadeDate
	public void setCandidateOfferMadeDate(DateTO aValue) {
		candidateOfferMadeDate = aValue;
	}

	//getter method for offerDeclineReasonCode
	public int getOfferDeclineReasonCode() {
		return offerDeclineReasonCode;
	}

	//setter method for offerDeclineReasonCode
	public void setOfferDeclineReasonCode(int aValue) {
		offerDeclineReasonCode = aValue;
	}

	//getter method for letterOfIntentDate
	public DateTO getLetterOfIntentDate() {
		return letterOfIntentDate;
	}

	//setter method for letterOfIntentDate
	public void setLetterOfIntentDate(DateTO aValue) {
		letterOfIntentDate = aValue;
	}

	//getter method for adverseActionDate
	public DateTO getAdverseActionDate() {
		return adverseActionDate;
	}

	//setter method for adverseActionDate
	public void setAdverseActionDate(DateTO aValue) {
		adverseActionDate = aValue;
	}

	public String getHumanResourcesSystemStoreNumber() {
		return humanResourcesSystemStoreNumber;
	}

	public void setHumanResourcesSystemStoreNumber(String humanResourcesSystemStoreNumber) {
		this.humanResourcesSystemStoreNumber = humanResourcesSystemStoreNumber;
	}

	public int getEmploymentRequisitionNumber() {
		return employmentRequisitionNumber;
	}

	public void setEmploymentRequisitionNumber(int employmentRequisitionNumber) {
		this.employmentRequisitionNumber = employmentRequisitionNumber;
	}

	public String getEmploymentPositionCandidateId() {
		return employmentPositionCandidateId;
	}

	public void setEmploymentPositionCandidateId(String employmentPositionCandidateId) {
		this.employmentPositionCandidateId = employmentPositionCandidateId;
	}

	public String getApplicantType() {
		return applicantType;
	}

	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String isParentalConsentFormsComplete() {
		return parentalConsentFormsComplete;
	}
	
	public boolean isCpdFormComplete() {
		return cpdFormComplete;
	}

	public void setCpdFormComplete(boolean cpdFormComplete) {
		this.cpdFormComplete = cpdFormComplete;
	}

	public boolean isRehireEligible() {
		return rehireEligible;
	}

	public void setRehireEligible(boolean rehireEligible) {
		this.rehireEligible = rehireEligible;
	}

	public String getTermDt() {
		return termDt;
	}

	public void setTermDt(String termDt) {
		this.termDt = termDt;
	}

	public String getTermRsn() {
		return termRsn;
	}

	public void setTermRsn(String termRsn) {
		this.termRsn = termRsn;
	}

	public String getTermLoc() {
		return termLoc;
	}

	public void setTermLoc(String termLoc) {
		this.termLoc = termLoc;
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

	/**
	 * @return the employmentCandidateId
	 */
	public String getEmploymentCandidateId() {
		return employmentCandidateId;
	}

	/**
	 * @param employmentCandidateId the employmentCandidateId to set
	 */
	public void setEmploymentCandidateId(String employmentCandidateId) {
		this.employmentCandidateId = employmentCandidateId;
	}

	/**
	 * @return the effectiveBeginDate
	 */
	public Date getEffectiveBeginDate() {
		return effectiveBeginDate;
	}

	/**
	 * @param effectiveBeginDate the effectiveBeginDate to set
	 */
	public void setEffectiveBeginDate(Date effectiveBeginDate) {
		this.effectiveBeginDate = effectiveBeginDate;
	}

	/**
	 * @return the effectiveEndDate
	 */
	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	/**
	 * @param effectiveEndDate the effectiveEndDate to set
	 */
	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
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

	public void setParentalConsentFormsComplete(String parentalConsentFormsComplete) {
		this.parentalConsentFormsComplete = parentalConsentFormsComplete;
	}

	public boolean isIsCandMinorFlag() {
		return IsCandMinorFlag;
	}

	public void setIsCandMinorFlag(boolean isCandMinorFlag) {
		IsCandMinorFlag = isCandMinorFlag;
	}

	public String getAssociateEmailForBgcConsent() {
		return associateEmailForBgcConsent;
	}

	public void setAssociateEmailForBgcConsent(String associateEmailForBgcConsent) {
		this.associateEmailForBgcConsent = associateEmailForBgcConsent;
	}
	
}

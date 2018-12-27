package com.homedepot.hr.hr.retailstaffing.dto;

//import java.sql.Date;
import java.sql.Timestamp;

//import com.homedepot.hr.hr.retailstaffing.util.DAODateConverterToDateTO;
import com.homedepot.hr.hr.retailstaffing.util.DB2Trimmer;
import com.homedepot.ta.aa.dao.builder.DAOConverter;
import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CandidateDTInformation")
public class CandidateDTInfoTO {
	
	@DAOElement("EMPLT_APLCNT_ID")
	@XStreamAlias("employmentApplicantID_DT")
	private String employmentApplicantID_DT;

	@DAOElement("DTEST_ORD_NBR")
	@XStreamAlias("drugTestOrderNumber_DT")
	private int drugTestOrderNumber_DT;

	@DAOElement("DTEST_RSN_CD")
	@XStreamAlias("drugTestReasonCode_DT")
	private int drugTestReasonCode_DT;

	@DAOElement("DTEST_TYP")
	@XStreamAlias("drugTestType_DT")
	private int drugTestType_DT;

	@DAOElement("EMPLT_REQN_NBR")
	@XStreamAlias("employmentRequisitionNumber_DT")
	private int employmentRequisitionNumber_DT;
	
	@DAOElement("RQSTR_EADDR")
	@XStreamAlias("requesterEmailAddress_DT")
	private String requesterEmailAddress_DT;
	
	@DAOElement("APLCNT_EADDR")
	@XStreamAlias("applicantEmailAddress_DT")
	private String applicantEmailAddress_DT;
	
	@DAOElement("CRT_TS")
	@XStreamAlias("createTimestamp_DT")
	//Instance variable for createTimestamp
	private Timestamp createTimestamp_DT;

	@DAOElement("CRT_USER_ID")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("createUserId_DT")
	//Instance variable for createUserId for Drug Test panel
	private String createUserId_DT;

	@DAOElement("RQST_CMPLT_TS")
	@XStreamAlias("requestCompTimestamp_DT")
	//Instance variable for requested completed Time stamp for Drug Test panel
	private Timestamp requestCompTimestamp_DT;


//	//getter method for employmentApplicantID_DT
	public String getEmploymentApplicantID_DT() {
		return employmentApplicantID_DT;
	}
	//setter method for employmentApplicantID_DT
	public void setEmploymentApplicantID_DT(String employmentApplicantID_DT) {
		this.employmentApplicantID_DT = employmentApplicantID_DT;
	}
	
	//getter method for drugTestOrderNumber_DT
	public int getDrugTestOrderNumber_DT() {
		return drugTestOrderNumber_DT;
	}
	//setter method for drugTestOrderNumber_DT
	public void setDrugTestOrderNumber_DT(int drugTestOrderNumber_DT) {
		this.drugTestOrderNumber_DT = drugTestOrderNumber_DT;
	}

	//getter method for drugTestReasonCode_DT
	public int getDrugTestReasonCode_DT() {
		return drugTestReasonCode_DT;
	}
	//setter method for drugTestReasonCode_DT
	public void setDrugTestReasonCode_DT(int drugTestReasonCode_DT) {
		this.drugTestReasonCode_DT = drugTestReasonCode_DT;
	}

	//getter method for drugTestType_DT
	public int getDrugTestType_DT() {
		return drugTestType_DT;
	}
	//setter method for drugTestType_DT
	public void setDrugTestType_DT(int drugTestType_DT) {
		this.drugTestType_DT = drugTestType_DT;
	}

	//getter method for EmploymentRequisitionNumber_DT
	public int getEmploymentRequisitionNumber_DT() {
		return employmentRequisitionNumber_DT;
	}
	//setter method for EmploymentRequisitionNumber_DT
	public void setEmploymentRequisitionNumber_DT(int employmentRequisitionNumber_DT) {
		this.employmentRequisitionNumber_DT = employmentRequisitionNumber_DT;
	}

	//getter method for requesterEmailAddress_DT
	public String getRequesterEmailAddress_DT() {
		return requesterEmailAddress_DT;
	}
	//setter method for requesterEmailAddress_DT
	public void setRequesterEmailAddress_DT(String requesterEmailAddress_DT) {
		this.requesterEmailAddress_DT = requesterEmailAddress_DT;
	}

	//getter method for applicantEmailAddress_DT
	public String getApplicantEmailAddress_DT() {
		return applicantEmailAddress_DT;
	}
	//setter method for applicantEmailAddress_DT
	public void setApplicantEmailAddress_DT(String applicantEmailAddress_DT) {
		this.applicantEmailAddress_DT = applicantEmailAddress_DT;
	}

	//getter method for createTimestamp_DT
	public Timestamp getCreateTimestamp_DT() {
		return createTimestamp_DT;
	}
	//setter method for createTimestamp_DT
	public void setCreateTimestamp_DT(Timestamp iValue) {
		createTimestamp_DT = iValue;
	}

	//getter method for createUserId_DT
	public String getCreateUserId_DT() {
		return createUserId_DT;
	}
	//setter method for createUserId_DT
	public void setCreateUserId_DT(String aValue) {
		createUserId_DT = aValue;
	}

	//getter method for requestCompTimestamp_DT
	public Timestamp getRequestCompTimestamp_DT() {
		return requestCompTimestamp_DT;
	}
	//setter method for requestCompTimestamp_DT
	public void setRequestCompTimestamp_DT(Timestamp cValue) {
		requestCompTimestamp_DT = cValue;
	}
	
}
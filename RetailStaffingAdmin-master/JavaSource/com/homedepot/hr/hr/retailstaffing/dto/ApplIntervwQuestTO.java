package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Timestamp;
//import java.util.List;



import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
//import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ApplIntervwQuestions")
public class ApplIntervwQuestTO implements Serializable {
	
	private static final long serialVersionUID = 362498820763181265L;
	
	@DAOElement("CRT_TS")
	@XStreamAlias("createTimestamp")
	//Instance variable for createTimestamp
	private Timestamp createTimestamp;
	
	@DAOElement("INTVW_QST_RSLT_IND")
	@XStreamAlias("interviewQuestRsltInd")
	//Instance variable for interviewQuestionResultIndicator
	private String interviewQuestionResultIndicator;

	@DAOElement("EMPLT_INTVW_QST_ID")
	@XStreamAlias("emplIntervwQuestId")
	//Instance variable for employmentInterviewQuestionId
	private String employmentInterviewQuestionId;
	
	//getter method for createTimestamp
	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	//setter method for createTimestamp
	public void setCreateTimestamp(Timestamp aValue) {
		createTimestamp = aValue;
	}
	
	//getter method for interviewQuestionResultIndicator
	public String getInterviewQuestionResultIndicator() {
		return interviewQuestionResultIndicator;
	}

	//setter method for interviewQuestionResultIndicator
	public void setInterviewQuestionResultIndicator(String aValue) {
		interviewQuestionResultIndicator = aValue;
	}

	//getter method for employmentInterviewQuestionId
	public String getEmploymentInterviewQuestionId() {
		return employmentInterviewQuestionId;
	}

	//setter method for employmentInterviewQuestionId
	public void setEmploymentInterviewQuestionId(String aValue) {
		employmentInterviewQuestionId = aValue;
	}
	
}

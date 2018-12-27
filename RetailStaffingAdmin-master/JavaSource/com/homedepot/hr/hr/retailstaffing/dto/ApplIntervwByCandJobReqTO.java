package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("ApplInterviewDetails")
public class ApplIntervwByCandJobReqTO implements Serializable {
	
	private static final long serialVersionUID = 362498820763181265L;
	
	@XStreamAlias("interviewerUserId")
	//Instance variable for interviewerUserId
	private String interviewerUserId;

	@XStreamAlias("interviewDate")
	//Instance variable for interviewDate
	private Date interviewDate;

	@XStreamAlias("createTimestamp")
	@DAOElement("CRT_TS")
	//Instance variable for createTimestamp
	private Timestamp createTimestamp;

	@XStreamAlias("interviewResultIndicator")
	@DAOElement("INTVW_RSLT_IND")
	//Instance variable for interviewResultIndicator
	private String interviewResultIndicator;

	@XStreamAlias("lastUpdateUserId")
	//Instance variable for lastUpdateUserId
	private String lastUpdateUserId;

	@XStreamAlias("lastUpdateTimestamp")
	//Instance variable for lastUpdateTimestamp
	private Timestamp lastUpdateTimestamp;


	//getter method for interviewerUserId
	public String getInterviewerUserId() {
		return interviewerUserId;
	}

	//setter method for interviewerUserId
	public void setInterviewerUserId(String aValue) {
		interviewerUserId = aValue;
	}

	//getter method for interviewDate
	public Date getInterviewDate() {
		return interviewDate;
	}

	//setter method for interviewDate
	public void setInterviewDate(Date aValue) {
		interviewDate = aValue;
	}

	//getter method for createTimestamp
	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	//setter method for createTimestamp
	public void setCreateTimestamp(Timestamp aValue) {
		createTimestamp = aValue;
	}

	//getter method for interviewResultIndicator
	public String getInterviewResultIndicator() {
		return interviewResultIndicator;
	}

	//setter method for interviewResultIndicator
	public void setInterviewResultIndicator(String aValue) {
		interviewResultIndicator = aValue;
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

}

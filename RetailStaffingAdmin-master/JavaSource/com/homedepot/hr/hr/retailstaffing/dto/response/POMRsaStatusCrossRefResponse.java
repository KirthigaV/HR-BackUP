package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("pomRsaStatusCrossRefResponse")
public class POMRsaStatusCrossRefResponse implements Serializable
{
	/**
	 * @return the error
	 */
	public ErrorTO getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ErrorTO error) {
		this.error = error;
	}

	private static final long serialVersionUID = 362498820763181265L;

	//Instance variable for rsaUpdateFlag
	@XStreamAlias("status")
	private String status;
	
	//Instance variable for pomCompleteStatusCode
	@XStreamAlias("pomCompleteStatusCode")
	private short pomCompleteStatusCode;

	//Instance variable for interviewRespondStatusCode
	@XStreamAlias("interviewRespondStatusCode")
	private short interviewRespondStatusCode;

	//Instance variable for rsaUpdateFlag
	@XStreamAlias("rsaUpdateFlag")
	private boolean rsaUpdateFlag;
	
	/** error */
	@XStreamAlias("error")
	public ErrorTO  error;
		
	//getter method for pomCompleteStatusCode
	public short getPomCompleteStatusCode() {
		return pomCompleteStatusCode;
	}

	//setter method for pomCompleteStatusCode
	public void setPomCompleteStatusCode(short aValue) {
		pomCompleteStatusCode = aValue;
	}

	//getter method for interviewRespondStatusCode
	public short getInterviewRespondStatusCode() {
		return interviewRespondStatusCode;
	}

	//setter method for interviewRespondStatusCode
	public void setInterviewRespondStatusCode(short aValue) {
		interviewRespondStatusCode = aValue;
	}

	//getter method for rsaUpdateFlag
	public boolean getRsaUpdateFlag() {
		return rsaUpdateFlag;
	}

	//setter method for rsaUpdateFlag
	public void setRsaUpdateFlag(boolean aValue) {
		rsaUpdateFlag = aValue;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}

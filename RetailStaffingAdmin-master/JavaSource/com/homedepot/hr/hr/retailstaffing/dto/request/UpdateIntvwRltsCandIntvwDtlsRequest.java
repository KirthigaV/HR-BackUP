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
 * File Name: UpdateStaffingRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;

import com.homedepot.hr.hr.retailstaffing.dto.BackgroundCheckDtlsTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.InterviewInformationTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create UpdateIntvwRltsCandIntvwDtlsRequest object  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("UpdateIntvwRltsCandIntvwDtlsRequest")
public class UpdateIntvwRltsCandIntvwDtlsRequest implements Serializable
{	
	
	private static final long serialVersionUID = 1236796915468229970L;

	@XStreamAlias("CandidateInformation")
	private CandidateInfoTO candidateInfoTo;
	
	@XStreamAlias("InterviewInformation")
	private InterviewInformationTO interviewInformationTo;
	
	@XStreamAlias("BackgroundCheckInfo")
	private BackgroundCheckDtlsTO backgroundCheckInfo;

	public CandidateInfoTO getCandidateInfoTo() {
		return candidateInfoTo;
	}

	public void setCandidateInfoTo(CandidateInfoTO candidateInfoTo) {
		this.candidateInfoTo = candidateInfoTo;
	}

	public InterviewInformationTO getInterviewInformationTo() {
		return interviewInformationTo;
	}

	public void setInterviewInformationTo(InterviewInformationTO interviewInformationTo) {
		this.interviewInformationTo = interviewInformationTo;
	}

	public BackgroundCheckDtlsTO getBackgroundCheckInfo() {
		return backgroundCheckInfo;
	}

	public void setBackgroundCheckInfo(BackgroundCheckDtlsTO backgroundCheckInfo) {
		this.backgroundCheckInfo = backgroundCheckInfo;
	}

	
}

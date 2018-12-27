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
 * This class is used to create SubmitBgcCandidateDtlsRequest object  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("SubmitBgcCandidateDtlsRequest")
public class SubmitBgcCandidateDtlsRequest implements Serializable
{	
	private static final long serialVersionUID = -6597797282235867281L;

	@XStreamAlias("CandidateInformation")
	private CandidateInfoTO candidateInfoTo;
	
	@XStreamAlias("BackgroundCheckInfo")
	private BackgroundCheckDtlsTO backgroundCheckInfo;

	public CandidateInfoTO getCandidateInfoTo() {
		return candidateInfoTo;
	}

	public void setCandidateInfoTo(CandidateInfoTO candidateInfoTo) {
		this.candidateInfoTo = candidateInfoTo;
	}

	public BackgroundCheckDtlsTO getBackgroundCheckInfo() {
		return backgroundCheckInfo;
	}

	public void setBackgroundCheckInfo(BackgroundCheckDtlsTO backgroundCheckInfo) {
		this.backgroundCheckInfo = backgroundCheckInfo;
	}

	
}

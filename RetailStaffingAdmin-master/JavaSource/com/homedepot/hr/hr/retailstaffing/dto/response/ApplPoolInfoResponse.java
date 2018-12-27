/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplProfileResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as final response object for appl profile response
 * 
 * @author TCS
 * 
 */
@XStreamAlias("Response")
public class ApplPoolInfoResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("status")
	private String status;

	@XStreamAlias("error")
	private GenericErrorTO errorResponse;

	@XStreamAlias("CandidateCountList")
	private CandidateCountResponse CandidateCountRes;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public GenericErrorTO getErrorResponse() {
		return errorResponse;
	}

	public void setErrorResponse(GenericErrorTO errorResponse) {
		this.errorResponse = errorResponse;
	}

	public CandidateCountResponse getCandidateCountRes() {
		return CandidateCountRes;
	}

	public void setCandidateCountRes(CandidateCountResponse candidateCountRes) {
		CandidateCountRes = candidateCountRes;
	}
	
}

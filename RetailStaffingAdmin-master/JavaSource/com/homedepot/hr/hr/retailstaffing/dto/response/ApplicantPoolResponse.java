/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplicantPoolResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;

import com.homedepot.hr.hr.retailstaffing.dto.PagingRecordInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as final response object for application pool response
 * 
 * @author
 * 
 */
@XStreamAlias("Response")
public class ApplicantPoolResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("status")
	private String status;

	@XStreamAlias("error")
	private GenericErrorTO errorResponse;

	@XStreamAlias("ApplicantList")
	private ApplicantResponse applApplicantRes;
	
	@XStreamAlias("firstRecord")
	PagingRecordInfo firstRecordInfo;

	@XStreamAlias("secondRecord")
	PagingRecordInfo secondRecordInfo;

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

	public ApplicantResponse getApplApplicantRes() {
		return applApplicantRes;
	}

	public void setApplApplicantRes(ApplicantResponse applApplicantRes) {
		this.applApplicantRes = applApplicantRes;
	}

	public PagingRecordInfo getFirstRecordInfo() {
		return firstRecordInfo;
	}

	public void setFirstRecordInfo(PagingRecordInfo firstRecordInfo) {
		this.firstRecordInfo = firstRecordInfo;
	}

	public PagingRecordInfo getSecondRecordInfo() {
		return secondRecordInfo;
	}

	public void setSecondRecordInfo(PagingRecordInfo secondRecordInfo) {
		this.secondRecordInfo = secondRecordInfo;
	}
	

}

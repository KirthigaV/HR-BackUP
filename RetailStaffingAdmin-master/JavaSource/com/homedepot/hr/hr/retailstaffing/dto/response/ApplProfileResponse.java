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
public class ApplProfileResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("status")
	private String status;

	@XStreamAlias("error")
	private GenericErrorTO errorResponse;

	@XStreamAlias("PersonalInfoList")
	private ApplPersonalInfoResponse appPersonalInfoRes;
	
	@XStreamAlias("AssociateInfoList")
	private AssociateWorkInfoResponse appAssociateInfoRes;
	
	@XStreamAlias("AssociateReviewList")
	private AssociateReviewResponse associateReviewRes;
	
	@XStreamAlias("AssociatePrePosList")
	private AssociatePrePosResponse associatePrePosRes;

	@XStreamAlias("EducationList")
	private ApplEducationInfoResponse applEducationInfoRes;
	
	@XStreamAlias("WorkHistoryList")
	private ApplWorkHistoryInfoResponse applWorkHistoryInfoRes;
	
	@XStreamAlias("JobPrefList")
	private ApplJobPrefInfoResponse applJobPrefInfoRes;
	
	@XStreamAlias("LanguageList")
	private ApplLangInfoResponse applLangInfoRes;
	
	@XStreamAlias("PhnScreenHistoryList")
	private ApplPhnScreenInfoResponse applPhnScreenInfoRes;
	
	@XStreamAlias("CandidateHistoryList")
	private ApplHistoryInfoResponse applHistoryInfoRes;
	
	@XStreamAlias("ApplicationHistoryList")
	private ApplApplicationHistoryInfoResponse applAppHistoryInfoRes;
	
	@XStreamAlias("ApplicationHistoryList2")
	private ApplApplicationHistoryInfoResponse2 applAppHistoryInfoRes2;

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

	public ApplPersonalInfoResponse getAppPersonalInfoRes() {
		return appPersonalInfoRes;
	}

	public void setAppPersonalInfoRes(ApplPersonalInfoResponse appPersonalInfoRes) {
		this.appPersonalInfoRes = appPersonalInfoRes;
	}

	public ApplEducationInfoResponse getApplEducationInfoRes() {
		return applEducationInfoRes;
	}

	public void setApplEducationInfoRes(
			ApplEducationInfoResponse applEducationInfoRes) {
		this.applEducationInfoRes = applEducationInfoRes;
	}

	public ApplWorkHistoryInfoResponse getApplWorkHistoryInfoRes() {
		return applWorkHistoryInfoRes;
	}

	public void setApplWorkHistoryInfoRes(
			ApplWorkHistoryInfoResponse applWorkHistoryInfoRes) {
		this.applWorkHistoryInfoRes = applWorkHistoryInfoRes;
	}

	public ApplJobPrefInfoResponse getApplJobPrefInfoRes() {
		return applJobPrefInfoRes;
	}

	public void setApplJobPrefInfoRes(ApplJobPrefInfoResponse applJobPrefInfoRes) {
		this.applJobPrefInfoRes = applJobPrefInfoRes;
	}

	public ApplLangInfoResponse getApplLangInfoRes() {
		return applLangInfoRes;
	}

	public void setApplLangInfoRes(ApplLangInfoResponse applLangInfoRes) {
		this.applLangInfoRes = applLangInfoRes;
	}

	public ApplPhnScreenInfoResponse getApplPhnScreenInfoRes() {
		return applPhnScreenInfoRes;
	}

	public void setApplPhnScreenInfoRes(
			ApplPhnScreenInfoResponse applPhnScreenInfoRes) {
		this.applPhnScreenInfoRes = applPhnScreenInfoRes;
	}

	public AssociateReviewResponse getAssociateReviewRes() {
		return associateReviewRes;
	}

	public void setAssociateReviewRes(AssociateReviewResponse associateReviewRes) {
		this.associateReviewRes = associateReviewRes;
	}

	public ApplHistoryInfoResponse getApplHistoryInfoRes() {
		return applHistoryInfoRes;
	}

	public void setApplHistoryInfoRes(ApplHistoryInfoResponse applHistoryInfoRes) {
		this.applHistoryInfoRes = applHistoryInfoRes;
	}

	public ApplApplicationHistoryInfoResponse getApplAppHistoryInfoRes() {
		return applAppHistoryInfoRes;
	}

	public void setApplAppHistoryInfoRes(
			ApplApplicationHistoryInfoResponse applAppHistoryInfoRes) {
		this.applAppHistoryInfoRes = applAppHistoryInfoRes;
	}

	public AssociateWorkInfoResponse getAppAssociateInfoRes() {
		return appAssociateInfoRes;
	}

	public void setAppAssociateInfoRes(AssociateWorkInfoResponse appAssociateInfoRes) {
		this.appAssociateInfoRes = appAssociateInfoRes;
	}
	
	public AssociatePrePosResponse getAssociatePrePosRes() {
		return associatePrePosRes;
	}

	public void setAssociatePrePosRes(AssociatePrePosResponse associatePrePosRes) {
		this.associatePrePosRes = associatePrePosRes;
	}

	public ApplApplicationHistoryInfoResponse2 getApplAppHistoryInfoRes2() {
		return applAppHistoryInfoRes2;
	}

	public void setApplAppHistoryInfoRes2(
			ApplApplicationHistoryInfoResponse2 applAppHistoryInfoRes2) {
		this.applAppHistoryInfoRes2 = applAppHistoryInfoRes2;
	}

}

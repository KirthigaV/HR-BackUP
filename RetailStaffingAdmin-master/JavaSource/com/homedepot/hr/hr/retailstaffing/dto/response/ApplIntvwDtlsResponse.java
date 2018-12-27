
/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: InterviewAvailResponse.java
 */

package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ApplIntvwDtlsResponse")
public class ApplIntvwDtlsResponse  implements Serializable{

	private static final long serialVersionUID = -2638070594396672684L;

	@XStreamAlias("interviewerUserId")
	private String interviewerUserId;
	
	@XStreamAlias("interviewDate")
	private String interviewDate;
	
	@XStreamAlias("interviewResultIndicator")
	private String interviewResultIndicator;

	public String getInterviewerUserId() {
		return interviewerUserId;
	}

	public void setInterviewerUserId(String interviewerUserId) {
		this.interviewerUserId = interviewerUserId;
	}

	public String getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(String interviewDate) {
		this.interviewDate = interviewDate;
	}

	public String getInterviewResultIndicator() {
		return interviewResultIndicator;
	}

	public void setInterviewResultIndicator(String interviewResultIndicator) {
		this.interviewResultIndicator = interviewResultIndicator;
	}
	
}

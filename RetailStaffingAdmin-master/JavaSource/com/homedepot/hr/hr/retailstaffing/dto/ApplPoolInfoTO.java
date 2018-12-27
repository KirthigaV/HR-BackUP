/*
 * Created on October 15, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: ApplicantPersonalInfoTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send education response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("CandidateCountDetail")
public class ApplPoolInfoTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;
	
	@XStreamAlias("candidateCount")
	private String candidateCount;

	public String getCandidateCount() {
		return candidateCount;
	}

	public void setCandidateCount(String candidateCount) {
		this.candidateCount = candidateCount;
	}
		
}

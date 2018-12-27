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
 * File Name: CreatePhoneScreenRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used to create PhoneScreenIntrwDetailsTO and CandidateDetailsTO for Input XML
 * 
 * @author TCS
 * 
 */

@XStreamAlias("CreatePhoneScreenRequest")
public class CreatePhoneScreenRequest
{
	@XStreamAlias("PhoneScreenIntrwDetail")
	private PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO;

	@XStreamAlias("CandidateList")
	private List<CandidateDetailsTO> candidateDetailsTOS;

	/**
	 * @return the phoneScreenIntrwDetailsTO
	 */
	public PhoneScreenIntrwDetailsTO getPhoneScreenIntrwDetailsTO()
	{
		return phoneScreenIntrwDetailsTO;
	}

	/**
	 * @param phoneScreenIntrwDetailsTO the phoneScreenIntrwDetailsTO to set
	 */
	public void setPhoneScreenIntrwDetailsTO(
			PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO)
	{
		this.phoneScreenIntrwDetailsTO = phoneScreenIntrwDetailsTO;
	}

	/**
	 * @return the candidateDetailsTOS
	 */
	public List<CandidateDetailsTO> getCandidateDetailsTOS()
	{
		return candidateDetailsTOS;
	}

	/**
	 * @param candidateDetailsTOS the candidateDetailsTOS to set
	 */
	public void setCandidateDetailsTOS(List<CandidateDetailsTO> candidateDetailsTOS)
	{
		this.candidateDetailsTOS = candidateDetailsTOS;
	}

	

}

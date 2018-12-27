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

import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used to create PhoneScreenIntrwDetailsTO and CandidateDetailsTO for Input XML
 * 
 * @author TCS
 * 
 */

@XStreamAlias("UpdateReviewPhnScrnRequest")
public class UpdateReviewPhnScrnRequest
{
	@XStreamAlias("RequisitionDetail")
	private RequisitionDetailTO reqDetailsTO;

	
	@XStreamAlias("ITIDetailList")
	private List<PhoneScreenIntrwDetailsTO> phnScrnIntrwDetTOs;

	/**
	 * @return the reqDetailsTO
	 */
	public RequisitionDetailTO getReqDetailsTO()
	{
		return reqDetailsTO;
	}

	/**
	 * @param reqDetailsTO the reqDetailsTO to set
	 */
	public void setReqDetailsTO(RequisitionDetailTO reqDetailsTO)
	{
		this.reqDetailsTO = reqDetailsTO;
	}

	/**
	 * @return the phnScrnIntrwDetTOs
	 */
	public List<PhoneScreenIntrwDetailsTO> getPhnScrnIntrwDetTOs()
	{
		return phnScrnIntrwDetTOs;
	}

	/**
	 * @param phnScrnIntrwDetTOs the phnScrnIntrwDetTOs to set
	 */
	public void setPhnScrnIntrwDetTOs(
			List<PhoneScreenIntrwDetailsTO> phnScrnIntrwDetTOs)
	{
		this.phnScrnIntrwDetTOs = phnScrnIntrwDetTOs;
	}

	
	
	

}

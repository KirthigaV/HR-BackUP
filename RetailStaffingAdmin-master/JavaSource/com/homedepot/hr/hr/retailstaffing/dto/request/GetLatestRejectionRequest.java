/*
 * Created on January 5, 2013
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: CreateRejectionRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import com.homedepot.hr.hr.retailstaffing.dto.RejectionDetailTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used to create RejectionDetailTO for Input XML
 * 
 * @author GXN5764
 * 
 */

@XStreamAlias("GetLatestRejectionRequest")
public class GetLatestRejectionRequest
{
	@XStreamAlias("RejectionDetail")
	private RejectionDetailTO rejectionDetailTO;

	/**
	 * @return the reqDetailsTO
	 */
	public RejectionDetailTO getReqDetailsTO()
	{
		return rejectionDetailTO;
	}

	/**
	 * @param reqDetailsTO the reqDetailsTO to set
	 */
	public void setReqDetailsTO(RejectionDetailTO rejectionDetailTO)
	{
		this.rejectionDetailTO = rejectionDetailTO;
	}

}

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionDetailResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Requisition Search
 * 
 * @author TCS
 * 
 */
@XStreamAlias("RequisitionDetailList")
public class RequisitionDetailResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("RequisitionDetail")
	private List<RequisitionDetailTO> reqDtlList;

	/**
	 * @param reqDtlList
	 *            the reqDtlList to set
	 */
	public void setReqDtlList(List<RequisitionDetailTO> reqDtlList)
	{
		this.reqDtlList = reqDtlList;
	}

	/**
	 * @return the reqDtlList
	 */
	public List<RequisitionDetailTO> getReqDtlList()
	{
		return reqDtlList;
	}

}

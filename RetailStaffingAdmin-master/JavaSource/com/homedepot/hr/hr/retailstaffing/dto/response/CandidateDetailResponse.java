/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: CandidateDetailResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Phone Screen Search
 * 
 * @author TCS
 * 
 */
@XStreamAlias("CandidateDetailList")
public class CandidateDetailResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("CandidateDetail")
	private List<CandidateDetailsTO> cndDtlList;

	/**
	 * @param reqDtlList
	 *            the reqDtlList to set
	 */
	public void setCndDtlList(List<CandidateDetailsTO> cndDtlList)
	{
		this.cndDtlList = cndDtlList;
	}

	/**
	 * @return the reqDtlList
	 */
	public List<CandidateDetailsTO> getCndDtlList()
	{
		return cndDtlList;
	}

}

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplHistoryInfoResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ApplHistoryInfoTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for appl history
 * 
 * @author TCS
 * 
 */
@XStreamAlias("CandidateHistoryList")
public class ApplHistoryInfoResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("CandidateHistory")
	private List<ApplHistoryInfoTO> applHistoryInfoTOList;

	public List<ApplHistoryInfoTO> getApplHistoryInfoTOList() {
		return applHistoryInfoTOList;
	}

	public void setApplHistoryInfoTOList(
			List<ApplHistoryInfoTO> applHistoryInfoTOList) {
		this.applHistoryInfoTOList = applHistoryInfoTOList;
	}
	
}
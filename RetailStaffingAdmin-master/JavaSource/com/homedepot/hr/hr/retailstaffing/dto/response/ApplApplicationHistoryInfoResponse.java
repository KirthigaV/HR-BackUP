/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplAppHistoryInfoResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ApplAppHistoryInfoTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for application history
 * 
 * @author
 * 
 */
@XStreamAlias("ApplicationHistoryList")
public class ApplApplicationHistoryInfoResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("ApplicationHistory")
	private List<ApplAppHistoryInfoTO> applAppHistoryInfoTOList;

	public List<ApplAppHistoryInfoTO> getApplAppHistoryInfoTOList() {
		return applAppHistoryInfoTOList;
	}

	public void setApplAppHistoryInfoTOList(
			List<ApplAppHistoryInfoTO> applAppHistoryInfoTOList) {
		this.applAppHistoryInfoTOList = applAppHistoryInfoTOList;
	}
	
}
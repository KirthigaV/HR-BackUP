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
 * File Name: AttachApplToReqRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ApplToReqDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create AttachApplToReqRequest object from Input XML
 * 
 * @author
 * 
 */
@XStreamAlias("ApplToReqRequest")
public class ApplToReqRequest {

	@XStreamAlias("ApplToReqList")
	private List<ApplToReqDetailsTO> applToReqDetailsTOList;
	

	public List<ApplToReqDetailsTO> getApplToReqDetailsTOList() {
		return applToReqDetailsTOList;
	}

	public void setApplToReqDetailsTOList(
			List<ApplToReqDetailsTO> applToReqDetailsTOList) {
		this.applToReqDetailsTOList = applToReqDetailsTOList;
	}
	
}

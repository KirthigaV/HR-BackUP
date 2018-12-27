/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplPersonalInfoResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ApplPersonalInfoTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for appl personal info
 * 
 * @author TCS
 * 
 */
@XStreamAlias("PersonalInfoList")
public class ApplPersonalInfoResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("PersonalInfo")
	private List<ApplPersonalInfoTO> applPersonalInfoList;

	/**
	 * @param applPersonalInfoList
	 *      	 the applPersonalInfoList to set
	 */
	public void setApplPersonalInfoList(
			List<ApplPersonalInfoTO> applPersonalInfoList) {
		this.applPersonalInfoList = applPersonalInfoList;
	}
	/**
	 * @return the applPersonalInfoList
	 */
	public List<ApplPersonalInfoTO> getApplPersonalInfoList() {
		return applPersonalInfoList;
	}

	
}
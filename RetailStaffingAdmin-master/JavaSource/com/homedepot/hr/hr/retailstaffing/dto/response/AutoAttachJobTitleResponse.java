/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StateDetailResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.AutoAttachJobTitlesTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Auto-Attach Job Titles
 * 
 * @author TCS
 * 
 */
@XStreamAlias("AutoAttachJobTitleList")
public class AutoAttachJobTitleResponse implements Serializable
{
	private static final long serialVersionUID = 1439555259849419007L;

	@XStreamImplicit
	@XStreamAlias("AutoAttachJobTitleDetail")
	private List<AutoAttachJobTitlesTO> aaJobTtlList;

	public List<AutoAttachJobTitlesTO> getAaJobTtlList() {
		return aaJobTtlList;
	}

	public void setAaJobTtlList(List<AutoAttachJobTitlesTO> aaJobTtlList) {
		this.aaJobTtlList = aaJobTtlList;
	}

}

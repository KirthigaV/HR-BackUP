/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: NoInterviewReasonResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ComboOptionsSortTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This is used as the final response object for Structured Interview Guide List.
 * 
 * @author TCS
 * 
 * 
 */
@XStreamAlias("StructuredInterviewGuideList")
public class StructuredInterviewGuideListResponse implements Serializable
{

	private static final long serialVersionUID = -5698740761318176254L;
	
	@XStreamImplicit
	@XStreamAlias("option")
	private List<ComboOptionsSortTO> dtlList;

	public List<ComboOptionsSortTO> getDtlList() {
		return dtlList;
	}

	public void setDtlList(List<ComboOptionsSortTO> dtlList) {
		this.dtlList = dtlList;
	}



}

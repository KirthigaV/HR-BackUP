/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: DeptResponse.java
 * Application: RetailStaffingRequest
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;


import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Schedule Description Drop Downs
 * 
 * @author SXK8130
 * 
 */
@XStreamAlias("ScheduleDescList")
public class ScheduleDescResponse implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -222488532461161523L;
	
	@XStreamImplicit
	@XStreamAlias("ScheduleDescDetail")
		private List<RequisitionCalendarTO> mSchDescList;
	
	public List<RequisitionCalendarTO> getSchDescList() {
		return mSchDescList;
	}
	public void setSchDescList(
			List<RequisitionCalendarTO> schDescList) {
		this.mSchDescList = schDescList;
	}
}

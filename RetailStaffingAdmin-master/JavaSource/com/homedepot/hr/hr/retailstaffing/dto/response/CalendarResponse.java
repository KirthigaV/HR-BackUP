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

import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Department Drop Downs
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("CalendarList")
public class CalendarResponse implements Serializable {	

	private static final long serialVersionUID = -933135850692045731L;
	@XStreamImplicit
	@XStreamAlias("CalendarDetail")
	private List<RequisitionScheduleTO> mSchedules;
	
	public CalendarResponse()
	{
		
	} // end constructor
	
	public CalendarResponse(List<RequisitionScheduleTO> schedules)
	{
		mSchedules = schedules;
	} // end constructor
	
	public List<RequisitionScheduleTO> getSchedules()
	{
		return mSchedules;
	} // end function getSchedules()

	public void setSchedules(List<RequisitionScheduleTO> schedules)
	{
		mSchedules = schedules;
	} // end function setSchedules()	
}

package com.homedepot.hr.hr.staffingforms.service.request;
/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: CalendarRequest.java
 */
import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.staffingforms.dto.AvailabilityBlock;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Request object that will be populated whenever a call is made to certain methods
 * of the CalendarService. Using the XStream API to marshal this object into an XML will 
 * produce the following format:<br>
 * <br>
 * <code>
 * &lt;calendarRequest&gt;<br>
 * &nbsp;&lt;availabilityBlocks&gt;<br>
 * &nbsp;&nbsp;&lt;availabilityBlock&gt;&lt;/availabilityBlock&gt;<br>
 * &nbsp;&lt;/availabilityBlocks&gt;<br>
 * &nbsp;&lt;requisitionSchedules&gt;<br>
 * &nbsp;&nbsp;&lt;requisitionSchedule&gt;&lt;/requisitionSchedule&gt;<br>
 * &nbsp;&lt;/requisitionSchedules&gt;<br>
 * &lt;/calendarRequest&gt;<br>
 * </code>
 * <br>
 * Only non-null member variables will be present in the XML generated by the XStream API.<br>
 * <br>
 * @see AvailabilityBlock
 * @see RequisitionSchedule
 */
@XStreamAlias("calendarRequest")
public class CalendarRequest implements Serializable
{
	private static final long serialVersionUID = -2345515392070714326L;

	// collection of availability blocks, populated during calls to /addAvailability
	@XStreamAlias("availabilityBlocks")
	private List<AvailabilityBlock> mAvailBlocks;
	
	// collection of interview slots, populated during calls to /removeAvailability
	@XStreamAlias("requisitionSchedules")
	private List<RequisitionSchedule> mReqSchedules;
	
	/**
	 * @return collection of availability blocks
	 */
	public List<AvailabilityBlock> getAvailBlocks()
	{
		return mAvailBlocks;
	} // end function getAvailBlocks()
	
	/**
	 * @param availBlocks collection of availability blocks
	 */
	public void setAvailBlocks(List<AvailabilityBlock> availBlocks)
	{
		mAvailBlocks = availBlocks;
	} // end function setAvailBlocks()
	
	/**
	 * @return collection of requisition schedule objects
	 */
	public List<RequisitionSchedule> getReqSchedules()
	{
		return mReqSchedules;
	} // end function getReqSchedules()
	
	/**
	 * @param schedules collection of requisition schedule objects
	 */
	public void setReqSchedules(List<RequisitionSchedule> schedules)
	{
		mReqSchedules = schedules;
	} // end function setReqSchedules()
} // end class CalendarRequest
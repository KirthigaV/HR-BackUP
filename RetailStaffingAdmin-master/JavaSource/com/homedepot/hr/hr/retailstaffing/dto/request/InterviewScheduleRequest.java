package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.InterviewScheduleRequestDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("InterviewScheduleRequest")
public class InterviewScheduleRequest {
	
	@XStreamAlias("InterviewScheduleRequestList")
	private List<InterviewScheduleRequestDetailsTO> intrwSchedReqList;
	
	public void setMinResList(List<InterviewScheduleRequestDetailsTO> intrwSchedReqList)
	{
		this.intrwSchedReqList = intrwSchedReqList;
	}

	/**
	 * @return the minResList
	 */
	public List<InterviewScheduleRequestDetailsTO> intrwSchedReqList()
	{
		return intrwSchedReqList;
	}

}

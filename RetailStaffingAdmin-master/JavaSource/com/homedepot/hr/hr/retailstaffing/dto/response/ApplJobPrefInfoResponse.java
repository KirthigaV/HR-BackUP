/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplJobPrefInfoResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for appl job preference
 * 
 * @author TCS
 * 
 */
@XStreamAlias("JobPrefList")
public class ApplJobPrefInfoResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("JobPrefSet")
	private List<SchedulePreferenceTO> applJobPrefInfoTO;

	public List<SchedulePreferenceTO> getApplJobPrefInfoTO() {
		return applJobPrefInfoTO;
	}

	public void setApplJobPrefInfoTO(List<SchedulePreferenceTO> applJobPrefInfoTO) {
		this.applJobPrefInfoTO = applJobPrefInfoTO;
	}

}
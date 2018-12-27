/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: JobTtlResponse.java
 * Application: RetailStaffingRequest
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.JobTtlDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Job Title Drop Downs
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("JobTitleList")
public class JobTtlResponse implements Serializable {

	private static final long serialVersionUID = -3633763698164099775L;
	
	@XStreamImplicit
	@XStreamAlias("JobTitleDetail")
	private List<JobTtlDetailsTO> jobTtlList;

	public void setJobTtlList(List<JobTtlDetailsTO> jobTtlList) {
		this.jobTtlList = jobTtlList;
	}

	public List<JobTtlDetailsTO> getJobTtlList() {
		return jobTtlList;
	}

}

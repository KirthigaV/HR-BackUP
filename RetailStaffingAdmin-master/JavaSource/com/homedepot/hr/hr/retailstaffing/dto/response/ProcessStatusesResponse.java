/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: processStatusesResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ProcessStatusTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This is used as the final response object for the process statuses.
 * 
 * @author TCS
 * 
 * 
 */
@XStreamAlias("ProcessStatusList")
public class ProcessStatusesResponse implements Serializable
{

	private static final long serialVersionUID = -2114958812533483936L;
	@XStreamImplicit
	@XStreamAlias("Status")
	private List<ProcessStatusTO> stsDtlList;

	/**
	 * @param stsDtlList
	 *            the stsDtlList to set
	 */
	public void setStsDtlList(List<ProcessStatusTO> stsDtlList)
	{
		this.stsDtlList = stsDtlList;
	}

	/**
	 * @return the stsDtlList
	 */
	public List<ProcessStatusTO> getStsDtlListt()
	{
		return stsDtlList;
	}

}

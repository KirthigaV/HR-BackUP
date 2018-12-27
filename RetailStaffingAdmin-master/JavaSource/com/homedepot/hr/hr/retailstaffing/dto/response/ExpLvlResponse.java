/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ExpLvlResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ExperienceLevelTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for States List Drop Down
 * 
 * @author TCS
 * 
 */
@XStreamAlias("ExpLevelList")
public class ExpLvlResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("ExpLevelDetail")
	private List<ExperienceLevelTO> expLvlList;

	/**
	 * @param stateDtlList
	 *            the stateDtlList to set
	 */
	public void setExpLvlList(List<ExperienceLevelTO> expLvlList)
	{
		this.expLvlList = expLvlList;
	}

	/**
	 * @return the stateDtlList
	 */
	public List<ExperienceLevelTO> getExpLvlList()
	{
		return expLvlList;
	}

}

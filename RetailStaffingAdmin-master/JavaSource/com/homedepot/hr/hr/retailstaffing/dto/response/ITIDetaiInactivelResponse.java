/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ITIDetailResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Phone Screen Search
 * 
 * @author TCS
 * 
 */
@XStreamAlias("ITIDetailListInactive")
public class ITIDetaiInactivelResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("ITIDetail")
	private List<PhoneScreenIntrwDetailsTO> itiDtlList;

	/**
	 * @param reqDtlList
	 *            the reqDtlList to set
	 */
	public void setITIDtlList(List<PhoneScreenIntrwDetailsTO> itiDtlList)
	{
		this.itiDtlList = itiDtlList;
	}

	/**
	 * @return the reqDtlList
	 */
	public List<PhoneScreenIntrwDetailsTO> getITIDtlList()
	{
		return itiDtlList;
	}

}

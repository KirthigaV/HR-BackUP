/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StoreDriverLicenseExemptResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for store details Search
 * 
 * @author TCS
 * 
 */
@XStreamAlias("StoreDriverLicenseExemptList")
public class StoreDriverLicenseExemptResponse implements Serializable
{
	
	private static final long serialVersionUID = -7138088639233268423L;
	
	@XStreamImplicit
	@XStreamAlias("StoreDriverLicenseExemptDetail")
	private List<StoreDetailsTO> strDtlList;

	/**
	 * @param reqDtlList
	 *            the reqDtlList to set
	 */
	public void setStrDtlList(List<StoreDetailsTO> strDtlList)
	{
		this.strDtlList = strDtlList;
	}

	/**
	 * @return the reqDtlList
	 */
	public List<StoreDetailsTO> getStrDtlList()
	{
		return strDtlList;
	}

	public void addStore(StoreDetailsTO store)
	{
		// if the store is not null
		if(store != null)
		{
			// if the list is null, initialize it
			if(strDtlList == null) { strDtlList = new ArrayList<StoreDetailsTO>(); }
			
			// add the store
			strDtlList.add(store);
		} // end if(store != null)
	}
}

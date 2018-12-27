/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: OrgUnitResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;
import com.homedepot.hr.hr.retailstaffing.dto.OrgUnitDetailsTO;
/**
 * This class is used as final response object for Organisation unit details
 * 
 * @author TCS
 * 
 */

public class OrgUnitResponse implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7373637775843700086L;
	List<OrgUnitDetailsTO> OrgUnitDetailsList;

	public List<OrgUnitDetailsTO> getOrgUnitDetailsList()
	{
		return OrgUnitDetailsList;
	}

	public void setOrgUnitDetailsList(List<OrgUnitDetailsTO> orgUnitDetailsList)
	{
		OrgUnitDetailsList = orgUnitDetailsList;
	}

}

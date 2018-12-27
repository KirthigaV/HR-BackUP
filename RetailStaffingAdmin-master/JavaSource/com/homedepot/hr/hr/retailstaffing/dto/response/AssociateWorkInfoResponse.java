/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: AssociateWorkInfoResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.AssociateWorkInfoTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for associate work info response
 * 
 * @author
 * 
 */
@XStreamAlias("AssociateInfoList")
public class AssociateWorkInfoResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("AssociateInfo")
	private List<AssociateWorkInfoTO> applAssociateInfoTOList;

	public List<AssociateWorkInfoTO> getApplAssociateInfoTOList() {
		return applAssociateInfoTOList;
	}

	public void setApplAssociateInfoTOList(
			List<AssociateWorkInfoTO> applAssociateInfoTOList) {
		this.applAssociateInfoTOList = applAssociateInfoTOList;
	}
	
}
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: AssociateResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.AssociateTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for associate response
 * 
 * @author TCS
 * 
 */
@XStreamAlias("AssociateList")
public class AssociateResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("Associate")
	private List<AssociateTO> applAssociateTOList;

	public List<AssociateTO> getApplAssociateTOList() {
		return applAssociateTOList;
	}

	public void setApplAssociateTOList(List<AssociateTO> applAssociateTOList) {
		this.applAssociateTOList = applAssociateTOList;
	}

	
	
}
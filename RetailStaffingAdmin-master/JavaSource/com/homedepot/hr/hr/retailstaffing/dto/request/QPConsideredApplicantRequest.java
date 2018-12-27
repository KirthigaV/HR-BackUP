/*
 * Created on January 5, 2013
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: CreateRejectionRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.QPConsideredApplicantDetailTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create QPConsideredApplicantDetailTO for Input XML
 * 
 * @author MTS1876
 * 
 */

@XStreamAlias("ApplicantsConsideredInQPRequest")
public class QPConsideredApplicantRequest implements Serializable {

	private static final long serialVersionUID = -1011657395233387466L;

	@XStreamAlias("applicantList")
	private List<QPConsideredApplicantDetailTO> applicantList;

	public List<QPConsideredApplicantDetailTO> getApplicantList() {
		return applicantList;
	}

	public void setApplicantList(List<QPConsideredApplicantDetailTO> applicantList) {
		this.applicantList = applicantList;
	}

}

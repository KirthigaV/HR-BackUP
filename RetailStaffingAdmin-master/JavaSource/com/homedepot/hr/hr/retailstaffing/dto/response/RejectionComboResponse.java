/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: NoInterviewReasonResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This is used as the final response object for Interview No Reason.
 * 
 * @author TCS
 * 
 * 
 */
@XStreamAlias("RejectionReasonList")
public class RejectionComboResponse implements Serializable
{
	
	private static final long serialVersionUID = -686737634528638586L;
	
	@XStreamImplicit
	@XStreamAlias("option")
	private List<ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO> dtlList;

	public List<ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO> getDtlList() {
		return dtlList;
	}

	public void setRejectList(List<ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO> dtlList) {
		this.dtlList = dtlList;
	}



}

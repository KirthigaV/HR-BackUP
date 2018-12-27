/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: InterviewQuestDetailResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ApplIntervwQuestTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Interview Questions Results
 *
 * @author sxs8544
 *
 */
@XStreamAlias("InterviewQuestionsList")
public class InterviewQuestDetailResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("CandQuestionDetail")
	private List<ApplIntervwQuestTO> intrwDtlList;

	/**
	 * @param the intrwDtlList to set
	 *
	 */
	public void setIntrwDtlList(List<ApplIntervwQuestTO> intrwDtlList)
	{
		this.intrwDtlList = intrwDtlList;
	}

	/**
	 * @return the intrwDtlList
	 */
	public List<ApplIntervwQuestTO> getIntrwDtlList()
	{
		return intrwDtlList;
	}

}
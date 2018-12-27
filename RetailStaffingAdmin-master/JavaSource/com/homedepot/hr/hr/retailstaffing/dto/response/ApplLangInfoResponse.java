/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplLangInfoResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.ApplLangInfoTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for appl language
 * 
 * @author TCS
 * 
 */
@XStreamAlias("LanguagesList")
public class ApplLangInfoResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("LanguageSet")
	private List<ApplLangInfoTO> applLangInfoTOList;

	public List<ApplLangInfoTO> getApplLangInfoTOList() {
		return applLangInfoTOList;
	}

	public void setApplLangInfoTOList(List<ApplLangInfoTO> applLangInfoTOList) {
		this.applLangInfoTOList = applLangInfoTOList;
	}
}
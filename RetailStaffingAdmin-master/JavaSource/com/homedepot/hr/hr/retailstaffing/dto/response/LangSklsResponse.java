/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: LangSklsResponse.java
 * Application: RetailStaffingRequest
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.LanguageSkillsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for Languages Skills Check Boxes
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("LangSklsList")
public class LangSklsResponse implements Serializable {

	private static final long serialVersionUID = 3293062731134004432L;

	@XStreamImplicit
	@XStreamAlias("LangSklsDetail")
	private List<LanguageSkillsTO> langSklsList;

	public void setLangSklsList(List<LanguageSkillsTO> langSklsList) {
		this.langSklsList = langSklsList;
	}

	public List<LanguageSkillsTO> getLangSklsList() {
		return langSklsList;
	}

}

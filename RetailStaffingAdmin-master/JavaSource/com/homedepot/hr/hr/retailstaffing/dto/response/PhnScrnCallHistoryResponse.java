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

import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenCallHistoryTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenDispositionsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * This class is used as final response object for phone screen call history
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("phnScrnCallHistoryList")
public class PhnScrnCallHistoryResponse implements Serializable {

	private static final long serialVersionUID = 5355856743324170429L;

	@XStreamImplicit
	@XStreamAlias("PhoneScreenCallHistoryDetail")
	private List<PhoneScreenCallHistoryTO> phnScrnCallHistoryList;

	public List<PhoneScreenCallHistoryTO> getPhnScrnCallHistoryList() {
		return phnScrnCallHistoryList;
	}

	public void setPhnScrnCallHistoryList(
			List<PhoneScreenCallHistoryTO> phnScrnCallHistoryList) {
		this.phnScrnCallHistoryList = phnScrnCallHistoryList;
	}




}

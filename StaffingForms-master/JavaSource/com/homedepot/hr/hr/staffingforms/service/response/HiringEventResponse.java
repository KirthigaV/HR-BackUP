package com.homedepot.hr.hr.staffingforms.service.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: CalendarResponse.java
 * Application: RetailStaffing
 */

import com.homedepot.hr.hr.staffingforms.dto.HiringEventDetail;
import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("hiringEventResponse")
public class HiringEventResponse extends Response
{

	private static final long serialVersionUID = 6030258433621479761L;

    @XStreamAlias("status")
    private String status;
    
    @XStreamAlias("hiringEventDetail")
    private HiringEventDetail hiringEventDetail;
        

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public HiringEventDetail getHiringEventDetail() {
		return hiringEventDetail;
	}

	public void setHiringEventDetail(HiringEventDetail hiringEventDetail) {
		this.hiringEventDetail = hiringEventDetail;
	}
 
} // end class HiringEventResponse
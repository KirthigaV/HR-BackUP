/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ApplicantResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.HiringEventDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventMgrTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventsStoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as final response object for applicant response
 * 
 * @author
 * 
 */
@XStreamAlias("HiringEventResponse")
public class HiringEventResponse implements Serializable {
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("hiringEventMgrData")
	private HiringEventMgrTO hiringEventMgrTO;

	@XStreamAlias("hiringEventDetail")
	private HiringEventDetailTO hiringEventDetailTO;
	
	@XStreamAlias("hiringEventParticipatingStores")
	private List<HiringEventsStoreDetailsTO> participatingStores;
	
	
	@XStreamAlias("packetDateTime")
	private TimeStampTO packetDateTime;

	public HiringEventMgrTO getHiringEventMgrTO() {
		return hiringEventMgrTO;
	}

	public void setHiringEventMgrTO(HiringEventMgrTO hiringEventMgrTO) {
		this.hiringEventMgrTO = hiringEventMgrTO;
	}

	public HiringEventDetailTO getHiringEventDetailTO() {
		return hiringEventDetailTO;
	}

	public void setHiringEventDetailTO(HiringEventDetailTO hiringEventDetailTO) {
		this.hiringEventDetailTO = hiringEventDetailTO;
	}

	public List<HiringEventsStoreDetailsTO> getParticipatingStores() {
		return participatingStores;
	}

	public void setParticipatingStores(List<HiringEventsStoreDetailsTO> participatingStores) {
		this.participatingStores = participatingStores;
	}

	/**
	 * @return
	 */
	public TimeStampTO getPacketDateTime() {
		return packetDateTime;
	}

	/**
	 * @param packetDateTime
	 */
	public void setPacketDateTime(TimeStampTO packetDateTime) {
		this.packetDateTime = packetDateTime;
	}
	
}
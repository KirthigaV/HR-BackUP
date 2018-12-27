package com.homedepot.hr.hr.staffingforms.service.request;
/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: CalendarRequest.java
 */
import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.staffingforms.dto.HiringEventDetail;
import com.homedepot.hr.hr.staffingforms.dto.HiringEventModifyAvailability;
import com.homedepot.hr.hr.staffingforms.dto.Store;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("UpdateHiringEventRequest")
public class UpdateHiringEventRequest implements Serializable
{

	private static final long serialVersionUID = 8438843132597819977L;

	@XStreamAlias("hiringEventDetail")
	private HiringEventDetail hiringEventDetail;
	
	@XStreamAlias("participatingStores")
	private List<Store> storeNumbers; 
	
	@XStreamAlias("availabilityModifications")
	private List<HiringEventModifyAvailability> availabilityModifications;

	public HiringEventDetail getHiringEventDetail() {
		return hiringEventDetail;
	}

	public void setHiringEventDetail(HiringEventDetail hiringEventDetail) {
		this.hiringEventDetail = hiringEventDetail;
	}

	public List<Store> getStoreNumbers() {
		return storeNumbers;
	}

	public void setStoreNumbers(List<Store> storeNumbers) {
		this.storeNumbers = storeNumbers;
	}

	public List<HiringEventModifyAvailability> getAvailabilityModifications() {
		return availabilityModifications;
	}

	public void setAvailabilityModifications(List<HiringEventModifyAvailability> availabilityModifications) {
		this.availabilityModifications = availabilityModifications;
	}
	
} // end class CreateHiringEventRequest
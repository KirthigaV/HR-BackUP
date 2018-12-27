/*
 * Created on Feb 12, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: MinimumResponseTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Minimum Response details  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("MinimumResponseDtl")
public class MinimumResponseTO
{
	@XStreamAlias("seqNbr")
	private String seqNbr;
	@XStreamAlias("minimumResponse")
	private String minimumStatus;
	private String lastUpdateSystemUserId;
	
	/**
	 * @param seqNbr
	 *            the seqNbr to set
	 */
	public void setSeqNbr(String seqNbr)
	{
		this.seqNbr = seqNbr;
	}

	/**
	 * @return the seqNbr
	 */
	public String getSeqNbr()
	{
		return seqNbr;
	}

	/**
	 * @param minimumStatus
	 *            the minimumStatus to set
	 */
	public void setMinimumStatus(String minimumStatus)
	{
		this.minimumStatus = minimumStatus;
	}

	/**
	 * @return the minimumStatus
	 */
	public String getMinimumStatus()
	{
		return minimumStatus;
	}
    /**
     * Set Last Update System User ID which represents the user id that
     * created this entry.
     * Used during copy of Minimum Requirements.
     *  
     * @param lastUpdateSystemUserId
     */
    public void setLastUpdateSystemUserId(String lastUpdateSystemUserId) {
                    this.lastUpdateSystemUserId = lastUpdateSystemUserId;
    }
    /**
     * Set Last Update System User ID which represents the user id that
     * created this entry.
     * Used during copy of Minimum Requirements.
     * 
     * @return
     */
    public String getLastUpdateSystemUserId() {
                    return lastUpdateSystemUserId;
    }

}

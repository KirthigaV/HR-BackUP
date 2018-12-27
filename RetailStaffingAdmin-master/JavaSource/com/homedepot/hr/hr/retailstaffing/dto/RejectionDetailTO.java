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
 * File Name: RequisitionDetailTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Requisition details  response in XML format
 * 
 * @author GXN5764
 * 
 */
/**
 * @author GXN5764
 *
 */
@XStreamAlias("RejectionDetail")
public class RejectionDetailTO implements Serializable
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7575746680713548574L;
	
	@XStreamAlias("reqNbr")
	private String reqNbr;
	@XStreamAlias("candID")
	private String candID;
	@XStreamAlias("rejectionCode")
	private String rejectionCode;
	
	
	/**
	 * @return the reqNbr
	 */
	public String getReqNbr()
	{
		return reqNbr;
	}

	/**
	 * @param reqNbr
	 *            the reqNbr to set
	 */
	public void setReqNbr(String reqNbr)
	{
		this.reqNbr = reqNbr;
	}

	
	/**
	 * @return candID id of candidate
	 */
	public String getCandID()
	{
		return candID;
	}
	
	/**
	 * @param candID
	 */
	public void setCandID(String candID)
	{
		this.candID = candID;
	}
	
	/**
	 * @return
	 */
	public String getRejectionCode()
	{
		return rejectionCode;
	}
	
	/**
	 * @param rejectionCode
	 */
	public void setRejectionCode(String rejectionCode)
	{
		this.rejectionCode = rejectionCode;
	}
}

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
 * File Name: ITIUpdateRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;

import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * This class is used to create PhoneScreenIntrwDetailsTO  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("UpdatePhoneScrnDtlsRequest")
public class ITIUpdateRequest implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("insertUpdate")
	private String insertUpdate;

	@XStreamAlias("PhoneScreenIntrwDetail")
	private PhoneScreenIntrwDetailsTO phnScrnDtlTo;

	/**
	 * @param insertUpdate
	 *            the insertUpdate to set
	 */
	public void setInsertUpdate(String insertUpdate)
	{
		this.insertUpdate = insertUpdate;
	}

	/**
	 * @return the insertUpdate
	 */
	public String getInsertUpdate()
	{
		return insertUpdate;
	}

	/**
	 * @param phnScrnDtlTo
	 *            the phnScrnDtlTo to set
	 */
	public void setPhnScrnDtlTo(PhoneScreenIntrwDetailsTO phnScrnDtlTo)
	{
		this.phnScrnDtlTo = phnScrnDtlTo;
	}

	/**
	 * @return the phnScrnDtlTo
	 */
	public PhoneScreenIntrwDetailsTO getPhnScrnDtlTo()
	{
		return phnScrnDtlTo;
	}

}

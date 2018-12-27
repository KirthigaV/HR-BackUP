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
 * File Name: PagingRecordInfo.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as to send pagination id and date details response in XML
 * format
 * 
 * @author TCS
 * 
 */
public class PagingRecordInfo implements Serializable {

	private static final long serialVersionUID = -7871918441282361103L;
	@XStreamAlias("updatedDate")
	DateTO updatedDate;
	@XStreamAlias("id")
	String id;
	@XStreamAlias("updatedTimestamp")
	TimeStampTO updatedTimestamp;

	/**
	 * @return the updatedDate
	 */
	public DateTO getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate
	 *            the updatedDate to set
	 */
	public void setUpdatedDate(DateTO updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public TimeStampTO getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	public void setUpdatedTimestamp(TimeStampTO updatedTimestamp) {
		this.updatedTimestamp = updatedTimestamp;
	}

}

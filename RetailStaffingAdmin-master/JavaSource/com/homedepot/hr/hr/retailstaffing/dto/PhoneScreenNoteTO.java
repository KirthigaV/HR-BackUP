/*
 * Created on December 7, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: PhoneScreenNoteTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("question")
public class PhoneScreenNoteTO implements Serializable {

	/**
                 * 
                 */
	private static final long serialVersionUID = -33696275840035108L;

	private String lastUpdateSystemUserId;

	private Date lastUpdateTimestamp;

	private String phoneScreenNoteText;

	private Timestamp createTimestamp;

	private String createSystemUserId;

	public String getLastUpdateSystemUserId() {
		return lastUpdateSystemUserId;
	}

	public void setLastUpdateSystemUserId(String lastUpdateSystemUserId) {
		this.lastUpdateSystemUserId = lastUpdateSystemUserId;
	}

	public Date getLastUpdateTimestamp() {
		return lastUpdateTimestamp;
	}

	public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
		this.lastUpdateTimestamp = lastUpdateTimestamp;
	}

	public String getPhoneScreenNoteText() {
		return phoneScreenNoteText;
	}

	public void setPhoneScreenNoteText(String phoneScreenNoteText) {
		this.phoneScreenNoteText = phoneScreenNoteText;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getCreateSystemUserId() {
		return createSystemUserId;
	}

	public void setCreateSystemUserId(String createSystemUserId) {
		this.createSystemUserId = createSystemUserId;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

}

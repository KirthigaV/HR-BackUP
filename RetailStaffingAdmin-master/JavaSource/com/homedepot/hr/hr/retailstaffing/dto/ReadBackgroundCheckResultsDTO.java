package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Interview Status details  response in XML format
 * 
 * @author dxg8002
 * 
 */
@XStreamAlias("backgroundCheckResults")
public class ReadBackgroundCheckResultsDTO implements Serializable {
	
	private static final long serialVersionUID = 7231236452476752449L;

	@XStreamAlias("backgroundCheckId")
	private int backgroundCheckId;

	@XStreamAlias("createTimestamp")
	private Timestamp createTimestamp;

	@XStreamAlias("backgroundCheckSystemPackageId")
	private int backgroundCheckSystemPackageId;

	@XStreamAlias("backgroundCheckSystemComponentId")
	private int backgroundCheckSystemComponentId;

	@XStreamAlias("backgroundCheckSystemComponentSequenceNumber")
	private short backgroundCheckSystemComponentSequenceNumber;

	@XStreamAlias("backgroundCheckSystemComponentCompletedDate")
	private Date backgroundCheckSystemComponentCompletedDate;

	@XStreamAlias("backgroundCheckSystemAlertStatusCode")
	private short backgroundCheckSystemAlertStatusCode;

	@XStreamAlias("overrideAlertStatusCode")
	private Short overrideAlertStatusCode;

	@XStreamAlias("alertStatusOverrideDate")
	private Date alertStatusOverrideDate;

	@XStreamAlias("favorableResultEffectiveDays")
	private short favorableResultEffectiveDays;

	@XStreamAlias("unfavorableResultEffectiveDays")
	private short unfavorableResultEffectiveDays;

	@XStreamAlias("backgroundCheckTypeCode")
	private int backgroundCheckTypeCode;

	public int getBackgroundCheckId() {
		return backgroundCheckId;
	}

	public void setBackgroundCheckId(int backgroundCheckId) {
		this.backgroundCheckId = backgroundCheckId;
	}

	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Timestamp createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public int getBackgroundCheckSystemPackageId() {
		return backgroundCheckSystemPackageId;
	}

	public void setBackgroundCheckSystemPackageId(int backgroundCheckSystemPackageId) {
		this.backgroundCheckSystemPackageId = backgroundCheckSystemPackageId;
	}

	public int getBackgroundCheckSystemComponentId() {
		return backgroundCheckSystemComponentId;
	}

	public void setBackgroundCheckSystemComponentId(int backgroundCheckSystemComponentId) {
		this.backgroundCheckSystemComponentId = backgroundCheckSystemComponentId;
	}

	public short getBackgroundCheckSystemComponentSequenceNumber() {
		return backgroundCheckSystemComponentSequenceNumber;
	}

	public void setBackgroundCheckSystemComponentSequenceNumber(short backgroundCheckSystemComponentSequenceNumber) {
		this.backgroundCheckSystemComponentSequenceNumber = backgroundCheckSystemComponentSequenceNumber;
	}

	public Date getBackgroundCheckSystemComponentCompletedDate() {
		return backgroundCheckSystemComponentCompletedDate;
	}

	public void setBackgroundCheckSystemComponentCompletedDate(Date backgroundCheckSystemComponentCompletedDate) {
		this.backgroundCheckSystemComponentCompletedDate = backgroundCheckSystemComponentCompletedDate;
	}

	public short getBackgroundCheckSystemAlertStatusCode() {
		return backgroundCheckSystemAlertStatusCode;
	}

	public void setBackgroundCheckSystemAlertStatusCode(short backgroundCheckSystemAlertStatusCode) {
		this.backgroundCheckSystemAlertStatusCode = backgroundCheckSystemAlertStatusCode;
	}

	public Short getOverrideAlertStatusCode() {
		return overrideAlertStatusCode;
	}

	public void setOverrideAlertStatusCode(Short overrideAlertStatusCode) {
		this.overrideAlertStatusCode = overrideAlertStatusCode;
	}

	public Date getAlertStatusOverrideDate() {
		return alertStatusOverrideDate;
	}

	public void setAlertStatusOverrideDate(Date alertStatusOverrideDate) {
		this.alertStatusOverrideDate = alertStatusOverrideDate;
	}

	public short getFavorableResultEffectiveDays() {
		return favorableResultEffectiveDays;
	}

	public void setFavorableResultEffectiveDays(short favorableResultEffectiveDays) {
		this.favorableResultEffectiveDays = favorableResultEffectiveDays;
	}

	public short getUnfavorableResultEffectiveDays() {
		return unfavorableResultEffectiveDays;
	}

	public void setUnfavorableResultEffectiveDays(short unfavorableResultEffectiveDays) {
		this.unfavorableResultEffectiveDays = unfavorableResultEffectiveDays;
	}

	public int getBackgroundCheckTypeCode() {
		return backgroundCheckTypeCode;
	}

	public void setBackgroundCheckTypeCode(int backgroundCheckTypeCode) {
		this.backgroundCheckTypeCode = backgroundCheckTypeCode;
	}	
	

}

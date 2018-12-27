/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: PhoneScreenMinReqTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("question")
public class PhoneScreenReqSpecMinReqTO implements Serializable
{
	private static final long serialVersionUID = 3775714142083771816L;

	//Was availability defined on the requisition
    @XStreamAlias("bypassDateTime")
    private String bypassDateTime;
    
    //Weekdays Required
    @XStreamAlias("weekdaysSelected")
    private String weekdaysSelected;
    
    //Weekdays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("weekdaysSelectedId")
    private int weekdaysSelectedId;
    
    //Saturdays required
    @XStreamAlias("saturdaysSelected")
    private String saturdaysSelected;
    
    //Saturdays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD 
    @XStreamAlias("saturdaysSelectedId")
    private int saturdaysSelectedId;
    
    //Sundays required
    @XStreamAlias("sundaysSelected")
    private String sundaysSelected;
    
    //Sundays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("sundaysSelectedId")
    private int sundaysSelectedId;
    
    //Early Morning required
    @XStreamAlias("earlyMorningSelected")
    private String earlyMorningSelected;
    
    //Early Morning ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("earlyMorningSelectedId")
    private int earlyMorningSelectedId;
    
    //Morning required
    @XStreamAlias("morningSelected")
    private String morningSelected;
    
    //Morning ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("morningSelectedId")
    private int morningSelectedId;
    
    //Afternoon Required
    @XStreamAlias("afternoonSelected")
    private String afternoonSelected;
    
    //Afternoon ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("afternoonSelectedId")
    private int afternoonSelectedId;
    
    //Night Required
    @XStreamAlias("nightSelected")
    private String nightSelected;
    
    //Night ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("nightSelectedId")
    private int nightSelectedId;
    
    //Late Night required
    @XStreamAlias("lateNightSelected")
    private String lateNightSelected;
    
    //Late Night ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("lateNightSelectedId")
    private int lateNightSelectedId;
    
    //Overnight Required
    @XStreamAlias("overnightSelected")
    private String overnightSelected;
    
    //Overnight ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("overnightSelectedId")
    private int overnightSelectedId;

	/**
	 * @return the bypassDateTime
	 */
	public String getBypassDateTime() {
		return bypassDateTime;
	}

	/**
	 * @param bypassDateTime the bypassDateTime to set
	 */
	public void setBypassDateTime(String bypassDateTime) {
		this.bypassDateTime = bypassDateTime;
	}

	/**
	 * @return the weekdaysSelected
	 */
	public String getWeekdaysSelected() {
		return weekdaysSelected;
	}

	/**
	 * @param weekdaysSelected the weekdaysSelected to set
	 */
	public void setWeekdaysSelected(String weekdaysSelected) {
		this.weekdaysSelected = weekdaysSelected;
	}

	/**
	 * @return the weekdaysSelectedId
	 */
	public int getWeekdaysSelectedId() {
		return weekdaysSelectedId;
	}

	/**
	 * @param weekdaysSelectedId the weekdaysSelectedId to set
	 */
	public void setWeekdaysSelectedId(int weekdaysSelectedId) {
		this.weekdaysSelectedId = weekdaysSelectedId;
	}

	/**
	 * @return the saturdaysSelected
	 */
	public String getSaturdaysSelected() {
		return saturdaysSelected;
	}

	/**
	 * @param saturdaysSelected the saturdaysSelected to set
	 */
	public void setSaturdaysSelected(String saturdaysSelected) {
		this.saturdaysSelected = saturdaysSelected;
	}

	/**
	 * @return the saturdaysSelectedId
	 */
	public int getSaturdaysSelectedId() {
		return saturdaysSelectedId;
	}

	/**
	 * @param saturdaysSelectedId the saturdaysSelectedId to set
	 */
	public void setSaturdaysSelectedId(int saturdaysSelectedId) {
		this.saturdaysSelectedId = saturdaysSelectedId;
	}

	/**
	 * @return the sundaysSelected
	 */
	public String getSundaysSelected() {
		return sundaysSelected;
	}

	/**
	 * @param sundaysSelected the sundaysSelected to set
	 */
	public void setSundaysSelected(String sundaysSelected) {
		this.sundaysSelected = sundaysSelected;
	}

	/**
	 * @return the sundaysSelectedId
	 */
	public int getSundaysSelectedId() {
		return sundaysSelectedId;
	}

	/**
	 * @param sundaysSelectedId the sundaysSelectedId to set
	 */
	public void setSundaysSelectedId(int sundaysSelectedId) {
		this.sundaysSelectedId = sundaysSelectedId;
	}

	/**
	 * @return the earlyMorningSelected
	 */
	public String getEarlyMorningSelected() {
		return earlyMorningSelected;
	}

	/**
	 * @param earlyMorningSelected the earlyMorningSelected to set
	 */
	public void setEarlyMorningSelected(String earlyMorningSelected) {
		this.earlyMorningSelected = earlyMorningSelected;
	}

	/**
	 * @return the earlyMorningSelectedId
	 */
	public int getEarlyMorningSelectedId() {
		return earlyMorningSelectedId;
	}

	/**
	 * @param earlyMorningSelectedId the earlyMorningSelectedId to set
	 */
	public void setEarlyMorningSelectedId(int earlyMorningSelectedId) {
		this.earlyMorningSelectedId = earlyMorningSelectedId;
	}

	/**
	 * @return the morningSelected
	 */
	public String getMorningSelected() {
		return morningSelected;
	}

	/**
	 * @param morningSelected the morningSelected to set
	 */
	public void setMorningSelected(String morningSelected) {
		this.morningSelected = morningSelected;
	}

	/**
	 * @return the morningSelectedId
	 */
	public int getMorningSelectedId() {
		return morningSelectedId;
	}

	/**
	 * @param morningSelectedId the morningSelectedId to set
	 */
	public void setMorningSelectedId(int morningSelectedId) {
		this.morningSelectedId = morningSelectedId;
	}

	/**
	 * @return the afternoonSelected
	 */
	public String getAfternoonSelected() {
		return afternoonSelected;
	}

	/**
	 * @param afternoonSelected the afternoonSelected to set
	 */
	public void setAfternoonSelected(String afternoonSelected) {
		this.afternoonSelected = afternoonSelected;
	}

	/**
	 * @return the afternoonSelectedId
	 */
	public int getAfternoonSelectedId() {
		return afternoonSelectedId;
	}

	/**
	 * @param afternoonSelectedId the afternoonSelectedId to set
	 */
	public void setAfternoonSelectedId(int afternoonSelectedId) {
		this.afternoonSelectedId = afternoonSelectedId;
	}

	/**
	 * @return the nightSelected
	 */
	public String getNightSelected() {
		return nightSelected;
	}

	/**
	 * @param nightSelected the nightSelected to set
	 */
	public void setNightSelected(String nightSelected) {
		this.nightSelected = nightSelected;
	}

	/**
	 * @return the nightSelectedId
	 */
	public int getNightSelectedId() {
		return nightSelectedId;
	}

	/**
	 * @param nightSelectedId the nightSelectedId to set
	 */
	public void setNightSelectedId(int nightSelectedId) {
		this.nightSelectedId = nightSelectedId;
	}

	/**
	 * @return the lateNightSelected
	 */
	public String getLateNightSelected() {
		return lateNightSelected;
	}

	/**
	 * @param lateNightSelected the lateNightSelected to set
	 */
	public void setLateNightSelected(String lateNightSelected) {
		this.lateNightSelected = lateNightSelected;
	}

	/**
	 * @return the lateNightSelectedId
	 */
	public int getLateNightSelectedId() {
		return lateNightSelectedId;
	}

	/**
	 * @param lateNightSelectedId the lateNightSelectedId to set
	 */
	public void setLateNightSelectedId(int lateNightSelectedId) {
		this.lateNightSelectedId = lateNightSelectedId;
	}

	/**
	 * @return the overnightSelected
	 */
	public String getOvernightSelected() {
		return overnightSelected;
	}

	/**
	 * @param overnightSelected the overnightSelected to set
	 */
	public void setOvernightSelected(String overnightSelected) {
		this.overnightSelected = overnightSelected;
	}

	/**
	 * @return the overnightSelectedId
	 */
	public int getOvernightSelectedId() {
		return overnightSelectedId;
	}

	/**
	 * @param overnightSelectedId the overnightSelectedId to set
	 */
	public void setOvernightSelectedId(int overnightSelectedId) {
		this.overnightSelectedId = overnightSelectedId;
	}	
}

package com.homedepot.hr.hr.staffingforms.dto;

import java.io.Serializable;

public class GenericSchedulePref implements Serializable {

	private static final long serialVersionUID = 7286419529968962570L;

	private String weekdays;
	private String saturday;
	private String sunday;
	private String earlyAm;
	private String mornings;
	private String afternoons;
	private String nights;
	private String lateNight;
	private String overnight;
	private String reasonableAccommodationRequested;

	public String getWeekdays() {
		return weekdays;
	}
	public void setWeekdays(String weekdays) {
		this.weekdays = weekdays;
	}
	public String getSaturday() {
		return saturday;
	}
	public void setSaturday(String saturday) {
		this.saturday = saturday;
	}
	public String getSunday() {
		return sunday;
	}
	public void setSunday(String sunday) {
		this.sunday = sunday;
	}
	public String getEarlyAm() {
		return earlyAm;
	}
	public void setEarlyAm(String earlyAm) {
		this.earlyAm = earlyAm;
	}
	public String getMornings() {
		return mornings;
	}
	public void setMornings(String mornings) {
		this.mornings = mornings;
	}
	public String getAfternoons() {
		return afternoons;
	}
	public void setAfternoons(String afternoons) {
		this.afternoons = afternoons;
	}
	public String getNights() {
		return nights;
	}
	public void setNights(String nights) {
		this.nights = nights;
	}
	public String getLateNight() {
		return lateNight;
	}
	public void setLateNight(String lateNight) {
		this.lateNight = lateNight;
	}
	public String getOvernight() {
		return overnight;
	}
	public void setOvernight(String overnight) {
		this.overnight = overnight;
	}
	public String getReasonableAccommodationRequested() {
		return reasonableAccommodationRequested;
	}
	public void setReasonableAccommodationRequested(String reasonableAccommodationRequested) {
		this.reasonableAccommodationRequested = reasonableAccommodationRequested;
	}


}

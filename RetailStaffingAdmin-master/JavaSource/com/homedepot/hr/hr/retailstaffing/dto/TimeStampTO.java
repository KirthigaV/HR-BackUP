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
 * File Name: TimeStampTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Time response in XML format
 * 
 * @author TCS
 * 
 */
public class TimeStampTO implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6653480098207345384L;
	@XStreamAlias("month")
	String month;
	@XStreamAlias("day")
	String day;
	@XStreamAlias("year")
	String year;
	@XStreamAlias("hour")
	String hour;
	@XStreamAlias("minute")
	String minute;
	@XStreamAlias("second")
	String second;
	@XStreamAlias("milliSecond")
	String milliSecond;
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	@XStreamAlias("formattedDate")
	String formattedDate;
	
	@XStreamAlias("formattedTime")
	String formattedTime;
	//End - Added as part of Flex to HTML Conversion - 13 May 2015

	
	/**
	 * @return the month
	 */
	public String getMonth()
	{
		return month;
	}


	/**
	 * @param month the month to set
	 */
	public void setMonth(String month)
	{
		this.month = month;
	}


	/**
	 * @return the day
	 */
	public String getDay()
	{
		return day;
	}


	/**
	 * @param day the day to set
	 */
	public void setDay(String day)
	{
		this.day = day;
	}


	/**
	 * @return the year
	 */
	public String getYear()
	{
		return year;
	}


	/**
	 * @param year the year to set
	 */
	public void setYear(String year)
	{
		this.year = year;
	}


	/**
	 * @return the hour
	 */
	public String getHour()
	{
		return hour;
	}


	/**
	 * @param hour the hour to set
	 */
	public void setHour(String hour)
	{
		this.hour = hour;
	}


	/**
	 * @return the minute
	 */
	public String getMinute()
	{
		return minute;
	}


	/**
	 * @param minute the minute to set
	 */
	public void setMinute(String minute)
	{
		this.minute = minute;
	}


	/**
	 * @return the second
	 */
	public String getSecond()
	{
		return second;
	}


	/**
	 * @param second the second to set
	 */
	public void setSecond(String second)
	{
		this.second = second;
	}


	/**
	 * @return the milliSecond
	 */
	public String getMilliSecond()
	{
		return milliSecond;
	}


	/**
	 * @param milliSecond the milliSecond to set
	 */
	public void setMilliSecond(String milliSecond)
	{
		this.milliSecond = milliSecond;
	}


	public String toString()
	{
		return (getYear() + "/" + getMonth() + "/" + getDay() + "/" + getHour()
				+ "/" + getMinute() + "/" + getSecond() + "/" + getMilliSecond());
	}
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * @return formatted date
	 */
	public String getFormattedDate() {
		return formattedDate;
	}


	/**
	 * @param formattedDate
	 */
	public void setFormattedDate(String formattedDate) {
		this.formattedDate = formattedDate;
	}

	/**
	 * @return formatted time
	 */
	public String getFormattedTime() {
		return formattedTime;
	}

	/**
	 * @param formattedTime
	 */
	public void setFormattedTime(String formattedTime) {
		this.formattedTime = formattedTime;
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015

}

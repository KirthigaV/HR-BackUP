/*
 * Created on Nov 20, 2009
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: DateTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class DateTO implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5324353819935551681L;
	@XStreamAlias("month")
	String month;
	@XStreamAlias("day")
	String day;
	@XStreamAlias("year")
	String year;
	//Added as part of Flex to HTML Conversion - 13 May 2015
	@XStreamAlias("formattedDate")
	String formattedDate;
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


	public String toString()
	{
		return (getMonth() + "/" + getDay() + "/" + getYear());
	}

	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * @return formattedDate
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
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
	
}

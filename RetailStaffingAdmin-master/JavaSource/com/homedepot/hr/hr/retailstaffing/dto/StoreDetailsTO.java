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
 * File Name: StoreDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.homedepot.hr.hr.retailstaffing.util.DB2Trimmer;
import com.homedepot.ta.aa.dao.builder.DAOConverter;
import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Store details  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("StoreDetails")
public class StoreDetailsTO
{

	@DAOElement("HR_SYS_STR_NBR")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("strNum")
	private String strNum;

	@DAOElement("HR_SYS_STR_NM")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("strName")
	private String strName;

	@DAOElement("PHN_NBR")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("phone")
	private String phone;
	
	@DAOElement("ADDR_LINE1_TXT")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("add")
	private String add;
	
	@DAOElement("CITY_NM")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("city")
	private String city;
	
	@DAOElement("ST_CD")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("state")
	private String state;
	
	@DAOElement("LONG_ZIP_CD")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("zip")
	private String zip;
	
	@DAOElement("NAME")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("strMgr")
	private String strMgr;
	
	@XStreamAlias("dstMgr")
	private String dstMgr;
	
	@XStreamAlias("dstHrMgr")
	private String dstHrMgr;
	
	@DAOElement("HR_SYS_OGRP_NM")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("dstCode")
	private String dstCode;
	
	@DAOElement("HR_SYS_RGN_CD")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("reg")
	private String reg;
	
	@DAOElement("HR_SYS_DIV_NM")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("div")
	private String div;
	
	@DAOElement("CNTRY_CD")
	@DAOConverter(DB2Trimmer.class)
	@XStreamAlias("countryCode")
	private String countryCode;
	
	@DAOElement("OE22")
	@XStreamAlias("timeZoneCode")
	private String timeZoneCode;
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	@XStreamAlias("formattedPhone")
	private String formattedPhone;
	
	@XStreamAlias("packetDateTime")
	private TimeStampTO packetDateTime;
	//End - Added as part of Flex to HTML Conversion - 13 May 2015

	/**
	 * @param countryCode
	 *            the countryCode to set
	 */
	public void setCountryCode(String countryCode)
	{
		this.countryCode = countryCode;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode()
	{
		return countryCode;
	}

	/**
	 * @return the strNum
	 */
	public String getStrNum()
	{
		return strNum;
	}

	/**
	 * @param strNum
	 *            the strNum to set
	 */
	public void setStrNum(String strNum)
	{
		this.strNum = strNum;
	}

	/**
	 * @return the strName
	 */
	public String getStrName()
	{
		return strName;
	}

	/**
	 * @param strName
	 *            the strName to set
	 */
	public void setStrName(String strName)
	{
		this.strName = strName;
	}

	/**
	 * @return the phone
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	/**
	 * @return the add
	 */
	public String getAdd()
	{
		return add;
	}

	/**
	 * @param add
	 *            the add to set
	 */
	public void setAdd(String add)
	{
		this.add = add;
	}

	/**
	 * @return the city
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city)
	{
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(String state)
	{
		this.state = state;
	}

	/**
	 * @return the zip
	 */
	public String getZip()
	{
		return zip;
	}

	/**
	 * @param zip
	 *            the zip to set
	 */
	public void setZip(String zip)
	{
		this.zip = zip;
	}

	/**
	 * @return the strMgr
	 */
	public String getStrMgr()
	{
		return strMgr;
	}

	/**
	 * @param strMgr
	 *            the strMgr to set
	 */
	public void setStrMgr(String strMgr)
	{
		this.strMgr = strMgr;
	}

	/**
	 * @return the dstMgr
	 */
	public String getDstMgr()
	{
		return dstMgr;
	}

	/**
	 * @param dstMgr
	 *            the dstMgr to set
	 */
	public void setDstMgr(String dstMgr)
	{
		this.dstMgr = dstMgr;
	}

	/**
	 * @return the dstHrMgr
	 */
	public String getDstHrMgr()
	{
		return dstHrMgr;
	}

	/**
	 * @param dstHrMgr
	 *            the dstHrMgr to set
	 */
	public void setDstHrMgr(String dstHrMgr)
	{
		this.dstHrMgr = dstHrMgr;
	}

	/**
	 * @return the dstCode
	 */
	public String getDstCode()
	{
		return dstCode;
	}

	/**
	 * @param dstCode
	 *            the dstCode to set
	 */
	public void setDstCode(String dstCode)
	{
		this.dstCode = dstCode;
	}

	/**
	 * @return the reg
	 */
	public String getReg()
	{
		return reg;
	}

	/**
	 * @param reg
	 *            the reg to set
	 */
	public void setReg(String reg)
	{
		this.reg = reg;
	}

	/**
	 * @return the div
	 */
	public String getDiv()
	{
		return div;
	}

	/**
	 * @param div
	 *            the div to set
	 */
	public void setDiv(String div)
	{
		this.div = div;
	}

	public String getTimeZoneCode() {
		return timeZoneCode;
	}

	public void setTimeZoneCode(String timeZoneCode) {
		this.timeZoneCode = timeZoneCode;
	}
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * @return formatted phone
	 */
	public String getFormattedPhone() {
		return formattedPhone;
	}
	
	/**
	 * @param formattedPhone
	 */
	public void setFormattedPhone(String formattedPhone) {
		this.formattedPhone = formattedPhone;
	}
	
	/**
	 * @return the zoneTime
	 */
	public TimeStampTO getPacketDateTime() {
		return packetDateTime;
	}

	/**
	 * @param zoneTime the zoneTime to set
	 */
	public void setPacketDateTime(TimeStampTO zoneTime) {
		this.packetDateTime = zoneTime;
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
}

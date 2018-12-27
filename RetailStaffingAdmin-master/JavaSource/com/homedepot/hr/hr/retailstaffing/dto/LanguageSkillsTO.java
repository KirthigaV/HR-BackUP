/*
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffingRequest
 *
 * File Name: LanguageSkillsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Language Skill details  response in XML format
 * 
 * @author MTS1876
 * 
 */
@XStreamAlias("LangSklsDetail")
public class LanguageSkillsTO
{
	@XStreamAlias("langCode")
	String langCode;
	@XStreamAlias("dsplyDesc")
	String dsplyDesc;
	@XStreamAlias("shortDesc")
	String shortDesc;
	/**
	 * @return the langCode
	 */
	public String getLangCode()
	{
		return langCode;
	}
	/**
	 * @param langCode the langCode to set
	 */
	public void setLangCode(String langCode)
	{
		this.langCode = langCode;
	}
	/**
	 * @return the dsplyDesc
	 */
	public String getDsplyDesc()
	{
		return dsplyDesc;
	}
	/**
	 * @param dsplyDesc the dsplyDesc to set
	 */
	public void setDsplyDesc(String dsplyDesc)
	{
		this.dsplyDesc = dsplyDesc;
	}
	/**
	 * @return the shortDesc
	 */
	public String getShortDesc()
	{
		return shortDesc;
	}
	/**
	 * @param shortDesc the shortDesc to set
	 */
	public void setShortDesc(String shortDesc)
	{
		this.shortDesc = shortDesc;
	}

	
}

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
 * File Name: OrgUnitDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Organization status response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("orgUnitDetail")
public class OrgUnitDetailsTO
{
	@XStreamAlias("code")
	@DAOElement({"HR_SYS_DIV_CD", "HR_SYS_RGN_CD", "HR_SYS_OGRP_CD"})
	String code;
	
	@XStreamAlias("desciption")
	@DAOElement({"HR_SYS_DIV_NM", "HR_SYS_RGN_NM", "HR_SYS_OGRP_NM"})
	String desciption;
	/**
	 * @return the code
	 */
	public String getCode()
	{
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code)
	{
		this.code = code;
	}
	/**
	 * @return the desciption
	 */
	public String getDesciption()
	{
		return desciption;
	}
	/**
	 * @param desciption the desciption to set
	 */
	public void setDesciption(String desciption)
	{
		this.desciption = desciption;
	}

	

}

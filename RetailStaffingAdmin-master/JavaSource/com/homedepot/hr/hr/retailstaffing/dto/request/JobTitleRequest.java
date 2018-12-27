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
 * File Name: JobTitleRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create Job Title Request object  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("JobTitleRequest")
public class JobTitleRequest 
{

	@XStreamAlias("strNo")
	private String strNo;
	@XStreamAlias("deptNo")
	private String deptNo;

	/**
	 * @return the strNo
	 */
	public String getStrNo()
	{
		return strNo;
	}

	/**
	 * @param strNo
	 *            the strNo to set
	 */
	public void setStrNo(String strNo)
	{
		this.strNo = strNo;
	}

	/**
	 * @return the deptNo
	 */
	public String getDeptNo()
	{
		return deptNo;
	}

	/**
	 * @param deptNo
	 *            the deptNo to set
	 */
	public void setDeptNo(String deptNo)
	{
		this.deptNo = deptNo;
	}

}

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
 * File Name: SearchRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create SearchRequest object  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("SearchRequest")
public class SearchRequest implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("inputNbr")
	private String inputNbr;
	@XStreamAlias("formName")
	private String formName;

	/**
	 * @return the inputNbr
	 */
	public String getInputNbr()
	{
		return inputNbr;
	}

	/**
	 * @param inputNbr
	 *            the inputNbr to set
	 */
	public void setInputNbr(String inputNbr)
	{
		this.inputNbr = inputNbr;
	}

	/**
	 * @return the formName
	 */
	public String getFormName()
	{
		return formName;
	}

	/**
	 * @param formName
	 *            the formName to set
	 */
	public void setFormName(String formName)
	{
		this.formName = formName;
	}

}

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
 * File Name: SummaryRequest.java
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import com.homedepot.hr.hr.retailstaffing.dto.PaginationInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.PagingRecordInfo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used to create AssociatePoolRequest object  for Input XML
 * 
 * @author TCS
 * 
 */
@XStreamAlias("AssociatePoolRequest")
public class AssociatePoolRequest {

	@XStreamAlias("orgSearchCode")
	String orgSearchCode;
	@XStreamAlias("orgID")
	String orgID;
	@XStreamAlias("processSearchCode")
	String processSearchCode;
	@XStreamAlias("paginationDet")
	PaginationInfoTO paginationInfoTO;
	@XStreamAlias("firstRecord")
	PagingRecordInfo firstRecordInfo;
	@XStreamAlias("secondRecord")
	PagingRecordInfo secondRecordInfo;
	/**
	 * @return the orgSearchCode
	 */
	public String getOrgSearchCode()
	{
		return orgSearchCode;
	}
	/**
	 * @param orgSearchCode the orgSearchCode to set
	 */
	public void setOrgSearchCode(String orgSearchCode)
	{
		this.orgSearchCode = orgSearchCode;
	}
	/**
	 * @return the orgID
	 */
	public String getOrgID()
	{
		return orgID;
	}
	/**
	 * @param orgID the orgID to set
	 */
	public void setOrgID(String orgID)
	{
		this.orgID = orgID;
	}
	/**
	 * @return the processSearchCode
	 */
	public String getProcessSearchCode()
	{
		return processSearchCode;
	}
	/**
	 * @param processSearchCode the processSearchCode to set
	 */
	public void setProcessSearchCode(String processSearchCode)
	{
		this.processSearchCode = processSearchCode;
	}
	/**
	 * @return the paginationInfoTO
	 */
	public PaginationInfoTO getPaginationInfoTO()
	{
		return paginationInfoTO;
	}
	/**
	 * @param paginationInfoTO the paginationInfoTO to set
	 */
	public void setPaginationInfoTO(PaginationInfoTO paginationInfoTO)
	{
		this.paginationInfoTO = paginationInfoTO;
	}
	/**
	 * @return the firstRecordInfo
	 */
	public PagingRecordInfo getFirstRecordInfo()
	{
		return firstRecordInfo;
	}
	/**
	 * @param firstRecordInfo the firstRecordInfo to set
	 */
	public void setFirstRecordInfo(PagingRecordInfo firstRecordInfo)
	{
		this.firstRecordInfo = firstRecordInfo;
	}
	/**
	 * @return the secondRecordInfo
	 */
	public PagingRecordInfo getSecondRecordInfo()
	{
		return secondRecordInfo;
	}
	/**
	 * @param secondRecordInfo the secondRecordInfo to set
	 */
	public void setSecondRecordInfo(PagingRecordInfo secondRecordInfo)
	{
		this.secondRecordInfo = secondRecordInfo;
	}
}

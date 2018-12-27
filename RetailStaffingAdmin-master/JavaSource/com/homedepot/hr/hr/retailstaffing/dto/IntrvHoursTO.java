package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Interview Status details  response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("Status")
public class IntrvHoursTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	
	@XStreamAlias("allocatedHrs")
	private Integer allocatedHrs;

	public Integer getAllocatedHrs() {
		return allocatedHrs;
	}

	public void setAllocatedHrs(Integer allocatedHrs) {
		this.allocatedHrs = allocatedHrs;
	}
	
	
}

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
 * File Name: ComboOptionsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.util.Comparator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send Combo Box Options response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("ComboOptions")
public class ComboOptionsSortTO
{

	private int sortOrder;
		
	@XStreamAlias("label")
	private String label;
	
	@XStreamAlias("data")
	private String data;
	
	@XStreamAlias("enabled")
	private boolean enabled;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	public static Comparator SortFieldComparator = new Comparator() {
		public int compare(Object resultBean, Object anotherResultBean) {
			Integer value1 = Integer.valueOf(((ComboOptionsSortTO) resultBean).getSortOrder());
			Integer value2 = Integer.valueOf(((ComboOptionsSortTO) anotherResultBean).getSortOrder());
			return value1.compareTo(value2);

		}
	};

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


}

 package com.homedepot.hr.hr.staffingforms.service;
/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: Service.java
 */

/**
 * interface containing constants used by all RESTful web services
 */
public interface Service
{
	/** application/XML MIME type */
	public static final String APPLICATION_XML = "application/xml";
	/** application/x-www-form-urlencoded MIME type */
	public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";
	
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String TEXT_HTML = "text/html";

	/** version 1 */
	public static final int VERSION1 = 1;
	public static final int[] VERSION1_3 = new int[]{1, 2, 3};
} // end interface Service
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: Generic.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author TCS Annotation for generic Exception Handling
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Generic
{
	String action();
}

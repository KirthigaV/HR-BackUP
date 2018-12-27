/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: ExceptionTO.java
 * Application: StaffingForm
 *
 */
//This file is newly added as part of Flex to HTML Conversion - 12 May 2015
package com.homedepot.hr.hr.staffingforms.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Exception")
public class ExceptionTO {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}

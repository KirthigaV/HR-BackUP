/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: StatusSlotCountDtl.java
 * Application: StaffingForm
 *
 */
//This file is newly added as part of Flex to HTML Conversion - 12 May 2015
package com.homedepot.hr.hr.staffingforms.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("StatusSlotCountDtl")
public class StatusSlotCountDtl implements Serializable{

    
    private static final long serialVersionUID = -1699993363325875196L;
    
    @XStreamAlias("availabeCount")
    private int availabeCount;
    @XStreamAlias("reservedCount")
    private int reservedCount; 
    @XStreamAlias("bookedCount")
    private int bookedCount;
    
    public int getAvailabeCount() {
        return availabeCount;
    }
    public void setAvailabeCount(int availabeCount) {
        this.availabeCount = availabeCount;
    }
    
    public int getReservedCount() {
        return reservedCount;
    }
    public void setReservedCount(int reservedCount) {
        this.reservedCount = reservedCount;
    }
    public int getBookedCount() {
        return bookedCount;
    }
    public void setBookedCount(int bookedCount) {
        this.bookedCount = bookedCount;
    }
}

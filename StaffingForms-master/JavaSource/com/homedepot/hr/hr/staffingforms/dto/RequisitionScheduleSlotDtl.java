/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionScheduleSlotDtl.java
 * Application: StaffingForm
 *
 */
//This file is newly added as part of Flex to HTML Conversion - 12 May 2015
package com.homedepot.hr.hr.staffingforms.dto;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("requisitionScheduleSlotDtl")
public class RequisitionScheduleSlotDtl {

    @XStreamAlias("slotTime")
    private String slotTime;
    
    @XStreamAlias("requisitionSchedules")
    private List<RequisitionSchedule> reqSchList;
    
    public String getSlotTime() {
        return slotTime;
    }
    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }
    public List<RequisitionSchedule> getReqSchList() {
        return reqSchList;
    }
    public void setReqSchList(List<RequisitionSchedule> reqSchList) {
        this.reqSchList = reqSchList;
    }
    
    
}

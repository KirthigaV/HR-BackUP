package com.homedepot.hr.hr.retailstaffing.services.response;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: InterviewResponse.java
 * Application: RetailStaffing
 */
import com.thoughtworks.xstream.annotations.XStreamAlias;

// TODO : MOVE ALL INTERVIEW SERVICE RESPONSES INTO THIS CLASS. THIS REQUIRES UI CHANGES

/**
 * Response object that will be populated and returned whenever a call to the InterviewService
 * is invoked. Using the XStream API to marshal this object into an XML will produce the following
 * format:<br>
 * <br>
 * <code>
 * &lt;interviewResponse&gt;<br>
 * &nbsp;&lt;error&gt;&lt;/error&gt;<br>
 * &nbsp;&lt;requisitionCalendarId&gt;&nbsp;&lt;/requisitionCalendarId&gt;<br>
 * &lt;/interviewResponse&gt;<br>
 * </code>
 * <br>
 * Only non-null member variables will be present in the XML generated by the XStream API.<br>
 * <br>
 * @see Response
 */
@XStreamAlias("interviewResponse")
public class InterviewResponse extends Response
{
    private static final long serialVersionUID = 3846162847272479497L;
    
    /**
     * requisition calendar id, populated whenever a requisition calendar is created
     */
    @XStreamAlias("requisitionCalendarId")
    private Integer mRequisitionCalendarId;
    
    /**
     * Get the requisition calendar id
     * 
     * @return the requisition calendar id
     */
    public Integer getRequisitionCalendarId()
    {
    	return mRequisitionCalendarId;
    } // end function getRequisitionCalendarId()
    
    /**
     * Set the requisition calendar id
     * 
     * @param requisitionCalendarId the requisition calendar id
     */
    public void setRequisitionCalendarId(Integer requisitionCalendarId)
    {
    	mRequisitionCalendarId = requisitionCalendarId;
    } // end function setRequisitionCalendarId()
} // end class InterviewResponse
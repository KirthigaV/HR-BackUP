package com.homedepot.hr.hr.staffingforms.service.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RequisitionResponse.java
 * Application: RetailStaffing
 */
import java.util.List;

import com.homedepot.hr.hr.staffingforms.dto.Requisition;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Response object that will be populated and returned whenever a call to the RequisitionService
 * is invoked. Using the XStream API to marshal this object into an XML will produce the following
 * format:<br>
 * <br>
 * <code>
 * &lt;requisitionResponse&gt;<br>
 * &nbsp;&lt;error&gt;&lt;/error&gt;<br>
 * &nbsp;&lt;requisitions&gt;&lt;/requisitions&gt;<br>
 * &lt;/requisitionResponse&gt;<br>
 * </code>
 * <br>
 * Only non-null member variables will be present in the XML generated by the XStream API.<br>
 * <br>
 * @see Requisition
 * @see Response
 */
@XStreamAlias("requisitionResponse")
public class RequisitionResponse extends Response
{
    private static final long serialVersionUID = -734650260257676208L;

    @XStreamAlias("requisitions")
    /** collection of requisition objects, populated when a call is made to the /activeRequisitionsForStore method */
    private List<Requisition> mRequisitions;
    
    /**
     * @return collection of requisitions
     */
    public List<Requisition> getRequisitions()
    {
    	return mRequisitions;
    } // end function getRequisitions()
    
    /**
     * @param requisitions collection of requisitions
     */
    public void setRequisitions(List<Requisition> requisitions)
    {
    	mRequisitions = requisitions;
    } // end function setRequisitions()
} // end class RequisitionResponse
package com.homedepot.hr.hr.retailstaffing.dto.request;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: QualifiedPoolRequest.java
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Transfer object used to store qualified pool request data. This transfer object
 * is used by methods in the QualifiedPoolService. The XStream API is used to marshal
 * and unmarshal this transfer object to and from the following XML:<br><br>
 * 
 * <code>
 * 	 &lt;qualifiedPoolRequest&gt;<br>
 *   &nbsp;&nbsp;&lt;requisition&gt;9999999999&lt;/requisition&gt;<br>
 *   &nbsp;&nbsp;&lt;jobSkillFilter&gt;1&lt;/jobSkillFilter&gt;<br>
 *   &nbsp;&nbsp;&lt;jobSkillFilter&gt;2&lt;/jobSkillFilter&gt;<br>
 *   &nbsp;&nbsp;.<br>
 *   &nbsp;&nbsp;.<br>
 *   &lt;/qualifiedPoolRequest&gt;
 * </code>
 * 
 * @author rlp05
 */
@XStreamAlias("qualifiedPoolRequest")
public class QualifiedPoolRequest implements Serializable
{
    private static final long serialVersionUID = -1728556386241249585L;
    
    // the requisition number the qualified pool is being generated for
    @XStreamAlias("requisition")
	private int mRequisitionNbr;
    
    // job skill filters that should be used to filter the qualified pool candidates
    @XStreamImplicit(itemFieldName="jobSkillFilter")    
	private List<Short> mJobSkillFilters;
	
    /**
     * @return						The requisition number the qualified pool is being generated for
     */
	public int getRequisitionNbr()
	{
		return mRequisitionNbr;
	} // end function getRequisitionNbr()
	
	/**
	 * @param requisitionNbr		The requisition number the qualified pool is being generated for
	 */
	public void setRequisitionNbr(int requisitionNbr)
	{
		mRequisitionNbr = requisitionNbr;
	} // end function setRequisitionNbr()
	
	/**
	 * @return						Job skill filters that should be used to filter the qualified pool candidates
	 */
	public List<Short> getJobSkillFilters()
	{
		return mJobSkillFilters;
	} // end function getJobSkillFilters()
	
	/** 
	 * @param filters				Job skill filters that should be used to filter the qualified pool candidates
	 */
	public void setJobSkillFilters(List<Short> filters)
	{
		mJobSkillFilters = filters;
	} // end function setJobSkillFilters()
	
	/**
	 * Add a job skill filter to the list of filters
	 * 
	 * @param filter			Job skill filter to add to the collection
	 */
	public void addJobSkillFilter(short filter)
	{
		if(mJobSkillFilters == null)
		{
			mJobSkillFilters = new ArrayList<Short>();
		} // end if(mJobSkillFilters == null)
		
		mJobSkillFilters.add(filter);
	} // end function addJobSkillFilter()
	
	/**
	 * @return		String representation of this request object
	 */
	@Override
	public String toString()
	{
		StringBuilder data = new StringBuilder(32);
		
		data.append("Qualified Pool Request\n==========\n  requisition number: ")
			.append(mRequisitionNbr);
		
		// if job skill filters were provided, add the filter data to the output
		if(mJobSkillFilters != null && !mJobSkillFilters.isEmpty())
		{
			data.append("\n  filters: ");
			
			int counter = 0;
			// iterate and add the filters , delimited (all except the last)
			while(counter < mJobSkillFilters.size() - 1)
			{
				data.append(mJobSkillFilters.get(counter++)).append(", ");
			} // end while(counter < mJobSkillFilters.size())
			
			// add the last
			data.append(mJobSkillFilters.get(counter));
		} // end if(mJobSkillFilters != null && !mJobSkillFilters.isEmpty())
		else
		{
			data.append("\n  filters: none");
		} // end else
		
		data.append("\n==========\n");
		
		return data.toString();
	} // end function toString()	
} // end class QualifiedPoolRequest
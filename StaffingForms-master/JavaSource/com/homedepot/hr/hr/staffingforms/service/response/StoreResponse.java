package com.homedepot.hr.hr.staffingforms.service.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: LocationResponse.java
 * Application: RetailStaffing
 */
import com.homedepot.hr.hr.staffingforms.dto.Store;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Response object that will be populated and returned whenever a call to the StoreService
 * is invoked. Using the XStream API to marshal this object into an XML will produce the following
 * format:<br>
 * <br>
 * <code>
 * &lt;storeResponse&gt;<br>
 * &nbsp;&lt;error&gt;&lt;/error&gt;<br>
 * &nbsp;&lt;valid&gt;&lt;/valid&gt;<br>
 * &lt;/storeResponse&gt;<br>
 * </code>
 * <br>
 * Only non-null member variables will be present in the XML generated by the XStream API.<br>
 * <br>
 * @see Response
 */
@XStreamAlias("storeResponse")
public class StoreResponse extends Response
{
    private static final long serialVersionUID = 2682096730647832227L;
    
	/** valid indicator, set whenever a call is made to the /isValidStore method or /getStoreDetails method */
	@XStreamAlias("valid")
	private boolean mValid;
	
	/** store details, set whenever a call is made to /getStoreDetails method */
	@XStreamAlias("storeDetails")
	private Store mStoreDetails;
	
	/**
	 * @return valid indicator
	 */
	public boolean isValid()
	{
		return mValid;
	} // end function isValid()
	
	/**
	 * @param valid valid indicator
	 */
	public void setValid(boolean valid)
	{
		mValid = valid;
	} // end function setValid()
	
	public Store getStoreDetails()
	{
		return mStoreDetails;
	}
	
	public void setStoreDetails(Store storeDetails)
	{
		mStoreDetails = storeDetails;
	}
} // end class StoreResponse
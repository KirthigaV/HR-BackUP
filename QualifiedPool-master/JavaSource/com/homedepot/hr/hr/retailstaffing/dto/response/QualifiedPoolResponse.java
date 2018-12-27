package com.homedepot.hr.hr.retailstaffing.dto.response;
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
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transfer object used to store qualified pool response data. This transfer object
 * is used by methods in the QualifiedPoolService. The XStream API is used to marshal
 * this transfer object to the following XML:<br><br>
 * 
 * <code>
 * 	 &lt;qualifiedPoolResponse&gt;<br>
 *   &nbsp;&nbsp;&lt;errorMsg&gt;Detailed exception message text here!&lt;/errorMsg&gt;<br>
 *   &nbsp;&nbsp;&lt;errorCode&gt;100&lt;/errorCode&gt;<br>
 *   &lt;/qualifiedPoolResponse&gt;<br>
 * </code>
 * 
 * @author rlp05
 */
@XStreamAlias("Response")
public class QualifiedPoolResponse implements Serializable
{
    private static final long serialVersionUID = -5900562995944412000L;
    
    //Tiering Value
    @XStreamAlias("tiering")
    private int tiering;

    // Details of an error that occurred during processing (should only be populated if an exception occurred) 
    @XStreamAlias("errorDetails")
    private ErrorDetails mErrorDetails;
    
    // list of candidates
    @XStreamAlias("candidates")
    private List<QualifiedCandidate> mCandidates;
    
    /**
     * @param errorDetails			Details of an error that occurred during processing
     */
    public void setErrorDetails(ErrorDetails errorDetails)
    {
    	mErrorDetails = errorDetails;
    } // end function setErrorDetails()
    
    /**
     * @return						Details of an error that occurred during processing
     */
    public ErrorDetails getErrorDetails()
    {
    	return mErrorDetails;
    } // end function getErrorDetails()
    
    /**
     * @param candidates			List of qualified candidates being returned
     */
    public void setCandidates(List<QualifiedCandidate> candidates)
    {
    	mCandidates = candidates;
    } // end function setCandidates()
    
    /**
     * @return						List of qualified candidates being returned
     */
    public List<QualifiedCandidate> getCandidates()
    {
    	return mCandidates;
    } // end function getCandidates()

	public int getTiering() {
		return tiering;
	}

	public void setTiering(int tiering) {
		this.tiering = tiering;
	}
} // end class QualifiedPoolResponse
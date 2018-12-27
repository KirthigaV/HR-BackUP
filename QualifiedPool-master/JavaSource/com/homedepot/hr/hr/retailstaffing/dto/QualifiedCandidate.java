package com.homedepot.hr.hr.retailstaffing.dto;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: QualifiedCandidate.java
 * Application: RetailStaffing
 */
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.homedepot.hr.hr.retailstaffing.util.DB2Trimmer;
import com.homedepot.hr.hr.retailstaffing.util.FullTimePartTimeProcesser;
import com.homedepot.ta.aa.dao.builder.DAOConverter;
import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Transfer object used to store qualified applicants (both internal and external).
 * The XStream API is used to marshal this transfer object into the following XML:<br><br>
 * 
 * <code>
 * 	 &lt;candidate&gt;<br>
 *   &nbsp;&nbsp;&lt;id&gt;000000000&lt;/id&gt;<br>
 *   &nbsp;&nbsp;&lt;name&gt;John Doe&lt;/name&gt;<br>
 *   &nbsp;&nbsp;&lt;store&gt;0111&lt;/store&gt;<br>
 *   &nbsp;&nbsp;&lt;department&gt;024&lt;/department&gt;<br>
 *   &nbsp;&nbsp;&lt;jobTitle&gt;SALES&lt;/jobTitle&gt;<br>
 *   &nbsp;&nbsp;&lt;type&gt;AS&lt;/type&gt;<br>
 *   &nbsp;&nbsp;&lt;availInd&gt;Y&lt;/availInd&gt;<br>
 *   &nbsp;&nbsp;&lt;emplCat&gt;FT&lt;/emplCat&gt;<br>
 *   &nbsp;&nbsp;&lt;updTs&gt;2099-12-31-00:00:00.00000&lt;/updTs&gt;<br>
 *   &nbsp;&nbsp;&lt;prefIndicator&gt;Y&lt;/prefIndicator&gt;<br>
 *   &nbsp;&nbsp;&lt;rehireEligibilityFlg&gt;Y&lt;/rehireEligibilityFlg&gt;<br>
 *   &nbsp;&nbsp;&lt;availDate&gt;1900-01-01&lt;/availDate&gt;<br>
 *   &nbsp;&nbsp;&lt;applDate&gt;1900-01-01&lt;/applDate&gt;<br>
 *   &nbsp;&nbsp;&lt;consideredFlg&gt;Y&lt;/consideredFlg&gt;<br>
 *   &lt;/candidate&gt;<br>
 * </code>
 * 
 * @author rlp05
 * @modified mts1876
 */

@XStreamAlias("candidate")
public class QualifiedCandidate implements Serializable
{
    private static final long serialVersionUID = 7725452365736013659L;
    
    // candidate identifier
    @DAOElement({"EMPLT_APLCNT_ID", "id"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("id")
    private String mId;
    
    // candidate name
    @DAOElement({"NAME", "name"})
    @DAOConverter(DB2Trimmer.class)    
    @XStreamAlias("name")
    private String mName;
    
    // location
    @DAOElement({"ORGANIZATION_1","storeNbr"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("store")
    private String mStoreNbr;
    
    // department
    @DAOElement({"ORGANIZATION_2", "deptNbr"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("department")
    private String mDeptNbr;
    
    // job title
    @DAOElement({"JOB_TITLE_ID", "jobTitleId"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("jobTitle")
    private String mJobTitleId;
    
    // the type of candidate (i.e. "AS", "AP")
    @DAOElement({"CAND_TYP_IND", "candTypInd"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("type")
    private String mCandTypInd;
    
    // availability type of the candidate (i.e. "R", "Y")
    @DAOElement({"CAND_AVAIL_IND", "candAvailInd", "FT_PT_IND"})
    @DAOConverter(FullTimePartTimeProcesser.class)
    @XStreamAlias("availInd")
    private String mCandAvailInd;
   
    // candidate employment category (i.e. "FT", "PT", "TP", "FS")
    @DAOElement({"EMPLOYMENT_CAT", "emplCat"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("emplCat")
    private String mEmplCat;
    
    // Date the user last updated their application
    @DAOElement({"CRT_TS", "updTs", "AVAIL_DT"})
    @XStreamAlias("updTs")
    private Date mUpdTs;
    
    // Indicates if this candidate has preference for this exact requisition
    @XStreamAlias("prefIndicator")
    private String mPrefIndicator;
    
    // Indicates if the candidate is eligible for re-hire
    @XStreamAlias("rehireEligibilityFlg")
    private String mRehireEligFlg;
    
    // Date candidate is available to begin working
    @XStreamAlias("availDate")
    private Date mAvailDate;
    
    // Date the applicant applied for a position
    @DAOElement({"EMPLT_APPL_DT", "applDate"})
    @XStreamAlias("applDate")
    private Date mApplDate;
    
    //Added for ATS, mts1876, 5/29/2013
    // Has candidate been Considered/Viewed, this is based on Req and User (i.e. "Y", "")
    @XStreamAlias("consideredFlg")
    private String mConsideredFlg;
    
    //Applicant Availability
    @XStreamAlias("availability")
    private String mAvailability;
    
    //Candidate Reference Number
    @DAOElement({"EMPLT_CAND_ID", "candidateReferenceNumber"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("candidateReferenceNumber")
    private String mCandidateReferenceNumber;
    
    //Associate Id
    @DAOElement({"Z_EMPLID", "associateId"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("associateId")
    private String mAssociateId;    
    
    //Current position start date
    @DAOElement({"CURR_POS_START_DT", "currentPositionStartDate"})
    private Date mCurrentPositionStartDate;    
    
    //Time in Position
    @XStreamAlias("timeInPosition")
    private String mTimeInPosition;
    
    //Last Review score
    @DAOElement({"USER_COMP_FLD_1", "reviewScore", "PERFORMANCE_DESC"})
    @DAOConverter(DB2Trimmer.class)
    @XStreamAlias("reviewScore")
    private String mReviewScore;
    
    /**
     * @return				The candidate identifier
     */
	public String getId()
    {
    	return mId;
    } // end function getId()
	
	/**
	 * @param id			The candidate identifier
	 */
	public void setId(String id)
    {
    	mId = id;
    } // end function setId()
	
	/**
	 * @return				The candidate name
	 */
	public String getName()
    {
    	return mName;
    } // end function getName()
	
	/**
	 * @param name			The candidate name
	 */
	public void setName(String name)
    {
    	mName = name;
    } // end function setName()
	
	/**
	 * @return				The location
	 */
	public String getStoreNbr()
    {
    	return mStoreNbr;
    } // end function getStoreNbr()
	
	/**
	 * @param storeNbr		The location
	 */
	public void setStoreNbr(String storeNbr)
    {
    	mStoreNbr = storeNbr;
    } // end function setStoreNbr()
	
	/**
	 * @return				The department
	 */
	public String getDeptNbr()
    {
    	return mDeptNbr;
    } // end function getDeptNbr()
	
	/**
	 * @param deptNbr		The department
	 */
	public void setDeptNbr(String deptNbr)
    {
		mDeptNbr = deptNbr;
    } // end function setDeptNbr()
	
	/**
	 * @return				The job title
	 */
	public String getJobTitleId()
    {
    	return mJobTitleId;
    } // end function getJobTitleId()
	
	/**
	 * @param jobTitleId	The job title
	 */
	public void setJobTitleId(String jobTitleId)
    {
    	mJobTitleId = jobTitleId;
    } // end function setJobTitleId()
	
	/**
	 * @return				The type of candidate
	 */
	public String getCandTypInd()
    {
    	return mCandTypInd;
    } // end function getCandTypInd()
	
	/**
	 * @param candTypInd	The type of candidate
	 */
	public void setCandTypInd(String candTypInd)
    {
    	mCandTypInd = candTypInd;
    } // end function setCandTypInd()
	
	/**
	 * @return				The availability type of candidate
	 */
	public String getCandAvailInd()
    {
    	return mCandAvailInd;
    } // end function getCandAvailInd()
	
	/**
	 * @param candAvailInd	The availability type of candidate
	 */
	public void setCandAvailInd(String candAvailInd)
    {
    	mCandAvailInd = candAvailInd;
    } // end function setCandAvailInd()
	
	/**
	 * @return				Candidate employment type
	 */
	public String getEmplCat()
    {
    	return mEmplCat;
    } // end function getEmplCat()
	
	/**
	 * @param emplCat		Candidate employment type
	 */
	public void setEmplCat(String emplCat)
    {
    	mEmplCat = emplCat;
    } // end function setEmplCat()
	
	/**
	 * @return				Date the user last updated their application
	 */
	public Date getUpdTs()
    {
    	return mUpdTs;
    } // end function getUpdTs()
	
	/**
	 * @param updTs			Date the user last updated their application
	 */
	public void setUpdTs(Date updTs)
    {
		mUpdTs = updTs;
    } // end function setUpdTs()
	
	/**
	 * @return				'Y' if this candidate has preference for this exact requisition, 'N' otherwise
	 */
	public String getPrefIndicator()
    {
    	return mPrefIndicator;
    } // end function getPrefIndicator()
	
	/**
	 * @param prefIndicator	Indicates if this candidate has preference for this exact requisition
	 */
	public void setPrefIndicator(String prefIndicator)
    {
    	mPrefIndicator = prefIndicator;
    } // end function setPrefIndicator()

	/**
	 * @return				'Y' if the candidate is eligible for re-hire, 'N' otherwise
	 */
	public String getRehireEligFlg()
	{
		return mRehireEligFlg;
	} // end function getRehireEligFlg()
	
	/**
	 * @param flag			Indicates if the candidate is eligible for re-hire
	 */
	public void setRehireEligFlg(String flag)
	{
		mRehireEligFlg = flag;
	} // end function setRehireEligFlg()
	
	/**
	 * @return				Date candidate is available to begin working
	 */
	public Date getAvailDate()
	{
		return mAvailDate;
	} // end function getAvailDate()
	
	/**
	 * @param availDt		Date candidate is available to begin working
	 */
	public void setAvailDate(Date availDt)
	{
		mAvailDate = availDt;
	} // end function setAvailDate()
	
	/**
	 * @return				Date the applicant applied for a position
	 */
	public Date getApplDate()
	{
		return mApplDate;
	} // end function getApplDate()
	
	/**
	 * @param date			Date the applicant applied for a position
	 */
	public void setApplDate(Date date)
	{
		mApplDate = date;
	} // end function setApplDate()
	
	
	public String getConsideredFlg() {
		return mConsideredFlg;
	}

	public void setConsideredFlg(String consideredFlg) {
		mConsideredFlg = consideredFlg;
	}

	public String getAvailability() {
		return mAvailability;
	}

	public void setAvailability(String availability) {
		mAvailability = availability;
	}

	public String getCandidateReferenceNumber() {
		return mCandidateReferenceNumber;
	}

	public void setCandidateReferenceNumber(String candidateReferenceNumber) {
		mCandidateReferenceNumber = candidateReferenceNumber;
	}
	
	public String getAssociateId() {
		return mAssociateId;
	}

	public void setAssociateId(String associateId) {
		mAssociateId = associateId;
	}
	
	public Date getCurrentPositionStartDate()
	{
		return mCurrentPositionStartDate;
	}
	
	public void setCurrentPositionStartDate(Date currentPositionStartDate)
	{
		mCurrentPositionStartDate = currentPositionStartDate;
	}	
	
	public String getTimeInPosition()
	{
		return mTimeInPosition;
	}
	
	public void setTimeInPosition(String timeInPosition)
	{
		mTimeInPosition = timeInPosition;
	} 	
	
	public String getReviewScore()
	{
		return mReviewScore;
	}
	
	public void setReviewScore(String reviewScore)
	{
		mReviewScore = reviewScore;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
    public int hashCode()
    {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((mId == null) ? 0 : mId.hashCode());
	    return result;
    } // end function hashCode()

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj)
    {
		if(this == obj)
		{
			return true;
		} // end if(this == obj)
		
		if(obj == null)
		{
			return false;
		} // end if(obj == null)

		if(getClass() != obj.getClass())
		{
			return false;
		} // end if(getClass() != obj.getClass())

		QualifiedCandidate other = (QualifiedCandidate)obj;
	    if(mId == null)
	    {
	    	if(other.getId() != null)
	    	{
	    		return false;
	    	} // end if(other.getId() != null)
	    } // end if(mId == null)
	    else if(!mId.equals(other.getId()))
	    {
	    	return false;
	    } // end else if(!mId.equals(other.getId()))

	    return true;
    } // end function equals	
} // end class QualifiedCandidate
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: HrRetlStffReconsialtionRequest.java
 * Application: RetailStaffing
 * 
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HrRetlStffReconsialtionRequest")
public class HrRetlStffReconsialtionRequest
{

	/**
                 * 
                 */

	@XStreamAlias("jobId")
	private String jobId;

	@XStreamAlias("contactId")
	private String contactId;
	@XStreamAlias("completionCode")
	private String completionCode;
	@XStreamAlias("interviewResponseStatus")
	private String interviewResponseStatus;
	@XStreamAlias("campgnTypInd")
	private String campaignType;
	@XStreamAlias("cmpTS")
	private String cmpTS;

	@XStreamAlias("callBegTS")
	private String callBegTS;
	@XStreamAlias("CallEndTS")
	private String CallEndTS;

	public String getJobId()
	{
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	public String getContactId()
	{
		return contactId;
	}

	public void setContactId(String contactId)
	{
		this.contactId = contactId;
	}

	public String getCompletionCode()
	{
		return completionCode;
	}

	public void setCompletionCode(String completionCode)
	{
		this.completionCode = completionCode;
	}

	public String getInterviewResponseStatus()
	{
		return interviewResponseStatus;
	}

	public void setInterviewResponseStatus(String interviewResponseStatus)
	{
		this.interviewResponseStatus = interviewResponseStatus;
	}

	public String getCampaignType()
	{
		return campaignType;
	}

	public void setCampaignType(String campaignType)
	{
		this.campaignType = campaignType;
	}

	public String getCmpTS()
	{
		return cmpTS;
	}

	public void setCmpTS(String cmpTS)
	{
		this.cmpTS = cmpTS;
	}

	public String getCallBegTS()
	{
		return callBegTS;
	}

	public void setCallBegTS(String callBegTS)
	{
		this.callBegTS = callBegTS;
	}

	public String getCallEndTS()
	{
		return CallEndTS;
	}

	public void setCallEndTS(String callEndTS)
	{
		CallEndTS = callEndTS;
	}

}

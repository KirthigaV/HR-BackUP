/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: POMRsaStatusCrossRefRequest.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.request;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("pomRsaStatusCrossRefRequest")

public class POMRsaStatusCrossRefRequest  {		
	
	@XStreamAlias("POMCmpltStatCode")
	private String completionCode; 
	
	
	@XStreamAlias("campgnTypInd")
	private String campaignType;
	
	@XStreamAlias("languageCode")
	private String languageCode;
	
	public String getCompletionCode() {
		return completionCode;
	}
	public void setCompletionCode(String completionCode) {
		this.completionCode = completionCode;
	}
	
	public String getCampaignType() {
		return campaignType;
	}
	public void setCampaignType(String campaignType) {
		this.campaignType = campaignType;
	}
	public String getLanguageCode() {
		return languageCode;
	}
	public void setLanguageCode(String languageCode) {
		languageCode = languageCode;
	}
	

}

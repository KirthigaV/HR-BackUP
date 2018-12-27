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

import com.homedepot.hr.hr.retailstaffing.dto.ErrorDetails;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Response")
public class VoltageServiceResponse implements Serializable
{

	private static final long serialVersionUID = 1648124883980426402L;

	//Encrypted/Decrypted returned data
    @XStreamAlias("outData")
    private String mOutData;

    // Details of an error that occurred during processing (should only be populated if an exception occurred) 
    @XStreamAlias("errorDetails")
    private ErrorDetails mErrorDetails;

	public String getOutData() {
		return mOutData;
	}

	public void setOutData(String mOutData) {
		this.mOutData = mOutData;
	}

	public ErrorDetails getErrorDetails() {
		return mErrorDetails;
	}

	public void setErrorDetails(ErrorDetails mErrorDetails) {
		this.mErrorDetails = mErrorDetails;
	}    

} // end class VoltageServiceResponse
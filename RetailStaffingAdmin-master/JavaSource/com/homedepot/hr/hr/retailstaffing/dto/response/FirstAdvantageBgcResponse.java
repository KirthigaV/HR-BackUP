package com.homedepot.hr.hr.retailstaffing.dto.response;

import com.homedepot.hr.hr.retailstaffing.dto.FirstAdvantageResponse;

public class FirstAdvantageBgcResponse {

	boolean success;
	String errorCode;
	String message;
	FirstAdvantageResponse firstAdvantageResponse;
	
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public FirstAdvantageResponse getFirstAdvantageResponse() {
		return firstAdvantageResponse;
	}

	public void setFirstAdvantageResponse(
			FirstAdvantageResponse firstAdvantageResponse) {
		this.firstAdvantageResponse = firstAdvantageResponse;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
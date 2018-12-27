package com.homedepot.hr.hr.retailstaffing.dto.response;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("updateFromPOMResponse")
public class UpdateFromPOMResponse {

	@XStreamAlias("updated")
	public String updated;
	
	/** error */
	@XStreamAlias("error")
	public ErrorTO  error;

	/**
	 * @return the error
	 */
	public ErrorTO getError() {
		return error;
	}

	/**
	 * @param error the error to set
	 */
	public void setError(ErrorTO error) {
		this.error = error;
	}

	/**
	 * @return the updated
	 */
	public String getUpdated() {
		return updated;
	}

	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	
}

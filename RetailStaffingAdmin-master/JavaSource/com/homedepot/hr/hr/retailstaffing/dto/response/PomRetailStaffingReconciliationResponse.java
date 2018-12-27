package com.homedepot.hr.hr.retailstaffing.dto.response;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("pomRetailStaffingReconciliationResponse")
public class PomRetailStaffingReconciliationResponse{

	@XStreamAlias("HrRetlStffReconResult")
	public String HrRetlStffReconResult;
	
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
	 * @return the hrRetlStffReconResult
	 */
	public String getHrRetlStffReconResult() {
		return HrRetlStffReconResult;
	}

	/**
	 * @param hrRetlStffReconResult the hrRetlStffReconResult to set
	 */
	public void setHrRetlStffReconResult(String hrRetlStffReconResult) {
		HrRetlStffReconResult = hrRetlStffReconResult;
	}
}

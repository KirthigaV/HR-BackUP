/*
 *
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: ReadThdStoreEmploymentRequisitionDTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class ReadThdStoreEmploymentRequisitionDTO {

		//Instance variable for employmentRequisitionNumber
		private int employmentRequisitionNumber;

		//Instance variable for lastUpdateSystemUserId
		private String lastUpdateSystemUserId;

		//Instance variable for lastUpdateTimestamp
		private Timestamp lastUpdateTimestamp;

		//Instance variable for hireManagerName
		private String hireManagerName;

		//Instance variable for jobTitleDescription
		private String jobTitleDescription;

		//Instance variable for hireManagerPhoneNumber
		private String hireManagerPhoneNumber;

		//Instance variable for requestNumber
		private String requestNumber;

		//Instance variable for targetExperienceLevelCode
		private Short targetExperienceLevelCode;

		//Instance variable for targetPayAmount
		private BigDecimal targetPayAmount;

		//Instance variable for hireEventFlag
		private Boolean hireEventFlag;

		//Instance variable for weekBeginDate
		private Date weekBeginDate;

		//Instance variable for hireManagerAvailabilityText
		private String hireManagerAvailabilityText;

		//Instance variable for requisitionStatusCode
		private Short requisitionStatusCode;

		//Instance variable for rscScheduleFlag
		private Boolean rscScheduleFlag;

		//Instance variable for applicantTemporaryFlag
		private Boolean applicantTemporaryFlag;

		//Instance variable for createTimestamp
		private Timestamp createTimestamp;

		//Instance variable for createSystemUserId
		private String createSystemUserId;

		//Instance variable for interviewCandidateCount
		private Short interviewCandidateCount;

		//Instance variable for interviewMinutes
		private Short interviewMinutes;

		//Instance variable for requisitionCalendarId
		private Integer requisitionCalendarId;

		//getter method for employmentRequisitionNumber
		public int getEmploymentRequisitionNumber() {
			return employmentRequisitionNumber;
		}

		//setter method for employmentRequisitionNumber
		public void setEmploymentRequisitionNumber(int aValue) {
			employmentRequisitionNumber = aValue;
		}

		//getter method for lastUpdateSystemUserId
		public String getLastUpdateSystemUserId() {
			return lastUpdateSystemUserId;
		}

		//setter method for lastUpdateSystemUserId
		public void setLastUpdateSystemUserId(String aValue) {
			lastUpdateSystemUserId = aValue;
		}

		//getter method for lastUpdateTimestamp
		public Timestamp getLastUpdateTimestamp() {
			return lastUpdateTimestamp;
		}

		//setter method for lastUpdateTimestamp
		public void setLastUpdateTimestamp(Timestamp aValue) {
			lastUpdateTimestamp = aValue;
		}

		//getter method for hireManagerName
		public String getHireManagerName() {
			return hireManagerName;
		}

		//setter method for hireManagerName
		public void setHireManagerName(String aValue) {
			hireManagerName = aValue;
		}

		//getter method for jobTitleDescription
		public String getJobTitleDescription() {
			return jobTitleDescription;
		}

		//setter method for jobTitleDescription
		public void setJobTitleDescription(String aValue) {
			jobTitleDescription = aValue;
		}

		//getter method for hireManagerPhoneNumber
		public String getHireManagerPhoneNumber() {
			return hireManagerPhoneNumber;
		}

		//setter method for hireManagerPhoneNumber
		public void setHireManagerPhoneNumber(String aValue) {
			hireManagerPhoneNumber = aValue;
		}

		//getter method for requestNumber
		public String getRequestNumber() {
			return requestNumber;
		}

		//setter method for requestNumber
		public void setRequestNumber(String aValue) {
			requestNumber = aValue;
		}

		//getter method for targetExperienceLevelCode
		public Short getTargetExperienceLevelCode() {
			return targetExperienceLevelCode;
		}

		//setter method for targetExperienceLevelCode
		public void setTargetExperienceLevelCode(Short aValue) {
			targetExperienceLevelCode = aValue;
		}

		//getter method for targetPayAmount
		public BigDecimal getTargetPayAmount() {
			return targetPayAmount;
		}

		//setter method for targetPayAmount
		public void setTargetPayAmount(BigDecimal aValue) {
			targetPayAmount = aValue;
		}

		//getter method for hireEventFlag
		public Boolean getHireEventFlag() {
			return hireEventFlag;
		}

		//setter method for hireEventFlag
		public void setHireEventFlag(Boolean aValue) {
			hireEventFlag = aValue;
		}

		//getter method for weekBeginDate
		public Date getWeekBeginDate() {
			return weekBeginDate;
		}

		//setter method for weekBeginDate
		public void setWeekBeginDate(Date aValue) {
			weekBeginDate = aValue;
		}

		//getter method for hireManagerAvailabilityText
		public String getHireManagerAvailabilityText() {
			return hireManagerAvailabilityText;
		}

		//setter method for hireManagerAvailabilityText
		public void setHireManagerAvailabilityText(String aValue) {
			hireManagerAvailabilityText = aValue;
		}

		//getter method for requisitionStatusCode
		public Short getRequisitionStatusCode() {
			return requisitionStatusCode;
		}

		//setter method for requisitionStatusCode
		public void setRequisitionStatusCode(Short aValue) {
			requisitionStatusCode = aValue;
		}

		//getter method for rscScheduleFlag
		public Boolean getRscScheduleFlag() {
			return rscScheduleFlag;
		}

		//setter method for rscScheduleFlag
		public void setRscScheduleFlag(Boolean aValue) {
			rscScheduleFlag = aValue;
		}

		//getter method for applicantTemporaryFlag
		public Boolean getApplicantTemporaryFlag() {
			return applicantTemporaryFlag;
		}

		//setter method for applicantTemporaryFlag
		public void setApplicantTemporaryFlag(Boolean aValue) {
			applicantTemporaryFlag = aValue;
		}

		//getter method for createTimestamp
		public Timestamp getCreateTimestamp() {
			return createTimestamp;
		}

		//setter method for createTimestamp
		public void setCreateTimestamp(Timestamp aValue) {
			createTimestamp = aValue;
		}

		//getter method for createSystemUserId
		public String getCreateSystemUserId() {
			return createSystemUserId;
		}

		//setter method for createSystemUserId
		public void setCreateSystemUserId(String aValue) {
			createSystemUserId = aValue;
		}

		//getter method for interviewCandidateCount
		public Short getInterviewCandidateCount() {
			return interviewCandidateCount;
		}

		//setter method for interviewCandidateCount
		public void setInterviewCandidateCount(Short aValue) {
			interviewCandidateCount = aValue;
		}

		//getter method for interviewMinutes
		public Short getInterviewMinutes() {
			return interviewMinutes;
		}

		//setter method for interviewMinutes
		public void setInterviewMinutes(Short aValue) {
			interviewMinutes = aValue;
		}

		//getter method for requisitionCalendarId
		public Integer getRequisitionCalendarId() {
			return requisitionCalendarId;
		}

		//setter method for requisitionCalendarId
		public void setRequisitionCalendarId(Integer aValue) {
			requisitionCalendarId = aValue;
		}
	

}

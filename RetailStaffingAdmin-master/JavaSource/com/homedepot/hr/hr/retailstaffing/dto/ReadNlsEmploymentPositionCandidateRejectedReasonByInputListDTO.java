package com.homedepot.hr.hr.retailstaffing.dto;

import java.sql.Timestamp;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("rejectionReason")
public  class ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO {

		//Instance variable for employmentPositionCandidateRejectReasonCode
		@XStreamAlias("employmentPositionCandidateRejectReasonCode")
		private short employmentPositionCandidateRejectReasonCode;

		//Instance variable for lastUpdateSystemUserId
		@XStreamAlias("lastUpdateSystemUserId")
		private String lastUpdateSystemUserId;

		//Instance variable for lastUpdateTimestamp
		@XStreamAlias("lastUpdateTimestamp")
		private Timestamp lastUpdateTimestamp;

		//Instance variable for displayReasonCode
		@XStreamAlias("displayReasonCode")
		private String displayReasonCode;

		//Instance variable for shortReasonDescription
		@XStreamAlias("shortReasonDescription")
		private String shortReasonDescription;

		//Instance variable for reasonDescription
		@XStreamAlias("reasonDescription")
		private String reasonDescription;

		//getter method for employmentPositionCandidateRejectReasonCode
		public short getEmploymentPositionCandidateRejectReasonCode() {
			return employmentPositionCandidateRejectReasonCode;
		}

		//setter method for employmentPositionCandidateRejectReasonCode
		public void setEmploymentPositionCandidateRejectReasonCode(short aValue) {
			employmentPositionCandidateRejectReasonCode = aValue;
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

		//getter method for displayReasonCode
		public String getDisplayReasonCode() {
			return displayReasonCode;
		}

		//setter method for displayReasonCode
		public void setDisplayReasonCode(String aValue) {
			displayReasonCode = aValue;
		}

		//getter method for shortReasonDescription
		public String getShortReasonDescription() {
			return shortReasonDescription;
		}

		//setter method for shortReasonDescription
		public void setShortReasonDescription(String aValue) {
			shortReasonDescription = aValue;
		}

		//getter method for reasonDescription
		public String getReasonDescription() {
			return reasonDescription;
		}

		//setter method for reasonDescription
		public void setReasonDescription(String aValue) {
			reasonDescription = aValue;
		}
	}
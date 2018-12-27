package com.homedepot.hr.hr.retailstaffing.dto.response;

public class InitiateDrugTestActionTO {
	
		  Boolean success;
		  String errorCode;
		  String message;
		  String orderNumber;
		  String emailaddress;
		  String requesterEmailAddress;
		  String orderInitiatedTs;
		  
		public Boolean getSuccess() {
			return success;
		}
		public void setSuccess(Boolean success) {
			this.success = success;
		}
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
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public String getEmailAddress() {
			return emailaddress;
		}
		public void setEmailaddress(String emailaddress) {
			this.emailaddress = emailaddress;
		}
		public String getRequesterEmailAddress() {
			return requesterEmailAddress;
		}
		public void setRequesterEmailAddress(String requesterEmailAddress) {
			this.requesterEmailAddress = requesterEmailAddress;
		}
		public String getOrderInitiatedTs() {
			return orderInitiatedTs;
		}
		public void setOrderInitiatedTs(String orderInitiatedTs) {
			this.orderInitiatedTs = orderInitiatedTs;
		}

		private int ReturnCode;
		
		public int getReturnCode() {
			// TODO Auto-generated method stub
			return 0;
		}
		public void setReturnCode(int returnCode) {
			ReturnCode = returnCode;
		}
}
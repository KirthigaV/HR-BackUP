/* 
* This program is proprietary to The Home Depot and is not to be
* reproduced, used, or disclosed without permission of:
*    
*  The Home Depot
*  2455 Paces Ferry Road, N.W.
*  Atlanta, GA 30339-4053
*
* File Name: RetailStaffingException.java
* Application: RetailStaffing
*
*/
package com.homedepot.hr.hr.retailstaffing.exceptions;

import java.io.Serializable;

import com.homedepot.hr.hr.retailstaffing.util.PropertyReader;
import com.homedepot.hr.hr.retailstaffing.util.Util;

/**
 * This class acts as a wrapper to all the exceptions
 * @author TCS
 *
 */
public class RetailStaffingException extends Exception implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L; 

	int errorCode;
	String className;
	String methodName;
	String technicalErrorMessage;
	String timestamp;
	String endUserMessage;
	Exception rootCauseException;
	
	public RetailStaffingException(){}
	public RetailStaffingException(int errorCode){
		this.errorCode = errorCode;
		this.className = this.getStackTrace()[0].getClassName();
		this.methodName = this.getStackTrace()[0].getMethodName();
		this.timestamp = Util.getCurrentTimestamp().toString();
		//Create the exception with message as error code message.
		PropertyReader propertyReader = PropertyReader.getInstance();
		String message = propertyReader.getProperty(Integer.toString(errorCode));
		Exception e = new Exception(message);
		rootCauseException = e;
	}
	
	public RetailStaffingException(String message){
		this.className = this.getStackTrace()[0].getClassName();
		this.methodName = this.getStackTrace()[0].getMethodName();
		this.timestamp = Util.getCurrentTimestamp().toString();
		this.technicalErrorMessage = message;
		//Create the exception with message as error code message.
		Exception e = new Exception(message);
		rootCauseException = e;
	}
	
	public RetailStaffingException(int errorCode,String message){
		this.errorCode = errorCode;
		this.className = this.getStackTrace()[0].getClassName();
		this.methodName = this.getStackTrace()[0].getMethodName();
		this.timestamp = Util.getCurrentTimestamp().toString();
		this.endUserMessage = message;
		//Create the exception with message as error code message.
		Exception e = null;
		PropertyReader propertyReader = null;
		if(message==null)
		{
			propertyReader = PropertyReader.getInstance();
			e = new Exception(propertyReader.getProperty(Integer.toString(errorCode)));
			rootCauseException = e;
		}
		else
		{
			propertyReader = PropertyReader.getInstance();
			e = new Exception(propertyReader.getProperty(Integer.toString(errorCode))+message);
			this.technicalErrorMessage = message;
			rootCauseException = e;
		}
		rootCauseException = e;
	}
	public RetailStaffingException(int errorCode,Exception e){
		this.errorCode = errorCode;
		this.className = this.getStackTrace()[0].getClassName();
		this.methodName = this.getStackTrace()[0].getMethodName();
		this.timestamp = Util.getCurrentTimestamp().toString();
		this.rootCauseException = e;
		PropertyReader propertyReader = PropertyReader.getInstance();
		this.technicalErrorMessage = propertyReader.getProperty(Integer.toString(errorCode));
	}

	public RetailStaffingException(String message,Exception e){
		this.className = this.getStackTrace()[0].getClassName();
		this.methodName = this.getStackTrace()[0].getMethodName();
		this.timestamp = Util.getCurrentTimestamp().toString();
		this.technicalErrorMessage = message;
		this.rootCauseException = e;
		
	}
	
	public RetailStaffingException(int errorCode,String message,Exception e){
		this.errorCode = errorCode;
		this.className = this.getStackTrace()[0].getClassName();
		this.methodName = this.getStackTrace()[0].getMethodName();
		this.timestamp = Util.getCurrentTimestamp().toString();
		this.endUserMessage = message;
		PropertyReader propertyReader = PropertyReader.getInstance();
		if(message!=null)
		{
		this.technicalErrorMessage = propertyReader.getProperty(Integer.toString(errorCode))+message;
		}
		else
		{
			this.technicalErrorMessage = propertyReader.getProperty(Integer.toString(errorCode));	
		}
		this.rootCauseException = e;
	}
	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}
	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return the technicalErrorMessage
	 */
	public String getTechnicalErrorMessage() {
		return technicalErrorMessage;
	}
	/**
	 * @param technicalErrorMessage the technicalErrorMessage to set
	 */
	public void setTechnicalErrorMessage(String technicalErrorMessage) {
		this.technicalErrorMessage = technicalErrorMessage;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getEndUserMessage() {
		return endUserMessage;
	}
	public void setEndUserMessage(String endUserMessage) {
		this.endUserMessage = endUserMessage;
	}
	public Exception getRootCauseException() {
		return rootCauseException;
	}
	public void setRootCauseException(Exception rootCauseException) {
		this.rootCauseException = rootCauseException;
	}
	
}

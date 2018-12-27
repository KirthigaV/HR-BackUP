/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 * 
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 */
package com.homedepot.hr.hr.staffingforms.exceptions;

import java.util.Date;
import java.util.List;

/**
 * 
 * ServiceException.java
 * 
 *
 */
public class HiringEventException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 private int severity;

	 private String errorMessage;

	 private final Exception sourceException;
	 
	 private String theMessageResource;

	 private final int errorCode;

	 private final Date timeStamp;
	 
	 private final String mDetailedResponse;
	 
	 private final List<Short> lineRefNbrList;
	 
	 private final String lineType;
	 
	 private final String errorCodeJaxb;

	/**
     *  
     */
    public HiringEventException()
    {
        super();
        this.severity = 0;
        this.errorMessage = "";
        this.theMessageResource = "";
        this.errorCode = 0;
        this.timeStamp = null;
        this.sourceException = null;
        this.mDetailedResponse = null;
        this.lineRefNbrList = null;
        this.lineType = null;
        this.errorCodeJaxb = null;
    }

    /**
	 * @return the mDetailedResponse
	 */
	public String getDetailedResponse() {
		return mDetailedResponse;
	}

	public List<Short> getLineRefNbrList() {
		return lineRefNbrList;
	}

	public String getLineType() {
		return lineType;
	}

	/**
     * @param message
     */
    public HiringEventException(String message)
    {
        super(message);
        this.severity = 0;
        this.errorMessage = "";
        this.theMessageResource = "";
        this.errorCode = 0;
        this.timeStamp = null;
        this.sourceException = null;
        this.mDetailedResponse = null;
        this.lineRefNbrList = null;
        this.lineType = null;
        this.errorCodeJaxb = null;
    }

    /**
     * @param message
     * @param cause
     */
    public HiringEventException(String message, Exception cause)
    {
        super(message, cause);
        this.severity = 0;
        this.errorMessage = "";
        this.theMessageResource = "";
        this.errorCode = 0;
        this.timeStamp = null;
        this.sourceException = null;
        this.mDetailedResponse = null;
        this.lineRefNbrList = null;
        this.lineType = null;
        this.errorCodeJaxb = null;
    }

    /**
     * @param message
     * @param cause
     */
    public HiringEventException(int severity, int errorCode, Date timestamp, String errormessage, Exception cause, String detailedErrorMessage)
    {
        super(errormessage, cause);
        this.severity = severity;
        this.errorMessage = errormessage;
        this.theMessageResource = "";
        this.errorCode = errorCode;
        this.timeStamp = timestamp;
        this.sourceException = cause;
        this.mDetailedResponse = detailedErrorMessage;
        this.lineRefNbrList = null;
        this.lineType = null;
        this.errorCodeJaxb = null;
    }
    
    /**
     * @param message
     * @param cause
     */
    public HiringEventException(int severity, int errorCode, Date timestamp, String errormessage, Exception cause, String detailedErrorMessage, 
    		List<Short> lineRefNbrList, String lineType)
    {
        super(errormessage, cause);
        this.severity = severity;
        this.errorMessage = errormessage;
        this.theMessageResource = "";
        this.errorCode = errorCode;
        this.timeStamp = timestamp;
        this.sourceException = cause;
        this.mDetailedResponse = detailedErrorMessage;
        this.lineRefNbrList = lineRefNbrList;
        this.lineType = lineType;
        this.errorCodeJaxb = null;
    }
    
    /**
     * @param cause
     */
    public HiringEventException(Exception cause)
    {
        super(cause);
        this.severity = 0;
        this.errorMessage = "";
        this.theMessageResource = "";
        this.errorCode = 0;
        this.timeStamp = null;
        this.sourceException = null;
        this.mDetailedResponse = "";
        this.lineRefNbrList = null;
        this.lineType = null;
        this.errorCodeJaxb = null;
    }
    
    /**
     * @param message
     * @param cause
     */
    public HiringEventException(String errorCodeJaxb,String errormessage, Exception cause)
    {
        super(errormessage, cause);
        this.severity = 0;
        this.errorMessage = errormessage;
        this.theMessageResource = "";
        this.errorCode = 0;
        this.timeStamp = null;
        this.sourceException = cause;
        this.mDetailedResponse = null;
        this.lineRefNbrList = null;
        this.lineType = null;
        this.errorCodeJaxb = errorCodeJaxb;
    }

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getTheMessageResource() {
		return theMessageResource;
	}

	public void setTheMessageResource(String theMessageResource) {
		this.theMessageResource = theMessageResource;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Exception getSourceException() {
		return sourceException;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public Date getTimeStamp() {
		return (Date)this.timeStamp.clone();
	}

	/**
	 * @return the errorCodeJaxb
	 */
	public String getErrorCodeJaxb() {
		return errorCodeJaxb;
	}
	
	
}

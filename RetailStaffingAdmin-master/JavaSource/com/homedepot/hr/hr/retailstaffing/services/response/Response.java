package com.homedepot.hr.hr.retailstaffing.services.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: Response.java
 * Application: RetailStaffing
 */
import java.io.IOException;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.enumerations.ErrorType;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.interfaces.XMLConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Generic response object that should be the base for all web service responses. If
 * exception details are set, the XStream API will generate the following error XML elements
 * for any object extending this one<br>
 * 
 * <code><br>
 * &lt;error&gt;<br>
 * &nbsp;&nbsp;&lt;errorType&gt;&lt;/errorType&gt;<br>
 * &nbsp;&nbsp;&lt;codeIdentifier&gt;&lt;/codeIdentifier&gt;<br>
 * &nbsp;&nbsp;&lt;cause&gt;&lt;/cause&gt;<br>
 * &lt;/error&gt;
 * </code>
 */
public class Response implements Serializable
{
    private static final long serialVersionUID = -8933923516651572720L;
    
    /** Error object, should be populated whenever an exception occurs */
	@XStreamAlias("error")
	private ErrorDetails mError;
	
	/**
	 * Convenience method to populate error information using the details provided
	 * 
	 * @param errorType type of error
	 * @param appObject application object the error occurred querying/processing/etc.
	 * @param cause cause (containing stack details)
	 */
	public void setErrorDetails(ErrorType errorType, ApplicationObject appObject, Throwable cause)
	{
		setErrorType(errorType);
		setComponentIdentifier(appObject);
		setCause(cause);
	} // end function setErrorDetails()
	
	/**
	 * Convenience method to populate error information using the details provided
	 * 
	 * @param errorType type of error
	 * @param inputField input field the error occurred on (most likely validating)
	 * @param cause cause (containing stack details)
	 */
	public void setErrorDetails(ErrorType errorType, InputField inputField, Throwable cause)
	{
		setErrorType(errorType);
		setComponentIdentifier(inputField);
		setCause(cause);
	} // end function setErrorDetails()
	
	/**
	 * @return the error object
	 */
	public ErrorDetails getError()
	{
		return mError;
	} // end function getError()

	/**
	 * @param error the error object
	 */
	public void setError(ErrorDetails error)
	{
		mError = error;
	} // end function setError()
	
	/**
	 * @return the code identifying the type of error
	 */
	public String getErrorType()
	{
		return (mError == null ? null : mError.getErrorType());
	} // end function getErrorType()

	/**
	 * @param errorType the code identifying the type of error
	 */
	public void setErrorType(String errorType)
	{
		if(mError == null)
		{
			mError = new ErrorDetails();
		} // end if(mError == null)
		
		mError.setErrorType(errorType);
	} // end function setErrorType()
	
	/**
	 * @param type ErrorType enumerated type
	 */
	public void setErrorType(ErrorType errorType)
	{
		if(errorType != null)
		{
			setErrorType(errorType.getType());
		} // end if(errorType != null)
	} // end function setErrorType()

	/**
	 * @return code identifying the object that the error occurred on
	 */	
	public String getComponentIdentifier()
	{
		return (mError == null ? null : mError.getComponentIdentifier());
	} // end function getComponentIdentifier()
	
	/**
	 * @param identifier code identifying the object that the error occurred on
	 */	
	public void setComponentIdentifier(String identifier)
	{
		if(mError == null)
		{
			mError = new ErrorDetails();
		} // end if(mError == null)
		
		mError.setComponentIdentifier(identifier);
	} // end function setCode()
	
	/**
	 * @param inputField InputField enumerated type. This method should be called
	 *        whenever input validation fails
	 */
	public void setComponentIdentifier(InputField inputField)
	{
		if(inputField != null)
		{
			setComponentIdentifier(inputField.getFieldIdentifier());
		} // end if(inputField != null)
	} // end function setComponentIdentifier()
	
	/**
	 * @param appObject ApplicationObject enumerated type. This method should be called
	 *        whenever NoRowsFound exceptions, QueryExceptions, etc occur.
	 */
	public void setComponentIdentifier(ApplicationObject appObject)
	{
		if(appObject != null)
		{
			setComponentIdentifier(appObject.getObjectIdentifier());
		} // end if(appObject != null)
	} // end function setComponentIdentifier()
	
	/**
	 * @return the stack trace of the error
	 */	
	public String getCause()
	{
		return (mError == null ? null : mError.getCause());
	} // end function getCause()
	
	/**
	 * @param exception the exception that occurred (where stack details are 
	 *        taken from)
	 */
	public void setCause(Throwable exception)
	{
		if(mError == null)
		{
			mError = new ErrorDetails();
		} // end if(mError == null)
		
		mError.setCause(exception);
	} // end function setCause()
	
	/**
	 * This class is used to communicate exception details to a 
	 * calling web service client. The XStream API will generate the following
	 * XML for this class <br>
	 * 
	 * <code><br>
	 * &lt;error&gt;<br>
	 * &nbsp;&nbsp;&lt;errorType&gt;1000&lt;/errorType&gt;<br>
	 * &nbsp;&nbsp;&lt;code&gt;123&lt;/code&gt;<br>
	 * &nbsp;&nbsp;&lt;cause&gt;CDATA here with error stack&lt;/cause&gt;<br>
	 * &lt;/error&gt;
	 * </code>
	 */
	@XStreamAlias("error")
	private static class ErrorDetails implements Serializable, XMLConstants
	{
        private static final long serialVersionUID = -41243471974607914L;
        
        /** Code identifying the type of error */
		@XStreamAlias("errorType")
		private String mErrorType;
		
		/**
		 * Code identifying the object that the error occurred on. For validation errors
		 * this code value identifies the field that failed validation. For database errors
		 * this code identifies the type of object that the database error occurred on. 
		 */
		@XStreamAlias("componentIdentifier")
		private String mComponentIdentifier;
		
		/** Stack Trace of the error */
		@XStreamAlias("cause")
		private String mCause;

		/**
		 * @return code identifying the type of error
		 */
		public String getErrorType()
		{
			return mErrorType;
		} // end function getErrorType()
		
		/**
		 * @param errorType code identifying the type of error
		 */
		public void setErrorType(String errorType)
		{
			mErrorType = errorType;
		} // end function setErrorType()
		
		/**
		 * @return code identifying the object that the error occurred on
		 */
		public String getComponentIdentifier()
		{
			return mComponentIdentifier;
		} // end function getComponentIdentifier()
		
		/**
		 * @param identifier code identifying the object that the error occurred on
		 */
		public void setComponentIdentifier(String identifier)
		{
			mComponentIdentifier = identifier;
		} // end function setComponentIdentifier()

		/**
		 * @return the stack trace of the error
		 */
		public String getCause()
		{
			return mCause;
		} // end function getCause()
		
		/**
		 * @param exception the exception that occurred (where stack details are 
		 *        taken from)
		 */
		public void setCause(Throwable exception)
		{
			StringWriter writer = null;
			PrintWriter errorWriter = null;
			
			if(exception != null)
			{
				try
				{
					writer = new StringWriter();
					errorWriter = new PrintWriter(writer);
					
					// append the CDATA start tag to the stream
					writer.append(CDATA_START_TAG);
					
					// print the stack to the string writer
					exception.printStackTrace(errorWriter);
					
					// append the CDATA end tag to the stream
					writer.append(CDATA_END_TAG);
					
					// set the exception details to the string just generated
					mCause = writer.toString();
				} // end try
				finally
				{
					if(errorWriter != null)
					{
						errorWriter.close();
					} // end if(errorWriter != null)
					
					if(writer != null)
					{
						try
						{
							writer.close();
						} // end try
						catch(IOException ignore)
						{
							// not much we can do here, so just ignore it
						} // end catch
					} // end if(writer != null)
				} // end finally
			} // end if(exception != null)
		} // end function setCause()
	} // end class ErrorDetails
} // end class Response
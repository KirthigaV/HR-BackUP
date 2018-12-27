package com.homedepot.hr.hr.retailstaffing.dto.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenQuestResponse.java
 * Application: RetailStaffing
 */
import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to transport the response from inserting phone screen questions into
 * the Retail Staffing Administration application. This response is used by
 * clients of the inbound phone screen service methods being developed for RSA
 * release 4.0
 */
@XStreamAlias("updatePhoneScreenReqnSpecKnockOutResponse")
public class UpdatePhoneScreenReqnSpecKnockOutResponse implements Serializable
{
	private static final long serialVersionUID = 5299248198235810051L;
        
	// XML default namespace (needed because some of the clients are using JAXB to un-marshal the XML into objects)
	@XStreamAlias("xmlns")
	private String mDefaultNamespace = "http://webapps.homedepot.com/RetailStaffing";
	// XML schema instance namespace (needed because some of the clients are using JAXB to un-marshal the XML into objects)
	@XStreamAlias("xmlns:xsi")
	private String mXmlSchemaNamespace = "http://www.w3.org/2001/XMLSchema-instance";
	// XML schema location namespace (needed because some of the clients are using JAXB to un-marshal the XML into objects)
	@XStreamAlias("xsi:schemaLocation")
	private String mSchemaLocation = "http://webapps.homedepot.com/RetailStaffing/RetailStaffingServices.xsd";
		
	// Error details object
	@XStreamAlias("error")
	private ErrorTO mError;
	
	//Confirmation Number / Phone Screen Number
	@XStreamAlias("confirmationNo")
	private String phoneScrnId;
	
	// Determined result of Requisition Specific KO Questions
	@XStreamAlias("candidatePhnScrnResult")
	private String candidatePhnScrnResult;
			
	//Is a Phone Screen required
	@XStreamAlias("phoneScreenRequired")
	private String phoneScreenRequired;
	
	//#Offers < #Open and #Candidates Sent for Interview < #Requested Interviews
	@XStreamAlias("offersInterviewsCheck")
	private String offersInterviewsCheck;
	
	//Will the RSC be scheduling Interviews
	@XStreamAlias("rscToSchedule")
	private String rscToSchedule;
	
	//Is there calendar availability to schedule an Interview.
	@XStreamAlias("calendarAvailability")
	private String calendarAvailability;
	
    public UpdatePhoneScreenReqnSpecKnockOutResponse()
    {
    } // end constructor
	
	/**
	 * Get the default namespace
	 * 
	 * @return				The default namespace
	 */
    public String getDefaultNamespace()
    {
    	return mDefaultNamespace;
    } // end function getDefaultNamespace()
    
    /**
     * Set the default namespace
     * 
     * @param namespace		The default namespace
     */
    public void setDefaultNamespace(String namespace)
    {
    	mDefaultNamespace = namespace;
    } // end function setDefaultNamespace()
    
    /**
     * Get the XML schema instance namespace
     * 
     * @return				The XML schema instance namespace
     */
    public String getXmlSchemaNamespace()
    {
    	return mXmlSchemaNamespace;
    } // end function getXmlSchemaNamespace()
    
    /**
     * Set the XML schema instance namespace
     * 
     * @param namespace		The XML schema instance namespace
     */
    public void setXmlSchemaNamespace(String namespace)
    {
    	mXmlSchemaNamespace = namespace;
    } // end function setXmlSchemaNamespace()   
    
    /**
     * Get the schema location
     * 
     * @return				The schema location
     */
    public String getSchemaLocation()
    {
    	return mSchemaLocation;
    } // end function getSchemaLocation()
    
    /**
     * Set the schema location 
     * 
     * @param schemaLoc		The schema location
     */
    public void setSchemaLocation(String schemaLoc)
    {
    	mSchemaLocation = schemaLoc;
    } // end function setSchemaLocation()
	
    /**
     * Get the error details object
     * 
     * @param error			The error details object
     */
	public void setError(ErrorTO error)
	{
		mError = error;
	} // end function setError()
	
	/**
	 * Set the error details object
	 * 
	 * @return				The error details object
	 */
	public ErrorTO getError()
	{
		return mError;
	} // end function getError()

	/**
	 * @return the candidatePhnScrnResult
	 */
	public String getCandidatePhnScrnResult() {
		return candidatePhnScrnResult;
	}

	/**
	 * @param candidatePhnScrnResult the candidatePhnScrnResult to set
	 */
	public void setCandidatePhnScrnResult(String candidatePhnScrnResult) {
		this.candidatePhnScrnResult = candidatePhnScrnResult;
	}

	/**
	 * @return the phoneScreenRequired
	 */
	public String getPhoneScreenRequired() {
		return phoneScreenRequired;
	}

	/**
	 * @param phoneScreenRequired the phoneScreenRequired to set
	 */
	public void setPhoneScreenRequired(String phoneScreenRequired) {
		this.phoneScreenRequired = phoneScreenRequired;
	}

	/**
	 * @return the offersInterviewsCheck
	 */
	public String getOffersInterviewsCheck() {
		return offersInterviewsCheck;
	}

	/**
	 * @param offersInterviewsCheck the offersInterviewsCheck to set
	 */
	public void setOffersInterviewsCheck(String offersInterviewsCheck) {
		this.offersInterviewsCheck = offersInterviewsCheck;
	}

	/**
	 * @return the rscToSchedule
	 */
	public String getRscToSchedule() {
		return rscToSchedule;
	}

	/**
	 * @param rscToSchedule the rscToSchedule to set
	 */
	public void setRscToSchedule(String rscToSchedule) {
		this.rscToSchedule = rscToSchedule;
	}

	/**
	 * @return the calendarAvailability
	 */
	public String getCalendarAvailability() {
		return calendarAvailability;
	}

	/**
	 * @param calendarAvailability the calendarAvailability to set
	 */
	public void setCalendarAvailability(String calendarAvailability) {
		this.calendarAvailability = calendarAvailability;
	}

	/**
	 * @return the phoneScrnId
	 */
	public String getPhoneScrnId() {
		return phoneScrnId;
	}

	/**
	 * @param phoneScrnId the phoneScrnId to set
	 */
	public void setPhoneScrnId(String phoneScrnId) {
		this.phoneScrnId = phoneScrnId;
	}
		
} // end class UpdatePhoneScreenReqnSpecKnockOutResponse
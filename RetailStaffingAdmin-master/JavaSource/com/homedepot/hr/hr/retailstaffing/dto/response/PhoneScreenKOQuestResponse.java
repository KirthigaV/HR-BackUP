package com.homedepot.hr.hr.retailstaffing.dto.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenKOQuestResponse.java
 * Application: RetailStaffing
 */
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenMinReqTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * Used to transport phone screen knock out question details to clients of 
 * the inbound phone screen service methods being developed for RSA release 4.0
 */
@XStreamAlias("phoneScreenKnockOutResponse")
public class PhoneScreenKOQuestResponse implements Serializable
{
    private static final long serialVersionUID = -8502456107099660323L;

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
    
	// the confirmation number (phone screen id)
    @XStreamAlias("confirmationNo")
    private String mConfirmationNbr;
    
    // list of knock out question ids that apply to the phone screen
    @XStreamAlias("questionsList")
    //private List<KnockOutQuestion> mQuestions;
    private List<PhoneScreenMinReqTO> mQuestions;

    /**
     * Default constructor
     */
    public PhoneScreenKOQuestResponse()
    {
    	mQuestions = new ArrayList<PhoneScreenMinReqTO>();
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
	 * Get the confirmation number
	 * 
	 * @return				The confirmation number
	 */
    public String getConfirmationNbr()
    {
    	return mConfirmationNbr;
    } // end function getConfirmationNbr()
    
    /**
     * Set the confirmation number
     * 
     * @param confNbr		The confirmation number
     */
    public void setConfirmationNbr(String confNbr)
    {
    	mConfirmationNbr = confNbr;
    } // end function setConfirmationNbr()
    
    /**
     * Get the list of knock out question id's that apply to the phone screen
     * 
     * @return				The list of knock out question id's that apply to the phone screen
     */
    public List<PhoneScreenMinReqTO> getQuestions()
    {
    	return mQuestions;
    } // end function getQuestions()
    
    /**
     * Add a knock out question id to the list
     * 
     * @param id			Knock out question id
     */
    public void addQuestion(String id)
    {
    	if(id != null && id.trim().length() > 0)
    	{
    		PhoneScreenMinReqTO req = new PhoneScreenMinReqTO();
    		req.setId(id);
	    	mQuestions.add(req);
    	} // end if(id != null && id.trim().length() > 0)
    } // end function addQuestion()
} // end class PhoneScreenKOQuestResponse
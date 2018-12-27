package com.homedepot.hr.hr.retailstaffing.dto.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenInboundResponse.java
 * Application: RetailStaffing
 */
import java.sql.Date;
import java.sql.Time;

import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchdIntrvwChecksTO;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to transport phone screen details to clients of the inbound
 * phone screen service methods being developed for RSA release 4.0
 */
@XStreamAlias("phoneScreenInboundResponse")
public class PhoneScreenInboundResponse implements RetailStaffingConstants
{
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

	/*
	 * Phone Screen Id, this is a String instead of an int because I want the XStream API
	 * to be able to ignore the tag. If this is an int then it will always be present with
	 * a 0 (or a valid value).
	 */
	@XStreamAlias("confirmationNo")
	private String mPhoneScreenId;
	
	//Last 6 digits of KCR#
	@XStreamAlias("accessCode")
	private String mAccessCode;
	
	// candidate details object
	@XStreamAlias("candidateDetails")
	private CandidateDetails mDetails;
	
	// candidate interview details object
	@XStreamAlias("candidateIntrwDetails")
	private IntrvwDetails mIntrvwDetails;
	
	// candidate interview location details object
	@XStreamAlias("candidateIntrwStoreDetails")
	private IntrvwLocDetails mIntrvwLocDetails;
	
	// Schedule Interviews Check
	@XStreamAlias("candidateSchdIntrwChecks")
	private SchdIntrvwChecks mSchdIntrvwChecks;
	
	/**
	 * Default Constructor
	 */
	public PhoneScreenInboundResponse()
	{
		
	} // end constructor()
	
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
	 * Get the phone screen id
	 * 
	 * @return				The phone screen id
	 */
	public String getPhoneScreenId()
	{
		return mPhoneScreenId;
	} // end function getPhoneScreenId()
	
	/**
	 * Set the phone screen id
	 * 
	 * @param phoneScreenId	The phone screen id
	 */
	public void setPhoneScreenId(String phoneScreenId)
	{
		mPhoneScreenId = phoneScreenId;
	} // end function setPhoneScreenId()

	/**
	 * Get the access code
	 * 
	 * @return				The access code
	 */
	public String getAccessCode()
	{
		return mAccessCode;
	} // end function getAccessCode()
	
	/**
	 * Set the accessCode
	 * 
	 * @param accessCode	The access code
	 */
	public void setAccessCode(String accessCode)
	{
		mAccessCode = accessCode;
	} // end function setAccessCode()
	
	/**
	 * Get the candidate details object
	 * 
	 * @return				The candidate details object
	 */
	public CandidateDetails getDetails()
	{
		return mDetails;
	} // end function getCandidateDetails()

	/**
	 * Set the candidate details object
	 * 
	 * @param details		The candidate details object
	 */
	public void setDetails(PhoneScreenIntrwDetailsTO details)
	{
		if(details != null)
		{
			/*
			 * Initialize a new candidate details object. This is a temporary waste of
			 * resources, but to stay consistent with the rest of the application the 
			 * XStream API will be used to generate the response XML. This object is needed
			 * in order to provide the correct XML format using the XStream API.
			 */
			mDetails = new CandidateDetails();
			mDetails.setId(details.getCndtNbr());
			mDetails.setFirstName(details.getFirstName());
			mDetails.setLastName(details.getLastName());
			mDetails.setPhone1(details.getCanPhn());
			mDetails.setPhone2(details.getPhone2());
			mDetails.setEmailAddr(details.getEmailAdd());
			mDetails.setTimeZone(details.getTimeZone());
			mDetails.setLangPref(details.getLangPref());
			mDetails.setSkillType(details.getSkillType());
			mDetails.setJobTitle(details.getJobTtl());
			mDetails.setAppStat(details.getCanStatus());
			mDetails.setIntrvwStat(details.getInterviewStatusDesc());
			mDetails.setPhnScrnStat(details.getPhoneScreenStatusDesc());
			mDetails.setReqStat(details.getReqStat());
			mDetails.setType(details.getInternalExternal());
			mDetails.setAssocId(details.getAid());
			mDetails.setMgrId(details.getMgrId());
			mDetails.setAccessCode(details.getAccessCode());
			
			// get the interview location details
			IntrwLocationDetailsTO intrvwDetails = details.getIntrLocDtls();
			
			 // if there are interview details for this phone screen
			if(intrvwDetails != null)
			{
				/*
				 * Initialize a new interview details object. This is a temporary waste of
				 * resources, but to stay consistent with the rest of the application the 
				 * XStream API will be used to generate the response XML. This object is needed
				 * in order to provide the correct XML format using the XStream API.
				 */
				mIntrvwDetails = new IntrvwDetails();
				
				// if an interview time exists (should for everything but phone screens in "STORE SCHEDULING" status
				if(intrvwDetails.getIntrvwDtTm() != null)
				{
					// set the interview date
					mIntrvwDetails.setIntrvwDt(intrvwDetails.getIntrvwDtTm().getTime());
					// set the interview time
					mIntrvwDetails.setIntrvwTm(intrvwDetails.getIntrvwDtTm().getTime());
				} // end if(intrvwDetails.getIntrvwDtTm() != null)
	
				/*
				 * Now initialize a new interview store details object. This is also a temporary
				 * waste of resources, see comment above.
				 */
				mIntrvwLocDetails = new IntrvwLocDetails();
				mIntrvwLocDetails.setLocNbr(intrvwDetails.getInterviewLocId());
				mIntrvwLocDetails.setLocName(intrvwDetails.getInterviewLocName());
				mIntrvwLocDetails.setAddr(intrvwDetails.getAdd());
				mIntrvwLocDetails.setCity(intrvwDetails.getCity());
				mIntrvwLocDetails.setState(intrvwDetails.getState());
				mIntrvwLocDetails.setZip(intrvwDetails.getZip());
				mIntrvwLocDetails.setPhone(intrvwDetails.getPhone());
				mIntrvwLocDetails.setTimeZone(intrvwDetails.getTimeZone());
			} // end if(intrvwDetails != null)
			
			//Get the flags for Candidates still needed and Calendar Availability
			SchdIntrvwChecksTO flagCheckDetails = details.getSchdIntrvwChecks();
			mSchdIntrvwChecks = new SchdIntrvwChecks();
			if (flagCheckDetails != null) {
				/*
				 * Initialize a new schedule interview Check object. This is a temporary waste of
				 * resources, but to stay consistent with the rest of the application the 
				 * XStream API will be used to generate the response XML. This object is needed
				 * in order to provide the correct XML format using the XStream API.
				 */	
			
				mSchdIntrvwChecks = new SchdIntrvwChecks();
				mSchdIntrvwChecks.setOffersInterviewsCheck(flagCheckDetails.getOffersInterviewsCheck());
				mSchdIntrvwChecks.setCalendarAvailability(flagCheckDetails.getCalendarAvailability());
			} // End - if (flagCheckDetails != null)	
		} // end if(details != null)
	} // end function setCandPhnScreenDetails()
	
	/**
	 * Get the candidate interview details object
	 * 
	 * @return				The candidate interview details object
	 */
	public IntrvwDetails getIntrvwDetails()
	{
		return mIntrvwDetails;
	} // end function getIntrvwDetails()
	
	/**
	 * Get the candidate interview location details object
	 * 
	 * @return				The candidate interview location details object
	 */
	public IntrvwLocDetails getIntrvwLocDetails()
	{
		return mIntrvwLocDetails;
	} // end function getIntrvwLocDetails()
	
	/**
	 * Used to transport candidate details to clients of the inbound
	 * phone screen service methods being developed for RSA release 4.0.
	 * This class is necessary to make the XStream API produce the XML
	 * that has been communicated to external users of this service.
	 */
	@XStreamAlias("candidateDetails")
	static class CandidateDetails
	{
		// id
		@XStreamAlias("candidateId")
		private String mId;
		// first name
		@XStreamAlias("candidateFirstName")
		private String mFirstName;
		// last name
		@XStreamAlias("candidateLastName")
		private String mLastName;
		// primary phone
		@XStreamAlias("candidatePhone1")
		private String mPhone1;
		// alternate phone
		@XStreamAlias("candidatePhone2")
		private String mPhone2;
		// email address
		@XStreamAlias("candidateEmail")
		private String mEmailAddr;
		// time zone
		@XStreamAlias("candidateTimeZone")
		private String mTimeZone;
		// language preference
		@XStreamAlias("candidateLanguagePref")
		private String mLangPref;
		// requisition job title skill type indicator
		@XStreamAlias("candidateSkillType")
		private String mSkillType;
		// requisition job title
		@XStreamAlias("candidateJobTitle")
		private String mJobTitle;
		// applicant status
		@XStreamAlias("candidateAppStatus")
		private String mAppStat;
		// interview status
		@XStreamAlias("candidateIntrwStatus")
		private String mIntrvwStat;
		// phone screen status
		@XStreamAlias("candidatePhoneScreenStatus")
		private String mPhnScrnStat;
		// requisition status
		@XStreamAlias("candidateReqStatus")
		private String mReqStat;
		// type of candidate (internal/external)
		@XStreamAlias("candidateType")
		private String mType;
		// associate id
		@XStreamAlias("associateId")
		private String mAssocId;
		// manager id
		@XStreamAlias("managerID")
		private String mMgrId;
		//Access Code
		@XStreamAlias("accessCode")
		private String mAccessCode;
		/**
		 * Default constructor
		 */
		public CandidateDetails()
		{
			// initialize everything to an empty string (so XStream will generate an empty tag)
			mId = EMPTY_STRING;
			mFirstName = EMPTY_STRING;
			mLastName = EMPTY_STRING;
			mPhone1 = EMPTY_STRING;
			mPhone2 = EMPTY_STRING;
			mEmailAddr = EMPTY_STRING;
			mTimeZone = EMPTY_STRING;
			mLangPref = EMPTY_STRING;
			mSkillType = EMPTY_STRING;
			mJobTitle = EMPTY_STRING;
			mAppStat = EMPTY_STRING;
			mIntrvwStat = EMPTY_STRING;
			mPhnScrnStat = EMPTY_STRING;
			mReqStat = EMPTY_STRING;
			mType = EMPTY_STRING;
			mAssocId = EMPTY_STRING;
			mMgrId = EMPTY_STRING;
			mAccessCode = EMPTY_STRING;
		} // end constructor

		/**
		 * Get the ID
		 * 
		 * @return					The ID
		 */
		public String getId()
		{
			return mId;
		} // end function getId()
		
		/**
		 * Set the id
		 * 
		 * @param id				The ID
		 */
		public void setId(String id)
		{
			if(id != null)
			{
				mId = id;
			} // end if(id != null)
		} // end function setId()
		
		/**
		 * Get the first name
		 * 
		 * @return					The first name
		 */
		public String getFirstName()
		{
			return mFirstName;
		} // end function getFirstName()
		
		/**
		 * Set the first name
		 * 
		 * @param firstName			The first name
		 */
		public void setFirstName(String firstName)
		{
			if(firstName != null)
			{
				mFirstName = firstName;
			} // end if(firstName != null)
		} // end function setFirstName()
		
		/**
		 * Get the last name
		 * 
		 * @return					The last name
		 */
		public String getLastName()
		{
			return mLastName;
		} // end function getLastName()
		
		/**
		 * Set the last name
		 * 
		 * @param lastName			The last name
		 */
		public void setLastName(String lastName)
		{
			if(lastName != null)
			{
				mLastName = lastName;
			} // end if(lastName != null)
		} // end function setLastName()
		
		/**
		 * Get the primary phone
		 * 
		 * @return					The primary phone
		 */
		public String getPhone1()
		{
			return mPhone1;
		} // end function getPhone1()
		
		/**
		 * Set the primary phone
		 * 
		 * @param phone1			The primary phone
		 */
		public void setPhone1(String phone1)
		{
			if(phone1 != null)
			{
				mPhone1 = phone1;
			} // end if(phone1 != null)
		} // end function setPhone1()
		
		/**
		 * Get the alternate phone
		 * 
		 * @return					The alternate phone
		 */
		public String getPhone2()
		{
			return mPhone2;
		} // end function getPhone2()
		
		/**
		 * Set the alternate phone
		 * 
		 * @param phone2			The alternate phone
		 */
		public void setPhone2(String phone2)
		{
			if(phone2 != null)
			{
				mPhone2 = phone2;
			} // end if(phone2 != null)
		} // end function setPhone2()
		
		/**
		 * Get the email address
		 * 
		 * @return					The email address
		 */
		public String getEmailAddr()
		{
			return mEmailAddr; 
		} // end function getEmailAddr()
		
		/**
		 * Set the email address
		 * 
		 * @param emailAddr			The email address
		 */
		public void setEmailAddr(String emailAddr)
		{
			if(emailAddr != null)
			{
				mEmailAddr = emailAddr;
			} // end if(emailAddr != null)
		} // end function setEmailAddr()
		
		/**
		 * Get the time zone
		 * 
		 * @return					The time zone
		 */
		public String getTimeZone()
		{
			return mTimeZone;
		} // end function getTimeZone()
		
		/**
		 * Set the time zone
		 * 
		 * @param timezone			The time zone
		 */
		public void setTimeZone(String timezone)
		{
			if(timezone != null)
			{
				mTimeZone = timezone;
			} // end if(timezone != null)
		} // end function setTimeZone()
		
		/**
		 * Get the language preference
		 * 
		 * @return					The language preference
		 */
		public String getLangPref()
		{
			return mLangPref;
		} // end function getLangPref()
		
		/**
		 * Set the language preference
		 * 
		 * @param langPref			The language preference
		 */
		public void setLangPref(String langPref)
		{
			if(langPref != null)
			{
				mLangPref = langPref;
			} // end if(langPref != null)
		} // end function setLangPref()
		
		/**
		 * Get the requisition job title skill type indicator
		 * 
		 * @return					The requisition job title skill type indicator
		 */
		public String getSkillType()
		{
			return mSkillType;
		} // end function getSkillType()
		
		/**
		 * Set the requisition job title skill type indicator
		 * 
		 * @param skillType			The requisition job title skill type indicator
		 */
		public void setSkillType(String skillType)
		{
			if(skillType != null)
			{
				mSkillType = skillType;
			} // end if(skillType != null)
		} // end function setSkillType()
		
		/**
		 * Get the requisition job title
		 * 
		 * @return					The requisition job title
		 */
		public String getJobTitle()
		{
			return mJobTitle;
		} // end function getJobTitle()
		
		/**
		 * Set the requisition job title
		 * 
		 * @param jobTitle			The requisition job title
		 */
		public void setJobTitle(String jobTitle)
		{
			if(jobTitle != null)
			{
				mJobTitle = jobTitle;
			} // end if(jobTitle != null)
		} // end function setJobTitle()
		
		/**
		 * Get the applicant status
		 * 
		 * @return					The applicant status
		 */
		public String getAppStat()
		{
			return mAppStat;
		} // end function getAppStat()
		
		/**
		 * Set the applicant status
		 * 
		 * @param appStat			The applicant status
		 */
		public void setAppStat(String appStat)
		{
			if(appStat != null)
			{
				mAppStat = appStat;
			} // end if(appStat != null)
		} // end function setAppStat()
		
		/**
		 * Get the interview status
		 * 
		 * @return					The interview status
		 */
		public String getIntrvwStat()
		{
			return mIntrvwStat;
		} // end function getIntrvwStat()
		
		/**
		 * Set the interview status
		 * 
		 * @param intrvwStat		The interview status
		 */
		public void setIntrvwStat(String intrvwStat)
		{
			if(intrvwStat != null)
			{
				mIntrvwStat = intrvwStat;
			} // end if(intrvwStat != null)
		} // end function setIntrvwStat()
		
		/**
		 * Get the phone screen status
		 * 
		 * @return					The phone screen status
		 */
		public String getPhnScrnStat()
		{
			return mPhnScrnStat;
		} // end function getPhnScrnStat()
		
		/**
		 * Set the phone screen status
		 * 
		 * @param phnScrnStat		The phone screen status
		 */
		public void setPhnScrnStat(String phnScrnStat)
		{
			if(phnScrnStat != null)
			{
				mPhnScrnStat = phnScrnStat;
			} // end if(phnScrnStat != null)
		} // end function setPhnScrnStat()
		
		/**
		 * Get the requisition status
		 * 
		 * @return					The requisition status
		 */
		public String getReqStat()
		{
			return mReqStat;
		} // end function getReqStat()
		
		/**
		 * Set the requisition status
		 * 
		 * @param reqStat			The requisition status
		 */
		public void setReqStat(String reqStat)
		{
			if(reqStat != null)
			{
				mReqStat = reqStat;
			} // end if(reqStat != null)
		} // end function setReqStat()
		
		/**
		 * Get the candidate type
		 * 
		 * @return					The candidate type
		 */
		public String getType()
		{
			return mType;
		} // end function getType()
		
		/**
		 * Set the candidate type
		 * 
		 * @param type				The candidate type
		 */
		public void setType(String type)
		{
			if(type != null)
			{
				mType = type;
			} // end if(type != null)
		} // end function setType()
		
		/**
		 * Get the associate id
		 * 
		 * @return					The associate id
		 */
		public String getAssocId()
		{
			return mAssocId;
		} // end function getAssocId()
		
		/**
		 * Set the associate id
		 * 
		 * @param assocId			The associate id
		 */
		public void setAssocId(String assocId)
		{
			if(assocId != null)
			{
				mAssocId = assocId;
			} // end if(assocId != null)
		} // end function setAssocId()

		/**
		 * Get the manager id
		 * 
		 * @return					The manager id
		 */
		public String getMgrId()
		{
			return mMgrId;
		} // end function getMgrId()
		
		/**
		 * Set the manager id
		 * 
		 * @param mgrId				The manager id
		 */
		public void setMgrId(String mgrId)
		{
			if(mgrId != null)
			{
				mMgrId = mgrId;
			} // end if(mgrId != null)
		} // end function setMgrId()
		
		/**
		 * Get the Access Code Id
		 * 
		 * @return					The Access Code
		 */
		public String getAccessCode()
		{
			return mAccessCode;
		} // end function getAccessCode()
		
		/**
		 * Set the Access Code
		 * 
		 * @param accessCode				The Access Code
		 */
		public void setAccessCode(String accessCode)
		{
				mAccessCode = accessCode;
		} // end function setAccessCode()		
	} // end class CandidateDetails
	
	/**
	 * Used to transport candidate interview details to clients of the inbound
	 * phone screen service methods being developed for RSA release 4.0.
	 * This class is necessary to make the XStream API produce the XML
	 * that has been communicated to external users of this service.
	 */	
	@XStreamAlias("candidateIntrwDetails")
	static class IntrvwDetails
	{
		// interview date
		@XStreamAlias("intrwDate")
		private Date mIntrvwDt;
		// interview time
		@XStreamAlias("intrwTime")
		private Time mIntrvwTm;
		
		/**
		 * Get the interview date
		 * 
		 * @return					The interview date
		 */
		public Date getIntrvwDt()
		{
			return mIntrvwDt;
		} // end function getIntrvwDt()
		
		/**
		 * Set the interview date
		 * 
		 * @param milliseconds		Millisecond value representing the interview date
		 */
		public void setIntrvwDt(long milliseconds)
		{
			mIntrvwDt = new Date(milliseconds);
		} // end function setIntrvwDt()
		
		/**
		 * Get the interview time
		 * 
		 * @return					The interview time
		 */
		public Time getIntrvwTm()
		{
			return mIntrvwTm;
		} // end function getIntrvwTm()
		
		/**
		 * Set the interview time
		 * 
		 * @param milliseconds		The interview time
		 */
		public void setIntrvwTm(long milliseconds)
		{
			mIntrvwTm = new Time(milliseconds);
		} // end function setIntrvwTm()
	} // end class IntrvwDetails

	/**
	 * Used to transport candidate interview location details to clients of 
	 * the inbound phone screen service methods being developed for RSA 
	 * release 4.0. This class is necessary to make the XStream API produce 
	 * the XML that has been communicated to external users of this service.
	 */	
	@XStreamAlias("candidateIntrwStoreDetails")
	static class IntrvwLocDetails
	{
		// location number
		@XStreamAlias("storeNo")
		private String mLocNbr;
		// location name
		@XStreamAlias("storeName")
		private String mLocName;
		// address
		@XStreamAlias("storeAddress")
		private String mAddr;
		// city
		@XStreamAlias("storeLocationCity")
		private String mCity;
		// state
		@XStreamAlias("storeLocationState")
		private String mState;
		// postal code
		@XStreamAlias("storeLocationZip")
		private String mZip;
		// phone number
		@XStreamAlias("storePhoneNo")
		private String mPhone;
		// time zone
		@XStreamAlias("storeTimeZone")
		private String mTimeZone;
		
		/**
		 * Default constructor
		 */
		public IntrvwLocDetails()
		{
			// initialize everything to an empty string (so XStream will generate an empty tag)
			mLocNbr = EMPTY_STRING;
			mLocName = EMPTY_STRING;
			mAddr = EMPTY_STRING;
			mCity = EMPTY_STRING;
			mState = EMPTY_STRING;
			mZip = EMPTY_STRING;
			mPhone = EMPTY_STRING;
			mTimeZone = EMPTY_STRING;
		} // end constructor()
		
		/**
		 * Get the location number
		 * 
		 * @return				The location number
		 */
		public String getLocNbr()
		{
			return mLocNbr;
		} // end function getLocNbr()
		
		/**
		 * Set the location number
		 * 
		 * @param locNbr		The location number
		 */
		public void setLocNbr(String locNbr)
		{
			if(locNbr != null)
			{
				mLocNbr = locNbr;			
			} // end if(locNbr != null)
		} // end function setLocNbr()
		
		/**
		 * Get the location name
		 * 
		 * @return				The location name
		 */
		public String getLocName()
		{
			return mLocName;
		} // end function getLocName()
		
		/**
		 * Set the location name
		 * 
		 * @param locName		The location name
		 */
		public void setLocName(String locName)
		{
			if(locName != null)
			{
				mLocName = locName;
			} // end if(locName != null)
		} // end function setLocName()
		
		/**
		 * Get the address
		 * 
		 * @return				The address
		 */
		public String getAddr()
		{
			return mAddr;
		} // end function getAddr()
		
		/**
		 * Set the address
		 * 
		 * @param addr			The address
		 */
		public void setAddr(String addr)
		{
			if(addr != null)
			{
				mAddr = addr;
			} // end if(addr != null)
		} // end function setAddr()
		
		/**
		 * Get the city
		 * 
		 * @return				The city
		 */
		public String getCity()
		{
			return mCity;
		} // end function getCity()
		
		/**
		 * Set the city
		 * 
		 * @param city			The city
		 */
		public void setCity(String city)
		{
			if(city != null)
			{
				mCity = city;
			} // end if(city != null)
		} // end function setCity()
		
		/**
		 * Get the state
		 * 
		 * @return				The state
		 */
		public String getState()
		{
			return mState;
		} // end function getState()
		
		/**
		 * Set the state
		 * 
		 * @param state			The state
		 */
		public void setState(String state)
		{
			if(state != null)
			{
				mState = state;
			} // end if(state != null)
		} // end function setState()
		
		/**
		 * Get the postal code
		 * 
		 * @return				The postal code
		 */
		public String getZip()
		{
			return mZip;
		} // end function getZip()
		
		/**
		 * Set the postal code
		 * 
		 * @param zip			The postal code
		 */
		public void setZip(String zip)
		{
			if(zip != null)
			{
				mZip = zip;
			} // end if(zip != null)
		} // end function setZip()
		
		/**
		 * Get the phone number
		 * 
		 * @return				The phone number
		 */
		public String getPhone()
		{
			return mPhone;
		} // end function getPhone()
		
		/**
		 * Set the phone number
		 * 
		 * @param phone			The phone number
		 */
		public void setPhone(String phone)
		{
			if(phone != null)
			{
				mPhone = phone;
			} // end if(phone != null)
		} // end function setPhone()
		
		/**
		 * Get the time zone
		 * 
		 * @return				The time zone
		 */
		public String getTimeZone()
		{
			return mTimeZone;
		} // end function getTimeZone()
		
		/**
		 * Set the time zone
		 * 
		 * @param timezone		The time zone
		 */
		public void setTimeZone(String timezone)
		{
			if(timezone != null)
			{
				mTimeZone = timezone;
			} // end if(timezone != null)
		} // end function setTimeZone()
	} // end class IntrvwLocDetails
	
	/**
	 * Used to transport the schedule interviews check details to clients of the inbound
	 * phone screen service methods being developed for RSA IVR Enhancements Jan 2016.
	 * This class is necessary to make the XStream API produce the XML
	 * that has been communicated to external users of this service.
	 */	
	@XStreamAlias("candidateSchdIntrwChecks")
	static class SchdIntrvwChecks
	{
		// Offers / Interviews Check
		@XStreamAlias("offersInterviewsCheck")
		private String mOffersInterviewsCheck;
		// Calendar Availability Check
		@XStreamAlias("calendarAvailability")
		private String mCalendarAvailability;
		
		public SchdIntrvwChecks()
		{
			mOffersInterviewsCheck = EMPTY_STRING;
			mCalendarAvailability = EMPTY_STRING;
		}
		/**
		 * Get the Offers Interviews Check
		 * 
		 * @return	The offersInterviewsCheck
		 */
		public String getOffersInterviewsCheck()
		{
			return mOffersInterviewsCheck;
		} // end function getOffersInterviewsCheck()
		
		/**
		 * Set the Offers Interviews Check
		 * 
		 * @param String	offersInterviewsCheck
		 */
		public void setOffersInterviewsCheck(String offersInterviewsCheck)
		{
			mOffersInterviewsCheck = offersInterviewsCheck;
		} // end function setOffersInterviewsCheck()
		
		/**
		 * Get the Calendar Availability Check
		 * 
		 * @return	The calendarAvailability
		 */
		public String getCalendarAvailability()
		{
			return mCalendarAvailability;
		} // end function getCalendarAvailability()
		
		/**
		 * Set the Calendar Availability Check
		 * 
		 * @param String  The calendarAvailability
		 */
		public void setCalendarAvailability(String calendarAvailability)
		{
			mCalendarAvailability = calendarAvailability;
		} // end function setCalendarAvailability()
	} // end class SchdIntrvwChecks	
} // end class PhoneScreenInboundResponse 
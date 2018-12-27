package com.homedepot.hr.hr.retailstaffing.dto.response;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenReqnSpecKOQuestResponse.java
 * Application: RetailStaffing
 */
import java.io.Serializable;

import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Used to transport phone screen requisition specific knock out details.  IVR Changes for FMS 7894
 */
@XStreamAlias("phoneScreenReqnSpecKODetailResponse")
public class PhoneScreenReqnSpecKODetailResponse implements Serializable
{
	private static final long serialVersionUID = -7106982814668814964L;
	
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
    private String confirmationNbr;

    //Position Title Description
    @XStreamAlias("positionTitle")
    private String positionTitle;
    
    //Part Time, Full Time, Seasonal
    @XStreamAlias("employmentCategory")
    private String employmentCategory;
    
    //Employment Category ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("employmentCategoryId")
    private int employmentCategoryId;
    
    //Is this a traveling position
    @XStreamAlias("travelingPosition")
    private String travelingPosition;
    
    //Store Location information
    @XStreamAlias("storeLocation")
    private String storeLocation;
    
    //Position/Location ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("positionQuestId")
    private int positionQuestId;
    
    //Entered Hourly Rate for job
    @XStreamAlias("hourlyRate")
    private String hourlyRate = null;
    
    //Hourly Rate ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("hourlyRateQuestId")
    private int hourlyRateQuestId;
    
    //Was availability defined on the requisition
    @XStreamAlias("bypassDateTime")
    private String bypassDateTime;
    
    //Weekdays Required
    @XStreamAlias("weekdaysSelected")
    private String weekdaysSelected;
    
    //Weekdays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("weekdaysSelectedId")
    private int weekdaysSelectedId;
    
    //Saturdays required
    @XStreamAlias("saturdaysSelected")
    private String saturdaysSelected;
    
    //Saturdays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD 
    @XStreamAlias("saturdaysSelectedId")
    private int saturdaysSelectedId;
    
    //Sundays required
    @XStreamAlias("sundaysSelected")
    private String sundaysSelected;
    
    //Sundays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("sundaysSelectedId")
    private int sundaysSelectedId;
    
    //Early Morning required
    @XStreamAlias("earlyMorningSelected")
    private String earlyMorningSelected;
    
    //Early Morning ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("earlyMorningSelectedId")
    private int earlyMorningSelectedId;
    
    //Morning required
    @XStreamAlias("morningSelected")
    private String morningSelected;
    
    //Morning ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("morningSelectedId")
    private int morningSelectedId;
    
    //Afternoon Required
    @XStreamAlias("afternoonSelected")
    private String afternoonSelected;
    
    //Afternoon ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("afternoonSelectedId")
    private int afternoonSelectedId;
    
    //Night Required
    @XStreamAlias("nightSelected")
    private String nightSelected;
    
    //Night ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("nightSelectedId")
    private int nightSelectedId;
    
    //Late Night required
    @XStreamAlias("lateNightSelected")
    private String lateNightSelected;
    
    //Late Night ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("lateNightSelectedId")
    private int lateNightSelectedId;
    
    //Overnight Required
    @XStreamAlias("overnightSelected")
    private String overnightSelected;
    
    //Overnight ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("overnightSelectedId")
    private int overnightSelectedId;
    
    //Needs Reasonable Accommodation for Schedule
    @XStreamAlias("reasonableAccommodation")
    private String reasonableAccommodation;
    
    //Reasonable Accommodation ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("reasonableAccommodationId")
    private int reasonableAccommodationId;
    
    //Driver's License Required
    @XStreamAlias("requiresDriversLicense")
    private String requiresDriversLicense;
    
    //Driver's License ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("requiresDriversLicenseId")
    private int requiresDriversLicenseId;
    
    //Reliable Transportation required
    @XStreamAlias("reliableTransportation")
    private String reliableTransportation;
    
    //Reliable Transportation ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("reliableTransportationId")
    private int reliableTransportationId;

    /**
     * Default constructor
     */
    public PhoneScreenReqnSpecKODetailResponse()
    {
    	
    } // end constructor
    
    //Set all of the question and ID defaults
    public void setQuestionAndIdDefaults() {
    	//Setup all Schedule Preferences to N, will update to Y if the requisition requires it.
    	bypassDateTime = "N";
    	weekdaysSelected = "N";
    	saturdaysSelected = "N";
    	sundaysSelected = "N";
    	earlyMorningSelected = "N";
    	morningSelected = "N";
    	afternoonSelected = "N";
    	nightSelected = "N";
    	lateNightSelected = "N";
    	overnightSelected = "N";
    	
    	//Set up driving questions to N, will update to Y if needed
    	requiresDriversLicense = "N";
    	reliableTransportation = "N";
    	
    	//Set all Schedule Preference ID's
    	weekdaysSelectedId = RetailStaffingConstants.WEEKDAYS_QUEST_TYPE_CD;
    	saturdaysSelectedId = RetailStaffingConstants.SATURDAY_QUEST_TYPE_CD;
    	sundaysSelectedId = RetailStaffingConstants.SUNDAY_QUEST_TYPE_CD;
    	earlyMorningSelectedId = RetailStaffingConstants.EARLY_AM_QUEST_TYPE_CD;
    	morningSelectedId = RetailStaffingConstants.MORNING_QUEST_TYPE_CD;
    	afternoonSelectedId = RetailStaffingConstants.AFTERNOON_QUEST_TYPE_CD;
    	nightSelectedId = RetailStaffingConstants.NIGHT_QUEST_TYPE_CD;
    	lateNightSelectedId = RetailStaffingConstants.LATE_NIGHT_QUEST_TYPE_CD;
    	overnightSelectedId = RetailStaffingConstants.OVERNIGHT_QUEST_TYPE_CD;
    	reasonableAccommodationId = RetailStaffingConstants.REASONABLE_ACCOMMODATION_QUEST_TYPE_CD;

    	//Set all other Question ID's
    	employmentCategoryId = RetailStaffingConstants.EMPLOYMENT_CATEGORY_QUEST_TYPE_CD;
    	positionQuestId = RetailStaffingConstants.POSITION_LOCATION_QUEST_TYPE_CD;
    	hourlyRateQuestId = RetailStaffingConstants.HOURLY_RATE_QUEST_TYPE_CD;
    	requiresDriversLicenseId = RetailStaffingConstants.REQUIRES_DRIVERS_LICENSE_QUEST_TYPE_CD;
    	reliableTransportationId = RetailStaffingConstants.RELIABLE_TRANSPORTATION_QUEST_TYPE_CD;
    }
    
    
    
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
	 * @return the confirmationNbr
	 */
	public String getConfirmationNbr() {
		return confirmationNbr;
	}

	/**
	 * @param confirmationNbr the confirmationNbr to set
	 */
	public void setConfirmationNbr(String confirmationNbr) {
		this.confirmationNbr = confirmationNbr;
	}

	/**
	 * @return the employmentCategory
	 */
	public String getEmploymentCategory() {
		return employmentCategory;
	}

	/**
	 * @param employmentCategory the employmentCategory to set
	 */
	public void setEmploymentCategory(String employmentCategory) {
		this.employmentCategory = employmentCategory;
	}

	/**
	 * @return the employmentCategoryId
	 */
	public int getEmploymentCategoryId() {
		return employmentCategoryId;
	}

	/**
	 * @param employmentCategoryId the employmentCategoryId to set
	 */
	public void setEmploymentCategoryId(int employmentCategoryId) {
		this.employmentCategoryId = employmentCategoryId;
	}

	/**
	 * @return the travelingPosition
	 */
	public String getTravelingPosition() {
		return travelingPosition;
	}

	/**
	 * @param travelingPosition the travelingPosition to set
	 */
	public void setTravelingPosition(String travelingPosition) {
		this.travelingPosition = travelingPosition;
	}

	/**
	 * @return the storeLocation
	 */
	public String getStoreLocation() {
		return storeLocation;
	}

	/**
	 * @param storeLocation the storeLocation to set
	 */
	public void setStoreLocation(String storeLocation) {
		this.storeLocation = storeLocation;
	}

	/**
	 * @return the positionQuestId
	 */
	public int getPositionQuestId() {
		return positionQuestId;
	}

	/**
	 * @param positionQuestId the positionQuestId to set
	 */
	public void setPositionQuestId(int positionQuestId) {
		this.positionQuestId = positionQuestId;
	}

	/**
	 * @return the hourlyRate
	 */
	public String getHourlyRate() {
		return hourlyRate;
	}

	/**
	 * @param hourlyRate the hourlyRate to set
	 */
	public void setHourlyRate(String hourlyRate) {
		this.hourlyRate = hourlyRate;
	}

	/**
	 * @return the hourlyRateQuestId
	 */
	public int getHourlyRateQuestId() {
		return hourlyRateQuestId;
	}

	/**
	 * @param hourlyRateQuestId the hourlyRateQuestId to set
	 */
	public void setHourlyRateQuestId(int hourlyRateQuestId) {
		this.hourlyRateQuestId = hourlyRateQuestId;
	}

	/**
	 * @return the bypassDateTime
	 */
	public String getBypassDateTime() {
		return bypassDateTime;
	}

	/**
	 * @param bypassDateTime the bypassDateTime to set
	 */
	public void setBypassDateTime(String bypassDateTime) {
		this.bypassDateTime = bypassDateTime;
	}

	/**
	 * @return the weekdaysSelected
	 */
	public String getWeekdaysSelected() {
		return weekdaysSelected;
	}

	/**
	 * @param weekdaysSelected the weekdaysSelected to set
	 */
	public void setWeekdaysSelected(String weekdaysSelected) {
		this.weekdaysSelected = weekdaysSelected;
	}

	/**
	 * @return the weekdaysSelectedId
	 */
	public int getWeekdaysSelectedId() {
		return weekdaysSelectedId;
	}

	/**
	 * @param weekdaysSelectedId the weekdaysSelectedId to set
	 */
	public void setWeekdaysSelectedId(int weekdaysSelectedId) {
		this.weekdaysSelectedId = weekdaysSelectedId;
	}

	/**
	 * @return the saturdaysSelected
	 */
	public String getSaturdaysSelected() {
		return saturdaysSelected;
	}

	/**
	 * @param saturdaysSelected the saturdaysSelected to set
	 */
	public void setSaturdaysSelected(String saturdaysSelected) {
		this.saturdaysSelected = saturdaysSelected;
	}

	/**
	 * @return the saturdaysSelectedId
	 */
	public int getSaturdaysSelectedId() {
		return saturdaysSelectedId;
	}

	/**
	 * @param saturdaysSelectedId the saturdaysSelectedId to set
	 */
	public void setSaturdaysSelectedId(int saturdaysSelectedId) {
		this.saturdaysSelectedId = saturdaysSelectedId;
	}

	/**
	 * @return the sundaysSelected
	 */
	public String getSundaysSelected() {
		return sundaysSelected;
	}

	/**
	 * @param sundaysSelected the sundaysSelected to set
	 */
	public void setSundaysSelected(String sundaysSelected) {
		this.sundaysSelected = sundaysSelected;
	}

	/**
	 * @return the sundaysSelectedId
	 */
	public int getSundaysSelectedId() {
		return sundaysSelectedId;
	}

	/**
	 * @param sundaysSelectedId the sundaysSelectedId to set
	 */
	public void setSundaysSelectedId(int sundaysSelectedId) {
		this.sundaysSelectedId = sundaysSelectedId;
	}

	/**
	 * @return the earlyMorningSelected
	 */
	public String getEarlyMorningSelected() {
		return earlyMorningSelected;
	}

	/**
	 * @param earlyMorningSelected the earlyMorningSelected to set
	 */
	public void setEarlyMorningSelected(String earlyMorningSelected) {
		this.earlyMorningSelected = earlyMorningSelected;
	}

	/**
	 * @return the earlyMorningSelectedId
	 */
	public int getEarlyMorningSelectedId() {
		return earlyMorningSelectedId;
	}

	/**
	 * @param earlyMorningSelectedId the earlyMorningSelectedId to set
	 */
	public void setEarlyMorningSelectedId(int earlyMorningSelectedId) {
		this.earlyMorningSelectedId = earlyMorningSelectedId;
	}

	/**
	 * @return the morningSelected
	 */
	public String getMorningSelected() {
		return morningSelected;
	}

	/**
	 * @param morningSelected the morningSelected to set
	 */
	public void setMorningSelected(String morningSelected) {
		this.morningSelected = morningSelected;
	}

	/**
	 * @return the morningSelectedId
	 */
	public int getMorningSelectedId() {
		return morningSelectedId;
	}

	/**
	 * @param morningSelectedId the morningSelectedId to set
	 */
	public void setMorningSelectedId(int morningSelectedId) {
		this.morningSelectedId = morningSelectedId;
	}

	/**
	 * @return the afternoonSelected
	 */
	public String getAfternoonSelected() {
		return afternoonSelected;
	}

	/**
	 * @param afternoonSelected the afternoonSelected to set
	 */
	public void setAfternoonSelected(String afternoonSelected) {
		this.afternoonSelected = afternoonSelected;
	}

	/**
	 * @return the afternoonSelectedId
	 */
	public int getAfternoonSelectedId() {
		return afternoonSelectedId;
	}

	/**
	 * @param afternoonSelectedId the afternoonSelectedId to set
	 */
	public void setAfternoonSelectedId(int afternoonSelectedId) {
		this.afternoonSelectedId = afternoonSelectedId;
	}

	/**
	 * @return the nightSelected
	 */
	public String getNightSelected() {
		return nightSelected;
	}

	/**
	 * @param nightSelected the nightSelected to set
	 */
	public void setNightSelected(String nightSelected) {
		this.nightSelected = nightSelected;
	}

	/**
	 * @return the nightSelectedId
	 */
	public int getNightSelectedId() {
		return nightSelectedId;
	}

	/**
	 * @param nightSelectedId the nightSelectedId to set
	 */
	public void setNightSelectedId(int nightSelectedId) {
		this.nightSelectedId = nightSelectedId;
	}

	/**
	 * @return the lateNightSelected
	 */
	public String getLateNightSelected() {
		return lateNightSelected;
	}

	/**
	 * @param lateNightSelected the lateNightSelected to set
	 */
	public void setLateNightSelected(String lateNightSelected) {
		this.lateNightSelected = lateNightSelected;
	}

	/**
	 * @return the lateNightSelectedId
	 */
	public int getLateNightSelectedId() {
		return lateNightSelectedId;
	}

	/**
	 * @param lateNightSelectedId the lateNightSelectedId to set
	 */
	public void setLateNightSelectedId(int lateNightSelectedId) {
		this.lateNightSelectedId = lateNightSelectedId;
	}

	/**
	 * @return the overnightSelected
	 */
	public String getOvernightSelected() {
		return overnightSelected;
	}

	/**
	 * @param overnightSelected the overnightSelected to set
	 */
	public void setOvernightSelected(String overnightSelected) {
		this.overnightSelected = overnightSelected;
	}

	/**
	 * @return the overnightSelectedId
	 */
	public int getOvernightSelectedId() {
		return overnightSelectedId;
	}

	/**
	 * @param overnightSelectedId the overnightSelectedId to set
	 */
	public void setOvernightSelectedId(int overnightSelectedId) {
		this.overnightSelectedId = overnightSelectedId;
	}

	/**
	 * @return the reasonableAccommodation
	 */
	public String getReasonableAccommodation() {
		return reasonableAccommodation;
	}

	/**
	 * @param reasonableAccommodation the reasonableAccommodation to set
	 */
	public void setReasonableAccommodation(String reasonableAccommodation) {
		this.reasonableAccommodation = reasonableAccommodation;
	}

	/**
	 * @return the reasonableAccommodationId
	 */
	public int getReasonableAccommodationId() {
		return reasonableAccommodationId;
	}

	/**
	 * @param reasonableAccommodationId the reasonableAccommodationId to set
	 */
	public void setReasonableAccommodationId(int reasonableAccommodationId) {
		this.reasonableAccommodationId = reasonableAccommodationId;
	}

	/**
	 * @return the requiresDriversLicense
	 */
	public String getRequiresDriversLicense() {
		return requiresDriversLicense;
	}

	/**
	 * @param requiresDriversLicense the requiresDriversLicense to set
	 */
	public void setRequiresDriversLicense(String requiresDriversLicense) {
		this.requiresDriversLicense = requiresDriversLicense;
	}

	/**
	 * @return the requiresDriversLicenseId
	 */
	public int getRequiresDriversLicenseId() {
		return requiresDriversLicenseId;
	}

	/**
	 * @param requiresDriversLicenseId the requiresDriversLicenseId to set
	 */
	public void setRequiresDriversLicenseId(int requiresDriversLicenseId) {
		this.requiresDriversLicenseId = requiresDriversLicenseId;
	}

	/**
	 * @return the reliableTransportation
	 */
	public String getReliableTransportation() {
		return reliableTransportation;
	}

	/**
	 * @param reliableTransportation the reliableTransportation to set
	 */
	public void setReliableTransportation(String reliableTransportation) {
		this.reliableTransportation = reliableTransportation;
	}

	/**
	 * @return the reliableTransportationId
	 */
	public int getReliableTransportationId() {
		return reliableTransportationId;
	}

	/**
	 * @param reliableTransportationId the reliableTransportationId to set
	 */
	public void setReliableTransportationId(int reliableTransportationId) {
		this.reliableTransportationId = reliableTransportationId;
	}

	/**
	 * @return the positionTitle
	 */
	public String getPositionTitle() {
		return positionTitle;
	}

	/**
	 * @param positionTitle the positionTitle to set
	 */
	public void setPositionTitle(String positionTitle) {
		this.positionTitle = positionTitle;
	}

} // end class PhoneScreenReqnSpecKOQuestResponse
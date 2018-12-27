/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: PhoneScreenMinReqTO.java
 */

package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("updatePhoneScreenReqnSpecKnockOutRequest")
public class UpdatePhoneScreenReqnSpecKnockOutRequest implements Serializable
{
	private static final long serialVersionUID = 3775714142083771816L;
	private static final Logger mLogger = Logger.getLogger(UpdatePhoneScreenReqnSpecKnockOutRequest.class);
	
	// the confirmation number (phone screen id)
    @XStreamAlias("confirmationNo")
    private String phoneScreenId;
    
    //Answer to Employment Category question
    @XStreamAlias("acceptsEmploymentCategory")
    private String acceptsEmploymentCategory;
    
    //Employment Category ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("employmentCategoryId")
    private int employmentCategoryId;

    //Answer to Position/Location question
    @XStreamAlias("acceptsPosition")
    private String acceptsPosition;
    
    //Position/Location ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("positionQuestId")
    private int positionQuestId;    
    
    //Answer to Hourly Rate question
    @XStreamAlias("acceptsHourlyRate")
    private String acceptsHourlyRate;
    
    //Hourly Rate ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("hourlyRateQuestId")
    private int hourlyRateQuestId;    
    
    //Answer to Weekdays question
    @XStreamAlias("canWorkWeekdays")
    private String canWorkWeekdays;
    
    //Weekdays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("weekdaysSelectedId")
    private int weekdaysSelectedId;
    
    //Answer to Saturdays question
    @XStreamAlias("canWorkSaturdays")
    private String canWorkSaturdays;
    
    //Saturdays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD 
    @XStreamAlias("saturdaysSelectedId")
    private int saturdaysSelectedId;
    
    //Answer to Sundays question
    @XStreamAlias("canWorkSundays")
    private String canWorkSundays;
    
    //Sundays ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("sundaysSelectedId")
    private int sundaysSelectedId;    
    
    //Answer to Early Morning
    @XStreamAlias("canWorkEarlyMorning")
    private String canWorkEarlyMorning;
    
    //Early Morning ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("earlyMorningSelectedId")
    private int earlyMorningSelectedId;
    
    //Answer to Morning question
    @XStreamAlias("canWorkMornings")
    private String canWorkMornings;
    
    //Morning ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("morningSelectedId")
    private int morningSelectedId;
    
    //Answer to Afternoon question
    @XStreamAlias("canWorkAfternoon")
    private String canWorkAfternoon;
    
    //Afternoon ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("afternoonSelectedId")
    private int afternoonSelectedId;
    
    //Answer to Night question
    @XStreamAlias("canWorkNight")
    private String canWorkNight;
    
    //Night ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("nightSelectedId")
    private int nightSelectedId;
    
    //Answer to Late Night question
    @XStreamAlias("canWorkLateNight")
    private String canWorkLateNight;
    
    //Late Night ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("lateNightSelectedId")
    private int lateNightSelectedId;
    
    //Answer to Overnight question
    @XStreamAlias("canWorkOvernight")
    private String canWorkOvernight;
    
    //Overnight ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("overnightSelectedId")
    private int overnightSelectedId;
    
    //Answer to Needs Reasonable Accommodation for Schedule question
    @XStreamAlias("needsReasonableAccommodation")
    private String needsReasonableAccommodation;
    
    //Reasonable Accommodation ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("reasonableAccommodationId")
    private int reasonableAccommodationId;
    
    //Answer to Driver's License Required question
    @XStreamAlias("hasDriversLicense")
    private String hasDriversLicense;
    
    //Driver's License ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("requiresDriversLicenseId")
    private int requiresDriversLicenseId;
    
    //Answer to Reliable Transportation required question
    @XStreamAlias("hasReliableTransportation")
    private String hasReliableTransportation;
    
    //Reliable Transportation ID in PHN_SCRN_MIN_REQMT field PSCRN_QST_TYP_CD
    @XStreamAlias("reliableTransportationId")
    private int reliableTransportationId;    
    
	/**
	 * @return the phoneScreenId
	 */
	public String getPhoneScreenId() {
		return phoneScreenId;
	}

	/**
	 * @param phoneScreenId the phoneScreenId to set
	 */
	public void setPhoneScreenId(String phoneScreenId) {
		this.phoneScreenId = phoneScreenId;
	}    
	
	/**
	 * @return the acceptsEmploymentCategory
	 */
	public String getAcceptsEmploymentCategory() {
		return acceptsEmploymentCategory;
	}

	/**
	 * @param acceptsEmploymentCategory the acceptsEmploymentCategory to set
	 */
	public void setAcceptsEmploymentCategory(String acceptsEmploymentCategory) {
		this.acceptsEmploymentCategory = acceptsEmploymentCategory;
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
	 * @return the acceptsPosition
	 */
	public String getAcceptsPosition() {
		return acceptsPosition;
	}

	/**
	 * @param acceptsPosition the acceptsPosition to set
	 */
	public void setAcceptsPosition(String acceptsPosition) {
		this.acceptsPosition = acceptsPosition;
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
	 * @return the acceptsHourlyRate
	 */
	public String getAcceptsHourlyRate() {
		return acceptsHourlyRate;
	}

	/**
	 * @param acceptsHourlyRate the acceptsHourlyRate to set
	 */
	public void setAcceptsHourlyRate(String acceptsHourlyRate) {
		this.acceptsHourlyRate = acceptsHourlyRate;
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
	 * @return the canWorkWeekdays
	 */
	public String getCanWorkWeekdays() {
		return canWorkWeekdays;
	}

	/**
	 * @param canWorkWeekdays the canWorkWeekdays to set
	 */
	public void setCanWorkWeekdays(String canWorkWeekdays) {
		this.canWorkWeekdays = canWorkWeekdays;
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
	 * @return the canWorkSaturdays
	 */
	public String getCanWorkSaturdays() {
		return canWorkSaturdays;
	}

	/**
	 * @param canWorkSaturdays the canWorkSaturdays to set
	 */
	public void setCanWorkSaturdays(String canWorkSaturdays) {
		this.canWorkSaturdays = canWorkSaturdays;
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
	 * @return the canWorkSundays
	 */
	public String getCanWorkSundays() {
		return canWorkSundays;
	}

	/**
	 * @param canWorkSundays the canWorkSundays to set
	 */
	public void setCanWorkSundays(String canWorkSundays) {
		this.canWorkSundays = canWorkSundays;
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
	 * @return the canWorkEarlyMorning
	 */
	public String getCanWorkEarlyMorning() {
		return canWorkEarlyMorning;
	}

	/**
	 * @param canWorkEarlyMorning the canWorkEarlyMorning to set
	 */
	public void setCanWorkEarlyMorning(String canWorkEarlyMorning) {
		this.canWorkEarlyMorning = canWorkEarlyMorning;
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
	 * @return the canWorkMornings
	 */
	public String getCanWorkMornings() {
		return canWorkMornings;
	}

	/**
	 * @param canWorkMornings the canWorkMornings to set
	 */
	public void setCanWorkMornings(String canWorkMornings) {
		this.canWorkMornings = canWorkMornings;
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
	 * @return the canWorkAfternoon
	 */
	public String getCanWorkAfternoon() {
		return canWorkAfternoon;
	}

	/**
	 * @param canWorkAfternoon the canWorkAfternoon to set
	 */
	public void setCanWorkAfternoon(String canWorkAfternoon) {
		this.canWorkAfternoon = canWorkAfternoon;
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
	 * @return the canWorkNight
	 */
	public String getCanWorkNight() {
		return canWorkNight;
	}

	/**
	 * @param canWorkNight the canWorkNight to set
	 */
	public void setCanWorkNight(String canWorkNight) {
		this.canWorkNight = canWorkNight;
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
	 * @return the canWorkLateNight
	 */
	public String getCanWorkLateNight() {
		return canWorkLateNight;
	}

	/**
	 * @param canWorkLateNight the canWorkLateNight to set
	 */
	public void setCanWorkLateNight(String canWorkLateNight) {
		this.canWorkLateNight = canWorkLateNight;
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
	 * @return the canWorkOvernight
	 */
	public String getCanWorkOvernight() {
		return canWorkOvernight;
	}

	/**
	 * @param canWorkOvernight the canWorkOvernight to set
	 */
	public void setCanWorkOvernight(String canWorkOvernight) {
		this.canWorkOvernight = canWorkOvernight;
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
	 * @return the needsReasonableAccommodation
	 */
	public String getNeedsReasonableAccommodation() {
		return needsReasonableAccommodation;
	}

	/**
	 * @param needsReasonableAccommodation the needsReasonableAccommodation to set
	 */
	public void setNeedsReasonableAccommodation(String needsReasonableAccommodation) {
		this.needsReasonableAccommodation = needsReasonableAccommodation;
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
	 * @return the hasDriversLicense
	 */
	public String getHasDriversLicense() {
		return hasDriversLicense;
	}

	/**
	 * @param hasDriversLicense the hasDriversLicense to set
	 */
	public void setHasDriversLicense(String hasDriversLicense) {
		this.hasDriversLicense = hasDriversLicense;
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
	 * @return the hasReliableTransportation
	 */
	public String getHasReliableTransportation() {
		return hasReliableTransportation;
	}

	/**
	 * @param hasReliableTransportation the hasReliableTransportation to set
	 */
	public void setHasReliableTransportation(String hasReliableTransportation) {
		this.hasReliableTransportation = hasReliableTransportation;
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
	
	//Create a Map of all the questions, with ID and Answer
	public Map<Integer, String> createMapOfQuestions() {
		Map<Integer, String> mappedQuestions = new HashMap<Integer, String>();
		
		if (acceptsEmploymentCategory != null && !acceptsEmploymentCategory.equals("") && employmentCategoryId != 0) {
			mappedQuestions.put(employmentCategoryId, acceptsEmploymentCategory);
		}
		
		if (acceptsPosition != null && !acceptsPosition.equals("") && positionQuestId != 0) {
			mappedQuestions.put(positionQuestId, acceptsPosition);
		}		
		
		if (acceptsHourlyRate != null && !acceptsHourlyRate.equals("") && hourlyRateQuestId != 0) {
			mappedQuestions.put(hourlyRateQuestId, acceptsHourlyRate);
		}
		
		if (canWorkWeekdays != null && !canWorkWeekdays.equals("") && weekdaysSelectedId != 0) {
			mappedQuestions.put(weekdaysSelectedId, canWorkWeekdays);
		}		

		if (canWorkSaturdays != null && !canWorkSaturdays.equals("") && saturdaysSelectedId != 0) {
			mappedQuestions.put(saturdaysSelectedId, canWorkSaturdays);
		}
		
		if (canWorkSundays != null && !canWorkSundays.equals("") && sundaysSelectedId != 0) {
			mappedQuestions.put(sundaysSelectedId, canWorkSundays);
		}		
		
		if (canWorkEarlyMorning != null && !canWorkEarlyMorning.equals("") && earlyMorningSelectedId != 0) {
			mappedQuestions.put(earlyMorningSelectedId, canWorkEarlyMorning);
		}		
		
		if (canWorkMornings != null && !canWorkMornings.equals("") && morningSelectedId != 0) {
			mappedQuestions.put(morningSelectedId, canWorkMornings);
		}
		
		if (canWorkAfternoon != null && !canWorkAfternoon.equals("") && afternoonSelectedId != 0) {
			mappedQuestions.put(afternoonSelectedId, canWorkAfternoon);
		}		
		
		if (canWorkNight != null && !canWorkNight.equals("") && nightSelectedId != 0) {
			mappedQuestions.put(nightSelectedId, canWorkNight);
		}		
		
		if (canWorkLateNight != null && !canWorkLateNight.equals("") && lateNightSelectedId != 0) {
			mappedQuestions.put(lateNightSelectedId, canWorkLateNight);
		}		
		
		if (canWorkOvernight != null && !canWorkOvernight.equals("") && overnightSelectedId != 0) {
			mappedQuestions.put(overnightSelectedId, canWorkOvernight);
		}		
		
		if (needsReasonableAccommodation != null && !needsReasonableAccommodation.equals("") && reasonableAccommodationId != 0) {
			mappedQuestions.put(reasonableAccommodationId, needsReasonableAccommodation);
		}		
		
		if (hasDriversLicense != null && !hasDriversLicense.equals("") && requiresDriversLicenseId != 0) {
			mappedQuestions.put(requiresDriversLicenseId, hasDriversLicense);
		}		
		
		if (hasReliableTransportation != null && !hasReliableTransportation.equals("") && reliableTransportationId != 0) {
			mappedQuestions.put(reliableTransportationId, hasReliableTransportation);
		}		

		return mappedQuestions;
	}
	
	public boolean determinePassFail() {
		boolean returnedResult = true;
		
		//Check Position/Location, Employment Category, Pay Rate.  These should always be answered
		if ((acceptsPosition != null && acceptsPosition.equals("N")) ||
			(acceptsEmploymentCategory != null && acceptsEmploymentCategory.equals("N")) ||
			(acceptsHourlyRate != null && acceptsHourlyRate.equals("N"))) {
			returnedResult = false;
		}
		mLogger.debug(String.format("Job Related KO Questions:%1$b", returnedResult));
		
		//Check Work Schedule
		if (returnedResult) {
			if ((canWorkWeekdays != null && canWorkWeekdays.equals("N")) ||
				(canWorkSaturdays != null && canWorkSaturdays.equals("N")) ||
				(canWorkSundays != null && canWorkSundays.equals("N")) ||
				(canWorkEarlyMorning != null && canWorkEarlyMorning.equals("N")) ||
				(canWorkMornings != null && canWorkMornings.equals("N")) ||
				(canWorkAfternoon != null && canWorkAfternoon.equals("N")) ||
				(canWorkNight != null && canWorkNight.equals("N")) ||
				(canWorkLateNight != null && canWorkLateNight.equals("N")) ||
				(canWorkOvernight != null && canWorkOvernight.equals("N"))) {
				returnedResult = false;
			}
			if  (!returnedResult && needsReasonableAccommodation != null && needsReasonableAccommodation.equals("Y")) {
				returnedResult = true;
			}
			mLogger.debug(String.format("Work Schedule Related KO Questions:%1$b", returnedResult));
		}
		
		//Check Driver's License, and Reliable Transportation.  These will only be answered if it is traveling position and requires a license
		if (returnedResult) {
			if ((hasDriversLicense != null && hasDriversLicense.equals("N")) ||
				(hasReliableTransportation != null && hasReliableTransportation.equals("N"))) {
				returnedResult = false;
			}		
			mLogger.debug(String.format("Dirving Related KO Questions:%1$b", returnedResult));
		}
		
		
		return returnedResult;
	}
}

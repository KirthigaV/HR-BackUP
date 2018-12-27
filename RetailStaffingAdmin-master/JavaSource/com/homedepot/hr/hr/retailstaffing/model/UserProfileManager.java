/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: UserProfileManager.java
 * Application: StaffingForm
 *
 */
//This file is newly added as part of Flex to HTML Conversion - 13 May 2015
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


import com.homedepot.hr.hr.retailstaffing.dto.response.UserProfileResponseDTO;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.ta.aa.catalina.realm.Profile;


public class UserProfileManager implements RetailStaffingConstants {

    private static final Logger LOGGER = Logger.getLogger(UserProfileManager.class);
    

    /**
     * Description: This method is used to get the user profile details
     * @param request
     * @param roles
     * @return
     * @throws Exception
     */
    public UserProfileResponseDTO getUserProfileDetails(HttpServletRequest request, String roles) throws Exception
    {
        if (LOGGER.isInfoEnabled())
        {
            LOGGER.info("Entering Inside UserProfileManager Class :: getUserProfileDetails() Method");
        }
        
        boolean isUserInRole = false;
        UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
        
        try{
            // Get the profile object.This will be available if the user logs in successfully via a login screen.
            Profile profile = Profile.getCurrent();
            if(null != profile)
            {
            	String firstName = profile.getProperty(Profile.FIRST_NAME);
            	String lastName = profile.getProperty(Profile.LAST_NAME);
            	String userId = (profile.getProperty(Profile.USER_ID)).toUpperCase(Locale.getDefault());
            	String strNbr = profile.getProperty(Profile.LOCATION_NUMBER);
                
                userProfileResponseDTO.setFirstName(firstName);
                userProfileResponseDTO.setLastName(lastName);
                userProfileResponseDTO.setUserId(userId);
                userProfileResponseDTO.setStrNbr(strNbr);
                userProfileResponseDTO.setStatus(SUCCESS_APP_STATUS);
            }
            else
            {
                userProfileResponseDTO.setStatus(FAILURE_APP_STATUS);
                userProfileResponseDTO.setErrorCd(USER_PROFILE_DET_NOT_FOUND_CODE);
                userProfileResponseDTO.setErrorDesc(USER_PROFILE_DET_NOT_FOUND);
            }
            if(!Util.isNullString(roles)){
                isUserInRole = validateUserRole(request,roles);
            }
            userProfileResponseDTO.setValidRole(isUserInRole);
        }
        finally
        {
            if (LOGGER.isInfoEnabled())
            {
                LOGGER.info("Exiting from UserProfileManager Class :: getUserProfileDetails() Method");
            }
        }
        return userProfileResponseDTO;
    }
    
    /**
     * Description: This method is used to the validate the the user role
     * @param request
     * @param roles
     * @return true/false
     */
    private boolean validateUserRole(HttpServletRequest request, String roles)
    {
        String[] rolesList = roles.split(COMMA);
        for (String roleName : rolesList)
        {
            if(request.isUserInRole(roleName)){
                return true;
            }
        }
        return false;
    }
}

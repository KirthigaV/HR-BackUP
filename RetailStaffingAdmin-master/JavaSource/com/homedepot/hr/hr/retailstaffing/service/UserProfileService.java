/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: UserProfileService.java
 * Application: Retail Staffing Admin
 *
 */
//This file is newly added as part of Flex to HTML Conversion - 13 May 2015
package com.homedepot.hr.hr.retailstaffing.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.response.UserProfileResponseDTO;
import com.homedepot.hr.hr.retailstaffing.model.UserProfileManager;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;

@Path(RetailStaffingConstants.USER_PROFILE_SERVICE_PATH)
public class UserProfileService implements RetailStaffingConstants {

    private static final Logger LOGGER = Logger.getLogger(UserProfileService.class);
     
    /**
     * Description: This method is used fetch the user profile details
     * @param request
     * @param roles
     * @param version
     * @param contentType
     * @return
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
      @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path(GET_USER_PROFILE_DETAILS_PATH)
    public String getUserProfileDetails(@Context HttpServletRequest request,
                                 @DefaultValue("")@FormParam("roles") String roles,
                                 @DefaultValue("1") @FormParam(VERSION) int version,
                                 @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
    {
        if (LOGGER.isInfoEnabled())
        {
            LOGGER.info("Entering Inside UserProfileService Class : : getUserProfileDetails() Method");
        }
        
        String response = null;
        UserProfileResponseDTO userProfileResponseDTO = new UserProfileResponseDTO();
        String mediaType = contentType;
        try
        {
            //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 7 May 2015
            mediaType = Util.getMediaType(mediaType);
             if(version == VERION_ONE){
            	UserProfileManager userProfileManager = new UserProfileManager();
                userProfileResponseDTO = userProfileManager.getUserProfileDetails(request, roles);
             }
        }
        catch(Exception e)
        {
            String msgDataVal = "Exception : Error occured in getUserProfileDetails()";
            Util.logFatalError(msgDataVal, e);
            userProfileResponseDTO.setStatus(FAILURE_APP_STATUS);
            userProfileResponseDTO.setErrorCd(USER_PROFILE_EXCEPTION_ERROR_CODE);
            userProfileResponseDTO.setErrorDesc(e.getMessage());
        }
        finally
        {
            /** Serialize object to XML/JSON **/
            response = Util.getRequiredFormatRes(mediaType, userProfileResponseDTO);
            if (LOGGER.isInfoEnabled())
            {
                LOGGER.info("Exiting From UserProfileService Class : : getUserProfileDetails() Method");

            }
        }
        return response;
    }
}

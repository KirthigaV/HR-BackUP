package com.homedepot.hr.hr.retailstaffing.model;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.UserDataTO;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.ta.aa.catalina.realm.Profile;

public class UserManager implements RetailStaffingConstants 
{
	private static final Logger mLogger = Logger.getLogger(UserManager.class);
	
	public static UserDataTO getUserData() throws Exception
	{
		long startTime = 0;
		 Profile profile = Profile.getCurrent();
		 UserDataTO userDataTo = new UserDataTO();
		 
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug("Entering getUserData()");
		}
		
		try
		{
	       	userDataTo.setFirstName(profile.getProperty(Profile.FIRST_NAME));
        	userDataTo.setLastName(profile.getProperty(Profile.LAST_NAME));
        	userDataTo.setUserId(profile.getProperty(Profile.USER_ID).toUpperCase());
        	userDataTo.setLocation(profile.getProperty(Profile.LOCATION_NUMBER));
		} 
		catch(Exception e)
		{
			mLogger.error(e.getMessage(), e);
			throw e;
		} 
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getUserData(), total time to process request: %1$.9f seconds",
				(((double)endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
		return userDataTo; 
	} 
	
} 
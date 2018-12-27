package com.homedepot.hr.hr.retailstaffing.util.monitoring;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.handlers.ProbeHandler;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.ClientApplLogger;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * Checks the major components of an application and saves the result here.
 * This class is accessed by the ApplicationProbe.jsp to display the monitoring results,
 *
 * Application teams need to customize portions of this class for application specific
 * monitoring.
 *
 */
public class ProbeResult implements DAOConstants
{
	/* User defined constants */
	private static final String UP = "<font size='+4'  face='Arial, sans-serif' color='green'>Up </font>";
	private static final String DOWN = "<font size='+4'  face='Arial, sans-serif' color='Red'>Down </font>";
	private static final String APPLICATION_NAME = "Retail Staffing";
	private static final String SYSTEM = "hr";
	private static final String SUBSYSTEM = "hr";

	private static final String SEL_HR_PHONE_SCREEN_FOR_EXISTENCE = "readHumanResourcesPhoneScreenForExistence";

	//*************************************************************************************
	private final Logger mLogger = Logger.getLogger(getClass());
	private ArrayList<String> mMessages = null;

	/* Constructor
	 *
	 */
	public ProbeResult()
	{
		mMessages = new ArrayList<String>();
	} // end constructor

	public String getApplicationName()
	{
		return APPLICATION_NAME;
	} // end function getApplicationName()

	public ArrayList<String> getMessages()
	{
		return mMessages;
	} // end function getMessages()

	/**
	 * Checks if the application components are working fine.
	 * @return String "UP" if everything is fine, or "DOWN" if any
	 * application component returned failure.
	 */
	public String getResult()
	{
		String result = DOWN;
		// clear out the messages
		mMessages.clear();

		try
		{
			// create the inputs to the DAO method (nothing but the method being invoked)
			MapStream inputs = new MapStream(SEL_HR_PHONE_SCREEN_FOR_EXISTENCE);
			inputs.put("humanResourcesPhoneScreenId", 1);

			// Create the callback handler to call the DAO method
			ProbeHandler handler = new ProbeHandler();

			// call the DAO method
			BasicDAO.getObject(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, handler);

			// check to see if the DAO call was successful, if not log an error
			if(!handler.isAvailable())
			{
				mLogger.error(new ClientApplLogger("An exception occurred querying the database for existence of the HR_PHN_SCRN table"));
				mMessages.add("An exception occurred querying the database for existence of the HR_PHN_SCRN table");
			} // end if(!handler.isAvailable())
		} // end try
		catch(QueryException qe)
		{
			mLogger.error(new ClientApplLogger("An exception occurred querying the database for existence of the HR_PHN_SCRN table"), qe);
			mMessages.add(qe.getMessage());
		} // end try

		if(mMessages.size() == 0)
		{
			result = UP;
		} // end if(mMessages.size() == 0)

		return result;
	} // end function getResult()

	public String getSubSystem()
	{
		return SUBSYSTEM;
	} // end function getSubSystem()

	public String getSystem()
	{
		return SYSTEM;
	} // end function getSystem()
} // end class ProbeResult
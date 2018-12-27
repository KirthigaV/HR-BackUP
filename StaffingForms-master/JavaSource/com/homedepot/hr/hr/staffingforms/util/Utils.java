package com.homedepot.hr.hr.staffingforms.util;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: SpecialCharacterFilter.java
 * Application: RetailStaffing
 */

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO.HiringEventPacket;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class Utils implements Constants
{
	
	/**
	 * Convenience method to convert a string into a SQL Date if it is not null
	 * 
	 * @param inputString input string to convert
	 * 
	 * @return SQL Date object or null if the string object provided was null
	 */
	public static Date convertToSQLDate(String toConvert)
	{
		java.sql.Date date = null;
		String msgDataVal = null;
		try
		{
			if(toConvert != null)
			{
				SimpleDateFormat dateFormat = new SimpleDateFormat(SQL_DATE_FORMAT);
				java.util.Date parsedDate = dateFormat.parse(toConvert);
				date = new java.sql.Date(parsedDate.getTime());
			}
		}
		catch (Exception e)
		{
			msgDataVal = "Exception : Error occured in Util.convertToSQLDate()";
		}
		return date;
	}
	
	//Start - Added  as part of  Flex to HTML Conversion - 12 May 2015
	
	/**
	 * Description - This method is used to whether the given string is null or empty string
	 * @param string
	 * @return - true/false
	 */
	public static boolean isNullString(String string)
    {
        if (string != null && !"".equals(string.trim()))
        {
            return false;
        }
        return true;
    }

	 /**
     * Description - This method used to convert the object to XML/JSON
     * @param mediaType
     * @param paramObject
     * @return response in JSON/XML format
     */
	 public static String getRequiredFormatRes(String mediaType,Object paramObject){
		String response = null;
		if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_XML))
		{
			/** Serialize the java objects into XML **/
			response = XMLHandler.toXML(paramObject);
		}
		else if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON))
		{
			/** Serialize the java objects into JSON **/
			response=getJsonResponse(paramObject);
		}
		return response;
		
	}
	 
	/**
	 * Description: This method is used convert the given date in MM/dd/yyyy format 
	 * @param date
	 * @return
	 */
	public static String convertDateFormat(Date date)
	{
		SimpleDateFormat sm = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
		return sm.format(date);
	}
    
    /**
	 * Description : This method is used to convert the java objects into JSON object and return the response
	 *
	 * @param paramObject 
	 * 			- the param object
	 * 
	 * @return 
	 * 			- the json response
	 * 
	 * @throws Exception 
	 * 			- the exception
	 */
	public static String getJsonResponse(Object paramObject)
	{
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.autodetectAnnotations(true);
		xstream.setMode(XStream.NO_REFERENCES);			       
		xstream.alias(paramObject.getClass().getSimpleName(),paramObject.getClass());
	    return xstream.toXML(paramObject);
		
	}
	
	/**
	 * Description: This method is used to convert the json String to Object 
	 * 
	 * @param jsonRequest
	 * @param cl
	 * @return Object converted from the given JSON
	 */
	public static Object getObjectFromJson(String jsonRequest, Class<?> cl)
	{
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.processAnnotations(cl);
		xstream.alias(cl.getSimpleName(),cl);
		xstream.aliasField("Applicants", HiringEventPacket.class, "applicants");
		xstream.alias("ApplicantDetails",HiringEventPacket.Applicants.ApplicantDetails.class);
		xstream.addImplicitCollection(HiringEventPacket.Applicants.class, "applicantDetails");
		return xstream.fromXML(jsonRequest );
	}
	
	/**
	 * Description: This method is used to convert the input String(xml/json) to Object
	 * 
	 * @param mediaType
	 * @param request
	 * @param cl
	 * @return Object converted from the given XML/JSON
	 */
	public static Object getObjectFromInput(String mediaType, String request, Class<?> cl){
		if(mediaType!=null && mediaType.equals(MediaType.APPLICATION_JSON)) {
			return getObjectFromJson(request, cl);
		}else{
			return XMLHandler.fromXML(request);
		}
	}
	
	/**
	 * Description: This method is used to remove the get the mediaType text before ';'
	 * @param mediaType
	 * @return mediaType (application/xml or applicattion/json) 
	 */
	public static String getMediaType(String mediaType)
	{
		String mType = mediaType;
		if(mType!=null && mType.contains(SEMI_COLON)){
			mType = mType.split(SEMI_COLON)[0];	
		}
		return mType;
	}
	
	/**
	 * Description: This method is used to check whether a list is null or not.
	 * 
	 * @param list
	 * @return true/false
	 */
	@SuppressWarnings("unchecked")
	public static boolean isNullList(Object obj) {
		boolean isNullList = true;
		if (obj instanceof List) {
			List<Object> list = (List<Object>) obj;
			if (!list.isEmpty()) {
				isNullList = false;
			}
		}
		return isNullList;
	}
	
	/**
	 * Description: This method is used to populate the possible timeslots of the day 5:00 AM to 21:30 PM
	 * @return
	 */
	public static List<String> getTimeSlots()
	{
		List<String> timeSlots = new ArrayList<String>();
		String startText =  "";
		for(int i=5;i<22;i++){
			startText = (i<10) ? ZERO: "";
			timeSlots.add(startText + i + COLON + MINUTES_ZERO);
			timeSlots.add(startText + i + COLON + MINUTES_THIRTY);
		}
		return timeSlots;
	}
	//End - Added  as part of  Flex to HTML Conversion - 12 May 2015
} // end class Utils
package com.homedepot.hr.et.ess.util;

import java.util.Date;

import org.apache.log4j.Logger;

public class JPLogUtil  {


private static final Logger logger = Logger.getLogger(JPLogUtil.class);

/**
 * This method is used to log the general error
 * 
 * @param e
 *            the Exception
 * 
 */
public static void logAppLogError(String classNm,String methodNm,Exception e, String message, int errorCode) {

	StringBuffer errMsgs = new StringBuffer();

	errMsgs.append("\n").append("Exception in Class:"+classNm+";Method:"+methodNm+";Details are : ");
	if (null != e.getMessage() || (!("".equals(e.getMessage())))) {
		errMsgs.append("\n\t" + e.getMessage());
	} else {
		errMsgs.append(e.getLocalizedMessage());
	}

	errMsgs.append("\n\t").append("Timestamp ").append(getCurrentDate());
	logger.fatal(new JPAppLogMessage(errorCode, message+errMsgs.toString()));
	logger.fatal(message+errMsgs.toString(), e);


}

public static void logError(String classNm,String methodNm,Exception e, String message, int errorCode) {

	StringBuffer errMsgs = new StringBuffer();

	errMsgs.append("\n").append("Exception in Class:"+classNm+";Method:"+methodNm+";Details are : ");
	if (null != e.getMessage() || (!("".equals(e.getMessage())))) {
		errMsgs.append("\n\t" + e.getMessage());
	} else {
		errMsgs.append(e.getLocalizedMessage());
	}

	errMsgs.append("\n\t").append("Timestamp ").append(getCurrentDate());
	logger.fatal(message+errMsgs.toString(), e);


}


public static void logInfo(String classNm,String methodNm,String message) {

	StringBuffer infoMsgs = new StringBuffer();

	infoMsgs.append("\n").append("Class:");
	infoMsgs.append(classNm);
	infoMsgs.append(";Method:");
	infoMsgs.append(methodNm);
	infoMsgs.append(";"+message);
	
	logger.info(infoMsgs.toString());


}


/**
 * This method is used to log the general error
 * 
 * @param e
 *            the Exception
 * 
 */
public static void logError(Exception e, int errorCode) {

	StringBuffer errMsgs = new StringBuffer();

	errMsgs.append("\n").append("Exception occurred. Details are : ");
	if (null != e.getMessage() || (!("".equals(e.getMessage())))) {
		errMsgs.append("\n\t" + e.getMessage());
	} else {
		errMsgs.append(e.getLocalizedMessage());
	}

	errMsgs.append("\n\t").append("Timestamp ").append(getCurrentDate());
	logger.fatal(new JPAppLogMessage(errorCode, errMsgs.toString()));
	logger.fatal(errMsgs.toString(), e);


}

public static Date getCurrentDate() {

	Date currentDate = null;
	final String METHOD_NM="getCurrentDate";
	final String CLASS_NM="BCLogUtil";
	try {

		currentDate = new Date(System.currentTimeMillis());

	} catch (Exception e) {

		logError(CLASS_NM,METHOD_NM, e, "Error in getting the current date", 1);

	}

	return currentDate;

}
}


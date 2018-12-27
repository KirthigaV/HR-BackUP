package com.homedepot.hr.hr.retailstaffing.services.formatters;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.log4j.Logger;

/**  
 * Class used to format Exception objects to XML
 */ 
public class ExceptionXMLFormatter {
	
	// Logger instance
	private static final Logger logger = Logger.getLogger(ExceptionXMLFormatter.class);
	
    /** 
     * Convert an Exception object to an XML string
     *
     * @param e the Exception to format 
     */
	public static String formatMessage(Exception e) {
	
	    if(logger.isDebugEnabled()){
            logger.debug("Start formatMessage");
            logger.debug("Exception: '"+e.getMessage()+"'");
        }
				
		StringBuilder sb = new StringBuilder();
		
		sb.append("<Exception>");
		sb.append("<userMessage>").append(e.getMessage()).append("</userMessage>");
		sb.append("  <![CDATA[\n");
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		sb.append(sw.toString());
		
		sb.append("  ]]>");

		sb.append("</Exception>");
		
		if(logger.isDebugEnabled()){
            logger.debug("End formatMessage");
        }
		
		return sb.toString();
	}

}


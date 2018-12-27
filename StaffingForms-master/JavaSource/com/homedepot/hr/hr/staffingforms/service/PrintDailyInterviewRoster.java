package com.homedepot.hr.hr.staffingforms.service;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import com.homedepot.ta.aa.log4j.SimpleExceptionLogger;
import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.bl.PrintApplicationManager;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.service.formatters.ExceptionXMLFormatter;

@Path("/PrintDailyInterviewRoster")
public class PrintDailyInterviewRoster implements Constants, Service
{

    // Logger instance
    private static final Logger logger = Logger.getLogger(PrintDailyInterviewRoster.class);
    
    @GET
    @Path("/getRosterPDF")
    @Produces("text/html")
    	
    public String getRosterPDF(@QueryParam("calendarId") int calendarId, @QueryParam("beginTimeStamp") Timestamp beginTimeStamp, 
    		                   @QueryParam("endTimeStamp") Timestamp endTimeStamp,@QueryParam("hireEventId") int hireEventId,
    		                   @QueryParam("hireEventName") String hireEventName,@Context HttpServletResponse httpResponse,
                               @DefaultValue("1") @QueryParam("version") int version)
    {
        if(logger.isDebugEnabled()){
            logger.debug("Start myMethod");
            logger.debug("version: '"+version+"'");
        }
        
        String result = null;

        try{			
            switch(version){
	            case 1:
	    			if (logger.isDebugEnabled()) {
	    				logger.debug("in version 1");
	    				logger.debug(String.format("Generating PDF for Daily Interview Roster"));
	    			} // end if
                 
	    			byte pdfBytes[] = null;
	    			// invoke the business logic method that builds the Application PDF
                   pdfBytes=PrintApplicationManager.getInterviewRosterPdfView(calendarId,beginTimeStamp,endTimeStamp,hireEventId,hireEventName);
	    			
	    			// set the MIME type on the response to indicate this is a PDF
	    			httpResponse.setContentType(APPLICATION_PDF);
	    			if(pdfBytes!=null)
	    			{
	    			httpResponse.setContentLength(pdfBytes.length);
	    			httpResponse.getOutputStream().write(pdfBytes);
	    			}

                    break;
                default: throw new Exception("Unsupported version: " + version);
            }
        }
        catch(Exception e){
            SimpleExceptionLogger.log(e,"StaffingForms-New");
            result = ExceptionXMLFormatter.formatMessage(e);
        }
        
        if(logger.isDebugEnabled()){
            logger.debug("End myMethod");
        }
        return null;	
    }  
	
 }

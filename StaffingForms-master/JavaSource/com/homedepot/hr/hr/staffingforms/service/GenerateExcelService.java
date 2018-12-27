package com.homedepot.hr.hr.staffingforms.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.homedepot.ta.aa.log4j.SimpleExceptionLogger;
import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.bl.PrintApplicationManager;
import com.homedepot.hr.hr.staffingforms.dto.ExceptionTO;
import com.homedepot.hr.hr.staffingforms.excel.formatters.ExceptionXMLFormatter;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.util.Utils;

@Path("/GenerateExcelService")
public class GenerateExcelService implements Constants
{

    // Logger instance
    private static final Logger logger = Logger.getLogger(GenerateExcelService.class);
    // Commented @Produces(MediaType.APPLICATION_XML) and  Added @Produces and @Consumes, method argument contentType as part of converting object to XML/JSON.
    // as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
    @GET
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
  	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/getExcelView")
	
    public String myMethod(@QueryParam("calendarId") int calendarId, @QueryParam("beginTimeStamp") Timestamp beginTimeStamp, 
            @QueryParam("endTimeStamp") Timestamp endTimeStamp, @QueryParam("hireEventId") int hireEventId,@QueryParam("hireEventName") String hireEventName,@Context HttpServletResponse httpResponse,
            @DefaultValue(MediaType.APPLICATION_XML) @HeaderParam(CONTENT_TYPE) String contentType)
    {
        if(logger.isDebugEnabled()){
            logger.debug("Start myMethod");
        }
        
        String result = null;
        //Added to get the Media Type text before ';' - For Flex to HTML Conversion - 6 June 2015
      	String mediaType = contentType;

        try{	
        	//Added to get the Media Type text before ';' - For Flex to HTML Conversion - 12 May 2015
			mediaType = Utils.getMediaType(mediaType);
			
            if (logger.isDebugEnabled()) {
            	logger.debug(String.format("Generating Excel for Daily Interview Roster"));
			}
            PrintApplicationManager.getInterviewRosterExcelView(calendarId,beginTimeStamp,endTimeStamp,hireEventId,hireEventName,httpResponse);
        }
        catch(Exception e){
            SimpleExceptionLogger.log(e,"StaffingForms-New");
            //Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
            if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_XML)){
            	result = ExceptionXMLFormatter.formatMessage(e);
            }else if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON)){
            	StringWriter sw = new StringWriter();
        		PrintWriter pw = new PrintWriter(sw);
        		pw.write(e.getMessage());
            	
            	ExceptionTO exception = new ExceptionTO();
            	exception.setMessage(sw.toString());
            	
            	result = Utils.getRequiredFormatRes(mediaType, e);
            }
            //End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 12 May 2015
        }
        
        if(logger.isDebugEnabled()){
            logger.debug("result: '"+result+"'");
            logger.debug("End myMethod");
        }
        return result;	
    }  
	
 }

package com.homedepot.hr.hr.staffingforms.service;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.bl.PrintDayViewManager;
import com.homedepot.hr.hr.staffingforms.enumerations.InputField;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;


/**
 *
 * This class is a service for generating a PDF document with Phone interview schedules
 * The first two parameters, store and calendar name, are passed through to the generated PDF without editing and 
 * appear in the header.  Calendar id and date are passed to PrintDayViewManager to retrieve schedule details.
 * Date is formatted and also displayed in the header.
 * 
 * NOTE:  In order for PDF generation to work, xalan-2.7.0.jar and xercesImpl-2.7.1.jar must be included in the project
 * path despite the show errors.
 * 
 * @author CHRIS STANN
 *
 */


@Path("/PrintDayViewService")
public class PrintDayViewService implements Constants, Service {
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(PrintDayViewService.class);
	private static final String ERROR_HTML_FORMAT = "<html><head><title>Retail Staffing Error</title></head><body>%1$s</body></html>";
	

	
	@GET
	@Path("/getPdfView")
	@Produces({APPLICATION_PDF, TEXT_HTML})
	public String getPdfView(@QueryParam("store") String store, @QueryParam("calendarName") String calendarName,@QueryParam("calendarId") int calendarId, @QueryParam("date") Date date, @Context HttpServletResponse httpResponse, @DefaultValue("1") @QueryParam("version") int version)
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getPdfView(), store: %1$s, calendarId: %2$s, date: %3$s, version %4$s", store, calendarId, date, version));
		} // end if
		
		try
		{
			// validate the version provided (currently we only support version 1
			ValidationUtils.validateVersion(version, VERSION1);
			// validate the calendar ID provided			
			ValidationUtils.validateCalendarId(calendarId);
			// validate the date provided is not null
			ValidationUtils.validateNotNull(InputField.DATE, date);
			
			// invoke the business logic method that communicates with the drug test vendor and generates the work order PDF
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("Generating PDF for store, calendarId, date:  %1$s, %2$s, %3$s", store, calendarId, date));
			} // end if
			
			PrintDayViewManager.getPdfView(store, calendarName, calendarId, date, httpResponse.getOutputStream());
			// set the MIME type on the response to indicate this is a PDF
			httpResponse.setContentType(APPLICATION_PDF);
		} // end try
		catch(Exception e)
		{
			// log the error
			mLogger.error(e.getMessage(), e);
			
			try
			{
				// setup the error response
				httpResponse.getOutputStream().print(String.format(ERROR_HTML_FORMAT, e.getMessage()));
				httpResponse.setContentType(TEXT_HTML);
			} // end try
			catch(IOException ioe)
			{
				mLogger.error("An exception occurred writing HTML error page to the response output stream", ioe);
			} // end catch
		} // end catch
		finally
		{
			try
			{
				if(httpResponse.getOutputStream() != null)
				{
					// flush the buffer
					httpResponse.getOutputStream().flush();
					// close the output stream
					httpResponse.getOutputStream().close();
				} // end if(httpResponse.getOutputStream() != null)
			} // end try
			catch(IOException ignore)
			{
				// not much we can do at this point
			} // end catch
		} // end finally
		
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();
			if(startTime == 0){ startTime = endTime; }
			mLogger.debug(String.format("Exiting getPdfView(), version: %1$s. Total time to process request: %2$.9f seconds", 
				version, (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return null;
	} // end function getPdfView() */		
		
		
		
		
		

		
	
	
	
	
	

}

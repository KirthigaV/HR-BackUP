package com.homedepot.hr.hr.staffingforms.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.bl.PrintApplicationManager;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;

/**
 * 
 * This class is a service for generating a PDF document of the Applicants
 * Application using the Phone Screen number as the passed in value to finding
 * the SSN and building the application. NOTE: In order for PDF generation to
 * work, xalan-2.7.0.jar and xercesImpl-2.7.1.jar must be included in the
 * project path despite the show errors.
 * 
 * @author Todd Stephens
 * 
 */

@Path("/PrintApplicationService")
public class PrintApplicationService implements Constants, Service {
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(PrintApplicationService.class);
	private static final String ERROR_HTML_FORMAT = "<html><head><title>Application Print Error</title></head><body>%1$s</body></html>";

	@GET
	@Path("/getAppPdfView")
	@Produces("application/pdf")
	public String getPdfView(@QueryParam("phnScrn") long phnScrn, @QueryParam("intExt") String intExt, @Context HttpServletResponse httpResponse, @DefaultValue("1") @QueryParam("version") int version) {
		long startTime = 0;
		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getAppPdfView(), PhoneScreen:%1$d version:%2$d", phnScrn, version));
		} // end if

		try {
			// validate the version provided (currently we only support version 1,2,3)
			ValidationUtils.validateVersion(version, VERSION1_3);
			// validate the Phone Screen Number provided
			ValidationUtils.validatePhoneScrnNum(phnScrn);
			// validate the Internal / External provided
			if (intExt == null || (!intExt.equalsIgnoreCase("E") && !intExt.equalsIgnoreCase("I"))) {
				if (version == 1) {
					mLogger.debug(String.format("Invalid Internal/External Flag for PhoneScreen: %1$d", phnScrn));
				} else if (version == 2) {
					mLogger.debug(String.format("Invalid Internal/External Flag for applicantId: %1$d", phnScrn));					
				} else if (version == 3) {
					mLogger.debug(String.format("Invalid Internal/External Flag for candRefId: %1$d", phnScrn));					
				}
				throw new Exception("Invalid Internal/External Flag");
			}

			// invoke the business logic method that builds the Application PDF
			if (mLogger.isDebugEnabled()) {
				if (version == 1) {
					mLogger.debug(String.format("Generating PDF for Applicant Phone Screen:  %1$d", phnScrn));
				} else if (version == 2) {
					mLogger.debug(String.format("Generating PDF for Applicant applicantId:  %1$d", phnScrn));					
				} else if (version == 3) {
					mLogger.debug(String.format("Generating PDF for Applicant candRefId:  %1$d", phnScrn));					
				}
			} // end if

			if (intExt.equalsIgnoreCase("E")) {
				//External
				PrintApplicationManager.getExtApplicantPdfView(phnScrn, httpResponse.getOutputStream(), version);
			} else {
				//Internal
				PrintApplicationManager.getIntProfilePdfView(phnScrn, httpResponse.getOutputStream(), version);
			}

			// set the MIME type on the response to indicate this is a PDF
			httpResponse.setContentType(APPLICATION_PDF);
		} // end try
		catch (Exception e) {
			// log the error
			mLogger.error(e.getMessage(), e);

			try {
				// setup the error response
				httpResponse.getOutputStream().print(String.format(ERROR_HTML_FORMAT, e.getMessage()));
				httpResponse.setContentType(TEXT_HTML);
			} // end try
			catch (IOException ioe) {
				mLogger.error("An exception occurred writing HTML error page to the response output stream", ioe);
			} // end catch
		} // end catch
		finally {
			try {
				if (httpResponse.getOutputStream() != null) {
					// flush the buffer
					httpResponse.getOutputStream().flush();
					// close the output stream
					httpResponse.getOutputStream().close();
				} // end if(httpResponse.getOutputStream() != null)
			} // end try
			catch (IOException ignore) {
				// not much we can do at this point
			} // end catch
		} // end finally

		if (mLogger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			mLogger.debug(String.format("Exiting getAppPdfView(), version: %1$s. Total time to process request: %2$.9f seconds", version, (((double) endTime - startTime) / NANOS_IN_SECOND)));
		} // end if

		return null;
	} // end function getPdfView() */

}

package com.homedepot.hr.hr.staffingforms.bl;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.homedepot.hr.hr.staffingforms.dao.handlers.ApplicationDAO;
import com.homedepot.hr.hr.staffingforms.dto.ApplicationData;
import com.homedepot.hr.hr.staffingforms.dto.GenericSchedulePref;
import com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO.dao.ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO;
import com.homedepot.hr.hr.staffingforms.dto.interviewRosterDTO.DailyInterviewRoster;
import com.homedepot.hr.hr.staffingforms.dto.interviewRosterDTO.DailyInterviewRoster.Interview;
import com.homedepot.hr.hr.staffingforms.dto.interviewRosterDTO.DailyInterviewRoster.Interview.Candidate;
import com.homedepot.hr.hr.staffingforms.dto.interviewRosterDTO.dao.ReadHumanResourcesHireEventDTO;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.pdf.PDFHelper;
import com.homedepot.hr.hr.staffingforms.util.HiringEventJAXBTransformer;
import com.homedepot.hr.hr.staffingforms.util.XMLHandler;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class generates PDF document Applicant Applications. It utilizes Apache
 * FOP open source API for PDF generation.
 * 
 * NOTE: In order for PDF generation to work, xalan-2.7.0.jar and
 * xercesImpl-2.7.1.jar must be included in the project path despite the shown
 * errors.
 * 
 * @author Todd Stephens
 */

public class PrintApplicationManager implements Constants, DAOConstants {
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(PrintApplicationManager.class);
	// Location of xsl for Applicant External
	private static final String XSL_FILE_EXT_PATH = "../xsl/app.xsl";
	// Location of xsl from Internal Profile
	private static final String XSL_FILE_INT_PATH = "../xsl/associateProfileReport.xsl";
	// Failure or informational messages used when data can not be returned
	private static final String PDF_ERROR = "An error occurred during generation of this document.";
	private static final String PDF_EMPTY = "No data was found for this document.    ";
	private static final SimpleDateFormat printDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	//Location of xsl for Daily Interview Roster
	private static final String XSL_FILE_INTVW_ROSTER_PATH = "../xsl/INTVW_ROSTER.xsl";
	/**
	 * First, XML is created containing all External Application Data. Second,
	 * an XSL style sheet is read from WEB-INF/xsl. Both XML and XSL are passed
	 * to the PDFHelper helper method which calls the API and returns a byte
	 * array of the PDF document. The byte array then is written to the
	 * OutputStream.
	 * 
	 * @param phoneScrnId
	 *            - used to find the Due to this supporting 3 versions, a long is passed in but the phone screen id only is a int and has to be cast.
	 * @param out
	 *            - OutputStream to which PDF is written
	 * @throws Exception
	 */
	public static void getExtApplicantPdfView(long phoneScrnId, OutputStream out, int version) throws Exception {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			if (version == 1) { 
				mLogger.debug(String.format("Entering PrintExtApplicationManager getPdfView() for Phone Screen:%1$d version:%2$d", phoneScrnId, version));
			} else if (version == 2) {
				mLogger.debug(String.format("Entering PrintExtApplicationManager getPdfView() for applicantId:%1$d version:%2$d", phoneScrnId, version));				
			} else if (version == 3) {
				mLogger.debug(String.format("Entering PrintExtApplicationManager getPdfView() for candRefId:%1$d version:%2$d", phoneScrnId, version));				
			}
		} // end if
		PDFHelper pdf = new PDFHelper();
		String xml = null;
		String xsl = null;
		GenericSchedulePref phoneScreenResponses = null;
		
		try {
			ApplicationData aimsApplication = new ApplicationData();
			// Current Date/Time
			Calendar printDate = Calendar.getInstance();
			aimsApplication.setCurrentTimeStamp(printDateFormat.format(printDate.getTime()));

			if (version == 1) {
				// Get applicantId from Phone Screen Passed
				ApplicationDAO.readHumanResourcesPhoneScreen(Integer.parseInt(Long.toString(phoneScrnId)), aimsApplication);
			} else if (version == 2) {
				//applicantId was passed in, so just set it in the object
				aimsApplication.setApplicantId(Long.valueOf(phoneScrnId).toString());
			} else if (version == 3) {
				//Candidate Id was passed in, so need to left Pad Candidate Id with zeros so that it is a 10 digit number
				String paddedCandRefId = Long.toString(phoneScrnId);
				if (paddedCandRefId.length() < 10) {
					paddedCandRefId = String.format("%010d", phoneScrnId);
				}
				ApplicationDAO.readExternalApplicantId(paddedCandRefId, aimsApplication);
			}

			if (aimsApplication.getApplicantId() == null) {
				mLogger.error(String.format("Unable to get Applicant ID for Phone Screen: %1$s", phoneScrnId));
				throw new Exception("Error occurred retriving Applicant ID.");
			}
			
			//Set the requisition the applicant is attached to, if not attached then requisitionAttachedTo will be null
			ApplicationDAO.readHumanResourcesStoreRequisitionCandidateByInputList(aimsApplication.getApplicantId(), aimsApplication);			

			//If the applicant is attached to a requisition then if they have a phone screen
			if (aimsApplication.getRequisitionAttachedTo() != null) {
				//Set the phone screen id and the phone screen status so that requisition specific answers can be fetched
				ApplicationDAO.readRequisitionPhoneScreen(aimsApplication.getApplicantId(), Integer.parseInt(aimsApplication.getRequisitionAttachedTo()), aimsApplication);

				//Applicant is attached and has a completed phone screen, get the responses 
				//Changed for FMS 7894 January 2016 CR's, removed the Phone Screen Complete status
				if (aimsApplication.getPhoneScreenId() > 0 /*&& aimsApplication.getPhoneScreenStatusCode() == 4*/) {
						phoneScreenResponses = ApplicationDAO.getPhoneScreenScheduleResponses(Integer.parseInt(Long.toString(aimsApplication.getPhoneScreenId())));					
				}
			}
					
			// Get Applicant Personal Data
			ApplicationDAO.getApplPersonalInfo(aimsApplication.getApplicantId(), aimsApplication);

			// When no name is returned, could mean that the Internal/External
			// Flag is incorrect.
			if (aimsApplication.getName() == null) {
				if (version == 1) {
					mLogger.error(String.format("No Name for Phone Screen: %1$s", phoneScrnId));
				} else if (version == 2) {
					mLogger.error(String.format("No Name for applicantId: %1$s", phoneScrnId));
				} else if (version == 3) {
					mLogger.error(String.format("No Name for candidateRefId: %1$s", phoneScrnId));
				}
				throw new Exception("Error occurred retriving Applicant Data.");
			}

			// Get Available Times
			ApplicationDAO.getApplJobPrefInfo(aimsApplication.getApplicantId(), aimsApplication);

			//Rework Applicant Availability using Phone Screen Responses.  This only applies to phone screens that have more than 10 minimum requirements
			if (phoneScreenResponses != null) {
				updateExternalApplicantBasedOnPhnScrnResponses(phoneScreenResponses, aimsApplication);
			}
			
			// Get Language Codes
			ApplicationDAO.getApplLangInfo(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Education
			ApplicationDAO.getApplEducationInfo(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Work History
			ApplicationDAO.getApplWorkHistroyInfo(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Positions Applied For
			ApplicationDAO.getApplPositionsAppliedFor(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Stores Applied For
			ApplicationDAO.getApplStoresAppliedFor(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			xml = XMLHandler.toXML(aimsApplication);
			String path = XSL_FILE_EXT_PATH;
			xsl = getXsl(pdf, path);

			if (xsl == null || xsl.length() < 1) {
				if (mLogger.isDebugEnabled()) {
					startTime = System.nanoTime();
					mLogger.error(String.format("XSL not present for Ext Application"));
				} // end if
			}

			byte pdfBytes[] = null;
			pdfBytes = pdf.createPdf(xml, xsl);
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("XML: %1$s", xml));
				
			}
			if (pdfBytes != null) {

				out.write(pdfBytes);
			}
		} catch (QueryException qe) {
			startTime = System.nanoTime();
			mLogger.error(String.format("QueryException in getExtApplicantPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(qe.getMessage(), qe);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw new Exception("Error occurred retriving data for Application.");
		} catch (Exception e) {
			startTime = System.nanoTime();
			mLogger.error(String.format("Exception in getExtApplicantPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(e.getMessage(), e);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw e;
		}

	}

	private static void updateExternalApplicantBasedOnPhnScrnResponses(GenericSchedulePref phoneScreenResponses, final ApplicationData aimsApplication) throws Exception {
		//Made Changes for FMS 7894 January 2016 CR's
		//Weekdays
		if (phoneScreenResponses.getWeekdays() != null && phoneScreenResponses.getWeekdays().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setWeekdays("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setWeekdays("N");
			}
		}
		
		//Saturday
		if (phoneScreenResponses.getSaturday() != null && phoneScreenResponses.getSaturday().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setSaturday("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setSaturday("N");
			}			
		}
		
		//Sunday
		if (phoneScreenResponses.getSunday() != null && phoneScreenResponses.getSunday().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setSunday("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setSunday("N");
			}
		}

		//Early AM
		if (phoneScreenResponses.getEarlyAm() != null && phoneScreenResponses.getEarlyAm().equals("N")) {			
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setEarlyAm("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setEarlyAm("N");
			}
		}				
		
		//Mornings
		if (phoneScreenResponses.getMornings() != null && phoneScreenResponses.getMornings().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setMornings("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setMornings("N");
			}
		}
		
		//Afternoons
		if (phoneScreenResponses.getAfternoons() != null && phoneScreenResponses.getAfternoons().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setAfternoons("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setAfternoons("N");
			}
		}				
		
		//Nights
		if (phoneScreenResponses.getNights() != null && phoneScreenResponses.getNights().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setNights("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setNights("N");
			}
		}				
		
		//Late Night
		if (phoneScreenResponses.getLateNight() != null && phoneScreenResponses.getLateNight().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setLateNight("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setLateNight("N");
			}
		}				
		
		//Overnight
		if (phoneScreenResponses.getOvernight() != null && phoneScreenResponses.getOvernight().equals("N")) {
			if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y"))
			{
				aimsApplication.setOvernight("X");
			}
			else if (phoneScreenResponses.getReasonableAccommodationRequested() != null)
			{
				aimsApplication.setOvernight("N");
			}
		}				
		
		//Set reasonableAccommodationRequested flag
		if (phoneScreenResponses.getReasonableAccommodationRequested() != null && phoneScreenResponses.getReasonableAccommodationRequested().equals("Y")) {
			aimsApplication.setReasonableAccommodationRequested("Y");
		}		
	
	}
	
	/**
	 * First, XML is created containing all Internal Profile Data. Second, an
	 * XSL style sheet is read from WEB-INF/xsl. Both XML and XSL are passed to
	 * the PDFHelper helper method which calls the API and returns a byte array
	 * of the PDF document. The byte array then is written to the OutputStream.
	 * 
	 * @param phoneScrnId
	 *            - used to find the.  Due to this supporting 3 versions, a long is passed in but the phone screen id only is a int and has to be cast.
	 * @param out
	 *            - OutputStream to which PDF is written
	 *            
	 * @param version - Version 1 Phone Screen ID, Version 2 applicantId, Version 3 candidateRefId 
	 *           
	 * @throws Exception
	 */
	public static void getIntProfilePdfView(long phoneScrnId, OutputStream out, int version) throws Exception {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			if (version == 1) {
				mLogger.debug(String.format("Entering PrintExtApplicationManager getIntProfilePdfView() for Phone Screen: %1$d", phoneScrnId));
			} else if (version == 2) {
				mLogger.debug(String.format("Entering PrintExtApplicationManager getIntProfilePdfView() for applicantId: %1$d", phoneScrnId));				
			} else if (version == 3) {
				mLogger.debug(String.format("Entering PrintExtApplicationManager getIntProfilePdfView() for candidateRefId: %1$d", phoneScrnId));				
			}
		} // end if
		PDFHelper pdf = new PDFHelper();
		String xml = null;
		String xsl = null;

		try {
			ApplicationData aimsApplication = new ApplicationData();
			// Current Date/Time
			Calendar printDate = Calendar.getInstance();
			aimsApplication.setCurrentTimeStamp(printDateFormat.format(printDate.getTime()));

			if (version == 1) {
				// Get applicantId from Phone Screen Passed
				ApplicationDAO.readHumanResourcesPhoneScreen(Integer.parseInt(Long.toString(phoneScrnId)), aimsApplication);
			} else if (version == 2) {
				//applicantId was passed in, so just set it in the object
				aimsApplication.setApplicantId(Long.valueOf(phoneScrnId).toString());
			} else if (version == 3) {
				//Candidate Id was passed in, so need to left Pad Candidate Id with zeros so that it is a 10 digit number
				String paddedCandRefId = Long.toString(phoneScrnId);
				if (paddedCandRefId.length() < 10) {
					paddedCandRefId = String.format("%010d", phoneScrnId);
				}
				ApplicationDAO.readInternalApplicantId(paddedCandRefId, aimsApplication);
			}
			
			if (aimsApplication.getApplicantId() == null) {
				mLogger.error(String.format("Unable to get  for Phone Screen: %1$s", phoneScrnId));
				throw new Exception("Error occurred retriving Applicant ID.");
			}

			// Get Associate Info
			ApplicationDAO.getAssociateInfo(aimsApplication.getApplicantId(), aimsApplication);

			// When no name is returned, could mean that the Internal/External
			// Flag is incorrect.
			if (aimsApplication.getName() == null) {
				if (version == 1) {
					mLogger.error(String.format("No Name for Phone Screen: %1$s", phoneScrnId));
				} else if (version == 2) {
					mLogger.error(String.format("No Name for applicantId: %1$s", phoneScrnId));
				} else if (version == 3) {
					mLogger.error(String.format("No Name for candidateRefId: %1$s", phoneScrnId));
				}
				throw new Exception("Error occurred retriving Applicant Data.");
			}

			// Get Associate Job Preferences
			ApplicationDAO.getAssoJobPref(aimsApplication.getApplicantId(), aimsApplication);

			// Get Associate Review Results
			ApplicationDAO.getAssociateReview(aimsApplication.getApplicantId(), aimsApplication);

			// Get Associate Previous Positions
			ApplicationDAO.getAssociatePrevPosition(aimsApplication.getApplicantId(), aimsApplication);

			xml = XMLHandler.toXML(aimsApplication);
			String path = XSL_FILE_INT_PATH;
			xsl = getXsl(pdf, path);

			if (xsl == null || xsl.length() < 1) {
				if (mLogger.isDebugEnabled()) {
					startTime = System.nanoTime();
					mLogger.error(String.format("XSL not present for Int Application"));
				} // end if
			}

			byte pdfBytes[] = null;
			pdfBytes = pdf.createPdf(xml, xsl);
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("XML: %1$s", xml));
			}
			if (pdfBytes != null) {

				out.write(pdfBytes);
			}
		} catch (QueryException qe) {
			startTime = System.nanoTime();
			mLogger.error(String.format("QueryException in getIntApplicantPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(qe.getMessage(), qe);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw new Exception("Error occurred retriving data for Application.");
		} catch (Exception e) {
			startTime = System.nanoTime();
			mLogger.error(String.format("Exception in getIntApplicantPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(e.getMessage(), e);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw e;
		}

	}

	/**
	 * A convenience method for returning a String containing the XSL style
	 * sheet. It leverages an instance of PDFHelper to read the style sheet at
	 * value in XSL_FILE_PATH class constant.
	 * 
	 * @param pdf
	 * @return
	 * @throws Exception
	 */
	private static String getXsl(PDFHelper pdf, String filePath) throws Exception {
		long startTime = 0;
		String xsl = null;
		xsl = pdf.getXMLString(filePath);

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Read XSL: %1$s", xsl));
		} // end if
		return xsl;
	}
	
	/**
	 * First, XML is created containing all External Application Data. Second,
	 * an XSL style sheet is read from WEB-INF/xsl. Both XML and XSL are passed
	 * to the PDFHelper helper method which calls the API and returns a byte
	 * array of the PDF document. The byte array then is written to the
	 * OutputStream.
	 * 
	 * @param phoneScrnId
	 *            - used to find the 
	 * @param out
	 *            - returns the pdfBytes generated
	 * @throws Exception
	 */
	public static byte[] getExtApplicantHiringEventPdfView(int phoneScrnId, OutputStream out, boolean mask) throws Exception {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering PrintExtApplicationManager getExtApplicantHiringEventPdfView() for Phone Screen: %1$d", phoneScrnId));
		} // end if
		PDFHelper pdf = new PDFHelper();
		String xml = null;
		String xsl = null;
		GenericSchedulePref phoneScreenResponses = null;
		
		try {
			ApplicationData aimsApplication = new ApplicationData();
			// Current Date/Time
			Calendar printDate = Calendar.getInstance();
			aimsApplication.setCurrentTimeStamp(printDateFormat.format(printDate.getTime()));

			// Get  from Phone Screen Passed
			ApplicationDAO.readHumanResourcesPhoneScreen(phoneScrnId, aimsApplication);

			if (aimsApplication.getApplicantId() == null) {
				mLogger.error(String.format("Unable to get  for Phone Screen: %1$s", phoneScrnId));
				throw new Exception("Error occurred retriving Applicant ID.");
			}

			//Set the requisition the applicant is attached to, if not attached then requisitionAttachedTo will be null
			ApplicationDAO.readHumanResourcesStoreRequisitionCandidateByInputList(aimsApplication.getApplicantId(), aimsApplication);			

			//If the applicant is attached to a requisition then if they have a phone screen
			if (aimsApplication.getRequisitionAttachedTo() != null) {
				//Set the phone screen id and the phone screen status so that requisition specific answers can be fetched
				ApplicationDAO.readRequisitionPhoneScreen(aimsApplication.getApplicantId(), Integer.parseInt(aimsApplication.getRequisitionAttachedTo()), aimsApplication);

				//Applicant is attached and has a completed phone screen, get the responses 
				if (aimsApplication.getPhoneScreenId() > 0 && aimsApplication.getPhoneScreenStatusCode() == 4) {
						phoneScreenResponses = ApplicationDAO.getPhoneScreenScheduleResponses(Integer.parseInt(Long.toString(aimsApplication.getPhoneScreenId())));					
				}
			}
			
			// Get Applicant Personal Data
			ApplicationDAO.getApplPersonalInfo(aimsApplication.getApplicantId(), aimsApplication);

			// When no name is returned, could mean that the Internal/External
			// Flag is incorrect.
			if (aimsApplication.getName() == null) {
				mLogger.error(String.format("No Name for Phone Screen: %1$s", phoneScrnId));
				throw new Exception("Error occurred retriving Applicant Data.");
			}

			// Get Available Times
			ApplicationDAO.getApplJobPrefInfo(aimsApplication.getApplicantId(), aimsApplication);

			//Rework Applicant Availability using Phone Screen Responses.  This only applies to phone screens that have more than 10 minimum requirements
			if (phoneScreenResponses != null) {
				updateExternalApplicantBasedOnPhnScrnResponses(phoneScreenResponses, aimsApplication);
			}
			
			// Get Language Codes
			ApplicationDAO.getApplLangInfo(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Education
			ApplicationDAO.getApplEducationInfo(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Work History
			ApplicationDAO.getApplWorkHistroyInfo(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Positions Applied For
			ApplicationDAO.getApplPositionsAppliedFor(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);

			// Get Stores Applied For
			ApplicationDAO.getApplStoresAppliedFor(aimsApplication.getApplicantId(), aimsApplication.getApplicationDate(), aimsApplication);
			
			xml = XMLHandler.toXML(aimsApplication);
			String path = XSL_FILE_EXT_PATH;
			xsl = getXsl(pdf, path);

			if (xsl == null || xsl.length() < 1) {
				if (mLogger.isDebugEnabled()) {
					startTime = System.nanoTime();
					mLogger.error(String.format("XSL not present for Ext Application"));
				} // end if
			}

			byte pdfBytes[] = null;
			pdfBytes = pdf.createPdf(xml, xsl);
			pdf.createPdf(xml, xsl);
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("XML: %1$s", xml));
			}

			return pdfBytes;
		} catch (QueryException qe) {
			startTime = System.nanoTime();
			mLogger.error(String.format("QueryException in getExtApplicantHiringEventPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(qe.getMessage(), qe);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw new Exception("Error occurred retriving data for Application.");
		} catch (Exception e) {
			startTime = System.nanoTime();
			mLogger.error(String.format("Exception in getExtApplicantHiringEventPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(e.getMessage(), e);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw e;
		}

	}

	/**
	 * First, XML is created containing all Internal Profile Data. Second, an
	 * XSL style sheet is read from WEB-INF/xsl. Both XML and XSL are passed to
	 * the PDFHelper helper method which calls the API and returns a byte array
	 * of the PDF document. The byte array then is written to the OutputStream.
	 * 
	 * @param phoneScrnId
	 *            - used to find the 
	 * @param out
	 *            - OutputStream to which PDF is written
	 * @throws Exception
	 */
	public static byte[] getIntProfileHiringEventPdfView(int phoneScrnId, OutputStream out, boolean mask) throws Exception {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering PrintExtApplicationManager getIntProfileHiringEventPdfView() for Phone Screen: %1$d", phoneScrnId));
		} // end if
		PDFHelper pdf = new PDFHelper();
		String xml = null;
		String xsl = null;

		try {
			ApplicationData aimsApplication = new ApplicationData();
			// Current Date/Time
			Calendar printDate = Calendar.getInstance();
			aimsApplication.setCurrentTimeStamp(printDateFormat.format(printDate.getTime()));

			// Get  from Phone Screen Passed
			ApplicationDAO.readHumanResourcesPhoneScreen(phoneScrnId, aimsApplication);

			if (aimsApplication.getApplicantId() == null) {
				mLogger.error(String.format("Unable to get  for Phone Screen: %1$s", phoneScrnId));
				throw new Exception("Error occurred retriving Applicant ID.");
			}

			// Get Associate Info
			ApplicationDAO.getAssociateInfo(aimsApplication.getApplicantId(), aimsApplication);

			// When no name is returned, could mean that the Internal/External
			// Flag is incorrect.
			if (aimsApplication.getName() == null) {
				mLogger.error(String.format("No Name for Phone Screen: %1$s", phoneScrnId));
				throw new Exception("Error occurred retriving Applicant Data.");
			}

			// Get Associate Job Preferences
			ApplicationDAO.getAssoJobPref(aimsApplication.getApplicantId(), aimsApplication);

			// Get Associate Review Results
			ApplicationDAO.getAssociateReview(aimsApplication.getApplicantId(), aimsApplication);

			// Get Associate Previous Positions
			ApplicationDAO.getAssociatePrevPosition(aimsApplication.getApplicantId(), aimsApplication);

			//Mask the  for Availability Application, Interview Packets.  Don't need to check for null ssn, it is done above.
			/*if (aimsApplication.getApplicantId().length() == 9) {
				if (mask) {
					String masked = "xxx-xx-" + aimsApplication.getApplicantId().substring(5);
					aimsApplication.setApplicantId(masked);
				}
			} else {
				//Bad  length, just mask it all.
				String masked = "xxx-xx-xxxx";
				aimsApplication.setApplicantId(masked);				
			}*/
			
			xml = XMLHandler.toXML(aimsApplication);
			String path = XSL_FILE_INT_PATH;
			xsl = getXsl(pdf, path);

			if (xsl == null || xsl.length() < 1) {
				if (mLogger.isDebugEnabled()) {
					startTime = System.nanoTime();
					mLogger.error(String.format("XSL not present for Ext Application"));
				} // end if
			}

			byte pdfBytes[] = null;
			pdfBytes = pdf.createPdf(xml, xsl);
			pdf.createPdf(xml, xsl);
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("XML: %1$s", xml));
			}
			return pdfBytes;
		} catch (QueryException qe) {
			startTime = System.nanoTime();
			mLogger.error(String.format("QueryException in getIntProfileHiringEventPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(qe.getMessage(), qe);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw new Exception("Error occurred retriving data for Application.");
		} catch (Exception e) {
			startTime = System.nanoTime();
			mLogger.error(String.format("Exception in getIntProfileHiringEventPdfView() for Phone Screen: %1$s", phoneScrnId));
			mLogger.error(e.getMessage(), e);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw e;
		}
	}	
	
	/**
	 * First, XML is created with the Input Data. Second,
	 * an XSL style sheet is read from WEB-INF/xsl. Both XML and XSL are passed
	 * to the PDFHelper helper method which calls the API and returns a byte
	 * array of the PDF document. The byte array then is written to the
	 * OutputStream.
	 * 
	 * 
	 * @param out
	 *            - OutputStream to which PDF is written
	 * @throws Exception
	 */
	public static byte[] getInterviewRosterPdfView(int calendarId,Timestamp beginTimeStamp,Timestamp endTimeStamp,int hireEventId,String hireEventName) throws Exception {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering PrintExtApplicationManager getInterviewRosterPdfView()"));
		} // end if
		PDFHelper pdf = new PDFHelper();
		String xml = null;
		String xsl = null;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
         try {
			byte pdfBytes[] = null;
			DailyInterviewRoster dailyInterviewRoster= new DailyInterviewRoster();
			Interview interview= new Interview();
			List<ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO> reqDetails=ApplicationDAO.readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetails(calendarId,beginTimeStamp,endTimeStamp);
			String[] candIdList = new String[reqDetails.size()];
			int candIdPos=0;
			for(ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO rosterDetails: reqDetails)
			{
				String interviewTime=null;
				String finalInterviewTime=null;
				if(rosterDetails.getBeginTimestamp()!=null)
				{
					interviewTime=rosterDetails.getBeginTimestamp().getHours()+":"+rosterDetails.getBeginTimestamp().getMinutes();
					finalInterviewTime = timeFormat.format(rosterDetails.getBeginTimestamp());
				}
					
				if(interviewTime!=null)
				{
				boolean grpCandidate=false;
				for (int j = 0; j < candIdList.length; j++) {
					if(interviewTime.equals(candIdList[j]))	
					{
						grpCandidate=true;
					}
				}
				
				Candidate candidate= new Candidate();
				if(rosterDetails.getCandidatePhoneNumber()!=null)
				candidate.setCandidatePhNo(rosterDetails.getCandidatePhoneNumber());
				if(rosterDetails.getCandidateName()!=null)
				candidate.setCandidateName(rosterDetails.getCandidateName());
				
				//Added for CDP
				if(rosterDetails.getCandRefNbr()!=null)
					candidate.setCandRefNbr(rosterDetails.getCandRefNbr().trim());
				
				if(rosterDetails.getHumanResourcesPhoneScreenId()!=null)
				candidate.setRSCPhoneScreenNo(String.valueOf(rosterDetails.getHumanResourcesPhoneScreenId()));
				if(rosterDetails.getElectronicMailAddressText()!=null)
				candidate.setCandidateEmail(rosterDetails.getElectronicMailAddressText());
				if(rosterDetails.getEmploymentRequisitionNumber()>0)
				candidate.setRequisitionNumber(String.valueOf(rosterDetails.getEmploymentRequisitionNumber()));
				if(rosterDetails.getHumanResourcesSystemStoreNumber()!=null)
				candidate.setStoreNo(rosterDetails.getHumanResourcesSystemStoreNumber());
				if(rosterDetails.getHumanResourcesSystemDepartmentNumber()!=null)
				candidate.setDepartmentNo(rosterDetails.getHumanResourcesSystemDepartmentNumber());
				if(rosterDetails.getJobTitleCode()!=null)
				candidate.setJobCode(rosterDetails.getJobTitleCode());
				candidate.setDate(dateFormat.format(rosterDetails.getBeginTimestamp()));
				candidate.setTime(timeFormat.format(rosterDetails.getBeginTimestamp()));
				
				if(grpCandidate)
				{
					interview.getCandidate().add(candidate);
				}
				
				else
				{
					if(interview.getTimeSlot()!=null)
					{
					dailyInterviewRoster.getInterview().add(interview);
					}
					interview= new Interview();
					interview.setTimeSlot(finalInterviewTime);
					interview.getCandidate().add(candidate);
					candIdList[candIdPos]=interviewTime;
					candIdPos++;
				}
				
				
			}
			}
			dailyInterviewRoster.getInterview().add(interview);
			
			// Current Date/Time
			Calendar printDate = Calendar.getInstance();
			dailyInterviewRoster.setPrintedDate(printDateFormat.format(printDate.getTime()));
			
			//Fetch HiringEvent Location Details from HireEvent Id
			ReadHumanResourcesHireEventDTO readHumanResourcesHireEventDTO=ApplicationDAO.readHumanResourcesHireEvent(hireEventId);
			dailyInterviewRoster.setHiringEventName(hireEventName);
			dailyInterviewRoster.setLocation(readHumanResourcesHireEventDTO.getHireEventLocationDescription());
			dailyInterviewRoster.setAddress(readHumanResourcesHireEventDTO.getHireEventAddressText());
			dailyInterviewRoster.setCity(readHumanResourcesHireEventDTO.getHireEventCityName());
			dailyInterviewRoster.setStateCd(readHumanResourcesHireEventDTO.getHireEventStateCode());
			dailyInterviewRoster.setZipCd(readHumanResourcesHireEventDTO.getHireEventZipCodeCode());
			
			if(dailyInterviewRoster!=null && dailyInterviewRoster.getInterview().size()>0)
			{
			xml = HiringEventJAXBTransformer.marshall(
					dailyInterviewRoster, DailyInterviewRoster.class);
			mLogger.debug("ROSTER XML"+xml);
			
			
         	
         	String path = XSL_FILE_INTVW_ROSTER_PATH;
			xsl = getXsl(pdf, path);
			
			if (xsl == null || xsl.length() < 1) {
				if (mLogger.isDebugEnabled()) {
					startTime = System.nanoTime();
					mLogger.error(String.format("XSL not present for Roster Creation"));
				} // end if
			}

			
			pdfBytes = pdf.createPdf(xml, xsl);
			
			
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("XML: %1$s", xml));
				
            }
			}
			return pdfBytes;
			
		} catch (QueryException qe) {
			startTime = System.nanoTime();
			mLogger.error(String.format("QueryException in getInterviewRosterPdfView()"));
			mLogger.error(qe.getMessage(), qe);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw new Exception("Error occurred retriving data for Application.");
		} catch (Exception e) {
			startTime = System.nanoTime();
			mLogger.error(String.format("Exception in getInterviewRosterPdfView()"));
			mLogger.error(e.getMessage(), e);
			// We want to log it here and pass the buck, so that the calling
			// service can generate a proper error message to the user
			throw e;
		}

	}
	
	/**
	 * This method is used for generating Excel sheet by fetching values from DAO
	 * 
	 * 
	 * @param out
	 *            - OutputStream to which PDF is written
	 * @throws Exception
	 */
	public static void getInterviewRosterExcelView(int calendarId,Timestamp beginTimeStamp,Timestamp endTimeStamp,int hireEventId,String hireEventName,HttpServletResponse httpResponse) throws Exception {
		long startTime = 0;

		if (mLogger.isDebugEnabled()) {
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering PrintExtApplicationManager getInterviewRosterExcelView()"));
		} // end if
		
		String xml = null;
		String xsl = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

       try {
			byte pdfBytes[] = null;
			Calendar printDate = Calendar.getInstance();
			DailyInterviewRoster dailyInterviewRoster= new DailyInterviewRoster();
			Interview interview= new Interview();
			List<ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO> reqDetails=ApplicationDAO.readHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetails(calendarId,beginTimeStamp,endTimeStamp);
			String[] candIdList = new String[reqDetails.size()];
			int candIdPos=0;
			for(ReadHumanResourceRequisitionSchedulePhoneScreenStoreEmploymentRequisitionDetailsDTO rosterDetails: reqDetails)
			{
				String interviewTime=null;
				String finalInterviewTime=null;
				
				if(rosterDetails.getBeginTimestamp()!=null)
				{
					interviewTime=rosterDetails.getBeginTimestamp().getHours()+":"+rosterDetails.getBeginTimestamp().getMinutes();
					finalInterviewTime = timeFormat.format(rosterDetails.getBeginTimestamp());
				}
				
				if(interviewTime!=null)
				{
				boolean grpCandidate=false;
				for (int j = 0; j < candIdList.length; j++) {
					if(interviewTime.equals(candIdList[j]))	
					{
						grpCandidate=true;
					}
				}
				
				Candidate candidate= new Candidate();
				if(rosterDetails.getCandidatePhoneNumber()!=null)
				candidate.setCandidatePhNo(rosterDetails.getCandidatePhoneNumber());
				if(rosterDetails.getCandidateName()!=null)
				candidate.setCandidateName(rosterDetails.getCandidateName());
			
				//Added for CDP
				if (rosterDetails.getCandRefNbr() != null) {
					candidate.setCandRefNbr(rosterDetails.getCandRefNbr());
				}
				
				if(rosterDetails.getHumanResourcesPhoneScreenId()!=null)
				candidate.setRSCPhoneScreenNo(String.valueOf(rosterDetails.getHumanResourcesPhoneScreenId()));
				if(rosterDetails.getElectronicMailAddressText()!=null)
				candidate.setCandidateEmail(rosterDetails.getElectronicMailAddressText());
				if(rosterDetails.getEmploymentRequisitionNumber()>0)
				candidate.setRequisitionNumber(String.valueOf(rosterDetails.getEmploymentRequisitionNumber()));
				if(rosterDetails.getHumanResourcesSystemStoreNumber()!=null)
				candidate.setStoreNo(rosterDetails.getHumanResourcesSystemStoreNumber());
				if(rosterDetails.getHumanResourcesSystemDepartmentNumber()!=null)
				candidate.setDepartmentNo(rosterDetails.getHumanResourcesSystemDepartmentNumber());
				if(rosterDetails.getJobTitleCode()!=null)
				candidate.setJobCode(rosterDetails.getJobTitleCode());
				candidate.setDate(dateFormat.format(rosterDetails.getBeginTimestamp()));
				candidate.setTime(timeFormat.format(rosterDetails.getBeginTimestamp()));
				
				if(grpCandidate)
				{
					interview.getCandidate().add(candidate);
				}
				
				else
				{
					if(interview.getTimeSlot()!=null)
					{
					dailyInterviewRoster.getInterview().add(interview);
					}
					interview= new Interview();
					interview.setTimeSlot(finalInterviewTime);
					interview.getCandidate().add(candidate);
					candIdList[candIdPos]=interviewTime;
					candIdPos++;
				}
				
				
			}
			}
			dailyInterviewRoster.getInterview().add(interview);
			
			//Fetch HiringEvent Location Details from HireEvent Id
			ReadHumanResourcesHireEventDTO readHumanResourcesHireEventDTO=ApplicationDAO.readHumanResourcesHireEvent(hireEventId);
			dailyInterviewRoster.setHiringEventName(hireEventName);
			dailyInterviewRoster.setLocation(readHumanResourcesHireEventDTO.getHireEventLocationDescription());
			dailyInterviewRoster.setAddress(readHumanResourcesHireEventDTO.getHireEventAddressText());
			dailyInterviewRoster.setCity(readHumanResourcesHireEventDTO.getHireEventCityName());
			dailyInterviewRoster.setStateCd(readHumanResourcesHireEventDTO.getHireEventStateCode());
			dailyInterviewRoster.setZipCd(readHumanResourcesHireEventDTO.getHireEventZipCodeCode());
			
			if(dailyInterviewRoster!=null && dailyInterviewRoster.getInterview().size()>0)
			{
			xml = HiringEventJAXBTransformer.marshall(
					dailyInterviewRoster, DailyInterviewRoster.class);
			mLogger.debug("ROSTER XML"+xml);
			
			createExcel(xml,httpResponse);
			if (mLogger.isDebugEnabled()) {
				mLogger.debug(String.format("XML: %1$s", xml));
				
            }
			}
			
            } 

       catch (Exception e) {
			startTime = System.nanoTime();
			mLogger.error(String.format("Exception in getInterviewRosterExcelView()"));
			mLogger.error(e.getMessage(), e);
			throw e;
		}

	}
	
	private static HSSFWorkbook workbook = new HSSFWorkbook();
	//private static HSSFSheet worksheet = workbook.createSheet("Interview Details");
	private static HSSFSheet worksheet =null;
	
	private static String hiringEventNameElement=null;
	private static String locationElement=null;
	private static String addressElement=null;
	private static String cityElement=null;
	private static String StateCdElement=null;
	private static String ZipCdElement=null;
	
	private static String intvwDate=null;
	private static String intvwTime=null;
	private static String rscPhnScrnNo=null;
	private static String candidateName=null;
	private static String candRefNbr=null;
	private static String candidatePhNo=null;
	private static String candidateEmail=null;
	private static String reqNo=null;
	private static String storeNo=null;
	private static String departmentNo=null;
	private static String jobCode=null;

	private static int rowNum;
	
	public static void createExcel(String XML,HttpServletResponse httpResponse)
	{
		rowNum=7;
		String inputXML=XML;
		workbook = new HSSFWorkbook();
		worksheet= workbook.createSheet("Interview Details");
		
			try {
	        	 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	            //Document doc = docBuilder.parse(new File("C://workspaces_3.7//workspace//StaffingForms-New//WebContent//WEB-INF//xsl//employee.xml"));
	            Document doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(inputXML.getBytes("utf-8"))));
	            
	            
	            // normalize text representation
	            doc.getDocumentElement().normalize();
	            System.out.println("Root element of the doc is :\" "+ doc.getDocumentElement().getNodeName() + "\"");
	            NodeList dailyInterviewRosterTag = doc.getElementsByTagName("DailyInterviewRoster");
	            int totalPersons = dailyInterviewRosterTag.getLength();
	            System.out.println("Total no of nodes : " + totalPersons);
	            for (int s = 0; s < dailyInterviewRosterTag.getLength(); s++) 
	            {
	                Node dailyInterviewRosterNode = dailyInterviewRosterTag.item(s);
	                if (dailyInterviewRosterNode.getNodeType() == Node.ELEMENT_NODE) 
	                {
	                    Element dailyInterviewRosterElement = (Element) dailyInterviewRosterNode;
	                    
	                    NodeList hiringEventNameList = dailyInterviewRosterElement.getElementsByTagName("HiringEventName");
	                    if(hiringEventNameList!=null && hiringEventNameList.item(0) !=null && hiringEventNameList.item(0).getFirstChild()!=null)
	                    hiringEventNameElement =  hiringEventNameList.item(0).getFirstChild().getNodeValue();
	                    System.out.println("HiringEventName : "+hiringEventNameElement);
//	                    NodeList textHEList = hiringEventNameElement.getChildNodes();
//	                    System.out.println("HiringEventName: "+ ((Node) textHEList.item(0)).getNodeValue().trim());
	                    //firstNames.add(((Node) textHEList.item(0)).getNodeValue().trim());
	                    NodeList location = dailyInterviewRosterElement.getElementsByTagName("Location");
	                    if(location!=null && location.item(0)!=null && location.item(0).getFirstChild()!=null)
	                    locationElement = location.item(0).getFirstChild().getNodeValue();
//	                    NodeList textLNList = lastNameElement.getChildNodes();
//	                    System.out.println("Location:"+ ((Node) textLNList.item(0)).getNodeValue().trim());
//	                    lastNames.add(((Node) textLNList.item(0)).getNodeValue().trim());
	                    NodeList address = dailyInterviewRosterElement.getElementsByTagName("Address");
	                    if(null!=address && null!=address.item(0) && null!=address.item(0).getFirstChild())
	                    addressElement = address.item(0).getFirstChild().getNodeValue();
	                    NodeList city = dailyInterviewRosterElement.getElementsByTagName("City");
	                    if(null!=city && null!=city.item(0) && null!=city.item(0).getFirstChild())
	                    cityElement = city.item(0).getFirstChild().getNodeValue();
	                    NodeList StateCd = dailyInterviewRosterElement.getElementsByTagName("StateCd");
	                    if(null!=StateCd && null!=StateCd.item(0) && null!=StateCd.item(0).getFirstChild())
	                    StateCdElement = StateCd.item(0).getFirstChild().getNodeValue();
	                    NodeList ZipCd = dailyInterviewRosterElement.getElementsByTagName("ZipCd");
	                    if(null!=ZipCd && null!=ZipCd.item(0) && null!=ZipCd.item(0).getFirstChild())
	                    ZipCdElement = ZipCd.item(0).getFirstChild().getNodeValue();
	                    	                   
	                }// end of if clause

	            }// end of for loop with s var
	            
	            generateExcelTemplate();
	            
	            NodeList interviewTag =null; 
	            interviewTag=doc.getElementsByTagName("Interview");
	            generateTableHeaders();
	            rowNum++;
	            for (int s = 0; s < interviewTag.getLength(); s++) 
	            {
	                Node interviewNode = null;
	                interviewNode=interviewTag.item(s);
	                if (interviewNode.getNodeType() == Node.ELEMENT_NODE) 
	                {
	                	 Element interviewElement = (Element) interviewNode;
		                 NodeList candidateTag =null; 
		                 candidateTag= interviewElement.getElementsByTagName("candidate");
		 	           for (int i = 0; i < candidateTag.getLength(); i++) 
			            {
		 	            	Node candidateNode =null;
		 	            	candidateNode=candidateTag.item(i);
		 	            	if (candidateNode.getNodeType() == Node.ELEMENT_NODE) 
			                {
		 	            		intvwDate=null;
		 	            		intvwTime=null;
		 	            		rscPhnScrnNo=null;
		 	            		candidateName=null;
		 	            		candRefNbr=null;
		 	            		candidatePhNo=null;
		 	            		candidateEmail=null;
		 	            		reqNo=null;
		 	            		storeNo=null;
		 	            		departmentNo=null;
		 	            		jobCode=null;
		 	            		
		 	            		Element candidateElement = (Element) candidateNode;
		 	            		NodeList intvwDateList = candidateElement.getElementsByTagName("Date");
		 	            		if(intvwDateList!=null)
		 	            		intvwDate =  intvwDateList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList intvwTimeList = candidateElement.getElementsByTagName("Time");
		 	            		if(intvwTimeList!=null)
		 	            		intvwTime =  intvwTimeList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList rscPhoneScreenNoList = candidateElement.getElementsByTagName("RSCPhoneScreenNo");
		 	            		if(null!=rscPhoneScreenNoList && null!=rscPhoneScreenNoList.item(0)&& null!=rscPhoneScreenNoList.item(0).getFirstChild())
		 	            		rscPhnScrnNo =  rscPhoneScreenNoList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList candidateNameList = candidateElement.getElementsByTagName("CandidateName");
		 	            		if(candidateNameList!=null && candidateNameList.item(0)!=null && candidateNameList.item(0).getFirstChild()!=null)
		 	            		candidateName =  candidateNameList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList candRefNbrList = candidateElement.getElementsByTagName("CandRefNbr");
		 	            		if(candRefNbrList!=null && null!=candRefNbrList.item(0)&& null!=candRefNbrList.item(0).getFirstChild())
		 	            		candRefNbr =  candRefNbrList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList candidatePhNoList = candidateElement.getElementsByTagName("CandidatePhNo");
		 	            		if(candidatePhNoList!=null && null!=candidatePhNoList.item(0) && null!=candidatePhNoList.item(0).getFirstChild())
		 	            		candidatePhNo =  candidatePhNoList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList candidateEmailList = candidateElement.getElementsByTagName("CandidateEmail");
		 	            		if(candidateEmailList!=null && candidateEmailList.item(0) !=null && candidateEmailList.item(0).getFirstChild()!=null )
		 	            		candidateEmail =  candidateEmailList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList reqnoList = candidateElement.getElementsByTagName("RequisitionNumber");
		 	            		if(reqnoList!=null && reqnoList.item(0)!=null && reqnoList.item(0).getFirstChild()!=null)
		 	            		reqNo =  reqnoList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList storeNoList = candidateElement.getElementsByTagName("StoreNo");
		 	            		if(null!=storeNoList && null!=storeNoList.item(0) && null!=storeNoList.item(0).getFirstChild())
		 	            		storeNo =  storeNoList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList deptNoList = candidateElement.getElementsByTagName("DepartmentNo");
		 	            		if(null!=deptNoList && null!=deptNoList.item(0) && null!=deptNoList.item(0).getFirstChild())
		 	            		departmentNo =  deptNoList.item(0).getFirstChild().getNodeValue();
		 	            		
		 	            		NodeList jobcodeList = candidateElement.getElementsByTagName("JobCode");
		 	            		if(null!=jobcodeList && null!=jobcodeList.item(0) && null!=jobcodeList.item(0).getFirstChild())
		 	            		jobCode =  jobcodeList.item(0).getFirstChild().getNodeValue();
		 	            				 	            		
//		 	            		Element RSCPhoneScreenNoElement = (Element)RSCPhoneScreenNoList.item(0);
//                                NodeList textRSCPhoneScreenNoList = RSCPhoneScreenNoElement.getChildNodes();
//		 	                    System.out.println("RSCPhoneScreenNoElement: " + 
//		 	                           ((Node)textRSCPhoneScreenNoList.item(0)).getNodeValue().trim());
		 	            		//System.out.println("RSCPhoneScreenNoElement:"+rscPhnScrnNo);
		 	            		

                            }
		 	            	generateTableColumnValues();
		 	            	
			            }
	                }
	                rowNum++;
	            }   
	            
	            
	            for(int columnIndex = 0; columnIndex < 14; columnIndex++) {    
	            	worksheet.autoSizeColumn(columnIndex); 
	            	} 

	            worksheet.setColumnWidth(0, 2000);
	            worksheet.setColumnWidth(1, 2000);
	            worksheet.setColumnWidth(2, 2200);
	            worksheet.setColumnWidth(3, 3500);
	            worksheet.setColumnWidth(4, 2000);
	            worksheet.setColumnWidth(5, 2500);
	            worksheet.setColumnWidth(6, 3500);
	            worksheet.setColumnWidth(7, 2000);
	            worksheet.setColumnWidth(8, 2000);
	            worksheet.setColumnWidth(9, 1800);
	            worksheet.setColumnWidth(10, 2000);
	            worksheet.setColumnWidth(11, 1800);
	            worksheet.setColumnWidth(12, 1800);
	            worksheet.setColumnWidth(13, 1800);
	            


	            
//	            workbook.write(fileOut);
//				fileOut.flush();
//				fileOut.close();
	            
	            // Setup the output 
	            String contentType = "application/vnd.ms-excel"; 
	            httpResponse.setContentType(contentType); 
	            ServletOutputStream out = httpResponse.getOutputStream(); 
	            workbook.write(out); 
	            out.flush(); 
	            out.close(); 


	            

	        } 
	        catch (SAXParseException err) 
	        {
	            System.out.println("** Parsing error" + ", line "+ err.getLineNumber() + ", uri " + err.getSystemId());
	            System.out.println(" " + err.getMessage());
	        } 
	        catch (SAXException e) 
	        {
	            Exception x = e.getException();
	            ((x == null) ? e : x).printStackTrace();
	        } 
	        catch (Throwable t) 
	        {
	            t.printStackTrace();
	        }
			
			
			//wb.dispose();  


		  //Desktop.getDesktop().open();  

		} 	
	
	  public static void generateExcelTemplate()
		{
		    Calendar printDate = Calendar.getInstance();
			// index from 0,0... cell A1 is cell(0,0)
			HSSFRow row1 = worksheet.createRow((short) 0);
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
					
			HSSFCellStyle boldCellStyle = workbook.createCellStyle();
			boldCellStyle.setWrapText(true);
			
			HSSFCellStyle textStyle = workbook.createCellStyle();
			textStyle.setWrapText(true);
			
			HSSFCell cellA1 = row1.createCell((short) 0);
			cellA1.setCellValue("Daily Interview Roster Details");
			//cellStyle = workbook.createCellStyle();
			
			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short)15);
			cellStyle.setFont(font);
			cellStyle.setFillForegroundColor(HSSFColor.GOLD.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellA1.setCellStyle(cellStyle);
					
			HSSFFont bolderHeader = workbook.createFont();
			bolderHeader.setFontHeightInPoints((short)7);
			bolderHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			boldCellStyle.setFont(bolderHeader);
			
			HSSFFont normalText = workbook.createFont();
			normalText.setFontHeightInPoints((short)7);
			textStyle.setFont(normalText);
			
			HSSFCell cellF1 = row1.createCell((short) 6);
			cellF1.setCellValue("Printed On:");
			cellF1.setCellStyle(boldCellStyle);
			HSSFCell dateValue = row1.createCell((short) 7);
			dateValue.setCellValue(printDateFormat.format(printDate.getTime()));
			dateValue.setCellStyle(textStyle);
			//cellStyle = workbook.createCellStyle();
//			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
//			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//			cellF1.setCellStyle(cellStyle);
	        
			HSSFRow row2 = worksheet.createRow((short) 3);
	        HSSFCell cellEventName = row2.createCell((short) 0);
	        HSSFCell cellEventNameValue = row2.createCell((short) 1);
	        cellEventName.setCellValue("Hiring   Event Name:");
	        cellEventName.setCellStyle(boldCellStyle);
			//cellStyle = workbook.createCellStyle();
//			cellEventName.setCellStyle(cellStyle);
		    cellEventNameValue.setCellValue(hiringEventNameElement);
		    cellEventNameValue.setCellStyle(textStyle);
//			cellEventNameValue.setCellStyle(cellStyle);
			
			HSSFRow row3 = worksheet.createRow((short) 4);
	        HSSFCell cellLocation = row3.createCell((short) 0);
	        HSSFCell cellLocationNameValue = row3.createCell((short) 1);
	        cellLocation.setCellValue("Location Name:");
	        cellLocation.setCellStyle(boldCellStyle);
			//cellStyle = workbook.createCellStyle();
//			cellLocation.setCellStyle(cellStyle);
			cellLocationNameValue.setCellValue(locationElement);
			cellLocationNameValue.setCellStyle(textStyle);
//			cellLocationNameValue.setCellStyle(cellStyle);
			
			
	        HSSFCell Address = row3.createCell((short) 2);
	        HSSFCell AddressValue = row3.createCell((short) 3);
	        Address.setCellValue("Address:");
	        Address.setCellStyle(boldCellStyle);
			//cellStyle = workbook.createCellStyle();
//			Address.setCellStyle(cellStyle);
			AddressValue.setCellValue(addressElement);
			AddressValue.setCellStyle(textStyle);
			
			HSSFRow row4 = worksheet.createRow((short) 5);
	        HSSFCell cellCityName = row4.createCell((short) 0);
	        HSSFCell cellCityNameValue = row4.createCell((short) 1);
	        
	        cellCityName.setCellValue("City:");
	        cellCityNameValue.setCellValue(cityElement);
	        cellCityName.setCellStyle(boldCellStyle);
	        cellCityNameValue.setCellStyle(textStyle);
			//cellStyle = workbook.createCellStyle();
//			cellCityName.setCellStyle(cellStyle);
//			cellCityNameValue.setCellStyle(cellStyle);
			
			
			HSSFCell State = row4.createCell((short) 2);
			HSSFCell StateValue = row4.createCell((short) 3);
			State.setCellValue("State:");
			StateValue.setCellValue(StateCdElement);
			State.setCellStyle(boldCellStyle);
			StateValue.setCellStyle(textStyle);
			//cellStyle = workbook.createCellStyle();
//			State.setCellStyle(cellStyle);
//			StateValue.setCellStyle(cellStyle);
			
			HSSFCell Zip = row4.createCell((short) 4);
			HSSFCell ZipValue = row4.createCell((short) 5);
			Zip.setCellValue("Zip:");
			ZipValue.setCellValue(ZipCdElement);
			Zip.setCellStyle(boldCellStyle);
			ZipValue.setCellStyle(textStyle);
			
			//cellStyle = workbook.createCellStyle();
//			Zip.setCellStyle(cellStyle);
//			ZipValue.setCellStyle(cellStyle);
			
//			HSSFRow row5 = worksheet.createRow((short) 7);
//	        HSSFCell celldispName = row5.createCell((short) 0);
//	        celldispName.setCellValue("The Daily Interview Roster details of the applicants are displayed below in the interview time slot order.");
//			//cellStyle = workbook.createCellStyle();
//			celldispName.setCellStyle(cellStyle);
			
//			worksheet.autoSizeColumn(0);
//			worksheet.autoSizeColumn(5);
			worksheet.addMergedRegion(new CellRangeAddress(0,0,0,4));

		}
		public static void generateTableHeaders()
		{
			
			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
			HSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontHeightInPoints((short)7);
			cellStyle.setFont(font);
			cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					
			HSSFRow row5 = worksheet.createRow((short) rowNum);
	        HSSFCell intvwDate = row5.createCell((short) 0);
	        intvwDate.setCellStyle(cellStyle);
	        intvwDate.setCellValue("Date");
	        HSSFCell intvwTime = row5.createCell((short) 1);
	        intvwTime.setCellStyle(cellStyle);
	        intvwTime.setCellValue("Time");
	        HSSFCell rscPhnScrnNo = row5.createCell((short) 2);
	        rscPhnScrnNo.setCellStyle(cellStyle);
	        rscPhnScrnNo.setCellValue("PhnScrnNo");
	        HSSFCell candidateName = row5.createCell((short)3);
	        candidateName.setCellStyle(cellStyle);
	        candidateName.setCellValue("CandidateName");
	        HSSFCell candRefNbr = row5.createCell((short) 4);
	        candRefNbr.setCellStyle(cellStyle);
	        candRefNbr.setCellValue("Cand Ref Nbr");
	        HSSFCell candidatePhNo = row5.createCell((short) 5);
	        candidatePhNo.setCellStyle(cellStyle);
	        candidatePhNo.setCellValue("Candidate   PhNo");
	        HSSFCell candidateEmail = row5.createCell((short) 6);
	        candidateEmail.setCellStyle(cellStyle);
	        candidateEmail.setCellValue("CandidateEmail");
	        HSSFCell reqNo = row5.createCell((short) 7);
	        reqNo.setCellStyle(cellStyle);
	        reqNo.setCellValue("ReqNo");
	        HSSFCell storeNo = row5.createCell((short) 8);
	        storeNo.setCellStyle(cellStyle);
	        storeNo.setCellValue("StoreNo");
	        HSSFCell departmentNo = row5.createCell((short) 9);
	        departmentNo.setCellStyle(cellStyle);
	        departmentNo.setCellValue("DeptNo");
	        HSSFCell jobCode = row5.createCell((short) 10);
	        jobCode.setCellStyle(cellStyle);
	        jobCode.setCellValue("JobCode");
	        HSSFCell hiringStore = row5.createCell((short) 11);
	        hiringStore.setCellStyle(cellStyle);
	        hiringStore.setCellValue("Hiring    Store");
	        HSSFCell storeDeptno = row5.createCell((short) 12);
	        storeDeptno.setCellStyle(cellStyle);
	        storeDeptno.setCellValue("StoreDeptNo");
	        HSSFCell storeJobCode = row5.createCell((short) 13);
	        storeJobCode.setCellStyle(cellStyle);
	        storeJobCode.setCellValue("StoreJob Code");
	        
			rowNum++;
		}
		
		public static void generateTableColumnValues()
		{
			HSSFCellStyle textStyle = workbook.createCellStyle();
			textStyle.setWrapText(true);
			HSSFFont font = workbook.createFont();
			font.setFontHeightInPoints((short)7);
			textStyle.setFont(font);
			
			HSSFRow row5 = worksheet.createRow((short) rowNum);
	        HSSFCell intvwDateCell = row5.createCell((short) 0);
	        intvwDateCell.setCellValue(intvwDate);
	        intvwDateCell.setCellStyle(textStyle);
	        HSSFCell intvwTimeCell = row5.createCell((short) 1);
	        intvwTimeCell.setCellValue(intvwTime);
	        intvwTimeCell.setCellStyle(textStyle);
	        HSSFCell rscPhnScrnNoCell = row5.createCell((short) 2);
	        rscPhnScrnNoCell.setCellValue(rscPhnScrnNo);
	        rscPhnScrnNoCell.setCellStyle(textStyle);
	        HSSFCell candidateNameCell = row5.createCell((short)3);
	        candidateNameCell.setCellValue(candidateName);
	        candidateNameCell.setCellStyle(textStyle);
	        HSSFCell candRefNbrCell = row5.createCell((short) 4);
	        candRefNbrCell.setCellValue(candRefNbr);
	        candRefNbrCell.setCellStyle(textStyle);
	        HSSFCell candidatePhNoCell = row5.createCell((short) 5);
	        candidatePhNoCell.setCellValue(candidatePhNo);
	        candidatePhNoCell.setCellStyle(textStyle);
	        HSSFCell candidateEmailCell = row5.createCell((short) 6);
	        candidateEmailCell.setCellValue(candidateEmail);
	        candidateEmailCell.setCellStyle(textStyle);
	        HSSFCell reqNoCell = row5.createCell((short) 7);
	        reqNoCell.setCellValue(reqNo);
	        reqNoCell.setCellStyle(textStyle);
	        HSSFCell storeNoCell = row5.createCell((short) 8);
	        storeNoCell.setCellValue(storeNo);
	        storeNoCell.setCellStyle(textStyle);
	        HSSFCell departmentNoCell = row5.createCell((short) 9);
	        departmentNoCell.setCellValue(departmentNo);
	        departmentNoCell.setCellStyle(textStyle);
	        HSSFCell jobCodeCell = row5.createCell((short) 10);
	        jobCodeCell.setCellValue(jobCode);
	        jobCodeCell.setCellStyle(textStyle);
	        HSSFCell hiringStorecell = row5.createCell((short) 11);
	        hiringStorecell.setCellValue("");
	        hiringStorecell.setCellStyle(textStyle);
	        HSSFCell storeDeptnoCell = row5.createCell((short) 12);
	        storeDeptnoCell.setCellValue("");
	        storeDeptnoCell.setCellStyle(textStyle);
	        HSSFCell storeJobCodeCell = row5.createCell((short) 13);
	        storeJobCodeCell.setCellValue("");
	        storeDeptnoCell.setCellStyle(textStyle);
	        
	        rowNum++;
			
		}
}

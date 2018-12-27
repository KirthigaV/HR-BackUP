package com.homedepot.hr.hr.staffingforms.bl;

import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.hr.hr.staffingforms.pdf.PDFHelper;
import com.homedepot.hr.hr.staffingforms.pdf.PDFNode;
/**
 * This class generates PDF document with dayView schedules.  It utilizes Apache FOP open source API 
 * for PDF generation.
 * 
 * NOTE:  In order for PDF generation to work, xalan-2.7.0.jar and xercesImpl-2.7.1.jar must be included in the project
 * path despite the shown errors.

 * @author CHRIS STANN
 */

public class PrintDayViewManager implements Constants, DAOConstants{
	/** Logger instance */
	private static final Logger mLogger = Logger.getLogger(CalendarManager.class);
	//Location of xsl for day view print
	private static final String XSL_FILE_PATH = "../xsl/printDayView.xsl";
	private static final String XSL_MET_FILE_PATH = "../xsl/printDayView_MET.xsl";
	//Failure or informational messages used when data can not be returned
	private static final String PDF_ERROR = "An error occurred during generation of this document.";
	private static final String PDF_EMPTY = "No data was found for this document.    ";
	
	//XML Node names
	private static final String DAY_VIEW = "dayView";
	private static final String PRINT_DATE = "printDate";
	private static final String CALENDAR = "calendar";
	private static final String DATE = "date";
	private static final String LOCATION = "location";
	private static final String TIME_SLOT = "timeSlot";
	private static final String NAME = "name";
	private static final String TIME = "time";
	private static final String SEQUENCE = "seq";
	private static final String STORE = "store";
	private static final String ERROR = "error";
	
	//Other formatting
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
	private static final SimpleDateFormat printDateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm a");
/**
 * First, XML is created containing all Calendar data for a given calendar id and date.  Second, an XSL style sheet
 * is read from WEB-INF/xsl.  Both XML and XSL are passed to the PDFHelper helper method which calls the API and returns
 * a byte array of the PDF document.  The byte array then is written to the OutputStream.
 * 
 * @param store - displayed as-is in the PDF document header
 * @param calendarName - displayed as-is in the PDF document header
 * @param calendarId - used to retrieve calendar details
 * @param date - used to retrieve calendar details and is formatted for the PDF document header
 * @param out - OutputStream to which PDF is written
 * @throws Exception
 */
	public static void getPdfView(String store, String calendarName, int calendarId, Date date, OutputStream out) throws Exception {
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering PrindDayViewManager getPdfView() for store, calendarId, date: %1$s, %2$s, %3$s", store, calendarId, date));
		} // end if
		PDFHelper pdf = new PDFHelper();
		String xml = null;
		String xsl = null;
	

		try {
			xml = getXmlView(store, calendarName, calendarId, date);
			//Retrieve a map of calendars for store number
			Map <Integer, RequisitionCalendar> calendarMap = CalendarManager.getActiveCalendarsForStore(store);
			//Retrieve a calendar based on calendar Id
			RequisitionCalendar cal = calendarMap.get(calendarId);
			//Get calendar type code
			short type = cal.getCalTypCd(); //1 - reg, 2 - MET, 10 - "special"
//			String path = calendarName.startsWith("MET")?XSL_MET_FILE_PATH:XSL_FILE_PATH;
			//If type is 2, then it's a MET calendar
			String path = (type == 2)?XSL_MET_FILE_PATH:XSL_FILE_PATH;
			xsl = getXsl(pdf,path);
			
			if(xsl == null || xsl.length() < 1) {
				if(mLogger.isDebugEnabled())
				{
					startTime = System.nanoTime();
					mLogger.error(String.format("XSL not present for store, calendarId, date: %1$s, %2$s %3$s", store, calendarId, date));
				} // end if
			}
			
			byte pdfBytes[] = null;
			pdfBytes = pdf.createPdf(xml, xsl);
			if(mLogger.isDebugEnabled())
			{
				mLogger.debug(String.format("XML: %1$s", xml));
			} 
			if (pdfBytes != null){
				
				out.write(pdfBytes);
			}
		} catch (Exception e) {
			if(mLogger.isDebugEnabled())
			{
				startTime = System.nanoTime();
				mLogger.error(String.format("Exception in getPdfView() for store, calendarId, date, exception: %1$s, %2$s %3$s, %4$s", store, calendarId, date, e));
				//We want to log it here and pass the buck, so that the calling service can generate a proper error message to the user
				throw e;
			} // end if
		}
		
	}
	/**
	 * Generates XML data for a given calendar based on calendarId and date.  See CalendarManager for more details.
	 * Writes store and calendarName directly to XML.
	 * 
	 * @param store
	 * @param calendarName
	 * @param calendarId
	 * @param date
	 * @return
	 */
	private static String getXmlView (String store, String calendarName, int calendarId, Date date) {
		long startTime = 0;
		int seq = 0;
		Timestamp prevTime = null;
		Calendar printDate = Calendar.getInstance();
		
		PDFNode calendar = new PDFNode(DAY_VIEW);
		PDFNode timeSlot = new PDFNode(TIME_SLOT);
		calendar.addContent(DATE, dateFormat.format(date));
		calendar.addContent(LOCATION, store);
		calendar.addContent(CALENDAR, calendarName);
		calendar.addContent(PRINT_DATE, printDateFormat.format(printDate.getTime()));
		
		try {
			List<RequisitionSchedule> scheduleList = CalendarManager.getCalendarDetailsForDate(calendarId, date);
			
			if (scheduleList == null || scheduleList.size() == 0 || store == null) {
				calendar.addContent(ERROR, PDF_EMPTY+" "+calendarId+" "+date);
			}
			
			for(RequisitionSchedule schedule: scheduleList  ) {
				//Create fancy XML from schedules
				
				//First go-round, initialize and populate timeSlot node
				if(prevTime == null) {
					prevTime = schedule.getBgnTs();
				}
			
				
				//When time slot changes, create a new timeSlot node
				if ((!prevTime.equals(schedule.getBgnTs())) && 
					(schedule.getCandidateName() != null)) {
					if (!timeSlot.isEmpty()) calendar.addContent(timeSlot);
					timeSlot.reset();
					seq=0;
					timeSlot = new PDFNode(TIME_SLOT);
					timeSlot.addContent(TIME, timeFormat.format(schedule.getBgnTs()));
					prevTime = schedule.getBgnTs();
				}
				if((schedule.getCandidateName() != null)) {
					seq++;
					if(timeSlot.isEmpty())timeSlot.addContent(TIME, timeFormat.format(schedule.getBgnTs()));
					PDFNode name = new PDFNode(NAME+seq);
					name.addAttribute(SEQUENCE, seq+"");
					name.addContent(NAME, schedule.getCandidateName());
					name.addContent(STORE, schedule.getStrNbr());
					timeSlot.addContent(name);
					name.reset();
				}
			}
			if (!timeSlot.isEmpty()) calendar.addContent(timeSlot);
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			calendar.addContent(ERROR, PDF_ERROR);
		} 
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Generated XML for Calendar: "+calendar.getPDFNode()));
		} // end if
		
		return calendar.getPDFNode();
	}
	/**
	 * A convenience method for returning a String containing the XSL style sheet. 
	 * It leverages an instance of PDFHelper to read the style sheet at value in
	 *  XSL_FILE_PATH class constant. 
	 * 
	 * @param pdf
	 * @return
	 * @throws Exception
	 */
	private static String getXsl(PDFHelper pdf, String filePath) throws Exception {
		long startTime = 0;
		String xsl = null;
		xsl = pdf.getXMLString(filePath);
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Read XSL: %1$s", xsl));
		} // end if
		return xsl;
	}
}

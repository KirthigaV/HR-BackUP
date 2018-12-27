package com.homedepot.hr.hr.staffingforms.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.homedepot.hr.hr.staffingforms.bl.PrintApplicationManager;
import com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO.HiringEventPacket;
import com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO.HiringEventPacket.Applicants.ApplicantDetails;
import com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO.response.Envelope;
import com.homedepot.hr.hr.staffingforms.dto.hiringEventPacketPDFDTO.response.Envelope.Status;
import com.homedepot.hr.hr.staffingforms.exceptions.HiringEventException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.pdf.PDFMerger;
import com.homedepot.hr.hr.staffingforms.service.formatters.ExceptionXMLFormatter;
import com.homedepot.hr.hr.staffingforms.util.HiringEventJAXBTransformer;
import com.homedepot.hr.hr.staffingforms.util.Utils;
import com.homedepot.hr.hr.staffingforms.util.ValidationUtils;
import com.homedepot.ta.aa.log4j.SimpleExceptionLogger;
import com.sun.jersey.api.client.Client;

@Path("/HiringEventPrintService")
public class HiringEventPrintService implements Constants, Service
{

	/**
	 * Initializing logger
	 */ 
	private static final Logger logger = Logger.getLogger(HiringEventPrintService.class);
	
	
    
	/**
	 * This service is used to generate a final merged PDF containing the
	   Applicant PDF of all the applicants
	 * 
	 * @param candidateData
	 * @param version
	 * @return String
	 */
	
	@POST
	@Path("/getMergedPDF")
	@Produces("text/html")
    public String getMergedPDF( @FormParam("hiringPacket") String hiringEventPacketRequest,@Context HttpServletResponse httpResponse,@DefaultValue("1") @QueryParam("version") int version, 
    						    @DefaultValue(MediaType.APPLICATION_XML) @FormParam("Content-Type") String contentType)
    {
        if(logger.isDebugEnabled()){
            logger.debug("Start getMergedPDF method");
            logger.debug("version: '"+version+"'");
        }
        
        String result = null;

        try{			
            switch(version){
	            case 1:
	                if(logger.isDebugEnabled()) 
	                	logger.debug("in version 1");
	                String uri = null;

	        		Client client = null;
                    InputStream input =null;
                    byte mergedPdfBytes[]=null;
                    ByteArrayOutputStream opStream=null;
                    Envelope responseEnvelope = null;
                    List<Integer> phnScrnList = new ArrayList<Integer>();
                    //phnScrnList.addAll();
                    Envelope envelope = null;
                    HiringEventPacket hiringEventPacketDTO = new HiringEventPacket();
                    String validationErrorXML=null;
                    String mediaType=contentType;
	        		
	        		 try {
	        			 mediaType = Utils.getMediaType(mediaType);
	        			 if (hiringEventPacketRequest != null) {
	        				 if (logger.isDebugEnabled()) {
	     						logger.debug("Hiring Event Packet Information Received is:"
	     								+ hiringEventPacketRequest);
	     					}
	        				 
	        				 
	        				 
	        				 if(mediaType.equalsIgnoreCase(MediaType.APPLICATION_JSON))
	        				 {
	        					 //responseEnvelope = new Envelope();
	        					 hiringEventPacketDTO = (HiringEventPacket) Utils.getObjectFromInput(mediaType, hiringEventPacketRequest, HiringEventPacket.class);
	        				 }
	        				 else
	        				 {
	        					 responseEnvelope = validateXMLWellformedness(hiringEventPacketRequest,
		        							responseEnvelope);
		        				 
		        				 if (responseEnvelope != null) {
		        					 if(responseEnvelope.getStatus()!=null)
		        							 {
		        						 validationErrorXML = HiringEventJAXBTransformer.marshall(
		        									responseEnvelope, Envelope.class);
		        						 httpResponse.setContentType(APPLICATION_XML);
		        						 httpResponse.setContentLength(validationErrorXML.length());
		        						 
		        						 httpResponse.getOutputStream().write(validationErrorXML.getBytes());
		        						 return null;
		        							 }
		        						}
		        				 hiringEventPacketDTO =HiringEventJAXBTransformer.unMarshall(hiringEventPacketRequest,
		        						 	HiringEventPacket.class);
	        				 }
	        				 
	        			 }
	        			 
	        			 StringBuilder sb= new StringBuilder();
	        			 byte pdfBytes[] = null;
	        			 
	        		 List<ApplicantDetails> applicantDtlDTO=new ArrayList<ApplicantDetails>();
	        			 if(hiringEventPacketDTO!=null)
	        			 {
	        			 
	        				 applicantDtlDTO=hiringEventPacketDTO.getApplicants().getApplicantDetails();
	        			 }
	        		  for(ApplicantDetails applicantDetailsDTO:applicantDtlDTO)
	        			{
	        			 
	        			  String intExtFlg=null;
	        			  int phnScrnId=applicantDetailsDTO.getPhnscrnId();
	        			   // validate the Phone Screen Number provided
	        				ValidationUtils.validatePhoneScrnNum(applicantDetailsDTO.getPhnscrnId());
	        				
	        				intExtFlg=applicantDetailsDTO.getIntExtFlg();
	        				// validate the Internal / External provided
	        				if (intExtFlg == null || (!intExtFlg.equalsIgnoreCase("E") && !intExtFlg.equalsIgnoreCase("I"))) {
	        					logger.debug(String.format("Invalid Internal/External Flag for PhoneScreen: %1$d", phnScrnId));
	        					throw new Exception("Invalid Internal/External Flag");
	        				}

	        				if (logger.isDebugEnabled()) {
	        					logger.debug(String.format("Generating PDF for Applicant Phone Screen:  %1$d", phnScrnId));
	        				} // end if

	        				if (intExtFlg.equalsIgnoreCase("E")) {
	        					//External and Mask SSN
	        					pdfBytes=PrintApplicationManager.getExtApplicantHiringEventPdfView(phnScrnId, httpResponse.getOutputStream(), true);
	        				} else {
	        					//Internal
	        					pdfBytes=PrintApplicationManager.getIntProfileHiringEventPdfView(phnScrnId, httpResponse.getOutputStream(), true);
	        				}
	        				
	        				InputStream inpStream=null; 
	        				inpStream= new ByteArrayInputStream(pdfBytes);

	        				opStream=PDFMerger.appendPDFDocumentStream(inpStream,opStream);
	        				
	        			byte[] buffer =null;
        			
	        			}

	        		  httpResponse.getOutputStream().write(opStream.toByteArray());
	        		  //httpResponse.setContentType(APPLICATION_PDF);
	        		  }
	        		 
	        		 catch (Exception exObj) {
	        			exObj.printStackTrace();

	        			}
	        		 finally {
	        			 if(client!=null)
	        			 {
	        				client.destroy();
	        			 }
	        			 

	        					if(httpResponse.getOutputStream() != null)
	        					{
	        						// flush the buffer
	        						httpResponse.getOutputStream().flush();
	        						// close the output stream
	        						httpResponse.getOutputStream().close();
	        					} // end if(httpResponse.getOutputStream() != null)

	        			} // end finally
	        			
                    break;
                default: throw new Exception("Unsupported version: " + version);
            }
        }
        catch(Exception e){
            SimpleExceptionLogger.log(e,"StaffingForms-New");
            result = ExceptionXMLFormatter.formatMessage(e);
        }
        
        if(logger.isDebugEnabled()){
            //logger.debug("result: '"+result+"'");
            logger.debug("End getMergedPDF");
        }
        return null;	
    }
	
	/**
	 * This method is used to validate the Incoming xml
	 * 
	 * @param xml
	 * @param responseEnvelope
	 * @return ResponseEnvelope
	 * @throws SAXException
	 * @throws IVRServiceException
	 * 
	 */

	public Envelope validateXMLWellformedness(String xml,
			Envelope responseEnvelope) throws SAXException,
			HiringEventException {

		if (logger.isDebugEnabled()) {
			logger.debug("Start method : validateXMLWellformedness() in CandidateImportManager");
		}
		
		responseEnvelope = new Envelope();

		try {
			if (!ValidationUtils.isEmptyString(xml)) {
				doValidate(xml);
			}
		} catch (IOException ioe) {
			SimpleExceptionLogger.log(ioe, "HiringEvent");
			throw new HiringEventException(SEVERITY_HIGH,
					HTTP_STATUS_ERROR, ValidationUtils.getCurrentDate(),
					HIRING_EVENT_XML_VALIDATION_ERROR_DESC, ioe,
					HIRING_EVENT_XML_VALIDATION_ERROR_DESC);

		} catch (SAXException sa) {
			
			Status status= new Status();
			status.setCode(HTTP_STATUS_ERROR);
			status.setShortDescription("Hiring Event Packet Data XML is Invalid");
			status.setLongDescription(sa.getMessage());
			responseEnvelope.setStatus(status);
			
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Finish method : validateXMLWellformedness() in PrintApplicationManager");
		}
		return responseEnvelope;
	}
	
	/**
	 * This method is used to validate the HiringEvent XML for Wellformedness
	 * 
	 * 
	 * @param xml
	 * @param responseEnvelope
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 * 
	 */

	private void doValidate(String xml) throws SAXException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("Start method : doValidate() in HiringEventPrintService");
		}
		XMLReader parser = XMLReaderFactory.createXMLReader();
		// parser.setContentHandler(new DefaultHandler());
		InputSource source = new InputSource(new ByteArrayInputStream(
				xml.getBytes()));
		parser.parse(source);
		if (logger.isDebugEnabled()) {
			logger.debug("Finish method : doValidate() in HiringEventPrintService");
		}
	}

	
}

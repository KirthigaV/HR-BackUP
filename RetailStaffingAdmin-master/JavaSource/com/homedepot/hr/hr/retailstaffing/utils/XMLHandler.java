package com.homedepot.hr.hr.retailstaffing.utils;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: XMLHandler.java
 * Application: RetailStaffing
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.BlcksInterviewTimeTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventMgrTO;
import com.homedepot.hr.hr.retailstaffing.dto.InterviewAvaliableSlotsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDate;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvwAvailDateList;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.MinimumResponseTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenCallHistoryTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ProcessStatusTO;
import com.homedepot.hr.hr.retailstaffing.dto.QPConsideredApplicantDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.RejectionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.UserDataTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.ApplToReqRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.ApplicantPoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.AssociatePoolRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.BackgroundRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.CreatePhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.CreateRejectionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.GetLatestRejectionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.HrRetlStffReconsialtionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.ITIUpdateRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.InsertPhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.InterviewAvailRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.InterviewScheduleRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.IntrvwAvailDatesRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.IntrvwSchedRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.IntvwResultsCandIntvwDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.JobTitleRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.POMRsaStatusCrossRefRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.QPConsideredApplicantRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SearchRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitBgcCandidateDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SubmitCandidatePersonalDataRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateIntvwRltsCandIntvwDtlsRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdatePhoneScreenReqnSpecKnockOutRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateReviewPhnScrnRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateStaffingRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplApplicationHistoryInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplApplicationHistoryInfoResponse2;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplEducationInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplHistoryInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplIntvwDtlsResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplJobPrefInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplLangInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplPersonalInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplPhnScreenInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplPoolInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplProfileResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplUnavailInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplUnavailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplWorkHistoryInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplicantPoolResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplicantResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociatePoolResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociatePrePosResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociateResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociateReviewResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociateWorkInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AutoAttachJobTitleResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CalendarResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateCountResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CompleteITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.DeptResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.ExpLvlResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.GenericErrorTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.HiringEventResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetaiInactivelResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.InterviewAvailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.InterviewQuestDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.IntrvwAvailDatesResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.IntrvwSchedResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.IntrvwTimePrefResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.JobTtlResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.LangSklsResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.NoInterviewReasonResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.OfferDeclineListResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.OfferMadeListResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.OfferResultListResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.OrgUnitResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.POMRsaStatusCrossRefResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhnScrnCallHistoryResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhnScrnDispCodeResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenInboundResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenKOQuestResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenQuestResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.PhoneScreenReqnSpecKODetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ProcessStatusesResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.ScheduleDescResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDriverLicenseExemptResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.StructuredInterviewGuideListResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdatePhoneScreenReqnSpecKnockOutResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.UpdateResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.VDNDetailsResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.VoltageServiceResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.interfaces.XMLConstants;
import com.homedepot.hr.hr.retailstaffing.services.response.InterviewResponse;
import com.homedepot.hr.hr.retailstaffing.services.response.StatusResponse;
import com.homedepot.hr.hr.retailstaffing.util.Generic;
import com.homedepot.hr.hr.retailstaffing.util.PropertyReader;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

/**
 * Contains methods to serialize/deserialize objects using the XStream API
 */
public class XMLHandler implements Constants, RetailStaffingConstants, XMLConstants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(XMLHandler.class);
	
	// default encoding of the XMLs generated
	private static final String XML_ENCODING = "UTF-8";
	
	// XStream API object that will be used to serialize/deserialize objects
	private static XStream mHandler;
	
	static
	{
		// initialize the handler
		mHandler = new XStream(new DomDriver(XML_ENCODING)
		{
			/*
			 * (non-Javadoc)
			 * @see com.thoughtworks.xstream.io.xml.DomDriver#createWriter(java.io.Writer)
			 */
			@Override
			public HierarchicalStreamWriter createWriter(Writer writer)
			{
				// override this to return an instance of my writer instead of the default
				return new CDATAWriter(writer);
			} // end function createWriter()
			
			/*
			 * (non-Javadoc)
			 * @see com.thoughtworks.xstream.io.xml.DomDriver#createReader(java.io.Reader)
			 */
			@Override
			public HierarchicalStreamReader createReader(Reader reader)
			{			
				long startTime = 0;
				HierarchicalStreamReader xmlReader = null;
				StringReader filteredReader = null;
				
				if(mLogger.isDebugEnabled())
				{
					startTime = System.nanoTime();
				} // end if
				
				try
				{
					//----------
					// first read the input XML into a StringWriter
					//----------					
					StringWriter xmlWriter = new StringWriter();
					
					char[] chars = new char[2048];
					int charsRead = 0;
					// iterate and read the input data, write the characters read to the StringWriter
					while((charsRead = reader.read(chars, 0, chars.length)) != -1)
					{
						xmlWriter.write(chars, 0, charsRead);
					} // end while((charsRead = reader.read(chars, 0, chars.length)) != -1)

					//---------
					// now filter the input XML removing any non-standard characters
					//---------
					String inputXml = xmlWriter.toString();
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Filtering Input XML\n%1$s", inputXml));
					} // end if

					// filter out any non-standard special characters (null check in utility method)
					inputXml = StringUtils.filterSpecialCharacters(inputXml);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Filtered Input XML\n%1$s", inputXml));
					} // end if
					//----------
					// now use the filtered input XML to create a new StringReader
					//----------
					filteredReader = new StringReader(inputXml);
					// invoke the super classes createReader passing in the StringReader created using the filtered XML
					xmlReader = super.createReader(filteredReader);
				} // end try
				catch(IOException ioe)
				{
					// log the exception that occurred
					mLogger.error("An exception occurred filtering input XML", ioe);
					
					try
					{
						// reset the reader
						reader.reset();						
						// invoke the parent's method
						xmlReader = super.createReader(reader);
					} // end try
					catch(IOException ignore)
					{
						// nothing we can do here
					} // end catch
					// set the 
				} // end catch()
				finally
				{
					// close the FilteredReader if it was created
					if(filteredReader != null)
					{
						filteredReader.close();
					} // end if(filteredReader != null)
				} // end finally
				
				if(mLogger.isDebugEnabled())
				{
					long endTime = System.nanoTime();
					
					if(startTime == 0)
					{
						startTime = endTime;
					} // end if(startTime == 0)
					
					mLogger.debug(String.format("Total time to filter input XML: %1$.9f seconds",
						(((double)endTime - startTime) / NANOS_IN_SECOND)));
				} // end if
				
				return xmlReader;
			} // end function createReader()
			
			/*
			 * (non-Javadoc)
			 * @see com.thoughtworks.xstream.io.xml.DomDriver#createReader(java.io.InputStream)
			 */
			@Override
			public HierarchicalStreamReader createReader(InputStream xml)
			{				
				InputStreamReader inputReader = null;
				HierarchicalStreamReader reader = null;
				
				try
				{
					// create an InputStreamReader from the input XML
					inputReader = new InputStreamReader(xml);
					// invoke the other createReader method overridden in this class (so logic isn't duplicated)
					reader = this.createReader(inputReader);
				} // end try
				finally
				{
					// close the InputStreamReader if it was created
					if(inputReader != null)
					{
						try
						{
							inputReader.close();
						} // end try
						catch(IOException ignore)
						{
							// ignore
						} // end catch
					} // end if(inputReader != null)
				} // end finally
				
				return reader;
			} // end function createReader()
		}); // end initialization

		// disable references
		mHandler.setMode(XStream.NO_REFERENCES);
		
		// add the classes this XStream object can marshal/unmarshal
		//==========
		// Transfer Objects
		//==========
		mHandler.processAnnotations(Status.class);
		mHandler.processAnnotations(RequisitionDetailTO.class);
		mHandler.processAnnotations(CandidateDetailsTO.class);
		mHandler.processAnnotations(StoreDetailsTO.class);
		mHandler.processAnnotations(PhoneScreenIntrwDetailsTO.class);
		mHandler.processAnnotations(StaffingDetailsTO.class);
		mHandler.processAnnotations(ProcessStatusTO.class);
		mHandler.processAnnotations(IntrwLocationDetailsTO.class);
		mHandler.processAnnotations(MinimumResponseTO.class);
		mHandler.processAnnotations(BlcksInterviewTimeTO.class);
		mHandler.processAnnotations(InterviewAvaliableSlotsTO.class);
		mHandler.processAnnotations(IntrvwAvailDate.class);
		mHandler.processAnnotations(IntrvwAvailDateList.class);
		mHandler.processAnnotations(BackgroundRequest.class);
		mHandler.processAnnotations(RejectionDetailTO.class);
		mHandler.processAnnotations(QPConsideredApplicantDetailTO.class);
		mHandler.processAnnotations(HiringEventMgrTO.class);
		mHandler.processAnnotations(HiringEventDetailTO.class);
		mHandler.processAnnotations(UserDataTO.class);
		mHandler.processAnnotations(PhoneScreenCallHistoryTO.class);
		
		//==========
		// Response Objects
		//==========
		mHandler.processAnnotations(ApplApplicationHistoryInfoResponse.class);
		mHandler.processAnnotations(ApplApplicationHistoryInfoResponse2.class);
		mHandler.processAnnotations(ApplEducationInfoResponse.class);
		mHandler.processAnnotations(ApplHistoryInfoResponse.class);
		mHandler.processAnnotations(ApplicantPoolResponse.class);
		mHandler.processAnnotations(ApplicantResponse.class);
		mHandler.processAnnotations(ApplIntvwDtlsResponse.class);
		mHandler.processAnnotations(ApplJobPrefInfoResponse.class);
		mHandler.processAnnotations(ApplLangInfoResponse.class);
		mHandler.processAnnotations(ApplPersonalInfoResponse.class);
		mHandler.processAnnotations(ApplPhnScreenInfoResponse.class);
		mHandler.processAnnotations(ApplPoolInfoResponse.class);
		mHandler.processAnnotations(ApplProfileResponse.class);
		mHandler.processAnnotations(ApplUnavailInfoResponse.class);
		mHandler.processAnnotations(ApplUnavailResponse.class);
		mHandler.processAnnotations(ApplWorkHistoryInfoResponse.class);
		mHandler.processAnnotations(AssociatePoolResponse.class);
		mHandler.processAnnotations(AssociatePrePosResponse.class);
		mHandler.processAnnotations(AssociateResponse.class);
		mHandler.processAnnotations(AssociateReviewResponse.class);
		mHandler.processAnnotations(AssociateWorkInfoResponse.class);
		mHandler.processAnnotations(CalendarResponse.class);
		mHandler.processAnnotations(CandidateCountResponse.class);
		mHandler.processAnnotations(CandidateDetailResponse.class);
		mHandler.processAnnotations(CompleteITIDetailResponse.class);
		mHandler.processAnnotations(DeptResponse.class);
		mHandler.processAnnotations(ErrorTO.class);
		mHandler.processAnnotations(ExpLvlResponse.class);
		mHandler.processAnnotations(GenericErrorTO.class);
		mHandler.processAnnotations(InterviewAvailResponse.class);
		mHandler.processAnnotations(InterviewQuestDetailResponse.class);
		mHandler.processAnnotations(IntrvwAvailDatesResponse.class);
		mHandler.processAnnotations(InterviewResponse.class);
		mHandler.processAnnotations(IntrvwSchedResponse.class);
		mHandler.processAnnotations(IntrvwTimePrefResponse.class);
		mHandler.processAnnotations(ITIDetaiInactivelResponse.class);
		mHandler.processAnnotations(ITIDetailResponse.class);
		mHandler.processAnnotations(JobTtlResponse.class);
		mHandler.processAnnotations(LangSklsResponse.class);
		mHandler.processAnnotations(NoInterviewReasonResponse.class);
		mHandler.processAnnotations(OfferDeclineListResponse.class);
		mHandler.processAnnotations(OfferMadeListResponse.class);
		mHandler.processAnnotations(OfferResultListResponse.class);
		mHandler.processAnnotations(OrgUnitResponse.class);
		mHandler.processAnnotations(PhoneScreenInboundResponse.class);
		mHandler.processAnnotations(PhoneScreenKOQuestResponse.class);
		mHandler.processAnnotations(PhoneScreenQuestResponse.class);
		mHandler.processAnnotations(POMRsaStatusCrossRefResponse.class);
		mHandler.processAnnotations(ProcessStatusesResponse.class);
		mHandler.processAnnotations(RequisitionDetailResponse.class);
		mHandler.processAnnotations(Response.class);
		mHandler.processAnnotations(ScheduleDescResponse.class);
		mHandler.processAnnotations(StateDetailResponse.class);
		mHandler.processAnnotations(StatusResponse.class);
		mHandler.processAnnotations(StoreDetailResponse.class);
		mHandler.processAnnotations(StructuredInterviewGuideListResponse.class);
		mHandler.processAnnotations(UpdateResponse.class);
		mHandler.processAnnotations(VDNDetailsResponse.class);
		mHandler.processAnnotations(HiringEventResponse.class);
		mHandler.processAnnotations(AutoAttachJobTitleResponse.class);
		mHandler.processAnnotations(StoreDriverLicenseExemptResponse.class);
		mHandler.processAnnotations(PhnScrnDispCodeResponse.class);
		mHandler.processAnnotations(VoltageServiceResponse.class);
		//Added for FMS 7894 IVR Changes
		mHandler.processAnnotations(PhoneScreenReqnSpecKODetailResponse.class);
		mHandler.processAnnotations(UpdatePhoneScreenReqnSpecKnockOutResponse.class);
		mHandler.processAnnotations(PhnScrnCallHistoryResponse.class);
		
		//==========
		// Request Objects
		//==========
		mHandler.processAnnotations(ApplicantPoolRequest.class);
		mHandler.processAnnotations(ApplToReqRequest.class);
		mHandler.processAnnotations(AssociatePoolRequest.class);
		mHandler.processAnnotations(CreatePhoneScreenRequest.class);
		mHandler.processAnnotations(HrRetlStffReconsialtionRequest.class);
		mHandler.processAnnotations(InsertPhoneScreenRequest.class);
		mHandler.processAnnotations(InterviewAvailRequest.class);
		mHandler.processAnnotations(InterviewScheduleRequest.class);
		mHandler.processAnnotations(IntrvwAvailDatesRequest.class);
		mHandler.processAnnotations(IntrvwSchedRequest.class);
		mHandler.processAnnotations(IntvwResultsCandIntvwDtlsRequest.class);
		mHandler.processAnnotations(ITIUpdateRequest.class);
		mHandler.processAnnotations(JobTitleRequest.class);
		mHandler.processAnnotations(POMRsaStatusCrossRefRequest.class);
		mHandler.processAnnotations(SearchRequest.class);
		mHandler.processAnnotations(SummaryRequest.class);
		mHandler.processAnnotations(UpdateIntvwRltsCandIntvwDtlsRequest.class);
		mHandler.processAnnotations(UpdateReviewPhnScrnRequest.class);
		mHandler.processAnnotations(UpdateStaffingRequest.class);
		mHandler.processAnnotations(CreateRejectionRequest.class) ;
		mHandler.processAnnotations(GetLatestRejectionRequest.class);
		mHandler.processAnnotations(QPConsideredApplicantRequest.class);
		mHandler.processAnnotations(SubmitCandidatePersonalDataRequest.class);
		//Added for FMS 7894 IVR Changes
		mHandler.processAnnotations(UpdatePhoneScreenReqnSpecKnockOutRequest.class);
		
		mHandler.processAnnotations(SubmitBgcCandidateDtlsRequest.class);
		
		// trick the XStream API to add namespaces to the following classes
		mHandler.useAttributeFor(PhoneScreenInboundResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenInboundResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenInboundResponse.class, SCHEMA_LOCATION_VAR);

		mHandler.useAttributeFor(PhoneScreenKOQuestResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenKOQuestResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenKOQuestResponse.class, SCHEMA_LOCATION_VAR);

		//Added for FMS 7894 IVR Changes
		mHandler.useAttributeFor(PhoneScreenReqnSpecKODetailResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenReqnSpecKODetailResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenReqnSpecKODetailResponse.class, SCHEMA_LOCATION_VAR);	
		
		mHandler.useAttributeFor(UpdatePhoneScreenReqnSpecKnockOutResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(UpdatePhoneScreenReqnSpecKnockOutResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(UpdatePhoneScreenReqnSpecKnockOutResponse.class, SCHEMA_LOCATION_VAR);			
		
		mHandler.useAttributeFor(PhoneScreenQuestResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenQuestResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(PhoneScreenQuestResponse.class, SCHEMA_LOCATION_VAR);

		mHandler.useAttributeFor(UpdateResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(UpdateResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(UpdateResponse.class, SCHEMA_LOCATION_VAR);

		mHandler.useAttributeFor(VDNDetailsResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(VDNDetailsResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(VDNDetailsResponse.class, SCHEMA_LOCATION_VAR);

		mHandler.useAttributeFor(IntrvwAvailDatesResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(IntrvwAvailDatesResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(IntrvwAvailDatesResponse.class, SCHEMA_LOCATION_VAR);

		mHandler.useAttributeFor(IntrvwTimePrefResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(IntrvwTimePrefResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(IntrvwTimePrefResponse.class, SCHEMA_LOCATION_VAR);

		mHandler.useAttributeFor(InterviewAvailResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(InterviewAvailResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(InterviewAvailResponse.class, SCHEMA_LOCATION_VAR);

		mHandler.useAttributeFor(IntrvwSchedResponse.class, DFLT_NAMESPACE_VAR);
		mHandler.useAttributeFor(IntrvwSchedResponse.class, XML_NAMESPACE_VAR);
		mHandler.useAttributeFor(IntrvwSchedResponse.class, SCHEMA_LOCATION_VAR);
	} // end static block
	
	/**
	 * Use the XStream API to deserialize the object provided into XML format
	 * 
	 * @param objectToDeserialize				The object to deserialize
	 * 
	 * @return									XML representation of the object provided
	 * 
	 * @throws XStreamException					Thrown if XStream fails to deserialize the object provided (null, etc.)
	 */
	public static String toXML(Object objectToDeserialize) throws XStreamException
	{
		// next use the XStream API to deserialize the object into an XML and return it
		return mHandler.toXML(objectToDeserialize);
	} // end toXML()
	
	/**
	 * Use the XStream API to serialize an Object from the XML String provided
	 * 
	 * @param xml									XML String to serialize into an object
	 * 
	 * @return										Object serialized from the XML String provided
	 * 
	 * @throws XStreamException						Thrown if XStream fails to serialize the object (i.e. null, data type mismatch, etc.)
	 */
	public static Object fromXML(String xml) throws XStreamException
	{
		// use the XStream object to serialize the XML into an object
		return mHandler.fromXML(xml);
	} // end function fromXML()
	
	/**
	 * Use the XStream API to serialize an Object from the XML String provided
	 * 
	 * @param xmlStream								InputStream object containing the XML to be parsed
	 * 
	 * @return										Object serialized from the XML String provided
	 * 
	 * @throws XStreamException						Thrown if XStream fails to serialize the object (i.e. null, data type mismatch, etc.)
	 */
	public static Object fromXML(InputStream xmlStream) throws XStreamException
	{
		// use the XStream object to serialize the XML into an object
		return mHandler.fromXML(xmlStream);		
	} // end function fromXML()
	
	/**
	 * Returns a string representing the exception.
	 * 
	 * TODO : change out the way exception handling is being performed in this application.
	 * 
	 * @return String XML 
	 */
	 //Added argument mediaType - For Flex to HTML conversion - 13 May 2015
	public static String createErrorXML(Object object, Exception e,String mediaType)
	{

		String response = null;
		GenericErrorTO ge = new GenericErrorTO();
		if(e == null)
		{
			ge.setEndUserErrorMsg(RetailStaffingConstants.RETURN_MESSAGE_FATAL_ERROR);
		}
		else
		{
			if(e instanceof RetailStaffingException)
			{
				RetailStaffingException ex = (RetailStaffingException)e;
				PropertyReader propertyReader = PropertyReader.getInstance();
				if(propertyReader.getProperty(Integer.toString(ex.getErrorCode())) != null)
				{
					ge.setEndUserErrorMsg(propertyReader.getProperty(Integer.toString(ex.getErrorCode())).trim());
					ge.setErrorCode(Integer.toString(ex.getErrorCode()));
				}
				else
				{
					if(null == ex.getEndUserMessage())
					{
						ge.setEndUserErrorMsg(RetailStaffingConstants.RETURN_MESSAGE_FATAL_ERROR);
					}
					else
					{
						ge.setEndUserErrorMsg(ex.getEndUserMessage());
					}
				}
				 //ge.setEndUserErrorMsg(ex.getTechnicalErrorMessage());

			}
			else
			{
				ge.setEndUserErrorMsg(e.toString());
				ge.setEndUserErrorMsg(RetailStaffingConstants.RETURN_MESSAGE_FATAL_ERROR);
			}

		}

		Class<? extends Object> newClass = object.getClass();
		Object obj = null;
		for(Method m : newClass.getDeclaredMethods())
		{
			if(m.isAnnotationPresent(Generic.class))
			{
				try
				{
					if(m.getAnnotation(Generic.class).action().equalsIgnoreCase("GET"))
					{
						obj = m.invoke(object);
					}
				}
				catch (IllegalArgumentException e1)
				{

				}
				catch (IllegalAccessException e1)
				{

				}
				catch (InvocationTargetException e2)
				{

				}
			}
		}
		if(obj != null)
		{
			Class<? extends Object> error = obj.getClass();
			for(Method m : error.getDeclaredMethods())
			{
				if(m.isAnnotationPresent(Generic.class))
				{
					try
					{
						m.invoke(obj, ge);
					}
					catch (IllegalArgumentException e1)
					{

					}
					catch (IllegalAccessException e1)
					{

					}
					catch (InvocationTargetException e1)
					{

					}
				}
			}
		}

		for(Method m : newClass.getDeclaredMethods())
		{
			if(m.isAnnotationPresent(Generic.class))
			{
				try
				{
					if((m.getAnnotation(Generic.class)).action().equalsIgnoreCase("SET"))
					{
						m.invoke(object, ge);
					}
				}
				catch (IllegalArgumentException e1)
				{

				}
				catch (IllegalAccessException e1)
				{

				}
				catch (InvocationTargetException e1)
				{

				}
			}
		}
		//Start - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		/** Get required Format (XML /JSON)**/
		response =Util.getRequiredFormatRes(mediaType,object);
		//End - Commented and Added as part of converting object to XML/JSON - For Flex to HTML Conversion - 13 May 2015
		return response;
	}	
	
	/**
	 * This class does not escape the < or > characters IF they are contained within a CDATA XML element
	 */
	private static class CDATAWriter extends PrettyPrintWriter
	{
		/*
		 * (non-Javadoc)
		 * @see com.thoughtworks.xstream.io.xml.PrettyPrintWriter#PrettyPrintWriter(Writer)
		 */
		public CDATAWriter(Writer writer)
		{
			super(writer);
		} // end constructor()
		
		/*
		 * (non-Javadoc)
		 * @see com.thoughtworks.xstream.io.xml.PrettyPrintWriter#writeText(com.thoughtworks.xstream.core.util.QuickWriter, java.lang.String)
		 */
		@Override
		protected void writeText(QuickWriter writer, String text)
		{
			// if a non-null text value was provided
			if(text != null)
			{
				// check to see if the text contains CDATA tags, if so we'll write it as is
				if(text.indexOf(CDATA_START_TAG) != -1)
				{
					writer.write(text);
				} // end if(text.indexOf(CDATA_START_TAG) != -1)
				else // if there is no CDATA tag, we'll let the base class write it
				{
					super.writeText(writer, text);
				} // end else
			} // end if(text != null)
		} // end function writeText()
	} // end class CDATAWriter	
} // end class XMLHandler
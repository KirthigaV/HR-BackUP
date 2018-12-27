/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: RetailStaffingApplProfileManager.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.ApplProfileDetailDAO;
import com.homedepot.hr.hr.retailstaffing.dao.ApplRejectionDAO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplAppHistoryInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplAppHistoryInfoTO2;
import com.homedepot.hr.hr.retailstaffing.dto.ApplEducationInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplHistoryInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplLangInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplPersonalInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplPhnScreenInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.ApplWorkHistoryInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.AssociatePrePosTO;
import com.homedepot.hr.hr.retailstaffing.dto.AssociateReviewTO;
import com.homedepot.hr.hr.retailstaffing.dto.AssociateWorkInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.QPConsideredApplicantDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO;
import com.homedepot.hr.hr.retailstaffing.dto.SchedulePreferenceTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.CreateRejectionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.GetLatestRejectionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.QPConsideredApplicantRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplApplicationHistoryInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplApplicationHistoryInfoResponse2;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplEducationInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplHistoryInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplJobPrefInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplLangInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplPersonalInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplPhnScreenInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplProfileResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ApplWorkHistoryInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociatePrePosResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociateReviewResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.AssociateWorkInfoResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RejectionComboResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
/*
/**
 * This Class is used to appl profile based on applicant number
 * Methods:
 * getApplicantProfile
 * @author
 * 
 */
public class RetailStaffingApplProfileManager implements
		RetailStaffingConstants
{
	private static final Logger logger = Logger
			.getLogger(RetailStaffingRequisitionManager.class);
	public int eventId = 0;

	/**
	 * This method is used to get the applicant profile based on the applicant number.
	 * 
	 * @param applNbr
	 * @return
	 * @throws RetailStaffingException
	 */
	public ApplProfileResponse getApplicantProfile(String profileParam)
			throws RetailStaffingException
	{
		logger.info(this
				+ "Enters RetailStaffingApplProfileManager method in Manager with input"
				+ profileParam);

		String applNo = profileParam.substring(0, profileParam.indexOf("_"));
		String applType = profileParam.substring(profileParam.indexOf("_") + 1, profileParam.lastIndexOf("_"));
		String applDate = "";
		// fixing profile issue 06-07-2011
		String emplApplDate = "";
		
		if (applType.equalsIgnoreCase("AP")) {
			applDate = profileParam.substring(profileParam.lastIndexOf("_") + 1);
		}
		ApplProfileDetailDAO applProfileDetailDAO = new ApplProfileDetailDAO();
		ApplProfileResponse applProfileRes = new ApplProfileResponse();
		// applicant TO list
		List<ApplPersonalInfoTO> applPersonalInfoList = null;
		List<AssociateWorkInfoTO> applAssociateInfoList = null;
		List<AssociateReviewTO> associateReviewTOList = null;
		List<AssociatePrePosTO> associatePrePosTOList = null;
		List<ApplEducationInfoTO> applEducationInfoList = null;
		List<ApplWorkHistoryInfoTO> applWorkHistoryInfoList = null;
		List<SchedulePreferenceTO> applJobPrefInfoList = null;
		List<ApplLangInfoTO> applLangInfoList = null;
		List<ApplPhnScreenInfoTO> applPhnScreenInfoList = null;
		List<ApplHistoryInfoTO> applHistoryInfoList = null;
		List<ApplAppHistoryInfoTO> applAppHistoryInfoList = null;
		List<ApplAppHistoryInfoTO2> applAppHistoryInfoList2 = null;
		
		// applicant response
		ApplPersonalInfoResponse applPersonalRes = new ApplPersonalInfoResponse();
		AssociateWorkInfoResponse applAssociateRes = new AssociateWorkInfoResponse();
		AssociateReviewResponse associateReviewRes = new AssociateReviewResponse();
		AssociatePrePosResponse associatePrePosRes = new AssociatePrePosResponse();
		ApplEducationInfoResponse applEducationRes = new ApplEducationInfoResponse();
		ApplWorkHistoryInfoResponse applWorkHistoryRes = new ApplWorkHistoryInfoResponse();
		ApplJobPrefInfoResponse applJobPrefRes = new ApplJobPrefInfoResponse();
		ApplLangInfoResponse applLangRes = new ApplLangInfoResponse();
		ApplPhnScreenInfoResponse applPhnScreenRes = new ApplPhnScreenInfoResponse();
		ApplHistoryInfoResponse applHistoryRes = new ApplHistoryInfoResponse();
		ApplApplicationHistoryInfoResponse applAppHistoryRes = new ApplApplicationHistoryInfoResponse();
		ApplApplicationHistoryInfoResponse2 applAppHistoryRes2 = new ApplApplicationHistoryInfoResponse2();
		
		try
		{
			if(applNo != null && !applNo.trim().equals(""))
			{
				// personal info
				applPersonalInfoList = applProfileDetailDAO.getApplPersonalInfo(applNo, applType);
				
				if (applPersonalInfoList != null && applPersonalInfoList.size() > 0) {
					applPersonalRes.setApplPersonalInfoList(applPersonalInfoList);
					applProfileRes.setAppPersonalInfoRes(applPersonalRes);
					// fixing profile issue 06-06-2011
					emplApplDate = applPersonalInfoList.get(0).getEmplApplicationDate();
				}
				
				// associate info
				if (applType.length() > 0 && applType.equalsIgnoreCase("AS")) {
					applAssociateInfoList = applProfileDetailDAO.getAssociateInfo(applNo);
					if (applAssociateInfoList != null && applAssociateInfoList.size() > 0) {
						applAssociateRes.setApplAssociateInfoTOList(applAssociateInfoList);
						applProfileRes.setAppAssociateInfoRes(applAssociateRes);
					}
				}
				
				// associate review
				if (applType.length() >0 && applType.equalsIgnoreCase("AS")) {
					//Changed to show Performance Description and fixed where it shows multiple reviews per year.
					//1st need Associate ID based on ApplicantId to get Review Information
					String associateId = ApplProfileDetailDAO.getAssociateIdByApplicantId(applNo);
					if (logger.isDebugEnabled()) {
						logger.debug(String.format("ApplicantID:%1$s returns AssociateId:%2$s", applNo, associateId));
					}
					
					List<AssociateReviewTO> associateReviewTOListComplete = ApplProfileDetailDAO.getAssociateReviewRevised(associateId);
					//Should only show one review per year.
					String currentLine = "";
					associateReviewTOList = new ArrayList<AssociateReviewTO>();
					for (int i = 0; i < associateReviewTOListComplete.size(); i++) {
						AssociateReviewTO temp = associateReviewTOListComplete.get(i);
						if (!currentLine.equals(temp.getDate().substring(0, 4))) {
							currentLine = temp.getDate().substring(0, 4);
							//Prior to 2002 reviews were just numbers, so replace description with value
							if (Integer.parseInt(temp.getDate().substring(0, 4)) < 2002) {
								temp.setResults(temp.getPerformance());
							}
							associateReviewTOList.add(temp);
							logger.debug(currentLine);
						}
					}					
					
					//Old
					//associateReviewTOList = applProfileDetailDAO.getAssociateReview(applNo);
					
					if (associateReviewTOList != null && associateReviewTOList.size() > 0) {
						associateReviewRes.setApplAssociateInfoTOList(associateReviewTOList);
						applProfileRes.setAssociateReviewRes(associateReviewRes);
					}
				}
				
				// associate previous position
				if (applType.length() >0 && applType.equalsIgnoreCase("AS")) {
					associatePrePosTOList = applProfileDetailDAO.getAssociatePrevPosistion(applNo);
					if (associatePrePosTOList != null && associatePrePosTOList.size() > 0) {
						associatePrePosRes.setAssociatePrePosTOList(associatePrePosTOList);
						applProfileRes.setAssociatePrePosRes(associatePrePosRes);
					}
				}
				
				// education
				applEducationInfoList = applProfileDetailDAO.getApplEducationInfo(applNo, applType, emplApplDate);
				if (applEducationInfoList != null && applEducationInfoList.size() > 0) {
					applEducationRes.setApplEducationInfoTOList(applEducationInfoList);
					applProfileRes.setApplEducationInfoRes(applEducationRes);
				}
				
				// work history
				applWorkHistoryInfoList = applProfileDetailDAO.getApplWorkHistroyInfo(applNo, applType, emplApplDate);
				if (applWorkHistoryInfoList != null && applWorkHistoryInfoList.size() > 0) {
					applWorkHistoryRes.setApplWorkHistoryInfoTOlist(applWorkHistoryInfoList);
					applProfileRes.setApplWorkHistoryInfoRes(applWorkHistoryRes);
				}
				
				// job preference
				applJobPrefInfoList = applProfileDetailDAO.getApplJobPrefInfo(applNo);
				if (applJobPrefInfoList != null && applJobPrefInfoList.size() > 0) {
					applJobPrefRes.setApplJobPrefInfoTO(applJobPrefInfoList);
					applProfileRes.setApplJobPrefInfoRes(applJobPrefRes);
				}
				
				// languages
				// applicant language
				if (applType != null && applType.equalsIgnoreCase("AP")) {
					applLangInfoList = applProfileDetailDAO.getApplLangInfo(applNo, applType, emplApplDate);
				}
				//associate language
				if (applType != null && applType.equalsIgnoreCase("AS")) {
					applLangInfoList = applProfileDetailDAO.getAssoLangInfo(applNo, applType);
				}
				if (applLangInfoList != null && applLangInfoList.size() > 0) {
					applLangRes.setApplLangInfoTOList(applLangInfoList);
					applProfileRes.setApplLangInfoRes(applLangRes);
				}
				
				// candidate / applicant history
				applHistoryInfoList = applProfileDetailDAO.getApplHistoryInfo(applNo);
				if (applHistoryInfoList != null && applHistoryInfoList.size() > 0) {
					applHistoryRes.setApplHistoryInfoTOList(applHistoryInfoList);
					applProfileRes.setApplHistoryInfoRes(applHistoryRes);
				}
				
				applPhnScreenInfoList = applProfileDetailDAO.getApplPhnScreenInfo(applNo);
				//Business requested that the result be sorted by Phone Screen in Desc Order MTS1876 06/20/2016
				Collections.sort(applPhnScreenInfoList, new Comparator<ApplPhnScreenInfoTO>() {
			        @Override
			        public int compare(final ApplPhnScreenInfoTO object1, final ApplPhnScreenInfoTO object2) {
			        	Integer i = new Integer(object2.getRequisitionNum()); 
			        	int result = i.compareTo(new Integer(object1.getRequisitionNum()));
			            return result;
			        }
			    });				
				if (applPhnScreenInfoList != null && applPhnScreenInfoList.size() > 0) {
					applPhnScreenRes.setApplPhnScreenInfoList(applPhnScreenInfoList);
					applProfileRes.setApplPhnScreenInfoRes(applPhnScreenRes);
				}
				
				// application history - 1
				if (applType != null && applType.equalsIgnoreCase("AP")) {
					applAppHistoryInfoList = applProfileDetailDAO.getApplAppHistoryInfo(applNo, applType, applDate);
				}
				if (applType != null && applType.equalsIgnoreCase("AS")) {
					applAppHistoryInfoList = applProfileDetailDAO.getAssoAppHistoryInfo(applNo, applType);
				}
			
				
				if (applAppHistoryInfoList != null && applAppHistoryInfoList.size() > 0) {
					applAppHistoryRes.setApplAppHistoryInfoTOList(applAppHistoryInfoList);
					applProfileRes.setApplAppHistoryInfoRes(applAppHistoryRes);
				}
				
				// application history - 2
				if (applType != null && applType.equalsIgnoreCase("AP")) {
					applAppHistoryInfoList2 = applProfileDetailDAO.getApplAppHistoryInfo2(applNo, applType, applDate);
				}
				if (applType != null && applType.equalsIgnoreCase("AS")) {
					applAppHistoryInfoList2 = applProfileDetailDAO.getAssoAppHistoryInfo2(applNo, applType);
				} 
				
				if (applAppHistoryInfoList2 != null && applAppHistoryInfoList2.size() > 0) {
					applAppHistoryRes2.setApplAppHistoryInfoTOList2(applAppHistoryInfoList2);
					applProfileRes.setApplAppHistoryInfoRes2(applAppHistoryRes2);
				}
			}
			else
			{				
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this
				+ "Leaves getRequisitionDetails method in Manager with output"
				+ applProfileRes);
		
		return applProfileRes;
	}

	//RSA 7.2 
	/**
	 * The manager method will return the latest
	 * rejection reason for this candidate for this requisition
	 * @param 
	 * @return 
	 * @throws RetailStaffingException
	 */
	public Response getLatestRejectReason(final GetLatestRejectionRequest getLatestRejectDetails) throws RetailStaffingException
	{
		Response res = null;
		
		
		try
		{
			ApplRejectionDAO RejectionDAO = new ApplRejectionDAO();
			
			if(logger.isDebugEnabled())
			{
			logger.debug("Inside getLatestRejectReason") ;
			}
			int rejReason = RejectionDAO.readEmploymentPositionCandidateRejected(getLatestRejectDetails.getReqDetailsTO().getCandID(), Integer.parseInt(getLatestRejectDetails.getReqDetailsTO().getReqNbr()));
			res = new Response();
			
			res.setRejectedReasonId(rejReason);
			res.setSuccessMsg(RetailStaffingConstants.REJECTION_DETAILS_FOUND) ;
			res.setStatus(RetailStaffingConstants.SUCCESS_APP_STATUS) ;
			
			logger.debug("RejectReasonCode: " + rejReason) ;

		}

		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return res;
	}

	
	//RSA 7.2 
	/**
	 * The manager method will return the list of all possible
	 * rejection reasons
	 * @param 
	 * @return 
	 * @throws RetailStaffingException
	 */
	public Response getRejectDetails() throws RetailStaffingException
	{
		Response res = null;
		List<ReadNlsEmploymentPositionCandidateRejectedReasonByInputListDTO> rejList = null;
		RejectionComboResponse rejComboResponse = null;
		
		try
		{
			ApplRejectionDAO RejectionDAO = new ApplRejectionDAO();
			// Get the available reasons.
			
			if(logger.isDebugEnabled())
			{
			logger.debug("Inside getRejectDetails") ;
			}
			rejList = RejectionDAO.readNlsEmploymentPositionCandidateRejectedReasonByInputList();
			res = new Response();
			rejComboResponse = new RejectionComboResponse();
			rejComboResponse.setRejectList(rejList);
			res.setRejectList(rejComboResponse);
			
			logger.debug("RejList Contents: "+rejList.toString()) ;

		}

		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		return res;
	}
	
	/**
	 * This method is used to create the rejection details based on the of
	 * review completed phone screens
	 * 
	 * @param UpdateReviewPhnScrnRequest
	 * @return Response
	 * @throws RetailStaffingException
	 */
	public Response createEmploymentPositionCandidateRejected(final CreateRejectionRequest createRejectDetails) throws RetailStaffingException {
		logger.info(this + "Enters createEmploymentPositionCandidateRejected method in Manager with input as" + createRejectDetails);
		Response res = new Response();
		ApplRejectionDAO rejDao = new ApplRejectionDAO();

		try 
		{
			if (createRejectDetails != null) 
			{
				rejDao.createEmploymentPositionCandidateRejected(createRejectDetails.getReqDetailsTO().getCandID(), 
						Integer.parseInt(createRejectDetails.getReqDetailsTO().getReqNbr()), Integer.parseInt(createRejectDetails.getReqDetailsTO().getRejectionCode())) ;
				
				res.setSuccessMsg(REJECTION_DETAILS_CREATED);
				
			}

			

		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.INSERTING_REJECTION_DETAILS_ERROR_CODE,
					RetailStaffingConstants.INSERTING_REJECTION_DETAILS_ERROR_MSG + "requisition number: "
							+ createRejectDetails.getReqDetailsTO().getReqNbr(), e);
		}
		logger.info(this + "Leaves createEmploymentPositionCandidateRejected method in Manager with output" + res);
		return res;
	}
	
	/**
	 * This method is used to add Considered Status from the QP
	 * 
	 * @param QPConsideredApplicantRequest
	 * @return void
	 * @throws RetailStaffingException
	 */
	public void markApplicantsAsConsideredInQP(final QPConsideredApplicantRequest applicantList) throws RetailStaffingException {

		long startTime = 0;
		if (logger.isDebugEnabled()) {
			startTime = System.nanoTime();
			logger.debug(String.format("Entering Manager markApplicantsAsConsideredInQP()"));
		}
		
		ApplRejectionDAO dao = new ApplRejectionDAO();
		QPConsideredApplicantDetailTO qPListedApplicantDetailTO = null;
		try 
		{
			if (applicantList != null) 
			{
				for (int i=0;i<applicantList.getApplicantList().size();i++) {
					qPListedApplicantDetailTO = new QPConsideredApplicantDetailTO();
					qPListedApplicantDetailTO = (QPConsideredApplicantDetailTO) applicantList.getApplicantList().get(i);
					//Reusing the Rejected DAO code, but will be putting in the Applicant Listed in QP
					dao.createEmploymentPositionCandidateRejected(qPListedApplicantDetailTO.getApplicantId(), 
							Integer.parseInt(qPListedApplicantDetailTO.getReqNbr()),
							Integer.parseInt(qPListedApplicantDetailTO.getConsideredCode())) ;
				}	
			}
			
		} catch (RetailStaffingException e) {
			throw e;
		} catch (Exception e) {
			throw new RetailStaffingException(RetailStaffingConstants.INSERTING_MARK_APPLICANT_AS_CONSIDERED_ERROR_MSG
							, e);
		}
		if (logger.isDebugEnabled()) {
			long endTime = System.nanoTime();
			if (startTime == 0) {
				startTime = endTime;
			}
			logger.debug(String.format("Exiting Manager markApplicantsAsConsideredInQP(). Total time to process request: %1$.9f seconds", (((double) endTime - startTime) / Constants.NANOS_IN_SECOND)));
		}
	}	
}

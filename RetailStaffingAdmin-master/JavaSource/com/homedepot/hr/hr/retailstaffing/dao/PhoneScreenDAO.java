/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PhoneScreenDAO.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.handlers.PhoneScreenHandler;
import com.homedepot.hr.hr.retailstaffing.dao.handlers.PomRsaCrossRefHandler;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.DateTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrvHoursTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.MinimumResponseTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenNoteTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadHumanResourcesStoreEmploymentRequisitionDTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateStaffingRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.POMRsaStatusCrossRefResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.ApplicationObject;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.NoRowsFoundException;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.interfaces.DAOConstants;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.UpdateNotifier;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.builder.UnitOfWork;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.exceptions.UpdateException;
import com.homedepot.ta.aa.dao.stream.MapStream;

/**
 * This Class is used to have DAO logic for Phone Screen Functionality This
 * Class will have the following functionalities:- readByPhoneScreenNumber -
 * Reading the phone screen details by phone number. readInterviewDetails -
 * Reading the interview detail for a particular phone screen.
 * readRequisitionStoreLocation - Reading the requisition store location details
 * for the candidate. readStoreDetails - Reading the store details for the store
 * number. readRequisitionDetails - Reading the requisition details for the
 * given requisition number. readResponses - Reading the responses for a given
 * phone number. readPhoneScreenEmploymentRequesitionNote - Reading the employee
 * requisition note for a given phone number. readRequisitionStaffingDetails -
 * Reading the staffing details for a given requisition number.
 * readNlsInterviewRespondStatus - Reading the interview response status codes
 * for the given phone number. readHumanResourcesPhoneScreen - Reading the phone
 * screen details by phone screen number. readNlsInterviewRespondStatusList -
 * Reading the nls response status list. getCandidateDetails - Reading the
 * candidate details by phone screen number and requisition number.
 * 
 * @author TCS
 * 
 */
public class PhoneScreenDAO implements DAOConstants, RetailStaffingConstants
{
		private static final Logger logger = Logger.getLogger(PhoneScreenDAO.class);

		/**
		 * This method is used to get the
		 * phnScrnId,ReqNbr,CndID,AssosiciateID,Name,Str,DEpt,Title.
		 * 
		 * @param phnScrnNbr
		 * @return
		 * @throws QueryException
		 */
		public List<PhoneScreenIntrwDetailsTO> readByPhoneScreenNumber(int phnScrnNbr) throws RetailStaffingException
		{
			final List<PhoneScreenIntrwDetailsTO> readByPhoneScreenNumberList = new ArrayList<PhoneScreenIntrwDetailsTO>();
			try
			{
				MapStream inputs = new MapStream("readByPhoneScreenNumber");
				inputs.put("humanResourcesPhoneScreenId", phnScrnNbr);
				inputs.put("tabno", TABNO_DEPT_NO);
				inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
				inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
				inputs.put(ACTV_FLG, true);
				inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
				inputs.addQualifier("effectiveEndDateGreaterThan", true);
				inputs.put("humanResourcesSystemCountryCode", "USA");

				BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						PhoneScreenIntrwDetailsTO readByPhoneScreenNumberDTO = null;
						while(results.next())
						{
							readByPhoneScreenNumberDTO = new PhoneScreenIntrwDetailsTO();
							readByPhoneScreenNumberDTO.setItiNbr(String.valueOf(results.getInt("humanResourcesPhoneScreenId")));
							readByPhoneScreenNumberDTO.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
							if(results.getString("candidatePersonId") != null)
							{
								readByPhoneScreenNumberDTO.setAid(StringUtils.trim(results.getString("candidatePersonId")));
							}
							readByPhoneScreenNumberDTO.setName(StringUtils.trim(results.getString("candidateName")));
							readByPhoneScreenNumberDTO.setReqNbr(String.valueOf(results.getInt("employmentRequisitionNumber")));
							readByPhoneScreenNumberDTO.setCndStrNbr(StringUtils.trim(results.getString("organization1")));
							readByPhoneScreenNumberDTO.setCndDeptNbr(StringUtils.trim(results.getString("organization2")));
							readByPhoneScreenNumberDTO.setTitle(StringUtils.trim(results.getString("jobTitleId")));
							readByPhoneScreenNumberDTO.setScrTyp(StringUtils.trim(results.getString("oe31")));

							//Added for CDP, remove leading zeros for display purposes.
							readByPhoneScreenNumberDTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));
							
							readByPhoneScreenNumberList.add(readByPhoneScreenNumberDTO);
						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "phonescreen number: " + phnScrnNbr, e);
			}

			return readByPhoneScreenNumberList;
		}

		/**
		 * This method is used to get the interview details to
		 * 
		 * @param phnScrnNbr
		 * @return list of interview details
		 * @throws QueryException
		 */
		public List<IntrwLocationDetailsTO> readInterviewDetails(int phnScrnNbr) throws RetailStaffingException
		{
			final List<IntrwLocationDetailsTO> readInterviewDetailsList = new ArrayList<IntrwLocationDetailsTO>();
			try
			{
				MapStream inputs = new MapStream("readInterviewDetails");
				inputs.put("humanResourcesPhoneScreenId", phnScrnNbr);
				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						IntrwLocationDetailsTO readInterviewDetailsDTO = null;
						while(results.next())
						{
							readInterviewDetailsDTO = new IntrwLocationDetailsTO();
							readInterviewDetailsDTO.setInterviewDate(Util.converDatetoDateTO(results.getDate("interviewDate")));
							readInterviewDetailsDTO.setInterviewTime(Util.converTimeStampTimeStampTO(results.getTimestamp("interviewTimestamp")));
							readInterviewDetailsDTO.setInterviewLocId(String.valueOf(results.getShort("interviewLocationTypeCode")));
							readInterviewDetailsDTO.setInterviewLocName(StringUtils.trim(results.getString("interviewLocationDescription")));
							readInterviewDetailsDTO.setPhone(StringUtils.trim(results.getString("interviewPhoneNumber")));
							readInterviewDetailsDTO.setAdd(StringUtils.trim(results.getString("interviewAddressText")));
							readInterviewDetailsDTO.setCity(StringUtils.trim(results.getString("interviewCityName")));
							readInterviewDetailsDTO.setState(StringUtils.trim(results.getString("interviewStateCode")));
							readInterviewDetailsDTO.setZip(StringUtils.trim(results.getString("interviewZipCodeCode")));
							readInterviewDetailsDTO.setInterviewer(StringUtils.trim(results.getString("interviewerName")));
							readInterviewDetailsList.add(readInterviewDetailsDTO);
						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_INTERVIEW_DETAILS_ERROR_CODE,
				    FETCHING_INTERVIEW_DETAILS_ERROR_MSG + "phonescreen number: " + phnScrnNbr, e);
			}
			return readInterviewDetailsList;
		}

		/**
		 * This method is used to get the requisition details
		 * 
		 * @param reqNbr
		 * @return list of requisition details
		 * @throws QueryException
		 */
		public static List<RequisitionDetailTO> readRequisitionDetails(int reqNbr) throws RetailStaffingException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug("Enter PhoneScreenDAO - readRequisitionDetails() ");
				logger.debug("requisition Number :" + reqNbr);
			}

			final List<RequisitionDetailTO> readRequisitionDetailsList = new ArrayList<RequisitionDetailTO>();
			try
			{
				MapStream inputs = new MapStream("readRequisitionDetails");
				inputs.put("employmentRequisitionNumber", reqNbr);
				inputs.put("tabno", TABNO_DEPT_NO);

				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						RequisitionDetailTO readRequisitionDetailsDTO = null;
						while(results.next())
						{
							readRequisitionDetailsDTO = new RequisitionDetailTO();
							readRequisitionDetailsDTO.setReqNbr(String.valueOf(results.getInt("employmentRequisitionNumber")));
							readRequisitionDetailsDTO.setDateCreate(String.valueOf(results.getDate("createTimestamp")));
							readRequisitionDetailsDTO.setStore(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
							readRequisitionDetailsDTO.setCreator(StringUtils.trim(results.getString("createUserId")));
							readRequisitionDetailsDTO.setDept(StringUtils.trim(results.getString("humanResourcesSystemDepartmentNumber")));
							readRequisitionDetailsDTO.setJob(StringUtils.trim(results.getString("jobTitleCode")));
							readRequisitionDetailsDTO.setJobTtl(StringUtils.trim(results.getString("jobTitleDescription")));
							readRequisitionDetailsDTO.setFillDt(String.valueOf(results.getDate("requiredPositionFillDate")));
							readRequisitionDetailsDTO.setOpenings(String.valueOf(results.getShort("openPositionCount")));
							if(results.getObject("interviewCandidateCount") != null)
							{
								readRequisitionDetailsDTO.setReqNumInterviews(String.valueOf(results.getObject("interviewCandidateCount")));
							}
							if(results.getBoolean("fullTimeRequiredFlag"))
							{
								readRequisitionDetailsDTO.setFt(TRUE);
							}
							else
							{
								readRequisitionDetailsDTO.setFt(FALSE);
							}

							if(results.getBoolean("partTimeRequiredFlag"))
							{
								readRequisitionDetailsDTO.setPt(TRUE);
							}
							else
							{
								readRequisitionDetailsDTO.setPt(FALSE);
							}

							if(results.getBoolean("applicantTemporaryFlag"))
							{
								readRequisitionDetailsDTO.setSealTempJob(TRUE);
							}
							else
							{
								readRequisitionDetailsDTO.setSealTempJob(FALSE);
							}

							// Get RSC to Schedule MTS1876 09/28/10
							// If Requisition was created in AIMS then there is not
							// a record in THD_STR_EMPLT_REQN need to set
							// rscScheduleFlag to True for the default
							if(results.getString("requisitionStatusCode") == null)
							{
								readRequisitionDetailsDTO.setRscSchdFlg(TRUE);
							}
							else
							{
								if(results.getBoolean("rscScheduleFlag"))
								{
									readRequisitionDetailsDTO.setRscSchdFlg(TRUE);
								}
								else
								{
									readRequisitionDetailsDTO.setRscSchdFlg(FALSE);
								}
							}

							//RSC To Manage Flag
							if (!results.wasNull("retailStaffingCenterRequisitionManagedIndicator")) {
									readRequisitionDetailsDTO.setRscToManageFlg(results.getString("retailStaffingCenterRequisitionManagedIndicator"));								
							} else {
								//If it is an old AIMS Requisition then it may not have a record in THD_STR_EMPLT_REQN
								readRequisitionDetailsDTO.setRscToManageFlg("N");
							}
							
							//Requisition Candidate Type.  Right now this is not being used in Flex, but is stubbed if needed
							if (!results.wasNull("candidateRequisitionTypeCode")) {
								switch (results.getShort("candidateRequisitionTypeCode")) {
									case 1:
										readRequisitionDetailsDTO.setReqCandidateType("Internal");
										break;
									case 2:
										readRequisitionDetailsDTO.setReqCandidateType("External");
										break;
									case 3:
										readRequisitionDetailsDTO.setReqCandidateType("Internal, External");
										break;
								}
							}
							
							if(results.getBoolean("activeFlag"))
							{
								readRequisitionDetailsDTO.setActive(TRUE);
							}
							else
							{
								readRequisitionDetailsDTO.setActive(FALSE);
							}
							readRequisitionDetailsDTO.setRequisitionStatusCode(String.valueOf(results.getShort("requisitionStatusCode")));

							readRequisitionDetailsDTO.setScrTyp(StringUtils.trim(results.getString("oe31")));

							// Calendar ID
							readRequisitionDetailsDTO.setReqCalId(results.getInt("requisitionCalendarId"));

							// Interview Duration
							readRequisitionDetailsDTO.setInterviewDurtn(String.valueOf(results.getInt("interviewMinutes")));
							readRequisitionDetailsList.add(readRequisitionDetailsDTO);

						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_REQUISITION_DETAILS_ERROR_CODE,
				    FETCHING_REQUISITION_DETAILS_ERROR_MSG + "requisition number: " + reqNbr, e);
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit PhoneScreenDAO - readRequisitionDetails() ");
			}
			return readRequisitionDetailsList;
		}

		/**
		 * This method is used to get the minimum response
		 * 
		 * @param phnScrnNbr
		 * @return list minimum responses
		 * @throws QueryException
		 */
		public List<MinimumResponseTO> readResponses(int phnScrnNbr) throws RetailStaffingException
		{
			final List<MinimumResponseTO> readResponsesList = new ArrayList<MinimumResponseTO>();
			try
			{
				MapStream inputs = new MapStream("readResponses");
				inputs.put("humanResourcesPhoneScreenId", phnScrnNbr);

				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						MinimumResponseTO readResponsesDTO = null;
						while(results.next())
						{
							readResponsesDTO = new MinimumResponseTO();
							readResponsesDTO.setSeqNbr(String.valueOf(results.getShort("sequenceNumber")));
							if(results.getString("minimumRequirementFlag") != null && results.getString("minimumRequirementFlag").trim().equalsIgnoreCase(TRUE))
							{
								readResponsesDTO.setMinimumStatus(MINIMUM_REQUR_YES_FLG);
							}
							else if(results.getString("minimumRequirementFlag") != null
							    && results.getString("minimumRequirementFlag").trim().equalsIgnoreCase(FALSE))
							{
								readResponsesDTO.setMinimumStatus(MINIMUM_REQUR_NO_FLG);
							}
							else
							{
								readResponsesDTO.setMinimumStatus(MINIMUM_REQUR_NA_FLG);
							}
							readResponsesList.add(readResponsesDTO);
						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_MINRESPONSES_DETAILS_ERROR_CODE, FETCHING_MINRESPONSES_DETAILS_ERROR_MSG + "phonescreen number: "
				    + phnScrnNbr, e);
			}
			return readResponsesList;
		}

		/**
		 * The method will be used for reading the employee requisition note for the
		 * given phone screen number and note type code.
		 * 
		 * @param phnScrnNbr
		 *            - the given phone screen number.
		 * 
		 * @param noteTypCode
		 *            - the given note type code.
		 * @return
		 * @throws RetailStaffingException
		 */
		public List<PhoneScreenIntrwDetailsTO> readPhoneScreenEmploymentRequesitionNote(int phnScrnNbr, final short noteTypCode) throws RetailStaffingException
		{
			final List<PhoneScreenIntrwDetailsTO> readPhoneScreenEmploymentRequesitionNoteList = new ArrayList<PhoneScreenIntrwDetailsTO>();
			try
			{
				MapStream inputs = new MapStream("readHumanResourcesPhoneScreenNote");
				inputs.put("humanResourcesPhoneScreenId", phnScrnNbr);
				inputs.put("humanResourcesNoteTypeCode", noteTypCode);

				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						PhoneScreenIntrwDetailsTO readPhoneScreenEmploymentRequesitionNoteDTO = null;
						while(results.next())
						{
							readPhoneScreenEmploymentRequesitionNoteDTO = new PhoneScreenIntrwDetailsTO();
							String cleanSpecialChars = null;
							if(noteTypCode == PHN_SCRN_RESPONSE_NOTE_TYP_CD)
							{
								//Production Issue 2/22/2011, Bullets being pasted into detail response text field causes an invalid character to be placed in the
								//returned XML from Flex.  Therefore, remove all Control Characters before sending to Flex.  MTS1876
								if (StringUtils.trim(results.getString("phoneScreenNoteText")) != null) {
									cleanSpecialChars = results.getString("phoneScreenNoteText").replaceAll("[^\\p{Graph}\\p{Blank}]", ". ");
									readPhoneScreenEmploymentRequesitionNoteDTO.setDetailTxt(StringUtils.trim(cleanSpecialChars));
								}

							}
							else if(noteTypCode == CONTACT_HISTRY_NOTE_TYP_CD)
							{
								//Production Issue 2/22/2011, Bullets being pasted into detail response text field causes an invalid character to be placed in the
								//returned XML from Flex.  Therefore, remove all Control Characters before sending to Flex.  MTS1876  Added
								//here too just in case it happens in Contact History.
								if (StringUtils.trim(results.getString("phoneScreenNoteText")) != null) {
									cleanSpecialChars = results.getString("phoneScreenNoteText").replaceAll("[^\\p{Graph}\\p{Blank}]", ". ");
									readPhoneScreenEmploymentRequesitionNoteDTO.setContactHistoryTxt(StringUtils.trim(cleanSpecialChars));
								}

							}
							readPhoneScreenEmploymentRequesitionNoteList.add(readPhoneScreenEmploymentRequesitionNoteDTO);
						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_REQUISITIONNOTE_DETAILS_ERROR_CODE, FETCHING_REQUISITIONNOTE_DETAILS_ERROR_MSG + "phonescreennumber: "
				    + phnScrnNbr + " notetypecode: " + noteTypCode, e);
			}
			return readPhoneScreenEmploymentRequesitionNoteList;
		}

		/**
		 * This method is used to get the staffing details of a particular
		 * requisition.
		 * 
		 * @param reqNbr
		 * @return list of staffing details
		 * @throws QueryException
		 */
		public List<StaffingDetailsTO> readRequisitionStaffingDetails(final int reqNbr) throws RetailStaffingException
		{
			final List<StaffingDetailsTO> readRequisitionStaffingDetailsList = new ArrayList<StaffingDetailsTO>();
			try
			{
				MapStream inputs = new MapStream("readRequisitionStaffingDetails");
				inputs.put("employmentRequisitionNumber", reqNbr);

				BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						StaffingDetailsTO readRequisitionStaffingDetailsDTO = null;
						while(results.next())
						{
							readRequisitionStaffingDetailsDTO = new StaffingDetailsTO();
							readRequisitionStaffingDetailsDTO.setHrgMgrName(StringUtils.trim(results.getString("hireManagerName")));
							readRequisitionStaffingDetailsDTO.setHrgMgrTtl(StringUtils.trim(results.getString("jobTitleDescription")));
							readRequisitionStaffingDetailsDTO.setHrgMgrPhn(StringUtils.trim(results.getString("hireManagerPhoneNumber")));
							readRequisitionStaffingDetailsDTO.setDaysTmMgrAvble(StringUtils.trim(results.getString("hireManagerAvailabilityText")));
							readRequisitionStaffingDetailsDTO.setWeekBeginDt(Util.converDatetoDateTO(results.getDate("weekBeginDate")));
							readRequisitionStaffingDetailsDTO.setRequestNbr(StringUtils.trim(results.getString("requestNumber")));
							readRequisitionStaffingDetailsDTO.setDesiredExp(String.valueOf(results.getShort("targetExperienceLevelCode")));
							// if
							// (results.wasNull("targetExperienceLevelCode"))
							// {
							// }
							if(!(results.wasNull("targetPayAmount")))
							{
								readRequisitionStaffingDetailsDTO.setTargetPay(String.valueOf(results.getBigDecimal("targetPayAmount")));
							}
							readRequisitionStaffingDetailsDTO.setQualPoolNts(StringUtils.trim(results.getString("requisitionNoteTextOne")));
							readRequisitionStaffingDetailsDTO.setReferals(StringUtils.trim(results.getString("requisitionNoteTextTwo")));
							if(results.getBoolean("hireEventFlag"))
							{
								readRequisitionStaffingDetailsDTO.setHrgEvntFlg("Y");
							}
							else
							{
								readRequisitionStaffingDetailsDTO.setHrgEvntFlg("N");
							}

							readRequisitionStaffingDetailsDTO.setStfHrgEvntStartDt(Util.converDatetoDateTO(results.getDate("hireEventBeginDate")));
							readRequisitionStaffingDetailsDTO.setStfHrgEvntEndDt(Util.converDatetoDateTO(results.getDate("hireEventEndDate")));
							readRequisitionStaffingDetailsDTO.setStfhrgEvntLoc(StringUtils.trim(results.getString("hireEventLocationDescription")));
							readRequisitionStaffingDetailsDTO.setStfhrgEvntLocPhn(StringUtils.trim(results.getString("hireEventPhoneNumber")));
							readRequisitionStaffingDetailsDTO.setAdd(StringUtils.trim(results.getString("hireEventAddressText")));
							readRequisitionStaffingDetailsDTO.setCity(StringUtils.trim(results.getString("hireEventCityName")));
							readRequisitionStaffingDetailsDTO.setZip(StringUtils.trim(results.getString("hireEventZipCodeCode")));
							readRequisitionStaffingDetailsDTO.setState(StringUtils.trim(results.getString("hireEventStateCode")));
							logger.info("result " + results.getTime("hireEventBeginTime"));
							readRequisitionStaffingDetailsDTO.setStartTime(Util.converTimeTimeStampTO(results.getTime("hireEventBeginTime")));

							logger.info("readRequisitionStaffingDetailsDTO.getStartTime()" + readRequisitionStaffingDetailsDTO.getStartTime());
							readRequisitionStaffingDetailsDTO.setEndTime(Util.converTimeTimeStampTO(results.getTime("hireEventEndTime")));
							readRequisitionStaffingDetailsDTO.setBreaks(StringUtils.trim(results.getString("hireEventBreakText")));
							readRequisitionStaffingDetailsDTO.setLunch(Util.converTimeTimeStampTO(results.getTime("lunchBeginTime")));

							if(!results.wasNull("interviewDurValue"))
							{
								readRequisitionStaffingDetailsDTO.setInterviewDurtn(String.valueOf(results.getShort("interviewDurValue")));
							}

							if(!(results.wasNull("interviewTimeSlotValue")))
							{
								readRequisitionStaffingDetailsDTO.setInterviewTmSlt(String.valueOf(results.getShort("interviewTimeSlotValue")));
							}

							readRequisitionStaffingDetailsDTO.setHiringEventID(String.valueOf(results.getInt("hireEventId")));
							readRequisitionStaffingDetailsDTO.setLastIntrTm(Util.converTimeTimeStampTO(results.getTime("lastInterviewTime")));
							
							//ATS Hiring Events
							readRequisitionStaffingDetailsDTO.setHireEvntType(StringUtils.trim(results.getString("hireEventTypeIndicator")));								
							readRequisitionStaffingDetailsDTO.setHireEvntMgrAssociateId(StringUtils.trim(results.getString("emgrHumanResourceAssociateId")));
							
							readRequisitionStaffingDetailsList.add(readRequisitionStaffingDetailsDTO);
						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_STAFFING_DETAILS_ERROR_CODE, FETCHING_STAFFING_DETAILS_ERROR_MSG + "requisition number: " + reqNbr, e);
			}
			return readRequisitionStaffingDetailsList;
		}

		/**
		 * The method is used for reading the phone screen details for the given
		 * phone screen number.
		 * 
		 * @param phnScrnNbr
		 *            - the given phone screen number.
		 * 
		 * @return list of phone screen interview details
		 * @throws RetailStaffingException
		 */
		public static List<PhoneScreenIntrwDetailsTO> readHumanResourcesPhoneScreen(final int phnScrnNbr) throws RetailStaffingException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug("Enter PhoneScreenDAO - readHumanResourcesPhoneScreen() ");
				logger.debug("phnScrnNbr :" + phnScrnNbr);
			}

			final List<PhoneScreenIntrwDetailsTO> readHumanResourcesPhoneScreenList = new ArrayList<PhoneScreenIntrwDetailsTO>();
			try
			{
				MapStream inputs = new MapStream("readHumanResourcesPhoneScreen");
				inputs.put("humanResourcesPhoneScreenId", phnScrnNbr);
				BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						PhoneScreenIntrwDetailsTO readHumanResourcesPhoneScreenDTO = null;
						while(results.next())
						{
							readHumanResourcesPhoneScreenDTO = new PhoneScreenIntrwDetailsTO();
							readHumanResourcesPhoneScreenDTO.setItiNbr(String.valueOf(phnScrnNbr));							
							readHumanResourcesPhoneScreenDTO.setCndtNbr(StringUtils.trim(results.getString("employmentPositionCandidateId")));
							readHumanResourcesPhoneScreenDTO.setCndStrNbr(StringUtils.trim(results.getString("humanResourcesSystemStoreNumber")));
							readHumanResourcesPhoneScreenDTO.setReqNbr(String.valueOf(results.getInt("employmentRequisitionNumber")));
							readHumanResourcesPhoneScreenDTO.setAid(StringUtils.trim(results.getString("candidatePersonId")));
							readHumanResourcesPhoneScreenDTO.setName(StringUtils.trim(results.getString("candidateName")));
							readHumanResourcesPhoneScreenDTO.setOverAllStatus(String.valueOf(results.getShort("overallRespondStatusCode")));
							if(results.wasNull("overallRespondStatusCode"))
							{
								// need to verify
							}

							// phoneScreenStatusCode
							readHumanResourcesPhoneScreenDTO.setPhoneScreenStatusCode(String.valueOf(results.getShort("phoneScreenStatusCode")));
							if(results.wasNull("phoneScreenStatusCode"))
							{
								// set it (21) which is "Initiate Self Screen"
								readHumanResourcesPhoneScreenDTO.setPhoneScreenStatusCode("21");
							}

							// interviewStatusCode
							readHumanResourcesPhoneScreenDTO.setInterviewStatusCode(String.valueOf(results.getShort("interviewStatusCode")));

							// interviewMaterialStatusCode
							readHumanResourcesPhoneScreenDTO.setInterviewMaterialStatusCode(String.valueOf(results.getShort("interviewMaterialStatusCode")));

							readHumanResourcesPhoneScreenDTO.setYnstatus(String.valueOf(results.getShort("minimumRequirementStatusCode")));
							readHumanResourcesPhoneScreenDTO.setPhnScreener(StringUtils.trim(results.getString("phoneScreenName")));

							if(results.getDate("phoneInterviewDate") != null)
							{
								readHumanResourcesPhoneScreenDTO.setPhnScrnDate(Util.converDatetoDateTO(results.getDate("phoneInterviewDate")));
							}
							else
							{
								DateTO ss = new DateTO();
								ss.setYear("0");
								ss.setMonth("0");
								ss.setDay("0");
								readHumanResourcesPhoneScreenDTO.setPhnScrnDate(ss);
							}

							if(results.getTimestamp("phoneInterviewTimestamp") != null)
							{
								readHumanResourcesPhoneScreenDTO.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("phoneInterviewTimestamp")));
							}
							else
							{
								TimeStampTO timeStampTO = new TimeStampTO();
								timeStampTO.setMonth("0");
								timeStampTO.setDay("0");
								timeStampTO.setYear("0");
								timeStampTO.setHour(null);
								timeStampTO.setMinute(null);
								timeStampTO.setSecond(null);
								readHumanResourcesPhoneScreenDTO.setPhnScrnTime(timeStampTO);
							}
							// phoneScreenStatusTimestamp

							if(results.getTimestamp("phoneScreenStatusTimestamp") != null)
							{
								readHumanResourcesPhoneScreenDTO.setPhoneScreenStatusTimestamp(Util.converTimeStampTimeStampTO(results
								    .getTimestamp("phoneScreenStatusTimestamp")));

							}

							if(results.getTimestamp("interviewStatusTimestamp") != null)
							{
								readHumanResourcesPhoneScreenDTO.setInterviewStatusTimestamp(Util.converTimeStampTimeStampTO(results
								    .getTimestamp("interviewStatusTimestamp")));
							}

							if(results.getTimestamp("interviewMaterialStatusTimestamp") != null)
							{
								readHumanResourcesPhoneScreenDTO.setInterviewMaterialStatusTimestamp(Util.converTimeStampTimeStampTO(results
								    .getTimestamp("interviewMaterialStatusTimestamp")));
							}
							logger.info("phone screener" + readHumanResourcesPhoneScreenDTO.getPhnScreener());

							readHumanResourcesPhoneScreenDTO.setEmailAdd(StringUtils.trim(results.getString("electronicMailAddressText")));

							readHumanResourcesPhoneScreenDTO.setReqCalId(String.valueOf(results.getInt("requisitionCalendarId")));

							//Added for CDP
							// TODO Replace with actual returned field name
							//readHumanResourcesPhoneScreenDTO.setCandRefNbr("CDP" + StringUtils.trim(results.getString("employmentPositionCandidateId")));
							//Added for CDP, remove leading zeros for display purposes.
							readHumanResourcesPhoneScreenDTO.setCandRefNbr(StringUtils.trim(results.getString("employmentCandidateId").replaceAll("^0+","")));							

							if (!results.wasNull("phoneScreenDispositionCode")) {
								readHumanResourcesPhoneScreenDTO.setPhoneScreenDispositionCode(results.getShort("phoneScreenDispositionCode"));
							}
							
							if (!results.wasNull("phoneScreenDispositionCode")) {
								readHumanResourcesPhoneScreenDTO.setPhoneScreenDispositionCode(results.getShort("phoneScreenDispositionCode"));
							}
							
							readHumanResourcesPhoneScreenList.add(readHumanResourcesPhoneScreenDTO);

						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + "phonescreen number: " + phnScrnNbr, e);
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit PhoneScreenDAO - readHumanResourcesPhoneScreen() ");
			}
			return readHumanResourcesPhoneScreenList;
		}

		/**
		 * This method is used for getting the candidate phone number and
		 * requisition number.
		 * 
		 * @param ssnNbr
		 *            - the candidate ssn number.
		 * 
		 * @param reqNumber
		 *            - the requisition number.
		 * @return list of candidate details
		 * @throws QueryException
		 */
		public List<CandidateDetailsTO> getCandidateDetails(String ssnNbr, int reqNumber) throws QueryException
		{
			final List<CandidateDetailsTO> cndList = new ArrayList<CandidateDetailsTO>();
			MapStream inputs = new MapStream("readInternalOrExternalCandidate");
			inputs.put("employmentRequisitionNumber", reqNumber);
			inputs.put("employmentPositionCandidateId", ssnNbr);
			inputs.put("tabno", TABNO_DEPT_NO);
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					CandidateDetailsTO candidateDetailsTO = null;

					if(results.next())
					{
						candidateDetailsTO = new CandidateDetailsTO();
						if(results.getString("phoneAreaCityCode") != null && results.getString("phoneLocalNumber") != null)
						{

							candidateDetailsTO.setCanPhn(StringUtils.trim(results.getString("phoneAreaCityCode"))
							    + StringUtils.trim(results.getString("phoneLocalNumber")));
						}
						else if(results.getString("phoneLocalNumber") != null)
						{
							candidateDetailsTO.setCanPhn(StringUtils.trim(results.getString("phoneLocalNumber")));
						}

						// Below logic for generating name as lastname,
						// middlename, firstname, suffixname.
						candidateDetailsTO.setName(Util.getCandidateName(results.getString("lastName"), results.getString("firstName"), results
						    .getString("middleInitialName"), results.getString("suffixName")));
						cndList.add(candidateDetailsTO);
					}
				}
			});

			return cndList;
		}

		/**
		 * This method is used for fetching the store employment requisition details
		 * 
		 * 
		 * @param reqNO
		 *            - the requisition number.
		 * @return list of requisition details
		 * @throws QueryException
		 */

		public static List<ReadHumanResourcesStoreEmploymentRequisitionDTO> readHumanResourcesStoreEmploymentRequisition(int reqNO) throws QueryException
		{
			final List<ReadHumanResourcesStoreEmploymentRequisitionDTO> readHumanResourcesStoreEmploymentRequisitionList = new ArrayList<ReadHumanResourcesStoreEmploymentRequisitionDTO>();

			MapStream inputs = new MapStream("readHumanResourcesStoreEmploymentRequisition");
			inputs.put("employmentRequisitionNumber", reqNO);

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					ReadHumanResourcesStoreEmploymentRequisitionDTO readHumanResourcesStoreEmploymentRequisitionDTO = null;
					while(results.next())
					{
						readHumanResourcesStoreEmploymentRequisitionDTO = new ReadHumanResourcesStoreEmploymentRequisitionDTO();
						readHumanResourcesStoreEmploymentRequisitionDTO.setHumanResourcesSystemStoreNumber(results.getString("humanResourcesSystemStoreNumber"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setCreateUserId(results.getString("createUserId"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setHumanResourcesSystemDepartmentNumber(results
						    .getString("humanResourcesSystemDepartmentNumber"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setJobTitleCode(results.getString("jobTitleCode"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setRequiredPositionFillDate(results.getDate("requiredPositionFillDate"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setAuthorizationPositionCount(results.getShort("authorizationPositionCount"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setOpenPositionCount(results.getShort("openPositionCount"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setFullTimeRequiredFlag(results.getString("fullTimeRequiredFlag"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setPartorpeakTimeRequiredFlag(results.getString("partorpeakTimeRequiredFlag"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setPmRequiredFlag(results.getString("pmRequiredFlag"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setWeekendRequiredFlag(results.getString("weekendRequiredFlag"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setActiveFlag(results.getString("activeFlag"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setLastUpdateUserId(results.getString("lastUpdateUserId"));
						readHumanResourcesStoreEmploymentRequisitionDTO.setLastUpdateTimestamp(results.getTimestamp("lastUpdateTimestamp"));
						readHumanResourcesStoreEmploymentRequisitionList.add(readHumanResourcesStoreEmploymentRequisitionDTO);
					}
				}
			});

			return readHumanResourcesStoreEmploymentRequisitionList;
		}

		/**
		 * This method is used for fetching the email address of an External
		 * candidate
		 * 
		 * 
		 * @param cndtNbr
		 *            - the candidate number.
		 * @return phone screen details
		 * @throws QueryException
		 */
		public static PhoneScreenIntrwDetailsTO readApplicantEmailAddress(String cndtNbr) throws QueryException
		{
			final PhoneScreenIntrwDetailsTO readApplicantEmailAddressList = new PhoneScreenIntrwDetailsTO();

			MapStream inputs = new MapStream("readApplicantEmailAddress");
			inputs.put("employmentApplicantId", cndtNbr);

			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					while(results.next())
					{
						if(results.getString("electronicMailAddressText") != null)
						{
							readApplicantEmailAddressList.setEmailAdd(results.getString("electronicMailAddressText").trim());
						}
						else
						{ // we did not get the e-mail address.
							readApplicantEmailAddressList.setEmailAdd("");
						}

					}
				}
			});

			return readApplicantEmailAddressList;
		}

		/**
		 * This method is used for fetching the email address of an Internal
		 * candidate
		 * 
		 * 
		 * @param cndtNbr
		 *            - the candidate number.
		 * @return phone screen details
		 * @throws QueryException
		 */
		public static PhoneScreenIntrwDetailsTO readInternalApplicantEmailAddress(String cndtNbr) throws QueryException
		{
			final PhoneScreenIntrwDetailsTO readApplicantEmailAddressList = new PhoneScreenIntrwDetailsTO();

			MapStream inputs = new MapStream("readInternalEmailAddress");
			inputs.put("employmentApplicantId", cndtNbr);
			inputs.put("recordStatus", "A");
			inputs.put("addressType", BigDecimal.valueOf(1));
			inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(ACTV_FLG, true);
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThan", true);
			inputs.put("humanResourcesSystemCountryCode", "USA");
			
			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					while(results.next())
					{
						if(results.getString("accessInformation6") != null)
						{
							readApplicantEmailAddressList.setEmailAdd(StringUtils.trim(results.getString("accessInformation6")));
						}
					}
				}
			});

			return readApplicantEmailAddressList;
		}

		/**
		 * This method is used for fetching the email address of the candidate
		 * 
		 * 
		 * @param cndtNbr
		 *            - the candidate number.
		 * @return phone screen interview details
		 * @throws QueryException
		 */
		public PhoneScreenIntrwDetailsTO readAssociatePhoneNumber(String cndtNbr) throws QueryException
		{
			final PhoneScreenIntrwDetailsTO readAssociatePhoneNumberList = new PhoneScreenIntrwDetailsTO();
		
			MapStream inputs = new MapStream("readAssociatePhoneNumber");
			inputs.put("employmentApplicantId", cndtNbr);
			inputs.put(EFF_BEGIN_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(EFF_END_DATE, new Date(Calendar.getInstance().getTimeInMillis()));
			inputs.put(ACTV_FLG, true);
			inputs.addQualifier("effectiveBeginDateLessThanEqualTo", true);
			inputs.addQualifier("effectiveEndDateGreaterThan", true);
			inputs.put("humanResourcesSystemCountryCode", "USA");

			BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
			{
				public void readResults(Results results, Query query, Inputs inputs) throws QueryException
				{
					while(results.next())
					{
						readAssociatePhoneNumberList.setCanPhn(results.getString("phoneNo"));
					}
				}
			});

			return readAssociatePhoneNumberList;
		}
		
		public List<IntrvHoursTO> readIntrvHours(int reqCalId, boolean flag) throws RetailStaffingException
		{

			final List<IntrvHoursTO> readTotalInterviewTimeList = new ArrayList<IntrvHoursTO>();

			try
			{
				MapStream inputs = new MapStream("readTotalInterviewTime");
				inputs.put("activeFlag", flag);
				inputs.putAllowNull("requisitionCalendarId", reqCalId);

				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						IntrvHoursTO readTotalInterviewTimeDTO = null;
						while(results.next())
						{
							readTotalInterviewTimeDTO = new IntrvHoursTO();
							readTotalInterviewTimeDTO.setAllocatedHrs(results.getInt("totalInterviewTime"));
							logger.info("enter into total interview time");
							/*
							 * if (results.wasNull("totalInterviewTime")) {
							 * readTotalInterviewTimeDTO.setAllocatedHrs(2); }
							 */

							readTotalInterviewTimeList.add(readTotalInterviewTimeDTO);
						}
					}
				});

			}

			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_INTERVIEW_RESPONSES_DETAILS_ERROR_CODE, e);
			}
			return readTotalInterviewTimeList;
		}

		// Added by p22o0mn for Defect #3323: To Get the Flag for enough Phone
		// Screens Completed or not for a given requisition.
		// Added by sxs8544 for Deferred #3373: To get the people have been
		// scheduled for all the requested interviews for this job requisition.

		public int readRemainingInterviewCandidateCountByPhoneScreenStatusCode(int reqNbr, boolean interviewStatCdFlag) throws RetailStaffingException
		{

			final List<Integer> resultList = new ArrayList<Integer>();
			try
			{

				MapStream inputs = new MapStream("readRemainingInterviewCandidateCountByPhoneScreenStatusCode");
				inputs.put("employmentRequisitionNumber", reqNbr);
				inputs.put("activeFlag", true);
				List<Short> statusList = new ArrayList<Short>();

				if(interviewStatCdFlag)
				{
					statusList.add(INTERVIEW_SCHEDULED);// 11
					statusList.add(STORE_SCHEDULING);// 12
					inputs.put("interviewStatusCodeList", statusList); // optional

				}
				else
				{
					statusList.add(PHONE_SCREEN_COMPLETED);
					statusList.add(STORE_ADMINISTERED);
					inputs.put("phoneScreenStatusCodeList", statusList);
				}

				BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{

						while(results.next())
						{
							int completedPhnScrnCount = results.getInt("total");
							resultList.add(completedPhnScrnCount);

						}
					}
				});

			}

			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_INTERVIEW_RESPONSES_DETAILS_ERROR_CODE, e);
			}
			return resultList.get(0);
		}
		
		public static void updateHumanResourcesPhoneScreenStatus(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, short phoneScrnStatus)
		    throws QueryException
		{
			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Entering updatePhoneScreenStatusByPhoneScreenStatCode(), phoneScrnId: %1$s, phoneScrnStatus: %2$s", phoneScrnId, phoneScrnStatus));
			} // end if
			
			MapStream updateITIInputs = null;
			updateITIInputs = new MapStream("updatePhoneScreenStatusByPhoneScreenStatCode");

			try
			{
				updateITIInputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				updateITIInputs.putAllowNull("phoneScreenStatusCode", phoneScrnStatus);
				query.updateObject(daoConn, handler, updateITIInputs);
			} // end try
			catch (QueryException e)
			{
				// log the error if it is not a No Rows Found Exception
				if (!e.getMessage().equals("No Rows Found")){
					logger.error("An exception occurred updating the phone screen status", e);
				}
				// throw the error back to the caller
				//throw e;
			} // end catch

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Exiting updatePhoneScreenStatusByPhoneScreenStatCode(), phoneScrnId: %1$s, phoneScrnStatus: %2$s", phoneScrnId, phoneScrnStatus));
			} // end if
		} // end function updateHumanResourcesPhoneScreenStatus()

		public static void updatePhoneScreenStatusByInterviewScreenStatCode(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId,
		    short interviewSchedStatus) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - updatePhoneScreenStatusByInterviewScreenStatCode(),"
				    + "phoneScrnId: %1$s, interviewSchedStatus: %2$s", phoneScrnId, interviewSchedStatus));
			}

			MapStream inputs = new MapStream("updatePhoneScreenStatusByInterviewScreenStatCode");

			try
			{
				inputs.put("interviewStatusCode", interviewSchedStatus);
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				query.updateObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
			//	throw e;
			}

			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - updatePhoneScreenStatusByInterviewScreenStatCode()");
			}
		}

		
		public static void updateStatusHistoryTable(DAOConnection daoConn, Query query, PhoneScreenHandler handler, int phoneScrnId,
			    short statusTypeCode, short statusCode) throws QueryException
			{

				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("Enter PhoneScreenDAO - updateStatusHistoryTable(),"
					    + "phoneScrnId: %1$s, statusTypeCode: %2$s, statusCode:%3$s", phoneScrnId, statusTypeCode, statusCode));
				}

				try
				{
					//Update the Status History table
					//Get the Max Sequence Number
					MapStream inputs = new MapStream(SEL_READ_MAX_CAND_CONTACT_STATUS);
					inputs.put(HR_PHN_SCRN_ID, phoneScrnId);
					query.getResult(daoConn, handler, inputs);
					logger.debug(String.format("Next Seq Num:%1$d for Phone Screen:%2$d", handler.getMaxSeqNumPlusOne(), phoneScrnId));
					
					//Insert Passed in Status record
					inputs.clear();
					inputs.setSelectorName(SEL_CREATE_CAND_CONTACT_STATUS);
					inputs.put(HR_PHN_SCRN_ID, phoneScrnId);
					inputs.put(CAND_CONTACT_STATUS_TYPE_CODE, statusTypeCode);
					inputs.put(SEQUENCE_NBR, handler.getMaxSeqNumPlusOne());
					if (statusTypeCode == 1) {
						inputs.put(PHN_SCRN_STAT_CD, statusCode);
						inputs.put(INTERVIEW_STAT_CD, INTERVIEW_STAT_ZERO);
					} else if (statusTypeCode == 2) {
						inputs.put(PHN_SCRN_STAT_CD, PHN_SCRN_STAT_ZERO);
						inputs.put(INTERVIEW_STAT_CD, statusCode);
					}
					
					query.insertObject(daoConn, handler, inputs);
				}
				catch (QueryException e)
				{
				//	throw e;
				}

				if(logger.isDebugEnabled())
				{
					logger.debug("Exit phoneScreenDAO - updateStatusHistoryTable()");
				}
			}		
		
		
		public static void updatePhoneScreenStatusByInterviewMaterialStatus(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId,
		    short materialStatus) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format(
				    "Enter PhoneScreenDAO - updatePhoneScreenStatusByInterviewMaterialStatus()," + "phoneScrnId: %1$s, materialStatus: %2$s", phoneScrnId,
				    materialStatus));
			}

			MapStream inputs = new MapStream("updatePhoneScreenStatusByInterviewMaterialStatCode");

			try
			{
				inputs.put("interviewMaterialStatusCode", materialStatus);
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				query.updateObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
			//	throw e;
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - updatePhoneScreenStatusByInterviewMaterialStatus()");
			}
		}

		public static void updatePhoneScreenDispositionCode(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId,
			    short phnScrnDispositionCode) throws QueryException
			{

				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("Enter PhoneScreenDAO - updatePhoneScreenDispositionCode(),"
					    + "phoneScrnId: %1$s, phoneScreenDispositionCode: %2$s", phoneScrnId, phnScrnDispositionCode));
				}

				MapStream inputs = new MapStream("updateHumanResourcesPhoneScreen");

				try
				{
					inputs.put("phoneScreenDispositionCode", phnScrnDispositionCode);
					inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
					inputs.put("phoneScreenDispositionTimestamp", Util.getCurrentTimestamp());
					query.updateObject(daoConn, handler, inputs);
				}
				catch (QueryException e)
				{
					throw e;
				}

				if(logger.isDebugEnabled())
				{
					logger.debug("Exit phoneScreenDAO - updatePhoneScreenDispositionCode()");
				}
			}
		
		public static void readPhoneScreenMinimumRequirementForExistence(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId,
		    short sequenceNumber) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - readPhoneScreenMinimumRequirementForExistence()," + "phoneScrnId: %1$s, sequenceNumber: %2$s",
				    phoneScrnId, sequenceNumber));
			}

			MapStream inputs = new MapStream("readPhoneScreenMinimumRequirementForExistence");

			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				inputs.put("sequenceNumber", sequenceNumber);
				query.getObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - readPhoneScreenMinimumRequirementForExistence()");
			}
		}

		public static void updatePhoneScreenMinimumRequirement(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, short sequenceNumber,
		    String minReqFlag) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - updatePhoneScreenMinimumRequirement(),"
				    + "phoneScrnId: %1$s, sequenceNumber: %2$s, minReqFlag: %3$s", phoneScrnId, sequenceNumber, minReqFlag));
			}

			MapStream inputs = new MapStream("updatePhoneScreenMinimumRequirement");

			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				inputs.put("sequenceNumber", sequenceNumber);
				inputs.put("minimumRequirementFlag", minReqFlag);
				query.updateObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - updatePhoneScreenMinimumRequirement()");
			}

		}

		public static void createPhoneScreenMinimumRequirement(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, short sequenceNumber,
		    String minReqFlag, short questionTypeCode) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - createPhoneScreenMinimumRequirement(),"
				    + "phoneScrnId: %1$s, sequenceNumber: %2$s, minReqFlag: %3$s", phoneScrnId, sequenceNumber, minReqFlag));
			}

			MapStream inputs = new MapStream("createPhoneScreenMinimumRequirementBatch");

			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				inputs.put("sequenceNumber", sequenceNumber);
				inputs.put("minimumRequirementFlag", minReqFlag);
				inputs.put("phoneScreenQuestionTypeCode", questionTypeCode);
				query.insertObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - createPhoneScreenMinimumRequirement()");
			}
		}

		public static void createPhoneScreenMinimumRequirementBatch(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, List<Object> inputCreatePhoneScreenMinimumRequirementList) throws QueryException
			{

				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("Enter PhoneScreenDAO - createPhoneScreenMinimumRequirement(),"
					    + "phoneScrnId: %1$s", phoneScrnId));
				}

				MapStream inputs = new MapStream("createPhoneScreenMinimumRequirementBatch");

				try
				{
					inputs.put("createPhoneScreenMinimumRequirementList", inputCreatePhoneScreenMinimumRequirementList);
					query.insertObject(daoConn, handler, inputs);
				}
				catch (QueryException e)
				{
					e.printStackTrace();
					throw e;
				}
				if(logger.isDebugEnabled())
				{
					logger.debug("Exit phoneScreenDAO - createPhoneScreenMinimumRequirementBatch()");
				}
			}
		
		public static void updateHumanResourcesPhoneScreenNote(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, short noteTypeCode,
		    String noteText) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - updateHumanResourcesPhoneScreenNote(),"
				    + "phoneScrnId: %1$s, noteTypeCode: %2$s, noteText: %3$s", phoneScrnId, noteTypeCode, noteText));
			}

			MapStream inputs = new MapStream("updateHumanResourcesPhoneScreenNote");

			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				inputs.put("humanResourcesNoteTypeCode", noteTypeCode);
				inputs.putAllowNull("phoneScreenNoteText", noteText);
				query.updateObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - updateHumanResourcesPhoneScreenNote()");
			}
		}

		public static void createHumanResourcesPhoneScreenNote(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId, short noteTypeCode,
		    String noteText) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - createHumanResourcesPhoneScreenNote(),"
				    + "phoneScrnId: %1$s, noteTypeCode: %2$s, noteText: %3$s", phoneScrnId, noteTypeCode, noteText));
			}

			MapStream inputs = new MapStream("createHumanResourcesPhoneScreenNote");

			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				inputs.put("humanResourcesNoteTypeCode", noteTypeCode);
				inputs.putAllowNull("phoneScreenNoteText", noteText); 
				query.insertObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - createHumanResourcesPhoneScreenNote()");
			}
		}

		public static void readHumanResourcesPhoneScreenNoteForExistence(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScrnId,
		    short noteTypeCode) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - readHumanResourcesPhoneScreenNoteForExistence()," + "phoneScrnId: %1$s, noteTypeCode: %2$s",
				    phoneScrnId, noteTypeCode));
			}

			MapStream inputs = new MapStream("readHumanResourcesPhoneScreenNoteForExistence");

			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				inputs.put("humanResourcesNoteTypeCode", noteTypeCode);
				query.getObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - createHumanResourcesPhoneScreenNote()");
			}
		}

		public static void updateHumanResourcesPhoneScreenNonStatusCodes(DAOConnection daoConn, Query query, QueryHandler handler,
		    PhoneScreenIntrwDetailsTO phoneScrnDetails) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug("Enter PhoneScreenDAO - updateHumanResourcesPhoneScreenNonStatusCodes()");
				logger.debug("humanResourcesPhoneScreenId : " + phoneScrnDetails.getItiNbr());
				logger.debug("humanResourcesSystemStoreNumber : " + phoneScrnDetails.getCndStrNbr());
				logger.debug("employmentPositionCandidateId : " + phoneScrnDetails.getCndtNbr());
				logger.debug("employmentRequisitionNumber : " + phoneScrnDetails.getReqNbr());
				logger.debug("candidatePersonId : " + phoneScrnDetails.getAid());
				logger.debug("candidateName : " + phoneScrnDetails.getName());
				logger.debug("candidatePhoneNumber : " + phoneScrnDetails.getName());
				logger.debug("phoneScreenName : " + phoneScrnDetails.getPhnScreener());
				if(phoneScrnDetails.getIntrLocDtls() != null)
				{
					if(phoneScrnDetails.getIntrLocDtls().getInterviewLocId() != null)
						logger.debug("interviewLocationTypeCode : " + phoneScrnDetails.getIntrLocDtls().getInterviewLocId().trim());
					if(phoneScrnDetails.getIntrLocDtls().getInterviewer() != null)
						logger.debug("interviewerName : " + phoneScrnDetails.getIntrLocDtls().getInterviewer());
					if(phoneScrnDetails.getIntrLocDtls().getInterviewLocName() != null)
						logger.debug("interviewLocationDescription : " + phoneScrnDetails.getIntrLocDtls().getInterviewLocName());
					if(phoneScrnDetails.getIntrLocDtls().getPhone() != null)
						logger.debug("interviewPhoneNumber : " + phoneScrnDetails.getIntrLocDtls().getPhone());
					if(phoneScrnDetails.getIntrLocDtls().getAdd() != null)
						logger.debug("interviewAddressText : " + phoneScrnDetails.getIntrLocDtls().getAdd());
					if(phoneScrnDetails.getIntrLocDtls().getCity() != null)
						logger.debug("interviewCityName : " + phoneScrnDetails.getIntrLocDtls().getCity());
					if(phoneScrnDetails.getIntrLocDtls().getState() != null)
						logger.debug("interviewStateCode : " + phoneScrnDetails.getIntrLocDtls().getState());
					if(phoneScrnDetails.getIntrLocDtls().getZip() != null)
						logger.debug("interviewZipCodeCode : " + phoneScrnDetails.getIntrLocDtls().getZip());
				}
				logger.debug("electronicMailAddressText : " + phoneScrnDetails.getEmailAdd());
				logger.debug("activeFlag : " + phoneScrnDetails.getCanStatus());
				logger.debug("requisitionCalendarId : " + phoneScrnDetails.getReqCalId());
			}

			MapStream inputs = new MapStream("updateHumanResourcesPhoneScreenNonStatusCodes");
			try
			{
				inputs.put("humanResourcesPhoneScreenId", Integer.parseInt(phoneScrnDetails.getItiNbr()));
				inputs.put("humanResourcesSystemStoreNumber", phoneScrnDetails.getCndStrNbr());
				inputs.put("employmentPositionCandidateId", phoneScrnDetails.getCndtNbr());
				inputs.put("employmentRequisitionNumber", Integer.parseInt(phoneScrnDetails.getReqNbr()));

				if(phoneScrnDetails.getAid() != null && !phoneScrnDetails.getAid().equals("") && !phoneScrnDetails.getAid().equals("null"))
				{
					inputs.putAllowNull("candidatePersonId", phoneScrnDetails.getAid());
				}
				else
				{
					inputs.putAllowNull("candidatePersonId", null);
				}

				inputs.putAllowNull("candidateName", phoneScrnDetails.getName());
				inputs.putAllowNull("candidatePhoneNumber", phoneScrnDetails.getCanPhn());
				
				if (phoneScrnDetails.getPhnScrnDate() != null && !phoneScrnDetails.getPhnScrnDate().getYear().equals("0") 
						    && !phoneScrnDetails.getPhnScrnDate().getMonth().equals("0") && !phoneScrnDetails.getPhnScrnDate().getDay().equals("0")) {
					inputs.putAllowNull("phoneInterviewDate", Util.convertDateTO(phoneScrnDetails.getPhnScrnDate()));
				} else {
					inputs.putAllowNull("phoneInterviewDate", null);
				}
				
				if (phoneScrnDetails.getPhnScrnTime() != null && !phoneScrnDetails.getPhnScrnTime().getYear().equals("0") 
					    && !phoneScrnDetails.getPhnScrnTime().getMonth().equals("0") && !phoneScrnDetails.getPhnScrnTime().getDay().equals("0")) {
					inputs.putAllowNull("phoneInterviewTimestamp", Util.convertTimestampTO(phoneScrnDetails.getPhnScrnTime()));
				} else {
					inputs.putAllowNull("phoneInterviewTimestamp", null);
				}
				
				inputs.putAllowNull("phoneScreenName", phoneScrnDetails.getPhnScreener());
				inputs.putAllowNull("phoneScreenRespondText", null);

				if(phoneScrnDetails.getYnstatus() != null && !StringUtils.trim(phoneScrnDetails.getYnstatus()).equalsIgnoreCase("null")
				    && !phoneScrnDetails.getYnstatus().trim().equals(EMPTY_STRING))
				{
					inputs.putAllowNull("minimumRequirementStatusCode", Short.parseShort(phoneScrnDetails.getYnstatus().trim()));
				}
				else
				{
					inputs.putAllowNull("minimumRequirementStatusCode", null);
				}
				if(phoneScrnDetails.getOverAllStatus() != null && !phoneScrnDetails.getOverAllStatus().trim().equals(EMPTY_STRING))
				{
					inputs.putAllowNull("overallRespondStatusCode", Short.parseShort(phoneScrnDetails.getOverAllStatus().trim()));
				}
				else
				{
					inputs.putAllowNull("overallRespondStatusCode", null);
				}

				if(phoneScrnDetails.getIntrLocDtls() != null)
				{
					if(phoneScrnDetails.getIntrLocDtls().getInterviewLocId() != null
					    && !phoneScrnDetails.getIntrLocDtls().getInterviewLocId().trim().equals(EMPTY_STRING))
					{
						inputs.putAllowNull("interviewLocationTypeCode", Short.parseShort(phoneScrnDetails.getIntrLocDtls().getInterviewLocId().trim()));
					}
					else
					{
						inputs.putAllowNull("interviewLocationTypeCode", null);
					}

					inputs.putAllowNull("interviewerName", phoneScrnDetails.getIntrLocDtls().getInterviewer());
					// check for null condition
					if(phoneScrnDetails.getIntrLocDtls().getInterviewDate() != null)
					{
						inputs.putAllowNull("interviewDate", Util.convertDateTO(phoneScrnDetails.getIntrLocDtls().getInterviewDate()));
					}
					else
					{
						inputs.putAllowNull("interviewDate", null);
					}
					if(phoneScrnDetails.getIntrLocDtls().getInterviewTime() != null)
					{
						inputs.putAllowNull("interviewTimestamp", Util.convertTimestampTO(phoneScrnDetails.getIntrLocDtls().getInterviewTime()));
					}
					else
					{
						inputs.putAllowNull("interviewTimestamp", null);
					}
					inputs.putAllowNull("interviewPhoneNumber", phoneScrnDetails.getIntrLocDtls().getPhone());
					inputs.putAllowNull("interviewAddressText", phoneScrnDetails.getIntrLocDtls().getAdd());
					inputs.putAllowNull("interviewCityName", phoneScrnDetails.getIntrLocDtls().getCity());
					inputs.putAllowNull("interviewStateCode", phoneScrnDetails.getIntrLocDtls().getState());
					inputs.putAllowNull("interviewZipCodeCode", phoneScrnDetails.getIntrLocDtls().getZip());
					inputs.putAllowNull("interviewLocationDescription", phoneScrnDetails.getIntrLocDtls().getInterviewLocName());

				}
				else
				{
					inputs.putAllowNull("interviewLocationTypeCode", null);
					inputs.putAllowNull("interviewerName", null);
					inputs.putAllowNull("interviewDate", null);
					inputs.putAllowNull("interviewTimestamp", null);
					inputs.putAllowNull("interviewLocationDescription", null);
					inputs.putAllowNull("interviewPhoneNumber", null);
					inputs.putAllowNull("interviewAddressText", null);
					inputs.putAllowNull("interviewCityName", null);
					inputs.putAllowNull("interviewStateCode", null);
					inputs.putAllowNull("interviewZipCodeCode", null);
				}
				inputs.putAllowNull("activeFlag", Util.convertToBoolean(phoneScrnDetails.getCanStatus()));
				inputs.putAllowNull("firstAtmptFailTimestamp", null);
				inputs.putAllowNull("scheduleInterviewFailTimestamp", null);
				inputs.putAllowNull("electronicMailAddressText", phoneScrnDetails.getEmailAdd());
				inputs.putAllowNull("requisitionCalendarId", Integer.parseInt(phoneScrnDetails.getReqCalId()));

				query.updateObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}

			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - updateHumanResourcesPhoneScreenNonStatusCodes()");
			}
		}

		public static void createHumanResourcesPhoneScreen(DAOConnection daoConn, Query query, QueryHandler handler, PhoneScreenIntrwDetailsTO phoneScrnDetails)
		    throws QueryException
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("Enter PhoneScreenDAO - createHumanResourcesPhoneScreen()");
			}

			MapStream inputs = new MapStream("createHumanResourcesPhoneScreen");

			try
			{
				inputs.put("humanResourcesSystemStoreNumber", phoneScrnDetails.getCndStrNbr());
				inputs.put("employmentPositionCandidateId", phoneScrnDetails.getCndtNbr());
				inputs.put("employmentRequisitionNumber", Integer.parseInt(phoneScrnDetails.getReqNbr()));
				inputs.putAllowNull("candidatePersonId", phoneScrnDetails.getAid());
				inputs.putAllowNull("candidateName", phoneScrnDetails.getName());
				inputs.putAllowNull("candidatePhoneNumber", phoneScrnDetails.getCanPhn());
				inputs.putAllowNull("phoneInterviewDate", Util.convertDateTO(phoneScrnDetails.getPhnScrnDate()));
				inputs.putAllowNull("phoneInterviewTimestamp", Util.convertTimestampTO(phoneScrnDetails.getPhnScrnTime()));
				inputs.putAllowNull("phoneScreenName", phoneScrnDetails.getPhnScreener());
				inputs.putAllowNull("phoneScreenRespondText", null);
				inputs.putAllowNull("phoneScreenDispositionCode", phoneScrnDetails.getPhoneScreenDispositionCode());
				
				if (phoneScrnDetails.getPhoneScreenDispositionCodeTimestamp() != null) {
					inputs.putAllowNull("phoneScreenDispositionTimestamp", Util.convertTimestampTO(phoneScrnDetails.getPhoneScreenDispositionCodeTimestamp()));
				} else {
					inputs.putAllowNull("phoneScreenDispositionTimestamp", Util.getCurrentTimestamp());
				}

				if(phoneScrnDetails.getYnstatus() != null && !phoneScrnDetails.getYnstatus().trim().equals(EMPTY_STRING))
				{
					inputs.putAllowNull("minimumRequirementStatusCode", Short.parseShort(phoneScrnDetails.getYnstatus().trim()));
				}
				else
				{
					inputs.putAllowNull("minimumRequirementStatusCode", null);
				}

				inputs.putAllowNull("overallRespondStatusCode", null);

				if(phoneScrnDetails.getIntrLocDtls() != null)
				{
					if(phoneScrnDetails.getIntrLocDtls().getInterviewLocId() != null
					    && !phoneScrnDetails.getIntrLocDtls().getInterviewLocId().trim().equals(EMPTY_STRING))
					{
						inputs.putAllowNull("interviewLocationTypeCode", Short.parseShort(phoneScrnDetails.getIntrLocDtls().getInterviewLocId().trim()));
					}
					else
					{
						inputs.putAllowNull("interviewLocationTypeCode", null);
					}

					inputs.putAllowNull("interviewerName", phoneScrnDetails.getIntrLocDtls().getInterviewer());
					inputs.putAllowNull("interviewDate", Util.convertDateTO(phoneScrnDetails.getIntrLocDtls().getInterviewDate()));
					inputs.putAllowNull("interviewTimestamp", Util.convertTimestampTO(phoneScrnDetails.getIntrLocDtls().getInterviewTime()));
					inputs.putAllowNull("interviewLocationDescription", phoneScrnDetails.getIntrLocDtls().getInterviewLocName());
					inputs.putAllowNull("interviewPhoneNumber", phoneScrnDetails.getIntrLocDtls().getPhone());
					inputs.putAllowNull("interviewAddressText", phoneScrnDetails.getIntrLocDtls().getAdd());
					inputs.putAllowNull("interviewCityName", phoneScrnDetails.getIntrLocDtls().getCity());
					inputs.putAllowNull("interviewStateCode", phoneScrnDetails.getIntrLocDtls().getState());
					inputs.putAllowNull("interviewZipCodeCode", phoneScrnDetails.getIntrLocDtls().getZip());
				}
				else
				{
					inputs.putAllowNull("interviewLocationTypeCode", null);
					if(phoneScrnDetails.getNamesOfInterviewers() != null && !phoneScrnDetails.getNamesOfInterviewers().equals(EMPTY_STRING))
					{
						inputs.putAllowNull("interviewerName", phoneScrnDetails.getNamesOfInterviewers());
					}
					else
					{
						inputs.putAllowNull("interviewerName", null);
					}
					inputs.putAllowNull("interviewDate", null);
					inputs.putAllowNull("interviewTimestamp", null);
					inputs.putAllowNull("interviewLocationDescription", null);
					inputs.putAllowNull("interviewPhoneNumber", null);
					inputs.putAllowNull("interviewAddressText", null);
					inputs.putAllowNull("interviewCityName", null);
					inputs.putAllowNull("interviewStateCode", null);
					inputs.putAllowNull("interviewZipCodeCode", null);
				}

				inputs.putAllowNull("activeFlag", Util.convertToBoolean("Y"));

				inputs.putAllowNull("phoneScreenStatusCode", Short.parseShort(phoneScrnDetails.getOverAllStatus().trim()));
				//Per SIR 3757 Don't carry over timestamp, set to current timestamp 
				inputs.putAllowNull("phoneScreenStatusTimestamp", Util.getCurrentTimestamp());

				inputs.putAllowNull("interviewStatusCode", INTERVIEW_STATUS_CODE_ZERO);
				inputs.putAllowNull("interviewStatusTimestamp", null);
				inputs.putAllowNull("interviewMaterialStatusCode", INTERVIEW_MAT_STATUS_CODE_ZERO);
				inputs.putAllowNull("interviewMaterialStatusTimestamp", null);
				inputs.putAllowNull("firstAtmptFailTimestamp", null);
				inputs.putAllowNull("scheduleInterviewFailTimestamp", null);
				inputs.putAllowNull("requisitionCalendarId", Integer.parseInt(phoneScrnDetails.getReqCalId()));

				// check for internal or external based on the Associate ID
				if(phoneScrnDetails.getAid() != null && !phoneScrnDetails.getAid().trim().equals(EMPTY_STRING))
				{
					// Internal if AID is present, get email from addresses_t
					PhoneScreenIntrwDetailsTO phnScrnIntrwDtlsEmail = null;
					phnScrnIntrwDtlsEmail = readInternalApplicantEmailAddress(phoneScrnDetails.getCndtNbr());
					// To fetch email address
					if(phnScrnIntrwDtlsEmail != null && phnScrnIntrwDtlsEmail.getEmailAdd() != null)
					{
						inputs.putAllowNull("electronicMailAddressText", StringUtils.trim(phnScrnIntrwDtlsEmail.getEmailAdd()));
					}
					else
					{
						inputs.putAllowNull("electronicMailAddressText", null);
					}
				}
				else
				{
					// External if no AID is present, get email from
					// EMPLT_APLCNT_PRIM
					PhoneScreenIntrwDetailsTO phnScrnIntrwDtlsEmail = null;
					phnScrnIntrwDtlsEmail = readApplicantEmailAddress(phoneScrnDetails.getCndtNbr());
					// To fetch email address
					if(phnScrnIntrwDtlsEmail != null && phnScrnIntrwDtlsEmail.getEmailAdd() != null)
					{
						inputs.putAllowNull("electronicMailAddressText", StringUtils.trim(phnScrnIntrwDtlsEmail.getEmailAdd()));
					}
					else
					{
						inputs.putAllowNull("electronicMailAddressText", null);
					}

				}
				inputs.addQualifier("returnGeneratedKey", true);
				//System.out.println("Before Insert.....");
				query.insertObject(daoConn, handler, inputs);
				//System.out.println("After Insert.....");
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}

			if(logger.isDebugEnabled())
			{
				logger.debug("Enter PhoneScreenDAO - createHumanResourcesPhoneScreen()");
			}
		}

		public static void updateHumanResourcesPhoneScreenMinimumRequirementStatusCode(DAOConnection daoConn, Query query, QueryHandler handler, int phoneScreenId,
		    String phoneScreenName, short statusCode) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug("Enter PhoneScreenDAO - updateHumanResourcesPhoneScreenMinimumRequirementStatusCode()");
			}

			MapStream inputs = new MapStream("updateHumanResourcesPhoneScreenMinimumRequirementStatusCode");

			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScreenId);
				inputs.putAllowNull("phoneInterviewDate", new Date(System.currentTimeMillis()));
				inputs.putAllowNull("phoneInterviewTimestamp", new Timestamp(System.currentTimeMillis()));
				inputs.putAllowNull("phoneScreenName", phoneScreenName);
				inputs.putAllowNull("minimumRequirementStatusCode", statusCode);
				query.updateObject(daoConn, handler, inputs);
			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}

			if(logger.isDebugEnabled())
			{
				logger.debug("Exit PhoneScreenDAO - updateHumanResourcesPhoneScreenMinimumRequirementStatusCode()");
			}
		}

		public static void updateHumanResourcesRetailStaffReconciliation(DAOConnection daoConn, Query query, QueryHandler handler, String campaignType, int jobId,
		    String contactID, short completeStatusCode, String interviewRespStatCode, Timestamp complete, Timestamp callBegin, Timestamp callEnd)
		    throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - updateHumanResourcesRetailStaffReconciliation(),"
				    + "campaignType: %1$s, jobId: %2$s, contactId: %3$s,completionCode:%4$s,interviewResponseStatus:%5$s,complete:%6$s,callBegin%7$s,callEnd%8$s,",
				    campaignType, jobId, contactID, completeStatusCode, interviewRespStatCode, complete, callBegin, callEnd));
			}
			MapStream inputs = new MapStream("updateHumanResourcesRetailStaffReconciliation");

			try
			{
				inputs.put("campaignNameIndicator", campaignType);
				inputs.put("jobId", jobId);
				inputs.put("contactId", contactID);			
				inputs.put("pomCompleteStatusCode", completeStatusCode);
				//[[
				// Validate the condition if the interviewRespStatCode is zero.
				if( interviewRespStatCode.trim().length() > 0 )
				{
					inputs.putAllowNull("interviewRespondStatusCode", Short.parseShort(interviewRespStatCode));
				}	
				else
				{
					inputs.putAllowNull("interviewRespondStatusCode", null );
				}
				//inputs.putAllowNull("interviewRespondStatusCode", interviewRespStatCode);
				//]]
				inputs.putAllowNull("completeTimestamp", complete);
				inputs.putAllowNull("callBeginTimestamp", callBegin);
				inputs.putAllowNull("callEndTimestamp", callEnd);
				query.updateObject(daoConn, handler, inputs);
			}
			catch (IllegalArgumentException e)
			{

				e.printStackTrace();
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - updateHumanResourcesRetailStaffReconciliation()");
			}
		}

		/**
		 * The method is used for reading the phone screen created within the last
		 * 60 days for a given candidate that matches the phone screen script
		 * required for a new job requisition. If one exists, we will reuse the
		 * fields retrieved to insert the same phone screen for a new job
		 * opportunity so that they do not have to call and screen the candidate
		 * again.
		 * 
		 * @param employmentPositionCandidateId
		 *            - The applicant Id
		 * @param phoneScreenStatusCodeList
		 *            - A list of status codes to match
		 * @param oe32
		 *            - oe32 value for this phone screen
		 * 
		 * @return list of phone screen interview details
		 * @throws RetailStaffingException
		 */
		public static List<PhoneScreenIntrwDetailsTO> readHumanResourcesPhoneScreenByRequisitionCreateDateInputList(String employmentPositionCandidateId,
		    List<Short> phoneScreenStatusCodeList, String oe32) throws RetailStaffingException
		{

			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_YEAR, -60);
			Date createdDate = new java.sql.Date(c.getTimeInMillis());

			if(logger.isDebugEnabled())
				;
			{
				logger.debug(String.format("Enter PhoneScreenDAO - readHumanResourcesPhoneScreenByRequisitionCreateDateInputList(),"
				    + "employmentPositionCandidateId: %1$s, oe32: %2$s", employmentPositionCandidateId, oe32));
			}

			final List<PhoneScreenIntrwDetailsTO> phoneScreenDetailList = new ArrayList<PhoneScreenIntrwDetailsTO>();
			try
			{
				MapStream inputs = new MapStream("readHumanResourcesPhoneScreenByRequisitionCreateDateInputList");
				inputs.put("oe32", oe32);
				inputs.put("employmentPositionCandidateId", employmentPositionCandidateId);
				inputs.put("tabno", TABNO_DEPT_NO);
				inputs.put("phoneScreenStatusCodeList", phoneScreenStatusCodeList);
				inputs.put("phoneInterviewDate", createdDate);

				BasicDAO.getResult(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						PhoneScreenIntrwDetailsTO phoneScreenDetailListDTO = null;
						while(results.next())
						{
							phoneScreenDetailListDTO = new PhoneScreenIntrwDetailsTO();

							phoneScreenDetailListDTO.setItiNbr(String.valueOf(results.getInt("humanResourcesPhoneScreenId")));
							// phoneInterviewDate
							if(results.getDate("phoneInterviewDate") != null)
							{
								phoneScreenDetailListDTO.setPhnScrnDate(Util.converDatetoDateTO(results.getDate("phoneInterviewDate")));
							}
							else
							{
								DateTO ss = new DateTO();
								ss.setYear("0");
								ss.setMonth("0");
								ss.setDay("0");
								phoneScreenDetailListDTO.setPhnScrnDate(ss);
							}
							// phoneInterviewTimestamp
							if(results.getTimestamp("phoneInterviewTimestamp") != null)
							{
								phoneScreenDetailListDTO.setPhnScrnTime(Util.converTimeStampTimeStampTO(results.getTimestamp("phoneInterviewTimestamp")));
							}
							else
							{
								TimeStampTO timeStampTO = new TimeStampTO();
								timeStampTO.setMonth("0");
								timeStampTO.setDay("0");
								timeStampTO.setYear("0");
								timeStampTO.setHour(null);
								timeStampTO.setMinute(null);
								timeStampTO.setSecond(null);
								phoneScreenDetailListDTO.setPhnScrnTime(timeStampTO);
							}
							// phoneScreenStatusCode
							phoneScreenDetailListDTO.setPhoneScreenStatusCode(String.valueOf(results.getShort("phoneScreenStatusCode")));
							// phoneScreenStatusTimestamp
							if(results.getTimestamp("phoneScreenStatusTimestamp") != null)
							{
								phoneScreenDetailListDTO.setPhoneScreenStatusTimestamp(Util.converTimeStampTimeStampTO(results
								    .getTimestamp("phoneScreenStatusTimestamp")));
								// Reset the milliseconds because it is causing a
								// conversion error.
								phoneScreenDetailListDTO.getPhoneScreenStatusTimestamp().setMilliSecond("000");
							}
							// phoneScreenName
							phoneScreenDetailListDTO.setPhnScreener(StringUtils.trim(results.getString("phoneScreenName")));
							// minimumRequirementStatusCode
							phoneScreenDetailListDTO.setYnstatus(String.valueOf(results.getShort("minimumRequirementStatusCode")));

							logger.info("phone screener" + phoneScreenDetailListDTO.getPhnScreener());

							//Phone Screen Disposition Code
							phoneScreenDetailListDTO.setPhoneScreenDispositionCode(results.getShort("phoneScreenDispositionCode"));
							
							//Phone Screen Disposition Code timestamp
							if(results.getTimestamp("phoneScreenDispositionTimestamp") != null)
							{
								phoneScreenDetailListDTO.setPhoneScreenDispositionCodeTimestamp(Util.converTimeStampTimeStampTO(results
								    .getTimestamp("phoneScreenDispositionTimestamp")));
								phoneScreenDetailListDTO.getPhoneScreenDispositionCodeTimestamp().setMilliSecond("000");
							}
							
							phoneScreenDetailList.add(phoneScreenDetailListDTO);

						}
					}
				});
			}
			catch (Exception e)
			{
				throw new RetailStaffingException(FETCHING_PHONE_DETAILS_ERROR_CODE, FETCHING_PHONE_DETAILS_ERROR_MSG + " candidate ID: "
				    + employmentPositionCandidateId, e);
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit PhoneScreenDAO - readHumanResourcesPhoneScreenByRequisitionCreateDateInputList() ");
			}
			return phoneScreenDetailList;
		}

		/**
		 * This method will get the phone screen type (BASIC or DETAILED) for the job title and
		 * department data provided
		 * 
		 * @param jobTitleCd the job title
		 * @param deptNbr the department
		 * 
		 * @return the type (BASIC or DETAILED) or null if a type was not found for the job title and
		 * 		   department data provided
		 * 
		 * @throws QueryException thrown if the job title code provided is null or empty, if the department
		 * 						  number provided is null or empty, or if an exception occurred querying the
		 * 						  database
		 */
		public static String getPhoneScreenType(String jobTitleCd, String deptNbr) throws QueryException
		{
			String type = null;			
			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Entering getPhoneScreenType(), jobTitleCd: %1$s, deptNbr: %2$s", jobTitleCd, deptNbr));
			} // end if
			
			try
			{
				// validate the job title code provided is not null or empty
				ValidationUtils.validateNotNullOrEmpty(InputField.JOB_TITLE_CD, jobTitleCd);
				// validate the department number provided is not null or empty
				ValidationUtils.validateNotNullOrEmpty(InputField.DEPARTMENT_NUMBER, deptNbr);
				
				/*
				 * concatenate the job title and department together. this is the key in the database
				 * to determine the "type" of phone screen. if the job title is > 6 characters in length
				 * we only want to use the first 6. if the job title is < 6 characters in length then we
				 * will right pad with spaces until it has a length of 6
				 */
				String key = String.format("%1$-6s%2$s", (jobTitleCd.length() > 6 ? jobTitleCd.substring(0, 6) : jobTitleCd), deptNbr);
				
				// create a wasted collection so we can get the value from the anonymous inner class we're about to create
				final List<String> types = new ArrayList<String>();
				
				// prepare the query 
				MapStream inputs = new MapStream("readTRPRX000ByInputList");
				inputs.put("tabno", TABNO_DEPT_NO);
				inputs.put("tableKeys", key);
				inputs.put("startEndDate", new java.sql.Date(System.currentTimeMillis()));
				
				if(logger.isDebugEnabled())
				{
					logger.debug(String.format("Executing query to get phone screen type using key %1$s", key));
				} // end if
							
				// execute the query
				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					/*
					 * (non-Javadoc)
					 * @see com.homedepot.ta.aa.dao.ResultsReader#readResults(com.homedepot.ta.aa.dao.Results, com.homedepot.ta.aa.dao.Query, com.homedepot.ta.aa.dao.Inputs)
					 */
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						// should only be one record returned
						if(results.next())
						{
							// add the job type to the return collection
							types.add(StringUtils.trim(results.getString("oe32")));
						} // end if(results.next())
					} // end function readResults()
				}); // end getResult() 
				
				// if a job type was found, set the return value 
				if(types.size() > 0)
				{
					type = types.get(0);
					
					if(logger.isDebugEnabled())
					{
						logger.debug(String.format("Found phone screen type %1$s using key %2$s", type, key));
					} // end if
				} // end if(types.size() > 0)
			} // end try
			catch(QueryException qe)
			{
				// log the error
				logger.error(String.format("An exception occurred getting phone screen type for job title %1$s and department %2$s", jobTitleCd, deptNbr));
				// throw the error
				throw qe;
			} // end catch
			
			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Exiting getPhoneScreenType(), jobTitleCd: %1$s, deptNbr: %2$s", jobTitleCd, deptNbr));
			} // end if

			return type;
		} // end function getPhoneScreenType()
		
		public static List<MinimumResponseTO> readPhoneScreenMinimumRequirementList(int phoneScrnId) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - readPhoneScreenMinimumRequirementList()," + "phoneScrnId: %1$s ", phoneScrnId));
			}

			MapStream inputs = new MapStream("readPhoneScreenMinimumRequirement");
			final List<MinimumResponseTO> phoneScreenMinReqList = new ArrayList<MinimumResponseTO>();
			try
			{
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);

				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						while(results.next())
						{
							MinimumResponseTO minReqTO = new MinimumResponseTO();
							String minReq = "A";
							if(results.getString("minimumRequirementFlag") != null)
							{
								minReq = results.getString("minimumRequirementFlag");
							}
							if(minReq.equalsIgnoreCase("TRUE") || minReq.equalsIgnoreCase("Y"))
							{
								minReqTO.setMinimumStatus("Y");
							}
							else if(minReq.equalsIgnoreCase("FALSE") || minReq.equalsIgnoreCase("N"))
							{
								minReqTO.setMinimumStatus("N");
							}
							else
							{
								minReqTO.setMinimumStatus(minReq);
							}
							minReqTO.setLastUpdateSystemUserId(results.getString("lastUpdateSystemUserId"));
							minReqTO.setSeqNbr(String.valueOf(results.getShort("sequenceNumber")));
							phoneScreenMinReqList.add(minReqTO);
						}

					}
				});

			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}

			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - readPhoneScreenMinimumRequirementList()");
			}

			return phoneScreenMinReqList;
		}

		public static PhoneScreenNoteTO readHumanResourcesPhoneScreenNote(int phoneScrnId, short noteTypeCode) throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Enter PhoneScreenDAO - readHumanResourcesPhoneScreenNote()," + "phoneScrnId: %1$s, noteTypeCode: %2$s", phoneScrnId,
				    noteTypeCode));
			}
			final PhoneScreenNoteTO phoneScreenNotesTO = new PhoneScreenNoteTO();
			try
			{
				MapStream inputs = new MapStream("readHumanResourcesPhoneScreenNote");
				inputs.put("humanResourcesPhoneScreenId", phoneScrnId);
				inputs.put("humanResourcesNoteTypeCode", noteTypeCode);
				BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, new ResultsReader()
				{
					public void readResults(Results results, Query query, Inputs inputs) throws QueryException
					{
						while(results.next())
						{
							phoneScreenNotesTO.setCreateSystemUserId(results.getString("createSystemUserId"));
							phoneScreenNotesTO.setCreateTimestamp(results.getTimestamp("createTimestamp"));
							phoneScreenNotesTO.setLastUpdateSystemUserId(results.getString("lastUpdateSystemUserId"));
							phoneScreenNotesTO.setLastUpdateTimestamp(results.getDate("lastUpdateTimestamp"));
							phoneScreenNotesTO.setPhoneScreenNoteText(results.getString("phoneScreenNoteText"));
						}
					}
				});

			}
			catch (QueryException e)
			{
				e.printStackTrace();
				throw e;
			}
			if(logger.isDebugEnabled())
			{
				logger.debug("Exit phoneScreenDAO - createHumanResourcesPhoneScreenNote()");
			}
			return phoneScreenNotesTO;
		}

		public static POMRsaStatusCrossRefResponse readNlsPomCompleteStatusByInputList(String compaignType, String statusDesc, String langCode)
		    throws QueryException
		{

			if(logger.isDebugEnabled())
			{
				logger.debug(String.format(
				    "Enter PhoneScreenDAO - readNlsPomCompleteStatusByInputList()," + "compaignType: %1$s, statusDesc: %2$s, langCode: %3$s", compaignType,
				    statusDesc, langCode));
			}

			MapStream inputs = new MapStream("readNlsPomCompleteStatusByInputList");

			inputs.put("campaignTypeIndicator", compaignType);
			inputs.put("shortStatusDescription", statusDesc);
			inputs.put("languageCode", langCode);

			PomRsaCrossRefHandler handler = new PomRsaCrossRefHandler();
			BasicDAO.getResult(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BU_ID, HRSTAFFING_VERSION, inputs, handler);

			if(logger.isDebugEnabled())
			{
				logger.debug("Enter phoneScreenDAO - readNlsPomCompleteStatusByInputList()");
			}
			return handler.getPOMCompleteStatus();
		}
		
		public static MapStream updateHrPhoneScreenCalendarIdsByReqNumber(MapStream inputs, final UpdateStaffingRequest staffingUpdate) throws QueryException {
			
			if(logger.isDebugEnabled())
			{
				logger.debug("Enter PhoneScreenDAO - updateHrPhoneScreenCalendarIdsByReqNumber()");
			}

				inputs.put("requisitionCalendarId", staffingUpdate.getReqCalId());
				inputs.put("employmentRequisitionNumber", Integer.parseInt(staffingUpdate.getReqNbr()));				
			
			if(logger.isDebugEnabled())
			{
				logger.debug("Exiting PhoneScreenDAO - updateHrPhoneScreenCalendarIdsByReqNumber()");
			}	
			return inputs;
		}	
		
		public static String getPhoneScreenBannerNum(final String deptNbr, final String jobTitleCd) throws QueryException
		{	
			final Date currentDate = new Date(System.currentTimeMillis());
			final int MAX_CHARS = 6;
			String bannerNum;
			
			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("Entering getPhoneScreenBannerNum(), Dept: %1$s, Job Title: %2$s", deptNbr, jobTitleCd));
			} // end if
			
			try
			{
				/*
				 * concatenate the job title and department together. this is the key in the database
				 * to determine the "type" of phone screen. if the job title is > 6 characters in length
				 * we only want to use the first 6. if the job title is < 6 characters in length then we
				 * will right pad with spaces until it has a length of 6
				 */
				String key = String.format("%1$-6s%2$s", (jobTitleCd.length() > MAX_CHARS ? jobTitleCd.substring(0, MAX_CHARS) : jobTitleCd), deptNbr);
				
				bannerNum = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
							   	.setSQL(SQL_READ_PHN_SCRN_BANNER_NUM)
							   	.input(1, TABNO_DEPT_NO)
							   	.input(2, key)
							    .input(3, currentDate)
								.input(4, currentDate)
								.debug(logger)
								.get(String.class);
				} // end try
			catch(QueryException qe)
			{
				// log the exception
				logger.error(String.format("An exception occurred getting phone screen banner number"), qe);
				// throw the exception
				throw qe;
			} // end catch
						
			return bannerNum;
		} // end function getPhoneScreenBannerNum		
		
}

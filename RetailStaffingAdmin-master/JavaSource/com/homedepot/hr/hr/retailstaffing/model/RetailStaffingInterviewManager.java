package com.homedepot.hr.hr.retailstaffing.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dao.PhoneScreenDAO;
import com.homedepot.hr.hr.retailstaffing.dao.RetailStaffingRequisitionDAO;
import com.homedepot.hr.hr.retailstaffing.dao.ScheduleDAO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReadHumanResourcesStoreEmploymentRequisitionDTO;
import com.homedepot.hr.hr.retailstaffing.dto.ReqScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.CalendarResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CandidateDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.CompleteITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDetailResponse;
import com.homedepot.hr.hr.retailstaffing.enumerations.InputField;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.util.RetailStaffingConstants;
import com.homedepot.hr.hr.retailstaffing.util.Util;
import com.homedepot.hr.hr.retailstaffing.utils.StringUtils;
import com.homedepot.hr.hr.retailstaffing.utils.ValidationUtils;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This Class is used to have the business logic for Search based on Iti Number.
 * Methods: getRequisitionDetails, getRCandidateDetails, getInterviewDetails
 * 
 * @author dxg8002
 * 
 */
public class RetailStaffingInterviewManager implements RetailStaffingConstants
{

	private static final Logger logger = Logger.getLogger(RetailStaffingInterviewManager.class);
	public int eventId = 0;

	/**
	 * This method is used to get the requisition details based on the
	 * requisition number.
	 * 
	 * @param itiNbr
	 * @return
	 * @throws RetailStaffingException
	 */
	public Response getPhoneScreenInterviewDetails(String itiNbr) throws RetailStaffingException
	{
		logger.info(this + "Enters getRequisitionDetails method in Manager with input" + itiNbr);
		Response res = null;

		StoreDetailResponse StrRes = new StoreDetailResponse();

		List<RequisitionDetailTO> reqList = null;
		List<CandidateDetailsTO> candDetTo = null;
		CandidateDetailResponse canDetRes = new CandidateDetailResponse();
		RequisitionDetailTO reqDtl = null;
		PhoneScreenDAO phnScrnDAO = new PhoneScreenDAO();
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		List<StaffingDetailsTO> stfList = null;
		StaffingDetailsTO stfDtls = null;
		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();

		try
		{
			res = new Response();
			if(itiNbr != null && !itiNbr.trim().equals(""))
			{
				// get Store Number from DAO
				reqList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(itiNbr));
				stfList = phnScrnDAO.readRequisitionStaffingDetails(Integer.parseInt(itiNbr));
				if(stfList != null && !stfList.isEmpty() && stfList.size() > 0)
				{
					stfDtls = stfList.get(0);
					res.setStfDtls(stfDtls);
				}

				if(reqList != null && !reqList.isEmpty() && reqList.size() > 0)
				{
					reqDtl = reqList.get(0);
					// get the store data
					StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(reqDtl.getStore());
					StrRes.addStore(store);
					res.setStrDtRes(StrRes);
					// to fetch basic requisition details
					reqRes.setReqDtlList(reqList);
					res.setReqDtlList(reqRes);

					// to fetch candidate details for requisition screen
					candDetTo = delReqMgr.readRequisitionCandidate(Integer.parseInt(itiNbr), reqDtl.getStore());
					if(candDetTo != null && candDetTo.size() > 0)
					{
						canDetRes.setCndDtlList(candDetTo);
						res.setCanDtRes(canDetRes);
					}
				}

			}
			else
			{
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}

		}
		catch (RetailStaffingException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getRequisitionDetails method in Manager with output" + res);
		return res;
	}

	public Response getInterviewDetails(String itiNbr) throws RetailStaffingException
	{
		logger.info(this + "Enters getPhoneScreenInterviewDetails method in Manager with input" + itiNbr);
		Response res = null;

		RequisitionDetailResponse reqRes = new RequisitionDetailResponse();

		ITIDetailResponse itiRes = new ITIDetailResponse();
		StoreDetailResponse strRes = new StoreDetailResponse();
		List<RequisitionDetailTO> reqList = new ArrayList<RequisitionDetailTO>();
		RequisitionDetailTO reqDtl = null;
		List<IntrwLocationDetailsTO> intrDtlsTO = null;
		List<PhoneScreenIntrwDetailsTO> phnScrnlist = null;
		List<PhoneScreenIntrwDetailsTO> phnScrnResTxtlist = null;
		PhoneScreenIntrwDetailsTO phnScrnIntrwDtls = null;
		List<RequisitionDetailTO> reqEmpCandList = new ArrayList<RequisitionDetailTO>();
		PhoneScreenDAO phnScrnDAO = null;
		List<StaffingDetailsTO> stfList = null;
		StaffingDetailsTO stfDtls = null;
		IntrwLocationDetailsTO intrLocTO = null;
		int phnScrnNbr = 0;
		String hirFlag = FALSE;
		List<PhoneScreenIntrwDetailsTO> phcrnResOverReslist = null;
		List<CandidateDetailsTO> cndtList = null;
		CandidateDetailsTO cndt = null;
		List<PhoneScreenIntrwDetailsTO> phnScrIntrTo = null;
		List<RequisitionScheduleTO> reqSchedTimeList = null;
		RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		CalendarResponse calRes = null;
		CompleteITIDetailResponse cmplITIRes = new CompleteITIDetailResponse();
		StoreDetailsTO store = null;
		List<ReadHumanResourcesStoreEmploymentRequisitionDTO> readStoreReq = new ArrayList<ReadHumanResourcesStoreEmploymentRequisitionDTO>();
		
		try
		{
			res = new Response();
			if(itiNbr != null && !itiNbr.trim().equals(""))
			{
				// input is ITI Number
				phnScrnNbr = Integer.parseInt(itiNbr);
				logger.info(this + "Input is ITI NBR" + phnScrnNbr);

				// getting data from DAO
				phnScrnDAO = new PhoneScreenDAO();
				// ScheduleDAO scheduleDao = new ScheduleDAO();
				calRes = new CalendarResponse();
				PhoneScreenIntrwDetailsTO phnScrnIntrwDtlsPhn = null;
				// calling the readByPhoneScreenNumber method of phonescreen DAO
				logger.info(this + "Before selector readByPhoneScreenNumber ");
				phnScrnlist = phnScrnDAO.readByPhoneScreenNumber(phnScrnNbr);
				if(phnScrnlist != null && !phnScrnlist.isEmpty())
				{
					phnScrnIntrwDtls = phnScrnlist.get(0);

					if(phnScrnIntrwDtls.getCndtNbr() != null)
					{
						// get the phone number of the candidate if present
						phnScrnIntrwDtlsPhn = phnScrnDAO.readAssociatePhoneNumber(phnScrnIntrwDtls.getCndtNbr());
					}
					// check for internal or external based on the Associate ID
					if(phnScrnIntrwDtls.getAid() != null && !phnScrnIntrwDtls.getAid().trim().equals(EMPTY_STRING))
					{
						// Set internal if AID is present
						phnScrnIntrwDtls.setInternalExternal(INTERNAL_FLAG);
						// set candidate phone number if it is internal
						phnScrnIntrwDtls.setCanPhn(phnScrnIntrwDtlsPhn.getCanPhn());
					}
					else
					{
						// Set external if no AID is present
						phnScrnIntrwDtls.setInternalExternal(EXTERNAL_FLAG);
						phnScrnIntrwDtls.setCndStrNbr(EMPTY_STRING);
						phnScrnIntrwDtls.setCndDeptNbr(EMPTY_STRING);
						phnScrnIntrwDtls.setTitle(EMPTY_STRING);
					}
					logger.info(this + "Before readHumanResourcesPhoneScreen ");
					// Fetching Candidate details
					if(phnScrnIntrwDtls.getReqNbr() != null && phnScrnIntrwDtls.getCndtNbr() != null)
					{
						logger.info(this + "Before getting the candidate details");
						cndtList = phnScrnDAO.getCandidateDetails(phnScrnIntrwDtls.getCndtNbr(), Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
						if(cndtList != null && cndtList.size() > 0)
						{
							cndt = cndtList.get(0);
							if(cndt != null)
							{
								// set candidate phone number if it is external
								if(phnScrnIntrwDtls.getInternalExternal().equals(EXTERNAL_FLAG))
								{
									phnScrnIntrwDtls.setCanPhn(cndt.getCanPhn());
								}
								if(phnScrnIntrwDtls.getName() == null)
								{
									phnScrnIntrwDtls.setName(cndt.getName());
								}

								if(phnScrnIntrwDtls.getCndtNbr() == null)
								{
									phnScrnIntrwDtls.setCndtNbr(cndt.getSsnNbr());
								}
							}
						}
						logger.info(this + "After getting the candidate details canphone: " + phnScrnIntrwDtls.getCanPhn());
					}

					phcrnResOverReslist = PhoneScreenDAO.readHumanResourcesPhoneScreen(phnScrnNbr);
					if(phcrnResOverReslist != null && !phcrnResOverReslist.isEmpty() && phcrnResOverReslist.size() > 0)
					{
						phnScrnIntrwDtls.setOverAllStatus(phcrnResOverReslist.get(0).getOverAllStatus());
						phnScrnIntrwDtls.setPhnScreener(phcrnResOverReslist.get(0).getPhnScreener());
						phnScrnIntrwDtls.setYnstatus(phcrnResOverReslist.get(0).getYnstatus());
						phnScrnIntrwDtls.setPhnScrnDate(phcrnResOverReslist.get(0).getPhnScrnDate());
						phnScrnIntrwDtls.setPhnScrnTime(phcrnResOverReslist.get(0).getPhnScrnTime());
						phnScrnIntrwDtls.setEmailAdd(phcrnResOverReslist.get(0).getEmailAdd());
						phnScrnIntrwDtls.setInterviewMaterialStatusTimestamp(phcrnResOverReslist.get(0).getInterviewMaterialStatusTimestamp());
						phnScrnIntrwDtls.setInterviewStatusTimestamp(phcrnResOverReslist.get(0).getInterviewStatusTimestamp());
						phnScrnIntrwDtls.setInterviewMaterialStatusCode(phcrnResOverReslist.get(0).getInterviewMaterialStatusCode());
						phnScrnIntrwDtls.setInterviewStatusCode(phcrnResOverReslist.get(0).getInterviewStatusCode());
						phnScrnIntrwDtls.setPhoneScreenStatusCode(phcrnResOverReslist.get(0).getPhoneScreenStatusCode());
					}
					logger.info(this + "Before readInterviewDetails ");
					// calling the DAO to get the interview details
					intrDtlsTO = phnScrnDAO.readInterviewDetails(phnScrnNbr);

					logger.info(this + "Before readRequisitionDetails " + Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
					// getting the requisition details
					reqList = PhoneScreenDAO.readRequisitionDetails(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
					if(reqList != null && !reqList.isEmpty() && reqList.size() > 0)
					{
						reqDtl = reqList.get(0);

						//Get the Calendar Name based on the reqCalId
						RequisitionCalendarTO reqCalTO = null; 
						reqCalTO = ScheduleManager.getRequisitionCalendarForReqCalId(reqDtl.getReqCalId());
						if (reqCalTO == null) {
							//reqCalId was zero, No Calendar
							reqDtl.setRequisitionCalendarName(" ");
						} else {
							//ATS Hiring Event, Calendar Description will contain ~|~ need to check for it if the reqnCalId != 0.
							if (reqCalTO.getRequisitionCalendarId() != 0 && reqCalTO.getRequisitionCalendarDescription().contains("~|~")) {
								String[] splitDesc = reqCalTO.getRequisitionCalendarDescription().split("~\\|~");
								//Index 0 is the Creating Str Num and Index 1 is the Description
								reqDtl.setRequisitionCalendarName(splitDesc[1]);
							} else {
								reqDtl.setRequisitionCalendarName(reqCalTO.getRequisitionCalendarDescription());
							}
						}
													
						// Deferred ticket: 3373
						int offerExtCandCnt = 0;
						reqEmpCandList = ScheduleDAO.readHumanResourcesStoreEmploymentRequisitionPositionCountByInputList(Integer.parseInt(phnScrnIntrwDtls
						    .getReqNbr()));
						if(reqEmpCandList != null && !reqEmpCandList.isEmpty() && reqEmpCandList.size() > 0)
						{
							offerExtCandCnt = (Integer.parseInt(reqEmpCandList.get(0).getAuthPosCount().trim()) - reqEmpCandList.get(0).getOfferExtCandCnt());
							reqDtl.setAuthPosCount(reqEmpCandList.get(0).getAuthPosCount());
							reqDtl.setOfferExtCandCnt(offerExtCandCnt);
						}

						// Deferred ticket: 3373
						int remainingIntwCandCnt = 0;
						remainingIntwCandCnt = phnScrnDAO.readRemainingInterviewCandidateCountByPhoneScreenStatusCode(Integer.parseInt(phnScrnIntrwDtls
						    .getReqNbr()), true);
						reqDtl.setTotal(remainingIntwCandCnt);

						logger.info(this + "After fetching the Str Emp Req Position & status of candiadate:" + reqDtl.getAuthPosCount() + "  value:"
						    + reqDtl.getOfferExtCandCnt());

						// to get the Human Resource Requisition Schedule
						logger.info(this + "Before HumanReadRequistion Schedule");
						// setup the start date to be tomorrow at 5am
						Calendar beginCal = Calendar.getInstance();
						beginCal.set(Calendar.HOUR_OF_DAY, 4);
						beginCal.set(Calendar.MINUTE, 0);
						beginCal.set(Calendar.SECOND, 0);
						beginCal.set(Calendar.MILLISECOND, 0);
						beginCal.add(Calendar.DATE, 1);

						reqSchedTimeList = ScheduleManager.getAvailableInterviewSlots(reqDtl.getRscSchdFlg(), reqDtl.getReqCalId(), new Timestamp(beginCal
						    .getTimeInMillis()), Short.parseShort(reqDtl.getInterviewDurtn()));

						if((reqSchedTimeList != null) && (!reqSchedTimeList.isEmpty()))
						{
							logger.info(this + "reqSchedTimeList not empty");
							calRes.setSchedules(reqSchedTimeList);
						}
						logger.info(this + "After HumanReadRequistion Schedule");

						// to get the hiring event information
						stfList = phnScrnDAO.readRequisitionStaffingDetails(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
						if(stfList != null && !stfList.isEmpty() && stfList.size() > 0)
						{
							stfDtls = stfList.get(0);
							res.setStfDtls(stfDtls);
						}

						// get the store details, no null check if the store is not found the LocationManager will throw a NoRowsFoundException
						store = LocationManager.getStoreDetailsDAO20(reqDtl.getStore());
						
						// setting the interview dtls to PhoneScreenTO
						// if the hiring event flag is N, then need to get the
						// values from Store Details
						if((intrDtlsTO == null || intrDtlsTO.size() < 1 || intrDtlsTO.get(0) == null
						    || intrDtlsTO.get(0).getInterviewLocId() == null || intrDtlsTO.get(0).getInterviewLocId().trim().equals(EMPTY_STRING))
						    && (hirFlag == null || hirFlag.equals(FALSE)))
						{
							phnScrnIntrwDtls.setInterviewLocTypCd(HIRING_STR_TYP_CD);
							intrLocTO = new IntrwLocationDetailsTO();
							intrLocTO.setAdd(store.getAdd());
							intrLocTO.setCity(store.getCity());
							intrLocTO.setState(store.getState());
							intrLocTO.setZip(store.getZip());
							intrLocTO.setPhone(store.getPhone());
							intrLocTO.setInterviewLocName(store.getStrName());
							intrLocTO.setInterviewLocId(HIRING_STR_TYP_CD);

							// 3432 - Default interviewer name on the
							// Interview Schedule Screen
							if(intrDtlsTO.get(0).getInterviewer() != null && intrLocTO != null && intrDtlsTO.size() > 0)
							{
								intrLocTO.setInterviewer(StringUtils.trim(intrDtlsTO.get(0).getInterviewer()));
							}
						}

						// if the hiring event flag is Y, then need to get the
						// values from Staffing Details
						else if((intrDtlsTO == null || intrDtlsTO.isEmpty() || intrDtlsTO.size() < 0 || intrDtlsTO.get(0) == null
						    || intrDtlsTO.get(0).getInterviewLocId() == null || intrDtlsTO.get(0).getInterviewLocId().trim().equals(EMPTY_STRING))
						    && (hirFlag != null && hirFlag.equals(TRUE)))
						{
							phnScrnIntrwDtls.setInterviewLocTypCd(HIRING_EVT_TYP_CD);
							intrLocTO = new IntrwLocationDetailsTO();
							if(stfDtls != null)
							{
								intrLocTO.setAdd(stfDtls.getAdd());
								intrLocTO.setCity(stfDtls.getCity());
								intrLocTO.setState(stfDtls.getState());
								intrLocTO.setZip(stfDtls.getZip());
								intrLocTO.setPhone(stfDtls.getStfhrgEvntLocPhn());
								intrLocTO.setInterviewLocName(stfDtls.getStfhrgEvntLoc());

								// 3432 - Default interviewer name on the
								// Interview Schedule Screen
								if(intrDtlsTO.get(0).getInterviewer() != null && intrLocTO != null && intrDtlsTO.size() > 0)
								{
									intrLocTO.setInterviewer(StringUtils.trim(intrDtlsTO.get(0).getInterviewer()));
								}
								// intrLocTO.setInterviewer(stfDtls.getHrgMgrName());
							}
						}
						else
						{
							// if the interview details is having any value,
							// then need to get from ther
							if(intrDtlsTO != null && !intrDtlsTO.isEmpty() && intrDtlsTO.size() > 0 && intrDtlsTO.get(0) != null)
							{
								phnScrnIntrwDtls.setInterviewLocTypCd(intrDtlsTO.get(0).getInterviewLocId());

								// MTS1876

								if(intrDtlsTO.get(0).getInterviewTime() != null)
								{
									// Added for defect 4243
									// [[
									String intrwStoreno = "";
									if(logger.isDebugEnabled())
									{
										logger.info("Enter into send packet calculation");
										logger.info("calender Id -->" + reqDtl.getReqCalId());
									}
									List<ReqScheduleTO> reqSchedList = null;

									Timestamp interviewTime = Util.convertTimestampTOtoTimestamp(intrDtlsTO.get(0).getInterviewTime());
									if(logger.isDebugEnabled())
									{
										logger.info("Interview time -->" + interviewTime.toString());
									}
									reqSchedList = delReqMgr.readHumanResourcesRequisitionSchedule(reqDtl.getReqCalId(), interviewTime);

									if(reqSchedList != null && reqSchedList.size() > 0)
									{
										intrwStoreno = reqSchedList.get(0).getHrSysStrNbr();
										if(logger.isDebugEnabled())
										{
											logger.info("storeno -->" + intrwStoreno);
										}
									}
									// ]]
									logger.info(this + "Before readSendPacketDateTime " + intrwStoreno);
									TimeStampTO timeStamp = new TimeStampTO();
									// for defect fix
									// timeStamp =
									// getSendPacketDateTime(intrDtlsTO.get(0).getInterviewTime(),
									// reqDtl.getStore());
									timeStamp = getSendPacketDateTime(intrDtlsTO.get(0).getInterviewTime(), intrwStoreno);
									intrDtlsTO.get(0).setSendPacketTime(timeStamp);
								}
								intrLocTO = intrDtlsTO.get(0); // Deferred 3432
															   // fixed
								// MTS1876

							}
							// worst case scenario
							else
							{
								phnScrnIntrwDtls.setInterviewLocTypCd(OTHR_LOC_TYP_CD);
								intrLocTO = new IntrwLocationDetailsTO();
							}
						}
						phnScrnIntrwDtls.setIntrLocDtls(intrLocTO);
					}
					logger.info(this + "Before readResponses ");
					// setting the 10 minimum response
					phnScrnIntrwDtls.setMinResList(phnScrnDAO.readResponses(phnScrnNbr));
					logger.info(this + "Before readPhoneScreenEmploymentRequesitionNote ");
					// to get the note text for Response - notetypecode is 1 -
					// Contact History
					phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(phnScrnNbr, CONTACT_HISTRY_NOTE_TYP_CD);
					if(phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0)
					{
						phnScrnIntrwDtls.setContactHistoryTxt(phnScrnResTxtlist.get(0).getContactHistoryTxt());

					}

					// to get the note text for Response - notetypecode is 2
					// -Detail response Text History
					phnScrnResTxtlist = null;
					phnScrnResTxtlist = phnScrnDAO.readPhoneScreenEmploymentRequesitionNote(phnScrnNbr, PHN_SCRN_RESPONSE_NOTE_TYP_CD);
					if(phnScrnResTxtlist != null && !phnScrnResTxtlist.isEmpty() && phnScrnResTxtlist.size() > 0)
					{
						phnScrnIntrwDtls.setDetailTxt(phnScrnResTxtlist.get(0).getDetailTxt());

					}

					// To fetch Complete phone screen details(Both Active and
					// Inactive)
					phnScrIntrTo = delReqMgr.readCompletePhoneScreenDetails(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
					if(phnScrIntrTo != null && phnScrIntrTo.size() > 0)
					{

						cmplITIRes.setITIDtlList(phnScrIntrTo);
						res.setCmplITIDet(cmplITIRes);
					}

					// To set Authorisation position count for requisition

					readStoreReq = phnScrnDAO.readHumanResourcesStoreEmploymentRequisition(Integer.parseInt(phnScrnIntrwDtls.getReqNbr()));
					if(readStoreReq != null && readStoreReq.size() > 0)
					{
						res.setAuthCnt(String.valueOf(readStoreReq.get(0).getAuthorizationPositionCount()));
					}

					phnScrnIntrwDtls.setHrEvntFlg(hirFlag);
					// setting the values to Response object
					phnScrnlist = null;
					phnScrnlist = new ArrayList<PhoneScreenIntrwDetailsTO>();
					phnScrnlist.add(phnScrnIntrwDtls);

					// reading interview Material Status code
					// Removed by MTS1876 on 06/03/2011. The Flex command that
					// this is returned to does not read it. Statuses for the
					// Interview page
					// are loaded before this is called
					reqRes.setReqDtlList(reqList);
					strRes.addStore(store);
					itiRes.setITIDtlList(phnScrnlist);

					res.setStatus(SUCCESS_APP_STATUS);
					res.setItiDtRes(itiRes);
					res.setReqDtlList(reqRes);
					res.setStrDtRes(strRes);
					res.setStfDtls(stfDtls);
					res.setCalendarRes(calRes);
					// res.setIntrMatStsRes(intMatStsRes);
				}
				else
				{
					logger.error(this + "Invalid input");
					throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
				}

			}
			else
			{
				logger.error(this + "Input is either null or empty string");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}
		}
		catch (RetailStaffingException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getPhoneScreenIntrwDtls method in Manager with output as" + res);
		return res;

	}
	
	public TimeStampTO getSendPacketDateTime(TimeStampTO obj, String strNbr) throws RetailStaffingException 
	{
		logger.debug("Entering getSendPacketDateTime()");
		TimeStampTO returnedTime = null; // new TimeStampTO();
		
		try 
		{
			// validate the store number provided is not null or empty
			ValidationUtils.validateNotNullOrEmpty(InputField.STORE_NUMBER, strNbr);
			
			int day = Integer.parseInt(obj.getDay());
			int month = Integer.parseInt(obj.getMonth());
			int year = Integer.parseInt(obj.getYear());
			int hour = Integer.parseInt(obj.getHour());
			int minute = Integer.parseInt(obj.getMinute());
			int second = Integer.parseInt(obj.getSecond());

			final String DATE_TIME_FORMAT = "yyyyMMdd-HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month - 1);
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			cal.set(Calendar.SECOND, second);

			// Date interviewDate;
			Date packetDate = null;
			boolean isDayLightSavings = false;
			
			//Date theDate = "";
			TimeZone objTZ = TimeZone.getTimeZone("US/Eastern");
			Calendar cal1 = Calendar.getInstance();
			cal1.set(Calendar.YEAR, year);
			cal1.set(Calendar.MONTH, month - 1);
			cal1.set(Calendar.DAY_OF_MONTH, day);
			if (objTZ.inDaylightTime(cal1.getTime())) 
			{
				isDayLightSavings = true;
			}
			
			// Get Time Zone Code
			StoreDetailsTO store = LocationManager.getStoreDetailsDAO20(strNbr);
			
			if(logger.isDebugEnabled())
			{
				logger.debug(String.format("***getSendPacketDateTime() method in Manager Time Zone code = %1$s", store.getTimeZoneCode()));
			} // end if
			
			if(store.getTimeZoneCode() != null)
			{
				if (store.getTimeZoneCode().equals("13005")) 
				{
					// Arizona
					cal.add(Calendar.HOUR, (isDayLightSavings ? 3 : 2));
				} 
				else if (store.getTimeZoneCode().equals("13411")) 
				{
					// Eastern
					cal.add(Calendar.HOUR, 0);
				} 
				else if (store.getTimeZoneCode().equals("13406")) 
				{
					// Mountain
					cal.add(Calendar.HOUR, 2);
				} 
				else if (store.getTimeZoneCode().equals("13404")) 
				{
					// Pacific
					cal.add(Calendar.HOUR, 3);
				} 
				else if (store.getTimeZoneCode().equals("13407") || store.getTimeZoneCode().equals("13007")) 
				{
					// Central
					cal.add(Calendar.HOUR, 1);
				} 
				else if (store.getTimeZoneCode().equals("13403")) 
				{
					// Alaska
					cal.add(Calendar.HOUR, 4);
				}
				// for all others, store is not in Scope of Application so just return the same time
			} // end if(store.getTimeZoneCode() != null)
			
			packetDate = cal.getTime();

			String returnedPacketDate = sdf.format(packetDate);
			
			returnedTime = new TimeStampTO();
			returnedTime.setYear(returnedPacketDate.substring(0,4));
			returnedTime.setMonth(returnedPacketDate.substring(4,6));
			returnedTime.setDay(returnedPacketDate.substring(6,8));
			returnedTime.setHour(returnedPacketDate.substring(9,11));
			returnedTime.setMinute(returnedPacketDate.substring(12,14));
			returnedTime.setSecond(returnedPacketDate.substring(15));
		} 
		catch (QueryException qe) 
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, qe);
		}
		
		logger.debug("Exiting getSendPacketDateTime()");
		return returnedTime;
	}
	
	public Response readHumanRequisitionSchedule(String rscSchdFlg, int reqCalId, String interviewDuration) throws RetailStaffingException
	{
		logger.info(String.format("Enters readHumanRequisitionSchedule method in Manager with input rscSchdFlg: %1$s, reqCalId: %2$d, interviewDuration: %3$s", rscSchdFlg, reqCalId, interviewDuration));

		Response res = null;

		//RequisitionDetailResponse reqRes = new RequisitionDetailResponse();

		List<RequisitionScheduleTO> reqSchedTimeList = null;
		//RetailStaffingRequisitionDAO delReqMgr = new RetailStaffingRequisitionDAO();
		CalendarResponse calRes = null;
		
		try
		{
			res = new Response();
			calRes = new CalendarResponse();
			if(rscSchdFlg != null && !rscSchdFlg.trim().equals("") && interviewDuration != null && !interviewDuration.trim().equals("") && reqCalId != -1) {
				
				// setup the start date to be tomorrow at 5am
				Calendar beginCal = Calendar.getInstance();
				beginCal.set(Calendar.HOUR_OF_DAY, 4);
				beginCal.set(Calendar.MINUTE, 0);
				beginCal.set(Calendar.SECOND, 0);
				beginCal.set(Calendar.MILLISECOND, 0);
				beginCal.add(Calendar.DATE, 1);

				reqSchedTimeList = ScheduleManager.getAvailableInterviewSlots(rscSchdFlg, reqCalId, new Timestamp(beginCal
						    .getTimeInMillis()), Short.parseShort(interviewDuration));

				if((reqSchedTimeList != null) && (!reqSchedTimeList.isEmpty()))
				{
					logger.info(this + "reqSchedTimeList not empty");
					calRes.setSchedules(reqSchedTimeList);
				}
					
				res.setCalendarRes(calRes);
				
			} else {
				logger.error(this + "Invalid input");
				throw new RetailStaffingException(RetailStaffingConstants.INVALID_INPUT_CODE);
			}

		}
		catch (RetailStaffingException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getPhoneScreenIntrwDtls method in Manager with output as" + res);
		return res;

	}	
	
	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * Description : This method will format(date, time, phoneNumber) the interview response.
	 * @param response
	 * @return
	 * @throws RetailStaffingException
	 */
	public Response getFormattedInterviewResponse(Response response) throws RetailStaffingException
	{
		logger.debug("Entering getFormattedInterviewResponse()");
		try
		{
			if(response!=null ){
				//Rounding off TargetPay
				if(response.getStfDtls()!=null){
					response.getStfDtls().setTargetPayRounded(Util.setTargetPay(response.getStfDtls().getTargetPay()));
				}
				
				//Formatting phone number for display purpose
				formatPhoneNumber(response);
				
				//Setting Formatted Date(MM/DD/YYYY) and Formatted Time (HH:MM or HH:MM AM/PM)
				formatDateTime(response.getCalendarRes());
				
				//Set the timStampTo based upon the store zone time
				if( response.getStrDtRes()!=null && !Util.isNullList(response.getStrDtRes().getStrDtlList())){
					StoreDetailsTO storeDetailsTO = response.getStrDtRes().getStrDtlList().get(0);
					String timeZoneCode = storeDetailsTO.getTimeZoneCode();
					TimeStampTO interviewTimeStampTO = getInterviewTimeForZone(response);
					if(interviewTimeStampTO!=null && !Util.isNullString(timeZoneCode)){
						storeDetailsTO.setPacketDateTime(Util.getStorePacketDateTime(interviewTimeStampTO, timeZoneCode));
					}
				}
			}
		}
		catch (Exception e)
		{
			throw new RetailStaffingException(RetailStaffingConstants.APP_FATAL_ERROR_CODE, e);
		}
		logger.info(this + "Leaves getFormattedInterviewResponse method in Manager with output as" + response);
		return response;
	}
	
	private TimeStampTO getInterviewTimeForZone(Response response)
	{
		try{
			TimeStampTO interviewTimeStampTO = response.getItiDtRes().getITIDtlList().get(0).getIntrLocDtls().getInterviewTime();
			TimeStampTO newTimeStampTO = new TimeStampTO();
			newTimeStampTO.setDay(interviewTimeStampTO.getDay());
			newTimeStampTO.setHour(interviewTimeStampTO.getHour());
			newTimeStampTO.setMilliSecond(interviewTimeStampTO.getMilliSecond());
			newTimeStampTO.setMinute(interviewTimeStampTO.getMinute());
			newTimeStampTO.setMonth(interviewTimeStampTO.getMonth());
			newTimeStampTO.setSecond(interviewTimeStampTO.getSecond());
			newTimeStampTO.setYear(interviewTimeStampTO.getYear());
			return newTimeStampTO;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * Description: This method will return the formatted phone number
	 * @param response
	 */
	private void formatPhoneNumber(Response response )
	{
		//Formatting phone number for display purpose
		if(response.getStrDtRes()!=null && !Util.isNullList(response.getStrDtRes().getStrDtlList()) ){
			for(StoreDetailsTO store: response.getStrDtRes().getStrDtlList()){
				store.setFormattedPhone(Util.formatPhoneNbr(store.getPhone()));
			}
		}
		
		//Formatting phone number for display purpose
		if(response.getItiDtRes()!=null && !Util.isNullList(response.getItiDtRes().getITIDtlList()) ){
			for(PhoneScreenIntrwDetailsTO phnScrnIntrWDtls : response.getItiDtRes().getITIDtlList()){
				if(EXTERNAL_FLAG.equalsIgnoreCase(phnScrnIntrWDtls.getInternalExternal())){
					phnScrnIntrWDtls.setCanPhnFormatted(Util.formatPhoneNbr(phnScrnIntrWDtls.getCanPhn()));
				}else{
					phnScrnIntrWDtls.setCanPhnFormatted(phnScrnIntrWDtls.getCanPhn());
				}
				
				//Set formattedDate(MM/DD/YYYY) and formattedTime(HH:MM AM/PM)
				if(phnScrnIntrWDtls.getIntrLocDtls() != null){
					Util.setFormattedDateAndTime(phnScrnIntrWDtls.getIntrLocDtls().getInterviewTime(),true);
					Util.setFormattedDateAndTime(phnScrnIntrWDtls.getIntrLocDtls().getSendPacketTime(),true);
				}
				Util.setFormattedDateAndTime(phnScrnIntrWDtls.getInterviewStatusTime(),true);
				Util.setFormattedDateAndTime(phnScrnIntrWDtls.getInterviewMaterialStatusTimestamp(),true);
			}
		}
	}
	
	/**
	 * This method will format the date and time
	 * @param calendarResponse
	 */
	private void formatDateTime(CalendarResponse calendarResponse)
	{
		if(calendarResponse!=null && !Util.isNullList(calendarResponse.getSchedules())){
			for(RequisitionScheduleTO reqSchTO : calendarResponse.getSchedules() ){
				if(reqSchTO.getBeginTimestamp()!=null){
					reqSchTO.setFormattedBeginDate(Util.getDateFromTs(reqSchTO.getBeginTimestamp()));
					reqSchTO.setFormattedBeginTime(Util.getTimeFromTs(reqSchTO.getBeginTimestamp(),true));
					reqSchTO.setFormatted24HrBeginTime(Util.getTimeFromTs(reqSchTO.getBeginTimestamp(),false));
				}
				
				if(reqSchTO.getEndTimestamp()!=null){
					reqSchTO.setFormattedEndDate(Util.getDateFromTs(reqSchTO.getEndTimestamp()));
					reqSchTO.setFormattedEndTime(Util.getTimeFromTs(reqSchTO.getEndTimestamp(),true));
					reqSchTO.setFormatted24HrEndTime(Util.getTimeFromTs(reqSchTO.getEndTimestamp(),false));
				}
			}
		}
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
}

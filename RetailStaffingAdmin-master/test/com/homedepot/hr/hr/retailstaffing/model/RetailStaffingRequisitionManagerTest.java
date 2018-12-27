package com.homedepot.hr.hr.retailstaffing.model;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

import com.homedepot.hr.hr.retailstaffing.dto.DateTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.JobTitleRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateReviewPhnScrnRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.UpdateStaffingRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.ScheduleDescResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingRequisitionManager;
import com.homedepot.hr.hr.retailstaffing.services.response.StatusResponse;


public class RetailStaffingRequisitionManagerTest {
	
	
	@Test
	public void testFormatDateStaffgDtls_1()
		throws Exception {
		StaffingDetailsTO staffingDetails = new StaffingDetailsTO();
		DateTO dateTO = new DateTO();
		dateTO.setDay("22");
		dateTO.setFormattedDate("22/06/2015");
		dateTO.setMonth("06");
		dateTO.setYear("2015");
		DateTO bdateTO = new DateTO();
		bdateTO.setDay("23");
		bdateTO.setFormattedDate("23/06/2015");
		bdateTO.setMonth("06");
		bdateTO.setYear("2015");
		
		staffingDetails.setStfHrgEvntEndDt(dateTO);
		staffingDetails.setWeekBeginDt(bdateTO);
		staffingDetails.setStfHrgEvntStartDt(bdateTO);
		RetailStaffingRequisitionManager.formatDateStaffgDtls(staffingDetails);

		// add additional test code here
	}

	
	@Test
	public void testFormatInterviewDate_1()
		throws Exception {
		List<PhoneScreenIntrwDetailsTO> phoneScreenIntrwDetailsTOList = new ArrayList();
		PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO = new PhoneScreenIntrwDetailsTO();
		phoneScreenIntrwDetailsTO.setInternalExternal("EXTERNAL");
		phoneScreenIntrwDetailsTO.setCanPhn("124366581");
		
		TimeStampTO interviewTimeStampTO = new TimeStampTO();
		interviewTimeStampTO.setDay("23");
		interviewTimeStampTO.setMonth("06");
		interviewTimeStampTO.setYear("2015");
		interviewTimeStampTO.setSecond("50");
		interviewTimeStampTO.setHour("11");
		interviewTimeStampTO.setFormattedDate("23/06/2015");
		interviewTimeStampTO.setMinute("50");
		interviewTimeStampTO.setMilliSecond("110");
		IntrwLocationDetailsTO intrwLocationDetailsTO = new IntrwLocationDetailsTO();
		intrwLocationDetailsTO.setInterviewTime(interviewTimeStampTO);
		intrwLocationDetailsTO.setSendPacketTime(interviewTimeStampTO);
		phoneScreenIntrwDetailsTO.setIntrLocDtls(intrwLocationDetailsTO);
		phoneScreenIntrwDetailsTOList.add(phoneScreenIntrwDetailsTO);
		
		ITIDetailResponse iTIDetailResponse = new ITIDetailResponse();		
		iTIDetailResponse.setITIDtlList(phoneScreenIntrwDetailsTOList);
		

		RetailStaffingRequisitionManager.formatInterviewDate(iTIDetailResponse);
		// add additional test code here
	}

	
	
	@Test
	public void testFormatTimeStaffgDtls_1()
		throws Exception {
		StaffingDetailsTO staffingDetails = new StaffingDetailsTO();
		TimeStampTO interviewTimeStampTO = new TimeStampTO();
		interviewTimeStampTO.setDay("23");
		interviewTimeStampTO.setMonth("06");
		interviewTimeStampTO.setYear("2015");
		interviewTimeStampTO.setSecond("50");
		interviewTimeStampTO.setHour("11");
		interviewTimeStampTO.setFormattedDate("23/06/2015");
		interviewTimeStampTO.setMinute("50");
		interviewTimeStampTO.setMilliSecond("110");
		staffingDetails.setLunch(interviewTimeStampTO);
		staffingDetails.setLastIntrTm(interviewTimeStampTO);
		staffingDetails.setEndTime(interviewTimeStampTO);
		staffingDetails.setStartTime(interviewTimeStampTO);

		RetailStaffingRequisitionManager.formatTimeStaffgDtls(staffingDetails);

		// add additional test code here
	}

	
	
	@Test
	public void testGetFormattedRequisitionResponse_1()
		throws Exception {
		RetailStaffingRequisitionManager fixture = new RetailStaffingRequisitionManager();
		fixture.eventId = 1;
		
		StaffingDetailsTO staffingDetails = new StaffingDetailsTO();
		TimeStampTO interviewTimeStampTO = new TimeStampTO();
		interviewTimeStampTO.setDay("23");
		interviewTimeStampTO.setMonth("06");
		interviewTimeStampTO.setYear("2015");
		interviewTimeStampTO.setSecond("50");
		interviewTimeStampTO.setHour("11");
		interviewTimeStampTO.setFormattedDate("23/06/2015");
		interviewTimeStampTO.setMinute("50");
		interviewTimeStampTO.setMilliSecond("110");
		staffingDetails.setLunch(interviewTimeStampTO);
		staffingDetails.setLastIntrTm(interviewTimeStampTO);
		staffingDetails.setEndTime(interviewTimeStampTO);
		staffingDetails.setStartTime(interviewTimeStampTO);
		
		List<PhoneScreenIntrwDetailsTO> phoneScreenIntrwDetailsTOList = new ArrayList();
		PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO = new PhoneScreenIntrwDetailsTO();
		phoneScreenIntrwDetailsTO.setInternalExternal("EXTERNAL");
		phoneScreenIntrwDetailsTO.setCanPhn("124366581");
		
		IntrwLocationDetailsTO intrwLocationDetailsTO = new IntrwLocationDetailsTO();
		intrwLocationDetailsTO.setInterviewTime(interviewTimeStampTO);
		intrwLocationDetailsTO.setSendPacketTime(interviewTimeStampTO);
		phoneScreenIntrwDetailsTO.setIntrLocDtls(intrwLocationDetailsTO);
		phoneScreenIntrwDetailsTOList.add(phoneScreenIntrwDetailsTO);
		
		ITIDetailResponse iTIDetailResponse = new ITIDetailResponse();		
		iTIDetailResponse.setITIDtlList(phoneScreenIntrwDetailsTOList);
		
		List<Status> statusList = new ArrayList();
		Status status = new Status();
		status.setCode((short)1);
		status.setLangCd("EN_US");
		status.setLastUpdSysUsrId("PSLXC");
		status.setLastUpdTs(new java.sql.Date(new java.util.Date().getTime()));
		status.setDispStatCd("1");
		status.setShortStatDesc("INITIATE BASIC");	
		status.setStatDesc("INITIATE BASIC PHONE SCREEN");
		statusList.add(status);
		StatusResponse statusResponse = new StatusResponse();
		statusResponse.addPhoneScreenStat(status);
		
		ScheduleDescResponse scheduleDescResponse = new ScheduleDescResponse();
		
		Response response = new Response();
		response.setStfDtls(staffingDetails);
		response.setItiDtRes(iTIDetailResponse);
		response.setStatusListRes(statusResponse);
		response.setScheduleRes(scheduleDescResponse);

		Response result = fixture.getFormattedRequisitionResponse(response);

		// add additional test code here
		assertNotNull(result);
		/*assertEquals(null, result.getUserData());
		assertEquals(null, result.getStatus());
		assertEquals(null, result.getRegionList());
		assertEquals(null, result.getStructuredInterviewGuideListRes());
		assertEquals(null, result.getHiringEventMgrData());
		assertEquals(null, result.getItiDtResInact());
		assertEquals(null, result.getErrorResponse());
		assertEquals(null, result.getFirstRecordInfo());
		assertEquals(null, result.getSecondRecordInfo());
		assertEquals(null, result.getProcessStsRes());
		assertEquals(null, result.getLangSklsRes());
		assertEquals(null, result.getCalendarRes());
		assertEquals(null, result.getShowQpButton());
		assertEquals(null, result.getNoIntvwReasonRes());
		assertEquals(null, result.getOfferMadeListRes());
		assertEquals(null, result.getOfferResultListRes());
		assertEquals(null, result.getCandinfoDtRes());
		assertEquals(null, result.getInterviewerDtlsRes());
		assertEquals(null, result.getLicenseInfoRes());
		assertEquals(0, result.getRejectedReasonId());
		assertEquals(null, result.getCandidateDetails());
		assertEquals(null, result.getLocationDetails());
		assertEquals(null, result.getStrDlExemptRes());
		assertEquals(null, result.getPhnScrDispCodeRes());
		assertEquals(null, result.getStateDtRes());
		assertEquals(null, result.getCanDtRes());
		assertEquals(null, result.getAuthCnt());
		assertEquals(null, result.getCmplITIDet());
		assertEquals(null, result.getDistList());
		assertEquals(null, result.getDivList());
		assertEquals(null, result.getAutoHold());
		assertEquals(null, result.getReqDtlList());
		assertEquals(null, result.getSuccessMsg());
		assertEquals(null, result.getExpLvlRes());
		assertEquals(null, result.getStrDtRes());
		assertEquals(null, result.getDeptRes());
		assertEquals(null, result.getJobTtlRes());
		assertEquals(null, result.getReqDtRes());
		assertEquals(null, result.getIntrwDtRes());
		assertEquals(null, result.getRejectList());
		assertEquals(null, result.getMaxDOBDate());
		assertEquals(null, result.getOfferDeclineListRes());
		assertEquals(null, result.getBackgroundChkDtlRes());
		assertEquals(null, result.getHiringEventResponse());
		assertEquals(null, result.getAutoAttachJobTitleRes());
		assertEquals(null, result.getCurrentDate());*/
	}

	
	
	
	@Test
	public void testSortITDlsListByName_1()
		throws Exception {
		List<PhoneScreenIntrwDetailsTO> phoneScreenIntrwDetailsTOList = new ArrayList();
		PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO = new PhoneScreenIntrwDetailsTO();
		phoneScreenIntrwDetailsTO.setInternalExternal("EXTERNAL");
		phoneScreenIntrwDetailsTO.setCanPhn("124366581");
		
		TimeStampTO interviewTimeStampTO = new TimeStampTO();
		interviewTimeStampTO.setDay("23");
		interviewTimeStampTO.setMonth("06");
		interviewTimeStampTO.setYear("2015");
		interviewTimeStampTO.setSecond("50");
		interviewTimeStampTO.setHour("11");
		interviewTimeStampTO.setFormattedDate("23/06/2015");
		interviewTimeStampTO.setMinute("50");
		interviewTimeStampTO.setMilliSecond("110");
		IntrwLocationDetailsTO intrwLocationDetailsTO = new IntrwLocationDetailsTO();
		intrwLocationDetailsTO.setInterviewTime(interviewTimeStampTO);
		intrwLocationDetailsTO.setSendPacketTime(interviewTimeStampTO);
		phoneScreenIntrwDetailsTO.setIntrLocDtls(intrwLocationDetailsTO);
		phoneScreenIntrwDetailsTOList.add(phoneScreenIntrwDetailsTO);
		
		ITIDetailResponse iTIDetailResponse = new ITIDetailResponse();		
		iTIDetailResponse.setITIDtlList(phoneScreenIntrwDetailsTOList);

		RetailStaffingRequisitionManager.sortITDlsListByName(iTIDetailResponse);

		// add additional test code here
	}

	
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(RetailStaffingRequisitionManagerTest.class);
	}
}
package com.homedepot.hr.hr.retailstaffing.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import static org.junit.Assert.*;

import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.Status;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.CreatePhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.ITIUpdateRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.SummaryRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.CalendarResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingITIManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingInterviewManager;
import com.homedepot.hr.hr.retailstaffing.services.response.StatusResponse;


public class RetailStaffingITIManagerTest {


	
	@Test
	public void testGetFormmattedPhnScrnResponse_1()
		throws Exception {
		RetailStaffingITIManager fixture = new RetailStaffingITIManager();
		Response response = new Response();
		
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
		response.setItiDtRes(iTIDetailResponse);
		
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
		response.setStatusListRes(statusResponse);
		Response result = fixture.getFormmattedPhnScrnResponse(response);

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
		assertEquals(null, result.getScheduleRes());
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
		assertEquals(null, result.getItiDtRes());
		assertEquals(null, result.getCanDtRes());
		assertEquals(null, result.getAuthCnt());
		assertEquals(null, result.getCmplITIDet());
		assertEquals(null, result.getDistList());
		assertEquals(null, result.getStfDtls());
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
	public void testFormatDateTime() throws Exception{
		RetailStaffingITIManager retailStaffingITIManager = new RetailStaffingITIManager();
		RetailStaffingITIManager retailStaffingITIManagerrSpy = PowerMockito.spy(retailStaffingITIManager);
		Response response = PowerMockito.mock(Response.class);
		PowerMockito.when(retailStaffingITIManagerrSpy,"formatDateTime",response).thenCallRealMethod(); 
		PowerMockito.verifyPrivate(retailStaffingITIManagerrSpy,Mockito.times(1)).invoke("formatDateTime",response);
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(RetailStaffingITIManagerTest.class);
	}
}
package com.homedepot.hr.hr.retailstaffing.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.CalendarResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.ITIDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.dto.response.StoreDetailResponse;
import com.homedepot.hr.hr.retailstaffing.exceptions.RetailStaffingException;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingInterviewManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RetailStaffingInterviewManager.class})
public class RetailStaffingInterviewManagerTest {

	@Test
	public void testGetFormattedInterviewResponse_1()
		throws Exception {
		RetailStaffingInterviewManager fixture = new RetailStaffingInterviewManager();
		fixture.eventId = 1;
		StaffingDetailsTO staffingDetailsTO = new StaffingDetailsTO();
		staffingDetailsTO.setHrgMgrName("TESTER QATESTER");
		staffingDetailsTO.setHrgMgrTtl("Sales Test Case 3");
		staffingDetailsTO.setHrgMgrPhn("6783331221");
		staffingDetailsTO.setTargetPay("22.0000");
		staffingDetailsTO.setDesiredExp("0");
		staffingDetailsTO.setRequestNbr("11501659");
		staffingDetailsTO.setHrgEvntFlg("N");
		staffingDetailsTO.setQualPoolNts("Language Skills:French Candidate Type:ExternalSchedule Preference:   Anytime: Mon, Tue,   Early AM: Wed,   Mornings: Thr,");
		staffingDetailsTO.setReferals("Notes field");
		staffingDetailsTO.setHiringEventID("0");
		staffingDetailsTO.setAutoAttachReqn(false);
	
		List<RequisitionScheduleTO> reqShecduleList= new ArrayList();
		RequisitionScheduleTO reqShecdule=new RequisitionScheduleTO();
		reqShecdule.setCalendarId(38);
		reqShecdule.setCreateSystemUserId("QAT2615");
		reqShecdule.setLastUpdateSystemUserId("QAT2615");
		reqShecdule.setHumanResourcesSystemStoreNumber("0121");
		reqShecdule.setInterviewerAvailabilityCount(Short.parseShort("1"));
		reqShecdule.setBeginTimestamp(new Timestamp(new java.util.Date().getTime()));
		reqShecdule.setCreateTimestamp(new Timestamp(new java.util.Date().getTime()));
		reqShecdule.setLastUpdateTimestamp(new Timestamp(new java.util.Date().getTime()));
		reqShecduleList.add(reqShecdule);
		
		StoreDetailResponse storeDetailsResponse = new StoreDetailResponse();
		StoreDetailsTO storeDetailsTO = new StoreDetailsTO();
		storeDetailsTO.setStrNum("0403");
		storeDetailsTO.setStrName("LAKE HAVASU CITY");
		storeDetailsTO.setPhone("9287645111");
		storeDetailsTO.setAdd("100 CENTER BLVD");
		storeDetailsTO.setCity("LAKE HAVASU CITY");
		storeDetailsTO.setState("AZ");
		storeDetailsTO.setZip("86404");
		storeDetailsTO.setStrMgr("FIELDS,CONNIE L");
		storeDetailsTO.setDstCode("PAC MTN DESERT REGION");
		storeDetailsTO.setReg("04RA");
		storeDetailsTO.setDiv("WESTERN DIVISION");
		storeDetailsTO.setCountryCode("US");
		storeDetailsTO.setTimeZoneCode("13005");
		storeDetailsResponse.addStore(storeDetailsTO);
		
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
		//PhoneScreenIntrwDetailsTO
		CalendarResponse calendarResponse = new CalendarResponse();
		calendarResponse.setSchedules(reqShecduleList);
		Response response = new Response();
		response.setItiDtRes(iTIDetailResponse);
		response.setStrDtRes(storeDetailsResponse);
		response.setStfDtls(staffingDetailsTO);
		response.setCalendarRes(calendarResponse);
		Response result = fixture.getFormattedInterviewResponse(response);
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
		assertEquals(null, result.getShowQpButton());
		assertEquals(null, result.getNoIntvwReasonRes());
		assertEquals(null, result.getOfferMadeListRes());
		assertEquals(null, result.getOfferResultListRes());
		assertEquals(null, result.getCandinfoDtRes());
		assertEquals(null, result.getInterviewerDtlsRes());
		assertEquals(null, result.getLicenseInfoRes());
		assertEquals(null, result.getStatusListRes());
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
		assertEquals(null, result.getSchPrefChkBxSelectionList());
		assertEquals(null, result.getCurrentDate());*/
	}
	
	@Test
	public void testFormatPhoneNumber() throws Exception{
		RetailStaffingInterviewManager retailStaffingInterviewManager = new RetailStaffingInterviewManager();
		RetailStaffingInterviewManager retailStaffingInterviewManagerSpy = PowerMockito.spy(retailStaffingInterviewManager);
		Response response = PowerMockito.mock(Response.class);
		PowerMockito.when(retailStaffingInterviewManagerSpy,"formatPhoneNumber",response).thenCallRealMethod(); 
		PowerMockito.verifyPrivate(retailStaffingInterviewManagerSpy,Mockito.times(1)).invoke("formatPhoneNumber",response);
	}
	
	@Test
	public void testFormatDateTime() throws Exception{
		RetailStaffingInterviewManager retailStaffingInterviewManager = new RetailStaffingInterviewManager();
		RetailStaffingInterviewManager retailStaffingInterviewManagerSpy = PowerMockito.spy(retailStaffingInterviewManager);
		CalendarResponse response = PowerMockito.mock(CalendarResponse.class);
		PowerMockito.when(retailStaffingInterviewManagerSpy,"formatDateTime",response).thenCallRealMethod(); 
		PowerMockito.verifyPrivate(retailStaffingInterviewManagerSpy,Mockito.times(1)).invoke("formatDateTime",response);
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(RetailStaffingInterviewManagerTest.class);
	}
}
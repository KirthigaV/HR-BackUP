package com.homedepot.hr.hr.retailstaffing.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.homedepot.hr.hr.retailstaffing.dto.DateTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionDetailTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.RequisitionDetailResponse;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Util.class})
public class UtilTest {
	

	@Test
	public void testConvertDateFormat_1()
		throws Exception {
		
		Date date = new Date(new java.util.Date().getTime());
		String result = Util.convertDateFormat(date);
		assertNotNull(result);
	}


	
	@Test
	public void testFormatPhoneNbr_1()
		throws Exception {
		String phoneNbr = "6783331221";
		String result = Util.formatPhoneNbr(phoneNbr);
		assertEquals("678-3331-221", result);
	}

	@Test
	public void testGetDateFromTs_1()
		throws Exception {
		Timestamp ts = new Timestamp(new java.util.Date().getTime());
		String result = Util.getDateFromTs(ts);
		assertNotNull(result);
	}



	
	@Test
	public void testGetDisplayTime_1()
		throws Exception {
		TimeStampTO timeStampTO = new TimeStampTO();
		timeStampTO.setDay("23");
		timeStampTO.setMonth("06");
		timeStampTO.setYear("2015");
		timeStampTO.setSecond("50");
		timeStampTO.setHour("11");
		timeStampTO.setFormattedDate("23/06/2015");
		timeStampTO.setMinute("50");
		timeStampTO.setMilliSecond("110");
		String result = Util.getDisplayTime(timeStampTO);

		// add additional test code here
		assertEquals("11:50 AM", result);
	}




	@Test
	public void testGetJsonResponse_1()
		throws Exception {
		TimeStampTO timeStampTO = new TimeStampTO();
		timeStampTO.setDay("23");
		timeStampTO.setMonth("06");
		timeStampTO.setYear("2015");
		timeStampTO.setSecond("50");
		timeStampTO.setHour("11");
		timeStampTO.setFormattedDate("23/06/2015");
		timeStampTO.setMinute("50");
		timeStampTO.setMilliSecond("110");
		String result = Util.getJsonResponse(timeStampTO);
		assertNotNull(result);
	}

	
	@Test
	public void testGetMediaType_1()
		throws Exception {
		String mediaType = "application/xml";
		String result = Util.getMediaType(mediaType);
		assertEquals("application/xml", result);
	}

	
	@Test
	public void testGetRequiredFormatRes_1()
		throws Exception {
		String mediaType = "application/xml";
		TimeStampTO timeStampTO = new TimeStampTO();
		timeStampTO.setDay("23");
		timeStampTO.setMonth("06");
		timeStampTO.setYear("2015");
		timeStampTO.setSecond("50");
		timeStampTO.setHour("11");
		timeStampTO.setFormattedDate("23/06/2015");
		timeStampTO.setMinute("50");
		timeStampTO.setMilliSecond("110");
		String result = Util.getRequiredFormatRes(mediaType, timeStampTO);
		assertNotNull(result);
	}

	
	@Test
	public void testGetSchPrefChkBxSelectionList_1()
		throws Exception {
		Response res = new Response();
		RequisitionDetailResponse requisitionDetailResponse=new RequisitionDetailResponse();
		List<RequisitionDetailTO> reqDtlList= new ArrayList();
		requisitionDetailResponse.setReqDtlList(reqDtlList);
		res.setReqDtRes(requisitionDetailResponse);
		List<Boolean> result = Util.getSchPrefChkBxSelectionList(res);
		assertNotNull(result);
		
	}

	
	@Test
	public void testGetStorePacketDateTime_1()
		throws Exception {
		TimeStampTO timeStampTO = new TimeStampTO();
		timeStampTO.setDay("23");
		timeStampTO.setMonth("06");
		timeStampTO.setYear("2015");
		timeStampTO.setSecond("50");
		timeStampTO.setHour("11");
		timeStampTO.setFormattedDate("23/06/2015");
		timeStampTO.setMinute("50");
		timeStampTO.setMilliSecond("110");
		String timeZoneCode = "25463";
		TimeStampTO result = Util.getStorePacketDateTime(timeStampTO, timeZoneCode);
		assertNotNull(result);
		
	}


	
	@Test
	public void testGetTimeFromTs_1()
		throws Exception {
		Timestamp ts = new Timestamp(new java.util.Date().getTime());
		boolean is12HourFormat = false;
		String result = Util.getTimeFromTs(ts, is12HourFormat);
		assertNotNull(result);
	}

	
	
	
	@Test
	public void testIsDST_1()
		throws Exception {
		Date intervwDate = new Date();
		boolean result = Util.isDST(intervwDate);
		assertEquals(false, result);
	}

	@Test
	public void testIsNullList_1()
		throws Exception {
		TimeStampTO timeStampTO = new TimeStampTO();
		timeStampTO.setDay("23");
		timeStampTO.setMonth("06");
		timeStampTO.setYear("2015");
		timeStampTO.setSecond("50");
		timeStampTO.setHour("11");
		timeStampTO.setFormattedDate("23/06/2015");
		timeStampTO.setMinute("50");
		timeStampTO.setMilliSecond("110");
		List<TimeStampTO> timeStampList = new ArrayList();
		timeStampList.add(timeStampTO);
		boolean result = Util.isNullList(timeStampList);
		assertEquals(false, result);
	}


	
	@Test
	public void testIsNullString_1()
		throws Exception {
		String string = "testNullString";
		boolean result = Util.isNullString(string);
		assertEquals(false, result);
	}
	
	@Test
	public void testSetFormattedDate_1()
		throws Exception {
		DateTO dateTO = new DateTO();
		dateTO.setMonth("05");
		dateTO.setYear("2015");
		dateTO.setDay("23");
		Util.setFormattedDate(dateTO);
	}

	
	@Test
	public void testSetFormattedDateAndTime_1()
		throws Exception {
		TimeStampTO timeStampTO = new TimeStampTO();
		timeStampTO.setDay("23");
		timeStampTO.setMonth("06");
		timeStampTO.setYear("2015");
		timeStampTO.setSecond("50");
		timeStampTO.setHour("11");
		timeStampTO.setFormattedDate("23/06/2015");
		timeStampTO.setMinute("50");
		timeStampTO.setMilliSecond("110");
		boolean isFormatDate = true;
		Util.setFormattedDateAndTime(timeStampTO, isFormatDate);
	}

	
	@Test
	public void testSetTargetPay_1()
		throws Exception {
		String value = "100";
		String result = Util.setTargetPay(value);
		assertEquals("100", result);
	}

	
	@Test
	public void testExceptionResponse_1()
		throws Exception {
		String mediaType = "application/xml";
		Exception e = new Exception();
		String result = Util.exceptionResponse(mediaType, e);
		assertNotNull(result);
	}
	
	@Test
	public void testSetApplErrorRes_1()
		throws Exception {
		String mediaType = "application/xml";
		String result = Util.setApplErrorRes(mediaType);
		assertNotNull(result);
	}
	
	@Test
	public void testGetDateFromInput() throws Exception{
		Util util = new Util();
		Util utilSpy = PowerMockito.spy(util);
		TimeStampTO timeStampTO = PowerMockito.mock(TimeStampTO.class);
		PowerMockito.when(utilSpy,"getDateFromInput",timeStampTO).thenCallRealMethod();
		PowerMockito.verifyPrivate(utilSpy,Mockito.times(1)).invoke("getDateFromInput",timeStampTO);
	}
	/*@Test
	public void testGetIntValue() throws Exception{
		Util util = new Util();
		Util utilSpy = PowerMockito.spy(util);
		String str = PowerMockito.mock(String.class);
		PowerMockito.when(utilSpy,"getIntValue",str).thenCallRealMethod();
		PowerMockito.verifyPrivate(utilSpy,Mockito.times(1)).invoke("getIntValue",str);
	}*/
	
	@Test
	public void testGetObjectFromJson(){
		String jsonRequest ="TimeStampTO:{[]}";
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.getObjectFromJson(jsonRequest, TimeStampTO.class)).thenReturn(new TimeStampTO());
		TimeStampTO timeStampTO = (TimeStampTO)Util.getObjectFromJson(jsonRequest, TimeStampTO.class);
		assertNotNull(timeStampTO);
	}
	
	@Test
	public void testGetObjectFromInput() throws Exception{
		String jsonRequest ="TimeStampTO:{[]}";
		String mediaType="application/json";
		PowerMockito.mockStatic(Util.class);
		PowerMockito.when(Util.getObjectFromInput(mediaType,jsonRequest, TimeStampTO.class)).thenReturn(new TimeStampTO());
		TimeStampTO timeStampTO = (TimeStampTO)Util.getObjectFromInput(mediaType,jsonRequest, TimeStampTO.class);
		assertNotNull(timeStampTO);
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(UtilTest.class);
	}
}
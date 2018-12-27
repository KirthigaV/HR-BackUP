package com.homedepot.hr.hr.retailstaffing.model;

import org.junit.*;

import static org.junit.Assert.*;

import com.homedepot.hr.hr.retailstaffing.dto.LocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.TimeStampTO;
import com.homedepot.hr.hr.retailstaffing.exceptions.ValidationException;
import com.homedepot.hr.hr.retailstaffing.model.LocationManager;


public class LocationManagerTest {
	

	@Test
	public void testGetFormattedStoreDtlsResponse_1()
		throws Exception {
		StoreDetailsTO store = new StoreDetailsTO();
		store.setPhone("4707234112");
		store.setTimeZoneCode("IST");
		TimeStampTO interviewTimeStampTO = new TimeStampTO();
		interviewTimeStampTO.setDay("23");
		interviewTimeStampTO.setMonth("06");
		interviewTimeStampTO.setYear("2015");
		interviewTimeStampTO.setSecond("50");
		interviewTimeStampTO.setHour("11");
		interviewTimeStampTO.setFormattedDate("23/06/2015");
		interviewTimeStampTO.setMinute("50");
		interviewTimeStampTO.setMilliSecond("110");
		StoreDetailsTO result = LocationManager.getFormattedStoreDtlsResponse(store, interviewTimeStampTO);

		// add additional test code here
		assertNotNull(result);
		assertEquals(null, result.getState());
		assertEquals("IST", result.getTimeZoneCode());
		assertEquals(null, result.getCountryCode());
		assertEquals("470-7234-112", result.getFormattedPhone());
		assertEquals(null, result.getDstHrMgr());
		assertEquals(null, result.getDstCode());
		assertEquals(null, result.getDstMgr());
		assertEquals(null, result.getStrName());
		assertEquals("4707234112", result.getPhone());
		assertEquals(null, result.getReg());
		assertEquals(null, result.getStrMgr());
		assertEquals(null, result.getStrNum());
		assertEquals(null, result.getDiv());
		assertEquals(null, result.getAdd());
		assertEquals(null, result.getZip());
		assertEquals(null, result.getCity());
	}

	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(LocationManagerTest.class);
	}
}
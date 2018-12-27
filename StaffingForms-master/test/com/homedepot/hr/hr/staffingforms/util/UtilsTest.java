package com.homedepot.hr.hr.staffingforms.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.homedepot.hr.hr.staffingforms.dto.DaySummary;

import static org.junit.Assert.*;

@PrepareForTest({Utils.class})
public class UtilsTest {
	
	@Test
	public void testConvertDateFormat()
		throws Exception {
		SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
        java.util.Date parsed = format.parse("06162015");
        Date date = new Date(parsed.getTime());
		String result = Utils.convertDateFormat(date);
		// add additional test code here
		assertEquals("06/16/2015", result);
	}

	
	@Test
	public void testGetJsonResponse()
		throws Exception {
		DaySummary daySummary = new DaySummary();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date parsed = format.parse("20150616");
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        daySummary.setDate(sql);
        daySummary.setFormattedDate("06/16/2015");
        daySummary.setDayOfWeekIndicator(3);
		String result = Utils.getJsonResponse(daySummary);
		assertNotNull(result);
	}

	
	@Test
	public void testGetMediaType_1()
		throws Exception {
		String mediaType = "application/xml";
		String result = Utils.getMediaType(mediaType);
		assertEquals("application/xml", result);
	}

	
	@Test
	public void testGetRequiredFormatRes_1()
		throws Exception {
		String mediaType = "application/xml";
		DaySummary daySummary = new DaySummary();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date parsed = format.parse("20150616");
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        daySummary.setDate(sql);
        daySummary.setFormattedDate("06/16/2015");
        daySummary.setDayOfWeekIndicator(3);
		String result = Utils.getRequiredFormatRes(mediaType, daySummary);
		assertNotNull(result);
	}

	
	@Test
	public void testIsNullList_1()
		throws Exception {
		List<DaySummary> daySummaryList = new ArrayList();
		DaySummary daySummary = new DaySummary();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date parsed = format.parse("20150616");
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        daySummary.setDate(sql);
        daySummary.setFormattedDate("06/16/2015");
        daySummary.setDayOfWeekIndicator(3);
        daySummaryList.add(daySummary);
		boolean result = Utils.isNullList(daySummaryList);
		assertEquals(false, result);
	}

	@Test
	public void testIsNullString_1()
		throws Exception {
		String string = null;
		boolean result = Utils.isNullString(string);
		assertEquals(true, result);
	}

	@Test
	public void testGetObjectFromJson() throws Exception{
		//{"[daySummary":{"date":"2015-06-16","formattedDate":"06\/16\/2015","dayOfWeekIndicator":3}]
		String jsonRequest="{daySummary:{}}";
		Object object = Utils.getObjectFromJson(jsonRequest, DaySummary.class);
		DaySummary daySummaryObj=(DaySummary)object;
		assertNotNull(daySummaryObj);
	}
	
	@Test
	public void testGetObjectFromInput() throws Exception{
		String mediaType="application/xml";
		String request="<daySummary></daySummary>";
		Object object = Utils.getObjectFromInput(mediaType,request, DaySummary.class);
		DaySummary daySummaryObj=(DaySummary)object;
		assertNotNull(daySummaryObj);
	}
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(UtilsTest.class);
	}
}
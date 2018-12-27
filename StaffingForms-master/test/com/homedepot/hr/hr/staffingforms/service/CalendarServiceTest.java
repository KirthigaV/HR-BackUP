package com.homedepot.hr.hr.staffingforms.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.homedepot.hr.hr.staffingforms.bl.CalendarManager;
import com.homedepot.hr.hr.staffingforms.dto.DaySummary;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.service.CalendarService;
import com.homedepot.ta.aa.dao.basic.BasicDAO;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CalendarService.class,CalendarManager.class,BasicDAO.class})
public class CalendarServiceTest {
	
	@Test
	public void testGetCalendarDetails_1()
		throws Exception {
		PowerMockito.mockStatic(CalendarManager.class);
		List<RequisitionSchedule> requisitionScheduleList=new ArrayList();
		RequisitionSchedule reqShecdule=new RequisitionSchedule();
		reqShecdule.setCalendarId(38);
		reqShecdule.setCrtSysUsrId("QAT2615");
		reqShecdule.setLastUpdSysUsrId("QAT2615");
		reqShecdule.setStrNbr("0121");
		reqShecdule.setSeqNbr(Short.parseShort("1"));
		reqShecdule.setReqnSchStatCd(Short.parseShort("1"));
		reqShecdule.setBgnTs(new Timestamp(new java.util.Date().getTime()));
		reqShecdule.setCrtTs(new Timestamp(new java.util.Date().getTime()));
		reqShecdule.setLastUpdTs(new Timestamp(new java.util.Date().getTime()));
		
		requisitionScheduleList.add(reqShecdule);
		final int cId=38;
		PowerMockito.when(CalendarManager.getCalendarDetailsForDate(cId,new java.sql.Date(new java.util.Date().getTime()))).thenReturn(requisitionScheduleList);
		
		CalendarService fixture = new CalendarService();
		int calendarId = 38;
		Date date = new Date(new java.util.Date().getTime());
		int version = 1;
		String contentType = "application/xml";
		String result = fixture.getCalendarDetails(calendarId, date, version, contentType);
		assertNotNull(result);
	}

	
	@Test
	public void testGetCalendarSummary_1()
		throws Exception {
		
		PowerMockito.mockStatic(CalendarManager.class);
		List<DaySummary> daySummaryList = new ArrayList();
		DaySummary daySummary = new DaySummary();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Date parsed = format.parse("20150616");
        java.sql.Date sql = new java.sql.Date(parsed.getTime());
        daySummary.setDate(sql);
        daySummary.setFormattedDate("06/16/2015");
        daySummary.setDayOfWeekIndicator(3);
        daySummaryList.add(daySummary);
        PowerMockito.when(CalendarManager.getCalendarSummaryForDateRange(38,new java.sql.Date(new java.util.Date().getTime()),new java.sql.Date(new java.util.Date().getTime()))).thenReturn(daySummaryList);
		
		CalendarService fixture = new CalendarService();
		int calendarId = 38;
		Date beginDate = new Date(System.currentTimeMillis()-24*60*60*1000);
		Date endDate = new Date(new java.util.Date().getTime());
		int version = 1;
		String contentType = "application/xml";
		String result = fixture.getCalendarSummary(calendarId, beginDate, endDate, version, contentType);
		assertNotNull(result);
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(CalendarServiceTest.class);
	}
}
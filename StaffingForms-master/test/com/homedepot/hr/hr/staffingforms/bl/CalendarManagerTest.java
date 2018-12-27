package com.homedepot.hr.hr.staffingforms.bl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.homedepot.hr.hr.staffingforms.dto.DaySummary;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionScheduleSlotDtl;
import com.homedepot.hr.hr.staffingforms.dto.StatusSlotCountDtl;
import com.homedepot.hr.hr.staffingforms.dto.StatusSlotSummary;


@PrepareForTest({CalendarManager.class})
public class CalendarManagerTest {
	
	@Test
	public void testGetFormattedCalendarResponse()
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
		List<DaySummary> result = CalendarManager.getFormattedCalendarResponse(daySummaryList);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	@Test
	public void testGetReqSchSlotDtls_1()
		throws Exception {
		List<RequisitionSchedule> reqSchList = new ArrayList();
		RequisitionSchedule reqShecdule = new RequisitionSchedule();
		reqShecdule.setCalendarId(38);
		reqShecdule.setCrtSysUsrId("QAT2615");
		reqShecdule.setLastUpdSysUsrId("QAT2615");
		reqShecdule.setStrNbr("0121");
		reqShecdule.setSeqNbr(Short.parseShort("1"));
		reqShecdule.setReqnSchStatCd(Short.parseShort("1"));
		reqShecdule.setBgnTs(new Timestamp(new java.util.Date().getTime()));
		reqShecdule.setCrtTs(new Timestamp(new java.util.Date().getTime()));
		reqShecdule.setLastUpdTs(new Timestamp(new java.util.Date().getTime()));
		reqSchList.add(reqShecdule);
		List<RequisitionScheduleSlotDtl> result = CalendarManager.getReqSchSlotDtls(reqSchList);
		assertNotNull(result);
		assertEquals(34, result.size());
	}

	

	@Test
	public void testSetSlotCount_1()
		throws Exception {
		StatusSlotSummary statusSlotSummary = new StatusSlotSummary((short) 3, 1);
		StatusSlotCountDtl statusSlotCountDtl = new StatusSlotCountDtl();
		statusSlotCountDtl.setAvailabeCount(1);
		statusSlotCountDtl.setBookedCount(1);
		statusSlotCountDtl.setReservedCount(1);
		CalendarManager.setSlotCount(statusSlotSummary, statusSlotCountDtl);
	}
	
	@Test
	public void testSetSlotCountList_1()
		throws Exception {
		DaySummary daySummary = new DaySummary();
		List<StatusSlotSummary> statusSlotSummaryList= new ArrayList();
		StatusSlotSummary statusSlotSummary = new StatusSlotSummary((short) 3, 1);
		statusSlotSummaryList.add(statusSlotSummary);
		daySummary.setSlotSummaries(statusSlotSummaryList);
		CalendarManager.setSlotCountList(daySummary);

	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(CalendarManagerTest.class);
	}
}
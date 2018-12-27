package com.homedepot.hr.hr.retailstaffing.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.homedepot.hr.hr.retailstaffing.dto.RequisitionCalendarTO;
import com.homedepot.hr.hr.retailstaffing.model.ScheduleManager;
import com.homedepot.hr.hr.retailstaffing.service.InterviewService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({InterviewService.class,ScheduleManager.class})
public class InterviewServiceTest {

	
	@Test
	public void testGetRequisitionCalendars_1()
		throws Exception {
		InterviewService fixture = new InterviewService();
		String storeNumber = "0121";
		int version = 1;
		String contentType = "application/xml";
		List<RequisitionCalendarTO> calendars = new ArrayList();
		PowerMockito.mockStatic(ScheduleManager.class);
		PowerMockito.when(ScheduleManager.getRequisitionCalendarsForStore(storeNumber, false)).thenReturn(calendars);
		String result = fixture.getRequisitionCalendars(storeNumber, version, contentType);
		assertNotNull(result);
	}
	
	@Test
	public void testGetRequisitionCalendars_2()
		throws Exception {
		InterviewService fixture = new InterviewService();
		String storeNumber = "0121";
		int version = 2;
		String contentType = "application/xml";
		List<RequisitionCalendarTO> calendars = new ArrayList();
		PowerMockito.mockStatic(ScheduleManager.class);
		PowerMockito.when(ScheduleManager.getRequisitionCalendarsAndCountsForStore(storeNumber)).thenReturn(calendars);
		String result = fixture.getRequisitionCalendars(storeNumber, version, contentType);
		assertNotNull(result);
	}
	
	
	@Test
	public void testGetRequisitionHiringEvents_1()
		throws Exception {
		InterviewService fixture = new InterviewService();
		String storeNumber = "0121";
		int version = 1;
		String contentType = "application/xml";
		List<RequisitionCalendarTO> hiringEvents = new ArrayList();
		PowerMockito.mockStatic(ScheduleManager.class);
		PowerMockito.when(ScheduleManager.getRequisitionHiringEventsForStore(storeNumber)).thenReturn(hiringEvents);
		String result = fixture.getRequisitionHiringEvents(storeNumber, version, contentType);
		assertNotNull(result);
	}
	
	@Test
	public void testGetRequisitionHiringEvents_2()
		throws Exception {
		InterviewService fixture = new InterviewService();
		String storeNumber = "0121";
		int version = 2;
		String contentType = "application/xml";
		List<RequisitionCalendarTO> hiringEvents = new ArrayList();
		PowerMockito.mockStatic(ScheduleManager.class);
		PowerMockito.when(ScheduleManager.getRequisitionHiringEventsAndCountsForStore(storeNumber)).thenReturn(hiringEvents);
		String result = fixture.getRequisitionHiringEvents(storeNumber, version, contentType);
		assertNotNull(result);
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(InterviewServiceTest.class);
	}
}
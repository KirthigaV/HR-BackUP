package com.homedepot.hr.hr.retailstaffing.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;











import com.homedepot.hr.hr.retailstaffing.dto.StoreDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.response.Response;
import com.homedepot.hr.hr.retailstaffing.model.LocationManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingITIManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingInterviewManager;
import com.homedepot.hr.hr.retailstaffing.model.RetailStaffingRequisitionManager;
import com.homedepot.hr.hr.retailstaffing.service.RetailStaffingService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RetailStaffingService.class,LocationManager.class,RetailStaffingRequisitionManager.class,RetailStaffingInterviewManager.class,RetailStaffingITIManager.class})
public class RetailStaffingServiceTest {



	@Test
	public void testGetInterviewDetails_1()
		throws Exception {
		RetailStaffingService fixture = new RetailStaffingService();
		String itiNbr = "12345";
		String contentType = "application/xml";
		RetailStaffingInterviewManager retailStaffingInterviewManager=PowerMockito.mock(RetailStaffingInterviewManager.class);
		PowerMockito.whenNew(RetailStaffingInterviewManager.class).withNoArguments().thenReturn(retailStaffingInterviewManager);
		Response res = new Response();
		PowerMockito.when(retailStaffingInterviewManager.getInterviewDetails(itiNbr)).thenReturn(res);
		String result = fixture.getInterviewDetails(itiNbr, contentType);
		assertNotNull(result);
	}

	
	@Test
	public void testGetPhoneScreenDetails_1()
		throws Exception {
		RetailStaffingService fixture = new RetailStaffingService();
		String itiNbrStatus = "STATUS";
		String contentType = "application/xml";
		RetailStaffingITIManager retailStaffingITIManager=PowerMockito.mock(RetailStaffingITIManager.class);
		PowerMockito.whenNew(RetailStaffingITIManager.class).withNoArguments().thenReturn(retailStaffingITIManager);
		Response res = new Response();
		PowerMockito.when(retailStaffingITIManager.getPhoneScreenIntrwDtls(itiNbrStatus)).thenReturn(res);
		String result = fixture.getPhoneScreenDetails(itiNbrStatus, contentType);
		assertNotNull(result);
	}

	
	
	@Test
	public void testGetRequisitionDetails_1()
		throws Exception {
		RetailStaffingService fixture = new RetailStaffingService();
		String reqNbr = "12345";
		String contentType = "application/xml";
		RetailStaffingRequisitionManager retailStaffingRequisitionManager=PowerMockito.mock(RetailStaffingRequisitionManager.class);
		PowerMockito.whenNew(RetailStaffingRequisitionManager.class).withNoArguments().thenReturn(retailStaffingRequisitionManager);
		Response res = new Response();
		PowerMockito.when(retailStaffingRequisitionManager.getRequisitionDetails(reqNbr)).thenReturn(res);
		String result = fixture.getRequisitionDetails(reqNbr, contentType);
		assertNotNull(result);
	}

	
	@Test
	public void testGetStoreDetails_1()
		throws Exception {
		RetailStaffingService fixture = new RetailStaffingService();
		String strNbr = "0121";
		String interviewDate = "";
		String contentType = "application/xml";
		StoreDetailsTO storeDetailsTO= new StoreDetailsTO();
		PowerMockito.mockStatic(LocationManager.class);
		PowerMockito.when(LocationManager.getStoreDetails(strNbr)).thenReturn(storeDetailsTO);
		String result = fixture.getStoreDetails(strNbr, interviewDate, contentType);
		assertNotNull(result);
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(RetailStaffingServiceTest.class);
	}
}
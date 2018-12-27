package com.homedepot.hr.hr.retailstaffing.service;

import static org.junit.Assert.assertNotNull;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import com.homedepot.hr.hr.retailstaffing.dto.response.UserProfileResponseDTO;
import com.homedepot.hr.hr.retailstaffing.service.UserProfileService;


public class UserProfileServiceTest {
	
	@Test
	public void testUserProfileService_1()
		throws Exception {
		UserProfileService result = new UserProfileService();
		assertNotNull(result);
		
	}

	
	@Test
	public void testGetUserProfileDetails_1()
		throws Exception {
		int version = 1;
		String mediaType = "application/xml";
		UserProfileService userProfileService = PowerMockito.mock(UserProfileService.class);
		HttpServletRequest request = PowerMockito.mock(HttpServletRequest.class);
		String roles = "admin";
		
		UserProfileResponseDTO dto = new UserProfileResponseDTO();
		dto.setUserId("QAT2715");
		dto.setValidRole(false);
		dto.setFirstName("TESTER");
		dto.setLastName("QATESTER");
		dto.setStatus("SUCCESS");
		dto.setErrorCd(0);
		PowerMockito.when(userProfileService.getUserProfileDetails(request, roles, version, mediaType)).thenReturn(dto.toString());
		String result = userProfileService.getUserProfileDetails(request, roles, version, mediaType);
		assertNotNull(result);
	}

	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(UserProfileServiceTest.class);
	}
}
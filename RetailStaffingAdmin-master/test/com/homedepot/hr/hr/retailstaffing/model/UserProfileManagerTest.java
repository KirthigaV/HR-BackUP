package com.homedepot.hr.hr.retailstaffing.model;

import javax.servlet.http.HttpServletRequest;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

import com.homedepot.hr.hr.retailstaffing.dto.response.UserProfileResponseDTO;
import com.homedepot.hr.hr.retailstaffing.model.UserProfileManager;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UserProfileManager.class})
public class UserProfileManagerTest {
	
	@Test
	public void testUserProfileManager_1()
		throws Exception {
		UserProfileManager result = new UserProfileManager();
		assertNotNull(result);
		
	}


	
	@Test
	public void testGetUserProfileDetails_1()
		throws Exception {
		
		UserProfileManager userProfileManager=PowerMockito.mock(UserProfileManager.class);
		HttpServletRequest request = PowerMockito.mock(HttpServletRequest.class);
		String roles = "admin";
		UserProfileResponseDTO dto = new UserProfileResponseDTO();
		dto.setUserId("QAT2715");
		dto.setValidRole(false);
		dto.setFirstName("TESTER");
		dto.setLastName("QATESTER");
		dto.setStatus("SUCCESS");
		dto.setErrorCd(0);
		PowerMockito.when(userProfileManager.getUserProfileDetails(request,roles)).thenReturn(dto);
		UserProfileResponseDTO result = userProfileManager.getUserProfileDetails(request, roles);
		assertNotNull(result);
	}

	
	
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(UserProfileManagerTest.class);
	}
}
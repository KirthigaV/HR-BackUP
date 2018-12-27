package com.homedepot.hr.hr.retailstaffing.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

import com.homedepot.hr.ae.dao.crude.oracle.ats.AtsCandidateCallPomResult;
import com.homedepot.hr.hr.retailstaffing.dao.handlers.CandidateDataFormHandler;
import com.homedepot.hr.hr.retailstaffing.dao.handlers.PhoneScreenHandler;
import com.homedepot.hr.hr.retailstaffing.dto.IntrwLocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.MinimumResponseTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenNoteTO;
import com.homedepot.hr.hr.retailstaffing.dto.RequisitionScheduleTO;
import com.homedepot.hr.hr.retailstaffing.dto.request.HrRetlStffReconsialtionRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.InsertPhoneScreenRequest;
import com.homedepot.hr.hr.retailstaffing.dto.request.POMRsaStatusCrossRefRequest;
import com.homedepot.hr.hr.retailstaffing.dto.response.POMRsaStatusCrossRefResponse;
import com.homedepot.hr.hr.retailstaffing.model.PhoneScreenManager;
import com.homedepot.ta.aa.dao.BusinessUse;
import com.homedepot.ta.aa.dao.Contract;
import com.homedepot.ta.aa.dao.DAOConnection;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Priority;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.QueryHandler;
import com.homedepot.ta.aa.dao.QueryManager;
import com.homedepot.ta.aa.dao.basic.BasicContract;
import com.homedepot.ta.aa.dao.basic.BasicDAOConnection;
import com.homedepot.ta.aa.dao.basic.BasicPriority;
import com.homedepot.ta.aa.dao.basic.BasicQueryManager;
import com.homedepot.ta.aa.dao.stream.DelegateInputs;


public class PhoneScreenManagerTest {

	@Test
	public void testSetpomRsaStatCrsRefResp_1()
		throws Exception {
		POMRsaStatusCrossRefResponse pomRsaStatCrsRefResp = new POMRsaStatusCrossRefResponse();
		pomRsaStatCrsRefResp.setPomCompleteStatusCode((short) 1);
		pomRsaStatCrsRefResp.setRsaUpdateFlag(true);
		pomRsaStatCrsRefResp.setInterviewRespondStatusCode((short) 1);
		String mediaType = "application/xml";
		String result = PhoneScreenManager.setpomRsaStatCrsRefResp(pomRsaStatCrsRefResp, mediaType);
		assertNotNull(result);
		
	}

	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(PhoneScreenManagerTest.class);
	}
}
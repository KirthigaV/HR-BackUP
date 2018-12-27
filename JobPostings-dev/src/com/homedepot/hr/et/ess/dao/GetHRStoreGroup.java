package com.homedepot.hr.et.ess.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.et.ess.util.JPConstants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class GetHRStoreGroup {

	private static final Logger logger = Logger.getLogger( GetHRStoreGroup.class );

	public List<String> readHumanResourcesStoreGrp(String storeID)	throws QueryException {
		
		final String METHODNAME = "readHumanResourcesStoreGrp";
		logger.debug("Entering inside:"+METHODNAME);
		final List<String> readHumanResourcesStoreGrpList = new ArrayList<String>();

		MapStream inputs = new MapStream(JPConstants.readHumanResourcesStoreGrp);
		logger.debug(METHODNAME+";storeID:"+storeID);
		inputs.put(JPConstants.humanResourcesSystemStoreNumber, storeID);
		inputs.put(JPConstants.baseStoreGroupFlag, "Y");
		inputs.put(JPConstants.humanResourcesSystemCountryCode, "USA");
		inputs.put(JPConstants.date, new Date(System.currentTimeMillis()));
		List<Object> inputLocationTypeCodeList = new ArrayList<Object>();
		inputLocationTypeCodeList.add("STR");
		inputLocationTypeCodeList.add("DC");
		inputs.put(JPConstants.locationTypeCodeList, inputLocationTypeCodeList);

		//TODO: Verify contract name, business use id below
		BasicDAO.getResult(JPConstants.HRETJOBPOSTING_NAME, JPConstants.BUSINESS_USE_ID,
				JPConstants.HRETJOBPOSTING_VERSION, inputs, new ResultsReader() {
					public void readResults(Results results, Query query,
							Inputs inputs) throws QueryException {
						
						while (results.next()) {
							logger.debug(METHODNAME+";Found Stores Count:"+results.getString("count"));
							readHumanResourcesStoreGrpList.add(results.getString("count"));
						}
					}
				});
		logger.debug("Exit:"+METHODNAME);
		return readHumanResourcesStoreGrpList;
	}

}

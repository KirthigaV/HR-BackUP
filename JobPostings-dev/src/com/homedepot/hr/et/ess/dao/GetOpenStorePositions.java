package com.homedepot.hr.et.ess.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.et.ess.bean.HourlyPostingsDTO;
import com.homedepot.hr.et.ess.bean.ReadOpenStorePositionsDTO;
import com.homedepot.hr.et.ess.util.JPConstants;
import com.homedepot.ta.aa.dao.Inputs;
import com.homedepot.ta.aa.dao.Query;
import com.homedepot.ta.aa.dao.Results;
import com.homedepot.ta.aa.dao.ResultsReader;
import com.homedepot.ta.aa.dao.basic.BasicDAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;
import com.homedepot.ta.aa.dao.stream.MapStream;

public class GetOpenStorePositions {

	private static final Logger logger = Logger.getLogger( GetOpenStorePositions.class );

	public List<ReadOpenStorePositionsDTO> readOpenStorePositions(HourlyPostingsDTO hourlyPostingsDTO)
			throws QueryException {
		
		final String METHODNAME = "readOpenStorePositions";
		logger.debug("Entering inside:"+METHODNAME);
		final List<ReadOpenStorePositionsDTO> readOpenStorePositionsList = new ArrayList<ReadOpenStorePositionsDTO>();

		MapStream inputs = new MapStream(JPConstants.readOpenStorePositions);
		//TODO: Replace values below
		inputs.put(JPConstants.humanResourcesSystemStoreNumber, hourlyPostingsDTO.getStoreIdValue());
		inputs.put(JPConstants.consentDecreeJobCategoryCodeList,hourlyPostingsDTO.getConsentDecreeJobCategoryCodeList());
		inputs.put(JPConstants.activeFlag, hourlyPostingsDTO.getActiveFlag());
		inputs.put(JPConstants.languageCode, hourlyPostingsDTO.getStoreLocale());
		inputs.put(JPConstants.date, hourlyPostingsDTO.getEndDate());

		//TODO: Verify contract name, business use id below
		BasicDAO.getResult(JPConstants.HRETJOBPOSTING_NAME, JPConstants.BUSINESS_USE_ID,JPConstants.HRETJOBPOSTING_VERSION, inputs, new ResultsReader() {
			
		public void readResults(Results results, Query query,Inputs inputs) throws QueryException {
			ReadOpenStorePositionsDTO readOpenStorePositionsDTO = null;
			
			while (results.next()) {
				readOpenStorePositionsDTO = new ReadOpenStorePositionsDTO();
				readOpenStorePositionsDTO.setHumanResourcesSystemDepartmentNumber(results.getString(JPConstants.humanResourcesSystemDepartmentNumber));
				readOpenStorePositionsDTO.setJobTitleDescription(results.getString(JPConstants.jobTitleDescription));
				readOpenStorePositionsDTO.setOpenPositionCount(results.getShort(JPConstants.openPositionCount));
				readOpenStorePositionsDTO.setFullTimeRequiredFlag(results.getString(JPConstants.fullTimeRequiredFlag));
				readOpenStorePositionsDTO.setPartTimeRequiredFlag(results.getString(JPConstants.partTimeRequiredFlag));
				readOpenStorePositionsList.add(readOpenStorePositionsDTO);
			}
		}
	});
		logger.debug(METHODNAME+";Number of jobpostings: "+readOpenStorePositionsList.size());
		logger.debug("Entering inside:"+METHODNAME);
		return readOpenStorePositionsList;
	}

}

package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.homedepot.hr.hr.retailstaffing.util.DAODateConverterToDateTO;
import com.homedepot.hr.hr.retailstaffing.util.DB2Trimmer;
import com.homedepot.ta.aa.dao.builder.BooleanStringFlagProcessor;
import com.homedepot.ta.aa.dao.builder.DAOConverter;
import com.homedepot.ta.aa.dao.builder.DAOElement;
import com.thoughtworks.xstream.annotations.XStreamAlias;


public class CandidateMinorInfoTO implements Serializable {
	private static final long serialVersionUID = 362498820763181265L;
	
	@DAOElement("DOCSREMAINING")
	@XStreamAlias("parentalConsentFormDocsIncomplete")
	private int parentalConsentFormDocsIncomplete;
	
	@DAOElement("ROWCOUNT")
	@XStreamAlias("parentalConsentFormDocsExistingRows")
	private int parentalConsentFormDocsExistingRows;

	public int getParentalConsentFormDocsIncomplete() {
		return parentalConsentFormDocsIncomplete;
	}

	public void setParentalConsentFormDocsIncomplete(
			int parentalConsentFormDocsIncomplete) {
		this.parentalConsentFormDocsIncomplete = parentalConsentFormDocsIncomplete;
	}

	public int getParentalConsentFormDocsExistingRows() {
		return parentalConsentFormDocsExistingRows;
	}

	public void setParentalConsentFormDocsExistingRows(
			int parentalConsentFormDocsExistingRows) {
		this.parentalConsentFormDocsExistingRows = parentalConsentFormDocsExistingRows;
	}

}

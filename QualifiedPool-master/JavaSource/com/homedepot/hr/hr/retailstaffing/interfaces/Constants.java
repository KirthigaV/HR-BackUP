package com.homedepot.hr.hr.retailstaffing.interfaces;

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: Constants.java
 * Application: RetailStaffing
 */
import com.homedepot.ta.aa.dao.Contract;
import com.homedepot.ta.aa.dao.basic.BasicContract;

/**
 * This class contains constant values that are shared throughout application
 * 
 * @author rlp05
 */
public interface Constants {
	/** total number of nanoseconds in a second */
	public static final double NANOS_IN_SECOND = 1000000000.0d;

	// error constant codes
	public static final int ERRCD_INVALID_VERSION = 101;
	public static final int ERRCD_INVALID_REQUEST = 102;
	public static final int ERRCD_INVALID_REQNBR = 103;
	public static final int ERRCD_INVALID_JOBSKILL = 104;
	public static final int ERRCD_INVALID_STRTYP_CD = 105;
	public static final int ERRCD_INVALID_CNTRY_CD = 106;
	public static final int ERRCD_INVALID_DEPT_NBR = 107;
	public static final int ERRCD_INVALID_JOB_TTL_CD = 108;
	public static final int ERRCD_INVALID_STRNBR = 109;
	public static final int ERRCD_INVALID_USERID = 110;

	public static final int ERRCD_DATA_ACCESS = 1001;

	// DAO constants for Contract HrHrStaffing
	public static final String HRSTAFFING_CONTRACT_NAME = "HrHrStaffing";
	public static final int HRSTAFFING_BUID = 10038;
	public static final int HRSTAFFING_VERSION = 1;

	public static final Contract HRSTAFFING_DAO_CONTRACT = new BasicContract(HRSTAFFING_CONTRACT_NAME, HRSTAFFING_BUID, HRSTAFFING_VERSION);

	// Added for ATS Project MTS1876
	// DAO constants for Contract WorkforceEmploymentQualifications
	public static final String WORKFORCEEMPLOYMENTQUALIFICATIONS_CONTRACT_NAME = "WorkforceEmploymentQualifications";
	public static final int WORKFORCEEMPLOYMENTQUALIFICATIONS_BUID = 55;
	public static final int WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION = 1;	
	
	public static final Contract WORKFORCEEMPLOYMENTQUALIFICATIONS_DAO_CONTRACT = new BasicContract(WORKFORCEEMPLOYMENTQUALIFICATIONS_CONTRACT_NAME, WORKFORCEEMPLOYMENTQUALIFICATIONS_BUID,
			WORKFORCEEMPLOYMENTQUALIFICATIONS_VERSION);
	
	// DAO constants for Contract WorkforceRecruitment
	public static final String WORKFORCERECRUITMENT_CONTRACT_NAME = "WorkforceRecruitment";
	public static final int WORKFORCERECRUITMENT_BUID = 55;
	public static final int WORKFORCERECRUITMENT_VERSION = 1;
	public static final Contract WORKFORCERECRUITMENT_DAO_CONTRACT = new BasicContract(WORKFORCERECRUITMENT_CONTRACT_NAME, WORKFORCERECRUITMENT_BUID, WORKFORCERECRUITMENT_VERSION);		

	//====================
	//DAO 2.0 Data Sources
	//====================
	public static final String DATA_SOURCE_DB2Z_PR1_005 = "java:comp/env/jdbc/DB2Z.PR1.005";
	public static final String DATA_SOURCE_DB2Z_PR1_032 = "java:comp/env/jdbc/DB2Z.PR1.032";
	
} // end interface Constants
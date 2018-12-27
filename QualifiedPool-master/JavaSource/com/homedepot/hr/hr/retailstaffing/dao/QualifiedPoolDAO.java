package com.homedepot.hr.hr.retailstaffing.dao;

/*
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: QualifiedPoolDAO.java
 * Application: RetailStaffing
 */
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.retailstaffing.dto.QualifiedCandidate;
import com.homedepot.hr.hr.retailstaffing.interfaces.Constants;
import com.homedepot.ta.aa.dao.builder.DAO;
import com.homedepot.ta.aa.dao.exceptions.QueryException;

/**
 * This class contains data access methods related to generating a qualified
 * pool of candidates for an employment requisition
 */
public class QualifiedPoolDAO implements Constants {
	// logger instance
	private static final Logger mLogger = Logger.getLogger(QualifiedPoolDAO.class);
	
	// active flag
	private static final String ACTIVE = "A";
	// another active flag
	private static final String YES = "Y";
	private static final String NO = "N";
	// Associate candidate type
	private static final String CAND_TYP_ASSOCIATE = "AS";
	// Applicant candidate type
	private static final String CAND_TYP_APPLICANT = "AP";
	// Country Code
	private static final String USA = "USA";

	// status indicating a background check failed
	private static final short BACKGROUND_CHK_FAILED_STATUS = 1;
	
	private static final List<String> ASSOCIATE_COMPENSATION_ACTION_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -2737014905954678470L;
		{
			add("IR"); 
			add("RR");
			add("MR");
			add("FR");
			add("RM");
		} // end constructor block
	}; // end collection ASSOCIATE_COMPENSATION_ACTION_LIST
	
	private static final int EVAL_CAT_CODE_1 = 1;
	private static final String en_US = "en_US";
	private static final int EVAL_RTG_CODE_27 = 27;
	
	//DAO 2.0 SQL===============
	public static final String SQL_GET_ACTIVE_ASSOCIATE_QP =   "SELECT DISTINCT CX.EMPLT_CAND_ID "
															+ "               ,SX.EMPLT_APLCNT_ID "
															+ "               ,A.NAME "
															+ "               ,A.ORGANIZATION_1 "
															+ "               ,A.ORGANIZATION_2 "
															+ "               ,A.JOB_TITLE_ID "
															+ "               ,B.CAND_TYP_IND "
															+ "               ,B.CAND_AVAIL_IND "
															+ "               ,A.EMPLOYMENT_CAT "
															+ "               ,A.EMP_CAT_EFF_DT "
															+ "               ,D.CRT_TS "
															+ "               ,AX.Z_EMPLID "
															+ "FROM PERSON_PROFILES_T A "
															+ "    ,EMPLT_POSN_CAND B "
															+ "    ,STRGRP_JTTL_CAND C "
															+ "    ,ASSOC_PREF_STR D "
															+ "    ,EAPLCNT_SSN_XREF SX "
															+ "    ,EAPLCNT_CAND_XREF CX "
															+ "    ,HR_EMPL_TSRT_XREF AX "															
															+ "WHERE SX.EMPLT_APLCNT_ID = B.EMPLT_POSN_CAND_ID "
															+ "  AND B.EMPLT_POSN_CAND_ID = C.EMPLT_POSN_CAND_ID "
															+ "  AND C.EMPLT_POSN_CAND_ID = D.HR_SYS_EMPL_ID "
															+ "  AND AX.EMPL_INTL_ID = SX.APLCNT_SSN_NBR "
															+ "  AND A.PERSON_ID = AX.TSRT_EMPL_ID "
															+ "  AND SX.EMPLT_APLCNT_ID = CX.EMPLT_APLCNT_ID "
															+ "  AND D.HR_SYS_STR_NBR = ? "
															+ "  AND ( A.ORGANIZATION_1 <> ? "
															+ "     OR A.ORGANIZATION_2 <> ? "
															+ "     OR A.JOB_TITLE_ID <> ? ) "
															+ "  AND A.STATUS_CD = ? "
															+ "  AND B.CAND_TYP_IND = ? "
															+ "  AND C.ACTV_FLG = ? "
															+ "  AND C.MIN_REQMT_MET_FLG = ? "
															+ "  AND SX.ACTV_FLG = ? "
															+ "  AND SX.EFF_BGN_DT <= ? "
															+ "  AND SX.EFF_END_DT > ? "
															+ "  AND AX.HR_SYS_CNTRY_CD = ? "
															+ "  AND B.CAND_AVAIL_IND IN ( {0} ) "
															+ "  AND C.CDECRE_JCTGRY_CD = ? "
															+ "  AND ( A.EMPLOYMENT_CAT IN ( {1} ) "
															+ "  {4} {5} "
														//	+ "  AND C.HR_STR_GRP_CD IN ( {2} ) "
															+ " {3} "
														//	+ "ORDER BY D.CRT_TS DESC "
														//	+ "FETCH FIRST 400 ROWS ONLY "
															+ "WITH UR ";
	
	public static final String SQL_JOB_SKILL_FILTER_ASSOCIATE = " AND (SELECT COUNT(*) "
															  + "      FROM APLCNT_JSKIL JSKIL "
															  + "      WHERE JSKIL.EMPLT_APLCNT_ID = SX.EMPLT_APLCNT_ID "
															  + "        AND JSKIL.JSKIL_CD = ? ) = 1 ";
		             
	public static final String SQL_GET_QP_ASSOC_FT_PT = "    OR ( A.EMPLOYMENT_CAT = ? "
													  + "         AND ((DAYS(CURRENT DATE) - DAYS(A.EMP_CAT_EFF_DT) >= ?) "
													  + "         AND ( DAYS(CURRENT DATE) - DAYS(A.EMP_CAT_EFF_DT) <= ?) ) ) ) ";	             
		              
	public static final String SQL_ASSOC_TIME_IN_POSITION = "SELECT SX.EMPLT_APLCNT_ID "
														  + "      ,MAX(DISTINCT POS.REL_BEGIN_DT) AS CURR_POS_START_DT "
														  + "FROM EE_POSITIONS_T POS "
														  + "    ,EAPLCNT_SSN_XREF SX "
														  + "    ,HR_EMPL_TSRT_XREF TX "
														  + "WHERE SX.EMPLT_APLCNT_ID IN ( {0} ) "
														  + "  AND TX.EMPL_INTL_ID = SX.APLCNT_SSN_NBR "
														  + "  AND POS.PERSON_ID = TX.TSRT_EMPL_ID "
														  + "  AND SX.ACTV_FLG = ? "
														  + "  AND SX.EFF_BGN_DT <= ? "
														  + "  AND SX.EFF_END_DT > ? "
														  + "  AND TX.HR_SYS_CNTRY_CD = ? "
														  + "GROUP BY SX.EMPLT_APLCNT_ID "
														  + "ORDER BY SX.EMPLT_APLCNT_ID "
														  + "WITH UR ";
	
	public static final String SQL_ASSOC_REVIEW_RESULTS = "SELECT USER_COMP_FLD_1 "
														+ "      ,EMPLT_APLCNT_ID "
														+ "FROM EE_COMPS_T "
														+ "    ,EAPLCNT_SSN_XREF SX "
														+ "    ,HR_EMPL_TSRT_XREF TX "
														+ "WHERE SX.EMPLT_APLCNT_ID IN ( {1} ) "
														+ "  AND SX.APLCNT_SSN_NBR = TX.EMPL_INTL_ID "
														+ "  AND PERSON_ID = TX.TSRT_EMPL_ID "
														+ "  AND RECORD_STATUS = ? " 
														+ "  AND COMP_ACTN IN ( {0} ) "
														+ "  AND SX.ACTV_FLG = ? " 
														+ "  AND SX.EFF_BGN_DT <= ? " 
														+ "  AND SX.EFF_END_DT > ? " 
														+ "  AND TX.HR_SYS_CNTRY_CD = ? " 
														+ "ORDER BY EMPLT_APLCNT_ID, EFF_DT DESC "
														+ "WITH UR ";


	public static final String SQL_GET_ASSOCIATE_REVIEW_SCORES  = "SELECT DISTINCT AXREF.Z_EMPLID, CHAR(A1.EFF_DT, ISO) DATE "
																+ "               ,A1.SEQUENCE_NO SEQ "
																+ "               ,A1.USER_COMP_FLD_1 PERFORMANCE "
																+ "               ,C1.EVAL_RTG_DESC AS PERFORMANCE_DESC "
																+ "FROM EE_COMPS_T A1 "
																+ "     INNER JOIN HR_EMPL_TSRT_XREF AXREF ON AXREF.TSRT_EMPL_ID = A1.PERSON_ID "
																+ "     LEFT OUTER JOIN EVAL_CTGRY_HR_RTG B1 ON B1.HR_SYS_RTG_CD = A1.USER_COMP_FLD_1 "
																+ "                                         AND B1.EVAL_CTGRY_CD = ? "
																+ "                                         AND A1.EFF_DT >= B1.EFF_BGN_DT "
																+ "                                         AND A1.EFF_DT <= B1.EFF_END_DT " 
																+ "     LEFT OUTER JOIN N_EVAL_RTG_CD C1 ON C1.EVAL_RTG_CD = B1.EVAL_RTG_CD "
																+ "                                     AND C1.LANG_CD = ? "
																+ "WHERE AXREF.Z_EMPLID IN ( {0} ) "
																+ "  AND A1.RECORD_STATUS = ? "
																+ "  AND A1.COMP_ACTN IN ( {2} ) "
																+ "  AND (B1.EVAL_RTG_CD IS NULL OR B1.EVAL_RTG_CD <= ?) "
																+ "UNION "
																+ "SELECT DISTINCT AXREF.Z_EMPLID, CHAR(A1.EFF_DT, ISO) DATE "
																+ "               ,A1.SEQUENCE_NO SEQ "
																+ "               ,A1.USER_COMP_FLD_1 PERFORMANCE "
																+ "               ,''('' || TRIM(C1.D_EVAL_RTG_CD) || '') '' || C1.EVAL_RTG_DESC AS PERFORMANCE_DESC "
																+ "FROM EE_COMPS_T A1 "
																+ "     INNER JOIN HR_EMPL_TSRT_XREF AXREF ON AXREF.TSRT_EMPL_ID = A1.PERSON_ID "
																+ "     LEFT OUTER JOIN EVAL_CTGRY_HR_RTG B1 ON B1.HR_SYS_RTG_CD = A1.USER_COMP_FLD_1 "
																+ "                                         AND B1.EVAL_CTGRY_CD = ? "
																+ "                                         AND A1.EFF_DT >= B1.EFF_BGN_DT "
																+ "                                         AND A1.EFF_DT <= B1.EFF_END_DT " 
																+ "     LEFT OUTER JOIN N_EVAL_RTG_CD C1 ON C1.EVAL_RTG_CD = B1.EVAL_RTG_CD "
																+ "                                     AND C1.LANG_CD = ? "
																+ "WHERE AXREF.Z_EMPLID IN ( {1} ) "
																+ "  AND A1.RECORD_STATUS = ? "
																+ "  AND A1.COMP_ACTN IN ( {3} ) "
																+ "  AND (B1.EVAL_RTG_CD IS NOT NULL AND B1.EVAL_RTG_CD > ?) "
																+ "ORDER BY SEQ DESC, DATE DESC "
																+ "WITH UR";
	
	public static final String SQL_GET_ACTIVE_APPLICANTS_QP = "SELECT DISTINCT A.EMPLT_APLCNT_ID "
														    + "               ,A.LAST_NM "
														    + "               ,A.FRST_NM "
														    + " 			  ,TRIM(A.LAST_NM) || '','' || TRIM(A.FRST_NM) AS NAME "
														    + "               ,A.EMPLT_APPL_DT "
														    + "               ,A.AVAIL_DT "
														    + "               ,A.REV_HRLOC_NBR "
														    + "               ,A.FTM_OK_FLG "
														    + "               ,A.PTM_OK_FLG "
														    + "               ,A.FTM_OK_FLG || A.PTM_OK_FLG AS FT_PT_IND "
														    + "               ,C.CAND_TYP_IND "
														    + "               ,A.LAST_UPD_TS "
														    + "               ,G.EMPTST_TIER_NBR "
														    + "               ,CX.EMPLT_CAND_ID "
														    + "FROM EMPLT_APLCNT_PRIM A "
														    + "    ,EMPLT_POSN_CAND C "
														    + "    ,EMPLT_APLCNT_STR D "
														    + "    ,EMPLT_APLCNT_ADDL E "
														    + "    ,APLCNT_EMPTST_TIER G "
														    + "    ,EAPLCNT_CAND_XREF CX "
														    + "WHERE A.EMPLT_APPL_DT = D.EMPLT_APPL_DT "
														    + "  AND A.EMPLT_APPL_DT = E.EMPLT_APPL_DT "
														    + "  AND A.EMPLT_APLCNT_ID = C.EMPLT_POSN_CAND_ID "
														    + "  AND A.EMPLT_APLCNT_ID = D.EMPLT_APLCNT_ID "
														    + "  AND A.EMPLT_APLCNT_ID = E.EMPLT_APLCNT_ID "
														    + "  AND A.EMPLT_APLCNT_ID = G.EMPLT_APLCNT_ID "
														    + "  AND A.EMPLT_APLCNT_ID = CX.EMPLT_APLCNT_ID "
														    + "  AND D.HR_SYS_STR_NBR = ? "
														    + "  AND C.CAND_AVAIL_IND IN ( {0} )"
														    + "  AND C.CAND_TYP_IND = ? " 
														    + "  AND A.ACTV_FLG = ? "
														    + "  AND G.EMPTST_ID = ? "
														    + "  AND G.ACTV_FLG = ? "
														    + "  AND E.MIN_EMPLT_AGE_FLG = ? "
														    + "  AND E.LGL_EMPLT_ELIG_FLG = ? "
														    + "  AND (SELECT CX.RHR_ELIG_IND "
														    + "       FROM EAPLCNT_CAND_XREF CX "
														    + "       WHERE CX.EMPLT_APLCNT_ID = A.EMPLT_APLCNT_ID) != ? "
														    + "   AND NOT EXISTS (SELECT ''Y'' "
														    + "                   FROM EPCAND_TEMP_RJTD TRC "
														    + "                   WHERE TRC.EMPLT_POSN_CAND_ID = C.EMPLT_POSN_CAND_ID "
														    + "                     AND TRC.HR_SYS_STR_NBR = D.HR_SYS_STR_NBR "
														    + "                     AND TRC.MET_FLG = ? "
														    + "                     AND (TRC.EFF_BGN_DT <= ? "
														    + "                     AND TRC.EFF_END_DT >= ? )) "
														    + "  AND NOT EXISTS (SELECT ''Y'' "
														    + "                  FROM EMPLT_DTEST A4 "
														    + "                  WHERE A4.EMPLT_POSN_CAND_ID = A.EMPLT_APLCNT_ID "
														    + "                    AND A4.DRUG_TEST_RSLT_CD IN ( {1} ) "
														    + "                    AND A4.DTEST_TS > ? ) "
														    + "  AND NOT EXISTS (SELECT ''Y'' "
														    + "                  FROM BGC_BKGRD_CHK A5 "
														    + "                      ,BKGRD_CHK_CPNT B5 "
														    + "                      ,BGC_PKG_CPNT C5 "
														    + "                      ,BGC_CPNT D5 "
														    + "                  WHERE A5.BKGRD_CHK_ID = B5.BKGRD_CHK_ID "
														    + "                    AND B5.BGC_CPNT_ID = C5.BGC_CPNT_ID "
														    + "                    AND B5.BGC_CPNT_ID = D5.BGC_CPNT_ID "
														    + "                    AND A5.EMPLT_POSN_CAND_ID = A.EMPLT_APLCNT_ID "
														    + "                    AND B5.BGC_CPNT_CMPLT_DT > ( CURRENT DATE - D5.UNFVRSLT_EFF_DAYS DAYS ) "
														    + "                    AND (C5.EFF_BGN_DT <= CURRENT DATE AND C5.EFF_END_DT >= CURRENT DATE ) "
														    + "                    AND (D5.EFF_BGN_DT <= CURRENT DATE AND D5.EFF_END_DT >= CURRENT DATE ) "
														    + "                    AND A5.ACTV_FLG = ? "
														    + "                    AND C5.BGC_PKG_ID = ? "
														    + "                    AND (B5.BGC_ALRT_STAT_CD = ? OR B5.OVRD_ALRT_STAT_CD = ? )) " 
														    + " {2} "
														    + "ORDER BY G.EMPTST_TIER_NBR ASC "
														    + "        ,A.AVAIL_DT DESC  "
														    + "        ,A.LAST_UPD_TS DESC "
														    + "FETCH first 500 ROWS only "
														    + "WITH UR ";
 	
	public static final String SQL_JOB_SKILL_FILTER_APPLICANT = " AND (SELECT COUNT(*) "
			  												  + "      FROM APLCNT_JSKIL JSKIL "
			  												  + "      WHERE JSKIL.EMPLT_APLCNT_ID = CX.EMPLT_APLCNT_ID "
			  												  + "        AND JSKIL.JSKIL_CD = ? ) = 1 ";

	/**
	 * This method generates a list of qualified associates for the criteria
	 * provided
	 * 
	 * @param strNbr
	 *            Employment requisition store number
	 * @param deptNbr
	 *            Employment requisition department number
	 * @param jobTtlCd
	 *            Employment requisition job title code
	 * @param candAvailInds
	 *            Status(es) candidates should be in to be considered for the
	 *            pool
	 * @param cdecreCtgryCd
	 *            Primary consent decree category code for the job
	 * @param empCategories
	 *            Collection of employment categories that qualify an associate
	 *            for the pool
	 * @param tmpCategory
	 *            Temporary employment category code, some special logic is put
	 *            around this code to exclude temporary associates that don't
	 *            fall within a certain time range
	 * @param tmpCatBgnOffset
	 *            Beginning of the temporary associate time range (inclusive)
	 * @param tmpCatEndOffset
	 *            End of the temporary associate time range (inclusive)
	 * @param jobSkillFilters
	 *            Job skill filters that should be applied before considering an
	 *            associate for the pool. This is an optional parameter and will
	 *            only be applied if the collection of filters is not null or
	 *            empty
	 * @param strGrpCds
	 * 			  Store group codes, not using at the moment           
	 * 
	 * @return A list of qualified associates that match the criteria provided
	 *         or an empty list if no associates meet the criteria
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs executing the query
	 */
	public static List<QualifiedCandidate> getActiveQualifiedAssociatesDAO20(String strNbr, String deptNbr, String jobTtlCd, List<String> candAvailInds, String cdecreCtgryCd, 
			List<String> empCategories, String tmpCategory, int tmpCatBgnOffset, int tmpCatEndOffset, List<Short> jobSkillFilters, List<String> strGrpCds, boolean ptFtReq) throws QueryException {
				
		Date currentDate = new Date(Calendar.getInstance().getTimeInMillis());
		long startTime = System.currentTimeMillis();
		boolean setJobSkillFilter = false;
		short jobSkillFilterValue = 0;
		
		if (jobSkillFilters != null && jobSkillFilters.size() > 0) {
			setJobSkillFilter = true;
			jobSkillFilterValue = jobSkillFilters.get(0);
		} // end if(jobSkillFilters != null && jobSkillFilters.size() > 0)
		
		List<QualifiedCandidate> readRequisitionCandidateList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_GET_ACTIVE_ASSOCIATE_QP)
				.displayAs("Get Active Associates Qualified Pool")
				.input(1, strNbr)
				.input(2, strNbr)
				.input(3, deptNbr) 
				.input(4, jobTtlCd)
				.input(5, ACTIVE)
				.input(6, CAND_TYP_ASSOCIATE)
				.input(7, YES) 
				.input(8, YES)
				.input(9, YES)
				.input(10, currentDate)
				.input(11, currentDate)
				.input(12, USA)
				.inClause(0, candAvailInds)
				.input(13, cdecreCtgryCd)
				.formatOnCondition(ptFtReq, 4, SQL_GET_QP_ASSOC_FT_PT)
				.formatOnCondition(!ptFtReq, 5, " ) ")
				.inClause(1, empCategories)
				.inputOnCondition(ptFtReq, 14, tmpCategory)
				.inputOnCondition(ptFtReq, 15, tmpCatBgnOffset)
				.inputOnCondition(ptFtReq, 16, tmpCatEndOffset)
				//.inClause(2, strGrpCds)
				.formatOnCondition( setJobSkillFilter, 3, SQL_JOB_SKILL_FILTER_ASSOCIATE, "" )
				.inputOnCondition( ptFtReq && setJobSkillFilter, 17, jobSkillFilterValue )
				.inputOnCondition( !ptFtReq && setJobSkillFilter, 14, jobSkillFilterValue )
				.debug(mLogger)
				.formatCycles(1)
				.list(QualifiedCandidate.class);

		if(mLogger.isDebugEnabled())
		{
			long endTime = System.currentTimeMillis();
			mLogger.debug(String.format("Exiting getActiveQualifiedAssociatesDAO20(). Total time to process request: %1$01d.%2$03d seconds", 
				 ((endTime - startTime) / 1000), ((endTime - startTime) % 1000)));
		} // end if
		
		return readRequisitionCandidateList;

	} // end function getActiveQualifiedAssociates()	
		
	public static List<QualifiedCandidate> getAssociateLastReviewDAO20(final List<String> associates) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getAssociateLastReviewDAO20()"));
		} // end if
		
		List<QualifiedCandidate> associateDtls = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				  .setSQL(SQL_GET_ASSOCIATE_REVIEW_SCORES)
				  .displayAs("Associates Last Review Score")
				  .input(1, EVAL_CAT_CODE_1)
				  .input(2, en_US)
				  .inClause(0, associates)
				  .input(3, ACTIVE)
				  .inClause(2, ASSOCIATE_COMPENSATION_ACTION_LIST)
				  .input(4, EVAL_RTG_CODE_27)
				  .input(5, EVAL_CAT_CODE_1)
				  .input(6, en_US)
				  .inClause(1, associates)
				  .input(7, ACTIVE)
				  .inClause(3, ASSOCIATE_COMPENSATION_ACTION_LIST)
				  .input(8, EVAL_RTG_CODE_27)
				  .debug(mLogger)
				  .formatCycles(1)
				  .list(QualifiedCandidate.class);		
						
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting getAssociateLastReviewDAO20(). Total time to get details: %1$.9f seconds.",
				(((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return associateDtls;
	} //End - getAssociateLastReviewDAO20(final List<String> associates)
	
	public static List<QualifiedCandidate> getAssociatesCurrentPositionStartDateDAO20(final List<String> associates) throws QueryException
	{
		long startTime = 0;
		
		if(mLogger.isDebugEnabled())
		{
			startTime = System.nanoTime();
			mLogger.debug(String.format("Entering getAssociatesCurrentPositionStartDateDAO20()"));
		} // end if
		
		final Date currentDate = new Date(System.currentTimeMillis());

		List<QualifiedCandidate> associateDtls = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
													.setSQL(SQL_ASSOC_TIME_IN_POSITION)
													.displayAs("Associates Position Start Date")
													.inClause(0, associates) 
													.input(1, YES)
													.input(2, currentDate)
													.input(3, currentDate)
													.input(4, USA)
													.debug(mLogger)
													.formatCycles(1)
													.list(QualifiedCandidate.class);
						
		if(mLogger.isDebugEnabled())
		{
			long endTime = System.nanoTime();			
			if(startTime == 0){ startTime = endTime; } // end if(startTime == 0)			
			mLogger.debug(String.format("Exiting getAssociatesCurrentPositionStartDateDAO20(). Total time to get associates current position start date details: %1$.9f seconds.",
				 (((double)endTime - startTime) / NANOS_IN_SECOND)));
		} // end if
		
		return associateDtls;
	} // End - getAssociatesCurrentPositionStartDateDAO20
	
	/**
	 * This method generates a list of qualified associates for the criteria
	 * provided
	 * 
	 * @param strNbr
	 *            The store number
	 * @param deptNbr
	 *            The department number
	 * @param posnPrefCd
	 *            Preferred position code for the job
	 * @param cdecreCtgryCd
	 *            Consent decree code for the job
	 * @param cdecreDesc
	 *            Consent Decree job category description
	 * @param posnCtgryCds
	 *            List of position codes, used when determining which position
	 *            codes are valid for the job
	 * @param currentDate
	 *            Today's date
	 * @param candAvailInds
	 *            Status(es) candidates should be in to be considered for the
	 *            pool
	 * @param ftReqdFlg
	 *            true if full-time applicants should be included, false
	 *            otherwise
	 * @param ptReqdFlg
	 *            true if part-time applicants should be included, false
	 *            otherwise
	 * @param tmpCategory
	 *            Temporary employment category code, some special logic is put
	 *            around this code to exclude applicants that were employed as
	 *            temporary associates in the past and have been gone less than
	 *            a specified threshold
	 * @param tmpTermThresholdDt
	 *            The threshold to be applied to former temporary associates
	 *            that have re-applied
	 * @param primPosnCtgryCd
	 *            Primary position category code for the job
	 * @param dtestReqd
	 *            True if a drug test check should be performed for this pool,
	 *            false otherwise
	 * @param dtestFailureCodes
	 *            List of drug test failure codes
	 * @param dtestFailThrshld
	 *            Threshold date for drug test failures
	 * @param jobSkillFilters
	 *            Job skill filters that should be applied before considering an
	 *            associate for the pool. This is an optional parameter and will
	 *            only be applied if the collection of filters is not null or
	 *            empty
	 * @param bgPkgId
	 *            background package id for this job
	 * @param tieringFlg
	 *            Use Kenexa Tiering - int value will be 0 if tiering is Off or
	 *            1 if tiering is On
	 * 
	 * @return A list of qualified applicants that match the criteria provided
	 *         or an empty list if no applicants meet the criteria
	 * 
	 * @throws QueryException
	 *             Thrown if an exception occurs executing the query
	 */	
	public static List<QualifiedCandidate> getActiveQualifiedApplicantsDAO20(String strNbr, 
			Date currentDate, List<String> candAvailInds, 
			List<String> dtestFailureCodes, Timestamp dtestFailThrshld, List<Short> jobSkillFilters, int bgPkgId, short tieringCtgryCd) throws QueryException {
				
		long startTime = System.currentTimeMillis();
		boolean setJobSkillFilter = false;
		short jobSkillFilterValue = 0;
		
		if (jobSkillFilters != null && jobSkillFilters.size() > 0) {
			setJobSkillFilter = true;
			jobSkillFilterValue = jobSkillFilters.get(0);
		} // end if(jobSkillFilters != null && jobSkillFilters.size() > 0)
		
		//Added for FMS 7894 January 2016 CR's
		//tieringCtgryCd for Merchandising is 1002, means this is a MET Job
		String metPosition = "N";
		if (tieringCtgryCd == 1002) {
			metPosition = "Y";
		}
		
		List<QualifiedCandidate> readRequisitionCandidateList = DAO.useJNDI(DATA_SOURCE_DB2Z_PR1_032)
				.setSQL(SQL_GET_ACTIVE_APPLICANTS_QP)
				.displayAs("Get Active Applicants Qualified Pool")
				.input(1, strNbr)
				.inClause(0, candAvailInds)
				.input(2, CAND_TYP_APPLICANT)
				.input(3, YES)
				.input(4, tieringCtgryCd)
				.input(5, YES)
				.input(6, YES)
				.input(7, YES)
				.input(8, NO)
				.input(9,metPosition)
				.input(10, currentDate)
				.input(11, currentDate)
				.inClause(1, dtestFailureCodes)
				.input(12, dtestFailThrshld)
				.input(13, ACTIVE)
				.input(14, bgPkgId)
				.input(15, BACKGROUND_CHK_FAILED_STATUS)
				.input(16, BACKGROUND_CHK_FAILED_STATUS)
				.formatOnCondition( setJobSkillFilter, 2, SQL_JOB_SKILL_FILTER_APPLICANT, "" )
				.inputOnCondition( setJobSkillFilter, 17, jobSkillFilterValue )
				.debug(mLogger)
				.formatCycles(1)
				.list(QualifiedCandidate.class);

		if(mLogger.isDebugEnabled())
		{
			long endTime = System.currentTimeMillis();
			mLogger.debug(String.format("Exiting getActiveQualifiedApplicantDAO20(). Total time to process request: %1$01d.%2$03d seconds", 
				 ((endTime - startTime) / 1000), ((endTime - startTime) % 1000)));
		} // end if
		
		return readRequisitionCandidateList;

	} // end function getActiveQualifiedApplicantsDAO20()	
} // end class QualifiedPoolDAO
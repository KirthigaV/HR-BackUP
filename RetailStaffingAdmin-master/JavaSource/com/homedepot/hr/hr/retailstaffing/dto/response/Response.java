/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: SearchResponse.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;
// import com.homedepot.hr.hr.retailstaffing.dto.response.*;



import com.homedepot.hr.hr.retailstaffing.dto.BackgroundCheckDtlsTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDTInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.CandidateInfoTO;
import com.homedepot.hr.hr.retailstaffing.dto.HiringEventMgrTO;
import com.homedepot.hr.hr.retailstaffing.dto.LocationDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.PagingRecordInfo;
import com.homedepot.hr.hr.retailstaffing.dto.ReadBackgroundCheckSystemConsentByInputListDTO;
import com.homedepot.hr.hr.retailstaffing.dto.StaffingDetailsTO;
import com.homedepot.hr.hr.retailstaffing.dto.UserDataTO;
import com.homedepot.hr.hr.retailstaffing.services.response.StatusResponse;
import com.homedepot.hr.hr.retailstaffing.util.Generic;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * This class is used as final response object for Search
 * 
 * @author TCS
 * 
 */
@XStreamAlias("Response")
public class Response implements Serializable {
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("status")
	private String status;

	@XStreamAlias("successMsg")
	private String successMsg;

	@XStreamAlias("autoHold")
	private String autoHold;

	@XStreamAlias("authCnt")
	private String authCnt;
	
	@XStreamAlias("showQpButton")
	private String showQpButton;
	
	@XStreamAlias("userData")
	private UserDataTO userData;	
	
	@XStreamAlias("currentDate")
	private String currentDate;
	
	@XStreamAlias("maxDOBDate")
	private String maxDOBDate;
	
	@XStreamAlias("showDrugTestPanel")
	private String showDrugTestPanel;
	
	@XStreamAlias("drugTestTypeForLoc")
	private String drugTestTypeForLoc;

	@XStreamAlias("showMinorConsentPanel")
	private String showMinorConsentPanel;
	
	@XStreamAlias("showNewBGCPanel")
	private String showNewBGCPanel;
	
	@XStreamAlias("profileAdvantageLink")
	private String profileAdvantageLink;
	
	/**
	 * @return the authCnt
	 */
	public String getAuthCnt() {
		return authCnt;
	}

	/**
	 * @param authCnt
	 *            the authCnt to set
	 */
	public void setAuthCnt(String authCnt) {
		this.authCnt = authCnt;
	}

	@XStreamAlias("error")
	private GenericErrorTO errorResponse;

	@XStreamAlias("staffingDetails")
	private StaffingDetailsTO stfDtls;

	@XStreamAlias("RequisitionDetailList")
	private RequisitionDetailResponse reqDtRes;

	@XStreamAlias("CandidateDetailList")
	private CandidateDetailResponse canDtRes;

	@XStreamAlias("ITIDetailList")
	private ITIDetailResponse itiDtRes;

	@XStreamAlias("ITIDetailListInactive")
	private ITIDetaiInactivelResponse itiDtResInact;
	
	@XStreamAlias("ScheduleDescList")
	private ScheduleDescResponse scheduleRes;
	
	@XStreamAlias("CalendarList")
	private CalendarResponse calendarRes;
		
	@XStreamAlias("InterviewQuestionsList")
	private InterviewQuestDetailResponse intrwDtRes;
	
	@XStreamAlias("CandidateInformation")
	private CandidateInfoTO candinfoDtRes;
	
	@XStreamAlias("CandidateDTInformation")
	private CandidateDTInfoTO candDTinfoDtRes;
	
	@XStreamAlias("InterviewerInfo")
	private ApplIntvwDtlsResponse interviewerDtlsRes;
	
	@XStreamAlias("BackgroundCheckInfo")
	private BackgroundCheckDtlsTO backgroundChkDtlRes;
	
	@XStreamAlias("LicenseInformation")
	private ReadBackgroundCheckSystemConsentByInputListDTO licenseInfoRes;
	
	@XStreamAlias("CandidateDetails")
	private CandidateDetailsTO candidateDetails;
	
	@XStreamAlias("schPrefChkBxSelectionList")
	private List<Boolean> schPrefChkBxSelectionList;
	
	/**
	 * @return the itiDtResInact
	 */
	public ITIDetaiInactivelResponse getItiDtResInact() {
		return itiDtResInact;
	}

	/**
	 * @param itiDtResInact
	 *            the itiDtResInact to set
	 */
	public void setItiDtResInact(ITIDetaiInactivelResponse itiDtResInact) {
		this.itiDtResInact = itiDtResInact;
	}

	@XStreamAlias("StoreDetailList")
	private StoreDetailResponse strDtRes;

	@XStreamAlias("StoreDriversLicenseExemptList")
	private StoreDriverLicenseExemptResponse strDlExemptRes;
	
	@XStreamAlias("StateDetailList")
	private StateDetailResponse stateDtRes;

	@XStreamAlias("ExpLevelList")
	private ExpLvlResponse expLvlRes;

	@XStreamAlias("LangSklsList")
	private LangSklsResponse langSklsRes;

	@XStreamAlias("DepartmentList")
	private DeptResponse deptRes;

	@XStreamAlias("JobTitleList")
	private JobTtlResponse jobTtlRes;
		
	@XStreamAlias("StatusList")
	private StatusResponse statusListRes;
	
	@XStreamAlias("ProcessStatusList")
	private ProcessStatusesResponse processStsRes;

	@XStreamAlias("InterviewNoReasonList")
	private NoInterviewReasonResponse noIntvwReasonRes;
	
	@XStreamAlias("OfferMadeList")
	private OfferMadeListResponse offerMadeListRes;
	
	@XStreamAlias("OfferResultList")
	private OfferResultListResponse offerResultListRes;

	@XStreamAlias("OfferDeclineReasonList")
	private OfferDeclineListResponse offerDeclineListRes;
	
	@XStreamAlias("StructuredInterviewGuideList")
	private StructuredInterviewGuideListResponse StructuredInterviewGuideListRes;
	
	@XStreamAlias("DivList")
	private OrgUnitResponse divList;

	@XStreamAlias("DistList")
	private OrgUnitResponse distList;

	@XStreamAlias("RegionList")
	private OrgUnitResponse regionList;

	@XStreamAlias("firstRecord")
	PagingRecordInfo firstRecordInfo;

	@XStreamAlias("secondRecord")
	PagingRecordInfo secondRecordInfo;

	@XStreamAlias("CmplITIDetail")
	private CompleteITIDetailResponse cmplITIDet;

	@XStreamAlias("rejComboRes")
	private RejectionComboResponse rejComboRes;

	@XStreamAlias("rejectedReasonId")
	private int rejectedReasonId;

	@XStreamAlias("hiringEventResponse")
	private HiringEventResponse hiringEventResponse;
	
	@XStreamAlias("hiringEventMgrData")
	private HiringEventMgrTO hiringEventMgrData; 
	
	@XStreamAlias("LocationDetails")
	private LocationDetailsTO locationDetails;
	
	@XStreamAlias("AutoAttachJobTitleList")
	private AutoAttachJobTitleResponse autoAttachJobTitleRes;	
	
	@XStreamAlias("PhoneScreenDispositionList")
	private PhnScrnDispCodeResponse phnScrDispCodeRes;
	
	@XStreamAlias("PhoneScreenCallHistoryList")
	private PhnScrnCallHistoryResponse phnScrnCallHistoryRes;
	
	/**
	 * @return the cmplITIDet
	 */
	public CompleteITIDetailResponse getCmplITIDet() {
		return cmplITIDet;
	}

	/**
	 * @param cmplITIDet
	 *            the cmplITIDet to set
	 */
	public void setCmplITIDet(CompleteITIDetailResponse cmplITIDet) {
		this.cmplITIDet = cmplITIDet;
	}

	/**
	 * @return the expLvlRes
	 */
	public ExpLvlResponse getExpLvlRes() {
		return expLvlRes;
	}

	/**
	 * @param expLvlRes
	 *            the expLvlRes to set
	 */
	public void setExpLvlRes(ExpLvlResponse expLvlRes) {
		this.expLvlRes = expLvlRes;
	}

	/**
	 * @param errorResponse
	 *            the errorResponse to set
	 */
	@Generic(action = "SET")
	public void setErrorResponse(GenericErrorTO errorRes) {
		this.errorResponse = errorRes;
	}

	/**
	 * @return the errorResponse
	 */
	@Generic(action = "GET")
	public GenericErrorTO getErrorResponse() {
		return errorResponse;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	/**
	 * @param reqDtlList
	 *            the reqDtlList to set
	 */
	public void setReqDtlList(RequisitionDetailResponse reqDtlRes) {
		this.reqDtRes = reqDtlRes;
	}

	/**
	 * @return the reqDtlList
	 */
	public RequisitionDetailResponse getReqDtlList() {
		return reqDtRes;
	}

	/**
	 * @return the canDtRes
	 */
	public CandidateDetailResponse getCanDtRes() {
		return canDtRes;
	}

	/**
	 * @param canDtRes
	 *            the canDtRes to set
	 */
	public void setCanDtRes(CandidateDetailResponse canDtRes) {
		this.canDtRes = canDtRes;
	}

	/**
	 * @return the itiDtRes
	 */
	public ITIDetailResponse getItiDtRes() {
		return itiDtRes;
	}

	/**
	 * @param itiDtRes
	 *            the itiDtRes to set
	 */
	public void setItiDtRes(ITIDetailResponse itiDtRes) {
		this.itiDtRes = itiDtRes;
	}

	/**
	 * @return the strDtRes
	 */
	public StoreDetailResponse getStrDtRes() {
		return strDtRes;
	}

	/**
	 * @param stateDtRes
	 *            the stateDtRes to set
	 */
	public void setStateDtRes(StateDetailResponse stateDtRes) {
		this.stateDtRes = stateDtRes;
	}

	/**
	 * @return the stateDtRes
	 */
	public StateDetailResponse getStateDtRes() {
		return stateDtRes;
	}

	/**
	 * @param strDtRes
	 *            the strDtRes to set
	 */
	public void setStrDtRes(StoreDetailResponse strDtRes) {
		this.strDtRes = strDtRes;
	}

	/**
	 * @param successMsg
	 *            the successMsg to set
	 */
	public void setSuccessMsg(String successMsg) {
		this.successMsg = successMsg;
	}

	/**
	 * @return the successMsg
	 */
	public String getSuccessMsg() {
		return successMsg;
	}

	/**
	 * @param stfDtls
	 *            the stfDtls to set
	 */
	public void setStfDtls(StaffingDetailsTO stfDtls) {
		this.stfDtls = stfDtls;
	}

	/**
	 * @return the stfDtls
	 */
	public StaffingDetailsTO getStfDtls() {
		return stfDtls;
	}

	public OrgUnitResponse getDivList() {
		return divList;
	}

	public void setDivList(OrgUnitResponse divList) {
		this.divList = divList;
	}

	public OrgUnitResponse getDistList() {
		return distList;
	}

	public void setDistList(OrgUnitResponse distList) {
		this.distList = distList;
	}

	public OrgUnitResponse getRegionList() {
		return regionList;
	}

	public void setRegionList(OrgUnitResponse regionList) {
		this.regionList = regionList;
	}

	public PagingRecordInfo getFirstRecordInfo() {
		return firstRecordInfo;
	}

	public void setFirstRecordInfo(PagingRecordInfo firstRecordInfo) {
		this.firstRecordInfo = firstRecordInfo;
	}

	public PagingRecordInfo getSecondRecordInfo() {
		return secondRecordInfo;
	}

	public void setSecondRecordInfo(PagingRecordInfo secondRecordInfo) {
		this.secondRecordInfo = secondRecordInfo;
	}

	public ProcessStatusesResponse getProcessStsRes() {
		return processStsRes;
	}

	public void setProcessStsRes(ProcessStatusesResponse processStsRes) {
		this.processStsRes = processStsRes;
	}

	/**
	 * @return the autoHold
	 */
	public String getAutoHold() {
		return autoHold;
	}

	/**
	 * @param autoHold
	 *            the autoHold to set
	 */
	public void setAutoHold(String autoHold) {
		this.autoHold = autoHold;
	}

	public LangSklsResponse getLangSklsRes() {
		return langSklsRes;
	}

	public void setLangSklsRes(LangSklsResponse langSklsRes) {
		this.langSklsRes = langSklsRes;
	}

	public DeptResponse getDeptRes() {
		return deptRes;
	}

	public void setDeptRes(DeptResponse deptRes) {
		this.deptRes = deptRes;
	}

	public JobTtlResponse getJobTtlRes() {
		return jobTtlRes;
	}

	public void setJobTtlRes(JobTtlResponse jobTtlRes) {
		this.jobTtlRes = jobTtlRes;
	}

	public RequisitionDetailResponse getReqDtRes() {
		return reqDtRes;
	}

	public void setReqDtRes(RequisitionDetailResponse reqDtRes) {
		this.reqDtRes = reqDtRes;
	}

	public ScheduleDescResponse getScheduleRes() {
		return scheduleRes;
	}

	public void setScheduleRes(ScheduleDescResponse scheduleRes) {
		this.scheduleRes = scheduleRes;
	}

	public CalendarResponse getCalendarRes() {
		return calendarRes;
	}

	public void setCalendarRes(CalendarResponse calendarRes) {
		this.calendarRes = calendarRes;
	}

	public String getShowQpButton() {
		return showQpButton;
	}

	public void setShowQpButton(String showQpButton) {
		this.showQpButton = showQpButton;
	}

	public NoInterviewReasonResponse getNoIntvwReasonRes() {
		return noIntvwReasonRes;
	}

	public void setNoIntvwReasonRes(NoInterviewReasonResponse noIntvwReasonRes) {
		this.noIntvwReasonRes = noIntvwReasonRes;
	}
	
	public OfferMadeListResponse getOfferMadeListRes() {
		return offerMadeListRes;
	}

	public void setOfferMadeListRes(OfferMadeListResponse offerMadeListRes) {
		this.offerMadeListRes = offerMadeListRes;
	}	

	public OfferResultListResponse getOfferResultListRes() {
		return offerResultListRes;
	}

	public void setOfferResultListRes(OfferResultListResponse offerResultListRes) {
		this.offerResultListRes = offerResultListRes;
	}	
	
	public OfferDeclineListResponse getOfferDeclineListRes() {
		return offerDeclineListRes;
	}

	public void setOfferDeclineListRes(OfferDeclineListResponse offerDeclineListRes) {
		this.offerDeclineListRes = offerDeclineListRes;
	}

	public StructuredInterviewGuideListResponse getStructuredInterviewGuideListRes() {
		return StructuredInterviewGuideListRes;
	}

	public void setStructuredInterviewGuideListRes(StructuredInterviewGuideListResponse structuredInterviewGuideListRes) {
		StructuredInterviewGuideListRes = structuredInterviewGuideListRes;
	}

	public void setIntrwDtRes(InterviewQuestDetailResponse intrwDtRes) {
		this.intrwDtRes = intrwDtRes;
	}

	public InterviewQuestDetailResponse getIntrwDtRes() {
		return intrwDtRes;
	}

	public void setCandinfoDtRes(CandidateInfoTO candinfoDtRes) {
		this.candinfoDtRes = candinfoDtRes;
	}

	public CandidateInfoTO getCandinfoDtRes() {
		return candinfoDtRes;
	}

	public ApplIntvwDtlsResponse getInterviewerDtlsRes() {
		return interviewerDtlsRes;
	}

	public void setInterviewerDtlsRes(ApplIntvwDtlsResponse interviewerDtlsRes) {
		this.interviewerDtlsRes = interviewerDtlsRes;
	}

	public BackgroundCheckDtlsTO getBackgroundChkDtlRes() {
		return backgroundChkDtlRes;
	}

	public void setBackgroundChkDtlRes(BackgroundCheckDtlsTO backgroundChkDtlRes) {
		this.backgroundChkDtlRes = backgroundChkDtlRes;
	}

	public ReadBackgroundCheckSystemConsentByInputListDTO getLicenseInfoRes() {
		return licenseInfoRes;
	}

	public void setLicenseInfoRes(ReadBackgroundCheckSystemConsentByInputListDTO licenseInfoRes) {
		this.licenseInfoRes = licenseInfoRes;
	}

	public StatusResponse getStatusListRes() {
		return statusListRes;
	}

	public void setStatusListRes(StatusResponse statusListRes) {
		this.statusListRes = statusListRes;
	}

	public void setRejectList(RejectionComboResponse rejComboRes) {
		this.rejComboRes = rejComboRes ;
	}
	
	public RejectionComboResponse getRejectList(){
		return rejComboRes;
	}

	public int getRejectedReasonId() {
		return rejectedReasonId;
	}

	public void setRejectedReasonId(int rejectedReasonId) {
		this.rejectedReasonId = rejectedReasonId;
	}

	public HiringEventResponse getHiringEventResponse() {
		return hiringEventResponse;
	}

	public void setHiringEventResponse(HiringEventResponse hiringEventResponse) {
		this.hiringEventResponse = hiringEventResponse;
	}

	public HiringEventMgrTO getHiringEventMgrData() {
		return hiringEventMgrData;
	}

	public void setHiringEventMgrData(HiringEventMgrTO hiringEventMgrData) {
		this.hiringEventMgrData = hiringEventMgrData;
	}

	public UserDataTO getUserData() {
		return userData;
	}

	public void setUserData(UserDataTO userData) {
		this.userData = userData;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public CandidateDetailsTO getCandidateDetails() {
		return candidateDetails;
	}

	public void setCandidateDetails(CandidateDetailsTO candidateDetails) {
		this.candidateDetails = candidateDetails;
	}

	public String getMaxDOBDate() {
		return maxDOBDate;
	}

	public void setMaxDOBDate(String maxDOBDate) {
		this.maxDOBDate = maxDOBDate;
	}

	public LocationDetailsTO getLocationDetails() {
		return locationDetails;
	}

	public void setLocationDetails(LocationDetailsTO locationDetails) {
		this.locationDetails = locationDetails;
	}

	public AutoAttachJobTitleResponse getAutoAttachJobTitleRes() {
		return autoAttachJobTitleRes;
	}

	public void setAutoAttachJobTitleRes(AutoAttachJobTitleResponse autoAttachJobTitleRes) {
		this.autoAttachJobTitleRes = autoAttachJobTitleRes;
	}

	public StoreDriverLicenseExemptResponse getStrDlExemptRes() {
		return strDlExemptRes;
	}

	public void setStrDlExemptRes(StoreDriverLicenseExemptResponse strDlExemptRes) {
		this.strDlExemptRes = strDlExemptRes;
	}

	public PhnScrnDispCodeResponse getPhnScrDispCodeRes() {
		return phnScrDispCodeRes;
	}

	public void setPhnScrDispCodeRes(PhnScrnDispCodeResponse phnScrDispCodeRes) {
		this.phnScrDispCodeRes = phnScrDispCodeRes;
	}

	/**
	 * @return the schPrefChkBxSelectionList
	 */
	public List<Boolean> getSchPrefChkBxSelectionList() {
		return schPrefChkBxSelectionList;
	}

	/**
	 * @param schPrefChkBxSelectionList the schPrefChkBxSelectionList to set
	 */
	public void setSchPrefChkBxSelectionList(
			List<Boolean> schPrefChkBxSelectionList) {
		this.schPrefChkBxSelectionList = schPrefChkBxSelectionList;
	}

	public PhnScrnCallHistoryResponse getPhnScrnCallHistoryRes() {
		return phnScrnCallHistoryRes;
	}

	public void setPhnScrnCallHistoryRes(
			PhnScrnCallHistoryResponse phnScrnCallHistoryRes) {
		this.phnScrnCallHistoryRes = phnScrnCallHistoryRes;
	}

	public CandidateDTInfoTO getCandDTinfoDtRes() {
		return candDTinfoDtRes;
	}

	public void setCandDTinfoDtRes(CandidateDTInfoTO candDTinfoDtRes) {
		this.candDTinfoDtRes = candDTinfoDtRes;
	}

	public String getShowDrugTestPanel() {
		return showDrugTestPanel;
	}

	public void setShowDrugTestPanel(String showDrugTestPanel) {
		this.showDrugTestPanel = showDrugTestPanel;
	}

	public String getDrugTestTypeForLoc() {
		return drugTestTypeForLoc;
	}

	public void setDrugTestTypeForLoc(String drugTestTypeForLoc) {
		this.drugTestTypeForLoc = drugTestTypeForLoc;
	}

	public String getShowMinorConsentPanel() {
		return showMinorConsentPanel;
	}

	public void setShowMinorConsentPanel(String showMinorConsentPanel) {
		this.showMinorConsentPanel = showMinorConsentPanel;
  }
  
	public String getShowNewBGCPanel() {
		return showNewBGCPanel;
	}

	public void setShowNewBGCPanel(String showNewBGCPanel) {
		this.showNewBGCPanel = showNewBGCPanel;
	}

	public String getProfileAdvantageLink() {
		return profileAdvantageLink;
	}

	public void setProfileAdvantageLink(String profileAdvantageLink) {
		this.profileAdvantageLink = profileAdvantageLink;
	}

}

/*
 * Created on Feb 12, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: PhoneScreenIntrwDetailsTO.java
 */
package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * This class is used as to send Phone Screen interview details response in XML
 * format
 * 
 * @author TCS
 * 
 */
//Added the interface Comparable<PhoneScreenIntrwDetailsTO> for sorting purpose - For Flex to HTML conversion - 13 May 2015 
@XStreamAlias("PhoneScreenIntrwDetail")
public class PhoneScreenIntrwDetailsTO implements Serializable ,Comparable<PhoneScreenIntrwDetailsTO>
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("reqNbr")
	private String reqNbr;
	@XStreamAlias("cndtNbr")
	private String cndtNbr;
	@XStreamAlias("ScreenNbr")
	private String ScreenNbr;
	@XStreamAlias("ovrAllscrTyp")
	private String ovrAllscrTyp;
	@XStreamAlias("phnScrnDate")
	private DateTO phnScrnDate;
	@XStreamAlias("phnScrnTime")
	private TimeStampTO phnScrnTime;
	@XStreamAlias("phnScreener")
	private String phnScreener;
	@XStreamAlias("title")
	private String title;
	@XStreamAlias("itiNbr")
	private String itiNbr;
	@XStreamAlias("canPhn")
	private String canPhn;
	
	//Added To format the phone number - For Flex to HTML Conversion - 13 May 2015 
	@XStreamAlias("canPhnFormatted")
	private String canPhnFormatted;
	
	@XStreamAlias("ynstatus")
	private String ynstatus;
	@XStreamAlias("overAllStatus")
	private String overAllStatus;

	@XStreamAlias("phoneScreenStatus")
	private String phoneScreenStatus;

	@XStreamAlias("canStatus")
	private String canStatus;
	@XStreamAlias("internalExternal")
	private String internalExternal;
	@XStreamAlias("AIMSDisp")
	private String AIMSDisp;
	@XStreamAlias("detailTxt")
	private String detailTxt;
	@XStreamAlias("nonHrgStoreLoc")
	private String nonHrgStoreLoc;
	@XStreamAlias("hrgStoreLoc")
	private String hrgStoreLoc;
	@XStreamAlias("hrgEvntLoc")
	private String hrgEvntLoc;
	@XStreamAlias("othrLoc")
	private String othrLoc;
	@XStreamAlias("MinimumResponseDtlList")
	private List<MinimumResponseTO> minResList;
	@XStreamAlias("InterviewLocDtls")
	private IntrwLocationDetailsTO intrLocDtls;
	@XStreamAlias("aid")
	private String aid;
	@XStreamAlias("name")
	private String name;
	@XStreamAlias("scrTyp")
	private String scrTyp;
	@XStreamAlias("job")
	private String job;
	@XStreamAlias("interviewLocTypCd")
	private String interviewLocTypCd;
	@XStreamAlias("cndStrNbr")
	private String cndStrNbr;
	@XStreamAlias("cndDeptNbr")
	private String cndDeptNbr;
	@XStreamAlias("jobTtl")
	private String jobTtl;
	@XStreamAlias("contactHistoryTxt")
	private String contactHistoryTxt;
	@XStreamAlias("hrEvntFlg")
	private String hrEvntFlg;
	@XStreamAlias("ynStatusDesc")
	private String ynStatusDesc;
	@XStreamAlias("offered")
	private String offered;
	@XStreamAlias("emailAdd")
	private String emailAdd;
	@XStreamAlias("fillDt")
	private String fillDt;
	@XStreamAlias("reqCalId")
	private String reqCalId;
	@XStreamAlias("namesOfInterviewers")
	private String namesOfInterviewers;
	@XStreamAlias("rscSchdFlg")
	private String rscSchdFlg;

	@XStreamAlias("interviewStatusTime")
	private TimeStampTO interviewStatusTimestamp;
	@XStreamAlias("interviewMaterialStatusTimestamp")
	private TimeStampTO interviewMaterialStatusTimestamp;

	@XStreamAlias("phoneScreenStatusTimestamp")
	private TimeStampTO phoneScreenStatusTimestamp;

	@XStreamAlias("phoneScreenStatusDesc")
	private String phoneScreenStatusDesc;

	@XStreamAlias("phoneScreenStatusCode")
	private String phoneScreenStatusCode;

	@XStreamAlias("interviewStatusCode")
	private String interviewStatusCode;

	@XStreamAlias("interviewStatusDesc")
	private String interviewStatusDesc;

	@XStreamAlias("interviewMaterialStatusCode")
	private String interviewMaterialStatusCode;
	
	@XStreamAlias("phoneScreenDispositionCode")
	private short phoneScreenDispositionCode;

	@XStreamAlias("phoneScreenDispositionCodeTimestamp")
	private TimeStampTO phoneScreenDispositionCodeTimestamp;
	
	// Added by p22o0mn for Defect #3323: To Get the Flag for enough Phone
	// Screens Completed or not for a given requisition
	@XStreamAlias("phoneScreensCompletedFlg")
	private String phoneScreensCompletedFlg;

	// Added by Sethu for New Data model change to support CTI: HR_REQN_SCH
	@XStreamAlias("intrvSeqNbr")
	private String intrvSeqNbr;

	private PhoneScreenNoteTO note;
	
	private String phoneScreenBannerNum;
	
	// these fields were added during RSA 4.0 (for CTI release 2 inbound phone screens)
	/*
	 * First name of the phone screen candidate
	 */
	@XStreamOmitField()
	private String mFirstName;
	/*
	 * Last name of the phone screen candidate
	 */
	@XStreamOmitField()
	private String mLastName;
	/*
	 * Alternate phone for the phone screen candidate
	 */
	@XStreamOmitField
	private String mPhone2;
	/*
	 * Time zone of the phone screen candidate
	 */
	@XStreamOmitField
	private String mTimeZone;
	/*
	 * Preferred language of the phone screen candidate
	 */
	@XStreamOmitField
	private String mLangPref;
	/*
	 * Skill test id required by the job title of the requisition this phone screen candidate belongs to
	 */
	@XStreamOmitField
	private String mSkillType;
	/*
	 * Status of the requisition this phone screen candidate belongs to
	 */
	@XStreamOmitField
	private String mReqStat;
	/*
	 * Manager ID for this phone screen candidate
	 */
	@XStreamOmitField
	private String mMgrId;
	
	//Added for CDP
	@XStreamAlias("candRefNbr")
	private String candRefNbr;
	
	@XStreamAlias("accessCode")
	private String accessCode;
	
	//Added for RSC Automation IVR Changes Jan 2016
	@XStreamAlias("schdIntrvwChecks")
	private SchdIntrvwChecksTO schdIntrvwChecks;	
	
	/**
	 * Added as part of Flex to HTML conversion - 13 May 2015
	 * Description: To sort the PhoneScreenIntrwDetailsTO based upon the name
	 */
	public int compareTo(PhoneScreenIntrwDetailsTO phoneScreenIntrwDetailsTO)
	{
		if(name!=null && phoneScreenIntrwDetailsTO!=null && phoneScreenIntrwDetailsTO.getName()!=null){
			return name.toUpperCase().compareTo(phoneScreenIntrwDetailsTO.getName().toUpperCase());
		}
		return 0;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
 
        if (!(o instanceof PhoneScreenIntrwDetailsTO)) {
            return false;
        }
        return false;
    }
	
	@Override
	public int hashCode()
	{
		return 0;
	}
	public TimeStampTO getInterviewStatusTimestamp()
	{
		return interviewStatusTimestamp;
	}
	
	public void setInterviewStatusTimestamp(TimeStampTO interviewStatusTimestamp)
	{
		this.interviewStatusTimestamp = interviewStatusTimestamp;
	}

	public TimeStampTO getInterviewMaterialStatusTimestamp()
	{
		return interviewMaterialStatusTimestamp;
	}

	public void setInterviewMaterialStatusTimestamp(TimeStampTO interviewMaterialStatusTimestamp)
	{
		this.interviewMaterialStatusTimestamp = interviewMaterialStatusTimestamp;
	}

	public String getInterviewStatusCode()
	{
		return interviewStatusCode;
	}

	public void setInterviewStatusCode(String interviewStatusCode)
	{
		this.interviewStatusCode = interviewStatusCode;
	}

	public String getInterviewMaterialStatusCode()
	{
		return interviewMaterialStatusCode;
	}

	public void setInterviewMaterialStatusCode(String interviewMaterialStatusCode)
	{
		this.interviewMaterialStatusCode = interviewMaterialStatusCode;
	}

	public String getPhoneScreenStatus()
	{
		return phoneScreenStatus;
	}

	public void setPhoneScreenStatus(String phoneScreenStatus)
	{
		this.phoneScreenStatus = phoneScreenStatus;
	}

	public String getPhoneScreenStatusDesc()
	{
		return phoneScreenStatusDesc;
	}

	public void setPhoneScreenStatusDesc(String phoneScreenStatusDesc)
	{
		this.phoneScreenStatusDesc = phoneScreenStatusDesc;
	}

	public String getPhoneScreenStatusCode()
	{
		return phoneScreenStatusCode;
	}

	public void setPhoneScreenStatusCode(String phoneScreenStatusCode)
	{
		this.phoneScreenStatusCode = phoneScreenStatusCode;
	}

	public TimeStampTO getPhoneScreenStatusTimestamp()
	{
		return phoneScreenStatusTimestamp;
	}

	public void setPhoneScreenStatusTimestamp(TimeStampTO phoneScreenStatusTimestamp)
	{
		this.phoneScreenStatusTimestamp = phoneScreenStatusTimestamp;
	}

	public TimeStampTO getInterviewStatusTime()
	{
		return interviewStatusTimestamp;
	}

	public void setInterviewStatusTime(TimeStampTO interviewStatusTimestamp)
	{
		this.interviewStatusTimestamp = interviewStatusTimestamp;

	}

	public TimeStampTO getInterviewMatStatusTime()
	{
		return interviewMaterialStatusTimestamp;
	}

	public void setInterviewMatStatusTime(TimeStampTO interviewMaterialStatusTimestamp)
	{
		this.interviewMaterialStatusTimestamp = interviewMaterialStatusTimestamp;
	}

	/**
	 * @return the emailAdd
	 */
	public String getEmailAdd()
	{
		return emailAdd;
	}

	/**
	 * @param emailAdd
	 *            the emailAdd to set
	 */
	public void setEmailAdd(String emailAdd)
	{
		this.emailAdd = emailAdd;
	}

	/**
	 * @return the offered
	 */
	public String getOffered()
	{
		return offered;
	}

	/**
	 * @param offered
	 *            the offered to set
	 */
	public void setOffered(String offered)
	{
		this.offered = offered;
	}

	/**
	 * @return the offerStatus
	 */
	public String getOfferStatus()
	{
		return offerStatus;
	}

	/**
	 * @param offerStatus
	 *            the offerStatus to set
	 */
	public void setOfferStatus(String offerStatus)
	{
		this.offerStatus = offerStatus;
	}

	@XStreamAlias("offerStatus")
	private String offerStatus;

	/**
	 * @param ynStatusDesc
	 *            the ynStatusDesc to set
	 */
	public void setYnStatusDesc(String ynStatusDesc)
	{
		this.ynStatusDesc = ynStatusDesc;
	}

	/**
	 * @return the ynStatusDesc
	 */
	public String getYnStatusDesc()
	{
		return ynStatusDesc;
	}

	/**
	 * @param contactHistoryTxt
	 *            the contactHistoryTxt to set
	 */
	public void setContactHistoryTxt(String contactHistoryTxt)
	{
		this.contactHistoryTxt = contactHistoryTxt;
	}

	/**
	 * @return the contactHistoryTxt
	 */
	public String getContactHistoryTxt()
	{
		return contactHistoryTxt;
	}

	/**
	 * @param minResList
	 *            the minResList to set
	 */
	public void setMinResList(List<MinimumResponseTO> minResList)
	{
		this.minResList = minResList;
	}

	/**
	 * @return the minResList
	 */
	public List<MinimumResponseTO> getMinResList()
	{
		return minResList;
	}

	/**
	 * @param cndDeptNbr
	 *            the cndDeptNbr to set
	 */
	public void setCndDeptNbr(String cndDeptNbr)
	{
		this.cndDeptNbr = cndDeptNbr;
	}

	/**
	 * @return the cndDeptNbr
	 */
	public String getCndDeptNbr()
	{
		return cndDeptNbr;
	}

	/**
	 * @param jobTtl
	 *            the jobTtl to set
	 */
	public void setJobTtl(String jobTtl)
	{
		this.jobTtl = jobTtl;
	}

	/**
	 * @return the jobTtl
	 */
	public String getJobTtl()
	{
		return jobTtl;
	}

	/**
	 * @param strNbr
	 *            the strNbr to set
	 */
	public void setCndStrNbr(String cndStrNbr)
	{
		this.cndStrNbr = cndStrNbr;
	}

	/**
	 * @return the strNbr
	 */
	public String getCndStrNbr()
	{
		return cndStrNbr;
	}

	public void setInterviewLocTypCd(String interviewLocTypCd)
	{
		this.interviewLocTypCd = interviewLocTypCd;
	}

	public String getInterviewLocTypCd()
	{
		return interviewLocTypCd;
	}

	/**
	 * @return the aid
	 */
	public String getAid()
	{
		return aid;
	}

	/**
	 * @param aid
	 *            the aid to set
	 */
	public void setAid(String aid)
	{
		this.aid = aid;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * @return the scrtyp
	 */
	public String getScrTyp()
	{
		return scrTyp;
	}

	/**
	 * @param scrtyp
	 *            the scrtyp to set
	 */
	public void setScrTyp(String scrTyp)
	{
		this.scrTyp = scrTyp;
	}

	/**
	 * @return the job
	 */
	public String getJob()
	{
		return job;
	}

	/**
	 * @param job
	 *            the job to set
	 */
	public void setJob(String job)
	{
		this.job = job;
	}

	public String getScreenNbr()
	{
		return ScreenNbr;
	}

	public void setScreenNbr(String screenNbr)
	{
		ScreenNbr = screenNbr;
	}

	/**
	 * @return the reqNbr
	 */
	public String getReqNbr()
	{
		return reqNbr;
	}

	/**
	 * @param reqNbr
	 *            the reqNbr to set
	 */
	public void setReqNbr(String reqNbr)
	{
		this.reqNbr = reqNbr;
	}

	/**
	 * @return the cndtNbr
	 */
	public String getCndtNbr()
	{
		return cndtNbr;
	}

	/**
	 * @param cndtNbr
	 *            the cndtNbr to set
	 */
	public void setCndtNbr(String cndtNbr)
	{
		this.cndtNbr = cndtNbr;
	}

	/**
	 * @return the ovrAllscrTyp
	 */
	public String getOvrAllscrTyp()
	{
		return ovrAllscrTyp;
	}

	/**
	 * @param ovrAllscrTyp
	 *            the ovrAllscrTyp to set
	 */
	public void setOvrAllscrTyp(String ovrAllscrTyp)
	{
		this.ovrAllscrTyp = ovrAllscrTyp;
	}

	public DateTO getPhnScrnDate()
	{
		return phnScrnDate;
	}

	public void setPhnScrnDate(DateTO phnScrnDate)
	{
		this.phnScrnDate = phnScrnDate;
	}

	public TimeStampTO getPhnScrnTime()
	{
		return phnScrnTime;
	}

	public void setPhnScrnTime(TimeStampTO phnScrnTime)
	{
		this.phnScrnTime = phnScrnTime;
	}

	/**
	 * @return the phnScreener
	 */
	public String getPhnScreener()
	{
		return phnScreener;
	}

	/**
	 * @param phnScreener
	 *            the phnScreener to set
	 */
	public void setPhnScreener(String phnScreener)
	{
		this.phnScreener = phnScreener;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the itiNbr
	 */
	public String getItiNbr()
	{
		return itiNbr;
	}

	/**
	 * @param itiNbr
	 *            the itiNbr to set
	 */
	public void setItiNbr(String itiNbr)
	{
		this.itiNbr = itiNbr;
	}

	/**
	 * @return the canPhn
	 */
	public String getCanPhn()
	{
		return canPhn;
	}

	/**
	 * @param canPhn
	 *            the canPhn to set
	 */
	public void setCanPhn(String canPhn)
	{
		this.canPhn = canPhn;
	}

	/**
	 * @return the ynstatus
	 */
	public String getYnstatus()
	{
		return ynstatus;
	}

	/**
	 * @param ynstatus
	 *            the ynstatus to set
	 */
	public void setYnstatus(String ynstatus)
	{
		this.ynstatus = ynstatus;
	}

	/**
	 * @return the overAllStatus
	 */
	public String getOverAllStatus()
	{
		return overAllStatus;
	}

	/**
	 * @param overAllStatus
	 *            the overAllStatus to set
	 */
	public void setOverAllStatus(String overAllStatus)
	{
		this.overAllStatus = overAllStatus;
	}

	/**
	 * @return the canStatus
	 */
	public String getCanStatus()
	{
		return canStatus;
	}

	/**
	 * @param canStatus
	 *            the canStatus to set
	 */
	public void setCanStatus(String canStatus)
	{
		this.canStatus = canStatus;
	}

	/**
	 * @return the internalExternal
	 */
	public String getInternalExternal()
	{
		return internalExternal;
	}

	/**
	 * @param internalExternal
	 *            the internalExternal to set
	 */
	public void setInternalExternal(String internalExternal)
	{
		this.internalExternal = internalExternal;
	}

	/**
	 * @return the aIMSStatus
	 */
	public String getAIMSDisp()
	{
		return AIMSDisp;
	}

	/**
	 * @param status
	 *            the aIMSStatus to set
	 */
	public void setAIMSDisp(String AIMSDisp)
	{
		this.AIMSDisp = AIMSDisp;
	}

	/**
	 * @return the detailTxt
	 */
	public String getDetailTxt()
	{
		return detailTxt;
	}

	/**
	 * @param detailTxt
	 *            the detailTxt to set
	 */
	public void setDetailTxt(String detailTxt)
	{
		this.detailTxt = detailTxt;
	}

	/**
	 * @return the nonHrgStoreLoc
	 */
	public String getNonHrgStoreLoc()
	{
		return nonHrgStoreLoc;
	}

	/**
	 * @param nonHrgStoreLoc
	 *            the nonHrgStoreLoc to set
	 */
	public void setNonHrgStoreLoc(String nonHrgStoreLoc)
	{
		this.nonHrgStoreLoc = nonHrgStoreLoc;
	}

	/**
	 * @return the hrgStoreLoc
	 */
	public String getHrgStoreLoc()
	{
		return hrgStoreLoc;
	}

	/**
	 * @param hrgStoreLoc
	 *            the hrgStoreLoc to set
	 */
	public void setHrgStoreLoc(String hrgStoreLoc)
	{
		this.hrgStoreLoc = hrgStoreLoc;
	}

	/**
	 * @return the hrgEvntLoc
	 */
	public String getHrgEvntLoc()
	{
		return hrgEvntLoc;
	}

	/**
	 * @param hrgEvntLoc
	 *            the hrgEvntLoc to set
	 */
	public void setHrgEvntLoc(String hrgEvntLoc)
	{
		this.hrgEvntLoc = hrgEvntLoc;
	}

	/**
	 * @return the othrLoc
	 */
	public String getOthrLoc()
	{
		return othrLoc;
	}

	/**
	 * @param othrLoc
	 *            the othrLoc to set
	 */
	public void setOthrLoc(String othrLoc)
	{
		this.othrLoc = othrLoc;
	}

	/**
	 * @return the intrLocDtls
	 */
	public IntrwLocationDetailsTO getIntrLocDtls()
	{
		return intrLocDtls;
	}

	/**
	 * @param intrLocDtls
	 *            the intrLocDtls to set
	 */
	public void setIntrLocDtls(IntrwLocationDetailsTO intrLocDtls)
	{
		this.intrLocDtls = intrLocDtls;
	}

	/**
	 * @param hrEvntFlg
	 *            the hrEvntFlg to set
	 */
	public void setHrEvntFlg(String hrEvntFlg)
	{
		this.hrEvntFlg = hrEvntFlg;
	}

	/**
	 * @return the hrEvntFlg
	 */
	public String getHrEvntFlg()
	{
		return hrEvntFlg;
	}

	public String getFillDt()
	{
		return fillDt;
	}

	public void setFillDt(String fillDt)
	{
		this.fillDt = fillDt;
	}

	public String getReqCalId()
	{
		return reqCalId;
	}

	public void setReqCalId(String reqCalId)
	{
		this.reqCalId = reqCalId;
	}

	public String getInterviewStatusDesc()
	{
		return interviewStatusDesc;
	}

	public void setInterviewStatusDesc(String interviewStatusDesc)
	{
		this.interviewStatusDesc = interviewStatusDesc;
	}

	public String getNamesOfInterviewers()
	{
		return namesOfInterviewers;
	}

	public void setNamesOfInterviewers(String namesOfInterviewers)
	{
		this.namesOfInterviewers = namesOfInterviewers;
	}

	public String getRscSchdFlg()
	{
		return rscSchdFlg;
	}

	public void setRscSchdFlg(String rscSchdFlg)
	{
		this.rscSchdFlg = rscSchdFlg;
	}

	public String getPhoneScreensCompletedFlg()
	{
		return phoneScreensCompletedFlg;
	}

	public void setPhoneScreensCompletedFlg(String phoneScreensCompletedFlg)
	{
		this.phoneScreensCompletedFlg = phoneScreensCompletedFlg;
	}

	/**
	 * @param SeqNbr
	 *            the seqNbr to set By SS
	 */
	public void setIntrvSeqNbr(String intrvSeqNbr)
	{
		this.intrvSeqNbr = intrvSeqNbr;
	}

	/**
	 * @return the seqNbr
	 */
	public String getIntrvSeqNbr()
	{
		return intrvSeqNbr;
	}

	public void setNote(PhoneScreenNoteTO note)
	{
		this.note = note;
	}

	public PhoneScreenNoteTO getNote()
	{
		return note;
	}

	/**
	 * Get the first name of the phone screen candidate
	 * 
	 * @return				The first name of the phone screen candidate
	 */
	public String getFirstName()
	{
		return mFirstName;
	} // end function getFirstName()
	
	/**
	 * Set the first name of the phone screen candidate
	 * 
	 * @param firstName		The first name
	 */
	public void setFirstName(String firstName)
	{
		mFirstName = firstName;
	} // end function setFirstName()
	
	/**
	 * Get the last name of the phone screen candidate
	 * 
	 * @return				The last name of the phone screen candidate
	 */
	public String getLastName()
	{
		return mLastName;
	} // end function getLastName()
	
	/**
	 * Set the last name of the phone screen candidate
	 * 
	 * @param lastName		The last name 
	 */
	public void setLastName(String lastName)
	{
		mLastName = lastName;
	} // end function setLastName()
	
	/**
	 * Get the alternative phone number for the phone screen candidate
	 * 
	 * @return				The alternative phone number for the phone screen candidate
	 */
	public String getPhone2()
	{
		return mPhone2;
	} // end function getPhone2()
	
	/**
	 * Set the alternative phone number for the phone screen candidate
	 * 
	 * @param phone2		The alternative phone number
	 */
	public void setPhone2(String phone2)
	{
		mPhone2 = phone2;
	} // end function setPhone2()
	
	/**
	 * Get the time zone of the phone screen candidate
	 * 
	 * @return				The time zone of the phone screen candidate
	 */
	public String getTimeZone()
	{
		return mTimeZone;
	} // end function getTimeZone()
	
	/**
	 * Set the time zone of the phone screen candidate
	 * 
	 * @param timezone		The time zone of the phone screen candidate
	 */
	public void setTimeZone(String timezone)
	{
		mTimeZone = timezone;
	} // end function getTimeZone()
	
	/**
	 * Get the language preference of the phone screen candidate
	 * 
	 * @return				The language preference of the phone screen candidate
	 */
	public String getLangPref()
	{
		return mLangPref;
	} // end function getLangPref()
	
	/**
	 * Set the language preference of the phone screen candidate
	 * 
	 * @param langPref		The language preference of the phone screen candidate
	 */
	public void setLangPref(String langPref)
	{
		mLangPref = langPref;
	} // end function setLangPref()
	
	/**
	 * Get the skill test id required by the job title of the requisition this phone screen candidate belongs to
	 * 
	 * @return				The skill test id required by the job title of the requisition this phone screen candidate belongs to
	 */
	public String getSkillType()
	{
		return mSkillType;
	} // end function getSkillType()
	
	/**
	 * Set the skill test id required by the job title of the requisition this phone screen candidate belongs to
	 * 
	 * @param skillType		The skill test id required by the job title of the requisition this phone screen candidate belongs to
	 */
	public void setSkillType(String skillType)
	{
		mSkillType = skillType;
	} // end function setSkillType()

	/**
	 * Get the status of the requisition this phone screen candidate belongs to
	 * 
	 * @return				The status of the requisition this phone screen candidate belongs to
	 */
	public String getReqStat()
	{
		return mReqStat;
	} // end function getReqStat()
	
	/**
	 * Set the status of the requisition this phone screen candidate belongs to
	 * 
	 * @param reqStat		The status of the requisition this phone screen candidate belongs to
	 */
	public void setReqStat(String reqStat)
	{
		mReqStat = reqStat;
	} // end function setReqStat()

	/**
	 * Get the manager ID for this phone screen candidate
	 * 
	 * @return				The manager ID for this phone screen candidate
	 */
	public String getMgrId()
	{
		return mMgrId;
	} // end function getMgrId()
	
	/**
	 * Set the manager ID for this phone screen candidate
	 * 
	 * @param mgrId			The manager ID for this phone screen candidate
	 */
	public void setMgrId(String mgrId)
	{
		mMgrId = mgrId;
	} // end function setMgrId()

	public String getCandRefNbr() {
		return candRefNbr;
	}

	public void setCandRefNbr(String candRefNbr) {
		this.candRefNbr = candRefNbr;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	//Start - Added as part of Flex to HTML Conversion - 13 May 2015
	/**
	 * @return formatted Phone Number
	 */
	public String getCanPhnFormatted() {
		return canPhnFormatted;
	}

	/**
	 * @param canPhnFormatted
	 */
	public void setCanPhnFormatted(String canPhnFormatted) {
		this.canPhnFormatted = canPhnFormatted;
	}
	//End - Added as part of Flex to HTML Conversion - 13 May 2015
	public short getPhoneScreenDispositionCode() {
		return phoneScreenDispositionCode;
	}

	public void setPhoneScreenDispositionCode(short phoneScreenDispositionCode) {
		this.phoneScreenDispositionCode = phoneScreenDispositionCode;
	}

	public TimeStampTO getPhoneScreenDispositionCodeTimestamp() {
		return phoneScreenDispositionCodeTimestamp;
	}

	public void setPhoneScreenDispositionCodeTimestamp(TimeStampTO phoneScreenDispositionCodeTimestamp) {
		this.phoneScreenDispositionCodeTimestamp = phoneScreenDispositionCodeTimestamp;
	}

	public SchdIntrvwChecksTO getSchdIntrvwChecks() {
		return schdIntrvwChecks;
	}

	public void setSchdIntrvwChecks(SchdIntrvwChecksTO schdIntrvwChecks) {
		this.schdIntrvwChecks = schdIntrvwChecks;
	}

	public String getPhoneScreenBannerNum() {
		return phoneScreenBannerNum;
	}

	public void setPhoneScreenBannerNum(String phoneScreenBannerNum) {
		this.phoneScreenBannerNum = phoneScreenBannerNum;
	}
}

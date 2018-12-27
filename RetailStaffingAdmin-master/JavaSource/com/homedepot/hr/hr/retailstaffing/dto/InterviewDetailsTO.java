package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class InterviewDetailsTO  implements Serializable {
	
	private static final long serialVersionUID = -7327961102498845435L;

	private int mPhoneScreenId;
	private String mStoreNumber;
	private String mEmplPosnCandId;
	private int mEmplReqNbr;
	private String mCandPersonId;
	private String mCandName;
	private String mCandPhoneNbr;
	private short mLocTypCd;
	private String mName;
	private Date mDate;
	private Timestamp mTimestamp;
	private String mLocDesc;
	private String mPhoneNbr;
	private String mAddress;
	private String mCity;
	private String mStateCd;
	private String mZipCd;
	private String mActvFlg;
	private short mInterviewMins;
	
	public int getPhoneScreenId() 
	{
		return mPhoneScreenId;
	} // end function getPhoneScreenId()
	
	public void setPhoneScreenId(int phoneScreenId)
	{
		mPhoneScreenId = phoneScreenId;
	} // end function setPhoneScreenId()
	
	public String getStoreNumber() 
	{
		return mStoreNumber;
	} // end function getStoreNumber()
	
	public void setStoreNumber(String storeNumber) 
	{
		mStoreNumber = storeNumber;
	} // end function setStoreNumber()
	
	public String getEmplPosnCandId() 
	{
		return mEmplPosnCandId;
	} // end function getEmplPosnCandId()
	
	public void setEmplPosnCandId(String emplPosnCandId) 
	{
		mEmplPosnCandId = emplPosnCandId;
	} // end function setEmplPosnCandId()
	
	public int getEmplReqNbr() 
	{
		return mEmplReqNbr;
	} // end function getEmplReqNbr()
	
	public void setEmplReqNbr(int emplReqNbr) 
	{
		mEmplReqNbr = emplReqNbr;
	} // end function setEmplReqNbr()
	
	public String getCandPersonId() 
	{
		return mCandPersonId;
	} // end function getCandPersonId()
	
	public void setCandPersonId(String candPersonId)
	{
		mCandPersonId = candPersonId;
	} // end function setCandPersonId()
	
	public String getCandName() 
	{
		return mCandName;
	} // end function getCandNaem()
	
	public void setCandName(String candName) 
	{
		mCandName = candName;
	} // end function setCandName()
	
	public String getCandPhoneNbr() 
	{
		return mCandPhoneNbr;
	} // end function getCandPhoneNbr()
	
	public void setCandPhoneNbr(String candPhoneNbr) 
	{
		mCandPhoneNbr = candPhoneNbr;
	} // end function setCandPhoneNbr()
	
	public short getLocTypCd() 
	{
		return mLocTypCd;
	} // end function getLocTypCd()
	
	public void setLocTypCd(short locTypCd) 
	{
		mLocTypCd = locTypCd;
	} // end function setLocTypCd()
	
	public String getName()
	{
		return mName;
	} // end function getName()
	
	public void setName(String name)
	{
		mName = name;
	} // end function setName()
	
	public Date getDate()
	{
		return mDate;
	} // end function getDate()
	
	public void setDate(Date date)
	{
		mDate = date;
	} // end function setDate()
	
	public Timestamp getTimestamp()
	{
		return mTimestamp;
	} // end function getTimestamp()
	
	public void setTimestamp(Timestamp timestamp)
	{
		mTimestamp = timestamp;
	} // end function setTimestamp()
	
	public String getLocDesc() 
	{
		return mLocDesc;
	} // end function getLocDesc()
	
	public void setLocDesc(String locDesc)
	{
		mLocDesc = locDesc;
	} // end function setLocDesc()
	
	public String getPhoneNbr() 
	{
		return mPhoneNbr;
	} // end function getPhoneNbr()
	
	public void setPhoneNbr(String phoneNbr)
	{
		mPhoneNbr = phoneNbr;
	} // end function setPhoneNbr()
	
	public String getAddress() 
	{
		return mAddress;
	} // end function getAddress()
	
	public void setAddress(String address) 
	{
		mAddress = address;
	} // end function setAddress()
	
	public String getCity() 
	{
		return mCity;
	} // end function getCity()
	
	public void setCity(String city)
	{
		mCity = city;
	} // end function setCity()
	
	public String getStateCd() 
	{
		return mStateCd;
	} // end function getStateCd()
	
	public void setStateCd(String stateCd) 
	{
		mStateCd = stateCd;
	} // end function setStateCd()
	
	public String getZipCd() 
	{
		return mZipCd;
	} // end function getZipCd()
	
	public void setZipCd(String zipCd)
	{
		mZipCd = zipCd;
	} // end function setZipCd()
	
	public String getActvFlg() 
	{
		return mActvFlg;
	} // end function getActvFlg()
	
	public void setActvFlg(String actvFlg) 
	{
		mActvFlg = actvFlg;
	} // end function setActvFlg()
	
	public short getInterviewMins()
	{
		return mInterviewMins;
	} // end function getInterviewMins()
	
	public void setInterviewMins(short interviewMins)
	{
		mInterviewMins = interviewMins;
	} // end function setInterviewMins()
}

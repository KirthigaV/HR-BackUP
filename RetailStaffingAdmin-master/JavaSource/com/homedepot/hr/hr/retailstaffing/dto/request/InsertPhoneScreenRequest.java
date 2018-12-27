/*
 * Created on December 05, 2010
 *
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 *
 * Application:RetailStaffing
 *
 * File Name: InsertPhoneScreenRequest.java
 */

package com.homedepot.hr.hr.retailstaffing.dto.request;

import java.util.List;
import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenMinReqTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("request")
public class InsertPhoneScreenRequest
{

	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("phoneScreenId")
	private String mPhoneScreenId;

	@XStreamAlias("questionsList")
	private List<PhoneScreenMinReqTO> mPhoneScreenMinReqTOList;

	public String getPhoneScreenId()
	{
		return mPhoneScreenId;
	}

	public void setphoneScreenId(String phoneScreenId)
	{
		mPhoneScreenId = phoneScreenId;
	}

	public List<PhoneScreenMinReqTO> getPhoneScreenMinReqTOList()
	{
		return mPhoneScreenMinReqTOList;
	}

	public void setPhoneScreenMinReqTOList(List<PhoneScreenMinReqTO> phoneScreenMinReqTOList)
	{
		mPhoneScreenMinReqTOList = phoneScreenMinReqTOList;
	}
	
	@Override
	public String toString()
	{
		StringBuilder data = new StringBuilder();
		
		data.append("InsertPhoneScreenRequest: phoneScreenId: ")
			.append(mPhoneScreenId)
			.append("Responses: ");
		
		if(mPhoneScreenMinReqTOList == null || mPhoneScreenMinReqTOList.size() == 0)
		{
			data.append((mPhoneScreenMinReqTOList == null ? "null" : "none"));			
		} // end if(mPhoneScreenMinReqTOList == null || mPhoneScreenMinReqTOList.size() == 0)
		else
		{
			for(PhoneScreenMinReqTO response : mPhoneScreenMinReqTOList)
			{
				data.append(response.toString()).append("  ");
			} // end for(PhoneScreenMinReqTO response : mPhoneScreenMinReqTOList)
		} // end else
		
		return data.toString();
	} // end function toString()
}

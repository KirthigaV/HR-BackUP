package com.homedepot.hr.hr.retailstaffing.dto.response;

import java.io.Serializable;
import java.util.List;

import com.homedepot.hr.hr.retailstaffing.dto.PhoneScreenIntrwDetailsTO;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


@XStreamAlias("CmplITIDetail")
public class CompleteITIDetailResponse implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamImplicit
	@XStreamAlias("PhoneScreenIntrwDetail")
	private List<PhoneScreenIntrwDetailsTO> itiDtlList;

	/**
	 * @param reqDtlList
	 *            the reqDtlList to set
	 */
	public void setITIDtlList(List<PhoneScreenIntrwDetailsTO> itiDtlList)
	{
		this.itiDtlList = itiDtlList;
	}

	/**
	 * @return the reqDtlList
	 */
	public List<PhoneScreenIntrwDetailsTO> getITIDtlList()
	{
		return itiDtlList;
	}



}

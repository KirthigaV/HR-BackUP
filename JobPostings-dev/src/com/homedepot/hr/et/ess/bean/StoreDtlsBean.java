package com.homedepot.hr.et.ess.bean;

import java.io.Serializable;

public class StoreDtlsBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String storeName;
	private String storeId;
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	

}

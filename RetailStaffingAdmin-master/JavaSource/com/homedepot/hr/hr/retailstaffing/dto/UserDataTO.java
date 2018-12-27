package com.homedepot.hr.hr.retailstaffing.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * This class is used as to send user info response in XML format
 * 
 * @author TCS
 * 
 */
@XStreamAlias("UserData")
public class UserDataTO implements Serializable
{
	private static final long serialVersionUID = 362498820763181265L;

	@XStreamAlias("firstName")
	private String firstName;
	
	@XStreamAlias("lastName")
	private String lastName;
	
	@XStreamAlias("userId")
	private String userId;
	
	@XStreamAlias("location")
	private String location;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

		
}

package com.homedepot.hr.hr.retailstaffing.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CookieUtils
{

	private static final Logger logger = Logger.getLogger(CookieUtils.class);

	public static Cookie getSSOCookie(HttpServletRequest request) throws Exception
	{
		Cookie[] cookie_jar = request.getCookies();
		Cookie thdSSO = null;
		
		try
		{	
			if (cookie_jar != null)
			{
				for (int i =0; i< cookie_jar.length; i++)
				{
					Cookie aCookie = cookie_jar[i];
					logger.debug("Name : " + aCookie.getName());
					logger.debug("Value: " + aCookie.getValue());
					if (aCookie.getName().equalsIgnoreCase("THDSSO")) {
						thdSSO = aCookie;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return thdSSO;
	}
}

/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: PropertyReader.java
 * Application: RetailStaffing
 *
 */
package com.homedepot.hr.hr.retailstaffing.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class will be used for reading the properties from the properties file.
 * This class will have methods for:- Getting the instance of class. Reading the
 * property from the properties file.
 * 
 * @author TCS
 * 
 */
public class PropertyReader
{

	private final static Logger logger = Logger.getLogger(PropertyReader.class);

	private static PropertyReader propertyReader = null;

	private static Properties props = null;

	private PropertyReader()
	{
		FileInputStream fis = null;
		String msgDataVal = null;
		try
		{
			synchronized (this)
			{
				if(props == null)
				{
					props = new Properties();
					URL url = PropertyReader.class
							.getResource("../resources/RetailStaffingProperties.properties");
					File fs = new File(url.getFile());
					logger.info("The file existence value is" + fs.exists());
					if(fs.exists())
					{
						fis = new FileInputStream(fs);
						props.load(fis);

					}
				}	
			}
		}
		catch (IOException e)
		{
			msgDataVal = "Exception : Error occured in PropertyReader";
			Util.logFatalError(msgDataVal,e);
		}
        finally
        {
        	if(fis!=null)
        	{
        		try
				{
					fis.close();
				}
				catch (IOException e)
				{
					msgDataVal = "Exception : Error occured in PropertyReader";
					Util.logFatalError(msgDataVal,e);
				}	
        	}
        }
	}

	public static PropertyReader getInstance()
	{
		
		if(propertyReader == null)
		{
			propertyReader = new PropertyReader();
		}
		return propertyReader;
	}

	public String getProperty(String key)
	{
		String val = null;
		val = props.getProperty(key);
		return val;
	}

}

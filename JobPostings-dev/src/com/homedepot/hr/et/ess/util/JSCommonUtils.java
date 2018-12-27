package com.homedepot.hr.et.ess.util;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Element;


/**
 * This class is used for implementing the common functionalities which will
 * support the business functionality.
 * 
 * @author HP
 */
public class JSCommonUtils {

	

	/**
	 * A static method converts the Element passed to a String
	 * 
	 * @param input
	 *            The Element value to be converted.
	 */
	public static String elementToString(Element el) {
		StringBuilder stringBuilder = null;
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			XMLSerializer serializer = new XMLSerializer();
			serializer.setOutputByteStream(stream);
			serializer.asDOMSerializer();
			serializer.serialize(el);

			stringBuilder = new StringBuilder(stream.toString());
		} catch (Exception except) {
		}

		return stringBuilder.toString();
	}
	/**
	 * Sets headers in the response that tend to discourage browser caching.
	 */
	public static void discourageCache(HttpServletResponse response) {
		Date now = new Date();
		response.setDateHeader("Expires", now.getTime());
		response.setDateHeader("Last-Modified", now.getTime());
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "must-revalidate");
		response.setHeader("Cache-Control", "max-age=0");
		response.setHeader("pragma", "no-cache");
	}
	}

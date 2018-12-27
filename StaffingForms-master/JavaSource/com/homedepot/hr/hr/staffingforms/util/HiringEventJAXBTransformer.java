package com.homedepot.hr.hr.staffingforms.util;

/*
 * This program is proprietary to The Home Depot and is not to be reproduced,
 * used, or disclosed without permission of:
 * 
 *    The Home Depot
 *    2455 Paces Ferry Road, NW
 *    Atlanta, GA 30339-4024
 */


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.exceptions.HiringEventException;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.interfaces.DAOConstants;
import com.homedepot.ta.aa.log4j.SimpleExceptionLogger;
import com.sun.xml.bind.marshaller.CharacterEscapeHandler;




public class HiringEventJAXBTransformer implements Constants, DAOConstants{
	private static final Logger logger = Logger
			.getLogger(HiringEventJAXBTransformer.class);

	/**
	 * This method is used for producing XML based on request
	 * 
	 * @param <T>
	 * @param obj
	 * @param clazz
	 * @return String
	 * @throws HiringEventException
	 */
	public static <T> String marshall(Object obj, Class<T> clazz)
			throws HiringEventException {
		if (logger.isDebugEnabled()) {
			logger.debug("Start marshall(Object,Class) in IVRJAXBTransformer ");
		}
		StringWriter strWriter = new StringWriter();
		String returnString = null;

		try {
			JAXBContext ctxObj = JAXBContext.newInstance(Class.forName(clazz
					.getName()));
			Marshaller marshaller = ctxObj.createMarshaller();

			marshaller.setProperty(
					"com.sun.xml.bind.characterEscapeHandler",
					new CharacterEscapeHandler() {
						@Override
						public void escape(char[] ac, int i, int j,
								boolean flag, Writer writer) throws IOException {
							// do not escape
							writer.write(ac, i, j);
						}

					});

			marshaller.marshal(obj, strWriter);

			returnString = strWriter.getBuffer().toString();

		} catch (Exception e) {
			SimpleExceptionLogger.log(e, "HiringEvent");
			throw new HiringEventException(SEVERITY_HIGH,
					HTTP_STATUS_ERROR, ValidationUtils.getCurrentDate(),
					HIRING_EVENT_JAXB_MARSHALL_ERROR_DESC, e,
					HIRING_EVENT_JAXB_MARSHALL_ERROR_DESC
					);
		} finally {
			try {
				if (strWriter != null) {
					strWriter.close();
				}

			} catch (IOException exception) {
				logger.warn(exception);
			}
			/**
			 * No need to throw this exception to manager layer because this is
			 * only warning error
			 */
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finish marshall(Object,Class) in IVRJAXBTransformer - returning : "
					+ returnString);
		}
		return returnString;
	}

	/**
	 * This method is used for creating object from xml
	 * 
	 * @param xml
	 * @param clazz
	 * @return
	 * @throws HiringEventException
	 */
	public static <T> T unMarshall(String xml, Class<T> clazz)
			throws HiringEventException {
		if (logger.isDebugEnabled()) {
			logger.debug("Start unMarshall(String,Class) in IVRJAXBTransformer :");
		}

		T obj = null;
		StringReader stringReader = null;

		try {
			JAXBContext ctxObj = JAXBContext.newInstance(Class.forName(clazz
					.getName()));
			Unmarshaller unmarshaller = ctxObj.createUnmarshaller();
			stringReader = new StringReader(xml);
			obj = clazz.cast(unmarshaller.unmarshal(stringReader));
			
		} catch (Exception e) {
			SimpleExceptionLogger.log(e, "HiringEvent");
			throw new HiringEventException(SEVERITY_HIGH,
					HTTP_STATUS_ERROR, ValidationUtils.getCurrentDate(),
					HIRING_EVENT_JAXB_UNMARSHALL_ERROR_DESC, e,
					HIRING_EVENT_JAXB_UNMARSHALL_ERROR_DESC);
		} finally {
			if (stringReader != null) {
				stringReader.close();
			}
			
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finish unMarshall(String,Class) in IVRJAXBTransformer - returning: "
					+ obj);
		}
		return obj;
	}

	/**
	 * 
	 * @param obj
	 * @param jaxbPkgName
	 * @return
	 * @throws HiringEventException
	 */
	public static String marshall(Object obj, String jaxbPkgName)
			throws HiringEventException {
		if (logger.isDebugEnabled()) {
			logger.debug("Start marshall(Object,String) in IVRJAXBTransformer :");
		}

		String returnStringXML = null;

		try {
			JAXBContext ctxObj = JAXBContext.newInstance(jaxbPkgName);

			Marshaller marshObj = ctxObj.createMarshaller();
			StringWriter strWriter = new StringWriter();
			marshObj.setProperty(
					"com.sun.xml.bind.characterEscapeHandler",
					new CharacterEscapeHandler() {
						@Override
						public void escape(char[] ac, int i, int j,
								boolean flag, Writer writer) throws IOException {
							// do not escape
							writer.write(ac, i, j);
						}

					});
			marshObj.marshal(obj, strWriter);
			returnStringXML = strWriter.getBuffer().toString();

		} catch (Exception e) {
			SimpleExceptionLogger.log(e, "HiringEvent");
			throw new HiringEventException(SEVERITY_HIGH,
					HTTP_STATUS_ERROR, ValidationUtils.getCurrentDate(),
					HIRING_EVENT_JAXB_MARSHALL_ERROR_DESC, e,
					HIRING_EVENT_JAXB_MARSHALL_ERROR_DESC
					);
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finish marshall(Object,String) in IVRJAXBTransformer - returning:"
					+ returnStringXML);
		}
		return returnStringXML;
	}

	/**
	 * 
	 * @param searchRequestTypeXml
	 * @param jaxbPkgName
	 * @return
	 * @throws HiringEventException
	 */
	@SuppressWarnings("unchecked")
	public static Object unMarshall(String searchRequestTypeXml,
			String jaxbPkgName) throws HiringEventException {

		if (logger.isDebugEnabled()) {
			logger.debug("Start unMarshallObject(String,String) in IVRJAXBTransformer :");
		}

		Object obj = null;
		try {
			JAXBContext ctxObj = JAXBContext.newInstance(jaxbPkgName);
			StringReader ir = new StringReader(searchRequestTypeXml);
			Unmarshaller unmarshaller = ctxObj.createUnmarshaller();
			obj = ((JAXBElement<Object>) unmarshaller.unmarshal(ir)).getValue();

		} catch (JAXBException e) {
			SimpleExceptionLogger.log(e, "HiringEvent");
			throw new HiringEventException(SEVERITY_HIGH,
					HTTP_STATUS_ERROR, ValidationUtils.getCurrentDate(),
					HIRING_EVENT_JAXB_UNMARSHALL_ERROR_DESC, e,
					HIRING_EVENT_JAXB_UNMARSHALL_ERROR_DESC);
		
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Finish unMarshall(String,String) in IVRJAXBTransformer - returning: "
					+ obj);
		}
		return obj;
	}
}

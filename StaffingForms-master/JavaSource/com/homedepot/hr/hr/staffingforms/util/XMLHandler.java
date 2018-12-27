package com.homedepot.hr.hr.staffingforms.util;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: XMLHandler.java
 * Application: RetailStaffing
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Logger;

import com.homedepot.hr.hr.staffingforms.dto.ApplicationData;
import com.homedepot.hr.hr.staffingforms.dto.AvailabilityBlock;
import com.homedepot.hr.hr.staffingforms.dto.DaySummary;
import com.homedepot.hr.hr.staffingforms.dto.HiringEventDetail;
import com.homedepot.hr.hr.staffingforms.dto.HiringEventModifyAvailability;
import com.homedepot.hr.hr.staffingforms.dto.Requisition;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionCalendar;
import com.homedepot.hr.hr.staffingforms.dto.RequisitionSchedule;
import com.homedepot.hr.hr.staffingforms.dto.StatusSlotSummary;
import com.homedepot.hr.hr.staffingforms.dto.Store;
import com.homedepot.hr.hr.staffingforms.interfaces.Constants;
import com.homedepot.hr.hr.staffingforms.interfaces.XMLConstants;
import com.homedepot.hr.hr.staffingforms.service.request.CalendarRequest;
import com.homedepot.hr.hr.staffingforms.service.request.CreateHiringEventRequest;
import com.homedepot.hr.hr.staffingforms.service.request.DeleteParticipatingStoreRequest;
import com.homedepot.hr.hr.staffingforms.service.request.UpdateHiringEventRequest;
import com.homedepot.hr.hr.staffingforms.service.response.CalendarResponse;
import com.homedepot.hr.hr.staffingforms.service.response.HiringEventResponse;
import com.homedepot.hr.hr.staffingforms.service.response.RequisitionResponse;
import com.homedepot.hr.hr.staffingforms.service.response.StoreResponse;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

/**
 * Contains methods to serialize/deserialize objects using the XStream API
 */
public class XMLHandler implements Constants, XMLConstants
{
	// logger instance
	private static final Logger mLogger = Logger.getLogger(XMLHandler.class);
	
	// default encoding of the XMLs generated
	private static final String XML_ENCODING = "UTF-8";
	
	// XStream API object that will be used to serialize/deserialize objects
	private static XStream mHandler;
	
	static
	{
		// initialize the handler
		mHandler = new XStream(new DomDriver(XML_ENCODING)
		{
			/*
			 * (non-Javadoc)
			 * @see com.thoughtworks.xstream.io.xml.DomDriver#createWriter(java.io.Writer)
			 */
			@Override
			public HierarchicalStreamWriter createWriter(Writer writer)
			{
				// override this to return an instance of my writer instead of the default
				return new CDATAWriter(writer);
			} // end function createWriter()
			
			/*
			 * (non-Javadoc)
			 * @see com.thoughtworks.xstream.io.xml.DomDriver#createReader(java.io.Reader)
			 */
			@Override
			public HierarchicalStreamReader createReader(Reader reader)
			{			
				long startTime = 0;
				HierarchicalStreamReader xmlReader = null;
				StringReader filteredReader = null;
				
				if(mLogger.isDebugEnabled())
				{
					startTime = System.nanoTime();
				} // end if
				
				try
				{
					//----------
					// first read the input XML into a StringWriter
					//----------					
					StringWriter xmlWriter = new StringWriter();
					
					char[] chars = new char[2048];
					int charsRead = 0;
					// iterate and read the input data, write the characters read to the StringWriter
					while((charsRead = reader.read(chars, 0, chars.length)) != -1)
					{
						xmlWriter.write(chars, 0, charsRead);
					} // end while((charsRead = reader.read(chars, 0, chars.length)) != -1)

					//---------
					// now filter the input XML removing any non-standard characters
					//---------
					String inputXml = xmlWriter.toString();
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Filtering Input XML\n%1$s", inputXml));
					} // end if

					// filter out any non-standard special characters (null check in utility method)
					inputXml = StringUtils.filterSpecialCharacters(inputXml);
					
					if(mLogger.isDebugEnabled())
					{
						mLogger.debug(String.format("Filtered Input XML\n%1$s", inputXml));
					} // end if
					//----------
					// now use the filtered input XML to create a new StringReader
					//----------
					filteredReader = new StringReader(inputXml);
					// invoke the super classes createReader passing in the StringReader created using the filtered XML
					xmlReader = super.createReader(filteredReader);
				} // end try
				catch(IOException ioe)
				{
					// log the exception that occurred
					mLogger.error("An exception occurred filtering input XML", ioe);
					
					try
					{
						// reset the reader
						reader.reset();						
						// invoke the parent's method
						xmlReader = super.createReader(reader);
					} // end try
					catch(IOException ignore)
					{
						// nothing we can do here
					} // end catch
					// set the 
				} // end catch()
				finally
				{
					// close the FilteredReader if it was created
					if(filteredReader != null)
					{
						filteredReader.close();
					} // end if(filteredReader != null)
				} // end finally
				
				if(mLogger.isDebugEnabled())
				{
					long endTime = System.nanoTime();
					
					if(startTime == 0)
					{
						startTime = endTime;
					} // end if(startTime == 0)
					
					mLogger.debug(String.format("Total time to filter input XML: %1$.9f seconds",
						(((double)endTime - startTime) / NANOS_IN_SECOND)));
				} // end if
				
				return xmlReader;
			} // end function createReader()
			
			/*
			 * (non-Javadoc)
			 * @see com.thoughtworks.xstream.io.xml.DomDriver#createReader(java.io.InputStream)
			 */
			@Override
			public HierarchicalStreamReader createReader(InputStream xml)
			{				
				InputStreamReader inputReader = null;
				HierarchicalStreamReader reader = null;
				
				try
				{
					// create an InputStreamReader from the input XML
					inputReader = new InputStreamReader(xml);
					// invoke the other createReader method overridden in this class (so logic isn't duplicated)
					reader = this.createReader(inputReader);
				} // end try
				finally
				{
					// close the InputStreamReader if it was created
					if(inputReader != null)
					{
						try
						{
							inputReader.close();
						} // end try
						catch(IOException ignore)
						{
							// ignore
						} // end catch
					} // end if(inputReader != null)
				} // end finally
				
				return reader;
			} // end function createReader()
		}); // end initialization

		// disable references
		mHandler.setMode(XStream.NO_REFERENCES);
		
		// add the classes this XStream object can marshal/unmarshal
		//==========
		// Transfer Objects
		//==========
		mHandler.processAnnotations(AvailabilityBlock.class);
		mHandler.processAnnotations(DaySummary.class);
		mHandler.processAnnotations(Requisition.class);
		mHandler.processAnnotations(RequisitionCalendar.class);
		mHandler.processAnnotations(RequisitionSchedule.class);
		mHandler.processAnnotations(StatusSlotSummary.class);
		mHandler.processAnnotations(Store.class);
		mHandler.processAnnotations(ApplicationData.class);	
		mHandler.processAnnotations(HiringEventDetail.class);
		mHandler.processAnnotations(HiringEventModifyAvailability.class);

		//==========
		// Response Objects
		//==========
		mHandler.processAnnotations(CalendarResponse.class);
		mHandler.processAnnotations(RequisitionResponse.class);
		mHandler.processAnnotations(StoreResponse.class);
		mHandler.processAnnotations(HiringEventResponse.class);
		
		//==========
		// Request Objects
		//==========
		mHandler.processAnnotations(CalendarRequest.class);
		mHandler.processAnnotations(CreateHiringEventRequest.class);
		mHandler.processAnnotations(DeleteParticipatingStoreRequest.class);
		mHandler.processAnnotations(UpdateHiringEventRequest.class);
		
	} // end static block
	
	/**
	 * Use the XStream API to deserialize the object provided into XML format
	 * 
	 * @param objectToDeserialize				The object to deserialize
	 * 
	 * @return									XML representation of the object provided
	 * 
	 * @throws XStreamException					Thrown if XStream fails to deserialize the object provided (null, etc.)
	 */
	public static String toXML(Object objectToDeserialize) throws XStreamException
	{
		// next use the XStream API to deserialize the object into an XML and return it
		return mHandler.toXML(objectToDeserialize);
	} // end toXML()
	
	/**
	 * Use the XStream API to serialize an Object from the XML String provided
	 * 
	 * @param xml									XML String to serialize into an object
	 * 
	 * @return										Object serialized from the XML String provided
	 * 
	 * @throws XStreamException						Thrown if XStream fails to serialize the object (i.e. null, data type mismatch, etc.)
	 */
	public static Object fromXML(String xml) throws XStreamException
	{
		// use the XStream object to serialize the XML into an object
		return mHandler.fromXML(xml);
	} // end function fromXML()
	
	/**
	 * Use the XStream API to serialize an Object from the XML String provided
	 * 
	 * @param xmlStream								InputStream object containing the XML to be parsed
	 * 
	 * @return										Object serialized from the XML String provided
	 * 
	 * @throws XStreamException						Thrown if XStream fails to serialize the object (i.e. null, data type mismatch, etc.)
	 */
	public static Object fromXML(InputStream xmlStream) throws XStreamException
	{
		// use the XStream object to serialize the XML into an object
		return mHandler.fromXML(xmlStream);		
	} // end function fromXML()
	
	/**
	 * This class does not escape the < or > characters IF they are contained within a CDATA XML element
	 */
	private static class CDATAWriter extends PrettyPrintWriter
	{
		/*
		 * (non-Javadoc)
		 * @see com.thoughtworks.xstream.io.xml.PrettyPrintWriter#PrettyPrintWriter(Writer)
		 */
		public CDATAWriter(Writer writer)
		{
			super(writer);
		} // end constructor()
		
		/*
		 * (non-Javadoc)
		 * @see com.thoughtworks.xstream.io.xml.PrettyPrintWriter#writeText(com.thoughtworks.xstream.core.util.QuickWriter, java.lang.String)
		 */
		@Override
		protected void writeText(QuickWriter writer, String text)
		{
			// if a non-null text value was provided
			if(text != null)
			{
				// check to see if the text contains CDATA tags, if so we'll write it as is
				if(text.indexOf(CDATA_START_TAG) != -1)
				{
					writer.write(text);
				} // end if(text.indexOf(CDATA_START_TAG) != -1)
				else // if there is no CDATA tag, we'll let the base class write it
				{
					super.writeText(writer, text);
				} // end else
			} // end if(text != null)
		} // end function writeText()
	} // end class CDATAWriter	
} // end class XMLHandler
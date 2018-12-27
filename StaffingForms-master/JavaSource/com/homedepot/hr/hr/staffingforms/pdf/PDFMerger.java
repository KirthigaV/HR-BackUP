package com.homedepot.hr.hr.staffingforms.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFMergerUtility;

public class PDFMerger {
	
	private static PDFMerger instance;
	
	public static PDFMerger getInstance() {
		if (isNull(instance)) {
			synchronized (PDFMerger.class) {
				/*
				 * Perform a second check to make sure the singleton didn't get
				 * initialized while this thread was queued to enter the
				 * synchronized block
				 */
				if (instance == null) {
					instance = new PDFMerger();
				}
			}
		}
		return instance;
	}
	
	public static void mergePDF(InputStream inp,ByteArrayOutputStream httpResponse)
	{
		PDFMergerUtility merger = new PDFMergerUtility();
		       
		merger.addSource(inp);
		try {
			
			merger.setDestinationStream(httpResponse);
			merger.mergeDocuments();
			
		} catch (COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return httpResponse;
	}
	
	public static void appendPDFDocument()
	{
		PDFMergerUtility fmerger = new PDFMergerUtility();
		PDDocument doc1=null;
		PDDocument doc2=null;
		
        File f = new File("C://ATS//HiringEvents//SamplePDFS//FinalPDF.pdf");
		
		if(!f.exists())
		{
			fmerger.addSource("C://ATS//HiringEvents//SamplePDFS//printApplicantProfile.pdf");
			fmerger.setDestinationFileName("C://ATS//HiringEvents//SamplePDFS//FinalPDF.pdf");
			try {
				fmerger.mergeDocuments();
			} catch (COSVisitorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		else
		{
		
		try {
			doc1= PDDocument.load("C://ATS//HiringEvents//SamplePDFS//printApplicantProfile.pdf");
			doc2=PDDocument.load("C://ATS//HiringEvents//SamplePDFS//FinalPDF.pdf");
			fmerger.appendDocument(doc2,doc1);
			doc2.save("C://ATS//HiringEvents//SamplePDFS//FinalPDF.pdf");
			
			if(doc1!=null)
			{
				doc1.close();
			}
			if(doc2!=null)
			{
				doc2.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (COSVisitorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}
	}
	
	
	public static ByteArrayOutputStream appendPDFDocumentStream(InputStream inp,ByteArrayOutputStream tempResponse)
	{
		PDFMergerUtility fmerger = new PDFMergerUtility();
		PDDocument doc1=null;
		PDDocument doc2=null;
		ByteArrayInputStream input=null;
        
		if(tempResponse==null)
		{
			tempResponse=new ByteArrayOutputStream();
			//fmerger.addSource("C://ATS//HiringEvents//SamplePDFS//printApplicantProfile.pdf");
			fmerger.addSource(inp);
			fmerger.setDestinationStream(tempResponse);
			try {
				fmerger.mergeDocuments();
			} catch (COSVisitorException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		else
		{
		
		try {
//			//ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(httpResponse.toString().getBytes()));
//			ByteArrayInputStream ois=new ByteArrayInputStream(tempResponse.toByteArray());
//			doc1= PDDocument.load("C://ATS//HiringEvents//SamplePDFS//printApplicantProfile.pdf");
//			//doc1= PDDocument.load(inp);
//			doc2=PDDocument.load(ois);
//			fmerger.appendDocument(doc2,doc1);
			
//			if(doc2!=null)
//			{
//			doc2.save(tempResponse);
//			doc2.save("C://ATS//HiringEvents//SamplePDFS//FinalPDF.pdf");
//			}
//			
//			
//			if(doc1!=null)
//			{
//				doc1.close();
//			}
//			if(doc2!=null)
//			{
//				doc2.close();
//			}
			
       		ByteArrayInputStream ois=new ByteArrayInputStream(tempResponse.toByteArray());
       		//fmerger.addSource("C://ATS//HiringEvents//SamplePDFS//printApplicantProfile.pdf");
       		fmerger.addSource(inp);
       		fmerger.addSource(ois);
       		tempResponse=new ByteArrayOutputStream();
       		fmerger.setDestinationStream(tempResponse);
       		fmerger.mergeDocuments();
		
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (COSVisitorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		}
		
		catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//	
		}
		}
		return tempResponse;
	}
	
	/**
	 * This method checks if input string is null
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNull(Object obj) {
		return null == obj ? true : false;
	}

}

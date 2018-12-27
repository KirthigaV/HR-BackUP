
package com.homedepot.hr.hr.staffingforms.pdf;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.log4j.Logger;

import com.etymon.pjx.PdfFormatException;
import com.etymon.pjx.PdfInputFile;
import com.etymon.pjx.PdfManager;
import com.etymon.pjx.PdfReader;
import com.etymon.pjx.PdfWriter;
import com.etymon.pjx.util.PdfAppender;


/**
 *  Helper class for creating PDF documents using Apache FOP API.
 *  
 * NOTE:  In order for PDF generation to work, xalan-2.7.0.jar and xercesImpl-2.7.1.jar must be included in the project
 * path despite the show errors.
 * @author CHRIS STANN
 */
public class PDFHelper{


	private Logger logger = Logger.getLogger(this.getClass());
 

    /**
     * Creates a byte array of PDF file from XML data and XSL style sheet
     * @param xml XML data
     * @param xsl XSL Style Sheet
     * @throws Exception
     * 
     */
    public  byte[] createPdf(String xml, String xsl) throws Exception
    {
//    	System.out.println(xml);
//    	System.out.println(xsl);

    	// setup FOP
        
        // configure fopFactory as desired
        FopFactory fopFactory = FopFactory.newInstance();
        // configure foUserAgent as desired
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        // Setup output
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        // Setup XML input
        Source src = new StreamSource(new StringReader(xml));
        // Setup XSLT
        Source xsltSrc = new StreamSource(new StringReader(xsl));
        
        //Make sure the XSL transformation's result is piped through to FOP
        Result res = null;
		// Construct fop with desired output format
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, outStream);
		res = new SAXResult(fop.getDefaultHandler());
			
        // Setup Transformer
		Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSrc);
	    // Set the value of a <param> in the stylesheet
	    transformer.setParameter("versionParam", "2.0");
		// Start the transformation and rendering process
		transformer.transform(src, res);
		
       
        byte[] content = outStream.toByteArray();
       
        // close the pdf file
        return content;
       
    }
    
  
   
	/**
	 * Appends multiple PDFs into one
	 * @param alFiles - ArrayList that contains PDF files to be appended
	 * @return - returns a byte array
	 */
	public  byte[] appendPdfFiles(ArrayList alFiles)
	{	
		 ByteArrayOutputStream os = new ByteArrayOutputStream();
		 try 
		 {
	       List<PdfManager> m = new ArrayList<PdfManager>();
	        
           
	       for(int i=0; i<alFiles.size();i++)
	       {
				m.add( new PdfManager(new PdfReader(new PdfInputFile( new File((String)alFiles.get(i))))) );
	       }
	
	       //create byte array from combined pdf files
	       PdfWriter w = new PdfWriter(os);
	       PdfAppender a = new PdfAppender(m, w);
	       a.append();
	       w.close();
	       os.close();
		} catch (PdfFormatException e) {
		    logger.error("appendPdfFiles Pdf Format Exception", e);
		} catch (IOException e) {
		    logger.error("appendPdfFiles IOException ", e);
		}
	       return os.toByteArray();
	 }


	/**
	 * Reads a file in the file system and returns a String containing text of that file.  
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public  String readXmlFile(String filename) {
		StringBuffer bs = new StringBuffer();
		File f = new File(filename);
		try {
			if (f.canRead()){
				FileReader fr = new FileReader(f);
				BufferedReader br = new BufferedReader(fr);
				String s;
				while((s = br.readLine()) != null) {
					bs.append(s);
					}
				fr.close();
			}
		} catch (FileNotFoundException e) {
			logger.error("getXMLString File Not Found", e);
		} catch (IOException e) {
			logger.error("getXMLString IOException: ",e);
		}
		return bs.toString();
	}
	/**
	 * Convenience method designed to facilitate reading an XSL style sheet which is 
	 * generally stored in a sub-folder of the WEB-INF directory.
	 * In a nutshell, it reads a resource as a text file and returns a String containing that text.
	 * 	 * 
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	public String getXMLString(String filename)  throws Exception{
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename);
		StringBuffer sb = new StringBuffer();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		String text = "";
		
		while ((text = reader.readLine()) != null) {
			sb.append(text);
		}
		
		return sb.toString();
	}
	
	
}




 
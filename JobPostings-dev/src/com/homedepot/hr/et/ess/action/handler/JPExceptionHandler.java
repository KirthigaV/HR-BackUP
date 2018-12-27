package com.homedepot.hr.et.ess.action.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

/**
 * @author HP
 * This class handles all the uncaught exceptions.
 */


public class JPExceptionHandler extends ExceptionHandler{
	
	private String CLASSNAME = "JPExceptionHandler";
	
	/* (non-Javadoc)
	 * @see org.apache.struts.action.ExceptionHandler#execute(java.lang.Exception, org.apache.struts.config.ExceptionConfig, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * This method is invoked by the Struts Exception Handler
	 */
	public ActionForward execute(
		Exception arg0,
		ExceptionConfig arg1,
		ActionMapping arg2,
		ActionForm arg3,
		HttpServletRequest arg4,
		HttpServletResponse arg5)
		throws ServletException {
		
		final String METHODNAME = "execute";
		//traceLogger.info("CLASS: "+ CLASSNAME+ "; METHOD: "+ METHODNAME+ ";Entering Method");
		//traceLogger.error("CLASS: "+ CLASSNAME+ "; METHOD: "+ METHODNAME+ ";The exception is "+arg0.toString());
		//arg0.printStackTrace();
		//traceLogger.info("CLASS: "+ CLASSNAME+ "; METHOD: "+ METHODNAME+ ";Exiting Method");
		return super.execute(arg0, arg1, arg2, arg3, arg4, arg5);
	}

   /* (non-Javadoc)
 * @see org.apache.struts.action.ExceptionHandler#logException(java.lang.Exception)
 */
	protected void logException(Exception arg0) {
		
		super.logException(arg0);
	}
	
	


}

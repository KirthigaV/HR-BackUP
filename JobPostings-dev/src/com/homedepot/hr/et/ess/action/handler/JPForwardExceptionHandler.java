package com.homedepot.hr.et.ess.action.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * @author HP
 * This class will forward the request based on the value in the session
 */


public class JPForwardExceptionHandler extends Action {
    	private String CLASSNAME = "JPForwardExceptionHandler";
        public JPForwardExceptionHandler() {
            super();
        }
    
        /**
             * initialize() 
             * @param mapping
             * @param form
             * @param request
             * @param response
             * @return
             * @throws Exception
             */
        public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
            final String METHODNAME = "execute";
          //  traceLogger.info( "CLASS: "+ CLASSNAME+ "; METHOD: " + METHODNAME + "; Entering method->");
				String handler = null;     
				//UserProfile user =(UserProfile) request.getSession().getAttribute(UserProfile.KEY);
				
				handler = "noleftnav";	
				
           
           ActionForward forward = new ActionForward();
           forward=mapping.findForward(handler);
           //traceLogger.info( "CLASS: " + CLASSNAME+ "; METHOD: " + METHODNAME+ "; Exiting method");
                return forward;
            }
}

package com.homedepot.hr.et.ess.util;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.homedepot.hr.et.ess.action.jobposting.JobPostingAction;

/**
* ApplicationCallbackHandler
*/
public class ApplicationCallbackHandler implements CallbackHandler 
{
	/** store the request object for use in the handle method */
	private HttpServletRequest request = null;
	private static final Logger logger = Logger.getLogger( ApplicationCallbackHandler.class );
	/**
	 * Constructor
	 */
	public ApplicationCallbackHandler(HttpServletRequest request) 
	{
		this.request = request;
	}

	/**
	 * Look for a NameCallback and pass the user id to it
	 * 
	 * @see javax.security.auth.callback.CallbackHandler#handle(Callback[])
	 */
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException 
	{
		logger.debug("##ApplicationCallbackHandler##");
		for (int i = 0; i < callbacks.length; i++) 
		{
			// look for a NameCallback
			if (callbacks[i] instanceof NameCallback) 
			{
				NameCallback nameCallBack = (NameCallback) callbacks[i];
				if ( "userId".equalsIgnoreCase(nameCallBack.getPrompt()))
				{
				logger.debug("request.getRemoteUser()....>"+request.getRemoteUser());
				nameCallBack.setName(request.getRemoteUser());
				}//user id callback handler
				else if ( "storeNbr".equalsIgnoreCase(nameCallBack.getPrompt()) )
				{
				boolean found = false;
				Cookie[] cookies = request.getCookies();
				if ( cookies != null )
				{
				for ( int c = 0; c < cookies.length; c++ )
				{
				logger.debug("cookies[c].getValue()....>"+cookies[c].getValue());
				if ( cookies[c].getName().equalsIgnoreCase("storeNumber") )
				{
				nameCallBack.setName(cookies[c].getValue());
				found = true;
				}//if the storeNumber cookie
				}//for each cookie
				}//cookies found
				if ( !found )
				{
				nameCallBack.setName("%23%23%23%23");
				}//no cookie found, set it to corporate
				}//else storeNbr callback handler }//if callback handlers are of type namecallback 
				
			} 
			
		}
		
	}
}
			
		
				
		
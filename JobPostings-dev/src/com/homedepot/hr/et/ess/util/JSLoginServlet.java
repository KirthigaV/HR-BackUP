package com.homedepot.hr.et.ess.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Servlet implementation class JSLoginServlet
 */

public class JSLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static final String LIFE_CYCLE_PHASE = "host.LCP";
	
	private static final String JS_USER_ADMIN = "JS Admin User";
	private static final String JS_USER_MANAGER = "JS Manager User";
	private static final String JS_USER_ANALYST = "JS Analyst User";
	private static final String ROLE = "role";
	private String XML_DOC_START_ELE = "JSUserProfile";
	private String XML_DOC_USER_ELE = "userDetails";
	private String sysUserID, firstName, lastName, storeID,storeID2,storeID3,locale;
	
	private static final Logger logger = Logger.getLogger(JSLoginServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public JSLoginServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		final String METHODNAME = "readOpenStorePositions";
		logger.debug("Entering inside:"+METHODNAME);
		
			PrintWriter pw = null;
			String userName = null;
			Element userDetails = null;
			Element jsUser = null;
			String jsUserProfile = null;	

		try {
			response.setContentType("text/html;charset=UTF-8");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.newDocument();

			jsUser = document.createElement(XML_DOC_START_ELE);

			userDetails = document.createElement(XML_DOC_USER_ELE);

			LoginContext lc = new LoginContext("login",new ApplicationCallbackHandler(request));
			logger.debug(METHODNAME+";LoginContext:"+lc);
			lc.login();
			Subject subject = lc.getSubject();
			Set principals = null;
			Principal p;

			// Sysuser ID
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();

			//sysUserID = p.getName();
			
			sysUserID=request.getUserPrincipal().getName();
			
			// Store number
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			storeID = p.getName();
			logger.debug(METHODNAME+";storeID"+storeID);
			
			// Store number
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			storeID2 = p.getName();
			logger.debug(METHODNAME+";storeID2"+storeID2);
			// Store number
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			storeID3 = p.getName();
			logger.debug(METHODNAME+";storeID3"+storeID3);
			
			// Store number
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			locale = p.getName();
			logger.debug(METHODNAME+";locale"+locale);
			
			// First Name
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			firstName = p.getName();
			logger.debug(METHODNAME+";firstName"+firstName);
			
			principals = subject.getPrincipals();
			p = (Principal) principals.iterator().next();
			lastName = p.getName();
			logger.debug(METHODNAME+";lastName"+lastName);
			
			userDetails.setAttribute("sysUserId", sysUserID);

			userDetails.setAttribute("firstName", firstName);

			userDetails.setAttribute("lastName", lastName);
			
			userDetails.setAttribute("storeNo", storeID3);
			
			userDetails.setAttribute("locale", locale);

			if (request.isUserInRole(JS_USER_ADMIN)) {
				userDetails.setAttribute(ROLE, JS_USER_ADMIN);
			} else if (request.isUserInRole(JS_USER_MANAGER)){
				userDetails.setAttribute(ROLE, JS_USER_MANAGER);
			}
			else if (request.isUserInRole(JS_USER_ANALYST)){
				userDetails.setAttribute(ROLE, JS_USER_ANALYST);
			}
			jsUser.appendChild(userDetails);
			jsUserProfile = JSCommonUtils.elementToString(jsUser);
		} catch (LoginException lex) {
			
			logger.debug("LoginException"+lex);
			jsUserProfile = "<JSUserProfile>"+
			"<userDetails firstName=\"Test User\" lastName=\"Test User\" carBatchActiveStatus=\"Success\"  role=\"CR Admin\" userId=\"SNP01\" sysUserId=\"88937288310680794111080J\"/>"+
			"</JSUserProfile>";
			
		} catch (Exception e) {
			
			logger.debug("Exception while Login"+e);
			jsUserProfile = "<JSUserProfile>"+
			"<userDetails firstName=\"Test User\" lastName=\"Test User\" carBatchActiveStatus=\"Success\"  role=\"CR Admin\" userId=\"SNP01\" sysUserId=\"88937288310680794111080J\"/>"+
			"</JSUserProfile>";
		}

		
   finally {
	   logger.debug("login completed ");
   
	   	pw = response.getWriter();
		if (null != pw && null != jsUserProfile) {
			JSCommonUtils.discourageCache(response);
			pw.write(jsUserProfile);
			HttpServletRequest httprequest = (HttpServletRequest) request;
			httprequest.setAttribute("USER_INFO",userName);
			pw.flush();
			pw.close();

		}
   }
	}

}

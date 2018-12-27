package com.homedepot.hr.hr.retailstaffing.util;
/* 
 * This program is proprietary to The Home Depot and is not to be
 * reproduced, used, or disclosed without permission of:
 *    
 *  The Home Depot
 *  2455 Paces Ferry Road, N.W.
 *  Atlanta, GA 30339-4053
 *
 * File Name: QualifiedPoolMessage.java
 * Application: RetailStaffing
 */
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.homedepot.ta.aa.probe.ApplicationPerformanceCheck;
import com.homedepot.ta.aa.probe.CheckList;
import com.homedepot.ta.aa.probe.DataSourceConnectionCheck;

/**
 * Utility Servlet that adds some additional checks to the "Automated Probe". This Servlet
 * is configured to initialize upon JVM startup in the web.xml
 * @author rlp05
 */
public class ApplicationCheck extends HttpServlet
{
	// data source for the application that should be checked during the probe
	private static final String DATASOURCE_NAME = "jdbc/DB2Z.PR1.005";
	// time in milliseconds that indicates there is potentially an issue with the system (2 1/2 minutes)
	private static final long PERF_CHECK_THRESHOLD_LIMIT = 150000;
	// serial value
    private static final long serialVersionUID = 8910868520048392057L;

    /*
     * (non-Javadoc)
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
	public void init(ServletConfig config) throws ServletException
	{
		// invoke the servlet init function
		super.init(config);		
		// get the monitoring check list
		CheckList monitoringChklist = CheckList.getInstance();
		// grab the Servlet context
		String contextName = config.getServletContext().getServletContextName();
		// add a data source check for the main data source used by this application
		monitoringChklist.addCheck(new DataSourceConnectionCheck(contextName, DATASOURCE_NAME));
		// add an application performance check 
		monitoringChklist.addCheck(new ApplicationPerformanceCheck(contextName, PERF_CHECK_THRESHOLD_LIMIT));
	} // end function init()
} // end class ApplicationCheck
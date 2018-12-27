package com.homedepot.hr.et.ess.util;

import java.sql.Connection;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;



/**
 * Checks the major components of an application and saves the result here.
 * This class is accessed by the ApplicationProbe.jsp to display the monitoring results,
 * 
 * Application teams need to customize portions of this class for application specific 
 * monitoring.
 * 
 */

public class ProbeResult {
	
	// ADD YOUR APPLICATION SPECIFIC VALUES HERE
	/* User defined constants*/
	private static final String APPLICATION_NAME = "JobPosting";
	private static final String SYSTEM = "hr";
	private static final String SUBSYSTEM = "et";
	
	//*************************************************************************************
	
	private static final String UP = "<font size='+4'  face='Arial, sans-serif' color='green'>Up </font>";
	private static final String DOWN = "<font size='+4'  face='Arial, sans-serif' color='Red'>Down </font>";
	
	/* member variables */
	private String _result;
	private String _applicationName;
	private String _system;
	private String _subSystem;
	private ArrayList<String> _messages = new ArrayList<String>();
	
	/* Constructor
	 * 
	 */
	public ProbeResult() {
		_applicationName = APPLICATION_NAME;
		_system = SYSTEM;
		_subSystem = SUBSYSTEM;
	}

	public String getApplicationName() {
		return _applicationName;
	}

	public ArrayList<String> getMessages() {
		return _messages;
	}

	/**
	 * Checks if the application components are working fine.
	 * @return String "UP" if everything is fine, or "DOWN" if any
	 * application component returned failure. 
	 */
	public String getResult() {
		
	    _messages.clear();
				

	    // ADD APPLICATION SPECIFIC CHECKS HERE AND POPULATE THE MESSAGES ARRAY LIST WITH ANY ERRORS
		
		/* If error messages are returned by any component, return DOWN as the  
		   result of monitoring */
	    
	    try{
	    	
	    	String DATASOURCE_CONTEXT = "java:comp/env/jdbc/DB2Z.PR1.007";
	        Connection result = null;
	        
	        Context initialContext = new InitialContext();
	        if ( initialContext == null){
	          throw new Exception("Unable to get the Initial Context");
	        }
	        DataSource datasource = (DataSource)initialContext.lookup(DATASOURCE_CONTEXT);
	        if (datasource != null) {
	          result = datasource.getConnection();
	          if(result==null){
	        	  throw new Exception("Connection null"); 
	          }
	        }
	        else {
	        	 throw new Exception("Failed to lookup datasource.");
	        }
	    	
	    //Get connection for DAO
	    	
	    
	    }catch(Exception e){
				
				_messages.add(e.getMessage());
	    	
	    }
		if (_messages.size() == 0) {
			_result = UP;
		} else {
			_result = DOWN;
		} 
		return _result;
	}
	
	public String getSubSystem() {
		return _subSystem;
	}

	public String getSystem() {
		return _system;
	}

}


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Strict//EN">

<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

   

<html:html>

<head>
	<title>JOB POSTINGS</title>
    <link rel="stylesheet" type="text/css" href="/JobPostings/css/layout.css" />
    <link rel="stylesheet" type="text/css" href="/JobPostings/css/styles.css" />   
    <link rel="stylesheet" type="text/css" href="/JobPostings/css/menu.css" /> 
          
</head>   

<body>
<!-- This is your master page template. Do not modify any id names or tags in this document -->
   
    <!-- Top Nav Container: This is your container that will hold the top navigation -->
	    <div id="header">
	    			<tiles:get name="headernav"/>
	    </div>
	      
     <!-- //Top Nav Container -->
    
     <!-- Formatting Containers: These two containers provide for the overall page structure. Do not modify -->
    <div id="wrapper">
     
        <!-- Left Hand Container: This is your container for the left hand page column -->
        <div id="sidebar">
            <div id="sidebarcontainer">
            	<tiles:get name="leftnav"/>
            </div>
        </div> 
        <!-- //Left Hand Container -->
    
        <div id="container">
            <!-- Content Container: This is your container for all content for each page -->
            <div id="content">
                <tiles:get name="body"/>
            </div>
            <!-- //Content Container -->
        </div>
        
        <!-- Clearing Element: This element is necessary to ensure proper page layout. Do not modify -->
        <div class="clearing">&nbsp;</div>
        <!-- //Clearing Element -->
        
    </div>
    
    <!-- Copyright Container: This is your container that will hold the copyright and any other footer content -->
    <div id="footer">
    	<tiles:get name="footer"/>
    </div>
    <!-- //Copyright Container -->
    
</body>
</html:html>
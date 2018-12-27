<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<!-- 
     READ ME FIRST:
     TO LEVERAGE THE APPLICATION PROBE, YOU WILL NEED TO EDIT TWO PROPERTIES IN THIS FILE, THE PAGE TITLE
     AND THE JAVA BEAN CLASS.  EACH IS LABELED WITH AN "EDIT HERE" TAG 
	 PLEASE REMOVE THE COMMENTS UPON COMPLETION
-->
	 
<!-- EDIT HERE: UPDATE THE CLASS NAME HERE TO POINT TO YOUR APPLICATION'S PROBE RESULT BEAN -->
<jsp:useBean id="probeResult" class="com.homedepot.hr.et.ess.util.ProbeResult" />
<HTML>
    <HEAD>
        <META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <!-- EDIT HERE: ADD YOUR APPLICATION NAME HERE -->
        <TITLE>Application Probe - MyAppName</TITLE>
    </HEAD>
    <BODY>
        <TABLE width="90%" border="0" align="center">
	        <TR>
		        <TD align="right" width="200">
		            <FONT size="+1" face="Arial, sans-serif"> Application Status:&nbsp; </FONT>
		        </TD>
		        <TD align="left">
		            <jsp:getProperty name="probeResult"property="result" />
		        </TD>
	        </TR>
         </TABLE>
         <BR/>
         <BR/>
         <TABLE width="90%" border="0" align="left">
             <TR>
		         <TD align="right" width="200">
		             <FONT size="+1" face="Arial, sans-serif"> Application:&nbsp; </FONT>
		         </TD>
		         <TD align="left">
		             <FONT size="+1" face="Arial, sans-serif"><jsp:getProperty name="probeResult" property="applicationName" /></FONT>
		         </TD>
	         </TR>
	         <TR>
		         <TD align="right" width="200">
		             <FONT size="+1" face="Arial, sans-serif"> System:&nbsp; </FONT>
		         </TD>
		         <TD align="left">
		             <FONT size="+1" face="Arial, sans-serif"><jsp:getProperty name="probeResult" property="system" /></FONT>
		         </TD>
	         </TR>
	         <TR>
		         <TD align="right" width="200">
		             <FONT size="+1" face="Arial, sans-serif"> Sub-system:&nbsp;</FONT>
		         </TD>
		         <TD align="left">
		             <FONT size="+1" face="Arial, sans-serif"><jsp:getProperty name="probeResult" property="subSystem" /></FONT>
		         </TD>
	         </TR>
	         <TR>
		         <%  
  		             java.util.ArrayList<String> messages = probeResult.getMessages();
  		             if(messages.size() > 0){
  		         %>
		             <TD align="right" width="200">
		                 <FONT size="+1" face="Arial, sans-serif"> Errors:&nbsp; </FONT>
		             </TD>
		             <TD>
		                 <FONT size="+1" face="Arial, sans-serif">
		                     <%  
  		                         for(String message:messages){
  		    	                     out.write(message+"<BR/>");
  		                         }
                             %>
  		                </FONT>
		             </TD>
  		         <%
  		             }
  	  		     %>
	         </TR>
         </TABLE>
     </BODY>
</HTML>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<script>
	
</script>
<head>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>

<bean:define id="jobPostingForm" name="jobPostingForm"
	type="com.homedepot.hr.et.ess.form.jobposting.JobPostingForm" />



<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>

<%   String hourlyPostingsUrl =request.getContextPath()+"/hourposts_init.do?pageAction=hourPostsInit";  
     //String carrDepotUrl="https://myapron.homedepot.com/en-us/myApron/hr/HRKB/Staffing/Recruiting/CareerDepot/CareerDepot-Associates.html";
     String carrDepotUrl="https://myapron.homedepot.com/en-us/myApron/hr/HRKB/HRServices/Applications/CareerDepot/CareerDepot-Internal_TG.aspx"; 
     
%>

<BODY>
<logic:messagesPresent>
	<TABLE cellSpacing="0" cellPadding="0" width="100%">
		<TR>
			<TD align="left"><jsp:include
				page="/WEB-INF/include/messages.jsp" /></TD>
		</TR>
	</TABLE>
</logic:messagesPresent>

<html:form action="/jp_init">

	<table>
		<TR>
			<td><h1><bean:message key='main.menu'/></h1></td>
		</TR>
	</table>
<div class="threebarsplit" style="width:632px;"></div>
	
	
	
	<table height="30">
		<TR>
			
		</TR>
	</table>
	
	<table>
		<tr>
			<td class="linkText">
			<a href="<%=hourlyPostingsUrl%>"><img
				src="images/YourStore.jpg" alt="Your Store" border="0" /> </a> <BR>
			<bean:message key='click.on' /><b> <bean:message key='your.store' /></b>
			<bean:message key='icon.to.view' /> <BR>
			<bean:message key='all.hourly.positions' /> <BR>
			<bean:message key='your.store' /></td>

			<td width="4%"></td>
			<td width="43%" align="left" class="linkText"><a
				href="<%=carrDepotUrl%>">
			<img src="images/CareerDepotLogo.jpg" alt="Career Depot" border="0" />
			</a> <BR>
			<bean:message key='click.on' /><b> <bean:message key='career.depot' /></b> <bean:message key='icon.to.view' /> <BR>
			<bean:message key='apply.positions' /> <BR>
			<bean:message key='THD' /></td>
		</tr>
	</table>
</html:form>
</BODY>
</html>
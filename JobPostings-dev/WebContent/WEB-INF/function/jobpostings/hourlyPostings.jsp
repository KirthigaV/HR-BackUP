<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>

<bean:define id="jobPostingForm" name="jobPostingForm"
	type="com.homedepot.hr.et.ess.form.jobposting.JobPostingForm" />



<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META http-equiv="Content-Style-Type" content="text/css">
<title></title>
</head>

<%
	String printHourlyPostingsUrl = request.getContextPath()+ "/printhourposts_init.do?pageAction=printhourPostsInit";
	String carrDepotUrl = "http://hrportal.homedepot.com/en_US/HR_Desktop_Portal/KnowledgeBase/US/Staffing/Recruiting/mc_careerdepot_chklst.htm";
	
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

<html:form action="/hourposts_init">
<html:hidden name="jobPostingForm" property="pageAction" value="hourPostsInit" />
<html:hidden name="jobPostingForm" property="pageNo" value="HPpage" />
	<table>
		<TR>
			<td><h1><bean:message key="hourly.positions" /></h1></td>
		</TR>
	</table>
	<div class="threebarsplit" style="width:632px;"></div>
	<table>
		<TR>
			<TD><H3><bean:message key="StoreId" /></H3></TD>
			<TD width="10%"></TD>
			<TD><html:text property="storeIdValue" size="15" maxlength="4"></html:text>
			</TD>
			<TD><html:submit styleClass="Button">SEARCH</html:submit> <logic:messagesPresent property="storeIdValue" >
			<html:img src="/JobPostings/images/Error.gif" border="0" />
			</logic:messagesPresent></TD>
		</TR>
	</table>
	
	<table height="30">
		<TR>
			<td></td>
		</TR>
	</table>
	
	<logic:equal name="jobPostingForm" property="jobPosInd" value="false">
	<logic:equal name="jobPostingForm" property="searchInd" value="true">
	<table>
		<tr>
			<td width="100%"></td>
		</tr>
		<tr>
			<td><h2><bean:message key="error.noresults" /></h2></td>
		</tr>
	</table>
	</logic:equal>
	</logic:equal>
	
	<logic:equal name="jobPostingForm" property="jobPosInd" value="true">
	<table>
		<TR>
			<td></td>
			<td><a
				href="JavaScript:window.print();"> <img src="images/printer.jpg" alt="Print" border="0" /> </a>
			</td>
		</TR>
		<TR>
			<td>
			<display:table id="data" name="sessionScope.jobPostingForm.hourlyPostingsList" requestURI="/hourposts_init.do" pagesize="10" class="TblGrid">
			<display:column property="humanResourcesSystemDepartmentNumber" title="Department" sortable="true" />
			<display:column property="jobTitleDescription" title="Job Title" sortable="true" />
			<display:column property="openPositionCount" title="# of Positions"	sortable="true" />
			<display:column property="fullTimeRequiredFlag" title="FT" sortable="true" />
			<display:column property="partTimeRequiredFlag" title="PT" sortable="true" />
			</display:table>
			</td>
		</TR>
	</table>
	</logic:equal>
	
	
	<logic:equal name="jobPostingForm" property="jobPosInd" value="true">
	<table>
		<tr>
			<td width="100%" height="15px"></td>
		</tr>
		<tr>
			<td width="100%" class="hourlyPostingsText"><bean:message
				key='hourly.postings.footer' /></td>
		</tr>
	</table>
	</logic:equal>
</html:form>
</BODY>
</html>
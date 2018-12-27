<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<%@ page language="java" import="java.util.Date,java.util.Locale,java.text.SimpleDateFormat"%>
<script language="JavaScript" type="text/JavaScript" src="js/jobPosting.js"></script>

<script>
	
</script>





<%
	java.util.Locale locale = new Locale("en", "US");//(java.util.Locale)request.getAttribute("LOCALE");
	String localeString = " ";
	SimpleDateFormat sdf = null;
	if (locale == null) {
		sdf = new SimpleDateFormat("MMMM d, yyyy");
	} else {
		localeString = locale.toString();
		sdf = new SimpleDateFormat("MMMM d, yyyy", locale);
	}
	
	String homeUrl =request.getContextPath()+"/jp_init.do?pageAction=jsHome";  
    String exitUrl ="http://hrportal.homedepot.com/en_US/HR_Desktop_Portal/KnowledgeBase/US/Staffing/Recruiting/mc_careerdepot_chklst.htm";
    
%>


<table border="0" width="100%">
	<tr>
		<td width="10%" rowspan="2"><img src="images/THDLogo.jpg" alt="The Home Depot" /></td>
		<td><H1>Job Opportunities</H1></td>
		<td width="10%" rowspan="2"><a href="<%=homeUrl%>"> <img src="images/hm.jpg" alt="home" />
		</a>
		</td>
		<td width="10%" rowspan="2"> <img src="images/exit.jpg" alt="exit" onclick="close_Current_Window_After_Yes();"/></td>
	</tr>
</table>

<table border="0" width="100%" bgcolor="#FF9933">
	<tr>
		<td>&nbsp;Date: <%=sdf.format(new Date())%></td>
	</tr>
</table>



<!-- /Header TABLE defines header for jsp -->


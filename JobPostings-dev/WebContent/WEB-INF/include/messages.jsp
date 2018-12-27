<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>

<%@page import="java.util.Locale, org.apache.struts.action.Action,
				org.apache.struts.action.ActionErrors,org.apache.struts.action.ActionError"%>
				
				
<tr>
<td>

<font color="red" size="2.5pt" >
<logic:messagesPresent>
   		<ul> 
<html:messages id="error">
		      <li>
				<%= error %>
			</li>
		   </html:messages>
   		</ul> 
</logic:messagesPresent>
</font>
</td>
</tr>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
      
<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		Vector<String> templateIdList = propsMgr.getTemplateIdList();
		int numTemplates = templateIdList.size();
		%>
	</head>
	<body onload="displayMsg();">
		<%@include file="header.jsp"%>
		<p class="section_head">
			Template Editor
		</p>

<%
	if(numTemplates > 0)
	{
%>

		<p>
		What would you like to do?
		</p>
<%
	} // end of if(num > 0)
%>
		<p>
		
		<input type="button" name="newTemplate" value="Create New Template" onclick="document.location='createTemplate.jsp'"/>
		</p>
<%
	String templateId = "";
	String templateTitle = "";
	if(numTemplates > 0)
	{
%>
		<p>
		...or select a template from the list and edit its properties:
		</p>
		<form name="chooseBook" action="../ManageTemplate" method="post">
			<select name="templateId" id="templateId" onchange="templateId.options[selectedIndex].value">
			
<% 
// get list of templates 
	Iterator<String> iTemplates = templateIdList.iterator();
	while(iTemplates.hasNext())
	{
		templateId = (String)iTemplates.next();
		templateTitle  = propsMgr.getTemplateTitle(templateId) + " (" + propsMgr.getTemplateFilename(templateId) + ")";
%>
				<option value="<%= templateId %>">
					<%= templateTitle %> 
				</option>
<% 
	} // end of while{}
%>
			</select>
		<input type="submit" name="editTemplate" value="Edit"  />
        <input type="submit" name="deleteTemplate" value="Delete..." onclick="return confirmDelete();" /><br/>
        Save selected template under a different name:<br/>
        <input type="text" name="newTemplateName" id="newTemplateName" size="50"/>
        <input type="submit" name="renameTemplate" value="Rename" onclick="return validateNewName()" />
        <input type="button" value="?" onclick="displayHelp('renamingTemplates.jsp')" />
        <hr/>
        Update template collection:<br/>
        <input type="submit" name="revertTemplates" value="Revert" />
        <span class="inline_text_note">Replaces user copies of all system-defined templates. All customizations will be lost.</span>
        <input type="button" value="?" onclick="displayHelp('revertTemplates.jsp')" /><br/>
        <input type="submit" name="updateTemplates"  value="Update" />
        <span class="inline_text_note">Checks for new templates; existing customized templates remain in place.</span>
<!--        <input type="hidden" name="templateFilename" value="<%= propsMgr.getTemplateFilename(templateId) %>" /> -->
        <input type="hidden" name="userId" value="<%= userId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
	</form>
<%
	} // end of if(num > 0)
%>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="index.jsp">start</a></div>
			</div>
		</div>
	</body>
</html>


<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.manuscripts.*" %>

		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);

		TemplateProfile templateProfile = null;
		String templateId =  request.getParameter("templateId");
		if(templateId != null && templateId.compareTo("null") != 0)
			templateProfile =  (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
			
		int numCharacters = 0;
		numCharacters = ((templateId == null || templateId.compareTo("null") == 0) ? propsMgr.getCharacterCount() : propsMgr.getCharacterCount(templateId));
		%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Character Editor
		</p>
<%
	if(numCharacters > 0)
	{
%>
		<p>
		What would you like to do?
		</p>
<%
	} // end of if(numCharacters > 0)
%>

		<p>
		<input type="button" name="newCharacter" value="Create New Character" onclick="document.location='createCharacter.jsp'">
		</p>
<%
	String character_code = "";
	String character_name = "";
	if(numCharacters > 0)
	{
%>
		<p>
		...or select a character profile from the list and edit its properties:
		</p>
		<p>
		<form name="chooseCharacter" action="../ManageProfile" method="POST">
			<select name="charId" id="charId" onchange="id.options[selectedIndex].value">
			
<% 
	Vector<String> characterList = (templateId == null || templateId.compareTo("null") == 0) ? propsMgr.getIdList(PropsManager.ObjType.CHARACTER) : propsMgr.getTemplateCharacterList(templateId);
	Iterator<String> iCharacters = characterList.iterator();
	while(iCharacters.hasNext())
	{
		character_code = (String)iCharacters.next();
		character_name = (templateId == null || templateId.compareTo("null") == 0) ? propsMgr.getName(PropsManager.ObjType.CHARACTER, character_code) : templateProfile.getName();		
%>
				<option value="<%= character_code %>">
					<%= character_name %>
				</option>
<% 
	} // end of while{}
%>
			</select>
		<input type="submit" name="editCharacter" value="Edit" >
        <input type="submit" name="deleteCharacter" value="Delete..." onclick="return confirmDelete();" />
        <input type="button" value="?" onclick="displayHelp('profileIds.jsp')" />
        <input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>"/> 
        <input type="hidden" name="templateId" value="<%= templateId  %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
	</form>
<%
	} // end of if(numCharacters > 0)
%>
	<div class="send_back">
		<div class="send_back_pair">
			<div class="send_back_label">Return to</div>
			<div class="send_back_target"><a href="index.jsp">Start</a></div>
		</div>
	</div>
	</body>
</html>


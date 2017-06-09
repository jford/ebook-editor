<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.characters.*,com.iBook.datastore.manuscripts.*" %>
		
<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String charId = request.getParameter("charId");
		String templateId = request.getParameter("templateId");
		TemplateProfile templateProfile = null;
		if(templateId.compareTo("null") != 0)
			templateProfile = propsMgr.getTemplateProfile(templateId);
		CharacterProfile profile = templateId.compareTo("null") == 0 ? propsMgr.getCharacterProfile(charId) : templateProfile.getCharacter(charId);
		String char_name = profile.getName();
		Vector<String> aliases = profile.getAliases();
		int aliasNum = aliases.size();
		String aliasQty = aliasNum == 1 ? "alias" : "aliases";
		String alias = "";
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Edit aliases for <i>eBook</i> character <i><%= char_name %></i>
		</p>
		<%
		if(aliases.size() > 0)
		{
		%>
			<p>
			<%= char_name %> is also known by the following <%= aliasQty %>:
			</p>
			<form name="editAliasForm" action="../ManageProfile" method="post" >
			<ol> 
		<%
			int aliasCount = 0;
			Iterator<String> aliasI = aliases.iterator();
			while(aliasI.hasNext())
			{
				alias = (String) aliasI.next();
				aliasCount++;
				%>
				<li><input type="text" size ="50" name="aliasIndex_<%= Integer.toString(aliasCount) %>" value="<%= alias %>" /></li>
				<%
			}
			%>
			</ol>
			To change text, edit one or more alias then click 
			<input type="submit" name="editAliasSubmit" value="Save" />
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
			<input type="hidden" name="charId" value="<%= charId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
			<hr/>
			<form name="deleteAliasForm" action="../ManageProfile" method="post">
			Delete alias at number 
			<input type="text" name="deleteAliasNum" size="3" value=""/> 
			<input type="submit" name="deleteAliasSubmit" value="Delete..." onclick="return numInRange(<%= aliasNum %>, deleteAliasNum.value);" />
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
			<input type="hidden" name="charId" value="<%= charId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
		<%
		} 
		else
		{
		%>
			<p>
			There are no aliases associated with the <i>eBook</i> character <i><%= char_name %></i>.
			</p>
		<%
		}
		%>
		<hr/>
		<form name="addAlias" action="../ManageProfile" method="post">
		Add new alias: <input type="text" size="50" name="akaName" />
		<input type="submit" name="addAlias" value="Add" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>"/>
		<input type="hidden" name="charId" value="<%= charId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<hr/>
		<p>
		Return to character editor
		<input type="button" name="returnToCharEditorButton" value="Done" onclick="document.location='editCharacter.jsp?templateId=<%= templateId %>&charId=<%= charId %>'" />
		</p>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editCharacters.jsp">Characters Editor</a></div>
			</div>
			<%
			if(templateId.compareTo("null") != 0)
			{
			%>
				<div class="send_back_pair">
					<div class="send_back_label"></div>
					<div class="send_back_target"><a href="editTemplate.jsp?templateId=<%= templateId %>">Template Editor</a></div>
				</div>
			<%
			}
			%>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
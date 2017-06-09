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
		TemplateProfile templateProfile = templateId.compareTo("null") == 0 ? null : propsMgr.getTemplateProfile(templateId);
		boolean isTemplateProfile = templateProfile == null ? false : true;
		CharacterProfile profile = templateId.compareTo("null") == 0 ? propsMgr.getCharacterProfile(charId) : templateProfile.getCharacter(charId);
		String char_name = profile.getName();
		Vector<String> attributes = profile.getAttributes();
		int attrNum = attributes.size();
		String attributeQty = attrNum == 1 ? "attribute" : "attributes";
		String attributeText = "";
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Edit attributes for <i>eBook</i> character <i><%= char_name %></i>
		</p>
		<%
		if(attributes.size() > 0)
		{
		%>
			<p>
			<%= char_name %> is defined by the following <%= attributeQty %>:
			</p>
			<form name="editAttrForm" action="../ManageProfile" method="post" >
			<ol> 
		<%
			int attrCount = 0;
			Iterator<String> attsI = attributes.iterator();
			while(attsI.hasNext())
			{
				attributeText = (String) attsI.next();
				attrCount++;
				%>
				<li><input type="text" size ="50" name="attrIndex_<%= Integer.toString(attrCount) %>" value="<%= attributeText %>" /></li>
				<%
			}
			%>
			</ol>
			To change text, edit one or more attributes then click <input type="submit" name="editAttrSubmit" value="Save" />
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
			<input type="hidden" name="charId" value="<%= charId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
			<hr/>
			<form name="deleteAttrForm" action="../ManageProfile" method="post">
			Delete attribute number 
			<input type="text" name="deleteAttrNum" size="3" value=""/> 
			<input type="submit" name="deleteAttrButton" value="Delete..." onclick="return numInRange(<%= attrNum %>, deleteAttrNum.value);" />
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
			There are no attributes associated with the <i>eBook</i> character <i><%= char_name %></i>.
			</p>
		<%
		}
		%>
		<form name="editCharAttribute" action="../ManageProfile" method="post">
		<hr/>
		Add new attribute: <input type="text" size="50" name="attributeText" />
		<input type="submit" name="updateAddAttribute" id="submitAttrChanges" value="Add" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>"/>
		<input type="hidden" name="charId" value="<%= charId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="/iBook/editor/editCharacters.jsp">Characters Editor</a></div>
			</div>
			<%
			if(isTemplateProfile)
			{
			%>
				<div class="send_back_pair">
					<div class="send_back_label"></div>
					<div class="send_back_target"><a href="/iBook/editor/editTemplate.jsp?templateId=<%= templateId %>">Template Editor</a></div>
				</div>
			<%
			}
			%>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="/iBook/editor/index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
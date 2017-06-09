<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
<%
		String userId = request.getRemoteUser();
		String loc_id = request.getParameter("loc_id");
		String templateId = request.getParameter("templateId");
		boolean isTemplateId = templateId.compareTo("null") == 0 ? false : true;
		PropsManager propsMgr = new PropsManager(userId);
		TemplateProfile templateProfile = null;
		LocationProfile profile = null;
		if(isTemplateId)
			profile = propsMgr.getTemplateLocation(templateId, loc_id);
		else
			profile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, loc_id);
		String location_name = profile.getName();
		Vector<String> aliases = profile.getAliases();
		int aliasNum = aliases.size();
		String aliasQty = aliasNum == 1 ? "alias" : "aliases";
		String alias = "";
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Edit aliases for <i>eBook</i> location <i><%= location_name %></i>
		</p>
		<%
		if(aliases.size() > 0)
		{
		%>
			<p>
			<%= location_name %> is also known by the following <%= aliasQty %>:
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
			To change text, edit one or more alias then click <input type="submit" name="editAliasSubmit" value="Save" />
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>" />
			<input type="hidden" name="loc_id" value="<%= loc_id %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			</form>
			<hr/>
			<form name="deleteAliasForm" action="ManageProfile" method="post">
			Delete alias at number 
			<input type="text" name="deleteAliasNum" size="3" value=""/> 
			<input type="submit" name="deleteAliasButton" value="Delete" onclick="return numInRange(<%= aliasNum %>, deleteAliasNum.value);" />
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>" />
			<input type="hidden" name="loc_id" value="<%= loc_id %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
		<%
		} 
		else
		{
		%>
			<p>
			There are no aliases associated with the <i>eBook</i> location <i><%= location_name %></i>.
			</p>
		<%
		}
		%>
		<form name="addAlias" action="../ManageProfile" method="post">
		<hr/>
		Add new alias: <input type="text" size="50" name="newAlias" />
		<input type="submit" name="addAlias" value="Add" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>"/>
		<input type="hidden" name="loc_id" value="<%= loc_id %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<hr/>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editLocation.jsp?templateId=<%= templateId %>&loc_id=<%= loc_id %>">Location Editor</a></div>
			</div>
			<%
			if(templateId != null && templateId.compareTo("null") != 0)
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
		
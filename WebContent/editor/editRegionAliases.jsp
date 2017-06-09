<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
<%
String userId = request.getRemoteUser();
       	String geolocId = request.getParameter("geolocId");
		String templateId = request.getParameter("templateId");
		boolean isTemplateId = templateId.compareTo("null") == 0 ? false : true;
		PropsManager propsMgr = new PropsManager(userId);
		TemplateProfile templateProfile = null;
		GeolocaleProfile profile = null;
		if(isTemplateId)
		{
			templateProfile = propsMgr.getTemplateProfile(templateId);
			profile = templateProfile.getGeoloc(geolocId);
		}
		else
			profile = (GeolocaleProfile)propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, geolocId);
		String geolocName = profile.getName();
		Vector<String> aliases = profile.getAliases();
		int aliasNum = aliases.size();
		String aliasQty = aliasNum == 1 ? "alias" : "aliases";
		String alias = "";
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Edit aliases for <i>eBook</i> region <i><%= geolocName %></i>
		</p>
		<%
		if(aliases.size() > 0)
		{
		%>
			<p>
			<%= geolocName %> is also known by the following <%= aliasQty %>:
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
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
			<input type="hidden" name="geolocId" value="<%= geolocId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
			<hr/>
			<form name="deleteAliasForm" action="../ManageProfile" method="post">
			Delete alias at number 
			<input type="text" name="deleteAliasNum" size="3" value=""/> 
			<input type="submit" name="deleteAliasButton" value="Delete..." onclick="return numInRange(<%= aliasNum %>, deleteAliasNum.value);" />
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
			<input type="hidden" name="geolocId" value="<%= geolocId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
		<%
		} 
		else
		{
		%>
			<p>
			There are no aliases associated with the <i>eBook</i> region <i><%= geolocName %></i>.
			</p>
		<%
		}
		%>
		<form name="addAlias" action="ManageProfile" method="post">
		<hr/>
		Add new alias: <input type="text" size="50" name="newAlias" />
		<input type="submit" name="addAlias" value="Add" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>"/>
		<input type="hidden" name="geolocId" value="<%= geolocId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<hr/>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editRegion.jsp?templateId=<%= templateId %>&geolocId=<%= geolocId %>">Region Editor</a></div>
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
		
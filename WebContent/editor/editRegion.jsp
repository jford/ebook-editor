<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
<%
		String userId = request.getRemoteUser();
		String loc_id = request.getParameter("loc_id");
		String geolocId = request.getParameter("geolocId");
		String templateId = request.getParameter("templateId");
		String geolocName = "";
		String geolocType = "";
		PropsManager propsMgr = new PropsManager(userId);
		TemplateProfile templateProfile = null;
		boolean editingTemplate = (templateId != null && templateId.compareTo("null") != 0) ? true : false;
		if(editingTemplate)
			templateProfile = propsMgr.getTemplateProfile(templateId);
			
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<%
		if(geolocId != null && geolocId.compareTo("null") != 0)
		{
			GeolocaleProfile profile = null;
			if(editingTemplate)
				profile = templateProfile.getGeoloc(geolocId);
			else
				profile = propsMgr.getGeolocale(geolocId);
			geolocName = profile.getName();
			geolocType = profile.getType();
			
			%>
			<p class="section_head">
			<%
			if(editingTemplate)
			{
			%>
				Edit <i>Book</i> Region <i><%= geolocName %></i>
			<%
			}
			else
			{
			%>
				Edit Attributes for <i>eBook</i> Region <i><%= geolocName %></i>
			<%
			}
			%>
			</p>
			<form name="editRegionNameForm" action="../ManageProfile" method="post">
			<input type="text" size="30" name="newRegionName" value="<%= geolocName %>" />
			<input type="submit" name="newRegionName" value="Edit Name" />
	<br/>
	<span class="inline_text_note">(<%= propsMgr.getIBookSystemName() %> ID: <i><%= geolocId %></i>) 
	<input type="button" value="?" id="smallHelpButton" onclick="displayHelp('profileIds.jsp')" /></span>
	<br/>
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
			<input type="hidden" name="geolocId" value="<%= geolocId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
			
			<form name="editRegionTypeForm" action="../ManageProfile" method="post">
			<%
			String buttonVal = editingTemplate ? "Edit Significance" : "Edit Type";
			if(editingTemplate)
			{
			%>
				Significance in source test:<br/>
			<%
			}
			%>
			<input type="text" size="30" name="newRegionType" value="<%= geolocType %>" />
			<input type="submit" name="newRegionType" value="<%= buttonVal %>" />
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
			<input type="hidden" name="geolocId" value="<%= geolocId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
			<hr/>
			<%
				Vector<String> attributes = profile.getDescriptions();
				int attrNum = attributes.size();
				String attributeQty = attrNum == 1 ? "attribute" : "attributes";
				String attributeText = "";
				if(attributes.size() > 0)
				{
				%>
					<p>
					<%= geolocName %> is defined by the following <%= attributeQty %>:
					</p>
					<form name="editAttrForm" action="../ManageProfile" method="post">
					<ol> 
				<%
					int attrCount = 0;
					Iterator<String> attsI = attributes.iterator();
					while(attsI.hasNext())
					{
						attributeText = (String) attsI.next();
						%>
						<li><input type="text" size ="50" name="attrIndex_<%= Integer.toString(attrCount) %>" value="<%= attributeText %>" /></li>
						<%
						attrCount++;
					}
					%>
					</ol>
					To change text, edit one or more attributes then click <input type="submit" name="editAttrSubmit" value="Save" />
					<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
					<input type="hidden" name="geolocId" value="<%= geolocId %>" />
					<input type="hidden" name="templateId" value="<%= templateId %>" />
					<input type="hidden" name="userId" value="<%= userId %>" />
					</form>
					<hr/>
					<form name="deleteAttrForm" action="../ManageProfile" method="post">
					Delete attribute number 
					<input type="text" name="deleteAttrNum" size="3" value=""/> 
					<input type="submit" name="deleteAttrButton" value="Delete" onclick="return attrNumInRange(<%= attrNum %>, deleteAttrNum.value);" />
					<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
					<input type="hidden" name="geolocId" value="<%= geolocId %>" />
					<input type="hidden" name="loc_id" value="<%= loc_id %>" />
					<input type="hidden" name="templateId" value="<%= templateId %>" />
					<input type="hidden" name="userId" value="<%= userId %>" />
					</form>
					<%
				} // end if(attribues.size())
				else
				{
				%>
				<p>
					<%= geolocName %> has no descriptive attributes defined.
				</p>
				<%
				}// end else
				%>
				<form name="editLocaleAttribute" action="../ManageProfile" method="post">
				Add new attribute: <input type="text" size="50" name="newAttr" />
				<input type="submit" name="submitAttrChanges" id="submitAttrChanges" value="Add" />
				<input type="hidden" name="profileType" value="geolocale"/>
				<input type="hidden" name="loc_id" value="<%= loc_id %>" />
				<input type="hidden" name="geolocId" value="<%= geolocId %>"/>
				<input type="hidden" name="templateId" value="<%= templateId %>" />
				<input type="hidden" name="userId" value="<%= userId %>" />
				</form>
		
				<%
				if(!editingTemplate)
				{
					if(loc_id != null && loc_id.length() > 0)
					{
						String locName = propsMgr.getLocationName(loc_id);
					%>	
						<hr/>
						<form name="removeGeolocForm" action="../ManageProfile" method="post">
						Remove region <i><%= geolocName %></i> from <i><%=  locName %></i> location:
						<input type="submit" name="removeGeolocFromLocation" id="removeGeolocFromLocation" value="Remove" />
						<input type="hidden" name="loc_id" value="<%= loc_id %>" />
						<input type="hidden" name="geolocId" value="<%= geolocId %>" />
						<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
						<input type="hidden" name="templateId" value="<%= templateId %>" />
						<input type="hidden" name="userId" value="<%= userId %>" />
						</form>
						<%
					} // end if loc_id.length() > 0)
					
					Vector<String> locList = propsMgr.getIdList(PropsManager.ObjType.LOCATION);
					Iterator<String> locListI = locList.iterator();
					%>
					<hr/>
					<form name="addLocationForm" action="../ManageProfile" method="post">
					Add the following location to this region:
					<select name="loc_id" id="loc_id" onchange="loc_id.options[selectedIndex].value">
					<%
					String newLocId = "";
					String newLocName = "";
					while(locListI.hasNext())
					{
						newLocId = locListI.next();
						newLocName = propsMgr.getName(PropsManager.ObjType.LOCATION, newLocId);
					%>
						<option value=<%= newLocId %>>
						<%= newLocName %>
						</option>
					<%
					} // end while
				%>
					</select>
					<input type="submit" name="addLocToRegion" value="Add" />
					<input type="hidden" name="loc_id" value=<%= newLocId %> />
					<input type="hidden" name="geolocId" value=<%= geolocId %> />
					<input type="hidden" name="profileType" value="geolocale" />
					<input type="hidden" name="templateId" value=<%= templateId %> />
					<input type="hidden" name="userId" value="<%= userId %>" />
					</form>
					<%
				} // end if(!editingTemplate)
				Vector<String> aliases = profile.getAliases();
				int numAliases = aliases.size();
				String alias = "";
				%>
			<hr/>
			<p>
			The region <%= geolocName %> <%= numAliases > 0 ? "is also known as" : "has no other names" %>.
			</p>
			<%
			if(numAliases > 0)
			{
			%>
			<ul>
			<%
				Iterator<String> aliasesI = aliases.iterator();
				while(aliasesI.hasNext())
				{
					alias = (String)aliasesI.next();
			%>
					<li><a href="editRegionAliases.jsp?templateId=<%= templateId %>&geolocId=<%= geolocId %>"><%= alias %></a> (Click to edit)</li>
			<%
				} // while(aliasesI.hasNext())
			%>
			</ul>
			<%
			} // if(numAliases > 0)
			%>
			<form name="addAliasForm" action="../ManageProfile" method="post">
			Add alias:
			<input type="text" size="50" name="alias"/>
			<input type="submit" name="addGeolocAliasSubmit" value="Add"/>
			<input type="hidden" name="geolocId" value=<%= geolocId %> />
			<input type="hidden" name="profileType" value="geolocale" />
			<input type="hidden" name="templateId" value=<%= templateId %> />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
		<%
		} // end if(geolocId not null)
		else
		{
			if(!editingTemplate)
			{
			%>
				<p>
				Which region?
				</p>
				<form name="chooseLocation" action="../ManageProfile" method="post">
				<select name="geolocId" id="loc_id" onchange="geolocId.options[selectedIndex].value">
			<% 
				Vector<String> geolocList = propsMgr.getIdList(PropsManager.ObjType.GEOLOCALE);
				Iterator<String> iGeolocs = geolocList.iterator();
				while(iGeolocs.hasNext())
				{
					geolocId = (String)iGeolocs.next();
					geolocName = propsMgr.getName(PropsManager.ObjType.GEOLOCALE, geolocId);		
			%>
							<option value="<%= geolocId %>">
								<%= geolocName %>
							</option>
			<% 
		        } // end while 
			%>
						</select>
					<input type="submit" name="editGeoloc" value="Edit" />
			        <input type="submit" name="deleteGeoloc" value="Delete" onclick="return confirmDelete();" />
			        <input type="button" value="?" onclick="displayHelp('profileIds.jsp')" />
			        <input type="hidden" name="profileType" value="geolocale" />
			        <input type="hidden" name="geolocId" value="<%= geolocId %>" />
			        <input type="hidden" name="templateId" value="<%= templateId %>" />
					<input type="hidden" name="userId" value="<%= userId %>" />
				</form>
				<%
			} // end if(!editingTemplate)
		} // end of else
		%>
		
		<div class="send_back">
		<%
		if(templateId != null && templateId.compareTo("null") != 0)
		{
		%>
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editTemplate.jsp?templateId=<%= templateId %>">Template Editor</a></div>
			</div>
		<%
		} // if templateId != null
		%>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="editLocations.jsp">Location Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
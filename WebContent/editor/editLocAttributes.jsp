<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
<%
		String userId = request.getRemoteUser();
		String loc_id = request.getParameter("loc_id");
		String templateId2 = request.getParameter("templateId");
		String templateId = request.getParameter("templateId");
		String templateTitle = "";
		boolean isTemplateId = false;
		if(templateId2.compareTo("null") != 0)
			isTemplateId = true;
		PropsManager propsMgr = new PropsManager(userId);
		LocationProfile profile = null;
		if(isTemplateId)
		{
			templateTitle = ((TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId)).getTitle();
			profile = propsMgr.getTemplateLocation(templateId, loc_id);
		}
		else
			profile = (LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, loc_id);
		String location_name = profile.getName();
		Vector<String> attributes = profile.getLocationDescriptions();
		int attrNum = attributes.size();
		String attributeQty = attrNum == 1 ? "attribute" : "attributes";
		String attributeText = "";
		String profileType = isTemplateId ? "<i>" + templateTitle + "</i> template" : "<i>eBook</i>";
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Edit attributes for <%= profileType %> location <i><%= location_name %></i>
		</p>
		<%
		if(attributes.size() > 0)
		{
		%>
			<p>
			<%= location_name %> is defined by the following <%= attributeQty %>:
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
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>" />
			<input type="hidden" name="loc_id" value="<%= loc_id %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			</form>
			<hr/>
			<form name="deleteAttrForm" action="../ManageProfile" method="post">
			Delete attribute number 
			<input type="text" name="deleteAttrNum" size="3" value=""/> 
			<input type="submit" name="deleteAttrButton" value="Delete..." onclick="return numInRange(<%= attrNum %>, deleteAttrNum.value);" />
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
			There are no attributes associated with the <i>eBook</i> location <i><%= location_name %></i>.
			</p>
		<%
		}
		%>
		<form name="editLocaleAttribute" action="../ManageProfile" method="post">
		<hr/>
		Add new attribute: <input type="text" size="50" name="newAttr" />
		<input type="submit" name="submitAttrChanges" id="submitAttrChanges" value="Add" />
		<input type="button" value="?" onclick="displayHelp('attributes.jsp')" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>"/>
		<input type="hidden" name="loc_id" value="<%= loc_id %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<hr/>
		<p>
		Return to location editor
		<input type="button" name="returnToLocationEditorButton" value="Done" onclick="document.location='editLocation.jsp?templateId=<%= templateId %>&loc_id=<%= loc_id %>'" />
		</p>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editLocations.jsp">Locations Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
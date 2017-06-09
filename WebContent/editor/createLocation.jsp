<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*" %>
		
<%String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String templateId = request.getParameter("templateId");
		String templateName = "";
		boolean isTemplate = (templateId != null && templateId.compareTo("null") != 0) ? true : false;
		if(isTemplate)
			templateName = propsMgr.getTemplateTitle(templateId);
%>
	</head>
	<body onload="document.getElementById('newLocationName').focus()" >
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Create Location <input type="button" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
		<%
		if(templateName.length() > 0)
		{
		%>
		for the template <i><%= templateName %></i>.
		<%
		}
		%>
		</p>
		<p class="inline_text_note">
		A location should be a specific setting where action takes place&mdash;a bedroom, 
		a kitchen, a hotel, or a street corner, for example. If the place you want to define is more general, 
		used in the narrative as a collective place where the actual sets are located&mdash;a city, perhaps,
		or a neighborhood within a city&mdash;then you might consider defining it as a region, 
		not a location. 
		</p>
		<p>
		<form name="createLocationForm" action="../ManageProfile" method="post">
		Location Name:<br/>
		<input type="text" name="newLocationName" id="newLocationName" size="50"/>
		</p>
		<p>
		Type (city? Country? State? Forest?):<br/>
		<input type="text" name="newLocationType" size="30" />
		</p>
		<p>
		Description attribute:<br/>
		<input type="text" name="newLocationDescription" size="50" /><input type="button" value="?" onclick="displayHelp('attributes.jsp')" />
		</p>
		<p>
		<input type="submit" name="createLocation" value="Create Location" onclick="return validEntry(newLocationName.value, 'location name');" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.LOCATION %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</p>
		</form>
		<p class="inline_text_note">
			After the location has been created, you will be able to use the <i>Edit Location</i> page to add more descriptive attributes, aliases, 
			and associated regions.
		</p>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editLocations.jsp">Locations Editor</a></div>
			</div>
			<%
			if(isTemplate)
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
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
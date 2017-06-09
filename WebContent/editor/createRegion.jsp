<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*" %>
		
<%
		String loc_id = request.getParameter("loc_id");
		String templateId = request.getParameter("templateId");
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String location_name = "";
		boolean isTemplate = (templateId != null && templateId.compareTo("null") != 0) ? true : false;
		boolean haveLocId = (loc_id != null && loc_id.compareTo("null") != 0) ? true : false;
		String buttonText = !haveLocId ? "Create Region" : "Create Region and Add to Profile";
		// loc_id is the ID of the location to which this region is attached;
		// loc_id can be null if createRegion is called from the template editor or 
		// from editLocations.jsp
		if(haveLocId)
			location_name = propsMgr.getLocationName(loc_id);
%>
	</head>
	<body onload="document.getElementById('newRegionName').focus()" >
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Create Region
			<%
			if(loc_id != null && loc_id.length() > 0)
			{
			%> 
			 and add it to <i>eBook</i> Location Profile for <%= location_name %>
			<%
			}
			%>
			<input type="button" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
		</p>
		<form name="addRegionToLocation" action="../ManageProfile" method="post">
		Region name:<br/>
		<input type="text" name="newRegionName" id="newRegionName" size="50"/><br/>
		Description:<br/>
		<input type="text" name="newRegionDescription" size="50" />
		<p>
		<input type="submit" name="createLocale" value="<%= buttonText %>" onclick="return validEntry(newRegionName.value, 'region name');"/>
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.GEOLOCALE %>" />
		<input type="hidden" name="loc_id" value="<%= loc_id %>"/>
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</p>
		</form>
		<%
		if(!isTemplate)
		{
		%>
			<p>
				Use the <i>Edit Location</i> page to add more descriptive attributes.
			</p>
		<%
		}
		%>
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
		
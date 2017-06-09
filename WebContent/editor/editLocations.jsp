<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.utilities.*" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String templateId = request.getParameter("templateId");
		int num = propsMgr.getLocationsCount();
		%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Locations Editor
		</p>
<%
	if(num > 0)
	{
%>
		<p>
		What would you like to do?
		</p>
<%
	} // end of if(num > 0)
%>
		<input type="button" name="createLocation" value="Create Location" onclick="document.location='createLocation.jsp?templateId=<%= templateId %> %>'" /><br/>
		<input type="button" name="createLocale" value="Create Region" onclick="document.location='createRegion.jsp?templateId=<%= templateId %>'">
		
		<input type="button" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
		<%
		if(propsMgr.getGeolocsCount() > 0)
		{
		%>
			<input type="button" name="editGeolocs" value="Edit Regions" onclick="document.location='editRegion.jsp?templateId=<%= templateId %>&loc_id=null&geolocId=null'" />
		<%
		} // end if geolocs num > 0
		String location_code = "";
		String location_name = "";
		if(num > 0)
		{
%>
		<p>
		...or select a location from the list and edit its properties:
		</p>
		<p>
		<form name="chooseLocation" action="../ManageProfile" method="post">
			<select name="loc_id" id="loc_id" onchange="loc_id.options[selectedIndex].value">
			
<% 
	Vector<String> locationList = propsMgr.getIdList(PropsManager.ObjType.LOCATION);
	Iterator<String> iLocations = locationList.iterator();
	while(iLocations.hasNext())
	{
		location_code = (String)iLocations.next();
		location_name = propsMgr.getName(PropsManager.ObjType.LOCATION, location_code);		
%>
				<option value="<%= location_code %>">
					<%= location_name %>
				</option>
<% 
        } 
%>
			</select>
		<input type="submit" name="editLocation" value="Edit" />
        <input type="submit" name="deleteLocation" value="Delete..." onclick="return confirmDelete();" />
        <input type="button" value="?" onclick="displayHelp('profileIds.jsp')" />
        <input type="hidden" name="profileType" value="location" />
        <input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
	</form>
<%
	} // end of if(num > 0)
%>
	</p>
	<div class="send_back">
		<div class="send_back_pair">
			<div class="send_back_label">Return to </div>
			<div class="sewnd_back_target"> <a href="index.jsp">Start</a></div>
		</div>
	</div>
	</body>
</html>


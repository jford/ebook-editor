<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String bookId = request.getParameter("bookId");
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		String templateId = bookProfile.getTemplateId();
		TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
		String projectName = bookProfile.getTitle();
		Vector<LocationProfile> tLocs = templateProfile.getLocations();
		Iterator<LocationProfile> tLocsI = tLocs.iterator();
		LocationProfile tLoc = null;
		String currentSubId = "";
		String currentSubName = "";
		String appNameFull = propsMgr.getIBookSystemName("full");
		String appNameShort = propsMgr.getIBookSystemName("short");
		boolean noLocWarningShown = false;
		%>
		<p class="section_head">
			Edit Location Assignments for <i><%= appNameFull %></i> project <i><%= projectName %></i> 
			<input type="button" name="help" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
			
		</p>
		<p>
		
		<table>
			<tr><th>Source Text Location</th><th><%= appNameShort %> Substitute</th></tr>
			<%
			Vector<String> locationList = propsMgr.getIdList(PropsManager.ObjType.LOCATION);
			int iBookLocCount = locationList.size();
			while(tLocsI.hasNext())
			{
				currentSubName = "";
				tLoc = (LocationProfile)tLocsI.next();
				currentSubId = bookProfile.getCurrentSub(PropsManager.ObjType.LOCATION, tLoc.getId());
				Vector<String> tLocAliases = tLoc.getAliases();
				Vector<String> tLocAttributes = tLoc.getLocationDescriptions();
				boolean hasAliases = tLocAliases.size() > 0 ? true : false;
				boolean hasAtts = tLocAttributes.size() > 0 ? true : false;
				if(currentSubId.length() > 0)
					currentSubName = ((LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, currentSubId)).getName();
				
			%>
			<tr>
				<td>
					<form name="locMapForm" action="../ManageBook" method="post">
						<%=  tLoc.getName() %>
						<%
						if(hasAliases || hasAtts)
						{
							if(hasAliases)
							{
							%>	
								<input type="submit" name="mapAliasesSubmit"id="smallSubmitButton" value="Map Aliases" onclick="return validateMapButton('<%= tLoc.getId() %>', '<%= currentSubId %>')"/>
							<%
							} // end if(hasAliases)
							if(hasAtts)
							{
							%>	
								<input type="submit" name="mapAttributesSubmit"id="smallSubmitButton" value="Map Attributes" onclick="return validateMapButton('<%= tLoc.getId() %>', '<%= currentSubId %>')"/>
							<%
							} // end if(hasAtts)
						} // end of if(hasAliases || hasAtts)
						%>
							<input type="hidden" name="bookId" value="<%= bookId %>" />
							<input type="hidden" name="tObjId" id="tObjId" value="<%= tLoc.getId() %>" />
							<input type="hidden" name="iBookObjId" id="iBookObjId" value="<%= currentSubId %>" />
						<input type="hidden" name="userId" value="<%= userId %>" />
						</form>
				</td>
				<td>
					<form name="setsubstituteForm" action="../ManageProfile" method="post">
						<%
				if(iBookLocCount > 0)
				{
					%>
					<select name="iBookLocId" id="iBookLocId" onchange="iBookLocId.options[selectedIndex].value">
					
					<% 
					String location_code = "";
					String location_name = "";
					Iterator<String> iLocations = locationList.iterator();
					%>
					<option value = <%= currentSubId %>>
					<%= currentSubName %>
					</option>
					<%
					while(iLocations.hasNext())
					{
						location_code = (String)iLocations.next();
						location_name = propsMgr.getName(PropsManager.ObjType.LOCATION, location_code);		
					%>
								<option value="<%= location_code %>">
									<%= location_name %>
								</option>
					<% 
					} // end of while{}
					%>
					</select>
				</td>
				<td>
				<input type="submit" name="setLocSubSubmit" id="smallSubmitButton" value="Set" />
				<input type="submit" name="clearLocSubSubmit" id="smallSubmitButton" value="Clear" />
				<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.BOOK %>" />
				<input type="hidden" name="bookId" value="<%= bookId %>" />
				<input type="hidden" name="tLocId" value="<%= tLoc.getId() %>" />
				<input type="hidden" name="userId" value="<%= userId %>" />
						<%
				} // end of if(iBookLoc > 0
				else
				{
					if(!noLocWarningShown)
					{
					%>
							<span class="inline_text_note" style="color: red" >(There are no <i><%= appNameShort %></i> locations available for substitution. Use the <a href="editLocations.jsp">Location Editor</a> to create one.)</span>
					<%
					noLocWarningShown = true;
					} // end of "if no warning shown"
				} // end of else
					%>
				</td>
				</form>
			</tr>
			<tr>
				<td  colspan="3"><div class="inline_text_note"><%= tLoc.getType() %></div></td>
			</tr>
			<%
			} // end while tLocsI.hasNext()
			%>
		</table>
		</p>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editLocations.jsp">Locations Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="editBook.jsp?bookId=<%= bookId %>">Book Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>

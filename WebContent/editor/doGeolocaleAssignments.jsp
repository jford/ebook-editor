<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<%
		String userId =request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String bookId = request.getParameter("bookId");
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		String templateId = bookProfile.getTemplateId();
		TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
		String projectName = bookProfile.getTitle();
		Vector<GeolocaleProfile> tGeolocs = templateProfile.getGeolocs();
		Iterator<GeolocaleProfile> tGeolocsI = tGeolocs.iterator();
		GeolocaleProfile tGeoloc = null;
		String currentSubId = "";
		String currentSubName = "";
		String appNameFull = propsMgr.getIBookSystemName("full");
		String appNameShort = propsMgr.getIBookSystemName("short");
		// warning text is defined in a table cell; only want to show it once for the entire table, 
		// not once every ropw
		boolean noGeolocWarningShown = false;
		%>
		<p class="section_head">
			Edit Region Assignments for <i><%= appNameFull %></i> project <i><%= projectName %></i>
			<input type="button" name="help" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
		</p>
		<p>
		
		<table>
			<tr><th>Source Text Region</th><th><%= appNameShort %> Substitute</th></tr>
			<tr>
				<td style="width: 40%">
					<span class="inline_text_note">Places often have aliases and regional 
					characteristics (attributes) that the <i><%= appNameShort %></i> editor can't map to 
					your custom places without some assistance. If the original source text 
					refers to  such an alias or regional attribute that you want converted 
					to more familiar text (dollars instead of British pounds, for example), 
					there will be a <b>Map Aliases</b> and/or a <b>Map Attributes</b> button 
					adjacent to the region's name.</span>
				</td>
				<td style="width: 40%">
					<span class="inline_text_note">The <b>Map Attributes</b> button opens a 
					page where you tell the editor what text to use in place of the 
					regional term. If you have defined one or more attributes in your region 
					profile, you can select from them. Otherwise, you need to enter the 
					substitute text manually. <br/><br/>(<b>Set/Clear</b> buttons only work 
					for a single region. Click for each change.)</span>
				</td>
			<tr>
			<%
			while(tGeolocsI.hasNext())
			{
				currentSubName = "";
				tGeoloc = (GeolocaleProfile)tGeolocsI.next();
				currentSubId = bookProfile.getCurrentSub(PropsManager.ObjType.GEOLOCALE, tGeoloc.getId());
				Vector<String> tGeolocAliases = tGeoloc.getAliases();
				Vector<String> tGeolocAttributes = tGeoloc.getDescriptions();
				boolean hasAliases = tGeolocAliases.size() > 0 ? true : false;
				boolean hasAtts = tGeolocAttributes.size() > 0 ? true : false;
				if(currentSubId.length() > 0)
					currentSubName = ((GeolocaleProfile)propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, currentSubId)).getName();
				
			%>
			<tr>
				<td>
					<form name="geolocMapForm" action="../ManageBook" method="post" >
						<%=  tGeoloc.getName() %> 
						<%
						if(hasAliases || hasAtts)
						{
							if(hasAliases)
							{
							%>	
								<input type="submit" name="mapAliasesSubmit"id="smallSubmitButton" value="Map Aliases" onclick="return validateMapButton('<%= tGeoloc.getId() %>', '<%= currentSubId %>')"/>
							<%
							} // end if(hasAliases)
							if(hasAtts)
							{
							%>	
								<input type="submit" name="mapAttributesSubmit"id="smallSubmitButton" value="Map Attributes" onclick="return validateMapButton('<%= tGeoloc.getId() %>', '<%= currentSubId %>')"/>
							<%
							} // end if(hasAtts)
						} // end of if(hasAliases || hasAtts)
						%>
						<input type="hidden" name="bookId" value="<%= bookId %>" />
						<input type="hidden" name="tObjId" id="tObjId" value="<%= tGeoloc.getId() %>" />
						<input type="hidden" name="iBookObjId" id="iBookObjId" value="<%= currentSubId %>" />
						<input type="hidden" name="userId" value="<%= userId %>" />
					</form> <!-- geolocMapForm -->
				</td>
				<td>
				<%
				String geoloc_code = "";
				String geoloc_name = "";
				Vector<String> geolocList = propsMgr.getIdList(PropsManager.ObjType.GEOLOCALE);
				int iGeolocCount = geolocList.size();
				Iterator<String> iGeolocales = geolocList.iterator();
				if(iGeolocCount > 0)
				{
				%>
					<form name="setAttributeForm" action="../ManageProfile" method="post" >
						<select name="iBookGeolocId" id="iBookGeolocId" onchange="iBookGeolocId.options[selectedIndex].value">
						<option value = <%= currentSubId %>>
						<%= currentSubName %>
						</option>
					<%
					while(iGeolocales.hasNext())
					{
						geoloc_code = (String)iGeolocales.next();
						geoloc_name = propsMgr.getName(PropsManager.ObjType.GEOLOCALE, geoloc_code);		
					%>
							<option value="<%= geoloc_code %>">
								<%= geoloc_name %>
							</option>
						
					<% 
					} // end of while{}
					%>
					</select>
					<input type="submit" name="setGeolocSubSubmit" id="smallSubmitButton" value="Set" />
					<input type="submit" name="clearGeolocSubSubmit" id="smallSubmitButton" value="Clear" />
					<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.BOOK %>" />
					<input type="hidden" name="bookId" value="<%= bookId %>" />
					<input type="hidden" name="tGeolocId" value="<%= tGeoloc.getId() %>" />
					<input type="hidden" name="userId" value="<%= userId %>" />
					<%
				} // end of if(iGeolocCount > 0)
				else
				{
					if(!noGeolocWarningShown)
					{
						// show this text only once for the table, not once every row
						%>
						<span class="inline_text_note" style="color: red">(There are no <i><%= appNameShort %></i> regions available for substitution. Use the <a href="editLocations.jsp">Location Editor</a> to create one.)</span>
						<%
						noGeolocWarningShown = true;
					} // end of if(!noGeolocWarningShown)
				} // of if(iGeolocCount >0) else...
				%>
				</td>
				</form>
			</tr>
			<tr>
				<td  colspan="3"><div class="inline_text_note"><%= tGeoloc.getType() %></div></td>
			</tr>
			<%
			} // end while tGeolocsI.hasNext()
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
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>

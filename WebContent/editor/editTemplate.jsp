<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.manuscripts.*,com.iBook.datastore.characters.*,com.iBook.datastore.locations.*" %>
		<%
		String userId = request.getRemoteUser();
		String msg = request.getParameter("msg");
		PropsManager propsMgr = new PropsManager(userId);
		String templateId = request.getParameter("templateId");
		TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
		int numTextblocks = templateProfile.getNumTextBlocks();
		String numTextblocksStr = new Integer(numTextblocks).toString();
		String title = templateProfile.getTitle();
		String author = templateProfile.getAuthor();
		String numStr = "";
		String pubtype = templateProfile.getPubtype();
		String pubdate = templateProfile.getPubdate();
		
		int characterCount = templateProfile.getCharacterCount();
		int locationsCount = templateProfile.getLocationsCount();
		int geolocsCount = templateProfile.getGeolocsCount();
		
		Iterator<CharacterProfile> tCharsI = null;
		Iterator<LocationProfile> tLocsI = null;
		Iterator<GeolocaleProfile> tGeolocsI = null;
		
		Vector<CharacterProfile> templateCharacters = templateProfile.getCharacters();
		Vector<LocationProfile> templateLocations = templateProfile.getLocations();
		Vector<GeolocaleProfile> templateGeolocs = templateProfile.getGeolocs();
		
		%>
	</head>
	<%
	if(msg != null && msg.length() > 0)
	{
	%>
		<body onload="alert('<%= msg %>');">
	<%
	}
	else
	{
	%>
		<body>
	<%
	}
	%>
		<%@include file="header.jsp"%>
		<p class="section_head">
			Template Editor
		</p>

	<!--  Template description -->
	
	<p>
	Source book: <i><%= title %></i>
	</p>
	<p>
	<%
	if(pubtype.length() > 0)
	{
	%>
	A <%= pubtype %> by 
	<%
	}
	else
	{
	%>
	By 
	<%
	}
	%>
	<%= author %><%
	if(pubdate.length() > 0)
	{
	%>, originally published in <%= pubdate %><%
	}
	%>. <input type="button" name="editTemplateDetailsButton" onclick="document.location='editTemplatePubDetails.jsp?templateId=<%= templateId %>'" value="Edit Details" />
	</p>		
	<hr/>
	
<!--------------------------------------------------------------------------------
	
	                                    Characters
	                                     
---------------------------------------------------------------------------------->
	
	<p>
	<i><%= title %></i> characters available for modification: 
	</p>
	<%
	if(characterCount < 1)
	{
	%>
		<p>
		None.
		</p>
	<%
	}
	else
	{
	%>
	<form name="templateCharacterList" action="../ManageTemplate" method="post">
		<select name="charId" id="charId" onchange="charId.options[selectedIndex].value">
	<%
		numStr = Integer.toString(characterCount);
		CharacterProfile tCharProfile = null;
		String tCharName = "";
		String tCharId = "";
		tCharsI = templateCharacters.iterator();
		while(tCharsI.hasNext())
		{
			tCharProfile = (CharacterProfile)tCharsI.next();
			tCharName = tCharProfile.getName();
			tCharId = tCharProfile.getId();
			%>
			<option value="<%= tCharId %>">
				<%= tCharName %>
			</option>
			<%
		}
		%>
		</select>
		<input type="submit" name="editTemplateCharSubmit" value="Edit" />
		<input type="submit" name="deleteTemplateCharSubmit" value="Delete Selected Character" />
		<input type="hidden" name="charId" value="<%= tCharId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" %>
	</form>
	</p>
	<%
	}
	%> 
	<p>
	
	<form name="createTemplateCharacterForm" action="../ManageTemplate" method="post">
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="submit" name="createTemplateCharacterSubmit" id="createTemplateCharacterSubmit" value="Add Character" />
		<input type="hidden" name="userId" value="<%= userId %>" %>
	</form>
	
	</p>
	
	<hr/>
	
<!--------------------------------------------------------------------------------
	
	                                    Locations
	                                     
---------------------------------------------------------------------------------->
	
	<p>
	<i><%= title %></i> locations available for modification: 
	</p>
	<%
	if(locationsCount < 1)
	{
	%>
		<p>
		None.
		</p>
	<%
	}
	else
	{
	%>
		<form name="templateLocationsList" action="../ManageTemplate" method="post">
			<select name="locId" id="locId" onchange="locId.options[selectedIndex].value">
	<%
		numStr = Integer.toString(locationsCount);
		LocationProfile tLocProfile = null;
		String tLocName = "";
		String tLocId = "";
		tLocsI = templateLocations.iterator();
		while(tLocsI.hasNext())
		{
			tLocProfile = (LocationProfile)tLocsI.next();
			tLocName = tLocProfile.getName();
			tLocId = tLocProfile.getId();
			%>
			<option value="<%= tLocId %>">
				<%= tLocName %>
			</option>
			<%
		}
		%>
		</select>
		<input type="submit" name="editTemplateLocSubmit" value="Edit" />
		<input type="submit" name="deleteTemplateLocSubmit" value="Delete Selected Location" />
		<input type="hidden" name="LocId" value="<%= tLocId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" %>
	</form>
	</p>
	<%
	}
	%> 
	<p>
	
	<form name="createTemplateLocationForm" action="../ManageTemplate" method="post">
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="submit" name="createTemplateLocationSubmit" id="createTemplateLocationSubmit" value="Add Location" />
		<input type="hidden" name="userId" value="<%= userId %>" %>
	</form>
	
	</p>
	
	<hr/>
	
<!--------------------------------------------------------------------------------
	
	                                    Geolocales 
	                                     
---------------------------------------------------------------------------------->
	
	<p>
	<i><%= title %></i> regions available for modification: 
	</p>
	<%
	if(geolocsCount < 1)
	{
	%>
		<p>
		None.
		</p>
	<%
	}
	else
	{
	%>
		<form name="templateGeolocaleList" action="../ManageTemplate" method="post">
			<select name="geolocId" id="geolocId" onchange="geolocId.options[selectedIndex].value">
	<%
		numStr = Integer.toString(geolocsCount);
		GeolocaleProfile tGeolocProfile = null;
		String tGeolocName = "";
		String tGeolocId = "";
		tGeolocsI = templateGeolocs.iterator();
		while(tGeolocsI.hasNext())
		{
			tGeolocProfile = (GeolocaleProfile)tGeolocsI.next();
			tGeolocName = tGeolocProfile.getName();
			tGeolocId = tGeolocProfile.getId();
			%>
			<option value="<%= tGeolocId %>">
				<%= tGeolocName %>
			</option>
			<%
		}
		%>
		</select>
		<input type="submit" name="editTemplateGeolocSubmit" value="Edit" />
		<input type="submit" name="deleteTemplateGeolocSubmit" value="Delete Selected Region" />
		<input type="hidden" name="geolocId" value="<%= tGeolocId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" %>
		<%
	}
	%> 
	</form>
	</p>
	<p>
	<p>
	
	<form name="createTemplateGeolocForm" action="../ManageTemplate" method="post">
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="submit" name="createTemplateGeolocSubmit" id="createTemplateGeolocSubmit" value="Add Region" />
		<input type="hidden" name="userId" value="<%= userId %>" %>
	</form>
	
	</p>
	
<!--------------------------------------------------------------------------------
	
	                                    Upload Source 
	                                     
---------------------------------------------------------------------------------->
	
	<hr/>
	<%
	String numTextBlocks = Integer.toString(templateProfile.getNumTextBlocks());
	%>
	<p>
	The <i><%= title %></i> template has <%= numTextBlocks %> blocks of text associated with it. If that's not correct, prepare a text file containing each block as a single, separate line of text, and upload it:
	</p> 
	<form name="uploadSrcForm" action="../UploadBookSource" method="post" enctype="multipart/form-data">
		<table>	
		<tr><td>Text source: </td><td><input type="file" name="textSource" size="50" /></td></tr>
		<tr><td></td><td style="padding-top: 8px"><div class="input_text_note">(Plain text file containing the source. Uploading a new file will overwrite the existing text.)</div></td></tr>
		</table>
		<input type="submit" name="uploadSrcSubmit" id="uploadSrcSubmit" value="Upload Source Text" />
		<input type="button" name="formattingHelp" value="?" onclick="displayHelp('inputFormat.jsp')" />
		<input type="hidden" name="templateId"  value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" %>
	</form>
	
	
<!--------------------------------------------------------------------------------
	
	                                    Enable Substitutions 
	                                     
---------------------------------------------------------------------------------->
	
	<hr/>
	<input type="button" name="enableCharSubs" value="Enable Substitutions" onclick="if(validateSourceUpload('<%= numTextblocksStr %>')) document.location='enableSubstitutions.jsp?templateId=<%= templateId %>'" />

	<div class="send_back">
		<div class="send_back_pair">
			<div class="send_back_label">Return to</div>
            <div class="send_back_target"><a href="editTemplates.jsp">Edit Templates</a></div>
        </div>
		<div class="send_back_pair">
			<div class="send_back_label"></div>
            <div class="send_back_target"><a href="index.jsp">Start</a></div>
        </div>
	</div>
	</body>
</html>
		


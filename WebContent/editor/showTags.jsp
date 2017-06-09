<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<!-- Displays text of a textblock with all <...> tags supporessed; designed to be called from UpdateTextblock servlet -->

	<body>
	<%
	String userId = request.getRemoteUser();
 	PropsManager propsMgr = new PropsManager(userId);
	String templateId = request.getParameter("templateId");
	TemplateProfile templateProfile = propsMgr.getTemplateProfile(templateId);
	String textblockId = request.getParameter("textblockId");
	String textblockNum = textblockId.substring(textblockId.lastIndexOf("_") + 1);
	String textblock = templateProfile.getTextblock(textblockId);
	
	Vector<CharacterProfile> tChars = templateProfile.getCharacters();
	Vector<GeolocaleProfile> tGeolocs = templateProfile.getGeolocs();
	Vector<LocationProfile> tLocs = templateProfile.getLocations();

 	int idx = 0;
	int end = 0;

	String divStart = "<span class=\"highlight\">";
	String divEnd = "</span>";
	String tagStart = "###TAG-START###";
	String tagEnd = "###TGAG-END###";
	
	StringBuffer textblockBuff = new StringBuffer(textblock);
	
	textblockBuff.replace(0, textblockBuff.length(), Utilities.replaceChars(textblockBuff.toString(), "<", tagStart+ "&lt;", "all"));
	textblockBuff.replace(0, textblockBuff.length(), Utilities.replaceChars(textblockBuff.toString(), ">", "&gt;" + tagEnd, "all"));
	
	while((idx = textblockBuff.indexOf(tagStart)) != -1)
	{
		textblockBuff.replace(idx, idx + tagStart.length(), divStart);
	}
	while((idx = textblockBuff.indexOf(tagEnd)) != -1)
	{
		textblockBuff.replace(idx, idx + tagEnd.length(), divEnd);
	}
	%>
	<p>
 	<span class="inline_text_note">Textblock <%= textblockNum %> (All text marked with &lt; and &gt; characters, including HTML tags if present, will
	be highlighted. Scroll down to see names matched to <i>eBook</i> IDs.)</span>
	</p>
	<p>
	<%= textblockBuff.toString() %>
	</p>
	<table>
		<tr>
			<th>Full Name</th><th>ID</th>
		</tr>
		<tr>
			<td><p class="section_subhead" style="margin-top: 15px">Characters</p></td>
		</tr>
		
		<%
			Iterator<CharacterProfile> tCharsI = tChars.iterator();
			CharacterProfile tChar = null;
			while(tCharsI.hasNext())
			{
				tChar = (CharacterProfile)tCharsI.next();
		%>
				<tr>
					<td><span class="inline_text_note"><%= tChar.getName() %></span></td>
					<td><span class="inline_text_note"><%= tChar.getId() %></span></td>
				</tr>
		<%
			}
		%>
		<tr>
			<td><p class="section_subhead" style="margin-top: 15px">Regions</p></td>
		</tr>
		<%
			Iterator<GeolocaleProfile> tGeolocsI = tGeolocs.iterator();
			GeolocaleProfile tGeoloc = null;
			while(tGeolocsI.hasNext())
			{
				tGeoloc = (GeolocaleProfile)tGeolocsI.next();
		%>
				<tr>
					<td><span class="inline_text_note"><%= tGeoloc.getName() %></span></td>
					<td><span class="inline_text_note"><%= tGeoloc.getId() %></span></td>
				</tr>
		<%
			}
		%>
			<tr>
				<td><p class="section_subhead" style="margin-top: 15px">Locations</p></td>
			</tr>
		<%
			Iterator<LocationProfile> tLocsI = tLocs.iterator();
			LocationProfile tLoc = null;
			while(tLocsI.hasNext())
			{
				tLoc = (LocationProfile)tLocsI.next();
		%>
				<tr>
					<td><span class="inline_text_note"><%= tLoc.getName() %></span></td>
					<td><span class="inline_text_note"><%= tLoc.getId() %></span></td>
				</tr>
		<%
			}
		%>
	</table>
	</body>
</html>
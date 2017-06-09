<html>
	<head>
		<title>Profile IDs</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<%
	String userId = request.getRemoteUser();
 	PropsManager propsMgr = new PropsManager(userId);
 	String objId = "";
	
	Vector<String> chars = propsMgr.getIdList(PropsManager.ObjType.CHARACTER);
	Vector<String> geolocs = propsMgr.getIdList(PropsManager.ObjType.GEOLOCALE);
	Vector<String> locs = propsMgr.getIdList(PropsManager.ObjType.LOCATION);
	Vector<String> books = propsMgr.getIdList(PropsManager.ObjType.BOOK);
	%>
	<p class="section_head">Profile IDs</p>
	
	<p>
	The <%= propsMgr.getIBookSystemName("short") %> uses identifiers (IDs) when referencing
	profile ojects.
	</p>
	<p>
	This can make it a bit daunting to edit raw <%= propsMgr.getIBookSystemName("short") %> 
	text, but it does help by insuring uniqueness among profiles of the same type even if they share
	names. 
	</p>
	<p>
	You may never need to refer to a profile's ID, but if you do, here are the IDs for objects 
	you have created:
	</p>

	<table>
		<tr>
			<th>Name</th><th>ID</th>
		</tr>
		<tr>
			<td><p class="section_subhead" style="margin-top: 15px">Books</p></td>
		</tr>
		<%
		if(books.size() == 0)
		{
			%>
			<tr><td>No books defined.</td></tr>
			<%
		}
		else
		{
			Iterator<String> booksI =  books.iterator();
			while(booksI.hasNext())
			{
				objId = (String)booksI.next();
		%>
				<tr>
					
					<td><span class="inline_text_note"><%= ((BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, objId)).getTitle() %></span></td>
					<td><span class="inline_text_note"><%= objId %></span></td>
					
				</tr>
		<%
			}
		}
		%>
		<tr>
			<td><p class="section_subhead" style="margin-top: 15px">Characters</p></td>
		</tr>
		<%
		if(chars.size() == 0)
		{
			%>
			<tr><td>No characters defined.</td></tr>
			<%
		}
		else
		{
			Iterator<String> charsI =  chars.iterator();
			while(charsI.hasNext())
			{
				objId = (String)charsI.next();
		%>
				<tr>
					<td><span class="inline_text_note"><%= ((CharacterProfile)propsMgr.getProfile(PropsManager.ObjType.CHARACTER, objId)).getName() %></span></td>
					<td><span class="inline_text_note"><%= objId %></span></td>
				</tr>
		<%
			}
		}
		%>
		<tr>
			<td><p class="section_subhead" style="margin-top: 15px">Regions</p></td>
		</tr>
		<%
		if(geolocs.size() == 0)
		{
			%>
			<tr><td>No regions defined.</td></tr>
			<%
		}
		else
		{
			Iterator<String> geolocsI =  geolocs.iterator();
			while(geolocsI.hasNext())
			{
				objId = (String)geolocsI.next();
		%>
				<tr>
					<td><span class="inline_text_note"><%= ((GeolocaleProfile)propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, objId)).getName() %></span></td>
					<td><span class="inline_text_note"><%= objId %></span></td>
				</tr>
		<%
			}
		}
		%>
			<tr>
				<td><p class="section_subhead" style="margin-top: 15px">Locations</p></td>
			</tr>
		<%
		if(locs.size() == 0)
		{
			%>
			<tr><td>No locations defined.</td></tr>
			<%
		}
		else
		{
			Iterator<String> locsI =  locs.iterator();
			while(locsI.hasNext())
			{
				objId = (String)locsI.next();
		%>
				<tr>
					<td><span class="inline_text_note"><%= ((LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, objId)).getName() %></span></td>
					<td><span class="inline_text_note"><%= objId %></span></td>
				</tr>
		<%
			}
		}
		%>
	</table>
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
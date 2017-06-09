<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	
	<style>
	li
	{
		font-size: .7em;
	}
	</style>

	<body>
	<%
	String userId = request.getRemoteUser();
	String templateId = request.getParameter("templateId");
 	PropsManager propsMgr = new PropsManager(userId);
 	TemplateProfile tProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
 	Vector<CharacterProfile> tChars = tProfile.getCharacters();
 	Vector<LocationProfile> tLocs = tProfile.getLocations();
 	Vector<GeolocaleProfile> tGeolocs = tProfile.getGeolocs();
 	Vector<String> aliases = null;
	Iterator<CharacterProfile> charsI = tChars.iterator();
	Iterator<LocationProfile> locsI = tLocs.iterator();
	Iterator<GeolocaleProfile> geolocsI = tGeolocs.iterator();
	Iterator<String> aliasesI = null;
 	CharacterProfile charProfile = null;
 	LocationProfile locProfile = null;
 	GeolocaleProfile geolocProfile = null;
 	String name = null;
 	boolean haveCharAlias = false;
 	boolean haveLocAlias = false;
 	boolean haveGeolocAlias = false;
	%>
	<p class="section_head">
	Aliases 
	</p>
	<p class="inline_text_note">
	Only objects that have at least one alias defined will be listed.
	</p>
	<p class="section_subhead">
	Characters
	</p>
	<%
	while(charsI.hasNext())
	{
		charProfile = (CharacterProfile)charsI.next();
		aliases = charProfile.getAliases();
		if(aliases.size() > 0)
		{
			haveCharAlias = true;
			%>
			<ul>
				<li><%= charProfile.getName() %></li>
				<ul>
				<%
					aliasesI = aliases.iterator();
					while(aliasesI.hasNext())
					{
						%>
							<li><%= aliasesI.next() %></li>
						<%
					}
				%>
				</ul>
			</ul>
			<%
		}
	}
	if(!haveCharAlias)
	{
		%>
		<p class="inline_text_note">
		There is no character with a defined alias. 
		</p>
		<%
	}
	%>
	<p class="section_subhead">
	Locations
	</p>
	<%
	while(locsI.hasNext())
	{
		locProfile = (LocationProfile)locsI.next();
		aliases = locProfile.getAliases();
		if(aliases.size() > 0)
		{
			haveLocAlias = true;
			%>
			<ul>
				<li><%= locProfile.getName() %></li>
				<ul>
				<%
					aliasesI = aliases.iterator();
					while(aliasesI.hasNext())
					{
						%>
							<li><%= aliasesI.next() %></li>
						<%
					}
				%>
				</ul>
			</ul>
			<%
		}
	}
	if(!haveLocAlias)
	{
		%>
		<p class="inline_text_note">
		There is no location with a defined alias. 
		</p>
		<%
	}
	%>
	<p class="section_subhead">
	Regions
	</p>
	<%
	while(geolocsI.hasNext())
	{
		geolocProfile = (GeolocaleProfile)geolocsI.next();
		aliases = geolocProfile.getAliases();
		if(aliases.size() > 0)
		{
			%>
			<ul>
				<li><%= geolocProfile.getName() %></li>
				<ul>
				<%
					aliasesI = aliases.iterator();
					while(aliasesI.hasNext())
					{
						haveGeolocAlias = true;
						%>
							<li><%= aliasesI.next() %></li>
						<%
					}
				%>
				</ul>
			</ul>
			<%
		}
	}
	if(!haveGeolocAlias)
	{
		%>
		<p class="inline_text_note">
		There is no region with a defined alias. 
		</p>
		<%
	}
	%>
	</body>
</html>
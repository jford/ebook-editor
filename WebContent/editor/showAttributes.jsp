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
 	Vector<LocationProfile> tLocs = tProfile.getLocations();
 	Vector<GeolocaleProfile> tGeolocs = tProfile.getGeolocs();
 	Vector<String> attributes = null;
 	Vector<String> aliases = null;
	Iterator<LocationProfile> locsI = tLocs.iterator();
	Iterator<GeolocaleProfile> geolocsI = tGeolocs.iterator();
	Iterator<String> attributesI = null;
	Iterator<String> aliasesI = null;
 	LocationProfile locProfile = null;
 	GeolocaleProfile geolocProfile = null;
 	String name = null;
 	boolean haveLocAtt = false;
 	boolean haveGeolocAtt = false;
	%>

<!--  Atributes -->

	<p class="section_head">
	Attributes 
	</p>
	<p class="inline_text_note">
	Only places that have at least one attribute defined will be listed.
	</p>
	<p class="section_subhead">
	Locations
	</p>
	<%
	while(locsI.hasNext())
	{
		locProfile = (LocationProfile)locsI.next();
		attributes = locProfile.getLocationDescriptions();
		if(attributes.size() > 0)
		{
			haveLocAtt = true;
			%>
			<ul>
				<li><%= locProfile.getName() %></li>
				<ul>
				<%
					attributesI = attributes.iterator();
					while(attributesI.hasNext())
					{
						%>
							<li><%= attributesI.next() %></li>
						<%
					}
				%>
				</ul>
			</ul>
			<%
		}
	}
	if(!haveLocAtt)
	{
		%>
		<p class="inline_text_note">
		There is no location with a defined attribute. 
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
		attributes = geolocProfile.getDescriptions();
		if(attributes.size() > 0)
		{
			%>
			<ul>
				<li><%= geolocProfile.getName() %></li>
				<ul>
				<%
				attributesI = attributes.iterator();
					while(attributesI.hasNext())
					{
						haveGeolocAtt = true;
						%>
							<li><%= attributesI.next() %></li>
						<%
					}
				%>
				</ul>
			</ul>
			<%
		}
	}
	if(!haveGeolocAtt)
	{
		%>
		<p class="inline_text_note">
		There is no region with a defined attribute. 
		</p>
		<%
	}
	%>

<!--  Aliases -->

<%
	locsI = tLocs.iterator();
	geolocsI = tGeolocs.iterator();
	boolean haveLocAlias = false;
	
%>
	<p class="section_head">
	Aliases 
	</p>
	<p class="inline_text_note">
	Only places that have at least one alias defined will be listed.
	</p>
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
	boolean haveGeolocAlias = false;
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
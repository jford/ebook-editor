<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<%
	String userId = request.getRemoteUser();
	String charId = request.getParameter("charId");
 	PropsManager propsMgr = new PropsManager(userId);
 	String templateId = request.getParameter("templateId");
 	CharacterProfile charProfile = null;
 	boolean isTemplateChar = templateId.equals("") ? false : true;
 	if(isTemplateChar)
 	{
	 	TemplateProfile tProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
	 	charProfile = tProfile.getCharacter(charId);
 	}
 	else
 		charProfile = (CharacterProfile)propsMgr.getCharacterProfile(charId);
 	String name = charProfile.getName();
 	Vector<String> aliases = charProfile.getAliases();
 	Vector<String> attributes = charProfile.getAttributes();
 	Iterator<String> aliasesI = aliases.iterator();
 	Iterator<String> attributesI = attributes.iterator();
 	String alias = "";
 	String attribute = "";
	%>
	<p class="section_head">
	<%= name %>
	</p>
	<p class="inline_text_note">
	(<%= propsMgr.getIBookSystemName("short") %> ID: <%= charId %>)
	</p>
	
	<!--  Names -->
	
	<p class="section_subhead">
	Names
	</p>
	<ul>
		<li><span style="color: blue">Formal name:</span> <%= charProfile.getNameFormal() %><br/>
		<span class="inline_text_note" style="line-height: 1em">(Prefix, first and last names; 
		if no prefix or first name, follow logic of <i>Full Name</i></i>)</span><br/></li>
		<li><span style="color: blue">Full name:</span> <%= name %><br/>
		<span class="inline_text_note" style="line-height: 1em">(Familiar and last names; if no familiar name, first and last names;
		if no familiar or first name, prefix and last name; if no familiar, first or prefix name, last name only;
		if no first or last name, prefix only)</span><br/></li>
		<li><span style="color: blue">Prefix:</span> <%= charProfile.getNamePrefix() %></li>
		<li><span style="color: blue">First name:</span> <%= charProfile.getNameFirst() %></li>
		<li><span style="color: blue">Middle name:</span> <%= charProfile.getNameMiddle() %></li>
		<li><span style="color: blue">Last name:</span> <%= charProfile.getNameLast() %></li>
		<li><span style="color: blue">Suffix:</span> <%= charProfile.getNameSuffix() %></li>
		<li><span style="color: blue">Familiar (short) name:</span> <%= charProfile.getShortName() %></li>
	</ul>
	
	<!--  Aliases -->
	
	<p class="section_subhead">
	Aliases
	</p>
	<%
	if(aliases.size() > 0)
	{
	%>
		<ul>
		<%
		while(aliasesI.hasNext())
		{
			alias = aliasesI.next();
		%>
			<li><%= alias %></li>
		<%
		}
		%>
		</ul>
	<%
	}
	else
	{
	%>
		There are no aliases defined for <%= name %>.
	<%
	}
	%>
	
	<!--  Attributes -->
	
	<p class="section_subhead">
	Attributes
	</p>
	<%
	if(attributes.size() > 0)
	{
	%>
		<ul>
		<%
		while(attributesI.hasNext())
		{
			attribute = attributesI.next();
		%>
			<li><%= attribute %></li>
		<%
		}
		%>
		</ul>
	<%
	}
	else
	{
	%>
		There are no attributes defined for <%= name %>.
	<%
	}
	%>
	</body>
</html>
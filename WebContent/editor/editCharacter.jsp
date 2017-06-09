<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.characters.*,com.iBook.datastore.manuscripts.*,com.iBook.utilities.*" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		int numChars = propsMgr.getCharacterCount();
		String charId = request.getParameter("charId");
		String templateId = request.getParameter("templateId");
		TemplateProfile templateProfile = null;
		CharacterProfile profile = null;
		boolean isTemplateProfile = false;
		if(templateId != null && templateId.compareTo("null") != 0)
		{
			templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
			profile =  templateProfile.getCharacter(charId);
			isTemplateProfile = true;
		}
		else
			profile = propsMgr.getCharacterProfile(charId);
		String gender = profile.getGender();
		Vector<String> attributes = profile.getAttributes();
		Vector<String> books = profile.getBooks();
		Vector<String> aliases = profile.getAliases();
		String character_name = "";
		String nameShort = "";
		String age = "";
		String namePrefix = "";
		String nameFirst = "";
		String nameMiddle = "";
		String nameLast = "";
		String nameSuffix = "";
		String nameFull ="";
		String nameFormal = "";
		
		if(profile != null)
		{
			character_name = profile.getName();
			gender = profile.getGender();
			if(gender.length() == 0)
				gender = "not specified";
			age = profile.getAge();
			if(age.length() == 0)
				age = "not specified";
			nameShort = profile.getShortName();
			namePrefix = profile.getNamePrefix();
			nameFirst = profile.getNameFirst();
			nameMiddle = profile.getNameMiddle();
			nameLast = profile.getNameLast();
			nameSuffix = profile.getNameSuffix();
			nameFull = profile.getName();
			nameFormal = profile.getNameFormal();
		}
		%>
	</head>
	<body onload="setGender('<%= gender %>')">
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Character Editor
		</p>
		<p>
	Name: <%= character_name  %> <br/>
	<span class="inline_text_note">(<%= PropsManager.getIBookSystemName() %> ID: <i><%= charId %></i>) 
	<input type="button" value="?" id="smallHelpButton" onclick="displayHelp('profileIds.jsp')" /></span>
	<%
	if(isTemplateProfile)
	{
	%>
		<form name="setContext" action="../ManageProfile" method="post">
			Context in source text: <br/>
			<span class="inline_text_note">(Brief note displayed on the <i>Book Editor's</i> character assignment page.)</span><br/>
			<input type="text" name="context" id="context" value="<%= profile.getContext() %>" size="50"/>		
			<input type="submit" name="setContextSubmit" value="Update Context"/>
			<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
			<input type="hidden" name="charId" value="<%= charId %>" />
			<input type="hidden" name="templateId" value="<%= templateId %>" />
			<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
	<%
	}
	%>
		<ul>
			<li>full name: <i><%= nameFull %></i><br/>
		<span class="inline_text_note" style="line-height: 1em">(Familiar and last names; if no familiar name, first and last names;
		if no familiar or first name, prefix and last name; if no familiar, first or prefix name, last name only;
		if no first or last name, prefix)</span><br/></li>
		<%
		String formalName = profile.getNameFormal();
		if(formalName.compareTo(character_name) != 0)
		{
		%>
			<li>formally: <i><%= formalName %></i><br/>
			<span class="inline_text_note" style="line-height: 1em">(Prefix, first and last names; if no prefix, first and last names;
		if no familiar or first name, prefix and last name; if no prefix or first name, follow logic of <i>Full Name</i></i>)</span><br/></li>
		<%
		}
		%>
		</ul>
		
		<div class="text_note_no_border">
		You can designate one or more (any combination) of these name components: 
		<ul>
			<li>prefix (Mr./Ms./Royal Excellency)</li>
			<li>first, middle and last names</li>
			<li>suffix (Jr./Sr./Esq.)</li>
			<li>short (or familiar, informal) name</li>
		</ul> 
		There must be at least a short name. If you don't designate one, it will be  
		derived from the values of the other entries, as follows: If there is a first  
		name, short name will be set to it. If no first name has been set, then short  
		name will be set to the value of middle name. If no middle name, then last  
		name, if no last name then prefix, if no prefix then suffix.
		</div>
		<form name="setNames" action="../ManageProfile" method="post">
		<table>
			<tr>
				<td>Prefix: </td><td><input type="text" name="namePrefix" id="namePrefix" value="<%= namePrefix %>" size="30"/></td>
			</tr>
			<tr>
				<td>First: </td><td><input type="text" name="nameFirst" id="nameFirst" value="<%= nameFirst %>" size="30"/></td>
			</tr>
			<tr>
				<td>Middle: </td><td><input type="text" name="nameMiddle" id="nameMiddle" value="<%= nameMiddle %>" size="30"/></td>
			</tr>
			<tr>
				<td>Last: </td><td><input type="text" name="nameLast" id="nameLast" value="<%= nameLast %>" size="30"/></td>
			</tr>
			<tr>
				<td>Suffix: </td><td><input type="text" name="nameSuffix" id="nameSuffix" value="<%= nameSuffix %>" size="30"/></td>
			</tr>
			<tr>
				<td>Short (familiar): </td><td><input type="text" name="nameShort" id="nameShort" value="<%= nameShort %>" size="30"/></td>
			</tr>
		</table>		
		<input type="submit" name="setNamesSubmit" value="Update Names" onclick="return characterHasName();"/>
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
		<input type="hidden" name="charId" value="<%= charId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<hr/>
		<p>
		<form name="setGender" action="../ManageProfile" method="post">
<!-- 
		Gender is <input name="gender" size="30" value="<%= gender %>" /> 
-->
		Gender is <input type="radio" name="gender" id="male" value="male" /> male <input type="radio" name="gender" id="female" value="female"/> female
		<input type="submit" name="updateGender" value="Update Gender" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
		<input type="hidden" name="charId" value="<%= charId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form> 
		</p>
		<hr/>
		<p>
		<form name="setAge" action="../ManageProfile" method="post">
		Age is <input name="age" size="30" value="<%= age %>" />
		<input type="submit" name="updateAge" value="Update Age" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
		<input type="hidden" name="charId" value="<%= charId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form> 
		</p>
		<hr/>
		<%
		if(aliases.size() > 0)
		{
		%>
			<p>
			<%= character_name %> is also known as:
			</p>
			<ul>
			<% 
			String aka = "";
			Iterator<String> aliasesI = aliases.iterator();
			while(aliasesI.hasNext())
			{
				aka = (String)aliasesI.next();
			%>
			<li><%= aka %></li>
			<%
			} // while aliasesI.hasNext()
			%>
			</ul>
			<input type="button" name="editAliases" value="Edit Aliases" onclick="document.location='editCharacterAliases.jsp?templateId=<%= templateId %>&charId=<%= charId %>'" />
			
		<% 
		} // if aliases.size() > 0
		%>
		<form name="aka" action="../ManageProfile" method="post">
		Another name for <%= character_name %> is: <input type="text" name="akaName" size="30"/>
		<input type="submit" name="addAlias" value="Add Alias" /><br/>
		<span class="inline_text_note">(You must click the <i>Add Alias</i> button after entering text in the input field, or else your entry will be lost.)</span>
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
		<input type="hidden" name="charId" value="<%= charId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<hr/>
		<%
		if(attributes.size() > 0)
		{
			%>
			<p>
			The character is defined by the following attributes:
			</p>
			<ul>
			<%
			String attribute = "";
			Iterator<String> attsI = attributes.iterator();
			while(attsI.hasNext())
			{
				attribute = (String)attsI.next();
				%>
				<li><%= attribute %></li>
				<%
			}
			%>
			</ul>
			<input type="button" value="Edit Attributes" onclick="document.location='editCharAttributes.jsp?templateId=<%= templateId %>&charId=<%= charId %>'" />
		<%
		}
		else
		{
		%>
		    <p>
			No notable attributes have been defined for this character.
			</p>
		<%
		} 
		%>
		<form name="addAttribute" action="../ManageProfile" method="post">
			Add attribute:
		<input type="text" name="attributeText" size="50" />
		<input type="submit" name="updateAddAttribute" value="Add Attribute" /><br/>
		<%
		if(isTemplateProfile)
		{
		%>
			<span class="inline_text_note">(You must click the <i>Add Attribute</i> button after entering text in the input field, or else your entry will be lost.)</span>
		<%
		}
		%>
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.CHARACTER %>" />
		<input type="hidden" name="charId" value="<%= charId %>" />
		<input type="hidden" name="templateId" value="<%= templateId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
	<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to </div>
				<div class="send_back_target"><a href="editCharacters.jsp?templateId=<%=templateId %>">Edit Characters</a></div>
			</div>
			<%
			if(templateId != null && templateId.compareTo("null") != 0)
			{
			%>
				<div class="send_back_pair">
					<div class="send_back_label"></div>
					<div class="send_back_target"><a href="editTemplate.jsp?templateId=<%= templateId %>">Edit Template</a></div>
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
		


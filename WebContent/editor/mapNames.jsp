<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	</head>

	<body>
	<%
	String userId = request.getRemoteUser();
	String tCharId = request.getParameter("tCharId");
 	PropsManager propsMgr = new PropsManager(userId);
 	
 	String templateId = request.getParameter("templateId");
 	TemplateProfile tProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
 	CharacterProfile tCharProfile = tProfile.getCharacter(tCharId);
 	String name = tCharProfile.getName();
 	Vector<String> tAliases = tCharProfile.getAliases();
 	Vector<String> tAttributes = tCharProfile.getAttributes();
 	Iterator<String> tAliasesI = tAliases.iterator();
 	Iterator<String> tAttributesI = tAttributes.iterator();
 	String tAlias = "";
 	String tAttribute = "";
 	
 	String standinId = request.getParameter("standinId");
 	CharacterProfile standinCharProfile = propsMgr.getCharacterProfile(standinId);
 	String standinName = standinCharProfile.getName();
 	Vector<String> standinAliases = standinCharProfile.getAliases();
 	Vector<String> standinAttributes = standinCharProfile.getAttributes();
 	Iterator<String> standinAliasesI = standinAliases.iterator();
 	Iterator<String> standinAttributesI = standinAttributes.iterator();
 	String standinAlias = "";
 	String standinAttribute = "";
	%>
	<p class="section_head">
	Names for the <i><%= name %></i> Character
	</p>
	<table>
		<tr>
			<td></td><th>Template Profile</th><th>Book Profile</th>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">Prefix</span></td><td><%= tCharProfile.getNamePrefix() %></td><td><%= standinCharProfile.getNamePrefix() %></td>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">First</span></td><td><%= tCharProfile.getNameFirst() %></td><td><%= standinCharProfile.getNameFirst() %></td>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">Middle</span></td><td><%= tCharProfile.getNameMiddle() %></td><td><%= standinCharProfile.getNameMiddle() %></td>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">Last</span></td><td><%= tCharProfile.getNameLast() %></td><td><%= standinCharProfile.getNameLast() %></td>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">Suffix</span></td><td><%= tCharProfile.getNameSuffix() %></td><td><%= standinCharProfile.getNameSuffix() %></td>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">Familiar (short)</span></td><td><%= tCharProfile.getShortName() %></td><td><%= standinCharProfile.getShortName() %></td>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">Aliases</span></td>
			<td>
			<%
			if(tAliases.size() > 0)
			{
				while(tAliasesI.hasNext())
				{
					%>
					<%= tAliasesI.next() %><br/>
					<%
				}
			}
			%>
			</td>
			<td>
			<%
			if(standinAliases.size() > 0)
			{
				while(standinAliasesI.hasNext())
				{
					%>
					<%= standinAliasesI.next() %><br/>
					<%
				}
			}
			%>
			</td>
		</tr>
		<tr>
			<td style="text-align: right; color: blue"><span class="inline_text_note">Attributes</span></td>
			<td>
			<%
			if(tAttributes.size() > 0)
			{
				while(tAttributesI.hasNext())
				{
					%>
					<%= tAttributesI.next() %><br/>
					<%
				}
			}
			%>
			</td>
			<td>
			<%
			if(standinAttributes.size() > 0)
			{
				while(standinAttributesI.hasNext())
				{
					%>
					<%= standinAttributesI.next() %><br/>
					<%
				}
			}
			%>
			</td>
		</tr>
	</table>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	Map template aliases to book-profile aliases on the character assignment 
	page. If there are no suitable aliases in the book-character profile, add 
	them in the character editor. If there is a template-profile value and no 
	comparable book-profile value, the template value will be used in the generated 
	file.<br/><br/>
	<i>Template profile ID: <%= tCharId %><br/>
	Standin (book) profile ID: <%= standinId %></i>   
	</p>
	</body>
</html>
<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	<%
/* 	Mapping Aliases and Attributes

	Alias and attribute mappings are stored in the book profile definition in bookList.xml.
	
	When parsed by the BookListParser, the information is written to the 
	ObjSubstitutionManager's aliasSubmatrix or attributeSubmatrix vector. The ObjSubstitutionManager is not 
	directly accessible. Use a BookProfile object to interact with the substitution
	matrix.  All alias mappings---character, location, and region---are stored in the same
	aliasSubmatrix vector, all attribute mappings in the attributeSubmatrix. They are differentiated 
	by the value of the IDs each entry contains.
	
	The Set/Clear buttons on the Map pages (mapAlias.jsp. mapAttributes.jsp) call the ManageBook servelet 
	with the following params:

		- bookId
		- tObjId (the character, location or geolocale object defined in the template)
		- tObjAlias/tObjAttribute (the text identified as an alias or attribute in the template)
		- substituteText (the string to be used in place of the template defined alias or attribute). 
		
	A BookProfile object is instantiated by a call to propsMgr.getBookProfile(), and the params 
	tObjId, tOjbAlias, tObjAttribute and substituteText are passed to the ObjSubstitutionManager through a call 
	to bookprofile.addTAliasSub() or bookprofile.addTAttributeSub(), which in turn calls the book profile's object substitution 
	manager's addAliasSubstitute() or addAttributeSubstitute() method. That call adds the new alias/attribute substitution information 
	to an aliasSubMatrix or attributeSubMatrix vector.

	Control is then returned to the ManageBook servelet and a new bookList.xml is written. 
	
	Control returns to the mapAliases.jsp or mapAttributes.jsp page.
	 
	Both set and clear buttons traverse the same code path; if setting a substitution, 
	substitueText will contain a string. If clearing a substitution, substituteText will 
	be set to "" in the ManageBook servelet. 
 */
	%>		
	<style>
	div.continue
	{
		margin-left: 10px;
		margin-right: 375px;
		background-color: yellow;
		padding: 10px;
	}
	</style>

</head>
	<body>
		<%@ include file="header.jsp" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String bookId = request.getParameter("bookId");
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		
		String projectName = bookProfile.getTitle();
		String templateId = bookProfile.getTemplateId();
		TemplateProfile tProfile = propsMgr.getTemplateProfile(templateId);
		
		Vector<String[]> unmappedAliases = bookProfile.getUnmappedAliases(tProfile);
		Iterator aliasesI = unmappedAliases.iterator();
		
		Vector<String[]> unmappedAttributes = bookProfile.getUnmappedAttributes(tProfile);
		Iterator attributesI = unmappedAttributes.iterator();
		
		String[] unmappedEntry = null;

		String objName = "undefined";
		String prevObjName = "undefined";
		String tObjName = "undefined";
		String aliasText = "undefined";
		String attributeText = "undefined";
		
		%>
		
		<p class="section_head">Unmapped Aliases/Attributes</p>

        <p>
        As itemized in the table below, you have substituted your own profiles for 
        original-source profiles but have not provided alternates for their aliases/attributes. This
		may result in some misunderstandings in the finished work. 
		</p>
		<p>
		In many if not most novels, people and places are often referred to by 
		aliases---something other than their formal names. In the template for <i>The Adventures 
		of Tom Sawyer</i>, for example, Tom is sometimes referred to as <i>the boy</i> 
		or <i>lad</i> or even <i>TOM</i> (all uppercase) when Aunt Polly yells at or for him.
		</p>
		<p>
		Locations and regions may have unique characteristics&mdash;attributes. In the novel <i>Dracula</i>, 
		for example, Jonathan Harker finds a copy of an <i>English Bradshaw's Guide</i> in the Count's 
		Translyvania castle, which the Count has apparently been using to become familiar with his intended new home. 
		</p>
		<p>
		These aliases or attributes may not be appropriate for your substitutions&mdash;if you change 
		the gender of the title character in a book derived from the Tom Sawyer template, 
		you would probably want to change <i>boy</i> to <i>girl</i> as the character's alias. If you've 
		redirected Dracula's intended destination to California, perhaps <i>Sunset Magazine</i> might be a 
		more locale-appropriate reference.
		</p>
		<div class="continue">
		I understand, continue anyway. 
		<input style="margin-top: 3px" type="button" value="Publish As Is" onclick="document.location='generate.jsp?useUnmapped=yes'" />
		</div>
		<p>
		Click the <i>Publish As Is</i> button, above, to continue the publishing process.
		</p>
		<p>
		Or, you can go back and map to aliases/attributes more appropriate for your custom profiles.  
		</p>
		<div class="continue" style="background-color: lightgreen">
		Okay, I want to fix things.
		<input type="button" value="Map Aliases/Attributes" onclick="document.location='editBook.jsp?bookId=<%= bookId %>'" />
		<input type="button" value="?" onclick="displayHelp('aliasesVsAttributes.jsp')" />
		</div>
		
		<%
		if(unmappedAliases.size() > 0)
		{
		%>
			
			<!-- unmapped aliases itemized -->
			
			<p class="section_subhead">Aliases</p>
			 
			<table>
				<tr>
					<th style="width: 250px">Profile</th><th style="width: 250px">Unmapped Alias</th>
				</tr>
				<%
				while(aliasesI.hasNext())
				{
					unmappedEntry = (String[])aliasesI.next();
					if(unmappedEntry[0].indexOf("_ibookchar_") != -1)
					{
						objName = propsMgr.getCharacterName(unmappedEntry[0]);
						tObjName = tProfile.getCharacter(unmappedEntry[1]).getName();
					}
					if(unmappedEntry[0].indexOf("_ibookloc_") != -1)
					{
						objName = propsMgr.getLocationName(unmappedEntry[0]);
						tObjName = tProfile.getLoc(unmappedEntry[1]).getName();
					}
					if(unmappedEntry[0].indexOf("_ibookgeoloc_") != -1)
					{
						objName = propsMgr.getGeolocaleName(unmappedEntry[0]);
						tObjName = tProfile.getGeoloc(unmappedEntry[1]).getName();
					}
					aliasText = unmappedEntry[2];
				%>
					<tr>
						<td><%= objName %> <% if(objName.compareTo(prevObjName) != 0) { %> (as <i><%= tObjName %></i>) <% } %></td>
						<td><%= aliasText %></td>
					</tr>
				<%
					prevObjName = objName;
				}                                                       
				%>
			</table>
		<%
		}
		if(unmappedAttributes.size() > 0)
		{
		%>
			<!-- unmapped attributes itemized -->
			
			<p class="section_subhead">
			Attributes
			</p>
			 
			<table>
				<tr>
					<th style="width: 250px">Profile</th><th style="width: 250px">Unmapped Attributes</th>
				</tr>
				<%
				while(attributesI.hasNext())
				{
					unmappedEntry = (String[])attributesI.next();
					if(unmappedEntry[0].indexOf("_ibookchar_") != -1)
					{
						objName = propsMgr.getCharacterName(unmappedEntry[0]);
						tObjName = tProfile.getCharacter(unmappedEntry[1]).getName();
					}
					if(unmappedEntry[0].indexOf("_ibookloc_") != -1)
					{
						objName = propsMgr.getLocationName(unmappedEntry[0]);
						tObjName = tProfile.getLoc(unmappedEntry[1]).getName();
					}
					if(unmappedEntry[0].indexOf("_ibookgeoloc_") != -1)
					{
						objName = propsMgr.getGeolocaleName(unmappedEntry[0]);
						tObjName = tProfile.getGeoloc(unmappedEntry[1]).getName();
					}
					attributeText = unmappedEntry[2];
				%>
					<tr>
						<td><%= objName %> <% if(objName.compareTo(prevObjName) != 0) { %> (as <i><%= tObjName %></i>) <% } %></td>
						<td><%= attributeText %></td>
					</tr>
				<%
					prevObjName = objName;
				}                                                       
				%>
			</table>
		<%
		}
		%>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="editCharacters.jsp">Character Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="editLocations.jsp">Location Editor</a></div>
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

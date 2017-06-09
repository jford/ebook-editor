<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	<%
/* 	Mapping Aliases

	Alias mappings are stored in the book profile definition in bookList.xml. (Attribute 
	mappings, managed from mapAttributes.jsp, are handled in the same way.) 
	
	When parsed by the BookListParser, the information is written to the 
	ObjSubstitutionManager's aliasSubmatrix vector. The ObjSubstitutionManager is not 
	directly accessible. Use a BookProfile object to interact with the substitution
	matrix.  All alias mappings---character, location, and region---are stored in the same
	aliasSubmatrix vector. They are differentiated by the value of the IDs each entry contains.
	
	The Set/Clear buttons on the Map Aliases page (mapAlias.jsp) call the ManageBook servelet 
	with the following params:

		- bookId
		- tObjId (the character, location or geolocale object defined in the template)
		- tObjAlias (the text identified as an alias in the template)
		- substituteText (the string to be used in place of the template defined alias). 
		
	A BookProfile object is instantiated by a call to propsMgr.getBookProfile(), and the params 
	tObjId, tOjbAlias, and substituteText are passed to the ObjSubstitutionManager through a call 
	to bookprofile.addTAliasSub(), which in turn calls the book profile's object substitution 
	manager's addAliasSubstitute() method. That call adds the new alias substitution information 
	to an aliasSubMatrix vector.

	Control is then returned to the ManageBook servelet and a new bookList.xml is written. 
	
	Control returns to the mapAliases.jsp page.
	 
	Both set and clear buttons traverse the same code path; if setting an alias substitution, 
	substitueText will contain a string. If clearing an alias substitution, substituteText will 
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
		String[] unmappedAliasEntry = null;
		Iterator aliasesI = unmappedAliases.iterator();
		
		String objName = "undefined";
		String prevObjName = "undefined";
		String tObjName = "undefined";
		String aliasText = "undefined";
		%>
		
		<p class="section_head">Unmapped Aliases</p>

        <p>
        As itemized in the table below, you have substituted your own profiles for 
        original-source profiles but have not provided alternates for their aliases. This
		may result in some misunderstandings in the finished work. 
		</p>
		<p>
		In many if not most novels, people and places are often referred to by 
		aliases---something other than their formal names. In the template for <i>The Adventures 
		of Tom Sawyer</i>, for example, Tom is sometimes referred to as <i>the boy</i> 
		or <i>lad</i> or even <i>TOM</i> (all uppercase) when Aunt Polly yells at or for him.
		</p>
		<p>
		These aliases may not be appropriate for your substitutions&mdash;if you change 
		the gender of the title character in a book derived from the Tom Sawyer template, 
		you would probably want to change <i>boy</i> to <i>girl</i> as the character's alias..
		</p>
		<div class="continue">
		Okay, I understand, but I want to continue anyway. 
		<input style="margin-top: 3px" type="button" value="Publish With Unmapped Aliases" onclick="document.location='generateEpub.jsp?useUnmappedAliases=yes'" />
		</div>
		<p>
		Click the <i>Publish with Unmapped Aliases</i> button, above, to continue the publishing process.
		</p>
		<p>
		Or, you can go back and map to aliases more appropriate for your custom profiles.  
		</p>
		<div class="continue" style="background-color: lightgreen">
		Okay, I want to fix things.
		<input type="button" value="Map Aliases" onclick="document.location='editBook.jsp?bookId=<%= bookId %>'" />
		<input type="button" value="?" onclick="displayHelp('aliases.jsp')" />
		</div>
		<table style="margin-top: 20px">
			<tr>
				<th>Profile</th><th>Unmapped Alias</th>
			</tr>
			<%
			while(aliasesI.hasNext())
			{
				unmappedAliasEntry = (String[])aliasesI.next();
				if(unmappedAliasEntry[0].indexOf("_ibookchar_") != -1)
				{
					objName = propsMgr.getCharacterName(unmappedAliasEntry[0]);
					tObjName = tProfile.getCharacter(unmappedAliasEntry[1]).getName();
				}
				if(unmappedAliasEntry[0].indexOf("_ibookloc_") != -1)
				{
					objName = propsMgr.getLocationName(unmappedAliasEntry[0]);
					tObjName = tProfile.getLoc(unmappedAliasEntry[1]).getName();
				}
				if(unmappedAliasEntry[0].indexOf("_ibookgeoloc_") != -1)
				{
					objName = propsMgr.getGeolocaleName(unmappedAliasEntry[0]);
					tObjName = tProfile.getGeoloc(unmappedAliasEntry[1]).getName();
				}
				aliasText = unmappedAliasEntry[2];
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

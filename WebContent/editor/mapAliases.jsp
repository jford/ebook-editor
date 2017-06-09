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


</head>
	<body>
		<%@ include file="header.jsp" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String bookId = request.getParameter("bookId");
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		String projectName = bookProfile.getTitle();
		String tObjId = request.getParameter("tObjId");
		// if doing a character, will need a standinId for showNames.jsp
		String standinId = "";
		
		boolean mapCharAlias = tObjId.indexOf("_char_") != -1 ? true : false;
		boolean mapGeolocAlias = tObjId.indexOf("_geoloc_") != -1 ? true : false;
		boolean mapLocAlias = tObjId.indexOf("_loc_") != -1 ? true : false;
		
		String templateId = bookProfile.getTemplateId();
		TemplateProfile tProfile = propsMgr.getTemplateProfile(templateId);
		
		// only one profile can be valid; two of the profile objects will be null...
		CharacterProfile tCharProfile = mapCharAlias ? tProfile.getCharacter(tObjId) : null;
		GeolocaleProfile tGeolocProfile = mapGeolocAlias ? tProfile.getGeoloc(tObjId) : null;
		LocationProfile tLocProfile = mapLocAlias ? tProfile.getLoc(tObjId) : null;
		
		Object iBookObjProfile = null;

		String tObjName = tCharProfile != null ? 
				tCharProfile.getName() : 
					tLocProfile != null ? 
							tLocProfile.getName() : 
								tGeolocProfile.getName();
		
		Vector<String> aliases = mapCharAlias ? tCharProfile.getAliases() : mapGeolocAlias ? tGeolocProfile.getAliases() : tLocProfile.getAliases();
		
		Vector<String> standinAliases = null;
		Vector<String[]> subMatrix = mapCharAlias ? bookProfile.getCharSubMatrix() : mapGeolocAlias ? bookProfile.getGeolocSubMatrix() : bookProfile.getLocSubMatrix();

		String[] subEntry = null;
		String iBookObjId = "";
		String iBookObjName = "";
		String iBookObjNumAliases = "";
		Iterator<String[]> matrixI = subMatrix.iterator();
		while(matrixI.hasNext())
		{
			subEntry = matrixI.next();
			if(subEntry[0].compareTo(tObjId) == 0)
			{
				iBookObjId = subEntry[1].length() > 0 ? subEntry[1] : "";
				if(mapCharAlias)
				{
					iBookObjProfile = propsMgr.getProfile(PropsManager.ObjType.CHARACTER, iBookObjId);
					iBookObjName = ((CharacterProfile)iBookObjProfile).getName();
					standinAliases = ((CharacterProfile)iBookObjProfile).getAliases();
					standinId = iBookObjId;
				}
				else if(mapGeolocAlias)
				{
					iBookObjProfile = propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, iBookObjId);
					iBookObjName = ((GeolocaleProfile)iBookObjProfile).getName();
					standinAliases = ((GeolocaleProfile)iBookObjProfile).getAliases();
				}
				else
				{
					iBookObjProfile = propsMgr.getProfile(PropsManager.ObjType.LOCATION, iBookObjId);
					iBookObjName = ((LocationProfile)iBookObjProfile).getName();
					standinAliases = ((LocationProfile)iBookObjProfile).getAliases();
				}
				iBookObjNumAliases = new Integer(standinAliases.size()).toString();
				break;
			}
		}
		String alias = "";
		int numAliases = standinAliases.size();
		%>
		
		<p class="section_head">
			<i><%= tObjName %></i> <%= numAliases > 1 ? "Aliases" : "Alias" %>
			<input type="button" value="?" onclick="displayHelp('aliases.jsp')" />
		</p>
		<%
		if(iBookObjName.length() > 0)
		{
			String aliasQty = "";
			if(standinAliases.size() == 0)
				aliasQty = "aliases";
			else
				aliasQty = standinAliases.size() > 1 ? "aliases" : "alias";
		%>
			<p>
 			<%= iBookObjName %>, 
 			<%
 			// is tObj a character?
 			if(standinId.length() > 0)
 			{
 				// yes, show button for opening showNames.jsp
 			%>
	 			<input type="button" value="?" onclick="displayNamesForChar('<%= standinId %>','')" />
	 		<%
	 		}
	 		%>
 			the <i>eBook</i> profile assigned as 
 			<%= tObjName %>'s standin, has <%= iBookObjNumAliases %> <%= aliasQty %> defined.
 			</p> 
 			<%
 			if(standinAliases.size() > 0)
 			{
 			%>
 				<p>
	 			To use this text, select from the menu, or enter new text to be used in place 
	 			of <%= tObjName %>'s alias, and click <b>Set</b>.
	 			</p>
	 		<%
 			}
 			else
 			{
	 		%>
	 			<P>
	 		   Enter the text to be used as a substitute and click <b>Set</b>.
	 		   </P>
			<%
 			}  // end if(standinAliases.size > 0)
		} // end if(iBookObjName.length() > 0)
		else
		{
		%>
			<P>
			No <i>eBook</i> profile has been assigned as <%= tObjName %>'s standin. 
			</p>
			<p>
			Enter text to be used in place of each alias and click <b>Set</b>b>.
			</P>
		<%
		} // end else
		%> 
		<p>
			<table>
				<tr>
					<th>Current Alias</th><th>Substitute Text</th><th style="width: 180">Options</th>
				</tr>
				<tr>
					<td/>
					<td/>
					<td style="vertical-align: middle"><span class="inline_text_note">(Select from list or enter new text. To use the current value, select it from the menu and click <b>Set</b>.)</span></td>
					<td style="width: 50px"><span class="inline_text_note" style="color: red">Button pairs valid for single character only. Click for each change.</span></span>
				</td></td>
				</tr>
			 <%
				 Iterator<String> aliasesI = aliases.iterator();
			 	 String substituteText = "";
				 while(aliasesI.hasNext())
				 {
					 alias = aliasesI.next();
					 substituteText = bookProfile.getAliasSubText(tObjId, alias);
			 %>
					<uL>
						<form name="switchAliasTextForm2" action="../ManageBook" method="post">
							<tr>
								<td>
									<%= alias %>
								</td>
								<td>
								<%
								if(substituteText.length() > 0)
								{
								%>
									<%=  bookProfile.getAliasSubText(tObjId, alias)%>
								<%
								}
								else
								{
								%>
									<span class="inline_text_note">No substitution defined.</span>
								<%
								}
								%>
								</td>
								<td>
								<%
									// editable combo box uses CSS styles defined in iBook_styles.css; source from 
									// http://stackoverflow.com/questions/2141357/editable-select-element
								%>
										<div class="select-editable">
										  <select onchange="this.nextElementSibling.value=this.value">
										  	<option value=""></option>
											<%
											String standinAlias = "";
											Iterator<String> standinAliasesI = standinAliases.iterator();
											int aliasCount = 0;
											String aliasCountStr = "";
											%>
											<option value="<%= alias %>"><%= alias %></option>
											<%
											while(standinAliasesI.hasNext())
											{
												
												aliasCountStr = new Integer(aliasCount++).toString();
												standinAlias = standinAliasesI.next();
												%>
												<option value="<%= standinAlias %>"><%= standinAlias %></option>
												<%
											}
											%>
										  </select>
										  <input type="text" name="substituteText" value=""/>
										</div>
									<%
									// end editable combo box source from stackoverflow
									%>
								</td>
								<td>
									<input type="submit" name="setAliasTextSubmit" id="smallSubmitButton" value="Set" />
									<input type="submit" name="clearAliasTextSubmit" id="smallSubmitButton" value="Clear" />
									<input type="hidden" name="bookId" value="<%= bookId %>" />
									<input type="hidden" name="tObjId" value="<%= tObjId %>" />
									<input type="hidden" name="tObjAlias" value="<%= alias %>" />
									<input type="hidden" name="userId" value="<%= userId %>" />
								</td>
							</tr>
						</form> <!--  mapAliasesForm -->
					</ul>
				 <%
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
				<div class="send_back_target"><a href="editBook.jsp?bookId=<%= bookId %>">Book Editor</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>

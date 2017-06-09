<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>

	<%
/* 	
		Attributes mappings are stored in the book profile definition in bookList.xml the same way as alias mappings.
		
		See the comment at the beginning of mapAlias.jsp for details.
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
		
		boolean mapCharAttribute = tObjId.indexOf("_char_") != -1 ? true : false;
		boolean mapGeolocAttribute = tObjId.indexOf("_geoloc_") != -1 ? true : false;
		boolean mapLocAttribute = tObjId.indexOf("_loc_") != -1 ? true : false;
		
		String templateId = bookProfile.getTemplateId();
		TemplateProfile tProfile = propsMgr.getTemplateProfile(templateId);
		
		// only one profile can be valid; two of the profile objects will be null...
		CharacterProfile tCharProfile = mapCharAttribute ? tProfile.getCharacter(tObjId) : null;
		GeolocaleProfile tGeolocProfile = mapGeolocAttribute ? tProfile.getGeoloc(tObjId) : null;
		LocationProfile tLocProfile = mapLocAttribute ? tProfile.getLoc(tObjId) : null;
		
		Object iBookObjProfile = null;

		String tObjName = tCharProfile != null ? 
				tCharProfile.getName() : 
					tLocProfile != null ? 
							tLocProfile.getName() : 
								tGeolocProfile.getName();
		
		Vector<String> attributes = mapCharAttribute ? tCharProfile.getAttributes() : mapGeolocAttribute ? tGeolocProfile.getDescriptions() : tLocProfile.getLocationDescriptions();
		
		Vector<String> standinAttributes = null;
		Vector<String[]> subMatrix = mapCharAttribute ? bookProfile.getCharSubMatrix() : mapGeolocAttribute ? bookProfile.getGeolocSubMatrix() : bookProfile.getLocSubMatrix();

		String[] subEntry = null;
		String iBookObjId = "";
		String iBookObjName = "";
		String iBookObjNumAttributes = "";
		Iterator<String[]> matrixI = subMatrix.iterator();
		while(matrixI.hasNext())
		{
			subEntry = matrixI.next();
			if(subEntry[0].compareTo(tObjId) == 0)
			{
				iBookObjId = subEntry[1].length() > 0 ? subEntry[1] : "";
				if(mapCharAttribute)
				{
					iBookObjProfile = propsMgr.getProfile(PropsManager.ObjType.CHARACTER, iBookObjId);
					iBookObjName = ((CharacterProfile)iBookObjProfile).getName();
					standinAttributes = ((CharacterProfile)iBookObjProfile).getAttributes();
				}
				else if(mapGeolocAttribute)
				{
					iBookObjProfile = propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, iBookObjId);
					iBookObjName = ((GeolocaleProfile)iBookObjProfile).getName();
					standinAttributes = ((GeolocaleProfile)iBookObjProfile).getDescriptions();
				}
				else
				{
					iBookObjProfile = propsMgr.getProfile(PropsManager.ObjType.LOCATION, iBookObjId);
					iBookObjName = ((LocationProfile)iBookObjProfile).getName();
					standinAttributes = ((LocationProfile)iBookObjProfile).getLocationDescriptions();
				}
				iBookObjNumAttributes = new Integer(standinAttributes.size()).toString();
				break;
			}
		}
		String attribute = "";
		int numAttributes = standinAttributes.size();
		%>
		
		<p class="section_head">
			<i><%= tObjName %></i> <%= numAttributes > 1 ? "Attributes" : "Attribute" %>
		</p>
		<%
		if(iBookObjName.length() > 0)
		{
			String attributeQty = "";
			if(standinAttributes.size() == 0)
				attributeQty = "attributes";
			else
				attributeQty = standinAttributes.size() > 1 ? "attributes" : "attribute";
		%>
			<p>
 			<%= iBookObjName %>, the <i>eBook</i> profile assigned as 
 			<%= tObjName %>'s standin, has <%= iBookObjNumAttributes %> <%= attributeQty %> defined.
 			</p> 
 			<%
 			if(standinAttributes.size() > 0)
 			{
 			%>
 				<p>
	 			To use this text, select from the menu, or enter new text to be used in place 
	 			of <%= tObjName %>'s attribute, and click <b>Set</b>.
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
 			}  // end if(standinAttributes.size > 0)
		} // end if(iBookObjName.length() > 0)
		else
		{
		%>
			<P>
			No <i>eBook</i> profile has been assigned as <%= tObjName %>'s standin. 
			</p>
			<p>
			Enter text to be used in place of each attribute and click <b>Set</b>b>.
			</P>
		<%
		} // end else
		%> 
		<p>
			<table>
				<tr>
					<th>Current Attribute</th><th>Substitute Text</th><th style="width: 180">Options</th>
				</tr>
				<tr>
					<td/>
					<td/>
					<td style="vertical-align: middle"><span class="inline_text_note">(Select from list or enter new text. To use the current value, select it from menu and click <b>Set</b></b>)</span></td>
					<td style="width: 50px"><span class="inline_text_note" style="color: red">Button pairs valid for single character only. Click for each change.</span></span>
				</tr>	 
			 <%
				 Iterator<String> attributesI = attributes.iterator();
			 	 String substituteText = "";
				 while(attributesI.hasNext())
				 {
					 attribute = attributesI.next();
					 substituteText = bookProfile.getAttributeSubText(tObjId, attribute);
			 %>
					<uL>
						<form name="switchAttributeTextForm2" action="../ManageBook" method="post">
							<tr>
								<td>
									<%= attribute %>
								</td>
								<td>
								<%
								if(substituteText.length() > 0)
								{
								%>
									<%=  bookProfile.getAttributeSubText(tObjId, attribute)%>
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
											String standinAttribute = "";
											Iterator<String> standinAttributesI = standinAttributes.iterator();
											int attributeCount = 0;
											String attributeCountStr = "";
											%>
											<option value="<%= attribute %>"><%= attribute %></option>
											<%
											while(standinAttributesI.hasNext())
											{
												
												attributeCountStr = new Integer(attributeCount++).toString();
												standinAttribute = standinAttributesI.next();
												%>
												<option value="<%= standinAttribute %>"><%= standinAttribute %></option>
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
									<input type="submit" name="setAttributeTextSubmit" id="smallSubmitButton" value="Set" />
									<input type="submit" name="clearAttributeTextSubmit" id="smallSubmitButton" value="Clear" />
									<input type="hidden" name="bookId" value="<%= bookId %>" />
									<input type="hidden" name="tObjId" value="<%= tObjId %>" />
									<input type="hidden" name="tObjAttribute" value="<%= attribute %>" />
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

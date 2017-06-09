<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String bookId = request.getParameter("bookId");
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
   		String templateId = bookProfile.getTemplateId();
		TemplateProfile templateProfile = (TemplateProfile)propsMgr.getProfile(PropsManager.ObjType.TEMPLATE, templateId);
		String projectName = bookProfile.getTitle();
		Vector<CharacterProfile> tChars = templateProfile.getCharacters();
		Vector<String> charAtts = null;
		Iterator<CharacterProfile> tCharsI = tChars.iterator();
		CharacterProfile tChar = null;
		String currentSubId = "";
		String currentSubName = "";
		int iBookCharCount = 0;
		boolean noCharWarningShown = false;
		%>
		<p class="section_head">
			Edit Character Assignments for <i><%= propsMgr.getIBookSystemName("short") %></i> project <i><%= projectName %></i>.
		</p>
		<p>
		
		<table>
			<tr><th colspan="2">Source Text Character</th><th width="30"></th><th colspan="2">eBook Substitute</th></tr>
			<tr>
				<td colspan="2">
				<span class="inline_text_note">Sometimes characters are referred to by nicknames, or aliases, 
				that aren't one of their formally defined names. You need to tell the 
				<i><%= propsMgr.getIBookSystemName("short") %></i> editor how to map these aliases to your character. If the 
				original source text uses an alias for a character  (there can be more than one alias for a given character), there will be a 
				<b>Map Aliases</b> button adjacent to the character's name.</span></td>
				<td></td>
				<td><span class="inline_text_note">The <b>Map Aliases</b> button opens a page where you tell the editor what text to use in place of the character's aliases. 
				If you have defined one or more aliases in your character profile, you can select from them. Otherwise,
				you need to enter the substitute alias manually.</td>
				<td><span class="inline_text_note" style="color: red">Button pairs valid for single character only. Click for each change.</span></span>
				</td>
			<tr>
			<%
			Vector<String> characterList = propsMgr.getIdList(PropsManager.ObjType.CHARACTER);
			iBookCharCount = characterList.size();

			while(tCharsI.hasNext())
			{
				currentSubName = "";
				tChar = (CharacterProfile)tCharsI.next();
				Vector<String> tCharAliases = tChar.getAliases();
				Vector<String> tCharAttributes = tChar.getAttributes();
				
				boolean hasAliases = tCharAliases.size() > 0 ? true : false;
				boolean hasAttributes = tCharAttributes.size() > 0 ? true : false;

				charAtts = tChar.getAttributes();
				
				currentSubId = bookProfile.getCurrentSub(PropsManager.ObjType.CHARACTER, tChar.getId());
				if(currentSubId.length() > 0)
					currentSubName = ((CharacterProfile)propsMgr.getProfile(PropsManager.ObjType.CHARACTER, currentSubId)).getName();
				%>
					<tr valign="top">
					<td><%=  tChar.getName() %>
					<input type="button" id="smallHelpButton" value="?" onclick="displayNamesForChar('<%= tChar.getId() %>', '<%= templateId %>');" />
					</td>
					<td>
					<form name="characterMappingForm" action="../ManageBook" method="post" >
				<%
				if(hasAliases)
				{
				%>
					<input type="submit" name="mapAliasesSubmit"id="mapAliasesSubmit" value="Map Aliases" onclick="return validateMapButton('<%= tChar.getId() %>', '<%= currentSubId %>')"/>
				<%
				}
				if(hasAttributes)
				{
				%>
					<input type="submit" name="mapAttributesSubmit"id="mapAttributesSubmit" value="Map Attributes" onclick="return validateMapButton('<%= tChar.getId() %>', '<%= currentSubId %>')"/>
				<%
				}
				%>
					<input type="hidden" name="bookId" value="<%= bookId %>" />
					<input type="hidden" name="tObjId" id="tObjId" value="<%= tChar.getId() %>" />
					<input type="hidden" name="iBookObjId" id="iBookObjId" value="<%= currentSubId %>" />
					<input type="hidden" name="userId" value="<%= userId %>" />
					</td>
					</form> <!-- mapAliasesForm -->
				</td>
				<td></td>	
				<form name="setSubstitutionForm" action="../ManageProfile" method="post">
				<td>				
				<%
				if(iBookCharCount > 0)
				{
					%>
							<select name="iBookCharId" id="iBookCharId" style="width: 150" onchange="iBookCharId.options[selectedIndex].value">
							
							<% 
							String character_code = "";
							String character_name = "";
							Iterator<String> iCharacters = characterList.iterator();
							%>
							<option value = <%= currentSubId %>>
							<%= currentSubName %>
							</option>
							<%
							while(iCharacters.hasNext())
							{
								character_code = (String)iCharacters.next();
								character_name = propsMgr.getName(PropsManager.ObjType.CHARACTER, character_code);		
							%>
										<option value="<%= character_code %>">
											<%= character_name %>
										</option>
							<% 
							} // end of while{}
							%>
							</select>
						</td>
						<td width="70">
						<input type="submit" name="setCharSubSubmit" id="smallSubmitButton" value="Set" />
						<input type="submit" name="clearCharSubSubmit" id="smallSubmitButton" value="Clear" />
						<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.BOOK %>" />
						<input type="hidden" name="bookId" value="<%= bookId %>" />
						<input type="hidden" name="tCharId" value="<%= tChar.getId() %>" />
						<input type="hidden" name="userId" value="<%= userId %>" />
						<%
				} // end of if(iBookChar > 0
				else
				{
					if(!noCharWarningShown)
					{
					%>
						<span class="inline_text_note" style="color: red">(There are no <i><%= propsMgr.getIBookSystemName("short")%></i> characters available for substitutions. Use the <a href="editCharacters.jsp">Character Editor</a> to create one.)</span>
					<%
						noCharWarningShown = true;
					} // end of "if no warning shown"
				} // end of else
				%>
					</td>
					</form> <!-- setSubstitutionForm -->
				</tr>
				<tr>
					<td colspan="3" valign="top">
						<div class="inline_text_note"><%= tChar.getContext() %></div>
					</td>
				</tr>
			<%
			} // end while tCharsI.hasNext()
			%>
		</table>
		</p>
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

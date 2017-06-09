<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,com.iBook.datastore.*,com.iBook.utilities.*,com.iBook.datastore.manuscripts.*,com.iBook.datastore.characters.*,com.iBook.datastore.locations.*" %>
		<%
		String templateId = request.getParameter("templateId");
		String textblockId = request.getParameter("textblockId");
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(request.getRemoteUser());
		TemplateProfile tProfile = propsMgr.getTemplateProfile(templateId);
		Vector<String> textblocks = tProfile.getTextblocks();
		int numTextblocks = textblocks.size();
		String filename = tProfile.getFilename();
		// if no textblock specified, get the next block after the block edited previously
		if(textblockId == null || textblockId.compareTo("null") == 0 || textblockId.length() == 0)
		{
			// if the most recently edited block was the last block, getNextTextblockId() will return the first textblock;
			textblockId = tProfile.getNextTextblockId(tProfile.getLastEditedTextblock());
			// if no blocks have been edited, start at the beginning
			if(textblockId.length() == 0 || textblockId.endsWith("_0"))
				textblockId = filename.substring(0, filename.indexOf(".")) + "_template_textblock_1";
		}
		String textblock = "";
		Vector<CharacterProfile> tChars = tProfile.getCharacters();
		Vector<LocationProfile> tLocs = tProfile.getLocations();
		Vector<GeolocaleProfile> tGeolocs = tProfile.getGeolocs();
		CharacterProfile tChar = null;
		LocationProfile tLoc = null;
		GeolocaleProfile tGeoloc = null;
		
		int idx = 0;
		
		String tCharId = "";
		String tCharName = "";
		String tLocId = "";
		String tLocName = "";
		String tGeolocId = "";
		String tGeolocName = "";
		
		Iterator<CharacterProfile> tCharsI = tChars.iterator();
		Iterator<LocationProfile> tLocsI = tLocs.iterator();
		Iterator<GeolocaleProfile> tGeolocsI = tGeolocs.iterator();
		%>
	</head>
	<body>
		<div class="page_title">
			Interactive Book Designer
		</div>
		<p class="section_head">
			Enable Substitutions in the Template <i><%= tProfile.getTitle() %></i> 	
		</p>

		<%
		// highlight all tags for display text
		textblock = tProfile.getTextblock(textblockId);
		
		int iTextblockNum = tProfile.getTextblockNum(textblockId);
		String sTextblockNum = new Integer(iTextblockNum).toString();
		String sNextTextblockNum = new Integer(iTextblockNum + 1).toString();
		String nextTextblockId = textblockId.substring(0, textblockId.lastIndexOf("_") + 1) + sNextTextblockNum;
		%>
		
		<form name="enableCharSubForm" action="../UpdateTextblock" method="post" >
		<table>
			<tr>
				<th>Text Block</th><th>Characters</th><th>Places</th>
			</tr>
			<tr>
				<td><textarea name="textblock" id="textblock" cols="38" rows="23"><%= textblock.toString() %></textarea><br/>
				<span class="inline_text_note">Number <%= sTextblockNum %> of <%= new Integer(numTextblocks).toString() %></span><br/>
				Update and 
				<input type="submit" name="updateTextblock" value="Stay" />
				<input type="submit" name="updateAndAdvanceTextblock" value="Advance"/>
				<input type="submit" name="updateAndGoBackOne" value="Go Back One" />
				<br/>
				<input type="text" name="blockNum" id="blockNum" size="2"/> 
				<input type="button" name="updateHelp" value="?" onclick="displayHelp('noUpdate.jsp')" /><br/>
				<input type="submit" style="background-color: lightgreen" name="updateAndGoToBlockNum" id="updateAndGoToBlockNum" value="Update and go to Specified Block" onclick="return numInRange(<%= textblocks.size() %>, blockNum.value)" />
				<input type="submit" style="background-color: yellow" name="goToBlockNum" id="goToBlockNum" value="Go to Specified Block (no update)" onclick="return numInRange(<%= textblocks.size() %>, blockNum.value)" />
				<br/><hr/>
				<input type="button" name="showTaggedText" value="Show Tag Text" onclick="displayTaggedText('<%= textblockId %>', '<%= templateId %>');" />
				<input type="button" name="showDisplayText" value="Show Display Text" onclick="displayDisplayText('<%= textblockId %>', '<%= templateId %>');" />
				<input type="button" value="?" onclick="displayHelp('displayText.jsp')" />
				<input type="hidden" name="templateId" id="templateId" value=<%= templateId %> />
				<input type="hidden" name="textblockId" value=<%= textblockId %> /> 
				<input type="hidden" name="userId" value=<%= userId %> />
				</td>
				<td valign="top" style="width:50%">
				<%
				if(tChars.size() > 0)
				{
				%>
					Enable substitution of: <br/>
					<select name="tCharSelector" id= "tCharSelector" onchange="tCharSelector.options[selectedIndex].value">
					<%
					while(tCharsI.hasNext())
					{
						tChar = (CharacterProfile)tCharsI.next();
						tCharId = tChar.getId();
						tCharName = tChar.getName();
						%>
						<option value=<%= tCharId %>><%= tCharName %></option>
						<%
					}
					%>
					</select>'s 
					<input type="button" onclick="insertMetachars('&lt;character name&gt;');" value="Full (first/last) Name" />
					<input type="button" onclick="insertMetachars('&lt;character shortName&gt;');" value="Short (familiar) Name" />
					<input type="button" onclick="insertMetachars('&lt;character formalName&gt;')" value="Formal Name" />
					<input type="button" onclick="insertMetachars('&lt;character namePrefix&gt;');" value="Name Prefix"/>
					<input type="button" onclick="insertMetachars('&lt;character nameFirst&gt;');" value="First Name"/>
					<input type="button" onclick="insertMetachars('&lt;character nameMiddle&gt;');" value="Middle Name"/>
					<input type="button" onclick="insertMetachars('&lt;character nameLast&gt;');" value="Last Name"/>
					<input type="button" onclick="insertMetachars('&lt;character nameSuffix&gt;');" value="Name Suffix"/>
					<input type="button" onclick="insertMetachars('&lt;character descriptor&gt;','&lt;/descriptor&gt;');" value="Description"/>
					<input type="button" onclick="insertMetachars('&lt;character gdescriptor&gt;', '&lt;/gdescriptor&gt');" value="Gender Reference"/>
					<input type="button" onclick="insertMetachars('&lt;character alias&gt;','&lt;/alias&gt;');" value="Alias"/>
					<input type="button" onclick="insertMetachars('&lt;character attribute&gt;','&lt;/attribute&gt;');" value="Attribute"/>
					<input type="button" onclick="displayHelp('characterSubstitutionOptions.jsp')" value="?" /><br/>
					...or pronoun appropriate for substitute's gender: 
					<input type="button" onclick="insertMetachars('&lt;getHeShe(character)&gt;');" value="He/She"/>
					<input type="button" onclick="insertMetachars('&lt;getHimHer(character)&gt;');" value="Him/Her"/>
					<input type="button" onclick="insertMetachars('&lt;getHisHer(character)&gt;');" value="His/Her"/>
					<input type="button" onclick="insertMetachars('&lt;getHisHers(character)&gt;');" value="His/Hers"/>
					<input type="button" onclick="insertMetachars('&lt;getHimselfHerself(character)&gt;');" value="Himself/Herself" />
					<br/><hr/>
					<input type="button" onclick="displayNames();" value="Show Names/Aliases" />
				<%
				}
				else
				{
				%>
				No characters have been <a href="createCharacter.jsp?templateId=<%= templateId %>">defined</a> for the template.
				<%
				}
				%>
				</td>
				<td valign="top" style="width:50%">
					<%
					if(tLocs.size() > 0 || tGeolocs.size() > 0)
					{
						if(tLocs.size() > 0)
						{
					%>
							Enable substitution of the location: <br/>
							<select name="tLocSelector" id= "tLocSelector" onchange="tLocSelector.options[selectedIndex].value">
							<%
							while(tLocsI.hasNext())
							{
								tLoc = (LocationProfile)tLocsI.next();
								tLocId = tLoc.getId();
								tLocName = tLoc.getName();
								%>
								<option value=<%= tLocId %>><%= tLocName %></option><%
							}
							%></select>'s <br/>
							<input type="button" onclick="insertMetachars('&lt;location name&gt;');" value="Name"/>
							<input type="button" onclick="insertMetachars('&lt;location attribute&gt;','&lt;/attribute&gt;');" value="Attribute"/>
							<input type="button" onclick="insertMetachars('&lt;location descriptor&gt;','&lt;/descriptor&gt;');" value="Description"/>
							<input type="button" onclick="insertMetachars('&lt;location alias&gt;','&lt;/alias&gt;');" value="Alias"/>
							<br/><br/>
							<%
						}
						if(tGeolocs.size() > 0)
						{
							%>
							Enable substitution of the region: <br/>
							<select name="tGeolocSelector" id= "tGeolocSelector" onchange="tGeolocSelector.options[selectedIndex].value">
							<%
							while(tGeolocsI.hasNext())
							{
								tGeoloc = (GeolocaleProfile)tGeolocsI.next();
								tGeolocId = tGeoloc.getId();
								tGeolocName = tGeoloc.getName();
								%>
								<option value=<%= tGeolocId %>><%= tGeolocName %></option><%
							}
							%>
							</select>'s 
							<input type="button" onclick="insertMetachars('&lt;region name&gt;');" value="Name"/>
							<input type="button" onclick="insertMetachars('&lt;region attribute&gt;','&lt;/attribute&gt;');" value="Attribute"/>
							<input type="button" onclick="insertMetachars('&lt;region descriptor&gt;','&lt;/descriptor&gt;');" value="Description"/>
							<input type="button" onclick="insertMetachars('&lt;region alias&gt;','&lt;/alias&gt;');" value="Alias"/>
							<input type="button" onclick="displayHelp('placesSubstitutionOptions.jsp')" value="?" /><br/>
							<hr/>
							<input type="button" onclick="displayAttribute('<%= templateId %>');" value="Show Attributes/Aliases" />
							
							<%
						}
					}
					else
					{
					%>
					No <a href="createLocation.jsp?templateId=<%= templateId %>">locations</a> or <a href="createRegion.jsp?templateId=<%= templateId %>">regions</a> have been defined for the template.
					<%
					}
					%>
				</td>
			</tr>
		</table>
		</form>
		
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to </div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"> </div>
				<div class="send_back_target"><a href="createCharacter.jsp?templateId=<%=templateId %>" onclick="return confirmJumpToProfileEditor('character')" >Create Character</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="createLocation.jsp?templateId=<%=templateId %>" onclick="return confirmJumpToProfileEditor('location')" >Create Location</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="createRegion.jsp?templateId=<%=templateId %>" onclick="return confirmJumpToProfileEditor('region')" >Create Region</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="editTemplate.jsp?templateId=<%= templateId %>">Edit Template</a></div>
			</div>
		</div>
	</body>
</html>


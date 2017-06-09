<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*,com.iBook.datastore.books.*,com.iBook.datastore.manuscripts.*,com.iBook.datastore.characters.*,com.iBook.datastore.locations.*" %>
<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String manuscriptId = request.getParameter("manuscriptId");
		String templateId = request.getParameter("templateId");
		TemplateProfile template = (TemplateProfile)propsMgr.getTemplateProfile(templateId);
		Manuscript manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
		String bookId = manuscript.getBookId();
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		
		Vector<String[]> descriptors = manuscript.getDescriptors();
		Vector<CharacterProfile> tChars = template.getCharacters();
		Vector<GeolocaleProfile> tGeolocs = template.getGeolocs();
		Vector<LocationProfile> tLocs = template.getLocations();
		
		CharacterProfile tChar = null;
		GeolocaleProfile tGeoloc = null;
		LocationProfile tLoc = null;
		
		boolean isChar = false;
		boolean isGeoloc = false;
		boolean isLoc = false;

		String[] descriptor = null;
		String numDescriptors = new Integer(manuscript.getNumDescriptorsUnresolved()).toString();
		String context = "";
		String contextCitation = "";
		// textblockId is name portion only; idx num needs to be added just before use
		String textblockId = manuscript.getMsName() + "_";
		String objId = "";
		String objName = "";
		String subName = "";
		String standinId = "";
		String textblock = "";
		String ctxtLeadin = "...";
		String ctxtEnd = "...";
		String textblockDisplayText = "";
		String descriptorDisplayText = "";
		String startDescriptorTag = "descriptor>";
		String closeDescriptorTag = "</descriptor>";
		String startGdescriptorTag = "gdescriptor>";
		String closeGdescriptorTag = "</gdescriptor>";
		String url = "";
		
		int idx = 0;
		int end = 0;
		
		int tagCheckIdx = 0;

		Iterator<String[]> descriptorsI = descriptors.iterator();
		
		// if there are no unresolved descriptors when resolveDescriptors.jsp is invoked, redirect to final proof (see <body> tag)
		String redirect_target = "finalProof.jsp?manuscriptId=" + manuscriptId + "&textblockId=" + textblockId + "1";
%>
	<style>
		#smallButton
		{
			height: 2em;
			width: 8em;
			font-size: .6em;
			padding-left: .2em;
			padding-top: 0px;
			text-align: top;
		}
	</style>
	</head>
	<%
	if(manuscript.getNumDescriptorsUnresolved() == 0)
	{
	%>
	<body onload="document.location='<%= redirect_target %>'" >
	<%
	}
	else
	{
	%>
	<body>
	<%
	}
	%>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Descriptor Resolution Editor
		</p>
		<p>
		The source text <i><%= manuscript.getSourceTitle() %></i> contains <%= numDescriptors %> instances of 
		unresolved descriptor text. 
		</p>
		<p>
		Descriptor text is a word or phrase used in reference to a character or place that can't be readily 
		replaced without user intervention. You need to tell <i>eBook</i> how to deal with each instance of 
		descriptor text, by either clicking the <i>Use Source</i> button, or by entering new text in the 
		edit panel and clicking the <i>Set</i> button. 
		</p>
		<p class="inline_text_note">
		Once a descriptor has been set (whether changed or unchanged) it will be removed from this table. To make additional 
		edits, you will need to search for specific text during the final-proofing stage at the end of the descriptor resolution process.	
		The process will not advance beyond this <i>descriptors</i> page until all descriptor instances have been resolved.
		</p>
		
		<table>
			<tr>
				<th>Person/Place</th><th>Descriptor Text</th><th>Context</th>
			</tr>
		<%
		for(int count = 0; count < descriptors.size(); count++)
		{
			isChar = false;
			isGeoloc = false;
			isLoc = false;
			
			// descriptor = { tObjId, textblockId_num, "text contained in source", "substitute text" }'
			descriptor = descriptorsI.next();
			if(descriptor[3].length() == 0)
			{
				ctxtLeadin = "...";
				ctxtEnd = "...";
	
				if(descriptor[0].indexOf("_template_char_") != -1)
				{
					isChar = true;
		 			Iterator<CharacterProfile> tCharsI = tChars.iterator();
					while(tCharsI.hasNext())
					{
						tChar = tCharsI.next();
						if(tChar.getId().compareTo(descriptor[0]) == 0)
							break;
					}
					objName = tChar.getName();
					objId = tChar.getId();
					standinId = ((BookProfile)propsMgr.getBookProfile(bookId)).getStandinId(tChar.getId());
					subName = ((CharacterProfile)propsMgr.getProfile(PropsManager.ObjType.CHARACTER, standinId)).getName();
				}
				else if(descriptor[0].indexOf("_template_geoloc_") != -1)
				{
					isGeoloc = true;
		 			Iterator<GeolocaleProfile> tGeolocsI = tGeolocs.iterator();
					while(tGeolocsI.hasNext())
					{
						tGeoloc = tGeolocsI.next();
						if(tGeoloc.getId().compareTo(descriptor[0]) == 0)
							break;
					}
					objName = tGeoloc.getName();
					objId = tGeoloc.getId();
					standinId = ((BookProfile)propsMgr.getBookProfile(bookId)).getStandinId(tGeoloc.getId());
					subName = ((GeolocaleProfile)propsMgr.getProfile(PropsManager.ObjType.GEOLOCALE, standinId)).getName();
				}
				else if(descriptor[0].indexOf("_template_loc_") != -1)
				{
					isLoc = true;
		 			Iterator<LocationProfile> tLocsI = tLocs.iterator();
					while(tLocsI.hasNext())
					{
						tLoc = tLocsI.next();
						if(tLoc.getId().compareTo(descriptor[0]) == 0)
							break;
					}
					objName = tLoc.getName();
					objId = tLoc.getId();
					standinId = ((BookProfile)propsMgr.getBookProfile(bookId)).getStandinId(tLoc.getId());
					subName = ((LocationProfile)propsMgr.getProfile(PropsManager.ObjType.LOCATION, standinId)).getName();
				}
				else
					continue;
	
				// textblockId was defined earlier as "textblockId_" with the expectation 
				// that the number portion would be added when necessary. Now it's necessary... 
				textblock = manuscript.getTextblock(textblockId + descriptor[1]);
				idx = textblock.indexOf(descriptor[2]);
				end = idx + descriptor[2].length();
				for(int spaceCount = 0; spaceCount < 10; spaceCount++)
				{
					end = textblock.indexOf(" ", end + 1);
					if(end == -1)
					{
						ctxtEnd = "";
						end = textblock.length();
					}
					idx = textblock.lastIndexOf(" ", idx - 1);
					if(idx < 0)
					{
						ctxtLeadin = "";
						idx = 0;
					}
				}
				
				context = textblock.substring(idx, end);
				// context string can't start...
				if((tagCheckIdx = context.indexOf(">")) != -1 && context.lastIndexOf("<", tagCheckIdx) == -1)
				{
					context = context.substring(tagCheckIdx + 1);
				}
				// ...or end in the middle of a tag (between < and >)
				if((tagCheckIdx = context.lastIndexOf("<")) != -1 && context.indexOf(">", tagCheckIdx) == -1)
				{
					context = context.substring(0, tagCheckIdx).trim();
				}
				
				context = "..." + context + "...";
				contextCitation = "(Textblock number " + descriptor[1]  + ")";
				
				textblockDisplayText = textblock.substring(1, textblock.length() - 1);
				// gdescriptor?
				if((idx = descriptor[2].indexOf(startGdescriptorTag)) != -1)
				{
					idx += startGdescriptorTag.length();
					end = descriptor[2].indexOf(closeGdescriptorTag);
				}
				else // no? must be descriptor...
				{
					idx = descriptor[2].indexOf(startDescriptorTag) + startDescriptorTag.length();
					end = descriptor[2].indexOf(closeDescriptorTag);
				}
				
				descriptorDisplayText = descriptor[2].substring(idx, end);
			%>
				<form name="changeDescriptorTextForm" action="../ManageDescriptors" method="post">
					<tr>
						<td width="25" valign="top"> 
						    <span class="inline_text_note">Reference to: <%= subName %><br/>
						    (for <%= objName %>)</span><br/>
						    <%
						    if(isChar)
						    {
						    %>
						    	<input type="button" id="smallButton" value="Show Names" onclick="mapNames('<%= templateId %>', '<%= tChar.getId() %>', '<%= standinId %>')" />
						    <%
						    }
						    else if(isGeoloc)
						    {
						    }
						    else if(isLoc)
						    {
						    }
						    %>
						</td>
						<td width="50" valign="top"> <textarea name="descriptorTextEdit" cols="30"><%= descriptorDisplayText %> </textarea><br/>
							<input type="submit" name="setDescriptorText" id="smallButton" value="Set Change" />
							<input type="submit" name="useOriginalDescriptorText" id="smallButton" value="Use Source" />
							<input type="button" name="help" id="smallButton" style="width: 20px" value="?" onclick="displayHelp('resolveDescriptors.jsp')" />
							<input type="hidden" name="manuscriptId" value="<%= manuscriptId %>" />
							<input type="hidden" name="textblockNum" value="<%=  descriptor[1] %>" />
							<input type="hidden" name="descriptorText" value="<%= Utilities.encodeHtmlTags(descriptor[2]) %>" />
							<input type="hidden" name="templateId" value="<%= templateId %>" />
							<input type="hidden" name="userId" value="<%= userId %>" />
							<input type="hidden" name="standinId" id="standinId" value="<%= standinId %>" />
						</td>
						<td width="250" valign="top"> 
							<%= context %> <br/>
							<div class="inline_text_note" style="text-align: right; margin-top: 10px">
							<%= contextCitation %>
							<input style="height: 2.2em;font-size: .8em;" type="button" name="showFullText" value="Show Full Text" onclick="displayTextblock('<%= new Integer(count).toString() %>', '<%= manuscriptId %>');" />
							</div>
						</td>
					</tr>
				</form>
			<% 
		} // if(descrptor[3].length > 0)
	} // for(...count < numDescriptors...)
	%>
	</table>
	
	<div class="send_back">
		<div class="send_back_pair">
			<div class="send_back_label">Go to</div>
			<div class="send_back_target"><a href="editBook.jsp?bookId=<%= bookId %>">Book Editor</a></div>
		</div>
		<div class="send_back_pair">
			<div class="send_back_label">Return to</div>
			<div class="send_back_target"><a href="index.jsp">Start</a></div>
		</div>
	</div>
	</body>
</html>
		
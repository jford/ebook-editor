<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*,com.iBook.datastore.books.*,com.iBook.datastore.manuscripts.*" %>
		
<%
		String outputFile = request.getParameter("outputFile");
		String bookId = request.getParameter("bookId");
		String useCurrentMappings = request.getParameter("useCurrentMappings");
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		boolean outputFileReady = outputFile != null && outputFile.length() > 0;
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		Manuscript mscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, bookProfile.getManuscriptId());
		int numBooks = propsMgr.getBookCount();
		String qty = numBooks == 1 ? "" : "s";
		String bookName = "";
		
%>
	<style>
		#deleteMS
		{
			height: 3em;
			width: 10em;
			font-size: .6em;
			padding: .3em;
			padding-top: .1em;
		}
	</style>
	</head>
	<%
	if(outputFileReady)
	{
	%>
		<body onload="document.location='/iBook/resources/users/<%= userId %>/resources/<%= outputFile %>'">
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
			Build eBook
		</p>
		<%
			String manuscriptId = bookProfile.getManuscriptId();
			String manuscriptName = ((Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId)).getMsName();
			int numTextBlocks = ((Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId)).getNumTextblocks();
			boolean needFinalProofButton = ((Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId)).getNumDescriptorsUnresolved() == 0 ? true : false;
		%>
			<hr/>
			<form name="buildEpubForm" action="../BuildEbook" method="post">
				Click
				<input type="submit" name="beginEbook" style="background: lightgreen" value="Continue" <% if(mscript.getNumTextblocks() > 0){%> onclick="return displayPublishAlert()" <%}%>/> 
				<input type="button" value="?" onclick="displayHelp('generatedFiles.jsp')" />
				to build the following eBook in 
				<select name="outputFormat" id="outputFormat" style="background: cyan" onChange="outputFormat.options[selectedIndex].value">
					<option name="epub" selected>.epub</option>
					<option name="pdf">.pdf</option>
				</select> format 
				<%
				if(numTextBlocks > 0 && needFinalProofButton)
				{
					// numTextBlocks == 0 until Continue button has been clicked at least once
					// needFinalProofButton == true only if MS has descriptors that need to be resolved 
				%> 
					 or <input type="button" style="background: yellow" value="Final Proof" onclick="document.location='finalProof.jsp?manuscriptId=<%= manuscriptId %>&textblockId=<%= manuscriptName + "_1"%>'">
					to revisit the <i>Final Proof Editor</i>
				<%
				}
				%>: 
				<div class="title_page_info" style="margin-left: 10%;">
					<p class="section_head">
					<i><%= bookProfile.getTitle() %></i>
					</p>
					<p>
					A retelling of <%= bookProfile.getSourceAuthorName() %>'s <i><%= bookProfile.getSourceTitle() %></i>
					</p>
					<p>
					by <%= bookProfile.getAuthorName() %>
					</p>
					<%
					String coverArtFilename = propsMgr.getCoverArtFilename(manuscriptId);
					if(coverArtFilename.length() > 0)
					{
						String coverArtPath = Utilities.replaceChars(propsMgr.getPathToUserDir() + "manuscripts" + propsMgr.getPathMarker() + coverArtFilename, "\\", "/", "all");
						coverArtPath = coverArtPath.substring(coverArtPath.indexOf("/iBook"));
					%>
						Current cover image: <img src="<%= coverArtPath %>" 
						                          alt="Cover art" 
						                          height="120" 
						                          width="80" 
						                          style="vertical-align: middle">
					<%
					}
					%>
				</div> 
				<input type="hidden" name="userId" value="<%= userId %>" />
				<input type="hidden" name="useCurrentMappings" value="<%= useCurrentMappings %>" />
				<input type="hidden" name="bookId" value="<%= bookId %>" />
				<%
				if(bookProfile.getManuscriptId().length() > 0)
				{
				%>
				<hr/>
				<span class="inline_text_note">
					(Click <input type="submit" name="redoDescriptors" id="deleteMS" value="Reset Manuscript" onclick="return confirmMsReset();"/>to revisit the <i>Descriptor 
					Resolution</i> editors&mdash;all descriptors will have to be resolved again, and all revisions of the original source text will be lost.)</span>
					<input type="button" name="help" value="?" onclick="displayHelp('resolveDescriptors.jsp')" />
				<%
				}
				%>
			</form>
			<hr/>
			<form name="uploadCoverImage" action="../UploadCoverImage" method="post" enctype="multipart/form-data">
				<table>	
				<tr>
					<td width="200">Upload cover image: </td>
				    <td><input type="file" name="coverImage" size="50" /> 
				        <input type="submit" name="uploadCoverImage" id="uploadCoverSubmit" value="Upload" onclick="return validEntry(coverImage.value, 'cover image filename/location')"/> 
				        <input type="button" value="?" onclick="displayHelp('coverArt.jsp')" />
				 	</td>
				</tr>
				<tr>
				    <td colspan="2" style="padding-top: 8px"><div class="input_text_note">(Uploading a new file will overwrite any previously uploaded image.)</div></td>
				</tr>
				<%
				if(coverArtFilename.length() > 0)
				{
				%>
					<tr>
						<td></td>
					    <td>
					    	<input type="submit" name="deleteCoverImage" value="Delete Cover Art"/>
					    	<input type="button" value="?" onclick="displayHelp('coverArt.jsp')" />
					    </td>
					</tr>
					<tr>
					    <td></td>
					    <td style="padding-top: 8px"><div class="input_text_note">(Until 
					    you explicitly delete the cover image, or replace it by uploading a 
					    new one, the previously uploaded image will be used in the output file.)</div></td>
					</tr>
				<%
				}
				%>
				</table>
				<input type="hidden" name="bookId" value="<%= bookId %>" />
				<input type="hidden" name="manuscriptId"  value="<%= manuscriptId %>" />
				<input type="hidden" name="userId" value="<%= userId %>" %>
	
				<p>
				<%
				if(mscript.getNumTextblocks() > 0 && mscript.getNumDescriptorsUnresolved() == 0)
				{						
				%>
				<input type="button" name="editFilebreaks" value="Edit Filebreaks" onclick="displayFilebreaks('<%= bookProfile.getManuscriptId() %>')" />
				<input type="button" name="help" value="?" onclick="displayHelp('fileBreaks.jsp')" />
				<%
				}
				%>
				</p>
			</form>
		
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
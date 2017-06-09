<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*,com.iBook.datastore.books.*,com.iBook.datastore.manuscripts.*,com.iBook.datastore.characters.*,com.iBook.datastore.locations.*" %>
		
<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String manuscriptId = request.getParameter("manuscriptId");
		String textblockId = request.getParameter("textblockId");
		String textblockNum = textblockId.substring(textblockId.lastIndexOf("_") + 1);
		Manuscript manuscript = (Manuscript)propsMgr.getProfile(PropsManager.ObjType.MANUSCRIPT, manuscriptId);
		String textblock = manuscript.getTextblock(textblockId);
		Vector<String> textblocks = manuscript.getTextblocks();

		// need templateId for show display text window
		String bookId = manuscript.getBookId();
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		String templateId = bookProfile.getTemplateId();
		
		int numTextblocks = manuscript.getNumTextblocks();
		int textblockIdx = 0;
		int idx = 0;
		int end = 0;
%>
	<style>
		#smallButton
		{
			height: 1.5em;
			width: 8em;
			font-size: .6em;
			padding: .3em;
			padding-top: .1em;
		}
		#generateEbook
		{
			background: #00ff00;
		}
	</style>
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Final Proof Editor
		</p>
		<p>
		<form name="generateEbookForm" action="../BuildEbook" method="post">
			If you're happy with the text, click 
			<input type="submit" name="generateEbook" id="generateEbook" value="Continue" <% if(manuscript.getNumTextblocks() > 0){%> onclick="return displayPublishAlert()" <%}%>/> to publish 
			<i><%= manuscript.getIBookTitle() %></i> in 
			<select name="outputFormat" id="outputFormat" style="background: cyan" onChange="outputFormat.options[selectedIndex].value">
				<option name="epub" selected>.epub</option>
				<option name="pdf">.pdf</option>
			</select> format, or review 
			the text and make any last minute changes: 
			<input type="button" name="help" value="?" onclick="displayHelp('finalProofEdits.jsp')" />
			<input type="hidden" name="manuscriptId" value="<%= manuscriptId %>" /> 
			<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		</p>
		<p>
		<form name="editTextBlockForm" action="../UpdateTextblock" method="post">
			<textarea name="textblock" id="textblock" cols="60" rows="20"><%= textblock %></textarea><br/>
			<span class="inline_text_note">Number <%= textblockNum %> of <%= new Integer(numTextblocks).toString() %></span>
			<input type="submit" name="updateTextblock" value="Stay" />
			<input type="submit" name="updateAndAdvanceTextblock" value="Advance" onclick=""/>
			<input type="submit" name="updateAndGoBackOne" value="Go Back One" /><br/><br/>

			<hr/>
			<input type="text" name="blockNum" id="blockNum" size="2"/> 
			<input type="button" name="updateHelp" value="?" onclick="displayHelp('noUpdate.jsp')" /><br/>
			<input type="submit" style="background-color: lightgreen" name="updateAndGoToBlockNum" id="updateAndGoToBlockNum" value="Update and go to Specified Block" onclick="return numInRange(<%= textblocks.size() %>, blockNum.value)" /><br/>
			<input type="submit" style="background-color: yellow" name="goToBlockNum" id="goToBlockNum" value="Go to Specified Block (no update)" onclick="return numInRange(<%= textblocks.size() %>, blockNum.value)" />
			<br/><hr/><br/>

			<textarea name="searchText" id="searchText" cols="30" rows="1"></textarea> 
			<input type="button" name="searchButton" value="Search" onclick="displaySearchResults('<%= Utilities.encodeHtmlTags(manuscriptId) %>');" /> 
		    <input type="button" name="help" value="?" onclick="displayHelp('search.jsp');" /> <br/>
			<span class="inline_text_note">Search the entire manuscript for the specified text.</span>
			<input type="hidden" name="manuscriptId" value=<%= manuscriptId %> />
			<input type="hidden" name="textblockId" value=<%= textblockId %> />
			<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		</p>
		<p>
		<input type="button" name="ediFilebreaksButton" value="Edit File Breaks" onclick="displayFilebreaks('<%= manuscriptId %>')" />
		<input type="button" name="help" value="?" onclick="displayHelp('fileBreaks.jsp');" />

		<%		
		// open show display text window
		%>
		
		<input type="button" name="showTaggedText" value="Show Tag Text" onclick="displayTaggedText('<%= textblockId %>', '<%= templateId %>');" />
		<input type="button" name="showDisplayText" value="Show Display Text" onclick="displayDisplayText('<%= textblockId %>', '<%= templateId %>');" />
		<input type="button" value="?" onclick="displayHelp('displayText.jsp')" />
		</p>


		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="generate.jsp">Build Epub</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
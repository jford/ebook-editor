<html>
	<head>
		<title>Display Text vs. Tagged Text</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		%>
	</head>
	
	<body>
	<p class="section_head">
	Display Text vs. Tagged Text
	</p>
	<p>
	When text is marked up in the <%= propsMgr.getIBookSystemName("short") %>, tags 
	enclosed in &lt; and &gt; brackets are inserted into the text to indicate to the 
	editor where and how text needs to be changed when the final output file is generated.
	</p>
	<p>
	These tags make it difficult to read the text, but it is sometimes necessary to see what 
	tags have been used. 
	</p>
	<p>
	The <b>Show Tag Text</b> button displays the edited text block with the tags in place and 
	highlighted so they stand out.
	</p>
	<p>
	The <b>Show Display Text</b> button shows the edited text block as it would appear in the 
	output file after the tags have been processed.
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	The Display Text window shows the text after processing. If viewed from the template editor, since
	no characters or places have been substituted yet&mdash;that's a task done by the user, outside 
	of the template editor&mdash;names, aliases, and descriptor text should all 
	be unchanged from the source narrative, unless errors have been made while inserting template 
	tags. Beware, though, when looking for such errors while simultaneously tagging text in the 
	template editor&mdash;text is not updated until you click one of the navigation buttons (<b>Stay</b>,
	 <b>Advance</b>, or <b>Go Back One</b>) after inserting the tag.
	 <br/><br/>
	 The edit pane on the <i>Final Proof</i> page shows the text after user substitutions have been made.
	 By this time in the process, most&mdash;but not all&mdash;tags will have been removed. The exceptions 
	 are for descriptive text associated with characters for which there are no substitutions. These tags
	 will still be in place in the text (they are removed at a later part of the <i>.epub</i> generation 
	 process). Use the <b>Show Display Text</b> button to view the text as it will be presented in the 
	 finished book. Use the <b>Show Tags</b> button to view the text as it was before tags where processed.   
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
<html>
	<head>
		<title>Searching Text</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	</head>
	
	<body>
		<p class="section_head">
		Searching Text
		</p>
		<p>
		The <b>Search</b> button in the <i>Final Proof Editor</i> will search the entire manuscript for the
		text entered in the text edit pane.
		</p>
		<p>
		A table of passages that contain the search text shows a textblock number for each hit.
		</p>
		<p>
		You may have to try several variations of search text to locate what you are looking for. The manuscript may still contain 
		some <i><%= PropsManager.getIBookSystemName("short") %></i> markup tags that will prevent the 
		search feature from finding text that you are certain should be in the manuscript.
		</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
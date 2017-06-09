<html>
	<head>
		<title>Templates</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<p class="section_head">
	Templates
	</p>
	<p>
	A template is the <%= PropsManager.getIBookSystemName("short") %> representation of the  
	narrative upon which your finished book will be modeled.
	<p>
	</p>
	The template contains markers which identify the elements you can change&mdash;names of 
	characters or places, for example.
	</p>
	<p>
	When you substitute a character profile for a template character, the <%= PropsManager.getIBookSystemName("short") %>
	will retrieve blocks of text in which references are made to the substituted profile, change the 
	name or description as appropriate, and store the new text in a manuscript file that will be 
	used to generate the final output file.
	</p>
	<p>
	Though the <%= PropsManager.getIBookSystemName("short") %> provides tools you can use to edit these 
	templates (or create your own from source text you provide), under normal circumstances that 
	shouldn't be necessary in order to create a custom version of one of the templates provided by the 
	<%= PropsManager.getIBookSystemName("short") %>.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
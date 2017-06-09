<html>
	<head>
		<title>Revert Templates</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<p class="section_head">
	Revert Templates
	</p>
	<p>
	The <b>Revert</b> button on the <i>Template Editor</i> page will overwrite 
	all user copies of system-defined templates with the current version of the system 
	templates. 
	</p>
	<p>
	Each <%= PropsManager.getIBookSystemName() %> user account has its own copy of these
	system defined templates, which you can edit in the <i>Template Editor</i>.
	</p>
	<p>
	The <b>Revert</b> button overwrites the user-account version of all system-defined 
	templates with the latest version of these system templates. 
	</p> 
	<p>
	By contrast, the <b>Update</b> button does not overwrite any templates. It will only retrieve 
	system-defined templates that do not currently exist in the user-account template folder. This
	is the mechanism by which newly released templates are distributed to the existing user base.  
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
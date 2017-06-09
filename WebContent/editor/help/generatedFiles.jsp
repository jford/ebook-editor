<html>
	<head>
		<title>Generated Files</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<p class="section_head">
	Generated Files
	</p>
	<p>Once your <i>.epub</i> or <i>.pdf</i> file has been generated, you will be given the 
	option of opening or downloading it.
	</p>
	<p>
	If you elect to do neither, the file will be kept on the server for 
	the duration of your current <%= PropsManager.getIBookSystemName("short") %> session,
	during which you can download it from the <i>start</i> page. 
	</p>
	<p>
	The next time you log in to the server, the file will be gone and you will have to regenerate 
	the book in order to retrieve it. 
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	Every time you generate a new <i>.epub</i> file, the book will be given a unique <i>.epub</i> 
	ID and it will be considered a different book. If you have multiple unique files for the same 
	book, your eReader will consider them different books and open them each individually when 
	you select them from a file navigation list.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
<html>
	<head>
		<title>Formatting Source Text</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<p class="section_head">
	Formatting Source Text
	</p>
	<p>
	Text being input as source text must be contained in a single, unformatted file, text only 
	(no images, no stylized text---specifications for fonts abd text size, for example&mdash;with 
	a very few exceptions, outlined below), using the UTF-8 character set. 
	</p>
	<p>
	Remove all line breaks except those that separate paragraphs. Source text will be divided 
	into individual textblocks for handling by the <i>eBook</i> editor; each line of text in 
	the input stream will be turned into a separate textblock. Do not separate lines of text 
	with blank lines. 
	</p>
	<p>
	A limited number of HTML tags are allowed. See <a href="finalProofEdits.jsp">Final 
	Proof Edits</a>.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
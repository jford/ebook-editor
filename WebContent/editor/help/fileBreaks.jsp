<html>
	<head>
		<title>File Breaks</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	</head>
	
	<body>
	<p class="section_head">
	File Breaks
	</p>
	<p>
	Filebreaks identify where chapters start in the manuscript. 
	</p>
	<p class="section_subhead">
	EPUB
	</p>
	<p>
	An <i>epub</i> book is made up of a collection of files contained inside an <i>.epub</i> 
	container file.
	</p>
	<p>
	In an <i>.epub</i> book, new <i>html</i> files will be created for each filebreak. 
	</p>
	<p>
	If you do not identify any filebreaks, the entire narrative of your book will be contained in 
	a single <i>.html</i> file within the <i>.epub</i> container.
	<p class="section_subhead">
	PDF
	</p>
	<p>
	A <i>.pdf</i> is a digital representation of a hard-copy printed book.
	</p>
	<p>
	In a <i>.pdf</i> book, a new odd-numbered page will be started for each filebreak. 
	</P>
	<p>
	With no filebreaks defined, a <i>.pdf</i> file will show the entire narrative 
	as a single long run of text. 
	</p>
	<P>
	If the last page of the previous chapter is an odd-numbered
	page in the <i>.pdf</i>, a blank page will be added in order to make the new chapter start 
	on an odd-numbered page. This facilitates printing the book using double-sided printing 
	(each sheet of paper printed front and back), with each chapter starting on the reader's 
	right hand side.
	</p>
	<p class="section_subhead">
	Value of Adding Filebreaks
	</p>
	<p>
	Adding filebreaks can be time consuming, especially if you want to add <i>html</i> markup 
	to the heading text of each chapter. 
	</p>
	<p>
	Filebreaks are not required by the editing system.
	</p>
	<p>
	But if the original source narrative is divided into discrete units&mdash;chapters, 
	sections, parts&mdash;you should consider dividing your narrative in a similar fashion.
	</p>
	Each unit of the original will be contained in a separate file within the <i>epub</i> package. 
	Among other advantages, this will result in a table of contents that can be used to 
	navigate the book more easily than scrolling or searching through one long text. 
	</p>
	<p>
	A <i>.pdf</i> created by the <i><%= PropsManager.getIBookSystemName("full") %></i> 
	will not have hyperlinks, but there will be a table of contents listing page numbers for 
	chapter starts, and you can use <i>html</i> mark-up to display chapter titles in 
	larger type.   
	</p>
	<p>
	To add mark-up tags, go the <b>Final Proof</b> page of the editor.
	</p>
	<p>
	If the original text uses a common word to mark the beginning of each unit (<i>Chapter</i>, 
	for example), you can use this to locate the textblocks that contain the chapter headings, 
	and use those textblock numbers as filebreak markers.
	</p>
	<p>
	Search for the word <i>Chapter</i> in an <i>eBook</i> based on the <i>Dracula</i> template, 
	for example, to locate each textblock that starts a chapter.
	</p>
	<p>
	Text marked with &lt;h1&gt tags will be rendered in a <i>.pdf</i> file using 
	a 36pt font, &lt;h2&gt; tags with 22pt, &lt;h3&gt; with 14pt font.The &lt;h1&gt; 
	tag is used for the book title on the title page, and for titles on the copyright,
	preface, table of contents, and legal disclaimer pages. 
	</p>
	<p>
	See <a href="finalProofEdits.jsp">Final 
	Proof Edits</a> for information about  support for other <i>html</i> tags.
	</p>
	<p>
	For <i>.epub</i> books, heading styles (font size, alignment) are determined by your eBook
	reader app.
	</p>
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
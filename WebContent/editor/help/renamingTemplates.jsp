<html>
	<head>
		<title>Renaming Templates</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>

	<body>
	<p class="section_head">
	Renaming Templates
	</p>
	<p>
	The <b>Rename</b> button on the <i>Template Editor</i> page will rename the <i>.xml</i> 
	file that contains the template profile data (the collection of information that 
	defines the template). It will also assign a new template id to the copy. Both files 
	will reside in the filesystem on the web server. They will function as two separate, 
	and unconnected, templates. 
	</p>
	<p>
	The only valid reason for renaming a template is to preserve any modifications you may have
	made to a template, so that you can revert your collection of <i>eBook</i> templates to their 
	original states without losing your modifications.
	</p>
	<p>
	For example, if you make changes to the <i>Dracula</i> template (not a book profile, but 
	the template, in the template editor) and then revert your templates, your changes will be
	overwritten.
	</p>
	<p>
	If you rename your customized template to something like <i>dracula_2.xml</i> before using
	the <b>Revert</b> function, you will end up with two Dracula templates: the original 
	<i>dracula.xml</i> as defined by <i>eBook</i> and your copy, <i>dracula_2.xml</i>.
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	The filename will always end in <i>.xml</i>. If you don't specify the <i>.xml</i> extenstion 
	when you rename the file, it will be added when the copy is made.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
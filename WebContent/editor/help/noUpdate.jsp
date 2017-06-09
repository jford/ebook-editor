<html>
	<head>
		<title>Text Updates</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	
	<body>
	<p class="section_head">
	Text Updates
	</p>
	<p>
	When you click the <b>Stay</b>,</b> <b>Advance</b>, <b>Go Back One</b>, or 
	the green <b>Update and Go To Specified Block</b> button, 
	the text currently displayed in the <i>Text Block</i> pane is saved prior to the 
	requested action being taken. The template is updated with whatever changes you have 
	made. 
	</p>
	<p>
	When you click the yellow <b>Go to Specified Block (no update)</b> button, the action is taken without 
	saving the current state of the displayed text. The text saved in the template 
	profile is not updated.
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 10px;">
	Whenever you return to the template editor, the textblock pane will show the text that was 
	last edited prior to departing the editor.  If you have proceeded in a linear progression 
	from the beginning, you will be assured of having edited each text block. However, if you have 
	jumped around using the <i>Go to Specified Block</i>, you may jump over unedited blocks. You should 
	make a note of where you are in the edtiing process before jumping around, or you may a) miss
	some text blocks or b) find yourself having to advance through several already edited text 
	blocks.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
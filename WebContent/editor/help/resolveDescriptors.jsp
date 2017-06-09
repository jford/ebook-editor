<html>
	<head>
		<title>Resetting Manuscript Text</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	</head>
	
	<body>
	<p class="section_head">
	Resetting Manuscript Text
	<ul>
		<li><a href="resolveDescriptors.jsp#descriptors">Descriptors</a></li>
		<li><a href="resolveDescriptors.jsp#textEdits">Resetting the Manuscript/Text Edits</a></li>
		<li><a href="resolveDescriptors.jsp#templatEdits">Changing Descriptors in the Template</a></li>
	</ul>
	<p class="section_subhead"><a name="descriptors"></a>
	Descriptors
	</p>
	Descriptors are words or phrases in the original source text that help define a person or place 
	in the context of the original narrative but that may not be valid in your customized version 
	of that original story, and for which there is no automatic substitution that a computer can make
	without some user input. 
	</p>
	<p>
	For example, in a story originally set in England but moved to America in your customization, there may be a reference
	to British pounds that should be changed to American dollars. The <i>eBook</i> editor has no way of 
	knowing that the word <i>pounds</i> should be changed to <i>dollars</i> in this context, but the likelihood of
	such a change was high enough that this particular use of <i>pounds</i> was flagged as a possible 
	descriptor in the <i>eBook</i> template. 
	</p>
	<p>
	When you changed the region <i>England</i> to some other place, you triggered the <i>descriptor</i> flag,
	which you now need to resolve.
	</p>
	<p>
	You can either change the reference in the <b>Descriptor Text</b> pane and click the 
	<b>Set Change</b> button, or you can opt to keep the reference to <i>pounds</i> by clicking the 
	<b>Use Source</b> button. 
	</p>
	<p>
	To help you determine if or what change should be made, the <b>Context</b> column shows how the 
	term is used in the narrative. If the excerpt shown there is not sufficient, you can see the 
	entire textblock in which the term is used by clicking the <b>Show Full Text</b> button.
	</p>
	<p class="section_subhead"><a name="textEdits"></a>
	Resetting All Descriptors
	</p>
	<p>
	Click the <i>Reset Manuscript</i> button on the <i>build Epub Book</i> page to revert to an 
	unedited state. (If you have <a href="resolveDescriptors.jsp#templatEdits">changed</a> descriptor tags in the template, you must reset the 
	manuscript in order to see your changes.)
	</p>
	<p>
	Any changes you have made to the story by entering text in the <i>Final Proof Editor</i> will 
	be lost if you click the <b>Reset Manuscript</b> button.
	</p>
	<p>
	Clicking the <b>Reset Manuscript</b> button will restore <i>descriptor</i> flags to the manuscript text,
	forcing you to resolve all descriptors again.
	<p>
	<p class="section_subhead"><a name="templatEdits"></a>
	Changing Descriptors in the Template
	</p>
	<p>
	If you are editing descriptors in the template (an advanced function not normally recommended) 
	while resolving them in the manuscript at the same time, you may find that your changes do not 
	appear in the text when you return to the <i>Resolve Descriptors</i> page from the template editor.
	</p>
	<p>
	Because of the way descriptors are maintained in the data repository, it may be necessary to 
	<a href="resolveDescriptors.jsp#textEdits">reset</a> all descriptors to see the changes. 
	</p>   
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	Be sure your change is correct before committing it. Once you resolve a descriptor, the 
	<i>descriptor</i> flags are removed from the text and the descriptor will be removed from 
	the list displayed by the <i>Descriptor Resolution Editor</i>.
	</p>
	<p class="inline_text_note">
	You can still change the text in the final proofing stage, but you will have to either know which 
	textblock (by number) to edit, or use the full-text search button in the <i>Final Proof Editor</i> 
	to locate the passage.
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
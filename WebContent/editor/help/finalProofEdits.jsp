<html>
	<head>
		<title>Final Proof Edits</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
	</head>
	
	<body>
		<p class="section_head">
		Final Proof Edits
		</p>
		<ul>
			<li><a href="finalProofEdits.jsp#html">HTML Markup</a></li>
			<li><a href="finalProofEdits.jsp#paragraphs">Adding Paragraphs</a></li>
			<li><a href="finalProofEdits.jsp#ebooktags"><%= PropsManager.getIBookSystemName("short") %> Tags</a></li>
		</ul>
		<p>
		The <i>Final Proof Editor</i> is where you can go nuts&mdash;change anything you want to 
		anything you like. 
		</p>
		<p>
		You can edit the text of the narrative in any way you like.
		</p>
		<p>
		When you click one of the navigation buttons below the text-editing pane 
		(<b>Stay</b>, <b>Advance</b>, or <b>Go Back One</b>), whatever is contained 
		in the edit pane will replace the corresponding textblock in the book. (The 
		<b>Go To Specified Block</b> button is an exception&mdash;the text in the 
		edit pane will not be saved if you use this button.)
		</p>
		<p class="section_subhead"><a name="html"></a>
		HTML Markup
		</p>
		<p>
		The <%= PropsManager.getIBookSystemName("short") %> supports the following <i>html</i>
		tags:
		</p>
		<ul>
			<li>&lt;i&gt;...&lt;/i&gt;</li>
			<li>&lt;b&gt;...&lt;/b&gt;</li>
			<li>&lt;h1&gt;...&lt;/h1&gt;</li>
			<li>&lt;h2&gt;...&lt;/h2&gt;</li>
			<li>&lt;h3&gt;...&lt;/h3&gt;</li>
			<li>&lt;center&gt;...&lt;/center&gt;</li>
			<li>&lt;ul&gt;...&lt;/ul&gt;</li>
			<li>&lt;li&gt;...&lt;/li&gt;</li>
		</ul> 
		<p>
		Unsupported tags will be treated as plain text and the characters will appear in the 
		generated text.
		</p> 
		<p>
		Interpretation and rendering of <i>html</i> markup in an <i>.epub</i> file are tasks 
		done by the eBook reader app in which the book is displayed. Most reader apps should be able 
		to handle the small subset of <i>html</i> tags supported 
		in <%= PropsManager.getIBookSystemName() %> titles, but that is not guaranteed.  
		</p>
		<p class="section_subhead"><a name="paragraphs"></a>
		Adding Paragraphs
		</p>
		<p>
		What you see as paragraphs in the finished book are stored internally by the 
		<%= PropsManager.getIBookSystemName("short") %> as single lines of unformatted text, 
		with no line breaks. In the repository, these lines of text&mdash;the paragraphs of the 
		finished work&mdash;are called <i>textblocks</i>.
		</p>
		<p>
		You cannot add textblocks to a book manuscript.
		</p>
		<p>However, you can add paragraphs to the narrative, 
		by entering the text into an existing textblock on the <i>Final Proof</i> page.
		</p>
		<p>
		You can force the textblock to be displayed as multiple paragraphs by inserting line breaks
		into the existing text. (This will not create new textblocks but will place markers 
		into the text that cause it to be treated as multiple paragraphs.)
		</p>
		<p>
		Put the cursor where you want the new paragraph to begin, press the <b>Enter</b> key 
		and enter the text of your new paragraph, followed by another tap of 
		the <b>Enter</b> key.
		</p>
		<p>
		When the <i>eBook</i> editor processes the text, the line breaks will be processed 
		as paragraph markers, even though the text continues to be stored internally as a 
		single textblock.
		</p>
		<p class="section_subhead"><a name="ebooktags"></a>
		<%= PropsManager.getIBookSystemName("short") %> Tags
		</p>
		<p>
		The text may still contain some <%= PropsManager.getIBookSystemName("short") %> tags 
		(in the form of <i>&lt;ebookObjectId tagType&gt;...&lt;/tagType&gt;</i>), 
		for descriptors and aliases of characters and places for which there were no substitutions
		made.
		</p>
		<p>
		You should leave those tags in place. They will be removed by the 
		<%= PropsManager.getIBookSystemName("short") %> when the output file is generated.
		</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
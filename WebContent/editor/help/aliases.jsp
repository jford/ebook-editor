<html>
	<head>
		<title>Aliases</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	
	<body>
	<p class="section_head">
	Aliases
	</p>
	<p>
	An alias is an alternate means of address for a character or place, something other 
	than one of the object's defined names. 
	</p>
	In the novel <i>The Adventures of Tom Sawyer</i>, for example, Tom is sometimes 
	referred to as <i>the boy</i>, or <i>lad</i>, or even <i>TOM</i> when Aunt Polly is 
	yelling at or for him.
	<p>
	The <%= PropsManager.getIBookSystemName("short") %> is not able to provide substitutes 
	for these aliases without some assistance.
	</p>
	When you assign substitute characters, locations, or regions, a <i>Map Aliases</i> button
	will be displayed on the object assignment editor page, next to the names of those objects 
	that have aliases in the source narrative.
	</p>
	<p>
	Click this button to map these aliases to the names you want to use in their place&mdash;you 
	can either enter new text for each of the source aliases, or you can select from a list of aliases
	that you defined in your substitute profile.
	</p>
	<p>
	If you choose to add aliases to your character/location profiles, the aliases will available across all 
	<%= PropsManager.getIBookSystemName("short") %> book projects. If you enter text in the 
	<i>Map Aliases</i> form, it will only be available in the current project.
	</p>
	<p>
	When you map an alias to a template alias, pay attention to how the alias is used in the source 
	narrative. In <i>TomSawyer</i>, for example, <i>boy</i> is an alias for Tom. If you change the title
	character's gender, you may want to map this alias to <i>girl</i>, but any other form of
	address might not work as well, as when Tom is referred to as <i>the boy</i> in the original text.
	</p>
	<hr/>
	<p class="inline_text_note" style="margin-top: 3px">
	Each entry on the <i>Map Aliases</i> page will have its own <i>Set</i> and <i>Clear</i> buttons. These
	buttons operate only the item to which they are attached. You must click one for each alias you change
	on this page.
 
	</p>	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
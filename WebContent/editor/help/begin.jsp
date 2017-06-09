<html>
	<head>
		<title>Where to Begin?</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		
	</head>
	<%
	String userId = request.getRemoteUser();
	PropsManager propsMgr = new PropsManager(userId);
	int numTemplates = propsMgr.getTemplateCount();
	%>

	<body>
	<p class="section_head">
	Where to Begin?
	</p>
	<p>
	If this is your first visit to the <i><%= PropsManager.getIBookSystemName() %></i>, you should see 
	this list of resources:
	</p>
	<ul>
		<li>No books</li>
		<li>No characters</li>
		<li>No locations</li>
		<li><i><%= new Integer(numTemplates).toString() %></i> templates</li>
	</ul>
	<p>
	So where to begin? What to do first?
	</p>
	<p>
	You already have the collection of templates provided by the <%= propsMgr.getIBookSystemName() %>&mdash;the <i>eBook</i> profiles of the books 
	you can modify with the <i><%= PropsManager.getIBookSystemName("short") %></i>. 
	</p>
	<p>
	<i>Dracula</i>, for example. 
	</p>
	<p>You don't need to do anything with the templates. (The <b>Template</b> button opens 
	up the template editor, which you can use to create and modify your own templates. 
	But that's an advanced function, and you don't want to go there, not yet at least. You 
	may want to experiment with it at some later date, but keep in mind it is an advanced
	function requiring some specialized knowledge of how the Interactive Book application 
	works. You may never use it and still get all you want or need from the <i>eBook</i> 
	process.)
	</p>
	<p>
	You do need to create a book profile, however. This defines the set of data that will 
	be integrated into the template when you are ready to generate your <i>epub</i> file. 
	Click the <b>Book</b> button to create or edit the book profile.
	</p>
	<p>  
	Before you can assign substitute characters or places, of course, you'll need to define 
	character and location profiles&mdash;the people and places that you will use to 
	personalize a novel such as <i>Dracula</i>.
	</p>
	<p>
	Click the <b>Character</b> or <b>Location</b> button to open the editor of 
	choice. You can start with either once you're ready to begin creating profiles.
	</p>
	<p>
	But it might be a good idea to create the book profile first, to see what characters
	and places in the original manuscript are available for substitutions. Then you can 
	create custom profiles for only those characters and places you want customized.   
	</p>
	The order of activities then would be something like:
	<ol>
	<li>Create book profile</li>
	<li>Create character/place profiles</li>
	<li>Return to the book editor to assign substitutions</li>
	</ol>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
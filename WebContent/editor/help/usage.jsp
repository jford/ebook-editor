<html>
	<head>
		<title>Help on Usage</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		<%
		String appNameFull = PropsManager.getIBookSystemName("full");
		String appNameShort = PropsManager.getIBookSystemName("short");
		%>
	</head>
	
	<style>
	
	li.usageHelpList
	{
		color: #0000cc;
		font-style: italic;
		margin-top: 5px;
	}
	li.usageHelpText
	{
		list-style-type: none;
		font-size: .7em;
	}
	li.usageToc
	{
		font-family: Helvetica,Arial,sans-serif;
		list-style-type: none;
		font-size: .7em;
	}
	
	</style>

	<body>
	<p class="section_head">
	Help on Usage
	</p>
	<ul>
		<li class="usageToc"><a href="usage.jsp#erraticDataDisplay">Erratic Data Display</a></li>
		<li class="usageToc"><a href="usage.jsp#navigating">Navigating with Back Button, BookMarks and URLs</a></li>
	</ul>
	<ul>
		<li class="usageHelpList"><a name="erraticDataDisplay"></a>Erratic Data display?</li>
		<ul>
			<li class="usageHelpText">Web browsers store data from your web 
			viewing in order to speed things when you revisit a web 
			page. <br/><br/>While generally a good thing, maybe not so much when you're using a 
			web application like the <i>Interactive Book Editor</i>. Each page 
			you view should be built fresh by the server every time you view it, 
			not retrieved from storage. <br/><br/>If you experience 
			odd behavior&mdash;requests for information not working correctly even 
			though you're sure you're sure your input was correct, for example&mdash;try 
			clearing your browser's cache. (Each browser does this differently, and the
			way it's done can change with a browser update.)</li>
		</ul>
		<li class="usageHelpList"><a name="navigating"></a>Navigating With Back Button, Bookmarks and URLs</li>
		<ul>
			<li class="usageHelpText">The <i><%= PropsManager.getIBookSystemName() %></i> pages rely on ID tokens that are passed 
			among pages and internal processes. These tokens are not always available when you 
			move among pages using bookmarks, or you enter URLs directly. You should 
			only use the buttons and links on the <i><%= PropsManager.getIBookSystemName("short") %></i> pages. The browser <i>back</i>
			button may be helpful sometimes, but it does not carry updated data with it and you 
			may find it somewhat confusing when data you know you've changed is not reflected 
			on the page.</li>
		</ul>
	</ul>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
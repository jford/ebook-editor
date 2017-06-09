<html>
	<head>
		<title>Book Profiles</title>
		<script type="text/javascript" src="/iBook/scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*, com.iBook.datastore.characters.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*,com.iBook.datastore.manuscripts.*" %>
		<%
		String appNameFull = PropsManager.getIBookSystemName("full");
		String appNameShort = PropsManager.getIBookSystemName("short");
		%>
	</head>

	<body>
	<p class="section_head">
	Book Profiles
	</p>
	<p>
	A book profile is where the <i><%= appNameFull %></i> stores the data which associates a 
	template&mdash;the <i><%= appNameShort %></i> representation of the published manuscript that you want 
	to modify&mdash;with your custom profiles of people and places.
	</p>
	<p>
	Once you create the book profile, the book editor will provide access to character, 
	location and region assignment pages, which is where you select the substitute profiles to be used 
	in the final output.  
	</p>
	<p>
	Use the <i><%= appNameShort %></i> character and location editors to create profiles for the people 
	and places you want to use as substitutes.
	</p>
	<p>
	There is no firmly enforced order in which you create these profiles. You can start 
	with any of the editors&mdash;character, location, book&mdash;you want.
	</p>
	<p>
	However, if you start by creating a book profile, you can use the assignment pages to see which
	characters and places in the original work are available for substitutes, then you can 
	create the substitute profiles that you will need. 
	</p>
	<p>
	When you have created profiles for the people and places you want to insert into the published
	manuscript, return to the book editor assignment pages to make the substitutions. 
	</p>
	
	<div class="send_back" style="left: 70%">
		<div class="send_back_pair">
			<div class="send_back_label">View</div>
			<div class="send_back_target"><a href="/iBook/editor/help/helpFilesIndex.jsp">Help Files Index</a></div>
		</div>
	</div>
	</body>
</html>
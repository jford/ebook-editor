<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.books.*,com.iBook.datastore.manuscripts.*" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		int numBooks = propsMgr.getBookCount();
		String bookId = request.getParameter("bookId");
		BookProfile profile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		String templateId = profile.getTemplateId();
		TemplateProfile templateProfile = propsMgr.getTemplateProfile(templateId);
		
		String pubdate = templateProfile.getPubdate();
		String title = propsMgr.getBookTitle(bookId);
		String author = propsMgr.getAuthorName(bookId);
		String sourceBook = propsMgr.getBookSourceTitle(bookId);
		String sourceAuthor = propsMgr.getSourceAuthorName(bookId);
//		Vector<String> characters = propsMgr.getCharactersForBook(bookId);
//		Vector<String> locations = propsMgr.getLocationsForBook(bookId);
		%>
	</head>
	<body>
		<%@include file="header.jsp"%>
		<p class="section_head">
			Book Editor
		</p>
		<form name="editBookTitleForm" action="../ManageProfile" method="post">
		Title: <input type="text" name="bookTitle" size="50" value="<%= title %>"/>
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.BOOK %>"/>
		<input type="hidden" name="bookId" value="<%= bookId %>"/>
		<input type="hidden" name="userId" value="<%= userId %>" />
		<input type="submit" name="editTitleSubmit" value="Change Title"/>	
		</form>
		<%
		if(sourceBook.length() > 0 && sourceBook.compareTo("undefined") != 0)
		{
		%>
		<p>
		A retelling of <%  if(sourceAuthor.length() > 0)
		{
		%>
		<%= sourceAuthor + "'s" %> 
		<%
		}
		else
		{
		%>
		the 
		<%
		}
		if(pubdate.length() > 0)
		{
		%>
		<%= pubdate %>
		<%
		}
		%>
		manuscript, <i><%= sourceBook %>.</i>
		</p>
		<%
		}
		%>
		<hr/>
		<p>
		By <%= author %>. <input type="button" name="editBookAuthorName" value="Edit Author Name" onclick="document.location='editAuthorName.jsp?bookId=<%= bookId %>'"/>
		</p>
		<hr/>
		 <input type="button" name="doCharacterAssignment" value="Edit Character Assignments" onclick="document.location='doCharacterAssignments.jsp?bookId=<%= bookId %>'"/>
		 <hr/>
		 <input type="button" name="doLocationAssignment" value="Edit Location Assignments" onclick="document.location='doLocationAssignments.jsp?bookId=<%= bookId %>'"/>
		 <input type="button" name="help" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
		 <hr/>
		 <input type="button" name="doGeolocAssignment" value="Edit Region Assignments" onclick="document.location='doGeolocaleAssignments.jsp?bookId=<%= bookId %>'"/>
 		 <input type="button" name="help" value="?" onclick="displayHelp('locationVsRegion.jsp')" />
		 
		
	<div class="send_back">
		<div class="send_back_label">
			<div class="send_back_pair">
				<div class="send_back_label">Go to</div>
				<div class="send_back_target"><a href="/iBook/editor/editBooks.jsp">Edit Books</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label"></div>
				<div class="send_back_target"><a href="verifyData.jsp?bookId=<%= bookId %>">Generate eBook</a></div>
			</div>
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
		</body>
		</html>
		


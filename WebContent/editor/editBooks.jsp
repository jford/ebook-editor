<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
      
<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*" %>
		<%
		boolean stopForDebugging = false;
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		int numBooks = propsMgr.getBookCount();
		String username = request.getRemoteUser();
		%>
	</head>
	<body>
		<%@include file="header.jsp"%>
		<p class="section_head">
			Book Editor
		</p>

<%
	if(numBooks > 0)
	{
%>

		<p>
		What would you like to do?
		</p>
<%
	} // end of if(num > 0)
%>
		<p>
		<input type="button" name="newBook" value="Create New Book" onclick="document.location='createBook.jsp'"/>
		<input type="button" value="?" onclick="displayHelp('createBookHelp.jsp')" />
		</p>
<%
	String book_code = "";
	String book_name = "";
	if(numBooks > 0)
	{
%>
		<p>
		...or select a book from the list and edit its properties:
		</p>
		<form name="chooseBook" action="../ManageProfile" method="post">
			<select name="bookId" id="bookId" onchange="bookId.options[selectedIndex].value">
			
<% 
// get list of book ids
	Vector<String> bookList = propsMgr.getIdList(PropsManager.ObjType.BOOK);
	Iterator<String> iBooks = bookList.iterator();
	while(iBooks.hasNext())
	{
		book_code = (String)iBooks.next();
		book_name = propsMgr.getBookTitle(book_code);
%>
				<option value="<%= book_code %>">
					<%= book_name %>
				</option>
<% 
	} // end of while{}
%>
			</select>
		<input type="submit" name="editBook" value="Edit" />
        <input type="submit" name="deleteBook" value="Delete..." onclick="return confirmDelete();" />
        <input type="button" value="?" onclick="displayHelp('profileIds.jsp')" />
        <input type="hidden" name="profileType" value="<%= PropsManager.ObjType.BOOK %>"/>
		<input type="hidden" name="userId" value="<%= userId %>" />
	</form>
<%
	} // end of if(num > 0)
%>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to </div>
				<div class="send_back_target"><a href="index.jsp">start</a></div>
			</div>
		</div>
	</body>
</html>


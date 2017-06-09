<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*,com.iBook.datastore.books.*,com.iBook.datastore.locations.*" %>
		
	</head>
	<body>
		<%@ include file="header.jsp" %>
		<%
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		String bookId = request.getParameter("bookId");
		BookProfile bookProfile = (BookProfile)propsMgr.getProfile(PropsManager.ObjType.BOOK, bookId);
		%>
		<p class="section_head">
			Edit <i><%= PropsManager.getIBookSystemName("short") %></i> author name for project <%= bookProfile.getTitle() %>.
		</p>
		<form name="resetAuthorName" action="../ManageProfile" method="post" >
		<table>
			<tr><td>Prefix:</td><td><input type="text" name="namePrefix" size="50" value="<%= bookProfile.getNamePrefix() %>" /></td></tr>
			<tr><td>First:</td><td><input type="text" name="nameFirst" size="50" value="<%= bookProfile.getNameFirst() %>" /></td></tr>
			<tr><td>Middle:</td><td><input type="text" name="nameMiddle" size="50" value="<%= bookProfile.getNameMiddle() %>" /></td></tr>
			<tr><td>Last:</td><td><input type="text" name="nameLast" size="50" value="<%= bookProfile.getNameLast() %>" /></td></tr>
			<tr><td>Suffix:</td><td><input type="text" name="nameSuffix" size="50" value="<%= bookProfile.getNameSuffix() %>" /></td></tr>
		</table>
		<input type="submit" name="resetAuthorNameSubmit" value="Change Names" />
		<input type="hidden" name="profileType" value="<%= PropsManager.ObjType.BOOK %>" />
		<input type="hidden" name="bookId" value="<%= bookId %>" />
		<input type="hidden" name="userId" value="<%= userId %>" />
		</form>
		<p>
		</p>
		<div class="send_back">
	          Return to <a href="index.jsp">start</a>
		</div>
	</body>
</html>

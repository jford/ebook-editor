<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.utilities.*,com.iBook.datastore.*,com.iBook.datastore.books.*,com.iBook.datastore.manuscripts.*" %>
		
<%
		String outputFile = request.getParameter("outputFile");
		String useCurrentMappings = request.getParameter("useCurrentMappings");
		String userId = request.getRemoteUser();
		PropsManager propsMgr = new PropsManager(userId);
		boolean outputFileReady = outputFile != null && outputFile.length() > 0;
		BookProfile bookProfile = null;
		int numBooks = propsMgr.getBookCount();
		String qty = numBooks == 1 ? "" : "s";
		String bookName = "";
		String bookId = "";
		
%>
	<style>
		#deleteMS
		{
			height: 3em;
			width: 10em;
			font-size: .6em;
			padding: .3em;
			padding-top: .1em;
		}
	</style>
	</head>
	<%
	if(outputFileReady)
	{
	%>
		<body onload="document.location='/iBook/resources/users/<%= userId %>/resources/<%= outputFile %>'">
	<%
	}
	else
	{
	%>
		<body>
	<%
	}
	%>
		<%@ include file="header.jsp" %>
		<p class="section_head">
			Generate eBook
		</p>
		<p>
		You have <%= numBooks %> book profile<%= qty  %>: 
		</p>
		<form action="" method="post">
			<select name="bookId" id="id" onchange="bookId.options[selectedIndex].value">
			
			<% 
			// get list of book ids
				Vector<String> bookList = propsMgr.getIdList(PropsManager.ObjType.BOOK);
				Iterator<String> iBooks = bookList.iterator();
				while(iBooks.hasNext())
				{
					bookId = (String)iBooks.next();
					bookName = propsMgr.getBookTitle(bookId);
			%>
							<option value="<%= bookId %>">
								<%= bookName %>
							</option>
			<% 
				} // end of while{}
			%>
			</select>
			<input type="button" name="selectBook" value="Select" onclick="bookId = getSelection();document.location='verifyData.jsp?bookId=' + bookId.value" />
			<input type="hidden" name="userId" value="<%= userId %>" />
			<input type="hidden" name="useCurrentMappings" value="<%= useCurrentMappings %>" />
		</form>
				
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">Start</a></div>
			</div>
		</div>
	</body>
</html>
		
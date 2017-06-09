<html>
	<head>
		<title>Interactive Books</title>
		<script type="text/javascript" src="../scripts/base_page_header.js"></script>
		<%@ page import="java.util.*,java.text.Collator,com.iBook.datastore.*" %>
		
<%
		String templateId = request.getParameter("templateId");
		PropsManager propsMgr = new PropsManager(request.getRemoteUser());
		String templateName = "templateName";
%>
	</head>
	<body>
		<%@ include file="header.jsp" %>
	<%
	%>
		<p class="section_head">
			Add Character to <%= templateName %> <i>iBook</i> template 
		</p>
		<div class="send_back">
			<div class="send_back_pair">
				<div class="send_back_label">Return to</div>
				<div class="send_back_target"><a href="index.jsp">start</a></div>
			</div>
		</div>
	</body>
</html>
		